package com.android.jdhshop.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.bean.FeeBean;
import com.android.jdhshop.bean.PayBean;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * Created by yohn on 2018/10/25.
 */

public class PayAdapter extends CommonAdapter<PayBean> {
    public PayAdapter(Context context, int layoutId, List<PayBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, PayBean item, int position) {
        TextView tv_title=viewHolder.getView( R.id.tv_title );
        tv_title.setText( item.title );
        //1 支付宝支付  2 微信支付
        if(1==item.type){
            Drawable drawable1 = mContext.getResources().getDrawable(R.mipmap.icon_my_alipay);
            drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
            tv_title.setCompoundDrawables(drawable1, null, null, null);
        }else{
            Drawable drawable1 = mContext.getResources().getDrawable(R.mipmap.icon_my_pay_wxpay);
            drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
            tv_title.setCompoundDrawables(drawable1, null, null, null);
        }
        //判断CheckBox的状态
        CheckBox rb_one=viewHolder.getView( R.id.rb_one );
        if(mDatas.get(position).isChecked()){
            rb_one.setChecked(true);//选中
        }else {
            rb_one.setChecked(false);//未选中
        }
    }
}
