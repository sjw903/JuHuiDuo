package com.android.jdhshop.bean;

import java.util.List;

/**
 * Created by yohn on 2018/9/19.
 */

public class OrderGuestNewBean {
    private List<OrderBean> list;

    public List<OrderBean> getList() {
        return list;
    }

    public void setList(List<OrderBean> list) {
        this.list = list;
    }
    public static class OrderBean{
        private String trade_id;//淘宝订单号
        private String item_title;//商品标题
        private String num_iid;
        private String item_num;//商品数量
        private String seller_shop_title;//卖家店铺名称
        private String commission;//推广者获得的收入金额，对应联盟后台报表“预估收入”
        private String commission_rate;//推广者获得的分成比率，对应联盟后台报表“分成比率”
        private String create_time;//淘客订单创建时间
        private String earning_time;//淘客订单结算时间
        private String tk_status;//淘客订单状态，3：订单结算，12：订单付款， 13：订单失效，14：订单成功
        private String order_type;//订单类型，如天猫，淘宝
        private String alipay_total_price;//付款金额
        private String  total_commission_rate;//奖比率
        private String total_commission_fee;//奖金额
        private String item_img;//	商品主图

        public String getNum_iid() {
            return num_iid;
        }

        public void setNum_iid(String num_iid) {
            this.num_iid = num_iid;
        }

        public String getTrade_id() {
            return trade_id;
        }

        public void setTrade_id(String trade_id) {
            this.trade_id = trade_id;
        }

        public String getItem_title() {
            return item_title;
        }

        public void setItem_title(String item_title) {
            this.item_title = item_title;
        }

        public String getItem_num() {
            return item_num;
        }

        public void setItem_num(String item_num) {
            this.item_num = item_num;
        }

        public String getSeller_shop_title() {
            return seller_shop_title;
        }

        public void setSeller_shop_title(String seller_shop_title) {
            this.seller_shop_title = seller_shop_title;
        }

        public String getCommission() {
            return commission;
        }

        public void setCommission(String commission) {
            this.commission = commission;
        }

        public String getCommission_rate() {
            return commission_rate;
        }

        public void setCommission_rate(String commission_rate) {
            this.commission_rate = commission_rate;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getEarning_time() {
            return earning_time;
        }

        public void setEarning_time(String earning_time) {
            this.earning_time = earning_time;
        }

        public String getTk_status() {
            return tk_status;
        }

        public void setTk_status(String tk_status) {
            this.tk_status = tk_status;
        }

        public String getOrder_type() {
            return order_type;
        }

        public void setOrder_type(String order_type) {
            this.order_type = order_type;
        }

        public String getAlipay_total_price() {
            return alipay_total_price;
        }

        public void setAlipay_total_price(String alipay_total_price) {
            this.alipay_total_price = alipay_total_price;
        }

        public String getTotal_commission_rate() {
            return total_commission_rate;
        }

        public void setTotal_commission_rate(String total_commission_rate) {
            this.total_commission_rate = total_commission_rate;
        }

        public String getTotal_commission_fee() {
            return total_commission_fee;
        }

        public void setTotal_commission_fee(String total_commission_fee) {
            this.total_commission_fee = total_commission_fee;
        }

        @Override
        public String toString() {
            return "OrderBean{" +
                    "trade_id='" + trade_id + '\'' +
                    ", item_title='" + item_title + '\'' +
                    ", item_num='" + item_num + '\'' +
                    ", seller_shop_title='" + seller_shop_title + '\'' +
                    ", commission='" + commission + '\'' +
                    ", commission_rate='" + commission_rate + '\'' +
                    ", create_time='" + create_time + '\'' +
                    ", earning_time='" + earning_time + '\'' +
                    ", tk_status='" + tk_status + '\'' +
                    ", order_type='" + order_type + '\'' +
                    ", alipay_total_price='" + alipay_total_price + '\'' +
                    ", total_commission_rate='" + total_commission_rate + '\'' +
                    ", total_commission_fee='" + total_commission_fee + '\'' +
                    ", goods_img='" + item_img + '\'' +
                    '}';
        }

        public String getGoods_img() {
            return item_img;
        }

        public void setGoods_img(String goods_img) {
            this.item_img = goods_img;
        }
    }
}
