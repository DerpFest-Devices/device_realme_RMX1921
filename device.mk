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

# AID/fs configs
PRODUCT_PACKAGES += \
    fs_config_files

# Bluetooth
PRODUCT_PACKAGES += \
    android.hardware.bluetooth.audio@2.0-impl \
    audio.bluetooth.default \
    libbthost_if \
    vendor.qti.hardware.bluetooth_audio@2.0.vendor
    
# Audio
PRODUCT_PACKAGES += \
    android.hardware.audio@2.0-service \
    android.hardware.audio@5.0-impl \
    android.hardware.audio.effect@5.0-impl \
    android.hardware.soundtrigger@2.1-impl \
    audio.a2dp.default \
    audio.r_submix.default \
    audio.usb.default \
    tinymix

# Camera
PRODUCT_PACKAGES += \
    android.hardware.camera.provider@2.4-impl \
    android.hardware.camera.provider@2.4-service \
    Snap

# Context Hub
PRODUCT_PACKAGES += \
    android.hardware.contexthub@1.0-impl.generic \
    android.hardware.contexthub@1.0-service
    
# Display
PRODUCT_PACKAGES += \
    android.hardware.graphics.composer@2.3-service \
    android.hardware.graphics.mapper@2.0-impl-qti-display \
    android.hardware.memtrack@1.0-impl \
    android.hardware.memtrack@1.0-service \
    gralloc.sdm710 \
    hwcomposer.sdm710 \
    memtrack.sdm710 \
    libtinyxml \
    libvulkan \
    vendor.qti.hardware.display.allocator@1.0-service

# DRM
PRODUCT_PACKAGES += \
    android.hardware.drm@1.0-impl \
    android.hardware.drm@1.0-service \
    android.hardware.drm@1.2-service.clearkey

# IPA
PRODUCT_PACKAGES += \
    libnetfilter_conntrack \
    libnfnetlink

# GPS
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/sec_config:vendor/etc/sec_config

# Health
PRODUCT_PACKAGES += \
    android.hardware.health@2.0-impl \
    android.hardware.health@2.0-service

# HIDL
PRODUCT_PACKAGES += \
    android.hidl.base@1.0 \
    android.hidl.base@1.0_system \
    android.hidl.manager@1.0 \
    android.hidl.manager@1.0_system

# IMS
PRODUCT_PACKAGES += \
    ims-ext-common

# Media
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/media/media_codecs.xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs.xml \
    $(LOCAL_PATH)/media/media_codecs_performance.xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs_performance.xml \
    $(LOCAL_PATH)/media/media_profiles.xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_profiles.xml \
    $(LOCAL_PATH)/media/media_profiles_vendor.xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_profiles_vendor.xml \
    $(LOCAL_PATH)/media/system_properties.xml:$(TARGET_COPY_OUT_VENDOR)/etc/system_properties.xml

PRODUCT_COPY_FILES += \
    frameworks/av/media/libstagefright/data/media_codecs_google_audio.xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs_google_audio.xml \
    frameworks/av/media/libstagefright/data/media_codecs_google_telephony.xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs_google_telephony.xml \
    frameworks/av/media/libstagefright/data/media_codecs_google_video.xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs_google_video.xml \
    frameworks/av/media/libstagefright/data/media_codecs_google_video_le.xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs_google_video_le.xml
    
# Native libraries whitelist
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/public.libraries.txt:$(TARGET_COPY_OUT_VENDOR)/etc/public.libraries.txt

# Net
PRODUCT_PACKAGES += \
    netutils-wrapper-1.0

# Overlays
DEVICE_PACKAGE_OVERLAYS += \
    $(LOCAL_PATH)/overlay-lineage

# Permissions
PRODUCT_COPY_FILES += \
    frameworks/native/data/etc/android.hardware.bluetooth.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/android.hardware.bluetooth.xml \
    frameworks/native/data/etc/android.hardware.bluetooth_le.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/android.hardware.bluetooth_le.xml \
    frameworks/native/data/etc/android.hardware.wifi.direct.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/android.hardware.wifi.direct.xml \
    frameworks/native/data/etc/android.hardware.wifi.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/android.hardware.wifi.xml \
    frameworks/native/data/etc/android.hardware.wifi.passpoint.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/android.hardware.wifi.passpoint.xml \
    frameworks/native/data/etc/android.hardware.camera.flash-autofocus.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/android.hardware.camera.flash-autofocus.xml \
    frameworks/native/data/etc/android.hardware.camera.front.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/android.hardware.camera.front.xml \
    frameworks/native/data/etc/android.hardware.camera.full.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/android.hardware.camera.full.xml \
    frameworks/native/data/etc/android.hardware.camera.raw.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/android.hardware.camera.raw.xml \
    frameworks/native/data/etc/android.hardware.telephony.ims.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/android.hardware.telephony.ims.xml \
    frameworks/native/data/etc/android.hardware.telephony.ims.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/android.hardware.telephony.ims.xml \
    frameworks/native/data/etc/android.software.ipsec_tunnels.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/android.software.ipsec_tunnels.xml

# Power
PRODUCT_PACKAGES += \
    android.hardware.power@1.0-impl \
    android.hardware.power@1.0-service \
    power.sdm710

PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/power/configs/powerhint.xml:$(TARGET_COPY_OUT_VENDOR)/etc/powerhint.xml

# Properties
-include $(LOCAL_PATH)/vendor_prop.mk

# QTI
PRODUCT_PACKAGES += \
    libqti_vndfwk_detect.vendor

PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/qti_whitelist.xml:system/etc/sysconfig/qti_whitelist.xml \
    $(LOCAL_PATH)/configs/privapp-permissions-qti.xml:vendor/etc/permissions/privapp-permissions-qti.xml

# Radio
PRODUCT_PACKAGES += \
    libjson \
    librmnetctl

# Ramdisk
PRODUCT_PACKAGES += \
    fstab.qcom \
    init.oppo.reserve.rc \
    init.msm.usb.configfs.rc \
    init.qcom.early_boot.sh \
    init.qcom.post_boot.sh \
    init.qcom.rc \
    init.qcom.sensors.sh \
    init.qcom.sh \
    init.qcom.usb.rc \
    init.qcom.usb.sh \
    init.recovery.qcom.rc \
    init.target.rc \
    ueventd.qcom.rc
    
# RCS
PRODUCT_PACKAGES += \
    rcs_service_aidl \
    rcs_service_aidl.xml \
    rcs_service_api \
    rcs_service_api.xml

# RenderScript
PRODUCT_PACKAGES += \
    android.hardware.renderscript@1.0-impl

# Seccomp policy
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/seccomp/mediacodec-seccomp.policy:$(TARGET_COPY_OUT_VENDOR)/etc/seccomp_policy/mediacodec.policy

# Sensors
PRODUCT_PACKAGES += \
    android.hardware.sensors@1.0-impl \
    android.hardware.sensors@1.0-service \
    libsensorndkbridge

# Soong namespaces
PRODUCT_SOONG_NAMESPACES += \
    $(LOCAL_PATH)

# Telephony
PRODUCT_PACKAGES += \
    qti-telephony-hidl-wrapper \
    qti_telephony_hidl_wrapper.xml \
    telephony-ext

PRODUCT_BOOT_JARS += \
    telephony-ext

# TextClassifier
PRODUCT_PACKAGES += \
    textclassifier.bundle1

# Thermal
PRODUCT_PACKAGES += \
    android.hardware.thermal@1.0-impl \
    android.hardware.thermal@1.0-service

# Trust HAL
PRODUCT_PACKAGES += \
    vendor.lineage.trust@1.0-service

# USB
PRODUCT_PACKAGES += \
    android.hardware.usb@1.0-service

# Vibrator
PRODUCT_PACKAGES += \
    android.hardware.vibrator@1.0-impl \
    android.hardware.vibrator@1.0-service

# WiFi Display
PRODUCT_PACKAGES += \
    libdisplayconfig \
    libnl \
    libqdMetaData.system

# Wlan
PRODUCT_PACKAGES += \
    android.hardware.wifi@1.0-service
    
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/wlan/hostapd.accept:$(TARGET_COPY_OUT_VENDOR)/etc/hostapd/hostapd.accept \
    $(LOCAL_PATH)/wlan/hostapd.conf:$(TARGET_COPY_OUT_VENDOR)/etc/hostapd/hostapd_default.conf \
    $(LOCAL_PATH)/wlan/hostapd.deny:$(TARGET_COPY_OUT_VENDOR)/etc/hostapd/hostapd.deny \
    $(LOCAL_PATH)/wlan/icm.conf:$(TARGET_COPY_OUT_VENDOR)/etc/wifi/icm.conf \
    $(LOCAL_PATH)/wlan/p2p_supplicant_overlay.conf:$(TARGET_COPY_OUT_VENDOR)/etc/wifi/p2p_supplicant_overlay.conf \
    $(LOCAL_PATH)/wlan/wpa_supplicant_overlay.conf:$(TARGET_COPY_OUT_VENDOR)/etc/wifi/wpa_supplicant_overlay.conf \
    $(LOCAL_PATH)/wlan/WCNSS_qcom_cfg.ini:$(TARGET_COPY_OUT_VENDOR)/etc/wifi/WCNSS_qcom_cfg.ini
