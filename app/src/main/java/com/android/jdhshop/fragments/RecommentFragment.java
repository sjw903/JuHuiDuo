package com.android.jdhshop.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.common.LogUtils;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.PromotionDetailsActivity;
import com.android.jdhshop.adapter.PHBAdapterList;
import com.android.jdhshop.adapter.ShopRecyclerAdapter;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.bean.PhbBean;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.utils.UIUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

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
 * @开发者:wmm
 * @时间:2018/11/16 15:18
 */
public class RecommentFragment extends BaseLazyFragment {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;

    DecimalFormat df=new DecimalFormat("0.00");
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.right_icon)
    ImageView rightIcon;
    private String pid;
    private int indexNum = 1;
    private String name;
    private int status = 0;
    private String sort;
    private LinearLayoutManager linearLayoutManager;
    private PHBAdapterList phbAdapter;
    private List<PhbBean> list = new ArrayList<>();
    private String group_id;
    private ACache mAcache;
    private boolean hasdata=true;
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
        mAcache = ACache.get(getContext());
        group_id = mAcache.getAsString("group_id");
        Bundle arguments = getArguments();
        if (arguments != null) {
            pid = arguments.getString("pid");
            name = arguments.getString("name");
            sort = arguments.getString("sort");
        }
        //适配器
        phbAdapter = new PHBAdapterList(getActivity(), R.layout.today_highlights_child_item, list);
        //管理器
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration( new DividerItemDecoration( context, DividerItemDecoration.VERTICAL ) );
        recyclerView.setAdapter(phbAdapter);
        //添加分割线
        //显示回滚
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
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                status = 0;
                if (hasdata) {
                    indexNum++;
                    getTbkListRequst();
                } else {
                    showToast("没有更多数据了");
                    refreshLayout.finishLoadMore(2000);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                indexNum = 1;
                hasdata=true;
                getTbkListRequst();
            }
        });
        //点击进入详情
        phbAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                PhbBean bean = list.get(position);
                if (bean != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("num_iid", bean.getItem_id());
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
        //开始刷新
        refreshLayout.autoRefresh();
    }

    /**
     * @属性:获取淘宝客商品列表
     * @开发者:陈飞
     * @时间:2018/7/26 17:05
     */
    private void getTbkListRequst() {
        RequestParams requestParams = new RequestParams();
        String url="";
        if(sort.equals("综合")){
            url=Constants.GET_TUIJIAN + "&page=" + indexNum;
        }else{
            url=Constants.GET_TUIJIAN + "&subcate=" + sort+ "&page=" + indexNum;
        }
        HttpUtils.post(url,RecommentFragment.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    JSONArray array = null;
                    array = jsonObject.getJSONArray("data");
                    PhbBean bean;
                    JSONObject object;
                    if(array.length()<=0){
                        hasdata=false;
                    }
                    if (indexNum == 1) {
                        list.clear();
                    }
                    for (int i = 0; i < array.length(); i++) {
                        bean = new PhbBean();
                        object = array.getJSONObject(i);
                        bean.setCommission_rate(object.getString("commission_rate"));
                        bean.setTitle(object.getString("title"));
                        bean.setSeller_id(object.getString("seller_id"));
                        bean.setNick("");
                        bean.setItem_id(object.getString("item_id"));
                        bean.setCoupon_amount(object.getString("coupon_amount"));
                        bean.setCoupon_end_time(object.getString("coupon_end_time"));
                        bean.setCoupon_start_time(object.getString("coupon_start_time"));
                        bean.setCoupon_remain_count(object.getString("coupon_total_count"));
                        bean.setCoupon_total_count(object.getString("coupon_total_count"));
                        bean.setCoupon_amount(object.getString("coupon_amount"));
                        bean.setPict_url(object.getString("pict_url"));
                        bean.setZk_final_price(object.getString("zk_final_price"));
                        bean.setVolume(object.getString("volume"));
                        bean.setUser_type(object.getString("user_type"));
                        double money=(Double.parseDouble(bean.getZk_final_price())-Double.parseDouble(bean.getCoupon_amount()))*Double.parseDouble(df.format((Double.parseDouble(bean.getCommission_rate())/100)));
                        money=money*Double.parseDouble(df.format((float)SPUtils.getIntData(getContext(),"rate",0)/100));
                        bean.setCommission(Double.parseDouble(df.format(money)));
                        list.add(bean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                phbAdapter.notifyDataSetChanged();
                if (refreshLayout != null) {
                    if (indexNum == 1) {
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
