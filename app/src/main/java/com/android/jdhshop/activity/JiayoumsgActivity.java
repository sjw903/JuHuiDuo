package com.android.jdhshop.activity;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jdhshop.common.LogUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.Jymsgsku1Adapter;
import com.android.jdhshop.adapter.JymsgskuAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.Jymsgbean;
import com.android.jdhshop.bean.PddClient;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * 加油详情页面
 */
public class JiayoumsgActivity extends BaseActivity {

    private GridView recyclerView1, recyclerView2;
    private ImageView iv_img;
    private TextView tv_name, tv_address, tv_button;
    private JymsgskuAdapter jymsgskuAdapter;
    private Jymsgsku1Adapter jymsgsku1Adapter;
    private LinearLayout ly_back;
    private String yqh = "";
    private TextView tv_youjia;

    @Override
    protected void initUI() {
        setStatusBar(Color.parseColor("#FEA04D"));
        setContentView(R.layout.activity_jiayoumsg);
        initview();

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    public void initview() {
        phone = CaiNiaoApplication.getInstances().getUserInfoBean().user_msg.phone;
        recyclerView1 = findViewById(R.id.jymsg_recy1);
        recyclerView2 = findViewById(R.id.jymsg_recy2);
        iv_img = findViewById(R.id.jymsg_img);
        tv_name = findViewById(R.id.jymsg_name);
        tv_address = findViewById(R.id.jymsg_address);
        tv_button = findViewById(R.id.jymsg_button);
        ly_back = findViewById(R.id.jiayoumsg_lyback);
        tv_youjia = findViewById(R.id.jiayoumsg_youjia);
        ly_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCode();
//                Intent intent = new Intent(JiayoumsgActivity.this, WebViewActivity.class);
//                intent.putExtra("title", "在线支付");
//                intent.putExtra("url", "http://open.czb365.com/redirection/todo?platformType=92652524&platformCode=" + phone + "&gasId=" + jymsgbeans.get(0).list.get(0).gasId + "&gunNo=" + yqh);
//                startActivity(intent);
            }
        });
        getlist();
    }
    private void  getCode(){
        String time = String.valueOf(System.currentTimeMillis() );
        RequestParams requestParams = new RequestParams();
        requestParams.put("app_key", "appm_api_h592655472");
        requestParams.put("platformId","92655472");
        requestParams.put("timestamp", time);
        requestParams.put("phone", SPUtils.getStringData(this,"phone",""));
        Map<String, String> temp = new HashMap<>();
        temp.put("app_key", "appm_api_h592655472");
        temp.put("platformId","92655472");
        temp.put("timestamp", time);
        temp.put("phone",SPUtils.getStringData(this,"phone",""));
        String sign = PddClient.getSign5(temp);
        requestParams.put("sign", sign);
        HttpUtils.post1("https://mcs.czb365.com/services/v3/begin/getSecretCode",JiayoumsgActivity.this, requestParams, new TextHttpResponseHandler() {
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
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject object = new JSONObject(responseString);
                    if (object.getInt("code") == 200) {
//                        Intent intent = new Intent(context, WebViewActivity.class);
////                        intent.putExtra("title", "在线支付");
////                        intent.putExtra("url", "http://open.czb365.com/redirection/todo?platformType=98651638&platformCode=" + object.getString("result") + "&gasId=" + jymsgbeans.get(0).list.get(0).gasId + "&gunNo=" + yqh);
////                        startActivity(intent);
                        Intent intent = new Intent(JiayoumsgActivity.this, WebViewActivity.class);
                        intent.putExtra("title","加油");
                        intent.putExtra("url", "https://open.czb365.com/redirection/todo/?platformType=92655472&authCode="+object.getString("result")+"&gasId=" + jymsgbeans.get(0).list.get(0).gasId + "&gunNo=" + yqh);
                        LogUtils.d("url", "https://open.czb365.com/redirection/todo/?platformType=92655472&authCode="+object.getString("result")+"&gasId=" + jymsgbeans.get(0).list.get(0).gasId + "&gunNo=" + yqh);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    int index = 0;
    private String phone;
    public List<Jymsgbean> jymsgbeans = new ArrayList<>();

    private void getlist() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("phone", phone);
        requestParams.put("gasIds", getIntent().getStringExtra("id"));
        LogUtils.d("dsfasd",requestParams.toString());
        HttpUtils.post(Constants.gas_detail,JiayoumsgActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(throwable.getMessage());
                LogUtils.d("dsfasdf",responseString);
            }

            @Override
            public void onStart() {
                super.onStart();
//                showLoadingDialog();
            }

            @Override
            public void onFinish() {
                super.onFinish();
//                closeLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("dsfasdf",responseString);
                try {
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        JSONObject array = object.getJSONObject("data");
//                        for (int i = 0; i < array.length(); i++) {
                            jymsgbeans.add(new Gson().fromJson(array.toString(), Jymsgbean.class));
//                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_name.setText(jymsgbeans.get(0).list.get(0).gasName);
                                tv_address.setText(getIntent().getStringExtra("address"));
                                Glide.with(JiayoumsgActivity.this).load(getIntent().getStringExtra("img")).into(iv_img);
                                jymsgskuAdapter = new JymsgskuAdapter(JiayoumsgActivity.this, jymsgbeans.get(0).list.get(0).oilPriceList);
                                if (jymsgbeans.get(0).list.get(0).oilPriceList.get(0).gunNos != null) {
                                    yqh = jymsgbeans.get(0).list.get(0).oilPriceList.get(0).gunNos.get(0).gunNo;
                                }
                                tv_youjia.setText(jymsgbeans.get(0).list.get(0).oilPriceList.get(0).priceYfq);
                                recyclerView1.setAdapter(jymsgskuAdapter);
                                recyclerView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        if (jymsgbeans.get(0).list.get(0).oilPriceList.get(i).gunNos != null) {
                                            yqh = jymsgbeans.get(0).list.get(0).oilPriceList.get(i).gunNos.get(0).gunNo;
                                        }
                                        tv_youjia.setText(jymsgbeans.get(0).list.get(0).oilPriceList.get(i).priceYfq);
                                        jymsgskuAdapter.setSelectindex(i);
                                        index = i;
                                        jymsgskuAdapter.notifyDataSetChanged();
                                        if (jymsgbeans.get(0).list.get(0).oilPriceList.get(i).gunNos != null) {
                                            jymsgsku1Adapter = new Jymsgsku1Adapter(JiayoumsgActivity.this, jymsgbeans.get(0).list.get(0).oilPriceList.get(i).gunNos);
                                            recyclerView2.setAdapter(jymsgsku1Adapter);
                                        }
                                    }
                                });
                                if (jymsgbeans.get(0).list.get(0).oilPriceList.get(index).gunNos != null) {
                                    jymsgsku1Adapter = new Jymsgsku1Adapter(JiayoumsgActivity.this, jymsgbeans.get(0).list.get(0).oilPriceList.get(index).gunNos);
                                    recyclerView2.setAdapter(jymsgsku1Adapter);
                                }
                                recyclerView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        yqh = jymsgbeans.get(0).list.get(0).oilPriceList.get(index).gunNos.get(i).gunNo;
                                        jymsgsku1Adapter.setSelectindex(i);
                                        jymsgsku1Adapter.notifyDataSetChanged();
                                    }
                                });

                            }
                        });
                    } else {
                        Toast.makeText(JiayoumsgActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
//                        showToast(object.getString("msg"));
                    }
                    Log.e("size", jymsgbeans.size() + "adf");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }
}
