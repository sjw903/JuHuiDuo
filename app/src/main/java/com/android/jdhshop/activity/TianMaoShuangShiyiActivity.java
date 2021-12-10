package com.android.jdhshop.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.ShuangshiyiAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.ShuangshiyiBean;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class TianMaoShuangShiyiActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private int page=1;
    private List<ShuangshiyiBean.Item> list=new ArrayList<>();
    private ShuangshiyiAdapter adapter;
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_tianmao_shuangshiyi);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText(getIntent().getExtras().getString("title"));
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter=new ShuangshiyiAdapter(R.layout.item_shuangshiyi,list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("num_iid", list.get(position).item_id);
                openActivity(PromotionDetailsActivity.class, bundle);
            }
        });
        refresh.setEnableRefresh(false);
        getList();
    }

    @Override
    protected void initListener() {
        refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page=page+1; // 接口有问题，如果是更多页则不会有数据;
                getList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page=1;
                getList();
            }
        });
    }
    private void getList(){
        RequestParams requestParams = new RequestParams();
        requestParams.put("p",page);
        requestParams.put("per","30");
        requestParams.put("material_id",getIntent().getExtras().getString("material_id"));
        HttpUtils.post(Constants.dgOptimusMaterial,TianMaoShuangShiyiActivity.this, requestParams, new onOKJsonHttpResponseHandler<ShuangshiyiBean>(new TypeToken<Response<ShuangshiyiBean>>() {
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
            public void onSuccess(int statusCode, Response<ShuangshiyiBean> datas) {
                if (datas.isSuccess()) {
//                    if(page==1){
//                        list.clear();
//                    }
                    list.addAll(datas.getData().list);
                    adapter.notifyDataSetChanged();
                    refresh.finishLoadMore();
                    refresh.finishRefresh();
                } else {
                    showToast(datas.getMsg());
                    refresh.finishLoadMore();
                    refresh.finishRefresh();
                }
            }
        });
    }
    @OnClick(R.id.tv_left)
    public void onViewClicked() {
        finish();
    }
}
