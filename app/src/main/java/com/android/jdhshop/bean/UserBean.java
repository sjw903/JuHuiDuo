package com.android.jdhshop.bean;

/**
 * 开发者：李福珍
 * 时间:2018/7/27 13:01
 * 说明：用户信息
 */
public class UserBean {
    private int code;//返回码
    private String msg;//返回码说明
    private String uid;//用户ID
    private String group_id;//用户组ID
    private String token;//用户身份令牌，作为其它所有用户接口的必填参数，请APP本地化保存
    private String is_forver;
    private String name;
    private String avatar;
    public UserBean() {
    }

    public UserBean(String user_id, String group_id, String token, String avatar, String nickname, String is_forever) {
        this.uid=user_id;
        this.token=token;
        this.avatar=avatar;
        this.group_id=group_id;
        this.name=nickname;
        this.is_forver=is_forever;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIs_forver() {
        return is_forver;
    }

    public void setIs_forver(String is_forver) {
        this.is_forver = is_forver;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserBean(String uid, String group_id, String token) {
        this.uid = uid;
        this.group_id = group_id;
        this.token = token;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", uid='" + uid + '\'' +
                ", group_id='" + group_id + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
