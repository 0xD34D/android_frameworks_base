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

import android.os.RemoteException;
import android.os.SystemClock;
<<<<<<< HEAD
import android.os.IBinder;

=======

/**
 * @hide
 */
>>>>>>> 54b6cfa... Initial Contribution
public class SyncContext {
    private ISyncContext mSyncContext;
    private long mLastHeartbeatSendTime;

    private static final long HEARTBEAT_SEND_INTERVAL_IN_MS = 1000;

<<<<<<< HEAD
    /**
     * @hide
     */
=======
>>>>>>> 54b6cfa... Initial Contribution
    public SyncContext(ISyncContext syncContextInterface) {
        mSyncContext = syncContextInterface;
        mLastHeartbeatSendTime = 0;
    }

    /**
     * Call to update the status text for this sync. This internally invokes
     * {@link #updateHeartbeat}, so it also takes the place of a call to that.
     *
     * @param message the current status message for this sync
<<<<<<< HEAD
     *
     * @hide
=======
>>>>>>> 54b6cfa... Initial Contribution
     */
    public void setStatusText(String message) {
        updateHeartbeat();
    }

    /**
     * Call to indicate that the SyncAdapter is making progress. E.g., if this SyncAdapter
     * downloads or sends records to/from the server, this may be called after each record
     * is downloaded or uploaded.
     */
<<<<<<< HEAD
    private void updateHeartbeat() {
=======
    public void updateHeartbeat() {
>>>>>>> 54b6cfa... Initial Contribution
        final long now = SystemClock.elapsedRealtime();
        if (now < mLastHeartbeatSendTime + HEARTBEAT_SEND_INTERVAL_IN_MS) return;
        try {
            mLastHeartbeatSendTime = now;
<<<<<<< HEAD
            if (mSyncContext != null) {
                mSyncContext.sendHeartbeat();
            }
=======
            mSyncContext.sendHeartbeat();
>>>>>>> 54b6cfa... Initial Contribution
        } catch (RemoteException e) {
            // this should never happen
        }
    }

    public void onFinished(SyncResult result) {
        try {
<<<<<<< HEAD
            if (mSyncContext != null) {
                mSyncContext.onFinished(result);
            }
=======
            mSyncContext.onFinished(result);
>>>>>>> 54b6cfa... Initial Contribution
        } catch (RemoteException e) {
            // this should never happen
        }
    }

<<<<<<< HEAD
    public IBinder getSyncContextBinder() {
        return (mSyncContext == null) ? null : mSyncContext.asBinder();
=======
    public ISyncContext getISyncContext() {
        return mSyncContext;
>>>>>>> 54b6cfa... Initial Contribution
    }
}
