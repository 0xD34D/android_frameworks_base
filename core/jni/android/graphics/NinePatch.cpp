<<<<<<< HEAD
/*
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

#define LOG_TAG "9patch"
#define LOG_NDEBUG 1

#include <androidfw/ResourceTypes.h>
#include <utils/Log.h>

#include "SkCanvas.h"
=======
#include <utils/ResourceTypes.h>

>>>>>>> 54b6cfa... Initial Contribution
#include "SkRegion.h"
#include "GraphicsJNI.h"

#include "JNIHelp.h"

extern void NinePatch_Draw(SkCanvas* canvas, const SkRect& bounds,
                const SkBitmap& bitmap, const android::Res_png_9patch& chunk,
                           const SkPaint* paint, SkRegion** outRegion);
<<<<<<< HEAD

=======
    
>>>>>>> 54b6cfa... Initial Contribution
using namespace android;

class SkNinePatchGlue {
public:
    static jboolean isNinePatchChunk(JNIEnv* env, jobject, jbyteArray obj)
    {
        if (NULL == obj) {
            return false;
        }
        if (env->GetArrayLength(obj) < (int)sizeof(Res_png_9patch)) {
            return false;
        }
<<<<<<< HEAD
        const jbyte* array = env->GetByteArrayElements(obj, 0);
        if (array != NULL) {
            const Res_png_9patch* chunk =
                                reinterpret_cast<const Res_png_9patch*>(array);
            int8_t wasDeserialized = chunk->wasDeserialized;
            env->ReleaseByteArrayElements(obj, const_cast<jbyte*>(array),
                                          JNI_ABORT);
            return wasDeserialized != -1;
=======
        jbyte* array = (jbyte*)env->GetPrimitiveArrayCritical(obj, 0);
        if (array != NULL)
        {
            Res_png_9patch* chunk = (Res_png_9patch*)array;
            int8_t numXDivs = chunk->numXDivs;
            env->ReleasePrimitiveArrayCritical(obj, array, 0);
            return array[0] != -1;
>>>>>>> 54b6cfa... Initial Contribution
        }
        return false;
    }

    static void validateNinePatchChunk(JNIEnv* env, jobject, jint, jbyteArray obj)
    {
        if (env->GetArrayLength(obj) < (int) (sizeof(Res_png_9patch))) {
<<<<<<< HEAD
            jniThrowRuntimeException(env, "Array too small for chunk.");
=======
            jniThrowException(env, "java/lang/RuntimeException",
                              "Array too small for chunk.");
>>>>>>> 54b6cfa... Initial Contribution
            return;
        }

        // XXX Also check that dimensions are correct.
    }

    static void draw(JNIEnv* env, SkCanvas* canvas, SkRect& bounds,
<<<<<<< HEAD
                      const SkBitmap* bitmap, jbyteArray chunkObj, const SkPaint* paint,
                      jint destDensity, jint srcDensity)
    {
        size_t chunkSize = env->GetArrayLength(chunkObj);
        void* storage = alloca(chunkSize);
        env->GetByteArrayRegion(chunkObj, 0, chunkSize,
                                reinterpret_cast<jbyte*>(storage));
        if (!env->ExceptionCheck()) {
            // need to deserialize the chunk
            Res_png_9patch* chunk = static_cast<Res_png_9patch*>(storage);
            assert(chunkSize == chunk->serializedSize());
            // this relies on deserialization being done in place
            Res_png_9patch::deserialize(chunk);

            if (destDensity == srcDensity || destDensity == 0
                    || srcDensity == 0) {
                ALOGV("Drawing unscaled 9-patch: (%g,%g)-(%g,%g)",
                        SkScalarToFloat(bounds.fLeft), SkScalarToFloat(bounds.fTop),
                        SkScalarToFloat(bounds.fRight), SkScalarToFloat(bounds.fBottom));
                NinePatch_Draw(canvas, bounds, *bitmap, *chunk, paint, NULL);
            } else {
                canvas->save();

                SkScalar scale = SkFloatToScalar(destDensity / (float)srcDensity);
                canvas->translate(bounds.fLeft, bounds.fTop);
                canvas->scale(scale, scale);

                bounds.fRight = SkScalarDiv(bounds.fRight-bounds.fLeft, scale);
                bounds.fBottom = SkScalarDiv(bounds.fBottom-bounds.fTop, scale);
                bounds.fLeft = bounds.fTop = 0;

                ALOGV("Drawing scaled 9-patch: (%g,%g)-(%g,%g) srcDensity=%d destDensity=%d",
                        SkScalarToFloat(bounds.fLeft), SkScalarToFloat(bounds.fTop),
                        SkScalarToFloat(bounds.fRight), SkScalarToFloat(bounds.fBottom),
                        srcDensity, destDensity);

                NinePatch_Draw(canvas, bounds, *bitmap, *chunk, paint, NULL);

                canvas->restore();
            }
        }
    }

    static void drawF(JNIEnv* env, jobject, SkCanvas* canvas, jobject boundsRectF,
                      const SkBitmap* bitmap, jbyteArray chunkObj, const SkPaint* paint,
                      jint destDensity, jint srcDensity)
=======
                      const SkBitmap* bitmap, jbyteArray chunkObj, const SkPaint* paint)
    {
        jbyte* array = env->GetByteArrayElements(chunkObj, 0);
        if (array != NULL)
        {
            size_t chunkSize = env->GetArrayLength(chunkObj);
            void* deserializedArray = alloca(chunkSize);
            Res_png_9patch* chunk = (Res_png_9patch*) deserializedArray;
            assert(chunkSize == ((Res_png_9patch*) array)->serializedSize());
            memcpy(chunk, array, chunkSize);
            Res_png_9patch::deserialize(chunk);            
            NinePatch_Draw(canvas, bounds, *bitmap, *chunk, paint, NULL);
            env->ReleaseByteArrayElements(chunkObj, array, 0);
        }
    } 

    static void drawF(JNIEnv* env, jobject, SkCanvas* canvas, jobject boundsRectF,
                      const SkBitmap* bitmap, jbyteArray chunkObj, const SkPaint* paint)
>>>>>>> 54b6cfa... Initial Contribution
    {
        SkASSERT(canvas);
        SkASSERT(boundsRectF);
        SkASSERT(bitmap);
        SkASSERT(chunkObj);
        // paint is optional

        SkRect      bounds;
        GraphicsJNI::jrectf_to_rect(env, boundsRectF, &bounds);

<<<<<<< HEAD
        draw(env, canvas, bounds, bitmap, chunkObj, paint, destDensity, srcDensity);
    }

    static void drawI(JNIEnv* env, jobject, SkCanvas* canvas, jobject boundsRect,
                      const SkBitmap* bitmap, jbyteArray chunkObj, const SkPaint* paint,
                      jint destDensity, jint srcDensity)
=======
        draw(env, canvas, bounds, bitmap, chunkObj, paint);
    }
 
    static void drawI(JNIEnv* env, jobject, SkCanvas* canvas, jobject boundsRect,
                      const SkBitmap* bitmap, jbyteArray chunkObj, const SkPaint* paint)
>>>>>>> 54b6cfa... Initial Contribution
    {
        SkASSERT(canvas);
        SkASSERT(boundsRect);
        SkASSERT(bitmap);
        SkASSERT(chunkObj);
        // paint is optional

        SkRect      bounds;
        GraphicsJNI::jrect_to_rect(env, boundsRect, &bounds);
<<<<<<< HEAD
        draw(env, canvas, bounds, bitmap, chunkObj, paint, destDensity, srcDensity);
    }

=======
        draw(env, canvas, bounds, bitmap, chunkObj, paint);
    }
    
>>>>>>> 54b6cfa... Initial Contribution
    static jint getTransparentRegion(JNIEnv* env, jobject,
                    const SkBitmap* bitmap, jbyteArray chunkObj,
                    jobject boundsRect)
    {
        SkASSERT(bitmap);
        SkASSERT(chunkObj);
        SkASSERT(boundsRect);
<<<<<<< HEAD

        SkRect      bounds;
        GraphicsJNI::jrect_to_rect(env, boundsRect, &bounds);
        size_t chunkSize = env->GetArrayLength(chunkObj);
        void* storage = alloca(chunkSize);
        env->GetByteArrayRegion(chunkObj, 0, chunkSize,
                                reinterpret_cast<jbyte*>(storage));
        if (!env->ExceptionCheck()) {
            // need to deserialize the chunk
            Res_png_9patch* chunk = static_cast<Res_png_9patch*>(storage);
            assert(chunkSize == chunk->serializedSize());
            // this relies on deserialization being done in place
            Res_png_9patch::deserialize(chunk);
            SkRegion* region = NULL;
            NinePatch_Draw(NULL, bounds, *bitmap, *chunk, NULL, &region);
            return (jint)region;
        }
=======
        
        SkRect      bounds;
        GraphicsJNI::jrect_to_rect(env, boundsRect, &bounds);
        jbyte* array = (jbyte*)env->GetByteArrayElements(chunkObj, 0);
        if (array != NULL)
        {
            size_t chunkSize = env->GetArrayLength(chunkObj);
            void* deserializedArray = alloca(chunkSize);
            Res_png_9patch* chunk = (Res_png_9patch*) deserializedArray;
            assert(chunkSize == ((Res_png_9patch*) array)->serializedSize());
            memcpy(chunk, array, chunkSize);
            Res_png_9patch::deserialize(chunk);
            SkRegion* region = NULL;
            NinePatch_Draw(NULL, bounds, *bitmap, *chunk, NULL, &region);
            env->ReleaseByteArrayElements(chunkObj, array, 0);
            return (jint)region;
        }
        
>>>>>>> 54b6cfa... Initial Contribution
        return 0;
    }

};

/////////////////////////////////////////////////////////////////////////////////////////

#include <android_runtime/AndroidRuntime.h>

static JNINativeMethod gNinePatchMethods[] = {
    { "isNinePatchChunk", "([B)Z",                      (void*)SkNinePatchGlue::isNinePatchChunk   },
    { "validateNinePatchChunk", "(I[B)V",               (void*)SkNinePatchGlue::validateNinePatchChunk   },
<<<<<<< HEAD
    { "nativeDraw", "(ILandroid/graphics/RectF;I[BIII)V", (void*)SkNinePatchGlue::drawF   },
    { "nativeDraw", "(ILandroid/graphics/Rect;I[BIII)V",  (void*)SkNinePatchGlue::drawI   },
    { "nativeGetTransparentRegion", "(I[BLandroid/graphics/Rect;)I",
                                                        (void*)SkNinePatchGlue::getTransparentRegion   }
};

=======
    { "nativeDraw", "(ILandroid/graphics/RectF;I[BI)V", (void*)SkNinePatchGlue::drawF   },
    { "nativeDraw", "(ILandroid/graphics/Rect;I[BI)V",  (void*)SkNinePatchGlue::drawI   },
    { "nativeGetTransparentRegion", "(I[BLandroid/graphics/Rect;)I", 
                                                        (void*)SkNinePatchGlue::getTransparentRegion   }
};

int register_android_graphics_NinePatch(JNIEnv* env);
>>>>>>> 54b6cfa... Initial Contribution
int register_android_graphics_NinePatch(JNIEnv* env)
{
    return android::AndroidRuntime::registerNativeMethods(env,
                                                       "android/graphics/NinePatch",
                                                       gNinePatchMethods,
                                                       SK_ARRAY_COUNT(gNinePatchMethods));
}
