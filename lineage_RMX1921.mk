#
# Copyright (C) 2020 The LineageOS Project
#
# SPDX-License-Identifier: Apache-2.0
#

# Inherit from RMX1921 device
$(call inherit-product, $(LOCAL_PATH)/device.mk)

PRODUCT_BRAND := realme
PRODUCT_DEVICE := RMX1921
PRODUCT_MANUFACTURER := realme
PRODUCT_NAME := lineage_RMX1921
PRODUCT_MODEL := realme XT

PRODUCT_GMS_CLIENTID_BASE := android-oppo
TARGET_VENDOR := realme
TARGET_VENDOR_PRODUCT_NAME := RMX1921
PRODUCT_BUILD_PROP_OVERRIDES += PRIVATE_BUILD_DESC="sdm710-user 10 QKQ1.190918.001 release-keys"

# Set BUILD_FINGERPRINT variable to be picked up by both system and vendor build.prop
BUILD_FINGERPRINT := sdm710-user-10-QKQ1.190918.001-release-keys
