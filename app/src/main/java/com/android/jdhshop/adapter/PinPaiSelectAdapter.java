package com.android.jdhshop.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.android.jdhshop.R;

import java.util.List;

public class PinPaiSelectAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    boolean[] booleans;
    public PinPaiSelectAdapter(int layoutResId, @Nullable List<String> data,boolean[] booleans) {
        super(layoutResId, data);
        this.booleans=booleans;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView x=helper.getView(R.id.ssd);
        helper.setText(R.id.ssd,item);
        if(booleans[helper.getAdapterPosition()]){
            helper.getView(R.id.ssd).setBackgroundColor(mContext.getResources().getColor(R.color.white));
            x.setTextColor(mContext.getResources().getColor(R.color.red));
        }else{
            helper.getView(R.id.ssd).setBackgroundColor(mContext.getResources().getColor(R.color.light1_gray));
            x.setTextColor(mContext.getResources().getColor(R.color.col_666));
        }
    }
}
