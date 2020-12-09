/*
 * Copyright (C) 2014 Slimroms
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

package com.Galaxy.Features.Touch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.Preference.OnPreferenceClickListener;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import com.Galaxy.Features.KernelControl;
import com.Galaxy.Features.R;
import com.Galaxy.Features.Touch.util.ActionConstants;
import com.Galaxy.Features.Touch.util.AppHelper;
import com.Galaxy.Features.Touch.util.ShortcutPickerHelper;
import com.Galaxy.Features.Touch.util.Utils;


public class ScreenOffGesture extends PreferenceFragment implements
        OnPreferenceChangeListener, OnPreferenceClickListener,
        ShortcutPickerHelper.OnPickListener {

    private static final String SETTINGS_METADATA_NAME = "com.android.settings";

    public static final String PREF_DT2W_ENABLE = "enable_dt2w";
    public static final String PREF_GESTURE_ENABLE = "enable_gestures";

    public static final String PREF_GESTURE_DOUBLE_TAP = "gesture_double_tap";
    public static final String PREF_GESTURE_W = "gesture_w";
    public static final String PREF_GESTURE_M = "gesture_m";
    public static final String PREF_GESTURE_CIRCLE = "gesture_circle";
    public static final String PREF_GESTURE_TWO_SWIPE = "gesture_two_swipe";
    public static final String PREF_GESTURE_UP_ARROW = "gesture_up_arrow";
    public static final String PREF_GESTURE_DOWN_ARROW = "gesture_down_arrow";
    public static final String PREF_GESTURE_LEFT_ARROW = "gesture_left_arrow";
    public static final String PREF_GESTURE_RIGHT_ARROW = "gesture_right_arrow";
    public static final String PREF_GESTURE_SWIPE_UP = "gesture_swipe_up";
    public static final String PREF_GESTURE_SWIPE_DOWN = "gesture_swipe_down";
    public static final String PREF_GESTURE_SWIPE_LEFT = "gesture_swipe_left";
    public static final String PREF_GESTURE_SWIPE_RIGHT = "gesture_swipe_right";

    private static final String KEY_GESTURE_HAPTIC_FEEDBACK = "gesture_haptic_feedback";

    private static final int DLG_SHOW_ACTION_DIALOG  = 0;
    private static final int DLG_RESET_TO_DEFAULT    = 1;

    private static final int MENU_RESET = Menu.FIRST;

    private Preference mGestureDoubleTap;
    private Preference mGestureW;
    private Preference mGestureM;
    private Preference mGestureCircle;
    private Preference mGestureTwoSwipe;
    private Preference mGestureUpArrow;
    private Preference mGestureDownArrow;
    private Preference mGestureLeftArrow;
    private Preference mGestureRightArrow;
    private Preference mGestureSwipeUp;
    private Preference mGestureSwipeDown;
    private Preference mGestureSwipeLeft;
    private Preference mGestureSwipeRight;

    private SwitchPreference mEnableDt2w;
    private SwitchPreference mEnableGestures;
    private SwitchPreference mHapticFeedback;

    private boolean mCheckPreferences;
    private SharedPreferences mScreenOffGestureSharedPreferences;

    private ShortcutPickerHelper mPicker;
    private String mPendingSettingsKey;
    private static Utils.FilteredDeviceFeaturesArray sFinalActionDialogArray;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        mPicker = new ShortcutPickerHelper(getActivity(), this);

        mScreenOffGestureSharedPreferences = getActivity().getSharedPreferences(
                Utils.PREFERENCES, Activity.MODE_PRIVATE);

        sFinalActionDialogArray = new Utils.FilteredDeviceFeaturesArray ();
        sFinalActionDialogArray = Utils.filterUnsupportedDeviceFeatures(getActivity(),
                getResources().getStringArray(R.array.shortcut_action_screen_off_values),
                getResources().getStringArray(R.array.shortcut_action_screen_off_entries));

        // Attach final settings screen.
        reloadSettings();

        setHasOptionsMenu(true);
    }

    private PreferenceScreen reloadSettings() {
        mCheckPreferences = false;
        PreferenceScreen prefs = getPreferenceScreen();
        if (prefs != null) {
            prefs.removeAll();
        }

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.screen_off_gesture);
        prefs = getPreferenceScreen();

        mEnableDt2w = (SwitchPreference) prefs.findPreference(PREF_DT2W_ENABLE);

        mEnableGestures = (SwitchPreference) prefs.findPreference(PREF_GESTURE_ENABLE);

        mHapticFeedback = (SwitchPreference) findPreference(KEY_GESTURE_HAPTIC_FEEDBACK);
        mHapticFeedback.setChecked(Utils.getIntSystem(getContext(), getActivity().
                getContentResolver(), Utils.TOUCHSCREEN_GESTURE_HAPTIC_FEEDBACK, 1) != 0);
        mHapticFeedback.setOnPreferenceChangeListener(this);

        mGestureDoubleTap = (Preference) prefs.findPreference(PREF_GESTURE_DOUBLE_TAP);
        mGestureW = (Preference) prefs.findPreference(PREF_GESTURE_W);
        mGestureM = (Preference) prefs.findPreference(PREF_GESTURE_M);
        mGestureCircle = (Preference) prefs.findPreference(PREF_GESTURE_CIRCLE);
        mGestureTwoSwipe = (Preference) prefs.findPreference(PREF_GESTURE_TWO_SWIPE);
        mGestureUpArrow = (Preference) prefs.findPreference(PREF_GESTURE_UP_ARROW);
        mGestureDownArrow = (Preference) prefs.findPreference(PREF_GESTURE_DOWN_ARROW);
        mGestureLeftArrow = (Preference) prefs.findPreference(PREF_GESTURE_LEFT_ARROW);
        mGestureRightArrow = (Preference) prefs.findPreference(PREF_GESTURE_RIGHT_ARROW);
        mGestureSwipeUp = (Preference) prefs.findPreference(PREF_GESTURE_SWIPE_UP);
        mGestureSwipeDown = (Preference) prefs.findPreference(PREF_GESTURE_SWIPE_DOWN);
        mGestureSwipeLeft = (Preference) prefs.findPreference(PREF_GESTURE_SWIPE_LEFT);
        mGestureSwipeRight = (Preference) prefs.findPreference(PREF_GESTURE_SWIPE_RIGHT);

        setupOrUpdatePreference(mGestureDoubleTap, mScreenOffGestureSharedPreferences
                .getString(PREF_GESTURE_DOUBLE_TAP, ActionConstants.ACTION_WAKE_DEVICE));
        setupOrUpdatePreference(mGestureW, mScreenOffGestureSharedPreferences
                .getString(PREF_GESTURE_W, ActionConstants.ACTION_CAMERA));
        setupOrUpdatePreference(mGestureM, mScreenOffGestureSharedPreferences
                .getString(PREF_GESTURE_M, ActionConstants.ACTION_MEDIA_PLAY_PAUSE));
        setupOrUpdatePreference(mGestureCircle, mScreenOffGestureSharedPreferences
                .getString(PREF_GESTURE_CIRCLE, ActionConstants.ACTION_VIB_SILENT));
        setupOrUpdatePreference(mGestureTwoSwipe, mScreenOffGestureSharedPreferences
                .getString(PREF_GESTURE_TWO_SWIPE, ActionConstants.ACTION_MEDIA_PREVIOUS));
        setupOrUpdatePreference(mGestureUpArrow, mScreenOffGestureSharedPreferences
                .getString(PREF_GESTURE_UP_ARROW, ActionConstants.ACTION_MEDIA_NEXT));
        setupOrUpdatePreference(mGestureDownArrow, mScreenOffGestureSharedPreferences
                .getString(PREF_GESTURE_DOWN_ARROW, ActionConstants.ACTION_MEDIA_NEXT));
        setupOrUpdatePreference(mGestureLeftArrow, mScreenOffGestureSharedPreferences
                .getString(PREF_GESTURE_LEFT_ARROW, ActionConstants.ACTION_MEDIA_NEXT));
        setupOrUpdatePreference(mGestureRightArrow, mScreenOffGestureSharedPreferences
                .getString(PREF_GESTURE_RIGHT_ARROW, ActionConstants.ACTION_MEDIA_NEXT));
        setupOrUpdatePreference(mGestureSwipeUp, mScreenOffGestureSharedPreferences
                    .getString(PREF_GESTURE_SWIPE_UP, ActionConstants.ACTION_WAKE_DEVICE));
        setupOrUpdatePreference(mGestureSwipeDown, mScreenOffGestureSharedPreferences
                .getString(PREF_GESTURE_SWIPE_DOWN, ActionConstants.ACTION_VIB_SILENT));
        setupOrUpdatePreference(mGestureSwipeLeft, mScreenOffGestureSharedPreferences
                .getString(PREF_GESTURE_SWIPE_LEFT, ActionConstants.ACTION_MEDIA_PREVIOUS));
        setupOrUpdatePreference(mGestureSwipeRight, mScreenOffGestureSharedPreferences
                .getString(PREF_GESTURE_SWIPE_RIGHT, ActionConstants.ACTION_MEDIA_NEXT));

        boolean enableDt2w =
                mScreenOffGestureSharedPreferences.getBoolean(PREF_DT2W_ENABLE, true);
        mEnableDt2w.setChecked(enableDt2w);
        mEnableDt2w.setOnPreferenceChangeListener(this);

        boolean enableGestures =
                mScreenOffGestureSharedPreferences.getBoolean(PREF_GESTURE_ENABLE, true);
        mEnableGestures.setChecked(enableGestures);
        mEnableGestures.setOnPreferenceChangeListener(this);

        mCheckPreferences = true;
        return prefs;
    }

    private void setupOrUpdatePreference(Preference preference, String action) {
        if (preference == null || action == null) {
            return;
        }

        if (action.startsWith("**")) {
            preference.setSummary(getDescription(action));
        } else {
            preference.setSummary(AppHelper.getFriendlyNameForUri(
                    getActivity(), getActivity().getPackageManager(), action));
        }
        preference.setOnPreferenceClickListener(this);
    }

    private String getDescription(String action) {
        if (sFinalActionDialogArray == null || action == null) {
            return null;
        }
        int i = 0;
        for (String actionValue : sFinalActionDialogArray.values) {
            if (action.equals(actionValue)) {
                return sFinalActionDialogArray.entries[i];
            }
            i++;
        }
        return null;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String settingsKey = null;
        int dialogTitle = 0;
	    if (preference == mGestureDoubleTap) {
            settingsKey = PREF_GESTURE_DOUBLE_TAP;
            dialogTitle = R.string.gesture_double_tap_title;
        } else if (preference == mGestureW) {
            settingsKey = PREF_GESTURE_W;
            dialogTitle = R.string.gesture_w_title;
        } else if (preference == mGestureM) {
            settingsKey = PREF_GESTURE_M;
            dialogTitle = R.string.gesture_m_title;
        } else if (preference == mGestureCircle) {
            settingsKey = PREF_GESTURE_CIRCLE;
            dialogTitle = R.string.gesture_circle_title;
        } else if (preference == mGestureTwoSwipe) {
            settingsKey = PREF_GESTURE_TWO_SWIPE;
            dialogTitle = R.string.gesture_two_swipe_title;
        } else if (preference == mGestureUpArrow) {
            settingsKey = PREF_GESTURE_UP_ARROW;
            dialogTitle = R.string.gesture_up_arrow_title;
        } else if (preference == mGestureDownArrow) {
            settingsKey = PREF_GESTURE_DOWN_ARROW;
            dialogTitle = R.string.gesture_down_arrow_title;
        } else if (preference == mGestureLeftArrow) {
            settingsKey = PREF_GESTURE_LEFT_ARROW;
            dialogTitle = R.string.gesture_left_arrow_title;
        } else if (preference == mGestureRightArrow) {
            settingsKey = PREF_GESTURE_RIGHT_ARROW;
            dialogTitle = R.string.gesture_right_arrow_title;
        } else if (preference == mGestureSwipeUp) {
            settingsKey = PREF_GESTURE_SWIPE_UP;
            dialogTitle = R.string.gesture_swipe_up_title;
        } else if (preference == mGestureSwipeDown) {
            settingsKey = PREF_GESTURE_SWIPE_DOWN;
            dialogTitle = R.string.gesture_swipe_down_title;
        } else if (preference == mGestureSwipeLeft) {
            settingsKey = PREF_GESTURE_SWIPE_LEFT;
            dialogTitle = R.string.gesture_swipe_left_title;
        } else if (preference == mGestureSwipeRight) {
            settingsKey = PREF_GESTURE_SWIPE_RIGHT;
            dialogTitle = R.string.gesture_swipe_right_title;
        }
        if (settingsKey != null) {
            showDialogInner(DLG_SHOW_ACTION_DIALOG, settingsKey, dialogTitle);
            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (!mCheckPreferences) {
            return false;
        }
        if (preference == mEnableDt2w) {
            mScreenOffGestureSharedPreferences.edit()
                    .putBoolean(PREF_DT2W_ENABLE, (Boolean) newValue).commit();
            KernelControl.enableDt2w((Boolean) newValue);
            return true;
        }
        if (preference == mEnableGestures) {
            mScreenOffGestureSharedPreferences.edit()
                    .putBoolean(PREF_GESTURE_ENABLE, (Boolean) newValue).commit();
            KernelControl.enableGestures((Boolean) newValue);
            return true;
        }
        final String key = preference.getKey();
        if (KEY_GESTURE_HAPTIC_FEEDBACK.equals(key)) {
                final boolean value = (boolean) newValue;
                Utils.putIntSystem(getContext(), getActivity().getContentResolver(),
                        Utils.TOUCHSCREEN_GESTURE_HAPTIC_FEEDBACK, value ? 1 : 0);
                return true;
        }
        return false;
    }

    // Reset all entries to default.
    private void resetToDefault() {
        SharedPreferences.Editor editor = mScreenOffGestureSharedPreferences.edit();

        mScreenOffGestureSharedPreferences.edit()
                .putBoolean(PREF_DT2W_ENABLE, true).commit();
        mScreenOffGestureSharedPreferences.edit()
                .putBoolean(PREF_GESTURE_ENABLE, true).commit();

        editor.putString(PREF_GESTURE_DOUBLE_TAP,
                ActionConstants.ACTION_WAKE_DEVICE).commit();
        editor.putString(PREF_GESTURE_W,
                ActionConstants.ACTION_CAMERA).commit();
        editor.putString(PREF_GESTURE_M,
                ActionConstants.ACTION_MEDIA_PLAY_PAUSE).commit();
        editor.putString(PREF_GESTURE_CIRCLE,
                ActionConstants.ACTION_VIB_SILENT).commit();
        editor.putString(PREF_GESTURE_TWO_SWIPE,
                ActionConstants.ACTION_MEDIA_PREVIOUS).commit();
        editor.putString(PREF_GESTURE_UP_ARROW,
                ActionConstants.ACTION_WAKE_DEVICE).commit();
        editor.putString(PREF_GESTURE_DOWN_ARROW,
                ActionConstants.ACTION_VIB_SILENT).commit();
        editor.putString(PREF_GESTURE_LEFT_ARROW,
                ActionConstants.ACTION_MEDIA_PREVIOUS).commit();
        editor.putString(PREF_GESTURE_RIGHT_ARROW,
                ActionConstants.ACTION_MEDIA_NEXT).commit();
		editor.putString(PREF_GESTURE_SWIPE_UP,
                ActionConstants.ACTION_WAKE_DEVICE).commit();
        editor.putString(PREF_GESTURE_SWIPE_DOWN,
                ActionConstants.ACTION_VIB_SILENT).commit();
        editor.putString(PREF_GESTURE_SWIPE_LEFT,
                ActionConstants.ACTION_MEDIA_PREVIOUS).commit();
        editor.putString(PREF_GESTURE_SWIPE_RIGHT,
                ActionConstants.ACTION_MEDIA_NEXT).commit();
        mHapticFeedback.setChecked(true);
        editor.commit();
        KernelControl.enableDt2w(true);
        KernelControl.enableGestures(true);
        reloadSettings();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void shortcutPicked(String action,
                String description, Bitmap bmp, boolean isApplication) {
        if (mPendingSettingsKey == null || action == null) {
            return;
        }
        mScreenOffGestureSharedPreferences.edit().putString(mPendingSettingsKey, action).commit();
        reloadSettings();
        mPendingSettingsKey = null;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ShortcutPickerHelper.REQUEST_PICK_SHORTCUT
                    || requestCode == ShortcutPickerHelper.REQUEST_PICK_APPLICATION
                    || requestCode == ShortcutPickerHelper.REQUEST_CREATE_SHORTCUT) {
                mPicker.onActivityResult(requestCode, resultCode, data);

            }
        } else {
            mPendingSettingsKey = null;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_RESET:
                    showDialogInner(DLG_RESET_TO_DEFAULT, null, 0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, MENU_RESET, 0, R.string.reset)
                .setIcon(R.drawable.ic_settings_reset)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    private void showDialogInner(int id, String settingsKey, int dialogTitle) {
        DialogFragment newFragment =
                MyAlertDialogFragment.newInstance(id, settingsKey, dialogTitle);
        newFragment.setTargetFragment(this, 0);
        newFragment.show(getFragmentManager(), "dialog " + id);
    }

    public static class MyAlertDialogFragment extends DialogFragment {

        public static MyAlertDialogFragment newInstance(
                int id, String settingsKey, int dialogTitle) {
            MyAlertDialogFragment frag = new MyAlertDialogFragment();
            Bundle args = new Bundle();
            args.putInt("id", id);
            args.putString("settingsKey", settingsKey);
            args.putInt("dialogTitle", dialogTitle);
            frag.setArguments(args);
            return frag;
        }

        ScreenOffGesture getOwner() {
            return (ScreenOffGesture) getTargetFragment();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int id = getArguments().getInt("id");
            final String settingsKey = getArguments().getString("settingsKey");
            int dialogTitle = getArguments().getInt("dialogTitle");
            switch (id) {
                case DLG_SHOW_ACTION_DIALOG:
                    if (sFinalActionDialogArray == null) {
                        return null;
                    }
                    return new AlertDialog.Builder(getActivity())
                    .setTitle(dialogTitle)
                    .setNegativeButton(R.string.cancel, null)
                    .setItems(getOwner().sFinalActionDialogArray.entries,
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            if (getOwner().sFinalActionDialogArray.values[item]
                                    .equals(ActionConstants.ACTION_APP)) {
                                if (getOwner().mPicker != null) {
                                    getOwner().mPendingSettingsKey = settingsKey;
                                    getOwner().mPicker.pickShortcut(getOwner().getId());
                                }
                            } else {
                                getOwner().mScreenOffGestureSharedPreferences.edit()
                                        .putString(settingsKey,
                                        getOwner().sFinalActionDialogArray.values[item]).commit();
                                getOwner().reloadSettings();
                            }
                        }
                    })
                    .create();
                case DLG_RESET_TO_DEFAULT:
                    return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.reset)
                    .setMessage(R.string.reset_message)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.dlg_ok,
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            getOwner().resetToDefault();
                        }
                    })
                    .create();
            }
            throw new IllegalArgumentException("unknown id " + id);
        }

        @Override
        public void onCancel(DialogInterface dialog) {
        }
    }

}
