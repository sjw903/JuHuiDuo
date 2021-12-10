package com.android.jdhshop.mallbean;

import java.io.Serializable;

public class MallGoodsCarListBean implements Serializable {
    public String id;//ID 购物车id
    public String user_id;//用户ID
    public String merchant_id;//商户ID
    public String goods_id;//商品ID
    public String goods_num;//商品数量
    public String goods_sku;
    public String goods_name;//商品名称
    public String img;//商品图片
    public String tmp_img;//商品图片缩略图
    public String old_price;//参考价格
    public String price;//实际价格
    public String clicknum;//浏览量
    public String inventory;//库存
    public String sales_volume;//实际销量
    public String virtual_volume;//虚拟销量
    public String sku_str;//商品规格属性说明
    public String sku_arr;//商品规格属性
    public String postage;//运费
    public boolean ischeck = false;

}
