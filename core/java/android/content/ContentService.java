/*
 * Copyright (C) 2006 The Android Open Source Project
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
import android.accounts.Account;
import android.database.IContentObserver;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserId;
import android.util.Log;
import android.util.SparseIntArray;
=======
import android.database.IContentObserver;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Config;
import android.util.Log;
>>>>>>> 54b6cfa... Initial Contribution
import android.Manifest;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
<<<<<<< HEAD
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
=======
>>>>>>> 54b6cfa... Initial Contribution

/**
 * {@hide}
 */
<<<<<<< HEAD
public final class ContentService extends IContentService.Stub {
=======
public final class ContentService extends ContentServiceNative {
>>>>>>> 54b6cfa... Initial Contribution
    private static final String TAG = "ContentService";
    private Context mContext;
    private boolean mFactoryTest;
    private final ObserverNode mRootNode = new ObserverNode("");
    private SyncManager mSyncManager = null;
    private final Object mSyncManagerLock = new Object();

    private SyncManager getSyncManager() {
        synchronized(mSyncManagerLock) {
            try {
                // Try to create the SyncManager, return null if it fails (e.g. the disk is full).
                if (mSyncManager == null) mSyncManager = new SyncManager(mContext, mFactoryTest);
            } catch (SQLiteException e) {
                Log.e(TAG, "Can't create SyncManager", e);
            }
            return mSyncManager;
        }
    }

    @Override
    protected synchronized void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        mContext.enforceCallingOrSelfPermission(Manifest.permission.DUMP,
                "caller doesn't have the DUMP permission");

        // This makes it so that future permission checks will be in the context of this
        // process rather than the caller's process. We will restore this before returning.
        long identityToken = clearCallingIdentity();
        try {
            if (mSyncManager == null) {
                pw.println("No SyncManager created!  (Disk full?)");
            } else {
                mSyncManager.dump(fd, pw);
            }
<<<<<<< HEAD
            pw.println();
            pw.println("Observer tree:");
            synchronized (mRootNode) {
                int[] counts = new int[2];
                final SparseIntArray pidCounts = new SparseIntArray();
                mRootNode.dumpLocked(fd, pw, args, "", "  ", counts, pidCounts);
                pw.println();
                ArrayList<Integer> sorted = new ArrayList<Integer>();
                for (int i=0; i<pidCounts.size(); i++) {
                    sorted.add(pidCounts.keyAt(i));
                }
                Collections.sort(sorted, new Comparator<Integer>() {
                    @Override
                    public int compare(Integer lhs, Integer rhs) {
                        int lc = pidCounts.get(lhs);
                        int rc = pidCounts.get(rhs);
                        if (lc < rc) {
                            return 1;
                        } else if (lc > rc) {
                            return -1;
                        }
                        return 0;
                    }

                });
                for (int i=0; i<sorted.size(); i++) {
                    int pid = sorted.get(i);
                    pw.print("  pid "); pw.print(pid); pw.print(": ");
                            pw.print(pidCounts.get(pid)); pw.println(" observers");
                }
                pw.println();
                pw.print(" Total number of nodes: "); pw.println(counts[0]);
                pw.print(" Total number of observers: "); pw.println(counts[1]);
            }
=======
>>>>>>> 54b6cfa... Initial Contribution
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

<<<<<<< HEAD
    @Override
    public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
            throws RemoteException {
        try {
            return super.onTransact(code, data, reply, flags);
        } catch (RuntimeException e) {
            // The content service only throws security exceptions, so let's
            // log all others.
            if (!(e instanceof SecurityException)) {
                Log.e(TAG, "Content Service Crash", e);
            }
            throw e;
        }
    }

    /*package*/ ContentService(Context context, boolean factoryTest) {
        mContext = context;
        mFactoryTest = factoryTest;
    }

    public void systemReady() {
=======
    /*package*/ ContentService(Context context, boolean factoryTest) {
        mContext = context;
        mFactoryTest = factoryTest;
>>>>>>> 54b6cfa... Initial Contribution
        getSyncManager();
    }

    public void registerContentObserver(Uri uri, boolean notifyForDescendents,
            IContentObserver observer) {
        if (observer == null || uri == null) {
            throw new IllegalArgumentException("You must pass a valid uri and observer");
        }
        synchronized (mRootNode) {
<<<<<<< HEAD
            mRootNode.addObserverLocked(uri, observer, notifyForDescendents, mRootNode,
                    Binder.getCallingUid(), Binder.getCallingPid());
            if (false) Log.v(TAG, "Registered observer " + observer + " at " + uri +
=======
            mRootNode.addObserver(uri, observer, notifyForDescendents);
            if (Config.LOGV) Log.v(TAG, "Registered observer " + observer + " at " + uri +
>>>>>>> 54b6cfa... Initial Contribution
                    " with notifyForDescendents " + notifyForDescendents);
        }
    }

    public void unregisterContentObserver(IContentObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("You must pass a valid observer");
        }
        synchronized (mRootNode) {
<<<<<<< HEAD
            mRootNode.removeObserverLocked(observer);
            if (false) Log.v(TAG, "Unregistered observer " + observer);
=======
            mRootNode.removeObserver(observer);
            if (Config.LOGV) Log.v(TAG, "Unregistered observer " + observer);
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    public void notifyChange(Uri uri, IContentObserver observer,
            boolean observerWantsSelfNotifications, boolean syncToNetwork) {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Notifying update of " + uri + " from observer " + observer
                    + ", syncToNetwork " + syncToNetwork);
        }
<<<<<<< HEAD

        int userId = UserId.getCallingUserId();
=======
>>>>>>> 54b6cfa... Initial Contribution
        // This makes it so that future permission checks will be in the context of this
        // process rather than the caller's process. We will restore this before returning.
        long identityToken = clearCallingIdentity();
        try {
            ArrayList<ObserverCall> calls = new ArrayList<ObserverCall>();
            synchronized (mRootNode) {
<<<<<<< HEAD
                mRootNode.collectObserversLocked(uri, 0, observer, observerWantsSelfNotifications,
=======
                mRootNode.collectObservers(uri, 0, observer, observerWantsSelfNotifications,
>>>>>>> 54b6cfa... Initial Contribution
                        calls);
            }
            final int numCalls = calls.size();
            for (int i=0; i<numCalls; i++) {
                ObserverCall oc = calls.get(i);
                try {
<<<<<<< HEAD
                    oc.mObserver.onChange(oc.mSelfChange, uri);
=======
                    oc.mObserver.onChange(oc.mSelfNotify);
>>>>>>> 54b6cfa... Initial Contribution
                    if (Log.isLoggable(TAG, Log.VERBOSE)) {
                        Log.v(TAG, "Notified " + oc.mObserver + " of " + "update at " + uri);
                    }
                } catch (RemoteException ex) {
                    synchronized (mRootNode) {
                        Log.w(TAG, "Found dead observer, removing");
                        IBinder binder = oc.mObserver.asBinder();
                        final ArrayList<ObserverNode.ObserverEntry> list
                                = oc.mNode.mObservers;
                        int numList = list.size();
                        for (int j=0; j<numList; j++) {
                            ObserverNode.ObserverEntry oe = list.get(j);
                            if (oe.observer.asBinder() == binder) {
                                list.remove(j);
                                j--;
                                numList--;
                            }
                        }
                    }
                }
            }
            if (syncToNetwork) {
                SyncManager syncManager = getSyncManager();
<<<<<<< HEAD
                if (syncManager != null) {
                    syncManager.scheduleLocalSync(null /* all accounts */, userId,
                            uri.getAuthority());
                }
=======
                if (syncManager != null) syncManager.scheduleLocalSync(uri);
>>>>>>> 54b6cfa... Initial Contribution
            }
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    /**
     * Hide this class since it is not part of api,
     * but current unittest framework requires it to be public
     * @hide
     *
     */
    public static final class ObserverCall {
        final ObserverNode mNode;
        final IContentObserver mObserver;
<<<<<<< HEAD
        final boolean mSelfChange;

        ObserverCall(ObserverNode node, IContentObserver observer, boolean selfChange) {
            mNode = node;
            mObserver = observer;
            mSelfChange = selfChange;
        }
    }

    public void requestSync(Account account, String authority, Bundle extras) {
        ContentResolver.validateSyncExtrasBundle(extras);
        int userId = UserId.getCallingUserId();

=======
        final boolean mSelfNotify;

        ObserverCall(ObserverNode node, IContentObserver observer,
                boolean selfNotify) {
            mNode = node;
            mObserver = observer;
            mSelfNotify = selfNotify;
        }
    }

    public void startSync(Uri url, Bundle extras) {
        ContentResolver.validateSyncExtrasBundle(extras);
>>>>>>> 54b6cfa... Initial Contribution
        // This makes it so that future permission checks will be in the context of this
        // process rather than the caller's process. We will restore this before returning.
        long identityToken = clearCallingIdentity();
        try {
            SyncManager syncManager = getSyncManager();
<<<<<<< HEAD
            if (syncManager != null) {
                syncManager.scheduleSync(account, userId, authority, extras, 0 /* no delay */,
                        false /* onlyThoseWithUnkownSyncableState */);
            }
=======
            if (syncManager != null) syncManager.startSync(url, extras);
>>>>>>> 54b6cfa... Initial Contribution
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    /**
     * Clear all scheduled sync operations that match the uri and cancel the active sync
<<<<<<< HEAD
     * if they match the authority and account, if they are present.
     * @param account filter the pending and active syncs to cancel using this account
     * @param authority filter the pending and active syncs to cancel using this authority
     */
    public void cancelSync(Account account, String authority) {
        int userId = UserId.getCallingUserId();

        // This makes it so that future permission checks will be in the context of this
        // process rather than the caller's process. We will restore this before returning.
        long identityToken = clearCallingIdentity();
        try {
            SyncManager syncManager = getSyncManager();
            if (syncManager != null) {
                syncManager.clearScheduledSyncOperations(account, userId, authority);
                syncManager.cancelActiveSync(account, userId, authority);
            }
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    /**
     * Get information about the SyncAdapters that are known to the system.
     * @return an array of SyncAdapters that have registered with the system
     */
    public SyncAdapterType[] getSyncAdapterTypes() {
=======
     * if it matches the uri. If the uri is null, clear all scheduled syncs and cancel
     * the active one, if there is one.
     * @param uri Filter on the sync operations to cancel, or all if null.
     */
    public void cancelSync(Uri uri) {
>>>>>>> 54b6cfa... Initial Contribution
        // This makes it so that future permission checks will be in the context of this
        // process rather than the caller's process. We will restore this before returning.
        long identityToken = clearCallingIdentity();
        try {
            SyncManager syncManager = getSyncManager();
<<<<<<< HEAD
            return syncManager.getSyncAdapterTypes();
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public boolean getSyncAutomatically(Account account, String providerName) {
        mContext.enforceCallingOrSelfPermission(Manifest.permission.READ_SYNC_SETTINGS,
                "no permission to read the sync settings");
        int userId = UserId.getCallingUserId();

        long identityToken = clearCallingIdentity();
        try {
            SyncManager syncManager = getSyncManager();
            if (syncManager != null) {
                return syncManager.getSyncStorageEngine().getSyncAutomatically(
                        account, userId, providerName);
            }
        } finally {
            restoreCallingIdentity(identityToken);
        }
        return false;
    }

    public void setSyncAutomatically(Account account, String providerName, boolean sync) {
        mContext.enforceCallingOrSelfPermission(Manifest.permission.WRITE_SYNC_SETTINGS,
                "no permission to write the sync settings");
        int userId = UserId.getCallingUserId();

        long identityToken = clearCallingIdentity();
        try {
            SyncManager syncManager = getSyncManager();
            if (syncManager != null) {
                syncManager.getSyncStorageEngine().setSyncAutomatically(
                        account, userId, providerName, sync);
            }
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public void addPeriodicSync(Account account, String authority, Bundle extras,
            long pollFrequency) {
        mContext.enforceCallingOrSelfPermission(Manifest.permission.WRITE_SYNC_SETTINGS,
                "no permission to write the sync settings");
        int userId = UserId.getCallingUserId();

        long identityToken = clearCallingIdentity();
        try {
            getSyncManager().getSyncStorageEngine().addPeriodicSync(
                    account, userId, authority, extras, pollFrequency);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public void removePeriodicSync(Account account, String authority, Bundle extras) {
        mContext.enforceCallingOrSelfPermission(Manifest.permission.WRITE_SYNC_SETTINGS,
                "no permission to write the sync settings");
        int userId = UserId.getCallingUserId();

        long identityToken = clearCallingIdentity();
        try {
            getSyncManager().getSyncStorageEngine().removePeriodicSync(account, userId, authority,
                    extras);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public List<PeriodicSync> getPeriodicSyncs(Account account, String providerName) {
        mContext.enforceCallingOrSelfPermission(Manifest.permission.READ_SYNC_SETTINGS,
                "no permission to read the sync settings");
        int userId = UserId.getCallingUserId();

        long identityToken = clearCallingIdentity();
        try {
            return getSyncManager().getSyncStorageEngine().getPeriodicSyncs(
                    account, userId, providerName);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public int getIsSyncable(Account account, String providerName) {
        mContext.enforceCallingOrSelfPermission(Manifest.permission.READ_SYNC_SETTINGS,
                "no permission to read the sync settings");
        int userId = UserId.getCallingUserId();

        long identityToken = clearCallingIdentity();
        try {
            SyncManager syncManager = getSyncManager();
            if (syncManager != null) {
                return syncManager.getSyncStorageEngine().getIsSyncable(
                        account, userId, providerName);
            }
        } finally {
            restoreCallingIdentity(identityToken);
        }
        return -1;
    }

    public void setIsSyncable(Account account, String providerName, int syncable) {
        mContext.enforceCallingOrSelfPermission(Manifest.permission.WRITE_SYNC_SETTINGS,
                "no permission to write the sync settings");
        int userId = UserId.getCallingUserId();

        long identityToken = clearCallingIdentity();
        try {
            SyncManager syncManager = getSyncManager();
            if (syncManager != null) {
                syncManager.getSyncStorageEngine().setIsSyncable(
                        account, userId, providerName, syncable);
            }
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public boolean getMasterSyncAutomatically() {
        mContext.enforceCallingOrSelfPermission(Manifest.permission.READ_SYNC_SETTINGS,
                "no permission to read the sync settings");
        int userId = UserId.getCallingUserId();

        long identityToken = clearCallingIdentity();
        try {
            SyncManager syncManager = getSyncManager();
            if (syncManager != null) {
                return syncManager.getSyncStorageEngine().getMasterSyncAutomatically(userId);
            }
        } finally {
            restoreCallingIdentity(identityToken);
        }
        return false;
    }

    public void setMasterSyncAutomatically(boolean flag) {
        mContext.enforceCallingOrSelfPermission(Manifest.permission.WRITE_SYNC_SETTINGS,
                "no permission to write the sync settings");
        int userId = UserId.getCallingUserId();

        long identityToken = clearCallingIdentity();
        try {
            SyncManager syncManager = getSyncManager();
            if (syncManager != null) {
                syncManager.getSyncStorageEngine().setMasterSyncAutomatically(flag, userId);
            }
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public boolean isSyncActive(Account account, String authority) {
        mContext.enforceCallingOrSelfPermission(Manifest.permission.READ_SYNC_STATS,
                "no permission to read the sync stats");
        int userId = UserId.getCallingUserId();

        long identityToken = clearCallingIdentity();
        try {
            SyncManager syncManager = getSyncManager();
            if (syncManager != null) {
                return syncManager.getSyncStorageEngine().isSyncActive(
                        account, userId, authority);
=======
            if (syncManager != null) {
                syncManager.clearScheduledSyncOperations(uri);
                syncManager.cancelActiveSync(uri);
>>>>>>> 54b6cfa... Initial Contribution
            }
        } finally {
            restoreCallingIdentity(identityToken);
        }
<<<<<<< HEAD
        return false;
    }

    public List<SyncInfo> getCurrentSyncs() {
        mContext.enforceCallingOrSelfPermission(Manifest.permission.READ_SYNC_STATS,
                "no permission to read the sync stats");
        int userId = UserId.getCallingUserId();

        long identityToken = clearCallingIdentity();
        try {
            return getSyncManager().getSyncStorageEngine().getCurrentSyncs(userId);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public SyncStatusInfo getSyncStatus(Account account, String authority) {
        mContext.enforceCallingOrSelfPermission(Manifest.permission.READ_SYNC_STATS,
                "no permission to read the sync stats");
        int userId = UserId.getCallingUserId();

        long identityToken = clearCallingIdentity();
        try {
            SyncManager syncManager = getSyncManager();
            if (syncManager != null) {
                return syncManager.getSyncStorageEngine().getStatusByAccountAndAuthority(
                        account, userId, authority);
            }
        } finally {
            restoreCallingIdentity(identityToken);
        }
        return null;
    }

    public boolean isSyncPending(Account account, String authority) {
        mContext.enforceCallingOrSelfPermission(Manifest.permission.READ_SYNC_STATS,
                "no permission to read the sync stats");
        int userId = UserId.getCallingUserId();

        long identityToken = clearCallingIdentity();
        try {
            SyncManager syncManager = getSyncManager();
            if (syncManager != null) {
                return syncManager.getSyncStorageEngine().isSyncPending(account, userId, authority);
            }
        } finally {
            restoreCallingIdentity(identityToken);
        }
        return false;
    }

    public void addStatusChangeListener(int mask, ISyncStatusObserver callback) {
        long identityToken = clearCallingIdentity();
        try {
            SyncManager syncManager = getSyncManager();
            if (syncManager != null && callback != null) {
                syncManager.getSyncStorageEngine().addStatusChangeListener(mask, callback);
            }
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public void removeStatusChangeListener(ISyncStatusObserver callback) {
        long identityToken = clearCallingIdentity();
        try {
            SyncManager syncManager = getSyncManager();
            if (syncManager != null && callback != null) {
                syncManager.getSyncStorageEngine().removeStatusChangeListener(callback);
            }
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public static ContentService main(Context context, boolean factoryTest) {
        ContentService service = new ContentService(context, factoryTest);
        ServiceManager.addService(ContentResolver.CONTENT_SERVICE_NAME, service);
=======
    }

    public static IContentService main(Context context, boolean factoryTest) {
        ContentService service = new ContentService(context, factoryTest);
        ServiceManager.addService("content", service);
>>>>>>> 54b6cfa... Initial Contribution
        return service;
    }

    /**
     * Hide this class since it is not part of api,
     * but current unittest framework requires it to be public
     * @hide
     */
    public static final class ObserverNode {
        private class ObserverEntry implements IBinder.DeathRecipient {
<<<<<<< HEAD
            public final IContentObserver observer;
            public final int uid;
            public final int pid;
            public final boolean notifyForDescendents;
            private final Object observersLock;

            public ObserverEntry(IContentObserver o, boolean n, Object observersLock,
                    int _uid, int _pid) {
                this.observersLock = observersLock;
                observer = o;
                uid = _uid;
                pid = _pid;
=======
            public IContentObserver observer;
            public boolean notifyForDescendents;

            public ObserverEntry(IContentObserver o, boolean n) {
                observer = o;
>>>>>>> 54b6cfa... Initial Contribution
                notifyForDescendents = n;
                try {
                    observer.asBinder().linkToDeath(this, 0);
                } catch (RemoteException e) {
                    binderDied();
                }
            }

            public void binderDied() {
<<<<<<< HEAD
                synchronized (observersLock) {
                    removeObserverLocked(observer);
                }
            }

            public void dumpLocked(FileDescriptor fd, PrintWriter pw, String[] args,
                    String name, String prefix, SparseIntArray pidCounts) {
                pidCounts.put(pid, pidCounts.get(pid)+1);
                pw.print(prefix); pw.print(name); pw.print(": pid=");
                        pw.print(pid); pw.print(" uid=");
                        pw.print(uid); pw.print(" target=");
                        pw.println(Integer.toHexString(System.identityHashCode(
                                observer != null ? observer.asBinder() : null)));
=======
                removeObserver(observer);
>>>>>>> 54b6cfa... Initial Contribution
            }
        }

        public static final int INSERT_TYPE = 0;
        public static final int UPDATE_TYPE = 1;
        public static final int DELETE_TYPE = 2;

        private String mName;
        private ArrayList<ObserverNode> mChildren = new ArrayList<ObserverNode>();
        private ArrayList<ObserverEntry> mObservers = new ArrayList<ObserverEntry>();

        public ObserverNode(String name) {
            mName = name;
        }

<<<<<<< HEAD
        public void dumpLocked(FileDescriptor fd, PrintWriter pw, String[] args,
                String name, String prefix, int[] counts, SparseIntArray pidCounts) {
            String innerName = null;
            if (mObservers.size() > 0) {
                if ("".equals(name)) {
                    innerName = mName;
                } else {
                    innerName = name + "/" + mName;
                }
                for (int i=0; i<mObservers.size(); i++) {
                    counts[1]++;
                    mObservers.get(i).dumpLocked(fd, pw, args, innerName, prefix,
                            pidCounts);
                }
            }
            if (mChildren.size() > 0) {
                if (innerName == null) {
                    if ("".equals(name)) {
                        innerName = mName;
                    } else {
                        innerName = name + "/" + mName;
                    }
                }
                for (int i=0; i<mChildren.size(); i++) {
                    counts[0]++;
                    mChildren.get(i).dumpLocked(fd, pw, args, innerName, prefix,
                            counts, pidCounts);
                }
            }
        }

=======
>>>>>>> 54b6cfa... Initial Contribution
        private String getUriSegment(Uri uri, int index) {
            if (uri != null) {
                if (index == 0) {
                    return uri.getAuthority();
                } else {
                    return uri.getPathSegments().get(index - 1);
                }
            } else {
                return null;
            }
        }

        private int countUriSegments(Uri uri) {
            if (uri == null) {
                return 0;
            }
            return uri.getPathSegments().size() + 1;
        }

<<<<<<< HEAD
        public void addObserverLocked(Uri uri, IContentObserver observer,
                boolean notifyForDescendents, Object observersLock, int uid, int pid) {
            addObserverLocked(uri, 0, observer, notifyForDescendents, observersLock, uid, pid);
        }

        private void addObserverLocked(Uri uri, int index, IContentObserver observer,
                boolean notifyForDescendents, Object observersLock, int uid, int pid) {
            // If this is the leaf node add the observer
            if (index == countUriSegments(uri)) {
                mObservers.add(new ObserverEntry(observer, notifyForDescendents, observersLock,
                        uid, pid));
=======
        public void addObserver(Uri uri, IContentObserver observer, boolean notifyForDescendents) {
            addObserver(uri, 0, observer, notifyForDescendents);
        }

        private void addObserver(Uri uri, int index, IContentObserver observer,
                boolean notifyForDescendents) {

            // If this is the leaf node add the observer
            if (index == countUriSegments(uri)) {
                mObservers.add(new ObserverEntry(observer, notifyForDescendents));
>>>>>>> 54b6cfa... Initial Contribution
                return;
            }

            // Look to see if the proper child already exists
            String segment = getUriSegment(uri, index);
<<<<<<< HEAD
            if (segment == null) {
                throw new IllegalArgumentException("Invalid Uri (" + uri + ") used for observer");
            }
=======
>>>>>>> 54b6cfa... Initial Contribution
            int N = mChildren.size();
            for (int i = 0; i < N; i++) {
                ObserverNode node = mChildren.get(i);
                if (node.mName.equals(segment)) {
<<<<<<< HEAD
                    node.addObserverLocked(uri, index + 1, observer, notifyForDescendents,
                            observersLock, uid, pid);
=======
                    node.addObserver(uri, index + 1, observer, notifyForDescendents);
>>>>>>> 54b6cfa... Initial Contribution
                    return;
                }
            }

            // No child found, create one
            ObserverNode node = new ObserverNode(segment);
            mChildren.add(node);
<<<<<<< HEAD
            node.addObserverLocked(uri, index + 1, observer, notifyForDescendents,
                    observersLock, uid, pid);
        }

        public boolean removeObserverLocked(IContentObserver observer) {
            int size = mChildren.size();
            for (int i = 0; i < size; i++) {
                boolean empty = mChildren.get(i).removeObserverLocked(observer);
=======
            node.addObserver(uri, index + 1, observer, notifyForDescendents);
        }

        public boolean removeObserver(IContentObserver observer) {
            int size = mChildren.size();
            for (int i = 0; i < size; i++) {
                boolean empty = mChildren.get(i).removeObserver(observer);
>>>>>>> 54b6cfa... Initial Contribution
                if (empty) {
                    mChildren.remove(i);
                    i--;
                    size--;
                }
            }

            IBinder observerBinder = observer.asBinder();
            size = mObservers.size();
            for (int i = 0; i < size; i++) {
                ObserverEntry entry = mObservers.get(i);
                if (entry.observer.asBinder() == observerBinder) {
                    mObservers.remove(i);
                    // We no longer need to listen for death notifications. Remove it.
                    observerBinder.unlinkToDeath(entry, 0);
                    break;
                }
            }

            if (mChildren.size() == 0 && mObservers.size() == 0) {
                return true;
            }
            return false;
        }

<<<<<<< HEAD
        private void collectMyObserversLocked(boolean leaf, IContentObserver observer,
                boolean observerWantsSelfNotifications, ArrayList<ObserverCall> calls) {
=======
        private void collectMyObservers(Uri uri,
                boolean leaf, IContentObserver observer, boolean selfNotify,
                ArrayList<ObserverCall> calls)
        {
>>>>>>> 54b6cfa... Initial Contribution
            int N = mObservers.size();
            IBinder observerBinder = observer == null ? null : observer.asBinder();
            for (int i = 0; i < N; i++) {
                ObserverEntry entry = mObservers.get(i);

                // Don't notify the observer if it sent the notification and isn't interesed
                // in self notifications
<<<<<<< HEAD
                boolean selfChange = (entry.observer.asBinder() == observerBinder);
                if (selfChange && !observerWantsSelfNotifications) {
=======
                if (entry.observer.asBinder() == observerBinder && !selfNotify) {
>>>>>>> 54b6cfa... Initial Contribution
                    continue;
                }

                // Make sure the observer is interested in the notification
                if (leaf || (!leaf && entry.notifyForDescendents)) {
<<<<<<< HEAD
                    calls.add(new ObserverCall(this, entry.observer, selfChange));
=======
                    calls.add(new ObserverCall(this, entry.observer, selfNotify));
>>>>>>> 54b6cfa... Initial Contribution
                }
            }
        }

<<<<<<< HEAD
        public void collectObserversLocked(Uri uri, int index, IContentObserver observer,
                boolean observerWantsSelfNotifications, ArrayList<ObserverCall> calls) {
=======
        public void collectObservers(Uri uri, int index, IContentObserver observer,
                boolean selfNotify, ArrayList<ObserverCall> calls) {
>>>>>>> 54b6cfa... Initial Contribution
            String segment = null;
            int segmentCount = countUriSegments(uri);
            if (index >= segmentCount) {
                // This is the leaf node, notify all observers
<<<<<<< HEAD
                collectMyObserversLocked(true, observer, observerWantsSelfNotifications, calls);
            } else if (index < segmentCount){
                segment = getUriSegment(uri, index);
                // Notify any observers at this level who are interested in descendents
                collectMyObserversLocked(false, observer, observerWantsSelfNotifications, calls);
=======
                collectMyObservers(uri, true, observer, selfNotify, calls);
            } else if (index < segmentCount){
                segment = getUriSegment(uri, index);
                // Notify any observers at this level who are interested in descendents
                collectMyObservers(uri, false, observer, selfNotify, calls);
>>>>>>> 54b6cfa... Initial Contribution
            }

            int N = mChildren.size();
            for (int i = 0; i < N; i++) {
                ObserverNode node = mChildren.get(i);
                if (segment == null || node.mName.equals(segment)) {
                    // We found the child,
<<<<<<< HEAD
                    node.collectObserversLocked(uri, index + 1,
                            observer, observerWantsSelfNotifications, calls);
=======
                    node.collectObservers(uri, index + 1, observer, selfNotify, calls);
>>>>>>> 54b6cfa... Initial Contribution
                    if (segment != null) {
                        break;
                    }
                }
            }
        }
    }
}
