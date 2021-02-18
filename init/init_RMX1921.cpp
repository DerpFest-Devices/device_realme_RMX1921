/*
 * Copyright (C) 2020 LineageOS Project
 *
 *
 * SPDX-License-Identifier: Apache-2.0
 */

#include <fstream>
#include <stdlib.h>
#define _REALLY_INCLUDE_SYS__SYSTEM_PROPERTIES_H_
#include <sys/_system_properties.h>

#include <android-base/properties.h>
#include "property_service.h"
#include "vendor_init.h"

using std::string;

void property_override(string prop, string value)
{
    auto pi = (prop_info*) __system_property_find(prop.c_str());
    if (pi != nullptr)
        __system_property_update(pi, value.c_str(), value.size());
    else
        __system_property_add(prop.c_str(), prop.size(), value.c_str(), value.size());
}

void vendor_load_properties() {

        string device, model, prjName;
	string oppo_sku = android::base::GetProperty("ro.boot.product.hardware.sku", "");

	std::ifstream infile("/proc/oppoVersion/prjName");
	getline(infile, prjName);

	if (prjName == "19651") {
            model = "RMX1921";
               if (oppo_sku == "nfc_ese" || oppo_sku == "RMX1921EU") {
                   device = "RMX1921EU";
                   property_override("ro.hardware.nfc_nci", "nqx.default");
                   property_override("ro.nfc.port", "I2C");
               } else {
                    device = "RMX1921";
                            }
                       }

    // Override all partitions' props
    string prop_partitions[] = { "", "odm.", "product.", "system.", "vendor." };

    for (const string &prop : prop_partitions) {
        property_override(string("ro.product.") + prop + string("name"), device);
        property_override(string("ro.product.") + prop + string("device"), device);
        property_override(string("ro.product.") + prop + string("model"), model);
        property_override(string("ro.") + prop + string("build.product"), device);

    //Set hardware SKU prop
    property_override("ro.boot.product.hardware.sku", device);
}
}
