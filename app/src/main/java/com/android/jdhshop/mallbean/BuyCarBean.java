package com.android.jdhshop.mallbean;

import java.io.Serializable;
import java.util.List;

public class BuyCarBean implements Serializable {
    private String goods_id;
    private String merchant_id;
    private String img;
    private String goods_name;
    private String price;
    private String old_price;
    private String num;
    private String postage;
    private List<Selectbean> selectbeans;

    public String getPostage() {
        return postage;
    }

    public void setPostage(String postage) {
        this.postage = postage;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOld_price() {
        return old_price;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public List<Selectbean> getSelectbeans() {
        return selectbeans;
    }

    public void setSelectbeans(List<Selectbean> selectbeans) {
        this.selectbeans = selectbeans;
    }
}
