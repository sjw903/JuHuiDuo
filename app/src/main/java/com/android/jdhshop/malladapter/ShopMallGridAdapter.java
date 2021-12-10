package com.android.jdhshop.malladapter;

import android.content.Context;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.mallbean.MallCatbean;
import com.android.jdhshop.widget.CircleImageView;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * 开发者：陈飞
 * 时间:2018/7/12 18:41
 * 说明：服务gridView适配器
 */
public class ShopMallGridAdapter extends CommonAdapter<MallCatbean> {
    private TextView textView=null;
    public ShopMallGridAdapter(Context context, int layoutId, List<MallCatbean> datas) {
        super(context, layoutId, datas);
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    @Override
    protected void convert(ViewHolder viewHolder, MallCatbean item, int position) {
        //设置图标
        CircleImageView imageView = viewHolder.getView(R.id.service_icon);
            Glide.with(mContext).load(Constants.APP_IP + item.img).error(R.mipmap.icon_defult_boy).dontAnimate().into(imageView);
        //设置名称
        viewHolder.setText(R.id.service_name, item.cat_name);
    }
}
