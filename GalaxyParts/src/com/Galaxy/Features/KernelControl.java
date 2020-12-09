/*
 * Copyright (C) 2014 SlimRoms Project
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

package com.Galaxy.Features;

import com.Galaxy.Features.Touch.util.Utils;
import java.io.File;

/*
 * Very ugly class which enables or disables for now
 * all gesture controls on kernel level.
 * We need to do it this way for now to do not break 3rd party kernel.
 * Kernel should have a better per gesture control but as long
 * this is not changed by the manufacture we would break gesture control on every
 * 3rd party kernel. Hence we do it this way for now.
 */

public final class KernelControl {

    private static String GESTURE_PATH = "/proc/touchpanel/gesture_enable";
    private static String DT2W_PATH = "/proc/touchpanel/double_tap_enable";

    private KernelControl() {
        // this class is not supposed to be instantiated
    }

    /**
     * Enable or disable gesture control.
     */
    public static void enableGestures(boolean enable) {
            if (new File(GESTURE_PATH).exists()) {
                Utils.writeLine(GESTURE_PATH, enable ? "1" : "0");
            }
    }

    public static void enableDt2w(boolean enable) {
            if (new File(GESTURE_PATH).exists()) {
                Utils.writeLine(DT2W_PATH, enable ? "1" : "0");
            }
    }

    /**
     * Do we have touch control at all?
     */
    public static boolean hasTouchscreenGestures() {
        return new File(GESTURE_PATH).exists();
    }

}
