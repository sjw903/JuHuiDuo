package com.android.jdhshop.https;

import com.android.jdhshop.bean.Response;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.config.Stringserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import cz.msebera.android.httpclient.Header;

//import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;

/**
 * 开发者：陈飞
 * 时间:2018/7/20 09:48
 * 说明：增强版返回响应体,自动解析
 */
public abstract class onOKJsonHttpResponseHandler<T> extends HttpUtils.TextHttpResponseHandler {

    private Class<T> aClass;
    private Gson gson;
    private TypeToken<Response<T>> fromJson;

    private Response<T> response;


    public onOKJsonHttpResponseHandler(TypeToken<Response<T>> fromJson) {
        gson = new GsonBuilder()
                .registerTypeAdapter(String.class, new Stringserializer()) //自定义反序列化
                .create();
        this.fromJson = fromJson;
    }


    public onOKJsonHttpResponseHandler() {
        gson = new Gson();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBytes, Throwable throwable) {
        if (headers!=null) {
            for (Header header :
                    headers) {
                LogUtils.d("Fail", header.getName() + ":" + header.getValue());
            }
        }

        super.onFailure(statusCode, headers, responseBytes, throwable);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        if (fromJson == null) {
            throw new NullPointerException("你传的TypeToken<Response<T>>为空");
        }
        for (Header header : getRequestHeaders()) {
            LogUtils.d("<-------", "请求[头]:------->" +header);
        }
        LogUtils.d("<-------", "请求网址:------->" +getRequestURI().toString());
        LogUtils.d("<-------", "请求响应:------->" + responseString);
        try {
            response = gson.fromJson(responseString, fromJson.getType());
            onSuccess(statusCode, response);
        }
        catch (Exception e){
            e.printStackTrace();
            onFailure(statusCode,headers,responseString.getBytes(),e.fillInStackTrace());
        }

    }

    public abstract void onSuccess(int statusCode, Response<T> datas);
}
