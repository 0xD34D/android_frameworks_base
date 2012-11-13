/*
 * Copyright (C) 2006 The Android Open Source Project
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

#define LOG_TAG "JavaBinder"
//#define LOG_NDEBUG 0

<<<<<<< HEAD
#include "android_os_Parcel.h"
#include "android_util_Binder.h"

#include "JNIHelp.h"

#include <fcntl.h>
#include <stdio.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>

#include <utils/Atomic.h>
#include <binder/IInterface.h>
#include <binder/IPCThreadState.h>
#include <utils/Log.h>
#include <utils/SystemClock.h>
#include <utils/List.h>
#include <utils/KeyedVector.h>
#include <cutils/logger.h>
#include <binder/Parcel.h>
#include <binder/ProcessState.h>
#include <binder/IServiceManager.h>
#include <utils/threads.h>
#include <utils/String8.h>

#include <ScopedUtfChars.h>
#include <ScopedLocalRef.h>

#include <android_runtime/AndroidRuntime.h>

//#undef ALOGV
//#define ALOGV(...) fprintf(stderr, __VA_ARGS__)

#define DEBUG_DEATH 0
#if DEBUG_DEATH
#define LOGDEATH ALOGD
#else
#define LOGDEATH ALOGV
#endif
=======
#include "android_util_Binder.h"
#include "JNIHelp.h"

#include <fcntl.h>
#include <sys/stat.h>
#include <stdio.h>

#include <utils/Atomic.h>
#include <utils/IInterface.h>
#include <utils/IPCThreadState.h>
#include <utils/Log.h>
#include <utils/Parcel.h>
#include <utils/ProcessState.h>
#include <utils/IServiceManager.h>

#include <android_runtime/AndroidRuntime.h>

//#undef LOGV
//#define LOGV(...) fprintf(stderr, __VA_ARGS__)
>>>>>>> 54b6cfa... Initial Contribution

using namespace android;

// ----------------------------------------------------------------------------

static struct bindernative_offsets_t
{
    // Class state.
    jclass mClass;
    jmethodID mExecTransact;

    // Object state.
    jfieldID mObject;

} gBinderOffsets;

// ----------------------------------------------------------------------------

static struct binderinternal_offsets_t
{
    // Class state.
    jclass mClass;
    jmethodID mForceGc;

} gBinderInternalOffsets;

// ----------------------------------------------------------------------------

static struct debug_offsets_t
{
    // Class state.
    jclass mClass;

} gDebugOffsets;

// ----------------------------------------------------------------------------

static struct weakreference_offsets_t
{
    // Class state.
    jclass mClass;
    jmethodID mGet;

} gWeakReferenceOffsets;

static struct error_offsets_t
{
    jclass mClass;
} gErrorOffsets;

// ----------------------------------------------------------------------------

static struct binderproxy_offsets_t
{
    // Class state.
    jclass mClass;
    jmethodID mConstructor;
    jmethodID mSendDeathNotice;

    // Object state.
    jfieldID mObject;
    jfieldID mSelf;
<<<<<<< HEAD
    jfieldID mOrgue;

} gBinderProxyOffsets;

static struct class_offsets_t
{
    jmethodID mGetName;
} gClassOffsets;

// ----------------------------------------------------------------------------

=======

} gBinderProxyOffsets;

// ----------------------------------------------------------------------------

static struct parcel_offsets_t
{
    jfieldID mObject;
    jfieldID mOwnObject;
} gParcelOffsets;

>>>>>>> 54b6cfa... Initial Contribution
static struct log_offsets_t
{
    // Class state.
    jclass mClass;
    jmethodID mLogE;
} gLogOffsets;

<<<<<<< HEAD
static struct parcel_file_descriptor_offsets_t
{
    jclass mClass;
    jmethodID mConstructor;
} gParcelFileDescriptorOffsets;

static struct strict_mode_callback_offsets_t
{
    jclass mClass;
    jmethodID mCallback;
} gStrictModeCallbackOffsets;
=======
static struct file_descriptor_offsets_t
{
    jclass mClass;
    jmethodID mConstructor;
    jfieldID mDescriptor;
} gFileDescriptorOffsets;

static struct parcel_file_descriptor_offsets_t
{
    jclass mClass;
    jmethodID mConstructor;
} gParcelFileDescriptorOffsets;
>>>>>>> 54b6cfa... Initial Contribution

// ****************************************************************************
// ****************************************************************************
// ****************************************************************************

static volatile int32_t gNumRefsCreated = 0;
static volatile int32_t gNumProxyRefs = 0;
static volatile int32_t gNumLocalRefs = 0;
static volatile int32_t gNumDeathRefs = 0;

static void incRefsCreated(JNIEnv* env)
{
    int old = android_atomic_inc(&gNumRefsCreated);
    if (old == 200) {
        android_atomic_and(0, &gNumRefsCreated);
        env->CallStaticVoidMethod(gBinderInternalOffsets.mClass,
                gBinderInternalOffsets.mForceGc);
    } else {
<<<<<<< HEAD
        ALOGV("Now have %d binder ops", old);
=======
        LOGV("Now have %d binder ops", old);
>>>>>>> 54b6cfa... Initial Contribution
    }
}

static JavaVM* jnienv_to_javavm(JNIEnv* env)
{
    JavaVM* vm;
    return env->GetJavaVM(&vm) >= 0 ? vm : NULL;
}

static JNIEnv* javavm_to_jnienv(JavaVM* vm)
{
    JNIEnv* env;
    return vm->GetEnv((void **)&env, JNI_VERSION_1_4) >= 0 ? env : NULL;
}

static void report_exception(JNIEnv* env, jthrowable excep, const char* msg)
{
    env->ExceptionClear();

    jstring tagstr = env->NewStringUTF(LOG_TAG);
    jstring msgstr = env->NewStringUTF(msg);

    if ((tagstr == NULL) || (msgstr == NULL)) {
        env->ExceptionClear();      /* assume exception (OOM?) was thrown */
<<<<<<< HEAD
        ALOGE("Unable to call Log.e()\n");
        ALOGE("%s", msg);
=======
        LOGE("Unable to call Log.e()\n");
        LOGE("%s", msg);
>>>>>>> 54b6cfa... Initial Contribution
        goto bail;
    }

    env->CallStaticIntMethod(
        gLogOffsets.mClass, gLogOffsets.mLogE, tagstr, msgstr, excep);
    if (env->ExceptionCheck()) {
        /* attempting to log the failure has failed */
<<<<<<< HEAD
        ALOGW("Failed trying to log exception, msg='%s'\n", msg);
=======
        LOGW("Failed trying to log exception, msg='%s'\n", msg);
>>>>>>> 54b6cfa... Initial Contribution
        env->ExceptionClear();
    }

    if (env->IsInstanceOf(excep, gErrorOffsets.mClass)) {
        /*
         * It's an Error: Reraise the exception, detach this thread, and
         * wait for the fireworks. Die even more blatantly after a minute
         * if the gentler attempt doesn't do the trick.
         *
         * The GetJavaVM function isn't on the "approved" list of JNI calls
         * that can be made while an exception is pending, so we want to
         * get the VM ptr, throw the exception, and then detach the thread.
         */
        JavaVM* vm = jnienv_to_javavm(env);
        env->Throw(excep);
        vm->DetachCurrentThread();
        sleep(60);
<<<<<<< HEAD
        ALOGE("Forcefully exiting");
=======
        LOGE("Forcefully exiting");
>>>>>>> 54b6cfa... Initial Contribution
        exit(1);
        *((int *) 1) = 1;
    }

bail:
    /* discard local refs created for us by VM */
    env->DeleteLocalRef(tagstr);
    env->DeleteLocalRef(msgstr);
}

class JavaBBinderHolder;

class JavaBBinder : public BBinder
{
public:
    JavaBBinder(JNIEnv* env, jobject object)
        : mVM(jnienv_to_javavm(env)), mObject(env->NewGlobalRef(object))
    {
<<<<<<< HEAD
        ALOGV("Creating JavaBBinder %p\n", this);
=======
        LOGV("Creating JavaBBinder %p\n", this);
>>>>>>> 54b6cfa... Initial Contribution
        android_atomic_inc(&gNumLocalRefs);
        incRefsCreated(env);
    }

    bool    checkSubclass(const void* subclassID) const
    {
        return subclassID == &gBinderOffsets;
    }

    jobject object() const
    {
        return mObject;
    }

protected:
    virtual ~JavaBBinder()
    {
<<<<<<< HEAD
        ALOGV("Destroying JavaBBinder %p\n", this);
=======
        LOGV("Destroying JavaBBinder %p\n", this);
>>>>>>> 54b6cfa... Initial Contribution
        android_atomic_dec(&gNumLocalRefs);
        JNIEnv* env = javavm_to_jnienv(mVM);
        env->DeleteGlobalRef(mObject);
    }

    virtual status_t onTransact(
        uint32_t code, const Parcel& data, Parcel* reply, uint32_t flags = 0)
    {
        JNIEnv* env = javavm_to_jnienv(mVM);

<<<<<<< HEAD
        ALOGV("onTransact() on %p calling object %p in env %p vm %p\n", this, mObject, env, mVM);

        IPCThreadState* thread_state = IPCThreadState::self();
        const int strict_policy_before = thread_state->getStrictModePolicy();
        thread_state->setLastTransactionBinderFlags(flags);
=======
        LOGV("onTransact() on %p calling object %p in env %p vm %p\n", this, mObject, env, mVM);
>>>>>>> 54b6cfa... Initial Contribution

        //printf("Transact from %p to Java code sending: ", this);
        //data.print();
        //printf("\n");
        jboolean res = env->CallBooleanMethod(mObject, gBinderOffsets.mExecTransact,
            code, (int32_t)&data, (int32_t)reply, flags);
        jthrowable excep = env->ExceptionOccurred();
<<<<<<< HEAD

=======
>>>>>>> 54b6cfa... Initial Contribution
        if (excep) {
            report_exception(env, excep,
                "*** Uncaught remote exception!  "
                "(Exceptions are not yet supported across processes.)");
            res = JNI_FALSE;

            /* clean up JNI local ref -- we don't return to Java code */
            env->DeleteLocalRef(excep);
        }

<<<<<<< HEAD
        // Restore the Java binder thread's state if it changed while
        // processing a call (as it would if the Parcel's header had a
        // new policy mask and Parcel.enforceInterface() changed
        // it...)
        const int strict_policy_after = thread_state->getStrictModePolicy();
        if (strict_policy_after != strict_policy_before) {
            // Our thread-local...
            thread_state->setStrictModePolicy(strict_policy_before);
            // And the Java-level thread-local...
            set_dalvik_blockguard_policy(env, strict_policy_before);
        }

        jthrowable excep2 = env->ExceptionOccurred();
        if (excep2) {
            report_exception(env, excep2,
                "*** Uncaught exception in onBinderStrictModePolicyChange");
            /* clean up JNI local ref -- we don't return to Java code */
            env->DeleteLocalRef(excep2);
        }

        // Need to always call through the native implementation of
        // SYSPROPS_TRANSACTION.
        if (code == SYSPROPS_TRANSACTION) {
            BBinder::onTransact(code, data, reply, flags);
        }

=======
>>>>>>> 54b6cfa... Initial Contribution
        //aout << "onTransact to Java code; result=" << res << endl
        //    << "Transact from " << this << " to Java code returning "
        //    << reply << ": " << *reply << endl;
        return res != JNI_FALSE ? NO_ERROR : UNKNOWN_TRANSACTION;
    }

    virtual status_t dump(int fd, const Vector<String16>& args)
    {
        return 0;
    }

private:
    JavaVM* const   mVM;
    jobject const   mObject;
};

// ----------------------------------------------------------------------------

class JavaBBinderHolder : public RefBase
{
public:
<<<<<<< HEAD
    sp<JavaBBinder> get(JNIEnv* env, jobject obj)
=======
    JavaBBinderHolder(JNIEnv* env, jobject object)
        : mObject(object)
    {
        LOGV("Creating JavaBBinderHolder for Object %p\n", object);
    }
    ~JavaBBinderHolder()
    {
        LOGV("Destroying JavaBBinderHolder for Object %p\n", mObject);
    }

    sp<JavaBBinder> get(JNIEnv* env)
>>>>>>> 54b6cfa... Initial Contribution
    {
        AutoMutex _l(mLock);
        sp<JavaBBinder> b = mBinder.promote();
        if (b == NULL) {
<<<<<<< HEAD
            b = new JavaBBinder(env, obj);
            mBinder = b;
            ALOGV("Creating JavaBinder %p (refs %p) for Object %p, weakCount=%d\n",
                 b.get(), b->getWeakRefs(), obj, b->getWeakRefs()->getWeakCount());
=======
            b = new JavaBBinder(env, mObject);
            mBinder = b;
            LOGV("Creating JavaBinder %p (refs %p) for Object %p, weakCount=%d\n",
                 b.get(), b->getWeakRefs(), mObject, b->getWeakRefs()->getWeakCount());
>>>>>>> 54b6cfa... Initial Contribution
        }

        return b;
    }

    sp<JavaBBinder> getExisting()
    {
        AutoMutex _l(mLock);
        return mBinder.promote();
    }

private:
    Mutex           mLock;
<<<<<<< HEAD
=======
    jobject         mObject;
>>>>>>> 54b6cfa... Initial Contribution
    wp<JavaBBinder> mBinder;
};

// ----------------------------------------------------------------------------

<<<<<<< HEAD
// Per-IBinder death recipient bookkeeping.  This is how we reconcile local jobject
// death recipient references passed in through JNI with the permanent corresponding
// JavaDeathRecipient objects.

class JavaDeathRecipient;

class DeathRecipientList : public RefBase {
    List< sp<JavaDeathRecipient> > mList;
    Mutex mLock;

public:
    DeathRecipientList();
    ~DeathRecipientList();

    void add(const sp<JavaDeathRecipient>& recipient);
    void remove(const sp<JavaDeathRecipient>& recipient);
    sp<JavaDeathRecipient> find(jobject recipient);
};

// ----------------------------------------------------------------------------

class JavaDeathRecipient : public IBinder::DeathRecipient
{
public:
    JavaDeathRecipient(JNIEnv* env, jobject object, const sp<DeathRecipientList>& list)
        : mVM(jnienv_to_javavm(env)), mObject(env->NewGlobalRef(object)),
          mObjectWeak(NULL), mList(list)
    {
        // These objects manage their own lifetimes so are responsible for final bookkeeping.
        // The list holds a strong reference to this object.
        LOGDEATH("Adding JDR %p to DRL %p", this, list.get());
        list->add(this);

=======
class JavaDeathRecipient : public IBinder::DeathRecipient
{
public:
    JavaDeathRecipient(JNIEnv* env, jobject object)
        : mVM(jnienv_to_javavm(env)), mObject(env->NewGlobalRef(object)),
          mHoldsRef(true)
    {
        incStrong(this);
>>>>>>> 54b6cfa... Initial Contribution
        android_atomic_inc(&gNumDeathRefs);
        incRefsCreated(env);
    }

    void binderDied(const wp<IBinder>& who)
    {
<<<<<<< HEAD
        LOGDEATH("Receiving binderDied() on JavaDeathRecipient %p\n", this);
        if (mObject != NULL) {
            JNIEnv* env = javavm_to_jnienv(mVM);

            env->CallStaticVoidMethod(gBinderProxyOffsets.mClass,
                    gBinderProxyOffsets.mSendDeathNotice, mObject);
            jthrowable excep = env->ExceptionOccurred();
            if (excep) {
                report_exception(env, excep,
                        "*** Uncaught exception returned from death notification!");
            }

            // Demote from strong ref to weak after binderDied() has been delivered,
            // to allow the DeathRecipient and BinderProxy to be GC'd if no longer needed.
            mObjectWeak = env->NewWeakGlobalRef(mObject);
            env->DeleteGlobalRef(mObject);
            mObject = NULL;
        }
=======
        JNIEnv* env = javavm_to_jnienv(mVM);

        LOGV("Receiving binderDied() on JavaDeathRecipient %p\n", this);

        env->CallStaticVoidMethod(gBinderProxyOffsets.mClass,
            gBinderProxyOffsets.mSendDeathNotice, mObject);
        jthrowable excep = env->ExceptionOccurred();
        if (excep) {
            report_exception(env, excep,
                "*** Uncaught exception returned from death notification!");
        }

        clearReference();
>>>>>>> 54b6cfa... Initial Contribution
    }

    void clearReference()
    {
<<<<<<< HEAD
        sp<DeathRecipientList> list = mList.promote();
        if (list != NULL) {
            LOGDEATH("Removing JDR %p from DRL %p", this, list.get());
            list->remove(this);
        } else {
            LOGDEATH("clearReference() on JDR %p but DRL wp purged", this);
        }
    }

    bool matches(jobject obj) {
        bool result;
        JNIEnv* env = javavm_to_jnienv(mVM);

        if (mObject != NULL) {
            result = env->IsSameObject(obj, mObject);
        } else {
            jobject me = env->NewLocalRef(mObjectWeak);
            result = env->IsSameObject(obj, me);
            env->DeleteLocalRef(me);
        }
        return result;
    }

    void warnIfStillLive() {
        if (mObject != NULL) {
            // Okay, something is wrong -- we have a hard reference to a live death
            // recipient on the VM side, but the list is being torn down.
            JNIEnv* env = javavm_to_jnienv(mVM);
            ScopedLocalRef<jclass> objClassRef(env, env->GetObjectClass(mObject));
            ScopedLocalRef<jstring> nameRef(env,
                    (jstring) env->CallObjectMethod(objClassRef.get(), gClassOffsets.mGetName));
            ScopedUtfChars nameUtf(env, nameRef.get());
            if (nameUtf.c_str() != NULL) {
                ALOGW("BinderProxy is being destroyed but the application did not call "
                        "unlinkToDeath to unlink all of its death recipients beforehand.  "
                        "Releasing leaked death recipient: %s", nameUtf.c_str());
            } else {
                ALOGW("BinderProxy being destroyed; unable to get DR object name");
                env->ExceptionClear();
            }
=======
        bool release = false;
        mLock.lock();
        if (mHoldsRef) {
            mHoldsRef = false;
            release = true;
        }
        mLock.unlock();
        if (release) {
            decStrong(this);
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

protected:
    virtual ~JavaDeathRecipient()
    {
<<<<<<< HEAD
        //ALOGI("Removing death ref: recipient=%p\n", mObject);
        android_atomic_dec(&gNumDeathRefs);
        JNIEnv* env = javavm_to_jnienv(mVM);
        if (mObject != NULL) {
            env->DeleteGlobalRef(mObject);
        } else {
            env->DeleteWeakGlobalRef(mObjectWeak);
        }
    }

private:
    JavaVM* const mVM;
    jobject mObject;
    jweak mObjectWeak; // will be a weak ref to the same VM-side DeathRecipient after binderDied()
    wp<DeathRecipientList> mList;
=======
        //LOGI("Removing death ref: recipient=%p\n", mObject);
        android_atomic_dec(&gNumDeathRefs);
        JNIEnv* env = javavm_to_jnienv(mVM);
        env->DeleteGlobalRef(mObject);
    }

private:
    JavaVM* const   mVM;
    jobject const   mObject;
    Mutex           mLock;
    bool            mHoldsRef;
>>>>>>> 54b6cfa... Initial Contribution
};

// ----------------------------------------------------------------------------

<<<<<<< HEAD
DeathRecipientList::DeathRecipientList() {
    LOGDEATH("New DRL @ %p", this);
}

DeathRecipientList::~DeathRecipientList() {
    LOGDEATH("Destroy DRL @ %p", this);
    AutoMutex _l(mLock);

    // Should never happen -- the JavaDeathRecipient objects that have added themselves
    // to the list are holding references on the list object.  Only when they are torn
    // down can the list header be destroyed.
    if (mList.size() > 0) {
        List< sp<JavaDeathRecipient> >::iterator iter;
        for (iter = mList.begin(); iter != mList.end(); iter++) {
            (*iter)->warnIfStillLive();
        }
    }
}

void DeathRecipientList::add(const sp<JavaDeathRecipient>& recipient) {
    AutoMutex _l(mLock);

    LOGDEATH("DRL @ %p : add JDR %p", this, recipient.get());
    mList.push_back(recipient);
}

void DeathRecipientList::remove(const sp<JavaDeathRecipient>& recipient) {
    AutoMutex _l(mLock);

    List< sp<JavaDeathRecipient> >::iterator iter;
    for (iter = mList.begin(); iter != mList.end(); iter++) {
        if (*iter == recipient) {
            LOGDEATH("DRL @ %p : remove JDR %p", this, recipient.get());
            mList.erase(iter);
            return;
        }
    }
}

sp<JavaDeathRecipient> DeathRecipientList::find(jobject recipient) {
    AutoMutex _l(mLock);

    List< sp<JavaDeathRecipient> >::iterator iter;
    for (iter = mList.begin(); iter != mList.end(); iter++) {
        if ((*iter)->matches(recipient)) {
            return *iter;
        }
    }
    return NULL;
}

// ----------------------------------------------------------------------------

=======
>>>>>>> 54b6cfa... Initial Contribution
namespace android {

static void proxy_cleanup(const void* id, void* obj, void* cleanupCookie)
{
    android_atomic_dec(&gNumProxyRefs);
    JNIEnv* env = javavm_to_jnienv((JavaVM*)cleanupCookie);
    env->DeleteGlobalRef((jobject)obj);
}

static Mutex mProxyLock;

jobject javaObjectForIBinder(JNIEnv* env, const sp<IBinder>& val)
{
    if (val == NULL) return NULL;

    if (val->checkSubclass(&gBinderOffsets)) {
        // One of our own!
        jobject object = static_cast<JavaBBinder*>(val.get())->object();
<<<<<<< HEAD
        LOGDEATH("objectForBinder %p: it's our own %p!\n", val.get(), object);
=======
        //printf("objectForBinder %p: it's our own %p!\n", val.get(), object);
>>>>>>> 54b6cfa... Initial Contribution
        return object;
    }

    // For the rest of the function we will hold this lock, to serialize
    // looking/creation of Java proxies for native Binder proxies.
    AutoMutex _l(mProxyLock);

    // Someone else's...  do we know about it?
    jobject object = (jobject)val->findObject(&gBinderProxyOffsets);
    if (object != NULL) {
        jobject res = env->CallObjectMethod(object, gWeakReferenceOffsets.mGet);
        if (res != NULL) {
<<<<<<< HEAD
            ALOGV("objectForBinder %p: found existing %p!\n", val.get(), res);
            return res;
        }
        LOGDEATH("Proxy object %p of IBinder %p no longer in working set!!!", object, val.get());
=======
            LOGV("objectForBinder %p: found existing %p!\n", val.get(), res);
            return res;
        }
        LOGV("Proxy object %p of IBinder %p no longer in working set!!!", object, val.get());
>>>>>>> 54b6cfa... Initial Contribution
        android_atomic_dec(&gNumProxyRefs);
        val->detachObject(&gBinderProxyOffsets);
        env->DeleteGlobalRef(object);
    }

    object = env->NewObject(gBinderProxyOffsets.mClass, gBinderProxyOffsets.mConstructor);
    if (object != NULL) {
<<<<<<< HEAD
        LOGDEATH("objectForBinder %p: created new proxy %p !\n", val.get(), object);
=======
        LOGV("objectForBinder %p: created new %p!\n", val.get(), object);
>>>>>>> 54b6cfa... Initial Contribution
        // The proxy holds a reference to the native object.
        env->SetIntField(object, gBinderProxyOffsets.mObject, (int)val.get());
        val->incStrong(object);

        // The native object needs to hold a weak reference back to the
        // proxy, so we can retrieve the same proxy if it is still active.
        jobject refObject = env->NewGlobalRef(
                env->GetObjectField(object, gBinderProxyOffsets.mSelf));
        val->attachObject(&gBinderProxyOffsets, refObject,
                jnienv_to_javavm(env), proxy_cleanup);

<<<<<<< HEAD
        // Also remember the death recipients registered on this proxy
        sp<DeathRecipientList> drl = new DeathRecipientList;
        drl->incStrong((void*)javaObjectForIBinder);
        env->SetIntField(object, gBinderProxyOffsets.mOrgue, reinterpret_cast<jint>(drl.get()));

=======
>>>>>>> 54b6cfa... Initial Contribution
        // Note that a new object reference has been created.
        android_atomic_inc(&gNumProxyRefs);
        incRefsCreated(env);
    }

    return object;
}

sp<IBinder> ibinderForJavaObject(JNIEnv* env, jobject obj)
{
    if (obj == NULL) return NULL;

    if (env->IsInstanceOf(obj, gBinderOffsets.mClass)) {
        JavaBBinderHolder* jbh = (JavaBBinderHolder*)
            env->GetIntField(obj, gBinderOffsets.mObject);
<<<<<<< HEAD
        return jbh != NULL ? jbh->get(env, obj) : NULL;
=======
        return jbh != NULL ? jbh->get(env) : NULL;
>>>>>>> 54b6cfa... Initial Contribution
    }

    if (env->IsInstanceOf(obj, gBinderProxyOffsets.mClass)) {
        return (IBinder*)
            env->GetIntField(obj, gBinderProxyOffsets.mObject);
    }

<<<<<<< HEAD
    ALOGW("ibinderForJavaObject: %p is not a Binder object", obj);
    return NULL;
}

jobject newParcelFileDescriptor(JNIEnv* env, jobject fileDesc)
{
    return env->NewObject(
            gParcelFileDescriptorOffsets.mClass, gParcelFileDescriptorOffsets.mConstructor, fileDesc);
}

void set_dalvik_blockguard_policy(JNIEnv* env, jint strict_policy)
{
    // Call back into android.os.StrictMode#onBinderStrictModePolicyChange
    // to sync our state back to it.  See the comments in StrictMode.java.
    env->CallStaticVoidMethod(gStrictModeCallbackOffsets.mClass,
                              gStrictModeCallbackOffsets.mCallback,
                              strict_policy);
}

void signalExceptionForError(JNIEnv* env, jobject obj, status_t err,
        bool canThrowRemoteException)
=======
    LOGW("ibinderForJavaObject: %p is not a Binder object", obj);
    return NULL;
}

Parcel* parcelForJavaObject(JNIEnv* env, jobject obj)
{
    if (obj) {
        Parcel* p = (Parcel*)env->GetIntField(obj, gParcelOffsets.mObject);
        if (p != NULL) {
            return p;
        }
        jniThrowException(env, "java/lang/IllegalStateException", "Parcel has been finalized!");
    }
    return NULL;
}

jobject newFileDescriptor(JNIEnv* env, int fd)
{
    jobject object = env->NewObject(
            gFileDescriptorOffsets.mClass, gFileDescriptorOffsets.mConstructor);
    if (object != NULL) {
        //LOGI("Created new FileDescriptor %p with fd %d\n", object, fd);
        env->SetIntField(object, gFileDescriptorOffsets.mDescriptor, fd);
    }
    return object;
}

jobject newParcelFileDescriptor(JNIEnv* env, jobject fileDesc)
{
    return env->NewObject(
            gParcelFileDescriptorOffsets.mClass, gParcelFileDescriptorOffsets.mConstructor, fileDesc);
}

void signalExceptionForError(JNIEnv* env, jobject obj, status_t err)
>>>>>>> 54b6cfa... Initial Contribution
{
    switch (err) {
        case UNKNOWN_ERROR:
            jniThrowException(env, "java/lang/RuntimeException", "Unknown error");
            break;
        case NO_MEMORY:
            jniThrowException(env, "java/lang/OutOfMemoryError", NULL);
            break;
        case INVALID_OPERATION:
            jniThrowException(env, "java/lang/UnsupportedOperationException", NULL);
            break;
        case BAD_VALUE:
            jniThrowException(env, "java/lang/IllegalArgumentException", NULL);
            break;
        case BAD_INDEX:
            jniThrowException(env, "java/lang/IndexOutOfBoundsException", NULL);
            break;
        case BAD_TYPE:
            jniThrowException(env, "java/lang/IllegalArgumentException", NULL);
            break;
        case NAME_NOT_FOUND:
            jniThrowException(env, "java/util/NoSuchElementException", NULL);
            break;
        case PERMISSION_DENIED:
            jniThrowException(env, "java/lang/SecurityException", NULL);
            break;
        case NOT_ENOUGH_DATA:
            jniThrowException(env, "android/os/ParcelFormatException", "Not enough data");
            break;
        case NO_INIT:
            jniThrowException(env, "java/lang/RuntimeException", "Not initialized");
            break;
        case ALREADY_EXISTS:
            jniThrowException(env, "java/lang/RuntimeException", "Item already exists");
            break;
        case DEAD_OBJECT:
<<<<<<< HEAD
            // DeadObjectException is a checked exception, only throw from certain methods.
            jniThrowException(env, canThrowRemoteException
                    ? "android/os/DeadObjectException"
                            : "java/lang/RuntimeException", NULL);
=======
            jniThrowException(env, "android/os/DeadObjectException", NULL);
>>>>>>> 54b6cfa... Initial Contribution
            break;
        case UNKNOWN_TRANSACTION:
            jniThrowException(env, "java/lang/RuntimeException", "Unknown transaction code");
            break;
        case FAILED_TRANSACTION:
<<<<<<< HEAD
            ALOGE("!!! FAILED BINDER TRANSACTION !!!");
            // TransactionTooLargeException is a checked exception, only throw from certain methods.
            // FIXME: Transaction too large is the most common reason for FAILED_TRANSACTION
            //        but it is not the only one.  The Binder driver can return BR_FAILED_REPLY
            //        for other reasons also, such as if the transaction is malformed or
            //        refers to an FD that has been closed.  We should change the driver
            //        to enable us to distinguish these cases in the future.
            jniThrowException(env, canThrowRemoteException
                    ? "android/os/TransactionTooLargeException"
                            : "java/lang/RuntimeException", NULL);
            break;
        case FDS_NOT_ALLOWED:
            jniThrowException(env, "java/lang/RuntimeException",
                    "Not allowed to write file descriptors here");
            break;
        default:
            ALOGE("Unknown binder error code. 0x%x", err);
            String8 msg;
            msg.appendFormat("Unknown binder error code. 0x%x", err);
            // RemoteException is a checked exception, only throw from certain methods.
            jniThrowException(env, canThrowRemoteException
                    ? "android/os/RemoteException" : "java/lang/RuntimeException", msg.string());
            break;
=======
            LOGE("!!! FAILED BINDER TRANSACTION !!!");
            //jniThrowException(env, "java/lang/OutOfMemoryError", "Binder transaction too large");
            break;
        default:
            LOGE("Unknown binder error code. 0x%x", err);
>>>>>>> 54b6cfa... Initial Contribution
    }
}

}

// ----------------------------------------------------------------------------

static jint android_os_Binder_getCallingPid(JNIEnv* env, jobject clazz)
{
    return IPCThreadState::self()->getCallingPid();
}

static jint android_os_Binder_getCallingUid(JNIEnv* env, jobject clazz)
{
    return IPCThreadState::self()->getCallingUid();
}

<<<<<<< HEAD
static jint android_os_Binder_getOrigCallingUid(JNIEnv* env, jobject clazz)
{
    return IPCThreadState::self()->getOrigCallingUid();
}

=======
>>>>>>> 54b6cfa... Initial Contribution
static jlong android_os_Binder_clearCallingIdentity(JNIEnv* env, jobject clazz)
{
    return IPCThreadState::self()->clearCallingIdentity();
}

static void android_os_Binder_restoreCallingIdentity(JNIEnv* env, jobject clazz, jlong token)
{
<<<<<<< HEAD
    // XXX temporary sanity check to debug crashes.
    int uid = (int)(token>>32);
    if (uid > 0 && uid < 999) {
        // In Android currently there are no uids in this range.
        char buf[128];
        sprintf(buf, "Restoring bad calling ident: 0x%Lx", token);
        jniThrowException(env, "java/lang/IllegalStateException", buf);
        return;
    }
    IPCThreadState::self()->restoreCallingIdentity(token);
}

static void android_os_Binder_setThreadStrictModePolicy(JNIEnv* env, jobject clazz, jint policyMask)
{
    IPCThreadState::self()->setStrictModePolicy(policyMask);
}

static jint android_os_Binder_getThreadStrictModePolicy(JNIEnv* env, jobject clazz)
{
    return IPCThreadState::self()->getStrictModePolicy();
}

=======
    IPCThreadState::self()->restoreCallingIdentity(token);
}

>>>>>>> 54b6cfa... Initial Contribution
static void android_os_Binder_flushPendingCommands(JNIEnv* env, jobject clazz)
{
    IPCThreadState::self()->flushCommands();
}

<<<<<<< HEAD
static void android_os_Binder_init(JNIEnv* env, jobject obj)
{
    JavaBBinderHolder* jbh = new JavaBBinderHolder();
=======
static void android_os_Binder_init(JNIEnv* env, jobject clazz)
{
    JavaBBinderHolder* jbh = new JavaBBinderHolder(env, clazz);
>>>>>>> 54b6cfa... Initial Contribution
    if (jbh == NULL) {
        jniThrowException(env, "java/lang/OutOfMemoryError", NULL);
        return;
    }
<<<<<<< HEAD
    ALOGV("Java Binder %p: acquiring first ref on holder %p", obj, jbh);
    jbh->incStrong((void*)android_os_Binder_init);
    env->SetIntField(obj, gBinderOffsets.mObject, (int)jbh);
}

static void android_os_Binder_destroy(JNIEnv* env, jobject obj)
{
    JavaBBinderHolder* jbh = (JavaBBinderHolder*)
        env->GetIntField(obj, gBinderOffsets.mObject);
    if (jbh != NULL) {
        env->SetIntField(obj, gBinderOffsets.mObject, 0);
        ALOGV("Java Binder %p: removing ref on holder %p", obj, jbh);
        jbh->decStrong((void*)android_os_Binder_init);
    } else {
        // Encountering an uninitialized binder is harmless.  All it means is that
        // the Binder was only partially initialized when its finalizer ran and called
        // destroy().  The Binder could be partially initialized for several reasons.
        // For example, a Binder subclass constructor might have thrown an exception before
        // it could delegate to its superclass's constructor.  Consequently init() would
        // not have been called and the holder pointer would remain NULL.
        ALOGV("Java Binder %p: ignoring uninitialized binder", obj);
    }
=======
    LOGV("Java Binder %p: acquiring first ref on holder %p", clazz, jbh);
    jbh->incStrong(clazz);
    env->SetIntField(clazz, gBinderOffsets.mObject, (int)jbh);
}

static void android_os_Binder_destroy(JNIEnv* env, jobject clazz)
{
    JavaBBinderHolder* jbh = (JavaBBinderHolder*)
        env->GetIntField(clazz, gBinderOffsets.mObject);
    env->SetIntField(clazz, gBinderOffsets.mObject, 0);
    LOGV("Java Binder %p: removing ref on holder %p", clazz, jbh);
    jbh->decStrong(clazz);
>>>>>>> 54b6cfa... Initial Contribution
}

// ----------------------------------------------------------------------------

static const JNINativeMethod gBinderMethods[] = {
     /* name, signature, funcPtr */
    { "getCallingPid", "()I", (void*)android_os_Binder_getCallingPid },
    { "getCallingUid", "()I", (void*)android_os_Binder_getCallingUid },
<<<<<<< HEAD
    { "getOrigCallingUidNative", "()I", (void*)android_os_Binder_getOrigCallingUid },
    { "clearCallingIdentity", "()J", (void*)android_os_Binder_clearCallingIdentity },
    { "restoreCallingIdentity", "(J)V", (void*)android_os_Binder_restoreCallingIdentity },
    { "setThreadStrictModePolicy", "(I)V", (void*)android_os_Binder_setThreadStrictModePolicy },
    { "getThreadStrictModePolicy", "()I", (void*)android_os_Binder_getThreadStrictModePolicy },
=======
    { "clearCallingIdentity", "()J", (void*)android_os_Binder_clearCallingIdentity },
    { "restoreCallingIdentity", "(J)V", (void*)android_os_Binder_restoreCallingIdentity },
>>>>>>> 54b6cfa... Initial Contribution
    { "flushPendingCommands", "()V", (void*)android_os_Binder_flushPendingCommands },
    { "init", "()V", (void*)android_os_Binder_init },
    { "destroy", "()V", (void*)android_os_Binder_destroy }
};

const char* const kBinderPathName = "android/os/Binder";

static int int_register_android_os_Binder(JNIEnv* env)
{
    jclass clazz;

    clazz = env->FindClass(kBinderPathName);
    LOG_FATAL_IF(clazz == NULL, "Unable to find class android.os.Binder");

    gBinderOffsets.mClass = (jclass) env->NewGlobalRef(clazz);
    gBinderOffsets.mExecTransact
        = env->GetMethodID(clazz, "execTransact", "(IIII)Z");
    assert(gBinderOffsets.mExecTransact);

    gBinderOffsets.mObject
        = env->GetFieldID(clazz, "mObject", "I");
    assert(gBinderOffsets.mObject);

    return AndroidRuntime::registerNativeMethods(
        env, kBinderPathName,
        gBinderMethods, NELEM(gBinderMethods));
}

// ****************************************************************************
// ****************************************************************************
// ****************************************************************************

namespace android {

jint android_os_Debug_getLocalObjectCount(JNIEnv* env, jobject clazz)
{
    return gNumLocalRefs;
}

jint android_os_Debug_getProxyObjectCount(JNIEnv* env, jobject clazz)
{
    return gNumProxyRefs;
}

jint android_os_Debug_getDeathObjectCount(JNIEnv* env, jobject clazz)
{
    return gNumDeathRefs;
}

}

// ****************************************************************************
// ****************************************************************************
// ****************************************************************************

static jobject android_os_BinderInternal_getContextObject(JNIEnv* env, jobject clazz)
{
    sp<IBinder> b = ProcessState::self()->getContextObject(NULL);
    return javaObjectForIBinder(env, b);
}

static void android_os_BinderInternal_joinThreadPool(JNIEnv* env, jobject clazz)
{
    sp<IBinder> b = ProcessState::self()->getContextObject(NULL);
    android::IPCThreadState::self()->joinThreadPool();
}

<<<<<<< HEAD
static void android_os_BinderInternal_disableBackgroundScheduling(JNIEnv* env,
        jobject clazz, jboolean disable)
{
    IPCThreadState::disableBackgroundScheduling(disable ? true : false);
}

static void android_os_BinderInternal_handleGc(JNIEnv* env, jobject clazz)
{
    ALOGV("Gc has executed, clearing binder ops");
=======
static void android_os_BinderInternal_handleGc(JNIEnv* env, jobject clazz)
{
    LOGV("Gc has executed, clearing binder ops");
>>>>>>> 54b6cfa... Initial Contribution
    android_atomic_and(0, &gNumRefsCreated);
}

// ----------------------------------------------------------------------------

static const JNINativeMethod gBinderInternalMethods[] = {
     /* name, signature, funcPtr */
    { "getContextObject", "()Landroid/os/IBinder;", (void*)android_os_BinderInternal_getContextObject },
    { "joinThreadPool", "()V", (void*)android_os_BinderInternal_joinThreadPool },
<<<<<<< HEAD
    { "disableBackgroundScheduling", "(Z)V", (void*)android_os_BinderInternal_disableBackgroundScheduling },
=======
>>>>>>> 54b6cfa... Initial Contribution
    { "handleGc", "()V", (void*)android_os_BinderInternal_handleGc }
};

const char* const kBinderInternalPathName = "com/android/internal/os/BinderInternal";

static int int_register_android_os_BinderInternal(JNIEnv* env)
{
    jclass clazz;

    clazz = env->FindClass(kBinderInternalPathName);
    LOG_FATAL_IF(clazz == NULL, "Unable to find class com.android.internal.os.BinderInternal");

    gBinderInternalOffsets.mClass = (jclass) env->NewGlobalRef(clazz);
    gBinderInternalOffsets.mForceGc
        = env->GetStaticMethodID(clazz, "forceBinderGc", "()V");
    assert(gBinderInternalOffsets.mForceGc);

    return AndroidRuntime::registerNativeMethods(
        env, kBinderInternalPathName,
        gBinderInternalMethods, NELEM(gBinderInternalMethods));
}

// ****************************************************************************
// ****************************************************************************
// ****************************************************************************

static jboolean android_os_BinderProxy_pingBinder(JNIEnv* env, jobject obj)
{
    IBinder* target = (IBinder*)
        env->GetIntField(obj, gBinderProxyOffsets.mObject);
    if (target == NULL) {
        return JNI_FALSE;
    }
    status_t err = target->pingBinder();
    return err == NO_ERROR ? JNI_TRUE : JNI_FALSE;
}

static jstring android_os_BinderProxy_getInterfaceDescriptor(JNIEnv* env, jobject obj)
{
    IBinder* target = (IBinder*) env->GetIntField(obj, gBinderProxyOffsets.mObject);
    if (target != NULL) {
<<<<<<< HEAD
        const String16& desc = target->getInterfaceDescriptor();
=======
        String16 desc = target->getInterfaceDescriptor();
>>>>>>> 54b6cfa... Initial Contribution
        return env->NewString(desc.string(), desc.size());
    }
    jniThrowException(env, "java/lang/RuntimeException",
            "No binder found for object");
    return NULL;
}

static jboolean android_os_BinderProxy_isBinderAlive(JNIEnv* env, jobject obj)
{
    IBinder* target = (IBinder*)
        env->GetIntField(obj, gBinderProxyOffsets.mObject);
    if (target == NULL) {
        return JNI_FALSE;
    }
    bool alive = target->isBinderAlive();
    return alive ? JNI_TRUE : JNI_FALSE;
}

<<<<<<< HEAD
static int getprocname(pid_t pid, char *buf, size_t len) {
    char filename[20];
    FILE *f;

    sprintf(filename, "/proc/%d/cmdline", pid);
    f = fopen(filename, "r");
    if (!f) { *buf = '\0'; return 1; }
    if (!fgets(buf, len, f)) { *buf = '\0'; return 2; }
    fclose(f);
    return 0;
}

static bool push_eventlog_string(char** pos, const char* end, const char* str) {
    jint len = strlen(str);
    int space_needed = 1 + sizeof(len) + len;
    if (end - *pos < space_needed) {
        ALOGW("not enough space for string. remain=%d; needed=%d",
             (end - *pos), space_needed);
        return false;
    }
    **pos = EVENT_TYPE_STRING;
    (*pos)++;
    memcpy(*pos, &len, sizeof(len));
    *pos += sizeof(len);
    memcpy(*pos, str, len);
    *pos += len;
    return true;
}

static bool push_eventlog_int(char** pos, const char* end, jint val) {
    int space_needed = 1 + sizeof(val);
    if (end - *pos < space_needed) {
        ALOGW("not enough space for int.  remain=%d; needed=%d",
             (end - *pos), space_needed);
        return false;
    }
    **pos = EVENT_TYPE_INT;
    (*pos)++;
    memcpy(*pos, &val, sizeof(val));
    *pos += sizeof(val);
    return true;
}

// From frameworks/base/core/java/android/content/EventLogTags.logtags:
#define LOGTAG_BINDER_OPERATION 52004

static void conditionally_log_binder_call(int64_t start_millis,
                                          IBinder* target, jint code) {
    int duration_ms = static_cast<int>(uptimeMillis() - start_millis);

    int sample_percent;
    if (duration_ms >= 500) {
        sample_percent = 100;
    } else {
        sample_percent = 100 * duration_ms / 500;
        if (sample_percent == 0) {
            return;
        }
        if (sample_percent < (random() % 100 + 1)) {
            return;
        }
    }

    char process_name[40];
    getprocname(getpid(), process_name, sizeof(process_name));
    String8 desc(target->getInterfaceDescriptor());

    char buf[LOGGER_ENTRY_MAX_PAYLOAD];
    buf[0] = EVENT_TYPE_LIST;
    buf[1] = 5;
    char* pos = &buf[2];
    char* end = &buf[LOGGER_ENTRY_MAX_PAYLOAD - 1];  // leave room for final \n
    if (!push_eventlog_string(&pos, end, desc.string())) return;
    if (!push_eventlog_int(&pos, end, code)) return;
    if (!push_eventlog_int(&pos, end, duration_ms)) return;
    if (!push_eventlog_string(&pos, end, process_name)) return;
    if (!push_eventlog_int(&pos, end, sample_percent)) return;
    *(pos++) = '\n';   // conventional with EVENT_TYPE_LIST apparently.
    android_bWriteLog(LOGTAG_BINDER_OPERATION, buf, pos - buf);
}

// We only measure binder call durations to potentially log them if
// we're on the main thread.  Unfortunately sim-eng doesn't seem to
// have gettid, so we just ignore this and don't log if we can't
// get the thread id.
static bool should_time_binder_calls() {
#ifdef HAVE_GETTID
  return (getpid() == androidGetTid());
#else
#warning no gettid(), so not logging Binder calls...
  return false;
#endif
}

static jboolean android_os_BinderProxy_transact(JNIEnv* env, jobject obj,
        jint code, jobject dataObj, jobject replyObj, jint flags) // throws RemoteException
{
    if (dataObj == NULL) {
        jniThrowNullPointerException(env, NULL);
=======
static jboolean android_os_BinderProxy_transact(JNIEnv* env, jobject obj,
                                                jint code, jobject dataObj,
                                                jobject replyObj, jint flags)
{
    if (dataObj == NULL) {
        jniThrowException(env, "java/lang/NullPointerException", NULL);
>>>>>>> 54b6cfa... Initial Contribution
        return JNI_FALSE;
    }

    Parcel* data = parcelForJavaObject(env, dataObj);
    if (data == NULL) {
        return JNI_FALSE;
    }
    Parcel* reply = parcelForJavaObject(env, replyObj);
    if (reply == NULL && replyObj != NULL) {
        return JNI_FALSE;
    }

    IBinder* target = (IBinder*)
        env->GetIntField(obj, gBinderProxyOffsets.mObject);
    if (target == NULL) {
        jniThrowException(env, "java/lang/IllegalStateException", "Binder has been finalized!");
        return JNI_FALSE;
    }

<<<<<<< HEAD
    ALOGV("Java code calling transact on %p in Java object %p with code %d\n",
            target, obj, code);

    // Only log the binder call duration for things on the Java-level main thread.
    // But if we don't
    const bool time_binder_calls = should_time_binder_calls();

    int64_t start_millis;
    if (time_binder_calls) {
        start_millis = uptimeMillis();
    }
    //printf("Transact from Java code to %p sending: ", target); data->print();
    status_t err = target->transact(code, *data, reply, flags);
    //if (reply) printf("Transact from Java code to %p received: ", target); reply->print();
    if (time_binder_calls) {
        conditionally_log_binder_call(start_millis, target, code);
    }

=======
    LOGV("Java code calling transact on %p in Java object %p with code %d\n",
            target, obj, code);
    //printf("Transact from Java code to %p sending: ", target); data->print();
    status_t err = target->transact(code, *data, reply, flags);
    //if (reply) printf("Transact from Java code to %p received: ", target); reply->print();
>>>>>>> 54b6cfa... Initial Contribution
    if (err == NO_ERROR) {
        return JNI_TRUE;
    } else if (err == UNKNOWN_TRANSACTION) {
        return JNI_FALSE;
    }

<<<<<<< HEAD
    signalExceptionForError(env, obj, err, true /*canThrowRemoteException*/);
=======
    signalExceptionForError(env, obj, err);
>>>>>>> 54b6cfa... Initial Contribution
    return JNI_FALSE;
}

static void android_os_BinderProxy_linkToDeath(JNIEnv* env, jobject obj,
<<<<<<< HEAD
        jobject recipient, jint flags) // throws RemoteException
{
    if (recipient == NULL) {
        jniThrowNullPointerException(env, NULL);
=======
                                               jobject recipient, jint flags)
{
    if (recipient == NULL) {
        jniThrowException(env, "java/lang/NullPointerException", NULL);
>>>>>>> 54b6cfa... Initial Contribution
        return;
    }

    IBinder* target = (IBinder*)
        env->GetIntField(obj, gBinderProxyOffsets.mObject);
    if (target == NULL) {
<<<<<<< HEAD
        ALOGW("Binder has been finalized when calling linkToDeath() with recip=%p)\n", recipient);
        assert(false);
    }

    LOGDEATH("linkToDeath: binder=%p recipient=%p\n", target, recipient);

    if (!target->localBinder()) {
        DeathRecipientList* list = (DeathRecipientList*)
                env->GetIntField(obj, gBinderProxyOffsets.mOrgue);
        sp<JavaDeathRecipient> jdr = new JavaDeathRecipient(env, recipient, list);
        status_t err = target->linkToDeath(jdr, NULL, flags);
=======
        LOGW("Binder has been finalized when calling linkToDeath() with recip=%p)\n", recipient);
        assert(false);
    }

    LOGV("linkToDeath: binder=%p recipient=%p\n", target, recipient);

    if (!target->localBinder()) {
        sp<JavaDeathRecipient> jdr = new JavaDeathRecipient(env, recipient);
        status_t err = target->linkToDeath(jdr, recipient, flags);
>>>>>>> 54b6cfa... Initial Contribution
        if (err != NO_ERROR) {
            // Failure adding the death recipient, so clear its reference
            // now.
            jdr->clearReference();
<<<<<<< HEAD
            signalExceptionForError(env, obj, err, true /*canThrowRemoteException*/);
=======
            signalExceptionForError(env, obj, err);
>>>>>>> 54b6cfa... Initial Contribution
        }
    }
}

static jboolean android_os_BinderProxy_unlinkToDeath(JNIEnv* env, jobject obj,
                                                 jobject recipient, jint flags)
{
    jboolean res = JNI_FALSE;
    if (recipient == NULL) {
<<<<<<< HEAD
        jniThrowNullPointerException(env, NULL);
=======
        jniThrowException(env, "java/lang/NullPointerException", NULL);
>>>>>>> 54b6cfa... Initial Contribution
        return res;
    }

    IBinder* target = (IBinder*)
        env->GetIntField(obj, gBinderProxyOffsets.mObject);
    if (target == NULL) {
<<<<<<< HEAD
        ALOGW("Binder has been finalized when calling linkToDeath() with recip=%p)\n", recipient);
        return JNI_FALSE;
    }

    LOGDEATH("unlinkToDeath: binder=%p recipient=%p\n", target, recipient);

    if (!target->localBinder()) {
        status_t err = NAME_NOT_FOUND;

        // If we find the matching recipient, proceed to unlink using that
        DeathRecipientList* list = (DeathRecipientList*)
                env->GetIntField(obj, gBinderProxyOffsets.mOrgue);
        sp<JavaDeathRecipient> origJDR = list->find(recipient);
        LOGDEATH("   unlink found list %p and JDR %p", list, origJDR.get());
        if (origJDR != NULL) {
            wp<IBinder::DeathRecipient> dr;
            err = target->unlinkToDeath(origJDR, NULL, flags, &dr);
            if (err == NO_ERROR && dr != NULL) {
                sp<IBinder::DeathRecipient> sdr = dr.promote();
                JavaDeathRecipient* jdr = static_cast<JavaDeathRecipient*>(sdr.get());
                if (jdr != NULL) {
                    jdr->clearReference();
                }
            }
        }

=======
        LOGW("Binder has been finalized when calling linkToDeath() with recip=%p)\n", recipient);
        return JNI_FALSE;
    }

    LOGV("unlinkToDeath: binder=%p recipient=%p\n", target, recipient);

    if (!target->localBinder()) {
        wp<IBinder::DeathRecipient> dr;
        status_t err = target->unlinkToDeath(NULL, recipient, flags, &dr);
        if (err == NO_ERROR && dr != NULL) {
            sp<IBinder::DeathRecipient> sdr = dr.promote();
            JavaDeathRecipient* jdr = static_cast<JavaDeathRecipient*>(sdr.get());
            if (jdr != NULL) {
                jdr->clearReference();
            }
        }
>>>>>>> 54b6cfa... Initial Contribution
        if (err == NO_ERROR || err == DEAD_OBJECT) {
            res = JNI_TRUE;
        } else {
            jniThrowException(env, "java/util/NoSuchElementException",
                              "Death link does not exist");
        }
    }

    return res;
}

static void android_os_BinderProxy_destroy(JNIEnv* env, jobject obj)
{
    IBinder* b = (IBinder*)
<<<<<<< HEAD
            env->GetIntField(obj, gBinderProxyOffsets.mObject);
    DeathRecipientList* drl = (DeathRecipientList*)
            env->GetIntField(obj, gBinderProxyOffsets.mOrgue);

    LOGDEATH("Destroying BinderProxy %p: binder=%p drl=%p\n", obj, b, drl);
    env->SetIntField(obj, gBinderProxyOffsets.mObject, 0);
    env->SetIntField(obj, gBinderProxyOffsets.mOrgue, 0);
    drl->decStrong((void*)javaObjectForIBinder);
    b->decStrong(obj);

=======
        env->GetIntField(obj, gBinderProxyOffsets.mObject);
    LOGV("Destroying BinderProxy %p: binder=%p\n", obj, b);
    env->SetIntField(obj, gBinderProxyOffsets.mObject, 0);
    b->decStrong(obj);
>>>>>>> 54b6cfa... Initial Contribution
    IPCThreadState::self()->flushCommands();
}

// ----------------------------------------------------------------------------

static const JNINativeMethod gBinderProxyMethods[] = {
     /* name, signature, funcPtr */
    {"pingBinder",          "()Z", (void*)android_os_BinderProxy_pingBinder},
    {"isBinderAlive",       "()Z", (void*)android_os_BinderProxy_isBinderAlive},
    {"getInterfaceDescriptor", "()Ljava/lang/String;", (void*)android_os_BinderProxy_getInterfaceDescriptor},
    {"transact",            "(ILandroid/os/Parcel;Landroid/os/Parcel;I)Z", (void*)android_os_BinderProxy_transact},
    {"linkToDeath",         "(Landroid/os/IBinder$DeathRecipient;I)V", (void*)android_os_BinderProxy_linkToDeath},
    {"unlinkToDeath",       "(Landroid/os/IBinder$DeathRecipient;I)Z", (void*)android_os_BinderProxy_unlinkToDeath},
    {"destroy",             "()V", (void*)android_os_BinderProxy_destroy},
};

const char* const kBinderProxyPathName = "android/os/BinderProxy";

static int int_register_android_os_BinderProxy(JNIEnv* env)
{
    jclass clazz;

    clazz = env->FindClass("java/lang/ref/WeakReference");
    LOG_FATAL_IF(clazz == NULL, "Unable to find class java.lang.ref.WeakReference");
    gWeakReferenceOffsets.mClass = (jclass) env->NewGlobalRef(clazz);
    gWeakReferenceOffsets.mGet
        = env->GetMethodID(clazz, "get", "()Ljava/lang/Object;");
    assert(gWeakReferenceOffsets.mGet);

    clazz = env->FindClass("java/lang/Error");
    LOG_FATAL_IF(clazz == NULL, "Unable to find class java.lang.Error");
    gErrorOffsets.mClass = (jclass) env->NewGlobalRef(clazz);
<<<<<<< HEAD

=======
    
>>>>>>> 54b6cfa... Initial Contribution
    clazz = env->FindClass(kBinderProxyPathName);
    LOG_FATAL_IF(clazz == NULL, "Unable to find class android.os.BinderProxy");

    gBinderProxyOffsets.mClass = (jclass) env->NewGlobalRef(clazz);
    gBinderProxyOffsets.mConstructor
        = env->GetMethodID(clazz, "<init>", "()V");
    assert(gBinderProxyOffsets.mConstructor);
    gBinderProxyOffsets.mSendDeathNotice
        = env->GetStaticMethodID(clazz, "sendDeathNotice", "(Landroid/os/IBinder$DeathRecipient;)V");
    assert(gBinderProxyOffsets.mSendDeathNotice);

    gBinderProxyOffsets.mObject
        = env->GetFieldID(clazz, "mObject", "I");
    assert(gBinderProxyOffsets.mObject);
    gBinderProxyOffsets.mSelf
        = env->GetFieldID(clazz, "mSelf", "Ljava/lang/ref/WeakReference;");
    assert(gBinderProxyOffsets.mSelf);
<<<<<<< HEAD
    gBinderProxyOffsets.mOrgue
        = env->GetFieldID(clazz, "mOrgue", "I");
    assert(gBinderProxyOffsets.mOrgue);

    clazz = env->FindClass("java/lang/Class");
    LOG_FATAL_IF(clazz == NULL, "Unable to find java.lang.Class");
    gClassOffsets.mGetName = env->GetMethodID(clazz, "getName", "()Ljava/lang/String;");
    assert(gClassOffsets.mGetName);
=======
>>>>>>> 54b6cfa... Initial Contribution

    return AndroidRuntime::registerNativeMethods(
        env, kBinderProxyPathName,
        gBinderProxyMethods, NELEM(gBinderProxyMethods));
}

// ****************************************************************************
// ****************************************************************************
// ****************************************************************************

<<<<<<< HEAD
int register_android_os_Binder(JNIEnv* env)
{
    if (int_register_android_os_Binder(env) < 0)
        return -1;
    if (int_register_android_os_BinderInternal(env) < 0)
        return -1;
    if (int_register_android_os_BinderProxy(env) < 0)
        return -1;

=======
static jint android_os_Parcel_dataSize(JNIEnv* env, jobject clazz)
{
    Parcel* parcel = parcelForJavaObject(env, clazz);
    return parcel ? parcel->dataSize() : 0;
}

static jint android_os_Parcel_dataAvail(JNIEnv* env, jobject clazz)
{
    Parcel* parcel = parcelForJavaObject(env, clazz);
    return parcel ? parcel->dataAvail() : 0;
}

static jint android_os_Parcel_dataPosition(JNIEnv* env, jobject clazz)
{
    Parcel* parcel = parcelForJavaObject(env, clazz);
    return parcel ? parcel->dataPosition() : 0;
}

static jint android_os_Parcel_dataCapacity(JNIEnv* env, jobject clazz)
{
    Parcel* parcel = parcelForJavaObject(env, clazz);
    return parcel ? parcel->dataCapacity() : 0;
}

static void android_os_Parcel_setDataSize(JNIEnv* env, jobject clazz, jint size)
{
    Parcel* parcel = parcelForJavaObject(env, clazz);
    if (parcel != NULL) {
        const status_t err = parcel->setDataSize(size);
        if (err != NO_ERROR) {
            jniThrowException(env, "java/lang/OutOfMemoryError", NULL);
        }
    }
}

static void android_os_Parcel_setDataPosition(JNIEnv* env, jobject clazz, jint pos)
{
    Parcel* parcel = parcelForJavaObject(env, clazz);
    if (parcel != NULL) {
        parcel->setDataPosition(pos);
    }
}

static void android_os_Parcel_setDataCapacity(JNIEnv* env, jobject clazz, jint size)
{
    Parcel* parcel = parcelForJavaObject(env, clazz);
    if (parcel != NULL) {
        const status_t err = parcel->setDataCapacity(size);
        if (err != NO_ERROR) {
            jniThrowException(env, "java/lang/OutOfMemoryError", NULL);
        }
    }
}

static void android_os_Parcel_writeNative(JNIEnv* env, jobject clazz,
                                          jobject data, jint offset,
                                          jint length)
{
    Parcel* parcel = parcelForJavaObject(env, clazz);
    if (parcel == NULL) {
        return;
    }
    void *dest;

    const status_t err = parcel->writeInt32(length);
    if (err != NO_ERROR) {
        jniThrowException(env, "java/lang/OutOfMemoryError", NULL);
    }

    dest = parcel->writeInplace(length);

    if (dest == NULL) {
        jniThrowException(env, "java/lang/OutOfMemoryError", NULL);
        return;
    }

    jbyte* ar = (jbyte*)env->GetPrimitiveArrayCritical((jarray)data, 0);
    if (ar) {
        memcpy(dest, ar, length);
        env->ReleasePrimitiveArrayCritical((jarray)data, ar, 0);
    }
}


static void android_os_Parcel_writeInt(JNIEnv* env, jobject clazz, jint val)
{
    Parcel* parcel = parcelForJavaObject(env, clazz);
    if (parcel != NULL) {
        const status_t err = parcel->writeInt32(val);
        if (err != NO_ERROR) {
            jniThrowException(env, "java/lang/OutOfMemoryError", NULL);
        }
    }
}

static void android_os_Parcel_writeLong(JNIEnv* env, jobject clazz, jlong val)
{
    Parcel* parcel = parcelForJavaObject(env, clazz);
    if (parcel != NULL) {
        const status_t err = parcel->writeInt64(val);
        if (err != NO_ERROR) {
            jniThrowException(env, "java/lang/OutOfMemoryError", NULL);
        }
    }
}

static void android_os_Parcel_writeFloat(JNIEnv* env, jobject clazz, jfloat val)
{
    Parcel* parcel = parcelForJavaObject(env, clazz);
    if (parcel != NULL) {
        const status_t err = parcel->writeFloat(val);
        if (err != NO_ERROR) {
            jniThrowException(env, "java/lang/OutOfMemoryError", NULL);
        }
    }
}

static void android_os_Parcel_writeDouble(JNIEnv* env, jobject clazz, jdouble val)
{
    Parcel* parcel = parcelForJavaObject(env, clazz);
    if (parcel != NULL) {
        const status_t err = parcel->writeDouble(val);
        if (err != NO_ERROR) {
            jniThrowException(env, "java/lang/OutOfMemoryError", NULL);
        }
    }
}

static void android_os_Parcel_writeString(JNIEnv* env, jobject clazz, jstring val)
{
    Parcel* parcel = parcelForJavaObject(env, clazz);
    if (parcel != NULL) {
        status_t err = NO_MEMORY;
        if (val) {
            const jchar* str = env->GetStringCritical(val, 0);
            if (str) {
                err = parcel->writeString16(str, env->GetStringLength(val));
                env->ReleaseStringCritical(val, str);
            }
        } else {
            err = parcel->writeString16(NULL, 0);
        }
        if (err != NO_ERROR) {
            jniThrowException(env, "java/lang/OutOfMemoryError", NULL);
        }
    }
}

static void android_os_Parcel_writeStrongBinder(JNIEnv* env, jobject clazz, jobject object)
{
    Parcel* parcel = parcelForJavaObject(env, clazz);
    if (parcel != NULL) {
        const status_t err = parcel->writeStrongBinder(ibinderForJavaObject(env, object));
        if (err != NO_ERROR) {
            jniThrowException(env, "java/lang/OutOfMemoryError", NULL);
        }
    }
}

static void android_os_Parcel_writeFileDescriptor(JNIEnv* env, jobject clazz, jobject object)
{
    Parcel* parcel = parcelForJavaObject(env, clazz);
    if (parcel != NULL) {
        const status_t err = parcel->writeDupFileDescriptor(
                env->GetIntField(object, gFileDescriptorOffsets.mDescriptor));
        if (err != NO_ERROR) {
            jniThrowException(env, "java/lang/OutOfMemoryError", NULL);
        }
    }
}

static jbyteArray android_os_Parcel_createByteArray(JNIEnv* env, jobject clazz)
{
    jbyteArray ret = NULL;

    Parcel* parcel = parcelForJavaObject(env, clazz);
    if (parcel != NULL) {
        int32_t len = parcel->readInt32();

        // sanity check the stored length against the true data size
        if (len >= 0 && len <= (int32_t)parcel->dataAvail()) {
            ret = env->NewByteArray(len);

            if (ret != NULL) {
                jbyte* a2 = (jbyte*)env->GetPrimitiveArrayCritical(ret, 0);
                if (a2) {
                    const void* data = parcel->readInplace(len);
                    memcpy(a2, data, len);
                    env->ReleasePrimitiveArrayCritical(ret, a2, 0);
                }
            }
        }
    }

    return ret;
}

static jint android_os_Parcel_readInt(JNIEnv* env, jobject clazz)
{
    Parcel* parcel = parcelForJavaObject(env, clazz);
    if (parcel != NULL) {
        return parcel->readInt32();
    }
    return 0;
}

static jlong android_os_Parcel_readLong(JNIEnv* env, jobject clazz)
{
    Parcel* parcel = parcelForJavaObject(env, clazz);
    if (parcel != NULL) {
        return parcel->readInt64();
    }
    return 0;
}

static jfloat android_os_Parcel_readFloat(JNIEnv* env, jobject clazz)
{
    Parcel* parcel = parcelForJavaObject(env, clazz);
    if (parcel != NULL) {
        return parcel->readFloat();
    }
    return 0;
}

static jdouble android_os_Parcel_readDouble(JNIEnv* env, jobject clazz)
{
    Parcel* parcel = parcelForJavaObject(env, clazz);
    if (parcel != NULL) {
        return parcel->readDouble();
    }
    return 0;
}

static jstring android_os_Parcel_readString(JNIEnv* env, jobject clazz)
{
    Parcel* parcel = parcelForJavaObject(env, clazz);
    if (parcel != NULL) {
        size_t len;
        const char16_t* str = parcel->readString16Inplace(&len);
        if (str) {
            return env->NewString(str, len);
        }
        return NULL;
    }
    return NULL;
}

static jobject android_os_Parcel_readStrongBinder(JNIEnv* env, jobject clazz)
{
    Parcel* parcel = parcelForJavaObject(env, clazz);
    if (parcel != NULL) {
        return javaObjectForIBinder(env, parcel->readStrongBinder());
    }
    return NULL;
}

static jobject android_os_Parcel_readFileDescriptor(JNIEnv* env, jobject clazz)
{
    Parcel* parcel = parcelForJavaObject(env, clazz);
    if (parcel != NULL) {
        int fd = parcel->readFileDescriptor();
        if (fd < 0) return NULL;
        fd = dup(fd);
        if (fd < 0) return NULL;
        jobject object = env->NewObject(
                gFileDescriptorOffsets.mClass, gFileDescriptorOffsets.mConstructor);
        if (object != NULL) {
            //LOGI("Created new FileDescriptor %p with fd %d\n", object, fd);
            env->SetIntField(object, gFileDescriptorOffsets.mDescriptor, fd);
        }
        return object;
    }
    return NULL;
}

static jobject android_os_Parcel_openFileDescriptor(JNIEnv* env, jobject clazz,
                                                    jstring name, jint mode)
{
    if (name == NULL) {
        jniThrowException(env, "java/lang/NullPointerException", NULL);
        return NULL;
    }
    const jchar* str = env->GetStringCritical(name, 0);
    if (str == NULL) {
        // Whatever, whatever.
        jniThrowException(env, "java/lang/IllegalStateException", NULL);
        return NULL;
    }
    String8 name8(str, env->GetStringLength(name));
    env->ReleaseStringCritical(name, str);
    int flags=0;
    switch (mode&0x30000000) {
        case 0:
        case 0x10000000:
            flags = O_RDONLY;
            break;
        case 0x20000000:
            flags = O_WRONLY;
            break;
        case 0x30000000:
            flags = O_RDWR;
            break;
    }

    if (mode&0x08000000) flags |= O_CREAT;
    if (mode&0x04000000) flags |= O_TRUNC;

    int realMode = S_IRWXU|S_IRWXG;
    if (mode&0x00000001) realMode |= S_IROTH;
    if (mode&0x00000002) realMode |= S_IWOTH;

    int fd = open(name8.string(), flags, realMode);
    if (fd < 0) {
        jniThrowException(env, "java/io/FileNotFoundException", NULL);
        return NULL;
    }
    jobject object = newFileDescriptor(env, fd);
    if (object == NULL) {
        close(fd);
    }
    return object;
}

static void android_os_Parcel_closeFileDescriptor(JNIEnv* env, jobject clazz, jobject object)
{
    int fd = env->GetIntField(object, gFileDescriptorOffsets.mDescriptor);
    if (fd >= 0) {
        env->SetIntField(object, gFileDescriptorOffsets.mDescriptor, -1);
        //LOGI("Closing ParcelFileDescriptor %d\n", fd);
        close(fd);
    }
}

static void android_os_Parcel_freeBuffer(JNIEnv* env, jobject clazz)
{
    int32_t own = env->GetIntField(clazz, gParcelOffsets.mOwnObject);
    if (own) {
        Parcel* parcel = parcelForJavaObject(env, clazz);
        if (parcel != NULL) {
            //LOGI("Parcel.freeBuffer() called for C++ Parcel %p\n", parcel);
            parcel->freeData();
        }
    }
}

static void android_os_Parcel_init(JNIEnv* env, jobject clazz, jint parcelInt)
{
    Parcel* parcel = (Parcel*)parcelInt;
    int own = 0;
    if (!parcel) {
        //LOGI("Initializing obj %p: creating new Parcel\n", clazz);
        own = 1;
        parcel = new Parcel;
    } else {
        //LOGI("Initializing obj %p: given existing Parcel %p\n", clazz, parcel);
    }
    if (parcel == NULL) {
        jniThrowException(env, "java/lang/OutOfMemoryError", NULL);
        return;
    }
    //LOGI("Initializing obj %p from C++ Parcel %p, own=%d\n", clazz, parcel, own);
    env->SetIntField(clazz, gParcelOffsets.mOwnObject, own);
    env->SetIntField(clazz, gParcelOffsets.mObject, (int)parcel);
}

static void android_os_Parcel_destroy(JNIEnv* env, jobject clazz)
{
    int32_t own = env->GetIntField(clazz, gParcelOffsets.mOwnObject);
    if (own) {
        Parcel* parcel = parcelForJavaObject(env, clazz);
        env->SetIntField(clazz, gParcelOffsets.mObject, 0);
        //LOGI("Destroying obj %p: deleting C++ Parcel %p\n", clazz, parcel);
        delete parcel;
    } else {
        env->SetIntField(clazz, gParcelOffsets.mObject, 0);
        //LOGI("Destroying obj %p: leaving C++ Parcel %p\n", clazz);
    }
}

static jbyteArray android_os_Parcel_marshall(JNIEnv* env, jobject clazz)
{
    Parcel* parcel = parcelForJavaObject(env, clazz);
    if (parcel == NULL) {
       return NULL;
    }

    // do not marshall if there are binder objects in the parcel
    if (parcel->objectsCount())
    {
        jniThrowException(env, "java/lang/RuntimeException", "Tried to marshall a Parcel that contained Binder objects.");
        return NULL;
    }

    jbyteArray ret = env->NewByteArray(parcel->dataSize());

    if (ret != NULL)
    {
        jbyte* array = (jbyte*)env->GetPrimitiveArrayCritical(ret, 0);
        if (array != NULL)
        {
            memcpy(array, parcel->data(), parcel->dataSize());
            env->ReleasePrimitiveArrayCritical(ret, array, 0);
        }
    }

    return ret;
}

static void android_os_Parcel_unmarshall(JNIEnv* env, jobject clazz, jbyteArray data, jint offset, jint length)
{
    Parcel* parcel = parcelForJavaObject(env, clazz);
    if (parcel == NULL || length < 0) {
       return;
    }

    jbyte* array = (jbyte*)env->GetPrimitiveArrayCritical(data, 0);
    if (array)
    {
        parcel->setDataSize(length);
        parcel->setDataPosition(0);

        void* raw = parcel->writeInplace(length);
        memcpy(raw, (array + offset), length);

        env->ReleasePrimitiveArrayCritical(data, array, 0);
    }
}

static void android_os_Parcel_appendFrom(JNIEnv* env, jobject clazz, jobject parcel, jint offset, jint length)
{
    Parcel* thisParcel = parcelForJavaObject(env, clazz);
    if (thisParcel == NULL) {
       return;
    }
    Parcel* otherParcel = parcelForJavaObject(env, parcel);
    if (otherParcel == NULL) {
       return;
    }

    (void) thisParcel->appendFrom(otherParcel, offset, length);
}

static jboolean android_os_Parcel_hasFileDescriptors(JNIEnv* env, jobject clazz)
{
    jboolean ret = JNI_FALSE;
    Parcel* parcel = parcelForJavaObject(env, clazz);
    if (parcel != NULL) {
        if (parcel->hasFileDescriptors()) {
            ret = JNI_TRUE;
        }
    }
    return ret;
}

static void android_os_Parcel_writeInterfaceToken(JNIEnv* env, jobject clazz, jstring name)
{
    Parcel* parcel = parcelForJavaObject(env, clazz);
    if (parcel != NULL) {
        // In the current implementation, the token is just the serialized interface name that
        // the caller expects to be invoking
        const jchar* str = env->GetStringCritical(name, 0);
        if (str != NULL) {
            parcel->writeInterfaceToken(String16(str, env->GetStringLength(name)));
            env->ReleaseStringCritical(name, str);
        }
    }
}

static void android_os_Parcel_enforceInterface(JNIEnv* env, jobject clazz, jstring name)
{
    jboolean ret = JNI_FALSE;

    Parcel* parcel = parcelForJavaObject(env, clazz);
    if (parcel != NULL) {
        const jchar* str = env->GetStringCritical(name, 0);
        if (str) {
            bool isValid = parcel->enforceInterface(String16(str, env->GetStringLength(name)));
            env->ReleaseStringCritical(name, str);
            if (isValid) {
                return;     // everything was correct -> return silently
            }
        }
    }

    // all error conditions wind up here
    jniThrowException(env, "java/lang/SecurityException",
            "Binder invocation to an incorrect interface");
}

// ----------------------------------------------------------------------------

static const JNINativeMethod gParcelMethods[] = {
    {"dataSize",            "()I", (void*)android_os_Parcel_dataSize},
    {"dataAvail",           "()I", (void*)android_os_Parcel_dataAvail},
    {"dataPosition",        "()I", (void*)android_os_Parcel_dataPosition},
    {"dataCapacity",        "()I", (void*)android_os_Parcel_dataCapacity},
    {"setDataSize",         "(I)V", (void*)android_os_Parcel_setDataSize},
    {"setDataPosition",     "(I)V", (void*)android_os_Parcel_setDataPosition},
    {"setDataCapacity",     "(I)V", (void*)android_os_Parcel_setDataCapacity},
    {"writeNative",         "([BII)V", (void*)android_os_Parcel_writeNative},
    {"writeInt",            "(I)V", (void*)android_os_Parcel_writeInt},
    {"writeLong",           "(J)V", (void*)android_os_Parcel_writeLong},
    {"writeFloat",          "(F)V", (void*)android_os_Parcel_writeFloat},
    {"writeDouble",         "(D)V", (void*)android_os_Parcel_writeDouble},
    {"writeString",         "(Ljava/lang/String;)V", (void*)android_os_Parcel_writeString},
    {"writeStrongBinder",   "(Landroid/os/IBinder;)V", (void*)android_os_Parcel_writeStrongBinder},
    {"writeFileDescriptor", "(Ljava/io/FileDescriptor;)V", (void*)android_os_Parcel_writeFileDescriptor},
    {"createByteArray",     "()[B", (void*)android_os_Parcel_createByteArray},
    {"readInt",             "()I", (void*)android_os_Parcel_readInt},
    {"readLong",            "()J", (void*)android_os_Parcel_readLong},
    {"readFloat",           "()F", (void*)android_os_Parcel_readFloat},
    {"readDouble",          "()D", (void*)android_os_Parcel_readDouble},
    {"readString",          "()Ljava/lang/String;", (void*)android_os_Parcel_readString},
    {"readStrongBinder",    "()Landroid/os/IBinder;", (void*)android_os_Parcel_readStrongBinder},
    {"internalReadFileDescriptor",  "()Ljava/io/FileDescriptor;", (void*)android_os_Parcel_readFileDescriptor},
    {"openFileDescriptor",  "(Ljava/lang/String;I)Ljava/io/FileDescriptor;", (void*)android_os_Parcel_openFileDescriptor},
    {"closeFileDescriptor", "(Ljava/io/FileDescriptor;)V", (void*)android_os_Parcel_closeFileDescriptor},
    {"freeBuffer",          "()V", (void*)android_os_Parcel_freeBuffer},
    {"init",                "(I)V", (void*)android_os_Parcel_init},
    {"destroy",             "()V", (void*)android_os_Parcel_destroy},
    {"marshall",            "()[B", (void*)android_os_Parcel_marshall},
    {"unmarshall",          "([BII)V", (void*)android_os_Parcel_unmarshall},
    {"appendFrom",          "(Landroid/os/Parcel;II)V", (void*)android_os_Parcel_appendFrom},
    {"hasFileDescriptors",  "()Z", (void*)android_os_Parcel_hasFileDescriptors},
    {"writeInterfaceToken", "(Ljava/lang/String;)V", (void*)android_os_Parcel_writeInterfaceToken},
    {"enforceInterface",    "(Ljava/lang/String;)V", (void*)android_os_Parcel_enforceInterface},
};

const char* const kParcelPathName = "android/os/Parcel";

static int int_register_android_os_Parcel(JNIEnv* env)
{
>>>>>>> 54b6cfa... Initial Contribution
    jclass clazz;

    clazz = env->FindClass("android/util/Log");
    LOG_FATAL_IF(clazz == NULL, "Unable to find class android.util.Log");
    gLogOffsets.mClass = (jclass) env->NewGlobalRef(clazz);
    gLogOffsets.mLogE = env->GetStaticMethodID(
        clazz, "e", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I");
    assert(gLogOffsets.mLogE);

<<<<<<< HEAD
=======
    clazz = env->FindClass("java/io/FileDescriptor");
    LOG_FATAL_IF(clazz == NULL, "Unable to find class java.io.FileDescriptor");
    gFileDescriptorOffsets.mClass = (jclass) env->NewGlobalRef(clazz);
    gFileDescriptorOffsets.mConstructor
        = env->GetMethodID(clazz, "<init>", "()V");
    gFileDescriptorOffsets.mDescriptor = env->GetFieldID(clazz, "descriptor", "I");
    LOG_FATAL_IF(gFileDescriptorOffsets.mDescriptor == NULL,
                 "Unable to find descriptor field in java.io.FileDescriptor");

>>>>>>> 54b6cfa... Initial Contribution
    clazz = env->FindClass("android/os/ParcelFileDescriptor");
    LOG_FATAL_IF(clazz == NULL, "Unable to find class android.os.ParcelFileDescriptor");
    gParcelFileDescriptorOffsets.mClass = (jclass) env->NewGlobalRef(clazz);
    gParcelFileDescriptorOffsets.mConstructor
        = env->GetMethodID(clazz, "<init>", "(Ljava/io/FileDescriptor;)V");

<<<<<<< HEAD
    clazz = env->FindClass("android/os/StrictMode");
    LOG_FATAL_IF(clazz == NULL, "Unable to find class android.os.StrictMode");
    gStrictModeCallbackOffsets.mClass = (jclass) env->NewGlobalRef(clazz);
    gStrictModeCallbackOffsets.mCallback = env->GetStaticMethodID(
        clazz, "onBinderStrictModePolicyChange", "(I)V");
    LOG_FATAL_IF(gStrictModeCallbackOffsets.mCallback == NULL,
                 "Unable to find strict mode callback.");

    return 0;
}
=======
    clazz = env->FindClass(kParcelPathName);
    LOG_FATAL_IF(clazz == NULL, "Unable to find class android.os.Parcel");

    gParcelOffsets.mObject
        = env->GetFieldID(clazz, "mObject", "I");
    gParcelOffsets.mOwnObject
        = env->GetFieldID(clazz, "mOwnObject", "I");

    return AndroidRuntime::registerNativeMethods(
        env, kParcelPathName,
        gParcelMethods, NELEM(gParcelMethods));
}

int register_android_os_Binder(JNIEnv* env)
{
    if (int_register_android_os_Binder(env) < 0)
        return -1;
    if (int_register_android_os_BinderInternal(env) < 0)
        return -1;
    if (int_register_android_os_BinderProxy(env) < 0)
        return -1;
    if (int_register_android_os_Parcel(env) < 0)
        return -1;
    return 0;
}

namespace android {

// Returns the Unix file descriptor for a ParcelFileDescriptor object
int getParcelFileDescriptorFD(JNIEnv* env, jobject object)
{
    return env->GetIntField(object, gFileDescriptorOffsets.mDescriptor);
}

}
>>>>>>> 54b6cfa... Initial Contribution
