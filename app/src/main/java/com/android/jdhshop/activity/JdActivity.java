package com.android.jdhshop.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.MyOderViewPagerAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.JDKindBean;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.fragments.JdFragment;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class JdActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.magic_indicator)
    MagicIndicator tabBar;
    @BindView(R.id.tv_title)
    AutoClearEditText tvTitle;
    @BindView(R.id.bg_head)
    LinearLayout bgHead;
    @BindView(R.id.tv_right)
    TextView tvRight;
    private List<JDKindBean> tabTitles = new ArrayList<>();
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private int index = 0;
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_jd);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.lite_blue));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        bgHead.setBackgroundColor(getResources().getColor(R.color.lite_blue));
        tvTitle.setBackground(getResources().getDrawable(R.drawable.bg_border_white_16dp));
        tvTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!TextUtils.isEmpty(tvTitle.getText().toString().trim())) {
                    ((InputMethodManager) tvTitle.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    getComeActivity()
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    Intent intent = new Intent(JdActivity.this, JdSearchRestultActivity.class);
                    intent.putExtra("key", tvTitle.getText().toString().trim());
                    startActivity(intent);
                } else {
                    showToast("请输入搜索内容");
                }
                return false;
            }
        });
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(tvTitle.getText().toString().trim())) {
                    ((InputMethodManager) tvTitle.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    getComeActivity()
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    Intent intent = new Intent(JdActivity.this, JdSearchRestultActivity.class);
                    intent.putExtra("key", tvTitle.getText().toString().trim());
                    startActivity(intent);
                } else {
                    showToast("请输入搜索内容");
                }
            }
        });
    }

    @Override
    protected void initData() {

        tvRight.setTextColor(Color.WHITE);
        tvLeft.setVisibility(View.VISIBLE);
        //动态设置drawableLeft属性
        Drawable drawable1 = getResources().getDrawable(R.mipmap.icon_back_while);
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
        tvLeft.setCompoundDrawables(drawable1, null, null, null);
        getPddKind();
    }

    private void getPddKind() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.GET_JD_KIND,JdActivity.this, requestParams, new onOKJsonHttpResponseHandler<JDKindBean>(new TypeToken<Response<JDKindBean>>() {
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
            public void onSuccess(int statusCode, Response<JDKindBean> datas) {
                if (datas.isSuccess()) {
                    final List<JDKindBean> list = datas.getData().getList();
                    JDKindBean bean = new JDKindBean();
                    bean.setJingdong_id("0");
                    bean.setJingdong_cat_id("0");
                    bean.setName("爆款");
                    list.add(0, bean);
                    tabTitles.addAll(list);
                    for (int i = 0; i < list.size(); i++) {
                        JdFragment homeTabsFragment = new JdFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("pid", list.get(i).getJingdong_id());
                        bundle.putString("us_id", list.get(i).getJingdong_cat_id());
                        bundle.putString("name", list.get(i).getName());
                        bundle.putString("index",i+"");
                        bundle.putString("sort", "");
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

                            CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(JdActivity.this);
                            commonPagerTitleView.setContentView(R.layout.simple_pager_title_layout);
                            // 初始化
                            final ImageView titleImg = commonPagerTitleView.findViewById(R.id.title_img);
                            if (index == 0) {
                                titleImg.setImageResource(R.mipmap.jingxuan);
                            } else {
                                Glide.with(JdActivity.this).load(Constants.APP_IP + tabTitles.get(index).getIcon()).dontAnimate().into(titleImg);
                            }
                            final TextView titleText = commonPagerTitleView.findViewById(R.id.title_text);
                            titleText.setText(tabTitles.get(index).getName());

                            commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {
                                @Override
                                public void onSelected(int index, int totalCount) {
                                    titleText.setTextColor(getResources().getColor(R.color.red1));
                                }

                                @Override
                                public void onDeselected(int index, int totalCount) {
                                    titleText.setTextColor(Color.BLACK);
                                }

                                @Override
                                public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
                                    titleImg.setScaleX(1.3f + (0.8f - 1.3f) * leavePercent);
                                    titleImg.setScaleY(1.3f + (0.8f - 1.3f) * leavePercent);
                                }

                                @Override
                                public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
                                    titleImg.setScaleX(0.8f + (1.3f - 0.8f) * enterPercent);
                                    titleImg.setScaleY(0.8f + (1.3f - 0.8f) * enterPercent);
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
                } else {
                    showToast(datas.getMsg());
                }
            }
        });
    }

    @Override
    protected void initListener() {

    }

    @OnClick(R.id.tv_left)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
