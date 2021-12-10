package com.android.jdhshop.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;

public class ViewUtil {
    private static int mStatusBarHeight;

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        if (context == null) {
            return (int) (dpValue * 2);
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        if (context == null) {
            return (int) (pxValue / 2f);
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 优先使用 getDisplayWidth/getDisplayHeight
     */
    @Deprecated
    public static int getScreenHeight(@NonNull Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * 优先使用 getDisplayWidth/getDisplayHeight
     */
    @Deprecated
    public static int getScreenWidth(@NonNull Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 优先使用 getDisplayWidth/getDisplayHeight
     */
    @Deprecated
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * 优先使用 getDisplayWidth/getDisplayHeight
     */
    @Deprecated
    public static int getScreenRealHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealMetrics(displayMetrics);
        } else {
            wm.getDefaultDisplay().getMetrics(displayMetrics);
        }
        return displayMetrics.heightPixels;
    }

    /**
     * 优先使用 getDisplayWidth/getDisplayHeight
     */
    @Deprecated
    public static int getScreenWidth(@NonNull Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static boolean isFullScreen(Activity activity) {
        return isFullScreen(activity.getWindow());
    }

    private static boolean isFullScreen(Window window) {
        int flag = window.getAttributes().flags;
        return (flag
                & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN;
    }

    public static int getStatusBarHeight(@Nullable Context context) {
        if (mStatusBarHeight > 0) {
            return mStatusBarHeight;
        }
        if (context == null) {
            return mStatusBarHeight;
        }
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            mStatusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        } else {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                field.setAccessible(true);
                int x = Integer.parseInt(field.get(obj).toString());
                mStatusBarHeight = context.getResources().getDimensionPixelSize(x);
            } catch (Throwable e1) {
                e1.printStackTrace();
            }
        }

        if (mStatusBarHeight <= 0) {
            mStatusBarHeight = dip2px(context, 25);
        }

        return mStatusBarHeight;
    }
}
