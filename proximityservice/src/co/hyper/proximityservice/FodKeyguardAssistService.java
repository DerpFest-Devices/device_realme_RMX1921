/*
 * Copyright (c) 2021 Thatisjigen <gianfranco.liguori.96@gmail.com>
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
 * Purpose: Disable sensors once display is unlocked.
 * 
 */

package co.hyper.proximityservice;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;


public class FodKeyguardAssistService extends Service {
    private static final String TAG = "FodKeyguardAssistService";
    private static final boolean DEBUG = false;
    private static final String AOD_STATUS = "/proc/touchpanel/fod_aod_listener";
    private static final String FOD_PRESSED = "/proc/touchpanel/fod_aod_pressed";
    private static final String DOZING = "/proc/touchpanel/DOZE_STATUS";
    private static final String PS_NEAR = "/proc/touchpanel/prox_near";

    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
                if (DEBUG) Log.d(TAG, "Device Unlocked");

                final boolean dozing = !FileHelper.getFileValueAsBoolean(DOZING, false);
                FileHelper.writeValue(AOD_STATUS, "0");//clean up the old state
                FileHelper.writeValue(PS_NEAR, "0");

                if (!dozing) {
                    RealmeProximityHelperService.pulsed = false;//clean up the pulsed flag
                    FileHelper.writeValue(FOD_PRESSED, "0");//clean up the pressed flag
                    RealmeProximityHelperService.mDisplayHelper.disableSensors();//unregister sensors.
                }
            }
        }
    };

    @Override
    public void onCreate() {
       if (DEBUG) Log.d(TAG, "Creating callback for FodHelperService");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(mIntentReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (DEBUG) Log.d(TAG, "Destroying FodHelperService service");
        unregisterReceiver(mIntentReceiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
} 
