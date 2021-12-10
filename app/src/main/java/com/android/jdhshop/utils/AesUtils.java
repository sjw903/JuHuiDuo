package com.android.jdhshop.utils;


import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesUtils {
    //-- 算法/模式/填充
    private static final String CipherMode = "AES/CBC/PKCS7Padding";

    //--创建密钥
    private static SecretKeySpec createKey(String password) {
        byte[] data = null;
        if (password == null) {
            password = "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append(password);
        String s = null;
        while (sb.length() < 32) {
            sb.append("1");//--密码长度不够32补足到32
        }
        s = sb.substring(0, 32);//--截取32位密码
        try {
            data = s.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new SecretKeySpec(data, "AES");
    }

    //--创建偏移量
    private static IvParameterSpec createIV(String iv) {
        byte[] data = null;
        if (iv == null) {
            iv = "";
        }
        StringBuffer sb = new StringBuffer(16);
        sb.append(iv);
        String s = null;
        while (sb.length() < 16) {
            sb.append(" ");//--偏移量长度不够16补足到16
        }
        s = sb.substring(0, 16);//--截取16位偏移量
        try {
            data = s.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new IvParameterSpec(data);
    }

    //--加密字节数组到字节数组
    private static byte[] encryptByte2Byte(byte[] content, String password, String iv) {
        try {
            SecretKeySpec key = createKey(password);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.ENCRYPT_MODE, key, createIV(iv));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //--加密字节数组到字符串
    private static String encryptByte2String(byte[] content, String password, String iv) {
        byte[] data = encryptByte2Byte(content, password, iv);
        String result = new String(data);
        return result;
    }

    //--加密字节数组到base64
    private static String encryptByte2Base64(byte[] content, String password, String iv) {
        byte[] data = encryptByte2Byte(content, password, iv);
        String result = new String(Base64.encode(data, Base64.DEFAULT));
        return result;
    }

    //--加密字符串到字节数组
    private static byte[] encryptString2Byte(String content, String password, String iv) {
        byte[] data = null;
        try {
            data = content.getBytes("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        data = encryptByte2Byte(data, password, iv);
        return data;
    }

    //--加密字符串到字符串
    private static String encryptString2String(String content, String password, String iv) {
        byte[] data = null;
        try {
            data = content.getBytes("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        data = encryptByte2Byte(data, password, iv);
        String result = new String(data);
        return result;
    }

    //--加密字符串到base64
    private static String encryptString2Base64(String content, String password, String iv) {
        byte[] data = null;
        try {
            data = content.getBytes("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        data = encryptByte2Byte(data, password, iv);
        return new String(Base64.encode(data, Base64.DEFAULT));
    }

    //-- 解密字节数组到字节数组
    private static byte[] decryptByte2Byte(byte[] content, String password, String iv) {
        try {
            SecretKeySpec key = createKey(password);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.DECRYPT_MODE, key, createIV(iv));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //--解密字符串到字节数组
    private static byte[] decryptString2Byte(String content, String password, String iv) {
        byte[] data = null;
        try {
            data = content.getBytes("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        data = decryptByte2Byte(data, password, iv);
        return data;
    }

    //--解密base64到字节数组
    private static byte[] decryptBase642Byte(String content, String password, String iv) {
        byte[] data = null;
        try {
            data = Base64.decode(content, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        data = decryptByte2Byte(data, password, iv);
        return data;
    }

    //-- 解密字节数组到字符串
    private static String decryptByte2String(byte[] content, String password, String iv) {
        byte[] data = decryptByte2Byte(content, password, iv);
        return new String(data);
    }

    //-- 解密字节数组到字符串
    private static String decryptBase642String(String content, String password, String iv) {
        byte[] data = Base64.decode(content, Base64.DEFAULT);
        return decryptByte2String(data, password, iv);
    }

    //-- MD5 --//
    public static String md5(String plainText) {
        //定义一个字节数组
        byte[] secretBytes = null;
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            //对字符串进行加密
            md.update(plainText.getBytes());
            //获得加密后的数据
            secretBytes = md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有md5这个算法！");
        }
        //将加密后的数据转换为16进制数字
        String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        md5code = md5code.substring(10, 26);
        return md5code;
    }

    //-- MD5 --//
    public static String md532(String plainText) {
        //定义一个字节数组
        byte[] secretBytes = null;
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            //对字符串进行加密
            md.update(plainText.getBytes());
            //获得加密后的数据
            secretBytes = md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有md5这个算法！");
        }
        //将加密后的数据转换为16进制数字
        String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
//        md5code = md5code.substring(10, 26);
        return md5code;
    }

    // -- 随机字符串生成 -- //
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    //-- 加密 --//
    public static String encrypt(String content) {
        String key,iv,key_1,key_2,iv_1,iv_2 = "";
        key = getRandomString(32);
        iv = md5(String.valueOf(SystemClock.currentThreadTimeMillis()));
        iv_1 = iv.substring(0,8);
        iv_2 = iv.substring(8,16);
        key_1 = key.substring(0,16);
        key_2 = key.substring(16,32);
        String tmp = encryptString2Base64(content, key, iv);
        return  (iv_1 + key_1 + tmp + key_2 + iv_2);
    }

    //-- 解密 --//
    public static String decrypt(String content) {
        String real_data,tmp_1,tmp_2,key,iv,key_1,key_2,iv_1,iv_2 = "";
        tmp_1 = content.substring(0,24);
        tmp_2 = content.substring(content.length()-24);
        iv_1 = tmp_1.substring(0,8);
        key_1 = tmp_1.substring(8,24);
        key_2 = tmp_2.substring(0,16);
        iv_2 = tmp_2.substring(16,24);
        iv = iv_1 + iv_2;
        key = key_1 + key_2;
        real_data = content.substring(24,content.length()-24);
        return decryptBase642String(real_data, key, iv);
    }
}
