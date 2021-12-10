package com.android.jdhshop.bean;

/**
 * 开发者：wmm
 * 时间:2018/11/16 11:59
 * 说明：淘宝客数据
 */
public class PhbBean {
    private String item_url;//商品详情页面地址
    private String nick;//卖家名称
    private String item_id;//商品ID
    private String pict_url;//商品主图
    private String provcity;//商品所在地
    private String reserve_price;//商品一口价格
    private String seller_id;//卖家ID
    private String[] small_images;//商品相册/商品小图列表
    private String title;//商品名称
    private String user_type;//卖家类型，0表示集市，1表示商城
    private String volume;//30天销量
    private String zk_final_price;//商品折扣价格
    private String coupon_total_count;//优惠券总量
    private String coupon_remain_count;//30天销量
    private String coupon_start_time;//优惠券开始时间
    private String coupon_end_time;//优惠券结束时间
    private Object coupon_info;//优惠券信息
    private Object coupon_id;//优惠券id
    private String coupon_amount;//优惠券面额
    private String coupon_click_url;//优惠券推广链接
    private String commission_rate;//奖比率(%)
    private double commission;//奖
    private boolean is_tmall;//是否天猫商品，true天猫、false淘宝

    public String[] getSmall_images() {
        return small_images;
    }

    public void setSmall_images(String[] small_images) {
        this.small_images = small_images;
    }

    public boolean isIs_tmall() {
        return is_tmall;
    }

    public void setIs_tmall(boolean is_tmall) {
        this.is_tmall = is_tmall;
    }

    public Object getCoupon_info() {
        return coupon_info;
    }

    public void setCoupon_info(Object coupon_info) {
        this.coupon_info = coupon_info;
    }

    public Object getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(Object coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getItem_url() {
        return item_url;
    }

    public void setItem_url(String item_url) {
        this.item_url = item_url;
    }

    public String getNick() {
        return nick;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
    public String getPict_url() {
        return pict_url;
    }

    public void setPict_url(String pict_url) {
        this.pict_url = pict_url;
    }

    public String getProvcity() {
        return provcity;
    }

    public void setProvcity(String provcity) {
        this.provcity = provcity;
    }

    public String getReserve_price() {
        return reserve_price;
    }

    public void setReserve_price(String reserve_price) {
        this.reserve_price = reserve_price;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getZk_final_price() {
        return zk_final_price;
    }

    public void setZk_final_price(String zk_final_price) {
        this.zk_final_price = zk_final_price;
    }

    public String getCoupon_total_count() {
        return coupon_total_count;
    }

    public void setCoupon_total_count(String coupon_total_count) {
        this.coupon_total_count = coupon_total_count;
    }

    public String getCoupon_remain_count() {
        return coupon_remain_count;
    }

    public void setCoupon_remain_count(String coupon_remain_count) {
        this.coupon_remain_count = coupon_remain_count;
    }

    public String getCoupon_start_time() {
        return coupon_start_time;
    }

    public void setCoupon_start_time(String coupon_start_time) {
        this.coupon_start_time = coupon_start_time;
    }

    public String getCoupon_end_time() {
        return coupon_end_time;
    }

    public void setCoupon_end_time(String coupon_end_time) {
        this.coupon_end_time = coupon_end_time;
    }


    public String getCoupon_amount() {
        return coupon_amount;
    }

    public void setCoupon_amount(String coupon_amount) {
        this.coupon_amount = coupon_amount;
    }

    public String getCoupon_click_url() {
        return coupon_click_url;
    }

    public void setCoupon_click_url(String coupon_click_url) {
        this.coupon_click_url = coupon_click_url;
    }

    public String getCommission_rate() {
        return commission_rate;
    }

    public void setCommission_rate(String commission_rate) {
        this.commission_rate = commission_rate;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }
}
