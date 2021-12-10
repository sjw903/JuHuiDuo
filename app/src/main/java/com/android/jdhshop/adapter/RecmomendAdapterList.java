package com.android.jdhshop.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.Recommendbean;
import com.android.jdhshop.utils.StringUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.DecimalFormat;
import java.util.List;

public class RecmomendAdapterList extends CommonAdapter<Recommendbean> {
    SpannableString spannableString;
    Drawable drawable;
    DecimalFormat df=new DecimalFormat("0.00");
    public RecmomendAdapterList(Context context, int layoutId, List<Recommendbean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final Recommendbean item, int position) {
        ImageView imageView = holder.getView(R.id.image);
        Glide.with(mContext).load(item.getPict_url()).placeholder(R.drawable.no_banner).dontAnimate().into(imageView);
        ((TextView)holder.getView(R.id.title_child)).setText(item.getTitle());
        //券后
        holder.setText(R.id.tx2, "¥" + String.format("%.2f", (StringUtils.doStringToDouble(item.getZk_final_price()) - StringUtils.doStringToDouble(item.getCoupon_amount()))));
        //券后2
        TextView tx2_2 = holder.getView(R.id.tx2_2);
        //设置删除线
        tx2_2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tx2_2.setText( item.getZk_final_price());
    }
}
