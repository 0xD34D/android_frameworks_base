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

#include <stdio.h>

//#define LOG_NDEBUG 0
<<<<<<< HEAD
#define LOG_TAG "SoundPool-JNI"
=======
#define LOG_TAG "SoundPool"
>>>>>>> 54b6cfa... Initial Contribution

#include <utils/Log.h>
#include <nativehelper/jni.h>
#include <nativehelper/JNIHelp.h>
#include <android_runtime/AndroidRuntime.h>
<<<<<<< HEAD
#include <media/SoundPool.h>
=======
#include "SoundPool.h"
>>>>>>> 54b6cfa... Initial Contribution

using namespace android;

static struct fields_t {
    jfieldID    mNativeContext;
<<<<<<< HEAD
    jmethodID   mPostEvent;
=======
>>>>>>> 54b6cfa... Initial Contribution
    jclass      mSoundPoolClass;
} fields;

static inline SoundPool* MusterSoundPool(JNIEnv *env, jobject thiz) {
    return (SoundPool*)env->GetIntField(thiz, fields.mNativeContext);
}

// ----------------------------------------------------------------------------
static int
android_media_SoundPool_load_URL(JNIEnv *env, jobject thiz, jstring path, jint priority)
{
<<<<<<< HEAD
    ALOGV("android_media_SoundPool_load_URL");
=======
    LOGV("android_media_SoundPool_load_URL");
>>>>>>> 54b6cfa... Initial Contribution
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (path == NULL) {
        jniThrowException(env, "java/lang/IllegalArgumentException", NULL);
        return 0;
    }
    const char* s = env->GetStringUTFChars(path, NULL);
    int id = ap->load(s, priority);
    env->ReleaseStringUTFChars(path, s);
    return id;
}

static int
android_media_SoundPool_load_FD(JNIEnv *env, jobject thiz, jobject fileDescriptor,
        jlong offset, jlong length, jint priority)
{
<<<<<<< HEAD
    ALOGV("android_media_SoundPool_load_FD");
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap == NULL) return 0;
    return ap->load(jniGetFDFromFileDescriptor(env, fileDescriptor),
=======
    LOGV("android_media_SoundPool_load_FD");
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap == NULL) return 0;
    return ap->load(getParcelFileDescriptorFD(env, fileDescriptor),
>>>>>>> 54b6cfa... Initial Contribution
            int64_t(offset), int64_t(length), int(priority));
}

static bool
android_media_SoundPool_unload(JNIEnv *env, jobject thiz, jint sampleID) {
<<<<<<< HEAD
    ALOGV("android_media_SoundPool_unload\n");
=======
    LOGV("android_media_SoundPool_unload\n");
>>>>>>> 54b6cfa... Initial Contribution
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap == NULL) return 0;
    return ap->unload(sampleID);
}

static int
android_media_SoundPool_play(JNIEnv *env, jobject thiz, jint sampleID,
        jfloat leftVolume, jfloat rightVolume, jint priority, jint loop,
        jfloat rate)
{
<<<<<<< HEAD
    ALOGV("android_media_SoundPool_play\n");
=======
    LOGV("android_media_SoundPool_play\n");
>>>>>>> 54b6cfa... Initial Contribution
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap == NULL) return 0;
    return ap->play(sampleID, leftVolume, rightVolume, priority, loop, rate);
}

static void
android_media_SoundPool_pause(JNIEnv *env, jobject thiz, jint channelID)
{
<<<<<<< HEAD
    ALOGV("android_media_SoundPool_pause");
=======
    LOGV("android_media_SoundPool_pause");
>>>>>>> 54b6cfa... Initial Contribution
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap == NULL) return;
    ap->pause(channelID);
}

static void
android_media_SoundPool_resume(JNIEnv *env, jobject thiz, jint channelID)
{
<<<<<<< HEAD
    ALOGV("android_media_SoundPool_resume");
=======
    LOGV("android_media_SoundPool_resume");
>>>>>>> 54b6cfa... Initial Contribution
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap == NULL) return;
    ap->resume(channelID);
}

static void
<<<<<<< HEAD
android_media_SoundPool_autoPause(JNIEnv *env, jobject thiz)
{
    ALOGV("android_media_SoundPool_autoPause");
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap == NULL) return;
    ap->autoPause();
}

static void
android_media_SoundPool_autoResume(JNIEnv *env, jobject thiz)
{
    ALOGV("android_media_SoundPool_autoResume");
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap == NULL) return;
    ap->autoResume();
}

static void
android_media_SoundPool_stop(JNIEnv *env, jobject thiz, jint channelID)
{
    ALOGV("android_media_SoundPool_stop");
=======
android_media_SoundPool_stop(JNIEnv *env, jobject thiz, jint channelID)
{
    LOGV("android_media_SoundPool_stop");
>>>>>>> 54b6cfa... Initial Contribution
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap == NULL) return;
    ap->stop(channelID);
}

static void
android_media_SoundPool_setVolume(JNIEnv *env, jobject thiz, jint channelID,
        float leftVolume, float rightVolume)
{
<<<<<<< HEAD
    ALOGV("android_media_SoundPool_setVolume");
=======
    LOGV("android_media_SoundPool_setVolume");
>>>>>>> 54b6cfa... Initial Contribution
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap == NULL) return;
    ap->setVolume(channelID, leftVolume, rightVolume);
}

static void
android_media_SoundPool_setPriority(JNIEnv *env, jobject thiz, jint channelID,
        int priority)
{
<<<<<<< HEAD
    ALOGV("android_media_SoundPool_setPriority");
=======
    LOGV("android_media_SoundPool_setPriority");
>>>>>>> 54b6cfa... Initial Contribution
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap == NULL) return;
    ap->setPriority(channelID, priority);
}

static void
android_media_SoundPool_setLoop(JNIEnv *env, jobject thiz, jint channelID,
        int loop)
{
<<<<<<< HEAD
    ALOGV("android_media_SoundPool_setLoop");
=======
    LOGV("android_media_SoundPool_setLoop");
>>>>>>> 54b6cfa... Initial Contribution
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap == NULL) return;
    ap->setLoop(channelID, loop);
}

static void
android_media_SoundPool_setRate(JNIEnv *env, jobject thiz, jint channelID,
        float rate)
{
<<<<<<< HEAD
    ALOGV("android_media_SoundPool_setRate");
=======
    LOGV("android_media_SoundPool_setRate");
>>>>>>> 54b6cfa... Initial Contribution
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap == NULL) return;
    ap->setRate(channelID, rate);
}

<<<<<<< HEAD
static void android_media_callback(SoundPoolEvent event, SoundPool* soundPool, void* user)
{
    ALOGV("callback: (%d, %d, %d, %p, %p)", event.mMsg, event.mArg1, event.mArg2, soundPool, user);
    JNIEnv *env = AndroidRuntime::getJNIEnv();
    env->CallStaticVoidMethod(fields.mSoundPoolClass, fields.mPostEvent, user, event.mMsg, event.mArg1, event.mArg2, NULL);
}

static jint
android_media_SoundPool_native_setup(JNIEnv *env, jobject thiz, jobject weakRef, jint maxChannels, jint streamType, jint srcQuality)
{
    ALOGV("android_media_SoundPool_native_setup");
    SoundPool *ap = new SoundPool(maxChannels, (audio_stream_type_t) streamType, srcQuality);
    if (ap == NULL) {
        return -1;
=======
static void
android_media_SoundPool_native_setup(JNIEnv *env, jobject thiz,
        jobject weak_this, jint maxChannels, jint streamType, jint srcQuality)
{
    LOGV("android_media_SoundPool_native_setup");
    SoundPool *ap = new SoundPool(weak_this, maxChannels, streamType, srcQuality);
    if (ap == NULL) {
        jniThrowException(env, "java/lang/RuntimeException", "Out of memory");
        return;
>>>>>>> 54b6cfa... Initial Contribution
    }

    // save pointer to SoundPool C++ object in opaque field in Java object
    env->SetIntField(thiz, fields.mNativeContext, (int)ap);
<<<<<<< HEAD

    // set callback with weak reference
    jobject globalWeakRef = env->NewGlobalRef(weakRef);
    ap->setCallback(android_media_callback, globalWeakRef);
    return 0;
=======
>>>>>>> 54b6cfa... Initial Contribution
}

static void
android_media_SoundPool_release(JNIEnv *env, jobject thiz)
{
<<<<<<< HEAD
    ALOGV("android_media_SoundPool_release");
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap != NULL) {

        // release weak reference
        jobject weakRef = (jobject) ap->getUserData();
        if (weakRef != NULL) {
            env->DeleteGlobalRef(weakRef);
        }

        // clear callback and native context
        ap->setCallback(NULL, NULL);
=======
    LOGV("android_media_SoundPool_release");
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap != NULL) {
>>>>>>> 54b6cfa... Initial Contribution
        env->SetIntField(thiz, fields.mNativeContext, 0);
        delete ap;
    }
}

// ----------------------------------------------------------------------------

// Dalvik VM type signatures
static JNINativeMethod gMethods[] = {
    {   "_load",
        "(Ljava/lang/String;I)I",
        (void *)android_media_SoundPool_load_URL
    },
    {   "_load",
        "(Ljava/io/FileDescriptor;JJI)I",
        (void *)android_media_SoundPool_load_FD
    },
    {   "unload",
        "(I)Z",
        (void *)android_media_SoundPool_unload
    },
    {   "play",
        "(IFFIIF)I",
        (void *)android_media_SoundPool_play
    },
    {   "pause",
        "(I)V",
        (void *)android_media_SoundPool_pause
    },
    {   "resume",
        "(I)V",
        (void *)android_media_SoundPool_resume
    },
<<<<<<< HEAD
    {   "autoPause",
        "()V",
        (void *)android_media_SoundPool_autoPause
    },
    {   "autoResume",
        "()V",
        (void *)android_media_SoundPool_autoResume
    },
=======
>>>>>>> 54b6cfa... Initial Contribution
    {   "stop",
        "(I)V",
        (void *)android_media_SoundPool_stop
    },
    {   "setVolume",
        "(IFF)V",
        (void *)android_media_SoundPool_setVolume
    },
    {   "setPriority",
        "(II)V",
        (void *)android_media_SoundPool_setPriority
    },
    {   "setLoop",
        "(II)V",
        (void *)android_media_SoundPool_setLoop
    },
    {   "setRate",
        "(IF)V",
        (void *)android_media_SoundPool_setRate
    },
    {   "native_setup",
<<<<<<< HEAD
        "(Ljava/lang/Object;III)I",
=======
        "(Ljava/lang/Object;III)V",
>>>>>>> 54b6cfa... Initial Contribution
        (void*)android_media_SoundPool_native_setup
    },
    {   "release",
        "()V",
        (void*)android_media_SoundPool_release
    }
};

static const char* const kClassPathName = "android/media/SoundPool";

jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    JNIEnv* env = NULL;
    jint result = -1;
    jclass clazz;

    if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
<<<<<<< HEAD
        ALOGE("ERROR: GetEnv failed\n");
=======
        LOGE("ERROR: GetEnv failed\n");
>>>>>>> 54b6cfa... Initial Contribution
        goto bail;
    }
    assert(env != NULL);

    clazz = env->FindClass(kClassPathName);
    if (clazz == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find %s", kClassPathName);
=======
        LOGE("Can't find %s", kClassPathName);
>>>>>>> 54b6cfa... Initial Contribution
        goto bail;
    }

    fields.mNativeContext = env->GetFieldID(clazz, "mNativeContext", "I");
    if (fields.mNativeContext == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find SoundPool.mNativeContext");
        goto bail;
    }

    fields.mPostEvent = env->GetStaticMethodID(clazz, "postEventFromNative",
                                               "(Ljava/lang/Object;IIILjava/lang/Object;)V");
    if (fields.mPostEvent == NULL) {
        ALOGE("Can't find android/media/SoundPool.postEventFromNative");
        goto bail;
    }

    // create a reference to class. Technically, we're leaking this reference
    // since it's a static object.
    fields.mSoundPoolClass = (jclass) env->NewGlobalRef(clazz);

=======
        LOGE("Can't find SoundPool.mNativeContext");
        goto bail;
    }

>>>>>>> 54b6cfa... Initial Contribution
    if (AndroidRuntime::registerNativeMethods(env, kClassPathName, gMethods, NELEM(gMethods)) < 0)
        goto bail;

    /* success -- return valid version number */
    result = JNI_VERSION_1_4;

bail:
    return result;
}
