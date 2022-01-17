/*
 * Copyright (C) 2018-2019 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include "Light.h"

#include <android-base/file.h>
#include <android-base/logging.h>
#include <fcntl.h>
#include <fstream>

using ::android::base::WriteStringToFile;

namespace {
/*
 * Read max brightness from path and close file.
 */
static int getMaxBrightness(std::string path) {
    std::ifstream file(path);
    int value;

    if (!file.is_open()) {
        LOG(WARNING) << "failed to read from " << path.c_str();
        return 0;
    }

    file >> value;
    return value;
}
} // anonymous namespace

namespace aidl {
namespace android {
namespace hardware {
namespace light {

static const std::string kLCDFile = "/sys/class/backlight/panel0-backlight/brightness";

#define AutoHwLight(light) {.id = (int)light, .type = light, .ordinal = 0}
#define MAX_BRIGHTNESS_PATH    "/sys/class/backlight/panel0-backlight/max_brightness"

// List of supported lights
const static std::vector<HwLight> kAvailableLights = {
    AutoHwLight(LightType::BACKLIGHT),
};

// AIDL methods
ndk::ScopedAStatus Lights::setLightState(int id, const HwLightState& state) {
    switch (id) {
        case (int)LightType::BACKLIGHT:
            WriteToFile(kLCDFile, RgbaToBrightness(state.color));
            break;
    }

    return ndk::ScopedAStatus::ok();
}

ndk::ScopedAStatus Lights::getLights(std::vector<HwLight>* lights) {
    for (auto i = kAvailableLights.begin(); i != kAvailableLights.end(); i++) {
        lights->push_back(*i);
    }
    return ndk::ScopedAStatus::ok();
}

uint32_t Lights::RgbaToBrightness(uint32_t color) {
    // Extract brightness from AARRGGBB.
    uint32_t alpha = (color >> 24) & 0xFF;
    uint32_t brightness;
    // Retrieve each of the RGB colors
    uint32_t red = (color >> 16) & 0xFF;
    uint32_t green = (color >> 8) & 0xFF;
    uint32_t blue = color & 0xFF;
    int max_brightness = getMaxBrightness(MAX_BRIGHTNESS_PATH);
    /*
     * Scale RGB brightness if Alpha brightness is not 0xFF.
     */
    if (alpha != 0xFF) {
        red = red * alpha / 0xFF;
        green = green * alpha / 0xFF;
        blue = blue * alpha / 0xFF;
    }
    brightness=((77 * red + 150 * green + 29 * blue) >> 8);
    // Scale the value for our panel
    brightness=((brightness - 1) * (max_brightness - 1) / (0xFF - 1) + 1);
    // Prevent errors
    if (brightness > max_brightness)
        brightness = max_brightness;
    return brightness;
}

// Write value to path and close file.
bool Lights::WriteToFile(const std::string& path, uint32_t content) {
    return WriteStringToFile(std::to_string(content), path);
}

}  // namespace light
}  // namespace hardware
}  // namespace android
}  // namespace aidl
