package com.android.jdhshop.my;

import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.jdhshop.https.HttpUtils.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.login.LoginActivity;
import com.android.jdhshop.login.WelActivity;
import com.android.jdhshop.widget.AutoClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 密码修改
 * Created by lifuzhen on 2017/3/24.
 */
public class ResetPasswordActivity extends BaseActivity {
    public static final String TAG = "ResetPasswordActivity";
    //抬头
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.et_oldpsd)
    TextInputEditText et_oldpsd;//原密碼
    @BindView(R.id.et_newpsd_sure)//新密码
     TextInputEditText et_newpsd_sure;
    @BindView(R.id.et_newpsd_sure1)//新密码
     TextInputEditText et_newpsd_sure1;

    @BindView(R.id.tv_setpsd)//确认
    TextView tv_setpsd;
//    @BindView(R.id.tv_qq)//联系客服
//    TextView tv_qq;

    private ACache mAcache;
    String token;
    @Override
    protected void initUI() {
        setContentView(R.layout.ac_reset_password);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        mAcache = ACache.get(this);
        token=mAcache.getAsString(Constants.TOKEN);
        tv_left.setVisibility(View.VISIBLE);
        tv_title.setText("修改密码");
    }

    @Override
    protected void initListener() {
        //返回
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResetPasswordActivity.this.finish();
            }
        });
        //确认
        tv_setpsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String one=getTextEditValue(et_oldpsd);
                String two=getTextEditValue(et_newpsd_sure);
                String three=getTextEditValue(et_newpsd_sure1);
                if(TextUtils.isEmpty(one)){
                    showToast("请输入您的原密码");
                    return;
                }
                if(TextUtils.isEmpty(two)){
                    showToast("请输入6-18位数字、字母或符号的密码");
                    return;
                }
                if(TextUtils.isEmpty(three)){
                    showToast("请输入6-18位数字、字母或符号的密码");
                    return;
                }
                RequestParams params = new RequestParams();
                params.put("token", token);//原密码
                params.put("oldpwd", one);//原密码
                params.put("pwd1", two);//
                params.put("pwd2", three);//
                getDataPrivate(params);
            }
        });
//        tv_qq.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    //是修改查看绩效密码
    private void getDataPrivate(RequestParams params) {
        if (!CommonUtils.isNetworkAvailable()) {
            T.showShort(ResetPasswordActivity.this, getResources().getString(R.string.error_network));
            return;
        }

        HttpUtils.post(Constants.CHANGE_PWD,ResetPasswordActivity.this, params, new AsyncHttpResponseHandler() {

            //开始
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog("正在刷新...");
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
                   //返回码
                    int code = jsonObject.optInt("code");
                    //返回码说明
                    String msg = jsonObject.optString("msg");
                    if (0==code) {//成功
                        showToast(msg);
                        openActivity(WelActivity.class);
                        finish();
                    } else {//失败
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
                T.showShort(ResetPasswordActivity.this, error.getMessage());
            }
        });
    }

}



