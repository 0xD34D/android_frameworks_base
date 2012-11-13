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

package com.android.internal.os;

import android.app.ActivityManagerNative;
<<<<<<< HEAD
import android.app.ApplicationErrorReport;
import android.os.Build;
import android.os.Debug;
import android.os.IBinder;
import android.os.Process;
import android.os.SystemProperties;
import android.util.Log;
import android.util.Slog;
import com.android.internal.logging.AndroidConfig;
import com.android.server.NetworkManagementSocketTagger;
import dalvik.system.VMRuntime;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.TimeZone;
import java.util.logging.LogManager;
=======
import android.app.IActivityManager;
import android.os.Debug;
import android.os.IBinder;
import android.os.ICheckinService;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.server.data.CrashData;
import android.util.Config;
import android.util.Log;

import com.android.internal.logging.AndroidConfig;

import dalvik.system.VMRuntime;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.LogManager;
import java.util.TimeZone;

>>>>>>> 54b6cfa... Initial Contribution
import org.apache.harmony.luni.internal.util.TimezoneGetter;

/**
 * Main entry point for runtime initialization.  Not for
 * public consumption.
 * @hide
 */
public class RuntimeInit {
    private final static String TAG = "AndroidRuntime";
<<<<<<< HEAD
    private final static boolean DEBUG = false;
=======
>>>>>>> 54b6cfa... Initial Contribution

    /** true if commonInit() has been called */
    private static boolean initialized;

<<<<<<< HEAD
    private static IBinder mApplicationObject;

    private static volatile boolean mCrashing = false;

    private static final native void nativeZygoteInit();
    private static final native void nativeFinishInit();
    private static final native void nativeSetExitWithoutCleanup(boolean exitWithoutCleanup);

=======
>>>>>>> 54b6cfa... Initial Contribution
    /**
     * Use this to log a message when a thread exits due to an uncaught
     * exception.  The framework catches these for the main threads, so
     * this should only matter for threads created by applications.
     */
    private static class UncaughtHandler implements Thread.UncaughtExceptionHandler {
        public void uncaughtException(Thread t, Throwable e) {
            try {
<<<<<<< HEAD
                // Don't re-enter -- avoid infinite loops if crash-reporting crashes.
                if (mCrashing) return;
                mCrashing = true;

                if (mApplicationObject == null) {
                    Slog.e(TAG, "*** FATAL EXCEPTION IN SYSTEM PROCESS: " + t.getName(), e);
                } else {
                    Slog.e(TAG, "FATAL EXCEPTION: " + t.getName(), e);
                }

                // Bring up crash dialog, wait for it to be dismissed
                ActivityManagerNative.getDefault().handleApplicationCrash(
                        mApplicationObject, new ApplicationErrorReport.CrashInfo(e));
            } catch (Throwable t2) {
                try {
                    Slog.e(TAG, "Error reporting crash", t2);
                } catch (Throwable t3) {
                    // Even Slog.e() fails!  Oh well.
                }
            } finally {
                // Try everything to make sure this process goes away.
                Process.killProcess(Process.myPid());
                System.exit(10);
            }
=======
                Log.e(TAG, "Uncaught handler: thread " + t.getName()
                        + " exiting due to uncaught exception");
            } catch (Throwable error) {
                // Ignore the throwable, since we're in the process of crashing anyway.
                // If we don't, the crash won't happen properly and the process will
                // be left around in a bad state.
            }
            crash(TAG, e);
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    private static final void commonInit() {
<<<<<<< HEAD
        if (DEBUG) Slog.d(TAG, "Entered RuntimeInit!");
=======
        if (Config.LOGV) Log.d(TAG, "Entered RuntimeInit!");
>>>>>>> 54b6cfa... Initial Contribution

        /* set default handler; this applies to all threads in the VM */
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtHandler());

<<<<<<< HEAD
=======
        int hasQwerty = getQwertyKeyboard();
        
        if (Config.LOGV) Log.d(TAG, ">>>>> qwerty keyboard = " + hasQwerty);
        if (hasQwerty == 1) {
            System.setProperty("qwerty", "1");
        }

>>>>>>> 54b6cfa... Initial Contribution
        /*
         * Install a TimezoneGetter subclass for ZoneInfo.db
         */
        TimezoneGetter.setInstance(new TimezoneGetter() {
            @Override
            public String getId() {
                return SystemProperties.get("persist.sys.timezone");
            }
        });
        TimeZone.setDefault(null);

        /*
         * Sets handler for java.util.logging to use Android log facilities.
         * The odd "new instance-and-then-throw-away" is a mirror of how
         * the "java.util.logging.config.class" system property works. We
         * can't use the system property here since the logger has almost
         * certainly already been initialized.
         */
        LogManager.getLogManager().reset();
        new AndroidConfig();

        /*
<<<<<<< HEAD
         * Sets the default HTTP User-Agent used by HttpURLConnection.
         */
        String userAgent = getDefaultUserAgent();
        System.setProperty("http.agent", userAgent);

        /*
         * Wire socket tagging to traffic stats.
         */
        NetworkManagementSocketTagger.install();

        /*
=======
>>>>>>> 54b6cfa... Initial Contribution
         * If we're running in an emulator launched with "-trace", put the
         * VM into emulator trace profiling mode so that the user can hit
         * F9/F10 at any time to capture traces.  This has performance
         * consequences, so it's not something you want to do always.
         */
        String trace = SystemProperties.get("ro.kernel.android.tracing");
        if (trace.equals("1")) {
<<<<<<< HEAD
            Slog.i(TAG, "NOTE: emulator trace profiling enabled");
=======
            Log.i(TAG, "NOTE: emulator trace profiling enabled");
>>>>>>> 54b6cfa... Initial Contribution
            Debug.enableEmulatorTraceOutput();
        }

        initialized = true;
    }

    /**
<<<<<<< HEAD
     * Returns an HTTP user agent of the form
     * "Dalvik/1.1.0 (Linux; U; Android Eclair Build/MASTER)".
     */
    private static String getDefaultUserAgent() {
        StringBuilder result = new StringBuilder(64);
        result.append("Dalvik/");
        result.append(System.getProperty("java.vm.version")); // such as 1.1.0
        result.append(" (Linux; U; Android ");

        String version = Build.VERSION.RELEASE; // "1.0" or "3.4b5"
        result.append(version.length() > 0 ? version : "1.0");

        // add the model for the release build
        if ("REL".equals(Build.VERSION.CODENAME)) {
            String model = Build.MODEL;
            if (model.length() > 0) {
                result.append("; ");
                result.append(model);
            }
        }
        String id = Build.ID; // "MASTER" or "M4-rc20"
        if (id.length() > 0) {
            result.append(" Build/");
            result.append(id);
        }
        result.append(")");
        return result.toString();
    }

    /**
=======
>>>>>>> 54b6cfa... Initial Contribution
     * Invokes a static "main(argv[]) method on class "className".
     * Converts various failing exceptions into RuntimeExceptions, with
     * the assumption that they will then cause the VM instance to exit.
     *
     * @param className Fully-qualified class name
     * @param argv Argument vector for main()
     */
<<<<<<< HEAD
    private static void invokeStaticMain(String className, String[] argv)
            throws ZygoteInit.MethodAndArgsCaller {
=======
    private static void invokeStaticMain(String className, String[] argv) 
            throws ZygoteInit.MethodAndArgsCaller {
        
        // We want to be fairly aggressive about heap utilization, to avoid
        // holding on to a lot of memory that isn't needed.
        VMRuntime.getRuntime().setTargetHeapUtilization(0.75f);
        
>>>>>>> 54b6cfa... Initial Contribution
        Class<?> cl;

        try {
            cl = Class.forName(className);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(
                    "Missing class when invoking static main " + className,
                    ex);
        }

        Method m;
        try {
            m = cl.getMethod("main", new Class[] { String[].class });
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(
                    "Missing static main on " + className, ex);
        } catch (SecurityException ex) {
            throw new RuntimeException(
                    "Problem getting static main on " + className, ex);
        }

        int modifiers = m.getModifiers();
        if (! (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers))) {
            throw new RuntimeException(
                    "Main method is not public and static on " + className);
        }

        /*
         * This throw gets caught in ZygoteInit.main(), which responds
         * by invoking the exception's run() method. This arrangement
         * clears up all the stack frames that were required in setting
         * up the process.
         */
        throw new ZygoteInit.MethodAndArgsCaller(m, argv);
    }

    public static final void main(String[] argv) {
<<<<<<< HEAD
        if (argv.length == 2 && argv[1].equals("application")) {
            if (DEBUG) Slog.d(TAG, "RuntimeInit: Starting application");
            redirectLogStreams();
        } else {
            if (DEBUG) Slog.d(TAG, "RuntimeInit: Starting tool");
        }

        commonInit();

=======
        commonInit();
        
>>>>>>> 54b6cfa... Initial Contribution
        /*
         * Now that we're running in interpreted code, call back into native code
         * to run the system.
         */
<<<<<<< HEAD
        nativeFinishInit();

        if (DEBUG) Slog.d(TAG, "Leaving RuntimeInit!");
    }

    /**
     * The main function called when started through the zygote process. This
     * could be unified with main(), if the native code in nativeFinishInit()
=======
        finishInit();

        if (Config.LOGV) Log.d(TAG, "Leaving RuntimeInit!");
    }
    
    public static final native void finishInit();

    /**
     * The main function called when started through the zygote process. This
     * could be unified with main(), if the native code in finishInit()
>>>>>>> 54b6cfa... Initial Contribution
     * were rationalized with Zygote startup.<p>
     *
     * Current recognized args:
     * <ul>
<<<<<<< HEAD
     *   <li> <code> [--] &lt;start class name&gt;  &lt;args&gt;
     * </ul>
     *
     * @param targetSdkVersion target SDK version
     * @param argv arg strings
     */
    public static final void zygoteInit(int targetSdkVersion, String[] argv)
            throws ZygoteInit.MethodAndArgsCaller {
        if (DEBUG) Slog.d(TAG, "RuntimeInit: Starting application from zygote");

        redirectLogStreams();

        commonInit();
        nativeZygoteInit();

        applicationInit(targetSdkVersion, argv);
    }

    /**
     * The main function called when an application is started through a
     * wrapper process.
     *
     * When the wrapper starts, the runtime starts {@link RuntimeInit#main}
     * which calls {@link WrapperInit#main} which then calls this method.
     * So we don't need to call commonInit() here.
     *
     * @param targetSdkVersion target SDK version
     * @param argv arg strings
     */
    public static void wrapperInit(int targetSdkVersion, String[] argv)
            throws ZygoteInit.MethodAndArgsCaller {
        if (DEBUG) Slog.d(TAG, "RuntimeInit: Starting application from wrapper");

        applicationInit(targetSdkVersion, argv);
    }

    private static void applicationInit(int targetSdkVersion, String[] argv)
            throws ZygoteInit.MethodAndArgsCaller {
        // If the application calls System.exit(), terminate the process
        // immediately without running any shutdown hooks.  It is not possible to
        // shutdown an Android application gracefully.  Among other things, the
        // Android runtime shutdown hooks close the Binder driver, which can cause
        // leftover running threads to crash before the process actually exits.
        nativeSetExitWithoutCleanup(true);

        // We want to be fairly aggressive about heap utilization, to avoid
        // holding on to a lot of memory that isn't needed.
        VMRuntime.getRuntime().setTargetHeapUtilization(0.75f);
        VMRuntime.getRuntime().setTargetSdkVersion(targetSdkVersion);

        final Arguments args;
        try {
            args = new Arguments(argv);
        } catch (IllegalArgumentException ex) {
            Slog.e(TAG, ex.getMessage());
            // let the process exit
            return;
        }

        // Remaining arguments are passed to the start class's static main
        invokeStaticMain(args.startClass, args.startArgs);
    }

    /**
     * Redirect System.out and System.err to the Android log.
     */
    public static void redirectLogStreams() {
        System.out.close();
        System.setOut(new AndroidPrintStream(Log.INFO, "System.out"));
        System.err.close();
        System.setErr(new AndroidPrintStream(Log.WARN, "System.err"));
    }

    /**
     * Report a serious error in the current process.  May or may not cause
     * the process to terminate (depends on system settings).
     *
     * @param tag to record with the error
     * @param t exception describing the error site and conditions
     */
    public static void wtf(String tag, Throwable t) {
        try {
            if (ActivityManagerNative.getDefault().handleApplicationWtf(
                    mApplicationObject, tag, new ApplicationErrorReport.CrashInfo(t))) {
                // The Activity Manager has already written us off -- now exit.
                Process.killProcess(Process.myPid());
                System.exit(10);
            }
        } catch (Throwable t2) {
            Slog.e(TAG, "Error reporting WTF", t2);
=======
     *   <li> --nice-name=<i>nice name to appear in ps</i>
     *   <li> <code> [--] &lt;start class name&gt;  &lt;args&gt;
     * </ul>
     *
     * @param argv arg strings
     */
    public static final void zygoteInit(String[] argv)
            throws ZygoteInit.MethodAndArgsCaller {
        // TODO: Doing this here works, but it seems kind of arbitrary. Find
        // a better place. The goal is to set it up for applications, but not
        // tools like am.
        System.setOut(new AndroidPrintStream(Log.INFO, "System.out"));
        System.setErr(new AndroidPrintStream(Log.WARN, "System.err"));

        commonInit();
        zygoteInitNative();

        int curArg = 0;
        for ( /* curArg */ ; curArg < argv.length; curArg++) {
            String arg = argv[curArg];

            if (arg.equals("--")) {
                curArg++;
                break;
            } else if (!arg.startsWith("--")) {
                break;
            } else if (arg.startsWith("--nice-name=")) {
                String niceName = arg.substring(arg.indexOf('=') + 1);
                Process.setArgV0(niceName);
            }
        }

        if (curArg == argv.length) {
            Log.e(TAG, "Missing classname argument to RuntimeInit!");
            // let the process exit
            return;
        }

        // Remaining arguments are passed to the start class's static main
        
        String startClass = argv[curArg++];
        String[] startArgs = new String[argv.length - curArg];

        System.arraycopy(argv, curArg, startArgs, 0, startArgs.length);
        invokeStaticMain(startClass, startArgs);
    }

    public static final native void zygoteInitNative();
    
    /**
     * Returns 1 if the computer is on. If the computer isn't on, the value returned by this method is undefined.
     */
    public static final native int isComputerOn();

    /**
     * Turns the computer on if the computer is off. If the computer is on, the behavior of this method is undefined. 
     */
    public static final native void turnComputerOn();

    /**
     * 
     * @return 1 if the device has a qwerty keyboard
     */
    public static native int getQwertyKeyboard();
    
    /**
     * Report a fatal error in the current process.  If this is a user-process,
     * a dialog may be displayed informing the user of the error.  This
     * function does not return; it forces the current process to exit.
     * 
     * @param tag to use when logging the error
     * @param t exception that was generated by the error
     */
    public static void crash(String tag, Throwable t) {
        if (mApplicationObject != null) {
            byte[] crashData = null;
            try {
                // Log exception.
                Log.e(TAG, Log.getStackTraceString(t));
                crashData = marshallException(tag, t);
                if (crashData == null) {
                    throw new NullPointerException("Can't marshall crash data");
                }
            } catch (Throwable t2) {
                try {
                    // Log exception as a string so we don't get in an infinite loop.
                    Log.e(TAG, "Error reporting crash: "
                            + Log.getStackTraceString(t2));
                } catch (Throwable t3) {
                    // Do nothing, must be OOM so we can't format the message
                }
            }

            try {
                // Display user-visible error message.
                String msg = t.getMessage();
                if (msg == null) {
                    msg = t.toString();
                }

                IActivityManager am = ActivityManagerNative.getDefault();
                try {
                    int res = am.handleApplicationError(mApplicationObject,
                            0, tag, msg, t.toString(), crashData);
                    // Is waiting for the debugger the right thing?
                    // For now I have turned off the Debug button, because
                    // I'm not sure what we should do if it is actually
                    // selected.
                    //Log.i(TAG, "Got app error result: " + res);
                    if (res == 1) {
                        Debug.waitForDebugger();
                        return;
                    }
                } catch (RemoteException e) {
                }
            } catch (Throwable t2) {
                try {
                    // Log exception as a string so we don't get in an infinite loop.
                    Log.e(TAG, "Error reporting crash: "
                            + Log.getStackTraceString(t2));
                } catch (Throwable t3) {
                    // Do nothing, must be OOM so we can't format the message
                }
            } finally {
                // Try everything to make sure this process goes away.
                Process.killProcess(Process.myPid());
                System.exit(10);
            }
        } else {
            try {
                Log.e(TAG, "*** EXCEPTION IN SYSTEM PROCESS.  System will crash.");
                Log.e(tag, Log.getStackTraceString(t));
                reportException(tag, t, true);  // synchronous
            } catch (Throwable t2) {
                // Do nothing, must be OOM so we can't format the message
            } finally {
                // Try everything to make sure this process goes away.
                Process.killProcess(Process.myPid());
                System.exit(10);
            }
        }
    }

    /** Counter used to prevent reentrancy in {@link #reportException}. */
    private static final AtomicInteger sInReportException = new AtomicInteger();

    /**
     * Report an error in the current process.  The exception information will
     * be handed off to the checkin service and eventually uploaded for analysis.
     * This is expensive!  Only use this when the exception indicates a programming
     * error ("should not happen").
     *
     * @param tag to use when logging the error
     * @param t exception that was generated by the error
     * @param sync true to wait for the report, false to "fire and forget"
     */
    public static void reportException(String tag, Throwable t, boolean sync) {
        if (!initialized) {
            // Exceptions during, eg, zygote cannot use this mechanism
            return;
        }

        // It's important to prevent an infinite crash-reporting loop:
        // while this function is running, don't let it be called again.
        int reenter = sInReportException.getAndIncrement();
        if (reenter != 0) {
            sInReportException.decrementAndGet();
            Log.e(TAG, "Crash logging skipped, already logging another crash");
            return;
        }

        // TODO: Enable callers to specify a level (i.e. warn or error).
        try {
            // Submit crash data to statistics service.
            byte[] crashData = marshallException(tag, t);
            ICheckinService checkin = ICheckinService.Stub.asInterface(
                    ServiceManager.getService("checkin"));
            if (checkin == null) {
                Log.e(TAG, "Crash logging skipped, no checkin service");
            } else if (sync) {
                checkin.reportCrashSync(crashData);
            } else {
                checkin.reportCrashAsync(crashData);
            }
        } catch (Throwable t2) {
            // Log exception as a string so we don't get in an infinite loop.
            Log.e(TAG, "Crash logging failed: " + t2);
        } finally {
            sInReportException.decrementAndGet();
        }
    }

    private static byte[] marshallException(String tag, Throwable t) {
        // Convert crash data to bytes.
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(bout);
        try {
            new CrashData(tag, t).write(dout);
            dout.close();
        } catch (IOException e) {
            return null;
        }
        return bout.toByteArray();
    }

    /**
     * Replay an encoded CrashData record back into a useable CrashData record.  This can be
     * helpful for providing debugging output after a process error.
     * 
     * @param crashDataBytes The byte array containing the encoded crash record
     * @return new CrashData record, or null if could not create one.
     */
    public static CrashData unmarshallException(byte[] crashDataBytes) {
        try {
            ByteArrayInputStream bin = new ByteArrayInputStream(crashDataBytes);
            DataInputStream din = new DataInputStream(bin);
            return new CrashData(din);
        } catch (IOException e) {
            return null;
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
     * Set the object identifying this application/process, for reporting VM
     * errors.
     */
    public static final void setApplicationObject(IBinder app) {
        mApplicationObject = app;
    }

<<<<<<< HEAD
    public static final IBinder getApplicationObject() {
        return mApplicationObject;
    }

=======
>>>>>>> 54b6cfa... Initial Contribution
    /**
     * Enable debugging features.
     */
    static {
        // Register handlers for DDM messages.
        android.ddm.DdmRegister.registerHandlers();
    }

<<<<<<< HEAD
    /**
     * Handles argument parsing for args related to the runtime.
     *
     * Current recognized args:
     * <ul>
     *   <li> <code> [--] &lt;start class name&gt;  &lt;args&gt;
     * </ul>
     */
    static class Arguments {
        /** first non-option argument */
        String startClass;

        /** all following arguments */
        String[] startArgs;

        /**
         * Constructs instance and parses args
         * @param args runtime command-line args
         * @throws IllegalArgumentException
         */
        Arguments(String args[]) throws IllegalArgumentException {
            parseArgs(args);
        }

        /**
         * Parses the commandline arguments intended for the Runtime.
         */
        private void parseArgs(String args[])
                throws IllegalArgumentException {
            int curArg = 0;
            for (; curArg < args.length; curArg++) {
                String arg = args[curArg];

                if (arg.equals("--")) {
                    curArg++;
                    break;
                } else if (!arg.startsWith("--")) {
                    break;
                }
            }

            if (curArg == args.length) {
                throw new IllegalArgumentException("Missing classname argument to RuntimeInit!");
            }

            startClass = args[curArg++];
            startArgs = new String[args.length - curArg];
            System.arraycopy(args, curArg, startArgs, 0, startArgs.length);
        }
    }
=======
    private static IBinder mApplicationObject;

>>>>>>> 54b6cfa... Initial Contribution
}
