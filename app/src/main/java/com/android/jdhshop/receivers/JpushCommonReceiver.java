package com.android.jdhshop.receivers;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.jdhshop.MainActivity;
import com.android.jdhshop.activity.BridgeActivity;
import com.android.jdhshop.common.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.CmdMessage;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class JpushCommonReceiver extends JPushMessageReceiver {
    private String TAG = "JpushCommonReceiver";
    public JpushCommonReceiver() {
        super();
    }

    @Override
    public Notification getNotification(Context context, NotificationMessage notificationMessage) {
        Log.d(TAG, "getNotification: " + notificationMessage);
        return super.getNotification(context, notificationMessage);
    }

    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        Log.d(TAG, "onMessage: " + customMessage);
        super.onMessage(context, customMessage);
    }

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        Log.d(TAG, "onNotifyMessageOpened: " + notificationMessage);
//        notificationMessage.
        try {
            JSONObject object=new JSONObject(notificationMessage.notificationExtras);
            Log.d(TAG, "JPUSH onReceive: " + notificationMessage.notificationExtras);
            String title=object.getString("key");
            String message=object.getString("value");
            SPUtils.saveStringData(context,"inform_title",title);
            SPUtils.saveStringData(context,"inform_message",message);
            Intent intent1=new Intent(context, MainActivity.class);
            intent1.putExtra("message",message);
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onNotifyMessageOpened(context, notificationMessage);
    }

    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage) {
        Log.d(TAG, "onNotifyMessageArrived: " + notificationMessage);
        /*
        * onNotifyMessageArrived: NotificationMessage{notificationId=460981060, msgId='2252062683808285', appkey='21b6a92c65b3463c75e18523', notificationContent='rhdrtysergserg', notificationAlertType=0, notificationTitle='esrhdsrethsdretgh', notificationSmallIcon='null', notificationLargeIcon='null', notificationExtras='{"key":"t","value":"eyJuYW1lIjoiZXNyaGRzcmV0aHNkcmV0Z2giLCJjb250ZW50IjoicmhkcnR5c2VyZ3NlcmciLCJ0b2FzdCI6InNlcmdzZXJnIiwicGF0aCI6Imh0dHBzOi8vYS5hcHAucXEuY29tL28vc2ltcGxlLmpzcD9wa2duYW1lPWNvbS5hbmRyb2lkLmpkaHNob3AiLCJrZXkiOiJteWluZm8iLCJpY29uIjoiaHR0cDovL3Rlc3QueGlubmlhbmtlamkuY29tLy9zdGF0aWMvYWRtaW4vaW1nL2xvZ28ucG5nIn0%3D"}', notificationStyle=0, notificationBuilderId=0, notificationBigText='null', notificationBigPicPath='null', notificationInbox='null', notificationPriority=0, notificationCategory='null', developerArg0='null', platform=1, notificationChannelId='', displayForeground='', notificationType=0', inAppMsgType=1', inAppMsgShowType=2', inAppMsgShowPos=0', inAppMsgTitle=, inAppMsgContentBody=, inAppType=0}
        * */
        //notificationMessage.notificationExtras =

        super.onNotifyMessageArrived(context, notificationMessage);
    }

    @Override
    public void onNotifyMessageUnShow(Context context, NotificationMessage notificationMessage) {
        Log.d(TAG, "onNotifyMessageUnShow: " + notificationMessage);
        super.onNotifyMessageUnShow(context, notificationMessage);
    }

    @Override
    public void onNotifyMessageDismiss(Context context, NotificationMessage notificationMessage) {
        Log.d(TAG, "onNotifyMessageDismiss: " + notificationMessage);
        super.onNotifyMessageDismiss(context, notificationMessage);
    }

    @Override
    public void onRegister(Context context, String s) {
        Log.d(TAG, "onRegister: " + s);
        super.onRegister(context, s);
    }

    @Override
    public void onConnected(Context context, boolean b) {
        Log.d(TAG, "onConnected: " + b);
        super.onConnected(context, b);
    }

    @Override
    public void onCommandResult(Context context, CmdMessage cmdMessage) {
        Log.d(TAG, "onCommandResult: " + cmdMessage);
        super.onCommandResult(context, cmdMessage);
    }

    @Override
    public void onMultiActionClicked(Context context, Intent intent) {
        Log.d(TAG, "onMultiActionClicked: " + intent);
        super.onMultiActionClicked(context, intent);
    }

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        Log.d(TAG, "onTagOperatorResult: " + jPushMessage);
        super.onTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        Log.d(TAG, "onCheckTagOperatorResult: " + jPushMessage);
        super.onCheckTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        Log.d(TAG, "onAliasOperatorResult: " + jPushMessage);
        super.onAliasOperatorResult(context, jPushMessage);
    }

    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        Log.d(TAG, "onMobileNumberOperatorResult: " + jPushMessage);
        super.onMobileNumberOperatorResult(context, jPushMessage);
    }

    @Override
    public void onNotificationSettingsCheck(Context context, boolean b, int i) {
        Log.d(TAG, "onNotificationSettingsCheck: " + b + ",i=" + i);
        super.onNotificationSettingsCheck(context, b, i);
    }

    @Override
    public boolean isNeedShowNotification(Context context, NotificationMessage notificationMessage, String s) {
        Log.d(TAG, "isNeedShowNotification: " + notificationMessage +"," +s);
        return super.isNeedShowNotification(context, notificationMessage, s);
    }

    @Override
    public void onInAppMessageArrived(Context context, NotificationMessage notificationMessage) {
        Log.d(TAG, "onInAppMessageArrived: " + notificationMessage);
        super.onInAppMessageArrived(context, notificationMessage);
    }

    @Override
    public void onInAppMessageClick(Context context, NotificationMessage notificationMessage) {
        Log.d(TAG, "onInAppMessageClick: " + notificationMessage);
        super.onInAppMessageClick(context, notificationMessage);
    }

    @Override
    public void onInAppMessageDismiss(Context context, NotificationMessage notificationMessage) {
        Log.d(TAG, "onInAppMessageDismiss: "  + notificationMessage);
        super.onInAppMessageDismiss(context, notificationMessage);
    }

    @Override
    public void onInAppMessageUnShow(Context context, NotificationMessage notificationMessage) {
        Log.d(TAG, "onInAppMessageUnShow: " + notificationMessage);
        super.onInAppMessageUnShow(context, notificationMessage);
    }

    @Override
    public boolean isNeedShowInAppMessage(Context context, NotificationMessage notificationMessage, String s) {
        Log.d(TAG, "isNeedShowInAppMessage: " + notificationMessage + " ," +s);
        return super.isNeedShowInAppMessage(context, notificationMessage, s);
    }

    @Override
    public byte onCheckInAppMessageState(Context context, String s) {
        Log.d(TAG, "onCheckInAppMessageState: " + s);
        return super.onCheckInAppMessageState(context, s);
    }

    @Override
    public boolean onSspNotificationWillShow(Context context, NotificationMessage notificationMessage, String s) {
        Log.d(TAG, "onSspNotificationWillShow: " + notificationMessage + "," + s);
        return super.onSspNotificationWillShow(context, notificationMessage, s);
    }

    @Override
    public byte onCheckSspNotificationState(Context context, String s) {
        Log.d(TAG, "onCheckSspNotificationState: " + s);
        return super.onCheckSspNotificationState(context, s);
    }

    @Override
    public void onPullInAppResult(Context context, JPushMessage jPushMessage) {
        Log.d(TAG, "onPullInAppResult: " + jPushMessage);
        super.onPullInAppResult(context, jPushMessage);
    }

    @Override
    public void onGeofenceRegion(Context context, String s, double v, double v1) {
        Log.d(TAG, "onGeofenceRegion: " + s+ ",v="+v+",v1="+v1);
        super.onGeofenceRegion(context, s, v, v1);
    }

    @Override
    public void onGeofenceReceived(Context context, String s) {
        Log.d(TAG, "onGeofenceReceived: " + s);
        super.onGeofenceReceived(context, s);
    }
}
