package com.android.jdhshop.bean;

import java.util.List;

public class BannerBean {
    private List<BannerBean> list;
    public List<BannerBean> theme_list;

    public List<BannerBean> getList() {
        return list;
    }

    public void setList(List<BannerBean> list) {
        this.list = list;
    }
    private String id="";
    private String cat_id="";
    private String title="";
    private String img="";
    private String href="";
    private int checked=0;
    public String       image_url;
    public String name;
    public String   goods_num;

    private String color="";
    private String type;
    private String type_value;
    public String text;
    public String detail_img;
    private String is_show;
   public String taobao_activity_id	;//淘宝推广活动ID
    public String  taobao_activity_name;//	淘宝推广活动名称
    public String icon;//	淘宝推广活动图标
    public String   tb_activity_id;//	淘宝推广活动官方ID(备用)
    public String   sort;//	排序


    public boolean isCheck = false;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIs_show() {
        return is_show;
    }

    public void setIs_show(String is_show) {
        this.is_show = is_show;
    }

    public int getChecked() {
        return checked;
    }

    public BannerBean(String img) {
        this.img = img;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType_value() {
        return type_value;
    }

    public void setType_value(String type_value) {
        this.type_value = type_value;
    }

    @Override
    public String toString() {
        return "BannerBean{" +
                "list=" + list +
                ", id='" + id + '\'' +
                ", cat_id='" + cat_id + '\'' +
                ", title='" + title + '\'' +
                ", img='" + img + '\'' +
                ", href='" + href + '\'' +
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
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

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
