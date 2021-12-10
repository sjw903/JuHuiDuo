package com.android.jdhshop.bean;

import java.util.List;

/**
 * Created by yohn on 2018/10/25.
 */

public class FeeBean {
    public List<FeesBean> list;//	费用配置列表
    public static class FeesBean{
        private boolean isChecked; //每条item的状态
        public String id;//	会员升级产品ID
        public String title;//	标题
        public String fee;//	升级费用
        public boolean isChecked() {
            return isChecked;
        }
        public void setChecked(boolean checked) {
            isChecked = checked;
        }
    }

}
