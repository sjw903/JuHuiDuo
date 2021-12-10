package com.android.jdhshop.mall;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.malladapter.MyOrderAdapter;
import com.android.jdhshop.mallbean.OrderDetailBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;

public class MyOrderFragment extends BaseLazyFragment implements OnRefreshLoadMoreListener {
    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private String status="0";
    private int page=1;
    private List<OrderDetailBean.OrderMsg> orderBeanList=new ArrayList<>();
    private MyOrderAdapter myOrderAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        view.findViewById(R.id.right_icon).setVisibility(View.GONE);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }
    private void init(){
        status=getArguments().getString("pid");
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.addItemDecoration(new RecycleViewDivider(context,DividerItemDecoration.VERTICAL,10,context.getResources().getColor(R.color.col_eb)));
        myOrderAdapter=new MyOrderAdapter(context, R.layout.item_order_2,orderBeanList);
        recyclerView.setAdapter(myOrderAdapter);
        refreshLayout.setOnRefreshLoadMoreListener(this);
    }
    @Override
    protected void lazyload() {
        refreshLayout.autoRefresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
    private void getOrderList(){
        RequestParams requestParams = new RequestParams();
        requestParams.put("status",status);
        requestParams.put("p",page);
        requestParams.put("per",6);
        HttpUtils.post("1".equals(Constants.MALL_ORDER_TYPE)?Constants.GET_USER_ORDER_LIST_UPDATE:Constants.GET_USER_ORDER_LIST, MyOrderFragment.this,requestParams, new onOKJsonHttpResponseHandler<OrderDetailBean>(new TypeToken<Response<OrderDetailBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Response<OrderDetailBean> datas) {
                if(datas.isSuccess()){
                    if(page==1){
                        orderBeanList.clear();
                    }
                    orderBeanList.addAll(datas.getData().list);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshLayout.finishRefresh();
                            refreshLayout.finishLoadMore();
                            myOrderAdapter.notifyDataSetChanged();
                        }
                    });
                }else{
                    showToast(datas.getMsg());
                }
            }
        });
    }
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getOrderList();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page=1;
        getOrderList();
    }
}
