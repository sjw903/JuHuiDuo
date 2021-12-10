package com.android.jdhshop.juduohui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.FansBean;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.juduohui.response.HttpJsonResponse;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
public class AttentionSearchActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;

    @BindView(R.id.tv_left)
    TextView tv_left;


    @BindView(R.id.tv_no_data)
    TextView tv_no_data;


    @BindView(R.id.et_search)
    EditText et_search;

    @BindView(R.id.tv_search)
    TextView tv_search;

    private List<FansBean> fansBeanList = new ArrayList<>();
    private int current_page = 1;
    private int page_size = 10;
    private Gson gson = new Gson();
    private BaseQuickAdapter<FansBean, BaseViewHolder> adapter;
    private String current_type = "1";
    private String currentSearchContent;

    @Override
    protected void initUI() {
        setContentView(R.layout.ac_attention_search);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        adapter = new BaseQuickAdapter<FansBean, BaseViewHolder>(R.layout.item_attention, fansBeanList) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, FansBean fansBean) {
                TextView name = baseViewHolder.getView(R.id.tv_name);
                name.setText(fansBean.user_name);
                ImageView imageView = baseViewHolder.getView(R.id.iv_head);
                TextView flag = baseViewHolder.getView(R.id.tv_flag);
                int is_mutual = fansBean.is_mutual;
                if (0 == is_mutual) {
                    flag.setText("已关注");
                    flag.setTextColor(getResources().getColor(R.color.white));
                    flag.setBackground(getResources().getDrawable(R.drawable.attention_submit_high_bg));
                } else {
                    flag.setText("关注");
                    flag.setTextColor(getResources().getColor(R.color.black));
                    flag.setBackground(getResources().getDrawable(R.drawable.attention_submit_normal_bg));
                }
                flag.setOnClickListener(v -> {
                    requestAttention(is_mutual, fansBean.auth_code, baseViewHolder.getLayoutPosition());
                });
                Glide.with(getComeActivity()).load(fansBean.auth_icon).placeholder(R.mipmap.ic_launcher).into(imageView);
            }
        };
        LinearLayoutManager manager = new LinearLayoutManager(getComeActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setFocusableInTouchMode(false);
        recyclerView.setFocusable(false);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        tv_left.setVisibility(View.VISIBLE);
        tv_left.setOnClickListener(v -> finish());
        tv_search.setOnClickListener(v -> {
            String searchContent = et_search.getText().toString();
            if (!TextUtils.isEmpty(searchContent)) {
                currentSearchContent = searchContent;
                current_page = 1;
                fansBeanList.clear();
                adapter.notifyDataSetChanged();
                refresh_layout.setEnableLoadMore(true);
                requestData(searchContent);
            }
        });
        et_search.setOnEditorActionListener((v, actionId, event) -> {
            if (EditorInfo.IME_ACTION_SEARCH == actionId) {
                closeInputMethod();
                String searchContent = et_search.getText().toString();
                if (!TextUtils.isEmpty(searchContent)) {
                    currentSearchContent = searchContent;
                    current_page = 1;
                    fansBeanList.clear();
                    adapter.notifyDataSetChanged();
                    refresh_layout.setEnableLoadMore(true);
                    requestData(searchContent);
                }
                return true;
            }
            return false;
        });
        refresh_layout.setOnLoadMoreListener(refreshLayout -> {
            String searchContent = et_search.getText().toString();
            if (!TextUtils.isEmpty(searchContent)) {
                if (!searchContent.equals(currentSearchContent)) {
                    currentSearchContent = searchContent;
                    current_page = 1;
                    fansBeanList.clear();
                    adapter.notifyDataSetChanged();
                    refresh_layout.setEnableLoadMore(true);
                    requestData(searchContent);
                } else {
                    requestData(currentSearchContent);
                }
            } else {
                requestData(currentSearchContent);
            }

        });
        refresh_layout.setEnableRefresh(false);
    }

    /**
     * @param content 搜索内容
     *                请求数据
     */
    private void requestData(String content) {
        if (!CommonUtils.isNetworkAvailable()) {
            showToast(getResources().getString(R.string.error_network));
            return;
        }

        RequestParams params = new RequestParams();
        params.put("page", current_page);
        params.put("per", page_size);
        params.put("search", content);

        HttpUtils.post(Constants.GET_FOLLOW_LIST, AttentionSearchActivity.this, params, new HttpJsonResponse() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // 输出错误信息
                LogUtils.e(TAG, "AttentionSearchActivity onFailure = " + responseString);
            }

            @Override
            public void onFinish() {
                // 隐藏进度条
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            protected void onSuccess(JSONObject response) {
                LogUtils.d("AttentionSearchActivity requestData onSuccess = " + response.toJSONString());
                if (response.getIntValue("code") == 0) {
                    current_page++;
                    if (response.getJSONObject("list").getJSONArray("data").size() > 0) {
                        List<FansBean> list = gson.fromJson(response.getJSONObject("list").getJSONArray("data").toJSONString(), new TypeToken<List<FansBean>>() {
                        }.getType());
                        if (null != list) {
                            tv_no_data.setVisibility(View.GONE);
                            fansBeanList.addAll(list);
                            adapter.notifyDataSetChanged();
                        }
                        refresh_layout.finishLoadMore();
                    }
                } else {
                    String msg = response.getString("msg");
                    if (!TextUtils.isEmpty(msg)) {
                        showToast(msg);
                    }
                    if (1 == current_page) {
                        tv_no_data.setVisibility(View.VISIBLE);
                        refresh_layout.finishLoadMore();
                        refresh_layout.setEnableLoadMore(false);
                    } else {
                        refresh_layout.finishLoadMoreWithNoMoreData();
                    }
                }
            }

            @Override
            public void onStart() {
                // 显示进度条
                super.onStart();
                showLoadingDialog();
            }
        });

    }

    /**
     * 请求关注
     *
     * @param state     处理状态 0.取消 1.添加
     * @param auth_code 访问的个人页面的邀请码信息
     * @param position  用来更新的位置
     */
    private void requestAttention(int state, String auth_code, int position) {
        if (!CommonUtils.isNetworkAvailable()) {
            showToast(getResources().getString(R.string.error_network));
            return;
        }

        RequestParams params = new RequestParams();
        params.put("auth_code", auth_code);
        params.put("state", state);

        HttpUtils.post(Constants.UPDATE_FOLLOW_LIST, AttentionSearchActivity.this, params, new HttpJsonResponse() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // 输出错误信息
                LogUtils.e(TAG, "onFailure()--" + responseString);
            }

            @Override
            public void onFinish() {
                // 隐藏进度条
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            protected void onSuccess(com.alibaba.fastjson.JSONObject response) {
                LogUtils.d("requestAttention onSuccess = " + response.toJSONString());
                if (response.getIntValue("code") == 0) {
                    updateAttentionInfo(position);
                } else {
                    String msg = response.getString("msg");
                    if (!TextUtils.isEmpty(msg)) {
                        showToast(msg);
                    }
                }
            }

            @Override
            public void onStart() {
                // 显示进度条
                super.onStart();
                showLoadingDialog();
            }
        });
    }

    /**
     * 更新指定位置状态
     *
     * @param position
     */
    void updateAttentionInfo(int position) {
        FansBean changedItem = adapter.getData().get(position);
        int is_mutual = changedItem.is_mutual;
        if (is_mutual == 0) {
            changedItem.is_mutual = 1;
        } else {
            changedItem.is_mutual = 0;
        }
        adapter.notifyItemChanged(position);
    }
}