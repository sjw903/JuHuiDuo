package com.android.jdhshop.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.adapter.WeiXinQunAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.JDKindBean;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.WeiXinQunXinXIBean;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * 杨亚东
 * 微信群信息名称
 */
public class AiWeiXinQunActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.weixinqun_recy)
    RecyclerView weixinqun_recy;
    private WeiXinQunAdapter weiXinQunAdapter;
//    @BindView(R.id.ai_weixinqun_genghuan)
//    TextView ai_weixinqun_genghuan;

    private List<WeiXinQunXinXIBean> weiXinQunXinXIBeanList = new ArrayList<>();
    private List<String> ssss = new ArrayList<>();

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_ai_wei_xin_qun);
        ButterKnife.bind(this);
        tv_left.setVisibility(View.VISIBLE);
        tv_title.setText("我的群信息");

    }

    @Override
    protected void initData() {
        weixinqun_recy.setLayoutManager(new LinearLayoutManager(AiWeiXinQunActivity.this));
        weiXinQunAdapter = new WeiXinQunAdapter(AiWeiXinQunActivity.this, weiXinQunXinXIBeanList);
        weixinqun_recy.setAdapter(weiXinQunAdapter);
        weiXinQunAdapter.notifyDataSetChanged();
    }

    public void onResume() {
        super.onResume();
        if (!CommonUtils.isNetworkAvailable()) {
            showToast( getResources().getString( R.string.error_network ) );
            return;
        }
        if ("".equals( SPUtils.getStringData( this, "token", "" ) )) {
            return;
        }
        weiXinQunXinXIBeanList.clear();
        weixinqun();
    }
    @Override
    protected void initListener() {

    }

    private void weixinqun() {
        // Log.d(TAG, "weixinqun: ");
        RequestParams params = new RequestParams();
        HttpUtils.post(Constants.getWXQinXinXI, AiWeiXinQunActivity.this,params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    JSONArray arry = jsonObject.getJSONArray("data");

                    // WeiXinQunXinXIBean weiXinQunXinXIBean1 = new WeiXinQunXinXIBean();
//                    weiXinQunXinXIBean1.wx_service_user=jsonObject.getString("wx_service_user");
//                    //jsonObject.getString("wx_service_user");
//                    LogUtils.d("aaaaa",responseString);
                    for (int i = 0; i < arry.length(); i++) {
                        JSONObject object = (JSONObject) arry.get(i);
                        if (object.getString("group_type").equals("1")) {
                            WeiXinQunXinXIBean weiXinQunXinXIBean = new WeiXinQunXinXIBean();
                            weiXinQunXinXIBean.wx_service_user = jsonObject.getString("wx_service_user");
                            try {
                                weiXinQunXinXIBean.group_title = object.getString("group_title");
                            } catch (JSONException e) {
                                weiXinQunXinXIBean.group_title = "";
                            }
                            try {
                                weiXinQunXinXIBean.status = object.getString("status");
                            } catch (JSONException e) {
                                weiXinQunXinXIBean.status = "0";
                            }

                            try {
                                weiXinQunXinXIBean.tmp_id = object.getString("tmp_id");
                            } catch (JSONException e) {
                                weiXinQunXinXIBean.tmp_id = "";
                            }

                            try {
                                weiXinQunXinXIBean.group_type = object.getString("group_type");
                            } catch (JSONException e) {
                                weiXinQunXinXIBean.group_type = "1";
                            }
                            try {
                                weiXinQunXinXIBean.group_num_type = object.getString("group_num_type");
                            } catch (JSONException e) {
                                weiXinQunXinXIBean.group_num_type = "9";
                            }
                            weiXinQunXinXIBeanList.add(weiXinQunXinXIBean);
                        }

                    }
                    // Log.d(TAG, "weixinqun: 2");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            weiXinQunAdapter.notifyDataSetChanged();
                        }
                    });
//
                } catch (JSONException e) {
                    // Log.d(TAG, "weixinqun: 1");
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.tv_left, R.id.set_weixinqun_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
//            case R.id.ai_weixinqun_genghuan://跳转申请助理
//                openActivity(AiOpeningaiActivity.class);
//                break;
            case R.id.set_weixinqun_add:
                if (weiXinQunXinXIBeanList.size() == 0) {
                    Intent intent = new Intent(AiWeiXinQunActivity.this, AiEstablishActivity.class);
                    intent.putExtra("tmpid", "3");
                    startActivity(intent);
                } else {
                    if (weiXinQunXinXIBeanList.size() >= 10) {
                        showToast("最多可建立十个群");
                    } else {
                        for (int i = 0; i < weiXinQunXinXIBeanList.size(); i++) {
                            String status = weiXinQunXinXIBeanList.get(i).status;
                            ssss.add(status);
                        }
                        String s = ssss + "";
                        if (s.indexOf("1") != -1) {
                            showToast("您还有未开通群助理的群号,请先使用完再新增群");
                        } else if (s.indexOf("2") != -1) {
                            showToast("您还有未开通群助理的群号,请先使用完再新增群");
                        } else if (s.indexOf("3") != -1) {
                            showToast("您还有未开通群助理的群号,请先使用完再新增群");
                        } else if (s.indexOf("4") != -1) {
                            showToast("您还有未开通群助理的群号,请先使用完再新增群");
                        } else if (s.indexOf("5") != -1) {
                            showToast("您还有未开通群助理的群号,请先使用完再新增群");
                        } else {
                            Intent intent = new Intent(AiWeiXinQunActivity.this, AiEstablishActivity.class);
                            intent.putExtra("tmpid", "3");
                            startActivity(intent);
                        }
                    }

                }

                break;
        }
    }
}
// if(weiXinQunXinXIBeanList.get(i).status.equals("9")){
//         Intent intent=new Intent(AiWeiXinQunActivity.this, AiEstablishActivity.class);
//        intent.putExtra("tmpid", "3");
//        startActivity(intent);
//        }else{
//        showToast("您还有未开通群助理的群号,请先使用完再新增群");
//        }