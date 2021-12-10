package com.android.jdhshop.mall;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.MyOderViewPagerAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.mallbean.MallCatbean;
import com.android.jdhshop.widget.indicator.MagicIndicator;
import com.android.jdhshop.widget.indicator.ViewPagerHelper;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.CommonNavigator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class ShopMallActivity extends BaseActivity {


    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tabBar)
    MagicIndicator tabBar;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tv_right)
    TextView tvRight;
    private int index;

    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_shop);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("自营商城");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("购物车");
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(MallGoodsCartActivity.class);
            }
        });
//        ShopMallFragment homeTabsFragment = new ShopMallFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("pid", getIntent().getExtras().getString("pid"));
//        bundle.putString("name", getIntent().getExtras().getString("name"));
//        homeTabsFragment.setArguments(bundle);
//        fragments.add(homeTabsFragment);
//        viewpager.setOffscreenPageLimit(fragments.size());
//        //适配器
//        MyOderViewPagerAdapter myOderViewPagerAdapter = new MyOderViewPagerAdapter(getSupportFragmentManager(), fragments);
//        viewpager.setAdapter(myOderViewPagerAdapter);
//        //头部导航栏
//        CommonNavigator commonNavigator = new CommonNavigator(getComeActivity());
//        commonNavigator.setSkimOver(true);
//        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
//
//            @Override
//            public int getCount() {
//                return 1;
//            }
//
//            @Override
//            public IPagerTitleView getTitleView(Context context, final int index) {
//                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
//                clipPagerTitleView.setText("");
//                clipPagerTitleView.setTextColor(getResources().getColor(R.color.col_999));
//                clipPagerTitleView.setClipColor(getResources().getColor(R.color.red1));
//                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        viewpager.setCurrentItem(index);
//                    }
//                });
//                return clipPagerTitleView;
//            }
//
//            @Override
//            public IPagerIndicator getIndicator(Context context) {
//                LinePagerIndicator indicator = new LinePagerIndicator(context);
//                indicator.setMode(1);
//                indicator.setColors(getResources().getColor(R.color.red1));
//                return indicator;
//            }
//        });
//        tabBar.setNavigator(commonNavigator);
//        ViewPagerHelper.bind(tabBar, viewpager);
//        //设置页数
//        viewpager.setCurrentItem(index);
        getTopCatListRequst();
    }

    @Override
    protected void initListener() {

    }


    @OnClick(R.id.tv_left)
    public void onViewClicked() {
        finish();
    }

    /**
     * @属性:获取顶级淘宝商品分类列表
     * @开发者:陈飞
     * @时间:2018/7/26 15:31
     */
    private void getTopCatListRequst() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.SLEF_MALL_CAT, ShopMallActivity.this,requestParams, new onOKJsonHttpResponseHandler<MallCatbean>(new TypeToken<Response<MallCatbean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
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
            public void onSuccess(int statusCode, Response<MallCatbean> datas) {
                if (datas.isSuccess()) {
                    final List<MallCatbean> list = datas.getData().list;
                    fragments.clear();
                    for (int i = 0; i < list.size(); i++) {
                        MallCatbean shopTabsChildBean = list.get(i);
                        ShopMallFragment homeTabsFragment = new ShopMallFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("pid", shopTabsChildBean.cat_id);
                        bundle.putString("name", shopTabsChildBean.cat_name);
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
                            ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                            clipPagerTitleView.setText(list.get(index).cat_name);
                            clipPagerTitleView.setTextColor(getResources().getColor(R.color.col_999));
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
                    //设置页数
                    viewpager.setCurrentItem(index);
                } else {
                    showToast(datas.getMsg());
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
