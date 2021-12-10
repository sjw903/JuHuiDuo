package com.android.jdhshop.wmm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.MainActivity;
import com.android.jdhshop.R;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * @author weimengmeng
 */
public class JpushReceiver extends BroadcastReceiver {
    private String TAG="JpushCommonReceiver";//getClass().getName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            CaiNiaoApplication.setRegisterid(regId);
            //send the Registration Id to your server...
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//            processCustomMessage(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            try {
                JSONObject object=new JSONObject(intent.getExtras().getString(JPushInterface.EXTRA_EXTRA));
             Log.d(TAG, "JpushReceiver onReceive: " + intent.getExtras().toString());
             Log.d(TAG, "JpushReceiver onReceive: " + object.toString());
                String title=object.getString("key");
                String message=object.getString("value");
                SPUtils.saveStringData(context,"inform_title",title);
                SPUtils.saveStringData(context,"inform_message",message);
                Intent intent1=new Intent(context,MainActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    //	send msg to Activity
    private void processCustomMessage(Context context, Bundle bundle) {
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String title=bundle.getString(JPushInterface.EXTRA_TITLE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        if (!TextUtils.isEmpty(extras)) {
            try {
                JSONObject extraJson = new JSONObject(extras);
                if (extraJson.length() > 0) {
                    try {
                        sndNotification(message,title,extraJson.getString("key"),extraJson.getString("value"));
                    } catch (JSONException e) {
                        sndNotification(message,title,extraJson.getString("key"),"");
                    }
                }
            } catch (JSONException e) {
                LogUtils.d("huan",e.toString());
            }
        }
    }
    /**
     * 发送通知
     *安卓8.0以上必须设置channel
     * @param message
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void sndNotification(String message,String title,String key,String value) {
        Context context=CaiNiaoApplication.getAppContext();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("hkx", "hkx", NotificationManager.IMPORTANCE_HIGH);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        int notifyId=SPUtils.getIntData(context,"notifyId",0);
        if(notifyId>=60000){
            notifyId=-1;
        }
        notifyId++;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Intent notificationIntent = new Intent(context, NotificcationClickReceiver.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationIntent.putExtra("title",key);
        notificationIntent.putExtra("message",value);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notifyId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentTitle(title)//设置通知栏标题
                .setContentIntent(pendingIntent) //设置通知栏点击意图
                .setContentText(message)
                .setTicker("通知内容") //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setSmallIcon(R.mipmap.app_icon)//设置通知小ICON
                .setChannelId("hkx")
                .setDefaults(Notification.DEFAULT_ALL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder
                    .setGroupSummary(false)
                    .setGroup("group");
        }
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        if (notificationManager != null) {
            notificationManager.notify(notifyId, notification);
        }
        SPUtils.saveIntData(context,"notifyId",notifyId);
    }
}
