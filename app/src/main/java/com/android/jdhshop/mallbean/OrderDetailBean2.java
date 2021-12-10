package com.android.jdhshop.mallbean;

import java.io.Serializable;
import java.util.List;

public class OrderDetailBean2 implements Serializable {
   public OrderDetailBean2 orderMsg;//	订单详情
    public String order_id;//	订单ID
    public String    user_id;//	用户ID
    public String  order_num;//	订单号
    public String  title	;//订单名称
    public String allprice;//	订单总价
    public String address;//	收货地址
    public String  company	;//单位名称
    public String postage;//邮费
    public String  consignee;//	收件人
    public String  contact_number;//	收件人电话
    public String  postcode	;//邮编
    public String   remark;//	备注
    public List<OrderDetailBean>  detail;//	订单详情列表

}
