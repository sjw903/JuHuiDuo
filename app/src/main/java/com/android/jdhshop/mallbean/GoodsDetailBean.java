package com.android.jdhshop.mallbean;

import java.util.List;

public class GoodsDetailBean {
    public DetailChild goodsMsg;
    public List<Imglist> imglist;//	商品相册
    public List<SkuList> skulist;
    public CatMsg catMsg;
    public class SkuList {
        public String goods_sku_id;//ID
        public String goods_id;//商品ID
        public String sku;//商品SKU配置，json数组
        public String price;//价格
        public String inventory;//	库存
        public String give_point;//	赠送积分
        public String img;//	图片路径
    }
    public class CatMsg {
        public String cat_name;//ID
        public String day_num;//商品ID
        public String give_point;//商品SKU配置，json数组
        public String commission1;//价格
    }

    public class Imglist {
        public String goods_img_id;//	ID
        public String goods_id;//	商品ID
        public String title;//	图片名称
        public String img;//	图片路径
        public String createtime;//	创建时间
    }

    public class DetailChild {
        public String goods_id;//	商品ID
        public String merchant_id;//	商户ID
        public String merchant_name;//	商户ID
        public String cat_id;//商品分类ID
        public String goods_name;//	商品名称
        public String goods_code;//	商品编码
        public String img;//	商品图片
        public String tmp_img;//	商品图片缩略图
        public String description;//	简介
        public String content;//	商品详情
        public String brand_id;//	品牌ID
        public String brand_name;//品牌名称
        public String is_top;//	是否推荐商品 Y是N否
        public String is_sale;//	是否折扣商品 Y是N否
        public String clicknum;//	浏览量
        public String old_price;//	参考价
        public String commission;//	佣金
        public String commission_rate;//	佣金
        public String is_send;
        public String price;//	实际价格
        public String inventory;//	库存
        public String give_point;//	赠送积分
        public String sales_volume;//	销量
        public String video;
        public String createtime;//	创建时间
        public String postage;//运费
        public List<Skuxianshibean> sku_arr;//	商品SKU属性
    }
}
