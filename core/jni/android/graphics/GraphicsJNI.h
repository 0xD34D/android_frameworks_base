#ifndef GraphicsJNI_DEFINED
#define GraphicsJNI_DEFINED

<<<<<<< HEAD
#include "SkBitmap.h"
#include "SkDevice.h"
#include "SkPixelRef.h"
#include "SkMallocPixelRef.h"
#include "SkPoint.h"
#include "SkRect.h"
#include "../images/SkBitmapRegionDecoder.h"
#include "../images/SkImageDecoder.h"
=======
#include "SkPoint.h"
#include "SkRect.h"
#include "SkBitmap.h"
>>>>>>> 54b6cfa... Initial Contribution
#include <jni.h>

class SkCanvas;
class SkPaint;
class SkPicture;

class GraphicsJNI {
public:
    // returns true if an exception is set (and dumps it out to the Log)
    static bool hasException(JNIEnv*);

    static void get_jrect(JNIEnv*, jobject jrect, int* L, int* T, int* R, int* B);
    static void set_jrect(JNIEnv*, jobject jrect, int L, int T, int R, int B);

    static SkIRect* jrect_to_irect(JNIEnv*, jobject jrect, SkIRect*);
    static void irect_to_jrect(const SkIRect&, JNIEnv*, jobject jrect);

    static SkRect* jrectf_to_rect(JNIEnv*, jobject jrectf, SkRect*);
    static SkRect* jrect_to_rect(JNIEnv*, jobject jrect, SkRect*);
    static void rect_to_jrectf(const SkRect&, JNIEnv*, jobject jrectf);
<<<<<<< HEAD

    static void set_jpoint(JNIEnv*, jobject jrect, int x, int y);

    static SkIPoint* jpoint_to_ipoint(JNIEnv*, jobject jpoint, SkIPoint* point);
    static void ipoint_to_jpoint(const SkIPoint& point, JNIEnv*, jobject jpoint);

    static SkPoint* jpointf_to_point(JNIEnv*, jobject jpointf, SkPoint* point);
    static void point_to_jpointf(const SkPoint& point, JNIEnv*, jobject jpointf);

=======
    
    static void set_jpoint(JNIEnv*, jobject jrect, int x, int y);
    
    static SkIPoint* jpoint_to_ipoint(JNIEnv*, jobject jpoint, SkIPoint* point);
    static void ipoint_to_jpoint(const SkIPoint& point, JNIEnv*, jobject jpoint);
    
    static SkPoint* jpointf_to_point(JNIEnv*, jobject jpointf, SkPoint* point);
    static void point_to_jpointf(const SkPoint& point, JNIEnv*, jobject jpointf);
  
>>>>>>> 54b6cfa... Initial Contribution
    static SkCanvas* getNativeCanvas(JNIEnv*, jobject canvas);
    static SkPaint*  getNativePaint(JNIEnv*, jobject paint);
    static SkBitmap* getNativeBitmap(JNIEnv*, jobject bitmap);
    static SkPicture* getNativePicture(JNIEnv*, jobject picture);
    static SkRegion* getNativeRegion(JNIEnv*, jobject region);
<<<<<<< HEAD

=======
    
>>>>>>> 54b6cfa... Initial Contribution
    /** Return the corresponding native config from the java Config enum,
        or kNo_Config if the java object is null.
    */
    static SkBitmap::Config getNativeBitmapConfig(JNIEnv*, jobject jconfig);
<<<<<<< HEAD

    /** Create a java Bitmap object given the native bitmap (required) and optional
        storage array (may be null).
    */
    static jobject createBitmap(JNIEnv* env, SkBitmap* bitmap, jbyteArray buffer,
                                bool isMutable, jbyteArray ninepatch, jintArray layoutbounds,
                                int density = -1);

    static jobject createBitmap(JNIEnv* env, SkBitmap* bitmap, bool isMutable,
                                jbyteArray ninepatch, int density = -1);

    static jobject createRegion(JNIEnv* env, SkRegion* region);

    static jobject createBitmapRegionDecoder(JNIEnv* env, SkBitmapRegionDecoder* bitmap);

    static jbyteArray allocateJavaPixelRef(JNIEnv* env, SkBitmap* bitmap,
                                     SkColorTable* ctable);
=======
    
    /** Create a java Bitmap object given the native bitmap (required) and optional
        storage array (may be null). If storage is specified, then it must already be
        locked, and its native address set as the bitmap's pixels. If storage is null,
        then the bitmap must be an owner of its natively allocated pixels (via allocPixels).
        */
    static jobject createBitmap(JNIEnv* env, SkBitmap* bitmap, bool isMutable,
                                jbyteArray ninePatch);
    
    static jobject createRegion(JNIEnv* env, SkRegion* region);

    /** Set a pixelref for the bitmap (needs setConfig to already be called)
        Returns true on success. If it returns false, then it failed, and the
        appropriate exception will have been raised.
    */
    static bool setJavaPixelRef(JNIEnv*, SkBitmap*, SkColorTable* ctable);
>>>>>>> 54b6cfa... Initial Contribution

    /** Copy the colors in colors[] to the bitmap, convert to the correct
        format along the way.
    */
    static bool SetPixels(JNIEnv* env, jintArray colors, int srcOffset,
                          int srcStride, int x, int y, int width, int height,
                          const SkBitmap& dstBitmap);
<<<<<<< HEAD

    static jbyteArray getBitmapStorageObj(SkPixelRef *pixref);
};

class AndroidPixelRef : public SkMallocPixelRef {
public:
    AndroidPixelRef(JNIEnv* env, void* storage, size_t size, jbyteArray storageObj,
                    SkColorTable* ctable);

    virtual ~AndroidPixelRef();

    jbyteArray getStorageObj() { return fStorageObj; }

    void setLocalJNIRef(jbyteArray arr);

    /** Used to hold a ref to the pixels when the Java bitmap may be collected.
     *  If specified, 'localref' is a valid JNI local reference to the byte array
     *  containing the pixel data.
     *
     *  'localref' may only be NULL if setLocalJNIRef() was already called with
     *  a JNI local ref that is still valid.
     */
    virtual void globalRef(void* localref=NULL);

    /** Release a ref that was acquired using globalRef(). */
    virtual void globalUnref();

private:
    JavaVM* fVM;
    bool fOnJavaHeap; // If true, the memory was allocated on the Java heap

    jbyteArray fStorageObj; // The Java byte[] object used as the bitmap backing store
    bool fHasGlobalRef; // If true, fStorageObj holds a JNI global ref

    mutable int32_t fGlobalRefCnt;
};

/** A helper class for accessing Java-heap-allocated bitmaps.
 *  This should be used when calling into a JNI method that retains a
 *  reference to the bitmap longer than the lifetime of the Java Bitmap.
 *
 *  After creating an instance of this class, a call to
 *  AndroidPixelRef::globalRef() will allocate a JNI global reference
 *  to the backing buffer object.
 */
class JavaHeapBitmapRef {
public:

    JavaHeapBitmapRef(JNIEnv *env, SkBitmap* nativeBitmap, jbyteArray buffer);
    ~JavaHeapBitmapRef();

private:
    JNIEnv* fEnv;
    SkBitmap* fNativeBitmap;
    jbyteArray fBuffer;
};

/** Allocator which allocates the backing buffer in the Java heap.
 *  Instances can only be used to perform a single allocation, which helps
 *  ensure that the allocated buffer is properly accounted for with a
 *  reference in the heap (or a JNI global reference).
 */
=======
};

>>>>>>> 54b6cfa... Initial Contribution
class JavaPixelAllocator : public SkBitmap::Allocator {
public:
    JavaPixelAllocator(JNIEnv* env);
    // overrides
    virtual bool allocPixelRef(SkBitmap* bitmap, SkColorTable* ctable);
<<<<<<< HEAD

    /** Return the Java array object created for the last allocation.
     *  This returns a local JNI reference which the caller is responsible
     *  for storing appropriately (usually by passing it to the Bitmap
     *  constructor).
     */
    jbyteArray getStorageObj() { return fStorageObj; }

    /** Same as getStorageObj(), but also resets the allocator so that it
     *  can allocate again.
     */
    jbyteArray getStorageObjAndReset() {
        jbyteArray result = fStorageObj;
        fStorageObj = NULL;
        fAllocCount = 0;
        return result;
    };

private:
    JavaVM* fVM;
    bool fAllocateInJavaHeap;
    jbyteArray fStorageObj;
    int fAllocCount;
};

enum JNIAccess {
    kRO_JNIAccess,
    kRW_JNIAccess
=======
    
private:
    JNIEnv* fEnv;
>>>>>>> 54b6cfa... Initial Contribution
};

class AutoJavaFloatArray {
public:
<<<<<<< HEAD
    AutoJavaFloatArray(JNIEnv* env, jfloatArray array,
                       int minLength = 0, JNIAccess = kRW_JNIAccess);
    ~AutoJavaFloatArray();

    float* ptr() const { return fPtr; }
    int    length() const { return fLen; }

=======
    AutoJavaFloatArray(JNIEnv* env, jfloatArray array, int minLength = 0);
    ~AutoJavaFloatArray();
    
    float* ptr() const { return fPtr; }
    int    length() const { return fLen; }
    
>>>>>>> 54b6cfa... Initial Contribution
private:
    JNIEnv*     fEnv;
    jfloatArray fArray;
    float*      fPtr;
    int         fLen;
<<<<<<< HEAD
    int         fReleaseMode;
=======
>>>>>>> 54b6cfa... Initial Contribution
};

class AutoJavaIntArray {
public:
    AutoJavaIntArray(JNIEnv* env, jintArray array, int minLength = 0);
    ~AutoJavaIntArray();
<<<<<<< HEAD

    jint* ptr() const { return fPtr; }
    int    length() const { return fLen; }

=======
    
    jint* ptr() const { return fPtr; }
    int    length() const { return fLen; }
    
>>>>>>> 54b6cfa... Initial Contribution
private:
    JNIEnv*     fEnv;
    jintArray fArray;
    jint*      fPtr;
    int         fLen;
};

class AutoJavaShortArray {
public:
<<<<<<< HEAD
    AutoJavaShortArray(JNIEnv* env, jshortArray array,
                       int minLength = 0, JNIAccess = kRW_JNIAccess);
    ~AutoJavaShortArray();

    jshort* ptr() const { return fPtr; }
    int    length() const { return fLen; }

=======
    AutoJavaShortArray(JNIEnv* env, jshortArray array, int minLength = 0);
    ~AutoJavaShortArray();
    
    jshort* ptr() const { return fPtr; }
    int    length() const { return fLen; }
    
>>>>>>> 54b6cfa... Initial Contribution
private:
    JNIEnv*     fEnv;
    jshortArray fArray;
    jshort*      fPtr;
    int         fLen;
<<<<<<< HEAD
    int         fReleaseMode;
=======
>>>>>>> 54b6cfa... Initial Contribution
};

class AutoJavaByteArray {
public:
    AutoJavaByteArray(JNIEnv* env, jbyteArray array, int minLength = 0);
    ~AutoJavaByteArray();
<<<<<<< HEAD

    jbyte* ptr() const { return fPtr; }
    int    length() const { return fLen; }

=======
    
    jbyte* ptr() const { return fPtr; }
    int    length() const { return fLen; }
    
>>>>>>> 54b6cfa... Initial Contribution
private:
    JNIEnv*     fEnv;
    jbyteArray fArray;
    jbyte*      fPtr;
    int         fLen;
};

<<<<<<< HEAD
=======
void doThrow(JNIEnv* env, const char* exc, const char* msg = NULL);
>>>>>>> 54b6cfa... Initial Contribution
void doThrowNPE(JNIEnv* env);
void doThrowAIOOBE(JNIEnv* env); // Array Index Out Of Bounds Exception
void doThrowIAE(JNIEnv* env, const char* msg = NULL);   // Illegal Argument
void doThrowRE(JNIEnv* env, const char* msg = NULL);   // Runtime
void doThrowISE(JNIEnv* env, const char* msg = NULL);   // Illegal State
void doThrowOOME(JNIEnv* env, const char* msg = NULL);   // Out of memory
<<<<<<< HEAD
void doThrowIOE(JNIEnv* env, const char* msg = NULL);   // IO Exception
=======
>>>>>>> 54b6cfa... Initial Contribution

#define NPE_CHECK_RETURN_ZERO(env, object)    \
    do { if (NULL == (object)) { doThrowNPE(env); return 0; } } while (0)

#define NPE_CHECK_RETURN_VOID(env, object)    \
    do { if (NULL == (object)) { doThrowNPE(env); return; } } while (0)

#endif
<<<<<<< HEAD
=======

>>>>>>> 54b6cfa... Initial Contribution
