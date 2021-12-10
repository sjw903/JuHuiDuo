package com.android.jdhshop.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 开发者：陈飞
 * 时间:2018/7/26 15:59
 * 说明：淘宝客数据
 */
public class SjhdBean implements Serializable {

    private List<SjhdListBean> list;

    public List<SjhdListBean> getList() {
        return list;
    }

    public void setList(List<SjhdListBean> list) {
        this.list = list;
    }

    public static class SjhdListBean implements Serializable {
       private String activity_id;//活动ID

       private String merchant_id;//	商家ID
       private String title;//'	活动标题
       private String img;//	活动图片
       private String start_time;//	活动开始时间
       private String end_time;//	活动结束时间
       private String content;//	活动详情
       private String reward;//	活动奖励
       private String rules;//	活动规则
       private String pubtime;//	发布时间
       private String sort;//	排序
       private String is_top;//	是否推荐/置顶 Y是 N否
       private String is_open;//	是否开启活动 Y是 N否

        public String getReward() {
            return reward;
        }

        public void setReward(String reward) {
            this.reward = reward;
        }

        public String getActivity_id() {
            return activity_id;
        }

        public void setActivity_id(String activity_id) {
            this.activity_id = activity_id;
        }

        public String getMerchant_id() {
            return merchant_id;
        }

        public void setMerchant_id(String merchant_id) {
            this.merchant_id = merchant_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getRules() {
            return rules;
        }

        public void setRules(String rules) {
            this.rules = rules;
        }

        public String getPubtime() {
            return pubtime;
        }

        public void setPubtime(String pubtime) {
            this.pubtime = pubtime;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getIs_top() {
            return is_top;
        }

        public void setIs_top(String is_top) {
            this.is_top = is_top;
        }

        public String getIs_open() {
            return is_open;
        }

        public void setIs_open(String is_open) {
            this.is_open = is_open;
        }
    }
}
