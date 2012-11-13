/*
 * Copyright (C) 2008 The Android Open Source Project
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

<<<<<<< HEAD
import com.android.internal.R;
import com.google.android.collect.Lists;
import com.google.android.collect.Maps;

import android.accounts.Account;
import android.accounts.AccountAndUser;
import android.accounts.AccountManager;
import android.accounts.AccountManagerService;
import android.accounts.OnAccountsUpdateListener;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AppGlobals;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.SyncStorageEngine.OnSyncRequestListener;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.RegisteredServicesCache;
import android.content.pm.RegisteredServicesCacheListener;
import android.content.pm.ResolveInfo;
import android.content.pm.UserInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
=======
import com.google.android.collect.Maps;

import com.android.internal.R;
import com.android.internal.util.ArrayUtils;

import android.accounts.AccountMonitor;
import android.accounts.AccountMonitorListener;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
>>>>>>> 54b6cfa... Initial Contribution
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
<<<<<<< HEAD
import android.os.PowerManager;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserId;
import android.os.WorkSource;
import android.provider.Settings;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.EventLog;
import android.util.Log;
import android.util.Pair;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
=======
import android.os.Parcel;
import android.os.PowerManager;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.pim.DateUtils;
import android.pim.Time;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.provider.Sync;
import android.provider.Settings;
import android.provider.Sync.History;
import android.text.TextUtils;
import android.util.Config;
import android.util.EventLog;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Observer;
import java.util.Observable;
>>>>>>> 54b6cfa... Initial Contribution

/**
 * @hide
 */
<<<<<<< HEAD
public class SyncManager implements OnAccountsUpdateListener {
    private static final String TAG = "SyncManager";

    /** Delay a sync due to local changes this long. In milliseconds */
    private static final long LOCAL_SYNC_DELAY;
=======
class SyncManager {
    private static final String TAG = "SyncManager";

    // used during dumping of the Sync history
    private static final long MILLIS_IN_HOUR = 1000 * 60 * 60;
    private static final long MILLIS_IN_DAY = MILLIS_IN_HOUR * 24;
    private static final long MILLIS_IN_WEEK = MILLIS_IN_DAY * 7;
    private static final long MILLIS_IN_4WEEKS = MILLIS_IN_WEEK * 4;

    /** Delay a sync due to local changes this long. In milliseconds */
    private static final long LOCAL_SYNC_DELAY = 30 * 1000; // 30 seconds
>>>>>>> 54b6cfa... Initial Contribution

    /**
     * If a sync takes longer than this and the sync queue is not empty then we will
     * cancel it and add it back to the end of the sync queue. In milliseconds.
     */
<<<<<<< HEAD
    private static final long MAX_TIME_PER_SYNC;

    static {
        final boolean isLargeRAM = ActivityManager.isLargeRAM();
        int defaultMaxInitSyncs = isLargeRAM ? 5 : 2;
        int defaultMaxRegularSyncs = isLargeRAM ? 2 : 1;
        MAX_SIMULTANEOUS_INITIALIZATION_SYNCS =
                SystemProperties.getInt("sync.max_init_syncs", defaultMaxInitSyncs);
        MAX_SIMULTANEOUS_REGULAR_SYNCS =
                SystemProperties.getInt("sync.max_regular_syncs", defaultMaxRegularSyncs);
        LOCAL_SYNC_DELAY =
                SystemProperties.getLong("sync.local_sync_delay", 30 * 1000 /* 30 seconds */);
        MAX_TIME_PER_SYNC =
                SystemProperties.getLong("sync.max_time_per_sync", 5 * 60 * 1000 /* 5 minutes */);
        SYNC_NOTIFICATION_DELAY =
                SystemProperties.getLong("sync.notification_delay", 30 * 1000 /* 30 seconds */);
    }

    private static final long SYNC_NOTIFICATION_DELAY;
=======
    private static final long MAX_TIME_PER_SYNC = 5 * 60 * 1000; // 5 minutes

    private static final long SYNC_NOTIFICATION_DELAY = 30 * 1000; // 30 seconds
>>>>>>> 54b6cfa... Initial Contribution

    /**
     * When retrying a sync for the first time use this delay. After that
     * the retry time will double until it reached MAX_SYNC_RETRY_TIME.
     * In milliseconds.
     */
    private static final long INITIAL_SYNC_RETRY_TIME_IN_MS = 30 * 1000; // 30 seconds

    /**
     * Default the max sync retry time to this value.
     */
    private static final long DEFAULT_MAX_SYNC_RETRY_TIME_IN_SECONDS = 60 * 60; // one hour

    /**
<<<<<<< HEAD
     * How long to wait before retrying a sync that failed due to one already being in progress.
     */
    private static final int DELAY_RETRY_SYNC_IN_PROGRESS_IN_SECONDS = 10;

    private static final int INITIALIZATION_UNBIND_DELAY_MS = 5000;

    private static final String SYNC_WAKE_LOCK_PREFIX = "*sync*";
    private static final String HANDLE_SYNC_ALARM_WAKE_LOCK = "SyncManagerHandleSyncAlarm";
    private static final String SYNC_LOOP_WAKE_LOCK = "SyncLoopWakeLock";

    private static final int MAX_SIMULTANEOUS_REGULAR_SYNCS;
    private static final int MAX_SIMULTANEOUS_INITIALIZATION_SYNCS;

    private Context mContext;

    private static final AccountAndUser[] INITIAL_ACCOUNTS_ARRAY = new AccountAndUser[0];

    private volatile AccountAndUser[] mAccounts = INITIAL_ACCOUNTS_ARRAY;

    volatile private PowerManager.WakeLock mHandleAlarmWakeLock;
    volatile private PowerManager.WakeLock mSyncManagerWakeLock;
    volatile private boolean mDataConnectionIsConnected = false;
    volatile private boolean mStorageIsLow = false;

    private final NotificationManager mNotificationMgr;
    private AlarmManager mAlarmService = null;

    private SyncStorageEngine mSyncStorageEngine;
    public SyncQueue mSyncQueue;

    protected final ArrayList<ActiveSyncContext> mActiveSyncContexts = Lists.newArrayList();

    // set if the sync active indicator should be reported
    private boolean mNeedSyncActiveNotification = false;

    private final PendingIntent mSyncAlarmIntent;
    // Synchronized on "this". Instead of using this directly one should instead call
    // its accessor, getConnManager().
    private ConnectivityManager mConnManagerDoNotUseDirectly;

    protected SyncAdaptersCache mSyncAdapters;
=======
     * An error notification is sent if sync of any of the providers has been failing for this long.
     */
    private static final long ERROR_NOTIFICATION_DELAY_MS = 1000 * 60 * 10; // 10 minutes

    private static final String SYNC_WAKE_LOCK = "SyncManagerSyncWakeLock";
    private static final String HANDLE_SYNC_ALARM_WAKE_LOCK = "SyncManagerHandleSyncAlarmWakeLock";
    
    private Context mContext;
    private ContentResolver mContentResolver;

    private String mStatusText = "";
    private long mHeartbeatTime = 0;

    private AccountMonitor mAccountMonitor;

    private volatile String[] mAccounts = null;

    volatile private PowerManager.WakeLock mSyncWakeLock;
    volatile private PowerManager.WakeLock mHandleAlarmWakeLock;
    volatile private boolean mDataConnectionIsConnected = false;
    volatile private boolean mStorageIsLow = false;
    private Sync.Settings.QueryMap mSyncSettings;

    private final NotificationManager mNotificationMgr;
    private AlarmManager mAlarmService = null;
    private HandlerThread mSyncThread;

    private volatile IPackageManager mPackageManager;

    private final SyncStorageEngine mSyncStorageEngine;
    private final SyncQueue mSyncQueue;

    private ActiveSyncContext mActiveSyncContext = null;

    // set if the sync error indicator should be reported.
    private boolean mNeedSyncErrorNotification = false;
    // set if the sync active indicator should be reported
    private boolean mNeedSyncActiveNotification = false;

    private volatile boolean mSyncPollInitialized;
    private final PendingIntent mSyncAlarmIntent;
    private final PendingIntent mSyncPollAlarmIntent;
>>>>>>> 54b6cfa... Initial Contribution

    private BroadcastReceiver mStorageIntentReceiver =
            new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
<<<<<<< HEAD
=======
                    ensureContentResolver();
>>>>>>> 54b6cfa... Initial Contribution
                    String action = intent.getAction();
                    if (Intent.ACTION_DEVICE_STORAGE_LOW.equals(action)) {
                        if (Log.isLoggable(TAG, Log.VERBOSE)) {
                            Log.v(TAG, "Internal storage is low.");
                        }
                        mStorageIsLow = true;
<<<<<<< HEAD
                        cancelActiveSync(null /* any account */, UserId.USER_ALL,
                                null /* any authority */);
=======
                        cancelActiveSync(null /* no url */);
>>>>>>> 54b6cfa... Initial Contribution
                    } else if (Intent.ACTION_DEVICE_STORAGE_OK.equals(action)) {
                        if (Log.isLoggable(TAG, Log.VERBOSE)) {
                            Log.v(TAG, "Internal storage is ok.");
                        }
                        mStorageIsLow = false;
                        sendCheckAlarmsMessage();
                    }
                }
            };

<<<<<<< HEAD
    private BroadcastReceiver mBootCompletedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            mSyncHandler.onBootCompleted();
        }
    };

    private BroadcastReceiver mBackgroundDataSettingChanged = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (getConnectivityManager().getBackgroundDataSetting()) {
                scheduleSync(null /* account */, UserId.USER_ALL, null /* authority */,
                        new Bundle(), 0 /* delay */,
                        false /* onlyThoseWithUnknownSyncableState */);
            }
        }
    };

    private final PowerManager mPowerManager;

    // Use this as a random offset to seed all periodic syncs
    private int mSyncRandomOffsetMillis;

    private static final long SYNC_ALARM_TIMEOUT_MIN = 30 * 1000; // 30 seconds
    private static final long SYNC_ALARM_TIMEOUT_MAX = 2 * 60 * 60 * 1000; // two hours

    private List<UserInfo> getAllUsers() {
        try {
            return AppGlobals.getPackageManager().getUsers();
        } catch (RemoteException re) {
            // Local to system process, shouldn't happen
        }
        return null;
    }

    private boolean containsAccountAndUser(AccountAndUser[] accounts, Account account, int userId) {
        boolean found = false;
        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i].userId == userId
                    && accounts[i].account.equals(account)) {
                found = true;
                break;
            }
        }
        return found;
    }

    public void onAccountsUpdated(Account[] accounts) {
        // remember if this was the first time this was called after an update
        final boolean justBootedUp = mAccounts == INITIAL_ACCOUNTS_ARRAY;

        List<UserInfo> users = getAllUsers();
        if (users == null)  return;

        int count = 0;

        // Get accounts from AccountManager for all the users on the system
        // TODO: Limit this to active users, when such a concept exists.
        AccountAndUser[] allAccounts = AccountManagerService.getSingleton().getAllAccounts();
        for (UserInfo user : users) {
            if (mBootCompleted) {
                Account[] accountsForUser =
                        AccountManagerService.getSingleton().getAccounts(user.id);
                mSyncStorageEngine.doDatabaseCleanup(accountsForUser, user.id);
            }
        }

        mAccounts = allAccounts;

        for (ActiveSyncContext currentSyncContext : mActiveSyncContexts) {
            if (!containsAccountAndUser(allAccounts,
                    currentSyncContext.mSyncOperation.account,
                    currentSyncContext.mSyncOperation.userId)) {
                Log.d(TAG, "canceling sync since the account has been removed");
                sendSyncFinishedOrCanceledMessage(currentSyncContext,
                        null /* no result since this is a cancel */);
            }
        }

        // we must do this since we don't bother scheduling alarms when
        // the accounts are not set yet
        sendCheckAlarmsMessage();

        if (allAccounts.length > 0) {
            // If this is the first time this was called after a bootup then
            // the accounts haven't really changed, instead they were just loaded
            // from the AccountManager. Otherwise at least one of the accounts
            // has a change.
            //
            // If there was a real account change then force a sync of all accounts.
            // This is a bit of overkill, but at least it will end up retrying syncs
            // that failed due to an authentication failure and thus will recover if the
            // account change was a password update.
            //
            // If this was the bootup case then don't sync everything, instead only
            // sync those that have an unknown syncable state, which will give them
            // a chance to set their syncable state.

            boolean onlyThoseWithUnkownSyncableState = justBootedUp;
            scheduleSync(null, UserId.USER_ALL, null, null, 0 /* no delay */,
                    onlyThoseWithUnkownSyncableState);
        }
    }

    private BroadcastReceiver mConnectivityIntentReceiver =
            new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            final boolean wasConnected = mDataConnectionIsConnected;

            // don't use the intent to figure out if network is connected, just check
            // ConnectivityManager directly.
            mDataConnectionIsConnected = readDataConnectionState();
            if (mDataConnectionIsConnected) {
                if (!wasConnected) {
                    if (Log.isLoggable(TAG, Log.VERBOSE)) {
                        Log.v(TAG, "Reconnection detected: clearing all backoffs");
                    }
                    mSyncStorageEngine.clearAllBackoffs(mSyncQueue);
                }
=======
    private BroadcastReceiver mConnectivityIntentReceiver =
            new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            NetworkInfo networkInfo =
                    intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo.State state = (networkInfo == null ? NetworkInfo.State.UNKNOWN :
                    networkInfo.getState());
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "received connectivity action.  network info: " + networkInfo);
            }

            // only pay attention to the CONNECTED and DISCONNECTED states.
            // if connected, we are connected.
            // if disconnected, we may not be connected.  in some cases, we may be connected on
            // a different network.
            // e.g., if switching from GPRS to WiFi, we may receive the CONNECTED to WiFi and
            // DISCONNECTED for GPRS in any order.  if we receive the CONNECTED first, and then
            // a DISCONNECTED, we want to make sure we set mDataConnectionIsConnected to true
            // since we still have a WiFi connection.
            switch (state) {
                case CONNECTED:
                    mDataConnectionIsConnected = true;
                    break;
                case DISCONNECTED:
                    if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
                        mDataConnectionIsConnected = false;
                    } else {
                        mDataConnectionIsConnected = true;
                    }
                    break;
                default:
                    // ignore the rest of the states -- leave our boolean alone.
            }
            if (mDataConnectionIsConnected) {
                initializeSyncPoll();
>>>>>>> 54b6cfa... Initial Contribution
                sendCheckAlarmsMessage();
            }
        }
    };

<<<<<<< HEAD
    private boolean readDataConnectionState() {
        NetworkInfo networkInfo = getConnectivityManager().getActiveNetworkInfo();
        return (networkInfo != null) && networkInfo.isConnected();
    }

    private BroadcastReceiver mShutdownIntentReceiver =
            new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.w(TAG, "Writing sync state before shutdown...");
            getSyncStorageEngine().writeAllState();
        }
    };

    private BroadcastReceiver mUserIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onUserRemoved(intent);
        }
    };

    private static final String ACTION_SYNC_ALARM = "android.content.syncmanager.SYNC_ALARM";
    private final SyncHandler mSyncHandler;

    private volatile boolean mBootCompleted = false;

    private ConnectivityManager getConnectivityManager() {
        synchronized (this) {
            if (mConnManagerDoNotUseDirectly == null) {
                mConnManagerDoNotUseDirectly = (ConnectivityManager)mContext.getSystemService(
                        Context.CONNECTIVITY_SERVICE);
            }
            return mConnManagerDoNotUseDirectly;
        }
    }
=======
    private static final String ACTION_SYNC_ALARM = "android.content.syncmanager.SYNC_ALARM";
    private static final String SYNC_POLL_ALARM = "android.content.syncmanager.SYNC_POLL_ALARM";
    private final SyncHandler mSyncHandler;

    private static final String[] SYNC_ACTIVE_PROJECTION = new String[]{
            Sync.Active.ACCOUNT,
            Sync.Active.AUTHORITY,
            Sync.Active.START_TIME,
    };

    private static final String[] SYNC_PENDING_PROJECTION = new String[]{
            Sync.Pending.ACCOUNT,
            Sync.Pending.AUTHORITY
    };

    private static final int MAX_SYNC_POLL_DELAY_SECONDS = 36 * 60 * 60; // 36 hours
    private static final int MIN_SYNC_POLL_DELAY_SECONDS = 24 * 60 * 60; // 24 hours

    private static final String SYNCMANAGER_PREFS_FILENAME = "/data/system/syncmanager.prefs";
>>>>>>> 54b6cfa... Initial Contribution

    public SyncManager(Context context, boolean factoryTest) {
        // Initialize the SyncStorageEngine first, before registering observers
        // and creating threads and so on; it may fail if the disk is full.
<<<<<<< HEAD
        mContext = context;
        SyncStorageEngine.init(context);
        mSyncStorageEngine = SyncStorageEngine.getSingleton();
        mSyncStorageEngine.setOnSyncRequestListener(new OnSyncRequestListener() {
            public void onSyncRequest(Account account, int userId, String authority,
                    Bundle extras) {
                scheduleSync(account, userId, authority, extras, 0, false);
            }
        });

        mSyncAdapters = new SyncAdaptersCache(mContext);
        mSyncQueue = new SyncQueue(mSyncStorageEngine, mSyncAdapters);

        HandlerThread syncThread = new HandlerThread("SyncHandlerThread",
                Process.THREAD_PRIORITY_BACKGROUND);
        syncThread.start();
        mSyncHandler = new SyncHandler(syncThread.getLooper());

        mSyncAdapters.setListener(new RegisteredServicesCacheListener<SyncAdapterType>() {
            public void onServiceChanged(SyncAdapterType type, boolean removed) {
                if (!removed) {
                    scheduleSync(null, UserId.USER_ALL, type.authority, null, 0 /* no delay */,
                            false /* onlyThoseWithUnkownSyncableState */);
                }
            }
        }, mSyncHandler);
=======
        SyncStorageEngine.init(context);
        mSyncStorageEngine = SyncStorageEngine.getSingleton();
        mSyncQueue = new SyncQueue(mSyncStorageEngine);

        mContext = context;
        
        mSyncThread = new HandlerThread("SyncHandlerThread", Process.THREAD_PRIORITY_BACKGROUND);
        mSyncThread.start();
        mSyncHandler = new SyncHandler(mSyncThread.getLooper());

        mPackageManager = null;
>>>>>>> 54b6cfa... Initial Contribution

        mSyncAlarmIntent = PendingIntent.getBroadcast(
                mContext, 0 /* ignored */, new Intent(ACTION_SYNC_ALARM), 0);

<<<<<<< HEAD
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(mConnectivityIntentReceiver, intentFilter);

        if (!factoryTest) {
            intentFilter = new IntentFilter(Intent.ACTION_BOOT_COMPLETED);
            context.registerReceiver(mBootCompletedReceiver, intentFilter);
        }

        intentFilter = new IntentFilter(ConnectivityManager.ACTION_BACKGROUND_DATA_SETTING_CHANGED);
        context.registerReceiver(mBackgroundDataSettingChanged, intentFilter);

=======
        mSyncPollAlarmIntent = PendingIntent.getBroadcast(
                mContext, 0 /* ignored */, new Intent(SYNC_POLL_ALARM), 0);

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(mConnectivityIntentReceiver, intentFilter);

>>>>>>> 54b6cfa... Initial Contribution
        intentFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
        intentFilter.addAction(Intent.ACTION_DEVICE_STORAGE_OK);
        context.registerReceiver(mStorageIntentReceiver, intentFilter);

<<<<<<< HEAD
        intentFilter = new IntentFilter(Intent.ACTION_SHUTDOWN);
        intentFilter.setPriority(100);
        context.registerReceiver(mShutdownIntentReceiver, intentFilter);

        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_USER_REMOVED);
        mContext.registerReceiver(mUserIntentReceiver, intentFilter);

=======
>>>>>>> 54b6cfa... Initial Contribution
        if (!factoryTest) {
            mNotificationMgr = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
            context.registerReceiver(new SyncAlarmIntentReceiver(),
                    new IntentFilter(ACTION_SYNC_ALARM));
        } else {
            mNotificationMgr = null;
        }
<<<<<<< HEAD
        mPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
=======
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mSyncWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, SYNC_WAKE_LOCK);
        mSyncWakeLock.setReferenceCounted(false);
>>>>>>> 54b6cfa... Initial Contribution

        // This WakeLock is used to ensure that we stay awake between the time that we receive
        // a sync alarm notification and when we finish processing it. We need to do this
        // because we don't do the work in the alarm handler, rather we do it in a message
        // handler.
<<<<<<< HEAD
        mHandleAlarmWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                HANDLE_SYNC_ALARM_WAKE_LOCK);
        mHandleAlarmWakeLock.setReferenceCounted(false);

        // This WakeLock is used to ensure that we stay awake while running the sync loop
        // message handler. Normally we will hold a sync adapter wake lock while it is being
        // synced but during the execution of the sync loop it might finish a sync for
        // one sync adapter before starting the sync for the other sync adapter and we
        // don't want the device to go to sleep during that window.
        mSyncManagerWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                SYNC_LOOP_WAKE_LOCK);
        mSyncManagerWakeLock.setReferenceCounted(false);

        mSyncStorageEngine.addStatusChangeListener(
                ContentResolver.SYNC_OBSERVER_TYPE_SETTINGS, new ISyncStatusObserver.Stub() {
            public void onStatusChanged(int which) {
                // force the sync loop to run if the settings change
                sendCheckAlarmsMessage();
            }
        });

        if (!factoryTest) {
            AccountManager.get(mContext).addOnAccountsUpdatedListener(SyncManager.this,
                mSyncHandler, false /* updateImmediately */);
            // do this synchronously to ensure we have the accounts before this call returns
            onAccountsUpdated(null);
        }

        // Pick a random second in a day to seed all periodic syncs
        mSyncRandomOffsetMillis = mSyncStorageEngine.getSyncRandomOffset() * 1000;
=======
        mHandleAlarmWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                HANDLE_SYNC_ALARM_WAKE_LOCK);
        mHandleAlarmWakeLock.setReferenceCounted(false);

        if (!factoryTest) {
            AccountMonitorListener listener = new AccountMonitorListener() {
                public void onAccountsUpdated(String[] accounts) {
                    final boolean hadAccountsAlready = mAccounts != null;
                    // copy the accounts into a new array and change mAccounts to point to it
                    String[] newAccounts = new String[accounts.length];
                    System.arraycopy(accounts, 0, newAccounts, 0, accounts.length);
                    mAccounts = newAccounts;

                    // if a sync is in progress yet it is no longer in the accounts list, cancel it
                    ActiveSyncContext activeSyncContext = mActiveSyncContext;
                    if (activeSyncContext != null) {
                        if (!ArrayUtils.contains(newAccounts,
                                activeSyncContext.mSyncOperation.account)) {
                            Log.d(TAG, "canceling sync since the account has been removed");
                            sendSyncFinishedOrCanceledMessage(activeSyncContext,
                                    null /* no result since this is a cancel */);
                        }
                    }

                    // we must do this since we don't bother scheduling alarms when
                    // the accounts are not set yet
                    sendCheckAlarmsMessage();

                    mSyncStorageEngine.doDatabaseCleanup(accounts);

                    if (hadAccountsAlready && mAccounts.length > 0) {
                        // request a sync so that if the password was changed we will retry any sync
                        // that failed when it was wrong
                        startSync(null /* all providers */, null /* no extras */);
                    }
                }
            };
            mAccountMonitor = new AccountMonitor(context, listener);
        }
    }

    private synchronized void initializeSyncPoll() {
        if (mSyncPollInitialized) return;
        mSyncPollInitialized = true;

        mContext.registerReceiver(new SyncPollAlarmReceiver(), new IntentFilter(SYNC_POLL_ALARM));

        // load the next poll time from shared preferences
        long absoluteAlarmTime = readSyncPollTime();

        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "initializeSyncPoll: absoluteAlarmTime is " + absoluteAlarmTime);
        }

        // Convert absoluteAlarmTime to elapsed realtime. If this time was in the past then
        // schedule the poll immediately, if it is too far in the future then cap it at
        // MAX_SYNC_POLL_DELAY_SECONDS.
        long absoluteNow = System.currentTimeMillis();
        long relativeNow = SystemClock.elapsedRealtime();
        long relativeAlarmTime = relativeNow;
        if (absoluteAlarmTime > absoluteNow) {
            long delayInMs = absoluteAlarmTime - absoluteNow;
            final int maxDelayInMs = MAX_SYNC_POLL_DELAY_SECONDS * 1000;
            if (delayInMs > maxDelayInMs) {
                delayInMs = MAX_SYNC_POLL_DELAY_SECONDS * 1000;
            }
            relativeAlarmTime += delayInMs;
        }

        // schedule an alarm for the next poll time
        scheduleSyncPollAlarm(relativeAlarmTime);
    }

    private void scheduleSyncPollAlarm(long relativeAlarmTime) {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "scheduleSyncPollAlarm: relativeAlarmTime is " + relativeAlarmTime
                    + ", now is " + SystemClock.elapsedRealtime()
                    + ", delay is " + (relativeAlarmTime - SystemClock.elapsedRealtime()));
        }
        ensureAlarmService();
        mAlarmService.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, relativeAlarmTime,
                mSyncPollAlarmIntent);
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Return a random value v that satisfies minValue <= v < maxValue. The difference between
     * maxValue and minValue must be less than Integer.MAX_VALUE.
     */
    private long jitterize(long minValue, long maxValue) {
        Random random = new Random(SystemClock.elapsedRealtime());
        long spread = maxValue - minValue;
        if (spread > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("the difference between the maxValue and the "
                    + "minValue must be less than " + Integer.MAX_VALUE);
        }
        return minValue + random.nextInt((int)spread);
    }

<<<<<<< HEAD
    public SyncStorageEngine getSyncStorageEngine() {
        return mSyncStorageEngine;
=======
    private void handleSyncPollAlarm() {
        // determine the next poll time
        long delayMs = jitterize(MIN_SYNC_POLL_DELAY_SECONDS, MAX_SYNC_POLL_DELAY_SECONDS) * 1000;
        long nextRelativePollTimeMs = SystemClock.elapsedRealtime() + delayMs;

        if (Log.isLoggable(TAG, Log.VERBOSE)) Log.v(TAG, "handleSyncPollAlarm: delay " + delayMs);

        // write the absolute time to shared preferences
        writeSyncPollTime(System.currentTimeMillis() + delayMs);

        // schedule an alarm for the next poll time
        scheduleSyncPollAlarm(nextRelativePollTimeMs);

        // perform a poll
        scheduleSync(null /* sync all syncable providers */, new Bundle(), 0 /* no delay */);
    }

    private void writeSyncPollTime(long when) {
        File f = new File(SYNCMANAGER_PREFS_FILENAME);
        DataOutputStream str = null;
        try {
            str = new DataOutputStream(new FileOutputStream(f));
            str.writeLong(when);
        } catch (FileNotFoundException e) {
            Log.w(TAG, "error writing to file " + f, e);
        } catch (IOException e) {
            Log.w(TAG, "error writing to file " + f, e);
        } finally {
            if (str != null) {
                try {
                    str.close();
                } catch (IOException e) {
                    Log.w(TAG, "error closing file " + f, e);
                }
            }
        }
    }

    private long readSyncPollTime() {
        File f = new File(SYNCMANAGER_PREFS_FILENAME);

        DataInputStream str = null;
        try {
            str = new DataInputStream(new FileInputStream(f));
            return str.readLong();
        } catch (FileNotFoundException e) {
            writeSyncPollTime(0);
        } catch (IOException e) {
            Log.w(TAG, "error reading file " + f, e);
        } finally {
            if (str != null) {
                try {
                    str.close();
                } catch (IOException e) {
                    Log.w(TAG, "error closing file " + f, e);
                }
            }
        }
        return 0;
    }

    public ActiveSyncContext getActiveSyncContext() {
        return mActiveSyncContext;
    }

    private Sync.Settings.QueryMap getSyncSettings() {
        if (mSyncSettings == null) {
            mSyncSettings = new Sync.Settings.QueryMap(mContext.getContentResolver(), true,
                    new Handler());
            mSyncSettings.addObserver(new Observer(){
                public void update(Observable o, Object arg) {
                    // force the sync loop to run if the settings change
                    sendCheckAlarmsMessage();
                }
            });
        }
        return mSyncSettings;
    }

    private void ensureContentResolver() {
        if (mContentResolver == null) {
            mContentResolver = mContext.getContentResolver();
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    private void ensureAlarmService() {
        if (mAlarmService == null) {
            mAlarmService = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        }
    }

<<<<<<< HEAD
=======
    public String getSyncingAccount() {
        ActiveSyncContext activeSyncContext = mActiveSyncContext;
        return (activeSyncContext != null) ? activeSyncContext.mSyncOperation.account : null;
    }

    /**
     * Returns whether or not sync is enabled.  Sync can be enabled by
     * setting the system property "ro.config.sync" to the value "yes".
     * This is normally done at boot time on builds that support sync.
     * @return true if sync is enabled
     */
    private boolean isSyncEnabled() {
        // Require the precise value "yes" to discourage accidental activation.
        return "yes".equals(SystemProperties.get("ro.config.sync"));
    }
    
>>>>>>> 54b6cfa... Initial Contribution
    /**
     * Initiate a sync. This can start a sync for all providers
     * (pass null to url, set onlyTicklable to false), only those
     * providers that are marked as ticklable (pass null to url,
     * set onlyTicklable to true), or a specific provider (set url
     * to the content url of the provider).
     *
     * <p>If the ContentResolver.SYNC_EXTRAS_UPLOAD boolean in extras is
     * true then initiate a sync that just checks for local changes to send
     * to the server, otherwise initiate a sync that first gets any
     * changes from the server before sending local changes back to
     * the server.
     *
     * <p>If a specific provider is being synced (the url is non-null)
     * then the extras can contain SyncAdapter-specific information
     * to control what gets synced (e.g. which specific feed to sync).
     *
     * <p>You'll start getting callbacks after this.
     *
<<<<<<< HEAD
     * @param requestedAccount the account to sync, may be null to signify all accounts
     * @param userId the id of the user whose accounts are to be synced. If userId is USER_ALL,
     *          then all users' accounts are considered.
     * @param requestedAuthority the authority to sync, may be null to indicate all authorities
     * @param extras a Map of SyncAdapter-specific information to control
     *          syncs of a specific provider. Can be null. Is ignored
     *          if the url is null.
     * @param delay how many milliseconds in the future to wait before performing this
     * @param onlyThoseWithUnkownSyncableState
     */
    public void scheduleSync(Account requestedAccount, int userId, String requestedAuthority,
            Bundle extras, long delay, boolean onlyThoseWithUnkownSyncableState) {
        boolean isLoggable = Log.isLoggable(TAG, Log.VERBOSE);

        final boolean backgroundDataUsageAllowed = !mBootCompleted ||
                getConnectivityManager().getBackgroundDataSetting();
=======
     * @param url The Uri of a specific provider to be synced, or
     *          null to sync all providers.
     * @param extras a Map of SyncAdapter-specific information to control
*          syncs of a specific provider. Can be null. Is ignored
*          if the url is null.
     * @param delay how many milliseconds in the future to wait before performing this
     *   sync. -1 means to make this the next sync to perform. 
     */
    public void scheduleSync(Uri url, Bundle extras, long delay) {
        boolean isLoggable = Log.isLoggable(TAG, Log.VERBOSE);
        if (isLoggable) {
            Log.v(TAG, "scheduleSync:"
                    + " delay " + delay
                    + ", url " + ((url == null) ? "(null)" : url)
                    + ", extras " + ((extras == null) ? "(null)" : extras));
        }

        if (!isSyncEnabled()) {
            if (isLoggable) {
                Log.v(TAG, "not syncing because sync is disabled");
            }
            setStatusText("Sync is disabled.");
            return;
        }

        if (mAccounts == null) setStatusText("The accounts aren't known yet.");
        if (!mDataConnectionIsConnected) setStatusText("No data connection");
        if (mStorageIsLow) setStatusText("Memory low");
>>>>>>> 54b6cfa... Initial Contribution

        if (extras == null) extras = new Bundle();

        Boolean expedited = extras.getBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, false);
        if (expedited) {
            delay = -1; // this means schedule at the front of the queue
        }

<<<<<<< HEAD
        AccountAndUser[] accounts;
        if (requestedAccount != null && userId != UserId.USER_ALL) {
            accounts = new AccountAndUser[] { new AccountAndUser(requestedAccount, userId) };
=======
        String[] accounts;
        String accountFromExtras = extras.getString(ContentResolver.SYNC_EXTRAS_ACCOUNT);
        if (!TextUtils.isEmpty(accountFromExtras)) {
            accounts = new String[]{accountFromExtras};
>>>>>>> 54b6cfa... Initial Contribution
        } else {
            // if the accounts aren't configured yet then we can't support an account-less
            // sync request
            accounts = mAccounts;
<<<<<<< HEAD
=======
            if (accounts == null) {
                // not ready yet
                if (isLoggable) {
                    Log.v(TAG, "scheduleSync: no accounts yet, dropping");
                }
                return;
            }
>>>>>>> 54b6cfa... Initial Contribution
            if (accounts.length == 0) {
                if (isLoggable) {
                    Log.v(TAG, "scheduleSync: no accounts configured, dropping");
                }
<<<<<<< HEAD
=======
                setStatusText("No accounts are configured.");
>>>>>>> 54b6cfa... Initial Contribution
                return;
            }
        }

        final boolean uploadOnly = extras.getBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, false);
<<<<<<< HEAD
        final boolean manualSync = extras.getBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, false);
        if (manualSync) {
            extras.putBoolean(ContentResolver.SYNC_EXTRAS_IGNORE_BACKOFF, true);
            extras.putBoolean(ContentResolver.SYNC_EXTRAS_IGNORE_SETTINGS, true);
        }
        final boolean ignoreSettings =
                extras.getBoolean(ContentResolver.SYNC_EXTRAS_IGNORE_SETTINGS, false);

        int source;
        if (uploadOnly) {
            source = SyncStorageEngine.SOURCE_LOCAL;
        } else if (manualSync) {
            source = SyncStorageEngine.SOURCE_USER;
        } else if (requestedAuthority == null) {
            source = SyncStorageEngine.SOURCE_POLL;
        } else {
            // this isn't strictly server, since arbitrary callers can (and do) request
            // a non-forced two-way sync on a specific url
            source = SyncStorageEngine.SOURCE_SERVER;
        }

        // Compile a list of authorities that have sync adapters.
        // For each authority sync each account that matches a sync adapter.
        final HashSet<String> syncableAuthorities = new HashSet<String>();
        for (RegisteredServicesCache.ServiceInfo<SyncAdapterType> syncAdapter :
                mSyncAdapters.getAllServices()) {
            syncableAuthorities.add(syncAdapter.type.authority);
        }

        // if the url was specified then replace the list of authorities with just this authority
        // or clear it if this authority isn't syncable
        if (requestedAuthority != null) {
            final boolean hasSyncAdapter = syncableAuthorities.contains(requestedAuthority);
            syncableAuthorities.clear();
            if (hasSyncAdapter) syncableAuthorities.add(requestedAuthority);
        }

        for (String authority : syncableAuthorities) {
            for (AccountAndUser account : accounts) {
                int isSyncable = mSyncStorageEngine.getIsSyncable(account.account, account.userId,
                        authority);
                if (isSyncable == 0) {
                    continue;
                }
                final RegisteredServicesCache.ServiceInfo<SyncAdapterType> syncAdapterInfo =
                        mSyncAdapters.getServiceInfo(
                                SyncAdapterType.newKey(authority, account.account.type));
                if (syncAdapterInfo == null) {
                    continue;
                }
                final boolean allowParallelSyncs = syncAdapterInfo.type.allowParallelSyncs();
                final boolean isAlwaysSyncable = syncAdapterInfo.type.isAlwaysSyncable();
                if (isSyncable < 0 && isAlwaysSyncable) {
                    mSyncStorageEngine.setIsSyncable(account.account, account.userId, authority, 1);
                    isSyncable = 1;
                }
                if (onlyThoseWithUnkownSyncableState && isSyncable >= 0) {
                    continue;
                }
                if (!syncAdapterInfo.type.supportsUploading() && uploadOnly) {
                    continue;
                }

                // always allow if the isSyncable state is unknown
                boolean syncAllowed =
                        (isSyncable < 0)
                        || ignoreSettings
                        || (backgroundDataUsageAllowed
                                && mSyncStorageEngine.getMasterSyncAutomatically(account.userId)
                                && mSyncStorageEngine.getSyncAutomatically(account.account,
                                        account.userId, authority));
                if (!syncAllowed) {
                    if (isLoggable) {
                        Log.d(TAG, "scheduleSync: sync of " + account + ", " + authority
                                + " is not allowed, dropping request");
                    }
                    continue;
                }

                Pair<Long, Long> backoff = mSyncStorageEngine
                        .getBackoff(account.account, account.userId, authority);
                long delayUntil = mSyncStorageEngine.getDelayUntilTime(account.account,
                        account.userId, authority);
                final long backoffTime = backoff != null ? backoff.first : 0;
                if (isSyncable < 0) {
                    Bundle newExtras = new Bundle();
                    newExtras.putBoolean(ContentResolver.SYNC_EXTRAS_INITIALIZE, true);
                    if (isLoggable) {
                        Log.v(TAG, "scheduleSync:"
                                + " delay " + delay
                                + ", source " + source
                                + ", account " + account
                                + ", authority " + authority
                                + ", extras " + newExtras);
                    }
                    scheduleSyncOperation(
                            new SyncOperation(account.account, account.userId, source, authority,
                                    newExtras, 0, backoffTime, delayUntil, allowParallelSyncs));
                }
                if (!onlyThoseWithUnkownSyncableState) {
                    if (isLoggable) {
                        Log.v(TAG, "scheduleSync:"
                                + " delay " + delay
                                + ", source " + source
                                + ", account " + account
                                + ", authority " + authority
                                + ", extras " + extras);
                    }
                    scheduleSyncOperation(
                            new SyncOperation(account.account, account.userId, source, authority,
                                    extras, delay, backoffTime, delayUntil, allowParallelSyncs));
                }
            }
        }
    }

    public void scheduleLocalSync(Account account, int userId, String authority) {
        final Bundle extras = new Bundle();
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, true);
        scheduleSync(account, userId, authority, extras, LOCAL_SYNC_DELAY,
                false /* onlyThoseWithUnkownSyncableState */);
    }

    public SyncAdapterType[] getSyncAdapterTypes() {
        final Collection<RegisteredServicesCache.ServiceInfo<SyncAdapterType>>
                serviceInfos =
                mSyncAdapters.getAllServices();
        SyncAdapterType[] types = new SyncAdapterType[serviceInfos.size()];
        int i = 0;
        for (RegisteredServicesCache.ServiceInfo<SyncAdapterType> serviceInfo : serviceInfos) {
            types[i] = serviceInfo.type;
            ++i;
        }
        return types;
=======
        final boolean force = extras.getBoolean(ContentResolver.SYNC_EXTRAS_FORCE, false);

        int source;
        if (uploadOnly) {
            source = Sync.History.SOURCE_LOCAL;
        } else if (force) {
            source = Sync.History.SOURCE_USER;
        } else if (url == null) {
            source = Sync.History.SOURCE_POLL;
        } else {
            // this isn't strictly server, since arbitrary callers can (and do) request
            // a non-forced two-way sync on a specific url
            source = Sync.History.SOURCE_SERVER;
        }

        List<String> names = new ArrayList<String>();
        List<ProviderInfo> providers = new ArrayList<ProviderInfo>();
        populateProvidersList(url, names, providers);

        final int numProviders = providers.size();
        for (int i = 0; i < numProviders; i++) {
            if (!providers.get(i).isSyncable) continue;
            final String name = names.get(i);
            for (String account : accounts) {
                scheduleSyncOperation(new SyncOperation(account, source, name, extras, delay));
                // TODO: remove this when Calendar supports multiple accounts. Until then
                // pretend that only the first account exists when syncing calendar.
                if ("calendar".equals(name)) {
                    break;
                }
            }
        }
    }

    private void setStatusText(String message) {
        mStatusText = message;
    }

    private void populateProvidersList(Uri url, List<String> names, List<ProviderInfo> providers) {
        try {
            final IPackageManager packageManager = getPackageManager();
            if (url == null) {
                packageManager.querySyncProviders(names, providers);
            } else {
                final String authority = url.getAuthority();
                ProviderInfo info = packageManager.resolveContentProvider(url.getAuthority(), 0);
                if (info != null) {
                    // only set this provider if the requested authority is the primary authority
                    String[] providerNames = info.authority.split(";");
                    if (url.getAuthority().equals(providerNames[0])) {
                        names.add(authority);
                        providers.add(info);
                    }
                }
            }
        } catch (RemoteException ex) {
            // we should really never get this, but if we do then clear the lists, which
            // will result in the dropping of the sync request
            Log.e(TAG, "error trying to get the ProviderInfo for " + url, ex);
            names.clear();
            providers.clear();
        }
    }

    public void scheduleLocalSync(Uri url) {
        final Bundle extras = new Bundle();
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, true);
        scheduleSync(url, extras, LOCAL_SYNC_DELAY);
    }

    private IPackageManager getPackageManager() {
        // Don't bother synchronizing on this. The worst that can happen is that two threads
        // can try to get the package manager at the same time but only one result gets
        // used. Since there is only one package manager in the system this doesn't matter.
        if (mPackageManager == null) {
            IBinder b = ServiceManager.getService("package");
            mPackageManager = IPackageManager.Stub.asInterface(b);
        }
        return mPackageManager;
    }

    /**
     * Initiate a sync for this given URL, or pass null for a full sync.
     *
     * <p>You'll start getting callbacks after this.
     *
     * @param url The Uri of a specific provider to be synced, or
     *          null to sync all providers.
     * @param extras a Map of SyncAdapter specific information to control
     *          syncs of a specific provider. Can be null. Is ignored
     */
    public void startSync(Uri url, Bundle extras) {
        scheduleSync(url, extras, 0 /* no delay */);
    }

    public void updateHeartbeatTime() {
        mHeartbeatTime = SystemClock.elapsedRealtime();
        ensureContentResolver();
        mContentResolver.notifyChange(Sync.Active.CONTENT_URI,
                null /* this change wasn't made through an observer */);
>>>>>>> 54b6cfa... Initial Contribution
    }

    private void sendSyncAlarmMessage() {
        if (Log.isLoggable(TAG, Log.VERBOSE)) Log.v(TAG, "sending MESSAGE_SYNC_ALARM");
        mSyncHandler.sendEmptyMessage(SyncHandler.MESSAGE_SYNC_ALARM);
    }

    private void sendCheckAlarmsMessage() {
        if (Log.isLoggable(TAG, Log.VERBOSE)) Log.v(TAG, "sending MESSAGE_CHECK_ALARMS");
<<<<<<< HEAD
        mSyncHandler.removeMessages(SyncHandler.MESSAGE_CHECK_ALARMS);
=======
>>>>>>> 54b6cfa... Initial Contribution
        mSyncHandler.sendEmptyMessage(SyncHandler.MESSAGE_CHECK_ALARMS);
    }

    private void sendSyncFinishedOrCanceledMessage(ActiveSyncContext syncContext,
            SyncResult syncResult) {
        if (Log.isLoggable(TAG, Log.VERBOSE)) Log.v(TAG, "sending MESSAGE_SYNC_FINISHED");
        Message msg = mSyncHandler.obtainMessage();
        msg.what = SyncHandler.MESSAGE_SYNC_FINISHED;
        msg.obj = new SyncHandlerMessagePayload(syncContext, syncResult);
        mSyncHandler.sendMessage(msg);
    }

<<<<<<< HEAD
    private void sendCancelSyncsMessage(final Account account, final int userId,
            final String authority) {
        if (Log.isLoggable(TAG, Log.VERBOSE)) Log.v(TAG, "sending MESSAGE_CANCEL");
        Message msg = mSyncHandler.obtainMessage();
        msg.what = SyncHandler.MESSAGE_CANCEL;
        msg.obj = Pair.create(account, authority);
        msg.arg1 = userId;
        mSyncHandler.sendMessage(msg);
    }

    class SyncHandlerMessagePayload {
        public final ActiveSyncContext activeSyncContext;
        public final SyncResult syncResult;

=======
    class SyncHandlerMessagePayload {
        public final ActiveSyncContext activeSyncContext;
        public final SyncResult syncResult;
        
>>>>>>> 54b6cfa... Initial Contribution
        SyncHandlerMessagePayload(ActiveSyncContext syncContext, SyncResult syncResult) {
            this.activeSyncContext = syncContext;
            this.syncResult = syncResult;
        }
    }

    class SyncAlarmIntentReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            mHandleAlarmWakeLock.acquire();
            sendSyncAlarmMessage();
        }
    }

<<<<<<< HEAD
    private void clearBackoffSetting(SyncOperation op) {
        mSyncStorageEngine.setBackoff(op.account, op.userId, op.authority,
                SyncStorageEngine.NOT_IN_BACKOFF_MODE, SyncStorageEngine.NOT_IN_BACKOFF_MODE);
        synchronized (mSyncQueue) {
            mSyncQueue.onBackoffChanged(op.account, op.userId, op.authority, 0);
        }
    }

    private void increaseBackoffSetting(SyncOperation op) {
        // TODO: Use this function to align it to an already scheduled sync
        //       operation in the specified window
        final long now = SystemClock.elapsedRealtime();

        final Pair<Long, Long> previousSettings =
                mSyncStorageEngine.getBackoff(op.account, op.userId, op.authority);
        long newDelayInMs = -1;
        if (previousSettings != null) {
            // don't increase backoff before current backoff is expired. This will happen for op's
            // with ignoreBackoff set.
            if (now < previousSettings.first) {
                if (Log.isLoggable(TAG, Log.VERBOSE)) {
                    Log.v(TAG, "Still in backoff, do not increase it. "
                        + "Remaining: " + ((previousSettings.first - now) / 1000) + " seconds.");
                }
                return;
            }
            // Subsequent delays are the double of the previous delay
            newDelayInMs = previousSettings.second * 2;
        }
        if (newDelayInMs <= 0) {
            // The initial delay is the jitterized INITIAL_SYNC_RETRY_TIME_IN_MS
            newDelayInMs = jitterize(INITIAL_SYNC_RETRY_TIME_IN_MS,
                    (long)(INITIAL_SYNC_RETRY_TIME_IN_MS * 1.1));
        }

        // Cap the delay
        long maxSyncRetryTimeInSeconds = Settings.Secure.getLong(mContext.getContentResolver(),
                Settings.Secure.SYNC_MAX_RETRY_DELAY_IN_SECONDS,
=======
    class SyncPollAlarmReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            handleSyncPollAlarm();
        }
    }

    private void rescheduleImmediately(SyncOperation syncOperation) {
        SyncOperation rescheduledSyncOperation = new SyncOperation(syncOperation);
        rescheduledSyncOperation.setDelay(0);
        scheduleSyncOperation(rescheduledSyncOperation);
    }

    private long rescheduleWithDelay(SyncOperation syncOperation) {
        long newDelayInMs;

        if (syncOperation.delay == 0) {
            // The initial delay is the jitterized INITIAL_SYNC_RETRY_TIME_IN_MS
            newDelayInMs = jitterize(INITIAL_SYNC_RETRY_TIME_IN_MS,
                    (long)(INITIAL_SYNC_RETRY_TIME_IN_MS * 1.1));
        } else {
            // Subsequent delays are the double of the previous delay
            newDelayInMs = syncOperation.delay * 2;
        }

        // Cap the delay
        ensureContentResolver();
        long maxSyncRetryTimeInSeconds = Settings.Gservices.getLong(mContentResolver,
                Settings.Gservices.SYNC_MAX_RETRY_DELAY_IN_SECONDS,
>>>>>>> 54b6cfa... Initial Contribution
                DEFAULT_MAX_SYNC_RETRY_TIME_IN_SECONDS);
        if (newDelayInMs > maxSyncRetryTimeInSeconds * 1000) {
            newDelayInMs = maxSyncRetryTimeInSeconds * 1000;
        }
<<<<<<< HEAD

        final long backoff = now + newDelayInMs;

        mSyncStorageEngine.setBackoff(op.account, op.userId, op.authority,
                backoff, newDelayInMs);

        op.backoff = backoff;
        op.updateEffectiveRunTime();

        synchronized (mSyncQueue) {
            mSyncQueue.onBackoffChanged(op.account, op.userId, op.authority, backoff);
        }
    }

    private void setDelayUntilTime(SyncOperation op, long delayUntilSeconds) {
        final long delayUntil = delayUntilSeconds * 1000;
        final long absoluteNow = System.currentTimeMillis();
        long newDelayUntilTime;
        if (delayUntil > absoluteNow) {
            newDelayUntilTime = SystemClock.elapsedRealtime() + (delayUntil - absoluteNow);
        } else {
            newDelayUntilTime = 0;
        }
        mSyncStorageEngine
                .setDelayUntilTime(op.account, op.userId, op.authority, newDelayUntilTime);
        synchronized (mSyncQueue) {
            mSyncQueue.onDelayUntilTimeChanged(op.account, op.authority, newDelayUntilTime);
        }
    }

    /**
     * Cancel the active sync if it matches the authority and account.
     * @param account limit the cancelations to syncs with this account, if non-null
     * @param authority limit the cancelations to syncs with this authority, if non-null
     */
    public void cancelActiveSync(Account account, int userId, String authority) {
        sendCancelSyncsMessage(account, userId, authority);
=======
        
        SyncOperation rescheduledSyncOperation = new SyncOperation(syncOperation);
        rescheduledSyncOperation.setDelay(newDelayInMs);
        scheduleSyncOperation(rescheduledSyncOperation);
        return newDelayInMs;
    }

    /**
     * Cancel the active sync if it matches the uri. The uri corresponds to the one passed
     * in to startSync().
     * @param uri If non-null, the active sync is only canceled if it matches the uri.
     *   If null, any active sync is canceled.
     */
    public void cancelActiveSync(Uri uri) {
        ActiveSyncContext activeSyncContext = mActiveSyncContext;
        if (activeSyncContext != null) {
            // if a Uri was specified then only cancel the sync if it matches the the uri
            if (uri != null) {
                if (!uri.getAuthority().equals(activeSyncContext.mSyncOperation.authority)) {
                    return;
                }
            }
            sendSyncFinishedOrCanceledMessage(activeSyncContext,
                    null /* no result since this is a cancel */);
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Create and schedule a SyncOperation.
     *
     * @param syncOperation the SyncOperation to schedule
     */
    public void scheduleSyncOperation(SyncOperation syncOperation) {
<<<<<<< HEAD
        boolean queueChanged;
        synchronized (mSyncQueue) {
            queueChanged = mSyncQueue.add(syncOperation);
        }

        if (queueChanged) {
=======
        // If this operation is expedited and there is a sync in progress then
        // reschedule the current operation and send a cancel for it.
        final boolean expedited = syncOperation.delay < 0;
        final ActiveSyncContext activeSyncContext = mActiveSyncContext;
        if (expedited && activeSyncContext != null) {
            final boolean activeIsExpedited = activeSyncContext.mSyncOperation.delay < 0;
            final boolean hasSameKey =
                    activeSyncContext.mSyncOperation.key.equals(syncOperation.key);
            // This request is expedited and there is a sync in progress.
            // Interrupt the current sync only if it is not expedited and if it has a different
            // key than the one we are scheduling.
            if (!activeIsExpedited && !hasSameKey) {
                rescheduleImmediately(activeSyncContext.mSyncOperation);
                sendSyncFinishedOrCanceledMessage(activeSyncContext, 
                        null /* no result since this is a cancel */);
            }
        }

        boolean operationEnqueued;
        synchronized (mSyncQueue) {
            operationEnqueued = mSyncQueue.add(syncOperation);
        }

        if (operationEnqueued) {
>>>>>>> 54b6cfa... Initial Contribution
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "scheduleSyncOperation: enqueued " + syncOperation);
            }
            sendCheckAlarmsMessage();
        } else {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "scheduleSyncOperation: dropping duplicate sync operation "
                        + syncOperation);
            }
        }
    }

    /**
<<<<<<< HEAD
     * Remove scheduled sync operations.
     * @param account limit the removals to operations with this account, if non-null
     * @param authority limit the removals to operations with this authority, if non-null
     */
    public void clearScheduledSyncOperations(Account account, int userId, String authority) {
        synchronized (mSyncQueue) {
            mSyncQueue.remove(account, userId, authority);
        }
        mSyncStorageEngine.setBackoff(account, userId, authority,
                SyncStorageEngine.NOT_IN_BACKOFF_MODE, SyncStorageEngine.NOT_IN_BACKOFF_MODE);
    }

    void maybeRescheduleSync(SyncResult syncResult, SyncOperation operation) {
        boolean isLoggable = Log.isLoggable(TAG, Log.DEBUG);
        if (isLoggable) {
            Log.d(TAG, "encountered error(s) during the sync: " + syncResult + ", " + operation);
        }

        operation = new SyncOperation(operation);

        // The SYNC_EXTRAS_IGNORE_BACKOFF only applies to the first attempt to sync a given
        // request. Retries of the request will always honor the backoff, so clear the
        // flag in case we retry this request.
        if (operation.extras.getBoolean(ContentResolver.SYNC_EXTRAS_IGNORE_BACKOFF, false)) {
            operation.extras.remove(ContentResolver.SYNC_EXTRAS_IGNORE_BACKOFF);
        }

        // If this sync aborted because the internal sync loop retried too many times then
        //   don't reschedule. Otherwise we risk getting into a retry loop.
=======
     * Remove any scheduled sync operations that match uri. The uri corresponds to the one passed
     * in to startSync().
     * @param uri If non-null, only operations that match the uri are cleared.
     *   If null, all operations are cleared.
     */
    public void clearScheduledSyncOperations(Uri uri) {
        synchronized (mSyncQueue) {
            mSyncQueue.clear(null, uri != null ? uri.getAuthority() : null);
        }
    }

    void maybeRescheduleSync(SyncResult syncResult, SyncOperation previousSyncOperation) {
        boolean isLoggable = Log.isLoggable(TAG, Log.DEBUG);
        if (isLoggable) {
            Log.d(TAG, "encountered error(s) during the sync: " + syncResult + ", "
                    + previousSyncOperation);
        }

>>>>>>> 54b6cfa... Initial Contribution
        // If the operation succeeded to some extent then retry immediately.
        // If this was a two-way sync then retry soft errors with an exponential backoff.
        // If this was an upward sync then schedule a two-way sync immediately.
        // Otherwise do not reschedule.
<<<<<<< HEAD
        if (operation.extras.getBoolean(ContentResolver.SYNC_EXTRAS_DO_NOT_RETRY, false)) {
            Log.d(TAG, "not retrying sync operation because SYNC_EXTRAS_DO_NOT_RETRY was specified "
                    + operation);
        } else if (operation.extras.getBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, false)
                && !syncResult.syncAlreadyInProgress) {
            operation.extras.remove(ContentResolver.SYNC_EXTRAS_UPLOAD);
            Log.d(TAG, "retrying sync operation as a two-way sync because an upload-only sync "
                    + "encountered an error: " + operation);
            scheduleSyncOperation(operation);
        } else if (syncResult.tooManyRetries) {
            Log.d(TAG, "not retrying sync operation because it retried too many times: "
                    + operation);
        } else if (syncResult.madeSomeProgress()) {
            if (isLoggable) {
                Log.d(TAG, "retrying sync operation because even though it had an error "
                        + "it achieved some success");
            }
            scheduleSyncOperation(operation);
        } else if (syncResult.syncAlreadyInProgress) {
            if (isLoggable) {
                Log.d(TAG, "retrying sync operation that failed because there was already a "
                        + "sync in progress: " + operation);
            }
            scheduleSyncOperation(new SyncOperation(operation.account, operation.userId,
                    operation.syncSource,
                    operation.authority, operation.extras,
                    DELAY_RETRY_SYNC_IN_PROGRESS_IN_SECONDS * 1000,
                    operation.backoff, operation.delayUntil, operation.allowParallelSyncs));
        } else if (syncResult.hasSoftError()) {
            if (isLoggable) {
                Log.d(TAG, "retrying sync operation because it encountered a soft error: "
                        + operation);
            }
            scheduleSyncOperation(operation);
        } else {
            Log.d(TAG, "not retrying sync operation because the error is a hard error: "
                    + operation);
        }
    }

    private void onUserRemoved(Intent intent) {
        int userId = intent.getIntExtra(Intent.EXTRA_USERID, -1);
        if (userId == -1) return;

        // Clean up the storage engine database
        mSyncStorageEngine.doDatabaseCleanup(new Account[0], userId);
        onAccountsUpdated(null);
        synchronized (mSyncQueue) {
            mSyncQueue.removeUser(userId);
=======

        if (syncResult.madeSomeProgress()) {
            if (isLoggable) {
                Log.d(TAG, "retrying sync operation immediately because "
                        + "even though it had an error it achieved some success");
            }
            rescheduleImmediately(previousSyncOperation);
        } else if (previousSyncOperation.extras.getBoolean(
                ContentResolver.SYNC_EXTRAS_UPLOAD, false)) {
            final SyncOperation newSyncOperation = new SyncOperation(previousSyncOperation);
            newSyncOperation.extras.putBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, false);
            newSyncOperation.setDelay(0);
            if (Config.LOGD) {
                Log.d(TAG, "retrying sync operation as a two-way sync because an upload-only sync "
                        + "encountered an error: " + previousSyncOperation);
            }
            scheduleSyncOperation(newSyncOperation);
        } else if (syncResult.hasSoftError()) {
            long delay = rescheduleWithDelay(previousSyncOperation);
            if (delay >= 0) {
                if (isLoggable) {
                    Log.d(TAG, "retrying sync operation in " + delay + " ms because "
                            + "it encountered a soft error: " + previousSyncOperation);
                }
            }
        } else {
            if (Config.LOGD) {
                Log.d(TAG, "not retrying sync operation because the error is a hard error: "
                        + previousSyncOperation);
            }
        }
    }

    /**
     * Value type that represents a sync operation.
     */
    static class SyncOperation implements Comparable {
        final String account;
        int syncSource;
        String authority;
        Bundle extras;
        final String key;
        long earliestRunTime;
        long delay;
        Long rowId = null;

        SyncOperation(String account, int source, String authority, Bundle extras, long delay) {
            this.account = account;
            this.syncSource = source;
            this.authority = authority;
            this.extras = new Bundle(extras);
            this.setDelay(delay);
            this.key = toKey();
        }

        SyncOperation(SyncOperation other) {
            this.account = other.account;
            this.syncSource = other.syncSource;
            this.authority = other.authority;
            this.extras = new Bundle(other.extras);
            this.delay = other.delay;
            this.earliestRunTime = other.earliestRunTime;
            this.key = toKey();
        }

        public void setDelay(long delay) {
            this.delay = delay;
            if (delay >= 0) {
                this.earliestRunTime = SystemClock.elapsedRealtime() + delay;
            } else {
                this.earliestRunTime = 0;
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("authority: ").append(authority);
            sb.append(" account: ").append(account);
            sb.append(" extras: ");
            extrasToStringBuilder(extras, sb);
            sb.append(" syncSource: ").append(syncSource);
            sb.append(" when: ").append(earliestRunTime);
            sb.append(" delay: ").append(delay);
            sb.append(" key: {").append(key).append("}");
            if (rowId != null) sb.append(" rowId: ").append(rowId);
            return sb.toString();
        }

        private String toKey() {
            StringBuilder sb = new StringBuilder();
            sb.append("authority: ").append(authority);
            sb.append(" account: ").append(account);
            sb.append(" extras: ");
            extrasToStringBuilder(extras, sb);
            return sb.toString();
        }

        private static void extrasToStringBuilder(Bundle bundle, StringBuilder sb) {
            sb.append("[");
            for (String key : bundle.keySet()) {
                sb.append(key).append("=").append(bundle.get(key)).append(" ");
            }
            sb.append("]");
        }

        public int compareTo(Object o) {
            SyncOperation other = (SyncOperation)o;
            if (earliestRunTime == other.earliestRunTime) {
                return 0;
            }
            return (earliestRunTime < other.earliestRunTime) ? -1 : 1;
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
     * @hide
     */
<<<<<<< HEAD
    class ActiveSyncContext extends ISyncContext.Stub
            implements ServiceConnection, IBinder.DeathRecipient {
        final SyncOperation mSyncOperation;
        final long mHistoryRowId;
        ISyncAdapter mSyncAdapter;
        final long mStartTime;
        long mTimeoutStartTime;
        boolean mBound;
        final PowerManager.WakeLock mSyncWakeLock;
        final int mSyncAdapterUid;
        SyncInfo mSyncInfo;
        boolean mIsLinkedToDeath = false;

        /**
         * Create an ActiveSyncContext for an impending sync and grab the wakelock for that
         * sync adapter. Since this grabs the wakelock you need to be sure to call
         * close() when you are done with this ActiveSyncContext, whether the sync succeeded
         * or not.
         * @param syncOperation the SyncOperation we are about to sync
         * @param historyRowId the row in which to record the history info for this sync
         * @param syncAdapterUid the UID of the application that contains the sync adapter
         * for this sync. This is used to attribute the wakelock hold to that application.
         */
        public ActiveSyncContext(SyncOperation syncOperation, long historyRowId,
                int syncAdapterUid) {
            super();
            mSyncAdapterUid = syncAdapterUid;
            mSyncOperation = syncOperation;
            mHistoryRowId = historyRowId;
            mSyncAdapter = null;
            mStartTime = SystemClock.elapsedRealtime();
            mTimeoutStartTime = mStartTime;
            mSyncWakeLock = mSyncHandler.getSyncWakeLock(
                    mSyncOperation.account, mSyncOperation.authority);
            mSyncWakeLock.setWorkSource(new WorkSource(syncAdapterUid));
            mSyncWakeLock.acquire();
        }

        public void sendHeartbeat() {
            // heartbeats are no longer used
        }

        public void onFinished(SyncResult result) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) Log.v(TAG, "onFinished: " + this);
=======
    class ActiveSyncContext extends ISyncContext.Stub {
        final SyncOperation mSyncOperation;
        final long mHistoryRowId;
        final IContentProvider mContentProvider;
        final ISyncAdapter mSyncAdapter;
        final long mStartTime;
        long mTimeoutStartTime;

        public ActiveSyncContext(SyncOperation syncOperation, IContentProvider contentProvider,
                ISyncAdapter syncAdapter, long historyRowId) {
            super();
            mSyncOperation = syncOperation;
            mHistoryRowId = historyRowId;
            mContentProvider = contentProvider;
            mSyncAdapter = syncAdapter;
            mStartTime = SystemClock.elapsedRealtime();
            mTimeoutStartTime = mStartTime;
        }

        public void sendHeartbeat() {
            // ignore this call if it corresponds to an old sync session
            if (mActiveSyncContext == this) {
                SyncManager.this.updateHeartbeatTime();
            }
        }

        public void onFinished(SyncResult result) {
>>>>>>> 54b6cfa... Initial Contribution
            // include "this" in the message so that the handler can ignore it if this
            // ActiveSyncContext is no longer the mActiveSyncContext at message handling
            // time
            sendSyncFinishedOrCanceledMessage(this, result);
        }

        public void toString(StringBuilder sb) {
            sb.append("startTime ").append(mStartTime)
                    .append(", mTimeoutStartTime ").append(mTimeoutStartTime)
                    .append(", mHistoryRowId ").append(mHistoryRowId)
                    .append(", syncOperation ").append(mSyncOperation);
        }

<<<<<<< HEAD
        public void onServiceConnected(ComponentName name, IBinder service) {
            Message msg = mSyncHandler.obtainMessage();
            msg.what = SyncHandler.MESSAGE_SERVICE_CONNECTED;
            msg.obj = new ServiceConnectionData(this, ISyncAdapter.Stub.asInterface(service));
            mSyncHandler.sendMessage(msg);
        }

        public void onServiceDisconnected(ComponentName name) {
            Message msg = mSyncHandler.obtainMessage();
            msg.what = SyncHandler.MESSAGE_SERVICE_DISCONNECTED;
            msg.obj = new ServiceConnectionData(this, null);
            mSyncHandler.sendMessage(msg);
        }

        boolean bindToSyncAdapter(RegisteredServicesCache.ServiceInfo info) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.d(TAG, "bindToSyncAdapter: " + info.componentName + ", connection " + this);
            }
            Intent intent = new Intent();
            intent.setAction("android.content.SyncAdapter");
            intent.setComponent(info.componentName);
            intent.putExtra(Intent.EXTRA_CLIENT_LABEL,
                    com.android.internal.R.string.sync_binding_label);
            intent.putExtra(Intent.EXTRA_CLIENT_INTENT, PendingIntent.getActivity(
                    mContext, 0, new Intent(Settings.ACTION_SYNC_SETTINGS), 0));
            mBound = true;
            final boolean bindResult = mContext.bindService(intent, this,
                    Context.BIND_AUTO_CREATE | Context.BIND_NOT_FOREGROUND
                    | Context.BIND_ALLOW_OOM_MANAGEMENT,
                    mSyncOperation.userId);
            if (!bindResult) {
                mBound = false;
            }
            return bindResult;
        }

        /**
         * Performs the required cleanup, which is the releasing of the wakelock and
         * unbinding from the sync adapter (if actually bound).
         */
        protected void close() {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.d(TAG, "unBindFromSyncAdapter: connection " + this);
            }
            if (mBound) {
                mBound = false;
                mContext.unbindService(this);
            }
            mSyncWakeLock.release();
            mSyncWakeLock.setWorkSource(null);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            toString(sb);
            return sb.toString();
        }

        @Override
        public void binderDied() {
            sendSyncFinishedOrCanceledMessage(this, null);
        }
    }

    protected void dump(FileDescriptor fd, PrintWriter pw) {
        dumpSyncState(pw);
        dumpSyncHistory(pw);

        pw.println();
        pw.println("SyncAdapters:");
        for (RegisteredServicesCache.ServiceInfo info : mSyncAdapters.getAllServices()) {
            pw.println("  " + info);
        }
    }

    static String formatTime(long time) {
        Time tobj = new Time();
        tobj.set(time);
        return tobj.format("%Y-%m-%d %H:%M:%S");
    }

    protected void dumpSyncState(PrintWriter pw) {
        pw.print("data connected: "); pw.println(mDataConnectionIsConnected);
        pw.print("auto sync: ");
        List<UserInfo> users = getAllUsers();
        if (users != null) {
            for (UserInfo user : users) {
                pw.print("u" + user.id + "="
                        + mSyncStorageEngine.getMasterSyncAutomatically(user.id));
            }
            pw.println();
        }
        pw.print("memory low: "); pw.println(mStorageIsLow);

        final AccountAndUser[] accounts = mAccounts;

        pw.print("accounts: ");
        if (accounts != INITIAL_ACCOUNTS_ARRAY) {
            pw.println(accounts.length);
        } else {
            pw.println("not known yet");
        }
        final long now = SystemClock.elapsedRealtime();
        pw.print("now: "); pw.print(now);
        pw.println(" (" + formatTime(System.currentTimeMillis()) + ")");
        pw.print("offset: "); pw.print(DateUtils.formatElapsedTime(mSyncRandomOffsetMillis/1000));
        pw.println(" (HH:MM:SS)");
        pw.print("uptime: "); pw.print(DateUtils.formatElapsedTime(now/1000));
                pw.println(" (HH:MM:SS)");
        pw.print("time spent syncing: ");
                pw.print(DateUtils.formatElapsedTime(
                        mSyncHandler.mSyncTimeTracker.timeSpentSyncing() / 1000));
                pw.print(" (HH:MM:SS), sync ");
                pw.print(mSyncHandler.mSyncTimeTracker.mLastWasSyncing ? "" : "not ");
                pw.println("in progress");
        if (mSyncHandler.mAlarmScheduleTime != null) {
            pw.print("next alarm time: "); pw.print(mSyncHandler.mAlarmScheduleTime);
                    pw.print(" (");
                    pw.print(DateUtils.formatElapsedTime((mSyncHandler.mAlarmScheduleTime-now)/1000));
                    pw.println(" (HH:MM:SS) from now)");
        } else {
            pw.println("no alarm is scheduled (there had better not be any pending syncs)");
        }

        pw.print("notification info: ");
        final StringBuilder sb = new StringBuilder();
        mSyncHandler.mSyncNotificationInfo.toString(sb);
        pw.println(sb.toString());

        pw.println();
        pw.println("Active Syncs: " + mActiveSyncContexts.size());
        for (SyncManager.ActiveSyncContext activeSyncContext : mActiveSyncContexts) {
            final long durationInSeconds = (now - activeSyncContext.mStartTime) / 1000;
            pw.print("  ");
            pw.print(DateUtils.formatElapsedTime(durationInSeconds));
            pw.print(" - ");
            pw.print(activeSyncContext.mSyncOperation.dump(false));
            pw.println();
        }

        synchronized (mSyncQueue) {
            sb.setLength(0);
            mSyncQueue.dump(sb);
        }
        pw.println();
        pw.print(sb.toString());

        // join the installed sync adapter with the accounts list and emit for everything
        pw.println();
        pw.println("Sync Status");
        for (AccountAndUser account : accounts) {
            pw.print("  Account "); pw.print(account.account.name);
                    pw.print(" u"); pw.print(account.userId);
                    pw.print(" "); pw.print(account.account.type);
                    pw.println(":");
            for (RegisteredServicesCache.ServiceInfo<SyncAdapterType> syncAdapterType :
                    mSyncAdapters.getAllServices()) {
                if (!syncAdapterType.type.accountType.equals(account.account.type)) {
                    continue;
                }

                SyncStorageEngine.AuthorityInfo settings =
                        mSyncStorageEngine.getOrCreateAuthority(
                                account.account, account.userId, syncAdapterType.type.authority);
                SyncStatusInfo status = mSyncStorageEngine.getOrCreateSyncStatus(settings);
                pw.print("    "); pw.print(settings.authority);
                pw.println(":");
                pw.print("      settings:");
                pw.print(" " + (settings.syncable > 0
                        ? "syncable"
                        : (settings.syncable == 0 ? "not syncable" : "not initialized")));
                pw.print(", " + (settings.enabled ? "enabled" : "disabled"));
                if (settings.delayUntil > now) {
                    pw.print(", delay for "
                            + ((settings.delayUntil - now) / 1000) + " sec");
                }
                if (settings.backoffTime > now) {
                    pw.print(", backoff for "
                            + ((settings.backoffTime - now) / 1000) + " sec");
                }
                if (settings.backoffDelay > 0) {
                    pw.print(", the backoff increment is " + settings.backoffDelay / 1000
                                + " sec");
                }
                pw.println();
                for (int periodicIndex = 0;
                        periodicIndex < settings.periodicSyncs.size();
                        periodicIndex++) {
                    Pair<Bundle, Long> info = settings.periodicSyncs.get(periodicIndex);
                    long lastPeriodicTime = status.getPeriodicSyncTime(periodicIndex);
                    long nextPeriodicTime = lastPeriodicTime + info.second * 1000;
                    pw.println("      periodic period=" + info.second
                            + ", extras=" + info.first
                            + ", next=" + formatTime(nextPeriodicTime));
                }
                pw.print("      count: local="); pw.print(status.numSourceLocal);
                pw.print(" poll="); pw.print(status.numSourcePoll);
                pw.print(" periodic="); pw.print(status.numSourcePeriodic);
                pw.print(" server="); pw.print(status.numSourceServer);
                pw.print(" user="); pw.print(status.numSourceUser);
                pw.print(" total="); pw.print(status.numSyncs);
                pw.println();
                pw.print("      total duration: ");
                pw.println(DateUtils.formatElapsedTime(status.totalElapsedTime/1000));
                if (status.lastSuccessTime != 0) {
                    pw.print("      SUCCESS: source=");
                    pw.print(SyncStorageEngine.SOURCES[status.lastSuccessSource]);
                    pw.print(" time=");
                    pw.println(formatTime(status.lastSuccessTime));
                }
                if (status.lastFailureTime != 0) {
                    pw.print("      FAILURE: source=");
                    pw.print(SyncStorageEngine.SOURCES[
                            status.lastFailureSource]);
                    pw.print(" initialTime=");
                    pw.print(formatTime(status.initialFailureTime));
                    pw.print(" lastTime=");
                    pw.println(formatTime(status.lastFailureTime));
                    int errCode = status.getLastFailureMesgAsInt(0);
                    pw.print("      message: "); pw.println(
                            getLastFailureMessage(errCode) + " (" + errCode + ")");
                }
            }
        }
    }

    private String getLastFailureMessage(int code) {
        switch (code) {
            case ContentResolver.SYNC_ERROR_SYNC_ALREADY_IN_PROGRESS:
                return "sync already in progress";

            case ContentResolver.SYNC_ERROR_AUTHENTICATION:
                return "authentication error";

            case ContentResolver.SYNC_ERROR_IO:
                return "I/O error";

            case ContentResolver.SYNC_ERROR_PARSE:
                return "parse error";

            case ContentResolver.SYNC_ERROR_CONFLICT:
                return "conflict error";

            case ContentResolver.SYNC_ERROR_TOO_MANY_DELETIONS:
                return "too many deletions error";

            case ContentResolver.SYNC_ERROR_TOO_MANY_RETRIES:
                return "too many retries error";

            case ContentResolver.SYNC_ERROR_INTERNAL:
                return "internal error";

            default:
                return "unknown";
        }
    }

    private void dumpTimeSec(PrintWriter pw, long time) {
        pw.print(time/1000); pw.print('.'); pw.print((time/100)%10);
        pw.print('s');
    }

    private void dumpDayStatistic(PrintWriter pw, SyncStorageEngine.DayStats ds) {
        pw.print("Success ("); pw.print(ds.successCount);
        if (ds.successCount > 0) {
            pw.print(" for "); dumpTimeSec(pw, ds.successTime);
            pw.print(" avg="); dumpTimeSec(pw, ds.successTime/ds.successCount);
        }
        pw.print(") Failure ("); pw.print(ds.failureCount);
        if (ds.failureCount > 0) {
            pw.print(" for "); dumpTimeSec(pw, ds.failureTime);
            pw.print(" avg="); dumpTimeSec(pw, ds.failureTime/ds.failureCount);
        }
        pw.println(")");
    }

    protected void dumpSyncHistory(PrintWriter pw) {
        dumpRecentHistory(pw);
        dumpDayStatistics(pw);
    }

    private void dumpRecentHistory(PrintWriter pw) {
        final ArrayList<SyncStorageEngine.SyncHistoryItem> items
                = mSyncStorageEngine.getSyncHistory();
        if (items != null && items.size() > 0) {
            final Map<String, AuthoritySyncStats> authorityMap = Maps.newHashMap();
            long totalElapsedTime = 0;
            long totalTimes = 0;
            final int N = items.size();

            int maxAuthority = 0;
            int maxAccount = 0;
            for (SyncStorageEngine.SyncHistoryItem item : items) {
                SyncStorageEngine.AuthorityInfo authority
                        = mSyncStorageEngine.getAuthority(item.authorityId);
                final String authorityName;
                final String accountKey;
                if (authority != null) {
                    authorityName = authority.authority;
                    accountKey = authority.account.name + "/" + authority.account.type;
                } else {
                    authorityName = "Unknown";
                    accountKey = "Unknown";
                }

                int length = authorityName.length();
                if (length > maxAuthority) {
                    maxAuthority = length;
                }
                length = accountKey.length();
                if (length > maxAccount) {
                    maxAccount = length;
                }

                final long elapsedTime = item.elapsedTime;
                totalElapsedTime += elapsedTime;
                totalTimes++;
                AuthoritySyncStats authoritySyncStats = authorityMap.get(authorityName);
                if (authoritySyncStats == null) {
                    authoritySyncStats = new AuthoritySyncStats(authorityName);
                    authorityMap.put(authorityName, authoritySyncStats);
                }
                authoritySyncStats.elapsedTime += elapsedTime;
                authoritySyncStats.times++;
                final Map<String, AccountSyncStats> accountMap = authoritySyncStats.accountMap;
                AccountSyncStats accountSyncStats = accountMap.get(accountKey);
                if (accountSyncStats == null) {
                    accountSyncStats = new AccountSyncStats(accountKey);
                    accountMap.put(accountKey, accountSyncStats);
                }
                accountSyncStats.elapsedTime += elapsedTime;
                accountSyncStats.times++;

            }

            if (totalElapsedTime > 0) {
                pw.println();
                pw.printf("Detailed Statistics (Recent history):  "
                        + "%d (# of times) %ds (sync time)\n",
                        totalTimes, totalElapsedTime / 1000);

                final List<AuthoritySyncStats> sortedAuthorities =
                        new ArrayList<AuthoritySyncStats>(authorityMap.values());
                Collections.sort(sortedAuthorities, new Comparator<AuthoritySyncStats>() {
                    @Override
                    public int compare(AuthoritySyncStats lhs, AuthoritySyncStats rhs) {
                        // reverse order
                        int compare = Integer.compare(rhs.times, lhs.times);
                        if (compare == 0) {
                            compare = Long.compare(rhs.elapsedTime, lhs.elapsedTime);
                        }
                        return compare;
                    }
                });

                final int maxLength = Math.max(maxAuthority, maxAccount + 3);
                final int padLength = 2 + 2 + maxLength + 2 + 10 + 11;
                final char chars[] = new char[padLength];
                Arrays.fill(chars, '-');
                final String separator = new String(chars);

                final String authorityFormat =
                        String.format("  %%-%ds: %%-9s  %%-11s\n", maxLength + 2);
                final String accountFormat =
                        String.format("    %%-%ds:   %%-9s  %%-11s\n", maxLength);

                pw.println(separator);
                for (AuthoritySyncStats authoritySyncStats : sortedAuthorities) {
                    String name = authoritySyncStats.name;
                    long elapsedTime;
                    int times;
                    String timeStr;
                    String timesStr;

                    elapsedTime = authoritySyncStats.elapsedTime;
                    times = authoritySyncStats.times;
                    timeStr = String.format("%ds/%d%%",
                            elapsedTime / 1000,
                            elapsedTime * 100 / totalElapsedTime);
                    timesStr = String.format("%d/%d%%",
                            times,
                            times * 100 / totalTimes);
                    pw.printf(authorityFormat, name, timesStr, timeStr);

                    final List<AccountSyncStats> sortedAccounts =
                            new ArrayList<AccountSyncStats>(
                                    authoritySyncStats.accountMap.values());
                    Collections.sort(sortedAccounts, new Comparator<AccountSyncStats>() {
                        @Override
                        public int compare(AccountSyncStats lhs, AccountSyncStats rhs) {
                            // reverse order
                            int compare = Integer.compare(rhs.times, lhs.times);
                            if (compare == 0) {
                                compare = Long.compare(rhs.elapsedTime, lhs.elapsedTime);
                            }
                            return compare;
                        }
                    });
                    for (AccountSyncStats stats: sortedAccounts) {
                        elapsedTime = stats.elapsedTime;
                        times = stats.times;
                        timeStr = String.format("%ds/%d%%",
                                elapsedTime / 1000,
                                elapsedTime * 100 / totalElapsedTime);
                        timesStr = String.format("%d/%d%%",
                                times,
                                times * 100 / totalTimes);
                        pw.printf(accountFormat, stats.name, timesStr, timeStr);
                    }
                    pw.println(separator);
                }
            }

            pw.println();
            pw.println("Recent Sync History");
            final String format = "  %-" + maxAccount + "s  %s\n";
            final Map<String, Long> lastTimeMap = Maps.newHashMap();

            for (int i = 0; i < N; i++) {
                SyncStorageEngine.SyncHistoryItem item = items.get(i);
                SyncStorageEngine.AuthorityInfo authority
                        = mSyncStorageEngine.getAuthority(item.authorityId);
                final String authorityName;
                final String accountKey;
                if (authority != null) {
                    authorityName = authority.authority;
                    accountKey = authority.account.name + "/" + authority.account.type;
                } else {
                    authorityName = "Unknown";
                    accountKey = "Unknown";
                }
                final long elapsedTime = item.elapsedTime;
                final Time time = new Time();
                final long eventTime = item.eventTime;
                time.set(eventTime);

                final String key = authorityName + "/" + accountKey;
                final Long lastEventTime = lastTimeMap.get(key);
                final String diffString;
                if (lastEventTime == null) {
                    diffString = "";
                } else {
                    final long diff = (lastEventTime - eventTime) / 1000;
                    if (diff < 60) {
                        diffString = String.valueOf(diff);
                    } else if (diff < 3600) {
                        diffString = String.format("%02d:%02d", diff / 60, diff % 60);
                    } else {
                        final long sec = diff % 3600;
                        diffString = String.format("%02d:%02d:%02d",
                                diff / 3600, sec / 60, sec % 60);
                    }
                }
                lastTimeMap.put(key, eventTime);

                pw.printf("  #%-3d: %s %8s  %5.1fs  %8s",
                        i + 1,
                        formatTime(eventTime),
                        SyncStorageEngine.SOURCES[item.source],
                        ((float) elapsedTime) / 1000,
                        diffString);
                pw.printf(format, accountKey, authorityName);

                if (item.event != SyncStorageEngine.EVENT_STOP
                        || item.upstreamActivity != 0
                        || item.downstreamActivity != 0) {
                    pw.printf("    event=%d upstreamActivity=%d downstreamActivity=%d\n",
                            item.event,
                            item.upstreamActivity,
                            item.downstreamActivity);
                }
                if (item.mesg != null
                        && !SyncStorageEngine.MESG_SUCCESS.equals(item.mesg)) {
                    pw.printf("    mesg=%s\n", item.mesg);
                }
            }
        }
    }

    private void dumpDayStatistics(PrintWriter pw) {
        SyncStorageEngine.DayStats dses[] = mSyncStorageEngine.getDayStatistics();
        if (dses != null && dses[0] != null) {
            pw.println();
            pw.println("Sync Statistics");
            pw.print("  Today:  "); dumpDayStatistic(pw, dses[0]);
            int today = dses[0].day;
            int i;
            SyncStorageEngine.DayStats ds;

            // Print each day in the current week.
            for (i=1; i<=6 && i < dses.length; i++) {
                ds = dses[i];
                if (ds == null) break;
                int delta = today-ds.day;
                if (delta > 6) break;

                pw.print("  Day-"); pw.print(delta); pw.print(":  ");
                dumpDayStatistic(pw, ds);
            }

            // Aggregate all following days into weeks and print totals.
            int weekDay = today;
            while (i < dses.length) {
                SyncStorageEngine.DayStats aggr = null;
                weekDay -= 7;
                while (i < dses.length) {
                    ds = dses[i];
                    if (ds == null) {
                        i = dses.length;
                        break;
                    }
                    int delta = weekDay-ds.day;
                    if (delta > 6) break;
                    i++;

                    if (aggr == null) {
                        aggr = new SyncStorageEngine.DayStats(weekDay);
                    }
                    aggr.successCount += ds.successCount;
                    aggr.successTime += ds.successTime;
                    aggr.failureCount += ds.failureCount;
                    aggr.failureTime += ds.failureTime;
                }
                if (aggr != null) {
                    pw.print("  Week-"); pw.print((today-weekDay)/7); pw.print(": ");
                    dumpDayStatistic(pw, aggr);
                }
            }
        }
    }

    private static class AuthoritySyncStats {
        String name;
        long elapsedTime;
        int times;
        Map<String, AccountSyncStats> accountMap = Maps.newHashMap();

        private AuthoritySyncStats(String name) {
            this.name = name;
        }
    }

    private static class AccountSyncStats {
        String name;
        long elapsedTime;
        int times;

        private AccountSyncStats(String name) {
            this.name = name;
        }
=======
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            toString(sb);
            return sb.toString();
        }
    }

    protected void dump(FileDescriptor fd, PrintWriter pw) {
        StringBuilder sb = new StringBuilder();
        dumpSyncState(sb);
        sb.append("\n");
        if (isSyncEnabled()) {
            dumpSyncHistory(sb);
        }
        pw.println(sb.toString());
    }

    protected void dumpSyncState(StringBuilder sb) {
        sb.append("sync enabled: ").append(isSyncEnabled()).append("\n");
        sb.append("data connected: ").append(mDataConnectionIsConnected).append("\n");
        sb.append("memory low: ").append(mStorageIsLow).append("\n");

        final String[] accounts = mAccounts;
        sb.append("accounts: ");
        if (accounts != null) {
            sb.append(accounts.length);
        } else {
            sb.append("none");
        }
        sb.append("\n");
        final long now = SystemClock.elapsedRealtime();
        sb.append("now: ").append(now).append("\n");
        sb.append("uptime: ").append(DateUtils.formatElapsedTime(now/1000)).append(" (HH:MM:SS)\n");
        sb.append("time spent syncing : ")
                .append(DateUtils.formatElapsedTime(
                        mSyncHandler.mSyncTimeTracker.timeSpentSyncing() / 1000))
                .append(" (HH:MM:SS), sync ")
                .append(mSyncHandler.mSyncTimeTracker.mLastWasSyncing ? "" : "not ")
                .append("in progress").append("\n");
        if (mSyncHandler.mAlarmScheduleTime != null) {
            sb.append("next alarm time: ").append(mSyncHandler.mAlarmScheduleTime)
                    .append(" (")
                    .append(DateUtils.formatElapsedTime((mSyncHandler.mAlarmScheduleTime-now)/1000))
                    .append(" (HH:MM:SS) from now)\n");
        } else {
            sb.append("no alarm is scheduled (there had better not be any pending syncs)\n");
        }

        sb.append("active sync: ").append(mActiveSyncContext).append("\n");

        sb.append("notification info: ");
        mSyncHandler.mSyncNotificationInfo.toString(sb);
        sb.append("\n");

        synchronized (mSyncQueue) {
            sb.append("sync queue: ");
            mSyncQueue.dump(sb);
        }

        Cursor c = mSyncStorageEngine.query(Sync.Active.CONTENT_URI,
                SYNC_ACTIVE_PROJECTION, null, null, null);
        sb.append("\n");
        try {
            if (c.moveToNext()) {
                final long durationInSeconds = (now - c.getLong(2)) / 1000;
                sb.append("Active sync: ").append(c.getString(0))
                        .append(" ").append(c.getString(1))
                        .append(", duration is ")
                        .append(DateUtils.formatElapsedTime(durationInSeconds)).append(".\n");
            } else {
                sb.append("No sync is in progress.\n");
            }
        } finally {
            c.close();
        }

        c = mSyncStorageEngine.query(Sync.Pending.CONTENT_URI,
                SYNC_PENDING_PROJECTION, null, null, "account, authority");
        sb.append("\nPending Syncs\n");
        try {
            if (c.getCount() != 0) {
                dumpSyncPendingHeader(sb);
                while (c.moveToNext()) {
                    dumpSyncPendingRow(sb, c);
                }
                dumpSyncPendingFooter(sb);
            } else {
                sb.append("none\n");
            }
        } finally {
            c.close();
        }

        String currentAccount = null;
        c = mSyncStorageEngine.query(Sync.Status.CONTENT_URI,
                STATUS_PROJECTION, null, null, "account, authority");
        sb.append("\nSync history by account and authority\n");
        try {
            while (c.moveToNext()) {
                if (!TextUtils.equals(currentAccount, c.getString(0))) {
                    if (currentAccount != null) {
                        dumpSyncHistoryFooter(sb);
                    }
                    currentAccount = c.getString(0);
                    dumpSyncHistoryHeader(sb, currentAccount);
                }

                dumpSyncHistoryRow(sb, c);
            }
            if (c.getCount() > 0) dumpSyncHistoryFooter(sb);
        } finally {
            c.close();
        }
    }

    private void dumpSyncHistoryHeader(StringBuilder sb, String account) {
        sb.append(" Account: ").append(account).append("\n");
        sb.append("  ___________________________________________________________________________________________________________________________\n");
        sb.append(" |                 |             num times synced           |   total  |         last success          |                     |\n");
        sb.append(" | authority       | local |  poll | server |  user | total | duration |  source |               time  |   result if failing |\n");
    }

    private static String[] STATUS_PROJECTION = new String[]{
            Sync.Status.ACCOUNT, // 0
            Sync.Status.AUTHORITY, // 1
            Sync.Status.NUM_SYNCS, // 2
            Sync.Status.TOTAL_ELAPSED_TIME, // 3
            Sync.Status.NUM_SOURCE_LOCAL, // 4
            Sync.Status.NUM_SOURCE_POLL, // 5
            Sync.Status.NUM_SOURCE_SERVER, // 6
            Sync.Status.NUM_SOURCE_USER, // 7
            Sync.Status.LAST_SUCCESS_SOURCE, // 8
            Sync.Status.LAST_SUCCESS_TIME, // 9
            Sync.Status.LAST_FAILURE_SOURCE, // 10
            Sync.Status.LAST_FAILURE_TIME, // 11
            Sync.Status.LAST_FAILURE_MESG // 12
    };

    private void dumpSyncHistoryRow(StringBuilder sb, Cursor c) {
        boolean hasSuccess = !c.isNull(9);
        boolean hasFailure = !c.isNull(11);
        Time timeSuccess = new Time();
        if (hasSuccess) timeSuccess.set(c.getLong(9));
        Time timeFailure = new Time();
        if (hasFailure) timeFailure.set(c.getLong(11));
        sb.append(String.format(" | %-15s | %5d | %5d | %6d | %5d | %5d | %8s | %7s | %19s | %19s |\n",
                c.getString(1),
                c.getLong(4),
                c.getLong(5),
                c.getLong(6),
                c.getLong(7),
                c.getLong(2),
                DateUtils.formatElapsedTime(c.getLong(3)/1000),
                hasSuccess ? Sync.History.SOURCES[c.getInt(8)] : "",
                hasSuccess ? timeSuccess.format("%Y-%m-%d %H:%M:%S") : "",
                hasFailure ? History.mesgToString(c.getString(12)) : ""));
    }

    private void dumpSyncHistoryFooter(StringBuilder sb) {
        sb.append(" |___________________________________________________________________________________________________________________________|\n");
    }

    private void dumpSyncPendingHeader(StringBuilder sb) {
        sb.append(" ____________________________________________________\n");
        sb.append(" | account                        | authority       |\n");
    }

    private void dumpSyncPendingRow(StringBuilder sb, Cursor c) {
        sb.append(String.format(" | %-30s | %-15s |\n", c.getString(0), c.getString(1)));
    }

    private void dumpSyncPendingFooter(StringBuilder sb) {
        sb.append(" |__________________________________________________|\n");
    }

    protected void dumpSyncHistory(StringBuilder sb) {
        Cursor c = mSyncStorageEngine.query(Sync.History.CONTENT_URI, null, "event=?",
                new String[]{String.valueOf(Sync.History.EVENT_STOP)},
                Sync.HistoryColumns.EVENT_TIME + " desc");
        try {
            long numSyncsLastHour = 0, durationLastHour = 0;
            long numSyncsLastDay = 0, durationLastDay = 0;
            long numSyncsLastWeek = 0, durationLastWeek = 0;
            long numSyncsLast4Weeks = 0, durationLast4Weeks = 0;
            long numSyncsTotal = 0, durationTotal = 0;

            long now = System.currentTimeMillis();
            int indexEventTime = c.getColumnIndexOrThrow(Sync.History.EVENT_TIME);
            int indexElapsedTime = c.getColumnIndexOrThrow(Sync.History.ELAPSED_TIME);
            while (c.moveToNext()) {
                long duration = c.getLong(indexElapsedTime);
                long endTime = c.getLong(indexEventTime) + duration;
                long millisSinceStart = now - endTime;
                numSyncsTotal++;
                durationTotal += duration;
                if (millisSinceStart < MILLIS_IN_HOUR) {
                    numSyncsLastHour++;
                    durationLastHour += duration;
                }
                if (millisSinceStart < MILLIS_IN_DAY) {
                    numSyncsLastDay++;
                    durationLastDay += duration;
                }
                if (millisSinceStart < MILLIS_IN_WEEK) {
                    numSyncsLastWeek++;
                    durationLastWeek += duration;
                }
                if (millisSinceStart < MILLIS_IN_4WEEKS) {
                    numSyncsLast4Weeks++;
                    durationLast4Weeks += duration;
                }
            }
            dumpSyncIntervalHeader(sb);
            dumpSyncInterval(sb, "hour", MILLIS_IN_HOUR, numSyncsLastHour, durationLastHour);
            dumpSyncInterval(sb, "day", MILLIS_IN_DAY, numSyncsLastDay, durationLastDay);
            dumpSyncInterval(sb, "week", MILLIS_IN_WEEK, numSyncsLastWeek, durationLastWeek);
            dumpSyncInterval(sb, "4 weeks",
                    MILLIS_IN_4WEEKS, numSyncsLast4Weeks, durationLast4Weeks);
            dumpSyncInterval(sb, "total", 0, numSyncsTotal, durationTotal);
            dumpSyncIntervalFooter(sb);
        } finally {
            c.close();
        }
    }

    private void dumpSyncIntervalHeader(StringBuilder sb) {
        sb.append("Sync Stats\n");
        sb.append(" ___________________________________________________________\n");
        sb.append(" |          |        |   duration in sec   |               |\n");
        sb.append(" | interval |  count |  average |    total | % of interval |\n");
    }

    private void dumpSyncInterval(StringBuilder sb, String label,
            long interval, long numSyncs, long duration) {
        sb.append(String.format(" | %-8s | %6d | %8.1f | %8.1f",
                label, numSyncs, ((float)duration/numSyncs)/1000, (float)duration/1000));
        if (interval > 0) {
            sb.append(String.format(" | %13.2f |\n", ((float)duration/interval)*100.0));
        } else {
            sb.append(String.format(" | %13s |\n", "na"));
        }
    }

    private void dumpSyncIntervalFooter(StringBuilder sb) {
        sb.append(" |_________________________________________________________|\n");
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * A helper object to keep track of the time we have spent syncing since the last boot
     */
    private class SyncTimeTracker {
        /** True if a sync was in progress on the most recent call to update() */
        boolean mLastWasSyncing = false;
        /** Used to track when lastWasSyncing was last set */
        long mWhenSyncStarted = 0;
        /** The cumulative time we have spent syncing */
        private long mTimeSpentSyncing;

        /** Call to let the tracker know that the sync state may have changed */
        public synchronized void update() {
<<<<<<< HEAD
            final boolean isSyncInProgress = !mActiveSyncContexts.isEmpty();
=======
            final boolean isSyncInProgress = mActiveSyncContext != null;
>>>>>>> 54b6cfa... Initial Contribution
            if (isSyncInProgress == mLastWasSyncing) return;
            final long now = SystemClock.elapsedRealtime();
            if (isSyncInProgress) {
                mWhenSyncStarted = now;
            } else {
                mTimeSpentSyncing += now - mWhenSyncStarted;
            }
            mLastWasSyncing = isSyncInProgress;
        }

        /** Get how long we have been syncing, in ms */
        public synchronized long timeSpentSyncing() {
            if (!mLastWasSyncing) return mTimeSpentSyncing;

            final long now = SystemClock.elapsedRealtime();
            return mTimeSpentSyncing + (now - mWhenSyncStarted);
        }
    }

<<<<<<< HEAD
    class ServiceConnectionData {
        public final ActiveSyncContext activeSyncContext;
        public final ISyncAdapter syncAdapter;
        ServiceConnectionData(ActiveSyncContext activeSyncContext, ISyncAdapter syncAdapter) {
            this.activeSyncContext = activeSyncContext;
            this.syncAdapter = syncAdapter;
        }
    }

=======
>>>>>>> 54b6cfa... Initial Contribution
    /**
     * Handles SyncOperation Messages that are posted to the associated
     * HandlerThread.
     */
    class SyncHandler extends Handler {
        // Messages that can be sent on mHandler
        private static final int MESSAGE_SYNC_FINISHED = 1;
        private static final int MESSAGE_SYNC_ALARM = 2;
        private static final int MESSAGE_CHECK_ALARMS = 3;
<<<<<<< HEAD
        private static final int MESSAGE_SERVICE_CONNECTED = 4;
        private static final int MESSAGE_SERVICE_DISCONNECTED = 5;
        private static final int MESSAGE_CANCEL = 6;
=======
>>>>>>> 54b6cfa... Initial Contribution

        public final SyncNotificationInfo mSyncNotificationInfo = new SyncNotificationInfo();
        private Long mAlarmScheduleTime = null;
        public final SyncTimeTracker mSyncTimeTracker = new SyncTimeTracker();
<<<<<<< HEAD
        private final HashMap<Pair<Account, String>, PowerManager.WakeLock> mWakeLocks =
                Maps.newHashMap();

        private volatile CountDownLatch mReadyToRunLatch = new CountDownLatch(1);
        public void onBootCompleted() {
            mBootCompleted = true;
            // TODO: Handle bootcompleted event for specific user. Now let's just iterate through
            // all the users.
            List<UserInfo> users = getAllUsers();
            if (users != null) {
                for (UserInfo user : users) {
                    mSyncStorageEngine.doDatabaseCleanup(
                            AccountManagerService.getSingleton().getAccounts(user.id),
                            user.id);
                }
            }
            if (mReadyToRunLatch != null) {
                mReadyToRunLatch.countDown();
            }
        }

        private PowerManager.WakeLock getSyncWakeLock(Account account, String authority) {
            final Pair<Account, String> wakeLockKey = Pair.create(account, authority);
            PowerManager.WakeLock wakeLock = mWakeLocks.get(wakeLockKey);
            if (wakeLock == null) {
                final String name = SYNC_WAKE_LOCK_PREFIX + "_" + authority + "_" + account;
                wakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, name);
                wakeLock.setReferenceCounted(false);
                mWakeLocks.put(wakeLockKey, wakeLock);
            }
            return wakeLock;
        }

        private void waitUntilReadyToRun() {
            CountDownLatch latch = mReadyToRunLatch;
            if (latch != null) {
                while (true) {
                    try {
                        latch.await();
                        mReadyToRunLatch = null;
                        return;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
=======

        // used to track if we have installed the error notification so that we don't reinstall
        // it if sync is still failing
        private boolean mErrorNotificationInstalled = false;

>>>>>>> 54b6cfa... Initial Contribution
        /**
         * Used to keep track of whether a sync notification is active and who it is for.
         */
        class SyncNotificationInfo {
<<<<<<< HEAD
=======
            // only valid if isActive is true
            public String account;

            // only valid if isActive is true
            public String authority;

>>>>>>> 54b6cfa... Initial Contribution
            // true iff the notification manager has been asked to send the notification
            public boolean isActive = false;

            // Set when we transition from not running a sync to running a sync, and cleared on
            // the opposite transition.
            public Long startTime = null;

            public void toString(StringBuilder sb) {
<<<<<<< HEAD
                sb.append("isActive ").append(isActive).append(", startTime ").append(startTime);
=======
                sb.append("account ").append(account)
                        .append(", authority ").append(authority)
                        .append(", isActive ").append(isActive)
                        .append(", startTime ").append(startTime);
>>>>>>> 54b6cfa... Initial Contribution
            }

            @Override
            public String toString() {
                StringBuilder sb = new StringBuilder();
                toString(sb);
                return sb.toString();
            }
        }

        public SyncHandler(Looper looper) {
            super(looper);
        }
<<<<<<< HEAD

        public void handleMessage(Message msg) {
            long earliestFuturePollTime = Long.MAX_VALUE;
            long nextPendingSyncTime = Long.MAX_VALUE;

            // Setting the value here instead of a method because we want the dumpsys logs
            // to have the most recent value used.
            try {
                waitUntilReadyToRun();
                mDataConnectionIsConnected = readDataConnectionState();
                mSyncManagerWakeLock.acquire();
                // Always do this first so that we be sure that any periodic syncs that
                // are ready to run have been converted into pending syncs. This allows the
                // logic that considers the next steps to take based on the set of pending syncs
                // to also take into account the periodic syncs.
                earliestFuturePollTime = scheduleReadyPeriodicSyncs();
                switch (msg.what) {
                    case SyncHandler.MESSAGE_CANCEL: {
                        Pair<Account, String> payload = (Pair<Account, String>)msg.obj;
                        if (Log.isLoggable(TAG, Log.VERBOSE)) {
                            Log.d(TAG, "handleSyncHandlerMessage: MESSAGE_SERVICE_CANCEL: "
                                    + payload.first + ", " + payload.second);
                        }
                        cancelActiveSyncLocked(payload.first, msg.arg1, payload.second);
                        nextPendingSyncTime = maybeStartNextSyncLocked();
                        break;
                    }

=======
        
        public void handleMessage(Message msg) {
            handleSyncHandlerMessage(msg);
        }

        private void handleSyncHandlerMessage(Message msg) {
            try {
                switch (msg.what) {
>>>>>>> 54b6cfa... Initial Contribution
                    case SyncHandler.MESSAGE_SYNC_FINISHED:
                        if (Log.isLoggable(TAG, Log.VERBOSE)) {
                            Log.v(TAG, "handleSyncHandlerMessage: MESSAGE_SYNC_FINISHED");
                        }
                        SyncHandlerMessagePayload payload = (SyncHandlerMessagePayload)msg.obj;
<<<<<<< HEAD
                        if (!isSyncStillActive(payload.activeSyncContext)) {
                            Log.d(TAG, "handleSyncHandlerMessage: dropping since the "
                                    + "sync is no longer active: "
                                    + payload.activeSyncContext);
                            break;
                        }
                        runSyncFinishedOrCanceledLocked(payload.syncResult, payload.activeSyncContext);

                        // since a sync just finished check if it is time to start a new sync
                        nextPendingSyncTime = maybeStartNextSyncLocked();
                        break;

                    case SyncHandler.MESSAGE_SERVICE_CONNECTED: {
                        ServiceConnectionData msgData = (ServiceConnectionData)msg.obj;
                        if (Log.isLoggable(TAG, Log.VERBOSE)) {
                            Log.d(TAG, "handleSyncHandlerMessage: MESSAGE_SERVICE_CONNECTED: "
                                    + msgData.activeSyncContext);
                        }
                        // check that this isn't an old message
                        if (isSyncStillActive(msgData.activeSyncContext)) {
                            runBoundToSyncAdapter(msgData.activeSyncContext, msgData.syncAdapter);
                        }
                        break;
                    }

                    case SyncHandler.MESSAGE_SERVICE_DISCONNECTED: {
                        final ActiveSyncContext currentSyncContext =
                                ((ServiceConnectionData)msg.obj).activeSyncContext;
                        if (Log.isLoggable(TAG, Log.VERBOSE)) {
                            Log.d(TAG, "handleSyncHandlerMessage: MESSAGE_SERVICE_DISCONNECTED: "
                                    + currentSyncContext);
                        }
                        // check that this isn't an old message
                        if (isSyncStillActive(currentSyncContext)) {
                            // cancel the sync if we have a syncadapter, which means one is
                            // outstanding
                            if (currentSyncContext.mSyncAdapter != null) {
                                try {
                                    currentSyncContext.mSyncAdapter.cancelSync(currentSyncContext);
                                } catch (RemoteException e) {
                                    // we don't need to retry this in this case
                                }
                            }

                            // pretend that the sync failed with an IOException,
                            // which is a soft error
                            SyncResult syncResult = new SyncResult();
                            syncResult.stats.numIoExceptions++;
                            runSyncFinishedOrCanceledLocked(syncResult, currentSyncContext);

                            // since a sync just finished check if it is time to start a new sync
                            nextPendingSyncTime = maybeStartNextSyncLocked();
                        }

                        break;
                    }
=======
                        if (mActiveSyncContext != payload.activeSyncContext) {
                            if (Config.LOGD) {
                                Log.d(TAG, "handleSyncHandlerMessage: sync context doesn't match, "
                                        + "dropping: mActiveSyncContext " + mActiveSyncContext
                                        + " != " + payload.activeSyncContext);
                            }
                            return;
                        }
                        runSyncFinishedOrCanceled(payload.syncResult);

                        // since we are no longer syncing, check if it is time to start a new sync
                        runStateIdle();
                        break;
>>>>>>> 54b6cfa... Initial Contribution

                    case SyncHandler.MESSAGE_SYNC_ALARM: {
                        boolean isLoggable = Log.isLoggable(TAG, Log.VERBOSE);
                        if (isLoggable) {
                            Log.v(TAG, "handleSyncHandlerMessage: MESSAGE_SYNC_ALARM");
                        }
                        mAlarmScheduleTime = null;
                        try {
<<<<<<< HEAD
                            nextPendingSyncTime = maybeStartNextSyncLocked();
=======
                            if (mActiveSyncContext != null) {
                                if (isLoggable) {
                                    Log.v(TAG, "handleSyncHandlerMessage: sync context is active");
                                }
                                runStateSyncing();
                            }

                            // if the above call to runStateSyncing() resulted in the end of a sync,
                            // check if it is time to start a new sync
                            if (mActiveSyncContext == null) {
                                if (isLoggable) {
                                    Log.v(TAG, "handleSyncHandlerMessage: "
                                            + "sync context is not active");
                                }
                                runStateIdle();
                            }
>>>>>>> 54b6cfa... Initial Contribution
                        } finally {
                            mHandleAlarmWakeLock.release();
                        }
                        break;
                    }

                    case SyncHandler.MESSAGE_CHECK_ALARMS:
                        if (Log.isLoggable(TAG, Log.VERBOSE)) {
                            Log.v(TAG, "handleSyncHandlerMessage: MESSAGE_CHECK_ALARMS");
                        }
<<<<<<< HEAD
                        nextPendingSyncTime = maybeStartNextSyncLocked();
                        break;
                }
            } finally {
                manageSyncNotificationLocked();
                manageSyncAlarmLocked(earliestFuturePollTime, nextPendingSyncTime);
                mSyncTimeTracker.update();
                mSyncManagerWakeLock.release();
            }
        }

        /**
         * Turn any periodic sync operations that are ready to run into pending sync operations.
         * @return the desired start time of the earliest future  periodic sync operation,
         * in milliseconds since boot
         */
        private long scheduleReadyPeriodicSyncs() {
            final boolean backgroundDataUsageAllowed =
                    getConnectivityManager().getBackgroundDataSetting();
            long earliestFuturePollTime = Long.MAX_VALUE;
            if (!backgroundDataUsageAllowed) {
                return earliestFuturePollTime;
            }

            AccountAndUser[] accounts = mAccounts;

            final long nowAbsolute = System.currentTimeMillis();
            final long shiftedNowAbsolute = (0 < nowAbsolute - mSyncRandomOffsetMillis)
                                               ? (nowAbsolute  - mSyncRandomOffsetMillis) : 0;

            ArrayList<SyncStorageEngine.AuthorityInfo> infos = mSyncStorageEngine.getAuthorities();
            for (SyncStorageEngine.AuthorityInfo info : infos) {
                // skip the sync if the account of this operation no longer exists
                if (!containsAccountAndUser(accounts, info.account, info.userId)) {
                    continue;
                }

                if (!mSyncStorageEngine.getMasterSyncAutomatically(info.userId)
                        || !mSyncStorageEngine.getSyncAutomatically(info.account, info.userId,
                                info.authority)) {
                    continue;
                }

                if (mSyncStorageEngine.getIsSyncable(info.account, info.userId, info.authority)
                        == 0) {
                    continue;
                }

                SyncStatusInfo status = mSyncStorageEngine.getOrCreateSyncStatus(info);
                for (int i = 0, N = info.periodicSyncs.size(); i < N; i++) {
                    final Bundle extras = info.periodicSyncs.get(i).first;
                    final Long periodInMillis = info.periodicSyncs.get(i).second * 1000;
                    // find when this periodic sync was last scheduled to run
                    final long lastPollTimeAbsolute = status.getPeriodicSyncTime(i);

                    long remainingMillis
                            = periodInMillis - (shiftedNowAbsolute % periodInMillis);

                    /*
                     * Sync scheduling strategy:
                     *    Set the next periodic sync based on a random offset (in seconds).
                     *
                     *    Also sync right now if any of the following cases hold
                     *    and mark it as having been scheduled
                     *
                     * Case 1:  This sync is ready to run now.
                     * Case 2:  If the lastPollTimeAbsolute is in the future,
                     *          sync now and reinitialize. This can happen for
                     *          example if the user changed the time, synced and
                     *          changed back.
                     * Case 3:  If we failed to sync at the last scheduled time
                     */
                    if (remainingMillis == periodInMillis  // Case 1
                            || lastPollTimeAbsolute > nowAbsolute // Case 2
                            || (nowAbsolute - lastPollTimeAbsolute
                                    >= periodInMillis)) { // Case 3
                        // Sync now
                        final Pair<Long, Long> backoff = mSyncStorageEngine.getBackoff(
                                info.account, info.userId, info.authority);
                        final RegisteredServicesCache.ServiceInfo<SyncAdapterType> syncAdapterInfo =
                                mSyncAdapters.getServiceInfo(
                                        SyncAdapterType.newKey(info.authority, info.account.type));
                        if (syncAdapterInfo == null) {
                            continue;
                        }
                        scheduleSyncOperation(
                                new SyncOperation(info.account, info.userId,
                                        SyncStorageEngine.SOURCE_PERIODIC,
                                        info.authority, extras, 0 /* delay */,
                                        backoff != null ? backoff.first : 0,
                                        mSyncStorageEngine.getDelayUntilTime(
                                                info.account, info.userId, info.authority),
                                        syncAdapterInfo.type.allowParallelSyncs()));
                        status.setPeriodicSyncTime(i, nowAbsolute);
                    }
                    // Compute when this periodic sync should next run
                    final long nextPollTimeAbsolute = nowAbsolute + remainingMillis;

                    // remember this time if it is earlier than earliestFuturePollTime
                    if (nextPollTimeAbsolute < earliestFuturePollTime) {
                        earliestFuturePollTime = nextPollTimeAbsolute;
                    }
                }
            }

            if (earliestFuturePollTime == Long.MAX_VALUE) {
                return Long.MAX_VALUE;
            }

            // convert absolute time to elapsed time
            return SystemClock.elapsedRealtime()
                    + ((earliestFuturePollTime < nowAbsolute)
                      ? 0
                      : (earliestFuturePollTime - nowAbsolute));
        }

        private long maybeStartNextSyncLocked() {
            final boolean isLoggable = Log.isLoggable(TAG, Log.VERBOSE);
            if (isLoggable) Log.v(TAG, "maybeStartNextSync");
=======
                        // we do all the work for this case in the finally block
                        break;
                }
            } finally {
                final boolean isSyncInProgress = mActiveSyncContext != null;
                if (!isSyncInProgress) {
                    mSyncWakeLock.release();
                }
                manageSyncNotification();
                manageErrorNotification();
                manageSyncAlarm();
                mSyncTimeTracker.update();
            }
        }

        private void runStateSyncing() {
            // if the sync timeout has been reached then cancel it

            ActiveSyncContext activeSyncContext = mActiveSyncContext;

            final long now = SystemClock.elapsedRealtime();
            if (now > activeSyncContext.mTimeoutStartTime + MAX_TIME_PER_SYNC) {
                SyncOperation nextSyncOperation;
                synchronized (mSyncQueue) {
                    nextSyncOperation = mSyncQueue.head();
                }
                if (nextSyncOperation != null && nextSyncOperation.earliestRunTime <= now) {
                    if (Config.LOGD) {
                        Log.d(TAG, "canceling and rescheduling sync because it ran too long: "
                                + activeSyncContext.mSyncOperation);
                    }
                    rescheduleImmediately(activeSyncContext.mSyncOperation);
                    sendSyncFinishedOrCanceledMessage(activeSyncContext,
                            null /* no result since this is a cancel */);
                } else {
                    activeSyncContext.mTimeoutStartTime = now + MAX_TIME_PER_SYNC;
                }
            }

            // no need to schedule an alarm, as that will be done by our caller.
        }

        private void runStateIdle() {
            boolean isLoggable = Log.isLoggable(TAG, Log.VERBOSE);
            if (isLoggable) Log.v(TAG, "runStateIdle");
>>>>>>> 54b6cfa... Initial Contribution

            // If we aren't ready to run (e.g. the data connection is down), get out.
            if (!mDataConnectionIsConnected) {
                if (isLoggable) {
<<<<<<< HEAD
                    Log.v(TAG, "maybeStartNextSync: no data connection, skipping");
                }
                return Long.MAX_VALUE;
=======
                    Log.v(TAG, "runStateIdle: no data connection, skipping");
                }
                setStatusText("No data connection");
                return;
>>>>>>> 54b6cfa... Initial Contribution
            }

            if (mStorageIsLow) {
                if (isLoggable) {
<<<<<<< HEAD
                    Log.v(TAG, "maybeStartNextSync: memory low, skipping");
                }
                return Long.MAX_VALUE;
=======
                    Log.v(TAG, "runStateIdle: memory low, skipping");
                }
                setStatusText("Memory low");
                return;
>>>>>>> 54b6cfa... Initial Contribution
            }

            // If the accounts aren't known yet then we aren't ready to run. We will be kicked
            // when the account lookup request does complete.
<<<<<<< HEAD
            AccountAndUser[] accounts = mAccounts;
            if (accounts == INITIAL_ACCOUNTS_ARRAY) {
                if (isLoggable) {
                    Log.v(TAG, "maybeStartNextSync: accounts not known, skipping");
                }
                return Long.MAX_VALUE;
=======
            String[] accounts = mAccounts;
            if (accounts == null) {
                if (isLoggable) {
                    Log.v(TAG, "runStateIdle: accounts not known, skipping");
                }
                setStatusText("Accounts not known yet");
                return;
>>>>>>> 54b6cfa... Initial Contribution
            }

            // Otherwise consume SyncOperations from the head of the SyncQueue until one is
            // found that is runnable (not disabled, etc). If that one is ready to run then
            // start it, otherwise just get out.
<<<<<<< HEAD
            final boolean backgroundDataUsageAllowed =
                    getConnectivityManager().getBackgroundDataSetting();

            final long now = SystemClock.elapsedRealtime();

            // will be set to the next time that a sync should be considered for running
            long nextReadyToRunTime = Long.MAX_VALUE;

            // order the sync queue, dropping syncs that are not allowed
            ArrayList<SyncOperation> operations = new ArrayList<SyncOperation>();
            synchronized (mSyncQueue) {
                if (isLoggable) {
                    Log.v(TAG, "build the operation array, syncQueue size is "
                        + mSyncQueue.mOperationsMap.size());
                }
                Iterator<SyncOperation> operationIterator =
                        mSyncQueue.mOperationsMap.values().iterator();
                while (operationIterator.hasNext()) {
                    final SyncOperation op = operationIterator.next();

                    // drop the sync if the account of this operation no longer exists
                    if (!containsAccountAndUser(accounts, op.account, op.userId)) {
                        operationIterator.remove();
                        mSyncStorageEngine.deleteFromPending(op.pendingOperation);
                        continue;
                    }

                    // drop this sync request if it isn't syncable
                    int syncableState = mSyncStorageEngine.getIsSyncable(
                            op.account, op.userId, op.authority);
                    if (syncableState == 0) {
                        operationIterator.remove();
                        mSyncStorageEngine.deleteFromPending(op.pendingOperation);
                        continue;
                    }

                    // if the next run time is in the future, meaning there are no syncs ready
                    // to run, return the time
                    if (op.effectiveRunTime > now) {
                        if (nextReadyToRunTime > op.effectiveRunTime) {
                            nextReadyToRunTime = op.effectiveRunTime;
                        }
                        continue;
                    }

                    final RegisteredServicesCache.ServiceInfo<SyncAdapterType> syncAdapterInfo;
                    syncAdapterInfo = mSyncAdapters.getServiceInfo(
                            SyncAdapterType.newKey(op.authority, op.account.type));

                    // only proceed if network is connected for requesting UID
                    final boolean uidNetworkConnected;
                    if (syncAdapterInfo != null) {
                        final NetworkInfo networkInfo = getConnectivityManager()
                                .getActiveNetworkInfoForUid(syncAdapterInfo.uid);
                        uidNetworkConnected = networkInfo != null && networkInfo.isConnected();
                    } else {
                        uidNetworkConnected = false;
                    }

                    // skip the sync if it isn't manual, and auto sync or
                    // background data usage is disabled or network is
                    // disconnected for the target UID.
                    if (!op.extras.getBoolean(ContentResolver.SYNC_EXTRAS_IGNORE_SETTINGS, false)
                            && (syncableState > 0)
                            && (!mSyncStorageEngine.getMasterSyncAutomatically(op.userId)
                                || !backgroundDataUsageAllowed
                                || !uidNetworkConnected
                                || !mSyncStorageEngine.getSyncAutomatically(
                                       op.account, op.userId, op.authority))) {
                        operationIterator.remove();
                        mSyncStorageEngine.deleteFromPending(op.pendingOperation);
                        continue;
                    }

                    operations.add(op);
                }
            }

            // find the next operation to dispatch, if one is ready
            // iterate from the top, keep issuing (while potentially cancelling existing syncs)
            // until the quotas are filled.
            // once the quotas are filled iterate once more to find when the next one would be
            // (also considering pre-emption reasons).
            if (isLoggable) Log.v(TAG, "sort the candidate operations, size " + operations.size());
            Collections.sort(operations);
            if (isLoggable) Log.v(TAG, "dispatch all ready sync operations");
            for (int i = 0, N = operations.size(); i < N; i++) {
                final SyncOperation candidate = operations.get(i);
                final boolean candidateIsInitialization = candidate.isInitialization();

                int numInit = 0;
                int numRegular = 0;
                ActiveSyncContext conflict = null;
                ActiveSyncContext longRunning = null;
                ActiveSyncContext toReschedule = null;
                ActiveSyncContext oldestNonExpeditedRegular = null;

                for (ActiveSyncContext activeSyncContext : mActiveSyncContexts) {
                    final SyncOperation activeOp = activeSyncContext.mSyncOperation;
                    if (activeOp.isInitialization()) {
                        numInit++;
                    } else {
                        numRegular++;
                        if (!activeOp.isExpedited()) {
                            if (oldestNonExpeditedRegular == null
                                || (oldestNonExpeditedRegular.mStartTime
                                    > activeSyncContext.mStartTime)) {
                                oldestNonExpeditedRegular = activeSyncContext;
                            }
                        }
                    }
                    if (activeOp.account.type.equals(candidate.account.type)
                            && activeOp.authority.equals(candidate.authority)
                            && activeOp.userId == candidate.userId
                            && (!activeOp.allowParallelSyncs
                                || activeOp.account.name.equals(candidate.account.name))) {
                        conflict = activeSyncContext;
                        // don't break out since we want to do a full count of the varieties
                    } else {
                        if (candidateIsInitialization == activeOp.isInitialization()
                                && activeSyncContext.mStartTime + MAX_TIME_PER_SYNC < now) {
                            longRunning = activeSyncContext;
                            // don't break out since we want to do a full count of the varieties
                        }
                    }
                }

                if (isLoggable) {
                    Log.v(TAG, "candidate " + (i + 1) + " of " + N + ": " + candidate);
                    Log.v(TAG, "  numActiveInit=" + numInit + ", numActiveRegular=" + numRegular);
                    Log.v(TAG, "  longRunning: " + longRunning);
                    Log.v(TAG, "  conflict: " + conflict);
                    Log.v(TAG, "  oldestNonExpeditedRegular: " + oldestNonExpeditedRegular);
                }

                final boolean roomAvailable = candidateIsInitialization
                        ? numInit < MAX_SIMULTANEOUS_INITIALIZATION_SYNCS
                        : numRegular < MAX_SIMULTANEOUS_REGULAR_SYNCS;

                if (conflict != null) {
                    if (candidateIsInitialization && !conflict.mSyncOperation.isInitialization()
                            && numInit < MAX_SIMULTANEOUS_INITIALIZATION_SYNCS) {
                        toReschedule = conflict;
                        if (Log.isLoggable(TAG, Log.VERBOSE)) {
                            Log.v(TAG, "canceling and rescheduling sync since an initialization "
                                    + "takes higher priority, " + conflict);
                        }
                    } else if (candidate.expedited && !conflict.mSyncOperation.expedited
                            && (candidateIsInitialization
                                == conflict.mSyncOperation.isInitialization())) {
                        toReschedule = conflict;
                        if (Log.isLoggable(TAG, Log.VERBOSE)) {
                            Log.v(TAG, "canceling and rescheduling sync since an expedited "
                                    + "takes higher priority, " + conflict);
                        }
                    } else {
                        continue;
                    }
                } else if (roomAvailable) {
                    // dispatch candidate
                } else if (candidate.isExpedited() && oldestNonExpeditedRegular != null
                           && !candidateIsInitialization) {
                    // We found an active, non-expedited regular sync. We also know that the
                    // candidate doesn't conflict with this active sync since conflict
                    // is null. Reschedule the active sync and start the candidate.
                    toReschedule = oldestNonExpeditedRegular;
                    if (Log.isLoggable(TAG, Log.VERBOSE)) {
                        Log.v(TAG, "canceling and rescheduling sync since an expedited is ready to run, "
                                + oldestNonExpeditedRegular);
                    }
                } else if (longRunning != null
                        && (candidateIsInitialization
                            == longRunning.mSyncOperation.isInitialization())) {
                    // We found an active, long-running sync. Reschedule the active
                    // sync and start the candidate.
                    toReschedule = longRunning;
                    if (Log.isLoggable(TAG, Log.VERBOSE)) {
                        Log.v(TAG, "canceling and rescheduling sync since it ran roo long, "
                              + longRunning);
                    }
                } else {
                    // we were unable to find or make space to run this candidate, go on to
                    // the next one
                    continue;
                }

                if (toReschedule != null) {
                    runSyncFinishedOrCanceledLocked(null, toReschedule);
                    scheduleSyncOperation(toReschedule.mSyncOperation);
                }
                synchronized (mSyncQueue){
                    mSyncQueue.remove(candidate);
                }
                dispatchSyncOperation(candidate);
            }

            return nextReadyToRunTime;
     }

        private boolean dispatchSyncOperation(SyncOperation op) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "dispatchSyncOperation: we are going to sync " + op);
                Log.v(TAG, "num active syncs: " + mActiveSyncContexts.size());
                for (ActiveSyncContext syncContext : mActiveSyncContexts) {
                    Log.v(TAG, syncContext.toString());
                }
            }

            // connect to the sync adapter
            SyncAdapterType syncAdapterType = SyncAdapterType.newKey(op.authority, op.account.type);
            RegisteredServicesCache.ServiceInfo<SyncAdapterType> syncAdapterInfo =
                    mSyncAdapters.getServiceInfo(syncAdapterType);
            if (syncAdapterInfo == null) {
                Log.d(TAG, "can't find a sync adapter for " + syncAdapterType
                        + ", removing settings for it");
                mSyncStorageEngine.removeAuthority(op.account, op.userId, op.authority);
                return false;
            }

            ActiveSyncContext activeSyncContext =
                    new ActiveSyncContext(op, insertStartSyncEvent(op), syncAdapterInfo.uid);
            activeSyncContext.mSyncInfo = mSyncStorageEngine.addActiveSync(activeSyncContext);
            mActiveSyncContexts.add(activeSyncContext);
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "dispatchSyncOperation: starting " + activeSyncContext);
            }
            if (!activeSyncContext.bindToSyncAdapter(syncAdapterInfo)) {
                Log.e(TAG, "Bind attempt failed to " + syncAdapterInfo);
                closeActiveSyncContext(activeSyncContext);
                return false;
            }

            return true;
        }

        private void runBoundToSyncAdapter(final ActiveSyncContext activeSyncContext,
              ISyncAdapter syncAdapter) {
            activeSyncContext.mSyncAdapter = syncAdapter;
            final SyncOperation syncOperation = activeSyncContext.mSyncOperation;
            try {
                activeSyncContext.mIsLinkedToDeath = true;
                syncAdapter.asBinder().linkToDeath(activeSyncContext, 0);

                syncAdapter.startSync(activeSyncContext, syncOperation.authority,
                        syncOperation.account, syncOperation.extras);
            } catch (RemoteException remoteExc) {
                Log.d(TAG, "maybeStartNextSync: caught a RemoteException, rescheduling", remoteExc);
                closeActiveSyncContext(activeSyncContext);
                increaseBackoffSetting(syncOperation);
                scheduleSyncOperation(new SyncOperation(syncOperation));
            } catch (RuntimeException exc) {
                closeActiveSyncContext(activeSyncContext);
                Log.e(TAG, "Caught RuntimeException while starting the sync " + syncOperation, exc);
            }
        }

        private void cancelActiveSyncLocked(Account account, int userId, String authority) {
            ArrayList<ActiveSyncContext> activeSyncs =
                    new ArrayList<ActiveSyncContext>(mActiveSyncContexts);
            for (ActiveSyncContext activeSyncContext : activeSyncs) {
                if (activeSyncContext != null) {
                    // if an authority was specified then only cancel the sync if it matches
                    if (account != null) {
                        if (!account.equals(activeSyncContext.mSyncOperation.account)) {
                            continue;
                        }
                    }
                    // if an account was specified then only cancel the sync if it matches
                    if (authority != null) {
                        if (!authority.equals(activeSyncContext.mSyncOperation.authority)) {
                            continue;
                        }
                    }
                    // check if the userid matches
                    if (userId != UserId.USER_ALL
                            && userId != activeSyncContext.mSyncOperation.userId) {
                        continue;
                    }
                    runSyncFinishedOrCanceledLocked(null /* no result since this is a cancel */,
                            activeSyncContext);
                }
            }
        }

        private void runSyncFinishedOrCanceledLocked(SyncResult syncResult,
                ActiveSyncContext activeSyncContext) {
            boolean isLoggable = Log.isLoggable(TAG, Log.VERBOSE);

            if (activeSyncContext.mIsLinkedToDeath) {
                activeSyncContext.mSyncAdapter.asBinder().unlinkToDeath(activeSyncContext, 0);
                activeSyncContext.mIsLinkedToDeath = false;
            }
            closeActiveSyncContext(activeSyncContext);
=======
            SyncOperation syncOperation;
            final Sync.Settings.QueryMap syncSettings = getSyncSettings();
            synchronized (mSyncQueue) {
                while (true) {
                    syncOperation = mSyncQueue.head();
                    if (syncOperation == null) {
                        if (isLoggable) {
                            Log.v(TAG, "runStateIdle: no more sync operations, returning");
                        }
                        return;
                    }

                    // Sync is disabled, drop this operation.
                    if (!isSyncEnabled()) {
                        if (isLoggable) {
                            Log.v(TAG, "runStateIdle: sync disabled, dropping " + syncOperation);
                        }
                        mSyncQueue.popHead();
                        continue;
                    }

                    // skip the sync if it isn't a force and the settings are off for this provider
                    final boolean force = syncOperation.extras.getBoolean(
                            ContentResolver.SYNC_EXTRAS_FORCE, false);
                    if (!force && (!syncSettings.getListenForNetworkTickles()
                            || !syncSettings.getSyncProviderAutomatically(
                            syncOperation.authority))) {
                        if (isLoggable) {
                            Log.v(TAG, "runStateIdle: sync off, dropping " + syncOperation);
                        }
                        mSyncQueue.popHead();
                        continue;
                    }

                    // skip the sync if the account of this operation no longer exists
                    if (!ArrayUtils.contains(accounts, syncOperation.account)) {
                        mSyncQueue.popHead();
                        if (isLoggable) {
                            Log.v(TAG, "runStateIdle: account not present, dropping "
                                    + syncOperation);
                        }
                        continue;
                    }

                    // go ahead and try to sync this syncOperation
                    if (isLoggable) {
                        Log.v(TAG, "runStateIdle: found sync candidate: " + syncOperation);
                    }
                    break;
                }

                // If the first SyncOperation isn't ready to run schedule a wakeup and
                // get out.
                final long now = SystemClock.elapsedRealtime();
                if (syncOperation.earliestRunTime > now) {
                    if (Log.isLoggable(TAG, Log.DEBUG)) {
                        Log.d(TAG, "runStateIdle: the time is " + now + " yet the next "
                                + "sync operation is for " + syncOperation.earliestRunTime
                                + ": " + syncOperation);
                    }
                    return;
                }

                // We will do this sync. Remove it from the queue and run it outside of the
                // synchronized block.
                if (isLoggable) {
                    Log.v(TAG, "runStateIdle: we are going to sync " + syncOperation);
                }
                mSyncQueue.popHead();
            }

            String providerName = syncOperation.authority;
            ensureContentResolver();
            IContentProvider contentProvider;

            // acquire the provider and update the sync history
            try {
                contentProvider = mContentResolver.acquireProvider(providerName);
                if (contentProvider == null) {
                    Log.e(TAG, "Provider " + providerName + " doesn't exist");
                    return;
                }
                if (contentProvider.getSyncAdapter() == null) {
                    Log.e(TAG, "Provider " + providerName + " isn't syncable, " + contentProvider);
                    return;
                }
            } catch (RemoteException remoteExc) {
                Log.e(TAG, "Caught a RemoteException while preparing for sync, rescheduling "
                        + syncOperation, remoteExc);
                rescheduleWithDelay(syncOperation);
                return;
            } catch (RuntimeException exc) {
                Log.e(TAG, "Caught a RuntimeException while validating sync of " + providerName,
                        exc);
                return;
            }

            final long historyRowId = insertStartSyncEvent(syncOperation);

            try {
                ISyncAdapter syncAdapter = contentProvider.getSyncAdapter();
                ActiveSyncContext activeSyncContext = new ActiveSyncContext(syncOperation,
                        contentProvider, syncAdapter, historyRowId);
                mSyncWakeLock.acquire();
                if (Log.isLoggable(TAG, Log.DEBUG)) {
                    Log.d(TAG, "starting sync of " + syncOperation);
                }
                syncAdapter.startSync(activeSyncContext, syncOperation.account,
                        syncOperation.extras);
                mActiveSyncContext = activeSyncContext;
                mSyncStorageEngine.setActiveSync(mActiveSyncContext);
            } catch (RemoteException remoteExc) {
                if (Config.LOGD) {
                    Log.d(TAG, "runStateIdle: caught a RemoteException, rescheduling", remoteExc);
                }
                mActiveSyncContext = null;
                mSyncStorageEngine.setActiveSync(mActiveSyncContext);
                rescheduleWithDelay(syncOperation);
            } catch (RuntimeException exc) {
                mActiveSyncContext = null;
                mSyncStorageEngine.setActiveSync(mActiveSyncContext);
                Log.e(TAG, "Caught a RuntimeException while starting the sync " + syncOperation,
                        exc);
            }

            // no need to schedule an alarm, as that will be done by our caller.
        }

        private void runSyncFinishedOrCanceled(SyncResult syncResult) {
            boolean isLoggable = Log.isLoggable(TAG, Log.VERBOSE);
            if (isLoggable) Log.v(TAG, "runSyncFinishedOrCanceled");
            ActiveSyncContext activeSyncContext = mActiveSyncContext;
            mActiveSyncContext = null;
            mSyncStorageEngine.setActiveSync(mActiveSyncContext);
>>>>>>> 54b6cfa... Initial Contribution

            final SyncOperation syncOperation = activeSyncContext.mSyncOperation;

            final long elapsedTime = SystemClock.elapsedRealtime() - activeSyncContext.mStartTime;

            String historyMessage;
            int downstreamActivity;
            int upstreamActivity;
            if (syncResult != null) {
                if (isLoggable) {
<<<<<<< HEAD
                    Log.v(TAG, "runSyncFinishedOrCanceled [finished]: "
=======
                    Log.v(TAG, "runSyncFinishedOrCanceled: is a finished: operation "
>>>>>>> 54b6cfa... Initial Contribution
                            + syncOperation + ", result " + syncResult);
                }

                if (!syncResult.hasError()) {
<<<<<<< HEAD
                    historyMessage = SyncStorageEngine.MESG_SUCCESS;
                    // TODO: set these correctly when the SyncResult is extended to include it
                    downstreamActivity = 0;
                    upstreamActivity = 0;
                    clearBackoffSetting(syncOperation);
                } else {
                    Log.d(TAG, "failed sync operation " + syncOperation + ", " + syncResult);
                    // the operation failed so increase the backoff time
                    if (!syncResult.syncAlreadyInProgress) {
                        increaseBackoffSetting(syncOperation);
                    }
                    // reschedule the sync if so indicated by the syncResult
                    maybeRescheduleSync(syncResult, syncOperation);
=======
                    if (isLoggable) {
                        Log.v(TAG, "finished sync operation " + syncOperation);
                    }
                    historyMessage = History.MESG_SUCCESS;
                    // TODO: set these correctly when the SyncResult is extended to include it
                    downstreamActivity = 0;
                    upstreamActivity = 0;
                } else {
                    maybeRescheduleSync(syncResult, syncOperation);
                    if (Config.LOGD) {
                        Log.d(TAG, "failed sync operation " + syncOperation);
                    }
>>>>>>> 54b6cfa... Initial Contribution
                    historyMessage = Integer.toString(syncResultToErrorNumber(syncResult));
                    // TODO: set these correctly when the SyncResult is extended to include it
                    downstreamActivity = 0;
                    upstreamActivity = 0;
                }
<<<<<<< HEAD

                setDelayUntilTime(syncOperation, syncResult.delayUntil);
            } else {
                if (isLoggable) {
                    Log.v(TAG, "runSyncFinishedOrCanceled [canceled]: " + syncOperation);
                }
                if (activeSyncContext.mSyncAdapter != null) {
                    try {
                        activeSyncContext.mSyncAdapter.cancelSync(activeSyncContext);
                    } catch (RemoteException e) {
                        // we don't need to retry this in this case
                    }
                }
                historyMessage = SyncStorageEngine.MESG_CANCELED;
=======
            } else {
                if (isLoggable) {
                    Log.v(TAG, "runSyncFinishedOrCanceled: is a cancel: operation "
                            + syncOperation);
                }
                try {
                    activeSyncContext.mSyncAdapter.cancelSync();
                } catch (RemoteException e) {
                    // we don't need to retry this in this case
                }
                historyMessage = History.MESG_CANCELED;
>>>>>>> 54b6cfa... Initial Contribution
                downstreamActivity = 0;
                upstreamActivity = 0;
            }

            stopSyncEvent(activeSyncContext.mHistoryRowId, syncOperation, historyMessage,
                    upstreamActivity, downstreamActivity, elapsedTime);

<<<<<<< HEAD
=======
            mContentResolver.releaseProvider(activeSyncContext.mContentProvider);

>>>>>>> 54b6cfa... Initial Contribution
            if (syncResult != null && syncResult.tooManyDeletions) {
                installHandleTooManyDeletesNotification(syncOperation.account,
                        syncOperation.authority, syncResult.stats.numDeletes);
            } else {
                mNotificationMgr.cancel(
                        syncOperation.account.hashCode() ^ syncOperation.authority.hashCode());
            }

            if (syncResult != null && syncResult.fullSyncRequested) {
<<<<<<< HEAD
                scheduleSyncOperation(new SyncOperation(syncOperation.account, syncOperation.userId,
                        syncOperation.syncSource, syncOperation.authority, new Bundle(), 0,
                        syncOperation.backoff, syncOperation.delayUntil,
                        syncOperation.allowParallelSyncs));
=======
                scheduleSyncOperation(new SyncOperation(syncOperation.account,
                        syncOperation.syncSource, syncOperation.authority, new Bundle(), 0));
>>>>>>> 54b6cfa... Initial Contribution
            }
            // no need to schedule an alarm, as that will be done by our caller.
        }

<<<<<<< HEAD
        private void closeActiveSyncContext(ActiveSyncContext activeSyncContext) {
            activeSyncContext.close();
            mActiveSyncContexts.remove(activeSyncContext);
            mSyncStorageEngine.removeActiveSync(activeSyncContext.mSyncInfo,
                    activeSyncContext.mSyncOperation.userId);
        }

=======
>>>>>>> 54b6cfa... Initial Contribution
        /**
         * Convert the error-containing SyncResult into the Sync.History error number. Since
         * the SyncResult may indicate multiple errors at once, this method just returns the
         * most "serious" error.
         * @param syncResult the SyncResult from which to read
         * @return the most "serious" error set in the SyncResult
         * @throws IllegalStateException if the SyncResult does not indicate any errors.
<<<<<<< HEAD
         *   If SyncResult.error() is true then it is safe to call this.
         */
        private int syncResultToErrorNumber(SyncResult syncResult) {
            if (syncResult.syncAlreadyInProgress)
                return ContentResolver.SYNC_ERROR_SYNC_ALREADY_IN_PROGRESS;
            if (syncResult.stats.numAuthExceptions > 0)
                return ContentResolver.SYNC_ERROR_AUTHENTICATION;
            if (syncResult.stats.numIoExceptions > 0)
                return ContentResolver.SYNC_ERROR_IO;
            if (syncResult.stats.numParseExceptions > 0)
                return ContentResolver.SYNC_ERROR_PARSE;
            if (syncResult.stats.numConflictDetectedExceptions > 0)
                return ContentResolver.SYNC_ERROR_CONFLICT;
            if (syncResult.tooManyDeletions)
                return ContentResolver.SYNC_ERROR_TOO_MANY_DELETIONS;
            if (syncResult.tooManyRetries)
                return ContentResolver.SYNC_ERROR_TOO_MANY_RETRIES;
            if (syncResult.databaseError)
                return ContentResolver.SYNC_ERROR_INTERNAL;
            throw new IllegalStateException("we are not in an error state, " + syncResult);
        }

        private void manageSyncNotificationLocked() {
            boolean shouldCancel;
            boolean shouldInstall;

            if (mActiveSyncContexts.isEmpty()) {
=======
         *   If SyncResult.error() is true then it is safe to call this.   
         */
        private int syncResultToErrorNumber(SyncResult syncResult) {
            if (syncResult.syncAlreadyInProgress) return History.ERROR_SYNC_ALREADY_IN_PROGRESS;
            if (syncResult.stats.numAuthExceptions > 0) return History.ERROR_AUTHENTICATION;
            if (syncResult.stats.numIoExceptions > 0) return History.ERROR_IO;
            if (syncResult.stats.numParseExceptions > 0) return History.ERROR_PARSE;
            if (syncResult.stats.numConflictDetectedExceptions > 0) return History.ERROR_CONFLICT;
            if (syncResult.tooManyDeletions) return History.ERROR_TOO_MANY_DELETIONS;
            if (syncResult.tooManyRetries) return History.ERROR_TOO_MANY_RETRIES;
            throw new IllegalStateException("we are not in an error state, " + toString());
        }

        private void manageSyncNotification() {
            boolean shouldCancel;
            boolean shouldInstall;

            if (mActiveSyncContext == null) {
>>>>>>> 54b6cfa... Initial Contribution
                mSyncNotificationInfo.startTime = null;

                // we aren't syncing. if the notification is active then remember that we need
                // to cancel it and then clear out the info
                shouldCancel = mSyncNotificationInfo.isActive;
                shouldInstall = false;
            } else {
                // we are syncing
<<<<<<< HEAD
=======
                final SyncOperation syncOperation = mActiveSyncContext.mSyncOperation;

>>>>>>> 54b6cfa... Initial Contribution
                final long now = SystemClock.elapsedRealtime();
                if (mSyncNotificationInfo.startTime == null) {
                    mSyncNotificationInfo.startTime = now;
                }

<<<<<<< HEAD
                // there are three cases:
                // - the notification is up: do nothing
=======
                // cancel the notification if it is up and the authority or account is wrong
                shouldCancel = mSyncNotificationInfo.isActive &&
                        (!syncOperation.authority.equals(mSyncNotificationInfo.authority)
                        || !syncOperation.account.equals(mSyncNotificationInfo.account));

                // there are four cases:
                // - the notification is up and there is no change: do nothing
                // - the notification is up but we should cancel since it is stale:
                //   need to install
>>>>>>> 54b6cfa... Initial Contribution
                // - the notification is not up but it isn't time yet: don't install
                // - the notification is not up and it is time: need to install

                if (mSyncNotificationInfo.isActive) {
<<<<<<< HEAD
                    shouldInstall = shouldCancel = false;
                } else {
                    // it isn't currently up, so there is nothing to cancel
                    shouldCancel = false;

                    final boolean timeToShowNotification =
                            now > mSyncNotificationInfo.startTime + SYNC_NOTIFICATION_DELAY;
                    if (timeToShowNotification) {
                        shouldInstall = true;
                    } else {
                        // show the notification immediately if this is a manual sync
                        shouldInstall = false;
                        for (ActiveSyncContext activeSyncContext : mActiveSyncContexts) {
                            final boolean manualSync = activeSyncContext.mSyncOperation.extras
                                    .getBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, false);
                            if (manualSync) {
                                shouldInstall = true;
                                break;
                            }
                        }
                    }
=======
                    shouldInstall = shouldCancel;
                } else {
                    final boolean timeToShowNotification = 
                            now > mSyncNotificationInfo.startTime + SYNC_NOTIFICATION_DELAY;
                    final boolean syncIsForced = syncOperation.extras
                            .getBoolean(ContentResolver.SYNC_EXTRAS_FORCE, false);
                    shouldInstall = timeToShowNotification || syncIsForced;
>>>>>>> 54b6cfa... Initial Contribution
                }
            }

            if (shouldCancel && !shouldInstall) {
                mNeedSyncActiveNotification = false;
                sendSyncStateIntent();
                mSyncNotificationInfo.isActive = false;
            }

            if (shouldInstall) {
<<<<<<< HEAD
                mNeedSyncActiveNotification = true;
                sendSyncStateIntent();
                mSyncNotificationInfo.isActive = true;
            }
        }

        private void manageSyncAlarmLocked(long nextPeriodicEventElapsedTime,
                long nextPendingEventElapsedTime) {
            // in each of these cases the sync loop will be kicked, which will cause this
            // method to be called again
            if (!mDataConnectionIsConnected) return;
            if (mStorageIsLow) return;

            // When the status bar notification should be raised
            final long notificationTime =
                    (!mSyncHandler.mSyncNotificationInfo.isActive
                            && mSyncHandler.mSyncNotificationInfo.startTime != null)
                            ? mSyncHandler.mSyncNotificationInfo.startTime + SYNC_NOTIFICATION_DELAY
                            : Long.MAX_VALUE;

            // When we should consider canceling an active sync
            long earliestTimeoutTime = Long.MAX_VALUE;
            for (ActiveSyncContext currentSyncContext : mActiveSyncContexts) {
                final long currentSyncTimeoutTime =
                        currentSyncContext.mTimeoutStartTime + MAX_TIME_PER_SYNC;
                if (Log.isLoggable(TAG, Log.VERBOSE)) {
                    Log.v(TAG, "manageSyncAlarm: active sync, mTimeoutStartTime + MAX is "
                            + currentSyncTimeoutTime);
                }
                if (earliestTimeoutTime > currentSyncTimeoutTime) {
                    earliestTimeoutTime = currentSyncTimeoutTime;
                }
            }

            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "manageSyncAlarm: notificationTime is " + notificationTime);
            }

            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "manageSyncAlarm: earliestTimeoutTime is " + earliestTimeoutTime);
            }

            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "manageSyncAlarm: nextPeriodicEventElapsedTime is "
                        + nextPeriodicEventElapsedTime);
            }
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "manageSyncAlarm: nextPendingEventElapsedTime is "
                        + nextPendingEventElapsedTime);
            }

            long alarmTime = Math.min(notificationTime, earliestTimeoutTime);
            alarmTime = Math.min(alarmTime, nextPeriodicEventElapsedTime);
            alarmTime = Math.min(alarmTime, nextPendingEventElapsedTime);

            // Bound the alarm time.
            final long now = SystemClock.elapsedRealtime();
            if (alarmTime < now + SYNC_ALARM_TIMEOUT_MIN) {
                if (Log.isLoggable(TAG, Log.VERBOSE)) {
                    Log.v(TAG, "manageSyncAlarm: the alarmTime is too small, "
                            + alarmTime + ", setting to " + (now + SYNC_ALARM_TIMEOUT_MIN));
                }
                alarmTime = now + SYNC_ALARM_TIMEOUT_MIN;
            } else if (alarmTime > now + SYNC_ALARM_TIMEOUT_MAX) {
                if (Log.isLoggable(TAG, Log.VERBOSE)) {
                    Log.v(TAG, "manageSyncAlarm: the alarmTime is too large, "
                            + alarmTime + ", setting to " + (now + SYNC_ALARM_TIMEOUT_MIN));
                }
                alarmTime = now + SYNC_ALARM_TIMEOUT_MAX;
=======
                SyncOperation syncOperation = mActiveSyncContext.mSyncOperation;
                mNeedSyncActiveNotification = true;
                sendSyncStateIntent();
                mSyncNotificationInfo.isActive = true;
                mSyncNotificationInfo.account = syncOperation.account;
                mSyncNotificationInfo.authority = syncOperation.authority;
            }
        }

        /**
         * Check if there were any long-lasting errors, if so install the error notification,
         * otherwise cancel the error notification.
         */
        private void manageErrorNotification() {
            //
            long when = mSyncStorageEngine.getInitialSyncFailureTime();
            if ((when > 0) && (when + ERROR_NOTIFICATION_DELAY_MS < System.currentTimeMillis())) {
                if (!mErrorNotificationInstalled) {
                    mNeedSyncErrorNotification = true;
                    sendSyncStateIntent();
                }
                mErrorNotificationInstalled = true;
            } else {
                if (mErrorNotificationInstalled) {
                    mNeedSyncErrorNotification = false;
                    sendSyncStateIntent();
                }
                mErrorNotificationInstalled = false;
            }
        }

        private void manageSyncAlarm() {
            // in each of these cases the sync loop will be kicked, which will cause this
            // method to be called again
            if (!mDataConnectionIsConnected) return;
            if (mAccounts == null) return;
            if (mStorageIsLow) return;
            
            // Compute the alarm fire time:
            // - not syncing: time of the next sync operation
            // - syncing, no notification: time from sync start to notification create time
            // - syncing, with notification: time till timeout of the active sync operation
            Long alarmTime = null;
            ActiveSyncContext activeSyncContext = mActiveSyncContext;
            if (activeSyncContext == null) {
                SyncOperation syncOperation;
                synchronized (mSyncQueue) {
                    syncOperation = mSyncQueue.head();
                }
                if (syncOperation != null) {
                    alarmTime = syncOperation.earliestRunTime;
                }
            } else {
                final long notificationTime =
                        mSyncHandler.mSyncNotificationInfo.startTime + SYNC_NOTIFICATION_DELAY;
                final long timeoutTime =
                        mActiveSyncContext.mTimeoutStartTime + MAX_TIME_PER_SYNC;
                if (mSyncHandler.mSyncNotificationInfo.isActive) {
                    alarmTime = timeoutTime;
                } else {
                    alarmTime = Math.min(notificationTime, timeoutTime);
                }
            }

            // adjust the alarmTime so that we will wake up when it is time to
            // install the error notification
            if (!mErrorNotificationInstalled) {
                long when = mSyncStorageEngine.getInitialSyncFailureTime();
                if (when > 0) {
                    when += ERROR_NOTIFICATION_DELAY_MS;
                    // convert when fron absolute time to elapsed run time
                    long delay = when - System.currentTimeMillis();
                    when = SystemClock.elapsedRealtime() + delay;
                    alarmTime = alarmTime != null ? Math.min(alarmTime, when) : when;
                }
>>>>>>> 54b6cfa... Initial Contribution
            }

            // determine if we need to set or cancel the alarm
            boolean shouldSet = false;
            boolean shouldCancel = false;
            final boolean alarmIsActive = mAlarmScheduleTime != null;
<<<<<<< HEAD
            final boolean needAlarm = alarmTime != Long.MAX_VALUE;
=======
            final boolean needAlarm = alarmTime != null;
>>>>>>> 54b6cfa... Initial Contribution
            if (needAlarm) {
                if (!alarmIsActive || alarmTime < mAlarmScheduleTime) {
                    shouldSet = true;
                }
            } else {
                shouldCancel = alarmIsActive;
            }

            // set or cancel the alarm as directed
            ensureAlarmService();
            if (shouldSet) {
<<<<<<< HEAD
                if (Log.isLoggable(TAG, Log.VERBOSE)) {
                    Log.v(TAG, "requesting that the alarm manager wake us up at elapsed time "
                            + alarmTime + ", now is " + now + ", " + ((alarmTime - now) / 1000)
                            + " secs from now");
                }
=======
>>>>>>> 54b6cfa... Initial Contribution
                mAlarmScheduleTime = alarmTime;
                mAlarmService.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, alarmTime,
                        mSyncAlarmIntent);
            } else if (shouldCancel) {
                mAlarmScheduleTime = null;
                mAlarmService.cancel(mSyncAlarmIntent);
            }
        }

        private void sendSyncStateIntent() {
            Intent syncStateIntent = new Intent(Intent.ACTION_SYNC_STATE_CHANGED);
<<<<<<< HEAD
            syncStateIntent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY_BEFORE_BOOT);
            syncStateIntent.putExtra("active", mNeedSyncActiveNotification);
            syncStateIntent.putExtra("failing", false);
            mContext.sendBroadcast(syncStateIntent);
        }

        private void installHandleTooManyDeletesNotification(Account account, String authority,
                long numDeletes) {
            if (mNotificationMgr == null) return;

            final ProviderInfo providerInfo = mContext.getPackageManager().resolveContentProvider(
                    authority, 0 /* flags */);
            if (providerInfo == null) {
                return;
            }
            CharSequence authorityName = providerInfo.loadLabel(mContext.getPackageManager());

            Intent clickIntent = new Intent(mContext, SyncActivityTooManyDeletes.class);
            clickIntent.putExtra("account", account);
            clickIntent.putExtra("authority", authority);
            clickIntent.putExtra("provider", authorityName.toString());
            clickIntent.putExtra("numDeletes", numDeletes);

=======
            syncStateIntent.putExtra("active", mNeedSyncActiveNotification);
            syncStateIntent.putExtra("failing", mNeedSyncErrorNotification);
            mContext.sendBroadcast(syncStateIntent);
        }

        private void installHandleTooManyDeletesNotification(String account, String authority,
                long numDeletes) {
            if (mNotificationMgr == null) return;
            Intent clickIntent = new Intent();
            clickIntent.setClassName("com.android.providers.subscribedfeeds",
                    "com.android.settings.SyncActivityTooManyDeletes");
            clickIntent.putExtra("account", account);
            clickIntent.putExtra("provider", authority);
            clickIntent.putExtra("numDeletes", numDeletes);
            
>>>>>>> 54b6cfa... Initial Contribution
            if (!isActivityAvailable(clickIntent)) {
                Log.w(TAG, "No activity found to handle too many deletes.");
                return;
            }
<<<<<<< HEAD

=======
            
>>>>>>> 54b6cfa... Initial Contribution
            final PendingIntent pendingIntent = PendingIntent
                    .getActivity(mContext, 0, clickIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            CharSequence tooManyDeletesDescFormat = mContext.getResources().getText(
                    R.string.contentServiceTooManyDeletesNotificationDesc);

<<<<<<< HEAD
=======
            String[] authorities = authority.split(";");
>>>>>>> 54b6cfa... Initial Contribution
            Notification notification =
                new Notification(R.drawable.stat_notify_sync_error,
                        mContext.getString(R.string.contentServiceSync),
                        System.currentTimeMillis());
            notification.setLatestEventInfo(mContext,
                    mContext.getString(R.string.contentServiceSyncNotificationTitle),
<<<<<<< HEAD
                    String.format(tooManyDeletesDescFormat.toString(), authorityName),
=======
                    String.format(tooManyDeletesDescFormat.toString(), authorities[0]),
>>>>>>> 54b6cfa... Initial Contribution
                    pendingIntent);
            notification.flags |= Notification.FLAG_ONGOING_EVENT;
            mNotificationMgr.notify(account.hashCode() ^ authority.hashCode(), notification);
        }

        /**
         * Checks whether an activity exists on the system image for the given intent.
<<<<<<< HEAD
         *
=======
         * 
>>>>>>> 54b6cfa... Initial Contribution
         * @param intent The intent for an activity.
         * @return Whether or not an activity exists.
         */
        private boolean isActivityAvailable(Intent intent) {
            PackageManager pm = mContext.getPackageManager();
            List<ResolveInfo> list = pm.queryIntentActivities(intent, 0);
            int listSize = list.size();
            for (int i = 0; i < listSize; i++) {
                ResolveInfo resolveInfo = list.get(i);
                if ((resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)
                        != 0) {
                    return true;
                }
            }
<<<<<<< HEAD

            return false;
        }

=======
            
            return false;
        }
        
>>>>>>> 54b6cfa... Initial Contribution
        public long insertStartSyncEvent(SyncOperation syncOperation) {
            final int source = syncOperation.syncSource;
            final long now = System.currentTimeMillis();

<<<<<<< HEAD
            EventLog.writeEvent(2720, syncOperation.authority,
                                SyncStorageEngine.EVENT_START, source,
                                syncOperation.account.name.hashCode());

            return mSyncStorageEngine.insertStartSyncEvent(
                    syncOperation.account, syncOperation.userId, syncOperation.authority,
                    now, source, syncOperation.isInitialization());
=======
            EventLog.writeEvent(2720, syncOperation.authority, Sync.History.EVENT_START, source);

            return mSyncStorageEngine.insertStartSyncEvent(
                    syncOperation.account, syncOperation.authority, now, source);
>>>>>>> 54b6cfa... Initial Contribution
        }

        public void stopSyncEvent(long rowId, SyncOperation syncOperation, String resultMessage,
                int upstreamActivity, int downstreamActivity, long elapsedTime) {
<<<<<<< HEAD
            EventLog.writeEvent(2720, syncOperation.authority,
                                SyncStorageEngine.EVENT_STOP, syncOperation.syncSource,
                                syncOperation.account.name.hashCode());

            mSyncStorageEngine.stopSyncEvent(rowId, elapsedTime,
                    resultMessage, downstreamActivity, upstreamActivity);
        }
    }

    private boolean isSyncStillActive(ActiveSyncContext activeSyncContext) {
        for (ActiveSyncContext sync : mActiveSyncContexts) {
            if (sync == activeSyncContext) {
                return true;
            }
        }
        return false;
=======
            EventLog.writeEvent(2720, syncOperation.authority, Sync.History.EVENT_STOP, syncOperation.syncSource);

            mSyncStorageEngine.stopSyncEvent(rowId, elapsedTime, resultMessage,
                    downstreamActivity, upstreamActivity);
        }
    }

    static class SyncQueue {
        private SyncStorageEngine mSyncStorageEngine;
        private final String[] COLUMNS = new String[]{
                "_id",
                "authority",
                "account",
                "extras",
                "source"
        };
        private static final int COLUMN_ID = 0;
        private static final int COLUMN_AUTHORITY = 1;
        private static final int COLUMN_ACCOUNT = 2;
        private static final int COLUMN_EXTRAS = 3;
        private static final int COLUMN_SOURCE = 4;

        private static final boolean DEBUG_CHECK_DATA_CONSISTENCY = false;

        // A priority queue of scheduled SyncOperations that is designed to make it quick
        // to find the next SyncOperation that should be considered for running.
        private final PriorityQueue<SyncOperation> mOpsByWhen = new PriorityQueue<SyncOperation>();

        // A Map of SyncOperations operationKey -> SyncOperation that is designed for
        // quick lookup of an enqueued SyncOperation.
        private final HashMap<String, SyncOperation> mOpsByKey = Maps.newHashMap();

        public SyncQueue(SyncStorageEngine syncStorageEngine) {
            mSyncStorageEngine = syncStorageEngine;
            Cursor cursor = mSyncStorageEngine.getPendingSyncsCursor(COLUMNS);
            try {
                while (cursor.moveToNext()) {
                    add(cursorToOperation(cursor),
                            true /* this is being added from the database */);
                }
            } finally {
                cursor.close();
                if (DEBUG_CHECK_DATA_CONSISTENCY) debugCheckDataStructures(true /* check the DB */);
            }
        }

        public boolean add(SyncOperation operation) {
            return add(new SyncOperation(operation),
                    false /* this is not coming from the database */);
        }

        private boolean add(SyncOperation operation, boolean fromDatabase) {
            if (DEBUG_CHECK_DATA_CONSISTENCY) debugCheckDataStructures(!fromDatabase);

            // If this operation is expedited then set its earliestRunTime to be immediately
            // before the head of the list, or not if none are in the list.
            if (operation.delay < 0) {
                SyncOperation headOperation = head();
                if (headOperation != null) {
                    operation.earliestRunTime = Math.min(SystemClock.elapsedRealtime(),
                            headOperation.earliestRunTime - 1);
                } else {
                    operation.earliestRunTime = SystemClock.elapsedRealtime();
                }
            }

            // - if an operation with the same key exists and this one should run earlier,
            //   delete the old one and add the new one
            // - if an operation with the same key exists and if this one should run
            //   later, ignore it
            // - if no operation exists then add the new one
            final String operationKey = operation.key;
            SyncOperation existingOperation = mOpsByKey.get(operationKey);

            // if this operation matches an existing operation that is being retried (delay > 0)
            // and this operation isn't forced, ignore this operation
            if (existingOperation != null && existingOperation.delay > 0) {
                if (!operation.extras.getBoolean(ContentResolver.SYNC_EXTRAS_FORCE, false)) {
                    return false;
                }
            }

            if (existingOperation != null
                    && operation.earliestRunTime >= existingOperation.earliestRunTime) {
                if (DEBUG_CHECK_DATA_CONSISTENCY) debugCheckDataStructures(!fromDatabase);
                return false;
            }

            if (existingOperation != null) {
                removeByKey(operationKey);
            }

            if (operation.rowId == null) {
                byte[] extrasData = null;
                Parcel parcel = Parcel.obtain();
                try {
                    operation.extras.writeToParcel(parcel, 0);
                    extrasData = parcel.marshall();
                } finally {
                    parcel.recycle();
                }
                ContentValues values = new ContentValues();
                values.put("account", operation.account);
                values.put("authority", operation.authority);
                values.put("source", operation.syncSource);
                values.put("extras", extrasData);
                Uri pendingUri = mSyncStorageEngine.insertIntoPending(values);
                operation.rowId = pendingUri == null ? null : ContentUris.parseId(pendingUri);
                if (operation.rowId == null) {
                    throw new IllegalStateException("error adding pending sync operation "
                            + operation);
                }
            }

            if (DEBUG_CHECK_DATA_CONSISTENCY) {
                debugCheckDataStructures(
                        false /* don't compare with the DB, since we know
                               it is inconsistent right now */ );
            }
            mOpsByKey.put(operationKey, operation);
            mOpsByWhen.add(operation);
            if (DEBUG_CHECK_DATA_CONSISTENCY) debugCheckDataStructures(!fromDatabase);
            return true;
        }

        public void removeByKey(String operationKey) {
            if (DEBUG_CHECK_DATA_CONSISTENCY) debugCheckDataStructures(true /* check the DB */);
            SyncOperation operationToRemove = mOpsByKey.remove(operationKey);
            if (!mOpsByWhen.remove(operationToRemove)) {
                throw new IllegalStateException(
                        "unable to find " + operationToRemove + " in mOpsByWhen");
            }

            if (mSyncStorageEngine.deleteFromPending(operationToRemove.rowId) != 1) {
                throw new IllegalStateException("unable to find pending row for "
                        + operationToRemove);
            }

            if (DEBUG_CHECK_DATA_CONSISTENCY) debugCheckDataStructures(true /* check the DB */);
        }

        public SyncOperation head() {
            if (DEBUG_CHECK_DATA_CONSISTENCY) debugCheckDataStructures(true /* check the DB */);
            return mOpsByWhen.peek();
        }

        public void popHead() {
            if (DEBUG_CHECK_DATA_CONSISTENCY) debugCheckDataStructures(true /* check the DB */);
            SyncOperation operation = mOpsByWhen.remove();
            if (mOpsByKey.remove(operation.key) == null) {
                throw new IllegalStateException("unable to find " + operation + " in mOpsByKey");
            }

            if (mSyncStorageEngine.deleteFromPending(operation.rowId) != 1) {
                throw new IllegalStateException("unable to find pending row for " + operation);
            }

            if (DEBUG_CHECK_DATA_CONSISTENCY) debugCheckDataStructures(true /* check the DB */);
        }

        public void clear(String account, String authority) {
            Iterator<Map.Entry<String, SyncOperation>> entries = mOpsByKey.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, SyncOperation> entry = entries.next();
                SyncOperation syncOperation = entry.getValue();
                if (account != null && !syncOperation.account.equals(account)) continue;
                if (authority != null && !syncOperation.authority.equals(authority)) continue;

                if (DEBUG_CHECK_DATA_CONSISTENCY) debugCheckDataStructures(true /* check the DB */);
                entries.remove();
                if (!mOpsByWhen.remove(syncOperation)) {
                    throw new IllegalStateException(
                            "unable to find " + syncOperation + " in mOpsByWhen");
                }

                if (mSyncStorageEngine.deleteFromPending(syncOperation.rowId) != 1) {
                    throw new IllegalStateException("unable to find pending row for "
                            + syncOperation);
                }

                if (DEBUG_CHECK_DATA_CONSISTENCY) debugCheckDataStructures(true /* check the DB */);
            }
        }

        public void dump(StringBuilder sb) {
            sb.append("SyncQueue: ").append(mOpsByWhen.size()).append(" operation(s)\n");
            for (SyncOperation operation : mOpsByWhen) {
                sb.append(operation).append("\n");
            }
        }

        private void debugCheckDataStructures(boolean checkDatabase) {
            if (mOpsByKey.size() != mOpsByWhen.size()) {
                throw new IllegalStateException("size mismatch: "
                        + mOpsByKey .size() + " != " + mOpsByWhen.size());
            }
            for (SyncOperation operation : mOpsByWhen) {
                if (!mOpsByKey.containsKey(operation.key)) {
                    throw new IllegalStateException(
                        "operation " + operation + " is in mOpsByWhen but not mOpsByKey");
                }
            }
            for (Map.Entry<String, SyncOperation> entry : mOpsByKey.entrySet()) {
                final SyncOperation operation = entry.getValue();
                final String key = entry.getKey();
                if (!key.equals(operation.key)) {
                    throw new IllegalStateException(
                        "operation " + operation + " in mOpsByKey doesn't match key " + key);
                }
                if (!mOpsByWhen.contains(operation)) {
                    throw new IllegalStateException(
                        "operation " + operation + " is in mOpsByKey but not mOpsByWhen");
                }
            }

            if (checkDatabase) {
                // check that the DB contains the same rows as the in-memory data structures
                Cursor cursor = mSyncStorageEngine.getPendingSyncsCursor(COLUMNS);
                try {
                    if (mOpsByKey.size() != cursor.getCount()) {
                        StringBuilder sb = new StringBuilder();
                        DatabaseUtils.dumpCursor(cursor, sb);
                        sb.append("\n");
                        dump(sb);
                        throw new IllegalStateException("DB size mismatch: "
                                + mOpsByKey .size() + " != " + cursor.getCount() + "\n"
                                + sb.toString());
                    }
                } finally {
                    cursor.close();
                }
            }
        }

        private SyncOperation cursorToOperation(Cursor cursor) {
            byte[] extrasData = cursor.getBlob(COLUMN_EXTRAS);
            Bundle extras;
            Parcel parcel = Parcel.obtain();
            try {
                parcel.unmarshall(extrasData, 0, extrasData.length);
                parcel.setDataPosition(0);
                extras = parcel.readBundle();
            } catch (RuntimeException e) {
                // A RuntimeException is thrown if we were unable to parse the parcel.
                // Create an empty parcel in this case.
                extras = new Bundle();
            } finally {
                parcel.recycle();
            }

            SyncOperation syncOperation = new SyncOperation(
                    cursor.getString(COLUMN_ACCOUNT),
                    cursor.getInt(COLUMN_SOURCE),
                    cursor.getString(COLUMN_AUTHORITY),
                    extras,
                    0 /* delay */);
            syncOperation.rowId = cursor.getLong(COLUMN_ID);
            return syncOperation;
        }
>>>>>>> 54b6cfa... Initial Contribution
    }
}
