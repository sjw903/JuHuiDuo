package com.android.jdhshop.merchantadapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.android.jdhshop.R;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.merchantbean.MerchantNewBean;

import java.util.List;

public class MerchantNewAdapter extends BaseQuickAdapter<MerchantNewBean.Item, BaseViewHolder> {
    public MerchantNewAdapter(int layoutResId, @Nullable List<MerchantNewBean.Item> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MerchantNewBean.Item item) {
        Glide.with(mContext).load(Constants.APP_IP+item.icon).into((ImageView) helper.getView(R.id.img));
        helper.setText(R.id.txt_name,item.title);
    }
}
