package com.android.jdhshop.juduohui;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.FansBean;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.juduohui.adapter.HomePagePubListItemAdapter;
import com.android.jdhshop.juduohui.response.HttpJsonResponse;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 个人主页搜索
 */
public class HomePageSearchActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.tv_left)
    TextView tv_left;

    @BindView(R.id.tv_no_data)
    TextView tv_no_data;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;

    @BindView(R.id.et_search)
    EditText et_search;

    @BindView(R.id.tv_search)
    TextView tv_search;

    private List<FansBean> fansBeanList = new ArrayList<>();
    private int current_page = 1;
    private int page_size = 10;
    private Gson gson = new Gson();
    private HomePagePubListItemAdapter newsItemAdapter;
    private JSONArray news = new JSONArray();
    private String current_type = "1";
    private int auth_code;
    private String currentSearchContent;
    private int need_update_position = -1; // 需要更新的文章位于列表的位置

    @Override
    protected void initUI() {
        setContentView(R.layout.ac_homepage_search);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        current_type = getIntent().getStringExtra("style_id");
        auth_code = getIntent().getIntExtra("auth_code", 0);
        newsItemAdapter = new HomePagePubListItemAdapter(getComeActivity(), news, new HomePagePubListItemAdapter.NewsClickListens() {
            @Override
            public void click(JSONObject news_infomation, int pos) {
                need_update_position = pos;
                // Log.d(TAG, "click: " + news_infomation);
                if (!news_infomation.containsKey("state") || news_infomation.getIntValue("state") == 2 || news_infomation.getIntValue("state") == 5) {
                    Intent intent = new Intent(getComeActivity(), NewsInformation.class);
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
            }

            @Override
            public void update(int pos) {
                need_update_position = pos;
                getNewsInfomation();
            }
        });
        newsItemAdapter.setCurrentType(current_type);
        recyclerView.setFocusableInTouchMode(false);
        LinearLayoutManager manager = new LinearLayoutManager(getComeActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setFocusableInTouchMode(false);
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(newsItemAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (-1 != need_update_position) {
//            getNewsInfomation();
        }
    }

    @Override
    protected void initListener() {
        tv_left.setVisibility(View.VISIBLE);
        tv_left.setOnClickListener(v -> finish());
        tv_search.setOnClickListener(v -> {
            String searchContent = et_search.getText().toString();
            if (!TextUtils.isEmpty(searchContent)) {
                refresh(searchContent);
            }
        });
        et_search.setOnEditorActionListener((v, actionId, event) -> {
            if (EditorInfo.IME_ACTION_SEARCH == actionId) {
                closeInputMethod();
                String searchContent = et_search.getText().toString();
                if (!TextUtils.isEmpty(searchContent)) {
                    refresh(searchContent);
                }
                return true;
            }
            return false;
        });
        refresh_layout.setOnLoadMoreListener(refreshLayout -> {
            String searchContent = et_search.getText().toString();
            if (!TextUtils.isEmpty(searchContent)) {//当前内容不为空
                //不相等就刷新
                if (!searchContent.equals(currentSearchContent)) {
                    refresh(searchContent);
                    //相等就继续加载
                } else {
                    current_page++;
                    getNewsPubList(currentSearchContent);
                }
            } else {
                current_page++;
                getNewsPubList(currentSearchContent);
            }
        });
        refresh_layout.setEnableRefresh(false);
    }

    private void refresh(String searchContent) {
        currentSearchContent = searchContent;
        current_page = 1;
        news.clear();
        newsItemAdapter.clearHistory();
        newsItemAdapter.notifyDataSetChanged();
        refresh_layout.setEnableLoadMore(true);
        getNewsPubList(searchContent);
    }

    /**
     * 搜索文章
     *
     * @param content
     */
    private void getNewsPubList(String content) {
        RequestParams req = new RequestParams();
        req.put("page", current_page);
        req.put("per", page_size);
        req.put("search", content);
        req.put("style_id", current_type);
        req.put("no_reprint", 0);
        req.put("auth_code", auth_code);
        req.put("type", 0);
        HttpUtils.post(Constants.MEDIA_LIB_REPORT_LIST, HomePageSearchActivity.this, req, new HttpJsonResponse() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                LogUtils.log("HomePageSearchActivity getNewsPubList onFailure = " + responseString);
            }

            @Override
            protected void onSuccess(JSONObject response) {
                LogUtils.log("HomePageSearchActivity getNewsPubList onSuccess = " + response.toJSONString());
                // Log.d(TAG, "onSuccess: " + (response.getIntValue("code") == 0));
                if (response.getIntValue("code") == 0) {
                    JSONObject list = response.getJSONObject("list");
                    if (list.getJSONArray("data").size() > 0) {
                        // Log.d(TAG, "onSuccess: 有数据");
                        news.addAll(list.getJSONArray("data"));
                    }
                    refresh_layout.finishLoadMore();
                    if (current_page == 1 && list.getJSONArray("data").size() == 0) {
                        // Log.d(TAG, "onSuccess: 没有数据");
                        refresh_layout.setEnableLoadMore(false);
                        tv_no_data.setVisibility(View.VISIBLE);
                    } else {
                        if (list.getJSONArray("data").size() == 0) {
                            refresh_layout.finishLoadMoreWithNoMoreData();
                        }
                        tv_no_data.setVisibility(View.GONE);
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
                        tv_no_data.setVisibility(View.VISIBLE);
                    } else {
                        refresh_layout.finishLoadMoreWithNoMoreData();
                    }
                }
            }

            @Override
            public void onFinish() {
                // 隐藏进度条
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            public void onStart() {
                // 显示进度条
                super.onStart();
                showLoadingDialog();
            }
        });
    }

    private void getNewsInfomation() {
        int need_update_id = news.getJSONObject(need_update_position).getIntValue("id");
        // Log.d(TAG, "getNewsInfomation: ");
        RequestParams req = new RequestParams();
        req.put("id", need_update_id);
        req.put("style_id", current_type);
        req.put("auth_code", auth_code);

        HttpUtils.post(Constants.GET_XH_MEDIA_LIB, HomePageSearchActivity.this, req, new HttpJsonResponse() {
            @Override
            protected void onSuccess(JSONObject response) {
                LogUtils.log("HomePageSearchActivity getNewsInfomation onSuccess " + response.toJSONString());
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

}