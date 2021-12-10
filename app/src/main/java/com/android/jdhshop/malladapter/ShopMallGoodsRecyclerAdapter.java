package com.android.jdhshop.malladapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jdhshop.common.LogUtils;
import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.mallbean.ShopMallGoodsBean;
import com.android.jdhshop.userupdate.UpdateGroupActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class ShopMallGoodsRecyclerAdapter extends CommonAdapter<ShopMallGoodsBean> {
    Drawable drawable;
    private OnDeleteClickLister mDeleteClickListener;
    public ShopMallGoodsRecyclerAdapter(Context context, int layoutId, List<ShopMallGoodsBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ShopMallGoodsBean item, int position) {
        //设置图片
        ImageView imageView = holder.getView(R.id.image);
        LogUtils.d("dfawef",item.img);
        Glide.with(mContext).load(item.img.contains("http")?item.img: Constants.APP_IP+item.img).error(R.mipmap.app_icon).dontAnimate().into(imageView);

        ((TextView)holder.getView(R.id.title_child)).setText(item.goods_name);
        holder.setText(R.id.tx2, item.price);
        TextView tx2_2 = holder.getView(R.id.tx2_2);
        tx2_2.setText("已售" + item.sales_volume);
        holder.setText(R.id.tx5, "赠送积分:"+item.give_point);
        holder.getView(R.id.tv_delete).setTag(position);
        holder.getView(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDeleteClickListener != null) {
                    mDeleteClickListener.onDeleteClick(v, (Integer) v.getTag());
                }
            }
        });
        if(mContext instanceof UpdateGroupActivity){
            holder.getView(R.id.txt_day).setVisibility(View.VISIBLE);
            holder.setText(R.id.txt_day,item.day_num+"天会员");
        }
    }
    public void setOnDeleteClickListener(OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }
}
