package com.android.jdhshop.advistion;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.utils.AesUtils;
import com.android.jdhshop.utils.Base64Utils;
import com.android.jdhshop.utils.UIUtils;
import com.baidu.mobads.sdk.api.InterstitialAd;
import com.baidu.mobads.sdk.api.InterstitialAdListener;
import com.baidu.mobads.sdk.api.RequestParameters;
import com.baidu.mobads.sdk.api.RewardVideoAd;
import com.baidu.mobads.sdk.api.SplashAd;
import com.baidu.mobads.sdk.api.SplashInteractionListener;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdLoadType;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.kwad.sdk.api.KsAdSDK;
import com.kwad.sdk.api.KsEntryElement;
import com.kwad.sdk.api.KsFeedAd;
import com.kwad.sdk.api.KsInterstitialAd;
import com.kwad.sdk.api.KsLoadManager;
import com.kwad.sdk.api.KsRewardVideoAd;
import com.kwad.sdk.api.KsScene;
import com.kwad.sdk.api.KsSplashScreenAd;
import com.kwad.sdk.api.KsVideoPlayConfig;
import com.loopj.android.http.RequestParams;
import com.miui.zeus.mimo.sdk.TemplateAd;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeADUnifiedListener;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.ads.nativ.NativeUnifiedAD;
import com.qq.e.ads.nativ.NativeUnifiedADData;
import com.qq.e.ads.rewardvideo.RewardVideoAD;
import com.qq.e.ads.rewardvideo.RewardVideoADListener;
import com.qq.e.ads.rewardvideo.ServerSideVerificationOptions;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;
import com.vivo.ad.splash.SplashAdListener;
import com.vivo.ad.video.VideoAdListener;
import com.vivo.mobilead.BaseAdParams;
import com.vivo.mobilead.interstitial.InterstitialAdParams;
import com.vivo.mobilead.interstitial.VivoInterstitialAd;
import com.vivo.mobilead.listener.IAdListener;
import com.vivo.mobilead.splash.SplashAdParams;
import com.vivo.mobilead.splash.VivoSplashAd;
import com.vivo.mobilead.unified.base.AdParams;
import com.vivo.mobilead.unified.base.VideoPolicy;
import com.vivo.mobilead.unified.base.VivoAdError;
import com.vivo.mobilead.unified.nativead.UnifiedVivoNativeExpressAd;
import com.vivo.mobilead.unified.nativead.UnifiedVivoNativeExpressAdListener;
import com.vivo.mobilead.unified.nativead.VivoNativeExpressView;
import com.vivo.mobilead.video.VideoAdParams;
import com.vivo.mobilead.video.VivoVideoAd;

import org.greenrobot.greendao.annotation.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class JuDuoHuiAdvertisement {
    private final Activity mActivity;
    private JSONObject config;
    private InfomationAdListen listen;
    private RewardAdListen reward_listen;
    private SplashAdListen splash_listen;
    private ContentAdListen content_listen;
    private InsertScreenAdListen insert_screen_listen;
    private DisplayMetrics dm = new DisplayMetrics();
    private WindowManager wm;
    private String TAG = getClass().getSimpleName();
    private int adv_width = 0; //广告默认宽
    private int adv_height = 0; // 广告默认高

    public static int AD_CHANNEL_YLH = 1;
    public static int AD_CHANNEL_CSJ = 2;
    public static int AD_CHANNEL_BD = 3;
    public static int AD_CHANNEL_KS = 4;
    public static int AD_CHANNEL_VIVO = 5;
    public static int AD_CHANNEL_OPPO = 6;
    public static int AD_CHANNEL_MI = 7;
    public static int AD_CHANNEL_SIGMOB = 8;

    /* 错误CODE定义 */
    /** 用户未登陆 */
    public static int ERROR_CODE_USER_NOT_LOGIN = -8;
    /** 功能未实现 */
    public static int ERROR_CODE_NOT_ACHIEVE = -2;
    /** 无广告填充 */
    public static int ERROR_CODE_NO_ADS = -1;
    /** 广告请求上限 */
    public static int ERROR_CODE_REQUEST_LIMIT = 9001;

    private String rewardAdsExtra = "";
    private String uid = "0";
    private String order_id = "";
    private String verification = "";
    private String adv_unit = ""; // 激励视频广告收益单位

    public JuDuoHuiAdvertisement(Activity activity){
        mActivity = activity;
        listen = null;
        wm = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        if (wm!=null) wm.getDefaultDisplay().getMetrics(dm);
    }
    public JuDuoHuiAdvertisement(Activity activity,@Nullable InfomationAdListen infomationAdListen){
        mActivity = activity;
        listen = infomationAdListen;
        wm = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        if (wm!=null) wm.getDefaultDisplay().getMetrics(dm);
    }
    public JuDuoHuiAdvertisement setInfomationAdListen(InfomationAdListen infomationAdListen){
        listen = infomationAdListen;
        return this;
    }
    public JuDuoHuiAdvertisement setRewardAdListen(RewardAdListen rewardAdListen){
        reward_listen = rewardAdListen;
        return this;
    }
    public JuDuoHuiAdvertisement setRewardUnit(String advUnit){
        adv_unit = advUnit;
        return this;
    }
    public JuDuoHuiAdvertisement setSplashAdListen(SplashAdListen splashAdListen) { splash_listen = splashAdListen; return this; }
    public JuDuoHuiAdvertisement setInsertScreenListen(InsertScreenAdListen insertScreenListen){ insert_screen_listen = insertScreenListen; return this; }
    public JuDuoHuiAdvertisement setContentAdListen(@NotNull ContentAdListen contentAdListen){ content_listen = contentAdListen; return this; }

    // 信息流广告监听接口
    public interface InfomationAdListen {
        void click(View v); // 点击
        void dislike(); // 不喜欢
        void display(View v,String position,JSONObject current_adv_config); // 显示
        void displayed(); // 广告已展示
        void close(); // 关闭
        void error(JSONObject error); // 出错
    }
    // 激励视频广告监听接口
    public interface RewardAdListen{
        void click(JSONObject click_info); // 点击
        void skip(); // 点过
        void reward(); // 验证
        void close();
        void show(JSONObject adv_info); // 广告展示
        void start(); // 视频播放开始
        void end(); // 视频播放结束 或 图片广告计时结束
        void pause();
        void error(JSONObject error);// 出错 CODE = -2 未实现，-1 请求出错，其它根据各联盟返回
        void requested(); // 广告已请求成功
        void cached();// 已缓存

    }
    // 开屏广告监听 接口
    public interface SplashAdListen{
        void close();
        void click(JSONObject info);
        void display();
        void displayed();
        void error(JSONObject error);
    }
    // 内容位广告监听接口 * 无用
    public interface ContentAdListen{
        void display(View v,KsEntryElement kv);
        void error(JSONObject error);
        void click(View v,JSONObject info);
    }
    // 插屏广告监听接口
    public interface InsertScreenAdListen{
        void error(JSONObject error);
        void click(View v,JSONObject info);
        void displayed();
        void close();
        void closed();
        void videoStart();
        void videoEnd();
        void skip();
        void receive();
        void cached();
    }

    public static JuDuoHuiAdvertisement getInstance(Activity activity){
        return new JuDuoHuiAdvertisement(activity);
    }
    public static JuDuoHuiAdvertisement getInstance(Activity activity, InfomationAdListen infomationAdListen) {
        return new JuDuoHuiAdvertisement(activity, infomationAdListen);
    }

    /**
     * 返回下一个广告通道配置
     * @param adv_cfg_list 广告位置配置列表
     * @param adv_type 广告类型
     * @param adv_pos 广告位置
     * @return JSONObject
     */
    public JSONObject getNextChannel(JSONArray adv_cfg_list,String adv_type,String adv_pos){
        if (adv_cfg_list == null) return null;
        int last_adv_index = SPUtils.getIntData(mActivity,"last_adv_"+adv_type +"_"+adv_pos+"_channel",-1); // 7


        int[] channel_id = new int[adv_cfg_list.size()];
        for (int i=0;i<adv_cfg_list.size();i++){
            channel_id[i] = adv_cfg_list.getJSONObject(i).getIntValue("channel");
        }
        Arrays.sort(channel_id);


        int next_channel_index = 0;
        int has_in_channel_array = Arrays.binarySearch(channel_id,last_adv_index);

        Log.d(TAG, "getNextChannel: last_adv_index = " + last_adv_index + ", channel_id = " + Arrays.toString(channel_id) + ",,,,,has_in_channel_array = " + has_in_channel_array);
        // 最后的一次channel是-1或者最后存储的channel不在当前数组中，取数组中的随机值
        if (last_adv_index == -1 || has_in_channel_array <0){
            next_channel_index = new Random().nextInt(channel_id.length);
        }
        else{
            if (has_in_channel_array+1 >= channel_id.length){
                next_channel_index = 0;
            }
            else {
                next_channel_index = has_in_channel_array+1;
            }
        }
        // Log.d(TAG, "getNextChannel: " + last_adv_index );
        if (next_channel_index >= adv_cfg_list.size()) {
            next_channel_index = 0;
        }
         Log.d(TAG, "getNextChannel: last_adv_index=" + last_adv_index + " ,next_channel_index="+next_channel_index + ",广告渠道："+channel_id[next_channel_index]);
        SPUtils.saveIntData(mActivity,"last_adv_"+adv_type +"_"+adv_pos+"_channel",channel_id[next_channel_index]);
        for (int i=0;i<adv_cfg_list.size();i++){
            if (adv_cfg_list.getJSONObject(i).getIntValue("channel") == channel_id[next_channel_index]){
                return adv_cfg_list.getJSONObject(i);
            }
        }
        return null;
    }

    /**
     * 获取广告位，宽高单位DP
     * @param adv_config
     * @param current_position
     * @param advWidth
     * @param advHeight
     */
    public void getInfomationAdv(JSONArray adv_config,String current_position,int advWidth,int advHeight){
        if (!SPUtils.getBoolean(mActivity,"is_open_ad",false)) return;
        adv_width = advWidth;
        adv_height = advHeight;
        getInfomationAdv(adv_config,current_position);
    }
    /**
     *  ADV_CONFIG JSON必须元素
     *  channel,advertisement_id
     * */
    // 信息流广告
    public void getInfomationAdv(JSONArray adv_config,String current_position){
        if (!SPUtils.getBoolean(mActivity,"is_open_ad",false)) return;
        config = getNextChannel(adv_config,"infomation","article_list");
        if (config == null) {
            JSONObject err = new JSONObject();
            err.put("code","-1");
            err.put("msg","广告配置为空");
            err.put("position",current_position);
            listen.error(err);
            return;
        }
        // Log.d(TAG, "getInfomationAdv: " + config.toJSONString());
        try {
            int info_channel_id = config.getIntValue("channel");
            switch (info_channel_id) {
                case 1: // 优量汇
                    getInfomationAdv_YLH(current_position);
                    break;
                case 2: // 穿山甲
                    getInfomationAdv_CSJ(current_position);
                    break;
                case 3: // 快手联盟
                    getInfomationAdv_KS(current_position);
                    break;
                case 4: // 百度联盟
                    getInfomationAdv_BD(current_position);
                    break;
                case 5: // VIVO
                    getInfomationAdv_VIVO(current_position);
                    break;
                case 6: // OPPO
                    getInfomationAdv_OPPO(current_position);
                    break;
                case 7: // MI
                    getInfomationAdv_MI(current_position);
                    break;
            }
        }
        catch (Exception e){
            JSONObject err = new JSONObject();
            err.put("code","-1");
            err.put("msg","获取广告失败");
            err.put("position",current_position);
            listen.error(err);
        }
    }
    /**
     * 根据通道获取信息流广告
     * @param adv_config 广告配置
     * @param current_position 当前位置
     */
    public void getInfomationAdvWithChannel(JSONObject adv_config,String current_position){

        if (!SPUtils.getBoolean(mActivity,"is_open_ad",false)) return;
        config = adv_config;
        if (config == null) {
            JSONObject err = new JSONObject();
            err.put("code","-1");
            err.put("msg","广告配置为空");
            err.put("position",current_position);
            listen.error(err);
            return;
        }

        try {
            int info_channel_id = config.getIntValue("channel");
            switch (info_channel_id) {
                case 1: // 优量汇
                    getInfomationAdv_YLH(current_position);
                    break;
                case 2: // 穿山甲
                    getInfomationAdv_CSJ(current_position);
                    break;
                case 3: // 快手联盟
                    getInfomationAdv_KS(current_position);
                    break;
                case 4: // 百度联盟
                    getInfomationAdv_BD(current_position);
                    break;
                case 5: // VIVO
                    getInfomationAdv_VIVO(current_position);
                    break;
                case 6: // OPPO
                    getInfomationAdv_OPPO(current_position);
                    break;
                case 7: // MI
                    getInfomationAdv_MI(current_position);
                    break;
            }
        }
        catch (Exception e){
            JSONObject err = new JSONObject();
            err.put("code","-1");
            err.put("msg","获取广告失败");
            err.put("position",current_position);
            listen.error(err);
        }
    }
    // 插屏广告
    public void getInsertScreenAd(JSONArray adv_config){
        if (!SPUtils.getBoolean(mActivity,"is_open_ad",false)) return;
        config = getNextChannel(adv_config,"insert_screen_adv","insert_screen_adv");
        if (config == null) {
            JSONObject err = new JSONObject();
            err.put("code","-1");
            err.put("msg","广告配置为空");
            insert_screen_listen.error(err);
            return;
        }
        // Log.d(TAG, "getInsertScreenAd: " + config.toJSONString());
        formatUseData();
        try {
            int info_channel_id = config.getIntValue("channel");
            switch (info_channel_id) {
                case 1: // 优量汇
                    getInsertScreenAdv_YLH();
                    break;
                case 2: // 穿山甲
                    getInsertScreenAdv_CSJ();
                    break;
                case 3: // 快手联盟
                    getInsertScreenAdv_KS();
                    break;
                case 4: // 百度联盟
                    getInsertScreenAdv_BD();
                    break;
                case 5: // VIVO
                    getInsertScreenAdv_VIVO();
                    break;
                case 6: // OPPO
                    getInsertScreenAdv_OPPO();
                    break;
                case 7: // MI
                    getInsertScreenAdv_MI();
                    break;
            }
        }
        catch (Exception e){
            JSONObject err = new JSONObject();
            err.put("code","-1");
            err.put("msg","获取广告失败");
            insert_screen_listen.error(err);
        }
    }
    // 激励视频广告
    public void getRewardAdv(JSONArray adv_config){
        if (!SPUtils.getBoolean(mActivity,"is_open_ad",false)) return;
        config = getNextChannel(adv_config,"reward","reward");
        if (config == null) {
            JSONObject err = new JSONObject();
            err.put("code","-1");
            err.put("msg","广告配置为空");
            reward_listen.error(err);
            return;
        }
        // Log.d(TAG, "getInfomationAdv: " + config.toJSONString());

        formatUseData();
        try {
            int info_channel_id = config.getIntValue("channel");
            switch (info_channel_id) {
                case 1: // 优量汇
                    getRewardAdv_YLH();
                    break;
                case 2: // 穿山甲
                    getRewardAdv_CSJ();
                    break;
                case 3: // 快手联盟
                    getRewardAdv_KS();
                    break;
                case 4: // 百度联盟
                    getRewardAdv_BD();
                    break;
                case 5: // VIVO
                    getRewardAdv_VIVO();
                    break;
                case 6: // OPPO
                    getRewardAdv_OPPO();
                    break;
                case 7: // MI
                    getRewardAdv_MI();
                    break;
            }
        }
        catch (Exception e){
            JSONObject err = new JSONObject();
            err.put("code","-1");
            err.put("msg","获取广告失败");
            reward_listen.error(err);
        }
    }
    // 开屏广告
    public void getSplashAdv(JSONArray adv_config,ViewGroup display_contain){
        if (!SPUtils.getBoolean(mActivity,"is_open_ad",false)) return;
        config = getNextChannel(adv_config,"splash","splash");
        if (config == null) {
            JSONObject err = new JSONObject();
            err.put("code","-1");
            err.put("msg","广告配置为空");
            splash_listen.error(err);
            return;
        }
        // Log.d(TAG, "getInfomationAdv: " + config.toJSONString());
        try {
            int info_channel_id = config.getIntValue("channel");
            switch (info_channel_id) {
                case 1: // 优量汇
                    getSplashAdv_YLH(display_contain);
                    break;
                case 2: // 穿山甲
                    getSplashAdv_CSJ(display_contain);
                    break;
                case 3: // 快手联盟
                    getSplashAdv_KS(display_contain);
                    break;
                case 4: // 百度联盟
                    getSplashAdv_BD(display_contain);
                    break;
                case 5: // VIVO
                    getSplashAdv_VIVO(display_contain);
                    break;
                case 6: // OPPO
                    getSplashAdv_OPPO();
                    break;
                case 7: // MI
                    getSplashAdv_MI();
                    break;
                case 8:
                    getSplashAdv_SIGMOB();
                    break;

            }
        }
        catch (Exception e){
            JSONObject err = new JSONObject();
            err.put("code","-1");
            err.put("msg","获取广告失败");
            splash_listen.error(err);
        }
    }
    // BANNER广告
    public void getBannerAdv(JSONObject adv_config){

    }

    // 内容位广告位
    public void getMediaContentEntryAdv(JSONArray adv_config, String current_position){
        config = adv_config.getJSONObject(0); //getNextChannel(adv_config,"content_adv","content_adv");
        getMediaContentAdv_KS(current_position);
    }

    private void getMediaContentAdv_KS(String current_position){
        KsScene adScene = new KsScene.Builder(config.getLongValue("advertisement_id")).build();
        KsAdSDK.getLoadManager().loadEntryElement(adScene, new KsLoadManager.EntryElementListener<KsEntryElement>() {
            @Override
            public void onError(int i, String s) {
                JSONObject err = new JSONObject();
                err.put("code",i);
                err.put("msg",s);
                content_listen.error(err);
            }

            @Override
            public void onEntryLoad(@Nullable KsEntryElement ksEntryElement) {

                // Log.d(TAG, "onEntryLoad: ");

                if (ksEntryElement == null){
                    JSONObject err = new JSONObject();
                    err.put("code","-1");
                    err.put("msg","获取广告失败");
                    content_listen.error(err);
                    return;
                }
                View v = ksEntryElement.getEntryView(mActivity, new KsEntryElement.OnFeedClickListener() {
                    @Override
                    public void handleFeedClick(@KsEntryElement.EntranceType int entryViewType, int itemPosition,
                                                View itemView) {
                        JSONObject info = new JSONObject();
                        info.put("code",entryViewType);
                        info.put("code2",itemPosition);
                        content_listen.click(itemView,info);
                    }
                });

                content_listen.display(v,ksEntryElement);
            }
        },true);

    }

    /**
     * 获取优量汇信息流广告段
     * @param current_position
     */
    private void getInfomationAdv_YLH(String current_position){
        ADSize adSize;
        if (adv_width == 0 && adv_height ==0) {
            adSize = new ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT);
        }
        else
        {
            adSize = new ADSize(adv_width,adv_height);
        }
        NativeExpressAD ad = new NativeExpressAD(mActivity, adSize, config.getString("advertisement_id"), new NativeExpressAD.NativeExpressADListener() {
            @Override
            public void onADLoaded(List<NativeExpressADView> list) {
                LogUtils.d("onADLoaded: ");
                // 3.返回数据后，SDK 会返回可以用于展示 NativeExpressADView 列表
                NativeExpressADView ad_item = list.get(0);
                ad_item.render();
            }

            @Override
            public void onRenderFail(NativeExpressADView nativeExpressADView) {
                LogUtils.d("onRenderFail: ");
                JSONObject error = new JSONObject();
                error.put("code",-1);
                error.put("msg","渲染失败");
                error.put("position",current_position);
                listen.error(error);
            }

            @Override
            public void onRenderSuccess(NativeExpressADView nativeExpressADView) {
                listen.display(nativeExpressADView,current_position,config);
            }

            @Override
            public void onADExposure(NativeExpressADView nativeExpressADView) {
                LogUtils.d("onADExposure: ");
                listen.displayed();
            }

            @Override
            public void onADClicked(NativeExpressADView nativeExpressADView) {
                LogUtils.d("onADClicked1111: ");
                LogUtils.d(""+current_position.toString());
                LogUtils.d(""+config.toString());
                ckickAdToJdh();  // 优量汇信息流 点击广告时，请求服务器
//                {"ap_id":46,"watch_interval_time":0,"max_watch_num":99999,"am_id":36,"display":"position_6","channel":1,"advertisement_id":"9082738255114695","max_watch_video":99999,"type":5,"place_type":9,"get_place_count":0,"get_advertisement_count":0,"height":795}
                LogUtils.d("end");
                listen.click(nativeExpressADView);
            }

            @Override
            public void onADClosed(NativeExpressADView nativeExpressADView) {
                LogUtils.d("onADClosed: ");
                nativeExpressADView.destroy();
                listen.close();
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
            }
            // 2.设置监听器，监听广告状态
            @Override
            public void onNoAD(AdError adError) {
                JSONObject error = new JSONObject();
                error.put("code",adError.getErrorCode());
                error.put("msg",adError.getErrorMsg());
                error.put("position",current_position);
                listen.error(error);
            }
        });
        VideoOption.Builder videoOption = new VideoOption.Builder();
        videoOption.setAutoPlayMuted(true);
        videoOption.setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI);
        ad.setVideoOption(videoOption.build());
        ad.loadAD(1);
    }

    /**
     * 穿山甲 信息流
     * @param current_position
     */
    private void getInfomationAdv_CSJ(String current_position){
        TTAdNative mTTAdNative = TTAdSdk.getAdManager().createAdNative(mActivity);
        AdSlot.Builder adSlotBuilder = new AdSlot.Builder()
                .setCodeId(config.getString("advertisement_id")) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1); //请求广告数量为1到3条

        if (adv_width == 0 && adv_height == 0){
            adSlotBuilder.setExpressViewAcceptedSize(UIUtils.px2dp(dm.widthPixels), 0); //期望模板广告view的size,单位dp
        }
        else{
            adSlotBuilder.setExpressViewAcceptedSize(adv_width, 0); //期望模板广告view的size,单位dp
        }
        AdSlot adSlot = adSlotBuilder.build();

        mTTAdNative.loadNativeExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            //广告请求失败
            @Override
            public void onError(int code, String message) {
                LogUtils.d("onError475: " + message);
                JSONObject error = new JSONObject();
                error.put("code",code);
                error.put("msg",message);
                error.put("position",current_position);
                listen.error(error);
            }

            //广告请求成功
            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                LogUtils.d("onNativeExpressAdLoad: ");
                TTNativeExpressAd adTmp = ads.get(0);
                adTmp.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {
                        LogUtils.d("getChuanShanJia onAdClicked2: 广告被点击");
                        ckickAdToJdh();  // 穿山甲信息流 点击广告时，请求服务器
                        listen.click(view);
                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        LogUtils.d("onAdShow: 广告展示");
                        listen.displayed();

                    }
                    @Override
                    public void onRenderFail(View view, String msg, int code) {
                        LogUtils.d("onRenderFail: " + msg + "," + code);
                        JSONObject err = new JSONObject();
                        err.put("code",code);
                        err.put("msg",msg);
                        err.put("position",current_position);
                        listen.error(err);
                    }
                    @Override
                    public void onRenderSuccess(View view, float width, float height) {
                        LogUtils.d("getChuanShanJia onRenderSuccess: 广告渲染完成 ，" + width + "," + height);
                        listen.display(view,current_position,config);
                    }
                });
                adTmp.setDislikeCallback(mActivity, new TTAdDislike.DislikeInteractionCallback() {
                    @Override
                    public void onShow() {
                        listen.dislike();
                    }
                    @Override
                    public void onSelected(int i, String s, boolean b) {

                    }
                    @Override
                    public void onCancel() {
                        LogUtils.d(TAG, "onCancel: ");
                        adTmp.destroy();
                        listen.close();
                    }

                });

                adTmp.render();
            }
        });
    }

    /**
     * 快手信息流
     * @param current_position
     */
    private void getInfomationAdv_KS(String current_position){
        KsLoadManager ksLoadManager = KsAdSDK.getLoadManager();
        KsScene.Builder ksSceneBuilder = new KsScene.Builder(config.getLongValue("advertisement_id")).adNum(1);
        if (adv_width!=0 && adv_height !=0){
            ksSceneBuilder.width(adv_width).height(adv_height);
        }
        KsScene ksScene = ksSceneBuilder.build();
        ksLoadManager.loadConfigFeedAd(ksScene, new KsLoadManager.FeedAdListener() {
            @Override
            public void onError(int i, String s) {
                JSONObject err = new JSONObject();
                err.put("code",i);
                err.put("msg",s);
                err.put("position",current_position);
                listen.error(err);
            }

            @Override
            public void onFeedAdLoad(@Nullable List<KsFeedAd> list) {
                if (list== null ||list.size() == 0){
                    JSONObject err = new JSONObject();
                    err.put("code",40003);
                    err.put("msg","无广告数据");
                    err.put("position",current_position);
                    listen.error(err);
                }
                KsFeedAd ksFeedAd = list.get(0);
                ksFeedAd.setAdInteractionListener(new KsFeedAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked() {
                        listen.click(ksFeedAd.getFeedView(mActivity));
                        ckickAdToJdh();  // 快手信息流 点击广告时，请求服务器
                        // Log.d(TAG, "onAdClicked3: ");
                    }

                    @Override
                    public void onAdShow() {
                        listen.displayed();
                        // Log.d(TAG, "onAdShow: ");
                    }

                    @Override
                    public void onDislikeClicked() {
                        // Log.d(TAG, "onDislikeClicked: ");
                        listen.close();
                    }

                });
                listen.display(ksFeedAd.getFeedView(mActivity),current_position,config);
            }
        });
    }

    /**
     * 百度信息流
     * @param current_position
     */
    private void getInfomationAdv_BD(String current_position){
        JSONObject err= new JSONObject();
        err.put("code",-2);
        err.put("msg","BD信息流暂未实现");
        err.put("position",current_position);
        listen.error(err);
    }

    /**
     * VIVO信息流
     * @param current_position
     */
    private void getInfomationAdv_VIVO(String current_position){
        AdParams.Builder builder = new AdParams.Builder(config.getString("advertisement_id"));
        builder.setVideoPolicy(VideoPolicy.MANUAL);
        if (adv_width!=0 && adv_height!=0) {
            builder.setNativeExpressWidth(adv_width);
//            builder.setNativeExpressHegiht(adv_height);
        }
        UnifiedVivoNativeExpressAd unifiedVivoNativeExpressAd = new UnifiedVivoNativeExpressAd(mActivity, builder.build(), new UnifiedVivoNativeExpressAdListener() {
            @Override
            public void onAdReady(VivoNativeExpressView vivoNativeExpressView) {
                listen.display(vivoNativeExpressView,current_position,config);
            }

            @Override
            public void onAdFailed(VivoAdError vivoAdError) {
                JSONObject err= new JSONObject();
                err.put("code",vivoAdError.getCode());
                err.put("msg",vivoAdError.getMsg());
                err.put("position",current_position);
                listen.error(err);
            }

            @Override
            public void onAdClick(VivoNativeExpressView vivoNativeExpressView) {
                listen.click(vivoNativeExpressView);
                ckickAdToJdh();  // VIVO点击广告时，请求服务器
                // Log.d(TAG, "click4: 广告被点击了");
            }

            @Override
            public void onAdShow(VivoNativeExpressView vivoNativeExpressView) {
                // Log.d(TAG, "onAdShow: ");
                listen.displayed();
            }

            @Override
            public void onAdClose(VivoNativeExpressView vivoNativeExpressView) {
                vivoNativeExpressView.destroy();
                listen.close();
            }
        });
        unifiedVivoNativeExpressAd.loadAd();
    }

    /**
     * OPPO信息流
     * @param current_position
     */
    private void getInfomationAdv_OPPO(String current_position){
        JSONObject err= new JSONObject();
        err.put("code",-2);
        err.put("msg","OPPO暂未实现");
        err.put("position",current_position);
        listen.error(err);
    }

    /**
     * 小米信息流
     * @param current_position
     */
    private void getInfomationAdv_MI(String current_position){

        TemplateAd tad = new TemplateAd();
        tad.load(config.getString("advertisement_id"), new TemplateAd.TemplateAdLoadListener() {
            @Override
            public void onAdLoaded() {
                LinearLayout vv = new LinearLayout(mActivity);
                vv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                tad.show(vv, new TemplateAd.TemplateAdInteractionListener() {
                    @Override
                    public void onAdShow() {
                        listen.displayed();
                    }

                    @Override
                    public void onAdClick() {
                        listen.click(vv);
                        ckickAdToJdh();  // 小米信息流 点击广告时，请求服务器
                        // Log.d(TAG, "click5: 广告被点击了");
                    }

                    @Override
                    public void onAdDismissed() {
                        listen.close();
                    }

                    @Override
                    public void onAdRenderFailed(int i, String s) {
                        JSONObject err = new JSONObject();
                        err.put("code",i);
                        err.put("msg",s);
                        err.put("position",current_position);
                        listen.error(err);
                    }
                });
                listen.display(vv,current_position,config);
            }

            @Override
            public void onAdLoadFailed(int i, String s) {
                JSONObject err = new JSONObject();
                err.put("code",i);
                err.put("msg",s);
                err.put("position",current_position);
                listen.error(err);
            }
        });
    }
    //获取激励视频广告段
    RewardVideoAD rewardVideoAD;


    private void getRewardAdv_YLH(){
        rewardVideoAD = new com.qq.e.ads.rewardvideo.RewardVideoAD(mActivity, config.getString("advertisement_id"), new RewardVideoADListener() {
            @Override
            public void onADLoad() {
                LogUtils.d("onADLoad: ");
                reward_listen.requested();
            }

            @Override
            public void onVideoCached() {
                LogUtils.d("onVideoCached: ");
                reward_listen.cached();
                ckickAdToJdh();  // 优量汇激励视频 点击广告时，请求服务器
                rewardVideoAD.showAD(mActivity);
            }

            @Override
            public void onADShow(){
                LogUtils.d("onADShow: ");
                reward_listen.show(config);
            }

            @Override
            public void onADExpose() {
                LogUtils.d("onADExpose: ");
//                reward_listen.show();
            }

            @Override
            public void onReward(Map<String, Object> map) {
                reward_listen.reward();
            }

            @Override
            public void onADClick() {
                LogUtils.d("onADClick: ");
                JSONObject click_info = new JSONObject();
                click_info.put("channel",AD_CHANNEL_YLH);
                reward_listen.click(click_info);
            }

            @Override
            public void onVideoComplete() {
                LogUtils.d("onVideoComplete: ");
                reward_listen.end();
            }

            @Override
            public void onADClose() {
                reward_listen.close();
            }

            @Override
            public void onError(AdError adError) {
                JSONObject error = new JSONObject();
                error.put("code",adError.getErrorCode());
                error.put("msg",adError.getErrorMsg());
                error.put("channel",AD_CHANNEL_YLH);
                reward_listen.error(error);
            }
        });
//        rewardVideoAD.setExt("_1");
        ServerSideVerificationOptions.Builder serverSideVerificationOptions = new ServerSideVerificationOptions.Builder();
        String token = SPUtils.getStringData(mActivity, "token", "not_login");

        serverSideVerificationOptions.setUserId(token);
        serverSideVerificationOptions.setCustomData(rewardAdsExtra);
        rewardVideoAD.setServerSideVerificationOptions(serverSideVerificationOptions.build());
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
     * 格式化激励视频使用的信息
     */
    private void formatUseData() {
        uid = SPUtils.getStringData(mActivity, "uid","0");
        if (!"0".equals(uid)){
            order_id = calcOrderNo();
            verification = calcVerification();
            rewardAdsExtra = calcExtraData();

//            JSONObject error = new JSONObject();
//            error.put("code",ERROR_CODE_USER_NOT_LOGIN);
//            error.put("msg","用户未登陆");
//            reward_listen.error(error);
//            return;
        }
    }

    /**
     * 穿山甲-激励视频
     */
    private void getRewardAdv_CSJ(){
        // Log.d(TAG, "getRewardAdv_CSJ: ");
        TTAdNative mTTAdNative = TTAdSdk.getAdManager().createAdNative(mActivity);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(config.getString("advertisement_id"))
                .setUserID(SPUtils.getStringData(mActivity, "token", "not_login"))//tag_id
                .setMediaExtra(rewardAdsExtra) //附加参数
                .setOrientation(TTAdConstant.HORIZONTAL) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();
        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int i, String s) {
                // Log.d(TAG, "onError: ");
                JSONObject error = new JSONObject();
                error.put("code",i);
                error.put("msg",s);
                error.put("channel",AD_CHANNEL_CSJ);
                reward_listen.error(error);
            }

            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ttRewardVideoAd) {
                // Log.d(TAG, "onRewardVideoAdLoad: ");
//                ttRewardVideoAd.showRewardVideoAd(mActivity);
            }

            // 这个方法弃用了。
            @Override
            public void onRewardVideoCached() {}

            @Override
            public void onRewardVideoCached(TTRewardVideoAd ttRewardVideoAd) {
                    reward_listen.cached();
//                    reward_listen.show(config);
                    ttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {
                        @Override
                        public void onAdShow() {
                            // #BUG 不会调用这里
                            // Log.d(TAG, "onAdShow: ");
                            reward_listen.show(config);
                        }

                        @Override
                        public void onAdVideoBarClick() {
                            ckickAdToJdh();  // 穿山甲激励视频 点击广告时，请求服务器
                            // Log.d(TAG, "onAdVideoBarClick: ");
                            JSONObject click_info = new JSONObject();
                            click_info.put("channel",AD_CHANNEL_CSJ);
                            reward_listen.click(click_info);
                        }

                        @Override
                        public void onAdClose() {
                            reward_listen.close();
                            // Log.d(TAG, "onAdClose: ");
                        }

                        @Override
                        public void onVideoComplete() {
                            // Log.d(TAG, "onVideoComplete: ");
                            reward_listen.end();
                        }

                        @Override
                        public void onVideoError() {
                            // Log.d(TAG, "onVideoError: ");
                            JSONObject error = new JSONObject();
                            error.put("code",2);
                            error.put("msg","视频错误");
                            error.put("channel",AD_CHANNEL_CSJ);
                            reward_listen.error(error);
                        }

                        @Override
                        public void onRewardVerify(boolean b, int i, String s, int i1, String s1) {
                            // Log.d(TAG, "onRewardVerify: ");
                            reward_listen.reward();
                        }

                        @Override
                        public void onSkippedVideo() {
                            // Log.d(TAG, "onSkippedVideo: ");
                            reward_listen.skip();
                        }
                    });
                    ttRewardVideoAd.showRewardVideoAd(mActivity);
            }
        });
    }

    /**
     * 快手-激励视频
     */
    private void getRewardAdv_KS(){
        /**
         * 2021-09-08 09:41:12.894 28748-28748/com.android.jdhshop D/JuDuoHuiFragment: onRequestResult:  i = 1
         * 2021-09-08 09:41:13.262 28748-28748/com.android.jdhshop D/JuDuoHuiFragment: onRewardVideoAdLoad: 1
         * 2021-09-08 09:41:13.563 28748-28748/com.android.jdhshop D/JuDuoHuiFragment: onVideoPlayStart:
         * 2021-09-08 09:41:43.590 28748-28748/com.android.jdhshop D/JuDuoHuiFragment: onRewardVerify:
         * 2021-09-08 09:42:01.392 28748-28748/com.android.jdhshop D/JuDuoHuiFragment: onVideoPlayEnd:
         * 2021-09-08 09:42:06.840 28748-28748/com.android.jdhshop D/JuDuoHuiFragment: onPageDismiss:
         */
        KsLoadManager ksLoadManager = KsAdSDK.getLoadManager();
        KsScene ksScene = new KsScene.Builder(config.getLongValue("advertisement_id")).adNum(1).build();
        Map<String,String> extra_data = new HashMap<>();
        extra_data.put("userId",SPUtils.getStringData(mActivity,"token",""));
        extra_data.put("extraData",rewardAdsExtra);
        ksScene.setRewardCallbackExtraData(extra_data);

        ksLoadManager.loadRewardVideoAd(ksScene, new KsLoadManager.RewardVideoAdListener() {
            @Override
            public void onError(int i, String s) {
                LogUtils.d(TAG, "onVideoDownloadFailed: ");
                JSONObject error = new JSONObject();
                error.put("code",i);
                error.put("msg",s);
                error.put("channel",AD_CHANNEL_KS);
                reward_listen.error(error);
            }

            @Override
            public void onRequestResult(int i) {
                // Log.d(TAG, "onRequestResult: " + i);
                reward_listen.requested();
            }

            @Override
            public void onRewardVideoAdLoad(@Nullable List<KsRewardVideoAd> list) {

                if (list == null) {
                    LogUtils.d(TAG, "onRewardVideoAdLoad: 广告返回为空");
                    LogUtils.d(TAG, "onVideoDownloadFailed: ");
                    JSONObject error = new JSONObject();
                    error.put("code",-2);
                    error.put("msg","广告返回为空");
                    error.put("channel",AD_CHANNEL_KS);
                    reward_listen.error(error);
                }
                else {
                    LogUtils.d(TAG, "onRewardVideoAdLoad: " + list.size());
                    KsRewardVideoAd rewardVideoAd = list.get(0);
                    KsVideoPlayConfig ksVideoPlayConfig = new KsVideoPlayConfig.Builder().videoSoundEnable(true).showLandscape(false).build();
                    rewardVideoAd.setRewardAdInteractionListener(new KsRewardVideoAd.RewardAdInteractionListener() {
                        @Override
                        public void onAdClicked() {
                            ckickAdToJdh();  // 快手激励视频 点击广告时，请求服务器
                            LogUtils.d(TAG, "onAdClicked: ");
                            JSONObject click_info = new JSONObject();
                            click_info.put("channel",AD_CHANNEL_KS);
                            reward_listen.click(click_info);
                        }

                        @Override
                        public void onPageDismiss() {
                            reward_listen.close();
                        }

                        @Override
                        public void onVideoPlayError(int i, int i1) {
                            LogUtils.d(TAG, "onVideoPlayError: i="+i+",i1="+i1);
                            JSONObject error = new JSONObject();
                            error.put("code",-1);
                            error.put("msg","播放出错");
                            error.put("channel",AD_CHANNEL_KS);
                            reward_listen.error(error);
                        }

                        @Override
                        public void onVideoPlayEnd() {
                            LogUtils.d(TAG, "onVideoPlayEnd: ");
                            reward_listen.end();
                        }

                        @Override
                        public void onVideoSkipToEnd(long l) {
                            LogUtils.d(TAG, "onVideoSkipToEnd: " +l);
                            reward_listen.skip();
                        }

                        @Override
                        public void onVideoPlayStart() {
                            LogUtils.d(TAG, "onVideoPlayStart: ");
                            reward_listen.show(config);
                            reward_listen.start();

                        }

                        @Override
                        public void onRewardVerify() {
                            reward_listen.reward();
                        }


                    });
                    rewardVideoAd.showRewardVideoAd(mActivity,ksVideoPlayConfig);
                }

            }
        });
    }
    RewardVideoAd bd_rewardVideoAd = null;

    /**
     * 百度激励视频
     */
    private void getRewardAdv_BD(){
        // Log.d(TAG, "getRewardAdv_BD: ");
        bd_rewardVideoAd = new RewardVideoAd(mActivity, config.getString("advertisement_id"), new RewardVideoAd.RewardVideoAdListener() {
            @Override
            public void onAdShow() {
                LogUtils.d(TAG, "onAdShow: ");
                reward_listen.show(config);
            }

            @Override
            public void onAdClick() {
                ckickAdToJdh();  // 百度激励视频点击广告时，请求服务器
                LogUtils.d(TAG, "onAdClick: ");
                JSONObject click_info = new JSONObject();
                click_info.put("channel", AD_CHANNEL_BD);
                reward_listen.click(click_info);
            }

            @Override
            public void onAdClose(float v) {
                reward_listen.close();
            }

            @Override
            public void onAdFailed(String s) {
                JSONObject error = new JSONObject();
                error.put("code",2);
                error.put("msg",s);
                error.put("channel",AD_CHANNEL_BD);
                reward_listen.error(error);
            }

            @Override
            public void onVideoDownloadSuccess() {
                LogUtils.d(TAG, "onVideoDownloadSuccess: ");
                reward_listen.cached();
            }

            @Override
            public void onVideoDownloadFailed() {
                LogUtils.d(TAG, "onVideoDownloadFailed: ");
                JSONObject error = new JSONObject();
                error.put("code",3);
                error.put("msg","视频下载失败");
                error.put("channel",AD_CHANNEL_BD);
                reward_listen.error(error);
            }

            @Override
            public void playCompletion() {
                LogUtils.d(TAG, "playCompletion: ");
                reward_listen.end();
            }

            @Override
            public void onAdLoaded() {
                LogUtils.d(TAG, "onAdLoaded: ");
                bd_rewardVideoAd.show();
            }

            @Override
            public void onAdSkip(float v) {
                LogUtils.d(TAG, "onAdSkip: " + v);
                reward_listen.skip();
            }

            @Override
            public void onRewardVerify(boolean b) {
                reward_listen.reward();
            }
        });
        bd_rewardVideoAd.setExtraInfo(rewardAdsExtra);
        bd_rewardVideoAd.setUserId(SPUtils.getStringData(mActivity, "token", ""));
        bd_rewardVideoAd.load();
    }
    VivoVideoAd vivoVideoAd = null;

    /**
     * VIVO激励视频
     */
    private void getRewardAdv_VIVO(){
        // Log.d(TAG, "getRewardAdv_VIVO: ");
        BaseAdParams adParams = new VideoAdParams.Builder(config.getString("advertisement_id"))
                .setGameId(SPUtils.getStringData(mActivity, "token", ""))
                .setAdCount(1)
                .build();


        vivoVideoAd = new VivoVideoAd(mActivity, (VideoAdParams) adParams, new VideoAdListener() {
            @Override
            public void onAdLoad() {
                vivoVideoAd.showAd(mActivity);
                reward_listen.show(config);
            }

            @Override
            public void onAdFailed(String s) {
                JSONObject error = new JSONObject();
                error.put("code",2);
                error.put("msg",s);
                error.put("channel",AD_CHANNEL_VIVO);
                reward_listen.error(error);
            }

            @Override
            public void onVideoStart() {
                reward_listen.start();
            }

            @Override
            public void onVideoCompletion() {
                reward_listen.close();
            }

            @Override
            public void onVideoClose(int i) {
                reward_listen.close();
            }

            @Override
            public void onVideoCloseAfterComplete() {
                reward_listen.skip();
            }

            @Override
            public void onVideoError(String s) {
                JSONObject error = new JSONObject();
                error.put("code",1);
                error.put("msg",s);
                error.put("channel",AD_CHANNEL_VIVO);
                reward_listen.error(error);
            }

            @Override
            public void onFrequency() {
                // 请求过于频繁
                JSONObject error = new JSONObject();
                error.put("code",-1);
                error.put("msg","请求过于频繁");
                error.put("channel",AD_CHANNEL_VIVO);
                reward_listen.error(error);

            }

            @Override
            public void onNetError(String s) {
                JSONObject error = new JSONObject();
                error.put("code",-1);
                error.put("msg",s);
                error.put("channel",AD_CHANNEL_VIVO);
                reward_listen.error(error);
            }

            @Override
            public void onRequestLimit() {
                JSONObject error = new JSONObject();
                error.put("code",-3);
                error.put("msg","广告请求已达上限");
                error.put("channel",AD_CHANNEL_VIVO);
                reward_listen.error(error);
            }

            @Override
            public void onVideoCached() {
                reward_listen.cached();
            }

            @Override
            public void onRewardVerify() {
                reward_listen.reward();
            }
        });
        vivoVideoAd.loadAd();
    }

    private void getRewardAdv_OPPO(){
        // 暂未实现
        JSONObject error = new JSONObject();
        error.put("code",-2);
        error.put("msg","暂未实现");
        error.put("channel",AD_CHANNEL_OPPO);
        reward_listen.error(error);
    }

    /**
     * 小米激励视频
     */
    private void getRewardAdv_MI(){
        com.miui.zeus.mimo.sdk.RewardVideoAd rewardVideoAd = new com.miui.zeus.mimo.sdk.RewardVideoAd();
        rewardVideoAd.loadAd(config.getString("advertisement_id"), new com.miui.zeus.mimo.sdk.RewardVideoAd.RewardVideoLoadListener() {
            @Override
            public void onAdRequestSuccess() {
                Log.d(TAG, "onAdRequestSuccess: ");
                reward_listen.requested();
            }

            @Override
            public void onAdLoadSuccess() {
                rewardVideoAd.showAd(mActivity, new com.miui.zeus.mimo.sdk.RewardVideoAd.RewardVideoInteractionListener() {
                    @Override
                    public void onAdPresent() {
                        Log.d(TAG, "onAdPresent: ");
                        reward_listen.show(config);
                    }

                    @Override
                    public void onAdClick() {
                        Log.d(TAG, "onAdClick: ");
                        ckickAdToJdh();  // 小米激励视频点击广告时，请求服务器
                        JSONObject info = new JSONObject();
                        info.put("channel",AD_CHANNEL_MI);
                        reward_listen.click(info);
                    }

                    @Override
                    public void onAdDismissed() {
                        Log.d(TAG, "onAdDismissed: ");
                        reward_listen.close();
                    }

                    @Override
                    public void onAdFailed(String s) {
                        Log.d(TAG, "onAdFailed: ");
                        JSONObject error = new JSONObject();
                        error.put("code",-1);
                        error.put("msg",s);
                        error.put("channel",AD_CHANNEL_MI);
                        reward_listen.error(error);
                    }

                    @Override
                    public void onVideoStart() {
                        Log.d(TAG, "onVideoStart: ");
                        reward_listen.start();
                    }

                    @Override
                    public void onVideoPause() {
                        Log.d(TAG, "onVideoPause: ");
                        reward_listen.pause();
                    }

                    @Override
                    public void onVideoComplete() {
                        Log.d(TAG, "onVideoComplete: ");
                        reward_listen.end();
                    }

                    @Override
                    public void onPicAdEnd() {
                        Log.d(TAG, "onPicAdEnd: ");
                        reward_listen.end();
                    }

                    @Override
                    public void onReward() {
                        Log.d(TAG, "onReward: ");

                        // 这里进行模拟小米远端回调
                        //
                        reward_listen.reward();
                    }
                });
            }

            @Override
            public void onAdLoadFailed(int i, String s) {
                Log.d(TAG, "onAdLoadFailed: xiaomi " + i + ",,, " + s);
                JSONObject error = new JSONObject();
                error.put("code",i);
                error.put("msg",s);
                error.put("channel",AD_CHANNEL_MI);
                error.put("config",config);
                reward_listen.error(error);
            }
        });
    }

    /**
     * 优量汇 开屏广告段
     * @param display_contain
     */
    private void getSplashAdv_YLH(@NotNull ViewGroup display_contain){
        SplashAD splashAD = new SplashAD(mActivity, config.getString("advertisement_id"), new SplashADListener() {
            // 	广告关闭时调用，可能是用户关闭或者展示时间到。此时一般需要跳过开屏的 Activity，进入应用内容页面
            @Override
            public void onADDismissed() {
                // Log.d(TAG, "onADDismissed: ");
                splash_listen.close();
            }

            //广告加载失败，error 对象包含了错误码和错误信息，错误码的详细内容可以参考文档第5章
            @Override
            public void onNoAD(AdError adError) {
                // Log.d(TAG, "onNoAD: ");
                JSONObject error = new JSONObject();
                error.put("code",adError.getErrorCode());
                error.put("msg",adError.getErrorMsg());
                splash_listen.error(error);
            }

            // 广告成功展示时调用，成功展示不等于有效展示（比如广告容器高度不够）
            @Override
            public void onADPresent() {
                // Log.d(TAG, "onADPresent: ");
                splash_listen.display();

            }
            //广告被点击时调用，不代表满足计费条件（如点击时网络异常）
            @Override
            public void onADClicked() {
                LogUtils.d(TAG, "onADClicked: ");
                ckickAdToJdh();  // 优量汇开屏广告 点击广告时，请求服务器

                JSONObject info = new JSONObject();
                info.put("channel",AD_CHANNEL_CSJ);
                splash_listen.click(info);
            }
            //倒计时回调，返回广告还将被展示的剩余时间，单位是 ms
            @Override
            public void onADTick(long l) {
                // Log.d(TAG, "onADTick: ");
            }
            //广告曝光时调用
            @Override
            public void onADExposure() {
                // Log.d(TAG, "onADExposure: ");
                splash_listen.displayed();
            }

            //广告加载成功的回调，在fetchAdOnly的情况下，表示广告拉取成功可以显示了。
            @Override
            public void onADLoaded(long l) {
                // Log.d(TAG, "onADLoaded: ");
            }
        });
        splashAD.fetchAndShowIn(display_contain);
    }

    /**
     * 穿山甲 开屏广告
     * @param display_contain
     */
    private void getSplashAdv_CSJ(@NotNull ViewGroup display_contain){
        TTAdNative mTTAdNative = TTAdSdk.getAdManager().createAdNative(mActivity);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(config.getString("advertisement_id"))
                .setImageAcceptedSize(dm.widthPixels,dm.heightPixels-UIUtils.dp2px(50))
                .setIsAutoPlay(true)
                .setAdLoadType(TTAdLoadType.LOAD)
                .build();

        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            public void onError(int i, String s) {
                // Log.d(TAG, "onError: " + i + "," + s);
                JSONObject error = new JSONObject();
                error.put("code",i);
                error.put("msg",s);
                splash_listen.error(error);
            }

            @Override
            public void onTimeout() {
                // Log.d(TAG, "onTimeout: ");
                splash_listen.close();
            }

            @Override
            public void onSplashAdLoad(TTSplashAd ttSplashAd) {

                ttSplashAd.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int i) {
                        ckickAdToJdh();  // 穿山甲开屏广告 点击广告时，请求服务器
                        // Log.d(TAG, "onAdClicked: 1");
                        JSONObject info = new JSONObject();
                        info.put("channel",AD_CHANNEL_CSJ);
                        splash_listen.click(info);
                    }

                    @Override
                    public void onAdShow(View view, int i) {
                        // Log.d(TAG, "onAdShow: 1");
                        splash_listen.displayed();
                    }

                    @Override
                    public void onAdSkip() {
                        // Log.d(TAG, "onAdSkip: 1");
                        splash_listen.close();
                    }

                    @Override
                    public void onAdTimeOver() {
                        // Log.d(TAG, "onAdTimeOver: 1");
                        // 这里是加载完了
                        splash_listen.close();
                    }
                });

                display_contain.addView(ttSplashAd.getSplashView() ,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        });
    }

    /**
     * 快手 开屏广告
     * @param display_contain
     */
    private void getSplashAdv_KS(@NotNull ViewGroup display_contain){
        KsScene scene = new KsScene.Builder(config.getLongValue("advertisement_id")).posId(config.getLongValue("advertisement_id")).build();
        // Log.d(TAG, "getSplashAdv_KS: AppId = " + KsAdSDK.getAppId());
        KsAdSDK.getLoadManager().loadSplashScreenAd(scene, new KsLoadManager.SplashScreenAdListener() {
            @Override
            public void onError(int i, String s) {
                // Log.d(TAG, "onError: " + config.toJSONString());
                // Log.d(TAG, "getSplashAdv_KS onError: i="+i+",s="+s);
                JSONObject error = new JSONObject();
                error.put("code",i);
                error.put("msg",s);
                error.put("channel",AD_CHANNEL_KS);
                splash_listen.error(error);
            }

            @Override
            public void onRequestResult(int i) {
                // Log.d(TAG, "onRequestResult: " + i);
            }

            @Override
            public void onSplashScreenAdLoad(@Nullable KsSplashScreenAd ksSplashScreenAd) {
                View v = ksSplashScreenAd.getView(mActivity, new KsSplashScreenAd.SplashScreenAdInteractionListener() {
                    @Override
                    public void onAdClicked() {
                        ckickAdToJdh();  // 快手开屏广告 点击广告时，请求服务器
                        // Log.d(TAG, "onAdClicked: ");
                        JSONObject info = new JSONObject();
                        info.put("channel",AD_CHANNEL_KS);
                        splash_listen.click(info);
                    }

                    @Override
                    public void onAdShowError(int i, String s) {
                        // Log.d(TAG, "onAdShowError: " + i +",s=" +s);
                        JSONObject error = new JSONObject();
                        error.put("code",i);
                        error.put("msg",s);
                        error.put("channel",AD_CHANNEL_KS);
                        splash_listen.error(error);
                    }

                    @Override
                    public void onAdShowEnd() {
                        // Log.d(TAG, "onAdShowEnd: ");
                        splash_listen.close();
                    }

                    @Override
                    public void onAdShowStart() {
                        // Log.d(TAG, "onAdShowStart: ");
                        splash_listen.display();
                    }

                    @Override
                    public void onSkippedAd() {
                        // Log.d(TAG, "onSkippedAd: ");
                        splash_listen.close();;
                    }

                    @Override
                    public void onDownloadTipsDialogShow() {
                        // Log.d(TAG, "onDownloadTipsDialogShow: ");
                    }

                    @Override
                    public void onDownloadTipsDialogDismiss() {
                        // Log.d(TAG, "onDownloadTipsDialogDismiss: ");
                    }

                    @Override
                    public void onDownloadTipsDialogCancel() {
                        // Log.d(TAG, "onDownloadTipsDialogCancel: ");
                    }
                });
                display_contain.addView(v,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        });
    }
    SplashAd BD_splashAd;

    /**
     * 百度 开屏广告
     * @param display_contain
     */
    private void getSplashAdv_BD(@NotNull ViewGroup display_contain){
        RequestParameters requestParameters = new RequestParameters.Builder().build();
        BD_splashAd = new SplashAd(mActivity, config.getString("advertisement_id"),requestParameters, new SplashInteractionListener() {
            @Override
            public void onLpClosed() {
                // Log.d(TAG, "onLpClosed: ");
                splash_listen.close();
            }

            @Override
            public void onAdPresent() {
                // Log.d(TAG, "onAdPresent: ");
                splash_listen.displayed();
            }

            @Override
            public void onAdDismissed() {
                // Log.d(TAG, "onAdDismissed: ");
                splash_listen.close();
            }

            @Override
            public void onAdClick() {
                ckickAdToJdh();  // 百度 开屏广告 点击广告时，请求服务器
                // Log.d(TAG, "onAdClick: ");
                JSONObject info = new JSONObject();
                info.put("channel",AD_CHANNEL_BD);
                splash_listen.click(info);
            }

            @Override
            public void onADLoaded() {
                // Log.d(TAG, "onADLoaded: ");
                splash_listen.display();
//                BD_splashAd.show(display_contain);
            }

            @Override
            public void onAdFailed(String s) {
                // Log.d(TAG, "onAdFailed: " + s);
                JSONObject error = new JSONObject();
                error.put("code",5);
                error.put("msg",s);
                error.put("channel",AD_CHANNEL_BD);
                splash_listen.error(error);
//                BD_splashAd.load();
            }
        });

        BD_splashAd.loadAndShow(display_contain);
    }

    /**
     * VIVO开屏广告
     * @param display_contain
     */
    private void getSplashAdv_VIVO(@Nullable ViewGroup display_contain){
        VivoSplashAd vivoSplashAd = new VivoSplashAd(mActivity, new SplashAdListener() {
            @Override
            public void onADDismissed() {
                // Log.d(TAG, "onADDismissed: ");
                splash_listen.close();
            }

            @Override
            public void onNoAD(com.vivo.ad.model.AdError adError) {
                // Log.d(TAG, "onNoAD: " + adError);
                // 暂未实现
                JSONObject error = new JSONObject();
                error.put("code",adError.getErrorCode());
                error.put("msg",adError.getErrorMsg());
                error.put("channel",AD_CHANNEL_VIVO);
                splash_listen.error(error);
            }

            @Override
            public void onADPresent() {
                // Log.d(TAG, "onADPresent: ");
                splash_listen.displayed();
            }

            @Override
            public void onADClicked() {
                ckickAdToJdh();  // VIVO开屏广告 点击广告时，请求服务器
                // Log.d(TAG, "onADClicked: ");
                // 暂未实现
                JSONObject info = new JSONObject();
                info.put("channel",AD_CHANNEL_OPPO);
                splash_listen.click(info);
            }
        },new SplashAdParams.Builder(config.getString("advertisement_id")).setAppTitle("聚多惠").build());
        vivoSplashAd.loadAd();
    }

    /**
     * OPPO开屏广告
     */
    private void getSplashAdv_OPPO(){
        // 暂未实现
        JSONObject error = new JSONObject();
        error.put("code",-2);
        error.put("msg","暂未实现");
        error.put("channel",AD_CHANNEL_OPPO);
        splash_listen.error(error);
    }
    // 小米不用，先建上
    private void getSplashAdv_MI(){
        // 暂未实现
        JSONObject error = new JSONObject();
        error.put("code",-2);
        error.put("msg","暂未实现");
        error.put("channel",AD_CHANNEL_MI);
        splash_listen.error(error);
    }
    private void getSplashAdv_SIGMOB(){
        JSONObject error = new JSONObject();
        error.put("code",-2);
        error.put("msg","暂未实现");
        error.put("channel",AD_CHANNEL_SIGMOB);
        splash_listen.error(error);
    }

    /* 插屏广告 */
    // 快手渠道插屏
    private void getInsertScreenAdv_KS(){
        KsScene ksScene = new KsScene.Builder(config.getLong("advertisement_id")).build();
        KsAdSDK.getLoadManager().loadInterstitialAd(ksScene, new KsLoadManager.InterstitialAdListener() {
            @Override
            public void onError(int i, String s) {
                JSONObject err = new JSONObject();
                err.put("code",i);
                err.put("msg",s);
                insert_screen_listen.error(err);
            }

            @Override
            public void onRequestResult(int i) {
                // Log.d(TAG, "onRequestResult: " + i);
            }

            @Override
            public void onInterstitialAdLoad(@Nullable List<KsInterstitialAd> list) {
                if (list!=null && list.size()>0) {
                    KsInterstitialAd kad = list.get(0);
                    kad.setAdInteractionListener(new KsInterstitialAd.AdInteractionListener() {
                        @Override
                        public void onAdClicked() {
                            ckickAdToJdh();
                            insert_screen_listen.click(null,config);
                        }

                        @Override
                        public void onAdShow() {
                            insert_screen_listen.displayed();
                        }

                        @Override
                        public void onAdClosed() {
                            insert_screen_listen.close();
                        }

                        @Override
                        public void onPageDismiss() {
                            insert_screen_listen.closed();
                        }

                        @Override
                        public void onVideoPlayError(int i, int i1) {
                            JSONObject err = new JSONObject();
                            err.put("code",i);
                            err.put("code2",i1);
                            err.put("msg","error");
                            insert_screen_listen.error(err);
                        }

                        @Override
                        public void onVideoPlayEnd() {
                            insert_screen_listen.videoEnd();
                        }

                        @Override
                        public void onVideoPlayStart() {
                            insert_screen_listen.videoStart();
                        }

                        @Override
                        public void onSkippedAd() {
                            insert_screen_listen.skip();
                        }
                    });
                    KsVideoPlayConfig ks_video_config = new KsVideoPlayConfig.Builder().videoSoundEnable(true).build();
                    kad.showInterstitialAd(mActivity,ks_video_config);
                }
            }
        });
    }
    // 穿山甲渠道插屏
    private void getInsertScreenAdv_CSJ(){
        TTAdNative ttNativeAd = TTAdSdk.getAdManager().createAdNative(mActivity);
        AdSlot adSlot = new AdSlot.Builder().setCodeId(config.getString("advertisement_id")).setExpressViewAcceptedSize((float) (UIUtils.px2dp(UIUtils.getScreenMeasuredWidth(mActivity))*0.9), (float) (UIUtils.px2dp(UIUtils.getScreenMeasuredHeight(mActivity))*0.6)).build();
        ttNativeAd.loadInteractionExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int i, String s) {
                // Log.d(TAG, "onError: i = " + i + ",s = " + s);
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> list) {
                // Log.d(TAG, "onNativeExpressAdLoad: " + list.size());
                TTNativeExpressAd ttNativeExpressAd = list.get(0);
                ttNativeExpressAd.setDislikeCallback(mActivity, new TTAdDislike.DislikeInteractionCallback() {
                    @Override
                    public void onShow() {
                        // Log.d(TAG, "onShow: ");
                    }

                    @Override
                    public void onSelected(int i, String s, boolean b) {
                        // Log.d(TAG, "onSelected: ");
                    }

                    @Override
                    public void onCancel() {
                        // Log.d(TAG, "onCancel: ");
                        insert_screen_listen.close();
                    }
                });
                ttNativeExpressAd.setVideoAdListener(new TTNativeExpressAd.ExpressVideoAdListener() {
                    @Override
                    public void onVideoLoad() {
                        insert_screen_listen.cached();
                    }

                    @Override
                    public void onVideoError(int i, int i1) {
                        JSONObject err = new JSONObject();
                        err.put("code",i);
                        err.put("code2",i1);
                        err.put("msg","视频播放失败");
                        insert_screen_listen.error(err);
                    }

                    @Override
                    public void onVideoAdStartPlay() {
                        insert_screen_listen.videoStart();
                    }

                    @Override
                    public void onVideoAdPaused() {

                    }

                    @Override
                    public void onVideoAdContinuePlay() {

                    }

                    @Override
                    public void onProgressUpdate(long l, long l1) {

                    }

                    @Override
                    public void onVideoAdComplete() {
                        insert_screen_listen.videoEnd();
                    }

                    @Override
                    public void onClickRetry() {

                    }
                });
                ttNativeExpressAd.setExpressInteractionListener(new TTNativeExpressAd.AdInteractionListener() {
                    @Override
                    public void onAdDismiss() {
                        // Log.d(TAG, "onAdDismiss: ");
                        insert_screen_listen.closed();
                    }

                    @Override
                    public void onAdClicked(View view, int i) {
                        // Log.d(TAG, "onAdClicked: ");
                        ckickAdToJdh();
                        insert_screen_listen.click(view,config);
                    }

                    @Override
                    public void onAdShow(View view, int i) {
                        // Log.d(TAG, "onAdShow: ");
                        insert_screen_listen.displayed();
                    }

                    @Override
                    public void onRenderFail(View view, String s, int i) {
                        // Log.d(TAG, "onRenderFail: ");
                        JSONObject err = new JSONObject();
                        err.put("code",i);
                        err.put("msg",s);
                        insert_screen_listen.error(err);
                    }

                    @Override
                    public void onRenderSuccess(View view, float v, float v1) {
                        // Log.d(TAG, "onRenderSuccess: ");
                        ttNativeExpressAd.showInteractionExpressAd(mActivity);
                    }
                });
                ttNativeExpressAd.render();

            }
        });
    }
    // 优量汇渠道插屏
    UnifiedInterstitialAD ylh_unified_interstitial_ad;
    private void getInsertScreenAdv_YLH(){
        ylh_unified_interstitial_ad = new UnifiedInterstitialAD(mActivity, config.getString("advertisement_id"), new UnifiedInterstitialADListener() {
            @Override
            public void onADReceive() {
                // Log.d(TAG, "onADReceive: ");
                insert_screen_listen.receive();
            }

            @Override
            public void onVideoCached() {
                // Log.d(TAG, "onVideoCached: ");
                insert_screen_listen.cached();
            }

            @Override
            public void onNoAD(AdError adError) {
                // Log.d(TAG, "onNoAD: ");
                JSONObject err = new JSONObject();
                err.put("code",adError.getErrorCode());
                err.put("msg",adError.getErrorMsg());
                insert_screen_listen.error(err);
            }

            @Override
            public void onADOpened() {
                // Log.d(TAG, "onADOpened: ");
                insert_screen_listen.displayed();
            }

            @Override
            public void onADExposure() {
                // Log.d(TAG, "onADExposure: ");
            }

            @Override
            public void onADClicked() {
                // Log.d(TAG, "onADClicked: ");
                ckickAdToJdh();
                insert_screen_listen.click(null,config);
            }

            @Override
            public void onADLeftApplication() {
                // Log.d(TAG, "onADLeftApplication: ");

            }

            @Override
            public void onADClosed() {
                // Log.d(TAG, "onADClosed: ");
                insert_screen_listen.closed();
            }

            @Override
            public void onRenderSuccess() {
                // Log.d(TAG, "onRenderSuccess: ");
                ylh_unified_interstitial_ad.show();
            }

            @Override
            public void onRenderFail() {
                // Log.d(TAG, "onRenderFail: ");
                JSONObject err = new JSONObject();
                err.put("code",9);
                err.put("msg","广告渲染失败");
                insert_screen_listen.error(err);
            }
        });
        ylh_unified_interstitial_ad.loadAD();
    }
    // 百度渠道插屏
    private void getInsertScreenAdv_BD(){
        InterstitialAd interstitialAd = new InterstitialAd(mActivity,config.getString("advertisement_id"));
        interstitialAd.setListener(new InterstitialAdListener() {
            @Override
            public void onAdReady() {
                interstitialAd.showAd();
            }

            @Override
            public void onAdPresent() {
                // Log.d(TAG, "onAdPresent: ");
                insert_screen_listen.displayed();
            }

            @Override
            public void onAdClick(InterstitialAd interstitialAd) {
                // Log.d(TAG, "onAdClick: ");
                ckickAdToJdh();
                insert_screen_listen.click(null,config);
            }

            @Override
            public void onAdDismissed() {
                // Log.d(TAG, "onAdDismissed: ");
                insert_screen_listen.closed();
            }

            @Override
            public void onAdFailed(String s) {
                // Log.d(TAG, "onAdFailed: " + s);
                JSONObject err = new JSONObject();
                err.put("code",9);
                err.put("msg",s);
                insert_screen_listen.error(err);
            }
        });
        interstitialAd.loadAd();

    }
    // VIVO渠道插屏
    VivoInterstitialAd vivoInterstitialAd;
    private void getInsertScreenAdv_VIVO(){
        InterstitialAdParams load_param = new InterstitialAdParams.Builder(config.getString("advertisement_id")).build();
        vivoInterstitialAd = new VivoInterstitialAd(mActivity, load_param, new IAdListener() {
            @Override
            public void onAdShow() {
                insert_screen_listen.displayed();
            }

            @Override
            public void onAdFailed(com.vivo.mobilead.model.VivoAdError vivoAdError) {
                JSONObject err = new JSONObject();
                err.put("code",vivoAdError.getErrorCode());
                err.put("msg",vivoAdError.getErrorMsg());
                insert_screen_listen.error(err);
            }

            @Override
            public void onAdReady() {
                vivoInterstitialAd.showAd();
            }

            @Override
            public void onAdClick() {
                ckickAdToJdh();
                insert_screen_listen.click(null,config);
            }

            @Override
            public void onAdClosed() {
                insert_screen_listen.closed();
            }
        });
        vivoInterstitialAd.load();
    }
    // OPPO渠道插屏
    private void getInsertScreenAdv_OPPO(){
        JSONObject err = new JSONObject();
        err.put("code",-2);
        err.put("msg","OPPO渠道暂未接入");
        insert_screen_listen.error(err);
    }
    // MI渠道插屏
    private void getInsertScreenAdv_MI(){
        com.miui.zeus.mimo.sdk.InterstitialAd interstitialAd = new com.miui.zeus.mimo.sdk.InterstitialAd();
        interstitialAd.loadAd("ebc4e8d5278e4c1407853f3ad9d64e61", new com.miui.zeus.mimo.sdk.InterstitialAd.InterstitialAdLoadListener() {
            @Override
            public void onAdLoadSuccess() {
                interstitialAd.show(mActivity, new com.miui.zeus.mimo.sdk.InterstitialAd.InterstitialAdInteractionListener() {
                    @Override
                    public void onAdClick() {
                        ckickAdToJdh();
                        insert_screen_listen.click(null,config);
                    }

                    @Override
                    public void onAdShow() {
                        insert_screen_listen.displayed();
                    }

                    @Override
                    public void onAdClosed() {
                        insert_screen_listen.closed();
                    }

                    @Override
                    public void onRenderFail(int i, String s) {
                        // Log.d(TAG, "onRenderFail: "+i+","+s);
                        JSONObject err = new JSONObject();
                        err.put("code",i);
                        err.put("msg",s);
                        insert_screen_listen.error(err);
                    }

                    @Override
                    public void onVideoStart() {
                        insert_screen_listen.videoStart();
                    }

                    @Override
                    public void onVideoPause() {

                    }

                    @Override
                    public void onVideoResume() {

                    }

                    @Override
                    public void onVideoEnd() {
                        insert_screen_listen.videoEnd();
                    }
                });
            }

            @Override
            public void onAdLoadFailed(int i, String s) {
                // Log.d(TAG, "onAdLoadFailed: i="+i+",s="+s);
                JSONObject err = new JSONObject();
                err.put("code",i);
                err.put("msg",s);
                insert_screen_listen.error(err);
            }
        });
    }
    public String getOrderId(){ return order_id; }
    public String getVerification(){ return verification; }
    public String getExtraData(){ return rewardAdsExtra; }

    private String calcOrderNo(long timestamp){
        return uid + timestamp;
    }

    private String calcOrderNo(){
        long timestamp = System.currentTimeMillis();
        return calcOrderNo(timestamp);
    }
    private String calcVerification(){
        // Log.d(TAG, "getVerification: " + order_id +"," + uid +"," + config.getString("am_id"));
        return AesUtils.md532(order_id + uid + config.getString("am_id"));
    }
    private String calcExtraData(){
        //base64_encode($orderNo.'(-o-o-)'.$verification.'(-o-o-)'.$uid);
        String tmp = order_id + "(-o-o-)" + verification + "(-o-o-)" + uid;
        return Base64Utils.encode(tmp.getBytes(StandardCharsets.UTF_8));
    }
    /**
     * 点击广告时，请求服务器
     */
    private void ckickAdToJdh(){
        RequestParams req = new RequestParams();
        String token = SPUtils.getStringData(mActivity,"token","");
        uid = SPUtils.getStringData(mActivity, "uid","0"); // 用的是用户ID
        req.put("token",token);
        req.put("am_id",config.getString("am_id"));
        long timestamp = System.currentTimeMillis();
        if(order_id == null || order_id == ""){ // 激励视频的用播放的订单号
            order_id = calcOrderNo(timestamp);
        }

        req.put("orderNo", order_id); // 自定义生成订单号
        req.put("item", CaiNiaoApplication.getInstances().getOaid()); //设备号参数
        verification = calcVerification(); // 获取订单签名
        req.put("verification",verification);
        req.put("ap_id",config.getString("ap_id"));
        req.put("place_type",config.getString("place_type"));

        // 生成签名t
        JSONObject json_obj = new JSONObject();
        json_obj.put("orderNo",order_id);
        json_obj.put("token",token);
        json_obj.put("timestamp", timestamp); // 毫秒，与签到的签名不一样（签到用的秒）
        req.put("t",AesUtils.encrypt(json_obj.toJSONString()));
        LogUtils.d("111: " + req.toString());

        HttpUtils.post(Constants.APP_IP + "/api/UserHuisign/otherGetClick", mActivity,req, new HttpUtils.TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogUtils.d("onFailure: " + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("onSuccess: " + responseString);
            }
        });
    }

    public void setNativeAdvListen(){

    }
    // 自渲染广告
    public void getNativeAdv(){
        NativeUnifiedAD unifiedAD = new NativeUnifiedAD(mActivity, "", new NativeADUnifiedListener() {
            @Override
            public void onADLoaded(List<NativeUnifiedADData> list) {

            }

            @Override
            public void onNoAD(AdError adError) {

            }
        });
    }
}
