package com.android.jdhshop.my;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.BindActivity;
import com.android.jdhshop.adapter.BalanceRecordAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.BalanceRecordListBean;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.login.WelActivity;
import com.android.jdhshop.widget.MyListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 余额
 * Created by yohn on 2018/7/14.
 */

public class BalanceActivity extends BaseActivity {
    @BindView(R.id.bg_head)
    LinearLayout bg_head;
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;

    @BindView(R.id.edt_money)
    TextView edt_money;
    @BindView(R.id.lv_record)
    ListView lv_record;

    @BindView(R.id.tv_cust)
    TextView tv_cust;
    @BindView(R.id.tv_tax)
    TextView tv_tax;
    @BindView(R.id.tv_platform)
    TextView tv_platform;

    @BindView(R.id.tv_commit)
    TextView tv_commit;//提现
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;//刷新

    BalanceRecordAdapter adapter = null;
    //数据源
    private List<BalanceRecordListBean.BalanceRecordListChildBean> dataList = new ArrayList<>();

    private int indexNum = 1;
    private ACache mAcache;
    String token,balance,user,service,plantform;
    private boolean hasdata=true;
    @Override
    protected void initUI() {
        setContentView(R.layout.ac_balance);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        Bundle b=getIntent().getExtras();
        if(b!=null){
            if(b.containsKey("balance")){
                balance=b.getString("balance");
            }
            if(b.containsKey("user")){
                user=b.getString("user");
            }
            if(b.containsKey("service")){
                service=b.getString("service");
            }
            if(b.containsKey("plantform")){
                plantform=b.getString("plantform");
            }
        }
        //余额
        if(!TextUtils.isEmpty(balance)){
            edt_money.setText(balance+"元");
        }else{
            edt_money.setText("");
        }
        //客户
        if(!TextUtils.isEmpty(user)){
            tv_cust.setText("客户："+user);
        }else{
            tv_cust.setText("客户：0");
        }
        //税
        if(!TextUtils.isEmpty(service)){
            tv_tax.setText("扣税："+service);
        }else{
            tv_tax.setText("扣税：0");
        }
        //平台
        if(!TextUtils.isEmpty(plantform)){
            tv_platform.setText("平台："+plantform);
        }else{
            tv_platform.setText("平台：0");
        }

        mAcache = ACache.get(this);
        token = mAcache.getAsString(Constants.TOKEN);
        tv_left.setVisibility(View.VISIBLE);
        tv_title.setText("余额");
//        //动态设置drawableLeft属性
//        Drawable drawable = getResources().getDrawable(R.mipmap.icon_my_qq);
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//        tv_right.setCompoundDrawables(null, null, drawable, null);
//        tv_right.setVisibility(View.GONE);

        //获去余额
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String point = extras.getString("balance");
                edt_money.setText(TextUtils.isEmpty(point) ? "0" : point);

            }
        }
        adapter = new BalanceRecordAdapter(this, R.layout.item_balance_record, dataList);
        lv_record.setAdapter(adapter);

        refresh_layout.autoRefresh();

    }

    @Override
    protected void initListener() {
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //联系客服
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.getContactCustomerService(BalanceActivity.this);
            }
        });
        //提现
        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CaiNiaoApplication.getUserInfoBean().user_msg==null){
                    showToast("发生未知错误请重新登录");
                    openActivity(WelActivity.class);
                    finish();
                    return;
                }
                if(CaiNiaoApplication.getUserInfoBean().user_msg.alipay_account==null){
                    openActivity(BindActivity.class);
                }else{
                    Bundle b=new Bundle();
                    b.putString("balance",balance);
                    openActivity(PutForwardActivity.class,b);
                    finish();
                }
            }
        });

        refresh_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (hasdata) {
                    indexNum++;
                    getBalanceRecord(0);
                } else {
                    showToast("没有更多数据了");
                    refreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                indexNum = 1;
                hasdata=true;
                getBalanceRecord(1);
            }
        });
    }

    /**
     * @属性:获取用户余额变动记录
     * @开发者:陈飞
     * @时间:2018/7/22 09:45
     */
    private void getBalanceRecord(final int status) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", token);
        requestParams.put("page", indexNum);
        requestParams.put("per", 10);
        HttpUtils.post(Constants.GET_BALANCE_RECORD,BalanceActivity.this, requestParams, new onOKJsonHttpResponseHandler<BalanceRecordListBean>(new TypeToken<Response<BalanceRecordListBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onSuccess(int statusCode, Response<BalanceRecordListBean> datas) {
                if (datas.isSuccess()) {
                    List<BalanceRecordListBean.BalanceRecordListChildBean> list = datas.getData().getList();
                    if (indexNum == 1) {
                        dataList.clear();
                    }
                    dataList.addAll(list);
                    if(list.size()<=0){
                        hasdata=false;
                    }
                } else {
                    showToast(datas.getMsg());
                    if ("用户不存在".equals(datas.getMsg())){
                        finish();
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

    @Override
    protected void ReceiverIsLoginMessage() {
        super.ReceiverIsLoginMessage();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh_layout.finishRefresh();
            }
        }, 1000);
    }
}
