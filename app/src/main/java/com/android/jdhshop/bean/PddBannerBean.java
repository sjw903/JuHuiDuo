package com.android.jdhshop.bean;

import java.util.List;

public class PddBannerBean {


    /**
     * code : 0
     * msg : 成功
     * data : {"list":[{"id":106,"cat_id":11,"title":"测试5","img":"https://juduohui.oss-cn-beijing.aliyuncs.com/Upload/Banner/2021-03-26/605d4a4b54a06791.jpg","color":"#FFACAC","href":"https://baijiahao.baidu.com/s?id=1688822512152718917&wfr=spider&for=pc","sort":5,"is_show":"Y","createtime":"2021-03-26 10:43:30","type":"1","type_value":"","agent_id":0},{"id":102,"cat_id":11,"title":"阿迪达斯奥莱官方专场","img":"https://juduohui.oss-cn-beijing.aliyuncs.com/Upload/Banner/2021-03-26/605d4aa1d672c612.jpg","color":"#66812B","href":"https://api-gw.haojingke.com/index.php/v1/api/pdd/getrecommendgoods?apikey={mayi_apikey}&offset={offset}&limit={limit}&isunion=0&channel_type=0","sort":4,"is_show":"Y","createtime":"2021-03-29 11:07:16","type":"4","type_value":"39999","agent_id":0},{"id":105,"cat_id":11,"title":"1.9包邮","img":"/Upload/Banner/2021-03-29/60614660aaddb906.png","color":"#FFFF81","href":"https://api-gw.haojingke.com/index.php/v1/api/pdd/getrecommendgoods?apikey={mayi_apikey}&offset={offset}&limit={limit}&isunion=0&channel_type=0","sort":3,"is_show":"Y","createtime":"2021-03-29 11:15:44","type":"1","type_value":"0","agent_id":0},{"id":103,"cat_id":11,"title":"今日爆款","img":"/Upload/Banner/2021-03-29/606148a6d5216365.png","color":"#66AC81","href":"https://api-gw.haojingke.com/index.php/v1/api/pdd/getrecommendgoods?apikey={mayi_apikey}&offset={offset}&limit={limit}&isunion=0&channel_type=1","sort":2,"is_show":"Y","createtime":"2021-03-29 11:25:40","type":"1","type_value":"1","agent_id":0},{"id":104,"cat_id":11,"title":"品牌清仓","img":"/Upload/Banner/2021-03-29/6061499acbab2115.png","color":"#FF562B","href":"https://api-gw.haojingke.com/index.php/v1/api/pdd/getrecommendgoods?apikey={mayi_apikey}&offset={offset}&limit={limit}&isunion=0&channel_type=2","sort":1,"is_show":"Y","createtime":"2021-03-29 11:32:42","type":"1","type_value":"2","agent_id":0}]}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 106
             * cat_id : 11
             * title : 测试5
             * img : https://juduohui.oss-cn-beijing.aliyuncs.com/Upload/Banner/2021-03-26/605d4a4b54a06791.jpg
             * color : #FFACAC
             * href : https://baijiahao.baidu.com/s?id=1688822512152718917&wfr=spider&for=pc
             * sort : 5
             * is_show : Y
             * createtime : 2021-03-26 10:43:30
             * type : 1
             * type_value :
             * agent_id : 0
             */

            private String id;
            private int cat_id;
            private String title;
            private String img;
            private String color;
            private String href;
            private int sort;
            private String is_show;
            private String createtime;
            private String type;
            private String type_value;
            private int agent_id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getCat_id() {
                return cat_id;
            }

            public void setCat_id(int cat_id) {
                this.cat_id = cat_id;
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

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }

            public String getHref() {
                return href;
            }

            public void setHref(String href) {
                this.href = href;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public String getIs_show() {
                return is_show;
            }

            public void setIs_show(String is_show) {
                this.is_show = is_show;
            }

            public String getCreatetime() {
                return createtime;
            }

            public void setCreatetime(String createtime) {
                this.createtime = createtime;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getType_value() {
                return type_value;
            }

            public void setType_value(String type_value) {
                this.type_value = type_value;
            }

            public int getAgent_id() {
                return agent_id;
            }

            public void setAgent_id(int agent_id) {
                this.agent_id = agent_id;
            }
        }
    }
}
