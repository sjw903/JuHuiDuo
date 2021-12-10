package com.android.jdhshop.juduohui;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
 * 粉丝界面
 */
public class FansActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    @BindView(R.id.tv_left)
    TextView tv_left;


    @BindView(R.id.tv_no_data)
    TextView tv_no_data;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;

    private List<FansBean> fansBeanList = new ArrayList<>();
    private int current_page = 1;
    private int page_size = 10;
    private Gson gson = new Gson();
    private BaseQuickAdapter<FansBean, BaseViewHolder> adapter;

    @Override
    protected void initUI() {
        setContentView(R.layout.ac_fans);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        adapter = new BaseQuickAdapter<FansBean, BaseViewHolder>(R.layout.item_fans, fansBeanList) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, FansBean fansBean) {
                TextView name = baseViewHolder.getView(R.id.tv_name);
                name.setText(fansBean.user_name);
                ImageView imageView = baseViewHolder.getView(R.id.iv_head);
                TextView flag = baseViewHolder.getView(R.id.tv_flag);
                int is_mutual = fansBean.is_mutual;
                if (0 == is_mutual) {
                    flag.setText("关注");
                    flag.setBackground(getResources().getDrawable(R.drawable.news_share_button));
                } else {
                    flag.setText("互关注");
                    flag.setBackground(getResources().getDrawable(R.drawable.aa));
                }
                flag.setOnClickListener(v -> {
                    requestAttention(is_mutual, fansBean.auth_code, baseViewHolder.getLayoutPosition());
                });
                Glide.with(getComeActivity()).load(fansBean.auth_icon).into(imageView);
            }
        };
        recyclerView.setAdapter(adapter);
        refresh_layout.setEnableRefresh(false);
        requestData();
    }

    @Override
    protected void initListener() {
        refresh_layout.setOnLoadMoreListener(refreshLayout -> {
            requestData();
        });
        tv_left.setVisibility(View.VISIBLE);
        tv_left.setOnClickListener(v -> finish());
        tv_title.setText(R.string.homepage_search_fans_title);
    }

    /**
     * 请求数据
     */
    private void requestData() {
        if (!CommonUtils.isNetworkAvailable()) {
            showToast(getResources().getString(R.string.error_network));
            return;
        }

        RequestParams params = new RequestParams();
        params.put("page", current_page);
        params.put("per", page_size);

        HttpUtils.post(Constants.GET_FANS_LIST, FansActivity.this, params, new HttpJsonResponse() {
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
                LogUtils.d("requestData onSuccess = " + response.toJSONString());
                if (response.getIntValue("code") == 0) {
                    if (View.GONE != tv_no_data.getVisibility()) {
                        tv_no_data.setVisibility(View.GONE);
                    }
                    current_page++;
                    if (response.getJSONObject("list").getJSONArray("data").size() > 0) {
                        List<FansBean> list = gson.fromJson(response.getJSONObject("list").getJSONArray("data").toJSONString(), new TypeToken<List<FansBean>>() {
                        }.getType());
                        if (null != list) {
                            fansBeanList.addAll(list);
                        }
                        if (null != refresh_layout) {
                            refresh_layout.finishLoadMore();
                        }
                    } else {
                        if (null != refresh_layout) {
                            refresh_layout.finishLoadMoreWithNoMoreData();
                        }
                    }
                } else {
                    String msg = response.getString("msg");
                    if (!TextUtils.isEmpty(msg)) {
                        showToast(msg);
                    }
                    if (current_page == 1) {
                        if (View.VISIBLE != tv_no_data.getVisibility()) {
                            tv_no_data.setVisibility(View.VISIBLE);
                        }
                        if (null != refresh_layout) {
                            refresh_layout.finishLoadMore();
                            refresh_layout.setEnableLoadMore(false);
                        }
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

        HttpUtils.post(Constants.UPDATE_FOLLOW_LIST, FansActivity.this, params, new HttpJsonResponse() {
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