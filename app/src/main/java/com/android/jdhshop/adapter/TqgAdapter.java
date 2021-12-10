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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.PromotionDetailsActivity;
import com.android.jdhshop.bean.TaobaoGuestBean;
import com.android.jdhshop.utils.StringUtils;
import com.android.jdhshop.utils.VerticalImageSpan;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.NumberFormat;
import java.util.List;

/**
 * 开发者：陈飞
 * 时间:2018/7/26 15:54
 * 说明：
 */
public class TqgAdapter extends CommonAdapter<TaobaoGuestBean.TaobaoGuesChildtBean> {
    SpannableString spannableString;
    Drawable drawable;
    NumberFormat numberFormat = NumberFormat.getInstance();
    public TqgAdapter(Context context, int layoutId, List<TaobaoGuestBean.TaobaoGuesChildtBean> datas) {
        super(context, layoutId, datas);
        numberFormat.setMaximumFractionDigits(2);
    }

    @Override
    protected void convert(ViewHolder holder, final TaobaoGuestBean.TaobaoGuesChildtBean item, int position) {
        //设置图片
        ImageView imageView = holder.getView(R.id.image);
        Glide.with(mContext).load(item.getPic_url()).dontAnimate().into(imageView);

        spannableString = new SpannableString("    " + item.getTitle());
        //设置是否天猫图片
        if (item.isIs_tmall()) { //是天猫
            drawable = mContext.getResources().getDrawable(R.mipmap.label_tm);
//            holder.setImageResource(R.id.shop_image, R.mipmap.label_tm);
        } else {//不是天猫
            drawable = mContext.getResources().getDrawable(R.mipmap.label_tb);
//            holder.setImageResource(R.id.shop_image, R.mipmap.label_tb);
        }
        ProgressBar progressBar=holder.getView(R.id.pb_progressbar);
        progressBar.setMax(Integer.valueOf(item.getTotal_amount()));
        progressBar.setProgress(Integer.valueOf(item.getSold_num()));
        holder.setText(R.id.txt_percent,numberFormat.format((float) Integer.valueOf(item.getSold_num()) / (float) Integer.valueOf(item.getTotal_amount()) * 100)+"%");
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        spannableString.setSpan(new VerticalImageSpan(drawable), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView)holder.getView(R.id.title_child)).setText(spannableString);
        holder.setText(R.id.tx2, "¥" + item.getZk_final_price());
        TextView tx2_2 = holder.getView(R.id.tx2_2);
        //设置删除线
        tx2_2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tx2_2.setText("原价:¥" + item.getReserve_price());
        //内部
        TextView tx3 = holder.getView(R.id.tx3);
        tx3.setText(item.getCoupon_amount() + "元");
        holder.setText(R.id.tx4, "奖:"+String.format("%.2f", item.getCommission()) + "元");
        holder.setText(R.id.tx5, "已售:"+item.getSold_num()+"件");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("num_iid", item.getNum_iid());
                Intent intent = new Intent(mContext, PromotionDetailsActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }
}
