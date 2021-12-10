package com.android.jdhshop.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.ShopGridAdapter;
import com.android.jdhshop.adapter.ZeroBuyAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.BannerBean;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.ShopTabsBean;
import com.android.jdhshop.bean.ShopTabsChildBean;
import com.android.jdhshop.bean.SubListByParentChildBean;
import com.android.jdhshop.bean.ZeroBuyBean;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.utils.CornerTransform;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class ZeroBuyActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    private int page = 1;
    private ZeroBuyAdapter buyAdapter;
    private List<ZeroBuyBean.Item> list = new ArrayList<>();

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_zero);
        ButterKnife.bind(this);
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("0元购");

    }

    @Override
    protected void initData() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        buyAdapter = new ZeroBuyAdapter(R.layout.zero_item, list);
        recyclerView.setLayoutManager(manager);
        buyAdapter.addHeaderView(LayoutInflater.from(this).inflate(R.layout.head_zero, null));
        recyclerView.setAdapter(buyAdapter);
        getList();
        buyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("num_iid", list.get(position).goods_id);
                openActivity(PromotionDetailsActivity.class, bundle);

            }
        });

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
        requestParams.put("p", page);
        requestParams.put("per", "6");
        HttpUtils.post(Constants.GETFREEGOODS, ZeroBuyActivity.this, requestParams, new onOKJsonHttpResponseHandler<ZeroBuyBean>(new TypeToken<Response<ZeroBuyBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(responseString);
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
            }

            @Override
            public void onSuccess(int statusCode, Response<ZeroBuyBean> datas) {
                if (datas.isSuccess()) {
                    if (page == 1) {
                        list.clear();
                    }
                    list.addAll(datas.getData().list);
                    buyAdapter.notifyDataSetChanged();
                } else {
                    showToast(datas.getMsg());
                }
            }
        });
    }

    @OnClick(R.id.tv_left)
    public void onViewClicked() {
        finish();
    }
}
