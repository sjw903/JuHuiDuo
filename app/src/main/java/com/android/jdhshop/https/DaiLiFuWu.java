package com.android.jdhshop.https;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.zip.CompressZip;
import com.loopj.android.http.AsyncHttpClient;
import com.android.jdhshop.https.HttpUtils.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import cz.msebera.android.httpclient.Header;

public class DaiLiFuWu {

    private static final String TAG = "DaiLiFuWu";

    public static void MyService(String request_json_string, Context mContext) {
        LogUtils.d(TAG, "全部参数：" + request_json_string);
        JSONObject request_obj = JSONObject.parseObject(request_json_string);
        // 请求组
        String request_url = request_obj.getString("url");
        String request_method = request_obj.getString("type");
        String request_header_str = request_obj.getString("headers");
        String request_data = request_obj.getString("data");
        // 返回组
        String response_url = request_obj.getString("backurl"); // 返回请求的url
        String response_method = request_obj.getString("backtype"); // 返回请求类型
        String response_header_str = request_obj.getString("backheaders"); // 返回请求的header
        String response_is_zip = request_obj.getString("zip_type"); // 是否为zip方式返回
        String response_data = request_obj.getString("backdata"); // 原样返回的数据
        String response_data_key = request_obj.getString("backkey"); // 请求回去的数据返回时对应的KEY


        // 处理 request的请求数据
        RequestParams request_params = new RequestParams();
        if (!request_data.equals("")) {
            JSONObject request_data_obj = JSONObject.parseObject(request_data);
            for (String req_item : request_data_obj.keySet()) {
                request_params.put(req_item, request_data_obj.getString(req_item));
            }
        }

        // 实例化 response 的 response

        AsyncHttpResponseHandler response_response_handler = new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                LogUtils.d(TAG, "onStart: 开始回调");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogUtils.d(TAG, "response_response_handler onFailure: " + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d(TAG, "response_response_handler onSuccess: " + responseString);
            }
        };

        // 实例化 request 的 response 处理
        AsyncHttpResponseHandler response_handler = new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogUtils.d(TAG, "onFailure: " + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d(TAG, "request 的 onSuccess: " + responseString);
                LogUtils.d(TAG, "onSuccess: "  + response_url);
                if (response_url != null && !response_url.equals("")) {
                    // 处理回调参数
                    LogUtils.d(TAG, "onSuccess: 进入回调缓解处理");
                    RequestParams response_params = new RequestParams();
                    if (response_data != null && !response_data.equals("")){
                        response_params.put("backdata",response_data);
//                        JSONObject res_obj = JSONObject.parseObject(response_data);
//                        for (String res_obj_item : res_obj.keySet()) {
//                            response_params.put(res_obj_item,res_obj.getString(res_obj_item));
//                        }
                    }

                    AsyncHttpClient response_client = HttpUtils.getClient();
                    response_client.removeAllHeaders();
                    // 处理返回的header
                    if (response_header_str!=null && !response_header_str.equals("")){
                        JSONObject res_header_obj = JSONObject.parseObject(response_header_str);
                        for (String res_header_obj_item :res_header_obj.keySet()){
                            response_client.addHeader(res_header_obj_item,res_header_obj.getString(res_header_obj_item));
                        }
                    }
                    // 使用zip 返回
                    if (response_is_zip.equals("1")) {
                        response_params.put("type",response_is_zip);
                        String tmp_zip_file_name = System.currentTimeMillis() +".zip";
                        File zip_temp_dir = mContext.getDir("zip_temp",Context.MODE_APPEND);
                        if (!zip_temp_dir.exists()) zip_temp_dir.mkdir();
                        File zip_temp_file = new File(zip_temp_dir,"data.json");
                            try {
                                zip_temp_file.createNewFile();
                                FileWriter f_w = new FileWriter(zip_temp_file);
                                BufferedWriter f_b_w = new BufferedWriter(f_w);
                                f_b_w.write(responseString);
                                f_b_w.flush();
                                f_b_w.close();
                                f_w.close();
                                CompressZip.ZipFolder(zip_temp_file.getAbsolutePath(),zip_temp_dir + "/" + tmp_zip_file_name);
                                LogUtils.d(TAG, "onSuccess: 传的zip包完整路径" + zip_temp_dir + "/" + tmp_zip_file_name);
                                File par = new File(zip_temp_dir + "/" + tmp_zip_file_name);
                                response_params.put(response_data_key,par);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                    }
                    // 使用 response_data_key 返回
                    else{
                        response_params.put(response_data_key,responseString);
                    }

                    LogUtils.d(TAG, "onSuccess: " + response_params.toString());
                    if (response_method == null && (response_method.equals("") || response_method.toLowerCase().equals("get"))){
                        response_client.get(response_url,response_params,response_response_handler);
                    }
                    else{
                        response_client.post(response_url,response_params,response_response_handler);
                    }

                }
            }
        };


        AsyncHttpClient client = HttpUtils.getClient();
        client.removeAllHeaders();
        // 处理headers
        JSONObject req_header_obj = JSONObject.parseObject(request_header_str);
        for (String req_header_item : req_header_obj.keySet()) {
            client.addHeader(req_header_item, req_header_obj.getString(req_header_item));
        }

        // post 请求
        if (request_method.toLowerCase().equals("post")) {
            client.post(request_url, request_params, response_handler);
        }
        // get 请求
        else {
            client.get(request_url, request_params, response_handler);
        }
    }
}
