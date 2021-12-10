package com.android.jdhshop.bean;

/**
 * Created by yohn on 2018/10/25.
 */

public class PayBean {
    private boolean isChecked; //每条item的状态
    public String id;//	会员升级产品ID
    public String title;//	标题
    public int type;//1 支付宝支付  2 微信支付
    public boolean isChecked() {
        return isChecked;
    }
    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
