package com.android.jdhshop.ziyuan;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * 加载外部资源包资源的管理类
 */
public class ResourceManager {
    private static final String TAG = "ResourceManager";
 
    private Context mContext;
    private Resources mResources;
    private String mPackageName;
    public static volatile ResourceManager mInstance;
 
    private ResourceManager() {
    }
 
    public static ResourceManager getInstance() {
        if (mInstance == null) {
            synchronized (ResourceManager.class) {
                if (mInstance == null) {
                    mInstance = new ResourceManager();
                }
            }
        }
        return mInstance;
    }
 
    public void loadResource(Context context, String path) {
        // 这里上下文实例持有的是Application引用，避免传入来的是Activity，造成内存泄漏
        mContext = context.getApplicationContext();
        try {
            // 获取资源包的包名
            PackageManager packageManager = mContext.getPackageManager();
            PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
            mPackageName = packageArchiveInfo.packageName;
 
            AssetManager assetManager = AssetManager.class.newInstance();
            // 把path指定apk文件作为资源加入到AssetManager里
            Method addAssetPath = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, path);
            // 初始化Resources，提供后续查找指定path的apk文件里的资源。
            // 因为AssetManager.addAssetPath(path)通过反射已经把apk的资源信息添加到了AssetManager里
            mResources = new Resources(assetManager, mContext.getResources().getDisplayMetrics(), mContext.getResources().getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,e.toString());
        }
    }
 
    /**
     * 根据资源名称和类型获取对应的资源id
     *
     * @param resName
     * @param defType
     * @return
     */
    public int getResourceId(String resName, String defType) {
        if (mResources == null) {
            return 0;
        }
 
        // 根据资源类型和资源名，去资源包里查找对应资源名字的资源id
        return mResources.getIdentifier(resName, defType, mPackageName);
    }
 
    /**
     * 获取对应资源的Drawable实例
     *
     * @param resName
     * @param defType
     * @return
     */
    public Drawable getDrawable(String resName, String defType) {
        int id = getResourceId(resName, defType);
        if (id == 0) {
            return null;
        }
        return mResources.getDrawable(id);
    }
 
    /**
     * 获取指定的颜色值
     *
     * @param resName 颜色资源名
     * @param defType
     * @return
     */
    public int getColor(String resName, String defType) {
        int id = getResourceId(resName, defType);
        if (id == 0) {
            return 0;
        }
        return mResources.getColor(id);
    }
 
}