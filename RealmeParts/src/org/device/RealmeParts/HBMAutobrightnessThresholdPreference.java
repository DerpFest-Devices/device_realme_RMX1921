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

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceViewHolder;

import org.device.RealmeParts.ProperSeekBarPreference;

import org.device.RealmeParts.R;

public class HBMAutobrightnessThresholdPreference extends ProperSeekBarPreference {

    private static int mMinVal = 0;
    private static int mMaxVal = 60000;
    private static int mDefVal = 20000;

    public HBMAutobrightnessThresholdPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        mInterval = 1000;
        mShowSign = false;
        mUnits = "";
        mContinuousUpdates = false;
        mMinValue = mMinVal;
        mMaxValue = mMaxVal;
        mDefaultValueExists = true;
        mDefaultValue = mDefVal;
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        mValue = Integer.parseInt(sharedPrefs.getString(RealmeParts.KEY_HBM_AUTOBRIGHTNESS_THRESHOLD, "20000"));

        setPersistent(false);
    }

    @Override
    protected void changeValue(int newValue) {
        SharedPreferences.Editor prefChange = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        prefChange.putString(RealmeParts.KEY_HBM_AUTOBRIGHTNESS_THRESHOLD, String.valueOf(newValue)).commit();
    }
} 
