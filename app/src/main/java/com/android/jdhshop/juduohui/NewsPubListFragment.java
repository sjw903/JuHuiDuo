package com.android.jdhshop.juduohui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.jdhshop.juduohui.adapter.NewsPubListItemAdapter;
import com.android.jdhshop.juduohui.response.HttpJsonResponse;
import com.android.jdhshop.widget.indicator.buildins.UIUtil;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;

public class NewsPubListFragment extends BaseLazyFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.no_data)
    LinearLayout no_data;
    NewsPubListItemAdapter newsItemAdapter;
    JSONArray news = new JSONArray();
    Unbinder unbinder;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.right_icon)
    ImageView rightIcon;
    ACache aCache;
    JSONArray adv_config;
    String interval_num;
    Context mContext;
    final String TAG = getClass().getSimpleName();
    private int current_page = 1;
    private int page_size = 10;
    public String current_type = "1";
    Activity mActivity;
    private int need_update_id = 0; //需要更新的文章ID
    private int need_update_position = 0; // 需要更新的文章位于列表的位置
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_page, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getContext();
        mActivity = getActivity();
        init();
        addListener();
        BaseLogDZiYuan.LogDingZiYuan(rightIcon, "home_btn.png");
        return view;
    }
    public interface NewsClickListens {
        void click(JSONObject news_infomation);
        void refresh();
    }

    private void init() {
        aCache = ACache.get(this.getContext());
        Bundle arguments = getArguments();
        current_type = arguments.getString("style_id");
        // Log.d(TAG, "init: 走这里！" + current_type);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, UIUtil.dip2px(getActivity(), 10), 0, UIUtil.dip2px(getActivity(), 10));
        layoutParams.width = RecyclerView.LayoutParams.MATCH_PARENT;
        layoutParams.height = RecyclerView.LayoutParams.WRAP_CONTENT;

        newsItemAdapter = new NewsPubListItemAdapter(mActivity,news, new NewsPubListFragment.NewsClickListens(){
            @Override
            public void click(JSONObject news_infomation) {
                // Log.d(TAG, "click: " + news_infomation);
                if (!news_infomation.containsKey("state") || news_infomation.getIntValue("state") == 2 || news_infomation.getIntValue("state") == 5){


                    need_update_id = news_infomation.getIntValue("id");
                    for (int i=0;i<news.size();i++){
                        JSONObject item = news.getJSONObject(i);
                        if (item.getIntValue("id") == need_update_id){
                            need_update_position = i;
                            break;
                        }
                    }

                    Intent intent = new Intent(getActivity(),NewsInformation.class);
                    intent.putExtra("config",news_infomation.toJSONString());
                    startActivity(intent);
                }
                else if (news_infomation.getIntValue("state") == 7){
                    showToast("此文章已删除，不可以进入详情页");
                }
                else{
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
        });
        newsItemAdapter.setCurrentType(current_type);

        recyclerView.setAdapter(newsItemAdapter);
        recyclerView.setFocusableInTouchMode(false);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        recyclerView.setFocusableInTouchMode(false);
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);

    }
    private void addListener(){
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                LogUtils.d("TAG", "onRefresh: ");
                news.clear();
                newsItemAdapter.clearHistory();
                newsItemAdapter.notifyDataSetChanged();
                current_page = 1;
                getNewsPubList();
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                LogUtils.d("TAG", "onLoadMore: ");
                current_page = current_page + 1;
                getNewsPubList();
            }
        });
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        // Log.d(TAG, "setArguments: " + args);
        if (recyclerView!=null) {

            if ("yes".equals(args.getString("refresh"))){
                current_page = 1;
                news.clear();
                newsItemAdapter.notifyDataSetChanged();
                refreshLayout.autoRefresh();

            }


            if ("yes".equals(args.getString("scroll"))) {
                recyclerView.setFocusableInTouchMode(true);
                recyclerView.setFocusable(true);
                recyclerView.setNestedScrollingEnabled(true);
            } else {
                recyclerView.setFocusableInTouchMode(false);
                recyclerView.setFocusable(false);
                recyclerView.setNestedScrollingEnabled(false);
            }
        }
    }

    // 获取广告配置
    private void getAdvConfig(){
        RequestParams req = new RequestParams();
        req.put("identifys","publish_content");
        HttpUtils.post(Constants.GET_ADVERTISEMENT_LIST ,NewsPubListFragment.this, req, new HttpJsonResponse() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);

            }

            @Override
            protected void onSuccess(JSONObject response) {
                super.onSuccess(response);
                if (response.getIntValue("code") == 0){
                    for (String key : response.getJSONObject("data").getJSONObject("place_list").keySet()){
                        JSONObject tmp = response.getJSONObject("data").getJSONObject("place_list").getJSONObject(key);
                        if ("publish_content".equals(tmp.getString("identify"))){
                            adv_config = tmp.getJSONArray("list");
                            interval_num = tmp.getString("interval_num");
                        }
                    }
                }
                else{
                    // Log.d(TAG, "onSuccess: 获取广告配置失败");
                }
                // Log.d(TAG, "onSuccess: " + adv_config);
                if (SPUtils.getBoolean(mContext,"is_open_ad",true)) {
                    newsItemAdapter.setAdvConfig(adv_config,interval_num);
                }
                // 广告配置加载完毕，获取新闻列表
                getNewsPubList();
            }
        });
    }

    @Override
    protected void lazyload() {
        getAdvConfig();
    }
    private void getNewsPubList(){
        RequestParams req = new RequestParams();
        req.put("page",current_page);
        req.put("per",page_size);
        req.put("style_id",current_type);
        HttpUtils.post(Constants.MEDIA_LIB_REPORT_LIST, NewsPubListFragment.this,req, new HttpJsonResponse() {
            @Override
            protected void onSuccess(JSONObject response) {
                super.onSuccess(response);
                // Log.d(TAG, "onSuccess: " + response.toJSONString());
                // Log.d(TAG, "onSuccess: " + (response.getIntValue("code") == 0));
                if (response.getIntValue("code") == 0){

                    if (response.getJSONObject("list").getJSONArray("data").size()>0) {
                        // Log.d(TAG, "onSuccess: 有数据");
                        news.addAll(response.getJSONObject("list").getJSONArray("data"));
                        // Log.d(TAG, "onSuccess: " + news.size());
                        // Log.d(TAG, "onSuccess: " + news.toJSONString());
                    }

                    if (current_page == 1 && response.getJSONObject("list").getJSONArray("data").size()==0){
                        // Log.d(TAG, "onSuccess: 没有数据");
                        no_data.setVisibility(View.VISIBLE);

                    }
                    else{
                        no_data.setVisibility(View.GONE);

                    }
                    newsItemAdapter.setCurrentType(current_type);
                    newsItemAdapter.notifyDataSetChanged();
                }
                else{
                    // Log.d(TAG, "onSuccess: 获取新闻列表失败。");
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (refreshLayout!=null) {
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadMore();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (need_update_id!=0){
            getNewsInfomation();
        }
    }
    private void getNewsInfomation(){
        // Log.d(TAG, "getNewsInfomation: ");
        RequestParams req = new RequestParams();
        req.put("id",need_update_id);
        req.put("notupdate","1");
        HttpUtils.post(Constants.GET_XH_MEDIA_LIB, NewsPubListFragment.this,req, new HttpJsonResponse() {
            @Override
            protected void onSuccess(JSONObject response) {
                super.onSuccess(response);
                if (response.getIntValue("code") == 0){
                    // Log.d(TAG, "onSuccess: " + response.toJSONString());
                    JSONObject old_item = news.getJSONObject(need_update_position);
                    old_item.remove("gold");
                    old_item.remove("readnum");
                    old_item.put("gold",response.getJSONObject("data").getIntValue("get_gold_sum"));
                    old_item.put("readnum",response.getJSONObject("data").getIntValue("readnum"));
                    newsItemAdapter.notifyItemChanged(need_update_position);
                    need_update_id = 0;
                    need_update_position = 0;
                }
                else{
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
}