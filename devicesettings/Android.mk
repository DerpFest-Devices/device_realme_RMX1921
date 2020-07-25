#
# Copyright (C) 2013 The CyanogenMod Project
# Copyright (C) 2017 The LineageOS Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES := $(call all-java-files-under, src)
LOCAL_CERTIFICATE := platform
LOCAL_PRIVATE_PLATFORM_APIS := true
LOCAL_PRIVILEGED_MODULE := true
LOCAL_PACKAGE_NAME := devicesettings

LOCAL_USE_AAPT2 := true

LOCAL_STATIC_ANDROID_LIBRARIES := \
    androidx.core_core \
    androidx.preference_preference

LOCAL_RESOURCE_DIR := \
    $(LOCAL_PATH)/res \
    $(TOP)/packages/resources/devicesettings/res

package_resource_overlays := $(strip \
    $(wildcard $(foreach dir, $(PRODUCT_PACKAGE_OVERLAYS), \
      $(addprefix $(dir)/, packages/apps/devicesettings/res))) \
    $(wildcard $(foreach dir, $(DEVICE_PACKAGE_OVERLAYS), \
      $(addprefix $(dir)/, packages/apps/devicesettings/res))))

LOCAL_RESOURCE_DIR := $(package_resource_overlays) $(LOCAL_RESOURCE_DIR)
LOCAL_PROGUARD_FLAG_FILES := proguard.flags
LOCAL_MODULE_TAGS := optional
include frameworks/base/packages/SettingsLib/common.mk
include $(BUILD_PACKAGE)
