package com.android.jdhshop.mallbean;

import java.util.List;

/**
 * 开发者：陈飞
 * 时间:2018/7/26 15:34
 * 说明：商品导航数
 */
public class ShopTabsBean {

    private List<ShopTabsChildBean> list;//顶级淘宝商品分类列表

    public List<ShopTabsChildBean> getList() {
        return list;
    }

    public void setList(List<ShopTabsChildBean> list) {
        this.list = list;
    }
}
