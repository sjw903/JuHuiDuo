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
import com.android.jdhshop.bean.TaobaoGuestBean;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.utils.StringUtils;
import com.android.jdhshop.utils.VerticalImageSpan;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.DecimalFormat;
import java.util.List;

public class NineAdapterList extends CommonAdapter<TaobaoGuestBean.TaobaoGuesChildtBean> {
    SpannableString spannableString;
    Drawable drawable;
    DecimalFormat df=new DecimalFormat("0.00");
    public NineAdapterList(Context context, int layoutId, List<TaobaoGuestBean.TaobaoGuesChildtBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final TaobaoGuestBean.TaobaoGuesChildtBean item, int position) {
        ImageView imageView = holder.getView(R.id.image);
        Glide.with(mContext).load(item.getPict_url()).placeholder(R.drawable.no_banner).override(CommonUtils.getScreenWidth()/2,180).dontAnimate().into(imageView);
        spannableString = new SpannableString("    " + item.getTitle());
        if ("1".equals(item.getUser_type())) { //是天猫
            drawable = mContext.getResources().getDrawable(R.mipmap.label_tm);
        } else {//不是天猫
            drawable = mContext.getResources().getDrawable(R.mipmap.label_tb);
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        spannableString.setSpan(new VerticalImageSpan(drawable), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView)holder.getView(R.id.title_child)).setText(spannableString);
     //券后2
        TextView tx2_2 = holder.getView(R.id.tx2_2);
        //设置删除线
        tx2_2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tx2_2.setText("¥" + item.getZk_final_price());
        //内部
        TextView tx3 = holder.getView(R.id.tx3);
        String temp2="0";
        if(!"".equals(item.getCoupon_info().toString())){
             temp2= org.apache.commons.lang3.StringUtils.substringBetween(item.getCoupon_info().toString(),"减","元");
                    tx3.setText(temp2+"元");
        }else{
            tx3.setText("0元");
        }
        //券后
        holder.setText(R.id.tx2, "¥" + String.format("%.2f", (StringUtils.doStringToDouble(item.getZk_final_price()) - StringUtils.doStringToDouble(temp2))));
        holder.setText(R.id.tx4, "奖:"+df.format((Double.valueOf(item.getZk_final_price())-Double.valueOf(temp2))*Double.valueOf(df.format(Double.valueOf(item.getCommission_rate())/10000))* Double.parseDouble(df.format((float) SPUtils.getIntData(mContext, "rate", 0) / 100))));
        int volumn=Integer.valueOf(item.getVolume());
        if(volumn<1000) {
            holder.setText(R.id.tx5, "已售:" + item.getVolume());
        }else{
            holder.setText(R.id.tx5,"已售:"+df.format((float)volumn/10000)+"万");
        }
    }
}
