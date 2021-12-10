package com.android.jdhshop.merchantactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.Buyczqbean;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.merchantactivity.MerRechargeActivity;
import com.android.jdhshop.merchantadapter.BuyczkAdapter;
import com.android.jdhshop.merchantbean.Merchantlistbean;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class BuyczkActivity extends BaseActivity {

        private RecyclerView recyclerView;
        private BuyczkAdapter buyczkAdapter;

        private String seleid = "";
        private String title = "",allpric = "";

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_buyczk);
        recyclerView = findViewById(R.id.buy_recy);
    }

    @Override
    protected void initData() {
        findViewById(R.id.buy_lyback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.buy_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seleid.equals("")){
                    return;
                }
                Intent intent = new Intent(BuyczkActivity.this, MerRechargeActivity.class);
                intent.putExtra("orderid",seleid);
                intent.putExtra("type","1");
                intent.putExtra("allprice",allpric);
                intent.putExtra("title",title);
                startActivity(intent);
            }
        });
        buyczkAdapter = new BuyczkAdapter(BuyczkActivity.this,buylist);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(BuyczkActivity.this,3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(buyczkAdapter);
        buyczkAdapter.setsubClickListener(new BuyczkAdapter.SubClickListener() {
            @Override
            public void OntopicClickListener(String detail, int posit) {
                buyczkAdapter.setSelectindex(posit);
                buyczkAdapter.notifyDataSetChanged();
                seleid = buylist.get(posit).card_cat_id;
                title = buylist.get(posit).cat_name;
                allpric = buylist.get(posit).payment_money;

            }
        });
        getmerchantlist();
    }

    @Override
    protected void initListener() {

    }
    List<Buyczqbean> buylist = new ArrayList<>();
    private void getmerchantlist() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", SPUtils.getStringData(this, "token", ""));
        HttpUtils.post(Constants.getCouplist, BuyczkActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("dsfasd", responseString);
                try {
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        JSONArray array = object.getJSONObject("data").getJSONArray("list");
                        for (int i = 0; i < array.length(); i++) {
                            buylist.add(new Gson().fromJson(array.getJSONObject(i).toString(), Buyczqbean.class));
                        }
                        buyczkAdapter.notifyDataSetChanged();
                        if (buylist.size()!=0) {
                            seleid = buylist.get(0).card_cat_id;
                            title = buylist.get(0).cat_name;
                            allpric = buylist.get(0).payment_money;
                        }
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
