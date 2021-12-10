package com.android.jdhshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.android.jdhshop.MainActivity;
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

public class WphActivity extends BaseActivity {
    List<Wphbean> wphbeanList = new ArrayList<>();
    WphAdatper wphAdatper;
    int page = 1;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
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
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, 2);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置滚动方向
        wphAdatper = new WphAdatper(R.layout.today_highlights_child_item2, wphbeanList);
        recyclerView.setLayoutManager(linearLayoutManager);
        LinearLayout head = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_head_vph, null);
        wphAdatper.addHeaderView(head);
        head.findViewById(R.id.wph_card1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("name","每日精选");
                bundle.putString("type","2");
                openActivity(WphKindActivity.class,bundle);
            }
        });
        head.findViewById(R.id.wph_card2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("name","出单爆款");
                bundle.putString("type","1");
                openActivity(WphKindActivity.class,bundle);
            }
        });
        head.findViewById(R.id.wph_card4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("name","超高佣金");
                bundle.putString("type","0");
                openActivity(WphKindActivity.class,bundle);
            }
        });
        head.findViewById(R.id.wph_card5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("name","女装会场");
                bundle.putString("type","0");
                bundle.putString("qw","女装");
                openActivity(WphKindActivity.class,bundle);
            }
        });
        head.findViewById(R.id.card6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("name","男装会场");
                bundle.putString("type","0");
                bundle.putString("qw","男装");
                openActivity(WphKindActivity.class,bundle);
            }
        });
        head.findViewById(R.id.wph_card7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("name","美妆会场");
                bundle.putString("type","0");
                bundle.putString("qw","美妆");
                openActivity(WphKindActivity.class,bundle);
            }
        });
        head.findViewById(R.id.card8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("name","母婴好货");
                bundle.putString("type","0");
                bundle.putString("qw","母婴");
                openActivity(WphKindActivity.class,bundle);
            }
        });
        head.findViewById(R.id.wph_card9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("name","内衣会场");
                bundle.putString("type","0");
                bundle.putString("qw","内衣");
                openActivity(WphKindActivity.class,bundle);
            }
        });
        head.findViewById(R.id.card10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("name","鞋靴箱包");
                bundle.putString("type","0");
                bundle.putString("qw","鞋靴");
                openActivity(WphKindActivity.class,bundle);
            }
        });
        head.findViewById(R.id.wph_card11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("name","数码家电");
                bundle.putString("type","0");
                bundle.putString("qw","数码");
                openActivity(WphKindActivity.class,bundle);
            }
        });
        head.findViewById(R.id.card12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("name","个护家清");
                bundle.putString("type","0");
                bundle.putString("qw","个护");
                openActivity(WphKindActivity.class,bundle);
            }
        });
        findViewById(R.id.wphhome_seek).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(WphSearchActivity.class);
            }
        });
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
        initWphbeanData();
        wphAdatper.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle=new Bundle();
                Intent intent = new Intent(WphActivity.this, WphDetailsActivity.class);
                bundle.putSerializable("goods", wphbeanList.get(position));
                intent.putExtra("goods", bundle);
                startActivity(intent);
            }
        });
    }

    private void initWphbeanData() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("channelType", "0");
        requestParams.put("page",page);
        requestParams.put("pageSize","10");
        HttpUtils.post(Constants.APP_IP + "/api/WPH/getList", WphActivity.this,requestParams, new TextHttpResponseHandler() {
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


