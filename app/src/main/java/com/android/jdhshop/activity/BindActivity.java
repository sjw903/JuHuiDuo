package com.android.jdhshop.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputEditText;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.jdhshop.login.BindPhoneActivity;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.PddClient;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.utils.CheckUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class BindActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.txt_one)
    TextView txtOne;
    @BindView(R.id.et_oldpsd)
    TextInputEditText etOldpsd;
    @BindView(R.id.get_old_sms)
    TextView getOldSms;
    @BindView(R.id.et_newpsd_sure)
    TextInputEditText etNewpsdSure;
    @BindView(R.id.et_newpsd_sure1)
    TextInputEditText etNewpsdSure1;
    @BindView(R.id.tip)
    TextView tip;
    private TimeCount time1;

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_bindzfb);
        ButterKnife.bind(this);
        time1 = new TimeCount(60000, 1000);

    }

    @Override
    protected void initData() {
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("绑定支付宝");
        tip.setSelected(true);
        etNewpsdSure.setText(CaiNiaoApplication.getUserInfoBean().user_detail.truename == null ? "" : CaiNiaoApplication.getUserInfoBean().user_detail.truename);
        txtOne.setText("已绑定支付宝: " + (CaiNiaoApplication.getUserInfoBean().user_msg.alipay_account == null ? "" : CaiNiaoApplication.getUserInfoBean().user_msg.alipay_account));
        etNewpsdSure1.setText(""+(CaiNiaoApplication.getUserInfoBean().user_msg.alipay_account == null ? "" : CaiNiaoApplication.getUserInfoBean().user_msg.alipay_account));
    }

    @Override
    protected void initListener() {
        getOldSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tip.setVisibility(View.GONE);
                String time = String.valueOf(System.currentTimeMillis() / 1000);
                RequestParams requestParams = new RequestParams();
                requestParams.put("data_type", PddClient.data_type);
                requestParams.put("version", PddClient.version);
                requestParams.put("timestamp", time);
                requestParams.put("token", SPUtils.getStringData(BindActivity.this, "token", ""));
                requestParams.put("type", "hkx.userDraw.sendAlipayCode");
                Map<String, String> temp = new HashMap<>();
                temp.put("data_type", PddClient.data_type);
                temp.put("version", PddClient.version);
                temp.put("timestamp", time);
                temp.put("token", SPUtils.getStringData(BindActivity.this, "token", ""));
                temp.put("type", "hkx.userDraw.sendAlipayCode");
                String sign = PddClient.getSign1(temp);
                requestParams.put("sign", sign);
                HttpUtils.post1(Constants.SEND_ALICOUNT_YAM, BindActivity.this,requestParams, new TextHttpResponseHandler() {
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
                            if (object.getInt("code") == 0) {
                                time1.start();
                                tip.setVisibility(View.VISIBLE);
                                tip.setText(Html.fromHtml("短信验证码已发送至您的当前登录手机号:  <span style='color:#FE0036'>"+SPUtils.getStringData(BindActivity.this,"phone","")+"</span>， 请注意查收"));
                            } else {
                                showToast( object.getString("msg"));
                                if ("用户不存在".equals(object.getString("msg"))){
                                    finish();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @OnClick({R.id.tv_left, R.id.tv_setpsd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.tv_setpsd:
                if (etNewpsdSure1.getText().toString().equals("") || etNewpsdSure.getText().toString().equals("")) {
                    T.showShort(BindActivity.this, "请填写完整信息");
                    return;
                }
                if(CheckUtil.isEmail(etNewpsdSure1.getText().toString().trim())||CheckUtil.isMobile(etNewpsdSure1.getText().toString().trim())){
                    bindZFB();
                }else{
                    T.showShort(BindActivity.this, "请输入正确的手机号或邮箱");
                }
                break;
        }
    }

    //修改接口传递参数
    private void bindZFB() {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        RequestParams requestParams = new RequestParams();
        requestParams.put("data_type", PddClient.data_type);
        requestParams.put("version", PddClient.version);
        requestParams.put("timestamp", time);
        requestParams.put("token", SPUtils.getStringData(BindActivity.this, "token", ""));
        requestParams.put("type", "hkx.userDraw.changeAlipay");
        requestParams.put("alipay_account", etNewpsdSure1.getText().toString().trim());
        requestParams.put("truename", etNewpsdSure.getText().toString().trim());
        //requestParams.put("code", etOldpsd.getText().toString().trim());
        Map<String, String> temp = new HashMap<>();
        temp.put("data_type", PddClient.data_type);
        temp.put("version", PddClient.version);
        temp.put("timestamp", time);
        temp.put("token", SPUtils.getStringData(BindActivity.this, "token", ""));
        temp.put("type", "hkx.userDraw.changeAlipay");
        temp.put("alipay_account", etNewpsdSure1.getText().toString().trim());
        temp.put("truename", etNewpsdSure.getText().toString().trim());
        //temp.put("code", etOldpsd.getText().toString().trim());
        String sign = PddClient.getSign1(temp);
        requestParams.put("sign", sign);
        HttpUtils.post1(Constants.CHANGEZFB,  BindActivity.this,requestParams, new TextHttpResponseHandler() {
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
                    if (object.getInt("code") == 0) {
                        CaiNiaoApplication.getUserInfoBean().user_detail.truename = etNewpsdSure.getText().toString();
                        CaiNiaoApplication.getUserInfoBean().user_msg.alipay_account = etNewpsdSure1.getText().toString();
                        finish();
                    }
                    Intent intent = new Intent();
                    intent.putExtra("data_return",etNewpsdSure.getText().toString());
                    setResult(RESULT_OK,intent);
                    SPUtils.saveStringData(BindActivity.this, "zfb_name", etNewpsdSure.getText().toString());
                    SPUtils.saveStringData(BindActivity.this, "zfb_id", etNewpsdSure1.getText().toString());
                    showToast( object.getString("msg"));
                    if ("用户不存在".equals(object.getString("msg"))){
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @SuppressLint("NewApi")
        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            getOldSms.setClickable(false);
            getOldSms.setText("倒计时" + millisUntilFinished / 1000 + getResources().getString(R.string.seconds_after));
        }

        @SuppressLint("NewApi")
        @Override
        public void onFinish() {// 计时完毕时触发
            getOldSms.setText(getResources().getString(R.string.get_verification_code));
            getOldSms.setClickable(true);
        }

    }
}
