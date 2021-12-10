package com.android.jdhshop.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.android.jdhshop.MainActivity;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.base.LogcatHelper;
import com.android.jdhshop.bean.PddClient;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.VersionInformationBean;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.juduohui.CommonDialog;
import com.android.jdhshop.login.BindPhoneActivity;
import com.android.jdhshop.login.WelActivity;
import com.android.jdhshop.my.ResetPasswordActivity;
import com.android.jdhshop.my.ResetPhoneActivity;
import com.android.jdhshop.utils.APKVersionCodeUtils;
import com.android.jdhshop.utils.CleanMessageUtil;
import com.android.jdhshop.zip.CompressZip;
import com.google.gson.reflect.TypeToken;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.android.jdhshop.https.HttpUtils.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import me.drakeet.materialdialog.MaterialDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author wim
 * 设置页
 */
public class SetActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_tb)
    TextView tvTb;
    @BindView(R.id.xianshi_phone)
    TextView xianshi_phone;
    @BindView(R.id.tv_quanxian)
    TextView tv_quanxian;
    @BindView(R.id.tv_zhuxiao)
    TextView tv_zhuxiao;
    private ACache mAcache;
    private String token;
    private String phone;
    private MaterialDialog downDilog;
    private View downLayout;
    private ProgressBar downProgressBar;
    private UpdateService.Builder builder;
    private boolean isDownLoadFinish = false;
    private AlibcLogin alibcLogin;


    @Override
    protected void initUI() {
        setContentView(R.layout.activity_set);
        ButterKnife.bind(this);
        mMaterialDialog = new MaterialDialog(this);//初始化MaterialDialog
        downDilog = new MaterialDialog(this);
        downLayout = LayoutInflater.from(this).inflate(R.layout.down_layout, null);
        downProgressBar = downLayout.findViewById(R.id.pb_progressbar);
        downDilog.setView(downLayout);
        downDilog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void initData() {
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("设置");
        mAcache = ACache.get(this);
        token = SPUtils.getStringData(this, "token", "");
        phone = SPUtils.getStringData(this, "phone", "");
        xianshi_phone.setText(phone);

        tvTb.setTag("0");
        alibcLogin = AlibcLogin.getInstance();
        whetherBindingTaobao();

    }

    @Override
    protected void initListener() {

    }

    private void whetherBindingTaobao() {
        String url = Constants.WHETHER_BINDING_TAOBAO;
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder().add("token", token).build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String result = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    String isBinding = jsonData.optString("is_binding");
                    if ("Y".equals(isBinding)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvTb.setText("淘宝号解绑");
                                tvTb.setTag("1");
                            }
                        });
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    @OnClick({R.id.txt_w,R.id.txt_q,R.id.tv_tb,R.id.tv_left, R.id.tv_seven, R.id.tv_eight, R.id.tv_nine, R.id.tv_ten, R.id.tv_phone, R.id.tv_zfb,R.id.tv_bang_phone,R.id.tv_quanxian,R.id.tv_pushlog,R.id.tv_zhuxiao})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_bang_phone://绑定手机号功能
                Intent intent = new Intent(SetActivity.this, BindPhoneActivity.class);
                //intent.putExtra("type", "wx");
                intent.putExtra("token", token);
                intent.putExtra("phone", phone);
                startActivity(intent);
                break;
            case R.id.txt_w:
                NewsActivity.actionStart(this, "33", "隐私政策");
                break;
            case R.id.txt_q:
                NewsActivity.actionStart(this, "1", "用户协议");
                break;
            case R.id.tv_left:
                finish();
                break;
            case R.id.tv_tb:
                if ("1".equals(tvTb.getTag().toString())) {
                    //解绑操作
                    CommonDialog.getInstance(getComeActivity()).setTitle("聚多惠").setMessage("您确定要解绑淘宝账号吗？解除后购买的淘宝订单将得不到返利，若淘宝账号绑定错误需要进行换绑可进行此操作。")
                            .setSubmit("确定解绑",false)
                            .setCancelButton("取消",true)
                            .setListener(new CommonDialog.CommonDialogListener() {
                                @Override
                                public void OnSubmit(AlertDialog dialog) {
                                    unBindTb();
                                    dialog.dismiss();
                                }

                                @Override
                                public void OnCancel(AlertDialog dialog) {
                                    dialog.dismiss();
                                }

                                @Override
                                public void OnDismiss() {

                                }

                                @Override
                                public void OnClose(AlertDialog dialog) {

                                }
                            }).show();
                    //解绑操作
//                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setTitle(getString(R.string.app_name));
//                    builder.setMessage("您确定要解绑淘宝账号吗？解除后购买的淘宝订单将得不到返利，若淘宝账号绑定错误需要进行换绑可进行此操作。");
//                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    builder.setPositiveButton("确定解绑", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            unBindTb();
//                            dialog.dismiss();
//                        }
//                    });
//                    builder.show();
                } else {
                    //绑定操作
                    CommonDialog.getInstance(getComeActivity()).setTitle("聚多惠").setMessage("一个" + getString(R.string.app_name) + "账号只可绑定一个淘宝账号，通过绑定的淘宝账号购物可得到返利，其他淘宝账号无法获得返利\n")
                            .setSubmit("去绑定",true)
                            .setCancelButton("取消",false)
                            .setListener(new CommonDialog.CommonDialogListener() {
                                @Override
                                public void OnSubmit(AlertDialog dialog) {
                                    bingTb();
                                    dialog.dismiss();
                                }

                                @Override
                                public void OnCancel(AlertDialog dialog) {
                                    dialog.dismiss();
                                }

                                @Override
                                public void OnDismiss() {

                                }

                                @Override
                                public void OnClose(AlertDialog dialog) {

                                }
                            }).show();


//                    //绑定操作
//                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setTitle(getString(R.string.app_name));
//                    builder.setMessage("一个" + getString(R.string.app_name) + "账号只可绑定一个淘宝账号，通过绑定的淘宝账号购物可得到返利，其他淘宝账号无法获得返利\n");
//                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            bingTb();
//                            dialog.dismiss();
//                        }
//                    });
//                    builder.show();
                }
                break;
            case R.id.tv_zfb:
//                openActivity(BindActivity.class);
                Bundle bb = new Bundle();
                bb.putString("url","./profit_set_alipay.html");
                openActivity(JuDuoHuiActivity.class,bb);

                break;
            case R.id.tv_seven:
                openActivity(ResetPasswordActivity.class);
                break;
            case R.id.tv_phone:
                openActivity(ResetPhoneActivity.class);
                break;
            case R.id.tv_eight:
                try {
                    showClearDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_nine:
                ArticleRequest();
                break;
            case R.id.tv_ten:
                getData();
                break;
            case R.id.tv_quanxian:
                openActivity(SetJurisdictionActivity.class);
                break;
            case R.id.tv_pushlog://提交log信息
                LogcatHelper.getInstance(SetActivity.this).stop();
                LogcatHelper.getInstance(this).start();
                PushLog();//一键提交log信息
                break;
            case R.id.tv_zhuxiao: // 注销账户
                CommonDialog commonDialog = new CommonDialog(this);
                LinearLayout message_layout = commonDialog.getMessageCenter();
                message_layout.removeAllViews();
                View v = LayoutInflater.from(this).inflate(R.layout.common_dialog_zhuxiao,null);
                TextView txt_agreement = v.findViewById(R.id.txt_agreement);
                CheckBox gouxuanrd = v.findViewById(R.id.gouxuanrd);
                LinearLayout agree_box = v.findViewById(R.id.www);
                message_layout.addView(v);
                txt_agreement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewsActivity.actionStart(SetActivity.this, "73", getString(R.string.zhuxiao_agree_text));
                    }
                });
                commonDialog.setTitle("账户注销").setSubmit("取消注销",true)
                        .setCancelButton("确认注销",false)
                        .setListener(new CommonDialog.CommonDialogListener() {
                            @Override
                            public void OnSubmit(AlertDialog dialog) {
                                dialog.dismiss();
                            }

                            @Override
                            public void OnCancel(AlertDialog dialog) {
                                if (!gouxuanrd.isChecked()){
                                    agree_box.startAnimation(AnimationUtils.loadAnimation(SetActivity.this, R.anim.transla_checkbox));
                                    Toast.makeText(SetActivity.this, "请勾选"+getString(R.string.zhuxiao_agree_text), Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    runOnUiThread(()->{
                                        showLoadingDialog("正在注销请稍后...");
                                        unRegUser();
                                    });
                                }
                            }

                            @Override
                            public void OnDismiss() {

                            }

                            @Override
                            public void OnClose(AlertDialog dialog) {

                            }
                        });
                commonDialog.show();
                break;
        }
    }

    public boolean isCommonUse = false; // 是否为公用调用。
    public DialogActivity3.PushLogCallBack pushLogCallBack;
    public void PushLog(){
        //String tmp_zip_file_name = System.currentTimeMillis() +".zip";
       // BaseNewZip.ZipFolder(Environment.getExternalStorageDirectory() + "/com.android.jdhshop/user.log",Environment.getExternalStorageDirectory() + "/com.android.jdhshop/user_log");//压缩log文件
//        CompressZip.ZipFolder( "/sdcard/com.android.jdhshop/userLog.txt", "/sdcard/com.android.jdhshop/user_log.zip");
        CompressZip.ZipFolder( "/sdcard/com.android.jdhshop/logs", "/sdcard/com.android.jdhshop/user_log.zip");

            //获取加密数据接口
            String file_name="/sdcard/com.android.jdhshop/user_log.zip";
            LogUtils.d(TAG, "GetJIaMi: " + file_name);
            RequestParams params = new RequestParams();
            params.put("file_name", file_name);
            params.put("oss_path_name", "userLog");
            params.put("channel_type", "2");
            HttpUtils.post(Constants.getHUOQUJIAMI,SetActivity.this, params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    if(!isCommonUse) showToast("提交失败");
                    else pushLogCallBack.OptionResult(false,"数据签名接口失败");
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseString);
                        LogUtils.d("ssssss", jsonObject + "");
                        String msg = jsonObject.getString("msg");
                        String code = jsonObject.getString("code");
                        JSONObject data = jsonObject.getJSONObject("data");
                        String accessid = data.getString("accessid");
                        String host = data.getString("host");
                        String policy = data.getString("policy");
                        String signature = data.getString("signature");
                        //String expire = data.getString("expire");
                        String callback = data.getString("callback");
                        String dir = data.getString("dir");
                        String pic_name = data.getString("pic_name");

                        LogUtils.d(TAG, "onSuccess:  " + dir + pic_name + "");

                        if (code.equals("0")) {
                            try {
                                String key_file = dir + "/" + pic_name;
//                            key_file = key_file.replace("/","//");
                                RequestParams params = new RequestParams();
                                params.put("OSSAccessKeyId", accessid + "");
                                params.put("callback", callback + "");
                                params.put("key", key_file);
                                params.put("policy", policy + "");
                                params.put("signature", signature + "");
                                params.put("success_action_status","200");

                                File f = new File(file_name);
                                params.put("file",f.getName()  , f);//给文件名加入时间戳
                                LogUtils.d(TAG, "----OSS" + params+f.getName()+System.currentTimeMillis() );
                                HttpUtils.postUpload(host, SetActivity.this,params, new TextHttpResponseHandler() {
                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        LogUtils.d("eeeeeeeeee", responseString + "");
                                    }

                                    @Override
                                    public void onStart() {
                                        super.onStart();
                                        if(!isCommonUse) showLoadingDialog();
                                    }

                                    @Override
                                    public void onFinish() {
                                        super.onFinish();
                                        if(!isCommonUse) closeLoadingDialog();
                                    }

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                        LogUtils.d(TAG, "onSuccess: " + responseString);
                                        try {
                                            JSONObject jsonObject = new JSONObject(responseString);
                                            int code = jsonObject.optInt("code");
                                            LogUtils.d("code", statusCode+"--------"+responseString+"");
                                            String msg = jsonObject.optString("msg");
                                            String url = jsonObject.optString("url");
                                            LogUtils.d("urlurlurl", url);
                                            RequestParams params = new RequestParams();
                                            params.put("url",url);
                                            HttpUtils.post(Constants.PUSHUSERLOG, SetActivity.this,params, new TextHttpResponseHandler() {
                                                @Override
                                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                                    if(!isCommonUse) showToast("提交失败");
                                                    else pushLogCallBack.OptionResult(false,"数据接口有问题。");
                                                }

                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(responseString);
                                                        String msg = jsonObject.getString("msg");
                                                        int code = jsonObject.getInt("code");
                                                        if (code == 0) {
                                                            if(!isCommonUse) showToast("提交成功");
                                                            else pushLogCallBack.OptionResult(true,"错误日志提报成功");
                                                            LogUtils.d(TAG, "onSuccess: 提交成功");

                                                        } else {
                                                            if(!isCommonUse) showToast("提交失败");
                                                            else pushLogCallBack.OptionResult(false,"数据接口处理失败");
                                                            LogUtils.d(TAG, "onSuccess: 提交失败");
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        if(!isCommonUse) showToast("提交失败");
                                                        else pushLogCallBack.OptionResult(false,"数据接口处理失败！");
                                                    }
                                                }
                                            });

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            if(!isCommonUse) showToast("提交失败");
                                            else pushLogCallBack.OptionResult(false,"接口返回数据解析失败");
                                        }

                                    }
                                });
                            } catch (Exception e) {
//                Toast.makeText(this, "上传失败", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                                if(!isCommonUse) showToast("提交失败");
                                else pushLogCallBack.OptionResult(false,"OSS接口处理失败");
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if(!isCommonUse) showToast("提交失败");
                        else pushLogCallBack.OptionResult(false,"签名接口数据处理失败");
                    }
                }
            });

    }

    private void unBindTb() {
        RequestParams params = new RequestParams();
        params.put("token", token);
        // get方式
        HttpUtils.post(Constants.UNBING_TAOBAO,SetActivity.this, params, new AsyncHttpResponseHandler() {
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
                        showToast("解绑成功");
                        unbindLd();
                        tvTb.setTag("0");
                        tvTb.setText("绑定淘宝号");
                        alibcLogin.logout(new AlibcLoginCallback() {
                            @Override
                            public void onSuccess(int i, String s, String s1) {

                            }

                            @Override
                            public void onFailure(int i, String s) {

                            }
                        });
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
            }
        });
    }

    private void bingTb() {
        //如果未绑定
//        alibcLogin.showLogin( new AlibcLoginCallback() {
//            @Override
//            public void onSuccess(int i, String s, String s1) {
//                s=alibcLogin.getSession().userid;
//                if (s == null) {
//                    return;
//                }
//                int length = s.length();
//                if (length > 6) {
//                    String b = s.substring(length - 6, length);
//                    String[] bArr = b.split("");
//                    String c = bArr[1] + "" + bArr[2] + "" + bArr[5] + "" + bArr[6] + "" + bArr[3] + "" + bArr[4];
//                    bindingTaobao(c);
//                }
//            }
//            @Override
//            public void onFailure(int code, String msg) {
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(SetActivity.this, msg,
//                                Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//        });


        if (alibcLogin.isLogin()) {
            LogUtils.d(TAG, "run: 已经登陆了");
            alibcLogin.logout(new AlibcLoginCallback() {
                @Override
                public void onSuccess(int i, String s, String s1) {
                    runOnUiThread(() -> {
                        alibcLogin.showLogin(new AlibcLoginCallback() {
                            @Override
                            public void onSuccess(int i, String s, String s1) {
                                s = alibcLogin.getSession().userid;
                                if (s == null) {
                                    return;
                                }
                                int length = s.length();
                                if (length > 6) {
                                    String b = s.substring(length - 6, length);
                                    String[] bArr = b.split("");
                                    String c = bArr[1] + "" + bArr[2] + "" + bArr[5] + "" + bArr[6] + "" + bArr[3] + "" + bArr[4];
                                    bindingTaobao(c);
//                                    int i=0;
                                }
                            }

                            @Override
                            public void onFailure(int code, String msg) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SetActivity.this, msg,
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
                    });
                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
        } else {
            alibcLogin.showLogin(new AlibcLoginCallback() {
                @Override
                public void onSuccess(int i, String s, String s1) {
                    s = alibcLogin.getSession().userid;
                    if (s == null) {
                        return;
                    }
                    int length = s.length();
                    if (length > 6) {
                        String b = s.substring(length - 6, length);
                        String[] bArr = b.split("");
                        String c = bArr[1] + "" + bArr[2] + "" + bArr[5] + "" + bArr[6] + "" + bArr[3] + "" + bArr[4];
                        bindingTaobao(c);
//                                    int i=0;
                    }
                }

                @Override
                public void onFailure(int code, String msg) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SetActivity.this, msg,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!"0".equals(SPUtils.getStringData(this, "coded", "0"))) {
            getToken(SPUtils.getStringData(this, "coded", "0"));
            SPUtils.saveStringData(this, "coded", "0");
        }
    }

    //绑定淘宝账号
    private void bindingTaobao(String tb_uid) {
        String url = Constants.BINDING_TAOBAO;
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder().add("token", token).add("tb_uid", tb_uid).build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String result = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if ("0".equals(code)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(SetActivity.this, "绑定淘宝账号成功",
//                                        Toast.LENGTH_LONG).show();
                                bingRld();
                                tvTb.setTag("1");
                                tvTb.setText("淘宝号解绑");
                            }
                        });
                    } else {
                        // 重复绑定
                        final String errorMsg = jsonObject.getString("msg");
                        if ("12".equals(code)) {
                            runOnUiThread(() -> {
                                showTipDialog3("淘宝账号重复绑定", (String) errorMsg, null, null, "知道了", null);
                            });

                        } else {

                            runOnUiThread(() -> {
                                Toast.makeText(SetActivity.this, errorMsg,
                                        Toast.LENGTH_LONG).show();
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void bingRld() {
        openActivity(WebViewActivity4.class);
//        Intent intent = new Intent(this, WebViewActivity.class);
//        intent.putExtra("title","绑定淘宝分享渠道");
//        intent.putExtra("url","https://oauth.taobao.com/authorize?response_type=code&client_id="+Constants.qd_app_key+"&redirect_uri=http://127.0.0.1:12345/error&state=1212&view=wap");
//        startActivity(intent);
    }

    private void getToken(String code) {
        String time = new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(new Date());
        RequestParams requestParams = new RequestParams();
        requestParams.put("method", "taobao.top.auth.token.create");
        requestParams.put("app_key", Constants.qd_app_key);
        requestParams.put("format", "json");
        requestParams.put("code", code);
        requestParams.put("timestamp", time);
        requestParams.put("v", "2.0");
        requestParams.put("sign_method", "md5");
        Map<String, String> temp = new HashMap<>();
        temp.put("method", "taobao.top.auth.token.create");
        temp.put("app_key", Constants.qd_app_key);
        temp.put("format", "json");
        temp.put("code", code);
        temp.put("timestamp", time);
        temp.put("v", "2.0");
        temp.put("sign_method", "md5");
        String sign = null;
        sign = PddClient.getSign3(temp, Constants.qd_app_secret);
        requestParams.put("sign", sign);
        HttpUtils.post1("https://eco.taobao.com/router/rest",SetActivity.this, requestParams, new TextHttpResponseHandler() {
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
                String temp = responseString.replaceAll("\\\\", "");
                temp = temp.replace("\"{", "{").replace("}\"", "}");
                try {
                    JSONObject object = new JSONObject(temp);
                    object = object.getJSONObject("top_auth_token_create_response");
                    temp = object.getString("token_result");
                    object = new JSONObject(temp);
                    getTemp(object.getString("refresh_token"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getTemp(String key) {
        String time = new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(new Date());
        RequestParams requestParams = new RequestParams();
        requestParams.put("method", "taobao.tbk.sc.publisher.info.save");
        requestParams.put("app_key", Constants.qd_app_key);
        requestParams.put("format", "json");
        requestParams.put("session", key);
        requestParams.put("inviter_code", Constants.qd_app_code);
        requestParams.put("timestamp", time);
        requestParams.put("info_type", "1");
        requestParams.put("v", "2.0");
        requestParams.put("sign_method", "md5");
        Map<String, String> temp = new HashMap<>();
        temp.put("method", "taobao.tbk.sc.publisher.info.save");
        temp.put("app_key", Constants.qd_app_key);
        temp.put("format", "json");
        temp.put("inviter_code", Constants.qd_app_code);
        temp.put("session", key);
        temp.put("info_type", "1");
        temp.put("timestamp", time);
        temp.put("v", "2.0");
        temp.put("sign_method", "md5");
        String sign = null;
        sign = PddClient.getSign3(temp, Constants.qd_app_secret);
        requestParams.put("sign", sign);
        HttpUtils.post1("https://eco.taobao.com/router/rest",SetActivity.this, requestParams, new TextHttpResponseHandler() {
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
                if (!responseString.contains("error_response")) {
                    try {
                        JSONObject object = new JSONObject(responseString);
                        bindLd(object.getJSONObject("tbk_sc_publisher_info_save_response").getJSONObject("data").getString("relation_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject object = new JSONObject(responseString);
                        ToastUtils.showShortToast(SetActivity.this, object.getJSONObject("error_response").getString("sub_msg") + "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void unbindLd() {
        RequestParams params = new RequestParams();
        HttpUtils.post(Constants.UNBIND_RELATION_ID,SetActivity.this, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                String responseResult = new String(arg2);
                try {
                    JSONObject jsonObject = new JSONObject(responseResult);
                    String code = jsonObject.optString("code");
                    String msg = jsonObject.optString("msg");
                    // String result = jsonObject.optString("result");
                    if ("0".equalsIgnoreCase(code)) { // 操作成功
//                        showToast("解除绑定成功");
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
            }
        });
    }

    private void bindLd(String relation_id) {
        RequestParams params = new RequestParams();
        params.put("tb_rid", relation_id);
        HttpUtils.post(Constants.BIND_RELATION_ID,SetActivity.this, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
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
                        showToast("开通绑定成功");
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
            }
        });
    }

    /**
     * @属性:清除缓存对话框
     * @开发者:陈飞
     * @时间:2018/7/28 23:37
     */
    public void showClearDialog() throws Exception {

        CommonDialog.getInstance(getComeActivity()).setTitle("清理缓存").setMessage("<p>当前缓存为" + CleanMessageUtil.getTotalCacheSize(getComeActivity()) + "<br>确认要清除缓存吗？</p>")
                .setSubmit("清理",false)
                .setCancelButton("暂不清理",true)
                .setListener(new CommonDialog.CommonDialogListener() {
                    @Override
                    public void OnSubmit(AlertDialog dialog) {
                        CleanMessageUtil.clearAllCache(getComeActivity());
                        showToast("清除成功");
                        dialog.dismiss();
                    }

                    @Override
                    public void OnCancel(AlertDialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void OnDismiss() {

                    }

                    @Override
                    public void OnClose(AlertDialog dialog) {

                    }
                }).show();

//        if (mMaterialDialog == null) {
//            mMaterialDialog = new MaterialDialog(getComeActivity());//实例化对话框
//        }
//        mMaterialDialog.setTitle("清理缓存")
//                .setMessage(Html.fromHtml("<p>当前缓存为" + CleanMessageUtil.getTotalCacheSize(getComeActivity()) + "<br>确认要清除缓存吗？</p>"))
//                .setPositiveButton("是", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        CleanMessageUtil.clearAllCache(getComeActivity());
//                        showToast("清除成功");
//                        mMaterialDialog.dismiss();
//                    }
//                }).setNegativeButton("否", new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                if (null != mMaterialDialog) {
//                    mMaterialDialog.dismiss();
//                }
//            }
//        })
//                .setCanceledOnTouchOutside(true)
//                .setOnDismissListener(
//                        new DialogInterface.OnDismissListener() {
//                            @Override
//                            public void onDismiss(DialogInterface dialog) {
//                                mMaterialDialog = null;
//                            }
//                        })
//                .show();
    }

    /**
     * @属性:获取版本信息
     * @开发者:陈飞
     * @时间:2018/7/21 17:14
     */
    private void ArticleRequest() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.MESSAGE_ARTICLE_VERSION_URL,SetActivity.this, requestParams, new onOKJsonHttpResponseHandler<VersionInformationBean>(new TypeToken<Response<VersionInformationBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
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
            public void onSuccess(int statusCode, Response<VersionInformationBean> datas) {
                if (datas.isSuccess()) {
                    final VersionInformationBean data = datas.getData();

                    if (data != null) {
                        //对比当前版本
                        if (CommonUtils.compareVersion(APKVersionCodeUtils.getVerName(getComeActivity()), data.getVersion()) == -1) {
                            AllenVersionChecker
                                    .getInstance()
                                    .downloadOnly(
                                            UIData.create().setTitle(getString(R.string.app_name)).setContent("发现新版本\n" + data.getContent()).setDownloadUrl(data.getDown_android())
                                    ).setSilentDownload(false).setShowNotification(true).setForceRedownload(true).executeMission(getComeActivity());

                        } else {
//                            CommonDialog.getInstance(getComeActivity()).setTitle("版本信息").setMessage("当前版本已经是最新版啦<br>版本号：" + data.getVersion() + "</b>").setSubmit("知道啦",true).show();
                            CommonDialog commonDialog = new CommonDialog(getComeActivity());
                            commonDialog.getMessageContent().setVisibility(View.GONE);
                            View root_view = commonDialog.getRootView();
                            RelativeLayout need_display = root_view.findViewById(R.id.version_check);
                            need_display.setVisibility(View.VISIBLE);
                            TextView title = root_view.findViewById(R.id.version_title);
                            title.setText("版本信息");
                            TextView message = root_view.findViewById(R.id.version_message);
                            message.setText(Html.fromHtml("当前版本已经是最新版啦<br>版本号：" + data.getVersion() + "</b>"));
                            commonDialog.show();
//                            showTipDialog("版本信息", Html.fromHtml("当前版本已经是最新版啦<br>版本号：" + data.getVersion() + "</b>"));

                        }
                    }

                } else {
                    showToast(datas.getMsg());
                }
            }
        });
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 要做的事情
            downProgressBar.setProgress(builder.getUpdateProgress());
            if (builder.getUpdateProgress() >= 99) {
                isDownLoadFinish = true;
                UpdateService.Builder.updateProgress = 1;
                downDilog.dismiss();
            }
            if (builder.getUpdateProgress() == -1) {
                UpdateService.Builder.updateProgress = 1;
                downDilog.dismiss();
                isDownLoadFinish = true;
                T.showShort(getComeActivity(), "下载更新失败");
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public class MyThread implements Runnable {
        @Override
        public void run() {
            while (!isDownLoadFinish) {
                try {
                    Thread.sleep(500);
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 退出登录
     */
    private void getData() {
        if (!CommonUtils.isNetworkAvailable()) {
            showToast(getResources().getString(R.string.error_network));
            return;
        }
        RequestParams params = new RequestParams();
        params.put("token", token);
        HttpUtils.post(Constants.LOGIN_OUT,SetActivity.this, params, new TextHttpResponseHandler() {
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
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    //返回码
                    int code = jsonObject.optInt("code");
                    //返回码说明
                    String msg = jsonObject.optString("msg");
                    if (mAcache != null) {
                        if (0 == code) {
                            mAcache.put(Constants.TOKEN, "");
                            mAcache.put(Constants.GROUP_ID, "");
                            mAcache.put(Constants.ACCOUT, "");
                            mAcache.put(Constants.PASSWORD, "");
                            SPUtils.saveStringData(getComeActivity(), Constants.TOKEN, "");
//                        showToast(msg);

                        } else {
//                        showToast(msg);
                            mAcache.put(Constants.TOKEN, "");
                            mAcache.put(Constants.GROUP_ID, "");
                            mAcache.put(Constants.ACCOUT, "");
                            mAcache.put(Constants.PASSWORD, "");
                            SPUtils.saveStringData(getComeActivity(), Constants.TOKEN, "");
                        }
                    }
                    openActivity(MainActivity.class);
                    Intent intent =new Intent(SetActivity.this,WelActivity.class);
                    startActivity(intent);

                    finish();
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

    private void unRegUser(){
        if (!CommonUtils.isNetworkAvailable()) {
            closeLoadingDialog();
            showToast(getResources().getString(R.string.error_network));
            return;
        }
        RequestParams params = new RequestParams();
        params.put("token", token);
        HttpUtils.post(Constants.UNREGISTER, SetActivity.this,params, new TextHttpResponseHandler() {
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
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    //返回码
                    int code = jsonObject.optInt("code");
                    //返回码说明
                    String msg = jsonObject.optString("msg");
                    if (mAcache != null) {
                        if (0 == code) {
                            mAcache.put(Constants.TOKEN, "");
                            mAcache.put(Constants.GROUP_ID, "");
                            mAcache.put(Constants.ACCOUT, "");
                            mAcache.put(Constants.PASSWORD, "");
                            SPUtils.saveStringData(getComeActivity(), Constants.TOKEN, "");
                        } else {
                            showToast(msg);
                            return;
                        }
                    }
                    openActivity(MainActivity.class);
                    Intent intent =new Intent(SetActivity.this,WelActivity.class);
                    startActivity(intent);
                    finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.d(TAG, "onActivityResult: " + requestCode +"," +resultCode + "," + data);
        if (requestCode == 8888){
            if (resultCode == 1){
                unRegUser();
            }
        }
    }
}
