package com.android.jdhshop.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.ShopRecyclerAdapter;
import com.android.jdhshop.adapter.TqgAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.TaobaoGuestBean;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class TqgActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private int page = 1;
    List<TaobaoGuestBean.TaobaoGuesChildtBean> taobaoGuesChildtBeans = new ArrayList<>();
    private TqgAdapter shopRecyclerAdapter;

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_tqg);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("淘抢购");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        shopRecyclerAdapter = new TqgAdapter(this, R.layout.tqg_item, taobaoGuesChildtBeans);
        recyclerView.setAdapter(shopRecyclerAdapter);
        refreshLayout.autoRefresh();
    }

    @Override
    protected void initListener() {
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getList();
            }
        });
    }

    private void getList() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("page_no", page);
        requestParams.put("page_size", 10);
        HttpUtils.post(Constants.GET_TQG, TqgActivity.this,requestParams, new onOKJsonHttpResponseHandler<TaobaoGuestBean>(new TypeToken<Response<TaobaoGuestBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onSuccess(int statusCode, Response<TaobaoGuestBean> datas) {
                if (datas.isSuccess()) {
                    if (datas.getData().getList() == null) {
                        return;
                    }
                    if (page == 1) {
                        taobaoGuesChildtBeans.clear();
                    }
                    taobaoGuesChildtBeans.addAll(datas.getData().getList());
                    if (refreshLayout != null) {
                        if (page == 1) {
                            refreshLayout.finishRefresh();
                        } else {
                            refreshLayout.finishLoadMore();
                        }
                    }
                    if (datas.getData().getList().size() <= 0 && page > 1) {
                        ToastUtils.showShortToast(TqgActivity.this, "没有更多数据");
                        page--;
                    }
                } else {
                    showToast(datas.getMsg());
                }

                shopRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.tv_left)
    public void onViewClicked() {
        finish();
    }
}
