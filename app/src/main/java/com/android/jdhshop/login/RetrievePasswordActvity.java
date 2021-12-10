package com.android.jdhshop.login;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jdhshop.https.HttpUtils.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 忘记密码
 * Created by yohn on 2018/7/13.
 */

public class RetrievePasswordActvity extends BaseActivity {
    @BindView(R.id.bg_head)
    LinearLayout bg_head;
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_title)
    TextView tv_title;
    //获取验证码
    @BindView(R.id.btn_code)
    TextView btn_code;

    //输入框
    @BindView(R.id.et_one)
    TextInputEditText et_one;//账号
    @BindView(R.id.et_two)
    TextInputEditText et_two;//验证码
    @BindView(R.id.et_three)
    TextInputEditText et_three;//新密码
    @BindView(R.id.et_four)
    TextInputEditText et_four;//重复密码

    @BindView(R.id.tv_register)
    TextView tv_register;//注册新账号
    @BindView(R.id.tv_fnish)
    TextView tv_fnish;//确认重置
//    @BindView(R.id.tv_qq)
//    TextView tv_qq;

    // 定时器
    private TimeCount time;

    @Override
    protected void initUI() {
        setContentView(R.layout.ac_ret_psd);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        bg_head.setBackgroundColor(getResources().getColor(android.R.color.white));
        //动态设置drawableLeft属性
        Drawable drawable1 = getResources().getDrawable(R.mipmap.icon_back);
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
        tv_left.setCompoundDrawables(drawable1, null, null, null);
        tv_left.setVisibility(View.VISIBLE);

        tv_title.setText("忘记密码");
        tv_title.setTextColor(getResources().getColor(R.color.col_333));
        time = new TimeCount(60000, 1000);
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }


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


    @Override
    protected void initListener() {
        //联系客服
//        tv_qq.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CommonUtils.getContactCustomerService(RetrievePasswordActvity.this);
//            }
//        });
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //获取验证码
        btn_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = getTextEditValue(et_one);
                if (TextUtils.isEmpty(phone)) {
                    showToast("请输入您的手机号码");
                    return;
                }
                getData(phone);
            }
        });
        //注册新账号
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(RegisterActivity.class);
            }
        });
        et_one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_one.length() == 11) {// 手机号的长度=11位
                    btn_code.setEnabled(true);
                } else {// 手机号的长度<11位
                    btn_code.setEnabled(false);
                }
            }
        });
        //确认重置
        tv_fnish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String one = getTextEditValue(et_one);
                String two = getTextEditValue(et_two);
                String three = getTextEditValue(et_three);
                String four = getTextEditValue(et_four);
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
                    showToast("请输入6-18位数字、字母或符号的密码");
                    return;
                }
                RequestParams params = new RequestParams();
                params.put("phone", one);//手机号
                params.put("code", two);//验证码
                params.put("pwd", three);//密码
                retrieveData(params);
            }
        });
    }

    /**
     * 修改密码
     **/
    public void retrieveData(RequestParams params) {
        if (!CommonUtils.isNetworkAvailable()) {
            showToast(getResources().getString(R.string.error_network));
            return;
        }
        HttpUtils.post(true, Constants.FINDPWD_BY_PHONE, RetrievePasswordActvity.this, params, new AsyncHttpResponseHandler() {

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

        RequestParams params = new RequestParams();
        params.put("phone", phone);
        // get方式
        HttpUtils.post(Constants.SEND_USER_FINDPWD, RetrievePasswordActvity.this, params, new AsyncHttpResponseHandler() {
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
