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

#include <fcntl.h>

#include "JNIHelp.h"
#include "android_runtime/AndroidRuntime.h"
#include "jni.h"
<<<<<<< HEAD
#include "cutils/logger.h"

// The size of the tag number comes out of the payload size.
#define MAX_EVENT_PAYLOAD (LOGGER_ENTRY_MAX_PAYLOAD - sizeof(int32_t))
=======
#include "utils/logger.h"

#define END_DELIMITER '\n'
#define INT_BUFFER_SIZE (sizeof(jbyte)+sizeof(jint)+sizeof(END_DELIMITER))
#define LONG_BUFFER_SIZE (sizeof(jbyte)+sizeof(jlong)+sizeof(END_DELIMITER))
#define INITAL_BUFFER_CAPACITY 256

#define MAX(a,b) ((a>b)?a:b)
>>>>>>> 54b6cfa... Initial Contribution

namespace android {

static jclass gCollectionClass;
static jmethodID gCollectionAddID;

static jclass gEventClass;
static jmethodID gEventInitID;

static jclass gIntegerClass;
static jfieldID gIntegerValueID;

<<<<<<< HEAD
=======
static jclass gListClass;
static jfieldID gListItemsID;

>>>>>>> 54b6cfa... Initial Contribution
static jclass gLongClass;
static jfieldID gLongValueID;

static jclass gStringClass;

<<<<<<< HEAD
=======
struct ByteBuf {
    size_t len;
    size_t capacity;
    uint8_t* buf;
    
    ByteBuf(size_t initSize) {
        buf = (uint8_t*)malloc(initSize);
        len = 0;
        capacity = initSize;        
    }
    
    ~ByteBuf() {
        free(buf);
    }
    
    bool ensureExtraCapacity(size_t extra) {
        size_t spaceNeeded = len + extra;
        if (spaceNeeded > capacity) {
            size_t newCapacity = MAX(spaceNeeded, 2 * capacity);
            void* newBuf = realloc(buf, newCapacity);
            if (newBuf == NULL) {
                return false;
            }
            capacity = newCapacity;
            buf = (uint8_t*)newBuf;
            return true;
        } else {
            return true;
        }
    }
 
    void putIntEvent(jint value) {
        bool succeeded = ensureExtraCapacity(INT_BUFFER_SIZE);
        buf[len++] = EVENT_TYPE_INT;
        memcpy(buf+len, &value, sizeof(jint));
        len += sizeof(jint);
    }

    void putByte(uint8_t value) {
        bool succeeded = ensureExtraCapacity(sizeof(uint8_t));
        buf[len++] = value;
    }

    void putLongEvent(jlong value) {
        bool succeeded = ensureExtraCapacity(LONG_BUFFER_SIZE);
        buf[len++] = EVENT_TYPE_LONG;
        memcpy(buf+len, &value, sizeof(jlong));
        len += sizeof(jlong);
    }


    void putStringEvent(JNIEnv* env, jstring value) {
        const char* strValue = env->GetStringUTFChars(value, NULL);
        uint32_t strLen = strlen(strValue); //env->GetStringUTFLength(value);
        bool succeeded = ensureExtraCapacity(1 + sizeof(uint32_t) + strLen);
        buf[len++] = EVENT_TYPE_STRING;
        memcpy(buf+len, &strLen, sizeof(uint32_t));
        len += sizeof(uint32_t);
        memcpy(buf+len, strValue, strLen);
        env->ReleaseStringUTFChars(value, strValue);
        len += strLen;
    }

    void putList(JNIEnv* env, jobject list) {
        jobjectArray items = (jobjectArray) env->GetObjectField(list, gListItemsID);
        if (items == NULL) {
            jniThrowException(env, "java/lang/NullPointerException", NULL);
            return;
        }

        jsize numItems = env->GetArrayLength(items);
        putByte(EVENT_TYPE_LIST);
        putByte(numItems);
        // We'd like to call GetPrimitveArrayCritical() but that might
        // not be safe since we're going to be doing some I/O
        for (int i = 0; i < numItems; i++) {
            jobject item = env->GetObjectArrayElement(items, i);
            if (env->IsInstanceOf(item, gIntegerClass)) {
                jint intVal = env->GetIntField(item, gIntegerValueID);
                putIntEvent(intVal);
            } else if (env->IsInstanceOf(item, gLongClass)) {
                jlong longVal = env->GetLongField(item, gLongValueID);
                putLongEvent(longVal);
            } else if (env->IsInstanceOf(item, gStringClass)) {
                putStringEvent(env, (jstring)item);
            } else if (env->IsInstanceOf(item, gListClass)) {
                putList(env, item);
            } else {
                jniThrowException(
                        env,
                        "java/lang/IllegalArgumentException",
                        "Attempt to log an illegal item type.");
                return;
            }
            env->DeleteLocalRef(item);
        }

        env->DeleteLocalRef(items);
    }
};

>>>>>>> 54b6cfa... Initial Contribution
/*
 * In class android.util.EventLog:
 *  static native int writeEvent(int tag, int value)
 */
static jint android_util_EventLog_writeEvent_Integer(JNIEnv* env, jobject clazz,
                                                     jint tag, jint value)
{
    return android_btWriteLog(tag, EVENT_TYPE_INT, &value, sizeof(value));
}

/*
 * In class android.util.EventLog:
 *  static native int writeEvent(long tag, long value)
 */
<<<<<<< HEAD
static jint android_util_EventLog_writeEvent_Long(JNIEnv* env, jobject clazz,
=======
static jint android_util_EventLog_writeEvent_Long(JNIEnv* env, jobject clazz, 
>>>>>>> 54b6cfa... Initial Contribution
                                                  jint tag, jlong value)
{
    return android_btWriteLog(tag, EVENT_TYPE_LONG, &value, sizeof(value));
}

/*
 * In class android.util.EventLog:
<<<<<<< HEAD
 *  static native int writeEvent(int tag, String value)
 */
static jint android_util_EventLog_writeEvent_String(JNIEnv* env, jobject clazz,
                                                    jint tag, jstring value) {
    uint8_t buf[MAX_EVENT_PAYLOAD];

    // Don't throw NPE -- I feel like it's sort of mean for a logging function
    // to be all crashy if you pass in NULL -- but make the NULL value explicit.
    const char *str = value != NULL ? env->GetStringUTFChars(value, NULL) : "NULL";
    jint len = strlen(str);
    const int max = sizeof(buf) - sizeof(len) - 2;  // Type byte, final newline
    if (len > max) len = max;

    buf[0] = EVENT_TYPE_STRING;
    memcpy(&buf[1], &len, sizeof(len));
    memcpy(&buf[1 + sizeof(len)], str, len);
    buf[1 + sizeof(len) + len] = '\n';

    if (value != NULL) env->ReleaseStringUTFChars(value, str);
    return android_bWriteLog(tag, buf, 2 + sizeof(len) + len);
=======
 *  static native int writeEvent(long tag, List value)
 */
static jint android_util_EventLog_writeEvent_List(JNIEnv* env, jobject clazz,
                                                  jint tag, jobject value) {
    if (value == NULL) {
        jclass clazz = env->FindClass("java/lang/IllegalArgumentException");
        env->ThrowNew(clazz, "writeEvent needs a value.");
        return -1;
    }
    ByteBuf byteBuf(INITAL_BUFFER_CAPACITY);
    byteBuf.putList(env, value);
    byteBuf.putByte((uint8_t)END_DELIMITER);
    int numBytesPut = byteBuf.len;
    int bytesWritten = android_bWriteLog(tag, byteBuf.buf, numBytesPut);
    return bytesWritten;
>>>>>>> 54b6cfa... Initial Contribution
}

/*
 * In class android.util.EventLog:
<<<<<<< HEAD
 *  static native int writeEvent(long tag, Object... value)
 */
static jint android_util_EventLog_writeEvent_Array(JNIEnv* env, jobject clazz,
                                                   jint tag, jobjectArray value) {
    if (value == NULL) {
        return android_util_EventLog_writeEvent_String(env, clazz, tag, NULL);
    }

    uint8_t buf[MAX_EVENT_PAYLOAD];
    const size_t max = sizeof(buf) - 1;  // leave room for final newline
    size_t pos = 2;  // Save room for type tag & array count

    jsize copied = 0, num = env->GetArrayLength(value);
    for (; copied < num && copied < 255; ++copied) {
        jobject item = env->GetObjectArrayElement(value, copied);
        if (item == NULL || env->IsInstanceOf(item, gStringClass)) {
            if (pos + 1 + sizeof(jint) > max) break;
            const char *str = item != NULL ? env->GetStringUTFChars((jstring) item, NULL) : "NULL";
            jint len = strlen(str);
            if (pos + 1 + sizeof(len) + len > max) len = max - pos - 1 - sizeof(len);
            buf[pos++] = EVENT_TYPE_STRING;
            memcpy(&buf[pos], &len, sizeof(len));
            memcpy(&buf[pos + sizeof(len)], str, len);
            pos += sizeof(len) + len;
            if (item != NULL) env->ReleaseStringUTFChars((jstring) item, str);
        } else if (env->IsInstanceOf(item, gIntegerClass)) {
            jint intVal = env->GetIntField(item, gIntegerValueID);
            if (pos + 1 + sizeof(intVal) > max) break;
            buf[pos++] = EVENT_TYPE_INT;
            memcpy(&buf[pos], &intVal, sizeof(intVal));
            pos += sizeof(intVal);
        } else if (env->IsInstanceOf(item, gLongClass)) {
            jlong longVal = env->GetLongField(item, gLongValueID);
            if (pos + 1 + sizeof(longVal) > max) break;
            buf[pos++] = EVENT_TYPE_LONG;
            memcpy(&buf[pos], &longVal, sizeof(longVal));
            pos += sizeof(longVal);
        } else {
            jniThrowException(env,
                    "java/lang/IllegalArgumentException",
                    "Invalid payload item type");
            return -1;
        }
        env->DeleteLocalRef(item);
    }

    buf[0] = EVENT_TYPE_LIST;
    buf[1] = copied;
    buf[pos++] = '\n';
    return android_bWriteLog(tag, buf, pos);
=======
 *  static native int writeEvent(int tag, String value)
 */
static jint android_util_EventLog_writeEvent_String(JNIEnv* env, jobject clazz,
                                                    jint tag, jstring value) {
    if (value == NULL) {
        jclass clazz = env->FindClass("java/lang/IllegalArgumentException");
        env->ThrowNew(clazz, "logEvent needs a value.");
        return -1;
    }

    ByteBuf byteBuf(INITAL_BUFFER_CAPACITY);
    byteBuf.putStringEvent(env, value);
    byteBuf.putByte((uint8_t)END_DELIMITER);
    int numBytesPut = byteBuf.len;
    int bytesWritten = android_bWriteLog(tag, byteBuf.buf, numBytesPut);
    return bytesWritten;
>>>>>>> 54b6cfa... Initial Contribution
}

/*
 * In class android.util.EventLog:
 *  static native void readEvents(int[] tags, Collection<Event> output)
<<<<<<< HEAD
 *
 *  Reads events from the event log, typically /dev/log/events
=======
>>>>>>> 54b6cfa... Initial Contribution
 */
static void android_util_EventLog_readEvents(JNIEnv* env, jobject clazz,
                                             jintArray tags,
                                             jobject out) {
    if (tags == NULL || out == NULL) {
<<<<<<< HEAD
        jniThrowNullPointerException(env, NULL);
=======
        jniThrowException(env, "java/lang/NullPointerException", NULL);
>>>>>>> 54b6cfa... Initial Contribution
        return;
    }

    int fd = open("/dev/" LOGGER_LOG_EVENTS, O_RDONLY | O_NONBLOCK);
    if (fd < 0) {
        jniThrowIOException(env, errno);
        return;
    }

    jsize tagLength = env->GetArrayLength(tags);
    jint *tagValues = env->GetIntArrayElements(tags, NULL);

    uint8_t buf[LOGGER_ENTRY_MAX_LEN];
<<<<<<< HEAD
    struct timeval timeout = {0, 0};
    fd_set readset;
    FD_ZERO(&readset);

    for (;;) {
        // Use a short select() to try to avoid problems hanging on read().
        // This means we block for 5ms at the end of the log -- oh well.
        timeout.tv_usec = 5000;
        FD_SET(fd, &readset);
        int r = select(fd + 1, &readset, NULL, NULL, &timeout);
        if (r == 0) {
            break;  // no more events
        } else if (r < 0 && errno == EINTR) {
            continue;  // interrupted by signal, try again
        } else if (r < 0) {
            jniThrowIOException(env, errno);  // Will throw on return
            break;
        }

        int len = read(fd, buf, sizeof(buf));
        if (len == 0 || (len < 0 && errno == EAGAIN)) {
            break;  // no more events
        } else if (len < 0 && errno == EINTR) {
            continue;  // interrupted by signal, try again
        } else if (len < 0) {
            jniThrowIOException(env, errno);  // Will throw on return
=======
    for (;;) {
        int len = read(fd, buf, sizeof(buf));
        if (len == 0 || (len < 0 && errno == EAGAIN)) {
            break;
        } else if (len < 0) {
            // This calls env->ThrowNew(), which doesn't throw an exception
            // now, but sets a flag to trigger an exception after we return.
            jniThrowIOException(env, errno);
>>>>>>> 54b6cfa... Initial Contribution
            break;
        } else if ((size_t) len < sizeof(logger_entry) + sizeof(int32_t)) {
            jniThrowException(env, "java/io/IOException", "Event too short");
            break;
        }

        logger_entry* entry = (logger_entry*) buf;
        int32_t tag = * (int32_t*) (buf + sizeof(*entry));

        int found = 0;
        for (int i = 0; !found && i < tagLength; ++i) {
            found = (tag == tagValues[i]);
        }

        if (found) {
            jsize len = sizeof(*entry) + entry->len;
            jbyteArray array = env->NewByteArray(len);
            if (array == NULL) break;

            jbyte *bytes = env->GetByteArrayElements(array, NULL);
            memcpy(bytes, buf, len);
            env->ReleaseByteArrayElements(array, bytes, 0);

            jobject event = env->NewObject(gEventClass, gEventInitID, array);
            if (event == NULL) break;

            env->CallBooleanMethod(out, gCollectionAddID, event);
            env->DeleteLocalRef(event);
            env->DeleteLocalRef(array);
        }
    }

    close(fd);
    env->ReleaseIntArrayElements(tags, tagValues, 0);
}

<<<<<<< HEAD
=======

>>>>>>> 54b6cfa... Initial Contribution
/*
 * JNI registration.
 */
static JNINativeMethod gRegisterMethods[] = {
    /* name, signature, funcPtr */
    { "writeEvent", "(II)I", (void*) android_util_EventLog_writeEvent_Integer },
    { "writeEvent", "(IJ)I", (void*) android_util_EventLog_writeEvent_Long },
    { "writeEvent",
      "(ILjava/lang/String;)I",
      (void*) android_util_EventLog_writeEvent_String
    },
    { "writeEvent",
<<<<<<< HEAD
      "(I[Ljava/lang/Object;)I",
      (void*) android_util_EventLog_writeEvent_Array
=======
      "(ILandroid/util/EventLog$List;)I",
      (void*) android_util_EventLog_writeEvent_List
>>>>>>> 54b6cfa... Initial Contribution
    },
    { "readEvents",
      "([ILjava/util/Collection;)V",
      (void*) android_util_EventLog_readEvents
<<<<<<< HEAD
    },
=======
    }
>>>>>>> 54b6cfa... Initial Contribution
};

static struct { const char *name; jclass *clazz; } gClasses[] = {
    { "android/util/EventLog$Event", &gEventClass },
<<<<<<< HEAD
=======
    { "android/util/EventLog$List", &gListClass },
>>>>>>> 54b6cfa... Initial Contribution
    { "java/lang/Integer", &gIntegerClass },
    { "java/lang/Long", &gLongClass },
    { "java/lang/String", &gStringClass },
    { "java/util/Collection", &gCollectionClass },
};

static struct { jclass *c; const char *name, *ft; jfieldID *id; } gFields[] = {
    { &gIntegerClass, "value", "I", &gIntegerValueID },
<<<<<<< HEAD
=======
    { &gListClass, "mItems", "[Ljava/lang/Object;", &gListItemsID },
>>>>>>> 54b6cfa... Initial Contribution
    { &gLongClass, "value", "J", &gLongValueID },
};

static struct { jclass *c; const char *name, *mt; jmethodID *id; } gMethods[] = {
    { &gEventClass, "<init>", "([B)V", &gEventInitID },
    { &gCollectionClass, "add", "(Ljava/lang/Object;)Z", &gCollectionAddID },
};

int register_android_util_EventLog(JNIEnv* env) {
    for (int i = 0; i < NELEM(gClasses); ++i) {
        jclass clazz = env->FindClass(gClasses[i].name);
        if (clazz == NULL) {
<<<<<<< HEAD
            ALOGE("Can't find class: %s\n", gClasses[i].name);
=======
            LOGE("Can't find class: %s\n", gClasses[i].name);
>>>>>>> 54b6cfa... Initial Contribution
            return -1;
        }
        *gClasses[i].clazz = (jclass) env->NewGlobalRef(clazz);
    }

    for (int i = 0; i < NELEM(gFields); ++i) {
        *gFields[i].id = env->GetFieldID(
                *gFields[i].c, gFields[i].name, gFields[i].ft);
        if (*gFields[i].id == NULL) {
<<<<<<< HEAD
            ALOGE("Can't find field: %s\n", gFields[i].name);
=======
            LOGE("Can't find field: %s\n", gFields[i].name);
>>>>>>> 54b6cfa... Initial Contribution
            return -1;
        }
    }

    for (int i = 0; i < NELEM(gMethods); ++i) {
        *gMethods[i].id = env->GetMethodID(
                *gMethods[i].c, gMethods[i].name, gMethods[i].mt);
        if (*gMethods[i].id == NULL) {
<<<<<<< HEAD
            ALOGE("Can't find method: %s\n", gMethods[i].name);
=======
            LOGE("Can't find method: %s\n", gMethods[i].name);
>>>>>>> 54b6cfa... Initial Contribution
            return -1;
        }
    }

    return AndroidRuntime::registerNativeMethods(
            env,
            "android/util/EventLog",
            gRegisterMethods, NELEM(gRegisterMethods));
}

}; // namespace android
<<<<<<< HEAD
=======

>>>>>>> 54b6cfa... Initial Contribution
