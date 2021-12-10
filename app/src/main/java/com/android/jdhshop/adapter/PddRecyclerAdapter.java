package com.android.jdhshop.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.PDDBean;
import com.android.jdhshop.my.CollectionActivity;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

/**
 * 开发者：wmm
 * 时间:2018/11/21 18:22
 * 说明：
 */
public class PddRecyclerAdapter extends CommonAdapter<PDDBean> {
    DecimalFormat df = new DecimalFormat("0.00");
    private String comm;
    private ShopRecyclerAdapter.OnDeleteClickLister mDeleteClickListener;

    public PddRecyclerAdapter(Context context, int layoutId, List<PDDBean> datas) {
        super(context, layoutId, datas);
    }

    private static boolean isBase64(String str) {
        String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        return Pattern.matches(base64Pattern, str);
    }

    /**
     * Base64解密字符串
     *
     * @param content     -- 待解密字符串
     * @param charsetName -- 字符串编码方式
     * @return
     */
    private String base64Decode(String content, String charsetName) {
        if (TextUtils.isEmpty(charsetName)) {
            charsetName = "utf-8";
        }
        byte[] contentByte = Base64.decode(content, Base64.DEFAULT);
        try {
            return new String(contentByte, charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void convert(ViewHolder holder, PDDBean item, int position) {
        //判断是不是点击ai助理添加商品进来的
        TextView view = holder.getView(R.id.tx2);
        String zhuaqu = SPUtils.getStringData(mContext, "zhuaqu", "");
        if(zhuaqu.equals("1")){
            holder.itemView.findViewById(R.id.zq_icon).setVisibility(View.VISIBLE);
            RequestParams params1 = new RequestParams();
            params1.put("product_id_str", item.getGoods_id());
            HttpUtils.post(Constants.getJianCHa,mContext, params1, new TextHttpResponseHandler() {
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
                                params1.put("product_id_str", item.getGoods_id());
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
                                                Log.d("拼多多", "onSuccess: " + item.toString());
                                                params.put("gid", s);
                                                params.put("pid", Constants.PLATFORM_PDD);
                                                params.put("id", item.getGoods_id());
                                                params.put("title", item.getGoods_name());//商品标题
                                                params.put("sign_id",item.getGoodsSign());
                                                params.put("desc", item.getGoods_desc());//商品推荐描述
                                                params.put("img", item.getGoods_image_url());//宣传图片
                                                params.put("price", df.format(Float.valueOf(item.getMin_normal_price()) / 100));//商品原价
                                                params.put("org_price", df.format(Double.valueOf(df.format(Float.valueOf(item.getMin_group_price()) / 100)) - Double.valueOf(df.format(Float.valueOf(item.getCoupon_discount()) / 100))));//商品拼团价格（接口对应字段price_pg）
                                                params.put("after_price", df.format(Double.valueOf(df.format(Float.valueOf(item.getMin_group_price()) / 100)) - Double.valueOf(df.format(Float.valueOf(item.getCoupon_discount()) / 100))));//券后/活动/折扣后最终价格（接口对应字段price_after）
                                                params.put("commission", df.format(Float.valueOf(item.getCommission() == null ? "0" : item.getCommission()) / 100 * 2) );//佣金
                                                params.put("ts_time", "");//商品推荐描述
                                                params.put("ticket_start_time", item.getCoupon_start_time());//券开始时间
                                                params.put("ticket_end_time", item.getCoupon_end_time());//券开始时间
                                                params.put("linkurl", item.getMateriaurl());//推广购买链接
                                                params.put("descurl", "");//详情页面链接
                                                params.put("discount", df.format(Float.valueOf(item.getCoupon_discount()) / 100) + "");
                                                params.put("shopname",item.getMall_name());
                                                HttpUtils.post(Constants.getAddShngPin, mContext,params, new TextHttpResponseHandler() {
                                                    @Override
                                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                                                    }

                                                    @Override
                                                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(responseString);
                                                            String msg = jsonObject.getString("msg");
                                                            //showToast("成功");
                                                            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                                            holder.itemView.findViewById(R.id.zq_icon).setVisibility(View.GONE);
                                                            Log.d("成功成功成功", "成功成功成功");
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





//        if(mContext instanceof CollectionActivity){
//            ImageView imageView = holder.getView(R.id.image);
//            Glide.with(mContext).load(item.getGoods_thumbnail_url()).error(R.drawable.no_banner).dontAnimate().into(imageView);
//            ((TextView)holder.getView(R.id.title_child)).setText(item.getGoods_name());
//            holder.getView(R.id.ll_top).setVisibility(View.GONE);
//            holder.setText(R.id.tx2_2,item.getMin_normal_price());
//        }else{
        //设置图片
        ImageView imageView = holder.getView(R.id.image);
        Glide.with(mContext).load(item.getGoods_thumbnail_url()).error(R.drawable.no_banner).dontAnimate().into(imageView);
        ((TextView) holder.getView(R.id.title_child)).setText(item.getGoods_name());
        holder.setText(R.id.tx2, "券后价:¥" + df.format(Double.valueOf(df.format(Float.valueOf(item.getMin_group_price()) / 100)) - Double.valueOf(df.format(Float.valueOf(item.getCoupon_discount()) / 100))));
        TextView tx2_2 = holder.getView(R.id.tx2_2);
//        df.format(Float.valueOf(item.getMin_normal_price())/100))
        //设置删除线
        tx2_2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tx2_2.setText("原价:¥" + df.format(Float.valueOf(item.getMin_normal_price()) / 100));
        TextView tx3 = holder.getView(R.id.tx3);
        tx3.setText(df.format(Float.valueOf(item.getCoupon_discount()) / 100) + "");
        holder.setText(R.id.tx4, "奖:" + df.format(Float.valueOf(item.getCommission() == null ? "0" : item.getCommission()) / 100) + "元");
        comm = df.format(Float.valueOf(item.getCommission() == null ? "0" : item.getCommission()) / 100);

//            holder.setText(R.id.tx4, "奖:"+ item.getCommission() + "元");
//        if (Long.valueOf(item.getSold_quantity()) < 1000) {
        holder.setText(R.id.tx5, "销量:" + item.getSales_tip());
//        } else {
//            holder.setText(R.id.tx5, "销量:" + df.format(Long.valueOf(item.getSold_quantity()) / 10000) + "万");
//        }
        holder.getView(R.id.tv_delete).setTag(position);
        holder.getView(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDeleteClickListener != null) {
                    mDeleteClickListener.onDeleteClick(v, (Integer) v.getTag());
                }
            }
        });
//        }
    }

    public void setOnDeleteClickListener(ShopRecyclerAdapter.OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }
}
