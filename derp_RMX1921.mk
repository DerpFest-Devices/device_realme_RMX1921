#
# Copyright (C) 2020 The LineageOS Project
#
# SPDX-License-Identifier: Apache-2.0
#

# Inherit some common DerpFest stuff
TARGET_GAPPS_ARCH := arm64
TARGET_BOOT_ANIMATION_RES := 1080
IS_PHONE := true
TARGET_SUPPORTS_GOOGLE_RECORDER := false
$(call inherit-product, vendor/aosip/config/common_full_phone.mk)

# Official-ify
DERP_BUILDTYPE := Official

# Inherit from RMX1921 device
$(call inherit-product, $(LOCAL_PATH)/device.mk)

PRODUCT_BRAND := Realme
PRODUCT_DEVICE := RMX1921
PRODUCT_MANUFACTURER := Realme
PRODUCT_NAME := derp_RMX1921
PRODUCT_MODEL := Realme XT

PRODUCT_GMS_CLIENTID_BASE := android-oppo

PRODUCT_BUILD_PROP_OVERRIDES += \
    PRIVATE_BUILD_DESC="coral-user 10 QQ3A.200705.002 6506677 release-keys" \
    PRODUCT_NAME="RMX1921"

# Set BUILD_FINGERPRINT variable to be picked up by both system and vendor build.prop
BUILD_FINGERPRINT := google/coral/coral:10/QQ3A.200705.002/6506677:user/release-keys
