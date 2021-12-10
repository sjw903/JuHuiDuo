package com.android.jdhshop.activity;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.HaoDanBean;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.utils.VerticalImageSpan;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 开发者：陈飞
 * 时间:2018/7/26 14:34
 * 说明：子适配器
 */
public class TodayHighlightsChildAdapter extends CommonAdapter<HaoDanBean> {
    SpannableString spannableString;
    Drawable drawable;
    DecimalFormat df=new DecimalFormat("0.00");
    public TodayHighlightsChildAdapter(Context context, int layoutId, List<HaoDanBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, HaoDanBean item, int position) {
        //设置图片
        ImageView imageView = holder.getView(R.id.image);
        Glide.with(mContext).load(item.itempic+"_310x310.jpg").placeholder(R.drawable.no_banner).dontAnimate().into(imageView);
        spannableString = new SpannableString("    " + item.itemtitle);
        //设置是否天猫图片
        if ("B".equals(item.shoptype)) { //是天猫
            drawable = mContext.getResources().getDrawable(R.mipmap.label_tm);
//            holder.setImageResource(R.id.shop_image, R.mipmap.label_tm);
        } else {//不是天猫
            drawable = mContext.getResources().getDrawable(R.mipmap.label_tb);
//            holder.setImageResource(R.id.shop_image, R.mipmap.label_tb);
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        spannableString.setSpan(new VerticalImageSpan(drawable), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView)holder.getView(R.id.title_child)).setText(spannableString);
        //券后
        holder.setText(R.id.tx2, "¥" + item.itemendprice);
        //券后2
        TextView tx2_2 = holder.getView(R.id.tx2_2);
        //设置删除线
        tx2_2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tx2_2.setText("原价:¥" + item.itemprice);
        //内部
        TextView tx3 = holder.getView(R.id.tx3);
        tx3.setText(item.couponmoney + "元");
        holder.setText(R.id.tx4, "奖:"+df.format(Double.valueOf(item.tkmoney)* Double.parseDouble(df.format((float) SPUtils.getIntData(mContext, "rate", 0) / 100))));
        holder.setText(R.id.tx5, "已售:"+item.itemsale);
    }
}
