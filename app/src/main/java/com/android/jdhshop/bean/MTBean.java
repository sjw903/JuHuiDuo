package com.android.jdhshop.bean;

import java.util.List;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : szp
 *     time   : 2020/06/09
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class MTBean {
    public String id;
    public String user_id;
    public String order_num;
    public String uniqueItemId;
    public String verifyTime;
    public String modifyTime;
    public String orderpaytime;
    public String actualItemAmount;
    public String actualorderamount;
    public String shopname;
    public String orderType;
    public String couponId;
    public String couponGroupId;
    public String couponDiscountAmount;
    public String couponPriceLimit;
    public String balanceamount;
    public String balanceCommissionRatio;
    public String itemStatus;
    public String balanceStatus;
    public String settlementType;
    public String couponSoure;
    public String orderPlatform;
    public String orderUserId;
    public String extraInfo;
    public String utmMedium;
    public String is_rebate;

    public int type;
    public int order_status;

    public String create_time;
    public String shop_Name;
    public String it_text;
    public String it_text_1;
    public String it_text_2;
    public String balance_Amount;
    public String fuzhi;
    public List<MTBean> list;

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public List<MTBean> getList() {
        return list;
    }

}


