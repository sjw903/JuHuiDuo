package com.android.jdhshop.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.DouKindAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.ShopTabsBean;
import com.android.jdhshop.bean.ShopTabsChildBean;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class DouKindActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.grid_view)
    GridView gridView;
    private DouKindAdapter adapter;
    private List<ShopTabsChildBean> list=new ArrayList<>();
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_kind);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("分类");
        adapter=new DouKindAdapter(this,R.layout.item_dou_kind,list);
        gridView.setAdapter(adapter);
        getList();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DouActivity.activity.finish();
                Bundle bundle=new Bundle();
                bundle.putString("id",list.get(position).cat_id);
                openActivity(DouActivity.class,bundle);
                finish();
            }
        });
    }
    private void getList(){
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.GET_DOU_KIND,DouKindActivity.this, requestParams, new onOKJsonHttpResponseHandler<ShopTabsBean>(new TypeToken<Response<ShopTabsBean>>() {
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
            public void onSuccess(int statusCode, Response<ShopTabsBean> datas) {
                if (datas.isSuccess()) {
                    list.addAll(datas.getData().getList());
                    adapter.notifyDataSetChanged();
                } else {
                    showToast(datas.getMsg());
                }
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
