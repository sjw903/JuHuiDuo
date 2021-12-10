package com.android.jdhshop.merchantbean;

import java.util.List;

public class Merchantmsgbean {
    public static merchant_msg merchant_msg;
    public static merchant_detail merchant_detail;
    public class merchant_msg{
        public String merchant_id;
        //商户ID
        public String group_id;
        ///商户组ID
        public String merchant_name;
        //商户名称
        public String phone;
        //手机号码
        public String auth;
        //服务
        public String email;
        //邮箱
        public String balance;
        //余额
        public String point;
        //积分
        public String   sales_num;
        //销量

        public String   comment_score;
        //评分
        public String consumption;
    }
    public class merchant_detail{
        public String merchant_avatar;
        //商户门头照片
        public String service_tel;
        //客服电话
        public String business_day_begin;
        //营业日开始日期
        public String business_day_end;
        //营业日结束日期
        public String business_hours_begin;
        //营业开始时间
        public String business_hours_end;
        //营业结束时间
        public String province;
        //省份
        public String city;
        //城市
        public String county;
        //区域/县
        public String   detail_address;
        //详细地址
        public String  lng_lat;
        // 经纬度
        public String  lng;
        //经度
        public String  lat;
        //纬度

        public String   sales_num;
        //销量

        public String   comment_score;
        //评分
        public String consumption;

    }
}
