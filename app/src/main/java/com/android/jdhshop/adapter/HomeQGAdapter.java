package com.android.jdhshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.PromotionDetailsActivity;
import com.android.jdhshop.bean.HaoDanBean;
import com.android.jdhshop.bean.TaobaoGuestBean;
import com.android.jdhshop.utils.StringUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.DecimalFormat;
import java.util.List;

public class HomeQGAdapter extends CommonAdapter<HaoDanBean> {
    DecimalFormat df = new DecimalFormat("0.00");
    public HomeQGAdapter(Context context, int layoutId, List<HaoDanBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final HaoDanBean bean, int position) {
        holder.setText(R.id.txt_nine, bean.itemtitle);
//        Glide.with(mContext).load(bean.itempic+"_310x310.jpg").placeholder(R.drawable.no_banner).dontAnimate().into((ImageView) holder.getView(R.id.img_nine));
        holder.setText(R.id.tx2, "¥" +bean.itemendprice);
        TextView tx2_2 = holder.getView(R.id.tx2_2);
        tx2_2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tx2_2.setText("¥" + bean.itemprice);
        ProgressBar progressBar=holder.getView(R.id.pb_progressbar);
//        progressBar.setVisibility(View.GONE);
        progressBar.setMax(Integer.valueOf(bean.couponnum));
        progressBar.setProgress(Integer.valueOf(bean.couponreceive));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("num_iid", bean.itemid);
                Intent intent = new Intent(mContext, PromotionDetailsActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }
}
