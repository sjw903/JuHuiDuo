package com.android.jdhshop.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ShopTabsChildBean{
    @Property(nameInDb = "taobao_cat_id")
    private String taobao_cat_id;//商品分类ID
    @Property(nameInDb = "name")
    private String name;//商品分类名称
    @Property(nameInDb = "cat_id")
    public String cat_id;//商品分类ID
    @Property(nameInDb = "icon")
    private String icon;//商品分类图标
    @Property(nameInDb = "pid")
    private String pid;//父级分类ID
    public ShopTabsChildBean(String taobao_cat_id, String name, String icon,
            String pid) {
        this.taobao_cat_id = taobao_cat_id;
        this.name = name;
        this.icon = icon;
        this.pid = pid;
    }
    public ShopTabsChildBean() {
    }
    @Generated(hash = 316689136)
    public ShopTabsChildBean(String taobao_cat_id, String name, String cat_id,
            String icon, String pid) {
        this.taobao_cat_id = taobao_cat_id;
        this.name = name;
        this.cat_id = cat_id;
        this.icon = icon;
        this.pid = pid;
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
    public String getCat_id() {
        return this.cat_id;
    }
    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }
}