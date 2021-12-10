package com.android.jdhshop.juduohui.message;


import com.alibaba.fastjson.JSONObject;

public class JuduohuiMessage {
    private JSONObject message;
    public JSONObject getMessage(){
        return message;
    }
    public void putMessage(JSONObject mes){
        message = mes;
    }

}
