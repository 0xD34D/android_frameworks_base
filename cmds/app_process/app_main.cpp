/*
 * Main entry of app process.
<<<<<<< HEAD
 *
 * Starts the interpreted runtime, then starts up the application.
 *
=======
 * 
 * Starts the interpreted runtime, then starts up the application.
 * 
>>>>>>> 54b6cfa... Initial Contribution
 */

#define LOG_TAG "appproc"

<<<<<<< HEAD
#include <binder/IPCThreadState.h>
#include <binder/ProcessState.h>
=======
#include <utils/IPCThreadState.h>
#include <utils/ProcessState.h>
>>>>>>> 54b6cfa... Initial Contribution
#include <utils/Log.h>
#include <cutils/process_name.h>
#include <cutils/memory.h>
#include <android_runtime/AndroidRuntime.h>

#include <stdio.h>
#include <unistd.h>

namespace android {

void app_usage()
{
    fprintf(stderr,
        "Usage: app_process [java-options] cmd-dir start-class-name [options]\n");
}

<<<<<<< HEAD
=======
status_t app_init(const char* className, int argc, const char* const argv[])
{
    LOGV("Entered app_init()!\n");

    AndroidRuntime* jr = AndroidRuntime::getRuntime();
    jr->callMain(className, argc, argv);
    
    LOGV("Exiting app_init()!\n");
    return NO_ERROR;
}

>>>>>>> 54b6cfa... Initial Contribution
class AppRuntime : public AndroidRuntime
{
public:
    AppRuntime()
        : mParentDir(NULL)
        , mClassName(NULL)
<<<<<<< HEAD
        , mClass(NULL)
=======
>>>>>>> 54b6cfa... Initial Contribution
        , mArgC(0)
        , mArgV(NULL)
    {
    }

#if 0
    // this appears to be unused
    const char* getParentDir() const
    {
        return mParentDir;
    }
#endif

    const char* getClassName() const
    {
        return mClassName;
    }

<<<<<<< HEAD
    virtual void onVmCreated(JNIEnv* env)
    {
        if (mClassName == NULL) {
            return; // Zygote. Nothing to do here.
        }

        /*
         * This is a little awkward because the JNI FindClass call uses the
         * class loader associated with the native method we're executing in.
         * If called in onStarted (from RuntimeInit.finishInit because we're
         * launching "am", for example), FindClass would see that we're calling
         * from a boot class' native method, and so wouldn't look for the class
         * we're trying to look up in CLASSPATH. Unfortunately it needs to,
         * because the "am" classes are not boot classes.
         *
         * The easiest fix is to call FindClass here, early on before we start
         * executing boot class Java code and thereby deny ourselves access to
         * non-boot classes.
         */
        char* slashClassName = toSlashClassName(mClassName);
        mClass = env->FindClass(slashClassName);
        if (mClass == NULL) {
            ALOGE("ERROR: could not find class '%s'\n", mClassName);
        }
        free(slashClassName);

        mClass = reinterpret_cast<jclass>(env->NewGlobalRef(mClass));
    }

    virtual void onStarted()
    {
        sp<ProcessState> proc = ProcessState::self();
        ALOGV("App process: starting thread pool.\n");
        proc->startThreadPool();

        AndroidRuntime* ar = AndroidRuntime::getRuntime();
        ar->callMain(mClassName, mClass, mArgC, mArgV);

        IPCThreadState::self()->stopProcess();
=======
    virtual void onStarted()
    {
        sp<ProcessState> proc = ProcessState::self();
        if (proc->supportsProcesses()) {
            LOGV("App process: starting thread pool.\n");
            proc->startThreadPool();
        }
        
        app_init(mClassName, mArgC, mArgV);

        if (ProcessState::self()->supportsProcesses()) {
            IPCThreadState::self()->stopProcess();
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    virtual void onZygoteInit()
    {
        sp<ProcessState> proc = ProcessState::self();
<<<<<<< HEAD
        ALOGV("App process: starting thread pool.\n");
        proc->startThreadPool();
=======
        if (proc->supportsProcesses()) {
            LOGV("App process: starting thread pool.\n");
            proc->startThreadPool();
        }       
>>>>>>> 54b6cfa... Initial Contribution
    }

    virtual void onExit(int code)
    {
        if (mClassName == NULL) {
            // if zygote
<<<<<<< HEAD
            IPCThreadState::self()->stopProcess();
=======
            if (ProcessState::self()->supportsProcesses()) {
                IPCThreadState::self()->stopProcess();
            }
>>>>>>> 54b6cfa... Initial Contribution
        }

        AndroidRuntime::onExit(code);
    }

<<<<<<< HEAD

    const char* mParentDir;
    const char* mClassName;
    jclass mClass;
=======
    
    const char* mParentDir;
    const char* mClassName;
>>>>>>> 54b6cfa... Initial Contribution
    int mArgC;
    const char* const* mArgV;
};

}

using namespace android;

/*
 * sets argv0 to as much of newArgv0 as will fit
 */
static void setArgv0(const char *argv0, const char *newArgv0)
{
    strlcpy(const_cast<char *>(argv0), newArgv0, strlen(argv0));
}

int main(int argc, const char* const argv[])
{
    // These are global variables in ProcessState.cpp
    mArgC = argc;
    mArgV = argv;
<<<<<<< HEAD

=======
    
>>>>>>> 54b6cfa... Initial Contribution
    mArgLen = 0;
    for (int i=0; i<argc; i++) {
        mArgLen += strlen(argv[i]) + 1;
    }
    mArgLen--;

    AppRuntime runtime;
<<<<<<< HEAD
    const char* argv0 = argv[0];
=======
    const char *arg;
    const char *argv0;

    argv0 = argv[0];
>>>>>>> 54b6cfa... Initial Contribution

    // Process command line arguments
    // ignore argv[0]
    argc--;
    argv++;

    // Everything up to '--' or first non '-' arg goes to the vm
<<<<<<< HEAD

    int i = runtime.addVmArguments(argc, argv);

    // Parse runtime arguments.  Stop at first unrecognized option.
    bool zygote = false;
    bool startSystemServer = false;
    bool application = false;
    const char* parentDir = NULL;
    const char* niceName = NULL;
    const char* className = NULL;
    while (i < argc) {
        const char* arg = argv[i++];
        if (!parentDir) {
            parentDir = arg;
        } else if (strcmp(arg, "--zygote") == 0) {
            zygote = true;
            niceName = "zygote";
        } else if (strcmp(arg, "--start-system-server") == 0) {
            startSystemServer = true;
        } else if (strcmp(arg, "--application") == 0) {
            application = true;
        } else if (strncmp(arg, "--nice-name=", 12) == 0) {
            niceName = arg + 12;
        } else {
            className = arg;
            break;
        }
    }

    if (niceName && *niceName) {
        setArgv0(argv0, niceName);
        set_process_name(niceName);
    }

    runtime.mParentDir = parentDir;

    if (zygote) {
        runtime.start("com.android.internal.os.ZygoteInit",
                startSystemServer ? "start-system-server" : "");
    } else if (className) {
        // Remainder of args get passed to startup class main()
        runtime.mClassName = className;
        runtime.mArgC = argc - i;
        runtime.mArgV = argv + i;
        runtime.start("com.android.internal.os.RuntimeInit",
                application ? "application" : "tool");
    } else {
        fprintf(stderr, "Error: no class name or --zygote supplied.\n");
        app_usage();
        LOG_ALWAYS_FATAL("app_process: no class name or --zygote supplied.");
        return 10;
    }
=======
    
    int i = runtime.addVmArguments(argc, argv);

    // Next arg is parent directory
    if (i < argc) {
        runtime.mParentDir = argv[i++];
    }

    // Next arg is startup classname or "--zygote"
    if (i < argc) {
        arg = argv[i++];
        if (0 == strcmp("--zygote", arg)) {
            bool startSystemServer = (i < argc) ? 
                    strcmp(argv[i], "--start-system-server") == 0 : false;
            setArgv0(argv0, "zygote");
            set_process_name("zygote");
            runtime.start("com.android.internal.os.ZygoteInit",
                startSystemServer);
        } else {
            set_process_name(argv0);

            runtime.mClassName = arg;

            // Remainder of args get passed to startup class main()
            runtime.mArgC = argc-i;
            runtime.mArgV = argv+i;

            LOGV("App process is starting with pid=%d, class=%s.\n",
                 getpid(), runtime.getClassName());
            runtime.start();
        }
    } else {
        LOG_ALWAYS_FATAL("app_process: no class name or --zygote supplied.");
        fprintf(stderr, "Error: no class name or --zygote supplied.\n");
        app_usage();
        return 10;
    }

>>>>>>> 54b6cfa... Initial Contribution
}
