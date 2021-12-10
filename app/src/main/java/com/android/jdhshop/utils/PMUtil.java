package com.android.jdhshop.utils;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * 界面管理工具
 */

public class PMUtil {
    private static final List<Activity> ACTIVITY_LIST = new ArrayList<>();
    private static Context mContext;

    private PMUtil() {
    }

    public synchronized static PMUtil getInstance() {
        return Holder.APP_UTIL;
    }

    public void init(Context context) {
        mContext = context;
    }

    public void pushAC(Activity activity) {
        ACTIVITY_LIST.add(activity);
    }

    public void removeAC(Activity activity) {
        ACTIVITY_LIST.remove(activity);
    }

    public void finishAc(Class<?> clazz) {
        if (clazz != null) {
            if (containAc(clazz)) {
                for (Activity ac : ACTIVITY_LIST) {
                    if (ac.getClass().equals(clazz)) {
                        ac.finish();
                    }
                }
            }
        }
    }

    public boolean containAc(Class<?> clazz) {
        for (Activity activity : ACTIVITY_LIST) {
            if (activity.getClass().equals(clazz)) {
                return true;
            }
        }
        return false;
    }

    public void finishAllAC() {
        for (Activity activity : ACTIVITY_LIST) {
            activity.finish();
        }
    }

    public Class getTopAC() {
        return ACTIVITY_LIST.isEmpty() ? null : ACTIVITY_LIST.get(ACTIVITY_LIST.size() - 1).getClass();
    }

    public void exit() {
        finishAllAC();
    }

    public Context getContext() {
        return mContext;
    }

    private static class Holder {
        private static final PMUtil APP_UTIL = new PMUtil();
    }
}
