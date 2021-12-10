package com.android.jdhshop.bean;

import java.util.List;

/**
 * 开发者：陈飞
 * 时间:2018/7/22 09:46
 * 说明：
 */
public class BalanceRecordListBean {

    private List<BalanceRecordListChildBean> list;

    public List<BalanceRecordListChildBean> getList() {
        return list;
    }

    public void setList(List<BalanceRecordListChildBean> list) {
        this.list = list;
    }

    public static class BalanceRecordListChildBean {
        private String id;//ID
        private String order_num;//订单号
        private String user_id;//用户ID
        private String status;//状态 1未支付 2已支付
        private String create_time;//创建时间
        private String money;//变动金额
        private String pay_time;//支付时间
        private String action;//操作类型 tbk 淘宝客返利
        private String action_zh;//操作类型中文描述
        private String action_symbol;//操作类型符号
        private String all_money;

        public String getAll_money() {
            return all_money;
        }

        public void setAll_money(String all_money) {
            this.all_money = all_money;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrder_num() {
            return order_num;
        }

        public void setOrder_num(String order_num) {
            this.order_num = order_num;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getPay_time() {
            return pay_time;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getAction_zh() {
            return action_zh;
        }

        public void setAction_zh(String action_zh) {
            this.action_zh = action_zh;
        }

        public String getAction_symbol() {
            return action_symbol;
        }

        @Override
        public String toString() {
            return "BalanceRecordListChildBean{" +
                    "id='" + id + '\'' +
                    ", order_num='" + order_num + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", status='" + status + '\'' +
                    ", create_time='" + create_time + '\'' +
                    ", money='" + money + '\'' +
                    ", pay_time='" + pay_time + '\'' +
                    ", action='" + action + '\'' +
                    ", action_zh='" + action_zh + '\'' +
                    ", action_symbol='" + action_symbol + '\'' +
                    ", all_money='" + all_money + '\'' +
                    '}';
        }

        public void setAction_symbol(String action_symbol) {
            this.action_symbol = action_symbol;
        }
    }
}
