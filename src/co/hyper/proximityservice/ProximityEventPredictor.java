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
 */

package co.hyper.proximityservice;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.os.Handler;

public class ProximityEventPredictor implements SensorEventListener {
    private static final boolean DEBUG = true;

    /* Proximity Control Path */
    private static final String PS_MASK = "/proc/touchpanel/prox_mask";
    private static final String PS_STATUS = "/proc/touchpanel/fd_enable";

    /* Senosr instances */
    private static boolean tiltRegistered = false;
    private Context mContext;
    private SensorManager mSensorManager;
    private Sensor mtiltListener;

    /* Constructor */
    public ProximityEventPredictor(Context context){
        if (DEBUG) Log.d(TAG, "Initialising ProximityEventPredictor constructor");
        mContext = context;
        mSensorManager = mContext.getSystemService(SensorManager.class);
        mtiltListener = mSensorManager.getDefaultSensor(Sensor.TYPE_TILT_DETECTOR);
    }

    /* Consider proximity sensor events based on tilt sensor readings*/
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.value[0] == 1.0f){
            sendFar();
        }
        /* TODO: Add logic for grabbing input when device is held
        * Find a way to quickly clear debounce time.
        */
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        /* Empty */
    }

    void enable() {
        /* Conditionally enable the lister */
        if (DEBUG) Log.d(TAG, "Enabling orientation listener");
        if (!FileHelper.getFileValueAsBoolean(PS_STATUS, false)){
            if (DEBUG) Log.d(TAG, "Not a tp proximity event bailing out");
            return;
        }
        tiltRegistered = true;
        mSensorManager.registerListener(this, mtiltListener, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /* Disable the listener */
    void disable(){
        if (DEBUG) Log.d(TAG, "Disabling orientation listener");
        if(!tiltRegistered){
            if (DEBUG) Log.d(TAG, "Orientation sensor wasn't registered by us.");
            return;
        }
        tiltRegistered = false;
        mSensorManager.unregisterListener(this, mtiltListener);
    }

    /* Set proximity status as far */
    void sendFar() {
       if (DEBUG) Log.d(TAG, "Sent far event to Proximity mask node");
       FileHelper.writeValue(PS_MASK, "1");
   }
}