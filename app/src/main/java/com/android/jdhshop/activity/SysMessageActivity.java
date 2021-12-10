package com.android.jdhshop.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.MessageAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.MessageCenterBean;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SysMessageActivity extends BaseActivity {
    @BindView(R.id.recy_focus)
    RecyclerView recyFocus;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private List<MessageCenterBean.MessageCenterChildBean> articleBeans = new ArrayList<>();
    MessageAdapter adapter;
    private int page = 1;

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("消息通知");
    }

    @Override
    protected void initData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyFocus.setLayoutManager(linearLayoutManager);
        recyFocus.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new MessageAdapter(R.layout.item_message, articleBeans);
        recyFocus.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NewsActivity.actionStart(SysMessageActivity.this,articleBeans.get(position).getArticle_id(), "通知详情");
            }
        });
        getList();
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initListener() {
        refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
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
    }
    private void getList(){
        RequestParams requestParams = new RequestParams();
        requestParams.put("cat_id", 4);
        requestParams.put("p",page);
        requestParams.put("per","10");
        HttpUtils.post(Constants.MESSAGE_ARTICLE_GETARTICLELIST_URL,SysMessageActivity.this, requestParams, new onOKJsonHttpResponseHandler<MessageCenterBean>(new TypeToken<Response<MessageCenterBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(throwable.getMessage());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                refresh.finishRefresh();
                refresh.finishLoadMore();
            }

            @Override
            public void onSuccess(int statusCode, Response<MessageCenterBean> datas) {
                if (datas.isSuccess()) {
                    if (datas.getData().getList().size() <= 0) {
                        return;
                    }
                    if(page==1){
                        articleBeans.clear();
                    }
                    articleBeans.addAll(datas.getData().getList());
                    adapter.notifyDataSetChanged();
                } else {
                    showToast(datas.getMsg());
                }
            }
        });
    }
}
