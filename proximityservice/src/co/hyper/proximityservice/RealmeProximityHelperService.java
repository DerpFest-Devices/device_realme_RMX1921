/*
 * Copyright (c) 2020 Harshit Jain <god@hyper-labs.tech>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Purpose: Main background service class that registers listener for display
 * helper class that helps in handling proximity events.
 * 
 */

package co.hyper.proximityservice;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.IBinder;
import android.os.UserHandle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

import static android.provider.Settings.Secure.DOZE_ALWAYS_ON;

public class RealmeProximityHelperService extends Service {
    private static final String TAG = "RealmeProximityHelperService";
    private static final String FOD_STATUS = "/proc/touchpanel/fod_aod_listener";
    private static final boolean DEBUG = false;

    // Create Variable for Infrared sensor class usage
    protected static DisplayStateHelper mDisplayHelper;
    protected static boolean pulsed;

    // Intent reciever to start and kill service upon relevant events
    // Enable sensor on screnoff intent and Panel suspend status
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(PowerManager.ACTION_DEVICE_IDLE_MODE_CHANGED) || action.equals(Intent.ACTION_SCREEN_OFF)){
                if(FileHelper.getFileValueAsBoolean(FOD_STATUS, false) && isAlwaysOnEnabled(context)){
                    mDisplayHelper.enable();
                    if (DEBUG) Log.d(TAG, "Pulsed is"+pulsed);
                    if (!pulsed){
                        mDisplayHelper.launchDozePulse(context);
                        pulsed = true;//don't loop pulses
                    }
                }
            }
            if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                if(!FileHelper.getFileValueAsBoolean(FOD_STATUS, false))
                        mDisplayHelper.disable();
            } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
                if (DEBUG) Log.d(TAG, "Enabling the DisplayState listener");
                mDisplayHelper.enable();
            }
        }
    };

    // Register object of Infrared Proximity Sensor
    // Register Screen off and Screen on Intents 
    @Override
    public void onCreate() {
        if (DEBUG) Log.d(TAG, "Creating service for DisplayStateListener");
        mDisplayHelper = new DisplayStateHelper(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(PowerManager.ACTION_DEVICE_IDLE_MODE_CHANGED);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mIntentReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (DEBUG) Log.d(TAG, "Starting DisplayStateListener service");
        mDisplayHelper.enable();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (DEBUG) Log.d(TAG, "Destroying DisplayStateListener service");
        mDisplayHelper.disable();
        unregisterReceiver(mIntentReceiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected static boolean isAlwaysOnEnabled(Context context) {
        final boolean enabledByDefault = context.getResources()
                .getBoolean(com.android.internal.R.bool.config_dozeAlwaysOnEnabled);

        return Settings.Secure.getIntForUser(context.getContentResolver(),
                DOZE_ALWAYS_ON, alwaysOnDisplayAvailable(context) && enabledByDefault ? 1 : 0,
                UserHandle.USER_CURRENT) != 0;
    }

    protected static boolean alwaysOnDisplayAvailable(Context context) {
        return new AmbientDisplayConfiguration(context).alwaysOnAvailable();
    }
}
