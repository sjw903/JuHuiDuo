package com.android.jdhshop.malladapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.mall.MyShopMallOrderActivity;
import com.android.jdhshop.mall.ShopMallOrderDetailActivity;
import com.android.jdhshop.mallbean.OrderDetailBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class MyOrderDetailAdapter extends CommonAdapter<OrderDetailBean.OrderMsg.Detail> {
    public MyOrderDetailAdapter(Context context, int layoutId, List<OrderDetailBean.OrderMsg.Detail> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final OrderDetailBean.OrderMsg.Detail orderDetailBean, final int position) {
        ImageView img = holder.getView(R.id.img_shop);
        Glide.with(mContext).load(orderDetailBean.img.contains("http")?orderDetailBean.img: Constants.APP_IP + orderDetailBean.img).error(R.drawable.no_banner).dontAnimate().into(img);
        holder.setText(R.id.txt_name, orderDetailBean.goods_name);
        holder.setText(R.id.txt_num, "商品数量: " + orderDetailBean.num);
        holder.setText(R.id.txt_price, orderDetailBean.price);
        if(mContext instanceof ShopMallOrderDetailActivity || mContext instanceof MyShopMallOrderActivity) {
            holder.getView(R.id.img_jia).setVisibility(View.GONE);
            holder.getView(R.id.img_jian).setVisibility(View.GONE);
        }
        if((orderDetailBean.sku_str==null||"".equals(orderDetailBean.sku_str))){
            holder.getView(R.id.txt_attribute).setVisibility(View.GONE);
        }else {
            holder.getView(R.id.txt_attribute).setVisibility(View.VISIBLE);
        }
        holder.setText(R.id.txt_attribute,orderDetailBean.sku_str==null?"":Html.fromHtml(orderDetailBean.sku_str).toString());
    }

}
