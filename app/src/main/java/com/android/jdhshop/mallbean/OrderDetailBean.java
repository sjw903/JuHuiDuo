package com.android.jdhshop.mallbean;

import java.io.Serializable;
import java.util.List;

public class OrderDetailBean implements Serializable {
    public List<OrderMsg> list;
    public OrderMsg orderMsg;
    public class OrderMsg  implements Serializable{
        public String    id	;//订单ID
        public String   user_id	;//用户ID
        public String   order_num;//	订单号
        public String   title;//	订单名称
        public String  allprice	;//订单总价
        public String   give_point	;//赠送积分数
        public String    address;//	收货地址
        public String   company;//	单位名称
        public String   consignee	;//收件人
        public String   contact_number;//	收件人电话
        public String  postcode;//	邮编
        public String   logistics;//	快递/物流公司
        public String    express_number;//	快递单号
        public String    remark;//	备注
        public String     status;//	订单状态
//1待付款
//2已付款、待发货
//3已发货、待确认收货
//4已确认收货、已完成
        public String  create_time	;//下单时间
        public String  pay_time	;//支付时间
        public String    deliver_time;//	发货、配送时间
        public String   finish_time;//	确认收货、完成时间
        public String   comment_time;//	评价时间
        public String   refund_time	;//申请退款时间
        public String   refund_success_time;//	退款成功时间
        public String  refund_fail_time	;//拒绝退款时间
        public String  pay_method	;//支付方式
//        public String   alipay支付宝支付 wxpay微信支付
        public String   point;//	可抵扣积分
        public String refund_express_number;
        public String drawback_reason;
        public String drawback_refuse_reason;
        public String reject_reason;
        @Override
        public String toString() {
            return "OrderMsg{" +
                    "id='" + id + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", order_num='" + order_num + '\'' +
                    ", title='" + title + '\'' +
                    ", allprice='" + allprice + '\'' +
                    ", give_point='" + give_point + '\'' +
                    ", address='" + address + '\'' +
                    ", company='" + company + '\'' +
                    ", consignee='" + consignee + '\'' +
                    ", contact_number='" + contact_number + '\'' +
                    ", postcode='" + postcode + '\'' +
                    ", logistics='" + logistics + '\'' +
                    ", express_number='" + express_number + '\'' +
                    ", remark='" + remark + '\'' +
                    ", status='" + status + '\'' +
                    ", create_time='" + create_time + '\'' +
                    ", pay_time='" + pay_time + '\'' +
                    ", deliver_time='" + deliver_time + '\'' +
                    ", finish_time='" + finish_time + '\'' +
                    ", comment_time='" + comment_time + '\'' +
                    ", refund_time='" + refund_time + '\'' +
                    ", refund_success_time='" + refund_success_time + '\'' +
                    ", refund_fail_time='" + refund_fail_time + '\'' +
                    ", pay_method='" + pay_method + '\'' +
                    ", point='" + point + '\'' +
                    ", detail=" + detail +
                    '}';
        }

        public List<Detail> detail;
        public class Detail  implements Serializable{
            public String   order_detail_id	;//订单详情ID
            public String     order_id;//	订单ID
            public String     order_num	;//订单号
            public String    goods_id	;//商品ID
            public String    goods_name;//	商品名称
            public String   sku;//	商品描述
            public String     price;//	单价
            public String     num;//	数量
            public String     allprice	;//总价
            public String    give_point	;//单件赠送积分
            public String    all_give_point;//	总赠送积分
            public String    img;//	商品图片
            public String    clicknum;//	浏览量
            public String   old_price;//	参考价格
            public String      sales_volume;//	销售量
            public String      sku_str	;//商品SKU属性

        }
    }
}
