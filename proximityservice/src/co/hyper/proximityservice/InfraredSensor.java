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
 * Purpose: This class when called upon by the Display helper class registers the
 * proximity sensor and listener based upon some sanity checks and updates the value
 * of proximity mask node after 150ms of a successful far event from stk_st2x2x 
 * Infrared proximity sensor used on realme mobiles.
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

public class InfraredSensor implements SensorEventListener {
    private static final boolean DEBUG = false;
    private static final String TAG = "InfraredSensor";
    private static final int SENSORID = 33171027; //stk_st2x2x NonWakeup (High Accuracy)
    private static final int MASK_TIME = 150;

    private static final String PS_STATUS = "/proc/touchpanel/fd_enable";
    private static final String PS_MASK = "/proc/touchpanel/prox_mask";

    // Store last status
    private static boolean sensorAlive = false;
    private static boolean flag = false;

    private Context mContext;
    private SensorManager mSensorManager;
    private Sensor mSensor;

    public InfraredSensor(Context context) {
        if (DEBUG) Log.d(TAG, "Intialising InfraDED sensor constructor");
        mContext = context;
        mSensorManager = mContext.getSystemService(SensorManager.class);
        mSensor = mSensorManager.getDefaultSensor(SENSORID, false);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        /* if we are here this means sensor live and is being used */
        sensorAlive = true;
        if (event.values[0] <= 3.50f) {
            /* We don't need to do anything since the sensor is near */
            if (DEBUG) Log.d(TAG, "Near detected, Sending same in 50ms");

            /*
             * Sensor reports change very often and it gets written to kernel node
             * where input sync+report takes place in realtime with a mutex lock on data
             * the stock sensor hal is supposed to poll & notify every change in satate
             * which becomes messy as we link these events (Infra Proximity to tp Proximity)
             * add a flag to handle this and return until a total event flip is observed
             * before reporting a near event again
             */
            if (flag) return;

            (new Handler()).postDelayed(this::sendNear, MASK_TIME-100);
            return;
        }
        /* Let's do stuff ? */
        if (DEBUG) Log.d(TAG, "Sending proximity far event in 150");
        (new Handler()).postDelayed(this::sendFar, MASK_TIME);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        /* Empty */
    }

    void enable() {
        if (FileHelper.getFileValueAsBoolean(PS_STATUS, false)) {
            if (DEBUG) Log.d(TAG, "Enabling QTI Proximity Sensor fd_enable was 1");
            sensorAlive = true;
            flag = false; // Allow reporting near for initial case
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            if (DEBUG) Log.d(TAG, "Not a touchpanel proximity event");
            return;
        }
    }

    void disable() {
        if(sensorAlive == true) {
            if (DEBUG) Log.d(TAG, "Disabling QTI Proximity");
            sensorAlive = false;
            flag = true;
            mSensorManager.unregisterListener(this, mSensor);
        } else {
            if (DEBUG) Log.d(TAG, "Sensor wasn't registered no need of killing");
            return;
        }
    }

    /* Set proximity status as far */
   void sendFar() {
       if (DEBUG) Log.d(TAG, "Sent far event to Proximity mask node");
       flag = false; // Disable spam control flag
       FileHelper.writeValue(PS_MASK, "1");
   }

   /* Set proximity status as near */
   void sendNear() {
       if (DEBUG) Log.d(TAG, "Sent near event to proximity mask node");
       flag = true; // Enable spam control flag
       FileHelper.writeValue(PS_MASK, "0");
    }
}
