package com.android.jdhshop.merchantadapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.android.jdhshop.R;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.merchantbean.Authbean;
import com.android.jdhshop.merchantbean.Merchantlistbean;
import com.android.jdhshop.utils.StickHeaderDecoration;

import java.text.DecimalFormat;
import java.util.List;


public class MerchantlistAdapter extends BaseMultiItemQuickAdapter<Merchantlistbean, BaseViewHolder> implements StickHeaderDecoration.StickHeaderInterface {

    private List<Merchantlistbean> lists;
    private List<Authbean> aulist;
    DecimalFormat df = new DecimalFormat("0.00");

    public MerchantlistAdapter(@Nullable List<Merchantlistbean> data) {
        super(data);
        addItemType(Merchantlistbean.TEXT, R.layout.sort_header);
        addItemType(Merchantlistbean.IMG, R.layout.item_nearbystore);
    }

    @Override
    protected void convert(BaseViewHolder holder, Merchantlistbean item) {
        if (holder.getItemViewType() == 1) {
            ImageView xx1= holder.getView(R.id.near_xx1);
            ImageView xx2= holder.getView(R.id.near_xx2);
            ImageView xx3= holder.getView(R.id.near_xx3);
            ImageView xx4= holder.getView(R.id.near_xx4);
            ImageView xx5= holder.getView(R.id.near_xx5);
            ImageView xx6= holder.getView(R.id.near_xx6);
            BaseLogDZiYuan.LogDingZiYuan(xx1, "ic_star_black_24dp.png");
            BaseLogDZiYuan.LogDingZiYuan(xx2, "ic_star_black_24dp.png");
            BaseLogDZiYuan.LogDingZiYuan(xx3, "ic_star_black_24dp.png");
            BaseLogDZiYuan.LogDingZiYuan(xx4, "ic_star_black_24dp.png");
            BaseLogDZiYuan.LogDingZiYuan(xx5, "ic_star_black_24dp.png");
            BaseLogDZiYuan.LogDingZiYuan(xx6, "icon_locations.png");
            holder.setText(R.id.itemnear_tvname, item.merchant_name);
            holder.setText(R.id.itemnear_tvpingfen, item.comment_score + "分");
            if (item.consumption != null && !item.consumption.equals("null")) {
                holder.setText(R.id.itemnear_tvrenjunxf, "¥" + item.consumption + "/人");
            } else {
                holder.getView(R.id.itemnear_tvrenjunxf).setVisibility(View.GONE);
            }
            holder.setText(R.id.itemnear_tvaddress, item.detail_address);
            holder.setText(R.id.itemnear_tvxiaoliang, "销量" + item.sales_num);
            if (item.distinct != null) {
                holder.setText(R.id.itemnear_tvjuli, df.format(Double.parseDouble(item.distinct) / 1000) + "km");
            } else {
                holder.setText(R.id.itemnear_tvjuli, "0km");
            }
            Glide.with(mContext).load(Constants.APP_IP + item.merchant_avatar).error(R.mipmap.app_icon).into((ImageView) holder.getView(R.id.itemnear_img));
            ((LinearLayout) holder.getView(R.id.itemnear_lytype)).removeAllViews();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (item.auth != null) {
                String[] service = item.auth.split(",");
                for (int i = 0; i < service.length; i++) {
                    if (!service[i].equals("")) {
                        TextView tv = new TextView(mContext);
                        for (int j = 0; j < aulist.size(); j++) {

                            if (Integer.parseInt(service[i]) == Integer.parseInt(aulist.get(j).id)) {
                                tv.setText(aulist.get(j).name);
                            }

                        }
                        tv.setBackgroundResource(R.drawable.my_imgbg);
                        tv.setTextColor(Color.parseColor("#FEAB0E"));
                        tv.setTextSize(12);
                        layoutParams.rightMargin = 15;
                        tv.setLayoutParams(layoutParams);
                        ((LinearLayout) holder.getView(R.id.itemnear_lytype)).addView(tv);
                    }
                }
            }
        }else{

        }
    }

    @Override
    public boolean isStick(int position) {
        return position == 1;
    }
}
