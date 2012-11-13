LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES := $(call all-subdir-java-files)
<<<<<<< HEAD
LOCAL_MODULE_TAGS := tests
=======
>>>>>>> 54b6cfa... Initial Contribution

LOCAL_MODULE := loadclass

include $(BUILD_JAVA_LIBRARY)
