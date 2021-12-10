package com.android.jdhshop.mall;

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
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyShopMallOrderActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tabBar)
    MagicIndicator tabBar;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private List<Fragment> fragments = new ArrayList<>();
    private int index = 0;
    private List<String> list;
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_shop);
        ButterKnife.bind(this);
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("我的订单");
        tabBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        if(getIntent().getExtras()!=null){
            index=Integer.valueOf(getIntent().getExtras().getString("type"));
            if(index<=5){
                list=new ArrayList<>(Arrays.asList("全部","待付款","待发货","待收货","已完成"));
                for(int i=0;i<5;i++){
                    MyOrderFragment homeTabsFragment = new MyOrderFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("pid",i+"");
                    homeTabsFragment.setArguments(bundle);
                    fragments.add(homeTabsFragment);
                }
            }else{
                tvTitle.setText("订单售后");
                list=new ArrayList<>(Arrays.asList("申请退款","已同意","拒绝退款","已退款"));
                index=0;
                for(int i=6;i<10;i++){
                    MyOrderFragment homeTabsFragment = new MyOrderFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("pid",i+"");
                    homeTabsFragment.setArguments(bundle);
                    fragments.add(homeTabsFragment);
                }
            }
        }
        viewpager.setOffscreenPageLimit(fragments.size());
        MyOderViewPagerAdapter myOderViewPagerAdapter = new MyOderViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(myOderViewPagerAdapter);
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
                commonPagerTitleView.setContentView(R.layout.item_text3);
                final TextView titleText = commonPagerTitleView.findViewById(R.id.txt_cat);
                titleText.setText(list.get(index));
                titleText.setLayoutParams(new LinearLayout.LayoutParams(BitmapUtils.getScreenWith(context) / list.size(), LinearLayout.LayoutParams.MATCH_PARENT));
                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {

                    @Override
                    public void onDeselected(int i, int i1) {
                        titleText.setTextColor(getResources().getColor(R.color.darkgray));
                    }
                    @Override
                    public void onSelected(int i, int i1) {
                        titleText.setTextColor(getResources().getColor(R.color.colorPrimary));
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
        viewpager.setCurrentItem(index);
    }

    @Override
    protected void initListener() {

    }

    @OnClick(R.id.tv_left)
    public void onViewClicked() {
        finish();
    }
}
