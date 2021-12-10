package com.android.jdhshop.merchantadapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.DjjActivity;
import com.android.jdhshop.merchantbean.MerchantNewBean;

import java.util.List;

public class MerchantDjqAdapter extends BaseQuickAdapter<MerchantNewBean.Item, BaseViewHolder> {
    public MerchantDjqAdapter(int layoutResId, @Nullable List<MerchantNewBean.Item> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MerchantNewBean.Item item) {
        if(mContext instanceof DjjActivity){
            helper.setText(R.id.txt_name,item.amount+"元代金券");
            helper.setText(R.id.txt_price,item.price);
            helper.setText(R.id.txt_time,item.validity_zh);
            helper.setText(R.id.txt_zhe,"库存"+item.inventory);
        }else{
            helper.setText(R.id.txt_one,item.amount+"元代金券");
            helper.setText(R.id.txt_two,item.price);
            helper.setText(R.id.txt_three,item.validity_zh);
            helper.setText(R.id.txt_five,"库存"+item.inventory);
            helper.addOnClickListener(R.id.txt_buy);
        }
    }
}
