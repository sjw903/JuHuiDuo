package com.android.jdhshop.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.MyOderViewPagerAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.PDDKindBean;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.fragments.PddFragment;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.widget.AutoClearEditText;
import com.android.jdhshop.widget.indicator.MagicIndicator;
import com.android.jdhshop.widget.indicator.ViewPagerHelper;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.CommonNavigator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.titles.CommonPagerTitleView;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class PddActivity extends BaseActivity {
//    @BindView(R.id.tv_left)
//    TextView tvLeft;
////    @BindView(R.id.homeBanner)
////    Banner banner;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.magic_indicator)
    MagicIndicator tabBar;
    @BindView(R.id.tv_title)
    AutoClearEditText tv_title;
    private List<PDDKindBean> tabTitles = new ArrayList<>();
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private int index = 0;
    @BindView(R.id.pddimage_top)
    ImageView pddimage_top;
    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.img_bg)
    ImageView img_bg;
    @BindView(R.id.img_search)
    ImageView img_search;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> banners = new ArrayList<>();

    @Override
    protected void initUI() {
        setStatusBar(Color.parseColor("#e02e24"));
        setContentView(R.layout.activity_pdd);
        ButterKnife.bind(this);
        BaseLogDZiYuan.LogDingZiYuan(pddimage_top,"pdd_top.png");
        BaseLogDZiYuan.LogDingZiYuan(img_back,"icon_back_while.png");
        BaseLogDZiYuan.LogDingZiYuan(img_bg,"pdd_logo.png");
        BaseLogDZiYuan.LogDingZiYuan(img_search,"pdd_top_search.png");
    }

    @Override
    protected void initData() {

//        tvLeft.setVisibility(View.VISIBLE);
        getPddKind();
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvRight.setText("取消");
        findViewById(R.id.img_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findViewById(R.id.ll_top).setVisibility(View.VISIBLE);
                findViewById(R.id.img_bg).setVisibility(View.GONE);
                findViewById(R.id.img_search).setVisibility(View.GONE);
//                openActivity(PddSearchResultActivity.class);
            }
        });
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.ll_top).setVisibility(View.GONE);
                findViewById(R.id.img_bg).setVisibility(View.VISIBLE);
                findViewById(R.id.img_search).setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.bg_head).setBackgroundColor(Color.parseColor("#00000000"));
        tvRight.setTextColor(getResources().getColor(R.color.white));
        tv_title.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(!TextUtils.isEmpty(tv_title.getText().toString().trim())){
                    ((InputMethodManager) tv_title.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    getComeActivity()
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    Intent intent=new Intent(PddActivity.this,PddSearchResultActivity.class);
                    intent.putExtra("keyword",tv_title.getText().toString().trim());
                    startActivity(intent);
                }else {
                    showToast("请输入搜索内容");
                }
                return false;
            }
        });
//        tvRight.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!TextUtils.isEmpty(tv_title.getText().toString().trim())){
//                    ((InputMethodManager) tv_title.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
//                            .hideSoftInputFromWindow(
//                                    getComeActivity()
//                                            .getCurrentFocus()
//                                            .getWindowToken(),
//                                    InputMethodManager.HIDE_NOT_ALWAYS);
//                    Intent intent=new Intent(PddActivity.this,PddSearchResultActivity.class);
//                    intent.putExtra("keyword",tv_title.getText().toString().trim());
//                    startActivity(intent);
//                }else {
//                    showToast("请输入搜索内容");
//                }
//            }
//        });
    }

    private void getPddKind() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.GET_PDD_KIND, PddActivity.this,requestParams, new onOKJsonHttpResponseHandler<PDDKindBean>(new TypeToken<Response<PDDKindBean>>() {
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
            public void onSuccess(int statusCode, Response<PDDKindBean> datas) {
                if (datas.isSuccess()) {
                    final List<PDDKindBean> list = datas.getData().getList();
                    PDDKindBean bean = new PDDKindBean();
                    bean.setPdd_id("0");
                    bean.setName("精选");
                    list.add(0, bean);
                    tabTitles.addAll(list);
                    for (int i = 0; i < list.size(); i++) {
                        PddFragment homeTabsFragment = new PddFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("pid", list.get(i).getPdd_id());
                        bundle.putString("name", list.get(i).getName());
                        bundle.putString("sort", "");
                        bundle.putString("index", i+"");
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
                            return tabTitles.size();
                        }

                        @Override
                        public IPagerTitleView getTitleView(Context context, final int index) {

                            CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(PddActivity.this);
                            commonPagerTitleView.setContentView(R.layout.simple_pager_title_layout);
                            // 初始化
                            final ImageView titleImg = commonPagerTitleView.findViewById(R.id.title_img);
                            if (index == 0) {
                                titleImg.setImageResource(R.mipmap.jingxuan);
                            } else {
                                Glide.with(PddActivity.this).load(Constants.APP_IP + tabTitles.get(index).getIcon()).dontAnimate().into(titleImg);
                            }
                            titleImg.setVisibility(View.GONE);
                            final TextView titleText = commonPagerTitleView.findViewById(R.id.title_text);
                            titleText.setText(tabTitles.get(index).getName());

                            commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {
                                @Override
                                public void onSelected(int index, int totalCount) {
                                    titleText.setTextColor(getResources().getColor(R.color.orange));
                                }

                                @Override
                                public void onDeselected(int index, int totalCount) {
                                    titleText.setTextColor(getResources().getColor(R.color.white));
                                }

                                @Override
                                public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
//                                    titleImg.setScaleX(1.3f + (0.8f - 1.3f) * leavePercent);
//                                    titleImg.setScaleY(1.3f + (0.8f - 1.3f) * leavePercent);
                                }

                                @Override
                                public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
//                                    titleImg.setScaleX(0.8f + (1.3f - 0.8f) * enterPercent);
//                                    titleImg.setScaleY(0.8f + (1.3f - 0.8f) * enterPercent);
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
                            indicator.setColors(getResources().getColor(R.color.orange));
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
    protected void initListener() {

    }

}
