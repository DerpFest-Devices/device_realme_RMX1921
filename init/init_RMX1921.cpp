/*
 * Copyright (C) 2021 LineageOS Project
 *
 *
 * SPDX-License-Identifier: Apache-2.0
 */

#include <fstream>
#include <unistd.h>
#include <vector>
#include <cstdlib>
#include <string.h>

#define _REALLY_INCLUDE_SYS__SYSTEM_PROPERTIES_H_
#include <sys/_system_properties.h>

#include <android-base/properties.h>
#include "property_service.h"
#include "vendor_init.h"

using android::base::GetProperty;
using std::string;

std::vector<string> ro_props_default_source_order = {
    "",
    "bootimage.",
    "odm.",
    "product.",
    "system.",
    "system_ext.",
    "vendor."
};

void property_override(char const prop[], char const value[], bool add = true) {
    prop_info *pi;

    pi = (prop_info *)__system_property_find(prop);

    if (pi)
	__system_property_update(pi, value, strlen(value));
    else if (add)
        __system_property_add(prop, strlen(prop), value, strlen(value));
}

void vendor_load_properties() {

    string oppo_sku = GetProperty("ro.boot.product.hardware.sku", "");

    if (oppo_sku == "nfc_ese"){
        property_override("ro.boot.product.hardware.sku", "RMX1921EU");
        property_override("ro.hardware.nfc_nci", "nqx.default");
        property_override("ro.nfc.port", "I2C");
    } else if (oppo_sku == "secure_element_uicc") {
        property_override("ro.boot.product.hardware.sku", "RMX1921");
    }
}
