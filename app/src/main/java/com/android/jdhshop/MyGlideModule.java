package com.android.jdhshop;

import android.content.Context;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;

public class MyGlideModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

//        设置缓存大小为20mb
        int memoryCacheSizeBytes = 1024 * 1024 * 20; // 20mb
        //        设置内存缓存大小
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));
        //        根据SD卡是否可用选择是在内部缓存还是SD卡缓存
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, "huikexiongImg", memoryCacheSizeBytes));
        }else {
            builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "huikexiongImg", memoryCacheSizeBytes));
        }
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
