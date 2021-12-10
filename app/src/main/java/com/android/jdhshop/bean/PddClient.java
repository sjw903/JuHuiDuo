package com.android.jdhshop.bean;

import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.common.SPUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 拼多多接口请求加密
 * @author wmm
 */
public class PddClient {
    public static String serverUrl="http://gw-api.pinduoduo.com/api/router";
    public static String client_id=SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "pdd_client_id", "");
    public static String client_secret=SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "pdd_client_secret", "");
    public static String data_type = "JSON";
    public static String version = "V1";
    public static String client_secret2=SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "jdh_secret", "");
    public static void setInfo(){
        client_id = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "pdd_client_id", "");
        client_secret = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "pdd_client_secret", "");
    }
    public static String getSign5(Map<String,String> map){
        map=sortMapByKey2(map);
        String temp="537f9844eb30bd11e49e0fe702dd1e68";
        for (String key:map.keySet()){
            temp+=(key+map.get(key));
        }
        temp+="537f9844eb30bd11e49e0fe702dd1e68";
        return stringToMD5(temp).toLowerCase();
    }
    public static String getSign1(Map<String,String> map){
        map=sortMapByKey2(map);
        String temp=client_secret2;
        for (String key:map.keySet()){
            temp+=(key+map.get(key));
        }
        temp+=client_secret2;
        return stringToMD5(temp).toUpperCase();
    }
    public static String getSign3(Map<String,String> map,String secret){
        map=sortMapByKey2(map);
        String temp=secret;
        for (String key:map.keySet()){
            temp+=(key+map.get(key));
        }
        temp+=secret;
        return stringToMD5(temp).toUpperCase();
    }public static String getSign4(Map<String,String> map,String secret){
        map=sortMapByKey2(map);
        String temp="";
        for (String key:map.keySet()){
            temp+=(key+"="+map.get(key)+"&");
        }
        temp+="secretKey="+secret;
        return stringToMD5(temp).toUpperCase();
    }
    public static String getSign(Map<String,Object> map){
        map=sortMapByKey(map);
        String temp=client_secret;
        for (String key:map.keySet()){
            temp+=(key+map.get(key));
        }
        temp+=client_secret;
        return stringToMD5(temp).toUpperCase();
    }
    public static String stringToMD5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }
    /**
     * 使用 Map按key进行排序
     * @param map
     * @return
     */
    public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, Object> sortMap = new TreeMap<>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }
    /**
     * 使用 Map按key进行排序
     * @param map
     * @return
     */
    public static Map<String, String> sortMapByKey2(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }
    static class  MapKeyComparator implements Comparator<String> {

        @Override
        public int compare(String str1, String str2) {

            return str1.compareTo(str2);
        }
    }
}
