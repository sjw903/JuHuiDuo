package com.android.jdhshop.bean;

import java.io.Serializable;

public class UserHomePageBean implements Serializable {
    public String domain;//领域（字符串有,串联）
    public int media_num;//转载文章数量
    public int fans_num;//粉丝数量
    public int follow_num;//关注数量
    public int goods_num;//被点赞数量
    public String user_sign;//用户签名
    public String back_image;//背景图片
    public int state;//状态
    public String refuse_str;//审核不通过原因
    public String error_msg;//错误原因
    public String user_name;//用户名称
    public String contact;//联系方式（微信或qq）
    public String phone;//手机号
    public int get_num;//浏览数量
    public String birthday;//生日
    public int sex;//性别
    public String area;//地区
    public String auth_icon;//头像
    public String integrity_rate;//信息完成度
    public boolean is_me;//是否为本人个人主页（true：是，false：不是）
    public boolean is_follow;//是否关注此个人主页（true：是，false：不是）
    public int auth_code;//个人主页用户邀请码

}
