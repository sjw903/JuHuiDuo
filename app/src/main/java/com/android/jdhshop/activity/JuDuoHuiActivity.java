package com.android.jdhshop.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.advistion.JuDuoHuiAdvertisement;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.fragments.JuDuoHuiFragment;
import com.android.jdhshop.juduohui.CommonDialog;
import com.android.jdhshop.juduohui.JuDuoHuiJavaScript;
import com.android.jdhshop.juduohui.JuduohuiRewardActivity;
import com.android.jdhshop.utils.IsWifi;
import com.android.jdhshop.utils.UIUtils;
import com.baidu.mobads.sdk.api.AdView;
import com.baidu.mobads.sdk.api.AdViewListener;
import com.bumptech.glide.Glide;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.king.zxing.Intents;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.comm.util.AdError;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//import com.tencent.smtt.sdk.CookieSyncManager;

public class JuDuoHuiActivity extends BaseActivity {

    Activity mContext = null;
    WebView wv = null;
    WebChromeClient xwwvcc = null;
    WebViewClient xwwvc = null;
    String TAG = "聚多惠WebAPP";

    @BindView(R.id.top_adv_scroll)
    ScrollView top_adv_scroll;
    @BindView(R.id.tmp_views)
    RelativeLayout tmp_views;


    @BindView(R.id.native_titlebar)
    LinearLayout native_title_bar;
    @BindView(R.id.desc_title)
    TextView desc_title;
    @BindView(R.id.desc_back)
    TextView desc_back;

    // 圆环外框
    @BindView(R.id.golds_circle_box)
    RelativeLayout golds_circle_box;
    // 圆形
    @BindView(R.id.golds_circle)
    DonutProgress golds_circle;
    // 圆环的背景
    @BindView(R.id.gold_bg)
    ImageView golds_bg;
    // 圆环的动画
    @BindView(R.id.gold_ani)
    ImageView golds_ani;
    @BindView(R.id.third_iweb)
    WebView thirdIWeb;

    JuDuoHuiJavaScript js = null;
    boolean has_user = true;
    String ip = "";

    private List<JSONObject> ads_list = new ArrayList<>();
    private String root_path = "";
    private static final int REQUEST_REWARD_ADV = 2000;

    /**
     * 自定义WebClient
     * 增加本地相册调用处理
     */
    private static final int REQUEST_FILE_CHOOSER_CODE = 102;
    private static final int REQUEST_SCAN_QR_CODE = 105;
    private JSONObject scan_qrcode_config = null;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})

    int i = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStatusBar(Color.TRANSPARENT);
        setStatusBar(Color.parseColor("#fe8358"));
        setTranslucentStatus(true);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_ju_duo_hui_webview);
        ButterKnife.bind(this);
        native_title_bar.setVisibility(View.INVISIBLE);
        mContext = this;
        if (CaiNiaoApplication.getUserInfoBean() == null) {
            commonGetUserMsg();
        }

        // 设置默认隐藏掉
        golds_circle.setTextSize(0.01F);
        golds_circle_box.setVisibility(View.INVISIBLE);
        golds_bg.clearAnimation();
        golds_ani.clearAnimation();
        golds_ani.setVisibility(View.INVISIBLE);


        root_path = mContext.getExternalFilesDir(null).getPath().replace("/storage/emulated/0", "/sdcard");
        File check_file = new File(root_path);
        if (check_file.exists()) {
            // Log.d(TAG, "onCreate:1 " + root_path);
            root_path = "file://" + root_path + "/web_app/";
            // Log.d(TAG, "onCreate:2 " + root_path);
        } else {
            root_path = "file:///android_asset/web_app/";
        }

        FrameLayout.LayoutParams ly = (FrameLayout.LayoutParams) golds_circle_box.getLayoutParams();
        golds_circle_box.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean return_val = true;
                LogUtils.d(TAG, "onTouch: " + event.getAction());
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if (event.getEventTime() - event.getDownTime() < 200) {
                            return_val = false;
                            v.performClick();
                        } else {
                            return_val = true;
                        }
                        break;
                    case MotionEvent.ACTION_DOWN:
                        return_val = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        LogUtils.d(TAG, "onTouch: " + event.getRawX() + "," + event.getRawY());
                        ly.leftMargin = (int) (event.getRawX() - ly.height / 2);
                        ly.topMargin = (int) (event.getRawY() - ly.width / 2);
                        golds_circle_box.setLayoutParams(ly);
                        return_val = true;
                        break;
                }
                return return_val;
            }
        });


        wv = findViewById(R.id.wv);
        wv.setWebViewClient(new JuDuoHuiWebClient());
        wv.setWebChromeClient(new JuDuoHuiWebChromeClient());
        WebSettings webSetting = wv.getSettings();
        CookieSyncManager.createInstance(this);
        CookieManager instance = CookieManager.getInstance();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            instance.setAcceptThirdPartyCookies(wv, true);
        instance.setAcceptCookie(true);
        CookieSyncManager.getInstance().sync();
        js = new JuDuoHuiJavaScript(wv, mContext, new JuDuoHuiJavaScript.CallBackListen() {
            @Override
            public void scanQrCode(JSONObject config) {
                Acp.getInstance(mContext).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.CAMERA)
                        .build(), new AcpListener() {

                    @Override
                    public void onGranted() {
                        scan_qrcode_config = config;
                        Intent scan_intent = new Intent(mContext, MyScanActivity.class);
                        scan_intent.putExtra("set_title", config.getString("title"));
                        startActivityForResult(scan_intent, REQUEST_SCAN_QR_CODE);
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        showToast("拒绝权限不能使用扫一扫");
                    }
                });
            }

            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void alert(JSONObject config) {
                /**
                 * 优先显示以 url为主体的
                 */
                runOnUiThread(() -> {
                    try {
                        String alert_title = config.getString("title");
                        String alert_message = config.getString("message");
                        String alert_url = config.getString("url");
                        String close_fun = config.getString("close");
                        String dismiss_fun = config.getString("dismiss");
                        JSONObject submit_config = config.containsKey("submit") ? config.getJSONObject("submit") : null;
                        JSONObject cancel_config = config.containsKey("cancel") ? config.getJSONObject("cancel") : null;
                        boolean cancel_out_side = !config.containsKey("cancel_outside") || config.getBooleanValue("cancel_outside");

                        // Log.d(TAG, "alert: alert_url = " + alert_url);
                        CommonDialog dialog = new CommonDialog(getComeActivity());
                        // 底部按钮配置段
                        if (submit_config == null) {
                            dialog.getSubmitButton().setVisibility(View.GONE);
                        } else {
                            dialog.setSubmit(submit_config.getString("text"), submit_config.getBoolean("active"));
                        }
                        if (cancel_config == null) {
                            dialog.getCancelButton().setVisibility(View.GONE);
                        } else {
                            dialog.setCancelButton(cancel_config.getString("text"), cancel_config.getBoolean("active"));
                        }

                        // 内容配置段,如果是 网址，则不显示下部的按钮
                        if (alert_url != null) {
                            WebView inner_wv = new WebView(getComeActivity()) {
                                @Override
                                protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                                    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
                                    int heightSize = MeasureSpec.getSize(heightMeasureSpec);
                                    int mMaxHeight = UIUtils.dp2px(200);
                                    if (heightMode == MeasureSpec.EXACTLY) {
                                        heightSize = heightSize <= mMaxHeight ? heightSize
                                                : (int) mMaxHeight;
                                    }
                                    if (heightMode == MeasureSpec.UNSPECIFIED) {
                                        heightSize = heightSize <= mMaxHeight ? heightSize
                                                : (int) mMaxHeight;
                                    }
                                    if (heightMode == MeasureSpec.AT_MOST) {
                                        heightSize = heightSize <= mMaxHeight ? heightSize
                                                : (int) mMaxHeight;
                                    }
                                    int maxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize,
                                            heightMode);
                                    super.onMeasure(widthMeasureSpec, maxHeightMeasureSpec);
                                }
                            };
                            LinearLayout.LayoutParams inner_wv_lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            inner_wv.setLayoutParams(inner_wv_lp);

                            CookieSyncManager.createInstance(getComeActivity());
                            CookieManager instance = CookieManager.getInstance();
                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                                instance.setAcceptThirdPartyCookies(inner_wv, true);
                            instance.setAcceptCookie(true);
                            CookieSyncManager.getInstance().sync();

                            WebSettings inner_webSetting = inner_wv.getSettings();
                            inner_webSetting.setAllowFileAccess(true);

                            inner_webSetting.setAllowUniversalAccessFromFileURLs(true);
                            inner_webSetting.setAllowFileAccessFromFileURLs(true);

                            inner_webSetting.setAllowContentAccess(true);
                            inner_webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
                            inner_webSetting.setSupportZoom(false);
                            inner_webSetting.setTextZoom(100);
                            inner_webSetting.setBuiltInZoomControls(false);
                            inner_webSetting.setUseWideViewPort(true);
                            inner_webSetting.setSupportMultipleWindows(false);//这里一定得是false,不然打开的网页中，不能在点击打开了
                            // settings.setLoadWithOverviewMode(true);
                            inner_webSetting.setAppCacheEnabled(true);
                            inner_webSetting.setDomStorageEnabled(true);
                            inner_webSetting.setJavaScriptEnabled(true);
                            inner_webSetting.setGeolocationEnabled(true);
                            inner_webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
                            inner_webSetting.setAppCachePath(mContext.getDir("appcache", 0).getPath());
                            inner_webSetting.setDatabasePath(mContext.getDir("databases", 0).getPath());
                            inner_webSetting.setGeolocationDatabasePath(mContext.getDir("geolocation", 0).getPath());
                            inner_webSetting.setBlockNetworkImage(false); // 解决图片不显示
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                inner_webSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                            }
                            thirdIWeb.addJavascriptInterface(js, "juduohui");
                            inner_webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
                            inner_webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);

                            inner_wv.addJavascriptInterface(js, "juduohui");
                            inner_wv.addJavascriptInterface(new JuDuoHuiFragment.DialogJs(dialog), "dialog");
                            dialog.getButtonContent().setVisibility(View.GONE);
                            dialog.setShowCloseButton(false);
                            LinearLayout message_content = dialog.getMessageContent();
                            message_content.removeAllViews();
                            message_content.addView(inner_wv);

                            String inner_url = StringUtils.startsWith(alert_url, "http") ? alert_url : root_path + "/" + alert_url;
                            inner_wv.loadUrl(inner_url);
                        } else {
                            dialog.setMessage(alert_message);
                        }

                        if (alert_title != null || "".equals(alert_title))
                            dialog.setTitle(alert_title);

                        dialog.setListener(new CommonDialog.CommonDialogListener() {
                            @Override
                            public void OnSubmit(AlertDialog dialog) {
                                if (submit_config != null) {
                                    wv.evaluateJavascript("(" + submit_config.getString("click") + ").call(this.vue)", null);
                                }
                                dialog.dismiss();
                            }

                            @Override
                            public void OnCancel(AlertDialog dialog) {
                                if (cancel_config != null) {
                                    wv.evaluateJavascript("(" + cancel_config.getString("click") + ").call(this.vue)", null);
                                }
                                dialog.dismiss();
                            }

                            @Override
                            public void OnDismiss() {
                                if (dismiss_fun!=null){
                                    wv.evaluateJavascript("("+dismiss_fun+").call(this.vue)",null);
                                }
                            }

                            @Override
                            public void OnClose(AlertDialog dialog) {
                                if (close_fun != null) {
                                    wv.evaluateJavascript("(" + close_fun + ").call(this.vue)", null);
                                }
                            }
                        });

                        dialog.setCanceledOnTouchOutside(cancel_out_side);
                        dialog.show();

                    } catch (Exception e) {
                        showToasts("弹窗初始化失败");
                    }
                });

            }

            @Override
            public void removeAllAdv() {
                runOnUiThread(() -> {
                    for (JSONObject itm : ads_list) {
                        LogUtils.d(TAG, "removeAllAdv: " + itm.get("ad_view").getClass().getName());
                    }
                    tmp_views.removeAllViews();
                    ads_list.clear();
                });
            }

            @Override
            public void sendParamToWebView(JSONObject web_config) {
                String type = web_config.getString("type");
                String callback = web_config.getString("callback");

                WebView target_view = null;
                if (type.equals("main")) {
                    target_view = wv;
                } else {
                    target_view = thirdIWeb;
                }
                final WebView t_view = target_view;
                runOnUiThread(() -> {
                    t_view.evaluateJavascript("(" + callback + ").call(this.vue)", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            LogUtils.d(TAG, "onReceiveValue: " + value);
                        }
                    });
                });

            }

            @Override
            public void renderWebView(JSONObject web_config) {
                LogUtils.d(TAG, "renderWebView: " + web_config.toJSONString());
                boolean is_display = web_config.getString("display").equals("yes");
                int width = web_config.getIntValue("width");
                int height = web_config.getIntValue("height");
                int top = web_config.getIntValue("top");
                int bottom = web_config.getIntValue("bottom");
                int left = web_config.getIntValue("left");


                LogUtils.d(TAG, "renderWebView: " + dm.toString());
                FrameLayout.LayoutParams lm = (FrameLayout.LayoutParams) thirdIWeb.getLayoutParams();
                lm.width = (int) (width * dm.scaledDensity);
                lm.height = (int) (height * dm.scaledDensity);
                lm.topMargin = (int) (top * dm.scaledDensity);
                lm.leftMargin = (int) (left * dm.scaledDensity);
                lm.bottomMargin = (int) (bottom * dm.scaledDensity);
                runOnUiThread(() -> {
                    thirdIWeb.setLayoutParams(lm);
                    if (is_display) {
                        thirdIWeb.setVisibility(View.VISIBLE);
                        String web_url = web_config.getString("url");
                        if (web_url != null && !web_url.startsWith("http")) {
                            web_url = root_path + web_url;
                            thirdIWeb.loadUrl(web_url);
                        } else if (web_url != null) {
                            thirdIWeb.loadUrl(web_url);
                        }

                    } else {
                        thirdIWeb.setVisibility(View.INVISIBLE);
                    }

                });

            }

            @Override
            public void startCircleAni() {
                runOnUiThread(() -> {
//                        golds_bg.startAnimation()
                    // 将原始的金币图缩小
                    Animation aa = new ScaleAnimation(1.0f, 0f, 1.0f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    aa.setDuration(3500);
                    aa.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            runOnUiThread(() -> {
                                golds_ani.setVisibility(View.VISIBLE);
                            });
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            runOnUiThread(() -> {
                                golds_ani.setVisibility(View.INVISIBLE);
                            });
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    golds_bg.startAnimation(aa);
                });
            }

            @Override
            public void setCircleTimer(JSONObject circle_config) {
                runOnUiThread(() -> {
                    // 是否显示
                    if (circle_config.containsKey("display")) {
                        boolean is_display = circle_config.getBooleanValue("display");
                        golds_circle_box.setVisibility(is_display ? View.VISIBLE : View.INVISIBLE);
                    }
                    // 位置
                    JSONObject position = circle_config.getJSONObject("position");
                    if (position != null) {
                        int top = position.getIntValue("top");
                        int left = position.getIntValue("left");
                        FrameLayout.LayoutParams circle_box_layout_params = (FrameLayout.LayoutParams) golds_circle_box.getLayoutParams();
                        if (position.containsKey("top")) {
                            circle_box_layout_params.topMargin = (int) (top * dm.scaledDensity - golds_circle_box.getHeight());
                            LogUtils.d(TAG, "setCircleTimer: top_margin" + top * dm.scaledDensity + ",xxxxxxxxx" + golds_circle_box.getHeight() + "," + golds_circle_box.getMeasuredHeight() + "," + golds_circle_box.getMinimumHeight());
                        }
                        if (position.containsKey("left")) {
                            circle_box_layout_params.leftMargin = (int) (left * dm.scaledDensity - golds_circle_box.getHeight());
                            LogUtils.d(TAG, "setCircleTimer: left_margin " + left * dm.scaledDensity);
                        }
                        golds_circle_box.setLayoutParams(circle_box_layout_params);
                    }
                    // 最小值，最大值，当前值
//                int min = circle_config.getIntValue("min");
                    int max = circle_config.getIntValue("max");
                    if (max != 0) {
                        golds_circle.setMax(max);
                    }
                    int current = circle_config.getIntValue("current");
                    if (current != 0) {
                        golds_circle.setProgress(current);
                    }
                    // 中间图片，普通图片，动画图片
                    String normal_img = circle_config.getString("normal_img");
                    if (normal_img != null) {
                        if (normal_img.startsWith("http")) {
                            Glide.with(mContext).load(normal_img).asBitmap().into(golds_bg);
                        } else {
                            try {
                                Glide.with(mContext).load(root_path + normal_img).asBitmap().into(golds_bg);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    String ani_img = circle_config.getString("ani_img");
                    if (ani_img != null) {
                        if (ani_img.startsWith("http")) {
                            Glide.with(mContext).load(ani_img).asGif().into(golds_ani);
                        } else {

                            try {
                                Glide.with(mContext).load(root_path + ani_img).asGif().into(golds_ani);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    String finish_color = circle_config.getString("finish_color");
                    if (finish_color != null)
                        golds_circle.setFinishedStrokeColor(Color.parseColor(finish_color));
                    float finish_width = circle_config.getFloatValue("finish_width");
                    if (finish_width != 0) golds_circle.setFinishedStrokeWidth(finish_width);
                    String unfinish_color = circle_config.getString("unfinish_color");
                    if (unfinish_color != null)
                        golds_circle.setUnfinishedStrokeColor(Color.parseColor(unfinish_color));
                    float unfinish_width = circle_config.getFloatValue("unfinish_width");
                    if (unfinish_width != 0) golds_circle.setUnfinishedStrokeWidth(unfinish_width);
                    String inner_background = circle_config.getString("inner_background");
                    if (inner_background != null)
                        golds_circle.setInnerBackgroundColor(Color.parseColor(inner_background));
                });
            }

            @Override
            public void setNativeTitleBar(JSONObject title_bar_set) {

                boolean is_display = title_bar_set.getBoolean("display");

                if (!is_display) return;

                String title_string = title_bar_set.getString("title");
                LogUtils.d(TAG, "setNativeTitleBar: " + title_string + ",is_display:" + is_display);
                String back_icon_path = title_bar_set.getString("back_icon"); // 返回的图片资源文件
                String title_font_color = title_bar_set.getString("color");
                float font_size = title_bar_set.getFloatValue("font-size");
                if (back_icon_path.startsWith("http")) {

                } else {
                    //back_icon_path = "file:///android_asset/web_app/" + back_icon_path;

                }
                String back_icon_function = title_bar_set.getString("back_function"); // 返回键的功能

                InputStream icon_stream = null;
                try {
                    if (root_path.indexOf("android_asset") > 0) {
                        icon_stream = getAssets().open("web_app/" + back_icon_path);
                    } else {
                        File icon_file = new File(root_path.replace("file://", "") + back_icon_path);
                        icon_stream = new FileInputStream(icon_file);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Drawable back_icon = BitmapDrawable.createFromStream(icon_stream, "back_icon");
                back_icon.setBounds(0, 0, 42, 42);
                runOnUiThread(() -> {
                    native_title_bar.setVisibility(is_display ? View.VISIBLE : View.INVISIBLE);
                    native_title_bar.post(() -> {
                        LogUtils.d(TAG, "setNativeTitleBar: " + native_title_bar.getMeasuredHeight() + "," + native_title_bar.getMinimumHeight() + "," + native_title_bar.getMeasuredHeight() / dm.scaledDensity);
                        wv.evaluateJavascript("$('body').css({'padding-top':'" + native_title_bar.getMeasuredHeight() / dm.scaledDensity + "px'});", null);
                    });
                    desc_title.setTextSize(font_size);
                    desc_title.setText(title_string);
                    if (title_font_color != null)
                        desc_title.setTextColor(Color.parseColor(title_font_color));
                    desc_back.setCompoundDrawables(back_icon, null, null, null);
                    desc_back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            runOnUiThread(() -> {
                                wv.evaluateJavascript(back_icon_function, new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String value) {
                                        // 处理传递过来方法没有
                                        if (value.contains("not defined")) {
                                            runOnUiThread(() -> {
                                                onBackPressed();
                                            });
                                        }
                                    }
                                });
                            });
                        }
                    });
                });

            }

            @Override
            public void setAdvStation(String css_class, boolean station) {
                for (int i = 0; i < ads_list.size(); i++) {
                    JSONObject item = ads_list.get(i);
                    if (css_class.equals("") || item.getString("web_class_name").equals(css_class)) {
                        runOnUiThread(() -> {
                            LinearLayout ad_box_view = item.getObject("ad_box_view", LinearLayout.class);
                            ad_box_view.setVisibility(station ? View.VISIBLE : View.INVISIBLE);
                        });
                    }
                }
            }

            @Override
            public void removeAdv(String css_class) {
                for (int i = 0; i < ads_list.size(); i++) {
                    JSONObject item = ads_list.get(i);
                    if (css_class.equals("") || item.getString("web_class_name").equals(css_class)) {
                        runOnUiThread(() -> {
                            LinearLayout ad_box_view = item.getObject("ad_box_view", LinearLayout.class);
                            ads_list.remove(item);
                            tmp_views.removeView(ad_box_view);
                        });
                    }
                }
            }

            @Override
            public void setScrollTop() {
                runOnUiThread(() -> {
//                    top_adv_scroll.scrollTo(0,scrollTopValue);
                    String adclass_id = "";
                    for (JSONObject item : ads_list) {
                        adclass_id = adclass_id + "'" + item.getString("web_class_name") + "',";
                    }
                    String run_string = "(function(a){var t=[];for(var i=0;i<a.length;i++){var w=$(a[i]).offset();w.cls=a[i];w.maskhide=$(a[i]).attr('mask-hidden');t.push(w)}return t})([" + adclass_id + "])";
                    LogUtils.d("setScrollsetScrollTopTop: " + run_string);
                    wv.evaluateJavascript(run_string, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
//                            LogUtils.d("复写的onReceiveValue: " + value);
                            try {
                                JSONArray newAdInfo = JSONArray.parseArray(value);
                                setAdvInfo(newAdInfo);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                });
            }


            @Override
            public void renderAdvert(JSONObject adv_config) {

                LogUtils.d(TAG, "renderAdvert: " + adv_config.toJSONString());

                int type = adv_config.containsKey("type") ? adv_config.getIntValue("type") : 0;
                LogUtils.d(TAG, "renderAdvert:  type = " + type);
                String channel = adv_config.getString("channel");
                String ads_class = adv_config.getString("ads_class");
                String adv_position_id = adv_config.getString("position_id");
                String order_id = adv_config.getString("order_id");
                String extra_data = adv_config.getString("extra_data");
                String display_fun = adv_config.getString("ondisplay");
                String click_fun = adv_config.getString("onclick");
                String download_fun = adv_config.getString("ondownload");
                String error_fun = adv_config.getString("onerror");
                String close_fun = adv_config.getString("onclose");
                String org_config = adv_config.getString("org_config"); // 原始广告配置带价格、列表

                if (type == 0 || type == 2) removeAdv(ads_class);
                runOnUiThread(() -> {
                    // 原
//                    String java_script = "(function(c){return $(c).offset();})('" + ads_class + "')";
                    // 新增方法,增加宽度（w），左填充（l）
                    String java_script = "(function(s){var o=$(s);if(o.length==0)return null;var p=o.offset();var w=o.width();var ow=o.innerWidth();var ml=(ow-w)/2;p.w=w;p.l=ml;return p})('"+ads_class +"')";
                    LogUtils.d("getAd: " + java_script);
                    wv.evaluateJavascript(java_script, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            LogUtils.d("getAd: onReceiveValue: " + value);
                            if (value == null || value.equals("null")) {
                                return;
                            }
                            String tmp = StringEscapeUtils.unescapeJson(value);

                            tmp = tmp.replace("\"", "");

                            JSONObject infomation = new JSONObject();
                            LogUtils.d("onReceiveValue: " + tmp);
                            JSONObject position = JSONObject.parseObject(tmp);

                            LogUtils.d("onReceiveValue: " + position.toString());
                            infomation.put("ad_class_name", ads_class);
                            infomation.put("ad_position_id", adv_position_id);
                            infomation.put("position", position);
                            infomation.put("order_id", order_id);
                            infomation.put("extra_data", extra_data);

                            infomation.put("display_fun", display_fun != null ? "(" + display_fun.replaceAll("\\t", "\t").replaceAll("\\n", "\n") + ").call(this.vue)" : null);
                            infomation.put("click_fun", click_fun != null ? "(" + click_fun + ").call(this.vue)" : null);
                            infomation.put("download_fun", download_fun != null ? "(" + download_fun + ").call(this.vue)" : null);
                            infomation.put("error_fun", error_fun != null ? "(" + error_fun + ").call(this.vue)" : null);
                            infomation.put("close_fun",close_fun != null ? "(" + close_fun + ").call(this.vue)" : null);

                            // adv_position_id 1 优量汇，2，穿山甲

                            if (type == 0) {

                                String ad_position_id = infomation.getString("ad_position_id");
                                String display_fun = infomation.getString("display_fun");
                                String click_fun = infomation.getString("click_fun");
                                String download_fun = infomation.getString("download_fun");
                                String close_fun = infomation.getString("close_fun");
                                String err_fun = infomation.getString("error_fun");
                                String channel = infomation.getString("channel");

                                JSONArray adv_cfg;
                                if (org_config.equals("")){
                                    adv_cfg = new JSONArray();
                                    JSONObject adv_cfg_item = new JSONObject();
                                    adv_cfg_item.put("channel",channel);
                                    adv_cfg_item.put("advertisement_id",ad_position_id);
                                    adv_cfg.add(adv_cfg_item);
                                }
                                else{
                                    JSONObject org_cnf = JSONObject.parseObject(org_config);
                                    adv_cfg = org_cnf.getJSONArray("list");
                                }

                                JuDuoHuiAdvertisement juDuoHuiAdvertisement = new JuDuoHuiAdvertisement(getComeActivity(),null);
                                juDuoHuiAdvertisement.setInfomationAdListen(new JuDuoHuiAdvertisement.InfomationAdListen() {
                                    int error_count = 0;
                                    int max_retry = 5;
                                    @Override
                                    public void click(View v) {
                                        runOnUiThread(()-> {
                                            if (click_fun != null)
                                                wv.evaluateJavascript(click_fun, null);
                                        });
                                    }

                                    @Override
                                    public void dislike() {
                                        runOnUiThread(()-> {
                                            if (close_fun != null)
                                                wv.evaluateJavascript(close_fun, null);
                                        });
                                    }

                                    @Override
                                    public void display(View v, String position,JSONObject c) {
                                        runOnUiThread(()->{
                                            commonAddAddToViews(infomation,v);
                                        });
                                    }

                                    @Override
                                    public void displayed() {
                                        runOnUiThread(()-> {
                                            if (display_fun != null)
                                                wv.evaluateJavascript(display_fun, null);
                                        });
                                    }

                                    @Override
                                    public void close() {
                                        runOnUiThread(()-> {
                                            if (close_fun != null)
                                                wv.evaluateJavascript(close_fun, null);
                                        });
                                    }

                                    @Override
                                    public void error(JSONObject error) {
                                        // Log.d(TAG, "error: " + error_count + "," + max_retry);
                                        // Log.d(TAG, "error: " + error.toJSONString());
                                        if (error_count<max_retry) {
                                            error_count = error_count +1;
                                            runOnUiThread(() -> {
                                                juDuoHuiAdvertisement.getInfomationAdv(adv_cfg, "web_get_vivo");
                                            });
                                        }
                                        else{
                                            runOnUiThread(()-> {
                                                if (err_fun != null)
                                                    wv.evaluateJavascript(err_fun, null);
                                            });
                                        }
                                    }
                                });
                                juDuoHuiAdvertisement.getInfomationAdv(adv_cfg,"web_get_vivo");

//                                if (channel.equals("1")) {
//                                    getYouLiangHui(infomation);
//                                } else if (channel.equals("2")) {
//                                    getChuanShanJia(infomation);
//                                } else if (channel.equals("3")) {
//                                    getKuaiShou(infomation);
//                                } else if (channel.equals("4")) {
//                                    getBaidu(infomation);
//                                } else if (channel.equals("5")) {
//                                    getBaidu(infomation);
//                                } else if (channel.equals("6")) {
//                                    getBaidu(infomation);
//                                } else if (channel.equals("7")) {
//                                    getBaidu(infomation);
//                                }
                            } else if (type == 2) {
                                if (channel.equals("1")) {
                                    getYouLiangHuiBanner(infomation);
                                } else if (channel.equals("2")) {
                                    getChuanShanJiaBanner(infomation);
                                } else if (channel.equals("3")) {
                                    getKuaiShouBanner(infomation);
                                } else if (channel.equals("4")) {
                                    getBaiduBanner(infomation);
                                }
                            }
                        }
                    });
                });

            }

            @Override
            public void getRewardVideoAd(String adv_config, String close_fun) {
                Intent intent = new Intent(getComeActivity(), JuduohuiRewardActivity.class);
                intent.putExtra("adv_config",adv_config);
                intent.putExtra("close_fun",close_fun);
                startActivityForResult(intent,REQUEST_REWARD_ADV);
            }


            @Override
            public void showToasts(String msg) {
                runOnUiThread(() -> {
                    showToast(msg);
                });
            }

            @Override
            public void press() {
                runOnUiThread(() -> {
                    if (wv.canGoBack()) {
                        wv.goBack();
                    } else {
                        LogUtils.d("press: 按了返回键");
                        finish();
                    }
                });
            }

            @Override
            public void setStatusBarColor(String color_string) {
                runOnUiThread(() -> {
                    setStatusBar(Color.parseColor(color_string));
                });
            }

            @Override
            public void reloadWindow() {
                runOnUiThread(() -> {
                    tmp_views.removeAllViews();
                    ads_list.clear();
                    wv.reload();
                });
            }

            @Override
            public void closeWindow() {

            }
        }, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                LogUtils.d("onReceiveValue: " + value);
            }
        });


        webSetting.setAllowFileAccess(true);

        webSetting.setAllowUniversalAccessFromFileURLs(true);
        webSetting.setAllowFileAccessFromFileURLs(true);

        webSetting.setAllowContentAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(false);
        webSetting.setTextZoom(100);

        webSetting.setBuiltInZoomControls(false);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setUseWideViewPort(false);
        webSetting.setSupportMultipleWindows(false);//这里一定得是false,不然打开的网页中，不能在点击打开了
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(mContext.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(mContext.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(mContext.getDir("geolocation", 0).getPath());
        webSetting.setBlockNetworkImage(false); // 解决图片不显示
//        webSetting.setUserAgentString(webSetting.getUserAgentString()+" "+( getIntent().getStringExtra("ua")==null?"":getIntent().getStringExtra("ua")));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting
                    .setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        wv.addJavascriptInterface(js, "juduohui");
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);

        Intent trans_intent = getIntent();
        String load_url = root_path + "/index.html";//"file:///android_asset/web_app/art_read.html";
        if (trans_intent.getStringExtra("url") != null && !"".equals(trans_intent.getStringExtra("url"))) {
            load_url = trans_intent.getStringExtra("url");
        }

        if (!load_url.startsWith("http")) {
            load_url = root_path + load_url;
        }
        has_user = trans_intent.getBooleanExtra("has_user", true);
        ip = IsWifi.getLocalIpAddress(this);
        LogUtils.d(TAG, "onCreate: " + load_url);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            instance.setAcceptThirdPartyCookies(thirdIWeb, true);
        instance.setAcceptCookie(true);
        CookieSyncManager.getInstance().sync();

        WebSettings settings = thirdIWeb.getSettings();
        settings.setAllowFileAccess(true);

        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowFileAccessFromFileURLs(true);

        settings.setAllowContentAccess(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setSupportZoom(false);
        settings.setTextZoom(100);
        settings.setBuiltInZoomControls(false);
        settings.setUseWideViewPort(true);
        settings.setSupportMultipleWindows(false);//这里一定得是false,不然打开的网页中，不能在点击打开了
        // settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setAppCacheMaxSize(Long.MAX_VALUE);
        settings.setAppCachePath(mContext.getDir("appcache", 0).getPath());
        settings.setDatabasePath(mContext.getDir("databases", 0).getPath());
        settings.setGeolocationDatabasePath(mContext.getDir("geolocation", 0).getPath());
        settings.setBlockNetworkImage(false); // 解决图片不显示
//        settings.setUserAgentString(settings.getUserAgentString()+" "+( getIntent().getStringExtra("ua")==null?"":getIntent().getStringExtra("ua")));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings
                    .setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        thirdIWeb.addJavascriptInterface(js, "juduohui");
        settings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        thirdIWeb.setBackgroundColor(Color.TRANSPARENT);
//        thirdIWeb.setVisibility(View.VISIBLE);
//        thirdIWeb.loadUrl("file:///android_asset/web_app/news_read_head.html");
        /**
         * 读取前端传过来的CONFIG【JSON】 进行配置
         */

        if (trans_intent.getStringExtra("config") != null) {

            JSONObject config = JSONObject.parseObject(trans_intent.getStringExtra("config"));
            if (config.getBooleanValue("setSupportZoom")) {
                webSetting.setSupportZoom(true);
            }

            if (config.getBooleanValue("setBuiltInZoomControls")) {
                webSetting.setBuiltInZoomControls(true);
            }
        }

        // 配置结束
        try {

            wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
        } catch (Exception es) {
            es.printStackTrace();
        }

        wv.loadUrl(load_url);

        top_adv_scroll.setSmoothScrollingEnabled(true);
        top_adv_scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                wv.dispatchTouchEvent(event);
                return true;
            }
        });

    }

    @Override
    protected void initUI() {
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {
    }


    private View my_ads = null;
    private RelativeLayout re = null;
    private RelativeLayout.LayoutParams lm = null;
    private WindowManager wm = null;
    private DisplayMetrics dm = null;
    private ViewGroup.LayoutParams scroll_view_lm = null;


    /**
     * 设置web页面中的广告位置信息
     *
     * @param web_ad_infomation 包含信息 { 'ad_class_name':'.cls','position':{'top':300,'left':300} }
     */
    private void setAdvInfo(JSONArray web_ad_infomation) {
//        LogUtils.d("setAdvInfo: 传递过来的参数:" + web_ad_infomation.toJSONString());
//        LogUtils.d("setAdvInfo: 数组：" + ads_list.size());
        for (int i = 0; i < ads_list.size(); i++) {
            JSONObject item = ads_list.get(i);
            for (int j = 0; j < web_ad_infomation.size(); j++) {
//                LogUtils.d(TAG, "setAdvInfo: web_class_name = " + item.getString("web_class_name") + ",cls =" + web_ad_infomation.getJSONObject(j).getString("cls") + ",maskhide=" +web_ad_infomation.getJSONObject(j).getString("maskhide"));
                if (item.getString("web_class_name").equals(web_ad_infomation.getJSONObject(j).getString("cls"))) {

                    if (web_ad_infomation.getJSONObject(j).containsKey("maskhide") && web_ad_infomation.getJSONObject(j).getString("maskhide").equals("yes")) {
                        LogUtils.d(TAG, "setAdvInfo: 隐藏掉被遮罩挡住的层");
                        LinearLayout ad_box_view = item.getObject("ad_box_view", LinearLayout.class);
                        ad_box_view.setVisibility(View.INVISIBLE);
                        continue;
                    }


                    int finalJ = j;
                    runOnUiThread(() -> {
//                        LogUtils.d("setAdvInfo: " + item.getString("web_class_name"));
                        LinearLayout ad_box_view = item.getObject("ad_box_view", LinearLayout.class);
                        ad_box_view.setVisibility(View.VISIBLE);
                        //LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ad_box_view.getLayoutParams();
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ad_box_view.getLayoutParams();
                        layoutParams.topMargin = (int) (web_ad_infomation.getJSONObject(finalJ).getFloat("top") * dm.scaledDensity);
                        ad_box_view.setLayoutParams(layoutParams);
                    });
                }
            }


        }
//        for (JSONObject item : ads_list){
//            LogUtils.d("setAdvInfo: 分解list中出现的 " + item.toJSONString());
//            if (item.getString("web_class_name").equals(web_ad_infomation.getString("ad_class_name"))){
//                LogUtils.d("setAdvInfo: 找个这个类的广告了");
//                LinearLayout ad_box_view = item.getObject("ad_box_view",LinearLayout.class);
//
//            }
//        }
    }


    UnifiedInterstitialAD banner = null;

    private void getYouLiangHuiBanner(JSONObject web_ad_infomation) {

        LogUtils.d(TAG, "getYouLiangHuiBanner: ");
        String adv_position_id = web_ad_infomation.getString("ad_position_id");
        String display_fun = web_ad_infomation.getString("display_fun");
        String click_fun = web_ad_infomation.getString("click_fun");
        String download_fun = web_ad_infomation.getString("download_fun");
        String err_fun = web_ad_infomation.getString("error_fun");

        banner = new UnifiedInterstitialAD(JuDuoHuiActivity.this, adv_position_id, new UnifiedInterstitialADListener() {
            @Override
            public void onADReceive() {
                LogUtils.d(TAG, "onADReceive: ");
                banner.show();
            }

            @Override
            public void onVideoCached() {
                LogUtils.d(TAG, "onVideoCached: ");

            }

            @Override
            public void onNoAD(AdError adError) {
                LogUtils.d(TAG, "onNoAD: ");
            }

            @Override
            public void onADOpened() {
                LogUtils.d(TAG, "onADOpened: ");
            }

            @Override
            public void onADExposure() {
                LogUtils.d(TAG, "onADExposure: ");
            }

            @Override
            public void onADClicked() {
                LogUtils.d(TAG, "onADClicked: ");
            }

            @Override
            public void onADLeftApplication() {
                LogUtils.d(TAG, "onADLeftApplication: ");
            }

            @Override
            public void onADClosed() {
                LogUtils.d(TAG, "onADClosed: ");
            }

            @Override
            public void onRenderSuccess() {
                LogUtils.d(TAG, "onRenderSuccess: ");
            }

            @Override
            public void onRenderFail() {
                LogUtils.d(TAG, "onRenderFail: ");
            }
        });

        banner.loadAD();
    }
    private void getChuanShanJiaBanner(JSONObject web_ad_infomation) {
        LogUtils.d("getChuanShanJiaBanner: " + web_ad_infomation.toJSONString());

        String ad_position_id = web_ad_infomation.getString("ad_position_id");
        String display_fun = web_ad_infomation.getString("display_fun");
        String click_fun = web_ad_infomation.getString("click_fun");
        String download_fun = web_ad_infomation.getString("download_fun");
        String err_fun = web_ad_infomation.getString("error_fun");

        // 穿山甲
        TTAdNative mTTAdNative = TTAdSdk.getAdManager().createAdNative(mContext);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(ad_position_id) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(500, 0) //期望模板广告view的size,单位dp
                .build();
        mTTAdNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            //广告请求失败
            @Override
            public void onError(int code, String message) {
                LogUtils.d("onError475: " + message);
                if (err_fun != null) wv.evaluateJavascript(err_fun, null);
                runOnUiThread(() -> {
//                    LogUtils.d("getChuanShanJia onError: 设置广告位高度" + height);
                    wv.evaluateJavascript("$('" + web_ad_infomation.getString("ad_class_name") + "').height(0)", null);
                    js.setScrollTop();
                });
            }

            //广告请求成功
            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                LogUtils.d("onNativeExpressAdLoad: ");
                for (TTNativeExpressAd ad : ads) {
                    TTNativeExpressAd adTmp = ad;
                    adTmp.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
                        @Override
                        public void onAdClicked(View view, int type) {
                            LogUtils.d("getChuanShanJia onAdClicked: 广告被点击");
                            if (click_fun != null) wv.evaluateJavascript(click_fun, null);
                        }

                        @Override
                        public void onAdShow(View view, int type) {
                            LogUtils.d("onAdShow: 广告展示");

                        }

                        @Override
                        public void onRenderFail(View view, String msg, int code) {
                            LogUtils.d("onRenderFail: " + msg + "," + code);
                            if (err_fun != null) wv.evaluateJavascript(err_fun, null);
                        }

                        @Override
                        public void onRenderSuccess(View view, float width, float height) {
                            LogUtils.d("getChuanShanJia onRenderSuccess: 广告渲染完成 ，" + width + "," + height);
                            if (display_fun != null) wv.evaluateJavascript(display_fun, null);
                            view.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    wv.post(() -> {
                                        wv.dispatchTouchEvent(event);
                                    });
                                    return false;
                                }
                            });
                            commonAddAddToViews(web_ad_infomation, view);
                        }
                    });
                    ad.render();
                }
            }
        });
    }

    private void getBaiduBanner(JSONObject web_ad_infomation) {
        LogUtils.d(TAG, "getBaiduBanner: " + web_ad_infomation.toJSONString());
        String ad_position_id = web_ad_infomation.getString("ad_position_id");
        String display_fun = web_ad_infomation.getString("display_fun");
        String click_fun = web_ad_infomation.getString("click_fun");
        String download_fun = web_ad_infomation.getString("download_fun");
        String err_fun = web_ad_infomation.getString("error_fun");

        AdView adView = new AdView(mContext, ad_position_id);
        adView.setListener(new AdViewListener() {
            @Override
            public void onAdReady(AdView adView) {
                LogUtils.d(TAG, "onAdReady: ");

            }

            @Override
            public void onAdShow(org.json.JSONObject jsonObject) {
                LogUtils.d(TAG, "onAdShow: " + jsonObject.toString());
            }

            @Override
            public void onAdClick(org.json.JSONObject jsonObject) {
                LogUtils.d(TAG, "onAdClick: " + jsonObject.toString());
            }

            @Override
            public void onAdFailed(String s) {
                LogUtils.d(TAG, "onAdFailed: " + s);
            }

            @Override
            public void onAdSwitch() {
                LogUtils.d(TAG, "onAdSwitch: ");
            }

            @Override
            public void onAdClose(org.json.JSONObject jsonObject) {
                LogUtils.d(TAG, "onAdClose: " + jsonObject.toString());
            }
        });
        commonAddAddToViews(web_ad_infomation, adView);
    }
    private void getKuaiShouBanner(JSONObject web_ad_infomation) {
        LogUtils.d(TAG, "getKuaiShouBanner: 暂无");
    }
    private void commonAddAddToViews(JSONObject web_ad_infomation, View view) {

        wv.evaluateJavascript("(function(){ return $('"+ web_ad_infomation.getString("ad_class_name") +"').length; })()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                if (value != null){
                    int check_has_adv_count = Integer.parseInt(value);
                    // 如果等于0，则有可能不需要渲染
                    if (check_has_adv_count>0){

                        runOnUiThread(()->{

                            JSONObject position = web_ad_infomation.getJSONObject("position");

                            int position_y = (int) (position.getFloat("top") * dm.scaledDensity);

                            LinearLayout linearLayout = new LinearLayout(mContext);
                            LinearLayout.LayoutParams linearLayout_param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                            linearLayout_param.topMargin = position_y;
                            linearLayout.setLayoutParams(linearLayout_param);
                            linearLayout.addView(view);

                            wv.evaluateJavascript("(function(){return localReadLib.getAdvStation();})()", new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String value) {
                                    LogUtils.d("onReceiveValue: 广告渲染完成,确认广告状态的返回值：" + value);
                                    if (value.equals("true")) {
                                        linearLayout.setVisibility(View.VISIBLE);
                                    } else {
                                        linearLayout.setVisibility(View.INVISIBLE);
                                    }

                                    tmp_views.addView(linearLayout);

                                    JSONObject ads_info = new JSONObject();
                                    ads_info.put("web_class_name", web_ad_infomation.getString("ad_class_name"));
                                    ads_info.put("ad_view", view);
                                    ads_info.put("ad_box_view", linearLayout);
                                    ads_info.put("order_id", web_ad_infomation.getString("order_id"));
                                    ads_info.put("extra_data", web_ad_infomation.getString("extra_data"));
                                    ads_list.add(ads_info);
                                    view.postDelayed(() -> {
                                        runOnUiThread(() -> {
                                            LogUtils.d("onRenderSuccess: 设置广告位高度" + view.getMeasuredHeight() / dm.scaledDensity);
                                            wv.evaluateJavascript("$('" + web_ad_infomation.getString("ad_class_name") + "').height(" + view.getMeasuredHeight() / dm.scaledDensity + ")", null);
                                            js.setScrollTop();
                                        });
                                    },200);

                                    runOnUiThread(() -> {
                                        js.setScrollTop();
                                    });
                                }
                            });



                        });

                    }
                }
            }
        });

    }
    private class JuDuoHuiWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            LogUtils.d(TAG, "onPageStarted: ");
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            LogUtils.d(TAG, "onPageFinished: ");
            if (url.contains("53kf.com")) {
                runOnUiThread(() -> {
                    String addString = "";
                    if (has_user) {
                        addString = "你好，我是咱们用户【" + CaiNiaoApplication.getUserInfoBean().user_detail.nickname + "，" + SPUtils.getStringData(mContext, "token", "") + "】，想咨询咱平台问题";
                    } else {
                        addString = "你好，我的IP地址是：" + ip + ",想咨询一下平台下载资源出错的问题";
                    }
                    wv.evaluateJavascript("(function(){ document.getElementById('textarea').value='" + addString + "，我正在输入中。。。';const event = document.createEvent('MouseEvents');event.initEvent('touchstart', true, false);document.getElementById('btnSend').dispatchEvent(event);document.getElementsByClassName('bottom-bar')[0].style.display='none'})()", null);
                });

            }

            js.setScrollTop();
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private class JuDuoHuiWebChromeClient extends WebChromeClient {
        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMessage = uploadMsg;
            openImageChooserActivity();
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            openImageChooserActivity();
        }

        // For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            openImageChooserActivity();
        }

        // For Android >= 5.0
        @Override
        public boolean onShowFileChooser(WebView webView,
                                         ValueCallback<Uri[]> filePathCallback,
                                         FileChooserParams fileChooserParams) {
            uploadMessageAboveL = filePathCallback;
            openImageChooserActivity();
            return true;
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            LogUtils.d(TAG, "onJsAlert: " + message);
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            LogUtils.d(TAG, "onJsConfirm: " + message);
            return super.onJsConfirm(view, url, message, result);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            LogUtils.d(TAG, "onJsPrompt: " + message);
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
            return super.onJsBeforeUnload(view, url, message, result);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            LogUtils.d(TAG, "onConsoleMessage: " + consoleMessage.messageLevel().name() + ":" + consoleMessage.message());
            return super.onConsoleMessage(consoleMessage);
        }
    }

    /**
     * 打开本地相册
     */
    private void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), REQUEST_FILE_CHOOSER_CODE);
    }

    public static class DialogJs {
        private CommonDialog dialog;

        public DialogJs(CommonDialog dg) {
            dialog = dg;
        }

        @JavascriptInterface
        public void close() {
            dialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FILE_CHOOSER_CODE) {
            if (null == mUploadMessage && null == uploadMessageAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }

        if (requestCode == REQUEST_SCAN_QR_CODE) {
            if (resultCode == 1000 || resultCode == RESULT_OK) {
                String result_text = data.getIntExtra("isfromdcim",0) == 0 ? data.getStringExtra(Intents.Scan.RESULT) : data.getStringExtra("text");
                String success_fun = scan_qrcode_config.getString("success");
                if (success_fun != null) {
                    wv.evaluateJavascript("(" + success_fun + ").call(this.vue,'" + result_text + "')", null);
                }
            } else {
                String error_fun = scan_qrcode_config.getString("error");
                if (error_fun != null) {
                    wv.evaluateJavascript("(" + error_fun + ").call(this.vue)", null);
                }
            }
        }

        // 激励视频广告回调
        if (requestCode == REQUEST_REWARD_ADV){
            String close_fun = data.getStringExtra("close_fun");
            // Log.d(TAG, "onActivityResult: " + close_fun);
            boolean option_result = resultCode == RESULT_OK;
            wv.evaluateJavascript("("+close_fun+").call(this.vue,"+option_result+")",null);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != REQUEST_FILE_CHOOSER_CODE || uploadMessageAboveL == null)
            return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{
                            Uri.parse(dataString)
                    };
            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }

    @Override
    public void onBackPressed() {
        LogUtils.d(TAG, "onBackPressed: ");
        if (js.listenAppStatus_back) {
            runOnUiThread(() -> {
                wv.evaluateJavascript(js.listenAppStatus_back_func.equals("") ? "OnStart()" : js.listenAppStatus_back_func, new JuDuoHuiValueCallBack());
            });
        } else {
            if (wv.canGoBack()) {
                runOnUiThread(() -> {
                    wv.goBack();
                });
            } else {
                finish();
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.d(TAG, "onStart: ");
        if (js.listenAppStatus_load) {
            runOnUiThread(() -> {
                wv.evaluateJavascript(js.listenAppStatus_load_func.equals("") ? "OnStart()" : js.listenAppStatus_load_func, new JuDuoHuiValueCallBack());
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d(TAG, "onResume: ");
        if (js.listenAppStatus_resume) {
            runOnUiThread(() -> {
                wv.evaluateJavascript(js.listenAppStatus_resume_func.equals("") ? "OnResume()" : js.listenAppStatus_resume_func, new JuDuoHuiValueCallBack());
                thirdIWeb.evaluateJavascript(js.listenAppStatus_resume_func.equals("") ? "OnResume()" : js.listenAppStatus_resume_func, new JuDuoHuiValueCallBack());
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (js.listenAppStatus_hide) {
            runOnUiThread(() -> {
                wv.evaluateJavascript(js.listenAppStatus_hide_func.equals("") ? "OnPause()" : js.listenAppStatus_hide_func, new JuDuoHuiValueCallBack());
                thirdIWeb.evaluateJavascript(js.listenAppStatus_hide_func.equals("") ? "OnPause()" : js.listenAppStatus_hide_func, new JuDuoHuiValueCallBack());
            });

        }
    }

    @Override
    protected void onDestroy() {
        wv.destroy();
        finish();
        super.onDestroy();
    }

    class JuDuoHuiValueCallBack implements ValueCallback<String> {

        @Override
        public void onReceiveValue(String value) {
            LogUtils.d(TAG, "onReceiveValue: " + value);
        }
    }

}