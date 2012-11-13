/* //device/libs/android_runtime/android_os_Power.cpp
**
** Copyright 2006, The Android Open Source Project
<<<<<<< HEAD
** Copyright (c) 2010-2011, Code Aurora Forum. All rights reserved.
**
** Licensed under the Apache License, Version 2.0 (the "License");
** you may not use this file except in compliance with the License.
** You may obtain a copy of the License at
**
**     http://www.apache.org/licenses/LICENSE-2.0
**
** Unless required by applicable law or agreed to in writing, software
** distributed under the License is distributed on an "AS IS" BASIS,
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
** See the License for the specific language governing permissions and
** limitations under the License.
*/

#include "JNIHelp.h"
#include "jni.h"
#include "android_runtime/AndroidRuntime.h"
#include <utils/misc.h>
#include <hardware_legacy/power.h>
#include <cutils/android_reboot.h>
#include <cutils/log.h>
=======
**
** Licensed under the Apache License, Version 2.0 (the "License"); 
** you may not use this file except in compliance with the License. 
** You may obtain a copy of the License at 
**
**     http://www.apache.org/licenses/LICENSE-2.0 
**
** Unless required by applicable law or agreed to in writing, software 
** distributed under the License is distributed on an "AS IS" BASIS, 
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
** See the License for the specific language governing permissions and 
** limitations under the License.
*/

#include "jni.h"
#include "android_runtime/AndroidRuntime.h"
#include <utils/misc.h>
#include <hardware/power.h>
#include <sys/reboot.h>
>>>>>>> 54b6cfa... Initial Contribution

namespace android
{

<<<<<<< HEAD
=======
static void throw_NullPointerException(JNIEnv *env, const char* msg)
{
    jclass clazz;
    clazz = env->FindClass("java/lang/NullPointerException");
    env->ThrowNew(clazz, msg);
}

>>>>>>> 54b6cfa... Initial Contribution
static void
acquireWakeLock(JNIEnv *env, jobject clazz, jint lock, jstring idObj)
{
    if (idObj == NULL) {
<<<<<<< HEAD
        jniThrowNullPointerException(env, "id is null");
=======
        throw_NullPointerException(env, "id is null");
>>>>>>> 54b6cfa... Initial Contribution
        return ;
    }

    const char *id = env->GetStringUTFChars(idObj, NULL);

    acquire_wake_lock(lock, id);

    env->ReleaseStringUTFChars(idObj, id);
}

static void
releaseWakeLock(JNIEnv *env, jobject clazz, jstring idObj)
{
    if (idObj == NULL) {
<<<<<<< HEAD
        jniThrowNullPointerException(env, "id is null");
=======
        throw_NullPointerException(env, "id is null");
>>>>>>> 54b6cfa... Initial Contribution
        return ;
    }

    const char *id = env->GetStringUTFChars(idObj, NULL);

    release_wake_lock(id);

    env->ReleaseStringUTFChars(idObj, id);

}

<<<<<<< HEAD
static void android_os_Power_shutdown(JNIEnv *env, jobject clazz)
{
    android_reboot(ANDROID_RB_POWEROFF, 0, 0);
}

static void android_os_Power_reboot(JNIEnv *env, jobject clazz, jstring reason)
{
    if (reason == NULL) {
        ALOGE("Rebooting with NULL reason");
        android_reboot(ANDROID_RB_RESTART, 0, 0);
    } else {
        const char *chars = env->GetStringUTFChars(reason, NULL);
        // for some reason a null string from java is coming in with the "null" text
        if (strncmp(chars, "null", 4) == 0) {
            ALOGE("Rebooting using ANDROID_RB_RESTART with reason=\"null\"");
            android_reboot(ANDROID_RB_RESTART, 0, 0);
        } else {
            ALOGE("Rebooting using ANDROID_RB_RESTART2 with reason=%s", chars);
            android_reboot(ANDROID_RB_RESTART2, 0, (char *) chars);
        }
        env->ReleaseStringUTFChars(reason, chars);  // In case it fails.
    }
    jniThrowIOException(env, errno);
}

static void android_os_Power_reboot_recovery(JNIEnv *env, jobject clazz, jstring reason)
{
    ALOGE("Rebooting into recovery!");
    const char *chars = env->GetStringUTFChars(reason, NULL);
    android_reboot(ANDROID_RB_RESTART2, 0, (char *) chars);
    env->ReleaseStringUTFChars(reason, chars);  // In case it fails.

    jniThrowIOException(env, errno);
}

#if defined(QCOM_HARDWARE) && !defined(LEGACY_QCOM)
static int
SetUnstableMemoryState(JNIEnv *env, jobject clazz, jboolean on)
{
    return set_unstable_memory_state(on);
}
#endif
=======
static int
setLastUserActivityTimeout(JNIEnv *env, jobject clazz, jlong timeMS)
{
    return set_last_user_activity_timeout(timeMS/1000);
}

static int
setLightBrightness(JNIEnv *env, jobject clazz, jint mask, jint brightness)
{
    return set_light_brightness(mask, brightness);
}

static int
setScreenState(JNIEnv *env, jobject clazz, jboolean on)
{
    return set_screen_state(on);
}

static void android_os_Power_shutdown(JNIEnv *env, jobject clazz)
{
    sync();
#ifdef HAVE_ANDROID_OS
    reboot(RB_POWER_OFF);
#endif
}

static void android_os_Power_reboot(JNIEnv *env, jobject clazz, jstring reason)
{
    sync();
#ifdef HAVE_ANDROID_OS
    if (reason == NULL) {
        reboot(RB_AUTOBOOT);
    } else {
        const char *chars = env->GetStringUTFChars(reason, NULL);
        __reboot(LINUX_REBOOT_MAGIC1, LINUX_REBOOT_MAGIC2,
                 LINUX_REBOOT_CMD_RESTART2, (char*) chars);
        env->ReleaseStringUTFChars(reason, chars);  // In case it fails.
    }
#endif
}
>>>>>>> 54b6cfa... Initial Contribution

static JNINativeMethod method_table[] = {
    { "acquireWakeLock", "(ILjava/lang/String;)V", (void*)acquireWakeLock },
    { "releaseWakeLock", "(Ljava/lang/String;)V", (void*)releaseWakeLock },
<<<<<<< HEAD
    { "shutdown", "()V", (void*)android_os_Power_shutdown },
    { "rebootNative", "(Ljava/lang/String;)V", (void*)android_os_Power_reboot },
#if defined(QCOM_HARDWARE) && !defined(LEGACY_QCOM)
    { "SetUnstableMemoryState",  "(Z)I", (void*)SetUnstableMemoryState},
#endif
=======
    { "setLastUserActivityTimeout", "(J)I", (void*)setLastUserActivityTimeout },
    { "setLightBrightness", "(II)I", (void*)setLightBrightness },
    { "setScreenState", "(Z)I", (void*)setScreenState },
    { "shutdown", "()V", (void*)android_os_Power_shutdown },
    { "reboot", "(Ljava/lang/String;)V", (void*)android_os_Power_reboot },
>>>>>>> 54b6cfa... Initial Contribution
};

int register_android_os_Power(JNIEnv *env)
{
    return AndroidRuntime::registerNativeMethods(
        env, "android/os/Power",
        method_table, NELEM(method_table));
}

};
