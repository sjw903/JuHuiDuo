package com.android.jdhshop.juduohui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.advistion.JuDuoHuiAdvertisement;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.juduohui.response.HttpJsonResponse;
import com.android.jdhshop.utils.AesUtils;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;

import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class JuduohuiRewardActivity extends BaseActivity {
    JuDuoHuiAdvertisement juDuoHuiAdvertisement;
    JSONArray adv_config = new JSONArray();
    JSONObject displayed_config;
    Context mContext;
    String getHuiPoint = "0";
    String adv_unit = null; // 获取收益的单位，默认不传为惠币

    int error_count = 1;
    int max_error = 5;

    Intent i = new Intent();
    @Override
    protected void initUI() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LOW_PROFILE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_juduohui_reward);
        getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.tt_trans_black));
        ButterKnife.bind(this);
        mContext = this;
        if (CaiNiaoApplication.getUserBean() == null) commonGetUserMsg();
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();

        if (intent == null){
            showToast("广告配置获取失败！");
            setResult(RESULT_CANCELED,i);
            finish();
            return;
        }

        try{
            // Log.d(TAG, "initData: " + intent.getStringExtra("adv_config"));
            JSONObject config = JSON.parseObject(intent.getStringExtra("adv_config"));
            getHuiPoint = config.getString("hpoint");
            adv_config = config.getJSONArray("list");

            adv_unit = intent.getStringExtra("adv_unit");
            // Log.d(TAG, "initData: " + adv_unit + ".,.... " + intent.getStringExtra("adv_units"));

            i.putExtra("hpoint",getHuiPoint);
            i.putExtra("close_fun",intent.getStringExtra("close_fun"));
        }catch (Exception e){
            e.printStackTrace();
            showToast("广告配置解析失败！");
            setResult(RESULT_CANCELED,i);
            finish();
        }

        if (adv_config.size()<=0){
            showToast("广告配置错误，暂无广告可以显示");
            setResult(RESULT_CANCELED,i);
            finish();
        }
        String uid = SPUtils.getStringData(mContext,"uid","0");

        if ("0".equals(uid)){
            showToast("用户信息获取失败，请重新登陆！");
            setResult(RESULT_CANCELED,i);
            finish();
        }
        juDuoHuiAdvertisement = new JuDuoHuiAdvertisement(this,null);
        juDuoHuiAdvertisement.setRewardUnit(adv_unit); // 设置广告收益的单位
    }

    @Override
    protected void initListener() {
        juDuoHuiAdvertisement.setRewardAdListen(new JuDuoHuiAdvertisement.RewardAdListen() {
            @Override
            public void click(JSONObject click_info) {
                // Log.d(TAG, "click: " + click_info);
            }

            @Override
            public void skip() {
                // Log.d(TAG, "skip: ");
            }

            @Override
            public void reward() {
                // Log.d(TAG, "reward: ");
                // 小米、VIVO 没有远端回调，本地进行远程回调
                if (displayed_config.getIntValue("channel") == JuDuoHuiAdvertisement.AD_CHANNEL_MI || displayed_config.getIntValue("channel") == JuDuoHuiAdvertisement.AD_CHANNEL_VIVO){
                    RequestParams req = new RequestParams();
                    JSONObject json_obj = new JSONObject();
                    json_obj.put("token",SPUtils.getStringData(mContext,"token",""));
                    json_obj.put("extra",juDuoHuiAdvertisement.getExtraData());
                    json_obj.put("timestamp", System.currentTimeMillis()/1000);
                    req.put("t",AesUtils.encrypt(json_obj.toJSONString()));
                    HttpUtils.post(Constants.ADVERTISEMENT_VERIFY,JuduohuiRewardActivity.this, req, new TextHttpResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            // Log.d(TAG, "reward onFailure: " + responseString);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            // Log.d(TAG, "reward onSuccess: " + responseString);
                        }
                    });
                }

            }

            @Override
            public void close() {
                // Log.d(TAG, "close: ");
                rewardInfo();
            }

            @Override
            public void show(JSONObject config) {
                // Log.d(TAG, "show: ");
                displayed_config = config;
                // Log.d(TAG, "show: " + config.toJSONString());
                pushAdOrderInfo();
            }

            @Override
            public void start() {
                // Log.d(TAG, "start: ");
            }

            @Override
            public void end() {
                // Log.d(TAG, "end: ");
            }

            @Override
            public void pause() {
                // Log.d(TAG, "pause: ");
            }

            @Override
            public void error(JSONObject error) {
                 Log.d(TAG, " onAdLoadFailed error: " + error);
                //  {"msg":"暂未实现","code":-2,"channel":5}

                if (error_count< adv_config.size()){
                    error_count = error_count + 1;
                    runOnUiThread(()->{
                        try {
                            juDuoHuiAdvertisement.getRewardAdv(adv_config);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    });
                }
                else{
                    Log.d(TAG, "error: 获取广告失败！！！");
                    setResult(RESULT_CANCELED, i);
                    finish();
                }
            }

            @Override
            public void requested() {
                // Log.d(TAG, "requested: ");
            }

            @Override
            public void cached() {
                // Log.d(TAG, "cached: ");
            }
        });
        // Log.d(TAG, "initListener: " + adv_config);
        try {
            juDuoHuiAdvertisement.getRewardAdv(adv_config);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 验证激励视频是否给与奖励
     */
    private void rewardInfo(){
        RequestParams req = new RequestParams();
        req.put("orderNo",juDuoHuiAdvertisement.getOrderId());
        HttpUtils.post(Constants.APP_IP + "/api/UserHuisign/getHuiPoint",JuduohuiRewardActivity.this, req, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogUtils.d("rewardInfo onFailure: " + responseString);
                setResult(RESULT_CANCELED,i);
                finish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("rewardInfo onSuccess: " + responseString);
                setResult(RESULT_OK,i);
                finish();
            }
        });
    }
    /**
     * 提交广告订单数据
     */

    private void pushAdOrderInfo(){
        RequestParams req = new RequestParams();
        req.put("token","");
        req.put("am_id",displayed_config.getString("am_id"));
        req.put("ap_id",displayed_config.getString("ap_id"));
        req.put("place_type",displayed_config.getString("place_type"));
        req.put("orderNo", juDuoHuiAdvertisement.getOrderId()); // 自定义生成订单号
        req.put("verification", juDuoHuiAdvertisement.getVerification());
        req.put("item", CaiNiaoApplication.getInstances().getOaid()); //设备号参数
        if (adv_unit!=null) req.put("unit",adv_unit);
        /* 参数规则
         * token
         * am_id
         * ap_id
         * place_type
         * orderNo
         * verification
         * item
         *
         * $orderNo=$uid.$msectime; 订单编号 uid+毫秒时间戳
         * $verification=md5($orderNo.$uid.$am_id); 验签参数
         * $extra=base64_encode($orderNo.'(-o-o-)'.$verification.'(-o-o-)'.$uid); 穿透参数
         */
        HttpUtils.post(Constants.ADD_ADVERTISEMENT_ORDER,JuduohuiRewardActivity.this, req, new HttpJsonResponse() {
            @Override
            protected void onSuccess(JSONObject response) {
                super.onSuccess(response);
                // Log.d(TAG, "onSuccess: " + response.toJSONString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);
                // Log.d(TAG, "onFailure: " + responseString);
                e.printStackTrace();
                setResult(RESULT_CANCELED,i);
                finish();
            }
        });
    }

}