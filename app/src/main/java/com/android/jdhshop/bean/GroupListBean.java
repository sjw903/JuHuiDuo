package com.android.jdhshop.bean;

import java.util.List;

public class GroupListBean {
    public List<Item> list;

    public class Item {
        public String id;//会员组ID
        public String title;//	会员组名称
        public String exp;//会员组升级所属成长/经验值
        public String fee_user;//该组会员淘宝/拼多多/京东购物可得佣金 自购返佣
        public String referrer_rate; // 邀请奖励
        public String referrer_rate2; // 教学奖励
        public String referrer_team; // 团队奖励
        public String referrer_store; // 店铺奖励
    }
}
