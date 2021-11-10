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
package org.device.RealmeParts;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.provider.Settings;
import android.provider.Settings.System;
import android.provider.Settings.Global;
import android.util.Log;

import org.device.RealmeParts.ModeSwitch.GameModeSwitch;

@TargetApi(24)
public class GameModeTileService extends TileService {
    private boolean enabled = false;
    private NotificationManager mNotificationManager;
    private Context mContext;
    private boolean DnD;
    private boolean HeadsUpOldState;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTileAdded() {
        mContext = getApplicationContext();
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        super.onTileAdded();
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        enabled = GameModeSwitch.isCurrentlyEnabled(this);
        getQsTile().setState(enabled ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
        getQsTile().updateTile();

    }

    @Override
    public void onStopListening() {
        super.onStopListening();
    }

    @Override
    public void onClick() {
        super.onClick();
        mContext = getApplicationContext();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        enabled = GameModeSwitch.isCurrentlyEnabled(this);
        Utils.writeValue(GameModeSwitch.getFile(), enabled ? "0" : "1");
        Utils.writeValue(RealmeParts.TP_LIMIT_ENABLE, enabled ? "1" : "0" );
        Utils.writeValue(RealmeParts.TP_DIRECTION, enabled ? "0" : "1" );
        if (sharedPrefs.getBoolean("dnd", false))
            DnD = true;
        boolean status = !enabled;
        if (status && DnD) {
            final boolean isHeadsUpEnabledByUser = Settings.Global.getInt(mContext.getContentResolver(),
                              Settings.Global.HEADS_UP_NOTIFICATIONS_ENABLED, 1) == 1;
            HeadsUpOldState = isHeadsUpEnabledByUser;
            sharedPrefs.edit().putBoolean(RealmeParts.KEY_HEADS_UP, isHeadsUpEnabledByUser).commit();
            Settings.Global.putInt(mContext.getContentResolver(), Settings.Global.HEADS_UP_NOTIFICATIONS_ENABLED, 0);
        } else if (!status && DnD) {
            final boolean wasHeadsUpEnabledByUser = sharedPrefs.getBoolean(RealmeParts.KEY_HEADS_UP, true);
            if (wasHeadsUpEnabledByUser)
                Settings.Global.putInt(mContext.getContentResolver(),
                    Settings.Global.HEADS_UP_NOTIFICATIONS_ENABLED, 1);
        }
        sharedPrefs.edit().putBoolean(RealmeParts.KEY_GAME_SWITCH, enabled ? false : true).commit();
        getQsTile().setState(enabled ? Tile.STATE_INACTIVE : Tile.STATE_ACTIVE);
        getQsTile().updateTile();
    }
}
