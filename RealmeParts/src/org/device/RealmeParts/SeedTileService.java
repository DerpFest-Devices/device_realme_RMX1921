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

import org.device.RealmeParts.RealmeParts;

public class SeedTileService extends TileService {
    private int currentState;
    private static final String SEED_PATH = "/sys/kernel/oppo_display/seed";

    @Override
        public void onStartListening() {
            super.onStartListening();
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            currentState = sharedPrefs.getInt (RealmeParts.KEY_SEED, 0);
            Tile tile = getQsTile();
            tile.setState(Tile.STATE_ACTIVE);
            tile.setLabel(getResources().getStringArray(R.array.seed_profiles)[currentState]);

            tile.updateTile();
            super.onStartListening();
    }

    @Override
        public void onClick() {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            currentState = sharedPrefs.getInt (RealmeParts.KEY_SEED, 0);
            int currentState = sharedPrefs.getInt (RealmeParts.KEY_SEED, 0);

            int nextState;
            if (currentState == 2) {
            nextState = 0;
            } else {
                    nextState = currentState + 1;
            }

        Tile tile = getQsTile();
        sharedPrefs.edit().putInt(RealmeParts.KEY_SEED, nextState).commit();
        tile.setLabel(getResources().getStringArray(R.array.seed_profiles)[nextState]);
        Utils.writeValue(SEED_PATH, Integer.toString(nextState));
        tile.updateTile();
        super.onClick();
    }
} 
