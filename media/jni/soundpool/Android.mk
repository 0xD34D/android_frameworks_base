LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES:= \
<<<<<<< HEAD
	android_media_SoundPool.cpp
=======
	android_media_SoundPool.cpp \
	SoundPool.cpp \
	SoundPoolThread.cpp
>>>>>>> 54b6cfa... Initial Contribution

LOCAL_SHARED_LIBRARIES := \
	libcutils \
	libutils \
	libandroid_runtime \
	libnativehelper \
<<<<<<< HEAD
	libmedia \
	libmedia_native
=======
	libmedia
>>>>>>> 54b6cfa... Initial Contribution

LOCAL_MODULE:= libsoundpool

include $(BUILD_SHARED_LIBRARY)
