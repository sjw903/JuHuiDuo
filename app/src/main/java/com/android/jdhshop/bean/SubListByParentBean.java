package com.android.jdhshop.bean;

import java.util.List;

/**
 * 开发者：陈飞
 * 时间:2018/7/26 16:15
 * 说明：子级目录
 */
public class SubListByParentBean {

    private List<SubListByParentChildBean> list;//子级淘宝商品分类列表

    public List<SubListByParentChildBean> getList() {
        return list;
    }

    public void setList(List<SubListByParentChildBean> list) {
        this.list = list;
    }
}
