package org.device.RealmeParts;

import java.io.File;
import org.device.RealmeParts.Utils;

public final class PerfControl {
    private static String GOVERNOR_LITTLE_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor";
    private static String GOVERNOR_BIG_PATH = "/sys/devices/system/cpu/cpu6/cpufreq/scaling_governor";
    private static String ADRENOBOOST_PATH = "/sys/class/kgsl/kgsl-3d0/devfreq/adrenoboost";
    private static String ADRENOIDLER_ENABLE_PATH = "/sys/module/adreno_idler/parameters/adreno_idler_active";
    private static String ADRENOIDLER_WL_PATH = "/sys/module/adreno_idler/parameters/adreno_idler_idleworkload";
    private static String ADRENOIDLER_DD_PATH = "/sys/module/adreno_idler/parameters/adreno_idler_downdifferential";
    private static String ADRENOIDLER_IW_PATH = "/sys/module/adreno_idler/parameters/adreno_idler_idlewait";
    private static String SCHEDBOOST_ENABLE_PATH = "/sys/module/cpu_boost/parameters/sched_boost_on_input";
    private static String BOOST_FREQ_PATH = "/sys/module/cpu_boost/parameters/input_boost_freq";
    private static String BOOST_MS_PATH = "/sys/module/cpu_boost/parameters/input_boost_ms";
    private static String INPUTBOOST_MS_PATH = "/sys/module/cpu_input_boost/parameters/input_boost_duration";
    private static String INPUTBOOST_FREQ_BIG = "/sys/module/cpu_input_boost/parameters/input_boost_freq_big";
    private static String INPUTBOOST_FREQ_LITTLE = "/sys/module/cpu_input_boost/parameters/input_boost_freq_little";
    private static final String TAG = "PerfControl";

    private PerfControl() {
        // this class is not supposed to be instantiated
    }

    public static void setPerf(int value){
        switch (value) {
            case 0:
                setGovernor("schedutil");
                setAdrenoboost("1");
                setAdrenoidler("Y");
                setAdrenoidlerWL("6000");
                setAdrenoidlerDD("15");
                setAdrenoidlerIW("25");
                setInputBoostFreqBig("1132800");
                setInputBoostFreqLittle("1209600");
                setBoostMS("0");
                setInputboostMS("96");
                setSchedBoost("1");
                break;
            case 1:
                setGovernor("schedutil");
                setAdrenoboost("3");
                setAdrenoidler("N");
                setInputBoostFreqBig("1843200");
                setInputBoostFreqLittle("1612800");
                setBoostMS("0");
                setInputboostMS("600");
                setSchedBoost("1");
                break;
            case 2:
                setGovernor("energy_dcfc");
                setAdrenoboost("0");
                setAdrenoidler("Y");
                setAdrenoidlerWL("10000");
                setAdrenoidlerDD("35");
                setAdrenoidlerIW("15");
                setBoostFreq("0:998400 1:998400 2:998400 3:998400 4:998400 5:998400 6:825600 7:825600");
                setBoostMS("35");
                setInputboostMS("0");
                setSchedBoost("0");
                break;
            case 3:
                setGovernor("schedutil");
                setAdrenoboost("2");
                setAdrenoidler("Y");
                setAdrenoidlerWL("5000");
                setAdrenoidlerDD("15");
                setAdrenoidlerIW("40");
                setInputBoostFreqBig("1747200");
                setInputBoostFreqLittle("1516800");
                setBoostMS("0");
                setInputboostMS("500");
                setSchedBoost("1");
                break;
        }
       return;
    }

    public static void setGovernor(String value) {
            if (new File(GOVERNOR_LITTLE_PATH).exists()) {
                Utils.writeValue(GOVERNOR_LITTLE_PATH, value);
            }
            if (new File(GOVERNOR_BIG_PATH).exists()) {
                Utils.writeValue(GOVERNOR_BIG_PATH, value);
            }
    }

    public static void setAdrenoboost(String value) {
            if (new File(ADRENOBOOST_PATH).exists()) {
                Utils.writeValue(ADRENOBOOST_PATH, value);
            }
    }

    public static void setAdrenoidler(String value) {
            if (new File(ADRENOIDLER_ENABLE_PATH).exists()) {
                Utils.writeValue(ADRENOIDLER_ENABLE_PATH, value);
            }
    }

    public static void setAdrenoidlerWL(String value) {
            if (new File(ADRENOIDLER_WL_PATH).exists()) {
                Utils.writeValue(ADRENOIDLER_WL_PATH, value);
            }
    }

    public static void setAdrenoidlerDD(String value) {
            if (new File(ADRENOIDLER_DD_PATH).exists()) {
                Utils.writeValue(ADRENOIDLER_DD_PATH, value);
            }
    }
    public static void setAdrenoidlerIW(String value) {
            if (new File(ADRENOIDLER_IW_PATH).exists()) {
                Utils.writeValue(ADRENOIDLER_IW_PATH, value);
            }
    }

    public static void setBoostFreq(String value) {
            if (new File(BOOST_FREQ_PATH).exists()) {
                Utils.writeValue(BOOST_FREQ_PATH, value);
            }
    }

    public static void setBoostMS(String value) {
            if (new File(BOOST_MS_PATH).exists()) {
                Utils.writeValue(BOOST_MS_PATH, value);
            }
    }

    public static void setSchedBoost(String value) {
            if (new File(SCHEDBOOST_ENABLE_PATH).exists()) {
                Utils.writeValue(SCHEDBOOST_ENABLE_PATH, value);
            }
    }

    public static void setInputboostMS(String value) {
            if (new File(INPUTBOOST_MS_PATH).exists()) {
                Utils.writeValue(INPUTBOOST_MS_PATH, value);
            }
    }

    public static void setInputBoostFreqBig(String value) {
            if (new File(INPUTBOOST_FREQ_BIG).exists()) {
                Utils.writeValue(INPUTBOOST_FREQ_BIG, value);
            }
    }

    public static void setInputBoostFreqLittle(String value) {
            if (new File(INPUTBOOST_FREQ_LITTLE).exists()) {
                Utils.writeValue(INPUTBOOST_FREQ_LITTLE, value);
            }
    }
}
