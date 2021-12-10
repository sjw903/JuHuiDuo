package com.android.jdhshop.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.merchantadapter.MerchantDjqAdapter;
import com.android.jdhshop.merchantbean.MerchantNewBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

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
 *     time   : 2020/05/15
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class DjjActivity extends BaseActivity {
    private List<MerchantNewBean.Item> list2 = new ArrayList<>();
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    MerchantDjqAdapter djqAdapter;

    @Override
    protected void initUI() {
        setContentView(R.layout.sjxxs_djj);
        ButterKnife.bind(this);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        djqAdapter = new MerchantDjqAdapter(R.layout.item_djq_new, list2);
        recyclerView.setAdapter(djqAdapter);
        djqAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("merchant_id", getIntent().getStringExtra("msgid"));
                bundle.putSerializable("bean", list2.get(position));
                bundle.putString("name", getIntent().getStringExtra("name"));
                openActivity(SjxxsDjjActivity.class, bundle);
            }
        });
        getList2();
    }

    private void getList2() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("merchant_id", getIntent().getStringExtra("msgid"));
        requestParams.put("p", "1");
        requestParams.put("per", "20");
        HttpUtils.post(Constants.APP_IP + "/api/O2oVoucher/getList", DjjActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Log.e("msg", responseString);
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        list2.addAll(new Gson().fromJson(object.getJSONObject("data").toString(), MerchantNewBean.class).list);
                        djqAdapter.notifyDataSetChanged();
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

    }
}
