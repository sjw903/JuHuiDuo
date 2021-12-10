package com.android.jdhshop.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.android.jdhshop.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class AdAdapter  extends CommonAdapter<Integer> {

    public AdAdapter(Context context, int layoutId, List<Integer> datas) {
        super(context, layoutId, datas);
    }
    @Override
    protected void convert(ViewHolder holder, Integer s, int position) {
        ((ImageView) holder.getView(R.id.image_show)).setImageResource(s);
    }
}
