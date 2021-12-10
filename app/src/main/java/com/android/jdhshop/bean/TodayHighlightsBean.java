package com.android.jdhshop.bean;

import java.util.List;

/**
 * 开发者：陈飞
 * 时间:2018/7/26 13:36
 * 说明：
 */
public class TodayHighlightsBean {

    private List<TaobaoGuestBean.TaobaoGuesChildtBean> list;

    private String title;

    public List<TaobaoGuestBean.TaobaoGuesChildtBean> getList() {
        return list;
    }

    public void setList(List<TaobaoGuestBean.TaobaoGuesChildtBean> list) {
        this.list = list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
