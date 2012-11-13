LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES:= \
	system_init.cpp

base = $(LOCAL_PATH)/../../..
<<<<<<< HEAD
native = $(LOCAL_PATH)/../../../../native

LOCAL_C_INCLUDES := \
	$(base)/services/sensorservice \
	$(native)/services/surfaceflinger \
=======

LOCAL_C_INCLUDES := \
	$(base)/camera/libcameraservice \
	$(base)/libs/audioflinger \
	$(base)/libs/surfaceflinger \
	$(base)/media/libmediaplayerservice \
>>>>>>> 54b6cfa... Initial Contribution
	$(JNI_H_INCLUDE)

LOCAL_SHARED_LIBRARIES := \
	libandroid_runtime \
<<<<<<< HEAD
	libsensorservice \
	libsurfaceflinger \
    libinput \
	libutils \
	libbinder \
=======
	libsurfaceflinger \
	libaudioflinger \
    libcameraservice \
    libmediaplayerservice \
	libutils \
>>>>>>> 54b6cfa... Initial Contribution
	libcutils

LOCAL_MODULE:= libsystem_server

include $(BUILD_SHARED_LIBRARY)
