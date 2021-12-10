package com.android.jdhshop.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.juduohui.AppResouceUpdate;
import com.android.jdhshop.utils.IsWifi;
import com.lljjcoder.style.citylist.Toast.ToastUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author wim
 */
public class DialogActivity3 extends Activity {
    private String TAG = getClass().getSimpleName();
    private RadioButton radioButton;
    private LinearLayout lll;
    private String wwww = "";
    private RadioGroup rdgrop;
    private long lastClick;
    private LinearLayout llllll;
    private LinearLayout llllll2;
    private LinearLayout llllll3;
    private int p = 0;//当前进度
    private ProgressBar progressBar;
    private TextView txt_cancle_tishi, txt_ok_tishi,jindu_text;
    @BindView(R.id.send_log)
    TextView send_log;
    @BindView(R.id.call_customer)
    TextView call_customer;
    @BindView(R.id.download_txt) TextView download_txt;
    @BindView(R.id.button_box) LinearLayout button_box;

    public interface PushLogCallBack{
        public void OptionResult(boolean result,String msg);
    }
    AppResouceUpdate.UpdateCallBack updateCallBack = new AppResouceUpdate.UpdateCallBack() {
        @Override
        public void success() {
            SPUtils.saveBoolean(DialogActivity3.this,"is_open_splash",true);
            setResult(RESULT_OK);
            finish();
        }
        @Override
        public void fail()
        {
            runOnUiThread(()->{
                LogUtils.d(TAG, "fail: ");
                download_txt.setText("资源文件下载失败！");
                progressBar.setVisibility(View.GONE);
                button_box.setVisibility(View.VISIBLE);
                setResult(RESULT_CANCELED);
                send_log.setVisibility(View.VISIBLE);
            });
        }
    };

    View.OnClickListener push_listen = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            send_log.setOnClickListener(null);
            send_log.setTextColor(Color.parseColor("#666666"));
            SetActivity s = new SetActivity();
            s.isCommonUse = true;
            s.pushLogCallBack = new PushLogCallBack() {
                @Override
                public void OptionResult(boolean result,String msg) {
                    runOnUiThread(()->{
                        if (result){
                            send_log.setText("日志已上报成功");
                        }
                        else{
                            send_log.setOnClickListener(push_listen);
                        }
                        ToastUtils.showLongToast(getApplicationContext(),msg);
                    });
                }
            };
            s.PushLog();
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_dialog3);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ButterKnife.bind(this);
        setFinishOnTouchOutside(false);
        radioButton = findViewById(R.id.gouxuanrd);
        llllll = findViewById(R.id.lllll);
        llllll2 = findViewById(R.id.lllll2);
        llllll3 = findViewById(R.id.lllll3);
        jindu_text = findViewById(R.id.jindu_text);
        txt_cancle_tishi = findViewById(R.id.txt_cancle_tishi);
        progressBar = findViewById(R.id.pb_progressbar1);
        txt_cancle_tishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rdgrop = findViewById(R.id.rdgrop);
        txt_ok_tishi = findViewById(R.id.txt_ok_tishi);
        txt_ok_tishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llllll.setVisibility(View.GONE);
                llllll3.setVisibility(View.GONE);
                if ("xm".equals(CaiNiaoApplication.getAppChannel()) || "hw".equals(CaiNiaoApplication.getAppChannel())){

                    AppResouceUpdate.checkUpdate(DialogActivity3.this, null, updateCallBack);
                }
                else {
                    llllll2.setVisibility(View.VISIBLE);

                    AppResouceUpdate.checkUpdate(DialogActivity3.this, progressBar, updateCallBack);
                }
            }
        });

        send_log.setOnClickListener(push_listen);

        call_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kf_intent = new Intent(getApplicationContext(), JuDuoHuiActivity.class);
                LogUtils.d(TAG, "onViewClicked: " + SPUtils.getStringData(getApplicationContext(),"kefu_url","https://jq.qq.com/?_wv=1027&k=orJGmf3a"));
                String custom_kefu = SPUtils.getStringData(getApplicationContext(),"kefu_url","https://jq.qq.com/?_wv=1027&k=orJGmf3a");
                kf_intent.putExtra("url", custom_kefu);
                kf_intent.putExtra("has_user",false);
                startActivity(kf_intent);
            }
        });


        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.d("eeeeeeeeeeee", radioButton.isChecked() + "");
                if (radioButton.isChecked()) {
                    if (wwww.equals("")) {
                        wwww = "true";
                        radioButton.setChecked(true);
                    } else if (wwww.equals("true")) {
                        radioButton.setChecked(false);
                        rdgrop.clearCheck();
                        wwww = "";
                    }
                } else {
                    rdgrop.clearCheck();
                }

            }
        });
        lll = findViewById(R.id.www);
        findViewById(R.id.txt_agreement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsActivity.actionStart(DialogActivity3.this, "1", "用户协议");
            }
        });
        findViewById(R.id.txt_agreement2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsActivity.actionStart(DialogActivity3.this, "33", "隐私政策");
            }
        });
        findViewById(R.id.txt_agreement3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsActivity.actionStart(DialogActivity3.this, "65", "第三方接入说明");
            }
        });
    }

    @OnClick({R.id.txt_cancle, R.id.txt_ok,})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.txt_cancle:
                setResult(RESULT_CANCELED, intent);
                finish();
                break;
            case R.id.txt_ok:
                if (radioButton.isChecked()) {
                    SPUtils.saveStringData(DialogActivity3.this, "ty", "true");
                    Log.d(TAG, "onViewClicked: 进这里");

                    myPermission();
                } else {
                    lll.startAnimation(AnimationUtils.loadAnimation(this, R.anim.transla_checkbox));
                    Toast.makeText(DialogActivity3.this, "请勾选用户协议和隐私政策", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public void myPermission() {
        Log.d(TAG, "myPermission: " + CaiNiaoApplication.getAppChannel());
        LogUtils.d("qqqqqqqq", "没权限" + CaiNiaoApplication.getAppChannel());
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user


            // 如果是华为小米渠道则不申请权限
            if ("xm".equals(CaiNiaoApplication.getAppChannel()) || "hw".equals(CaiNiaoApplication.getAppChannel()) ){
                Log.d(TAG, "myPermission: ");
                IsWIFI();
                return;
            }

            assistant2(getText(R.string.app_name) + "权限申请提醒", getText(R.string.app_name) + "需获取设备存储权限才可正常使用。", "", REQUEST_EXTERNAL_STORAGE, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ActivityCompat.requestPermissions(
                            DialogActivity3.this,
                            PERMISSIONS_STORAGE,
                            REQUEST_EXTERNAL_STORAGE
                    );
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SPUtils.saveStringData(DialogActivity3.this, "ty", "false");
                    System.exit(0);
                }
            });

//            AlertDialog.Builder diag_builder = new AlertDialog.Builder(DialogActivity3.this);
//            diag_builder.setTitle(getText(R.string.app_name)+",权限获取提醒");
//            diag_builder.setTitle(getText(R.string.app_name)+",需要获取设备存储权限方可正常运行,请确认是否同意授予获取权限.");
//            diag_builder.setPositiveButton("同意授予", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    SPUtils.saveStringData(DialogActivity3.this, "aaaa", "true");
//                    ActivityCompat.requestPermissions(
//                            DialogActivity3.this,
//                            PERMISSIONS_STORAGE,
//                            REQUEST_EXTERNAL_STORAGE
//                    );
//                }
//            });
//
//            diag_builder.setNeutralButton("取消退出", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    System.exit(0);
//                }
//            });
//
//            diag_builder.show();

        } else {
            try {
                CaiNiaoApplication.getInstances().initAppInfo();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            //判断文件是否存在
            boolean b = fileIsExists(Constants.ZIYUAN_PATH+"icon_life_g.png");
            if(b){
                LogUtils.d("tttttttt", "存在");
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }else{
                LogUtils.d("tttttttt", "不存在");
                IsWIFI();
            }
        }
    }

    //strFile 为文件名称 返回true为存在
    public boolean fileIsExists(String strFile) {
        try {
            File f=new File(strFile);
            if(f.exists()) {
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_EXTERNAL_STORAGE) {

            if (hasAllPermissionsGranted(grantResults)) {
                Log.i("qqqqqq", "用户允许打开权限");
                boolean b = fileIsExists(Constants.ZIYUAN_PATH+"icon_life_g.png");
                if(b){
                    LogUtils.d("tttttttt", "存在");
                    Intent intent = new Intent();
                    SPUtils.saveStringData(DialogActivity3.this, "ty", "true");
                    SPUtils.saveStringData(DialogActivity3.this, "aaaa", "true");
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    LogUtils.d("tttttttt", "不存在");
                    IsWIFI();
                }
            } else {
                assistant("权限自动申请被拒提醒",  "您请拒绝程序自动获取设备存储权限，请点击”立即设置“前往权限设置页面手动设置。", "");
                Log.i("qqqqqq", "用户拒绝打开权限");
                //showPermissionDialog("请去设置页面打开***权限", OPEN_SET_REQUEST_CODE);

            }

        }

    }

    private void IsWIFI() {
        boolean networkAvailable = IsWifi.isNetworkAvailable(DialogActivity3.this);
        if (networkAvailable == true) {
            Log.i("qqqqqq", "已连接网络");
            boolean wifiActive = IsWifi.isWifiActive(DialogActivity3.this);
            if (wifiActive == true) {
                Log.i("qqqqqq", "是无线网");
                // wifi 环境进行资源更新。

//                AppResouceUpdate.checkUpdate(DialogActivity3.this, progressBar, updateCallBack);
                // wifi 环境进行资源更新结束。

                llllll.setVisibility(View.GONE);
                if ("xm".equals(CaiNiaoApplication.getAppChannel()) || "hw".equals(CaiNiaoApplication.getAppChannel())){

                    llllll2.setVisibility(View.VISIBLE);
                    AppResouceUpdate.checkUpdate(DialogActivity3.this, progressBar, updateCallBack);
                    SPUtils.saveBoolean(DialogActivity3.this,"is_open_splash",true);
                    Toast.makeText(DialogActivity3.this, "程序正在初始化，请稍后", Toast.LENGTH_SHORT).show();

                }
                else {
                    llllll2.setVisibility(View.VISIBLE);
                    AppResouceUpdate.checkUpdate(DialogActivity3.this, progressBar, updateCallBack);
                    Toast.makeText(DialogActivity3.this, "检测到您用的是WIFI1,已为您自动下载资源包", Toast.LENGTH_SHORT).show();
                }



            } else {
                llllll.setVisibility(View.GONE);
                llllll2.setVisibility(View.GONE);
                llllll3.setVisibility(View.VISIBLE);
                Log.i("qqqqqq", "不是无线网");
            }
        } else {
            Log.i("qqqqqq", "未连接网络");
            Toast.makeText(DialogActivity3.this, "请连接网络再进入应用", Toast.LENGTH_SHORT).show();
        }
    }
    AlertDialog tempDialog;
    private void assistant2(String dia_title, String text1, String text2, int requestCode, DialogInterface.OnClickListener queren_listener, DialogInterface.OnClickListener cancel_listener) {
        // 1.创建弹出式对话框
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DialogActivity3.this);    // 系统默认Dialog没有输入框
        // 获取自定义的布局
        View alertDialogView = View.inflate(DialogActivity3.this, R.layout.dialog_quanxian, null);
        tempDialog = alertDialog.create();
        tempDialog.setView(alertDialogView, 0, 0, 0, 0);
        tempDialog.getWindow().setBackgroundDrawableResource(R.drawable.yuanjiao);
        final EditText editText = (EditText) alertDialogView.findViewById(R.id.ed_message);
        TextView title = (TextView) alertDialogView.findViewById(R.id.ai_assistant_dia_title);
        TextView textv1 = (TextView) alertDialogView.findViewById(R.id.ai_ass_text1);
        TextView textv2 = (TextView) alertDialogView.findViewById(R.id.ai_ass_text2);
        title.setText(dia_title);
        textv1.setText(text1);
        textv2.setText(text2);
        tempDialog.setCancelable(false);

        tempDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView queren = alertDialogView.findViewById(R.id.negativeTextView);
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queren_listener.onClick(tempDialog,0);
            }
        });
        TextView cancel_button = alertDialogView.findViewById(R.id.cancel_button);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel_listener.onClick(tempDialog,0);
            }
        });
        tempDialog.show();
    }

    private void assistant(String dia_title, String text1, String text2) {
        // 1.创建弹出式对话框
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DialogActivity3.this);    // 系统默认Dialog没有输入框
        // 获取自定义的布局
        View alertDialogView = View.inflate(DialogActivity3.this, R.layout.dialog_quanxian, null);
        tempDialog = alertDialog.create();
        tempDialog.setView(alertDialogView, 0, 0, 0, 0);
        tempDialog.getWindow().setBackgroundDrawableResource(R.drawable.yuanjiao);
        final EditText editText = (EditText) alertDialogView.findViewById(R.id.ed_message);
        TextView title = (TextView) alertDialogView.findViewById(R.id.ai_assistant_dia_title);
        TextView textv1 = (TextView) alertDialogView.findViewById(R.id.ai_ass_text1);
        TextView textv2 = (TextView) alertDialogView.findViewById(R.id.ai_ass_text2);
        title.setText(dia_title);
        textv1.setText(text1);
        textv2.setText(text2);
        tempDialog.setCancelable(false);

        tempDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView queren = alertDialogView.findViewById(R.id.negativeTextView);
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAppDetailSettingIntent(DialogActivity3.this, DialogActivity3.REQUEST_EXTERNAL_STORAGE);
                tempDialog.dismiss();
            }
        });
        TextView cancel_button = alertDialogView.findViewById(R.id.cancel_button);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.saveStringData(DialogActivity3.this, "ty", "false");
                System.exit(0);
//                tempDialog.dismiss();
            }
        });
        tempDialog.show();
    }

    /**
     * 跳转到权限设置界面
     */
    private void getAppDetailSettingIntent(Context context,int request_code) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
//        startActivity(intent);
        startActivityForResult(intent,request_code);
    }



    /**
     *     * 判断权限申请结果
     * <p>
     *     *
     * <p>
     *     * @param grantResults
     * <p>
     *     * @return
     * <p>
     *    
     */

    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {

        for (int grantResult : grantResults) {

            if (grantResult == PackageManager.PERMISSION_DENIED) {//PERMISSION_GRANTED 授予

                return false;

            }

        }

        return true;

    }
}
