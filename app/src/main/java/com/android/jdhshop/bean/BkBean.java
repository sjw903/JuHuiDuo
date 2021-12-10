package com.android.jdhshop.bean;

import java.io.Serializable;
import java.util.List;

public class BkBean implements Serializable {
    public List<BKItem> list;
    public class BKItem{
        public String   goods_id;//	淘宝商品ID
        public String     goods_name;//	商品名称
        public String    zk_final_price;//	商品折扣价格
        public String       pict_url;//	商品主图
        public List<String>     small_images	;//商品相册/商品小图列表
        public String     description;//	卖点信息
        public String     commission_rate	;//佣金比率，%
        public String    coupon_amount	;//优惠券金额
        public String    volume		;//销量
        public String    create_time	;//	录入时间
        public String itemprice;
        public String tkrates;
        public String itemendprice;
        public String copy_comment;
        public String copy_content;
        public String title;
        public String itemid;
        public List<String> itempic;
        public String dummy_click_statistics;
        public String sola_image;
        public String couponmoney;
        public String add_time;
    }
}
