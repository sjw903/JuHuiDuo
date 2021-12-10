package com.android.jdhshop.adapter;

import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.Wphbean;
import com.android.jdhshop.common.SPUtils;

import java.text.DecimalFormat;
import java.util.List;

public class WphAdatper extends BaseQuickAdapter<Wphbean, BaseViewHolder> {
    DecimalFormat df=new DecimalFormat("0.00");
    public WphAdatper(int layoutResId, @Nullable List<Wphbean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Wphbean item) {
        //设置图片
        ImageView imageView = holder.getView(R.id.image);
        Glide.with(mContext).load(item.goodsThumbUrl).placeholder(R.drawable.no_banner).dontAnimate().into(imageView);
        ((TextView)holder.getView(R.id.title_child)).setText(item.goodsName);
        //券后
        holder.setText(R.id.tx2, "¥" + df.format(Double.valueOf(item.vipPrice)));
        //券后2
        TextView tx2_2 = holder.getView(R.id.tx2_2);
        //设置删除线
        tx2_2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tx2_2.setText( item.marketPrice);
//        TextView tvhehe = holder.getView( R.id.hehejiage );
//        tvhehe.setText("升级分享最高可赚:¥" + df.format((Double.valueOf(item.zk_final_price)- Double.parseDouble(item.coupon_amount))*Double.parseDouble( item.tk_rate )/100* Double.parseDouble(df.format((float) SPUtils.getIntData(mContext, "rate", 0) / 100))) );
        //内部
        TextView tx3 = holder.getView(R.id.tx3);
        tx3.setText(df.format((Double.parseDouble(item.discount))*Double.valueOf(item.vipPrice)) );
        tx3.setVisibility(View.GONE);
        holder.setText(R.id.tx4, "预估赚:"+df.format(Double.parseDouble( item.commission )* Double.parseDouble(df.format((float) SPUtils.getIntData(mContext, "rate", 0) / 100))));
//        holder.setText(R.id.tx5, "已售:"+item.volume);
    }
}
