package com.android.jdhshop.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class PDDBean implements Serializable {
    private List<PDDBean> list;
    private String create_at;
    private String goods_id;
    private String goods_name;
    private String goods_desc;
    private String goods_thumbnail_url;
    private String goods_image_url;
    private String[] goods_gallery_urls;
    private String sold_quantity;
    private String min_group_price;
    private String min_normal_price;
    private String mall_name;
    private String merchant_type;
    private String category_id;
    private String category_name;
    private String opt_id;
    private String opt_name;
//    private String opt_ids;
//    private String cat_ids;
    private String mall_cps;
    private String has_coupon;
    private String coupon_min_order_amount;
    private String coupon_discount;
    private String coupon_total_quantity;
    private String coupon_remain_quantity;
    private String coupon_start_time;
    private String coupon_end_time;
    private String promotion_rate;
    public String goods_pic;
    private String goods_eval_score;
    private String goods_eval_count;
    private String avg_desc;
    private String avg_lgst;
    private String avg_serv;
    private String desc_pct;
    private String lgst_pct;
    private String serv_pct;
    private String commission;
    private String sales_tip;
    private String goods_sign;
    private String discount;

    public String getGoodsSign() {
        return goods_sign;
    }

    public void setGoodsSign(String goodsSign) {
        this.goods_sign = goodsSign;
    }
    public String getSales_tip() {
        return sales_tip;
    }

    public void setSales_tip(String sales_tip) {
        this.sales_tip = sales_tip;
    }

    public List<PDDBean> getList() {
        return list;
    }

    public void setList(List<PDDBean> list) {
        this.list = list;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_desc() {
        return goods_desc;
    }

    public void setGoods_desc(String goods_desc) {
        this.goods_desc = goods_desc;
    }

    public String getGoods_thumbnail_url() {
        return goods_thumbnail_url;
    }

    public void setGoods_thumbnail_url(String goods_thumbnail_url) {
        this.goods_thumbnail_url = goods_thumbnail_url;
    }

    public String getGoods_image_url() {
        return goods_image_url;
    }

    public void setGoods_image_url(String goods_image_url) {
        this.goods_image_url = goods_image_url;
    }

    public String[] getGoods_gallery_urls() {
        return goods_gallery_urls;
    }

    public void setGoods_gallery_urls(String[] goods_gallery_urls) {
        this.goods_gallery_urls = goods_gallery_urls;
    }

    public String getSold_quantity() {
        return sold_quantity;
    }

    public void setSold_quantity(String sold_quantity) {
        this.sold_quantity = sold_quantity;
    }

    public String getMin_group_price() {
        return min_group_price;
    }

    public void setMin_group_price(String min_group_price) {
        this.min_group_price = min_group_price;
    }

    public String getMin_normal_price() {
        return min_normal_price;
    }

    public void setMin_normal_price(String min_normal_price) {
        this.min_normal_price = min_normal_price;
    }

    public String getMall_name() {
        return mall_name;
    }

    public void setMall_name(String mall_name) {
        this.mall_name = mall_name;
    }

    public String getMerchant_type() {
        return merchant_type;
    }

    public void setMerchant_type(String merchant_type) {
        this.merchant_type = merchant_type;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getOpt_id() {
        return opt_id;
    }

    public void setOpt_id(String opt_id) {
        this.opt_id = opt_id;
    }

    public String getOpt_name() {
        return opt_name;
    }

    public void setOpt_name(String opt_name) {
        this.opt_name = opt_name;
    }

//    public String getOpt_ids() {
//        return opt_ids;
//    }
//
//    public void setOpt_ids(String opt_ids) {
//        this.opt_ids = opt_ids;
//    }
//
//    public String getCat_ids() {
//        return cat_ids;
//    }
//
//    public void setCat_ids(String cat_ids) {
//        this.cat_ids = cat_ids;
//    }

    public String getMall_cps() {
        return mall_cps;
    }

    public void setMall_cps(String mall_cps) {
        this.mall_cps = mall_cps;
    }

    public String getHas_coupon() {
        return has_coupon;
    }

    public void setHas_coupon(String has_coupon) {
        this.has_coupon = has_coupon;
    }

    public String getCoupon_min_order_amount() {
        return coupon_min_order_amount;
    }

    public void setCoupon_min_order_amount(String coupon_min_order_amount) {
        this.coupon_min_order_amount = coupon_min_order_amount;
    }

    public String getCoupon_discount() {
        return coupon_discount;
    }

    public void setCoupon_discount(String coupon_discount) {
        this.coupon_discount = coupon_discount;
    }

    public String getCoupon_total_quantity() {
        return coupon_total_quantity;
    }

    public void setCoupon_total_quantity(String coupon_total_quantity) {
        this.coupon_total_quantity = coupon_total_quantity;
    }

    public String getCoupon_remain_quantity() {
        return coupon_remain_quantity;
    }

    public void setCoupon_remain_quantity(String coupon_remain_quantity) {
        this.coupon_remain_quantity = coupon_remain_quantity;
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

    public String getPromotion_rate() {
        return promotion_rate;
    }

    public void setPromotion_rate(String promotion_rate) {
        this.promotion_rate = promotion_rate;
    }

    public String getGoods_eval_score() {
        return goods_eval_score;
    }

    public void setGoods_eval_score(String goods_eval_score) {
        this.goods_eval_score = goods_eval_score;
    }

    public String getGoods_eval_count() {
        return goods_eval_count;
    }

    public void setGoods_eval_count(String goods_eval_count) {
        this.goods_eval_count = goods_eval_count;
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

    public String getCommission() {
        return commission;
    }
    public void setCommission(String commission) {
        this.commission = commission;
    }


    private String goods_short_name;
    private String materiaurl;
    private String price;
    private String price_pg;
    private String price_after;

    private String quota;
    private String picurl;
    private String sales;
    private String start_time;
    private String end_time;
    private String pf_cid;
    private String pf_cname;
    private String pdd_c_info;
    private String ispg;
    private String comments;
    private String goodcommentsshare;
    private String commissionshare;
    private String shopid;
    private String shopname;
    private String has_mall_coupon;
    private String mall_coupon_discount_pct;
    private String mall_coupon_min_order_amount;
    private String mall_coupon_max_discount_amount;
    private String mall_coupon_total_quantity;
    private String mall_coupon_remain_quantity;
    private String mall_coupon_start_time;
    private String mall_coupon_end_time;
    private String zs_duo_id;
    private String coupon_take_quantity;
    private String predict_promotion_rate;
    private List<String> picurls;

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



    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice_pg() {
        return price_pg;
    }

    public void setPrice_pg(String price_pg) {
        this.price_pg = price_pg;
    }

    public String getPrice_after() {
        return price_after;
    }

    public void setPrice_after(String price_after) {
        this.price_after = price_after;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getQuota() {
        return quota;
    }

    public void setQuota(String quota) {
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

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getPf_cid() {
        return pf_cid;
    }

    public void setPf_cid(String pf_cid) {
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

    public String getIspg() {
        return ispg;
    }

    public void setIspg(String ispg) {
        this.ispg = ispg;
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



    public String getCommissionshare() {
        return commissionshare;
    }

    public void setCommissionshare(String commissionshare) {
        this.commissionshare = commissionshare;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getHas_mall_coupon() {
        return has_mall_coupon;
    }

    public void setHas_mall_coupon(String has_mall_coupon) {
        this.has_mall_coupon = has_mall_coupon;
    }

    public String getMall_coupon_discount_pct() {
        return mall_coupon_discount_pct;
    }

    public void setMall_coupon_discount_pct(String mall_coupon_discount_pct) {
        this.mall_coupon_discount_pct = mall_coupon_discount_pct;
    }

    public String getMall_coupon_min_order_amount() {
        return mall_coupon_min_order_amount;
    }

    public void setMall_coupon_min_order_amount(String mall_coupon_min_order_amount) {
        this.mall_coupon_min_order_amount = mall_coupon_min_order_amount;
    }

    public String getMall_coupon_max_discount_amount() {
        return mall_coupon_max_discount_amount;
    }

    public void setMall_coupon_max_discount_amount(String mall_coupon_max_discount_amount) {
        this.mall_coupon_max_discount_amount = mall_coupon_max_discount_amount;
    }

    public String getMall_coupon_total_quantity() {
        return mall_coupon_total_quantity;
    }

    public void setMall_coupon_total_quantity(String mall_coupon_total_quantity) {
        this.mall_coupon_total_quantity = mall_coupon_total_quantity;
    }

    public String getMall_coupon_remain_quantity() {
        return mall_coupon_remain_quantity;
    }

    public void setMall_coupon_remain_quantity(String mall_coupon_remain_quantity) {
        this.mall_coupon_remain_quantity = mall_coupon_remain_quantity;
    }

    public String getMall_coupon_start_time() {
        return mall_coupon_start_time;
    }

    public void setMall_coupon_start_time(String mall_coupon_start_time) {
        this.mall_coupon_start_time = mall_coupon_start_time;
    }

    public String getMall_coupon_end_time() {
        return mall_coupon_end_time;
    }

    public void setMall_coupon_end_time(String mall_coupon_end_time) {
        this.mall_coupon_end_time = mall_coupon_end_time;
    }








    public String getGoods_sign() {
        return goods_sign;
    }

    public void setGoods_sign(String goods_sign) {
        this.goods_sign = goods_sign;
    }

    public String getZs_duo_id() {
        return zs_duo_id;
    }

    public void setZs_duo_id(String zs_duo_id) {
        this.zs_duo_id = zs_duo_id;
    }



    public String getCoupon_take_quantity() {
        return coupon_take_quantity;
    }

    public void setCoupon_take_quantity(String coupon_take_quantity) {
        this.coupon_take_quantity = coupon_take_quantity;
    }

    public String getPredict_promotion_rate() {
        return predict_promotion_rate;
    }

    public void setPredict_promotion_rate(String predict_promotion_rate) {
        this.predict_promotion_rate = predict_promotion_rate;
    }

    public List<String> getPicurls() {
        return picurls;
    }

    public void setPicurls(List<String> picurls) {
        this.picurls = picurls;
    }

    @Override
    public String toString() {
        return "PDDBean{" +
                "list=" + list +
                ", create_at='" + create_at + '\'' +
                ", goods_id='" + goods_id + '\'' +
                ", goods_name='" + goods_name + '\'' +
                ", goods_desc='" + goods_desc + '\'' +
                ", goods_thumbnail_url='" + goods_thumbnail_url + '\'' +
                ", goods_image_url='" + goods_image_url + '\'' +
                ", goods_gallery_urls=" + Arrays.toString(goods_gallery_urls) +
                ", sold_quantity='" + sold_quantity + '\'' +
                ", min_group_price='" + min_group_price + '\'' +
                ", min_normal_price='" + min_normal_price + '\'' +
                ", mall_name='" + mall_name + '\'' +
                ", merchant_type='" + merchant_type + '\'' +
                ", category_id='" + category_id + '\'' +
                ", category_name='" + category_name + '\'' +
                ", opt_id='" + opt_id + '\'' +
                ", opt_name='" + opt_name + '\'' +
                ", mall_cps='" + mall_cps + '\'' +
                ", has_coupon='" + has_coupon + '\'' +
                ", coupon_min_order_amount='" + coupon_min_order_amount + '\'' +
                ", coupon_discount='" + coupon_discount + '\'' +
                ", coupon_total_quantity='" + coupon_total_quantity + '\'' +
                ", coupon_remain_quantity='" + coupon_remain_quantity + '\'' +
                ", coupon_start_time='" + coupon_start_time + '\'' +
                ", coupon_end_time='" + coupon_end_time + '\'' +
                ", promotion_rate='" + promotion_rate + '\'' +
                ", goods_pic='" + goods_pic + '\'' +
                ", goods_eval_score='" + goods_eval_score + '\'' +
                ", goods_eval_count='" + goods_eval_count + '\'' +
                ", avg_desc='" + avg_desc + '\'' +
                ", avg_lgst='" + avg_lgst + '\'' +
                ", avg_serv='" + avg_serv + '\'' +
                ", desc_pct='" + desc_pct + '\'' +
                ", lgst_pct='" + lgst_pct + '\'' +
                ", serv_pct='" + serv_pct + '\'' +
                ", commission='" + commission + '\'' +
                ", sales_tip='" + sales_tip + '\'' +
                ", goods_sign='" + goods_sign + '\'' +
                ", discount='" + discount + '\'' +
                ", goods_short_name='" + goods_short_name + '\'' +
                ", materiaurl='" + materiaurl + '\'' +
                ", price='" + price + '\'' +
                ", price_pg='" + price_pg + '\'' +
                ", price_after='" + price_after + '\'' +
                ", quota='" + quota + '\'' +
                ", picurl='" + picurl + '\'' +
                ", sales='" + sales + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", pf_cid='" + pf_cid + '\'' +
                ", pf_cname='" + pf_cname + '\'' +
                ", pdd_c_info='" + pdd_c_info + '\'' +
                ", ispg='" + ispg + '\'' +
                ", comments='" + comments + '\'' +
                ", goodcommentsshare='" + goodcommentsshare + '\'' +
                ", commissionshare='" + commissionshare + '\'' +
                ", shopid='" + shopid + '\'' +
                ", shopname='" + shopname + '\'' +
                ", has_mall_coupon='" + has_mall_coupon + '\'' +
                ", mall_coupon_discount_pct='" + mall_coupon_discount_pct + '\'' +
                ", mall_coupon_min_order_amount='" + mall_coupon_min_order_amount + '\'' +
                ", mall_coupon_max_discount_amount='" + mall_coupon_max_discount_amount + '\'' +
                ", mall_coupon_total_quantity='" + mall_coupon_total_quantity + '\'' +
                ", mall_coupon_remain_quantity='" + mall_coupon_remain_quantity + '\'' +
                ", mall_coupon_start_time='" + mall_coupon_start_time + '\'' +
                ", mall_coupon_end_time='" + mall_coupon_end_time + '\'' +
                ", zs_duo_id='" + zs_duo_id + '\'' +
                ", coupon_take_quantity='" + coupon_take_quantity + '\'' +
                ", predict_promotion_rate='" + predict_promotion_rate + '\'' +
                ", picurls=" + picurls +
                '}';
    }
}





