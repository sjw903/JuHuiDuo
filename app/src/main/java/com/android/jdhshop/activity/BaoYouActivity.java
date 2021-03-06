package com.android.jdhshop.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.adapter.MyOderViewPagerAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.ShopTabsBean;
import com.android.jdhshop.bean.ShopTabsChildBean;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.fragments.BaoYouFragment;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.widget.indicator.MagicIndicator;
import com.android.jdhshop.widget.indicator.ViewPagerHelper;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.CommonNavigator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.titles.ClipPagerTitleView;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class BaoYouActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tabBar)
    MagicIndicator tabBar;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private int index=0;

    private List<Fragment> fragments = new ArrayList<>();
    @Override
    protected void initUI() {
        setContentView(R.layout.ac_phb);
        ButterKnife.bind(this);
        tvLeft.setVisibility(View.VISIBLE);
        switch (getIntent().getStringExtra("type")){
            case "1":
                tvTitle.setText("????????????");
                break;
            case "2":
                tvTitle.setText("9.9??????");
                break;
            case "4":
                tvTitle.setText("?????????");
                break;
            case "5":
                tvTitle.setText("?????????");
                break;
            case "9":
                tvTitle.setText("????????????");
                break;
            case "20":
                tvTitle.setText("????????????");
                break;
            case "22":
                tvTitle.setText("?????????");
                break;
            case "23":
                tvTitle.setText("????????????");
                break;
        }
        final List<String> temp=new ArrayList<>();
        temp.add("??????");
        temp.add("??????");
        temp.add("??????");
        temp.add("??????");
        temp.add("??????");
        temp.add("??????");
        temp.add("??????");
        temp.add("??????");
        temp.add("??????");
        temp.add("??????");
        temp.add("??????");
        temp.add("??????");
        temp.add("??????");
        temp.add("??????");
        temp.add("??????");
        temp.add("??????");
        temp.add("??????");
        temp.add("??????");
        fragments.clear();
        for (int i=0;i<temp.size();i++) {
            BaoYouFragment homeTabsFragment = new BaoYouFragment();
            Bundle bundle = new Bundle();
            bundle.putString("pid",i+"");
            bundle.putString("type",getIntent().getStringExtra("type")+"");
            bundle.putString("name", temp.get(i));
            homeTabsFragment.setArguments(bundle);
            fragments.add(homeTabsFragment);
        }
        viewpager.setOffscreenPageLimit(fragments.size());
        //?????????
        MyOderViewPagerAdapter myOderViewPagerAdapter = new MyOderViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(myOderViewPagerAdapter);
        //???????????????
        CommonNavigator commonNavigator = new CommonNavigator(getComeActivity());
        commonNavigator.setSkimOver(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return temp.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText(temp.get(index));
                clipPagerTitleView.setTextColor(getResources().getColor(R.color.col_666));
                clipPagerTitleView.setClipColor(getResources().getColor(R.color.red1));
                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewpager.setCurrentItem(index);
                    }
                });
                return clipPagerTitleView;
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
        //????????????
        viewpager.setCurrentItem(index);
//        getTopKind();
    }
    private void getTopKind(){
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.HOME_TAOBAOCAT_GETTOPCATLIST_URL,BaoYouActivity.this, requestParams, new onOKJsonHttpResponseHandler<ShopTabsBean>(new TypeToken<Response<ShopTabsBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Response<ShopTabsBean> datas) {
                if (datas.isSuccess()) {
                    final List<ShopTabsChildBean> list = datas.getData().getList();
                    list.add(0,new ShopTabsChildBean("","??????","",""));
                    fragments.clear();
                    for (int i=0;i<list.size();i++) {
                        BaoYouFragment homeTabsFragment = new BaoYouFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("pid",i+"");
                        bundle.putString("name", list.get(i).getName());
                        if (!TextUtils.isEmpty(list.get(i).getName())) {
                            bundle.putString("sort", list.get(i).getName());
                        }
                        homeTabsFragment.setArguments(bundle);
                        fragments.add(homeTabsFragment);
                    }
                    viewpager.setOffscreenPageLimit(fragments.size());
                    //?????????
                    MyOderViewPagerAdapter myOderViewPagerAdapter = new MyOderViewPagerAdapter(getSupportFragmentManager(), fragments);
                    viewpager.setAdapter(myOderViewPagerAdapter);
                    //???????????????
                    CommonNavigator commonNavigator = new CommonNavigator(getComeActivity());
                    commonNavigator.setSkimOver(true);
                    commonNavigator.setAdapter(new CommonNavigatorAdapter() {

                        @Override
                        public int getCount() {
                            return list.size();
                        }

                        @Override
                        public IPagerTitleView getTitleView(Context context, final int index) {
                            ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                            clipPagerTitleView.setText(list.get(index).getName());
                            clipPagerTitleView.setTextColor(getResources().getColor(R.color.col_666 ));
                            clipPagerTitleView.setClipColor(getResources().getColor(R.color.red1));
                            clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    viewpager.setCurrentItem(index);
                                }
                            });
                            return clipPagerTitleView;
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
                    //????????????
                    viewpager.setCurrentItem(index);
                } else {
                    showToast(datas.getMsg());
                }
            }
        });
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
