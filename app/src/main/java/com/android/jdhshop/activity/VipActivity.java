package com.android.jdhshop.activity;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.fragments.VipFragmentNew;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VipActivity extends BaseActivity {
    @BindView(R.id.vip_pager)
    ViewPager vip_pager;

    Activity mActivity;


    @Override
    protected void initUI() {
        setContentView(R.layout.activity_vip);
        ButterKnife.bind(this);
        mActivity = this;

        setStatusBar(Color.BLACK);
        FragmentManager fragmentManager = getSupportFragmentManager();
        vip_pager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return new VipFragmentNew();
            }

            @Override
            public int getCount() {
                return 1;
            }
        });
        vip_pager.setCurrentItem(0,true);
        vip_pager.setOffscreenPageLimit(1);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }
}