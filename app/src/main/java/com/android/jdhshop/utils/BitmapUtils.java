package com.android.jdhshop.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * 生成bitmap
 */
public class BitmapUtils {

    /**
     * 将整体ViewGroup生成图片
     * @param v
     * @return
     */
    public static Bitmap createViewBitmap(View v, Context context) {
        Bitmap bitmap = Bitmap.createBitmap( v.getWidth(),v.getHeight(),
                Bitmap.Config.ARGB_8888);
//        Bitmap bitmap = Bitmap.createBitmap( 1000,500,
//                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }
    public static int getScreenWith(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

}
