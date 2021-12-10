package com.android.jdhshop.juduohui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.MainActivity;
import com.android.jdhshop.R;
import com.android.jdhshop.advistion.JuDuoHuiAdvertisement;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.panpf.sketch.SketchImageView;

public class JuduohuiSplashActivity extends BaseActivity {
    JuDuoHuiAdvertisement juDuoHuiAdvertisement;
    Context mContext;
    JSONArray splash_config;
    Intent i ;
    int MaxRetry = 5;
    int errCount = 1;
    boolean can_be_close = false;
    boolean in_app = true;
    private final String TAG = getClass().getSimpleName();


    @BindView(R.id.tmp_splash)
    SketchImageView tmp_splash;
    @BindView(R.id.logo_v)
    LinearLayout logo_v;
    @Override
    protected void initUI() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LOW_PROFILE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_juduohui_splash);
        ButterKnife.bind(this);
        mContext = this;


        i = getIntent();

        // Log.d(TAG, "initUI: " + i.getIntExtra("close_only",0));

        if (CaiNiaoApplication.getUserBean() == null) commonGetUserMsg();

        String splash_cfg = SPUtils.getStringData(mContext,"ad_place_app_ea","");
        if ("".equals(splash_cfg)){
            // Log.d(TAG, "initUI: 广告配置为空");
            closeFun();
            return;
        }

        try {
            JSONObject all_config = JSONObject.parseObject(splash_cfg);
            splash_config = all_config.getJSONArray("list");
            if (splash_config.size()<= 0) {
                // Log.d(TAG, "initUI: 广告配置中的可用渠道为空");
                closeFun();
                return;
            }
        }
        catch (Exception e){
            // Log.d(TAG, "initUI: 解析广告配置出错");
            LogUtils.logd(splash_cfg);
            e.printStackTrace();
            closeFun();
            return;
        }

        LinearLayout layout = findViewById(R.id.splash_v);

        String tmp_splash_file = SPUtils.getStringData(this,"splash_tmp_file","");
        if (!"".equals(tmp_splash_file)) {
            tmp_splash.displayContentImage(tmp_splash_file);
//            try {
//                Drawable dr = Drawable.createFromPath(tmp_splash_file);
//                layout.setBackground(dr);
//            }
//            catch (Exception e){
//                e.printStackTrace();
//            }
        }

        juDuoHuiAdvertisement = new JuDuoHuiAdvertisement(this,null);
        juDuoHuiAdvertisement.setSplashAdListen(new JuDuoHuiAdvertisement.SplashAdListen() {
            @Override
            public void close() {
                // Log.d(TAG, "close: ");
                can_be_close = true;
                if (in_app) closeFun();
            }

            @Override
            public void click(JSONObject info) {
                // Log.d(TAG, "click: " + info);
            }

            @Override
            public void display() {
                // Log.d(TAG, "display: ");
                logo_v.setVisibility(View.VISIBLE);
            }

            @Override
            public void displayed() {
                // Log.d(TAG, "displayed: ");
            }

            @Override
            public void error(JSONObject error) {
                // Log.d(TAG, "error: " + error.toJSONString());
                if (errCount<MaxRetry) {
                    errCount = errCount+1;
                    runOnUiThread(() -> {
                        juDuoHuiAdvertisement.getSplashAdv(splash_config, layout);
                    });
                }
                else{
                    if (in_app) closeFun();
                }
            }
        });
        if (splash_config != null && splash_config.size()>0) {
            juDuoHuiAdvertisement.getSplashAdv(splash_config, layout);
        }
        else{
            closeFun();
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) return  false;
        return super.onKeyDown(keyCode, event);
    }

    private void closeFun(){
        if (i!=null){
            if (i.getIntExtra("close_only",0) == 1){
                // Log.d(TAG, "closeFun: 仅关闭");
                finish();
                return;
            }
            else{
                Intent intent = new Intent(mContext,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return;
            }
        }
        // Log.d(TAG, "closeFun: 启动");
        Intent intent = new Intent(mContext,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        in_app = true;
        if (can_be_close) closeFun();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Log.d(TAG, "onPause: 跳出去广告了");
        in_app = false;
    }
}