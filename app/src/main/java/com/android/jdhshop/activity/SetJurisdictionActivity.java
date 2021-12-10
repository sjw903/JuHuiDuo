package com.android.jdhshop.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.juduohui.response.HttpJsonResponse;
import com.loopj.android.http.RequestParams;
import com.suke.widget.SwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class SetJurisdictionActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.cop_switchbutton)
    SwitchButton cop_switchbutton;
    @BindView(R.id.tv_ago_notifition)
    SwitchButton tv_ago_notifition;
    @BindView(R.id.tv_pt_notifition)
    SwitchButton tv_pt_notifition;
    @BindView(R.id.tv_wxgzh_notifition)
    SwitchButton tv_wxgzh_notifition;

    static int REQUEST_PERMISSION_AGO_NOTIFICATION = 8341;
    static int REQUEST_PERMISSION_PT_NOTIFICATION = 8342;

    static int UPDATE_PT_NOTIFICATION = 0;
    static int UPDATE_WX_NOTIFICATION = 1;

    static int STATUS_OPEN = 1;
    static int STATUS_CLOSE = 2;

    private boolean kg;
    @Override
    protected void initUI() {
        setContentView(R.layout.ac_set_jurisdiction);
        ButterKnife.bind(this);
        tv_left.setVisibility(View.VISIBLE);
        tv_title.setText("权限设置");
    }

    @Override
    protected void initData() {
        // 是否开启剪切板监听
        String kg = SPUtils.getStringData(this, "read_clipboard", "true");
        if(kg.equals("true")){
            cop_switchbutton.setChecked(true);
        }else if(kg.equals("false")){
            cop_switchbutton.setChecked(false);
        }

        boolean isEnable = NotificationManagerCompat.from(SetJurisdictionActivity.this).areNotificationsEnabled();
        // 常驻通知判断状态

        if (isEnable){
            if ("true".equals(SPUtils.getStringData(this, "ago_notification", "true"))) {
                tv_ago_notifition.setChecked(true);
            }
            else{
                tv_ago_notifition.setChecked(false);
            }
        }
        else{
            tv_ago_notifition.setChecked(false);
        }

        if (CaiNiaoApplication.getUserInfoBean()!=null){
            tv_wxgzh_notifition.setChecked(CaiNiaoApplication.getUserInfoBean().user_detail.is_account == 1);

            if (isEnable){
                tv_pt_notifition.setChecked(CaiNiaoApplication.getUserInfoBean().user_detail.is_information == 1);
            }
            else{
                tv_pt_notifition.setChecked(false);
                if (CaiNiaoApplication.getUserInfoBean().user_detail.is_information==1){
                    SendNotificationStatus(UPDATE_PT_NOTIFICATION,STATUS_CLOSE);
                }
            }
        }
        else{
            SendNotificationStatus(UPDATE_WX_NOTIFICATION,STATUS_OPEN);
            tv_wxgzh_notifition.setChecked(true);

            if (isEnable){
                SendNotificationStatus(UPDATE_PT_NOTIFICATION,STATUS_OPEN);
                tv_pt_notifition.setChecked(true);
            }
            else{
                SendNotificationStatus(UPDATE_PT_NOTIFICATION,STATUS_CLOSE);
                tv_pt_notifition.setChecked(false);
            }
        }

    }

    @Override
    protected void initListener() {
        cop_switchbutton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (view.isChecked()){
                    cop_switchbutton.setChecked(true);
                    SPUtils.saveStringData(SetJurisdictionActivity.this,"read_clipboard",cop_switchbutton.isChecked()+"");
                }else{
                    cop_switchbutton.setChecked(false);
                    SPUtils.saveStringData(SetJurisdictionActivity.this,"read_clipboard",cop_switchbutton.isChecked()+"");
                }
            }
        });

        tv_ago_notifition.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                // Log.d(TAG, "onCheckedChanged: 当前状态：" + view.isChecked() +"," + isChecked);
                // Log.d(TAG, "onCheckedChanged: 服务是否运行：" + CaiNiaoApplication.isServiceRun(SetJurisdictionActivity.this, Constants.AGO_NOTIFICATION_SERVICE));
                SPUtils.saveStringData(CaiNiaoApplication.getAppContext(),"ago_notification",isChecked+"");
                if (isChecked) {
                    boolean isEnable = NotificationManagerCompat.from(SetJurisdictionActivity.this).areNotificationsEnabled();
                    if (!isEnable) {
                        //未打开通知
                        AlertDialog alertDialog = new AlertDialog.Builder(SetJurisdictionActivity.this)
                                .setTitle("提示")
                                .setCancelable(false)
                                .setMessage("“通知”中打开通知权限可以及时获取查看订单消息哦")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        tv_ago_notifition.setChecked(false);
                                        dialog.cancel();
                                    }
                                })
                                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        Intent intent = new Intent();
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                                            intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
                                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {  //5.0
                                            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                                            intent.putExtra("app_package", getPackageName());
                                            intent.putExtra("app_uid", getApplicationInfo().uid);
                                            startActivity(intent);
                                        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {  //4.4
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                                            intent.setData(Uri.parse("package:" + getPackageName()));
                                        } else if (Build.VERSION.SDK_INT >= 15) {
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                            intent.setData(Uri.fromParts("package", getPackageName(), null));
                                        }
//                                        startActivity(intent);
                                        startActivityForResult(intent, REQUEST_PERMISSION_AGO_NOTIFICATION);
                                    }
                                })
                                .create();
                        alertDialog.show();
                        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                    else{
                        CaiNiaoApplication.getInstances().startNotification();
                    }
                }
                else
                {
                    CaiNiaoApplication.getInstances().stopNotification();
                }
            }
        });

        tv_pt_notifition.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                SPUtils.saveStringData(CaiNiaoApplication.getAppContext(),"pt_notification",isChecked+"");
                if (isChecked) {
                    boolean isEnable = NotificationManagerCompat.from(SetJurisdictionActivity.this).areNotificationsEnabled();
                    if (!isEnable) {
                        //未打开通知权限
                        AlertDialog alertDialog = new AlertDialog.Builder(SetJurisdictionActivity.this)
                                .setTitle("提示")
                                .setCancelable(false)
                                .setMessage("“通知”中打开通知权限可以及时获取查看订单消息哦")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        tv_pt_notifition.setChecked(false);
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        Intent intent = new Intent();
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                                            intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
                                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {  //5.0
                                            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                                            intent.putExtra("app_package", getPackageName());
                                            intent.putExtra("app_uid", getApplicationInfo().uid);
                                            startActivity(intent);
                                        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {  //4.4
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                                            intent.setData(Uri.parse("package:" + getPackageName()));
                                        } else if (Build.VERSION.SDK_INT >= 15) {
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                            intent.setData(Uri.fromParts("package", getPackageName(), null));
                                        }
//                                        startActivity(intent);
                                        startActivityForResult(intent, REQUEST_PERMISSION_PT_NOTIFICATION);
                                    }
                                })
                                .create();
                        alertDialog.show();
                        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                    else{
                        SendNotificationStatus(UPDATE_PT_NOTIFICATION,STATUS_OPEN);
                    }
                }
                else
                {
                    SendNotificationStatus(UPDATE_PT_NOTIFICATION,STATUS_CLOSE);
                }
            }
        });

        tv_wxgzh_notifition.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                SPUtils.saveStringData(CaiNiaoApplication.getAppContext(),"wxgzh_notification",isChecked+"");
                if (isChecked){
                    SendNotificationStatus(UPDATE_WX_NOTIFICATION,STATUS_OPEN);
                }
                else
                {
                    SendNotificationStatus(UPDATE_WX_NOTIFICATION,STATUS_CLOSE);
                }
            }
        });
    }

    /**
     * 提交通知开关状态
     * @param type_of 0 普通通知 ，1微信通知
     * @param is_open 0 关闭 1开启
     */
    private void SendNotificationStatus(int type_of,int is_open){
        String request_url = type_of == 0 ?  Constants.UPDATE_PT_NOTIFICATION_STATUS : Constants.UPDATE_WX_NOTIFICATION_STATUS;
        RequestParams req = new RequestParams();
        req.put("state",is_open);

        HttpUtils.post(request_url, SetJurisdictionActivity.this,req, new HttpJsonResponse() {
            @Override
            protected void onSuccess(JSONObject response) {
                super.onSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        commonGetUserMsg();
    }

    @OnClick({R.id.tv_left, R.id.yisi, R.id.yisi1, R.id.yisi2, R.id.yisi3, R.id.yisi4, R.id.yisi5, R.id.yisi6,R.id.ai_weixinqun_genghuan,R.id.ai_weixinqun_genghuan1,R.id.ai_weixinqun_genghuan2,R.id.ai_weixinqun_genghuan3,R.id.ai_weixinqun_genghuan4,R.id.ai_weixinqun_genghuan5,R.id.ai_weixinqun_genghuan6})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.yisi:
            case R.id.yisi1:
            case R.id.yisi2:
            case R.id.yisi3:
            case R.id.yisi4:
            case R.id.yisi5:
            case R.id.yisi6:
                NewsActivity.actionStart(this, "33", "隐私政策");
                break;
            case R.id.ai_weixinqun_genghuan:
            case R.id.ai_weixinqun_genghuan1:
            case R.id.ai_weixinqun_genghuan2:
            case R.id.ai_weixinqun_genghuan3:
            case R.id.ai_weixinqun_genghuan4:
            case R.id.ai_weixinqun_genghuan5:
            case R.id.ai_weixinqun_genghuan6:
                getAppDetailSettingIntent(this);
                break;
        }
    }
    /**
     * 跳转到权限设置界面
     */
    public void getAppDetailSettingIntent(Context context){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT >= 9){
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if(Build.VERSION.SDK_INT <= 8){
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Log.d(TAG, "onActivityResult: requestCode = " + requestCode +", resultCode = " + resultCode +", data = " +data);
        if (requestCode == REQUEST_PERMISSION_AGO_NOTIFICATION){
            boolean isEnable = NotificationManagerCompat.from(SetJurisdictionActivity.this).areNotificationsEnabled();
            if (isEnable) {
                tv_ago_notifition.setChecked(true);
                CaiNiaoApplication.getInstances().startNotification();
            }
            else{
                tv_ago_notifition.setChecked(false);
            }
        }

        if (requestCode == REQUEST_PERMISSION_PT_NOTIFICATION){
            boolean isEnable = NotificationManagerCompat.from(SetJurisdictionActivity.this).areNotificationsEnabled();
            if (isEnable) {
                tv_pt_notifition.setChecked(true);
                SendNotificationStatus(UPDATE_PT_NOTIFICATION,STATUS_OPEN);
            }
            else{
                tv_pt_notifition.setChecked(false);
                SendNotificationStatus(UPDATE_PT_NOTIFICATION,STATUS_CLOSE);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
