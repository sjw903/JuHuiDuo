package com.android.jdhshop.bean;

/**
 * 开发者：陈飞
 * 时间:2018/7/27 14:33
 * 说明：获取淘宝客商品详情
 */
public class PromotionDetailsBean {
    private String cat_name;//一级类目名称
    private String num_iid;//商品ID
    private String title;//商品名称
    private String pict_url;//商品主图
    private SmallImagesBean small_images;//商品相册/商品小图列表
    private String reserve_price;//商品一口价格
    private String zk_final_price;//商品折扣价格
    private String user_type;//卖家类型，0表示集市，1表示商城
    private String provcity;//商品所在地
    private String item_url;//商品详情页面地址
    private String seller_id;//卖家ID
    private String nick;//卖家名称
    private String volume;//30天销量
    private String cat_leaf_name;//叶子类目名称
    private String is_prepay;//是否加入消费者保障
    private String shop_dsr;//店铺dsr 评分
    private String ratesum;//卖家等级
    private String content_url;//详情url
    private String coupon_total_count;//优惠券总量
    private String coupon_remain_count;//优惠券剩余量
    private String coupon_start_time;//优惠券开始时间
    private String coupon_end_time;//优惠券结束时间
    private String coupon_info;//优惠券信息
    private String coupon_amount;//优惠券面额

    public String getCoupon_click_url_r() {
        return coupon_click_url_r;
    }

    public void setCoupon_click_url_r(String coupon_click_url_r) {
        this.coupon_click_url_r = coupon_click_url_r;
    }

    private String coupon_click_url_r;//优惠券推广链接
    private String coupon_click_url;//优惠券推广链接
    private String commission_rate;//奖比率(%)
    private String commission;//奖
    private String item_description;//卖点:宝贝描述（推荐理由）
    private String commission_vip;
    public static class SmallImagesBean {
        private Object string;

        public Object getSmall_images() {
            return string;
        }

        public void setSmall_images(Object string) {
            this.string = string;
        }
    }

    public String getContent_url() {
        return content_url;
    }

    public void setContent_url(String content_url) {
        this.content_url = content_url;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getNum_iid() {
        return num_iid;
    }

    public void setNum_iid(String num_iid) {
        this.num_iid = num_iid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPict_url() {
        return pict_url;
    }

    public void setPict_url(String pict_url) {
        this.pict_url = pict_url;
    }

    public SmallImagesBean getSmall_images() {
        return small_images;
    }

    public void setSmall_images(SmallImagesBean small_images) {
        this.small_images = small_images;
    }

    public String getReserve_price() {
        return reserve_price;
    }

    public void setReserve_price(String reserve_price) {
        this.reserve_price = reserve_price;
    }

    public String getZk_final_price() {
        return zk_final_price;
    }

    public String getCommission_vip() {
        return commission_vip;
    }

    public void setCommission_vip(String commission_vip) {
        this.commission_vip = commission_vip;
    }

    public void setZk_final_price(String zk_final_price) {
        this.zk_final_price = zk_final_price;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getProvcity() {
        return provcity;
    }

    public void setProvcity(String provcity) {
        this.provcity = provcity;
    }

    public String getItem_url() {
        return item_url;
    }

    public void setItem_url(String item_url) {
        this.item_url = item_url;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getCat_leaf_name() {
        return cat_leaf_name;
    }

    public void setCat_leaf_name(String cat_leaf_name) {
        this.cat_leaf_name = cat_leaf_name;
    }

    public String getIs_prepay() {
        return is_prepay;
    }

    public void setIs_prepay(String is_prepay) {
        this.is_prepay = is_prepay;
    }

    public String getShop_dsr() {
        return shop_dsr;
    }

    public void setShop_dsr(String shop_dsr) {
        this.shop_dsr = shop_dsr;
    }

    public String getRatesum() {
        return ratesum;
    }

    public void setRatesum(String ratesum) {
        this.ratesum = ratesum;
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

    public String getCoupon_info() {
        return coupon_info;
    }

    public void setCoupon_info(String coupon_info) {
        this.coupon_info = coupon_info;
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

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    @Override
    public String toString() {
        return "PromotionDetailsBean{" +
                "cat_name='" + cat_name + '\'' +
                ", num_iid='" + num_iid + '\'' +
                ", title='" + title + '\'' +
                ", pict_url='" + pict_url + '\'' +
                ", small_images=" + small_images +
                ", reserve_price='" + reserve_price + '\'' +
                ", zk_final_price='" + zk_final_price + '\'' +
                ", user_type='" + user_type + '\'' +
                ", provcity='" + provcity + '\'' +
                ", item_url='" + item_url + '\'' +
                ", seller_id='" + seller_id + '\'' +
                ", nick='" + nick + '\'' +
                ", volume='" + volume + '\'' +
                ", cat_leaf_name='" + cat_leaf_name + '\'' +
                ", is_prepay='" + is_prepay + '\'' +
                ", shop_dsr='" + shop_dsr + '\'' +
                ", ratesum='" + ratesum + '\'' +
                ", coupon_total_count='" + coupon_total_count + '\'' +
                ", coupon_remain_count='" + coupon_remain_count + '\'' +
                ", coupon_start_time='" + coupon_start_time + '\'' +
                ", coupon_end_time='" + coupon_end_time + '\'' +
                ", coupon_info='" + coupon_info + '\'' +
                ", coupon_amount='" + coupon_amount + '\'' +
                ", coupon_click_url='" + coupon_click_url + '\'' +
                ", commission_rate='" + commission_rate + '\'' +
                ", commission='" + commission + '\'' +
                ", item_description='" + item_description + '\'' +
                '}';
    }
}
