package com.android.jdhshop.userupdate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.mall.MallGoodsDetailsActivity;
import com.android.jdhshop.malladapter.ShopMallGoodsRecyclerAdapter;
import com.android.jdhshop.mallbean.ShopMallGoodsBean;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class UpdateGroupActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    List<ShopMallGoodsBean> taobaoGuesChildtBeans = new ArrayList<>();
    private ShopMallGoodsRecyclerAdapter shopRecyclerAdapter;
    int page=1;
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_update_group);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        tvTitle.setText("购买商品");
        tvLeft.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        shopRecyclerAdapter = new ShopMallGoodsRecyclerAdapter(this, R.layout.shop_mall_goods_item, taobaoGuesChildtBeans);
        recyclerView.setAdapter(shopRecyclerAdapter);
        //点击进入详情
        shopRecyclerAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                ShopMallGoodsBean taobaoGuesChildtBean = taobaoGuesChildtBeans.get(position);
                if (taobaoGuesChildtBean != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("goods_id", taobaoGuesChildtBean.goods_id);
                    bundle.putString("isVip","1");
                    openActivity(MallGoodsDetailsActivity.class, bundle);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page=1;
                getList();
            }
        });
        getList();
    }
    private void getList(){
        RequestParams requestParams = new RequestParams();
        requestParams.put("p", page);
        requestParams.put("group_id", "3");
        requestParams.put("per", 10);
        HttpUtils.post(Constants.APP_IP+"/api/UserGoods/getList",UpdateGroupActivity.this, requestParams, new onOKJsonHttpResponseHandler<ShopMallGoodsBean>(new TypeToken<Response<ShopMallGoodsBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onSuccess(int statusCode, Response<ShopMallGoodsBean> datas) {
                if (datas.isSuccess()) {
                    List<ShopMallGoodsBean> list = datas.getData().list;
                    if(page==1)
                    taobaoGuesChildtBeans.clear();
                    taobaoGuesChildtBeans.addAll(list);
                } else {
                    showToast(datas.getMsg());
                }
                shopRecyclerAdapter.notifyDataSetChanged();
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
            }
        });
    }
    @Override
    protected void initListener() {

    }
    @OnClick(R.id.tv_left)
    public void onViewClicked() {
        finish();
    }
}
