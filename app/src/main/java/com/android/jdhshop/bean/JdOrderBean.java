package com.android.jdhshop.bean;

import java.util.List;

public class JdOrderBean {
    private List<JdOrderBean> list;
    private String goods_name;
    private String goods_img;
    private String order_time;
    private String pay_price;
    private String commission;
    private String commissionRate;
    private String order_status;
    private String skuid;
    private String orderId;
    private String data_url;
    public String getOrder_sn() {
        return orderId;
    }

    public void setOrder_sn(String order_sn) {
        this.orderId = order_sn;
    }

    public String data_url() {
        return data_url;
    }

    public void data_url(String order_sn) {
        this.data_url = order_sn;
    }

    public List<JdOrderBean> getList() {
        return list;
    }

    public void setList(List<JdOrderBean> list) {
        this.list = list;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_img() {
        return goods_img;
    }

    public void setGoods_img(String goods_img) {
        this.goods_img = goods_img;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getPay_price() {
        return pay_price;
    }

    public String getSkuid() {
        return skuid;
    }

    public void setSkuid(String skuid) {
        this.skuid = skuid;
    }

    public void setPay_price(String pay_price) {
        this.pay_price = pay_price;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(String commissionRate) {
        this.commissionRate = commissionRate;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }
}
