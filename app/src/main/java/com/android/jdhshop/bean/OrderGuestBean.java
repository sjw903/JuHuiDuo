package com.android.jdhshop.bean;

import java.util.List;

/**
 * Created by yohn on 2018/7/28.
 */

public class OrderGuestBean {
    private List<OrderBean> list;

    public List<OrderBean> getList() {
        return list;
    }

    public void setList(List<OrderBean> list) {
        this.list = list;
    }
    public static class OrderBean{
        private String id;//	ID
        private String user_id;//	用户ID
        private String  order_num;//	淘宝订单号
        private String money;//	奖
        private String apply_time;//	申请时间
        private String is_check;//	是否审核 Y已审核 N未审核
        private String check_result;//	审核结果 Y通过 N不通过
        private String  check_time;//	审核时间
        private String admin_id;//	审核管理员ID
        private String status;//	状态 1待审核 2审核不通过 3已返利

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getOrder_num() {
            return order_num;
        }

        public void setOrder_num(String order_num) {
            this.order_num = order_num;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getApply_time() {
            return apply_time;
        }

        public void setApply_time(String apply_time) {
            this.apply_time = apply_time;
        }

        public String getIs_check() {
            return is_check;
        }

        public void setIs_check(String is_check) {
            this.is_check = is_check;
        }

        public String getCheck_result() {
            return check_result;
        }

        public void setCheck_result(String check_result) {
            this.check_result = check_result;
        }

        public String getCheck_time() {
            return check_time;
        }

        public void setCheck_time(String check_time) {
            this.check_time = check_time;
        }

        public String getAdmin_id() {
            return admin_id;
        }

        public void setAdmin_id(String admin_id) {
            this.admin_id = admin_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
