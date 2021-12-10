package com.android.jdhshop.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.PromotionDetailsActivity;
import com.android.jdhshop.adapter.ShopGridAdapter;
import com.android.jdhshop.adapter.ShopRecyclerAdapterHd;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.bean.HaoDanBean;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.SubListByParentBean;
import com.android.jdhshop.bean.SubListByParentChildBean;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.utils.DrawableCenterTextView;
import com.android.jdhshop.utils.UIUtils;
import com.android.jdhshop.widget.NoScrollGridView;
import com.android.jdhshop.widget.indicator.buildins.UIUtil;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
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
public class ShopFragment extends BaseLazyFragment {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;


    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.right_icon)
    ImageView rightIcon;
    private ShopRecyclerAdapterHd shopRecyclerAdapter;
    private NoScrollGridView gridView;
    private String pid;

    List<SubListByParentChildBean> subListByParentChildBeans = new ArrayList<>();
    List<HaoDanBean> taobaoGuesChildtBeans = new ArrayList<>();
    private ShopGridAdapter shopGridAdapter;

    private int indexNum = 1;
    private String name;
    private int status = 0;
    private HeaderAndFooterWrapper headerAndFooterWrapper;
    private String sort = "0";
    private LinearLayoutManager linearLayoutManager;
    private TextView[] textViews;
    private TextView currentSearchView;
    private boolean hasdata = true;
    @BindView(R.id.jiage_st)
    DrawableCenterTextView jiageSt;
    @BindView(R.id.xiaoliang_st)
    DrawableCenterTextView xiaoliangSt;
    @BindView(R.id.yongjin_st)
    DrawableCenterTextView yongjinSt;
    @BindView(R.id.tuiguang_st)
    DrawableCenterTextView tuiguangSt;
    DecimalFormat df = new DecimalFormat("0.00");
    private Gson gson = new Gson();
    private String min_id = "1",tb_p="1";

    ACache aCache;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        addListener();
        BaseLogDZiYuan.LogDingZiYuan(rightIcon, "home_btn.png");

        return view;
    }

    private void init() {
        aCache = ACache.get(this.getContext());

        Bundle arguments = getArguments();
        if (arguments != null) {
            pid = arguments.getString("pid");
            name = arguments.getString("name");
            sort = arguments.getString("sort");
        }
        //头部是GridView视图
        gridView = new NoScrollGridView(getActivity());
        gridView.setNumColumns(4);
        gridView.setGravity(Gravity.CENTER);

        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, UIUtil.dip2px(getActivity(), 10), 0, UIUtil.dip2px(getActivity(), 10));
        layoutParams.width = RecyclerView.LayoutParams.MATCH_PARENT;
        layoutParams.height = RecyclerView.LayoutParams.WRAP_CONTENT;
        gridView.setLayoutParams(layoutParams);
        yongjinSt.setText("佣金比例");
        textViews = new TextView[]{jiageSt, xiaoliangSt, yongjinSt, tuiguangSt};
        shopGridAdapter = new ShopGridAdapter(getActivity(), R.layout.service_home_grid_item, subListByParentChildBeans);
        gridView.setAdapter(shopGridAdapter);
        shopRecyclerAdapter = new ShopRecyclerAdapterHd(getActivity(), R.layout.today_highlights_child_item, taobaoGuesChildtBeans);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        headerAndFooterWrapper = new HeaderAndFooterWrapper(shopRecyclerAdapter);
        headerAndFooterWrapper.addHeaderView(gridView);
        recyclerView.setAdapter(headerAndFooterWrapper);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //屏幕高度
                int screenMeasuredHeight = UIUtils.getScreenMeasuredHeight(getActivity());
                //如果滚动距离大于屏幕高度，那么显示回滚，否则。。。
                if (getScollYDistance() >= (screenMeasuredHeight / 2)) {
                    rightIcon.setVisibility(View.VISIBLE);
                } else {
                    rightIcon.setVisibility(View.GONE);
                }
                LogUtils.d("TAG", "高度为:" + getScollYDistance());

            }
        });
    }

    private void addListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                LogUtils.d("TAG", "onRefresh: ");
                status = 1;
                hasdata = true;
                min_id = "1";
                taobaoGuesChildtBeans.clear();
                shopRecyclerAdapter.notifyDataSetChanged();
                getTbkListRequst(name);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                LogUtils.d("TAG", "onLoadMore: ");
                status = 0;
                if (hasdata) {
                    indexNum++;
                    getTbkListRequst(name);
                } else {
                    showToast("没有更多数据了");
                    refreshLayout.finishLoadMore(2000);
                }
            }
        });

        //点击分类
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SubListByParentChildBean subListByParentChildBean = subListByParentChildBeans.get(i);
                if (subListByParentChildBean.getName().equals(name)) {
                    return;
                }
                name = subListByParentChildBean.getName();
                if (shopGridAdapter.getTextView() != null) {
                    shopGridAdapter.getTextView().setTextColor(getResources().getColor(R.color.black));
                    shopGridAdapter.setTextView(null);
                }
                if (currentSearchView != null)
                    currentSearchView.setTextColor(getResources().getColor(R.color.black));
                ((TextView) view.findViewById(R.id.service_name)).setTextColor(getResources().getColor(R.color.red1));
                currentSearchView = view.findViewById(R.id.service_name);
                refreshLayout.autoRefresh();
            }
        });
        //点击进入详情
        shopRecyclerAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                HaoDanBean taobaoGuesChildtBean = taobaoGuesChildtBeans.get(position - 1);
                if (taobaoGuesChildtBean != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("num_iid", taobaoGuesChildtBean.itemid);
                    bundle.putSerializable("bean",taobaoGuesChildtBean);
                    openActivity(PromotionDetailsActivity.class, bundle);
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
    public long getScollYDistance() {
        int position = linearLayoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = linearLayoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected void lazyload() { //懒加载，界面开始后刷新
        getSubListByParentRequst();
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
        gets();
    }

    private void gets() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("pid", pid);
        HttpUtils.post(Constants.HOME_TAOBAOCAT_GETSUBLISTBYPARENT_URL,ShopFragment.this, requestParams, new onOKJsonHttpResponseHandler<SubListByParentBean>(new TypeToken<Response<SubListByParentBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Response<SubListByParentBean> datas) {
                if (datas.isSuccess()) {
                    List<SubListByParentChildBean> list = datas.getData().getList();
//                    for(int i=0;i<list.size();i++){
//                        CaiNiaoApplication.getInstances().getDaoSession().getSubListByParentChildBeanDao().save(list.get(i));
//                    }
                    subListByParentChildBeans.clear();
                    subListByParentChildBeans.addAll(list);
                } else {
                    showToast(datas.getMsg());
                }
                shopGridAdapter.notifyDataSetChanged();
                status = 1;
                min_id="1";
                hasdata = true;
                indexNum = 1;
                getTbkListRequst(name);
            }
        });
    }

    /**
     * @属性:获取淘宝客商品列表
     * @开发者:陈飞
     * @时间:2018/7/26 17:05
     */
    private void getTbkListRequst(String search) {

        LogUtils.d("TAG", "getTbkListRequst: "+search);

        if (aCache.getAsString(search+"_last_tb_p")!=null) {
            tb_p = aCache.getAsString(search + "_last_tb_p");
        }
        if (aCache.getAsString(search+"_last_min_id")!=null) {
            min_id = aCache.getAsString(search + "_last_min_id");
        }


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
        requestParams.put("keyword", search);
        requestParams.put("back", "10");
        requestParams.put("is_coupon","1");
        requestParams.put("min_id", min_id);
        requestParams.put("tb_p",tb_p);
        if (!TextUtils.isEmpty(sort)) {
            requestParams.put("sort", sort);
        }
        HttpUtils.post(Constants.HOME_TBK_GETTBKLIST_NEW_URL_HD,ShopFragment.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("dsfasd", responseString);
                try {
                    JSONObject object = new JSONObject(responseString);
                    if ("1".equals(object.getString("code"))) {
                        JSONArray array = object.getJSONArray("data");
                        if (min_id.equals("1")) {
                            taobaoGuesChildtBeans.clear();
                        }
                        tb_p = object.getString("tb_p");
                        min_id = object.getString("min_id");

                        aCache.put(search+"_last_tb_p",tb_p,Constants.CacheSaveTime);
                        aCache.put(search+"_last_min_id",min_id,Constants.CacheSaveTime);

                        if (array.length() <= 0) {
                            hasdata = false;
                            return;
                        }
                        for (int i = 0; i < array.length(); i++) {
                            taobaoGuesChildtBeans.add(new Gson().fromJson(array.getJSONObject(i).toString(), HaoDanBean.class));
                        }
                        headerAndFooterWrapper.notifyDataSetChanged();
                        shopRecyclerAdapter.notifyDataSetChanged();
                    } else {
                        if (!min_id.equals("1") || !tb_p.equals("1")){
                            aCache.put(search+"_last_tb_p","1",Constants.CacheSaveTime);
                            aCache.put(search+"_last_min_id","1",Constants.CacheSaveTime);
                            getTbkListRequst(search);
                        }
                        else {
                            showToast(object.getString("msg"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadMore();
                }
            }
        });
    }

    private void selectView(int position) {
        for (TextView textView : textViews) {
            textView.setTextColor(getResources().getColor(R.color.black));
        }
        textViews[position - 1].setTextColor(getResources().getColor(R.color.red1));
    }

    @OnClick({R.id.right_icon, R.id.jiage_st, R.id.xiaoliang_st, R.id.yongjin_st, R.id.tuiguang_st})
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
            case R.id.jiage_st:
                if ("1".equals(sort)) {
                    sort = "2";
                    jiageSt.setText("价格(降)");
                } else if ("2".equals(sort)) {
                    sort = "1";
                    jiageSt.setText("价格(升)");
                } else {
                    sort = "1";
                    jiageSt.setText("价格(升)");
                }
                selectView(1);
                min_id = "1";
                refreshLayout.autoRefresh();
                break;
            case R.id.xiaoliang_st:
                if ("4".equals(sort)) {
                    sort = "7";
                    xiaoliangSt.setText("销量(升)");
                } else if ("7".equals(sort)) {
                    sort = "4";
                    xiaoliangSt.setText("销量(降)");
                } else {
                    sort = "4";
                    xiaoliangSt.setText("销量(降)");
                }
                selectView(2);
                min_id = "1";
                refreshLayout.autoRefresh();
                break;
            case R.id.yongjin_st:
                if ("5".equals(sort)) {
                    yongjinSt.setText("佣金比例(升)");
                    sort = "8";
                } else if ("8".equals(sort)) {
                    sort = "5";
                    yongjinSt.setText("佣金比例(降)");
                } else {
                    sort = "5";
                    yongjinSt.setText("佣金比例(降)");
                }
                selectView(3);
                min_id = "1";
                refreshLayout.autoRefresh();
                break;
            case R.id.tuiguang_st:
                sort = "13";
                tuiguangSt.setText("推广量(降)");
                selectView(4);
                min_id = "1";
                refreshLayout.autoRefresh();
                break;
        }
    }
}