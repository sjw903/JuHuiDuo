package com.android.jdhshop.bean;

public class LinShiAIIDBean {

    /**
     * code : 0
     * msg : 成功
     * data : {"tmp_id":100014,"group_title":"京东/拼多多内购优惠群100014","wx_service_user":"juduohui888"}
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
        /**
         * tmp_id : 100014
         * group_title : 京东/拼多多内购优惠群100014
         * wx_service_user : juduohui888
         */

        private int tmp_id;
        private String group_title;
        private String wx_service_user;

        public int getTmp_id() {
            return tmp_id;
        }

        public void setTmp_id(int tmp_id) {
            this.tmp_id = tmp_id;
        }

        public String getGroup_title() {
            return group_title;
        }

        public void setGroup_title(String group_title) {
            this.group_title = group_title;
        }

        public String getWx_service_user() {
            return wx_service_user;
        }

        public void setWx_service_user(String wx_service_user) {
            this.wx_service_user = wx_service_user;
        }
    }
}
