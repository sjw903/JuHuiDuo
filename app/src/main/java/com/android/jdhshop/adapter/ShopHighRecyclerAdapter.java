package com.android.jdhshop.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.TaobaoGuestBean;
import com.android.jdhshop.utils.StringUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by yohn on 2018/8/31.
 */

public class ShopHighRecyclerAdapter extends CommonAdapter<TaobaoGuestBean.TaobaoGuesChildtBean> {

    public ShopHighRecyclerAdapter(Context context, int layoutId, List<TaobaoGuestBean.TaobaoGuesChildtBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, TaobaoGuestBean.TaobaoGuesChildtBean taobaoGuesChildtBean, int position) {
        //设置图片
        ImageView imageView = holder.getView( R.id.image);
        Glide.with(mContext).load(taobaoGuesChildtBean.getPict_url()).dontAnimate().into(imageView);

        //设置是否天猫图片
        if (taobaoGuesChildtBean.isIs_tmall()) { //是天猫
            holder.setImageResource(R.id.shop_image, R.mipmap.home_tmall);
        } else {//不是天猫
            holder.setImageResource(R.id.shop_image, R.mipmap.home_taobao);
        }


        //标题
        holder.setText(R.id.title_child, taobaoGuesChildtBean.getTitle());
//        holder.setText(R.id.tx1, "折扣价");
        //券后
        holder.setText(R.id.tx2, "¥" + String.format("%.2f", StringUtils.doStringToDouble(taobaoGuesChildtBean.getZk_final_price()) - StringUtils.doStringToDouble(taobaoGuesChildtBean.getCoupon_amount())));
        //券后2
        TextView tx2_2 = holder.getView(R.id.tx2_2);
        //设置删除线
        tx2_2.getPaint().setFlags( Paint.STRIKE_THRU_TEXT_FLAG);
        tx2_2.setText("¥" + taobaoGuesChildtBean.getZk_final_price());
        //内部
        TextView tx3 = holder.getView(R.id.tx3);
        tx3.setText(taobaoGuesChildtBean.getCoupon_amount() + "元券");
        holder.setText(R.id.tx4, String.format("%.2f", taobaoGuesChildtBean.getCommission()) + "元");
        holder.setText(R.id.tx5, taobaoGuesChildtBean.getVolume());
    }

}
