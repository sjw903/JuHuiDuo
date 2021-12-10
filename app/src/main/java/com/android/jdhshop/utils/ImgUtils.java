package com.android.jdhshop.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import pub.devrel.easypermissions.EasyPermissions;

public class ImgUtils {
    /**
     * 保存文件到指定路径
     */
    public static boolean saveImageToGallery2(Context context, Bitmap bmp,String url){
        /**
         * 先判断有没有权限
         */
        if (Build.VERSION.SDK_INT >= 23) {
            //读取sd卡的权限
            String[] mPermissionList = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (EasyPermissions.hasPermissions(context, mPermissionList)) {
                // 首先保存图片
                String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "hkxsave";
                File appDir = new File(storePath);
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                String fileName =url;
                File file = new File(appDir, fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    //通过io流的方式来压缩保存图片
                    boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
                    fos.flush();
                    fos.close();
                    //把文件插入到系统图库
                    MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
                    //保存图片后发送广播通知更新数据库
                    Uri uri = Uri.fromFile(file);
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                    if (isSuccess) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //未同意过,或者说是拒绝了，再次申请权限
                EasyPermissions.requestPermissions((Activity) context, "保存图片需要读取sd卡的权限",1, mPermissionList);
            }
        } else {
            // 首先保存图片
            String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "hkxsave";
            File appDir = new File(storePath);
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            String fileName =url;
            File file = new File(appDir, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                //通过io流的方式来压缩保存图片
                boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
                fos.flush();
                fos.close();
                //把文件插入到系统图库
                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
                //保存图片后发送广播通知更新数据库
                Uri uri = Uri.fromFile(file);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                if (isSuccess) {
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     * 保存文件到指定路径
     */
    public static boolean saveImageToGallery(Context context, Bitmap bmp){
        /**
         * 先判断有没有权限
         */
        if (Build.VERSION.SDK_INT >= 23) {
            //读取sd卡的权限
            String[] mPermissionList = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (EasyPermissions.hasPermissions(context, mPermissionList)) {
                // 首先保存图片
                String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "hkxsave";
                File appDir = new File(storePath);
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                String fileName = System.currentTimeMillis() + ".jpg";
                File file = new File(appDir, fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    //通过io流的方式来压缩保存图片
                    boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
                    fos.flush();
                    fos.close();
                    //把文件插入到系统图库
                    //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
                    //保存图片后发送广播通知更新数据库
                    Uri uri = Uri.fromFile(file);
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                    if (isSuccess) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //未同意过,或者说是拒绝了，再次申请权限
                EasyPermissions.requestPermissions((Activity) context, "保存图片需要读取sd卡的权限",1, mPermissionList);
            }
        } else {
            // 首先保存图片
            String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "hkxsave";
            File appDir = new File(storePath);
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            String fileName = System.currentTimeMillis() + ".jpg";
            File file = new File(appDir, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                //通过io流的方式来压缩保存图片
                boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
                fos.flush();
                fos.close();
                //把文件插入到系统图库
                //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
                //保存图片后发送广播通知更新数据库
                Uri uri = Uri.fromFile(file);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                if (isSuccess) {
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     * 保存文件到指定路径
     */
    public static String saveImageToGallery2(Context context, Bitmap bmp){
        /**
         * 先判断有没有权限
         */
        if (Build.VERSION.SDK_INT >= 23) {
            //读取sd卡的权限
            String[] mPermissionList = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (EasyPermissions.hasPermissions(context, mPermissionList)) {
                // 首先保存图片
                String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "hkxsave";
                File appDir = new File(storePath);
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                String fileName = System.currentTimeMillis() + ".jpg";
                File file = new File(appDir, fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    //通过io流的方式来压缩保存图片
                    boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
                    fos.flush();
                    fos.close();
                    //把文件插入到系统图库
                    //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
                    //保存图片后发送广播通知更新数据库
                    Uri uri = Uri.fromFile(file);
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                    if (isSuccess) {
                        return file.getPath();
                    } else {
                        return "";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //未同意过,或者说是拒绝了，再次申请权限
                EasyPermissions.requestPermissions((Activity) context, "保存图片需要读取sd卡的权限",1, mPermissionList);
            }
        } else {
            // 首先保存图片
            String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "hkxsave";
            File appDir = new File(storePath);
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            String fileName = System.currentTimeMillis() + ".jpg";
            File file = new File(appDir, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                //通过io流的方式来压缩保存图片
                boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
                fos.flush();
                fos.close();
                //把文件插入到系统图库
                //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
                //保存图片后发送广播通知更新数据库
                Uri uri = Uri.fromFile(file);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                if (isSuccess) {
                    return file.getPath();
                } else {
                    return "";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}