package com.android.jdhshop.activity;

import static com.android.jdhshop.CaiNiaoApplication.getAppContext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.MainActivity;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.PddClient;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.juduohui.JuduohuiSplashActivity;
import com.android.jdhshop.utils.AesUtils;
import com.android.jdhshop.widget.LoadingDialog;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;


public class SplashActivity extends Activity {
    private String TAG = getClass().getSimpleName();
    /**
     * @param msg 加载框提示语
     * 显示圆形加载进度对话框
     */
    private LoadingDialog loadingDialog;
    private boolean isFirstStart = false;
    private ACache aCache;

    protected void showLoadingDialog(String msg) {
        if (!this.isDestroyed() || !this.isFinishing()) {
            if (loadingDialog == null) {
                loadingDialog = LoadingDialog.createDialog(this);
                loadingDialog.setMessage(msg);
                loadingDialog.setCanceledOnTouchOutside(false);
            }
            try {
                loadingDialog.show();
            } catch (Exception e) {
            }
        }
    }

    private LottieAnimationView lottieAnimationView;

    /**
     * 关闭进度对话框
     */
    protected void closeLoadingDialog() {
        if (!this.isDestroyed() || !this.isFinishing()) {
            if (loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
                loadingDialog = null;
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(R.layout.activity_first);
        aCache = ACache.get(this);
        // 如果是小米或者华为渠道则资源首页地址变更为 /data/data/package/files/juduohui/
        Log.d(TAG, "onCreate: " + CaiNiaoApplication.getAppChannel());
        if ("xm".equals(CaiNiaoApplication.getAppChannel()) || "hw".equals(CaiNiaoApplication.getAppChannel())) {
            Constants.MAIN_FILE_PATH = getFilesDir().getAbsolutePath() + "/juduohuiziyuan/";
            Constants.ZIYUAN_PATH = getFilesDir().getAbsolutePath() + "/juduohuiziyuan/images/";
        }

        String kg = SPUtils.getStringData(this, "ty", "");
        if (!"true".equals(kg)) {
            Intent intent = new Intent(this, DialogActivity3.class);
            startActivityForResult(intent, 2);
        } else {
            initAppSet();
        }
    }

    public void initAppSet() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("channel", CaiNiaoApplication.getAppChannel());
        requestParams.put("appcode", CaiNiaoApplication.getAppCode());
        HttpUtils.post(Constants.GET_APP_SET, SplashActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // Log.d(TAG, "onFailure: statusCode = " + statusCode + " , " + responseString);
                LogUtils.logd(responseString);
                throwable.printStackTrace();
                ToastUtils.showLongToast(SplashActivity.this, "app加载配置失败，请重启");
                runOnUiThread(() -> {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(SplashActivity.this::initAppSet);
                        }
                    }, 10000);

                });
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.logd(responseString);
                try {

                    JSONObject tmp = new JSONObject(responseString);
                    LogUtils.logd("AppSet responseString = " + responseString);
                    String real_json = AesUtils.decrypt(tmp.getString("encryptedData"));
                    LogUtils.logd("AppSet: " + real_json);
                    JSONObject object = new JSONObject(real_json);
                    if (tmp.getInt("code") == 0) {
                        SPUtils.saveStringData(SplashActivity.this, "app_logo", object.getString("app_logo"));
                        SPUtils.saveStringData(SplashActivity.this, "tbk_pid", object.getString("tbk_pid"));
                        SPUtils.saveStringData(SplashActivity.this, "tbk_rid", object.getString("tbk_rid"));
                        SPUtils.saveStringData(SplashActivity.this, "tbk_aid", object.getString("tbk_aid"));
                        SPUtils.saveIntData(SplashActivity.this, "default_rate", object.getInt("default_rate")); //默认最高分成比例
                        SPUtils.saveStringData(SplashActivity.this, "pdd_pid", object.getString("pdd_pid"));
                        SPUtils.saveStringData(SplashActivity.this, "wx_appid", object.getString("wxpay_appid"));
                        SPUtils.saveStringData(SplashActivity.this, "wx_secret", object.getString("wxpay_appsecret"));
                        SPUtils.saveStringData(SplashActivity.this, "tbk_appkey", object.getString("tbk_appkey"));
                        SPUtils.saveStringData(SplashActivity.this, "tbk_relation_code", object.getString("tbk_relation_code"));
                        SPUtils.saveStringData(SplashActivity.this, "tbk_appsecret", object.getString("tbk_appsecret"));
                        SPUtils.saveStringData(SplashActivity.this, "pdd_client_id", object.getString("pdd_client_id"));
                        SPUtils.saveStringData(SplashActivity.this, "pdd_client_secret", object.getString("pdd_client_secret"));
                        SPUtils.saveStringData(SplashActivity.this, "jdh_secret", object.getString("jdh_secret"));
                        SPUtils.saveStringData(SplashActivity.this, "user_upgrade_register", object.getString("user_upgrade_register"));
                        SPUtils.saveStringData(SplashActivity.this, "user_upgrade_buy", object.getString("user_upgrade_buy"));
                        SPUtils.saveStringData(getAppContext(), "jd_key", object.getString("jd_android_key"));
                        SPUtils.saveStringData(getAppContext(), "jd_secret", object.getString("jd_android_secret"));
                        SPUtils.saveStringData(getAppContext(), "jd_pos_id", object.getString("jd_position_id"));
                        SPUtils.saveStringData(getAppContext(), "JD_APP_KEY_NEW", object.getString("jd_appkey_myxq"));
                        // 首页初次进入的弹窗配置
                        SPUtils.saveStringData(SplashActivity.this, "notice_title", object.getString("notice_title"));
                        SPUtils.saveStringData(SplashActivity.this, "notice_content", object.getString("notice_content"));
                        // 赚钱页面ICON相关配置
                        SPUtils.saveStringData(SplashActivity.this, "makemoneyList", object.getString("makemoneyList"));
                        // 客服默认URL
                        String kefu_url = object.getString("customer_service_url");
                        if (kefu_url == null || "".equals(kefu_url))
                            kefu_url = "https://jq.qq.com/?_wv=1027&k=orJGmf3a";
                        SPUtils.saveStringData(SplashActivity.this, "kefu_url", kefu_url);
                        // 此渠道是否开启广告推送
                        SPUtils.saveBoolean(SplashActivity.this, "is_open_ad", object.getBoolean("ad_is_open"));
                        // 云享域名
                        SPUtils.saveStringData(SplashActivity.this, "yx_domain", object.getString("yun_domain"));
                        // 云享APPID
                        SPUtils.saveStringData(SplashActivity.this, "yx_app_id", object.getString("yun_appid"));
                        // 弹窗广告配置
                        SPUtils.saveStringData(SplashActivity.this, "ad_place_app_al", object.getString("ad_place_app_al"));
                        // 开屏广告配置
                        SPUtils.saveStringData(SplashActivity.this, "ad_place_app_ea", object.getJSONObject("ad_place_app_ea").toString());
                        // 插屏广告配置
                        SPUtils.saveStringData(SplashActivity.this, "ad_place_app_insert_screen", object.getJSONObject("ad_place_app_is").toString());
                        // 默认开启两种通知
                        SPUtils.saveStringData(SplashActivity.this, "pt_notification", "true");
                        SPUtils.saveStringData(SplashActivity.this, "ago_notification", "true");
                        // 默认开启剪切板检测
                        SPUtils.saveStringData(SplashActivity.this, "read_clipboard", "true");
                        // 是否强制更新
                        SPUtils.saveBoolean(SplashActivity.this, "forced_to_update", object.getBoolean("forced_to_update"));
                        // 分享文章域名
                        SPUtils.saveStringData(SplashActivity.this, "share_article_host", object.getString("share_article_host"));
                        // 是否保持用户在线监控
                        SPUtils.saveStringData(SplashActivity.this, "heart_beat", object.getString("is_check_user_online"));
                        if ("1".equals(object.getString("is_check_user_online"))) {
                            // Log.d(TAG, "onSuccess: 需要心跳！");
                            CaiNiaoApplication.heartBeat.sendEmptyMessageDelayed(1, Constants.HEART_BEAT_TIME);
                        }
                        // 活动列表处理
                        JSONArray activityList = object.optJSONArray("activityList");

                        if (activityList != null && activityList.length() > 0) {
                            aCache.put("activityList", activityList.toString());
                        }

                        // 启动跳转页面处理
                        JSONObject app_t_url = object.optJSONObject("app_t_url");
                        if (app_t_url != null) {
                            aCache.put("jumpUrl", app_t_url.toString());
                        }
                        String personage_homepage_host = object.getString("personage_homepage_host");
                        SPUtils.saveStringData(SplashActivity.this, "personage_homepage_host",personage_homepage_host);
                        Constants.setInfo();
                        PddClient.setInfo();
                        init();
                    } else {
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void init() {
        if ("0".equals(SPUtils.getStringData(this, "iss", "0"))) {
            Intent intent = new Intent(this, DialogActivity3.class);
            startActivityForResult(intent, 2);
        } else {

            String splash_adv_cfg = SPUtils.getStringData(this, "ad_place_app_ea", "");
            LogUtils.logd("广告开启状态：" + SPUtils.getBoolean(this, "is_open_ad", true));
            // 如果开屏广告不为空，则跳开屏页面
            if (!isFirstStart && !"".equals(splash_adv_cfg) && SPUtils.getBoolean(this, "is_open_ad", true)) {
                Intent i = new Intent(this, JuduohuiSplashActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(i, 9999);
                return;
            }

            //
            String TAG = "SPLASH";
            String splash_data = SPUtils.getStringData(this, "ade", "");
            if (splash_data.equals("")) {
                RequestParams requestParams = new RequestParams();
                requestParams.put("cat_id", 6);
                HttpUtils.post(Constants.GET_BANNER, SplashActivity.this, requestParams, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        LogUtils.d(responseString);
                        LogUtils.d(throwable.getMessage());
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        SPUtils.saveStringData(getAppContext(), "ade", responseString);
                        runOnUiThread(() -> {
                            init();
                        });
                    }
                });
                return;
            }

            try {
                JSONObject object = new JSONObject(splash_data);
                if (object.getInt("code") == 0) {
                    final JSONArray array = object.getJSONObject("data").getJSONArray("list");
                    if (array.length() > 0) {
                        Intent intent = new Intent(SplashActivity.this, AdActivity.class);
                        startActivity(intent);
//                        finish();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
//                        finish();
                    }
                } else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            } catch (JSONException e) {
                // Log.d(TAG, "init: " + e.getMessage());
                e.printStackTrace();
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
//                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 2) {
            isFirstStart = true;
            LogUtils.d("TAG", "onActivityResult: 回调。");
            SPUtils.saveStringData(this, "iss", "1");
            this.initAppSet();
//            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
