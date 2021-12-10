package com.android.jdhshop.bean;

import java.util.List;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : szp
 *     time   : 2019/05/17
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class OrderMsgbean {
    public String id;//	订单ID
    public String user_id;//	用户ID
    public String order_num;//	订单号
    public String title;//	订单名称
    public String allprice;//	订单总价
    public String give_point;//	赠送积分数
    public String address;//	收货地址
    public String company;//	单位名称
    public String consignee;//	收件人
    public String contact_number;//	收件人电话
    public String postcode;//	邮编
    public String logistics;//	快递/物流公司
    public String express_number;//	快递单号
    public String remark;//	备注
    public String status;//	订单状态
    //1待付款
//2已付款、待发货
//3已发货、待确认收货
//4已确认收货、已完成
    public String status_zh;//	订单状态中文描述
    public String createtime;//	下单时间
    public String pay_time;//	支付时间
    public String deliver_time;//	配送时间
    public String finish_time;//	完成时间
    public String comment_time;//	评价时间
    public String refund_time;//	申请退款时间
    public String  refund_success_time;//	退款成功时间
    public String  refund_fail_time;//	拒绝退款时间
    public String pay_method;//	支付方式
    //alipay;//支付宝支付
    //wxpay微信支付
    // balance;//余额支付
    public String point;//	抵扣积分
    public List<OrderMsgdetailbean> detail;//	订单详情列表


//    public String order_id;//	订单ID
//    public String user_id;//	用户ID
//    public String order_num;//	订单号
//    public String title;//	订单名称
//    public String allprice;//	订单总价
//    public String address;//	收货地址
//    public String company;//	单位名称
//    public String consignee;//	收件人
//    public String  contact_number;//	收件人电话
//    public String  postcode;//	邮编
//    public String  remark;//	备注
//   public List<OrderMsgdetailbean> detail;//	订单详情列表

}
