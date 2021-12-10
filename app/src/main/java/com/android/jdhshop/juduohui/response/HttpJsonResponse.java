package com.android.jdhshop.juduohui.response;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public abstract class HttpJsonResponse extends TextHttpResponseHandler {
    private final String TAG = getClass().getSimpleName();
    protected void onSuccess(JSONObject response){}
    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
        // Log.d(TAG, "onFailure: " + responseString);
        e.printStackTrace();
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        // Log.d(TAG, "onSuccess: " + responseString);
        try {
            JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(responseString);
            onSuccess(jsonObject);
        }
        catch (Exception e){
            onFailure(statusCode,headers,responseString,e);
        }
    }
}