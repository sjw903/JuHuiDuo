package com.android.jdhshop.adapter;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.ShuangshiyiBean;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.utils.VerticalImageSpan;

import java.text.DecimalFormat;
import java.util.List;

public class ShuangshiyiAdapter extends BaseQuickAdapter<ShuangshiyiBean.Item,BaseViewHolder> {
    DecimalFormat df=new DecimalFormat("0.00");
    SpannableString spannableString;
    Drawable drawable;
    public ShuangshiyiAdapter(int layoutResId, @Nullable List<ShuangshiyiBean.Item> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ShuangshiyiBean.Item item) {
        ImageView imageView = holder.getView(R.id.image);
        Glide.with(mContext).load("http://"+item.pict_url).placeholder(R.drawable.no_banner).override(CommonUtils.getScreenWidth()/2,180).dontAnimate().into(imageView);
        spannableString = new SpannableString("    " + item.title);
            drawable = mContext.getResources().getDrawable(R.mipmap.icon_tmall);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        spannableString.setSpan(new VerticalImageSpan(drawable), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView)holder.getView(R.id.title_child)).setText(spannableString);
        //券后2
        TextView tx2_2 = holder.getView(R.id.tx2_2);
        //设置删除线
        tx2_2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tx2_2.setText(item.zk_final_price);
        //内部
        holder.setText(R.id.txt_desc,item.presale_discount_fee_text);
        if(item.presale_discount_fee_text==null||"".equals(item.presale_discount_fee_text)){
            holder.getView(R.id.txt_desc).setVisibility(View.GONE);
        }else{
            holder.getView(R.id.txt_desc).setVisibility(View.VISIBLE);
        }
        TextView tx3 = holder.getView(R.id.tx3);
        tx3.setText(item.coupon_amount);
        //券后
        holder.setText(R.id.tx2, "¥" +String.format("%.2f",(Double.valueOf(item.zk_final_price)-Double.valueOf(item.coupon_amount))));
        holder.setText(R.id.tx4, "奖:"+df.format((Double.valueOf(item.zk_final_price)-Double.valueOf(item.coupon_amount))*(Double.valueOf(item.commission_rate)/ 100)*Double.parseDouble(df.format((float) SPUtils.getIntData(mContext, "rate", 0) / 100))));
        holder.setText(R.id.tx5,"已售"+item.volume);
    }
}
