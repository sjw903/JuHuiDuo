package com.android.jdhshop.https;

import android.util.Log;

import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.loopj.android.http.DataAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class SyncHttpUtils {
    public static String decode(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }
        StringBuilder retBuf = new StringBuilder();
        int maxLoop = unicodeStr.length();
        for (int i = 0; i < maxLoop; i++) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i < maxLoop - 5) && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr.charAt(i + 1) == 'U')))
                    try {
                        retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        retBuf.append(unicodeStr.charAt(i));
                    }
                else
                    retBuf.append(unicodeStr.charAt(i));
            } else {
                retBuf.append(unicodeStr.charAt(i));
            }
        }
        return retBuf.toString();
    }
    public static byte[] down(String url){
        SyncThread syncThread = new SyncThread(url);
        syncThread.setMethod("down");
        syncThread.start();
        try {
            syncThread.join();
            while (syncThread.getState() != Thread.State.TERMINATED){
                syncThread.wait();
            }
            return syncThread.getDown_result();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static Map<String,String> get(String url){
        SyncThread syncThread = new SyncThread(url);
        syncThread.start();
        try {
            syncThread.join();
            while (syncThread.getState() != Thread.State.TERMINATED){
                syncThread.wait();
            }
            return syncThread.getResult();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static Map<String,String> post(String url, RequestParams requestParams){
        SyncThread syncThread = new SyncThread(url,requestParams);
        syncThread.start();
        try {
            syncThread.join();
            while (syncThread.getState() != Thread.State.TERMINATED){
                syncThread.wait();
            }
            return syncThread.getResult();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static class SyncThread extends Thread{
        private final Map<String,String> result = new HashMap<>();
        private byte[] down_result = null;
        private final String request_url;
        private final RequestParams request_params;
        private String method="get";
        private Runnable runnable;
        public SyncThread(String url){
            request_url = url;
            request_params = null;
        }
        public SyncThread(String url,RequestParams params){
            request_url = url;
            request_params = params;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        @Override
        public void run() {
            String TAG = "SyncThread";
            LogUtils.d(TAG, "run: ");
            SyncHttpClient client = new SyncHttpClient();
            if (request_params==null) {
                // Log.d(TAG, "run: 1");
                client.get(request_url, new DataAsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        down_result = responseBody;
                        if (method.equals("down")) return;
                        String charset=DEFAULT_CHARSET;
                        for(Header header:headers){
                            if ("Content-Type".equals(header.getName())){
                                charset = StringUtils.substringAfterLast(header.getValue(),"charset=");
                            }
                        }
                        String ss = "";
                        try {
                            ss =new String(responseBody,charset);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        if (result != null) result.clear();
                        result.put("code", "S");
                        result.put("data",ss);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
            }
            else
            {
                // Log.d(TAG, "run: 2");
                client.post(request_url, request_params, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        if (result != null) result.clear();
                        // Log.d(TAG, "onFailure: " + responseString);
                        result.put("code", "F");
                        result.put("data", responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        // Log.d(TAG, "onSuccess: " + responseString);
                        if (result != null) result.clear();
                        result.put("code", "S");
                        result.put("data", responseString);
                    }
                });
            }
            super.run();
        }

        @Override
        public synchronized void start() {
            super.start();
        }

        public Map<String, String> getResult() {
            return result;
        }
        public byte[] getDown_result() { return down_result; }

        public String getRequestUrl() {
            return request_url;
        }
    }
}
