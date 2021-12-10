package com.android.jdhshop.login;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jdhshop.https.HttpUtils.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.BindActivity;
import com.android.jdhshop.activity.NewsActivity;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.PddClient;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.my.MyMessageActivity;
import com.android.jdhshop.my.PutForwardActivity;
import com.android.jdhshop.utils.CheckUtil;
import com.android.jdhshop.widget.AutoClearEditText;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 注册
 * Created by yohn on 2018/7/12.
 */

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.et_one)//手机号
            TextInputEditText et_one;
    @BindView(R.id.et_two)//验证码
            TextInputEditText et_two;
    @BindView(R.id.et_three)//密码
            TextInputEditText et_three;
    @BindView(R.id.et_four)//重复密码
            TextInputEditText et_four;
    @BindView(R.id.et_five)//授权码
            TextInputEditText et_five;
    //获取验证码
    @BindView(R.id.btn_code)
    TextView btn_code;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_register)//注册
            TextView tv_register;
    @BindView(R.id.cb_agree)//同意协议
            CheckBox cb_agree;
    @BindView(R.id.bg_head)
    LinearLayout bg_head;
//    @BindView(R.id.tv_qq)
//    TextView tv_qq;

    // 定时器
    private TimeCount time;
    @BindView(R.id.ll_sdf)
    TextInputLayout ll_sdf;
    @Override
    protected void initUI() {
        setContentView(R.layout.ac_register);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        tvTitle.setText("注册新账号");
        //cb_agree.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        time = new TimeCount(60000, 1000);
    }

    @Override
    protected void initListener() {
        findViewById(R.id.txt_agreement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsActivity.actionStart(RegisterActivity.this,"1", "用户协议与隐私条款");
            }
        });
        findViewById(R.id.tv_left).setVisibility(View.VISIBLE);
        findViewById(R.id.tv_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if("N".equals( SPUtils.getStringData(getComeActivity(),"is_auth","Y"))){
            ll_sdf.setHint("邀请码或手机号(选填)");
        }else{
            ll_sdf.setHint("邀请码或手机号(必填)");
        }
        Drawable drawable1 = getResources().getDrawable(R.mipmap.icon_back);
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
        ((TextView) findViewById(R.id.tv_left)).setCompoundDrawables(drawable1, null, null, null);
        //联系客服
        //获取验证码
        btn_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = getTextEditValue(et_one);
                if (TextUtils.isEmpty(phone)) {
                    showToast("请输入您的手机号码");
                    return;
                }
                if(!CheckUtil.isMobile(phone)){
                    T.showShort(RegisterActivity.this, "请输入正确的手机号");
                    return;
                }
                getData(phone);
            }
        });
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String one = getTextEditValue(et_one);
                String two = getTextEditValue(et_two);
                String three = getTextEditValue(et_three);
                String four = getTextEditValue(et_four);
                String five = getTextEditValue(et_five);
                Boolean isCheck = cb_agree.isChecked();
                if (TextUtils.isEmpty(one)) {
                    showToast("请输入您的手机号");
                    return;
                }
                if (TextUtils.isEmpty(two)) {
                    showToast("请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(three)) {
                    showToast("请输入6-18位数字、字母或符号的密码");
                    return;
                }
                if (TextUtils.isEmpty(four)) {
                    showToast("请再次输入密码");
                    return;
                }
                if("N".equals( SPUtils.getStringData(getComeActivity(),"is_auth","Y"))){
                }else{
                    if (TextUtils.isEmpty(five)) {
                        showToast("请输入邀请码或手机号");
                        return;
                    }
                }
                if (!isCheck) {
                    showToast("请勾选同意服务协议");
                    return;
                }
                RequestParams params = new RequestParams();
                if(five.trim().length()>0)
                    if(five.trim().length()>=7){
                        if(CheckUtil.isMobile(five)){
                            params.put("referrer_phone", five);//推荐人
                        }else{
                            showToast("手机号格式不正确");
                            return;
                        }
                    }else if(five.trim().length()<=5){
                        showToast("邀请码格式不正确");
                        return;
                    }else{
                        params.put("auth_code", five);//推荐人
                    }
                params.put("phone", one);//手机号
                params.put("code", two);//验证码
                params.put("pwd1", three);//密码
                params.put("pwd2", four);//重复密码
                registerData(params);
            }
        });
        cb_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // openActivity(AgreementActivity.class);
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
            btn_code.setClickable(false);
            btn_code.setText("倒计时" + millisUntilFinished / 1000 + getResources().getString(R.string.seconds_after));
            // btn_get_code.setTextSize(15);
//            btn_code.setTextColor(getResources().getColor(R.color.col_999));
//            btn_code.setBackground(getResources().getDrawable(R.drawable.bg_border_gray));
        }

        @SuppressLint("NewApi")
        @Override
        public void onFinish() {// 计时完毕时触发
            btn_code.setText(getResources().getString(R.string.get_verification_code));
            btn_code.setClickable(true);
        }

    }

    /**
     * 注册
     **/
    public void registerData(RequestParams params) {
        if (!CommonUtils.isNetworkAvailable()) {
            showToast(getResources().getString(R.string.error_network));
            return;
        }
        HttpUtils.post(true, Constants.REGISTER,RegisterActivity.this, params, new AsyncHttpResponseHandler() {

            //开始
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog("正在注册...");
            }

            //完成
            @Override
            public void onFinish() {
                super.onFinish();
                closeLoadingDialog();
            }

            //成功
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseResult = new String(responseBody);
                LogUtils.d(TAG, responseResult.toString());
                try {
                    JSONObject jsonObject = new JSONObject(responseResult);
                    String code = jsonObject.optString("code");
                    String msg = jsonObject.optString("msg");
                    if ("0".equalsIgnoreCase(code)) {
                        showToast(msg);
                        finish();
                    } else {
                        showToast(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtils.d(TAG, e.toString());
                }
            }

            //失败
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                LogUtils.d(TAG, error.getMessage());
                showToast(error.getMessage());
            }
        });

    }

    /**************************** 接口 *************************************/
    /**
     * 获取验证码接口
     */
    private void getData(String phone) {
        if (!CommonUtils.isNetworkAvailable()) {
            showToast(getResources().getString(R.string.error_network));
            return;
        }
        String time1=String.valueOf(System.currentTimeMillis() / 1000);
        RequestParams requestParams = new RequestParams();
        requestParams.put("data_type",PddClient.data_type);
        requestParams.put("version",PddClient.version);
        requestParams.put("timestamp",time1);
        requestParams.put("type","hkx.sms");
        requestParams.put("phone", phone);
        Map<String,String> temp=new HashMap<>();
        temp.put("data_type",PddClient.data_type);
        temp.put("version",PddClient.version);
        temp.put("timestamp",time1);
        temp.put("type","hkx.sms");
        temp.put("phone", phone);
        String sign=PddClient.getSign1(temp);
        requestParams.put("sign",sign);
        // get方式
        HttpUtils.post(Constants.SEND_USER_REGISTER,RegisterActivity.this, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                // 显示进度条
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                String responseResult = new String(arg2);
                LogUtils.e(TAG, "onSuccess()--" + responseResult);
                try {
                    JSONObject jsonObject = new JSONObject(responseResult);
                    String code = jsonObject.optString("code");
                    String msg = jsonObject.optString("msg");
                    // String result = jsonObject.optString("result");
                    if ("0".equalsIgnoreCase(code)) { // 操作成功
                        time.start();
                        showToast(getResources().getString(R.string.get_verification_code_success));
                    } else {
                        showToast(msg);
                    }
                } catch (JSONException e) {
                    LogUtils.i(TAG, e.toString());
                }
            }

            @Override
            public void onFinish() {
                // 隐藏进度条
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                // 输出错误信息
                LogUtils.e(TAG, "onFailure()--" + arg3.toString());
            }
        });
    }
}
