package com.android.jdhshop;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.android.jdhshop.base.LogcatHelper;
import com.android.jdhshop.bean.UserBean;
import com.android.jdhshop.bean.UserInfoBean;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.dao.DaoMaster;
import com.android.jdhshop.dao.DaoSession;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.juduohui.AssetTools;
import com.android.jdhshop.juduohui.JuduohuiSplashActivity;
import com.android.jdhshop.juduohui.NotificationService;
import com.android.jdhshop.utils.MiitHelper;
import com.android.jdhshop.utils.PMUtil;
import com.baidu.mobads.sdk.api.BDAdConfig;
import com.baidu.mobads.sdk.api.BDDialogParams;
import com.baidu.mobads.sdk.api.MobadsPermissionSettings;
import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.github.anzewei.parallaxbacklayout.ParallaxHelper;
import com.google.gson.Gson;
import com.kepler.jd.Listener.AsyncInitListener;
import com.kepler.jd.login.KeplerApiManager;
import com.kwad.sdk.api.KsAdSDK;
import com.kwad.sdk.api.SdkConfig;
import com.loopj.android.http.RequestParams;
import com.miui.zeus.mimo.sdk.MimoSdk;
import com.qq.e.comm.managers.GDTADManager;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.uuch.adlibrary.utils.DisplayUtil;
import com.vivo.mobilead.manager.VInitCallback;
import com.vivo.mobilead.manager.VivoAdManager;
import com.vivo.mobilead.model.VAdConfig;
import com.vivo.mobilead.unified.base.VivoAdError;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.jiguang.analytics.android.api.CalculateEvent;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jpush.android.api.JPushInterface;
import cz.msebera.android.httpclient.Header;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;

/**
 * Created by yohn on 2018/7/6.
 */

public class CaiNiaoApplication extends MultiDexApplication {
    private DaoMaster.DevOpenHelper mHelper;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private static Context context;
    private Context testContext;
    private static UserBean userBean; //??????????????????????????????
    private PendingIntent restartIntent;
    public static IWXAPI api;
    private static UserInfoBean userInfoBean;
    private static String registerid = "";
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private static CaiNiaoApplication instance;
    public boolean isInit = false;
    public boolean isIniting = false;
    private static String oaid = "";
    public static boolean isOpenNotification = true;

    public static String getRegisterid() {
        return registerid;
    }

    public static void setRegisterid(String registerid) {
        CaiNiaoApplication.registerid = registerid;
    }

    public static CaiNiaoApplication getInstances() {
        return instance;
    }

    public String getOaid() {
        return oaid;
    }

    private ServiceConnection connection = null;
    public boolean isBindNotificationServices = false;

    public static class HeartBeat extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RequestParams req = new RequestParams();
            req.put("imei", CaiNiaoApplication.oaid);
            req.put("appcode", CaiNiaoApplication.getAppCode());
            req.put("appname", CaiNiaoApplication.getAppName());
            req.put("channel", CaiNiaoApplication.getAppChannel());
            req.put("brand", Build.BRAND);
            req.put("marking",Build.MODEL);
            req.put("sdk",Build.VERSION.SDK_INT);
            Log.d("HeartBeat", "handleMessage: " + req.toString() + " ??????????????????" + CaiNiaoApplication.getAppChannel());
            HttpUtils.post(Constants.USER_HEART_BEAT, context, req, new TextHttpResponseHandler() {
                @Override
                public void onFinish() {
                    super.onFinish();
                    heartBeat.sendEmptyMessageDelayed(1, Constants.HEART_BEAT_TIME);
                }
            });
        }
    }

    public static HeartBeat heartBeat = new HeartBeat();

    private ServiceConnection getConnection() {
        if (connection == null) {
            connection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    LogUtils.d("CaiNiaoAPP", "onServiceConnected: " + name);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    LogUtils.d("CaiNiaoAPP", "onServiceDisconnected: " + name);
                }
            };
        }
        return connection;
    }

    public static boolean isServiceRun(Context mContext, String className) {
        boolean isRun = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(100);
        int size = serviceList.size();
        for (int i = 0; i < size; i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRun = true;
                break;
            }
        }
        return isRun;
    }

    public NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public void startNotification() {
        if (!isBindNotificationServices) {
            Intent notification_intent = new Intent(getAppContext(), NotificationService.class);
            isBindNotificationServices = getAppContext().bindService(notification_intent, getConnection(), BIND_AUTO_CREATE);
        }
    }

    public void stopNotification() {
        if (isBindNotificationServices) {
            isBindNotificationServices = false;
            getAppContext().unbindService(CaiNiaoApplication.getInstances().getConnection());
        }
    }

    @Override
    public void onCreate() {
        //?????????
        super.onCreate();
        context = getApplicationContext();// ??????Context
        testContext = this;
        instance = this;
        String kg = SPUtils.getStringData(this, "ty", "");
        LogUtils.d("CaiNiaoApplication", "??????????????????????????????:" + kg);

        if (kg.equals("true")) {
            initAppInfo();
            //initResourceManager();


            return;
        } else {
            LogUtils.d("22222222", "????????????");
        }

        if ("1".equals(SPUtils.getStringData(this, "firstmb", "1"))) {
            SPUtils.saveStringData(this, "firstmb", "0");
            SPUtils.saveStringData(this, "mbone", "???{??????}???");
            SPUtils.saveStringData(this, "mbtwo", "???????????????{????????????}");
            SPUtils.saveStringData(this, "mbthree", "???????????????{?????????}");
            SPUtils.saveStringData(this, "mbfour", "?????????" + getString(R.string.app_name) + "????????????{??????}???");
            SPUtils.saveStringData(this, "mbfive", "???~???????????????{?????????}????????????Tao???????????????");
        }
    }

    public void initAppInfo() {
        if (!isIniting) {
            isIniting = true;
        } else {
            return;
        }


        //????????????
        LogUtils.d("22222222", "????????????");
        registerIds(context);
        registerWeChat(context); // ???????????????SDK
        registerKepler(); // ???????????????SDK
        registerActivityLifecycleCallbacks(ParallaxHelper.getInstance());
        // ?????????
        registerChuanShanJia();
        // ?????????
        registerYouLiangHui();
        // ??????
        registerBaidu(context);
        // ??????
        registerKuaiShou(testContext);
        // Vivo
        registerVivo(context);
        // ????????????
        registerXiaoMi(context);
        // App??????????????????
//        registerAppCycleListen();
        // ??????????????????
        registerJPush(context);
        // ??????WEB??????APP
        releaseWebApp();
        isInit = true;
        LogcatHelper.getInstance(this).start();


        try {
            Constants.RESOURCE_CODE = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData.getString("JDH_RESOURCE_CODE");
        } catch (Exception e) {
            Constants.RESOURCE_CODE = "hw";
            e.printStackTrace();
        }


        // android 7.0???????????????????????????
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        setDatabase();
        initDisplayOpinion();
        Fresco.initialize(CaiNiaoApplication.getAppContext());

        isOpenNotification = "true".equals(SPUtils.getStringData(context, "pt_notification", "true"));
        // ????????????
        AlibcTradeSDK.asyncInit(this, new AlibcTradeInitCallback() {
            @Override
            public void onSuccess() {
                LogUtils.d("TAG", "AlibcTradeSDK ?????????????????? ");
            }

            @Override
            public void onFailure(int code, String msg) {
                LogUtils.d(code + "" + msg);
            }
        });

        CrashReport.initCrashReport(getApplicationContext(), "57fda43e0e", false);
//        CrashHandler.getInstance().init(this); // ???????????????????????????????????????????????????????????????

//            heartBeat.sendEmptyMessageDelayed(1, Constants.HEART_BEAT_TIME);


        RequestParams requestParams = new RequestParams();
        requestParams.put("cat_id", 6);
        HttpUtils.post(Constants.GET_BANNER, context, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogUtils.d(responseString);
                LogUtils.d(throwable.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                SPUtils.saveStringData(getAppContext(), "ade", responseString);
            }
        });
    }

    private void setDatabase() {
        //1.???????????????
        mHelper = new DaoMaster.DevOpenHelper(this, "hkxdb", null);
        //2.??????????????????
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.disableWriteAheadLogging();
        //3.??????????????????
        mDaoMaster = new DaoMaster(db);
        //4.???????????????
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    private void initDisplayOpinion() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        DisplayUtil.density = dm.density;
        DisplayUtil.densityDPI = dm.densityDpi;
        DisplayUtil.screenWidthPx = dm.widthPixels;
        DisplayUtil.screenhightPx = dm.heightPixels;
        DisplayUtil.screenWidthDip = DisplayUtil.px2dip(getApplicationContext(), dm.widthPixels);
        DisplayUtil.screenHightDip = DisplayUtil.px2dip(getApplicationContext(), dm.heightPixels);
    }

    public static Context getAppContext() {
        return context;
    }

    public static UserInfoBean getUserInfoBean() {
        return userInfoBean;
    }

    public static void setUserInfoBean(UserInfoBean userInfoBean1) {
        userInfoBean = userInfoBean1;
    }

    public static UserBean getUserBean() {
        return userBean;
    }

    public static void setUserBean(UserBean userBean1) {
        userBean = userBean1;
    }

    public static void commonGetUserMsg() {
        RequestParams params = new RequestParams();
        HttpUtils.post(Constants.GET_USER_MSG, context, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogUtils.d("commonGetUserMsg onFailure responseString = " + responseString);
            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("commonGetUserMsg onSuccess responseString = " + responseString);
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    //?????????
                    UserInfoBean userBean = null;
                    int code = jsonObject.optInt("code");
                    //???????????????
                    String msg = jsonObject.optString("msg");
                    String data = jsonObject.optString("data");
                    if (0 == code) {
                        if (!TextUtils.isEmpty(data)) {//GSON??????????????????double??????
                            Gson gson = new Gson();
                            userBean = gson.fromJson(data.trim(), UserInfoBean.class);
                            CaiNiaoApplication.setUserInfoBean(userBean);
                        }
                        if (null != userBean) {
                            CaiNiaoApplication.setUserBean(new UserBean(userBean.user_detail.user_id, userBean.user_msg.group_id, SPUtils.getStringData(getAppContext(), "token", ""), userBean.user_detail.avatar, userBean.user_detail.nickname, userBean.user_msg.is_forever));
                        }
                    } else {
                    }
                } catch (JSONException e) {
                }
            }
        });
    }


    public static void registerWeChat(Context context) {   //???????????????app
        LogUtils.d("TAG", "registerWeChat: " + Constants.WX_APP_ID);
        api = WXAPIFactory.createWXAPI(context, Constants.WX_APP_ID, true);
        api.registerApp(Constants.WX_APP_ID);
    }

    public static void registerKepler() {
        LogUtils.d("Kepler", "Kepler: ID:" + Constants.JD_CLIENT_ID);
        LogUtils.d("Kepler", "Kepler: ST:" + Constants.JD_SECRET);
        KeplerApiManager.asyncInitSdk(CaiNiaoApplication.getInstances(), Constants.JD_CLIENT_ID, Constants.JD_SECRET,
                new AsyncInitListener() {
                    @Override
                    public void onSuccess() {
                        Log.e("Kepler", "??????Kepler???????????????!");
                        LogUtils.d("Kepler", "????????????:" + KeplerApiManager.getKeplerVersion());

                    }

                    @Override
                    public void onFailure() {
                        Log.e("Kepler",
                                "Kepler asyncInitSdk ???????????????????????? lib ???????????????????????????,?????????????????????????????????");
                    }
                });
    }

    // ??????????????????
    public static void registerYouLiangHui() {

        GDTADManager gdtadManager = GDTADManager.getInstance();
//        gdtadManager.setPluginLoadListener( new PM.a.a() {
//            @Override
//            public void onLoadSuccess() {
//                LogUtils.d("?????????", "onLoadSuccess: ");
//            }
//
//            @Override
//            public void onLoadFail() {
//                LogUtils.d("?????????", "onLoadFail: ");
//            }
//        });

        if (!gdtadManager.isInitialized()) {
            gdtadManager.initWith(getAppContext(), "1111812401");
        }

        LogUtils.d("?????????", "registerYouLiangHui: " + gdtadManager.getDeviceStatus().toString());
    }

    public static void registerChuanShanJia() {
        LogUtils.d("TAG", "registerChuanShanJia: ???????????????");
        TTAdSdk.init(context, new TTAdConfig.Builder()
                .appId("5176036")
                .useTextureView(true) //????????????SurfaceView??????????????????,??????SurfaceView??????????????????????????????TextureView
                .appName("?????????APP")
                .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)//???????????????
                .allowShowNotify(true) //????????????sdk?????????????????????,????????????false??????????????????????????????????????????
                .debug(false) //??????????????????????????????????????????????????????????????????????????????
                .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI) //???????????????????????????????????????,????????????????????????????????????apk?????????????????????????????????????????????????????????
                .supportMultiProcess(false) //????????????????????????true??????.asyncInit(true) //?????????????????????sdk,?????????true????????????SDK??????????????????3450??????????????????~~
                //.httpStack(new MyOkStack3())//?????????????????????demo????????????okhttp3??????????????????????????????????????????????????????????????????
                .build(), new TTAdSdk.InitCallback() {
            @Override
            public void success() {
                Log.d("TAG", "success: ");
            }

            @Override
            public void fail(int i, String s) {
                Log.d("TAG", "fail: " + i + ",s =" + s);
            }
        });
    }

    public static void registerBaidu(Context context) {
        BDAdConfig bdAdConfig = new BDAdConfig.Builder()
                // 1?????????app???????????????
                .setAppName("?????????APP")
                // 2????????????mssp??????????????????appsid???????????????????????????????????????????????????AndroidManifest.xml????????????
                .setAppsid("d422b745")
                // 3????????????????????????????????????????????????????????????
                .setDialogParams(new BDDialogParams.Builder()
                        .setDlDialogType(BDDialogParams.TYPE_BOTTOM_POPUP)
                        .setDlDialogAnimStyle(BDDialogParams.ANIM_STYLE_NONE)
                        .build())
                .build(context);
        bdAdConfig.init();

        // ??????SDK??????????????????????????????????????????????????????????????????APP LIST
        // ?????????????????????SDK?????????????????????SDK?????????????????????????????????????????????IMEI???????????????
        // ??????SDK????????????????????????????????????ECPM
        MobadsPermissionSettings.setPermissionReadDeviceID(true);
        MobadsPermissionSettings.setPermissionLocation(true);
        MobadsPermissionSettings.setPermissionStorage(true);
        MobadsPermissionSettings.setPermissionAppList(true);
    }

    public static void registerKuaiShou(Context context) {
        KsAdSDK.init(context, new SdkConfig.Builder().appId("748800001").appName("?????????").showNotification(true).debug(false).canReadICCID(true).build());
    }

    public static void registerXiaoMi(Context context) {
        MimoSdk.init(context);
        MimoSdk.setDebugOn(true);
    }

    public static void registerVivo(Context context) {
        VAdConfig.Builder vAdConfig = new VAdConfig.Builder();
        vAdConfig.setDebug(false).setMediaId("f5b2d25f2c4e4a53a632426ae078c32d");
        VivoAdManager.getInstance().init(getInstances(), vAdConfig.build(), new VInitCallback() {
            @Override
            public void suceess() {
                Log.d("TAG", "success: ");
            }

            @Override
            public void failed(@NonNull VivoAdError vivoAdError) {
                Log.d("TAG", "failed: ");
            }
        });
    }

    public static void registerJPush(Context context) {
        //?????????????????????
        JPushInterface.setDebugMode(true); // ??????????????????????????????logutils????????????????????????
        JPushInterface.init(context);
        JAnalyticsInterface.setDebugMode(true);
        JAnalyticsInterface.init(context);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
        if (SPUtils.getIntData(context, format.format(new Date()) + "use", 0) != 1) {
            CalculateEvent calculateEvent = new CalculateEvent("openApp", 1);
            JAnalyticsInterface.onEvent(context, calculateEvent);
            SPUtils.saveIntData(context, format.format(new Date()) + "use", 1);
        }
    }

    // ??????????????????????????????
    public static void registerIds(Context context) {

        MiitHelper miitHelper = new MiitHelper(new MiitHelper.AppIdsUpdater() {
            @Override
            public void onIdsValid(String ids) {
//                oaid = ids;
                Log.d("TAG", "CaiNiaoApplicationOnIdsValid: " + ids);
            }

            @Override
            public void supportOaID(String i) {
                oaid = i;
            }
        });
        miitHelper.getDeviceIds(context);
    }

    public boolean wasBackground = false;    //????????????????????????,???????????????????????????

    // ??????APP??????????????????
    public void registerAppCycleListen() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            private String TAG = "ZJM";

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                PMUtil.getInstance().pushAC(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                // Log.d(TAG, "onActivityResumed: ");
                if (wasBackground) {
                    wasBackground = false;
                    int timeout_splash = 15; // ?????????
                    if (System.currentTimeMillis() - last_time > timeout_splash * 1000 && SPUtils.getBoolean(activity,"is_open_ad",false)) {
                        // Log.d(TAG, "onActivityResumed: ????????????????????????!");
                        Intent ii = new Intent(getAppContext(), JuduohuiSplashActivity.class);
                        ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ii.putExtra("close_only", 1);
                        activity.startActivity(ii);
                    }
                }

            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                wasBackground = isBackground(activity);
                // Log.d(TAG, "onActivityStopped: " + wasBackground);
                if (wasBackground) {
                    last_time = System.currentTimeMillis();
                } else {
                    last_time = 0;
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                PMUtil.getInstance().removeAC(activity);
            }


            public long last_time = 0;

            public boolean isBackground(Context context) {

                ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

                List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();

                for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                    // Log.d(TAG, "isBackground: " + appProcess.processName+","+appProcess.importanceReasonComponent + ",,,," + appProcess.importance + " = " + IMPORTANCE_BACKGROUND);
                    if (appProcess.processName.equals(context.getPackageName())) {
                        if (appProcess.importance != IMPORTANCE_FOREGROUND) {
                            Log.i("??????", appProcess.processName);

                            return true;

                        } else {

                            Log.i("??????", appProcess.processName);

                            return false;

                        }

                    }

                }

                return false;

            }

            private boolean isApplicationBroughtToBackground() {

                ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
                if (!tasks.isEmpty()) {
                    ComponentName topActivity = tasks.get(0).topActivity;
                    if (!topActivity.getPackageName().equals(getPackageName())) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private static String app_version_code;
    private static String app_version_name;

    private static void initAppVersionInfo() {
        try {
            PackageManager packageManager = CaiNiaoApplication.getAppContext().getPackageManager();
            PackageInfo pi = packageManager.getPackageInfo(CaiNiaoApplication.getInstances().getPackageName(), 0);
            app_version_code = pi.versionCode + "";
            app_version_name = pi.versionName;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getAppCode() {
        if (app_version_code == null || "".equals(app_version_code)) {
            initAppVersionInfo();
        }
        return app_version_code;
    }

    public static String getAppName() {
        if (app_version_code == null || "".equals(app_version_code)) {
            initAppVersionInfo();
        }
        return app_version_name;
    }

    public static String getAppChannel() {
        return Constants.RESOURCE_CODE != null ? Constants.RESOURCE_CODE : "hw";
    }

    @Subscribe
    private void getCustomMessage(String msg) {
        Log.d("ZJM", "getCustomMessage: " + msg);
    }

    /**
     * ????????????????????????Activity
     *
     * @param context
     * @return
     */
    public static String getTopActivity(Context context) {
        android.app.ActivityManager manager = (android.app.ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

        if (runningTaskInfos != null) {
            return (runningTaskInfos.get(0).topActivity).toString();
        } else
            return null;
    }

    public static ComponentName getTopActivityCn(Context context) {
        android.app.ActivityManager manager = (android.app.ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

        if (runningTaskInfos != null) {
            return runningTaskInfos.get(0).topActivity;
        } else
            return null;
    }

    public void releaseWebApp() {
        LogUtils.logd("????????? WEB_APP ??????");
        // ??????????????? /sdcard/Android/data/com.android.jdhshop/files/web_app
        File check_file = getExternalFilesDir("web_app");
        // ??????????????? /sdcard/Android/data/com.android.jdhshop/files
        String root_path = getExternalFilesDir(null).getPath().replace("/storage/emulated/0", "/sdcard");
        // ??????????????? ?????? ??????????????? ?????? ???????????????
        if (!check_file.exists() || check_file.list().length <= 0) {
            LogUtils.logd("??????");
            AssetTools.releaseAssets(this, "web_app", root_path);
            LogUtils.logd("????????????");
        } else {
            File check_version_file = new File(check_file, "version.txt");
            // ??????version??????????????????????????? ?????? ???????????????????????????????????????
            LogUtils.logd("onActivityCreated: " + check_version_file.lastModified() + "," + AssetTools.getPackageLastUpdateTime(this));
            if (check_version_file.lastModified() < AssetTools.getPackageLastUpdateTime(this)) {
                AssetTools.releaseAssets(this, "web_app", root_path);
            }
            check_version_file = null;
        }
        LogUtils.logd("???????????????");
    }
}
