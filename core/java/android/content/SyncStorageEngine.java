<<<<<<< HEAD
/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.content;

import com.android.internal.os.AtomicFile;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FastXmlSerializer;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.accounts.Account;
import android.accounts.AccountAndUser;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.util.SparseArray;
import android.util.Xml;
import android.util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.TimeZone;
import java.util.List;

/**
 * Singleton that tracks the sync data and overall sync
 * history on the device.
 *
 * @hide
 */
public class SyncStorageEngine extends Handler {

    private static final String TAG = "SyncManager";
    private static final boolean DEBUG_FILE = false;

    private static final String XML_ATTR_NEXT_AUTHORITY_ID = "nextAuthorityId";
    private static final String XML_ATTR_LISTEN_FOR_TICKLES = "listen-for-tickles";
    private static final String XML_ATTR_SYNC_RANDOM_OFFSET = "offsetInSeconds";
    private static final String XML_ATTR_ENABLED = "enabled";
    private static final String XML_ATTR_USER = "user";
    private static final String XML_TAG_LISTEN_FOR_TICKLES = "listenForTickles";

    private static final long DEFAULT_POLL_FREQUENCY_SECONDS = 60 * 60 * 24; // One day

    // @VisibleForTesting
    static final long MILLIS_IN_4WEEKS = 1000L * 60 * 60 * 24 * 7 * 4;

    /** Enum value for a sync start event. */
    public static final int EVENT_START = 0;

    /** Enum value for a sync stop event. */
    public static final int EVENT_STOP = 1;

    // TODO: i18n -- grab these out of resources.
    /** String names for the sync event types. */
    public static final String[] EVENTS = { "START", "STOP" };

    /** Enum value for a server-initiated sync. */
    public static final int SOURCE_SERVER = 0;

    /** Enum value for a local-initiated sync. */
    public static final int SOURCE_LOCAL = 1;
    /**
     * Enum value for a poll-based sync (e.g., upon connection to
     * network)
     */
    public static final int SOURCE_POLL = 2;

    /** Enum value for a user-initiated sync. */
    public static final int SOURCE_USER = 3;

    /** Enum value for a periodic sync. */
    public static final int SOURCE_PERIODIC = 4;

    public static final long NOT_IN_BACKOFF_MODE = -1;

    public static final Intent SYNC_CONNECTION_SETTING_CHANGED_INTENT =
            new Intent("com.android.sync.SYNC_CONN_STATUS_CHANGED");

    // TODO: i18n -- grab these out of resources.
    /** String names for the sync source types. */
    public static final String[] SOURCES = { "SERVER",
                                             "LOCAL",
                                             "POLL",
                                             "USER",
                                             "PERIODIC" };

    // The MESG column will contain one of these or one of the Error types.
    public static final String MESG_SUCCESS = "success";
    public static final String MESG_CANCELED = "canceled";

    public static final int MAX_HISTORY = 100;

    private static final int MSG_WRITE_STATUS = 1;
    private static final long WRITE_STATUS_DELAY = 1000*60*10; // 10 minutes

    private static final int MSG_WRITE_STATISTICS = 2;
    private static final long WRITE_STATISTICS_DELAY = 1000*60*30; // 1/2 hour

    private static final boolean SYNC_ENABLED_DEFAULT = false;

    // the version of the accounts xml file format
    private static final int ACCOUNTS_VERSION = 2;

    private static HashMap<String, String> sAuthorityRenames;

    static {
        sAuthorityRenames = new HashMap<String, String>();
        sAuthorityRenames.put("contacts", "com.android.contacts");
        sAuthorityRenames.put("calendar", "com.android.calendar");
    }

    public static class PendingOperation {
        final Account account;
        final int userId;
        final int syncSource;
        final String authority;
        final Bundle extras;        // note: read-only.
        final boolean expedited;

        int authorityId;
        byte[] flatExtras;

        PendingOperation(Account account, int userId, int source,
                String authority, Bundle extras, boolean expedited) {
            this.account = account;
            this.userId = userId;
            this.syncSource = source;
            this.authority = authority;
            this.extras = extras != null ? new Bundle(extras) : extras;
            this.expedited = expedited;
            this.authorityId = -1;
        }

        PendingOperation(PendingOperation other) {
            this.account = other.account;
            this.userId = other.userId;
            this.syncSource = other.syncSource;
            this.authority = other.authority;
            this.extras = other.extras;
            this.authorityId = other.authorityId;
            this.expedited = other.expedited;
        }
    }

    static class AccountInfo {
        final AccountAndUser accountAndUser;
        final HashMap<String, AuthorityInfo> authorities =
                new HashMap<String, AuthorityInfo>();

        AccountInfo(AccountAndUser accountAndUser) {
            this.accountAndUser = accountAndUser;
        }
    }

    public static class AuthorityInfo {
        final Account account;
        final int userId;
        final String authority;
        final int ident;
        boolean enabled;
        int syncable;
        long backoffTime;
        long backoffDelay;
        long delayUntil;
        final ArrayList<Pair<Bundle, Long>> periodicSyncs;

        /**
         * Copy constructor for making deep-ish copies. Only the bundles stored
         * in periodic syncs can make unexpected changes.
         *
         * @param toCopy AuthorityInfo to be copied.
         */
        AuthorityInfo(AuthorityInfo toCopy) {
            account = toCopy.account;
            userId = toCopy.userId;
            authority = toCopy.authority;
            ident = toCopy.ident;
            enabled = toCopy.enabled;
            syncable = toCopy.syncable;
            backoffTime = toCopy.backoffTime;
            backoffDelay = toCopy.backoffDelay;
            delayUntil = toCopy.delayUntil;
            periodicSyncs = new ArrayList<Pair<Bundle, Long>>();
            for (Pair<Bundle, Long> sync : toCopy.periodicSyncs) {
                // Still not a perfect copy, because we are just copying the mappings.
                periodicSyncs.add(Pair.create(new Bundle(sync.first), sync.second));
            }
        }

        AuthorityInfo(Account account, int userId, String authority, int ident) {
            this.account = account;
            this.userId = userId;
            this.authority = authority;
            this.ident = ident;
            enabled = SYNC_ENABLED_DEFAULT;
            syncable = -1; // default to "unknown"
            backoffTime = -1; // if < 0 then we aren't in backoff mode
            backoffDelay = -1; // if < 0 then we aren't in backoff mode
            periodicSyncs = new ArrayList<Pair<Bundle, Long>>();
            periodicSyncs.add(Pair.create(new Bundle(), DEFAULT_POLL_FREQUENCY_SECONDS));
        }
    }

    public static class SyncHistoryItem {
        int authorityId;
        int historyId;
        long eventTime;
        long elapsedTime;
        int source;
        int event;
        long upstreamActivity;
        long downstreamActivity;
        String mesg;
        boolean initialization;
    }

    public static class DayStats {
        public final int day;
        public int successCount;
        public long successTime;
        public int failureCount;
        public long failureTime;

        public DayStats(int day) {
            this.day = day;
        }
    }

    interface OnSyncRequestListener {
        /**
         * Called when a sync is needed on an account(s) due to some change in state.
         * @param account
         * @param userId
         * @param authority
         * @param extras
         */
        public void onSyncRequest(Account account, int userId, String authority, Bundle extras);
    }

    // Primary list of all syncable authorities.  Also our global lock.
    private final SparseArray<AuthorityInfo> mAuthorities =
            new SparseArray<AuthorityInfo>();

    private final HashMap<AccountAndUser, AccountInfo> mAccounts
            = new HashMap<AccountAndUser, AccountInfo>();

    private final ArrayList<PendingOperation> mPendingOperations =
            new ArrayList<PendingOperation>();

    private final SparseArray<ArrayList<SyncInfo>> mCurrentSyncs
            = new SparseArray<ArrayList<SyncInfo>>();

    private final SparseArray<SyncStatusInfo> mSyncStatus =
            new SparseArray<SyncStatusInfo>();

    private final ArrayList<SyncHistoryItem> mSyncHistory =
            new ArrayList<SyncHistoryItem>();

    private final RemoteCallbackList<ISyncStatusObserver> mChangeListeners
            = new RemoteCallbackList<ISyncStatusObserver>();

    private int mNextAuthorityId = 0;

    // We keep 4 weeks of stats.
    private final DayStats[] mDayStats = new DayStats[7*4];
    private final Calendar mCal;
    private int mYear;
    private int mYearInDays;

    private final Context mContext;

    private static volatile SyncStorageEngine sSyncStorageEngine = null;

    private int mSyncRandomOffset;

    /**
     * This file contains the core engine state: all accounts and the
     * settings for them.  It must never be lost, and should be changed
     * infrequently, so it is stored as an XML file.
     */
    private final AtomicFile mAccountInfoFile;

    /**
     * This file contains the current sync status.  We would like to retain
     * it across boots, but its loss is not the end of the world, so we store
     * this information as binary data.
     */
    private final AtomicFile mStatusFile;

    /**
     * This file contains sync statistics.  This is purely debugging information
     * so is written infrequently and can be thrown away at any time.
     */
    private final AtomicFile mStatisticsFile;

    /**
     * This file contains the pending sync operations.  It is a binary file,
     * which must be updated every time an operation is added or removed,
     * so we have special handling of it.
     */
    private final AtomicFile mPendingFile;
    private static final int PENDING_FINISH_TO_WRITE = 4;
    private int mNumPendingFinished = 0;

    private int mNextHistoryId = 0;
    private SparseArray<Boolean> mMasterSyncAutomatically = new SparseArray<Boolean>();

    private OnSyncRequestListener mSyncRequestListener;

    private SyncStorageEngine(Context context, File dataDir) {
        mContext = context;
        sSyncStorageEngine = this;

        mCal = Calendar.getInstance(TimeZone.getTimeZone("GMT+0"));

        File systemDir = new File(dataDir, "system");
        File syncDir = new File(systemDir, "sync");
        syncDir.mkdirs();
        mAccountInfoFile = new AtomicFile(new File(syncDir, "accounts.xml"));
        mStatusFile = new AtomicFile(new File(syncDir, "status.bin"));
        mPendingFile = new AtomicFile(new File(syncDir, "pending.bin"));
        mStatisticsFile = new AtomicFile(new File(syncDir, "stats.bin"));

        readAccountInfoLocked();
        readStatusLocked();
        readPendingOperationsLocked();
        readStatisticsLocked();
        readAndDeleteLegacyAccountInfoLocked();
        writeAccountInfoLocked();
        writeStatusLocked();
        writePendingOperationsLocked();
        writeStatisticsLocked();
    }

    public static SyncStorageEngine newTestInstance(Context context) {
        return new SyncStorageEngine(context, context.getFilesDir());
=======
package android.content;

import android.Manifest;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.Sync;
import android.text.TextUtils;
import android.util.Config;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * ContentProvider that tracks the sync data and overall sync
 * history on the device.
 * 
 * @hide
 */
public class SyncStorageEngine {
    private static final String TAG = "SyncManager";

    private static final String DATABASE_NAME = "syncmanager.db";
    private static final int DATABASE_VERSION = 10;

    private static final int STATS = 1;
    private static final int STATS_ID = 2;
    private static final int HISTORY = 3;
    private static final int HISTORY_ID = 4;
    private static final int SETTINGS = 5;
    private static final int PENDING = 7;
    private static final int ACTIVE = 8;
    private static final int STATUS = 9;

    private static final UriMatcher sURLMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    private static final HashMap<String,String> HISTORY_PROJECTION_MAP;
    private static final HashMap<String,String> PENDING_PROJECTION_MAP;
    private static final HashMap<String,String> ACTIVE_PROJECTION_MAP;
    private static final HashMap<String,String> STATUS_PROJECTION_MAP;

    private final Context mContext;
    private final SQLiteOpenHelper mOpenHelper;
    private static SyncStorageEngine sSyncStorageEngine = null;

    static {
        sURLMatcher.addURI("sync", "stats", STATS);
        sURLMatcher.addURI("sync", "stats/#", STATS_ID);
        sURLMatcher.addURI("sync", "history", HISTORY);
        sURLMatcher.addURI("sync", "history/#", HISTORY_ID);
        sURLMatcher.addURI("sync", "settings", SETTINGS);
        sURLMatcher.addURI("sync", "status", STATUS);
        sURLMatcher.addURI("sync", "active", ACTIVE);
        sURLMatcher.addURI("sync", "pending", PENDING);

        HashMap<String,String> map;
        PENDING_PROJECTION_MAP = map = new HashMap<String,String>();
        map.put(Sync.History._ID, Sync.History._ID);
        map.put(Sync.History.ACCOUNT, Sync.History.ACCOUNT);
        map.put(Sync.History.AUTHORITY, Sync.History.AUTHORITY);

        ACTIVE_PROJECTION_MAP = map = new HashMap<String,String>();
        map.put(Sync.History._ID, Sync.History._ID);
        map.put(Sync.History.ACCOUNT, Sync.History.ACCOUNT);
        map.put(Sync.History.AUTHORITY, Sync.History.AUTHORITY);
        map.put("startTime", "startTime");

        HISTORY_PROJECTION_MAP = map = new HashMap<String,String>();
        map.put(Sync.History._ID, "history._id as _id");
        map.put(Sync.History.ACCOUNT, "stats.account as account");
        map.put(Sync.History.AUTHORITY, "stats.authority as authority");
        map.put(Sync.History.EVENT, Sync.History.EVENT);
        map.put(Sync.History.EVENT_TIME, Sync.History.EVENT_TIME);
        map.put(Sync.History.ELAPSED_TIME, Sync.History.ELAPSED_TIME);
        map.put(Sync.History.SOURCE, Sync.History.SOURCE);
        map.put(Sync.History.UPSTREAM_ACTIVITY, Sync.History.UPSTREAM_ACTIVITY);
        map.put(Sync.History.DOWNSTREAM_ACTIVITY, Sync.History.DOWNSTREAM_ACTIVITY);
        map.put(Sync.History.MESG, Sync.History.MESG);

        STATUS_PROJECTION_MAP = map = new HashMap<String,String>();
        map.put(Sync.Status._ID, "status._id as _id");
        map.put(Sync.Status.ACCOUNT, "stats.account as account");
        map.put(Sync.Status.AUTHORITY, "stats.authority as authority");
        map.put(Sync.Status.TOTAL_ELAPSED_TIME, Sync.Status.TOTAL_ELAPSED_TIME);
        map.put(Sync.Status.NUM_SYNCS, Sync.Status.NUM_SYNCS);
        map.put(Sync.Status.NUM_SOURCE_LOCAL, Sync.Status.NUM_SOURCE_LOCAL);
        map.put(Sync.Status.NUM_SOURCE_POLL, Sync.Status.NUM_SOURCE_POLL);
        map.put(Sync.Status.NUM_SOURCE_SERVER, Sync.Status.NUM_SOURCE_SERVER);
        map.put(Sync.Status.NUM_SOURCE_USER, Sync.Status.NUM_SOURCE_USER);
        map.put(Sync.Status.LAST_SUCCESS_SOURCE, Sync.Status.LAST_SUCCESS_SOURCE);
        map.put(Sync.Status.LAST_SUCCESS_TIME, Sync.Status.LAST_SUCCESS_TIME);
        map.put(Sync.Status.LAST_FAILURE_SOURCE, Sync.Status.LAST_FAILURE_SOURCE);
        map.put(Sync.Status.LAST_FAILURE_TIME, Sync.Status.LAST_FAILURE_TIME);
        map.put(Sync.Status.LAST_FAILURE_MESG, Sync.Status.LAST_FAILURE_MESG);
        map.put(Sync.Status.PENDING, Sync.Status.PENDING);
    }

    private static final String[] STATS_ACCOUNT_PROJECTION =
            new String[] { Sync.Stats.ACCOUNT };

    private static final int MAX_HISTORY_EVENTS_TO_KEEP = 5000;

    private static final String SELECT_INITIAL_FAILURE_TIME_QUERY_STRING = ""
            + "SELECT min(a) "
            + "FROM ("
            + "  SELECT initialFailureTime AS a "
            + "  FROM status "
            + "  WHERE stats_id=? AND a IS NOT NULL "
            + "    UNION "
            + "  SELECT ? AS a"
            + " )";

    private SyncStorageEngine(Context context) {
        mContext = context;
        mOpenHelper = new SyncStorageEngine.DatabaseHelper(context);
        sSyncStorageEngine = this;
    }

    public static SyncStorageEngine newTestInstance(Context context) {
        return new SyncStorageEngine(context);
>>>>>>> 54b6cfa... Initial Contribution
    }

    public static void init(Context context) {
        if (sSyncStorageEngine != null) {
<<<<<<< HEAD
            return;
        }
        // This call will return the correct directory whether Encrypted File Systems is
        // enabled or not.
        File dataDir = Environment.getSecureDataDirectory();
        sSyncStorageEngine = new SyncStorageEngine(context, dataDir);
=======
            throw new IllegalStateException("already initialized");
        }
        sSyncStorageEngine = new SyncStorageEngine(context);
>>>>>>> 54b6cfa... Initial Contribution
    }

    public static SyncStorageEngine getSingleton() {
        if (sSyncStorageEngine == null) {
            throw new IllegalStateException("not initialized");
        }
        return sSyncStorageEngine;
    }

<<<<<<< HEAD
    protected void setOnSyncRequestListener(OnSyncRequestListener listener) {
        if (mSyncRequestListener == null) {
            mSyncRequestListener = listener;
        }
    }

    @Override public void handleMessage(Message msg) {
        if (msg.what == MSG_WRITE_STATUS) {
            synchronized (mAuthorities) {
                writeStatusLocked();
            }
        } else if (msg.what == MSG_WRITE_STATISTICS) {
            synchronized (mAuthorities) {
                writeStatisticsLocked();
            }
        }
    }

    public int getSyncRandomOffset() {
        return mSyncRandomOffset;
    }

    public void addStatusChangeListener(int mask, ISyncStatusObserver callback) {
        synchronized (mAuthorities) {
            mChangeListeners.register(callback, mask);
        }
    }

    public void removeStatusChangeListener(ISyncStatusObserver callback) {
        synchronized (mAuthorities) {
            mChangeListeners.unregister(callback);
        }
    }

    private void reportChange(int which) {
        ArrayList<ISyncStatusObserver> reports = null;
        synchronized (mAuthorities) {
            int i = mChangeListeners.beginBroadcast();
            while (i > 0) {
                i--;
                Integer mask = (Integer)mChangeListeners.getBroadcastCookie(i);
                if ((which & mask.intValue()) == 0) {
                    continue;
                }
                if (reports == null) {
                    reports = new ArrayList<ISyncStatusObserver>(i);
                }
                reports.add(mChangeListeners.getBroadcastItem(i));
            }
            mChangeListeners.finishBroadcast();
        }

        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "reportChange " + which + " to: " + reports);
        }

        if (reports != null) {
            int i = reports.size();
            while (i > 0) {
                i--;
                try {
                    reports.get(i).onStatusChanged(which);
                } catch (RemoteException e) {
                    // The remote callback list will take care of this for us.
                }
            }
        }
    }

    public boolean getSyncAutomatically(Account account, int userId, String providerName) {
        synchronized (mAuthorities) {
            if (account != null) {
                AuthorityInfo authority = getAuthorityLocked(account, userId, providerName,
                        "getSyncAutomatically");
                return authority != null && authority.enabled;
            }

            int i = mAuthorities.size();
            while (i > 0) {
                i--;
                AuthorityInfo authority = mAuthorities.valueAt(i);
                if (authority.authority.equals(providerName)
                        && authority.userId == userId
                        && authority.enabled) {
                    return true;
                }
            }
            return false;
        }
    }

    public void setSyncAutomatically(Account account, int userId, String providerName,
            boolean sync) {
        Log.d(TAG, "setSyncAutomatically: " + /* account + */" provider " + providerName
                + ", user " + userId + " -> " + sync);
        synchronized (mAuthorities) {
            AuthorityInfo authority = getOrCreateAuthorityLocked(account, userId, providerName, -1,
                    false);
            if (authority.enabled == sync) {
                Log.d(TAG, "setSyncAutomatically: already set to " + sync + ", doing nothing");
                return;
            }
            authority.enabled = sync;
            writeAccountInfoLocked();
        }

        if (sync) {
            requestSync(account, userId, providerName, new Bundle());
        }
        reportChange(ContentResolver.SYNC_OBSERVER_TYPE_SETTINGS);
    }

    public int getIsSyncable(Account account, int userId, String providerName) {
        synchronized (mAuthorities) {
            if (account != null) {
                AuthorityInfo authority = getAuthorityLocked(account, userId, providerName,
                        "getIsSyncable");
                if (authority == null) {
                    return -1;
                }
                return authority.syncable;
            }

            int i = mAuthorities.size();
            while (i > 0) {
                i--;
                AuthorityInfo authority = mAuthorities.valueAt(i);
                if (authority.authority.equals(providerName)) {
                    return authority.syncable;
                }
            }
            return -1;
        }
    }

    public void setIsSyncable(Account account, int userId, String providerName, int syncable) {
        if (syncable > 1) {
            syncable = 1;
        } else if (syncable < -1) {
            syncable = -1;
        }
        Log.d(TAG, "setIsSyncable: " + account + ", provider " + providerName
                + ", user " + userId + " -> " + syncable);
        synchronized (mAuthorities) {
            AuthorityInfo authority = getOrCreateAuthorityLocked(account, userId, providerName, -1,
                    false);
            if (authority.syncable == syncable) {
                Log.d(TAG, "setIsSyncable: already set to " + syncable + ", doing nothing");
                return;
            }
            authority.syncable = syncable;
            writeAccountInfoLocked();
        }

        if (syncable > 0) {
            requestSync(account, userId, providerName, new Bundle());
        }
        reportChange(ContentResolver.SYNC_OBSERVER_TYPE_SETTINGS);
    }

    public Pair<Long, Long> getBackoff(Account account, int userId, String providerName) {
        synchronized (mAuthorities) {
            AuthorityInfo authority = getAuthorityLocked(account, userId, providerName,
                    "getBackoff");
            if (authority == null || authority.backoffTime < 0) {
                return null;
            }
            return Pair.create(authority.backoffTime, authority.backoffDelay);
        }
    }

    public void setBackoff(Account account, int userId, String providerName,
            long nextSyncTime, long nextDelay) {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "setBackoff: " + account + ", provider " + providerName
                    + ", user " + userId
                    + " -> nextSyncTime " + nextSyncTime + ", nextDelay " + nextDelay);
        }
        boolean changed = false;
        synchronized (mAuthorities) {
            if (account == null || providerName == null) {
                for (AccountInfo accountInfo : mAccounts.values()) {
                    if (account != null && !account.equals(accountInfo.accountAndUser.account)
                            && userId != accountInfo.accountAndUser.userId) {
                        continue;
                    }
                    for (AuthorityInfo authorityInfo : accountInfo.authorities.values()) {
                        if (providerName != null && !providerName.equals(authorityInfo.authority)) {
                            continue;
                        }
                        if (authorityInfo.backoffTime != nextSyncTime
                                || authorityInfo.backoffDelay != nextDelay) {
                            authorityInfo.backoffTime = nextSyncTime;
                            authorityInfo.backoffDelay = nextDelay;
                            changed = true;
                        }
                    }
                }
            } else {
                AuthorityInfo authority =
                        getOrCreateAuthorityLocked(account, userId, providerName, -1 /* ident */,
                                true);
                if (authority.backoffTime == nextSyncTime && authority.backoffDelay == nextDelay) {
                    return;
                }
                authority.backoffTime = nextSyncTime;
                authority.backoffDelay = nextDelay;
                changed = true;
            }
        }

        if (changed) {
            reportChange(ContentResolver.SYNC_OBSERVER_TYPE_SETTINGS);
        }
    }

    public void clearAllBackoffs(SyncQueue syncQueue) {
        boolean changed = false;
        synchronized (mAuthorities) {
            for (AccountInfo accountInfo : mAccounts.values()) {
                for (AuthorityInfo authorityInfo : accountInfo.authorities.values()) {
                    if (authorityInfo.backoffTime != NOT_IN_BACKOFF_MODE
                            || authorityInfo.backoffDelay != NOT_IN_BACKOFF_MODE) {
                        if (Log.isLoggable(TAG, Log.VERBOSE)) {
                            Log.v(TAG, "clearAllBackoffs:"
                                    + " authority:" + authorityInfo.authority
                                    + " account:" + accountInfo.accountAndUser.account.name
                                    + " user:" + accountInfo.accountAndUser.userId
                                    + " backoffTime was: " + authorityInfo.backoffTime
                                    + " backoffDelay was: " + authorityInfo.backoffDelay);
                        }
                        authorityInfo.backoffTime = NOT_IN_BACKOFF_MODE;
                        authorityInfo.backoffDelay = NOT_IN_BACKOFF_MODE;
                        syncQueue.onBackoffChanged(accountInfo.accountAndUser.account,
                                accountInfo.accountAndUser.userId, authorityInfo.authority, 0);
                        changed = true;
                    }
                }
            }
        }

        if (changed) {
            reportChange(ContentResolver.SYNC_OBSERVER_TYPE_SETTINGS);
        }
    }

    public void setDelayUntilTime(Account account, int userId, String providerName,
            long delayUntil) {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "setDelayUntil: " + account + ", provider " + providerName
                    + ", user " + userId + " -> delayUntil " + delayUntil);
        }
        synchronized (mAuthorities) {
            AuthorityInfo authority = getOrCreateAuthorityLocked(
                    account, userId, providerName, -1 /* ident */, true);
            if (authority.delayUntil == delayUntil) {
                return;
            }
            authority.delayUntil = delayUntil;
        }

        reportChange(ContentResolver.SYNC_OBSERVER_TYPE_SETTINGS);
    }

    public long getDelayUntilTime(Account account, int userId, String providerName) {
        synchronized (mAuthorities) {
            AuthorityInfo authority = getAuthorityLocked(account, userId, providerName,
                    "getDelayUntil");
            if (authority == null) {
                return 0;
            }
            return authority.delayUntil;
        }
    }

    private void updateOrRemovePeriodicSync(Account account, int userId, String providerName,
            Bundle extras,
            long period, boolean add) {
        if (period <= 0) {
            period = 0;
        }
        if (extras == null) {
            extras = new Bundle();
        }
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "addOrRemovePeriodicSync: " + account + ", user " + userId
                    + ", provider " + providerName
                    + " -> period " + period + ", extras " + extras);
        }
        synchronized (mAuthorities) {
            try {
                AuthorityInfo authority =
                        getOrCreateAuthorityLocked(account, userId, providerName, -1, false);
                if (add) {
                    // add this periodic sync if one with the same extras doesn't already
                    // exist in the periodicSyncs array
                    boolean alreadyPresent = false;
                    for (int i = 0, N = authority.periodicSyncs.size(); i < N; i++) {
                        Pair<Bundle, Long> syncInfo = authority.periodicSyncs.get(i);
                        final Bundle existingExtras = syncInfo.first;
                        if (equals(existingExtras, extras)) {
                            if (syncInfo.second == period) {
                                return;
                            }
                            authority.periodicSyncs.set(i, Pair.create(extras, period));
                            alreadyPresent = true;
                            break;
                        }
                    }
                    // if we added an entry to the periodicSyncs array also add an entry to
                    // the periodic syncs status to correspond to it
                    if (!alreadyPresent) {
                        authority.periodicSyncs.add(Pair.create(extras, period));
                        SyncStatusInfo status = getOrCreateSyncStatusLocked(authority.ident);
                        status.setPeriodicSyncTime(authority.periodicSyncs.size() - 1, 0);
                    }
                } else {
                    // remove any periodic syncs that match the authority and extras
                    SyncStatusInfo status = mSyncStatus.get(authority.ident);
                    boolean changed = false;
                    Iterator<Pair<Bundle, Long>> iterator = authority.periodicSyncs.iterator();
                    int i = 0;
                    while (iterator.hasNext()) {
                        Pair<Bundle, Long> syncInfo = iterator.next();
                        if (equals(syncInfo.first, extras)) {
                            iterator.remove();
                            changed = true;
                            // if we removed an entry from the periodicSyncs array also
                            // remove the corresponding entry from the status
                            if (status != null) {
                                status.removePeriodicSyncTime(i);
                            }
                        } else {
                            i++;
                        }
                    }
                    if (!changed) {
                        return;
                    }
                }
            } finally {
                writeAccountInfoLocked();
                writeStatusLocked();
            }
        }

        reportChange(ContentResolver.SYNC_OBSERVER_TYPE_SETTINGS);
    }

    public void addPeriodicSync(Account account, int userId, String providerName, Bundle extras,
            long pollFrequency) {
        updateOrRemovePeriodicSync(account, userId, providerName, extras, pollFrequency,
                true /* add */);
    }

    public void removePeriodicSync(Account account, int userId, String providerName,
            Bundle extras) {
        updateOrRemovePeriodicSync(account, userId, providerName, extras, 0 /* period, ignored */,
                false /* remove */);
    }

    public List<PeriodicSync> getPeriodicSyncs(Account account, int userId, String providerName) {
        ArrayList<PeriodicSync> syncs = new ArrayList<PeriodicSync>();
        synchronized (mAuthorities) {
            AuthorityInfo authority = getAuthorityLocked(account, userId, providerName,
                    "getPeriodicSyncs");
            if (authority != null) {
                for (Pair<Bundle, Long> item : authority.periodicSyncs) {
                    syncs.add(new PeriodicSync(account, providerName, item.first,
                            item.second));
                }
            }
        }
        return syncs;
    }

    public void setMasterSyncAutomatically(boolean flag, int userId) {
        synchronized (mAuthorities) {
            Boolean auto = mMasterSyncAutomatically.get(userId);
            if (auto != null && (boolean) auto == flag) {
                return;
            }
            mMasterSyncAutomatically.put(userId, flag);
            writeAccountInfoLocked();
        }
        if (flag) {
            requestSync(null, userId, null, new Bundle());
        }
        reportChange(ContentResolver.SYNC_OBSERVER_TYPE_SETTINGS);
        mContext.sendBroadcast(SYNC_CONNECTION_SETTING_CHANGED_INTENT);
    }

    public boolean getMasterSyncAutomatically(int userId) {
        synchronized (mAuthorities) {
            Boolean auto = mMasterSyncAutomatically.get(userId);
            return auto == null ? true : auto;
        }
    }

    public AuthorityInfo getOrCreateAuthority(Account account, int userId, String authority) {
        synchronized (mAuthorities) {
            return getOrCreateAuthorityLocked(account, userId, authority,
                    -1 /* assign a new identifier if creating a new authority */,
                    true /* write to storage if this results in a change */);
        }
    }

    public void removeAuthority(Account account, int userId, String authority) {
        synchronized (mAuthorities) {
            removeAuthorityLocked(account, userId, authority, true /* doWrite */);
        }
    }

    public AuthorityInfo getAuthority(int authorityId) {
        synchronized (mAuthorities) {
            return mAuthorities.get(authorityId);
=======
    private class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE pending ("
                    + "_id INTEGER PRIMARY KEY,"
                    + "authority TEXT NOT NULL,"
                    + "account TEXT NOT NULL,"
                    + "extras BLOB NOT NULL,"
                    + "source INTEGER NOT NULL"
                    + ");");

            db.execSQL("CREATE TABLE stats (" +
                       "_id INTEGER PRIMARY KEY," +
                       "account TEXT, " +
                       "authority TEXT, " +
                       "syncdata TEXT, " +
                       "UNIQUE (account, authority)" +
                       ");");

            db.execSQL("CREATE TABLE history (" +
                       "_id INTEGER PRIMARY KEY," +
                       "stats_id INTEGER," +
                       "eventTime INTEGER," +
                       "elapsedTime INTEGER," +
                       "source INTEGER," +
                       "event INTEGER," +
                       "upstreamActivity INTEGER," +
                       "downstreamActivity INTEGER," +
                       "mesg TEXT);");

            db.execSQL("CREATE TABLE status ("
                    + "_id INTEGER PRIMARY KEY,"
                    + "stats_id INTEGER NOT NULL,"
                    + "totalElapsedTime INTEGER NOT NULL DEFAULT 0,"
                    + "numSyncs INTEGER NOT NULL DEFAULT 0,"
                    + "numSourcePoll INTEGER NOT NULL DEFAULT 0,"
                    + "numSourceServer INTEGER NOT NULL DEFAULT 0,"
                    + "numSourceLocal INTEGER NOT NULL DEFAULT 0,"
                    + "numSourceUser INTEGER NOT NULL DEFAULT 0,"
                    + "lastSuccessTime INTEGER,"
                    + "lastSuccessSource INTEGER,"
                    + "lastFailureTime INTEGER,"
                    + "lastFailureSource INTEGER,"
                    + "lastFailureMesg STRING,"
                    + "initialFailureTime INTEGER,"
                    + "pending INTEGER NOT NULL DEFAULT 0);");

            db.execSQL("CREATE TABLE active ("
                    + "_id INTEGER PRIMARY KEY,"
                    + "authority TEXT,"
                    + "account TEXT,"
                    + "startTime INTEGER);");

            db.execSQL("CREATE INDEX historyEventTime ON history (eventTime)");

            db.execSQL("CREATE TABLE settings (" +
                       "name TEXT PRIMARY KEY," +
                       "value TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion == 9 && newVersion == 10) {
                Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will preserve old data");
                db.execSQL("ALTER TABLE status ADD COLUMN initialFailureTime INTEGER");
                return;
            }

            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS pending");
            db.execSQL("DROP TABLE IF EXISTS stats");
            db.execSQL("DROP TABLE IF EXISTS history");
            db.execSQL("DROP TABLE IF EXISTS settings");
            db.execSQL("DROP TABLE IF EXISTS active");
            db.execSQL("DROP TABLE IF EXISTS status");
            onCreate(db);
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            if (!db.isReadOnly()) {
                db.delete("active", null, null);
                db.insert("active", "account", null);
            }
        }
    }

    protected void doDatabaseCleanup(String[] accounts) {
        HashSet<String> currentAccounts = new HashSet<String>();
        for (String account : accounts) currentAccounts.add(account);
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("stats", STATS_ACCOUNT_PROJECTION,
                null /* where */, null /* where args */, Sync.Stats.ACCOUNT,
                null /* having */, null /* order by */);
        try {
            while (cursor.moveToNext()) {
                String account = cursor.getString(0);
                if (TextUtils.isEmpty(account)) {
                    continue;
                }
                if (!currentAccounts.contains(account)) {
                    String where = Sync.Stats.ACCOUNT + "=?";
                    int numDeleted;
                    numDeleted = db.delete("stats", where, new String[]{account});
                    if (Config.LOGD) {
                        Log.d(TAG, "deleted " + numDeleted
                                + " records from stats table"
                                + " for account " + account);
                    }
                }
            }
        } finally {
            cursor.close();
        }
    }

    protected void setActiveSync(SyncManager.ActiveSyncContext activeSyncContext) {
        if (activeSyncContext != null) {
            updateActiveSync(activeSyncContext.mSyncOperation.account,
                    activeSyncContext.mSyncOperation.authority, activeSyncContext.mStartTime);
        } else {
            // we indicate that the sync is not active by passing null for all the parameters
            updateActiveSync(null, null, null);
        }
    }

    private int updateActiveSync(String account, String authority, Long startTime) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("account", account);
        values.put("authority", authority);
        values.put("startTime", startTime);
        int numChanges = db.update("active", values, null, null);
        if (numChanges > 0) {
            mContext.getContentResolver().notifyChange(Sync.Active.CONTENT_URI,
                    null /* this change wasn't made through an observer */);
        }
        return numChanges;
    }

    /**
     * Implements the {@link ContentProvider#query} method
     */
    public Cursor query(Uri url, String[] projectionIn,
            String selection, String[] selectionArgs, String sort) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        // Generate the body of the query
        int match = sURLMatcher.match(url);
        String groupBy = null;
        switch (match) {
            case STATS:
                qb.setTables("stats");
                break;
            case STATS_ID:
                qb.setTables("stats");
                qb.appendWhere("_id=");
                qb.appendWhere(url.getPathSegments().get(1));
                break;
            case HISTORY:
                // join the stats and history tables, so the caller can get
                // the account and authority information as part of this query.
                qb.setTables("stats, history");
                qb.setProjectionMap(HISTORY_PROJECTION_MAP);
                qb.appendWhere("stats._id = history.stats_id");
                break;
            case ACTIVE:
                qb.setTables("active");
                qb.setProjectionMap(ACTIVE_PROJECTION_MAP);
                qb.appendWhere("account is not null");
                break;
            case PENDING:
                qb.setTables("pending");
                qb.setProjectionMap(PENDING_PROJECTION_MAP);
                groupBy = "account, authority";
                break;
            case STATUS:
                // join the stats and status tables, so the caller can get
                // the account and authority information as part of this query.
                qb.setTables("stats, status");
                qb.setProjectionMap(STATUS_PROJECTION_MAP);
                qb.appendWhere("stats._id = status.stats_id");
                break;
            case HISTORY_ID:
                // join the stats and history tables, so the caller can get
                // the account and authority information as part of this query.
                qb.setTables("stats, history");
                qb.setProjectionMap(HISTORY_PROJECTION_MAP);
                qb.appendWhere("stats._id = history.stats_id");
                qb.appendWhere("AND history._id=");
                qb.appendWhere(url.getPathSegments().get(1));
                break;
            case SETTINGS:
                qb.setTables("settings");
                break;
            default:
                throw new IllegalArgumentException("Unknown URL " + url);
        }

        if (match == SETTINGS) {
            mContext.enforceCallingOrSelfPermission(Manifest.permission.READ_SYNC_SETTINGS,
                    "no permission to read the sync settings");
        } else {
            mContext.enforceCallingOrSelfPermission(Manifest.permission.READ_SYNC_STATS,
                    "no permission to read the sync stats");
        }
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor c = qb.query(db, projectionIn, selection, selectionArgs, groupBy, null, sort);
        c.setNotificationUri(mContext.getContentResolver(), url);
        return c;
    }

    /**
     * Implements the {@link ContentProvider#insert} method
     * @param callerIsTheProvider true if this is being called via the
     *  {@link ContentProvider#insert} in method rather than directly.
     * @throws UnsupportedOperationException if callerIsTheProvider is true and the url isn't
     *   for the Settings table.
     */
    public Uri insert(boolean callerIsTheProvider, Uri url, ContentValues values) {
        String table;
        long rowID;
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sURLMatcher.match(url);
        checkCaller(callerIsTheProvider, match);
        switch (match) {
            case SETTINGS:
                mContext.enforceCallingOrSelfPermission(Manifest.permission.WRITE_SYNC_SETTINGS,
                        "no permission to write the sync settings");
                table = "settings";
                rowID = db.replace(table, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URL " + url);
        }


        if (rowID > 0) {
            mContext.getContentResolver().notifyChange(url, null /* observer */);
            return Uri.parse("content://sync/" + table + "/" + rowID);
        }

        return null;
    }

    private static void checkCaller(boolean callerIsTheProvider, int match) {
        if (callerIsTheProvider && match != SETTINGS) {
            throw new UnsupportedOperationException(
                    "only the settings are modifiable via the ContentProvider interface");
        }
    }

    /**
     * Implements the {@link ContentProvider#delete} method
     * @param callerIsTheProvider true if this is being called via the
     *  {@link ContentProvider#delete} in method rather than directly.
     * @throws UnsupportedOperationException if callerIsTheProvider is true and the url isn't
     *   for the Settings table.
     */
    public int delete(boolean callerIsTheProvider, Uri url, String where, String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sURLMatcher.match(url);

        int numRows;
        switch (match) {
            case SETTINGS:
                mContext.enforceCallingOrSelfPermission(Manifest.permission.WRITE_SYNC_SETTINGS,
                        "no permission to write the sync settings");
                numRows = db.delete("settings", where, whereArgs);
                break;
            default:
                throw new UnsupportedOperationException("Cannot delete URL: " + url);
        }

        if (numRows > 0) {
            mContext.getContentResolver().notifyChange(url, null /* observer */);
        }
        return numRows;
    }

    /**
     * Implements the {@link ContentProvider#update} method
     * @param callerIsTheProvider true if this is being called via the
     *  {@link ContentProvider#update} in method rather than directly.
     * @throws UnsupportedOperationException if callerIsTheProvider is true and the url isn't
     *   for the Settings table.
     */
    public int update(boolean callerIsTheProvider, Uri url, ContentValues initialValues,
            String where, String[] whereArgs) {
        switch (sURLMatcher.match(url)) {
            case SETTINGS:
                throw new UnsupportedOperationException("updating url " + url
                        + " is not allowed, use insert instead");
            default:
                throw new UnsupportedOperationException("Cannot update URL: " + url);
        }
    }

    /**
     * Implements the {@link ContentProvider#getType} method
     */
    public String getType(Uri url) {
        int match = sURLMatcher.match(url);
        switch (match) {
            case SETTINGS:
                return "vnd.android.cursor.dir/sync-settings";
            default:
                throw new IllegalArgumentException("Unknown URL");
        }
    }

    protected Uri insertIntoPending(ContentValues values) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            long rowId = db.insert("pending", Sync.Pending.ACCOUNT, values);
            if (rowId < 0) return null;
            String account = values.getAsString(Sync.Pending.ACCOUNT);
            String authority = values.getAsString(Sync.Pending.AUTHORITY);

            long statsId = createStatsRowIfNecessary(account, authority);
            createStatusRowIfNecessary(statsId);

            values.clear();
            values.put(Sync.Status.PENDING, 1);
            int numUpdatesStatus = db.update("status", values, "stats_id=" + statsId, null);

            db.setTransactionSuccessful();

            mContext.getContentResolver().notifyChange(Sync.Pending.CONTENT_URI,
                    null /* no observer initiated this change */);
            if (numUpdatesStatus > 0) {
                mContext.getContentResolver().notifyChange(Sync.Status.CONTENT_URI,
                        null /* no observer initiated this change */);
            }
            return ContentUris.withAppendedId(Sync.Pending.CONTENT_URI, rowId);
        } finally {
            db.endTransaction();
        }
    }

    int deleteFromPending(long rowId) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            String account;
            String authority;
            Cursor c = db.query("pending",
                    new String[]{Sync.Pending.ACCOUNT, Sync.Pending.AUTHORITY},
                    "_id=" + rowId, null, null, null, null);
            try {
                if (c.getCount() != 1) {
                    return 0;
                }
                c.moveToNext();
                account = c.getString(0);
                authority = c.getString(1);
            } finally {
                c.close();
            }
            db.delete("pending", "_id=" + rowId, null /* no where args */);
            final String[] accountAuthorityWhereArgs = new String[]{account, authority};
            boolean isPending = 0 < DatabaseUtils.longForQuery(db,
                    "SELECT COUNT(*) FROM PENDING WHERE account=? AND authority=?",
                    accountAuthorityWhereArgs);
            if (!isPending) {
                long statsId = createStatsRowIfNecessary(account, authority);
                db.execSQL("UPDATE status SET pending=0 WHERE stats_id=" + statsId);
            }
            db.setTransactionSuccessful();

            mContext.getContentResolver().notifyChange(Sync.Pending.CONTENT_URI,
                    null /* no observer initiated this change */);
            if (!isPending) {
                mContext.getContentResolver().notifyChange(Sync.Status.CONTENT_URI,
                        null /* no observer initiated this change */);
            }
            return 1;
        } finally {
            db.endTransaction();
        }
    }

    int clearPending() {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            int numChanges = db.delete("pending", null, null /* no where args */);
            if (numChanges > 0) {
                db.execSQL("UPDATE status SET pending=0");
                mContext.getContentResolver().notifyChange(Sync.Pending.CONTENT_URI,
                        null /* no observer initiated this change */);
                mContext.getContentResolver().notifyChange(Sync.Status.CONTENT_URI,
                        null /* no observer initiated this change */);
            }
            db.setTransactionSuccessful();
            return numChanges;
        } finally {
            db.endTransaction();
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
<<<<<<< HEAD
     * Returns true if there is currently a sync operation for the given
     * account or authority actively being processed.
     */
    public boolean isSyncActive(Account account, int userId, String authority) {
        synchronized (mAuthorities) {
            for (SyncInfo syncInfo : getCurrentSyncs(userId)) {
                AuthorityInfo ainfo = getAuthority(syncInfo.authorityId);
                if (ainfo != null && ainfo.account.equals(account)
                        && ainfo.authority.equals(authority)
                        && ainfo.userId == userId) {
                    return true;
                }
            }
        }

        return false;
    }

    public PendingOperation insertIntoPending(PendingOperation op) {
        synchronized (mAuthorities) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "insertIntoPending: account=" + op.account
                        + " user=" + op.userId
                        + " auth=" + op.authority
                        + " src=" + op.syncSource
                        + " extras=" + op.extras);
            }

            AuthorityInfo authority = getOrCreateAuthorityLocked(op.account, op.userId,
                    op.authority,
                    -1 /* desired identifier */,
                    true /* write accounts to storage */);
            if (authority == null) {
                return null;
            }

            op = new PendingOperation(op);
            op.authorityId = authority.ident;
            mPendingOperations.add(op);
            appendPendingOperationLocked(op);

            SyncStatusInfo status = getOrCreateSyncStatusLocked(authority.ident);
            status.pending = true;
        }

        reportChange(ContentResolver.SYNC_OBSERVER_TYPE_PENDING);
        return op;
    }

    public boolean deleteFromPending(PendingOperation op) {
        boolean res = false;
        synchronized (mAuthorities) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "deleteFromPending: account=" + op.account
                    + " user=" + op.userId
                    + " auth=" + op.authority
                    + " src=" + op.syncSource
                    + " extras=" + op.extras);
            }
            if (mPendingOperations.remove(op)) {
                if (mPendingOperations.size() == 0
                        || mNumPendingFinished >= PENDING_FINISH_TO_WRITE) {
                    writePendingOperationsLocked();
                    mNumPendingFinished = 0;
                } else {
                    mNumPendingFinished++;
                }

                AuthorityInfo authority = getAuthorityLocked(op.account, op.userId, op.authority,
                        "deleteFromPending");
                if (authority != null) {
                    if (Log.isLoggable(TAG, Log.VERBOSE)) Log.v(TAG, "removing - " + authority);
                    final int N = mPendingOperations.size();
                    boolean morePending = false;
                    for (int i=0; i<N; i++) {
                        PendingOperation cur = mPendingOperations.get(i);
                        if (cur.account.equals(op.account)
                                && cur.authority.equals(op.authority)
                                && cur.userId == op.userId) {
                            morePending = true;
                            break;
                        }
                    }

                    if (!morePending) {
                        if (Log.isLoggable(TAG, Log.VERBOSE)) Log.v(TAG, "no more pending!");
                        SyncStatusInfo status = getOrCreateSyncStatusLocked(authority.ident);
                        status.pending = false;
                    }
                }

                res = true;
            }
        }

        reportChange(ContentResolver.SYNC_OBSERVER_TYPE_PENDING);
        return res;
    }

    /**
     * Return a copy of the current array of pending operations.  The
     * PendingOperation objects are the real objects stored inside, so that
     * they can be used with deleteFromPending().
     */
    public ArrayList<PendingOperation> getPendingOperations() {
        synchronized (mAuthorities) {
            return new ArrayList<PendingOperation>(mPendingOperations);
        }
    }

    /**
     * Return the number of currently pending operations.
     */
    public int getPendingOperationCount() {
        synchronized (mAuthorities) {
            return mPendingOperations.size();
        }
    }

    /**
     * Called when the set of account has changed, given the new array of
     * active accounts.
     */
    public void doDatabaseCleanup(Account[] accounts, int userId) {
        synchronized (mAuthorities) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) Log.w(TAG, "Updating for new accounts...");
            SparseArray<AuthorityInfo> removing = new SparseArray<AuthorityInfo>();
            Iterator<AccountInfo> accIt = mAccounts.values().iterator();
            while (accIt.hasNext()) {
                AccountInfo acc = accIt.next();
                if (!ArrayUtils.contains(accounts, acc.accountAndUser.account)
                        && acc.accountAndUser.userId == userId) {
                    // This account no longer exists...
                    if (Log.isLoggable(TAG, Log.VERBOSE)) {
                        Log.w(TAG, "Account removed: " + acc.accountAndUser);
                    }
                    for (AuthorityInfo auth : acc.authorities.values()) {
                        removing.put(auth.ident, auth);
                    }
                    accIt.remove();
                }
            }

            // Clean out all data structures.
            int i = removing.size();
            if (i > 0) {
                while (i > 0) {
                    i--;
                    int ident = removing.keyAt(i);
                    mAuthorities.remove(ident);
                    int j = mSyncStatus.size();
                    while (j > 0) {
                        j--;
                        if (mSyncStatus.keyAt(j) == ident) {
                            mSyncStatus.remove(mSyncStatus.keyAt(j));
                        }
                    }
                    j = mSyncHistory.size();
                    while (j > 0) {
                        j--;
                        if (mSyncHistory.get(j).authorityId == ident) {
                            mSyncHistory.remove(j);
                        }
                    }
                }
                writeAccountInfoLocked();
                writeStatusLocked();
                writePendingOperationsLocked();
                writeStatisticsLocked();
            }
        }
    }

    /**
     * Called when a sync is starting. Supply a valid ActiveSyncContext with information
     * about the sync.
     */
    public SyncInfo addActiveSync(SyncManager.ActiveSyncContext activeSyncContext) {
        final SyncInfo syncInfo;
        synchronized (mAuthorities) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "setActiveSync: account="
                    + activeSyncContext.mSyncOperation.account
                    + " auth=" + activeSyncContext.mSyncOperation.authority
                    + " src=" + activeSyncContext.mSyncOperation.syncSource
                    + " extras=" + activeSyncContext.mSyncOperation.extras);
            }
            AuthorityInfo authority = getOrCreateAuthorityLocked(
                    activeSyncContext.mSyncOperation.account,
                    activeSyncContext.mSyncOperation.userId,
                    activeSyncContext.mSyncOperation.authority,
                    -1 /* assign a new identifier if creating a new authority */,
                    true /* write to storage if this results in a change */);
            syncInfo = new SyncInfo(authority.ident,
                    authority.account, authority.authority,
                    activeSyncContext.mStartTime);
            getCurrentSyncs(authority.userId).add(syncInfo);
        }

        reportActiveChange();
        return syncInfo;
    }

    /**
     * Called to indicate that a previously active sync is no longer active.
     */
    public void removeActiveSync(SyncInfo syncInfo, int userId) {
        synchronized (mAuthorities) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "removeActiveSync: account=" + syncInfo.account
                        + " user=" + userId
                        + " auth=" + syncInfo.authority);
            }
            getCurrentSyncs(userId).remove(syncInfo);
        }

        reportActiveChange();
    }

    /**
     * To allow others to send active change reports, to poke clients.
     */
    public void reportActiveChange() {
        reportChange(ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE);
    }

    /**
     * Note that sync has started for the given account and authority.
     */
    public long insertStartSyncEvent(Account accountName, int userId, String authorityName,
                                     long now, int source, boolean initialization) {
        long id;
        synchronized (mAuthorities) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "insertStartSyncEvent: account=" + accountName + "user=" + userId
                    + " auth=" + authorityName + " source=" + source);
            }
            AuthorityInfo authority = getAuthorityLocked(accountName, userId, authorityName,
                    "insertStartSyncEvent");
            if (authority == null) {
                return -1;
            }
            SyncHistoryItem item = new SyncHistoryItem();
            item.initialization = initialization;
            item.authorityId = authority.ident;
            item.historyId = mNextHistoryId++;
            if (mNextHistoryId < 0) mNextHistoryId = 0;
            item.eventTime = now;
            item.source = source;
            item.event = EVENT_START;
            mSyncHistory.add(0, item);
            while (mSyncHistory.size() > MAX_HISTORY) {
                mSyncHistory.remove(mSyncHistory.size()-1);
            }
            id = item.historyId;
            if (Log.isLoggable(TAG, Log.VERBOSE)) Log.v(TAG, "returning historyId " + id);
        }

        reportChange(ContentResolver.SYNC_OBSERVER_TYPE_STATUS);
        return id;
    }

    public static boolean equals(Bundle b1, Bundle b2) {
        if (b1.size() != b2.size()) {
            return false;
        }
        if (b1.isEmpty()) {
            return true;
        }
        for (String key : b1.keySet()) {
            if (!b2.containsKey(key)) {
                return false;
            }
            if (!b1.get(key).equals(b2.get(key))) {
                return false;
            }
        }
        return true;
    }

    public void stopSyncEvent(long historyId, long elapsedTime, String resultMessage,
            long downstreamActivity, long upstreamActivity) {
        synchronized (mAuthorities) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "stopSyncEvent: historyId=" + historyId);
            }
            SyncHistoryItem item = null;
            int i = mSyncHistory.size();
            while (i > 0) {
                i--;
                item = mSyncHistory.get(i);
                if (item.historyId == historyId) {
                    break;
                }
                item = null;
            }

            if (item == null) {
                Log.w(TAG, "stopSyncEvent: no history for id " + historyId);
                return;
            }

            item.elapsedTime = elapsedTime;
            item.event = EVENT_STOP;
            item.mesg = resultMessage;
            item.downstreamActivity = downstreamActivity;
            item.upstreamActivity = upstreamActivity;

            SyncStatusInfo status = getOrCreateSyncStatusLocked(item.authorityId);

            status.numSyncs++;
            status.totalElapsedTime += elapsedTime;
            switch (item.source) {
                case SOURCE_LOCAL:
                    status.numSourceLocal++;
                    break;
                case SOURCE_POLL:
                    status.numSourcePoll++;
                    break;
                case SOURCE_USER:
                    status.numSourceUser++;
                    break;
                case SOURCE_SERVER:
                    status.numSourceServer++;
                    break;
                case SOURCE_PERIODIC:
                    status.numSourcePeriodic++;
                    break;
            }

            boolean writeStatisticsNow = false;
            int day = getCurrentDayLocked();
            if (mDayStats[0] == null) {
                mDayStats[0] = new DayStats(day);
            } else if (day != mDayStats[0].day) {
                System.arraycopy(mDayStats, 0, mDayStats, 1, mDayStats.length-1);
                mDayStats[0] = new DayStats(day);
                writeStatisticsNow = true;
            } else if (mDayStats[0] == null) {
            }
            final DayStats ds = mDayStats[0];

            final long lastSyncTime = (item.eventTime + elapsedTime);
            boolean writeStatusNow = false;
            if (MESG_SUCCESS.equals(resultMessage)) {
                // - if successful, update the successful columns
                if (status.lastSuccessTime == 0 || status.lastFailureTime != 0) {
                    writeStatusNow = true;
                }
                status.lastSuccessTime = lastSyncTime;
                status.lastSuccessSource = item.source;
                status.lastFailureTime = 0;
                status.lastFailureSource = -1;
                status.lastFailureMesg = null;
                status.initialFailureTime = 0;
                ds.successCount++;
                ds.successTime += elapsedTime;
            } else if (!MESG_CANCELED.equals(resultMessage)) {
                if (status.lastFailureTime == 0) {
                    writeStatusNow = true;
                }
                status.lastFailureTime = lastSyncTime;
                status.lastFailureSource = item.source;
                status.lastFailureMesg = resultMessage;
                if (status.initialFailureTime == 0) {
                    status.initialFailureTime = lastSyncTime;
                }
                ds.failureCount++;
                ds.failureTime += elapsedTime;
            }

            if (writeStatusNow) {
                writeStatusLocked();
            } else if (!hasMessages(MSG_WRITE_STATUS)) {
                sendMessageDelayed(obtainMessage(MSG_WRITE_STATUS),
                        WRITE_STATUS_DELAY);
            }
            if (writeStatisticsNow) {
                writeStatisticsLocked();
            } else if (!hasMessages(MSG_WRITE_STATISTICS)) {
                sendMessageDelayed(obtainMessage(MSG_WRITE_STATISTICS),
                        WRITE_STATISTICS_DELAY);
            }
        }

        reportChange(ContentResolver.SYNC_OBSERVER_TYPE_STATUS);
    }

    /**
     * Return a list of the currently active syncs. Note that the returned items are the
     * real, live active sync objects, so be careful what you do with it.
     */
    public List<SyncInfo> getCurrentSyncs(int userId) {
        synchronized (mAuthorities) {
            ArrayList<SyncInfo> syncs = mCurrentSyncs.get(userId);
            if (syncs == null) {
                syncs = new ArrayList<SyncInfo>();
                mCurrentSyncs.put(userId, syncs);
            }
            return syncs;
        }
    }

    /**
     * Return an array of the current sync status for all authorities.  Note
     * that the objects inside the array are the real, live status objects,
     * so be careful what you do with them.
     */
    public ArrayList<SyncStatusInfo> getSyncStatus() {
        synchronized (mAuthorities) {
            final int N = mSyncStatus.size();
            ArrayList<SyncStatusInfo> ops = new ArrayList<SyncStatusInfo>(N);
            for (int i=0; i<N; i++) {
                ops.add(mSyncStatus.valueAt(i));
            }
            return ops;
        }
    }

    /**
     * Return an array of the current authorities. Note
     * that the objects inside the array are the real, live objects,
     * so be careful what you do with them.
     */
    public ArrayList<AuthorityInfo> getAuthorities() {
        synchronized (mAuthorities) {
            final int N = mAuthorities.size();
            ArrayList<AuthorityInfo> infos = new ArrayList<AuthorityInfo>(N);
            for (int i=0; i<N; i++) {
                // Make deep copy because AuthorityInfo syncs are liable to change.
                infos.add(new AuthorityInfo(mAuthorities.valueAt(i)));
            }
            return infos;
        }
    }

    /**
     * Returns the status that matches the authority and account.
     *
     * @param account the account we want to check
     * @param authority the authority whose row should be selected
     * @return the SyncStatusInfo for the authority
     */
    public SyncStatusInfo getStatusByAccountAndAuthority(Account account, int userId,
            String authority) {
        if (account == null || authority == null) {
          throw new IllegalArgumentException();
        }
        synchronized (mAuthorities) {
            final int N = mSyncStatus.size();
            for (int i=0; i<N; i++) {
                SyncStatusInfo cur = mSyncStatus.valueAt(i);
                AuthorityInfo ainfo = mAuthorities.get(cur.authorityId);

                if (ainfo != null && ainfo.authority.equals(authority)
                        && ainfo.userId == userId
                        && account.equals(ainfo.account)) {
                  return cur;
                }
            }
            return null;
        }
    }

    /**
     * Return true if the pending status is true of any matching authorities.
     */
    public boolean isSyncPending(Account account, int userId, String authority) {
        synchronized (mAuthorities) {
            final int N = mSyncStatus.size();
            for (int i=0; i<N; i++) {
                SyncStatusInfo cur = mSyncStatus.valueAt(i);
                AuthorityInfo ainfo = mAuthorities.get(cur.authorityId);
                if (ainfo == null) {
                    continue;
                }
                if (userId != ainfo.userId) {
                    continue;
                }
                if (account != null && !ainfo.account.equals(account)) {
                    continue;
                }
                if (ainfo.authority.equals(authority) && cur.pending) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Return an array of the current sync status for all authorities.  Note
     * that the objects inside the array are the real, live status objects,
     * so be careful what you do with them.
     */
    public ArrayList<SyncHistoryItem> getSyncHistory() {
        synchronized (mAuthorities) {
            final int N = mSyncHistory.size();
            ArrayList<SyncHistoryItem> items = new ArrayList<SyncHistoryItem>(N);
            for (int i=0; i<N; i++) {
                items.add(mSyncHistory.get(i));
            }
            return items;
        }
    }

    /**
     * Return an array of the current per-day statistics.  Note
     * that the objects inside the array are the real, live status objects,
     * so be careful what you do with them.
     */
    public DayStats[] getDayStatistics() {
        synchronized (mAuthorities) {
            DayStats[] ds = new DayStats[mDayStats.length];
            System.arraycopy(mDayStats, 0, ds, 0, ds.length);
            return ds;
        }
    }

    private int getCurrentDayLocked() {
        mCal.setTimeInMillis(System.currentTimeMillis());
        final int dayOfYear = mCal.get(Calendar.DAY_OF_YEAR);
        if (mYear != mCal.get(Calendar.YEAR)) {
            mYear = mCal.get(Calendar.YEAR);
            mCal.clear();
            mCal.set(Calendar.YEAR, mYear);
            mYearInDays = (int)(mCal.getTimeInMillis()/86400000);
        }
        return dayOfYear + mYearInDays;
    }

    /**
     * Retrieve an authority, returning null if one does not exist.
     *
     * @param accountName The name of the account for the authority.
     * @param authorityName The name of the authority itself.
     * @param tag If non-null, this will be used in a log message if the
     * requested authority does not exist.
     */
    private AuthorityInfo getAuthorityLocked(Account accountName, int userId, String authorityName,
            String tag) {
        AccountAndUser au = new AccountAndUser(accountName, userId);
        AccountInfo accountInfo = mAccounts.get(au);
        if (accountInfo == null) {
            if (tag != null) {
                if (Log.isLoggable(TAG, Log.VERBOSE)) {
                    Log.v(TAG, tag + ": unknown account " + au);
                }
            }
            return null;
        }
        AuthorityInfo authority = accountInfo.authorities.get(authorityName);
        if (authority == null) {
            if (tag != null) {
                if (Log.isLoggable(TAG, Log.VERBOSE)) {
                    Log.v(TAG, tag + ": unknown authority " + authorityName);
                }
            }
            return null;
        }

        return authority;
    }

    private AuthorityInfo getOrCreateAuthorityLocked(Account accountName, int userId,
            String authorityName, int ident, boolean doWrite) {
        AccountAndUser au = new AccountAndUser(accountName, userId);
        AccountInfo account = mAccounts.get(au);
        if (account == null) {
            account = new AccountInfo(au);
            mAccounts.put(au, account);
        }
        AuthorityInfo authority = account.authorities.get(authorityName);
        if (authority == null) {
            if (ident < 0) {
                ident = mNextAuthorityId;
                mNextAuthorityId++;
                doWrite = true;
            }
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "created a new AuthorityInfo for " + accountName
                        + ", user " + userId
                        + ", provider " + authorityName);
            }
            authority = new AuthorityInfo(accountName, userId, authorityName, ident);
            account.authorities.put(authorityName, authority);
            mAuthorities.put(ident, authority);
            if (doWrite) {
                writeAccountInfoLocked();
            }
        }

        return authority;
    }

    private void removeAuthorityLocked(Account account, int userId, String authorityName,
            boolean doWrite) {
        AccountInfo accountInfo = mAccounts.get(new AccountAndUser(account, userId));
        if (accountInfo != null) {
            final AuthorityInfo authorityInfo = accountInfo.authorities.remove(authorityName);
            if (authorityInfo != null) {
                mAuthorities.remove(authorityInfo.ident);
                if (doWrite) {
                    writeAccountInfoLocked();
                }
            }
        }
    }

    public SyncStatusInfo getOrCreateSyncStatus(AuthorityInfo authority) {
        synchronized (mAuthorities) {
            return getOrCreateSyncStatusLocked(authority.ident);
        }
    }

    private SyncStatusInfo getOrCreateSyncStatusLocked(int authorityId) {
        SyncStatusInfo status = mSyncStatus.get(authorityId);
        if (status == null) {
            status = new SyncStatusInfo(authorityId);
            mSyncStatus.put(authorityId, status);
        }
        return status;
    }

    public void writeAllState() {
        synchronized (mAuthorities) {
            // Account info is always written so no need to do it here.

            if (mNumPendingFinished > 0) {
                // Only write these if they are out of date.
                writePendingOperationsLocked();
            }

            // Just always write these...  they are likely out of date.
            writeStatusLocked();
            writeStatisticsLocked();
        }
    }

    /**
     * public for testing
     */
    public void clearAndReadState() {
        synchronized (mAuthorities) {
            mAuthorities.clear();
            mAccounts.clear();
            mPendingOperations.clear();
            mSyncStatus.clear();
            mSyncHistory.clear();

            readAccountInfoLocked();
            readStatusLocked();
            readPendingOperationsLocked();
            readStatisticsLocked();
            readAndDeleteLegacyAccountInfoLocked();
            writeAccountInfoLocked();
            writeStatusLocked();
            writePendingOperationsLocked();
            writeStatisticsLocked();
        }
    }

    /**
     * Read all account information back in to the initial engine state.
     */
    private void readAccountInfoLocked() {
        int highestAuthorityId = -1;
        FileInputStream fis = null;
        try {
            fis = mAccountInfoFile.openRead();
            if (DEBUG_FILE) Log.v(TAG, "Reading " + mAccountInfoFile.getBaseFile());
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(fis, null);
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.START_TAG) {
                eventType = parser.next();
            }
            String tagName = parser.getName();
            if ("accounts".equals(tagName)) {
                String listen = parser.getAttributeValue(null, XML_ATTR_LISTEN_FOR_TICKLES);
                String versionString = parser.getAttributeValue(null, "version");
                int version;
                try {
                    version = (versionString == null) ? 0 : Integer.parseInt(versionString);
                } catch (NumberFormatException e) {
                    version = 0;
                }
                String nextIdString = parser.getAttributeValue(null, XML_ATTR_NEXT_AUTHORITY_ID);
                try {
                    int id = (nextIdString == null) ? 0 : Integer.parseInt(nextIdString);
                    mNextAuthorityId = Math.max(mNextAuthorityId, id);
                } catch (NumberFormatException e) {
                    // don't care
                }
                String offsetString = parser.getAttributeValue(null, XML_ATTR_SYNC_RANDOM_OFFSET);
                try {
                    mSyncRandomOffset = (offsetString == null) ? 0 : Integer.parseInt(offsetString);
                } catch (NumberFormatException e) {
                    mSyncRandomOffset = 0;
                }
                if (mSyncRandomOffset == 0) {
                    Random random = new Random(System.currentTimeMillis());
                    mSyncRandomOffset = random.nextInt(86400);
                }
                mMasterSyncAutomatically.put(0, listen == null || Boolean.parseBoolean(listen));
                eventType = parser.next();
                AuthorityInfo authority = null;
                Pair<Bundle, Long> periodicSync = null;
                do {
                    if (eventType == XmlPullParser.START_TAG) {
                        tagName = parser.getName();
                        if (parser.getDepth() == 2) {
                            if ("authority".equals(tagName)) {
                                authority = parseAuthority(parser, version);
                                periodicSync = null;
                                if (authority.ident > highestAuthorityId) {
                                    highestAuthorityId = authority.ident;
                                }
                            } else if (XML_TAG_LISTEN_FOR_TICKLES.equals(tagName)) {
                                parseListenForTickles(parser);
                            }
                        } else if (parser.getDepth() == 3) {
                            if ("periodicSync".equals(tagName) && authority != null) {
                                periodicSync = parsePeriodicSync(parser, authority);
                            }
                        } else if (parser.getDepth() == 4 && periodicSync != null) {
                            if ("extra".equals(tagName)) {
                                parseExtra(parser, periodicSync);
                            }
                        }
                    }
                    eventType = parser.next();
                } while (eventType != XmlPullParser.END_DOCUMENT);
            }
        } catch (XmlPullParserException e) {
            Log.w(TAG, "Error reading accounts", e);
            return;
        } catch (java.io.IOException e) {
            if (fis == null) Log.i(TAG, "No initial accounts");
            else Log.w(TAG, "Error reading accounts", e);
            return;
        } finally {
            mNextAuthorityId = Math.max(highestAuthorityId + 1, mNextAuthorityId);
            if (fis != null) {
                try {
                    fis.close();
                } catch (java.io.IOException e1) {
                }
            }
        }

        maybeMigrateSettingsForRenamedAuthorities();
    }

    /**
     * some authority names have changed. copy over their settings and delete the old ones
     * @return true if a change was made
     */
    private boolean maybeMigrateSettingsForRenamedAuthorities() {
        boolean writeNeeded = false;

        ArrayList<AuthorityInfo> authoritiesToRemove = new ArrayList<AuthorityInfo>();
        final int N = mAuthorities.size();
        for (int i=0; i<N; i++) {
            AuthorityInfo authority = mAuthorities.valueAt(i);
            // skip this authority if it isn't one of the renamed ones
            final String newAuthorityName = sAuthorityRenames.get(authority.authority);
            if (newAuthorityName == null) {
                continue;
            }

            // remember this authority so we can remove it later. we can't remove it
            // now without messing up this loop iteration
            authoritiesToRemove.add(authority);

            // this authority isn't enabled, no need to copy it to the new authority name since
            // the default is "disabled"
            if (!authority.enabled) {
                continue;
            }

            // if we already have a record of this new authority then don't copy over the settings
            if (getAuthorityLocked(authority.account, authority.userId, newAuthorityName, "cleanup")
                    != null) {
                continue;
            }

            AuthorityInfo newAuthority = getOrCreateAuthorityLocked(authority.account,
                    authority.userId, newAuthorityName, -1 /* ident */, false /* doWrite */);
            newAuthority.enabled = true;
            writeNeeded = true;
        }

        for (AuthorityInfo authorityInfo : authoritiesToRemove) {
            removeAuthorityLocked(authorityInfo.account, authorityInfo.userId,
                    authorityInfo.authority, false /* doWrite */);
            writeNeeded = true;
        }

        return writeNeeded;
    }

    private void parseListenForTickles(XmlPullParser parser) {
        String user = parser.getAttributeValue(null, XML_ATTR_USER);
        int userId = 0;
        try {
            userId = Integer.parseInt(user);
        } catch (NumberFormatException e) {
            Log.e(TAG, "error parsing the user for listen-for-tickles", e);
        } catch (NullPointerException e) {
            Log.e(TAG, "the user in listen-for-tickles is null", e);
        }
        String enabled = parser.getAttributeValue(null, XML_ATTR_ENABLED);
        boolean listen = enabled == null || Boolean.parseBoolean(enabled);
        mMasterSyncAutomatically.put(userId, listen);
    }

    private AuthorityInfo parseAuthority(XmlPullParser parser, int version) {
        AuthorityInfo authority = null;
        int id = -1;
        try {
            id = Integer.parseInt(parser.getAttributeValue(
                    null, "id"));
        } catch (NumberFormatException e) {
            Log.e(TAG, "error parsing the id of the authority", e);
        } catch (NullPointerException e) {
            Log.e(TAG, "the id of the authority is null", e);
        }
        if (id >= 0) {
            String authorityName = parser.getAttributeValue(null, "authority");
            String enabled = parser.getAttributeValue(null, XML_ATTR_ENABLED);
            String syncable = parser.getAttributeValue(null, "syncable");
            String accountName = parser.getAttributeValue(null, "account");
            String accountType = parser.getAttributeValue(null, "type");
            String user = parser.getAttributeValue(null, XML_ATTR_USER);
            int userId = user == null ? 0 : Integer.parseInt(user);
            if (accountType == null) {
                accountType = "com.google";
                syncable = "unknown";
            }
            authority = mAuthorities.get(id);
            if (DEBUG_FILE) Log.v(TAG, "Adding authority: account="
                    + accountName + " auth=" + authorityName
                    + " user=" + userId
                    + " enabled=" + enabled
                    + " syncable=" + syncable);
            if (authority == null) {
                if (DEBUG_FILE) Log.v(TAG, "Creating entry");
                authority = getOrCreateAuthorityLocked(
                        new Account(accountName, accountType), userId, authorityName, id, false);
                // If the version is 0 then we are upgrading from a file format that did not
                // know about periodic syncs. In that case don't clear the list since we
                // want the default, which is a daily periodioc sync.
                // Otherwise clear out this default list since we will populate it later with
                // the periodic sync descriptions that are read from the configuration file.
                if (version > 0) {
                    authority.periodicSyncs.clear();
                }
            }
            if (authority != null) {
                authority.enabled = enabled == null || Boolean.parseBoolean(enabled);
                if ("unknown".equals(syncable)) {
                    authority.syncable = -1;
                } else {
                    authority.syncable =
                            (syncable == null || Boolean.parseBoolean(syncable)) ? 1 : 0;
                }
            } else {
                Log.w(TAG, "Failure adding authority: account="
                        + accountName + " auth=" + authorityName
                        + " enabled=" + enabled
                        + " syncable=" + syncable);
            }
        }

        return authority;
    }

    private Pair<Bundle, Long> parsePeriodicSync(XmlPullParser parser, AuthorityInfo authority) {
        Bundle extras = new Bundle();
        String periodValue = parser.getAttributeValue(null, "period");
        final long period;
        try {
            period = Long.parseLong(periodValue);
        } catch (NumberFormatException e) {
            Log.e(TAG, "error parsing the period of a periodic sync", e);
            return null;
        } catch (NullPointerException e) {
            Log.e(TAG, "the period of a periodic sync is null", e);
            return null;
        }
        final Pair<Bundle, Long> periodicSync = Pair.create(extras, period);
        authority.periodicSyncs.add(periodicSync);

        return periodicSync;
    }

    private void parseExtra(XmlPullParser parser, Pair<Bundle, Long> periodicSync) {
        final Bundle extras = periodicSync.first;
        String name = parser.getAttributeValue(null, "name");
        String type = parser.getAttributeValue(null, "type");
        String value1 = parser.getAttributeValue(null, "value1");
        String value2 = parser.getAttributeValue(null, "value2");

        try {
            if ("long".equals(type)) {
                extras.putLong(name, Long.parseLong(value1));
            } else if ("integer".equals(type)) {
                extras.putInt(name, Integer.parseInt(value1));
            } else if ("double".equals(type)) {
                extras.putDouble(name, Double.parseDouble(value1));
            } else if ("float".equals(type)) {
                extras.putFloat(name, Float.parseFloat(value1));
            } else if ("boolean".equals(type)) {
                extras.putBoolean(name, Boolean.parseBoolean(value1));
            } else if ("string".equals(type)) {
                extras.putString(name, value1);
            } else if ("account".equals(type)) {
                extras.putParcelable(name, new Account(value1, value2));
            }
        } catch (NumberFormatException e) {
            Log.e(TAG, "error parsing bundle value", e);
        } catch (NullPointerException e) {
            Log.e(TAG, "error parsing bundle value", e);
        }
    }

    /**
     * Write all account information to the account file.
     */
    private void writeAccountInfoLocked() {
        if (DEBUG_FILE) Log.v(TAG, "Writing new " + mAccountInfoFile.getBaseFile());
        FileOutputStream fos = null;

        try {
            fos = mAccountInfoFile.startWrite();
            XmlSerializer out = new FastXmlSerializer();
            out.setOutput(fos, "utf-8");
            out.startDocument(null, true);
            out.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

            out.startTag(null, "accounts");
            out.attribute(null, "version", Integer.toString(ACCOUNTS_VERSION));
            out.attribute(null, XML_ATTR_NEXT_AUTHORITY_ID, Integer.toString(mNextAuthorityId));
            out.attribute(null, XML_ATTR_SYNC_RANDOM_OFFSET, Integer.toString(mSyncRandomOffset));

            // Write the Sync Automatically flags for each user
            final int M = mMasterSyncAutomatically.size();
            for (int m = 0; m < M; m++) {
                int userId = mMasterSyncAutomatically.keyAt(m);
                Boolean listen = mMasterSyncAutomatically.valueAt(m);
                out.startTag(null, XML_TAG_LISTEN_FOR_TICKLES);
                out.attribute(null, XML_ATTR_USER, Integer.toString(userId));
                out.attribute(null, XML_ATTR_ENABLED, Boolean.toString(listen));
                out.endTag(null, XML_TAG_LISTEN_FOR_TICKLES);
            }

            final int N = mAuthorities.size();
            for (int i=0; i<N; i++) {
                AuthorityInfo authority = mAuthorities.valueAt(i);
                out.startTag(null, "authority");
                out.attribute(null, "id", Integer.toString(authority.ident));
                out.attribute(null, "account", authority.account.name);
                out.attribute(null, XML_ATTR_USER, Integer.toString(authority.userId));
                out.attribute(null, "type", authority.account.type);
                out.attribute(null, "authority", authority.authority);
                out.attribute(null, XML_ATTR_ENABLED, Boolean.toString(authority.enabled));
                if (authority.syncable < 0) {
                    out.attribute(null, "syncable", "unknown");
                } else {
                    out.attribute(null, "syncable", Boolean.toString(authority.syncable != 0));
                }
                for (Pair<Bundle, Long> periodicSync : authority.periodicSyncs) {
                    out.startTag(null, "periodicSync");
                    out.attribute(null, "period", Long.toString(periodicSync.second));
                    final Bundle extras = periodicSync.first;
                    for (String key : extras.keySet()) {
                        out.startTag(null, "extra");
                        out.attribute(null, "name", key);
                        final Object value = extras.get(key);
                        if (value instanceof Long) {
                            out.attribute(null, "type", "long");
                            out.attribute(null, "value1", value.toString());
                        } else if (value instanceof Integer) {
                            out.attribute(null, "type", "integer");
                            out.attribute(null, "value1", value.toString());
                        } else if (value instanceof Boolean) {
                            out.attribute(null, "type", "boolean");
                            out.attribute(null, "value1", value.toString());
                        } else if (value instanceof Float) {
                            out.attribute(null, "type", "float");
                            out.attribute(null, "value1", value.toString());
                        } else if (value instanceof Double) {
                            out.attribute(null, "type", "double");
                            out.attribute(null, "value1", value.toString());
                        } else if (value instanceof String) {
                            out.attribute(null, "type", "string");
                            out.attribute(null, "value1", value.toString());
                        } else if (value instanceof Account) {
                            out.attribute(null, "type", "account");
                            out.attribute(null, "value1", ((Account)value).name);
                            out.attribute(null, "value2", ((Account)value).type);
                        }
                        out.endTag(null, "extra");
                    }
                    out.endTag(null, "periodicSync");
                }
                out.endTag(null, "authority");
            }

            out.endTag(null, "accounts");

            out.endDocument();

            mAccountInfoFile.finishWrite(fos);
        } catch (java.io.IOException e1) {
            Log.w(TAG, "Error writing accounts", e1);
            if (fos != null) {
                mAccountInfoFile.failWrite(fos);
            }
        }
    }

    static int getIntColumn(Cursor c, String name) {
        return c.getInt(c.getColumnIndex(name));
    }

    static long getLongColumn(Cursor c, String name) {
        return c.getLong(c.getColumnIndex(name));
    }

    /**
     * Load sync engine state from the old syncmanager database, and then
     * erase it.  Note that we don't deal with pending operations, active
     * sync, or history.
     */
    private void readAndDeleteLegacyAccountInfoLocked() {
        // Look for old database to initialize from.
        File file = mContext.getDatabasePath("syncmanager.db");
        if (!file.exists()) {
            return;
        }
        String path = file.getPath();
        SQLiteDatabase db = null;
        try {
            db = SQLiteDatabase.openDatabase(path, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
        }

        if (db != null) {
            final boolean hasType = db.getVersion() >= 11;

            // Copy in all of the status information, as well as accounts.
            if (DEBUG_FILE) Log.v(TAG, "Reading legacy sync accounts db");
            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
            qb.setTables("stats, status");
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("_id", "status._id as _id");
            map.put("account", "stats.account as account");
            if (hasType) {
                map.put("account_type", "stats.account_type as account_type");
            }
            map.put("authority", "stats.authority as authority");
            map.put("totalElapsedTime", "totalElapsedTime");
            map.put("numSyncs", "numSyncs");
            map.put("numSourceLocal", "numSourceLocal");
            map.put("numSourcePoll", "numSourcePoll");
            map.put("numSourceServer", "numSourceServer");
            map.put("numSourceUser", "numSourceUser");
            map.put("lastSuccessSource", "lastSuccessSource");
            map.put("lastSuccessTime", "lastSuccessTime");
            map.put("lastFailureSource", "lastFailureSource");
            map.put("lastFailureTime", "lastFailureTime");
            map.put("lastFailureMesg", "lastFailureMesg");
            map.put("pending", "pending");
            qb.setProjectionMap(map);
            qb.appendWhere("stats._id = status.stats_id");
            Cursor c = qb.query(db, null, null, null, null, null, null);
            while (c.moveToNext()) {
                String accountName = c.getString(c.getColumnIndex("account"));
                String accountType = hasType
                        ? c.getString(c.getColumnIndex("account_type")) : null;
                if (accountType == null) {
                    accountType = "com.google";
                }
                String authorityName = c.getString(c.getColumnIndex("authority"));
                AuthorityInfo authority = this.getOrCreateAuthorityLocked(
                        new Account(accountName, accountType), 0 /* legacy is single-user */,
                        authorityName, -1, false);
                if (authority != null) {
                    int i = mSyncStatus.size();
                    boolean found = false;
                    SyncStatusInfo st = null;
                    while (i > 0) {
                        i--;
                        st = mSyncStatus.valueAt(i);
                        if (st.authorityId == authority.ident) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        st = new SyncStatusInfo(authority.ident);
                        mSyncStatus.put(authority.ident, st);
                    }
                    st.totalElapsedTime = getLongColumn(c, "totalElapsedTime");
                    st.numSyncs = getIntColumn(c, "numSyncs");
                    st.numSourceLocal = getIntColumn(c, "numSourceLocal");
                    st.numSourcePoll = getIntColumn(c, "numSourcePoll");
                    st.numSourceServer = getIntColumn(c, "numSourceServer");
                    st.numSourceUser = getIntColumn(c, "numSourceUser");
                    st.numSourcePeriodic = 0;
                    st.lastSuccessSource = getIntColumn(c, "lastSuccessSource");
                    st.lastSuccessTime = getLongColumn(c, "lastSuccessTime");
                    st.lastFailureSource = getIntColumn(c, "lastFailureSource");
                    st.lastFailureTime = getLongColumn(c, "lastFailureTime");
                    st.lastFailureMesg = c.getString(c.getColumnIndex("lastFailureMesg"));
                    st.pending = getIntColumn(c, "pending") != 0;
                }
            }

            c.close();

            // Retrieve the settings.
            qb = new SQLiteQueryBuilder();
            qb.setTables("settings");
            c = qb.query(db, null, null, null, null, null, null);
            while (c.moveToNext()) {
                String name = c.getString(c.getColumnIndex("name"));
                String value = c.getString(c.getColumnIndex("value"));
                if (name == null) continue;
                if (name.equals("listen_for_tickles")) {
                    setMasterSyncAutomatically(value == null || Boolean.parseBoolean(value), 0);
                } else if (name.startsWith("sync_provider_")) {
                    String provider = name.substring("sync_provider_".length(),
                            name.length());
                    int i = mAuthorities.size();
                    while (i > 0) {
                        i--;
                        AuthorityInfo authority = mAuthorities.valueAt(i);
                        if (authority.authority.equals(provider)) {
                            authority.enabled = value == null || Boolean.parseBoolean(value);
                            authority.syncable = 1;
                        }
                    }
                }
            }

            c.close();

            db.close();

            (new File(path)).delete();
        }
    }

    public static final int STATUS_FILE_END = 0;
    public static final int STATUS_FILE_ITEM = 100;

    /**
     * Read all sync status back in to the initial engine state.
     */
    private void readStatusLocked() {
        if (DEBUG_FILE) Log.v(TAG, "Reading " + mStatusFile.getBaseFile());
        try {
            byte[] data = mStatusFile.readFully();
            Parcel in = Parcel.obtain();
            in.unmarshall(data, 0, data.length);
            in.setDataPosition(0);
            int token;
            while ((token=in.readInt()) != STATUS_FILE_END) {
                if (token == STATUS_FILE_ITEM) {
                    SyncStatusInfo status = new SyncStatusInfo(in);
                    if (mAuthorities.indexOfKey(status.authorityId) >= 0) {
                        status.pending = false;
                        if (DEBUG_FILE) Log.v(TAG, "Adding status for id "
                                + status.authorityId);
                        mSyncStatus.put(status.authorityId, status);
                    }
                } else {
                    // Ooops.
                    Log.w(TAG, "Unknown status token: " + token);
                    break;
                }
            }
        } catch (java.io.IOException e) {
            Log.i(TAG, "No initial status");
        }
    }

    /**
     * Write all sync status to the sync status file.
     */
    private void writeStatusLocked() {
        if (DEBUG_FILE) Log.v(TAG, "Writing new " + mStatusFile.getBaseFile());

        // The file is being written, so we don't need to have a scheduled
        // write until the next change.
        removeMessages(MSG_WRITE_STATUS);

        FileOutputStream fos = null;
        try {
            fos = mStatusFile.startWrite();
            Parcel out = Parcel.obtain();
            final int N = mSyncStatus.size();
            for (int i=0; i<N; i++) {
                SyncStatusInfo status = mSyncStatus.valueAt(i);
                out.writeInt(STATUS_FILE_ITEM);
                status.writeToParcel(out, 0);
            }
            out.writeInt(STATUS_FILE_END);
            fos.write(out.marshall());
            out.recycle();

            mStatusFile.finishWrite(fos);
        } catch (java.io.IOException e1) {
            Log.w(TAG, "Error writing status", e1);
            if (fos != null) {
                mStatusFile.failWrite(fos);
            }
        }
    }

    public static final int PENDING_OPERATION_VERSION = 2;

    /**
     * Read all pending operations back in to the initial engine state.
     */
    private void readPendingOperationsLocked() {
        if (DEBUG_FILE) Log.v(TAG, "Reading " + mPendingFile.getBaseFile());
        try {
            byte[] data = mPendingFile.readFully();
            Parcel in = Parcel.obtain();
            in.unmarshall(data, 0, data.length);
            in.setDataPosition(0);
            final int SIZE = in.dataSize();
            while (in.dataPosition() < SIZE) {
                int version = in.readInt();
                if (version != PENDING_OPERATION_VERSION && version != 1) {
                    Log.w(TAG, "Unknown pending operation version "
                            + version + "; dropping all ops");
                    break;
                }
                int authorityId = in.readInt();
                int syncSource = in.readInt();
                byte[] flatExtras = in.createByteArray();
                boolean expedited;
                if (version == PENDING_OPERATION_VERSION) {
                    expedited = in.readInt() != 0;
                } else {
                    expedited = false;
                }
                AuthorityInfo authority = mAuthorities.get(authorityId);
                if (authority != null) {
                    Bundle extras;
                    if (flatExtras != null) {
                        extras = unflattenBundle(flatExtras);
                    } else {
                        // if we are unable to parse the extras for whatever reason convert this
                        // to a regular sync by creating an empty extras
                        extras = new Bundle();
                    }
                    PendingOperation op = new PendingOperation(
                            authority.account, authority.userId, syncSource,
                            authority.authority, extras, expedited);
                    op.authorityId = authorityId;
                    op.flatExtras = flatExtras;
                    if (DEBUG_FILE) Log.v(TAG, "Adding pending op: account=" + op.account
                            + " auth=" + op.authority
                            + " src=" + op.syncSource
                            + " expedited=" + op.expedited
                            + " extras=" + op.extras);
                    mPendingOperations.add(op);
                }
            }
        } catch (java.io.IOException e) {
            Log.i(TAG, "No initial pending operations");
        }
    }

    private void writePendingOperationLocked(PendingOperation op, Parcel out) {
        out.writeInt(PENDING_OPERATION_VERSION);
        out.writeInt(op.authorityId);
        out.writeInt(op.syncSource);
        if (op.flatExtras == null && op.extras != null) {
            op.flatExtras = flattenBundle(op.extras);
        }
        out.writeByteArray(op.flatExtras);
        out.writeInt(op.expedited ? 1 : 0);
    }

    /**
     * Write all currently pending ops to the pending ops file.
     */
    private void writePendingOperationsLocked() {
        final int N = mPendingOperations.size();
        FileOutputStream fos = null;
        try {
            if (N == 0) {
                if (DEBUG_FILE) Log.v(TAG, "Truncating " + mPendingFile.getBaseFile());
                mPendingFile.truncate();
                return;
            }

            if (DEBUG_FILE) Log.v(TAG, "Writing new " + mPendingFile.getBaseFile());
            fos = mPendingFile.startWrite();

            Parcel out = Parcel.obtain();
            for (int i=0; i<N; i++) {
                PendingOperation op = mPendingOperations.get(i);
                writePendingOperationLocked(op, out);
            }
            fos.write(out.marshall());
            out.recycle();

            mPendingFile.finishWrite(fos);
        } catch (java.io.IOException e1) {
            Log.w(TAG, "Error writing pending operations", e1);
            if (fos != null) {
                mPendingFile.failWrite(fos);
            }
        }
    }

    /**
     * Append the given operation to the pending ops file; if unable to,
     * write all pending ops.
     */
    private void appendPendingOperationLocked(PendingOperation op) {
        if (DEBUG_FILE) Log.v(TAG, "Appending to " + mPendingFile.getBaseFile());
        FileOutputStream fos = null;
        try {
            fos = mPendingFile.openAppend();
        } catch (java.io.IOException e) {
            if (DEBUG_FILE) Log.v(TAG, "Failed append; writing full file");
            writePendingOperationsLocked();
            return;
        }

        try {
            Parcel out = Parcel.obtain();
            writePendingOperationLocked(op, out);
            fos.write(out.marshall());
            out.recycle();
        } catch (java.io.IOException e1) {
            Log.w(TAG, "Error writing pending operations", e1);
        } finally {
            try {
                fos.close();
            } catch (java.io.IOException e2) {
            }
        }
    }

    static private byte[] flattenBundle(Bundle bundle) {
        byte[] flatData = null;
        Parcel parcel = Parcel.obtain();
        try {
            bundle.writeToParcel(parcel, 0);
            flatData = parcel.marshall();
        } finally {
            parcel.recycle();
        }
        return flatData;
    }

    static private Bundle unflattenBundle(byte[] flatData) {
        Bundle bundle;
        Parcel parcel = Parcel.obtain();
        try {
            parcel.unmarshall(flatData, 0, flatData.length);
            parcel.setDataPosition(0);
            bundle = parcel.readBundle();
        } catch (RuntimeException e) {
            // A RuntimeException is thrown if we were unable to parse the parcel.
            // Create an empty parcel in this case.
            bundle = new Bundle();
        } finally {
            parcel.recycle();
        }
        return bundle;
    }

    private void requestSync(Account account, int userId, String authority, Bundle extras) {
        // If this is happening in the system process, then call the syncrequest listener
        // to make a request back to the SyncManager directly.
        // If this is probably a test instance, then call back through the ContentResolver
        // which will know which userId to apply based on the Binder id.
        if (android.os.Process.myUid() == android.os.Process.SYSTEM_UID
                && mSyncRequestListener != null) {
            mSyncRequestListener.onSyncRequest(account, userId, authority, extras);
        } else {
            ContentResolver.requestSync(account, authority, extras);
        }
    }

    public static final int STATISTICS_FILE_END = 0;
    public static final int STATISTICS_FILE_ITEM_OLD = 100;
    public static final int STATISTICS_FILE_ITEM = 101;

    /**
     * Read all sync statistics back in to the initial engine state.
     */
    private void readStatisticsLocked() {
        try {
            byte[] data = mStatisticsFile.readFully();
            Parcel in = Parcel.obtain();
            in.unmarshall(data, 0, data.length);
            in.setDataPosition(0);
            int token;
            int index = 0;
            while ((token=in.readInt()) != STATISTICS_FILE_END) {
                if (token == STATISTICS_FILE_ITEM
                        || token == STATISTICS_FILE_ITEM_OLD) {
                    int day = in.readInt();
                    if (token == STATISTICS_FILE_ITEM_OLD) {
                        day = day - 2009 + 14245;  // Magic!
                    }
                    DayStats ds = new DayStats(day);
                    ds.successCount = in.readInt();
                    ds.successTime = in.readLong();
                    ds.failureCount = in.readInt();
                    ds.failureTime = in.readLong();
                    if (index < mDayStats.length) {
                        mDayStats[index] = ds;
                        index++;
                    }
                } else {
                    // Ooops.
                    Log.w(TAG, "Unknown stats token: " + token);
                    break;
                }
            }
        } catch (java.io.IOException e) {
            Log.i(TAG, "No initial statistics");
        }
    }

    /**
     * Write all sync statistics to the sync status file.
     */
    private void writeStatisticsLocked() {
        if (DEBUG_FILE) Log.v(TAG, "Writing new " + mStatisticsFile.getBaseFile());

        // The file is being written, so we don't need to have a scheduled
        // write until the next change.
        removeMessages(MSG_WRITE_STATISTICS);

        FileOutputStream fos = null;
        try {
            fos = mStatisticsFile.startWrite();
            Parcel out = Parcel.obtain();
            final int N = mDayStats.length;
            for (int i=0; i<N; i++) {
                DayStats ds = mDayStats[i];
                if (ds == null) {
                    break;
                }
                out.writeInt(STATISTICS_FILE_ITEM);
                out.writeInt(ds.day);
                out.writeInt(ds.successCount);
                out.writeLong(ds.successTime);
                out.writeInt(ds.failureCount);
                out.writeLong(ds.failureTime);
            }
            out.writeInt(STATISTICS_FILE_END);
            fos.write(out.marshall());
            out.recycle();

            mStatisticsFile.finishWrite(fos);
        } catch (java.io.IOException e1) {
            Log.w(TAG, "Error writing stats", e1);
            if (fos != null) {
                mStatisticsFile.failWrite(fos);
            }
=======
     * Returns a cursor over all the pending syncs in no particular order. This cursor is not
     * "live", in that if changes are made to the pending table any observers on this cursor
     * will not be notified.
     * @param projection Return only these columns. If null then all columns are returned.
     * @return the cursor of pending syncs
     */
    public Cursor getPendingSyncsCursor(String[] projection) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        return db.query("pending", projection, null, null, null, null, null);
    }

    // @VisibleForTesting
    static final long MILLIS_IN_4WEEKS = 1000L * 60 * 60 * 24 * 7 * 4;

    private boolean purgeOldHistoryEvents(long now) {
        // remove events that are older than MILLIS_IN_4WEEKS
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numDeletes = db.delete("history", "eventTime<" + (now - MILLIS_IN_4WEEKS), null);
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            if (numDeletes > 0) {
                Log.v(TAG, "deleted " + numDeletes + " old event(s) from the sync history");
            }
        }

        // keep only the last MAX_HISTORY_EVENTS_TO_KEEP history events
        numDeletes += db.delete("history", "eventTime < (select min(eventTime) from "
                + "(select eventTime from history order by eventTime desc limit ?))",
                new String[]{String.valueOf(MAX_HISTORY_EVENTS_TO_KEEP)});
        
        return numDeletes > 0;
    }

    public long insertStartSyncEvent(String account, String authority, long now, int source) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long statsId = createStatsRowIfNecessary(account, authority);

        purgeOldHistoryEvents(now);
        ContentValues values = new ContentValues();
        values.put(Sync.History.STATS_ID, statsId);
        values.put(Sync.History.EVENT_TIME, now);
        values.put(Sync.History.SOURCE, source);
        values.put(Sync.History.EVENT, Sync.History.EVENT_START);
        long rowId = db.insert("history", null, values);
        mContext.getContentResolver().notifyChange(Sync.History.CONTENT_URI, null /* observer */);
        mContext.getContentResolver().notifyChange(Sync.Status.CONTENT_URI, null /* observer */);
        return rowId;
    }

    public void stopSyncEvent(long historyId, long elapsedTime, String resultMessage,
            long downstreamActivity, long upstreamActivity) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(Sync.History.ELAPSED_TIME, elapsedTime);
            values.put(Sync.History.EVENT, Sync.History.EVENT_STOP);
            values.put(Sync.History.MESG, resultMessage);
            values.put(Sync.History.DOWNSTREAM_ACTIVITY, downstreamActivity);
            values.put(Sync.History.UPSTREAM_ACTIVITY, upstreamActivity);

            int count = db.update("history", values, "_id=?",
                    new String[]{Long.toString(historyId)});
            // We think that count should always be 1 but don't want to change this until after
            // launch.
            if (count > 0) {
                int source = (int) DatabaseUtils.longForQuery(db,
                        "SELECT source FROM history WHERE _id=" + historyId, null);
                long eventTime = DatabaseUtils.longForQuery(db,
                        "SELECT eventTime FROM history WHERE _id=" + historyId, null);
                long statsId = DatabaseUtils.longForQuery(db,
                        "SELECT stats_id FROM history WHERE _id=" + historyId, null);

                createStatusRowIfNecessary(statsId);

                // update the status table to reflect this sync
                StringBuilder sb = new StringBuilder();
                ArrayList<String> bindArgs = new ArrayList<String>();
                sb.append("UPDATE status SET");
                sb.append(" numSyncs=numSyncs+1");
                sb.append(", totalElapsedTime=totalElapsedTime+" + elapsedTime);
                switch (source) {
                    case Sync.History.SOURCE_LOCAL:
                        sb.append(", numSourceLocal=numSourceLocal+1");
                        break;
                    case Sync.History.SOURCE_POLL:
                        sb.append(", numSourcePoll=numSourcePoll+1");
                        break;
                    case Sync.History.SOURCE_USER:
                        sb.append(", numSourceUser=numSourceUser+1");
                        break;
                    case Sync.History.SOURCE_SERVER:
                        sb.append(", numSourceServer=numSourceServer+1");
                        break;
                }

                final String statsIdString = String.valueOf(statsId);
                final long lastSyncTime = (eventTime + elapsedTime);
                if (Sync.History.MESG_SUCCESS.equals(resultMessage)) {
                    // - if successful, update the successful columns
                    sb.append(", lastSuccessTime=" + lastSyncTime);
                    sb.append(", lastSuccessSource=" + source);
                    sb.append(", lastFailureTime=null");
                    sb.append(", lastFailureSource=null");
                    sb.append(", lastFailureMesg=null");
                    sb.append(", initialFailureTime=null");
                } else if (!Sync.History.MESG_CANCELED.equals(resultMessage)) {
                    sb.append(", lastFailureTime=" + lastSyncTime);
                    sb.append(", lastFailureSource=" + source);
                    sb.append(", lastFailureMesg=?");
                    bindArgs.add(resultMessage);
                    long initialFailureTime = DatabaseUtils.longForQuery(db,
                            SELECT_INITIAL_FAILURE_TIME_QUERY_STRING, 
                            new String[]{statsIdString, String.valueOf(lastSyncTime)});
                    sb.append(", initialFailureTime=" + initialFailureTime);
                }
                sb.append(" WHERE stats_id=?");
                bindArgs.add(statsIdString);
                db.execSQL(sb.toString(), bindArgs.toArray());
                db.setTransactionSuccessful();
                mContext.getContentResolver().notifyChange(Sync.History.CONTENT_URI,
                        null /* observer */);
                mContext.getContentResolver().notifyChange(Sync.Status.CONTENT_URI,
                        null /* observer */);
            }
        } finally {
            db.endTransaction();
        }
    }

    /**
     * If sync is failing for any of the provider/accounts then determine the time at which it
     * started failing and return the earliest time over all the provider/accounts. If none are
     * failing then return 0.
     */
    public long getInitialSyncFailureTime() {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        // Join the settings for a provider with the status so that we can easily
        // check if each provider is enabled for syncing. We also join in the overall
        // enabled flag ("listen_for_tickles") to each row so that we don't need to
        // make a separate DB lookup to access it.
        Cursor c = db.rawQuery(""
                + "SELECT initialFailureTime, s1.value, s2.value "
                + "FROM status "
                + "LEFT JOIN stats ON status.stats_id=stats._id "
                + "LEFT JOIN settings as s1 ON 'sync_provider_' || authority=s1.name "
                + "LEFT JOIN settings as s2 ON s2.name='listen_for_tickles' "
                + "where initialFailureTime is not null "
                + "  AND lastFailureMesg!=" + Sync.History.ERROR_TOO_MANY_DELETIONS
                + "  AND lastFailureMesg!=" + Sync.History.ERROR_AUTHENTICATION
                + "  AND lastFailureMesg!=" + Sync.History.ERROR_SYNC_ALREADY_IN_PROGRESS
                + "  AND authority!='subscribedfeeds' "
                + " ORDER BY initialFailureTime", null);
        try {
            while (c.moveToNext()) {
                // these settings default to true, so if they are null treat them as enabled
                final String providerEnabledString = c.getString(1);
                if (providerEnabledString != null && !Boolean.parseBoolean(providerEnabledString)) {
                    continue;
                }
                final String allEnabledString = c.getString(2);
                if (allEnabledString != null && !Boolean.parseBoolean(allEnabledString)) {
                    continue;
                }
                return c.getLong(0);
            }
        } finally {
            c.close();
        }
        return 0;
    }

    private void createStatusRowIfNecessary(long statsId) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        boolean statusExists = 0 != DatabaseUtils.longForQuery(db,
                "SELECT count(*) FROM status WHERE stats_id=" + statsId, null);
        if (!statusExists) {
            ContentValues values = new ContentValues();
            values.put("stats_id", statsId);
            db.insert("status", null, values);
        }
    }

    private long createStatsRowIfNecessary(String account, String authority) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        StringBuilder where = new StringBuilder();
        where.append(Sync.Stats.ACCOUNT + "= ?");
        where.append(" and " + Sync.Stats.AUTHORITY + "= ?");
        Cursor cursor = query(Sync.Stats.CONTENT_URI,
                Sync.Stats.SYNC_STATS_PROJECTION,
                where.toString(), new String[] { account, authority },
                null /* order */);
        try {
            long id;
            if (cursor.moveToFirst()) {
                id = cursor.getLong(cursor.getColumnIndexOrThrow(Sync.Stats._ID));
            } else {
                ContentValues values = new ContentValues();
                values.put(Sync.Stats.ACCOUNT, account);
                values.put(Sync.Stats.AUTHORITY, authority);
                id = db.insert("stats", null, values);
            }
            return id;
        } finally {
            cursor.close();
>>>>>>> 54b6cfa... Initial Contribution
        }
    }
}
