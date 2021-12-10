package com.android.jdhshop.wmm;

import android.app.Activity;
import android.os.Bundle;

import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;

public class QQShareUtil {
    private static Bundle params;
    private static Tencent mTencent;

    static {
        mTencent = Tencent.createInstance("1107989076", CaiNiaoApplication.getAppContext());
    }

    public static void shareToQQ(String title, String summary, String target_url, ArrayList<String> list, Activity activity, IUiListener listener) {
        params = new Bundle();
        ArrayList<String> temp = new ArrayList<>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).startsWith("http")) {
                    temp.add(Constants.APP_IP + list.get(i).replace("\"", "").replace("\\", ""));
                } else {
                    temp.add(list.get(i));
                }
            }
        }
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);// 标题
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);// 摘要
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, target_url);// 摘要
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, temp.size() > 0 ? temp.get(0) : SPUtils.getStringData(activity, "app_logo", "https://juduohui-s.oss-cn-shenzhen.aliyuncs.com/Upload/logo/logo.png"));
//        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);

        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, activity.getPackageName());
        mTencent.shareToQQ(activity, params, listener);

    }
    public static void shareToQQ(String title, String summary, String target_url, Activity activity, IUiListener listener) {
        params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);// 标题
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);// 摘要
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, target_url);// 摘要
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, activity.getPackageName());
        mTencent.shareToQQ(activity, params, listener);
    }

    public static void shareToQQZone(String title, String summary, String target_url, Activity activity, IUiListener listener) {
        params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);// 标题
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);// 摘要
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, target_url);// 摘要
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, activity.getPackageName());
        mTencent.shareToQQ(activity, params, listener);
    }
    /**
     * 分享本地图片到QQ
     *
     * @param imgUrl 图片url
     */
    public static void shareImgToQQ(String imgUrl, Activity activity, IUiListener listener) {
        params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);// 设置分享类型为纯图片分享
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imgUrl);// 需要分享的本地图片URL
        mTencent.shareToQQ(activity, params, listener);
    }

    /**
     * 分享本地图片到QQ空间
     *
     * @param imgUrl 图片url
     */
    public static void shareImgToQQZONE(String imgUrl, Activity activity, IUiListener listener) {
        params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);// 设置分享类型为纯图片分享
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imgUrl);// 需要分享的本地图片URL
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        mTencent.shareToQQ(activity, params, listener);
    }

    /**
     * 分享到QQ空间
     *
     * @param list       图片数组
     * @param title      分享的标题
     * @param content    分享的内容简介
     * @param target_url 点击跳转的目标见面
     * @param activity   当前ACTIVITY
     * @param listener   监听器
     */
    public static void shareToQZone(ArrayList<String> list, String title, String content, String target_url, Activity activity, IUiListener listener) {
        params = new Bundle();
        ArrayList<String> temp = new ArrayList<>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).startsWith("http")) {
                    temp.add(Constants.APP_IP + list.get(i).replace("\"", "").replace("\\", ""));
                } else {
                    temp.add(list.get(i));
                }
            }
        }
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);// 标题
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, content);// 摘要
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, target_url);// 摘要
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, temp);// 图片地址
        mTencent.shareToQzone(activity, params, listener);
    }
}