package com.android.jdhshop.bean;

import java.io.Serializable;

/**
 * 开发者：陈飞
 * 时间:2018/7/28 13:32
 * 说明：历史数据
 */
public class SearchHistoryBean implements Serializable {
    private String content; //历史内容

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchHistoryBean)) return false;

        SearchHistoryBean that = (SearchHistoryBean) o;

        return content != null ? content.equals( that.content ) : that.content == null;
    }

    @Override
    public int hashCode() {
        return content != null ? content.hashCode() : 0;
    }
}
