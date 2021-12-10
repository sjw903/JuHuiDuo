package com.android.jdhshop.mallbean;

public class ShopTabsChildBean{
    private String taobao_cat_id;//商品分类ID
    private String name;//商品分类名称
    private String icon;//商品分类图标
    private String pid;//父级分类ID
   public String cat_id;///	商品分类ID
    public String cat_name	;//商品分类名称
    public String  keywords;//	关键词
    public String   description;//	简介描述
    public String   img;//	商品分类图片

    public ShopTabsChildBean(String taobao_cat_id, String name, String icon,
            String pid) {
        this.taobao_cat_id = taobao_cat_id;
        this.name = name;
        this.icon = icon;
        this.pid = pid;
    }
    public ShopTabsChildBean() {
    }
    public String getTaobao_cat_id() {
        return this.taobao_cat_id;
    }
    public void setTaobao_cat_id(String taobao_cat_id) {
        this.taobao_cat_id = taobao_cat_id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIcon() {
        return this.icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getPid() {
        return this.pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
}