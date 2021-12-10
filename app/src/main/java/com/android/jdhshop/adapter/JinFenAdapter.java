package com.android.jdhshop.adapter;

import android.graphics.Paint;
import android.support.annotation.Nullable;
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
import com.android.jdhshop.bean.JinFenBean;
import com.android.jdhshop.common.SPUtils;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class JinFenAdapter extends BaseQuickAdapter<JinFenBean.Item, BaseViewHolder> {
    DecimalFormat df = new DecimalFormat("0.00");
    private String comm;

    public JinFenAdapter(int layoutResId, @Nullable List<JinFenBean.Item> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, JinFenBean.Item item) {
        //判断是不是点击ai助理添加商品进来的
        String zhuaqu = SPUtils.getStringData(mContext, "zhuaqu", "");
        if (zhuaqu.equals("1")) {
            holder.itemView.findViewById(R.id.zq_icon).setVisibility(View.VISIBLE);
            RequestParams params1 = new RequestParams();
            params1.put("product_id_str", item.skuId);
            HttpUtils.post(Constants.getJianCHa, mContext, params1, new TextHttpResponseHandler() {
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
                                params1.put("product_id_str", item.skuId);
                                HttpUtils.post(Constants.getJianCHa, mContext, params1, new TextHttpResponseHandler() {
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
                                                params.put("id", item.skuId);
                                                params.put("title", item.skuName);//商品标题
                                                params.put("desc", item.inOrderCount30Days);//商品推荐描述
                                                params.put("img", item.imageInfo.imageList.get(0).url);//宣传图片
                                                params.put("price", item.priceInfo.price);//商品原价
                                                params.put("org_price", "");//商品拼团价格（接口对应字段price_pg）
                                                params.put("after_price", Double.valueOf(item.priceInfo.price) - Double.valueOf(item.couponInfo.couponList.get(0).discount) + "");//券后/活动/折扣后最终价格（接口对应字段price_after）
                                                params.put("commission", item.commissionInfo.couponCommission);//佣金
                                                params.put("ts_time", "");//商品推荐描述
                                                params.put("ticket_start_time", item.commissionInfo.startTime);//券开始时间
                                                params.put("ticket_end_time", item.commissionInfo.endTime);//券开始时间
                                                params.put("linkurl", item.couponInfo.couponList.get(0).link);//推广购买链接
                                                params.put("descurl", "");//详情页面链接
                                                params.put("discount", Float.valueOf(String.valueOf(item.couponInfo.couponList.get(0).discount)));
//                                                params.put("shopname",item.);
                                                HttpUtils.post(Constants.getAddShngPin, mContext, params, new TextHttpResponseHandler() {
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
        Glide.with(mContext).load(item.imageInfo.imageList.get(0).url).placeholder(R.drawable.no_banner).dontAnimate().into(imageView);
        ((TextView) holder.getView(R.id.title_child)).setText(item.skuName);
        //券后2
        TextView tx2_2 = holder.getView(R.id.tx2_2);
        //设置删除线
        tx2_2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tx2_2.setText("原价:¥" + item.priceInfo.price);
        //内部
        TextView tx3 = holder.getView(R.id.tx3);
        if (item.couponInfo == null || item.couponInfo.couponList.size() <= 0) {
            tx3.setText(0 + "");
        } else {
            tx3.setText(item.couponInfo.couponList.get(0).discount + "");
        }
        TextView tx2 = holder.getView(R.id.tx2);
        try {
            tx2.setText("¥" + df.format(Double.valueOf(item.priceInfo.price) - Double.valueOf(item.couponInfo.couponList.get(0).discount)));
        } catch (Exception e) {
            tx2.setText("¥" + item.priceInfo.price);
        }
        holder.setText(R.id.tx4, "奖:" + df.format(Double.valueOf(tx2.getText().toString().replace("¥", "")) * Double.valueOf(df.format(Double.valueOf(item.commissionInfo.commissionShare) / 100)) * Double.parseDouble(df.format((float) SPUtils.getIntData(mContext, "rate", 0) / 100))));
        comm = df.format(Double.valueOf(tx2.getText().toString().replace("¥", "")) * Double.valueOf(df.format(Double.valueOf(item.commissionInfo.commissionShare) / 100)) * Double.parseDouble(df.format((float) SPUtils.getIntData(mContext, "rate", 0) / 100)));

        if (Double.valueOf(item.inOrderCount30Days) < 1000) {
            holder.setText(R.id.tx5, "销量:" + Double.valueOf(item.inOrderCount30Days));
        } else {
            holder.setText(R.id.tx5, "销量:" + df.format(Double.valueOf(item.inOrderCount30Days) / 10000) + "万");
        }
    }
}
