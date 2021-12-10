package com.android.jdhshop.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author 陈飞 @使用方法： （1）在任何地方发送广播
 * BroadcastManager.getInstance(mContext).sendBroadcast(FindOrderActivity.ACTION_RECEIVE_MESSAGE);
 * （2）页面在onCreate()中初始化广播
 * BroadcastManager.getInstance(mContext).addAction(ACTION_RECEIVE_MESSAGE,
 * new BroadcastReceiver(){
 * @ClassName：BroadcastManager
 * @描述:广播管理者
 * @Override public void onReceive(Context arg0, Intent intent) { String command
 * = intent.getAction(); if(!TextUtils.isEmpty(command)){
 * if((ACTION_RECEIVE_MESSAGE).equals(command)){ //获取json结果 String
 * json = intent.getStringExtra("result"); //做你该做的事情 } } } });
 * (3)页面在ondestory销毁广播
 * BroadcastManager.getInstance(mContext).destroy(ACTION_RECEIVE_MESSAGE);
 */
public class BroadcastManager {

    private Context mContext;
    private static BroadcastManager instance;
    private Map<String, BroadcastReceiver> receiverMap;

    /**
     * 构造方法
     *
     * @param context
     */
    private BroadcastManager(Context context) {
        this.mContext = context;
        receiverMap = new HashMap<String, BroadcastReceiver>();
    }

    /**
     * [获取BroadcastManager实例，单例模式实现]
     *
     * @param context
     * @return
     */
    public static BroadcastManager getInstance(Context context) {
        if (instance == null) {
            synchronized (BroadcastManager.class) {
                if (instance == null) {
                    instance = new BroadcastManager(context);
                }
            }
        }
        return instance;
    }

    /**
     * 添加Action,做广播的初始化
     *
     * @param
     */
    public void addAction(String action, BroadcastReceiver receiver) {
//        try {
//            IntentFilter filter = new IntentFilter();
//            if(receiverMap.containsKey(action)){
//                return;
//            }
//            filter.addAction(action);
//            mContext.registerReceiver(receiver, filter);
//            receiverMap.put(action, receiver);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 添加Action,做广播的初始化
     *
     * @param
     */
    public void addAction(String[] actions, BroadcastReceiver receiver) {
//        try {
//            IntentFilter filter = new IntentFilter();
//            for (String action : actions) {
//                if (receiverMap.containsKey(action)) {
//                    continue;
//                }
//                filter.addAction(action);
//            }
//            mContext.registerReceiver(receiver, filter);
//            for (String action : actions) {
//                receiverMap.put(action, receiver);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 发送广播
     *
     * @param action 唯一码
     */
    public void sendBroadcast(String action) {
        sendBroadcast(action, "");
    }

    /**
     * 发送广播
     *
     * @param action 唯一码
     * @param obj    参数
     */
    public void sendBroadcast(String action, Object obj) {
//        try {
//            Intent intent = new Intent();
//            intent.setAction(action);
//            intent.putExtra("result", obj.toString());
//            mContext.sendBroadcast(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 发送广播
     *
     * @param action 唯一码
     * @param obj    参数
     */
    public void sendBroadcast(String action, Serializable obj) {
//        try {
//            Intent intent = new Intent();
//            intent.setAction(action);
//            intent.putExtra("result", obj);
//            mContext.sendBroadcast(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 发送广播
     *
     * @param action 唯一码
     * @param obj    参数
     */
    public void sendBroadcast(String action, Intent obj) {
//        try {
//            obj.setAction(action);
//            mContext.sendBroadcast(obj);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void destroy() {
//        try {
//            if (receiverMap != null) {
//                Set<String> set = receiverMap.keySet();
//                Iterator<String> actions = set.iterator();
//                while (actions.hasNext()) {
//                    BroadcastReceiver receiver = receiverMap.get(actions.next());
//                    if (receiver != null) {
//                        mContext.unregisterReceiver(receiver);
//                    }
//                }
//            }
//        } catch (IllegalArgumentException e) {
//            LogUtils.d("Broadcastmanager", e.toString());
//        }
    }

    /**
     * 销毁广播
     *
     * @param actions
     */
    public void destroy(String... actions) {
//        try {
//            if (receiverMap != null) {
//                for (String action : actions) {
//                    BroadcastReceiver receiver = receiverMap.get(action);
//                    if (receiver != null) {
//                        mContext.unregisterReceiver(receiver);
//                    }
//                }
//            }
//        } catch (IllegalArgumentException e) {
//            LogUtils.d("Broadcastmanager", e.toString());
//        }
    }
}