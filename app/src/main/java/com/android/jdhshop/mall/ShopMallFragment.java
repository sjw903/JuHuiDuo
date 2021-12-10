package com.android.jdhshop.mall;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jdhshop.base.BaseLogDZiYuan;
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
import com.android.jdhshop.malladapter.ShopMallGoodsRecyclerAdapter;
import com.android.jdhshop.malladapter.ShopMallGridAdapter;
import com.android.jdhshop.mallbean.MallCatbean;
import com.android.jdhshop.mallbean.ShopMallGoodsBean;
import com.android.jdhshop.widget.NoScrollGridView;
import com.android.jdhshop.widget.indicator.buildins.UIUtil;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;

/**
 * @属性:商品页
 * @开发者:陈飞
 * @时间:2018/7/26 15:18
 */
public class ShopMallFragment extends BaseLazyFragment {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;


    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.right_icon)
    ImageView rightIcon;
    private ShopMallGoodsRecyclerAdapter shopRecyclerAdapter;
    private NoScrollGridView gridView;
    private String pid;

    List<MallCatbean> subListByParentChildBeans = new ArrayList<>();
    List<ShopMallGoodsBean> taobaoGuesChildtBeans = new ArrayList<>();
    private ShopMallGridAdapter shopGridAdapter;

    private int indexNum = 1;
    private String name;
    private int status = 0;
    private HeaderAndFooterWrapper headerAndFooterWrapper;
    private TextView currentSearchView;
    private boolean hasdata = true;
    private String cat_id = "12";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        addListener();
        BaseLogDZiYuan.LogDingZiYuan(rightIcon, "home_btn.png");
        return view;
    }

    private void init() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            pid = arguments.getString("pid");
            name = arguments.getString("name");
        }
        gridView = new NoScrollGridView(getActivity());
        gridView.setNumColumns(4);
        gridView.setGravity(Gravity.CENTER);
        gridView.setBackgroundColor(context.getResources().getColor(R.color.white));
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, UIUtil.dip2px(getActivity(), 5));
        layoutParams.width = RecyclerView.LayoutParams.MATCH_PARENT;
        layoutParams.height = RecyclerView.LayoutParams.WRAP_CONTENT;
        gridView.setLayoutParams(layoutParams);
        shopGridAdapter = new ShopMallGridAdapter(getActivity(), R.layout.service_home_grid_item, subListByParentChildBeans);
        gridView.setAdapter(shopGridAdapter);
        shopRecyclerAdapter = new ShopMallGoodsRecyclerAdapter(getActivity(), R.layout.shop_mall_goods_item, taobaoGuesChildtBeans);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        headerAndFooterWrapper = new HeaderAndFooterWrapper(shopRecyclerAdapter);
        headerAndFooterWrapper.addHeaderView(gridView);
        recyclerView.setAdapter(headerAndFooterWrapper);

        //显示回滚
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                //屏幕高度
//                int screenMeasuredHeight = UIUtils.getScreenMeasuredHeight(getActivity());
//                //如果滚动距离大于屏幕高度，那么显示回滚，否则。。。
//                if (getScollYDistance() >= (screenMeasuredHeight / 2)) {
//                    rightIcon.setVisibility(View.VISIBLE);
//                } else {
//                    rightIcon.setVisibility(View.GONE);
//                }
//            }
//        });
    }

    private void addListener() {
        getSubListByParentRequst();
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                status = 0;
                if (hasdata) {
                    indexNum++;
                    getTbkListRequst(name);
                } else {
                    showToast("没有更多数据了");
                    refreshLayout.finishLoadMore(2000);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                status = 1;
                hasdata = true;
                indexNum = 1;
                getTbkListRequst(name);
            }
        });
        //点击分类
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MallCatbean subListByParentChildBean = subListByParentChildBeans.get(i);
                if (subListByParentChildBean.cat_name.equals(name)) {
                    return;
                }
                name = subListByParentChildBean.cat_name;
                if (shopGridAdapter.getTextView() != null) {
                    shopGridAdapter.getTextView().setTextColor(getResources().getColor(R.color.black));
                    shopGridAdapter.setTextView(null);
                }
                if (currentSearchView != null)
                    currentSearchView.setTextColor(getResources().getColor(R.color.black));
                ((TextView) view.findViewById(R.id.service_name)).setTextColor(getResources().getColor(R.color.red1));
                currentSearchView = view.findViewById(R.id.service_name);
                cat_id = subListByParentChildBean.cat_id;
                refreshLayout.autoRefresh();
            }
        });
        //点击进入详情
        shopRecyclerAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                ShopMallGoodsBean taobaoGuesChildtBean = taobaoGuesChildtBeans.get(position - 1);
                if (taobaoGuesChildtBean != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("goods_id", taobaoGuesChildtBean.goods_id);
                    openActivity(MallGoodsDetailsActivity.class, bundle);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * @属性:获取屏幕高度的方法
     * @开发者:陈飞
     * @时间:2018/8/7 14:22
     */
//    public long getScollYDistance() {
//        int position = linearLayoutManager.findFirstVisibleItemPosition();
//        View firstVisiableChildView = linearLayoutManager.findViewByPosition(position);
//        if(firstVisiableChildView==null){
//            return 0;
//        }
//        int itemHeight = firstVisiableChildView.getHeight();
//        return (position) * itemHeight - firstVisiableChildView.getTop();
//    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected void lazyload() { //懒加载，界面开始后刷新
        //开始刷新
        refreshLayout.autoRefresh();
    }


    /**
     * @属性:获取子级淘宝商品分类列表
     * @开发者:陈飞
     * @时间:2018/7/26 16:18
     */
    private void getSubListByParentRequst() {
        if (TextUtils.isEmpty(pid)) {
            showToast("没有获取到父类pid");
            return;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("pid", pid);
        HttpUtils.post(Constants.SLEF_MALL_CAT_SUB, ShopMallFragment.this,requestParams, new onOKJsonHttpResponseHandler<MallCatbean>(new TypeToken<Response<MallCatbean>>() {
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
            public void onSuccess(int statusCode, Response<MallCatbean> datas) {
                if (datas.isSuccess()) {
                    List<MallCatbean> list = datas.getData().list;
                    subListByParentChildBeans.clear();
                    subListByParentChildBeans.addAll(list);
                } else {
                    showToast(datas.getMsg());
                }
                shopGridAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * @属性:获取淘宝客商品列表
     * @开发者:陈飞
     * @时间:2018/7/26 17:05
     */
    private void getTbkListRequst(String search) {
        if (TextUtils.isEmpty(name)) {
            if (refreshLayout != null) {
                if (status == 1) {
                    refreshLayout.finishRefresh();
                } else {
                    refreshLayout.finishLoadMore();
                }
            }
            showToast("未传查询词");
            return;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("p", indexNum);
        requestParams.put("cat_id", "".equals(cat_id) ? pid : cat_id);
        requestParams.put("per", 10);
        HttpUtils.post(Constants.SLEF_MALL_GOODS,ShopMallFragment.this, requestParams, new onOKJsonHttpResponseHandler<ShopMallGoodsBean>(new TypeToken<Response<ShopMallGoodsBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onSuccess(int statusCode, Response<ShopMallGoodsBean> datas) {
                if (datas.isSuccess()) {
                    List<ShopMallGoodsBean> list = datas.getData().list;
                    if (status == 1) {
                        taobaoGuesChildtBeans.clear();
                    }
                    taobaoGuesChildtBeans.addAll(list);
                    if (list.size() <= 0) {
                        hasdata = false;
                    }
                } else {
                    showToast(datas.getMsg());
                }
                headerAndFooterWrapper.notifyDataSetChanged();
                if (refreshLayout != null) {
                    if (status == 1) {
                        refreshLayout.finishRefresh();
                    } else {
                        refreshLayout.finishLoadMore();
                    }
                }
            }
        });
    }

    @OnClick({R.id.right_icon})
    public void onViewClicked(View view) { //回到头部
        switch (view.getId()) {
            case R.id.right_icon:
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.smoothScrollToPosition(0);
                    }
                });
                break;

        }

    }
}
