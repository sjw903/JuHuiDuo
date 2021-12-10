package com.android.jdhshop.juduohui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.JuDuoHuiActivity;
import com.android.jdhshop.activity.NewsActivity;
import com.android.jdhshop.advistion.JuDuoHuiAdvertisement;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.UserInfoBean;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.juduohui.adapter.NewsPagerAdapter;
import com.android.jdhshop.juduohui.response.HttpJsonResponse;
import com.android.jdhshop.utils.MyScrollView;
import com.android.jdhshop.utils.UIUtils;
import com.android.jdhshop.widget.indicator.MagicIndicator;
import com.android.jdhshop.widget.indicator.ViewPagerHelper;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.CommonNavigator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.titles.ClipPagerTitleView;
import com.blankj.utilcode.util.ClickUtils;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * 发布界面
 */
public class NewsPubListActivity extends BaseActivity {

    @BindView(R.id.main_smart_refresh)
    SmartRefreshLayout main_smart_refresh;

    @BindView(R.id.header_line_box)
    RelativeLayout header_line_box;
    @BindView(R.id.tab_bar)
    MagicIndicator tab_bar;
    @BindView(R.id.tab_body)
    ViewPager tab_body;

    @BindView(R.id.back_button)
    ImageView back_button;
    @BindView(R.id.back_button_2)
    ImageView back_button_2;

    @BindView(R.id.pub_area)
    TextView pub_area; //发布区域
    @BindView(R.id.pub_area_2)
    TextView pub_area_2; //发布区域

    @BindView(R.id.second_title)
    LinearLayout second_title;
    @BindView(R.id.pub_news_box)
    LinearLayout pub_news_box;


    @BindView(R.id.guize)
    TextView guize; //规则
    @BindView(R.id.gong_gao)
    TextView gong_gao; //公告

    @BindView(R.id.list_adv)
    LinearLayout list_adv; //列表广告位

    @BindView(R.id.today_pub_title)
    TextView today_pub_title; //今日发布数量标题
    @BindView(R.id.today_pub)
    TextView today_pub; //今日发布数量

    @BindView(R.id.today_golds)
    TextView today_golds; // 今日金币

    @BindView(R.id.sum_golds_title)
    TextView sum_golds_title;
    @BindView(R.id.sum_golds)
    TextView sum_golds;

    @BindView(R.id.today_pub_max)
    TextView today_pub_max; //日发布上限
    @BindView(R.id.pub_news_golds)
    TextView pub_news_golds; //发布文章加金币数量

    @BindView(R.id.pub_button)
    Button pub_button; //发布按钮

    @BindView(R.id.main_scroll)
    MyScrollView main_scroll;

    @BindView(R.id.pub_homepage)
    TextView pub_homepage; //个人主页

    List<String> tabs = Arrays.asList("我的发布", "最新发布", "今收最高");
    List<Fragment> tabs_body = new ArrayList<>();
    CommonNavigatorAdapter tab_menus_adapter;

    WindowManager wm = null;
    DisplayMetrics dm = new DisplayMetrics();
    Activity mActivity;

    static int NEW_PUB_ARTICLE = 9966;


    String ip = "";
    String area = "";

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_news_pub_list);
        ButterKnife.bind(this);
        mActivity = this;
        setStatusBar(Color.parseColor("#FE7855"));

        wm = getWindowManager();
        wm.getDefaultDisplay().getMetrics(dm);

        Drawable go_back_button_icon = getResources().getDrawable(R.drawable.ic_goback);
        go_back_button_icon.setBounds(0, 0, 56, 56);
        go_back_button_icon.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        back_button.setImageDrawable(go_back_button_icon);

        Drawable guize_icon = getResources().getDrawable(R.drawable.ic_news_guize);
        guize_icon.setColorFilter(Color.parseColor("#990000"), PorterDuff.Mode.SRC_IN);
        guize_icon.setBounds(0, 0, UIUtils.dp2px(12), UIUtils.dp2px(12));
        guize.setCompoundDrawables(guize_icon, null, null, null);

        Drawable right_arrow_icon = getResources().getDrawable(R.drawable.ic_arrow_right);
        right_arrow_icon.setColorFilter(Color.parseColor("#990000"), PorterDuff.Mode.SRC_IN);
        right_arrow_icon.setBounds(0, 0, UIUtils.dp2px(12), UIUtils.dp2px(12));
        sum_golds_title.setCompoundDrawables(null, null, right_arrow_icon, null);
        today_pub_title.setCompoundDrawables(null, null, right_arrow_icon, null);

        Drawable gg_left_icon = getResources().getDrawable(R.drawable.news_gg_title);
        gg_left_icon.setBounds(0, 0, 96, 42);
        Drawable gg_right_icon = getResources().getDrawable(R.drawable.to);
        gg_right_icon.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);
        gg_right_icon.setBounds(0, 0, 20, 36);
        gong_gao.setCompoundDrawables(gg_left_icon, null, gg_right_icon, null);

        ClickUtils.applyGlobalDebouncing(pub_homepage, v -> {
            UserInfoBean userInfoBean = CaiNiaoApplication.getUserInfoBean();
            if (null == userInfoBean) {
                finish();
                return;
            }
            UserInfoBean.UserMsgBean userMsgBean = userInfoBean.user_msg;
            if (null == userMsgBean) {
                finish();
                return;
            }
            String auth_code = userMsgBean.auth_code;
            Bundle bundle = new Bundle();
            bundle.putString("auth_code", auth_code);
            openActivity(JuduohuiHomePageActivity.class, bundle);
        });
        initTabBar();
        getAdvConfig();
        changePageSize();
        getMediaTopInfo();
    }

    private void getMediaTopInfo() {
        RequestParams re = new RequestParams();
        HttpUtils.post(Constants.MEDIA_LIB_REPRINT_INFO, NewsPubListActivity.this, re, new HttpJsonResponse() {
            @Override
            protected void onSuccess(JSONObject response) {
                super.onSuccess(response);
                // Log.d(TAG, "onSuccess: " + response);
                if (response.getIntValue("code") == 0) {
                    JSONObject info = response.getJSONObject("statistical");
                    runOnUiThread(() -> {
                        today_pub.setText(info.getString("num"));
                        today_golds.setText(info.getString("gold"));
                        sum_golds.setText(info.getString("goldSum"));

                        today_pub_max.setText(String.format("日上限%s金币", response.getString("reprint_gold_max")));
                        pub_news_golds.setText(String.format("+%s金币", response.getString("reprint_gold")));

                    });
                } else {
                    showToast(response.getString("msg"));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);
                // Log.d(TAG, "onFailure: " + responseString);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                main_smart_refresh.finishRefresh();
            }
        });
    }

    public void changePageSize() {
        runOnUiThread(() -> {
            ViewGroup.LayoutParams lm = tab_body.getLayoutParams();
            lm.height = dm.heightPixels - UIUtils.dp2px(36 + 70);
            tab_body.setLayoutParams(lm);
        });
    }

    JSONArray adv_config = new JSONArray();
    JuDuoHuiAdvertisement juDuoHuiAdvertisement = null;

    // 获取广告配置
    private void getAdvConfig() {
        RequestParams req = new RequestParams();
        req.put("identifys", "publish_content");
        HttpUtils.post(Constants.GET_ADVERTISEMENT_LIST, NewsPubListActivity.this, req, new HttpJsonResponse() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);

            }

            @Override
            protected void onSuccess(JSONObject response) {
                super.onSuccess(response);
                if (response.getIntValue("code") == 0) {
                    for (String key : response.getJSONObject("data").getJSONObject("place_list").keySet()) {
                        JSONObject tmp = response.getJSONObject("data").getJSONObject("place_list").getJSONObject(key);
                        if ("publish_content".equals(tmp.getString("identify"))) {
                            adv_config = tmp.getJSONArray("list");
                        }
                    }
                    juDuoHuiAdvertisement = new JuDuoHuiAdvertisement(mActivity, null);
                    juDuoHuiAdvertisement.setInfomationAdListen(new JuDuoHuiAdvertisement.InfomationAdListen() {
                        @Override
                        public void click(View v) {

                        }

                        @Override
                        public void dislike() {

                        }

                        @Override
                        public void display(View v, String position, JSONObject config) {
                            runOnUiThread(() -> {
                                list_adv.setVisibility(View.VISIBLE);
                                list_adv.removeAllViews();
                                list_adv.addView(v);
                            });
                        }

                        @Override
                        public void displayed() {

                        }

                        @Override
                        public void close() {

                        }

                        @Override
                        public void error(JSONObject error) {
                            mActivity.runOnUiThread(() -> {
                                juDuoHuiAdvertisement.getInfomationAdv(adv_config, error.getString("position"));
                            });
                        }
                    });

                    juDuoHuiAdvertisement.getInfomationAdv(adv_config, "my_ads");

                } else {
                    // Log.d(TAG, "onSuccess: 获取广告配置失败");
                }
            }
        });
    }

    @Override
    protected void initData() {
        HttpUtils.get_third("http://pv.sohu.com/cityjson?ie=utf-8", NewsPubListActivity.this, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // Log.d(TAG, "awfeawefawef: " + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                // Log.d(TAG, "awfeawefawef: " + responseString);
                try {
                    String tmp = responseString.replace("var returnCitySN = ", "").replace(";", "");
                    JSONObject jo = JSONObject.parseObject(tmp);
                    // Log.d(TAG, "awfeawefawef: " + jo.getString("cname"));
                    // Log.d(TAG, "awfeawefawef: " + jo.getString("cip"));
                    ip = jo.getString("cip");
                    area = jo.getString("cname");
                    runOnUiThread(() -> {
                        pub_area.setText(area);
                        pub_area_2.setText(area);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    String FragmentScroll = "no";

    @Override
    protected void initListener() {
        main_scroll.setScrolListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                int[] sss = new int[2];
                tab_bar.getLocationOnScreen(sss);
                Bundle b = new Bundle();
                if (sss[1] < 700) {
                    setStatusBar(Color.parseColor("#FFFFFF"));
                    second_title.setVisibility(View.VISIBLE);
                    FragmentScroll = "yes";
                    b.putString("scroll", "yes");
                }

                if (sss[1] > 710) {
                    setStatusBar(Color.parseColor("#FE7855"));
                    second_title.setVisibility(View.GONE);
                    FragmentScroll = "no";
                    b.putString("scroll", "no");
                }
                // Log.d(TAG, "onScroll: " + Arrays.toString(sss));
                // Log.d(TAG, "onScroll: " +  tab_body.getCurrentItem());


                tabs_body.get(tab_body.getCurrentItem()).setArguments(b);

            }
        });

        main_smart_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getMediaTopInfo();
                // Log.d(TAG, "onRefresh: " + tab_body.getCurrentItem());
                Bundle b = new Bundle();
                b.putString("refresh", "yes");
                tabs_body.get(tab_body.getCurrentItem()).setArguments(b);
                if (adv_config != null && adv_config.size() > 0) {
                    juDuoHuiAdvertisement.getInfomationAdv(adv_config, "my_ads");
                }

            }
        });

    }

    @OnClick({R.id.pub_button, R.id.back_button, R.id.back_button_2, R.id.today_pub_title, R.id.sum_golds_title, R.id.guize})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pub_button:
//                openActivity(NewsPubActivity.class);
                openActivityForResult(NewsPubActivity.class, null, NEW_PUB_ARTICLE);
                break;
            case R.id.back_button_2:
            case R.id.back_button:
                finish();
                break;
            case R.id.today_pub_title:
            case R.id.sum_golds_title:
                Bundle b = new Bundle();
                b.putString("url", "./news_history.html");
                openActivity(JuDuoHuiActivity.class, b);
                break;
            case R.id.guize:
                NewsActivity.actionStart(getComeActivity(), "77", "文章转载规则");
                break;
        }
    }

    private long tabbar_last_click_time = 0;
    private View tabbar_last_click_v = null;

    private void initTabBar() {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setSkimOver(true);
        tab_menus_adapter = new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return tabs.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context) {
                    @Override
                    public void onSelected(int index, int totalCount) {
                        super.onSelected(index, totalCount);
                        setTextSize(UIUtils.dp2px(16));
                    }

                    @Override
                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
                        super.onLeave(index, totalCount, leavePercent, leftToRight);
                        setTextSize(UIUtils.dp2px(16));
                    }
                };
                clipPagerTitleView.setText(tabs.get(index));
                clipPagerTitleView.setTextColor(Color.parseColor("#333333")); // 普通的文字
                clipPagerTitleView.setTextSize(UIUtils.dp2px(16));
                // 左 上 右 下
                clipPagerTitleView.setPadding(UIUtils.dp2px(10), UIUtils.dp2px(8), UIUtils.dp2px(10), UIUtils.dp2px(16));
                clipPagerTitleView.setClipColor(Color.parseColor("#FF5722"));

                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tabbar_last_click_time == 0 || tabbar_last_click_v == null) {
                            tabbar_last_click_v = v;
                            tabbar_last_click_time = System.currentTimeMillis();
                        } else {
                            // 如果上次点击的是这个v,并且两次点击时间间隔小号500，则认为是双击
                            if (tabbar_last_click_v == v && System.currentTimeMillis() - tabbar_last_click_time < 500) {
                                Bundle bundle = new Bundle();
                                bundle.putString("refresh", "yes");
                                tabs_body.get(index).setArguments(bundle);
                                tabbar_last_click_v = v;
                                tabbar_last_click_time = System.currentTimeMillis();
                                return;
                            } else {
                                tabbar_last_click_v = v;
                                tabbar_last_click_time = System.currentTimeMillis();
                            }

                        }

                        // Log.d(TAG, "onClick: " + index);
                        tab_body.setCurrentItem(index);
                        Bundle b = new Bundle();
                        b.putString("scroll", FragmentScroll);
                        tab_body.postDelayed(() -> {
                            tabs_body.get(index).setArguments(b);
                        }, 500);

                    }
                });

                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setLineWidth(UIUtils.dp2px(33));
                indicator.setLineHeight(UIUtils.dp2px(3));
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setColors(getResources().getColor(R.color.aliuser_edittext_bg_color_activated));
                return indicator;
            }
        };
        commonNavigator.setAdapter(tab_menus_adapter);
        tab_bar.setNavigator(commonNavigator);

        for (int i = 0; i < 3; i++) {
            NewsPubListFragment fragment = new NewsPubListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("style_id", (i + 1) + "");
            fragment.setArguments(bundle);
            tabs_body.add(fragment);
        }

        tab_body.setOffscreenPageLimit(tabs_body.size());

        NewsPagerAdapter pagerAdapter = new NewsPagerAdapter(getSupportFragmentManager(), tabs_body);
        tab_body.setAdapter(pagerAdapter);
        ViewPagerHelper.bind(tab_bar, tab_body);
        tab_body.setCurrentItem(0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == NEW_PUB_ARTICLE) {
            // Log.d(TAG, "onActivityResult: ");
            Bundle bundle = new Bundle();
            bundle.putString("refresh", "yes");
            tabs_body.get(0).setArguments(bundle);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMediaTopInfo();
    }
}