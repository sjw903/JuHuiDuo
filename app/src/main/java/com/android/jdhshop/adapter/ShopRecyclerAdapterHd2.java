package com.android.jdhshop.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.Gydlistbean;
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
public class ShopRecyclerAdapterHd2 extends CommonAdapter<Gydlistbean> {
    Drawable drawable;
    private OnDeleteClickLister mDeleteClickListener;
    DecimalFormat df=new DecimalFormat("0.00");
    public ShopRecyclerAdapterHd2(Context context, int layoutId, List<Gydlistbean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Gydlistbean item, int position) {
        //设置图片
        ImageView misimg = holder.getView( R.id.shop_image );
        if (item.user_type.equals( "1" )){
            Glide.with( mContext ).load( R.mipmap.home_taobao ).into( misimg );
        }else if (item.user_type.equals( "2" )){
            Glide.with( mContext ).load( R.mipmap.home_tmall ).into( misimg );
        }else{
            misimg.setVisibility( View.GONE );
        }
        ImageView imageView = holder.getView(R.id.image);
        Glide.with(mContext).load(item.pict_url).placeholder(R.drawable.no_banner).dontAnimate().into(imageView);
        ((TextView) holder.getView(R.id.title_child)).setText(item.title);
        //券后
        holder.setText(R.id.tx2, "¥" + item.quanhou_jiage);
        //券后2
        TextView tx2_2 = holder.getView(R.id.tx2_2);
        //设置删除线
        tx2_2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tx2_2.setText(item.size);
        //内部
        TextView tx3 = holder.getView(R.id.tx3);
        tx3.setText(item.coupon_info_money+"元券");
        holder.setText(R.id.tx6,"销量:"+item.volume);
        holder.setText( R.id.tx5,"店铺:"+item.shop_title );
        try {
            holder.setText(R.id.tx4, "预估收益:"+df.format(Double.valueOf(item.tkfee3)* Double.parseDouble(df.format((float) SPUtils.getIntData(mContext, "rate", 0) / 100)))+"元");
        } catch (Exception e) {
            holder.setText(R.id.tx4, "预估收益0");
        }
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
