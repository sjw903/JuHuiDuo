package com.android.jdhshop.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.AttendRecordAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.AttendRecordBean;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class AttendRecordActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private AttendRecordAdapter adapter;
    private List<AttendRecordBean.Items> list=new ArrayList<>();
    private int page=1;
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_attend_record);
        ButterKnife.bind(this);
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("签到记录");
    }

    @Override
    protected void initData() {
        adapter=new AttendRecordAdapter(this,R.layout.item_balance_record,list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page=1;
                getList();
            }
        });
        refreshLayout.autoRefresh();
    }
    private void getList(){
        RequestParams requestParams = new RequestParams();
        requestParams.put("page", page);
        requestParams.put("per", 10);
        HttpUtils.post(Constants.ATTEND_RECORD,AttendRecordActivity.class, requestParams, new onOKJsonHttpResponseHandler<AttendRecordBean>(new TypeToken<Response<AttendRecordBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (page == 1) {
                    refreshLayout.finishRefresh();
                } else {
                    refreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onSuccess(int statusCode, Response<AttendRecordBean> datas) {
                if (datas.isSuccess()) {
                    if (page == 1) {
                        list.clear();
                    }
                    list.addAll(datas.getData().getList());
                } else {
                    showToast(datas.getMsg());
                    if("用户不存在".equals(datas.getMsg())){
                        finish();
                        return;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    protected void initListener() {

    }
    @OnClick(R.id.tv_left)
    public void onViewClicked() {
        finish();
    }
}
