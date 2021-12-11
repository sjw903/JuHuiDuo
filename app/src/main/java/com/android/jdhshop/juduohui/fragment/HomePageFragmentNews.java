package com.android.jdhshop.juduohui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.juduohui.NewsInformation;
import com.android.jdhshop.juduohui.adapter.HomePagePubListItemAdapter;
import com.android.jdhshop.juduohui.response.HttpJsonResponse;
import com.android.jdhshop.widget.indicator.buildins.UIUtil;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;

/**
 * 主页发布的新闻
 */
public class HomePageFragmentNews extends BaseLazyFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.no_data)
    LinearLayout no_data;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;

    @BindView(R.id.right_icon)
    ImageView rightIcon;

    @BindView(R.id.cb_header)
    CheckBox cb_header;

    HomePagePubListItemAdapter newsItemAdapter;
    JSONArray news = new JSONArray();
    Unbinder unbinder;

    ACache aCache;
    JSONArray adv_config;
    String interval_num;
    Context mContext;
    private int current_page = 1;
    private int page_size = 10;
    public String current_type = "1";
    Activity mActivity;
    private int need_update_position = -1; // 需要更新的文章位于列表的位置
    private int type = 0;
    private int no_reprint = 0;
    private int auth_code;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage_news, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getContext();
        mActivity = getActivity();
        init();
        addListener();
        BaseLogDZiYuan.LogDingZiYuan(rightIcon, "home_btn.png");
        return view;
    }

    private void init() {
        aCache = ACache.get(this.getContext());
        Bundle arguments = getArguments();
        current_type = arguments.getString("style_id");
        type = arguments.getInt("type");
        auth_code = arguments.getInt("auth_code");
        // Log.d(TAG, "init: 走这里！" + current_type);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, UIUtil.dip2px(getActivity(), 10), 0, UIUtil.dip2px(getActivity(), 10));
        layoutParams.width = RecyclerView.LayoutParams.MATCH_PARENT;
        layoutParams.height = RecyclerView.LayoutParams.WRAP_CONTENT;

        newsItemAdapter = new HomePagePubListItemAdapter(mActivity, news, new HomePagePubListItemAdapter.NewsClickListens() {
            @Override
            public void click(JSONObject news_infomation, int pos) {
                // Log.d(TAG, "click: " + news_infomation);
                if (!news_infomation.containsKey("state") || news_infomation.getIntValue("state") == 2 || news_infomation.getIntValue("state") == 5) {
                    need_update_position = pos;
                    Intent intent = new Intent(getActivity(), NewsInformation.class);
                    intent.putExtra("config", news_infomation.toJSONString());
                    startActivity(intent);
                } else if (news_infomation.getIntValue("state") == 7) {
                    showToast("此文章已删除，不可以进入详情页");
                } else {
                    showToast("此文章审核还未通过，不可以进入详情页");
                }

            }

            @Override
            public void refresh() {
                current_page = 1;
                news.clear();
                newsItemAdapter.clearHistory();
                newsItemAdapter.notifyDataSetChanged();
                getNewsPubList();
            }

            @Override
            public void update(int pos) {
                need_update_position = pos;
                getNewsInfomation();
            }
        });
        newsItemAdapter.setCurrentType(current_type);

        recyclerView.setAdapter(newsItemAdapter);
        recyclerView.setFocusableInTouchMode(false);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setFocusableInTouchMode(false);
        recyclerView.setFocusable(false);
        cb_header.setOnCheckedChangeListener((buttonView, isChecked) -> {
            news.clear();
            refresh_layout.setEnableLoadMore(true);
            newsItemAdapter.clearHistory();
            newsItemAdapter.notifyDataSetChanged();
            current_page = 1;
            getNewsPubList();
        });
    }


    private void addListener() {
//        refreshLayout.setOnRefreshListener(refreshLayout -> {
//            LogUtils.d("TAG", "onRefresh: ");
//            news.clear();
//            newsItemAdapterAll.clearHistory();
//            newsItemAdapterAll.notifyDataSetChanged();
//            current_page = 1;
//            getNewsPubList();
//        });
        refresh_layout.setEnableRefresh(false);//是否启用下拉刷新功能
        refresh_layout.setOnLoadMoreListener(refreshLayout -> {
            LogUtils.d("TAG", "onLoadMore: ");
            current_page = current_page + 1;
            getNewsPubList();
        });
    }

    // 获取广告配置
    private void getAdvConfig() {
        RequestParams req = new RequestParams();
        req.put("identifys", "publish_content");
        HttpUtils.post(Constants.GET_ADVERTISEMENT_LIST, HomePageFragmentNews.this, req, new HttpJsonResponse() {
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
                            interval_num = tmp.getString("interval_num");
                        }
                    }
                } else {
                    // Log.d(TAG, "onSuccess: 获取广告配置失败");
                }
                // Log.d(TAG, "onSuccess: " + adv_config);
                if (SPUtils.getBoolean(mContext, "is_open_ad", true)) {
                    newsItemAdapter.setAdvConfig(adv_config, interval_num);
                }
                // 广告配置加载完毕，获取新闻列表
                getNewsPubList();
            }
        });
    }

    @Override
    protected void lazyload() {
        getNewsPubList();
    }

    private void getNewsPubList() {
        checkBoxState();
        RequestParams req = new RequestParams();
        req.put("page", current_page);
        req.put("per", page_size);
        req.put("style_id", current_type);
        req.put("no_reprint", no_reprint);
        req.put("auth_code", auth_code);
        req.put("type", type);
        HttpUtils.post(Constants.MEDIA_LIB_REPORT_LIST, HomePageFragmentNews.this, req, new HttpJsonResponse() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                LogUtils.log("HomePageFragmentNews getNewsPubList onFailure = " + responseString);
            }

            @Override
            protected void onSuccess(JSONObject response) {
                LogUtils.log("HomePageFragmentNews getNewsPubList onSuccess = " + response.toJSONString());
                // Log.d(TAG, "onSuccess: " + (response.getIntValue("code") == 0));
                if (response.getIntValue("code") == 0) {
                    JSONObject list = response.getJSONObject("list");
                    if (list.getJSONArray("data").size() > 0) {
                        // Log.d(TAG, "onSuccess: 有数据");
                        news.addAll(list.getJSONArray("data"));
                        // Log.d(TAG, "onSuccess: " + news.size());
                        // Log.d(TAG, "onSuccess: " + news.toJSONString());
                    }
                    refresh_layout.finishLoadMore();
                    if (current_page == 1 && list.getJSONArray("data").size() == 0) {
                        // Log.d(TAG, "onSuccess: 没有数据");
                        refresh_layout.setEnableLoadMore(false);
                        no_data.setVisibility(View.VISIBLE);
                    } else {
                        if (list.getJSONArray("data").size() == 0) {
                            refresh_layout.finishLoadMoreWithNoMoreData();
                        }
                        no_data.setVisibility(View.GONE);
                    }
                    boolean is_me = list.getBooleanValue("is_me");
                    newsItemAdapter.setIs_me(is_me);
                    newsItemAdapter.setCurrentType(current_type);
                    newsItemAdapter.notifyDataSetChanged();
                } else {
                    String msg = response.getString("msg");
                    if (!TextUtils.isEmpty(msg)) {
                        showToast(msg);
                    }
                    if (1 == current_page) {
                        no_data.setVisibility(View.VISIBLE);
                    } else {
                        refresh_layout.finishLoadMoreWithNoMoreData();
                    }

                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (-1 != need_update_position) {
//            getNewsInfomation();
        }
    }

    private void getNewsInfomation() {
        int need_update_id = news.getJSONObject(need_update_position).getIntValue("id");
        // Log.d(TAG, "getNewsInfomation: ");
        checkBoxState();
        RequestParams req = new RequestParams();
        req.put("id", need_update_id);
        req.put("style_id", current_type);
        req.put("no_reprint", no_reprint);
        req.put("auth_code", auth_code);
        req.put("type", type);
        HttpUtils.post(Constants.GET_XH_MEDIA_LIB, HomePageFragmentNews.this, req, new HttpJsonResponse() {
            @Override
            protected void onSuccess(JSONObject response) {
                LogUtils.log("getNewsInfomation onSuccess " + response.toJSONString());
                super.onSuccess(response);
                if (response.getIntValue("code") == 0) {
                    // Log.d(TAG, "onSuccess: " + response.toJSONString());
                    news.set(need_update_position, response.getJSONObject("data"));
                    newsItemAdapter.notifyItemChanged(need_update_position);
                    need_update_position = -1;
                } else {
                    // Log.d(TAG, "onSuccess: 失败");
                    showToast("获取文章详情失败");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);
                // Log.d(TAG, "onFailure: " + responseString);
            }
        });
    }

    /**
     * 获取checkbox状态,设置是否获取转载状态
     */
    private void checkBoxState() {
        if (cb_header.isChecked()) {
            no_reprint = 1;
        } else {
            no_reprint = 0;
        }
    }
}
