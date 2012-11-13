<<<<<<< HEAD
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

=======
>>>>>>> 54b6cfa... Initial Contribution
package android.telephony;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.ServiceState;
<<<<<<< HEAD
import android.telephony.SignalStrength;
import android.telephony.CellLocation;
import android.telephony.CellInfo;
import android.util.Log;

import com.android.internal.telephony.IPhoneStateListener;
import com.android.internal.telephony.Phone;

/**
 * A listener class for monitoring changes in specific telephony states
 * on the device, including service state, signal strength, message
 * waiting indicator (voicemail), and others.
 * <p>
 * Override the methods for the state that you wish to receive updates for, and
=======
import android.telephony.CellLocation;
import android.util.Log;

import com.android.internal.telephony.IPhoneStateListener;

/**
 * A listener class for monitoring changes in specific telephony states
 * on the device, including service state, signal strength, message 
 * waiting indicator (voicemail), and others.
 * <p>
 * Override the methods for the state that you wish to receive updates for, and 
>>>>>>> 54b6cfa... Initial Contribution
 * pass your PhoneStateListener object, along with bitwise-or of the LISTEN_
 * flags to {@link TelephonyManager#listen TelephonyManager.listen()}.
 * <p>
 * Note that access to some telephony information is
<<<<<<< HEAD
 * permission-protected. Your application won't receive updates for protected
 * information unless it has the appropriate permissions declared in
 * its manifest file. Where permissions apply, they are noted in the
 * appropriate LISTEN_ flags.
=======
 * permission-protected. Your application won't receive updates for protected 
 * information unless it has the appropriate permissions declared in 
 * its manifest file. Where permissions apply, they are noted in the
 * appropriate LISTEN_ flags. 
>>>>>>> 54b6cfa... Initial Contribution
 */
public class PhoneStateListener {

    /**
     * Stop listening for updates.
     */
    public static final int LISTEN_NONE = 0;

    /**
     *  Listen for changes to the network service state (cellular).
     *
     *  @see #onServiceStateChanged
     *  @see ServiceState
     */
    public static final int LISTEN_SERVICE_STATE                            = 0x00000001;

    /**
     * Listen for changes to the network signal strength (cellular).
<<<<<<< HEAD
     * {@more}
     * Requires Permission: {@link android.Manifest.permission#READ_PHONE_STATE
     * READ_PHONE_STATE}
     * <p>
     *
     * @see #onSignalStrengthChanged
     *
     * @deprecated by {@link #LISTEN_SIGNAL_STRENGTHS}
     */
    @Deprecated
=======
     * <p>
     * Example: The status bar uses this to control the signal-strength
     * icon.
     *
     * @see #onSignalStrengthChanged
     */
>>>>>>> 54b6cfa... Initial Contribution
    public static final int LISTEN_SIGNAL_STRENGTH                          = 0x00000002;

    /**
     * Listen for changes to the message-waiting indicator.
<<<<<<< HEAD
     * {@more}
     * Requires Permission: {@link android.Manifest.permission#READ_PHONE_STATE
     * READ_PHONE_STATE}
=======
>>>>>>> 54b6cfa... Initial Contribution
     * <p>
     * Example: The status bar uses this to determine when to display the
     * voicemail icon.
     *
     * @see #onMessageWaitingIndicatorChanged
     */
    public static final int LISTEN_MESSAGE_WAITING_INDICATOR                = 0x00000004;

    /**
     * Listen for changes to the call-forwarding indicator.
<<<<<<< HEAD
     * {@more}
     * Requires Permission: {@link android.Manifest.permission#READ_PHONE_STATE
     * READ_PHONE_STATE}
=======
     *
>>>>>>> 54b6cfa... Initial Contribution
     * @see #onCallForwardingIndicatorChanged
     */
    public static final int LISTEN_CALL_FORWARDING_INDICATOR                = 0x00000008;

    /**
<<<<<<< HEAD
     * Listen for changes to the device's cell location. Note that
=======
     * Listen for changes to the device's cell location. Note that 
>>>>>>> 54b6cfa... Initial Contribution
     * this will result in frequent callbacks to the listener.
     * {@more}
     * Requires Permission: {@link android.Manifest.permission#ACCESS_COARSE_LOCATION
     * ACCESS_COARSE_LOCATION}
     * <p>
<<<<<<< HEAD
     * If you need regular location updates but want more control over
     * the update interval or location precision, you can set up a listener
     * through the {@link android.location.LocationManager location manager}
     * instead.
     *
=======
     * If you need regular location updates but want more control over 
     * the update interval or location precision, you can set up a listener 
     * through the {@link android.location.LocationManager location manager} 
     * instead. 
     * 
>>>>>>> 54b6cfa... Initial Contribution
     * @see #onCellLocationChanged
     */
    public static final int LISTEN_CELL_LOCATION                            = 0x00000010;

    /**
     * Listen for changes to the device call state.
<<<<<<< HEAD
     * {@more}
     * Requires Permission: {@link android.Manifest.permission#READ_PHONE_STATE
     * READ_PHONE_STATE}
=======
     *
>>>>>>> 54b6cfa... Initial Contribution
     * @see #onCallStateChanged
     */
    public static final int LISTEN_CALL_STATE                               = 0x00000020;

    /**
     * Listen for changes to the data connection state (cellular).
     *
     * @see #onDataConnectionStateChanged
     */
    public static final int LISTEN_DATA_CONNECTION_STATE                    = 0x00000040;

    /**
     * Listen for changes to the direction of data traffic on the data
     * connection (cellular).
<<<<<<< HEAD
     * {@more}
     * Requires Permission: {@link android.Manifest.permission#READ_PHONE_STATE
     * READ_PHONE_STATE}
     * Example: The status bar uses this to display the appropriate
=======
     *
     * Example: The status bar uses this to display the appropriate  
>>>>>>> 54b6cfa... Initial Contribution
     * data-traffic icon.
     *
     * @see #onDataActivity
     */
    public static final int LISTEN_DATA_ACTIVITY                            = 0x00000080;

<<<<<<< HEAD
    /**
     * Listen for changes to the network signal strengths (cellular).
     * <p>
     * Example: The status bar uses this to control the signal-strength
     * icon.
     *
     * @see #onSignalStrengthsChanged
     */
    public static final int LISTEN_SIGNAL_STRENGTHS                         = 0x00000100;

    /**
     * Listen for changes to OTASP mode.
     *
     * @see #onOtaspChanged
     * @hide
     */
    public static final int LISTEN_OTASP_CHANGED                            = 0x00000200;

    /**
     * Listen for changes to observed cell info.
     *
     * @see #onCellInfoChanged
     * @hide pending API review
     */
    public static final int LISTEN_CELL_INFO = 0x00000400;

=======
>>>>>>> 54b6cfa... Initial Contribution
    public PhoneStateListener() {
    }

    /**
<<<<<<< HEAD
     * Callback invoked when device service state changes.
=======
     * Callback invoked when device service state changes. 
>>>>>>> 54b6cfa... Initial Contribution
     *
     * @see ServiceState#STATE_EMERGENCY_ONLY
     * @see ServiceState#STATE_IN_SERVICE
     * @see ServiceState#STATE_OUT_OF_SERVICE
     * @see ServiceState#STATE_POWER_OFF
     */
    public void onServiceStateChanged(ServiceState serviceState) {
        // default implementation empty
    }

    /**
     * Callback invoked when network signal strength changes.
     *
     * @see ServiceState#STATE_EMERGENCY_ONLY
     * @see ServiceState#STATE_IN_SERVICE
     * @see ServiceState#STATE_OUT_OF_SERVICE
     * @see ServiceState#STATE_POWER_OFF
<<<<<<< HEAD
     * @deprecated Use {@link #onSignalStrengthsChanged(SignalStrength)}
     */
    @Deprecated
=======
     */
>>>>>>> 54b6cfa... Initial Contribution
    public void onSignalStrengthChanged(int asu) {
        // default implementation empty
    }

    /**
<<<<<<< HEAD
     * Callback invoked when the message-waiting indicator changes.
=======
     * Callback invoked when the message-waiting indicator changes. 
>>>>>>> 54b6cfa... Initial Contribution
     */
    public void onMessageWaitingIndicatorChanged(boolean mwi) {
        // default implementation empty
    }

    /**
<<<<<<< HEAD
     * Callback invoked when the call-forwarding indicator changes.
=======
     * Callback invoked when the call-forwarding indicator changes. 
>>>>>>> 54b6cfa... Initial Contribution
     */
    public void onCallForwardingIndicatorChanged(boolean cfi) {
        // default implementation empty
    }

    /**
<<<<<<< HEAD
     * Callback invoked when device cell location changes.
=======
     * Callback invoked when device cell location changes. 
>>>>>>> 54b6cfa... Initial Contribution
     */
    public void onCellLocationChanged(CellLocation location) {
        // default implementation empty
    }

    /**
<<<<<<< HEAD
     * Callback invoked when device call state changes.
=======
     * Callback invoked when device call state changes. 
>>>>>>> 54b6cfa... Initial Contribution
     *
     * @see TelephonyManager#CALL_STATE_IDLE
     * @see TelephonyManager#CALL_STATE_RINGING
     * @see TelephonyManager#CALL_STATE_OFFHOOK
     */
    public void onCallStateChanged(int state, String incomingNumber) {
        // default implementation empty
    }

    /**
<<<<<<< HEAD
     * Callback invoked when connection state changes.
=======
     * Callback invoked when connection state changes. 
>>>>>>> 54b6cfa... Initial Contribution
     *
     * @see TelephonyManager#DATA_DISCONNECTED
     * @see TelephonyManager#DATA_CONNECTING
     * @see TelephonyManager#DATA_CONNECTED
     * @see TelephonyManager#DATA_SUSPENDED
     */
    public void onDataConnectionStateChanged(int state) {
        // default implementation empty
    }

    /**
<<<<<<< HEAD
     * same as above, but with the network type.  Both called.
     */
    public void onDataConnectionStateChanged(int state, int networkType) {
    }

    /**
     * Callback invoked when data activity state changes.
=======
     * Callback invoked when data activity state changes. 
>>>>>>> 54b6cfa... Initial Contribution
     *
     * @see TelephonyManager#DATA_ACTIVITY_NONE
     * @see TelephonyManager#DATA_ACTIVITY_IN
     * @see TelephonyManager#DATA_ACTIVITY_OUT
     * @see TelephonyManager#DATA_ACTIVITY_INOUT
<<<<<<< HEAD
     * @see TelephonyManager#DATA_ACTIVITY_DORMANT
=======
>>>>>>> 54b6cfa... Initial Contribution
     */
    public void onDataActivity(int direction) {
        // default implementation empty
    }

    /**
<<<<<<< HEAD
     * Callback invoked when network signal strengths changes.
     *
     * @see ServiceState#STATE_EMERGENCY_ONLY
     * @see ServiceState#STATE_IN_SERVICE
     * @see ServiceState#STATE_OUT_OF_SERVICE
     * @see ServiceState#STATE_POWER_OFF
     */
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        // default implementation empty
    }


    /**
     * The Over The Air Service Provisioning (OTASP) has changed. Requires
     * the READ_PHONE_STATE permission.
     * @param otaspMode is integer <code>OTASP_UNKNOWN=1<code>
     *   means the value is currently unknown and the system should wait until
     *   <code>OTASP_NEEDED=2<code> or <code>OTASP_NOT_NEEDED=3<code> is received before
     *   making the decisision to perform OTASP or not.
     *
     * @hide
     */
    public void onOtaspChanged(int otaspMode) {
        // default implementation empty
    }

    /**
     * Callback invoked when a observed cell info gets changed.
     *
     * A notification should be sent when:
     *     1. a cell is newly-observed.
     *     2. a observed cell is not visible.
     *     3. any of the cell info of a observed cell has changed.
     *
     * @hide pending API review
     */
    public void onCellInfoChanged(CellInfo cellInfo) {
        // default implementation empty
    }

    /**
=======
>>>>>>> 54b6cfa... Initial Contribution
     * The callback methods need to be called on the handler thread where
     * this object was created.  If the binder did that for us it'd be nice.
     */
    IPhoneStateListener callback = new IPhoneStateListener.Stub() {
        public void onServiceStateChanged(ServiceState serviceState) {
            Message.obtain(mHandler, LISTEN_SERVICE_STATE, 0, 0, serviceState).sendToTarget();
        }

        public void onSignalStrengthChanged(int asu) {
            Message.obtain(mHandler, LISTEN_SIGNAL_STRENGTH, asu, 0, null).sendToTarget();
        }

        public void onMessageWaitingIndicatorChanged(boolean mwi) {
            Message.obtain(mHandler, LISTEN_MESSAGE_WAITING_INDICATOR, mwi ? 1 : 0, 0, null)
                    .sendToTarget();
        }

        public void onCallForwardingIndicatorChanged(boolean cfi) {
            Message.obtain(mHandler, LISTEN_CALL_FORWARDING_INDICATOR, cfi ? 1 : 0, 0, null)
                    .sendToTarget();
        }

        public void onCellLocationChanged(Bundle bundle) {
            CellLocation location = CellLocation.newFromBundle(bundle);
            Message.obtain(mHandler, LISTEN_CELL_LOCATION, 0, 0, location).sendToTarget();
        }

        public void onCallStateChanged(int state, String incomingNumber) {
            Message.obtain(mHandler, LISTEN_CALL_STATE, state, 0, incomingNumber).sendToTarget();
        }

<<<<<<< HEAD
        public void onDataConnectionStateChanged(int state, int networkType) {
            Message.obtain(mHandler, LISTEN_DATA_CONNECTION_STATE, state, networkType).
                    sendToTarget();
=======
        public void onDataConnectionStateChanged(int state) {
            Message.obtain(mHandler, LISTEN_DATA_CONNECTION_STATE, state, 0, null).sendToTarget();
>>>>>>> 54b6cfa... Initial Contribution
        }

        public void onDataActivity(int direction) {
            Message.obtain(mHandler, LISTEN_DATA_ACTIVITY, direction, 0, null).sendToTarget();
        }
<<<<<<< HEAD

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            Message.obtain(mHandler, LISTEN_SIGNAL_STRENGTHS, 0, 0, signalStrength).sendToTarget();
        }

        public void onOtaspChanged(int otaspMode) {
            Message.obtain(mHandler, LISTEN_OTASP_CHANGED, otaspMode, 0).sendToTarget();
        }

        public void onCellInfoChanged(CellInfo cellInfo) {
            Message.obtain(mHandler, LISTEN_CELL_INFO, 0, 0).sendToTarget();
        }
=======
>>>>>>> 54b6cfa... Initial Contribution
    };

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            //Log.d("TelephonyRegistry", "what=0x" + Integer.toHexString(msg.what) + " msg=" + msg);
            switch (msg.what) {
                case LISTEN_SERVICE_STATE:
                    PhoneStateListener.this.onServiceStateChanged((ServiceState)msg.obj);
                    break;
                case LISTEN_SIGNAL_STRENGTH:
                    PhoneStateListener.this.onSignalStrengthChanged(msg.arg1);
                    break;
                case LISTEN_MESSAGE_WAITING_INDICATOR:
                    PhoneStateListener.this.onMessageWaitingIndicatorChanged(msg.arg1 != 0);
                    break;
                case LISTEN_CALL_FORWARDING_INDICATOR:
                    PhoneStateListener.this.onCallForwardingIndicatorChanged(msg.arg1 != 0);
                    break;
                case LISTEN_CELL_LOCATION:
                    PhoneStateListener.this.onCellLocationChanged((CellLocation)msg.obj);
                    break;
                case LISTEN_CALL_STATE:
                    PhoneStateListener.this.onCallStateChanged(msg.arg1, (String)msg.obj);
                    break;
                case LISTEN_DATA_CONNECTION_STATE:
<<<<<<< HEAD
                    PhoneStateListener.this.onDataConnectionStateChanged(msg.arg1, msg.arg2);
=======
>>>>>>> 54b6cfa... Initial Contribution
                    PhoneStateListener.this.onDataConnectionStateChanged(msg.arg1);
                    break;
                case LISTEN_DATA_ACTIVITY:
                    PhoneStateListener.this.onDataActivity(msg.arg1);
                    break;
<<<<<<< HEAD
                case LISTEN_SIGNAL_STRENGTHS:
                    PhoneStateListener.this.onSignalStrengthsChanged((SignalStrength)msg.obj);
                    break;
                case LISTEN_OTASP_CHANGED:
                    PhoneStateListener.this.onOtaspChanged(msg.arg1);
                    break;
                case LISTEN_CELL_INFO:
                    PhoneStateListener.this.onCellInfoChanged((CellInfo)msg.obj);
=======
>>>>>>> 54b6cfa... Initial Contribution
            }
        }
    };
}
