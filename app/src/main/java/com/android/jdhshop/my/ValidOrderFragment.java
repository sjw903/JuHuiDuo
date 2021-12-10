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
import com.android.jdhshop.adapter.JdOrderNewAdapter;
import com.android.jdhshop.adapter.OrderNewAdapter;
import com.android.jdhshop.adapter.PddOrderNewAdapter;
import com.android.jdhshop.adapter.WphOrderNewAdapter;
import com.android.jdhshop.base.BaseFragment;
import com.android.jdhshop.bean.JdOrderBean;
import com.android.jdhshop.bean.MessageEvent;
import com.android.jdhshop.bean.OrderGuestNewBean;
import com.android.jdhshop.bean.PddOrderBean;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.WphOrderBean;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 已付款
 * Created by yohn on 2018/9/19.
 */

public class ValidOrderFragment extends BaseFragment {
    public static final String TAG = "ValidOrderFragment";

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;
    @BindView(R.id.lv_order)
    ListView lv_order;

    private View view;
    public static ValidOrderFragment fragment;
    private ACache mAcache;
    String token;
    private int indexNum = 1;
    private int pageNum = 6;
    private int status = 0;
    List<OrderGuestNewBean.OrderBean> orderList = new ArrayList<>();
    private OrderNewAdapter orderAdapter;
    private boolean hasdata = true;
    //以下是拼多多
    @BindView(R.id.lv_pdd)
    ListView lv_pdd;
    private List<PddOrderBean> listPdd = new ArrayList<>();
    private PddOrderNewAdapter opddAdapter;
    private int pddindexNum = 1;
    private boolean hasPddMore = true;
    //京东订单
    @BindView(R.id.lv_jd)
    ListView lv_jd;
    private List<JdOrderBean> listjd=new ArrayList<>();
    private JdOrderNewAdapter jdAdapter;
    private int jddindexNum = 1;
    private boolean hasJdMore=true;
    //唯品会
    @BindView(R.id.lv_wph)
    ListView lv_wph;
    private List<WphOrderBean> listWph = new ArrayList<>();
    private WphOrderNewAdapter wphOrderNewAdapter;
    private int WphdindexNum = 1;
    private boolean hasWphMore=true;
    public static ValidOrderFragment getInstance() {
        if (fragment == null) {
            fragment = new ValidOrderFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_audit, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        init();
        addListener();
        return view;
    }
    private void addListener() {
        refresh_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if(MyOrderActivity.CURRENT_TYPE==0){
                    status = 0;
                    if (hasdata) {
                        indexNum++;
                        getData();
                    } else {
                        showToast("没有更多数据了");
                        refreshLayout.finishLoadMore(1000);
                    }
                }else  if(MyOrderActivity.CURRENT_TYPE==1){
                    if (hasPddMore) {
                        pddindexNum++;
                        getPdd();
                    } else {
                        showToast("没有更多数据了");
                        refreshLayout.finishLoadMore(1000);
                    }
                }else  if(MyOrderActivity.CURRENT_TYPE==2){
                    if (hasJdMore) {
                        jddindexNum++;
                        //getJd();
                    } else {
                        showToast("没有更多数据了");
                        refreshLayout.finishLoadMore(1000);
                    }
                }else  if(MyOrderActivity.CURRENT_TYPE==3){
                    if (hasWphMore) {
                        WphdindexNum++;
                        getWph();
                    } else {
                        showToast("没有更多数据了");
                        refreshLayout.finishLoadMore(1000);
                    }
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if(MyOrderActivity.CURRENT_TYPE==0) {
                    status = 1;
                    indexNum = 1;
                    hasdata = true;
                    getData();
                }else if(MyOrderActivity.CURRENT_TYPE==1){
                    pddindexNum = 1;
                    hasPddMore = true;
                    getPdd();
                }else if(MyOrderActivity.CURRENT_TYPE==2){
                    jddindexNum=1;
                    hasJdMore=true;
                    getJd();
                }else if(MyOrderActivity.CURRENT_TYPE==3){
                    WphdindexNum=1;
                    hasWphMore=true;
                    getWph();
                }
            }
        });
    }

    //初始化数据
    private void init() {
        mAcache = ACache.get(getActivity());
        token = mAcache.getAsString(Constants.TOKEN);

        orderAdapter = new OrderNewAdapter(getActivity());
        orderAdapter.setData(orderList);
        lv_order.setAdapter(orderAdapter);
        //拼多多
        opddAdapter=new PddOrderNewAdapter(getActivity());
        opddAdapter.setData(listPdd);
        lv_pdd.setAdapter(opddAdapter);
        //京东
        jdAdapter=new JdOrderNewAdapter(getActivity());
        jdAdapter.setData(listjd);
        lv_jd.setAdapter(jdAdapter);
        wphOrderNewAdapter=new WphOrderNewAdapter(getActivity());
        wphOrderNewAdapter.setData(listWph);
        lv_wph.setAdapter(wphOrderNewAdapter);
        //开始刷新
        refresh_layout.autoRefresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    /**
     * 数据请求
     */
    private void getData() {
        if (!CommonUtils.isNetworkAvailable()) {
            showToast(getResources().getString(R.string.error_network));
            return;
        }

        RequestParams params = new RequestParams();
        params.put("token", SPUtils.getStringData(context,Constants.TOKEN,""));
//        淘客订单状态，3：订单结算，12：订单付款， 13：订单失效，14：订单成功
//        默认空值，获取全部
        params.put("tk_status", "12");
        params.put("p", indexNum);//默认第1页
        params.put("per", pageNum);//每页条数，默认6条
        params.put("type",MyOrderActivity.CURRENT_ORDER_TYPE);
        params.put("trade_id",MyOrderActivity.order_sn);
        HttpUtils.post(Constants.GET_ORDER_LIST_NEW, ValidOrderFragment.this,params, new onOKJsonHttpResponseHandler<OrderGuestNewBean>(new TypeToken<Response<OrderGuestNewBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onSuccess(int statusCode, Response<OrderGuestNewBean> datas) {
                if (datas.isSuccess()) {
                    List<OrderGuestNewBean.OrderBean> list = datas.getData().getList();
                    if (status == 1) {
                        orderList.clear();
                    }
                    orderList.addAll(list);
                    if (list.size() == 0) {
                        hasdata = false;
                    }
                } else {
                    hasdata = false;
                    showToast(datas.getMsg());
                    if("用户不存在".equals(datas.getMsg())){
                        MyOrderActivity.orderActivity.finish();
                        return;
                    }
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

    private void getPdd() {
        if (!CommonUtils.isNetworkAvailable()) {
            showToast(getResources().getString(R.string.error_network));
            return;
        }
        RequestParams params = new RequestParams();
        params.put("order_status", "0");
        params.put("p", pddindexNum);//默认第1页
        params.put("per", pageNum);//每页条数，默认6条
        params.put("type",MyOrderActivity.CURRENT_ORDER_TYPE);
        params.put("order_sn",MyOrderActivity.order_sn);
        HttpUtils.post(Constants.GET_PDD_ORDER, ValidOrderFragment.this,params, new onOKJsonHttpResponseHandler<PddOrderBean>(new TypeToken<Response<PddOrderBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onSuccess(int statusCode, Response<PddOrderBean> datas) {
                if (datas.isSuccess()) {
                    List<PddOrderBean> list = datas.getData().getList();
                    if (pddindexNum == 1) {
                        listPdd.clear();
                    }
                    listPdd.addAll(list);
                    if (list.size() == 0) {
                        hasPddMore = false;
                    }
                } else {
                    hasPddMore = false;
                    showToast(datas.getMsg());
                    if("用户不存在".equals(datas.getMsg())){
                        MyOrderActivity.orderActivity.finish();
                        return;
                    }
                }
                opddAdapter.setData(listPdd);
                if (refresh_layout != null) {
                    if (pddindexNum == 1) {
                        refresh_layout.finishRefresh();
                    } else {
                        refresh_layout.finishLoadMore();
                    }
                }
            }
        });
    }
    private void getWph(){
        if (!CommonUtils.isNetworkAvailable()) {
            showToast(getResources().getString(R.string.error_network));
            return;
        }
        RequestParams params = new RequestParams();
        params.put( "status","1" );//订单状态
        params.put("p", WphdindexNum);//默认第1页
        params.put("per", pageNum);//每页条数，默认6条
        params.put("type",MyOrderActivity.CURRENT_ORDER_TYPE);//订单类型
        params.put("orderSn",MyOrderActivity.order_sn);//订单号
        HttpUtils.post(Constants.APP_IP+"/api/WphOrder/getList",ValidOrderFragment.this, params, new onOKJsonHttpResponseHandler<WphOrderBean>(new TypeToken<Response<WphOrderBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }
            @Override
            public void onSuccess(int statusCode, Response<WphOrderBean> datas) {
                if (datas.isSuccess()) {
                    List<WphOrderBean> list = datas.getData().getList();
                    if (WphdindexNum == 1) {
                        listWph.clear();
                    }
                    listWph.addAll(list);
                    if(list.size()==0){
                        hasWphMore=false;
                    }
                } else {
                    hasWphMore=false;
                    showToast(datas.getMsg());
                    if("用户不存在".equals(datas.getMsg())){
                        MyOrderActivity.orderActivity.finish();
                        return;
                    }
                }
                wphOrderNewAdapter.setData(listWph);
                if (refresh_layout != null) {
                    if (WphdindexNum == 1) {
                        refresh_layout.finishRefresh();
                    } else {
                        refresh_layout.finishLoadMore();
                    }
                }
            }
        });
    }
    private void getJd(){
        if (!CommonUtils.isNetworkAvailable()) {
            showToast(getResources().getString(R.string.error_network));
            return;
        }
        RequestParams params = new RequestParams();
        params.put("order_status","");
        params.put("p", jddindexNum);//默认第1页
        params.put("per", pageNum);//每页条数，默认6条
        params.put("type",MyOrderActivity.CURRENT_ORDER_TYPE);
        params.put("order_sn",MyOrderActivity.order_sn);
        HttpUtils.post(Constants.GETJD_ORDERLIST, ValidOrderFragment.this,params, new onOKJsonHttpResponseHandler<JdOrderBean>(new TypeToken<Response<JdOrderBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onSuccess(int statusCode, Response<JdOrderBean> datas) {
                if (datas.isSuccess()) {
                    List<JdOrderBean> list = datas.getData().getList();
                    if (jddindexNum == 1) {
                        listjd.clear();
                    }
                    listjd.addAll(list);
                    if(list.size()==0){
                        hasJdMore=false;
                    }
                } else {
                    hasJdMore=false;
                    showToast(datas.getMsg());
                    if("用户不存在".equals(datas.getMsg())){
                        MyOrderActivity.orderActivity.finish();
                        return;
                    }
                }
                jdAdapter.setData(listjd);
                if (refresh_layout != null) {
                    if (jddindexNum == 1) {
                        refresh_layout.finishRefresh();
                    } else {
                        refresh_layout.finishLoadMore();
                    }
                }
            }
        });
    }
    @Subscribe
    public void Event(MessageEvent messageEvent) {
        if(messageEvent!=null&&"refresh".equals(messageEvent.getMessage())) {
            if (MyOrderActivity.CURRENT_TYPE == 0) {
                lv_order.setVisibility(View.VISIBLE);
                lv_jd.setVisibility(View.GONE);
                lv_pdd.setVisibility(View.GONE);
                lv_wph.setVisibility(View.GONE);
                if (orderList.size() <= 0) {
                    refresh_layout.autoRefresh();
                }
            } else if(MyOrderActivity.CURRENT_TYPE ==1){
                lv_order.setVisibility(View.GONE);
                lv_jd.setVisibility(View.GONE);
                lv_pdd.setVisibility(View.VISIBLE);
                lv_wph.setVisibility(View.GONE);
                if (listPdd.size() <= 0) {
                    refresh_layout.autoRefresh();
                }
            }else if(MyOrderActivity.CURRENT_TYPE ==2){
                lv_order.setVisibility(View.GONE);
                lv_jd.setVisibility(View.VISIBLE);
                lv_pdd.setVisibility(View.GONE);
                lv_wph.setVisibility(View.GONE);
                if (listjd.size() <= 0) {
                    refresh_layout.autoRefresh();
                }
            }else{
                lv_order.setVisibility(View.GONE);
                lv_jd.setVisibility(View.GONE);
                lv_pdd.setVisibility(View.GONE);
                lv_wph.setVisibility(View.VISIBLE);
                if (listjd.size() <= 0) {
                    refresh_layout.autoRefresh();
                }
            }
        }else if(messageEvent!=null&&"refresh2".equals(messageEvent.getMessage())){
            if (MyOrderActivity.CURRENT_TYPE == 0) {
                lv_order.setVisibility(View.VISIBLE);
                lv_jd.setVisibility(View.GONE);
                lv_pdd.setVisibility(View.GONE);
                lv_wph.setVisibility(View.GONE);
                refresh_layout.autoRefresh();
            } else if (MyOrderActivity.CURRENT_TYPE == 1) {
                lv_order.setVisibility(View.GONE);
                lv_jd.setVisibility(View.GONE);
                lv_pdd.setVisibility(View.VISIBLE);
                lv_wph.setVisibility(View.GONE);
                refresh_layout.autoRefresh();
            }else if(MyOrderActivity.CURRENT_TYPE ==2){
                lv_order.setVisibility(View.GONE);
                lv_jd.setVisibility(View.VISIBLE);
                lv_pdd.setVisibility(View.GONE);
                lv_wph.setVisibility(View.GONE);
                refresh_layout.autoRefresh();
            }else{
                lv_order.setVisibility(View.GONE);
                lv_jd.setVisibility(View.GONE);
                lv_pdd.setVisibility(View.GONE);
                lv_wph.setVisibility(View.VISIBLE);
                refresh_layout.autoRefresh();
            }
        }else if(!messageEvent.getMessage().contains("#")){
            refresh_layout.autoRefresh();
        }
    }
}
