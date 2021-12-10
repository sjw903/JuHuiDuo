package com.android.jdhshop.bean;

public class ShareBean {
    private String ischeck="N";
    private String imgUrl="";

    public ShareBean(String ischeck, String imgUrl) {
        this.ischeck = ischeck;
        this.imgUrl = imgUrl;
    }

    public String getIscheck() {
        return ischeck;
    }

    public void setIscheck(String ischeck) {
        this.ischeck = ischeck;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
