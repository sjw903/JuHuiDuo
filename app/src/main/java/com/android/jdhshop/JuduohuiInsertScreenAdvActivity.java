package com.android.jdhshop;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.android.jdhshop.base.BaseActivity;

public class JuduohuiInsertScreenAdvActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juduohui_insert_screen_adv);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LOW_PROFILE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_juduohui_reward);
        getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.tt_trans_black));
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
}