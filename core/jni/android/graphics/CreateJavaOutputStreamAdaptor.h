#ifndef CreateJavaOutputStream_DEFINED
#define CreateJavaOutputStream_DEFINED

//#include <android_runtime/AndroidRuntime.h>
#include "jni.h"
#include "SkStream.h"

SkStream* CreateJavaInputStreamAdaptor(JNIEnv* env, jobject stream,
<<<<<<< HEAD
                                       jbyteArray storage, int markSize = 0);
=======
                                       jbyteArray storage);
>>>>>>> 54b6cfa... Initial Contribution
SkWStream* CreateJavaOutputStreamAdaptor(JNIEnv* env, jobject stream,
                                         jbyteArray storage);

#endif
