package com.android.jdhshop.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.ziyuan.ResourceManager;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.BalanceRecordAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.BalanceRecordListBean;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.tencent.mm.opensdk.utils.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 余额
 * Created by yohn on 2018/7/14.
 */

public class CommissionActivity extends BaseActivity {
    @BindView(R.id.bg_head)
    LinearLayout bg_head;
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.wushuju_image)
    ImageView wushuju_image;
    @BindView(R.id.wushuju_text)
    TextView wushuju_text;
    @BindView(R.id.lv_record)
    ListView lv_record;
    @BindView(R.id.tv_commit)
    TextView tv_commit;//提现
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;//刷新

    BalanceRecordAdapter adapter = null;
    @BindView(R.id.ll_one)
    LinearLayout llOne;
    //数据源
    private List<BalanceRecordListBean.BalanceRecordListChildBean> dataList = new ArrayList<>();
    private int indexNum = 1;
    private ACache mAcache;
    String token;
    private boolean hasdata = true;
    private int type = 1;//奖还是提现

    @Override
    protected void initUI() {
        setContentView(R.layout.ac_balance);
        ButterKnife.bind(this);
        llOne.setVisibility(View.GONE);
        tv_commit.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            type = b.getInt("type");
        }
        mAcache = ACache.get(this);
        token = mAcache.getAsString(Constants.TOKEN);
        tv_left.setVisibility(View.VISIBLE);
        tv_title.setText(type == 1 ? "奖收入明细" : "提现明细");
        adapter = new BalanceRecordAdapter(this, R.layout.item_balance_record, dataList);
        lv_record.setAdapter(adapter);
        refresh_layout.autoRefresh();

        BaseLogDZiYuan.LogDingZiYuan(wushuju_image, "wushuju.png");
    }

    @Override
    protected void initListener() {
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        refresh_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (hasdata) {
                    indexNum++;
                    getBalanceRecord();
                } else {
                    showToast("没有更多数据了");
                    refreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                indexNum = 1;
                hasdata = true;
                getBalanceRecord();
            }
        });
    }

    /**
     * @属性:获取用户余额变动记录
     * @开发者:陈飞
     * @时间:2018/7/22 09:45
     */
    private void getBalanceRecord() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", token);
        requestParams.put("page", indexNum);
        requestParams.put("per", 10);
        requestParams.put("type", type);
        HttpUtils.post(Constants.COMMISSION_PUT_DETAIL, CommissionActivity.this, requestParams, new onOKJsonHttpResponseHandler<BalanceRecordListBean>(new TypeToken<Response<BalanceRecordListBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onSuccess(int statusCode, Response<BalanceRecordListBean> datas) {
                if (datas.isSuccess()) {


                    List<BalanceRecordListBean.BalanceRecordListChildBean> list = datas.getData().getList();
                    //判断为空吐司提示
                    if (list == null || list.size() == 0) {

                        wushuju_image.setVisibility(View.VISIBLE);
                        wushuju_text.setVisibility(View.VISIBLE);
                        //showToast("无收入明细");


                    }
                    if (indexNum == 1) {
                        dataList.clear();
                    }
                    dataList.addAll(list);
                    if (list.size() <= 0) {
                        hasdata = false;
                    }
                } else {
                    showToast(datas.getMsg());
                    if ("用户不存在".equals(datas.getMsg())) {
                        finish();
                        return;
                    }
                }
                adapter.notifyDataSetChanged();
                if (indexNum == 1) {
                    refresh_layout.finishRefresh();
                } else {
                    refresh_layout.finishLoadMore();
                }
            }
        });
    }
}
