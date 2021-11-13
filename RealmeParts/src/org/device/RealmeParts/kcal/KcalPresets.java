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

package org.device.RealmeParts.kcal;

public class KcalPresets {

    public static final String[] red = {"256", "225", "250", "240", "256", "250", "250", "236", "256", "253", "226", "225", "230", "237", "245", "240", "230", "217", "237", "230"};
    public static final String[] green = {"256", "245", "250", "240", "250", "250", "250", "248", "256", "246", "215", "225", "232", "235", "245", "256", "240", "215", "239", "232"};
    public static final String[] blue = {"256", "256", "235", "240", "251", "256", "256", "256", "256", "243", "256", "237", "255", "255", "256", "256", "255", "255", "256", "255"};
    public static final String[] satIntensity = {"255", "264", "251", "257", "290", "284", "257", "274", "289", "274", "264", "277", "277", "265", "273", "290", "285", "265", "270", "274"};
    public static final String[] scrContrast = {"255", "255", "260", "255", "260", "264", "264", "258", "264", "258", "260", "264", "243", "255", "255", "264", "270", "250", "275", "268"};
    public static final String[] scrHue = {"0", "0", "1520", "0", "1526", "0", "0", "0", "0", "0", "10", "0", "0", "1515", "10", "0", "0", "0", "0", "0"};
    public static final String[] scrValue = {"255", "255", "240", "255", "264", "245", "245", "251", "242", "251", "247", "250", "273", "253", "265", "242", "240", "253", "247", "247"};

    enum Presets {
        DEFAULT, VERSION1, VERSION2, VERSION3, TRILUMINOUS, DEEPBW, DEEPND, COOLAMOLED, EXTREMEAMOLED, WARMAMOLED, HYBRIDMAMBA, DEEPAMOLED, COLDBRIGHTAMOLED, CLEANDEEP, SHAMSHUNG, NATURE, SHAMSHUNGCOLD, COLDASICE, DEEPSAT, BLACKAF;
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
                case 11:
                    return DEEPAMOLED;
                case 12:
                    return COLDBRIGHTAMOLED;
                case 13:
                    return CLEANDEEP;
                case 14:
                    return SHAMSHUNG;
                case 15:
                    return NATURE;
                case 16:
                    return SHAMSHUNGCOLD;
                case 17:
                    return COLDASICE;
                case 18:
                    return DEEPSAT;
                case 19:
                    return BLACKAF;
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

