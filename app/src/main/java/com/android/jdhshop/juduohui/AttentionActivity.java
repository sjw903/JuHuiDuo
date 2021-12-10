package com.android.jdhshop.juduohui;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.AttentionBean;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.dialog.CustomDialog;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.juduohui.response.HttpJsonResponse;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.ViewConvertListener;
import com.shehuan.nicedialog.ViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 我关注的用户
 * 用户界面
 */
public class AttentionActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    @BindView(R.id.tv_left)
    TextView tv_left;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_no_data)
    TextView tv_no_data;

    @BindView(R.id.tv_order_value)
    TextView tv_order_value;


    @BindView(R.id.tv_search)
    TextView tv_search;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;

    private int current_page = 1;
    private int page_size = 20;
    private List<String> stringList;
    private Gson gson = new Gson();
    private List<AttentionBean> attentionBeanList = new ArrayList<>();
    private BaseQuickAdapter<AttentionBean, BaseViewHolder> adapter;
    private int attentionIndex = 0;

    @Override
    protected void initUI() {
        setContentView(R.layout.ac_attention);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        String[] attentionOrder = getResources().getStringArray(R.array.homepage_search_attention_order);
        stringList = Arrays.asList(attentionOrder);
        adapter = new BaseQuickAdapter<AttentionBean, BaseViewHolder>(R.layout.item_attention, attentionBeanList) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, AttentionBean fansBean) {
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
        recyclerView.setAdapter(adapter);
        requestData();
    }

    @Override
    protected void initListener() {
        refresh_layout.setOnLoadMoreListener(refreshLayout -> {
            requestData();
        });
        refresh_layout.setEnableRefresh(false);
        tv_left.setVisibility(View.VISIBLE);
        tv_left.setOnClickListener(v -> finish());
        tv_title.setText(R.string.homepage_search_attention_title);
        tv_order_value.setText(stringList.get(attentionIndex));
        tv_order_value.setOnClickListener(v -> {
            setTextDrawableRight(R.mipmap.icon_up);
            CustomDialog customDialog = CustomDialog.init();
            customDialog.setDismissListener(() -> {
                setTextDrawableRight(R.mipmap.icon_down);
            });
            customDialog
                    .setLayoutId(R.layout.dialog_attention_order)
                    .setConvertListener(new ViewConvertListener() {
                        @Override
                        public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                            RecyclerView recyclerView = holder.getView(R.id.recyclerView);
                            BaseQuickAdapter<String, BaseViewHolder> adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_attention_order, stringList) {
                                @Override
                                protected void convert(BaseViewHolder baseViewHolder, String s) {
                                    TextView textView = baseViewHolder.getView(R.id.tv_text);
                                    textView.setText(s);
                                    if (s.equals(stringList.get(attentionIndex))) {
                                        textView.setTextColor(getResources().getColor(R.color.red));
                                    } else {
                                        textView.setTextColor(getResources().getColor(R.color.black));
                                    }
                                    textView.setOnClickListener(v1 -> {
                                        dialog.dismissAllowingStateLoss();
                                        getTypeAndRequest(s);
                                    });
                                }
                            };
                            recyclerView.setAdapter(adapter);
                            TextView title = holder.getView(R.id.tv_title);
                            title.setText(getString(R.string.homepage_search_attention_order_title));
                            TextView cancel = holder.getView(R.id.tv_cancel);
                            cancel.setText(getString(android.R.string.cancel));
                            cancel.setOnClickListener(v1 -> {
                                dialog.dismissAllowingStateLoss();
                            });
                        }
                    })
                    .setGravity(Gravity.BOTTOM)
                    .show(getSupportFragmentManager());

        });
        tv_search.setOnClickListener(v -> openActivity(AttentionSearchActivity.class));
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
        params.put("order_type", attentionIndex);
        HttpUtils.post(Constants.GET_FOLLOW_LIST, AttentionActivity.this, params, new HttpJsonResponse() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogUtils.e(TAG, "AttentionActivity onFailure = " + responseString);
            }

            @Override
            public void onFinish() {
                // 隐藏进度条
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            protected void onSuccess(com.alibaba.fastjson.JSONObject response) {
                LogUtils.d("AttentionActivity requestData onSuccess = " + response.toJSONString());
                if (response.getIntValue("code") == 0) {
                    if (View.GONE != tv_no_data.getVisibility()) {
                        tv_no_data.setVisibility(View.GONE);
                    }
                    current_page++;
                    if (response.getJSONObject("list").getJSONArray("data").size() > 0) {
                        List<AttentionBean> list = gson.fromJson(response.getJSONObject("list").getJSONArray("data").toJSONString(), new TypeToken<List<AttentionBean>>() {
                        }.getType());
                        if (null != list) {
                            adapter.addData(list);
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
                    }
                    if (null != refresh_layout) {
                        refresh_layout.finishLoadMore();
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
     * 关注与取消
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

        HttpUtils.post(Constants.UPDATE_FOLLOW_LIST, AttentionActivity.this, params, new HttpJsonResponse() {
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
                LogUtils.d("AttentionActivity requestAttention onSuccess = " + response.toJSONString());
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
     * 更新列表
     *
     * @param position
     */
    void updateAttentionInfo(int position) {
        AttentionBean changedItem = adapter.getData().get(position);
        int is_mutual = changedItem.is_mutual;
        if (is_mutual == 0) {
            changedItem.is_mutual = 1;
        } else {
            changedItem.is_mutual = 0;
        }
        adapter.notifyItemChanged(position);
    }

    private void getTypeAndRequest(String content) {
        tv_order_value.setText(content);
        int len = stringList.size();
        for (int i = 0; i < len; i++) {
            if (content.equals(stringList.get(i))) {
                attentionIndex = i;
                break;
            }
        }
        current_page = 1;
        adapter.getData().clear();
        refresh_layout.setEnableLoadMore(true);
        requestData();
    }

    private void setTextDrawableRight(int resId) {
        Drawable drawableRight = getResources().getDrawable(resId);
        drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(),
                drawableRight.getMinimumHeight());
        tv_order_value.setCompoundDrawables(null, null, drawableRight, null);
    }
}