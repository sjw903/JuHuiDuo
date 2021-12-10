package com.android.jdhshop.adapter;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.android.jdhshop.R;
import com.android.jdhshop.bean.BalanceRecordListBean;
import com.android.jdhshop.bean.FeeBean;
import com.android.jdhshop.utils.DateUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * Created by yohn on 2018/10/25.
 */

public class FeeAdapter extends CommonAdapter<FeeBean.FeesBean> {
    public FeeAdapter(Context context, int layoutId, List<FeeBean.FeesBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, FeeBean.FeesBean item, int position) {
        viewHolder.setText(R.id.tv_title, item.title);
        LinearLayout ll_head = viewHolder.getView(R.id.ll_head);
        if (mDatas.size() - 1 == position) {
            ll_head.setBackground(mContext.getResources().getDrawable(R.mipmap.icon_gray_fee3));
        } else {
            ll_head.setBackground(mContext.getResources().getDrawable(R.mipmap.icon_gray_fee));
        }
        //判断CheckBox的状态
        CheckBox rb_one = viewHolder.getView(R.id.rb_one);
        if (mDatas.get(position).isChecked()) {
            rb_one.setChecked(true);//选中
        } else {
            rb_one.setChecked(false);//未选中
        }
    }
}