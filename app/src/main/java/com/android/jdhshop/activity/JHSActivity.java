package com.android.jdhshop.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.JhsListAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.JhsListbean;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class JHSActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private int indexNum = 1;
    private boolean hasdata = true;
    private List<JhsListbean> jhsListbeans = new ArrayList<>();
    private JhsListAdapter adapter;
    private Gson gson = new Gson();
    DecimalFormat df=new DecimalFormat("0.00");
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_jhs);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("聚划算");
        LinearLayoutManager tempManger = new LinearLayoutManager(this);
        tempManger.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(tempManger);
        adapter = new JhsListAdapter(this, R.layout.jhs_item, jhsListbeans);
        recyclerView.setAdapter(adapter);
        refreshLayout.autoRefresh();
    }

    /**
     * 获取聚划算列表
     */
    private void getList() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.GET_GOODS_JHS + "&page_no=" + indexNum,JHSActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (refreshLayout != null) {
                    if (indexNum == 1) {
                        refreshLayout.finishRefresh();
                    } else {
                        refreshLayout.finishLoadMore();
                    }
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject object = new JSONObject(responseString);
                    if (object.getInt("error") == 0) {
                        if (indexNum == 1) {
                            jhsListbeans.clear();
                        }
                        JSONArray array = object.getJSONArray("data");
                        JhsListbean bean;
                        for (int i = 0; i < array.length(); i++) {
                            bean= gson.fromJson(array.getJSONObject(i).toString(), JhsListbean.class);
                            double money=Double.parseDouble(bean.getOrig_price())*(Double.parseDouble(bean.getCommission_rate())/100);
                            money=money*Double.parseDouble(df.format((float)SPUtils.getIntData(JHSActivity.this,"rate",0)/100));
                            bean.setCommission(df.format(money));
                            jhsListbeans.add(bean);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void initListener() {
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (hasdata) {
                    indexNum++;
                    getList();
                } else {
                    refreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                indexNum = 1;
                hasdata = true;
                getList();
            }
        });
    }

    @OnClick(R.id.tv_left)
    public void onViewClicked() {
        finish();
    }
}
