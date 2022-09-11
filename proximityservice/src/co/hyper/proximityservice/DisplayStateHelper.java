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
 * Purpose: This file registers display state listener and makes sure proximity
 * sensor is only triggered on display.STATE_OFF and when fd_enable = 1 which
 * signifies that the system has held a proximity wakelock indicating
 * touchpanel proximity has suspended and no events are recieved from it thus
 * its now time to trigger the infrared proximity and take events from it.
 * 
 */

package co.hyper.proximityservice;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManager.DisplayListener;
import android.view.Display;
import android.util.Log;
import android.os.UserHandle;

public class DisplayStateHelper implements DisplayListener {
     private static final String TAG = "DisplayStateListener";
     private static final boolean DEBUG = false;
     private Context mcontext;
     private InfraredSensor mFakeProximity;
     private FodHelper mFodHelper;
     private static final String FOD_STATUS = "/proc/touchpanel/fod_aod_listener";
     private static final String DOZING = "/proc/touchpanel/DOZE_STATUS";
     private static final String DOZE_INTENT = "com.android.systemui.doze.pulse";

     public DisplayStateHelper(Context context){
        if (DEBUG) Log.d(TAG, "Initialising display state listner constructor");
         mcontext = context;
         mFakeProximity = new InfraredSensor(context);
         mFodHelper = new FodHelper(context);//register a proximity sensor so that it will poll for pressed status on fod and pulse once pressed to trigger the display "ON" state
     }

     @Override
     public void onDisplayAdded(int displayId) {
        /* Empty */
     }
   
     @Override
     public void onDisplayRemoved(int displayId) {
        /* Empty "*/
     }
     
     @Override
     public void onDisplayChanged(int displayId) {
        if ((displayId == Display.DEFAULT_DISPLAY && isDefaultDisplayOff(mcontext)) || (FileHelper.getFileValueAsBoolean(FOD_STATUS, false) && RealmeProximityHelperService.isAlwaysOnEnabled(mcontext))) {
                // register proximity or fod listener
                if (FileHelper.getFileValueAsBoolean(FOD_STATUS, false) && RealmeProximityHelperService.isAlwaysOnEnabled(mcontext)){
                   mFodHelper.enable();
                }
                else if (isDefaultDisplayOff(mcontext)) 
                    mFakeProximity.enable();
        } else mFakeProximity.disable();
    }

    void enable() {
        /* Enable the lister */
        if (DEBUG) Log.d(TAG, "Registering display state listner");
        mcontext.getSystemService(DisplayManager.class).registerDisplayListener(this, null);
        mFakeProximity.disable();
    }

    void disable() {
        /* kill the listener */
        if (DEBUG) Log.d(TAG, "Killing display state listener");
        mcontext.getSystemService(DisplayManager.class).unregisterDisplayListener(this);
        mFakeProximity.disable();
        disableSensors();
    }

    private static boolean isDefaultDisplayOff(Context context) {
        Display display = context.getSystemService(DisplayManager.class).getDisplay(Display.DEFAULT_DISPLAY);
        return display.getState() == Display.STATE_OFF;
      }

    public void disableSensors() {
        mFodHelper.disable();
    }

    protected static void launchDozePulse(Context context) {
        context.sendBroadcastAsUser(new Intent(DOZE_INTENT),
                        new UserHandle(UserHandle.USER_CURRENT));
    }
}
