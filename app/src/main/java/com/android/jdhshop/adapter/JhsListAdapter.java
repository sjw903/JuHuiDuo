package com.android.jdhshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.PromotionDetailsActivity;
import com.android.jdhshop.bean.JhsListbean;
import com.android.jdhshop.bean.PDDBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 开发者：wmm
 * 时间:2018/11/29 10:10
 * 说明：
 */
public class JhsListAdapter extends CommonAdapter<JhsListbean> {
    DecimalFormat df=new DecimalFormat("0.00");
    public JhsListAdapter(Context context, int layoutId, List<JhsListbean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final JhsListbean item, int position) {
        //设置图片
        ImageView imageView = holder.getView(R.id.image);
        Glide.with(mContext).load(item.getPict_url()).dontAnimate().into(imageView);
        ((TextView)holder.getView(R.id.title_child)).setText(item.getTitle());
        holder.setText(R.id.tx2, item.getJdd_num()+"人拼团");
        TextView tx2_2 = holder.getView(R.id.tx2_2);
        tx2_2.setText("¥:"+item.getJdd_price());
        TextView tx3 = holder.getView(R.id.tx3);
        tx3.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tx3.setText("原价:"+df.format(Float.valueOf(item.getOrig_price())) + "元");
        holder.setText(R.id.tx4, "奖:"+df.format(Float.valueOf(item.getCommission()==null?"0":item.getCommission()))+ "元");
        holder.setText(R.id.tx5, "已售:"+item.getSell_num());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("num_iid",item.getItem_id());
                Intent intent = new Intent(mContext, PromotionDetailsActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }
}
