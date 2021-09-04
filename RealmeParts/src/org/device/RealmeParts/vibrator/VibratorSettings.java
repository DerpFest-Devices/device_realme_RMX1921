package org.device.RealmeParts.vibrator;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.preference.PreferenceFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;

import org.device.RealmeParts.vibrator.utils.VibrationSeekBarPreference;
import org.device.RealmeParts.vibrator.utils.VibrateFileUtils;

import org.device.RealmeParts.R;

public class VibratorSettings extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    public static final String PREF_CALL_LEVEL = "vibration_strength_call";
    public static final String PREF_NOTIFICATION_LEVEL = "vibration_strength_notification";
    public static final String PREF_USER_LEVEL = "vibration_strength_system";
    public static final String CALL_LEVEL = "/sys/devices/platform/soc/c440000.qcom,spmi/spmi-0/spmi0-01/c440000.qcom,spmi:qcom,pm660@1:qcom,haptics@c000/leds/vibrator/vmax_mv_call";
    public static final String NOTIFICATION_LEVEL = "/sys/devices/platform/soc/c440000.qcom,spmi/spmi-0/spmi0-01/c440000.qcom,spmi:qcom,pm660@1:qcom,haptics@c000/leds/vibrator/vmax_mv_strong";
    public static final String USER_LEVEL = "/sys/devices/platform/soc/c440000.qcom,spmi/spmi-0/spmi0-01/c440000.qcom,spmi:qcom,pm660@1:qcom,haptics@c000/leds/vibrator/vmax_mv_user";

    private VibrationSeekBarPreference mCallVib;
    private VibrationSeekBarPreference mNotificationVib;
    private VibrationSeekBarPreference mUserVib;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.vibration_setting, rootKey);

        mCallVib = (VibrationSeekBarPreference) findPreference(PREF_CALL_LEVEL);
        mCallVib.setOnPreferenceChangeListener(this);

        mNotificationVib = (VibrationSeekBarPreference) findPreference(PREF_NOTIFICATION_LEVEL);
        mNotificationVib.setOnPreferenceChangeListener(this);

        mUserVib = (VibrationSeekBarPreference) findPreference(PREF_USER_LEVEL);
        mUserVib.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final String key = preference.getKey();
        int value = (Integer) newValue;
        switch (key) {
            case PREF_CALL_LEVEL:
                VibrateFileUtils.setValue(CALL_LEVEL, value);
                break;

            case PREF_NOTIFICATION_LEVEL:
                 VibrateFileUtils.setValue(NOTIFICATION_LEVEL, value);
                 break;

            case PREF_USER_LEVEL:
                 VibrateFileUtils.setValue(USER_LEVEL, value);
                 break;
            default:
                break;
        }
        return true;
    }
}
