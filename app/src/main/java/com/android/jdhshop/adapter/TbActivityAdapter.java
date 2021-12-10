package com.android.jdhshop.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.BannerBean;
import com.android.jdhshop.config.Constants;

import java.util.List;

public class TbActivityAdapter extends BaseQuickAdapter<BannerBean,BaseViewHolder> {
    public TbActivityAdapter(int layoutResId, @Nullable List<BannerBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BannerBean item) {
        Glide.with(mContext).load(Constants.APP_IP+item.icon).into((ImageView) helper.getView(R.id.img));

    }
}
