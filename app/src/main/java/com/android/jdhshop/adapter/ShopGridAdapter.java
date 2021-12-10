package com.android.jdhshop.adapter;

import android.content.Context;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.SubListByParentBean;
import com.android.jdhshop.bean.SubListByParentChildBean;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.widget.CircleImageView;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * 开发者：陈飞
 * 时间:2018/7/12 18:41
 * 说明：服务gridView适配器
 */
public class ShopGridAdapter extends CommonAdapter<SubListByParentChildBean> {
    private TextView textView=null;
    public ShopGridAdapter(Context context, int layoutId, List<SubListByParentChildBean> datas) {
        super(context, layoutId, datas);
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    @Override
    protected void convert(ViewHolder viewHolder, SubListByParentChildBean item, int position) {
        //设置图标
        CircleImageView imageView = viewHolder.getView(R.id.service_icon);
        String img_src = item.getIcon().startsWith("http") ? item.getIcon() : Constants.APP_IP + item.getIcon();
        Glide.with(mContext).load(img_src).dontAnimate().into(imageView);
        //设置名称
        viewHolder.setText(R.id.service_name, item.getName());
        if(item.getName().equals(SPUtils.getStringData(mContext,"search_name",""))){
            textView=viewHolder.getView(R.id.service_name);
            ((TextView)viewHolder.getView(R.id.service_name)).setTextColor(mContext.getResources().getColor(R.color.red1));
            SPUtils.saveStringData(mContext,"search_name","");
        }
    }
}
