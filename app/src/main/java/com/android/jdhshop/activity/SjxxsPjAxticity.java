package com.android.jdhshop.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;

import com.android.jdhshop.R;
import com.android.jdhshop.adapter.SjxxsPjAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.SjxxsPjbean;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

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
 *     author : Administrator
 *     e-mail : szp
 *     time   : 2020/05/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class SjxxsPjAxticity extends BaseActivity {
    @BindView(R.id.btn1)
    RadioButton btn1;
    @BindView(R.id.btn2)
    RadioButton btn2;
    @BindView(R.id.btn3)
    RadioButton btn3;
    @BindView(R.id.btn5)
    RadioButton btn5;
    @BindView(R.id.btn4)
    RadioButton btn4;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;
    int page=1;
    private List<SjxxsPjbean> sjxxsPjbeanList = new ArrayList<>();
    SjxxsPjAdapter adapter;
    @Override
    protected void initUI() {
        setContentView(R.layout.sjxxs_pj);
        ButterKnife.bind(this);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initSjxxsPjbean();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new SjxxsPjAdapter(sjxxsPjbeanList);
        recyclerView.setAdapter(adapter);
        getList();
        refresh_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page=1;
                getList();
            }
        });
        getList();
        getData();
    }
    private void  getList(){
        RequestParams requestParams = new RequestParams();
        requestParams.put("merchant_id",getIntent().getExtras().getString("msgid"));
        requestParams.put("p",page);
        HttpUtils.post(Constants.APP_IP+"/api/O2oGoodsComment/commentList",SjxxsPjAxticity.this, requestParams, new TextHttpResponseHandler() {
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
                refresh_layout.finishRefresh();
                refresh_layout.finishLoadMore();
//                closeLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject temp = new JSONObject(responseString);
                    if(temp.getInt("code")==0){
                        GsonBuilder builder=new GsonBuilder();
                        builder.serializeNulls();
                        Gson gson=builder.create();
                        JSONArray array=temp.getJSONObject("data").getJSONArray("list");
                        if(page==1){
                            sjxxsPjbeanList.clear();
                        }
                        for(int i=0;i<array.length();i++){
                            sjxxsPjbeanList.add(gson.fromJson(array.getJSONObject(i).toString(), SjxxsPjbean.class));
                        }
                        adapter.notifyDataSetChanged();
                    }else{
                        showToast(temp.getString("msg"));
                        if("用户不存在".equals(temp.getString("msg"))){
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void getData(){
        RequestParams requestParams = new RequestParams();
        requestParams.put("merchant_id",getIntent().getExtras().getString("msgid"));
        HttpUtils.post(Constants.APP_IP + "/api/O2oGoodsComment/statistics", SjxxsPjAxticity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        btn1.setText("全部("+object.getJSONObject("data").getJSONObject("list").getString("allnum")+")");
                        btn2.setText("晒图("+object.getJSONObject("data").getJSONObject("list").getString("have_img_num")+")");
                        btn3.setText("好评("+object.getJSONObject("data").getJSONObject("list").getString("lvl1_num")+")");
                        btn4.setText("中评("+(Integer.valueOf(object.getJSONObject("data").getJSONObject("list").getString("allnum"))-Integer.valueOf(object.getJSONObject("data").getJSONObject("list").getString("lvl1_num"))-Integer.valueOf(object.getJSONObject("data").getJSONObject("list").getString("lvl2_num")))+")");
                        btn5.setText("差评("+object.getJSONObject("data").getJSONObject("list").getString("lvl2_num")+")");

                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void initSjxxsPjbean() {
        for (int i = 0; i < 2; i++) {
            SjxxsPjbean pj = new SjxxsPjbean(R.drawable.sjxxs1, "淮南子", "2019-06-10", "醇香奶茶搭配香滑补丁、\tQ弹珍珠及鲜嫩口感的仙草冻，多层次的丰富口感，一次满足！", R.mipmap.default_cover);
            sjxxsPjbeanList.add(pj);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }
}
