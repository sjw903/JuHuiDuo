package com.android.jdhshop.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jdhshop.activity.SetActivity;
import com.android.jdhshop.ziyuan.ResourceManager;
import com.android.jdhshop.https.HttpUtils.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.CropActivity;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.PddClient;
import com.android.jdhshop.bean.UserBean;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.utils.CheckUtil;
import com.android.jdhshop.widget.ImageSelectDialog;
import com.tencent.mm.opensdk.utils.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class BindPhoneActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_one)
    TextInputEditText etOne;
    @BindView(R.id.et_two)
    TextInputEditText etTwo;
    @BindView(R.id.btn_code)
    TextView btnCode;
    @BindView(R.id.tv_register)
    TextView tv_register;
    @BindView(R.id.et_four)
    TextInputEditText etFour;
    @BindView(R.id.ll_code)
    TextInputLayout llCode;
    @BindView(R.id.view_one)
    View viewOne;
    private TimeCount time;
    private ACache mAcache;

    @Override
    protected void initUI() {
        setContentView(R.layout.ac_bindphone);
        ButterKnife.bind(this);
        time = new TimeCount(60000, 1000);
        mAcache = ACache.get(this);
    }

    @Override
    protected void initData() {
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("???????????????");
        String phone = getIntent().getStringExtra("phone");
        etOne.setText(phone);
        String s = etOne.getText().toString();
        if (s.equals("")) {
            return;
        } else {
            showToast("??????????????????");

        }

        if ("N".equals(SPUtils.getStringData(getComeActivity(), "is_auth", "Y"))) {
            llCode.setHint("?????????????????????(??????)");
        } else {
            llCode.setHint("?????????????????????(??????)");
        }
        //tvTitle.setText("??????" + (getIntent().getStringExtra("type").equals("wx") ? "??????" : "QQ"));



    }

    @Override
    protected void initListener() {
    }

    @OnClick({R.id.tv_left, R.id.btn_code, R.id.tv_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_avater:
                new ImageSelectDialog(getComeActivity(), R.style.ActionSheetDialogStyle).setOnImageSelectDialogListener(new ImageSelectDialog.onImageSelectDialogListener() {
                    @Override
                    public void onImageSelectResult(String picturePath) {
                        if (picturePath == null || "".equals(picturePath))
                            return;
                        Intent intent = new Intent(getComeActivity(), CropActivity.class);
                        intent.putExtra("url", picturePath);
                        startActivityForResult(intent, 1000);
                    }
                }).show();
                break;
            case R.id.tv_left:
                finish();
                break;
            case R.id.btn_code:
                String phone = getTextEditValue(etOne);
                if (TextUtils.isEmpty(phone)) {
                    showToast("???????????????????????????");
                    return;
                }
                if (!CheckUtil.isMobile(etOne.getText().toString().trim())) {
                    T.showShort(this, "???????????????????????????");
                    return;
                }
                getData(phone);
                break;
            case R.id.tv_register:
                if (TextUtils.isEmpty(etOne.getText().toString().trim())) {
                    T.showShort(this, "?????????????????????");
                    return;
                }
                if (TextUtils.isEmpty(etTwo.getText().toString().trim())) {
                    T.showShort(this, "??????????????????");
                    return;
                }
                if ("N".equals(SPUtils.getStringData(getComeActivity(), "is_auth", "Y"))) {
                } else {
                    if (llCode.getVisibility() == View.VISIBLE && TextUtils.isEmpty(etFour.getText().toString().trim())) {
                        T.showShort(this, "??????????????????");
                        return;
                    }
                }
                bindPhone();
                break;
        }
    }

    private void bindPhone() {
        RequestParams params = new RequestParams();
        params.put("phone", etOne.getText().toString().trim());//?????????
        params.put("code", etTwo.getText().toString().trim());//?????????
        params.put("token", getIntent().getStringExtra("token"));

//        params.put("type", getIntent().getStringExtra("type"));
//        params.put("openid", getIntent().getStringExtra("openid"));
//        params.put("nickname", getIntent().getStringExtra("name"));
//        params.put("avatar", getIntent().getStringExtra("avatar"));
        if (llCode.getVisibility() == View.VISIBLE) {
            if (etFour.getText().toString().trim().length() > 0)
                if (etFour.getText().toString().trim().length() >= 7) {
                    if (CheckUtil.isMobile(etFour.getText().toString())) {
                        params.put("referrer_phone", etFour.getText().toString());//?????????
                    } else {
                        showToast("????????????????????????");
                        return;
                    }
                } else if (etFour.getText().toString().trim().length() <= 5) {
                    showToast("????????????????????????");
                    return;
                } else {
                    params.put("auth_code", etFour.getText().toString());//?????????
                }
        }
        if (!CommonUtils.isNetworkAvailable()) {
            showToast(getResources().getString(R.string.error_network));
            return;
        }
        LogUtils.d(TAG, params.toString());
        HttpUtils.post(Constants.BIND_Phone, BindPhoneActivity.this,params, new AsyncHttpResponseHandler() {

            //??????
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog("????????????...");
            }

            //??????
            @Override
            public void onFinish() {
                super.onFinish();
                closeLoadingDialog();
            }

            //??????
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseResult = new String(responseBody);
                LogUtils.d(TAG, responseResult.toString());
                try {
                    JSONObject jsonObject = new JSONObject(responseResult);
                    String code = jsonObject.optString("code");
                    String msg = jsonObject.optString("msg");
                    if ("0".equalsIgnoreCase(code)) {
                        showToast("????????????");
                        CaiNiaoApplication.commonGetUserMsg();
                        finish();

                        SPUtils.saveStringData(BindPhoneActivity.this, "phone", etOne.getText().toString());
                        mAcache.put(Constants.TOKEN, jsonObject.getJSONObject("data").getString("token"));
                        mAcache.put(Constants.GROUP_ID, "1");
                        mAcache.put(Constants.ACCOUT, etOne.getText().toString());
                        CaiNiaoApplication.setUserBean(new UserBean(jsonObject.getJSONObject("data").getString("uid"), "1", jsonObject.getJSONObject("data").getString("token")));

                        SPUtils.saveStringData(BindPhoneActivity.this, Constants.TOKEN, jsonObject.getJSONObject("data").getString("token"));
                        SPUtils.saveStringData(BindPhoneActivity.this, Constants.UID, jsonObject.getJSONObject("data").getString("uid"));
                        //WelActivity.activity.finish();

                        //WelActivity.activity = null;
                    } else if (msg.equals("???????????????")) {
                        showToast("?????????????????????");
                    } else {
                        showToast(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtils.d(TAG, e.toString());
                }
            }

            //??????
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                showToast(error.getMessage());
            }
        });
    }

    /**
     * ?????????????????????
     */
    private void getData(String phone) {
        if (!CommonUtils.isNetworkAvailable()) {
            showToast(getResources().getString(R.string.error_network));
            return;
        }
        String time1 = String.valueOf(System.currentTimeMillis() / 1000);
        RequestParams requestParams = new RequestParams();
        requestParams.put("data_type", PddClient.data_type);
        requestParams.put("version", PddClient.version);
        requestParams.put("timestamp", time1);
        requestParams.put("type", "hkx.sms");
        requestParams.put("phone", phone);
        Map<String, String> temp = new HashMap<>();
        temp.put("data_type", PddClient.data_type);
        temp.put("version", PddClient.version);
        temp.put("timestamp", time1);
        temp.put("type", "hkx.sms");
        temp.put("phone", phone);
        String sign = PddClient.getSign1(temp);
        requestParams.put("sign", sign);
        // get??????
        HttpUtils.post(Constants.GET_YZM_BIND,BindPhoneActivity.this, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                // ???????????????
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
                    if ("0".equalsIgnoreCase(code)) { // ????????????
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
                // ???????????????
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                // ??????????????????
                LogUtils.e(TAG, "onFailure()--" + arg3.toString());
            }
        });
        HttpUtils.post(Constants.APP_IP + "/api/UserAccount/checkPhone", BindPhoneActivity.this,requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                // ???????????????
                super.onStart();
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
                    if ("0".equalsIgnoreCase(code)) { // ????????????
                        if ("N".equals(jsonObject.getJSONObject("data").getString("is_register"))) {
                            //llCode.setVisibility(View.VISIBLE);
                            viewOne.setVisibility(View.VISIBLE);
                        }
                    } else {
                        showToast(msg);
                    }
                } catch (JSONException e) {
                    LogUtils.i(TAG, e.toString());
                }
            }

            @Override
            public void onFinish() {
                // ???????????????
                super.onFinish();
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                // ??????????????????
                LogUtils.e(TAG, "onFailure()--" + arg3.toString());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// ????????????????????????,????????????????????????
        }

        @SuppressLint("NewApi")
        @Override
        public void onTick(long millisUntilFinished) {// ??????????????????
            btnCode.setClickable(false);
            btnCode.setText("?????????" + millisUntilFinished / 1000 + getResources().getString(R.string.seconds_after));
            // btn_get_code.setTextSize(15);
//            btn_code.setTextColor(getResources().getColor(R.color.col_999));
//            btn_code.setBackground(getResources().getDrawable(R.drawable.bg_border_gray));
        }

        @SuppressLint("NewApi")
        @Override
        public void onFinish() {// ?????????????????????
            btnCode.setText(getResources().getString(R.string.get_verification_code));
            btnCode.setClickable(true);
        }

    }

}
