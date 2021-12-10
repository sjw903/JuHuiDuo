package com.android.jdhshop.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.fragments.ChaoJiDaoHangFragment;

public class ChaojiDaoHangActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chaoji_dao_hang);
    }

    @Override
    protected void initUI() {
        Fragment fm = new ChaoJiDaoHangFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.super_nav_frame,fm).commitAllowingStateLoss();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }
}