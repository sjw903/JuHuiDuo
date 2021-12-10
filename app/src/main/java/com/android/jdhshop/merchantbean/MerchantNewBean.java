package com.android.jdhshop.merchantbean;

import java.io.Serializable;
import java.util.List;

public class MerchantNewBean {
    public List<Item> list;

    public class Item implements Serializable {
        public String news_id;
        public String title;
        public String icon;
        public String mob_img;
        public String mob_text;
        public String pubtime;
        public String clicknum;
        public String id;//抵扣卷ID
        public String merchant_id;//	商家ID
        public String description;//	抵扣卷说明
        public String amount;    //卷金额
        public String price;//购买价格
        public String inventory;//	库存
        public String quota;//每人每日限购
        public String validity_type;//	有效期类型1：按天2：按截至日期
        public String validity_days;    //有效期天数
        public String validity_date;//有效期截至日期
        public String validity_zh;//有效期类型中文描述

    }

}
