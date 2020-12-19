#!/bin/bash
#
# Copyright (C) 2016 The CyanogenMod Project
# Copyright (C) 2017-2021 The LineageOS Project
#
# SPDX-License-Identifier: Apache-2.0

set -e

DEVICE=RMX1921
VENDOR=realme

INITIAL_COPYRIGHT_YEAR=2019

# Load extract_utils and do some sanity checks
MY_DIR="${BASH_SOURCE%/*}"
if [[ ! -d "$MY_DIR" ]]; then MY_DIR="$PWD"; fi

ANDROID_ROOT="${MY_DIR}/../../.."

HELPER="${ANDROID_ROOT}/tools/extract-utils/extract_utils.sh"
if [ ! -f "${HELPER}" ]; then
    echo "Unable to find helper script at ${HELPER}"
    exit 1
fi
. "$HELPER"

# Initialize the helper for common
setup_vendor "$DEVICE" "$VENDOR" "$ANDROID_ROOT" true

# Warning headers and guards
write_headers "RMX1921"

write_makefiles "$MY_DIR"/proprietary-files.txt

# Finish
write_footers
