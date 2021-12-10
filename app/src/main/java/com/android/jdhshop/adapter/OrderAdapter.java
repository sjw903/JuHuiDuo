package com.android.jdhshop.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.bean.OrderGuestBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yohn on 2018/7/28.
 */

public class OrderAdapter extends BaseAdapter {
    private Context context;
    private List<OrderGuestBean.OrderBean> dataList = new ArrayList<>();

    public OrderAdapter(Context context) {
        this.context = context;
    }
    public void setData(List<OrderGuestBean.OrderBean> dataList) {
        if (dataList == null) {
            this.dataList = new ArrayList<>();
        } else {
            this.dataList = dataList;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order, null);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            holder.tv_stu = (TextView) convertView.findViewById(R.id.tv_stu);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        OrderGuestBean.OrderBean model=dataList.get(position);
        if(!TextUtils.isEmpty(model.getOrder_num())){
            holder.tv_num.setText("订单编号："+model.getOrder_num());
        }else{
            holder.tv_num.setText("订单编号：");
        }

        if("3".equalsIgnoreCase(model.getStatus())){//状态 1待审核 2审核不通过 3已返利
            holder.tv_stu.setText("交易成功");
        }else if("1".equalsIgnoreCase(model.getStatus())){
            holder.tv_stu.setText("交易成功");
        }else{
            holder.tv_stu.setText("交易失败");
        }

        if(!TextUtils.isEmpty(model.getMoney())){
            holder.tv_price.setText("奖："+model.getMoney()+"元");
        }else{
            holder.tv_price.setText("奖：");
        }

        if(!TextUtils.isEmpty(model.getCheck_time())){
            holder.tv_date.setText("返现时间："+model.getCheck_time());
        }else{
            holder.tv_date.setText("返现时间：");
        }

        return convertView;
    }
    // 声明缓存类
    class ViewHolder {
        TextView tv_price;//是否认证
        TextView tv_date;//值
        TextView tv_num;//门头名称
        TextView tv_stu;//门头名称
    }
}
