<<<<<<< HEAD
/*
=======
/* //device/libs/android_runtime/android_media_AudioSystem.cpp
>>>>>>> 54b6cfa... Initial Contribution
**
** Copyright 2006, The Android Open Source Project
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

#define LOG_TAG "AudioSystem"
<<<<<<< HEAD
#include <utils/Log.h>
=======
#include "utils/Log.h"
>>>>>>> 54b6cfa... Initial Contribution

#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>
#include <math.h>

<<<<<<< HEAD
#include <jni.h>
#include <JNIHelp.h>
#include <android_runtime/AndroidRuntime.h>

#include <media/AudioSystem.h>

#include <system/audio.h>
#include <system/audio_policy.h>
=======
#include "jni.h"
#include "JNIHelp.h"
#include "android_runtime/AndroidRuntime.h"

#include <media/AudioSystem.h>
#include <media/AudioTrack.h>
>>>>>>> 54b6cfa... Initial Contribution

// ----------------------------------------------------------------------------

using namespace android;

<<<<<<< HEAD
static const char* const kClassPathName = "android/media/AudioSystem";

=======
>>>>>>> 54b6cfa... Initial Contribution
enum AudioError {
    kAudioStatusOk = 0,
    kAudioStatusError = 1,
    kAudioStatusMediaServerDied = 100
};

static int check_AudioSystem_Command(status_t status)
{
    if (status == NO_ERROR) {
        return kAudioStatusOk;
    } else {
        return kAudioStatusError;
    }
}

static int
<<<<<<< HEAD
=======
android_media_AudioSystem_setVolume(JNIEnv *env, jobject clazz, jint type, jint volume)
{
    LOGV("setVolume(%d)", int(volume));
    if (int(type) == AudioTrack::VOICE_CALL) {
        return check_AudioSystem_Command(AudioSystem::setStreamVolume(type, float(volume) / 100.0));
    } else {
        return check_AudioSystem_Command(AudioSystem::setStreamVolume(type, AudioSystem::linearToLog(volume)));
    }
}

static int
android_media_AudioSystem_getVolume(JNIEnv *env, jobject clazz, jint type)
{
    float v;
    int v_int = -1;
    if (AudioSystem::getStreamVolume(int(type), &v) == NO_ERROR) {
        // voice call volume is converted to log scale in the hardware
        if (int(type) == AudioTrack::VOICE_CALL) {
            v_int = lrint(v * 100.0);
        } else {
            v_int = AudioSystem::logToLinear(v);
        }
    }
    return v_int;
}

static int
>>>>>>> 54b6cfa... Initial Contribution
android_media_AudioSystem_muteMicrophone(JNIEnv *env, jobject thiz, jboolean on)
{
    return check_AudioSystem_Command(AudioSystem::muteMicrophone(on));
}

static jboolean
android_media_AudioSystem_isMicrophoneMuted(JNIEnv *env, jobject thiz)
{
    bool state = false;
    AudioSystem::isMicrophoneMuted(&state);
    return state;
}

<<<<<<< HEAD
static jboolean
android_media_AudioSystem_isStreamActive(JNIEnv *env, jobject thiz, jint stream, jint inPastMs)
{
    bool state = false;
    AudioSystem::isStreamActive((audio_stream_type_t) stream, &state, inPastMs);
    return state;
}

static int
android_media_AudioSystem_setParameters(JNIEnv *env, jobject thiz, jstring keyValuePairs)
{
    const jchar* c_keyValuePairs = env->GetStringCritical(keyValuePairs, 0);
    String8 c_keyValuePairs8;
    if (keyValuePairs) {
        c_keyValuePairs8 = String8(c_keyValuePairs, env->GetStringLength(keyValuePairs));
        env->ReleaseStringCritical(keyValuePairs, c_keyValuePairs);
    }
    int status = check_AudioSystem_Command(AudioSystem::setParameters(0, c_keyValuePairs8));
    return status;
}

static jstring
android_media_AudioSystem_getParameters(JNIEnv *env, jobject thiz, jstring keys)
{
    const jchar* c_keys = env->GetStringCritical(keys, 0);
    String8 c_keys8;
    if (keys) {
        c_keys8 = String8(c_keys, env->GetStringLength(keys));
        env->ReleaseStringCritical(keys, c_keys);
    }
    return env->NewStringUTF(AudioSystem::getParameters(0, c_keys8).string());
}

static void
android_media_AudioSystem_error_callback(status_t err)
{
    JNIEnv *env = AndroidRuntime::getJNIEnv();
    if (env == NULL) {
        return;
    }

    jclass clazz = env->FindClass(kClassPathName);
=======
static int
android_media_AudioSystem_setRouting(JNIEnv *env, jobject clazz, jint mode, jint routes, jint mask)
{
    return check_AudioSystem_Command(AudioSystem::setRouting(mode, uint32_t(routes), uint32_t(mask)));
}

static jint
android_media_AudioSystem_getRouting(JNIEnv *env, jobject clazz, jint mode)
{
    uint32_t routes = -1;
    AudioSystem::getRouting(mode, &routes);
    return jint(routes);
}

static int
android_media_AudioSystem_setMode(JNIEnv *env, jobject clazz, jint mode)
{
    return check_AudioSystem_Command(AudioSystem::setMode(mode));
}

static jint
android_media_AudioSystem_getMode(JNIEnv *env, jobject clazz)
{
    int mode = AudioSystem::MODE_INVALID;
    AudioSystem::getMode(&mode);
    return jint(mode);
}

static jboolean
android_media_AudioSystem_isMusicActive(JNIEnv *env, jobject thiz)
{
    bool state = false;
    AudioSystem::isMusicActive(&state);
    return state;
}

// Temporary interface, do not use
// TODO: Replace with a more generic key:value get/set mechanism
static void
android_media_AudioSystem_setParameter(JNIEnv *env, jobject thiz, jstring key, jstring value)
{
    const char *c_key = env->GetStringUTFChars(key, NULL);
    const char *c_value = env->GetStringUTFChars(value, NULL);
    AudioSystem::setParameter(c_key, c_value);
    env->ReleaseStringUTFChars(key, c_key);
    env->ReleaseStringUTFChars(value, c_value);
}

void android_media_AudioSystem_error_callback(status_t err)
{
    JNIEnv *env = AndroidRuntime::getJNIEnv();
    jclass clazz = env->FindClass("android/media/AudioSystem");
>>>>>>> 54b6cfa... Initial Contribution

    int error;

    switch (err) {
    case DEAD_OBJECT:
        error = kAudioStatusMediaServerDied;
        break;
    case NO_ERROR:
        error = kAudioStatusOk;
        break;
    default:
        error = kAudioStatusError;
        break;
    }

    env->CallStaticVoidMethod(clazz, env->GetStaticMethodID(clazz, "errorCallbackFromNative","(I)V"), error);
}

<<<<<<< HEAD
static int
android_media_AudioSystem_setDeviceConnectionState(JNIEnv *env, jobject thiz, jint device, jint state, jstring device_address)
{
    const char *c_address = env->GetStringUTFChars(device_address, NULL);
    int status = check_AudioSystem_Command(AudioSystem::setDeviceConnectionState(static_cast <audio_devices_t>(device),
                                          static_cast <audio_policy_dev_state_t>(state),
                                          c_address));
    env->ReleaseStringUTFChars(device_address, c_address);
    return status;
}

static int
android_media_AudioSystem_getDeviceConnectionState(JNIEnv *env, jobject thiz, jint device, jstring device_address)
{
    const char *c_address = env->GetStringUTFChars(device_address, NULL);
    int state = static_cast <int>(AudioSystem::getDeviceConnectionState(static_cast <audio_devices_t>(device),
                                          c_address));
    env->ReleaseStringUTFChars(device_address, c_address);
    return state;
}

static int
android_media_AudioSystem_setPhoneState(JNIEnv *env, jobject thiz, jint state)
{
    return check_AudioSystem_Command(AudioSystem::setPhoneState((audio_mode_t) state));
}

static int
android_media_AudioSystem_setForceUse(JNIEnv *env, jobject thiz, jint usage, jint config)
{
    return check_AudioSystem_Command(AudioSystem::setForceUse(static_cast <audio_policy_force_use_t>(usage),
                                                           static_cast <audio_policy_forced_cfg_t>(config)));
}

static int
android_media_AudioSystem_getForceUse(JNIEnv *env, jobject thiz, jint usage)
{
    return static_cast <int>(AudioSystem::getForceUse(static_cast <audio_policy_force_use_t>(usage)));
}

static int
android_media_AudioSystem_initStreamVolume(JNIEnv *env, jobject thiz, jint stream, jint indexMin, jint indexMax)
{
    return check_AudioSystem_Command(AudioSystem::initStreamVolume(static_cast <audio_stream_type_t>(stream),
                                                                   indexMin,
                                                                   indexMax));
}

static int
android_media_AudioSystem_setStreamVolumeIndex(JNIEnv *env,
                                               jobject thiz,
                                               jint stream,
                                               jint index,
                                               jint device)
{
    return check_AudioSystem_Command(
            AudioSystem::setStreamVolumeIndex(static_cast <audio_stream_type_t>(stream),
                                              index,
                                              (audio_devices_t)device));
}

static int
android_media_AudioSystem_getStreamVolumeIndex(JNIEnv *env,
                                               jobject thiz,
                                               jint stream,
                                               jint device)
{
    int index;
    if (AudioSystem::getStreamVolumeIndex(static_cast <audio_stream_type_t>(stream),
                                          &index,
                                          (audio_devices_t)device)
            != NO_ERROR) {
        index = -1;
    }
    return index;
}

static int
android_media_AudioSystem_setMasterVolume(JNIEnv *env, jobject thiz, jfloat value)
{
    return check_AudioSystem_Command(AudioSystem::setMasterVolume(value));
}

static jfloat
android_media_AudioSystem_getMasterVolume(JNIEnv *env, jobject thiz)
{
    float value;
    if (AudioSystem::getMasterVolume(&value) != NO_ERROR) {
        value = -1.0;
    }
    return value;
}

static int
android_media_AudioSystem_setMasterMute(JNIEnv *env, jobject thiz, jboolean mute)
{
    return check_AudioSystem_Command(AudioSystem::setMasterMute(mute));
}

static jfloat
android_media_AudioSystem_getMasterMute(JNIEnv *env, jobject thiz)
{
    bool mute;
    if (AudioSystem::getMasterMute(&mute) != NO_ERROR) {
        mute = false;
    }
    return mute;
}

static jint
android_media_AudioSystem_getDevicesForStream(JNIEnv *env, jobject thiz, jint stream)
{
    return (jint) AudioSystem::getDevicesForStream(static_cast <audio_stream_type_t>(stream));
}

// ----------------------------------------------------------------------------

static JNINativeMethod gMethods[] = {
    {"setParameters",        "(Ljava/lang/String;)I", (void *)android_media_AudioSystem_setParameters},
    {"getParameters",        "(Ljava/lang/String;)Ljava/lang/String;", (void *)android_media_AudioSystem_getParameters},
    {"muteMicrophone",      "(Z)I",     (void *)android_media_AudioSystem_muteMicrophone},
    {"isMicrophoneMuted",   "()Z",      (void *)android_media_AudioSystem_isMicrophoneMuted},
    {"isStreamActive",      "(II)Z",     (void *)android_media_AudioSystem_isStreamActive},
    {"setDeviceConnectionState", "(IILjava/lang/String;)I", (void *)android_media_AudioSystem_setDeviceConnectionState},
    {"getDeviceConnectionState", "(ILjava/lang/String;)I",  (void *)android_media_AudioSystem_getDeviceConnectionState},
    {"setPhoneState",       "(I)I",     (void *)android_media_AudioSystem_setPhoneState},
    {"setForceUse",         "(II)I",    (void *)android_media_AudioSystem_setForceUse},
    {"getForceUse",         "(I)I",     (void *)android_media_AudioSystem_getForceUse},
    {"initStreamVolume",    "(III)I",   (void *)android_media_AudioSystem_initStreamVolume},
    {"setStreamVolumeIndex","(III)I",   (void *)android_media_AudioSystem_setStreamVolumeIndex},
    {"getStreamVolumeIndex","(II)I",    (void *)android_media_AudioSystem_getStreamVolumeIndex},
    {"setMasterVolume",     "(F)I",     (void *)android_media_AudioSystem_setMasterVolume},
    {"getMasterVolume",     "()F",      (void *)android_media_AudioSystem_getMasterVolume},
    {"setMasterMute",       "(Z)I",     (void *)android_media_AudioSystem_setMasterMute},
    {"getMasterMute",       "()Z",      (void *)android_media_AudioSystem_getMasterMute},
    {"getDevicesForStream", "(I)I",     (void *)android_media_AudioSystem_getDevicesForStream},
};

int register_android_media_AudioSystem(JNIEnv *env)
{
    AudioSystem::setErrorCallback(android_media_AudioSystem_error_callback);

    return AndroidRuntime::registerNativeMethods(env,
                kClassPathName, gMethods, NELEM(gMethods));
=======
// ----------------------------------------------------------------------------

static JNINativeMethod gMethods[] = {
    {"setVolume",           "(II)I",    (void *)android_media_AudioSystem_setVolume},
    {"getVolume",           "(I)I",     (void *)android_media_AudioSystem_getVolume},
    {"setParameter",        "(Ljava/lang/String;Ljava/lang/String;)V", (void *)android_media_AudioSystem_setParameter},
    {"muteMicrophone",      "(Z)I",     (void *)android_media_AudioSystem_muteMicrophone},
    {"isMicrophoneMuted",   "()Z",      (void *)android_media_AudioSystem_isMicrophoneMuted},
    {"setRouting",          "(III)I",   (void *)android_media_AudioSystem_setRouting},
    {"getRouting",          "(I)I",     (void *)android_media_AudioSystem_getRouting},
    {"setMode",             "(I)I",     (void *)android_media_AudioSystem_setMode},
    {"getMode",             "()I",      (void *)android_media_AudioSystem_getMode},
    {"isMusicActive",       "()Z",      (void *)android_media_AudioSystem_isMusicActive},
};

const char* const kClassPathName = "android/media/AudioSystem";

int register_android_media_AudioSystem(JNIEnv *env)
{
    AudioSystem::setErrorCallback(android_media_AudioSystem_error_callback);
    
    return AndroidRuntime::registerNativeMethods(env,
                "android/media/AudioSystem", gMethods, NELEM(gMethods));
>>>>>>> 54b6cfa... Initial Contribution
}
