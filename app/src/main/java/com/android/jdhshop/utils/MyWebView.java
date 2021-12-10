package com.android.jdhshop.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class MyWebView extends WebView {
    public MyWebView(Context context) {
        super(context);
    }
    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
    }
    //重写onScrollChanged 方法
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        scrollTo(0,t);
    }
}