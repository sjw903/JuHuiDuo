package com.android.jdhshop.merchantadapter;

import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.android.jdhshop.common.LogUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.android.jdhshop.R;
import com.android.jdhshop.config.Constants;

import java.util.List;

public class MerchantNewImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public MerchantNewImgAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        LogUtils.d("dfas",item);
        Glide.with(mContext).load(Constants.APP_IP+item).into((ImageView) helper.getView(R.id.img));

    }
}
