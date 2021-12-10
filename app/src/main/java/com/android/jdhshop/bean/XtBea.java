package com.android.jdhshop.bean;

import java.io.Serializable;
import java.util.List;

public class XtBea implements Serializable {
    private String id;
    private String title;
    private String img;
    private String time;
    private String share_num;
    private List<XtBea> list;
    public String getId() {
        return id;
    }

    public List<XtBea> getList() {
        return list;
    }

    public void setList(List<XtBea> list) {
        this.list = list;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getShare_num() {
        return share_num;
    }

    public void setShare_num(String share_num) {
        this.share_num = share_num;
    }
}
