package com.android.jdhshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.PromotionDetailsActivity;
import com.android.jdhshop.bean.HaoDanBean;
import com.android.jdhshop.utils.StringUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class PHBAdapter extends CommonAdapter<HaoDanBean> {

    public PHBAdapter(Context context, int layoutId, List<HaoDanBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, final HaoDanBean item, int position) {
        //设置标题
        TextView title = viewHolder.getView(R.id.txt_nine);
        title.setText(item.itemtitle);
//        Glide.with(mContext).load(item.itempic+"_310x310.jpg").placeholder(R.drawable.no_banner).dontAnimate().into((ImageView)viewHolder.getView(R.id.img_nine));
        //券后
        viewHolder.setText(R.id.tx2, "¥" +item.itemendprice);
        //券后2
        TextView tx2_2 = viewHolder.getView(R.id.tx2_2);
        //设置删除线
        tx2_2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tx2_2.setText("¥" + item.itemprice);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("num_iid", item.itemid);
                Intent intent = new Intent(mContext, PromotionDetailsActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }
}
