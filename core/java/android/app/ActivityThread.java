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

package android.app;

<<<<<<< HEAD
import android.app.backup.BackupAgent;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks2;
=======
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks;
>>>>>>> 54b6cfa... Initial Contribution
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.Context;
import android.content.IContentProvider;
import android.content.Intent;
<<<<<<< HEAD
import android.content.IIntentReceiver;
=======
import android.content.ServiceConnection;
>>>>>>> 54b6cfa... Initial Contribution
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageManager;
<<<<<<< HEAD
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.content.res.AssetManager;
import android.content.res.CompatibilityInfo;
=======
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.content.res.AssetManager;
>>>>>>> 54b6cfa... Initial Contribution
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDebug;
<<<<<<< HEAD
import android.database.sqlite.SQLiteDebug.DbStats;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.IConnectivityManager;
import android.net.Proxy;
import android.net.ProxyProperties;
import android.opengl.GLUtils;
import android.os.AsyncTask;
import android.os.Binder;
=======
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.http.AndroidHttpClient;
>>>>>>> 54b6cfa... Initial Contribution
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
<<<<<<< HEAD
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.StrictMode;
import android.os.SystemClock;
import android.os.Trace;
import android.os.UserId;
import android.util.AndroidRuntimeException;
import android.util.DisplayMetrics;
import android.util.EventLog;
import android.util.Log;
import android.util.LogPrinter;
import android.util.PrintWriterPrinter;
import android.util.Slog;
import android.view.Display;
import android.view.HardwareRenderer;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewManager;
import android.view.ViewRootImpl;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManagerImpl;
import android.renderscript.RenderScript;

import com.android.internal.os.BinderInternal;
import com.android.internal.os.RuntimeInit;
import com.android.internal.os.SamplingProfilerIntegration;
=======
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.util.AndroidRuntimeException;
import android.util.Config;
import android.util.DisplayMetrics;
import android.util.EventLog;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewManager;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManagerImpl;

import com.android.internal.os.BinderInternal;
import com.android.internal.os.RuntimeInit;
import com.android.internal.util.ArrayUtils;
>>>>>>> 54b6cfa... Initial Contribution

import org.apache.harmony.xnet.provider.jsse.OpenSSLSocketImpl;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
<<<<<<< HEAD
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
=======
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
>>>>>>> 54b6cfa... Initial Contribution
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;

<<<<<<< HEAD
import libcore.io.IoUtils;

import dalvik.system.CloseGuard;

final class SuperNotCalledException extends AndroidRuntimeException {
    public SuperNotCalledException(String msg) {
=======
final class IntentReceiverLeaked extends AndroidRuntimeException {
    public IntentReceiverLeaked(String msg) {
        super(msg);
    }
}

final class ServiceConnectionLeaked extends AndroidRuntimeException {
    public ServiceConnectionLeaked(String msg) {
>>>>>>> 54b6cfa... Initial Contribution
        super(msg);
    }
}

<<<<<<< HEAD
final class RemoteServiceException extends AndroidRuntimeException {
    public RemoteServiceException(String msg) {
=======
final class SuperNotCalledException extends AndroidRuntimeException {
    public SuperNotCalledException(String msg) {
>>>>>>> 54b6cfa... Initial Contribution
        super(msg);
    }
}

/**
 * This manages the execution of the main thread in an
 * application process, scheduling and executing activities,
 * broadcasts, and other operations on it as the activity
 * manager requests.
 *
 * {@hide}
 */
public final class ActivityThread {
<<<<<<< HEAD
    /** @hide */
    public static final String TAG = "ActivityThread";
    private static final android.graphics.Bitmap.Config THUMBNAIL_FORMAT = Bitmap.Config.RGB_565;
    static final boolean localLOGV = false;
    static final boolean DEBUG_MESSAGES = false;
    /** @hide */
    public static final boolean DEBUG_BROADCAST = false;
    private static final boolean DEBUG_RESULTS = false;
    private static final boolean DEBUG_BACKUP = false;
    private static final boolean DEBUG_CONFIGURATION = false;
    private static final boolean DEBUG_SERVICE = false;
    private static final boolean DEBUG_MEMORY_TRIM = false;
    private static final boolean DEBUG_PROVIDER = false;
=======
    private static final String TAG = "ActivityThread";
    private static final boolean DEBUG = false;
    private static final boolean localLOGV = DEBUG ? Config.LOGD : Config.LOGV;
    private static final boolean DEBUG_BROADCAST = false;
>>>>>>> 54b6cfa... Initial Contribution
    private static final long MIN_TIME_BETWEEN_GCS = 5*1000;
    private static final Pattern PATTERN_SEMICOLON = Pattern.compile(";");
    private static final int SQLITE_MEM_RELEASED_EVENT_LOG_TAG = 75003;
    private static final int LOG_ON_PAUSE_CALLED = 30021;
    private static final int LOG_ON_RESUME_CALLED = 30022;

<<<<<<< HEAD
    static ContextImpl mSystemContext = null;

    static IPackageManager sPackageManager;

    final ApplicationThread mAppThread = new ApplicationThread();
    final Looper mLooper = Looper.myLooper();
    final H mH = new H();
    final HashMap<IBinder, ActivityClientRecord> mActivities
            = new HashMap<IBinder, ActivityClientRecord>();
    // List of new activities (via ActivityRecord.nextIdle) that should
    // be reported when next we idle.
    ActivityClientRecord mNewActivities = null;
    // Number of activities that are currently visible on-screen.
    int mNumVisibleActivities = 0;
    final HashMap<IBinder, Service> mServices
            = new HashMap<IBinder, Service>();
    AppBindData mBoundApplication;
    Profiler mProfiler;
    Configuration mConfiguration;
    Configuration mCompatConfiguration;
    Configuration mResConfiguration;
    CompatibilityInfo mResCompatibilityInfo;
    Application mInitialApplication;
    final ArrayList<Application> mAllApplications
            = new ArrayList<Application>();
    // set of instantiated backup agents, keyed by package name
    final HashMap<String, BackupAgent> mBackupAgents = new HashMap<String, BackupAgent>();
    static final ThreadLocal<ActivityThread> sThreadLocal = new ThreadLocal<ActivityThread>();
    Instrumentation mInstrumentation;
    String mInstrumentationAppDir = null;
    String mInstrumentationAppLibraryDir = null;
    String mInstrumentationAppPackage = null;
    String mInstrumentedAppDir = null;
    String mInstrumentedAppLibraryDir = null;
    boolean mSystemThread = false;
    boolean mJitEnabled = false;

    // These can be accessed by multiple threads; mPackages is the lock.
    // XXX For now we keep around information about all packages we have
    // seen, not removing entries from this map.
    // NOTE: The activity and window managers need to call in to
    // ActivityThread to do things like update resource configurations,
    // which means this lock gets held while the activity and window managers
    // holds their own lock.  Thus you MUST NEVER call back into the activity manager
    // or window manager or anything that depends on them while holding this lock.
    final HashMap<String, WeakReference<LoadedApk>> mPackages
            = new HashMap<String, WeakReference<LoadedApk>>();
    final HashMap<String, WeakReference<LoadedApk>> mResourcePackages
            = new HashMap<String, WeakReference<LoadedApk>>();
    final HashMap<CompatibilityInfo, DisplayMetrics> mDisplayMetrics
            = new HashMap<CompatibilityInfo, DisplayMetrics>();
    final HashMap<ResourcesKey, WeakReference<Resources> > mActiveResources
            = new HashMap<ResourcesKey, WeakReference<Resources> >();
    final ArrayList<ActivityClientRecord> mRelaunchingActivities
            = new ArrayList<ActivityClientRecord>();
    Configuration mPendingConfiguration = null;

    // The lock of mProviderMap protects the following variables.
    final HashMap<String, ProviderClientRecord> mProviderMap
        = new HashMap<String, ProviderClientRecord>();
    final HashMap<IBinder, ProviderRefCount> mProviderRefCountMap
        = new HashMap<IBinder, ProviderRefCount>();
    final HashMap<IBinder, ProviderClientRecord> mLocalProviders
        = new HashMap<IBinder, ProviderClientRecord>();
    final HashMap<ComponentName, ProviderClientRecord> mLocalProvidersByName
            = new HashMap<ComponentName, ProviderClientRecord>();

    final HashMap<Activity, ArrayList<OnActivityPausedListener>> mOnPauseListeners
        = new HashMap<Activity, ArrayList<OnActivityPausedListener>>();

    final GcIdler mGcIdler = new GcIdler();
    boolean mGcIdlerScheduled = false;

    static Handler sMainThreadHandler;  // set once in main()

    Bundle mCoreSettings = null;

    static final class ActivityClientRecord {
        IBinder token;
        int ident;
=======
    
    public static final ActivityThread currentActivityThread() {
        return (ActivityThread)sThreadLocal.get();
    }

    public static final String currentPackageName()
    {
        ActivityThread am = currentActivityThread();
        return (am != null && am.mBoundApplication != null)
            ? am.mBoundApplication.processName : null;
    }

    public static IPackageManager getPackageManager() {
        if (sPackageManager != null) {
            //Log.v("PackageManager", "returning cur default = " + sPackageManager);
            return sPackageManager;
        }
        IBinder b = ServiceManager.getService("package");
        //Log.v("PackageManager", "default service binder = " + b);
        sPackageManager = IPackageManager.Stub.asInterface(b);
        //Log.v("PackageManager", "default service = " + sPackageManager);
        return sPackageManager;
    }

    DisplayMetrics getDisplayMetricsLocked() {
        if (mDisplay == null) {
            WindowManager wm = WindowManagerImpl.getDefault();
            mDisplay = wm.getDefaultDisplay();
        }
        DisplayMetrics metrics = new DisplayMetrics();
        mDisplay.getMetrics(metrics);
        return metrics;
    }

    Resources getTopLevelResources(String appDir) {
        synchronized (mPackages) {
            //Log.w(TAG, "getTopLevelResources: " + appDir);
            WeakReference<Resources> wr = mActiveResources.get(appDir);
            Resources r = wr != null ? wr.get() : null;
            if (r != null && r.getAssets().isUpToDate()) {
                //Log.w(TAG, "Returning cached resources " + r + " " + appDir);
                return r;
            }

            //if (r != null) {
            //    Log.w(TAG, "Throwing away out-of-date resources!!!! "
            //            + r + " " + appDir);
            //}

            AssetManager assets = new AssetManager();
            if (assets.addAssetPath(appDir) == 0) {
                return null;
            }
            DisplayMetrics metrics = getDisplayMetricsLocked();
            r = new Resources(assets, metrics, getConfiguration());
            //Log.i(TAG, "Created app resources " + r + ": " + r.getConfiguration());
            // XXX need to remove entries when weak references go away
            mActiveResources.put(appDir, new WeakReference<Resources>(r));
            return r;
        }
    }

    final Handler getHandler() {
        return mH;
    }

    public final static class PackageInfo {

        private final ActivityThread mActivityThread;
        private final ApplicationInfo mApplicationInfo;
        private final String mPackageName;
        private final String mAppDir;
        private final String mResDir;
        private final String[] mSharedLibraries;
        private final String mDataDir;
        private final File mDataDirFile;
        private final ClassLoader mBaseClassLoader;
        private final boolean mSecurityViolation;
        private final boolean mIncludeCode;
        private Resources mResources;
        private ClassLoader mClassLoader;
        private Application mApplication;
        private final HashMap<Context, HashMap<BroadcastReceiver, ReceiverDispatcher>> mReceivers
            = new HashMap<Context, HashMap<BroadcastReceiver, ReceiverDispatcher>>();
        private final HashMap<Context, HashMap<BroadcastReceiver, ReceiverDispatcher>> mUnregisteredReceivers
        = new HashMap<Context, HashMap<BroadcastReceiver, ReceiverDispatcher>>();
        private final HashMap<Context, HashMap<ServiceConnection, ServiceDispatcher>> mServices
            = new HashMap<Context, HashMap<ServiceConnection, ServiceDispatcher>>();
        private final HashMap<Context, HashMap<ServiceConnection, ServiceDispatcher>> mUnboundServices
            = new HashMap<Context, HashMap<ServiceConnection, ServiceDispatcher>>();

        int mClientCount = 0;

        public PackageInfo(ActivityThread activityThread, ApplicationInfo aInfo,
                ActivityThread mainThread, ClassLoader baseLoader,
                boolean securityViolation, boolean includeCode) {
            mActivityThread = activityThread;
            mApplicationInfo = aInfo;
            mPackageName = aInfo.packageName;
            mAppDir = aInfo.sourceDir;
            mResDir = aInfo.publicSourceDir;
            mSharedLibraries = aInfo.sharedLibraryFiles;
            mDataDir = aInfo.dataDir;
            mDataDirFile = mDataDir != null ? new File(mDataDir) : null;
            mBaseClassLoader = baseLoader;
            mSecurityViolation = securityViolation;
            mIncludeCode = includeCode;

            if (mAppDir == null) {
                if (mSystemContext == null) {
                    mSystemContext =
                        ApplicationContext.createSystemContext(mainThread);
                    mSystemContext.getResources().updateConfiguration(
                            mainThread.getConfiguration(),
                            mainThread.getDisplayMetricsLocked());
                    //Log.i(TAG, "Created system resources "
                    //        + mSystemContext.getResources() + ": "
                    //        + mSystemContext.getResources().getConfiguration());
                }
                mClassLoader = mSystemContext.getClassLoader();
                mResources = mSystemContext.getResources();
            }
        }

        public PackageInfo(ActivityThread activityThread, String name,
                Context systemContext) {
            mActivityThread = activityThread;
            mApplicationInfo = new ApplicationInfo();
            mApplicationInfo.packageName = name;
            mPackageName = name;
            mAppDir = null;
            mResDir = null;
            mSharedLibraries = null;
            mDataDir = null;
            mDataDirFile = null;
            mBaseClassLoader = null;
            mSecurityViolation = false;
            mIncludeCode = true;
            mClassLoader = systemContext.getClassLoader();
            mResources = systemContext.getResources();
        }

        public String getPackageName() {
            return mPackageName;
        }

        public boolean isSecurityViolation() {
            return mSecurityViolation;
        }

        /**
         * Gets the array of shared libraries that are listed as
         * used by the given package.
         * 
         * @param packageName the name of the package (note: not its
         * file name)
         * @return null-ok; the array of shared libraries, each one
         * a fully-qualified path
         */
        private static String[] getLibrariesFor(String packageName) {
            ApplicationInfo ai = null;
            try {
                ai = getPackageManager().getApplicationInfo(packageName,
                        PackageManager.GET_SHARED_LIBRARY_FILES);
            } catch (RemoteException e) {
                throw new AssertionError(e);
            }

            if (ai == null) {
                return null;
            }

            return ai.sharedLibraryFiles;
        }

        /**
         * Combines two arrays (of library names) such that they are
         * concatenated in order but are devoid of duplicates. The
         * result is a single string with the names of the libraries
         * separated by colons, or <code>null</code> if both lists
         * were <code>null</code> or empty.
         * 
         * @param list1 null-ok; the first list
         * @param list2 null-ok; the second list
         * @return null-ok; the combination
         */
        private static String combineLibs(String[] list1, String[] list2) {
            StringBuilder result = new StringBuilder(300);
            boolean first = true;

            if (list1 != null) {
                for (String s : list1) {
                    if (first) {
                        first = false;
                    } else {
                        result.append(':');
                    }
                    result.append(s);
                }
            }

            // Only need to check for duplicates if list1 was non-empty.
            boolean dupCheck = !first;

            if (list2 != null) {
                for (String s : list2) {
                    if (dupCheck && ArrayUtils.contains(list1, s)) {
                        continue;
                    }
                    
                    if (first) {
                        first = false;
                    } else {
                        result.append(':');
                    }
                    result.append(s);
                }
            }

            return result.toString();
        }
                
        public ClassLoader getClassLoader() {
            synchronized (this) {
                if (mClassLoader != null) {
                    return mClassLoader;
                }

                if (mIncludeCode && !mPackageName.equals("android")) {
                    String zip = mAppDir;

                    /*
                     * The following is a bit of a hack to inject
                     * instrumentation into the system: If the app
                     * being started matches one of the instrumentation names,
                     * then we combine both the "instrumentation" and
                     * "instrumented" app into the path, along with the
                     * concatenation of both apps' shared library lists.
                     */

                    String instrumentationAppDir =
                            mActivityThread.mInstrumentationAppDir;
                    String instrumentationAppPackage =
                            mActivityThread.mInstrumentationAppPackage;
                    String instrumentedAppDir =
                            mActivityThread.mInstrumentedAppDir;
                    String[] instrumentationLibs = null;

                    if (mAppDir.equals(instrumentationAppDir)
                            || mAppDir.equals(instrumentedAppDir)) {
                        zip = instrumentationAppDir + ":" + instrumentedAppDir;
                        if (! instrumentedAppDir.equals(instrumentationAppDir)) {
                            instrumentationLibs =
                                getLibrariesFor(instrumentationAppPackage);
                        }
                    }

                    if ((mSharedLibraries != null) ||
                            (instrumentationLibs != null)) {
                        zip = 
                            combineLibs(mSharedLibraries, instrumentationLibs)
                            + ':' + zip;
                    }

                    /*
                     * With all the combination done (if necessary, actually
                     * create the class loader.
                     */

                    if (localLOGV) Log.v(TAG, "Class path: " + zip);

                    mClassLoader =
                        ApplicationLoaders.getDefault().getClassLoader(
                            zip, mDataDir, mBaseClassLoader);
                } else {
                    if (mBaseClassLoader == null) {
                        mClassLoader = ClassLoader.getSystemClassLoader();
                    } else {
                        mClassLoader = mBaseClassLoader;
                    }
                }
                return mClassLoader;
            }
        }

        public String getAppDir() {
            return mAppDir;
        }

        public String getResDir() {
            return mResDir;
        }

        public String getDataDir() {
            return mDataDir;
        }

        public File getDataDirFile() {
            return mDataDirFile;
        }

        public AssetManager getAssets(ActivityThread mainThread) {
            return getResources(mainThread).getAssets();
        }

        public Resources getResources(ActivityThread mainThread) {
            if (mResources == null) {
                mResources = mainThread.getTopLevelResources(mResDir);
            }
            return mResources;
        }

        public Application makeApplication() {
            if (mApplication != null) {
                return mApplication;
            }
            
            Application app = null;
            
            String appClass = mApplicationInfo.className;
            if (appClass == null) {
                appClass = "android.app.Application";
            }

            try {
                java.lang.ClassLoader cl = getClassLoader();
                ApplicationContext appContext = new ApplicationContext();
                appContext.init(this, null, mActivityThread);
                app = mActivityThread.mInstrumentation.newApplication(
                        cl, appClass, appContext);
                appContext.setOuterContext(app);
            } catch (Exception e) {
                if (!mActivityThread.mInstrumentation.onException(app, e)) {
                    throw new RuntimeException(
                        "Unable to instantiate application " + appClass
                        + ": " + e.toString(), e);
                }
            }
            mActivityThread.mAllApplications.add(app);
            return mApplication = app;
        }
        
        public void removeContextRegistrations(Context context,
                String who, String what) {
            HashMap<BroadcastReceiver, ReceiverDispatcher> rmap =
                mReceivers.remove(context);
            if (rmap != null) {
                Iterator<ReceiverDispatcher> it = rmap.values().iterator();
                while (it.hasNext()) {
                    ReceiverDispatcher rd = it.next();
                    IntentReceiverLeaked leak = new IntentReceiverLeaked(
                            what + " " + who + " has leaked IntentReceiver "
                            + rd.getIntentReceiver() + " that was " +
                            "originally registered here. Are you missing a " +
                            "call to unregisterReceiver()?");
                    leak.setStackTrace(rd.getLocation().getStackTrace());
                    Log.e(TAG, leak.getMessage(), leak);
                    try {
                        ActivityManagerNative.getDefault().unregisterReceiver(
                                rd.getIIntentReceiver());
                    } catch (RemoteException e) {
                        // system crashed, nothing we can do
                    }
                }
            }
            mUnregisteredReceivers.remove(context);
            //Log.i(TAG, "Receiver registrations: " + mReceivers);
            HashMap<ServiceConnection, ServiceDispatcher> smap =
                mServices.remove(context);
            if (smap != null) {
                Iterator<ServiceDispatcher> it = smap.values().iterator();
                while (it.hasNext()) {
                    ServiceDispatcher sd = it.next();
                    ServiceConnectionLeaked leak = new ServiceConnectionLeaked(
                            what + " " + who + " has leaked ServiceConnection "
                            + sd.getServiceConnection() + " that was originally bound here");
                    leak.setStackTrace(sd.getLocation().getStackTrace());
                    Log.e(TAG, leak.getMessage(), leak);
                    try {
                        ActivityManagerNative.getDefault().unbindService(
                                sd.getIServiceConnection());
                    } catch (RemoteException e) {
                        // system crashed, nothing we can do
                    }
                    sd.doForget();
                }
            }
            mUnboundServices.remove(context);
            //Log.i(TAG, "Service registrations: " + mServices);
        }

        public IIntentReceiver getReceiverDispatcher(BroadcastReceiver r,
                Context context, Handler handler,
                Instrumentation instrumentation, boolean registered) {
            synchronized (mReceivers) {
                ReceiverDispatcher rd = null;
                HashMap<BroadcastReceiver, ReceiverDispatcher> map = null;
                if (registered) {
                    map = mReceivers.get(context);
                    if (map != null) {
                        rd = map.get(r);
                    }
                }
                if (rd == null) {
                    rd = new ReceiverDispatcher(r, context, handler,
                            instrumentation, registered);
                    if (registered) {
                        if (map == null) {
                            map = new HashMap<BroadcastReceiver, ReceiverDispatcher>();
                            mReceivers.put(context, map);
                        }
                        map.put(r, rd);
                    }
                } else {
                    rd.validate(context, handler);
                }
                return rd.getIIntentReceiver();
            }
        }

        public IIntentReceiver forgetReceiverDispatcher(Context context,
                BroadcastReceiver r) {
            synchronized (mReceivers) {
                HashMap<BroadcastReceiver, ReceiverDispatcher> map = mReceivers.get(context);
                ReceiverDispatcher rd = null;
                if (map != null) {
                    rd = map.get(r);
                    if (rd != null) {
                        map.remove(r);
                        if (map.size() == 0) {
                            mReceivers.remove(context);
                        }
                        if (r.getDebugUnregister()) {
                            HashMap<BroadcastReceiver, ReceiverDispatcher> holder
                                    = mUnregisteredReceivers.get(context);
                            if (holder == null) {
                                holder = new HashMap<BroadcastReceiver, ReceiverDispatcher>();
                                mUnregisteredReceivers.put(context, holder);
                            }
                            RuntimeException ex = new IllegalArgumentException(
                                    "Originally unregistered here:");
                            ex.fillInStackTrace();
                            rd.setUnregisterLocation(ex);
                            holder.put(r, rd);
                        }
                        return rd.getIIntentReceiver();
                    }
                }
                HashMap<BroadcastReceiver, ReceiverDispatcher> holder
                        = mUnregisteredReceivers.get(context);
                if (holder != null) {
                    rd = holder.get(r);
                    if (rd != null) {
                        RuntimeException ex = rd.getUnregisterLocation();
                        throw new IllegalArgumentException(
                                "Unregistering Receiver " + r
                                + " that was already unregistered", ex);
                    }
                }
                if (context == null) {
                    throw new IllegalStateException("Unbinding Receiver " + r
                            + " from Context that is no longer in use: " + context);
                } else {
                    throw new IllegalArgumentException("Receiver not registered: " + r);
                }

            }
        }

        static final class ReceiverDispatcher {

            final static class InnerReceiver extends IIntentReceiver.Stub {
                final WeakReference<ReceiverDispatcher> mDispatcher;
                final ReceiverDispatcher mStrongRef;
                
                InnerReceiver(ReceiverDispatcher rd, boolean strong) {
                    mDispatcher = new WeakReference<ReceiverDispatcher>(rd);
                    mStrongRef = strong ? rd : null;
                }
                public void performReceive(Intent intent, int resultCode,
                        String data, Bundle extras, boolean ordered) {
                    ReceiverDispatcher rd = mDispatcher.get();
                    if (DEBUG_BROADCAST) {
                        int seq = intent.getIntExtra("seq", -1);
                        Log.i(TAG, "Receiving broadcast " + intent.getAction() + " seq=" + seq
                                + " to " + rd);
                    }
                    if (rd != null) {
                        rd.performReceive(intent, resultCode, data, extras, ordered);
                    }
                }
            }
            
            final IIntentReceiver.Stub mIIntentReceiver;
            final BroadcastReceiver mReceiver;
            final Context mContext;
            final Handler mActivityThread;
            final Instrumentation mInstrumentation;
            final boolean mRegistered;
            final IntentReceiverLeaked mLocation;
            RuntimeException mUnregisterLocation;

            final class Args implements Runnable {
                private Intent mCurIntent;
                private int mCurCode;
                private String mCurData;
                private Bundle mCurMap;
                private boolean mCurOrdered;

                public void run() {
                    BroadcastReceiver receiver = mReceiver;
                    if (DEBUG_BROADCAST) {
                        int seq = mCurIntent.getIntExtra("seq", -1);
                        Log.i(TAG, "Dispathing broadcast " + mCurIntent.getAction() + " seq=" + seq
                                + " to " + mReceiver);
                    }
                    if (receiver == null) {
                        return;
                    }

                    IActivityManager mgr = ActivityManagerNative.getDefault();
                    Intent intent = mCurIntent;
                    mCurIntent = null;
                    try {
                        ClassLoader cl =  mReceiver.getClass().getClassLoader();
                        intent.setExtrasClassLoader(cl);
                        if (mCurMap != null) {
                            mCurMap.setClassLoader(cl);
                        }
                        receiver.setOrderedHint(true);
                        receiver.setResult(mCurCode, mCurData, mCurMap);
                        receiver.clearAbortBroadcast();
                        receiver.setOrderedHint(mCurOrdered);
                        receiver.onReceive(mContext, intent);
                    } catch (Exception e) {
                        if (mRegistered && mCurOrdered) {
                            try {
                                mgr.finishReceiver(mIIntentReceiver,
                                        mCurCode, mCurData, mCurMap, false);
                            } catch (RemoteException ex) {
                            }
                        }
                        if (mInstrumentation == null ||
                                !mInstrumentation.onException(mReceiver, e)) {
                            throw new RuntimeException(
                                "Error receiving broadcast " + intent
                                + " in " + mReceiver, e);
                        }
                    }
                    if (mRegistered && mCurOrdered) {
                        try {
                            mgr.finishReceiver(mIIntentReceiver,
                                    receiver.getResultCode(),
                                    receiver.getResultData(),
                                    receiver.getResultExtras(false),
                                    receiver.getAbortBroadcast());
                        } catch (RemoteException ex) {
                        }
                    }
                }
            }

            ReceiverDispatcher(BroadcastReceiver receiver, Context context,
                    Handler activityThread, Instrumentation instrumentation,
                    boolean registered) {
                if (activityThread == null) {
                    throw new NullPointerException("Handler must not be null");
                }

                mIIntentReceiver = new InnerReceiver(this, !registered);
                mReceiver = receiver;
                mContext = context;
                mActivityThread = activityThread;
                mInstrumentation = instrumentation;
                mRegistered = registered;
                mLocation = new IntentReceiverLeaked(null);
                mLocation.fillInStackTrace();
            }

            void validate(Context context, Handler activityThread) {
                if (mContext != context) {
                    throw new IllegalStateException(
                        "Receiver " + mReceiver +
                        " registered with differing Context (was " +
                        mContext + " now " + context + ")");
                }
                if (mActivityThread != activityThread) {
                    throw new IllegalStateException(
                        "Receiver " + mReceiver +
                        " registered with differing handler (was " +
                        mActivityThread + " now " + activityThread + ")");
                }
            }

            IntentReceiverLeaked getLocation() {
                return mLocation;
            }

            BroadcastReceiver getIntentReceiver() {
                return mReceiver;
            }
            
            IIntentReceiver getIIntentReceiver() {
                return mIIntentReceiver;
            }

            void setUnregisterLocation(RuntimeException ex) {
                mUnregisterLocation = ex;
            }

            RuntimeException getUnregisterLocation() {
                return mUnregisterLocation;
            }

            public void performReceive(Intent intent, int resultCode,
                    String data, Bundle extras, boolean ordered) {
                if (DEBUG_BROADCAST) {
                    int seq = intent.getIntExtra("seq", -1);
                    Log.i(TAG, "Enqueueing broadcast " + intent.getAction() + " seq=" + seq
                            + " to " + mReceiver);
                }
                Args args = new Args();
                args.mCurIntent = intent;
                args.mCurCode = resultCode;
                args.mCurData = data;
                args.mCurMap = extras;
                args.mCurOrdered = ordered;
                if (!mActivityThread.post(args)) {
                    if (mRegistered) {
                        IActivityManager mgr = ActivityManagerNative.getDefault();
                        try {
                            mgr.finishReceiver(mIIntentReceiver, args.mCurCode,
                                    args.mCurData, args.mCurMap, false);
                        } catch (RemoteException ex) {
                        }
                    }
                }
            }

        }

        public final IServiceConnection getServiceDispatcher(ServiceConnection c,
                Context context, Handler handler, int flags) {
            synchronized (mServices) {
                ServiceDispatcher sd = null;
                HashMap<ServiceConnection, ServiceDispatcher> map = mServices.get(context);
                if (map != null) {
                    sd = map.get(c);
                }
                if (sd == null) {
                    sd = new ServiceDispatcher(c, context, handler, flags);
                    if (map == null) {
                        map = new HashMap<ServiceConnection, ServiceDispatcher>();
                        mServices.put(context, map);
                    }
                    map.put(c, sd);
                } else {
                    sd.validate(context, handler);
                }
                return sd.getIServiceConnection();
            }
        }

        public final IServiceConnection forgetServiceDispatcher(Context context,
                ServiceConnection c) {
            synchronized (mServices) {
                HashMap<ServiceConnection, ServiceDispatcher> map
                        = mServices.get(context);
                ServiceDispatcher sd = null;
                if (map != null) {
                    sd = map.get(c);
                    if (sd != null) {
                        map.remove(c);
                        sd.doForget();
                        if (map.size() == 0) {
                            mServices.remove(context);
                        }
                        if ((sd.getFlags()&Context.BIND_DEBUG_UNBIND) != 0) {
                            HashMap<ServiceConnection, ServiceDispatcher> holder
                                    = mUnboundServices.get(context);
                            if (holder == null) {
                                holder = new HashMap<ServiceConnection, ServiceDispatcher>();
                                mUnboundServices.put(context, holder);
                            }
                            RuntimeException ex = new IllegalArgumentException(
                                    "Originally unbound here:");
                            ex.fillInStackTrace();
                            sd.setUnbindLocation(ex);
                            holder.put(c, sd);
                        }
                        return sd.getIServiceConnection();
                    }
                }
                HashMap<ServiceConnection, ServiceDispatcher> holder
                        = mUnboundServices.get(context);
                if (holder != null) {
                    sd = holder.get(c);
                    if (sd != null) {
                        RuntimeException ex = sd.getUnbindLocation();
                        throw new IllegalArgumentException(
                                "Unbinding Service " + c
                                + " that was already unbound", ex);
                    }
                }
                if (context == null) {
                    throw new IllegalStateException("Unbinding Service " + c
                            + " from Context that is no longer in use: " + context);
                } else {
                    throw new IllegalArgumentException("Service not registered: " + c);
                }
            }
        }

        static final class ServiceDispatcher {
            private final InnerConnection mIServiceConnection;
            private final ServiceConnection mConnection;
            private final Context mContext;
            private final Handler mActivityThread;
            private final ServiceConnectionLeaked mLocation;
            private final int mFlags;

            private RuntimeException mUnbindLocation;

            private boolean mDied;

            private static class ConnectionInfo {
                IBinder binder;
                IBinder.DeathRecipient deathMonitor;
            }

            private static class InnerConnection extends IServiceConnection.Stub {
                final WeakReference<ServiceDispatcher> mDispatcher;
                
                InnerConnection(ServiceDispatcher sd) {
                    mDispatcher = new WeakReference<ServiceDispatcher>(sd);
                }

                public void connected(ComponentName name, IBinder service) throws RemoteException {
                    ServiceDispatcher sd = mDispatcher.get();
                    if (sd != null) {
                        sd.connected(name, service);
                    }
                }
            }
            
            private final HashMap<ComponentName, ConnectionInfo> mActiveConnections
                = new HashMap<ComponentName, ConnectionInfo>();

            ServiceDispatcher(ServiceConnection conn,
                    Context context, Handler activityThread, int flags) {
                mIServiceConnection = new InnerConnection(this);
                mConnection = conn;
                mContext = context;
                mActivityThread = activityThread;
                mLocation = new ServiceConnectionLeaked(null);
                mLocation.fillInStackTrace();
                mFlags = flags;
            }

            void validate(Context context, Handler activityThread) {
                if (mContext != context) {
                    throw new RuntimeException(
                        "ServiceConnection " + mConnection +
                        " registered with differing Context (was " +
                        mContext + " now " + context + ")");
                }
                if (mActivityThread != activityThread) {
                    throw new RuntimeException(
                        "ServiceConnection " + mConnection +
                        " registered with differing handler (was " +
                        mActivityThread + " now " + activityThread + ")");
                }
            }

            void doForget() {
                synchronized(this) {
                    Iterator<ConnectionInfo> it = mActiveConnections.values().iterator();
                    while (it.hasNext()) {
                        ConnectionInfo ci = it.next();
                        ci.binder.unlinkToDeath(ci.deathMonitor, 0);
                    }
                    mActiveConnections.clear();
                }
            }

            ServiceConnectionLeaked getLocation() {
                return mLocation;
            }

            ServiceConnection getServiceConnection() {
                return mConnection;
            }

            IServiceConnection getIServiceConnection() {
                return mIServiceConnection;
            }
            
            int getFlags() {
                return mFlags;
            }

            void setUnbindLocation(RuntimeException ex) {
                mUnbindLocation = ex;
            }

            RuntimeException getUnbindLocation() {
                return mUnbindLocation;
            }

            public void connected(ComponentName name, IBinder service) {
                if (mActivityThread != null) {
                    mActivityThread.post(new RunConnection(name, service, 0));
                } else {
                    doConnected(name, service);
                }
            }

            public void death(ComponentName name, IBinder service) {
                ConnectionInfo old;

                synchronized (this) {
                    mDied = true;
                    old = mActiveConnections.remove(name);
                    if (old == null || old.binder != service) {
                        // Death for someone different than who we last
                        // reported...  just ignore it.
                        return;
                    }
                    old.binder.unlinkToDeath(old.deathMonitor, 0);
                }

                if (mActivityThread != null) {
                    mActivityThread.post(new RunConnection(name, service, 1));
                } else {
                    doDeath(name, service);
                }
            }

            public void doConnected(ComponentName name, IBinder service) {
                ConnectionInfo old;
                ConnectionInfo info;

                synchronized (this) {
                    old = mActiveConnections.get(name);
                    if (old != null && old.binder == service) {
                        // Huh, already have this one.  Oh well!
                        return;
                    }

                    if (service != null) {
                        // A new service is being connected... set it all up.
                        mDied = false;
                        info = new ConnectionInfo();
                        info.binder = service;
                        info.deathMonitor = new DeathMonitor(name, service);
                        try {
                            service.linkToDeath(info.deathMonitor, 0);
                            mActiveConnections.put(name, info);
                        } catch (RemoteException e) {
                            // This service was dead before we got it...  just
                            // don't do anything with it.
                            mActiveConnections.remove(name);
                            return;
                        }

                    } else {
                        // The named service is being disconnected... clean up.
                        mActiveConnections.remove(name);
                    }

                    if (old != null) {
                        old.binder.unlinkToDeath(old.deathMonitor, 0);
                    }
                }

                // If there was an old service, it is not disconnected.
                if (old != null) {
                    mConnection.onServiceDisconnected(name);
                }
                // If there is a new service, it is now connected.
                if (service != null) {
                    mConnection.onServiceConnected(name, service);
                }
            }

            public void doDeath(ComponentName name, IBinder service) {
                mConnection.onServiceDisconnected(name);
            }

            private final class RunConnection implements Runnable {
                RunConnection(ComponentName name, IBinder service, int command) {
                    mName = name;
                    mService = service;
                    mCommand = command;
                }

                public void run() {
                    if (mCommand == 0) {
                        doConnected(mName, mService);
                    } else if (mCommand == 1) {
                        doDeath(mName, mService);
                    }
                }

                final ComponentName mName;
                final IBinder mService;
                final int mCommand;
            }

            private final class DeathMonitor implements IBinder.DeathRecipient
            {
                DeathMonitor(ComponentName name, IBinder service) {
                    mName = name;
                    mService = service;
                }

                public void binderDied() {
                    death(mName, mService);
                }

                final ComponentName mName;
                final IBinder mService;
            }
        }
    }

    private static ApplicationContext mSystemContext = null;

    private static final class ActivityRecord {
        IBinder token;
>>>>>>> 54b6cfa... Initial Contribution
        Intent intent;
        Bundle state;
        Activity activity;
        Window window;
        Activity parent;
        String embeddedID;
<<<<<<< HEAD
        Activity.NonConfigurationInstances lastNonConfigurationInstances;
=======
        Object lastNonConfigurationInstance;
>>>>>>> 54b6cfa... Initial Contribution
        boolean paused;
        boolean stopped;
        boolean hideForNow;
        Configuration newConfig;
<<<<<<< HEAD
        Configuration createdConfig;
        ActivityClientRecord nextIdle;

        String profileFile;
        ParcelFileDescriptor profileFd;
        boolean autoStopProfiler;

        ActivityInfo activityInfo;
        CompatibilityInfo compatInfo;
        LoadedApk packageInfo;
=======
        ActivityRecord nextIdle;

        ActivityInfo activityInfo;
        PackageInfo packageInfo;
>>>>>>> 54b6cfa... Initial Contribution

        List<ResultInfo> pendingResults;
        List<Intent> pendingIntents;

        boolean startsNotResumed;
<<<<<<< HEAD
        boolean isForward;
        int pendingConfigChanges;
        boolean onlyLocalRequest;

        View mPendingRemoveWindow;
        WindowManager mPendingRemoveWindowManager;

        ActivityClientRecord() {
=======

        ActivityRecord() {
>>>>>>> 54b6cfa... Initial Contribution
            parent = null;
            embeddedID = null;
            paused = false;
            stopped = false;
            hideForNow = false;
            nextIdle = null;
        }

<<<<<<< HEAD
        public boolean isPreHoneycomb() {
            if (activity != null) {
                return activity.getApplicationInfo().targetSdkVersion
                        < android.os.Build.VERSION_CODES.HONEYCOMB;
            }
            return false;
        }

        public String toString() {
            ComponentName componentName = intent != null ? intent.getComponent() : null;
=======
        public String toString() {
            ComponentName componentName = intent.getComponent();
>>>>>>> 54b6cfa... Initial Contribution
            return "ActivityRecord{"
                + Integer.toHexString(System.identityHashCode(this))
                + " token=" + token + " " + (componentName == null
                        ? "no component name" : componentName.toShortString())
                + "}";
        }
    }

<<<<<<< HEAD
    final class ProviderClientRecord {
        final String[] mNames;
        final IContentProvider mProvider;
        final ContentProvider mLocalProvider;
        final IActivityManager.ContentProviderHolder mHolder;

        ProviderClientRecord(String[] names, IContentProvider provider,
                ContentProvider localProvider,
                IActivityManager.ContentProviderHolder holder) {
            mNames = names;
            mProvider = provider;
            mLocalProvider = localProvider;
            mHolder = holder;
        }
    }

    static final class NewIntentData {
=======
    private final class ProviderRecord implements IBinder.DeathRecipient {
        final String mName;
        final IContentProvider mProvider;
        final ContentProvider mLocalProvider;

        ProviderRecord(String name, IContentProvider provider,
                ContentProvider localProvider) {
            mName = name;
            mProvider = provider;
            mLocalProvider = localProvider;
        }

        public void binderDied() {
            removeDeadProvider(mName, mProvider);
        }
    }

    private static final class NewIntentData {
>>>>>>> 54b6cfa... Initial Contribution
        List<Intent> intents;
        IBinder token;
        public String toString() {
            return "NewIntentData{intents=" + intents + " token=" + token + "}";
        }
    }

<<<<<<< HEAD
    static final class ReceiverData extends BroadcastReceiver.PendingResult {
        public ReceiverData(Intent intent, int resultCode, String resultData, Bundle resultExtras,
                boolean ordered, boolean sticky, IBinder token) {
            super(resultCode, resultData, resultExtras, TYPE_COMPONENT, ordered, sticky, token);
            this.intent = intent;
        }

        Intent intent;
        ActivityInfo info;
        CompatibilityInfo compatInfo;
        public String toString() {
            return "ReceiverData{intent=" + intent + " packageName=" +
                    info.packageName + " resultCode=" + getResultCode()
                    + " resultData=" + getResultData() + " resultExtras="
                    + getResultExtras(false) + "}";
        }
    }

    static final class CreateBackupAgentData {
        ApplicationInfo appInfo;
        CompatibilityInfo compatInfo;
        int backupMode;
        public String toString() {
            return "CreateBackupAgentData{appInfo=" + appInfo
                    + " backupAgent=" + appInfo.backupAgentName
                    + " mode=" + backupMode + "}";
        }
    }

    static final class CreateServiceData {
        IBinder token;
        ServiceInfo info;
        CompatibilityInfo compatInfo;
=======
    private static final class ReceiverData {
        Intent intent;
        ActivityInfo info;
        int resultCode;
        String resultData;
        Bundle resultExtras;
        boolean sync;
        boolean resultAbort;
        public String toString() {
            return "ReceiverData{intent=" + intent + " packageName=" +
            info.packageName + " resultCode=" + resultCode
            + " resultData=" + resultData + " resultExtras=" + resultExtras + "}";
        }
    }

    private static final class CreateServiceData {
        IBinder token;
        ServiceInfo info;
>>>>>>> 54b6cfa... Initial Contribution
        Intent intent;
        public String toString() {
            return "CreateServiceData{token=" + token + " className="
            + info.name + " packageName=" + info.packageName
            + " intent=" + intent + "}";
        }
    }

<<<<<<< HEAD
    static final class BindServiceData {
=======
    private static final class BindServiceData {
>>>>>>> 54b6cfa... Initial Contribution
        IBinder token;
        Intent intent;
        boolean rebind;
        public String toString() {
            return "BindServiceData{token=" + token + " intent=" + intent + "}";
        }
    }

<<<<<<< HEAD
    static final class ServiceArgsData {
        IBinder token;
        boolean taskRemoved;
        int startId;
        int flags;
=======
    private static final class ServiceArgsData {
        IBinder token;
        int startId;
>>>>>>> 54b6cfa... Initial Contribution
        Intent args;
        public String toString() {
            return "ServiceArgsData{token=" + token + " startId=" + startId
            + " args=" + args + "}";
        }
    }

<<<<<<< HEAD
    static final class AppBindData {
        LoadedApk info;
=======
    private static final class AppBindData {
        PackageInfo info;
>>>>>>> 54b6cfa... Initial Contribution
        String processName;
        ApplicationInfo appInfo;
        List<ProviderInfo> providers;
        ComponentName instrumentationName;
<<<<<<< HEAD
        Bundle instrumentationArgs;
        IInstrumentationWatcher instrumentationWatcher;
        int debugMode;
        boolean enableOpenGlTrace;
        boolean restrictedBackupMode;
        boolean persistent;
        Configuration config;
        CompatibilityInfo compatInfo;

        /** Initial values for {@link Profiler}. */
        String initProfileFile;
        ParcelFileDescriptor initProfileFd;
        boolean initAutoStopProfiler;

=======
        String profileFile;
        Bundle instrumentationArgs;
        IInstrumentationWatcher instrumentationWatcher;
        int debugMode;
        Configuration config;
        boolean handlingProfiling;
>>>>>>> 54b6cfa... Initial Contribution
        public String toString() {
            return "AppBindData{appInfo=" + appInfo + "}";
        }
    }

<<<<<<< HEAD
    static final class Profiler {
        String profileFile;
        ParcelFileDescriptor profileFd;
        boolean autoStopProfiler;
        boolean profiling;
        boolean handlingProfiling;
        public void setProfiler(String file, ParcelFileDescriptor fd) {
            if (profiling) {
                if (fd != null) {
                    try {
                        fd.close();
                    } catch (IOException e) {
                        // Ignore
                    }
                }
                return;
            }
            if (profileFd != null) {
                try {
                    profileFd.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
            profileFile = file;
            profileFd = fd;
        }
        public void startProfiling() {
            if (profileFd == null || profiling) {
                return;
            }
            try {
                Debug.startMethodTracing(profileFile, profileFd.getFileDescriptor(),
                        8 * 1024 * 1024, 0);
                profiling = true;
            } catch (RuntimeException e) {
                Slog.w(TAG, "Profiling failed on path " + profileFile);
                try {
                    profileFd.close();
                    profileFd = null;
                } catch (IOException e2) {
                    Slog.w(TAG, "Failure closing profile fd", e2);
                }
            }
        }
        public void stopProfiling() {
            if (profiling) {
                profiling = false;
                Debug.stopMethodTracing();
                if (profileFd != null) {
                    try {
                        profileFd.close();
                    } catch (IOException e) {
                    }
                }
                profileFd = null;
                profileFile = null;
            }
        }
    }

    static final class DumpComponentInfo {
        ParcelFileDescriptor fd;
        IBinder token;
        String prefix;
        String[] args;
    }

    static final class ResultData {
=======
    private static final class DumpServiceInfo {
        FileDescriptor fd;
        IBinder service;
        String[] args;
        boolean dumped;
    }

    private static final class ResultData {
>>>>>>> 54b6cfa... Initial Contribution
        IBinder token;
        List<ResultInfo> results;
        public String toString() {
            return "ResultData{token=" + token + " results" + results + "}";
        }
    }

<<<<<<< HEAD
    static final class ContextCleanupInfo {
        ContextImpl context;
=======
    private static final class ContextCleanupInfo {
        ApplicationContext context;
>>>>>>> 54b6cfa... Initial Contribution
        String what;
        String who;
    }

<<<<<<< HEAD
    static final class ProfilerControlData {
        String path;
        ParcelFileDescriptor fd;
    }

    static final class DumpHeapData {
        String path;
        ParcelFileDescriptor fd;
    }

    static final class UpdateCompatibilityData {
        String pkg;
        CompatibilityInfo info;
    }
    
    private native void dumpGraphicsInfo(FileDescriptor fd);

    private class ApplicationThread extends ApplicationThreadNative {
        private static final String HEAP_COLUMN = "%13s %8s %8s %8s %8s %8s %8s";
        private static final String ONE_COUNT_COLUMN = "%21s %8d";
        private static final String TWO_COUNT_COLUMNS = "%21s %8d %21s %8d";
        private static final String DB_INFO_FORMAT = "  %8s %8s %14s %14s  %s";

        // Formatting for checkin service - update version if row format changes
        private static final int ACTIVITY_THREAD_CHECKIN_VERSION = 1;

        private void updatePendingConfiguration(Configuration config) {
            synchronized (mPackages) {
                if (mPendingConfiguration == null ||
                        mPendingConfiguration.isOtherSeqNewer(config)) {
                    mPendingConfiguration = config;
                }
            }
        }

        public final void schedulePauseActivity(IBinder token, boolean finished,
                boolean userLeaving, int configChanges) {
            queueOrSendMessage(
                    finished ? H.PAUSE_ACTIVITY_FINISHING : H.PAUSE_ACTIVITY,
                    token,
                    (userLeaving ? 1 : 0),
                    configChanges);
=======
    private final class ApplicationThread extends ApplicationThreadNative {
        private static final String HEAP_COLUMN = "%17s %8s %8s %8s %8s";
        private static final String ONE_COUNT_COLUMN = "%17s %8d";
        private static final String TWO_COUNT_COLUMNS = "%17s %8d %17s %8d";

        public final void schedulePauseActivity(IBinder token, boolean finished,
                int configChanges) {
            queueOrSendMessage(
                    finished ? H.PAUSE_ACTIVITY_FINISHING : H.PAUSE_ACTIVITY,
                    token, configChanges);
>>>>>>> 54b6cfa... Initial Contribution
        }

        public final void scheduleStopActivity(IBinder token, boolean showWindow,
                int configChanges) {
           queueOrSendMessage(
                showWindow ? H.STOP_ACTIVITY_SHOW : H.STOP_ACTIVITY_HIDE,
                token, 0, configChanges);
        }

        public final void scheduleWindowVisibility(IBinder token, boolean showWindow) {
            queueOrSendMessage(
                showWindow ? H.SHOW_WINDOW : H.HIDE_WINDOW,
                token);
        }

<<<<<<< HEAD
        public final void scheduleSleeping(IBinder token, boolean sleeping) {
            queueOrSendMessage(H.SLEEPING, token, sleeping ? 1 : 0);
        }

        public final void scheduleResumeActivity(IBinder token, boolean isForward) {
            queueOrSendMessage(H.RESUME_ACTIVITY, token, isForward ? 1 : 0);
=======
        public final void scheduleResumeActivity(IBinder token) {
            queueOrSendMessage(H.RESUME_ACTIVITY, token);
>>>>>>> 54b6cfa... Initial Contribution
        }

        public final void scheduleSendResult(IBinder token, List<ResultInfo> results) {
            ResultData res = new ResultData();
            res.token = token;
            res.results = results;
            queueOrSendMessage(H.SEND_RESULT, res);
        }

        // we use token to identify this activity without having to send the
        // activity itself back to the activity manager. (matters more with ipc)
<<<<<<< HEAD
        public final void scheduleLaunchActivity(Intent intent, IBinder token, int ident,
                ActivityInfo info, Configuration curConfig, CompatibilityInfo compatInfo,
                Bundle state, List<ResultInfo> pendingResults,
                List<Intent> pendingNewIntents, boolean notResumed, boolean isForward,
                String profileName, ParcelFileDescriptor profileFd, boolean autoStopProfiler) {
            ActivityClientRecord r = new ActivityClientRecord();

            r.token = token;
            r.ident = ident;
            r.intent = intent;
            r.activityInfo = info;
            r.compatInfo = compatInfo;
=======
        public final void scheduleLaunchActivity(Intent intent, IBinder token,
                ActivityInfo info, Bundle state, List<ResultInfo> pendingResults,
                List<Intent> pendingNewIntents, boolean notResumed) {
            ActivityRecord r = new ActivityRecord();

            r.token = token;
            r.intent = intent;
            r.activityInfo = info;
>>>>>>> 54b6cfa... Initial Contribution
            r.state = state;

            r.pendingResults = pendingResults;
            r.pendingIntents = pendingNewIntents;

            r.startsNotResumed = notResumed;
<<<<<<< HEAD
            r.isForward = isForward;

            r.profileFile = profileName;
            r.profileFd = profileFd;
            r.autoStopProfiler = autoStopProfiler;

            updatePendingConfiguration(curConfig);
=======
>>>>>>> 54b6cfa... Initial Contribution

            queueOrSendMessage(H.LAUNCH_ACTIVITY, r);
        }

        public final void scheduleRelaunchActivity(IBinder token,
                List<ResultInfo> pendingResults, List<Intent> pendingNewIntents,
<<<<<<< HEAD
                int configChanges, boolean notResumed, Configuration config) {
            requestRelaunchActivity(token, pendingResults, pendingNewIntents,
                    configChanges, notResumed, config, true);
=======
                int configChanges, boolean notResumed) {
            ActivityRecord r = new ActivityRecord();

            r.token = token;
            r.pendingResults = pendingResults;
            r.pendingIntents = pendingNewIntents;
            r.startsNotResumed = notResumed;

            synchronized (mRelaunchingActivities) {
                mRelaunchingActivities.add(r);
            }
            
            queueOrSendMessage(H.RELAUNCH_ACTIVITY, r, configChanges);
>>>>>>> 54b6cfa... Initial Contribution
        }

        public final void scheduleNewIntent(List<Intent> intents, IBinder token) {
            NewIntentData data = new NewIntentData();
            data.intents = intents;
            data.token = token;

            queueOrSendMessage(H.NEW_INTENT, data);
        }

        public final void scheduleDestroyActivity(IBinder token, boolean finishing,
                int configChanges) {
            queueOrSendMessage(H.DESTROY_ACTIVITY, token, finishing ? 1 : 0,
                    configChanges);
        }

        public final void scheduleReceiver(Intent intent, ActivityInfo info,
<<<<<<< HEAD
                CompatibilityInfo compatInfo, int resultCode, String data, Bundle extras,
                boolean sync) {
            ReceiverData r = new ReceiverData(intent, resultCode, data, extras,
                    sync, false, mAppThread.asBinder());
            r.info = info;
            r.compatInfo = compatInfo;
            queueOrSendMessage(H.RECEIVER, r);
        }

        public final void scheduleCreateBackupAgent(ApplicationInfo app,
                CompatibilityInfo compatInfo, int backupMode) {
            CreateBackupAgentData d = new CreateBackupAgentData();
            d.appInfo = app;
            d.compatInfo = compatInfo;
            d.backupMode = backupMode;

            queueOrSendMessage(H.CREATE_BACKUP_AGENT, d);
        }

        public final void scheduleDestroyBackupAgent(ApplicationInfo app,
                CompatibilityInfo compatInfo) {
            CreateBackupAgentData d = new CreateBackupAgentData();
            d.appInfo = app;
            d.compatInfo = compatInfo;

            queueOrSendMessage(H.DESTROY_BACKUP_AGENT, d);
        }

        public final void scheduleCreateService(IBinder token,
                ServiceInfo info, CompatibilityInfo compatInfo) {
            CreateServiceData s = new CreateServiceData();
            s.token = token;
            s.info = info;
            s.compatInfo = compatInfo;
=======
                int resultCode, String data, Bundle extras, boolean sync) {
            ReceiverData r = new ReceiverData();

            r.intent = intent;
            r.info = info;
            r.resultCode = resultCode;
            r.resultData = data;
            r.resultExtras = extras;
            r.sync = sync;

            queueOrSendMessage(H.RECEIVER, r);
        }

        public final void scheduleCreateService(IBinder token,
                ServiceInfo info) {
            CreateServiceData s = new CreateServiceData();
            s.token = token;
            s.info = info;
>>>>>>> 54b6cfa... Initial Contribution

            queueOrSendMessage(H.CREATE_SERVICE, s);
        }

        public final void scheduleBindService(IBinder token, Intent intent,
                boolean rebind) {
            BindServiceData s = new BindServiceData();
            s.token = token;
            s.intent = intent;
            s.rebind = rebind;

<<<<<<< HEAD
            if (DEBUG_SERVICE)
                Slog.v(TAG, "scheduleBindService token=" + token + " intent=" + intent + " uid="
                        + Binder.getCallingUid() + " pid=" + Binder.getCallingPid());
=======
>>>>>>> 54b6cfa... Initial Contribution
            queueOrSendMessage(H.BIND_SERVICE, s);
        }

        public final void scheduleUnbindService(IBinder token, Intent intent) {
            BindServiceData s = new BindServiceData();
            s.token = token;
            s.intent = intent;

            queueOrSendMessage(H.UNBIND_SERVICE, s);
        }

<<<<<<< HEAD
        public final void scheduleServiceArgs(IBinder token, boolean taskRemoved, int startId,
            int flags ,Intent args) {
            ServiceArgsData s = new ServiceArgsData();
            s.token = token;
            s.taskRemoved = taskRemoved;
            s.startId = startId;
            s.flags = flags;
=======
        public final void scheduleServiceArgs(IBinder token, int startId,
            Intent args) {
            ServiceArgsData s = new ServiceArgsData();
            s.token = token;
            s.startId = startId;
>>>>>>> 54b6cfa... Initial Contribution
            s.args = args;

            queueOrSendMessage(H.SERVICE_ARGS, s);
        }

        public final void scheduleStopService(IBinder token) {
            queueOrSendMessage(H.STOP_SERVICE, token);
        }

        public final void bindApplication(String processName,
                ApplicationInfo appInfo, List<ProviderInfo> providers,
                ComponentName instrumentationName, String profileFile,
<<<<<<< HEAD
                ParcelFileDescriptor profileFd, boolean autoStopProfiler,
                Bundle instrumentationArgs, IInstrumentationWatcher instrumentationWatcher,
                int debugMode, boolean enableOpenGlTrace, boolean isRestrictedBackupMode,
                boolean persistent, Configuration config, CompatibilityInfo compatInfo,
                Map<String, IBinder> services, Bundle coreSettings) {
=======
                Bundle instrumentationArgs, IInstrumentationWatcher instrumentationWatcher,
                int debugMode, Configuration config,
                Map<String, IBinder> services) {
            Process.setArgV0(processName);
>>>>>>> 54b6cfa... Initial Contribution

            if (services != null) {
                // Setup the service cache in the ServiceManager
                ServiceManager.initServiceCache(services);
            }

<<<<<<< HEAD
            setCoreSettings(coreSettings);

=======
>>>>>>> 54b6cfa... Initial Contribution
            AppBindData data = new AppBindData();
            data.processName = processName;
            data.appInfo = appInfo;
            data.providers = providers;
            data.instrumentationName = instrumentationName;
<<<<<<< HEAD
            data.instrumentationArgs = instrumentationArgs;
            data.instrumentationWatcher = instrumentationWatcher;
            data.debugMode = debugMode;
            data.enableOpenGlTrace = enableOpenGlTrace;
            data.restrictedBackupMode = isRestrictedBackupMode;
            data.persistent = persistent;
            data.config = config;
            data.compatInfo = compatInfo;
            data.initProfileFile = profileFile;
            data.initProfileFd = profileFd;
            data.initAutoStopProfiler = false;
=======
            data.profileFile = profileFile;
            data.instrumentationArgs = instrumentationArgs;
            data.instrumentationWatcher = instrumentationWatcher;
            data.debugMode = debugMode;
            data.config = config;
>>>>>>> 54b6cfa... Initial Contribution
            queueOrSendMessage(H.BIND_APPLICATION, data);
        }

        public final void scheduleExit() {
            queueOrSendMessage(H.EXIT_APPLICATION, null);
        }

<<<<<<< HEAD
        public final void scheduleSuicide() {
            queueOrSendMessage(H.SUICIDE, null);
        }

=======
>>>>>>> 54b6cfa... Initial Contribution
        public void requestThumbnail(IBinder token) {
            queueOrSendMessage(H.REQUEST_THUMBNAIL, token);
        }

        public void scheduleConfigurationChanged(Configuration config) {
<<<<<<< HEAD
            updatePendingConfiguration(config);
=======
            synchronized (mRelaunchingActivities) {
                mPendingConfiguration = config;
            }
>>>>>>> 54b6cfa... Initial Contribution
            queueOrSendMessage(H.CONFIGURATION_CHANGED, config);
        }

        public void updateTimeZone() {
            TimeZone.setDefault(null);
        }

<<<<<<< HEAD
        public void clearDnsCache() {
            // a non-standard API to get this to libcore
            InetAddress.clearDnsCache();
        }

        public void setHttpProxy(String host, String port, String exclList) {
            Proxy.setHttpProxySystemProperty(host, port, exclList);
        }

=======
>>>>>>> 54b6cfa... Initial Contribution
        public void processInBackground() {
            mH.removeMessages(H.GC_WHEN_IDLE);
            mH.sendMessage(mH.obtainMessage(H.GC_WHEN_IDLE));
        }

        public void dumpService(FileDescriptor fd, IBinder servicetoken, String[] args) {
<<<<<<< HEAD
            DumpComponentInfo data = new DumpComponentInfo();
            try {
                data.fd = ParcelFileDescriptor.dup(fd);
                data.token = servicetoken;
                data.args = args;
                queueOrSendMessage(H.DUMP_SERVICE, data);
            } catch (IOException e) {
                Slog.w(TAG, "dumpService failed", e);
=======
            DumpServiceInfo data = new DumpServiceInfo();
            data.fd = fd;
            data.service = servicetoken;
            data.args = args;
            data.dumped = false;
            queueOrSendMessage(H.DUMP_SERVICE, data);
            synchronized (data) {
                while (!data.dumped) {
                    try {
                        data.wait();
                    } catch (InterruptedException e) {
                        // no need to do anything here, we will keep waiting until
                        // dumped is set
                    }
                }
>>>>>>> 54b6cfa... Initial Contribution
            }
        }

        // This function exists to make sure all receiver dispatching is
        // correctly ordered, since these are one-way calls and the binder driver
        // applies transaction ordering per object for such calls.
        public void scheduleRegisteredReceiver(IIntentReceiver receiver, Intent intent,
<<<<<<< HEAD
                int resultCode, String dataStr, Bundle extras, boolean ordered,
                boolean sticky) throws RemoteException {
            receiver.performReceive(intent, resultCode, dataStr, extras, ordered, sticky);
        }

=======
                int resultCode, String dataStr, Bundle extras, boolean ordered)
                throws RemoteException {
            receiver.performReceive(intent, resultCode, dataStr, extras, ordered);
        }
        
>>>>>>> 54b6cfa... Initial Contribution
        public void scheduleLowMemory() {
            queueOrSendMessage(H.LOW_MEMORY, null);
        }

        public void scheduleActivityConfigurationChanged(IBinder token) {
            queueOrSendMessage(H.ACTIVITY_CONFIGURATION_CHANGED, token);
        }

<<<<<<< HEAD
        public void profilerControl(boolean start, String path, ParcelFileDescriptor fd,
                int profileType) {
            ProfilerControlData pcd = new ProfilerControlData();
            pcd.path = path;
            pcd.fd = fd;
            queueOrSendMessage(H.PROFILER_CONTROL, pcd, start ? 1 : 0, profileType);
        }

        public void dumpHeap(boolean managed, String path, ParcelFileDescriptor fd) {
            DumpHeapData dhd = new DumpHeapData();
            dhd.path = path;
            dhd.fd = fd;
            queueOrSendMessage(H.DUMP_HEAP, dhd, managed ? 1 : 0);
        }

        public void setSchedulingGroup(int group) {
            // Note: do this immediately, since going into the foreground
            // should happen regardless of what pending work we have to do
            // and the activity manager will wait for us to report back that
            // we are done before sending us to the background.
            try {
                Process.setProcessGroup(Process.myPid(), group);
            } catch (Exception e) {
                Slog.w(TAG, "Failed setting process group to " + group, e);
            }
        }

        public void getMemoryInfo(Debug.MemoryInfo outInfo) {
            Debug.getMemoryInfo(outInfo);
        }

        public void dispatchPackageBroadcast(int cmd, String[] packages) {
            queueOrSendMessage(H.DISPATCH_PACKAGE_BROADCAST, packages, cmd);
        }

        public void scheduleCrash(String msg) {
            queueOrSendMessage(H.SCHEDULE_CRASH, msg);
        }

        public void dumpActivity(FileDescriptor fd, IBinder activitytoken,
                String prefix, String[] args) {
            DumpComponentInfo data = new DumpComponentInfo();
            try {
                data.fd = ParcelFileDescriptor.dup(fd);
                data.token = activitytoken;
                data.prefix = prefix;
                data.args = args;
                queueOrSendMessage(H.DUMP_ACTIVITY, data);
            } catch (IOException e) {
                Slog.w(TAG, "dumpActivity failed", e);
            }
        }

        public void dumpProvider(FileDescriptor fd, IBinder providertoken,
                String[] args) {
            DumpComponentInfo data = new DumpComponentInfo();
            try {
                data.fd = ParcelFileDescriptor.dup(fd);
                data.token = providertoken;
                data.args = args;
                queueOrSendMessage(H.DUMP_PROVIDER, data);
            } catch (IOException e) {
                Slog.w(TAG, "dumpProvider failed", e);
            }
        }

        @Override
        public Debug.MemoryInfo dumpMemInfo(FileDescriptor fd, boolean checkin,
                boolean all, String[] args) {
            FileOutputStream fout = new FileOutputStream(fd);
            PrintWriter pw = new PrintWriter(fout);
            try {
                return dumpMemInfo(pw, checkin, all);
            } finally {
                pw.flush();
            }
        }

        private Debug.MemoryInfo dumpMemInfo(PrintWriter pw, boolean checkin, boolean all) {
=======
        public void requestPss() {
            try {
                ActivityManagerNative.getDefault().reportPss(this,
                        (int)Process.getPss(Process.myPid()));
            } catch (RemoteException e) {
            }
        }
        
        @Override
        protected void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
>>>>>>> 54b6cfa... Initial Contribution
            long nativeMax = Debug.getNativeHeapSize() / 1024;
            long nativeAllocated = Debug.getNativeHeapAllocatedSize() / 1024;
            long nativeFree = Debug.getNativeHeapFreeSize() / 1024;

            Debug.MemoryInfo memInfo = new Debug.MemoryInfo();
            Debug.getMemoryInfo(memInfo);

<<<<<<< HEAD
            if (!all) {
                return memInfo;
            }
=======
            final int nativeShared = memInfo.nativeSharedDirty;
            final int dalvikShared = memInfo.dalvikSharedDirty;
            final int otherShared = memInfo.otherSharedDirty;

            final int nativePrivate = memInfo.nativePrivateDirty;
            final int dalvikPrivate = memInfo.dalvikPrivateDirty;
            final int otherPrivate = memInfo.otherPrivateDirty;
>>>>>>> 54b6cfa... Initial Contribution

            Runtime runtime = Runtime.getRuntime();

            long dalvikMax = runtime.totalMemory() / 1024;
            long dalvikFree = runtime.freeMemory() / 1024;
            long dalvikAllocated = dalvikMax - dalvikFree;
<<<<<<< HEAD
            long viewInstanceCount = ViewDebug.getViewInstanceCount();
            long viewRootInstanceCount = ViewDebug.getViewRootImplCount();
            long appContextInstanceCount = Debug.countInstancesOfClass(ContextImpl.class);
            long activityInstanceCount = Debug.countInstancesOfClass(Activity.class);
            int globalAssetCount = AssetManager.getGlobalAssetCount();
            int globalAssetManagerCount = AssetManager.getGlobalAssetManagerCount();
            int binderLocalObjectCount = Debug.getBinderLocalObjectCount();
            int binderProxyObjectCount = Debug.getBinderProxyObjectCount();
            int binderDeathObjectCount = Debug.getBinderDeathObjectCount();
            long openSslSocketCount = Debug.countInstancesOfClass(OpenSSLSocketImpl.class);
            SQLiteDebug.PagerStats stats = SQLiteDebug.getDatabaseInfo();

            // For checkin, we print one long comma-separated list of values
            if (checkin) {
                // NOTE: if you change anything significant below, also consider changing
                // ACTIVITY_THREAD_CHECKIN_VERSION.
                String processName = (mBoundApplication != null)
                        ? mBoundApplication.processName : "unknown";

                // Header
                pw.print(ACTIVITY_THREAD_CHECKIN_VERSION); pw.print(',');
                pw.print(Process.myPid()); pw.print(',');
                pw.print(processName); pw.print(',');

                // Heap info - max
                pw.print(nativeMax); pw.print(',');
                pw.print(dalvikMax); pw.print(',');
                pw.print("N/A,");
                pw.print(nativeMax + dalvikMax); pw.print(',');

                // Heap info - allocated
                pw.print(nativeAllocated); pw.print(',');
                pw.print(dalvikAllocated); pw.print(',');
                pw.print("N/A,");
                pw.print(nativeAllocated + dalvikAllocated); pw.print(',');

                // Heap info - free
                pw.print(nativeFree); pw.print(',');
                pw.print(dalvikFree); pw.print(',');
                pw.print("N/A,");
                pw.print(nativeFree + dalvikFree); pw.print(',');

                // Heap info - proportional set size
                pw.print(memInfo.nativePss); pw.print(',');
                pw.print(memInfo.dalvikPss); pw.print(',');
                pw.print(memInfo.otherPss); pw.print(',');
                pw.print(memInfo.nativePss + memInfo.dalvikPss + memInfo.otherPss); pw.print(',');

                // Heap info - shared
                pw.print(memInfo.nativeSharedDirty); pw.print(',');
                pw.print(memInfo.dalvikSharedDirty); pw.print(',');
                pw.print(memInfo.otherSharedDirty); pw.print(',');
                pw.print(memInfo.nativeSharedDirty + memInfo.dalvikSharedDirty
                        + memInfo.otherSharedDirty); pw.print(',');

                // Heap info - private
                pw.print(memInfo.nativePrivateDirty); pw.print(',');
                pw.print(memInfo.dalvikPrivateDirty); pw.print(',');
                pw.print(memInfo.otherPrivateDirty); pw.print(',');
                pw.print(memInfo.nativePrivateDirty + memInfo.dalvikPrivateDirty
                        + memInfo.otherPrivateDirty); pw.print(',');

                // Object counts
                pw.print(viewInstanceCount); pw.print(',');
                pw.print(viewRootInstanceCount); pw.print(',');
                pw.print(appContextInstanceCount); pw.print(',');
                pw.print(activityInstanceCount); pw.print(',');

                pw.print(globalAssetCount); pw.print(',');
                pw.print(globalAssetManagerCount); pw.print(',');
                pw.print(binderLocalObjectCount); pw.print(',');
                pw.print(binderProxyObjectCount); pw.print(',');

                pw.print(binderDeathObjectCount); pw.print(',');
                pw.print(openSslSocketCount); pw.print(',');

                // SQL
                pw.print(stats.memoryUsed / 1024); pw.print(',');
                pw.print(stats.memoryUsed / 1024); pw.print(',');
                pw.print(stats.pageCacheOverflow / 1024); pw.print(',');
                pw.print(stats.largestMemAlloc / 1024);
                for (int i = 0; i < stats.dbStats.size(); i++) {
                    DbStats dbStats = stats.dbStats.get(i);
                    pw.print(','); pw.print(dbStats.dbName);
                    pw.print(','); pw.print(dbStats.pageSize);
                    pw.print(','); pw.print(dbStats.dbSize);
                    pw.print(','); pw.print(dbStats.lookaside);
                    pw.print(','); pw.print(dbStats.cache);
                    pw.print(','); pw.print(dbStats.cache);
                }
                pw.println();

                return memInfo;
            }

            // otherwise, show human-readable format
            printRow(pw, HEAP_COLUMN, "", "", "Shared", "Private", "Heap", "Heap", "Heap");
            printRow(pw, HEAP_COLUMN, "", "Pss", "Dirty", "Dirty", "Size", "Alloc", "Free");
            printRow(pw, HEAP_COLUMN, "", "------", "------", "------", "------", "------",
                    "------");
            printRow(pw, HEAP_COLUMN, "Native", memInfo.nativePss, memInfo.nativeSharedDirty,
                    memInfo.nativePrivateDirty, nativeMax, nativeAllocated, nativeFree);
            printRow(pw, HEAP_COLUMN, "Dalvik", memInfo.dalvikPss, memInfo.dalvikSharedDirty,
                    memInfo.dalvikPrivateDirty, dalvikMax, dalvikAllocated, dalvikFree);

            int otherPss = memInfo.otherPss;
            int otherSharedDirty = memInfo.otherSharedDirty;
            int otherPrivateDirty = memInfo.otherPrivateDirty;

            for (int i=0; i<Debug.MemoryInfo.NUM_OTHER_STATS; i++) {
                printRow(pw, HEAP_COLUMN, Debug.MemoryInfo.getOtherLabel(i),
                        memInfo.getOtherPss(i), memInfo.getOtherSharedDirty(i),
                        memInfo.getOtherPrivateDirty(i), "", "", "");
                otherPss -= memInfo.getOtherPss(i);
                otherSharedDirty -= memInfo.getOtherSharedDirty(i);
                otherPrivateDirty -= memInfo.getOtherPrivateDirty(i);
            }

            printRow(pw, HEAP_COLUMN, "Unknown", otherPss, otherSharedDirty,
                    otherPrivateDirty, "", "", "");
            printRow(pw, HEAP_COLUMN, "TOTAL", memInfo.getTotalPss(),
                    memInfo.getTotalSharedDirty(), memInfo.getTotalPrivateDirty(),
                    nativeMax+dalvikMax, nativeAllocated+dalvikAllocated,
                    nativeFree+dalvikFree);

            pw.println(" ");
            pw.println(" Objects");
            printRow(pw, TWO_COUNT_COLUMNS, "Views:", viewInstanceCount, "ViewRootImpl:",
                    viewRootInstanceCount);

            printRow(pw, TWO_COUNT_COLUMNS, "AppContexts:", appContextInstanceCount,
                    "Activities:", activityInstanceCount);

            printRow(pw, TWO_COUNT_COLUMNS, "Assets:", globalAssetCount,
                    "AssetManagers:", globalAssetManagerCount);

            printRow(pw, TWO_COUNT_COLUMNS, "Local Binders:", binderLocalObjectCount,
                    "Proxy Binders:", binderProxyObjectCount);
            printRow(pw, ONE_COUNT_COLUMN, "Death Recipients:", binderDeathObjectCount);

            printRow(pw, ONE_COUNT_COLUMN, "OpenSSL Sockets:", openSslSocketCount);

            // SQLite mem info
            pw.println(" ");
            pw.println(" SQL");
            printRow(pw, ONE_COUNT_COLUMN, "MEMORY_USED:", stats.memoryUsed / 1024);
            printRow(pw, TWO_COUNT_COLUMNS, "PAGECACHE_OVERFLOW:",
                    stats.pageCacheOverflow / 1024, "MALLOC_SIZE:", stats.largestMemAlloc / 1024);
            pw.println(" ");
            int N = stats.dbStats.size();
            if (N > 0) {
                pw.println(" DATABASES");
                printRow(pw, "  %8s %8s %14s %14s  %s", "pgsz", "dbsz", "Lookaside(b)", "cache",
                        "Dbname");
                for (int i = 0; i < N; i++) {
                    DbStats dbStats = stats.dbStats.get(i);
                    printRow(pw, DB_INFO_FORMAT,
                            (dbStats.pageSize > 0) ? String.valueOf(dbStats.pageSize) : " ",
                            (dbStats.dbSize > 0) ? String.valueOf(dbStats.dbSize) : " ",
                            (dbStats.lookaside > 0) ? String.valueOf(dbStats.lookaside) : " ",
                            dbStats.cache, dbStats.dbName);
                }
            }

            // Asset details.
            String assetAlloc = AssetManager.getAssetAllocations();
            if (assetAlloc != null) {
                pw.println(" ");
                pw.println(" Asset Allocations");
                pw.print(assetAlloc);
            }

            return memInfo;
        }

        @Override
        public void dumpGfxInfo(FileDescriptor fd, String[] args) {
            dumpGraphicsInfo(fd);
            WindowManagerImpl.getDefault().dumpGfxInfo(fd);
        }

        @Override
        public void dumpDbInfo(FileDescriptor fd, String[] args) {
            PrintWriter pw = new PrintWriter(new FileOutputStream(fd));
            PrintWriterPrinter printer = new PrintWriterPrinter(pw);
            SQLiteDebug.dump(printer, args);
            pw.flush();
        }

        @Override
        public void unstableProviderDied(IBinder provider) {
            queueOrSendMessage(H.UNSTABLE_PROVIDER_DIED, provider);
        }

        private void printRow(PrintWriter pw, String format, Object...objs) {
            pw.println(String.format(format, objs));
        }

        public void setCoreSettings(Bundle coreSettings) {
            queueOrSendMessage(H.SET_CORE_SETTINGS, coreSettings);
        }

        public void updatePackageCompatibilityInfo(String pkg, CompatibilityInfo info) {
            UpdateCompatibilityData ucd = new UpdateCompatibilityData();
            ucd.pkg = pkg;
            ucd.info = info;
            queueOrSendMessage(H.UPDATE_PACKAGE_COMPATIBILITY_INFO, ucd);
        }

        public void scheduleTrimMemory(int level) {
            queueOrSendMessage(H.TRIM_MEMORY, null, level);
        }

    }

    private class H extends Handler {
=======

            printRow(pw, HEAP_COLUMN, "", "native", "dalvik", "other", "total");
            printRow(pw, HEAP_COLUMN, "size:", nativeMax, dalvikMax, "N/A", nativeMax + dalvikMax);
            printRow(pw, HEAP_COLUMN, "allocated:", nativeAllocated, dalvikAllocated, "N/A",
                    nativeAllocated + dalvikAllocated);
            printRow(pw, HEAP_COLUMN, "free:", nativeFree, dalvikFree, "N/A",
                    nativeFree + dalvikFree);

            printRow(pw, HEAP_COLUMN, "(Pss):", memInfo.nativePss, memInfo.dalvikPss,
                    memInfo.otherPss, memInfo.nativePss + memInfo.dalvikPss + memInfo.otherPss);

            printRow(pw, HEAP_COLUMN, "(shared dirty):", nativeShared, dalvikShared, otherShared,
                    nativeShared + dalvikShared + otherShared);
            printRow(pw, HEAP_COLUMN, "(priv dirty):", nativePrivate, dalvikPrivate, otherPrivate,
                    nativePrivate + dalvikPrivate + otherPrivate);

            pw.println(" ");
            pw.println(" Objects");
            printRow(pw, TWO_COUNT_COLUMNS, "Views:", ViewDebug.getViewInstanceCount(), "ViewRoots:",
                    ViewDebug.getViewRootInstanceCount());

            printRow(pw, TWO_COUNT_COLUMNS, "AppContexts:", ApplicationContext.getInstanceCount(),
                    "Activities:", Activity.getInstanceCount());

            printRow(pw, TWO_COUNT_COLUMNS, "Assets:", AssetManager.getGlobalAssetCount(),
                    "AssetManagers:", AssetManager.getGlobalAssetManagerCount());

            printRow(pw, TWO_COUNT_COLUMNS, "Local Binders:", Debug.getBinderLocalObjectCount(),
                    "Proxy Binders:", Debug.getBinderProxyObjectCount());
            printRow(pw, ONE_COUNT_COLUMN, "Death Recipients:", Debug.getBinderDeathObjectCount());

            printRow(pw, ONE_COUNT_COLUMN, "OpenSSL Sockets:", OpenSSLSocketImpl.getInstanceCount());

            // SQLite mem info
            long sqliteAllocated = SQLiteDebug.getHeapAllocatedSize() / 1024;
            SQLiteDebug.PagerStats stats = new SQLiteDebug.PagerStats();
            SQLiteDebug.getPagerStats(stats);

            pw.println(" ");
            pw.println(" SQL");
            printRow(pw, TWO_COUNT_COLUMNS, "heap:", sqliteAllocated, "dbFiles:",
                    stats.databaseBytes / 1024);
            printRow(pw, TWO_COUNT_COLUMNS, "numPagers:", stats.numPagers, "inactivePageKB:",
                    (stats.totalBytes - stats.referencedBytes) / 1024);
            printRow(pw, ONE_COUNT_COLUMN, "activePageKB:", stats.referencedBytes / 1024);
        }

        private void printRow(PrintWriter pw, String format, Object...objs) {
            pw.println(String.format(format, objs));
        }
    }

    private final class H extends Handler {
>>>>>>> 54b6cfa... Initial Contribution
        public static final int LAUNCH_ACTIVITY         = 100;
        public static final int PAUSE_ACTIVITY          = 101;
        public static final int PAUSE_ACTIVITY_FINISHING= 102;
        public static final int STOP_ACTIVITY_SHOW      = 103;
        public static final int STOP_ACTIVITY_HIDE      = 104;
        public static final int SHOW_WINDOW             = 105;
        public static final int HIDE_WINDOW             = 106;
        public static final int RESUME_ACTIVITY         = 107;
        public static final int SEND_RESULT             = 108;
<<<<<<< HEAD
        public static final int DESTROY_ACTIVITY        = 109;
=======
        public static final int DESTROY_ACTIVITY         = 109;
>>>>>>> 54b6cfa... Initial Contribution
        public static final int BIND_APPLICATION        = 110;
        public static final int EXIT_APPLICATION        = 111;
        public static final int NEW_INTENT              = 112;
        public static final int RECEIVER                = 113;
        public static final int CREATE_SERVICE          = 114;
        public static final int SERVICE_ARGS            = 115;
        public static final int STOP_SERVICE            = 116;
        public static final int REQUEST_THUMBNAIL       = 117;
        public static final int CONFIGURATION_CHANGED   = 118;
        public static final int CLEAN_UP_CONTEXT        = 119;
        public static final int GC_WHEN_IDLE            = 120;
        public static final int BIND_SERVICE            = 121;
        public static final int UNBIND_SERVICE          = 122;
        public static final int DUMP_SERVICE            = 123;
        public static final int LOW_MEMORY              = 124;
        public static final int ACTIVITY_CONFIGURATION_CHANGED = 125;
        public static final int RELAUNCH_ACTIVITY       = 126;
<<<<<<< HEAD
        public static final int PROFILER_CONTROL        = 127;
        public static final int CREATE_BACKUP_AGENT     = 128;
        public static final int DESTROY_BACKUP_AGENT    = 129;
        public static final int SUICIDE                 = 130;
        public static final int REMOVE_PROVIDER         = 131;
        public static final int ENABLE_JIT              = 132;
        public static final int DISPATCH_PACKAGE_BROADCAST = 133;
        public static final int SCHEDULE_CRASH          = 134;
        public static final int DUMP_HEAP               = 135;
        public static final int DUMP_ACTIVITY           = 136;
        public static final int SLEEPING                = 137;
        public static final int SET_CORE_SETTINGS       = 138;
        public static final int UPDATE_PACKAGE_COMPATIBILITY_INFO = 139;
        public static final int TRIM_MEMORY             = 140;
        public static final int DUMP_PROVIDER           = 141;
        public static final int UNSTABLE_PROVIDER_DIED  = 142;
        String codeToString(int code) {
            if (DEBUG_MESSAGES) {
=======
        String codeToString(int code) {
            if (localLOGV) {
>>>>>>> 54b6cfa... Initial Contribution
                switch (code) {
                    case LAUNCH_ACTIVITY: return "LAUNCH_ACTIVITY";
                    case PAUSE_ACTIVITY: return "PAUSE_ACTIVITY";
                    case PAUSE_ACTIVITY_FINISHING: return "PAUSE_ACTIVITY_FINISHING";
                    case STOP_ACTIVITY_SHOW: return "STOP_ACTIVITY_SHOW";
                    case STOP_ACTIVITY_HIDE: return "STOP_ACTIVITY_HIDE";
                    case SHOW_WINDOW: return "SHOW_WINDOW";
                    case HIDE_WINDOW: return "HIDE_WINDOW";
                    case RESUME_ACTIVITY: return "RESUME_ACTIVITY";
                    case SEND_RESULT: return "SEND_RESULT";
                    case DESTROY_ACTIVITY: return "DESTROY_ACTIVITY";
                    case BIND_APPLICATION: return "BIND_APPLICATION";
                    case EXIT_APPLICATION: return "EXIT_APPLICATION";
                    case NEW_INTENT: return "NEW_INTENT";
                    case RECEIVER: return "RECEIVER";
                    case CREATE_SERVICE: return "CREATE_SERVICE";
                    case SERVICE_ARGS: return "SERVICE_ARGS";
                    case STOP_SERVICE: return "STOP_SERVICE";
                    case REQUEST_THUMBNAIL: return "REQUEST_THUMBNAIL";
                    case CONFIGURATION_CHANGED: return "CONFIGURATION_CHANGED";
                    case CLEAN_UP_CONTEXT: return "CLEAN_UP_CONTEXT";
                    case GC_WHEN_IDLE: return "GC_WHEN_IDLE";
                    case BIND_SERVICE: return "BIND_SERVICE";
                    case UNBIND_SERVICE: return "UNBIND_SERVICE";
                    case DUMP_SERVICE: return "DUMP_SERVICE";
                    case LOW_MEMORY: return "LOW_MEMORY";
                    case ACTIVITY_CONFIGURATION_CHANGED: return "ACTIVITY_CONFIGURATION_CHANGED";
                    case RELAUNCH_ACTIVITY: return "RELAUNCH_ACTIVITY";
<<<<<<< HEAD
                    case PROFILER_CONTROL: return "PROFILER_CONTROL";
                    case CREATE_BACKUP_AGENT: return "CREATE_BACKUP_AGENT";
                    case DESTROY_BACKUP_AGENT: return "DESTROY_BACKUP_AGENT";
                    case SUICIDE: return "SUICIDE";
                    case REMOVE_PROVIDER: return "REMOVE_PROVIDER";
                    case ENABLE_JIT: return "ENABLE_JIT";
                    case DISPATCH_PACKAGE_BROADCAST: return "DISPATCH_PACKAGE_BROADCAST";
                    case SCHEDULE_CRASH: return "SCHEDULE_CRASH";
                    case DUMP_HEAP: return "DUMP_HEAP";
                    case DUMP_ACTIVITY: return "DUMP_ACTIVITY";
                    case SLEEPING: return "SLEEPING";
                    case SET_CORE_SETTINGS: return "SET_CORE_SETTINGS";
                    case UPDATE_PACKAGE_COMPATIBILITY_INFO: return "UPDATE_PACKAGE_COMPATIBILITY_INFO";
                    case TRIM_MEMORY: return "TRIM_MEMORY";
                    case DUMP_PROVIDER: return "DUMP_PROVIDER";
                    case UNSTABLE_PROVIDER_DIED: return "UNSTABLE_PROVIDER_DIED";
                }
            }
            return Integer.toString(code);
        }
        public void handleMessage(Message msg) {
            if (DEBUG_MESSAGES) Slog.v(TAG, ">>> handling: " + codeToString(msg.what));
            switch (msg.what) {
                case LAUNCH_ACTIVITY: {
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityStart");
                    ActivityClientRecord r = (ActivityClientRecord)msg.obj;

                    r.packageInfo = getPackageInfoNoCheck(
                            r.activityInfo.applicationInfo, r.compatInfo);
                    handleLaunchActivity(r, null);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                } break;
                case RELAUNCH_ACTIVITY: {
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityRestart");
                    ActivityClientRecord r = (ActivityClientRecord)msg.obj;
                    handleRelaunchActivity(r);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                } break;
                case PAUSE_ACTIVITY:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityPause");
                    handlePauseActivity((IBinder)msg.obj, false, msg.arg1 != 0, msg.arg2);
                    maybeSnapshot();
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case PAUSE_ACTIVITY_FINISHING:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityPause");
                    handlePauseActivity((IBinder)msg.obj, true, msg.arg1 != 0, msg.arg2);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case STOP_ACTIVITY_SHOW:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityStop");
                    handleStopActivity((IBinder)msg.obj, true, msg.arg2);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case STOP_ACTIVITY_HIDE:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityStop");
                    handleStopActivity((IBinder)msg.obj, false, msg.arg2);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case SHOW_WINDOW:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityShowWindow");
                    handleWindowVisibility((IBinder)msg.obj, true);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case HIDE_WINDOW:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityHideWindow");
                    handleWindowVisibility((IBinder)msg.obj, false);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case RESUME_ACTIVITY:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityResume");
                    handleResumeActivity((IBinder)msg.obj, true,
                            msg.arg1 != 0);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case SEND_RESULT:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityDeliverResult");
                    handleSendResult((ResultData)msg.obj);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case DESTROY_ACTIVITY:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityDestroy");
                    handleDestroyActivity((IBinder)msg.obj, msg.arg1 != 0,
                            msg.arg2, false);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case BIND_APPLICATION:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "bindApplication");
                    AppBindData data = (AppBindData)msg.obj;
                    handleBindApplication(data);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
=======
                }
            }
            return "(unknown)";
        }
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LAUNCH_ACTIVITY: {
                    ActivityRecord r = (ActivityRecord)msg.obj;

                    r.packageInfo = getPackageInfoNoCheck(
                            r.activityInfo.applicationInfo);
                    handleLaunchActivity(r);
                } break;
                case RELAUNCH_ACTIVITY: {
                    ActivityRecord r = (ActivityRecord)msg.obj;
                    handleRelaunchActivity(r, msg.arg1);
                } break;
                case PAUSE_ACTIVITY:
                    handlePauseActivity((IBinder)msg.obj, false, msg.arg2);
                    break;
                case PAUSE_ACTIVITY_FINISHING:
                    handlePauseActivity((IBinder)msg.obj, true, msg.arg2);
                    break;
                case STOP_ACTIVITY_SHOW:
                    handleStopActivity((IBinder)msg.obj, true, msg.arg2);
                    break;
                case STOP_ACTIVITY_HIDE:
                    handleStopActivity((IBinder)msg.obj, false, msg.arg2);
                    break;
                case SHOW_WINDOW:
                    handleWindowVisibility((IBinder)msg.obj, true);
                    break;
                case HIDE_WINDOW:
                    handleWindowVisibility((IBinder)msg.obj, false);
                    break;
                case RESUME_ACTIVITY:
                    handleResumeActivity((IBinder)msg.obj, true);
                    break;
                case SEND_RESULT:
                    handleSendResult((ResultData)msg.obj);
                    break;
                case DESTROY_ACTIVITY:
                    handleDestroyActivity((IBinder)msg.obj, msg.arg1 != 0,
                            msg.arg2, false);
                    break;
                case BIND_APPLICATION:
                    AppBindData data = (AppBindData)msg.obj;
                    handleBindApplication(data);
>>>>>>> 54b6cfa... Initial Contribution
                    break;
                case EXIT_APPLICATION:
                    if (mInitialApplication != null) {
                        mInitialApplication.onTerminate();
                    }
                    Looper.myLooper().quit();
                    break;
                case NEW_INTENT:
<<<<<<< HEAD
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityNewIntent");
                    handleNewIntent((NewIntentData)msg.obj);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case RECEIVER:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "broadcastReceiveComp");
                    handleReceiver((ReceiverData)msg.obj);
                    maybeSnapshot();
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case CREATE_SERVICE:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "serviceCreate");
                    handleCreateService((CreateServiceData)msg.obj);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case BIND_SERVICE:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "serviceBind");
                    handleBindService((BindServiceData)msg.obj);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case UNBIND_SERVICE:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "serviceUnbind");
                    handleUnbindService((BindServiceData)msg.obj);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case SERVICE_ARGS:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "serviceStart");
                    handleServiceArgs((ServiceArgsData)msg.obj);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case STOP_SERVICE:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "serviceStop");
                    handleStopService((IBinder)msg.obj);
                    maybeSnapshot();
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case REQUEST_THUMBNAIL:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "requestThumbnail");
                    handleRequestThumbnail((IBinder)msg.obj);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case CONFIGURATION_CHANGED:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "configChanged");
                    handleConfigurationChanged((Configuration)msg.obj, null);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
=======
                    handleNewIntent((NewIntentData)msg.obj);
                    break;
                case RECEIVER:
                    handleReceiver((ReceiverData)msg.obj);
                    break;
                case CREATE_SERVICE:
                    handleCreateService((CreateServiceData)msg.obj);
                    break;
                case BIND_SERVICE:
                    handleBindService((BindServiceData)msg.obj);
                    break;
                case UNBIND_SERVICE:
                    handleUnbindService((BindServiceData)msg.obj);
                    break;
                case SERVICE_ARGS:
                    handleServiceArgs((ServiceArgsData)msg.obj);
                    break;
                case STOP_SERVICE:
                    handleStopService((IBinder)msg.obj);
                    break;
                case REQUEST_THUMBNAIL:
                    handleRequestThumbnail((IBinder)msg.obj);
                    break;
                case CONFIGURATION_CHANGED:
                    handleConfigurationChanged((Configuration)msg.obj);
>>>>>>> 54b6cfa... Initial Contribution
                    break;
                case CLEAN_UP_CONTEXT:
                    ContextCleanupInfo cci = (ContextCleanupInfo)msg.obj;
                    cci.context.performFinalCleanup(cci.who, cci.what);
                    break;
                case GC_WHEN_IDLE:
                    scheduleGcIdler();
                    break;
                case DUMP_SERVICE:
<<<<<<< HEAD
                    handleDumpService((DumpComponentInfo)msg.obj);
                    break;
                case LOW_MEMORY:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "lowMemory");
                    handleLowMemory();
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case ACTIVITY_CONFIGURATION_CHANGED:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityConfigChanged");
                    handleActivityConfigurationChanged((IBinder)msg.obj);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case PROFILER_CONTROL:
                    handleProfilerControl(msg.arg1 != 0, (ProfilerControlData)msg.obj, msg.arg2);
                    break;
                case CREATE_BACKUP_AGENT:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "backupCreateAgent");
                    handleCreateBackupAgent((CreateBackupAgentData)msg.obj);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case DESTROY_BACKUP_AGENT:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "backupDestroyAgent");
                    handleDestroyBackupAgent((CreateBackupAgentData)msg.obj);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case SUICIDE:
                    Process.killProcess(Process.myPid());
                    break;
                case REMOVE_PROVIDER:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "providerRemove");
                    completeRemoveProvider((ProviderRefCount)msg.obj);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case ENABLE_JIT:
                    ensureJitEnabled();
                    break;
                case DISPATCH_PACKAGE_BROADCAST:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "broadcastPackage");
                    handleDispatchPackageBroadcast(msg.arg1, (String[])msg.obj);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case SCHEDULE_CRASH:
                    throw new RemoteServiceException((String)msg.obj);
                case DUMP_HEAP:
                    handleDumpHeap(msg.arg1 != 0, (DumpHeapData)msg.obj);
                    break;
                case DUMP_ACTIVITY:
                    handleDumpActivity((DumpComponentInfo)msg.obj);
                    break;
                case DUMP_PROVIDER:
                    handleDumpProvider((DumpComponentInfo)msg.obj);
                    break;
                case SLEEPING:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "sleeping");
                    handleSleeping((IBinder)msg.obj, msg.arg1 != 0);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case SET_CORE_SETTINGS:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "setCoreSettings");
                    handleSetCoreSettings((Bundle) msg.obj);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case UPDATE_PACKAGE_COMPATIBILITY_INFO:
                    handleUpdatePackageCompatibilityInfo((UpdateCompatibilityData)msg.obj);
                    break;
                case TRIM_MEMORY:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "trimMemory");
                    handleTrimMemory(msg.arg1);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case UNSTABLE_PROVIDER_DIED:
                    handleUnstableProviderDied((IBinder)msg.obj, false);
                    break;
            }
            if (DEBUG_MESSAGES) Slog.v(TAG, "<<< done: " + codeToString(msg.what));
        }

        private void maybeSnapshot() {
            if (mBoundApplication != null && SamplingProfilerIntegration.isEnabled()) {
                // convert the *private* ActivityThread.PackageInfo to *public* known
                // android.content.pm.PackageInfo
                String packageName = mBoundApplication.info.mPackageName;
                android.content.pm.PackageInfo packageInfo = null;
                try {
                    Context context = getSystemContext();
                    if(context == null) {
                        Log.e(TAG, "cannot get a valid context");
                        return;
                    }
                    PackageManager pm = context.getPackageManager();
                    if(pm == null) {
                        Log.e(TAG, "cannot get a valid PackageManager");
                        return;
                    }
                    packageInfo = pm.getPackageInfo(
                            packageName, PackageManager.GET_ACTIVITIES);
                } catch (NameNotFoundException e) {
                    Log.e(TAG, "cannot get package info for " + packageName, e);
                }
                SamplingProfilerIntegration.writeSnapshot(mBoundApplication.processName, packageInfo);
=======
                    handleDumpService((DumpServiceInfo)msg.obj);
                    break;
                case LOW_MEMORY:
                    handleLowMemory();
                    break;
                case ACTIVITY_CONFIGURATION_CHANGED:
                    handleActivityConfigurationChanged((IBinder)msg.obj);
                    break;
>>>>>>> 54b6cfa... Initial Contribution
            }
        }
    }

<<<<<<< HEAD
    private class Idler implements MessageQueue.IdleHandler {
        public final boolean queueIdle() {
            ActivityClientRecord a = mNewActivities;
            boolean stopProfiling = false;
            if (mBoundApplication != null && mProfiler.profileFd != null
                    && mProfiler.autoStopProfiler) {
                stopProfiling = true;
            }
            if (a != null) {
                mNewActivities = null;
                IActivityManager am = ActivityManagerNative.getDefault();
                ActivityClientRecord prev;
                do {
                    if (localLOGV) Slog.v(
                        TAG, "Reporting idle of " + a +
                        " finished=" +
                        (a.activity != null && a.activity.mFinished));
                    if (a.activity != null && !a.activity.mFinished) {
                        try {
                            am.activityIdle(a.token, a.createdConfig, stopProfiling);
                            a.createdConfig = null;
                        } catch (RemoteException ex) {
                            // Ignore
=======
    private final class Idler implements MessageQueue.IdleHandler {
        public final boolean queueIdle() {
            ActivityRecord a = mNewActivities;
            if (a != null) {
                mNewActivities = null;
                IActivityManager am = ActivityManagerNative.getDefault();
                ActivityRecord prev;
                do {
                    if (localLOGV) Log.v(
                        TAG, "Reporting idle of " + a +
                        " finished=" +
                        (a.activity != null ? a.activity.mFinished : false));
                    if (a.activity != null && !a.activity.mFinished) {
                        try {
                            am.activityIdle(a.token);
                        } catch (RemoteException ex) {
>>>>>>> 54b6cfa... Initial Contribution
                        }
                    }
                    prev = a;
                    a = a.nextIdle;
                    prev.nextIdle = null;
                } while (a != null);
            }
<<<<<<< HEAD
            if (stopProfiling) {
                mProfiler.stopProfiling();
            }
            ensureJitEnabled();
=======
>>>>>>> 54b6cfa... Initial Contribution
            return false;
        }
    }

    final class GcIdler implements MessageQueue.IdleHandler {
        public final boolean queueIdle() {
            doGcIfNeeded();
            return false;
        }
    }

<<<<<<< HEAD
    private static class ResourcesKey {
        final private String mResDir;
        final private float mScale;
        final private int mHash;

        ResourcesKey(String resDir, float scale) {
            mResDir = resDir;
            mScale = scale;
            mHash = mResDir.hashCode() << 2 + (int) (mScale * 2);
        }

        @Override
        public int hashCode() {
            return mHash;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ResourcesKey)) {
                return false;
            }
            ResourcesKey peer = (ResourcesKey) obj;
            return mResDir.equals(peer.mResDir) && mScale == peer.mScale;
        }
    }

    public static ActivityThread currentActivityThread() {
        return sThreadLocal.get();
    }

    public static String currentPackageName() {
        ActivityThread am = currentActivityThread();
        return (am != null && am.mBoundApplication != null)
            ? am.mBoundApplication.processName : null;
    }

    public static Application currentApplication() {
        ActivityThread am = currentActivityThread();
        return am != null ? am.mInitialApplication : null;
    }

    public static IPackageManager getPackageManager() {
        if (sPackageManager != null) {
            //Slog.v("PackageManager", "returning cur default = " + sPackageManager);
            return sPackageManager;
        }
        IBinder b = ServiceManager.getService("package");
        //Slog.v("PackageManager", "default service binder = " + b);
        sPackageManager = IPackageManager.Stub.asInterface(b);
        //Slog.v("PackageManager", "default service = " + sPackageManager);
        return sPackageManager;
    }

    DisplayMetrics getDisplayMetricsLocked(CompatibilityInfo ci, boolean forceUpdate) {
        DisplayMetrics dm = mDisplayMetrics.get(ci);
        if (dm != null && !forceUpdate) {
            return dm;
        }
        if (dm == null) {
            dm = new DisplayMetrics();
            mDisplayMetrics.put(ci, dm);
        }
        Display d = WindowManagerImpl.getDefault(ci).getDefaultDisplay();
        d.getMetrics(dm);
        //Slog.i("foo", "New metrics: w=" + metrics.widthPixels + " h="
        //        + metrics.heightPixels + " den=" + metrics.density
        //        + " xdpi=" + metrics.xdpi + " ydpi=" + metrics.ydpi);
        return dm;
    }

    private Configuration mMainThreadConfig = new Configuration();
    Configuration applyConfigCompatMainThread(Configuration config, CompatibilityInfo compat) {
        if (config == null) {
            return null;
        }
        if (compat != null && !compat.supportsScreen()) {
            mMainThreadConfig.setTo(config);
            config = mMainThreadConfig;
            compat.applyToConfiguration(config);
        }
        return config;
    }

    /**
     * Creates the top level Resources for applications with the given compatibility info.
     *
     * @param resDir the resource directory.
     * @param compInfo the compability info. It will use the default compatibility info when it's
     * null.
     */
    Resources getTopLevelResources(String resDir, CompatibilityInfo compInfo) {
        ResourcesKey key = new ResourcesKey(resDir, compInfo.applicationScale);
        Resources r;
        synchronized (mPackages) {
            // Resources is app scale dependent.
            if (false) {
                Slog.w(TAG, "getTopLevelResources: " + resDir + " / "
                        + compInfo.applicationScale);
            }
            WeakReference<Resources> wr = mActiveResources.get(key);
            r = wr != null ? wr.get() : null;
            //if (r != null) Slog.i(TAG, "isUpToDate " + resDir + ": " + r.getAssets().isUpToDate());
            if (r != null && r.getAssets().isUpToDate()) {
                if (false) {
                    Slog.w(TAG, "Returning cached resources " + r + " " + resDir
                            + ": appScale=" + r.getCompatibilityInfo().applicationScale);
                }
                return r;
            }
        }

        //if (r != null) {
        //    Slog.w(TAG, "Throwing away out-of-date resources!!!! "
        //            + r + " " + resDir);
        //}

        AssetManager assets = new AssetManager();
        if (assets.addAssetPath(resDir) == 0) {
            return null;
        }

        //Slog.i(TAG, "Resource: key=" + key + ", display metrics=" + metrics);
        DisplayMetrics metrics = getDisplayMetricsLocked(null, false);
        r = new Resources(assets, metrics, getConfiguration(), compInfo);
        if (false) {
            Slog.i(TAG, "Created app resources " + resDir + " " + r + ": "
                    + r.getConfiguration() + " appScale="
                    + r.getCompatibilityInfo().applicationScale);
        }
        
        synchronized (mPackages) {
            WeakReference<Resources> wr = mActiveResources.get(key);
            Resources existing = wr != null ? wr.get() : null;
            if (existing != null && existing.getAssets().isUpToDate()) {
                // Someone else already created the resources while we were
                // unlocked; go ahead and use theirs.
                r.getAssets().close();
                return existing;
            }
            
            // XXX need to remove entries when weak references go away
            mActiveResources.put(key, new WeakReference<Resources>(r));
            return r;
        }
    }

    /**
     * Creates the top level resources for the given package.
     */
    Resources getTopLevelResources(String resDir, LoadedApk pkgInfo) {
        return getTopLevelResources(resDir, pkgInfo.mCompatibilityInfo.get());
    }

    final Handler getHandler() {
        return mH;
    }

    public final LoadedApk getPackageInfo(String packageName, CompatibilityInfo compatInfo,
            int flags) {
        synchronized (mPackages) {
            WeakReference<LoadedApk> ref;
=======
    static IPackageManager sPackageManager;

    final ApplicationThread mAppThread = new ApplicationThread();
    final Looper mLooper = Looper.myLooper();
    final H mH = new H();
    final HashMap<IBinder, ActivityRecord> mActivities
            = new HashMap<IBinder, ActivityRecord>();
    // List of new activities (via ActivityRecord.nextIdle) that should
    // be reported when next we idle.
    ActivityRecord mNewActivities = null;
    // Number of activities that are currently visible on-screen.
    int mNumVisibleActivities = 0;
    final HashMap<IBinder, Service> mServices
            = new HashMap<IBinder, Service>();
    AppBindData mBoundApplication;
    Configuration mConfiguration;
    Application mInitialApplication;
    final ArrayList<Application> mAllApplications
            = new ArrayList<Application>();
    static final ThreadLocal sThreadLocal = new ThreadLocal();
    Instrumentation mInstrumentation;
    String mInstrumentationAppDir = null;
    String mInstrumentationAppPackage = null;
    String mInstrumentedAppDir = null;
    boolean mSystemThread = false;

    /**
     * Activities that are enqueued to be relaunched.  This list is accessed
     * by multiple threads, so you must synchronize on it when accessing it.
     */
    final ArrayList<ActivityRecord> mRelaunchingActivities
            = new ArrayList<ActivityRecord>();
    Configuration mPendingConfiguration = null;
    
    // These can be accessed by multiple threads; mPackages is the lock.
    // XXX For now we keep around information about all packages we have
    // seen, not removing entries from this map.
    final HashMap<String, WeakReference<PackageInfo>> mPackages
        = new HashMap<String, WeakReference<PackageInfo>>();
    final HashMap<String, WeakReference<PackageInfo>> mResourcePackages
        = new HashMap<String, WeakReference<PackageInfo>>();
    Display mDisplay = null;
    HashMap<String, WeakReference<Resources> > mActiveResources
        = new HashMap<String, WeakReference<Resources> >();

    // The lock of mProviderMap protects the following variables.
    final HashMap<String, ProviderRecord> mProviderMap
        = new HashMap<String, ProviderRecord>();
    final HashMap<IBinder, ProviderRefCount> mProviderRefCountMap
        = new HashMap<IBinder, ProviderRefCount>();
    final HashMap<IBinder, ProviderRecord> mLocalProviders
        = new HashMap<IBinder, ProviderRecord>();

    final GcIdler mGcIdler = new GcIdler();
    boolean mGcIdlerScheduled = false;

    public final PackageInfo getPackageInfo(String packageName, int flags) {
        synchronized (mPackages) {
            WeakReference<PackageInfo> ref;
>>>>>>> 54b6cfa... Initial Contribution
            if ((flags&Context.CONTEXT_INCLUDE_CODE) != 0) {
                ref = mPackages.get(packageName);
            } else {
                ref = mResourcePackages.get(packageName);
            }
<<<<<<< HEAD
            LoadedApk packageInfo = ref != null ? ref.get() : null;
            //Slog.i(TAG, "getPackageInfo " + packageName + ": " + packageInfo);
            //if (packageInfo != null) Slog.i(TAG, "isUptoDate " + packageInfo.mResDir
            //        + ": " + packageInfo.mResources.getAssets().isUpToDate());
=======
            PackageInfo packageInfo = ref != null ? ref.get() : null;
            //Log.i(TAG, "getPackageInfo " + packageName + ": " + packageInfo);
>>>>>>> 54b6cfa... Initial Contribution
            if (packageInfo != null && (packageInfo.mResources == null
                    || packageInfo.mResources.getAssets().isUpToDate())) {
                if (packageInfo.isSecurityViolation()
                        && (flags&Context.CONTEXT_IGNORE_SECURITY) == 0) {
                    throw new SecurityException(
                            "Requesting code from " + packageName
                            + " to be run in process "
                            + mBoundApplication.processName
                            + "/" + mBoundApplication.appInfo.uid);
                }
                return packageInfo;
            }
        }

        ApplicationInfo ai = null;
        try {
            ai = getPackageManager().getApplicationInfo(packageName,
<<<<<<< HEAD
                    PackageManager.GET_SHARED_LIBRARY_FILES, UserId.myUserId());
        } catch (RemoteException e) {
            // Ignore
        }

        if (ai != null) {
            return getPackageInfo(ai, compatInfo, flags);
=======
                    PackageManager.GET_SHARED_LIBRARY_FILES);
        } catch (RemoteException e) {
        }

        if (ai != null) {
            return getPackageInfo(ai, flags);
>>>>>>> 54b6cfa... Initial Contribution
        }

        return null;
    }

<<<<<<< HEAD
    public final LoadedApk getPackageInfo(ApplicationInfo ai, CompatibilityInfo compatInfo,
            int flags) {
        boolean includeCode = (flags&Context.CONTEXT_INCLUDE_CODE) != 0;
        boolean securityViolation = includeCode && ai.uid != 0
                && ai.uid != Process.SYSTEM_UID && (mBoundApplication != null
                        ? !UserId.isSameApp(ai.uid, mBoundApplication.appInfo.uid)
                        : true);
=======
    public final PackageInfo getPackageInfo(ApplicationInfo ai, int flags) {
        boolean includeCode = (flags&Context.CONTEXT_INCLUDE_CODE) != 0;
        boolean securityViolation = includeCode && ai.uid != 0
                && ai.uid != Process.SYSTEM_UID && (mBoundApplication != null
                        ? ai.uid != mBoundApplication.appInfo.uid : true);
>>>>>>> 54b6cfa... Initial Contribution
        if ((flags&(Context.CONTEXT_INCLUDE_CODE
                |Context.CONTEXT_IGNORE_SECURITY))
                == Context.CONTEXT_INCLUDE_CODE) {
            if (securityViolation) {
                String msg = "Requesting code from " + ai.packageName
                        + " (with uid " + ai.uid + ")";
                if (mBoundApplication != null) {
                    msg = msg + " to be run in process "
                        + mBoundApplication.processName + " (with uid "
                        + mBoundApplication.appInfo.uid + ")";
                }
                throw new SecurityException(msg);
            }
        }
<<<<<<< HEAD
        return getPackageInfo(ai, compatInfo, null, securityViolation, includeCode);
    }

    public final LoadedApk getPackageInfoNoCheck(ApplicationInfo ai,
            CompatibilityInfo compatInfo) {
        return getPackageInfo(ai, compatInfo, null, false, true);
    }

    public final LoadedApk peekPackageInfo(String packageName, boolean includeCode) {
        synchronized (mPackages) {
            WeakReference<LoadedApk> ref;
            if (includeCode) {
                ref = mPackages.get(packageName);
            } else {
                ref = mResourcePackages.get(packageName);
            }
            return ref != null ? ref.get() : null;
        }
    }

    private LoadedApk getPackageInfo(ApplicationInfo aInfo, CompatibilityInfo compatInfo,
            ClassLoader baseLoader, boolean securityViolation, boolean includeCode) {
        synchronized (mPackages) {
            WeakReference<LoadedApk> ref;
=======
        return getPackageInfo(ai, null, securityViolation, includeCode);
    }

    public final PackageInfo getPackageInfoNoCheck(ApplicationInfo ai) {
        return getPackageInfo(ai, null, false, true);
    }

    private final PackageInfo getPackageInfo(ApplicationInfo aInfo,
            ClassLoader baseLoader, boolean securityViolation, boolean includeCode) {
        synchronized (mPackages) {
            WeakReference<PackageInfo> ref;
>>>>>>> 54b6cfa... Initial Contribution
            if (includeCode) {
                ref = mPackages.get(aInfo.packageName);
            } else {
                ref = mResourcePackages.get(aInfo.packageName);
            }
<<<<<<< HEAD
            LoadedApk packageInfo = ref != null ? ref.get() : null;
            if (packageInfo == null || (packageInfo.mResources != null
                    && !packageInfo.mResources.getAssets().isUpToDate())) {
                if (localLOGV) Slog.v(TAG, (includeCode ? "Loading code package "
=======
            PackageInfo packageInfo = ref != null ? ref.get() : null;
            if (packageInfo == null || (packageInfo.mResources != null
                    && !packageInfo.mResources.getAssets().isUpToDate())) {
                if (localLOGV) Log.v(TAG, (includeCode ? "Loading code package "
>>>>>>> 54b6cfa... Initial Contribution
                        : "Loading resource-only package ") + aInfo.packageName
                        + " (in " + (mBoundApplication != null
                                ? mBoundApplication.processName : null)
                        + ")");
                packageInfo =
<<<<<<< HEAD
                    new LoadedApk(this, aInfo, compatInfo, this, baseLoader,
=======
                    new PackageInfo(this, aInfo, this, baseLoader,
>>>>>>> 54b6cfa... Initial Contribution
                            securityViolation, includeCode &&
                            (aInfo.flags&ApplicationInfo.FLAG_HAS_CODE) != 0);
                if (includeCode) {
                    mPackages.put(aInfo.packageName,
<<<<<<< HEAD
                            new WeakReference<LoadedApk>(packageInfo));
                } else {
                    mResourcePackages.put(aInfo.packageName,
                            new WeakReference<LoadedApk>(packageInfo));
=======
                            new WeakReference<PackageInfo>(packageInfo));
                } else {
                    mResourcePackages.put(aInfo.packageName,
                            new WeakReference<PackageInfo>(packageInfo));
>>>>>>> 54b6cfa... Initial Contribution
                }
            }
            return packageInfo;
        }
    }

<<<<<<< HEAD
=======
    public final boolean hasPackageInfo(String packageName) {
        synchronized (mPackages) {
            WeakReference<PackageInfo> ref;
            ref = mPackages.get(packageName);
            if (ref != null && ref.get() != null) {
                return true;
            }
            ref = mResourcePackages.get(packageName);
            if (ref != null && ref.get() != null) {
                return true;
            }
            return false;
        }
    }
    
>>>>>>> 54b6cfa... Initial Contribution
    ActivityThread() {
    }

    public ApplicationThread getApplicationThread()
    {
        return mAppThread;
    }

    public Instrumentation getInstrumentation()
    {
        return mInstrumentation;
    }

    public Configuration getConfiguration() {
<<<<<<< HEAD
        return mResConfiguration;
    }

    public boolean isProfiling() {
        return mProfiler != null && mProfiler.profileFile != null
                && mProfiler.profileFd == null;
    }

    public String getProfileFilePath() {
        return mProfiler.profileFile;
=======
        return mConfiguration;
    }

    public boolean isProfiling() {
        return mBoundApplication != null && mBoundApplication.profileFile != null;
    }

    public String getProfileFilePath() {
        return mBoundApplication.profileFile;
>>>>>>> 54b6cfa... Initial Contribution
    }

    public Looper getLooper() {
        return mLooper;
    }

    public Application getApplication() {
        return mInitialApplication;
    }
<<<<<<< HEAD

    public String getProcessName() {
        return mBoundApplication.processName;
    }

    public ContextImpl getSystemContext() {
        synchronized (this) {
            if (mSystemContext == null) {
                ContextImpl context =
                    ContextImpl.createSystemContext(this);
                LoadedApk info = new LoadedApk(this, "android", context, null,
                        CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO);
                context.init(info, null, this);
                context.getResources().updateConfiguration(
                        getConfiguration(), getDisplayMetricsLocked(
                                CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO, false));
                mSystemContext = context;
                //Slog.i(TAG, "Created system resources " + context.getResources()
=======
    
    public ApplicationContext getSystemContext() {
        synchronized (this) {
            if (mSystemContext == null) {
                ApplicationContext context =
                    ApplicationContext.createSystemContext(this);
                PackageInfo info = new PackageInfo(this, "android", context);
                context.init(info, null, this);
                context.getResources().updateConfiguration(
                        getConfiguration(), getDisplayMetricsLocked());
                mSystemContext = context;
                //Log.i(TAG, "Created system resources " + context.getResources()
>>>>>>> 54b6cfa... Initial Contribution
                //        + ": " + context.getResources().getConfiguration());
            }
        }
        return mSystemContext;
    }

<<<<<<< HEAD
    public void installSystemApplicationInfo(ApplicationInfo info) {
        synchronized (this) {
            ContextImpl context = getSystemContext();
            context.init(new LoadedApk(this, "android", context, info,
                    CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO), null, this);

            // give ourselves a default profiler
            mProfiler = new Profiler();
        }
    }

    void ensureJitEnabled() {
        if (!mJitEnabled) {
            mJitEnabled = true;
            dalvik.system.VMRuntime.getRuntime().startJitCompilation();
        }
    }
    
=======
>>>>>>> 54b6cfa... Initial Contribution
    void scheduleGcIdler() {
        if (!mGcIdlerScheduled) {
            mGcIdlerScheduled = true;
            Looper.myQueue().addIdleHandler(mGcIdler);
        }
        mH.removeMessages(H.GC_WHEN_IDLE);
    }

    void unscheduleGcIdler() {
        if (mGcIdlerScheduled) {
            mGcIdlerScheduled = false;
            Looper.myQueue().removeIdleHandler(mGcIdler);
        }
        mH.removeMessages(H.GC_WHEN_IDLE);
    }

    void doGcIfNeeded() {
        mGcIdlerScheduled = false;
        final long now = SystemClock.uptimeMillis();
<<<<<<< HEAD
        //Slog.i(TAG, "**** WE MIGHT WANT TO GC: then=" + Binder.getLastGcTime()
        //        + "m now=" + now);
        if ((BinderInternal.getLastGcTime()+MIN_TIME_BETWEEN_GCS) < now) {
            //Slog.i(TAG, "**** WE DO, WE DO WANT TO GC!");
=======
        //Log.i(TAG, "**** WE MIGHT WANT TO GC: then=" + Binder.getLastGcTime()
        //        + "m now=" + now);
        if ((BinderInternal.getLastGcTime()+MIN_TIME_BETWEEN_GCS) < now) {
            //Log.i(TAG, "**** WE DO, WE DO WANT TO GC!");
>>>>>>> 54b6cfa... Initial Contribution
            BinderInternal.forceGc("bg");
        }
    }

<<<<<<< HEAD
    public void registerOnActivityPausedListener(Activity activity,
            OnActivityPausedListener listener) {
        synchronized (mOnPauseListeners) {
            ArrayList<OnActivityPausedListener> list = mOnPauseListeners.get(activity);
            if (list == null) {
                list = new ArrayList<OnActivityPausedListener>();
                mOnPauseListeners.put(activity, list);
            }
            list.add(listener);
        }
    }

    public void unregisterOnActivityPausedListener(Activity activity,
            OnActivityPausedListener listener) {
        synchronized (mOnPauseListeners) {
            ArrayList<OnActivityPausedListener> list = mOnPauseListeners.get(activity);
            if (list != null) {
                list.remove(listener);
            }
        }
    }

=======
>>>>>>> 54b6cfa... Initial Contribution
    public final ActivityInfo resolveActivityInfo(Intent intent) {
        ActivityInfo aInfo = intent.resolveActivityInfo(
                mInitialApplication.getPackageManager(), PackageManager.GET_SHARED_LIBRARY_FILES);
        if (aInfo == null) {
            // Throw an exception.
            Instrumentation.checkStartActivityResult(
<<<<<<< HEAD
                    ActivityManager.START_CLASS_NOT_FOUND, intent);
        }
        return aInfo;
    }

    public final Activity startActivityNow(Activity parent, String id,
        Intent intent, ActivityInfo activityInfo, IBinder token, Bundle state,
        Activity.NonConfigurationInstances lastNonConfigurationInstances) {
        ActivityClientRecord r = new ActivityClientRecord();
            r.token = token;
            r.ident = 0;
=======
                    IActivityManager.START_CLASS_NOT_FOUND, intent);
        }
        return aInfo;
    }
    
    public final Activity startActivityNow(Activity parent, String id,
            Intent intent, IBinder token, Bundle state) {
        ActivityInfo aInfo = resolveActivityInfo(intent);
        return startActivityNow(parent, id, intent, aInfo, token, state);
    }
    
    public final Activity startActivityNow(Activity parent, String id,
            Intent intent, ActivityInfo activityInfo, IBinder token, Bundle state) {
        ActivityRecord r = new ActivityRecord();
            r.token = token;
>>>>>>> 54b6cfa... Initial Contribution
            r.intent = intent;
            r.state = state;
            r.parent = parent;
            r.embeddedID = id;
            r.activityInfo = activityInfo;
<<<<<<< HEAD
            r.lastNonConfigurationInstances = lastNonConfigurationInstances;
=======
>>>>>>> 54b6cfa... Initial Contribution
        if (localLOGV) {
            ComponentName compname = intent.getComponent();
            String name;
            if (compname != null) {
                name = compname.toShortString();
            } else {
                name = "(Intent " + intent + ").getComponent() returned null";
            }
<<<<<<< HEAD
            Slog.v(TAG, "Performing launch: action=" + intent.getAction()
                    + ", comp=" + name
                    + ", token=" + token);
        }
        return performLaunchActivity(r, null);
=======
            Log.v(TAG, "Performing launch: action=" + intent.getAction()
                    + ", comp=" + name
                    + ", token=" + token);
        }
        return performLaunchActivity(r);
>>>>>>> 54b6cfa... Initial Contribution
    }

    public final Activity getActivity(IBinder token) {
        return mActivities.get(token).activity;
    }

    public final void sendActivityResult(
            IBinder token, String id, int requestCode,
            int resultCode, Intent data) {
<<<<<<< HEAD
        if (DEBUG_RESULTS) Slog.v(TAG, "sendActivityResult: id=" + id
                + " req=" + requestCode + " res=" + resultCode + " data=" + data);
=======
>>>>>>> 54b6cfa... Initial Contribution
        ArrayList<ResultInfo> list = new ArrayList<ResultInfo>();
        list.add(new ResultInfo(id, requestCode, resultCode, data));
        mAppThread.scheduleSendResult(token, list);
    }

    // if the thread hasn't started yet, we don't have the handler, so just
    // save the messages until we're ready.
<<<<<<< HEAD
    private void queueOrSendMessage(int what, Object obj) {
        queueOrSendMessage(what, obj, 0, 0);
    }

    private void queueOrSendMessage(int what, Object obj, int arg1) {
        queueOrSendMessage(what, obj, arg1, 0);
    }

    private void queueOrSendMessage(int what, Object obj, int arg1, int arg2) {
        synchronized (this) {
            if (DEBUG_MESSAGES) Slog.v(
=======
    private final void queueOrSendMessage(int what, Object obj) {
        queueOrSendMessage(what, obj, 0, 0);
    }

    private final void queueOrSendMessage(int what, Object obj, int arg1) {
        queueOrSendMessage(what, obj, arg1, 0);
    }

    private final void queueOrSendMessage(int what, Object obj, int arg1, int arg2) {
        synchronized (this) {
            if (localLOGV) Log.v(
>>>>>>> 54b6cfa... Initial Contribution
                TAG, "SCHEDULE " + what + " " + mH.codeToString(what)
                + ": " + arg1 + " / " + obj);
            Message msg = Message.obtain();
            msg.what = what;
            msg.obj = obj;
            msg.arg1 = arg1;
            msg.arg2 = arg2;
            mH.sendMessage(msg);
        }
    }

<<<<<<< HEAD
    final void scheduleContextCleanup(ContextImpl context, String who,
=======
    final void scheduleContextCleanup(ApplicationContext context, String who,
>>>>>>> 54b6cfa... Initial Contribution
            String what) {
        ContextCleanupInfo cci = new ContextCleanupInfo();
        cci.context = context;
        cci.who = who;
        cci.what = what;
        queueOrSendMessage(H.CLEAN_UP_CONTEXT, cci);
    }

<<<<<<< HEAD
    private Activity performLaunchActivity(ActivityClientRecord r, Intent customIntent) {
=======
    private final Activity performLaunchActivity(ActivityRecord r) {
>>>>>>> 54b6cfa... Initial Contribution
        // System.out.println("##### [" + System.currentTimeMillis() + "] ActivityThread.performLaunchActivity(" + r + ")");

        ActivityInfo aInfo = r.activityInfo;
        if (r.packageInfo == null) {
<<<<<<< HEAD
            r.packageInfo = getPackageInfo(aInfo.applicationInfo, r.compatInfo,
                    Context.CONTEXT_INCLUDE_CODE);
        }

=======
            r.packageInfo = getPackageInfo(aInfo.applicationInfo,
                    Context.CONTEXT_INCLUDE_CODE);
        }
            
>>>>>>> 54b6cfa... Initial Contribution
        ComponentName component = r.intent.getComponent();
        if (component == null) {
            component = r.intent.resolveActivity(
                mInitialApplication.getPackageManager());
            r.intent.setComponent(component);
        }

        if (r.activityInfo.targetActivity != null) {
            component = new ComponentName(r.activityInfo.packageName,
                    r.activityInfo.targetActivity);
        }

        Activity activity = null;
        try {
            java.lang.ClassLoader cl = r.packageInfo.getClassLoader();
            activity = mInstrumentation.newActivity(
                    cl, component.getClassName(), r.intent);
<<<<<<< HEAD
            StrictMode.incrementExpectedActivityCount(activity.getClass());
=======
>>>>>>> 54b6cfa... Initial Contribution
            r.intent.setExtrasClassLoader(cl);
            if (r.state != null) {
                r.state.setClassLoader(cl);
            }
        } catch (Exception e) {
            if (!mInstrumentation.onException(activity, e)) {
                throw new RuntimeException(
                    "Unable to instantiate activity " + component
                    + ": " + e.toString(), e);
            }
        }

        try {
<<<<<<< HEAD
            Application app = r.packageInfo.makeApplication(false, mInstrumentation);

            if (localLOGV) Slog.v(TAG, "Performing launch of " + r);
            if (localLOGV) Slog.v(
=======
            Application app = r.packageInfo.makeApplication();
            
            if (localLOGV) Log.v(TAG, "Performing launch of " + r);
            if (localLOGV) Log.v(
>>>>>>> 54b6cfa... Initial Contribution
                    TAG, r + ": app=" + app
                    + ", appName=" + app.getPackageName()
                    + ", pkg=" + r.packageInfo.getPackageName()
                    + ", comp=" + r.intent.getComponent().toShortString()
                    + ", dir=" + r.packageInfo.getAppDir());

            if (activity != null) {
<<<<<<< HEAD
                ContextImpl appContext = new ContextImpl();
                appContext.init(r.packageInfo, r.token, this);
                appContext.setOuterContext(activity);
                CharSequence title = r.activityInfo.loadLabel(appContext.getPackageManager());
                Configuration config = new Configuration(mCompatConfiguration);
                if (DEBUG_CONFIGURATION) Slog.v(TAG, "Launching activity "
                        + r.activityInfo.name + " with config " + config);
                activity.attach(appContext, this, getInstrumentation(), r.token,
                        r.ident, app, r.intent, r.activityInfo, title, r.parent,
                        r.embeddedID, r.lastNonConfigurationInstances, config);

                if (customIntent != null) {
                    activity.mIntent = customIntent;
                }
                r.lastNonConfigurationInstances = null;
=======
                ApplicationContext appContext = new ApplicationContext();
                appContext.init(r.packageInfo, r.token, this);
                appContext.setOuterContext(activity);
                CharSequence title = r.activityInfo.loadLabel(appContext.getPackageManager());
                Configuration config = new Configuration(mConfiguration);
                activity.attach(appContext, this, getInstrumentation(), r.token, app, 
                        r.intent, r.activityInfo, title, r.parent, r.embeddedID,
                        r.lastNonConfigurationInstance, config);
                
                r.lastNonConfigurationInstance = null;
>>>>>>> 54b6cfa... Initial Contribution
                activity.mStartedActivity = false;
                int theme = r.activityInfo.getThemeResource();
                if (theme != 0) {
                    activity.setTheme(theme);
                }

                activity.mCalled = false;
                mInstrumentation.callActivityOnCreate(activity, r.state);
                if (!activity.mCalled) {
                    throw new SuperNotCalledException(
                        "Activity " + r.intent.getComponent().toShortString() +
                        " did not call through to super.onCreate()");
                }
                r.activity = activity;
                r.stopped = true;
                if (!r.activity.mFinished) {
                    activity.performStart();
                    r.stopped = false;
                }
                if (!r.activity.mFinished) {
                    if (r.state != null) {
                        mInstrumentation.callActivityOnRestoreInstanceState(activity, r.state);
                    }
                }
                if (!r.activity.mFinished) {
                    activity.mCalled = false;
                    mInstrumentation.callActivityOnPostCreate(activity, r.state);
                    if (!activity.mCalled) {
                        throw new SuperNotCalledException(
                            "Activity " + r.intent.getComponent().toShortString() +
                            " did not call through to super.onPostCreate()");
                    }
                }
<<<<<<< HEAD
=======
                r.state = null;
>>>>>>> 54b6cfa... Initial Contribution
            }
            r.paused = true;

            mActivities.put(r.token, r);

        } catch (SuperNotCalledException e) {
            throw e;

        } catch (Exception e) {
            if (!mInstrumentation.onException(activity, e)) {
                throw new RuntimeException(
                    "Unable to start activity " + component
                    + ": " + e.toString(), e);
            }
        }

        return activity;
    }

<<<<<<< HEAD
    private void handleLaunchActivity(ActivityClientRecord r, Intent customIntent) {
=======
    private final void handleLaunchActivity(ActivityRecord r) {
>>>>>>> 54b6cfa... Initial Contribution
        // If we are getting ready to gc after going to the background, well
        // we are back active so skip it.
        unscheduleGcIdler();

<<<<<<< HEAD
        if (r.profileFd != null) {
            mProfiler.setProfiler(r.profileFile, r.profileFd);
            mProfiler.startProfiling();
            mProfiler.autoStopProfiler = r.autoStopProfiler;
        }

        // Make sure we are running with the most recent config.
        handleConfigurationChanged(null, null);

        if (localLOGV) Slog.v(
            TAG, "Handling launch of " + r);
        Activity a = performLaunchActivity(r, customIntent);

        if (a != null) {
            r.createdConfig = new Configuration(mConfiguration);
            Bundle oldState = r.state;
            handleResumeActivity(r.token, false, r.isForward);
=======
        if (localLOGV) Log.v(
            TAG, "Handling launch of " + r);
        Activity a = performLaunchActivity(r);

        if (a != null) {
            handleResumeActivity(r.token, false);
>>>>>>> 54b6cfa... Initial Contribution

            if (!r.activity.mFinished && r.startsNotResumed) {
                // The activity manager actually wants this one to start out
                // paused, because it needs to be visible but isn't in the
                // foreground.  We accomplish this by going through the
                // normal startup (because activities expect to go through
                // onResume() the first time they run, before their window
                // is displayed), and then pausing it.  However, in this case
                // we do -not- need to do the full pause cycle (of freezing
                // and such) because the activity manager assumes it can just
                // retain the current state it has.
                try {
                    r.activity.mCalled = false;
                    mInstrumentation.callActivityOnPause(r.activity);
<<<<<<< HEAD
                    // We need to keep around the original state, in case
                    // we need to be created again.  But we only do this
                    // for pre-Honeycomb apps, which always save their state
                    // when pausing, so we can not have them save their state
                    // when restarting from a paused state.  For HC and later,
                    // we want to (and can) let the state be saved as the normal
                    // part of stopping the activity.
                    if (r.isPreHoneycomb()) {
                        r.state = oldState;
                    }
=======
>>>>>>> 54b6cfa... Initial Contribution
                    if (!r.activity.mCalled) {
                        throw new SuperNotCalledException(
                            "Activity " + r.intent.getComponent().toShortString() +
                            " did not call through to super.onPause()");
                    }

                } catch (SuperNotCalledException e) {
                    throw e;

                } catch (Exception e) {
                    if (!mInstrumentation.onException(r.activity, e)) {
                        throw new RuntimeException(
                                "Unable to pause activity "
                                + r.intent.getComponent().toShortString()
                                + ": " + e.toString(), e);
                    }
                }
                r.paused = true;
            }
        } else {
            // If there was an error, for any reason, tell the activity
            // manager to stop us.
            try {
                ActivityManagerNative.getDefault()
                    .finishActivity(r.token, Activity.RESULT_CANCELED, null);
            } catch (RemoteException ex) {
<<<<<<< HEAD
                // Ignore
=======
>>>>>>> 54b6cfa... Initial Contribution
            }
        }
    }

<<<<<<< HEAD
    private void deliverNewIntents(ActivityClientRecord r,
=======
    private final void deliverNewIntents(ActivityRecord r,
>>>>>>> 54b6cfa... Initial Contribution
            List<Intent> intents) {
        final int N = intents.size();
        for (int i=0; i<N; i++) {
            Intent intent = intents.get(i);
            intent.setExtrasClassLoader(r.activity.getClassLoader());
<<<<<<< HEAD
            r.activity.mFragments.noteStateNotSaved();
=======
>>>>>>> 54b6cfa... Initial Contribution
            mInstrumentation.callActivityOnNewIntent(r.activity, intent);
        }
    }

    public final void performNewIntents(IBinder token,
            List<Intent> intents) {
<<<<<<< HEAD
        ActivityClientRecord r = mActivities.get(token);
        if (r != null) {
            final boolean resumed = !r.paused;
            if (resumed) {
                r.activity.mTemporaryPause = true;
=======
        ActivityRecord r = mActivities.get(token);
        if (r != null) {
            final boolean resumed = !r.paused;
            if (resumed) {
>>>>>>> 54b6cfa... Initial Contribution
                mInstrumentation.callActivityOnPause(r.activity);
            }
            deliverNewIntents(r, intents);
            if (resumed) {
<<<<<<< HEAD
                r.activity.performResume();
                r.activity.mTemporaryPause = false;
            }
        }
    }

    private void handleNewIntent(NewIntentData data) {
        performNewIntents(data.token, data.intents);
    }

    private static final ThreadLocal<Intent> sCurrentBroadcastIntent = new ThreadLocal<Intent>();

    /**
     * Return the Intent that's currently being handled by a
     * BroadcastReceiver on this thread, or null if none.
     * @hide
     */
    public static Intent getIntentBeingBroadcast() {
        return sCurrentBroadcastIntent.get();
    }

    private void handleReceiver(ReceiverData data) {
=======
                mInstrumentation.callActivityOnResume(r.activity);
            }
        }
    }
    
    private final void handleNewIntent(NewIntentData data) {
        performNewIntents(data.token, data.intents);
    }

    private final void handleReceiver(ReceiverData data) {
>>>>>>> 54b6cfa... Initial Contribution
        // If we are getting ready to gc after going to the background, well
        // we are back active so skip it.
        unscheduleGcIdler();

        String component = data.intent.getComponent().getClassName();

<<<<<<< HEAD
        LoadedApk packageInfo = getPackageInfoNoCheck(
                data.info.applicationInfo, data.compatInfo);

        IActivityManager mgr = ActivityManagerNative.getDefault();

        BroadcastReceiver receiver;
        try {
            java.lang.ClassLoader cl = packageInfo.getClassLoader();
            data.intent.setExtrasClassLoader(cl);
            data.setExtrasClassLoader(cl);
            receiver = (BroadcastReceiver)cl.loadClass(component).newInstance();
        } catch (Exception e) {
            if (DEBUG_BROADCAST) Slog.i(TAG,
                    "Finishing failed broadcast to " + data.intent.getComponent());
            data.sendFinished(mgr);
=======
        PackageInfo packageInfo = getPackageInfoNoCheck(
                data.info.applicationInfo);

        IActivityManager mgr = ActivityManagerNative.getDefault();

        BroadcastReceiver receiver = null;
        try {
            java.lang.ClassLoader cl = packageInfo.getClassLoader();
            data.intent.setExtrasClassLoader(cl);
            if (data.resultExtras != null) {
                data.resultExtras.setClassLoader(cl);
            }
            receiver = (BroadcastReceiver)cl.loadClass(component).newInstance();
        } catch (Exception e) {
            try {
                mgr.finishReceiver(mAppThread.asBinder(), data.resultCode,
                                   data.resultData, data.resultExtras, data.resultAbort);
            } catch (RemoteException ex) {
            }
>>>>>>> 54b6cfa... Initial Contribution
            throw new RuntimeException(
                "Unable to instantiate receiver " + component
                + ": " + e.toString(), e);
        }

        try {
<<<<<<< HEAD
            Application app = packageInfo.makeApplication(false, mInstrumentation);

            if (localLOGV) Slog.v(
=======
            Application app = packageInfo.makeApplication();
            
            if (localLOGV) Log.v(
>>>>>>> 54b6cfa... Initial Contribution
                TAG, "Performing receive of " + data.intent
                + ": app=" + app
                + ", appName=" + app.getPackageName()
                + ", pkg=" + packageInfo.getPackageName()
                + ", comp=" + data.intent.getComponent().toShortString()
                + ", dir=" + packageInfo.getAppDir());

<<<<<<< HEAD
            ContextImpl context = (ContextImpl)app.getBaseContext();
            sCurrentBroadcastIntent.set(data.intent);
            receiver.setPendingResult(data);
            receiver.onReceive(context.getReceiverRestrictedContext(),
                    data.intent);
        } catch (Exception e) {
            if (DEBUG_BROADCAST) Slog.i(TAG,
                    "Finishing failed broadcast to " + data.intent.getComponent());
            data.sendFinished(mgr);
=======
            ApplicationContext context = (ApplicationContext)app.getBaseContext();
            receiver.setOrderedHint(true);
            receiver.setResult(data.resultCode, data.resultData,
                data.resultExtras);
            receiver.setOrderedHint(data.sync);
            receiver.onReceive(context.getReceiverRestrictedContext(),
                    data.intent);
        } catch (Exception e) {
            try {
                mgr.finishReceiver(mAppThread.asBinder(), data.resultCode,
                    data.resultData, data.resultExtras, data.resultAbort);
            } catch (RemoteException ex) {
            }
>>>>>>> 54b6cfa... Initial Contribution
            if (!mInstrumentation.onException(receiver, e)) {
                throw new RuntimeException(
                    "Unable to start receiver " + component
                    + ": " + e.toString(), e);
            }
<<<<<<< HEAD
        } finally {
            sCurrentBroadcastIntent.set(null);
        }

        if (receiver.getPendingResult() != null) {
            data.finish();
        }
    }

    // Instantiate a BackupAgent and tell it that it's alive
    private void handleCreateBackupAgent(CreateBackupAgentData data) {
        if (DEBUG_BACKUP) Slog.v(TAG, "handleCreateBackupAgent: " + data);

        // no longer idle; we have backup work to do
        unscheduleGcIdler();

        // instantiate the BackupAgent class named in the manifest
        LoadedApk packageInfo = getPackageInfoNoCheck(data.appInfo, data.compatInfo);
        String packageName = packageInfo.mPackageName;
        if (mBackupAgents.get(packageName) != null) {
            Slog.d(TAG, "BackupAgent " + "  for " + packageName
                    + " already exists");
            return;
        }

        BackupAgent agent = null;
        String classname = data.appInfo.backupAgentName;

        // full backup operation but no app-supplied agent?  use the default implementation
        if (classname == null && (data.backupMode == IApplicationThread.BACKUP_MODE_FULL
                || data.backupMode == IApplicationThread.BACKUP_MODE_RESTORE_FULL)) {
            classname = "android.app.backup.FullBackupAgent";
        }

        try {
            IBinder binder = null;
            try {
                if (DEBUG_BACKUP) Slog.v(TAG, "Initializing agent class " + classname);

                java.lang.ClassLoader cl = packageInfo.getClassLoader();
                agent = (BackupAgent) cl.loadClass(classname).newInstance();

                // set up the agent's context
                ContextImpl context = new ContextImpl();
                context.init(packageInfo, null, this);
                context.setOuterContext(agent);
                agent.attach(context);

                agent.onCreate();
                binder = agent.onBind();
                mBackupAgents.put(packageName, agent);
            } catch (Exception e) {
                // If this is during restore, fail silently; otherwise go
                // ahead and let the user see the crash.
                Slog.e(TAG, "Agent threw during creation: " + e);
                if (data.backupMode != IApplicationThread.BACKUP_MODE_RESTORE
                        && data.backupMode != IApplicationThread.BACKUP_MODE_RESTORE_FULL) {
                    throw e;
                }
                // falling through with 'binder' still null
            }

            // tell the OS that we're live now
            try {
                ActivityManagerNative.getDefault().backupAgentCreated(packageName, binder);
            } catch (RemoteException e) {
                // nothing to do.
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to create BackupAgent "
                    + classname + ": " + e.toString(), e);
        }
    }

    // Tear down a BackupAgent
    private void handleDestroyBackupAgent(CreateBackupAgentData data) {
        if (DEBUG_BACKUP) Slog.v(TAG, "handleDestroyBackupAgent: " + data);

        LoadedApk packageInfo = getPackageInfoNoCheck(data.appInfo, data.compatInfo);
        String packageName = packageInfo.mPackageName;
        BackupAgent agent = mBackupAgents.get(packageName);
        if (agent != null) {
            try {
                agent.onDestroy();
            } catch (Exception e) {
                Slog.w(TAG, "Exception thrown in onDestroy by backup agent of " + data.appInfo);
                e.printStackTrace();
            }
            mBackupAgents.remove(packageName);
        } else {
            Slog.w(TAG, "Attempt to destroy unknown backup agent " + data);
        }
    }

    private void handleCreateService(CreateServiceData data) {
=======
        }

        try {
            if (data.sync) {
                mgr.finishReceiver(
                    mAppThread.asBinder(), receiver.getResultCode(),
                    receiver.getResultData(), receiver.getResultExtras(false),
                        receiver.getAbortBroadcast());
            } else {
                mgr.finishReceiver(mAppThread.asBinder(), 0, null, null, false);
            }
        } catch (RemoteException ex) {
        }
    }

    private final void handleCreateService(CreateServiceData data) {
>>>>>>> 54b6cfa... Initial Contribution
        // If we are getting ready to gc after going to the background, well
        // we are back active so skip it.
        unscheduleGcIdler();

<<<<<<< HEAD
        LoadedApk packageInfo = getPackageInfoNoCheck(
                data.info.applicationInfo, data.compatInfo);
=======
        PackageInfo packageInfo = getPackageInfoNoCheck(
                data.info.applicationInfo);
>>>>>>> 54b6cfa... Initial Contribution
        Service service = null;
        try {
            java.lang.ClassLoader cl = packageInfo.getClassLoader();
            service = (Service) cl.loadClass(data.info.name).newInstance();
        } catch (Exception e) {
            if (!mInstrumentation.onException(service, e)) {
                throw new RuntimeException(
                    "Unable to instantiate service " + data.info.name
                    + ": " + e.toString(), e);
            }
        }

        try {
<<<<<<< HEAD
            if (localLOGV) Slog.v(TAG, "Creating service " + data.info.name);

            ContextImpl context = new ContextImpl();
            context.init(packageInfo, null, this);

            Application app = packageInfo.makeApplication(false, mInstrumentation);
=======
            if (localLOGV) Log.v(TAG, "Creating service " + data.info.name);

            ApplicationContext context = new ApplicationContext();
            context.init(packageInfo, null, this);

            Application app = packageInfo.makeApplication();
>>>>>>> 54b6cfa... Initial Contribution
            context.setOuterContext(service);
            service.attach(context, this, data.info.name, data.token, app,
                    ActivityManagerNative.getDefault());
            service.onCreate();
            mServices.put(data.token, service);
            try {
<<<<<<< HEAD
                ActivityManagerNative.getDefault().serviceDoneExecuting(
                        data.token, 0, 0, 0);
=======
                ActivityManagerNative.getDefault().serviceDoneExecuting(data.token);
>>>>>>> 54b6cfa... Initial Contribution
            } catch (RemoteException e) {
                // nothing to do.
            }
        } catch (Exception e) {
            if (!mInstrumentation.onException(service, e)) {
                throw new RuntimeException(
                    "Unable to create service " + data.info.name
                    + ": " + e.toString(), e);
            }
        }
    }

<<<<<<< HEAD
    private void handleBindService(BindServiceData data) {
        Service s = mServices.get(data.token);
        if (DEBUG_SERVICE)
            Slog.v(TAG, "handleBindService s=" + s + " rebind=" + data.rebind);
=======
    private final void handleBindService(BindServiceData data) {
        Service s = mServices.get(data.token);
>>>>>>> 54b6cfa... Initial Contribution
        if (s != null) {
            try {
                data.intent.setExtrasClassLoader(s.getClassLoader());
                try {
                    if (!data.rebind) {
                        IBinder binder = s.onBind(data.intent);
                        ActivityManagerNative.getDefault().publishService(
                                data.token, data.intent, binder);
                    } else {
                        s.onRebind(data.intent);
                        ActivityManagerNative.getDefault().serviceDoneExecuting(
<<<<<<< HEAD
                                data.token, 0, 0, 0);
                    }
                    ensureJitEnabled();
=======
                                data.token);
                    }
>>>>>>> 54b6cfa... Initial Contribution
                } catch (RemoteException ex) {
                }
            } catch (Exception e) {
                if (!mInstrumentation.onException(s, e)) {
                    throw new RuntimeException(
                            "Unable to bind to service " + s
                            + " with " + data.intent + ": " + e.toString(), e);
                }
            }
        }
    }

<<<<<<< HEAD
    private void handleUnbindService(BindServiceData data) {
=======
    private final void handleUnbindService(BindServiceData data) {
>>>>>>> 54b6cfa... Initial Contribution
        Service s = mServices.get(data.token);
        if (s != null) {
            try {
                data.intent.setExtrasClassLoader(s.getClassLoader());
                boolean doRebind = s.onUnbind(data.intent);
                try {
                    if (doRebind) {
                        ActivityManagerNative.getDefault().unbindFinished(
                                data.token, data.intent, doRebind);
                    } else {
                        ActivityManagerNative.getDefault().serviceDoneExecuting(
<<<<<<< HEAD
                                data.token, 0, 0, 0);
=======
                                data.token);
>>>>>>> 54b6cfa... Initial Contribution
                    }
                } catch (RemoteException ex) {
                }
            } catch (Exception e) {
                if (!mInstrumentation.onException(s, e)) {
                    throw new RuntimeException(
                            "Unable to unbind to service " + s
                            + " with " + data.intent + ": " + e.toString(), e);
                }
            }
        }
    }

<<<<<<< HEAD
    private void handleDumpService(DumpComponentInfo info) {
        final StrictMode.ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
        try {
            Service s = mServices.get(info.token);
            if (s != null) {
                PrintWriter pw = new PrintWriter(new FileOutputStream(info.fd.getFileDescriptor()));
                s.dump(info.fd.getFileDescriptor(), pw, info.args);
                pw.flush();
            }
        } finally {
            IoUtils.closeQuietly(info.fd);
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    private void handleDumpActivity(DumpComponentInfo info) {
        final StrictMode.ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
        try {
            ActivityClientRecord r = mActivities.get(info.token);
            if (r != null && r.activity != null) {
                PrintWriter pw = new PrintWriter(new FileOutputStream(info.fd.getFileDescriptor()));
                r.activity.dump(info.prefix, info.fd.getFileDescriptor(), pw, info.args);
                pw.flush();
            }
        } finally {
            IoUtils.closeQuietly(info.fd);
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    private void handleDumpProvider(DumpComponentInfo info) {
        final StrictMode.ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
        try {
            ProviderClientRecord r = mLocalProviders.get(info.token);
            if (r != null && r.mLocalProvider != null) {
                PrintWriter pw = new PrintWriter(new FileOutputStream(info.fd.getFileDescriptor()));
                r.mLocalProvider.dump(info.fd.getFileDescriptor(), pw, info.args);
                pw.flush();
            }
        } finally {
            IoUtils.closeQuietly(info.fd);
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    private void handleServiceArgs(ServiceArgsData data) {
=======
    private void handleDumpService(DumpServiceInfo info) {
        try {
            Service s = mServices.get(info.service);
            if (s != null) {
                PrintWriter pw = new PrintWriter(new FileOutputStream(info.fd));
                s.dump(info.fd, pw, info.args);
                pw.close();
            }
        } finally {
            synchronized (info) {
                info.dumped = true;
                info.notifyAll();
            }
        }
    }

    private final void handleServiceArgs(ServiceArgsData data) {
>>>>>>> 54b6cfa... Initial Contribution
        Service s = mServices.get(data.token);
        if (s != null) {
            try {
                if (data.args != null) {
                    data.args.setExtrasClassLoader(s.getClassLoader());
                }
<<<<<<< HEAD
                int res;
                if (!data.taskRemoved) {
                    res = s.onStartCommand(data.args, data.flags, data.startId);
                } else {
                    s.onTaskRemoved(data.args);
                    res = Service.START_TASK_REMOVED_COMPLETE;
                }

                QueuedWork.waitToFinish();

                try {
                    ActivityManagerNative.getDefault().serviceDoneExecuting(
                            data.token, 1, data.startId, res);
                } catch (RemoteException e) {
                    // nothing to do.
                }
                ensureJitEnabled();
=======
                s.onStart(data.args, data.startId);
                try {
                    ActivityManagerNative.getDefault().serviceDoneExecuting(data.token);
                } catch (RemoteException e) {
                    // nothing to do.
                }
>>>>>>> 54b6cfa... Initial Contribution
            } catch (Exception e) {
                if (!mInstrumentation.onException(s, e)) {
                    throw new RuntimeException(
                            "Unable to start service " + s
                            + " with " + data.args + ": " + e.toString(), e);
                }
            }
        }
    }

<<<<<<< HEAD
    private void handleStopService(IBinder token) {
        Service s = mServices.remove(token);
        if (s != null) {
            try {
                if (localLOGV) Slog.v(TAG, "Destroying service " + s);
                s.onDestroy();
                Context context = s.getBaseContext();
                if (context instanceof ContextImpl) {
                    final String who = s.getClassName();
                    ((ContextImpl) context).scheduleFinalCleanup(who, "Service");
                }

                QueuedWork.waitToFinish();

                try {
                    ActivityManagerNative.getDefault().serviceDoneExecuting(
                            token, 0, 0, 0);
=======
    private final void handleStopService(IBinder token) {
        Service s = mServices.remove(token);
        if (s != null) {
            try {
                if (localLOGV) Log.v(TAG, "Destroying service " + s);
                s.onDestroy();
                Context context = s.getBaseContext();
                if (context instanceof ApplicationContext) {
                    final String who = s.getClassName();
                    ((ApplicationContext) context).scheduleFinalCleanup(who, "Service");
                }
                try {
                    ActivityManagerNative.getDefault().serviceDoneExecuting(token);
>>>>>>> 54b6cfa... Initial Contribution
                } catch (RemoteException e) {
                    // nothing to do.
                }
            } catch (Exception e) {
                if (!mInstrumentation.onException(s, e)) {
                    throw new RuntimeException(
                            "Unable to stop service " + s
                            + ": " + e.toString(), e);
                }
            }
        }
<<<<<<< HEAD
        //Slog.i(TAG, "Running services: " + mServices);
    }

    public final ActivityClientRecord performResumeActivity(IBinder token,
            boolean clearHide) {
        ActivityClientRecord r = mActivities.get(token);
        if (localLOGV) Slog.v(TAG, "Performing resume of " + r
=======
        //Log.i(TAG, "Running services: " + mServices);
    }

    public final ActivityRecord performResumeActivity(IBinder token,
            boolean clearHide) {
        ActivityRecord r = mActivities.get(token);
        if (localLOGV) Log.v(TAG, "Performing resume of " + r
>>>>>>> 54b6cfa... Initial Contribution
                + " finished=" + r.activity.mFinished);
        if (r != null && !r.activity.mFinished) {
            if (clearHide) {
                r.hideForNow = false;
                r.activity.mStartedActivity = false;
            }
            try {
                if (r.pendingIntents != null) {
                    deliverNewIntents(r, r.pendingIntents);
                    r.pendingIntents = null;
                }
                if (r.pendingResults != null) {
                    deliverResults(r, r.pendingResults);
                    r.pendingResults = null;
                }
                r.activity.performResume();

<<<<<<< HEAD
                EventLog.writeEvent(LOG_ON_RESUME_CALLED,
                        r.activity.getComponentName().getClassName());

                r.paused = false;
                r.stopped = false;
=======
                EventLog.writeEvent(LOG_ON_RESUME_CALLED, 
                        r.activity.getComponentName().getClassName());
                
                r.paused = false;
                r.stopped = false;
                if (r.activity.mStartedActivity) {
                    r.hideForNow = true;
                }
>>>>>>> 54b6cfa... Initial Contribution
                r.state = null;
            } catch (Exception e) {
                if (!mInstrumentation.onException(r.activity, e)) {
                    throw new RuntimeException(
                        "Unable to resume activity "
                        + r.intent.getComponent().toShortString()
                        + ": " + e.toString(), e);
                }
            }
        }
        return r;
    }

<<<<<<< HEAD
    static final void cleanUpPendingRemoveWindows(ActivityClientRecord r) {
        if (r.mPendingRemoveWindow != null) {
            r.mPendingRemoveWindowManager.removeViewImmediate(r.mPendingRemoveWindow);
            IBinder wtoken = r.mPendingRemoveWindow.getWindowToken();
            if (wtoken != null) {
                WindowManagerImpl.getDefault().closeAll(wtoken,
                        r.activity.getClass().getName(), "Activity");
            }
        }
        r.mPendingRemoveWindow = null;
        r.mPendingRemoveWindowManager = null;
    }

    final void handleResumeActivity(IBinder token, boolean clearHide, boolean isForward) {
=======
    final void handleResumeActivity(IBinder token, boolean clearHide) {
>>>>>>> 54b6cfa... Initial Contribution
        // If we are getting ready to gc after going to the background, well
        // we are back active so skip it.
        unscheduleGcIdler();

<<<<<<< HEAD
        ActivityClientRecord r = performResumeActivity(token, clearHide);
=======
        ActivityRecord r = performResumeActivity(token, clearHide);
>>>>>>> 54b6cfa... Initial Contribution

        if (r != null) {
            final Activity a = r.activity;

<<<<<<< HEAD
            if (localLOGV) Slog.v(
=======
            if (localLOGV) Log.v(
>>>>>>> 54b6cfa... Initial Contribution
                TAG, "Resume " + r + " started activity: " +
                a.mStartedActivity + ", hideForNow: " + r.hideForNow
                + ", finished: " + a.mFinished);

<<<<<<< HEAD
            final int forwardBit = isForward ?
                    WindowManager.LayoutParams.SOFT_INPUT_IS_FORWARD_NAVIGATION : 0;

            // If the window hasn't yet been added to the window manager,
            // and this guy didn't finish itself or start another activity,
            // then go ahead and add the window.
            boolean willBeVisible = !a.mStartedActivity;
            if (!willBeVisible) {
                try {
                    willBeVisible = ActivityManagerNative.getDefault().willActivityBeVisible(
                            a.getActivityToken());
                } catch (RemoteException e) {
                }
            }
            if (r.window == null && !a.mFinished && willBeVisible) {
=======
            // If the window hasn't yet been added to the window manager,
            // and this guy didn't finish itself or start another activity,
            // then go ahead and add the window.
            if (r.window == null && !a.mFinished && !a.mStartedActivity) {
>>>>>>> 54b6cfa... Initial Contribution
                r.window = r.activity.getWindow();
                View decor = r.window.getDecorView();
                decor.setVisibility(View.INVISIBLE);
                ViewManager wm = a.getWindowManager();
                WindowManager.LayoutParams l = r.window.getAttributes();
                a.mDecor = decor;
                l.type = WindowManager.LayoutParams.TYPE_BASE_APPLICATION;
<<<<<<< HEAD
                l.softInputMode |= forwardBit;
                if (a.mVisibleFromClient) {
                    a.mWindowAdded = true;
                    wm.addView(decor, l);
                }

            // If the window has already been added, but during resume
            // we started another activity, then don't yet make the
            // window visible.
            } else if (!willBeVisible) {
                if (localLOGV) Slog.v(
=======
                wm.addView(decor, l);

            // If the window has already been added, but during resume
            // we started another activity, then don't yet make the
            // window visisble.
            } else if (a.mStartedActivity) {
                if (localLOGV) Log.v(
>>>>>>> 54b6cfa... Initial Contribution
                    TAG, "Launch " + r + " mStartedActivity set");
                r.hideForNow = true;
            }

<<<<<<< HEAD
            // Get rid of anything left hanging around.
            cleanUpPendingRemoveWindows(r);

            // The window is now visible if it has been added, we are not
            // simply finishing, and we are not starting another activity.
            if (!r.activity.mFinished && willBeVisible
                    && r.activity.mDecor != null && !r.hideForNow) {
                if (r.newConfig != null) {
                    if (DEBUG_CONFIGURATION) Slog.v(TAG, "Resuming activity "
                            + r.activityInfo.name + " with newConfig " + r.newConfig);
                    performConfigurationChanged(r.activity, r.newConfig);
                    freeTextLayoutCachesIfNeeded(r.activity.mCurrentConfig.diff(r.newConfig));
                    r.newConfig = null;
                }
                if (localLOGV) Slog.v(TAG, "Resuming " + r + " with isForward="
                        + isForward);
                WindowManager.LayoutParams l = r.window.getAttributes();
                if ((l.softInputMode
                        & WindowManager.LayoutParams.SOFT_INPUT_IS_FORWARD_NAVIGATION)
                        != forwardBit) {
                    l.softInputMode = (l.softInputMode
                            & (~WindowManager.LayoutParams.SOFT_INPUT_IS_FORWARD_NAVIGATION))
                            | forwardBit;
                    if (r.activity.mVisibleFromClient) {
                        ViewManager wm = a.getWindowManager();
                        View decor = r.window.getDecorView();
                        wm.updateViewLayout(decor, l);
                    }
                }
                r.activity.mVisibleFromServer = true;
                mNumVisibleActivities++;
                if (r.activity.mVisibleFromClient) {
                    r.activity.makeVisible();
                }
            }

            if (!r.onlyLocalRequest) {
                r.nextIdle = mNewActivities;
                mNewActivities = r;
                if (localLOGV) Slog.v(
                    TAG, "Scheduling idle handler for " + r);
                Looper.myQueue().addIdleHandler(new Idler());
            }
            r.onlyLocalRequest = false;
=======
            // The window is now visible if it has been added, we are not
            // simply finishing, and we are not starting another activity.
            if (!r.activity.mFinished && r.activity.mDecor != null
                    && !r.hideForNow) {
                if (r.newConfig != null) {
                    performConfigurationChanged(r.activity, r.newConfig);
                    r.newConfig = null;
                }
                r.activity.mDecor.setVisibility(View.VISIBLE);
                mNumVisibleActivities++;
            }

            r.nextIdle = mNewActivities;
            mNewActivities = r;
            if (localLOGV) Log.v(
                TAG, "Scheduling idle handler for " + r);
            Looper.myQueue().addIdleHandler(new Idler());
>>>>>>> 54b6cfa... Initial Contribution

        } else {
            // If an exception was thrown when trying to resume, then
            // just end this activity.
            try {
                ActivityManagerNative.getDefault()
                    .finishActivity(token, Activity.RESULT_CANCELED, null);
            } catch (RemoteException ex) {
            }
        }
    }

    private int mThumbnailWidth = -1;
    private int mThumbnailHeight = -1;
<<<<<<< HEAD
    private Bitmap mAvailThumbnailBitmap = null;
    private Canvas mThumbnailCanvas = null;

    private Bitmap createThumbnailBitmap(ActivityClientRecord r) {
        Bitmap thumbnail = mAvailThumbnailBitmap;
        try {
            if (thumbnail == null) {
                int w = mThumbnailWidth;
                int h;
                if (w < 0) {
                    Resources res = r.activity.getResources();
                    mThumbnailHeight = h =
                        res.getDimensionPixelSize(com.android.internal.R.dimen.thumbnail_height);

                    mThumbnailWidth = w =
                        res.getDimensionPixelSize(com.android.internal.R.dimen.thumbnail_width);
                } else {
                    h = mThumbnailHeight;
                }

                // On platforms where we don't want thumbnails, set dims to (0,0)
                if ((w > 0) && (h > 0)) {
                    thumbnail = Bitmap.createBitmap(w, h, THUMBNAIL_FORMAT);
                    thumbnail.eraseColor(0);
                }
            }

            if (thumbnail != null) {
                Canvas cv = mThumbnailCanvas;
                if (cv == null) {
                    mThumbnailCanvas = cv = new Canvas();
                }
    
                cv.setBitmap(thumbnail);
                if (!r.activity.onCreateThumbnail(thumbnail, cv)) {
                    mAvailThumbnailBitmap = thumbnail;
                    thumbnail = null;
                }
                cv.setBitmap(null);
            }

=======

    private final Bitmap createThumbnailBitmap(ActivityRecord r) {
        Bitmap thumbnail = null;
        try {
            int w = mThumbnailWidth;
            int h;
            if (w < 0) {
                Resources res = r.activity.getResources();
                mThumbnailHeight = h =
                    res.getDimensionPixelSize(com.android.internal.R.dimen.thumbnail_height);

                mThumbnailWidth = w =
                    res.getDimensionPixelSize(com.android.internal.R.dimen.thumbnail_width);
            } else {
                h = mThumbnailHeight;
            }

            // XXX Only set hasAlpha if needed?
            thumbnail = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
            thumbnail.eraseColor(0);
            Canvas cv = new Canvas(thumbnail);
            if (!r.activity.onCreateThumbnail(thumbnail, cv)) {
                thumbnail = null;
            }
>>>>>>> 54b6cfa... Initial Contribution
        } catch (Exception e) {
            if (!mInstrumentation.onException(r.activity, e)) {
                throw new RuntimeException(
                        "Unable to create thumbnail of "
                        + r.intent.getComponent().toShortString()
                        + ": " + e.toString(), e);
            }
            thumbnail = null;
        }

        return thumbnail;
    }

<<<<<<< HEAD
    private void handlePauseActivity(IBinder token, boolean finished,
            boolean userLeaving, int configChanges) {
        ActivityClientRecord r = mActivities.get(token);
        if (r != null) {
            //Slog.v(TAG, "userLeaving=" + userLeaving + " handling pause of " + r);
            if (userLeaving) {
                performUserLeavingActivity(r);
            }

            r.activity.mConfigChangeFlags |= configChanges;
            performPauseActivity(token, finished, r.isPreHoneycomb());

            // Make sure any pending writes are now committed.
            if (r.isPreHoneycomb()) {
                QueuedWork.waitToFinish();
            }
            
            // Tell the activity manager we have paused.
            try {
                ActivityManagerNative.getDefault().activityPaused(token);
=======
    private final void handlePauseActivity(IBinder token, boolean finished,
            int configChanges) {
        ActivityRecord r = mActivities.get(token);
        if (r != null) {
            r.activity.mConfigChangeFlags |= configChanges;
            Bundle state = performPauseActivity(token, finished, true);

            // Tell the activity manager we have paused.
            try {
                ActivityManagerNative.getDefault().activityPaused(token, state);
>>>>>>> 54b6cfa... Initial Contribution
            } catch (RemoteException ex) {
            }
        }
    }

<<<<<<< HEAD
    final void performUserLeavingActivity(ActivityClientRecord r) {
        mInstrumentation.callActivityOnUserLeaving(r.activity);
    }

    final Bundle performPauseActivity(IBinder token, boolean finished,
            boolean saveState) {
        ActivityClientRecord r = mActivities.get(token);
        return r != null ? performPauseActivity(r, finished, saveState) : null;
    }

    final Bundle performPauseActivity(ActivityClientRecord r, boolean finished,
=======
    final Bundle performPauseActivity(IBinder token, boolean finished,
            boolean saveState) {
        ActivityRecord r = mActivities.get(token);
        return r != null ? performPauseActivity(r, finished, saveState) : null;
    }

    final Bundle performPauseActivity(ActivityRecord r, boolean finished,
>>>>>>> 54b6cfa... Initial Contribution
            boolean saveState) {
        if (r.paused) {
            if (r.activity.mFinished) {
                // If we are finishing, we won't call onResume() in certain cases.
                // So here we likewise don't want to call onPause() if the activity
                // isn't resumed.
                return null;
            }
            RuntimeException e = new RuntimeException(
                    "Performing pause of activity that is not resumed: "
                    + r.intent.getComponent().toShortString());
<<<<<<< HEAD
            Slog.e(TAG, e.getMessage(), e);
=======
            Log.e(TAG, e.getMessage(), e);
>>>>>>> 54b6cfa... Initial Contribution
        }
        Bundle state = null;
        if (finished) {
            r.activity.mFinished = true;
        }
        try {
            // Next have the activity save its current state and managed dialogs...
            if (!r.activity.mFinished && saveState) {
                state = new Bundle();
<<<<<<< HEAD
                state.setAllowFds(false);
=======
>>>>>>> 54b6cfa... Initial Contribution
                mInstrumentation.callActivityOnSaveInstanceState(r.activity, state);
                r.state = state;
            }
            // Now we are idle.
            r.activity.mCalled = false;
            mInstrumentation.callActivityOnPause(r.activity);
            EventLog.writeEvent(LOG_ON_PAUSE_CALLED, r.activity.getComponentName().getClassName());
            if (!r.activity.mCalled) {
                throw new SuperNotCalledException(
                    "Activity " + r.intent.getComponent().toShortString() +
                    " did not call through to super.onPause()");
            }

        } catch (SuperNotCalledException e) {
            throw e;

        } catch (Exception e) {
            if (!mInstrumentation.onException(r.activity, e)) {
                throw new RuntimeException(
                        "Unable to pause activity "
                        + r.intent.getComponent().toShortString()
                        + ": " + e.toString(), e);
            }
        }
        r.paused = true;
<<<<<<< HEAD

        // Notify any outstanding on paused listeners
        ArrayList<OnActivityPausedListener> listeners;
        synchronized (mOnPauseListeners) {
            listeners = mOnPauseListeners.remove(r.activity);
        }
        int size = (listeners != null ? listeners.size() : 0);
        for (int i = 0; i < size; i++) {
            listeners.get(i).onPaused(r.activity);
        }

        return state;
    }

    final void performStopActivity(IBinder token, boolean saveState) {
        ActivityClientRecord r = mActivities.get(token);
        performStopActivityInner(r, null, false, saveState);
    }

    private static class StopInfo implements Runnable {
        ActivityClientRecord activity;
        Bundle state;
        Bitmap thumbnail;
        CharSequence description;

        @Override public void run() {
            // Tell activity manager we have been stopped.
            try {
                if (DEBUG_MEMORY_TRIM) Slog.v(TAG, "Reporting activity stopped: " + activity);
                ActivityManagerNative.getDefault().activityStopped(
                    activity.token, state, thumbnail, description);
            } catch (RemoteException ex) {
            }
        }
    }

    private static final class ProviderRefCount {
        public final IActivityManager.ContentProviderHolder holder;
        public final ProviderClientRecord client;
        public int stableCount;
        public int unstableCount;

        // When this is set, the stable and unstable ref counts are 0 and
        // we have a pending operation scheduled to remove the ref count
        // from the activity manager.  On the activity manager we are still
        // holding an unstable ref, though it is not reflected in the counts
        // here.
        public boolean removePending;

        ProviderRefCount(IActivityManager.ContentProviderHolder inHolder,
                ProviderClientRecord inClient, int sCount, int uCount) {
            holder = inHolder;
            client = inClient;
            stableCount = sCount;
            unstableCount = uCount;
        }
    }

    /**
     * Core implementation of stopping an activity.  Note this is a little
     * tricky because the server's meaning of stop is slightly different
     * than our client -- for the server, stop means to save state and give
     * it the result when it is done, but the window may still be visible.
     * For the client, we want to call onStop()/onStart() to indicate when
     * the activity's UI visibillity changes.
     */
    private void performStopActivityInner(ActivityClientRecord r,
            StopInfo info, boolean keepShown, boolean saveState) {
        if (localLOGV) Slog.v(TAG, "Performing stop of " + r);
        Bundle state = null;
=======
        return state;
    }

    final void performStopActivity(IBinder token) {
        ActivityRecord r = mActivities.get(token);
        performStopActivityInner(r, null, false);
    }

    private static class StopInfo {
        Bitmap thumbnail;
        CharSequence description;
    }

    private final class ProviderRefCount {
        public int count;
        ProviderRefCount(int pCount) {
            count = pCount;
        }
    }

    private final void performStopActivityInner(ActivityRecord r,
            StopInfo info, boolean keepShown) {
        if (localLOGV) Log.v(TAG, "Performing stop of " + r);
>>>>>>> 54b6cfa... Initial Contribution
        if (r != null) {
            if (!keepShown && r.stopped) {
                if (r.activity.mFinished) {
                    // If we are finishing, we won't call onResume() in certain
                    // cases.  So here we likewise don't want to call onStop()
                    // if the activity isn't resumed.
                    return;
                }
                RuntimeException e = new RuntimeException(
                        "Performing stop of activity that is not resumed: "
                        + r.intent.getComponent().toShortString());
<<<<<<< HEAD
                Slog.e(TAG, e.getMessage(), e);
=======
                Log.e(TAG, e.getMessage(), e);
>>>>>>> 54b6cfa... Initial Contribution
            }

            if (info != null) {
                try {
                    // First create a thumbnail for the activity...
<<<<<<< HEAD
                    // For now, don't create the thumbnail here; we are
                    // doing that by doing a screen snapshot.
                    info.thumbnail = null; //createThumbnailBitmap(r);
=======
                    //info.thumbnail = createThumbnailBitmap(r);
>>>>>>> 54b6cfa... Initial Contribution
                    info.description = r.activity.onCreateDescription();
                } catch (Exception e) {
                    if (!mInstrumentation.onException(r.activity, e)) {
                        throw new RuntimeException(
                                "Unable to save state of activity "
                                + r.intent.getComponent().toShortString()
                                + ": " + e.toString(), e);
                    }
                }
            }

<<<<<<< HEAD
            // Next have the activity save its current state and managed dialogs...
            if (!r.activity.mFinished && saveState) {
                if (r.state == null) {
                    state = new Bundle();
                    state.setAllowFds(false);
                    mInstrumentation.callActivityOnSaveInstanceState(r.activity, state);
                    r.state = state;
                } else {
                    state = r.state;
                }
            }

=======
>>>>>>> 54b6cfa... Initial Contribution
            if (!keepShown) {
                try {
                    // Now we are idle.
                    r.activity.performStop();
                } catch (Exception e) {
                    if (!mInstrumentation.onException(r.activity, e)) {
                        throw new RuntimeException(
                                "Unable to stop activity "
                                + r.intent.getComponent().toShortString()
                                + ": " + e.toString(), e);
                    }
                }
                r.stopped = true;
            }

            r.paused = true;
        }
    }

<<<<<<< HEAD
    private void updateVisibility(ActivityClientRecord r, boolean show) {
        View v = r.activity.mDecor;
        if (v != null) {
            if (show) {
                if (!r.activity.mVisibleFromServer) {
                    r.activity.mVisibleFromServer = true;
                    mNumVisibleActivities++;
                    if (r.activity.mVisibleFromClient) {
                        r.activity.makeVisible();
                    }
                }
                if (r.newConfig != null) {
                    if (DEBUG_CONFIGURATION) Slog.v(TAG, "Updating activity vis "
                            + r.activityInfo.name + " with new config " + r.newConfig);
                    performConfigurationChanged(r.activity, r.newConfig);
                    freeTextLayoutCachesIfNeeded(r.activity.mCurrentConfig.diff(r.newConfig));
                    r.newConfig = null;
                }
            } else {
                if (r.activity.mVisibleFromServer) {
                    r.activity.mVisibleFromServer = false;
                    mNumVisibleActivities--;
                    v.setVisibility(View.INVISIBLE);
=======
    private final void updateVisibility(ActivityRecord r, boolean show) {
        View v = r.activity.mDecor;
        if (v != null) {
            if (show) {
                if (v.getVisibility() != View.VISIBLE) {
                    v.setVisibility(View.VISIBLE);
                    mNumVisibleActivities++;
                }
                if (r.newConfig != null) {
                    performConfigurationChanged(r.activity, r.newConfig);
                    r.newConfig = null;
                }
            } else {
                if (v.getVisibility() == View.VISIBLE) {
                    v.setVisibility(View.INVISIBLE);
                    mNumVisibleActivities--;
>>>>>>> 54b6cfa... Initial Contribution
                }
            }
        }
    }

<<<<<<< HEAD
    private void handleStopActivity(IBinder token, boolean show, int configChanges) {
        ActivityClientRecord r = mActivities.get(token);
        r.activity.mConfigChangeFlags |= configChanges;

        StopInfo info = new StopInfo();
        performStopActivityInner(r, info, show, true);

        if (localLOGV) Slog.v(
=======
    private final void handleStopActivity(IBinder token, boolean show, int configChanges) {
        ActivityRecord r = mActivities.get(token);
        r.activity.mConfigChangeFlags |= configChanges;

        StopInfo info = new StopInfo();
        performStopActivityInner(r, info, show);

        if (localLOGV) Log.v(
>>>>>>> 54b6cfa... Initial Contribution
            TAG, "Finishing stop of " + r + ": show=" + show
            + " win=" + r.window);

        updateVisibility(r, show);
<<<<<<< HEAD

        // Make sure any pending writes are now committed.
        if (!r.isPreHoneycomb()) {
            QueuedWork.waitToFinish();
        }

        // Schedule the call to tell the activity manager we have
        // stopped.  We don't do this immediately, because we want to
        // have a chance for any other pending work (in particular memory
        // trim requests) to complete before you tell the activity
        // manager to proceed and allow us to go fully into the background.
        info.activity = r;
        info.state = r.state;
        mH.post(info);
    }

    final void performRestartActivity(IBinder token) {
        ActivityClientRecord r = mActivities.get(token);
=======
        
        // Tell activity manager we have been stopped.
        try {
            ActivityManagerNative.getDefault().activityStopped(
                r.token, info.thumbnail, info.description);
        } catch (RemoteException ex) {
        }
    }

    final void performRestartActivity(IBinder token) {
        ActivityRecord r = mActivities.get(token);
>>>>>>> 54b6cfa... Initial Contribution
        if (r.stopped) {
            r.activity.performRestart();
            r.stopped = false;
        }
    }

<<<<<<< HEAD
    private void handleWindowVisibility(IBinder token, boolean show) {
        ActivityClientRecord r = mActivities.get(token);
        
        if (r == null) {
            Log.w(TAG, "handleWindowVisibility: no activity for token " + token);
            return;
        }
        
        if (!show && !r.stopped) {
            performStopActivityInner(r, null, show, false);
=======
    private final void handleWindowVisibility(IBinder token, boolean show) {
        ActivityRecord r = mActivities.get(token);
        if (!show && !r.stopped) {
            performStopActivityInner(r, null, show);
>>>>>>> 54b6cfa... Initial Contribution
        } else if (show && r.stopped) {
            // If we are getting ready to gc after going to the background, well
            // we are back active so skip it.
            unscheduleGcIdler();

            r.activity.performRestart();
            r.stopped = false;
        }
        if (r.activity.mDecor != null) {
<<<<<<< HEAD
            if (false) Slog.v(
=======
            if (Config.LOGV) Log.v(
>>>>>>> 54b6cfa... Initial Contribution
                TAG, "Handle window " + r + " visibility: " + show);
            updateVisibility(r, show);
        }
    }

<<<<<<< HEAD
    private void handleSleeping(IBinder token, boolean sleeping) {
        ActivityClientRecord r = mActivities.get(token);

        if (r == null) {
            Log.w(TAG, "handleSleeping: no activity for token " + token);
            return;
        }

        if (sleeping) {
            if (!r.stopped && !r.isPreHoneycomb()) {
                try {
                    // Now we are idle.
                    r.activity.performStop();
                } catch (Exception e) {
                    if (!mInstrumentation.onException(r.activity, e)) {
                        throw new RuntimeException(
                                "Unable to stop activity "
                                + r.intent.getComponent().toShortString()
                                + ": " + e.toString(), e);
                    }
                }
                r.stopped = true;
            }

            // Make sure any pending writes are now committed.
            if (!r.isPreHoneycomb()) {
                QueuedWork.waitToFinish();
            }

            // Tell activity manager we slept.
            try {
                ActivityManagerNative.getDefault().activitySlept(r.token);
            } catch (RemoteException ex) {
            }
        } else {
            if (r.stopped && r.activity.mVisibleFromServer) {
                r.activity.performRestart();
                r.stopped = false;
            }
        }
    }

    private void handleSetCoreSettings(Bundle coreSettings) {
        synchronized (mPackages) {
            mCoreSettings = coreSettings;
        }
    }

    private void handleUpdatePackageCompatibilityInfo(UpdateCompatibilityData data) {
        LoadedApk apk = peekPackageInfo(data.pkg, false);
        if (apk != null) {
            apk.mCompatibilityInfo.set(data.info);
        }
        apk = peekPackageInfo(data.pkg, true);
        if (apk != null) {
            apk.mCompatibilityInfo.set(data.info);
        }
        handleConfigurationChanged(mConfiguration, data.info);
        WindowManagerImpl.getDefault().reportNewConfiguration(mConfiguration);
    }

    private void deliverResults(ActivityClientRecord r, List<ResultInfo> results) {
=======
    private final void deliverResults(ActivityRecord r, List<ResultInfo> results) {
>>>>>>> 54b6cfa... Initial Contribution
        final int N = results.size();
        for (int i=0; i<N; i++) {
            ResultInfo ri = results.get(i);
            try {
                if (ri.mData != null) {
                    ri.mData.setExtrasClassLoader(r.activity.getClassLoader());
                }
<<<<<<< HEAD
                if (DEBUG_RESULTS) Slog.v(TAG,
                        "Delivering result to activity " + r + " : " + ri);
=======
>>>>>>> 54b6cfa... Initial Contribution
                r.activity.dispatchActivityResult(ri.mResultWho,
                        ri.mRequestCode, ri.mResultCode, ri.mData);
            } catch (Exception e) {
                if (!mInstrumentation.onException(r.activity, e)) {
                    throw new RuntimeException(
                            "Failure delivering result " + ri + " to activity "
                            + r.intent.getComponent().toShortString()
                            + ": " + e.toString(), e);
                }
            }
        }
    }

<<<<<<< HEAD
    private void handleSendResult(ResultData res) {
        ActivityClientRecord r = mActivities.get(res.token);
        if (DEBUG_RESULTS) Slog.v(TAG, "Handling send result to " + r);
=======
    private final void handleSendResult(ResultData res) {
        ActivityRecord r = mActivities.get(res.token);
        if (localLOGV) Log.v(TAG, "Handling send result to " + r);
>>>>>>> 54b6cfa... Initial Contribution
        if (r != null) {
            final boolean resumed = !r.paused;
            if (!r.activity.mFinished && r.activity.mDecor != null
                    && r.hideForNow && resumed) {
                // We had hidden the activity because it started another
                // one...  we have gotten a result back and we are not
                // paused, so make sure our window is visible.
                updateVisibility(r, true);
            }
            if (resumed) {
                try {
                    // Now we are idle.
                    r.activity.mCalled = false;
<<<<<<< HEAD
                    r.activity.mTemporaryPause = true;
=======
>>>>>>> 54b6cfa... Initial Contribution
                    mInstrumentation.callActivityOnPause(r.activity);
                    if (!r.activity.mCalled) {
                        throw new SuperNotCalledException(
                            "Activity " + r.intent.getComponent().toShortString()
                            + " did not call through to super.onPause()");
                    }
                } catch (SuperNotCalledException e) {
                    throw e;
                } catch (Exception e) {
                    if (!mInstrumentation.onException(r.activity, e)) {
                        throw new RuntimeException(
                                "Unable to pause activity "
                                + r.intent.getComponent().toShortString()
                                + ": " + e.toString(), e);
                    }
                }
            }
            deliverResults(r, res.results);
            if (resumed) {
<<<<<<< HEAD
                r.activity.performResume();
                r.activity.mTemporaryPause = false;
=======
                mInstrumentation.callActivityOnResume(r.activity);
>>>>>>> 54b6cfa... Initial Contribution
            }
        }
    }

<<<<<<< HEAD
    public final ActivityClientRecord performDestroyActivity(IBinder token, boolean finishing) {
        return performDestroyActivity(token, finishing, 0, false);
    }

    private ActivityClientRecord performDestroyActivity(IBinder token, boolean finishing,
            int configChanges, boolean getNonConfigInstance) {
        ActivityClientRecord r = mActivities.get(token);
        Class activityClass = null;
        if (localLOGV) Slog.v(TAG, "Performing finish of " + r);
        if (r != null) {
            activityClass = r.activity.getClass();
=======
    public final ActivityRecord performDestroyActivity(IBinder token, boolean finishing) {
        return performDestroyActivity(token, finishing, 0, false);
    }

    private final ActivityRecord performDestroyActivity(IBinder token, boolean finishing,
            int configChanges, boolean getNonConfigInstance) {
        ActivityRecord r = mActivities.get(token);
        if (localLOGV) Log.v(TAG, "Performing finish of " + r);
        if (r != null) {
>>>>>>> 54b6cfa... Initial Contribution
            r.activity.mConfigChangeFlags |= configChanges;
            if (finishing) {
                r.activity.mFinished = true;
            }
            if (!r.paused) {
                try {
                    r.activity.mCalled = false;
                    mInstrumentation.callActivityOnPause(r.activity);
<<<<<<< HEAD
                    EventLog.writeEvent(LOG_ON_PAUSE_CALLED,
                            r.activity.getComponentName().getClassName());
                    if (!r.activity.mCalled) {
                        throw new SuperNotCalledException(
                            "Activity " + safeToComponentShortString(r.intent)
=======
                    EventLog.writeEvent(LOG_ON_PAUSE_CALLED, 
                            r.activity.getComponentName().getClassName());
                    if (!r.activity.mCalled) {
                        throw new SuperNotCalledException(
                            "Activity " + r.intent.getComponent().toShortString()
>>>>>>> 54b6cfa... Initial Contribution
                            + " did not call through to super.onPause()");
                    }
                } catch (SuperNotCalledException e) {
                    throw e;
                } catch (Exception e) {
                    if (!mInstrumentation.onException(r.activity, e)) {
                        throw new RuntimeException(
                                "Unable to pause activity "
<<<<<<< HEAD
                                + safeToComponentShortString(r.intent)
=======
                                + r.intent.getComponent().toShortString()
>>>>>>> 54b6cfa... Initial Contribution
                                + ": " + e.toString(), e);
                    }
                }
                r.paused = true;
            }
            if (!r.stopped) {
                try {
                    r.activity.performStop();
                } catch (SuperNotCalledException e) {
                    throw e;
                } catch (Exception e) {
                    if (!mInstrumentation.onException(r.activity, e)) {
                        throw new RuntimeException(
                                "Unable to stop activity "
<<<<<<< HEAD
                                + safeToComponentShortString(r.intent)
=======
                                + r.intent.getComponent().toShortString()
>>>>>>> 54b6cfa... Initial Contribution
                                + ": " + e.toString(), e);
                    }
                }
                r.stopped = true;
            }
            if (getNonConfigInstance) {
                try {
<<<<<<< HEAD
                    r.lastNonConfigurationInstances
                            = r.activity.retainNonConfigurationInstances();
=======
                    r.lastNonConfigurationInstance
                            = r.activity.onRetainNonConfigurationInstance();
>>>>>>> 54b6cfa... Initial Contribution
                } catch (Exception e) {
                    if (!mInstrumentation.onException(r.activity, e)) {
                        throw new RuntimeException(
                                "Unable to retain activity "
                                + r.intent.getComponent().toShortString()
                                + ": " + e.toString(), e);
                    }
                }
<<<<<<< HEAD
            }
            try {
                r.activity.mCalled = false;
                mInstrumentation.callActivityOnDestroy(r.activity);
                if (!r.activity.mCalled) {
                    throw new SuperNotCalledException(
                        "Activity " + safeToComponentShortString(r.intent) +
=======
                
            }
            try {
                r.activity.mCalled = false;
                r.activity.onDestroy();
                if (!r.activity.mCalled) {
                    throw new SuperNotCalledException(
                        "Activity " + r.intent.getComponent().toShortString() +
>>>>>>> 54b6cfa... Initial Contribution
                        " did not call through to super.onDestroy()");
                }
                if (r.window != null) {
                    r.window.closeAllPanels();
                }
            } catch (SuperNotCalledException e) {
                throw e;
            } catch (Exception e) {
                if (!mInstrumentation.onException(r.activity, e)) {
                    throw new RuntimeException(
<<<<<<< HEAD
                            "Unable to destroy activity " + safeToComponentShortString(r.intent)
=======
                            "Unable to destroy activity "
                            + r.intent.getComponent().toShortString()
>>>>>>> 54b6cfa... Initial Contribution
                            + ": " + e.toString(), e);
                }
            }
        }
        mActivities.remove(token);
<<<<<<< HEAD
        StrictMode.decrementExpectedActivityCount(activityClass);
        return r;
    }

    private static String safeToComponentShortString(Intent intent) {
        ComponentName component = intent.getComponent();
        return component == null ? "[Unknown]" : component.toShortString();
    }

    private void handleDestroyActivity(IBinder token, boolean finishing,
            int configChanges, boolean getNonConfigInstance) {
        ActivityClientRecord r = performDestroyActivity(token, finishing,
                configChanges, getNonConfigInstance);
        if (r != null) {
            cleanUpPendingRemoveWindows(r);
            WindowManager wm = r.activity.getWindowManager();
            View v = r.activity.mDecor;
            if (v != null) {
                if (r.activity.mVisibleFromServer) {
                    mNumVisibleActivities--;
                }
                IBinder wtoken = v.getWindowToken();
                if (r.activity.mWindowAdded) {
                    if (r.onlyLocalRequest) {
                        // Hold off on removing this until the new activity's
                        // window is being added.
                        r.mPendingRemoveWindow = v;
                        r.mPendingRemoveWindowManager = wm;
                    } else {
                        wm.removeViewImmediate(v);
                    }
                }
                if (wtoken != null && r.mPendingRemoveWindow == null) {
=======

        return r;
    }

    private final void handleDestroyActivity(IBinder token, boolean finishing,
            int configChanges, boolean getNonConfigInstance) {
        ActivityRecord r = performDestroyActivity(token, finishing,
                configChanges, getNonConfigInstance);
        if (r != null) {
            WindowManager wm = r.activity.getWindowManager();
            View v = r.activity.mDecor;
            if (v != null) {
                if (v.getVisibility() == View.VISIBLE) {
                    mNumVisibleActivities--;
                }
                IBinder wtoken = v.getWindowToken();
                wm.removeViewImmediate(v);
                if (wtoken != null) {
>>>>>>> 54b6cfa... Initial Contribution
                    WindowManagerImpl.getDefault().closeAll(wtoken,
                            r.activity.getClass().getName(), "Activity");
                }
                r.activity.mDecor = null;
            }
<<<<<<< HEAD
            if (r.mPendingRemoveWindow == null) {
                // If we are delaying the removal of the activity window, then
                // we can't clean up all windows here.  Note that we can't do
                // so later either, which means any windows that aren't closed
                // by the app will leak.  Well we try to warning them a lot
                // about leaking windows, because that is a bug, so if they are
                // using this recreate facility then they get to live with leaks.
                WindowManagerImpl.getDefault().closeAll(token,
                        r.activity.getClass().getName(), "Activity");
            }
=======
            WindowManagerImpl.getDefault().closeAll(token,
                    r.activity.getClass().getName(), "Activity");
>>>>>>> 54b6cfa... Initial Contribution

            // Mocked out contexts won't be participating in the normal
            // process lifecycle, but if we're running with a proper
            // ApplicationContext we need to have it tear down things
            // cleanly.
            Context c = r.activity.getBaseContext();
<<<<<<< HEAD
            if (c instanceof ContextImpl) {
                ((ContextImpl) c).scheduleFinalCleanup(
=======
            if (c instanceof ApplicationContext) {
                ((ApplicationContext) c).scheduleFinalCleanup(
>>>>>>> 54b6cfa... Initial Contribution
                        r.activity.getClass().getName(), "Activity");
            }
        }
        if (finishing) {
            try {
                ActivityManagerNative.getDefault().activityDestroyed(token);
            } catch (RemoteException ex) {
                // If the system process has died, it's game over for everyone.
            }
        }
    }

<<<<<<< HEAD
    public final void requestRelaunchActivity(IBinder token,
            List<ResultInfo> pendingResults, List<Intent> pendingNewIntents,
            int configChanges, boolean notResumed, Configuration config,
            boolean fromServer) {
        ActivityClientRecord target = null;

        synchronized (mPackages) {
            for (int i=0; i<mRelaunchingActivities.size(); i++) {
                ActivityClientRecord r = mRelaunchingActivities.get(i);
                if (r.token == token) {
                    target = r;
                    if (pendingResults != null) {
                        if (r.pendingResults != null) {
                            r.pendingResults.addAll(pendingResults);
                        } else {
                            r.pendingResults = pendingResults;
                        }
                    }
                    if (pendingNewIntents != null) {
                        if (r.pendingIntents != null) {
                            r.pendingIntents.addAll(pendingNewIntents);
                        } else {
                            r.pendingIntents = pendingNewIntents;
                        }
                    }
                    break;
                }
            }

            if (target == null) {
                target = new ActivityClientRecord();
                target.token = token;
                target.pendingResults = pendingResults;
                target.pendingIntents = pendingNewIntents;
                if (!fromServer) {
                    ActivityClientRecord existing = mActivities.get(token);
                    if (existing != null) {
                        target.startsNotResumed = existing.paused;
                    }
                    target.onlyLocalRequest = true;
                }
                mRelaunchingActivities.add(target);
                queueOrSendMessage(H.RELAUNCH_ACTIVITY, target);
            }

            if (fromServer) {
                target.startsNotResumed = notResumed;
                target.onlyLocalRequest = false;
            }
            if (config != null) {
                target.createdConfig = config;
            }
            target.pendingConfigChanges |= configChanges;
        }
    }

    private void handleRelaunchActivity(ActivityClientRecord tmp) {
=======
    private final void handleRelaunchActivity(ActivityRecord tmp, int configChanges) {
>>>>>>> 54b6cfa... Initial Contribution
        // If we are getting ready to gc after going to the background, well
        // we are back active so skip it.
        unscheduleGcIdler();

        Configuration changedConfig = null;
<<<<<<< HEAD
        int configChanges = 0;

        // First: make sure we have the most recent configuration and most
        // recent version of the activity, or skip it if some previous call
        // had taken a more recent version.
        synchronized (mPackages) {
=======
        
        // First: make sure we have the most recent configuration and most
        // recent version of the activity, or skip it if some previous call
        // had taken a more recent version.
        synchronized (mRelaunchingActivities) {
>>>>>>> 54b6cfa... Initial Contribution
            int N = mRelaunchingActivities.size();
            IBinder token = tmp.token;
            tmp = null;
            for (int i=0; i<N; i++) {
<<<<<<< HEAD
                ActivityClientRecord r = mRelaunchingActivities.get(i);
                if (r.token == token) {
                    tmp = r;
                    configChanges |= tmp.pendingConfigChanges;
=======
                ActivityRecord r = mRelaunchingActivities.get(i);
                if (r.token == token) {
                    tmp = r;
>>>>>>> 54b6cfa... Initial Contribution
                    mRelaunchingActivities.remove(i);
                    i--;
                    N--;
                }
            }
<<<<<<< HEAD

            if (tmp == null) {
                if (DEBUG_CONFIGURATION) Slog.v(TAG, "Abort, activity not relaunching!");
                return;
            }

            if (DEBUG_CONFIGURATION) Slog.v(TAG, "Relaunching activity "
                    + tmp.token + " with configChanges=0x"
                    + Integer.toHexString(configChanges));

=======
            
            if (tmp == null) {
                return;
            }
            
>>>>>>> 54b6cfa... Initial Contribution
            if (mPendingConfiguration != null) {
                changedConfig = mPendingConfiguration;
                mPendingConfiguration = null;
            }
        }
<<<<<<< HEAD

        if (tmp.createdConfig != null) {
            // If the activity manager is passing us its current config,
            // assume that is really what we want regardless of what we
            // may have pending.
            if (mConfiguration == null
                    || (tmp.createdConfig.isOtherSeqNewer(mConfiguration)
                            && mConfiguration.diff(tmp.createdConfig) != 0)) {
                if (changedConfig == null
                        || tmp.createdConfig.isOtherSeqNewer(changedConfig)) {
                    changedConfig = tmp.createdConfig;
                }
            }
        }
        
        if (DEBUG_CONFIGURATION) Slog.v(TAG, "Relaunching activity "
                + tmp.token + ": changedConfig=" + changedConfig);
        
        // If there was a pending configuration change, execute it first.
        if (changedConfig != null) {
            handleConfigurationChanged(changedConfig, null);
        }

        ActivityClientRecord r = mActivities.get(tmp.token);
        if (DEBUG_CONFIGURATION) Slog.v(TAG, "Handling relaunch of " + r);
        if (r == null) {
            return;
        }

        r.activity.mConfigChangeFlags |= configChanges;
        r.onlyLocalRequest = tmp.onlyLocalRequest;
        Intent currentIntent = r.activity.mIntent;

        r.activity.mChangingConfigurations = true;

        // Need to ensure state is saved.
        if (!r.paused) {
            performPauseActivity(r.token, false, r.isPreHoneycomb());
        }
        if (r.state == null && !r.stopped && !r.isPreHoneycomb()) {
            r.state = new Bundle();
            r.state.setAllowFds(false);
            mInstrumentation.callActivityOnSaveInstanceState(r.activity, r.state);
        }

        handleDestroyActivity(r.token, false, configChanges, true);

=======
        
        // If there was a pending configuration change, execute it first.
        if (changedConfig != null) {
            handleConfigurationChanged(changedConfig);
        }
        
        ActivityRecord r = mActivities.get(tmp.token);
        if (localLOGV) Log.v(TAG, "Handling relaunch of " + r);
        if (r == null) {
            return;
        }
        
        r.activity.mConfigChangeFlags |= configChanges;
        
        Bundle savedState = null;
        if (!r.paused) {
            savedState = performPauseActivity(r.token, false, true);
        }
        
        handleDestroyActivity(r.token, false, configChanges, true);
        
>>>>>>> 54b6cfa... Initial Contribution
        r.activity = null;
        r.window = null;
        r.hideForNow = false;
        r.nextIdle = null;
<<<<<<< HEAD
        // Merge any pending results and pending intents; don't just replace them
        if (tmp.pendingResults != null) {
            if (r.pendingResults == null) {
                r.pendingResults = tmp.pendingResults;
            } else {
                r.pendingResults.addAll(tmp.pendingResults);
            }
        }
        if (tmp.pendingIntents != null) {
            if (r.pendingIntents == null) {
                r.pendingIntents = tmp.pendingIntents;
            } else {
                r.pendingIntents.addAll(tmp.pendingIntents);
            }
        }
        r.startsNotResumed = tmp.startsNotResumed;

        handleLaunchActivity(r, currentIntent);
    }

    private void handleRequestThumbnail(IBinder token) {
        ActivityClientRecord r = mActivities.get(token);
=======
        r.pendingResults = tmp.pendingResults;
        r.pendingIntents = tmp.pendingIntents;
        r.startsNotResumed = tmp.startsNotResumed;
        if (savedState != null) {
            r.state = savedState;
        }
        
        handleLaunchActivity(r);
    }

    private final void handleRequestThumbnail(IBinder token) {
        ActivityRecord r = mActivities.get(token);
>>>>>>> 54b6cfa... Initial Contribution
        Bitmap thumbnail = createThumbnailBitmap(r);
        CharSequence description = null;
        try {
            description = r.activity.onCreateDescription();
        } catch (Exception e) {
            if (!mInstrumentation.onException(r.activity, e)) {
                throw new RuntimeException(
                        "Unable to create description of activity "
                        + r.intent.getComponent().toShortString()
                        + ": " + e.toString(), e);
            }
        }
        //System.out.println("Reporting top thumbnail " + thumbnail);
        try {
            ActivityManagerNative.getDefault().reportThumbnail(
                token, thumbnail, description);
        } catch (RemoteException ex) {
        }
    }

<<<<<<< HEAD
    ArrayList<ComponentCallbacks2> collectComponentCallbacksLocked(
            boolean allActivities, Configuration newConfig) {
        ArrayList<ComponentCallbacks2> callbacks
                = new ArrayList<ComponentCallbacks2>();

        if (mActivities.size() > 0) {
            for (ActivityClientRecord ar : mActivities.values()) {
                Activity a = ar.activity;
                if (a != null) {
                    Configuration thisConfig = applyConfigCompatMainThread(newConfig,
                            ar.packageInfo.mCompatibilityInfo.getIfNeeded());
                    if (!ar.activity.mFinished && (allActivities || !ar.paused)) {
                        // If the activity is currently resumed, its configuration
                        // needs to change right now.
                        callbacks.add(a);
                    } else if (thisConfig != null) {
=======
    ArrayList<ComponentCallbacks> collectComponentCallbacksLocked(
            boolean allActivities, Configuration newConfig) {
        ArrayList<ComponentCallbacks> callbacks
                = new ArrayList<ComponentCallbacks>();
        
        if (mActivities.size() > 0) {
            Iterator<ActivityRecord> it = mActivities.values().iterator();
            while (it.hasNext()) {
                ActivityRecord ar = it.next();
                Activity a = ar.activity;
                if (a != null) {
                    if (!ar.activity.mFinished && (allActivities ||
                            (a != null && !ar.paused))) {
                        // If the activity is currently resumed, its configuration
                        // needs to change right now.
                        callbacks.add(a);
                    } else if (newConfig != null) {
>>>>>>> 54b6cfa... Initial Contribution
                        // Otherwise, we will tell it about the change
                        // the next time it is resumed or shown.  Note that
                        // the activity manager may, before then, decide the
                        // activity needs to be destroyed to handle its new
                        // configuration.
<<<<<<< HEAD
                        if (DEBUG_CONFIGURATION) {
                            Slog.v(TAG, "Setting activity "
                                    + ar.activityInfo.name + " newConfig=" + thisConfig);
                        }
                        ar.newConfig = thisConfig;
=======
                        ar.newConfig = newConfig;
>>>>>>> 54b6cfa... Initial Contribution
                    }
                }
            }
        }
        if (mServices.size() > 0) {
<<<<<<< HEAD
            for (Service service : mServices.values()) {
                callbacks.add(service);
=======
            Iterator<Service> it = mServices.values().iterator();
            while (it.hasNext()) {
                callbacks.add(it.next());
>>>>>>> 54b6cfa... Initial Contribution
            }
        }
        synchronized (mProviderMap) {
            if (mLocalProviders.size() > 0) {
<<<<<<< HEAD
                for (ProviderClientRecord providerClientRecord : mLocalProviders.values()) {
                    callbacks.add(providerClientRecord.mLocalProvider);
=======
                Iterator<ProviderRecord> it = mLocalProviders.values().iterator();
                while (it.hasNext()) {
                    callbacks.add(it.next().mLocalProvider);
>>>>>>> 54b6cfa... Initial Contribution
                }
            }
        }
        final int N = mAllApplications.size();
        for (int i=0; i<N; i++) {
            callbacks.add(mAllApplications.get(i));
        }
<<<<<<< HEAD

        return callbacks;
    }

    private static void performConfigurationChanged(ComponentCallbacks2 cb, Configuration config) {
        // Only for Activity objects, check that they actually call up to their
        // superclass implementation.  ComponentCallbacks2 is an interface, so
=======
        
        return callbacks;
    }
    
    private final void performConfigurationChanged(
            ComponentCallbacks cb, Configuration config) {
        // Only for Activity objects, check that they actually call up to their
        // superclass implementation.  ComponentCallbacks is an interface, so
>>>>>>> 54b6cfa... Initial Contribution
        // we check the runtime type and act accordingly.
        Activity activity = (cb instanceof Activity) ? (Activity) cb : null;
        if (activity != null) {
            activity.mCalled = false;
        }
<<<<<<< HEAD

=======
        
>>>>>>> 54b6cfa... Initial Contribution
        boolean shouldChangeConfig = false;
        if ((activity == null) || (activity.mCurrentConfig == null)) {
            shouldChangeConfig = true;
        } else {
<<<<<<< HEAD

=======
            
>>>>>>> 54b6cfa... Initial Contribution
            // If the new config is the same as the config this Activity
            // is already running with then don't bother calling
            // onConfigurationChanged
            int diff = activity.mCurrentConfig.diff(config);
            if (diff != 0) {
<<<<<<< HEAD
                // If this activity doesn't handle any of the config changes
                // then don't bother calling onConfigurationChanged as we're
                // going to destroy it.
                if ((~activity.mActivityInfo.getRealConfigChanged() & diff) == 0) {
=======
                
                // If this activity doesn't handle any of the config changes
                // then don't bother calling onConfigurationChanged as we're
                // going to destroy it.
                if ((~activity.mActivityInfo.configChanges & diff) == 0) {
>>>>>>> 54b6cfa... Initial Contribution
                    shouldChangeConfig = true;
                }
            }
        }
<<<<<<< HEAD

        if (DEBUG_CONFIGURATION) Slog.v(TAG, "Config callback " + cb
                + ": shouldChangeConfig=" + shouldChangeConfig);
        if (shouldChangeConfig) {
            cb.onConfigurationChanged(config);

            if (activity != null) {
                if (!activity.mCalled) {
                    throw new SuperNotCalledException(
                            "Activity " + activity.getLocalClassName() +
                        " did not call through to super.onConfigurationChanged()");
                }
                activity.mConfigChangeFlags = 0;
                activity.mCurrentConfig = new Configuration(config);
            }
        }
    }

    public final void applyConfigurationToResources(Configuration config) {
        synchronized (mPackages) {
            applyConfigurationToResourcesLocked(config, null);
        }
    }

    final boolean applyConfigurationToResourcesLocked(Configuration config,
            CompatibilityInfo compat) {
        if (mResConfiguration == null) {
            mResConfiguration = new Configuration();
        }
        if (!mResConfiguration.isOtherSeqNewer(config) && compat == null) {
            if (DEBUG_CONFIGURATION) Slog.v(TAG, "Skipping new config: curSeq="
                    + mResConfiguration.seq + ", newSeq=" + config.seq);
            return false;
        }
        int changes = mResConfiguration.updateFrom(config);
        DisplayMetrics dm = getDisplayMetricsLocked(null, true);

        if (compat != null && (mResCompatibilityInfo == null ||
                !mResCompatibilityInfo.equals(compat))) {
            mResCompatibilityInfo = compat;
            changes |= ActivityInfo.CONFIG_SCREEN_LAYOUT
                    | ActivityInfo.CONFIG_SCREEN_SIZE
                    | ActivityInfo.CONFIG_SMALLEST_SCREEN_SIZE;
        }

        // set it for java, this also affects newly created Resources
        if (config.locale != null) {
            Locale.setDefault(config.locale);
        }

        Resources.updateSystemConfiguration(config, dm, compat);

        ApplicationPackageManager.configurationChanged();
        //Slog.i(TAG, "Configuration changed in " + currentPackageName());
        
        Iterator<WeakReference<Resources>> it =
            mActiveResources.values().iterator();
        //Iterator<Map.Entry<String, WeakReference<Resources>>> it =
        //    mActiveResources.entrySet().iterator();
        while (it.hasNext()) {
            WeakReference<Resources> v = it.next();
            Resources r = v.get();
            if (r != null) {
                if (DEBUG_CONFIGURATION) Slog.v(TAG, "Changing resources "
                        + r + " config to: " + config);
                r.updateConfiguration(config, dm, compat);
                //Slog.i(TAG, "Updated app resources " + v.getKey()
                //        + " " + r + ": " + r.getConfiguration());
            } else {
                //Slog.i(TAG, "Removing old resources " + v.getKey());
                it.remove();
            }
        }
        
        return changes != 0;
    }

    final Configuration applyCompatConfiguration() {
        Configuration config = mConfiguration;
        if (mCompatConfiguration == null) {
            mCompatConfiguration = new Configuration();
        }
        mCompatConfiguration.setTo(mConfiguration);
        if (mResCompatibilityInfo != null && !mResCompatibilityInfo.supportsScreen()) {
            mResCompatibilityInfo.applyToConfiguration(mCompatConfiguration);
            config = mCompatConfiguration;
        }
        return config;
    }

    final void handleConfigurationChanged(Configuration config, CompatibilityInfo compat) {

        ArrayList<ComponentCallbacks2> callbacks = null;
        int configDiff = 0;

        synchronized (mPackages) {
            if (mPendingConfiguration != null) {
                if (!mPendingConfiguration.isOtherSeqNewer(config)) {
                    config = mPendingConfiguration;
                }
                mPendingConfiguration = null;
            }

            if (config == null) {
                return;
            }
            
            if (DEBUG_CONFIGURATION) Slog.v(TAG, "Handle configuration changed: "
                    + config);
        
            applyConfigurationToResourcesLocked(config, compat);
            
            if (mConfiguration == null) {
                mConfiguration = new Configuration();
            }
            if (!mConfiguration.isOtherSeqNewer(config) && compat == null) {
                return;
            }
            configDiff = mConfiguration.diff(config);
            mConfiguration.updateFrom(config);
            config = applyCompatConfiguration();
            callbacks = collectComponentCallbacksLocked(false, config);
        }
        
        // Cleanup hardware accelerated stuff
        WindowManagerImpl.getDefault().trimLocalMemory();

        freeTextLayoutCachesIfNeeded(configDiff);

        if (callbacks != null) {
            final int N = callbacks.size();
            for (int i=0; i<N; i++) {
                performConfigurationChanged(callbacks.get(i), config);
            }
        }
    }

    final void freeTextLayoutCachesIfNeeded(int configDiff) {
        if (configDiff != 0) {
            // Ask text layout engine to free its caches if there is a locale change
            boolean hasLocaleConfigChange = ((configDiff & ActivityInfo.CONFIG_LOCALE) != 0);
            if (hasLocaleConfigChange) {
                Canvas.freeTextLayoutCaches();
                if (DEBUG_CONFIGURATION) Slog.v(TAG, "Cleared TextLayout Caches");
            }
        }
    }

    final void handleActivityConfigurationChanged(IBinder token) {
        ActivityClientRecord r = mActivities.get(token);
        if (r == null || r.activity == null) {
            return;
        }

        if (DEBUG_CONFIGURATION) Slog.v(TAG, "Handle activity config changed: "
                + r.activityInfo.name);
        
        performConfigurationChanged(r.activity, mCompatConfiguration);

        freeTextLayoutCachesIfNeeded(r.activity.mCurrentConfig.diff(mCompatConfiguration));
    }

    final void handleProfilerControl(boolean start, ProfilerControlData pcd, int profileType) {
        if (start) {
            try {
                switch (profileType) {
                    default:                        
                        mProfiler.setProfiler(pcd.path, pcd.fd);
                        mProfiler.autoStopProfiler = false;
                        mProfiler.startProfiling();
                        break;
                }
            } catch (RuntimeException e) {
                Slog.w(TAG, "Profiling failed on path " + pcd.path
                        + " -- can the process access this path?");
            } finally {
                try {
                    pcd.fd.close();
                } catch (IOException e) {
                    Slog.w(TAG, "Failure closing profile fd", e);
                }
            }
        } else {
            switch (profileType) {
                default:
                    mProfiler.stopProfiling();
                    break;
=======
        
        if (shouldChangeConfig) {
            cb.onConfigurationChanged(config);
            
            if (activity != null) {
                if (!activity.mCalled) {
                    throw new SuperNotCalledException(
                            "Activity " + activity.getLocalClassName() +
                        " did not call through to super.onConfigurationChanged()");
                }
                activity.mConfigChangeFlags = 0;
                activity.mCurrentConfig = new Configuration(config);
>>>>>>> 54b6cfa... Initial Contribution
            }
        }
    }

<<<<<<< HEAD
    static final void handleDumpHeap(boolean managed, DumpHeapData dhd) {
        if (managed) {
            try {
                Debug.dumpHprofData(dhd.path, dhd.fd.getFileDescriptor());
            } catch (IOException e) {
                Slog.w(TAG, "Managed heap dump failed on path " + dhd.path
                        + " -- can the process access this path?");
            } finally {
                try {
                    dhd.fd.close();
                } catch (IOException e) {
                    Slog.w(TAG, "Failure closing profile fd", e);
                }
            }
        } else {
            Debug.dumpNativeHeap(dhd.fd.getFileDescriptor());
        }
    }

    final void handleDispatchPackageBroadcast(int cmd, String[] packages) {
        boolean hasPkgInfo = false;
        if (packages != null) {
            for (int i=packages.length-1; i>=0; i--) {
                //Slog.i(TAG, "Cleaning old package: " + packages[i]);
                if (!hasPkgInfo) {
                    WeakReference<LoadedApk> ref;
                    ref = mPackages.get(packages[i]);
                    if (ref != null && ref.get() != null) {
                        hasPkgInfo = true;
                    } else {
                        ref = mResourcePackages.get(packages[i]);
                        if (ref != null && ref.get() != null) {
                            hasPkgInfo = true;
                        }
                    }
                }
                mPackages.remove(packages[i]);
                mResourcePackages.remove(packages[i]);
            }
        }
        ApplicationPackageManager.handlePackageBroadcast(cmd, packages,
                hasPkgInfo);
    }
        
    final void handleLowMemory() {
        ArrayList<ComponentCallbacks2> callbacks;

        synchronized (mPackages) {
            callbacks = collectComponentCallbacksLocked(true, null);
        }

        final int N = callbacks.size();
        for (int i=0; i<N; i++) {
            callbacks.get(i).onLowMemory();
        }

        // Ask SQLite to free up as much memory as it can, mostly from its page caches.
        if (Process.myUid() != Process.SYSTEM_UID) {
            int sqliteReleased = SQLiteDatabase.releaseMemory();
            EventLog.writeEvent(SQLITE_MEM_RELEASED_EVENT_LOG_TAG, sqliteReleased);
        }

        // Ask graphics to free up as much as possible (font/image caches)
        Canvas.freeCaches();

        // Ask text layout engine to free also as much as possible
        Canvas.freeTextLayoutCaches();

        BinderInternal.forceGc("mem");
    }

    final void handleTrimMemory(int level) {
        if (DEBUG_MEMORY_TRIM) Slog.v(TAG, "Trimming memory to level: " + level);

        final WindowManagerImpl windowManager = WindowManagerImpl.getDefault();
        windowManager.startTrimMemory(level);

        ArrayList<ComponentCallbacks2> callbacks;
        synchronized (mPackages) {
            callbacks = collectComponentCallbacksLocked(true, null);
        }

        final int N = callbacks.size();
        for (int i = 0; i < N; i++) {
            callbacks.get(i).onTrimMemory(level);
        }

        windowManager.endTrimMemory();        
    }

    private void setupGraphicsSupport(LoadedApk info, File cacheDir) {
        if (Process.isIsolated()) {
            // Isolated processes aren't going to do UI.
            return;
        }
        try {
            int uid = Process.myUid();
            String[] packages = getPackageManager().getPackagesForUid(uid);

            // If there are several packages in this application we won't
            // initialize the graphics disk caches 
            if (packages != null && packages.length == 1) {
                HardwareRenderer.setupDiskCache(cacheDir);
                RenderScript.setupDiskCache(cacheDir);
            }
        } catch (RemoteException e) {
            // Ignore
        }
    }    
    
    private void handleBindApplication(AppBindData data) {
        mBoundApplication = data;
        mConfiguration = new Configuration(data.config);
        mCompatConfiguration = new Configuration(data.config);

        mProfiler = new Profiler();
        mProfiler.profileFile = data.initProfileFile;
        mProfiler.profileFd = data.initProfileFd;
        mProfiler.autoStopProfiler = data.initAutoStopProfiler;

        // send up app name; do this *before* waiting for debugger
        Process.setArgV0(data.processName);
        android.ddm.DdmHandleAppName.setAppName(data.processName);

        if (data.persistent) {
            // Persistent processes on low-memory devices do not get to
            // use hardware accelerated drawing, since this can add too much
            // overhead to the process.
            Display display = WindowManagerImpl.getDefault().getDefaultDisplay();
            if (!ActivityManager.isHighEndGfx(display)) {
                HardwareRenderer.disable(false);
            }
        }
        
        if (mProfiler.profileFd != null) {
            mProfiler.startProfiling();
        }

        // If the app is Honeycomb MR1 or earlier, switch its AsyncTask
        // implementation to use the pool executor.  Normally, we use the
        // serialized executor as the default. This has to happen in the
        // main thread so the main looper is set right.
        if (data.appInfo.targetSdkVersion <= android.os.Build.VERSION_CODES.HONEYCOMB_MR1) {
            AsyncTask.setDefaultExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

=======
    final void handleConfigurationChanged(Configuration config) {
        
        synchronized (mRelaunchingActivities) {
            if (mPendingConfiguration != null) {
                config = mPendingConfiguration;
                mPendingConfiguration = null;
            }
        }
        
        ArrayList<ComponentCallbacks> callbacks
                = new ArrayList<ComponentCallbacks>();
        
        synchronized(mPackages) {
            if (mConfiguration == null) {
                mConfiguration = new Configuration();
            }
            mConfiguration.updateFrom(config);

            // set it for java, this also affects newly created Resources
            if (config.locale != null) {
                Locale.setDefault(config.locale);
            }

            if (mSystemContext != null) {
                mSystemContext.getResources().updateConfiguration(config, null);
                //Log.i(TAG, "Updated system resources " + mSystemContext.getResources()
                //        + ": " + mSystemContext.getResources().getConfiguration());
            }

            ApplicationContext.ApplicationPackageManager.configurationChanged();
            //Log.i(TAG, "Configuration changed in " + currentPackageName());
            {
                Iterator<WeakReference<Resources>> it =
                    mActiveResources.values().iterator();
                //Iterator<Map.Entry<String, WeakReference<Resources>>> it =
                //    mActiveResources.entrySet().iterator();
                while (it.hasNext()) {
                    WeakReference<Resources> v = it.next();
                    Resources r = v.get();
                    if (r != null) {
                        r.updateConfiguration(config, null);
                        //Log.i(TAG, "Updated app resources " + v.getKey()
                        //        + " " + r + ": " + r.getConfiguration());
                    } else {
                        //Log.i(TAG, "Removing old resources " + v.getKey());
                        it.remove();
                    }
                }
            }
            
            callbacks = collectComponentCallbacksLocked(false, config);
        }
        
        final int N = callbacks.size();
        for (int i=0; i<N; i++) {
            performConfigurationChanged(callbacks.get(i), config);
        }
    }

    final void handleActivityConfigurationChanged(IBinder token) {
        ActivityRecord r = mActivities.get(token);
        if (r == null || r.activity == null) {
            return;
        }
        
        performConfigurationChanged(r.activity, mConfiguration);
    }

    final void handleLowMemory() {
        ArrayList<ComponentCallbacks> callbacks
                = new ArrayList<ComponentCallbacks>();

        synchronized(mPackages) {
            callbacks = collectComponentCallbacksLocked(true, null);
        }
        
        final int N = callbacks.size();
        for (int i=0; i<N; i++) {
            callbacks.get(i).onLowMemory();
        }

        // Ask SQLite to free up as much memory as it can, mostly from it's page caches
        int sqliteReleased = SQLiteDatabase.releaseMemory();
        EventLog.writeEvent(SQLITE_MEM_RELEASED_EVENT_LOG_TAG, sqliteReleased);

        BinderInternal.forceGc("mem");
    }

    private final void handleBindApplication(AppBindData data) {
        mBoundApplication = data;
        mConfiguration = new Configuration(data.config);

        // We now rely on this being set by zygote.
        //Process.setGid(data.appInfo.gid);
        //Process.setUid(data.appInfo.uid);

        // send up app name; do this *before* waiting for debugger
        android.ddm.DdmHandleAppName.setAppName(data.processName);

>>>>>>> 54b6cfa... Initial Contribution
        /*
         * Before spawning a new process, reset the time zone to be the system time zone.
         * This needs to be done because the system time zone could have changed after the
         * the spawning of this process. Without doing this this process would have the incorrect
         * system time zone.
         */
        TimeZone.setDefault(null);

        /*
         * Initialize the default locale in this process for the reasons we set the time zone.
         */
        Locale.setDefault(data.config.locale);

<<<<<<< HEAD
        /*
         * Update the system configuration since its preloaded and might not
         * reflect configuration changes. The configuration object passed
         * in AppBindData can be safely assumed to be up to date
         */
        applyConfigurationToResourcesLocked(data.config, data.compatInfo);
        applyCompatConfiguration();

        data.info = getPackageInfoNoCheck(data.appInfo, data.compatInfo);

        final ContextImpl appContext = new ContextImpl();
        appContext.init(data.info, null, this);
        final File cacheDir = appContext.getCacheDir();

        // Provide a usable directory for temporary files
        System.setProperty("java.io.tmpdir", cacheDir.getAbsolutePath());

        setupGraphicsSupport(data.info, cacheDir);

        /**
         * For system applications on userdebug/eng builds, log stack
         * traces of disk and network access to dropbox for analysis.
         */
        if ((data.appInfo.flags &
             (ApplicationInfo.FLAG_SYSTEM |
              ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)) != 0) {
            StrictMode.conditionallyEnableDebugLogging();
        }

        /**
         * For apps targetting SDK Honeycomb or later, we don't allow
         * network usage on the main event loop / UI thread.
         *
         * Note to those grepping:  this is what ultimately throws
         * NetworkOnMainThreadException ...
         */
        if (data.appInfo.targetSdkVersion > 9) {
            StrictMode.enableDeathOnNetwork();
        }

        /**
         * Switch this process to density compatibility mode if needed.
         */
        if ((data.appInfo.flags&ApplicationInfo.FLAG_SUPPORTS_SCREEN_DENSITIES)
                == 0) {
            Bitmap.setDefaultDensity(DisplayMetrics.DENSITY_DEFAULT);
        }
=======
        data.info = getPackageInfoNoCheck(data.appInfo);
>>>>>>> 54b6cfa... Initial Contribution

        if (data.debugMode != IApplicationThread.DEBUG_OFF) {
            // XXX should have option to change the port.
            Debug.changeDebugPort(8100);
            if (data.debugMode == IApplicationThread.DEBUG_WAIT) {
<<<<<<< HEAD
                Slog.w(TAG, "Application " + data.info.getPackageName()
=======
                Log.w(TAG, "Application " + data.info.getPackageName()
>>>>>>> 54b6cfa... Initial Contribution
                      + " is waiting for the debugger on port 8100...");

                IActivityManager mgr = ActivityManagerNative.getDefault();
                try {
                    mgr.showWaitingForDebugger(mAppThread, true);
                } catch (RemoteException ex) {
                }

                Debug.waitForDebugger();

                try {
                    mgr.showWaitingForDebugger(mAppThread, false);
                } catch (RemoteException ex) {
                }

            } else {
<<<<<<< HEAD
                Slog.w(TAG, "Application " + data.info.getPackageName()
=======
                Log.w(TAG, "Application " + data.info.getPackageName()
>>>>>>> 54b6cfa... Initial Contribution
                      + " can be debugged on port 8100...");
            }
        }

<<<<<<< HEAD
        // Enable OpenGL tracing if required
        if (data.enableOpenGlTrace) {
            GLUtils.enableTracing();
        }

        /**
         * Initialize the default http proxy in this process for the reasons we set the time zone.
         */
        IBinder b = ServiceManager.getService(Context.CONNECTIVITY_SERVICE);
        if (b != null) {
            // In pre-boot mode (doing initial launch to collect password), not
            // all system is up.  This includes the connectivity service, so don't
            // crash if we can't get it.
            IConnectivityManager service = IConnectivityManager.Stub.asInterface(b);
            try {
                ProxyProperties proxyProperties = service.getProxy();
                Proxy.setHttpProxySystemProperty(proxyProperties);
            } catch (RemoteException e) {}
        }

        if (data.instrumentationName != null) {
=======
        if (data.instrumentationName != null) {
            ApplicationContext appContext = new ApplicationContext();
            appContext.init(data.info, null, this);
>>>>>>> 54b6cfa... Initial Contribution
            InstrumentationInfo ii = null;
            try {
                ii = appContext.getPackageManager().
                    getInstrumentationInfo(data.instrumentationName, 0);
            } catch (PackageManager.NameNotFoundException e) {
            }
            if (ii == null) {
                throw new RuntimeException(
                    "Unable to find instrumentation info for: "
                    + data.instrumentationName);
            }

            mInstrumentationAppDir = ii.sourceDir;
<<<<<<< HEAD
            mInstrumentationAppLibraryDir = ii.nativeLibraryDir;
            mInstrumentationAppPackage = ii.packageName;
            mInstrumentedAppDir = data.info.getAppDir();
            mInstrumentedAppLibraryDir = data.info.getLibDir();
=======
            mInstrumentationAppPackage = ii.packageName;
            mInstrumentedAppDir = data.info.getAppDir();
>>>>>>> 54b6cfa... Initial Contribution

            ApplicationInfo instrApp = new ApplicationInfo();
            instrApp.packageName = ii.packageName;
            instrApp.sourceDir = ii.sourceDir;
            instrApp.publicSourceDir = ii.publicSourceDir;
            instrApp.dataDir = ii.dataDir;
<<<<<<< HEAD
            instrApp.nativeLibraryDir = ii.nativeLibraryDir;
            LoadedApk pi = getPackageInfo(instrApp, data.compatInfo,
                    appContext.getClassLoader(), false, true);
            ContextImpl instrContext = new ContextImpl();
=======
            PackageInfo pi = getPackageInfo(instrApp,
                    appContext.getClassLoader(), false, true);
            ApplicationContext instrContext = new ApplicationContext();
>>>>>>> 54b6cfa... Initial Contribution
            instrContext.init(pi, null, this);

            try {
                java.lang.ClassLoader cl = instrContext.getClassLoader();
                mInstrumentation = (Instrumentation)
                    cl.loadClass(data.instrumentationName.getClassName()).newInstance();
            } catch (Exception e) {
                throw new RuntimeException(
                    "Unable to instantiate instrumentation "
                    + data.instrumentationName + ": " + e.toString(), e);
            }

            mInstrumentation.init(this, instrContext, appContext,
                    new ComponentName(ii.packageName, ii.name), data.instrumentationWatcher);

<<<<<<< HEAD
            if (mProfiler.profileFile != null && !ii.handleProfiling
                    && mProfiler.profileFd == null) {
                mProfiler.handlingProfiling = true;
                File file = new File(mProfiler.profileFile);
=======
            if (data.profileFile != null && !ii.handleProfiling) {
                data.handlingProfiling = true;
                File file = new File(data.profileFile);
>>>>>>> 54b6cfa... Initial Contribution
                file.getParentFile().mkdirs();
                Debug.startMethodTracing(file.toString(), 8 * 1024 * 1024);
            }

<<<<<<< HEAD
        } else {
            mInstrumentation = new Instrumentation();
        }

        if ((data.appInfo.flags&ApplicationInfo.FLAG_LARGE_HEAP) != 0) {
            dalvik.system.VMRuntime.getRuntime().clearGrowthLimit();
        }

        // Allow disk access during application and provider setup. This could
        // block processing ordered broadcasts, but later processing would
        // probably end up doing the same disk access.
        final StrictMode.ThreadPolicy savedPolicy = StrictMode.allowThreadDiskWrites();
        try {
            // If the app is being launched for full backup or restore, bring it up in
            // a restricted environment with the base application class.
            Application app = data.info.makeApplication(data.restrictedBackupMode, null);
            mInitialApplication = app;

            // don't bring up providers in restricted mode; they may depend on the
            // app's custom Application class
            if (!data.restrictedBackupMode) {
                List<ProviderInfo> providers = data.providers;
                if (providers != null) {
                    installContentProviders(app, providers);
                    // For process that contains content providers, we want to
                    // ensure that the JIT is enabled "at some point".
                    mH.sendEmptyMessageDelayed(H.ENABLE_JIT, 10*1000);
                }
            }

            // Do this after providers, since instrumentation tests generally start their
            // test thread at this point, and we don't want that racing.
=======
>>>>>>> 54b6cfa... Initial Contribution
            try {
                mInstrumentation.onCreate(data.instrumentationArgs);
            }
            catch (Exception e) {
                throw new RuntimeException(
                    "Exception thrown in onCreate() of "
                    + data.instrumentationName + ": " + e.toString(), e);
            }

<<<<<<< HEAD
            try {
                mInstrumentation.callApplicationOnCreate(app);
            } catch (Exception e) {
                if (!mInstrumentation.onException(app, e)) {
                    throw new RuntimeException(
                        "Unable to create application " + app.getClass().getName()
                        + ": " + e.toString(), e);
                }
            }
        } finally {
            StrictMode.setThreadPolicy(savedPolicy);
=======
        } else {
            mInstrumentation = new Instrumentation();
        }

        Application app = data.info.makeApplication();
        mInitialApplication = app;

        List<ProviderInfo> providers = data.providers;
        if (providers != null) {
            installContentProviders(app, providers);
        }

        try {
            mInstrumentation.callApplicationOnCreate(app);
        } catch (Exception e) {
            if (!mInstrumentation.onException(app, e)) {
                throw new RuntimeException(
                    "Unable to create application " + app.getClass().getName()
                    + ": " + e.toString(), e);
            }
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /*package*/ final void finishInstrumentation(int resultCode, Bundle results) {
        IActivityManager am = ActivityManagerNative.getDefault();
<<<<<<< HEAD
        if (mProfiler.profileFile != null && mProfiler.handlingProfiling
                && mProfiler.profileFd == null) {
            Debug.stopMethodTracing();
        }
        //Slog.i(TAG, "am: " + ActivityManagerNative.getDefault()
=======
        if (mBoundApplication.profileFile != null && mBoundApplication.handlingProfiling) {
            Debug.stopMethodTracing();
        }
        //Log.i(TAG, "am: " + ActivityManagerNative.getDefault()
>>>>>>> 54b6cfa... Initial Contribution
        //      + ", app thr: " + mAppThread);
        try {
            am.finishInstrumentation(mAppThread, resultCode, results);
        } catch (RemoteException ex) {
        }
    }

<<<<<<< HEAD
    private void installContentProviders(
=======
    private final void installContentProviders(
>>>>>>> 54b6cfa... Initial Contribution
            Context context, List<ProviderInfo> providers) {
        final ArrayList<IActivityManager.ContentProviderHolder> results =
            new ArrayList<IActivityManager.ContentProviderHolder>();

<<<<<<< HEAD
        for (ProviderInfo cpi : providers) {
            StringBuilder buf = new StringBuilder(128);
            buf.append("Pub ");
=======
        Iterator<ProviderInfo> i = providers.iterator();
        while (i.hasNext()) {
            ProviderInfo cpi = i.next();
            StringBuilder buf = new StringBuilder(128);
            buf.append("Publishing provider ");
>>>>>>> 54b6cfa... Initial Contribution
            buf.append(cpi.authority);
            buf.append(": ");
            buf.append(cpi.name);
            Log.i(TAG, buf.toString());
<<<<<<< HEAD
            IActivityManager.ContentProviderHolder cph = installProvider(context, null, cpi,
                    false /*noisy*/, true /*noReleaseNeeded*/, true /*stable*/);
            if (cph != null) {
                cph.noReleaseNeeded = true;
                results.add(cph);
=======
            IContentProvider cp = installProvider(context, null, cpi, false);
            if (cp != null) {
                IActivityManager.ContentProviderHolder cph =
                    new IActivityManager.ContentProviderHolder(cpi);
                cph.provider = cp;
                results.add(cph);
                // Don't ever unload this provider from the process.
                synchronized(mProviderMap) {
                    mProviderRefCountMap.put(cp.asBinder(), new ProviderRefCount(10000));
                }
>>>>>>> 54b6cfa... Initial Contribution
            }
        }

        try {
            ActivityManagerNative.getDefault().publishContentProviders(
                getApplicationThread(), results);
        } catch (RemoteException ex) {
        }
    }

<<<<<<< HEAD
    public final IContentProvider acquireProvider(Context c, String name, boolean stable) {
        IContentProvider provider = acquireExistingProvider(c, name, stable);
        if (provider != null) {
            return provider;
        }

        // There is a possible race here.  Another thread may try to acquire
        // the same provider at the same time.  When this happens, we want to ensure
        // that the first one wins.
        // Note that we cannot hold the lock while acquiring and installing the
        // provider since it might take a long time to run and it could also potentially
        // be re-entrant in the case where the provider is in the same process.
        IActivityManager.ContentProviderHolder holder = null;
        try {
            holder = ActivityManagerNative.getDefault().getContentProvider(
                    getApplicationThread(), name, stable);
        } catch (RemoteException ex) {
        }
        if (holder == null) {
            Slog.e(TAG, "Failed to find provider info for " + name);
            return null;
        }

        // Install provider will increment the reference count for us, and break
        // any ties in the race.
        holder = installProvider(c, holder, holder.info,
                true /*noisy*/, holder.noReleaseNeeded, stable);
        return holder.provider;
    }

    private final void incProviderRefLocked(ProviderRefCount prc, boolean stable) {
        if (stable) {
            prc.stableCount += 1;
            if (prc.stableCount == 1) {
                // We are acquiring a new stable reference on the provider.
                int unstableDelta;
                if (prc.removePending) {
                    // We have a pending remove operation, which is holding the
                    // last unstable reference.  At this point we are converting
                    // that unstable reference to our new stable reference.
                    unstableDelta = -1;
                    // Cancel the removal of the provider.
                    if (DEBUG_PROVIDER) {
                        Slog.v(TAG, "incProviderRef: stable "
                                + "snatched provider from the jaws of death");
                    }
                    prc.removePending = false;
                    mH.removeMessages(H.REMOVE_PROVIDER, prc);
                } else {
                    unstableDelta = 0;
                }
                try {
                    if (DEBUG_PROVIDER) {
                        Slog.v(TAG, "incProviderRef Now stable - "
                                + prc.holder.info.name + ": unstableDelta="
                                + unstableDelta);
                    }
                    ActivityManagerNative.getDefault().refContentProvider(
                            prc.holder.connection, 1, unstableDelta);
                } catch (RemoteException e) {
                    //do nothing content provider object is dead any way
                }
            }
        } else {
            prc.unstableCount += 1;
            if (prc.unstableCount == 1) {
                // We are acquiring a new unstable reference on the provider.
                if (prc.removePending) {
                    // Oh look, we actually have a remove pending for the
                    // provider, which is still holding the last unstable
                    // reference.  We just need to cancel that to take new
                    // ownership of the reference.
                    if (DEBUG_PROVIDER) {
                        Slog.v(TAG, "incProviderRef: unstable "
                                + "snatched provider from the jaws of death");
                    }
                    prc.removePending = false;
                    mH.removeMessages(H.REMOVE_PROVIDER, prc);
                } else {
                    // First unstable ref, increment our count in the
                    // activity manager.
                    try {
                        if (DEBUG_PROVIDER) {
                            Slog.v(TAG, "incProviderRef: Now unstable - "
                                    + prc.holder.info.name);
                        }
                        ActivityManagerNative.getDefault().refContentProvider(
                                prc.holder.connection, 0, 1);
                    } catch (RemoteException e) {
                        //do nothing content provider object is dead any way
                    }
                }
            }
        }
    }

    public final IContentProvider acquireExistingProvider(Context c, String name,
            boolean stable) {
        synchronized (mProviderMap) {
            ProviderClientRecord pr = mProviderMap.get(name);
            if (pr == null) {
                return null;
            }

            IContentProvider provider = pr.mProvider;
            IBinder jBinder = provider.asBinder();

            // Only increment the ref count if we have one.  If we don't then the
            // provider is not reference counted and never needs to be released.
            ProviderRefCount prc = mProviderRefCountMap.get(jBinder);
            if (prc != null) {
                incProviderRefLocked(prc, stable);
            }
            return provider;
        }
    }

    public final boolean releaseProvider(IContentProvider provider, boolean stable) {
        if (provider == null) {
            return false;
        }

        IBinder jBinder = provider.asBinder();
        synchronized (mProviderMap) {
            ProviderRefCount prc = mProviderRefCountMap.get(jBinder);
            if (prc == null) {
                // The provider has no ref count, no release is needed.
                return false;
            }

            boolean lastRef = false;
            if (stable) {
                if (prc.stableCount == 0) {
                    if (DEBUG_PROVIDER) Slog.v(TAG,
                            "releaseProvider: stable ref count already 0, how?");
                    return false;
                }
                prc.stableCount -= 1;
                if (prc.stableCount == 0) {
                    // What we do at this point depends on whether there are
                    // any unstable refs left: if there are, we just tell the
                    // activity manager to decrement its stable count; if there
                    // aren't, we need to enqueue this provider to be removed,
                    // and convert to holding a single unstable ref while
                    // doing so.
                    lastRef = prc.unstableCount == 0;
                    try {
                        if (DEBUG_PROVIDER) {
                            Slog.v(TAG, "releaseProvider: No longer stable w/lastRef="
                                    + lastRef + " - " + prc.holder.info.name);
                        }
                        ActivityManagerNative.getDefault().refContentProvider(
                                prc.holder.connection, -1, lastRef ? 1 : 0);
                    } catch (RemoteException e) {
                        //do nothing content provider object is dead any way
                    }
                }
            } else {
                if (prc.unstableCount == 0) {
                    if (DEBUG_PROVIDER) Slog.v(TAG,
                            "releaseProvider: unstable ref count already 0, how?");
                    return false;
                }
                prc.unstableCount -= 1;
                if (prc.unstableCount == 0) {
                    // If this is the last reference, we need to enqueue
                    // this provider to be removed instead of telling the
                    // activity manager to remove it at this point.
                    lastRef = prc.stableCount == 0;
                    if (!lastRef) {
                        try {
                            if (DEBUG_PROVIDER) {
                                Slog.v(TAG, "releaseProvider: No longer unstable - "
                                        + prc.holder.info.name);
                            }
                            ActivityManagerNative.getDefault().refContentProvider(
                                    prc.holder.connection, 0, -1);
                        } catch (RemoteException e) {
                            //do nothing content provider object is dead any way
                        }
                    }
                }
            }

            if (lastRef) {
                if (!prc.removePending) {
                    // Schedule the actual remove asynchronously, since we don't know the context
                    // this will be called in.
                    // TODO: it would be nice to post a delayed message, so
                    // if we come back and need the same provider quickly
                    // we will still have it available.
                    if (DEBUG_PROVIDER) {
                        Slog.v(TAG, "releaseProvider: Enqueueing pending removal - "
                                + prc.holder.info.name);
                    }
                    prc.removePending = true;
                    Message msg = mH.obtainMessage(H.REMOVE_PROVIDER, prc);
                    mH.sendMessage(msg);
                } else {
                    Slog.w(TAG, "Duplicate remove pending of provider " + prc.holder.info.name);
                }
            }
            return true;
        }
    }

    final void completeRemoveProvider(ProviderRefCount prc) {
        synchronized (mProviderMap) {
            if (!prc.removePending) {
                // There was a race!  Some other client managed to acquire
                // the provider before the removal was completed.
                // Abort the removal.  We will do it later.
                if (DEBUG_PROVIDER) Slog.v(TAG, "completeRemoveProvider: lost the race, "
                        + "provider still in use");
                return;
            }

            final IBinder jBinder = prc.holder.provider.asBinder();
            ProviderRefCount existingPrc = mProviderRefCountMap.get(jBinder);
            if (existingPrc == prc) {
                mProviderRefCountMap.remove(jBinder);
            }

            Iterator<ProviderClientRecord> iter = mProviderMap.values().iterator();
            while (iter.hasNext()) {
                ProviderClientRecord pr = iter.next();
                IBinder myBinder = pr.mProvider.asBinder();
                if (myBinder == jBinder) {
                    iter.remove();
                }
            }
        }

        try {
            if (DEBUG_PROVIDER) {
                Slog.v(TAG, "removeProvider: Invoking ActivityManagerNative."
                        + "removeContentProvider(" + prc.holder.info.name + ")");
            }
            ActivityManagerNative.getDefault().removeContentProvider(
                    prc.holder.connection, false);
        } catch (RemoteException e) {
            //do nothing content provider object is dead any way
        }
    }

    final void handleUnstableProviderDied(IBinder provider, boolean fromClient) {
        synchronized(mProviderMap) {
            ProviderRefCount prc = mProviderRefCountMap.get(provider);
            if (prc != null) {
                if (DEBUG_PROVIDER) Slog.v(TAG, "Cleaning up dead provider "
                        + provider + " " + prc.holder.info.name);
                mProviderRefCountMap.remove(provider);
                if (prc.client != null && prc.client.mNames != null) {
                    for (String name : prc.client.mNames) {
                        ProviderClientRecord pr = mProviderMap.get(name);
                        if (pr != null && pr.mProvider.asBinder() == provider) {
                            Slog.i(TAG, "Removing dead content provider: " + name);
                            mProviderMap.remove(name);
                        }
                    }
                }
                if (fromClient) {
                    // We found out about this due to execution in our client
                    // code.  Tell the activity manager about it now, to ensure
                    // that the next time we go to do anything with the provider
                    // it knows it is dead (so we don't race with its death
                    // notification).
                    try {
                        ActivityManagerNative.getDefault().unstableProviderDied(
                                prc.holder.connection);
                    } catch (RemoteException e) {
                        //do nothing content provider object is dead any way
                    }
                }
=======
    private final IContentProvider getProvider(Context context, String name) {
        synchronized(mProviderMap) {
            final ProviderRecord pr = mProviderMap.get(name);
            if (pr != null) {
                return pr.mProvider;
            }
        }

        IActivityManager.ContentProviderHolder holder = null;
        try {
            holder = ActivityManagerNative.getDefault().getContentProvider(
                getApplicationThread(), name);
        } catch (RemoteException ex) {
        }
        if (holder == null) {
            Log.e(TAG, "Failed to find provider info for " + name);
            return null;
        }
        if (holder.permissionFailure != null) {
            throw new SecurityException("Permission " + holder.permissionFailure
                    + " required for provider " + name);
        }

        IContentProvider prov = installProvider(context, holder.provider,
                holder.info, true);
        //Log.i(TAG, "noReleaseNeeded=" + holder.noReleaseNeeded);
        if (holder.noReleaseNeeded || holder.provider == null) {
            // We are not going to release the provider if it is an external
            // provider that doesn't care about being released, or if it is
            // a local provider running in this process.
            //Log.i(TAG, "*** NO RELEASE NEEDED");
            synchronized(mProviderMap) {
                mProviderRefCountMap.put(prov.asBinder(), new ProviderRefCount(10000));
            }
        }
        return prov;
    }

    public final IContentProvider acquireProvider(Context c, String name) {
        IContentProvider provider = getProvider(c, name);
        if(provider == null)
            return null;
        IBinder jBinder = provider.asBinder();
        synchronized(mProviderMap) {
            ProviderRefCount prc = mProviderRefCountMap.get(jBinder);
            if(prc == null) {
                mProviderRefCountMap.put(jBinder, new ProviderRefCount(1));
            } else {
                prc.count++;
            } //end else
        } //end synchronized
        return provider;
    }

    public final boolean releaseProvider(IContentProvider provider) {
        if(provider == null) {
            return false;
        }
        IBinder jBinder = provider.asBinder();
        synchronized(mProviderMap) {
            ProviderRefCount prc = mProviderRefCountMap.get(jBinder);
            if(prc == null) {
                if(localLOGV) Log.v(TAG, "releaseProvider::Weird shouldnt be here");
                return false;
            } else {
                prc.count--;
                if(prc.count == 0) {
                    mProviderRefCountMap.remove(jBinder);
                    //invoke removeProvider to dereference provider
                    removeProviderLocked(provider);
                } //end if
            } //end else
        } //end synchronized
        return true;
    }

    public final void removeProviderLocked(IContentProvider provider) {
        if (provider == null) {
            return;
        }
        IBinder providerBinder = provider.asBinder();
        boolean amRemoveFlag = false;

        // remove the provider from mProviderMap
        Iterator<ProviderRecord> iter = mProviderMap.values().iterator();
        while (iter.hasNext()) {
            ProviderRecord pr = iter.next();
            IBinder myBinder = pr.mProvider.asBinder();
            if (myBinder == providerBinder) {
                //find if its published by this process itself
                if(pr.mLocalProvider != null) {
                    if(localLOGV) Log.i(TAG, "removeProvider::found local provider returning");
                    return;
                }
                if(localLOGV) Log.v(TAG, "removeProvider::Not local provider Unlinking " +
                        "death recipient");
                //content provider is in another process
                myBinder.unlinkToDeath(pr, 0);
                iter.remove();
                //invoke remove only once for the very first name seen
                if(!amRemoveFlag) {
                    try {
                        if(localLOGV) Log.v(TAG, "removeProvider::Invoking " +
                                "ActivityManagerNative.removeContentProvider("+pr.mName);
                        ActivityManagerNative.getDefault().removeContentProvider(getApplicationThread(), pr.mName);
                        amRemoveFlag = true;
                    } catch (RemoteException e) {
                        //do nothing content provider object is dead any way
                    } //end catch
                }
            } //end if myBinder
        }  //end while iter
    }

    final void removeDeadProvider(String name, IContentProvider provider) {
        synchronized(mProviderMap) {
            ProviderRecord pr = mProviderMap.get(name);
            if (pr.mProvider.asBinder() == provider.asBinder()) {
                Log.i(TAG, "Removing dead content provider: " + name);
                mProviderMap.remove(name);
>>>>>>> 54b6cfa... Initial Contribution
            }
        }
    }

<<<<<<< HEAD
    private ProviderClientRecord installProviderAuthoritiesLocked(IContentProvider provider,
            ContentProvider localProvider,IActivityManager.ContentProviderHolder holder) {
        String names[] = PATTERN_SEMICOLON.split(holder.info.authority);
        ProviderClientRecord pcr = new ProviderClientRecord(names, provider,
                localProvider, holder);
        for (int i = 0; i < names.length; i++) {
            ProviderClientRecord existing = mProviderMap.get(names[i]);
            if (existing != null) {
                Slog.w(TAG, "Content provider " + pcr.mHolder.info.name
                        + " already published as " + names[i]);
            } else {
                mProviderMap.put(names[i], pcr);
            }
        }
        return pcr;
    }

    /**
     * Installs the provider.
     *
     * Providers that are local to the process or that come from the system server
     * may be installed permanently which is indicated by setting noReleaseNeeded to true.
     * Other remote providers are reference counted.  The initial reference count
     * for all reference counted providers is one.  Providers that are not reference
     * counted do not have a reference count (at all).
     *
     * This method detects when a provider has already been installed.  When this happens,
     * it increments the reference count of the existing provider (if appropriate)
     * and returns the existing provider.  This can happen due to concurrent
     * attempts to acquire the same provider.
     */
    private IActivityManager.ContentProviderHolder installProvider(Context context,
            IActivityManager.ContentProviderHolder holder, ProviderInfo info,
            boolean noisy, boolean noReleaseNeeded, boolean stable) {
        ContentProvider localProvider = null;
        IContentProvider provider;
        if (holder == null || holder.provider == null) {
            if (DEBUG_PROVIDER || noisy) {
                Slog.d(TAG, "Loading provider " + info.authority + ": "
=======
    final void removeDeadProviderLocked(String name, IContentProvider provider) {
        ProviderRecord pr = mProviderMap.get(name);
        if (pr.mProvider.asBinder() == provider.asBinder()) {
            Log.i(TAG, "Removing dead content provider: " + name);
            mProviderMap.remove(name);
        }
    }

    private final IContentProvider installProvider(Context context,
            IContentProvider provider, ProviderInfo info, boolean noisy) {
        ContentProvider localProvider = null;
        if (provider == null) {
            if (noisy) {
                Log.d(TAG, "Loading provider " + info.authority + ": "
>>>>>>> 54b6cfa... Initial Contribution
                        + info.name);
            }
            Context c = null;
            ApplicationInfo ai = info.applicationInfo;
            if (context.getPackageName().equals(ai.packageName)) {
                c = context;
            } else if (mInitialApplication != null &&
                    mInitialApplication.getPackageName().equals(ai.packageName)) {
                c = mInitialApplication;
            } else {
                try {
                    c = context.createPackageContext(ai.packageName,
                            Context.CONTEXT_INCLUDE_CODE);
                } catch (PackageManager.NameNotFoundException e) {
<<<<<<< HEAD
                    // Ignore
                }
            }
            if (c == null) {
                Slog.w(TAG, "Unable to get context for package " +
=======
                }
            }
            if (c == null) {
                Log.w(TAG, "Unable to get context for package " +
>>>>>>> 54b6cfa... Initial Contribution
                      ai.packageName +
                      " while loading content provider " +
                      info.name);
                return null;
            }
            try {
                final java.lang.ClassLoader cl = c.getClassLoader();
                localProvider = (ContentProvider)cl.
                    loadClass(info.name).newInstance();
                provider = localProvider.getIContentProvider();
                if (provider == null) {
<<<<<<< HEAD
                    Slog.e(TAG, "Failed to instantiate class " +
=======
                    Log.e(TAG, "Failed to instantiate class " +
>>>>>>> 54b6cfa... Initial Contribution
                          info.name + " from sourceDir " +
                          info.applicationInfo.sourceDir);
                    return null;
                }
<<<<<<< HEAD
                if (DEBUG_PROVIDER) Slog.v(
=======
                if (Config.LOGV) Log.v(
>>>>>>> 54b6cfa... Initial Contribution
                    TAG, "Instantiating local provider " + info.name);
                // XXX Need to create the correct context for this provider.
                localProvider.attachInfo(c, info);
            } catch (java.lang.Exception e) {
                if (!mInstrumentation.onException(null, e)) {
                    throw new RuntimeException(
                            "Unable to get provider " + info.name
                            + ": " + e.toString(), e);
                }
                return null;
            }
<<<<<<< HEAD
        } else {
            provider = holder.provider;
            if (DEBUG_PROVIDER) Slog.v(TAG, "Installing external provider " + info.authority + ": "
                    + info.name);
        }

        IActivityManager.ContentProviderHolder retHolder;

        synchronized (mProviderMap) {
            if (DEBUG_PROVIDER) Slog.v(TAG, "Checking to add " + provider
                    + " / " + info.name);
            IBinder jBinder = provider.asBinder();
            if (localProvider != null) {
                ComponentName cname = new ComponentName(info.packageName, info.name);
                ProviderClientRecord pr = mLocalProvidersByName.get(cname);
                if (pr != null) {
                    if (DEBUG_PROVIDER) {
                        Slog.v(TAG, "installProvider: lost the race, "
                                + "using existing local provider");
                    }
                    provider = pr.mProvider;
                } else {
                    holder = new IActivityManager.ContentProviderHolder(info);
                    holder.provider = provider;
                    holder.noReleaseNeeded = true;
                    pr = installProviderAuthoritiesLocked(provider, localProvider, holder);
                    mLocalProviders.put(jBinder, pr);
                    mLocalProvidersByName.put(cname, pr);
                }
                retHolder = pr.mHolder;
            } else {
                ProviderRefCount prc = mProviderRefCountMap.get(jBinder);
                if (prc != null) {
                    if (DEBUG_PROVIDER) {
                        Slog.v(TAG, "installProvider: lost the race, updating ref count");
                    }
                    // We need to transfer our new reference to the existing
                    // ref count, releasing the old one...  but only if
                    // release is needed (that is, it is not running in the
                    // system process).
                    if (!noReleaseNeeded) {
                        incProviderRefLocked(prc, stable);
                        try {
                            ActivityManagerNative.getDefault().removeContentProvider(
                                    holder.connection, stable);
                        } catch (RemoteException e) {
                            //do nothing content provider object is dead any way
                        }
                    }
                } else {
                    ProviderClientRecord client = installProviderAuthoritiesLocked(
                            provider, localProvider, holder);
                    if (noReleaseNeeded) {
                        prc = new ProviderRefCount(holder, client, 1000, 1000);
                    } else {
                        prc = stable
                                ? new ProviderRefCount(holder, client, 1, 0)
                                : new ProviderRefCount(holder, client, 0, 1);
                    }
                    mProviderRefCountMap.put(jBinder, prc);
                }
                retHolder = prc.holder;
            }
        }

        return retHolder;
    }

    private void attach(boolean system) {
        sThreadLocal.set(this);
        mSystemThread = system;
        if (!system) {
            ViewRootImpl.addFirstDrawHandler(new Runnable() {
                public void run() {
                    ensureJitEnabled();
                }
            });
=======
        } else if (localLOGV) {
            Log.v(TAG, "Installing external provider " + info.authority + ": "
                    + info.name);
        }

        synchronized (mProviderMap) {
            // Cache the pointer for the remote provider.
            String names[] = PATTERN_SEMICOLON.split(info.authority);
            for (int i=0; i<names.length; i++) {
                ProviderRecord pr = new ProviderRecord(names[i], provider,
                        localProvider);
                try {
                    provider.asBinder().linkToDeath(pr, 0);
                    mProviderMap.put(names[i], pr);
                } catch (RemoteException e) {
                    return null;
                }
            }
            if (localProvider != null) {
                mLocalProviders.put(provider.asBinder(),
                        new ProviderRecord(null, provider, localProvider));
            }
        }

        return provider;
    }

    private final void attach(boolean system) {
        sThreadLocal.set(this);
        mSystemThread = system;
        AndroidHttpClient.setThreadBlocked(true);
        if (!system) {
>>>>>>> 54b6cfa... Initial Contribution
            android.ddm.DdmHandleAppName.setAppName("<pre-initialized>");
            RuntimeInit.setApplicationObject(mAppThread.asBinder());
            IActivityManager mgr = ActivityManagerNative.getDefault();
            try {
                mgr.attachApplication(mAppThread);
            } catch (RemoteException ex) {
<<<<<<< HEAD
                // Ignore
=======
>>>>>>> 54b6cfa... Initial Contribution
            }
        } else {
            // Don't set application object here -- if the system crashes,
            // we can't display an alert, we just want to die die die.
            android.ddm.DdmHandleAppName.setAppName("system_process");
            try {
                mInstrumentation = new Instrumentation();
<<<<<<< HEAD
                ContextImpl context = new ContextImpl();
=======
                ApplicationContext context = new ApplicationContext();
>>>>>>> 54b6cfa... Initial Contribution
                context.init(getSystemContext().mPackageInfo, null, this);
                Application app = Instrumentation.newApplication(Application.class, context);
                mAllApplications.add(app);
                mInitialApplication = app;
                app.onCreate();
            } catch (Exception e) {
                throw new RuntimeException(
                        "Unable to instantiate Application():" + e.toString(), e);
            }
        }
<<<<<<< HEAD
        
        ViewRootImpl.addConfigCallback(new ComponentCallbacks2() {
            public void onConfigurationChanged(Configuration newConfig) {
                synchronized (mPackages) {
                    // We need to apply this change to the resources
                    // immediately, because upon returning the view
                    // hierarchy will be informed about it.
                    if (applyConfigurationToResourcesLocked(newConfig, null)) {
                        // This actually changed the resources!  Tell
                        // everyone about it.
                        if (mPendingConfiguration == null ||
                                mPendingConfiguration.isOtherSeqNewer(newConfig)) {
                            mPendingConfiguration = newConfig;
                            
                            queueOrSendMessage(H.CONFIGURATION_CHANGED, newConfig);
                        }
                    }
                }
            }
            public void onLowMemory() {
            }
            public void onTrimMemory(int level) {
            }
        });
    }

    public static ActivityThread systemMain() {
        HardwareRenderer.disable(true);
=======
    }

    private final void detach()
    {
        AndroidHttpClient.setThreadBlocked(false);
        sThreadLocal.set(null);
    }

    public static final ActivityThread systemMain() {
>>>>>>> 54b6cfa... Initial Contribution
        ActivityThread thread = new ActivityThread();
        thread.attach(true);
        return thread;
    }

<<<<<<< HEAD
    public final void installSystemProviders(List<ProviderInfo> providers) {
        if (providers != null) {
            installContentProviders(mInitialApplication, providers);
        }
    }

    public int getIntCoreSetting(String key, int defaultValue) {
        synchronized (mPackages) {
            if (mCoreSettings != null) {
                return mCoreSettings.getInt(key, defaultValue);
            } else {
                return defaultValue;
            }
        }
    }

    public static void main(String[] args) {
        SamplingProfilerIntegration.start();

        // CloseGuard defaults to true and can be quite spammy.  We
        // disable it here, but selectively enable it later (via
        // StrictMode) on debug builds, but using DropBox, not logs.
        CloseGuard.setEnabled(false);

        Process.setArgV0("<pre-initialized>");

        Looper.prepareMainLooper();
        if (sMainThreadHandler == null) {
            sMainThreadHandler = new Handler();
        }
=======
    public final void installSystemProviders(List providers) {
        if (providers != null) {
            installContentProviders(mInitialApplication,
                                    (List<ProviderInfo>)providers);
        }
    }

    public static final void main(String[] args) {
        Process.setArgV0("<pre-initialized>");

        Looper.prepareMainLooper();
>>>>>>> 54b6cfa... Initial Contribution

        ActivityThread thread = new ActivityThread();
        thread.attach(false);

<<<<<<< HEAD
        AsyncTask.init();

        if (false) {
            Looper.myLooper().setMessageLogging(new
                    LogPrinter(Log.DEBUG, "ActivityThread"));
        }

        Looper.loop();

        throw new RuntimeException("Main thread loop unexpectedly exited");
=======
        Looper.loop();

        if (Process.supportsProcesses()) {
            throw new RuntimeException("Main thread loop unexpectedly exited");
        }

        thread.detach();
        String name;
        if (thread.mInitialApplication != null) name = thread.mInitialApplication.getPackageName();
        else name = "<unknown>";
        Log.i(TAG, "Main thread of " + name + " is now exiting");
>>>>>>> 54b6cfa... Initial Contribution
    }
}
