package com.android.jdhshop.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * 开发者：陈飞
 * 时间:2018/7/27 16:14
 * 说明：
 */
public class ImageAlbumAdapter extends CommonAdapter<String> {

    public ImageAlbumAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, String item, int position) {
        ImageView imageView = viewHolder.getView(R.id.image_show);
        Glide.with(mContext).load(item).dontAnimate().into(imageView);
    }
}
