package com.android.jdhshop.bean;

import java.io.Serializable;
import java.util.List;

public class SuCaiBean implements Serializable {
    private List<SuCaiBean> list;
    private String id;
    private String uid;
    private String board_id;
    private String title;
    private String keyword;
    private String description;
    private String img;
    private String mob_text;
    private String mob_img;
    private String clicknum;
    private String pubtime;
    private String share_num;
    private String nickname;
    private String avatar;

    public List<SuCaiBean> getList() {
        return list;
    }

    public void setList(List<SuCaiBean> list) {
        this.list = list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBoard_id() {
        return board_id;
    }

    public void setBoard_id(String board_id) {
        this.board_id = board_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMob_text() {
        return mob_text;
    }

    public void setMob_text(String mob_text) {
        this.mob_text = mob_text;
    }

    public String getMob_img() {
        return mob_img;
    }

    public void setMob_img(String mob_img) {
        this.mob_img = mob_img;
    }

    public String getClicknum() {
        return clicknum;
    }

    public void setClicknum(String clicknum) {
        this.clicknum = clicknum;
    }

    public String getPubtime() {
        return pubtime;
    }

    public void setPubtime(String pubtime) {
        this.pubtime = pubtime;
    }

    public String getShare_num() {
        return share_num;
    }

    public void setShare_num(String share_num) {
        this.share_num = share_num;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
