package com.android.jdhshop.https;

import android.content.Context;
import android.os.Build;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.juduohui.message.JuduohuiMessage;
import com.android.jdhshop.utils.Base64Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;


/**
 * @author Lin 网络请求工具类
 */
//

public class HttpUtils {

    public static final String TAG = "HttpUtils";
    private static AsyncHttpClient client = new AsyncHttpClient(); // 实例化对象

//    private static String imei;

    static {
        // 获取手机设备的唯一码(IMIE )
//        final TelephonyManager TelephonyMgr = (TelephonyManager) (CaiNiaoApplication.getAppContext()).getSystemService(Context.TELEPHONY_SERVICE);
        //动态获取权限

//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
//            imei = TelephonyMgr.getDeviceId();
//        } else {
//            Acp.getInstance(CaiNiaoApplication.getAppContext()).request(new AcpOptions.Builder().setPermissions(android.Manifest.permission.READ_PHONE_STATE).build(), new AcpListener() {
//                @Override
//                public void onGranted() {
//                    imei = TelephonyMgr.getDeviceId();
//                }
//
//                @Override
//                public void onDenied(List<String> permissions) {
//                    Toast.makeText(CaiNiaoApplication.getAppContext(), permissions.toString() + "权限拒绝", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        }


//        if (TextUtils.isEmpty(imei)) {
//            imei = "77";// 默认值
//        }
        try {
            String versionCode = Build.VERSION.RELEASE; // 设备的系统版本
//            String versionCode = String.valueOf(SystemUtil.getVersionCode(CarZoneApplication.getAppContext()));
            client.addHeader("version", versionCode);// 系统版本
            client.addHeader("platform", "Android安卓");//app类型iOS苹果、Android安卓
        } catch (Exception e) {
//            LogUtils.e(TAG, "获取系统版本出错！");
            e.printStackTrace();
        }
        client.setTimeout(41000); // 设置链接超时，如果不设置，默认为10s
        client.setLoggingLevel(Log.ERROR);

        //HTTPS
        SSLSocketFactory sf = SSLSocketFactory.getSocketFactory();
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER); // 允许所有主机的验证

        //SSLSocketFactory sf = MySSLSocketFactory.getSocketFactory();
        //sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER); // 允许所有主机的验证

        client.setSSLSocketFactory(sf);


    }

    public abstract static class TextHttpResponseHandler extends AsyncHttpResponseHandler {

        protected void onSuccess(int statusCode, Header[] headers, String responseString) {
        }

        protected void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            onSuccess(statusCode, headers, responseBody != null ? new String(responseBody) : null);

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            onFailure(statusCode, headers, responseBody != null ? new String(responseBody) : null, error);
        }
    }

    public abstract static class AsyncHttpResponseHandler extends com.loopj.android.http.AsyncHttpResponseHandler {
        @Override
        protected void handleMessage(Message message) {
            super.handleMessage(message);
            Object[] response;
            int status_code = 0;
            Header[] headers;
            byte[] response_body;
            Throwable throwable;

            try {
                switch (message.what) {
                    case SUCCESS_MESSAGE:
                        response = (Object[]) message.obj;
                        if (response != null && response.length >= 3) {
                            status_code = (Integer) response[0];
                            headers = (Header[]) response[1];
                            response_body = (byte[]) response[2];
//                            // Log.d(TAG, "handleMessage请求网址: "+ getRequestURI());
//                            // Log.d(TAG, "handleMessage数据返回: " + status_code + "," + (response_body!=null ? new String(response_body) : ""));

                            if (headers != null) {
//                                // Log.d(TAG, "handleMessage: 有头信息,通知开头状态："+CaiNiaoApplication.isOpenNotification);
                                for (Header header : headers) {
//                                    // Log.d(TAG, "handleMessage success header: " + header.getName() +" = " + "".equals(header.getValue()));
                                    if (header.getName().equalsIgnoreCase("inform") && !"".equals(header.getValue()) && header.getValue().length() > 10) {
                                        // Log.d(TAG, "handleMessage success header: " + new String(Base64Utils.decode(header.getValue())));
                                    }
                                    if (header.getName().equalsIgnoreCase("inform")) {
                                        if (!"".equals(header.getValue()) && header.getValue().length() > 10 && CaiNiaoApplication.isOpenNotification) {
//                                            // Log.d(TAG, "handleMessage: 有数据进行本地通知。");
                                            try {
                                                String tmp_message = header.getValue();
                                                tmp_message = new String(Base64Utils.decode(tmp_message));
//                                                // Log.d(TAG, "handleMessage: " + tmp_message);
                                                ACache.get(CaiNiaoApplication.getAppContext()).put("jumpUrl", tmp_message);
                                                JSONObject json_m = JSONObject.parseObject(tmp_message);
                                                JuduohuiMessage message1 = new JuduohuiMessage();
                                                message1.putMessage(json_m);
                                                EventBus.getDefault().post(message1);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }

                        } else {
                            // Log.d(TAG, "SUCCESS_MESSAGE didn't got enough params");
                        }
                        break;
                    case FAILURE_MESSAGE:
                        response = (Object[]) message.obj;
                        if (response != null && response.length >= 4) {
                            status_code = (Integer) response[0];
                            headers = (Header[]) response[1];
                            response_body = (byte[]) response[2];
                            throwable = (Throwable) response[3];
                            // Log.d(TAG, "handleMessage: " + status_code + "," + (response_body!=null ? new String(response_body) : ""));
                        } else {
                            // Log.d(TAG, "FAILURE_MESSAGE didn't got enough params");
                        }
                        break;
                }
            } catch (Throwable error) {
                onUserException(error);
            }
        }
    }

    public static void get_third(String urlString, Object tag, AsyncHttpResponseHandler res) {
        res.setTag(tag);
        client.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1");
        client.setEnableRedirects(true, true, true);
        client.get(urlString, res);
    }

    public static void get(Object tag, String urlString, AsyncHttpResponseHandler res) // 用一个完整url获取一个string对象
    {
        res.setTag(tag);
        String token = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "token", "");

        client.addHeader("Content-type", "text/html;charset=UTF-8");
        client.addHeader("token", token);
        client.get(urlString, res);
    }

    /**
     * post提交
     *
     * @param urlString 接口地址
     * @param params
     * @param res
     */
    public static void post(String urlString, Object tag, RequestParams params, AsyncHttpResponseHandler res) {
        res.setTag(tag);
        String token = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), Constants.TOKEN, "");
        client.addHeader("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
        client.addHeader("token", token);
        params.put("token", token);
        LogUtils.log("请求网址：" + urlString);
        LogUtils.log("请求参数：" + params.toString());
        client.post(urlString, params, res);
    }

    public static void postjm(String urlString, Object tag, RequestParams params, AsyncHttpResponseHandler res) {
        res.setTag(tag);
        String token = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), Constants.TOKEN, "");
        client.addHeader("Content-type", "form-data;charset=UTF-8");
        client.addHeader("token", token);
        params.put("token", token);
        LogUtils.logd("请求网址：" + urlString);
        LogUtils.logd("请求参数：" + params.toString());
        client.post(urlString, params, res);
    }

    public static void post1(String urlString, Object tag, RequestParams params, AsyncHttpResponseHandler res) {
        res.setTag(tag);
        client.addHeader("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
        LogUtils.logd("请求网址：" + urlString);
        LogUtils.logd("请求参数：" + params.toString());
        client.post(urlString, params, res);
    }

    /**
     * post提交
     *
     * @param urlString 接口地址
     * @param params
     * @param res
     */
    public static void postUpload(String urlString, Object tag, RequestParams params, AsyncHttpResponseHandler res) {
        res.setTag(tag);
        AsyncHttpClient client1 = new AsyncHttpClient();
        String token = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "token", "");
        client1.addHeader("token", token);
        params.put("token", token);
        LogUtils.logd("请求网址：" + urlString);
        LogUtils.logd("请求参数：" + params.toString());
        client1.post(urlString, params, res);
    }

    public static void get(Object tag, String urlString, RequestParams params, AsyncHttpResponseHandler res) // url里面带参数
    {
        res.setTag(tag);
        String token = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "token", "");
        client.addHeader("Content-type", "text/html;charset=UTF-8");
        client.addHeader("token", token);
        params.put("token", token);
        LogUtils.logd("请求网址：" + urlString);
        LogUtils.logd("请求参数：" + params.toString());
        client.get(urlString, params, res);
    }

    /**
     * @param urlString
     * @param params
     * @param res
     */
    public static void post(boolean is_loginorregister, String urlString, Object tag, RequestParams params, AsyncHttpResponseHandler res) // url里面带参数
    {
        res.setTag(tag);
        String token = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "token", "");

        client.addHeader("token", token);
//        client.addHeader("Content-type","text/html;charset=UTF-8");//application/x-www-form-urlencoded
        client.addHeader("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
        params.put("token", token);
        LogUtils.logd("请求网址：" + urlString);
        LogUtils.logd("请求参数：" + params.toString());
        client.post(urlString, params, res);
    }

    public static void get(Object tag, String urlString, JsonHttpResponseHandler res) // 不带参数，获取json对象或者数组
    {
        res.setTag(tag);
        String token = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "token", "");

        client.addHeader("Content-type", "text/html;charset=UTF-8");
        client.addHeader("token", token);
        LogUtils.d(TAG, "请求网址：" + urlString);
        client.get(urlString, res);
    }

    public static void post(Object tag, String urlString, JsonHttpResponseHandler res) // 不带参数，获取json对象或者数组
    {
        res.setTag(tag);
        String token = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "token", "");
//        if (TextUtils.isEmpty(token)) {
//            Intent intent = new Intent(CaiNiaoApplication.getAppContext(), LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            CaiNiaoApplication.getAppContext().startActivity(intent);
//            return;
//        }
        client.addHeader("Content-type", "text/html;charset=UTF-8");
        client.addHeader("token", token);
        LogUtils.d(TAG, "请求网址：" + urlString);
        client.post(urlString, res);
    }

    public static void get(Object tag, String urlString, RequestParams params, JsonHttpResponseHandler res) // 带参数，获取json对象或者数组
    {
        res.setTag(tag);
        String token = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "token", "");

        client.addHeader("Content-type", "text/html;charset=UTF-8");
        client.addHeader("token", token);
        params.put("token", token);
        LogUtils.d(TAG, "请求网址：" + urlString);
        LogUtils.d(TAG, "请求参数：" + params.toString());
        client.get(urlString, params, res);
    }

    public static void post(Object tag, String urlString, RequestParams params, JsonHttpResponseHandler res) // 带参数，获取json对象或者数组
    {
        res.setTag(tag);
        String token = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "token", "");

        client.addHeader("Content-type", "application/json;charset=UTF-8");
//        client.addHeader("Content-type","application/json");

        client.addHeader("token", token);
        params.put("token", token);
        LogUtils.d(TAG, "请求网址：" + urlString);
        LogUtils.d(TAG, "请求参数：" + params.toString());
        client.post(urlString, params, res);
    }

    //TODO json提交数据
    public static void post(Object tag, Context mContext, String urlString, String jsonString, JsonHttpResponseHandler res) // 带参数，获取json对象或者数组
    {
        res.setTag(tag);
        String token = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "token", "");

        client.addHeader("token", token);
        ByteArrayEntity entity = null;
//        try {
//            entity = new ByteArrayEntity(jsonString.getBytes("UTF-8"));
//            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        client.post(mContext,urlString,entity,"application/x-www-form-urlencoded;charset=UTF-8",res);
        try {
            entity = new ByteArrayEntity(jsonString.getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        LogUtils.d(TAG, "请求网址：" + urlString);
        LogUtils.d(TAG, "请求参数：" + jsonString);
        client.post(mContext, urlString, entity, "application/json", res);
    }

    public static void get(Object tag, String uString, BinaryHttpResponseHandler bHandler) // 下载数据使用，会返回byte数据
    {
        bHandler.setTag(tag);
        String token = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "token", "");

        client.addHeader("token", token);
        LogUtils.d(TAG, "请求网址：" + uString);
        client.get(uString, bHandler);
    }

    public static void post(Object tag, String uString, BinaryHttpResponseHandler bHandler) // 下载数据使用，会返回byte数据
    {
        bHandler.setTag(tag);
        String token = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "token", "");

        client.addHeader("Content-type", "text/html;charset=UTF-8");
        client.addHeader("token", token);
        client.post(uString, bHandler);
    }


    public static void gethmedia(String backheaders, String uString, BinaryHttpResponseHandler bHandler) {
        String token = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "token", "");
        client.addHeader("token", token);
        client.addHeader("backheaders", backheaders);
        LogUtils.d(TAG, "请求网址：" + uString);
        client.get(uString, bHandler);
    }

    public static void posthmedia(String urlString, RequestParams params, TextHttpResponseHandler res) {
        String token = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "token", "");
        client.addHeader("Content-type", "application/json;charset=UTF-8");
//        client.addHeader("Content-type","application/json");
        client.addHeader("token", token);

        params.put("token", token);

        LogUtils.d(TAG, "请求网址：" + urlString);
        LogUtils.d(TAG, "请求参数：" + params.toString());
        client.post(urlString, params, res);
    }

    public static AsyncHttpClient getClient() {
        return client;
    }
}
