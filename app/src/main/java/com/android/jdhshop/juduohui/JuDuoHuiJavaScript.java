package com.android.jdhshop.juduohui;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.JdDetailsActivity;
import com.android.jdhshop.activity.JuDuoHuiActivity;
import com.android.jdhshop.activity.PddDetailsActivity;
import com.android.jdhshop.activity.XianWanActivity;
import com.android.jdhshop.bean.MyGoodsResp;
import com.android.jdhshop.bean.PDDBean;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.DaiLiFuWu;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.AsyncHttpResponseHandler;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.juduohui.message.JuduohuiCountInsertAdvMessage;
import com.android.jdhshop.juduohui.response.HttpJsonResponse;
import com.android.jdhshop.utils.AesUtils;
import com.android.jdhshop.utils.Base64Utils;
import com.android.jdhshop.utils.DownloadUtil;
import com.android.jdhshop.utils.ImgUtils;
import com.android.jdhshop.utils.WxUtil;
import com.android.jdhshop.widget.LoadingDialog;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class JuDuoHuiJavaScript {
    private final String TAG = "JuDuoHuiJavaScript";
    private Activity mContext = null;
    private WebView wv = null;
    private ValueCallback<String> stringValueCallback = null;

    public boolean listenAppStatus_resume = false; // 是否需要监听恢复
    public String listenAppStatus_resume_func = ""; // 监听恢复时的返回方法
    public boolean listenAppStatus_hide = false; // 是否需要监听隐藏
    public String listenAppStatus_hide_func = ""; // 监听恢复时的返回方法
    public boolean listenAppStatus_load = false; // 是否需要监听加载完成
    public String listenAppStatus_load_func = ""; // 监听恢复时的返回方法
    public boolean listenAppStatus_back = false; // 是否需要监听返回键
    public String listenAppStatus_back_func = ""; // 监听恢复时的返回方法
    private CallBackListen backPressListen = null;

    public interface CallBackListen {
        /**
         * 按下返回键的操作逻辑
         */
        void press();

        /**
         * JS关闭窗口的操作逻辑
         */
        void closeWindow();

        /**
         * 重载当前窗口的逻辑
         */
        void reloadWindow();
        void setStatusBarColor(String color_string);
        void showToasts(String msg);
        void renderAdvert(JSONObject adv_config); // 获取广告并渲染到图层
        void getRewardVideoAd(String adv_config,String close_fun); // 获取激励视频广告
        void setAdvStation(String css_class, boolean station);
        void setScrollTop();
        void setNativeTitleBar(com.alibaba.fastjson.JSONObject native_title_bar_setting);
        void setCircleTimer(com.alibaba.fastjson.JSONObject circle_config);
        void startCircleAni();
        void removeAllAdv();
        void removeAdv(String config);
        void renderWebView(JSONObject web_config);
        void sendParamToWebView(JSONObject web_config);
        void scanQrCode(JSONObject config);
        void alert(JSONObject config);
    }

    /**
     * 实例化
     *
     * @param web_view              mWebView
     * @param cn                    mContext
     * @param string_value_callback mValueCallBack<String>
     */
    public JuDuoHuiJavaScript(WebView web_view, Activity cn, CallBackListen brp, ValueCallback<String> string_value_callback) {
        this.mContext = cn;
        this.wv = web_view;
        this.stringValueCallback = string_value_callback;
        this.backPressListen = brp;
    }

    private void startActivity(Intent intent) {
        mContext.startActivity(intent);
    }

    private void androidShowToast(String message) {
        Looper.prepare();
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }
    public LoadingDialog loadingDialog;
    @JavascriptInterface
    public void androidShowLoading(String message){
        if ("undefined".equals(message)) { message = ""; }
        loadingDialog = LoadingDialog.createDialog(mContext);
        loadingDialog.setMessage(message);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
    }

    @JavascriptInterface
    public void countViewPage(){
        JuduohuiCountInsertAdvMessage message = new JuduohuiCountInsertAdvMessage();
        message.setAction(0);
        message.setActivity(mContext);
        EventBus.getDefault().post(message);
    }

    @JavascriptInterface
    public void androidCloseLoading(){
        if (loadingDialog!=null) loadingDialog.dismiss();
    }
    @JavascriptInterface
    public void setProxy(String json_string){
        try {
            DaiLiFuWu.MyService(json_string, mContext);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @JavascriptInterface
    public void sendParamToWebView(String json_config){
        LogUtils.d(TAG, "sendParams: " + json_config);
        try{
            JSONObject config = JSONObject.parseObject(json_config.replaceAll("\n",""));
            backPressListen.sendParamToWebView(config);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @JavascriptInterface
    public void renderWebView(String json_config){
        LogUtils.d(TAG, "renderWebView: " + json_config);
        try {
            JSONObject config = JSONObject.parseObject(json_config.replaceAll("\n",""));
            backPressListen.renderWebView(config);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public String getScreenInfo(){
        DisplayMetrics dt = new DisplayMetrics();
        WindowManager dm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        JSONObject report = new JSONObject();
        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        LogUtils.d(TAG, "getScreenInfo: " + statusBarHeight1);
        if (dm!=null) {
            dm.getDefaultDisplay().getMetrics(dt);
            LogUtils.d(TAG, "getScreenInfo: " + dt);
            report.put("width", (int) (dt.widthPixels / dt.scaledDensity) );
            report.put("height", (int) (dt.heightPixels / dt.scaledDensity) );
        }

        return report.toJSONString();
    }

    public int CheckInstalled(String packageName) {
        int isInstall = 0;
        LogUtils.d(TAG, "CheckInstall: " + packageName);
        if (!TextUtils.isEmpty(packageName)) {

            try {
                ApplicationInfo info = mContext.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
                LogUtils.d(TAG, "CheckInstall: " + info);
                isInstall = 1;
            } catch (PackageManager.NameNotFoundException e) {
                LogUtils.d(TAG, "CheckInstall: " + packageName + "未安装.");
            }
        }

        return isInstall;
    }

    @JavascriptInterface
    public void scanQrCode(String json_config){
        try {
            JSONObject config = JSONObject.parseObject(json_config);
            // Log.d(TAG, "scanQrCode: " + json_config);

            backPressListen.scanQrCode(config);
        }
        catch (Exception e){
            e.printStackTrace();
            showToast("扫码配置出错，请检查");
        }

    }

    @JavascriptInterface
    public void removeAllAdv(){
        backPressListen.removeAllAdv();
    }

    /**
     * 前台获取广告 —— 信息流【暂时，后期改成所有都走此接口】
     * @param json_config 样例
     *      {
     * 			channel:"",
     * 			position_id:"0",
     * 			ads_class:"",
     * 			order_id:"",
     * 			ondisplay:function(),
     * 			onerror:function(),
     * 			onclick:function(),
     * 			ondownload:function()
     * 		}
     */
    @JavascriptInterface
    public void renderAdvert(String json_config){
        try {
            JSONObject tmp = JSONObject.parseObject(json_config);
            backPressListen.renderAdvert(tmp);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @JavascriptInterface
    public void getRewardVideoAd(String adv_config,String close_function){

            backPressListen.getRewardVideoAd(adv_config,close_function);
    }

    @JavascriptInterface
    public void checkResourceUpdate(){
        AssetTools.checkUpdate(mContext);
    }
    @JavascriptInterface
    public String getAppIP(){
        return Constants.APP_IP;
    }
    @JavascriptInterface
    public String getKefuUrl(){ return SPUtils.getStringData(mContext,"kefu_url","");}
    @JavascriptInterface
    public void setNativeTitle(String json_string){
        LogUtils.d(TAG, "setNativeTitle: " + json_string);
        com.alibaba.fastjson.JSONObject title_bar_set = com.alibaba.fastjson.JSONObject.parseObject(json_string);
        backPressListen.setNativeTitleBar(title_bar_set);
    }
    @JavascriptInterface
    public void setScrollTop(){
        backPressListen.setScrollTop();
    }
    @JavascriptInterface
    public boolean getAdIsOpen(){
        return SPUtils.getBoolean(mContext,"is_open_ad",true);
    }
    @JavascriptInterface
    public void setAdvStation(String css_class,boolean ad_station){
        backPressListen.setAdvStation(css_class,ad_station);
    }
    @JavascriptInterface
    public void setAdvStation(boolean ad_station){
        backPressListen.setAdvStation("",ad_station);
    }

    @JavascriptInterface
    public void showToast(String msg) {
        backPressListen.showToasts(msg);
    }

    public void postCallBack(String call_back_function, String return_value) {
        wv.post(() -> {
            String tmp = return_value.replace("\\n", "").replace("\\r", "");
            wv.evaluateJavascript(call_back_function + "('" + Base64Utils.encode(tmp.getBytes()) + "')", stringValueCallback);
        });
    }
    @JavascriptInterface
    public String md532(String msg){
        return AesUtils.md532(msg);
    }
    @JavascriptInterface
    public String md5(String msg){

String res = AesUtils.md5(msg);
        Log.d(TAG, "md5: " + msg +",,, res " + res);
        return res; }
    @JavascriptInterface
    public String e(String msg){
        return AesUtils.encrypt(msg);
    }
    @JavascriptInterface
    public String d(String msg){
        return AesUtils.decrypt(msg);
    }
    @JavascriptInterface
    public void s(String msg){
        try {
            JSONObject response = JSONObject.parseObject(msg);
            JSONObject headers =  response.getJSONObject("headers");
            String ticket = headers.getString("set-cookies");
            if (ticket == null) return;
            JSONObject post_t = new JSONObject();
            post_t.put("ticket",ticket);
            post_t.put("token",getToken());
            RequestParams post_r = new RequestParams();
            post_r.put("t", AesUtils.encrypt(post_t.toJSONString()));
            HttpUtils.post(Constants.NEWS_READ_PING_BACK, mContext,post_r, new HttpJsonResponse() {
                @Override
                protected void onSuccess(JSONObject response) {
                    super.onSuccess(response);
                    // Log.d(TAG, "onSuccess: " + response.toJSONString());
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @JavascriptInterface
    public String si(String json_data){
        try{
            JSONObject data = JSONObject.parseObject(json_data);
            data.put("timestamp",(int)(System.currentTimeMillis()/1000));
            return AesUtils.encrypt(data.toJSONString());
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @JavascriptInterface
    public boolean checkPermission() {

        AcpOptions.Builder builder = new AcpOptions.Builder();
        builder.setRationalMessage("游戏试玩需要用到存储权限，请先授予程序权限!")
                .setRationalBtn("去设置")
                .setDeniedMessage("用户拒绝了权限申请")
                .setDeniedCloseBtn("取消")
                .setDeniedSettingBtn("setDeniedSettingBtn")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        Acp.getInstance(mContext).request(builder.build(), new AcpListener() {
            @Override
            public void onGranted() {
//                    wv.post(new Runnable() {
//                        @Override
//                        public void run() {
//                        }
//                    })
                LogUtils.d(TAG, "onGranted: 用户请允许了");
            }

            @Override
            public void onDenied(List<String> permissions) {
                LogUtils.d(TAG, "onDenied: 用户拒绝了");
            }
        });
        return true;
    }

    /**
     * AJAX获取信息
     * @param ajax_config 实现了大部分的jquery的ajax参数
     */
    @JavascriptInterface
    public void zjax(String ajax_config){
        LogUtils.d(TAG, "zjax - 1: " + ajax_config);

        JSONObject req_config = JSONObject.parseObject(ajax_config);
        String method =req_config.getString("type");
        String url = req_config.getString("url");
        String data = req_config.getString("data");
        String success_callback = req_config.getString("success");
        String error_callback = req_config.getString("error");
        String complete = req_config.getString("complete");

        if (url == null) return;
        if (method == null) method = "get";

        TextHttpResponseHandler callback_handler = new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (error_callback != null) {
                    wv.post(()->{
                        LogUtils.d(TAG, "onFailure: error回调" + responseString);
                        wv.evaluateJavascript("("+error_callback+")("+ responseString +",this.vue)",null);
                    });
                }
                else
                {
                    LogUtils.d(TAG, "onFailure: " + responseString);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (success_callback != null){
                    wv.post(()->{
                        LogUtils.d(TAG, "onSuccess: success回调" + responseString);
                        wv.evaluateJavascript("("+success_callback+")("+ responseString +",this.vue)",null);
                    });
                }
                else
                {
                    LogUtils.d(TAG, "onSuccess: " + responseString);
                }
            }

            @Override
            public void onFinish() {
                wv.post(()->{
                    wv.evaluateJavascript("("+complete+")()",null);
                    LogUtils.d(TAG, "onFinish: complete回调");
                });
                super.onFinish();
            }
        };

        RequestParams request_params = new RequestParams();
        if (data!=null){
            JSONObject request_data = JSONObject.parseObject(data);
            for (String keys : request_data.keySet()){
                request_params.put(keys,request_data.getString(keys));
            }
        }
        if (method.equals("post")){
            HttpUtils.post(url, mContext,request_params, callback_handler);
        }
        else{
            HttpUtils.get(mContext,url, request_params, callback_handler);
        }
    }

    @JavascriptInterface
    public String ajax(String url, String method, String data, String callback) {
        RequestParams req_param = new RequestParams();

        if (data != null && !"".equals(data)) {
            com.alibaba.fastjson.JSONObject json_param = com.alibaba.fastjson.JSONObject.parseObject(data);
            for (String key : json_param.keySet()) {
                req_param.put(key, json_param.getString(key));
            }
        }

        req_param.put("cust_time", System.currentTimeMillis());

        // url , json_data
        if (method.toLowerCase().equals("get")) {
            // get

            HttpUtils.get(mContext,url, req_param, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    com.alibaba.fastjson.JSONObject js = com.alibaba.fastjson.JSONObject.parseObject(responseString);
                    postCallBack(callback, responseString);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    com.alibaba.fastjson.JSONObject js = com.alibaba.fastjson.JSONObject.parseObject(responseString);
                    postCallBack(callback, responseString);
                }
            });
        } else {
            // post
            HttpUtils.post(url,mContext, req_param, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    com.alibaba.fastjson.JSONObject js = com.alibaba.fastjson.JSONObject.parseObject(responseString);
                    postCallBack(callback, responseString);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    com.alibaba.fastjson.JSONObject js = com.alibaba.fastjson.JSONObject.parseObject(responseString);
                    postCallBack(callback, responseString);
                }
            });

        }
        return "";
    }

    @JavascriptInterface
    public void backPress() {

//            runOnUiThread(JuDuoHuiActivity.this::onBackPressed);

        backPressListen.press();
    }
    @JavascriptInterface
    public void setCircleTimer(String json_string){
        LogUtils.d(TAG, "setCircleTimer: " + json_string);
        com.alibaba.fastjson.JSONObject config = null;
        try{
            config = com.alibaba.fastjson.JSONObject.parseObject(json_string);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if (config != null) {
            backPressListen.setCircleTimer(config);
        }
    }
    @JavascriptInterface
    public void startCircleAni(){
        backPressListen.startCircleAni();
    }
    @JavascriptInterface
    public void addEventListen(String addListen, String function_name) {
        if ("hide".equals(addListen)) {
            listenAppStatus_hide = true;
            listenAppStatus_hide_func = function_name;
        } else if ("resume".equals(addListen)) {
            listenAppStatus_resume = true;
            listenAppStatus_resume_func = function_name;
        } else if ("load".equals(addListen)) {
            listenAppStatus_load = true;
            listenAppStatus_load_func = function_name;
        } else if ("back".equals(addListen)) {
            listenAppStatus_back = true;
            listenAppStatus_back_func = function_name;
        }
        wv.post(()->{
           if (listenAppStatus_back && wv.canGoBack()){
               wv.goBack();
           }
        });
        LogUtils.d(TAG, "addEventListen(string,string): " + addListen + "," + function_name);
    }
    @JavascriptInterface
    public void removeEventListener(String listen_name){
        switch (listen_name){
            case "hide":
                listenAppStatus_hide = false;
                listenAppStatus_hide_func = null;
                break;
            case "resume":
                listenAppStatus_resume = false;
                listenAppStatus_resume_func = null;
                break;
            case "load":
                listenAppStatus_load = false;
                listenAppStatus_load_func = null;
                break;
            case "back":
                listenAppStatus_back = false;
                listenAppStatus_back_func = null;
                break;
        }
    }

    @JavascriptInterface
    public void openUrl(String url) {
        Intent in = new Intent(mContext, JuDuoHuiActivity.class);
        in.putExtra("url", url);
        in.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        startActivity(in);
    }

    @JavascriptInterface
    public void openUrl(String url, String config) {

        Intent in = new Intent(mContext, JuDuoHuiActivity.class);
        in.putExtra("url", url);
        in.putExtra("config",config);
        in.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        startActivity(in);
    }

    @JavascriptInterface
    public String getMYXQAppID() {
        return SPUtils.getStringData(mContext, "JD_APP_KEY_NEW", "");
    }

    @JavascriptInterface
    public String getJDHSecret(){ return SPUtils.getStringData(mContext,"jdh_secret",""); }
    @JavascriptInterface
    public String getToken() {
        return SPUtils.getStringData(mContext, "token", "");
    }
    @JavascriptInterface
    public String getIMEI(){
        return CaiNiaoApplication.getInstances().getOaid();
    }
    @JavascriptInterface
    public String getMakeMoneyList(){ return SPUtils.getStringData(mContext,"makemoneyList",""); }
    @JavascriptInterface
    public String getUid(){ return SPUtils.getStringData(mContext, "uid", ""); }
    @JavascriptInterface
    public String getResource(){
        return CaiNiaoApplication.getAppChannel();
    }
    @JavascriptInterface
    public String getYXDomain(){
        return SPUtils.getStringData(mContext,"yx_domain","");
    }
    @JavascriptInterface
    public String getYXAppID(){
        return SPUtils.getStringData(mContext,"yx_app_id","");
    }
    @JavascriptInterface
    public String getAppCode(){
        try {
            PackageManager packageManager = CaiNiaoApplication.getAppContext().getPackageManager();
            PackageInfo pi = packageManager.getPackageInfo(CaiNiaoApplication.getInstances().getPackageName(), 0);
            return pi.versionCode+"";

        }
        catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
    @JavascriptInterface
    public void closeWindow() { backPressListen.closeWindow(); }
    @JavascriptInterface
    public void reloadWindow(){ backPressListen.reloadWindow(); }

    @JavascriptInterface
    public void setStatusBar(String color) { backPressListen.setStatusBarColor(color); }

    @JavascriptInterface
    public void openXianWan() {
        Intent i = new Intent(mContext, XianWanActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }

    private void openJdDetail(String goods_id) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("apikey", Constants.JD_APP_KEY_NEW);
        requestParams.put("goods_ids", goods_id);
        requestParams.put("isunion", "1");

        HttpUtils.get(mContext,Constants.JDNEWGOODS_LIST + "?" + requestParams.toString(), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                JSONObject object1 = null;
                try {
                    object1 = JSONObject.parseObject(responseString);
                    JSONArray array = object1.getJSONObject("data").getJSONArray("data");
                    MyGoodsResp resp = new Gson().fromJson(array.getJSONObject(0).toString(), MyGoodsResp.class);
                    Intent intent = new Intent(mContext, JdDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("goods", resp);
                    intent.putExtra("goods", bundle);
                    wv.post(() -> {
                        wv.evaluateJavascript("layer.closeAll()", null);
                    });
                    startActivity(intent);


                } catch (JSONException e) {
                    e.printStackTrace();
                    wv.post(() -> {
                        wv.evaluateJavascript("alertMessage('" + e.getMessage() + "')", null);
                    });
                }

            }
        });
    }

    private void openPddDetail(String goods_id, String goods_sign_id) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("goods_id", goods_id);
        requestParams.put("apikey", Constants.JD_APP_KEY_NEW);
        requestParams.put("isunion", "1");
        requestParams.put("keyword", goods_id + "");

        HttpUtils.post(Constants.pddSearch,mContext, requestParams, new TextHttpResponseHandler() {
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
                wv.post(() -> {
                    wv.evaluateJavascript("layer.closeAll()", null);
                });

                try {
                    JSONObject object = JSONObject.parseObject(responseString).getJSONObject("data");
                    Intent intent = new Intent(mContext, PddDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("goods", new Gson().fromJson(object.getJSONArray("goods_list").getJSONObject(0).toString(), PDDBean.class));
                    intent.putExtra("goods", bundle);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                    wv.post(() -> {
                        wv.evaluateJavascript("alertMessage('" + e.getMessage() + "')", null);
                    });
                }
            }
        });
    }

    @JavascriptInterface
    public void alert(String json_config){
        // Log.d(TAG, "alert: " + json_config);
        try{
            JSONObject config = JSON.parseObject(json_config);
            backPressListen.alert(config);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @JavascriptInterface
    public void alertAddGolds(String option_add,String number,String unit,String message,String button_string,String button_fun){
        // Log.d(TAG, "alertAddGolds: " + option_add+","+number+","+unit+","+message+","+button_string+","+button_fun);
        mContext.runOnUiThread(()->{
            CommonDialog commonDialog = new CommonDialog(mContext);
            commonDialog.getMessageContent().setVisibility(View.GONE);
            View root_view = commonDialog.getRootView();
            RelativeLayout golds_main = root_view.findViewById(R.id.add_golds_box);
            golds_main.setVisibility(View.VISIBLE);
            TextView desp_message = root_view.findViewById(R.id.desp_message);
            desp_message.setText(message);
            TextView desp_message_no = root_view.findViewById(R.id.desp_message_no);
            desp_message_no.setText(number);
            TextView desp_message_add = root_view.findViewById(R.id.desp_message_add);
            desp_message_add.setText(option_add);
            TextView desp_message_unit = root_view.findViewById(R.id.desp_message_uni);
            desp_message_unit.setText(unit);
            Button golds_button = root_view.findViewById(R.id.golds_button);

            if ("".equals(button_string)){
                // Log.d(TAG, "alertAddGolds: button_string = "+ button_string );
//                golds_button.setVisibility(View.GONE);
                root_view.findViewById(R.id.golds_button_box).setVisibility(View.GONE);
                root_view.findViewById(R.id.no_golds_button_box).setVisibility(View.VISIBLE);
            }
            else {
                golds_button.setText(button_string);
                golds_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wv.evaluateJavascript("("+ button_fun +").call(this.vue);",null);
                        commonDialog.dismiss();
                    }
                });
            }
            commonDialog.show();
        });
    }
    @JavascriptInterface
    public void showGoodsDetail(String goods_info_json) {
        LogUtils.d(TAG, "showGoodsDetail: " + goods_info_json);
        com.alibaba.fastjson.JSONObject goods_info = com.alibaba.fastjson.JSONObject.parseObject(goods_info_json);
        String source_from = goods_info.getString("source");
        switch (source_from) {
            case "jd":
                openJdDetail(goods_info.getString("goods_id"));
                break;
            case "pdd":
                openPddDetail(goods_info.getString("goods_id"), goods_info.getString("goods_id"));
                break;
        }
    }

    /**
     * 分享文本到微信
     *
     * @param to_wx_url 分享到微信的消息文本
     * @param toAnyWay  分享到哪里，0个人，1朋友圈
     */
    @JavascriptInterface
    public void ShareTxtToWeiXin(String to_wx_url, int toAnyWay) {
        WxUtil.sendTextMessage(toAnyWay, to_wx_url);
    }

    @JavascriptInterface
    public void ShareCardToWeiXin(String url,String desc, int toAnyWay) {
        WxUtil.sendCardMessage(toAnyWay, mContext, desc, url);
    }

    private String package_name;

    @JavascriptInterface
    public void CheckInstall(String packageName) {
        package_name = packageName;
        LogUtils.d(TAG, "CheckInstall: " + packageName);
        int isInstall = CheckInstalled(packageName);
        String js = "javascript:CheckInstall_Return(" + (isInstall == 1 ? "1)" : "0)");
        wv.post(new Runnable() {
            @Override
            public void run() {
                wv.evaluateJavascript(js, null);
            }
        });

    }

    @JavascriptInterface
    public void OpenAPP(final String packageName) {
        LogUtils.d(TAG, "OpenAPP: " + packageName);
        if (CheckInstalled(packageName) == 1) {
            try {
                PackageManager pm = mContext.getPackageManager();
                PackageInfo pi = pm.getPackageInfo(packageName, 0);
                Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
                //resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                resolveIntent.setPackage(pi.packageName);
                List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);
                ResolveInfo ri = apps.iterator().next();
                if (ri != null) {
                    String package_name = ri.activityInfo.packageName;
                    String class_name = ri.activityInfo.name;
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_FROM_BACKGROUND);
                    ComponentName cn = new ComponentName(package_name, class_name);
                    intent.setComponent(cn);
                    mContext.startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showToast("未安装指定应用");
        }
    }

    @JavascriptInterface
    public void InstallAPP(final String url) {
        LogUtils.d(TAG, "InstallAPP: " + url);
        File ff = Environment.getExternalStoragePublicDirectory("jdh_download");
        if (!ff.exists()) ff.mkdirs();
        String filename = package_name + "_xw.apk";
        String full_path = ff.getAbsolutePath() + "/" + filename;
        LogUtils.d(TAG, "InstallAPP: " + filename);
        LogUtils.d(TAG, "InstallAPP: " + ff);
        DownloadUtil.get().download(url, ff.getAbsolutePath(), filename, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(mContext, "com.android.jdhshop.update_app.file_provider", new File(full_path));
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(Uri.fromFile(new File(full_path)), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                startActivity(intent);
            }

            @Override
            public void onDownloading(int progress) {
                wv.post(new Runnable() {
                    @Override
                    public void run() {
                        wv.loadUrl("javascript:setProgress('" + package_name + "'," + progress + ")");
                    }
                });

            }

            @Override
            public void onDownloadFailed(Exception e) {
                showToast("下载失败" + e.getMessage());
            }
        });

    }

    @JavascriptInterface
    public void Browser(final String url) {
        LogUtils.d(TAG, "使用系统浏览器浏览: " + url);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * 保存图片到本地相册
     */
    @JavascriptInterface
    public void saveToSdCard(String urls) {
        LogUtils.d(TAG, "saveToSdCard: " + urls);
        try {
            if (urls.startsWith("http")) {
                HttpUtils.get(mContext,urls, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                        ImgUtils.saveImageToGallery2(mContext, bitmap);
                        showToast("保存到相册成功");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        showToast("文件下载失败，请秒重试");
                    }
                });
            } else {
                String tmp_urls = urls.replace("data:image/png;base64,", "");
                byte[] bytes = Base64.decode(tmp_urls, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ImgUtils.saveImageToGallery2(mContext, bitmap);
                showToast("保存到相册成功");
            }
        }
        catch (Exception e){
            showToast("保存到相册失败，请手动截图保存或换一份体验券吧");
        }
    }

    /**
     * 分享图片到微信
     */
    @JavascriptInterface
    public void ShareImgToWeiXin(String share_img_url, int toAnyWay) {
        if (share_img_url.startsWith("http")) {
            HttpUtils.get(mContext,share_img_url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    WxUtil.sendImageMessage(responseBody, toAnyWay);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    showToast("文件下载失败，请秒重试");
                }
            });
        } else {
            String tmp_urls = share_img_url.replace("data:image/png;base64,", "");
            byte[] bytes = Base64.decode(tmp_urls, Base64.DEFAULT);
            WxUtil.sendImageMessage(bytes, toAnyWay);
        }
    }

}
