package com.android.jdhshop;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.jdhshop.activity.JdDetailsActivity;
import com.android.jdhshop.activity.JuDuoHuiActivity;
import com.android.jdhshop.activity.MyMarketActivity;
import com.android.jdhshop.activity.NewShuanshierActivity;
import com.android.jdhshop.activity.NewsActivity;
import com.android.jdhshop.activity.PddActivity;
import com.android.jdhshop.activity.PddDetailsActivity;
import com.android.jdhshop.activity.PromotionDetailsActivity;
import com.android.jdhshop.activity.QdActivity;
import com.android.jdhshop.activity.SplashActivity;
import com.android.jdhshop.activity.WebViewActivity;
import com.android.jdhshop.activity.WebViewActivity2;
import com.android.jdhshop.advistion.entry.VideoContentFragment;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.base.RandomUntil;
import com.android.jdhshop.bean.MessageEvent;
import com.android.jdhshop.bean.MyGoodsResp;
import com.android.jdhshop.bean.PDDBean;
import com.android.jdhshop.bean.PddClient;
import com.android.jdhshop.bean.UserInfoBean;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.fragments.HomeFragment;
import com.android.jdhshop.fragments.JuDuoHuiFragment;
import com.android.jdhshop.fragments.MyFragmentNew;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.juduohui.AppResouceUpdate;
import com.android.jdhshop.juduohui.CommonDialog;
import com.android.jdhshop.juduohui.JuduohuiMainActivity;
import com.android.jdhshop.juduohui.NewsInformation;
import com.android.jdhshop.juduohui.message.JuduohuiCountInsertAdvMessage;
import com.android.jdhshop.juduohui.message.JuduohuiMessage;
import com.android.jdhshop.login.WelActivity;
import com.android.jdhshop.my.BalanceActivity;
import com.android.jdhshop.my.MyOrderActivity;
import com.android.jdhshop.my.MyShareUrlActivity;
import com.android.jdhshop.utils.OkHttpUtils;
import com.android.jdhshop.utils.UIUtils;
import com.android.jdhshop.widget.CaiNiaoRadioGroup;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.uuch.adlibrary.AdConstant;
import com.uuch.adlibrary.AdManager;
import com.uuch.adlibrary.bean.AdInfo;
import com.youth.banner.transformer.ZoomOutTranformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cz.msebera.android.httpclient.Header;
import me.panpf.sketch.SketchImageView;
import okhttp3.Call;
import okhttp3.Callback;

import static com.android.jdhshop.CaiNiaoApplication.isServiceRun;

//import com.loopj.android.http.BinaryHttpResponseHandler;

public class MainActivity extends BaseActivity {
    private FragmentManager fm;
    @BindView(R.id.rg)
    CaiNiaoRadioGroup rg;
    @BindView(R.id.rb_1)
    RadioButton rb_1;
    @BindView(R.id.rb_2)
    RadioButton rb_2;
    @BindView(R.id.rb_3)
    RadioButton rb_3;
    @BindView(R.id.rb_4)
    RadioButton rb_4;
    @BindView(R.id.rb_5)
    RadioButton rb_5;
    int type = 0;//登录成功后跳转到我的
    private ACache mAcache;
    private String token, phone, pwd;
    private List<AdInfo> adInfos = new ArrayList<>();
    AdManager adManager;
    private int currentAc = 0;
    private Fragment f1, f2, f3, f4, f5;
    FragmentTransaction ft;
    private Fragment currentFragment = new Fragment();
    int id = 0;
    private long lastClick;
    private final static String TAG = "MainActivity";
    private NotificationManager notificationManager;

    private int notification_id = 8000;

    @Override
    protected void initUI() {
        setContentView(R.layout.ac_main);
        ButterKnife.bind(this);

    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_EXTERNAL_STORAGE) {

            if (hasAllPermissionsGranted(grantResults)) {

                Log.i(TAG, "用户允许打开权限");
//                downloadZip();//下载需要更新的资源包
            } else {
                assistant("申请权限", "应用需打开存储权限才可正常使用。", "", REQUEST_EXTERNAL_STORAGE);
                Log.i(TAG, "用户拒绝打开权限");
                //showPermissionDialog("请去设置页面打开***权限", OPEN_SET_REQUEST_CODE);

            }

        }

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

    private void assistant(String dia_title, String text1, String text2, int requestCode) {
        // 1.创建弹出式对话框
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);    // 系统默认Dialog没有输入框
        // 获取自定义的布局
        View alertDialogView = View.inflate(MainActivity.this, R.layout.dialog_quanxian, null);
        final AlertDialog tempDialog = alertDialog.create();
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
                getAppDetailSettingIntent(MainActivity.this);
                tempDialog.dismiss();
            }
        });

        tempDialog.show();
    }

    private void xiaoxi(String dia_title, String text1) {

        CommonDialog commonDialog = new CommonDialog(MainActivity.this);
        commonDialog.getMessageContent().setVisibility(View.GONE);
        View root_view = commonDialog.getRootView();
        RelativeLayout version_main = root_view.findViewById(R.id.version_check);
        version_main.setVisibility(View.VISIBLE);
        TextView message = root_view.findViewById(R.id.version_message);
        TextView title = root_view.findViewById(R.id.version_title);
        title.setText("版本信息");

        String display_message = text1.replaceAll("\\n", "<br>");
        message.setText(Html.fromHtml(display_message));
        commonDialog.setListener(new CommonDialog.CommonDialogListener() {
            @Override
            public void OnSubmit(AlertDialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void OnCancel(AlertDialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void OnDismiss() {
                SPUtils.saveBoolean(getComeActivity(), "is_show_weclome_diag", true);
            }

            @Override
            public void OnClose(AlertDialog dialog) {

            }
        });
        commonDialog.show();
    }

    /**
     * 跳转到权限设置界面
     */
    public void getAppDetailSettingIntent(Context context) {
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
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String kg = SPUtils.getStringData(this, "ty", "");
        if (kg.equals("true")) {
            mAcache = ACache.get(this);
            checkVersion();
        }
        else{
            if (getIntent().getData() != null) {
                Uri uri = getIntent().getData();
                String open_param = new String(Base64.decode(uri.getQueryParameter("t"), Base64.DEFAULT));
                if (!"".equals(open_param)) {
                    mAcache.put("jumpUrl", open_param);
                }
            }
            openActivityAndCloseThis(SplashActivity.class);
            return;
        }


        if (!CaiNiaoApplication.getInstances().isInit){
            CaiNiaoApplication.getInstances().initAppInfo();
        }

        if (!isServiceRun(this, Constants.AGO_NOTIFICATION_SERVICE) && "true".equals(SPUtils.getStringData(this, "ago_notification", "true"))) {
            CaiNiaoApplication.getInstances().startNotification();
        }

        if (adManager == null && !"".equals(SPUtils.getStringData(this, Constants.TOKEN, ""))) {
            getActivities();
        }
        id = R.id.rb_1;

        File file = new File(Constants.ZIYUAN_PATH);
        LogUtils.d(TAG, "onCreate: " + file.mkdirs() + " --- " + getIntent());
//        myPermission();//检查权限
        initAppSet();//首页弹窗提醒消息
        jumpChecker();

        if (!SPUtils.getStringData(this,"uid","").equals("")) {
            JPushInterface.setAlias(this, 0, SPUtils.getStringData(this,"uid",""));
        }
    }


    private void jumpChecker() {
        // Log.d(TAG, "jumpChecker: 进 " + getIntent() + " --- " + getIntent().getData() + " --- " + getIntent().getStringExtra("message"));
        // 通过URL协议进入
        if (getIntent().getData() != null) {
            Uri uri = getIntent().getData();
            String open_param = new String(Base64.decode(uri.getQueryParameter("t"), Base64.DEFAULT));
            if (!"".equals(open_param)) {
                mAcache.put("jumpUrl", open_param);
                jumpToTarget();
            }
        }

        // 通过通知栏点击进入
        if (getIntent().getStringExtra("message") != null) {
            mAcache.put("jumpUrl", getIntent().getStringExtra("message"));
            jumpToTarget();
        }


    }

    private void checkNotificationClick(){
        /**
         * 在这边判断是不是点击通知栏进入
         * 点击通知栏的话，根据通知类型做相应的跳转
         */

        String check_inform_title = SPUtils.getStringData(this, "inform_title", "-1");

        Log.d("JpushCommonReceiver", "checkNotificationClick: " + check_inform_title);

        if ("t".equals(check_inform_title)){
            String check_inform_message = SPUtils.getStringData(this, "inform_message", "");
            if (!"".equals(check_inform_message)){


                try {
                    String open_param = URLDecoder.decode(check_inform_message, "utf8");
                    open_param = new String(Base64.decode(open_param, Base64.DEFAULT));
                    Log.d("JpushCommonReceiver", "checkNotificationClick: " + open_param);
                    if (!"".equals(open_param)) {
                        // Log.d(TAG, "checkNotificationClick: " + open_param);
                        mAcache.put("jumpUrl", open_param);
                        jumpToTarget();
                    }
                }
                catch (Exception e){
                    // Log.d(TAG, "checkNotificationClick: 解析参数出错！");
                    e.printStackTrace();
                }
            }
            SPUtils.saveStringData(this,"inform_title","-1");
            SPUtils.saveStringData(this,"inform_message","-1");

        }else if ("order".equals(check_inform_title)) {
            SPUtils.saveStringData(this, "inform_title", "-1");
            Intent intent = new Intent(this, MyOrderActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else if ("banlance".equals(check_inform_title)) {
            SPUtils.saveStringData(this, "inform_title", "-1");
            UserInfoBean userBean = CaiNiaoApplication.getUserInfoBean();
            Bundle b = new Bundle();
            if (null != userBean && null != userBean.user_msg) {
                b.putString("balance", userBean.user_msg.balance);
                b.putString("user", userBean.user_msg.balance_user);
                b.putString("service", userBean.user_msg.balance_service);
                b.putString("plantform", userBean.user_msg.balance_plantform);
            }
            Intent intent = new Intent(this, BalanceActivity.class);
            intent.putExtras(b);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else if ("goods".equals(check_inform_title)) {
            SPUtils.saveStringData(this, "inform_title", "-1");
            if (SPUtils.getStringData(this, "inform_message", "-1").startsWith("taobao")) {
                Bundle bundle = new Bundle();
                bundle.putString("num_iid", SPUtils.getStringData(this, "inform_message", "-1").substring(7));
                openActivity(PromotionDetailsActivity.class, bundle);
            } else if (SPUtils.getStringData(this, "inform_message", "-1").startsWith("jingdong")) {
                getJdGoodsMsgRequest(SPUtils.getStringData(this, "inform_message", "-1").substring(9));
            } else if (SPUtils.getStringData(this, "inform_message", "-1").startsWith("pdd")) {
                getPddGoodsMsgRequest(SPUtils.getStringData(this, "inform_message", "-1").substring(4));
            }
            SPUtils.saveStringData(this, "inform_message", "-1");
        } else if ("article".equals(check_inform_title)) {
            SPUtils.saveStringData(this, "inform_title", "-1");
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("title", "文章详情");
            intent.putExtra("url", SPUtils.getStringData(this, "inform_message", "-1"));
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 极光通知栏点击检测
        Log.d(TAG, "onResume: 进");
        checkNotificationClick();
        /* 登陆失效设置为默认分成比例 */
        if ("".equals(SPUtils.getStringData(this, "token", ""))) {
            SPUtils.saveIntData(MainActivity.this, "rate", SPUtils.getIntData(this, "default_rate", 89));
            return;
        }

        getRate();
//        myPermission();//检查权限
        if (!SPUtils.getStringData(this,"uid","").equals("")) {
            JPushInterface.setAlias(this, 0, SPUtils.getStringData(this,"uid",""));
        }

        CaiNiaoApplication.commonGetUserMsg();
    }

    @Override
    protected void initData() {

        if (mAcache == null) mAcache = ACache.get(this);

        token = mAcache.getAsString(Constants.TOKEN);
        phone = mAcache.getAsString(Constants.ACCOUT);
        pwd = mAcache.getAsString(Constants.PASSWORD);
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            if (bundle.containsKey("type")) {
                type = bundle.getInt("type");
            }
        }
        fm = getSupportFragmentManager();
        f1 = new HomeFragment();



        f2 = new VideoContentFragment();//new ChaoJiDaoHangFragment();
//        f3 = new VIPFragment();
//        f3 = new VipFragmentNew();

        f4 = new JuDuoHuiFragment();// new CommuitityFragment();
//        f5 =new MyFragment();
        f5 = new MyFragmentNew();
        ft = fm.beginTransaction();
        switchFragment(f1).commit();
        setStatusBar(getResources().getColor(R.color.bai));

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    AppResouceUpdate.checkUpdate(MainActivity.this, null, new AppResouceUpdate.UpdateCallBack() {
                        @Override
                        public void success() {
                            LogUtils.d(TAG, "MainActivity success: ");
                        }

                        @Override
                        public void fail() {
                            LogUtils.d(TAG, "MainActivity fail: ");
                        }
                    });
                });

            }
        }, 1000);


        LogUtils.d(TAG, "initData: " + getIntent());
        if (getIntent() != null && getIntent().getAction() != null && getIntent().getAction().equals(Constants.NOTIFICATION_ACTION)) {
            String activity_path = getIntent().getStringExtra("path");
            String notice_toast = getIntent().getStringExtra("toast");
            String activity_name = getIntent().getStringExtra("name");

            jumpFragment(activity_path, notice_toast);
        }
    }

    private FragmentTransaction switchFragment(Fragment targetFragment) {
        try {
            getSupportFragmentManager().executePendingTransactions();
        }
        catch (Exception ignored){}
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.frame, targetFragment, targetFragment.getClass().getName());
        } else {
            transaction
                    .hide(currentFragment)
                    .show(targetFragment);
        }
        currentFragment = targetFragment;
        return transaction;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void initListener() {

        rb_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LogUtils.d(TAG, "onClick: 试试刷新页面");
//                // 已经是你了，再点击你就刷新喽。
                // Log.d(TAG, "onClick: " + f2.isInLayout() + f2.isAdded() + f2.isVisible());
                if (currentFragment instanceof VideoContentFragment && f2.isAdded() && rg.getCheckedRadioButtonId() == R.id.rb_2) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("reload", 1);
                    f2.setArguments(bundle);
                }
            }
        });
        rg.setOnCheckedChangeListener(new CaiNiaoRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CaiNiaoRadioGroup group, int checkedId) {
                Drawable[] dr = rb_2.getCompoundDrawables();
                dr[1].setColorFilter(Color.parseColor("#FFA0A0A0"), PorterDuff.Mode.SRC_IN);
                if (checkedId == R.id.rb_1) {
                    id = checkedId;
                    switchFragment(f1).commit();
                    setStatusBar(Color.parseColor(SPUtils.getStringData(MainActivity.this, "color", "#FF3366")));
                } else if (checkedId == R.id.rb_2 || checkedId == R.id.ly_2) {
                    id = checkedId;

                    dr[1].setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.SRC_IN);
                    switchFragment(f2).commit();
                    setStatusBar(Color.parseColor("#000000"));
                } else if (checkedId == R.id.rb_3) {
                    if ("".equals(SPUtils.getStringData(MainActivity.this, "token", ""))) {
                        T.showShort(MainActivity.this, "请先登录");
                        rg.check(id);
                        openActivity(WelActivity.class);
                        return;
                    }
                    rg.check(id);
//                    openActivity(MyShareUrlActivity.class);

//                    id = checkedId;
//                    switchFragment(f3).commit();
//                    setStatusBar(getResources().getColor(R.color.black));
                    Bundle bundle = new Bundle();
                    bundle.putString("jump", "yes");
                    bundle.putString("path", JuduohuiMainActivity.class.getName() + ",f2");
                    openActivity(JuduohuiMainActivity.class, bundle);
                } else if (checkedId == R.id.rb_4) {
                    if ("".equals(SPUtils.getStringData(MainActivity.this, "token", ""))) {
                        T.showShort(MainActivity.this, "请先登录");
                        rg.check(id);
                        openActivity(WelActivity.class);
                        return;
                    }
                    id = checkedId;
                    switchFragment(f5).commit();
                    setStatusBar(Color.parseColor("#fe5240"));
                } else if (checkedId == R.id.rb_5) {

//                    if ("".equals(SPUtils.getStringData(MainActivity.this, "token", ""))) {
//                        T.showShort(MainActivity.this, "请先登录");
//                        rg.check(id);
//                        openActivity(WelActivity.class);
//                        return;
//                    }
                    // id = checkedId;

                    rg.check(id);
                    openActivity(JuduohuiMainActivity.class);


//                    switchFragment(f4).commit();
//                    setStatusBar(Color.parseColor("#fe7254"));
//                    openActivity(XianWanActivity.class);
                }
            }
        });
    }

    private void getRate() {
        LogUtils.d(TAG, "getRate: ");
        RequestParams params = new RequestParams();
        params.put("token", SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "token", ""));
        LogUtils.d(TAG, "getRate: " + params.toString());
        HttpUtils.post(Constants.GET_RATE,MainActivity.this, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onFinish() {
                // 隐藏进度条
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d(TAG, "getRate onSuccess: " + params.toString());
                LogUtils.d(TAG, "getRate onSuccess: " + responseString);
//                LogUtils.d(TAG, "Constants.GET_RATE onSuccess: " + params.toString());
//                LogUtils.d(TAG, "Constants.GET_RATE onSuccess: " + responseString);
//                for (Header h:headers) {
//                    LogUtils.d(TAG, "onSuccess: " + h.getName() +":" + h.getValue());
//                }

                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    //返回码
                    int code = jsonObject.optInt("code");
                    if (0 == code) {
                        SPUtils.saveIntData(MainActivity.this, "rate", jsonObject.getJSONObject("data").getInt("commission_rate"));
//                        ChangZhuTongZ();
//                        ChangZhuIcon();
//                        changzhu();
//                        LogUtils.d("时间时间时间2", changZhuTimeBean.cache_time+"--");
//                        int cache_time = SPUtils.getIntData(MainActivity.this, "cache_time", 0);
//                        int show_interval_time = SPUtils.getIntData(MainActivity.this, "show_interval_time", 0);
//                        int cache_time_icon = SPUtils.getIntData(MainActivity.this, "cache_time_icon", 0);
//                        LogUtils.d("时间时间时间2", cache_time + "--");
//                        if (cache_time == 0) {
//                            timer = new Timer();
//                            timer.schedule(new TimerTask() {
//                                @Override
//                                public void run() {
//                                    // (1) 使用handler发送消息
//                                    Message message = new Message();
//                                    message.what = 256;
//                                    mHandler1.sendMessage(message);
//                                }
//                            }, 0, 1);//每隔60秒使用handler发送一下消息,也就是每隔60秒执行一次,一直重复执行
//                        } else {
//                            timer = new Timer();
//                            timer.schedule(new TimerTask() {
//                                @Override
//                                public void run() {
//                                    // (1) 使用handler发送消息
//                                    Message message = new Message();
//                                    message.what = 256;
//                                    mHandler1.sendMessage(message);
//                                }
//                            }, 0, cache_time);//每隔60秒使用handler发送一下消息,也就是每隔60秒执行一次,一直重复执行
//                        }
//                        if (show_interval_time == 0) {
//                            timer.schedule(new TimerTask() {
//                                @Override
//                                public void run() {
//                                    // (1) 使用handler发送消息
//                                    Message message = new Message();
//                                    message.what = 257;
//                                    mHandler1.sendMessage(message);
//                                }
//                            }, 0, 1);//每隔60秒使用handler发送一下消息,也就是每隔60秒执行一次,一直重复执行
//                        } else {
//                            timer.schedule(new TimerTask() {
//                                @Override
//                                public void run() {
//                                    // (1) 使用handler发送消息
//                                    Message message = new Message();
//                                    message.what = 257;
//                                    mHandler1.sendMessage(message);
//                                }
//                            }, 0, show_interval_time);//每隔60秒使用handler发送一下消息,也就是每隔60秒执行一次,一直重复执行
//                        }
//                        if (cache_time_icon == 0) {
//                            timer.schedule(new TimerTask() {
//                                @Override
//                                public void run() {
//                                    // (1) 使用handler发送消息
//                                    Message message = new Message();
//                                    message.what = 258;
//                                    mHandler1.sendMessage(message);
//                                }
//                            }, 0, 1);//每隔60秒使用handler发送一下消息,也就是每隔60秒执行一次,一直重复执行
//                        } else {
//                            timer.schedule(new TimerTask() {
//                                @Override
//                                public void run() {
//                                    // (1) 使用handler发送消息
//                                    Message message = new Message();
//                                    message.what = 258;
//                                    mHandler1.sendMessage(message);
//                                }
//                            }, 0, cache_time_icon);//每隔60秒使用handler发送一下消息,也就是每隔60秒执行一次,一直重复执行
//                        }
                    } else if (17 == code) {
                        LogUtils.d(TAG, jsonObject.getString("msg"));
                    } else {
                        T.showShort(MainActivity.this, "登录信息已过期");
                        openActivity(WelActivity.class);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
            }
        });

    }

    @Subscribe
    public void toHhuiyuan(MessageEvent event) {
        if ("huiyuan".equals(event.getMessage())) {
            ((RadioButton) findViewById(R.id.rb_3)).setChecked(true);
        } else if ("commuity".equals(event.getMessage())) {
            ((RadioButton) findViewById(R.id.rb_5)).setChecked(true);
        } else if ("back_to_main".equals(event.getMessage())){
            ((RadioButton) findViewById(R.id.rb_1)).setChecked(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
//        CaiNiaoApplication.getInstances().stopNotification();
    }

    long last_back_time = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            // Log.d(TAG, "onKeyDown: 按了返回键了");

            if (last_back_time == 0 || System.currentTimeMillis() - last_back_time > 2000) {
                showToast("再次按下返回退出" + getResources().getString(R.string.app_name));
                last_back_time = System.currentTimeMillis();
                return false;
            }

            return super.onKeyDown(keyCode, event);
//            if (currentFragment instanceof JuDuoHuiFragment) {
//                if (((JuDuoHuiFragment) currentFragment).backKeyPress()) {
//                    return super.onKeyDown(keyCode, event);
//                } else {
//                    return false;
//                }
//            }
        }


//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            startActivity(intent);
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        LogUtils.d(TAG, "onBackPressed: ");
    }

    /**
     * @属性:获取京东推送商品详情
     * @开发者:wmm
     * @时间:2018/12/11 9:50
     */
    private void getJdGoodsMsgRequest(String id) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("apikey", Constants.JD_APP_KEY_NEW);
        requestParams.put("goods_ids", id);
        requestParams.put("isunion", "1");
        OkHttpUtils.getInstance().get(Constants.JDNEWGOODS_LIST + "?" + requestParams.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d("dsfasdf", e.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String s = response.body().string();
                LogUtils.d("dsfasdf", s);
                try {
                    JSONObject object1 = new JSONObject(s);
                    JSONArray array = object1.getJSONObject("data").getJSONArray("data");
                    if (array == null || array.length() == 0) {
                        return;
                    }
                    for (int i = 0; i < 1; i++) {
                        MyGoodsResp resp = new Gson().fromJson(array.getJSONObject(i).toString(), MyGoodsResp.class);
                        Intent intent = new Intent(MainActivity.this, JdDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("goods", resp);
                        intent.putExtra("goods", bundle);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
//        String SERVER_URL = "https://router.jd.com/api";
//        String appKey = Constants.JD_CLIENT_ID;
//        String appSecret = Constants.JD_SECRET;
//        String accessToken = "";
//        final JdClient client = new DefaultJdClient(SERVER_URL, accessToken, appKey, appSecret);
//        final UnionOpenGoodsQueryRequest request = new UnionOpenGoodsQueryRequest();
//        final GoodsReq goodsReq = new GoodsReq();
//        goodsReq.setSkuIds(new Long[]{Long.valueOf(id)});
//        goodsReq.setPageSize(6);
//        goodsReq.setPageIndex(1);
//        request.setGoodsReqDTO(goodsReq);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    UnionOpenGoodsQueryResponse response = client.execute(request);
//                    if (response.getData() == null) {
//                        return;
//                    }
//                    if (response.getData().length <= 0) {
//                        return;
//                    }

//                } catch (JdException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

    /**
     * @属性:获取拼多多商品详情
     * @开发者:wmm
     * @时间:2018/12/11 9:50
     */
    private void getPddGoodsMsgRequest(String id) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("goods_id", id);
        HttpUtils.post(Constants.GET_PDD_DETAIL,MainActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (getComeActivity().isDestroyed())
                    return;
                try {
                    if (new JSONObject(responseString).getInt("code") != 0) {
                        T.showShort(MainActivity.this, new JSONObject(responseString).getString("msg"));
                        return;
                    }
                    JSONObject object = new JSONObject(responseString).getJSONObject("data");
                    Intent intent = new Intent(MainActivity.this, PddDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("goods", new Gson().fromJson(object.getJSONObject("goods_details").toString(), PDDBean.class));
                    intent.putExtra("goods", bundle);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showAc(final int posi) {
        if (posi >= adInfos.size()) {
            return;
        }
        List<AdInfo> temp = new ArrayList<>();
        temp.add(adInfos.get(posi));
        if ("8".equals(adInfos.get(posi).getTitle())) {
            isRece(posi);
        } else {
            adManager = new AdManager(MainActivity.this, temp);
            adManager.setOnImageClickListener((view, advInfo) -> {
                Intent intent;
                switch (advInfo.getTitle()) {
                    case "1":
                        intent = new Intent(MainActivity.this, WebViewActivity.class);
                        intent.putExtra("title", advInfo.getTitle());
                        intent.putExtra("url", advInfo.getUrl());
                        startActivity(intent);
                        break;
                    case "2":
                        intent = getPackageManager().getLaunchIntentForPackage("com.taobao.taobao");
                        if (intent != null) {
                            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData mClipData = ClipData.newPlainText("Label", advInfo.getUrl());
                            cm.setPrimaryClip(mClipData);
                            startActivity(intent);
                            return;
                        } else {
                            T.showShort(MainActivity.this, "未安装淘宝客户端");
                        }
                        break;
                    case "3":
                        break;
                    case "4":
                        break;
                    case "5":
                        break;
                    case "6":
                        intent = new Intent(MainActivity.this, WebViewActivity2.class);
                        intent.putExtra("title", "年货节");
                        intent.putExtra("url", "");
                        startActivity(intent);
                        break;
                    case "7":
                        isRece(posi);
                        return;
                    case "8":
                        isRece(posi);
                        return;
                    case "9":
                        Bundle bundle = new Bundle();
                        bundle.putString("num_iid", advInfo.getUrl());
                        openActivity(PromotionDetailsActivity.class, bundle);
                        break;
                    case "17":
                        try {
                            Intent intent1 = new Intent(MainActivity.this, NewShuanshierActivity.class);
                            intent1.putExtra("img", Constants.APP_IP + object1.getString("detail_img"));
                            intent1.putExtra("title", object1.getString("title"));
                            intent1.putExtra("text", object1.getString("text"));
                            intent1.putExtra("tbuid", CaiNiaoApplication.getUserInfoBean().user_msg.tb_rid);
                            intent1.putExtra("hdid", object1.getString("type_value"));
                            startActivity(intent1);
                        } catch (Exception e) {

                        }
                        break;
                }
                adManager.dismissAdDialog();
                adManager = null;
                currentAc = currentAc + 1;
                showAc(currentAc);
            }).setOnCloseClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adManager = null;
                    currentAc = currentAc + 1;
                    showAc(currentAc);
                }
            });
            adManager.setOverScreen(true)
                    .setPageTransformer(new ZoomOutTranformer()).showAdDialog(AdConstant.ANIM_UP_TO_DOWN);
        }
    }

    JSONObject object1;

    private void getActivities() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("cat_id", 5);
        HttpUtils.post(Constants.GET_BANNER, MainActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(throwable.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject object = new JSONObject(responseString);
                    if (object.getInt("code") == 0) {
                        final JSONArray array = object.getJSONObject("data").getJSONArray("list");
                        adInfos.clear();
                        for (int i = 0; i < array.length(); i++) {
                            AdInfo adInfo = new AdInfo();
                            object = array.getJSONObject(i);
                            adInfo.setActivityImg(Constants.APP_IP + object.getString("img"));
                            switch (object.getString("type")) {
                                case "1":
                                    adInfo.setUrl(object.getString("href"));
                                    break;
                                case "2":
                                    adInfo.setUrl(object.getString("type_value"));
                                    break;
                                case "3":
                                    break;
                                case "4":
                                    break;
                                case "5":
                                    break;
                                case "6":
                                    break;
                                case "7":
                                    break;
                                case "8":
                                    break;
                                case "9":
                                    adInfo.setUrl(object.getString("type_value"));
                                    break;
                                case "17":
                                    object1 = object;
                                    break;
                            }
                            adInfo.setTitle(object.getString("type"));
                            adInfos.add(adInfo);
                        }
                        showAc(currentAc);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void isRece(final int pos) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        RequestParams requestParams = new RequestParams();
        requestParams.put("data_type", PddClient.data_type);
        requestParams.put("version", PddClient.version);
        requestParams.put("timestamp", time);
        requestParams.put("token", SPUtils.getStringData(this, "token", ""));
        requestParams.put("type", "hkx.UserBalanceRecord.isReceiveBonus");
        Map<String, String> temp = new HashMap<>();
        temp.put("data_type", PddClient.data_type);
        temp.put("version", PddClient.version);
        temp.put("timestamp", time);
        temp.put("token", SPUtils.getStringData(this, "token", ""));
        temp.put("type", "hkx.UserBalanceRecord.isReceiveBonus");
        String sign = PddClient.getSign1(temp);
        requestParams.put("sign", sign);
        HttpUtils.post(Constants.ISRECEIVER_BONUS,MainActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    if ("N".equals(jsonObject.getJSONObject("data").getString("is_receive"))) {
                        List<AdInfo> temp = new ArrayList<>();
                        temp.add(adInfos.get(pos));
                        adManager = new AdManager(MainActivity.this, temp);
                        adManager.setOnImageClickListener(new AdManager.OnImageClickListener() {
                            @Override
                            public void onImageClick(View view, AdInfo advInfo) {
                                hongbao();
//                                        Intent intent = new Intent(MainActivity.this, WebViewActivity2.class);
//                                        intent.putExtra("title", "年货节");
//                                        intent.putExtra("url", "");
//                                        startActivity(intent);
                                adManager.dismissAdDialog();
                                adManager = null;
                                currentAc = currentAc + 1;
                                showAc(currentAc);
                            }
                        }).setOnCloseClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                adManager = null;
                                currentAc = currentAc + 1;
                                showAc(currentAc);
                            }
                        });
                        adManager.setOverScreen(true)
                                .setPageTransformer(new ZoomOutTranformer()).showAdDialog(AdConstant.ANIM_UP_TO_DOWN);
                    } else {
                        currentAc = currentAc + 1;
                        showAc(currentAc);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void hongbao() {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        RequestParams requestParams = new RequestParams();
        requestParams.put("data_type", PddClient.data_type);
        requestParams.put("version", PddClient.version);
        requestParams.put("timestamp", time);
        requestParams.put("token", SPUtils.getStringData(this, "token", ""));
        requestParams.put("type", "hkx.UserBalanceRecord.receiveBonus");
        Map<String, String> temp = new HashMap<>();
        temp.put("data_type", PddClient.data_type);
        temp.put("version", PddClient.version);
        temp.put("timestamp", time);
        temp.put("token", SPUtils.getStringData(this, "token", ""));
        temp.put("type", "hkx.UserBalanceRecord.receiveBonus");
        String sign = PddClient.getSign1(temp);
        requestParams.put("sign", sign);
        HttpUtils.post(Constants.HONGBAO, MainActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

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
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    Intent intent = new Intent(MainActivity.this, QdActivity.class);
                    intent.putExtra("money", jsonObject.getString("money"));
                    SPUtils.saveIntData(MainActivity.this, "hongbao", 1);
                    if (code == 0) {
                        intent.putExtra("mess", "新年红包已存至余额~");
                    } else {
                        intent.putExtra("mess", msg);
                    }
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (intent != null)
            LogUtils.d(TAG, "onActivityResult: __intent:" + intent.getDataString());
        if (resultCode == 1000) {
            //判断读取剪切板权限开没开
            String kg = SPUtils.getStringData(this, "read_clipboard", "");
            if (!kg.equals("true")) {
                showToast("请在我的页面-设置中心-权限设置-剪切板权限-开启");
            } else {
                Snackbar snackbar = Snackbar.make(getCurrentFocus(), "图片数据已识别，正在后台分析扫码数据...", Snackbar.LENGTH_LONG);
                snackbar.setAction("好的", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        }
    }

    AlertDialog custom_alert = null;

    private void hotActivity() {
        // 活动列表
        if (mAcache == null) mAcache = ACache.get(this);
        String activity_list = mAcache.getAsString("activityList");
        if (!"".equals(activity_list) && activity_list != null) {
            // Log.d(TAG, "initAppSet: " + activity_list);
            try {
                com.alibaba.fastjson.JSONArray activity_array = com.alibaba.fastjson.JSONArray.parseArray(activity_list);
                int will_index = RandomUntil.getNum(0, activity_array.size());
                com.alibaba.fastjson.JSONObject will_obj = activity_array.getJSONObject(will_index);
                if (will_obj != null) {
                    mAcache.remove("activityList");
                    AlertDialog.Builder custom_alert_builder = new AlertDialog.Builder(getComeActivity());

                    View custom_view = LayoutInflater.from(getComeActivity()).inflate(R.layout.custom_activity_dialog, null);
                    SketchImageView custom_image = custom_view.findViewById(R.id.custom_image);
                    custom_image.getOptions().setThumbnailMode(true);
                    custom_image.displayImage(will_obj.getString("icon"));
                    custom_image.setClickable(true);
                    custom_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            runOnUiThread(() -> {
                                Intent custom_open = new Intent();
                                custom_open.setComponent(new ComponentName(getComeActivity(), will_obj.getString("path")));
                                custom_open.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(custom_open);
                                custom_alert.dismiss();
                            });
                        }
                    });
                    custom_alert_builder.setView(custom_view);
                    custom_alert = custom_alert_builder.create();
                    custom_alert.show();
                    custom_alert.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                    WindowManager.LayoutParams lp = custom_alert.getWindow().getAttributes();
                    lp.width = custom_alert.getWindow().getWindowManager().getDefaultDisplay().getWidth() - UIUtils.dp2px(40);
                    custom_alert.getWindow().setAttributes(lp);//设置宽度
//                    Glide.with(this).load("https://b.bdstatic.com/searchbox/icms/searchbox/img/ipnews-top_img.jpg").asBitmap().thumbnail(0.1F).fitCenter().into(custom_image);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 2)
    public void onMessage(JuduohuiMessage message){
        // Log.d(TAG, "onMessage: MainActivity处理消息");
        // 如果有name,icon则是普通通知，走BaseActivity中的消息处理
        if (message.getMessage().containsKey("name") && message.getMessage().containsKey("icon")) return;
        // Log.d(TAG, "onMessage: 正常跳");
        mAcache.put("jumpUrl",message.getMessage().toJSONString());
        jumpToTarget();

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(JuduohuiCountInsertAdvMessage message){

    }
    private void jumpToTarget() {
        Log.d("JpushCommonReceiver", "jumpToTarget: ");
        // Log.d(TAG, "jumpToTarget: ");
        CaiNiaoApplication.getInstances().getNotificationManager().cancel(notification_id);
        String app_t_url = mAcache.getAsString("jumpUrl");
        Log.d("JpushCommonReceiver", "jumpToTarget: app_t_url = " + app_t_url);
        // Log.d(TAG, "jumpToTarget: " + app_t_url);
        if (!"".equals(app_t_url) && app_t_url != null) {
            try {
                com.alibaba.fastjson.JSONObject obj = com.alibaba.fastjson.JSONObject.parseObject(app_t_url);
//                if (obj.containsKey("name") && obj.containsKey("icon") && obj.containsKey("path")) return;
                String action = obj.getString("key");
                com.alibaba.fastjson.JSONObject val_obj = obj.getJSONObject("val");
                Bundle bundle = new Bundle();
                Intent open_intent = new Intent();
                // Log.d(TAG, "jumpToTarget: 跳转的KEY值 = " + action);
                if (!"".equals(obj.getString("toast"))) showToast(obj.getString("toast"));
                switch (action) {
                    case "myinfo": //我的1
                        String notice = obj.getString("toast");
                        jumpFragment(MainActivity.class.getName() + ",f5", notice);
                        break;
                    case "index":
                        break;
                    case "shop_order": //购物订单1
                        if (val_obj != null && val_obj.containsKey("order_id")) {
                            String trade_id = val_obj.getString("order_id");
                            bundle.putString("se", trade_id);
                        }
                        openActivity(MyOrderActivity.class, bundle);
                        break;
                    case "invitation": // 邀请页面1
                        openActivity(MyShareUrlActivity.class);
                        break;
                    case "myMarker": // 我的市场1
                        // Log.d(TAG, "jumpToTarget: level" + val_obj.getIntValue("level"));
                        bundle.putInt("level", val_obj.getIntValue("level"));
                        openActivity(MyMarketActivity.class, bundle);
                        break;
                    case "Pddorder": // 拼多多列表1
                        Log.d("JpushCommonReceiver", "jumpToTarget: 跳拼多多");
                        openActivity(PddActivity.class);
                        break;
                    case "productDetails": // 商品详情页 1
                        String p_type = val_obj.getString("type"); // 是哪个平台的商品
                        String goods_id = val_obj.getString("id"); // 商品ID
                        switch (p_type) {
                            case "1": // 淘宝
                                bundle.putString("num_iid", goods_id);
                                openActivity(PromotionDetailsActivity.class, bundle);
                                break;
                            case "2": // 京东
                                getJdGoodsMsgRequest(goods_id);
                                break;
                            case "3": // 拼多多
                                getPddGoodsMsgRequest(goods_id);
                                break;
                        }
                        break;
                    case "sign": //签到页面
                        bundle.putString("jump", "yes");
                        bundle.putString("path", JuduohuiMainActivity.class.getName() + ",f2");
                        bundle.putString("notice", obj.getString("notice"));
                        openActivity(JuduohuiMainActivity.class, bundle);
                        break;
                    case "salary": //领工资
                        bundle.putString("jump", "yes");
                        bundle.putString("path", JuduohuiMainActivity.class.getName() + ",f3");
                        bundle.putString("notice", obj.getString("notice"));
                        openActivity(JuduohuiMainActivity.class, bundle);
                        break;
                    case "participRecord": //调查卷参与记录
                        bundle.putString("url", "./survey_history.html");
                        openActivity(JuDuoHuiActivity.class, bundle);
                        break;
                    case "banlance": // 资金记录
                        bundle.putString("url", "./profit_history.html");
                        openActivity(JuDuoHuiActivity.class, bundle);
                        break;
                    case "balanceDraw": // 购物提现页面
                        bundle.putString("url", "./profit.html");
                        bundle.putString("tab", "1");
                        openActivity(JuDuoHuiActivity.class, bundle);
                        break;
                    case "hpointDraw": // 惠币提现页面
                        bundle.putString("url", "./profit.html");
                        bundle.putString("tab", "2");
                        openActivity(JuDuoHuiActivity.class, bundle);
                        break;
                    case "readmakemoneyList": //阅读赚钱页面
                        openActivity(JuduohuiMainActivity.class);
                        break;
                    case "readmakemoneyDetails": //阅读赚钱的详情页
                        com.alibaba.fastjson.JSONObject tmp = new com.alibaba.fastjson.JSONObject();
                        tmp.put("id", val_obj.getString("artice_id"));
                        bundle.putString("config", tmp.toJSONString());
                        open_intent.putExtras(bundle);
                        open_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        openActivity(NewsInformation.class, bundle);
                        break;
                    case "configActriceDetails":
                        NewsActivity.actionStart(MainActivity.this, val_obj.getString("article_id"), val_obj.getString("article_title"));
                        break;
                    case "other": // 其它
                        Intent tt = new Intent();
                        ComponentName componentName = new ComponentName(this, obj.getString("path"));
                        tt.setComponent(componentName);
                        tt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(tt);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            mAcache.remove("jumpUrl");
        }
    }

    public void initAppSet() {
        String notice_title = SPUtils.getStringData(getApplicationContext(), "notice_title", "");
        String notice_content = SPUtils.getStringData(getApplicationContext(), "notice_content", "");
        if (!notice_content.equals("") && !SPUtils.getBoolean(getComeActivity(), "is_show_weclome_diag", false)) {
            xiaoxi(notice_title, notice_content);
        }
        hotActivity();
        jumpToTarget();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Log.d(TAG, "onNewIntent: tong yi " + SPUtils.getStringData(this, "ty", ""));

        // Log.d(TAG, "onNewIntent: " + CaiNiaoApplication.getInstances().isInit);
        if (!CaiNiaoApplication.getInstances().isInit){
            openActivity(SplashActivity.class);
            return;
        }

        LogUtils.d(TAG, "zjm onNewIntent: " + intent);
        LogUtils.d(TAG, "zjm onNewIntent: " + intent.getStringExtra("path") + "," + intent.getStringExtra("name") + "," + intent.getStringExtra("toast"));
        // Log.d(TAG, "onNewIntent: " + intent.getData() + "action" + intent.getAction());
        if (intent.getAction()!=null && intent.getAction().equals(Constants.NOTIFICATION_ACTION)) {
            Log.d(TAG, "onNewIntent: ");
            String activity_path = intent.getStringExtra("path");
            String notice_toast = intent.getStringExtra("toast");
            String activity_name = intent.getStringExtra("name");
            jumpFragment(activity_path, notice_toast);
        }
        else{
            setIntent(intent);
            jumpChecker();
        }
    }

    void jumpFragment(String activity_path, String notice_toast) {
        if (!"".equals(notice_toast)) showToast(notice_toast);
        LogUtils.d(TAG, "jumpFragment: " + activity_path);
        if (!activity_path.equals("")) {
            //特殊处理 fragments
            if (activity_path.contains(",")) {
                String[] tmp_arr = activity_path.split(",");
                Fragment f;
                RadioButton radioButton;

                // Log.d(TAG, "jumpFragment: " + MainActivity.class.getCanonicalName());
                // Log.d(TAG, "jumpFragment: " + MainActivity.class.getName());
                // Log.d(TAG, "jumpFragment: " + MainActivity.class.getSimpleName());
                // Log.d(TAG, "jumpFragment: " + MainActivity.class.toString());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // Log.d(TAG, "jumpFragment: " + MainActivity.class.getTypeName());
                    // Log.d(TAG, "jumpFragment: " + MainActivity.class.toGenericString());
                }
                if (tmp_arr[0].equals(MainActivity.class.getName())) {
                    switch (tmp_arr[1]) {
                        case "f1":
                            f = f1;
                            radioButton = rb_1;
                            break;
                        case "f2":
                            f = f2;
                            radioButton = rb_2;
                            break;
                        case "f3":
                            f = f3;
                            radioButton = rb_3;
                            break;
                        case "f4":

                            if ("".equals(SPUtils.getStringData(MainActivity.this, "token", ""))) {
                                T.showShort(MainActivity.this, "请先登录");
                                rg.check(id);
                                openActivity(WelActivity.class);
                                return;
                            }

                            f = f4;
                            radioButton = rb_5;
                            radioButton.performClick();
                            return;
//                            break;
                        case "f5":
                            f = f5;
                            radioButton = rb_4;
                            break;
                        default:
                            f = f1;
                            radioButton = rb_1;
                            break;
                    }
                    radioButton.performClick();
                    try{
                        switchFragment(f).commit();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }


//                    if (!f.isAdded()){
//                        getSupportFragmentManager().beginTransaction().hide(currentFragment).show(f).commit();
//                    }
//                    else{
//                        switchFragment(f).commit();
//                    }

                } else {
                    try {
                        Intent tmp_intent = new Intent();
                        tmp_intent.setComponent(new ComponentName(getPackageName(), tmp_arr[0]));
                        tmp_intent.setAction(Constants.NOTIFICATION_ACTION);
                        tmp_intent.putExtra("jump", "yes");
                        tmp_intent.putExtra("toast", notice_toast);
                        tmp_intent.putExtra("path", activity_path);
                        tmp_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(tmp_intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    Intent tmp_intent = new Intent();
                    tmp_intent.setComponent(new ComponentName(getPackageName(), activity_path));
                    startActivity(tmp_intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
