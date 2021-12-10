package com.android.jdhshop.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.github.anzewei.parallaxbacklayout.ParallaxHelper;
import com.android.jdhshop.config.Constants;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.jdhshop.config.Constants.APP_IP;

public class WebViewActivity4 extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.wv)
    WebView wv;
    private FrameLayout mFrameLayout;

    @Override
    protected void initUI() {
        setContentView(R.layout.ac_webview);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        ParallaxHelper.disableParallaxBack(this);
        mFrameLayout =findViewById(R.id.mFrameLayout);
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("渠道授权");
        // 以显示传入url的方式打开页面（第二个参数是套件名称）
        Map<String, String> exParams = new HashMap<>();
        // 这里的 client_id 应该是阿里百川的APIID

        AlibcTrade.openByUrl(this, "", "https://oauth.taobao.com/authorize?response_type=code&client_id=" + SPUtils.getStringData(this,"tbk_appkey","32671209") +  "&redirect_uri="+ APP_IP+"/wap/Taobao/call_back&state=1212&view=tmall", wv,
                webViewClient, new WebChromeClient(){
                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        super.onProgressChanged(view, newProgress);
                        LogUtils.d(TAG, "onProgressChanged: 这里这里"+view.getUrl()+"____"+view.getOriginalUrl());
                    }
                }, new AlibcShowParams(OpenType.Native),
                // "mm_214700047_428250273_108430350403"
                new AlibcTaokeParams(SPUtils.getStringData(this,"tbk_rid",""), "", ""), exParams, new AlibcTradeCallback() {
                    @Override
                    public void onTradeSuccess(AlibcTradeResult tradeResult) {
//                        Log.e(TAG, tradeResult.payResult.);

                    }
                    @Override
                    public void onFailure(int code, String msg) {
                        Log.e(TAG, "code=" + code + ", msg=" + msg);
                    }
                });
    }

    /***
     * 获取url 指定name的value;
     * @param url
     * @param name
     * @return
     */
    private String getValueByName(String url, String name) {
        String result = "";
        int index = url.indexOf("?");
        String temp = url.substring(index + 1);
        String[] keyValue = temp.split("&");
        for (String str : keyValue) {
            if (str.contains(name)) {
                result = str.replace(name + "=", "");
                break;
            }
        }
        return result;
    }
    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        switch (config.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                break;
        }
    }
    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
            if (!url.contains("oauth.taobao") && !url.contains("oauth.m.taobao"))
                closeLoadingDialog();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
            if (!url.contains("oauth.taobao") && !url.contains("oauth.m.taobao"))
                showLoadingDialog();
            wv.loadUrl("javascript:(function(){" +
                    "var objs = document.getElementsByTagName('img'); " +
                    "for(var i=0;i<objs.length;i++)  " +
                    "{"
                    + "var img = objs[i];   " +
                    "    img.style.maxWidth = '100%'; img.style.height = 'auto';  " +
                    "}" +
                    "})()");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String newurl) {
            if (newurl.contains("/error?code=")) {
                SPUtils.saveStringData(WebViewActivity4.this, "coded", getValueByName(newurl, "code"));
                finish();
            }
            try {
                //处理intent协议
                if (newurl.startsWith("intent://")) {
                    Intent intent;
                    try {
                        intent = Intent.parseUri(newurl, Intent.URI_INTENT_SCHEME);
                        intent.addCategory("android.intent.category.BROWSABLE");
                        intent.setComponent(null);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                            intent.setSelector(null);
                        }
                        List<ResolveInfo> resolves = getPackageManager().queryIntentActivities(intent, 0);
                        if (resolves.size() > 0) {
                            startActivityIfNeeded(intent, -1);
                        }
                        return true;
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                // 处理自定义scheme协议
                if (!newurl.startsWith("http")) {
                    try {
                        // 以下固定写法
                        final Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(newurl));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        // 防止没有安装的情况
                        e.printStackTrace();
                        Uri uri = Uri.parse(getIntent().getStringExtra("url"));
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        finish();
                    }
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return super.shouldOverrideUrlLoading(view, newurl);
        }
    };

    private class MyWebChromeClient extends WebChromeClient {
        private View mCustomView;
        private CustomViewCallback mCustomViewCallback;

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mCustomView = view;
            mFrameLayout.addView(mCustomView);
            mCustomViewCallback = callback;
            wv.setVisibility(View.GONE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        public void onHideCustomView() {
            wv.setVisibility(View.VISIBLE);
            if (mCustomView == null) {
                return;
            }
            mCustomView.setVisibility(View.GONE);
            mFrameLayout.removeView(mCustomView);
            mCustomViewCallback.onCustomViewHidden();
            mCustomView = null;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            super.onHideCustomView();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        wv.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        wv.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wv.destroy();
    }
    @Override
    public void onBackPressed() {
        if (wv.canGoBack()) {
            wv.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void initListener() {

    }

    @OnClick(R.id.tv_left)
    public void onViewClicked() {
        finish();
    }
}
