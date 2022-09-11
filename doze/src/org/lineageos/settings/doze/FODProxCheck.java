/*
 * Copyright (C) 2015 The CyanogenMod Project
 *               2017-2018 The LineageOS Project
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
 */

package org.lineageos.settings.doze;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FODProxCheck implements SensorEventListener {

    private static final boolean DEBUG = false;
    private static final String FOD_PROXCHECK = "/proc/touchpanel/fod_proxcheck";
    private static final String TAG = "ProximitySensor";

    private boolean isNear = false;
    private boolean resetTap = false;

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Context mContext;
    private ExecutorService mExecutorService;
    private PowerManager mPowerManager;

    public FODProxCheck(Context context) {
        mContext = context;
        mSensorManager = mContext.getSystemService(SensorManager.class);
        mSensor = mSensorManager.getDefaultSensor(33171005, true); //Stk_st2x2x Wakeup mode
        mExecutorService = Executors.newSingleThreadExecutor();
        mPowerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
    }

    private Future<?> submit(Runnable runnable) {
        return mExecutorService.submit(runnable);
    }

    public static String getFile() {
        if (Utils.fileWritable(FOD_PROXCHECK)) {
            return FOD_PROXCHECK;
        }
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        isNear = event.values[0] < mSensor.getMaximumRange();
        Utils.writeValue(getFile(), isNear ? "0" : "1");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        /* Empty */
    }

    protected void enable() {
        if (DEBUG) Log.d(TAG, "Enabling");
        submit(() -> {
            mSensorManager.registerListener(this, mSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        });
    }

    protected void disable() {
        if (DEBUG) Log.d(TAG, "Disabling");
        submit(() -> {
            mSensorManager.unregisterListener(this, mSensor);
        });
    }
} 
