package com.android.jdhshop.bean;

import java.util.List;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : szp
 *     time   : 2020/06/17
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class WphOrderBean {
    private List<WphOrderBean> list;//订单类表
    private String type;  //订单类型 1本身（默认），2 直接，3 间接
    private String orderSn;	 //订单号
    private String goodsId;  //商品ID
    public String goodsid;
    private String goodsName;   //商品名称
    public String ordersubstatusname;
    public String ordersn;
    private String status;//订单状态：//0-不合格，1-待定，2-已完结
    private String orderSubStatusName;//订单子状态：流转状态-支持状态：（已下单、已付款、已签收、待结算、已结算、已失效）
    private int p;	//	默认第1页
    private int per	;//每页条数，默认6条
    private String order_detail_id;//	ID
    private String user_id;//用户ID
    private String order_id	;//订单记录ID
    private String goodsname;//	商品名称
    private String goodsthumb;//	商品缩略图
    private String goodscount;//	商品数量
    private String commissiontotalcost;//	商品计佣金额(元,保留两位小数)
    private String commissionrate;//	商品佣金比例(%)
    private String commission;//	商品佣金金额(元,保留两位小数)
    private String commcode;//	佣金编码：对应商品二级分类
    private String commname;	//佣金方案名称
    private String ordersource;//	订单来源
    private String sizeid;	//商品尺码：2019.01.01之后可用
    public List<WphOrderBean> getList() {
        return list;
    }
    public void setList(List<WphOrderBean> list) {
        this.list = list;
    }
    public String getSizeid() {
        return sizeid;
    }
    public void setSizeid(String sizeid) {
        this.sizeid = sizeid;
    }
    public String getOrdersource() {
        return ordersource;
    }
    public void setOrdersource(String ordersource) {
        this.ordersource = ordersource;
    }
    public String getOrdersn() {
        return ordersn;
    }
    public void setOrdersn(String ordersn) {
        this.ordersn = ordersn;
    }
    public String getCommname() {
        return commname;
    }
    public void setCommname(String commname) {
        this.commname = commname;
    }
    public String getCommcode() {
        return commcode;
    }
    public void setCommcode(String commcode) {
        this.commcode = commcode;
    }
    public String getCommission() {
        return commission;
    }
    public void setCommission(String commission) {
        this.commission = commission;
    }
    public String getCommissionrate() {
        return commissionrate;
    }
    public void setCommissionrate(String commissionrate) {
        this.commissionrate = commissionrate;
    }
    public String getCommissiontotalcost() {
        return commissiontotalcost;
    }
    public void setCommissiontotalcost(String commissiontotalcost) {
        this.commissiontotalcost = commissiontotalcost;
    }
    public String getGoodscount() {
        return goodscount;
    }
    public void setGoodscount(String goodscount) {
        this.goodscount = goodscount;
    }
    public String getGoodsId() {
        return goodsId;
    }
    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }
    public String getGoodsid() {
        return goodsid;
    }
    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }
    public String getGoodsName() {
        return goodsName;
    }
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
    public String getGoodsname() {
        return goodsname;
    }
    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }
    public String getGoodsthumb() {
        return goodsthumb;
    }
    public void setGoodsthumb(String goodsthumb) {
        this.goodsthumb = goodsthumb;
    }
    public int getP() {
        return p;
    }
    public void setP(int p) {
        this.p = p;
    }
    public int getPer() {
        return per;
    }
    public void setPer(int per) {
        this.per = per;
    }
    public String getOrder_id() {
        return order_id;
    }
    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
    public String getOrder_detail_id() {
        return order_detail_id;
    }
    public void setOrder_detail_id(String order_detail_id) {
        this.order_detail_id = order_detail_id;
    }
    public String getOrderSn() {
        return orderSn;
    }
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }
    public String getOrderSubStatusName() {
        return orderSubStatusName;
    }
    public void setOrderSubStatusName(String orderSubStatusName) {
        this.orderSubStatusName = orderSubStatusName;
    }
    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}

