package com.android.jdhshop.bean;

import java.io.Serializable;
import java.util.List;

public class PDDKindBean implements Serializable {
    private List<PDDKindBean> list;
    private String pdd_cat_id;
    private String name;
    private String icon;
    private String sort;
    private String is_show;
    private String pid;
    private String pdd_id;

    public List<PDDKindBean> getList() {
        return list;
    }

    public void setList(List<PDDKindBean> list) {
        this.list = list;
    }

    public String getPdd_cat_id() {
        return pdd_cat_id;
    }

    public void setPdd_cat_id(String pdd_cat_id) {
        this.pdd_cat_id = pdd_cat_id;
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

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getIs_show() {
        return is_show;
    }

    public void setIs_show(String is_show) {
        this.is_show = is_show;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPdd_id() {
        return pdd_id;
    }

    public void setPdd_id(String pdd_id) {
        this.pdd_id = pdd_id;
    }
}
