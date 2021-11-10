/*
* Copyright (C) 2016 The OmniROM Project
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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.util.Log;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import androidx.preference.TwoStatePreference;


import org.device.RealmeParts.ModeSwitch.GameModeSwitch;
import org.device.RealmeParts.Touch.ScreenOffGestureSettings;
import org.device.RealmeParts.audio.SoundControlSettingsActivity;
import org.device.RealmeParts.kcal.DisplayCalibration;
import org.device.RealmeParts.preferences.CustomSeekBarPreference;
import org.device.RealmeParts.preferences.SecureSettingListPreference;
import org.device.RealmeParts.preferences.SecureSettingSwitchPreference;
import org.device.RealmeParts.Startup;

public class RealmeParts extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String KEY_CATEGORY_GRAPHICS = "graphics";

    public static final String KEY_PERFORMANCE = "perf_tuner";
    public static final String KEY_SEED = "seed";
    public static final String KEY_HBM_SWITCH = "hbm";
    public static final String KEY_HBM_AUTOBRIGHTNESS_SWITCH = "hbm_autobrightness";
    public static final String KEY_HBM_AUTOBRIGHTNESS_THRESHOLD = "hbm_autobrightness_threshould";
    public static final String KEY_DC_SWITCH = "dc";
    public static final String KEY_OTG_SWITCH = "otg";
    public static final String KEY_GAME_SWITCH = "game";
    public static final String TP_LIMIT_ENABLE = "/proc/touchpanel/oppo_tp_limit_enable";
    public static final String TP_DIRECTION = "/proc/touchpanel/oppo_tp_direction";
    private static final String SEED_PATH = "/sys/kernel/oppo_display/seed";
    public static final String KEY_HEADS_UP = "headsupstatus";
    public static final String KEY_DND_SWITCH = "dnd";
    public static final String KEY_BACKUP_PERF = "perfoldstate";
    public static final String KEY_GAMING_PERF = "gamingperf";

    public static final String KEY_SETTINGS_PREFIX = "RealmeParts";
    private static TwoStatePreference mEnableDolbyAtmos;
    private static TwoStatePreference mHBMModeSwitch;
    private static TwoStatePreference mHBMAutobrightnessSwitch;
    private static TwoStatePreference mDCModeSwitch;
    protected static SecureSettingListPreference mSeedModeSwitch;
    private static TwoStatePreference mOTGModeSwitch;
    private static TwoStatePreference mGameModeSwitch;
    public static TwoStatePreference mDNDSwitch;
    private static TwoStatePreference mGamePerfSwitch;
    private Preference mGesturesPref;
    private Preference mKcalPref;
    private Preference mAudioPref;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        addPreferencesFromResource(R.xml.Realme_preferences);

        mDCModeSwitch = (TwoStatePreference) findPreference(KEY_DC_SWITCH);
        mDCModeSwitch.setEnabled(DCModeSwitch.isSupported());
        mDCModeSwitch.setChecked(DCModeSwitch.isCurrentlyEnabled(this.getContext()));
        mDCModeSwitch.setOnPreferenceChangeListener(new DCModeSwitch());

        mHBMModeSwitch = (TwoStatePreference) findPreference(KEY_HBM_SWITCH);
        mHBMModeSwitch.setEnabled(HBMModeSwitch.isSupported());
        mHBMModeSwitch.setChecked(HBMModeSwitch.isCurrentlyEnabled(this.getContext()));
        mHBMModeSwitch.setOnPreferenceChangeListener(new HBMModeSwitch());
        mHBMAutobrightnessSwitch = (TwoStatePreference) findPreference(KEY_HBM_AUTOBRIGHTNESS_SWITCH);
        mHBMAutobrightnessSwitch.setChecked(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(RealmeParts.KEY_HBM_AUTOBRIGHTNESS_SWITCH, false));
        mHBMAutobrightnessSwitch.setOnPreferenceChangeListener(this);

        mSeedModeSwitch = (SecureSettingListPreference) findPreference(KEY_SEED);
        mSeedModeSwitch.setValue(Utils.getFileValue(SEED_PATH, "0"));
        mSeedModeSwitch.setSummary(mSeedModeSwitch.getEntry());
        mSeedModeSwitch.setOnPreferenceChangeListener(this);

        mOTGModeSwitch = (TwoStatePreference) findPreference(KEY_OTG_SWITCH);
        mOTGModeSwitch.setEnabled(OTGModeSwitch.isSupported());
        mOTGModeSwitch.setChecked(OTGModeSwitch.isCurrentlyEnabled(this.getContext()));
        mOTGModeSwitch.setOnPreferenceChangeListener(new OTGModeSwitch());

        mGameModeSwitch = (TwoStatePreference) findPreference(KEY_GAME_SWITCH);
        mGameModeSwitch.setEnabled(GameModeSwitch.isSupported());
        mGameModeSwitch.setChecked(GameModeSwitch.isCurrentlyEnabled(this.getContext()));
        mGameModeSwitch.setOnPreferenceChangeListener(new GameModeSwitch(this.getContext()));

        mDNDSwitch = (TwoStatePreference) findPreference(KEY_DND_SWITCH);
        mDNDSwitch.setChecked(prefs.getBoolean(KEY_DND_SWITCH, false));
        mDNDSwitch.setOnPreferenceChangeListener(this);

        mGamePerfSwitch = (TwoStatePreference) findPreference(KEY_GAMING_PERF);
        mGamePerfSwitch.setChecked(prefs.getBoolean(KEY_GAMING_PERF, false));
        mGamePerfSwitch.setOnPreferenceChangeListener(this);

        mGesturesPref = findPreference("screen_gestures");
                mGesturesPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                     @Override
                     public boolean onPreferenceClick(Preference preference) {
                         Intent intent = new Intent(getContext(), ScreenOffGestureSettings.class);
                         startActivity(intent);
                         return true;
                     }
                });

        mKcalPref = findPreference("kcal");
                mKcalPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                     @Override
                     public boolean onPreferenceClick(Preference preference) {
                         Intent intent = new Intent(getContext(), DisplayCalibration.class);
                         startActivity(intent);
                         return true;
                     }
                });

        mAudioPref = findPreference("sound");
                mAudioPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                     @Override
                     public boolean onPreferenceClick(Preference preference) {
                         Intent intent = new Intent(getContext(), SoundControlSettingsActivity.class);
                         startActivity(intent);
                         return true;
                     }
                });
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        final String key = preference.getKey();
        if (preference == mHBMAutobrightnessSwitch) {
                    Boolean enabled = (Boolean) value;
                    SharedPreferences.Editor prefChange = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                    prefChange.putBoolean(KEY_HBM_AUTOBRIGHTNESS_SWITCH, enabled).commit();
                    Startup.enableService(getContext());
                }
        if (preference == mSeedModeSwitch){
            int thisvalue = Integer.valueOf((String) value);
            mSeedModeSwitch.setValue((String) value);
            mSeedModeSwitch.setSummary(mSeedModeSwitch.getEntry());
            SharedPreferences.Editor prefChange = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
            prefChange.putInt(KEY_SEED, thisvalue).commit();
            Utils.writeValue(SEED_PATH, (String) value);
        }
        return true;
    }

    public static boolean isHBMAutobrightnessEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(RealmeParts.KEY_HBM_AUTOBRIGHTNESS_SWITCH, false);
    }

    private boolean isAppNotInstalled(String uri) {
        PackageManager packageManager = getContext().getPackageManager();
        try {
            packageManager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            return true;
        }
    }
}
