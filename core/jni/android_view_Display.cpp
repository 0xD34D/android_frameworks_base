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

#include <stdio.h>
#include <assert.h>

<<<<<<< HEAD
#include <cutils/properties.h>

#include <gui/SurfaceComposerClient.h>
=======
#include <ui/SurfaceComposerClient.h>
>>>>>>> 54b6cfa... Initial Contribution
#include <ui/PixelFormat.h>
#include <ui/DisplayInfo.h>

#include "jni.h"
<<<<<<< HEAD
#include "JNIHelp.h"
#include <android_runtime/AndroidRuntime.h>
#include <utils/misc.h>
#include <utils/Log.h>
#include <cutils/properties.h>
=======
#include <android_runtime/AndroidRuntime.h>
#include <utils/misc.h>
>>>>>>> 54b6cfa... Initial Contribution

// ----------------------------------------------------------------------------

namespace android {

// ----------------------------------------------------------------------------

struct offsets_t {
    jfieldID display;
    jfieldID pixelFormat;
    jfieldID fps;
    jfieldID density;
    jfieldID xdpi;
    jfieldID ydpi;
};
static offsets_t offsets;
<<<<<<< HEAD
static bool headless = false;
=======

static void doThrow(JNIEnv* env, const char* exc, const char* msg = NULL)
{
    jclass npeClazz = env->FindClass(exc);
    env->ThrowNew(npeClazz, msg);
}
>>>>>>> 54b6cfa... Initial Contribution

// ----------------------------------------------------------------------------

static void android_view_Display_init(
        JNIEnv* env, jobject clazz, jint dpy)
{
    DisplayInfo info;
<<<<<<< HEAD
    if (headless) {
        // initialize dummy display with reasonable values
        info.pixelFormatInfo.format = 1; // RGB_8888
        info.fps = 60;
        info.density = 160;
        info.xdpi = 160;
        info.ydpi = 160;
    } else {
        status_t err = SurfaceComposerClient::getDisplayInfo(DisplayID(dpy), &info);
        if (err < 0) {
            jniThrowException(env, "java/lang/IllegalArgumentException", NULL);
            return;
        }
=======
    status_t err = SurfaceComposerClient::getDisplayInfo(DisplayID(dpy), &info);
    if (err < 0) {
        doThrow(env, "java/lang/IllegalArgumentException");
        return;
>>>>>>> 54b6cfa... Initial Contribution
    }
    env->SetIntField(clazz, offsets.pixelFormat,info.pixelFormatInfo.format);
    env->SetFloatField(clazz, offsets.fps,      info.fps);
    env->SetFloatField(clazz, offsets.density,  info.density);
    env->SetFloatField(clazz, offsets.xdpi,     info.xdpi);
    env->SetFloatField(clazz, offsets.ydpi,     info.ydpi);
}

<<<<<<< HEAD
static jint android_view_Display_getRawWidthNative(
        JNIEnv* env, jobject clazz)
{
    if (headless) return 640;
=======
static jint android_view_Display_getWidth(
        JNIEnv* env, jobject clazz)
{
>>>>>>> 54b6cfa... Initial Contribution
    DisplayID dpy = env->GetIntField(clazz, offsets.display);
    return SurfaceComposerClient::getDisplayWidth(dpy);
}

<<<<<<< HEAD
static jint android_view_Display_getRawHeightNative(
        JNIEnv* env, jobject clazz)
{
    if (headless) return 480;
=======
static jint android_view_Display_getHeight(
        JNIEnv* env, jobject clazz)
{
>>>>>>> 54b6cfa... Initial Contribution
    DisplayID dpy = env->GetIntField(clazz, offsets.display);
    return SurfaceComposerClient::getDisplayHeight(dpy);
}

static jint android_view_Display_getOrientation(
        JNIEnv* env, jobject clazz)
{
<<<<<<< HEAD
    if (headless) return 0; // Surface.ROTATION_0
=======
>>>>>>> 54b6cfa... Initial Contribution
    DisplayID dpy = env->GetIntField(clazz, offsets.display);
    return SurfaceComposerClient::getDisplayOrientation(dpy);
}

static jint android_view_Display_getDisplayCount(
        JNIEnv* env, jclass clazz)
{
<<<<<<< HEAD
    if (headless) return 1;
=======
>>>>>>> 54b6cfa... Initial Contribution
    return SurfaceComposerClient::getNumberOfDisplays();
}

// ----------------------------------------------------------------------------

const char* const kClassPathName = "android/view/Display";

static void nativeClassInit(JNIEnv* env, jclass clazz);

static JNINativeMethod gMethods[] = {
    {   "nativeClassInit", "()V",
            (void*)nativeClassInit },
    {   "getDisplayCount", "()I",
            (void*)android_view_Display_getDisplayCount },
	{   "init", "(I)V",
            (void*)android_view_Display_init },
<<<<<<< HEAD
    {   "getRawWidthNative", "()I",
            (void*)android_view_Display_getRawWidthNative },
    {   "getRawHeightNative", "()I",
            (void*)android_view_Display_getRawHeightNative },
=======
    {   "getWidth", "()I",
            (void*)android_view_Display_getWidth },
    {   "getHeight", "()I",
            (void*)android_view_Display_getHeight },
>>>>>>> 54b6cfa... Initial Contribution
    {   "getOrientation", "()I",
            (void*)android_view_Display_getOrientation }
};

void nativeClassInit(JNIEnv* env, jclass clazz)
{
<<<<<<< HEAD
    char value[PROPERTY_VALUE_MAX];

    property_get("ro.config.headless", value, "0");
    if (strcmp(value, "1") == 0)
        headless = true;

=======
>>>>>>> 54b6cfa... Initial Contribution
    offsets.display     = env->GetFieldID(clazz, "mDisplay", "I");
    offsets.pixelFormat = env->GetFieldID(clazz, "mPixelFormat", "I");
    offsets.fps         = env->GetFieldID(clazz, "mRefreshRate", "F");
    offsets.density     = env->GetFieldID(clazz, "mDensity", "F");
    offsets.xdpi        = env->GetFieldID(clazz, "mDpiX", "F");
    offsets.ydpi        = env->GetFieldID(clazz, "mDpiY", "F");
}

int register_android_view_Display(JNIEnv* env)
{
    return AndroidRuntime::registerNativeMethods(env,
            kClassPathName, gMethods, NELEM(gMethods));
}

};
<<<<<<< HEAD
=======

>>>>>>> 54b6cfa... Initial Contribution
