package com.android.jdhshop.juduohui;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.DaiLiFuWu;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.juduohui.adapter.NewsListItemAdapter;
import com.android.jdhshop.juduohui.response.HttpJsonResponse;
import com.android.jdhshop.widget.indicator.buildins.UIUtil;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;
public class NewsListFragment extends BaseLazyFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    NewsListItemAdapter newsItemAdapter;
    JSONArray news = new JSONArray();
    Unbinder unbinder;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.right_icon)
    ImageView rightIcon;
    ACache aCache;
    final String TAG = getClass().getSimpleName();
    JSONObject config;
    private int current_page = 1;
    private int page_size = 10;
    public String current_type = "-1";
    Context mContext;
    Activity mActivity;
    boolean is_loading = false;
    boolean is_first_load = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_page, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getContext();
        mActivity = getActivity();
        BaseLogDZiYuan.LogDingZiYuan(rightIcon, "home_btn.png");
        return view;
    }

    private void init() {
        // Log.d(TAG, "init: ");
        aCache = ACache.get(this.getContext());
        Bundle arguments = getArguments();
        config = JSONObject.parseObject(arguments.getString("config"));
        current_type = config.getString("id");
        // Log.d(TAG, "NewsListFragment type" + current_type);

        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, UIUtil.dip2px(getActivity(), 10), 0, UIUtil.dip2px(getActivity(), 10));
        layoutParams.width = RecyclerView.LayoutParams.MATCH_PARENT;
        layoutParams.height = RecyclerView.LayoutParams.WRAP_CONTENT;

        newsItemAdapter = new NewsListItemAdapter(mActivity, news, new NewsListItemAdapter.NewsClickListens() {
            @Override
            public void click(JSONObject news_infomation) {
                Intent intent = new Intent(getActivity(), NewsInformation.class);
                intent.putExtra("config", news_infomation.toJSONString());
                startActivity(intent);
            }

            @Override
            public void load() {
                if (!is_loading) {
                    is_loading = true;
                    current_page = current_page + 1;
                    getNewsList();
                }
            }
        });
        if (current_type.equals("0")) {
            newsItemAdapter.setNewsType(0);
        }
        newsItemAdapter.setHasStableIds(true);
        recyclerView.setAdapter(newsItemAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
    }

    private void addListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                LogUtils.d("TAG", "onRefresh: ");
                news.clear();
                newsItemAdapter.clearHistory();
                newsItemAdapter.notifyDataSetChanged();
                current_page = 1;
                getNewsList();
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                LogUtils.d("TAG", "onLoadMore: ");
                current_page = current_page + 1;
                getNewsList();
            }
        });

        //点击分类
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                SubListByParentChildBean subListByParentChildBean = subListByParentChildBeans.get(i);
//                if (subListByParentChildBean.getName().equals(name)) {
//                    return;
//                }
//                name = subListByParentChildBean.getName();
//                if (shopGridAdapter.getTextView() != null) {
//                    shopGridAdapter.getTextView().setTextColor(getResources().getColor(R.color.black));
//                    shopGridAdapter.setTextView(null);
//                }
//                if (currentSearchView != null)
//                    currentSearchView.setTextColor(getResources().getColor(R.color.black));
//                ((TextView) view.findViewById(R.id.service_name)).setTextColor(getResources().getColor(R.color.red1));
//                currentSearchView = view.findViewById(R.id.service_name);
//                refreshLayout.autoRefresh();
//            }
//        });
        //点击进入详情
//        shopRecyclerAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                HaoDanBean taobaoGuesChildtBean = taobaoGuesChildtBeans.get(position - 1);
//                if (taobaoGuesChildtBean != null) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("num_iid", taobaoGuesChildtBean.itemid);
//                    bundle.putSerializable("bean",taobaoGuesChildtBean);
//                    openActivity(PromotionDetailsActivity.class, bundle);
//                }
//            }
//
//            @Override
//            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
//                return false;
//            }
//        });
    }

    /**
     * @属性:获取屏幕高度的方法
     */
    public long getScollYDistance() {
//        int position = linearLayoutManager.findFirstVisibleItemPosition();
//        View firstVisiableChildView = linearLayoutManager.findViewByPosition(position);
//        int itemHeight = firstVisiableChildView.getHeight();
//        return (position) * itemHeight - firstVisiableChildView.getTop();
        return 0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected void lazyload() { //懒加载，界面开始后刷新
        init();
        addListener();
        getReadConfig();
        getAdvConfig();
    }

    /**
     * 获取阅读配置
     * url: GET_READ_CONFIG
     */
    public void getReadConfig() {
        RequestParams req = new RequestParams();
        HttpUtils.post(Constants.GET_READ_CONFIG, NewsListFragment.this, req, new HttpJsonResponse() {
            @Override
            protected void onSuccess(JSONObject response) {
                super.onSuccess(response);
                // Log.d(TAG, "getReadConfig: " + response.toJSONString());
                if (response.getIntValue("code") == 0) {
                    JSONObject data = response.getJSONObject("data");
                    aCache.put("cycle_interval", data.getString("cycle_interval"));
                    aCache.put("interval_income", data.getString("interval_income"));
                    aCache.put("maxinum_reading_time", data.getString("maxinum_reading_time"));
                    aCache.put("maxinum_reading_time_article", data.getString("maxinum_reading_time_article"));
                    aCache.put("operation_time_limit", data.getString("operation_time_limit"));
                } else {
                    //showToast(response.getString("msg"));
                    // Log.d(TAG, "onSuccess: " + response.toJSONString());
                }
            }
        });
    }

    // 获取广告配置
    private void getAdvConfig() {
        RequestParams req = new RequestParams();
        req.put("identifys", "article_list,content");
        HttpUtils.post(Constants.GET_ADVERTISEMENT_LIST, NewsListFragment.this, req, new HttpJsonResponse() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);

            }

            @Override
            protected void onSuccess(JSONObject response) {
                super.onSuccess(response);
                JSONObject infomation_adv_object = new JSONObject();
                JSONObject content_adv_object = new JSONObject();
                if (response.getIntValue("code") == 0) {
                    for (String key : response.getJSONObject("data").getJSONObject("place_list").keySet()) {
                        JSONObject tmp = response.getJSONObject("data").getJSONObject("place_list").getJSONObject(key);
                        if ("article_list".equals(tmp.getString("identify"))) {
                            infomation_adv_object = tmp;
                        }

                        if ("content".equals(tmp.getString("identify"))) {
                            content_adv_object = tmp;
                        }
                    }
                } else {
                    // Log.d(TAG, "onSuccess: 获取广告配置失败");
                }
                if (SPUtils.getBoolean(mContext, "is_open_ad", true)) {
                    if (infomation_adv_object != null && infomation_adv_object.containsKey("list"))
                        newsItemAdapter.setInfomationAdvConfig(infomation_adv_object);
                }

                if (content_adv_object != null && content_adv_object.containsKey("list"))
                    newsItemAdapter.setContentAdvConfig(content_adv_object);
                // 广告配置加载完毕，获取新闻列表
                getNewsList();
            }
        });
    }

    private void getNewsList() {
        if (is_first_load) {
            showLoadingDialog();
            is_first_load = false;
        }
        RequestParams req = new RequestParams();
        req.put("page", current_page);
        req.put("per", page_size);
        req.put("type_id", current_type);
        HttpUtils.post(Constants.GET_XH_MEDIA_LIB_LIST, NewsListFragment.this, req, new HttpJsonResponse() {
            @Override
            protected void onSuccess(JSONObject response) {
                super.onSuccess(response);
                // Log.d(TAG, "onSuccess: " + response.toJSONString());
                // Log.d(TAG, "onSuccess: " + (response.getIntValue("code") == 0));
                if (response.getIntValue("code") == 0) {

                    if (response.containsKey("request_data")) {
                        DaiLiFuWu.MyService(response.getString("request_data"), mActivity);
                    }

                    int start_size = news.size();
                    int add_item_count = 0;
                    for (int i = 0; i < response.getJSONObject("list").getJSONArray("data").size(); i++) {
                        if (response.getJSONObject("list").getJSONArray("data").getJSONObject(i).getIntValue("has_video") == 1) {
                            continue;
                        }
                        add_item_count++;
                        news.add(response.getJSONObject("list").getJSONArray("data").getJSONObject(i));

                    }
//                    news.addAll(response.getJSONObject("list").getJSONArray("data"));
                    // Log.d(TAG, "onSuccess: " + start_size + ",ssssssss = "+news.size());
                    // Log.d(TAG, "onSuccess: " + news.toJSONString());
//                    newsItemAdapter.notifyDataSetChanged();
                    if (start_size == 0) {
                        // Log.d(TAG, "onSuccess: 首次加载或刷新");
                        newsItemAdapter.notifyDataSetChanged();
                    } else {
                        // Log.d(TAG, "onSuccess: 加载更多");
                        newsItemAdapter.notifyItemInserted(start_size);
                    }

                } else {
                    // Log.d(TAG, "onSuccess: 获取新闻列表失败。");
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                closeLoadingDialog();
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadMore();
                    is_loading = false;
                }
            }
        });
    }

    @OnClick({R.id.right_icon})
    public void onViewClicked(View view) { //回到头部
        switch (view.getId()) {
            case R.id.right_icon:
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.smoothScrollToPosition(0);
                    }
                });
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (newsItemAdapter != null) newsItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Log.d(TAG, "NewsListFragment onActivityResult: requestCode=" +requestCode+",resultCode=" +resultCode+",data "+data);
    }
}