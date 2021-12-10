package com.android.jdhshop.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.ShopTabsChildBean;
import com.android.jdhshop.config.Constants;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

public class DouKindAdapter extends CommonAdapter<ShopTabsChildBean> {

    public DouKindAdapter(Context context, int layoutId, List<ShopTabsChildBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, ShopTabsChildBean item, int position) {
        viewHolder.setText(R.id.txt_name,item.getName());
        Glide.with(mContext).load(Constants.APP_IP+item.getIcon()).into((ImageView) viewHolder.getView(R.id.img_icon));
    }
}
