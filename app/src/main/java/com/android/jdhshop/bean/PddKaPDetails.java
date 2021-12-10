package com.android.jdhshop.bean;

import java.util.List;

public class PddKaPDetails {

    private DataBean data;
    private String message;
    private int status_code;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public static class DataBean {


        private int total;
        private List<GoodsListBean> goods_list;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<GoodsListBean> getGoods_list() {
            return goods_list;
        }

        public void setGoods_list(List<GoodsListBean> goods_list) {
            this.goods_list = goods_list;
        }

        public static class GoodsListBean {
            private long goods_id;
            private String goods_name;
            private String goods_short_name;
            private String materiaurl;
            private String goods_desc;
            private int price;
            private int price_pg;
            private String price_after;
            private int discount;
            private int quota;
            private String picurl;
            private String sales;
            private int start_time;
            private int end_time;
            private int pf_cid;
            private String pf_cname;
            private String pdd_c_info;
            private int ispg;
            private int opt_id;
            private String opt_name;
            private String comments;
            private String goodcommentsshare;
            private String commission;
            private int commissionshare;
            private int shopid;
            private String shopname;
            private int has_mall_coupon;
            private int mall_coupon_discount_pct;
            private int mall_coupon_min_order_amount;
            private int mall_coupon_max_discount_amount;
            private int mall_coupon_total_quantity;
            private int mall_coupon_remain_quantity;
            private int mall_coupon_start_time;
            private int mall_coupon_end_time;
            private String avg_desc;
            private String avg_lgst;
            private String avg_serv;
            private String desc_pct;
            private String lgst_pct;
            private String serv_pct;
            private String goods_sign;
            private int zs_duo_id;
            private int coupon_remain_quantity;
            private int coupon_total_quantity;
            private int coupon_take_quantity;
            private int predict_promotion_rate;
            private List<String> picurls;

            public long getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(long goods_id) {
                this.goods_id = goods_id;
            }

            public String getGoods_name() {
                return goods_name;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
            }

            public String getGoods_short_name() {
                return goods_short_name;
            }

            public void setGoods_short_name(String goods_short_name) {
                this.goods_short_name = goods_short_name;
            }

            public String getMateriaurl() {
                return materiaurl;
            }

            public void setMateriaurl(String materiaurl) {
                this.materiaurl = materiaurl;
            }

            public String getGoods_desc() {
                return goods_desc;
            }

            public void setGoods_desc(String goods_desc) {
                this.goods_desc = goods_desc;
            }

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public int getPrice_pg() {
                return price_pg;
            }

            public void setPrice_pg(int price_pg) {
                this.price_pg = price_pg;
            }

            public String getPrice_after() {
                return price_after;
            }

            public void setPrice_after(String price_after) {
                this.price_after = price_after;
            }

            public int getDiscount() {
                return discount;
            }

            public void setDiscount(int discount) {
                this.discount = discount;
            }

            public int getQuota() {
                return quota;
            }

            public void setQuota(int quota) {
                this.quota = quota;
            }

            public String getPicurl() {
                return picurl;
            }

            public void setPicurl(String picurl) {
                this.picurl = picurl;
            }

            public String getSales() {
                return sales;
            }

            public void setSales(String sales) {
                this.sales = sales;
            }

            public int getStart_time() {
                return start_time;
            }

            public void setStart_time(int start_time) {
                this.start_time = start_time;
            }

            public int getEnd_time() {
                return end_time;
            }

            public void setEnd_time(int end_time) {
                this.end_time = end_time;
            }

            public int getPf_cid() {
                return pf_cid;
            }

            public void setPf_cid(int pf_cid) {
                this.pf_cid = pf_cid;
            }

            public String getPf_cname() {
                return pf_cname;
            }

            public void setPf_cname(String pf_cname) {
                this.pf_cname = pf_cname;
            }

            public String getPdd_c_info() {
                return pdd_c_info;
            }

            public void setPdd_c_info(String pdd_c_info) {
                this.pdd_c_info = pdd_c_info;
            }

            public int getIspg() {
                return ispg;
            }

            public void setIspg(int ispg) {
                this.ispg = ispg;
            }

            public int getOpt_id() {
                return opt_id;
            }

            public void setOpt_id(int opt_id) {
                this.opt_id = opt_id;
            }

            public String getOpt_name() {
                return opt_name;
            }

            public void setOpt_name(String opt_name) {
                this.opt_name = opt_name;
            }

            public String getComments() {
                return comments;
            }

            public void setComments(String comments) {
                this.comments = comments;
            }

            public String getGoodcommentsshare() {
                return goodcommentsshare;
            }

            public void setGoodcommentsshare(String goodcommentsshare) {
                this.goodcommentsshare = goodcommentsshare;
            }

            public String getCommission() {
                return commission;
            }

            public void setCommission(String commission) {
                this.commission = commission;
            }

            public int getCommissionshare() {
                return commissionshare;
            }

            public void setCommissionshare(int commissionshare) {
                this.commissionshare = commissionshare;
            }

            public int getShopid() {
                return shopid;
            }

            public void setShopid(int shopid) {
                this.shopid = shopid;
            }

            public String getShopname() {
                return shopname;
            }

            public void setShopname(String shopname) {
                this.shopname = shopname;
            }

            public int getHas_mall_coupon() {
                return has_mall_coupon;
            }

            public void setHas_mall_coupon(int has_mall_coupon) {
                this.has_mall_coupon = has_mall_coupon;
            }

            public int getMall_coupon_discount_pct() {
                return mall_coupon_discount_pct;
            }

            public void setMall_coupon_discount_pct(int mall_coupon_discount_pct) {
                this.mall_coupon_discount_pct = mall_coupon_discount_pct;
            }

            public int getMall_coupon_min_order_amount() {
                return mall_coupon_min_order_amount;
            }

            public void setMall_coupon_min_order_amount(int mall_coupon_min_order_amount) {
                this.mall_coupon_min_order_amount = mall_coupon_min_order_amount;
            }

            public int getMall_coupon_max_discount_amount() {
                return mall_coupon_max_discount_amount;
            }

            public void setMall_coupon_max_discount_amount(int mall_coupon_max_discount_amount) {
                this.mall_coupon_max_discount_amount = mall_coupon_max_discount_amount;
            }

            public int getMall_coupon_total_quantity() {
                return mall_coupon_total_quantity;
            }

            public void setMall_coupon_total_quantity(int mall_coupon_total_quantity) {
                this.mall_coupon_total_quantity = mall_coupon_total_quantity;
            }

            public int getMall_coupon_remain_quantity() {
                return mall_coupon_remain_quantity;
            }

            public void setMall_coupon_remain_quantity(int mall_coupon_remain_quantity) {
                this.mall_coupon_remain_quantity = mall_coupon_remain_quantity;
            }

            public int getMall_coupon_start_time() {
                return mall_coupon_start_time;
            }

            public void setMall_coupon_start_time(int mall_coupon_start_time) {
                this.mall_coupon_start_time = mall_coupon_start_time;
            }

            public int getMall_coupon_end_time() {
                return mall_coupon_end_time;
            }

            public void setMall_coupon_end_time(int mall_coupon_end_time) {
                this.mall_coupon_end_time = mall_coupon_end_time;
            }

            public String getAvg_desc() {
                return avg_desc;
            }

            public void setAvg_desc(String avg_desc) {
                this.avg_desc = avg_desc;
            }

            public String getAvg_lgst() {
                return avg_lgst;
            }

            public void setAvg_lgst(String avg_lgst) {
                this.avg_lgst = avg_lgst;
            }

            public String getAvg_serv() {
                return avg_serv;
            }

            public void setAvg_serv(String avg_serv) {
                this.avg_serv = avg_serv;
            }

            public String getDesc_pct() {
                return desc_pct;
            }

            public void setDesc_pct(String desc_pct) {
                this.desc_pct = desc_pct;
            }

            public String getLgst_pct() {
                return lgst_pct;
            }

            public void setLgst_pct(String lgst_pct) {
                this.lgst_pct = lgst_pct;
            }

            public String getServ_pct() {
                return serv_pct;
            }

            public void setServ_pct(String serv_pct) {
                this.serv_pct = serv_pct;
            }

            public String getGoods_sign() {
                return goods_sign;
            }

            public void setGoods_sign(String goods_sign) {
                this.goods_sign = goods_sign;
            }

            public int getZs_duo_id() {
                return zs_duo_id;
            }

            public void setZs_duo_id(int zs_duo_id) {
                this.zs_duo_id = zs_duo_id;
            }

            public int getCoupon_remain_quantity() {
                return coupon_remain_quantity;
            }

            public void setCoupon_remain_quantity(int coupon_remain_quantity) {
                this.coupon_remain_quantity = coupon_remain_quantity;
            }

            public int getCoupon_total_quantity() {
                return coupon_total_quantity;
            }

            public void setCoupon_total_quantity(int coupon_total_quantity) {
                this.coupon_total_quantity = coupon_total_quantity;
            }

            public int getCoupon_take_quantity() {
                return coupon_take_quantity;
            }

            public void setCoupon_take_quantity(int coupon_take_quantity) {
                this.coupon_take_quantity = coupon_take_quantity;
            }

            public int getPredict_promotion_rate() {
                return predict_promotion_rate;
            }

            public void setPredict_promotion_rate(int predict_promotion_rate) {
                this.predict_promotion_rate = predict_promotion_rate;
            }

            public List<String> getPicurls() {
                return picurls;
            }

            public void setPicurls(List<String> picurls) {
                this.picurls = picurls;
            }
        }
    }
}
