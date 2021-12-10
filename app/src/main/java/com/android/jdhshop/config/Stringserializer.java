package com.android.jdhshop.config;

import android.text.TextUtils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * 描述：String的自定义反序列化
 * 作者：陈飞
 * 时间：2018/8/24 09:47
 **/
public class Stringserializer implements JsonDeserializer<String> {

    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonArray()) {
            String s = json.getAsJsonArray().toString();
            if (TextUtils.isEmpty(s)) {
                return "";
            } else if (s.substring(0, 1).equals("[") && s.substring(1, 2).equals("]")) {
                return "";
            } else {
                return s;
            }
        } else {
            String s = json.getAsString();
            if (TextUtils.isEmpty(s)) {
                return "";
            } else {
                return s;
            }
        }
    }
}
