package com.android.jdhshop.mall;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.malladapter.AddressAdapter;
import com.android.jdhshop.mallbean.AddressBean;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class AddressActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView_address)
    RecyclerView recyclerViewAddress;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private List<AddressBean> list = new ArrayList();
    private AddressAdapter addressAdapter;

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_adreess);
        ButterKnife.bind(this);
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("我的收货地址");
    }

    @Override
    protected void initData() {
        addressAdapter = new AddressAdapter(this, R.layout.item_address, list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewAddress.setLayoutManager(linearLayoutManager);
        recyclerViewAddress.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerViewAddress.setAdapter(addressAdapter);
        if ("2".equals(getIntent().getStringExtra("type"))) {
            addressAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("address", list.get(position));
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLayout.autoRefresh();
    }

    @Override
    protected void initListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                list.clear();
                getList();
            }
        });
    }

    private void getList() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.ADDRESSLIST, AddressActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onStart() {
                super.onStart();
//                showLoadingDialog();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                refreshLayout.finishRefresh();
//                closeLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject temp = new JSONObject(responseString);
                    if (temp.getInt("code") == 0) {
                        GsonBuilder builder = new GsonBuilder();
                        builder.serializeNulls();
                        Gson gson = builder.create();
                        JSONArray array = temp.getJSONObject("data").getJSONArray("list");
                        for (int i = 0; i < array.length(); i++) {
                            list.add(gson.fromJson(array.getJSONObject(i).toString(), AddressBean.class));
                        }
                        addressAdapter.notifyDataSetChanged();
                    } else {
                        showToast(temp.getString("msg"));
                        if ("用户不存在".equals(temp.getString("msg"))) {
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.tv_left, R.id.btn_add_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.btn_add_address:
                openActivity(EditAddressActivity.class);
                break;
        }
    }
}
