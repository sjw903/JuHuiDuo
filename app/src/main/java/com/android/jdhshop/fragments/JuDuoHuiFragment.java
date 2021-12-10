package com.android.jdhshop.fragments;

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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.MyScanActivity;
import com.android.jdhshop.advistion.JuDuoHuiAdvertisement;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.juduohui.AssetTools;
import com.android.jdhshop.juduohui.CommonDialog;
import com.android.jdhshop.juduohui.JuDuoHuiJavaScript;
import com.android.jdhshop.juduohui.JuduohuiRewardActivity;
import com.android.jdhshop.utils.AesUtils;
import com.android.jdhshop.utils.UIUtils;
import com.baidu.mobads.sdk.api.AdView;
import com.baidu.mobads.sdk.api.AdViewListener;
import com.baidu.mobads.sdk.api.RewardVideoAd;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.king.zxing.Intents;
import com.kwad.sdk.api.KsAdSDK;
import com.kwad.sdk.api.KsFeedAd;
import com.kwad.sdk.api.KsLoadManager;
import com.kwad.sdk.api.KsRewardVideoAd;
import com.kwad.sdk.api.KsScene;
import com.kwad.sdk.api.KsVideoPlayConfig;
import com.loopj.android.http.RequestParams;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.ads.rewardvideo.RewardVideoADListener;
import com.qq.e.ads.rewardvideo.ServerSideVerificationOptions;
import com.qq.e.comm.util.AdError;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

//import com.tencent.smtt.sdk.CookieSyncManager;


/**
 * <pre>
 *     author : Administrator
 *     e-mail : szp
 *     time   : 2020/05/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class JuDuoHuiFragment extends BaseLazyFragment {
    String TAG = "JuDuoHuiFragment";
    @BindView(R.id.wv)
    WebView wv;
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
    @BindView(R.id.third_iweb) WebView thirdIWeb;

    JuDuoHuiJavaScript js = null;
    private Activity mContext = null;
    private static final int REQUEST_FILE_CHOOSER_CODE = 102;
    private static final int REQUEST_SCAN_QR_CODE = 105;
    private static final int REQUEST_REWARD_ADV = 2000;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    public List<JSONObject> ads_list = new ArrayList<>();

    private View my_ads = null;
    private RelativeLayout re = null;
    private RelativeLayout.LayoutParams lm = null;
    private WindowManager wm = null;
    private DisplayMetrics dm = null;
    private ViewGroup.LayoutParams scroll_view_lm = null;
    private JSONObject scan_qrcode_config = null;
    private String load_file_name = "index.html";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ju_duo_hui_webview, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility"})
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        LogUtils.d("初始化 WEB_APP 存储");
        // 这里路径是 /sdcard/Android/data/com.android.jdhshop/files/web_app
        File check_file = mContext.getExternalFilesDir("web_app");
        // 这里路径是 /sdcard/Android/data/com.android.jdhshop/files
        String root_path = mContext.getExternalFilesDir(null).getPath().replace("/storage/emulated/0", "/sdcard");
        // 路径不存在 或者 路径下为空 或者 非首次安装
        if (!check_file.exists() || check_file.list().length <= 0) {
            LogUtils.d("更新");
            AssetTools.releaseAssets(mContext, "web_app", root_path);
            LogUtils.d("更新结束");
        }
        else{
            File check_version_file = new File(check_file,"version.txt");
            // 如果version文件的最后修改时间 小于 安装包的最后安装时间则更新
            LogUtils.d("onActivityCreated: " + check_version_file.lastModified()+"," + AssetTools.getPackageLastUpdateTime(mContext));
            if (check_version_file.lastModified() < AssetTools.getPackageLastUpdateTime(mContext)){
                AssetTools.releaseAssets(mContext, "web_app", root_path);
            }
            check_version_file = null;
        }
        LogUtils.d("初始化结束");

        LogUtils.d("开始进行版本检测");
        try {
           AssetTools.checkUpdate(mContext);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        LogUtils.d("结束进行版本检测");

        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

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
                        scan_intent.putExtra("set_title",config.getString("title"));
                        startActivityForResult(scan_intent, REQUEST_SCAN_QR_CODE);
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        showToast("拒绝权限不能使用扫一扫");
                    }
                });
            }

            @Override
            public void alert(JSONObject config) {
                /**
                 * 优先显示以 url为主体的
                 */

                getActivity().runOnUiThread(()->{
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
                        CommonDialog dialog = new CommonDialog(getActivity());
                        // 底部按钮配置段
                        if (submit_config==null){
                            dialog.getSubmitButton().setVisibility(View.GONE);
                        }else{
                            dialog.setSubmit(submit_config.getString("text"),submit_config.getBoolean("active"));
                        }
                        if (cancel_config==null){
                            dialog.getCancelButton().setVisibility(View.GONE);
                        }else{
                            dialog.setCancelButton(cancel_config.getString("text"),cancel_config.getBoolean("active"));
                        }

                        // 内容配置段,如果是 网址，则不显示下部的按钮
                        if (alert_url!=null){
                            WebView inner_wv = new WebView(getActivity()){
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

                            CookieSyncManager.createInstance(getActivity());
                            CookieManager instance = CookieManager.getInstance();
                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) instance.setAcceptThirdPartyCookies(inner_wv,true);
                            instance.setAcceptCookie(true);
                            CookieSyncManager.getInstance().sync();

                            WebSettings inner_webSetting =  inner_wv.getSettings();
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

                            inner_wv.addJavascriptInterface(js,"juduohui");
                            inner_wv.addJavascriptInterface(new DialogJs(dialog),"dialog");
                            dialog.getButtonContent().setVisibility(View.GONE);
                            dialog.setShowCloseButton(false);
                            LinearLayout message_content = dialog.getMessageContent();
                            message_content.removeAllViews();
                            message_content.addView(inner_wv,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                            String inner_url = StringUtils.startsWith(alert_url,"http") ? alert_url : root_path + "/web_app/" + alert_url;
                            inner_wv.loadUrl(inner_url);

                        }
                        else{
                            if (alert_title!= null || "".equals(alert_title)) dialog.setTitle(alert_title);
                            dialog.setMessage(alert_message);
                        }


                        dialog.setListener(new CommonDialog.CommonDialogListener() {
                            @Override
                            public void OnSubmit(AlertDialog dialog) {
                                if (submit_config!=null){
                                    wv.evaluateJavascript("("+submit_config.getString("click")+").call(this.vue)",null);
                                }
                                dialog.dismiss();
                            }

                            @Override
                            public void OnCancel(AlertDialog dialog) {
                                if (cancel_config!=null){
                                    wv.evaluateJavascript("("+cancel_config.getString("click")+").call(this.vue)",null);
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
                                if (close_fun!=null){
                                    wv.evaluateJavascript("("+close_fun+").call(this.vue)",null);
                                }
                            }
                        });

                        dialog.setCanceledOnTouchOutside(cancel_out_side);
                        dialog.show();

                    }
                    catch (Exception e){
                        showToasts("弹窗初始化失败");
                        e.printStackTrace();
                    }
                });




            }

            @Override
            public void sendParamToWebView(JSONObject web_config) {
                String type = web_config.getString("type");
                String callback = web_config.getString("callback");

                WebView target_view = null;
                if (type.equals("main")){
                    target_view = wv;
                }
                else{
                    target_view = thirdIWeb;
                }

                target_view.evaluateJavascript("(" + callback + ").call(this.vue)", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        LogUtils.d(TAG, "onReceiveValue: " + value);
                    }
                });
            }

            @Override
            public void renderWebView(JSONObject web_config) {
                boolean is_display = web_config.getString("display").equals("yes");
                int width = web_config.getIntValue("width");
                int height = web_config.getInteger("height");
                int top = web_config.getIntValue("top");
                int bottom = web_config.getInteger("bottom");
            }

            @Override
            public void removeAllAdv() {
                // Log.d(TAG, "removeAllAdv: ");
                getActivity().runOnUiThread(() -> {
                    ads_list = new ArrayList<>();
                    tmp_views.removeAllViews();
                });
            }

            @Override
            public void setCircleTimer(JSONObject circle_config) {
            }

            @Override
            public void startCircleAni() {
            }

            @Override
            public void setNativeTitleBar(JSONObject native_title_bar_setting) {
            }

            @Override
            public void setAdvStation(String css_class, boolean station) {
                for (int i = 0; i < ads_list.size(); i++) {
                    JSONObject item = ads_list.get(i);
                    if (css_class.equals("") || item.getString("web_class_name").equals(css_class)) {
                        getActivity().runOnUiThread(() -> {
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
                        getActivity().runOnUiThread(() -> {
                            LinearLayout ad_box_view = item.getObject("ad_box_view", LinearLayout.class);
                            ads_list.remove(item);
                            tmp_views.removeView(ad_box_view);
                        });
                    }
                }
            }

            @Override
            public void setScrollTop() {
                if (getActivity()==null) return;
                getActivity().runOnUiThread(() -> {
//                    top_adv_scroll.scrollTo(0,scrollTopValue);
                    String adclass_id = "";
                    for (JSONObject item : ads_list) {
                        String tmp = item.getString("web_class_name").startsWith(".") ? item.getString("web_class_name") : "."+item.getString("web_class_name");
                        adclass_id = adclass_id + "'" + tmp + "',";
                    }
                    String run_string = "(function(a){var t=[];for(var i=0;i<a.length;i++){var w=$(a[i]).position(); if (w == undefined){ continue; } w.cls=a[i];t.push(w)}return t})([" + adclass_id + "])";
                    LogUtils.logd("setScrollTop: " + run_string);
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

                LogUtils.logd(org_config);
                if (type == 0 || type == 2) removeAdv(ads_class);
                if (getActivity()==null) return;
                Activity act = getActivity();
                act.runOnUiThread(() -> {
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

                            infomation.put("display_fun", display_fun != null ? "("+display_fun.replaceAll("\\t","\t").replaceAll("\\n","\n")+").call(this.vue)" : null);
                            infomation.put("click_fun", click_fun != null ? "("+click_fun+").call(this.vue)" : null);
                            infomation.put("download_fun", download_fun != null ? "("+download_fun+").call(this.vue)" : null);
                            infomation.put("error_fun", error_fun != null ? "("+error_fun+").call(this.vue)" : null);
                            infomation.put("close_fun", close_fun != null ? "(" + close_fun +").call(this.vue)":null);

                            // adv_position_id 1 优量汇，2，穿山甲

                            if (type == 0) {

                                String ad_position_id = infomation.getString("ad_position_id");
                                String display_fun = infomation.getString("display_fun");
                                String click_fun = infomation.getString("click_fun");
                                String download_fun = infomation.getString("download_fun");
                                String close_fun = infomation.getString("close_fun");
                                String err_fun = infomation.getString("error_fun");
                                String channel = adv_config.getString("channel");
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

                                JuDuoHuiAdvertisement juDuoHuiAdvertisement = new JuDuoHuiAdvertisement(getActivity(),null);
                                juDuoHuiAdvertisement.setInfomationAdListen(new JuDuoHuiAdvertisement.InfomationAdListen() {
                                    int error_count = 0;
                                    int max_retry = 5;
                                    @Override
                                    public void click(View v) {
                                        // Log.d(TAG, "click: ");
                                        if (getActivity() == null) return;
                                        getActivity().runOnUiThread(()-> {
                                            if (click_fun != null)
                                                wv.evaluateJavascript(click_fun, null);
                                        });
                                    }

                                    @Override
                                    public void dislike() {
                                        // Log.d(TAG, "dislike: ");
                                        if (getActivity() == null) return;
                                        getActivity().runOnUiThread(()-> {
                                            if (close_fun != null)
                                                wv.evaluateJavascript(close_fun, null);
                                        });
                                    }

                                    @Override
                                    public void display(View v, String position,JSONObject config) {
                                        // Log.d(TAG, "display: ");
                                        if (getActivity() == null) return;
                                        getActivity().runOnUiThread(()->{
                                            commonAddAddToViews(infomation,v);
                                        });
                                    }

                                    @Override
                                    public void displayed() {
                                        // Log.d(TAG, "displayed: ");
                                        if (getActivity() == null) return;
                                        getActivity().runOnUiThread(()-> {
                                            if (display_fun != null)
                                                wv.evaluateJavascript(display_fun, null);
                                        });
                                    }

                                    @Override
                                    public void close() {
                                        // Log.d(TAG, "close: ");
                                        if (getActivity() == null) return;
                                        getActivity().runOnUiThread(()-> {
                                            if (close_fun != null)
                                                wv.evaluateJavascript(close_fun, null);
                                        });
                                    }

                                    @Override
                                    public void error(JSONObject error) {
                                        // Log.d(TAG, "error: " + error_count + "," + max_retry);
                                        // Log.d(TAG, "error: " + error.toJSONString());
                                        if (getActivity() == null) return;
                                        if (error_count<max_retry) {
                                            error_count = error_count +1;
                                            getActivity().runOnUiThread(() -> {
                                                juDuoHuiAdvertisement.getInfomationAdv(adv_cfg, "web_get_vivo");
                                            });
                                        }
                                        else{
                                            getActivity().runOnUiThread(()-> {
                                                if (err_fun != null)
                                                    wv.evaluateJavascript(err_fun, null);
                                            });
                                        }
                                    }
                                });
                                juDuoHuiAdvertisement.getInfomationAdv(adv_cfg,"web_get_vivo",dm.widthPixels,0);

//                                if (channel.equals("1")) {
//                                    getYouLiangHui(infomation);
//                                } else if (channel.equals("2")) {
//                                    getChuanShanJia(infomation);
//                                } else if (channel.equals("3")) {
//                                    getKuaiShou(infomation);
//                                } else if (channel.equals("4")) {
//                                    getBaidu(infomation);
//                                }
                            }
                            else if (type == 2)
                            {
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
                Intent intent = new Intent(getActivity(), JuduohuiRewardActivity.class);
                intent.putExtra("adv_config",adv_config);
                // Log.d(TAG, "getRewardVideoAd: " + close_fun);
                intent.putExtra("close_fun",close_fun);
                startActivityForResult(intent,REQUEST_REWARD_ADV);
            }

            @Override
            public void showToasts(String msg) {
                getActivity().runOnUiThread(() -> {
                    showToast(msg);
                });
            }

            @Override
            public void press() {
                getActivity().runOnUiThread(()->{
                    if (wv.canGoBack()) {
                        wv.goBack();
                    } else {
                        LogUtils.d("press: 按了返回键");
                        getActivity().finish();
                    }
                });
            }

            @Override
            public void setStatusBarColor(String color_string) {
                getActivity().runOnUiThread(() -> {
                    setStatusBar(Color.parseColor(color_string));
                });
            }

            @Override
            public void reloadWindow() {
                getActivity().runOnUiThread(() -> {
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

        String sd = null;
        try {
            sd = URLEncoder.encode(AesUtils.encrypt("2"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        LogUtils.d("onCreate: " + sd);
//        wv = findViewById(R.id.wv);

        CookieSyncManager.createInstance(getContext());
        CookieManager instance = CookieManager.getInstance();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) instance.setAcceptThirdPartyCookies(wv,true);
        instance.setAcceptCookie(true);
        CookieSyncManager.getInstance().sync();


        wv.setWebViewClient(new JuDuoHuiWebClient());
        wv.setWebChromeClient(new JuDuoHuiWebChromeClient());

        WebSettings webSetting = wv.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setAllowUniversalAccessFromFileURLs(true);
        webSetting.setAllowFileAccessFromFileURLs(true);

        webSetting.setAllowContentAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(false);
        webSetting.setTextZoom(100);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setUseWideViewPort(true);
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

//        Intent trans_intent =  getIntent();
        String load_url = "file://" + root_path + "/web_app/"+load_file_name;//"file:///android_asset/web_app/art_read.html";
//        if (trans_intent.getStringExtra("url")!= null && !"".equals(trans_intent.getStringExtra("url"))){
//            load_url = trans_intent.getStringExtra("url");
//        }

//        wv.loadUrl("file:///android_asset/web_app/index.html");
        LogUtils.d("加载路径: " + load_url);
        thirdIWeb.setVisibility(View.GONE);
        wv.loadUrl(load_url);
        top_adv_scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                wv.dispatchTouchEvent(event);
                return true;
            }
        });
    }

    @Override
    public void setArguments(Bundle args) {
        LogUtils.d("setArguments: ");
        try{
            int click_check = args.getInt("click_check",0);
            if (click_check == 1){
                int is_reload = args.getInt("reload",0);
                if (is_reload == 1 && wv!=null && js.listenAppStatus_resume){
                    wv.evaluateJavascript(js.listenAppStatus_resume_func,null);
                }
            }
            else {
                load_file_name = args.getString("load_file", "index.html");
            }
        }
        catch (Exception e){  e.printStackTrace(); }
        super.setArguments(args);
    }

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
                if (item.getString("web_class_name").equals(web_ad_infomation.getJSONObject(j).getString("cls"))) {
                    int finalJ = j;
                    getActivity().runOnUiThread(() -> {
                        LogUtils.d("setAdvInfo: " + item.getString("web_class_name"));
                        LinearLayout ad_box_view = item.getObject("ad_box_view", LinearLayout.class);
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

    private void getYouLiangHui(JSONObject web_ad_infomation) {
        String adv_position_id = web_ad_infomation.getString("ad_position_id");
        String display_fun = web_ad_infomation.getString("display_fun");
        String click_fun = web_ad_infomation.getString("click_fun");
        String download_fun = web_ad_infomation.getString("download_fun");
        String err_fun = web_ad_infomation.getString("error_fun");
        String ad_class_name = web_ad_infomation.getString("ad_class_name");

        ADSize adSize = new ADSize(ADSize.FULL_WIDTH, (int) (100 * dm.scaledDensity));
        LogUtils.d("getYouLiangHui: " + adSize.getWidth() + "," + adSize.getHeight());
        NativeExpressAD ad = new NativeExpressAD(mContext, adSize, adv_position_id, new NativeExpressAD.NativeExpressADListener() {
            @Override
            public void onADLoaded(List<NativeExpressADView> list) {
                LogUtils.d("onADLoaded: ");

                for (NativeExpressADView ad_item : list) {

                    ad_item.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            wv.post(() -> {
                                wv.dispatchTouchEvent(event);
                            });
                            return false;
                        }
                    });
                    commonAddAddToViews(web_ad_infomation,ad_item);
                    ad_item.render();

                }


            }

            @Override
            public void onRenderFail(NativeExpressADView nativeExpressADView) {
                LogUtils.d("onRenderFail: ");
            }

            @Override
            public void onRenderSuccess(NativeExpressADView nativeExpressADView) {
                getActivity().runOnUiThread(() -> {
                    nativeExpressADView.post(new Runnable() {
                        @Override
                        public void run() {
                            LogUtils.d("getYouLiangHui onRenderSuccess: 设置广告位高度 4444" + nativeExpressADView.getMeasuredHeight());
                            getActivity().runOnUiThread(() -> {
                                wv.evaluateJavascript("$('" + web_ad_infomation.getString("ad_class_name") + "').height(" + nativeExpressADView.getMeasuredHeight() / dm.scaledDensity + ")", new JuDuoHuiValueCallBack());
                                js.setScrollTop();
                                if (display_fun !=null) wv.evaluateJavascript(display_fun,null);
                            });
                        }
                    });

                });
            }

            @Override
            public void onADExposure(NativeExpressADView nativeExpressADView) {
                LogUtils.d("onADExposure: ");
            }

            @Override
            public void onADClicked(NativeExpressADView nativeExpressADView) {
                LogUtils.d("onADClicked: ");
                if (click_fun!=null) wv.evaluateJavascript(click_fun,null);
            }

            @Override
            public void onADClosed(NativeExpressADView nativeExpressADView) {
                LogUtils.d("onADClosed: ");
                nativeExpressADView.destroy();
                wv.evaluateJavascript("(function(){ $('"+ad_class_name+"').hide(); }).call(this.vue)",null);
            }

            @Override
            public void onADLeftApplication(NativeExpressADView nativeExpressADView) {
                LogUtils.d("onADLeftApplication: ");
            }

            @Override
            public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {
                LogUtils.d("onADOpenOverlay: ");
            }

            @Override
            public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {
                LogUtils.d("onADCloseOverlay: ");
                LogUtils.d(TAG, "getKuaiShou onDislikeClicked: ");
//                        if (close_fun!=null) wv.evaluateJavascript(close_fun,null);

            }

            @Override
            public void onNoAD(AdError adError) {
                LogUtils.d("onNoAD: 665 " + adError.getErrorMsg());
                getActivity().runOnUiThread(() -> {
                    if (err_fun!=null) wv.evaluateJavascript(err_fun.replace("ARGUMENT","'"+web_ad_infomation.toJSONString()+"'"),null);
                    else wv.evaluateJavascript("$('" + web_ad_infomation.getString("ad_class_name") + "').hide()", null);
                    js.setScrollTop();
                });
            }
        });
        VideoOption.Builder videoOption = new VideoOption.Builder();
        videoOption.setAutoPlayMuted(true);
        videoOption.setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI);
        ad.setVideoOption(videoOption.build());
        ad.loadAD(1);
    }
    UnifiedInterstitialAD banner = null;
    private void getYouLiangHuiBanner(JSONObject web_ad_infomation){

        String adv_position_id = web_ad_infomation.getString("ad_position_id");
        String display_fun = web_ad_infomation.getString("display_fun");
        String click_fun = web_ad_infomation.getString("click_fun");
        String download_fun = web_ad_infomation.getString("download_fun");
        String err_fun = web_ad_infomation.getString("error_fun");

        banner = new UnifiedInterstitialAD(getActivity(), adv_position_id, new UnifiedInterstitialADListener() {
            @Override
            public void onADReceive() {
                LogUtils.d(TAG, "onADReceive: ");
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

    /**
     * 获取广告位
     *
     * @param web_ad_infomation web页面中的广告位信息 {'ad_class_name':'.cls','position':{'top':300,'left':300 } }
     */
    private void getChuanShanJia(JSONObject web_ad_infomation) {

        LogUtils.d("getChuanShanJia: " + web_ad_infomation.toJSONString());

        String ad_position_id = web_ad_infomation.getString("ad_position_id");
        String display_fun = web_ad_infomation.getString("display_fun");
        String click_fun = web_ad_infomation.getString("click_fun");
        String download_fun = web_ad_infomation.getString("download_fun");
        String err_fun = web_ad_infomation.getString("error_fun");

        // 穿山甲
        TTAdNative mTTAdNative = TTAdSdk.getAdManager().createAdNative(getContext());
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(ad_position_id) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(500, 0) //期望模板广告view的size,单位dp
                .build();


        mTTAdNative.loadNativeExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            //广告请求失败
            @Override
            public void onError(int code, String message) {
                LogUtils.d("onError475: " + message);
                if (err_fun!=null) wv.evaluateJavascript(err_fun,null);
                getActivity().runOnUiThread(() -> {
                    LogUtils.d("getChuanShanJia onError: 设置广告位高度" + height);
                    wv.evaluateJavascript("$('" + web_ad_infomation.getString("ad_class_name") + "').height(0)", new JuDuoHuiValueCallBack());
                    js.setScrollTop();
                });
            }

            //广告请求成功
            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                LogUtils.d("onNativeExpressAdLoad: ");
                for (TTNativeExpressAd ad : ads) {
                    TTNativeExpressAd adTmp = ad;
                    adTmp.setDislikeCallback(getActivity(), new TTAdDislike.DislikeInteractionCallback() {
                        @Override
                        public void onShow() {

                        }

                        @Override
                        public void onSelected(int i, String s, boolean b) {

                        }

                        @Override
                        public void onCancel() {
                            ad.destroy();
                            wv.evaluateJavascript("$('" + web_ad_infomation.getString("ad_class_name") + "').hide()", null);
                            js.setScrollTop();
                        }
                    });
                    adTmp.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
                        @Override
                        public void onAdClicked(View view, int type) {
                            LogUtils.d("getChuanShanJia onAdClicked: 广告被点击");
                            if (click_fun!=null) wv.evaluateJavascript(click_fun,null);
                        }

                        @Override
                        public void onAdShow(View view, int type) {
                            LogUtils.d("onAdShow: 广告展示");

                        }

                        @Override
                        public void onRenderFail(View view, String msg, int code) {
                            LogUtils.d("onRenderFail: " + msg + "," + code);
                            if (err_fun!=null) wv.evaluateJavascript(err_fun,null);
                        }

                        @Override
                        public void onRenderSuccess(View view, float width, float height) {
                            LogUtils.d("getChuanShanJia onRenderSuccess: 广告渲染完成 ，" + width + "," + height);
                            if (display_fun!=null) wv.evaluateJavascript(display_fun,null);
                            view.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    wv.post(() -> {
                                        wv.dispatchTouchEvent(event);
                                    });
                                    return false;
                                }
                            });
                            commonAddAddToViews(web_ad_infomation,view);
                        }
                    });
                    ad.render();
                }
            }
        });
    }
    private void getChuanShanJiaBanner(JSONObject web_ad_infomation){
        LogUtils.d("getChuanShanJiaBanner: " + web_ad_infomation.toJSONString());

        String ad_position_id = web_ad_infomation.getString("ad_position_id");
        String display_fun = web_ad_infomation.getString("display_fun");
        String click_fun = web_ad_infomation.getString("click_fun");
        String download_fun = web_ad_infomation.getString("download_fun");
        String err_fun = web_ad_infomation.getString("error_fun");

        // 穿山甲
        TTAdNative mTTAdNative = TTAdSdk.getAdManager().createAdNative(getContext());
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
                if (err_fun!=null) wv.evaluateJavascript(err_fun,null);
                getActivity().runOnUiThread(() -> {
                    LogUtils.d("getChuanShanJia onError: 设置广告位高度" + height);
                    wv.evaluateJavascript("$('" + web_ad_infomation.getString("ad_class_name") + "').height(0)", new JuDuoHuiValueCallBack());
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
                            if (click_fun!=null) wv.evaluateJavascript(click_fun,null);
                        }

                        @Override
                        public void onAdShow(View view, int type) {
                            LogUtils.d("onAdShow: 广告展示");

                        }

                        @Override
                        public void onRenderFail(View view, String msg, int code) {
                            LogUtils.d("onRenderFail: " + msg + "," + code);
                            if (err_fun!=null) wv.evaluateJavascript(err_fun,null);
                        }

                        @Override
                        public void onRenderSuccess(View view, float width, float height) {
                            LogUtils.d("getChuanShanJia onRenderSuccess: 广告渲染完成 ，" + width + "," + height);
                            if (display_fun!=null) wv.evaluateJavascript(display_fun,null);
                            view.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    wv.post(() -> {
                                        wv.dispatchTouchEvent(event);
                                    });
                                    return false;
                                }
                            });
                            commonAddAddToViews(web_ad_infomation,view);
                        }
                    });
                    ad.render();
                }
            }
        });
    }

    private void getBaiduBanner(JSONObject web_ad_infomation){
        String ad_position_id = web_ad_infomation.getString("ad_position_id");
        String display_fun = web_ad_infomation.getString("display_fun");
        String click_fun = web_ad_infomation.getString("click_fun");
        String download_fun = web_ad_infomation.getString("download_fun");
        String err_fun = web_ad_infomation.getString("error_fun");

        AdView adView = new AdView(mContext,ad_position_id);
        adView.setListener(new AdViewListener() {
            @Override
            public void onAdReady(AdView adView) {
                LogUtils.d(TAG, "onAdReady: ");
                commonAddAddToViews(web_ad_infomation,adView);
            }

            @Override
            public void onAdShow(org.json.JSONObject jsonObject) {
                LogUtils.d(TAG, "onAdShow: "+jsonObject.toString());
            }

            @Override
            public void onAdClick(org.json.JSONObject jsonObject) {
                LogUtils.d(TAG, "onAdClick: "+jsonObject.toString());
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
    }

    private void getBaidu(JSONObject web_ad_infomation){
        String ad_class_name = web_ad_infomation.getString("ad_class_name");
        wv.evaluateJavascript("(function(){ $('"+ad_class_name+"').hide(); }).call(this.vue)",null);
    }

    private void getKuaiShou(JSONObject web_ad_infomation){
        long ad_position_id = web_ad_infomation.getLongValue("ad_position_id");
        String display_fun = web_ad_infomation.getString("display_fun");
        String click_fun = web_ad_infomation.getString("click_fun");
        String download_fun = web_ad_infomation.getString("download_fun");
        String err_fun = web_ad_infomation.getString("error_fun");
        String close_fun = web_ad_infomation.getString("close_fun");
        String ad_class_name = web_ad_infomation.getString("ad_class_name");

        KsLoadManager ksLoadManager = KsAdSDK.getLoadManager();
        KsScene ksScene = new KsScene.Builder(ad_position_id).adNum(1).build();
        ksLoadManager.loadConfigFeedAd(ksScene, new KsLoadManager.FeedAdListener() {
            @Override
            public void onError(int i, String s) {
                if (err_fun!=null) wv.evaluateJavascript(err_fun,null);
            }

            @Override
            public void onFeedAdLoad(@Nullable List<KsFeedAd> list) {
                KsFeedAd ksFeedAd = list.get(0);
                ksFeedAd.setAdInteractionListener(new KsFeedAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked() {
                        if (click_fun!=null) wv.evaluateJavascript(click_fun,null);
                    }

                    @Override
                    public void onAdShow() {
                        if (display_fun!=null) wv.evaluateJavascript(display_fun,null);
                    }

                    @Override
                    public void onDislikeClicked() {
                        LogUtils.d(TAG, "getKuaiShou onDislikeClicked: ");
//                        if (close_fun!=null) wv.evaluateJavascript(close_fun,null);
                        wv.evaluateJavascript("(function(){ $('"+ad_class_name+"').hide(); }).call(this.vue)",null);
                    }
                });
                View ad_view = ksFeedAd.getFeedView(mContext);
                commonAddAddToViews(web_ad_infomation,ad_view);
            }
        });
    }
    private void getKuaiShouBanner(JSONObject web_ad_infomation){
        LogUtils.d(TAG, "getKuaiShouBanner: 暂无");
    }
    private void commonAddAddToViews(JSONObject web_ad_infomation,View view){

        Activity sActivity = getActivity();

        if (sActivity == null) return;

        wv.evaluateJavascript("(function(){ return $('"+ web_ad_infomation.getString("ad_class_name") +"').length; })()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                if (value != null){
                    int check_has_adv_count = Integer.parseInt(value);
                    // 如果等于0，则当前广告位已不存在 不需要渲染
                    if (check_has_adv_count>0){

                        sActivity.runOnUiThread(()->{

                            JSONObject position = web_ad_infomation.getJSONObject("position");

                            int position_y = (int) (position.getFloat("top") * dm.scaledDensity);
                            int position_x = (int) (position.getFloat("left")*dm.scaledDensity);
                            int position_w = (int) (position.getFloat("w")*dm.scaledDensity);

                            LinearLayout linearLayout = new LinearLayout(mContext);
                            LinearLayout.LayoutParams linearLayout_param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            linearLayout_param.width = position_w;
                            linearLayout_param.topMargin = position_y;
                            // Log.d(TAG, "onReceiveValue: position_x = " + position_x);
                            linearLayout_param.leftMargin = position_x;
                            linearLayout.setLayoutParams(linearLayout_param);
                            linearLayout.addView(view);
                            wv.post(() -> {
                                wv.evaluateJavascript("(function(){return this.localReadLib.getAdvStation();})()", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String value) {
                                        LogUtils.d("onReceiveValue: 广告渲染完成,确认广告状态的返回值：" + value);
                                        if (value.equals("true")) {
                                            linearLayout.setVisibility(View.VISIBLE);
                                        } else {
                                            linearLayout.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                });
                            });
                            tmp_views.addView(linearLayout);

                            JSONObject ads_info = new JSONObject();
                            ads_info.put("web_class_name", web_ad_infomation.getString("ad_class_name"));
                            ads_info.put("ad_view", view);
                            ads_info.put("ad_box_view", linearLayout);
                            ads_info.put("order_id", web_ad_infomation.getString("order_id"));
                            ads_info.put("extra_data", web_ad_infomation.getString("extra_data"));
                            ads_list.add(ads_info);
                            view.postDelayed(() -> {
                                sActivity.runOnUiThread(() -> {
                                    LogUtils.d("onRenderSuccess: 设置广告位高度" + view.getMeasuredHeight() / dm.scaledDensity);
                                    wv.evaluateJavascript("$('" + web_ad_infomation.getString("ad_class_name") + "').height(" + view.getMeasuredHeight() / dm.scaledDensity + ")", null);
                                    js.setScrollTop();
                                });
                            },200);

                            sActivity.runOnUiThread(() -> {
                                js.setScrollTop();
                            });

                        });

                    }
                }
            }
        });

//        JSONObject position = web_ad_infomation.getJSONObject("position");
//
//        int position_y = (int) (position.getFloat("top") * dm.scaledDensity);
//
//
//        LinearLayout linearLayout = new LinearLayout(mContext);
//        LinearLayout.LayoutParams linearLayout_param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        linearLayout_param.topMargin = position_y;
//        linearLayout.setLayoutParams(linearLayout_param);
//        linearLayout.addView(view);
//        wv.post(() -> {
//            wv.evaluateJavascript("(function(){return this.localReadLib.getAdvStation();})()", new ValueCallback<String>() {
//                @Override
//                public void onReceiveValue(String value) {
//                    LogUtils.d("onReceiveValue: 广告渲染完成,确认广告状态的返回值：" + value);
//                    if (value.equals("true")) {
//                        linearLayout.setVisibility(View.VISIBLE);
//                    } else {
//                        linearLayout.setVisibility(View.INVISIBLE);
//                    }
//                }
//            });
//        });
//        tmp_views.addView(linearLayout);
//
//        JSONObject ads_info = new JSONObject();
//        ads_info.put("web_class_name", web_ad_infomation.getString("ad_class_name"));
//        ads_info.put("ad_view", view);
//        ads_info.put("ad_box_view", linearLayout);
//        ads_info.put("order_id",web_ad_infomation.getString("order_id"));
//        ads_info.put("extra_data",web_ad_infomation.getString("extra_data"));
//        ads_list.add(ads_info);
//        view.postDelayed(()->{
//            if (getActivity()!=null) {
//                getActivity().runOnUiThread(() -> {
//                    // Log.d(TAG, "commonAddAddToViews: " + view.getHeight() + " = " + view.getMeasuredHeight() + "," + view.getMinimumHeight());
//                    LogUtils.d("onRenderSuccess: 设置广告位高度" + view.getMeasuredHeight() / dm.scaledDensity);
//                    wv.evaluateJavascript("$('" + web_ad_infomation.getString("ad_class_name") + "').height(" + view.getMeasuredHeight() / dm.scaledDensity + ")", new JuDuoHuiValueCallBack());
//                    js.setScrollTop();
//                });
//            }
//        },200);

    }

    private com.qq.e.ads.rewardvideo.RewardVideoAD rewardVideoAD = null;

    /***
     * 激励视频广告 - 优量汇
     * @param ad_position_code 广告位ID
     * @param orderNo 订单号
     * @param extra 扩展数据
     * @param callback_fun JS回调
     */
    private void getRewardVideoAdv_YouLiangHui(String ad_position_code, String orderNo, String extra, String callback_fun) {
        LogUtils.d("getRewardVideoAdv_YouLiangHui: " + ad_position_code + ","+ orderNo+","+ extra +"," + callback_fun);
        JSONObject result = new JSONObject();
        rewardVideoAD = new com.qq.e.ads.rewardvideo.RewardVideoAD(mContext, ad_position_code, new RewardVideoADListener() {
            @Override
            public void onADLoad() {
                LogUtils.d("onADLoad: ");
            }

            @Override
            public void onVideoCached() {
                LogUtils.d("onVideoCached: ");
                rewardVideoAD.showAD(getActivity());
            }

            @Override
            public void onADShow() {
                LogUtils.d("onADShow: ");
            }

            @Override
            public void onADExpose() {
                LogUtils.d("onADExpose: ");

            }

            @Override
            public void onReward(Map<String, Object> map) {
                LogUtils.d("onReward: ");
                RequestParams self_req = new RequestParams();
                self_req.put("orderNo",orderNo);
                result.put("orderNo",orderNo);
                for (Map.Entry<String, Object> s : map.entrySet()) {
                    LogUtils.d("onReward: " + s.getKey() + "," + s.getValue().toString());
                    self_req.put(s.getKey(),s.getValue().toString());
                    result.put(s.getKey(),s.getValue().toString());
                }
                result.put("code",0);
                result.put("rewardVerify", true);

//                LogUtils.d("优量汇广告SDK回调onReward: " +self_req.toString());
//                new Timer().schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        getActivity().runOnUiThread(()->{
//                            HttpUtils.post(Constants.APP_IP +"/api/UserHuisign/getHuiPoint", self_req, new TextHttpResponseHandler() {
//                                @Override
//                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                                    LogUtils.d("onFailure: " + responseString);
//                                }
//
//                                @Override
//                                public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                                    LogUtils.d("onSuccess: " + responseString);
//                                }
//                            });
//                        });
//                    }
//                },1000);

            }

            @Override
            public void onADClick() {
                LogUtils.d("onADClick: ");
            }

            @Override
            public void onVideoComplete() {
                LogUtils.d("onVideoComplete: ");

            }

            @Override
            public void onADClose() {
                LogUtils.d("onADClose: ");
                getActivity().runOnUiThread(() -> {
                    wv.evaluateJavascript("(" + callback_fun + ").call(this.vue,'" + result.toJSONString() + "')", null);
                });
            }

            @Override
            public void onError(AdError adError) {
                LogUtils.d("onError603: " + adError.getErrorMsg());
                getActivity().runOnUiThread(() -> {
                    result.put("rewardVerify", false);
                    result.put("code",Constants.AD_REWARD_PLAY_ERROR);
                    result.put("o", adError.getErrorCode());
                    result.put("msg", adError.getErrorMsg());
                    wv.evaluateJavascript("(" + callback_fun + ").call(this.vue,'" + result.toJSONString() + "')", null);
                });
            }
        });

        rewardVideoAD.setExt(extra + "_1");
        ServerSideVerificationOptions.Builder serverSideVerificationOptions = new ServerSideVerificationOptions.Builder();
        String token = SPUtils.getStringData(getContext(), "token", "not_login");
        serverSideVerificationOptions.setUserId(token);
        serverSideVerificationOptions.setCustomData(extra);
        rewardVideoAD.setServerSideVerificationOptions(serverSideVerificationOptions.build());

        Log.e(TAG, "getRewardVideoAdv_YouLiangHui: "+token+","+extra);

        LogUtils.d("getRewardVideoAdv_YouLiangHui: " + serverSideVerificationOptions.toString() ) ;
        LogUtils.d("getRewardVideoAdv_YouLiangHui: " + rewardVideoAD.toString() ) ;
        rewardVideoAD.loadAD();
        /**
         * 2021-08-04 09:51:44.256 30555-30555/com.android.jdhshop D/JuDuoHuiFragment: onReward:
         * 2021-08-04 09:51:44.256 30555-30555/com.android.jdhshop D/JuDuoHuiFragment: onReward: transId,49d8c7ee67c5deb784ca2de92c754b49
         * 2021-08-04 09:51:44.262 30555-30555/com.android.jdhshop D/JuDuoHuiFragment: onVideoComplete:
         * 2021-08-04 09:51:45.143 30555-31083/com.android.jdhshop V/StudioTransport: Handling agent command 1200 for pid: 30555.
         * 2021-08-04 09:51:49.426 30555-30555/com.android.jdhshop D/JuDuoHuiFragment: onADClose:
         */
    }

    /**
     * 激励视频广告 - 穿山甲
     *
     * @param ad_position_code 广告位ID
     * @param orderNo          订单号
     * @param extra            扩展数据
     * @param callback_fun     JS回调
     */
    private void getRewardVideoAdv_ChuanShanJia(String ad_position_code, String orderNo, String extra, String callback_fun) {
        JSONObject result = new JSONObject();

        TTAdNative mTTAdNative = TTAdSdk.getAdManager().createAdNative(mContext);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(ad_position_code)
                .setUserID(SPUtils.getStringData(mContext, "token", "not_login"))//tag_id
                .setMediaExtra(extra) //附加参数
                .setOrientation(TTAdConstant.HORIZONTAL) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();
        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int i, String s) {
                LogUtils.d("getRewardVideoAdv_ChuanShanJia onError: " + i + "," + s);

                getActivity().runOnUiThread(() -> {
                    result.put("rewardVerify", false);
                    result.put("code", Constants.AD_REWARD_PLAY_ERROR);
                    result.put("o",i);
                    result.put("msg", s);
                    wv.evaluateJavascript("(" + callback_fun + ").call(this.vue,'" + result.toJSONString() + "')", null);
                });
            }

            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ttRewardVideoAd) {
                LogUtils.d("onRewardVideoAdLoad: ");
            }

            @Override
            public void onRewardVideoCached() {

            }

            @Override
            public void onRewardVideoCached(TTRewardVideoAd ttRewardVideoAd) {
                getActivity().runOnUiThread(() -> {
                    ttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {
                        private final String TAG = "穿山甲";

                        @Override
                        public void onAdShow() {
                            LogUtils.d("onAdShow: ");
                        }

                        @Override
                        public void onAdVideoBarClick() {
                            LogUtils.d("onAdVideoBarClick: ");
                        }

                        @Override
                        public void onAdClose() {
                            LogUtils.d("onAdClose: ");
                            getActivity().runOnUiThread(() -> {
                                wv.evaluateJavascript("(" + callback_fun + ").call(this.vue,'" + result.toJSONString() + "')", null);
                            });
                        }

                        @Override
                        public void onVideoComplete() {
                            LogUtils.d("onVideoComplete: ");
                        }

                        @Override
                        public void onVideoError() {
                            LogUtils.d("onVideoError: ");
                            result.put("code",Constants.AD_REWARD_PLAY_ERROR);
                            result.put("msg","错误");
                            getActivity().runOnUiThread(() -> {
                                wv.evaluateJavascript("(" + callback_fun + ").call(this.vue,'" + result.toJSONString() + "')", null);
                            });
                        }

                        @Override
                        public void onRewardVerify(boolean b, int i, String s, int i1, String s1) {
                            LogUtils.d("onRewardVerify: " + b + ',' + i + "," + s + "," + i1 + "," + s1);
                            // 这里不回调，改为 adclose回调

                            result.put("rewardVerify", b);
                            result.put("rewardAmount", i);
                            result.put("rewardName", s);
                            result.put("code",Constants.AD_REWARD_PLAY_SUCCESS);
                            result.put("o", i1);
                            result.put("msg", s1);
                        }

                        @Override
                        public void onSkippedVideo() {
                            LogUtils.d("onSkippedVideo: ");
                        }
                    });
                    ttRewardVideoAd.showRewardVideoAd(getActivity());
                });
            }
        });
    }
    RewardVideoAd bd_rewardVideoAd = null;
    private void getRewardVideoAdv_Baidu(String ad_position_code, String orderNo, String extra, String callback_fun){

        bd_rewardVideoAd = new RewardVideoAd(mContext, ad_position_code, new RewardVideoAd.RewardVideoAdListener() {
            boolean is_verify = false;
            JSONObject result = new JSONObject();
            @Override
            public void onAdShow() {
                LogUtils.d(TAG, "onAdShow: ");
            }

            @Override
            public void onAdClick() {
                LogUtils.d(TAG, "onAdClick: ");
            }

            @Override
            public void onAdClose(float v) {
                LogUtils.d(TAG, "onAdClose: " + v);
//                result.put("rewardVerify", is_verify);
                wv.evaluateJavascript("(" + callback_fun + ").call(this.vue,'" + result.toJSONString() + "')", null);
            }

            @Override
            public void onAdFailed(String s) {
                LogUtils.d(TAG, "getRewardVideoAdv_Baidu onAdFailed: " + s);
                showToast(s);
                result.put("rewardVerify", false);
                result.put("code",Constants.AD_REWARD_PLAY_ERROR);
                result.put("msg",s);
                wv.evaluateJavascript("(" + callback_fun + ").call(this.vue,'" + result.toJSONString() + "')", null);
            }

            @Override
            public void onVideoDownloadSuccess() {
                LogUtils.d(TAG, "onVideoDownloadSuccess: ");
            }

            @Override
            public void onVideoDownloadFailed() {
                LogUtils.d(TAG, "onVideoDownloadFailed: ");
            }

            @Override
            public void playCompletion() {
                LogUtils.d(TAG, "playCompletion: ");
            }

            @Override
            public void onAdLoaded() {
                LogUtils.d(TAG, "onAdLoaded: ");
                bd_rewardVideoAd.show();
            }

            @Override
            public void onAdSkip(float v) {
                LogUtils.d(TAG, "onAdSkip: " + v);
            }

            @Override
            public void onRewardVerify(boolean b) {
                LogUtils.d(TAG, "baidu onRewardVerify: " + b);
                result.put("code",Constants.AD_REWARD_PLAY_SUCCESS); // 播放成功，可以验证。

/*                is_verify = b;
                LogUtils.d(TAG, "onRewardVerify: ");
                RequestParams req = new RequestParams();
                req.put("token", SPUtils.getStringData(getContext(), "token","") );
                req.put("orderNo",orderNo);
                HttpUtils.post(Constants.APP_IP + "/api/UserHuisign/getHuiPoint", req, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        result.put("rewardVerify",false);
                        result.put("code",1);
                        result.put("msg","服务器出错！");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        JSONObject response = JSONObject.parseObject(responseString);
                        if (response.getIntValue("code") == 0 && response.getIntValue("third_state") == 1){
                            result.put("rewardVerify",true);
                            result.put("code",0);
                            result.put("msg","成功");
                        }
                        else {
                            result.put("rewardVerify",false);
                            result.put("code",1);
                            result.put("msg","失败");
                        }
                    }
                });*/
            }
        });
        bd_rewardVideoAd.setExtraInfo(extra);
        bd_rewardVideoAd.setUserId(SPUtils.getStringData(context, "token", ""));
        bd_rewardVideoAd.load();

//        if (bd_rewardVideoAd.isReady()) bd_rewardVideoAd.show();
    }

    private void getRewardVideoAdv_KuaiShou(String ad_position_code, String orderNo, String extra, String callback_fun){

        /**
         * 2021-09-08 09:41:12.894 28748-28748/com.android.jdhshop D/JuDuoHuiFragment: onRequestResult:  i = 1
         * 2021-09-08 09:41:13.262 28748-28748/com.android.jdhshop D/JuDuoHuiFragment: onRewardVideoAdLoad: 1
         * 2021-09-08 09:41:13.563 28748-28748/com.android.jdhshop D/JuDuoHuiFragment: onVideoPlayStart:
         * 2021-09-08 09:41:43.590 28748-28748/com.android.jdhshop D/JuDuoHuiFragment: onRewardVerify:
         * 2021-09-08 09:42:01.392 28748-28748/com.android.jdhshop D/JuDuoHuiFragment: onVideoPlayEnd:
         * 2021-09-08 09:42:06.840 28748-28748/com.android.jdhshop D/JuDuoHuiFragment: onPageDismiss:
         */
        JSONObject result = new JSONObject();
        KsLoadManager ksLoadManager = KsAdSDK.getLoadManager();
        KsScene ksScene = new KsScene.Builder(Long.parseLong(ad_position_code)).adNum(1).build();
        Map<String,String> extra_data = new HashMap<>();
        extra_data.put("userId",SPUtils.getStringData(getContext(),"token",""));
        extra_data.put("extraData",extra);
        ksScene.setRewardCallbackExtraData(extra_data);

        ksLoadManager.loadRewardVideoAd(ksScene, new KsLoadManager.RewardVideoAdListener() {
            @Override
            public void onError(int i, String s) {
                LogUtils.d(TAG, "getRewardVideoAdv_KuaiShou onError: code = " + i + ", message = " + s);
                showToast("配置信息出错，请与客服联系【CODE:04】！");
                result.put("rewardVerify", false);
                result.put("code",i);
                result.put("msg",s);
                wv.evaluateJavascript("(" + callback_fun + ").call(this.vue,'" + result.toJSONString() + "')", null);
            }

            @Override
            public void onRequestResult(int i) {
                LogUtils.d(TAG, "onRequestResult:  i = " +i);
            }

            @Override
            public void onRewardVideoAdLoad(@Nullable List<KsRewardVideoAd> list) {

                if (list == null) {
                    LogUtils.d(TAG, "onRewardVideoAdLoad: 广告返回为空");
                }
                else {
                    LogUtils.d(TAG, "onRewardVideoAdLoad: " + list.size());
                    KsRewardVideoAd rewardVideoAd = list.get(0);
                    KsVideoPlayConfig ksVideoPlayConfig = new KsVideoPlayConfig.Builder().videoSoundEnable(true).showLandscape(false).build();
                    rewardVideoAd.setRewardAdInteractionListener(new KsRewardVideoAd.RewardAdInteractionListener() {
                        @Override
                        public void onAdClicked() {
                            LogUtils.d(TAG, "onAdClicked: ");
                        }

                        @Override
                        public void onPageDismiss() {
                            LogUtils.d(TAG, "onPageDismiss: ");
//                            result.put("rewardVerify", true);
                            wv.evaluateJavascript("(" + callback_fun + ").call(this.vue,'"+ result.toJSONString() +"')", null);
                        }

                        @Override
                        public void onVideoPlayError(int i, int i1) {
                            LogUtils.d(TAG, "onVideoPlayError: i="+i+",i1="+i1);
                            result.put("code",Constants.AD_REWARD_PLAY_ERROR);
                            result.put("o",i);
                            result.put("s",i1);
                            result.put("msg","播放出错");
                        }

                        @Override
                        public void onVideoPlayEnd() {
                            LogUtils.d(TAG, "onVideoPlayEnd: ");

                        }

                        @Override
                        public void onVideoSkipToEnd(long l) {
                            LogUtils.d(TAG, "onVideoSkipToEnd: " +l);
                        }

                        @Override
                        public void onVideoPlayStart() {
                            LogUtils.d(TAG, "onVideoPlayStart: ");
                        }

                        @Override
                        public void onRewardVerify() {
                            LogUtils.d(TAG, "KuaiShou onRewardVerify: ");
                            result.put("code",Constants.AD_REWARD_PLAY_SUCCESS);
                            result.put("msg","观看完毕");
//                            RequestParams req = new RequestParams();
//                            req.put("token", SPUtils.getStringData(getContext(), "token","") );
//                            req.put("orderNo",orderNo);
//                            HttpUtils.post(Constants.APP_IP + "/api/UserHuisign/getHuiPoint", req, new TextHttpResponseHandler() {
//                                @Override
//                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                                    result.put("rewardVerify",false);
//                                }
//
//                                @Override
//                                public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                                    LogUtils.d(TAG, "onSuccess: " + responseString);
//                                    JSONObject response = JSONObject.parseObject(responseString);
//                                    if (response.getIntValue("code") == 0){
//                                        result.put("rewardVerify",true);
//                                    }
//                                    else {
//                                        result.put("rewardVerify",false);
//                                    }
//                                }
//                            });
                        }

                    });

                    rewardVideoAd.showRewardVideoAd(getActivity(),ksVideoPlayConfig);
                }

            }
        });
    }

    //原生开发请用此方法获取设备号
    @SuppressLint({"HardwareIds", "MissingPermission"})
    private static String getDeviceId(Context context) {
        String imei = null;
        TelephonyManager telephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyMgr != null) {
            try {
                imei = telephonyMgr.getDeviceId();
            } catch (Exception e) {
                return imei;
            }
        }
        return imei;
    }

    private class JuDuoHuiWebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            LogUtils.d("onPageStarted: " + url);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            LogUtils.d("onPageFinished: " + view.getMeasuredHeight());

            super.onPageFinished(view, url);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            if (!url.startsWith("http") && !url.startsWith("https") && !url.startsWith("ftp")) {
//                try {
//                    Uri uri = Uri.parse(url);
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    if (mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0) {
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        return true;
//                    }
//                } catch (Exception e) {
//                    LogUtils.d("setWebViewClient", "shouldOverrideUrlLoading: " + e.getMessage());
//                }
//                return true;
//            }

            view.loadUrl(url);
            return true;

//            return super.shouldOverrideUrlLoading(view, url);
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
            LogUtils.d("onJsAlert: " + message);
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            LogUtils.d("onJsConfirm: " + message);
            return super.onJsConfirm(view, url, message, result);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            LogUtils.d("onJsPrompt: " + message);
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
            return super.onJsBeforeUnload(view, url, message, result);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {

            LogUtils.d("onConsoleMessage: " + consoleMessage.messageLevel().name() + ":" + consoleMessage.message());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

        if (requestCode == REQUEST_SCAN_QR_CODE){
            if (resultCode == RESULT_OK) {
                String result_text = data.getStringExtra(Intents.Scan.RESULT);
                String success_fun = scan_qrcode_config.getString("success");
                if (success_fun != null){
                    wv.evaluateJavascript("("+success_fun+").call(this.vue,'"+ result_text +"')",null);
                }
            }
            else{
                String error_fun = scan_qrcode_config.getString("error");
                if (error_fun!=null){
                    wv.evaluateJavascript("("+error_fun+").call(this.vue)",null);
                }
            }
        }

        // 激励视频广告回调
        if (requestCode == REQUEST_REWARD_ADV ){
            // Log.d(TAG, "onActivityResult: 走这里？" + requestCode +","+resultCode+","+data);
            String close_fun = data.getStringExtra("close_fun");
            String hpoint = data.getStringExtra("hpoint");

            if (close_fun.startsWith("\"")){
                close_fun = close_fun.substring(1,close_fun.length()-1);
            }
            close_fun = close_fun.replaceAll("\\t","\t").replaceAll("\\n","\n");
            boolean option_result = resultCode == RESULT_OK;
            close_fun = "("+close_fun+").call(this.vue,"+option_result+")";
            LogUtils.logd(close_fun);
            wv.evaluateJavascript(close_fun,null);
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != REQUEST_FILE_CHOOSER_CODE || uploadMessageAboveL == null)
            return;
        Uri[] results = null;
        if (resultCode == RESULT_OK) {
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

    //    @Override
//    public void onBackPressed() {
//        LogUtils.d("onBackPressed: ");
//        if (listenAppStatus_back){
//            wv.evaluateJavascript( listenAppStatus_back_func.equals("") ? "OnStart()" : listenAppStatus_back_func ,new JuDuoHuiActivity.JuDuoHuiValueCallBack());
//        }
//        else {
//            if (wv.canGoBack()) {
//                wv.goBack();
//            } else {
//                super.onBackPressed();
//            }
//        }
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        LogUtils.d("onStart: ");
//        if (listenAppStatus_load){
//            wv.evaluateJavascript( listenAppStatus_load_func.equals("") ? "OnStart()" : listenAppStatus_load_func ,new JuDuoHuiActivity.JuDuoHuiValueCallBack());
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        LogUtils.d("onResume: ");
//        if (listenAppStatus_resume){
//            wv.evaluateJavascript( listenAppStatus_resume_func.equals("") ? "OnPause()" : listenAppStatus_resume_func ,new JuDuoHuiActivity.JuDuoHuiValueCallBack());
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        LogUtils.d("onPause: ");
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (listenAppStatus_hide){
//            wv.evaluateJavascript( listenAppStatus_hide_func.equals("") ? "OnPause()" : listenAppStatus_hide_func ,new JuDuoHuiActivity.JuDuoHuiValueCallBack());
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        wv.destroy();
//        super.onDestroy();
//    }
    public static class DialogJs {
        private CommonDialog dialog;
        public DialogJs(CommonDialog dg){
            dialog = dg;
        }
        @JavascriptInterface
        public void close(){
            dialog.dismiss();
        }
    }

    class JuDuoHuiValueCallBack implements ValueCallback<String> {

        @Override
        public void onReceiveValue(String value) {
            LogUtils.d("onReceiveValue: " + value);
        }
    }

    @Override
    protected void lazyload() {
    }


    public void onResume() {
        super.onResume();
        if (!CommonUtils.isNetworkAvailable()) {
            showToast(getResources().getString(R.string.error_network));
            return;
        }
        if (js.listenAppStatus_resume && isVisible()) {
            wv.evaluateJavascript(js.listenAppStatus_resume_func.equals("") ? "OnResume()" : js.listenAppStatus_resume_func, null);
        }
    }

    // 当显示时触发，代替 onShow 使用
    @Override
    protected void onVisible() {
        super.onVisible();
        LogUtils.d("onVisible: ");
    }

    // 当隐藏时触发，代替 onPause 使用
    @Override
    protected void onInVisible() {
        super.onInVisible();
        LogUtils.d("onInVisible: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d("onPause: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d("onStart: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d("onStop: ");
        if (js.listenAppStatus_hide) {
            wv.evaluateJavascript(js.listenAppStatus_hide_func.equals("") ? "OnPause()" : js.listenAppStatus_hide_func, null);
        }
    }

    private long firstClickTime = 0;
    public boolean backKeyPress(){
        if (System.currentTimeMillis() - firstClickTime < 2000 && !wv.canGoBack()){
            return true;
        }
        else
        {
            if (wv.canGoBack()){
                wv.goBack();
            }
            else{
                showToast("再次按返回键将退出");
                firstClickTime = System.currentTimeMillis();
            }
            return false;
        }

    }
}


