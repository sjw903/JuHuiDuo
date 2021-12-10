package com.android.jdhshop.merchantbean;

import java.util.List;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : szp
 *     time   : 2019/05/16
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class Orderlistbean {
    public String id;//  10  ,
    public String user_id;//  2  ,
    public String order_num;//  5d4a8f13062b5480  ,
    public String title;//  Selander皙兰达面膜  ,
    public String allprice;//  55500  ,
    public String give_point;//  0  ,
    public String deduction_point;//  0  ,
    public String address;//  安徽省池州市贵池区软件大道  ,
    public String company;//    ,
    public String consignee;//  李  ,
    public String contact_number;//  15996934250  ,
    public String postcode;//    ,
    public String logistics;// null ,
    public String express_number;// null ,
    public String remark;//    ,
    public String status;//  1  ,
    public String create_time;//  2019-08-07 16 ;//42 ;//59  ,
    public String pay_time;//  0000-00-00 00 ;//00 ;//00  ,
    public String deliver_time;//  0000-00-00 00 ;//00 ;//00  ,
    public String finish_time;//  0000-00-00 00 ;//00 ;//00  ,
    public String comment_time;//  0000-00-00 00 ;//00 ;//00  ,
    public String refund_time;//  0000-00-00 00 ;//00 ;//00  ,
    public String refund_success_time;//  0000-00-00 00 ;//00 ;//00  ,
    public String refund_fail_time;//  0000-00-00 00 ;//00 ;//00  ,
    public String pay_method;// null ,
    public String point;//  0  ,
    public String consumer_code;// null ,
    public String order_detail_id;//订单详情ID
    public String order_id;    //订单ID
    public String merchant_id;//	商户ID
    public String goods_id;//	商品ID
    public String goods_name;    //商品名称
    public String sku;//商品描述
    public String price;    //单价
    public String num;//数量
    public String img;    //商品图片
    public String is_comment;//	是否评论 Y是 N否
    public String use_time;//使用时间
    public List<itemorder> detail;

    public class itemorder {
        public String order_detail_id;//  10  ,
        public String order_id;//  10  ,
        public String order_num;//  5d4a8f13062b5480  ,
        public String goods_id;//  21  ,
        public String goods_name;//  Selander皙兰达面膜  ,
        public String price;// 555 ,
        public String num;//  1  ,
        public String allprice;// 555 ,
        public String give_point;//  0  ,
        public String all_give_point;//  0  ,
        public String sku;// null ,
        public String img;//  /Public/Upload/Goods/2019-07-29/5d3e9c3ecb122771.jpg  ,
        public String clicknum;//  0  ,
        public String old_price;//  666.00  ,
        public String sales_volume;//  0  ,
        public String virtual_volume;//  0  ,
        public String[] sku_arr;// [ ] ,
        public String sku_str;// false
    }

}
