/* * Copyright (C) 2013 The OmniROM Project * * This program is free software: you can redistribute it and/or modify * it under the terms of the GNU General Public License as
published by * the Free Software Foundation, either version 2 of the License, or * (at your option) any later version. * * This program is distributed in the hope that it will
be useful, * but WITHOUT ANY WARRANTY; without even the implied warranty of * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the * GNU General Public License
for more details. * * You should have received a copy of the GNU General Public License * along with this program. If not, see <http://www.gnu.org/licenses/>. * */

package com.Galaxy.Features.Touch.util;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class Utils {

    public static final String TAG = "GalaxyParts";

    public static final String PREFERENCES = "GalaxyPartsPreferences";
    public static final String AMBIENT_GESTURE_HAPTIC_FEEDBACK =
            "AMBIENT_GESTURE_HAPTIC_FEEDBACK";
    public static final String TOUCHSCREEN_GESTURE_HAPTIC_FEEDBACK =
            "TOUCHSCREEN_GESTURE_HAPTIC_FEEDBACK";

    private static final String SETTINGS_METADATA_NAME = "com.android.settings";

    public static FilteredDeviceFeaturesArray filterUnsupportedDeviceFeatures(Context context,
            String[] valuesArray, String[] entriesArray) {
        if (valuesArray == null || entriesArray == null || context == null) {
            return null;
        }
        List<String> finalEntries = new ArrayList<String>();
        List<String> finalValues = new ArrayList<String>();
        FilteredDeviceFeaturesArray filteredDeviceFeaturesArray =
            new FilteredDeviceFeaturesArray();

        for (int i = 0; i < valuesArray.length; i++) {
                finalEntries.add(entriesArray[i]);
                finalValues.add(valuesArray[i]);
        }
        filteredDeviceFeaturesArray.entries =
            finalEntries.toArray(new String[finalEntries.size()]);
        filteredDeviceFeaturesArray.values =
            finalValues.toArray(new String[finalValues.size()]);
        return filteredDeviceFeaturesArray;
    }

    public static class FilteredDeviceFeaturesArray {
        public String[] entries;
        public String[] values;
    }
			
	public static int getIntSystem(Context context, ContentResolver cr, String name, int def) {
        int ret = getInt(context, name, def);
        return ret;
    }

    public static int getInt(Context context, String name, int def) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES, 0);
        return settings.getInt(name, def);
    }

    public static boolean putIntSystem(Context context, ContentResolver cr, String name, int value) {
        boolean ret = putInt(context, name, value);
        return ret;
    }

    public static boolean putInt(Context context, String name, int value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(name, value);
        return editor.commit();
    }

    /**
     * Write a string value to the specified file.
     * @param filename The filename
     * @param value The value
     */
    public static void writeValue(String filename, String value) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(filename));
            fos.write(value.getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Write the "color value" to the specified file. The value is scaled from
     * an integer to an unsigned integer by multiplying by 2.
     * @param filename The filename
     * @param value The value of max value Integer.MAX
     */
    public static void writeColor(String filename, int value) {
        writeValue(filename, String.valueOf((long) value * 2));
    }
    /**
     * Write the "gamma value" to the specified file.
     * @param filename The filename
     * @param value The value
     */
    public static void writeGamma(String filename, int value) {
        writeValue(filename, String.valueOf(value));
    }
    /**
     * Check if the specified file exists.
     * @param filename The filename
     * @return Whether the file exists or not
     */
    public static boolean fileExists(String filename) {
        return new File(filename).exists();
    }
    public static boolean fileWritable(String filename) {
        return fileExists(filename) && new File(filename).canWrite();
    }
    public static String readLine(String filename) {
        BufferedReader br = null;
        String line = null;
        try {
            br = new BufferedReader(new FileReader(filename), 1024);
            line = br.readLine();
        } catch (IOException e) {
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
        return line;
    }
    public static boolean getFileValueAsBoolean(String filename, boolean defValue) {
        String fileValue = readLine(filename);
        if(fileValue!=null){
            return (fileValue.equals("0")?false:true);
        }
        return defValue;
    }
    public static String getFileValue(String filename, String defValue) {
        String fileValue = readLine(filename);
        if(fileValue!=null){
            return fileValue;
        }
        return defValue;
    }
	
	/**
     * Reads the first line of text from the given file
     */
    public static String readOneLine(String fileName) {
        String line = null;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(fileName), 512);
            line = reader.readLine();
        } catch (IOException e) {
            Log.e(TAG, "Could not read from file " + fileName, e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                // ignored, not much we can do anyway
            }
        }

        return line;
    }

    /**
     * Writes the given value into the given file
     *
     * @return true on success, false on failure
     */
    public static boolean writeLine(String fileName, String value) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(value.getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not write to file " + fileName, e);
            return false;
        }

        return true;
    }
	
	public static void registerPreferenceChangeListener(Context context,
            SharedPreferences.OnSharedPreferenceChangeListener preferenceListener) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES, 0);
        settings.registerOnSharedPreferenceChangeListener(preferenceListener);
    }

    public static void unregisterPreferenceChangeListener(Context context,
            SharedPreferences.OnSharedPreferenceChangeListener preferenceListener) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES, 0);
        settings.unregisterOnSharedPreferenceChangeListener(preferenceListener);
    }
}
