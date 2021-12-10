package com.android.jdhshop.activity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.utils.WxUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

import static com.android.jdhshop.config.Constants.APP_IP;

public class WebViewActivity2 extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.wv)
    WebView wv;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    private String tkl = "";

    @Override
    protected void initUI() {
        setContentView(R.layout.ac_webview);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        llBottom.setVisibility(View.VISIBLE);
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("超级红包主会场");
        wv.setWebViewClient(webViewClient);
        wv.setWebChromeClient(webChromeClient);
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js
        //支持屏幕缩放
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        wv.loadUrl(APP_IP+"/wap/Index/share2/uid/" + SPUtils.getStringData(this, Constants.UID, "0"));
        getInfo();
    }

    private void getInfo() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.NIANHUOJIE,WebViewActivity2.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (getComeActivity().isDestroyed())
                    return;
                try {
                    if (new JSONObject(responseString).getInt("code") != 0) {
                        T.showShort(WebViewActivity2.this, new JSONObject(responseString).getString("msg"));
                        return;
                    }
                    JSONObject object = new JSONObject(responseString).getJSONObject("data");
                    tkl = object.getString("tkl");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
            closeLoadingDialog();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
            showLoadingDialog();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String newurl) {
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

    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private WebChromeClient webChromeClient = new WebChromeClient() {
        //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(webView.getContext());
            localBuilder.setMessage(message).setPositiveButton("确定", null);
            localBuilder.setCancelable(false);
            localBuilder.create().show();

            //注意:
            //必须要这一句代码:result.confirm()表示:
            //处理结果为确定状态同时唤醒WebCore线程
            //否则不能继续点击按钮
            result.confirm();
            return true;
        }

        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        //加载进度回调
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
        }
    };

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

    @OnClick({R.id.tv_left, R.id.btn_copy, R.id.btn_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.btn_copy:
                String tks="年货节派新年红包啦！！！最高888元福气红包，帮你清空购物车！每天可领取三次喔！长按复制口令"+tkl+" ，打开淘宝即可领取红包！";
                WXTextObject msg1 = new WXTextObject();  //这个对象是用来包裹发送信息的对象
                msg1.text=tks;
                WXMediaMessage msgtemp = new WXMediaMessage();
                msgtemp.mediaObject = msg1;
                msgtemp.description = tks;
                SendMessageToWX.Req req1 = new SendMessageToWX.Req();    //创建一个请求对象
                req1.message = msgtemp;  //把msg放入请求对象中
                req1.scene = SendMessageToWX.Req.WXSceneSession;;    //设置发送到朋友圈
                req1.transaction = "haoyou";  //这个tag要唯一,用于在回调中分辨是哪个分享请求
                if(CaiNiaoApplication.api==null){
                    CaiNiaoApplication.api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, true);
                    CaiNiaoApplication.api.registerApp(Constants.WX_APP_ID);
                }
                CaiNiaoApplication.api.sendReq(req1);   //如果调用成功微信,会返回true
                break;
            case R.id.btn_share:
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = APP_IP + "/wap/Index/share2/uid/" + SPUtils.getStringData(this, Constants.UID, "0");
                WXMediaMessage msg = new WXMediaMessage(webpage);
                Resources res =getResources();
                Bitmap bmp= BitmapFactory.decodeResource(res, R.mipmap.app_icon);
                Bitmap thumbBitmap = Bitmap.createScaledBitmap(bmp, 150, 150, true);
                msg.thumbData = WxUtil.bmpToByteArray(thumbBitmap,true);
                msg.description="年货节派新年红包啦！！！最高888元福气红包，帮你清空购物车！每天可领取三次喔！";
                msg.title="2019天猫年货合家-主会场（带超级红包）";
                //在这设置缩略图
                //官方文档介绍这个bitmap不能超过32kb
                //如果一个像素是8bit的话换算成正方形的bitmap则边长不超过181像素,边长设置成150是比较保险的
                //或者使用msg.setThumbImage(thumbBitmap);省去自己转换二进制数据的过程
                //如果超过32kb则抛异常

                SendMessageToWX.Req req = new SendMessageToWX.Req();    //创建一个请求对象
                req.message = msg;  //把msg放入请求对象中
                req.scene = SendMessageToWX.Req.WXSceneSession;    //设置发送到朋友圈
                //req.scene = SendMessageToWX.Req.WXSceneSession;   //设置发送给朋友
                req.transaction = "haoyou";  //这个tag要唯一,用于在回调中分辨是哪个分享请求
                if(CaiNiaoApplication.api==null){
                    CaiNiaoApplication.api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, true);
                    CaiNiaoApplication.api.registerApp(Constants.WX_APP_ID);
                }
               CaiNiaoApplication.api.sendReq(req);   //如果调用成功微信,会返回true
                break;
        }
    }
}
