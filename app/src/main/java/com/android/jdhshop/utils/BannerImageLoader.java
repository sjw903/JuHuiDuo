package com.android.jdhshop.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.common.CommonUtils;
import com.youth.banner.loader.ImageLoader;

/**
 * 开发者：陈飞
 * 时间:2018/7/16 08:45
 * 说明：导航头部图片加载器
 */
public class BannerImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        if(context!=null&&!((Activity)context).isDestroyed()) {
            Glide.with(context).load(path).override(CommonUtils.getScreenWidth(),320).dontAnimate().into(imageView);
        }
    }
}
