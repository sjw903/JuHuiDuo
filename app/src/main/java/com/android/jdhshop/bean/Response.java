package com.android.jdhshop.bean;

import java.io.Serializable;

/**
 * 开发者：陈飞
 * 时间:2018/7/20 09:30
 * 说明：响应基类，服务器固定格式，所有响应继承此类
 */
public class Response<T> implements Serializable {
    private int code;//响应码 0.代表成功
    private String msg;//消息体 成功或者其他的提示
    private T data; //服务器返回数据

    public boolean isSuccess() {
        if (code == 0) {
            return true;
        }
        return false;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
