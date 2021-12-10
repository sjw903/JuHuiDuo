package com.android.jdhshop.mallbean;

import java.util.List;

public class ShopMallGoodsBean {
    public String goods_id;//	商品ID
    public String cat_id;//商品分类ID
    public String goods_name;//	商品名称
    public String goods_code;//商品编码
    public String day_num;
    public String img;//	商品图片
    public String tmp_img;//商品图片缩略图
    public String description;//	简介
    public String brand_id;//	品牌ID
    public String clicknum;//浏览量
    public String old_price;//	参考价
    public String price;//实际价格
    public String inventory;//库存
    public String give_point;//赠送积分
    public String sales_volume;//	销量
    public String createtime;//创建时间
    public List<ShopMallGoodsBean> list;
    public String	commission;//	佣金
    public String commission1;
    public String  commission_rate="1.00";//	佣金
    public String check="N";
}
