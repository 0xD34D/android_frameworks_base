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
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentQueryMap;
=======
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
>>>>>>> 54b6cfa... Initial Contribution
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
<<<<<<< HEAD
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.database.Cursor;
import android.location.Address;
import android.location.Criteria;
import android.location.GeocoderParams;
import android.location.IGpsStatusListener;
import android.location.IGpsStatusProvider;
import android.location.ILocationListener;
import android.location.ILocationManager;
import android.location.INetInitiatedListener;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
=======
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.IGpsStatusListener;
import android.location.ILocationListener;
import android.location.ILocationManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.location.LocationProviderImpl;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
>>>>>>> 54b6cfa... Initial Contribution
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
<<<<<<< HEAD
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Process;
import android.os.RemoteException;
import android.os.WorkSource;
import android.provider.Settings;
import android.util.Log;
import android.util.Slog;
import android.util.PrintWriterPrinter;

import com.android.internal.content.PackageMonitor;
import com.android.internal.location.GpsNetInitiatedHandler;

import com.android.server.location.GeocoderProxy;
import com.android.server.location.GpsLocationProvider;
import com.android.server.location.LocationProviderInterface;
import com.android.server.location.LocationProviderProxy;
import com.android.server.location.MockProvider;
import com.android.server.location.PassiveProvider;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
=======
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Config;
import android.util.Log;

import com.android.internal.location.CellState;
import com.android.internal.location.GpsLocationProvider;
import com.android.internal.location.LocationCollector;
import com.android.internal.location.LocationMasfClient;
import com.android.internal.location.NetworkLocationProvider;
import com.android.internal.location.TrackProvider;
>>>>>>> 54b6cfa... Initial Contribution

/**
 * The service class that manages LocationProviders and issues location
 * updates and alerts.
 *
 * {@hide}
 */
<<<<<<< HEAD
public class LocationManagerService extends ILocationManager.Stub implements Runnable {
    private static final String TAG = "LocationManagerService";
    private static final boolean LOCAL_LOGV = false;
=======
public class LocationManagerService extends ILocationManager.Stub {
    private static final String TAG = "LocationManagerService";

    // Minimum time interval between last known location writes, in milliseconds.
    private static final long MIN_LAST_KNOWN_LOCATION_TIME = 60L * 1000L;

    // Max time to hold wake lock for, in milliseconds.
    private static final long MAX_TIME_FOR_WAKE_LOCK = 60 * 1000L;

    // Time to wait after releasing a wake lock for clients to process location update,
    // in milliseconds.
    private static final long TIME_AFTER_WAKE_LOCK = 2 * 1000L;
>>>>>>> 54b6cfa... Initial Contribution

    // The last time a location was written, by provider name.
    private HashMap<String,Long> mLastWriteTime = new HashMap<String,Long>();

<<<<<<< HEAD
=======
    private static final Pattern PATTERN_COMMA = Pattern.compile(",");

>>>>>>> 54b6cfa... Initial Contribution
    private static final String ACCESS_FINE_LOCATION =
        android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String ACCESS_COARSE_LOCATION =
        android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String ACCESS_MOCK_LOCATION =
        android.Manifest.permission.ACCESS_MOCK_LOCATION;
    private static final String ACCESS_LOCATION_EXTRA_COMMANDS =
        android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS;
<<<<<<< HEAD
    private static final String INSTALL_LOCATION_PROVIDER =
        android.Manifest.permission.INSTALL_LOCATION_PROVIDER;

    private static final String BLACKLIST_CONFIG_NAME = "locationPackagePrefixBlacklist";
    private static final String WHITELIST_CONFIG_NAME = "locationPackagePrefixWhitelist";

    // Location Providers may sometimes deliver location updates
    // slightly faster that requested - provide grace period so
    // we don't unnecessarily filter events that are otherwise on
    // time
    private static final int MAX_PROVIDER_SCHEDULING_JITTER = 100;
=======
>>>>>>> 54b6cfa... Initial Contribution

    // Set of providers that are explicitly enabled
    private final Set<String> mEnabledProviders = new HashSet<String>();

    // Set of providers that are explicitly disabled
    private final Set<String> mDisabledProviders = new HashSet<String>();

    // Locations, status values, and extras for mock providers
<<<<<<< HEAD
    private final HashMap<String,MockProvider> mMockProviders = new HashMap<String,MockProvider>();
=======
    HashMap<String,MockProvider> mMockProviders = new HashMap<String,MockProvider>();
    private final HashMap<String,Location> mMockProviderLocation = new HashMap<String,Location>();
    private final HashMap<String,Integer> mMockProviderStatus = new HashMap<String,Integer>();
    private final HashMap<String,Bundle> mMockProviderStatusExtras = new HashMap<String,Bundle>();
    private final HashMap<String,Long> mMockProviderStatusUpdateTime = new HashMap<String,Long>();
>>>>>>> 54b6cfa... Initial Contribution

    private static boolean sProvidersLoaded = false;

    private final Context mContext;
<<<<<<< HEAD
    private PackageManager mPackageManager;  // final after initialize()
    private String mNetworkLocationProviderPackageName;  // only used on handler thread
    private String mGeocodeProviderPackageName;  // only used on handler thread
    private GeocoderProxy mGeocodeProvider;
    private IGpsStatusProvider mGpsStatusProvider;
    private INetInitiatedListener mNetInitiatedListener;
    private LocationWorkerHandler mLocationHandler;

    // Cache the real providers for use in addTestProvider() and removeTestProvider()
     LocationProviderProxy mNetworkLocationProvider;
     LocationProviderInterface mGpsLocationProvider;

    // Handler messages
    private static final int MESSAGE_LOCATION_CHANGED = 1;
    private static final int MESSAGE_PACKAGE_UPDATED = 2;

    // wakelock variables
    private final static String WAKELOCK_KEY = "LocationManagerService";
    private PowerManager.WakeLock mWakeLock = null;
    private int mPendingBroadcasts;
    
    /**
     * List of all receivers.
     */
    private final HashMap<Object, Receiver> mReceivers = new HashMap<Object, Receiver>();


    /**
     * List of location providers.
     */
    private final ArrayList<LocationProviderInterface> mProviders =
        new ArrayList<LocationProviderInterface>();
    private final HashMap<String, LocationProviderInterface> mProvidersByName
        = new HashMap<String, LocationProviderInterface>();

    /**
     * Object used internally for synchronization
     */
    private final Object mLock = new Object();
=======
    private GpsLocationProvider mGpsLocationProvider;
    private NetworkLocationProvider mNetworkLocationProvider;
    private LocationWorkerHandler mLocationHandler;

    // Handler messages
    private static final int MESSAGE_HEARTBEAT = 1;
    private static final int MESSAGE_ACQUIRE_WAKE_LOCK = 2;
    private static final int MESSAGE_RELEASE_WAKE_LOCK = 3;

    // Alarm manager and wakelock variables
    private final static String ALARM_INTENT = "com.android.location.ALARM_INTENT";
    private final static String WAKELOCK_KEY = "LocationManagerService";
    private final static String WIFILOCK_KEY = "LocationManagerService";
    private AlarmManager mAlarmManager;
    private long mAlarmInterval = 0;
    private boolean mScreenOn = true;
    private PowerManager.WakeLock mWakeLock = null;
    private WifiManager.WifiLock mWifiLock = null;
    private long mWakeLockAcquireTime = 0;
    private boolean mWakeLockGpsReceived = true;
    private boolean mWakeLockNetworkReceived = true;
    private boolean mWifiWakeLockAcquired = false;
    private boolean mCellWakeLockAcquired = false;

    /**
     * Mapping from listener IBinder to local Listener wrappers.
     */
    private final HashMap<IBinder,Listener> mListeners =
        new HashMap<IBinder,Listener>();

    /**
     * Mapping from listener IBinder to a map from provider name to UpdateRecord.
     */
    private final HashMap<IBinder,HashMap<String,UpdateRecord>> mLocationListeners =
        new HashMap<IBinder,HashMap<String,UpdateRecord>>();

    /**
     * Mapping from listener IBinder to a map from provider name to last broadcast location.
     */
    private final HashMap<IBinder,HashMap<String,Location>> mLastFixBroadcast =
        new HashMap<IBinder,HashMap<String,Location>>();
    private final HashMap<IBinder,HashMap<String,Long>> mLastStatusBroadcast =
        new HashMap<IBinder,HashMap<String,Long>>();
>>>>>>> 54b6cfa... Initial Contribution

    /**
     * Mapping from provider name to all its UpdateRecords
     */
<<<<<<< HEAD
    private final HashMap<String,ArrayList<UpdateRecord>> mRecordsByProvider =
        new HashMap<String,ArrayList<UpdateRecord>>();

    /**
     * Temporary filled in when computing min time for a provider.  Access is
     * protected by global lock mLock.
     */
    private final WorkSource mTmpWorkSource = new WorkSource();

    // Proximity listeners
    private Receiver mProximityReceiver = null;
    private ILocationListener mProximityListener = null;
=======
    private final HashMap<String,HashSet<UpdateRecord>> mRecordsByProvider =
        new HashMap<String,HashSet<UpdateRecord>>();

    /**
     * Mappings from provider name to object to use for current location. Locations
     * contained in this list may not always be valid.
     */
    private final HashMap<String,Location> mLocationsByProvider =
        new HashMap<String,Location>();

    // Proximity listeners
    private ProximityListener mProximityListener = null;
>>>>>>> 54b6cfa... Initial Contribution
    private HashMap<PendingIntent,ProximityAlert> mProximityAlerts =
        new HashMap<PendingIntent,ProximityAlert>();
    private HashSet<ProximityAlert> mProximitiesEntered =
        new HashSet<ProximityAlert>();

    // Last known location for each provider
    private HashMap<String,Location> mLastKnownLocation =
        new HashMap<String,Location>();

<<<<<<< HEAD
    private int mNetworkState = LocationProvider.TEMPORARILY_UNAVAILABLE;

    // for prefix blacklist
    private String[] mWhitelist = new String[0];
    private String[] mBlacklist = new String[0];

    // for Settings change notification
    private ContentQueryMap mSettings;

    /**
     * A wrapper class holding either an ILocationListener or a PendingIntent to receive
     * location updates.
     */
    private final class Receiver implements IBinder.DeathRecipient, PendingIntent.OnFinished {
        final ILocationListener mListener;
        final PendingIntent mPendingIntent;
        final Object mKey;
        final HashMap<String,UpdateRecord> mUpdateRecords = new HashMap<String,UpdateRecord>();
        final String mPackageName;

        int mPendingBroadcasts;
        String mRequiredPermissions;

        Receiver(ILocationListener listener, String packageName) {
            mListener = listener;
            mPendingIntent = null;
            mKey = listener.asBinder();
            mPackageName = packageName;
        }

        Receiver(PendingIntent intent, String packageName) {
            mPendingIntent = intent;
            mListener = null;
            mKey = intent;
            mPackageName = packageName;
        }

        @Override
        public boolean equals(Object otherObj) {
            if (otherObj instanceof Receiver) {
                return mKey.equals(
                        ((Receiver)otherObj).mKey);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return mKey.hashCode();
        }

        @Override
        public String toString() {
            String result;
            if (mListener != null) {
                result = "Receiver{"
                        + Integer.toHexString(System.identityHashCode(this))
                        + " Listener " + mKey + "}";
            } else {
                result = "Receiver{"
                        + Integer.toHexString(System.identityHashCode(this))
                        + " Intent " + mKey + "}";
            }
            result += "mUpdateRecords: " + mUpdateRecords;
            return result;
        }

        public boolean isListener() {
            return mListener != null;
        }

        public boolean isPendingIntent() {
            return mPendingIntent != null;
        }

        public ILocationListener getListener() {
            if (mListener != null) {
                return mListener;
            }
            throw new IllegalStateException("Request for non-existent listener");
        }

        public PendingIntent getPendingIntent() {
            if (mPendingIntent != null) {
                return mPendingIntent;
            }
            throw new IllegalStateException("Request for non-existent intent");
        }

        public boolean callStatusChangedLocked(String provider, int status, Bundle extras) {
            if (mListener != null) {
                try {
                    synchronized (this) {
                        // synchronize to ensure incrementPendingBroadcastsLocked()
                        // is called before decrementPendingBroadcasts()
                        mListener.onStatusChanged(provider, status, extras);
                        if (mListener != mProximityListener) {
                            // call this after broadcasting so we do not increment
                            // if we throw an exeption.
                            incrementPendingBroadcastsLocked();
                        }
                    }
                } catch (RemoteException e) {
                    return false;
                }
            } else {
                Intent statusChanged = new Intent();
                statusChanged.putExtras(extras);
                statusChanged.putExtra(LocationManager.KEY_STATUS_CHANGED, status);
                try {
                    synchronized (this) {
                        // synchronize to ensure incrementPendingBroadcastsLocked()
                        // is called before decrementPendingBroadcasts()
                        mPendingIntent.send(mContext, 0, statusChanged, this, mLocationHandler,
                                mRequiredPermissions);
                        // call this after broadcasting so we do not increment
                        // if we throw an exeption.
                        incrementPendingBroadcastsLocked();
                    }
                } catch (PendingIntent.CanceledException e) {
                    return false;
                }
            }
            return true;
        }

        public boolean callLocationChangedLocked(Location location) {
            if (mListener != null) {
                try {
                    synchronized (this) {
                        // synchronize to ensure incrementPendingBroadcastsLocked()
                        // is called before decrementPendingBroadcasts()
                        mListener.onLocationChanged(location);
                        if (mListener != mProximityListener) {
                            // call this after broadcasting so we do not increment
                            // if we throw an exeption.
                            incrementPendingBroadcastsLocked();
                        }
                    }
                } catch (RemoteException e) {
                    return false;
                }
            } else {
                Intent locationChanged = new Intent();
                locationChanged.putExtra(LocationManager.KEY_LOCATION_CHANGED, location);
                try {
                    synchronized (this) {
                        // synchronize to ensure incrementPendingBroadcastsLocked()
                        // is called before decrementPendingBroadcasts()
                        mPendingIntent.send(mContext, 0, locationChanged, this, mLocationHandler,
                                mRequiredPermissions);
                        // call this after broadcasting so we do not increment
                        // if we throw an exeption.
                        incrementPendingBroadcastsLocked();
                    }
                } catch (PendingIntent.CanceledException e) {
                    return false;
                }
            }
            return true;
        }

        public boolean callProviderEnabledLocked(String provider, boolean enabled) {
            if (mListener != null) {
                try {
                    synchronized (this) {
                        // synchronize to ensure incrementPendingBroadcastsLocked()
                        // is called before decrementPendingBroadcasts()
                        if (enabled) {
                            mListener.onProviderEnabled(provider);
                        } else {
                            mListener.onProviderDisabled(provider);
                        }
                        if (mListener != mProximityListener) {
                            // call this after broadcasting so we do not increment
                            // if we throw an exeption.
                            incrementPendingBroadcastsLocked();
                        }
                    }
                } catch (RemoteException e) {
                    return false;
                }
            } else {
                Intent providerIntent = new Intent();
                providerIntent.putExtra(LocationManager.KEY_PROVIDER_ENABLED, enabled);
                try {
                    synchronized (this) {
                        // synchronize to ensure incrementPendingBroadcastsLocked()
                        // is called before decrementPendingBroadcasts()
                        mPendingIntent.send(mContext, 0, providerIntent, this, mLocationHandler,
                                mRequiredPermissions);
                        // call this after broadcasting so we do not increment
                        // if we throw an exeption.
                        incrementPendingBroadcastsLocked();
                    }
                } catch (PendingIntent.CanceledException e) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public void binderDied() {
            if (LOCAL_LOGV) {
                Slog.v(TAG, "Location listener died");
            }
            synchronized (mLock) {
                removeUpdatesLocked(this);
            }
            synchronized (this) {
                if (mPendingBroadcasts > 0) {
                    LocationManagerService.this.decrementPendingBroadcasts();
                    mPendingBroadcasts = 0;
                }
            }
        }

        public void onSendFinished(PendingIntent pendingIntent, Intent intent,
                int resultCode, String resultData, Bundle resultExtras) {
            synchronized (this) {
                decrementPendingBroadcastsLocked();
            }
        }

        // this must be called while synchronized by caller in a synchronized block
        // containing the sending of the broadcaset
        private void incrementPendingBroadcastsLocked() {
            if (mPendingBroadcasts++ == 0) {
                LocationManagerService.this.incrementPendingBroadcasts();
            }
        }

        private void decrementPendingBroadcastsLocked() {
            if (--mPendingBroadcasts == 0) {
                LocationManagerService.this.decrementPendingBroadcasts();
            }
        }
    }

    public void locationCallbackFinished(ILocationListener listener) {
        //Do not use getReceiver here as that will add the ILocationListener to
        //the receiver list if it is not found.  If it is not found then the
        //LocationListener was removed when it had a pending broadcast and should
        //not be added back.
        IBinder binder = listener.asBinder();
        Receiver receiver = mReceivers.get(binder);
        if (receiver != null) {
            synchronized (receiver) {
                // so wakelock calls will succeed
                long identity = Binder.clearCallingIdentity();
                receiver.decrementPendingBroadcastsLocked();
                Binder.restoreCallingIdentity(identity);
           }
        }
    }

    private final class SettingsObserver implements Observer {
        public void update(Observable o, Object arg) {
            synchronized (mLock) {
                updateProvidersLocked();
            }
        }
    }

    private void addProvider(LocationProviderInterface provider) {
        mProviders.add(provider);
        mProvidersByName.put(provider.getName(), provider);
    }

    private void removeProvider(LocationProviderInterface provider) {
        mProviders.remove(provider);
        mProvidersByName.remove(provider.getName());
    }

    private void loadProviders() {
        synchronized (mLock) {
=======
    // Battery status extras (from com.android.server.BatteryService)
    private static final String BATTERY_EXTRA_SCALE = "scale";
    private static final String BATTERY_EXTRA_LEVEL = "level";
    private static final String BATTERY_EXTRA_PLUGGED = "plugged";

    // Last known cell service state
    private TelephonyManager mTelephonyManager;
    private ServiceState mServiceState = new ServiceState();

    // Location collector
    private LocationCollector mCollector;

    // Location MASF service
    private LocationMasfClient mMasfClient;

    // Wifi Manager
    private WifiManager mWifiManager;

    private final class Listener implements IBinder.DeathRecipient {
        final ILocationListener mListener;

        Listener(ILocationListener listener) {
            mListener = listener;
        }

        public void binderDied() {
            if (Config.LOGD) {
                Log.d(TAG, "Location listener died");
            }
            synchronized (mLocationListeners) {
                _removeUpdates(mListener);
            }
        }
    }

    private Location readLastKnownLocation(String provider) {
        Location location = null;
        String s = null;
        try {
            File f = new File(LocationManager.SYSTEM_DIR + "/location."
                + provider);
            if (!f.exists()) {
                return null;
            }
            BufferedReader reader = new BufferedReader(new FileReader(f), 256);
            s = reader.readLine();
        } catch (IOException e) {
            Log.w(TAG, "Unable to read last known location", e);
        }

        if (s == null) {
            return null;
        }
        try {
            String[] tokens = PATTERN_COMMA.split(s);
            int idx = 0;
            long time = Long.parseLong(tokens[idx++]);
            double latitude = Double.parseDouble(tokens[idx++]);
            double longitude = Double.parseDouble(tokens[idx++]);
            double altitude = Double.parseDouble(tokens[idx++]);
            float bearing = Float.parseFloat(tokens[idx++]);
            float speed = Float.parseFloat(tokens[idx++]);

            location = new Location(provider);
            location.setTime(time);
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            location.setAltitude(altitude);
            location.setBearing(bearing);
            location.setSpeed(speed);
        } catch (NumberFormatException nfe) {
            Log.e(TAG, "NumberFormatException reading last known location", nfe);
            return null;
        }

        return location;
    }

    private void writeLastKnownLocation(String provider,
        Location location) {
        long now = SystemClock.elapsedRealtime();
        Long last = mLastWriteTime.get(provider);
        if ((last != null)
            && (now - last.longValue() < MIN_LAST_KNOWN_LOCATION_TIME)) {
            return;
        }
        mLastWriteTime.put(provider, now);

        StringBuilder sb = new StringBuilder(100);
        sb.append(location.getTime());
        sb.append(',');
        sb.append(location.getLatitude());
        sb.append(',');
        sb.append(location.getLongitude());
        sb.append(',');
        sb.append(location.getAltitude());
        sb.append(',');
        sb.append(location.getBearing());
        sb.append(',');
        sb.append(location.getSpeed());

        FileWriter writer = null;
        try {
            File d = new File(LocationManager.SYSTEM_DIR);
            if (!d.exists()) {
                if (!d.mkdirs()) {
                    Log.w(TAG, "Unable to create directory to write location");
                    return;
                }
            }
            File f = new File(LocationManager.SYSTEM_DIR + "/location." + provider);
            writer = new FileWriter(f);
            writer.write(sb.toString());
        } catch (IOException e) {
            Log.w(TAG, "Unable to write location", e);
        } finally {
            if (writer != null) {
                try {
                writer.close();
                } catch (IOException e) {
                    Log.w(TAG, "Exception closing file", e);
                }
            }
        }
    }

    /**
     * Load providers from /data/location/<provider_name>/
     *                                                          class
     *                                                          kml
     *                                                          nmea
     *                                                          track
     *                                                          location
     *                                                          properties
     */
    private void loadProviders() {
        synchronized (LocationManagerService.class) {
>>>>>>> 54b6cfa... Initial Contribution
            if (sProvidersLoaded) {
                return;
            }

            // Load providers
<<<<<<< HEAD
            loadProvidersLocked();
=======
            loadProvidersNoSync();
>>>>>>> 54b6cfa... Initial Contribution
            sProvidersLoaded = true;
        }
    }

<<<<<<< HEAD
    private void loadProvidersLocked() {
        try {
            _loadProvidersLocked();
        } catch (Exception e) {
            Slog.e(TAG, "Exception loading providers:", e);
        }
    }

    private void _loadProvidersLocked() {
        // Attempt to load "real" providers first
        if (GpsLocationProvider.isSupported()) {
            // Create a gps location provider
            GpsLocationProvider gpsProvider = new GpsLocationProvider(mContext, this);
            mGpsStatusProvider = gpsProvider.getGpsStatusProvider();
            mNetInitiatedListener = gpsProvider.getNetInitiatedListener();
            addProvider(gpsProvider);
            mGpsLocationProvider = gpsProvider;
        }

        // create a passive location provider, which is always enabled
        PassiveProvider passiveProvider = new PassiveProvider(this);
        addProvider(passiveProvider);
        mEnabledProviders.add(passiveProvider.getName());

        // initialize external network location and geocoder services.
        // The initial value of mNetworkLocationProviderPackageName and
        // mGeocodeProviderPackageName is just used to determine what
        // signatures future mNetworkLocationProviderPackageName and
        // mGeocodeProviderPackageName packages must have. So alternate
        // providers can be installed under a different package name
        // so long as they have the same signature as the original
        // provider packages.
        if (mNetworkLocationProviderPackageName != null) {
            String packageName = findBestPackage(LocationProviderProxy.SERVICE_ACTION,
                    mNetworkLocationProviderPackageName);
            if (packageName != null) {
                mNetworkLocationProvider = new LocationProviderProxy(mContext,
                        LocationManager.NETWORK_PROVIDER,
                        packageName, mLocationHandler);
                mNetworkLocationProviderPackageName = packageName;
                addProvider(mNetworkLocationProvider);
            }
        }
        if (mGeocodeProviderPackageName != null) {
            String packageName = findBestPackage(GeocoderProxy.SERVICE_ACTION,
                    mGeocodeProviderPackageName);
            if (packageName != null) {
                mGeocodeProvider = new GeocoderProxy(mContext, packageName);
                mGeocodeProviderPackageName = packageName;
            }
        }

        updateProvidersLocked();
    }

    /**
     * Pick the best (network location provider or geocode provider) package.
     * The best package:
     * - implements serviceIntentName
     * - has signatures that match that of sigPackageName
     * - has the highest version value in a meta-data field in the service component
     */
    String findBestPackage(String serviceIntentName, String sigPackageName) {
        Intent intent = new Intent(serviceIntentName);
        List<ResolveInfo> infos = mPackageManager.queryIntentServices(intent,
                PackageManager.GET_META_DATA);
        if (infos == null) return null;

        int bestVersion = Integer.MIN_VALUE;
        String bestPackage = null;
        for (ResolveInfo info : infos) {
            String packageName = info.serviceInfo.packageName;
            // check signature
            if (mPackageManager.checkSignatures(packageName, sigPackageName) !=
                    PackageManager.SIGNATURE_MATCH) {
                Slog.w(TAG, packageName + " implements " + serviceIntentName +
                       " but its signatures don't match those in " + sigPackageName +
                       ", ignoring");
                continue;
            }
            // read version
            int version = 0;
            if (info.serviceInfo.metaData != null) {
                version = info.serviceInfo.metaData.getInt("version", 0);
            }
            if (LOCAL_LOGV) Slog.v(TAG, packageName + " implements " + serviceIntentName +
                    " with version " + version);
            if (version > bestVersion) {
                bestVersion = version;
                bestPackage = packageName;
            }
        }

        return bestPackage;
=======
    private void loadProvidersNoSync() {
        try {
            _loadProvidersNoSync();
        } catch (Exception e) {
            Log.e(TAG, "Exception loading providers:", e);
        }
    }

    private void _loadProvidersNoSync() {
        // Attempt to load "real" providers first
        if (NetworkLocationProvider.isSupported()) {
            // Create a network location provider
            mNetworkLocationProvider = new NetworkLocationProvider(mContext, mMasfClient);
            LocationProviderImpl.addProvider(mNetworkLocationProvider);
        }

        if (GpsLocationProvider.isSupported()) {
            // Create a gps location provider
            mGpsLocationProvider = new GpsLocationProvider(mContext, mCollector);
            LocationProviderImpl.addProvider(mGpsLocationProvider);
        }

        // Load fake providers if real providers are not available
        File f = new File(LocationManager.PROVIDER_DIR);
        if (f.isDirectory()) {
            File[] subdirs = f.listFiles();
            for (int i = 0; i < subdirs.length; i++) {
                if (!subdirs[i].isDirectory()) {
                    continue;
                }

                String name = subdirs[i].getName();

                if (Config.LOGD) {
                    Log.d(TAG, "Found dir " + subdirs[i].getAbsolutePath());
                    Log.d(TAG, "name = " + name);
                }

                // Don't create a fake provider if a real provider exists
                if (LocationProviderImpl.getProvider(name) == null) {
                    LocationProviderImpl provider = null;
                    try {
                        File classFile = new File(subdirs[i], "class");
                        // Look for a 'class' file
                        provider = LocationProviderImpl.loadFromClass(classFile);

                        // Look for an 'kml', 'nmea', or 'track' file
                        if (provider == null) {
                            // Load properties from 'properties' file, if present
                            File propertiesFile = new File(subdirs[i], "properties");

                            if (propertiesFile.exists()) {
                                provider = new TrackProvider(name);
                                ((TrackProvider)provider).readProperties(propertiesFile);

                                File kmlFile = new File(subdirs[i], "kml");
                                if (kmlFile.exists()) {
                                    ((TrackProvider) provider).readKml(kmlFile);
                                } else {
                                    File nmeaFile = new File(subdirs[i], "nmea");
                                    if (nmeaFile.exists()) {
                                        ((TrackProvider) provider).readNmea(name, nmeaFile);
                                    } else {
                                        File trackFile = new File(subdirs[i], "track");
                                        if (trackFile.exists()) {
                                            ((TrackProvider) provider).readTrack(trackFile);
                                        }
                                    }
                                }
                            }
                        }
                        if (provider != null) {
                            LocationProviderImpl.addProvider(provider);
                        }
                        // Grab the initial location of a TrackProvider and
                        // store it as the last known location for that provider
                        if (provider instanceof TrackProvider) {
                            TrackProvider tp = (TrackProvider) provider;
                            mLastKnownLocation.put(tp.getName(), tp.getInitialLocation());
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Exception loading provder " + name, e);
                    }
                }
            }
        }

        updateProviders();
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * @param context the context that the LocationManagerService runs in
     */
    public LocationManagerService(Context context) {
        super();
        mContext = context;
<<<<<<< HEAD
        Resources resources = context.getResources();

        mNetworkLocationProviderPackageName = resources.getString(
                com.android.internal.R.string.config_networkLocationProviderPackageName);
        mGeocodeProviderPackageName = resources.getString(
                com.android.internal.R.string.config_geocodeProviderPackageName);

        mPackageMonitor.register(context, null, true);

        if (LOCAL_LOGV) {
            Slog.v(TAG, "Constructed LocationManager Service");
        }
    }

    void systemReady() {
        // we defer starting up the service until the system is ready 
        Thread thread = new Thread(null, this, "LocationManagerService");
        thread.start();
    }

    private void initialize() {
        // Create a wake lock, needs to be done before calling loadProviders() below
        PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKELOCK_KEY);
        mPackageManager = mContext.getPackageManager();

        // Load providers
        loadProviders();
        loadBlacklist();

        // Register for Network (Wifi or Mobile) updates
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        // Register for Package Manager updates
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_RESTARTED);
        intentFilter.addAction(Intent.ACTION_QUERY_PACKAGE_RESTART);
        mContext.registerReceiver(mBroadcastReceiver, intentFilter);
        IntentFilter sdFilter = new IntentFilter(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
        mContext.registerReceiver(mBroadcastReceiver, sdFilter);

        // listen for settings changes
        ContentResolver resolver = mContext.getContentResolver();
        Cursor settingsCursor = resolver.query(Settings.Secure.CONTENT_URI, null,
                "(" + Settings.System.NAME + "=?)",
                new String[]{Settings.Secure.LOCATION_PROVIDERS_ALLOWED},
                null);
        mSettings = new ContentQueryMap(settingsCursor, Settings.System.NAME, true, mLocationHandler);
        SettingsObserver settingsObserver = new SettingsObserver();
        mSettings.addObserver(settingsObserver);
    }

    public void run()
    {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        Looper.prepare();
        mLocationHandler = new LocationWorkerHandler();
        initialize();
        Looper.loop();
    }

    private boolean isAllowedBySettingsLocked(String provider) {
=======
        mLocationHandler = new LocationWorkerHandler();

        if (Config.LOGD) {
            Log.d(TAG, "Constructed LocationManager Service");
        }

        // Initialize the LocationMasfClient
        mMasfClient = new LocationMasfClient(mContext);

        // Create location collector
        mCollector = new LocationCollector(mMasfClient);

        // Load providers
        loadProviders();

        // Listen for Radio changes
        mTelephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(mPhoneStateListener,
                PhoneStateListener.LISTEN_SERVICE_STATE | PhoneStateListener.LISTEN_CELL_LOCATION);

        // Register for Network (Wifi or Mobile) updates
        NetworkStateBroadcastReceiver networkReceiver = new NetworkStateBroadcastReceiver();
        IntentFilter networkIntentFilter = new IntentFilter();
        networkIntentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        networkIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        networkIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        networkIntentFilter.addAction(GpsLocationProvider.GPS_ENABLED_CHANGE_ACTION);
        context.registerReceiver(networkReceiver, networkIntentFilter);

        // Alarm manager
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Register for power updates
        PowerStateBroadcastReceiver powerStateReceiver = new PowerStateBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ALARM_INTENT);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        context.registerReceiver(powerStateReceiver, intentFilter);

        // Create a wake lock
        PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKELOCK_KEY);

        // Get the wifi manager
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        // Create a wifi lock for future use
        mWifiLock = getWifiWakelock();

        // There might be an existing wifi scan available
        if (mWifiManager != null) {
            List<ScanResult> wifiScanResults = mWifiManager.getScanResults();
            if (wifiScanResults != null && wifiScanResults.size() != 0) {
                if (mNetworkLocationProvider != null) {
                    mNetworkLocationProvider.updateWifiScanResults(wifiScanResults);
                }
            }
        }
    }

    private WifiManager.WifiLock getWifiWakelock() {
        if (mWifiLock == null && mWifiManager != null) {
            mWifiLock = mWifiManager.createWifiLock(WIFILOCK_KEY);
            mWifiLock.setReferenceCounted(false);
        }
        return mWifiLock;
    }

    private boolean isAllowedBySettings(String provider) {
>>>>>>> 54b6cfa... Initial Contribution
        if (mEnabledProviders.contains(provider)) {
            return true;
        }
        if (mDisabledProviders.contains(provider)) {
            return false;
        }
        // Use system settings
        ContentResolver resolver = mContext.getContentResolver();
<<<<<<< HEAD

        return Settings.Secure.isLocationProviderEnabled(resolver, provider);
    }

    private String checkPermissionsSafe(String provider, String lastPermission) {
        if (LocationManager.GPS_PROVIDER.equals(provider)
                 || LocationManager.PASSIVE_PROVIDER.equals(provider)) {
            if (mContext.checkCallingOrSelfPermission(ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                throw new SecurityException("Provider " + provider
                        + " requires ACCESS_FINE_LOCATION permission");
            }
            return ACCESS_FINE_LOCATION;
        }

        // Assume any other provider requires the coarse or fine permission.
        if (mContext.checkCallingOrSelfPermission(ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            return ACCESS_FINE_LOCATION.equals(lastPermission)
                    ? lastPermission : ACCESS_COARSE_LOCATION;
        }
        if (mContext.checkCallingOrSelfPermission(ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            return ACCESS_FINE_LOCATION;
        }

        throw new SecurityException("Provider " + provider
                + " requires ACCESS_FINE_LOCATION or ACCESS_COARSE_LOCATION permission");
    }

    private boolean isAllowedProviderSafe(String provider) {
        if ((LocationManager.GPS_PROVIDER.equals(provider)
                || LocationManager.PASSIVE_PROVIDER.equals(provider))
            && (mContext.checkCallingOrSelfPermission(ACCESS_FINE_LOCATION)
=======
        String allowedProviders = Settings.System.getString(resolver,
                        Settings.System.LOCATION_PROVIDERS_ALLOWED);

        return ((allowedProviders != null) && (allowedProviders.contains(provider)));
    }

    private void checkPermissions(String provider) {
        if (LocationManager.GPS_PROVIDER.equals(provider)
            && (mContext.checkCallingPermission(ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)) {
            throw new SecurityException("Requires ACCESS_FINE_LOCATION permission");
        }
        if (LocationManager.NETWORK_PROVIDER.equals(provider)
            && (mContext.checkCallingPermission(ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            && (mContext.checkCallingPermission(ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)) {
            throw new SecurityException(
                "Requires ACCESS_FINE_LOCATION or ACCESS_COARSE_LOCATION permission");
        }
    }

    private boolean isAllowedProvider(String provider) {
        if (LocationManager.GPS_PROVIDER.equals(provider)
            && (mContext.checkCallingPermission(ACCESS_FINE_LOCATION)
>>>>>>> 54b6cfa... Initial Contribution
                != PackageManager.PERMISSION_GRANTED)) {
            return false;
        }
        if (LocationManager.NETWORK_PROVIDER.equals(provider)
<<<<<<< HEAD
            && (mContext.checkCallingOrSelfPermission(ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            && (mContext.checkCallingOrSelfPermission(ACCESS_COARSE_LOCATION)
=======
            && (mContext.checkCallingPermission(ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            && (mContext.checkCallingPermission(ACCESS_COARSE_LOCATION)
>>>>>>> 54b6cfa... Initial Contribution
                != PackageManager.PERMISSION_GRANTED)) {
            return false;
        }

        return true;
    }

<<<<<<< HEAD
    public List<String> getAllProviders() {
        try {
            synchronized (mLock) {
                return _getAllProvidersLocked();
            }
        } catch (SecurityException se) {
            throw se;
        } catch (Exception e) {
            Slog.e(TAG, "getAllProviders got exception:", e);
=======
    private String[] getPackageNames() {
        // Since a single UID may correspond to multiple packages, this can only be used as an
        // approximation for tracking
        return mContext.getPackageManager().getPackagesForUid(Binder.getCallingUid());
    }

    public List<String> getAllProviders() {
        try {
            return _getAllProviders();
        } catch (SecurityException se) {
            throw se;
        } catch (Exception e) {
            Log.e(TAG, "getAllProviders got exception:", e);
>>>>>>> 54b6cfa... Initial Contribution
            return null;
        }
    }

<<<<<<< HEAD
    private List<String> _getAllProvidersLocked() {
        if (LOCAL_LOGV) {
            Slog.v(TAG, "getAllProviders");
        }
        ArrayList<String> out = new ArrayList<String>(mProviders.size());
        for (int i = mProviders.size() - 1; i >= 0; i--) {
            LocationProviderInterface p = mProviders.get(i);
=======
    private List<String> _getAllProviders() {
        if (Config.LOGD) {
            Log.d(TAG, "getAllProviders");
        }
        List<LocationProviderImpl> providers = LocationProviderImpl.getProviders();
        ArrayList<String> out = new ArrayList<String>(providers.size());

        for (LocationProviderImpl p : providers) {
>>>>>>> 54b6cfa... Initial Contribution
            out.add(p.getName());
        }
        return out;
    }

<<<<<<< HEAD
    public List<String> getProviders(Criteria criteria, boolean enabledOnly) {
        try {
            synchronized (mLock) {
                return _getProvidersLocked(criteria, enabledOnly);
            }
        } catch (SecurityException se) {
            throw se;
        } catch (Exception e) {
            Slog.e(TAG, "getProviders got exception:", e);
=======
    public List<String> getProviders(boolean enabledOnly) {
        try {
            return _getProviders(enabledOnly);
        } catch (SecurityException se) {
            throw se;
        } catch (Exception e) {
            Log.e(TAG, "getProviders gotString exception:", e);
>>>>>>> 54b6cfa... Initial Contribution
            return null;
        }
    }

<<<<<<< HEAD
    private List<String> _getProvidersLocked(Criteria criteria, boolean enabledOnly) {
        if (LOCAL_LOGV) {
            Slog.v(TAG, "getProviders");
        }
        ArrayList<String> out = new ArrayList<String>(mProviders.size());
        for (int i = mProviders.size() - 1; i >= 0; i--) {
            LocationProviderInterface p = mProviders.get(i);
            String name = p.getName();
            if (isAllowedProviderSafe(name)) {
                if (enabledOnly && !isAllowedBySettingsLocked(name)) {
                    continue;
                }
                if (criteria != null && !p.meetsCriteria(criteria)) {
=======
    private List<String> _getProviders(boolean enabledOnly) {
        if (Config.LOGD) {
            Log.d(TAG, "getProviders");
        }
        List<LocationProviderImpl> providers = LocationProviderImpl.getProviders();
        ArrayList<String> out = new ArrayList<String>();

        for (LocationProviderImpl p : providers) {
            String name = p.getName();
            if (isAllowedProvider(name)) {
                if (enabledOnly && !isAllowedBySettings(name)) {
>>>>>>> 54b6cfa... Initial Contribution
                    continue;
                }
                out.add(name);
            }
        }
        return out;
    }

<<<<<<< HEAD
    /**
     * Returns the next looser power requirement, in the sequence:
     *
     * POWER_LOW -> POWER_MEDIUM -> POWER_HIGH -> NO_REQUIREMENT
     */
    private int nextPower(int power) {
        switch (power) {
        case Criteria.POWER_LOW:
            return Criteria.POWER_MEDIUM;
        case Criteria.POWER_MEDIUM:
            return Criteria.POWER_HIGH;
        case Criteria.POWER_HIGH:
            return Criteria.NO_REQUIREMENT;
        case Criteria.NO_REQUIREMENT:
        default:
            return Criteria.NO_REQUIREMENT;
        }
    }

    /**
     * Returns the next looser accuracy requirement, in the sequence:
     *
     * ACCURACY_FINE -> ACCURACY_APPROXIMATE-> NO_REQUIREMENT
     */
    private int nextAccuracy(int accuracy) {
        if (accuracy == Criteria.ACCURACY_FINE) {
            return Criteria.ACCURACY_COARSE;
        } else {
            return Criteria.NO_REQUIREMENT;
        }
    }

    private class LpPowerComparator implements Comparator<LocationProviderInterface> {
        public int compare(LocationProviderInterface l1, LocationProviderInterface l2) {
            // Smaller is better
            return (l1.getPowerRequirement() - l2.getPowerRequirement());
         }

         public boolean equals(LocationProviderInterface l1, LocationProviderInterface l2) {
             return (l1.getPowerRequirement() == l2.getPowerRequirement());
         }
    }

    private class LpAccuracyComparator implements Comparator<LocationProviderInterface> {
        public int compare(LocationProviderInterface l1, LocationProviderInterface l2) {
            // Smaller is better
            return (l1.getAccuracy() - l2.getAccuracy());
         }

         public boolean equals(LocationProviderInterface l1, LocationProviderInterface l2) {
             return (l1.getAccuracy() == l2.getAccuracy());
         }
    }

    private class LpCapabilityComparator implements Comparator<LocationProviderInterface> {

        private static final int ALTITUDE_SCORE = 4;
        private static final int BEARING_SCORE = 4;
        private static final int SPEED_SCORE = 4;

        private int score(LocationProviderInterface p) {
            return (p.supportsAltitude() ? ALTITUDE_SCORE : 0) +
                (p.supportsBearing() ? BEARING_SCORE : 0) +
                (p.supportsSpeed() ? SPEED_SCORE : 0);
        }

        public int compare(LocationProviderInterface l1, LocationProviderInterface l2) {
            return (score(l2) - score(l1)); // Bigger is better
         }

         public boolean equals(LocationProviderInterface l1, LocationProviderInterface l2) {
             return (score(l1) == score(l2));
         }
    }

    private LocationProviderInterface best(List<String> providerNames) {
        ArrayList<LocationProviderInterface> providers;
        synchronized (mLock) {
            providers = new ArrayList<LocationProviderInterface>(providerNames.size());
            for (String name : providerNames) {
                providers.add(mProvidersByName.get(name));
            }
        }

        if (providers.size() < 2) {
            return providers.get(0);
        }

        // First, sort by power requirement
        Collections.sort(providers, new LpPowerComparator());
        int power = providers.get(0).getPowerRequirement();
        if (power < providers.get(1).getPowerRequirement()) {
            return providers.get(0);
        }

        int idx, size;

        ArrayList<LocationProviderInterface> tmp = new ArrayList<LocationProviderInterface>();
        idx = 0;
        size = providers.size();
        while ((idx < size) && (providers.get(idx).getPowerRequirement() == power)) {
            tmp.add(providers.get(idx));
            idx++;
        }

        // Next, sort by accuracy
        Collections.sort(tmp, new LpAccuracyComparator());
        int acc = tmp.get(0).getAccuracy();
        if (acc < tmp.get(1).getAccuracy()) {
            return tmp.get(0);
        }

        ArrayList<LocationProviderInterface> tmp2 = new ArrayList<LocationProviderInterface>();
        idx = 0;
        size = tmp.size();
        while ((idx < size) && (tmp.get(idx).getAccuracy() == acc)) {
            tmp2.add(tmp.get(idx));
            idx++;
        }

        // Finally, sort by capability "score"
        Collections.sort(tmp2, new LpCapabilityComparator());
        return tmp2.get(0);
    }

    /**
     * Returns the name of the provider that best meets the given criteria. Only providers
     * that are permitted to be accessed by the calling activity will be
     * returned.  If several providers meet the criteria, the one with the best
     * accuracy is returned.  If no provider meets the criteria,
     * the criteria are loosened in the following sequence:
     *
     * <ul>
     * <li> power requirement
     * <li> accuracy
     * <li> bearing
     * <li> speed
     * <li> altitude
     * </ul>
     *
     * <p> Note that the requirement on monetary cost is not removed
     * in this process.
     *
     * @param criteria the criteria that need to be matched
     * @param enabledOnly if true then only a provider that is currently enabled is returned
     * @return name of the provider that best matches the requirements
     */
    public String getBestProvider(Criteria criteria, boolean enabledOnly) {
        List<String> goodProviders = getProviders(criteria, enabledOnly);
        if (!goodProviders.isEmpty()) {
            return best(goodProviders).getName();
        }

        // Make a copy of the criteria that we can modify
        criteria = new Criteria(criteria);

        // Loosen power requirement
        int power = criteria.getPowerRequirement();
        while (goodProviders.isEmpty() && (power != Criteria.NO_REQUIREMENT)) {
            power = nextPower(power);
            criteria.setPowerRequirement(power);
            goodProviders = getProviders(criteria, enabledOnly);
        }
        if (!goodProviders.isEmpty()) {
            return best(goodProviders).getName();
        }

        // Loosen accuracy requirement
        int accuracy = criteria.getAccuracy();
        while (goodProviders.isEmpty() && (accuracy != Criteria.NO_REQUIREMENT)) {
            accuracy = nextAccuracy(accuracy);
            criteria.setAccuracy(accuracy);
            goodProviders = getProviders(criteria, enabledOnly);
        }
        if (!goodProviders.isEmpty()) {
            return best(goodProviders).getName();
        }

        // Remove bearing requirement
        criteria.setBearingRequired(false);
        goodProviders = getProviders(criteria, enabledOnly);
        if (!goodProviders.isEmpty()) {
            return best(goodProviders).getName();
        }

        // Remove speed requirement
        criteria.setSpeedRequired(false);
        goodProviders = getProviders(criteria, enabledOnly);
        if (!goodProviders.isEmpty()) {
            return best(goodProviders).getName();
        }

        // Remove altitude requirement
        criteria.setAltitudeRequired(false);
        goodProviders = getProviders(criteria, enabledOnly);
        if (!goodProviders.isEmpty()) {
            return best(goodProviders).getName();
        }

        return null;
    }

    public boolean providerMeetsCriteria(String provider, Criteria criteria) {
        LocationProviderInterface p = mProvidersByName.get(provider);
        if (p == null) {
            throw new IllegalArgumentException("provider=" + provider);
        }
        return p.meetsCriteria(criteria);
    }

    private void updateProvidersLocked() {
        boolean changesMade = false;
        for (int i = mProviders.size() - 1; i >= 0; i--) {
            LocationProviderInterface p = mProviders.get(i);
            boolean isEnabled = p.isEnabled();
            String name = p.getName();
            boolean shouldBeEnabled = isAllowedBySettingsLocked(name);
            if (isEnabled && !shouldBeEnabled) {
                updateProviderListenersLocked(name, false);
                changesMade = true;
            } else if (!isEnabled && shouldBeEnabled) {
                updateProviderListenersLocked(name, true);
                changesMade = true;
            }
        }
        if (changesMade) {
            mContext.sendBroadcast(new Intent(LocationManager.PROVIDERS_CHANGED_ACTION));
        }
    }

    private void updateProviderListenersLocked(String provider, boolean enabled) {
        int listeners = 0;

        LocationProviderInterface p = mProvidersByName.get(provider);
        if (p == null) {
            return;
        }

        ArrayList<Receiver> deadReceivers = null;
        
        ArrayList<UpdateRecord> records = mRecordsByProvider.get(provider);
        if (records != null) {
            final int N = records.size();
            for (int i=0; i<N; i++) {
                UpdateRecord record = records.get(i);
                // Sends a notification message to the receiver
                if (!record.mReceiver.callProviderEnabledLocked(provider, enabled)) {
                    if (deadReceivers == null) {
                        deadReceivers = new ArrayList<Receiver>();
                    }
                    deadReceivers.add(record.mReceiver);
                }
                listeners++;
            }
        }

        if (deadReceivers != null) {
            for (int i=deadReceivers.size()-1; i>=0; i--) {
                removeUpdatesLocked(deadReceivers.get(i));
            }
        }
        
        if (enabled) {
            p.enable();
            if (listeners > 0) {
                p.setMinTime(getMinTimeLocked(provider), mTmpWorkSource);
                p.enableLocationTracking(true);
            }
        } else {
            p.enableLocationTracking(false);
            p.disable();
        }
    }

    private long getMinTimeLocked(String provider) {
        long minTime = Long.MAX_VALUE;
        ArrayList<UpdateRecord> records = mRecordsByProvider.get(provider);
        mTmpWorkSource.clear();
        if (records != null) {
            for (int i=records.size()-1; i>=0; i--) {
                UpdateRecord ur = records.get(i);
                long curTime = ur.mMinTime;
                if (curTime < minTime) {
                    minTime = curTime;
                }
            }
            long inclTime = (minTime*3)/2;
            for (int i=records.size()-1; i>=0; i--) {
                UpdateRecord ur = records.get(i);
                if (ur.mMinTime <= inclTime) {
                    mTmpWorkSource.add(ur.mUid);
                }
            }
        }
        return minTime;
    }

    private class UpdateRecord {
        final String mProvider;
        final Receiver mReceiver;
        final long mMinTime;
        final float mMinDistance;
        final boolean mSingleShot;
        final int mUid;
        Location mLastFixBroadcast;
        long mLastStatusBroadcast;

        /**
         * Note: must be constructed with lock held.
         */
        UpdateRecord(String provider, long minTime, float minDistance, boolean singleShot,
            Receiver receiver, int uid) {
            mProvider = provider;
            mReceiver = receiver;
            mMinTime = minTime;
            mMinDistance = minDistance;
            mSingleShot = singleShot;
            mUid = uid;

            ArrayList<UpdateRecord> records = mRecordsByProvider.get(provider);
            if (records == null) {
                records = new ArrayList<UpdateRecord>();
                mRecordsByProvider.put(provider, records);
            }
            if (!records.contains(this)) {
                records.add(this);
            }
        }

        /**
         * Method to be called when a record will no longer be used.  Calling this multiple times
         * must have the same effect as calling it once.
         */
        void disposeLocked() {
            ArrayList<UpdateRecord> records = mRecordsByProvider.get(this.mProvider);
            if (records != null) {
                records.remove(this);
            }
        }

        @Override
        public String toString() {
            return "UpdateRecord{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " mProvider: " + mProvider + " mUid: " + mUid + "}";
        }

        void dump(PrintWriter pw, String prefix) {
            pw.println(prefix + this);
            pw.println(prefix + "mProvider=" + mProvider + " mReceiver=" + mReceiver);
            pw.println(prefix + "mMinTime=" + mMinTime + " mMinDistance=" + mMinDistance);
            pw.println(prefix + "mSingleShot=" + mSingleShot);
            pw.println(prefix + "mUid=" + mUid);
            pw.println(prefix + "mLastFixBroadcast:");
            if (mLastFixBroadcast != null) {
                mLastFixBroadcast.dump(new PrintWriterPrinter(pw), prefix + "  ");
            }
            pw.println(prefix + "mLastStatusBroadcast=" + mLastStatusBroadcast);
        }
    }

    private Receiver getReceiver(ILocationListener listener, String packageName) {
        IBinder binder = listener.asBinder();
        Receiver receiver = mReceivers.get(binder);
        if (receiver == null) {
            receiver = new Receiver(listener, packageName);
            mReceivers.put(binder, receiver);

            try {
                if (receiver.isListener()) {
                    receiver.getListener().asBinder().linkToDeath(receiver, 0);
                }
            } catch (RemoteException e) {
                Slog.e(TAG, "linkToDeath failed:", e);
                return null;
            }
        }
        return receiver;
    }

    private Receiver getReceiver(PendingIntent intent, String packageName) {
        Receiver receiver = mReceivers.get(intent);
        if (receiver == null) {
            receiver = new Receiver(intent, packageName);
            mReceivers.put(intent, receiver);
        }
        return receiver;
    }

    private boolean providerHasListener(String provider, int uid, Receiver excludedReceiver) {
        ArrayList<UpdateRecord> records = mRecordsByProvider.get(provider);
        if (records != null) {
            for (int i = records.size() - 1; i >= 0; i--) {
                UpdateRecord record = records.get(i);
                if (record.mUid == uid && record.mReceiver != excludedReceiver) {
                    return true;
                }
           }
        }
        for (ProximityAlert alert : mProximityAlerts.values()) {
            if (alert.mUid == uid) {
                return true;
            }
        }
        return false;
    }

    public void requestLocationUpdates(String provider, Criteria criteria,
        long minTime, float minDistance, boolean singleShot, ILocationListener listener,
        String packageName) {
        checkPackageName(Binder.getCallingUid(), packageName);
        if (criteria != null) {
            // FIXME - should we consider using multiple providers simultaneously
            // rather than only the best one?
            // Should we do anything different for single shot fixes?
            provider = getBestProvider(criteria, true);
            if (provider == null) {
                throw new IllegalArgumentException("no providers found for criteria");
            }
        }
        try {
            synchronized (mLock) {
                requestLocationUpdatesLocked(provider, minTime, minDistance, singleShot,
                        getReceiver(listener, packageName));
            }
        } catch (SecurityException se) {
            throw se;
        } catch (IllegalArgumentException iae) {
            throw iae;
        } catch (Exception e) {
            Slog.e(TAG, "requestUpdates got exception:", e);
        }
    }

    void validatePendingIntent(PendingIntent intent) {
        if (intent.isTargetedToPackage()) {
            return;
        }
        Slog.i(TAG, "Given Intent does not require a specific package: "
                + intent);
        // XXX we should really throw a security exception, if the caller's
        // targetSdkVersion is high enough.
        //throw new SecurityException("Given Intent does not require a specific package: "
        //        + intent);
    }

    public void requestLocationUpdatesPI(String provider, Criteria criteria,
            long minTime, float minDistance, boolean singleShot, PendingIntent intent,
            String packageName) {
        checkPackageName(Binder.getCallingUid(), packageName);
        validatePendingIntent(intent);
        if (criteria != null) {
            // FIXME - should we consider using multiple providers simultaneously
            // rather than only the best one?
            // Should we do anything different for single shot fixes?
            provider = getBestProvider(criteria, true);
            if (provider == null) {
                throw new IllegalArgumentException("no providers found for criteria");
            }
        }
        try {
            synchronized (mLock) {
                requestLocationUpdatesLocked(provider, minTime, minDistance, singleShot,
                        getReceiver(intent, packageName));
            }
        } catch (SecurityException se) {
            throw se;
        } catch (IllegalArgumentException iae) {
            throw iae;
        } catch (Exception e) {
            Slog.e(TAG, "requestUpdates got exception:", e);
        }
    }

    private void requestLocationUpdatesLocked(String provider, long minTime, float minDistance,
            boolean singleShot, Receiver receiver) {

        LocationProviderInterface p = mProvidersByName.get(provider);
        if (p == null) {
            //throw new IllegalArgumentException("requested provider " + provider +
                    //" doesn't exisit");
//            p = mProvidersByName.get(LocationManager.PASSIVE_PROVIDER);
//            if (p == null) {
                throw new IllegalArgumentException("requested provider " + provider +
                    " doesn't exisit");
//            }
        }
        receiver.mRequiredPermissions = checkPermissionsSafe(provider,
                receiver.mRequiredPermissions);

        // so wakelock calls will succeed
        final int callingPid = Binder.getCallingPid();
        final int callingUid = Binder.getCallingUid();
        boolean newUid = !providerHasListener(provider, callingUid, null);
        long identity = Binder.clearCallingIdentity();
        try {
            UpdateRecord r = new UpdateRecord(provider, minTime, minDistance, singleShot,
                    receiver, callingUid);
            UpdateRecord oldRecord = receiver.mUpdateRecords.put(provider, r);
            if (oldRecord != null) {
                oldRecord.disposeLocked();
            }

            if (newUid) {
                p.addListener(callingUid);
            }

            boolean isProviderEnabled = isAllowedBySettingsLocked(provider);
            if (isProviderEnabled) {
                long minTimeForProvider = getMinTimeLocked(provider);
                Slog.i(TAG, "request " + provider + " (pid " + callingPid + ") " + minTime +
                        " " + minTimeForProvider + (singleShot ? " (singleshot)" : ""));
                p.setMinTime(minTimeForProvider, mTmpWorkSource);
                // try requesting single shot if singleShot is true, and fall back to
                // regular location tracking if requestSingleShotFix() is not supported
                if (!singleShot || !p.requestSingleShotFix()) {
                    p.enableLocationTracking(true);
                }
            } else {
                // Notify the listener that updates are currently disabled
                receiver.callProviderEnabledLocked(provider, false);
            }
            if (LOCAL_LOGV) {
                Slog.v(TAG, "_requestLocationUpdates: provider = " + provider + " listener = " + receiver);
=======
    public void updateProviders() {
        for (LocationProviderImpl p : LocationProviderImpl.getProviders()) {
            boolean isEnabled = p.isEnabled();
            String name = p.getName();
            boolean shouldBeEnabled = isAllowedBySettings(name);

            // Collection is only allowed when network provider is being used
            if (p.getName().equals(LocationManager.NETWORK_PROVIDER)) {
                mCollector.updateNetworkProviderStatus(shouldBeEnabled);
            }

            if (isEnabled && !shouldBeEnabled) {
                updateProviderListeners(name, false);
            } else if (!isEnabled && shouldBeEnabled) {
                updateProviderListeners(name, true);
            }

        }
    }

    private void updateProviderListeners(String provider, boolean enabled) {
        int listeners = 0;

        LocationProviderImpl p = LocationProviderImpl.getProvider(provider);
        if (p == null) {
            return;
        }

        synchronized (mRecordsByProvider) {

            HashSet<UpdateRecord> records = mRecordsByProvider.get(provider);
            if (records != null) {
                for (UpdateRecord record : records) {
                    // Sends a notification message to the listener
                    try {
                        if (enabled) {
                            record.mListener.mListener.onProviderEnabled(provider);
                        } else {
                            record.mListener.mListener.onProviderDisabled(provider);
                        }
                    } catch (RemoteException e) {
                        // The death link will clean this up.
                    }
                    listeners++;
                }
            }
        }

        if (enabled) {
            p.enable();
            if (listeners > 0) {
                p.setMinTime(getMinTime(provider));
                p.enableLocationTracking(true);
                updateWakelockStatus(mScreenOn);
            }
        } else {
            p.enableLocationTracking(false);
            p.disable();
            updateWakelockStatus(mScreenOn);
        }

        if (enabled && listeners > 0) {
            mLocationHandler.removeMessages(MESSAGE_HEARTBEAT, provider);
            Message m = Message.obtain(mLocationHandler, MESSAGE_HEARTBEAT, provider);
            mLocationHandler.sendMessageAtTime(m, SystemClock.uptimeMillis() + 1000);
        } else {
            mLocationHandler.removeMessages(MESSAGE_HEARTBEAT, provider);
        }
    }

    private long getMinTime(String provider) {
        long minTime = Long.MAX_VALUE;
        synchronized (mRecordsByProvider) {
            HashSet<UpdateRecord> records = mRecordsByProvider.get(provider);
            if (records != null) {
                for (UpdateRecord r : records) {
                    minTime = Math.min(minTime, r.mMinTime);
                }
            }
        }
        return minTime;
    }

    private class UpdateRecord {
        String mProvider;
        Listener mListener;
        long mMinTime;
        float mMinDistance;
        String[] mPackages;

        UpdateRecord(String provider, long minTime, float minDistance, Listener listener,
            String[] packages) {
            mProvider = provider;
            mListener = listener;
            mMinTime = minTime;
            mMinDistance = minDistance;
            mPackages = packages;

            synchronized (mRecordsByProvider) {
                HashSet<UpdateRecord> records = mRecordsByProvider.get(provider);
                if (records == null) {
                    records = new HashSet<UpdateRecord>();
                    mRecordsByProvider.put(provider, records);
                }
                records.add(this);
            }
        }

        /**
         * Method to be called when a record will no longer be used.  Calling this multiple times
         * must have the same effect as calling it once.
         */
        public void dispose() {
            synchronized (mRecordsByProvider) {
                HashSet<UpdateRecord> records = mRecordsByProvider.get(this.mProvider);
                records.remove(this);
            }
        }

        /**
         * Calls dispose().
         */
        @Override protected void finalize() {
            dispose();
        }
    }

    public void requestLocationUpdates(String provider,
        long minTime, float minDistance, ILocationListener listener) {

        try {
            _requestLocationUpdates(provider, minTime, minDistance, listener);
        } catch (SecurityException se) {
            throw se;
        } catch (Exception e) {
            Log.e(TAG, "requestUpdates got exception:", e);
        }
    }

    private void _requestLocationUpdates(String provider,
        long minTime, float minDistance, ILocationListener listener) {
        if (Config.LOGD) {
            Log.d(TAG, "_requestLocationUpdates: listener = " + listener.asBinder());
        }

        LocationProviderImpl impl = LocationProviderImpl.getProvider(provider);
        if (impl == null) {
            throw new IllegalArgumentException("provider=" + provider);
        }

        checkPermissions(provider);

        String[] packages = getPackageNames();

        // so wakelock calls will succeed
        long identity = Binder.clearCallingIdentity();
        try {
            Listener myListener = new Listener(listener);
            UpdateRecord r = new UpdateRecord(provider, minTime, minDistance, myListener, packages);

            synchronized (mLocationListeners) {
                IBinder binder = listener.asBinder();
                if (mListeners.get(binder) == null) {
                    try {
                        binder.linkToDeath(myListener, 0);
                        mListeners.put(binder, myListener);
                    } catch (RemoteException e) {
                        return;
                    }
                }

                HashMap<String,UpdateRecord> records = mLocationListeners.get(binder);
                if (records == null) {
                    records = new HashMap<String,UpdateRecord>();
                    mLocationListeners.put(binder, records);
                }
                UpdateRecord oldRecord = records.put(provider, r);
                if (oldRecord != null) {
                    oldRecord.dispose();
                }

                if (impl instanceof NetworkLocationProvider) {
                    ((NetworkLocationProvider) impl).addListener(packages);
                }

                boolean isProviderEnabled = isAllowedBySettings(provider);
                if (isProviderEnabled) {
                    long minTimeForProvider = getMinTime(provider);
                    impl.setMinTime(minTimeForProvider);
                    impl.enableLocationTracking(true);
                    updateWakelockStatus(mScreenOn);

                    // Clear heartbeats if any before starting a new one
                    mLocationHandler.removeMessages(MESSAGE_HEARTBEAT, provider);
                    Message m = Message.obtain(mLocationHandler, MESSAGE_HEARTBEAT, provider);
                    mLocationHandler.sendMessageAtTime(m, SystemClock.uptimeMillis() + 1000);

                } else {
                    try {
                        // Notify the listener that updates are currently disabled
                        listener.onProviderDisabled(provider);
                    } catch(RemoteException e) {
                        Log.w(TAG, "RemoteException calling onProviderDisabled on " + listener);
                    }
                }
>>>>>>> 54b6cfa... Initial Contribution
            }
        } finally {
            Binder.restoreCallingIdentity(identity);
        }
    }

<<<<<<< HEAD
    public void removeUpdates(ILocationListener listener, String packageName) {
        try {
            synchronized (mLock) {
                removeUpdatesLocked(getReceiver(listener, packageName));
            }
        } catch (SecurityException se) {
            throw se;
        } catch (IllegalArgumentException iae) {
            throw iae;
        } catch (Exception e) {
            Slog.e(TAG, "removeUpdates got exception:", e);
        }
    }

    public void removeUpdatesPI(PendingIntent intent, String packageName) {
        try {
            synchronized (mLock) {
                removeUpdatesLocked(getReceiver(intent, packageName));
            }
        } catch (SecurityException se) {
            throw se;
        } catch (IllegalArgumentException iae) {
            throw iae;
        } catch (Exception e) {
            Slog.e(TAG, "removeUpdates got exception:", e);
        }
    }

    private void removeUpdatesLocked(Receiver receiver) {
        if (LOCAL_LOGV) {
            Slog.v(TAG, "_removeUpdates: listener = " + receiver);
        }

        // so wakelock calls will succeed
        final int callingPid = Binder.getCallingPid();
        final int callingUid = Binder.getCallingUid();
        long identity = Binder.clearCallingIdentity();
        try {
            if (mReceivers.remove(receiver.mKey) != null && receiver.isListener()) {
                receiver.getListener().asBinder().unlinkToDeath(receiver, 0);
                synchronized(receiver) {
                    if(receiver.mPendingBroadcasts > 0) {
                        decrementPendingBroadcasts();
                        receiver.mPendingBroadcasts = 0;
                    }
                }
            }

            // Record which providers were associated with this listener
            HashSet<String> providers = new HashSet<String>();
            HashMap<String,UpdateRecord> oldRecords = receiver.mUpdateRecords;
            if (oldRecords != null) {
                // Call dispose() on the obsolete update records.
                for (UpdateRecord record : oldRecords.values()) {
                    if (!providerHasListener(record.mProvider, callingUid, receiver)) {
                        LocationProviderInterface p = mProvidersByName.get(record.mProvider);
                        if (p != null) {
                            p.removeListener(callingUid);
                        }
                    }
                    record.disposeLocked();
                }
                // Accumulate providers
                providers.addAll(oldRecords.keySet());
            }

            // See if the providers associated with this listener have any
            // other listeners; if one does, inform it of the new smallest minTime
            // value; if one does not, disable location tracking for it
            for (String provider : providers) {
                // If provider is already disabled, don't need to do anything
                if (!isAllowedBySettingsLocked(provider)) {
                    continue;
                }

                boolean hasOtherListener = false;
                ArrayList<UpdateRecord> recordsForProvider = mRecordsByProvider.get(provider);
                if (recordsForProvider != null && recordsForProvider.size() > 0) {
                    hasOtherListener = true;
                }

                LocationProviderInterface p = mProvidersByName.get(provider);
                if (p != null) {
                    if (hasOtherListener) {
                        long minTime = getMinTimeLocked(provider);
                        Slog.i(TAG, "remove " + provider + " (pid " + callingPid +
                                "), next minTime = " + minTime);
                        p.setMinTime(minTime, mTmpWorkSource);
                    } else {
                        Slog.i(TAG, "remove " + provider + " (pid " + callingPid +
                                "), disabled");
                        p.enableLocationTracking(false);
                    }
                }
=======
    public void removeUpdates(ILocationListener listener) {
        try {
            _removeUpdates(listener);
        } catch (SecurityException se) {
            throw se;
        } catch (Exception e) {
            Log.e(TAG, "removeUpdates got exception:", e);
        }
    }

    private void _removeUpdates(ILocationListener listener) {
        if (Config.LOGD) {
            Log.d(TAG, "_removeUpdates: listener = " + listener.asBinder());
        }

        // so wakelock calls will succeed
        long identity = Binder.clearCallingIdentity();
        try {
            synchronized (mLocationListeners) {
                IBinder binder = listener.asBinder();
                Listener myListener = mListeners.remove(binder);
                if (myListener != null) {
                    binder.unlinkToDeath(myListener, 0);
                }

                // Record which providers were associated with this listener
                HashSet<String> providers = new HashSet<String>();
                HashMap<String,UpdateRecord> oldRecords = mLocationListeners.get(binder);
                if (oldRecords != null) {
                    // Call dispose() on the obsolete update records.
                    for (UpdateRecord record : oldRecords.values()) {
                        if (record.mProvider.equals(LocationManager.NETWORK_PROVIDER)) {
                            if (mNetworkLocationProvider != null) {
                                mNetworkLocationProvider.removeListener(record.mPackages);
                            }
                        }
                        record.dispose();
                    }
                    // Accumulate providers
                    providers.addAll(oldRecords.keySet());
                }

                mLocationListeners.remove(binder);
                mLastFixBroadcast.remove(binder);
                mLastStatusBroadcast.remove(binder);

                // See if the providers associated with this listener have any
                // other listeners; if one does, inform it of the new smallest minTime
                // value; if one does not, disable location tracking for it
                for (String provider : providers) {
                    // If provider is already disabled, don't need to do anything
                    if (!isAllowedBySettings(provider)) {
                        continue;
                    }

                    boolean hasOtherListener = false;
                    synchronized (mRecordsByProvider) {
                        HashSet<UpdateRecord> recordsForProvider = mRecordsByProvider.get(provider);
                        if (recordsForProvider != null && recordsForProvider.size() > 0) {
                            hasOtherListener = true;
                        }
                    }

                    LocationProviderImpl p = LocationProviderImpl.getProvider(provider);
                    if (p != null) {
                        if (hasOtherListener) {
                            p.setMinTime(getMinTime(provider));
                        } else {
                            mLocationHandler.removeMessages(MESSAGE_HEARTBEAT, provider);
                            p.enableLocationTracking(false);
                        }
                    }
                }

                updateWakelockStatus(mScreenOn);                
>>>>>>> 54b6cfa... Initial Contribution
            }
        } finally {
            Binder.restoreCallingIdentity(identity);
        }
    }

    public boolean addGpsStatusListener(IGpsStatusListener listener) {
<<<<<<< HEAD
        if (mGpsStatusProvider == null) {
            return false;
        }
        if (mContext.checkCallingOrSelfPermission(ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("Requires ACCESS_FINE_LOCATION permission");
        }

        try {
            mGpsStatusProvider.addGpsStatusListener(listener);
        } catch (RemoteException e) {
            Slog.e(TAG, "mGpsStatusProvider.addGpsStatusListener failed", e);
=======
        if (mGpsLocationProvider == null) {
            return false;
        }
        try {
            mGpsLocationProvider.addGpsStatusListener(listener);
        } catch (RemoteException e) {
            Log.w(TAG, "RemoteException in addGpsStatusListener");
>>>>>>> 54b6cfa... Initial Contribution
            return false;
        }
        return true;
    }

    public void removeGpsStatusListener(IGpsStatusListener listener) {
<<<<<<< HEAD
        synchronized (mLock) {
            try {
                mGpsStatusProvider.removeGpsStatusListener(listener);
            } catch (Exception e) {
                Slog.e(TAG, "mGpsStatusProvider.removeGpsStatusListener failed", e);
            }
        }
    }

    public boolean sendExtraCommand(String provider, String command, Bundle extras) {
        if (provider == null) {
            // throw NullPointerException to remain compatible with previous implementation
            throw new NullPointerException();
        }

        // first check for permission to the provider
        checkPermissionsSafe(provider, null);
        // and check for ACCESS_LOCATION_EXTRA_COMMANDS
        if ((mContext.checkCallingOrSelfPermission(ACCESS_LOCATION_EXTRA_COMMANDS)
=======
        mGpsLocationProvider.removeGpsStatusListener(listener);
    }

    public boolean sendExtraCommand(String provider, String command, Bundle extras) {
        // first check for permission to the provider
        checkPermissions(provider);
        // and check for ACCESS_LOCATION_EXTRA_COMMANDS
        if ((mContext.checkCallingPermission(ACCESS_LOCATION_EXTRA_COMMANDS)
>>>>>>> 54b6cfa... Initial Contribution
                != PackageManager.PERMISSION_GRANTED)) {
            throw new SecurityException("Requires ACCESS_LOCATION_EXTRA_COMMANDS permission");
        }

<<<<<<< HEAD
        synchronized (mLock) {
            LocationProviderInterface p = mProvidersByName.get(provider);
            if (p == null) {
                return false;
            }
    
            return p.sendExtraCommand(command, extras);
        }
    }

    public boolean sendNiResponse(int notifId, int userResponse)
    {
        if (Binder.getCallingUid() != Process.myUid()) {
            throw new SecurityException(
                    "calling sendNiResponse from outside of the system is not allowed");
        }
        try {
            return mNetInitiatedListener.sendNiResponse(notifId, userResponse);
        }
        catch (RemoteException e)
        {
            Slog.e(TAG, "RemoteException in LocationManagerService.sendNiResponse");
            return false;
        }
    }

    class ProximityAlert {
        final int  mUid;
        final double mLatitude;
        final double mLongitude;
        final float mRadius;
        final long mExpiration;
        final PendingIntent mIntent;
        final Location mLocation;
        final String mPackageName;

        public ProximityAlert(int uid, double latitude, double longitude,
            float radius, long expiration, PendingIntent intent, String packageName) {
            mUid = uid;
=======
        LocationProviderImpl impl = LocationProviderImpl.getProvider(provider);
        if (provider == null) {
            return false;
        }

        return impl.sendExtraCommand(command, extras);
    }

    class ProximityAlert {
        double mLatitude;
        double mLongitude;
        float mRadius;
        long mExpiration;
        PendingIntent mIntent;
        Location mLocation;

        public ProximityAlert(double latitude, double longitude,
            float radius, long expiration, PendingIntent intent) {
>>>>>>> 54b6cfa... Initial Contribution
            mLatitude = latitude;
            mLongitude = longitude;
            mRadius = radius;
            mExpiration = expiration;
            mIntent = intent;
<<<<<<< HEAD
            mPackageName = packageName;
=======
>>>>>>> 54b6cfa... Initial Contribution

            mLocation = new Location("");
            mLocation.setLatitude(latitude);
            mLocation.setLongitude(longitude);
        }

<<<<<<< HEAD
        long getExpiration() {
            return mExpiration;
        }

        PendingIntent getIntent() {
            return mIntent;
        }

        boolean isInProximity(double latitude, double longitude, float accuracy) {
=======
        public long getExpiration() {
            return mExpiration;
        }

        public PendingIntent getIntent() {
            return mIntent;
        }

        public boolean isInProximity(double latitude, double longitude) {
>>>>>>> 54b6cfa... Initial Contribution
            Location loc = new Location("");
            loc.setLatitude(latitude);
            loc.setLongitude(longitude);

            double radius = loc.distanceTo(mLocation);
<<<<<<< HEAD
            return radius <= Math.max(mRadius,accuracy);
        }
        
        @Override
        public String toString() {
            return "ProximityAlert{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " uid " + mUid + mIntent + "}";
        }
        
        void dump(PrintWriter pw, String prefix) {
            pw.println(prefix + this);
            pw.println(prefix + "mLatitude=" + mLatitude + " mLongitude=" + mLongitude);
            pw.println(prefix + "mRadius=" + mRadius + " mExpiration=" + mExpiration);
            pw.println(prefix + "mIntent=" + mIntent);
            pw.println(prefix + "mLocation:");
            mLocation.dump(new PrintWriterPrinter(pw), prefix + "  ");
=======
            return radius <= mRadius;
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    // Listener for receiving locations to trigger proximity alerts
<<<<<<< HEAD
    class ProximityListener extends ILocationListener.Stub implements PendingIntent.OnFinished {

        boolean isGpsAvailable = false;

        // Note: this is called with the lock held.
=======
    class ProximityListener extends ILocationListener.Stub {

        boolean isGpsAvailable = false;

>>>>>>> 54b6cfa... Initial Contribution
        public void onLocationChanged(Location loc) {

            // If Gps is available, then ignore updates from NetworkLocationProvider
            if (loc.getProvider().equals(LocationManager.GPS_PROVIDER)) {
                isGpsAvailable = true;
            }
            if (isGpsAvailable && loc.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
                return;
            }

            // Process proximity alerts
            long now = System.currentTimeMillis();
            double latitude = loc.getLatitude();
            double longitude = loc.getLongitude();
<<<<<<< HEAD
            float accuracy = loc.getAccuracy();
            ArrayList<PendingIntent> intentsToRemove = null;

            for (ProximityAlert alert : mProximityAlerts.values()) {
                PendingIntent intent = alert.getIntent();
                long expiration = alert.getExpiration();

                if (inBlacklist(alert.mPackageName)) {
                    continue;
                }

                if ((expiration == -1) || (now <= expiration)) {
                    boolean entered = mProximitiesEntered.contains(alert);
                    boolean inProximity =
                        alert.isInProximity(latitude, longitude, accuracy);
                    if (!entered && inProximity) {
                        if (LOCAL_LOGV) {
                            Slog.v(TAG, "Entered alert");
=======
            ArrayList<PendingIntent> intentsToRemove = null;

            for (ProximityAlert alert : mProximityAlerts.values()) {

                PendingIntent intent = alert.getIntent();
                long expiration = alert.getExpiration();

                if ((expiration == -1) || (now <= expiration)) {
                    boolean entered = mProximitiesEntered.contains(alert);
                    boolean inProximity =
                        alert.isInProximity(latitude, longitude);
                    if (!entered && inProximity) {
                        if (Config.LOGD) {
                            Log.i(TAG, "Entered alert");
>>>>>>> 54b6cfa... Initial Contribution
                        }
                        mProximitiesEntered.add(alert);
                        Intent enteredIntent = new Intent();
                        enteredIntent.putExtra(LocationManager.KEY_PROXIMITY_ENTERING, true);
                        try {
<<<<<<< HEAD
                            synchronized (this) {
                                // synchronize to ensure incrementPendingBroadcasts()
                                // is called before decrementPendingBroadcasts()
                                intent.send(mContext, 0, enteredIntent, this, mLocationHandler,
                                        ACCESS_FINE_LOCATION);
                                // call this after broadcasting so we do not increment
                                // if we throw an exeption.
                                incrementPendingBroadcasts();
                            }
                        } catch (PendingIntent.CanceledException e) {
                            if (LOCAL_LOGV) {
                                Slog.v(TAG, "Canceled proximity alert: " + alert, e);
=======
                            intent.send(mContext, 0, enteredIntent, null, null);
                        } catch (PendingIntent.CanceledException e) {
                            if (Config.LOGD) {
                                Log.i(TAG, "Canceled proximity alert: " + alert, e);
>>>>>>> 54b6cfa... Initial Contribution
                            }
                            if (intentsToRemove == null) {
                                intentsToRemove = new ArrayList<PendingIntent>();
                            }
                            intentsToRemove.add(intent);
                        }
                    } else if (entered && !inProximity) {
<<<<<<< HEAD
                        if (LOCAL_LOGV) {
                            Slog.v(TAG, "Exited alert");
=======
                        if (Config.LOGD) {
                            Log.i(TAG, "Exited alert");
>>>>>>> 54b6cfa... Initial Contribution
                        }
                        mProximitiesEntered.remove(alert);
                        Intent exitedIntent = new Intent();
                        exitedIntent.putExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);
                        try {
<<<<<<< HEAD
                            synchronized (this) {
                                // synchronize to ensure incrementPendingBroadcasts()
                                // is called before decrementPendingBroadcasts()
                                intent.send(mContext, 0, exitedIntent, this, mLocationHandler,
                                        ACCESS_FINE_LOCATION);
                                // call this after broadcasting so we do not increment
                                // if we throw an exeption.
                                incrementPendingBroadcasts();
                            }
                        } catch (PendingIntent.CanceledException e) {
                            if (LOCAL_LOGV) {
                                Slog.v(TAG, "Canceled proximity alert: " + alert, e);
=======
                            intent.send(mContext, 0, exitedIntent, null, null);
                        } catch (PendingIntent.CanceledException e) {
                            if (Config.LOGD) {
                                Log.i(TAG, "Canceled proximity alert: " + alert, e);
>>>>>>> 54b6cfa... Initial Contribution
                            }
                            if (intentsToRemove == null) {
                                intentsToRemove = new ArrayList<PendingIntent>();
                            }
                            intentsToRemove.add(intent);
                        }
                    }
                } else {
                    // Mark alert for expiration
<<<<<<< HEAD
                    if (LOCAL_LOGV) {
                        Slog.v(TAG, "Expiring proximity alert: " + alert);
=======
                    if (Config.LOGD) {
                        Log.i(TAG, "Expiring proximity alert: " + alert);
>>>>>>> 54b6cfa... Initial Contribution
                    }
                    if (intentsToRemove == null) {
                        intentsToRemove = new ArrayList<PendingIntent>();
                    }
                    intentsToRemove.add(alert.getIntent());
                }
            }

            // Remove expired alerts
            if (intentsToRemove != null) {
                for (PendingIntent i : intentsToRemove) {
<<<<<<< HEAD
                    ProximityAlert alert = mProximityAlerts.get(i);
                    mProximitiesEntered.remove(alert);
                    removeProximityAlertLocked(i);
                }
            }
        }

        // Note: this is called with the lock held.
=======
                    mProximityAlerts.remove(i);
                    ProximityAlert alert = mProximityAlerts.get(i);
                    mProximitiesEntered.remove(alert);
                }
            }

        }

>>>>>>> 54b6cfa... Initial Contribution
        public void onProviderDisabled(String provider) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
                isGpsAvailable = false;
            }
        }

<<<<<<< HEAD
        // Note: this is called with the lock held.
=======
>>>>>>> 54b6cfa... Initial Contribution
        public void onProviderEnabled(String provider) {
            // ignore
        }

<<<<<<< HEAD
        // Note: this is called with the lock held.
=======
>>>>>>> 54b6cfa... Initial Contribution
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if ((provider.equals(LocationManager.GPS_PROVIDER)) &&
                (status != LocationProvider.AVAILABLE)) {
                isGpsAvailable = false;
            }
        }
<<<<<<< HEAD

        public void onSendFinished(PendingIntent pendingIntent, Intent intent,
                int resultCode, String resultData, Bundle resultExtras) {
            // synchronize to ensure incrementPendingBroadcasts()
            // is called before decrementPendingBroadcasts()
            synchronized (this) {
                decrementPendingBroadcasts();
            }
        }
    }

    public void addProximityAlert(double latitude, double longitude,
        float radius, long expiration, PendingIntent intent, String packageName) {
        validatePendingIntent(intent);
        try {
            synchronized (mLock) {
                addProximityAlertLocked(latitude, longitude, radius, expiration, intent,
                        packageName);
            }
        } catch (SecurityException se) {
            throw se;
        } catch (IllegalArgumentException iae) {
            throw iae;
        } catch (Exception e) {
            Slog.e(TAG, "addProximityAlert got exception:", e);
        }
    }

    private void addProximityAlertLocked(double latitude, double longitude,
        float radius, long expiration, PendingIntent intent, String packageName) {
        if (LOCAL_LOGV) {
            Slog.v(TAG, "addProximityAlert: latitude = " + latitude +
=======
    }

    public void addProximityAlert(double latitude, double longitude,
        float radius, long expiration, PendingIntent intent) {
        try {
          _addProximityAlert(latitude, longitude, radius, expiration, intent);
        } catch (SecurityException se) {
            throw se;
        } catch (Exception e) {
            Log.e(TAG, "addProximityAlert got exception:", e);
        }
    }

    private void _addProximityAlert(double latitude, double longitude,
        float radius, long expiration, PendingIntent intent) {
        if (Config.LOGD) {
            Log.d(TAG, "addProximityAlert: latitude = " + latitude +
>>>>>>> 54b6cfa... Initial Contribution
                    ", longitude = " + longitude +
                    ", expiration = " + expiration +
                    ", intent = " + intent);
        }

<<<<<<< HEAD
        checkPackageName(Binder.getCallingUid(), packageName);

        // Require ability to access all providers for now
        if (!isAllowedProviderSafe(LocationManager.GPS_PROVIDER) ||
            !isAllowedProviderSafe(LocationManager.NETWORK_PROVIDER)) {
=======
        // Require ability to access all providers for now
        if (!isAllowedProvider(LocationManager.GPS_PROVIDER) ||
            !isAllowedProvider(LocationManager.NETWORK_PROVIDER)) {
>>>>>>> 54b6cfa... Initial Contribution
            throw new SecurityException("Requires ACCESS_FINE_LOCATION permission");
        }

        if (expiration != -1) {
            expiration += System.currentTimeMillis();
        }
<<<<<<< HEAD
        ProximityAlert alert = new ProximityAlert(Binder.getCallingUid(),
                latitude, longitude, radius, expiration, intent, packageName);
        mProximityAlerts.put(intent, alert);

        if (mProximityReceiver == null) {
            mProximityListener = new ProximityListener();
            mProximityReceiver = new Receiver(mProximityListener, packageName);

            for (int i = mProviders.size() - 1; i >= 0; i--) {
                LocationProviderInterface provider = mProviders.get(i);
                requestLocationUpdatesLocked(provider.getName(), 1000L, 1.0f,
                        false, mProximityReceiver);
=======
        ProximityAlert alert = new ProximityAlert(latitude, longitude, radius, expiration, intent);
        mProximityAlerts.put(intent, alert);

        if (mProximityListener == null) {
            mProximityListener = new ProximityListener();

            LocationProvider provider = LocationProviderImpl.getProvider(
                LocationManager.GPS_PROVIDER);
            if (provider != null) {
                _requestLocationUpdates(provider.getName(), 1000L, 1.0f, mProximityListener);
            }

            provider =
                LocationProviderImpl.getProvider(LocationManager.NETWORK_PROVIDER);
            if (provider != null) {
                _requestLocationUpdates(provider.getName(), 1000L, 1.0f, mProximityListener);
>>>>>>> 54b6cfa... Initial Contribution
            }
        }
    }

    public void removeProximityAlert(PendingIntent intent) {
        try {
<<<<<<< HEAD
            synchronized (mLock) {
               removeProximityAlertLocked(intent);
            }
        } catch (SecurityException se) {
            throw se;
        } catch (IllegalArgumentException iae) {
            throw iae;
        } catch (Exception e) {
            Slog.e(TAG, "removeProximityAlert got exception:", e);
        }
    }

    private void removeProximityAlertLocked(PendingIntent intent) {
        if (LOCAL_LOGV) {
            Slog.v(TAG, "removeProximityAlert: intent = " + intent);
=======
           _removeProximityAlert(intent);
        } catch (SecurityException se) {
            throw se;
        } catch (Exception e) {
            Log.e(TAG, "removeProximityAlert got exception:", e);
        }
    }

    private void _removeProximityAlert(PendingIntent intent) {
        if (Config.LOGD) {
            Log.d(TAG, "removeProximityAlert: intent = " + intent);
>>>>>>> 54b6cfa... Initial Contribution
        }

        mProximityAlerts.remove(intent);
        if (mProximityAlerts.size() == 0) {
<<<<<<< HEAD
            if (mProximityReceiver != null) {
                removeUpdatesLocked(mProximityReceiver);
            }
            mProximityReceiver = null;
=======
            _removeUpdates(mProximityListener);
>>>>>>> 54b6cfa... Initial Contribution
            mProximityListener = null;
        }
     }

    /**
<<<<<<< HEAD
     * @return null if the provider does not exist
     * @throws SecurityException if the provider is not allowed to be
=======
     * @return null if the provider does not exits
     * @throw SecurityException if the provider is not allowed to be
>>>>>>> 54b6cfa... Initial Contribution
     * accessed by the caller
     */
    public Bundle getProviderInfo(String provider) {
        try {
<<<<<<< HEAD
            synchronized (mLock) {
                return _getProviderInfoLocked(provider);
            }
        } catch (SecurityException se) {
            throw se;
        } catch (IllegalArgumentException iae) {
            throw iae;
        } catch (Exception e) {
            Slog.e(TAG, "_getProviderInfo got exception:", e);
=======
            return _getProviderInfo(provider);
        } catch (SecurityException se) {
            throw se;
        } catch (Exception e) {
            Log.e(TAG, "_getProviderInfo got exception:", e);
>>>>>>> 54b6cfa... Initial Contribution
            return null;
        }
    }

<<<<<<< HEAD
    private Bundle _getProviderInfoLocked(String provider) {
        LocationProviderInterface p = mProvidersByName.get(provider);
=======
    private Bundle _getProviderInfo(String provider) {
        LocationProviderImpl p = LocationProviderImpl.getProvider(provider);
>>>>>>> 54b6cfa... Initial Contribution
        if (p == null) {
            return null;
        }

<<<<<<< HEAD
        checkPermissionsSafe(provider, null);
=======
        checkPermissions(provider);
>>>>>>> 54b6cfa... Initial Contribution

        Bundle b = new Bundle();
        b.putBoolean("network", p.requiresNetwork());
        b.putBoolean("satellite", p.requiresSatellite());
        b.putBoolean("cell", p.requiresCell());
        b.putBoolean("cost", p.hasMonetaryCost());
        b.putBoolean("altitude", p.supportsAltitude());
        b.putBoolean("speed", p.supportsSpeed());
        b.putBoolean("bearing", p.supportsBearing());
        b.putInt("power", p.getPowerRequirement());
        b.putInt("accuracy", p.getAccuracy());

        return b;
    }

    public boolean isProviderEnabled(String provider) {
        try {
<<<<<<< HEAD
            synchronized (mLock) {
                return _isProviderEnabledLocked(provider);
            }
        } catch (SecurityException se) {
            throw se;
        } catch (Exception e) {
            Slog.e(TAG, "isProviderEnabled got exception:", e);
=======
            return _isProviderEnabled(provider);
        } catch (SecurityException se) {
            throw se;
        } catch (Exception e) {
            Log.e(TAG, "isProviderEnabled got exception:", e);
>>>>>>> 54b6cfa... Initial Contribution
            return false;
        }
    }

<<<<<<< HEAD
    public void reportLocation(Location location, boolean passive) {
        if (mContext.checkCallingOrSelfPermission(INSTALL_LOCATION_PROVIDER)
                != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("Requires INSTALL_LOCATION_PROVIDER permission");
        }

        mLocationHandler.removeMessages(MESSAGE_LOCATION_CHANGED, location);
        Message m = Message.obtain(mLocationHandler, MESSAGE_LOCATION_CHANGED, location);
        m.arg1 = (passive ? 1 : 0);
        mLocationHandler.sendMessageAtFrontOfQueue(m);
    }

    private boolean _isProviderEnabledLocked(String provider) {
        checkPermissionsSafe(provider, null);

        LocationProviderInterface p = mProvidersByName.get(provider);
        if (p == null) {
            return false;
        }
        return isAllowedBySettingsLocked(provider);
    }

    public Location getLastKnownLocation(String provider, String packageName) {
        if (LOCAL_LOGV) {
            Slog.v(TAG, "getLastKnownLocation: " + provider);
        }
        try {
            synchronized (mLock) {
                return _getLastKnownLocationLocked(provider, packageName);
            }
        } catch (SecurityException se) {
            throw se;
        } catch (Exception e) {
            Slog.e(TAG, "getLastKnownLocation got exception:", e);
=======
    private boolean _isProviderEnabled(String provider) {
        checkPermissions(provider);

        LocationProviderImpl p = LocationProviderImpl.getProvider(provider);
        if (p == null) {
            throw new IllegalArgumentException("provider=" + provider);
        }
        return isAllowedBySettings(provider);
    }

    public Location getLastKnownLocation(String provider) {
        try {
            return _getLastKnownLocation(provider);
        } catch (SecurityException se) {
            throw se;
        } catch (Exception e) {
            Log.e(TAG, "getLastKnownLocation got exception:", e);
>>>>>>> 54b6cfa... Initial Contribution
            return null;
        }
    }

<<<<<<< HEAD
    private Location _getLastKnownLocationLocked(String provider, String packageName) {
        checkPermissionsSafe(provider, null);
        checkPackageName(Binder.getCallingUid(), packageName);

        LocationProviderInterface p = mProvidersByName.get(provider);
        if (p == null) {
            return null;
        }

        if (!isAllowedBySettingsLocked(provider)) {
            return null;
        }

        if (inBlacklist(packageName)) {
            return null;
        }

        return mLastKnownLocation.get(provider);
    }

    private static boolean shouldBroadcastSafe(Location loc, Location lastLoc, UpdateRecord record) {
=======
    private Location _getLastKnownLocation(String provider) {
        checkPermissions(provider);

        LocationProviderImpl p = LocationProviderImpl.getProvider(provider);
        if (p == null) {
            throw new IllegalArgumentException("provider=" + provider);
        }

        if (!isAllowedBySettings(provider)) {
            return null;
        }

        Location location = mLastKnownLocation.get(provider);
        if (location == null) {
            // Get the persistent last known location for the provider
            location = readLastKnownLocation(provider);
            if (location != null) {
                mLastKnownLocation.put(provider, location);
            }
        }

        return location;
    }

    private boolean shouldBroadcast(Location loc, Location lastLoc, UpdateRecord record) {
>>>>>>> 54b6cfa... Initial Contribution
        // Always broadcast the first update
        if (lastLoc == null) {
            return true;
        }

<<<<<<< HEAD
        // Check whether sufficient time has passed
        long minTime = record.mMinTime;
        if (loc.getTime() - lastLoc.getTime() < minTime - MAX_PROVIDER_SCHEDULING_JITTER) {
=======
        // Don't broadcast same location again regardless of condition
        // TODO - we should probably still rebroadcast if user explicitly sets a minTime > 0
        if (loc.getTime() == lastLoc.getTime()) {
>>>>>>> 54b6cfa... Initial Contribution
            return false;
        }

        // Check whether sufficient distance has been traveled
        double minDistance = record.mMinDistance;
        if (minDistance > 0.0) {
            if (loc.distanceTo(lastLoc) <= minDistance) {
                return false;
            }
        }

        return true;
    }

<<<<<<< HEAD
    private void handleLocationChangedLocked(Location location, boolean passive) {
        String provider = (passive ? LocationManager.PASSIVE_PROVIDER : location.getProvider());
        ArrayList<UpdateRecord> records = mRecordsByProvider.get(provider);
=======
    private void handleLocationChanged(String provider) {
        HashSet<UpdateRecord> records = mRecordsByProvider.get(provider);
>>>>>>> 54b6cfa... Initial Contribution
        if (records == null || records.size() == 0) {
            return;
        }

<<<<<<< HEAD
        LocationProviderInterface p = mProvidersByName.get(provider);
=======
        LocationProviderImpl p = LocationProviderImpl.getProvider(provider);
>>>>>>> 54b6cfa... Initial Contribution
        if (p == null) {
            return;
        }

<<<<<<< HEAD
        // Update last known location for provider
        Location lastLocation = mLastKnownLocation.get(provider);
        if (lastLocation == null) {
            mLastKnownLocation.put(provider, new Location(location));
        } else {
            lastLocation.set(location);
=======
        // Get location object
        Location loc = mLocationsByProvider.get(provider);
        if (loc == null) {
            loc = new Location(provider);
            mLocationsByProvider.put(provider, loc);
        } else {
            loc.reset();
        }

        // Use the mock location if available
        Location mockLoc = mMockProviderLocation.get(provider);
        boolean locationValid;
        if (mockLoc != null) {
            locationValid = true;
            loc.set(mockLoc);
        } else {
            locationValid = p.getLocation(loc);
        }

        // Update last known location for provider
        if (locationValid) {
            Location location = mLastKnownLocation.get(provider);
            if (location == null) {
                mLastKnownLocation.put(provider, new Location(loc));
            } else {
                location.set(loc);
            }
            writeLastKnownLocation(provider, loc);

            if (p instanceof NetworkLocationProvider) {
                mWakeLockNetworkReceived = true;
            } else if (p instanceof GpsLocationProvider) {
                // Gps location received signal is in NetworkStateBroadcastReceiver
            }
>>>>>>> 54b6cfa... Initial Contribution
        }

        // Fetch latest status update time
        long newStatusUpdateTime = p.getStatusUpdateTime();

<<<<<<< HEAD
       // Get latest status
        Bundle extras = new Bundle();
        int status = p.getStatus(extras);

        ArrayList<Receiver> deadReceivers = null;
        
        // Broadcast location or status to all listeners
        final int N = records.size();
        for (int i=0; i<N; i++) {
            UpdateRecord r = records.get(i);
            Receiver receiver = r.mReceiver;
            boolean receiverDead = false;

            if (inBlacklist(receiver.mPackageName)) {
                continue;
            }

            Location lastLoc = r.mLastFixBroadcast;
            if ((lastLoc == null) || shouldBroadcastSafe(location, lastLoc, r)) {
                if (lastLoc == null) {
                    lastLoc = new Location(location);
                    r.mLastFixBroadcast = lastLoc;
                } else {
                    lastLoc.set(location);
                }
                if (!receiver.callLocationChangedLocked(location)) {
                    Slog.w(TAG, "RemoteException calling onLocationChanged on " + receiver);
                    receiverDead = true;
                }
            }

            long prevStatusUpdateTime = r.mLastStatusBroadcast;
            if ((newStatusUpdateTime > prevStatusUpdateTime) &&
                (prevStatusUpdateTime != 0 || status != LocationProvider.AVAILABLE)) {

                r.mLastStatusBroadcast = newStatusUpdateTime;
                if (!receiver.callStatusChangedLocked(provider, status, extras)) {
                    receiverDead = true;
                    Slog.w(TAG, "RemoteException calling onStatusChanged on " + receiver);
                }
            }

            // remove receiver if it is dead or we just processed a single shot request
            if (receiverDead || r.mSingleShot) {
                if (deadReceivers == null) {
                    deadReceivers = new ArrayList<Receiver>();
                }
                if (!deadReceivers.contains(receiver)) {
                    deadReceivers.add(receiver);
                }
            }
        }
        
        if (deadReceivers != null) {
            for (int i=deadReceivers.size()-1; i>=0; i--) {
                removeUpdatesLocked(deadReceivers.get(i));
            }
        }
=======
        // Override real time with mock time if present
        Long mockStatusUpdateTime = mMockProviderStatusUpdateTime.get(provider);
        if (mockStatusUpdateTime != null) {
            newStatusUpdateTime = mockStatusUpdateTime.longValue();
        }

        // Get latest status
        Bundle extras = new Bundle();
        int status = p.getStatus(extras);

        // Override status with mock status if present
        Integer mockStatus = mMockProviderStatus.get(provider);
        if (mockStatus != null) {
            status = mockStatus.intValue();
        }

        // Override extras with mock extras if present
        Bundle mockExtras = mMockProviderStatusExtras.get(provider);
        if (mockExtras != null) {
            extras.clear();
            extras.putAll(mockExtras);
        }

        // Broadcast location or status to all listeners
        for (UpdateRecord r : records) {
            ILocationListener listener = r.mListener.mListener;
            IBinder binder = listener.asBinder();

            // Broadcast location only if it is valid
            if (locationValid) {
                HashMap<String,Location> map = mLastFixBroadcast.get(binder);
                if (map == null) {
                    map = new HashMap<String,Location>();
                    mLastFixBroadcast.put(binder, map);
                }
                Location lastLoc = map.get(provider);
                if ((lastLoc == null) || shouldBroadcast(loc, lastLoc, r)) {
                    if (lastLoc == null) {
                        lastLoc = new Location(loc);
                        map.put(provider, lastLoc);
                    } else {
                        lastLoc.set(loc);
                    }
                    try {
                        listener.onLocationChanged(loc);
                    } catch (RemoteException doe) {
                        Log.w(TAG, "RemoteException calling onLocationChanged on " + listener);
                        _removeUpdates(listener);
                    }
                }
            }

            // Broadcast status message
            HashMap<String,Long> statusMap = mLastStatusBroadcast.get(binder);
            if (statusMap == null) {
                statusMap = new HashMap<String,Long>();
                mLastStatusBroadcast.put(binder, statusMap);
            }
            long prevStatusUpdateTime =
                (statusMap.get(provider) != null) ? statusMap.get(provider) : 0;

            if ((newStatusUpdateTime > prevStatusUpdateTime) &&
                (prevStatusUpdateTime != 0 || status != LocationProvider.AVAILABLE)) {

                statusMap.put(provider, newStatusUpdateTime);
                try {
                    listener.onStatusChanged(provider, status, extras);
                } catch (RemoteException doe) {
                    Log.w(TAG, "RemoteException calling onStatusChanged on " + listener);
                    _removeUpdates(listener);
                }
            }
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    private class LocationWorkerHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            try {
<<<<<<< HEAD
                if (msg.what == MESSAGE_LOCATION_CHANGED) {
                    // log("LocationWorkerHandler: MESSAGE_LOCATION_CHANGED!");

                    synchronized (mLock) {
                        Location location = (Location) msg.obj;
                        String provider = location.getProvider();
                        boolean passive = (msg.arg1 == 1);

                        if (!passive) {
                            // notify other providers of the new location
                            for (int i = mProviders.size() - 1; i >= 0; i--) {
                                LocationProviderInterface p = mProviders.get(i);
                                if (!provider.equals(p.getName())) {
                                    p.updateLocation(location);
                                }
                            }
                        }

                        if (isAllowedBySettingsLocked(provider)) {
                            handleLocationChangedLocked(location, passive);
                        }
                    }
                } else if (msg.what == MESSAGE_PACKAGE_UPDATED) {
                    String packageName = (String) msg.obj;

                    // reconnect to external providers if there is a better package
                    if (mNetworkLocationProviderPackageName != null &&
                            mPackageManager.resolveService(
                            new Intent(LocationProviderProxy.SERVICE_ACTION)
                            .setPackage(packageName), 0) != null) {
                        // package implements service, perform full check
                        String bestPackage = findBestPackage(
                                LocationProviderProxy.SERVICE_ACTION,
                                mNetworkLocationProviderPackageName);
                        if (packageName.equals(bestPackage)) {
                            mNetworkLocationProvider.reconnect(bestPackage);
                            mNetworkLocationProviderPackageName = packageName;
                        }
                    }
                    if (mGeocodeProviderPackageName != null &&
                            mPackageManager.resolveService(
                            new Intent(GeocoderProxy.SERVICE_ACTION)
                            .setPackage(packageName), 0) != null) {
                        // package implements service, perform full check
                        String bestPackage = findBestPackage(
                                GeocoderProxy.SERVICE_ACTION,
                                mGeocodeProviderPackageName);
                        if (packageName.equals(bestPackage)) {
                            mGeocodeProvider.reconnect(bestPackage);
                            mGeocodeProviderPackageName = packageName;
                        }
                    }
                }
            } catch (Exception e) {
                // Log, don't crash!
                Slog.e(TAG, "Exception in LocationWorkerHandler.handleMessage:", e);
=======
                if (msg.what == MESSAGE_HEARTBEAT) {
                    // log("LocationWorkerHandler: Heartbeat!");

                    synchronized (mRecordsByProvider) {
                        String provider = (String) msg.obj;
                        if (!isAllowedBySettings(provider)) {
                            return;
                        }

                        // Process the location fix if the screen is on or we're holding a wakelock
                        if (mScreenOn || (mWakeLockAcquireTime != 0)) {
                            handleLocationChanged(provider);
                        }

                        // If it continues to have listeners
                        HashSet<UpdateRecord> records = mRecordsByProvider.get(provider);
                        if (records != null && records.size() > 0) {
                            Message m = Message.obtain(this, MESSAGE_HEARTBEAT, provider);
                            sendMessageAtTime(m, SystemClock.uptimeMillis() + 1000);
                        }
                    }

                    if ((mWakeLockAcquireTime != 0) &&
                        (SystemClock.elapsedRealtime() - mWakeLockAcquireTime > MAX_TIME_FOR_WAKE_LOCK)) {

                        removeMessages(MESSAGE_ACQUIRE_WAKE_LOCK);
                        removeMessages(MESSAGE_RELEASE_WAKE_LOCK);

                        log("LocationWorkerHandler: Exceeded max time for wake lock");
                        Message m = Message.obtain(this, MESSAGE_RELEASE_WAKE_LOCK);
                        sendMessageAtFrontOfQueue(m);

                    } else if (mWakeLockAcquireTime != 0 &&
                        mWakeLockGpsReceived && mWakeLockNetworkReceived) {

                        removeMessages(MESSAGE_ACQUIRE_WAKE_LOCK);
                        removeMessages(MESSAGE_RELEASE_WAKE_LOCK);

                        log("LocationWorkerHandler: Locations received.");
                        mWakeLockAcquireTime = 0;
                        Message m = Message.obtain(this, MESSAGE_RELEASE_WAKE_LOCK);
                        sendMessageDelayed(m, TIME_AFTER_WAKE_LOCK);
                    }

                } else if (msg.what == MESSAGE_ACQUIRE_WAKE_LOCK) {
                    log("LocationWorkerHandler: Acquire");
                    acquireWakeLock();
                } else if (msg.what == MESSAGE_RELEASE_WAKE_LOCK) {
                    log("LocationWorkerHandler: Release");
                    
                    // Update wakelock status so the next alarm is set before releasing wakelock
                    updateWakelockStatus(mScreenOn);
                    releaseWakeLock();
                }
            } catch (Exception e) {
                // Log, don't crash!
                Log.e(TAG, "Exception in LocationWorkerHandler.handleMessage:", e);
>>>>>>> 54b6cfa... Initial Contribution
            }
        }
    }

<<<<<<< HEAD
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            boolean queryRestart = action.equals(Intent.ACTION_QUERY_PACKAGE_RESTART);
            if (queryRestart
                    || action.equals(Intent.ACTION_PACKAGE_REMOVED)
                    || action.equals(Intent.ACTION_PACKAGE_RESTARTED)
                    || action.equals(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE)) {
                synchronized (mLock) {
                    int uidList[] = null;
                    if (action.equals(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE)) {
                        uidList = intent.getIntArrayExtra(Intent.EXTRA_CHANGED_UID_LIST);
                    } else {
                        uidList = new int[]{intent.getIntExtra(Intent.EXTRA_UID, -1)};
                    }
                    if (uidList == null || uidList.length == 0) {
                        return;
                    }
                    for (int uid : uidList) {
                        if (uid >= 0) {
                            ArrayList<Receiver> removedRecs = null;
                            for (ArrayList<UpdateRecord> i : mRecordsByProvider.values()) {
                                for (int j=i.size()-1; j>=0; j--) {
                                    UpdateRecord ur = i.get(j);
                                    if (ur.mReceiver.isPendingIntent() && ur.mUid == uid) {
                                        if (queryRestart) {
                                            setResultCode(Activity.RESULT_OK);
                                            return;
                                        }
                                        if (removedRecs == null) {
                                            removedRecs = new ArrayList<Receiver>();
                                        }
                                        if (!removedRecs.contains(ur.mReceiver)) {
                                            removedRecs.add(ur.mReceiver);
                                        }
                                    }
                                }
                            }
                            ArrayList<ProximityAlert> removedAlerts = null;
                            for (ProximityAlert i : mProximityAlerts.values()) {
                                if (i.mUid == uid) {
                                    if (queryRestart) {
                                        setResultCode(Activity.RESULT_OK);
                                        return;
                                    }
                                    if (removedAlerts == null) {
                                        removedAlerts = new ArrayList<ProximityAlert>();
                                    }
                                    if (!removedAlerts.contains(i)) {
                                        removedAlerts.add(i);
                                    }
                                }
                            }
                            if (removedRecs != null) {
                                for (int i=removedRecs.size()-1; i>=0; i--) {
                                    removeUpdatesLocked(removedRecs.get(i));
                                }
                            }
                            if (removedAlerts != null) {
                                for (int i=removedAlerts.size()-1; i>=0; i--) {
                                    removeProximityAlertLocked(removedAlerts.get(i).mIntent);
                                }
                            }
                        }
                    }
                }
            } else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                boolean noConnectivity =
                    intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
                if (!noConnectivity) {
                    mNetworkState = LocationProvider.AVAILABLE;
                } else {
                    mNetworkState = LocationProvider.TEMPORARILY_UNAVAILABLE;
                }

                final ConnectivityManager connManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo info = connManager.getActiveNetworkInfo();

                // Notify location providers of current network state
                synchronized (mLock) {
                    for (int i = mProviders.size() - 1; i >= 0; i--) {
                        LocationProviderInterface provider = mProviders.get(i);
                        if (provider.requiresNetwork()) {
                            provider.updateNetworkState(mNetworkState, info);
                        }
                    }
                }
            }
        }
    };

    private final PackageMonitor mPackageMonitor = new PackageMonitor() {
        @Override
        public void onPackageUpdateFinished(String packageName, int uid) {
            // Called by main thread; divert work to LocationWorker.
            Message.obtain(mLocationHandler, MESSAGE_PACKAGE_UPDATED, packageName).sendToTarget();
        }
        @Override
        public void onPackageAdded(String packageName, int uid) {
            // Called by main thread; divert work to LocationWorker.
            Message.obtain(mLocationHandler, MESSAGE_PACKAGE_UPDATED, packageName).sendToTarget();
        }
    };

    // Wake locks

    private void incrementPendingBroadcasts() {
        synchronized (mWakeLock) {
            if (mPendingBroadcasts++ == 0) {
                try {
                    mWakeLock.acquire();
                    log("Acquired wakelock");
                } catch (Exception e) {
                    // This is to catch a runtime exception thrown when we try to release an
                    // already released lock.
                    Slog.e(TAG, "exception in acquireWakeLock()", e);
                }
=======
    PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        @Override
        public void onCellLocationChanged(CellLocation cellLocation) {
            try {
                ServiceState serviceState = mServiceState;

                // Gets cell state
                CellState cellState = new CellState(serviceState, cellLocation);

                // Notify collector
                mCollector.updateCellState(cellState);

                // Updates providers
                List<LocationProviderImpl> providers = LocationProviderImpl.getProviders();
                for (LocationProviderImpl provider : providers) {
                    if (provider.requiresCell()) {
                        provider.updateCellState(cellState);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception in PhoneStateListener.onCellLocationCahnged:", e);
            }
        }

        @Override
        public void onServiceStateChanged(ServiceState serviceState) {
            mServiceState = serviceState;
        }
    };

    private class PowerStateBroadcastReceiver extends BroadcastReceiver {
        @Override public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(ALARM_INTENT)) {
                mLocationHandler.removeMessages(MESSAGE_ACQUIRE_WAKE_LOCK);
                mLocationHandler.removeMessages(MESSAGE_RELEASE_WAKE_LOCK);

                log("PowerStateBroadcastReceiver: Alarm received");
                Message m = mLocationHandler.obtainMessage(MESSAGE_ACQUIRE_WAKE_LOCK);
                mLocationHandler.sendMessageAtFrontOfQueue(m);

            } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                log("PowerStateBroadcastReceiver: Screen off");
                updateWakelockStatus(false);

            } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
                log("PowerStateBroadcastReceiver: Screen on");
                updateWakelockStatus(true);

            } else if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                log("PowerStateBroadcastReceiver: Battery changed");
                int scale = intent.getIntExtra(BATTERY_EXTRA_SCALE, 100);
                int level = intent.getIntExtra(BATTERY_EXTRA_LEVEL, 0);
                boolean plugged = intent.getIntExtra(BATTERY_EXTRA_PLUGGED, 0) != 0;

                // Notify collector battery state
                mCollector.updateBatteryState(scale, level, plugged);
>>>>>>> 54b6cfa... Initial Contribution
            }
        }
    }

<<<<<<< HEAD
    private void decrementPendingBroadcasts() {
        synchronized (mWakeLock) {
            if (--mPendingBroadcasts == 0) {
                try {
                    // Release wake lock
                    if (mWakeLock.isHeld()) {
                        mWakeLock.release();
                        log("Released wakelock");
                    } else {
                        log("Can't release wakelock again!");
                    }
                } catch (Exception e) {
                    // This is to catch a runtime exception thrown when we try to release an
                    // already released lock.
                    Slog.e(TAG, "exception in releaseWakeLock()", e);
                }
            }
        }
    }

    // Geocoder

    public boolean geocoderIsPresent() {
        return mGeocodeProvider != null;
    }

    public String getFromLocation(double latitude, double longitude, int maxResults,
            GeocoderParams params, List<Address> addrs) {
        if (mGeocodeProvider != null) {
            return mGeocodeProvider.getFromLocation(latitude, longitude, maxResults,
                    params, addrs);
        }
        return null;
    }


    public String getFromLocationName(String locationName,
            double lowerLeftLatitude, double lowerLeftLongitude,
            double upperRightLatitude, double upperRightLongitude, int maxResults,
            GeocoderParams params, List<Address> addrs) {

        if (mGeocodeProvider != null) {
            return mGeocodeProvider.getFromLocationName(locationName, lowerLeftLatitude,
                    lowerLeftLongitude, upperRightLatitude, upperRightLongitude,
                    maxResults, params, addrs);
        }
        return null;
    }

    // Mock Providers

    private void checkMockPermissionsSafe() {
        boolean allowMocks = Settings.Secure.getInt(mContext.getContentResolver(),
                Settings.Secure.ALLOW_MOCK_LOCATION, 0) == 1;
        if (!allowMocks) {
            throw new SecurityException("Requires ACCESS_MOCK_LOCATION secure setting");
        }

        if (mContext.checkCallingPermission(ACCESS_MOCK_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("Requires ACCESS_MOCK_LOCATION permission");
        }            
    }

    public void addTestProvider(String name, boolean requiresNetwork, boolean requiresSatellite,
        boolean requiresCell, boolean hasMonetaryCost, boolean supportsAltitude,
        boolean supportsSpeed, boolean supportsBearing, int powerRequirement, int accuracy) {
        checkMockPermissionsSafe();

        if (LocationManager.PASSIVE_PROVIDER.equals(name)) {
            throw new IllegalArgumentException("Cannot mock the passive location provider");
        }

        long identity = Binder.clearCallingIdentity();
        synchronized (mLock) {
            MockProvider provider = new MockProvider(name, this,
                requiresNetwork, requiresSatellite,
                requiresCell, hasMonetaryCost, supportsAltitude,
                supportsSpeed, supportsBearing, powerRequirement, accuracy);
            // remove the real provider if we are replacing GPS or network provider
            if (LocationManager.GPS_PROVIDER.equals(name)
                    || LocationManager.NETWORK_PROVIDER.equals(name)) {
                LocationProviderInterface p = mProvidersByName.get(name);
                if (p != null) {
                    p.enableLocationTracking(false);
                    removeProvider(p);
                }
            }
            if (mProvidersByName.get(name) != null) {
                throw new IllegalArgumentException("Provider \"" + name + "\" already exists");
            }
            addProvider(provider);
            mMockProviders.put(name, provider);
            mLastKnownLocation.put(name, null);
            updateProvidersLocked();
        }
        Binder.restoreCallingIdentity(identity);
    }

    public void removeTestProvider(String provider) {
        checkMockPermissionsSafe();
        synchronized (mLock) {
            MockProvider mockProvider = mMockProviders.get(provider);
            if (mockProvider == null) {
                throw new IllegalArgumentException("Provider \"" + provider + "\" unknown");
            }
            long identity = Binder.clearCallingIdentity();
            removeProvider(mProvidersByName.get(provider));
            mMockProviders.remove(mockProvider);
            // reinstall real provider if we were mocking GPS or network provider
            if (LocationManager.GPS_PROVIDER.equals(provider) &&
                    mGpsLocationProvider != null) {
                addProvider(mGpsLocationProvider);
            } else if (LocationManager.NETWORK_PROVIDER.equals(provider) &&
                    mNetworkLocationProvider != null) {
                addProvider(mNetworkLocationProvider);
            }
            mLastKnownLocation.put(provider, null);
            updateProvidersLocked();
            Binder.restoreCallingIdentity(identity);
        }
    }

    public void setTestProviderLocation(String provider, Location loc) {
        checkMockPermissionsSafe();
        synchronized (mLock) {
            MockProvider mockProvider = mMockProviders.get(provider);
            if (mockProvider == null) {
                throw new IllegalArgumentException("Provider \"" + provider + "\" unknown");
            }
            // clear calling identity so INSTALL_LOCATION_PROVIDER permission is not required
            long identity = Binder.clearCallingIdentity();
            mockProvider.setLocation(loc);
            Binder.restoreCallingIdentity(identity);
        }
    }

    public void clearTestProviderLocation(String provider) {
        checkMockPermissionsSafe();
        synchronized (mLock) {
            MockProvider mockProvider = mMockProviders.get(provider);
            if (mockProvider == null) {
                throw new IllegalArgumentException("Provider \"" + provider + "\" unknown");
            }
            mockProvider.clearLocation();
        }
    }

    public void setTestProviderEnabled(String provider, boolean enabled) {
        checkMockPermissionsSafe();
        synchronized (mLock) {
            MockProvider mockProvider = mMockProviders.get(provider);
            if (mockProvider == null) {
                throw new IllegalArgumentException("Provider \"" + provider + "\" unknown");
            }
            long identity = Binder.clearCallingIdentity();
            if (enabled) {
                mockProvider.enable();
                mEnabledProviders.add(provider);
                mDisabledProviders.remove(provider);
            } else {
                mockProvider.disable();
                mEnabledProviders.remove(provider);
                mDisabledProviders.add(provider);
            }
            updateProvidersLocked();
            Binder.restoreCallingIdentity(identity);
        }
    }

    public void clearTestProviderEnabled(String provider) {
        checkMockPermissionsSafe();
        synchronized (mLock) {
            MockProvider mockProvider = mMockProviders.get(provider);
            if (mockProvider == null) {
                throw new IllegalArgumentException("Provider \"" + provider + "\" unknown");
            }
            long identity = Binder.clearCallingIdentity();
            mEnabledProviders.remove(provider);
            mDisabledProviders.remove(provider);
            updateProvidersLocked();
            Binder.restoreCallingIdentity(identity);
        }
    }

    public void setTestProviderStatus(String provider, int status, Bundle extras, long updateTime) {
        checkMockPermissionsSafe();
        synchronized (mLock) {
            MockProvider mockProvider = mMockProviders.get(provider);
            if (mockProvider == null) {
                throw new IllegalArgumentException("Provider \"" + provider + "\" unknown");
            }
            mockProvider.setStatus(status, extras, updateTime);
        }
    }

    public void clearTestProviderStatus(String provider) {
        checkMockPermissionsSafe();
        synchronized (mLock) {
            MockProvider mockProvider = mMockProviders.get(provider);
            if (mockProvider == null) {
                throw new IllegalArgumentException("Provider \"" + provider + "\" unknown");
            }
            mockProvider.clearStatus();
        }
    }

    public class BlacklistObserver extends ContentObserver {
        public BlacklistObserver(Handler handler) {
            super(handler);
        }
        @Override
        public void onChange(boolean selfChange) {
            reloadBlacklist();
        }
    }

    private void loadBlacklist() {
        // Register for changes
        BlacklistObserver observer = new BlacklistObserver(mLocationHandler);
        mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor(
                BLACKLIST_CONFIG_NAME), false, observer);
        mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor(
                WHITELIST_CONFIG_NAME), false, observer);
        reloadBlacklist();
    }

    private void reloadBlacklist() {
        String blacklist[] = getStringArray(BLACKLIST_CONFIG_NAME);
        String whitelist[] = getStringArray(WHITELIST_CONFIG_NAME);
        synchronized (mLock) {
            mWhitelist = whitelist;
            Slog.i(TAG, "whitelist: " + arrayToString(mWhitelist));
            mBlacklist = blacklist;
            Slog.i(TAG, "blacklist: " + arrayToString(mBlacklist));
        }
    }

    private static String arrayToString(String[] array) {
        StringBuilder s = new StringBuilder();
        s.append('[');
        boolean first = true;
        for (String a : array) {
            if (!first) s.append(',');
            first = false;
            s.append(a);
        }
        s.append(']');
        return s.toString();
    }

    private String[] getStringArray(String key) {
        String flatString = Settings.Secure.getString(mContext.getContentResolver(), key);
        if (flatString == null) {
            return new String[0];
        }
        String[] splitStrings = flatString.split(",");
        ArrayList<String> result = new ArrayList<String>();
        for (String pkg : splitStrings) {
            pkg = pkg.trim();
            if (pkg.isEmpty()) {
                continue;
            }
            result.add(pkg);
        }
        return result.toArray(new String[result.size()]);
    }

    /**
     * Return true if in blacklist, and not in whitelist.
     */
    private boolean inBlacklist(String packageName) {
        synchronized (mLock) {
            for (String black : mBlacklist) {
                if (packageName.startsWith(black)) {
                    if (inWhitelist(packageName)) {
                        continue;
                    } else {
                        if (LOCAL_LOGV) Log.d(TAG, "dropping location (blacklisted): "
                                + packageName + " matches " + black);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Return true if any of packages are in whitelist
     */
    private boolean inWhitelist(String pkg) {
        synchronized (mLock) {
            for (String white : mWhitelist) {
                if (pkg.startsWith(white)) return true;
            }
        }
        return false;
    }

    private void checkPackageName(int uid, String packageName) {
        if (packageName == null) {
            throw new SecurityException("packageName cannot be null");
        }
        String[] packages = mPackageManager.getPackagesForUid(uid);
        if (packages == null) {
            throw new SecurityException("invalid UID " + uid);
        }
        for (String pkg : packages) {
            if (packageName.equals(pkg)) return;
        }
        throw new SecurityException("invalid package name");
=======
    private class NetworkStateBroadcastReceiver extends BroadcastReceiver {
        @Override public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {

                List<ScanResult> wifiScanResults = mWifiManager.getScanResults();

                if (wifiScanResults == null) {
                    return;
                }

                // Notify provider and collector of Wifi scan results
                mCollector.updateWifiScanResults(wifiScanResults);
                if (mNetworkLocationProvider != null) {
                    mNetworkLocationProvider.updateWifiScanResults(wifiScanResults);
                }

            } else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                int networkState = LocationProvider.TEMPORARILY_UNAVAILABLE;

                boolean noConnectivity =
                    intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
                if (!noConnectivity) {
                    networkState = LocationProvider.AVAILABLE;
                }

                // Notify location providers of current network state
                List<LocationProviderImpl> providers = LocationProviderImpl.getProviders();
                for (LocationProviderImpl provider : providers) {
                    if (provider.requiresNetwork()) {
                        provider.updateNetworkState(networkState);
                    }
                }

            } else if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_UNKNOWN);

                boolean enabled;
                if (state == WifiManager.WIFI_STATE_ENABLED) {
                    enabled = true;
                } else if (state == WifiManager.WIFI_STATE_DISABLED) {
                    enabled = false;
                } else {
                    return;
                }

                // Notify network provider of current wifi enabled state
                if (mNetworkLocationProvider != null) {
                    mNetworkLocationProvider.updateWifiEnabledState(enabled);
                }

            } else if (action.equals(GpsLocationProvider.GPS_ENABLED_CHANGE_ACTION)) {

                final boolean enabled = intent.getBooleanExtra(GpsLocationProvider.EXTRA_ENABLED, false);

                if (!enabled) {
                    // When GPS is disabled, we are OK to release wake-lock
                    mWakeLockGpsReceived = true;
                }
            }

        }
    }

    // Wake locks

    private void updateWakelockStatus(boolean screenOn) {
        log("updateWakelockStatus(): " + screenOn);

        boolean needsLock = false;
        long minTime = Integer.MAX_VALUE;

        if (mNetworkLocationProvider != null && mNetworkLocationProvider.isLocationTracking()) {
            needsLock = true;
            minTime = Math.min(mNetworkLocationProvider.getMinTime(), minTime);
        }

        if (mGpsLocationProvider != null && mGpsLocationProvider.isLocationTracking()) {
            needsLock = true;
            minTime = Math.min(mGpsLocationProvider.getMinTime(), minTime);
            if (screenOn) {
                startGps();
            } else if (mScreenOn && !screenOn) {
                
                // We just turned the screen off so stop navigating
                stopGps();
            }
        }

        mScreenOn = screenOn;

        PendingIntent sender =
            PendingIntent.getBroadcast(mContext, 0, new Intent(ALARM_INTENT), 0);

        // Cancel existing alarm
        log("Cancelling existing alarm");
        mAlarmManager.cancel(sender);

        if (needsLock && !mScreenOn) {
            long now = SystemClock.elapsedRealtime();
            mAlarmManager.set(
                AlarmManager.ELAPSED_REALTIME_WAKEUP, now + minTime, sender);
            mAlarmInterval = minTime;
            log("Creating a new wakelock alarm with minTime = " + minTime);
        } else {
            log("No need for alarm");
            mAlarmInterval = -1;

            // Clear out existing wakelocks
            mLocationHandler.removeMessages(MESSAGE_ACQUIRE_WAKE_LOCK);
            mLocationHandler.removeMessages(MESSAGE_RELEASE_WAKE_LOCK);
            releaseWakeLock();
        }
    }

    private void acquireWakeLock() {
        if (mWakeLock.isHeld()) {
            log("Must release wakelock before acquiring");
            mWakeLockAcquireTime = 0;
            mWakeLock.release();
        }

        boolean networkActive = (mNetworkLocationProvider != null) 
                && mNetworkLocationProvider.isLocationTracking();
        boolean gpsActive = (mGpsLocationProvider != null)
                && mGpsLocationProvider.isLocationTracking();

        boolean needsLock = networkActive || gpsActive;
        if (!needsLock) {
            log("No need for Lock!");
            return;
        }

        mWakeLockGpsReceived = !gpsActive;
        mWakeLockNetworkReceived = !networkActive;

        // Acquire wake lock
        mWakeLock.acquire();
        mWakeLockAcquireTime = SystemClock.elapsedRealtime();
        log("Acquired wakelock");

        // Start the gps provider
        startGps();
        
        // Acquire cell lock
        if (mCellWakeLockAcquired) {
            // Lock is already acquired
        } else if (!mWakeLockNetworkReceived) {
            mTelephonyManager.enableLocationUpdates();
            mCellWakeLockAcquired = true;
        } else {
            mCellWakeLockAcquired = false;
        }

        // Notify NetworkLocationProvider
        if (mNetworkLocationProvider != null) {
            mNetworkLocationProvider.updateCellLockStatus(mCellWakeLockAcquired);
        }

        // Acquire wifi lock
        WifiManager.WifiLock wifiLock = getWifiWakelock();
        if (wifiLock != null) {
            if (mWifiWakeLockAcquired) {
                // Lock is already acquired
            } else if (mWifiManager.isWifiEnabled() && !mWakeLockNetworkReceived) {
                wifiLock.acquire();
                mWifiWakeLockAcquired = true;
            } else {
                mWifiWakeLockAcquired = false;
                Log.w(TAG, "acquireWakeLock(): Unable to get WiFi lock");
            }
        }
    }

    private void startGps() {
        boolean gpsActive = (mGpsLocationProvider != null) 
                    && mGpsLocationProvider.isLocationTracking();
        if (gpsActive) {
            mGpsLocationProvider.startNavigating();
        }
    }

    private void stopGps() {
        boolean gpsActive = mGpsLocationProvider != null 
                    && mGpsLocationProvider.isLocationTracking();
        if (gpsActive) {
            mGpsLocationProvider.stopNavigating();
        }
    }

    private void releaseWakeLock() {
        // Release wifi lock
        WifiManager.WifiLock wifiLock = getWifiWakelock();
        if (wifiLock != null) {
            if (mWifiWakeLockAcquired) {
                wifiLock.release();
                mWifiWakeLockAcquired = false;
            }
        }

        if (!mScreenOn) {
            
            // Stop the gps
            stopGps();
        }
        
        // Release cell lock
        if (mCellWakeLockAcquired) {
            mTelephonyManager.disableLocationUpdates();
            mCellWakeLockAcquired = false;
        }
        
        // Notify NetworkLocationProvider
        if (mNetworkLocationProvider != null) {
            mNetworkLocationProvider.updateCellLockStatus(mCellWakeLockAcquired);
        }
        
        // Release wake lock
        mWakeLockAcquireTime = 0;
        if (mWakeLock.isHeld()) {
            log("Released wakelock");
            mWakeLock.release();
        } else {
            log("Can't release wakelock again!");
        }
    }
    
    // Geocoder

    public String getFromLocation(double latitude, double longitude, int maxResults,
        String language, String country, String variant, String appName, List<Address> addrs) {
        try {
            Locale locale = new Locale(language, country, variant);
            mMasfClient.reverseGeocode(locale, appName, latitude, longitude, maxResults, addrs);
            return null;
        } catch(IOException e) {
            return e.getMessage();
        } catch(Exception e) {
            Log.e(TAG, "getFromLocation got exception:", e);
            return null;
        }
    }

    public String getFromLocationName(String locationName,
        double lowerLeftLatitude, double lowerLeftLongitude,
        double upperRightLatitude, double upperRightLongitude, int maxResults,
        String language, String country, String variant, String appName, List<Address> addrs) {

        try {
            Locale locale = new Locale(language, country, variant);
            mMasfClient.forwardGeocode(locale, appName, locationName,
                lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRightLongitude,
                maxResults, addrs);
            return null;
        } catch(IOException e) {
            return e.getMessage();
        } catch(Exception e) {
            Log.e(TAG, "getFromLocationName got exception:", e);
            return null;
        }
    }

    // Mock Providers

    class MockProvider extends LocationProviderImpl {
        boolean mRequiresNetwork;
        boolean mRequiresSatellite;
        boolean mRequiresCell;
        boolean mHasMonetaryCost;
        boolean mSupportsAltitude;
        boolean mSupportsSpeed;
        boolean mSupportsBearing;
        int mPowerRequirement;
        int mAccuracy;

        public MockProvider(String name,  boolean requiresNetwork, boolean requiresSatellite,
            boolean requiresCell, boolean hasMonetaryCost, boolean supportsAltitude,
            boolean supportsSpeed, boolean supportsBearing, int powerRequirement, int accuracy) {
            super(name);

            mRequiresNetwork = requiresNetwork;
            mRequiresSatellite = requiresSatellite;
            mRequiresCell = requiresCell;
            mHasMonetaryCost = hasMonetaryCost;
            mSupportsAltitude = supportsAltitude;
            mSupportsBearing = supportsBearing;
            mSupportsSpeed = supportsSpeed;
            mPowerRequirement = powerRequirement;
            mAccuracy = accuracy;
        }

        @Override
        public void disable() {
            String name = getName();
            mEnabledProviders.remove(name);
            mDisabledProviders.add(name);
        }

        @Override
        public void enable() {
            String name = getName();
            mEnabledProviders.add(name);
            mDisabledProviders.remove(name);
        }

        @Override
        public boolean getLocation(Location l) {
            Location loc = mMockProviderLocation.get(getName());
            if (loc == null) {
                return false;
            }
            l.set(loc);
            return true;
        }

        @Override
        public int getStatus(Bundle extras) {
            String name = getName();
            Integer s = mMockProviderStatus.get(name);
            int status = (s == null) ? AVAILABLE : s.intValue();
            Bundle newExtras = mMockProviderStatusExtras.get(name);
            if (newExtras != null) {
                extras.clear();
                extras.putAll(newExtras);
            }
            return status;
        }

        @Override
        public boolean isEnabled() {
            return mEnabledProviders.contains(getName());
        }

        @Override
        public int getAccuracy() {
            return mAccuracy;
        }

        @Override
        public int getPowerRequirement() {
            return mPowerRequirement;
        }

        @Override
        public boolean hasMonetaryCost() {
            return mHasMonetaryCost;
        }

        @Override
        public boolean requiresCell() {
            return mRequiresCell;
        }

        @Override
        public boolean requiresNetwork() {
            return mRequiresNetwork;
        }

        @Override
        public boolean requiresSatellite() {
            return mRequiresSatellite;
        }

        @Override
        public boolean supportsAltitude() {
            return mSupportsAltitude;
        }

        @Override
        public boolean supportsBearing() {
            return mSupportsBearing;
        }

        @Override
        public boolean supportsSpeed() {
            return mSupportsSpeed;
        }
    }

    public void addTestProvider(String name, boolean requiresNetwork, boolean requiresSatellite,
        boolean requiresCell, boolean hasMonetaryCost, boolean supportsAltitude,
        boolean supportsSpeed, boolean supportsBearing, int powerRequirement, int accuracy) {
        if (mContext.checkCallingPermission(ACCESS_MOCK_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("Requires ACCESS_MOCK_LOCATION permission");
        }

        MockProvider provider = new MockProvider(name, requiresNetwork, requiresSatellite,
            requiresCell, hasMonetaryCost, supportsAltitude,
            supportsSpeed, supportsBearing, powerRequirement, accuracy);
        if (LocationProviderImpl.getProvider(name) != null) {
            throw new IllegalArgumentException("Provider \"" + name + "\" already exists");
        }
        LocationProviderImpl.addProvider(provider);
        updateProviders();
    }

    public void removeTestProvider(String provider) {
        if (mContext.checkCallingPermission(ACCESS_MOCK_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("Requires ACCESS_MOCK_LOCATION permission");
        }
        LocationProviderImpl p = LocationProviderImpl.getProvider(provider);
        if (p == null) {
            throw new IllegalArgumentException("Provider \"" + provider + "\" unknown");
        }
        LocationProviderImpl.removeProvider(p);
        updateProviders();
    }

    public void setTestProviderLocation(String provider, Location loc) {
        if (mContext.checkCallingPermission(ACCESS_MOCK_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("Requires ACCESS_MOCK_LOCATION permission");
        }
        if (LocationProviderImpl.getProvider(provider) == null) {
            throw new IllegalArgumentException("Provider \"" + provider + "\" unknown");
        }
        mMockProviderLocation.put(provider, loc);
    }

    public void clearTestProviderLocation(String provider) {
        if (mContext.checkCallingPermission(ACCESS_MOCK_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("Requires ACCESS_MOCK_LOCATION permission");
        }
        if (LocationProviderImpl.getProvider(provider) == null) {
            throw new IllegalArgumentException("Provider \"" + provider + "\" unknown");
        }
        mMockProviderLocation.remove(provider);
    }

    public void setTestProviderEnabled(String provider, boolean enabled) {
        if (mContext.checkCallingPermission(ACCESS_MOCK_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("Requires ACCESS_MOCK_LOCATION permission");
        }
        if (LocationProviderImpl.getProvider(provider) == null) {
            throw new IllegalArgumentException("Provider \"" + provider + "\" unknown");
        }
        if (enabled) {
            mEnabledProviders.add(provider);
            mDisabledProviders.remove(provider);
        } else {
            mEnabledProviders.remove(provider);
            mDisabledProviders.add(provider);
        }
        updateProviders();
    }

    public void clearTestProviderEnabled(String provider) {
        if (mContext.checkCallingPermission(ACCESS_MOCK_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("Requires ACCESS_MOCK_LOCATION permission");
        }
        if (LocationProviderImpl.getProvider(provider) == null) {
            throw new IllegalArgumentException("Provider \"" + provider + "\" unknown");
        }
        mEnabledProviders.remove(provider);
        mDisabledProviders.remove(provider);
        updateProviders();
    }

    public void setTestProviderStatus(String provider, int status, Bundle extras, long updateTime) {
        if (mContext.checkCallingPermission(ACCESS_MOCK_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("Requires ACCESS_MOCK_LOCATION permission");
        }
        if (LocationProviderImpl.getProvider(provider) == null) {
            throw new IllegalArgumentException("Provider \"" + provider + "\" unknown");
        }
        mMockProviderStatus.put(provider, new Integer(status));
        mMockProviderStatusExtras.put(provider, extras);
        mMockProviderStatusUpdateTime.put(provider, new Long(updateTime));
    }

    public void clearTestProviderStatus(String provider) {
        if (mContext.checkCallingPermission(ACCESS_MOCK_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("Requires ACCESS_MOCK_LOCATION permission");
        }
        if (LocationProviderImpl.getProvider(provider) == null) {
            throw new IllegalArgumentException("Provider \"" + provider + "\" unknown");
        }
        mMockProviderStatus.remove(provider);
        mMockProviderStatusExtras.remove(provider);
        mMockProviderStatusUpdateTime.remove(provider);
>>>>>>> 54b6cfa... Initial Contribution
    }

    private void log(String log) {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
<<<<<<< HEAD
            Slog.d(TAG, log);
        }
    }
    
    protected void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        if (mContext.checkCallingOrSelfPermission(android.Manifest.permission.DUMP)
                != PackageManager.PERMISSION_GRANTED) {
            pw.println("Permission Denial: can't dump LocationManagerService from from pid="
                    + Binder.getCallingPid()
                    + ", uid=" + Binder.getCallingUid());
            return;
        }
        
        synchronized (mLock) {
            pw.println("Current Location Manager state:");
            pw.println("  sProvidersLoaded=" + sProvidersLoaded);
            pw.println("  Listeners:");
            int N = mReceivers.size();
            for (int i=0; i<N; i++) {
                pw.println("    " + mReceivers.get(i));
            }
            pw.println("  Location Listeners:");
            for (Receiver i : mReceivers.values()) {
                pw.println("    " + i + ":");
                for (Map.Entry<String,UpdateRecord> j : i.mUpdateRecords.entrySet()) {
                    pw.println("      " + j.getKey() + ":");
                    j.getValue().dump(pw, "        ");
                }
            }
            pw.println("  Package blacklist:" + arrayToString(mBlacklist));
            pw.println("  Package whitelist:" + arrayToString(mWhitelist));
            pw.println("  Records by Provider:");
            for (Map.Entry<String, ArrayList<UpdateRecord>> i
                    : mRecordsByProvider.entrySet()) {
                pw.println("    " + i.getKey() + ":");
                for (UpdateRecord j : i.getValue()) {
                    pw.println("      " + j + ":");
                    j.dump(pw, "        ");
                }
            }
            pw.println("  Last Known Locations:");
            for (Map.Entry<String, Location> i
                    : mLastKnownLocation.entrySet()) {
                pw.println("    " + i.getKey() + ":");
                i.getValue().dump(new PrintWriterPrinter(pw), "      ");
            }
            if (mProximityAlerts.size() > 0) {
                pw.println("  Proximity Alerts:");
                for (Map.Entry<PendingIntent, ProximityAlert> i
                        : mProximityAlerts.entrySet()) {
                    pw.println("    " + i.getKey() + ":");
                    i.getValue().dump(pw, "      ");
                }
            }
            if (mProximitiesEntered.size() > 0) {
                pw.println("  Proximities Entered:");
                for (ProximityAlert i : mProximitiesEntered) {
                    pw.println("    " + i + ":");
                    i.dump(pw, "      ");
                }
            }
            pw.println("  mProximityReceiver=" + mProximityReceiver);
            pw.println("  mProximityListener=" + mProximityListener);
            if (mEnabledProviders.size() > 0) {
                pw.println("  Enabled Providers:");
                for (String i : mEnabledProviders) {
                    pw.println("    " + i);
                }
                
            }
            if (mDisabledProviders.size() > 0) {
                pw.println("  Disabled Providers:");
                for (String i : mDisabledProviders) {
                    pw.println("    " + i);
                }
                
            }
            if (mMockProviders.size() > 0) {
                pw.println("  Mock Providers:");
                for (Map.Entry<String, MockProvider> i : mMockProviders.entrySet()) {
                    i.getValue().dump(pw, "      ");
                }
            }
            for (LocationProviderInterface provider: mProviders) {
                String state = provider.getInternalState();
                if (state != null) {
                    pw.println(provider.getName() + " Internal State:");
                    pw.write(state);
                }
            }
        }
    }
}
=======
            Log.d(TAG, log);
        }
    }
}

>>>>>>> 54b6cfa... Initial Contribution
