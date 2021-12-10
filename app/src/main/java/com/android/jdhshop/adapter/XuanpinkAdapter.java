package com.android.jdhshop.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.Xuanpinkbean;
import com.android.jdhshop.common.SPUtils;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 开发者：陈飞
 * 时间:2018/7/26 14:34
 * 说明：子适配器
 */
public class XuanpinkAdapter extends CommonAdapter<Xuanpinkbean> {
    SpannableString spannableString;
    Drawable drawable;
    private String comm;
    DecimalFormat df=new DecimalFormat("0.00");
    public XuanpinkAdapter(Context context, int layoutId, List<Xuanpinkbean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Xuanpinkbean item, int position) {
        //判断是不是点击ai助理添加商品进来的
        String zhuaqu = SPUtils.getStringData(mContext, "zhuaqu", "");
        if(zhuaqu.equals("2")){
            holder.getView(R.id.zq_icon).setVisibility(View.VISIBLE);
        }
        holder.getView(R.id.zq_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestParams params1 = new RequestParams();
                params1.put("product_id_str", item.num_iid);
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
                                params.put("id", item.num_iid);
                                params.put("title", item.title);//商品标题
                                params.put("desc", item.title);//商品推荐描述
                                params.put("img", item.pict_url);//宣传图片
                                params.put("price", item.zk_final_price);//商品原价
                                params.put("org_price", "");//商品拼团价格（接口对应字段price_pg）
                                params.put("after_price", "");//券后/活动/折扣后最终价格（接口对应字段price_after）
                                params.put("commission","0.15");//佣金
                                params.put("ts_time", "");//商品推荐描述
                                params.put("ts_time",item.coupon_start_time);//券开始时间
                                params.put("te_time",item.coupon_end_time);//券开始时间
                                params.put("linkurl",item.coupon_click_url);//推广购买链接
                                params.put("descurl", "");//详情页面链接
                                params.put("discount", item.coupon_amount+"");
                                params.put("shopname",item.shop_title);
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
                                            Toast.makeText(mContext, msg+"", Toast.LENGTH_SHORT).show();
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
                            // Log.d("成功成功成功", "成功成功成功");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        //设置图片
        ImageView imageView = holder.getView(R.id.image);
        Glide.with(mContext).load(item.pict_url+"_310x310.jpg").placeholder(R.drawable.no_banner).dontAnimate().into(imageView);
        ((TextView)holder.getView(R.id.title_child)).setText(item.title);
        //券后
        holder.setText(R.id.tx2, "¥" + df.format(Double.valueOf(item.zk_final_price)- Double.parseDouble(item.coupon_amount)) );
        //券后2
        TextView tx2_2 = holder.getView(R.id.tx2_2);
        //设置删除线
        tx2_2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tx2_2.setText( item.zk_final_price);
//        TextView tvhehe = holder.getView( R.id.hehejiage );
//        tvhehe.setText("升级分享最高可赚:¥" + df.format((Double.valueOf(item.zk_final_price)- Double.parseDouble(item.coupon_amount))*Double.parseDouble( item.tk_rate )/100* Double.parseDouble(df.format((float) SPUtils.getIntData(mContext, "rate", 0) / 100))) );
        //内部
        TextView tx3 = holder.getView(R.id.tx3);
        tx3.setText(item.coupon_amount + "元");
        holder.setText(R.id.tx4, "预估省:"+df.format((Double.valueOf(item.zk_final_price)- Double.parseDouble(item.coupon_amount))*Double.parseDouble( item.tk_rate )/100* Double.parseDouble(df.format((float) SPUtils.getIntData(mContext, "rate", 0) / 100))+Double.valueOf(item.coupon_amount)));
        holder.setText(R.id.tx5, "已售:"+item.volume);
    }
}
