package com.android.jdhshop.mall;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.common.LogUtils;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.malladapter.ExpressDetailAdapter;
import com.android.jdhshop.mallbean.ExpressDetailBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * 物流详情
 */
public class ExpressActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.img_point)
    ImageView img_point;
    @BindView(R.id.recy_wuliu)
    RecyclerView recyWuliu;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private List<ExpressDetailBean> list=new ArrayList<>();
    private ExpressDetailAdapter adapter;
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_wuliu);
        ButterKnife.bind(this);
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("物流详情");
        BaseLogDZiYuan.LogDingZiYuan(img_point, "icon_point_gray.png");
    }
    @Override
    protected void initData() {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyWuliu.setLayoutManager(linearLayoutManager);
        adapter=new ExpressDetailAdapter(R.layout.item_express_detail,list);
        recyWuliu.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getExpressDetail();
            }
        });
        refreshLayout.autoRefresh();
        refreshLayout.setEnableLoadMore(false);
    }
    private void getExpressDetail(){
        list.clear();
        RequestParams requestParams = new RequestParams();
        requestParams.put("express_number", getIntent().getStringExtra("number"));
        LogUtils.d("dsfasdfsd",requestParams.toString());
        HttpUtils.post("1".equals(Constants.MALL_ORDER_TYPE)?Constants.EXPRESS_DETAIL_UPDATE:Constants.EXPRESS_DETAIL, ExpressActivity.this,requestParams, new onOKJsonHttpResponseHandler<ExpressDetailBean>(new TypeToken<Response<ExpressDetailBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onSuccess(int statusCode, Response<ExpressDetailBean> datas) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefresh();
                        adapter.notifyDataSetChanged();
                    }
                });
                if (datas.isSuccess()) {
                    list.clear();
                    list.addAll(datas.getData().logisticsMsg);
                } else {
//                    showToast(datas.getMsg());
                    if("用户不存在".equals(datas.getMsg())){
                        finish();
                        return;
                    }
                }
            }
        });
    }
    @OnClick(R.id.tv_left)
    public void onViewClicked() {
        finish();
    }
}
