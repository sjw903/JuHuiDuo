package com.android.jdhshop.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alipay.sdk.app.H5PayCallback;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.PromotionDetailsBean;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.login.LoginActivity;
import com.android.jdhshop.my.MyShareUrlActivity;
import com.android.jdhshop.utils.StringUtils;
import com.android.jdhshop.utils.VerticalImageSpan;
import com.android.jdhshop.utils.ZxingUtils;

import java.net.URISyntaxException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.analytics.android.api.CountEvent;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cz.msebera.android.httpclient.Header;

public class WebViewActivity3 extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.fragment)
    FrameLayout mFrameLayout;
    @BindView(R.id.wv)
    WebView wv;

    @Override
    protected void initUI() {
        setContentView(R.layout.ac_webview);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText(getIntent().getStringExtra("title"));
        wv.setWebViewClient(webViewClient);
        wv.setWebChromeClient(new MyWebChromeClient());
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js
        //支持屏幕缩放
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportMultipleWindows(false);//这里一定得是false,不然打开的网页中，不能在点击打开了
        // webSetting.setLoadWithOverviewMode(true);
        webSettings.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setAppCacheMaxSize(Long.MAX_VALUE);
        webSettings.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSettings.setDatabasePath(this.getDir("databases", 0).getPath());
        webSettings.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSettings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        wv.loadUrl(getIntent().getStringExtra("url"));
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
    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
            if(!url.contains("oauth.taobao")&&!url.contains("oauth.m.taobao")&&!url.contains("trip"))
                closeLoadingDialog();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
            if(!url.contains("oauth.taobao")&&!url.contains("oauth.m.taobao")&&!url.contains("trip"))
                showLoadingDialog();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String newurl) {
            if (newurl.contains("https://mclient.alipay.com")) {
                final PayTask task = new PayTask(WebViewActivity3.this);
                boolean isIntercepted = task.payInterceptorWithUrl(newurl, true, new H5PayCallback() {
                    @Override
                    public void onPayResult(final H5PayResultModel result) {
                        // 支付结果返回
                        final String url = result.getReturnUrl();
//                        if (!TextUtils.isEmpty(url)) {
//                            H5PayDemoActivity.this.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    view.loadUrl(url);
//                                }
//                            });
//                        }
                    }
                });
                view.goBack();
                return true;
            }
            if(newurl.contains("detail.m.tmall")||newurl.contains("detail.tmall.hk")){
                Uri uri = Uri.parse(newurl);
                String numId=uri.getQueryParameter("id");
                getGoodsMsgRequest(numId);
                return true;
            }
            if(newurl.contains("trip/travel-detail")){
                Uri uri = Uri.parse(newurl);
                String numId=uri.getQueryParameter("id");
                getGoodsMsgRequest(numId);
                return true;
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
                        view.goBack();
                        if(newurl.contains("pinduoduo")){
                            finish();
                        }
                    } catch (Exception e) {
                        // 防止没有安装的情况
                        e.printStackTrace();
                        Uri uri = Uri.parse(getIntent().getStringExtra("url"));
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        view.goBack();
                        if(newurl.contains("pinduoduo")){
                            finish();
                        }
                    }
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return super.shouldOverrideUrlLoading(view, newurl);
        }
    };

    protected void getGoodsMsgRequest(final String num_iid) {
        String url = "";
        RequestParams requestParams = new RequestParams();
            url = Constants.HOME_TBK_GETGOODSMSG_URL;
            requestParams.put("num_iid", num_iid);
        HttpUtils.post(url,WebViewActivity3.this, requestParams, new onOKJsonHttpResponseHandler<PromotionDetailsBean>(new TypeToken<Response<PromotionDetailsBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Response<PromotionDetailsBean> datas) {
                if (getComeActivity().isDestroyed())
                    return;
                if (datas.isSuccess()) {
                    if(Double.valueOf(datas.getData().getCommission())<0){
                        showToast("该商品无佣金");
                    }else{
                        Bundle bundle = new Bundle();
                        bundle.putString("num_iid", num_iid);
                        openActivity(PromotionDetailsActivity.class, bundle);
                    }
                }else{
                    showToast("暂无优惠券或者佣金");
                }
            }
        });
    }
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (wv.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {//点击返回按钮的时候判断有没有上一页
            wv.goBack(); // goBack()表示返回webView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void initListener() {

    }

    @OnClick(R.id.tv_left)
    public void onViewClicked() {
        finish();
    }
}
