package com.android.jdhshop.merchantbean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class Merchantlistbean implements MultiItemEntity {
    public static final int TEXT =0;
    public static final int IMG =1;
    public String merchant_id ;// 1 ,
    public String auth ;// 1,2 ,
    public String merchant_name ;// 车神一站式服务中心 ,
    public String merchant_avatar ;// /Public/Upload/Merchant/avatar/5e158cfb0cb71861.png ,
    public String province ;// 江苏省 ,
    public String city ;// 南京市 ,
    public String county ;// 雨花台区 ,
    public String detail_address ;// 蓝筹谷 ,
    public String lng_lat ;//  ,
    public String lng ;//  ,
    public String lat ;//  ,
    public String sales_num ;// 0 ,
    public String comment_score ;// 3.3 ,
    public String consumption ;// 0
    public String distinct;
    @Override
    public int getItemType() {
        return merchant_id==null?0:1;
    }
}
