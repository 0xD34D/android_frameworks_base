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

#undef LOG_TAG
#define LOG_TAG "CursorWindow"

#include <jni.h>
#include <JNIHelp.h>
#include <android_runtime/AndroidRuntime.h>

#include <utils/Log.h>
#include <utils/String8.h>
#include <utils/String16.h>
<<<<<<< HEAD
#include <utils/Unicode.h>
=======
>>>>>>> 54b6cfa... Initial Contribution

#include <stdio.h>
#include <string.h>
#include <unistd.h>

<<<<<<< HEAD
#include <androidfw/CursorWindow.h>
#include "android_os_Parcel.h"
#include "android_util_Binder.h"
#include "android_database_SQLiteCommon.h"

namespace android {

static struct {
    jfieldID data;
    jfieldID sizeCopied;
} gCharArrayBufferClassInfo;

static jstring gEmptyString;

static void throwExceptionWithRowCol(JNIEnv* env, jint row, jint column) {
    String8 msg;
    msg.appendFormat("Couldn't read row %d, col %d from CursorWindow.  "
            "Make sure the Cursor is initialized correctly before accessing data from it.",
            row, column);
    jniThrowException(env, "java/lang/IllegalStateException", msg.string());
}

static void throwUnknownTypeException(JNIEnv * env, jint type) {
    String8 msg;
    msg.appendFormat("UNKNOWN type %d", type);
    jniThrowException(env, "java/lang/IllegalStateException", msg.string());
}

static jint nativeCreate(JNIEnv* env, jclass clazz, jstring nameObj, jint cursorWindowSize) {
    String8 name;
    const char* nameStr = env->GetStringUTFChars(nameObj, NULL);
    name.setTo(nameStr);
    env->ReleaseStringUTFChars(nameObj, nameStr);

    CursorWindow* window;
    status_t status = CursorWindow::create(name, cursorWindowSize, &window);
    if (status || !window) {
        ALOGE("Could not allocate CursorWindow '%s' of size %d due to error %d.",
                name.string(), cursorWindowSize, status);
        return 0;
    }

    LOG_WINDOW("nativeInitializeEmpty: window = %p", window);
    return reinterpret_cast<jint>(window);
}

static jint nativeCreateFromParcel(JNIEnv* env, jclass clazz, jobject parcelObj) {
    Parcel* parcel = parcelForJavaObject(env, parcelObj);

    CursorWindow* window;
    status_t status = CursorWindow::createFromParcel(parcel, &window);
    if (status || !window) {
        ALOGE("Could not create CursorWindow from Parcel due to error %d.", status);
        return 0;
    }

    LOG_WINDOW("nativeInitializeFromBinder: numRows = %d, numColumns = %d, window = %p",
            window->getNumRows(), window->getNumColumns(), window);
    return reinterpret_cast<jint>(window);
}

static void nativeDispose(JNIEnv* env, jclass clazz, jint windowPtr) {
    CursorWindow* window = reinterpret_cast<CursorWindow*>(windowPtr);
    if (window) {
        LOG_WINDOW("Closing window %p", window);
        delete window;
    }
}

static jstring nativeGetName(JNIEnv* env, jclass clazz, jint windowPtr) {
    CursorWindow* window = reinterpret_cast<CursorWindow*>(windowPtr);
    return env->NewStringUTF(window->name().string());
}

static void nativeWriteToParcel(JNIEnv * env, jclass clazz, jint windowPtr,
        jobject parcelObj) {
    CursorWindow* window = reinterpret_cast<CursorWindow*>(windowPtr);
    Parcel* parcel = parcelForJavaObject(env, parcelObj);

    status_t status = window->writeToParcel(parcel);
    if (status) {
        String8 msg;
        msg.appendFormat("Could not write CursorWindow to Parcel due to error %d.", status);
        jniThrowRuntimeException(env, msg.string());
    }
}

static void nativeClear(JNIEnv * env, jclass clazz, jint windowPtr) {
    CursorWindow* window = reinterpret_cast<CursorWindow*>(windowPtr);
    LOG_WINDOW("Clearing window %p", window);
    status_t status = window->clear();
    if (status) {
        LOG_WINDOW("Could not clear window. error=%d", status);
    }
}

static jint nativeGetNumRows(JNIEnv* env, jclass clazz, jint windowPtr) {
    CursorWindow* window = reinterpret_cast<CursorWindow*>(windowPtr);
    return window->getNumRows();
}

static jboolean nativeSetNumColumns(JNIEnv* env, jclass clazz, jint windowPtr,
        jint columnNum) {
    CursorWindow* window = reinterpret_cast<CursorWindow*>(windowPtr);
    status_t status = window->setNumColumns(columnNum);
    return status == OK;
}

static jboolean nativeAllocRow(JNIEnv* env, jclass clazz, jint windowPtr) {
    CursorWindow* window = reinterpret_cast<CursorWindow*>(windowPtr);
    status_t status = window->allocRow();
    return status == OK;
}

static void nativeFreeLastRow(JNIEnv* env, jclass clazz, jint windowPtr) {
    CursorWindow* window = reinterpret_cast<CursorWindow*>(windowPtr);
    window->freeLastRow();
}

static jint nativeGetType(JNIEnv* env, jclass clazz, jint windowPtr,
        jint row, jint column) {
    CursorWindow* window = reinterpret_cast<CursorWindow*>(windowPtr);
    LOG_WINDOW("returning column type affinity for %d,%d from %p", row, column, window);

    CursorWindow::FieldSlot* fieldSlot = window->getFieldSlot(row, column);
    if (!fieldSlot) {
        // FIXME: This is really broken but we have CTS tests that depend
        // on this legacy behavior.
        //throwExceptionWithRowCol(env, row, column);
        return CursorWindow::FIELD_TYPE_NULL;
    }
    return window->getFieldSlotType(fieldSlot);
}

static jbyteArray nativeGetBlob(JNIEnv* env, jclass clazz, jint windowPtr,
        jint row, jint column) {
    CursorWindow* window = reinterpret_cast<CursorWindow*>(windowPtr);
    LOG_WINDOW("Getting blob for %d,%d from %p", row, column, window);

    CursorWindow::FieldSlot* fieldSlot = window->getFieldSlot(row, column);
    if (!fieldSlot) {
=======
#include "CursorWindow.h"
#include "sqlite3_exception.h"
#include "android_util_Binder.h"


namespace android {

static jfieldID gWindowField;
static jfieldID gBufferField;
static jfieldID gSizeCopiedField;

#define GET_WINDOW(env, object) ((CursorWindow *)env->GetIntField(object, gWindowField))
#define SET_WINDOW(env, object, window) (env->SetIntField(object, gWindowField, (int)window))
#define SET_BUFFER(env, object, buf) (env->SetObjectField(object, gBufferField, buf))
#define SET_SIZE_COPIED(env, object, size) (env->SetIntField(object, gSizeCopiedField, size))

CursorWindow * get_window_from_object(JNIEnv * env, jobject javaWindow)
{
    return GET_WINDOW(env, javaWindow);
}

static void native_init_empty(JNIEnv * env, jobject object, jboolean localOnly)
{
    uint8_t * data;
    size_t size;
    CursorWindow * window;

    window = new CursorWindow(MAX_WINDOW_SIZE);
    if (!window) {
        jniThrowException(env, "java/lang/RuntimeException", "No memory for native window object");
        return;
    }

    if (!window->initBuffer(localOnly)) {
        jniThrowException(env, "java/lang/IllegalStateException", "Couldn't init cursor window");
        delete window;
        return;
    }

LOG_WINDOW("native_init_empty: window = %p", window);
    SET_WINDOW(env, object, window);
}

static void native_init_memory(JNIEnv * env, jobject object, jobject memObj)
{
    sp<IMemory> memory = interface_cast<IMemory>(ibinderForJavaObject(env, memObj));
    if (memory == NULL) {
        jniThrowException(env, "java/lang/IllegalStateException", "Couldn't get native binder");
        return;
    }

    CursorWindow * window = new CursorWindow();
    if (!window) {
        jniThrowException(env, "java/lang/RuntimeException", "No memory for native window object");
        return;
    }
    if (!window->setMemory(memory)) {
        jniThrowException(env, "java/lang/RuntimeException", "No memory in memObj");
        delete window;
        return;
    }

LOG_WINDOW("native_init_memory: numRows = %d, numColumns = %d, window = %p", window->getNumRows(), window->getNumColumns(), window);
    SET_WINDOW(env, object, window);
}

static jobject native_getBinder(JNIEnv * env, jobject object)
{
    CursorWindow * window = GET_WINDOW(env, object);
    if (window) {
        sp<IMemory> memory = window->getMemory();
        if (memory != NULL) {
            sp<IBinder> binder = memory->asBinder();
            return javaObjectForIBinder(env, binder);
        }
    }
    return NULL;
}

static void native_clear(JNIEnv * env, jobject object)
{
    CursorWindow * window = GET_WINDOW(env, object);
LOG_WINDOW("Clearing window %p", window);
    if (window == NULL) {
        jniThrowException(env, "java/lang/IllegalStateException", "clear() called after close()");
        return;
    }
    window->clear();
}

static void native_close(JNIEnv * env, jobject object)
{
    CursorWindow * window = GET_WINDOW(env, object);
    if (window) {
LOG_WINDOW("Closing window %p", window);
        delete window;
        SET_WINDOW(env, object, 0);
    }
}

static void throwExceptionWithRowCol(JNIEnv * env, jint row, jint column)
{
    char buf[100];
    snprintf(buf, sizeof(buf), "get field slot from row %d col %d failed", row, column);
    jniThrowException(env, "java/lang/IllegalStateException", buf);
}

static void throwUnknowTypeException(JNIEnv * env, jint type)
{
    char buf[80];
    snprintf(buf, sizeof(buf), "UNKNOWN type %d", type);
    jniThrowException(env, "java/lang/IllegalStateException", buf);
}

static jlong getLong_native(JNIEnv * env, jobject object, jint row, jint column)
{
    int32_t err;
    CursorWindow * window = GET_WINDOW(env, object);
LOG_WINDOW("Getting long for %d,%d from %p", row, column, window);

    field_slot_t field;
    err = window->read_field_slot(row, column, &field);
    if (err != 0) {
        throwExceptionWithRowCol(env, row, column);
        return 0;
    }

    uint8_t type = field.type;
    if (type == FIELD_TYPE_INTEGER) {
        int64_t value;
        if (window->getLong(row, column, &value)) {
            return value;
        }
        return 0;
    } else if (type == FIELD_TYPE_STRING) {
        uint32_t size = field.data.buffer.size;
        if (size > 0) {
#if WINDOW_STORAGE_UTF8
            return strtoll((char const *)window->offsetToPtr(field.data.buffer.offset), NULL, 0);
#else
            String8 ascii((char16_t *) window->offsetToPtr(field.data.buffer.offset), size / 2);
            char const * str = ascii.string();
            return strtoll(str, NULL, 0);
#endif
        } else {
            return 0;
        }
    } else if (type == FIELD_TYPE_FLOAT) {
        double value;
        if (window->getDouble(row, column, &value)) {
            return value;
        }
        return 0;
    } else if (type == FIELD_TYPE_NULL) {
        return 0;
    } else if (type == FIELD_TYPE_BLOB) {
        throw_sqlite3_exception(env, "Unable to convert BLOB to long");
        return 0;
    } else {
        throwUnknowTypeException(env, type);
        return 0;
    }
}

static jbyteArray getBlob_native(JNIEnv* env, jobject object, jint row, jint column)
{
    int32_t err;
    CursorWindow * window = GET_WINDOW(env, object);
LOG_WINDOW("Getting blob for %d,%d from %p", row, column, window);

    field_slot_t field;
    err = window->read_field_slot(row, column, &field);
    if (err != 0) {
>>>>>>> 54b6cfa... Initial Contribution
        throwExceptionWithRowCol(env, row, column);
        return NULL;
    }

<<<<<<< HEAD
    int32_t type = window->getFieldSlotType(fieldSlot);
    if (type == CursorWindow::FIELD_TYPE_BLOB || type == CursorWindow::FIELD_TYPE_STRING) {
        size_t size;
        const void* value = window->getFieldSlotValueBlob(fieldSlot, &size);
        jbyteArray byteArray = env->NewByteArray(size);
        if (!byteArray) {
            env->ExceptionClear();
            throw_sqlite3_exception(env, "Native could not create new byte[]");
            return NULL;
        }
        env->SetByteArrayRegion(byteArray, 0, size, static_cast<const jbyte*>(value));
        return byteArray;
    } else if (type == CursorWindow::FIELD_TYPE_INTEGER) {
        throw_sqlite3_exception(env, "INTEGER data in nativeGetBlob ");
    } else if (type == CursorWindow::FIELD_TYPE_FLOAT) {
        throw_sqlite3_exception(env, "FLOAT data in nativeGetBlob ");
    } else if (type == CursorWindow::FIELD_TYPE_NULL) {
        // do nothing
    } else {
        throwUnknownTypeException(env, type);
=======
    uint8_t type = field.type;
    if (type == FIELD_TYPE_BLOB || type == FIELD_TYPE_STRING) {
        jbyteArray byteArray = env->NewByteArray(field.data.buffer.size);
        LOG_ASSERT(byteArray, "Native could not create new byte[]");
        env->SetByteArrayRegion(byteArray, 0, field.data.buffer.size,
            (const jbyte*)window->offsetToPtr(field.data.buffer.offset));
        return byteArray;
    } else if (type == FIELD_TYPE_INTEGER) {
        throw_sqlite3_exception(env, "INTEGER data in getBlob_native ");
    } else if (type == FIELD_TYPE_FLOAT) {
        throw_sqlite3_exception(env, "FLOAT data in getBlob_native ");
    } else if (type == FIELD_TYPE_NULL) {
        // do nothing
    } else {
        throwUnknowTypeException(env, type);
>>>>>>> 54b6cfa... Initial Contribution
    }
    return NULL;
}

<<<<<<< HEAD
static jstring nativeGetString(JNIEnv* env, jclass clazz, jint windowPtr,
        jint row, jint column) {
    CursorWindow* window = reinterpret_cast<CursorWindow*>(windowPtr);
    LOG_WINDOW("Getting string for %d,%d from %p", row, column, window);

    CursorWindow::FieldSlot* fieldSlot = window->getFieldSlot(row, column);
    if (!fieldSlot) {
=======
static jboolean isBlob_native(JNIEnv* env, jobject object, jint row, jint column)
{
    int32_t err;
    CursorWindow * window = GET_WINDOW(env, object);
LOG_WINDOW("Checking if column is a blob for %d,%d from %p", row, column, window);

    field_slot_t field;
    err = window->read_field_slot(row, column, &field);
    if (err != 0) {
>>>>>>> 54b6cfa... Initial Contribution
        throwExceptionWithRowCol(env, row, column);
        return NULL;
    }

<<<<<<< HEAD
    int32_t type = window->getFieldSlotType(fieldSlot);
    if (type == CursorWindow::FIELD_TYPE_STRING) {
        size_t sizeIncludingNull;
        const char* value = window->getFieldSlotValueString(fieldSlot, &sizeIncludingNull);
        if (sizeIncludingNull <= 1) {
            return gEmptyString;
        }
        // Convert to UTF-16 here instead of calling NewStringUTF.  NewStringUTF
        // doesn't like UTF-8 strings with high codepoints.  It actually expects
        // Modified UTF-8 with encoded surrogate pairs.
        String16 utf16(value, sizeIncludingNull - 1);
        return env->NewString(reinterpret_cast<const jchar*>(utf16.string()), utf16.size());
    } else if (type == CursorWindow::FIELD_TYPE_INTEGER) {
        int64_t value = window->getFieldSlotValueLong(fieldSlot);
        char buf[32];
        snprintf(buf, sizeof(buf), "%lld", value);
        return env->NewStringUTF(buf);
    } else if (type == CursorWindow::FIELD_TYPE_FLOAT) {
        double value = window->getFieldSlotValueDouble(fieldSlot);
        char buf[32];
        snprintf(buf, sizeof(buf), "%g", value);
        return env->NewStringUTF(buf);
    } else if (type == CursorWindow::FIELD_TYPE_NULL) {
        return NULL;
    } else if (type == CursorWindow::FIELD_TYPE_BLOB) {
        throw_sqlite3_exception(env, "Unable to convert BLOB to string");
        return NULL;
    } else {
        throwUnknownTypeException(env, type);
=======
    return field.type == FIELD_TYPE_BLOB || field.type == FIELD_TYPE_NULL;
}

static jstring getString_native(JNIEnv* env, jobject object, jint row, jint column)
{
    int32_t err;
    CursorWindow * window = GET_WINDOW(env, object);
LOG_WINDOW("Getting string for %d,%d from %p", row, column, window);

    field_slot_t field;
    err = window->read_field_slot(row, column, &field);
    if (err != 0) {
        throwExceptionWithRowCol(env, row, column);
        return NULL;
    }

    uint8_t type = field.type;
    if (type == FIELD_TYPE_STRING) {
        uint32_t size = field.data.buffer.size;
        if (size > 0) {
#if WINDOW_STORAGE_UTF8
            // Pass size - 1 since the UTF8 is null terminated and we don't want a null terminator on the UTF16 string
            String16 utf16((char const *)window->offsetToPtr(field.data.buffer.offset), size - 1);
            return env->NewString((jchar const *)utf16.string(), utf16.size());
#else
            return env->NewString((jchar const *)window->offsetToPtr(field.data.buffer.offset), size / 2);
#endif
        } else {
            return env->NewStringUTF("");
        }
    } else if (type == FIELD_TYPE_INTEGER) {
        int64_t value;
        if (window->getLong(row, column, &value)) {
            char buf[32];
            snprintf(buf, sizeof(buf), "%lld", value);
            return env->NewStringUTF(buf);
        }
        return NULL;
    } else if (type == FIELD_TYPE_FLOAT) {
        double value;
        if (window->getDouble(row, column, &value)) {
            char buf[32];
            snprintf(buf, sizeof(buf), "%g", value);
            return env->NewStringUTF(buf);
        }
        return NULL;
    } else if (type == FIELD_TYPE_NULL) {
        return NULL;
    } else if (type == FIELD_TYPE_BLOB) {
        throw_sqlite3_exception(env, "Unable to convert BLOB to string");
        return NULL;
    } else {
        throwUnknowTypeException(env, type);
>>>>>>> 54b6cfa... Initial Contribution
        return NULL;
    }
}

<<<<<<< HEAD
static jcharArray allocCharArrayBuffer(JNIEnv* env, jobject bufferObj, size_t size) {
    jcharArray dataObj = jcharArray(env->GetObjectField(bufferObj,
            gCharArrayBufferClassInfo.data));
    if (dataObj && size) {
        jsize capacity = env->GetArrayLength(dataObj);
        if (size_t(capacity) < size) {
            env->DeleteLocalRef(dataObj);
            dataObj = NULL;
        }
    }
    if (!dataObj) {
        jsize capacity = size;
        if (capacity < 64) {
            capacity = 64;
        }
        dataObj = env->NewCharArray(capacity); // might throw OOM
        if (dataObj) {
            env->SetObjectField(bufferObj, gCharArrayBufferClassInfo.data, dataObj);
        }
    }
    return dataObj;
}

static void fillCharArrayBufferUTF(JNIEnv* env, jobject bufferObj,
        const char* str, size_t len) {
    ssize_t size = utf8_to_utf16_length(reinterpret_cast<const uint8_t*>(str), len);
    if (size < 0) {
        size = 0; // invalid UTF8 string
    }
    jcharArray dataObj = allocCharArrayBuffer(env, bufferObj, size);
    if (dataObj) {
        if (size) {
            jchar* data = static_cast<jchar*>(env->GetPrimitiveArrayCritical(dataObj, NULL));
            utf8_to_utf16_no_null_terminator(reinterpret_cast<const uint8_t*>(str), len,
                    reinterpret_cast<char16_t*>(data));
            env->ReleasePrimitiveArrayCritical(dataObj, data, 0);
        }
        env->SetIntField(bufferObj, gCharArrayBufferClassInfo.sizeCopied, size);
    }
}

static void clearCharArrayBuffer(JNIEnv* env, jobject bufferObj) {
    jcharArray dataObj = allocCharArrayBuffer(env, bufferObj, 0);
    if (dataObj) {
        env->SetIntField(bufferObj, gCharArrayBufferClassInfo.sizeCopied, 0);
    }
}

static void nativeCopyStringToBuffer(JNIEnv* env, jclass clazz, jint windowPtr,
        jint row, jint column, jobject bufferObj) {
    CursorWindow* window = reinterpret_cast<CursorWindow*>(windowPtr);
    LOG_WINDOW("Copying string for %d,%d from %p", row, column, window);

    CursorWindow::FieldSlot* fieldSlot = window->getFieldSlot(row, column);
    if (!fieldSlot) {
        throwExceptionWithRowCol(env, row, column);
        return;
    }

    int32_t type = window->getFieldSlotType(fieldSlot);
    if (type == CursorWindow::FIELD_TYPE_STRING) {
        size_t sizeIncludingNull;
        const char* value = window->getFieldSlotValueString(fieldSlot, &sizeIncludingNull);
        if (sizeIncludingNull > 1) {
            fillCharArrayBufferUTF(env, bufferObj, value, sizeIncludingNull - 1);
        } else {
            clearCharArrayBuffer(env, bufferObj);
        }
    } else if (type == CursorWindow::FIELD_TYPE_INTEGER) {
        int64_t value = window->getFieldSlotValueLong(fieldSlot);
        char buf[32];
        snprintf(buf, sizeof(buf), "%lld", value);
        fillCharArrayBufferUTF(env, bufferObj, buf, strlen(buf));
    } else if (type == CursorWindow::FIELD_TYPE_FLOAT) {
        double value = window->getFieldSlotValueDouble(fieldSlot);
        char buf[32];
        snprintf(buf, sizeof(buf), "%g", value);
        fillCharArrayBufferUTF(env, bufferObj, buf, strlen(buf));
    } else if (type == CursorWindow::FIELD_TYPE_NULL) {
        clearCharArrayBuffer(env, bufferObj);
    } else if (type == CursorWindow::FIELD_TYPE_BLOB) {
        throw_sqlite3_exception(env, "Unable to convert BLOB to string");
    } else {
        throwUnknownTypeException(env, type);
    }
}

static jlong nativeGetLong(JNIEnv* env, jclass clazz, jint windowPtr,
        jint row, jint column) {
    CursorWindow* window = reinterpret_cast<CursorWindow*>(windowPtr);
    LOG_WINDOW("Getting long for %d,%d from %p", row, column, window);

    CursorWindow::FieldSlot* fieldSlot = window->getFieldSlot(row, column);
    if (!fieldSlot) {
        throwExceptionWithRowCol(env, row, column);
        return 0;
    }

    int32_t type = window->getFieldSlotType(fieldSlot);
    if (type == CursorWindow::FIELD_TYPE_INTEGER) {
        return window->getFieldSlotValueLong(fieldSlot);
    } else if (type == CursorWindow::FIELD_TYPE_STRING) {
        size_t sizeIncludingNull;
        const char* value = window->getFieldSlotValueString(fieldSlot, &sizeIncludingNull);
        return sizeIncludingNull > 1 ? strtoll(value, NULL, 0) : 0L;
    } else if (type == CursorWindow::FIELD_TYPE_FLOAT) {
        return jlong(window->getFieldSlotValueDouble(fieldSlot));
    } else if (type == CursorWindow::FIELD_TYPE_NULL) {
        return 0;
    } else if (type == CursorWindow::FIELD_TYPE_BLOB) {
        throw_sqlite3_exception(env, "Unable to convert BLOB to long");
        return 0;
    } else {
        throwUnknownTypeException(env, type);
        return 0;
    }
}

static jdouble nativeGetDouble(JNIEnv* env, jclass clazz, jint windowPtr,
        jint row, jint column) {
    CursorWindow* window = reinterpret_cast<CursorWindow*>(windowPtr);
    LOG_WINDOW("Getting double for %d,%d from %p", row, column, window);

    CursorWindow::FieldSlot* fieldSlot = window->getFieldSlot(row, column);
    if (!fieldSlot) {
=======
/**
 * Use this only to convert characters that are known to be within the
 * 0-127 range for direct conversion to UTF-16
 */
static jint charToJchar(const char* src, jchar* dst, jint bufferSize)
{
    int32_t len = strlen(src);

    if (bufferSize < len) {
        len = bufferSize;
    }

    for (int i = 0; i < len; i++) {
        *dst++ = (*src++ & 0x7F);
    }
    return len;
}

static jcharArray copyStringToBuffer_native(JNIEnv* env, jobject object, jint row,
                                      jint column, jint bufferSize, jobject buf)
{
    int32_t err;
    CursorWindow * window = GET_WINDOW(env, object);
LOG_WINDOW("Copying string for %d,%d from %p", row, column, window);

    field_slot_t field;
    err = window->read_field_slot(row, column, &field);
    if (err != 0) {
        jniThrowException(env, "java/lang/IllegalStateException", "Unable to get field slot");
        return NULL;
    }
    
    jcharArray buffer = (jcharArray)env->GetObjectField(buf, gBufferField);
    if (buffer == NULL) {
        jniThrowException(env, "java/lang/IllegalStateException", "buf should not be null");
        return NULL;        
    }
    jchar* dst = env->GetCharArrayElements(buffer, NULL);
    uint8_t type = field.type;
    uint32_t sizeCopied = 0;
    jcharArray newArray = NULL;
    if (type == FIELD_TYPE_STRING) {
        uint32_t size = field.data.buffer.size;
        if (size > 0) {            
#if WINDOW_STORAGE_UTF8
            // Pass size - 1 since the UTF8 is null terminated and we don't want a null terminator on the UTF16 string
            String16 utf16((char const *)window->offsetToPtr(field.data.buffer.offset), size - 1);
            int32_t strSize = utf16.size();
            if (strSize > bufferSize || dst == NULL) {
                newArray = env->NewCharArray(strSize);
                env->SetCharArrayRegion(newArray, 0, strSize, (jchar const *)utf16.string());
            } else {                
                memcpy(dst, (jchar const *)utf16.string(), strSize * 2);
            }
            sizeCopied = strSize;
#else
            sizeCopied = size/2 + size % 2;
            if (size > bufferSize * 2 || dst == NULL) {
                newArray = env->NewCharArray(sizeCopied);
                memcpy(newArray, (jchar const *)window->offsetToPtr(field.data.buffer.offset), size);
            } else {
                memcpy(dst, (jchar const *)window->offsetToPtr(field.data.buffer.offset), size);
            }
#endif
        } 
    } else if (type == FIELD_TYPE_INTEGER) {
        int64_t value;
        if (window->getLong(row, column, &value)) {
            char buf[32];
            int len;
            snprintf(buf, sizeof(buf), "%lld", value);
            jchar* dst = env->GetCharArrayElements(buffer, NULL);
            sizeCopied = charToJchar(buf, dst, bufferSize);
         }
    } else if (type == FIELD_TYPE_FLOAT) {
        double value;
        if (window->getDouble(row, column, &value)) {
            char tempbuf[32];
            snprintf(tempbuf, sizeof(tempbuf), "%g", value);
            jchar* dst = env->GetCharArrayElements(buffer, NULL);
            sizeCopied = charToJchar(tempbuf, dst, bufferSize);
        }
    } else if (type == FIELD_TYPE_NULL) {
    } else if (type == FIELD_TYPE_BLOB) {
        throw_sqlite3_exception(env, "Unable to convert BLOB to string");
    } else {
        LOGE("Unknown field type %d", type);
        throw_sqlite3_exception(env, "UNKNOWN type in copyStringToBuffer_native()");
    }
    SET_SIZE_COPIED(env, buf, sizeCopied);
    env->ReleaseCharArrayElements(buffer, dst, JNI_OK);
    return newArray;
}

static jdouble getDouble_native(JNIEnv* env, jobject object, jint row, jint column)
{
    int32_t err;
    CursorWindow * window = GET_WINDOW(env, object);
LOG_WINDOW("Getting double for %d,%d from %p", row, column, window);

    field_slot_t field;
    err = window->read_field_slot(row, column, &field);
    if (err != 0) {
>>>>>>> 54b6cfa... Initial Contribution
        throwExceptionWithRowCol(env, row, column);
        return 0.0;
    }

<<<<<<< HEAD
    int32_t type = window->getFieldSlotType(fieldSlot);
    if (type == CursorWindow::FIELD_TYPE_FLOAT) {
        return window->getFieldSlotValueDouble(fieldSlot);
    } else if (type == CursorWindow::FIELD_TYPE_STRING) {
        size_t sizeIncludingNull;
        const char* value = window->getFieldSlotValueString(fieldSlot, &sizeIncludingNull);
        return sizeIncludingNull > 1 ? strtod(value, NULL) : 0.0;
    } else if (type == CursorWindow::FIELD_TYPE_INTEGER) {
        return jdouble(window->getFieldSlotValueLong(fieldSlot));
    } else if (type == CursorWindow::FIELD_TYPE_NULL) {
        return 0.0;
    } else if (type == CursorWindow::FIELD_TYPE_BLOB) {
        throw_sqlite3_exception(env, "Unable to convert BLOB to double");
        return 0.0;
    } else {
        throwUnknownTypeException(env, type);
=======
    uint8_t type = field.type;
    if (type == FIELD_TYPE_FLOAT) {
        double value;
        if (window->getDouble(row, column, &value)) {
            return value;
        }
        return 0.0;
    } else if (type == FIELD_TYPE_STRING) {
        uint32_t size = field.data.buffer.size;
        if (size > 0) {
#if WINDOW_STORAGE_UTF8
            return strtod((char const *)window->offsetToPtr(field.data.buffer.offset), NULL);
#else
            String8 ascii((char16_t *) window->offsetToPtr(field.data.buffer.offset), size / 2);
            char const * str = ascii.string();
            return strtod(str, NULL);
#endif
        } else {
            return 0.0;
        }
    } else if (type == FIELD_TYPE_INTEGER) {
        int64_t value;
        if (window->getLong(row, column, &value)) {
            return (double) value;
        }
        return 0.0;
    } else if (type == FIELD_TYPE_NULL) {
        return 0.0;
    } else if (type == FIELD_TYPE_BLOB) {
        throw_sqlite3_exception(env, "Unable to convert BLOB to double");
        return 0.0;
    } else {
        throwUnknowTypeException(env, type);
>>>>>>> 54b6cfa... Initial Contribution
        return 0.0;
    }
}

<<<<<<< HEAD
static jboolean nativePutBlob(JNIEnv* env, jclass clazz, jint windowPtr,
        jbyteArray valueObj, jint row, jint column) {
    CursorWindow* window = reinterpret_cast<CursorWindow*>(windowPtr);
    jsize len = env->GetArrayLength(valueObj);

    void* value = env->GetPrimitiveArrayCritical(valueObj, NULL);
    status_t status = window->putBlob(row, column, value, len);
    env->ReleasePrimitiveArrayCritical(valueObj, value, JNI_ABORT);

    if (status) {
        LOG_WINDOW("Failed to put blob. error=%d", status);
        return false;
    }

    LOG_WINDOW("%d,%d is BLOB with %u bytes", row, column, len);
    return true;
}

static jboolean nativePutString(JNIEnv* env, jclass clazz, jint windowPtr,
        jstring valueObj, jint row, jint column) {
    CursorWindow* window = reinterpret_cast<CursorWindow*>(windowPtr);

    size_t sizeIncludingNull = env->GetStringUTFLength(valueObj) + 1;
    const char* valueStr = env->GetStringUTFChars(valueObj, NULL);
    if (!valueStr) {
        LOG_WINDOW("value can't be transferred to UTFChars");
        return false;
    }
    status_t status = window->putString(row, column, valueStr, sizeIncludingNull);
    env->ReleaseStringUTFChars(valueObj, valueStr);

    if (status) {
        LOG_WINDOW("Failed to put string. error=%d", status);
        return false;
    }

    LOG_WINDOW("%d,%d is TEXT with %u bytes", row, column, sizeIncludingNull);
    return true;
}

static jboolean nativePutLong(JNIEnv* env, jclass clazz, jint windowPtr,
        jlong value, jint row, jint column) {
    CursorWindow* window = reinterpret_cast<CursorWindow*>(windowPtr);
    status_t status = window->putLong(row, column, value);

    if (status) {
        LOG_WINDOW("Failed to put long. error=%d", status);
        return false;
    }

    LOG_WINDOW("%d,%d is INTEGER 0x%016llx", row, column, value);
    return true;
}

static jboolean nativePutDouble(JNIEnv* env, jclass clazz, jint windowPtr,
        jdouble value, jint row, jint column) {
    CursorWindow* window = reinterpret_cast<CursorWindow*>(windowPtr);
    status_t status = window->putDouble(row, column, value);

    if (status) {
        LOG_WINDOW("Failed to put double. error=%d", status);
        return false;
    }

    LOG_WINDOW("%d,%d is FLOAT %lf", row, column, value);
    return true;
}

static jboolean nativePutNull(JNIEnv* env, jclass clazz, jint windowPtr,
        jint row, jint column) {
    CursorWindow* window = reinterpret_cast<CursorWindow*>(windowPtr);
    status_t status = window->putNull(row, column);

    if (status) {
        LOG_WINDOW("Failed to put null. error=%d", status);
        return false;
    }

    LOG_WINDOW("%d,%d is NULL", row, column);
    return true;
}

static JNINativeMethod sMethods[] =
{
    /* name, signature, funcPtr */
    { "nativeCreate", "(Ljava/lang/String;I)I",
            (void*)nativeCreate },
    { "nativeCreateFromParcel", "(Landroid/os/Parcel;)I",
            (void*)nativeCreateFromParcel },
    { "nativeDispose", "(I)V",
            (void*)nativeDispose },
    { "nativeWriteToParcel", "(ILandroid/os/Parcel;)V",
            (void*)nativeWriteToParcel },
    { "nativeGetName", "(I)Ljava/lang/String;",
            (void*)nativeGetName },
    { "nativeClear", "(I)V",
            (void*)nativeClear },
    { "nativeGetNumRows", "(I)I",
            (void*)nativeGetNumRows },
    { "nativeSetNumColumns", "(II)Z",
            (void*)nativeSetNumColumns },
    { "nativeAllocRow", "(I)Z",
            (void*)nativeAllocRow },
    { "nativeFreeLastRow", "(I)V",
            (void*)nativeFreeLastRow },
    { "nativeGetType", "(III)I",
            (void*)nativeGetType },
    { "nativeGetBlob", "(III)[B",
            (void*)nativeGetBlob },
    { "nativeGetString", "(III)Ljava/lang/String;",
            (void*)nativeGetString },
    { "nativeGetLong", "(III)J",
            (void*)nativeGetLong },
    { "nativeGetDouble", "(III)D",
            (void*)nativeGetDouble },
    { "nativeCopyStringToBuffer", "(IIILandroid/database/CharArrayBuffer;)V",
            (void*)nativeCopyStringToBuffer },
    { "nativePutBlob", "(I[BII)Z",
            (void*)nativePutBlob },
    { "nativePutString", "(ILjava/lang/String;II)Z",
            (void*)nativePutString },
    { "nativePutLong", "(IJII)Z",
            (void*)nativePutLong },
    { "nativePutDouble", "(IDII)Z",
            (void*)nativePutDouble },
    { "nativePutNull", "(III)Z",
            (void*)nativePutNull },
};

#define FIND_CLASS(var, className) \
        var = env->FindClass(className); \
        LOG_FATAL_IF(! var, "Unable to find class " className);

#define GET_FIELD_ID(var, clazz, fieldName, fieldDescriptor) \
        var = env->GetFieldID(clazz, fieldName, fieldDescriptor); \
        LOG_FATAL_IF(! var, "Unable to find field " fieldName);

int register_android_database_CursorWindow(JNIEnv * env)
{
    jclass clazz;
    FIND_CLASS(clazz, "android/database/CharArrayBuffer");

    GET_FIELD_ID(gCharArrayBufferClassInfo.data, clazz,
            "data", "[C");
    GET_FIELD_ID(gCharArrayBufferClassInfo.sizeCopied, clazz,
            "sizeCopied", "I");

    gEmptyString = jstring(env->NewGlobalRef(env->NewStringUTF("")));
    LOG_FATAL_IF(!gEmptyString, "Unable to create empty string");
=======
static jboolean isNull_native(JNIEnv* env, jobject object, jint row, jint column)
{
    CursorWindow * window = GET_WINDOW(env, object);
LOG_WINDOW("Checking for NULL at %d,%d from %p", row, column, window);

    bool isNull;
    if (window->getNull(row, column, &isNull)) {
        return isNull;
    }

    //TODO throw execption?
    return true;
}

static jint getNumRows(JNIEnv * env, jobject object)
{
    CursorWindow * window = GET_WINDOW(env, object);
    return window->getNumRows();
}

static jboolean setNumColumns(JNIEnv * env, jobject object, jint columnNum)
{
    CursorWindow * window = GET_WINDOW(env, object);
    return window->setNumColumns(columnNum);
}

static jboolean allocRow(JNIEnv * env, jobject object)
{
    CursorWindow * window = GET_WINDOW(env, object);
    return window->allocRow() != NULL;
}

static jboolean putBlob_native(JNIEnv * env, jobject object, jbyteArray value, jint row, jint col)
{
    CursorWindow * window = GET_WINDOW(env, object);
    if (!value) {
        LOG_WINDOW("How did a null value send to here");
        return false;
    }
    field_slot_t * fieldSlot = window->getFieldSlotWithCheck(row, col);
    if (fieldSlot == NULL) {
        LOG_WINDOW(" getFieldSlotWithCheck error ");
        return false;
    }

    jint len = env->GetArrayLength(value);
    int offset = window->alloc(len);
    if (!offset) {
        LOG_WINDOW("Failed allocating %u bytes", len);
        return false;
    }
    jbyte * bytes = env->GetByteArrayElements(value, NULL);
    window->copyIn(offset, (uint8_t const *)bytes, len);

    // This must be updated after the call to alloc(), since that
    // may move the field around in the window
    fieldSlot->type = FIELD_TYPE_BLOB;
    fieldSlot->data.buffer.offset = offset;
    fieldSlot->data.buffer.size = len;
    env->ReleaseByteArrayElements(value, bytes, JNI_ABORT);
    LOG_WINDOW("%d,%d is BLOB with %u bytes @ %d", row, col, len, offset);
    return true;
}

static jboolean putString_native(JNIEnv * env, jobject object, jstring value, jint row, jint col)
{
    CursorWindow * window = GET_WINDOW(env, object);
    if (!value) {
        LOG_WINDOW("How did a null value send to here");
        return false;
    }
    field_slot_t * fieldSlot = window->getFieldSlotWithCheck(row, col);
    if (fieldSlot == NULL) {
        LOG_WINDOW(" getFieldSlotWithCheck error ");
        return false;
    }

#if WINDOW_STORAGE_UTF8
    int len = env->GetStringUTFLength(value) + 1;
    char const * valStr = env->GetStringUTFChars(value, NULL);
#else
    int len = env->GetStringLength(value);
    // GetStringLength return number of chars and one char takes 2 bytes
    len *= 2;
    const jchar* valStr = env->GetStringChars(value, NULL);
#endif
    if (!valStr) {
        LOG_WINDOW("value can't be transfer to UTFChars");
        return false;
    }

    int offset = window->alloc(len);
    if (!offset) {
        LOG_WINDOW("Failed allocating %u bytes", len);
#if WINDOW_STORAGE_UTF8
        env->ReleaseStringUTFChars(value, valStr);
#else
        env->ReleaseStringChars(value, valStr);
#endif
        return false;
    }

    window->copyIn(offset, (uint8_t const *)valStr, len);

    // This must be updated after the call to alloc(), since that
    // may move the field around in the window
    fieldSlot->type = FIELD_TYPE_STRING;
    fieldSlot->data.buffer.offset = offset;
    fieldSlot->data.buffer.size = len;

    LOG_WINDOW("%d,%d is TEXT with %u bytes @ %d", row, col, len, offset);
#if WINDOW_STORAGE_UTF8
    env->ReleaseStringUTFChars(value, valStr);
#else
    env->ReleaseStringChars(value, valStr);
#endif

    return true;
}

static jboolean putLong_native(JNIEnv * env, jobject object, jlong value, jint row, jint col)
{
    CursorWindow * window = GET_WINDOW(env, object);
    if (!window->putLong(row, col, value)) {
        LOG_WINDOW(" getFieldSlotWithCheck error ");
        return false;
    }

    LOG_WINDOW("%d,%d is INTEGER 0x%016llx", row, col, value);

    return true;
}

static jboolean putDouble_native(JNIEnv * env, jobject object, jdouble value, jint row, jint col)
{
    CursorWindow * window = GET_WINDOW(env, object);
    if (!window->putDouble(row, col, value)) {
        LOG_WINDOW(" getFieldSlotWithCheck error ");
        return false;
    }

    LOG_WINDOW("%d,%d is FLOAT %lf", row, col, value);

    return true;
}

static jboolean putNull_native(JNIEnv * env, jobject object, jint row, jint col)
{
    CursorWindow * window = GET_WINDOW(env, object);
    if (!window->putNull(row, col)) {
        LOG_WINDOW(" getFieldSlotWithCheck error ");
        return false;
    }

    LOG_WINDOW("%d,%d is NULL", row, col);

    return true;
}

// free the last row
static void freeLastRow(JNIEnv * env, jobject object) {
    CursorWindow * window = GET_WINDOW(env, object);
    window->freeLastRow();
}

static JNINativeMethod sMethods[] =
{
     /* name, signature, funcPtr */
    {"native_init", "(Z)V", (void *)native_init_empty},
    {"native_init", "(Landroid/os/IBinder;)V", (void *)native_init_memory},
    {"native_getBinder", "()Landroid/os/IBinder;", (void *)native_getBinder},
    {"native_clear", "()V", (void *)native_clear},
    {"close_native", "()V", (void *)native_close},
    {"getLong_native", "(II)J", (void *)getLong_native},
    {"getBlob_native", "(II)[B", (void *)getBlob_native},
    {"isBlob_native", "(II)Z", (void *)isBlob_native},
    {"getString_native", "(II)Ljava/lang/String;", (void *)getString_native},
    {"copyStringToBuffer_native", "(IIILandroid/database/CharArrayBuffer;)[C", (void *)copyStringToBuffer_native},
    {"getDouble_native", "(II)D", (void *)getDouble_native},
    {"isNull_native", "(II)Z", (void *)isNull_native},
    {"getNumRows_native", "()I", (void *)getNumRows},
    {"setNumColumns_native", "(I)Z", (void *)setNumColumns},
    {"allocRow_native", "()Z", (void *)allocRow},
    {"putBlob_native", "([BII)Z", (void *)putBlob_native},
    {"putString_native", "(Ljava/lang/String;II)Z", (void *)putString_native},
    {"putLong_native", "(JII)Z", (void *)putLong_native},
    {"putDouble_native", "(DII)Z", (void *)putDouble_native},
    {"freeLastRow_native", "()V", (void *)freeLastRow},
    {"putNull_native", "(II)Z", (void *)putNull_native},
};

int register_android_database_CursorWindow(JNIEnv * env)
{
    jclass clazz;

    clazz = env->FindClass("android/database/CursorWindow");
    if (clazz == NULL) {
        LOGE("Can't find android/database/CursorWindow");
        return -1;
    }

    gWindowField = env->GetFieldID(clazz, "nWindow", "I");

    if (gWindowField == NULL) {
        LOGE("Error locating fields");
        return -1;
    }
    
    clazz =  env->FindClass("android/database/CharArrayBuffer");
    if (clazz == NULL) {
        LOGE("Can't find android/database/CharArrayBuffer");
        return -1;
    }

    gBufferField = env->GetFieldID(clazz, "data", "[C");

    if (gBufferField == NULL) {
        LOGE("Error locating fields data in CharArrayBuffer");
        return -1;
    }

    gSizeCopiedField = env->GetFieldID(clazz, "sizeCopied", "I");

    if (gSizeCopiedField == NULL) {
        LOGE("Error locating fields sizeCopied in CharArrayBuffer");
        return -1;
    }
>>>>>>> 54b6cfa... Initial Contribution

    return AndroidRuntime::registerNativeMethods(env, "android/database/CursorWindow",
            sMethods, NELEM(sMethods));
}

} // namespace android
