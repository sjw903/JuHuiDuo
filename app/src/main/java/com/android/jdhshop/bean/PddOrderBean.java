package com.android.jdhshop.bean;

import java.util.List;

public class PddOrderBean {
    private String id;
    private String custom_parameters;
    private String zs_duo_id;
    private String auth_duo_id;
    private String group_id;
    private String type;
    private String match_channel;
    private String order_modify_at;
    private String cps_sign;
    private String url_last_generate_time;
    private String point_time;
    private String return_status;
    private String pid;
    private String status;
    private String order_settle_time;
    private String order_verify_time;
    private String order_receive_time;
    private String order_group_success_time;
    private String order_pay_time;
    private String order_create_time;
    private String order_status_desc;
    private String order_status;
    private String batch_no;
    private String promotion_amount;
    private String promotion_rate;
    private String order_amount;
    private String goods_price;
    private String goods_quantity;
    private String goods_thumbnail_url;
    private String goods_name;
    private String goods_id;
    private String order_sn;
    private String user_id;
    private String goods_sign;
    private List<PddOrderBean> list;

    @Override
    public String toString() {
        return "PddOrderBean{" +
                "id='" + id + '\'' +
                ", custom_parameters='" + custom_parameters + '\'' +
                ", zs_duo_id='" + zs_duo_id + '\'' +
                ", auth_duo_id='" + auth_duo_id + '\'' +
                ", group_id='" + group_id + '\'' +
                ", type='" + type + '\'' +
                ", match_channel='" + match_channel + '\'' +
                ", order_modify_at='" + order_modify_at + '\'' +
                ", cps_sign='" + cps_sign + '\'' +
                ", url_last_generate_time='" + url_last_generate_time + '\'' +
                ", point_time='" + point_time + '\'' +
                ", return_status='" + return_status + '\'' +
                ", pid='" + pid + '\'' +
                ", status='" + status + '\'' +
                ", order_settle_time='" + order_settle_time + '\'' +
                ", order_verify_time='" + order_verify_time + '\'' +
                ", order_receive_time='" + order_receive_time + '\'' +
                ", order_group_success_time='" + order_group_success_time + '\'' +
                ", order_pay_time='" + order_pay_time + '\'' +
                ", order_create_time='" + order_create_time + '\'' +
                ", order_status_desc='" + order_status_desc + '\'' +
                ", order_status='" + order_status + '\'' +
                ", batch_no='" + batch_no + '\'' +
                ", promotion_amount='" + promotion_amount + '\'' +
                ", promotion_rate='" + promotion_rate + '\'' +
                ", order_amount='" + order_amount + '\'' +
                ", goods_price='" + goods_price + '\'' +
                ", goods_quantity='" + goods_quantity + '\'' +
                ", goods_thumbnail_url='" + goods_thumbnail_url + '\'' +
                ", goods_name='" + goods_name + '\'' +
                ", goods_id='" + goods_id + '\'' +
                ", order_sn='" + order_sn + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getGoods_sign() {
        return goods_sign;
    }

    public void setGoods_sign(String goods_sign) {
        this.id = goods_sign;
    }
    public String getCustom_parameters() {
        return custom_parameters;
    }

    public void setCustom_parameters(String custom_parameters) {
        this.custom_parameters = custom_parameters;
    }

    public String getZs_duo_id() {
        return zs_duo_id;
    }

    public void setZs_duo_id(String zs_duo_id) {
        this.zs_duo_id = zs_duo_id;
    }

    public String getAuth_duo_id() {
        return auth_duo_id;
    }

    public void setAuth_duo_id(String auth_duo_id) {
        this.auth_duo_id = auth_duo_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMatch_channel() {
        return match_channel;
    }

    public void setMatch_channel(String match_channel) {
        this.match_channel = match_channel;
    }

    public String getOrder_modify_at() {
        return order_modify_at;
    }

    public void setOrder_modify_at(String order_modify_at) {
        this.order_modify_at = order_modify_at;
    }

    public String getCps_sign() {
        return cps_sign;
    }

    public void setCps_sign(String cps_sign) {
        this.cps_sign = cps_sign;
    }

    public String getUrl_last_generate_time() {
        return url_last_generate_time;
    }

    public void setUrl_last_generate_time(String url_last_generate_time) {
        this.url_last_generate_time = url_last_generate_time;
    }

    public String getPoint_time() {
        return point_time;
    }

    public void setPoint_time(String point_time) {
        this.point_time = point_time;
    }

    public String getReturn_status() {
        return return_status;
    }

    public void setReturn_status(String return_status) {
        this.return_status = return_status;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrder_settle_time() {
        return order_settle_time;
    }

    public void setOrder_settle_time(String order_settle_time) {
        this.order_settle_time = order_settle_time;
    }

    public String getOrder_verify_time() {
        return order_verify_time;
    }

    public void setOrder_verify_time(String order_verify_time) {
        this.order_verify_time = order_verify_time;
    }

    public String getOrder_receive_time() {
        return order_receive_time;
    }

    public void setOrder_receive_time(String order_receive_time) {
        this.order_receive_time = order_receive_time;
    }

    public String getOrder_group_success_time() {
        return order_group_success_time;
    }

    public void setOrder_group_success_time(String order_group_success_time) {
        this.order_group_success_time = order_group_success_time;
    }

    public String getOrder_pay_time() {
        return order_pay_time;
    }

    public void setOrder_pay_time(String order_pay_time) {
        this.order_pay_time = order_pay_time;
    }

    public String getOrder_create_time() {
        return order_create_time;
    }

    public void setOrder_create_time(String order_create_time) {
        this.order_create_time = order_create_time;
    }

    public String getOrder_status_desc() {
        return order_status_desc;
    }

    public void setOrder_status_desc(String order_status_desc) {
        this.order_status_desc = order_status_desc;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public String getPromotion_amount() {
        return promotion_amount;
    }

    public void setPromotion_amount(String promotion_amount) {
        this.promotion_amount = promotion_amount;
    }

    public String getPromotion_rate() {
        return promotion_rate;
    }

    public void setPromotion_rate(String promotion_rate) {
        this.promotion_rate = promotion_rate;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public String getGoods_quantity() {
        return goods_quantity;
    }

    public void setGoods_quantity(String goods_quantity) {
        this.goods_quantity = goods_quantity;
    }

    public String getGoods_thumbnail_url() {
        return goods_thumbnail_url;
    }

    public void setGoods_thumbnail_url(String goods_thumbnail_url) {
        this.goods_thumbnail_url = goods_thumbnail_url;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<PddOrderBean> getList() {
        return list;
    }

    public void setList(List<PddOrderBean> list) {
        this.list = list;
    }
}
