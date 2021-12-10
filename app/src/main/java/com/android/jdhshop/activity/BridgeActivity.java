package com.android.jdhshop.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.MainActivity;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.juduohui.JuduohuiMainActivity;
import com.android.jdhshop.juduohui.NewsInformation;
import com.android.jdhshop.my.MyOrderActivity;
import com.android.jdhshop.my.MyShareUrlActivity;

public class BridgeActivity extends BaseActivity {

    private String TAG = getClass().getSimpleName();
    private ACache mAcache;
    private int notification_id = 8000;
    private Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Log.d(TAG, "onCreate: " + getIntent());



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge);
        mActivity = this;
        mAcache = ACache.get(this);


        Intent intent = getIntent();
        LogUtils.d(TAG, "onNewIntent: " + intent);
        LogUtils.d(TAG, "onNewIntent: " + intent.getStringExtra("path") + "," + intent.getStringExtra("name") + "," + intent.getStringExtra("toast"));

        if (intent.getAction()!=null && intent.getAction().equals(Constants.NOTIFICATION_ACTION)) {
            String activity_path = intent.getStringExtra("path");
            String notice_toast = intent.getStringExtra("toast");
            String activity_name = intent.getStringExtra("name");
            jumpFragment(activity_path, notice_toast);
        }
        else{
            setIntent(intent);
            jumpChecker();
        }

    }

    private void jumpFragment(String activity_path, String notice_toast) {
        if (!"".equals(notice_toast)) showToast(notice_toast);
        LogUtils.d(TAG, "jumpFragment: " + activity_path);
        if (!activity_path.equals("")) {
            //特殊处理 fragments
            if (activity_path.contains(",")) {
                String[] tmp_arr = activity_path.split(",");
                if (tmp_arr[0].equals(MainActivity.class.getName())) {
                    Bundle b = new Bundle();
                    Intent main_intent = new Intent(this,MainActivity.class);
                    b.putString("path", activity_path);
                    b.putString("toast",notice_toast);
                    main_intent.putExtras(b);
                    main_intent.setAction(Constants.NOTIFICATION_ACTION);
                    startActivity(main_intent);
                    //openActivityAndCloseThis(MainActivity.class,b);

                } else {
                    try {
                        Intent tmp_intent = new Intent();
                        tmp_intent.setComponent(new ComponentName(getPackageName(), tmp_arr[0]));
                        tmp_intent.setAction(Constants.NOTIFICATION_ACTION);
                        tmp_intent.putExtra("jump", "yes");
                        tmp_intent.putExtra("toast", notice_toast);
                        tmp_intent.putExtra("path", activity_path);
//                        tmp_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(tmp_intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    Intent tmp_intent = new Intent();
                    tmp_intent.setComponent(new ComponentName(getPackageName(), activity_path));
                    startActivity(tmp_intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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


    private void jumpChecker() {
        // 通过URL协议进入
        if (getIntent().getData() != null) {
            // Log.d(TAG, "jumpChecker: 通过URL协议进入");
            Uri uri = getIntent().getData();
            String open_param = new String(Base64.decode(uri.getQueryParameter("t"), Base64.DEFAULT));
            if (!"".equals(open_param)) {
                mAcache.put("jumpUrl", open_param);
                jumpToTarget();
            }
        }

        // 通过通知栏点击进入
        if (getIntent().getStringExtra("message") != null) {
            // Log.d(TAG, "jumpChecker: 通过通知栏点击进入");
            mAcache.put("jumpUrl", getIntent().getStringExtra("message"));
            jumpToTarget();
        }
    }

    private void jumpToTarget() {
        // Log.d(TAG, "jumpToTarget: ");
        CaiNiaoApplication.getInstances().getNotificationManager().cancel(notification_id);
        String app_t_url = mAcache.getAsString("jumpUrl");
         Log.d(TAG, "jumpToTarget: " + app_t_url);
        if (!"".equals(app_t_url) && app_t_url != null) {
            try {
                com.alibaba.fastjson.JSONObject obj = com.alibaba.fastjson.JSONObject.parseObject(app_t_url);
                String action = obj.getString("key");
                com.alibaba.fastjson.JSONObject val_obj = obj.getJSONObject("val");
                Bundle bundle = new Bundle();
                Intent open_intent = new Intent();
                // Log.d(TAG, "jumpToTarget: 跳转的KEY值 = " + action);
                if (!"".equals(obj.getString("toast"))) showToast(obj.getString("toast"));
                switch (action) {
                    case "myinfo": //我的1
                        String notice = obj.getString("toast");
//                        jumpFragment(MainActivity.class.getName() + ",f5", notice);
                        bundle.putString("message",obj.toJSONString());
                        open_intent.setClass(mActivity,MainActivity.class);
                        open_intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        open_intent.putExtras(bundle);
                        startActivity(open_intent,bundle);
                        return;
//                        break;
                    case "index":
                        break;
                    case "shop_order": //购物订单1
                        String trade_id = val_obj.getString("order_id");
                        bundle.putString("se", trade_id);
                        openActivity(MyOrderActivity.class, bundle);
                        break;
                    case "invitation": // 邀请页面1
                        openActivity(MyShareUrlActivity.class);
                        break;
                    case "myMarker": // 我的市场1
                        // Log.d(TAG, "jumpToTarget: level" + val_obj.getIntValue("level"));
                        bundle.putInt("level", val_obj.getIntValue("level"));
                        openActivity(MyMarketActivity.class, bundle);
                        break;
                    case "Pddorder": // 拼多多列表1
                        openActivity(PddActivity.class);
                        break;
                    case "productDetails": // 商品详情页 1
                        String p_type = val_obj.getString("type"); // 是哪个平台的商品
                        String goods_id = val_obj.getString("id"); // 商品ID
                        switch (p_type) {
                            case "1": // 淘宝
                                bundle.putString("num_iid", goods_id);
                                openActivity(PromotionDetailsActivity.class, bundle);
                                break;
                            case "2": // 京东
//                                getJdGoodsMsgRequest(goods_id);
                                break;
                            case "3": // 拼多多
//                                getPddGoodsMsgRequest(goods_id);
                                break;
                        }
                        break;
                    case "sign": //签到页面
                        bundle.putString("jump", "yes");
                        bundle.putString("path", JuduohuiMainActivity.class.getName() + ",f2");
                        bundle.putString("notice", obj.getString("notice"));
                        openActivity(JuduohuiMainActivity.class, bundle);
                        break;
                    case "salary": //领工资
                        bundle.putString("jump", "yes");
                        bundle.putString("path", JuduohuiMainActivity.class.getName() + ",f3");
                        bundle.putString("notice", obj.getString("notice"));
                        openActivity(JuduohuiMainActivity.class, bundle);
                        break;
                    case "participRecord": //调查卷参与记录
                        bundle.putString("url", "./survey_history.html");
                        openActivity(JuDuoHuiActivity.class, bundle);
                        break;
                    case "banlance": // 资金记录
                        bundle.putString("url", "./profit_history.html");
                        openActivity(JuDuoHuiActivity.class, bundle);
                        break;
                    case "balanceDraw": // 购物提现页面
                        bundle.putString("url", "./profit.html");
                        bundle.putString("tab", "1");
                        openActivity(JuDuoHuiActivity.class, bundle);
                        break;
                    case "hpointDraw": // 惠币提现页面
                        bundle.putString("url", "./profit.html");
                        bundle.putString("tab", "2");
                        openActivity(JuDuoHuiActivity.class, bundle);
                        break;
                    case "readmakemoneyList": //阅读赚钱页面
                        openActivity(JuduohuiMainActivity.class);
                        break;
                    case "readmakemoneyDetails": //阅读赚钱的详情页
                        com.alibaba.fastjson.JSONObject tmp = new com.alibaba.fastjson.JSONObject();
                        tmp.put("id", val_obj.getString("artice_id"));
                        bundle.putString("config", tmp.toJSONString());
                        open_intent.putExtras(bundle);
                        open_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        openActivity(NewsInformation.class, bundle);
                        break;
                    case "configActriceDetails":
                        NewsActivity.actionStart(mActivity, val_obj.getString("article_id"), val_obj.getString("article_title"));
                        break;
                    case "other": // 其它
                        Intent tt = new Intent();
                        ComponentName componentName = new ComponentName(this, obj.getString("path"));
                        tt.setComponent(componentName);
                        tt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(tt);
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            mAcache.remove("jumpUrl");
            finish();
        }
    }

}