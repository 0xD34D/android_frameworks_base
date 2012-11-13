/*
 * Copyright (C) 2007 The Android Open Source Project
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

package com.android.server;

<<<<<<< HEAD
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.LinkCapabilities;
import android.net.LinkProperties;
import android.os.Binder;
import android.os.Bundle;
=======
import android.app.ActivityManagerNative;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Binder;
>>>>>>> 54b6cfa... Initial Contribution
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
<<<<<<< HEAD
import android.telephony.SignalStrength;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Slog;
=======
import android.telephony.TelephonyManager;
import android.util.Log;
>>>>>>> 54b6cfa... Initial Contribution

import java.util.ArrayList;
import java.io.FileDescriptor;
import java.io.PrintWriter;
<<<<<<< HEAD
import java.net.NetworkInterface;

import com.android.internal.app.IBatteryStats;
=======

>>>>>>> 54b6cfa... Initial Contribution
import com.android.internal.telephony.ITelephonyRegistry;
import com.android.internal.telephony.IPhoneStateListener;
import com.android.internal.telephony.DefaultPhoneNotifier;
import com.android.internal.telephony.Phone;
<<<<<<< HEAD
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.TelephonyIntents;
import com.android.server.am.BatteryStatsService;

/**
 * Since phone process can be restarted, this class provides a centralized place
 * that applications can register and be called back from.
 */
class TelephonyRegistry extends ITelephonyRegistry.Stub {
    private static final String TAG = "TelephonyRegistry";
    private static final boolean DBG = false;

    private static class Record {
        String pkgForDebug;

        IBinder binder;

        IPhoneStateListener callback;

        int events;
    }

    private final Context mContext;

    // access should be inside synchronized (mRecords) for these two fields
    private final ArrayList<IBinder> mRemoveList = new ArrayList<IBinder>();
    private final ArrayList<Record> mRecords = new ArrayList<Record>();

    private final IBatteryStats mBatteryStats;

    private int mCallState = TelephonyManager.CALL_STATE_IDLE;

    private String mCallIncomingNumber = "";

    private ServiceState mServiceState = new ServiceState();

    private SignalStrength mSignalStrength = new SignalStrength();

    private boolean mMessageWaiting = false;

    private boolean mCallForwarding = false;

    private int mDataActivity = TelephonyManager.DATA_ACTIVITY_NONE;

    private int mDataConnectionState = TelephonyManager.DATA_UNKNOWN;

    private boolean mDataConnectionPossible = false;

    private String mDataConnectionReason = "";

    private String mDataConnectionApn = "";

    private ArrayList<String> mConnectedApns;

    private LinkProperties mDataConnectionLinkProperties;

    private LinkCapabilities mDataConnectionLinkCapabilities;

    private Bundle mCellLocation = new Bundle();

    private int mDataConnectionNetworkType;

    private int mOtaspMode = ServiceStateTracker.OTASP_UNKNOWN;

    private CellInfo mCellInfo = null;

    static final int PHONE_STATE_PERMISSION_MASK =
                PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR |
                PhoneStateListener.LISTEN_CALL_STATE |
                PhoneStateListener.LISTEN_DATA_ACTIVITY |
                PhoneStateListener.LISTEN_DATA_CONNECTION_STATE |
                PhoneStateListener.LISTEN_MESSAGE_WAITING_INDICATOR;

    // we keep a copy of all of the state so we can send it out when folks
    // register for it
    //
    // In these calls we call with the lock held. This is safe becasuse remote
    // calls go through a oneway interface and local calls going through a
    // handler before they get to app code.

    TelephonyRegistry(Context context) {
        CellLocation  location = CellLocation.getEmpty();

        // Note that location can be null for non-phone builds like
        // like the generic one.
        if (location != null) {
            location.fillInNotifierBundle(mCellLocation);
        }
        mContext = context;
        mBatteryStats = BatteryStatsService.getService();
        mConnectedApns = new ArrayList<String>();
=======
import com.android.internal.telephony.PhoneStateIntentReceiver;
import com.android.internal.telephony.TelephonyIntents;

import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;


/**
 * Since phone process can be restarted, this class provides a centralized
 * place that applications can register and be called back from.
 */
class TelephonyRegistry extends ITelephonyRegistry.Stub {
    private static final String TAG = "TelephonyRegistry";

    private static class Record {
        String pkgForDebug;
        IBinder binder;
        IPhoneStateListener callback;
        int events;
    }

    private Context mContext;
    private ArrayList<Record> mRecords = new ArrayList();

    private int mCallState = TelephonyManager.CALL_STATE_IDLE;
    private String mCallIncomingNumber = "";
    private ServiceState mServiceState = new ServiceState();
    private int mSignalStrength = -1;
    private boolean mMessageWaiting = false;
    private boolean mCallForwarding = false;
    private int mDataActivity = TelephonyManager.DATA_ACTIVITY_NONE;
    private int mDataConnectionState = TelephonyManager.DATA_CONNECTED;
    private boolean mDataConnectionPossible = false;
    private String mDataConnectionReason = "";
    private String mDataConnectionApn = "";
    private String mDataConnectionInterfaceName = "";
    private Bundle mCellLocation = new Bundle();

    // we keep a copy of all of the sate so we can send it out when folks register for it
    //
    // In these calls we call with the lock held.  This is safe becasuse remote
    // calls go through a oneway interface and local calls going through a handler before
    // they get to app code.

    TelephonyRegistry(Context context) {
        CellLocation.getEmpty().fillInNotifierBundle(mCellLocation);
        mContext = context;
>>>>>>> 54b6cfa... Initial Contribution
    }

    public void listen(String pkgForDebug, IPhoneStateListener callback, int events,
            boolean notifyNow) {
<<<<<<< HEAD
        // Slog.d(TAG, "listen pkg=" + pkgForDebug + " events=0x" +
        // Integer.toHexString(events));
        if (events != 0) {
            /* Checks permission and throws Security exception */
            checkListenerPermission(events);
=======
        //Log.d(TAG, "listen pkg=" + pkgForDebug + " events=0x" + Integer.toHexString(events));
        if (events != 0) {
            // check permissions
            if ((events & PhoneStateListener.LISTEN_CELL_LOCATION) != 0) {
                mContext.enforceCallingOrSelfPermission(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION, null);

            }
>>>>>>> 54b6cfa... Initial Contribution

            synchronized (mRecords) {
                // register
                Record r = null;
                find_and_add: {
                    IBinder b = callback.asBinder();
                    final int N = mRecords.size();
<<<<<<< HEAD
                    for (int i = 0; i < N; i++) {
=======
                    for (int i=0; i<N; i++) {
>>>>>>> 54b6cfa... Initial Contribution
                        r = mRecords.get(i);
                        if (b == r.binder) {
                            break find_and_add;
                        }
                    }
                    r = new Record();
                    r.binder = b;
                    r.callback = callback;
                    r.pkgForDebug = pkgForDebug;
                    mRecords.add(r);
                }
                int send = events & (events ^ r.events);
                r.events = events;
                if (notifyNow) {
                    if ((events & PhoneStateListener.LISTEN_SERVICE_STATE) != 0) {
<<<<<<< HEAD
                        try {
                            r.callback.onServiceStateChanged(new ServiceState(mServiceState));
                        } catch (RemoteException ex) {
                            remove(r.binder);
                        }
                    }
                    if ((events & PhoneStateListener.LISTEN_SIGNAL_STRENGTH) != 0) {
                        try {
                            int gsmSignalStrength = mSignalStrength.getGsmSignalStrength();
                            r.callback.onSignalStrengthChanged((gsmSignalStrength == 99 ? -1
                                    : gsmSignalStrength));
=======
                        sendServiceState(r, mServiceState);
                    }
                    if ((events & PhoneStateListener.LISTEN_SIGNAL_STRENGTH) != 0) {
                        try {
                            r.callback.onSignalStrengthChanged(mSignalStrength);
>>>>>>> 54b6cfa... Initial Contribution
                        } catch (RemoteException ex) {
                            remove(r.binder);
                        }
                    }
                    if ((events & PhoneStateListener.LISTEN_MESSAGE_WAITING_INDICATOR) != 0) {
                        try {
                            r.callback.onMessageWaitingIndicatorChanged(mMessageWaiting);
                        } catch (RemoteException ex) {
                            remove(r.binder);
                        }
                    }
                    if ((events & PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR) != 0) {
                        try {
                            r.callback.onCallForwardingIndicatorChanged(mCallForwarding);
                        } catch (RemoteException ex) {
                            remove(r.binder);
                        }
                    }
                    if ((events & PhoneStateListener.LISTEN_CELL_LOCATION) != 0) {
<<<<<<< HEAD
                        try {
                            r.callback.onCellLocationChanged(new Bundle(mCellLocation));
                        } catch (RemoteException ex) {
                            remove(r.binder);
                        }
=======
                        sendCellLocation(r, mCellLocation);
>>>>>>> 54b6cfa... Initial Contribution
                    }
                    if ((events & PhoneStateListener.LISTEN_CALL_STATE) != 0) {
                        try {
                            r.callback.onCallStateChanged(mCallState, mCallIncomingNumber);
                        } catch (RemoteException ex) {
                            remove(r.binder);
                        }
                    }
                    if ((events & PhoneStateListener.LISTEN_DATA_CONNECTION_STATE) != 0) {
                        try {
<<<<<<< HEAD
                            r.callback.onDataConnectionStateChanged(mDataConnectionState,
                                mDataConnectionNetworkType);
=======
                            r.callback.onDataConnectionStateChanged(mDataConnectionState);
>>>>>>> 54b6cfa... Initial Contribution
                        } catch (RemoteException ex) {
                            remove(r.binder);
                        }
                    }
                    if ((events & PhoneStateListener.LISTEN_DATA_ACTIVITY) != 0) {
                        try {
                            r.callback.onDataActivity(mDataActivity);
                        } catch (RemoteException ex) {
                            remove(r.binder);
                        }
                    }
<<<<<<< HEAD
                    if ((events & PhoneStateListener.LISTEN_SIGNAL_STRENGTHS) != 0) {
                        try {
                            r.callback.onSignalStrengthsChanged(mSignalStrength);
                        } catch (RemoteException ex) {
                            remove(r.binder);
                        }
                    }
                    if ((events & PhoneStateListener.LISTEN_OTASP_CHANGED) != 0) {
                        try {
                            r.callback.onOtaspChanged(mOtaspMode);
                        } catch (RemoteException ex) {
                            remove(r.binder);
                        }
                    }
                    if ((events & PhoneStateListener.LISTEN_CELL_INFO) != 0) {
                        try {
                            r.callback.onCellInfoChanged(new CellInfo(mCellInfo));
                        } catch (RemoteException ex) {
                            remove(r.binder);
                        }
                    }
=======
>>>>>>> 54b6cfa... Initial Contribution
                }
            }
        } else {
            remove(callback.asBinder());
        }
    }

    private void remove(IBinder binder) {
        synchronized (mRecords) {
<<<<<<< HEAD
            final int recordCount = mRecords.size();
            for (int i = 0; i < recordCount; i++) {
=======
            final int N = mRecords.size();
            for (int i=0; i<N; i++) {
>>>>>>> 54b6cfa... Initial Contribution
                if (mRecords.get(i).binder == binder) {
                    mRecords.remove(i);
                    return;
                }
            }
        }
    }

    public void notifyCallState(int state, String incomingNumber) {
<<<<<<< HEAD
        if (!checkNotifyPermission("notifyCallState()")) {
            return;
        }
        synchronized (mRecords) {
            mCallState = state;
            mCallIncomingNumber = incomingNumber;
            for (Record r : mRecords) {
=======
        synchronized (mRecords) {
            mCallState = state;
            mCallIncomingNumber = incomingNumber;
            final int N = mRecords.size();
            for (int i=N-1; i>=0; i--) {
                Record r = mRecords.get(i);
>>>>>>> 54b6cfa... Initial Contribution
                if ((r.events & PhoneStateListener.LISTEN_CALL_STATE) != 0) {
                    try {
                        r.callback.onCallStateChanged(state, incomingNumber);
                    } catch (RemoteException ex) {
<<<<<<< HEAD
                        mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
=======
                        remove(r.binder);
                    }
                }
            }
>>>>>>> 54b6cfa... Initial Contribution
        }
        broadcastCallStateChanged(state, incomingNumber);
    }

    public void notifyServiceState(ServiceState state) {
<<<<<<< HEAD
        if (!checkNotifyPermission("notifyServiceState()")){
            return;
        }
        synchronized (mRecords) {
            mServiceState = state;
            for (Record r : mRecords) {
                if ((r.events & PhoneStateListener.LISTEN_SERVICE_STATE) != 0) {
                    try {
                        r.callback.onServiceStateChanged(new ServiceState(state));
                    } catch (RemoteException ex) {
                        mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
=======
        synchronized (mRecords) {
            mServiceState = state;
            final int N = mRecords.size();
            for (int i=N-1; i>=0; i--) {
                Record r = mRecords.get(i);
                if ((r.events & PhoneStateListener.LISTEN_SERVICE_STATE) != 0) {
                    sendServiceState(r, state);
                }
            }
>>>>>>> 54b6cfa... Initial Contribution
        }
        broadcastServiceStateChanged(state);
    }

<<<<<<< HEAD
    public void notifySignalStrength(SignalStrength signalStrength) {
        if (!checkNotifyPermission("notifySignalStrength()")) {
            return;
        }
        synchronized (mRecords) {
            mSignalStrength = signalStrength;
            for (Record r : mRecords) {
                if ((r.events & PhoneStateListener.LISTEN_SIGNAL_STRENGTHS) != 0) {
                    try {
                        r.callback.onSignalStrengthsChanged(new SignalStrength(signalStrength));
                    } catch (RemoteException ex) {
                        mRemoveList.add(r.binder);
                    }
                }
                if ((r.events & PhoneStateListener.LISTEN_SIGNAL_STRENGTH) != 0) {
                    try {
                        int gsmSignalStrength = signalStrength.getGsmSignalStrength();
                        r.callback.onSignalStrengthChanged((gsmSignalStrength == 99 ? -1
                                : gsmSignalStrength));
                    } catch (RemoteException ex) {
                        mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
        broadcastSignalStrengthChanged(signalStrength);
    }

    public void notifyCellInfo(CellInfo cellInfo) {
        if (!checkNotifyPermission("notifyCellInfo()")) {
            return;
        }

        synchronized (mRecords) {
            mCellInfo = cellInfo;
            for (Record r : mRecords) {
                if ((r.events & PhoneStateListener.LISTEN_CELL_INFO) != 0) {
                    try {
                        r.callback.onCellInfoChanged(new CellInfo(cellInfo));
                    } catch (RemoteException ex) {
                        mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    public void notifyMessageWaitingChanged(boolean mwi) {
        if (!checkNotifyPermission("notifyMessageWaitingChanged()")) {
            return;
        }
        synchronized (mRecords) {
            mMessageWaiting = mwi;
            for (Record r : mRecords) {
=======
    public void notifySignalStrength(int signalStrengthASU) {
        synchronized (mRecords) {
            mSignalStrength = signalStrengthASU;
            final int N = mRecords.size();
            for (int i=N-1; i>=0; i--) {
                Record r = mRecords.get(i);
                if ((r.events & PhoneStateListener.LISTEN_SIGNAL_STRENGTH) != 0) {
                    try {
                        r.callback.onSignalStrengthChanged(signalStrengthASU);
                    } catch (RemoteException ex) {
                        remove(r.binder);
                    }
                }
            }
        }
        broadcastSignalStrengthChanged(signalStrengthASU);
    }

    public void notifyMessageWaitingChanged(boolean mwi) {
        synchronized (mRecords) {
            mMessageWaiting = mwi;
            final int N = mRecords.size();
            for (int i=N-1; i>=0; i--) {
                Record r = mRecords.get(i);
>>>>>>> 54b6cfa... Initial Contribution
                if ((r.events & PhoneStateListener.LISTEN_MESSAGE_WAITING_INDICATOR) != 0) {
                    try {
                        r.callback.onMessageWaitingIndicatorChanged(mwi);
                    } catch (RemoteException ex) {
<<<<<<< HEAD
                        mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
=======
                        remove(r.binder);
                    }
                }
            }
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    public void notifyCallForwardingChanged(boolean cfi) {
<<<<<<< HEAD
        if (!checkNotifyPermission("notifyCallForwardingChanged()")) {
            return;
        }
        synchronized (mRecords) {
            mCallForwarding = cfi;
            for (Record r : mRecords) {
=======
        synchronized (mRecords) {
            mCallForwarding = cfi;
            final int N = mRecords.size();
            for (int i=N-1; i>=0; i--) {
                Record r = mRecords.get(i);
>>>>>>> 54b6cfa... Initial Contribution
                if ((r.events & PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR) != 0) {
                    try {
                        r.callback.onCallForwardingIndicatorChanged(cfi);
                    } catch (RemoteException ex) {
<<<<<<< HEAD
                        mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
=======
                        remove(r.binder);
                    }
                }
            }
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    public void notifyDataActivity(int state) {
<<<<<<< HEAD
        if (!checkNotifyPermission("notifyDataActivity()" )) {
            return;
        }
        synchronized (mRecords) {
            mDataActivity = state;
            for (Record r : mRecords) {
=======
        synchronized (mRecords) {
            mDataActivity = state;
            final int N = mRecords.size();
            for (int i=N-1; i>=0; i--) {
                Record r = mRecords.get(i);
>>>>>>> 54b6cfa... Initial Contribution
                if ((r.events & PhoneStateListener.LISTEN_DATA_ACTIVITY) != 0) {
                    try {
                        r.callback.onDataActivity(state);
                    } catch (RemoteException ex) {
<<<<<<< HEAD
                        mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    public void notifyDataConnection(int state, boolean isDataConnectivityPossible,
            String reason, String apn, String apnType, LinkProperties linkProperties,
            LinkCapabilities linkCapabilities, int networkType, boolean roaming) {
        if (!checkNotifyPermission("notifyDataConnection()" )) {
            return;
        }
        if (DBG) {
            Slog.i(TAG, "notifyDataConnection: state=" + state + " isDataConnectivityPossible="
                + isDataConnectivityPossible + " reason='" + reason
                + "' apn='" + apn + "' apnType=" + apnType + " networkType=" + networkType);
        }
        synchronized (mRecords) {
            boolean modified = false;
            if (state == TelephonyManager.DATA_CONNECTED) {
                if (!mConnectedApns.contains(apnType)) {
                    mConnectedApns.add(apnType);
                    if (mDataConnectionState != state) {
                        mDataConnectionState = state;
                        modified = true;
                    }
                }
            } else {
                if (mConnectedApns.remove(apnType)) {
                    if (mConnectedApns.isEmpty()) {
                        mDataConnectionState = state;
                        modified = true;
                    } else {
                        // leave mDataConnectionState as is and
                        // send out the new status for the APN in question.
                    }
                }
            }
            mDataConnectionPossible = isDataConnectivityPossible;
            mDataConnectionReason = reason;
            mDataConnectionLinkProperties = linkProperties;
            mDataConnectionLinkCapabilities = linkCapabilities;
            if (mDataConnectionNetworkType != networkType) {
                mDataConnectionNetworkType = networkType;
                // need to tell registered listeners about the new network type
                modified = true;
            }
            if (modified) {
                if (DBG) {
                    Slog.d(TAG, "onDataConnectionStateChanged(" + mDataConnectionState
                        + ", " + mDataConnectionNetworkType + ")");
                }
                for (Record r : mRecords) {
                    if ((r.events & PhoneStateListener.LISTEN_DATA_CONNECTION_STATE) != 0) {
                        try {
                            r.callback.onDataConnectionStateChanged(mDataConnectionState,
                                    mDataConnectionNetworkType);
                        } catch (RemoteException ex) {
                            mRemoveList.add(r.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
        broadcastDataConnectionStateChanged(state, isDataConnectivityPossible, reason, apn,
                apnType, linkProperties, linkCapabilities, roaming);
    }

    public void notifyDataConnectionFailed(String reason, String apnType) {
        if (!checkNotifyPermission("notifyDataConnectionFailed()")) {
            return;
        }
        /*
         * This is commented out because there is no onDataConnectionFailed callback
         * in PhoneStateListener. There should be.
=======
                        remove(r.binder);
                    }
                }
            }
        }
    }

    public void notifyDataConnection(int state, boolean isDataConnectivityPissible,
            String reason, String apn, String interfaceName) {
        synchronized (mRecords) {
            mDataConnectionState = state;
            mDataConnectionPossible = isDataConnectivityPissible;
            mDataConnectionReason = reason;
            mDataConnectionApn = apn;
            mDataConnectionInterfaceName = interfaceName;
            final int N = mRecords.size();
            for (int i=N-1; i>=0; i--) {
                Record r = mRecords.get(i);
                if ((r.events & PhoneStateListener.LISTEN_DATA_CONNECTION_STATE) != 0) {
                    try {
                        r.callback.onDataConnectionStateChanged(state);
                    } catch (RemoteException ex) {
                        remove(r.binder);
                    }
                }
            }
        }
        broadcastDataConnectionStateChanged(state, isDataConnectivityPissible,
                reason, apn, interfaceName);
    }

    public void notifyDataConnectionFailed(String reason) {
        /*
         * This is commented out because there is on onDataConnectionFailed callback
         * on PhoneStateListener.  There should be.
>>>>>>> 54b6cfa... Initial Contribution
        synchronized (mRecords) {
            mDataConnectionFailedReason = reason;
            final int N = mRecords.size();
            for (int i=N-1; i>=0; i--) {
                Record r = mRecords.get(i);
                if ((r.events & PhoneStateListener.LISTEN_DATA_CONNECTION_FAILED) != 0) {
                    // XXX
                }
            }
        }
        */
<<<<<<< HEAD
        broadcastDataConnectionFailed(reason, apnType);
    }

    public void notifyCellLocation(Bundle cellLocation) {
        if (!checkNotifyPermission("notifyCellLocation()")) {
            return;
        }
        synchronized (mRecords) {
            mCellLocation = cellLocation;
            for (Record r : mRecords) {
                if ((r.events & PhoneStateListener.LISTEN_CELL_LOCATION) != 0) {
                    try {
                        r.callback.onCellLocationChanged(new Bundle(cellLocation));
                    } catch (RemoteException ex) {
                        mRemoveList.add(r.binder);
                    }

                }
            }
            handleRemoveListLocked();
        }
    }

    public void notifyOtaspChanged(int otaspMode) {
        if (!checkNotifyPermission("notifyOtaspChanged()" )) {
            return;
        }
        synchronized (mRecords) {
            mOtaspMode = otaspMode;
            for (Record r : mRecords) {
                if ((r.events & PhoneStateListener.LISTEN_OTASP_CHANGED) != 0) {
                    try {
                        r.callback.onOtaspChanged(otaspMode);
                    } catch (RemoteException ex) {
                        mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    @Override
    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        if (mContext.checkCallingOrSelfPermission(android.Manifest.permission.DUMP)
                != PackageManager.PERMISSION_GRANTED) {
            pw.println("Permission Denial: can't dump telephony.registry from from pid="
                    + Binder.getCallingPid() + ", uid=" + Binder.getCallingUid());
            return;
        }
        synchronized (mRecords) {
            final int recordCount = mRecords.size();
=======
        broadcastDataConnectionFailed(reason);
    }

    public void notifyCellLocation(Bundle cellLocation) {
        synchronized (mRecords) {
            mCellLocation = cellLocation;
            final int N = mRecords.size();
            for (int i=N-1; i>=0; i--) {
                Record r = mRecords.get(i);
                if ((r.events & PhoneStateListener.LISTEN_CELL_LOCATION) != 0) {
                    sendCellLocation(r, cellLocation);
                }
            }
        }
    }

    //
    // the new callback broadcasting
    //
    // copy the service state object so they can't mess it up in the local calls
    // 
    public void sendServiceState(Record r, ServiceState state) {
        try {
            r.callback.onServiceStateChanged(new ServiceState(state));
        } catch (RemoteException ex) {
            remove(r.binder);
        }
    }

    public void sendCellLocation(Record r, Bundle cellLocation) {
        try {
            r.callback.onCellLocationChanged(new Bundle(cellLocation));
        } catch (RemoteException ex) {
            remove(r.binder);
        }
    }


    @Override
    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        if (mContext.checkCallingPermission(android.Manifest.permission.DUMP)
                != PackageManager.PERMISSION_GRANTED) {
            pw.println("Permission Denial: can't dump telephony.registry from from pid="
                    + Binder.getCallingPid()
                    + ", uid=" + Binder.getCallingUid());
            return;
        }
        synchronized (mRecords) {
            final int N = mRecords.size();
>>>>>>> 54b6cfa... Initial Contribution
            pw.println("last known state:");
            pw.println("  mCallState=" + mCallState);
            pw.println("  mCallIncomingNumber=" + mCallIncomingNumber);
            pw.println("  mServiceState=" + mServiceState);
            pw.println("  mSignalStrength=" + mSignalStrength);
            pw.println("  mMessageWaiting=" + mMessageWaiting);
            pw.println("  mCallForwarding=" + mCallForwarding);
            pw.println("  mDataActivity=" + mDataActivity);
            pw.println("  mDataConnectionState=" + mDataConnectionState);
            pw.println("  mDataConnectionPossible=" + mDataConnectionPossible);
            pw.println("  mDataConnectionReason=" + mDataConnectionReason);
            pw.println("  mDataConnectionApn=" + mDataConnectionApn);
<<<<<<< HEAD
            pw.println("  mDataConnectionLinkProperties=" + mDataConnectionLinkProperties);
            pw.println("  mDataConnectionLinkCapabilities=" + mDataConnectionLinkCapabilities);
            pw.println("  mCellLocation=" + mCellLocation);
            pw.println("  mCellInfo=" + mCellInfo);
            pw.println("registrations: count=" + recordCount);
            for (Record r : mRecords) {
=======
            pw.println("  mDataConnectionInterfaceName=" + mDataConnectionInterfaceName);
            pw.println("  mCellLocation=" + mCellLocation);
            pw.println("registrations: count=" + N);
            for (int i=0; i<N; i++) {
                Record r = mRecords.get(i);
>>>>>>> 54b6cfa... Initial Contribution
                pw.println("  " + r.pkgForDebug + " 0x" + Integer.toHexString(r.events));
            }
        }
    }

<<<<<<< HEAD
=======
    
>>>>>>> 54b6cfa... Initial Contribution
    //
    // the legacy intent broadcasting
    //

    private void broadcastServiceStateChanged(ServiceState state) {
<<<<<<< HEAD
        long ident = Binder.clearCallingIdentity();
        try {
            mBatteryStats.notePhoneState(state.getState());
        } catch (RemoteException re) {
            // Can't do much
        } finally {
            Binder.restoreCallingIdentity(ident);
        }

=======
>>>>>>> 54b6cfa... Initial Contribution
        Intent intent = new Intent(TelephonyIntents.ACTION_SERVICE_STATE_CHANGED);
        Bundle data = new Bundle();
        state.fillInNotifierBundle(data);
        intent.putExtras(data);
<<<<<<< HEAD
        mContext.sendStickyBroadcast(intent);
    }

    private void broadcastSignalStrengthChanged(SignalStrength signalStrength) {
        long ident = Binder.clearCallingIdentity();
        try {
            mBatteryStats.notePhoneSignalStrength(signalStrength);
        } catch (RemoteException e) {
            /* The remote entity disappeared, we can safely ignore the exception. */
        } finally {
            Binder.restoreCallingIdentity(ident);
        }

        Intent intent = new Intent(TelephonyIntents.ACTION_SIGNAL_STRENGTH_CHANGED);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        Bundle data = new Bundle();
        signalStrength.fillInNotifierBundle(data);
        intent.putExtras(data);
        mContext.sendStickyBroadcast(intent);
    }

    private void broadcastCallStateChanged(int state, String incomingNumber) {
        long ident = Binder.clearCallingIdentity();
        try {
            if (state == TelephonyManager.CALL_STATE_IDLE) {
                mBatteryStats.notePhoneOff();
            } else {
                mBatteryStats.notePhoneOn();
            }
        } catch (RemoteException e) {
            /* The remote entity disappeared, we can safely ignore the exception. */
        } finally {
            Binder.restoreCallingIdentity(ident);
        }

        Intent intent = new Intent(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        intent.putExtra(Phone.STATE_KEY, DefaultPhoneNotifier.convertCallState(state).toString());
        if (!TextUtils.isEmpty(incomingNumber)) {
            intent.putExtra(TelephonyManager.EXTRA_INCOMING_NUMBER, incomingNumber);
        }
        mContext.sendBroadcast(intent, android.Manifest.permission.READ_PHONE_STATE);
    }

    private void broadcastDataConnectionStateChanged(int state,
            boolean isDataConnectivityPossible,
            String reason, String apn, String apnType, LinkProperties linkProperties,
            LinkCapabilities linkCapabilities, boolean roaming) {
        // Note: not reporting to the battery stats service here, because the
        // status bar takes care of that after taking into account all of the
        // required info.
=======
        broadcastStickyIntent(intent);
    }

    private void broadcastSignalStrengthChanged(int asu) {
        Intent intent = new Intent(TelephonyIntents.ACTION_SIGNAL_STRENGTH_CHANGED);
        intent.putExtra(PhoneStateIntentReceiver.INTENT_KEY_ASU, asu);
        broadcastStickyIntent(intent);
    }

    private void broadcastCallStateChanged(int state, String incomingNumber) {
        Intent intent = new Intent(TelephonyIntents.ACTION_PHONE_STATE_CHANGED);
        intent.putExtra(Phone.STATE_KEY,
                DefaultPhoneNotifier.convertCallState(state).toString());
        intent.putExtra(PhoneStateIntentReceiver.INTENT_KEY_NUM, incomingNumber);
        broadcastStickyIntent(intent);
    }

    private void broadcastDataConnectionStateChanged(int state, boolean isDataConnectivityPossible,
            String reason, String apn, String interfaceName) {
>>>>>>> 54b6cfa... Initial Contribution
        Intent intent = new Intent(TelephonyIntents.ACTION_ANY_DATA_CONNECTION_STATE_CHANGED);
        intent.putExtra(Phone.STATE_KEY, DefaultPhoneNotifier.convertDataState(state).toString());
        if (!isDataConnectivityPossible) {
            intent.putExtra(Phone.NETWORK_UNAVAILABLE_KEY, true);
        }
        if (reason != null) {
            intent.putExtra(Phone.STATE_CHANGE_REASON_KEY, reason);
        }
<<<<<<< HEAD
        if (linkProperties != null) {
            intent.putExtra(Phone.DATA_LINK_PROPERTIES_KEY, linkProperties);
            String iface = linkProperties.getInterfaceName();
            if (iface != null) {
                intent.putExtra(Phone.DATA_IFACE_NAME_KEY, iface);
            }
        }
        if (linkCapabilities != null) {
            intent.putExtra(Phone.DATA_LINK_CAPABILITIES_KEY, linkCapabilities);
        }
        if (roaming) intent.putExtra(Phone.DATA_NETWORK_ROAMING_KEY, true);

        intent.putExtra(Phone.DATA_APN_KEY, apn);
        intent.putExtra(Phone.DATA_APN_TYPE_KEY, apnType);
        mContext.sendStickyBroadcast(intent);
    }

    private void broadcastDataConnectionFailed(String reason, String apnType) {
        Intent intent = new Intent(TelephonyIntents.ACTION_DATA_CONNECTION_FAILED);
        intent.putExtra(Phone.FAILURE_REASON_KEY, reason);
        intent.putExtra(Phone.DATA_APN_TYPE_KEY, apnType);
        mContext.sendStickyBroadcast(intent);
    }

    private boolean checkNotifyPermission(String method) {
        if (mContext.checkCallingOrSelfPermission(android.Manifest.permission.MODIFY_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        String msg = "Modify Phone State Permission Denial: " + method + " from pid="
                + Binder.getCallingPid() + ", uid=" + Binder.getCallingUid();
        if (DBG) Slog.w(TAG, msg);
        return false;
    }

    private void checkListenerPermission(int events) {
        if ((events & PhoneStateListener.LISTEN_CELL_LOCATION) != 0) {
            mContext.enforceCallingOrSelfPermission(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION, null);

        }

        if ((events & PhoneStateListener.LISTEN_CELL_INFO) != 0) {
            mContext.enforceCallingOrSelfPermission(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION, null);

        }

        if ((events & PHONE_STATE_PERMISSION_MASK) != 0) {
            mContext.enforceCallingOrSelfPermission(
                    android.Manifest.permission.READ_PHONE_STATE, null);
        }
    }

    private void handleRemoveListLocked() {
        if (mRemoveList.size() > 0) {
            for (IBinder b: mRemoveList) {
                remove(b);
            }
            mRemoveList.clear();
        }
=======
        intent.putExtra(Phone.DATA_APN_KEY, apn);
        intent.putExtra(Phone.DATA_IFACE_NAME_KEY, interfaceName);
        broadcastStickyIntent(intent);
    }

    private void broadcastDataConnectionFailed(String reason) {
        Intent intent = new Intent(TelephonyIntents.ACTION_DATA_CONNECTION_FAILED);
        intent.putExtra(Phone.FAILURE_REASON_KEY, reason);
        broadcastStickyIntent(intent);
    }

    private static void broadcastStickyIntent(Intent intent) {
        ActivityManagerNative.broadcastStickyIntent(intent, null);
>>>>>>> 54b6cfa... Initial Contribution
    }
}
