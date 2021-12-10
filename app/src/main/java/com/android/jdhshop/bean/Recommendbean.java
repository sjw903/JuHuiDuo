package com.android.jdhshop.bean;

import java.io.Serializable;
import java.util.List;

public class Recommendbean implements Serializable {
    private List<Recommendbean> list;
    private String category_id;
    private String click_url;
    private String commission_rate;
    private String coupon_amount;
    private String coupon_start_fee;
    private String coupon_info;
    private String item_description;
    private String item_id;
    private String jdd_num;
    private String jdd_price;
    private String level_one_category_id;
    private String level_one_category_name;
    private String oetime;
    private String orig_price;
    private String ostime;
    private String pict_url;
    private String sell_num;
    private String seller_id;
    private String stock;
    private String title;
    private String total_stock;
    private String num_iid;
    private String user_type;
    private String commission;
    private String zk_final_price;

    public String getCoupon_start_fee() {
        return coupon_start_fee;
    }

    public void setCoupon_start_fee(String coupon_start_fee) {
        this.coupon_start_fee = coupon_start_fee;
    }

    public String getCoupon_info() {
        return coupon_info;
    }

    public void setCoupon_info(String coupon_info) {
        this.coupon_info = coupon_info;
    }

    public String getNum_iid() {
        return num_iid;
    }

    public void setNum_iid(String num_iid) {
        this.num_iid = num_iid;
    }

    public String getZk_final_price() {
        return zk_final_price;
    }

    public void setZk_final_price(String zk_final_price) {
        this.zk_final_price = zk_final_price;
    }

    public List<Recommendbean> getList() {
        return list;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public void setList(List<Recommendbean> list) {
        this.list = list;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getClick_url() {
        return click_url;
    }

    public void setClick_url(String click_url) {
        this.click_url = click_url;
    }

    public String getCommission_rate() {
        return commission_rate;
    }

    public void setCommission_rate(String commission_rate) {
        this.commission_rate = commission_rate;
    }

    public String getCoupon_amount() {
        return coupon_amount;
    }

    public void setCoupon_amount(String coupon_amount) {
        this.coupon_amount = coupon_amount;
    }

    public String getCoupone_start_fee() {
        return coupon_start_fee;
    }

    public void setCoupone_start_fee(String coupone_start_fee) {
        this.coupon_start_fee = coupone_start_fee;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getJdd_num() {
        return jdd_num;
    }

    public void setJdd_num(String jdd_num) {
        this.jdd_num = jdd_num;
    }

    public String getJdd_price() {
        return jdd_price;
    }

    public void setJdd_price(String jdd_price) {
        this.jdd_price = jdd_price;
    }

    public String getLevel_one_category_id() {
        return level_one_category_id;
    }

    public void setLevel_one_category_id(String level_one_category_id) {
        this.level_one_category_id = level_one_category_id;
    }

    public String getLevel_one_category_name() {
        return level_one_category_name;
    }

    public void setLevel_one_category_name(String level_one_category_name) {
        this.level_one_category_name = level_one_category_name;
    }

    public String getOetime() {
        return oetime;
    }

    public void setOetime(String oetime) {
        this.oetime = oetime;
    }

    public String getOrig_price() {
        return orig_price;
    }

    public void setOrig_price(String orig_price) {
        this.orig_price = orig_price;
    }

    public String getOstime() {
        return ostime;
    }

    public void setOstime(String ostime) {
        this.ostime = ostime;
    }

    public String getPict_url() {
        return pict_url;
    }

    public void setPict_url(String pict_url) {
        this.pict_url = pict_url;
    }

    public String getSell_num() {
        return sell_num;
    }

    public void setSell_num(String sell_num) {
        this.sell_num = sell_num;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotal_stock() {
        return total_stock;
    }

    public void setTotal_stock(String total_stock) {
        this.total_stock = total_stock;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
