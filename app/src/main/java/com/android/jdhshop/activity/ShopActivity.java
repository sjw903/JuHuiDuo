package com.android.jdhshop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.MyOderViewPagerAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.ShopTabsBean;
import com.android.jdhshop.bean.ShopTabsChildBean;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.fragments.ShopFragment;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * @属性:商品页
 * @开发者:陈飞
 * @时间:2018/7/26 15:10
 */
public class ShopActivity extends BaseActivity {


    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tabBar)
    MagicIndicator tabBar;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private int index;

    private List<Fragment> fragments = new ArrayList<>();
    private String sort;

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_shop);
        butterknife.ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        tvLeft.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        if (intent != null) {

            String title = intent.getStringExtra("title");
            //页数
            index = intent.getIntExtra("index", 0);
            //搜索吗
            sort = intent.getStringExtra("sort");
            //设置标题
            tvTitle.setText(title);

        }
        //获取顶级淘宝商品分类列表
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
        HttpUtils.post(Constants.HOME_TAOBAOCAT_GETTOPCATLIST_URL,ShopActivity.this, requestParams, new onOKJsonHttpResponseHandler<ShopTabsBean>(new TypeToken<Response<ShopTabsBean>>() {
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

                    fragments.clear();
                    for (int i=0;i<list.size();i++) {
                        ShopTabsChildBean shopTabsChildBean=list.get(i);
                        ShopFragment homeTabsFragment = new ShopFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("pid", shopTabsChildBean.getTaobao_cat_id());
                        if((index==i)&&!"".equals(getIntent().getStringExtra("name"))){
                            bundle.putString("name",getIntent().getStringExtra("name"));
                            SPUtils.saveStringData(ShopActivity.this,"search_name",getIntent().getStringExtra("name"));
                        }else{
                            bundle.putString("name", shopTabsChildBean.getName());
                        }
                        if (!TextUtils.isEmpty(sort)) {
                            bundle.putString("sort", sort);
                        }
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
                            clipPagerTitleView.setText(list.get(index).getName());
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
}
