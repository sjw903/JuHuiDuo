package com.android.jdhshop.adapter;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.MyGoodsResp;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.utils.VerticalImageSpan;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import jd.union.open.goods.query.response.GoodsResp;

public class JDAdapterList2 extends BaseQuickAdapter<MyGoodsResp, BaseViewHolder> {
    SpannableString spannableString;
    Drawable drawable;
    DecimalFormat df = new DecimalFormat("0.00");
    private String comm;
    private String yuanjia;
    private ShopRecyclerAdapter.OnDeleteClickLister mDeleteClickListener;

    public JDAdapterList2(int layoutResId, @Nullable List<MyGoodsResp> data) {
        super(layoutResId, data);
    }

//    public JDAdapterList2(Context context, int layoutId, List<GoodsResp> datas) {
//        super(context, layoutId, datas);
//    }

    @Override
    protected void convert(BaseViewHolder holder, MyGoodsResp item) {

        //判断是不是点击ai助理添加商品进来的
        String zhuaqu = SPUtils.getStringData(mContext, "zhuaqu", "");
        if (zhuaqu.equals("1")) {
            holder.itemView.findViewById(R.id.zq_icon).setVisibility(View.VISIBLE);
            RequestParams params1 = new RequestParams();
            params1.put("product_id_str", item.getSkuId());
            HttpUtils.post(Constants.getJianCHa, mContext,params1, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseString);
                        String string = jsonObject.getString("list");
                        if (string.equals("[]")) {
                            holder.itemView.findViewById(R.id.zq_icon).setVisibility(View.VISIBLE);
                        } else {
                            holder.itemView.findViewById(R.id.zq_icon).setVisibility(View.GONE);
                        }
                        holder.itemView.findViewById(R.id.zq_icon).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RequestParams params1 = new RequestParams();
                                params1.put("product_id_str", item.getSkuId());
                                HttpUtils.post(Constants.getJianCHa, mContext,params1, new TextHttpResponseHandler() {
                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                                    }

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(responseString);
                                            String string = jsonObject.getString("list");
                                            if (string.equals("[]")) {
                                                String huoquaddid = SPUtils.getStringData(mContext, "huoquaddid", "");
                                                String replace = huoquaddid.replaceAll("\\[", "");
                                                String s = replace.replaceAll("]", "");
                                                RequestParams params = new RequestParams();
                                                params.put("gid", s);
                                                params.put("pid", Constants.PLATFORM_JD);
                                                params.put("id", item.getSkuId());
                                                params.put("title", item.getSkuName());//商品标题
                                                params.put("desc", item.getSkuName());//商品推荐描述
                                                params.put("img", item.imageInfo.getImageList()[0].getUrl());//宣传图片
                                                params.put("price", item.pingGouInfo != null ? item.priceInfo.getPrice() : 0);//商品原价
                                                params.put("org_price", "");//商品拼团价格（接口对应字段price_pg）
                                                params.put("after_price", item.priceInfo.getPrice() - item.couponInfo.getCouponList()[0].getDiscount() + "");//券后/活动/折扣后最终价格（接口对应字段price_after）
                                                params.put("commission", df.format(Double.valueOf(item.priceInfo.getPrice() - item.couponInfo.getCouponList()[0].getDiscount()) * Double.valueOf(df.format(item.commissionInfo.getCommissionShare() / 100))));//佣金
                                                params.put("ts_time", "");//商品推荐描述
                                                params.put("ticket_start_time", item.couponInfo.getCouponList()[0].getGetStartTime()+"");//券开始时间
                                                params.put("ticket_end_time", item.couponInfo.getCouponList()[0].getGetEndTime()+"");//券开始时间
                                                params.put("linkurl", item.couponInfo.getCouponList()[0].getLink());//推广购买链接
                                                params.put("descurl", "");//详情页面链接
                                                params.put("discount",Float.valueOf(String.valueOf(item.couponInfo.getCouponList()[0].getDiscount())));
                                                params.put("shopname",item.shopInfo.getShopName());
                                                HttpUtils.post(Constants.getAddShngPin,mContext, params, new TextHttpResponseHandler() {
                                                    @Override
                                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                                                    }

                                                    @Override
                                                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(responseString);
                                                            //showToast("成功");
                                                            Toast.makeText(mContext, "添加成功", Toast.LENGTH_SHORT).show();
                                                            Log.d("成功成功成功", "成功成功成功");
                                                            holder.itemView.findViewById(R.id.zq_icon).setVisibility(View.GONE);
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(mContext, "已有该商品", Toast.LENGTH_SHORT).show();
                                                holder.itemView.findViewById(R.id.zq_icon).setVisibility(View.GONE);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        });
                        //showToast("成功");

                        //Log.d("成功成功成功", "成功成功成功");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        TextView view = holder.getView(R.id.tx2);

        ImageView imageView = holder.getView(R.id.image);
        if (item.imageInfo == null || item.imageInfo.getImageList().length == 0) {
            Glide.with(mContext).load("").placeholder(R.drawable.no_banner).dontAnimate().into(imageView);
        } else {
            Glide.with(mContext).load(item.imageInfo.getImageList()[0].getUrl()).placeholder(R.drawable.no_banner).dontAnimate().into(imageView);
        }
        spannableString = new SpannableString("   " + item.getSkuName());
        drawable = mContext.getResources().getDrawable(R.mipmap.label_jd);
        drawable.setBounds(0, 0, 120, 100);
        spannableString.setSpan(new VerticalImageSpan(drawable), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView) holder.getView(R.id.title_child)).setText(spannableString);
        //券后2
        TextView tx2_2 = holder.getView(R.id.tx2_2);
        //设置删除线
        tx2_2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tx2_2.setText("原价:¥" + (item.pingGouInfo != null ? item.priceInfo.getPrice() : 0));        //内部
        TextView tx3 = holder.getView(R.id.tx3);
        if (item.couponInfo == null || item.couponInfo.getCouponList().length == 0) {
            tx3.setText(0 + "");
        } else {
            tx3.setText(item.couponInfo.getCouponList()[0].getDiscount() + "");
        }
        TextView tx2 = holder.getView(R.id.tx2);
        try {
            tx2.setText("¥" + df.format(item.pingGouInfo.getPingouPrice() - item.couponInfo.getCouponList()[0].getDiscount()));
        } catch (Exception e) {
            try {
                tx2.setText("¥" + df.format(item.priceInfo.getPrice() - item.couponInfo.getCouponList()[0].getDiscount()));
            } catch (Exception e1) {
                tx2.setText("¥" + df.format(item.priceInfo.getPrice()));
            }
        }
        if (item.commissionInfo == null) {
            holder.setText(R.id.tx4, "奖:0");
        } else {
            holder.setText(R.id.tx4, "奖:" + df.format(Double.valueOf(tx2.getText().toString().replace("¥", "")) * Double.valueOf(df.format(item.commissionInfo.getCommissionShare() / 100)) * Double.parseDouble(df.format((float) SPUtils.getIntData(mContext, "rate", 0) / 100))));
            comm = df.format(Double.valueOf(tx2.getText().toString().replace("¥", "")) * Double.valueOf(df.format(item.commissionInfo.getCommissionShare() / 100)));
            Log.d("commmmm", comm+"");
            Log.d("`````", Double.valueOf(tx2.getText().toString().replace("¥", ""))+"");
            Log.d("----", Double.valueOf(df.format(item.commissionInfo.getCommissionShare() / 100))+"");
            Log.d("tx2tx2tx2", df.format(Double.valueOf(tx2.getText().toString().replace("¥", ""))));
            Log.d("commissionI", Double.valueOf(df.format(item.commissionInfo.getCommissionShare() / 100))+"");
            Log.d("commissionI", Double.parseDouble(df.format((float) SPUtils.getIntData(mContext, "rate", 0) / 100))+"");
        }
        if (item.getInOrderCount30Days() < 1000) {
            holder.setText(R.id.tx5, "销量:" + item.getInOrderCount30Days());
        } else {
            holder.setText(R.id.tx5, "销量:" + df.format((float) item.getInOrderCount30Days() / 10000) + "万");
        }
        holder.getView(R.id.tv_delete).setTag(holder.getAdapterPosition());
        holder.getView(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDeleteClickListener != null) {
                    mDeleteClickListener.onDeleteClick(v, (Integer) v.getTag());
                }
            }
        });
    }

}
