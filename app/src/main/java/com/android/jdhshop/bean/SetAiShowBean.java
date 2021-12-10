package com.android.jdhshop.bean;

import java.io.Serializable;

public class SetAiShowBean implements Serializable {
    public String id ;
    public String user_id ;//微信群所属用户ID
    public String  name;//微信群名称
    public String welcome ;//进群欢迎语
    public String  welcome_status;//欢迎语状态：0未开启，1开启
    public String  start_timestamp;//开始推送时间
    public String  stop_timestamp;//结束推送时间
    public String  interval_time;//两次推送之间的间隔，0：10分，1：20分，2：30分，3：50分
    public String  platform;//发品平台类型：1拼多多，2京东,3淘宝
    public String  product_type;//推送商品类型：0:全部，1有券商品，2无券商品
    public String  commission_type;//佣金商品类型：0全部,1有佣金,2无佣金
    public String  desc_type;//详情推广类型：0不推送，1我拿返利,2我的推广
    public String  href_type;//商品链接类型：0不推送,1我拿返利

    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getWelcome() {
        return welcome;
    }

    public String getWelcome_status() {
        return welcome_status;
    }

    public String getStart_timestamp() {
        return start_timestamp;
    }

    public String getStop_timestamp() {
        return stop_timestamp;
    }

    public String getInterval_time() {
        return interval_time;
    }

    public String getPlatform() {
        return platform;
    }

    public String getProduct_type() {
        return product_type;
    }

    public String getCommission_type() {
        return commission_type;
    }

    public String getDesc_type() {
        return desc_type;
    }

    public String getHref_type() {
        return href_type;
    }

    public String getGroup_type() {
        return group_type;
    }

    public String getRobot_name() {
        return robot_name;
    }

    public String  group_type;//微信群类型：0用户全部群组默认配置，1正常群组
    public String  robot_name;//机器人名字，用户可修改

}
