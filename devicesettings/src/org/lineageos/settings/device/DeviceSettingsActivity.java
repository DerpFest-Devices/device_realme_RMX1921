/*
* Copyright (C) 2017 The OmniROM Project
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
package org.lineageos.settings.device;

import android.app.Fragment;
import android.os.Bundle;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceManager;
import com.android.settingslib.collapsingtoolbar.CollapsingToolbarBaseActivity;
import com.android.settingslib.collapsingtoolbar.R;

public class DeviceSettingsActivity extends CollapsingToolbarBaseActivity {

    private DeviceSettings mDeviceSettingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fragment fragment = getFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment == null) {
            mDeviceSettingsFragment = new DeviceSettings();
            getFragmentManager().beginTransaction()
                .add(R.id.content_frame, mDeviceSettingsFragment)
                .commit();
        } else {
            mDeviceSettingsFragment = (DeviceSettings) fragment;
        }
    }
}
