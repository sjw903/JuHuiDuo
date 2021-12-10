package com.android.jdhshop.merchantactivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.merchantadapter.MerchantlistAdapter;
import com.android.jdhshop.merchantbean.Authbean;
import com.android.jdhshop.merchantbean.Merchantlistbean;
import com.android.jdhshop.utils.RecyclerViewSpacesItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 附近二级页面
 * Created by cc on 2017/12/28.
 */

public class MerchantlistActivity extends BaseActivity {

    private LinearLayout ly_back;
    private TextView tv_title;
    private RecyclerView recyclerView;
    SmartRefreshLayout smartRefreshLayout;

    //附近商铺适配器
    MerchantlistAdapter lvAdapter;
    //附近list
    List<Merchantlistbean> nearByLists = new ArrayList<>();

    int page = 1;


    @Override
    protected void initUI() {
        setContentView(R.layout.activity_merchantlist);
        init();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        ly_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化
     */
    private void init() {
        ly_back = findViewById(R.id.ll_back);
        tv_title = findViewById(R.id.tv_name);
        recyclerView = findViewById(R.id.nearbysec_recycl);
        smartRefreshLayout = findViewById(R.id.refresh_layout);

        tv_title.setText(getIntent().getStringExtra("title"));


        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                page = 1;
                nearByLists.clear();
                getNearByLists(page + "");

                //refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page++;
                getNearByLists(page + "");

                //refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败

            }
        });

        getauthlist();
        getNearByLists(page + "");

        lvAdapter = new MerchantlistAdapter(nearByLists);
        recyclerView.addItemDecoration(new RecyclerViewSpacesItemDecoration(8, 15, 8, 15));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(lvAdapter);
        lvAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MerchantlistActivity.this, MerchantmsgActivity.class);
                intent.putExtra("msgid", nearByLists.get(position).merchant_id);
                startActivity(intent);
            }
        });
    }

    /**
     * 获取附近list
     */
    private void getNearByLists(final String p) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", SPUtils.getStringData(this, "token", ""));
        requestParams.put("o2o_cat_id", getIntent().getStringExtra("groupid"));
//        requestParams.put("lng_lat",Constants.jds+","+Constants.wds);
        requestParams.put("page", p);
        requestParams.put("per", "6");
        HttpUtils.post(Constants.getMerchantList, MerchantlistActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogUtils.d("dfasd", responseString);
            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("dfasd", responseString);
                try {
                    if (smartRefreshLayout != null) {
                        smartRefreshLayout.finishRefresh();
                        smartRefreshLayout.finishLoadMore();
                    }
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        JSONArray array = object.getJSONObject("data").getJSONArray("list");
                        if (page == 1) {
                            nearByLists.clear();
                        }
                        for (int i = 0; i < array.length(); i++) {
                            nearByLists.add(new Gson().fromJson(array.getJSONObject(i).toString(), Merchantlistbean.class));
                        }
                        lvAdapter.notifyDataSetChanged();
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }


    //获取服务
    public List<Authbean> authbeanList = new ArrayList<>();

    private void getauthlist() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.getMerchantAuth, MerchantlistActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        JSONArray array = object.getJSONObject("data").getJSONArray("list");
                        for (int i = 0; i < array.length(); i++) {
                            authbeanList.add(new Gson().fromJson(array.getJSONObject(i).toString(), Authbean.class));
                        }
                        lvAdapter.notifyDataSetChanged();
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}