package org.device.RealmeParts.vibrator.utils;

import android.content.ContentResolver;
import android.preference.PreferenceDataStore;
import android.provider.Settings;

public class SecureSettingsStore extends androidx.preference.PreferenceDataStore
        implements PreferenceDataStore {

    private final ContentResolver mContentResolver;

    public SecureSettingsStore(ContentResolver contentResolver) {
        mContentResolver = contentResolver;
    }

    public boolean getBoolean(String key, boolean defValue) {
        return getInt(key, defValue ? 1 : 0) != 0;
    }

    public float getFloat(String key, float defValue) {
        return Settings.Secure.getFloat(mContentResolver, key, defValue);
    }

    public int getInt(String key, int defValue) {
        return Settings.Secure.getInt(mContentResolver, key, defValue);
    }

    public long getLong(String key, long defValue) {
        return Settings.Secure.getLong(mContentResolver, key, defValue);
    }

    public String getString(String key, String defValue) {
        String result = Settings.Secure.getString(mContentResolver, key);
        return result == null ? defValue : result;
    }

    public void putBoolean(String key, boolean value) {
        putInt(key, value ? 1 : 0);
    }

    public void putFloat(String key, float value) {
        Settings.Secure.putFloat(mContentResolver, key, value);
    }

    public void putInt(String key, int value) {
        Settings.Secure.putInt(mContentResolver, key, value);
    }

    public void putLong(String key, long value) {
        Settings.Secure.putLong(mContentResolver, key, value);
    }

    public void putString(String key, String value) {
        Settings.Secure.putString(mContentResolver, key, value);
    }
}
