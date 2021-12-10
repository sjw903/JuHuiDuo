package com.android.jdhshop.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.SearchNewCommonActivity;
import com.android.jdhshop.activity.SearchResultActivity;
import com.android.jdhshop.bean.TaobaoGuestBean;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.my.CollectionActivity;
import com.android.jdhshop.utils.StringUtils;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 开发者：陈飞
 * 时间:2018/7/26 15:54
 * 说明：
 */
public class ShopRecyclerAdapter2 extends CommonAdapter<TaobaoGuestBean.TaobaoGuesChildtBean> {
    Drawable drawable;
    private OnDeleteClickLister mDeleteClickListener;
    private String comm;
    public ShopRecyclerAdapter2(Context context, int layoutId, List<TaobaoGuestBean.TaobaoGuesChildtBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, TaobaoGuestBean.TaobaoGuesChildtBean item, int position) {

        String zhuaqu = SPUtils.getStringData(mContext, "zhuaqu", "");
        //判断是不是点击ai助理添加商品进来的
        if(zhuaqu.equals("2")){
            holder.itemView.findViewById(R.id.zq_icon).setVisibility(View.VISIBLE);
        }
        holder.itemView.findViewById(R.id.zq_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params1 = new RequestParams();
                params1.put("product_id_str", item.getNum_iid());
                HttpUtils.post(Constants.getJianCHa, mContext,params1, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        try {
                            JSONObject jsonObject = new JSONObject( responseString );
                            String string = jsonObject.getString("list");
                            if(string.equals("[]")){
                                String huoquaddid = SPUtils.getStringData(mContext, "huoquaddid", "");
                                String replace = huoquaddid.replaceAll("\\[", "");
                                String s = replace.replaceAll("]", "");
                                RequestParams params = new RequestParams();
                                params.put("gid", s);
                                params.put("pid", Constants.PLATFORM_TB);
                                params.put("id", item.getNum_iid());
                                params.put("title", item.getTitle());//商品标题
                                params.put("desc", item.getTitle());//商品推荐描述
                                params.put("img", item.getPic_url());//宣传图片
                                params.put("price", item.getZk_final_price());//商品原价
                                params.put("org_price", "");//商品拼团价格（接口对应字段price_pg）
                                params.put("after_price", String.format("%.2f", (StringUtils.doStringToDouble(item.getZk_final_price()) - StringUtils.doStringToDouble(item.getCoupon_amount()))));//券后/活动/折扣后最终价格（接口对应字段price_after）
                                params.put("commission",comm );//佣金
                                params.put("ts_time", "");//商品推荐描述
                                params.put("ticket_start_time","");//券开始时间
                                params.put("ticket_end_time","");//券开始时间
                                params.put("linkurl",item.getClick_url());//推广购买链接
                                params.put("descurl", item.getItem_url());//详情页面链接
                                params.put("discount", item.getCoupon_amount()+"");
                                params.put("shopname",item.getNick());
                                HttpUtils.post(Constants.getAddShngPin, mContext,params, new TextHttpResponseHandler() {
                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                                    }

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                        try {
                                            JSONObject jsonObject = new JSONObject( responseString );
                                            String msg = jsonObject.getString("msg");
                                            //showToast("成功");
                                            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                            Log.d("成功成功成功", "成功成功成功");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(mContext, "已有该商品", Toast.LENGTH_SHORT).show();
                            }
                            //showToast("成功");

                            Log.d("成功成功成功", "成功成功成功");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });
        //设置图片
        ImageView imageView = holder.getView(R.id.image);
        Glide.with(mContext).load(item.getPict_url()).placeholder(R.drawable.no_banner).dontAnimate().into(imageView);

        ((TextView) holder.getView(R.id.title_child)).setText(item.getTitle());
        //券后
        holder.setText(R.id.tx2, "¥" + String.format("%.2f", (StringUtils.doStringToDouble(item.getZk_final_price()) - StringUtils.doStringToDouble(item.getCoupon_amount()))));
        //券后2
        TextView tx2_2 = holder.getView(R.id.tx2_2);
        //设置删除线
        tx2_2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tx2_2.setText(item.getZk_final_price());
        //内部
        TextView tx3 = holder.getView(R.id.tx3);
        TextView tx2 = holder.getView(R.id.tx2);
        tx3.setText(item.getCoupon_amount()==null?"0":item.getCoupon_amount().replace(".00", ""));
        try {
            if(mContext instanceof SearchNewCommonActivity||mContext instanceof SearchResultActivity){
                holder.setText(R.id.tx4, "预估赚" + String.format("%.2f",  Double.valueOf(item.getCommission_rate())*SPUtils.getIntData(mContext,"rate",0)/1000000*Double.valueOf(tx2.getText().toString().replace("¥",""))));
                holder.setText(R.id.tx5,"已售"+item.getVolume());
                comm=String.format("%.2f",  Double.valueOf(item.getCommission_rate())*SPUtils.getIntData(mContext,"rate",0)/1000000*Double.valueOf(tx2.getText().toString().replace("¥","")));
            }else{
                holder.setText(R.id.tx4, "预估赚" + item.getCommission());
                holder.setText(R.id.tx5,"已售"+item.getVolume());
                comm=item.getCommission()+"";
            }
        } catch (Exception e) {
            holder.setText(R.id.tx4, "预估赚0");
        }
        if(mContext instanceof CollectionActivity){
            holder.getView(R.id.tv_delete).setTag(position);
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

    public void setOnDeleteClickListener(OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }
}
