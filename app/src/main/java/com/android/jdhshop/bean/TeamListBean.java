package com.android.jdhshop.bean;

import java.util.List;

public class TeamListBean {
    private String referrer_num;//推荐总人数
    private String inviter;//推荐人姓名
    private String team_first;//第一市场人数
    private String team_second;//第二市场人数
    private List<Teamlist> teamlist1;//第一市场团队列表
    private List<Teamlist> teamlist2;//第二市场团队列表
    public List<Teamlist> teamlist;
    public static class Teamlist{
        private String uid;//用户id
        private String name;//姓名
        private String avatar;//头像
        private String group_name;//会员组名称
        private String referrer_num;//邀请人数
        private String referrer_name;//推荐人姓名
        private String register_time;//注册时间
        public String remark;
        private String group_id;
        private String is_buy;
        public String getUid() {
            return uid;
        }

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }

        public String getIs_buy() {
            return is_buy;
        }

        public void setIs_buy(String is_buy) {
            this.is_buy = is_buy;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public String getRegister_time() {
            return register_time;
        }

        public void setRegister_time(String register_time) {
            this.register_time = register_time;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public String getReferrer_num() {
            return referrer_num;
        }

        public void setReferrer_num(String referrer_num) {
            this.referrer_num = referrer_num;
        }

        public String getReferrer_name() {
            return referrer_name;
        }

        public void setReferrer_name(String referrer_name) {
            this.referrer_name = referrer_name;
        }
    }


    public String getReferrer_num() {
        int team_first = 0;
            team_first = Integer.parseInt(this.team_first);
        int team_second = 0;
            team_second = Integer.parseInt(this.team_second);
        return String.valueOf(team_first+team_second);
    }

    public void setReferrer_num(String referrer_num) {
        this.referrer_num = referrer_num;
    }

    public String getEferrer_name() {
        return inviter;
    }

    public void setEferrer_name(String eferrer_name) {
        this.inviter = eferrer_name;
    }

    public String getTeam_first() {
        return team_first;
    }

    public void setTeam_first(String team_first) {
        this.team_first = team_first;
    }

    public String getTeam_second() {
        return team_second;
    }

    public void setTeam_second(String team_second) {
        this.team_second = team_second;
    }

    public String getInviter() {
        return inviter;
    }

    public void setInviter(String inviter) {
        this.inviter = inviter;
    }

    public List<Teamlist> getTeamlist1() {
        return teamlist1;
    }

    public void setTeamlist1(List<Teamlist> teamlist1) {
        this.teamlist1 = teamlist1;
    }

    public List<Teamlist> getTeamlist2() {
        return teamlist2;
    }

    public void setTeamlist2(List<Teamlist> teamlist2) {
        this.teamlist2 = teamlist2;
    }
}
