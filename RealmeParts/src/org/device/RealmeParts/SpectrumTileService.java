/*
* Copyright (C) 2018 The OmniROM Project
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

import android.content.Intent;
import android.content.SharedPreferences;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import androidx.preference.PreferenceManager;
import android.os.SystemProperties;

import org.device.RealmeParts.RealmeParts;

public class SpectrumTileService extends TileService {

private static final String SPECTRUM_SYSTEM_PROPERTY = "persist.spectrum.profile";
    @Override
        public void onStartListening() {
            int currentState = getintProp(SPECTRUM_SYSTEM_PROPERTY, 0);

            Tile tile = getQsTile();
            tile.setState(Tile.STATE_ACTIVE);
            tile.setLabel(getResources().getStringArray(R.array.spectrum_profiles)[currentState]);

            tile.updateTile();
            super.onStartListening();
    }

    @Override
        public void onClick() {
            int currentState = getintProp(SPECTRUM_SYSTEM_PROPERTY, 0);

            int nextState;
            if (currentState == 3) {
            nextState = 0;
        } else {
            nextState = currentState + 1;
        }

        Tile tile = getQsTile();
        setintProp(SPECTRUM_SYSTEM_PROPERTY, nextState);
        tile.setLabel(getResources().getStringArray(R.array.spectrum_profiles)[nextState]);

        tile.updateTile();
        super.onClick();
    }
    public static void setintProp(String prop, int value) {
        SystemProperties.set(prop, String.valueOf(value));
    }

    public static int getintProp(String prop, int defaultValue) {
        return SystemProperties.getInt(prop, defaultValue);
    }
}
