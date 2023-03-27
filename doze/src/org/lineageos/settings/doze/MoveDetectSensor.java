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
import android.os.SystemClock;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MoveDetectSensor implements SensorEventListener {

    private static final boolean DEBUG = true;
    private static final String TAG = "MoveDetectSensor";

    private static final String MOVE_DETECT_SENSOR = "qti.sensor.move_detect";

    private static final int MIN_PULSE_INTERVAL_MS = 1000;

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Sensor mFakeProximitySensor;
    private Context mContext;
    private ExecutorService mExecutorService;

    private long mEntryTimestamp;

    private static boolean isNear = false;

    public MoveDetectSensor(Context context) {
        mContext = context;
        mSensorManager = mContext.getSystemService(SensorManager.class);
        mSensor = DozeUtils.getSensor(mSensorManager, MOVE_DETECT_SENSOR);
        mFakeProximitySensor = DozeUtils.getSensor(mSensorManager, "qti.sensor.proximity_fake");
        mExecutorService = Executors.newSingleThreadExecutor();
    }

    private Future<?> submit(Runnable runnable) {
        return mExecutorService.submit(runnable);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (DEBUG) Log.d(TAG, "Got sensor event: " + event.values[0]);

        long delta = SystemClock.elapsedRealtime() - mEntryTimestamp;
        if (delta < MIN_PULSE_INTERVAL_MS) {
            return;
        }

        mEntryTimestamp = SystemClock.elapsedRealtime();

        if (event.values[0] == 2.0f && !isNear) {
            DozeUtils.launchDozePulse(mContext);
        }
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
            mSensorManager.registerListener(mProximitySensorListener, mFakeProximitySensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
            mEntryTimestamp = SystemClock.elapsedRealtime();
        });
    }

    protected void disable() {
        if (DEBUG) Log.d(TAG, "Disabling");
        submit(() -> {
            mSensorManager.unregisterListener(mProximitySensorListener, mFakeProximitySensor);
            mSensorManager.unregisterListener(this, mSensor);
        });
    }

    private final SensorEventListener mProximitySensorListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
            isNear = (event.values[0] == 0.0f);
            Log.i(TAG,"isNear: " + isNear);
        }

        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };
}
