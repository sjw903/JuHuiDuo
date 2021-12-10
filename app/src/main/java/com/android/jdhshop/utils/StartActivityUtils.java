package com.android.jdhshop.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

/**
 * Activity跳转动画
 */
public class StartActivityUtils {
    public static class ActivityStartParam {
        public Activity mActivity;
        public View mSourceView;
        public float mPhotoCoorX;
        public float mPhotoCoorY;
        public int mViewWidth;
        public int mViewHeight;
        public Bundle mExtraParams;
        public Class<?> mTargetActivityCls;

        public ActivityStartParam setPhotoCoorX(float photoCoorX) {
            mPhotoCoorX = photoCoorX;
            return this;
        }

        public ActivityStartParam setPhotoCoorY(float photoCoorY) {
            mPhotoCoorY = photoCoorY;
            return this;
        }

        public ActivityStartParam setViewWidth(int width) {
            mViewWidth = width;
            return this;
        }

        public ActivityStartParam setViewHeight(int height) {
            mViewHeight = height;
            return this;
        }
    }

    public static void startActivityForResult(int requestCode,
                                              @NonNull ActivityStartParam detailParam) {
        Intent intent = new Intent(detailParam.mActivity, detailParam.mTargetActivityCls);
        if (detailParam.mExtraParams != null) {
            intent.putExtras(detailParam.mExtraParams);
        }
        if (detailParam.mSourceView != null) {
            ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(detailParam.mActivity,
                            detailParam.mSourceView, "ksad_content_base_layout");

            startActivityForResultWithActivityCompat(detailParam.mActivity, intent, requestCode,
                    options.toBundle());
        } else {
            startActivityForResult(detailParam.mActivity, intent, requestCode);
        }
    }


    private static ActivityOptionsCompat createActivityOptionCompat(Context context,
                                                                    View clickedView) {
        float initialScale = clickedView.getWidth() * 1f / UIUtils.getScreenMeasuredWidth(context);
        int initialOperationBarHeight =
                (int) (initialScale * UIUtils.dp2px(50));
        ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(
                clickedView, 0, -initialOperationBarHeight, clickedView.getWidth(),
                clickedView.getHeight() + initialOperationBarHeight);
        return options;
    }


    private static void startActivityForResultWithActivityCompat(Activity activity, Intent intent,
                                                                 int requestCode, @Nullable Bundle options) {
        try {
            ActivityCompat.startActivityForResult(activity, intent, requestCode, options);
        } catch (Throwable e) {
            startActivityForResult(activity, intent, requestCode);
        }
    }

    private static void startActivityForResult(Activity activity, Intent intent, int requestCode) {
        try {
            activity.startActivityForResult(intent, requestCode);
            // Config animation in theme may not work on some devices (e.g. smartisan t1)
            // activity.overridePendingTransition(
            // intent.getIntExtra(START_ENTER_PAGE_ANIMATION, R.anim.slide_in_from_right),
            // intent.getIntExtra(START_EXIT_PAGE_ANIMATION, R.anim.fade_out));
        } catch (ActivityNotFoundException e) {
            // ToastUtil.info(R.string.activity_not_found_error);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
