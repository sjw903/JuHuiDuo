package com.android.jdhshop.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.TextView;

import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.PromotionDetailsActivity;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.UserBean;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.utils.CheckUtil;
import com.android.jdhshop.ziyuan.ResourceManager;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cz.msebera.android.httpclient.Header;

public class LoginActivity extends BaseActivity  {
    @BindView(R.id.et_account)
    TextInputEditText et_account;
    @BindView(R.id.et_psd)
    TextInputEditText et_psd;
    @BindView(R.id.tv_forgotpsd)
    TextView tv_forgotpsd;
    @BindView(R.id.tv_login)
    TextView tv_login;
    private ACache mAcache;
    public static Activity activity;
    @Override
    protected void initUI() {
        setContentView(R.layout.ac_login);

        ButterKnife.bind(this);
        activity=this;
    }
    @Override
    protected void initData() {
        mAcache = ACache.get(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        et_account.setText(SPUtils.getStringData(this, "phone", ""));
        et_psd.setText(SPUtils.getStringData(this, "pwd", ""));
    }
    @Override
    protected void initListener() {

        Drawable drawable = ResourceManager.getInstance().getDrawable("bg_tv_circle_half_appmain", "drawable");
        if (drawable != null) {
            tv_login.setBackground(drawable);
        } else {
            // Log.d(TAG, "drawable is null");
        }
        //????????????
        tv_forgotpsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(RetrievePasswordActvity.class);
            }
        });
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = getTextEditValue(et_account);
                String psd = getTextEditValue(et_psd);
                getData(account, psd);
            }
        });
    }
    @Override
    public void onBack(View v) {

    }
    /**
     * ??????
     */
    private void getData(final String phone, final String psd) {
        if (!CommonUtils.isNetworkAvailable()) {
            showToast(getResources().getString(R.string.error_network));
            return;
        }
        if(!CheckUtil.isMobile(phone)){
            T.showShort(LoginActivity.this, "???????????????????????????");

            return;
        }
        RequestParams params = new RequestParams();
        params.put("account", phone);
//        final DesUtil desUtil = new DesUtil(DesUtil.SECURITY_PRIVATE_KEY);
//        params.put("password", desUtil.encrypt(pwd));//????????????
        params.put("pwd", psd);
        HttpUtils.post(Constants.LOGIN, LoginActivity.this,params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // ??????????????????
                LogUtils.e(TAG, "onFailure()--" + responseString);
            }

            @Override
            public void onFinish() {
                // ???????????????
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                LogUtils.e(TAG, "onSuccess()--" + responseString);
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    //?????????
                    int code = jsonObject.optInt("code");
                    //???????????????
                    String msg = jsonObject.optString("msg");
                    //??????ID
                    String uid = jsonObject.optString("uid");
                    //?????????ID
                    String group_id = jsonObject.optString("group_id");
                    //????????????????????????????????????????????????????????????????????????APP???????????????
                    String token = jsonObject.optString("token");
                    if (0 == code) {
                        SPUtils.saveStringData(LoginActivity.this, "phone", et_account.getText().toString());
                        SPUtils.saveStringData(LoginActivity.this, "pwd", et_psd.getText().toString());
                        mAcache.put(Constants.TOKEN, token);
                        mAcache.put(Constants.GROUP_ID, group_id);
                        mAcache.put(Constants.ACCOUT, phone);
                        mAcache.put(Constants.PASSWORD, psd);
                        SPUtils.saveStringData(LoginActivity.this, "token",token);
                        CaiNiaoApplication.setUserBean(new UserBean(uid, group_id, token));
                        commonGetUserMsg();
                        SPUtils.saveStringData(LoginActivity.this, Constants.TOKEN, token);
                        SPUtils.saveStringData(LoginActivity.this, Constants.UID, uid);
                        JPushInterface.setAlias(LoginActivity.this, uid,
                                new TagAliasCallback() {//????????????,i=0????????????,??????????????????
                                    @Override
                                    public void gotResult(int responseCode, String s, Set<String> set) {
                                        if (responseCode==0) {
//                                            System.out.println("jpush alias@@@@@??????????????????");
                                        }
                                    }
                                });
                        //???????????????????????????
                        // BroadcastManager.getInstance(IndexActivity.this).sendBroadcast(BroadcastContants.sendLoginMessage);
//                        Bundle bundle=new Bundle();
//                        bundle.putInt("type",3);
                        // openActivity(MainActivity.class);
                        SPUtils.saveStringData(LoginActivity.this, "is", "1");
                        WelActivity.activity.finish();
                        WelActivity.activity = null;

                        Intent back_intent =  getIntent();
                        if (back_intent!=null){
                            Bundle b = back_intent.getExtras();
                            if (b!=null){
                                String num_iid = b.getString("num_iid");
                                b.putString("num_iid",num_iid);
                                openActivity(PromotionDetailsActivity.class,b);
                            }
                        }
                        finish();
                    } else {
                        ToastUtils.showShortToast(LoginActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("???????????????");
                }
            }

            @Override
            public void onStart() {
                // ???????????????
                super.onStart();
                showLoadingDialog();
            }
        });

    }
    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

}
