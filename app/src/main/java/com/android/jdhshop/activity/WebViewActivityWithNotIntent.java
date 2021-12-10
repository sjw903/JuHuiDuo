package com.android.jdhshop.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.alipay.sdk.app.H5PayCallback;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.login.WelActivity;
import com.android.jdhshop.my.MyShareUrlActivity;
import com.android.jdhshop.my.RechargeActivity;
import com.github.anzewei.parallaxbacklayout.ParallaxHelper;
import com.king.zxing.Intents;
import com.lljjcoder.style.citylist.Toast.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import com.tencent.smtt.sdk.CookieSyncManager;

/**
 *  不跳系统的浏览器
 */
public class WebViewActivityWithNotIntent extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.wv)
    WebView wv;
    private FrameLayout mFrameLayout;

    @Override
    protected void initUI() {
        setContentView(R.layout.ac_webview2);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        CookieSyncManager.createInstance(this);
        CookieManager instance = CookieManager.getInstance();
        CookieManager.setAcceptFileSchemeCookies(true);
        instance.setAcceptCookie(true);
        instance.removeExpiredCookie();
        instance.removeSessionCookie();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            instance.setAcceptThirdPartyCookies(wv,true);
        }
        //CookieManager.getInstance().removeAllCookie();
//        WebStorage.getInstance().deleteAllData();
        ParallaxHelper.disableParallaxBack(this);
        mFrameLayout = findViewById(R.id.mFrameLayout);
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText(getIntent().getStringExtra("title"));
        wv.setWebChromeClient(new MyWebChromeClient());
        wv.setWebViewClient(webViewClient);
        WebSettings webSetting = wv.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);//这里一定得是false,不然打开的网页中，不能在点击打开了
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        wv.addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");
        wv.addJavascriptInterface(new InJavaScriptLocalObj1(), "dadaInfo");
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        webSetting.setBlockNetworkImage(false); // 解决图片不显示
        webSetting.setUserAgentString(webSetting.getUserAgentString()+" "+(getIntent().getStringExtra("ua")==null?"":getIntent().getStringExtra("ua")));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting
                    .setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
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
    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void getSource(String html) {
            LogUtils.d("html=", html);
        }
    }
    class InJavaScriptLocalObj1 {
        @JavascriptInterface
        public void getScancode(String msg) {
            startActivityForResult(new Intent(WebViewActivityWithNotIntent.this, MyScanActivity.class), 5);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode ==RESULT_OK && requestCode == 5) {
            String temp = data.getStringExtra(Intents.Scan.RESULT);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject();
                jsonObject.put("result",temp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            wv.loadUrl("javascript:showCodeResult(" + jsonObject.toString() + ")");
        }
    }
    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
            if (!url.contains("oauth.taobao") && !url.contains("oauth.m.taobao")) {
                closeLoadingDialog();
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
            if (!url.contains("oauth.taobao") && !url.contains("oauth.m.taobao")) {
//                showLoadingDialog();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String newurl = request.getUrl().toString();
            LogUtils.d(TAG, "加载网址【新】: "+newurl);
            if (newurl.contains("openapp.jdmobile") || newurl.contains("intent://m.taobao") || newurl.contains("tbopen://")){
                //view.loadUrl(newurl);
                LogUtils.d(TAG, "shouldOverrideUrlLoading: 走到这里了么？");
                return true;
            }
            else
            {
                return super.shouldOverrideUrlLoading(view, request);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String newurl) {
            LogUtils.d(TAG, "加载网址【老】: "+newurl);
            if (newurl.startsWith("dmooo://toShare")) {
                openActivity(MyShareUrlActivity.class);
                return true;
            }
            if (newurl.startsWith("dmooo://toLogin")) {
                openActivity(WelActivity.class);
                return true;
            }
            if (newurl.startsWith("dmooo://noActivity")) {
                ToastUtils.showShortToast(WebViewActivityWithNotIntent.this, "目前没有拉新活动");
                finish();
                return true;
            }
            if (newurl.contains("/error?code=")) {
                SPUtils.saveStringData(WebViewActivityWithNotIntent.this, "coded", getValueByName(newurl, "code"));
                finish();
            }
            if (newurl.contains("openapp.jdmobile") || newurl.contains("intent://m.taobao")){
                //view.loadUrl(newurl);
                LogUtils.d(TAG, "shouldOverrideUrlLoading: 走到这里了么？");
                return true;
            }
            if (newurl.contains("tbopen://") || newurl.contains("intent://m.taobao")){
                //view.loadUrl(newurl);
                LogUtils.d(TAG, "shouldOverrideUrlLoading: 走到这里了么？");
                return true;
            }
            if (newurl.contains("https://mclient.alipay.com")) {
                final PayTask task = new PayTask(WebViewActivityWithNotIntent.this);
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
                return true;
            }
            if (newurl.startsWith("https://wx.tenpay") || newurl.startsWith("https://mclient.alipay")) {
                if("".equals(SPUtils.getStringData(WebViewActivityWithNotIntent.this,"token",""))){
                    T.showShort(WebViewActivityWithNotIntent.this, "请先登录");
                    openActivity(WelActivity.class);
                    finish();
                    return true;
                }
                if ("1".equals(CaiNiaoApplication.getUserInfoBean().user_msg.group_id) || "2".equals(CaiNiaoApplication.getUserInfoBean().user_msg.group_id)) {
                    //解绑操作
                    AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivityWithNotIntent.this);
                    builder.setTitle(getString(R.string.app_name));
                    builder.setMessage("升级vip后享受加油优惠");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            wv.reload();
                        }
                    });
                    builder.setPositiveButton("去升级", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            openActivity(RechargeActivity.class);
                            finish();
                        }
                    });
                    builder.setCancelable(false).show();
                    return true;
                }
            }
            try {
                //处理intent协议
                if (newurl.startsWith("intent://")) {
                    return true;
//                    Intent intent;
//                    try {
//                        intent = Intent.parseUri(newurl, Intent.URI_INTENT_SCHEME);
//                        intent.addCategory("android.intent.category.BROWSABLE");
//                        intent.setComponent(null);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
//                            intent.setSelector(null);
//                        }
//                        List<ResolveInfo> resolves = getPackageManager().queryIntentActivities(intent, 0);
//                        if (resolves.size() > 0) {
//                            startActivityIfNeeded(intent, -1);
//                        }
//                        return true;
//                    } catch (URISyntaxException e) {
//                        e.printStackTrace();
//                    }
                }
                // 处理自定义scheme协议
                if (!newurl.startsWith("http")) {
                    return true;
                }
//                    try {
//                        // 以下固定写法
//                        final Intent intent = new Intent(Intent.ACTION_VIEW,
//                                Uri.parse(newurl));
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        startActivity(intent);
//                        view.goBack();
//
//                        if(newurl.contains("pinduoduo")||newurl.contains("vipshop")){
//
//                            finish();
//                        }
//                    } catch (Exception e) {
//                        // 防止没有安装的情况
//                        e.printStackTrace();
//                        Uri uri = Uri.parse(getIntent().getStringExtra("url"));
//                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                        startActivity(intent);
//                        view.goBack();
//                        if(newurl.contains("pinduoduo")||newurl.contains("vipshop")){
//                            finish();
//                        }
//                    }
//                    return true;
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return super.shouldOverrideUrlLoading(view, newurl);
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.loadUrl("javascript:window.java_obj.getSource('<head>'+" +
                            "document.getElementsByClassName('moneybox_com_input')[0].value+'</head>');");
                }
            },500);
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

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
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
