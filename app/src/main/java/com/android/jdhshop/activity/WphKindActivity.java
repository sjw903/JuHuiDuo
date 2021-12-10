package com.android.jdhshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.common.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.WphAdatper;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.Wphbean;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * <pre>
 *     author :yangzhuyuan
 *     e-mail :1050867066@qq.com
 *     time   : 2020/04/20
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class WphKindActivity extends BaseActivity {
    List<Wphbean> wphbeanList = new ArrayList<>();
    WphAdatper wphAdatper;
    int page = 1;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.txt_title)
    TextView txt_title;

    private ImageView fanhui,sousuo;
    @Override
    protected void initUI() {
        setStatusBar(getResources().getColor(R.color.red));
        setContentView(R.layout.activity_wph);
        ButterKnife.bind(this);

        sousuo= findViewById(R.id.wphhome_seek);
        fanhui= findViewById(R.id.wph_fanhui);
        BaseLogDZiYuan.LogDingZiYuan(sousuo, "ic_search_black_24dp.png");
        BaseLogDZiYuan.LogDingZiYuan(fanhui, "icon_back_while.png");
        findViewById(R.id.wphhome_lyback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt_title.setText(getIntent().getExtras().getString("name"));
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, 2);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置滚动方向
        wphAdatper = new WphAdatper(R.layout.today_highlights_child_item2, wphbeanList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(wphAdatper);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                initWphbeanData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page=1;
                initWphbeanData();
            }
        });
        findViewById(R.id.wphhome_seek).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(WphSearchActivity.class);
            }
        });
        initWphbeanData();
        wphAdatper.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle=new Bundle();
                Intent intent = new Intent(WphKindActivity.this, WphDetailsActivity.class);
                bundle.putSerializable("goods", wphbeanList.get(position));
                intent.putExtra("goods", bundle);
                startActivity(intent);
            }
        });
    }

    private void initWphbeanData() {
        RequestParams requestParams = new RequestParams();
        if(getIntent().getExtras().getString("qw")!=null){
            requestParams.put("keyword",getIntent().getExtras().getString("qw"));
        }
        requestParams.put("channelType",getIntent().getExtras().getString("type"));
        requestParams.put("page",page);
        requestParams.put("pageSize","10");
        HttpUtils.post(Constants.APP_IP + (getIntent().getExtras().getString("qw")==null?"/api/WPH/getList":"/api/WPH/query"), WphKindActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onFinish() {
                super.onFinish();
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("dfasdf",responseString);
                try {
                    JSONObject object=new JSONObject(responseString);
                    if("0".equals(object.getString("code"))){
                        if(page==1){
                            wphbeanList.clear();
                        }
                        JSONArray array=object.getJSONObject("data").getJSONArray("goodsInfoList");
                        for(int i=0;i<array.length();i++){
                            wphbeanList.add(new Gson().fromJson(array.getJSONObject(i).toString(),Wphbean.class));
                        }
                        wphAdatper.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {
    }
}


