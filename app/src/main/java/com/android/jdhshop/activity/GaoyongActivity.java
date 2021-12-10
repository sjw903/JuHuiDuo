package com.android.jdhshop.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.adapter.ShopRecyclerAdapterHd2;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.Gydlistbean;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.utils.DrawableCenterTextView;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class GaoyongActivity extends BaseActivity implements View.OnClickListener {

    DrawableCenterTextView jiageSt;
    DrawableCenterTextView xiaoliangSt;
    DrawableCenterTextView yongjinSt;
    DrawableCenterTextView tuiguangSt;
    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefreshLayout;
    private LinearLayout lyback;
    private ShopRecyclerAdapterHd2 shopRecyclerAdapter;
    private String name = "new";
    private int indexNum = 1;
    private LinearLayoutManager linearLayoutManager;
    private boolean hasdata = true;

    private TextView tv_title;

    private TextView[] textViews;

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_gaoyong);
    }

    @Override
    protected void initData() {
        tv_title = findViewById(R.id.title);
        recyclerView = findViewById(R.id.recyclerView);
        smartRefreshLayout = findViewById(R.id.refresh_layout);
        lyback = findViewById(R.id.gy_lyback);
        jiageSt = findViewById(R.id.jiage_st);
        xiaoliangSt = findViewById(R.id.xiaoliang_st);
        yongjinSt = findViewById(R.id.yongjin_st);
        tuiguangSt = findViewById(R.id.tuiguang_st);
        jiageSt.setOnClickListener(this);
        xiaoliangSt.setOnClickListener(this);
        yongjinSt.setOnClickListener(this);
        tuiguangSt.setOnClickListener(this);
        lyback.setOnClickListener(this);
        textViews = new TextView[]{jiageSt, xiaoliangSt, yongjinSt, tuiguangSt};
        shopRecyclerAdapter = new ShopRecyclerAdapterHd2(GaoyongActivity.this, R.layout.today_highlights_child_item3, taobaoGuesChildtBeans);
        linearLayoutManager = new LinearLayoutManager(GaoyongActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(shopRecyclerAdapter);
        getTbkListRequst(name);
        tv_title.setText(getIntent().getStringExtra("title"));
    }

    @Override
    protected void initListener() {
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                taobaoGuesChildtBeans.clear();
                indexNum = 1;
                getTbkListRequst(name);


                //refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                indexNum++;
                getTbkListRequst(name);


            }
        });

        //点击进入详情
        shopRecyclerAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Gydlistbean taobaoGuesChildtBean = taobaoGuesChildtBeans.get(position);
                if (taobaoGuesChildtBean != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("num_iid", taobaoGuesChildtBean.tao_id);
                    openActivity(PromotionDetailsActivity.class, bundle);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.jiage_st://价格
                if ("price_asc".equals(name)) {
                    name = "price_desc";
                    jiageSt.setText("价格(降)");
                } else if ("price_desc".equals(name)) {
                    name = "price_asc";
                    jiageSt.setText("价格(升)");
                } else {
                    name = "price_asc";
                    jiageSt.setText("价格(升)");
                }
                selectView(1);
                taobaoGuesChildtBeans.clear();
                indexNum = 1;
                getTbkListRequst(name);
                break;
            case R.id.xiaoliang_st://销量
                if ("sale_num_desc".equals(name)) {
                    name = "sale_num_asc";
                    xiaoliangSt.setText("销量(升)");
                } else if ("sale_num_asc".equals(name)) {
                    name = "sale_num_desc";
                    xiaoliangSt.setText("销量(降)");
                } else {
                    name = "sale_num_desc";
                    xiaoliangSt.setText("销量(降)");
                }
                selectView(2);
                taobaoGuesChildtBeans.clear();
                indexNum = 1;
                getTbkListRequst(name);
                break;
            case R.id.yongjin_st://佣金比
                if ("commission_rate_desc".equals(name)) {
                    yongjinSt.setText("佣金比例(升)");
                    name = "commission_rate_asc";
                } else if ("commission_rate_asc".equals(name)) {
                    name = "commission_rate_desc";
                    yongjinSt.setText("佣金比例(降)");
                } else {
                    name = "commission_rate_desc";
                    yongjinSt.setText("佣金比例(降)");
                }
                selectView(3);
                taobaoGuesChildtBeans.clear();
                indexNum = 1;
                getTbkListRequst(name);
                break;
            case R.id.tuiguang_st://推广量
                name = "coupon_info_money_desc";
                tuiguangSt.setText("推广量(降)");
                selectView(4);
                taobaoGuesChildtBeans.clear();
                indexNum = 1;
                getTbkListRequst(name);
                break;
            case R.id.gy_lyback:
                finish();
                break;
        }
    }

    List<Gydlistbean> taobaoGuesChildtBeans = new ArrayList<>();

    private void getTbkListRequst(String search) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("page", indexNum);
        requestParams.put("page_size", "10");
        if (getIntent().getStringExtra("type").equals("100")) {
            requestParams.put("coupon_amount_start", getIntent().getStringExtra("type"));
        } else if (getIntent().getStringExtra("type").equals("20")) {
            requestParams.put("commission_rate_start", getIntent().getStringExtra("type"));
        } else if (getIntent().getStringExtra("type").equals("2")) {
            requestParams.put("baodan", getIntent().getStringExtra("type"));
        } else if (getIntent().getStringExtra("type").equals("pinpai")) {
            requestParams.put("pinpai_name", getIntent().getStringExtra("type"));
        }
        requestParams.put("sort", search);
        HttpUtils.post(Constants.APP_IP + getIntent().getStringExtra("url"), GaoyongActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("dsfasd", responseString);
                try {
                    JSONObject object = new JSONObject(responseString);
                    if ("200".equals(object.getString("status"))) {
                        JSONArray array = object.getJSONArray("content");
                        for (int i = 0; i < array.length(); i++) {
                            taobaoGuesChildtBeans.add(new Gson().fromJson(array.getJSONObject(i).toString(), Gydlistbean.class));
                        }
                        shopRecyclerAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.finishLoadMore();
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
}
