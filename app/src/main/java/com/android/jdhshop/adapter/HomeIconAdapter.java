package com.android.jdhshop.adapter;

import android.content.Context;
import android.util.Log;

import com.android.jdhshop.R;
import com.android.jdhshop.bean.Item;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.fragments.PinPaiFragment;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import me.panpf.sketch.SketchImageView;

public class HomeIconAdapter extends CommonAdapter<Item> {
    public HomeIconAdapter(Context context, int layoutId, List<Item> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Item setBean, int position) {
        SketchImageView imageView = holder.getView(R.id.service_icon1);
        imageView.getOptions().setCacheProcessedImageInDisk(true);
        imageView.getOptions().setDecodeGifImage(true);
        if(mContext instanceof PinPaiFragment){
            imageView.displayImage( setBean.icon);
        }else{
            LogUtils.d("TAG", "convert: " + setBean.icon);
            if (setBean.icon.startsWith("http:") || setBean.icon.startsWith("https:")) {
                imageView.displayImage(setBean.icon);
            }
            else
            {
                imageView.displayImage(Constants.APP_IP + setBean.icon);
            }
        }
        holder.setText(R.id.service_name, setBean.name);
    }
}
