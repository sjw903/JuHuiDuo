package com.android.jdhshop.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.snadapter.HomegoodlistAdapter2;
import com.android.jdhshop.snbean.HomeGoodlistbean;
import com.android.jdhshop.suning.SnGoodmsgActivity;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SuningGoodlistFragment extends BaseLazyFragment  {

    private TextView tv_title;
    LinearLayout ly_back;
    private RecyclerView recyclerView;
    private int page = 1;
    private SmartRefreshLayout smartRefreshLayout;
    String keywords="";
    private HomegoodlistAdapter2 homegoodlistAdapter2;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_suning_goodlist, container, false);
        ButterKnife.bind(this, view);
        view.findViewById(R.id.rl_top).setVisibility(View.GONE);
        initData();
        initListener();
        return view;
    }
    public void setContent(String keyword) {
        this.keywords = keyword;
        smartRefreshLayout.autoRefresh();
    }

    protected void initData() {
        tv_title = view.findViewById( R.id.sngoodlist_title );
        ly_back = view. findViewById( R.id.sngoodlist_lyback );
        recyclerView =  view.findViewById( R.id.sngoodlist_recyclerview );
        smartRefreshLayout =  view.findViewById( R.id.refresh_layout );


        homegoodlistAdapter2 = new HomegoodlistAdapter2( context,R.layout.today_highlights_child_item2, homegoodlist );
        GridLayoutManager gridLayoutManager = new GridLayoutManager( context, 2 );
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
//        getlists();//获取小编推荐
    }

    protected void initListener() {
            homegoodlistAdapter2.setOnItemClickListener( new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    Intent intent = new Intent(context,SnGoodmsgActivity.class );
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



    List<HomeGoodlistbean> homegoodlist = new ArrayList<>(  );
    private void getlists() {

        RequestParams requestParams = new RequestParams();
            requestParams.put( "keywords",keywords );
        requestParams.put( "page",page );
        requestParams.put( "pagesize","8" );
        HttpUtils.post1( Constants.sn_appip+"/api/Suning/getKeywordGoods",SuningGoodlistFragment.this, requestParams, new TextHttpResponseHandler() {
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
                             array = object.getJSONObject( "sn_responseContent" ).getJSONObject( "sn_body" ).getJSONArray( "querySearchcommodity" );
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

    @Override
    protected void lazyload() {

    }
}
