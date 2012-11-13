# 
# Copyright 2006 The Android Open Source Project
#
# Android Asset Packaging Tool
#

<<<<<<< HEAD
# This tool is prebuilt if we're doing an app-only build.
ifeq ($(TARGET_BUILD_APPS),)

=======
>>>>>>> 54b6cfa... Initial Contribution
LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES := \
	AaptAssets.cpp \
	Command.cpp \
<<<<<<< HEAD
	CrunchCache.cpp \
	FileFinder.cpp \
=======
>>>>>>> 54b6cfa... Initial Contribution
	Main.cpp \
	Package.cpp \
	StringPool.cpp \
	XMLNode.cpp \
<<<<<<< HEAD
	ResourceFilter.cpp \
	ResourceTable.cpp \
	Images.cpp \
	Resource.cpp \
    SourcePos.cpp \
    ZipEntry.cpp \
    ZipFile.cpp

=======
	ResourceTable.cpp \
	Images.cpp \
	Resource.cpp \
    SourcePos.cpp
>>>>>>> 54b6cfa... Initial Contribution

LOCAL_CFLAGS += -Wno-format-y2k

LOCAL_C_INCLUDES += external/expat/lib
LOCAL_C_INCLUDES += external/libpng
LOCAL_C_INCLUDES += external/zlib
LOCAL_C_INCLUDES += build/libs/host/include

#LOCAL_WHOLE_STATIC_LIBRARIES := 
LOCAL_STATIC_LIBRARIES := \
	libhost \
<<<<<<< HEAD
	libandroidfw \
=======
>>>>>>> 54b6cfa... Initial Contribution
	libutils \
	libcutils \
	libexpat \
	libpng

<<<<<<< HEAD
ifeq ($(HOST_OS),linux)
LOCAL_LDLIBS += -lrt -ldl -lpthread
endif

# Statically link libz for MinGW (Win SDK under Linux),
# and dynamically link for all others.
ifneq ($(strip $(USE_MINGW)),)
  LOCAL_STATIC_LIBRARIES += libz
else
  LOCAL_LDLIBS += -lz
=======
LOCAL_LDLIBS := -lz

ifeq ($(HOST_OS),linux)
LOCAL_LDLIBS += -lrt
endif

ifeq ($(HOST_OS),windows)
ifeq ($(strip $(USE_CYGWIN),),)
LOCAL_LDLIBS += -lws2_32
endif
>>>>>>> 54b6cfa... Initial Contribution
endif

LOCAL_MODULE := aapt

include $(BUILD_HOST_EXECUTABLE)

<<<<<<< HEAD
endif # TARGET_BUILD_APPS
=======
>>>>>>> 54b6cfa... Initial Contribution
