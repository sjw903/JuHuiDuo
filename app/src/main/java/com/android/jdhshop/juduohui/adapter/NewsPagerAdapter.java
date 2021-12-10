package com.android.jdhshop.juduohui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class NewsPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments = new ArrayList<>();
    public NewsPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
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
