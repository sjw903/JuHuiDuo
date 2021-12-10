package com.android.jdhshop.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.SjhdBean;
import com.android.jdhshop.bean.TaobaoGuestBean;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.utils.StringUtils;
import com.android.jdhshop.utils.VerticalImageSpan;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 开发者：陈飞
 * 时间:2018/7/26 15:54
 * 说明：
 */
public class SjhdRecyclerAdapter extends CommonAdapter<SjhdBean.SjhdListBean> {
    SpannableString spannableString;
    Drawable drawable;
    private OnDeleteClickLister mDeleteClickListener;
    public SjhdRecyclerAdapter(Context context, int layoutId, List<SjhdBean.SjhdListBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, SjhdBean.SjhdListBean item, int position) {
        //设置图片
        ImageView imageView = holder.getView(R.id.iv_image);
        Glide.with(mContext).load(Constants.APP_IP+item.getImg()).placeholder(R.mipmap.default_cover).dontAnimate().into(imageView);


        TextView it_text = holder.getView(R.id.it_text);
        it_text.setText("" + item.getTitle());

        TextView it_text_1 = holder.getView(R.id.it_text_1);
        it_text_1.setText("" + item.getStart_time());
    }
    public void setOnDeleteClickListener(OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }
}
