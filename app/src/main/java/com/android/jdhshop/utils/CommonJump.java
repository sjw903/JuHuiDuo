package com.android.jdhshop.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.activity.MyMarketActivity;
import com.android.jdhshop.activity.PddActivity;
import com.android.jdhshop.juduohui.JuduohuiMainActivity;
import com.android.jdhshop.my.MyOrderActivity;
import com.android.jdhshop.my.MyShareUrlActivity;

// 所有活动等跳转方法
public class CommonJump {
    Activity mActivity;
    JSONObject jump_config;
    public CommonJump(Activity activity){
        mActivity = activity;
    }

    public CommonJump setConfig(JSONObject config){
        jump_config = config;
        return this;
    }

    public interface JumpCallback{
        void jump();
    }

    public void JumpToTarget(){
        if (jump_config == null) return;
        String action = jump_config.getString("key");
        switch (action){
            case "myinfo": //我的
                jumpFragment("f5,nothing","");
                break;
            case "shop_order": //购物订单
                JSONObject val_obj = jump_config.getJSONObject("val");
                String trade_id = val_obj.getString("order_id");
                Bundle bundle = new Bundle();
                bundle.putString("se",trade_id);
                openActivity(MyOrderActivity.class,bundle);
                break;
            case "invitation": // 邀请页面
                openActivity(MyShareUrlActivity.class);
                break;
            case "myMarker": // 我的市场
                openActivity(MyMarketActivity.class);
                break;
            case "Pddorder": // 拼多多列表
                openActivity(PddActivity.class);
                break;
            case "productDetails": // 商品详情页
                break;
            case "sign": //签到页面

                openActivity(JuduohuiMainActivity.class);
                break;
            case "participRecord": //调查卷参与记录
                break;
            case "banlance": // 资金记录
                break;
            case "balanceDraw": // 购物提现页面
                break;
            case "hpointDraw": // 惠币提现页面
                break;
            case "readmakemoneyList": //阅读赚钱页面
                break;
            case "readmakemoneyDetails": //阅读赚钱的详情页
                break;
            case "other": // 其它
                break;
        }
    }

    private void openActivity(Class target_class){
        Intent new_intent = new Intent(mActivity,target_class);
        new_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(new_intent);
    }
    private void openActivity(Class target_class,Bundle bundle){
        Intent new_intent = new Intent(mActivity,target_class);
        new_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        new_intent.putExtras(bundle);
        mActivity.startActivity(new_intent);
    }

    private void jumpFragment(String page_fragment,String title){

    }


}
