/* //device/libs/android_runtime/android_os_Power.cpp
**
** Copyright 2006, The Android Open Source Project
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

namespace android
{

static void
acquireWakeLock(JNIEnv *env, jobject clazz, jint lock, jstring idObj)
{
    if (idObj == NULL) {
        jniThrowNullPointerException(env, "id is null");
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
        jniThrowNullPointerException(env, "id is null");
        return ;
    }

    const char *id = env->GetStringUTFChars(idObj, NULL);

    release_wake_lock(id);

    env->ReleaseStringUTFChars(idObj, id);

}

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

static JNINativeMethod method_table[] = {
    { "acquireWakeLock", "(ILjava/lang/String;)V", (void*)acquireWakeLock },
    { "releaseWakeLock", "(Ljava/lang/String;)V", (void*)releaseWakeLock },
    { "shutdown", "()V", (void*)android_os_Power_shutdown },
    { "rebootNative", "(Ljava/lang/String;)V", (void*)android_os_Power_reboot },
#if defined(QCOM_HARDWARE) && !defined(LEGACY_QCOM)
    { "SetUnstableMemoryState",  "(Z)I", (void*)SetUnstableMemoryState},
#endif
};

int register_android_os_Power(JNIEnv *env)
{
    return AndroidRuntime::registerNativeMethods(
        env, "android/os/Power",
        method_table, NELEM(method_table));
}

};
