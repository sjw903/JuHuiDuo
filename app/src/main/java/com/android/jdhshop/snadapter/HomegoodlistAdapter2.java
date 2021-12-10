package com.android.jdhshop.snadapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.snbean.HomeGoodlistbean;
import com.zhy.adapter.recyclerview.CommonAdapter;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 开发者：陈飞
 * 时间:2018/7/26 14:34
 * 说明：子适配器
 */
public class HomegoodlistAdapter2 extends CommonAdapter<HomeGoodlistbean> {
    SpannableString spannableString;
    Drawable drawable;
    DecimalFormat df=new DecimalFormat("0.00");
    public HomegoodlistAdapter2(Context context, int layoutId, List<HomeGoodlistbean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(com.zhy.adapter.recyclerview.base.ViewHolder holder, HomeGoodlistbean item, int position) {
        //设置图片
        ImageView imageView = holder.getView(R.id.image);
        try {
            Glide.with(mContext).load(item.commodityInfo.pictureUrl.get( 0 ).picUrl).placeholder(R.drawable.no_banner).dontAnimate().into(imageView);
        }catch (Exception e){
            Glide.with(mContext).load("").placeholder(R.drawable.no_banner).dontAnimate().into(imageView);
        }
        ((TextView)holder.getView(R.id.title_child)).setText(item.commodityInfo.commodityName);

        if (item.commodityInfo!=null && item.commodityInfo.commodityPrice!=null) {
            //券后
            if (item.couponInfo.couponValue == null || item.couponInfo.couponValue.equals("")) {
                holder.setText(R.id.tx2, "¥" + df.format(Double.parseDouble(item.commodityInfo.commodityPrice)));

            } else {
                holder.setText(R.id.tx2, "¥" + df.format(Double.parseDouble(item.commodityInfo.commodityPrice) - Double.parseDouble(item.couponInfo.couponValue)));
            }

        //券后2
        TextView tx2_2 = holder.getView(R.id.tx2_2);
        //设置删除线
        tx2_2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tx2_2.setText( item.commodityInfo.commodityPrice);

        //内部
        TextView tx3 = holder.getView(R.id.tx3);

        if (item.couponInfo.couponValue==null||item.couponInfo.couponValue.equals( "" )){
            tx3.setText( "0元");
//            if (Constants.group_id != null && Constants.group_id != null && Constants.group_id.equals( "1" )) {
//                holder.setText( R.id.tx4, "升级赚:" + df.format( (Double.parseDouble( item.commodityInfo.commodityPrice ) ) * Double.valueOf( item.commodityInfo.rate ) / 100 * Double.parseDouble( df.format( (float) Double.parseDouble( Constants.yongjinbi ) / 100 ) ) ) );
////            holder.setText( R.id.tx4, "升级赚:" + df.format( Double.valueOf( item.itemendprice ) *Double.parseDouble( item.tkrates )/100*0.6 ) );
//            } else if (Constants.group_id != null && Constants.group_id.equals( "2" ) || Constants.group_id.equals( "3" )) {
//                holder.setText( R.id.tx4, "预估赚:" + df.format( (Double.parseDouble( item.commodityInfo.commodityPrice ) ) * Double.valueOf( item.commodityInfo.rate ) / 100 * Double.parseDouble( df.format( (float) SPUtils.getIntData( mContext, "rate", 0 ) / 100.00 ) ) ) );
//            } else {
                holder.setText( R.id.tx4, "预估赚:" + df.format( (Double.parseDouble( item.commodityInfo.commodityPrice ) ) * Double.valueOf( item.commodityInfo.rate ) / 100 * Double.parseDouble( df.format( (float) SPUtils.getIntData( mContext, "rate", 0 ) / 100.00 ) ) ) );
//            }
        }else {
            tx3.setText(item.couponInfo.couponValue  + "元");
//            if (Constants.group_id != null && Constants.group_id != null && Constants.group_id.equals( "1" )) {
//                holder.setText( R.id.tx4, "升级赚:" + df.format( (Double.parseDouble( item.commodityInfo.commodityPrice ) - Double.parseDouble( item.couponInfo.couponValue )) * Double.valueOf( item.commodityInfo.rate ) / 100 * Double.parseDouble( df.format( (float) Double.parseDouble( Constants.yongjinbi ) / 100 ) ) ) );
////            holder.setText( R.id.tx4, "升级赚:" + df.format( Double.valueOf( item.itemendprice ) *Double.parseDouble( item.tkrates )/100*0.6 ) );
//            } else if (Constants.group_id != null && Constants.group_id.equals( "2" ) || Constants.group_id.equals( "3" )) {
//                holder.setText( R.id.tx4, "预估赚:" + df.format( (Double.parseDouble( item.commodityInfo.commodityPrice ) - Double.parseDouble( item.couponInfo.couponValue )) * Double.valueOf( item.commodityInfo.rate ) / 100 * Double.parseDouble( df.format( (float) SPUtils.getIntData( mContext, "rate", 0 ) / 100.00 ) ) ) );
//            } else {
                holder.setText( R.id.tx4, "预估赚:" + df.format( (Double.parseDouble( item.commodityInfo.commodityPrice ) - Double.parseDouble( item.couponInfo.couponValue )) * Double.valueOf( item.commodityInfo.rate ) / 100 * Double.parseDouble( df.format( (float) SPUtils.getIntData( mContext, "rate", 0 ) / 100.00 ) ) ) );
//            }
        }
//        holder.setText(R.id.tx4, "预估赚:"+df.format(Double.valueOf(item.tkmoney)* Double.parseDouble(df.format((float) SPUtils.getIntData(mContext, "rate", 0) / 100))));

        holder.setText(R.id.tx5, "已售:"+item.commodityInfo.monthSales);
        }
    }



    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }
}
