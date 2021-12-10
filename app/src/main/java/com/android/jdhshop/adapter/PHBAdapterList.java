package com.android.jdhshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.PromotionDetailsActivity;
import com.android.jdhshop.bean.PhbBean;
import com.android.jdhshop.bean.TaobaoGuestBean;
import com.android.jdhshop.utils.StringUtils;
import com.android.jdhshop.utils.VerticalImageSpan;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.DecimalFormat;
import java.util.List;

public class PHBAdapterList extends CommonAdapter<PhbBean> {
    SpannableString spannableString;
    Drawable drawable;
    DecimalFormat df=new DecimalFormat("0.00");
    public PHBAdapterList(Context context, int layoutId, List<PhbBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final PhbBean item, int position) {
        ImageView imageView = holder.getView(R.id.image);
        Glide.with(mContext).load(item.getPict_url()).placeholder(R.drawable.no_banner).dontAnimate().into(imageView);
        spannableString = new SpannableString("    " + item.getTitle());
        if ("1".equals(item.getUser_type())) { //是天猫
            drawable = mContext.getResources().getDrawable(R.mipmap.label_tm);
        } else {//不是天猫
            drawable = mContext.getResources().getDrawable(R.mipmap.label_tb);
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        spannableString.setSpan(new VerticalImageSpan(drawable), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView)holder.getView(R.id.title_child)).setText(spannableString);
        //券后
        holder.setText(R.id.tx2, "¥" + String.format("%.2f", (StringUtils.doStringToDouble(item.getZk_final_price()) - StringUtils.doStringToDouble(item.getCoupon_amount()))));
        //券后2
        TextView tx2_2 = holder.getView(R.id.tx2_2);
        //设置删除线
        tx2_2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tx2_2.setText("¥" + item.getZk_final_price());
        //内部
        TextView tx3 = holder.getView(R.id.tx3);
        tx3.setText(item.getCoupon_amount());
        holder.setText(R.id.tx4, "奖:"+String.format("%.2f", item.getCommission()));
        int volumn=Integer.valueOf(item.getVolume());
        if(volumn<1000) {
            holder.setText(R.id.tx5, "已售:" + item.getVolume());
        }else{
            holder.setText(R.id.tx5,"已售:"+df.format((float)volumn/10000)+"万");
        }

    }
}
