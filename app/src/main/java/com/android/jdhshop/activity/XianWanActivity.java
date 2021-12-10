package com.android.jdhshop.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.ali.auth.third.core.storage.aes.AESCrypt;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.utils.AesUtils;
import com.android.jdhshop.utils.DES3DUtils;
import com.android.jdhshop.utils.DownloadUtil;
import com.android.jdhshop.utils.OkHttpUtils;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.OkHttpDownloader;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class XianWanActivity extends BaseActivity {

    Context mContext = null;
    WebView wv=null;
    WebChromeClient xwwvcc = null;
    WebViewClient xwwvc = null;
    String TAG = "闲玩";
    /**
     * 自定义WebClient
     * 增加本地相册调用处理
     */
    private static final int REQUEST_FILE_CHOOSER_CODE = 102;
    private ValueCallback <Uri> mUploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private ImageView tv_left;
    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xian_wan);
        tv_left=findViewById(R.id.tv_left);
        BaseLogDZiYuan.LogDingZiYuan(tv_left, "icon_back.png");
        mContext = this;
        String sd = null;
        try {
            sd = URLEncoder.encode(AesUtils.encrypt("2"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // Log.d(TAG, "onCreate: " + sd);

        wv = findViewById(R.id.wv);
        wv.setWebViewClient(new XwWebClient());
        wv.setWebChromeClient(new WebChromeClient());
        WebSettings webSetting = wv.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setAllowContentAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);//这里一定得是false,不然打开的网页中，不能在点击打开了
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0).getPath());
        webSetting.setBlockNetworkImage(false); // 解决图片不显示
        webSetting.setUserAgentString(webSetting.getUserAgentString()+" "+(getIntent().getStringExtra("ua")==null?"":getIntent().getStringExtra("ua")));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting
                    .setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        wv.addJavascriptInterface(new XianWanJavaScript(), "android");
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        wv.loadUrl(getXianWanUrl());
    }
    @Override
    protected void initUI() {}
    @Override
    protected void initData() {}
    @Override
    protected void initListener() {}
    /**
     * ptype 	是 	string 	2 安卓
     * androidosv 	是 	string 	安卓操作系统版本号,指的是API level 如:29 、28 （android Q 对应 29）
     * deviceid 	是 	string 	设备号 android取设备号 点击查看获取方法 （必须必须必须保持一致） 获取不到请传 0
     * msaoaid 	是 	string 	安全联盟OAID （未接入安全联盟请传空字符串“”） 安全联盟接入说明
     * appid 	是 	string 	开发者id 由闲玩分配
     * appsign 	是 	string 	客户端的用户id，即每个用户的用户ID 每个用户的唯一，且不变
     * xwversion 	是 	int 	默认值 2 （2 ：表示已实现上述2.1中的第2、3点 所示的打开相册 scheme唤醒功能）
     * keycode 	是 	string 	加密校验 MD5(appid+deviceid+msaoaid+androidosv+ptype+appsign+appsecret)
     */


    private String getXianWanUrl(){
        String base = "https://h5.17xianwan.com/try/try_list_plus";
        String p_type = "2";
        String android_osv = Build.VERSION.SDK_INT+"";
        String deviceid = getDeviceId(mContext);
        String app_id = "8237";
        String app_secret = "tqbraldybfsyafe3";
        String app_sign = AesUtils.md5(SPUtils.getStringData(mContext,"uid","0"));
        String xwversion = "2";
        String msaoaid = CaiNiaoApplication.getInstances().getOaid();
        String keycode = AesUtils.md532( app_id+deviceid+ msaoaid+android_osv+p_type+app_sign+ app_secret);

        String r_str =  base+"?ptype="+p_type+"&msaoaid="+ msaoaid +"&androidosv="+android_osv+"&deviceid="+deviceid+"&appsign="+ app_sign +"&appid="+app_id+"&xwversion="+xwversion+"&keycode="+keycode;
        Log.d("闲玩", "getXianWanUrl: " + r_str);
        return r_str;
    }


    //原生开发请用此方法获取设备号
    @SuppressLint("HardwareIds")
    private static String getDeviceId(Context context) {
        String imei = null;
        TelephonyManager telephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyMgr != null) {
            try {
                imei = telephonyMgr.getDeviceId();
            } catch (Exception e) {
                return imei;
            }
        }
        return imei;
    }
    private String package_name = "";
    private class XianWanJavaScript{

        public int CheckInstalled(String packageName) {
            int isInstall = 0;
            // Log.d(TAG, "CheckInstall: " + packageName);
            if (!TextUtils.isEmpty(packageName)) {

                try {
                    ApplicationInfo info = mContext.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
                    // Log.d(TAG, "CheckInstall: " + info);
                    isInstall = 1;
                } catch (PackageManager.NameNotFoundException e) {
                    // Log.d(TAG, "CheckInstall: " + packageName + "未安装.");
                }
            }

            return isInstall;
        }

        @JavascriptInterface
        public void CheckInstall(String packageName) {
            package_name = packageName;
            // Log.d(TAG, "CheckInstall: " + packageName);
            int isInstall = CheckInstalled(packageName);
            String js = "javascript:CheckInstall_Return(" + (isInstall == 1 ? "1)" : "0)");
            wv.post(new Runnable() {
                @Override
                public void run() {
                    wv.evaluateJavascript(js,null);
                }
            });

        }
        @JavascriptInterface
        public void OpenAPP(final String packageName) {
            // Log.d(TAG, "OpenAPP: " + packageName);
            if (CheckInstalled(packageName) == 1) {
                try {
                    PackageManager pm = mContext.getPackageManager();
                    PackageInfo pi = pm.getPackageInfo(packageName, 0);
                    Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
                    //resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    resolveIntent.setPackage(pi.packageName);
                    List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);
                    ResolveInfo ri = apps.iterator().next();
                    if (ri != null) {
                        String package_name = ri.activityInfo.packageName;
                        String class_name = ri.activityInfo.name;
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_FROM_BACKGROUND);
                        ComponentName cn = new ComponentName(package_name, class_name);
                        intent.setComponent(cn);
                        mContext.startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //show_toast(context, "未安装指定应用");
            }
        }
        @JavascriptInterface
        public void InstallAPP(final String url) {
            // Log.d(TAG, "InstallAPP: " + url);
            File ff = Environment.getExternalStoragePublicDirectory("jdh_download");
            if (!ff.exists()) ff.mkdirs();
            String filename = package_name +"_xw.apk";
            String full_path = ff.getAbsolutePath()+"/"+filename;
            // Log.d(TAG, "InstallAPP: " + filename);
            // Log.d(TAG, "InstallAPP: " + ff);
            DownloadUtil.get().download(url, ff.getAbsolutePath(), filename, new DownloadUtil.OnDownloadListener() {
                @Override
                public void onDownloadSuccess(File file) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Uri contentUri = FileProvider.getUriForFile(mContext,"com.android.jdhshop.update_app.file_provider",new File(full_path));
                        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                    } else {
                        intent.setDataAndType(Uri.fromFile(new File(full_path)),"application/vnd.android.package-archive");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }

                    startActivity(intent);
                }

                @Override
                public void onDownloading(int progress) {
                    wv.post(new Runnable() {
                        @Override
                        public void run() {
                            wv.loadUrl("javascript:setProgress('" + package_name + "',"+progress+")");
                        }
                    });

                }

                @Override
                public void onDownloadFailed(Exception e) {
                    showToast("下载失败"+ e.getMessage());
                }
            });

        }
        @JavascriptInterface
        public void Browser(final String url) {
            // Log.d(TAG, "Browser: " + url);
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }



    private class XwWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (!url.startsWith("http") && !url.startsWith("https") && !url.startsWith("ftp")) {
                try {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0) {
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                    }
                } catch (Exception e) {
                    Log.d("setWebViewClient", "shouldOverrideUrlLoading: " + e.getMessage());
                }
                return true;
            }
            if (Build.VERSION.SDK_INT < 26) {
                view.loadUrl(url);
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
    private class XwWebChromeClient extends WebChromeClient{
        // For Android 3.0+
        public void openFileChooser(ValueCallback < Uri > uploadMsg) {
            mUploadMessage = uploadMsg;
            openImageChooserActivity();
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            openImageChooserActivity();
        }

        // For Android 4.1
        public void openFileChooser(ValueCallback < Uri > uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            openImageChooserActivity();
        }

        // For Android >= 5.0
        @Override
        public boolean onShowFileChooser(WebView webView,
                                         ValueCallback < Uri[] > filePathCallback,
                                         WebChromeClient.FileChooserParams fileChooserParams) {
            uploadMessageAboveL = filePathCallback;
            openImageChooserActivity();
            return true;
        }
    }

    /**
     * 打开本地相册
     */
    private void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), REQUEST_FILE_CHOOSER_CODE);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FILE_CHOOSER_CODE) {
            if (null == mUploadMessage && null == uploadMessageAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != REQUEST_FILE_CHOOSER_CODE || uploadMessageAboveL == null)
            return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[] {
                            Uri.parse(dataString)
                    };
            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }

    @Override
    public void onBackPressed() {
        // Log.d(TAG, "onBackPressed: ");
        if (wv.canGoBack()){
            wv.goBack();
        }
        else {
            super.onBackPressed();
        }
    }
}