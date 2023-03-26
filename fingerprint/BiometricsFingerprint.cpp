/*
 * Copyright (C) 2021 The LineageOS Project
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
#define LOG_TAG "android.hardware.biometrics.fingerprint@2.3-service.xt"
#define LOG_VERBOSE "android.hardware.biometrics.fingerprint@2.3-service.xt"

#include <hardware/hardware.h>
#include <hardware/fingerprint.h>
#include "BiometricsFingerprint.h"

#include <inttypes.h>
#include <unistd.h>
#include <fstream>

#define FINGERPRINT_ACQUIRED_VENDOR 6

#define FP_PRESS_PATH "/sys/kernel/oppo_display/notify_fppress"
#define DIMLAYER_PATH "/sys/kernel/oppo_display/dimlayer_hbm"
#define AOD_MODE_PATH "/sys/kernel/oppo_display/aod_light_mode_set"
#define NOTIFY_BLANK_PATH "/sys/kernel/oppo_display/notify_panel_blank"
#define POWER_STATUS "/sys/kernel/oppo_display/power_status"
#define DC_DIM_PATH "/sys/kernel/oppo_display/dimlayer_bl_en"

namespace android {
namespace hardware {
namespace biometrics {
namespace fingerprint {
namespace V2_3 {
namespace implementation {

bool volatile dcDimState;
bool volatile mFodCircleVisible;
std::string lastStorePath;
uint32_t lastGid;

BiometricsFingerprint::BiometricsFingerprint() {
    mOppoBiometricsFingerprint = vendor::oppo::hardware::biometrics::fingerprint::V2_1::IBiometricsFingerprint::getService();
    mFodCircleVisible = false;
}


/*
 * Write value to path and close file.
 */
template <typename T>
static void set(const std::string& path, const T& value) {
    std::ofstream file(path);
    file << value;
}

template <typename T>
static T get(const std::string& path, const T& def) {
    std::ifstream file(path);
    T result;

    file >> result;
    return file.fail() ? def : result;
}


class OppoClientCallback : public vendor::oppo::hardware::biometrics::fingerprint::V2_1::IBiometricsFingerprintClientCallback {
public:
    OppoClientCallback(sp<android::hardware::biometrics::fingerprint::V2_1::IBiometricsFingerprintClientCallback> clientCallback) : mClientCallback(clientCallback) {}
    Return<void> onEnrollResult(uint64_t deviceId, uint32_t fingerId,
        uint32_t groupId, uint32_t remaining) {
        return mClientCallback->onEnrollResult(deviceId, fingerId, groupId, remaining);
    }

    Return<void> onAcquired(uint64_t deviceId, vendor::oppo::hardware::biometrics::fingerprint::V2_1::FingerprintAcquiredInfo acquiredInfo,
        int32_t vendorCode) {
        return mClientCallback->onAcquired(deviceId, OppoToAOSPFingerprintAcquiredInfo(acquiredInfo), vendorCode);
    }

    Return<void> onAuthenticated(uint64_t deviceId, uint32_t fingerId, uint32_t groupId,
        const hidl_vec<uint8_t>& token) {
        if (token.size() > 0) {
            if (mFodCircleVisible) {
                set(DC_DIM_PATH, dcDimState);
            }

            set(DIMLAYER_PATH, 0);
            set(FP_PRESS_PATH, 0);
            mFodCircleVisible = false;
        }

        ALOGE("onAuthenticated %lu %u %u, isAuthSucceed %d", deviceId, fingerId, groupId, token.size() > 0);
        
        return mClientCallback->onAuthenticated(deviceId, fingerId, groupId, token);
    }

    Return<void> onError(uint64_t deviceId, vendor::oppo::hardware::biometrics::fingerprint::V2_1::FingerprintError error, int32_t vendorCode) {
        return mClientCallback->onError(deviceId, OppoToAOSPFingerprintError(error), vendorCode);
    }

    Return<void> onRemoved(uint64_t deviceId, uint32_t fingerId, uint32_t groupId,
        uint32_t remaining) {
        ALOGE("onRemoved %lu %u", deviceId, fingerId);
        mClientCallback->onRemoved(deviceId, fingerId, groupId, remaining);
        return Void();
    }

    Return<void> onEnumerate(uint64_t deviceId, uint32_t fingerId, uint32_t groupId,
        uint32_t remaining) {
        ALOGE("onEnumerate %lu %u %u %u", deviceId, fingerId, groupId, remaining);
        return mClientCallback->onEnumerate(deviceId, fingerId, groupId, remaining);
    }

    Return<void> onTouchUp(uint64_t deviceId) {
        return mClientCallback->onAcquired(deviceId, android::hardware::biometrics::fingerprint::V2_1::FingerprintAcquiredInfo::ACQUIRED_VENDOR, 0);
	}
    Return<void> onTouchDown(uint64_t deviceId)  {
        return mClientCallback->onAcquired(deviceId, android::hardware::biometrics::fingerprint::V2_1::FingerprintAcquiredInfo::ACQUIRED_VENDOR, 1);
	}
    Return<void> onSyncTemplates(uint64_t deviceId, const hidl_vec<uint32_t>& fingerId, uint32_t remaining) {
        ALOGE("onSyncTemplates %lu %zu %u", deviceId, fingerId.size(), remaining);

        size_t nFingers = fingerId.size();
        if(nFingers > 0) {
            for(auto finger: fingerId) {
                mClientCallback->onEnumerate(deviceId, finger, 0, --nFingers);
            }
        } else {
            mClientCallback->onEnumerate(deviceId, 0, 0, 0);
        }
        return Void();
}

    Return<void> onFingerprintCmd(int32_t deviceId, const hidl_vec<uint32_t>& groupId, uint32_t remaining) { return Void(); }
    Return<void> onImageInfoAcquired(uint32_t type, uint32_t quality, uint32_t match_score) { return Void(); }
    Return<void> onMonitorEventTriggered(uint32_t type, const hidl_string& data) { return Void(); }
    Return<void> onEngineeringInfoUpdated(uint32_t length, const hidl_vec<uint32_t>& keys, const hidl_vec<hidl_string>& values) { return Void(); }

private:
    sp<android::hardware::biometrics::fingerprint::V2_1::IBiometricsFingerprintClientCallback> mClientCallback;

    Return<android::hardware::biometrics::fingerprint::V2_1::FingerprintAcquiredInfo> OppoToAOSPFingerprintAcquiredInfo(vendor::oppo::hardware::biometrics::fingerprint::V2_1::FingerprintAcquiredInfo info) {
        switch(info) {
            case vendor::oppo::hardware::biometrics::fingerprint::V2_1::FingerprintAcquiredInfo::ACQUIRED_GOOD: return android::hardware::biometrics::fingerprint::V2_1::FingerprintAcquiredInfo::ACQUIRED_GOOD;
            case vendor::oppo::hardware::biometrics::fingerprint::V2_1::FingerprintAcquiredInfo::ACQUIRED_PARTIAL: return android::hardware::biometrics::fingerprint::V2_1::FingerprintAcquiredInfo::ACQUIRED_PARTIAL;
            case vendor::oppo::hardware::biometrics::fingerprint::V2_1::FingerprintAcquiredInfo::ACQUIRED_INSUFFICIENT: return android::hardware::biometrics::fingerprint::V2_1::FingerprintAcquiredInfo::ACQUIRED_INSUFFICIENT;
            case vendor::oppo::hardware::biometrics::fingerprint::V2_1::FingerprintAcquiredInfo::ACQUIRED_IMAGER_DIRTY: return android::hardware::biometrics::fingerprint::V2_1::FingerprintAcquiredInfo::ACQUIRED_IMAGER_DIRTY;
            case vendor::oppo::hardware::biometrics::fingerprint::V2_1::FingerprintAcquiredInfo::ACQUIRED_TOO_SLOW: return android::hardware::biometrics::fingerprint::V2_1::FingerprintAcquiredInfo::ACQUIRED_TOO_SLOW;
            case vendor::oppo::hardware::biometrics::fingerprint::V2_1::FingerprintAcquiredInfo::ACQUIRED_TOO_FAST: return android::hardware::biometrics::fingerprint::V2_1::FingerprintAcquiredInfo::ACQUIRED_TOO_FAST;
            case vendor::oppo::hardware::biometrics::fingerprint::V2_1::FingerprintAcquiredInfo::ACQUIRED_VENDOR: return android::hardware::biometrics::fingerprint::V2_1::FingerprintAcquiredInfo::ACQUIRED_VENDOR;
            default:
                return android::hardware::biometrics::fingerprint::V2_1::FingerprintAcquiredInfo::ACQUIRED_GOOD;
        }
    }

    Return<android::hardware::biometrics::fingerprint::V2_1::FingerprintError> OppoToAOSPFingerprintError(vendor::oppo::hardware::biometrics::fingerprint::V2_1::FingerprintError error) {
        switch(error) {
            case vendor::oppo::hardware::biometrics::fingerprint::V2_1::FingerprintError::ERROR_NO_ERROR: return android::hardware::biometrics::fingerprint::V2_1::FingerprintError::ERROR_NO_ERROR;
            case vendor::oppo::hardware::biometrics::fingerprint::V2_1::FingerprintError::ERROR_HW_UNAVAILABLE: return android::hardware::biometrics::fingerprint::V2_1::FingerprintError::ERROR_HW_UNAVAILABLE;
            case vendor::oppo::hardware::biometrics::fingerprint::V2_1::FingerprintError::ERROR_UNABLE_TO_PROCESS: return android::hardware::biometrics::fingerprint::V2_1::FingerprintError::ERROR_UNABLE_TO_PROCESS;
            case vendor::oppo::hardware::biometrics::fingerprint::V2_1::FingerprintError::ERROR_TIMEOUT: return android::hardware::biometrics::fingerprint::V2_1::FingerprintError::ERROR_TIMEOUT;
            case vendor::oppo::hardware::biometrics::fingerprint::V2_1::FingerprintError::ERROR_NO_SPACE: return android::hardware::biometrics::fingerprint::V2_1::FingerprintError::ERROR_NO_SPACE;
            case vendor::oppo::hardware::biometrics::fingerprint::V2_1::FingerprintError::ERROR_CANCELED: return android::hardware::biometrics::fingerprint::V2_1::FingerprintError::ERROR_CANCELED;
            case vendor::oppo::hardware::biometrics::fingerprint::V2_1::FingerprintError::ERROR_UNABLE_TO_REMOVE: return android::hardware::biometrics::fingerprint::V2_1::FingerprintError::ERROR_UNABLE_TO_REMOVE;
            case vendor::oppo::hardware::biometrics::fingerprint::V2_1::FingerprintError::ERROR_LOCKOUT: return android::hardware::biometrics::fingerprint::V2_1::FingerprintError::ERROR_LOCKOUT;
            case vendor::oppo::hardware::biometrics::fingerprint::V2_1::FingerprintError::ERROR_VENDOR: return android::hardware::biometrics::fingerprint::V2_1::FingerprintError::ERROR_VENDOR;
            default:
                return android::hardware::biometrics::fingerprint::V2_1::FingerprintError::ERROR_NO_ERROR;
        }
    }
};

Return<bool> BiometricsFingerprint::isUdfps(uint32_t) {
    return true;
}

Return<void> BiometricsFingerprint::onFingerDown(uint32_t, uint32_t, float, float) {
    set(DIMLAYER_PATH, 1);
    set(FP_PRESS_PATH, 1);
    return Void();
}

Return<void> BiometricsFingerprint::onFingerUp() {
    set(FP_PRESS_PATH, 0);
    return Void();
}

Return<uint64_t> BiometricsFingerprint::setNotify(
        const sp<IBiometricsFingerprintClientCallback>& clientCallback) {
    ALOGE("setNotify");
    mOppoClientCallback = new OppoClientCallback(clientCallback);
    return mOppoBiometricsFingerprint->setNotify(mOppoClientCallback);
}

Return<RequestStatus> BiometricsFingerprint::OppoToAOSPRequestStatus(vendor::oppo::hardware::biometrics::fingerprint::V2_1::RequestStatus req) {
    switch(req) {
        case vendor::oppo::hardware::biometrics::fingerprint::V2_1::RequestStatus::SYS_UNKNOWN: return RequestStatus::SYS_UNKNOWN;
        case vendor::oppo::hardware::biometrics::fingerprint::V2_1::RequestStatus::SYS_OK: return RequestStatus::SYS_OK;
        case vendor::oppo::hardware::biometrics::fingerprint::V2_1::RequestStatus::SYS_ENOENT: return RequestStatus::SYS_ENOENT;
        case vendor::oppo::hardware::biometrics::fingerprint::V2_1::RequestStatus::SYS_EINTR: return RequestStatus::SYS_EINTR;
        case vendor::oppo::hardware::biometrics::fingerprint::V2_1::RequestStatus::SYS_EIO: return RequestStatus::SYS_EIO;
        case vendor::oppo::hardware::biometrics::fingerprint::V2_1::RequestStatus::SYS_EAGAIN: return RequestStatus::SYS_EAGAIN;
        case vendor::oppo::hardware::biometrics::fingerprint::V2_1::RequestStatus::SYS_ENOMEM: return RequestStatus::SYS_ENOMEM;
        case vendor::oppo::hardware::biometrics::fingerprint::V2_1::RequestStatus::SYS_EACCES: return RequestStatus::SYS_EACCES;
        case vendor::oppo::hardware::biometrics::fingerprint::V2_1::RequestStatus::SYS_EFAULT: return RequestStatus::SYS_EFAULT;
        case vendor::oppo::hardware::biometrics::fingerprint::V2_1::RequestStatus::SYS_EBUSY: return RequestStatus::SYS_EBUSY;
        case vendor::oppo::hardware::biometrics::fingerprint::V2_1::RequestStatus::SYS_EINVAL: return RequestStatus::SYS_EINVAL;
        case vendor::oppo::hardware::biometrics::fingerprint::V2_1::RequestStatus::SYS_ENOSPC: return RequestStatus::SYS_ENOSPC;
        case vendor::oppo::hardware::biometrics::fingerprint::V2_1::RequestStatus::SYS_ETIMEDOUT: return RequestStatus::SYS_ETIMEDOUT;
        default:
            return RequestStatus::SYS_UNKNOWN;
    }
}

Return<uint64_t> BiometricsFingerprint::preEnroll()  {
    ALOGE("preEnroll");
    return mOppoBiometricsFingerprint->preEnroll();
}

Return<RequestStatus> BiometricsFingerprint::enroll(const hidl_array<uint8_t, 69>& hat,
    uint32_t gid, uint32_t timeoutSec)  {
    if (!mFodCircleVisible) {
        dcDimState = get(DC_DIM_PATH, 0);
        set(DC_DIM_PATH, 0);
    }
    if (get(POWER_STATUS, 3) || get(POWER_STATUS, 4)) {
	    set(NOTIFY_BLANK_PATH, 1);
        set(AOD_MODE_PATH, 0);
	}
    mFodCircleVisible = true;
    set(DIMLAYER_PATH, 1);
    return OppoToAOSPRequestStatus(mOppoBiometricsFingerprint->enroll(hat, gid, timeoutSec));
}

Return<RequestStatus> BiometricsFingerprint::postEnroll()  {
    if (mFodCircleVisible) {
        set(DC_DIM_PATH, dcDimState);
    }

    set(DIMLAYER_PATH, 0);
	set(FP_PRESS_PATH, 0);
    mFodCircleVisible = false;
    return OppoToAOSPRequestStatus(mOppoBiometricsFingerprint->postEnroll());
}

Return<uint64_t> BiometricsFingerprint::getAuthenticatorId()  {
    ALOGE("getAuthId");
    return mOppoBiometricsFingerprint->getAuthenticatorId();
}

Return<RequestStatus> BiometricsFingerprint::cancel()  {
    if (mFodCircleVisible) {
        set(DC_DIM_PATH, dcDimState);
    }

    set(DIMLAYER_PATH, 0);
	set(FP_PRESS_PATH, 0);
    mFodCircleVisible = false;

    if (mOppoBiometricsFingerprint->cancel() == vendor::oppo::hardware::biometrics::fingerprint::V2_1::RequestStatus::SYS_OK) {
        mOppoClientCallback->onError(mOppoBiometricsFingerprint->setNotify(mOppoClientCallback), vendor::oppo::hardware::biometrics::fingerprint::V2_1::FingerprintError::ERROR_CANCELED, 0);
    }
    return OppoToAOSPRequestStatus(mOppoBiometricsFingerprint->cancel());
}

Return<RequestStatus> BiometricsFingerprint::enumerate()  {
	RequestStatus ret = OppoToAOSPRequestStatus(mOppoBiometricsFingerprint->enumerate());
    ALOGE("ENUMERATING!");
	mOppoBiometricsFingerprint->setActiveGroup(lastGid, lastStorePath);
    return ret;
}

Return<RequestStatus> BiometricsFingerprint::remove(uint32_t gid, uint32_t fid)  {
    ALOGE("remove");
    return OppoToAOSPRequestStatus(mOppoBiometricsFingerprint->remove(gid, fid));
}

Return<RequestStatus> BiometricsFingerprint::setActiveGroup(uint32_t gid,
    const hidl_string& storePath)  {
    ALOGE("setActiveGroup");
	lastStorePath = storePath;
	lastGid = gid;
    return OppoToAOSPRequestStatus(mOppoBiometricsFingerprint->setActiveGroup(gid, storePath));
}

Return<RequestStatus> BiometricsFingerprint::authenticate(uint64_t operationId, uint32_t gid)  {
    if (!mFodCircleVisible) {
        dcDimState = get(DC_DIM_PATH, 0);
        set(DC_DIM_PATH, 0);
    }
    mFodCircleVisible = true;

    set(DIMLAYER_PATH, 1);
	set(FP_PRESS_PATH, 0);
    ALOGE("auth");
    return OppoToAOSPRequestStatus(mOppoBiometricsFingerprint->authenticate(operationId, gid));
}

}  // namespace implementation
}  // namespace V2_3
}  // namespace fingerprint
}  // namespace biometrics
}  // namespace hardware
}  // namespace android
