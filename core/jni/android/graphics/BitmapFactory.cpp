<<<<<<< HEAD
#define LOG_TAG "BitmapFactory"

#include "BitmapFactory.h"
#include "NinePatchPeeker.h"
#include "SkImageDecoder.h"
#include "SkImageRef_ashmem.h"
#include "SkImageRef_GlobalPool.h"
#include "SkPixelRef.h"
#include "SkStream.h"
#include "SkTemplates.h"
#include "SkUtils.h"
#include "CreateJavaOutputStreamAdaptor.h"
#include "AutoDecodeCancel.h"
#include "Utils.h"
#include "JNIHelp.h"

#include <android_runtime/AndroidRuntime.h>
#include <androidfw/Asset.h>
#include <androidfw/ResourceTypes.h>
#include <netinet/in.h>
#include <sys/mman.h>
#include <sys/stat.h>

jfieldID gOptions_justBoundsFieldID;
jfieldID gOptions_sampleSizeFieldID;
jfieldID gOptions_configFieldID;
jfieldID gOptions_mutableFieldID;
jfieldID gOptions_ditherFieldID;
jfieldID gOptions_purgeableFieldID;
jfieldID gOptions_shareableFieldID;
jfieldID gOptions_preferQualityOverSpeedFieldID;
jfieldID gOptions_widthFieldID;
jfieldID gOptions_heightFieldID;
jfieldID gOptions_mimeFieldID;
jfieldID gOptions_mCancelID;
jfieldID gOptions_bitmapFieldID;
jfieldID gBitmap_nativeBitmapFieldID;
jfieldID gBitmap_layoutBoundsFieldID;
=======
#include "SkImageDecoder.h"
#include "SkPixelRef.h"
#include "SkStream.h"
#include "GraphicsJNI.h"
#include "SkTemplates.h"
#include "SkUtils.h"
#include "CreateJavaOutputStreamAdaptor.h"

#include <android_runtime/AndroidRuntime.h>
#include <utils/Asset.h>
#include <utils/ResourceTypes.h>
#include <netinet/in.h>
#include <sys/mman.h>

static jclass gOptions_class;
static jfieldID gOptions_justBoundsFieldID;
static jfieldID gOptions_sampleSizeFieldID;
static jfieldID gOptions_configFieldID;
static jfieldID gOptions_ditherFieldID;
static jfieldID gOptions_widthFieldID;
static jfieldID gOptions_heightFieldID;
static jfieldID gOptions_mimeFieldID;

static jclass gFileDescriptor_class;
static jfieldID gFileDescriptor_descriptor;
>>>>>>> 54b6cfa... Initial Contribution

#if 0
    #define TRACE_BITMAP(code)  code
#else
    #define TRACE_BITMAP(code)
#endif

<<<<<<< HEAD
using namespace android;

=======
//#define MIN_SIZE_TO_USE_MMAP    (4*1024)

///////////////////////////////////////////////////////////////////////////////

class AutoDecoderCancel {
public:
    AutoDecoderCancel(jobject options, SkImageDecoder* decoder);
    ~AutoDecoderCancel();

    static bool RequestCancel(jobject options);
    
private:
    AutoDecoderCancel*  fNext;
    jobject             fJOptions;  // java options object
    SkImageDecoder*     fDecoder;
};

static SkMutex  gAutoDecoderCancelMutex;
static AutoDecoderCancel* gAutoDecoderCancel;

AutoDecoderCancel::AutoDecoderCancel(jobject joptions,
                                       SkImageDecoder* decoder) {
    fJOptions = joptions;
    fDecoder = decoder;

    // only need to be in the list if we have options
    if (NULL != joptions) {
        SkAutoMutexAcquire ac(gAutoDecoderCancelMutex);
        
        fNext = gAutoDecoderCancel;
        gAutoDecoderCancel = this;
    }
}

AutoDecoderCancel::~AutoDecoderCancel() {
    const jobject joptions = fJOptions;
    
    if (NULL != joptions) {
        SkAutoMutexAcquire ac(gAutoDecoderCancelMutex);
        
        // remove us
        AutoDecoderCancel* pair = gAutoDecoderCancel;
        AutoDecoderCancel* prev = NULL;
        while (pair != NULL) {
            AutoDecoderCancel* next = pair->fNext;
            if (pair->fJOptions == joptions) {
                SkASSERT(pair->fDecoder == fDecoder);
                if (prev) {
                    prev->fNext = next;
                } else {
                    gAutoDecoderCancel = next;
                }
                return;
            }
            pair = next;
        }
        SkDebugf("xxxxxxxxxxxxxxxxxxxxxxx not found in pair list %p %p\n",
                 fJOptions, fDecoder);
    }
}

bool AutoDecoderCancel::RequestCancel(jobject joptions) {
    SkAutoMutexAcquire ac(gAutoDecoderCancelMutex);

    AutoDecoderCancel* pair = gAutoDecoderCancel;
    while (pair != NULL) {
        if (pair->fJOptions == joptions) {
            pair->fDecoder->cancelDecode();
            return true;
        }
        pair = pair->fNext;
    }
    return false;
}

///////////////////////////////////////////////////////////////////////////////

using namespace android;

class NinePatchPeeker : public SkImageDecoder::Peeker {
public:
    NinePatchPeeker() {
        fPatchIsValid = false;
    }

    ~NinePatchPeeker() {
        if (fPatchIsValid) {
            free(fPatch);
        }
    }

    bool    fPatchIsValid;
    Res_png_9patch*  fPatch;

    virtual bool peek(const char tag[], const void* data, size_t length) {
        if (strcmp("npTc", tag) == 0 && length >= sizeof(Res_png_9patch)) {
            Res_png_9patch* patch = (Res_png_9patch*) data;
            size_t patchSize = patch->serializedSize();
            assert(length == patchSize);
            // You have to copy the data because it is owned by the png reader
            Res_png_9patch* patchNew = (Res_png_9patch*) malloc(patchSize);
            memcpy(patchNew, patch, patchSize);
            Res_png_9patch::deserialize(patchNew);
            patchNew->fileToDevice();
            if (fPatchIsValid) {
                free(fPatch);
            }
            fPatch = patchNew;
            //printf("9patch: (%d,%d)-(%d,%d)\n",
            //       fPatch.sizeLeft, fPatch.sizeTop,
            //       fPatch.sizeRight, fPatch.sizeBottom);
            fPatchIsValid = true;
        } else {
            fPatch = NULL;
        }
        return true;    // keep on decoding
    }
};

class AssetStreamAdaptor : public SkStream {
public:
    AssetStreamAdaptor(Asset* a) : fAsset(a) {}
    
	virtual bool rewind() {
        off_t pos = fAsset->seek(0, SEEK_SET);
        return pos != (off_t)-1;
    }
    
	virtual size_t read(void* buffer, size_t size) {
        ssize_t amount;
        
        if (NULL == buffer) {
            if (0 == size) {  // caller is asking us for our total length
                return fAsset->getLength();
            }
            // asset->seek returns new total offset
            // we want to return amount that was skipped

            off_t oldOffset = fAsset->seek(0, SEEK_CUR);
            if (-1 == oldOffset) {
                return 0;
            }
            off_t newOffset = fAsset->seek(size, SEEK_CUR);
            if (-1 == newOffset) {
                return 0;
            }
            amount = newOffset - oldOffset;
        } else {
            amount = fAsset->read(buffer, size);
        }
        
        if (amount < 0) {
            amount = 0;
        }
        return amount;
    }
    
private:
    Asset*  fAsset;
};

///////////////////////////////////////////////////////////////////////////////

>>>>>>> 54b6cfa... Initial Contribution
static inline int32_t validOrNeg1(bool isValid, int32_t value) {
//    return isValid ? value : -1;
    SkASSERT((int)isValid == 0 || (int)isValid == 1);
    return ((int32_t)isValid - 1) | value;
}

<<<<<<< HEAD
jstring getMimeTypeString(JNIEnv* env, SkImageDecoder::Format format) {
=======
static jstring getMimeTypeString(JNIEnv* env, SkImageDecoder::Format format) {
>>>>>>> 54b6cfa... Initial Contribution
    static const struct {
        SkImageDecoder::Format fFormat;
        const char*            fMimeType;
    } gMimeTypes[] = {
        { SkImageDecoder::kBMP_Format,  "image/bmp" },
        { SkImageDecoder::kGIF_Format,  "image/gif" },
        { SkImageDecoder::kICO_Format,  "image/x-ico" },
        { SkImageDecoder::kJPEG_Format, "image/jpeg" },
        { SkImageDecoder::kPNG_Format,  "image/png" },
        { SkImageDecoder::kWBMP_Format, "image/vnd.wap.wbmp" }
    };
<<<<<<< HEAD

=======
    
>>>>>>> 54b6cfa... Initial Contribution
    const char* cstr = NULL;
    for (size_t i = 0; i < SK_ARRAY_COUNT(gMimeTypes); i++) {
        if (gMimeTypes[i].fFormat == format) {
            cstr = gMimeTypes[i].fMimeType;
            break;
        }
    }

    jstring jstr = 0;
    if (NULL != cstr) {
        jstr = env->NewStringUTF(cstr);
    }
    return jstr;
}

<<<<<<< HEAD
static bool optionsPurgeable(JNIEnv* env, jobject options) {
    return options != NULL && env->GetBooleanField(options, gOptions_purgeableFieldID);
}

static bool optionsShareable(JNIEnv* env, jobject options) {
    return options != NULL && env->GetBooleanField(options, gOptions_shareableFieldID);
}

static bool optionsJustBounds(JNIEnv* env, jobject options) {
    return options != NULL && env->GetBooleanField(options, gOptions_justBoundsFieldID);
}

static void scaleNinePatchChunk(android::Res_png_9patch* chunk, float scale) {
    chunk->paddingLeft = int(chunk->paddingLeft * scale + 0.5f);
    chunk->paddingTop = int(chunk->paddingTop * scale + 0.5f);
    chunk->paddingRight = int(chunk->paddingRight * scale + 0.5f);
    chunk->paddingBottom = int(chunk->paddingBottom * scale + 0.5f);

    for (int i = 0; i < chunk->numXDivs; i++) {
        chunk->xDivs[i] = int(chunk->xDivs[i] * scale + 0.5f);
        if (i > 0 && chunk->xDivs[i] == chunk->xDivs[i - 1]) {
            chunk->xDivs[i]++;
        }
    }

    for (int i = 0; i < chunk->numYDivs; i++) {
        chunk->yDivs[i] = int(chunk->yDivs[i] * scale + 0.5f);
        if (i > 0 && chunk->yDivs[i] == chunk->yDivs[i - 1]) {
            chunk->yDivs[i]++;
        }
    }
}

static jbyteArray nativeScaleNinePatch(JNIEnv* env, jobject, jbyteArray chunkObject, jfloat scale,
        jobject padding) {

    jbyte* array = env->GetByteArrayElements(chunkObject, 0);
    if (array != NULL) {
        size_t chunkSize = env->GetArrayLength(chunkObject);
        void* storage = alloca(chunkSize);
        android::Res_png_9patch* chunk = static_cast<android::Res_png_9patch*>(storage);
        memcpy(chunk, array, chunkSize);
        android::Res_png_9patch::deserialize(chunk);

        scaleNinePatchChunk(chunk, scale);
        memcpy(array, chunk, chunkSize);

        if (padding) {
            GraphicsJNI::set_jrect(env, padding, chunk->paddingLeft, chunk->paddingTop,
                    chunk->paddingRight, chunk->paddingBottom);
        }

        env->ReleaseByteArrayElements(chunkObject, array, 0);
    }
    return chunkObject;
}

static SkPixelRef* installPixelRef(SkBitmap* bitmap, SkStream* stream,
        int sampleSize, bool ditherImage) {

    SkImageRef* pr;
    // only use ashmem for large images, since mmaps come at a price
    if (bitmap->getSize() >= 32 * 1024) {
        pr = new SkImageRef_ashmem(stream, bitmap->config(), sampleSize);
    } else {
        pr = new SkImageRef_GlobalPool(stream, bitmap->config(), sampleSize);
    }
    pr->setDitherImage(ditherImage);
    bitmap->setPixelRef(pr)->unref();
    pr->isOpaque(bitmap);
    return pr;
}

// since we "may" create a purgeable imageref, we require the stream be ref'able
// i.e. dynamically allocated, since its lifetime may exceed the current stack
// frame.
static jobject doDecode(JNIEnv* env, SkStream* stream, jobject padding,
        jobject options, bool allowPurgeable, bool forcePurgeable = false,
        bool applyScale = false, float scale = 1.0f) {

    int sampleSize = 1;

    SkImageDecoder::Mode mode = SkImageDecoder::kDecodePixels_Mode;
    SkBitmap::Config prefConfig = SkBitmap::kARGB_8888_Config;

    bool doDither = true;
    bool isMutable = false;
    bool willScale = applyScale && scale != 1.0f;
    bool isPurgeable = !willScale &&
            (forcePurgeable || (allowPurgeable && optionsPurgeable(env, options)));
    bool preferQualityOverSpeed = false;

    jobject javaBitmap = NULL;

    if (options != NULL) {
        sampleSize = env->GetIntField(options, gOptions_sampleSizeFieldID);
        if (optionsJustBounds(env, options)) {
            mode = SkImageDecoder::kDecodeBounds_Mode;
        }

=======
static jobject doDecode(JNIEnv* env, SkStream* stream, jobject padding,
                        jobject options) {

    int sampleSize = 1;
    SkImageDecoder::Mode mode = SkImageDecoder::kDecodePixels_Mode;
    SkBitmap::Config prefConfig = SkBitmap::kNo_Config;
    bool doDither = true;
    
    if (NULL != options) {
        sampleSize = env->GetIntField(options, gOptions_sampleSizeFieldID);
        if (env->GetBooleanField(options, gOptions_justBoundsFieldID)) {
            mode = SkImageDecoder::kDecodeBounds_Mode;
        }
>>>>>>> 54b6cfa... Initial Contribution
        // initialize these, in case we fail later on
        env->SetIntField(options, gOptions_widthFieldID, -1);
        env->SetIntField(options, gOptions_heightFieldID, -1);
        env->SetObjectField(options, gOptions_mimeFieldID, 0);
<<<<<<< HEAD

        jobject jconfig = env->GetObjectField(options, gOptions_configFieldID);
        prefConfig = GraphicsJNI::getNativeBitmapConfig(env, jconfig);
        isMutable = env->GetBooleanField(options, gOptions_mutableFieldID);
        doDither = env->GetBooleanField(options, gOptions_ditherFieldID);
        preferQualityOverSpeed = env->GetBooleanField(options,
                gOptions_preferQualityOverSpeedFieldID);
        javaBitmap = env->GetObjectField(options, gOptions_bitmapFieldID);
    }

    if (willScale && javaBitmap != NULL) {
        return nullObjectReturn("Cannot pre-scale a reused bitmap");
    }

    SkImageDecoder* decoder = SkImageDecoder::Factory(stream);
    if (decoder == NULL) {
        return nullObjectReturn("SkImageDecoder::Factory returned null");
    }

    decoder->setSampleSize(sampleSize);
    decoder->setDitherImage(doDither);
    decoder->setPreferQualityOverSpeed(preferQualityOverSpeed);

    NinePatchPeeker peeker(decoder);
    JavaPixelAllocator javaAllocator(env);

    SkBitmap* bitmap;
    if (javaBitmap == NULL) {
        bitmap = new SkBitmap;
    } else {
        if (sampleSize != 1) {
            return nullObjectReturn("SkImageDecoder: Cannot reuse bitmap with sampleSize != 1");
        }
        bitmap = (SkBitmap*) env->GetIntField(javaBitmap, gBitmap_nativeBitmapFieldID);
        // config of supplied bitmap overrules config set in options
        prefConfig = bitmap->getConfig();
    }

    SkAutoTDelete<SkImageDecoder> add(decoder);
    SkAutoTDelete<SkBitmap> adb(bitmap, javaBitmap == NULL);

    decoder->setPeeker(&peeker);
    if (!isPurgeable) {
        decoder->setAllocator(&javaAllocator);
    }

    AutoDecoderCancel adc(options, decoder);

    // To fix the race condition in case "requestCancelDecode"
    // happens earlier than AutoDecoderCancel object is added
    // to the gAutoDecoderCancelMutex linked list.
    if (options != NULL && env->GetBooleanField(options, gOptions_mCancelID)) {
        return nullObjectReturn("gOptions_mCancelID");
    }

    SkImageDecoder::Mode decodeMode = mode;
    if (isPurgeable) {
        decodeMode = SkImageDecoder::kDecodeBounds_Mode;
    }

    SkBitmap* decoded;
    if (willScale) {
        decoded = new SkBitmap;
    } else {
        decoded = bitmap;
    }
    SkAutoTDelete<SkBitmap> adb2(willScale ? decoded : NULL);

    if (!decoder->decode(stream, decoded, prefConfig, decodeMode, javaBitmap != NULL)) {
        return nullObjectReturn("decoder->decode returned false");
    }

    int scaledWidth = decoded->width();
    int scaledHeight = decoded->height();

    if (willScale && mode != SkImageDecoder::kDecodeBounds_Mode) {
        scaledWidth = int(scaledWidth * scale + 0.5f);
        scaledHeight = int(scaledHeight * scale + 0.5f);
    }

    // update options (if any)
    if (options != NULL) {
        env->SetIntField(options, gOptions_widthFieldID, scaledWidth);
        env->SetIntField(options, gOptions_heightFieldID, scaledHeight);
        env->SetObjectField(options, gOptions_mimeFieldID,
                getMimeTypeString(env, decoder->getFormat()));
    }

    // if we're in justBounds mode, return now (skip the java bitmap)
    if (mode == SkImageDecoder::kDecodeBounds_Mode) {
=======
        
        jobject jconfig = env->GetObjectField(options, gOptions_configFieldID);
        prefConfig = GraphicsJNI::getNativeBitmapConfig(env, jconfig);
        doDither = env->GetBooleanField(options, gOptions_ditherFieldID);
    }

    SkImageDecoder* decoder = SkImageDecoder::Factory(stream);
    if (NULL == decoder) {
        return NULL;
    }
    
    decoder->setSampleSize(sampleSize);
    decoder->setDitherImage(doDither);

    NinePatchPeeker     peeker;
    JavaPixelAllocator  allocator(env);
    SkBitmap*           bitmap = new SkBitmap;
    Res_png_9patch      dummy9Patch;

    SkAutoTDelete<SkImageDecoder>   add(decoder);
    SkAutoTDelete<SkBitmap>         adb(bitmap);

    decoder->setPeeker(&peeker);
    decoder->setAllocator(&allocator);
    
    AutoDecoderCancel   adc(options, decoder);

    if (!decoder->decode(stream, bitmap, prefConfig, mode)) {
        return NULL;
    }
    
    // update options (if any)
    if (NULL != options) {
        env->SetIntField(options, gOptions_widthFieldID, bitmap->width());
        env->SetIntField(options, gOptions_heightFieldID, bitmap->height());
        // TODO: set the mimeType field with the data from the codec.
        // but how to reuse a set of strings, rather than allocating new one
        // each time?
        env->SetObjectField(options, gOptions_mimeFieldID,
                            getMimeTypeString(env, decoder->getFormat()));
    }
    
    // if we're in justBounds mode, return now (skip the java bitmap)
    if (SkImageDecoder::kDecodeBounds_Mode == mode) {
>>>>>>> 54b6cfa... Initial Contribution
        return NULL;
    }

    jbyteArray ninePatchChunk = NULL;
<<<<<<< HEAD
    if (peeker.fPatch != NULL) {
        if (willScale) {
            scaleNinePatchChunk(peeker.fPatch, scale);
        }

        size_t ninePatchArraySize = peeker.fPatch->serializedSize();
        ninePatchChunk = env->NewByteArray(ninePatchArraySize);
        if (ninePatchChunk == NULL) {
            return nullObjectReturn("ninePatchChunk == null");
        }

        jbyte* array = (jbyte*) env->GetPrimitiveArrayCritical(ninePatchChunk, NULL);
        if (array == NULL) {
            return nullObjectReturn("primitive array == null");
        }

=======
    if (peeker.fPatchIsValid) {
        size_t ninePatchArraySize = peeker.fPatch->serializedSize();
        ninePatchChunk = env->NewByteArray(ninePatchArraySize);
        if (NULL == ninePatchChunk) {
            return NULL;
        }
        jbyte* array = (jbyte*)env->GetPrimitiveArrayCritical(ninePatchChunk,
                                                              NULL);
        if (NULL == array) {
            return NULL;
        }
>>>>>>> 54b6cfa... Initial Contribution
        peeker.fPatch->serialize(array);
        env->ReleasePrimitiveArrayCritical(ninePatchChunk, array, 0);
    }

<<<<<<< HEAD
    jintArray layoutBounds = NULL;
    if (peeker.fLayoutBounds != NULL) {
        layoutBounds = env->NewIntArray(4);
        if (layoutBounds == NULL) {
            return nullObjectReturn("layoutBounds == null");
        }

        jint scaledBounds[4];
        if (willScale) {
            for (int i=0; i<4; i++) {
                scaledBounds[i] = (jint)((((jint*)peeker.fLayoutBounds)[i]*scale) + .5f);
            }
        } else {
            memcpy(scaledBounds, (jint*)peeker.fLayoutBounds, sizeof(scaledBounds));
        }
        env->SetIntArrayRegion(layoutBounds, 0, 4, scaledBounds);
        if (javaBitmap != NULL) {
            env->SetObjectField(javaBitmap, gBitmap_layoutBoundsFieldID, layoutBounds);
        }
    }

    if (willScale) {
        // This is weird so let me explain: we could use the scale parameter
        // directly, but for historical reasons this is how the corresponding
        // Dalvik code has always behaved. We simply recreate the behavior here.
        // The result is slightly different from simply using scale because of
        // the 0.5f rounding bias applied when computing the target image size
        const float sx = scaledWidth / float(decoded->width());
        const float sy = scaledHeight / float(decoded->height());

        SkBitmap::Config config = decoded->config();
        switch (config) {
            case SkBitmap::kNo_Config:
            case SkBitmap::kIndex8_Config:
            case SkBitmap::kRLE_Index8_Config:
                config = SkBitmap::kARGB_8888_Config;
                break;
            default:
                break;
        }

        bitmap->setConfig(config, scaledWidth, scaledHeight);
        bitmap->setIsOpaque(decoded->isOpaque());
        bitmap->allocPixels(&javaAllocator, NULL);
        bitmap->eraseColor(0);

        SkPaint paint;
        paint.setFilterBitmap(true);

        SkCanvas canvas(*bitmap);
        canvas.scale(sx, sy);
        canvas.drawBitmap(*decoded, 0.0f, 0.0f, &paint);
    }

    if (padding) {
        if (peeker.fPatch != NULL) {
            GraphicsJNI::set_jrect(env, padding,
                    peeker.fPatch->paddingLeft, peeker.fPatch->paddingTop,
                    peeker.fPatch->paddingRight, peeker.fPatch->paddingBottom);
=======
    // detach bitmap from its autotdeleter, since we want to own it now
    adb.detach();

    if (padding) {
        if (peeker.fPatchIsValid) {
            GraphicsJNI::set_jrect(env, padding,
                                   peeker.fPatch->paddingLeft,
                                   peeker.fPatch->paddingTop,
                                   peeker.fPatch->paddingRight,
                                   peeker.fPatch->paddingBottom);
>>>>>>> 54b6cfa... Initial Contribution
        } else {
            GraphicsJNI::set_jrect(env, padding, -1, -1, -1, -1);
        }
    }
<<<<<<< HEAD

    SkPixelRef* pr;
    if (isPurgeable) {
        pr = installPixelRef(bitmap, stream, sampleSize, doDither);
    } else {
        // if we get here, we're in kDecodePixels_Mode and will therefore
        // already have a pixelref installed.
        pr = bitmap->pixelRef();
    }
    if (pr == NULL) {
        return nullObjectReturn("Got null SkPixelRef");
    }

    if (!isMutable) {
        // promise we will never change our pixels (great for sharing and pictures)
        pr->setImmutable();
    }

    // detach bitmap from its autodeleter, since we want to own it now
    adb.detach();

    if (javaBitmap != NULL) {
        // If a java bitmap was passed in for reuse, pass it back
        return javaBitmap;
    }
    // now create the java bitmap
    return GraphicsJNI::createBitmap(env, bitmap, javaAllocator.getStorageObj(),
            isMutable, ninePatchChunk, layoutBounds, -1);
}

static jobject nativeDecodeStreamScaled(JNIEnv* env, jobject clazz, jobject is, jbyteArray storage,
        jobject padding, jobject options, jboolean applyScale, jfloat scale) {

    jobject bitmap = NULL;
    SkStream* stream = CreateJavaInputStreamAdaptor(env, is, storage, 0);

    if (stream) {
        // for now we don't allow purgeable with java inputstreams
        bitmap = doDecode(env, stream, padding, options, false, false, applyScale, scale);
=======
    
    // promise we will never change our pixels (great for sharing and pictures)
    SkPixelRef* ref = bitmap->pixelRef();
    SkASSERT(ref);
    ref->setImmutable();

    return GraphicsJNI::createBitmap(env, bitmap, false, ninePatchChunk);
}

static jobject nativeDecodeStream(JNIEnv* env, jobject clazz,
                                  jobject is,       // InputStream
                                  jbyteArray storage,   // byte[]
                                  jobject padding,
                                  jobject options) {  // BitmapFactory$Options
    jobject bitmap = NULL;
    SkStream* stream = CreateJavaInputStreamAdaptor(env, is, storage);

    if (stream) {
        bitmap = doDecode(env, stream, padding, options);
>>>>>>> 54b6cfa... Initial Contribution
        stream->unref();
    }
    return bitmap;
}

<<<<<<< HEAD
static jobject nativeDecodeStream(JNIEnv* env, jobject clazz, jobject is, jbyteArray storage,
        jobject padding, jobject options) {

    return nativeDecodeStreamScaled(env, clazz, is, storage, padding, options, false, 1.0f);
}

static ssize_t getFDSize(int fd) {
    off64_t curr = ::lseek64(fd, 0, SEEK_CUR);
=======
static ssize_t getFDSize(int fd) {
    off_t curr = ::lseek(fd, 0, SEEK_CUR);
>>>>>>> 54b6cfa... Initial Contribution
    if (curr < 0) {
        return 0;
    }
    size_t size = ::lseek(fd, 0, SEEK_END);
<<<<<<< HEAD
    ::lseek64(fd, curr, SEEK_SET);
    return size;
}

static jobject nativeDecodeFileDescriptor(JNIEnv* env, jobject clazz, jobject fileDescriptor,
        jobject padding, jobject bitmapFactoryOptions) {

    NPE_CHECK_RETURN_ZERO(env, fileDescriptor);

    jint descriptor = jniGetFDFromFileDescriptor(env, fileDescriptor);

    bool isPurgeable = optionsPurgeable(env, bitmapFactoryOptions);
    bool isShareable = optionsShareable(env, bitmapFactoryOptions);
    bool weOwnTheFD = false;
    if (isPurgeable && isShareable) {
        int newFD = ::dup(descriptor);
        if (-1 != newFD) {
            weOwnTheFD = true;
            descriptor = newFD;
        }
    }

    SkFDStream* stream = new SkFDStream(descriptor, weOwnTheFD);
    SkAutoUnref aur(stream);
    if (!stream->isValid()) {
        return NULL;
    }

    /* Restore our offset when we leave, so we can be called more than once
       with the same descriptor. This is only required if we didn't dup the
       file descriptor, but it is OK to do it all the time.
    */
    AutoFDSeek as(descriptor);

    /* Allow purgeable iff we own the FD, i.e., in the puregeable and
       shareable case.
    */
    return doDecode(env, stream, padding, bitmapFactoryOptions, weOwnTheFD);
}

/*  make a deep copy of the asset, and return it as a stream, or NULL if there
    was an error.
 */
static SkStream* copyAssetToStream(Asset* asset) {
    // if we could "ref/reopen" the asset, we may not need to copy it here
    off64_t size = asset->seek(0, SEEK_SET);
    if ((off64_t)-1 == size) {
        SkDebugf("---- copyAsset: asset rewind failed\n");
        return NULL;
    }

    size = asset->getLength();
    if (size <= 0) {
        SkDebugf("---- copyAsset: asset->getLength() returned %d\n", size);
        return NULL;
    }

    SkStream* stream = new SkMemoryStream(size);
    void* data = const_cast<void*>(stream->getMemoryBase());
    off64_t len = asset->read(data, size);
    if (len != size) {
        SkDebugf("---- copyAsset: asset->read(%d) returned %d\n", size, len);
        delete stream;
        stream = NULL;
    }
    return stream;
}

static jobject nativeDecodeAssetScaled(JNIEnv* env, jobject clazz, jint native_asset,
        jobject padding, jobject options, jboolean applyScale, jfloat scale) {

    SkStream* stream;
    Asset* asset = reinterpret_cast<Asset*>(native_asset);
    bool forcePurgeable = optionsPurgeable(env, options);
    if (forcePurgeable) {
        // if we could "ref/reopen" the asset, we may not need to copy it here
        // and we could assume optionsShareable, since assets are always RO
        stream = copyAssetToStream(asset);
        if (stream == NULL) {
            return NULL;
        }
    } else {
        // since we know we'll be done with the asset when we return, we can
        // just use a simple wrapper
        stream = new AssetStreamAdaptor(asset);
    }
    SkAutoUnref aur(stream);
    return doDecode(env, stream, padding, options, true, forcePurgeable, applyScale, scale);
}

static jobject nativeDecodeAsset(JNIEnv* env, jobject clazz, jint native_asset,
        jobject padding, jobject options) {

    return nativeDecodeAssetScaled(env, clazz, native_asset, padding, options, false, 1.0f);
}

static jobject nativeDecodeByteArray(JNIEnv* env, jobject, jbyteArray byteArray,
        int offset, int length, jobject options) {

    /*  If optionsShareable() we could decide to just wrap the java array and
        share it, but that means adding a globalref to the java array object
        and managing its lifetime. For now we just always copy the array's data
        if optionsPurgeable(), unless we're just decoding bounds.
     */
    bool purgeable = optionsPurgeable(env, options) && !optionsJustBounds(env, options);
    AutoJavaByteArray ar(env, byteArray);
    SkStream* stream = new SkMemoryStream(ar.ptr() + offset, length, purgeable);
    SkAutoUnref aur(stream);
    return doDecode(env, stream, NULL, options, purgeable);
=======
    ::lseek(fd, curr, SEEK_SET);
    return size;
}

/** Restore the file descriptor's offset in our destructor
 */
class AutoFDSeek {
public:
    AutoFDSeek(int fd) : fFD(fd) {
        fCurr = ::lseek(fd, 0, SEEK_CUR);
    }
    ~AutoFDSeek() {
        if (fCurr >= 0) {
            ::lseek(fFD, fCurr, SEEK_SET);
        }
    }
private:
    int     fFD;
    off_t   fCurr;
};

static jobject nativeDecodeFileDescriptor(JNIEnv* env, jobject clazz,
                                          jobject fileDescriptor,
                                          jobject padding,
                                          jobject bitmapFactoryOptions) {
    NPE_CHECK_RETURN_ZERO(env, fileDescriptor);

    jint descriptor = env->GetIntField(fileDescriptor,
                                       gFileDescriptor_descriptor);
    
#ifdef MIN_SIZE_TO_USE_MMAP
    // First try to use mmap
    size_t size = getFDSize(descriptor);
    if (size >= MIN_SIZE_TO_USE_MMAP) {
        void* addr = mmap(NULL, size, PROT_READ, MAP_PRIVATE, descriptor, 0);
//        SkDebugf("-------- mmap returned %p %d\n", addr, size);
        if (MAP_FAILED != addr) {
            SkMemoryStream strm(addr, size);
            jobject obj = doDecode(env, &strm, padding, bitmapFactoryOptions);
            munmap(addr, size);
            return obj;
        }
    }
#endif

    // we pass false for closeWhenDone, since the caller owns the descriptor    
    SkFDStream file(descriptor, false);
    if (!file.isValid()) {
        return NULL;
    }
    
    /* Restore our offset when we leave, so the caller doesn't have to.
       This is a real feature, so we can be called more than once with the
       same descriptor.
    */
    AutoFDSeek as(descriptor);

    return doDecode(env, &file, padding, bitmapFactoryOptions);
}

static jobject nativeDecodeAsset(JNIEnv* env, jobject clazz,
                                 jint native_asset,    // Asset
                                 jobject padding,       // Rect
                                 jobject options) { // BitmapFactory$Options
    AssetStreamAdaptor  mystream((Asset*)native_asset);

    return doDecode(env, &mystream, padding, options);
}

static jobject nativeDecodeByteArray(JNIEnv* env, jobject, jbyteArray byteArray,
                                     int offset, int length, jobject options) {
    AutoJavaByteArray   ar(env, byteArray);
    SkMemoryStream  stream(ar.ptr() + offset, length);

    return doDecode(env, &stream, NULL, options);
>>>>>>> 54b6cfa... Initial Contribution
}

static void nativeRequestCancel(JNIEnv*, jobject joptions) {
    (void)AutoDecoderCancel::RequestCancel(joptions);
}

<<<<<<< HEAD
static jboolean nativeIsSeekable(JNIEnv* env, jobject, jobject fileDescriptor) {
    jint descriptor = jniGetFDFromFileDescriptor(env, fileDescriptor);
    return ::lseek64(descriptor, 0, SEEK_CUR) != -1 ? JNI_TRUE : JNI_FALSE;
}

=======
>>>>>>> 54b6cfa... Initial Contribution
///////////////////////////////////////////////////////////////////////////////

static JNINativeMethod gMethods[] = {
    {   "nativeDecodeStream",
        "(Ljava/io/InputStream;[BLandroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;",
        (void*)nativeDecodeStream
    },
<<<<<<< HEAD
    {   "nativeDecodeStream",
        "(Ljava/io/InputStream;[BLandroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;ZF)Landroid/graphics/Bitmap;",
        (void*)nativeDecodeStreamScaled
    },
=======
>>>>>>> 54b6cfa... Initial Contribution

    {   "nativeDecodeFileDescriptor",
        "(Ljava/io/FileDescriptor;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;",
        (void*)nativeDecodeFileDescriptor
    },

    {   "nativeDecodeAsset",
        "(ILandroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;",
        (void*)nativeDecodeAsset
    },

<<<<<<< HEAD
    {   "nativeDecodeAsset",
        "(ILandroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;ZF)Landroid/graphics/Bitmap;",
        (void*)nativeDecodeAssetScaled
    },

    {   "nativeDecodeByteArray",
        "([BIILandroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;",
        (void*)nativeDecodeByteArray
    },

    {   "nativeScaleNinePatch",
        "([BFLandroid/graphics/Rect;)[B",
        (void*)nativeScaleNinePatch
    },

    {   "nativeIsSeekable",
        "(Ljava/io/FileDescriptor;)Z",
        (void*)nativeIsSeekable
    },
=======
    {   "nativeDecodeByteArray",
        "([BIILandroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;",
        (void*)nativeDecodeByteArray
    }
>>>>>>> 54b6cfa... Initial Contribution
};

static JNINativeMethod gOptionsMethods[] = {
    {   "requestCancel", "()V", (void*)nativeRequestCancel }
};

<<<<<<< HEAD
=======
static jclass make_globalref(JNIEnv* env, const char classname[]) {
    jclass c = env->FindClass(classname);
    SkASSERT(c);
    return (jclass)env->NewGlobalRef(c);
}

>>>>>>> 54b6cfa... Initial Contribution
static jfieldID getFieldIDCheck(JNIEnv* env, jclass clazz,
                                const char fieldname[], const char type[]) {
    jfieldID id = env->GetFieldID(clazz, fieldname, type);
    SkASSERT(id);
    return id;
}

<<<<<<< HEAD
int register_android_graphics_BitmapFactory(JNIEnv* env) {
    jclass options_class = env->FindClass("android/graphics/BitmapFactory$Options");
    SkASSERT(options_class);
    gOptions_bitmapFieldID = getFieldIDCheck(env, options_class, "inBitmap",
        "Landroid/graphics/Bitmap;");
    gOptions_justBoundsFieldID = getFieldIDCheck(env, options_class, "inJustDecodeBounds", "Z");
    gOptions_sampleSizeFieldID = getFieldIDCheck(env, options_class, "inSampleSize", "I");
    gOptions_configFieldID = getFieldIDCheck(env, options_class, "inPreferredConfig",
            "Landroid/graphics/Bitmap$Config;");
    gOptions_mutableFieldID = getFieldIDCheck(env, options_class, "inMutable", "Z");
    gOptions_ditherFieldID = getFieldIDCheck(env, options_class, "inDither", "Z");
    gOptions_purgeableFieldID = getFieldIDCheck(env, options_class, "inPurgeable", "Z");
    gOptions_shareableFieldID = getFieldIDCheck(env, options_class, "inInputShareable", "Z");
    gOptions_preferQualityOverSpeedFieldID = getFieldIDCheck(env, options_class,
            "inPreferQualityOverSpeed", "Z");
    gOptions_widthFieldID = getFieldIDCheck(env, options_class, "outWidth", "I");
    gOptions_heightFieldID = getFieldIDCheck(env, options_class, "outHeight", "I");
    gOptions_mimeFieldID = getFieldIDCheck(env, options_class, "outMimeType", "Ljava/lang/String;");
    gOptions_mCancelID = getFieldIDCheck(env, options_class, "mCancel", "Z");

    jclass bitmap_class = env->FindClass("android/graphics/Bitmap");
    SkASSERT(bitmap_class);
    gBitmap_nativeBitmapFieldID = getFieldIDCheck(env, bitmap_class, "mNativeBitmap", "I");
    gBitmap_layoutBoundsFieldID = getFieldIDCheck(env, bitmap_class, "mLayoutBounds", "[I");
=======
#define kClassPathName  "android/graphics/BitmapFactory"

#define RETURN_ERR_IF_NULL(value) \
    do { if (!(value)) { assert(0); return -1; } } while (false)

int register_android_graphics_BitmapFactory(JNIEnv* env);
int register_android_graphics_BitmapFactory(JNIEnv* env) {
    gOptions_class = make_globalref(env, "android/graphics/BitmapFactory$Options");
    gOptions_justBoundsFieldID = getFieldIDCheck(env, gOptions_class, "inJustDecodeBounds", "Z");
    gOptions_sampleSizeFieldID = getFieldIDCheck(env, gOptions_class, "inSampleSize", "I");
    gOptions_configFieldID = getFieldIDCheck(env, gOptions_class, "inPreferredConfig",
            "Landroid/graphics/Bitmap$Config;");
    gOptions_ditherFieldID = getFieldIDCheck(env, gOptions_class, "inDither", "Z");
    gOptions_widthFieldID = getFieldIDCheck(env, gOptions_class, "outWidth", "I");
    gOptions_heightFieldID = getFieldIDCheck(env, gOptions_class, "outHeight", "I");
    gOptions_mimeFieldID = getFieldIDCheck(env, gOptions_class, "outMimeType", "Ljava/lang/String;");

    gFileDescriptor_class = make_globalref(env, "java/io/FileDescriptor");
    gFileDescriptor_descriptor = getFieldIDCheck(env, gFileDescriptor_class, "descriptor", "I");

>>>>>>> 54b6cfa... Initial Contribution
    int ret = AndroidRuntime::registerNativeMethods(env,
                                    "android/graphics/BitmapFactory$Options",
                                    gOptionsMethods,
                                    SK_ARRAY_COUNT(gOptionsMethods));
    if (ret) {
        return ret;
    }
<<<<<<< HEAD
    return android::AndroidRuntime::registerNativeMethods(env, "android/graphics/BitmapFactory",
=======
    return android::AndroidRuntime::registerNativeMethods(env, kClassPathName,
>>>>>>> 54b6cfa... Initial Contribution
                                         gMethods, SK_ARRAY_COUNT(gMethods));
}
