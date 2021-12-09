/*
* Copyright (C) 2014 SlimRoms Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.device.RealmeParts.Touch.util;

import android.app.ActivityManagerNative;
import android.app.ISearchManager;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.input.InputManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.media.session.MediaSessionLegacyHelper;
import android.provider.Settings;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.WindowManagerGlobal;

import java.net.URISyntaxException;
import java.util.List;

public class Action {

    private static final int MSG_INJECT_KEY_DOWN = 1066;
    private static final int MSG_INJECT_KEY_UP = 1067;
    private static final String PULSE_ACTION = "com.android.systemui.doze.pulse";

    private static boolean sTorchEnabled = false;

    private static ISearchManager mSearchManagerService;
    private static KeyguardManager mKeyguardManager;
    private static PowerManager mPowerManager;

    public static void processAction(Context context, String action, boolean isLongpress) {
        processActionWithOptions(context, action, isLongpress, true);
    }

    public static void processActionWithOptions(Context context,
            String action, boolean isLongpress, boolean collapseShade) {

            if (action == null || action.equals(ActionConstants.ACTION_NULL)) {
                return;
            }

            boolean isKeyguardShowing = false;
            try {
                isKeyguardShowing =
                        WindowManagerGlobal.getWindowManagerService().isKeyguardLocked();
            } catch (RemoteException e) {
                Log.w("Action", "Error getting window manager service", e);
            }

            // process the actions
            if (action.equals(ActionConstants.ACTION_AMBIENT_DISPLAY )) {
                final boolean dozeEnabled = Settings.Secure.getInt(context.getContentResolver(),
                    Settings.Secure.DOZE_ENABLED, 1) != 0;
            if (dozeEnabled) {
                    final Intent intent = new Intent(PULSE_ACTION);
                    context.sendBroadcastAsUser(intent, UserHandle.CURRENT);
            }
            } else if (action.equals(ActionConstants.ACTION_TORCH)) {
                try {
                    CameraManager cameraManager = (CameraManager)
                            context.getSystemService(Context.CAMERA_SERVICE);
                    for (final String cameraId : cameraManager.getCameraIdList()) {
                        CameraCharacteristics characteristics =
                            cameraManager.getCameraCharacteristics(cameraId);
                        Boolean flashAvailable = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                        int orient = characteristics.get(CameraCharacteristics.LENS_FACING);
                        if (flashAvailable != null && flashAvailable && orient == CameraCharacteristics.LENS_FACING_BACK) {
                            cameraManager.setTorchMode(cameraId, !sTorchEnabled);
                            sTorchEnabled = !sTorchEnabled;
                            break;
                        }
                    }
                } catch (CameraAccessException e) {
                }
            return;
            } else if (action.equals(ActionConstants.ACTION_VIB)) {
                AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                if(am != null && ActivityManagerNative.isSystemReady()) {
                    if(am.getRingerMode() != AudioManager.RINGER_MODE_VIBRATE) {
                        am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                        Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        if(vib != null){
                            vib.vibrate(50);
                        }
                    }else{
                        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        ToneGenerator tg = new ToneGenerator(
                                AudioManager.STREAM_NOTIFICATION,
                                (int)(ToneGenerator.MAX_VOLUME * 0.85));
                        if(tg != null){
                            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                        }
                    }
                }
                return;
            } else if (action.equals(ActionConstants.ACTION_SILENT)) {
                AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                if (am != null && ActivityManagerNative.isSystemReady()) {
                    if (am.getRingerMode() != AudioManager.RINGER_MODE_SILENT) {
                        am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    } else {
                        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        ToneGenerator tg = new ToneGenerator(
                                AudioManager.STREAM_NOTIFICATION,
                                (int)(ToneGenerator.MAX_VOLUME * 0.85));
                        if (tg != null) {
                            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                        }
                    }
                }
                return;
            } else if (action.equals(ActionConstants.ACTION_VIB_SILENT)) {
                AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                if (am != null && ActivityManagerNative.isSystemReady()) {
                    if (am.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
                        am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                        Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        if (vib != null) {
                            vib.vibrate(50);
                        }
                    } else if (am.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {
                        am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    } else {
                        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        ToneGenerator tg = new ToneGenerator(
                                AudioManager.STREAM_NOTIFICATION,
                                (int)(ToneGenerator.MAX_VOLUME * 0.85));
                        if (tg != null) {
                            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                        }
                    }
                }
                return;
            } else if (action.equals(ActionConstants.ACTION_CAMERA)) {
                triggerCameraAction(context);
                return;
            } else if (action.equals(ActionConstants.ACTION_MEDIA_PREVIOUS)) {
                dispatchMediaKeyWithWakeLock(KeyEvent.KEYCODE_MEDIA_PREVIOUS, context);
                return;
            } else if (action.equals(ActionConstants.ACTION_MEDIA_NEXT)) {
                dispatchMediaKeyWithWakeLock(KeyEvent.KEYCODE_MEDIA_NEXT, context);
                return;
            } else if (action.equals(ActionConstants.ACTION_MEDIA_PLAY_PAUSE)) {
                dispatchMediaKeyWithWakeLock(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, context);
                return;
            } else if (action.equals(ActionConstants.ACTION_VOLUME_DOWN)) {
                triggerVirtualKeypress(KeyEvent.KEYCODE_VOLUME_DOWN, isLongpress);
            return;
            } else if (action.equals(ActionConstants.ACTION_VOLUME_UP)) {
                    triggerVirtualKeypress(KeyEvent.KEYCODE_VOLUME_UP, isLongpress);
            return;
            } else if (action.equals(ActionConstants.ACTION_ASSIST)){
                mSearchManagerService = ISearchManager.Stub.asInterface(ServiceManager.getService(Context.SEARCH_SERVICE));
                if (mSearchManagerService != null) {
                        try {
                            mSearchManagerService.launchAssist(context.getUserId(), new Bundle());
                        } catch (RemoteException e) {
                        }
                }
            return;
            } else if (action.equals(ActionConstants.ACTION_WAKE_DEVICE)) {
                PowerManager powerManager =
                        (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                if (!powerManager.isScreenOn()) {
                    powerManager.wakeUp(SystemClock.uptimeMillis());
                }
                return;
            } else {
                // we must have a custom uri
                Intent intent = null;
                try {
                    intent = Intent.parseUri(action, 0);
                } catch (URISyntaxException e) {
                    Log.e("aospActions:", "URISyntaxException: [" + action + "]");
                    return;
                }
                startActivity(context, intent);
                return;
            }

    }

    public static boolean isActionKeyEvent(String action) {
        if (action.equals(ActionConstants.ACTION_NULL)) {
            return true;
        }
        return false;
    }

    private static void startActivity(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivityAsUser(intent,
                new UserHandle(UserHandle.USER_CURRENT));
    }

    private static void dispatchMediaKeyWithWakeLock(int keycode, Context context) {
        if (ActivityManagerNative.isSystemReady()) {
            KeyEvent event = new KeyEvent(SystemClock.uptimeMillis(),
                    SystemClock.uptimeMillis(), KeyEvent.ACTION_DOWN, keycode, 0);
            MediaSessionLegacyHelper.getHelper(context).sendMediaButtonEvent(event, true);
            event = KeyEvent.changeAction(event, KeyEvent.ACTION_UP);
            MediaSessionLegacyHelper.getHelper(context).sendMediaButtonEvent(event, true);
        }
    }

    private static void ensureKeyguardManager(Context context) {
        if (mKeyguardManager == null) {
            mKeyguardManager =
                    (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        }
    }

    private static ActivityInfo getBestActivityInfo(Intent intent, Context context) {
        PackageManager pm = context.getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);
        if (resolveInfo != null) {
            return resolveInfo.activityInfo;
        } else {
            // If the resolving failed, just find our own best match
            return getBestActivityInfo(intent, null);
        }
    }

    private static ActivityInfo getBestActivityInfo(Intent intent, ActivityInfo match, Context context) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
        ActivityInfo best = null;
        if (activities.size() > 0) {
            best = activities.get(0).activityInfo;
            if (match != null) {
                String packageName = match.applicationInfo.packageName;
                for (int i = activities.size() - 1; i >= 0; i--) {
                    ActivityInfo activityInfo = activities.get(i).activityInfo;
                    if (packageName.equals(activityInfo.applicationInfo.packageName)) {
                        best = activityInfo;
                    }
                }
            }
        }
        return best;
    }

    private static void triggerCameraAction(Context context) {
        ensureKeyguardManager(context);
        mPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        WakeLock wl = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "GestureWakeLock");
        wl.acquire(500);
        if (mKeyguardManager.inKeyguardRestrictedInputMode()) {
            launchSecureCamera(context);
        } else {
            launchCamera(context);
        }
    }

    private static void launchCamera(Context context) {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_FROM_BACKGROUND);
        if (getBestActivityInfo(intent, context) != null) {
            // Only launch if we can succeed, but let the user pick the action
            context.startActivity(intent);
        }
    }

    private static void launchSecureCamera(Context context) {
        // Keyguard won't allow a picker, try to pick the secure intent in the package
        // that would be the one used for a default action of launching the camera
        Intent normalIntent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        normalIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        normalIntent.addFlags(Intent.FLAG_FROM_BACKGROUND);

        Intent secureIntent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA_SECURE);
        secureIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        secureIntent.addFlags(Intent.FLAG_FROM_BACKGROUND);

        ActivityInfo normalActivity = getBestActivityInfo(normalIntent, context);
        ActivityInfo secureActivity = getBestActivityInfo(secureIntent, normalActivity, context);
        if (secureActivity != null) {
            secureIntent.setComponent(new ComponentName(secureActivity.applicationInfo.packageName, secureActivity.name));
            context.startActivity(secureIntent);
        }
    }

    public static void triggerVirtualKeypress(final int keyCode, boolean longpress) {
        InputManager im = InputManager.getInstance();
        long now = SystemClock.uptimeMillis();
        int downflags = 0;
        int upflags = 0;
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT
            || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
            || keyCode == KeyEvent.KEYCODE_DPAD_UP
            || keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            downflags = upflags = KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE;
        } else {
            downflags = upflags = KeyEvent.FLAG_FROM_SYSTEM | KeyEvent.FLAG_VIRTUAL_HARD_KEY;
        }
        if (longpress) {
            downflags |= KeyEvent.FLAG_LONG_PRESS;
        }

        final KeyEvent downEvent = new KeyEvent(now, now, KeyEvent.ACTION_DOWN,
                keyCode, 0, 0, KeyCharacterMap.VIRTUAL_KEYBOARD, 0,
                downflags,
                InputDevice.SOURCE_KEYBOARD);
        im.injectInputEvent(downEvent, InputManager.INJECT_INPUT_EVENT_MODE_ASYNC);

        final KeyEvent upEvent = new KeyEvent(now, now, KeyEvent.ACTION_UP,
                keyCode, 0, 0, KeyCharacterMap.VIRTUAL_KEYBOARD, 0,
                upflags,
                InputDevice.SOURCE_KEYBOARD);
        im.injectInputEvent(upEvent, InputManager.INJECT_INPUT_EVENT_MODE_ASYNC);
    }
}
