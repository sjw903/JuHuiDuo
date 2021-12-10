package com.android.jdhshop.bean;

/**
 * 用户信息
 * Created by yohn on 2018/7/28.
 */

public class UserInfoBean {
    public UserDetailBean user_detail;//用户详情
    public UserMsgBean user_msg;//用户账号信息
    public static class UserMsgBean {
        public String uid;//		用户ID
        public String group_id;//		会员组ID
        public String group_name;//		会员组ID
        public String tb_rid;
        public String username;//		用户名
        public String phone;//		手机号码
        public String email;//		邮箱
        public String user_code;//		授权码
        public String balance;//		余额
        public String point;//	积分
        public String balance_user;//余额-用户
        public String balance_service;//余额-扣税
        public String balance_plantform;//	余额-平台
        public String hpoint;
        public String hpoint_yesterday;
        public String hpoint_sum;
        public String hpoint_today;
        public String is_forever;//是否终生会员 Y是 N否
        public String expiration_date;//会员到期时间
        public String alipay_account;//支付宝账号
        public String auth_code;
        public String is_share_vip;
        public String share_url; // 用户分享链接
        public String is_complete_info;
        public String is_sign;
        public String hasTalkGroup;
    }

    public static class UserDetailBean {
        public String user_id;//	用户ID
        public String nickname;//	昵称
        public String avatar;//	头像
        public String truename;//	真实姓名
        public String sex;//	性别
        public String height;//	身高
        public String weight;//	体重
        public String blood;//	血型
        public String birthday;//	出生日期
        public String qq;//	QQ号
        public String weixin;//	微信号
        public String province;//	省份
        public String city;//	城市
        public String county;//	县/区域
        public String detail_address;//	详细地址
        public String signature;//	个性签名
        public String hasTalkGroup;
        public int is_information; // 普通通知是否开启 1开 其它关
        public int is_account; // 微信公众号通知是否开启 1开 其它关
    }
}
