package org.device.RealmeParts.vibrator.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public final class VibrateFileUtils {

    public static boolean fileWritable(String filename) {
        return fileExists(filename) && new File(filename).canWrite();
    }

    public static boolean fileExists(String filename) {
        if (filename == null) {
            return false;
        }
        return new File(filename).exists();
    }

    public static void setValue(String path, int value) {
        if (fileWritable(path)) {
            if (path == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(new File(path));
                fos.write(Integer.toString(value).getBytes());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setValue(String path, double value) {
        if (fileWritable(path)) {
            if (path == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(new File(path));
                fos.write(Long.toString(Math.round(value)).getBytes());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setValue(String path, String value) {
        if (fileWritable(path)) {
            if (path == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(new File(path));
                fos.write(value.getBytes());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
