package com.android.jdhshop.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 开发者：陈飞
 * 时间:2018/7/16 16:40
 * 说明：我的Fragment适配器
 */
public class MyOderViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();

    public MyOderViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        if (fragments != null) {
            this.fragments.clear();
            this.fragments.addAll(fragments);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
