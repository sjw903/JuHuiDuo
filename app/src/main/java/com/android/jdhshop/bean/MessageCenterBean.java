package com.android.jdhshop.bean;

import java.util.List;

/**
 * 开发者：陈飞
 * 时间:2018/7/28 10:22
 * 说明：消息列表
 */
public class MessageCenterBean {

    private List<MessageCenterChildBean> list;

    public List<MessageCenterChildBean> getList() {
        return list;
    }

    public void setList(List<MessageCenterChildBean> list) {
        this.list = list;
    }

    public static class MessageCenterChildBean {
        private String article_id;//文章ID
        private String cat_id;//文章分类ID
        private String title;//文章标题
        private String author;//作者
        private String keywords;//关键词
        private String description;//简要说明
        private String pubtime;//发布时间
        private String img;//标题图片
        private String bigimg;//大图片
        private String file;//文件
        private String clicknum;//浏览量
        private String href;//链接地址

        public String getArticle_id() {
            return article_id;
        }

        public void setArticle_id(String article_id) {
            this.article_id = article_id;
        }

        public String getCat_id() {
            return cat_id;
        }

        public void setCat_id(String cat_id) {
            this.cat_id = cat_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPubtime() {
            return pubtime;
        }

        public void setPubtime(String pubtime) {
            this.pubtime = pubtime;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getBigimg() {
            return bigimg;
        }

        public void setBigimg(String bigimg) {
            this.bigimg = bigimg;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getClicknum() {
            return clicknum;
        }

        public void setClicknum(String clicknum) {
            this.clicknum = clicknum;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        @Override
        public String toString() {
            return "MessageCenterChildBean{" +
                    "article_id='" + article_id + '\'' +
                    ", cat_id='" + cat_id + '\'' +
                    ", title='" + title + '\'' +
                    ", author='" + author + '\'' +
                    ", keywords='" + keywords + '\'' +
                    ", description='" + description + '\'' +
                    ", pubtime='" + pubtime + '\'' +
                    ", img='" + img + '\'' +
                    ", bigimg='" + bigimg + '\'' +
                    ", file='" + file + '\'' +
                    ", clicknum='" + clicknum + '\'' +
                    ", href='" + href + '\'' +
                    '}';
        }
    }
}
