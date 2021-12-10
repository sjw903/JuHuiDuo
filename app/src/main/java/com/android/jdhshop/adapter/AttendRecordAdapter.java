package com.android.jdhshop.adapter;

import android.content.Context;

import com.android.jdhshop.R;
import com.android.jdhshop.bean.AttendRecordBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class AttendRecordAdapter extends CommonAdapter<AttendRecordBean.Items> {
    public AttendRecordAdapter(Context context, int layoutId, List<AttendRecordBean.Items> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, AttendRecordBean.Items attendRecordBean, int position) {
        holder.setText(R.id.tv_one, "签到时间:"+attendRecordBean.getSign_time().split(" ")[1]);
        holder.setText(R.id.tv_two, "积分: +" + attendRecordBean.getPoint());
        holder.setText(R.id.tv_three, "日期:"+attendRecordBean.getSign_date());
        holder.setText(R.id.tv_four, "连续签到:"+attendRecordBean.getContinuous_day()+"天");
    }
}
