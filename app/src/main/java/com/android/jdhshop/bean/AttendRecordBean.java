package com.android.jdhshop.bean;

import java.util.List;

public class AttendRecordBean {
    private List<Items> list;
    public class Items{
        private String id;//	ID
        private String  user_id;//	用户ID
        private String  sign_date;//	签到日期
        private String  sign_time;//	签到时间
        private String   point;//	签到获取积分数
        private String   continuous_day;//连续签到天数

        @Override
        public String toString() {
            return "Items{" +
                    "id='" + id + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", sign_date='" + sign_date + '\'' +
                    ", sign_time='" + sign_time + '\'' +
                    ", point='" + point + '\'' +
                    ", continuous_day='" + continuous_day + '\'' +
                    '}';
        }

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

        public String getSign_date() {
            return sign_date;
        }

        public void setSign_date(String sign_date) {
            this.sign_date = sign_date;
        }

        public String getSign_time() {
            return sign_time;
        }

        public void setSign_time(String sign_time) {
            this.sign_time = sign_time;
        }

        public String getPoint() {
            return point;
        }

        public void setPoint(String point) {
            this.point = point;
        }

        public String getContinuous_day() {
            return continuous_day;
        }

        public void setContinuous_day(String continuous_day) {
            this.continuous_day = continuous_day;
        }
    }
    public List<Items> getList() {
        return list;
    }

    public void setList(List<Items> list) {
        this.list = list;
    }
}
