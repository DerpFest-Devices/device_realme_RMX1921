/*
* Copyright (C) 2020 The LineageOS Project
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
*/

package org.device.RealmeParts.ModeSwitch;

import androidx.preference.Preference;
import android.app.Notification;
import android.app.NotificationManager;
import android.util.Log;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.Settings.System;
import android.provider.Settings.Global;

import org.device.RealmeParts.Utils;
import org.device.RealmeParts.RealmeParts;

public class GameModeSwitch implements Preference.OnPreferenceChangeListener {

    private static final String FILE = "/proc/touchpanel/game_switch_enable";
    private static final String TAG = Utils.class.getSimpleName();

    private NotificationManager mNotificationManager;
    private Context mContext;
    private static final String GAMING_SCREEN_BRIGHTNESS_MODE = "gaming_screen_brightness_mode";
    private boolean HeadsUpOldState;
    private boolean DnD;
    private boolean Perf;
    private int PerfOldState;

    public GameModeSwitch(Context context) {
        mContext = context;
    }

    public static String getFile() {
        if (Utils.fileWritable(FILE)) {
            return FILE;
        }
        return null;
    }

    public static boolean isSupported() {
        return Utils.fileWritable(getFile());
    }

    public static boolean isCurrentlyEnabled(Context context) {
        return Utils.getFileValueAsBoolean(getFile(), false);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Boolean enabled = (Boolean) newValue;
        Utils.writeValue(getFile(), enabled ? "1" : "0");
        Utils.writeValue(org.device.RealmeParts.RealmeParts.TP_LIMIT_ENABLE, enabled ? "0" : "1" );
        Utils.writeValue(org.device.RealmeParts.RealmeParts.TP_DIRECTION, enabled ? "1" : "0" );
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        boolean status = (Boolean) newValue;
        if (sharedPrefs.getBoolean("dnd", false))
            DnD = true;
        else 
            DnD = false;
        if (sharedPrefs.getBoolean("gamingperf", false))
            Perf = true;
        else 
            Perf = false;
        if (status && Perf){
            PerfOldState = sharedPrefs.getInt (RealmeParts.KEY_PERFORMANCE, 0);
            sharedPrefs.edit().putInt(RealmeParts.KEY_BACKUP_PERF, PerfOldState).commit();
            sharedPrefs.edit().putInt(RealmeParts.KEY_PERFORMANCE, 3).commit();
        } else if (!status && Perf){
            PerfOldState = sharedPrefs.getInt (RealmeParts.KEY_BACKUP_PERF, 0);
            sharedPrefs.edit().putInt(RealmeParts.KEY_PERFORMANCE, PerfOldState).commit();
        }
        if (status && DnD) {
            final boolean isHeadsUpEnabledByUser = Settings.Global.getInt(mContext.getContentResolver(),
                              Settings.Global.HEADS_UP_NOTIFICATIONS_ENABLED, 1) == 1;
            HeadsUpOldState = isHeadsUpEnabledByUser;
            sharedPrefs.edit().putBoolean(RealmeParts.KEY_HEADS_UP, isHeadsUpEnabledByUser).commit();
            Settings.Global.putInt(mContext.getContentResolver(), Settings.Global.HEADS_UP_NOTIFICATIONS_ENABLED, 0);
            Log.d(TAG, "HEADS_UP_NOTIFICATIONS disabled, was:" + HeadsUpOldState);
        } else if (!status && DnD) {
            final boolean wasHeadsUpEnabledByUser = sharedPrefs.getBoolean(RealmeParts.KEY_HEADS_UP, true);
            Log.d(TAG, "HEADS_UP_NOTIFICATIONS enabled, was:" + HeadsUpOldState);

            if (wasHeadsUpEnabledByUser)
                Settings.Global.putInt(mContext.getContentResolver(),
                    Settings.Global.HEADS_UP_NOTIFICATIONS_ENABLED, 1);
        }
        Log.e(TAG, "game mode set to " + enabled);
        return true;
    }
}
