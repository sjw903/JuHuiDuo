package com.android.jdhshop.my;

import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.juduohui.JuDuoHuiJavaScript;
import com.android.jdhshop.utils.MyWebView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yohn on 2018/10/25.
 */

public class MyMessageActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.listView)
    MyWebView listView;
    @BindView(R.id.tv_right)
    TextView tvRight;

    @Override
    protected void initUI() {
        setContentView(R.layout.fragment_service);
        ButterKnife.bind(this);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("刷新");
    }

    @Override
    protected void initData() {
        tvTitle.setText("常见问题");
        tv_left.setVisibility(View.VISIBLE);
        listView.setWebViewClient(webViewClient);
        listView.setWebChromeClient(webChromeClient);
        listView.setHorizontalScrollBarEnabled(false);//水平不显示
        listView.setVerticalScrollBarEnabled(false); //垂直不显示
        WebSettings webSettings = listView.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js
        //支持屏幕缩放
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        listView.loadUrl("file:///android_asset/index.html");
        listView.addJavascriptInterface(new JuDuoHuiJavaScript(listView, getComeActivity(), new JuDuoHuiJavaScript.CallBackListen() {
            @Override
            public void press() {

            }

            @Override
            public void closeWindow() {

            }

            @Override
            public void reloadWindow() {

            }

            @Override
            public void setStatusBarColor(String color_string) {

            }

            @Override
            public void showToasts(String msg) {

            }

            @Override
            public void renderAdvert(JSONObject adv_config) {

            }

            @Override
            public void getRewardVideoAd(String adv_config, String close_fun) {

            }

            @Override
            public void setAdvStation(String css_class, boolean station) {

            }

            @Override
            public void setScrollTop() {

            }

            @Override
            public void setNativeTitleBar(JSONObject native_title_bar_setting) {

            }

            @Override
            public void setCircleTimer(JSONObject circle_config) {

            }

            @Override
            public void startCircleAni() {

            }

            @Override
            public void removeAllAdv() {

            }

            @Override
            public void removeAdv(String config) {

            }

            @Override
            public void renderWebView(JSONObject web_config) {

            }

            @Override
            public void sendParamToWebView(JSONObject web_config) {

            }

            @Override
            public void scanQrCode(JSONObject config) {

            }

            @Override
            public void alert(JSONObject config) {

            }
        },null),"juduohui");
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.reload();
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
            return super.shouldOverrideUrlLoading(view, newurl);
        }
    };
    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private WebChromeClient webChromeClient = new WebChromeClient() {
        //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
            showToast(message);
//            AlertDialog.Builder localBuilder = new AlertDialog.Builder(webView.getContext());
//            localBuilder.setMessage(message).setPositiveButton("确定", null);
//            localBuilder.setCancelable(false);
//            localBuilder.create().show();
//
//            //注意:
//            //必须要这一句代码:result.confirm()表示:
//            //处理结果为确定状态同时唤醒WebCore线程
//            //否则不能继续点击按钮
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
        if (listView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {//点击返回按钮的时候判断有没有上一页
            listView.goBack(); // goBack()表示返回webView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void initListener() {
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listView.canGoBack()) {//点击返回按钮的时候判断有没有上一页
                    listView.goBack(); // goBack()表示返回webView的上一页面
                }else{
                    finish();
                }
            }
        });
    }
}
