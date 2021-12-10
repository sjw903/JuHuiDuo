package com.android.jdhshop.common;

import android.text.TextUtils;
import android.util.Log;

/**
 * LogUtils工具说明:
 * 1 只输出等级大于等于LEVEL的日志
 * 所以在开发和产品发布后通过修改LEVEL来选择性输出日志.
 * 当LEVEL=NOTHING则屏蔽了所有的日志.
 * 2 v,d,i,w,e均对应两个方法.
 * 若不设置TAG或者TAG为空则为设置默认TAG
 */
public class LogUtils {
    private static final String CONST_TAG = "happy";
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;
    public static final int LEVEL = 0;
    public static final String SEPARATOR = ",";

    public static void v(String message) {
        if (LEVEL <= VERBOSE) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            String tag = getDefaultTag(stackTraceElement);
            Log.v(tag, getLogInfo(stackTraceElement) + message);
        }
    }

    public static void v(String tag, String message) {
        if (LEVEL <= VERBOSE) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            if (TextUtils.isEmpty(tag)) {
                tag = getDefaultTag(stackTraceElement);
            }
            Log.v(tag, getLogInfo(stackTraceElement) + message);
        }
    }

    public static void d(String message) {
        if (LEVEL <= DEBUG) {
//            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
//            String tag = getDefaultTag(stackTraceElement);
//            LogUtils.d(tag, getLogInfo(stackTraceElement) + message);
            if (message == null) return;
            Log.d("LogUtils", message);
        }
    }

    public static void d(String tag, String message) {
        if (LEVEL <= DEBUG) {
            // Log.d(TAG, message);
//            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
//            if (TextUtils.isEmpty(tag)) {
//                tag = getDefaultTag(stackTraceElement);
//            }
//            LogUtils.d(tag, getLogInfo(stackTraceElement) + message);
            TAG = tag;

            logd(message);
        }
    }

    public static void i(String message) {
        if (LEVEL <= INFO) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            String tag = getDefaultTag(stackTraceElement);
            Log.i(tag, getLogInfo(stackTraceElement) + message);
        }
    }

    public static void i(String tag, String message) {
        if (LEVEL <= INFO) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            if (TextUtils.isEmpty(tag)) {
                tag = getDefaultTag(stackTraceElement);
            }
            Log.i(tag, getLogInfo(stackTraceElement) + message);
        }
    }

    public static void w(String message) {
        if (LEVEL <= WARN) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            String tag = getDefaultTag(stackTraceElement);
            Log.w(tag, getLogInfo(stackTraceElement) + message);
        }
    }

    public static void w(String tag, String message) {
        if (LEVEL <= WARN) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            if (TextUtils.isEmpty(tag)) {
                tag = getDefaultTag(stackTraceElement);
            }
            Log.w(tag, getLogInfo(stackTraceElement) + message);
        }
    }

    public static void e(String tag, String message) {
        if (LEVEL <= ERROR) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            if (TextUtils.isEmpty(tag)) {
                tag = getDefaultTag(stackTraceElement);
            }
            Log.e(tag, getLogInfo(stackTraceElement) + message);
        }
    }

    /**
     * 获取默认的TAG名称.
     * 比如在MainActivity.java中调用了日志输出.
     * 则TAG为MainActivity
     */
    public static String getDefaultTag(StackTraceElement stackTraceElement) {
        String fileName = stackTraceElement.getFileName();
        String stringArray[] = fileName.split("\\.");
        String tag = stringArray[0];
        return tag;
    }

    /**
     * 输出日志所包含的信息
     */
    public static String getLogInfo(StackTraceElement stackTraceElement) {
        StringBuilder logInfoStringBuilder = new StringBuilder();
        // 获取线程名
        String threadName = Thread.currentThread().getName();
        // 获取线程ID
        long threadID = Thread.currentThread().getId();
        // 获取文件名.即xxx.java
        String fileName = stackTraceElement.getFileName();
        // 获取类名.即包名+类名
        String className = stackTraceElement.getClassName();
        // 获取方法名称
        String methodName = stackTraceElement.getMethodName();
        // 获取生日输出行数
        int lineNumber = stackTraceElement.getLineNumber();

        logInfoStringBuilder.append("[ ");
        logInfoStringBuilder.append("threadID=" + threadID).append(SEPARATOR);
        logInfoStringBuilder.append("threadName=" + threadName).append(SEPARATOR);
        logInfoStringBuilder.append("fileName=" + fileName).append(SEPARATOR);
        logInfoStringBuilder.append("className=" + className).append(SEPARATOR);
        logInfoStringBuilder.append("methodName=" + methodName).append(SEPARATOR);
        logInfoStringBuilder.append("lineNumber=" + lineNumber);
        logInfoStringBuilder.append(" ] ");
        return logInfoStringBuilder.toString();
    }

    static String TAG = "JuduohuiApp";

    public static void logd(String msg) {
        if (LEVEL <= VERBOSE) {

            int max_str_length = 2001 - TAG.length();
            //大于4000时
            while (msg.length() > max_str_length) {
                Log.d(TAG, msg.substring(0, max_str_length));
                msg = msg.substring(max_str_length);
            }
            Log.d(TAG, msg);
        }
    }

    public static void log(String msg) {
        if (LEVEL <= DEBUG) {

            int max_str_length = 2000 - CONST_TAG.length();
            //大于2000时
            while (msg.length() > max_str_length) {
                Log.d(CONST_TAG, msg.substring(0, max_str_length));
                msg = msg.substring(max_str_length);
            }
            Log.d(CONST_TAG, msg);
        }
    }
}
