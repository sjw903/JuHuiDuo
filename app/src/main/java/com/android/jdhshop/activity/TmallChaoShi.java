package com.android.jdhshop.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.alibaba.baichuan.trade.common.utils.AlibcLogger;
import com.alipay.sdk.app.H5PayCallback;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.login.WelActivity;
import com.android.jdhshop.my.MyShareUrlActivity;
import com.android.jdhshop.my.RechargeActivity;
import com.android.jdhshop.utils.OkHttpUtils;
import com.github.anzewei.parallaxbacklayout.ParallaxHelper;
import com.king.zxing.Intents;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.alibaba.baichuan.trade.biz.applink.adapter.AlibcFailModeType.AlibcNativeFailModeJumpDOWNLOAD;

//import com.tencent.smtt.sdk.CookieSyncManager;

public class TmallChaoShi extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.wv)
    WebView wv;
    @BindView(R.id.tmall_buy)
    LinearLayout tmall_buy;
    @BindView(R.id.tm_tuize)
    TextView tm_guize;
    @BindView(R.id.tm_leftm)
    TextView tm_leftm;
    @BindView(R.id.tm_rightm)
    TextView tm_rightm;
    private AlibcLogin alibcLogin;
    private FrameLayout mFrameLayout;

    @Override
    protected void initUI() {
        setContentView(R.layout.ac_tmall_chaoshi);
        ButterKnife.bind(this);
        showLoadingDialog();
        alibcLogin = AlibcLogin.getInstance();
    }

    @Override
    protected void initData() {

        String token = SPUtils.getStringData(this,"token","");
        if ("".equals(token)){
            openActivity(WelActivity.class);
            T.showShort(this,"请先登陆"+ getString(R.string.app_name));
            finish();
        }
        else
        {
            whetherBindingTaobao(1);
        }

        tm_guize.setOnClickListener(v -> {
            Intent intent = new Intent(TmallChaoShi.this, NewsOnlyContentActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("article_id", "36");
            bundle.putString("title", "天猫超市规则");
            intent.putExtras(bundle);
            startActivity(intent);
        });

        CookieSyncManager.createInstance(this);
        CookieManager instance = CookieManager.getInstance();
        CookieManager.setAcceptFileSchemeCookies(true);
        instance.setAcceptCookie(true);
        instance.removeExpiredCookie();
        instance.removeSessionCookie();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            instance.setAcceptThirdPartyCookies(wv,true);
        }

//        WebStorage.getInstance().deleteAllData();
        ParallaxHelper.disableParallaxBack(this);
        mFrameLayout = findViewById(R.id.mFrameLayout);
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText(getIntent().getStringExtra("title"));
        wv.setWebChromeClient(new WebChromeClient());
        wv.setWebViewClient(webViewClient);
        WebSettings webSetting = wv.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setAllowContentAccess(true);
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
    private String goods_id="";
    private String click_url = "";
    private void WebViewPageChange(WebView view,String url){
        //
        Pattern pattern = Pattern.compile("tmall.com/item.htm.*&id=([0-9]*)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()){
            LogUtils.d(TAG, "WebViewPageChange: "+matcher.groupCount()+"___"+matcher.group(0)+"___"+matcher.group(1));

            tmall_buy.setVisibility(View.VISIBLE);
            RequestParams params = new RequestParams();
            params.put("num_iid",matcher.group(1));
            String token = SPUtils.getStringData(TmallChaoShi.this,"token","");
            LogUtils.d(TAG, "WebViewPageChange: "+ token);
            LogUtils.d(TAG, "WebViewPageChange: "+ matcher.group(1));
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    FormBody.Builder form_builder = new FormBody.Builder();
                    form_builder.add("token",token);
                    form_builder.add("num_iid",matcher.group(1));
                    goods_id = matcher.group(1);

                    OkHttpUtils.getInstance().post(Constants.HOME_TBK_GETGOODSMSG_URL, form_builder.build() , new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            LogUtils.d(TAG, "onFailure: "+e.getMessage());
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response res) throws IOException {
                            String response = res.body().string();
                            try{
                                LogUtils.d(TAG, "onSuccess: "+response);
                                com.alibaba.fastjson.JSONObject res_obj = com.alibaba.fastjson.JSONObject.parseObject(response);
                                if ("0".equals(res_obj.getString("code"))){
                                    com.alibaba.fastjson.JSONObject item = res_obj.getJSONObject("data");
                                    String commission = item.getString("commission");
                                    String buy_url = item.getString("coupon_click_url");
                                    click_url = buy_url;
                                    LogUtils.d(TAG, "获取完数据了: "+response);
                                    runOnUiThread(()->{
                                        tm_leftm.setText(String.format("买就返%s元", commission));
                                        tm_rightm.setOnClickListener(v->{
                                            goToBuy(buy_url);
                                        });
                                        tm_leftm.setOnClickListener(v->{
                                            goToBuy(buy_url);
                                        });
                                    });
                                }
                                else
                                {
                                    runOnUiThread(()->{
                                        Toast.makeText(TmallChaoShi.this, res_obj.getString("msg"), Toast.LENGTH_LONG).show();
                                    });
                                }
                            }
                            catch (Exception e){
                                LogUtils.d(TAG, "请求数据出错了: "+e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
            thread.start();

        }
        else
        {
            tmall_buy.setVisibility(View.INVISIBLE);
        }
    }
    private void TmallProductCalc(){

    }

    private void goToBuy(String coupon_click_url){
//        Map<String, String> exParams = new HashMap<>();
//        AlibcTrade.openByUrl(TmallChaoShi.this, "", coupon_click_url, null, new WebViewClient(), new WebChromeClient(), new AlibcShowParams(OpenType.Native)
//                , new AlibcTaokeParams(SPUtils.getStringData(getApplicationContext(), "tbk_pid", ""), "", ""), exParams, new AlibcTradeCallback() {
//                    @Override
//                    public void onTradeSuccess(AlibcTradeResult alibcTradeResult) {
//
//                        LogUtils.d(TAG, "onTradeSuccess: " + alibcTradeResult.resultType.name() + "_______" + alibcTradeResult.payResult);
//                    }
//
//                    @Override
//                    public void onFailure(int i, String s) {
//
//                    }
//                });

        AlibcBasePage page = new AlibcDetailPage(goods_id);
        AlibcShowParams showParams = new AlibcShowParams();
        showParams.setOpenType(OpenType.Auto);
        showParams.setClientType("taobao");
        showParams.setBackUrl("tbopen32019391://");
        showParams.setNativeOpenFailedMode(AlibcNativeFailModeJumpDOWNLOAD);

        AlibcTaokeParams taokeParams = new AlibcTaokeParams(SPUtils.getStringData(TmallChaoShi.this,"tbk_pid",""), "", "");
        taokeParams.setPid(SPUtils.getStringData(TmallChaoShi.this, "tbk_pid", ""));
        taokeParams.setAdzoneid(SPUtils.getStringData(TmallChaoShi.this, "tbk_aid", ""));
//adzoneid是需要taokeAppkey参数才可以转链成功&店铺页面需要卖家id（sellerId），具体设置方式如下：
//            taokeParams.extraParams.put("taokeAppkey","31907828");
//            taokeParams.extraParams.put("sellerId", "31907828");
//自定义参数
        Map<String, String> trackParams = new HashMap<>();
        AlibcTrade.openByBizCode(TmallChaoShi.this, page, null, new WebViewClient(),
                new WebChromeClient(), "detail", showParams, taokeParams,
                trackParams, new AlibcTradeCallback() {
                    @Override
                    public void onTradeSuccess(AlibcTradeResult tradeResult) {
                        // 交易成功回调（其他情形不回调）
                        AlibcLogger.i(TAG, "open detail page success");
                        return;
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        // 失败回调信息
                        AlibcLogger.e(TAG, "code=" + code + ", msg=" + msg);
                        if (code == -1) {
                            Toast.makeText(TmallChaoShi.this, "唤端失败，失败模式为none",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
            startActivityForResult(new Intent(TmallChaoShi.this, MyScanActivity.class), 5);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode ==RESULT_OK && requestCode == 5) {
            String temp = data.getStringExtra(Intents.Scan.RESULT);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject();
                jsonObject.put("result",temp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            wv.loadUrl("javascript:showCodeResult(" + jsonObject.toString() +")");
        }
        // 从登陆页面返回
        if (resultCode == 999){
            LogUtils.d(TAG, "onActivityResult: 从登陆页面返回");
            commonGetUserMsg();
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
            LogUtils.d(TAG, "onPageStarted: "+url);
            runOnUiThread(()->{
                WebViewPageChange(view,url);
            });
            if (!url.contains("oauth.taobao") && !url.contains("oauth.m.taobao")) {
//                showLoadingDialog();
            }
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String newurl) {
            if (!newurl.startsWith("http")){
                LogUtils.d(TAG, "请求非http，已返回true,请求的url = "+newurl);
                return true;
            }
            if (newurl.startsWith("dmooo://toShare")) {
                openActivity(MyShareUrlActivity.class);
                return true;
            }
            if (newurl.startsWith("dmooo://toLogin")) {
                openActivity(WelActivity.class);
                return true;
            }
            if (newurl.startsWith("dmooo://noActivity")) {
                ToastUtils.showShortToast(TmallChaoShi.this, "目前没有拉新活动");
                finish();
                return true;
            }
            if (newurl.contains("/error?code=")) {
                SPUtils.saveStringData(TmallChaoShi.this, "coded", getValueByName(newurl, "code"));
                finish();
            }
            if (newurl.contains("https://mclient.alipay.com")) {
                final PayTask task = new PayTask(TmallChaoShi.this);
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
                if("".equals(SPUtils.getStringData(TmallChaoShi.this,"token",""))){
                    T.showShort(TmallChaoShi.this, "请先登录");
                    openActivity(WelActivity.class);
                    finish();
                    return true;
                }
                if ("1".equals(CaiNiaoApplication.getUserInfoBean().user_msg.group_id) || "2".equals(CaiNiaoApplication.getUserInfoBean().user_msg.group_id)) {
                    //解绑操作
                    AlertDialog.Builder builder = new AlertDialog.Builder(TmallChaoShi.this);
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
//                    try {
//                        // 以下固定写法
//                        Intent intent;
//                        if (newurl.startsWith("pinduoduo")) {
//                            String pddUrl = "pddopen://?appKey=7063037941b2435f9a47109da4c33cd8&packageId=com.android.jdhshop&backUrl=hkxuri%3A%2F%2Fhkxtb%3A1024%2Ftarget&h5Url="+ Uri.encode(getIntent().getStringExtra("url"));
//                            LogUtils.d("JumpUrl", pddUrl);
//                            intent = new Intent(Intent.ACTION_VIEW,
//                                    Uri.parse(pddUrl));
//                        }
//                        else
//                        {
//                            intent = new Intent(Intent.ACTION_VIEW,
//                                    Uri.parse(newurl));
//                        }
//
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
                    return true;
                }
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
//                    view.loadUrl("javascript:window.java_obj.getSource('<head>'+" +
//                            "document.getElementsByClassName('moneybox_com_input')[0].value+'</head>');");
                    if (view!=null) {
                        view.evaluateJavascript("if (document.querySelector('.nZ5uAp1XCP-BKExYE5MQG')!=null){document.querySelector('.nZ5uAp1XCP-BKExYE5MQG').style='display:none'; document.querySelector('.baxia-dialog').style='display:none'; }", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String s) {

                            }
                        });
                    }
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

    @OnClick({R.id.tv_left})
    public void onViewClicked() {
        finish();

    }


    private void whetherBindingTaobao(final int type) {
        showLoadingDialog();
        String url = Constants.WHETHER_BINDING_TAOBAO;
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder().add("token", SPUtils.getStringData(this, "token", "")).build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                closeLoadingDialog();
                String result = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    LogUtils.d(TAG, "onResponse: " + code);
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    String isBinding = jsonData.optString("is_binding");
                    if ("N".equals(isBinding)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showTipDialog2("领券提示", Html.fromHtml("领券需要您绑定淘宝号，一个" + getString(R.string.app_name) + "账号只可绑定一个淘宝账号，通过绑定的淘宝账号购物可得到返利，其他淘宝账号无法获得返利"), new onClickListener() {
                                    @Override
                                    public void onClickSure() {

                                        //如果未绑定
                                        alibcLogin.showLogin(new AlibcLoginCallback() {
                                            @Override
                                            public void onSuccess(int i, String s, String s1) {
                                                s = alibcLogin.getSession().userid;
                                                if (s == null) {
                                                    return;
                                                }
                                                int length = s.length();
                                                if (length > 6) {
                                                    String b = s.substring(length - 6, length);
                                                    String[] bArr = b.split("");
                                                    String c = bArr[1] + "" + bArr[2] + "" + bArr[5] + "" + bArr[6] + "" + bArr[3] + "" + bArr[4];
                                                    bindingTaobao(c);
//                                    int i=0;
                                                }
                                            }

                                            @Override
                                            public void onFailure(int code, String msg) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(TmallChaoShi.this, msg,
                                                                Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }, "去绑定");
                            }
                        });
                    } else {
                        lqhandle.sendEmptyMessage(type);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    //绑定淘宝账号
    private void bindingTaobao(String tb_uid) {
        String url = Constants.BINDING_TAOBAO;
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder().add("token", SPUtils.getStringData(this, "token", "")).add("tb_uid", tb_uid).build();
        Request request = new Request.Builder().url(url).post(body).build();
        LogUtils.d(TAG, "bindingTaobao: " +
                SPUtils.getStringData(this, "tbk_pid", "") + "pid" +
                SPUtils.getStringData(this, "tbk_rid", "") + "rid" +
                SPUtils.getStringData(this, "tbk_aid", "") + "aid");

        LogUtils.d(TAG, "bindingTaobao: token=" + SPUtils.getStringData(this, "token", "") + "&tb_uid=" + tb_uid);
        LogUtils.d(TAG, "bindingTaobao: " + url);
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String result = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if ("0".equals(code)) {
                        LogUtils.d(TAG, "onResponse: 绑定淘宝ID成功");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LogUtils.d(TAG, "run: 跳tipBind");
                                //tipBind();
                                Toast.makeText(TmallChaoShi.this, "绑定淘宝账号成功",
                                        Toast.LENGTH_LONG).show();
                                lqhandle.sendEmptyMessage(1);
                            }
                        });
                    } else {
                        Message send_message = new Message();
                        send_message.what = 3;
                        send_message.obj = jsonObject.getString("msg");
                        lqhandle.sendMessage(send_message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private Handler lqhandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 3) {
                showTipDialog3("淘宝账号重复绑定", (String) msg.obj, null, null, "知道了", null);
//                Toast.makeText(PromotionDetailsActivity.this, (String) msg.obj,
//                        Toast.LENGTH_LONG).show();
                return;
            }


            AlibcBasePage page = new AlibcDetailPage(goods_id);
            AlibcShowParams showParams = new AlibcShowParams();
            showParams.setOpenType(OpenType.Auto);
            showParams.setClientType("taobao");
            showParams.setBackUrl("tbopen32019391://");
            showParams.setNativeOpenFailedMode(AlibcNativeFailModeJumpDOWNLOAD);

            AlibcTaokeParams taokeParams = new AlibcTaokeParams(SPUtils.getStringData(TmallChaoShi.this,"tbk_pid",""), "", "");
            taokeParams.setPid(SPUtils.getStringData(TmallChaoShi.this, "tbk_pid", ""));
            taokeParams.setAdzoneid(SPUtils.getStringData(TmallChaoShi.this, "tbk_aid", ""));
//adzoneid是需要taokeAppkey参数才可以转链成功&店铺页面需要卖家id（sellerId），具体设置方式如下：
//            taokeParams.extraParams.put("taokeAppkey","31907828");
//            taokeParams.extraParams.put("sellerId", "31907828");
//自定义参数
            Map<String, String> trackParams = new HashMap<>();
            AlibcTrade.openByBizCode(TmallChaoShi.this, page, null, new WebViewClient(),
                    new WebChromeClient(), "detail", showParams, taokeParams,
                    trackParams, new AlibcTradeCallback() {
                        @Override
                        public void onTradeSuccess(AlibcTradeResult tradeResult) {
                            // 交易成功回调（其他情形不回调）
                            AlibcLogger.i(TAG, "open detail page success");
                            return;
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            // 失败回调信息
                            AlibcLogger.e(TAG, "code=" + code + ", msg=" + msg);
                            if (code == -1) {
                                Toast.makeText(TmallChaoShi.this, "唤端失败，失败模式为none",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            LogUtils.d(TAG, "handleMessage: 这里跳淘宝领券么？");
            Map<String, String> exParams = new HashMap<>();
            AlibcTrade.openByUrl(TmallChaoShi.this, "", click_url , null, new WebViewClient(), new WebChromeClient(), new AlibcShowParams(OpenType.Native)
                    , new AlibcTaokeParams(SPUtils.getStringData(getApplicationContext(), "tbk_pid", ""), "", ""), exParams, new AlibcTradeCallback() {
                        @Override
                        public void onTradeSuccess(AlibcTradeResult alibcTradeResult) {

                            LogUtils.d(TAG, "onTradeSuccess: " + alibcTradeResult.resultType.name() + "_______" + alibcTradeResult.payResult);
                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });
        }
    };
}
