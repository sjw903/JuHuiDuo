package com.android.jdhshop.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.SjhdRecyclerAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.SjhdBean;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.login.WelActivity;
import com.android.jdhshop.utils.SlideRecyclerView;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : szp
 *     time   : 2020/05/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class SjhdListAxticity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    SlideRecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    private View view;
    private SjhdRecyclerAdapter shopRecyclerAdapter;
    public List<SjhdBean.SjhdListBean> sjhdListBeanList = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager;
    DecimalFormat df=new DecimalFormat("0.00");
    Gson gson=new Gson();
    private int page1=1,page2=1,page3=1;

    @Override
    protected void initUI() {
        setContentView(R.layout.sjhd_shb_hdlb);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        tvTitle.setText("商家活动");
        tvLeft.setVisibility(View.VISIBLE);
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //适配器
        shopRecyclerAdapter = new SjhdRecyclerAdapter(this, R.layout.sjhd_shb_hdlb_item, sjhdListBeanList);
        //管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //添加分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(shopRecyclerAdapter);
        //取消加载
        //开始刷新
        refreshLayout.autoRefresh();
    }

    @Override
    protected void initListener() {
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (TextUtils.isEmpty(SPUtils.getStringData(SjhdListAxticity.this, Constants.TOKEN, ""))) {
                    openActivity(WelActivity.class);
                    refreshLayout.finishRefresh();
                    return;
                }
                page1++;
                getActivityList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (TextUtils.isEmpty(SPUtils.getStringData(SjhdListAxticity.this, Constants.TOKEN, ""))) {
                    openActivity(WelActivity.class);
                    refreshLayout.finishRefresh();
                    return;
                }
                page1=1;
                getActivityList();
            }
        });
        //点击进入详情
        shopRecyclerAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                SjhdBean.SjhdListBean sjhdListBean = sjhdListBeanList.get(position);
                if (sjhdListBean != null) {
                    Intent intent = new Intent(SjhdListAxticity.this, SjhdDetailAxticity.class);
                    intent.putExtra("sjhdListBean", sjhdListBean);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    protected void ReceiverIsLoginMessage() {
        super.ReceiverIsLoginMessage();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (refreshLayout != null) {
                    refreshLayout.autoRefresh();
                }
            }
        }, 1000);
    }

    @Override
    protected void ReceiverBroadCastMessage(String status, String result, Serializable serializable, Intent intent) {
        super.ReceiverBroadCastMessage(status, result, serializable, intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * @属性:获取活动列表
     * @开发者:陈飞
     * @时间:2018/7/26 17:05
     */
    private void getActivityList() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("merchant_id",getIntent().getStringExtra("merchant_id"));
        requestParams.put("p",page1);
        requestParams.put("per",6);
        HttpUtils.post(Constants.GETSJHDlist, SjhdListAxticity.this,requestParams, new onOKJsonHttpResponseHandler<SjhdBean>(new TypeToken<Response<SjhdBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onSuccess(int statusCode, Response<SjhdBean> datas) {
                if (datas.isSuccess()) {
                    List<SjhdBean.SjhdListBean> list = datas.getData().getList();
                    if(page1==1){
                        sjhdListBeanList.clear();
                    }
                    sjhdListBeanList.addAll(list);
                } else {
                    showToast(datas.getMsg());
                }
                shopRecyclerAdapter.notifyDataSetChanged();
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadMore();
                }
            }
        });
    }
}
