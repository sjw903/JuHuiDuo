package com.android.jdhshop.merchantactivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;

public class MapUtil {

    public final static String BAIDU_PKG = "com.baidu.BaiduMap"; //百度地图的包名

    public final static String GAODE_PKG = "com.autonavi.minimap";//高德地图的包名

    public static void openGaoDe(Context context, double latitude, double longitude) {
        Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("androidamap://navi?sourceApplication=众惠宝&lat=" + latitude + "&lon=" + longitude + "&dev=0"));
        intent.setPackage("com.autonavi.minimap");
        context.startActivity(intent);
    }
//
//    public static void openBaidu(Context context, MarkerOptions markerOption) {
//        Intent i1 = new Intent();
//        double[] position = GPSUtil.gcj02_To_Bd09(markerOption.getPosition().latitude, markerOption.getPosition().longitude);
//        i1.setData(Uri.parse("baidumap://map/geocoder?location=" + position[0] + "," + position[1]));
//        context.startActivity(i1);
//    }

    /**
     * 检测地图应用是否安装
     *
     * @param context
     * @param packagename
     * @return
     */
    public static boolean checkMapAppsIsExist(Context context, String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (Exception e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }
}
