/*
* Copyright (C) 2016 The OmniROM Project
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
*/

package com.Galaxy.Features.kcal;

public class KcalPresets {

    public static final String[] red = {"256", "225", "250", "240", "256", "250", "250", "236", "256", "253", "226"};
    public static final String[] green = {"256", "245", "250", "240", "250", "250", "250", "248", "256", "246", "215"};
    public static final String[] blue = {"256", "256", "235", "240", "251", "256", "256", "256", "256", "243", "256"};
    public static final String[] satIntensity = {"255", "264", "251", "257", "290", "284", "257", "274", "289", "274", "264"};
    public static final String[] scrContrast = {"255", "255", "260", "255", "260", "264", "264", "258", "264", "258", "260"};
    public static final String[] scrHue = {"0", "0", "1520", "0", "1526", "0", "0", "0", "0", "0", "10"};
    public static final String[] scrValue = {"255", "255", "240", "255", "264", "245", "245", "251", "242", "251", "247"};

    enum Presets {
        DEFAULT, VERSION1, VERSION2, VERSION3, TRILUMINOUS, DEEPBW, DEEPND, COOLAMOLED, EXTREMEAMOLED, WARMAMOLED, HYBRIDMAMBA;
        public static Presets toEnum(int index) {
            switch (index) {
                case 0:
                    return DEFAULT;
                case 1:
                    return VERSION1;
                case 2:
                    return VERSION2;
                case 3:
                    return VERSION3;
                case 4:
                    return TRILUMINOUS;
                case 5:
                    return DEEPBW;
                case 6:
                    return DEEPND;
                case 7:
                    return COOLAMOLED;
                case 8:
                    return EXTREMEAMOLED;
                case 9:
                    return WARMAMOLED;
                case 10:
                    return HYBRIDMAMBA;
            }
            return null;
        }
    }

    public static void setValue(String value) {
        int index = Integer.parseInt(value);
        DisplayCalibration.setValueRGB(red[index], green[index], blue[index]);
        DisplayCalibration.setValueSat(satIntensity[index]);
        DisplayCalibration.setValueCon(scrContrast[index]);
        DisplayCalibration.setValueHue(scrHue[index]);
        DisplayCalibration.setValueVal(scrValue[index]);
    }
}

