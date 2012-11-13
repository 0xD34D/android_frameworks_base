LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_CFLAGS += -DHAVE_CONFIG_H -DKHTML_NO_EXCEPTIONS -DGKWQ_NO_JAVA
LOCAL_CFLAGS += -DNO_SUPPORT_JS_BINDING -DQT_NO_WHEELEVENT -DKHTML_NO_XBL
LOCAL_CFLAGS += -U__APPLE__

ifeq ($(TARGET_ARCH), arm)
	LOCAL_CFLAGS += -DPACKED="__attribute__ ((packed))"
else
	LOCAL_CFLAGS += -DPACKED=""
endif

<<<<<<< HEAD
ifeq ($(WITH_JIT),true)
	LOCAL_CFLAGS += -DWITH_JIT
endif

ifneq ($(USE_CUSTOM_RUNTIME_HEAP_MAX),)
  LOCAL_CFLAGS += -DCUSTOM_RUNTIME_HEAP_MAX=$(USE_CUSTOM_RUNTIME_HEAP_MAX)
endif

ifeq ($(USE_OPENGL_RENDERER),true)
	LOCAL_CFLAGS += -DUSE_OPENGL_RENDERER
endif

LOCAL_CFLAGS += -DGL_GLEXT_PROTOTYPES -DEGL_EGLEXT_PROTOTYPES

LOCAL_SRC_FILES:= \
	AndroidRuntime.cpp \
	Time.cpp \
	com_android_internal_content_NativeLibraryHelper.cpp \
	com_google_android_gles_jni_EGLImpl.cpp \
	com_google_android_gles_jni_GLImpl.cpp.arm \
	android_app_NativeActivity.cpp \
	android_opengl_GLES10.cpp \
	android_opengl_GLES10Ext.cpp \
	android_opengl_GLES11.cpp \
	android_opengl_GLES11Ext.cpp \
	android_opengl_GLES20.cpp \
	android_database_CursorWindow.cpp \
	android_database_SQLiteCommon.cpp \
	android_database_SQLiteConnection.cpp \
	android_database_SQLiteGlobal.cpp \
	android_database_SQLiteDebug.cpp \
	android_emoji_EmojiFactory.cpp \
	android_view_Display.cpp \
	android_view_DisplayEventReceiver.cpp \
	android_view_Surface.cpp \
	android_view_TextureView.cpp \
	android_view_InputChannel.cpp \
	android_view_InputDevice.cpp \
	android_view_InputEventReceiver.cpp \
	android_view_KeyEvent.cpp \
	android_view_KeyCharacterMap.cpp \
	android_view_HardwareRenderer.cpp \
	android_view_GLES20DisplayList.cpp \
	android_view_GLES20Canvas.cpp \
	android_view_MotionEvent.cpp \
	android_view_PointerIcon.cpp \
	android_view_VelocityTracker.cpp \
	android_text_AndroidCharacter.cpp \
	android_text_AndroidBidi.cpp \
	android_os_Debug.cpp \
	android_os_FileUtils.cpp \
	android_os_MemoryFile.cpp \
	android_os_MessageQueue.cpp \
	android_os_ParcelFileDescriptor.cpp \
	android_os_Parcel.cpp \
=======
LOCAL_SRC_FILES:= \
	ActivityManager.cpp \
	AndroidRuntime.cpp \
	CursorWindow.cpp \
	com_google_android_gles_jni_EGLImpl.cpp \
	com_google_android_gles_jni_GLImpl.cpp.arm \
	android_database_CursorWindow.cpp \
	android_database_SQLiteDebug.cpp \
	android_database_SQLiteDatabase.cpp \
	android_database_SQLiteProgram.cpp \
	android_database_SQLiteQuery.cpp \
	android_database_SQLiteStatement.cpp \
	android_view_Display.cpp \
	android_view_Surface.cpp \
	android_view_ViewRoot.cpp \
	android_text_AndroidCharacter.cpp \
	android_text_KeyCharacterMap.cpp \
	android_os_Debug.cpp \
	android_os_Exec.cpp \
	android_os_FileUtils.cpp \
	android_os_MemoryFile.cpp \
	android_os_ParcelFileDescriptor.cpp \
>>>>>>> 54b6cfa... Initial Contribution
	android_os_Power.cpp \
	android_os_StatFs.cpp \
	android_os_SystemClock.cpp \
	android_os_SystemProperties.cpp \
<<<<<<< HEAD
	android_os_Trace.cpp \
	android_os_UEventObserver.cpp \
	android_net_LocalSocketImpl.cpp \
	android_net_NetUtils.cpp \
	android_net_TrafficStats.cpp \
	android_net_wifi_Wifi.cpp \
	android_nio_utils.cpp \
	android_text_format_Time.cpp \
=======
	android_os_UEventObserver.cpp \
	android_os_NetStat.cpp \
	android_os_Hardware.cpp \
	android_net_LocalSocketImpl.cpp \
	android_net_NetUtils.cpp \
	android_net_wifi_Wifi.cpp \
	android_nio_utils.cpp \
	android_pim_EventRecurrence.cpp \
	android_pim_Time.cpp \
	android_security_Md5MessageDigest.cpp \
>>>>>>> 54b6cfa... Initial Contribution
	android_util_AssetManager.cpp \
	android_util_Binder.cpp \
	android_util_EventLog.cpp \
	android_util_Log.cpp \
	android_util_FloatMath.cpp \
	android_util_Process.cpp \
	android_util_StringBlock.cpp \
	android_util_XmlBlock.cpp \
<<<<<<< HEAD
	android/graphics/AutoDecodeCancel.cpp \
=======
	android_util_Base64.cpp \
>>>>>>> 54b6cfa... Initial Contribution
	android/graphics/Bitmap.cpp \
	android/graphics/BitmapFactory.cpp \
	android/graphics/Camera.cpp \
	android/graphics/Canvas.cpp \
	android/graphics/ColorFilter.cpp \
	android/graphics/DrawFilter.cpp \
	android/graphics/CreateJavaOutputStreamAdaptor.cpp \
	android/graphics/Graphics.cpp \
<<<<<<< HEAD
	android/graphics/HarfbuzzSkia.cpp \
=======
>>>>>>> 54b6cfa... Initial Contribution
	android/graphics/Interpolator.cpp \
	android/graphics/LayerRasterizer.cpp \
	android/graphics/MaskFilter.cpp \
	android/graphics/Matrix.cpp \
	android/graphics/Movie.cpp \
<<<<<<< HEAD
	android/graphics/NinePatch.cpp \
	android/graphics/NinePatchImpl.cpp \
	android/graphics/NinePatchPeeker.cpp \
=======
	android/graphics/NIOBuffer.cpp \
	android/graphics/NinePatch.cpp \
	android/graphics/NinePatchImpl.cpp \
>>>>>>> 54b6cfa... Initial Contribution
	android/graphics/Paint.cpp \
	android/graphics/Path.cpp \
	android/graphics/PathMeasure.cpp \
	android/graphics/PathEffect.cpp \
	android_graphics_PixelFormat.cpp \
	android/graphics/Picture.cpp \
	android/graphics/PorterDuff.cpp \
<<<<<<< HEAD
	android/graphics/BitmapRegionDecoder.cpp \
	android/graphics/Rasterizer.cpp \
	android/graphics/Region.cpp \
	android/graphics/Shader.cpp \
	android/graphics/SurfaceTexture.cpp \
	android/graphics/TextLayout.cpp \
	android/graphics/TextLayoutCache.cpp \
	android/graphics/Typeface.cpp \
	android/graphics/Utils.cpp \
	android/graphics/Xfermode.cpp \
	android/graphics/YuvToJpegEncoder.cpp \
	android_media_AudioRecord.cpp \
	android_media_AudioSystem.cpp \
	android_media_AudioTrack.cpp \
	android_media_JetPlayer.cpp \
	android_media_ToneGenerator.cpp \
	android_hardware_Camera.cpp \
	android_hardware_SensorManager.cpp \
	android_hardware_SerialPort.cpp \
	android_hardware_UsbDevice.cpp \
	android_hardware_UsbDeviceConnection.cpp \
	android_hardware_UsbRequest.cpp \
=======
	android/graphics/Rasterizer.cpp \
	android/graphics/Region.cpp \
	android/graphics/Shader.cpp \
	android/graphics/Typeface.cpp \
	android/graphics/Xfermode.cpp \
	android_media_AudioSystem.cpp \
	android_media_ToneGenerator.cpp \
	android_hardware_Camera.cpp \
	android_hardware_SensorManager.cpp \
>>>>>>> 54b6cfa... Initial Contribution
	android_debug_JNITest.cpp \
	android_util_FileObserver.cpp \
	android/opengl/poly_clip.cpp.arm \
	android/opengl/util.cpp.arm \
<<<<<<< HEAD
	android_bluetooth_HeadsetBase.cpp \
	android_bluetooth_common.cpp \
	android_bluetooth_BluetoothAudioGateway.cpp \
	android_bluetooth_BluetoothSocket.cpp \
	android_bluetooth_c.c \
	android_server_BluetoothService.cpp \
	android_server_BluetoothEventLoop.cpp \
	android_server_BluetoothA2dpService.cpp \
	android_server_NetworkManagementSocketTagger.cpp \
	android_server_Watchdog.cpp \
	android_ddm_DdmHandleNativeHeap.cpp \
	com_android_internal_os_ZygoteInit.cpp \
	android_backup_BackupDataInput.cpp \
	android_backup_BackupDataOutput.cpp \
	android_backup_FileBackupHelperBase.cpp \
	android_backup_BackupHelperDispatcher.cpp \
	android_app_backup_FullBackup.cpp \
	android_content_res_ObbScanner.cpp \
	android_content_res_Configuration.cpp \
    android_animation_PropertyValuesHolder.cpp
=======
	android_bluetooth_Database.cpp \
	android_bluetooth_HeadsetBase.cpp \
	android_bluetooth_common.cpp \
	android_bluetooth_BluetoothAudioGateway.cpp \
	android_bluetooth_RfcommSocket.cpp \
	android_bluetooth_ScoSocket.cpp \
	android_server_BluetoothDeviceService.cpp \
	android_server_BluetoothEventLoop.cpp \
	android_message_digest_sha1.cpp \
	android_ddm_DdmHandleNativeHeap.cpp \
	android_location_GpsLocationProvider.cpp \
	com_android_internal_os_ZygoteInit.cpp \
	com_android_internal_graphics_NativeUtils.cpp
>>>>>>> 54b6cfa... Initial Contribution

LOCAL_C_INCLUDES += \
	$(JNI_H_INCLUDE) \
	$(LOCAL_PATH)/android/graphics \
<<<<<<< HEAD
	$(LOCAL_PATH)/../../libs/hwui \
	$(LOCAL_PATH)/../../../native/opengl/libs \
	$(call include-path-for, bluedroid) \
	$(call include-path-for, libhardware)/hardware \
	$(call include-path-for, libhardware_legacy)/hardware_legacy \
 $(TOP)/frameworks/av/include \
	external/skia/include/core \
	external/skia/include/effects \
	external/skia/include/images \
	external/skia/src/ports \
	external/skia/include/utils \
=======
	$(call include-path-for, corecg graphics) \
	$(call include-path-for, libhardware)/hardware \
	$(LOCAL_PATH)/../../include/ui \
	$(LOCAL_PATH)/../../include/utils \
>>>>>>> 54b6cfa... Initial Contribution
	external/sqlite/dist \
	external/sqlite/android \
	external/expat/lib \
	external/openssl/include \
	external/tremor/Tremor \
	external/icu4c/i18n \
	external/icu4c/common \
<<<<<<< HEAD
	external/jpeg \
	external/harfbuzz/contrib \
	external/harfbuzz/src \
	external/zlib \
	frameworks/opt/emoji \
	libcore/include

LOCAL_SHARED_LIBRARIES := \
	libandroidfw \
=======

LOCAL_SHARED_LIBRARIES := \
>>>>>>> 54b6cfa... Initial Contribution
	libexpat \
	libnativehelper \
	libcutils \
	libutils \
<<<<<<< HEAD
	libbinder \
	libnetutils \
	libui \
	libgui \
	libcamera_client \
	libskia \
	libsqlite \
	libdvm \
	libEGL \
	libGLESv1_CM \
	libGLESv2 \
	libETC1 \
	libhardware \
	libhardware_legacy \
=======
	libnetutils \
	libui \
	libsgl \
	libcorecg \
	libsqlite \
	libdvm \
	libGLES_CM \
	libhardware \
>>>>>>> 54b6cfa... Initial Contribution
	libsonivox \
	libcrypto \
	libssl \
	libicuuc \
	libicui18n \
<<<<<<< HEAD
	libmedia \
	libmedia_native \
	libwpa_client \
	libjpeg \
	libusbhost \
	libharfbuzz \
	libz

ifeq ($(USE_OPENGL_RENDERER),true)
	LOCAL_SHARED_LIBRARIES += libhwui
endif
=======
	libicudata \
	libmedia \
	libwpa_client
>>>>>>> 54b6cfa... Initial Contribution

ifeq ($(BOARD_HAVE_BLUETOOTH),true)
LOCAL_C_INCLUDES += \
	external/dbus \
<<<<<<< HEAD
	system/bluetooth/bluez-clean-headers
=======
	external/bluez/libs/include
>>>>>>> 54b6cfa... Initial Contribution
LOCAL_CFLAGS += -DHAVE_BLUETOOTH
LOCAL_SHARED_LIBRARIES += libbluedroid libdbus
endif

<<<<<<< HEAD
LOCAL_SHARED_LIBRARIES += \
	libdl
# we need to access the private Bionic header
# <bionic_tls.h> in com_google_android_gles_jni_GLImpl.cpp
LOCAL_CFLAGS += -I$(LOCAL_PATH)/../../../../bionic/libc/private

LOCAL_LDLIBS += -lpthread -ldl

=======
ifeq ($(TARGET_ARCH),arm)
LOCAL_SHARED_LIBRARIES += \
	libdl
endif

LOCAL_LDLIBS += -lpthread -ldl

ifeq ($(TARGET_OS),linux)
ifeq ($(TARGET_ARCH),x86)
LOCAL_LDLIBS += -lrt
endif
endif

>>>>>>> 54b6cfa... Initial Contribution
ifeq ($(WITH_MALLOC_LEAK_CHECK),true)
	LOCAL_CFLAGS += -DMALLOC_LEAK_CHECK
endif

LOCAL_MODULE:= libandroid_runtime

include $(BUILD_SHARED_LIBRARY)

<<<<<<< HEAD
=======

>>>>>>> 54b6cfa... Initial Contribution
include $(call all-makefiles-under,$(LOCAL_PATH))
