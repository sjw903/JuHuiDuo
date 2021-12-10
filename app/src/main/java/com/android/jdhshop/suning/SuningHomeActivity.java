package com.android.jdhshop.suning;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.snadapter.HomegoodlistAdapter2;
import com.android.jdhshop.snadapter.SnhometopAdapter;
import com.android.jdhshop.snbean.HomeGoodlistbean;
import com.android.jdhshop.snbean.Suhomebean;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 苏宁首页
 */
public class SuningHomeActivity extends BaseActivity implements View.OnClickListener {


    private LinearLayout ly_back;
    private ImageView iv_seek;
    private RecyclerView toprecy, buttonrecy, toprecy1;
    private SnhometopAdapter snhometopAdapter;
    private SnhometopAdapter snhometopAdapter1;
    private HomegoodlistAdapter2 homegoodlistAdapter2;
    private SmartRefreshLayout smartRefreshLayout;
    private int page = 1;
    private ImageView fanhui, tuijian;

    @Override
    protected void initUI() {
        setStatusBar(Color.parseColor("#F4B945"));
        setContentView(R.layout.activity_suning_home);
    }

    @Override
    protected void initData() {
        ly_back = findViewById(R.id.snhome_lyback);
        iv_seek = findViewById(R.id.snhome_seek);
        toprecy = findViewById(R.id.snhome_toprecy);
        fanhui = findViewById(R.id.suining_fanhui);
        tuijian = findViewById(R.id.suning_tui);
        BaseLogDZiYuan.LogDingZiYuan(fanhui, "icon_back_while.png");
        BaseLogDZiYuan.LogDingZiYuan(iv_seek, "ic_search_black_24dp.png");
        BaseLogDZiYuan.LogDingZiYuan(tuijian, "suningxiaobiao.png");

        buttonrecy = findViewById(R.id.snhome_buttonrecy);
        buttonrecy.setHasFixedSize(true);
        buttonrecy.setNestedScrollingEnabled(false);
        toprecy1 = findViewById(R.id.snhome_toprecy1);
        smartRefreshLayout = findViewById(R.id.refresh_layout);

        ly_back.setOnClickListener(this);
        iv_seek.setOnClickListener(this);

        homegoodlistAdapter2 = new HomegoodlistAdapter2(SuningHomeActivity.this, R.layout.today_highlights_child_item2, homegoodlist);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(SuningHomeActivity.this, 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        buttonrecy.setLayoutManager(gridLayoutManager);
        buttonrecy.setAdapter(homegoodlistAdapter2);
        homegoodlistAdapter2.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(SuningHomeActivity.this, SnGoodmsgActivity.class);
                intent.putExtra("goodid", homegoodlist.get(position).commodityInfo.commodityCode);
                intent.putExtra("shopid", homegoodlist.get(position).commodityInfo.supplierCode);
                intent.putExtra("msg", homegoodlist.get(position));
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        getgoodlists();


        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                homegoodlist.clear();
                page = 1;
                getlists();


                //refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page++;
                getlists();


                //refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败

            }
        });
        getlists();//获取小编推荐
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.snhome_lyback:
                finish();
                break;
            case R.id.snhome_seek:
                Intent intent = new Intent(SuningHomeActivity.this, SnSeekActivity.class);
                startActivity(intent);
                break;
        }
    }

    List<Suhomebean> goodlistbeans = new ArrayList<>();

    private void getgoodlists() {

        RequestParams requestParams = new RequestParams();
        HttpUtils.post1(Constants.GET_COMMISSIONCATEGORY, SuningHomeActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();

//                if (smartRefreshLayout!=null){
//                    smartRefreshLayout.finishRefresh();
//                    smartRefreshLayout.finishLoadMore();
//                }else{
//                    showLoadingDialog();
//                }
            }

            @Override
            public void onFinish() {
                super.onFinish();

//                if (smartRefreshLayout != null) {
//
//                }else{
//                    closeLoadingDialog();
//                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    if ("0".equals(jsonObject.getString("code"))) {
//                        JSONObject object = jsonObject.getJSONObject( "data" );
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            goodlistbeans.add(new Gson().fromJson(array.getJSONObject(i).toString(), Suhomebean.class));
                        }
                        snhometopAdapter = new SnhometopAdapter(SuningHomeActivity.this, goodlistbeans.subList(0, 3));
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(SuningHomeActivity.this, 3);
                        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        toprecy.setLayoutManager(gridLayoutManager);
                        toprecy.setAdapter(snhometopAdapter);

                        snhometopAdapter1 = new SnhometopAdapter(SuningHomeActivity.this, goodlistbeans.subList(3, goodlistbeans.size()));
                        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(SuningHomeActivity.this, 4);
                        gridLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
                        toprecy1.setLayoutManager(gridLayoutManager1);
                        toprecy1.setAdapter(snhometopAdapter1);
//                        snhometopAdapter.notifyDataSetChanged();


                        snhometopAdapter.setsubClickListener(new SnhometopAdapter.SubClickListener() {
                            @Override
                            public void OntopicClickListener(View v, String detail, int posit) {
                                Intent intent = new Intent(SuningHomeActivity.this, SuningGoodlistActivity.class);
                                intent.putExtra("id", goodlistbeans.subList(0, 3).get(posit).sn_cat_id);
                                intent.putExtra("title", goodlistbeans.subList(0, 3).get(posit).name);
                                intent.putExtra("url", "/api/Suning/getCommissionGoodsList");

                                startActivity(intent);
                            }
                        });
                        snhometopAdapter1.setsubClickListener(new SnhometopAdapter.SubClickListener() {
                            @Override
                            public void OntopicClickListener(View v, String detail, int posit) {
                                Intent intent = new Intent(SuningHomeActivity.this, SuningGoodlistActivity.class);
                                intent.putExtra("id", goodlistbeans.subList(3, goodlistbeans.size()).get(posit).sn_cat_id);
                                intent.putExtra("title", goodlistbeans.subList(3, goodlistbeans.size()).get(posit).name);
                                intent.putExtra("url", "/api/Suning/getCommissionGoodsList");
                                startActivity(intent);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    List<HomeGoodlistbean> homegoodlist = new ArrayList<>();

    private void getlists() {

        RequestParams requestParams = new RequestParams();
        requestParams.put("page", page);
        requestParams.put("pagesize", "8");
        HttpUtils.post1(Constants.GET_RECOMMENDGOODS,SuningHomeActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
//                if (smartRefreshLayout!=null){
//                    smartRefreshLayout.finishRefresh();
//                    smartRefreshLayout.finishLoadMore();
//                }else{
//                    showLoadingDialog();
//                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.finishLoadMore();
                } else {

                }
//                if (smartRefreshLayout != null) {
//
//                }else{
//                    closeLoadingDialog();
//                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    if ("0".equals(jsonObject.getString("code"))) {
                        JSONObject object = jsonObject.getJSONObject("data");
                        JSONArray array = object.getJSONObject("sn_responseContent").getJSONObject("sn_body").getJSONArray("queryRecommendcommodity");
                        for (int i = 0; i < array.length(); i++) {
                            homegoodlist.add(new Gson().fromJson(array.getJSONObject(i).toString(), HomeGoodlistbean.class));
                        }
                        homegoodlistAdapter2.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
