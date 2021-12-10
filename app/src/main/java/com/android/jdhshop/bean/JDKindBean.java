package com.android.jdhshop.bean;

import java.io.Serializable;
import java.util.List;

public class JDKindBean implements Serializable {
    private List<JDKindBean> list;
    private String jingdong_cat_id;
    private String pid;
    private String name;
    private String icon;
    private String is_show;
    private String jingdong_id;

    public String getJingdong_id() {
        return jingdong_id;
    }

    public void setJingdong_id(String jingdong_id) {
        this.jingdong_id = jingdong_id;
    }

    public List<JDKindBean> getList() {
        return list;
    }

    public void setList(List<JDKindBean> list) {
        this.list = list;
    }

    public String getJingdong_cat_id() {
        return jingdong_cat_id;
    }

    public void setJingdong_cat_id(String jingdong_cat_id) {
        this.jingdong_cat_id = jingdong_cat_id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIs_show() {
        return is_show;
    }

    public void setIs_show(String is_show) {
        this.is_show = is_show;
    }
}
