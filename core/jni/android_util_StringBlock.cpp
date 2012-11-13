/* //device/libs/android_runtime/android_util_StringBlock.cpp
**
** Copyright 2006, The Android Open Source Project
**
<<<<<<< HEAD
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
=======
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
>>>>>>> 54b6cfa... Initial Contribution
** limitations under the License.
*/

#define LOG_TAG "StringBlock"

#include "jni.h"
<<<<<<< HEAD
#include "JNIHelp.h"
=======
>>>>>>> 54b6cfa... Initial Contribution
#include <utils/misc.h>
#include <android_runtime/AndroidRuntime.h>
#include <utils/Log.h>

<<<<<<< HEAD
#include <androidfw/ResourceTypes.h>
=======
#include <utils/ResourceTypes.h>
>>>>>>> 54b6cfa... Initial Contribution

#include <stdio.h>

namespace android {

// ----------------------------------------------------------------------------

<<<<<<< HEAD
=======
static void doThrow(JNIEnv* env, const char* exc, const char* msg = NULL)
{
    jclass npeClazz;

    npeClazz = env->FindClass(exc);
    LOG_FATAL_IF(npeClazz == NULL, "Unable to find class %s", exc);

    env->ThrowNew(npeClazz, msg);
}

>>>>>>> 54b6cfa... Initial Contribution
static jint android_content_StringBlock_nativeCreate(JNIEnv* env, jobject clazz,
                                                  jbyteArray bArray,
                                                  jint off, jint len)
{
    if (bArray == NULL) {
<<<<<<< HEAD
        jniThrowNullPointerException(env, NULL);
=======
        doThrow(env, "java/lang/NullPointerException");
>>>>>>> 54b6cfa... Initial Contribution
        return 0;
    }

    jsize bLen = env->GetArrayLength(bArray);
    if (off < 0 || off >= bLen || len < 0 || len > bLen || (off+len) > bLen) {
<<<<<<< HEAD
        jniThrowException(env, "java/lang/IndexOutOfBoundsException", NULL);
=======
        doThrow(env, "java/lang/IndexOutOfBoundsException");
>>>>>>> 54b6cfa... Initial Contribution
        return 0;
    }

    jbyte* b = env->GetByteArrayElements(bArray, NULL);
    ResStringPool* osb = new ResStringPool(b+off, len, true);
    env->ReleaseByteArrayElements(bArray, b, 0);

    if (osb == NULL || osb->getError() != NO_ERROR) {
<<<<<<< HEAD
        jniThrowException(env, "java/lang/IllegalArgumentException", NULL);
=======
        doThrow(env, "java/lang/IllegalArgumentException");
>>>>>>> 54b6cfa... Initial Contribution
        return 0;
    }

    return (jint)osb;
}

static jint android_content_StringBlock_nativeGetSize(JNIEnv* env, jobject clazz,
                                                   jint token)
{
    ResStringPool* osb = (ResStringPool*)token;
    if (osb == NULL) {
<<<<<<< HEAD
        jniThrowNullPointerException(env, NULL);
=======
        doThrow(env, "java/lang/NullPointerException");
>>>>>>> 54b6cfa... Initial Contribution
        return 0;
    }

    return osb->size();
}

static jstring android_content_StringBlock_nativeGetString(JNIEnv* env, jobject clazz,
                                                        jint token, jint idx)
{
    ResStringPool* osb = (ResStringPool*)token;
    if (osb == NULL) {
<<<<<<< HEAD
        jniThrowNullPointerException(env, NULL);
=======
        doThrow(env, "java/lang/NullPointerException");
>>>>>>> 54b6cfa... Initial Contribution
        return 0;
    }

    size_t len;
<<<<<<< HEAD
    const char* str8 = osb->string8At(idx, &len);
    if (str8 != NULL) {
        return env->NewStringUTF(str8);
    }

    const char16_t* str = osb->stringAt(idx, &len);
    if (str == NULL) {
        jniThrowException(env, "java/lang/IndexOutOfBoundsException", NULL);
=======
    const char16_t* str = osb->stringAt(idx, &len);
    if (str == NULL) {
        doThrow(env, "java/lang/IndexOutOfBoundsException");
>>>>>>> 54b6cfa... Initial Contribution
        return 0;
    }

    return env->NewString((const jchar*)str, len);
}

static jintArray android_content_StringBlock_nativeGetStyle(JNIEnv* env, jobject clazz,
                                                         jint token, jint idx)
{
    ResStringPool* osb = (ResStringPool*)token;
    if (osb == NULL) {
<<<<<<< HEAD
        jniThrowNullPointerException(env, NULL);
=======
        doThrow(env, "java/lang/NullPointerException");
>>>>>>> 54b6cfa... Initial Contribution
        return NULL;
    }

    const ResStringPool_span* spans = osb->styleAt(idx);
    if (spans == NULL) {
        return NULL;
    }

    const ResStringPool_span* pos = spans;
    int num = 0;
    while (pos->name.index != ResStringPool_span::END) {
        num++;
        pos++;
    }

    if (num == 0) {
        return NULL;
    }

    jintArray array = env->NewIntArray((num*sizeof(ResStringPool_span))/sizeof(jint));
<<<<<<< HEAD
    if (array == NULL) { // NewIntArray already threw OutOfMemoryError.
=======
    if (array == NULL) {
        doThrow(env, "java/lang/OutOfMemoryError");
>>>>>>> 54b6cfa... Initial Contribution
        return NULL;
    }

    num = 0;
    static const int numInts = sizeof(ResStringPool_span)/sizeof(jint);
    while (spans->name.index != ResStringPool_span::END) {
        env->SetIntArrayRegion(array,
                                  num*numInts, numInts,
                                  (jint*)spans);
        spans++;
        num++;
    }

    return array;
}

<<<<<<< HEAD
=======
static jint android_content_StringBlock_nativeIndexOfString(JNIEnv* env, jobject clazz,
                                                         jint token, jstring str)
{
    ResStringPool* osb = (ResStringPool*)token;
    if (osb == NULL || str == NULL) {
        doThrow(env, "java/lang/NullPointerException");
        return 0;
    }

    const char16_t* str16 = env->GetStringChars(str, NULL);
    jsize strLen = env->GetStringLength(str);

    ssize_t idx = osb->indexOfString(str16, strLen);

    env->ReleaseStringChars(str, str16);

    return idx;
}

>>>>>>> 54b6cfa... Initial Contribution
static void android_content_StringBlock_nativeDestroy(JNIEnv* env, jobject clazz,
                                                   jint token)
{
    ResStringPool* osb = (ResStringPool*)token;
    if (osb == NULL) {
<<<<<<< HEAD
        jniThrowNullPointerException(env, NULL);
=======
        doThrow(env, "java/lang/NullPointerException");
>>>>>>> 54b6cfa... Initial Contribution
        return;
    }

    delete osb;
}

// ----------------------------------------------------------------------------

/*
 * JNI registration.
 */
static JNINativeMethod gStringBlockMethods[] = {
    /* name, signature, funcPtr */
    { "nativeCreate",      "([BII)I",
            (void*) android_content_StringBlock_nativeCreate },
    { "nativeGetSize",      "(I)I",
            (void*) android_content_StringBlock_nativeGetSize },
    { "nativeGetString",    "(II)Ljava/lang/String;",
            (void*) android_content_StringBlock_nativeGetString },
    { "nativeGetStyle",    "(II)[I",
            (void*) android_content_StringBlock_nativeGetStyle },
<<<<<<< HEAD
=======
    { "nativeIndexOfString","(ILjava/lang/String;)I",
            (void*) android_content_StringBlock_nativeIndexOfString },
>>>>>>> 54b6cfa... Initial Contribution
    { "nativeDestroy",      "(I)V",
            (void*) android_content_StringBlock_nativeDestroy },
};

int register_android_content_StringBlock(JNIEnv* env)
{
    return AndroidRuntime::registerNativeMethods(env,
            "android/content/res/StringBlock", gStringBlockMethods, NELEM(gStringBlockMethods));
}

}; // namespace android
<<<<<<< HEAD
=======

>>>>>>> 54b6cfa... Initial Contribution
