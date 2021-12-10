package com.android.jdhshop.adapter;

import android.content.Context;

import com.android.jdhshop.R;
import com.android.jdhshop.bean.BalanceRecordListBean;
import com.android.jdhshop.utils.DateUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * 提现记录
 * Created by yohn on 2018/7/14.
 */

public class BalanceRecordAdapter extends CommonAdapter<BalanceRecordListBean.BalanceRecordListChildBean> {
    public BalanceRecordAdapter(Context context, int layoutId, List<BalanceRecordListBean.BalanceRecordListChildBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, BalanceRecordListBean.BalanceRecordListChildBean item, int position) {
        viewHolder.setText(R.id.tv_one, item.getAction_zh());
        String actMoney = "";
        switch (item.getAction()) {
            case "recharge": //增加
                actMoney = "+";
                break;
            case "draw": //减少
                actMoney = "-";
                break;
        }
        viewHolder.setText(R.id.tv_two, item.getAction_symbol() + item.getMoney() + "元");
        viewHolder.setText(R.id.tv_three, DateUtils.format_yyyy_MMstr(item.getPay_time()));
        viewHolder.setText(R.id.tv_four, "余额：" + item.getAll_money());
    }
}
