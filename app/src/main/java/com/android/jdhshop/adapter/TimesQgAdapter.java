package com.android.jdhshop.adapter;

import android.content.Context;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.Calendar;
import java.util.List;

public class TimesQgAdapter extends CommonAdapter<String> {
    private int position=0;
    public TimesQgAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    protected void convert(ViewHolder holder, String s, int position) {
        holder.setText(R.id.txt_time,s);
        Calendar calendar=Calendar.getInstance();
        int HH=calendar.get(Calendar.HOUR_OF_DAY);
        String current=s.split(":")[0];
        if(current.startsWith("0")&&!current.startsWith("00"))
            current=current.replace("0","");
        int temp=Integer.valueOf(current);
        TextView status=holder.getView(R.id.txt_status);
        TextView time=holder.getView(R.id.txt_time);
            if ((HH - 2) >= temp) {
                holder.setText(R.id.txt_status, "已开抢");
                status.setTextColor(mContext.getResources().getColor(R.color.light1_gray));
                time.setTextColor(mContext.getResources().getColor(R.color.light1_gray));
            } else if (temp> HH) {
                holder.setText(R.id.txt_status, "即将开抢");
                status.setTextColor(mContext.getResources().getColor(R.color.light1_gray));
                time.setTextColor(mContext.getResources().getColor(R.color.light1_gray));
            } else {
                holder.setText(R.id.txt_status, "正在开抢");
                status.setTextColor(mContext.getResources().getColor(R.color.white));
                time.setTextColor(mContext.getResources().getColor(R.color.white));
                this.position = position;
            }
    }
}
