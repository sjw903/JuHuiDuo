package com.android.jdhshop.juduohui;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.https.SyncHttpUtils;
import com.android.jdhshop.zip.ZipFileUtil;
import com.loopj.android.http.RequestParams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.ListIterator;

import cz.msebera.android.httpclient.Header;

public class AssetTools {

    private static final String TAG = "AssetsCopyer";
    private static int local_web_app_version = 0;

    /**
     * 判断是否为首次安装
     * @param context 上下文
     * @return boolean true 首次，false 覆盖
     */
    public static boolean isFirstInstall(Context context) {
        return getPackageFirstInstallTime(context) == getPackageLastUpdateTime(context);
    }

    /**
     * 取安装包的首次安装日期
     * @param context 上下文
     * @return long time
     */
    public static long getPackageFirstInstallTime(Context context) {
        String name = context.getPackageName();
        long time = 0;
        try {
            time = context.getPackageManager().getPackageInfo(name, 0).firstInstallTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 取安装包的最后更新日期
     * @param context 上下文
     * @return long time
     */
    public static long getPackageLastUpdateTime(Context context) {
        String name = context.getPackageName();
        long time = 0;
        try {
            time = context.getPackageManager().getPackageInfo(name, 0).lastUpdateTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 检测线上的程序是否有更新
     *
     * @param mContext 上下文
     */
    public static void checkUpdate(Context mContext) {
        File web_app_root = mContext.getExternalFilesDir("web_app");
        AssetManager assetManager = mContext.getAssets();
        try {
            InputStream local_version_is = null;
            File version_file = new File(web_app_root,"version.txt");
            // 如果存在已释放的WEB_APP版本号则使用这个版本号
            if (version_file.exists()){
                local_version_is = new FileInputStream(version_file);
                // Log.d(TAG, "checkUpdate: 使用SD卡上的文件版本");
            }
            else{
                local_version_is = assetManager.open("web_app/version.txt");
                // Log.d(TAG, "checkUpdate: 使用asserts上的文件版本");
            }

            InputStreamReader local_version_ir = new InputStreamReader(local_version_is);
            BufferedReader bufferedReader = new BufferedReader(local_version_ir);
            local_web_app_version = Integer.parseInt(bufferedReader.readLine());
            bufferedReader.close();
            local_version_ir.close();
            local_version_is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestParams req = new RequestParams();
        req.put("local_version", local_web_app_version);

        HttpUtils.post(Constants.APP_IP + "/api/webAppUpdate",mContext, req, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // Log.d(TAG, "onFailure: 请求失败," + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                // Log.d(TAG, "onSuccess: " + responseString);
                JSONObject update_info = JSONObject.parseObject(responseString);
                // 获取版本号成功
                if (update_info.getString("code").equals("0")) {
                    JSONArray version_list = update_info.getJSONArray("list");
                    int [] ver_code_list = sortVersion(version_list,"version");
                    for (int ver : ver_code_list){
                        // 需要更新
                        // Log.d(TAG, "onSuccess: " + ver +" >" + local_web_app_version);
                        if (ver > local_web_app_version) {
                            String update_url = getFeildString(version_list,ver,"oss_url");
                            if (update_url == null) {
                                // Log.d(TAG, "onSuccess: 获取的网址不存在，JSON结构有问题");
                            }
                            else{
                                // Log.d(TAG, "onSuccess: 从线上下载压缩包，更新压缩包从线上下载压缩包，更新压缩包," + ver);
                                new Thread(()->{
                                    updateOnlineResource(update_url);
                                }).start();
                            }
                        }
                    }
                } else {
                    // Log.d(TAG, "onSuccess: 获取线上更新信息失败。");
                }
            }
        });
    }
    public static void updateOnlineResource(String online_url){
        // Log.d(TAG, "从线上下载压缩包，更新压缩包" + online_url);

        byte[] result = SyncHttpUtils.down(online_url);
        if (result!=null) {
            // Log.d(TAG, "onResponse: 下载" + online_url + "成功");
            // Log.d(TAG, "onSuccess: 下载文件完成，文件保存到" + CaiNiaoApplication.getAppContext().getFilesDir());
            File temp_file = new File(CaiNiaoApplication.getAppContext().getFilesDir(), "temp.zip");
            try {
                if (temp_file.exists()) temp_file.delete();
                temp_file.createNewFile();
                temp_file.setWritable(true);
                FileOutputStream fo = new FileOutputStream(temp_file);
                fo.write(result);
                fo.flush();
                fo.close();
                // Log.d(TAG, "onSuccess: 解压文件");
                if (ZipFileUtil.upZipFile(temp_file, CaiNiaoApplication.getAppContext().getExternalFilesDir("web_app").getPath()) == 0) {
                    // Log.d(TAG, "onSuccess: 解压已完成，删除掉临时文件");
                    temp_file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            // Log.d(TAG, "updateOnlineResource: 下载文件失败，下次再更新");
        }
    }
    public static String getFeildString(JSONArray version_list,int ver_code,String url_field){
        for (ListIterator<Object> it = version_list.listIterator(); it.hasNext(); ) {
            JSONObject i = (JSONObject) it.next();
            if (i.getIntValue("version") == ver_code){
                return i.getString(url_field);
            }
        }
        return null;
    }
    public static int[] sortVersion(JSONArray version_list,String sort_feild){
        int[] sort_arrays = new int[version_list.size()];

        for (int j=0;j<version_list.size();j++) {
            JSONObject version_item = version_list.getJSONObject(j);
            sort_arrays[j] = version_item.getIntValue(sort_feild);
        }
        Arrays.sort(sort_arrays);
        return sort_arrays;
    }

    public static void releaseAssets(Context context, String assetsDir, String releaseDir) {
        if (TextUtils.isEmpty(releaseDir)) {
            return;
        } else if (releaseDir.endsWith("/")) {
            releaseDir = releaseDir.substring(0, releaseDir.length() - 1);
        }

        if (TextUtils.isEmpty(assetsDir) || assetsDir.equals("/")) {
            assetsDir = "";
        } else if (assetsDir.endsWith("/")) {
            assetsDir = assetsDir.substring(0, assetsDir.length() - 1);
        }

        AssetManager assets = context.getAssets();
        try {
            String[] fileNames = assets.list(assetsDir);//只能获取到文件(夹)名,所以还得判断是文件夹还是文件
            if (fileNames.length > 0) {// is dir
                for (String name : fileNames) {
                    if (!TextUtils.isEmpty(assetsDir)) {
                        name = assetsDir + File.separator + name;//补全assets资源路径
                    }
//                    Log.i(, brian name= + name);
                    String[] childNames = assets.list(name);//判断是文件还是文件夹
                    if (!TextUtils.isEmpty(name) && childNames.length > 0) {
                        checkFolderExists(releaseDir + File.separator + name);
                        releaseAssets(context, name, releaseDir);//递归, 因为资源都是带着全路径,
                        //所以不需要在递归是设置目标文件夹的路径
                    } else {
                        InputStream is = assets.open(name);
//                        FileUtil.writeFile(releaseDir + File.separator + name, is);
                        writeFile(releaseDir + File.separator + name, is);
                    }
                }
            } else {// is file
                InputStream is = assets.open(assetsDir);
                // 写入文件前, 需要提前级联创建好路径, 下面有代码贴出
//                FileUtil.writeFile(releaseDir + File.separator + assetsDir, is);
                writeFile(releaseDir + File.separator + assetsDir, is);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean writeFile(String fileName, InputStream in) throws IOException {
        boolean bRet = true;
        try {
            OutputStream os = new FileOutputStream(fileName);
            byte[] buffer = new byte[4112];
            int read;
            while ((read = in.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }
            in.close();
            in = null;
            os.flush();
            os.close();
            os = null;
//			Log.v(TAG, "copyed file: " + fileName);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            bRet = false;
        }
        return bRet;
    }

    private static void checkFolderExists(String path) {
        File file = new File(path);
        if ((file.exists() && !file.isDirectory()) || !file.exists()) {
            file.mkdirs();
        }
    }

}