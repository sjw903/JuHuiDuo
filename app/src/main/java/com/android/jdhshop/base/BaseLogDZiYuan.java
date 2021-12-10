package com.android.jdhshop.base;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.android.jdhshop.R;
import com.android.jdhshop.config.Constants;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * 加载本地图片资源公共类
 */
public class BaseLogDZiYuan {


    public static void LogDingZiYuan(ImageView id, String imageuri){
        Bitmap bitmap1 = getLoacalBitmap(Constants.ZIYUAN_PATH+imageuri+"");
        id.setImageBitmap(bitmap1);
    }

    /**
     * 加载本地图片
     *
     * @param url
     * @return
     */

    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
