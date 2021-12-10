package com.android.jdhshop.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Item {
    @Property(nameInDb = "id")
    public String id;
    @Property(nameInDb = "name")
    public String name;
    @Property(nameInDb = "icon")
    public String icon;
    @Property(nameInDb = "type")
    public String type;
    @Property(nameInDb = "is_index_show")
    public String is_index_show;
    @Property(nameInDb = "href")
    public String href;
    @Generated(hash = 1913923657)
    public Item(String id, String name, String icon, String type,
            String is_index_show, String href) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.type = type;
        this.is_index_show = is_index_show;
        this.href = href;
    }
    @Generated(hash = 1470900980)
    public Item() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
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
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getIs_index_show() {
        return this.is_index_show;
    }
    public void setIs_index_show(String is_index_show) {
        this.is_index_show = is_index_show;
    }
    public String getHref() {
        return this.href;
    }
    public void setHref(String href) {
        this.href = href;
    }
}
