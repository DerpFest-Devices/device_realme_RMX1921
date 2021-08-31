package org.device.RealmeParts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import androidx.preference.PreferenceManager;

import org.device.RealmeParts.RealmeParts;

public class PerfTileService extends TileService {
    private int currentState;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
    }

    @Override
    public void onStartListening() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        currentState = sharedPrefs.getInt (RealmeParts.KEY_PERFORMANCE, 0);
        PerfControl.setPerf(currentState);
        Tile tile = getQsTile();
        tile.setState(Tile.STATE_ACTIVE);
        tile.setLabel(getResources().getStringArray(R.array.performance_profiles)[currentState]);
        tile.updateTile();
        super.onStartListening();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
    }

    @Override
        public void onClick() {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            currentState = sharedPrefs.getInt (RealmeParts.KEY_PERFORMANCE, 0);

            int nextState;
            if (currentState == 3) {
                nextState = 0;
            } else {
                nextState = currentState + 1;
            }

            Tile tile = getQsTile();
            sharedPrefs.edit().putInt(RealmeParts.KEY_PERFORMANCE, nextState).commit();
            tile.setLabel(getResources().getStringArray(R.array.performance_profiles)[nextState]);
            PerfControl.setPerf(nextState);
            tile.updateTile();
            super.onClick();
    }
}
