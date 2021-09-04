package org.device.RealmeParts.vibrator;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;

public class VibratorSettingsActivity extends Activity {

    private VibratorSettings mVibrateSettingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        Fragment fragment = getFragmentManager().findFragmentById(android.R.id.content);
        if (fragment == null) {
            mVibrateSettingsFragment = new VibratorSettings();
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, mVibrateSettingsFragment)
                    .commit();
        } else {
            mVibrateSettingsFragment = (VibratorSettings) fragment;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 
