package com.android.jdhshop.merchantadapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.SetBean;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.merchantbean.MerchantGroupbean;
import com.android.jdhshop.widget.CircleImageView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class MerchanthomeIconAdapter extends CommonAdapter<MerchantGroupbean> {
    public MerchanthomeIconAdapter(Context context, int layoutId, List<MerchantGroupbean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, MerchantGroupbean setBean, int position) {
        //设置图标
        CircleImageView imageView = holder.getView(R.id.service_icon);
        Glide.with(mContext).load(Constants.APP_IP + setBean.icon).placeholder(R.drawable.no_banner).dontAnimate().into(imageView);
        //设置名称
        holder.setText(R.id.service_name, setBean.name);
    }
}
