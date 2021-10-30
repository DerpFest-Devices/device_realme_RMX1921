/*
* Copyright (C) 2013 The OmniROM Project
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
package org.device.RealmeParts;

import android.app.Activity;
import android.provider.Settings;
import android.text.TextUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.os.UserHandle;
import androidx.preference.PreferenceManager;
import org.device.RealmeParts.ModeSwitch.GameModeSwitch;
import org.device.RealmeParts.Touch.util.Utils;
import org.device.RealmeParts.Touch.ScreenOffGesture;
import org.device.RealmeParts.audio.SoundControlSettings;
import org.device.RealmeParts.audio.SoundControlFileUtils;
import org.device.RealmeParts.kcal.KcalService;
import org.device.RealmeParts.vibrator.VibratorSettings;
import org.device.RealmeParts.vibrator.utils.VibrateFileUtils;

public class Startup extends BroadcastReceiver {

    private boolean mHBM = false;
    private static final String TAG = "BootReceiver";
    private static final String ONE_TIME_TUNABLE_RESTORE = "hardware_tunable_restored";
    private static boolean mServiceEnabled = false;
    private static final String SEED_PATH = "/sys/kernel/oppo_display/seed";

    private void restore(String file, boolean enabled) {
        if (file == null) {
            return;
        }
        if (enabled) {
            Utils.writeValue(file, "1");
        }
    }

    private void restore(String file, String value) {
        if (file == null) {
            return;
        }
        Utils.writeValue(file, value);
    }

    @Override
    public void onReceive(final Context context, final Intent bootintent) {
        boolean enabled = false;
        int value = 0;
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences (context);
        enabled = sharedPrefs.getBoolean (RealmeParts.KEY_GAME_SWITCH, false);
        restore (GameModeSwitch.getFile ( ), enabled);

        enableComponent(context, ScreenOffGesture.class.getName());
        SharedPreferences screenOffGestureSharedPreferences = context.getSharedPreferences(
                Utils.PREFERENCES, Activity.MODE_PRIVATE);
        KernelControl.enableGestures(
                screenOffGestureSharedPreferences.getBoolean(
                        ScreenOffGesture.PREF_GESTURE_ENABLE, true));
        KernelControl.enableDt2w(
                screenOffGestureSharedPreferences.getBoolean(
                        ScreenOffGesture.PREF_DT2W_ENABLE, true));

        Intent kcalIntent  = new Intent(context , KcalService.class);
        context.startService(kcalIntent);

        enabled = sharedPrefs.getBoolean(RealmeParts.KEY_DC_SWITCH, false);
        restore(DCModeSwitch.getFile(), enabled);
        enabled = sharedPrefs.getBoolean(RealmeParts.KEY_HBM_SWITCH, false);
        restore(HBMModeSwitch.getFile(), enabled);
        value = sharedPrefs.getInt(RealmeParts.KEY_SEED, 0);
        Utils.writeValue(SEED_PATH, Integer.toString(value));
        enabled = sharedPrefs.getBoolean(RealmeParts.KEY_OTG_SWITCH, false);
        restore(OTGModeSwitch.getFile(), enabled);
        enableService(context);

        int gain = Settings.Secure.getInt(context.getContentResolver(),
                SoundControlSettings.PREF_HEADPHONE_GAIN, 4);
        SoundControlFileUtils.setValue(SoundControlSettings.HEADPHONE_GAIN_PATH, gain + " " + gain);
        SoundControlFileUtils.setValue(SoundControlSettings.MICROPHONE_GAIN_PATH, Settings.Secure.getInt(context.getContentResolver(),
                SoundControlSettings.PREF_MICROPHONE_GAIN, 0));
        VibrateFileUtils.setValue(VibratorSettings.CALL_LEVEL, Settings.Secure.getInt(context.getContentResolver(),
                VibratorSettings.PREF_CALL_LEVEL, 3596));
        VibrateFileUtils.setValue(VibratorSettings.NOTIFICATION_LEVEL, Settings.Secure.getInt(context.getContentResolver(),
                VibratorSettings.PREF_NOTIFICATION_LEVEL, 3596));
        VibrateFileUtils.setValue(VibratorSettings.USER_LEVEL, Settings.Secure.getInt(context.getContentResolver(),
                VibratorSettings.PREF_USER_LEVEL, 3300));
    }

    private void enableComponent(Context context, String name) {
    }

    private boolean hasRestoredTunable(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(ONE_TIME_TUNABLE_RESTORE, false);
    }

    private void setRestoredTunable(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean(ONE_TIME_TUNABLE_RESTORE, true).apply();

        }

    private static void startService(Context context) {
        context.startServiceAsUser(new Intent(context, AutoHighBrightnessModeService.class),
                UserHandle.CURRENT);
        mServiceEnabled = true;
    }

    private static void stopService(Context context) {
        mServiceEnabled = false;
        context.stopServiceAsUser(new Intent(context, AutoHighBrightnessModeService.class),
                UserHandle.CURRENT);
    }

    public static void enableService(Context context) {
        if (RealmeParts.isHBMAutobrightnessEnabled(context) && !mServiceEnabled) {
            startService(context);
        } else if (!RealmeParts.isHBMAutobrightnessEnabled(context) && mServiceEnabled) {
            stopService(context);
        }
    }
    }
