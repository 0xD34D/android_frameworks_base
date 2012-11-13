#include "CreateJavaOutputStreamAdaptor.h"

#define RETURN_NULL_IF_NULL(value) \
    do { if (!(value)) { SkASSERT(0); return NULL; } } while (false)

<<<<<<< HEAD
static jmethodID    gInputStream_resetMethodID;
static jmethodID    gInputStream_markMethodID;
=======
static jclass       gInputStream_Clazz;
static jmethodID    gInputStream_resetMethodID;
>>>>>>> 54b6cfa... Initial Contribution
static jmethodID    gInputStream_availableMethodID;
static jmethodID    gInputStream_readMethodID;
static jmethodID    gInputStream_skipMethodID;

class JavaInputStreamAdaptor : public SkStream {
public:
    JavaInputStreamAdaptor(JNIEnv* env, jobject js, jbyteArray ar)
        : fEnv(env), fJavaInputStream(js), fJavaByteArray(ar) {
        SkASSERT(ar);
        fCapacity   = env->GetArrayLength(ar);
        SkASSERT(fCapacity > 0);
        fBytesRead  = 0;
    }
<<<<<<< HEAD

	virtual bool rewind() {
        JNIEnv* env = fEnv;

=======
    
	virtual bool rewind() {
        JNIEnv* env = fEnv;
        
>>>>>>> 54b6cfa... Initial Contribution
        fBytesRead = 0;

        env->CallVoidMethod(fJavaInputStream, gInputStream_resetMethodID);
        if (env->ExceptionCheck()) {
            env->ExceptionDescribe();
            env->ExceptionClear();
<<<<<<< HEAD
            SkDebugf("------- reset threw an exception\n");
=======
            printf("------- reset threw an exception\n");
>>>>>>> 54b6cfa... Initial Contribution
            return false;
        }
        return true;
    }
<<<<<<< HEAD

    size_t doRead(void* buffer, size_t size) {
        JNIEnv* env = fEnv;
        size_t bytesRead = 0;
=======
    
	virtual size_t read(void* buffer, size_t size) {
        JNIEnv* env = fEnv;
        
        if (buffer == NULL && size == 0) {
            jint avail = env->CallIntMethod(fJavaInputStream,
                                            gInputStream_availableMethodID);
            if (env->ExceptionCheck()) {
                env->ExceptionDescribe();
                env->ExceptionClear();
                printf("------- available threw an exception\n");
                avail = 0;
            }
            return avail;
        }

        size_t bytesRead = 0;

        if (buffer == NULL) { // skip
            jlong skipped = env->CallLongMethod(fJavaInputStream,
                                        gInputStream_skipMethodID, (jlong)size);
            if (env->ExceptionCheck()) {
                env->ExceptionDescribe();
                env->ExceptionClear();
                printf("------- available threw an exception\n");
                return 0;
            }
            if (skipped < 0) {
                return 0;
            }
            return (size_t)skipped;
        }

>>>>>>> 54b6cfa... Initial Contribution
        // read the bytes
        do {
            size_t requested = size;
            if (requested > fCapacity)
                requested = fCapacity;

            jint n = env->CallIntMethod(fJavaInputStream,
<<<<<<< HEAD
                                        gInputStream_readMethodID, fJavaByteArray, 0, requested);
            if (env->ExceptionCheck()) {
                env->ExceptionDescribe();
                env->ExceptionClear();
                SkDebugf("---- read threw an exception\n");
                return 0;
            }

            if (n < 0) { // n == 0 should not be possible, see InputStream read() specifications.
                break;  // eof
            }

            env->GetByteArrayRegion(fJavaByteArray, 0, n,
                                    reinterpret_cast<jbyte*>(buffer));
            if (env->ExceptionCheck()) {
                env->ExceptionDescribe();
                env->ExceptionClear();
                SkDebugf("---- read:GetByteArrayRegion threw an exception\n");
                return 0;
            }

=======
                    gInputStream_readMethodID, fJavaByteArray, 0, requested);
            if (env->ExceptionCheck()) {
                env->ExceptionDescribe();
                env->ExceptionClear();
                printf("---- read threw an exception\n");
                return 0;
            }

            if (n <= 0) {
                break;  // eof
            }

            jbyte* array = env->GetByteArrayElements(fJavaByteArray, NULL);
            memcpy(buffer, array, n);
            env->ReleaseByteArrayElements(fJavaByteArray, array, 0);
            
>>>>>>> 54b6cfa... Initial Contribution
            buffer = (void*)((char*)buffer + n);
            bytesRead += n;
            size -= n;
            fBytesRead += n;
        } while (size != 0);
<<<<<<< HEAD

        return bytesRead;
    }

    size_t doSkip(size_t size) {
        JNIEnv* env = fEnv;

        jlong skipped = env->CallLongMethod(fJavaInputStream,
                                            gInputStream_skipMethodID, (jlong)size);
        if (env->ExceptionCheck()) {
            env->ExceptionDescribe();
            env->ExceptionClear();
            SkDebugf("------- skip threw an exception\n");
            return 0;
        }
        if (skipped < 0) {
            skipped = 0;
        }

        return (size_t)skipped;
    }

    size_t doSize() {
        JNIEnv* env = fEnv;
        jint avail = env->CallIntMethod(fJavaInputStream,
                                        gInputStream_availableMethodID);
        if (env->ExceptionCheck()) {
            env->ExceptionDescribe();
            env->ExceptionClear();
            SkDebugf("------- available threw an exception\n");
            avail = 0;
        }
        return avail;
    }

	virtual size_t read(void* buffer, size_t size) {
        JNIEnv* env = fEnv;
        if (NULL == buffer) {
            if (0 == size) {
                return this->doSize();
            } else {
                /*  InputStream.skip(n) can return <=0 but still not be at EOF
                    If we see that value, we need to call read(), which will
                    block if waiting for more data, or return -1 at EOF
                 */
                size_t amountSkipped = 0;
                do {
                    size_t amount = this->doSkip(size - amountSkipped);
                    if (0 == amount) {
                        char tmp;
                        amount = this->doRead(&tmp, 1);
                        if (0 == amount) {
                            // if read returned 0, we're at EOF
                            break;
                        }
                    }
                    amountSkipped += amount;
                } while (amountSkipped < size);
                return amountSkipped;
            }
        }
        return this->doRead(buffer, size);
    }

=======
        
        return bytesRead;
    }
    
>>>>>>> 54b6cfa... Initial Contribution
private:
    JNIEnv*     fEnv;
    jobject     fJavaInputStream;   // the caller owns this object
    jbyteArray  fJavaByteArray;     // the caller owns this object
    size_t      fCapacity;
    size_t      fBytesRead;
};

SkStream* CreateJavaInputStreamAdaptor(JNIEnv* env, jobject stream,
<<<<<<< HEAD
                                       jbyteArray storage, int markSize) {
    static bool gInited;

    if (!gInited) {
        jclass inputStream_Clazz = env->FindClass("java/io/InputStream");
        RETURN_NULL_IF_NULL(inputStream_Clazz);

        gInputStream_resetMethodID      = env->GetMethodID(inputStream_Clazz,
                                                           "reset", "()V");
        gInputStream_markMethodID       = env->GetMethodID(inputStream_Clazz,
                                                           "mark", "(I)V");
        gInputStream_availableMethodID  = env->GetMethodID(inputStream_Clazz,
                                                           "available", "()I");
        gInputStream_readMethodID       = env->GetMethodID(inputStream_Clazz,
                                                           "read", "([BII)I");
        gInputStream_skipMethodID       = env->GetMethodID(inputStream_Clazz,
                                                           "skip", "(J)J");

        RETURN_NULL_IF_NULL(gInputStream_resetMethodID);
        RETURN_NULL_IF_NULL(gInputStream_markMethodID);
=======
                                       jbyteArray storage) {
    static bool gInited;

    if (!gInited) {
        gInputStream_Clazz = env->FindClass("java/io/InputStream");
        RETURN_NULL_IF_NULL(gInputStream_Clazz);
        gInputStream_Clazz = (jclass)env->NewGlobalRef(gInputStream_Clazz);

        gInputStream_resetMethodID      = env->GetMethodID(gInputStream_Clazz,
                                                           "reset", "()V");
        gInputStream_availableMethodID  = env->GetMethodID(gInputStream_Clazz,
                                                           "available", "()I");
        gInputStream_readMethodID       = env->GetMethodID(gInputStream_Clazz,
                                                           "read", "([BII)I");
        gInputStream_skipMethodID       = env->GetMethodID(gInputStream_Clazz,
                                                           "skip", "(J)J");

        RETURN_NULL_IF_NULL(gInputStream_resetMethodID);
>>>>>>> 54b6cfa... Initial Contribution
        RETURN_NULL_IF_NULL(gInputStream_availableMethodID);
        RETURN_NULL_IF_NULL(gInputStream_availableMethodID);
        RETURN_NULL_IF_NULL(gInputStream_skipMethodID);

        gInited = true;
    }

<<<<<<< HEAD
    if (markSize) {
        env->CallVoidMethod(stream, gInputStream_markMethodID, markSize);
    }

=======
>>>>>>> 54b6cfa... Initial Contribution
    return new JavaInputStreamAdaptor(env, stream, storage);
}

///////////////////////////////////////////////////////////////////////////////

<<<<<<< HEAD
=======
static jclass       gOutputStream_Clazz;
>>>>>>> 54b6cfa... Initial Contribution
static jmethodID    gOutputStream_writeMethodID;
static jmethodID    gOutputStream_flushMethodID;

class SkJavaOutputStream : public SkWStream {
public:
    SkJavaOutputStream(JNIEnv* env, jobject stream, jbyteArray storage)
        : fEnv(env), fJavaOutputStream(stream), fJavaByteArray(storage) {
        fCapacity = env->GetArrayLength(storage);
    }
<<<<<<< HEAD

	virtual bool write(const void* buffer, size_t size) {
        JNIEnv* env = fEnv;
        jbyteArray storage = fJavaByteArray;

=======
    
	virtual bool write(const void* buffer, size_t size) {
        JNIEnv* env = fEnv;
        jbyteArray storage = fJavaByteArray;
        
>>>>>>> 54b6cfa... Initial Contribution
        while (size > 0) {
            size_t requested = size;
            if (requested > fCapacity) {
                requested = fCapacity;
            }

<<<<<<< HEAD
            env->SetByteArrayRegion(storage, 0, requested,
                                    reinterpret_cast<const jbyte*>(buffer));
            if (env->ExceptionCheck()) {
                env->ExceptionDescribe();
                env->ExceptionClear();
                SkDebugf("--- write:SetByteArrayElements threw an exception\n");
                return false;
            }
=======
            jbyte* array = env->GetByteArrayElements(storage, NULL);
            memcpy(array, buffer, requested);
            env->ReleaseByteArrayElements(storage, array, 0);
>>>>>>> 54b6cfa... Initial Contribution

            fEnv->CallVoidMethod(fJavaOutputStream, gOutputStream_writeMethodID,
                                 storage, 0, requested);
            if (env->ExceptionCheck()) {
                env->ExceptionDescribe();
                env->ExceptionClear();
<<<<<<< HEAD
                SkDebugf("------- write threw an exception\n");
                return false;
            }

=======
                printf("------- write threw an exception\n");
                return false;
            }
            
>>>>>>> 54b6cfa... Initial Contribution
            buffer = (void*)((char*)buffer + requested);
            size -= requested;
        }
        return true;
    }
<<<<<<< HEAD

    virtual void flush() {
        fEnv->CallVoidMethod(fJavaOutputStream, gOutputStream_flushMethodID);
    }

=======
    
    virtual void flush() {
        fEnv->CallVoidMethod(fJavaOutputStream, gOutputStream_flushMethodID);
    }
    
>>>>>>> 54b6cfa... Initial Contribution
private:
    JNIEnv*     fEnv;
    jobject     fJavaOutputStream;  // the caller owns this object
    jbyteArray  fJavaByteArray;     // the caller owns this object
    size_t      fCapacity;
};

SkWStream* CreateJavaOutputStreamAdaptor(JNIEnv* env, jobject stream,
                                         jbyteArray storage) {
    static bool gInited;

    if (!gInited) {
<<<<<<< HEAD
        jclass outputStream_Clazz = env->FindClass("java/io/OutputStream");
        RETURN_NULL_IF_NULL(outputStream_Clazz);

        gOutputStream_writeMethodID = env->GetMethodID(outputStream_Clazz,
                                                       "write", "([BII)V");
        RETURN_NULL_IF_NULL(gOutputStream_writeMethodID);
        gOutputStream_flushMethodID = env->GetMethodID(outputStream_Clazz,
=======
        gOutputStream_Clazz = env->FindClass("java/io/OutputStream");
        RETURN_NULL_IF_NULL(gOutputStream_Clazz);
        gOutputStream_Clazz = (jclass)env->NewGlobalRef(gOutputStream_Clazz);

        gOutputStream_writeMethodID = env->GetMethodID(gOutputStream_Clazz,
                                                       "write", "([BII)V");
        RETURN_NULL_IF_NULL(gOutputStream_writeMethodID);
        gOutputStream_flushMethodID = env->GetMethodID(gOutputStream_Clazz,
>>>>>>> 54b6cfa... Initial Contribution
                                                       "flush", "()V");
        RETURN_NULL_IF_NULL(gOutputStream_flushMethodID);

        gInited = true;
    }

    return new SkJavaOutputStream(env, stream, storage);
}
<<<<<<< HEAD
=======

>>>>>>> 54b6cfa... Initial Contribution
