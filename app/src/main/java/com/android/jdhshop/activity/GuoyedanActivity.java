package com.android.jdhshop.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.jdhshop.R;
import com.android.jdhshop.adapter.GuoydlistAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.bean.Gydlistbean;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

public class GuoyedanActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private LinearLayout ly_back;
    private SmartRefreshLayout smartRefreshLayout;
    private GuoydlistAdapter guoydlistAdapter;
    private int page = 1;
   private ImageView guoyedd;
    @Override
    protected void initUI() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView( R.layout.activity_guoyedan );
    }

    @Override
    protected void initData() {
        smartRefreshLayout = findViewById( R.id.refresh_layout );
        ly_back = findViewById( R.id.gyd_lyback );
        recyclerView = findViewById( R.id.gyd_recy );
        guoyedd = findViewById( R.id.guoyedd );
        ly_back.setOnClickListener( this );

        guoydlistAdapter = new GuoydlistAdapter( GuoyedanActivity.this,gydlist );
//        recyclerView.addItemDecoration( new RecyclerViewSpacesItemDecoration( 8,15,8,15 ) );
        GridLayoutManager gridLayoutManager = new GridLayoutManager( GuoyedanActivity.this,1 );
        gridLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        recyclerView.setLayoutManager( gridLayoutManager );
        recyclerView.setAdapter( guoydlistAdapter );

        getlist();

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                    gydlist.clear();
                    page = 1;
                    getlist();


                //refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                    page++;
                    getlist();


            }
        });

        BaseLogDZiYuan.LogDingZiYuan(guoyedd, "icon_back_while.png");

    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.gyd_lyback:
                finish();
                break;
        }
    }


    public List<Gydlistbean> gydlist = new ArrayList<>(  );
    private void getlist() {
        RequestParams requestParams = new RequestParams();
        requestParams.put( "live", "2");
        requestParams.put( "page",page );
        requestParams.put( "page_size","8" );
        HttpUtils.post( Constants.APP_IP+"/api/Zhetaoke/getNoticeGoods", GuoyedanActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(throwable.getMessage());
                LogUtils.d("dfasdf",responseString);
            }

            @Override
            public void onStart() {
                showLoadingDialog();
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                closeLoadingDialog();
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("dfasdf",responseString);
                try {
                    JSONObject object = new JSONObject(responseString);
                    if ("200".equals(object.getString("status"))) {
                        JSONArray array = object.getJSONArray("content");
                        for (int i = 0; i < array.length(); i++) {
                            gydlist.add(new Gson().fromJson(array.getJSONObject(i).toString(), Gydlistbean.class));
                        }
                        guoydlistAdapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText( GuoyedanActivity.this,object.getString( "msg" ),Toast.LENGTH_SHORT ).show();
//                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }
}
