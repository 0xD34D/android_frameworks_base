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

package android.bluetooth;

<<<<<<< HEAD
import android.annotation.SdkConstant;
import android.annotation.SdkConstant.SdkConstantType;
=======
>>>>>>> 54b6cfa... Initial Contribution
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
<<<<<<< HEAD
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Public API for controlling the Bluetooth Headset Service. This includes both
 * Bluetooth Headset and Handsfree (v1.5) profiles.
 *
 * <p>BluetoothHeadset is a proxy object for controlling the Bluetooth Headset
 * Service via IPC.
 *
 * <p> Use {@link BluetoothAdapter#getProfileProxy} to get
 * the BluetoothHeadset proxy object. Use
 * {@link BluetoothAdapter#closeProfileProxy} to close the service connection.
 *
 * <p> Android only supports one connected Bluetooth Headset at a time.
 * Each method is protected with its appropriate permission.
 */
public final class BluetoothHeadset implements BluetoothProfile {
    private static final String TAG = "BluetoothHeadset";
    private static final boolean DBG = false;

    /**
     * Intent used to broadcast the change in connection state of the Headset
     * profile.
     *
     * <p>This intent will have 3 extras:
     * <ul>
     *   <li> {@link #EXTRA_STATE} - The current state of the profile. </li>
     *   <li> {@link #EXTRA_PREVIOUS_STATE}- The previous state of the profile. </li>
     *   <li> {@link BluetoothDevice#EXTRA_DEVICE} - The remote device. </li>
     * </ul>
     * <p>{@link #EXTRA_STATE} or {@link #EXTRA_PREVIOUS_STATE} can be any of
     * {@link #STATE_DISCONNECTED}, {@link #STATE_CONNECTING},
     * {@link #STATE_CONNECTED}, {@link #STATE_DISCONNECTING}.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission to
     * receive.
     */
    @SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final String ACTION_CONNECTION_STATE_CHANGED =
        "android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED";

    /**
     * Intent used to broadcast the change in the Audio Connection state of the
     * A2DP profile.
     *
     * <p>This intent will have 3 extras:
     * <ul>
     *   <li> {@link #EXTRA_STATE} - The current state of the profile. </li>
     *   <li> {@link #EXTRA_PREVIOUS_STATE}- The previous state of the profile. </li>
     *   <li> {@link BluetoothDevice#EXTRA_DEVICE} - The remote device. </li>
     * </ul>
     * <p>{@link #EXTRA_STATE} or {@link #EXTRA_PREVIOUS_STATE} can be any of
     * {@link #STATE_AUDIO_CONNECTED}, {@link #STATE_AUDIO_DISCONNECTED},
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission
     * to receive.
     */
    @SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final String ACTION_AUDIO_STATE_CHANGED =
        "android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED";


    /**
     * Intent used to broadcast that the headset has posted a
     * vendor-specific event.
     *
     * <p>This intent will have 4 extras and 1 category.
     * <ul>
     *  <li> {@link BluetoothDevice#EXTRA_DEVICE} - The remote Bluetooth Device
     *       </li>
     *  <li> {@link #EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD} - The vendor
     *       specific command </li>
     *  <li> {@link #EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD_TYPE} - The AT
     *       command type which can be one of  {@link #AT_CMD_TYPE_READ},
     *       {@link #AT_CMD_TYPE_TEST}, or {@link #AT_CMD_TYPE_SET},
     *       {@link #AT_CMD_TYPE_BASIC},{@link #AT_CMD_TYPE_ACTION}. </li>
     *  <li> {@link #EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_ARGS} - Command
     *       arguments. </li>
     * </ul>
     *
     *<p> The category is the Company ID of the vendor defining the
     * vendor-specific command. {@link BluetoothAssignedNumbers}
     *
     * For example, for Plantronics specific events
     * Category will be {@link #VENDOR_SPECIFIC_HEADSET_EVENT_COMPANY_ID_CATEGORY}.55
     *
     * <p> For example, an AT+XEVENT=foo,3 will get translated into
     * <ul>
     *   <li> EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD = +XEVENT </li>
     *   <li> EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD_TYPE = AT_CMD_TYPE_SET </li>
     *   <li> EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_ARGS = foo, 3 </li>
     * </ul>
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission
     * to receive.
     */
    @SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final String ACTION_VENDOR_SPECIFIC_HEADSET_EVENT =
            "android.bluetooth.headset.action.VENDOR_SPECIFIC_HEADSET_EVENT";

    /**
     * A String extra field in {@link #ACTION_VENDOR_SPECIFIC_HEADSET_EVENT}
     * intents that contains the name of the vendor-specific command.
     */
    public static final String EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD =
            "android.bluetooth.headset.extra.VENDOR_SPECIFIC_HEADSET_EVENT_CMD";

    /**
     * An int extra field in {@link #ACTION_VENDOR_SPECIFIC_HEADSET_EVENT}
     * intents that contains the AT command type of the vendor-specific command.
     */
    public static final String EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD_TYPE =
            "android.bluetooth.headset.extra.VENDOR_SPECIFIC_HEADSET_EVENT_CMD_TYPE";

    /**
     * AT command type READ used with
     * {@link #EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD_TYPE}
     * For example, AT+VGM?. There are no arguments for this command type.
     */
    public static final int AT_CMD_TYPE_READ = 0;

    /**
     * AT command type TEST used with
     * {@link #EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD_TYPE}
     * For example, AT+VGM=?. There are no arguments for this command type.
     */
    public static final int AT_CMD_TYPE_TEST = 1;

    /**
     * AT command type SET used with
     * {@link #EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD_TYPE}
     * For example, AT+VGM=<args>.
     */
    public static final int AT_CMD_TYPE_SET = 2;

    /**
     * AT command type BASIC used with
     * {@link #EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD_TYPE}
     * For example, ATD. Single character commands and everything following the
     * character are arguments.
     */
    public static final int AT_CMD_TYPE_BASIC = 3;

    /**
     * AT command type ACTION used with
     * {@link #EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD_TYPE}
     * For example, AT+CHUP. There are no arguments for action commands.
     */
    public static final int AT_CMD_TYPE_ACTION = 4;

    /**
     * A Parcelable String array extra field in
     * {@link #ACTION_VENDOR_SPECIFIC_HEADSET_EVENT} intents that contains
     * the arguments to the vendor-specific command.
     */
    public static final String EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_ARGS =
            "android.bluetooth.headset.extra.VENDOR_SPECIFIC_HEADSET_EVENT_ARGS";

    /**
     * The intent category to be used with {@link #ACTION_VENDOR_SPECIFIC_HEADSET_EVENT}
     * for the companyId
     */
    public static final String VENDOR_SPECIFIC_HEADSET_EVENT_COMPANY_ID_CATEGORY  =
            "android.bluetooth.headset.intent.category.companyid";

    /**
     * Headset state when SCO audio is not connected.
     * This state can be one of
     * {@link #EXTRA_STATE} or {@link #EXTRA_PREVIOUS_STATE} of
     * {@link #ACTION_AUDIO_STATE_CHANGED} intent.
     */
    public static final int STATE_AUDIO_DISCONNECTED = 10;

    /**
     * Headset state when SCO audio is connecting.
     * This state can be one of
     * {@link #EXTRA_STATE} or {@link #EXTRA_PREVIOUS_STATE} of
     * {@link #ACTION_AUDIO_STATE_CHANGED} intent.
     */
    public static final int STATE_AUDIO_CONNECTING = 11;

    /**
     * Headset state when SCO audio is connected.
     * This state can be one of
     * {@link #EXTRA_STATE} or {@link #EXTRA_PREVIOUS_STATE} of
     * {@link #ACTION_AUDIO_STATE_CHANGED} intent.
     */
    public static final int STATE_AUDIO_CONNECTED = 12;


    private Context mContext;
    private ServiceListener mServiceListener;
    private IBluetoothHeadset mService;
    BluetoothAdapter mAdapter;

    /**
     * Create a BluetoothHeadset proxy object.
     */
    /*package*/ BluetoothHeadset(Context context, ServiceListener l) {
        mContext = context;
        mServiceListener = l;
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!context.bindService(new Intent(IBluetoothHeadset.class.getName()), mConnection, 0)) {
=======
import android.os.RemoteException;
import android.os.IBinder;
import android.util.Log;

/**
 * The Android Bluetooth API is not finalized, and *will* change. Use at your
 * own risk.
 *
 * Public API for controlling the Bluetooth Headset Service.
 *
 * BluetoothHeadset is a proxy object for controlling the Bluetooth Headset
 * Service.
 *
 * Creating a BluetoothHeadset object will create a binding with the
 * BluetoothHeadset service. Users of this object should call close() when they
 * are finished with the BluetoothHeadset, so that this proxy object can unbind
 * from the service.
 *
 * BlueoothHeadset objects are not guarenteed to be connected to the
 * BluetoothHeadsetService at all times. Calls on this object while not
 * connected to the service will result in default error return values. Even
 * after object construction, there is a short delay (~10ms) before this proxy
 * object is actually connected to the Service.
 *
 * Android only supports one connected Bluetooth Headset at a time.
 *
 * Note that in this context, Headset includes both Bluetooth Headset's and
 * Handsfree devices.
 *
 * @hide
 */
public class BluetoothHeadset {

    private final static String TAG = "BluetoothHeadset";

    private final Context mContext;
    private IBluetoothHeadset mService;

    /** There was an error trying to obtain the state */
    public static final int STATE_ERROR        = -1;
    /** No headset currently connected */
    public static final int STATE_DISCONNECTED = 0;
    /** Connection attempt in progress */
    public static final int STATE_CONNECTING   = 1;
    /** A headset is currently connected */
    public static final int STATE_CONNECTED    = 2;

    public static final int RESULT_FAILURE = 0;
    public static final int RESULT_SUCCESS = 1;
    /** Connection cancelled before completetion. */
    public static final int RESULT_CANCELLED = 2;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = IBluetoothHeadset.Stub.asInterface(service);
            Log.i(TAG, "Proxy object is now connected to Bluetooth Headset Service");
        }
        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };

    /**
     * Create a BluetoothHeadset proxy object.
     * Remeber to call close() when you are done with this object, so that it
     * can unbind from the BluetoothHeadsetService.
     */
    public BluetoothHeadset(Context context) {
        mContext = context;
        if (!context.bindService(
                new Intent(IBluetoothHeadset.class.getName()), mConnection, 0)) {
>>>>>>> 54b6cfa... Initial Contribution
            Log.e(TAG, "Could not bind to Bluetooth Headset Service");
        }
    }

<<<<<<< HEAD
=======
    protected void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

>>>>>>> 54b6cfa... Initial Contribution
    /**
     * Close the connection to the backing service.
     * Other public functions of BluetoothHeadset will return default error
     * results once close() has been called. Multiple invocations of close()
     * are ok.
     */
<<<<<<< HEAD
    /*package*/ synchronized void close() {
        if (DBG) log("close()");
=======
    public synchronized void close() {
>>>>>>> 54b6cfa... Initial Contribution
        if (mConnection != null) {
            mContext.unbindService(mConnection);
            mConnection = null;
        }
<<<<<<< HEAD
        mServiceListener = null;
    }

    /**
     * Initiate connection to a profile of the remote bluetooth device.
     *
     * <p> Currently, the system supports only 1 connection to the
     * headset/handsfree profile. The API will automatically disconnect connected
     * devices before connecting.
     *
     * <p> This API returns false in scenarios like the profile on the
     * device is already connected or Bluetooth is not turned on.
     * When this API returns true, it is guaranteed that
     * connection state intent for the profile will be broadcasted with
     * the state. Users can get the connection state of the profile
     * from this intent.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH_ADMIN}
     * permission.
     *
     * @param device Remote Bluetooth Device
     * @return false on immediate error,
     *               true otherwise
     * @hide
     */
    public boolean connect(BluetoothDevice device) {
        if (DBG) log("connect(" + device + ")");
        if (mService != null && isEnabled() &&
            isValidDevice(device)) {
            try {
                return mService.connect(device);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (mService == null) Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    /**
     * Initiate disconnection from a profile
     *
     * <p> This API will return false in scenarios like the profile on the
     * Bluetooth device is not in connected state etc. When this API returns,
     * true, it is guaranteed that the connection state change
     * intent will be broadcasted with the state. Users can get the
     * disconnection state of the profile from this intent.
     *
     * <p> If the disconnection is initiated by a remote device, the state
     * will transition from {@link #STATE_CONNECTED} to
     * {@link #STATE_DISCONNECTED}. If the disconnect is initiated by the
     * host (local) device the state will transition from
     * {@link #STATE_CONNECTED} to state {@link #STATE_DISCONNECTING} to
     * state {@link #STATE_DISCONNECTED}. The transition to
     * {@link #STATE_DISCONNECTING} can be used to distinguish between the
     * two scenarios.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH_ADMIN}
     * permission.
     *
     * @param device Remote Bluetooth Device
     * @return false on immediate error,
     *               true otherwise
     * @hide
     */
    public boolean disconnect(BluetoothDevice device) {
        if (DBG) log("disconnect(" + device + ")");
        if (mService != null && isEnabled() &&
            isValidDevice(device)) {
            try {
                return mService.disconnect(device);
            } catch (RemoteException e) {
              Log.e(TAG, Log.getStackTraceString(new Throwable()));
              return false;
            }
        }
        if (mService == null) Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public List<BluetoothDevice> getConnectedDevices() {
        if (DBG) log("getConnectedDevices()");
        if (mService != null && isEnabled()) {
            try {
                return mService.getConnectedDevices();
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
                return new ArrayList<BluetoothDevice>();
            }
        }
        if (mService == null) Log.w(TAG, "Proxy not attached to service");
        return new ArrayList<BluetoothDevice>();
    }

    /**
     * {@inheritDoc}
     */
    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        if (DBG) log("getDevicesMatchingStates()");
        if (mService != null && isEnabled()) {
            try {
                return mService.getDevicesMatchingConnectionStates(states);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
                return new ArrayList<BluetoothDevice>();
            }
        }
        if (mService == null) Log.w(TAG, "Proxy not attached to service");
        return new ArrayList<BluetoothDevice>();
    }

    /**
     * {@inheritDoc}
     */
    public int getConnectionState(BluetoothDevice device) {
        if (DBG) log("getConnectionState(" + device + ")");
        if (mService != null && isEnabled() &&
            isValidDevice(device)) {
            try {
                return mService.getConnectionState(device);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
                return BluetoothProfile.STATE_DISCONNECTED;
            }
        }
        if (mService == null) Log.w(TAG, "Proxy not attached to service");
        return BluetoothProfile.STATE_DISCONNECTED;
    }

    /**
     * Set priority of the profile
     *
     * <p> The device should already be paired.
     *  Priority can be one of {@link #PRIORITY_ON} or
     * {@link #PRIORITY_OFF},
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH_ADMIN}
     * permission.
     *
     * @param device Paired bluetooth device
     * @param priority
     * @return true if priority is set, false on error
     * @hide
     */
    public boolean setPriority(BluetoothDevice device, int priority) {
        if (DBG) log("setPriority(" + device + ", " + priority + ")");
        if (mService != null && isEnabled() &&
            isValidDevice(device)) {
            if (priority != BluetoothProfile.PRIORITY_OFF &&
                priority != BluetoothProfile.PRIORITY_ON) {
              return false;
            }
            try {
                return mService.setPriority(device, priority);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (mService == null) Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    /**
     * Get the priority of the profile.
     *
     * <p> The priority can be any of:
     * {@link #PRIORITY_AUTO_CONNECT}, {@link #PRIORITY_OFF},
     * {@link #PRIORITY_ON}, {@link #PRIORITY_UNDEFINED}
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param device Bluetooth device
     * @return priority of the device
     * @hide
     */
    public int getPriority(BluetoothDevice device) {
        if (DBG) log("getPriority(" + device + ")");
        if (mService != null && isEnabled() &&
            isValidDevice(device)) {
            try {
                return mService.getPriority(device);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
                return PRIORITY_OFF;
            }
        }
        if (mService == null) Log.w(TAG, "Proxy not attached to service");
        return PRIORITY_OFF;
    }

    /**
     * Start Bluetooth voice recognition. This methods sends the voice
     * recognition AT command to the headset and establishes the
     * audio connection.
     *
     * <p> Users can listen to {@link #ACTION_AUDIO_STATE_CHANGED}.
     * If this function returns true, this intent will be broadcasted with
     * {@link #EXTRA_STATE} set to {@link #STATE_AUDIO_CONNECTING}.
     *
     * <p> {@link #EXTRA_STATE} will transition from
     * {@link #STATE_AUDIO_CONNECTING} to {@link #STATE_AUDIO_CONNECTED} when
     * audio connection is established and to {@link #STATE_AUDIO_DISCONNECTED}
     * in case of failure to establish the audio connection.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param device Bluetooth headset
     * @return false if there is no headset connected of if the
     *               connected headset doesn't support voice recognition
     *               or on error, true otherwise
     */
    public boolean startVoiceRecognition(BluetoothDevice device) {
        if (DBG) log("startVoiceRecognition()");
        if (mService != null && isEnabled() &&
            isValidDevice(device)) {
            try {
                return mService.startVoiceRecognition(device);
            } catch (RemoteException e) {
                Log.e(TAG,  Log.getStackTraceString(new Throwable()));
            }
        }
        if (mService == null) Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    /**
     * Stop Bluetooth Voice Recognition mode, and shut down the
     * Bluetooth audio path.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param device Bluetooth headset
     * @return false if there is no headset connected
     *               or on error, true otherwise
     */
    public boolean stopVoiceRecognition(BluetoothDevice device) {
        if (DBG) log("stopVoiceRecognition()");
        if (mService != null && isEnabled() &&
            isValidDevice(device)) {
            try {
                return mService.stopVoiceRecognition(device);
            } catch (RemoteException e) {
                Log.e(TAG,  Log.getStackTraceString(new Throwable()));
            }
        }
        if (mService == null) Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    /**
     * Check if Bluetooth SCO audio is connected.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param device Bluetooth headset
     * @return true if SCO is connected,
     *         false otherwise or on error
     */
    public boolean isAudioConnected(BluetoothDevice device) {
        if (DBG) log("isAudioConnected()");
        if (mService != null && isEnabled() &&
            isValidDevice(device)) {
            try {
              return mService.isAudioConnected(device);
            } catch (RemoteException e) {
              Log.e(TAG,  Log.getStackTraceString(new Throwable()));
            }
        }
        if (mService == null) Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    /**
     * Get battery usage hint for Bluetooth Headset service.
     * This is a monotonically increasing integer. Wraps to 0 at
     * Integer.MAX_INT, and at boot.
     * Current implementation returns the number of AT commands handled since
     * boot. This is a good indicator for spammy headset/handsfree units that
     * can keep the device awake by polling for cellular status updates. As a
     * rule of thumb, each AT command prevents the CPU from sleeping for 500 ms
     *
     * @param device the bluetooth headset.
     * @return monotonically increasing battery usage hint, or a negative error
     *         code on error
     * @hide
     */
    public int getBatteryUsageHint(BluetoothDevice device) {
        if (DBG) log("getBatteryUsageHint()");
        if (mService != null && isEnabled() &&
            isValidDevice(device)) {
            try {
                return mService.getBatteryUsageHint(device);
            } catch (RemoteException e) {
                Log.e(TAG,  Log.getStackTraceString(new Throwable()));
            }
        }
        if (mService == null) Log.w(TAG, "Proxy not attached to service");
        return -1;
    }

    /**
     * Indicates if current platform supports voice dialing over bluetooth SCO.
     *
     * @return true if voice dialing over bluetooth is supported, false otherwise.
     * @hide
     */
    public static boolean isBluetoothVoiceDialingEnabled(Context context) {
        return context.getResources().getBoolean(
                com.android.internal.R.bool.config_bluetooth_sco_off_call);
    }

    /**
     * Cancel the outgoing connection.
     * Note: This is an internal function and shouldn't be exposed
     *
     * @hide
     */
    public boolean cancelConnectThread() {
        if (DBG) log("cancelConnectThread");
        if (mService != null && isEnabled()) {
            try {
                return mService.cancelConnectThread();
            } catch (RemoteException e) {Log.e(TAG, e.toString());}
        } else {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) Log.d(TAG, Log.getStackTraceString(new Throwable()));
        }
        return false;
    }

    /**
     * Accept the incoming connection.
     * Note: This is an internal function and shouldn't be exposed
     *
     * @hide
     */
    public boolean acceptIncomingConnect(BluetoothDevice device) {
        if (DBG) log("acceptIncomingConnect");
        if (mService != null && isEnabled()) {
            try {
                return mService.acceptIncomingConnect(device);
            } catch (RemoteException e) {Log.e(TAG, e.toString());}
        } else {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) Log.d(TAG, Log.getStackTraceString(new Throwable()));
        }
        return false;
    }

    /**
     * Create the connect thread for the incoming connection.
     * Note: This is an internal function and shouldn't be exposed
     *
     * @hide
     */
    public boolean createIncomingConnect(BluetoothDevice device) {
        if (DBG) log("createIncomingConnect");
        if (mService != null && isEnabled()) {
            try {
                return mService.createIncomingConnect(device);
            } catch (RemoteException e) {Log.e(TAG, e.toString());}
        } else {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) Log.d(TAG, Log.getStackTraceString(new Throwable()));
        }
        return false;
    }

    /**
     * Reject the incoming connection.
     * @hide
     */
    public boolean rejectIncomingConnect(BluetoothDevice device) {
        if (DBG) log("rejectIncomingConnect");
        if (mService != null) {
            try {
                return mService.rejectIncomingConnect(device);
            } catch (RemoteException e) {Log.e(TAG, e.toString());}
        } else {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) Log.d(TAG, Log.getStackTraceString(new Throwable()));
        }
        return false;
    }

    /**
     * Connect to a Bluetooth Headset.
     * Note: This is an internal function and shouldn't be exposed
     *
     * @hide
     */
    public boolean connectHeadsetInternal(BluetoothDevice device) {
        if (DBG) log("connectHeadsetInternal");
        if (mService != null && isEnabled()) {
            try {
                return mService.connectHeadsetInternal(device);
            } catch (RemoteException e) {Log.e(TAG, e.toString());}
        } else {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) Log.d(TAG, Log.getStackTraceString(new Throwable()));
        }
        return false;
    }

    /**
     * Disconnect a Bluetooth Headset.
     * Note: This is an internal function and shouldn't be exposed
     *
     * @hide
     */
    public boolean disconnectHeadsetInternal(BluetoothDevice device) {
        if (DBG) log("disconnectHeadsetInternal");
        if (mService != null && !isDisabled()) {
            try {
                 return mService.disconnectHeadsetInternal(device);
            } catch (RemoteException e) {Log.e(TAG, e.toString());}
        } else {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) Log.d(TAG, Log.getStackTraceString(new Throwable()));
=======
    }

    /**
     * Get the current state of the Bluetooth Headset service.
     * @return One of the STATE_ return codes, or STATE_ERROR if this proxy
     *         object is currently not connected to the Headset service.
     */
    public int getState() {
        if (mService != null) {
            try {
                return mService.getState();
            } catch (RemoteException e) {Log.e(TAG, e.toString());}
        }
        return BluetoothHeadset.STATE_ERROR;
    }

    /**
     * Get the Bluetooth address of the current headset.
     * @return The Bluetooth address, or null if not in connected or connecting
     *         state, or if this proxy object is not connected to the Headset
     *         service.
     */
    public String getHeadsetAddress() {
        if (mService != null) {
            try {
                return mService.getHeadsetAddress();
            } catch (RemoteException e) {Log.e(TAG, e.toString());}
        }
        return null;
    }

    /**
     * Request to initiate a connection to a headset.
     * This call does not block. Fails if a headset is already connecting
     * or connected.
     * Will connect to the last connected headset if address is null.
     * @param address The Bluetooth Address to connect to, or null to connect
     *                to the last connected headset.
     * @param callback A callback with onCreateBondingResult() defined, or
     *                 null.
     * @return        False if there was a problem initiating the connection
     *                procedure, and your callback will not be used. True if
     *                the connection procedure was initiated, in which case
     *                your callback is guarenteed to be called.
     */
    public boolean connectHeadset(String address, IBluetoothHeadsetCallback callback) {
        if (mService != null) {
            try {
                return mService.connectHeadset(address, callback);
            } catch (RemoteException e) {Log.e(TAG, e.toString());}
>>>>>>> 54b6cfa... Initial Contribution
        }
        return false;
    }

    /**
<<<<<<< HEAD
     * Set the audio state of the Headset.
     * Note: This is an internal function and shouldn't be exposed
     *
     * @hide
     */
    public boolean setAudioState(BluetoothDevice device, int state) {
        if (DBG) log("setAudioState");
        if (mService != null && !isDisabled()) {
            try {
                return mService.setAudioState(device, state);
            } catch (RemoteException e) {Log.e(TAG, e.toString());}
        } else {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) Log.d(TAG, Log.getStackTraceString(new Throwable()));
=======
     * Returns true if the specified headset is connected (does not include
     * connecting). Returns false if not connected, or if this proxy object
     * if not currently connected to the headset service.
     */
    public boolean isConnected(String address) {
        if (mService != null) {
            try {
                return mService.isConnected(address);
            } catch (RemoteException e) {Log.e(TAG, e.toString());}
>>>>>>> 54b6cfa... Initial Contribution
        }
        return false;
    }

    /**
<<<<<<< HEAD
     * Get the current audio state of the Headset.
     * Note: This is an internal function and shouldn't be exposed
     *
     * @hide
     */
    public int getAudioState(BluetoothDevice device) {
        if (DBG) log("getAudioState");
        if (mService != null && !isDisabled()) {
            try {
                return mService.getAudioState(device);
            } catch (RemoteException e) {Log.e(TAG, e.toString());}
        } else {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) Log.d(TAG, Log.getStackTraceString(new Throwable()));
        }
        return BluetoothHeadset.STATE_AUDIO_DISCONNECTED;
    }

    /**
     * Initiates a SCO channel connection with the headset (if connected).
     * Also initiates a virtual voice call for Handsfree devices as many devices
     * do not accept SCO audio without a call.
     * This API allows the handsfree device to be used for routing non-cellular
     * call audio.
     *
     * @param device Remote Bluetooth Device
     * @return true if successful, false if there was some error.
     * @hide
     */
    public boolean startScoUsingVirtualVoiceCall(BluetoothDevice device) {
        if (DBG) log("startScoUsingVirtualVoiceCall()");
        if (mService != null && isEnabled() && isValidDevice(device)) {
            try {
                return mService.startScoUsingVirtualVoiceCall(device);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) Log.d(TAG, Log.getStackTraceString(new Throwable()));
        }
        return false;
    }

    /**
     * Terminates an ongoing SCO connection and the associated virtual
     * call.
     *
     * @param device Remote Bluetooth Device
     * @return true if successful, false if there was some error.
     * @hide
     */
    public boolean stopScoUsingVirtualVoiceCall(BluetoothDevice device) {
        if (DBG) log("stopScoUsingVirtualVoiceCall()");
        if (mService != null && isEnabled() && isValidDevice(device)) {
            try {
                return mService.stopScoUsingVirtualVoiceCall(device);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) Log.d(TAG, Log.getStackTraceString(new Throwable()));
        }
        return false;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            if (DBG) Log.d(TAG, "Proxy object connected");
            mService = IBluetoothHeadset.Stub.asInterface(service);

            if (mServiceListener != null) {
                mServiceListener.onServiceConnected(BluetoothProfile.HEADSET, BluetoothHeadset.this);
            }
        }
        public void onServiceDisconnected(ComponentName className) {
            if (DBG) Log.d(TAG, "Proxy object disconnected");
            mService = null;
            if (mServiceListener != null) {
                mServiceListener.onServiceDisconnected(BluetoothProfile.HEADSET);
            }
        }
    };

    private boolean isEnabled() {
       if (mAdapter.getState() == BluetoothAdapter.STATE_ON) return true;
       return false;
    }

    private boolean isDisabled() {
       if (mAdapter.getState() == BluetoothAdapter.STATE_OFF) return true;
       return false;
    }

    private boolean isValidDevice(BluetoothDevice device) {
       if (device == null) return false;

       if (BluetoothAdapter.checkBluetoothAddress(device.getAddress())) return true;
       return false;
    }

    private static void log(String msg) {
        Log.d(TAG, msg);
    }
=======
     * Disconnects the current headset. Currently this call blocks, it may soon
     * be made asynchornous. Returns false if this proxy object is
     * not currently connected to the Headset service.
     */
    public boolean disconnectHeadset() {
        if (mService != null) {
            try {
                mService.disconnectHeadset();
            } catch (RemoteException e) {Log.e(TAG, e.toString());}
        }
        return false;
    }
>>>>>>> 54b6cfa... Initial Contribution
}
