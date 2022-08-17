LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := RemovePackages
LOCAL_MODULE_CLASS := APPS
LOCAL_MODULE_TAGS := optional
LOCAL_OVERRIDES_PACKAGES := AmbientSensePrebuilt AppDirectedSMSService CarrierSetup ConnMO DCMO DMService MyVerizonServices RecorderPrebuilt Maps YouTube Photos Drive PrebuiltGmail arcore SoundAmplifierPrebuilt SafetyHubPrebuilt SecurityHubPrebuilt Showcase USCCDM VZWAPNLib VzwOmaTrigger MusicFX GCS GoogleCamera DevicePolicyPrebuilt Chrome Chrome-Stub SCONE Tycho PixelLiveWallpaperPrebuilt GoogleTTS CarrierMetrics DiagnosticsToolPrebuilt NgaResources ScribePrebuilt
LOCAL_UNINSTALLABLE_MODULE := true
LOCAL_CERTIFICATE := PRESIGNED
LOCAL_SRC_FILES := /dev/null
include $(BUILD_PREBUILT)
