<<<<<<< HEAD
=======
ifneq ($(TARGET_SIMULATOR),true)
>>>>>>> 54b6cfa... Initial Contribution
LOCAL_PATH:= $(call my-dir)

#include $(CLEAR_VARS)
#LOCAL_SRC_FILES := bctest.c binder.c
#LOCAL_MODULE := bctest
#include $(BUILD_EXECUTABLE)

include $(CLEAR_VARS)
LOCAL_SHARED_LIBRARIES := liblog
LOCAL_SRC_FILES := service_manager.c binder.c
LOCAL_MODULE := servicemanager
include $(BUILD_EXECUTABLE)
<<<<<<< HEAD
=======
endif
>>>>>>> 54b6cfa... Initial Contribution
