package com.android.jdhshop.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RomUtils {

    private static final String ROM_MIUI = "MIUI";
    private static final String ROM_EMUI = "EMUI";
    private static final String ROM_FLYME = "FLYME";
    private static final String ROM_OPPO = "OPPO";
    private static final String ROM_SMARTISAN = "SMARTISAN";
    private static final String ROM_VIVO = "VIVO";
    private static final String ROM_QIKU = "QIKU";
    private static final String ROM_360 = "360";
    private static final String ROM_ONEPLUS = "OnePlus";
    private static final String ROM_SAMSUNG = "SAMSUNG";

    private static final String KEY_VERSION_MIUI = "ro.miui.ui.version.name";
    private static final String KEY_VERSION_EMUI = "ro.build.version.emui";
    private static final String KEY_VERSION_OPPO = "ro.build.version.opporom";
    private static final String KEY_VERSION_SMARTISAN = "ro.smartisan.version";
    private static final String KEY_VERSION_VIVO = "ro.vivo.os.version";
    private static final String KEY_VERSION_ONEPLUS = "ro.product.system.manufacturer";
    private static final String KEY_VERSION_SAMSUNG = "ro.product.manufacturer";

    private static String sName;
    private static String sVersion;

    public static boolean isEmui() {
        return check(ROM_EMUI);
    }

    public static boolean isMiui() {
        return check(ROM_MIUI);
    }

    public static boolean isVivo() {
        return check(ROM_VIVO);
    }

    public static boolean isOppo() {
        return check(ROM_OPPO);
    }

    public static boolean isOnePlus() {
        return check(ROM_ONEPLUS);
    }

    public static boolean isFlyme() {
        return check(ROM_FLYME);
    }

    public static boolean is360() {
        return check(ROM_QIKU) || check(ROM_360);
    }

    public static boolean isSmartisan() {
        return check(ROM_SMARTISAN);
    }

    public static boolean isSamsung() {
        return check(ROM_SAMSUNG);
    }

    public static String getName() {
        if (sName == null) {
            check("");
        }
        return sName;
    }

    public static String getVersion() {
        if (sVersion == null) {
            check("");
        }
        return sVersion;
    }

    public static boolean check(String rom) {
        if (sName != null) {
            return sName.contains(rom);
        }

        if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_OPPO))) {
            sName = ROM_OPPO;
        } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_VIVO))) {
            sName = ROM_VIVO;
        } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_EMUI))) {
            sName = ROM_EMUI;
        } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_MIUI))) {
            sName = ROM_MIUI;
        } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_ONEPLUS))) {
            sName = ROM_ONEPLUS;
        } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_SMARTISAN))) {
            sName = ROM_SMARTISAN;
        } else if (getProp(KEY_VERSION_SAMSUNG).toUpperCase().contains(ROM_SAMSUNG)) {
            sName = ROM_SAMSUNG;
        } else {
            sVersion = Build.DISPLAY;
            if (sVersion.toUpperCase().contains(ROM_FLYME)) {
                sName = ROM_FLYME;
            } else {
                sVersion = Build.UNKNOWN;
                sName = Build.MANUFACTURER.toUpperCase();
            }
        }
        return sName.contains(rom);
    }

    @SuppressLint("PrivateApi")
    public static String getProp(String key) {
        String properties = null;
        Class<?> clazz;
        try {
            clazz = Class.forName("android.os.SystemProperties");
            Method method = clazz.getDeclaredMethod("get", String.class);
            properties = (String) method.invoke(clazz, key);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(properties)) {
            BufferedReader input = null;
            try {
                Process p = Runtime.getRuntime().exec("getprop " + key);
                input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
                properties = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (input != null) {
                        input.close();
                    }
                } catch (IOException ioe) {
                    // ignore
                }
            }
        }
        return properties;
    }

    public static boolean isMeitu() {
        try {
            return Build.MANUFACTURER.toUpperCase().contains("MEITU");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean isM5() {
        try {
            return Build.MODEL.toUpperCase().contains("M5");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}