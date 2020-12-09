/*
 * Copyright (c) 2015 The CyanogenMod Project
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

package com.Galaxy.Features.kcal;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.SharedPreferences;
import android.os.UserHandle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import java.lang.Math;

public final class Utils {

	    /**
     * Write a string value to the specified file.
     * @param filename      The filename
     * @param value         The value
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

    public static boolean fileExists(String filename) {
        return new File(filename).exists();
    }

    public static boolean fileWritable(String filename) {
        return fileExists(filename) && new File(filename).canWrite();
    }

    /**
     * Convert color temperature in Kelvins to ColorMatrix color
     * @param temperature
     * @return array of ColorMatrix (R, G, B)
     */
    public static int[] RGBfromK(int temperature) {
        int[] rgb = new int[3];
        temperature = temperature / 100;
        double red;
        double green;
        double blue;
        // R
        if (temperature <=66 )red = 255;
        else {
            red = temperature - 60;
            red = 329.698727446 * (Math.pow (red, -0.1332047592));
            if (red < 0) red = 0;
            if (red > 255) red = 255;
        }
        // G
        if (temperature <= 66){
            green = temperature;
            green = 99.4708025861 * Math.log(green) - 161.1195681661;
            if (green < 0) green = 0;
            if (green > 255) green = 255;
        }
        else {
            green = temperature - 60;
            green = 288.1221695283 * (Math.pow(green, -0.0755148492));
            if (green < 0) green = 0;
            if (green > 255) green = 255;
        }

        // B
        if (temperature >= 66) blue = 255;
        else
        if (temperature <= 19) blue = 0;
        else {
            blue = temperature - 10;
            blue = 138.5177312231 * Math.log(blue) - 305.0447927307;
            if (blue < 0) blue = 0;
            if (blue > 255) blue = 255;
        }

        rgb[0] = (int) red;
        rgb[1] = (int) green;
        rgb[2] = (int) blue;
        Log.e("RGBfromK",""+temperature+" "+red+" "+" "+green+" "+blue);
        return rgb;
    }

    public static int KfromRGB(double R, double G, double B) {
        double r, g, b, X, Y, Z, xr, yr, zr;

        // D65/2°
        double Xr = 95.047;
        double Yr = 100.0;
        double Zr = 108.883;

        r = R/255.0;
        g = G/255.0;
        b = B/255.0;

        if (r > 0.04045)
            r = Math.pow((r+0.055)/1.055,2.4);
        else
            r = r/12.92;

        if (g > 0.04045)
            g = Math.pow((g+0.055)/1.055,2.4);
        else
            g = g/12.92;

        if (b > 0.04045)
            b = Math.pow((b+0.055)/1.055,2.4);
        else
            b = b/12.92 ;

        r*=100;
        g*=100;
        b*=100;

        X =  0.4124*r + 0.3576*g + 0.1805*b;
        Y =  0.2126*r + 0.7152*g + 0.0722*b;
        Z =  0.0193*r + 0.1192*g + 0.9505*b;

        double x =  (X/(X+Y+Z));
        double y =  (Y/(X+Y+Z));

        int CCT=(int) ((-449*Math.pow((x-0.332)/(y-0.1858), 3))+(3525*Math.pow((x-0.332)/(y-0.1858), 2))-(6823.3*((x-0.332)/(y-0.1858)))+(5520.33));
        return CCT;
    }

    public static double clamp(double x, double min, double max) {
        if (x < min) {
            return min;
        }
        if (x > max) {
            return max;
        }
        return x;
    }

}
