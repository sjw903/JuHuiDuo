package com.android.jdhshop.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spanned;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.MainActivity;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.AdActivity;
import com.android.jdhshop.activity.BindActivity;
import com.android.jdhshop.activity.BridgeActivity;
import com.android.jdhshop.activity.DialogActivity;
import com.android.jdhshop.activity.DialogActivity2;
import com.android.jdhshop.activity.NewsActivity;
import com.android.jdhshop.activity.SearchResultActivity;
import com.android.jdhshop.activity.SplashActivity;
import com.android.jdhshop.activity.UpdateService;
import com.android.jdhshop.advistion.JuDuoHuiAdvertisement;
import com.android.jdhshop.bean.MyGoodsResp;
import com.android.jdhshop.bean.PDDBean;
import com.android.jdhshop.bean.PddClient;
import com.android.jdhshop.bean.PromotionDetailsBean;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.VersionInformationBean;
import com.android.jdhshop.bean.Wphbean;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.AsyncHttpResponseHandler;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.https.SyncHttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.juduohui.CommonDialog;
import com.android.jdhshop.juduohui.JuduohuiSplashActivity;
import com.android.jdhshop.juduohui.message.JuduohuiCountInsertAdvMessage;
import com.android.jdhshop.juduohui.message.JuduohuiMessage;
import com.android.jdhshop.login.LoginActivity;
import com.android.jdhshop.login.RegisterActivity;
import com.android.jdhshop.login.WelActivity;
import com.android.jdhshop.my.ResetPasswordActivity;
import com.android.jdhshop.my.ResetPhoneActivity;
import com.android.jdhshop.utils.APKVersionCodeUtils;
import com.android.jdhshop.utils.BroadcastContants;
import com.android.jdhshop.utils.BroadcastManager;
import com.android.jdhshop.utils.DES3DUtils;
import com.android.jdhshop.utils.DownloadUtil;
import com.android.jdhshop.utils.InstallUtil;
import com.android.jdhshop.utils.OkHttpUtils;
import com.android.jdhshop.utils.StartActivityUtils;
import com.android.jdhshop.widget.EchartView;
import com.android.jdhshop.widget.LoadingDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.anzewei.parallaxbacklayout.ParallaxBack;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.annotation.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jiguang.analytics.android.api.CountEvent;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
//import cn.jpush.android.api.JPushInterface;
import cz.msebera.android.httpclient.Header;
import me.drakeet.materialdialog.MaterialDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import pub.devrel.easypermissions.EasyPermissions;

//import com.android.jdhshop.https.HttpUtils.AsyncHttpResponseHandler;
//import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
//import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;

/**
 * Activity基类
 */
@ParallaxBack
public abstract class BaseActivity extends AppCompatActivity {

    public static final String TAG = "BaseActivity";
    public static boolean isUpdata = true;
    /**
     * 用来保存所有已打开的Activity
     */
    private static Stack<Activity> listActivity = new Stack<Activity>();

    /**
     * 记录上次点击按钮的时间
     **/
    private long lastClickTime;
    /**
     * 按钮连续点击最低间隔时间 单位：毫秒
     **/
    public final static int CLICK_TIME = 500;


    public MaterialDialog mMaterialDialog;
    public LoadingDialog loadingDialog;

    private ACache mAcache;
    String userId;
    //指纹管理
    private FingerprintManagerCompat fingerprintManager;

    public static Uri uri;
    public File file;
    private MaterialDialog downDilog;
    private View downLayout;
    private ProgressBar downProgressBar;
    private UpdateService.Builder builder;
    private boolean isDownLoadFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置activity为无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        int flag = getIntent().getFlags();
        //首次启动 Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT 为 0
        //再次点击图标启动时就不为零了
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        String kg = SPUtils.getStringData(this, "ty", "");
        LogUtils.d("kg", "onCreate: kg:" + kg);

        if (kg.equals("true")) {
            setStatusBar(Color.WHITE);
            JAnalyticsInterface.onPageStart(this, this.getClass().getCanonicalName());
            //初始化
            fingerprintManager = FingerprintManagerCompat.from(BaseActivity.this);
            mMaterialDialog = new MaterialDialog(this);//初始化MaterialDialog
            downDilog = new MaterialDialog(this);
            downLayout = LayoutInflater.from(this).inflate(R.layout.down_layout, null);
            downProgressBar = downLayout.findViewById(R.id.pb_progressbar);
            downDilog.setView(downLayout);
            downDilog.setCanceledOnTouchOutside(false);
            // 将activity推入栈中
            listActivity.push(this);
            File mPhotoFile = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "hkx");// TODO

            EventBus.getDefault().register(this);
        }

//        //拍照路径


        // 初始化ui
        initUI();
        // 初始化数据
        initData();
        // 事件监听
        initListener();
        //检查版本更新
//        if (isUpdata) {
//            ArticleRequest();
//        }

    }

    protected void setStatusBar(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        if (color == Color.WHITE) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            if (isFlyme()) {
                CommonUtils.setMeizuStatusBarDarkIcon(this, true);
            } else if (isMIUI()) {
                CommonUtils.setMiuiStatusBarDarkMode(this, true);
            } else if (Build.MANUFACTURER.equalsIgnoreCase("OPPO")) {
                CommonUtils.setOPPOStatusTextColor(true, this);
            } else {
            }
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }

    }

    NotificationManager notificationManager;

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 1)
    public void onMessage(@NotNull JuduohuiMessage message) {
        // Log.d(TAG, "onMessage: 接收消息");
        // Log.d(TAG, "JuduohuiMessage: " + message.getMessage());
        // 如果消息内无name并且也没有icon则认为是直接跳转信息
        if (!message.getMessage().containsKey("name") && !message.getMessage().containsKey("icon"))
            return;
        // Log.d(TAG, "onMessage: 通知跳");
        Context activity = getApplicationContext();
        com.alibaba.fastjson.JSONObject notification_message = message.getMessage();
        if (message.getMessage() != null) {
            boolean is_notification_enable = NotificationManagerCompat.from(getComeActivity()).areNotificationsEnabled();
            CaiNiaoApplication.isOpenNotification = is_notification_enable;
            if (is_notification_enable) {
                int notification_id = (int) System.currentTimeMillis();
                if (notificationManager == null)
                    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                NotificationCompat.Builder notification_builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel = notificationManager.getNotificationChannel(Constants.NORMAL_NOTIFICATION_CHANNEL);

                    if (notificationChannel == null) {
                        notificationChannel = new NotificationChannel(Constants.NORMAL_NOTIFICATION_CHANNEL, "聚多惠", NotificationManager.IMPORTANCE_HIGH);
                        notificationManager.createNotificationChannel(notificationChannel);
                    }
                    notificationChannel.enableLights(true);
                    notificationChannel.enableVibration(true);
                    notificationChannel.setName("聚多惠普通通知");
                    // Log.d(TAG, "onMessage: " + notificationChannel);
                    notification_builder = new NotificationCompat.Builder(getComeActivity(), Constants.NORMAL_NOTIFICATION_CHANNEL);
                } else {
                    notification_builder = new NotificationCompat.Builder(getComeActivity());
                }

                Intent notification_i = new Intent(activity, BridgeActivity.class);
                notification_i.putExtra("message", message.getMessage().toJSONString());
                notification_i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);


                String icon_url = message.getMessage().getString("icon");
                String notification_title = message.getMessage().getString("name");
                String notification_content = message.getMessage().getString("content");

                PendingIntent notification_intent = PendingIntent.getActivity(activity, notification_id, notification_i, PendingIntent.FLAG_UPDATE_CURRENT);
                notification_builder
                        .setSmallIcon(R.drawable.app_icon)
                        .setContentIntent(notification_intent)
                        .setContentTitle(notification_title)
                        .setAutoCancel(true)
                        .setContentText(notification_content);


                if (icon_url != null) {
                    // Log.d(TAG, "onMessage: 有图片需要下载 ");
                    Glide.with(activity).load(icon_url).asBitmap().thumbnail(0.5f).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            notification_builder.setLargeIcon(resource);
                            notificationManager.notify(notification_id, notification_builder.build());
                        }
                    });
                } else {
                    // Log.d(TAG, "onMessage: 无图片需要下载 ");
                    notificationManager.notify(notification_id, notification_builder.build());
                }
            }
        }
        EventBus.getDefault().cancelEventDelivery(message);
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 1)
    public void onCountInsertScreenAdv(@NotNull JuduohuiCountInsertAdvMessage message) {
        EventBus.getDefault().cancelEventDelivery(message);
        if (mAcache == null) {
            mAcache = ACache.get(this);
        }
        com.alibaba.fastjson.JSONObject adv_config_obj = com.alibaba.fastjson.JSONObject.parseObject(SPUtils.getStringData(this, "ad_place_app_insert_screen", ""));
        com.alibaba.fastjson.JSONArray adv_config = adv_config_obj.getJSONArray("list");

        int max_page_view = adv_config_obj.getIntValue("interval_num");
        int current_count = mAcache.getAsString("current_insert_adv_page") == null || "".equals(mAcache.getAsString("current_insert_adv_page")) ? 0 : Integer.parseInt(mAcache.getAsString("current_insert_adv_page"));
        // Log.d(TAG, "onCountInsertScreenAdv: 当前：" + current_count + ",总："+max_page_view);
        if (current_count >= max_page_view) {

            // 跳广告
            // Log.d(TAG, "onCountInsertScreenAdv: 跳广告");
            JuDuoHuiAdvertisement advertisement = new JuDuoHuiAdvertisement(message.getActivity());
            advertisement.setInsertScreenListen(new JuDuoHuiAdvertisement.InsertScreenAdListen() {
                @Override
                public void error(com.alibaba.fastjson.JSONObject error) {
                    // Log.d(TAG, "onCountInsertScreenAdv error: " + error);
                    advertisement.getInsertScreenAd(adv_config);
                }

                @Override
                public void click(View v, com.alibaba.fastjson.JSONObject info) {

                }

                @Override
                public void displayed() {
                    mAcache.put("current_insert_adv_page", "0");
                }

                @Override
                public void close() {

                }

                @Override
                public void closed() {

                }

                @Override
                public void videoStart() {

                }

                @Override
                public void videoEnd() {

                }

                @Override
                public void skip() {

                }

                @Override
                public void receive() {

                }

                @Override
                public void cached() {

                }
            });
            advertisement.getInsertScreenAd(adv_config);
        } else {
            mAcache.put("current_insert_adv_page", String.valueOf(current_count + 1));
        }
    }

    private void zhuanlian(String temp) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        RequestParams requestParams = new RequestParams();
        requestParams.put("data_type", PddClient.data_type);
        requestParams.put("pid", "2642045_41895967");
        requestParams.put("client_id", PddClient.client_id);
        requestParams.put("timestamp", time);
        requestParams.put("source_url", temp);
        requestParams.put("type", "pdd.ddk.goods.zs.unit.url.gen");
        Map<String, Object> tempd = new HashMap<>();
        tempd.put("data_type", PddClient.data_type);
        tempd.put("type", "pdd.ddk.goods.zs.unit.url.gen");
        tempd.put("client_id", PddClient.client_id);
        tempd.put("pid", "2642045_41895967");
        tempd.put("source_url", temp);
        tempd.put("timestamp", time);
        String sign = PddClient.getSign(tempd);
        requestParams.put("sign", sign);
        HttpUtils.post1(PddClient.serverUrl, BaseActivity.this, requestParams, new TextHttpResponseHandler() {
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
                if (responseString.contains("error_response")) {
                    return;
                }
                try {
                    JSONObject o = new JSONObject(responseString);
                    String temp1 = o.getJSONObject("goods_zs_unit_generate_response").getString("url");
                    if (temp1.contains("mobile.yangkeduo.com")) {
                        Uri uri = Uri.parse(temp1);
                        if (uri.getQueryParameter("goods_id") != null && !"".equals(uri.getQueryParameter("goods_id"))) {
                            getPddDetail(uri.getQueryParameter("goods_id"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        String kg = SPUtils.getStringData(this, "read_clipboard", "true");

        if (!kg.equals("true")) {
            return;
        } else {
            if (hasFocus) {
                if (getComeActivity() instanceof NewsActivity || getComeActivity() instanceof JuduohuiSplashActivity || getComeActivity() instanceof SplashActivity || getComeActivity() instanceof AdActivity || getComeActivity() instanceof WelActivity || getComeActivity() instanceof LoginActivity || getComeActivity() instanceof BindActivity || getComeActivity() instanceof ResetPhoneActivity || getComeActivity() instanceof ResetPasswordActivity || getComeActivity() instanceof RegisterActivity) {
                    return;
                }
                if (Build.VERSION.SDK_INT >= 23) {
                    LogUtils.d(TAG, "剪切板监控SDK>23 ");
                    //读取sd卡的权限
                    String[] mPermissionList = new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    if (EasyPermissions.hasPermissions(this, mPermissionList)) {
                        aiClipData();
                    } else {
                        // 每2分钟进行一次权限检测
                        if (mAcache == null) mAcache = ACache.get(getCacheDir());
                        if (!"".equals(mAcache.getAsString("checked_permission"))) {
                            return;
                        }

                        mAcache.put("checked_permission","yes",2*60);

                        //未同意过,或者说是拒绝了，再次申请权限
                        Log.d(TAG, "onWindowFocusChanged: 这里进行权限申请了！");
//                        EasyPermissions.requestPermissions(this, "读取淘宝、京东等复制链接需要读取权限", 1, mPermissionList);
                        AcpOptions.Builder acpOptions = new AcpOptions.Builder();
                        acpOptions.setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .setDeniedSettingBtn("关闭提示")
                                .setDeniedCloseBtn("重新获取")
                                .setDeniedMessage("您已拒绝了授予" + getComeActivity().getResources().getString(R.string.app_name) +"读写设备上的文件权限，部分需要此权限的功能将不能正常使用，请知悉! \n\n点击重新获取跳转到APP权限设置页面，请授予“读写手机存储”权限。")
                                .setRationalBtn("再次获取")
                                .setRationalMessage(getComeActivity().getResources().getString(R.string.app_name) + "需要读写设备上的文件权限才可以正常使用。")
                                .build();

                        Acp.getInstance(getComeActivity()).request(acpOptions.build(), new AcpListener() {
                            @Override
                            public void onGranted() {
                                Log.d(TAG, "onGranted: ");
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                Log.d(TAG, "onDenied: " + permissions.size() + "" + Arrays.toString(permissions.toArray()));
                                getAppDetailSettingIntent(getComeActivity());
                            }
                        });
                    }
                } else {
                    LogUtils.d(TAG, "剪切板监控SDK<23 ");
                    aiClipData();
                }
            }
        }

    }

    // 剪切板监控
    private void aiClipData() {
        String temp = getClipboard();
        temp = StringUtils.trim(temp);
        if (temp.length() < 6) return;
        if (!temp.contains("tb.cn") && !temp.contains("jd.com") && !temp.contains("taobao.com") && !temp.contains("tmall.com") && !temp.contains("yangkeduo.com")) {
            return;
        }

        if (!"".equals(temp)) {
            final String ori_url = temp;
            Intent test_intent = getIntent();
            LogUtils.d(TAG, "onWindowFocusChanged: " + test_intent + "__" + test_intent.getDataString());
            /*天猫PC中的用手机购买*/
            if (temp.contains("c.tb.cn") && temp.startsWith("http")) {
                String finalTemp = temp;
                Thread tmp_thread = new Thread(() -> {
                    LogUtils.d(TAG, "请求: " + finalTemp + ",转长链接");
                    String goods_id = "";
                    Map<String, String> result = SyncHttpUtils.get(finalTemp);
                    String long_url = "";
                    if (result == null) return;
                    if ("S".equals(result.get("code"))) {
                        String responseString = result.get("data");
                        //String goods_id = "";

                        Pattern long_url_pattern = Pattern.compile("var\\s*url\\s*=\\s*'(.*)'");
                        Matcher long_url_matcher = long_url_pattern.matcher(responseString);
                        if (long_url_matcher.find()) {
                            long_url = long_url_matcher.group(long_url_matcher.groupCount());
                        }

                        LogUtils.d(TAG, "onWindowFocusChanged:long_url " + long_url);

                        if (responseString.contains("var url = '") && responseString.contains("Fid%3D")) {
                            String regx = "(Fid%3D)(.\\d*)";
                            Pattern pattern = Pattern.compile(regx);
                            Matcher matcher = pattern.matcher(responseString);
                            // 正则查找
                            if (matcher.find()) {
                                goods_id = matcher.group(2);
                            }
                            //back_url[0] = goods_id;
                        } else if (responseString.contains("var url = '") && responseString.contains("taobao.com/i")) {
                            Pattern pps = Pattern.compile("(taobao.com/i)([0-9]*[0-9])");
                            Matcher matchers = pps.matcher(responseString);
                            // 匹配到商品ID
                            if (matchers.find()) {
                                goods_id = matchers.group(2);
                            }
                            //back_url[0]=goods_id;
                        } else if (responseString.contains("var url = '") && responseString.contains("&key=") && responseString.contains("&dl_redirect=")) {
                            LogUtils.d(TAG, "进这里了！");
                            String regx = "&key=([0-9]*)";
                            Pattern pattern = Pattern.compile(regx);
                            Matcher matcher = pattern.matcher(responseString);
                            // 正则查找
                            if (matcher.find()) {
                                goods_id = matcher.group(matcher.groupCount());
                            }
                            LogUtils.d(TAG, "进这里了: googds_id " + goods_id);
                        } else if (responseString.contains("var url = '") && responseString.contains("taobao.com/i") && responseString.contains("\"pic\":") && responseString.contains("\"title\":")) {
                            // 有可能里边含有所有需要的数据，那就不用
                            String titles = "";
                            String pics = "";
                            Pattern pic_pt = Pattern.compile("\"pic\":\\s*\"(.*)\",");
                            Matcher tmp_matcher = pic_pt.matcher(responseString);
                            if (tmp_matcher.find()) {
                                pics = tmp_matcher.group(1);
                            }

                            Pattern title_pt = Pattern.compile("\\\"title\\\":\\s*\\\"(.*)\\\"");
                            Matcher mm_matcher = title_pt.matcher(responseString);
                            if (mm_matcher.find()) {
                                titles = mm_matcher.group(1);
                            }
                            Intent intent = new Intent(BaseActivity.this, DialogActivity.class);
                            intent.putExtra("url", Constants.AliGoodDescUrl + goods_id);
                            intent.putExtra("num_iid", goods_id);
                            intent.putExtra("pic", pics);
                            intent.putExtra("title", titles);
                            intent.putExtra("type", "tbweb");
                            syncGetDetail(goods_id, intent, "", "".equals(long_url) ? ori_url : long_url);
                            return;
                        }
                    }

                    LogUtils.d(TAG, "onWindowFocusChanged: goods_id::::" + goods_id);
                    Intent intent = new Intent(BaseActivity.this, DialogActivity.class);
                    intent.putExtra("url", "");
                    intent.putExtra("num_iid", goods_id);
                    intent.putExtra("commission", "");
                    intent.putExtra("type", "tb");
                    syncGetDetail(goods_id, intent, "", "".equals(long_url) ? ori_url : long_url);
                    clearClip();
                });
                tmp_thread.setName("商品扫码处理");
                tmp_thread.start();

            }
            // PC淘宝用手机购买二维码和手机淘宝复制链接会进入这里处理
            else if (temp.contains("m.tb.cn") && temp.contains("http")) {
                // 形式 https://m.tb.cn/a.ZRs8?scm=20140619.pc_detail.itemId.0&id=629608307932
                // 手机淘宝复制链接处理，如果没有结果则进    getDetail  进入第二套获取商品信息逻辑中处理.

                // 需要使用同步方法，不能锁死主线程
                String finalTemp = temp;
                Thread tmp_thread = new Thread(() -> {
                    /** 原始方法开始 */
                    if (finalTemp.contains("id=")) {
                        LogUtils.d(TAG, "PC淘宝用手机购买二维码: " + finalTemp);
                        Uri temp_uri = Uri.parse(finalTemp);
                        String num_iid = temp_uri.getQueryParameter("id");
                        if (!"".equals(num_iid)) {
                            Intent intent = new Intent(BaseActivity.this, DialogActivity.class);
                            intent.putExtra("url", "");
                            intent.putExtra("num_iid", num_iid);
                            intent.putExtra("commission", "");
                            intent.putExtra("type", "tb");
                            syncGetDetail(num_iid, intent, "", ori_url);
                            return;
                        }
                    }


                    // 形式 淘口令有两种 普通带链接，微信有http不带链接
                    LogUtils.d(TAG, "淘口令: " + finalTemp);
                    // 取其中的 链接
                    Pattern ps = Pattern.compile("(https:\\S*)");
                    Matcher ms = ps.matcher(finalTemp);

                    // 有网址，取商品ID
                    if (ms.find()) {
                        //String goods_id = getShortLinkGoodsID(ms.group());
                        String goods_id = "";
                        Map<String, String> result = SyncHttpUtils.get(ms.group());
                        if ("S".equals(result.get("code"))) {
                            String responseString = result.get("data");
                            //String goods_id = "";
                            if (responseString.contains("var url = '") && responseString.contains("Fid%3D")) {
                                String regx = "(Fid%3D)(.\\d*)";
                                Pattern pattern = Pattern.compile(regx);
                                Matcher matcher = pattern.matcher(responseString);
                                // 正则查找
                                if (matcher.find()) {
                                    goods_id = matcher.group(2);
                                }
                                //back_url[0] = goods_id;
                            }
                            if (responseString.contains("var url = '") && responseString.contains("taobao.com/i") && !responseString.contains("&id=")) {
                                Pattern pps = Pattern.compile("(taobao.com/i)([0-9]*[0-9])");
                                Matcher matchers = pps.matcher(responseString);
                                // 匹配到商品ID
                                if (matchers.find()) {
                                    goods_id = matchers.group(2);
                                }
                                //back_url[0]=goods_id;
                            }

                            if (responseString.contains("var url = '") && responseString.contains("taobao.com/i") && responseString.contains("&id=")) {
                                Pattern pps = Pattern.compile("(taobao.com/i).*.&id=([0-9]*[0-9])");
                                Matcher matchers = pps.matcher(responseString);
                                // 匹配到商品ID
                                if (matchers.find()) {
                                    goods_id = matchers.group(2);
                                }
                                //back_url[0]=goods_id;
                            }

                            // 有可能里边含有所有需要的数据，那就不用
                            if (responseString.contains("var url = '") && responseString.contains("taobao.com/i") && responseString.contains("\"pic\":") && responseString.contains("\"title\":")) {
                                String titles = "";
                                String pics = "";
                                Pattern pic_pt = Pattern.compile("\"pic\":\\s*\"(.*)\",");
                                Matcher tmp_matcher = pic_pt.matcher(responseString);
                                if (tmp_matcher.find()) {
                                    pics = tmp_matcher.group(1);
                                }

                                Pattern title_pt = Pattern.compile("\\\"title\\\":\\s*\\\"(.*)\\\"");
                                Matcher mm_matcher = title_pt.matcher(responseString);
                                if (mm_matcher.find()) {
                                    titles = mm_matcher.group(1);
                                }
                                Intent intent = new Intent(BaseActivity.this, DialogActivity.class);
                                intent.putExtra("url", Constants.AliGoodDescUrl + goods_id);
                                intent.putExtra("num_iid", goods_id);
                                intent.putExtra("pic", pics);
                                intent.putExtra("title", titles);
                                intent.putExtra("type", "tbweb");
                                syncGetDetail(goods_id, intent, "", ms.group());
                                return;
                            }
                        }

                        // 取GOODSID逻辑
                        Intent intent = new Intent(BaseActivity.this, DialogActivity.class);
                        intent.putExtra("url", "");
                        intent.putExtra("num_iid", goods_id);
                        intent.putExtra("commission", "");
                        intent.putExtra("type", "tb");
                        syncGetDetail(goods_id, intent, "", ms.group());
                        clearClip();
                    } else {
                        LogUtils.d(TAG, "解密淘口令: ");
                        //4.0 淘口令 没有网址
                        RequestParams tkl_request = new RequestParams();
                        tkl_request.put("content", finalTemp);
                        Map<String, String> result = SyncHttpUtils.post(Constants.TklDescrpt, tkl_request);
                        LogUtils.d(TAG, "解密淘口令: 结果" + result.get("code"));
                        if ("S".equals(result.get("code"))) {
                            try {
                                String responseString = result.get("data");
                                com.alibaba.fastjson.JSONObject back_obj = com.alibaba.fastjson.JSONObject.parseObject(responseString);
                                if ("0".equals(back_obj.getString("code"))) {
                                    String goods_url = back_obj.getJSONObject("data").getString("url");
                                    String goods_id = "";

                                    if (goods_url != null) {
                                        Uri tmp_uri = Uri.parse(goods_url);
                                        Pattern pps = Pattern.compile("[0-9]*[0-9]");
                                        Matcher matcher = pps.matcher(Objects.requireNonNull(tmp_uri.getLastPathSegment()));
                                        Intent intent = new Intent(BaseActivity.this, DialogActivity.class);
                                        // 匹配到商品ID
                                        if (matcher.find()) {
                                            goods_id = matcher.group();

                                        }
                                        intent.putExtra("url", back_obj.getJSONObject("data").getString("url"));
                                        intent.putExtra("title", back_obj.getJSONObject("data").getString("content"));
                                        intent.putExtra("num_iid", goods_id);
                                        intent.putExtra("commission", "");
                                        intent.putExtra("type", "tbweb");
                                        syncGetDetail(goods_id, intent, "", goods_url);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            runOnUiThread(() -> {
                                getGoodsMsgRequest(ori_url);
                                clearClip();
                            });
                        }
                    }
                    /** 原始方法结束 */
                });
                tmp_thread.start();
                clearClip();
                try {
                    Snackbar snackbar = Snackbar.make(getCurrentFocus(), "您复制了" + temp + "，处理中。", Snackbar.LENGTH_LONG);
                    snackbar.setAction("好", v1 -> {
                        snackbar.dismiss();
                    });
                    snackbar.setActionTextColor(Color.parseColor("#fff760"));
                    snackbar.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            /*淘宝PC版网址*/
            else if (temp.contains("item.taobao.com")) {
                Uri uri = Uri.parse(temp);
                if (uri.getQueryParameter("id") != null && !"".equals(uri.getQueryParameter("id"))) {

                    Intent intent = new Intent(BaseActivity.this, DialogActivity.class);
                    intent.putExtra("url", "");
                    intent.putExtra("num_iid", uri.getQueryParameter("id"));
                    intent.putExtra("commission", "");
                    intent.putExtra("type", "tb");
                    getDetail(uri.getQueryParameter("id"), intent, "", ori_url);

                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("Label", "");
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                }
            } else if (temp.contains("product.suning.com") || temp.contains("m.suning.com")) {
                Uri uri = Uri.parse(temp);
                if (uri.getLastPathSegment().endsWith(".html")) {
                    LogUtils.d(TAG, "onCreate: " + uri.getLastPathSegment());
                    String goods_id = uri.getLastPathSegment().substring(0, uri.getLastPathSegment().indexOf(".ht"));
                    getSnGoodsMsg(getComeActivity(), goods_id);
                }
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", "");
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);

            } else if (temp.contains("mobile.yangkeduo.com")) {
                Uri uri = Uri.parse(temp);
                if (uri.getQueryParameter("goods_id") != null && !"".equals(uri.getQueryParameter("goods_id"))) {
                    getPddDetail(uri.getQueryParameter("goods_id"));
                }
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", "");
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
            } else if (temp.contains("vip.com/product")) {
                getWPGoodsRequest(StringUtils.substringBetween(temp, "product-", ".html"));
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", "");
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
            } else if (temp.contains("p.pinduoduo.com")) {
                zhuanlian(temp);
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", "");
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
            } else if (temp.contains("item.m.jd.com/product") || temp.contains("item.jd.com")) {
                String goods_id = "";
                if (temp.contains("item.m.jd.com/product")) {
                    goods_id = StringUtils.substringBetween(temp, "product/", ".html");
                } else {
                    goods_id = StringUtils.substringBetween(temp, ".jd.com/", ".html");
                }
                getJdGoodsRequest(goods_id);
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", "");
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
            } else {
                if (temp.contains("-----------------------")) {
                    return;
                }
                LogUtils.d(TAG, "getGoodsMsgRequest: ");
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", "");
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                getGoodsMsgRequest(temp);
            }
        }
    }

    private void getWPGoodsRequest(String goodsId) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("goodsId", goodsId.split("-")[1]);
        HttpUtils.post(Constants.APP_IP + "/api/WPH/goodsInfo", BaseActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    final JSONObject object1 = new JSONObject(responseString).getJSONArray("data").getJSONObject(0);
                    Intent intent = new Intent(BaseActivity.this, DialogActivity.class);
                    Gson gson = new Gson();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("goods", gson.fromJson(object1.toString(), Wphbean.class));
                    intent.putExtra("goods", bundle);
                    intent.putExtra("pic", object1.getString("goodsMainPicture"));
                    intent.putExtra("title", object1.getString("goodsName"));
                    intent.putExtra("vipPrice", object1.getString("vipPrice"));
                    intent.putExtra("marketPrice", object1.getString("marketPrice"));
                    intent.putExtra("commission", object1.getString("commission"));
                    intent.putExtra("type", "vip");
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 判断手机是否是魅族
     *
     * @return
     */
    private static boolean isFlyme() {
        try {
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }

    public void showTipDialog3(String title, String content, final onClickListener listener, final onClickListener cancle, String okStr, String cancleStr) {
        if (mMaterialDialog == null) {
            mMaterialDialog = new MaterialDialog(this);//实例化对话框
        }
        if (cancle != null) {
            mMaterialDialog.setNegativeButton(cancleStr, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cancle != null) {
                        cancle.onClickSure();
                    }
                    mMaterialDialog.dismiss();
                }
            });
        }
        mMaterialDialog.setTitle(title)
                .setMessage(content)
                .setPositiveButton(okStr, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onClickSure();
                        }
                        mMaterialDialog.dismiss();
                    }
                })
                .setCanceledOnTouchOutside(true)
                .setOnDismissListener(
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                mMaterialDialog = null;
                            }
                        })
                .show();
    }

    /**
     * 判断手机是否是小米
     *
     * @return
     */
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    private static boolean isMIUI() {
        Properties prop = new Properties();
        boolean isMIUI;
        try {
            prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        } catch (IOException e) {
//            e.printStackTrace();
            return false;
        }
        isMIUI = prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
//        SPUtils.getInstance().putCacheData("isMIUI",isMIUI);//保存是否MIUI
        return isMIUI;
    }

    /**
     * 初始化ui
     **/
    protected abstract void initUI();

    /**
     * 初始化数据
     **/
    protected abstract void initData();

    /**
     * 初始化监听
     **/
    protected abstract void initListener();

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    /**
     * 保存activity状态
     **/
    protected void saveInstanceState(Bundle outState) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onBack(View v) {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            startActivity(intent);
            return false;
        } else if (keyCode == KeyEvent.KEYCODE_BACK && this instanceof MainActivity) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        onReceiverBroadCastMessage();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int i = 0; i < grantResults.length; i++) {
//判断权限的结果，如果有被拒绝，就return
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                        T.showShort(BaseActivity.this, "您已可以在设置里重新打开该权限");
                    }
                }
            }
        }
    }

    private Handler handlers = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            T.showShort(getComeActivity(), "该商品没有优惠券或奖");
            super.handleMessage(msg);
        }
    };

    /**
     * @属性:获取京东推送商品详情
     * @开发者:wmm
     * @时间:2018/12/11 9:50
     */
    protected void getJdGoodsRequest(String id) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("apikey", Constants.JD_APP_KEY_NEW);
        requestParams.put("goods_ids", id);
        requestParams.put("isunion", "1");

        HttpUtils.get(BaseActivity.this, Constants.JDNEWGOODS_LIST + "?" + requestParams.toString(), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogUtils.d(TAG, "获取京东商品失败: " + statusCode + "_responseString:" + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject object1 = new JSONObject(responseString);
                    String check_data = object1.getString("data");
                    JSONArray array = null;

                    if (!check_data.equals("")) {
                        array = object1.getJSONObject("data").getJSONArray("data");
                    }

                    if (array == null || array.length() == 0) {
                        LogUtils.d(TAG, "onResponse: 正在处理剪切板中的商品信息");
                        // 没有联盟产品信息，从京东官网取;
                        String product_url = "https://item.m.jd.com/product/" + id + ".html";
                        showLoadingDialog("正在处理剪切板中的商品信息，请稍后！");
                        HttpUtils.get(BaseActivity.this, product_url, new TextHttpResponseHandler() {
                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                LogUtils.d(TAG, "onFailure: " + statusCode + "___" + responseString);
                                closeLoadingDialog();
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                closeLoadingDialog();
                                String s_str = "\"item\":";
                                String e_str = "});";
                                int s_int = 0;
                                int e_int = 0;
                                String t2 = org.apache.commons.lang3.StringUtils.substringBetween(responseString, s_str, e_str);
                                if (t2 != null) {
                                    LogUtils.d(TAG, "onSuccess: " + t2);
                                }
                                if (responseString.contains(s_str)) {
                                    s_int = responseString.indexOf(s_str);
                                    e_int = responseString.indexOf(e_str, s_int);
                                    String t1 = responseString.substring(s_int + s_str.length(), e_int);
                                    try {
                                        com.alibaba.fastjson.JSONObject t1_obj = com.alibaba.fastjson.JSONObject.parseObject(t1);
                                        String product_title = t1_obj.getString("skuName") == null ? t1_obj.getString("pName") : t1_obj.getString("skuName");
                                        String product_pic = t1_obj.getJSONArray("image").size() > 0 ? "https://m.360buyimg.com/mobilecms/s750x750_j" + t1_obj.getJSONArray("image").getString(0) : "";
                                        Intent intent = new Intent(getComeActivity(), DialogActivity.class);
                                        intent.putExtra("type", "jdweb");
                                        intent.putExtra("title", product_title);
                                        intent.putExtra("pic", product_pic);
                                        intent.putExtra("url", product_url);
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        showToastLong("解析JSON失败！");
                                    }
                                }
                            }
                        });
                        return;
                    }
                    for (int i = 0; i < 1; i++) {
                        MyGoodsResp resp = new Gson().fromJson(array.getJSONObject(i).toString(), MyGoodsResp.class);
                        Intent intent = new Intent(getComeActivity(), DialogActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("goods", resp);
                        intent.putExtra("goods", bundle);
                        intent.putExtra("pic", resp.imageInfo.getImageList()[0].getUrl());
                        intent.putExtra("title", resp.getSkuName());
                        intent.putExtra("commission", "");
                        intent.putExtra("type", "jd");
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        /*OkHttpUtils.getInstance().get(Constants.JDNEWGOODS_LIST + "?" + requestParams.toString(), new Callback() {
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
                    String check_data = object1.getString("data");
                    JSONArray array = null;

                    if (!check_data.equals("")) {
                        array = object1.getJSONObject("data").getJSONArray("data");
                    }

                    if (array == null || array.length() == 0) {
                        LogUtils.d(TAG, "onResponse: 正在处理剪切板中的商品信息");
                        // 没有联盟产品信息，从京东官网取;
                        String product_url = "https://item.m.jd.com/product/" + id + ".html";
                        HttpUtils.get(product_url, new TextHttpResponseHandler() {
                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                LogUtils.d(TAG, "onFailure: "+statusCode+"___"+responseString);
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                String s_str = "\"item\":";
                                String e_str="});";
                                int s_int = 0;
                                int e_int = 0;
                                if (responseString.contains(s_str)) {
                                    s_int = responseString.indexOf(s_str);
                                    e_int = responseString.indexOf(e_str,s_int);
                                    String t1 = responseString.substring(s_int+s_str.length(),e_int);
                                    try{
                                        com.alibaba.fastjson.JSONObject  t1_obj = com.alibaba.fastjson.JSONObject.parseObject(t1);
                                        String product_title = t1_obj.getString("skuName") == null ? t1_obj.getString("pName") : t1_obj.getString("skuName");
                                        String product_pic = t1_obj.getJSONArray("image").size()>0 ? "https://m.360buyimg.com/mobilecms/s750x750_j"+t1_obj.getJSONArray("image").getString(0) : "";
                                        Intent intent = new Intent(getComeActivity(),DialogActivity.class);
                                        intent.putExtra("type","jdweb");
                                        intent.putExtra("title",product_title);
                                        intent.putExtra("pic",product_pic);
                                        intent.putExtra("url",product_url);
                                        startActivity(intent);
                                    }
                                    catch (Exception e){
                                        showToastLong("解析JSON失败！");
                                    }
                                }
                            }
                        });
                        return;
                    }
                    for (int i = 0; i < 1; i++) {
                        MyGoodsResp resp = new Gson().fromJson(array.getJSONObject(i).toString(), MyGoodsResp.class);
                        Intent intent = new Intent(getComeActivity(), DialogActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("goods", resp);
                        intent.putExtra("goods", bundle);
                        intent.putExtra("pic", resp.imageInfo.getImageList()[0].getUrl());
                        intent.putExtra("title", resp.getSkuName());
                        intent.putExtra("commission", "");
                        intent.putExtra("type", "jd");
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });*/
////        JAnalyticsInterface.onEvent(this,new CountEvent("jd_copy_search_lq"));
//        String SERVER_URL = "https://router.jd.com/api";
//        String appKey = "094f4eeba5cd4108ba1cd4e7a6d93cc6";
//        String appSecret = "852e6f678c3b4e27a89cc9dc6e67ee01";
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
//                        handlers.sendEmptyMessage(0);
//                        return;
//                    }
//                    if (response.getData().length <= 0) {
//                        handlers.sendEmptyMessage(0);
//                        return;
//                    }
//                    try {
//                    } catch (Exception e) {
//                        LogUtils.d("jddddj", "3" + e.toString());
//
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
     * @时间:2018/11/22 9:00
     */
    protected void getPddDetail(final String goods_id) {
        JAnalyticsInterface.onEvent(this, new CountEvent("pdd_copy_search_lq"));
        RequestParams requestParams = new RequestParams();
        requestParams.put("goods_id", goods_id);
        HttpUtils.post(Constants.GET_PDD_DETAIL, BaseActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (getComeActivity().isDestroyed())
                    return;
                try {
                    if (new JSONObject(responseString).getInt("code") != 0) {
                        T.showShort(getComeActivity(), "该商品没有优惠券或奖");
                        return;
                    }
                    DecimalFormat df = new DecimalFormat("0.00");
                    JSONObject object = new JSONObject(responseString).getJSONObject("data").getJSONObject("goods_details");
                    Intent intent = new Intent(BaseActivity.this, DialogActivity.class);
                    Gson gson = new Gson();
                    double tem = (Double.valueOf(object.getString("min_group_price")) - Double.valueOf(object.getString("coupon_discount"))) * Double.valueOf(df.format(Double.valueOf(object.getString("promotion_rate")) / 1000));
                    object.put("commission", df.format(tem * SPUtils.getIntData(getComeActivity(), "rate", 0) / 100));
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("goods", gson.fromJson(object.toString().replace("goods_gallery_urls", "imagss"), PDDBean.class));
                    intent.putExtra("goods", bundle);
                    intent.putExtra("pic", object.getString("goods_thumbnail_url"));
                    intent.putExtra("title", object.getString("goods_name"));
                    intent.putExtra("commission", "");
                    intent.putExtra("type", "pdd");
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void checkVersion() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.MESSAGE_ARTICLE_VERSION_URL, BaseActivity.this, requestParams, new onOKJsonHttpResponseHandler<VersionInformationBean>(new TypeToken<Response<VersionInformationBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
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
            public void onSuccess(int statusCode, Response<VersionInformationBean> datas) {
                // Log.d(TAG, "获取版本信息成功: " + datas.toString());
                if (datas.isSuccess()) {
                    // Log.d(TAG, "onSuccess: " + datas.getData().toString());
                    final VersionInformationBean data = datas.getData();

                    if (data != null) {
                        Constants.SHARE_URL = data.getShare_url();
                        Constants.VSUP_URL = data.getVy_url_s();
                        Constants.V_URL = data.getVy_url_c();
                        Constants.SHARE_URL_REGISTER = data.getShare_url_register();
                        SPUtils.saveStringData(getComeActivity(), "is_tm_11", data.is_tb_11);
                        SPUtils.saveStringData(getComeActivity(), "down_type", data.down_type);
                        SPUtils.saveStringData(getComeActivity(), "share_url_vip", data.share_url_vip);
                        SPUtils.saveStringData(getComeActivity(), "default_avatar", data.default_avatar);
                        SPUtils.saveStringData(getComeActivity(), "down_android_yyb", data.down_android_yyb);
                        SPUtils.saveStringData(getComeActivity(), "down_ios", data.down_ios);
                        SPUtils.saveStringData(getComeActivity(), "is_auth", data.user_auth_code_exist);
//                        &&!"1".equals(SPUtils.getStringData(getComeActivity(),data.getVersion(),"0"))
                        // Log.d(TAG, "onSuccess: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));

//                        String[] tmp_url = data.getDown_android().split("/");

                        if (CommonUtils.compareVersion(APKVersionCodeUtils.getVerName(getComeActivity()), data.getVersion()) == -1) {
                            // 还需要舍弃后边带问号的部分
                            Uri tmp_urls = Uri.parse(data.getDown_android());

                            CommonDialog commonDialog = new CommonDialog(getComeActivity());
                            commonDialog.setTitle("发现新版本").setMessage(data.getContent())
                                    .setSubmit("下载", true)
                                    .setCancelButton("去应用宝下载", false)
                                    .setShowCloseButton(false)
                                    .setCancelable(false)
                                    .setListener(new CommonDialog.CommonDialogListener() {
                                        @Override
                                        public void OnSubmit(AlertDialog dialog) {
                                            LinearLayout button_content = commonDialog.getButtonContent();
                                            button_content.setVisibility(View.GONE);
                                            LinearLayout progress_content = commonDialog.getProgressContent();
                                            progress_content.setVisibility(View.VISIBLE);
                                            Button install_bt = commonDialog.getInstallButton();
                                            ProgressBar progressBar = commonDialog.getProgressBar();
                                            progressBar.setVisibility(View.VISIBLE);

                                            DownloadUtil.get().download(data.getDown_android(), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath(), tmp_urls.getLastPathSegment(), new DownloadUtil.OnDownloadListener() {
                                                @Override
                                                public void onDownloadSuccess(File file) {
                                                    // Log.d(TAG, "onDownloadSuccess: ");
                                                    InstallUtil util = new InstallUtil(getComeActivity(), file.getPath());
                                                    ((BaseActivity) getComeActivity()).setOnActivityResultLisntener(new onActivityResultLisntener() {

                                                        @Override
                                                        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                                                            if (resultCode == RESULT_OK && requestCode == InstallUtil.UNKNOWN_CODE) {
                                                                util.install();//再次执行安装流程，包含权限判等
                                                            }
                                                        }
                                                    });
                                                    runOnUiThread(() -> {
                                                        progressBar.setVisibility(View.GONE);
                                                        install_bt.setVisibility(View.VISIBLE);
                                                        install_bt.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                runOnUiThread(() -> {
                                                                    util.install();
                                                                });

                                                            }
                                                        });

                                                        util.install();
                                                    });

                                                }

                                                @Override
                                                public void onDownloading(int progress) {
                                                    // Log.d(TAG, "onDownloading: " + progress);
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                        progressBar.setProgress(progress, true);
                                                    } else {
                                                        progressBar.setProgress(progress);
                                                    }
                                                }

                                                @Override
                                                public void onDownloadFailed(Exception e) {
                                                    // Log.d(TAG, "onDownloadFailed: " + e.getMessage());
                                                    runOnUiThread(() -> {
                                                        showToast("下载失败，请重新尝试。");
                                                        progress_content.setVisibility(View.GONE);
                                                        button_content.setVisibility(View.VISIBLE);
                                                    });
                                                }
                                            });
                                        }

                                        @Override
                                        public void OnCancel(AlertDialog dialog) {
                                            Intent intent = new Intent();
                                            intent.setAction("android.intent.action.VIEW");
                                            Uri content_url = Uri.parse(data.down_android_yyb);
                                            intent.setData(content_url);
                                            getComeActivity().startActivity(intent);
                                        }

                                        @Override
                                        public void OnDismiss() {

                                        }

                                        @Override
                                        public void OnClose(AlertDialog dialog) {

                                        }
                                    });

                            if (!SPUtils.getBoolean(getComeActivity(), "forced_to_update", true)) {
                                commonDialog.setShowCloseButton(true).setCanceledOnTouchOutside(true).setCancelable(true);
                            } else {
                                commonDialog.setShowCloseButton(false).setCanceledOnTouchOutside(false).setCancelable(false);
                            }
                            commonDialog.show();
                        }
//                        if (CommonUtils.compareVersion(APKVersionCodeUtils.getVerName(getComeActivity()), data.getVersion()) == -1) {
//                            AllenVersionChecker
//                                    .getInstance()
//                                    .downloadOnly(
//                                            UIData.create().setTitle(getString(R.string.app_name)).setContent("发现新版本\n" + data.getContent()).setDownloadUrl(data.getDown_android())
//                                    ).setSilentDownload(false).setShowNotification(true).setForceRedownload(true).setOnCancelListener(new OnCancelListener() {
//                                @Override
//                                public void onCancel() {
//                                    runOnUiThread(()->{
//                                        checkVersion();
//                                    });
//                                }
//                            }).executeMission(getComeActivity());
//                        }
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

    protected void getGoodsMsgRequest(final String tkl) {
        LogUtils.d(TAG, "去服务器搜索淘口令: " + tkl);
        JAnalyticsInterface.onEvent(this, new CountEvent("tb_copy_search_lq"));
        String url = "";
        RequestParams requestParams = new RequestParams();
        requestParams.put("tkl", tkl);
        HttpUtils.post(Constants.SEARCHTKL, BaseActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    com.alibaba.fastjson.JSONObject object = com.alibaba.fastjson.JSONObject.parseObject(responseString);
                    if (object.getIntValue("code") == 0) {
                        LogUtils.d(TAG, "getGoodsMsgRequest: 服务器返回有正常数据");
                        object = object.getJSONObject("data");
                        Intent intent = new Intent(BaseActivity.this, DialogActivity.class);
                        intent.putExtra("url", tkl);
                        intent.putExtra("num_iid", object.getString("num_iid"));
                        intent.putExtra("commission", "");
                        intent.putExtra("type", "tb");
//                        startActivity(intent);
                        getDetail(object.getString("num_iid"), intent, tkl, "");
                    } else {
                        LogUtils.d(TAG, "getGoodsMsgRequest: 服务器返回无正常数据");
                        com.alibaba.fastjson.JSONObject finalObject = object;
                        Thread tmp_thread = new Thread(() -> {
                            // 对于没有结果的商品该 如何处理。
                            LogUtils.d(TAG, "解密淘口令: ");
                            //4.0 淘口令 没有网址
                            RequestParams tkl_request = new RequestParams();
                            tkl_request.put("content", tkl);
                            Map<String, String> result = SyncHttpUtils.post(Constants.TklDescrpt, tkl_request);
                            LogUtils.d(TAG, "解密淘口令: 结果" + result.get("code"));
                            if ("S".equals(result.get("code"))) {
                                try {
                                    String tkl_responseString = result.get("data");
                                    com.alibaba.fastjson.JSONObject back_obj = com.alibaba.fastjson.JSONObject.parseObject(tkl_responseString);
                                    if ("0".equals(back_obj.getString("code"))) {
                                        String goods_url = back_obj.getJSONObject("data").getString("url");
                                        String goods_id = "";

                                        if (goods_url != null) {
                                            Uri tmp_uri = Uri.parse(goods_url);
                                            Pattern pps = Pattern.compile("[0-9]*[0-9]");
                                            Matcher matcher = pps.matcher(Objects.requireNonNull(tmp_uri.getLastPathSegment()));
                                            Intent intent = new Intent(BaseActivity.this, DialogActivity.class);
                                            // 匹配到商品ID
                                            if (matcher.find()) {
                                                goods_id = matcher.group();

                                            }
                                            intent.putExtra("url", back_obj.getJSONObject("data").getString("url"));
                                            intent.putExtra("num_iid", goods_id);
                                            intent.putExtra("commission", "");
                                            intent.putExtra("type", "tbweb");
                                            syncGetDetail(goods_id, intent, tkl, goods_url);
                                        }
                                    }
                                    return;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            if (tkl.startsWith("【") && tkl.contains("http") && tkl.contains("¥")) {
                                //获取剪贴板管理器：
                                Bundle bundle = new Bundle();
                                bundle.putString("content", tkl.substring(tkl.indexOf("【") + 1, tkl.lastIndexOf("】")));
                                bundle.putInt("type", 0);//1高级搜索 0 普通搜索
                                openActivity(SearchResultActivity.class, bundle);
                                showToast(finalObject.getString("msg"));
                            } else {
                                Intent intent = new Intent(getComeActivity(), DialogActivity2.class);
                                intent.putExtra("search", tkl);
                                startActivity(intent);
                            }
                        });
                        tmp_thread.start();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * @param num_iid     商品ID
     * @param intent      意图
     * @param tkl         淘口令
     * @param originalUrl 原始网址
     */
    private void getDetail(String num_iid, final Intent intent, final String tkl, final String originalUrl) {

        LogUtils.d(TAG, "getDetail: " + originalUrl);
        RequestParams requestParams = new RequestParams();
        requestParams.put("num_iid", num_iid);

        HttpUtils.post(Constants.HOME_TBK_GETGOODSMSG_URL, BaseActivity.this, requestParams, new onOKJsonHttpResponseHandler<PromotionDetailsBean>(new TypeToken<Response<PromotionDetailsBean>>() {
        }) {
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
            public void onSuccess(int statusCode, Response<PromotionDetailsBean> datas) {
                if (getComeActivity().isDestroyed())
                    return;
                if (datas.isSuccess()) {
                    PromotionDetailsBean data = datas.getData();
                    if (data != null) {
                        intent.putExtra("pic", data.getPict_url());
                        intent.putExtra("title", data.getTitle());
                        intent.putExtra("one", data.getCommission());
                        intent.putExtra("two", data.getZk_final_price());
                        intent.putExtra("three", data.getCoupon_amount());
                        startActivity(intent);
                    }
                } else {

                    if (!"".equals(num_iid)) {
                        LogUtils.d(TAG, "onSuccess: 异步版本——有产品ID");
                        Map<String, String> result = SyncHttpUtils.get(Constants.AliGoodDescUrl + num_iid);//getAliGoodDesc(num_iid);
                        // Log.d(TAG, "onSuccess: " + result.get("data"));
                        if ("S".equals(result.get("code"))) {
                            String titles = "";
                            String pics = "";
                            if (result.get("data").contains("var g_config")) {
                                LogUtils.d(TAG, "onCreate: 走第一套");
                                String mm = "title\\s*:\\s'\\S*'";
                                Pattern pattern = Pattern.compile(mm);
                                Matcher matcher = pattern.matcher(result.get("data"));
                                if (matcher.find()) {
                                    titles = StringUtils.trim(StringUtils.substringBetween(matcher.group(), "'", "'"));
                                }
                                String reg_pic = "pic\\s*:\\s'[\\S|\\s]*'";
                                pattern = Pattern.compile(reg_pic);
                                matcher = pattern.matcher(result.get("data"));
                                if (matcher.find()) {
                                    pics = StringUtils.trim(StringUtils.substringBetween(matcher.group(), "'", "'"));
                                    if (!pics.startsWith("http")) {
                                        if (pics.startsWith("//")) {
                                            pics = "https:" + pics;
                                        } else {
                                            pics = "https://" + pics;
                                        }
                                    }
                                }
                            } else if (result.get("data").contains("\"code\":\"50001\"")) {
                                // Log.d(TAG, "onSuccess: 无结果。不能用");
                            } else {
                                LogUtils.d(TAG, "onCreate: 走第二套");
                                String mm = "(\"title\"\\s*:\\s*\")(.*)\"";
                                Pattern pattern = Pattern.compile(mm);
                                Matcher matcher = pattern.matcher(result.get("data"));
                                if (matcher.find()) {
                                    titles = StringUtils.substring(matcher.group(2), 0, matcher.group(2).indexOf("\""));
                                }
                                String reg_pic = "(\"imgVedioPic\"\\s*:\\s*\")(.*)\"";
                                pattern = Pattern.compile(reg_pic);
                                matcher = pattern.matcher(result.get("data"));
                                if (matcher.find()) {
                                    pics = StringUtils.substring(matcher.group(2), 0, matcher.group(2).indexOf("\""));
                                    if (!pics.startsWith("http")) {
                                        if (pics.startsWith("//")) {
                                            pics = "https:" + pics;
                                        } else {
                                            pics = "https://" + pics;
                                        }
                                    }
                                }
                            }
                            LogUtils.d(TAG, "onSuccess: " + titles);
                            LogUtils.d(TAG, "onSuccess: " + pics);
                            titles = SyncHttpUtils.decode(titles);
                            intent.putExtra("pic", pics);
                            intent.putExtra("title", titles);
                            intent.putExtra("one", "");
                            intent.putExtra("two", "");
                            intent.putExtra("three", "");
                            intent.putExtra("url", Constants.AliGoodDescUrl + num_iid);
                            intent.putExtra("type", "tbweb");
                            startActivity(intent);
                            return;
                        }
                    }

                    // 最终去按原始淘口令为关键字取

                    if (tkl.startsWith("【") && tkl.contains("http") && tkl.contains("¥")) {
                        //获取剪贴板管理器：
                        Bundle bundle = new Bundle();
                        bundle.putString("content", tkl.substring(tkl.indexOf("【") + 1, tkl.lastIndexOf("】")));
                        bundle.putInt("type", 0);//1高级搜索 0 普通搜索
                        openActivity(SearchResultActivity.class, bundle);
                    } else {
                        Intent intent = new Intent(getComeActivity(), DialogActivity2.class);
                        intent.putExtra("search", tkl);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    // getDetail同步版本
    private void syncGetDetail(String num_iid, final Intent intent, final String tkl, final String originalUrl) {

        LogUtils.d(TAG, "syncGetDetail originalUrl: " + originalUrl);
        LogUtils.d(TAG, "syncGetDetail num_iid: " + num_iid);
        LogUtils.d(TAG, "syncGetDetail tkl: " + tkl);
        RequestParams requestParams = new RequestParams();
        requestParams.put("num_iid", num_iid);
        Map<String, String> post_result = SyncHttpUtils.post(Constants.HOME_TBK_GETGOODSMSG_URL, requestParams);
        if (post_result == null) return;
        com.alibaba.fastjson.JSONObject result = null;
        try {
            result = com.alibaba.fastjson.JSONObject.parseObject(post_result.get("data"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        // 服务器返回正确数据
        if ("0".equals(result.getString("code"))) {
            LogUtils.d(TAG, "syncGetDetail: 服务器返回正确数据");
            com.alibaba.fastjson.JSONObject data = result.getJSONObject("data");
            LogUtils.d(TAG, "syncGetDetail: " + data.toJSONString());
            intent.putExtra("pic", data.getString("pict_url"));
            intent.putExtra("title", data.getString("title"));
            intent.putExtra("one", data.getString("commission"));
            intent.putExtra("two", data.getString("zk_final_price"));
            intent.putExtra("three", data.getString("coupon_amount"));
            startActivity(intent);
            return;
        }

        if (!"".equals(intent.getStringExtra("pic")) && intent.getStringExtra("pic") != null && !"".equals(intent.getStringExtra("title")) && intent.getStringExtra("title") != null) {
            startActivity(intent);
            return;
        }

        // 从淘宝官网取商品信息
        if (!"".equals(num_iid)) {
            LogUtils.d(TAG, "onSuccess: 有产品ID");
            Map<String, String> result2 = SyncHttpUtils.get(Constants.AliGoodDescUrl + num_iid);//getAliGoodDesc(num_iid);
            // Log.d(TAG, "syncGetDetail: " + result2.get("data"));
            if (result2 == null) return;
            if ("S".equals(result2.get("code"))) {
                // 做到这里了，有的没有 下边的这些，需要检测  "title": "(.*)" ，"imgVedioPic": "(.*)" 。
                String titles = "";
                String pics = "";
                if (result2.get("data").contains("var g_config")) {
                    LogUtils.d(TAG, "onCreate: 走第一套");
                    String mm = "title\\s*:\\s'\\S*'";
                    Pattern pattern = Pattern.compile(mm);
                    Matcher matcher = pattern.matcher(result2.get("data"));
                    if (matcher.find()) {
                        titles = StringUtils.trim(StringUtils.substringBetween(matcher.group(), "'", "'"));
                    }
                    String reg_pic = "pic\\s*:\\s'[\\S|\\s]*'";
                    pattern = Pattern.compile(reg_pic);
                    matcher = pattern.matcher(result2.get("data"));
                    if (matcher.find()) {
                        pics = StringUtils.trim(StringUtils.substringBetween(matcher.group(), "'", "'"));
                        if (!pics.startsWith("http")) {
                            if (pics.startsWith("//")) {
                                pics = "https:" + pics;
                            } else {
                                pics = "https://" + pics;
                            }
                        }
                    }
                } else if (result2.get("data").contains("class=\"login-adlink\"")) {
                    // 此处需要登陆后方可查看商品详情信息
                    runOnUiThread(() -> {
                        showToast("此商品无优惠信息。");
                    });
                    return;
                } else {
                    LogUtils.d(TAG, "onCreate: 走第二套");
                    String mm = "(\"title\"\\s*:\\s*\")(.*)\"";
                    Pattern pattern = Pattern.compile(mm);
                    Matcher matcher = pattern.matcher(result2.get("data"));
                    if (matcher.find()) {
                        titles = StringUtils.substring(matcher.group(2), 0, matcher.group(2).indexOf("\""));
                    }
                    String reg_pic = "(\"imgVedioPic\"\\s*:\\s*\")(.*)\"";
                    pattern = Pattern.compile(reg_pic);
                    matcher = pattern.matcher(result2.get("data"));
                    if (matcher.find()) {
                        pics = StringUtils.substring(matcher.group(2), 0, matcher.group(2).indexOf("\""));
                        if (!pics.startsWith("http")) {
                            if (pics.startsWith("//")) {
                                pics = "https:" + pics;
                            } else {
                                pics = "https://" + pics;
                            }
                        }
                    }
                }

                LogUtils.d(TAG, "onSuccess: " + titles);
                LogUtils.d(TAG, "onSuccess: " + pics);

                titles = SyncHttpUtils.decode(titles);
                intent.putExtra("pic", pics);
                if (titles != null) intent.putExtra("title", titles);
                intent.putExtra("one", "");
                intent.putExtra("two", "");
                intent.putExtra("three", "");
                intent.putExtra("url", Constants.AliGoodDescUrl + num_iid);
                intent.putExtra("type", "tbweb");
                startActivity(intent);
                return;
            }
        }

        if (tkl.startsWith("【") && tkl.contains("http") && tkl.contains("¥")) {
            //获取剪贴板管理器：
            Bundle bundle = new Bundle();
            bundle.putString("content", tkl.substring(tkl.indexOf("【") + 1, tkl.lastIndexOf("】")));
            bundle.putInt("type", 0);//1高级搜索 0 普通搜索
            openActivity(SearchResultActivity.class, bundle);
        } else {

            if (!"".equals(tkl)) {
                Intent intent_other = new Intent(getComeActivity(), DialogActivity2.class);
                intent_other.putExtra("search", tkl);
                startActivity(intent_other);
                return;
            }

            // 这里 num_iid 肯定为空

            Map<String, String> res = SyncHttpUtils.get(originalUrl);
//            LogUtils.d(TAG, "syncGetDetail: " + res.get("code") + "," + res.get("data"));

            if (res.get("code").equals("S")) {
                String clickUrl = StringUtils.substringBetween(res.get("data"), "var url = 'https:", "';");
                String extraData = StringUtils.substringBetween(res.get("data"), "var extraData =", ";");
                String extraData_jie = cutString(extraData, "'", "'");

                String tmp_num_iid = StringUtils.substringBetween(clickUrl, "taobao.com/i", ".htm?");
                if (!"".equals(tmp_num_iid)) {
                    syncGetDetail(tmp_num_iid, intent, tkl, originalUrl);
                    return;
                }

                com.alibaba.fastjson.JSONObject extraDataJson = com.alibaba.fastjson.JSONObject.parseObject(extraData_jie);
                handler.post(() -> {
                    Intent start_intent = new Intent(BaseActivity.this, DialogActivity.class);
                    start_intent.putExtra("pic", extraDataJson.getString("pic"));
                    start_intent.putExtra("title", extraDataJson.getString("title"));
                    start_intent.putExtra("one", "");
                    start_intent.putExtra("two", "");
                    start_intent.putExtra("three", "");
                    start_intent.putExtra("url", "https:" + clickUrl);
                    start_intent.putExtra("type", "opentbweb");
                    startActivity(start_intent);
                });
            }
        }
    }

    public String cutString(String str, String start, String end) {
        /*if (isBlank(str)) {
            return str;
        }*/
        String reg = start + "(.*)" + end;
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            str = matcher.group(1);
        }
        return str;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 从栈中移除当前activity
        if (listActivity.contains(this)) {
            listActivity.remove(this);
        }
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this);
        JAnalyticsInterface.onPageEnd(this, this.getClass().getCanonicalName());
    }

    /**
     * (返回TextView或者EditText的值)
     *
     * @param textView TextView对象
     */
    public String getTextEditValue(TextView textView) {

        return textView.getText().toString().trim();
    }

    /**
     * 判断EditText是否没有输入值
     *
     * @param editText EditText对象
     */
    public boolean isEditTextNoValue(EditText editText) {
        return (null == editText) || (editText.getText().toString().length() == 0);
    }

    /**
     * short吐司
     *
     * @param msg
     * @author NML
     */
    public void showToast(String msg) {
        if ("用户不存在".equals(msg)) {
            T.showShort(getApplicationContext(), "登录信息已过期");
            openActivity(WelActivity.class);
        } else {
            T.showShort(getApplicationContext(), msg);
        }
    }

    /**
     * long吐司
     *
     * @param text
     */
    public void showToastLong(String text) {
        try {
            T.showLong(getApplicationContext(), text);
        } catch (Exception e) {
            runOnUiThread(() -> {
                showToastLong(text);
            });
        }
    }

    /**
     * 显示圆形加载进度对话框
     */
    protected void showLoadingDialog() {
        if (!getComeActivity().isDestroyed() || !getComeActivity().isFinishing()) {
            if (loadingDialog == null) {
                loadingDialog = LoadingDialog.createDialog(this);
                loadingDialog.setMessage("正在加载..");
                loadingDialog.setCanceledOnTouchOutside(false);
            }
            loadingDialog.show();
        }
    }

    /**
     * @param msg 加载框提示语
     *            显示圆形加载进度对话框
     */
    protected void showLoadingDialog(String msg) {
        if (!getComeActivity().isDestroyed() || !getComeActivity().isFinishing()) {
            if (loadingDialog == null) {
                loadingDialog = LoadingDialog.createDialog(this);
                loadingDialog.setMessage(msg);
                loadingDialog.setCanceledOnTouchOutside(false);
            }
            loadingDialog.show();
        }
    }

    /**
     * 关闭进度对话框
     */
    protected void closeLoadingDialog() {
        if (!getComeActivity().isDestroyed() || !getComeActivity().isFinishing()) {
            if (loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
                loadingDialog = null;
            }
        }
    }

    /**********************
     * activity跳转
     **********************************/
    public void openActivity(Class<?> targetActivityClass) {
        openActivity(targetActivityClass, null);
    }

    public void openActivity(Class<?> targetActivityClass, Bundle bundle) {
        Intent intent = new Intent(this, targetActivityClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void openActivityAndCloseThis(Class<?> targetActivityClass) {
        openActivity(targetActivityClass);
        this.finish();
    }

    public void openActivityAndCloseThis(Class<?> targetActivityClass, Bundle bundle) {
        openActivity(targetActivityClass, bundle);
        this.finish();
    }

    public void openActivityForResult(Class<?> targetActivityClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, targetActivityClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }

    /***************************************************************/

    /**
     * 验证上次点击按钮时间间隔，防止重复点击
     */
    public boolean verifyClickTime() {
        if (System.currentTimeMillis() - lastClickTime <= CLICK_TIME) {
            return false;
        }
        lastClickTime = System.currentTimeMillis();
        return true;
    }

    /**
     * 收起键盘
     */
    public void closeInputMethod() {
        // 收起键盘
        View view = getWindow().peekDecorView();// 用于判断虚拟软键盘是否是显示的
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 点击空白位置 隐藏软键盘
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    /**
     * 获取string
     *
     * @param mRid
     * @return
     */
    public String getStringMethod(int mRid) {
        return this.getResources().getString(mRid);
    }

    /**
     * 获取demin
     *
     * @param mRid
     * @return
     */
    protected int getDemonIntegerMethod(int mRid) {
        return (int) this.getResources().getDimension(mRid);
    }

    /**
     * 关闭所有(前台、后台)Activity,注意：请已BaseActivity为父类
     */
    protected static void finishAll() {
        int len = listActivity.size();
        for (int i = 0; i < len; i++) {
            Activity activity = listActivity.pop();
            activity.finish();
        }
    }

    public Activity getActivitys(int index) {
        Activity activity = listActivity.get(index);
        return activity;
    }

    /*
     * ************Fragement相关方法************************************************
     *
     */
    private Fragment currentFragment;

    /**
     * Fragment替换(当前destrory,新的create)
     */
    public void fragmentReplace(int target, Fragment toFragment, boolean backStack) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        String toClassName = toFragment.getClass().getSimpleName();
        if (manager.findFragmentByTag(toClassName) == null) {
            transaction.replace(target, toFragment, toClassName);
            if (backStack) {
                transaction.addToBackStack(toClassName);
            }
            transaction.commit();
        }
    }

    /**
     * Fragment替换(核心为隐藏当前的,显示现在的,用过的将不会destrory与create)
     */
    public void smartFragmentReplace(int target, Fragment toFragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        // 如有当前在使用的->隐藏当前的
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
        String toClassName = toFragment.getClass().getSimpleName();
        // toFragment之前添加使用过->显示出来
        if (manager.findFragmentByTag(toClassName) != null) {
            transaction.show(toFragment);
        } else {// toFragment还没添加使用过->添加上去
            transaction.add(target, toFragment, toClassName);
        }
        transaction.commit();
        // toFragment更新为当前的
        currentFragment = toFragment;
    }

    /***********************************************************************/
    /**
     * 获取点击事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isShouldHideKeyboard(view, ev)) {
                HideSoftInput(view.getWindowToken());
                view.clearFocus();
            }
        }
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {

        }
        return true;
    }

    /**
     * 隐藏软键盘
     */
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 点击空白区域，隐藏输入法软键盘
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                v.clearFocus();
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }


    /**
     * 对话框提示
     **/
    public void showTipDialog(String content) {

        if (mMaterialDialog == null) {
            mMaterialDialog = new MaterialDialog(this);//实例化对话框
        }
        mMaterialDialog.setTitle("友情提示")
                .setMessage(content)
                .setPositiveButton("知道啦", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                })
                .setCanceledOnTouchOutside(true)
                .setOnDismissListener(
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                mMaterialDialog = null;
                            }
                        })
                .show();
    }

    public void showTipDialog(String title, Spanned content) {
        if (mMaterialDialog == null) {
            mMaterialDialog = new MaterialDialog(this);//实例化对话框
        }
        mMaterialDialog.setTitle(title)
                .setMessage(content)
                .setPositiveButton("知道啦", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                })
                .setCanceledOnTouchOutside(true)
                .setOnDismissListener(
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                mMaterialDialog = null;
                            }
                        })
                .show();
    }

    public void showTipDialog2(String title, Spanned content, final onClickListener listener, String text) {
        if (mMaterialDialog == null) {
            mMaterialDialog = new MaterialDialog(this);//实例化对话框
        }
        mMaterialDialog.setTitle(title)
                .setMessage(content)
                .setPositiveButton(text, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onClickSure();
                        }
                        mMaterialDialog.dismiss();
                    }
                })
                .setCanceledOnTouchOutside(true)
                .setOnDismissListener(
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                mMaterialDialog = null;
                            }
                        })
                .show();
    }

    public void showTipDialog(String title, String content, final onClickListener listener, final onClickListener cancle) {
        if (mMaterialDialog == null) {
            mMaterialDialog = new MaterialDialog(this);//实例化对话框
        }
        if (cancle != null) {
            mMaterialDialog.setNegativeButton("忽略此版本", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cancle != null) {
                        cancle.onClickSure();
                    }
                    mMaterialDialog.dismiss();
                }
            });
        }
        mMaterialDialog.setTitle(title)
                .setMessage(content)
                .setPositiveButton("立即更新", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onClickSure();
                        }
                        mMaterialDialog.dismiss();
                    }
                })
                .setCanceledOnTouchOutside(true)
                .setOnDismissListener(
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                mMaterialDialog = null;
                            }
                        })
                .show();
    }

    public interface onClickListener {
        void onClickSure();
    }

    /**
     * 拨号对话框提示
     **/
    public void showCallDialog(final String msg, final String phone) {

        if (mMaterialDialog == null) {
            mMaterialDialog = new MaterialDialog(this);//实例化对话框
        }
//        final String phone = "400-888-4909";
        mMaterialDialog.setTitle("友情提示")
                .setMessage(msg)
                .setPositiveButton("呼叫", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mMaterialDialog) {
                            mMaterialDialog.dismiss();
                        }
                        isCallPhone(phone);
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (null != mMaterialDialog) {
                            mMaterialDialog.dismiss();
                        }
                    }
                })
                .setCanceledOnTouchOutside(true)
                .setOnDismissListener(
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                mMaterialDialog = null;
                            }
                        })
                .show();
    }

    //拨打电话
    public void isCallPhone(final String phone) {
        //系统版本小于7.0
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phone));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Acp.getInstance(this).request(new AcpOptions.Builder().setPermissions(Manifest.permission.CALL_PHONE).build(),
                    new AcpListener() {
                        @Override
                        public void onGranted() {
                            //注意：不用用带参的构造方法 否则 android studio 环境出错，提示要你检查授权
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + phone));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                        @Override
                        public void onDenied(List<String> permissions) {
                        }
                    });
        }

    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context          上下文对象
     * @param packageName：应用包名
     * @return
     */
    public boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    @TargetApi(19)
    public void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    /**
     * 判断某一个类是否存在任务栈里面
     *
     * @return
     */
    public boolean isExsitMianActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        ComponentName cmpName = intent.resolveActivity(getPackageManager());
        boolean flag = false;
        if (cmpName != null) { // 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                    flag = true;
                    break;  //跳出循环，优化效率
                }
            }
        }
        return flag;
    }


    public void getSnGoodsMsg(Context context, String goods_id) {
        RequestParams requestParams = new RequestParams();
        // 先取 api/Suning/getGoodsInfo 参数 goods_id ，使用productUrlWap,pictureUrl
        requestParams.put("goodsCode", goods_id);
        showLoadingDialog("正在处理...");
        HttpUtils.post(Constants.APP_IP + "/api/Suning/getGoodsInfo", BaseActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                closeLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    com.alibaba.fastjson.JSONObject back_json = com.alibaba.fastjson.JSONObject.parseObject(responseString);
                    if ("0".equals(back_json.getString("code"))) {
                        String goods_title = back_json.getJSONObject("data").getJSONObject("sn_responseContent").getJSONObject("sn_body").getJSONArray("getUnionInfomation").getJSONObject(0).getString("goodsName");
                        String pic_url = back_json.getJSONObject("data").getJSONObject("sn_responseContent").getJSONObject("sn_body").getJSONArray("getUnionInfomation").getJSONObject(0).getString("pictureUrl");
                        String wap_url = back_json.getJSONObject("data").getJSONObject("sn_responseContent").getJSONObject("sn_body").getJSONArray("getUnionInfomation").getJSONObject(0).getString("productUrlWap");

                        RequestParams req_parm = new RequestParams();
                        req_parm.put("detailUrl", wap_url);
                        req_parm.put("quanUrl", "");
                        req_parm.put("subUser", CaiNiaoApplication.getUserInfoBean().user_msg.uid);

                        HttpUtils.post(Constants.APP_IP + "/api/Suning/getGoodsAndCouponUrl", BaseActivity.this, req_parm, new TextHttpResponseHandler() {
                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                com.alibaba.fastjson.JSONObject se_json = com.alibaba.fastjson.JSONObject.parseObject(responseString);
                                if ("0".equals(se_json.getString("code"))) {
                                    String tg_url = se_json.getJSONObject("data").getJSONObject("sn_responseContent").getJSONObject("sn_body").getJSONObject("getExtensionlink").getString("shortLink");

                                    Intent intent = new Intent(BaseActivity.this, DialogActivity.class);
                                    intent.putExtra("url", tg_url);
                                    intent.putExtra("pic", pic_url);
                                    intent.putExtra("title", goods_title);
                                    intent.putExtra("type", "suning");
                                    LogUtils.d(TAG, "onSuccess: 跳到DialogActivity");
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onStart() {
                                super.onStart();
                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();
                                closeLoadingDialog();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    closeLoadingDialog();
                }
            }
        });
    }

    /**
     * 提交登录信息，登录成功后 保存用户登录的token
     **/
    public void submitData(final Context context, final String account, final String pwd) {
//        if (!CommonUtils.isNetworkAvailable()) {
//            showToast(getResources().getString(R.string.error_network));
//            return;
//        }
        RequestParams params = new RequestParams();
        params.put("userName", account);//员工号
//        final DesUtil desUtil = new DesUtil(DesUtil.SECURITY_PRIVATE_KEY);
//        params.put("password", desUtil.encrypt(pwd));//登录密码
        params.put("password", pwd);//登录密码

        HttpUtils.post(true, "", BaseActivity.this, params, new AsyncHttpResponseHandler() {

            //开始
            @Override
            public void onStart() {
                super.onStart();
//                showLoadingDialog("正在登录...");
            }

            //完成
            @Override
            public void onFinish() {
                super.onFinish();
//                closeLoadingDialog();
            }

            //成功
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseResult = new String(responseBody);


            }

            //失败
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                LogUtils.d(TAG, error.getMessage());
                showToast(error.getMessage());

                finish();
            }
        });

    }

    /**
     * 设置文本图片
     */
    public void setViewDrawable(Context mContext, TextView v, int drawable) {
        v.setVisibility(View.VISIBLE);
        v.setPadding(40, 0, 40, 0);
        Drawable dra = mContext.getResources().getDrawable(drawable);
        dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
        v.setCompoundDrawables(null, null, dra, null);
    }

    /**
     * 截取可见屏幕，包括状态栏
     */
    public static Bitmap shotScreen(Activity activity) {
        View view = activity.getWindow().getDecorView();
        Display display = activity.getWindowManager().getDefaultDisplay();
        view.layout(0, 0, display.getWidth(), display.getHeight());
        // 允许当前窗口保存缓存信息，这样getDrawingCache()方法才会返回一个Bitmap
        view.setDrawingCacheEnabled(true);
        return Bitmap.createBitmap(view.getDrawingCache());
    }

    /**
     * 截取可见屏幕，不包括状态栏
     */
    public static Bitmap shotActivity(Activity activity) {
        View view = activity.getWindow().getDecorView();
        Rect frame = new Rect();
        view.getWindowVisibleDisplayFrame(frame);
        view.setDrawingCacheEnabled(true);
        view.destroyDrawingCache();// 更新cache
        return Bitmap.createBitmap(view.getDrawingCache(), 0, frame.top, frame.width(), frame.height());
    }

    /*******************************拍照**********************************/
    /**
     * 拍照
     */
    public void photographRequest(Context context, final int code) {
        File mPhotoFile = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "cainiao");// TODO
        if (mPhotoFile.exists()) {
            mPhotoFile.delete();
        } else {
            mPhotoFile.mkdirs();
        }

        //獲取系統版本
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        // 激活相机
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                    "yyyy_MM_dd_HH_mm_ss");
            String filename = timeStampFormat.format(new Date());
            File tempFile = new File(mPhotoFile,
                    filename + ".jpg");

            if (currentapiVersion < 24) {
                // 从文件中创建uri
                uri = Uri.fromFile(tempFile);
            } else {
                //兼容android7.0 使用共享文件的形式
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, tempFile.getAbsolutePath());
                uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            }
        }


        //系统版本小于6.0
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            try {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 指定调用相机拍照后的照片存储的路径
//                mPhotoFile = new File(Environment.getExternalStorageDirectory().getPath()
//                        + File.separator + "image.jpg");// TODO
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, code);// 拍照
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.d("无法启动照相机");
            }
        } else {
            Acp.getInstance(context).request(new AcpOptions.Builder()
                    .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .build(), new AcpListener() {

                @Override
                public void onGranted() {
                    if (hasSdcard()) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(intent, code);
                    } else {
                        showToast("设备没有SD卡！");
                    }
                }

                @Override
                public void onDenied(List<String> permissions) {
                    //showToast("你拒绝拍照！");
                }
            });
        }


    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 从相册选择
     */
    public void selectPhoto(Context context, final int code) {
        //系统版本小于7.0
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            try {// 获取系统图册
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                // 要限制上传到服务器的图片类型时可以直接写如：image/jpeg 、 image/png等的类型
//                                   intent.setType("image/*");
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                startActivityForResult(intent, code);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Acp.getInstance(context).request(new AcpOptions.Builder()
                    .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .build(), new AcpListener() {

                @Override
                public void onGranted() {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, code);
                }

                @Override
                public void onDenied(List<String> permissions) {
                    // showToast("你拒绝拍照！");
                }
            });
        }
    }

    /**
     * 是否放弃编辑
     *
     * @param context
     */
    public void isEdit(Context context, String msg) {
        if (mMaterialDialog == null) {
            mMaterialDialog = new MaterialDialog(context);//实例化对话框
        }
        mMaterialDialog.setTitle("温馨提示")
                .setMessage(msg)
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                        finish();
                    }
                })
                .setNegativeButton("取消",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (null != mMaterialDialog) {
                                    mMaterialDialog.dismiss();
                                }
                            }
                        })
                .setCanceledOnTouchOutside(false)
                .setOnDismissListener(
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                mMaterialDialog = null;
                            }
                        })
                .show();
    }


    /**
     * @属性:接收广播是否登录消息
     * @开发者:陈飞
     * @时间:2018/7/21 14:33
     */
    private void onReceiverBroadCastMessage() {
        BroadcastManager.getInstance(this).addAction(new String[]{BroadcastContants.sendLoginMessage, BroadcastContants.sendAllObjectMessage}, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BroadcastContants.sendLoginMessage.equals(action)) {
                    ReceiverIsLoginMessage();
                } else {
                    String result = intent.getStringExtra("result");
                    Serializable resultSear = intent.getSerializableExtra("result");
                    ReceiverBroadCastMessage(action, resultSear, intent);
                    ReceiverBroadCastMessage(action, result, resultSear, intent);
                }
            }
        });
    }

    /**
     * @属性:
     * @开发者:陈飞
     * @时间:2018/7/21 14:57
     */
    protected void ReceiverIsLoginMessage() {

    }


    /**
     * @属性:
     * @开发者:陈飞
     * @时间:2018/7/21 14:57
     */
    protected void ReceiverBroadCastMessage(String status, Serializable serializable, Intent intent) {

    }

    /**
     * @属性:
     * @开发者:陈飞
     * @时间:2018/7/21 14:57
     */
    protected void ReceiverBroadCastMessage(String status, String resultStatus, Serializable serializable, Intent intent) {

    }

    /**
     * @属性:采用接口回调结果
     * @开发者:陈飞
     * @时间:2018/7/22 11:08
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (lisntener != null) {
            lisntener.onActivityResult(requestCode, resultCode, data);
        }
    }

    public interface onActivityResultLisntener {
        void onActivityResult(int requestCode, int resultCode, Intent data);

    }

    onActivityResultLisntener lisntener;

    /**
     * @属性:回调方法
     * @开发者:陈飞
     * @时间:2018/7/22 11:11
     */
    public void setOnActivityResultLisntener(onActivityResultLisntener lisntener) {
        this.lisntener = lisntener;
    }

    public Activity getComeActivity() {
        return this;
    }


    /**
     * @属性:获取剪贴板内容
     * @开发者:陈飞
     * @时间:2018/7/28 09:57
     */
    public String getClipboard() {
        ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData data = cm.getPrimaryClip();
        if (data == null)
            return "";
        ClipData.Item item = data.getItemAt(0);
        if (item == null)
            return "";
        if (item.getText() == null)
            return "";
        String content = item.getText().toString();
        return content;
    }

    /**
     * @属性:获取剪贴板内容
     * @开发者:陈飞
     * @时间:2018/7/28 09:57
     */
    public void copyClipboard(String str) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", str);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
    }


    public void commonGetUserMsg() {
        CaiNiaoApplication.commonGetUserMsg();
    }

    // 根据商品ID 获取淘宝、天猫商品 标题、图片
    public Map<String, String> getAliGoodDesc(String goods_id) {
        Map<String, String> back_map = new HashMap<>();
        HttpUtils.get(BaseActivity.this, Constants.AliGoodDescUrl + goods_id, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                String titles = "";
                String pics = "";
                String mm = "title\\s*:\\s'\\S*'";
                Pattern pattern = Pattern.compile(mm);
                Matcher matcher = pattern.matcher(responseString);
                if (matcher.find()) {
                    titles = StringUtils.trim(StringUtils.substringBetween(matcher.group(), "'", "'"));
                }
                String reg_pic = "pic\\s*:\\s'[\\S|\\s]*'";
                pattern = Pattern.compile(reg_pic);
                matcher = pattern.matcher(responseString);
                if (matcher.find()) {
                    pics = StringUtils.trim(StringUtils.substringBetween(matcher.group(), "'", "'"));
                }
                back_map.put("title", titles);
                back_map.put("pic", pics);
            }
        });
        return back_map;
    }

    // 淘宝短链接取商品ID
    public String getShortLinkGoodsID(String url) {
        final String[] back_url = {""};
        String long_url = "";
        Thread sync_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SyncHttpClient client = new SyncHttpClient();
                client.get(url, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        String goods_id = "";
                        if (responseString.contains("var url = '") && responseString.contains("Fid%3D")) {
                            String regx = "(Fid%3D)(.\\d*)";
                            Pattern pattern = Pattern.compile(regx);
                            Matcher matcher = pattern.matcher(responseString);
                            // 正则查找
                            if (matcher.find()) {
                                goods_id = matcher.group(2);
                            }
                            back_url[0] = goods_id;
                        }
                        if (responseString.contains("var url = '") && responseString.contains("taobao.com/i")) {
                            Pattern pps = Pattern.compile("(taobao.com/i)([0-9]*[0-9])");
                            Matcher matchers = pps.matcher(responseString);
                            // 匹配到商品ID
                            if (matchers.find()) {
                                goods_id = matchers.group(2);
                            }
                            back_url[0] = goods_id;
                        }
                    }
                });
            }
        });
        sync_thread.start();
        try {
            sync_thread.join();
            while (sync_thread.getState() != Thread.State.TERMINATED) {
                sync_thread.wait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return back_url[0];
    }

    public void clearClip() {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", "");
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
        }
    }

    /**
     * @param goods_id   商品id
     * @param channel_id 频道ID：int 0 taobao,1 pdd, 2 jd
     * @param echartView 渲染ECHARTVIEW
     */
    public void loadTrendEcharts(String goods_id, int channel_id, EchartView echartView, TextView title_header) {
        String goods_info_url = "";
        switch (channel_id) {
            case 1:
                goods_info_url = "https://mobile.yangkeduo.com/goods2.html?goods_id=" + goods_id;
                break;
            case 2:
                goods_info_url = "https://item.m.jd.com/product/" + goods_id + ".html";
                break;
            default:
                goods_info_url = "https://detail.m.tmall.com/templatesNew/index?id=" + goods_id;
                break;
        }

        String rnd_String = System.currentTimeMillis() + "dingdong";
        String requestKey = DES3DUtils.des3EncodeCBC(Constants.trendKEY, rnd_String);
        RequestParams params = new RequestParams();
        params.put("key", requestKey);
        params.put("turl", goods_info_url);
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("key", requestKey).add("turl", goods_info_url);

        OkHttpUtils.getInstance().post(Constants.trendAPI_URL, builder.build(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d(TAG, "加载ECHART数据失败: ");
                runOnUiThread(() -> {
                    echartView.setVisibility(View.GONE);
                    title_header.setVisibility(View.GONE);
                });

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String responseString = response.body().string();
                LogUtils.d(TAG, "加载ECHART数据成功 : " + responseString);
                try {
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(responseString);
                    String tmpJSONArrayString = jsonObject.getString("datePrice");
                    String[] tmpArray = tmpJSONArrayString.split("],");
                    List<String> list_x = new ArrayList<>();
                    List<String> list_y = new ArrayList<>();

                    for (String tmp : tmpArray) {
                        String pan = "\\((.*)\\),(.*)";

                        Pattern r = Pattern.compile(pan);
                        Matcher m = r.matcher(tmp);
                        if (m.find()) {
                            String dateString = m.group(1).replace(",", "-");
                            String dataString = m.group(2);
                            list_x.add(dateString);
                            list_y.add(String.format("%.2f", Float.valueOf(dataString)));
                        }
                    }

                    com.alibaba.fastjson.JSONArray xxx = new com.alibaba.fastjson.JSONArray();
                    xxx.addAll(list_x);
                    com.alibaba.fastjson.JSONArray yyy = new com.alibaba.fastjson.JSONArray();
                    yyy.addAll(list_y);
                    runOnUiThread(() -> {
                        LogUtils.d(TAG, "加载ECHART数据成功: 设置显示出来!");
                        echartView.setVisibility(View.VISIBLE);
                        echartView.refreshEchartsWithData(xxx, yyy);
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        LogUtils.d(TAG, "加载ECHART数据成功: 解析数据出错!");
                        echartView.setVisibility(View.GONE);
                        title_header.setVisibility(View.GONE);
                    });
                }

            }
        });
//        echartView.refreshEchartsWithData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // Log.d(TAG, "onNewIntent: 收到新的意图：" + intent);
        super.onNewIntent(intent);
    }

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

}
