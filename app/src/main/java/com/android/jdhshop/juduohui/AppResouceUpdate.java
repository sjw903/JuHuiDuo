package com.android.jdhshop.juduohui;

import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.SyncHttpUtils;
import com.android.jdhshop.utils.DownloadUtil;
import com.android.jdhshop.zip.ZipFileUtil;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;

import java.io.File;
import java.io.FileOutputStream;

import cz.msebera.android.httpclient.Header;

public class AppResouceUpdate {
    private static final String TAG = "AppResouceUpdate";
    public interface UpdateCallBack{
        void success();
        void fail();
    }

    public static void downloadResouce(int version_code, String url, ProgressBar progressBar,UpdateCallBack callBack){
        File img_file_path = new File(Constants.ZIYUAN_PATH);
        if (!img_file_path.exists()) img_file_path.mkdirs();
        DownloadUtil.get().download(url, Constants.MAIN_FILE_PATH, "juduohuiziyuan.zip", new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                LogUtils.d(TAG, "onDownloadSuccess: 文件下载成功！\n 下一步解决文件"+ file.getAbsolutePath());
                try {
                    Context mContext = CaiNiaoApplication.getAppContext();
                    //new File("/sdcard/com.android.jdhshop/resources/.image/juduohuiziyuan.zip")
                    int result = ZipFileUtil.upZipFile(file, Constants.ZIYUAN_PATH);

                    LogUtils.d(TAG, "onDownloadSuccess: 解压结果" + result);
                    if (result == -1){
                        callBack.fail();
                        return;
                    }
                    LogUtils.d(TAG, "onDownloadSuccess: 这里还会执行吗？");
                    // 记录不是首次启动
                    SPUtils.saveBoolean(mContext,"isFirstBoot",false);
                    // 记录已更新的版本号。
                    String update_history = SPUtils.getStringData(mContext,"update_history","");
                    JSONArray update_history_obj = null;
                    if (update_history.equals("")){
                        update_history_obj = new JSONArray();
                    }
                    else{
                        update_history_obj = JSONArray.parseArray(update_history);
                    }

                    JSONObject update_item = new JSONObject();
                    update_item.put("code",version_code);
                    update_item.put("url",url);
                    update_history_obj.add(update_item);
                    SPUtils.saveStringData(mContext,"update_history",update_history_obj.toJSONString());
                    callBack.success();

                } catch (Exception e) {
                    e.printStackTrace();
                    callBack.fail();
                    //Toast.makeText(AdActivity.this, "部分资源包下载失败,请退出从新进入应用", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onDownloading(int progress) {
                if (progressBar!=null) progressBar.setProgress(progress);
            }

            @Override
            public void onDownloadFailed(Exception e) {
                LogUtils.d("Exception----------", e + "");
                callBack.fail();
            }
        });
    }
    public static void checkUpdate(Context mContext, ProgressBar progressBar,UpdateCallBack callBack){
        Log.d(TAG, "checkUpdate: 进入检测" + Constants.MAIN_FILE_PATH + " ... " + Constants.ZIYUAN_PATH);
        // 是否为首次启动。
        boolean isFirstBoots= SPUtils.getBoolean(mContext,"isFirstBoot",true);

        // 如果是小米或者华为渠道则资源首页地址变更为 /data/data/package/files/juduohui/
//        if ("xm".equals(CaiNiaoApplication.getAppChannel()) || "hw".equals(CaiNiaoApplication.getAppChannel()) ){
//            Constants.MAIN_FILE_PATH = "";
//        }

        File img_file_path = new File(Constants.ZIYUAN_PATH);
        // 如果资源目录不存在，则认为为首次进入
        if (!img_file_path.exists()) isFirstBoots = true;
        try{
            File main_path = new File(Constants.MAIN_FILE_PATH);
            if (!main_path.exists()) main_path.mkdirs();
            File no_media_file = new File(main_path,".nomedia");
            if (!no_media_file.exists()) no_media_file.createNewFile();
        }
        catch (Exception e){
            e.printStackTrace();
            callBack.fail();
        }
        // 如果资源文件不存在，则认为为首次进入
        if (!fileIsExists(Constants.ZIYUAN_PATH+"icon_life_g.png")) isFirstBoots = true;
        final boolean isFirstBoot = isFirstBoots;
        if (isFirstBoot) SPUtils.saveStringData(mContext,"update_history","");



        HttpUtils.get(mContext,Constants.DOWNLOADZIP, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogUtils.d(TAG, "onFailure: 资源列表接口请求失败");
                throwable.printStackTrace();
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d(TAG, "onSuccess: " + responseString +"," + isFirstBoot);
                try {
                    com.alibaba.fastjson.JSONObject res = com.alibaba.fastjson.JSONObject.parseObject(responseString);
                    com.alibaba.fastjson.JSONArray ver_list = res.getJSONArray("list");
                    if (ver_list != null) {
                        int[] ver_list_arr = AssetTools.sortVersion(res.getJSONArray("list"), "version");
                        String version_oss_url = "";
                        // 首次更新进度条，其它进行静默更新。
                        if (isFirstBoot) {
                            LogUtils.d(TAG, "onSuccess: 弹窗更新");
                            for (int i = 0; i < ver_list.size(); i++) {
                                if (ver_list.getJSONObject(i).getIntValue("version") == ver_list_arr[0]) {
                                    // 开始更新喽
                                    version_oss_url = ver_list.getJSONObject(i).getString("oss_url");
                                }
                            }

                            if (!version_oss_url.equals("")) {
                                downloadResouce(ver_list_arr[0], version_oss_url, progressBar, callBack);
                            } else {
                                LogUtils.d(TAG, "onSuccess: 资源解析失败！");
                                callBack.fail();
                            }
                        }
                        else {
                            LogUtils.d(TAG, "onSuccess: 静默更新");
                            new Thread(()->{
                                String update_history = SPUtils.getStringData(mContext,"update_history","");
                                JSONArray update_history_obj = null;
                                if (update_history.equals("")){
                                    update_history_obj = new JSONArray();
                                }
                                else{
                                    update_history_obj = JSONArray.parseArray(update_history);
                                }

                                for (int version_code : ver_list_arr){

                                    boolean is_updated = false;
                                    // 检测是否已经更新过。
                                    for (int z=0;z<update_history_obj.size();z++){
                                        if (update_history_obj.getJSONObject(z).getIntValue("code") == version_code){
                                            is_updated = true;
                                            break;
                                        }
                                    }

                                    if (is_updated) continue;

                                    for (int j = 0 ; j < ver_list.size(); j++){
                                        if (version_code == ver_list.getJSONObject(j).getIntValue("version")){
                                            byte[] zip_temp = SyncHttpUtils.down(ver_list.getJSONObject(j).getString("oss_url"));
                                            File file = new File(Constants.MAIN_FILE_PATH,"temp.zip");
                                            try {
                                                if (!file.exists()) file.createNewFile();
                                                file.setWritable(true);
                                                FileOutputStream fo = new FileOutputStream(file);
                                                fo.write(zip_temp);
                                                fo.flush();
                                                fo.close();
                                                ZipFileUtil.upZipFile(file,Constants.MAIN_FILE_PATH+"/image");
                                                // 写入历史记录
                                                JSONObject update_item = new JSONObject();
                                                update_item.put("code",version_code);
                                                update_item.put("url",ver_list.getJSONObject(j).getString("oss_url"));
                                                update_history_obj.add(update_item);
                                                SPUtils.saveStringData(mContext,"update_history",update_history_obj.toJSONString());

                                                LogUtils.d(TAG, "onSuccess: " + version_code + "," + ver_list.getJSONObject(j).getString("oss_url"));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                callBack.fail();
                                            }
                                        }
                                    }

                                    callBack.success();
                                }
                            }).start();
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                    callBack.fail();
                }
            }
        });
    }
    //strFile 为文件名称 返回true为存在
    public static boolean fileIsExists(String strFile) {
        try {
            File f=new File(strFile);
            if(f.exists()) {
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
