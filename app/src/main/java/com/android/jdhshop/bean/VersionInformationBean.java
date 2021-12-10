package com.android.jdhshop.bean;

/**
 * 开发者：陈飞
 * 时间:2018/7/23 10:47
 * 说明：版本信息
 */
public class VersionInformationBean {
    private String down_url;//app下载地址
    private String version_android;//版本号
    private String content;//更新内容
    private String down_android;//android下载地址

    private String share_url;
    private String share_url_register;
    private String vy_url_c = "";
    private String vy_url_s = "";
    public String share_url_vip = "N";
    public String is_tb_11;
    public String user_auth_code_exist;
    public String down_type;
    public String down_android_yyb;
    public String down_ios;
    public String default_avatar;

    public String getVy_url_c() {
        return vy_url_c;
    }

    public void setVy_url_c(String vy_url_c) {
        this.vy_url_c = vy_url_c;
    }

    public String getVy_url_s() {
        return vy_url_s;
    }

    public void setVy_url_s(String vy_url_s) {
        this.vy_url_s = vy_url_s;
    }

    public String getDown_android() {
        return down_android;
    }

    public String getShare_url_register() {
        return share_url_register;
    }

    public void setShare_url_register(String share_url_register) {
        this.share_url_register = share_url_register;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public void setDown_android(String down_android) {
        this.down_android = down_android;
    }


    public String getDown_url() {
        return down_url;
    }

    public void setDown_url(String down_url) {
        this.down_url = down_url;
    }

    public String getVersion() {
        return version_android;
    }

    public void setVersion(String version) {
        this.version_android = version;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
