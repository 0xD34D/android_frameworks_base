/*
 * Main entry of system server process.
 * 
 * Calls the standard system initialization function, and then
 * puts the main thread into the thread pool so it can handle
 * incoming transactions.
 * 
 */

#define LOG_TAG "sysproc"

<<<<<<< HEAD
#include <binder/IPCThreadState.h>
=======
#include <utils/IPCThreadState.h>
>>>>>>> 54b6cfa... Initial Contribution
#include <utils/Log.h>

#include <private/android_filesystem_config.h>

#include <sys/time.h>
#include <sys/resource.h>

#include <signal.h>
#include <stdio.h>
#include <unistd.h>

using namespace android;

extern "C" status_t system_init();

bool finish_system_init()
{
    return true;
}

static void blockSignals()
{
    sigset_t mask;
    int cc;
    
    sigemptyset(&mask);
    sigaddset(&mask, SIGQUIT);
    sigaddset(&mask, SIGUSR1);
    cc = sigprocmask(SIG_BLOCK, &mask, NULL);
    assert(cc == 0);
}

int main(int argc, const char* const argv[])
{
<<<<<<< HEAD
    ALOGI("System server is starting with pid=%d.\n", getpid());
=======
    LOGI("System server is starting with pid=%d.\n", getpid());
>>>>>>> 54b6cfa... Initial Contribution

    blockSignals();
    
    // You can trust me, honestly!
<<<<<<< HEAD
    ALOGW("*** Current priority: %d\n", getpriority(PRIO_PROCESS, 0));
    setpriority(PRIO_PROCESS, 0, -1);

=======
    LOGW("*** Current priority: %d\n", getpriority(PRIO_PROCESS, 0));
    setpriority(PRIO_PROCESS, 0, -1);

    #if HAVE_ANDROID_OS
    //setgid(GID_SYSTEM);
    //setuid(UID_SYSTEM);
    #endif

>>>>>>> 54b6cfa... Initial Contribution
    system_init();    
}
