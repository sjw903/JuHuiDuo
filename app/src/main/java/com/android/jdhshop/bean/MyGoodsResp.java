package com.android.jdhshop.bean;

import java.io.Serializable;

import jd.union.open.goods.query.response.CategoryInfo;
import jd.union.open.goods.query.response.CommissionInfo;
import jd.union.open.goods.query.response.CouponInfo;
import jd.union.open.goods.query.response.GoodsResp;
import jd.union.open.goods.query.response.ImageInfo;
import jd.union.open.goods.query.response.PingGouInfo;
import jd.union.open.goods.query.response.PriceInfo;
import jd.union.open.goods.query.response.ShopInfo;

public class MyGoodsResp implements Serializable {
    public CategoryInfo categoryInfo;
    private Long comments;
    public CommissionInfo commissionInfo;
    public JdCommBean jdCommBean;
    public CouponInfo couponInfo;
    public GoodsResp goodsResp;
    private Double goodCommentsShare;
    public ImageInfo imageInfo;
    private Long inOrderCount30Days;
    private Integer isJdSale;
    private String materialUrl;
    private String link;
    public PriceInfo priceInfo;
    public ShopInfo shopInfo;
    private Long skuId;
    private String skuName;
    private String couponCommission;
    private Integer isHot;
    private Long spuid;
    private String brandCode;
    private String brandName;
    private String owner;
    public PingGouInfo pingGouInfo;
    public String endTime;
    public String startTime;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public MyGoodsResp() {
    }


    public void setComments(Long var1) {
        this.comments = var1;
    }

    public Long getComments() {
        return this.comments;
    }




    public void setGoodCommentsShare(Double var1) {
        this.goodCommentsShare = var1;
    }

    public Double getGoodCommentsShare() {
        return this.goodCommentsShare;
    }

    public void setInOrderCount30Days(Long var1) {
        this.inOrderCount30Days = var1;
    }

    public Long getInOrderCount30Days() {
        return this.inOrderCount30Days;
    }

    public void setIsJdSale(Integer var1) {
        this.isJdSale = var1;
    }

    public Integer getIsJdSale() {
        return this.isJdSale;
    }

    public void setMaterialUrl(String var1) {
        this.materialUrl = var1;
    }

    public String getMaterialUrl() {
        return this.materialUrl;
    }
    public void setCoupComminss(String var1) {
        this.couponCommission = var1;
    }

    public String getCoupComminss() {
        return this.couponCommission;
    }

    public void setSkuId(Long var1) {
        this.skuId = var1;
    }

    public Long getSkuId() {
        return this.skuId;
    }

    public void setSkuName(String var1) {
        this.skuName = var1;
    }

    public String getSkuName() {
        return this.skuName;
    }

    public void setIsHot(Integer var1) {
        this.isHot = var1;
    }

    public Integer getIsHot() {
        return this.isHot;
    }

    public void setSpuid(Long var1) {
        this.spuid = var1;
    }

    public Long getSpuid() {
        return this.spuid;
    }

    public void setBrandCode(String var1) {
        this.brandCode = var1;
    }

    public String getBrandCode() {
        return this.brandCode;
    }

    public void setBrandName(String var1) {
        this.brandName = var1;
    }

    public String getBrandName() {
        return this.brandName;
    }

    public void setOwner(String var1) {
        this.owner = var1;
    }

    public String getOwner() {
        return this.owner;
    }
}
