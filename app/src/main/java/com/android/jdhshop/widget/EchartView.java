package com.android.jdhshop.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.alibaba.fastjson.JSONArray;
import com.android.jdhshop.common.LogUtils;

import static android.webkit.WebSettings.LOAD_NO_CACHE;

public class EchartView extends WebView {
    private static final String TAG = EchartView.class.getSimpleName();

    public EchartView(Context context) {
        this(context, null);
    }

    public EchartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EchartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("JavascriptInterface")
    private void init() {
        super.setOverScrollMode(OVER_SCROLL_NEVER);
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setCacheMode(LOAD_NO_CACHE);
        webSettings.setAppCacheEnabled(false);
        addJavascriptInterface(new eChartJavascript(),"juhuohui");
        loadUrl("file:///android_asset/echarts.html");
    }


    static class eChartJavascript{
        @JavascriptInterface
        public void loadComplete(String timers){
            LogUtils.d(TAG, "loadComplete: "+ timers);
        }
        @JavascriptInterface
        public void loadFinished(String timers){
            LogUtils.d(TAG, "loadFinished: "+ timers);
        }
        @JavascriptInterface
        public String getConfig(){
            return "";
        }
    }



    public void refreshEchartsWithData(JSONArray x, JSONArray y){
        String call = "javascript:loadEchartsData("+x.toJSONString()+","+y.toJSONString()+")";
        loadUrl(call);
    }

}