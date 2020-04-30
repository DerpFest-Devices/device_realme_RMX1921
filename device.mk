#
# Copyright (C) 2020 The LineageOS Project
#
# SPDX-License-Identifier: Apache-2.0
#

# Realme XT is a product launched with pie
$(call inherit-product, build/make/target/product/product_launched_with_p.mk)

# Inherit from those products. Most specific first.
$(call inherit-product, $(SRC_TARGET_DIR)/product/core_64_bit.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/full_base_telephony.mk)

# Get non-open-source specific aspects
$(call inherit-product-if-exists, vendor/realme/RMX1921/RMX1921-vendor.mk)

# Inherit some common Lineage stuff
$(call inherit-product, vendor/lineage/config/common_full_phone.mk)

# Audio
PRODUCT_PACKAGES += \
    audio.a2dp.default

# Display
PRODUCT_PACKAGES += \
    libvulkan

# IMS
PRODUCT_PACKAGES += \
    ims-ext-common

# Net
PRODUCT_PACKAGES += \
    netutils-wrapper-1.0

# Properties
-include $(LOCAL_PATH)/vendor_prop.mk

# RCS
PRODUCT_PACKAGES += \
    rcs_service_aidl \
    rcs_service_aidl.xml \
    rcs_service_api \
    rcs_service_api.xml

# Telephony
PRODUCT_PACKAGES += \
    telephony-ext

PRODUCT_BOOT_JARS += \
    telephony-ext
