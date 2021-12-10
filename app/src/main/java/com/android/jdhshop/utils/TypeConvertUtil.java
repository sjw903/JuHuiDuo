package com.android.jdhshop.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 基础类型转换类
 *
 * @author lifuzhen
 * @version V 1.0
 * @date 2017/7/12 09:48.
 */
public class TypeConvertUtil {


    // yyyy-MM-dd HH:mm:ss 格式
    public static final String DEFAULT_DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    //yyyy-MM-dd HH:mm 格式
    public static final String DEFAULT_DATE_TIME_HHmm_FORMAT_PATTERN = "yyyy-MM-dd HH:mm";
    //yyyy-MM-dd HH 格式
    public static final String DEFAULT_DATE_TIME_HH_FORMAT_PATTERN = "yyyy-MM-dd HH";
    //yyyy-MM-dd 格式
    public static final String DEFAULT_DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    //HH:mm:ss 格式
    public static final String DEFAULT_TIME_FORMAT_PATTERN = "HH:mm:ss";
    //HH:mm 格式
    public static final String DEFAULT_TIME_HHmm_FORMAT_PATTERN = "HH:mm";

    private java.util.Calendar c;   //日历类

    /**
     * string to boolean
     * @return
     */
    public static boolean stingToBoolean(String value, boolean defaultValue) {
        return TextUtils.isEmpty(value) ? defaultValue : Boolean.valueOf(value);
    }


    /**
     * string to int
     */
    public static int stringToInt(String value, int defaultValue) {
        return TextUtils.isEmpty(value) ? defaultValue : Integer.parseInt(value);
    }

    /**
     * string to string
     */
    public static String getString(String value, String defaultValue) {
        return TextUtils.isEmpty(value) ? defaultValue : value;
    }


    /**
     * string to Ingeger
     */
    public static Integer stringToInteger(String value, Integer defaultValue) {
        return TextUtils.isEmpty(value) ? defaultValue : Integer.valueOf(value);
    }


    /**
     * string to long
     */
    public static long stringToLong(String value, long defaultValue) {
        return TextUtils.isEmpty(value) ? defaultValue : Long.parseLong(value);
    }


    /**
     * string to double
     */
    public static double stringToDouble(String value, double defaultValue) {
        return TextUtils.isEmpty(value) ? defaultValue : Double.parseDouble(value);
    }


    /**
     * string to float
     */
    public static float stringToFloat(String value, float defaultValue) {
        return TextUtils.isEmpty(value) ? defaultValue : Float.parseFloat(value);
    }


    /**
     * 使用日期转换pattern
     * <p>pattern的用法参见java.text.SimpleDateFormat</p>
     * @param pattern 日期解析规则
     * @return 按规则转换后的日期时间字符串
     */
    public String toDateTimeString(String pattern) {
        SimpleDateFormat fmt = (SimpleDateFormat) DateFormat.getDateInstance();
        fmt.applyPattern(pattern);
        return fmt.format(c.getTime());
    }

    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    /**
     * 转换为 DEFAULT_DATE_FORMAT_PATTERN (yyyy-MM-dd) 格式字符串
     *
     * @return yyyy-MM-dd格式字符串
     */
    public String toDateString() {
        return toDateTimeString(TypeConvertUtil.DEFAULT_DATE_FORMAT_PATTERN);
    }

    /**
     * 转换为 DEFAULT_TIME_FORMAT_PATTERN (HH:mm:ss) 格式字符串
     *
     * @return HH:mm:ss 格式字符串
     */
    public String toTimeString() {
        return toDateTimeString(TypeConvertUtil.DEFAULT_TIME_FORMAT_PATTERN);
    }

    /**
     * 转换为 DEFAULT_DATE_TIME_FORMAT_PATTERN (yyyy-MM-dd HH:mm:ss) 格式字符串
     *
     * @return yyyy-MM-dd HH:mm:ss 格式字符串
     */
    public String toDateTimeString() {
        return toDateTimeString(TypeConvertUtil.DEFAULT_DATE_TIME_FORMAT_PATTERN);
    }


    /**
     * 将字符按照指定分隔符生成字符数组
     *
     * @param propValue
     * @param delim
     * @return
     */
    public static String[] toStringArray(String propValue, String delim) {
        if (propValue != null) {
            return propValue.split(delim);
        } else {
            return null;
        }
    }

    /**
     * 将list转化为数组
     * @param list
     * @return
     */
    public static String[] toArray(List<String> list) {
        if (list == null) return null;
        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }


    /**
     * 数值字符串格式成0.00
     * @param str
     * @return
     */
    public static String getString(String str){
//        DecimalFormat formater = new DecimalFormat("#0.##");
//        formater.setRoundingMode(RoundingMode.FLOOR);
//       return  formater.format(Double.parseDouble(str)).toString();
        if (TextUtils.isEmpty(str)){
            str = "0";
        }

        DecimalFormat df =null;
        if(str.contains(".")){
            String[] s=str.split("\\.");
            if(null!=s&&s.length>1&&s[1].length()==1){
                return str+"0";
            }else if(null!=s&&s.length>1&&s[1].length()>1){
                df = new DecimalFormat("0.00");
                return df.format(Double.parseDouble(str));
            }else if(null!=s&&s.length==1){
                return str+"00";
            }else{
                return "0.00";
            }
        }else{
            if(0==Double.parseDouble(str)){
                return "0.00";
            }else{
                return str+".00";
            }
        }
    }
}
