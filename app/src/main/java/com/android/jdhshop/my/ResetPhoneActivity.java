package com.android.jdhshop.my;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.jdhshop.https.HttpUtils.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.BindActivity;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.PddClient;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.login.LoginActivity;
import com.android.jdhshop.login.RegisterActivity;
import com.android.jdhshop.utils.CheckUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * 更换手机号
 * Created by wmm on 2018/11/21.
 */
public class ResetPhoneActivity extends BaseActivity {
    public static final String TAG = "ResetPhoneActivity";
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.et_oldpsd)
    TextInputEditText et_oldpsd;//原手机号验证码
    @BindView(R.id.et_newpsd_sure)//新手机号
            TextInputEditText et_newpsd_sure;
    @BindView(R.id.et_newpsd_sure1)//新手机号验证码
            TextInputEditText et_newpsd_sure1;

    @BindView(R.id.tv_setpsd)//确认
            TextView tv_setpsd;
    @BindView(R.id.get_old_sms)
    TextView getOldSms;
    @BindView(R.id.get_new_sms)
    TextView getNewSms;
    private ACache mAcache;
    String token;
    private TimeCount time1;
    private TimeCount2 time2;
    @Override
    protected void initUI() {
        setContentView(R.layout.ac_reset_phone);
        ButterKnife.bind(this);
        time1 = new TimeCount(60000, 1000);
        time2 = new TimeCount2(60000, 1000);
    }

    @Override
    protected void initData() {
        mAcache = ACache.get(this);
        token = mAcache.getAsString(Constants.TOKEN);
        tv_left.setVisibility(View.VISIBLE);
        tv_title.setText("更换手机号");
    }

    @Override
    protected void initListener() {
    }
    private void setGetOldSms(){
        RequestParams params = new RequestParams();
        HttpUtils.post(Constants.GET_PHONE_CODE, ResetPhoneActivity.this,params, new TextHttpResponseHandler() {
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
                    JSONObject object=new JSONObject(responseString);
                    if(object.getInt("code")==0){
                        time1.start();
                    }else{
                    }
                    T.showShort(ResetPhoneActivity.this,object.getString("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void setGetNewdSms(){
        if (TextUtils.isEmpty(et_newpsd_sure.getText().toString())) {
            showToast("请输入新手机号");
            return;
        }
        if(!CheckUtil.isMobile(et_newpsd_sure.getText().toString().trim())){
            T.showShort(ResetPhoneActivity.this, "请输入正确的手机号");
            return;
        }
        String time1=String.valueOf(System.currentTimeMillis() / 1000);
        RequestParams requestParams = new RequestParams();
        requestParams.put("data_type",PddClient.data_type);
        requestParams.put("version",PddClient.version);
        requestParams.put("timestamp",time1);
        requestParams.put("type","hkx.sms");
        requestParams.put("phone", et_newpsd_sure.getText().toString().trim());
        Map<String,String> temp=new HashMap<>();
        temp.put("data_type",PddClient.data_type);
        temp.put("version",PddClient.version);
        temp.put("timestamp",time1);
        temp.put("type","hkx.sms");
        temp.put("phone", et_newpsd_sure.getText().toString().trim());
        String sign=PddClient.getSign1(temp);
        requestParams.put("sign",sign);
        HttpUtils.post(Constants.CHANGE_PHONE_SENDSMS, ResetPhoneActivity.this,requestParams, new TextHttpResponseHandler() {
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
                    JSONObject object=new JSONObject(responseString);
                    if(object.getInt("code")==0){
                        time2.start();
                    }else{
                    }
                    T.showShort(ResetPhoneActivity.this,object.getString("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void checkOldCode(){
        if (TextUtils.isEmpty(et_oldpsd.getText().toString())) {
            showToast("请输入原手机验证码");
            return;
        }
        RequestParams params = new RequestParams();
        params.put("phone", SPUtils.getStringData(this,"phone",""));
        params.put("code", et_oldpsd.getText().toString().trim());
        HttpUtils.post(Constants.CHECK_CODE, ResetPhoneActivity.this,params, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject object=new JSONObject(responseString);
                    if(object.getInt("code")==0){
                        submitChange();
                    }else{
                        T.showShort(ResetPhoneActivity.this,object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void submitChange(){
        if (TextUtils.isEmpty(et_newpsd_sure.getText().toString())) {
            showToast("请输入新手机号");
            return;
        }
        if (TextUtils.isEmpty(et_newpsd_sure1.getText().toString())) {
            showToast("请输入新手机验证码");
            return;
        }
        RequestParams params = new RequestParams();
        params.put("phone", et_newpsd_sure.getText().toString().trim());
        params.put("code", et_newpsd_sure1.getText().toString().trim());
        HttpUtils.post(Constants.CHNAGE_PHONE,ResetPhoneActivity.this, params, new TextHttpResponseHandler() {
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
                    JSONObject object=new JSONObject(responseString);
                    if(object.getInt("code")==0){
                        finish();
                    }else{
                    }
                    T.showShort(ResetPhoneActivity.this,object.getString("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @OnClick({R.id.tv_left,R.id.get_old_sms, R.id.get_new_sms, R.id.tv_setpsd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.get_old_sms:
                setGetOldSms();
                break;
            case R.id.get_new_sms:
                setGetNewdSms();
                break;
            case R.id.tv_setpsd:
                checkOldCode();//先验证码旧手机验证码对不对
                break;
        }
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
    class TimeCount2 extends CountDownTimer {

        public TimeCount2(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @SuppressLint("NewApi")
        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            getNewSms.setClickable(false);
            getNewSms.setText("倒计时" + millisUntilFinished / 1000 + getResources().getString(R.string.seconds_after));
        }

        @SuppressLint("NewApi")
        @Override
        public void onFinish() {// 计时完毕时触发
            getNewSms.setText(getResources().getString(R.string.get_verification_code));
            getNewSms.setClickable(true);
        }
    }
}



