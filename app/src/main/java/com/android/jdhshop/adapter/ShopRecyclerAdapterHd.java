package com.android.jdhshop.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.HaoDanBean;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.my.CollectionActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 开发者：陈飞
 * 时间:2018/7/26 15:54
 * 说明：
 */
public class ShopRecyclerAdapterHd extends CommonAdapter<HaoDanBean> {
    Drawable drawable;
    private OnDeleteClickLister mDeleteClickListener;
    DecimalFormat df=new DecimalFormat("0.00");
    public ShopRecyclerAdapterHd(Context context, int layoutId, List<HaoDanBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, HaoDanBean item, int position) {
        //设置图片
        ImageView imageView = holder.getView(R.id.image);
        Glide.with(mContext).load(item.itempic+"_310x310.jpg").placeholder(R.drawable.no_banner).dontAnimate().into(imageView);
        ((TextView) holder.getView(R.id.title_child)).setText(item.itemtitle);
        //券后
        holder.setText(R.id.tx2, "¥" + item.itemendprice);
        //券后2
        TextView tx2_2 = holder.getView(R.id.tx2_2);
        //设置删除线
        tx2_2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tx2_2.setText(item.itemprice);
        //内部
        TextView tx3 = holder.getView(R.id.tx3);
        tx3.setText(item.couponmoney+"元");
        holder.setText(R.id.tx5,"已售"+item.itemsale);

        TextView tx4 = holder.getView(R.id.tx4);
        tx4.setText("现金红包");
        tx4.setBackgroundResource(R.drawable.bg_txt_taobao);

//        try {
//            holder.setText(R.id.tx4, "预估赚:"+df.format(Double.valueOf(item.itemendprice)*Double.valueOf(item.tkrates)/100* Double.parseDouble(df.format((float) SPUtils.getIntData(mContext, "rate", 0) / 100))));
//        } catch (Exception e) {
//            holder.setText(R.id.tx4, "预估赚0");
//        }
        if(mContext instanceof CollectionActivity){
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
    }

    public void setOnDeleteClickListener(OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }
}
