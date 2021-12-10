package com.android.jdhshop.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.github.anzewei.parallaxbacklayout.ParallaxHelper;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.tencent.connect.UserInfo;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class WelActivity extends BaseActivity implements IUiListener {
    @BindView(R.id.tv_register)
    TextView tv_register;
    private ACache mAcache;
    private SendAuth.Req req;
    private Tencent tencent;
    public static Activity activity;
    @BindView(R.id.btn_one2)
    Button btn_one2;
    @BindView(R.id.wxloglog)
    ImageView wxloglog;
    public String edtext;
    private Button quxiao;
    @Override
    protected void initUI() {

        setContentView(R.layout.ac_welone);
        ButterKnife.bind(this);
        ParallaxHelper.disableParallaxBack(this);
        activity = this;
    }

    @Override
    protected void initData() {
        req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "hkx" + System.currentTimeMillis();
        req.transaction = "login";
        mAcache = ACache.get(this);


        Bitmap bitmap = getLoacalBitmap(Constants.ZIYUAN_PATH+"ic_launcher.png");
        Bitmap bitmap1 = getLoacalBitmap(Constants.ZIYUAN_PATH+"login_wechat_white.png");
        BitmapDrawable bd=new BitmapDrawable(bitmap1);
        wxloglog.setImageBitmap(bitmap);
        if (android.os.Build.VERSION.RELEASE.equals("10") || android.os.Build.VERSION.RELEASE.equals("11") ){
            bd.setBounds(0, 0, 50, 50);// 一定要设置setBounds();
            btn_one2.setCompoundDrawables(bd,null,null,null);
        }else{
            bd.setBounds(0, 0, bd.getMinimumWidth(), bd.getMinimumHeight());// 一定要设置setBounds();
            btn_one2.setCompoundDrawables(bd,null,null,null);
        }


        //wxloglog.setImageURI(imageContentUri);

    }
    /**
     * 加载本地图片
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if ("cancle".equals(SPUtils.getStringData(this, "wx_code", ""))) {
            T.showShort(this, "取消微信登录");
        } else if (!"".equals(SPUtils.getStringData(this, "wx_code", ""))) {
//            T.showShort(this,"微信code去获取openid");
            getOpenId(SPUtils.getStringData(this, "wx_code", ""));
        }
        SPUtils.saveStringData(this, "wx_code", "");
    }

    private void getOpenId(String code) {
        LogUtils.d("dsfasdf",Constants.WX_APP_ID);
        RequestParams params = new RequestParams();

        HttpUtils.get(WelActivity.this,"https://api.weixin.qq.com/sns/oauth2/access_token?appid="+Constants.WX_APP_ID+"&secret="+Constants.WX_APP_SECRET+"&code=" + code + "&grant_type=authorization_code", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                // Log.d(TAG, "getOpenId onSuccess: " + responseString);
                if (responseString.contains("errcode")) {
                    T.showShort(WelActivity.this, "授权失败");
                    return;
                }
                try {
                    JSONObject object = new JSONObject(responseString);
                    String openid = object.getString("openid");
                    String access_token = object.getString("access_token");
                    LogUtils.d("qqqqqq", access_token+"");
                    String unionid = object.getString("unionid");
//                    String nickname = object.optString("nickname");
//                    T.showShort(WelActivity.this, "授权失败");
                    object.optString("figureurl_qq_2");
                    checkIsBind("wx_app", object.getString("openid"), object.getString("access_token"),object.getString("unionid"));

//                    //更改为只能微信登录
//                    RequestParams params = new RequestParams();
//                    params.put("openid", openid);
//                    params.put("unionid", unionid);
//                    params.put("register_type", "1");
//                    params.put("auth_code",edtext);
//                    //T.showShort(WelActivity.this, inputServer+"");
//                    HttpUtils.post(Constants.weixLogin, params, new TextHttpResponseHandler() {
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                            LogUtils.e(TAG, "onFailure()--" + responseString);
//
//                        }
//
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                           try {
//                               JSONObject jsonObject = new JSONObject(responseString);
//                               //返回码
//                               int code = jsonObject.optInt("code");
//
//                               //返回码说明
//                              String msg = jsonObject.optString("msg");
//                              // T.showShort(WelActivity.this, msg+"");
//                               if(msg.equals("推荐人不存在")){
//                                   T.showShort(WelActivity.this, "推荐人不存在1");
//                               }else{
//                                   return;
//                               }
//
//                           }catch (JSONException e) {
//                               e.printStackTrace();
//                           }
//                        }
//                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    T.showShort(WelActivity.this, "授权失败");

                }
            }

            @Override
            public void onStart() {
                super.onStart();
            }
        });
    }
    private boolean is_new_user = true;
    private void checkIsBind(final String type, final String openId, final String access_token,final String union_id) {
        RequestParams params = new RequestParams();
        params.put("type", type);
        params.put("openid", openId);
        params.put("register_type",type);
        params.put("unionid",union_id);
        HttpUtils.post(Constants.IS_BIND,WelActivity.this, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // 输出错误信息
                LogUtils.e(TAG, "onFailure()--" + responseString);
            }

            @Override
            public void onFinish() {
                // 隐藏进度条
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                // Log.d(TAG, "checkIsBind onSuccess: " + responseString);
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    //返回码
                    int code = jsonObject.optInt("code");
                    try {
                        is_new_user = !"Z".equals(jsonObject.getJSONObject("data").getString("is_freeze"));
                    }
                    catch (Exception e){
                        is_new_user = true;
                    }

                    // Log.d(TAG, "onSuccess: " + is_new_user);
                    //返回码说明
                    String msg = jsonObject.optString("msg");
                    //Toast.makeText(WelActivity.activity, msg+"",Toast.LENGTH_SHORT).show();
                    if (0 == code && is_new_user) {
                        JSONObject jsonObject2 = jsonObject.getJSONObject("data");
                        String uid = jsonObject2.optString("uid");
                        String token = jsonObject2.optString("token");
                        mAcache.put(Constants.TOKEN, token);
                        SPUtils.saveStringData(WelActivity.this, Constants.TOKEN, token);
                        SPUtils.saveStringData(WelActivity.this, Constants.UID, uid);
                        SPUtils.saveStringData(WelActivity.this,"inviteCode",jsonObject2.getString("auth_code"));
                        SPUtils.saveStringData(WelActivity.this, "is", "1");
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        // Log.d(TAG, "onSuccess: go getAvatarInfo" + type+ ","+openId+ ","+ access_token+ ","+union_id);
                        getAvatarInfo(type, openId, access_token,union_id);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStart() {
                // 显示进度条
                super.onStart();
                showLoadingDialog();
            }
        });
    }

    private void postRegInfo(RequestParams requestParams){
        HttpUtils.post(Constants.weixLogin, WelActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // Log.d(TAG, "onFailure: " + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                // Log.d(TAG, "onSuccess: " + responseString);
                com.alibaba.fastjson.JSONObject response = com.alibaba.fastjson.JSONObject.parseObject(responseString);
                if (response.getIntValue("code") == 0){
                    String token = response.getString("token");
                    SPUtils.saveStringData(WelActivity.this,"token",token);
                    setResult(RESULT_OK);
                    finish();
                }
                else
                {
                    showToast(response.getString("msg"));
                }
            }
        });
    }

    private void getAvatarInfo(final String type, final String openId, final String access_token, final String union_id) {
        if ("qq".equals(type)) {
            UserInfo info = new UserInfo(this, tencent.getQQToken());
            info.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    final String nickname = ((JSONObject) o).optString("nickname");
                    final String headImg = ((JSONObject) o).optString("figureurl_qq_2");

//                    showTipDialog2("登录提示", Html.fromHtml("暂未绑定手机号"), new onClickListener() {
//                        @Override
//                        public void onClickSure() {
//                            Intent intent = new Intent(WelActivity.this, BindPhoneActivity.class);
//                            intent.putExtra("type", type);
//                            intent.putExtra("openid", openId);
//                            intent.putExtra("name", nickname);
//                            intent.putExtra("avatar", headImg);
//                            startActivity(intent);
//                        }
//                    }, "去绑定");
                    final EditText inputServer = new EditText(WelActivity.this);
                    AlertDialog.Builder builder = new AlertDialog.Builder(WelActivity.this);
                    builder.setTitle("绑定邀请码").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                            .setNegativeButton("跳过", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    btn_one2.performClick();
                                }
                            });

                    builder.setPositiveButton("绑定", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            inputServer.getText().toString();
                            try {
                                String openid = ((JSONObject) o).getString("openid");
                                String unionid = ((JSONObject) o).getString("unionid");

                                //更改为只能微信登录
                            RequestParams params = new RequestParams();
                            params.put("openid", openid);
                            params.put("unionid", unionid);
                            params.put("register_type",type);
                            params.put("auth_code", inputServer);
                            //T.showShort(WelActivity.this, inputServer+"");
                            HttpUtils.post(Constants.weixLogin,WelActivity.this, params, new TextHttpResponseHandler() {
                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    LogUtils.e(TAG, "onFailure()--" + responseString);

                                }

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(responseString);
                                        //返回码
                                        String code = jsonObject.optString("code");
                                        //返回码说明
                                        String msg = jsonObject.optString("msg");
                                        //T.showShort(WelActivity.this, msg+"");
//                                        if(msg.equals("推荐人不存在")){
//
//                                        }
                                        if(msg.equals("推荐人不存在")){
                                            T.showShort(WelActivity.this, "推荐人不存在2");
                                        }else{
                                            btn_one2.performClick();
                                        }

                                    }catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    builder.show();
                }

                @Override
                public void onError(UiError uiError) {
                }

                @Override
                public void onCancel() {
                }
            });

        } else {
            RequestParams params = new RequestParams();
            HttpUtils.get(WelActivity.this,"https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openId,params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onFinish() {
                    super.onFinish();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    if (responseString.contains("errcode")) {
                        T.showShort(WelActivity.this, "授权失败");
                        T.showShort(WelActivity.this, "333333");
                        return;
                    }
                    try {

                        final JSONObject object = new JSONObject(responseString);
                        final String nickname = ((JSONObject) object).getString("nickname");
                        final String avatar = ((JSONObject) object).getString("headimgurl");
                        final String openid = ((JSONObject) object).getString("openid");
                        final String unionid = ((JSONObject) object).getString("unionid");
                        final String sex = ((JSONObject) object).getString("sex");
                        final String province = ((JSONObject) object).getString("province");
                        final String city = ((JSONObject) object).getString("city");

                        //更改为只能微信登录
                        RequestParams params = new RequestParams();
                        params.put("openid", openid);
                        params.put("unionid", unionid);
                        params.put("register_type", "wx_app");
                        params.put("nickname", nickname);
                        params.put("avatar", avatar);
                        params.put("sex", sex);
                        params.put("province", province);
                        params.put("city", city);

                        if (!is_new_user){
                            postRegInfo(params);
                            return;
                        }


                        // 1.创建弹出式对话框
                        final AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(WelActivity.this);    // 系统默认Dialog没有输入框
                        // 获取自定义的布局
                        View alertDialogView = View.inflate(WelActivity.this, R.layout.dialog_yaoqingma, null);
                        final AlertDialog tempDialog = alertDialog.create();
                        tempDialog.setView(alertDialogView, 0, 0, 0, 0);
                        tempDialog.getWindow().setBackgroundDrawableResource(R.drawable.yuanjiao);
                        final EditText editText = (EditText)alertDialogView.findViewById(R.id.ed_message);
                        tempDialog.setCancelable(true);
                        tempDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        Button bind_button = alertDialogView.findViewById(R.id.positiveTextView);
                        Button skip_button = alertDialogView.findViewById(R.id.negativeTextView);

                        View.OnClickListener option_listener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Log.d(TAG, "onClick: " + (v.getId() == R.id.positiveTextView));
                                String auth_code = "";
                                if ((v.getId() == R.id.positiveTextView)) {
                                     auth_code = editText.getText().toString();
                                }
                                else{
                                    auth_code = "";
                                }
                                params.put("auth_code",auth_code);
                                postRegInfo(params);
                                tempDialog.dismiss();
                            }
                        };

                        bind_button.setOnClickListener(option_listener);
                        skip_button.setOnClickListener(option_listener);

                        tempDialog.show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        T.showShort(WelActivity.this, "授权失败");

                    }
                }

                @Override
                public void onStart() {
                    super.onStart();
                }
            });
        }
   }

    @Override
    protected void initListener() {
        //注册
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(RegisterActivity.class);
            }
        });
    }
    @Override
    public void onBack(View v) {

    }
    @OnClick({R.id.tv_login_wx,R.id.tv_login_phone,R.id.btn_one1,R.id.btn_one2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_one2:
            case R.id.tv_login_wx:
                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mm") == null) {
                    T.showShort(this, "请安装微信客户端");
                    return;
                }
                CaiNiaoApplication.api.sendReq(req);
                break;
            case R.id.btn_one1:
            case R.id.tv_login_phone:
                Intent login_intent = new Intent(this,LoginActivity.class);
                Intent old_intent = getIntent();
                Bundle bundle = null;
                if (old_intent!=null){
                    try {
                        bundle = old_intent.getExtras();
                        LogUtils.d(TAG, "onViewClicked: "+bundle);
                        LogUtils.d(TAG, "onViewClicked: " + bundle.getString("num_iid"));
                    }
                    catch (Exception e){
                    }
                }
                if (bundle != null) { login_intent.putExtras(bundle);}
                startActivity(login_intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, this);
    }


    /**
     * 腾讯授权回调
     *
     * @param o
     */
    @Override
    public void onComplete(Object o) {
        if (o.toString().contains("openid")) {
            try {
                JSONObject object = new JSONObject(o.toString());
                checkIsBind("wx_app", object.getString("openid"), object.getString("access_token"),"");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(UiError uiError) {
        T.showShort(this, "登录异常");
    }

    @Override
    public void onCancel() {
        T.showShort(this, "取消操作");
    }
}
