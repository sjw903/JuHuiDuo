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
    private int adv_width = 0; //???????????????
    private int adv_height = 0; // ???????????????

    public static int AD_CHANNEL_YLH = 1;
    public static int AD_CHANNEL_CSJ = 2;
    public static int AD_CHANNEL_BD = 3;
    public static int AD_CHANNEL_KS = 4;
    public static int AD_CHANNEL_VIVO = 5;
    public static int AD_CHANNEL_OPPO = 6;
    public static int AD_CHANNEL_MI = 7;
    public static int AD_CHANNEL_SIGMOB = 8;

    /* ??????CODE?????? */
    /** ??????????????? */
    public static int ERROR_CODE_USER_NOT_LOGIN = -8;
    /** ??????????????? */
    public static int ERROR_CODE_NOT_ACHIEVE = -2;
    /** ??????????????? */
    public static int ERROR_CODE_NO_ADS = -1;
    /** ?????????????????? */
    public static int ERROR_CODE_REQUEST_LIMIT = 9001;

    private String rewardAdsExtra = "";
    private String uid = "0";
    private String order_id = "";
    private String verification = "";
    private String adv_unit = ""; // ??????????????????????????????

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

    // ???????????????????????????
    public interface InfomationAdListen {
        void click(View v); // ??????
        void dislike(); // ?????????
        void display(View v,String position,JSONObject current_adv_config); // ??????
        void displayed(); // ???????????????
        void close(); // ??????
        void error(JSONObject error); // ??????
    }
    // ??????????????????????????????
    public interface RewardAdListen{
        void click(JSONObject click_info); // ??????
        void skip(); // ??????
        void reward(); // ??????
        void close();
        void show(JSONObject adv_info); // ????????????
        void start(); // ??????????????????
        void end(); // ?????????????????? ??? ????????????????????????
        void pause();
        void error(JSONObject error);// ?????? CODE = -2 ????????????-1 ??????????????????????????????????????????
        void requested(); // ?????????????????????
        void cached();// ?????????

    }
    // ?????????????????? ??????
    public interface SplashAdListen{
        void close();
        void click(JSONObject info);
        void display();
        void displayed();
        void error(JSONObject error);
    }
    // ??????????????????????????? * ??????
    public interface ContentAdListen{
        void display(View v,KsEntryElement kv);
        void error(JSONObject error);
        void click(View v,JSONObject info);
    }
    // ????????????????????????
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
     * ?????????????????????????????????
     * @param adv_cfg_list ????????????????????????
     * @param adv_type ????????????
     * @param adv_pos ????????????
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
        // ???????????????channel???-1?????????????????????channel????????????????????????????????????????????????
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
         Log.d(TAG, "getNextChannel: last_adv_index=" + last_adv_index + " ,next_channel_index="+next_channel_index + ",???????????????"+channel_id[next_channel_index]);
        SPUtils.saveIntData(mActivity,"last_adv_"+adv_type +"_"+adv_pos+"_channel",channel_id[next_channel_index]);
        for (int i=0;i<adv_cfg_list.size();i++){
            if (adv_cfg_list.getJSONObject(i).getIntValue("channel") == channel_id[next_channel_index]){
                return adv_cfg_list.getJSONObject(i);
            }
        }
        return null;
    }

    /**
     * ??????????????????????????????DP
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
     *  ADV_CONFIG JSON????????????
     *  channel,advertisement_id
     * */
    // ???????????????
    public void getInfomationAdv(JSONArray adv_config,String current_position){
        if (!SPUtils.getBoolean(mActivity,"is_open_ad",false)) return;
        config = getNextChannel(adv_config,"infomation","article_list");
        if (config == null) {
            JSONObject err = new JSONObject();
            err.put("code","-1");
            err.put("msg","??????????????????");
            err.put("position",current_position);
            listen.error(err);
            return;
        }
        // Log.d(TAG, "getInfomationAdv: " + config.toJSONString());
        try {
            int info_channel_id = config.getIntValue("channel");
            switch (info_channel_id) {
                case 1: // ?????????
                    getInfomationAdv_YLH(current_position);
                    break;
                case 2: // ?????????
                    getInfomationAdv_CSJ(current_position);
                    break;
                case 3: // ????????????
                    getInfomationAdv_KS(current_position);
                    break;
                case 4: // ????????????
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
            err.put("msg","??????????????????");
            err.put("position",current_position);
            listen.error(err);
        }
    }
    /**
     * ?????????????????????????????????
     * @param adv_config ????????????
     * @param current_position ????????????
     */
    public void getInfomationAdvWithChannel(JSONObject adv_config,String current_position){

        if (!SPUtils.getBoolean(mActivity,"is_open_ad",false)) return;
        config = adv_config;
        if (config == null) {
            JSONObject err = new JSONObject();
            err.put("code","-1");
            err.put("msg","??????????????????");
            err.put("position",current_position);
            listen.error(err);
            return;
        }

        try {
            int info_channel_id = config.getIntValue("channel");
            switch (info_channel_id) {
                case 1: // ?????????
                    getInfomationAdv_YLH(current_position);
                    break;
                case 2: // ?????????
                    getInfomationAdv_CSJ(current_position);
                    break;
                case 3: // ????????????
                    getInfomationAdv_KS(current_position);
                    break;
                case 4: // ????????????
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
            err.put("msg","??????????????????");
            err.put("position",current_position);
            listen.error(err);
        }
    }
    // ????????????
    public void getInsertScreenAd(JSONArray adv_config){
        if (!SPUtils.getBoolean(mActivity,"is_open_ad",false)) return;
        config = getNextChannel(adv_config,"insert_screen_adv","insert_screen_adv");
        if (config == null) {
            JSONObject err = new JSONObject();
            err.put("code","-1");
            err.put("msg","??????????????????");
            insert_screen_listen.error(err);
            return;
        }
        // Log.d(TAG, "getInsertScreenAd: " + config.toJSONString());
        formatUseData();
        try {
            int info_channel_id = config.getIntValue("channel");
            switch (info_channel_id) {
                case 1: // ?????????
                    getInsertScreenAdv_YLH();
                    break;
                case 2: // ?????????
                    getInsertScreenAdv_CSJ();
                    break;
                case 3: // ????????????
                    getInsertScreenAdv_KS();
                    break;
                case 4: // ????????????
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
            err.put("msg","??????????????????");
            insert_screen_listen.error(err);
        }
    }
    // ??????????????????
    public void getRewardAdv(JSONArray adv_config){
        if (!SPUtils.getBoolean(mActivity,"is_open_ad",false)) return;
        config = getNextChannel(adv_config,"reward","reward");
        if (config == null) {
            JSONObject err = new JSONObject();
            err.put("code","-1");
            err.put("msg","??????????????????");
            reward_listen.error(err);
            return;
        }
        // Log.d(TAG, "getInfomationAdv: " + config.toJSONString());

        formatUseData();
        try {
            int info_channel_id = config.getIntValue("channel");
            switch (info_channel_id) {
                case 1: // ?????????
                    getRewardAdv_YLH();
                    break;
                case 2: // ?????????
                    getRewardAdv_CSJ();
                    break;
                case 3: // ????????????
                    getRewardAdv_KS();
                    break;
                case 4: // ????????????
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
            err.put("msg","??????????????????");
            reward_listen.error(err);
        }
    }
    // ????????????
    public void getSplashAdv(JSONArray adv_config,ViewGroup display_contain){
        if (!SPUtils.getBoolean(mActivity,"is_open_ad",false)) return;
        config = getNextChannel(adv_config,"splash","splash");
        if (config == null) {
            JSONObject err = new JSONObject();
            err.put("code","-1");
            err.put("msg","??????????????????");
            splash_listen.error(err);
            return;
        }
        // Log.d(TAG, "getInfomationAdv: " + config.toJSONString());
        try {
            int info_channel_id = config.getIntValue("channel");
            switch (info_channel_id) {
                case 1: // ?????????
                    getSplashAdv_YLH(display_contain);
                    break;
                case 2: // ?????????
                    getSplashAdv_CSJ(display_contain);
                    break;
                case 3: // ????????????
                    getSplashAdv_KS(display_contain);
                    break;
                case 4: // ????????????
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
            err.put("msg","??????????????????");
            splash_listen.error(err);
        }
    }
    // BANNER??????
    public void getBannerAdv(JSONObject adv_config){

    }

    // ??????????????????
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
                    err.put("msg","??????????????????");
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
     * ?????????????????????????????????
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
                // 3.??????????????????SDK ??????????????????????????? NativeExpressADView ??????
                NativeExpressADView ad_item = list.get(0);
                ad_item.render();
            }

            @Override
            public void onRenderFail(NativeExpressADView nativeExpressADView) {
                LogUtils.d("onRenderFail: ");
                JSONObject error = new JSONObject();
                error.put("code",-1);
                error.put("msg","????????????");
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
                ckickAdToJdh();  // ?????????????????? ?????????????????????????????????
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
            // 2.????????????????????????????????????
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
     * ????????? ?????????
     * @param current_position
     */
    private void getInfomationAdv_CSJ(String current_position){
        TTAdNative mTTAdNative = TTAdSdk.getAdManager().createAdNative(mActivity);
        AdSlot.Builder adSlotBuilder = new AdSlot.Builder()
                .setCodeId(config.getString("advertisement_id")) //?????????id
                .setSupportDeepLink(true)
                .setAdCount(1); //?????????????????????1???3???

        if (adv_width == 0 && adv_height == 0){
            adSlotBuilder.setExpressViewAcceptedSize(UIUtils.px2dp(dm.widthPixels), 0); //??????????????????view???size,??????dp
        }
        else{
            adSlotBuilder.setExpressViewAcceptedSize(adv_width, 0); //??????????????????view???size,??????dp
        }
        AdSlot adSlot = adSlotBuilder.build();

        mTTAdNative.loadNativeExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            //??????????????????
            @Override
            public void onError(int code, String message) {
                LogUtils.d("onError475: " + message);
                JSONObject error = new JSONObject();
                error.put("code",code);
                error.put("msg",message);
                error.put("position",current_position);
                listen.error(error);
            }

            //??????????????????
            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                LogUtils.d("onNativeExpressAdLoad: ");
                TTNativeExpressAd adTmp = ads.get(0);
                adTmp.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {
                        LogUtils.d("getChuanShanJia onAdClicked2: ???????????????");
                        ckickAdToJdh();  // ?????????????????? ?????????????????????????????????
                        listen.click(view);
                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        LogUtils.d("onAdShow: ????????????");
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
                        LogUtils.d("getChuanShanJia onRenderSuccess: ?????????????????? ???" + width + "," + height);
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
     * ???????????????
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
                    err.put("msg","???????????????");
                    err.put("position",current_position);
                    listen.error(err);
                }
                KsFeedAd ksFeedAd = list.get(0);
                ksFeedAd.setAdInteractionListener(new KsFeedAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked() {
                        listen.click(ksFeedAd.getFeedView(mActivity));
                        ckickAdToJdh();  // ??????????????? ?????????????????????????????????
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
     * ???????????????
     * @param current_position
     */
    private void getInfomationAdv_BD(String current_position){
        JSONObject err= new JSONObject();
        err.put("code",-2);
        err.put("msg","BD?????????????????????");
        err.put("position",current_position);
        listen.error(err);
    }

    /**
     * VIVO?????????
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
                ckickAdToJdh();  // VIVO?????????????????????????????????
                // Log.d(TAG, "click4: ??????????????????");
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
     * OPPO?????????
     * @param current_position
     */
    private void getInfomationAdv_OPPO(String current_position){
        JSONObject err= new JSONObject();
        err.put("code",-2);
        err.put("msg","OPPO????????????");
        err.put("position",current_position);
        listen.error(err);
    }

    /**
     * ???????????????
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
                        ckickAdToJdh();  // ??????????????? ?????????????????????????????????
                        // Log.d(TAG, "click5: ??????????????????");
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
    //???????????????????????????
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
                ckickAdToJdh();  // ????????????????????? ?????????????????????????????????
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
     * ????????????????????????????????????
     */
    private void formatUseData() {
        uid = SPUtils.getStringData(mActivity, "uid","0");
        if (!"0".equals(uid)){
            order_id = calcOrderNo();
            verification = calcVerification();
            rewardAdsExtra = calcExtraData();

//            JSONObject error = new JSONObject();
//            error.put("code",ERROR_CODE_USER_NOT_LOGIN);
//            error.put("msg","???????????????");
//            reward_listen.error(error);
//            return;
        }
    }

    /**
     * ?????????-????????????
     */
    private void getRewardAdv_CSJ(){
        // Log.d(TAG, "getRewardAdv_CSJ: ");
        TTAdNative mTTAdNative = TTAdSdk.getAdManager().createAdNative(mActivity);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(config.getString("advertisement_id"))
                .setUserID(SPUtils.getStringData(mActivity, "token", "not_login"))//tag_id
                .setMediaExtra(rewardAdsExtra) //????????????
                .setOrientation(TTAdConstant.HORIZONTAL) //?????????????????????????????????????????????TTAdConstant.HORIZONTAL ??? TTAdConstant.VERTICAL
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

            // ????????????????????????
            @Override
            public void onRewardVideoCached() {}

            @Override
            public void onRewardVideoCached(TTRewardVideoAd ttRewardVideoAd) {
                    reward_listen.cached();
//                    reward_listen.show(config);
                    ttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {
                        @Override
                        public void onAdShow() {
                            // #BUG ??????????????????
                            // Log.d(TAG, "onAdShow: ");
                            reward_listen.show(config);
                        }

                        @Override
                        public void onAdVideoBarClick() {
                            ckickAdToJdh();  // ????????????????????? ?????????????????????????????????
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
                            error.put("msg","????????????");
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
     * ??????-????????????
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
                    LogUtils.d(TAG, "onRewardVideoAdLoad: ??????????????????");
                    LogUtils.d(TAG, "onVideoDownloadFailed: ");
                    JSONObject error = new JSONObject();
                    error.put("code",-2);
                    error.put("msg","??????????????????");
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
                            ckickAdToJdh();  // ?????????????????? ?????????????????????????????????
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
                            error.put("msg","????????????");
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
     * ??????????????????
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
                ckickAdToJdh();  // ???????????????????????????????????????????????????
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
                error.put("msg","??????????????????");
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
     * VIVO????????????
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
                // ??????????????????
                JSONObject error = new JSONObject();
                error.put("code",-1);
                error.put("msg","??????????????????");
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
                error.put("msg","????????????????????????");
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
        // ????????????
        JSONObject error = new JSONObject();
        error.put("code",-2);
        error.put("msg","????????????");
        error.put("channel",AD_CHANNEL_OPPO);
        reward_listen.error(error);
    }

    /**
     * ??????????????????
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
                        ckickAdToJdh();  // ???????????????????????????????????????????????????
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

                        // ????????????????????????????????????
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
     * ????????? ???????????????
     * @param display_contain
     */
    private void getSplashAdv_YLH(@NotNull ViewGroup display_contain){
        SplashAD splashAD = new SplashAD(mActivity, config.getString("advertisement_id"), new SplashADListener() {
            // 	?????????????????????????????????????????????????????????????????????????????????????????????????????? Activity???????????????????????????
            @Override
            public void onADDismissed() {
                // Log.d(TAG, "onADDismissed: ");
                splash_listen.close();
            }

            //?????????????????????error ???????????????????????????????????????????????????????????????????????????????????????5???
            @Override
            public void onNoAD(AdError adError) {
                // Log.d(TAG, "onNoAD: ");
                JSONObject error = new JSONObject();
                error.put("code",adError.getErrorCode());
                error.put("msg",adError.getErrorMsg());
                splash_listen.error(error);
            }

            // ???????????????????????????????????????????????????????????????????????????????????????????????????
            @Override
            public void onADPresent() {
                // Log.d(TAG, "onADPresent: ");
                splash_listen.display();

            }
            //????????????????????????????????????????????????????????????????????????????????????
            @Override
            public void onADClicked() {
                LogUtils.d(TAG, "onADClicked: ");
                ckickAdToJdh();  // ????????????????????? ?????????????????????????????????

                JSONObject info = new JSONObject();
                info.put("channel",AD_CHANNEL_CSJ);
                splash_listen.click(info);
            }
            //???????????????????????????????????????????????????????????????????????? ms
            @Override
            public void onADTick(long l) {
                // Log.d(TAG, "onADTick: ");
            }
            //?????????????????????
            @Override
            public void onADExposure() {
                // Log.d(TAG, "onADExposure: ");
                splash_listen.displayed();
            }

            //?????????????????????????????????fetchAdOnly?????????????????????????????????????????????????????????
            @Override
            public void onADLoaded(long l) {
                // Log.d(TAG, "onADLoaded: ");
            }
        });
        splashAD.fetchAndShowIn(display_contain);
    }

    /**
     * ????????? ????????????
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
                        ckickAdToJdh();  // ????????????????????? ?????????????????????????????????
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
                        // ?????????????????????
                        splash_listen.close();
                    }
                });

                display_contain.addView(ttSplashAd.getSplashView() ,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        });
    }

    /**
     * ?????? ????????????
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
                        ckickAdToJdh();  // ?????????????????? ?????????????????????????????????
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
     * ?????? ????????????
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
                ckickAdToJdh();  // ?????? ???????????? ?????????????????????????????????
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
     * VIVO????????????
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
                // ????????????
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
                ckickAdToJdh();  // VIVO???????????? ?????????????????????????????????
                // Log.d(TAG, "onADClicked: ");
                // ????????????
                JSONObject info = new JSONObject();
                info.put("channel",AD_CHANNEL_OPPO);
                splash_listen.click(info);
            }
        },new SplashAdParams.Builder(config.getString("advertisement_id")).setAppTitle("?????????").build());
        vivoSplashAd.loadAd();
    }

    /**
     * OPPO????????????
     */
    private void getSplashAdv_OPPO(){
        // ????????????
        JSONObject error = new JSONObject();
        error.put("code",-2);
        error.put("msg","????????????");
        error.put("channel",AD_CHANNEL_OPPO);
        splash_listen.error(error);
    }
    // ????????????????????????
    private void getSplashAdv_MI(){
        // ????????????
        JSONObject error = new JSONObject();
        error.put("code",-2);
        error.put("msg","????????????");
        error.put("channel",AD_CHANNEL_MI);
        splash_listen.error(error);
    }
    private void getSplashAdv_SIGMOB(){
        JSONObject error = new JSONObject();
        error.put("code",-2);
        error.put("msg","????????????");
        error.put("channel",AD_CHANNEL_SIGMOB);
        splash_listen.error(error);
    }

    /* ???????????? */
    // ??????????????????
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
    // ?????????????????????
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
                        err.put("msg","??????????????????");
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
    // ?????????????????????
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
                err.put("msg","??????????????????");
                insert_screen_listen.error(err);
            }
        });
        ylh_unified_interstitial_ad.loadAD();
    }
    // ??????????????????
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
    // VIVO????????????
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
    // OPPO????????????
    private void getInsertScreenAdv_OPPO(){
        JSONObject err = new JSONObject();
        err.put("code",-2);
        err.put("msg","OPPO??????????????????");
        insert_screen_listen.error(err);
    }
    // MI????????????
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
     * ?????????????????????????????????
     */
    private void ckickAdToJdh(){
        RequestParams req = new RequestParams();
        String token = SPUtils.getStringData(mActivity,"token","");
        uid = SPUtils.getStringData(mActivity, "uid","0"); // ???????????????ID
        req.put("token",token);
        req.put("am_id",config.getString("am_id"));
        long timestamp = System.currentTimeMillis();
        if(order_id == null || order_id == ""){ // ????????????????????????????????????
            order_id = calcOrderNo(timestamp);
        }

        req.put("orderNo", order_id); // ????????????????????????
        req.put("item", CaiNiaoApplication.getInstances().getOaid()); //???????????????
        verification = calcVerification(); // ??????????????????
        req.put("verification",verification);
        req.put("ap_id",config.getString("ap_id"));
        req.put("place_type",config.getString("place_type"));

        // ????????????t
        JSONObject json_obj = new JSONObject();
        json_obj.put("orderNo",order_id);
        json_obj.put("token",token);
        json_obj.put("timestamp", timestamp); // ?????????????????????????????????????????????????????????
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
    // ???????????????
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
