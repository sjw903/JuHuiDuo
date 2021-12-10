package com.android.jdhshop.bean;

import java.util.List;

public class GoodsSmoke {
    public List<Item> list;
    public class Item {
        public String phone;
        public String action;//	操作类型
        public String action_id;//操作类型id
        //1=领取了优惠券
//2=分享了宝贝
//3=购买了宝贝
        public String time;//时间
        public String smoke;//	烟雾弹操作
        public String avatar;
    }
}
