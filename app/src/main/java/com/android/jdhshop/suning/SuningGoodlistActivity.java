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
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.snadapter.HomegoodlistAdapter2;
import com.android.jdhshop.snbean.HomeGoodlistbean;
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

public class SuningGoodlistActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title;
    LinearLayout ly_back;
    private RecyclerView recyclerView;
    private int page = 1;
    private SmartRefreshLayout smartRefreshLayout;

    private HomegoodlistAdapter2 homegoodlistAdapter2;
    private ImageView fanhui;

    @Override
    protected void initUI() {
        setContentView( R.layout.activity_suning_goodlist );
    }

    @Override
    protected void initData() {
        tv_title = findViewById( R.id.sngoodlist_title );
        ly_back = findViewById( R.id.sngoodlist_lyback );
        fanhui = findViewById( R.id.goodlist_fanhui );
        BaseLogDZiYuan.LogDingZiYuan(fanhui, "icon_back.png");
        recyclerView = findViewById( R.id.sngoodlist_recyclerview );
        smartRefreshLayout = findViewById( R.id.refresh_layout );
        ly_back.setOnClickListener( this );
        tv_title.setText( getIntent().getStringExtra( "title" ) );


        homegoodlistAdapter2 = new HomegoodlistAdapter2( SuningGoodlistActivity.this,R.layout.today_highlights_child_item2, homegoodlist );
        GridLayoutManager gridLayoutManager = new GridLayoutManager( SuningGoodlistActivity.this, 2 );
        gridLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        recyclerView.setLayoutManager( gridLayoutManager );
        recyclerView.setAdapter( homegoodlistAdapter2 );
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
            homegoodlistAdapter2.setOnItemClickListener( new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    Intent intent = new Intent( SuningGoodlistActivity.this,SnGoodmsgActivity.class );
                    intent.putExtra( "goodid", homegoodlist.get( position ).commodityInfo.commodityCode);
                    intent.putExtra( "shopid",homegoodlist.get( position ).commodityInfo.supplierCode );
                     intent.putExtra( "msg", homegoodlist.get( position ));
                    startActivity( intent);
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            } );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sngoodlist_lyback:
                finish();
                break;
        }
    }


    List<HomeGoodlistbean> homegoodlist = new ArrayList<>(  );
    private void getlists() {

        RequestParams requestParams = new RequestParams();
        if (getIntent().getStringExtra( "id" )!=null&&!"".equals( getIntent().getStringExtra( "id" ) )) {
            requestParams.put( "category", getIntent().getStringExtra( "id" ) );
        }
        if (getIntent().getStringExtra( "seek" )!=null&&!"".equals( getIntent().getStringExtra( "seek" ) )){
            requestParams.put( "keywords", getIntent().getStringExtra( "seek" ) );
        }
        requestParams.put( "page",page );
        requestParams.put( "pagesize","8" );
        HttpUtils.post1( Constants.sn_appip+getIntent().getStringExtra( "url" ),SuningGoodlistActivity.this, requestParams, new TextHttpResponseHandler() {
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
                }else{

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
                    JSONObject jsonObject=new JSONObject(responseString);
                    if ("0".equals( jsonObject.getString( "code" ) )) {
                        JSONObject object = jsonObject.getJSONObject( "data" );
                        JSONArray array;
                        if (getIntent().getStringExtra( "seek" )!=null&&!"".equals( getIntent().getStringExtra( "seek" ) )){
                             array = object.getJSONObject( "sn_responseContent" ).getJSONObject( "sn_body" ).getJSONArray( "querySearchcommodity" );
                        }else {
                             array = object.getJSONObject( "sn_responseContent" ).getJSONObject( "sn_body" ).getJSONArray( "queryInverstmentcommodity" );
                        }
                        for (int i = 0; i < array.length(); i++) {
                            homegoodlist.add( new Gson().fromJson( array.getJSONObject( i ).toString(), HomeGoodlistbean.class ) );
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
