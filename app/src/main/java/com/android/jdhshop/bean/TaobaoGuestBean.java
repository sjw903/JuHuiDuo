package com.android.jdhshop.bean;

import java.util.List;

/**
 * 开发者：陈飞
 * 时间:2018/7/26 15:59
 * 说明：淘宝客数据
 */
public class TaobaoGuestBean {

    private List<TaobaoGuesChildtBean> list;

    public List<TaobaoGuesChildtBean> getList() {
        return list;
    }

    public void setList(List<TaobaoGuesChildtBean> list) {
        this.list = list;
    }

    public static class TaobaoGuesChildtBean {
        private String item_url;//商品详情页面地址
        private String nick;//卖家名称
        private String num_iid;//商品ID
        private String pict_url;//商品主图
        private String pic_url;//商品主图
        private String provcity;//商品所在地
        private String reserve_price;//商品一口价格
        private String seller_id;//卖家ID
//        private SmallImagesBeans small_images;//商品相册/商品小图列表
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
        private String total_amount;
        private String sold_num;
        private String click_url;
        private String category_name;
        private String end_time;
        private String start_time;

        public String getPic_url() {
            return pic_url;
        }

        public void setPic_url(String pic_url) {
            this.pic_url = pic_url;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public String getSold_num() {
            return sold_num;
        }

        public void setSold_num(String sold_num) {
            this.sold_num = sold_num;
        }

        public String getClick_url() {
            return click_url;
        }

        public void setClick_url(String click_url) {
            this.click_url = click_url;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public static class SmallImagesBeans {
            private Object string;

            public Object getString() {
                return string;
            }

            public void setString(Object string) {
                this.string = string;
            }
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

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getNum_iid() {
            return num_iid;
        }

        public void setNum_iid(String num_iid) {
            this.num_iid = num_iid;
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

//        public SmallImagesBeans getSmall_images() {
//            return small_images;
//        }
//
//        public void setSmall_images(SmallImagesBeans small_images) {
//            this.small_images = small_images;
//        }

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

        @Override
        public String toString() {
            return "TaobaoGuesChildtBean{" +
                    "item_url='" + item_url + '\'' +
                    ", nick='" + nick + '\'' +
                    ", num_iid='" + num_iid + '\'' +
                    ", pict_url='" + pict_url + '\'' +
                    ", provcity='" + provcity + '\'' +
                    ", reserve_price='" + reserve_price + '\'' +
                    ", seller_id='" + seller_id + '\'' +
                    ", title='" + title + '\'' +
                    ", user_type='" + user_type + '\'' +
                    ", volume='" + volume + '\'' +
                    ", zk_final_price='" + zk_final_price + '\'' +
                    ", coupon_total_count='" + coupon_total_count + '\'' +
                    ", coupon_remain_count='" + coupon_remain_count + '\'' +
                    ", coupon_start_time='" + coupon_start_time + '\'' +
                    ", coupon_end_time='" + coupon_end_time + '\'' +
                    ", coupon_info=" + coupon_info +
                    ", coupon_id=" + coupon_id +
                    ", coupon_amount='" + coupon_amount + '\'' +
                    ", coupon_click_url='" + coupon_click_url + '\'' +
                    ", commission_rate='" + commission_rate + '\'' +
                    ", commission=" + commission +
                    ", is_tmall=" + is_tmall +
                    ", total_amount='" + total_amount + '\'' +
                    ", sold_num='" + sold_num + '\'' +
                    ", click_url='" + click_url + '\'' +
                    ", category_name='" + category_name + '\'' +
                    ", end_time='" + end_time + '\'' +
                    ", start_time='" + start_time + '\'' +
                    '}';
        }

        public void setCommission(double commission) {
            this.commission = commission;
        }
    }
}
