package com.android.jdhshop.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.PromotionDetailsActivity;
import com.android.jdhshop.bean.OrderGuestNewBean;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.my.MyOrderActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yohn on 2018/9/19.
 */

public class OrderNewAdapter extends BaseAdapter {
    private Context context;
    DecimalFormat df = new DecimalFormat("0.00");
    private List<OrderGuestNewBean.OrderBean> dataList = new ArrayList<>();

    public OrderNewAdapter(Context context) {
        this.context = context;
    }
    public void setData(List<OrderGuestNewBean.OrderBean> dataList) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate( R.layout.item_order_new, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_stu = (TextView) convertView.findViewById(R.id.tv_stu);
            holder.title_child = (TextView) convertView.findViewById(R.id.title_child);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_one = (TextView) convertView.findViewById(R.id.tv_one);
            holder.tv_two = (TextView) convertView.findViewById(R.id.tv_two);
            holder.txt_code = (TextView) convertView.findViewById(R.id.order_num);
            holder.txt_copy1 = (TextView) convertView.findViewById(R.id.txt_copy1);
            holder.tv_three = (TextView) convertView.findViewById(R.id.tv_three);
            holder.shop_image = (ImageView) convertView.findViewById(R.id.shop_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final OrderGuestNewBean.OrderBean model=dataList.get(position);
        holder.tv_name.setText( model.getSeller_shop_title() );
//        淘客订单状态，3：订单结算，12：订单付款， 13：订单失效，14：订单成功
        if("3".equalsIgnoreCase(  model.getTk_status())){
            holder.tv_stu.setText( "已结算" );
        }else if("12".equalsIgnoreCase(  model.getTk_status())){
            holder.tv_stu.setText( "已付款" );
        }else if("13".equalsIgnoreCase(  model.getTk_status())){
            holder.tv_stu.setText( "失效" );
        }else if("14".equalsIgnoreCase(  model.getTk_status())){
            holder.tv_stu.setText( "订单成功" );
        }
        holder.txt_code.setText("订单号:"+model.getTrade_id());
        holder.title_child.setText( model.getItem_title() );
        holder.tv_date.setText( model.getCreate_time() );
        holder.tv_one.setText(df.format(Float.valueOf(model.getCommission()) / 100) );
        holder.tv_two.setText( model.getAlipay_total_price());
        if(!TextUtils.isEmpty( model.getCommission_rate()  )){
            StringBuffer sb=new StringBuffer( );
            sb.append(  model.getCommission_rate() ).append( "%" );
            holder.tv_three.setText( sb.toString());
        }else{
            holder.tv_three.setText( "0%");
        }
//            holder.tv_three.setVisibility(View.GONE);

        if(model.getGoods_img()!=null&&model.getGoods_img().startsWith( "http" )){
            Glide.with(context).load(model.getGoods_img()).dontAnimate().into(holder.shop_image);
        }else{
            Glide.with(context).load(Constants.APP_IP + model.getGoods_img()).placeholder(R.drawable.no_banner).dontAnimate().into(holder.shop_image);
        }
        holder.txt_copy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(model.getTrade_id());
                T.showShort(context, "订单号已复制");
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("num_iid", dataList.get(position).getNum_iid());
                context.startActivity(new Intent(context,PromotionDetailsActivity.class).putExtras(bundle));
            }
        });
        return convertView;
    }

    // 声明缓存类
   static class ViewHolder {
        TextView tv_name,tv_date,title_child,tv_stu,tv_one,tv_two,tv_three,txt_code,txt_copy1;//值
        ImageView shop_image;//
    }
}
