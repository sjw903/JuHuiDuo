package com.android.jdhshop.merchantbean;

import java.util.List;

/**
 * 附近分类
 * Created by cc on 2017/12/21.
 */

public class NearByCortItem {
    public String taobao_cat_id;//商品分类ID
    public String merchant_type_id;//商户类型ID
    public String name;//商品分类名称
    public String icon;//商品分类图标
    public String sort;//排序 数字越大越在前
    public String is_show;//是否显示 Y显示 N不显示
    public String pid;//父级分类ID

    public List<NearByCortItem> list;

    public List<NearByCortItem> sublist;

    public String getMerchant_type_id() {
        return merchant_type_id;
    }

    public void setMerchant_type_id(String merchant_type_id) {
        this.merchant_type_id = merchant_type_id;
    }

    public String getTaobao_cat_id() {
        return taobao_cat_id;
    }

    public void setTaobao_cat_id(String taobao_cat_id) {
        this.taobao_cat_id = taobao_cat_id;
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

    public List<NearByCortItem> getList() {
        return list;
    }

    public void setList(List<NearByCortItem> list) {
        this.list = list;
    }
}
