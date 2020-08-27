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
import android.os.IBinder;
import android.util.Log;

public class RealmeProximityHelperService extends Service {
    private static final String TAG = "RealmeProximityHelperService";
    private static final String PS_STATUS = "/proc/touchpanel/fd_enable";
    private static final boolean DEBUG = true;

    // Create Variable for Infrared sensor class usage
    private DisplayStateHelper mDisplayHelper;

    // Intent reciever to start and kill service upon relevant events
    // Enable sensor on screnoff intent and Panel suspend status
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                if(!FileHelper.getFileValueAsBoolean(PS_STATUS, false)){
                if (DEBUG) Log.d(TAG, "Screen-off no proximity event");
                mDisplayHelper.disable();
            } else {
                if (DEBUG) Log.d(TAG, "Not disabling, Proximity event fd_enable = 1");
            }
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
}