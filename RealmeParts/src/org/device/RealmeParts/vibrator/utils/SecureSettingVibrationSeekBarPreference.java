package org.device.RealmeParts.vibrator.utils;

import android.content.Context;
import android.util.AttributeSet;

public class SecureSettingVibrationSeekBarPreference extends VibrationSeekBarPreference {

    public SecureSettingVibrationSeekBarPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setPreferenceDataStore(new SecureSettingsStore(context.getContentResolver()));
    }

    public SecureSettingVibrationSeekBarPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setPreferenceDataStore(new SecureSettingsStore(context.getContentResolver()));
    }

    public SecureSettingVibrationSeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPreferenceDataStore(new SecureSettingsStore(context.getContentResolver()));
    }

    public SecureSettingVibrationSeekBarPreference(Context context) {
        super(context);
        setPreferenceDataStore(new SecureSettingsStore(context.getContentResolver()));
    }
} 
