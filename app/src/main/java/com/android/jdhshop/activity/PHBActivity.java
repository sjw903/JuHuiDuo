package com.android.jdhshop.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.adapter.MyOderViewPagerAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.fragments.PHBFragment;
import com.android.jdhshop.utils.BitmapUtils;
import com.android.jdhshop.widget.indicator.MagicIndicator;
import com.android.jdhshop.widget.indicator.ViewPagerHelper;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.CommonNavigator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PHBActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tabBar)
    MagicIndicator tabBar;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private int index = 0;

    private List<Fragment> fragments = new ArrayList<>();
    private List<String> list = new ArrayList<>();

    @Override
    protected void initUI() {
        setContentView(R.layout.ac_phb);
        ButterKnife.bind(this);
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("排行榜");
        list.add("实时销量榜");
        list.add("今日爆单榜");
        list.add("昨日爆单榜");
        for (int i = 0; i < list.size(); i++) {
            PHBFragment homeTabsFragment = new PHBFragment();
            Bundle bundle = new Bundle();
            bundle.putString("pid", (i+1) + "");
            bundle.putString("name", list.get(i));
            bundle.putString("sort", list.get(i));
            homeTabsFragment.setArguments(bundle);
            fragments.add(homeTabsFragment);
        }
        viewpager.setOffscreenPageLimit(fragments.size());
        //适配器
        MyOderViewPagerAdapter myOderViewPagerAdapter = new MyOderViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(myOderViewPagerAdapter);
        //头部导航栏
        CommonNavigator commonNavigator = new CommonNavigator(getComeActivity());
        commonNavigator.setSkimOver(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);
                commonPagerTitleView.setContentView(R.layout.item_text2);
                final TextView titleText = commonPagerTitleView.findViewById(R.id.txt_cat);
                titleText.setText(list.get(index));
                titleText.setLayoutParams(new LinearLayout.LayoutParams(BitmapUtils.getScreenWith(context) / 3, LinearLayout.LayoutParams.MATCH_PARENT));
                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {

                    @Override
                    public void onDeselected(int i, int i1) {
                        titleText.setTextColor(getResources().getColor(R.color.col_666));
                    }

                    @Override
                    public void onSelected(int i, int i1) {
                        titleText.setTextColor(getResources().getColor(R.color.red1));
                    }

                    @Override
                    public void onEnter(int i, int i1, float v, boolean b) {
                    }

                    @Override
                    public void onLeave(int i, int i1, float v, boolean b) {

                    }
                });

                commonPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewpager.setCurrentItem(index);
                    }
                });
                return commonPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(1);
                indicator.setColors(getResources().getColor(R.color.red1));
                return indicator;
            }
        });
        tabBar.setNavigator(commonNavigator);
        ViewPagerHelper.bind(tabBar, viewpager);
        //设置页数
        viewpager.setCurrentItem(index);
    }
    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {
    }

    @OnClick(R.id.tv_left)
    public void onViewClicked() {
        finish();
    }
}
