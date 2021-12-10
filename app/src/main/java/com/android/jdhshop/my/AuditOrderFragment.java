package com.android.jdhshop.my;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.OrderAdapter;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.bean.OrderGuestBean;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 待审核的订单
 * Created by yohn on 2018/7/28.
 */

public class AuditOrderFragment extends BaseLazyFragment {
    public static final String TAG = "AuditOrderFragment";
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;
    @BindView(R.id.lv_order)
    ListView lv_order;

    private View view;
    public static AuditOrderFragment fragment;

    private ACache mAcache;
    String token;
    private int indexNum = 1;
    private int pageNum = 6;
    private int status = 0;
    List<OrderGuestBean.OrderBean> orderList = new ArrayList<>();
    private OrderAdapter orderAdapter;

    public static AuditOrderFragment getInstance() {
        if (fragment == null) {
            fragment = new AuditOrderFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_audit, container, false);
        ButterKnife.bind(this, view);

        init();
        addListener();
        return view;
    }

    //初始化数据
    private void init() {
        mAcache = ACache.get(getActivity());
        token = mAcache.getAsString(Constants.TOKEN);
        orderAdapter=new OrderAdapter(getActivity());
        orderAdapter.setData(orderList);
        lv_order.setAdapter(orderAdapter);

        //开始刷新
        refresh_layout.autoRefresh();
        getData();
    }

    private void addListener() {
        refresh_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                status = 0;
                if (orderList.size() == 10) {
                    indexNum++;
                    getData();
                } else {
                    showToast("没有更多数据了");
                    refreshLayout.finishLoadMore(2000);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                status = 1;
                getData();
            }
        });
    }

    /**
     *
     */
    private void getData() {
        if (!CommonUtils.isNetworkAvailable()) {
            showToast(getResources().getString(R.string.error_network));
            return;
        }

        RequestParams params = new RequestParams();
        params.put("token", token);
        params.put("status", '1');//状态 1待审核 2审核不通过 3已返利
        params.put("p", indexNum);
        params.put("per", pageNum);
        HttpUtils.post(Constants.GET_ORDER_LIST, AuditOrderFragment.this,params, new onOKJsonHttpResponseHandler<OrderGuestBean>(new TypeToken<Response<OrderGuestBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onSuccess(int statusCode, Response<OrderGuestBean> datas) {
                if (datas.isSuccess()) {
                    List<OrderGuestBean.OrderBean> list = datas.getData().getList();
                    if (status == 1) {
                        orderList.clear();
                    }
                    orderList.addAll(list);

                } else {
                    showToast(datas.getMsg());
                }
                orderAdapter.setData(orderList);
                if (refresh_layout != null) {
                    if (status == 1) {
                        refresh_layout.finishRefresh();
                    } else {
                        refresh_layout.finishLoadMore();
                    }
                }
            }
        });



    }

    @Override
    protected void lazyload() {

    }
}
