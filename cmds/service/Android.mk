LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES:= \
	service.cpp

<<<<<<< HEAD
LOCAL_SHARED_LIBRARIES := libutils libbinder
=======
LOCAL_SHARED_LIBRARIES := \
	libutils
>>>>>>> 54b6cfa... Initial Contribution

ifeq ($(TARGET_OS),linux)
	LOCAL_CFLAGS += -DXP_UNIX
	#LOCAL_SHARED_LIBRARIES += librt
endif

LOCAL_MODULE:= service

include $(BUILD_EXECUTABLE)
