package org.device.RealmeParts.kcal;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import androidx.preference.PreferenceManager;

import org.device.RealmeParts.kcal.DisplayCalibration;

public class KcalService extends Service {

    @Override
    public void onCreate() {
        // Code to execute when the service is first created
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    Context context = getApplicationContext();
    DisplayCalibration.restore(context);
    return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
    return null;
    }
}
