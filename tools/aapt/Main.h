//
// Copyright 2006 The Android Open Source Project
//
// Some global defines that don't really merit their own header.
//
#ifndef __MAIN_H
#define __MAIN_H

<<<<<<< HEAD
#include <utils/Log.h>
#include <utils/threads.h>
#include <utils/List.h>
#include <utils/Errors.h>
#include "Bundle.h"
#include "AaptAssets.h"
#include "ZipFile.h"


/* Benchmarking Flag */
//#define BENCHMARK 1

#if BENCHMARK
    #include <time.h>
#endif /* BENCHMARK */
=======
#include <utils.h>
#include "Bundle.h"
#include "AaptAssets.h"
#include <utils/ZipFile.h>
>>>>>>> 54b6cfa... Initial Contribution

extern int doVersion(Bundle* bundle);
extern int doList(Bundle* bundle);
extern int doDump(Bundle* bundle);
extern int doAdd(Bundle* bundle);
extern int doRemove(Bundle* bundle);
extern int doPackage(Bundle* bundle);
<<<<<<< HEAD
extern int doCrunch(Bundle* bundle);
=======
>>>>>>> 54b6cfa... Initial Contribution

extern int calcPercent(long uncompressedLen, long compressedLen);

extern android::status_t writeAPK(Bundle* bundle,
    const sp<AaptAssets>& assets,
    const android::String8& outputFile);

<<<<<<< HEAD
extern android::status_t updatePreProcessedCache(Bundle* bundle);

=======
>>>>>>> 54b6cfa... Initial Contribution
extern android::status_t buildResources(Bundle* bundle,
    const sp<AaptAssets>& assets);

extern android::status_t writeResourceSymbols(Bundle* bundle,
    const sp<AaptAssets>& assets, const String8& pkgName, bool includePrivate);

<<<<<<< HEAD
extern android::status_t writeProguardFile(Bundle* bundle, const sp<AaptAssets>& assets);

=======
>>>>>>> 54b6cfa... Initial Contribution
extern bool isValidResourceType(const String8& type);

ssize_t processAssets(Bundle* bundle, ZipFile* zip, const sp<AaptAssets>& assets);

extern status_t filterResources(Bundle* bundle, const sp<AaptAssets>& assets);

int dumpResources(Bundle* bundle);

<<<<<<< HEAD
String8 getAttribute(const ResXMLTree& tree, const char* ns,
                            const char* attr, String8* outError);

status_t writeDependencyPreReqs(Bundle* bundle, const sp<AaptAssets>& assets,
                                FILE* fp, bool includeRaw);
=======
>>>>>>> 54b6cfa... Initial Contribution
#endif // __MAIN_H
