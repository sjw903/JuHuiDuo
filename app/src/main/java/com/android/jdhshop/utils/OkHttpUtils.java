package com.android.jdhshop.utils;

import android.util.Log;

import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.IOException;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtils {
    private static OkHttpUtils okHttpUtil;
    private final OkHttpClient okHttpClient;


    private OkHttpUtils() {
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .build();
    }

    public static OkHttpUtils getInstance() {
        if (null == okHttpUtil) {
            synchronized (OkHttpUtils.class) {
                if (null == okHttpUtil) {
                    okHttpUtil = new OkHttpUtils();
                }
            }
        }
        return okHttpUtil;
    }

    //get 请求
    public void get(String urlString, Callback callback) {
        Request request = new Request.Builder().url(urlString).get().build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    //post请求
    public void post(String urlString, FormBody formBody, Callback callback) {
        Request request = new Request.Builder().url(urlString).method("POST", formBody).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public void postUpload(String urlString, FormBody formBody,String key, File upfile,Callback callback){
        Request.Builder builder = new Request.Builder();
        MultipartBody.Builder req_body_builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        for(int i=0; i< formBody.size(); i++){
            req_body_builder.addFormDataPart(formBody.name(i),formBody.value(i));
            Log.d("UPLOAD", "postUpload: " + formBody.name(i) +" = " + formBody.value(i) + " , " + formBody.encodedName(i) + " = " + formBody.encodedValue(i));
        }
        req_body_builder.addPart(MultipartBody.Part.createFormData(key,upfile.getName(),RequestBody.create(MediaType.parse("image/png"),upfile)));

        Request req =  builder.url(urlString).post(req_body_builder.build()).build();
        okHttpClient.newCall(req).enqueue(callback);
    }

    //添加拦截器
    class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            String method = request.method();
            Response proceed = chain.proceed(request);
            return proceed;
        }
    }
}