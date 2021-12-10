package com.android.jdhshop.mallbean;

import java.io.Serializable;

public class AddressBean implements Serializable {
    public String id;//	收货地址ID
    public String user_id;//	用户ID
    public String  province;//	省份
    public String  city	;//城市
    public String  county;//	城市
    public String detail_address;//	详细地址
    public String company	;//单位名称
    public String consignee	;//收件人
    public String contact_number;//	联系电话
    public String  postcode	;//邮编
    public String is_default;//	是否为默认地址 Y是 N否

}
