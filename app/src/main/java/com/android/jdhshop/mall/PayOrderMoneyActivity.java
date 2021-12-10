package com.android.jdhshop.mall;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.android.jdhshop.common.LogUtils;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class PayOrderMoneyActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.txt_money)
    TextView txtMoney;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_info)
    TextView txtInfo;
    @BindView(R.id.rb_pay_zfb)
    RadioButton rbPayZfb;
    @BindView(R.id.rb_pay_yue)
    RadioButton rbPayYue;
    private String order_num = "";

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_pay_money);
        ButterKnife.bind(this);
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("订单支付");
        txtName.setText(getIntent().getStringExtra("name"));
        txtMoney.setText(getIntent().getStringExtra("money"));
        order_num = getIntent().getStringExtra("order_num");
        txtInfo.setText("订单编号: " + getIntent().getStringExtra("order_num"));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @OnClick({R.id.tv_left, R.id.btn_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.btn_pay:
                getpPayMoneyForm(order_num);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SPUtils.getStringData(this, "pay", "0").toString().equals("1")) {
            SPUtils.saveStringData(this, "pay", "0");
            showToast("支付成功");
            Bundle bundle = new Bundle();
            bundle.putString("money", getIntent().getStringExtra("money"));
            bundle.putString("order_num", order_num);
            bundle.putString("order_id", getIntent().getStringExtra("order_id"));
            openActivity(PayResultActivity.class, bundle);
            order_num = "";
            finish();
        }
    }

    private void getpPayMoneyForm(final String order_num1) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("order_id", getIntent().getStringExtra("order_id"));
        requestParams.put("pay_method", rbPayZfb.isChecked() ? "alipay" : "wxpay");
        HttpUtils.post(getIntent().getStringExtra("isVip")!=null?Constants.APP_IP+"/api/UserOrder/getPayForm":Constants.GET_ORDER_FORM, PayOrderMoneyActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogUtils.d("dfasdfsd", responseString);
                showToast(responseString);
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
                LogUtils.d("dsfasd", responseString);
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
//                    showToast(msg);
                    if (code == 0) {
//                        finish();
                        if (responseString.contains("alipay_sdk")) {
                            payZFB(jsonObject.getJSONObject("data").getString("pay_parameters"));
                        } else {
                            //微信支付表单信息回调
                            JSONObject object = null;
                            try {
                                String string = jsonObject.getJSONObject("data").getString("pay_parameters").replaceFirst("\"", "");
                                object = new JSONObject(string);
                                PayReq req = new PayReq();
                                req.appId = Constants.WX_APP_ID;//你的微信appid
                                req.partnerId = object.getString("partnerid");//商户号
                                req.prepayId = object.getString("prepayid");
                                req.nonceStr = object.getString("noncestr");
                                req.timeStamp = object.getString("timestamp");//时间戳
                                req.packageValue = "Sign=WXPay";
                                req.sign = object.getString("sign");//签名
                                CaiNiaoApplication.api.sendReq(req);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            showToast("支付成功");
//                            order_num = "";
//                            Bundle bundle = new Bundle();
//                            bundle.putString("money", getIntent().getStringExtra("money"));
//                            bundle.putString("order_num", order_num1);
//                            openActivity(PayResultActivity.class, bundle);
//                            finish();
                        }
                    } else {
                        showToast(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Handler zfbHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                GsonBuilder builder = new GsonBuilder();
                builder.serializeNulls();
                JSONObject object = new JSONObject(builder.create().toJson(msg.obj));
                if (object.getInt("resultStatus") == 9000) {
                    Bundle bundle = new Bundle();
                    bundle.putString("money", getIntent().getStringExtra("money"));
                    bundle.putString("order_num", order_num);
                    bundle.putString("order_id", getIntent().getStringExtra("order_id"));
                    openActivity(PayResultActivity.class, bundle);
                    order_num = "";
                    showToast("支付成功");
                    finish();
                } else {
                    showToast("支付失败");
                }
            } catch (JSONException e) {
//                showToast("支付发生错误");
            }
        }
    };

    private void payZFB(final String payInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayOrderMoneyActivity.this);
                Map<String, String> result = alipay.payV2(payInfo, true);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                zfbHandle.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
