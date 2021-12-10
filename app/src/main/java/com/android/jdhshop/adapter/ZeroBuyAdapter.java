package com.android.jdhshop.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.ZeroBuyBean;
import com.android.jdhshop.utils.StringUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class ZeroBuyAdapter extends BaseQuickAdapter<ZeroBuyBean.Item,BaseViewHolder> {

    public ZeroBuyAdapter(int layoutResId, @Nullable List<ZeroBuyBean.Item> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ZeroBuyBean.Item zeroBuyBean) {
        ImageView imageView = holder.getView(R.id.image);
        Glide.with(mContext).load(zeroBuyBean.pict_url).placeholder(R.drawable.no_banner).dontAnimate().into(imageView);
        ((TextView)holder.getView(R.id.title_child)).setText(zeroBuyBean.goods_name);
        //券后
        holder.setText(R.id.tx2, String.format("%.2f", (StringUtils.doStringToDouble(zeroBuyBean.zk_final_price) - StringUtils.doStringToDouble(zeroBuyBean.coupon_amount))));
        //券后2
        TextView tx2_2 = holder.getView(R.id.tx2_2);
        //设置删除线
        tx2_2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tx2_2.setText( zeroBuyBean.zk_final_price);
        TextView tx3 = holder.getView(R.id.tx3);
        tx3.setText(zeroBuyBean.coupon_amount);
        holder.setText(R.id.tx4, "补贴¥"+zeroBuyBean.subsidy_amount);
    }
}
