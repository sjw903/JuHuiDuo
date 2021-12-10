package com.android.jdhshop.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.PromotionDetailsActivity;
import com.android.jdhshop.bean.Vipptitem;

import java.util.List;

/**
 * 购物车  适配器
 * */
public class BrandlistAdapter extends BaseQuickAdapter<Vipptitem,BaseViewHolder> {
    public BrandlistAdapter(int layoutResId, @Nullable List<Vipptitem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final Vipptitem item) {
        holder.setText(R.id.itemitem_name, item.itemtitle );
        Glide.with( mContext ).load( item.itempic+"_310x310.jpg" ).into((ImageView) holder.getView(R.id.itemitem_img));
        holder.setText(R.id.itemitem_qhj,"券后价: ¥"+ item.itemendprice );
        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("num_iid", item.itemid);
                Intent intent = new Intent( mContext,PromotionDetailsActivity.class );
                intent.putExtras( bundle );
                mContext.startActivity( intent );
            }
        } );
    }
}
