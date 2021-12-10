package com.android.jdhshop.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.common.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.ShopRecyclerAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.TaobaoGuestBean;
import com.android.jdhshop.bean.TodayHighlightsBean;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
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
 * Created by yohn on 2018/8/30.
 */

public class ShopNewActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    DecimalFormat df = new DecimalFormat("0.00");


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;

    @BindView(R.id.right_icon)
    ImageView rightIcon;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private int indexNum = 1;
    private int status = 0;

    List<TaobaoGuestBean.TaobaoGuesChildtBean> taobaoGuesChildtBeans = new ArrayList<>();
    private ShopRecyclerAdapter shopRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Gson gson = new Gson();
    private String title,sort;
    private boolean hasdata=true;
    @Override
    protected void initUI() {
        setContentView( R.layout.activity_shop_new );
        unbinder = ButterKnife.bind( this );
        BaseLogDZiYuan.LogDingZiYuan(rightIcon,"home_btn.png");
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if(null!=bundle){
            if(bundle.containsKey( "title" )){
                title=bundle.getString( "title" );
            }
            if(bundle.containsKey( "sort" )){
                sort=bundle.getString( "sort" );
            }
        }

        tvTitle.setText( title );
        tvLeft.setVisibility( View.VISIBLE );

        //管理器
        linearLayoutManager = new LinearLayoutManager( this );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        recyclerView.setLayoutManager( linearLayoutManager );
        //添加分割线
        recyclerView.addItemDecoration( new DividerItemDecoration( this, DividerItemDecoration.VERTICAL ) );

        //适配器
        shopRecyclerAdapter = new ShopRecyclerAdapter( this, R.layout.today_highlights_child_item, taobaoGuesChildtBeans );
        recyclerView.setAdapter( shopRecyclerAdapter );

        //显示回滚
        recyclerView.addOnScrollListener( new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled( recyclerView, dx, dy );
                //屏幕高度
                int screenMeasuredHeight = UIUtils.getScreenMeasuredHeight( ShopNewActivity.this );
                //如果滚动距离大于屏幕高度，那么显示回滚，否则。。。
                if (getScollYDistance() >= (screenMeasuredHeight / 2)) {
                    rightIcon.setVisibility( View.VISIBLE );
                } else {
                    rightIcon.setVisibility( View.GONE );
                }
                LogUtils.d( "TAG", "高度为:" + getScollYDistance() );

            }
        } );

        //开始刷新
        refreshLayout.autoRefresh();
    }

    @Override
    protected void initListener() {
        refreshLayout.setOnRefreshLoadMoreListener( new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                status = 0;
                if (hasdata) {
                    indexNum++;
                    getTabkListNew();
                } else {
                    showToast( "没有更多数据了" );
                    refreshLayout.finishLoadMore( 2000 );
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                status = 1;
                hasdata=true;
                getTabkListNew();
            }
        } );

        //点击进入详情
        shopRecyclerAdapter.setOnItemClickListener( new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                TaobaoGuestBean.TaobaoGuesChildtBean taobaoGuesChildtBean = taobaoGuesChildtBeans.get( position );
                if (taobaoGuesChildtBean != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString( "num_iid", taobaoGuesChildtBean.getNum_iid() );
                    openActivity( PromotionDetailsActivity.class, bundle );
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        } );

        tvLeft.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );
    }

    @OnClick({R.id.right_icon})
    public void onViewClicked(View view) { //回到头部
        switch (view.getId()) {
            case R.id.right_icon:
                recyclerView.post( new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.smoothScrollToPosition( 0 );
                    }
                } );
                break;

        }

    }
    /**
     * 获取数据
     */
    private void getTabkListNew() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.GET_TUIJIAN + "&page=" + indexNum, ShopNewActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(throwable.getMessage());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (indexNum == 1) {
                    refreshLayout.finishRefresh();
                } else {
                    refreshLayout.finishLoadMore();
                }
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (responseString.contains("count")) {
                    try {
                        JSONObject object = new JSONObject(responseString);
                        JSONArray array = object.getJSONArray("data");
                        if (indexNum == 1) {
                            taobaoGuesChildtBeans.clear();
                        }
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject temp = array.getJSONObject(i);
                            double tem = (Double.valueOf(temp.getString("zk_final_price")) - Double.valueOf(temp.getString("coupon_amount"))) * Double.valueOf(df.format(Double.valueOf(temp.getString("commission_rate")) / 100));
                            temp.put("commission", df.format(tem * SPUtils.getIntData(ShopNewActivity.this, "rate", 0) / 100));
                            taobaoGuesChildtBeans.add(gson.fromJson(temp.toString().replace("item_id", "num_iid"), TaobaoGuestBean.TaobaoGuesChildtBean.class));
                        }
                        TodayHighlightsBean todayHighlightsBean = new TodayHighlightsBean();
                        todayHighlightsBean.setTitle(title);
                        todayHighlightsBean.setList(taobaoGuesChildtBeans);
                        shopRecyclerAdapter.notifyDataSetChanged();
                        if (array.length() <= 0) {
                            hasdata = false;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * @属性:获取屏幕高度的方法
     * @开发者:陈飞
     * @时间:2018/8/7 14:22
     */
    public int getScollYDistance() {
        int position = linearLayoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = linearLayoutManager.findViewByPosition( position );
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight; //- firstVisiableChildView.getTop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
