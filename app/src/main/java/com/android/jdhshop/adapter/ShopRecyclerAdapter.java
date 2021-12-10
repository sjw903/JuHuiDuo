package com.android.jdhshop.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.TaobaoGuestBean;
import com.android.jdhshop.utils.StringUtils;
import com.android.jdhshop.utils.VerticalImageSpan;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 开发者：陈飞
 * 时间:2018/7/26 15:54
 * 说明：
 */
public class ShopRecyclerAdapter extends CommonAdapter<TaobaoGuestBean.TaobaoGuesChildtBean> {
    SpannableString spannableString;
    Drawable drawable;
    private OnDeleteClickLister mDeleteClickListener;
    public ShopRecyclerAdapter(Context context, int layoutId, List<TaobaoGuestBean.TaobaoGuesChildtBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, TaobaoGuestBean.TaobaoGuesChildtBean item, int position) {
        //设置图片
        ImageView imageView = holder.getView(R.id.image);
        Glide.with(mContext).load(item.getPict_url()).placeholder(R.mipmap.icon_defult_boy).dontAnimate().into(imageView);

        spannableString = new SpannableString("    " + item.getTitle());
        //设置是否天猫图片
        if ("1".equals(item.getUser_type())) { //是天猫
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
        holder.setText(R.id.tx2, "¥" + String.format("%.2f", (StringUtils.doStringToDouble(item.getZk_final_price()) - StringUtils.doStringToDouble(item.getCoupon_amount()))));
        //券后2
        TextView tx2_2 = holder.getView(R.id.tx2_2);
        //设置删除线
        tx2_2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tx2_2.setText("原价:¥" + item.getZk_final_price());
        //内部
        TextView tx3 = holder.getView(R.id.tx3);
        tx3.setText(item.getCoupon_amount() + "元");
        holder.setText(R.id.tx4, "奖:"+String.format("%.2f", item.getCommission()) + "元");
        holder.setText(R.id.tx5, "已售:"+item.getVolume());
        holder.getView(R.id.tv_delete).setTag(position);
        holder.getView(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDeleteClickListener != null) {
                    mDeleteClickListener.onDeleteClick(v, (Integer) v.getTag());
                }
            }
        });
    }
    public void setOnDeleteClickListener(OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }
}
