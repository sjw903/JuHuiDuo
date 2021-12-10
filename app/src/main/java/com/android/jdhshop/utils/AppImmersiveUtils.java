package com.android.jdhshop.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AppImmersiveUtils {

    /**
     * 联盟沉浸式状态，这个只有23以上才支持
     */
    public static boolean canStartImmersiveMode() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 需要剔除全屏模式，目前看这种方法判断没啥问题
     */
    public static boolean isImmersiveMode(@NonNull Activity activity) {
        if (canStartImmersiveMode()) {
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if ((activity.getWindow().getDecorView().getSystemUiVisibility() & option) == option
                    && !ViewUtil.isFullScreen(activity)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param activity
     * @param color 状态栏背景色
     * @param dark 状态栏字体图标是否采用黑色
     */
    public static void startImmersiveMode(@NonNull Activity activity, int color, boolean dark) {
        startImmersiveMode(activity, color, dark, true);
    }

    /**
     * 此方法适用于背景全屏展示的页面
     *
     * @param activity
     * @param view 需要偏移的view
     * @param dark 状态栏字体图标是否采用黑色
     */
    public static void startImmersiveMode(@NonNull Activity activity, @NonNull View view,
                                          boolean dark) {
        if (canStartImmersiveMode()) {
            startImmersiveMode(activity, Color.TRANSPARENT, dark, true);
            view.setTranslationY(ViewUtil.getStatusBarHeight(activity));
        }
    }

    public static void adjustViewPaddingTop(@NonNull Context context, @NonNull View view) {
        view.setPadding(view.getPaddingLeft(), ViewUtil.getStatusBarHeight(context),
                view.getPaddingRight(), view.getPaddingBottom());
    }

    /**
     * @param activity
     * @param color 状态栏背景色
     * @param dark 状态栏字体图标是否采用黑色
     * @param customImmersiveMode 是否自定义沉浸式，否表示Content View会设置一个高度为状态栏高度的padding，
     *          这是快手的默认实现方案，代码在GifshowActivity里面
     */
    public static void startImmersiveMode(@NonNull Activity activity, int color, boolean dark,
                                          boolean customImmersiveMode) {
        if (!canStartImmersiveMode()) {
            return;
        }
        adjustStatusBar(activity, color, dark);
        if (!customImmersiveMode) {
            View view = activity.findViewById(android.R.id.content);
            view.setPadding(0, ViewUtil.getStatusBarHeight(activity), 0, 0);
        }
    }

    /**
     * 真正实现沉浸式的代码
     *
     * @param activity
     * @param color
     * @param dark
     */
    public static void adjustStatusBar(@NonNull Activity activity, int color, boolean dark) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if (dark && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                option |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                if (RomUtils.isMiui()) {
                    setMIUILightStatusBar(activity, true);
                } else if (RomUtils.isFlyme()) {
                    MeiZuStatusBarColorUtils.setStatusBarDarkIcon(activity, true);
                }
            }
            window.getDecorView().setSystemUiVisibility(option);
            window.setStatusBarColor(color);
            // forcedNavigationBarColor
            window.setNavigationBarColor(window.getNavigationBarColor());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.getDecorView().setSystemUiVisibility(option);
        }
    }

    public static void setStatusBarColor(Activity activity, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (RomUtils.isMiui()) {
                setMIUILightStatusBar(activity, true);
            } else if (RomUtils.isFlyme()) {
                MeiZuStatusBarColorUtils.setStatusBarDarkIcon(activity, true);
            }
            window.setStatusBarColor(color);
        }
    }

    /**
     * MIUI实现沉浸式方法
     *
     * @param activity
     * @param dark
     * @return
     */
    public static boolean setMIUILightStatusBar(@NonNull Activity activity, boolean dark) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), dark ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
