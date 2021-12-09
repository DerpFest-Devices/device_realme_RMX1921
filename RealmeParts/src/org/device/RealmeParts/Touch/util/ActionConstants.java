/*
* Copyright (C) 2013 SlimRoms Project
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

public class ActionConstants {

    // key must fit with the values arrays from Settings to use
    // SlimActions.java actions
    public static final String ACTION_CAMERA                = "**camera**";
    public static final String ACTION_ASSIST                = "**assist**";
    public static final String ACTION_VIB                  = "**ring_vib**";
    public static final String ACTION_SILENT               = "**ring_silent**";
    public static final String ACTION_VIB_SILENT           = "**ring_vib_silent**";
    public static final String ACTION_TORCH                = "**torch**";
    public static final String ACTION_MEDIA_PREVIOUS       = "**media_previous**";
    public static final String ACTION_MEDIA_NEXT           = "**media_next**";
    public static final String ACTION_MEDIA_PLAY_PAUSE     = "**media_play_pause**";
    public static final String ACTION_WAKE_DEVICE          = "**wake_device**";
    public static final String ACTION_AMBIENT_DISPLAY = "**ambient**";
    public static final String ACTION_VOLUME_DOWN = "**vol_down**";
    public static final String ACTION_VOLUME_UP = "**vol_up**";

    // no action
    public static final String ACTION_NULL            = "**null**";

    // this shorcut constant is only used to identify if the user
    // selected in settings a custom app...after it is choosed intent uri
    // is saved in the ButtonConfig object
    public static final String ACTION_APP          = "**app**";

    public static final String ICON_EMPTY = "empty";
    public static final String SYSTEM_ICON_IDENTIFIER = "system_shortcut=";
    public static final String ACTION_DELIMITER = "|";

}
