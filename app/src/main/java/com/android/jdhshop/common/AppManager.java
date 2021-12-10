package com.android.jdhshop.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.util.Stack;

/**
 * @author Lin
 *
 */
public class AppManager {
	private static String TAG = "AppManagerTagInfo";
	private static Stack<Activity> mActivityStack;
	private static AppManager mAppManager;
	private AppManager() {
		
	}

	/**
	 * 单一实例
	 */
	public static AppManager getInstance() {
		if (mAppManager == null) {
			mAppManager = new AppManager();
		}
		return mAppManager;
	}

	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity) {
		if (mActivityStack == null) {
			mActivityStack = new Stack<Activity>();
		}
		mActivityStack.add(activity);
	}

	/**
	 * 获取栈顶Activity（堆栈中最后一个压入的）
	 */
	public Activity getTopActivity() {
		Activity activity = mActivityStack.lastElement();
		return activity;
	}

	/**
	 * 结束栈顶Activity（堆栈中最后一个压入的）
	 */
	public void killTopActivity() {
		Activity activity = mActivityStack.lastElement();
		killActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void killActivity(Activity activity) {
		if (activity != null) {
			activity.finish();
			activity = null;
			mActivityStack.remove(activity);
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void killActivity(Class<?> cls) {
//		for (Activity activity : mActivityStack) {
//			if (activity.getClass().equals(cls)) {
//				killActivity(activity);
//			}
//		}
		for(int i=0;i<mActivityStack.size();i++){
			if(mActivityStack.get(i).equals(cls)){
				killActivity(cls);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void killAllActivity() {
		for (int i = 0, size = mActivityStack.size(); i < size; i++) {
			if (null != mActivityStack.get(i)) {
				mActivityStack.get(i).finish();
			}
		}
		mActivityStack.clear();
	}

	/**
	 * 退出应用程序
	 */
	public void AppExit(Context context, Activity activity) {
		try {
			int currentVersion = android.os.Build.VERSION.SDK_INT;  
            if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) { 
            	killAllActivity();
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(startMain);  
                System.exit(0);
            } else {
            	killAllActivity();
    			ActivityManager activityManager = (ActivityManager) context
    					.getSystemService(Context.ACTIVITY_SERVICE);
    			activityManager.killBackgroundProcesses(context.getPackageName());
    			System.exit(0);
			}
			
		} catch (Exception e) {
		}
	}
}

