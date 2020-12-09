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
package com.Galaxy.Features.kcal;

import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.Matrix;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.Galaxy.Features.R;
import com.Galaxy.Features.kcal.utils.SeekBarPreference;

public class DisplayCalibration extends PreferenceActivity implements
        OnPreferenceChangeListener {

    public static final String KEY_KCAL_ENABLED = "kcal_enabled";
    public static final String KEY_KCAL_RED = "kcal_red";
    public static final String KEY_KCAL_GREEN = "kcal_green";
    public static final String KEY_KCAL_BLUE = "kcal_blue";
    public static final String KEY_KCAL_SATURATION = "kcal_saturation";
    public static final String KEY_KCAL_CONTRAST = "kcal_contrast";
    public static final String KEY_KCAL_COLOR_TEMP = "kcal_color_temp";
    public static final String KEY_KCAL_HUE = "kcal_hue";
    public static final String KEY_KCAL_VALUE = "kcal_value";
    public static final String KEY_KCAL_GREYSCALE = "kcal_greyscale";
    public static final String KEY_KCAL_PRESETS_LIST = "presets_list";

    private SeekBarPreference mKcalRed;
    private SeekBarPreference mKcalBlue;
    private SeekBarPreference mKcalGreen;
    private SeekBarPreference mKcalSaturation;
    private SeekBarPreference mKcalContrast;
    private SeekBarPreference mKcalColorTemp;
    private SharedPreferences mPrefs;
    private SwitchPreference mKcalEnabled;
    private SeekBarPreference mKcalHue;
    private SeekBarPreference mKcalValue;
    private SwitchPreference mKcalGreyscale;
    private ListPreference mKcalPresetsListPreference;
    private boolean mEnabled;

    private String mRed;
    private String mBlue;
    private String mGreen;

    private static final String COLOR_FILE = "/sys/module/msm_drm/parameters/kcal";
    private static final String COLOR_FILE_CONTRAST = "/sys/module/msm_drm/parameters/kcal_cont";
    private static final String COLOR_FILE_SATURATION = "/sys/module/msm_drm/parameters/kcal_sat";
    private static final String COLOR_FILE_ENABLE = "/sys/module/msm_drm/parameters/kcal_enable";
    private static final String COLOR_FILE_HUE = "/sys/module/msm_drm/parameters/kcal_hue";
    private static final String COLOR_FILE_VALUE = "/sys/module/msm_drm/parameters/kcal_val";

    private static Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.display_cal);

        ImageView imageView = (ImageView) findViewById(R.id.calibration_pic);
        imageView.setImageResource(R.drawable.calibration_png);

        addPreferencesFromResource(R.xml.display_calibration);

        mKcalEnabled = (SwitchPreference) findPreference(KEY_KCAL_ENABLED);
        mKcalEnabled.setChecked(mPrefs.getBoolean(DisplayCalibration.KEY_KCAL_ENABLED, false));
        mKcalEnabled.setOnPreferenceChangeListener(this);

        mKcalRed = (SeekBarPreference) findPreference(KEY_KCAL_RED);
        mKcalRed.setInitValue(mPrefs.getInt(KEY_KCAL_RED, mKcalRed.def));
        mKcalRed.setOnPreferenceChangeListener(this);

        mKcalGreen = (SeekBarPreference) findPreference(KEY_KCAL_GREEN);
        mKcalGreen.setInitValue(mPrefs.getInt(KEY_KCAL_GREEN, mKcalGreen.def));
        mKcalGreen.setOnPreferenceChangeListener(this);

        mKcalBlue = (SeekBarPreference) findPreference(KEY_KCAL_BLUE);
        mKcalBlue.setInitValue(mPrefs.getInt(KEY_KCAL_BLUE, mKcalBlue.def));
        mKcalBlue.setOnPreferenceChangeListener(this);

        mKcalSaturation = (SeekBarPreference) findPreference(KEY_KCAL_SATURATION);
        mKcalSaturation.setInitValue(mPrefs.getInt(KEY_KCAL_SATURATION, mKcalSaturation.def));
        mKcalSaturation.setOnPreferenceChangeListener(this);

        mKcalContrast = (SeekBarPreference) findPreference(KEY_KCAL_CONTRAST);
        mKcalContrast.setInitValue(mPrefs.getInt(KEY_KCAL_CONTRAST, mKcalContrast.def));
        mKcalContrast.setOnPreferenceChangeListener(this);

        mKcalHue = (SeekBarPreference) findPreference(KEY_KCAL_HUE);
        mKcalHue.setInitValue(mPrefs.getInt(KEY_KCAL_HUE, mKcalHue.def));
        mKcalHue.setOnPreferenceChangeListener(this);

        mKcalColorTemp = (SeekBarPreference) findPreference(KEY_KCAL_COLOR_TEMP);
        mKcalColorTemp.setInitValue(mPrefs.getInt(KEY_KCAL_COLOR_TEMP, mKcalColorTemp.def));
        mKcalColorTemp.setOnPreferenceChangeListener(this);

        mKcalValue = (SeekBarPreference) findPreference(KEY_KCAL_VALUE);
        mKcalValue.setInitValue(mPrefs.getInt(KEY_KCAL_VALUE, mKcalValue.def));
        mKcalValue.setOnPreferenceChangeListener(this);

        mKcalGreyscale = (SwitchPreference) findPreference(KEY_KCAL_GREYSCALE);
        mKcalGreyscale.setChecked(mPrefs.getBoolean(KEY_KCAL_GREYSCALE, false));
        mKcalGreyscale.setOnPreferenceChangeListener(this);

        mRed = String.valueOf(mPrefs.getInt(KEY_KCAL_RED, mKcalRed.def));
        mGreen = String.valueOf(mPrefs.getInt(KEY_KCAL_GREEN, mKcalGreen.def));
        mBlue = String.valueOf(mPrefs.getInt(KEY_KCAL_BLUE, mKcalBlue.def));

        mKcalPresetsListPreference = (ListPreference) findPreference(KEY_KCAL_PRESETS_LIST);
        String kcalPresetsValue = mPrefs.getString(KEY_KCAL_PRESETS_LIST, "0");
        mKcalPresetsListPreference.setValue(kcalPresetsValue);
        mKcalPresetsListPreference.setOnPreferenceChangeListener(this);
    }

    private boolean isSupported(String file) {
        return Utils.fileWritable(file);
    }

    public static void restore(Context context) {
       boolean storeEnabled = PreferenceManager
                .getDefaultSharedPreferences(context).getBoolean(DisplayCalibration.KEY_KCAL_ENABLED, false);
       if (storeEnabled) {
           Utils.writeValue(COLOR_FILE_ENABLE, "1");
           Utils.writeValue(COLOR_FILE, "1");
           int storedRed = PreferenceManager
                   .getDefaultSharedPreferences(context).getInt(DisplayCalibration.KEY_KCAL_RED, 256);
           int storedGreen = PreferenceManager
                   .getDefaultSharedPreferences(context).getInt(DisplayCalibration.KEY_KCAL_GREEN, 256);
           int storedBlue = PreferenceManager
                   .getDefaultSharedPreferences(context).getInt(DisplayCalibration.KEY_KCAL_BLUE, 256);
           int storedSaturation = PreferenceManager
                   .getDefaultSharedPreferences(context).getInt(DisplayCalibration.KEY_KCAL_SATURATION, 255);
           int storedContrast = PreferenceManager
                   .getDefaultSharedPreferences(context).getInt(DisplayCalibration.KEY_KCAL_CONTRAST, 255);
           int storedHue = PreferenceManager
                   .getDefaultSharedPreferences(context).getInt(DisplayCalibration.KEY_KCAL_HUE, 0);
           int storedVal = PreferenceManager
                   .getDefaultSharedPreferences(context).getInt(DisplayCalibration.KEY_KCAL_VALUE, 255);
           boolean storedGreyscale = PreferenceManager
                   .getDefaultSharedPreferences(context).getBoolean(DisplayCalibration.KEY_KCAL_GREYSCALE, false);
           String storedValue = ((String) String.valueOf(storedRed)
                   + " " + String.valueOf(storedGreen) + " " +  String.valueOf(storedBlue));
           Utils.writeValue(COLOR_FILE, storedValue);
           Utils.writeValue(COLOR_FILE_CONTRAST, String.valueOf(storedContrast));
           Utils.writeValue(COLOR_FILE_SATURATION, storedGreyscale ? "128" : String.valueOf(storedSaturation));
           Utils.writeValue(COLOR_FILE_HUE, String.valueOf(storedHue));
           Utils.writeValue(COLOR_FILE_VALUE, String.valueOf(storedVal));
       }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.kcal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_reset:
                reset();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void reset() {
        int red = mKcalRed.reset();
        int green = mKcalGreen.reset();
        int blue = mKcalBlue.reset();
        int saturation = mKcalSaturation.reset();
        int contrast = mKcalContrast.reset();
        int hue = mKcalHue.reset();
        int value = mKcalValue.reset();
        boolean greyscale = false;
        String preset = "0";

        mKcalGreyscale.setChecked(greyscale);
	mKcalPresetsListPreference.setValue(preset);

        mPrefs.edit().putInt(KEY_KCAL_RED, red).commit();
        mPrefs.edit().putInt(KEY_KCAL_GREEN, green).commit();
        mPrefs.edit().putInt(KEY_KCAL_BLUE, blue).commit();
        mPrefs.edit().putInt(KEY_KCAL_SATURATION, saturation).commit();
        mPrefs.edit().putInt(KEY_KCAL_CONTRAST, contrast).commit();
        mPrefs.edit().putInt(KEY_KCAL_HUE, hue).commit();
        mPrefs.edit().putInt(KEY_KCAL_VALUE, value).commit();
        mPrefs.edit().putBoolean(KEY_KCAL_GREYSCALE, greyscale).commit();
	mPrefs.edit().putString(KEY_KCAL_PRESETS_LIST, preset).commit();

        String storedValue = Integer.toString(red) + " " + Integer.toString(green) + " " +  Integer.toString(blue);

        Utils.writeValue(COLOR_FILE, storedValue);
        Utils.writeValue(COLOR_FILE_SATURATION, Integer.toString(saturation));
        Utils.writeValue(COLOR_FILE_CONTRAST, Integer.toString(contrast));
        Utils.writeValue(COLOR_FILE_HUE, String.valueOf(hue));
        Utils.writeValue(COLOR_FILE_VALUE, String.valueOf(value));

        int cct = Utils.KfromRGB(mPrefs.getInt(KEY_KCAL_RED, 256), mPrefs.getInt(KEY_KCAL_GREEN, 256), mPrefs.getInt(KEY_KCAL_BLUE, 256));
        mKcalColorTemp.setValue(cct);
    }

    public static void setValueRGB(String red, String green, String blue) {
        float valRed = Float.parseFloat((String) red);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(KEY_KCAL_RED, (int) valRed).commit();
        float valGreen = Float.parseFloat((String) green);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(KEY_KCAL_GREEN, (int) valGreen).commit();
        float valBlue = Float.parseFloat((String) blue);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(KEY_KCAL_BLUE, (int) valBlue).commit();
        Utils.writeValue(COLOR_FILE, red + " " + green + " " + blue);
    }

    public static void setValueSat(String newValue) {
        float valSat = Float.parseFloat((String) newValue);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(KEY_KCAL_SATURATION, (int) valSat).commit();
        Utils.writeValue(COLOR_FILE_SATURATION, newValue);
    }

    public static void setValueCon(String newValue) {
        float valCon = Float.parseFloat((String) newValue);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(KEY_KCAL_CONTRAST, (int) valCon).commit();
        Utils.writeValue(COLOR_FILE_CONTRAST, newValue);
    }

    public static void setValueHue(String newValue) {
        float valHue = Float.parseFloat((String) newValue);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(KEY_KCAL_HUE, (int) valHue).commit();
        Utils.writeValue(COLOR_FILE_HUE, newValue);
    }

    public static void setValueVal(String newValue) {
        float valVal = Float.parseFloat((String) newValue);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(KEY_KCAL_VALUE, (int) valVal).commit();
        Utils.writeValue(COLOR_FILE_VALUE, newValue);
    }

    private void refresh() {
        mKcalRed.setInitValue(mPrefs.getInt(KEY_KCAL_RED, mKcalRed.def));
        mKcalGreen.setInitValue(mPrefs.getInt(KEY_KCAL_GREEN, mKcalGreen.def));
        mKcalBlue.setInitValue(mPrefs.getInt(KEY_KCAL_BLUE, mKcalBlue.def));
        mKcalSaturation.setInitValue(mPrefs.getInt(KEY_KCAL_SATURATION, mKcalSaturation.def));
        mKcalContrast.setInitValue(mPrefs.getInt(KEY_KCAL_CONTRAST, mKcalContrast.def));
        mKcalHue.setInitValue(mPrefs.getInt(KEY_KCAL_HUE, mKcalHue.def));
        mKcalValue.setInitValue(mPrefs.getInt(KEY_KCAL_VALUE, mKcalValue.def));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mKcalEnabled) {
            Boolean enabled = (Boolean) newValue;
            mPrefs.edit().putBoolean(KEY_KCAL_ENABLED, enabled).commit();
            mRed = String.valueOf(mPrefs.getInt(KEY_KCAL_RED, 256));
            mBlue = String.valueOf(mPrefs.getInt(KEY_KCAL_BLUE, 256));
            mGreen = String.valueOf(mPrefs.getInt(KEY_KCAL_GREEN, 256));
            String storedValue = ((String) String.valueOf(mRed)
                   + " " + String.valueOf(mGreen) + " " +  String.valueOf(mBlue));
            String mSaturation = String.valueOf(mPrefs.getInt(KEY_KCAL_SATURATION, 256));
            String mContrast = String.valueOf(mPrefs.getInt(KEY_KCAL_CONTRAST, 256));
            String mHue = String.valueOf(mPrefs.getInt(KEY_KCAL_HUE, 0));
            String mValue = String.valueOf(mPrefs.getInt(KEY_KCAL_VALUE, 255));
            Boolean mGreyscale = mPrefs.getBoolean(KEY_KCAL_GREYSCALE, false);
            Utils.writeValue(COLOR_FILE_ENABLE, enabled ? "1" : "0");
            Utils.writeValue(COLOR_FILE, storedValue);
            Utils.writeValue(COLOR_FILE_SATURATION, mSaturation);
            Utils.writeValue(COLOR_FILE_CONTRAST, mContrast);
            Utils.writeValue(COLOR_FILE_HUE, mHue);
            Utils.writeValue(COLOR_FILE_VALUE, mValue);

            int cct = Utils.KfromRGB(mPrefs.getInt(KEY_KCAL_RED, 256), mPrefs.getInt(KEY_KCAL_GREEN, 256), mPrefs.getInt(KEY_KCAL_BLUE, 256));
            mKcalColorTemp.setValue(cct);
            return true;
        } else if (preference == mKcalRed) {
            float val = Float.parseFloat((String) newValue);
            mPrefs.edit().putInt(KEY_KCAL_RED, (int) val).commit();
            mGreen = String.valueOf(mPrefs.getInt(KEY_KCAL_GREEN, 256));
            mBlue = String.valueOf(mPrefs.getInt(KEY_KCAL_BLUE, 256));
            String strVal = ((String) newValue + " " + mGreen + " " +mBlue);
            Utils.writeValue(COLOR_FILE, strVal);

            int cct = Utils.KfromRGB(val, mPrefs.getInt(KEY_KCAL_GREEN, 256), mPrefs.getInt(KEY_KCAL_BLUE, 256));
            mKcalColorTemp.setValue(cct);
            return true;
        } else if (preference == mKcalGreen) {
            float val = Float.parseFloat((String) newValue);
            mPrefs.edit().putInt(KEY_KCAL_GREEN, (int) val).commit();
            mRed = String.valueOf(mPrefs.getInt(KEY_KCAL_RED, 256));
            mBlue = String.valueOf(mPrefs.getInt(KEY_KCAL_BLUE, 256));
            String strVal = ((String) mRed + " " + newValue + " " +mBlue);
            Utils.writeValue(COLOR_FILE, strVal);

            int cct = Utils.KfromRGB(mPrefs.getInt(KEY_KCAL_RED, 256), val, mPrefs.getInt(KEY_KCAL_BLUE, 256));
            mKcalColorTemp.setValue(cct);
            return true;
        } else if (preference == mKcalBlue) {
            float val = Float.parseFloat((String) newValue);
            mPrefs.edit().putInt(KEY_KCAL_BLUE, (int) val).commit();
            mRed = String.valueOf(mPrefs.getInt(KEY_KCAL_RED, 256));
            mGreen = String.valueOf(mPrefs.getInt(KEY_KCAL_GREEN, 256));
            String strVal = ((String) mRed + " " + mGreen + " " +newValue);
            Utils.writeValue(COLOR_FILE, strVal);

            int cct = Utils.KfromRGB(mPrefs.getInt(KEY_KCAL_RED, 256), mPrefs.getInt(KEY_KCAL_GREEN, 256), val);
            mKcalColorTemp.setValue(cct);
            return true;
        } else if (preference == mKcalSaturation) {
            float val = Float.parseFloat((String) newValue);
            mPrefs.edit().putInt(KEY_KCAL_SATURATION, (int) val).commit();
            String strVal = (String) newValue;
            Utils.writeValue(COLOR_FILE_SATURATION, strVal);
            return true;
        } else if (preference == mKcalContrast) {
            float val = Float.parseFloat((String) newValue);
            mPrefs.edit().putInt(KEY_KCAL_CONTRAST, (int) val).commit();
            String strVal = (String) newValue;
            Utils.writeValue(COLOR_FILE_CONTRAST, strVal);
            return true;
        } else if (preference == mKcalHue) {
            float val = Float.parseFloat((String) newValue);
            mPrefs.edit().putInt(KEY_KCAL_HUE, (int) val).commit();
            String strVal = (String) newValue;
            Utils.writeValue(COLOR_FILE_HUE, strVal);
            return true;
        } else if (preference == mKcalValue) {
            float val = Float.parseFloat((String) newValue);
            mPrefs.edit().putInt(KEY_KCAL_VALUE, (int) val).commit();
            String strVal = (String) newValue;
            Utils.writeValue(COLOR_FILE_VALUE, strVal);
            return true;
        } else if (preference == mKcalGreyscale) {
            Boolean greyscaleEnabled = (Boolean) newValue;
            mPrefs.edit().putBoolean(KEY_KCAL_GREYSCALE, greyscaleEnabled).commit();
            String storedSaturation = String.valueOf(mPrefs.getInt(KEY_KCAL_SATURATION, 255));
            Utils.writeValue(COLOR_FILE_SATURATION, greyscaleEnabled ? "128" : String.valueOf(storedSaturation));
            return true;
        } else if (preference == mKcalPresetsListPreference) {
            String currValue = (String) newValue;
            mPrefs.edit().putString(KEY_KCAL_PRESETS_LIST, currValue).commit();
            KcalPresets.setValue(currValue);
            refresh();
            return true;
        } else if (preference == mKcalColorTemp) {
            int val = Integer.parseInt((String) newValue);
            int[] colorTemp = Utils.RGBfromK(val);
            int red = colorTemp[0];
            int green = colorTemp[1];
            int blue = colorTemp[2];

            mKcalRed.setValue(red);
            mKcalGreen.setValue(green);
            mKcalBlue.setValue(blue);

            mPrefs.edit().putInt(KEY_KCAL_RED, red).commit();
            mPrefs.edit().putInt(KEY_KCAL_GREEN, green).commit();
            mPrefs.edit().putInt(KEY_KCAL_BLUE, blue).commit();

            String storedValue = Integer.toString(red) + " " + Integer.toString(green) + " " +  Integer.toString(blue);
            Utils.writeValue(COLOR_FILE, storedValue);
        }
        return false;
    }
}
