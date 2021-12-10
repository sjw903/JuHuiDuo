package com.android.jdhshop.bean;

import java.io.Serializable;

/**
 * 关注的人
 */
public class AttentionBean implements Serializable {
    public int is_mutual;//是否已相互关注 0.否 1.是
    public String create_time;//关注时间
    public String auth_code;//粉丝用户邀请码
    public String user_name;//粉丝用户名称
    public String auth_icon;//粉丝用户展示头像
    public int fans_num;//关注用户的粉丝数量
    public int visit_num;//七天内访问关注用户次数
}
