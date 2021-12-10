package com.android.jdhshop.juduohui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.BridgeActivity;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.utils.DownloadUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.android.jdhshop.config.Constants.NOTIFICATION_ACTION;

public class NotificationService extends Service {
    private final String TAG = getClass().getSimpleName();
    private int message_cache_time = 30;
    private int message_interval_time = 0;
    private int icon_cache_time = 30;

    private Notification notification;
    private NotificationCompat.Builder builder;
    private NotificationManager manager;
    private final String CHANNEL_ID = "常驻通知";
    private final int NOTIFICATION_ID = 998;

    private JSONArray icon_array = new JSONArray();
    private JSONArray message_array = new JSONArray();
    private int current_message_index = 0;

    private RemoteViews custom_view; // 展开的
    private RemoteViews custom_mini; // 收缩的
    private MessageHandler messageHandler;

    private final int UPDATE_MESSAGE = 1; // 更新文本消息
    private final int UPDATE_ICON = 2; // 更新图标信息

    private final List<Integer> image_view_list = new ArrayList<>(); // 1-5个小图标的名称列表
    private final int REQUEST_CODE_TEXT = 1000;
    private final int REQUEST_CODE_ICON = 3000;

    private int need_download = 0;
    private int download_count = 0;
    private int custom_view_use_count = 0;
    private final int custom_view_use_max = 50;

    private final ACache cache;
    private final Context mContext;

    private boolean isInit = false;

    public NotificationService() {
        image_view_list.add(R.id.tz_image);
        image_view_list.add(R.id.tz_image1);
        image_view_list.add(R.id.tz_image2);
        image_view_list.add(R.id.tz_image3);
        image_view_list.add(R.id.tz_image4);

        // 初始化handler
        messageHandler = new MessageHandler();
        mContext = CaiNiaoApplication.getAppContext();
        // 初始化缓存
        cache = ACache.get(mContext);


        // 初始化通知
        initNotification();
        // 获取线上ICON配置
        getOnlineIcon();
        // 获取线上TEXT配置
        getOnlineMessage();
    }

    class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case UPDATE_ICON:
                    //updateNotificationIcon();
                    getOnlineIcon();
                    break;
                case UPDATE_MESSAGE:
                    if (cache.getAsJSONArray("message_list") == null) {
                        getOnlineMessage();
                    }
                    else {
                        updateNotificationText();
                    }
                    break;
            }
        }
    }

    private void initNotification() {
        builder = new NotificationCompat.Builder(mContext);
        manager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {        //Android 8.0适配
            // 为了兼容过去的，先删除掉原来的Channel
            if (!isInit) {
                try {
                    manager.deleteNotificationChannel(CHANNEL_ID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "聚多惠常驻通知栏", NotificationManager.IMPORTANCE_LOW);
                manager.createNotificationChannel(channel);
                isInit = true;
            }

            builder = new NotificationCompat.Builder(mContext,CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(mContext);

        }
        NotificationCompat.Style style = new NotificationCompat.DecoratedCustomViewStyle();
        if (custom_view  == null) custom_view = new RemoteViews(mContext.getPackageName(), R.layout.changzhutongzhi_view);
        if (custom_mini  == null) custom_mini = new RemoteViews(mContext.getPackageName(), R.layout.changzhutongzhi_min_view);

        if (custom_view_use_count>custom_view_use_max){
            custom_view = new RemoteViews(mContext.getPackageName(), R.layout.changzhutongzhi_view);
            custom_mini = new RemoteViews(mContext.getPackageName(), R.layout.changzhutongzhi_min_view);
            custom_view_use_count = 0;
        }


        builder.setContentTitle("聚多惠通知")            //指定通知栏的标题内容
                .setContentText("省钱赚钱就来聚多惠，让钱包鼓起来！")             //通知的正文内容
                .setWhen(System.currentTimeMillis())                //通知创建的时间
                .setSmallIcon(R.mipmap.low_android_icon)    //通知显示的小图标，只能用alpha图层的图片进行设置
                .setLocalOnly(true)
                .setStyle(style)
                .setCustomBigContentView(custom_view)
                .setCustomContentView(custom_mini)
                .setOngoing(true)
                .setSound(null)
                .setVibrate(null)
                .setGroup("silent");
//                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.app_icon)); 不设置则不显示右侧大图
        notification = builder.build();

    }

    // 获取线上的通知图标
    private void getOnlineIcon() {
        HttpUtils.get(NotificationService.this,Constants.NOTIFICATION_ICON, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (headers!=null) {
                    for (Header header : headers) {
                        // Log.d(TAG, "onFailure: " + header.getName() + " = " + header.getValue());
                    }
                }
                // Log.d(TAG, "线上获取常驻通知ICON信息失败,请检查接口是否出错!");
                throwable.printStackTrace();
                messageHandler.sendEmptyMessageDelayed(UPDATE_ICON,icon_cache_time * 1000);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                JSONObject response = JSONObject.parseObject(responseString);
                if (response.getString("code").equals("S")) {
                    icon_cache_time = response.getIntValue("cache_time");
                    icon_array = response.getJSONArray("icon_list");
                    if (icon_array == null) icon_array = new JSONArray();
                    cache.put("icon_list",icon_array,icon_cache_time);

                    if (icon_array.size() == 0) {
                        custom_view.setViewVisibility(R.id.icon_boxs, View.GONE);
                        custom_view.setViewVisibility(R.id.tz_image,View.GONE);
                        for (int i = icon_array.size(); i < 5; i++) {
                            custom_view.setImageViewBitmap(image_view_list.get(i), null);
                            Intent intent = new Intent(mContext, BridgeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.setAction(NOTIFICATION_ACTION);
                            PendingIntent p_intent = PendingIntent.getActivity(mContext, REQUEST_CODE_ICON + i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            custom_view.setOnClickPendingIntent(image_view_list.get(i), p_intent);
                        }
                        send();
                        messageHandler.sendEmptyMessageDelayed(UPDATE_ICON,icon_cache_time*1000);
                    }
                    else{

                        need_download = icon_array.size();
                        download_count = 0;

//                        // Log.d(TAG, "onSuccess: 需要下载" + need_download +"," + download_count);

//                        for (int i=0;i<5;i++){
//                            try{
//                                File tmp_file = new File(getFilesDir(),"icon_"+i+".png");
//                                tmp_file.deleteOnExit();
//                            }
//                            catch (Exception e){
//                                e.printStackTrace();
//                            }
//                        }
                        for (int i = 0; i< need_download; i++){
                            final int j = i;
//                            // Log.d(TAG, "onSuccess: 下载ICON网址：" + icon_array.getJSONObject(i).getString("icon"));
                            String[] tmp_url = icon_array.getJSONObject(i).getString("icon").split("/");
//                            // Log.d(TAG, "onSuccess: " + tmp_url[tmp_url.length-1]);
                            final String target_file_name = tmp_url[tmp_url.length-1];

                            File check_file = new File(getFilesDir(),target_file_name);
//                            // Log.d(TAG, "onSuccess: " + check_file.exists());
                            if (check_file.exists()){
                                // 文件存在不需要下载。
//                                // Log.d(TAG, "onSuccess: 文件存在不需要下载。");
                                commonSetView(check_file,j);
                            }else
                            {
                                // 文件不存在，下载
//                                // Log.d(TAG, "onSuccess: 文件不存在，下载。");
                                DownloadUtil.get().download(icon_array.getJSONObject(i).getString("icon"), getFilesDir().getPath(), target_file_name, new DownloadUtil.OnDownloadListener() {
                                    @Override
                                    public void onDownloadSuccess(File file) {
                                        commonSetView(file,j);
                                    }

                                    @Override
                                    public void onDownloading(int progress) {}

                                    @Override
                                    public void onDownloadFailed(Exception e) {
                                        custom_view.setViewVisibility(image_view_list.get(j),View.GONE);
                                        custom_view.setImageViewBitmap(image_view_list.get(j), null);
                                        Intent intent = new Intent(mContext, BridgeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        intent.setAction(NOTIFICATION_ACTION);
                                        PendingIntent p_intent = PendingIntent.getActivity(mContext, REQUEST_CODE_ICON + j, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        custom_view.setOnClickPendingIntent(image_view_list.get(j), p_intent);
//                                        // Log.d(TAG, "onDownloadFailed: " + j +"个下载失败");
                                    }
                                });
                            }
                        }

                        if (need_download < image_view_list.size()){
                            for (int i = need_download; i < image_view_list.size(); i++) {
                                custom_view.setViewVisibility(image_view_list.get(i),View.GONE);
                                custom_view.setImageViewBitmap(image_view_list.get(i), null);
                                Intent intent = new Intent(mContext, BridgeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intent.setAction(NOTIFICATION_ACTION);
                                PendingIntent p_intent = PendingIntent.getActivity(mContext, REQUEST_CODE_ICON + i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                custom_view.setOnClickPendingIntent(image_view_list.get(i), p_intent);
                            }
                        }
                    }

                } else {
//                    // Log.d(TAG, "线上获取常驻通知ICON信息失败,请检查接口是否正确");
                    // 接口信息出错则再次计划队列
                    messageHandler.sendEmptyMessageDelayed(UPDATE_ICON,icon_cache_time * 1000);
                }
            }
        });
    }

    private void commonSetView(File file_name,int j){

        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        op.inSampleSize = 2;
        op.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(file_name.getPath(), op);

        custom_view.setViewVisibility(image_view_list.get(j),View.VISIBLE);
        custom_view.setImageViewBitmap(image_view_list.get(j), bitmap);
        Intent intent = new Intent(mContext, BridgeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setAction(NOTIFICATION_ACTION);
        intent.putExtra("path", icon_array.getJSONObject(j).getString("path"));
        intent.putExtra("name", icon_array.getJSONObject(j).getString("name"));
        intent.putExtra("toast", icon_array.getJSONObject(j).getString("toast"));
        PendingIntent p_intent;

        if (icon_array.getJSONObject(j).getString("path").contains(",")) {

            Intent intent2 = new Intent(mContext,BridgeActivity.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent2.setAction(NOTIFICATION_ACTION);
//                                    intent2.setComponent(new ComponentName(getPackageName(),icon_array.getJSONObject(j).getString("path")));
            intent2.putExtra("path", icon_array.getJSONObject(j).getString("path"));
            intent2.putExtra("name", icon_array.getJSONObject(j).getString("name"));
            intent2.putExtra("toast", icon_array.getJSONObject(j).getString("toast"));

            p_intent = PendingIntent.getActivity(mContext, REQUEST_CODE_ICON + j,intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        else
        {
            p_intent = PendingIntent.getActivity(mContext, REQUEST_CODE_ICON + j, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
//                                    PendingIntent p_intent = PendingIntent.getActivities(mContext, REQUEST_CODE_ICON + j, new Intent[]{intent,intent2}, PendingIntent.FLAG_UPDATE_CURRENT);
        custom_view.setOnClickPendingIntent(image_view_list.get(j), p_intent);

        download_count = download_count +1;

//        // Log.d(TAG, "onDownloadSuccess: 下载完成第"+ j +"个，总："+download_count);

        if (download_count == need_download){
            cache.put("icon_list", icon_array, icon_cache_time);
            custom_view.setViewVisibility(R.id.icon_boxs, View.VISIBLE);
            custom_view.setViewVisibility(R.id.tz_image,View.VISIBLE);
//            // Log.d(TAG, "onDownloadSuccess: 都下载完了，发送通知");
            send();
            messageHandler.sendEmptyMessageDelayed(UPDATE_ICON,icon_cache_time * 1000);
        }
    }
    // 获取上线的通知文本
    private void getOnlineMessage() {
        message_array.clear();
//        // Log.d(TAG, "getOnlineMessage: ");
        HttpUtils.get(NotificationService.this,Constants.NOTIFICATION_TEXT, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // Log.d(TAG, "线上获取常驻通知文本信息失败,请检查接口是否出错!");
                // Log.d(TAG, "onFailure: " + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (messageHandler ==null) messageHandler = new MessageHandler();
                // Log.d(TAG, Constants.NOTIFICATION_TEXT + ",onSuccess: " + responseString);
                JSONObject response = JSONObject.parseObject(responseString);
                message_cache_time = response.getIntValue("cache_time");
                message_interval_time = response.getIntValue("show_interval_time");

                if (response.getString("code").equals("S")) {
                    message_array = response.getJSONArray("text_list");
                    cache.put("message_list", message_array, message_cache_time);
                    //messageHandler.sendEmptyMessageDelayed(UPDATE_MESSAGE,message_interval_time * 1000 );

                    messageHandler.sendEmptyMessage(UPDATE_MESSAGE);
                } else {
//                    // Log.d(TAG, "线上获取常驻通知文本信息失败,请检查接口是否正确");
                    messageHandler.sendEmptyMessageDelayed(UPDATE_MESSAGE,message_cache_time * 1000 );
                }
            }
        });
    }

    private void send() {

        // Log.d(TAG, "send: " + custom_view_use_count);
        if (manager == null) manager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        if (message_array.size() ==0) {
            manager.cancel(NOTIFICATION_ID);
            return;
        }
        
        initNotification();
        notification.bigContentView = custom_view;
        notification.contentView = custom_mini;
        manager.notify(NOTIFICATION_ID, notification);
        custom_view_use_count++;
    }

    // 更新通知文本
    private void updateNotificationText() {
            // 一系列的操作
            if (message_array.size()>0) {

                if (current_message_index >= message_array.size()) current_message_index = 0;
                Intent i = new Intent(mContext, BridgeActivity.class);
                i.setAction(NOTIFICATION_ACTION);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.putExtra("name","text_message");
                i.putExtra("icon","null");
                i.putExtra("path", message_array.getJSONObject(current_message_index).getString("path"));

                PendingIntent p_intent;
                if (message_array.getJSONObject(current_message_index).getString("path").contains(",")) {

                    Intent intent2 = new Intent(mContext,BridgeActivity.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent2.setAction(NOTIFICATION_ACTION);
//                                    intent2.setComponent(new ComponentName(getPackageName(),icon_array.getJSONObject(j).getString("path")));
                    intent2.putExtra("path", message_array.getJSONObject(current_message_index).getString("path"));

                    p_intent = PendingIntent.getActivity(mContext, REQUEST_CODE_TEXT + current_message_index,intent2, PendingIntent.FLAG_UPDATE_CURRENT);
                }
                else
                {
                    p_intent = PendingIntent.getActivity(mContext, REQUEST_CODE_TEXT + current_message_index, i, PendingIntent.FLAG_UPDATE_CURRENT);
                }


//                PendingIntent p_intent = PendingIntent.getActivity(mContext, REQUEST_CODE_TEXT + current_message_index, i, PendingIntent.FLAG_UPDATE_CURRENT);
                custom_view.setTextViewText(R.id.tz_title, message_array.getJSONObject(current_message_index).getString("title"));
                custom_mini.setTextViewText(R.id.tz_title, message_array.getJSONObject(current_message_index).getString("title"));
                custom_view.setTextViewText(R.id.tz_content, message_array.getJSONObject(current_message_index).getString("content"));
                custom_mini.setTextViewText(R.id.tz_content, message_array.getJSONObject(current_message_index).getString("content"));

                custom_view.setOnClickPendingIntent(R.id.tz_title, p_intent);
                custom_view.setOnClickPendingIntent(R.id.tz_content, p_intent);
                custom_mini.setOnClickPendingIntent(R.id.tz_title, p_intent);
                custom_mini.setOnClickPendingIntent(R.id.tz_content, p_intent);

                current_message_index = current_message_index + 1;
                send();
                // 进行下次更新显示计划
//                // Log.d(TAG, "updateNotificationText: 进行下次更新显示计划,"+message_interval_time);
                messageHandler.sendEmptyMessageDelayed(UPDATE_MESSAGE, message_interval_time*1000);
            }
            else{
                // 进行下次更新数据计划
//                // Log.d(TAG, "updateNotificationText: 进行下次更新数据计划," + message_cache_time*1000);
                manager.cancel(NOTIFICATION_ID);
                messageHandler.sendEmptyMessageDelayed(UPDATE_MESSAGE,message_cache_time*1000);
            }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        messageHandler.removeCallbacksAndMessages(null);
        manager.cancelAll();
        manager = null;
        notification = null;
        messageHandler = null;
//        // Log.d(TAG, "Constants.: " + intent);
        stopSelf();
        return super.onUnbind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    class MyBinder extends Binder {
        MyBinder() {}
    }
}