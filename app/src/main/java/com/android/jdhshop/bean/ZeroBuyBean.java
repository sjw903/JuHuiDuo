package com.android.jdhshop.bean;

import java.util.List;

public class ZeroBuyBean {
    public List<Item> list;
    public class Item{
      public String  goods_id;//	淘宝商品ID
        public String  goods_name	;//商品名称
        public String   zk_final_price;//	商品折扣价格
        public String    pict_url	;//商品主图
        public String    small_images	;//商品相册/商品小图列表
        public String   description	;//卖点信息
        public String   commission_rate;//	佣金比率，%
        public String    coupon_amount;//	优惠券金额
        public String   volume;//	销量
        public String    subsidy_amount;//	补贴金额
        public String    create_time	;//录入时间
    }
}
