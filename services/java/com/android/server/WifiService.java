/*
<<<<<<< HEAD
 * Copyright (C) 2010 The Android Open Source Project
=======
 * Copyright (C) 2008 The Android Open Source Project
>>>>>>> 54b6cfa... Initial Contribution
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
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
=======
import static android.net.wifi.WifiManager.WIFI_STATE_DISABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_DISABLING;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLING;
import static android.net.wifi.WifiManager.WIFI_STATE_UNKNOWN;

import android.app.AlarmManager;
import android.app.PendingIntent;
>>>>>>> 54b6cfa... Initial Contribution
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
<<<<<<< HEAD
import android.database.ContentObserver;
import android.net.wifi.IWifiManager;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiStateMachine;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiWatchdogStateMachine;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WpsInfo;
import android.net.wifi.WpsResult;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.NetworkInfo.DetailedState;
import android.net.TrafficStats;
import android.os.Binder;
import android.os.Handler;
import android.os.Messenger;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.INetworkManagementService;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.WorkSource;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Slog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.io.FileDescriptor;
import java.io.PrintWriter;

import com.android.internal.app.IBatteryStats;
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.util.AsyncChannel;
import com.android.server.am.BatteryStatsService;
import com.android.internal.R;

/**
 * WifiService handles remote WiFi operation requests by implementing
 * the IWifiManager interface.
 *
 * @hide
 */
//TODO: Clean up multiple locks and implement WifiService
// as a SM to track soft AP/client/adhoc bring up based
// on device idle state, airplane mode and boot.

public class WifiService extends IWifiManager.Stub {
    private static final String TAG = "WifiService";
    private static final boolean DBG = false;

    private final WifiStateMachine mWifiStateMachine;

    private Context mContext;
=======
import android.net.wifi.IWifiManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNative;
import android.net.wifi.WifiStateTracker;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.NetworkStateTracker;
import android.net.DhcpInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.provider.Settings;
import android.provider.Settings.Gservices;
import android.util.Log;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/**
 * WifiService handles remote WiFi operation requests by implementing
 * the IWifiManager interface. It also creates a WifiMonitor to listen
 * for Wifi-related events.
 *
 * {@hide}
 */
public class WifiService extends IWifiManager.Stub {
    private static final String TAG = "WifiService";
    private static final boolean DBG = false;
    private static final Pattern scanResultPattern = Pattern.compile("\t{1,}");
    private final WifiStateTracker mWifiStateTracker;

    private Context mContext;
    private int mWifiState;
>>>>>>> 54b6cfa... Initial Contribution

    private AlarmManager mAlarmManager;
    private PendingIntent mIdleIntent;
    private static final int IDLE_REQUEST = 0;
<<<<<<< HEAD
    private boolean mScreenOff;
    private boolean mDeviceIdle;
    private boolean mEmergencyCallbackMode = false;
    private int mPluggedType;

    private final LockList mLocks = new LockList();
    // some wifi lock statistics
    private int mFullHighPerfLocksAcquired;
    private int mFullHighPerfLocksReleased;
    private int mFullLocksAcquired;
    private int mFullLocksReleased;
    private int mScanLocksAcquired;
    private int mScanLocksReleased;

    /* A mapping from UID to scan count */
    private HashMap<Integer, Integer> mScanCount =
            new HashMap<Integer, Integer>();

    private final List<Multicaster> mMulticasters =
            new ArrayList<Multicaster>();
    private int mMulticastEnabled;
    private int mMulticastDisabled;

    private final IBatteryStats mBatteryStats;

    private boolean mEnableTrafficStatsPoll = false;
    private int mTrafficStatsPollToken = 0;
    private long mTxPkts;
    private long mRxPkts;
    /* Tracks last reported data activity */
    private int mDataActivity;
    private String mInterfaceName;

    /**
     * Interval in milliseconds between polling for traffic
     * statistics
     */
    private static final int POLL_TRAFFIC_STATS_INTERVAL_MSECS = 1000;

    /**
     * See {@link Settings.Secure#WIFI_IDLE_MS}. This is the default value if a
     * Settings.Secure value is not present. This timeout value is chosen as
     * the approximate point at which the battery drain caused by Wi-Fi
     * being enabled but not active exceeds the battery drain caused by
     * re-establishing a connection to the mobile data network.
     */
    private static final long DEFAULT_IDLE_MS = 15 * 60 * 1000; /* 15 minutes */
=======
    private boolean mDeviceIdle;
    private boolean mChargerAttached;
    private final LockList mLocks = new LockList();

    /**
     * See {@link Gservices#WIFI_IDLE_MS}. This is the default value if a
     * Gservices value is not present.
     */
    private long mIdleMillis = 2 * 60 * 1000; /* 2 minutes */

    private static final String WAKELOCK_TAG = "WifiService";

    /**
     * The maximum amount of time to hold the wake lock after a disconnect
     * caused by stopping the driver. Establishing an EDGE connection has been
     * observed to take about 5 seconds under normal circumstances. This
     * provides a bit of extra margin.
     * <p>
     * See {@link Gservices#WIFI_MOBILE_DATA_TRANSITION_WAKELOCK_TIMEOUT_MS}.
     * This is the default value if a Gservices value is not present.
     */
    private int mWakelockTimeout = 8000;
    // Wake lock used by driver-stop operation
    private static PowerManager.WakeLock mDriverStopWakeLock;
    // Wake lock used by other operations
    private static PowerManager.WakeLock mWakeLock;

    private static final int MESSAGE_ENABLE_WIFI      = 0;
    private static final int MESSAGE_DISABLE_WIFI     = 1;
    private static final int MESSAGE_STOP_WIFI        = 2;
    private static final int MESSAGE_START_WIFI       = 3;
    private static final int MESSAGE_RELEASE_WAKELOCK = 4;

    private WifiHandler mWifiHandler;

    /*
     * Map used to keep track of hidden networks presence, which
     * is needed to switch between active and passive scan modes.
     * If there is at least one hidden network that is currently
     * present (enabled), we want to do active scans instead of
     * passive.
     */
    private final Map<Integer, Boolean> mIsHiddenNetworkPresent;
    /*
     * The number of currently present hidden networks. When this
     * counter goes from 0 to 1 or from 1 to 0, we change the
     * scan mode to active or passive respectively. Initially, we
     * set the counter to 0 and we increment it every time we add
     * a new present (enabled) hidden network.
     */
    private int mNumHiddenNetworkPresent;
    /*
     * Whether we change the scan mode is due to a hidden network
     * (in this class, this is always the case)
     */
    private final static boolean SET_DUE_TO_A_HIDDEN_NETWORK = true;

    /*
     * Cache of scan results objects (size is somewhat arbitrary)
     */
    private static final int SCAN_RESULT_CACHE_SIZE = 80;
    private final LinkedHashMap<String, ScanResult> mScanResultCache;

    /*
     * Character buffer used to parse scan results (optimization)
     */
    private static final int SCAN_RESULT_BUFFER_SIZE = 512;
    private char[] mScanResultBuffer;
    private boolean mNeedReconfig;
>>>>>>> 54b6cfa... Initial Contribution

    private static final String ACTION_DEVICE_IDLE =
            "com.android.server.WifiManager.action.DEVICE_IDLE";

<<<<<<< HEAD
    private static final int WIFI_DISABLED                  = 0;
    private static final int WIFI_ENABLED                   = 1;
    /* Wifi enabled while in airplane mode */
    private static final int WIFI_ENABLED_AIRPLANE_OVERRIDE = 2;
    /* Wifi disabled due to airplane mode on */
    private static final int WIFI_DISABLED_AIRPLANE_ON      = 3;

    /* Persisted state that tracks the wifi & airplane interaction from settings */
    private AtomicInteger mPersistWifiState = new AtomicInteger(WIFI_DISABLED);
    /* Tracks current airplane mode state */
    private AtomicBoolean mAirplaneModeOn = new AtomicBoolean(false);
    /* Tracks whether wifi is enabled from WifiStateMachine's perspective */
    private boolean mWifiEnabled;

    private boolean mIsReceiverRegistered = false;


    NetworkInfo mNetworkInfo = new NetworkInfo(ConnectivityManager.TYPE_WIFI, 0, "WIFI", "");

    // Variables relating to the 'available networks' notification
    /**
     * The icon to show in the 'available networks' notification. This will also
     * be the ID of the Notification given to the NotificationManager.
     */
    private static final int ICON_NETWORKS_AVAILABLE =
            com.android.internal.R.drawable.stat_notify_wifi_in_range;
    /**
     * When a notification is shown, we wait this amount before possibly showing it again.
     */
    private final long NOTIFICATION_REPEAT_DELAY_MS;
    /**
     * Whether the user has set the setting to show the 'available networks' notification.
     */
    private boolean mNotificationEnabled;
    /**
     * Observes the user setting to keep {@link #mNotificationEnabled} in sync.
     */
    private NotificationEnabledSettingObserver mNotificationEnabledSettingObserver;
    /**
     * The {@link System#currentTimeMillis()} must be at least this value for us
     * to show the notification again.
     */
    private long mNotificationRepeatTime;
    /**
     * The Notification object given to the NotificationManager.
     */
    private Notification mNotification;
    /**
     * Whether the notification is being shown, as set by us. That is, if the
     * user cancels the notification, we will not receive the callback so this
     * will still be true. We only guarantee if this is false, then the
     * notification is not showing.
     */
    private boolean mNotificationShown;
    /**
     * The number of continuous scans that must occur before consider the
     * supplicant in a scanning state. This allows supplicant to associate with
     * remembered networks that are in the scan results.
     */
    private static final int NUM_SCANS_BEFORE_ACTUALLY_SCANNING = 3;
    /**
     * The number of scans since the last network state change. When this
     * exceeds {@link #NUM_SCANS_BEFORE_ACTUALLY_SCANNING}, we consider the
     * supplicant to actually be scanning. When the network state changes to
     * something other than scanning, we reset this to 0.
     */
    private int mNumScansSinceNetworkStateChange;

    /**
     * Asynchronous channel to WifiStateMachine
     */
    private AsyncChannel mWifiStateMachineChannel;

    /**
     * Clients receiving asynchronous messages
     */
    private List<AsyncChannel> mClients = new ArrayList<AsyncChannel>();

    /**
     * Handles client connections
     */
    private class AsyncServiceHandler extends Handler {

        AsyncServiceHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AsyncChannel.CMD_CHANNEL_HALF_CONNECTED: {
                    if (msg.arg1 == AsyncChannel.STATUS_SUCCESSFUL) {
                        Slog.d(TAG, "New client listening to asynchronous messages");
                        mClients.add((AsyncChannel) msg.obj);
                    } else {
                        Slog.e(TAG, "Client connection failure, error=" + msg.arg1);
                    }
                    break;
                }
                case AsyncChannel.CMD_CHANNEL_DISCONNECTED: {
                    if (msg.arg1 == AsyncChannel.STATUS_SEND_UNSUCCESSFUL) {
                        Slog.d(TAG, "Send failed, client connection lost");
                    } else {
                        Slog.d(TAG, "Client connection lost with reason: " + msg.arg1);
                    }
                    mClients.remove((AsyncChannel) msg.obj);
                    break;
                }
                case AsyncChannel.CMD_CHANNEL_FULL_CONNECTION: {
                    AsyncChannel ac = new AsyncChannel();
                    ac.connect(mContext, this, msg.replyTo);
                    break;
                }
                case WifiManager.ENABLE_TRAFFIC_STATS_POLL: {
                    mEnableTrafficStatsPoll = (msg.arg1 == 1);
                    mTrafficStatsPollToken++;
                    if (mEnableTrafficStatsPoll) {
                        notifyOnDataActivity();
                        sendMessageDelayed(Message.obtain(this, WifiManager.TRAFFIC_STATS_POLL,
                                mTrafficStatsPollToken, 0), POLL_TRAFFIC_STATS_INTERVAL_MSECS);
                    }
                    break;
                }
                case WifiManager.TRAFFIC_STATS_POLL: {
                    if (msg.arg1 == mTrafficStatsPollToken) {
                        notifyOnDataActivity();
                        sendMessageDelayed(Message.obtain(this, WifiManager.TRAFFIC_STATS_POLL,
                                mTrafficStatsPollToken, 0), POLL_TRAFFIC_STATS_INTERVAL_MSECS);
                    }
                    break;
                }
                case WifiManager.CONNECT_NETWORK: {
                    mWifiStateMachine.sendMessage(Message.obtain(msg));
                    break;
                }
                case WifiManager.SAVE_NETWORK: {
                    mWifiStateMachine.sendMessage(Message.obtain(msg));
                    break;
                }
                case WifiManager.FORGET_NETWORK: {
                    mWifiStateMachine.sendMessage(Message.obtain(msg));
                    break;
                }
                case WifiManager.START_WPS: {
                    mWifiStateMachine.sendMessage(Message.obtain(msg));
                    break;
                }
                case WifiManager.CANCEL_WPS: {
                    mWifiStateMachine.sendMessage(Message.obtain(msg));
                    break;
                }
                case WifiManager.DISABLE_NETWORK: {
                    mWifiStateMachine.sendMessage(Message.obtain(msg));
                    break;
                }
                default: {
                    Slog.d(TAG, "WifiServicehandler.handleMessage ignoring msg=" + msg);
                    break;
                }
            }
        }
    }
    private AsyncServiceHandler mAsyncServiceHandler;

    /**
     * Handles interaction with WifiStateMachine
     */
    private class WifiStateMachineHandler extends Handler {
        private AsyncChannel mWsmChannel;

        WifiStateMachineHandler(android.os.Looper looper) {
            super(looper);
            mWsmChannel = new AsyncChannel();
            mWsmChannel.connect(mContext, this, mWifiStateMachine.getHandler());
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AsyncChannel.CMD_CHANNEL_HALF_CONNECTED: {
                    if (msg.arg1 == AsyncChannel.STATUS_SUCCESSFUL) {
                        mWifiStateMachineChannel = mWsmChannel;
                    } else {
                        Slog.e(TAG, "WifiStateMachine connection failure, error=" + msg.arg1);
                        mWifiStateMachineChannel = null;
                    }
                    break;
                }
                case AsyncChannel.CMD_CHANNEL_DISCONNECTED: {
                    Slog.e(TAG, "WifiStateMachine channel lost, msg.arg1 =" + msg.arg1);
                    mWifiStateMachineChannel = null;
                    //Re-establish connection to state machine
                    mWsmChannel.connect(mContext, this, mWifiStateMachine.getHandler());
                    break;
                }
                default: {
                    Slog.d(TAG, "WifiStateMachineHandler.handleMessage ignoring msg=" + msg);
                    break;
                }
            }
        }
    }
    WifiStateMachineHandler mWifiStateMachineHandler;

    /**
     * Temporary for computing UIDS that are responsible for starting WIFI.
     * Protected by mWifiStateTracker lock.
     */
    private final WorkSource mTmpWorkSource = new WorkSource();
    private WifiWatchdogStateMachine mWifiWatchdogStateMachine;

    WifiService(Context context) {
        mContext = context;

        mInterfaceName =  SystemProperties.get("wifi.interface", "wlan0");

        mWifiStateMachine = new WifiStateMachine(mContext, mInterfaceName);
        mWifiStateMachine.enableRssiPolling(true);
        mBatteryStats = BatteryStatsService.getService();

        mAlarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        Intent idleIntent = new Intent(ACTION_DEVICE_IDLE, null);
        mIdleIntent = PendingIntent.getBroadcast(mContext, IDLE_REQUEST, idleIntent, 0);

        mContext.registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        mAirplaneModeOn.set(isAirplaneModeOn());
                        handleAirplaneModeToggled(mAirplaneModeOn.get());
                        updateWifiState();
                    }
                },
                new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED));

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

        mContext.registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                                    WifiManager.WIFI_STATE_DISABLED);

                            mWifiEnabled = (wifiState == WifiManager.WIFI_STATE_ENABLED);

                           // reset & clear notification on any wifi state change
                            resetNotification();
                        } else if (intent.getAction().equals(
                                WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                            mNetworkInfo = (NetworkInfo) intent.getParcelableExtra(
                                    WifiManager.EXTRA_NETWORK_INFO);
                            // reset & clear notification on a network connect & disconnect
                            switch(mNetworkInfo.getDetailedState()) {
                                case CONNECTED:
                                case DISCONNECTED:
                                    evaluateTrafficStatsPolling();
                                    resetNotification();
                                    break;
                            }
                        } else if (intent.getAction().equals(
                                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                            checkAndSetNotification();
                        }
                    }
                }, filter);

        HandlerThread wifiThread = new HandlerThread("WifiService");
        wifiThread.start();
        mAsyncServiceHandler = new AsyncServiceHandler(wifiThread.getLooper());
        mWifiStateMachineHandler = new WifiStateMachineHandler(wifiThread.getLooper());

        // Setting is in seconds
        NOTIFICATION_REPEAT_DELAY_MS = Settings.Secure.getInt(context.getContentResolver(),
                Settings.Secure.WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY, 900) * 1000l;
        mNotificationEnabledSettingObserver = new NotificationEnabledSettingObserver(new Handler());
        mNotificationEnabledSettingObserver.register();
    }

    /**
     * Check if Wi-Fi needs to be enabled and start
     * if needed
     *
     * This function is used only at boot time
     */
    public void checkAndStartWifi() {
        mAirplaneModeOn.set(isAirplaneModeOn());
        mPersistWifiState.set(getPersistedWifiState());
        /* Start if Wi-Fi should be enabled or the saved state indicates Wi-Fi was on */
        boolean wifiEnabled = shouldWifiBeEnabled() || testAndClearWifiSavedState();
        Slog.i(TAG, "WifiService starting up with Wi-Fi " +
                (wifiEnabled ? "enabled" : "disabled"));

        // If we are already disabled (could be due to airplane mode), avoid changing persist
        // state here
        if (wifiEnabled) setWifiEnabled(wifiEnabled);

        mWifiWatchdogStateMachine = WifiWatchdogStateMachine.
               makeWifiWatchdogStateMachine(mContext);

    }

    private boolean testAndClearWifiSavedState() {
        final ContentResolver cr = mContext.getContentResolver();
        int wifiSavedState = 0;
        try {
            wifiSavedState = Settings.Secure.getInt(cr, Settings.Secure.WIFI_SAVED_STATE);
            if(wifiSavedState == 1)
                Settings.Secure.putInt(cr, Settings.Secure.WIFI_SAVED_STATE, 0);
        } catch (Settings.SettingNotFoundException e) {
            ;
        }
        return (wifiSavedState == 1);
    }

    private int getPersistedWifiState() {
        final ContentResolver cr = mContext.getContentResolver();
        try {
            return Settings.Secure.getInt(cr, Settings.Secure.WIFI_ON);
        } catch (Settings.SettingNotFoundException e) {
            Settings.Secure.putInt(cr, Settings.Secure.WIFI_ON, WIFI_DISABLED);
            return WIFI_DISABLED;
        }
    }

    private boolean shouldWifiBeEnabled() {
        if (mAirplaneModeOn.get()) {
            return mPersistWifiState.get() == WIFI_ENABLED_AIRPLANE_OVERRIDE;
        } else {
            return mPersistWifiState.get() != WIFI_DISABLED;
        }
    }

    private void handleWifiToggled(boolean wifiEnabled) {
        boolean airplaneEnabled = mAirplaneModeOn.get() && isAirplaneToggleable();
        if (wifiEnabled) {
            if (airplaneEnabled) {
                persistWifiState(WIFI_ENABLED_AIRPLANE_OVERRIDE);
            } else {
                persistWifiState(WIFI_ENABLED);
            }
        } else {
            // When wifi state is disabled, we do not care
            // if airplane mode is on or not. The scenario of
            // wifi being disabled due to airplane mode being turned on
            // is handled handleAirplaneModeToggled()
            persistWifiState(WIFI_DISABLED);
        }
    }

    private void handleAirplaneModeToggled(boolean airplaneEnabled) {
        if (airplaneEnabled) {
            // Wifi disabled due to airplane on
            if (mWifiEnabled) {
                persistWifiState(WIFI_DISABLED_AIRPLANE_ON);
            }
        } else {
            /* On airplane mode disable, restore wifi state if necessary */
            if (testAndClearWifiSavedState() ||
                    mPersistWifiState.get() == WIFI_ENABLED_AIRPLANE_OVERRIDE) {
                persistWifiState(WIFI_ENABLED);
            }
        }
    }

    private void persistWifiState(int state) {
        final ContentResolver cr = mContext.getContentResolver();
        mPersistWifiState.set(state);
        Settings.Secure.putInt(cr, Settings.Secure.WIFI_ON, state);
=======
    WifiService(Context context, WifiStateTracker tracker) {
        mContext = context;
        mWifiStateTracker = tracker;

        /*
         * Initialize the hidden-networks state
         */
        mIsHiddenNetworkPresent = new HashMap<Integer, Boolean>();
        mNumHiddenNetworkPresent = 0;

        ContentResolver contentResolver = context.getContentResolver();
        mIdleMillis = Gservices.getLong(contentResolver, Gservices.WIFI_IDLE_MS, mIdleMillis);
        mWakelockTimeout = Gservices.getInt(contentResolver,
                Gservices.WIFI_MOBILE_DATA_TRANSITION_WAKELOCK_TIMEOUT_MS, mWakelockTimeout);
        
        mScanResultCache = new LinkedHashMap<String, ScanResult>(
            SCAN_RESULT_CACHE_SIZE, 0.75f, true) {
                /*
                 * Limit the cache size by SCAN_RESULT_CACHE_SIZE
                 * elements
                 */
                public boolean removeEldestEntry(Map.Entry eldest) {
                    return SCAN_RESULT_CACHE_SIZE < this.size();
                }
            };

        mScanResultBuffer = new char [SCAN_RESULT_BUFFER_SIZE];

        HandlerThread wifiThread = new HandlerThread("WifiService");
        wifiThread.start();
        mWifiHandler = new WifiHandler(wifiThread.getLooper());

        mWifiState = WIFI_STATE_DISABLED;
        boolean wifiEnabled = getPersistedWifiEnabled();

        mAlarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        Intent idleIntent = new Intent(ACTION_DEVICE_IDLE, null);
        mIdleIntent = PendingIntent.getBroadcast(mContext, IDLE_REQUEST, idleIntent, 0);

        PowerManager powerManager = (PowerManager)mContext.getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKELOCK_TAG);
        mDriverStopWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKELOCK_TAG);
        mWifiStateTracker.setReleaseWakeLockCallback(
                new Runnable() {
                    public void run() {
                        mWifiHandler.removeMessages(MESSAGE_RELEASE_WAKELOCK);
                        synchronized (mDriverStopWakeLock) {
                            if (mDriverStopWakeLock.isHeld()) {
                                if (DBG) Log.d(TAG, "Releasing driver-stop wakelock " +
                                        mDriverStopWakeLock);
                                mDriverStopWakeLock.release();
                            }
                        }
                    }
                }
        );

        Log.d(TAG, "WifiService starting up with Wi-Fi " +
            (wifiEnabled ? "enabled" : "disabled"));
        registerForBroadcasts();
        setWifiEnabledBlocking(wifiEnabled);
    }

    /**
     * Initializes the hidden networks state. Must be called when we
     * enable Wi-Fi.
     */
    private synchronized void initializeHiddenNetworksState() {
        // First, reset the state
        resetHiddenNetworksState();

        // ... then add networks that are marked as hidden
        List<WifiConfiguration> networks = getConfiguredNetworks();
        if (!networks.isEmpty()) {
            for (WifiConfiguration config : networks) {
                if (config != null && config.hiddenSSID) {
                    addOrUpdateHiddenNetwork(
                        config.networkId,
                        config.status != WifiConfiguration.Status.DISABLED);
                }
            }

        }
    }

    /**
     * Resets the hidden networks state.
     */
    private synchronized void resetHiddenNetworksState() {
        mNumHiddenNetworkPresent = 0;
        mIsHiddenNetworkPresent.clear();
    }

    /**
     * Marks all but netId network as not present.
     */
    private synchronized void markAllHiddenNetworksButOneAsNotPresent(int netId) {
        for (Map.Entry<Integer, Boolean> entry : mIsHiddenNetworkPresent.entrySet()) {
            if (entry != null) {
                Integer networkId = entry.getKey();
                if (networkId != netId) {
                    updateNetworkIfHidden(
                        networkId, false);
                }
            }
        }
    }

    /**
     * Updates the netId network presence status if netId is an existing
     * hidden network.
     */
    private synchronized void updateNetworkIfHidden(int netId, boolean present) {
        if (isHiddenNetwork(netId)) {
            addOrUpdateHiddenNetwork(netId, present);
        }
    }

    /**
     * Updates the netId network presence status if netId is an existing
     * hidden network. If the network does not exist, adds the network.
     */
    private synchronized void addOrUpdateHiddenNetwork(int netId, boolean present) {
        if (0 <= netId) {

            // If we are adding a new entry or modifying an existing one
            Boolean isPresent = mIsHiddenNetworkPresent.get(netId);
            if (isPresent == null || isPresent != present) {
                if (present) {
                    incrementHiddentNetworkPresentCounter();
                } else {
                    // If we add a new hidden network, no need to change
                    // the counter (it must be 0)
                    if (isPresent != null) {
                        decrementHiddentNetworkPresentCounter();
                    }
                }
                mIsHiddenNetworkPresent.put(netId, new Boolean(present));
            }
        } else {
            Log.e(TAG, "addOrUpdateHiddenNetwork(): Invalid (negative) network id!");
        }
    }

    /**
     * Removes the netId network if it is hidden (being kept track of).
     */
    private synchronized void removeNetworkIfHidden(int netId) {
        if (isHiddenNetwork(netId)) {
            removeHiddenNetwork(netId);
        }
    }

    /**
     * Removes the netId network. For the call to be successful, the network
     * must be hidden.
     */
    private synchronized void removeHiddenNetwork(int netId) {
        if (0 <= netId) {
            Boolean isPresent =
                mIsHiddenNetworkPresent.remove(netId);
            if (isPresent != null) {
                // If we remove an existing hidden network that is not
                // present, no need to change the counter
                if (isPresent) {
                    decrementHiddentNetworkPresentCounter();
                }
            } else {
                if (DBG) {
                    Log.d(TAG, "removeHiddenNetwork(): Removing a non-existent network!");
                }
            }
        } else {
            Log.e(TAG, "removeHiddenNetwork(): Invalid (negative) network id!");
        }
    }

    /**
     * Returns true if netId is an existing hidden network.
     */
    private synchronized boolean isHiddenNetwork(int netId) {
        return mIsHiddenNetworkPresent.containsKey(netId);
    }

    /**
     * Increments the present (enabled) hidden networks counter. If the
     * counter value goes from 0 to 1, changes the scan mode to active.
     */
    private void incrementHiddentNetworkPresentCounter() {
        ++mNumHiddenNetworkPresent;
        if (1 == mNumHiddenNetworkPresent) {
            // Switch the scan mode to "active"
            mWifiStateTracker.setScanMode(true, SET_DUE_TO_A_HIDDEN_NETWORK);
        }
    }

    /**
     * Decrements the present (enabled) hidden networks counter. If the
     * counter goes from 1 to 0, changes the scan mode back to passive.
     */
    private void decrementHiddentNetworkPresentCounter() {
        if (0 < mNumHiddenNetworkPresent) {
            --mNumHiddenNetworkPresent;
            if (0 == mNumHiddenNetworkPresent) {
                // Switch the scan mode to "passive"
                mWifiStateTracker.setScanMode(false, SET_DUE_TO_A_HIDDEN_NETWORK);
            }
        } else {
            Log.e(TAG, "Hidden-network counter invariant violation!");
        }
    }

    private boolean getPersistedWifiEnabled() {
        final ContentResolver cr = mContext.getContentResolver();
        try {
            return Settings.System.getInt(cr, Settings.System.WIFI_ON) == 1;
        } catch (Settings.SettingNotFoundException e) {
            Settings.System.putInt(cr, Settings.System.WIFI_ON, 0);
            return false;
        }
    }

    private void persistWifiEnabled(boolean enabled) {
        final ContentResolver cr = mContext.getContentResolver();
        Settings.System.putInt(cr, Settings.System.WIFI_ON, enabled ? 1 : 0);
    }

    NetworkStateTracker getNetworkStateTracker() {
        return mWifiStateTracker;
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * see {@link android.net.wifi.WifiManager#pingSupplicant()}
<<<<<<< HEAD
     * @return {@code true} if the operation succeeds, {@code false} otherwise
     */
    public boolean pingSupplicant() {
        enforceAccessPermission();
        if (mWifiStateMachineChannel != null) {
            return mWifiStateMachine.syncPingSupplicant(mWifiStateMachineChannel);
        } else {
            Slog.e(TAG, "mWifiStateMachineChannel is not initialized");
            return false;
=======
     * @return {@code true} if the operation succeeds
     */
    public boolean pingSupplicant() {
        enforceChangePermission();
        synchronized (mWifiStateTracker) {
            return WifiNative.pingCommand();
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
     * see {@link android.net.wifi.WifiManager#startScan()}
<<<<<<< HEAD
     */
    public void startScan(boolean forceActive) {
        enforceChangePermission();

        int uid = Binder.getCallingUid();
        int count = 0;
        synchronized (mScanCount) {
            if (mScanCount.containsKey(uid)) {
                count = mScanCount.get(uid);
            }
            mScanCount.put(uid, ++count);
        }
        mWifiStateMachine.startScan(forceActive);
    }

    private void enforceAccessPermission() {
        mContext.enforceCallingOrSelfPermission(android.Manifest.permission.ACCESS_WIFI_STATE,
                                                "WifiService");
    }

    private void enforceChangePermission() {
        mContext.enforceCallingOrSelfPermission(android.Manifest.permission.CHANGE_WIFI_STATE,
                                                "WifiService");

    }

    private void enforceMulticastChangePermission() {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.CHANGE_WIFI_MULTICAST_STATE,
                "WifiService");
    }

    /**
     * see {@link android.net.wifi.WifiManager#setWifiEnabled(boolean)}
     * @param enable {@code true} to enable, {@code false} to disable.
     * @return {@code true} if the enable/disable operation was
     *         started or is already in the queue.
     */
    public synchronized boolean setWifiEnabled(boolean enable) {
        enforceChangePermission();
        Slog.d(TAG, "setWifiEnabled: " + enable + " pid=" + Binder.getCallingPid()
                    + ", uid=" + Binder.getCallingUid());
        if (DBG) {
            Slog.e(TAG, "Invoking mWifiStateMachine.setWifiEnabled\n");
        }

        if (enable) {
            reportStartWorkSource();
        }
        mWifiStateMachine.setWifiEnabled(enable);

        /*
         * Caller might not have WRITE_SECURE_SETTINGS,
         * only CHANGE_WIFI_STATE is enforced
         */

        long ident = Binder.clearCallingIdentity();
        handleWifiToggled(enable);
        Binder.restoreCallingIdentity(ident);

        if (enable) {
            if (!mIsReceiverRegistered) {
                registerForBroadcasts();
                mIsReceiverRegistered = true;
            }
        } else if (mIsReceiverRegistered) {
            mContext.unregisterReceiver(mReceiver);
            mIsReceiverRegistered = false;
=======
     * @return {@code true} if the operation succeeds
     */
    public boolean startScan() {
        enforceChangePermission();
        synchronized (mWifiStateTracker) {
            switch (mWifiStateTracker.getSupplicantState()) {
                case DISCONNECTED:
                case INACTIVE:
                case SCANNING:
                case DORMANT:
                    break;
                default:
                    WifiNative.setScanResultHandlingCommand(
                            WifiStateTracker.SUPPL_SCAN_HANDLING_LIST_ONLY);
                    break;
            }
            return WifiNative.scanCommand();
        }
    }

    /**
     * see {@link android.net.wifi.WifiManager#setWifiEnabled(boolean)}
     * @return {@code true} if the enable/disable operation was
     *         started or is already in the queue.
     */
    public boolean setWifiEnabled(boolean enable) {
        enforceChangePermission();
        if (mWifiHandler == null) return false;

        /*
         * Remove any enable/disable Wi-Fi messages we may have in the queue
         * before adding a new one
         */
        synchronized (mWifiHandler) {
            mWakeLock.acquire();

            mWifiHandler.obtainMessage(
                enable ? MESSAGE_ENABLE_WIFI : MESSAGE_DISABLE_WIFI).sendToTarget();
        }

        return true;
    }

    /**
     * Enables/disables Wi-Fi synchronously.
     * @param enable {@code true} to turn Wi-Fi on, {@code false} to turn it off.
     * @return {@code true} if the operation succeeds (or if the existing state
     *         is the same as the requested state)
     */
    private boolean setWifiEnabledBlocking(boolean enable) {
        final int eventualWifiState = enable ? WIFI_STATE_ENABLED : WIFI_STATE_DISABLED;

        if (mWifiState == eventualWifiState) {
            return true;
        }
        if (enable && isAirplaneModeOn()) {
            return false;
        }

        updateWifiState(enable ? WIFI_STATE_ENABLING : WIFI_STATE_DISABLING);

        if (enable) {
            if (!WifiNative.loadDriver()) {
                Log.e(TAG, "Failed to load Wi-Fi driver.");
                updateWifiState(WIFI_STATE_UNKNOWN);
                return false;
            }
            if (!WifiNative.startSupplicant()) {
                WifiNative.unloadDriver();
                Log.e(TAG, "Failed to start supplicant daemon.");
                updateWifiState(WIFI_STATE_UNKNOWN);
                return false;
            }
            mWifiStateTracker.startEventLoop();
        } else {

            // Remove notification (it will no-op if it isn't visible)
            mWifiStateTracker.setNotificationVisible(false, 0, false, 0);

            boolean failedToStopSupplicantOrUnloadDriver = false;
            if (!WifiNative.stopSupplicant()) {
                Log.e(TAG, "Failed to stop supplicant daemon.");
                updateWifiState(WIFI_STATE_UNKNOWN);
                failedToStopSupplicantOrUnloadDriver = true;
            }
            
            // We must reset the interface before we unload the driver
            mWifiStateTracker.resetInterface();
            
            if (!WifiNative.unloadDriver()) {
                Log.e(TAG, "Failed to unload Wi-Fi driver.");
                if (!failedToStopSupplicantOrUnloadDriver) {
                    updateWifiState(WIFI_STATE_UNKNOWN);
                    failedToStopSupplicantOrUnloadDriver = true;
                }
            }
            if (failedToStopSupplicantOrUnloadDriver) {
                return false;
            }
        }

        // Success!

        persistWifiEnabled(enable);
        updateWifiState(eventualWifiState);

        /*
         * Initiliaze the hidden networs state if the Wi-Fi is being turned on.
         */
        if (enable) {
            initializeHiddenNetworksState();
>>>>>>> 54b6cfa... Initial Contribution
        }

        return true;
    }

<<<<<<< HEAD
    /**
     * see {@link WifiManager#getWifiState()}
     * @return One of {@link WifiManager#WIFI_STATE_DISABLED},
     *         {@link WifiManager#WIFI_STATE_DISABLING},
     *         {@link WifiManager#WIFI_STATE_ENABLED},
     *         {@link WifiManager#WIFI_STATE_ENABLING},
     *         {@link WifiManager#WIFI_STATE_UNKNOWN}
     */
    public int getWifiEnabledState() {
        enforceAccessPermission();
        return mWifiStateMachine.syncGetWifiState();
    }

    /**
     * see {@link android.net.wifi.WifiManager#setWifiApEnabled(WifiConfiguration, boolean)}
     * @param wifiConfig SSID, security and channel details as
     *        part of WifiConfiguration
     * @param enabled true to enable and false to disable
     */
    public void setWifiApEnabled(WifiConfiguration wifiConfig, boolean enabled) {
        enforceChangePermission();
        mWifiStateMachine.setWifiApEnabled(wifiConfig, enabled);
    }

    /**
     * see {@link WifiManager#getWifiApState()}
     * @return One of {@link WifiManager#WIFI_AP_STATE_DISABLED},
     *         {@link WifiManager#WIFI_AP_STATE_DISABLING},
     *         {@link WifiManager#WIFI_AP_STATE_ENABLED},
     *         {@link WifiManager#WIFI_AP_STATE_ENABLING},
     *         {@link WifiManager#WIFI_AP_STATE_FAILED}
     */
    public int getWifiApEnabledState() {
        enforceAccessPermission();
        return mWifiStateMachine.syncGetWifiApState();
    }

    /**
     * see {@link WifiManager#getWifiApConfiguration()}
     * @return soft access point configuration
     */
    public WifiConfiguration getWifiApConfiguration() {
        enforceAccessPermission();
        return mWifiStateMachine.syncGetWifiApConfiguration();
    }

    /**
     * see {@link WifiManager#setWifiApConfiguration(WifiConfiguration)}
     * @param wifiConfig WifiConfiguration details for soft access point
     */
    public void setWifiApConfiguration(WifiConfiguration wifiConfig) {
        enforceChangePermission();
        if (wifiConfig == null)
            return;
        mWifiStateMachine.setWifiApConfiguration(wifiConfig);
=======
    private void updateWifiState(int wifiState) {
        final int previousWifiState = mWifiState;

        // Update state
        mWifiState = wifiState;

        // Broadcast
        final Intent intent = new Intent(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intent.putExtra(WifiManager.EXTRA_WIFI_STATE, wifiState);
        intent.putExtra(WifiManager.EXTRA_PREVIOUS_WIFI_STATE, previousWifiState);
        mContext.sendStickyBroadcast(intent);
    }

    private void enforceAccessPermission() {
        mContext.enforceCallingOrSelfPermission(android.Manifest.permission.ACCESS_WIFI_STATE,
                                                "WifiService");
    }

    private void enforceChangePermission() {
        mContext.enforceCallingOrSelfPermission(android.Manifest.permission.CHANGE_WIFI_STATE,
                                                "WifiService");

    }

    /**
     * see {@link WifiManager#getWifiState()}
     * @return One of {@link WifiManager#WIFI_STATE_DISABLED},
     *         {@link WifiManager#WIFI_STATE_DISABLING},
     *         {@link WifiManager#WIFI_STATE_ENABLED},
     *         {@link WifiManager#WIFI_STATE_ENABLING},
     *         {@link WifiManager#WIFI_STATE_UNKNOWN}
     */
    public int getWifiState() {
        enforceAccessPermission();
        return mWifiState;
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * see {@link android.net.wifi.WifiManager#disconnect()}
<<<<<<< HEAD
     */
    public void disconnect() {
        enforceChangePermission();
        mWifiStateMachine.disconnectCommand();
=======
     * @return {@code true} if the operation succeeds
     */
    public boolean disconnect() {
        enforceChangePermission();
        synchronized (mWifiStateTracker) {
            return WifiNative.disconnectCommand();
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * see {@link android.net.wifi.WifiManager#reconnect()}
<<<<<<< HEAD
     */
    public void reconnect() {
        enforceChangePermission();
        mWifiStateMachine.reconnectCommand();
=======
     * @return {@code true} if the operation succeeds
     */
    public boolean reconnect() {
        enforceChangePermission();
        synchronized (mWifiStateTracker) {
            return WifiNative.reconnectCommand();
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * see {@link android.net.wifi.WifiManager#reassociate()}
<<<<<<< HEAD
     */
    public void reassociate() {
        enforceChangePermission();
        mWifiStateMachine.reassociateCommand();
=======
     * @return {@code true} if the operation succeeds
     */
    public boolean reassociate() {
        enforceChangePermission();
        synchronized (mWifiStateTracker) {
            return WifiNative.reassociateCommand();
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * see {@link android.net.wifi.WifiManager#getConfiguredNetworks()}
     * @return the list of configured networks
     */
    public List<WifiConfiguration> getConfiguredNetworks() {
        enforceAccessPermission();
<<<<<<< HEAD
        if (mWifiStateMachineChannel != null) {
            return mWifiStateMachine.syncGetConfiguredNetworks(mWifiStateMachineChannel);
        } else {
            Slog.e(TAG, "mWifiStateMachineChannel is not initialized");
            return null;
        }
    }

    /**
     * see {@link android.net.wifi.WifiManager#addOrUpdateNetwork(WifiConfiguration)}
     * @return the supplicant-assigned identifier for the new or updated
     * network if the operation succeeds, or {@code -1} if it fails
     */
    public int addOrUpdateNetwork(WifiConfiguration config) {
        enforceChangePermission();
        if (mWifiStateMachineChannel != null) {
            return mWifiStateMachine.syncAddOrUpdateNetwork(mWifiStateMachineChannel, config);
        } else {
            Slog.e(TAG, "mWifiStateMachineChannel is not initialized");
            return -1;
        }
    }

     /**
=======
        String listStr;
        /*
         * We don't cache the list, because we want to allow
         * for the possibility that the configuration file
         * has been modified through some external means,
         * such as the wpa_cli command line program.
         */
        synchronized (mWifiStateTracker) {
            listStr = WifiNative.listNetworksCommand();
        }
        List<WifiConfiguration> networks =
            new ArrayList<WifiConfiguration>();
        if (listStr == null)
            return networks;

        String[] lines = listStr.split("\n");
        // Skip the first line, which is a header
       for (int i = 1; i < lines.length; i++) {
           String[] result = lines[i].split("\t");
           // network-id | ssid | bssid | flags
           WifiConfiguration config = new WifiConfiguration();
           try {
               config.networkId = Integer.parseInt(result[0]);
           } catch(NumberFormatException e) {
               continue;
           }
           if (result.length > 3) {
               if (result[3].indexOf("[CURRENT]") != -1)
                   config.status = WifiConfiguration.Status.CURRENT;
               else if (result[3].indexOf("[DISABLED]") != -1)
                   config.status = WifiConfiguration.Status.DISABLED;
               else
                   config.status = WifiConfiguration.Status.ENABLED;
           } else
               config.status = WifiConfiguration.Status.ENABLED;
           synchronized (mWifiStateTracker) {
               readNetworkVariables(config);
           }
           networks.add(config);
       }

        return networks;
    }

    /**
     * Read the variables from the supplicant daemon that are needed to
     * fill in the WifiConfiguration object.
     * <p/>
     * The caller must hold the synchronization monitor.
     * @param config the {@link WifiConfiguration} object to be filled in.
     */
    private static void readNetworkVariables(WifiConfiguration config) {

        int netId = config.networkId;
        if (netId < 0)
            return;

        /*
         * TODO: maybe should have a native method that takes an array of
         * variable names and returns an array of values. But we'd still
         * be doing a round trip to the supplicant daemon for each variable.
         */
        String value;

        value = WifiNative.getNetworkVariableCommand(netId, WifiConfiguration.ssidVarName);
        if (!TextUtils.isEmpty(value)) {
            config.SSID = value;
        } else {
            config.SSID = null;
        }

        value = WifiNative.getNetworkVariableCommand(netId, WifiConfiguration.bssidVarName);
        if (!TextUtils.isEmpty(value)) {
            config.BSSID = value;
        } else {
            config.BSSID = null;
        }

        value = WifiNative.getNetworkVariableCommand(netId, WifiConfiguration.priorityVarName);
        config.priority = -1;
        if (!TextUtils.isEmpty(value)) {
            try {
                config.priority = Integer.parseInt(value);
            } catch (NumberFormatException e) {
            }
        }

        value = WifiNative.getNetworkVariableCommand(netId, WifiConfiguration.hiddenSSIDVarName);
        config.hiddenSSID = false;
        if (!TextUtils.isEmpty(value)) {
            try {
                config.hiddenSSID = Integer.parseInt(value) != 0;
            } catch (NumberFormatException e) {
            }
        }

        value = WifiNative.getNetworkVariableCommand(netId, WifiConfiguration.wepTxKeyIdxVarName);
        config.wepTxKeyIndex = -1;
        if (!TextUtils.isEmpty(value)) {
            try {
                config.wepTxKeyIndex = Integer.parseInt(value);
            } catch (NumberFormatException e) {
            }
        }

        /*
         * Get up to 4 WEP keys. Note that the actual keys are not passed back,
         * just a "*" if the key is set, or the null string otherwise.
         */
        for (int i = 0; i < 4; i++) {
            value = WifiNative.getNetworkVariableCommand(netId, WifiConfiguration.wepKeyVarNames[i]);
            if (!TextUtils.isEmpty(value)) {
                config.wepKeys[i] = value;
            } else {
                config.wepKeys[i] = null;
            }
        }

        /*
         * Get the private shared key. Note that the actual keys are not passed back,
         * just a "*" if the key is set, or the null string otherwise.
         */
        value = WifiNative.getNetworkVariableCommand(netId, WifiConfiguration.pskVarName);
        if (!TextUtils.isEmpty(value)) {
            config.preSharedKey = value;
        } else {
            config.preSharedKey = null;
        }

        value = WifiNative.getNetworkVariableCommand(config.networkId,
                WifiConfiguration.Protocol.varName);
        if (!TextUtils.isEmpty(value)) {
            String vals[] = value.split(" ");
            for (String val : vals) {
                int index =
                    lookupString(val, WifiConfiguration.Protocol.strings);
                if (0 <= index) {
                    config.allowedProtocols.set(index);
                }
            }
        }

        value = WifiNative.getNetworkVariableCommand(config.networkId,
                WifiConfiguration.KeyMgmt.varName);
        if (!TextUtils.isEmpty(value)) {
            String vals[] = value.split(" ");
            for (String val : vals) {
                int index =
                    lookupString(val, WifiConfiguration.KeyMgmt.strings);
                if (0 <= index) {
                    config.allowedKeyManagement.set(index);
                }
            }
        }

        value = WifiNative.getNetworkVariableCommand(config.networkId,
                WifiConfiguration.AuthAlgorithm.varName);
        if (!TextUtils.isEmpty(value)) {
            String vals[] = value.split(" ");
            for (String val : vals) {
                int index =
                    lookupString(val, WifiConfiguration.AuthAlgorithm.strings);
                if (0 <= index) {
                    config.allowedAuthAlgorithms.set(index);
                }
            }
        }

        value = WifiNative.getNetworkVariableCommand(config.networkId,
                WifiConfiguration.PairwiseCipher.varName);
        if (!TextUtils.isEmpty(value)) {
            String vals[] = value.split(" ");
            for (String val : vals) {
                int index =
                    lookupString(val, WifiConfiguration.PairwiseCipher.strings);
                if (0 <= index) {
                    config.allowedPairwiseCiphers.set(index);
                }
            }
        }

        value = WifiNative.getNetworkVariableCommand(config.networkId,
                WifiConfiguration.GroupCipher.varName);
        if (!TextUtils.isEmpty(value)) {
            String vals[] = value.split(" ");
            for (String val : vals) {
                int index =
                    lookupString(val, WifiConfiguration.GroupCipher.strings);
                if (0 <= index) {
                    config.allowedGroupCiphers.set(index);
                }
            }
        }
    }

    /**
     * see {@link android.net.wifi.WifiManager#addOrUpdateNetwork(WifiConfiguration)}
     * @return the supplicant-assigned identifier for the new or updated
     * network if the operation succeeds, or {@code -1} if it fails
     */
    public synchronized int addOrUpdateNetwork(WifiConfiguration config) {
        enforceChangePermission();
        /*
         * If the supplied networkId is -1, we create a new empty
         * network configuration. Otherwise, the networkId should
         * refer to an existing configuration.
         */
        int netId = config.networkId;
        boolean newNetwork = netId == -1;
        boolean doReconfig = false;
        int currentPriority;
        // networkId of -1 means we want to create a new network
        if (newNetwork) {
            netId = WifiNative.addNetworkCommand();
            if (netId < 0) {
                if (DBG) {
                    Log.d(TAG, "Failed to add a network!");
                }
                return -1;
            }
            doReconfig = true;
        } else {
            String priorityVal = WifiNative.getNetworkVariableCommand(netId, WifiConfiguration.priorityVarName);
            currentPriority = -1;
            if (!TextUtils.isEmpty(priorityVal)) {
                try {
                    currentPriority = Integer.parseInt(priorityVal);
                } catch (NumberFormatException e) {
                }
            }
            doReconfig = currentPriority != config.priority;
        }
        mNeedReconfig = mNeedReconfig || doReconfig;

        /*
         * If we have hidden networks, we may have to change the scan mode
         */
        if (config.hiddenSSID) {
            // Mark the network as present unless it is disabled
            addOrUpdateHiddenNetwork(
                netId, config.status != WifiConfiguration.Status.DISABLED);
        }

        setVariables: {
            /*
             * Note that if a networkId for a non-existent network
             * was supplied, then the first setNetworkVariableCommand()
             * will fail, so we don't bother to make a separate check
             * for the validity of the ID up front.
             */

            if (config.SSID != null &&
                !WifiNative.setNetworkVariableCommand(
                    netId,
                    WifiConfiguration.ssidVarName,
                    config.SSID)) {
                if (DBG) {
                    Log.d(TAG, "failed to set SSID: "+config.SSID);
                }
                break setVariables;
            }

            if (config.BSSID != null &&
                !WifiNative.setNetworkVariableCommand(
                    netId,
                    WifiConfiguration.bssidVarName,
                    config.BSSID)) {
                if (DBG) {
                    Log.d(TAG, "failed to set BSSID: "+config.BSSID);
                }
                break setVariables;
            }

            String allowedKeyManagementString =
                makeString(config.allowedKeyManagement, WifiConfiguration.KeyMgmt.strings);
            if (config.allowedKeyManagement.cardinality() != 0 &&
                !WifiNative.setNetworkVariableCommand(
                    netId,
                    WifiConfiguration.KeyMgmt.varName,
                    allowedKeyManagementString)) {
                if (DBG) {
                    Log.d(TAG, "failed to set key_mgmt: "+
                          allowedKeyManagementString);
                }
                break setVariables;
            }

            String allowedProtocolsString =
                makeString(config.allowedProtocols, WifiConfiguration.Protocol.strings);
            if (config.allowedProtocols.cardinality() != 0 &&
                !WifiNative.setNetworkVariableCommand(
                    netId,
                    WifiConfiguration.Protocol.varName,
                    allowedProtocolsString)) {
                if (DBG) {
                    Log.d(TAG, "failed to set proto: "+
                          allowedProtocolsString);
                }
                break setVariables;
            }

            String allowedAuthAlgorithmsString =
                makeString(config.allowedAuthAlgorithms, WifiConfiguration.AuthAlgorithm.strings);
            if (config.allowedAuthAlgorithms.cardinality() != 0 &&
                !WifiNative.setNetworkVariableCommand(
                    netId,
                    WifiConfiguration.AuthAlgorithm.varName,
                    allowedAuthAlgorithmsString)) {
                if (DBG) {
                    Log.d(TAG, "failed to set auth_alg: "+
                          allowedAuthAlgorithmsString);
                }
                break setVariables;
            }

            String allowedPairwiseCiphersString =
                makeString(config.allowedPairwiseCiphers, WifiConfiguration.PairwiseCipher.strings);
            if (config.allowedPairwiseCiphers.cardinality() != 0 &&
                !WifiNative.setNetworkVariableCommand(
                    netId,
                    WifiConfiguration.PairwiseCipher.varName,
                    allowedPairwiseCiphersString)) {
                if (DBG) {
                    Log.d(TAG, "failed to set pairwise: "+
                          allowedPairwiseCiphersString);
                }
                break setVariables;
            }

            String allowedGroupCiphersString =
                makeString(config.allowedGroupCiphers, WifiConfiguration.GroupCipher.strings);
            if (config.allowedGroupCiphers.cardinality() != 0 &&
                !WifiNative.setNetworkVariableCommand(
                    netId,
                    WifiConfiguration.GroupCipher.varName,
                    allowedGroupCiphersString)) {
                if (DBG) {
                    Log.d(TAG, "failed to set group: "+
                          allowedGroupCiphersString);
                }
                break setVariables;
            }

            // Prevent client screw-up by passing in a WifiConfiguration we gave it
            // by preventing "*" as a key.
            if (config.preSharedKey != null && !config.preSharedKey.equals("*") &&
                !WifiNative.setNetworkVariableCommand(
                    netId,
                    WifiConfiguration.pskVarName,
                    config.preSharedKey)) {
                if (DBG) {
                    Log.d(TAG, "failed to set psk: "+config.preSharedKey);
                }
                break setVariables;
            }

            boolean hasSetKey = false;
            if (config.wepKeys != null) {
                for (int i = 0; i < config.wepKeys.length; i++) {
                    // Prevent client screw-up by passing in a WifiConfiguration we gave it
                    // by preventing "*" as a key.
                    if (config.wepKeys[i] != null && !config.wepKeys[i].equals("*")) {
                        if (!WifiNative.setNetworkVariableCommand(
                                netId,
                                WifiConfiguration.wepKeyVarNames[i],
                                config.wepKeys[i])) {
                            if (DBG) {
                                Log.d(TAG,
                                      "failed to set wep_key"+i+": " +
                                      config.wepKeys[i]);
                            }
                            break setVariables;
                        }
                        hasSetKey = true;
                    }
                }
            }

            if (hasSetKey) {
                if (!WifiNative.setNetworkVariableCommand(
                        netId,
                        WifiConfiguration.wepTxKeyIdxVarName,
                        Integer.toString(config.wepTxKeyIndex))) {
                    if (DBG) {
                        Log.d(TAG,
                              "failed to set wep_tx_keyidx: "+
                              config.wepTxKeyIndex);
                    }
                    break setVariables;
                }
            }

            if (!WifiNative.setNetworkVariableCommand(
                    netId,
                    WifiConfiguration.priorityVarName,
                    Integer.toString(config.priority))) {
                if (DBG) {
                    Log.d(TAG, config.SSID + ": failed to set priority: "
                          +config.priority);
                }
                break setVariables;
            }

            if (config.hiddenSSID && !WifiNative.setNetworkVariableCommand(
                    netId,
                    WifiConfiguration.hiddenSSIDVarName,
                    Integer.toString(config.hiddenSSID ? 1 : 0))) {
                if (DBG) {
                    Log.d(TAG, config.SSID + ": failed to set hiddenSSID: "+
                          config.hiddenSSID);
                }
                break setVariables;
            }

            return netId;
        }

        /*
         * For an update, if one of the setNetworkVariable operations fails,
         * we might want to roll back all the changes already made. But the
         * chances are that if anything is going to go wrong, it'll happen
         * the first time we try to set one of the variables.
         */
        if (newNetwork) {
            removeNetwork(netId);
            if (DBG) {
                Log.d(TAG,
                      "Failed to set a network variable, removed network: "
                      + netId);
            }
        }
        return -1;
    }

    private static String makeString(BitSet set, String[] strings) {
        StringBuffer buf = new StringBuffer();
        int nextSetBit = -1;

        /* Make sure all set bits are in [0, strings.length) to avoid
         * going out of bounds on strings.  (Shouldn't happen, but...) */
        set = set.get(0, strings.length);

        while ((nextSetBit = set.nextSetBit(nextSetBit + 1)) != -1) {
            buf.append(strings[nextSetBit].replace('_', '-')).append(' ');
        }

        // remove trailing space
        if (set.cardinality() > 0) {
            buf.setLength(buf.length() - 1);
        }

        return buf.toString();
    }

    private static int lookupString(String string, String[] strings) {
        int size = strings.length;

        string = string.replace('-', '_');

        for (int i = 0; i < size; i++)
            if (string.equals(strings[i]))
                return i;

        if (DBG) {
            // if we ever get here, we should probably add the
            // value to WifiConfiguration to reflect that it's
            // supported by the WPA supplicant
            Log.w(TAG, "Failed to look-up a string: " + string);
        }

        return -1;
    }

    /**
>>>>>>> 54b6cfa... Initial Contribution
     * See {@link android.net.wifi.WifiManager#removeNetwork(int)}
     * @param netId the integer that identifies the network configuration
     * to the supplicant
     * @return {@code true} if the operation succeeded
     */
    public boolean removeNetwork(int netId) {
        enforceChangePermission();
<<<<<<< HEAD
        if (mWifiStateMachineChannel != null) {
            return mWifiStateMachine.syncRemoveNetwork(mWifiStateMachineChannel, netId);
        } else {
            Slog.e(TAG, "mWifiStateMachineChannel is not initialized");
            return false;
=======

        /*
         * If we have hidden networks, we may have to change the scan mode
         */
        removeNetworkIfHidden(netId);

        synchronized (mWifiStateTracker) {
            return WifiNative.removeNetworkCommand(netId);
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
     * See {@link android.net.wifi.WifiManager#enableNetwork(int, boolean)}
     * @param netId the integer that identifies the network configuration
     * to the supplicant
     * @param disableOthers if true, disable all other networks.
     * @return {@code true} if the operation succeeded
     */
    public boolean enableNetwork(int netId, boolean disableOthers) {
        enforceChangePermission();
<<<<<<< HEAD
        if (mWifiStateMachineChannel != null) {
            return mWifiStateMachine.syncEnableNetwork(mWifiStateMachineChannel, netId,
                    disableOthers);
        } else {
            Slog.e(TAG, "mWifiStateMachineChannel is not initialized");
            return false;
=======

        /*
         * If we have hidden networks, we may have to change the scan mode
         */
         synchronized(this) {
             if (disableOthers) {
                 markAllHiddenNetworksButOneAsNotPresent(netId);
             }
             updateNetworkIfHidden(netId, true);
         }

        synchronized (mWifiStateTracker) {
            return WifiNative.enableNetworkCommand(netId, disableOthers);
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
     * See {@link android.net.wifi.WifiManager#disableNetwork(int)}
     * @param netId the integer that identifies the network configuration
     * to the supplicant
     * @return {@code true} if the operation succeeded
     */
    public boolean disableNetwork(int netId) {
        enforceChangePermission();
<<<<<<< HEAD
        if (mWifiStateMachineChannel != null) {
            return mWifiStateMachine.syncDisableNetwork(mWifiStateMachineChannel, netId);
        } else {
            Slog.e(TAG, "mWifiStateMachineChannel is not initialized");
            return false;
=======

        /*
         * If we have hidden networks, we may have to change the scan mode
         */
        updateNetworkIfHidden(netId, false);

        synchronized (mWifiStateTracker) {
            return WifiNative.disableNetworkCommand(netId);
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
     * See {@link android.net.wifi.WifiManager#getConnectionInfo()}
     * @return the Wi-Fi information, contained in {@link WifiInfo}.
     */
    public WifiInfo getConnectionInfo() {
        enforceAccessPermission();
        /*
         * Make sure we have the latest information, by sending
         * a status request to the supplicant.
         */
<<<<<<< HEAD
        return mWifiStateMachine.syncRequestConnectionInfo();
=======
        return mWifiStateTracker.requestConnectionInfo();
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Return the results of the most recent access point scan, in the form of
     * a list of {@link ScanResult} objects.
     * @return the list of results
     */
    public List<ScanResult> getScanResults() {
        enforceAccessPermission();
<<<<<<< HEAD
        return mWifiStateMachine.syncGetScanResultsList();
    }

    /**
     * Tell the supplicant to persist the current list of configured networks.
     * @return {@code true} if the operation succeeded
     *
     * TODO: deprecate this
     */
    public boolean saveConfiguration() {
        boolean result = true;
        enforceChangePermission();
        if (mWifiStateMachineChannel != null) {
            return mWifiStateMachine.syncSaveConfig(mWifiStateMachineChannel);
        } else {
            Slog.e(TAG, "mWifiStateMachineChannel is not initialized");
            return false;
        }
    }

    /**
     * Set the country code
     * @param countryCode ISO 3166 country code.
     * @param persist {@code true} if the setting should be remembered.
     *
     * The persist behavior exists so that wifi can fall back to the last
     * persisted country code on a restart, when the locale information is
     * not available from telephony.
     */
    public void setCountryCode(String countryCode, boolean persist) {
        Slog.i(TAG, "WifiService trying to set country code to " + countryCode +
                " with persist set to " + persist);
        enforceChangePermission();
        mWifiStateMachine.setCountryCode(countryCode, persist);
    }

    /**
     * Set the operational frequency band
     * @param band One of
     *     {@link WifiManager#WIFI_FREQUENCY_BAND_AUTO},
     *     {@link WifiManager#WIFI_FREQUENCY_BAND_5GHZ},
     *     {@link WifiManager#WIFI_FREQUENCY_BAND_2GHZ},
     * @param persist {@code true} if the setting should be remembered.
     *
     */
    public void setFrequencyBand(int band, boolean persist) {
        enforceChangePermission();
        if (!isDualBandSupported()) return;
        Slog.i(TAG, "WifiService trying to set frequency band to " + band +
                " with persist set to " + persist);
        mWifiStateMachine.setFrequencyBand(band, persist);
    }


    /**
     * Get the operational frequency band
     */
    public int getFrequencyBand() {
        enforceAccessPermission();
        return mWifiStateMachine.getFrequencyBand();
    }

    public boolean isDualBandSupported() {
        //TODO: Should move towards adding a driver API that checks at runtime
        return mContext.getResources().getBoolean(
                com.android.internal.R.bool.config_wifi_dual_band_support);
    }

    /**
     * Return the DHCP-assigned addresses from the last successful DHCP request,
     * if any.
     * @return the DHCP information
     */
    public DhcpInfo getDhcpInfo() {
        enforceAccessPermission();
        return mWifiStateMachine.syncGetDhcpInfo();
    }

    /**
     * see {@link android.net.wifi.WifiManager#startWifi}
     *
     */
    public void startWifi() {
        enforceChangePermission();
        /* TODO: may be add permissions for access only to connectivity service
         * TODO: if a start issued, keep wifi alive until a stop issued irrespective
         * of WifiLock & device idle status unless wifi enabled status is toggled
         */

        mWifiStateMachine.setDriverStart(true, mEmergencyCallbackMode);
        mWifiStateMachine.reconnectCommand();
    }

    /**
     * see {@link android.net.wifi.WifiManager#stopWifi}
     *
     */
    public void stopWifi() {
        enforceChangePermission();
        /* TODO: may be add permissions for access only to connectivity service
         * TODO: if a stop is issued, wifi is brought up only by startWifi
         * unless wifi enabled status is toggled
         */
        mWifiStateMachine.setDriverStart(false, mEmergencyCallbackMode);
    }


    /**
     * see {@link android.net.wifi.WifiManager#addToBlacklist}
     *
     */
    public void addToBlacklist(String bssid) {
        enforceChangePermission();

        mWifiStateMachine.addToBlacklist(bssid);
    }

    /**
     * see {@link android.net.wifi.WifiManager#clearBlacklist}
     *
     */
    public void clearBlacklist() {
        enforceChangePermission();

        mWifiStateMachine.clearBlacklist();
    }

    /**
     * Get a reference to handler. This is used by a client to establish
     * an AsyncChannel communication with WifiService
     */
    public Messenger getWifiServiceMessenger() {
        /* Enforce the highest permissions
           TODO: when we consider exposing the asynchronous API, think about
                 how to provide both access and change permissions seperately
         */
        enforceAccessPermission();
        enforceChangePermission();
        return new Messenger(mAsyncServiceHandler);
    }

    /** Get a reference to WifiStateMachine handler for AsyncChannel communication */
    public Messenger getWifiStateMachineMessenger() {
        enforceAccessPermission();
        enforceChangePermission();
        return mWifiStateMachine.getMessenger();
    }

    /**
     * Get the IP and proxy configuration file
     */
    public String getConfigFile() {
        enforceAccessPermission();
        return mWifiStateMachine.getConfigFile();
=======
        String reply;
        synchronized (mWifiStateTracker) {
            reply = WifiNative.scanResultsCommand();
        }
        if (reply == null) {
            return null;
        }

        List<ScanResult> scanList = new ArrayList<ScanResult>();

        int lineCount = 0;

        int replyLen = reply.length();
        // Parse the result string, keeping in mind that the last line does
        // not end with a newline.
        for (int lineBeg = 0, lineEnd = 0; lineEnd <= replyLen; ++lineEnd) {
            if (lineEnd == replyLen || reply.charAt(lineEnd) == '\n') {
                ++lineCount;
                /*
                 * Skip the first line, which is a header
                 */
                if (lineCount == 1) {
                    lineBeg = lineEnd + 1;
                    continue;
                }
                int lineLen = lineEnd - lineBeg;
                if (0 < lineLen && lineLen <= SCAN_RESULT_BUFFER_SIZE) {
                    int scanResultLevel = 0;
                    /*
                     * At most one thread should have access to the buffer at a time!
                     */
                    synchronized(mScanResultBuffer) {
                        boolean parsingScanResultLevel = false;
                        for (int i = lineBeg; i < lineEnd; ++i) {
                            char ch = reply.charAt(i);
                            /*
                             * Assume that the signal level starts with a '-'
                             */
                            if (ch == '-') {
                                /*
                                 * Skip whatever instances of '-' we may have
                                 * after we parse the signal level
                                 */
                                parsingScanResultLevel = (scanResultLevel == 0);
                            } else if (parsingScanResultLevel) {
                                int digit = Character.digit(ch, 10);
                                if (0 <= digit) {
                                    scanResultLevel =
                                        10 * scanResultLevel + digit;
                                    /*
                                     * Replace the signal level number in
                                     * the string with 0's for caching
                                     */
                                    ch = '0';
                                } else {
                                    /*
                                     * Reset the flag if we meet a non-digit
                                     * character
                                     */
                                    parsingScanResultLevel = false;
                                }
                            }
                            mScanResultBuffer[i - lineBeg] = ch;
                        }
                        if (scanResultLevel != 0) {
                            ScanResult scanResult = parseScanResult(
                                new String(mScanResultBuffer, 0, lineLen));
                            if (scanResult != null) {
                              scanResult.level = -scanResultLevel;
                              scanList.add(scanResult);
                              //if (DBG) Log.d(TAG, "ScanResult: " + scanResult);
                            }
                        } else if (DBG) {
                            Log.w(TAG,
                                  "ScanResult.level=0: misformatted scan result?");
                        }
                    }
                } else if (0 < lineLen) {
                    if (DBG) {
                        Log.w(TAG, "Scan result line is too long: " +
                              (lineEnd - lineBeg) + ", skipping the line!");
                    }
                }
                lineBeg = lineEnd + 1;
            }
        }
        mWifiStateTracker.setScanResultsList(scanList);
        return scanList;
    }

    /**
     * Parse the scan result line passed to us by wpa_supplicant (helper).
     * @param line the line to parse
     * @return the {@link ScanResult} object
     */
    private ScanResult parseScanResult(String line) {
        ScanResult scanResult = null;
        if (line != null) {
            /*
             * Cache implementation (LinkedHashMap) is not synchronized, thus,
             * must synchronized here!
             */
            synchronized (mScanResultCache) {
                scanResult = mScanResultCache.get(line);
                if (scanResult == null) {
                    String[] result = scanResultPattern.split(line);
                    if (3 <= result.length && result.length <= 5) {
                        // bssid | frequency | level | flags | ssid
                        int frequency;
                        int level;
                        try {
                            frequency = Integer.parseInt(result[1]);
                            level = Integer.parseInt(result[2]);
                        } catch (NumberFormatException e) {
                            frequency = 0;
                            level = 0;
                        }

                        /*
                         * The formatting of the results returned by
                         * wpa_supplicant is intended to make the fields
                         * line up nicely when printed,
                         * not to make them easy to parse. So we have to
                         * apply some heuristics to figure out which field
                         * is the SSID and which field is the flags.
                         */
                        String ssid;
                        String flags;
                        if (result.length == 4) {
                            if (result[3].charAt(0) == '[') {
                                flags = result[3];
                                ssid = "";
                            } else {
                                flags = "";
                                ssid = result[3];
                            }
                        } else if (result.length == 5) {
                            flags = result[3];
                            ssid = result[4];
                        } else {
                            // Here, we must have 3 fields: no flags and ssid
                            // set
                            flags = "";
                            ssid = "";
                        }

                        // Do not add scan results that have no SSID set
                        if (0 < ssid.trim().length()) {
                            scanResult =
                                new ScanResult(
                                    ssid, result[0], flags, level, frequency);
                            mScanResultCache.put(line, scanResult);
                        }
                    } else {
                        Log.w(TAG, "Misformatted scan result text with " +
                              result.length + " fields: " + line);
                    }
                }
            }
        }

        return scanResult;
    }

    /**
     * Parse the "flags" field passed back in a scan result by wpa_supplicant,
     * and construct a {@code WifiConfiguration} that describes the encryption,
     * key management, and authenticaion capabilities of the access point.
     * @param flags the string returned by wpa_supplicant
     * @return the {@link WifiConfiguration} object, filled in
     */
    WifiConfiguration parseScanFlags(String flags) {
        WifiConfiguration config = new WifiConfiguration();

        if (flags.length() == 0) {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        // ... to be implemented
        return config;
    }

    /**
     * Tell the supplicant to persist the current list of configured networks.
     * @return {@code true} if the operation succeeded
     */
    public boolean saveConfiguration() {
        boolean result;
        enforceChangePermission();
        synchronized (mWifiStateTracker) {
            result = WifiNative.saveConfigCommand();
            if (result && mNeedReconfig) {
                mNeedReconfig = false;
                result = WifiNative.reloadConfigCommand();
                
                if (result) {
                    Intent intent = new Intent(WifiManager.NETWORK_IDS_CHANGED_ACTION);
                    mContext.sendBroadcast(intent);
                }
            }
        }
        return result;
    }

    /**
     * Return the DHCP-assigned addresses from the last successful DHCP request,
     * if any.
     * @return the DHCP information
     */
    public DhcpInfo getDhcpInfo() {
        enforceAccessPermission();
        return mWifiStateTracker.getDhcpInfo();
>>>>>>> 54b6cfa... Initial Contribution
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

<<<<<<< HEAD
            long idleMillis =
                Settings.Secure.getLong(mContext.getContentResolver(),
                                        Settings.Secure.WIFI_IDLE_MS, DEFAULT_IDLE_MS);
            int stayAwakeConditions =
                Settings.System.getInt(mContext.getContentResolver(),
                                       Settings.System.STAY_ON_WHILE_PLUGGED_IN, 0);
            if (action.equals(Intent.ACTION_SCREEN_ON)) {
                if (DBG) {
                    Slog.d(TAG, "ACTION_SCREEN_ON");
                }
                mAlarmManager.cancel(mIdleIntent);
                mScreenOff = false;
                evaluateTrafficStatsPolling();
                setDeviceIdleAndUpdateWifi(false);
            } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                if (DBG) {
                    Slog.d(TAG, "ACTION_SCREEN_OFF");
                }
                mScreenOff = true;
                evaluateTrafficStatsPolling();
                /*
                 * Set a timer to put Wi-Fi to sleep, but only if the screen is off
                 * AND the "stay on while plugged in" setting doesn't match the
                 * current power conditions (i.e, not plugged in, plugged in to USB,
                 * or plugged in to AC).
                 */
                if (!shouldWifiStayAwake(stayAwakeConditions, mPluggedType)) {
                    //Delayed shutdown if wifi is connected
                    if (mNetworkInfo.getDetailedState() == DetailedState.CONNECTED) {
                        if (DBG) Slog.d(TAG, "setting ACTION_DEVICE_IDLE: " + idleMillis + " ms");
                        mAlarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                                + idleMillis, mIdleIntent);
                    } else {
                        setDeviceIdleAndUpdateWifi(true);
                    }
                }
            } else if (action.equals(ACTION_DEVICE_IDLE)) {
                setDeviceIdleAndUpdateWifi(true);
            } else if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                /*
                 * Set a timer to put Wi-Fi to sleep, but only if the screen is off
                 * AND we are transitioning from a state in which the device was supposed
                 * to stay awake to a state in which it is not supposed to stay awake.
                 * If "stay awake" state is not changing, we do nothing, to avoid resetting
                 * the already-set timer.
                 */
                int pluggedType = intent.getIntExtra("plugged", 0);
                if (DBG) {
                    Slog.d(TAG, "ACTION_BATTERY_CHANGED pluggedType: " + pluggedType);
                }
                if (mScreenOff && shouldWifiStayAwake(stayAwakeConditions, mPluggedType) &&
                        !shouldWifiStayAwake(stayAwakeConditions, pluggedType)) {
                    long triggerTime = System.currentTimeMillis() + idleMillis;
                    if (DBG) {
                        Slog.d(TAG, "setting ACTION_DEVICE_IDLE timer for " + idleMillis + "ms");
                    }
                    mAlarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, mIdleIntent);
                }

                //Start scan stats tracking when device unplugged
                if (pluggedType == 0) {
                    synchronized (mScanCount) {
                        mScanCount.clear();
                    }
                }
                mPluggedType = pluggedType;
            } else if (action.equals(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE,
                        BluetoothAdapter.STATE_DISCONNECTED);
                mWifiStateMachine.sendBluetoothAdapterStateChange(state);
            } else if (action.equals(TelephonyIntents.ACTION_EMERGENCY_CALLBACK_MODE_CHANGED)) {
                mEmergencyCallbackMode = intent.getBooleanExtra("phoneinECMState", false);
                updateWifiState();
            }
        }

        /**
         * Determines whether the Wi-Fi chipset should stay awake or be put to
         * sleep. Looks at the setting for the sleep policy and the current
         * conditions.
         *
         * @see #shouldDeviceStayAwake(int, int)
         */
        private boolean shouldWifiStayAwake(int stayAwakeConditions, int pluggedType) {
            //Never sleep as long as the user has not changed the settings
            int wifiSleepPolicy = Settings.System.getInt(mContext.getContentResolver(),
                    Settings.System.WIFI_SLEEP_POLICY,
                    Settings.System.WIFI_SLEEP_POLICY_NEVER);

            if (wifiSleepPolicy == Settings.System.WIFI_SLEEP_POLICY_NEVER) {
                // Never sleep
                return true;
            } else if ((wifiSleepPolicy == Settings.System.WIFI_SLEEP_POLICY_NEVER_WHILE_PLUGGED) &&
                    (pluggedType != 0)) {
                // Never sleep while plugged, and we're plugged
                return true;
            } else {
                // Default
                return shouldDeviceStayAwake(stayAwakeConditions, pluggedType);
            }
        }

        /**
         * Determine whether the bit value corresponding to {@code pluggedType} is set in
         * the bit string {@code stayAwakeConditions}. Because a {@code pluggedType} value
         * of {@code 0} isn't really a plugged type, but rather an indication that the
         * device isn't plugged in at all, there is no bit value corresponding to a
         * {@code pluggedType} value of {@code 0}. That is why we shift by
         * {@code pluggedType - 1} instead of by {@code pluggedType}.
         * @param stayAwakeConditions a bit string specifying which "plugged types" should
         * keep the device (and hence Wi-Fi) awake.
         * @param pluggedType the type of plug (USB, AC, or none) for which the check is
         * being made
         * @return {@code true} if {@code pluggedType} indicates that the device is
         * supposed to stay awake, {@code false} otherwise.
         */
        private boolean shouldDeviceStayAwake(int stayAwakeConditions, int pluggedType) {
            return (stayAwakeConditions & pluggedType) != 0;
        }
    };

    private void setDeviceIdleAndUpdateWifi(boolean deviceIdle) {
        mDeviceIdle = deviceIdle;
        reportStartWorkSource();
        updateWifiState();
    }

    private synchronized void reportStartWorkSource() {
        mTmpWorkSource.clear();
        if (mDeviceIdle) {
            for (int i=0; i<mLocks.mList.size(); i++) {
                mTmpWorkSource.add(mLocks.mList.get(i).mWorkSource);
            }
        }
        mWifiStateMachine.updateBatteryWorkSource(mTmpWorkSource);
    }

    private void updateWifiState() {
        boolean lockHeld = mLocks.hasLocks();
        int strongestLockMode = WifiManager.WIFI_MODE_FULL;
        boolean wifiShouldBeStarted;

        if (mEmergencyCallbackMode) {
            wifiShouldBeStarted = false;
        } else {
            wifiShouldBeStarted = !mDeviceIdle || lockHeld;
        }

        if (lockHeld) {
            strongestLockMode = mLocks.getStrongestLockMode();
        }
        /* If device is not idle, lockmode cannot be scan only */
        if (!mDeviceIdle && strongestLockMode == WifiManager.WIFI_MODE_SCAN_ONLY) {
            strongestLockMode = WifiManager.WIFI_MODE_FULL;
        }

        /* Disable tethering when airplane mode is enabled */
        if (mAirplaneModeOn.get()) {
            mWifiStateMachine.setWifiApEnabled(null, false);
        }

        if (shouldWifiBeEnabled()) {
            if (wifiShouldBeStarted) {
                reportStartWorkSource();
                mWifiStateMachine.setWifiEnabled(true);
                mWifiStateMachine.setScanOnlyMode(
                        strongestLockMode == WifiManager.WIFI_MODE_SCAN_ONLY);
                mWifiStateMachine.setDriverStart(true, mEmergencyCallbackMode);
                mWifiStateMachine.setHighPerfModeEnabled(strongestLockMode
                        == WifiManager.WIFI_MODE_FULL_HIGH_PERF);
            } else {
                mWifiStateMachine.setDriverStart(false, mEmergencyCallbackMode);
            }
        } else {
            mWifiStateMachine.setWifiEnabled(false);
=======
            if (action.equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)) {
                /* do nothing, we'll check isAirplaneModeOn later. */
            } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
                mAlarmManager.cancel(mIdleIntent);
                mDeviceIdle = false;
            } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                long triggerTime = System.currentTimeMillis() + mIdleMillis;
                mAlarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, mIdleIntent);
                /* we can return now -- there's nothing to do until we get the idle intent back */
                return;
            } else if (action.equals(ACTION_DEVICE_IDLE)) {
                mDeviceIdle = true;
            } else if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                int plugged = intent.getIntExtra("plugged", 0);
                mChargerAttached = (plugged != 0);
            } else {
                return;
            }

            updateWifiState();
        }
    };

    private void updateWifiState() {
        boolean wifiEnabled = getPersistedWifiEnabled();
        boolean airplaneMode = isAirplaneModeOn();
        boolean lockHeld = mLocks.hasLocks();

        boolean wifiShouldBeEnabled = wifiEnabled && !airplaneMode;
        boolean wifiShouldBeStarted = !mDeviceIdle || lockHeld;

        synchronized (mWifiHandler) {
            if (wifiShouldBeEnabled) {
                if (wifiShouldBeStarted) {
                    mWakeLock.acquire();
                    mWifiHandler.obtainMessage(MESSAGE_ENABLE_WIFI).sendToTarget();
                    mWakeLock.acquire();
                    mWifiHandler.obtainMessage(MESSAGE_START_WIFI).sendToTarget();
                } else {
                    mDriverStopWakeLock.acquire();
                    mWifiHandler.obtainMessage(MESSAGE_STOP_WIFI).sendToTarget();
                    mWifiHandler.sendEmptyMessageDelayed(MESSAGE_RELEASE_WAKELOCK, mWakelockTimeout);
                }
            } else {
                mWakeLock.acquire();
                mWifiHandler.obtainMessage(MESSAGE_DISABLE_WIFI).sendToTarget();
            }
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    private void registerForBroadcasts() {
<<<<<<< HEAD
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(ACTION_DEVICE_IDLE);
        intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        intentFilter.addAction(TelephonyIntents.ACTION_EMERGENCY_CALLBACK_MODE_CHANGED);
        mContext.registerReceiver(mReceiver, intentFilter);
    }

    private boolean isAirplaneSensitive() {
        String airplaneModeRadios = Settings.System.getString(mContext.getContentResolver(),
                Settings.System.AIRPLANE_MODE_RADIOS);
        return airplaneModeRadios == null
            || airplaneModeRadios.contains(Settings.System.RADIO_WIFI);
    }

    private boolean isAirplaneToggleable() {
        String toggleableRadios = Settings.System.getString(mContext.getContentResolver(),
                Settings.System.AIRPLANE_MODE_TOGGLEABLE_RADIOS);
        return toggleableRadios != null
            && toggleableRadios.contains(Settings.System.RADIO_WIFI);
    }

    /**
     * Returns true if Wi-Fi is sensitive to airplane mode, and airplane mode is
     * currently on.
     * @return {@code true} if airplane mode is on.
     */
    private boolean isAirplaneModeOn() {
        return isAirplaneSensitive() && Settings.System.getInt(mContext.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) == 1;
    }

    @Override
    protected void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        if (mContext.checkCallingOrSelfPermission(android.Manifest.permission.DUMP)
=======
        String airplaneModeRadios = Settings.System.getString(mContext.getContentResolver(),
                Settings.System.AIRPLANE_MODE_RADIOS);
        boolean isAirplaneSensitive = airplaneModeRadios == null
            || airplaneModeRadios.contains(Settings.System.RADIO_WIFI);
        if (isAirplaneSensitive) {
            mContext.registerReceiver(mReceiver,
                new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED));
        }
        mContext.registerReceiver(mReceiver,
                new IntentFilter(Intent.ACTION_SCREEN_ON));
        mContext.registerReceiver(mReceiver,
                new IntentFilter(Intent.ACTION_SCREEN_OFF));
        mContext.registerReceiver(mReceiver,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        mContext.registerReceiver(mReceiver,
                new IntentFilter(ACTION_DEVICE_IDLE));
    }
    /**
     * Returns true if airplane mode is currently on.
     * @return {@code true} if airplane mode is on.
     */
    private boolean isAirplaneModeOn() {
        return Settings.System.getInt(mContext.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) == 1;
    }

    /**
     * Handler that allows posting to the WifiThread.
     */
    private class WifiHandler extends Handler {
        public WifiHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case MESSAGE_ENABLE_WIFI: {
                    setWifiEnabledBlocking(true);
                    /* fall through */
                }

                case MESSAGE_START_WIFI: {
                    WifiNative.startDriverCommand();
                    mWakeLock.release();
                    break;
                }

                case MESSAGE_DISABLE_WIFI: {
                    setWifiEnabledBlocking(false);
                    mWakeLock.release();
                    break;
                }

                case MESSAGE_STOP_WIFI: {
                    WifiNative.stopDriverCommand();
                    // don't release wakelock
                    break;
                }

                case MESSAGE_RELEASE_WAKELOCK: {
                    synchronized (mDriverStopWakeLock) {
                        if (mDriverStopWakeLock.isHeld()) {
                            mDriverStopWakeLock.release();
                        }
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    protected void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        if (mContext.checkCallingPermission(android.Manifest.permission.DUMP)
>>>>>>> 54b6cfa... Initial Contribution
                != PackageManager.PERMISSION_GRANTED) {
            pw.println("Permission Denial: can't dump WifiService from from pid="
                    + Binder.getCallingPid()
                    + ", uid=" + Binder.getCallingUid());
            return;
        }
<<<<<<< HEAD
        pw.println("Wi-Fi is " + mWifiStateMachine.syncGetWifiStateByName());
        pw.println("Stay-awake conditions: " +
                Settings.System.getInt(mContext.getContentResolver(),
                                       Settings.System.STAY_ON_WHILE_PLUGGED_IN, 0));
        pw.println();

        pw.println("Internal state:");
        pw.println(mWifiStateMachine);
        pw.println();
        pw.println("Latest scan results:");
        List<ScanResult> scanResults = mWifiStateMachine.syncGetScanResultsList();
=======
        pw.println("Wi-Fi is " + stateName(mWifiState));
        pw.println();

        pw.println("Latest scan results:");
        List<ScanResult> scanResults = mWifiStateTracker.getScanResultsList();
>>>>>>> 54b6cfa... Initial Contribution
        if (scanResults != null && scanResults.size() != 0) {
            pw.println("  BSSID              Frequency   RSSI  Flags             SSID");
            for (ScanResult r : scanResults) {
                pw.printf("  %17s  %9d  %5d  %-16s  %s%n",
                                         r.BSSID,
                                         r.frequency,
                                         r.level,
                                         r.capabilities,
                                         r.SSID == null ? "" : r.SSID);
            }
        }
<<<<<<< HEAD
        pw.println();
        pw.println("Locks acquired: " + mFullLocksAcquired + " full, " +
                mFullHighPerfLocksAcquired + " full high perf, " +
                mScanLocksAcquired + " scan");
        pw.println("Locks released: " + mFullLocksReleased + " full, " +
                mFullHighPerfLocksReleased + " full high perf, " +
                mScanLocksReleased + " scan");
        pw.println();
        pw.println("Locks held:");
        mLocks.dump(pw);

        pw.println("Scan count since last plugged in");
        synchronized (mScanCount) {
            for(int sc : mScanCount.keySet()) {
                pw.println("UID: " + sc + " Scan count: " + mScanCount.get(sc));
            }
        }

        pw.println();
        pw.println("WifiWatchdogStateMachine dump");
        mWifiWatchdogStateMachine.dump(pw);
        pw.println("WifiStateMachine dump");
        mWifiStateMachine.dump(fd, pw, args);
    }

    private class WifiLock extends DeathRecipient {
        WifiLock(int lockMode, String tag, IBinder binder, WorkSource ws) {
            super(lockMode, tag, binder, ws);
=======
    }

    private static String stateName(int wifiState) {
        switch (wifiState) {
            case WIFI_STATE_DISABLING:
                return "disabling";
            case WIFI_STATE_DISABLED:
                return "disabled";
            case WIFI_STATE_ENABLING:
                return "enabling";
            case WIFI_STATE_ENABLED:
                return "enabled";
            case WIFI_STATE_UNKNOWN:
                return "unknown state";
            default:
                return "[invalid state]";
        }
    }

    private class WifiLock implements IBinder.DeathRecipient {
        String mTag;
        IBinder mBinder;

        WifiLock(String tag, IBinder binder) {
            super();
            mTag = tag;
            mBinder = binder;
            try {
                mBinder.linkToDeath(this, 0);
            } catch (RemoteException e) {
                binderDied();
            }
>>>>>>> 54b6cfa... Initial Contribution
        }

        public void binderDied() {
            synchronized (mLocks) {
                releaseWifiLockLocked(mBinder);
            }
        }

        public String toString() {
<<<<<<< HEAD
            return "WifiLock{" + mTag + " type=" + mMode + " binder=" + mBinder + "}";
=======
            return "WifiLock{" + mTag + " binder=" + mBinder + "}";
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    private class LockList {
<<<<<<< HEAD
        private List<WifiLock> mList;

        private LockList() {
            mList = new ArrayList<WifiLock>();
        }

        private synchronized boolean hasLocks() {
            return !mList.isEmpty();
        }

        private synchronized int getStrongestLockMode() {
            if (mList.isEmpty()) {
                return WifiManager.WIFI_MODE_FULL;
            }

            if (mFullHighPerfLocksAcquired > mFullHighPerfLocksReleased) {
                return WifiManager.WIFI_MODE_FULL_HIGH_PERF;
            }

            if (mFullLocksAcquired > mFullLocksReleased) {
                return WifiManager.WIFI_MODE_FULL;
            }

            return WifiManager.WIFI_MODE_SCAN_ONLY;
        }

        private void addLock(WifiLock lock) {
            if (findLockByBinder(lock.mBinder) < 0) {
                mList.add(lock);
            }
        }

        private WifiLock removeLock(IBinder binder) {
            int index = findLockByBinder(binder);
            if (index >= 0) {
                WifiLock ret = mList.remove(index);
                ret.unlinkDeathRecipient();
                return ret;
=======
        private ArrayList<WifiLock> mList;

        public LockList() {
            mList = new ArrayList<WifiLock>();
        }

        public boolean hasLocks() {
            return !mList.isEmpty();
        }

        public void addLock(WifiLock lock) {
            int index = findLockByBinder(lock.mBinder);
            if (index < 0) {
                mList.add(lock);
            } else {
            }
        }

        public WifiLock removeLock(IBinder binder) {
            int index = findLockByBinder(binder);
            if (index >= 0) {
                return mList.remove(index);
>>>>>>> 54b6cfa... Initial Contribution
            } else {
                return null;
            }
        }

        private int findLockByBinder(IBinder binder) {
            int size = mList.size();
            for (int i = size - 1; i >= 0; i--)
                if (mList.get(i).mBinder == binder)
                    return i;
            return -1;
        }
<<<<<<< HEAD

        private void dump(PrintWriter pw) {
            for (WifiLock l : mList) {
                pw.print("    ");
                pw.println(l);
            }
        }
    }

    void enforceWakeSourcePermission(int uid, int pid) {
        if (uid == android.os.Process.myUid()) {
            return;
        }
        mContext.enforcePermission(android.Manifest.permission.UPDATE_DEVICE_STATS,
                pid, uid, null);
    }

    public boolean acquireWifiLock(IBinder binder, int lockMode, String tag, WorkSource ws) {
        mContext.enforceCallingOrSelfPermission(android.Manifest.permission.WAKE_LOCK, null);
        if (lockMode != WifiManager.WIFI_MODE_FULL &&
                lockMode != WifiManager.WIFI_MODE_SCAN_ONLY &&
                lockMode != WifiManager.WIFI_MODE_FULL_HIGH_PERF) {
            Slog.e(TAG, "Illegal argument, lockMode= " + lockMode);
            if (DBG) throw new IllegalArgumentException("lockMode=" + lockMode);
            return false;
        }
        if (ws != null && ws.size() == 0) {
            ws = null;
        }
        if (ws != null) {
            enforceWakeSourcePermission(Binder.getCallingUid(), Binder.getCallingPid());
        }
        if (ws == null) {
            ws = new WorkSource(Binder.getCallingUid());
        }
        WifiLock wifiLock = new WifiLock(lockMode, tag, binder, ws);
=======
    }

    public boolean acquireWifiLock(IBinder binder, String tag) {
        mContext.enforceCallingOrSelfPermission(android.Manifest.permission.WAKE_LOCK, null);
        WifiLock wifiLock = new WifiLock(tag, binder);
>>>>>>> 54b6cfa... Initial Contribution
        synchronized (mLocks) {
            return acquireWifiLockLocked(wifiLock);
        }
    }

<<<<<<< HEAD
    private void noteAcquireWifiLock(WifiLock wifiLock) throws RemoteException {
        switch(wifiLock.mMode) {
            case WifiManager.WIFI_MODE_FULL:
            case WifiManager.WIFI_MODE_FULL_HIGH_PERF:
                mBatteryStats.noteFullWifiLockAcquiredFromSource(wifiLock.mWorkSource);
                break;
            case WifiManager.WIFI_MODE_SCAN_ONLY:
                mBatteryStats.noteScanWifiLockAcquiredFromSource(wifiLock.mWorkSource);
                break;
        }
    }

    private void noteReleaseWifiLock(WifiLock wifiLock) throws RemoteException {
        switch(wifiLock.mMode) {
            case WifiManager.WIFI_MODE_FULL:
            case WifiManager.WIFI_MODE_FULL_HIGH_PERF:
                mBatteryStats.noteFullWifiLockReleasedFromSource(wifiLock.mWorkSource);
                break;
            case WifiManager.WIFI_MODE_SCAN_ONLY:
                mBatteryStats.noteScanWifiLockReleasedFromSource(wifiLock.mWorkSource);
                break;
        }
    }

    private boolean acquireWifiLockLocked(WifiLock wifiLock) {
        if (DBG) Slog.d(TAG, "acquireWifiLockLocked: " + wifiLock);

        mLocks.addLock(wifiLock);

        long ident = Binder.clearCallingIdentity();
        try {
            noteAcquireWifiLock(wifiLock);
            switch(wifiLock.mMode) {
            case WifiManager.WIFI_MODE_FULL:
                ++mFullLocksAcquired;
                break;
            case WifiManager.WIFI_MODE_FULL_HIGH_PERF:
                ++mFullHighPerfLocksAcquired;
                break;

            case WifiManager.WIFI_MODE_SCAN_ONLY:
                ++mScanLocksAcquired;
                break;
            }

            // Be aggressive about adding new locks into the accounted state...
            // we want to over-report rather than under-report.
            reportStartWorkSource();

            updateWifiState();
            return true;
        } catch (RemoteException e) {
            return false;
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    public void updateWifiLockWorkSource(IBinder lock, WorkSource ws) {
        int uid = Binder.getCallingUid();
        int pid = Binder.getCallingPid();
        if (ws != null && ws.size() == 0) {
            ws = null;
        }
        if (ws != null) {
            enforceWakeSourcePermission(uid, pid);
        }
        long ident = Binder.clearCallingIdentity();
        try {
            synchronized (mLocks) {
                int index = mLocks.findLockByBinder(lock);
                if (index < 0) {
                    throw new IllegalArgumentException("Wifi lock not active");
                }
                WifiLock wl = mLocks.mList.get(index);
                noteReleaseWifiLock(wl);
                wl.mWorkSource = ws != null ? new WorkSource(ws) : new WorkSource(uid);
                noteAcquireWifiLock(wl);
            }
        } catch (RemoteException e) {
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
=======
    private boolean acquireWifiLockLocked(WifiLock wifiLock) {
        mLocks.addLock(wifiLock);
        updateWifiState();
        return true;
>>>>>>> 54b6cfa... Initial Contribution
    }

    public boolean releaseWifiLock(IBinder lock) {
        mContext.enforceCallingOrSelfPermission(android.Manifest.permission.WAKE_LOCK, null);
        synchronized (mLocks) {
            return releaseWifiLockLocked(lock);
        }
    }

    private boolean releaseWifiLockLocked(IBinder lock) {
<<<<<<< HEAD
        boolean hadLock;

        WifiLock wifiLock = mLocks.removeLock(lock);

        if (DBG) Slog.d(TAG, "releaseWifiLockLocked: " + wifiLock);

        hadLock = (wifiLock != null);

        long ident = Binder.clearCallingIdentity();
        try {
            if (hadLock) {
                noteReleaseWifiLock(wifiLock);
                switch(wifiLock.mMode) {
                    case WifiManager.WIFI_MODE_FULL:
                        ++mFullLocksReleased;
                        break;
                    case WifiManager.WIFI_MODE_FULL_HIGH_PERF:
                        ++mFullHighPerfLocksReleased;
                        break;
                    case WifiManager.WIFI_MODE_SCAN_ONLY:
                        ++mScanLocksReleased;
                        break;
                }
            }

            // TODO - should this only happen if you hadLock?
            updateWifiState();

        } catch (RemoteException e) {
        } finally {
            Binder.restoreCallingIdentity(ident);
        }

        return hadLock;
    }

    private abstract class DeathRecipient
            implements IBinder.DeathRecipient {
        String mTag;
        int mMode;
        IBinder mBinder;
        WorkSource mWorkSource;

        DeathRecipient(int mode, String tag, IBinder binder, WorkSource ws) {
            super();
            mTag = tag;
            mMode = mode;
            mBinder = binder;
            mWorkSource = ws;
            try {
                mBinder.linkToDeath(this, 0);
            } catch (RemoteException e) {
                binderDied();
            }
        }

        void unlinkDeathRecipient() {
            mBinder.unlinkToDeath(this, 0);
        }
    }

    private class Multicaster extends DeathRecipient {
        Multicaster(String tag, IBinder binder) {
            super(Binder.getCallingUid(), tag, binder, null);
        }

        public void binderDied() {
            Slog.e(TAG, "Multicaster binderDied");
            synchronized (mMulticasters) {
                int i = mMulticasters.indexOf(this);
                if (i != -1) {
                    removeMulticasterLocked(i, mMode);
                }
            }
        }

        public String toString() {
            return "Multicaster{" + mTag + " binder=" + mBinder + "}";
        }

        public int getUid() {
            return mMode;
        }
    }

    public void initializeMulticastFiltering() {
        enforceMulticastChangePermission();

        synchronized (mMulticasters) {
            // if anybody had requested filters be off, leave off
            if (mMulticasters.size() != 0) {
                return;
            } else {
                mWifiStateMachine.startFilteringMulticastV4Packets();
            }
        }
    }

    public void acquireMulticastLock(IBinder binder, String tag) {
        enforceMulticastChangePermission();

        synchronized (mMulticasters) {
            mMulticastEnabled++;
            mMulticasters.add(new Multicaster(tag, binder));
            // Note that we could call stopFilteringMulticastV4Packets only when
            // our new size == 1 (first call), but this function won't
            // be called often and by making the stopPacket call each
            // time we're less fragile and self-healing.
            mWifiStateMachine.stopFilteringMulticastV4Packets();
        }

        int uid = Binder.getCallingUid();
        Long ident = Binder.clearCallingIdentity();
        try {
            mBatteryStats.noteWifiMulticastEnabled(uid);
        } catch (RemoteException e) {
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    public void releaseMulticastLock() {
        enforceMulticastChangePermission();

        int uid = Binder.getCallingUid();
        synchronized (mMulticasters) {
            mMulticastDisabled++;
            int size = mMulticasters.size();
            for (int i = size - 1; i >= 0; i--) {
                Multicaster m = mMulticasters.get(i);
                if ((m != null) && (m.getUid() == uid)) {
                    removeMulticasterLocked(i, uid);
                }
            }
        }
    }

    private void removeMulticasterLocked(int i, int uid)
    {
        Multicaster removed = mMulticasters.remove(i);

        if (removed != null) {
            removed.unlinkDeathRecipient();
        }
        if (mMulticasters.size() == 0) {
            mWifiStateMachine.startFilteringMulticastV4Packets();
        }

        Long ident = Binder.clearCallingIdentity();
        try {
            mBatteryStats.noteWifiMulticastDisabled(uid);
        } catch (RemoteException e) {
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    public boolean isMulticastEnabled() {
        enforceAccessPermission();

        synchronized (mMulticasters) {
            return (mMulticasters.size() > 0);
        }
    }

    /**
     * Evaluate if traffic stats polling is needed based on
     * connection and screen on status
     */
    private void evaluateTrafficStatsPolling() {
        Message msg;
        if (mNetworkInfo.getDetailedState() == DetailedState.CONNECTED && !mScreenOff) {
            msg = Message.obtain(mAsyncServiceHandler,
                    WifiManager.ENABLE_TRAFFIC_STATS_POLL, 1, 0);
        } else {
            msg = Message.obtain(mAsyncServiceHandler,
                    WifiManager.ENABLE_TRAFFIC_STATS_POLL, 0, 0);
        }
        msg.sendToTarget();
    }

    private void notifyOnDataActivity() {
        long sent, received;
        long preTxPkts = mTxPkts, preRxPkts = mRxPkts;
        int dataActivity = WifiManager.DATA_ACTIVITY_NONE;

        mTxPkts = TrafficStats.getTxPackets(mInterfaceName);
        mRxPkts = TrafficStats.getRxPackets(mInterfaceName);

        if (preTxPkts > 0 || preRxPkts > 0) {
            sent = mTxPkts - preTxPkts;
            received = mRxPkts - preRxPkts;
            if (sent > 0) {
                dataActivity |= WifiManager.DATA_ACTIVITY_OUT;
            }
            if (received > 0) {
                dataActivity |= WifiManager.DATA_ACTIVITY_IN;
            }

            if (dataActivity != mDataActivity && !mScreenOff) {
                mDataActivity = dataActivity;
                for (AsyncChannel client : mClients) {
                    client.sendMessage(WifiManager.DATA_ACTIVITY_NOTIFICATION, mDataActivity);
                }
            }
        }
    }


    private void checkAndSetNotification() {
        // If we shouldn't place a notification on available networks, then
        // don't bother doing any of the following
        if (!mNotificationEnabled) return;

        State state = mNetworkInfo.getState();
        if ((state == NetworkInfo.State.DISCONNECTED)
                || (state == NetworkInfo.State.UNKNOWN)) {
            // Look for an open network
            List<ScanResult> scanResults = mWifiStateMachine.syncGetScanResultsList();
            if (scanResults != null) {
                int numOpenNetworks = 0;
                for (int i = scanResults.size() - 1; i >= 0; i--) {
                    ScanResult scanResult = scanResults.get(i);

                    //A capability of [ESS] represents an open access point
                    //that is available for an STA to connect
                    if (scanResult.capabilities != null &&
                            scanResult.capabilities.equals("[ESS]")) {
                        numOpenNetworks++;
                    }
                }

                if (numOpenNetworks > 0) {
                    if (++mNumScansSinceNetworkStateChange >= NUM_SCANS_BEFORE_ACTUALLY_SCANNING) {
                        /*
                         * We've scanned continuously at least
                         * NUM_SCANS_BEFORE_NOTIFICATION times. The user
                         * probably does not have a remembered network in range,
                         * since otherwise supplicant would have tried to
                         * associate and thus resetting this counter.
                         */
                        setNotificationVisible(true, numOpenNetworks, false, 0);
                    }
                    return;
                }
            }
        }

        // No open networks in range, remove the notification
        setNotificationVisible(false, 0, false, 0);
    }

    /**
     * Clears variables related to tracking whether a notification has been
     * shown recently and clears the current notification.
     */
    private void resetNotification() {
        mNotificationRepeatTime = 0;
        mNumScansSinceNetworkStateChange = 0;
        setNotificationVisible(false, 0, false, 0);
    }

    /**
     * Display or don't display a notification that there are open Wi-Fi networks.
     * @param visible {@code true} if notification should be visible, {@code false} otherwise
     * @param numNetworks the number networks seen
     * @param force {@code true} to force notification to be shown/not-shown,
     * even if it is already shown/not-shown.
     * @param delay time in milliseconds after which the notification should be made
     * visible or invisible.
     */
    private void setNotificationVisible(boolean visible, int numNetworks, boolean force,
            int delay) {

        // Since we use auto cancel on the notification, when the
        // mNetworksAvailableNotificationShown is true, the notification may
        // have actually been canceled.  However, when it is false we know
        // for sure that it is not being shown (it will not be shown any other
        // place than here)

        // If it should be hidden and it is already hidden, then noop
        if (!visible && !mNotificationShown && !force) {
            return;
        }

        NotificationManager notificationManager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Message message;
        if (visible) {

            // Not enough time has passed to show the notification again
            if (System.currentTimeMillis() < mNotificationRepeatTime) {
                return;
            }

            if (mNotification == null) {
                // Cache the Notification object.
                mNotification = new Notification();
                mNotification.when = 0;
                mNotification.icon = ICON_NETWORKS_AVAILABLE;
                mNotification.flags = Notification.FLAG_AUTO_CANCEL;
                mNotification.contentIntent = PendingIntent.getActivity(mContext, 0,
                        new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK), 0);
            }

            CharSequence title = mContext.getResources().getQuantityText(
                    com.android.internal.R.plurals.wifi_available, numNetworks);
            CharSequence details = mContext.getResources().getQuantityText(
                    com.android.internal.R.plurals.wifi_available_detailed, numNetworks);
            mNotification.tickerText = title;
            mNotification.setLatestEventInfo(mContext, title, details, mNotification.contentIntent);

            mNotificationRepeatTime = System.currentTimeMillis() + NOTIFICATION_REPEAT_DELAY_MS;

            notificationManager.notify(ICON_NETWORKS_AVAILABLE, mNotification);
        } else {
            notificationManager.cancel(ICON_NETWORKS_AVAILABLE);
        }

        mNotificationShown = visible;
    }

    private class NotificationEnabledSettingObserver extends ContentObserver {

        public NotificationEnabledSettingObserver(Handler handler) {
            super(handler);
        }

        public void register() {
            ContentResolver cr = mContext.getContentResolver();
            cr.registerContentObserver(Settings.Secure.getUriFor(
                Settings.Secure.WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON), true, this);
            mNotificationEnabled = getValue();
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            mNotificationEnabled = getValue();
            resetNotification();
        }

        private boolean getValue() {
            return Settings.Secure.getInt(mContext.getContentResolver(),
                    Settings.Secure.WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON, 1) == 1;
        }
    }


=======
        boolean result;
        result = (mLocks.removeLock(lock) != null);
        updateWifiState();
        return result;
    }
>>>>>>> 54b6cfa... Initial Contribution
}
