package com.android.jdhshop.malladapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.android.jdhshop.R;
import com.android.jdhshop.mallbean.ExpressDetailBean;

import java.util.List;

public class ExpressDetailAdapter extends BaseQuickAdapter<ExpressDetailBean, BaseViewHolder> {
    public ExpressDetailAdapter(int layoutResId, @Nullable List<ExpressDetailBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ExpressDetailBean item) {
        ImageView imageView=helper.getView(R.id.img_point);
        TextView txt1=helper.getView(R.id.txt_time);
        TextView txt2=helper.getView(R.id.txt_content);
        txt1.setText(item.time);
        txt2.setText("【"+item.location+"】"+item.context);
        if(helper.getAdapterPosition()==0){
            imageView.setImageResource(R.drawable.icon_point_red);
            txt1.setTextColor(mContext.getResources().getColor(R.color.red1));
            txt2.setTextColor(mContext.getResources().getColor(R.color.red1));
        }else{
            imageView.setImageResource(R.drawable.icon_point_gray);
            txt1.setTextColor(mContext.getResources().getColor(R.color.col_333));
            txt2.setTextColor(mContext.getResources().getColor(R.color.darkgray));
        }

    }
}
