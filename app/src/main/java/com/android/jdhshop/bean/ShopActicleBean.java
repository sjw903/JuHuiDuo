package com.android.jdhshop.bean;

/**
 * 开发者：陈飞
 * 时间:2018/7/20 15:36
 * 说明：商城文章
 */
public class ShopActicleBean {

    private ArticleAsg article_msg;

    public static class ArticleAsg {
        private String article_id;//文章ID
        private String cat_id;//文章分类ID
        private String title;//文章标题
        private String title_font_color;//标题颜色
        private String author;//作者
        private String keywords;//关键词
        private String description;//简要说明
        private String content;//内容
        private String is_show;//是否显示 Y显示 N不显示
        private String is_top;//是否置顶 Y是 N否
        private String pubtime;//发布时间
        private String img;//标题图片
        private String bigimg;//大图片
        private String file;//文件
        private String sort;//排序
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

        public String getTitle_font_color() {
            return title_font_color;
        }

        public void setTitle_font_color(String title_font_color) {
            this.title_font_color = title_font_color;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getIs_show() {
            return is_show;
        }

        public void setIs_show(String is_show) {
            this.is_show = is_show;
        }

        public String getIs_top() {
            return is_top;
        }

        public void setIs_top(String is_top) {
            this.is_top = is_top;
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

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
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
            return "ArticleAsg{" +
                    "article_id='" + article_id + '\'' +
                    ", cat_id='" + cat_id + '\'' +
                    ", title='" + title + '\'' +
                    ", title_font_color='" + title_font_color + '\'' +
                    ", author='" + author + '\'' +
                    ", keywords='" + keywords + '\'' +
                    ", description='" + description + '\'' +
                    ", content='" + content + '\'' +
                    ", is_show='" + is_show + '\'' +
                    ", is_top='" + is_top + '\'' +
                    ", pubtime='" + pubtime + '\'' +
                    ", img='" + img + '\'' +
                    ", bigimg='" + bigimg + '\'' +
                    ", file='" + file + '\'' +
                    ", sort='" + sort + '\'' +
                    ", clicknum='" + clicknum + '\'' +
                    ", href='" + href + '\'' +
                    '}';
        }
    }

    public ArticleAsg getArticle_msg() {
        return article_msg;
    }

    public void setArticle_msg(ArticleAsg article_msg) {
        this.article_msg = article_msg;
    }

    @Override
    public String toString() {
        return "ShopActicleBean{" +
                "article_msg=" + article_msg +
                '}';
    }
}
