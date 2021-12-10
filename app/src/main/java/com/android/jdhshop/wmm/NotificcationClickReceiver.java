package com.android.jdhshop.wmm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.jdhshop.MainActivity;
import com.android.jdhshop.common.SPUtils;

/**
 * @author wmm
 * 2018-12-10 15:12
 * 消息推送点击处理
 */
public class NotificcationClickReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("JpushCommonReceiver", "NotificcationClickReceiver onReceive: ");
        String title=intent.getStringExtra("title");
        String message=intent.getStringExtra("message");
        //统一默认打开首页，进入首页后做判断跳转
        Intent intent1=new Intent(context,MainActivity.class);
        SPUtils.saveStringData(context,"inform_title",title);
        SPUtils.saveStringData(context,"inform_message",message);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
