package com.android.jdhshop.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.View;

import com.android.jdhshop.CaiNiaoApplication;

/**
 * 开发者：陈飞
 * 时间:2018/7/26 17:01
 * 说明：
 */
public class UIUtils {
    public static Context getContext() {
        return CaiNiaoApplication.getAppContext();
    }

    /**
     * 获取资源对象
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获取资源文件字符串
     *
     * @param id
     * @return
     */
    public static String getString(int id) {
        return getResources().getString(id);
    }

    /**
     * 获取资源文件字符串数组
     *
     * @param id
     * @return
     */
    public static String[] getStringArray(int id) {
        return getResources().getStringArray(id);
    }

    /**
     * 获取资源文件图片
     *
     * @param id
     * @return
     */
    public static Drawable getDrawable(int id) {
        return ContextCompat.getDrawable(getContext(), id);
    }

    /**
     * 获取资源文件颜色
     *
     * @param id
     * @return
     */
    public static int getColor(int id) {
        return ContextCompat.getColor(getContext(), id);
    }

    /**
     * 根据id获取颜色的状态选择器
     *
     * @param id
     * @return
     */
    public static ColorStateList getColorStateList(int id) {
        return ContextCompat.getColorStateList(getContext(), id);
    }

    /**
     * 获取尺寸
     *
     * @param id
     * @return
     */
    public static int getDimen(int id) {
        return getResources().getDimensionPixelSize(id); // 返回具体像素值
    }

    /**
     * dp -> px
     *
     * @param dp
     * @return
     */
    public static int dp2px(float dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    /**
     * px -> dp
     *
     * @param px
     * @return
     */
    public static float px2dp(int px) {
        float density = getResources().getDisplayMetrics().density;
        return px / density;
    }

    /**
     * 加载布局文件
     *
     * @param id
     * @return
     */
    public static View inflate(int id) {
        return View.inflate(getContext(), id, null);
    }


    /**
     * findViewById的泛型封装,减少强转代码
     *
     * @param layout
     * @param id
     * @param <T>
     * @return
     */
    public static <T extends View> T findViewById(View layout, int id) {
        return (T) layout.findViewById(id);
    }

    /**
     * 获取屏幕的比例
     *
     * @param context
     * @return
     */
    public static float getScaledDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float value = dm.scaledDensity;
        return value;
    }

    /**
     * 获取控件的高度,如果获取的高度为0,则重新计算尺寸后再返回高度
     *
     * @param view
     * @return
     */
    public static int getViewMeasuredHeight(View view) {
        calcViewMeasure(view);
        return view.getMeasuredHeight();
    }

    /**
     * 获取控件的高度,如果获取的高度为0,则重新计算尺寸后再返回高度
     *
     * @param context
     * @return
     */
    public static int getScreenMeasuredHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    /**
     * 获取控件的高度,如果获取的高度为0,则重新计算尺寸后再返回高度
     *
     * @param context
     * @return
     */
    public static int getScreenMeasuredWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    /**
     * 获取控件的宽度,如果获取的宽度为0,则重新计算尺寸后再返回宽度
     *
     * @param view
     * @return
     */
    public static int getViewMeasuredWidth(View view) {
        calcViewMeasure(view);
        return view.getMeasuredWidth();
    }

    /**
     * 测量控件的尺寸
     *
     * @param view
     */
    public static void calcViewMeasure(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int expandSpec = View.MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
    }

    /**
     * 设置textview指定文字为某一颜色
     *
     * @param content 显示的文字
     * @param color   需要转换成的颜色值
     * @param start   需要变色文字开始位置
     * @param end     需要变色文字结束位置
     */
    public static SpannableStringBuilder changeTextColor(String content, int color, int start, int end) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }
}
