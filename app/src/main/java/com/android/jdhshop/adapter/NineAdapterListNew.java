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
import com.android.jdhshop.bean.HaoDanBean;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.common.SPUtils;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class NineAdapterListNew extends CommonAdapter<HaoDanBean> {
    Drawable drawable;
    DecimalFormat df=new DecimalFormat("0.00");
    public NineAdapterListNew(Context context, int layoutId, List<HaoDanBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final HaoDanBean item, int position) {
        //判断是不是点击ai助理添加商品进来的
        String zhuaqu = SPUtils.getStringData(mContext, "zhuaqu", "");
        if(zhuaqu.equals("2")){
            holder.getView(R.id.zq_icon).setVisibility(View.VISIBLE);
        }
        holder.getView(R.id.zq_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestParams params1 = new RequestParams();
                params1.put("product_id_str", item.itemid);
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
                                params.put("id", item.itemid);
                                params.put("title", item.itemtitle);//商品标题
                                params.put("desc", item.itemdesc);//商品推荐描述
                                params.put("img", item.itempic);//宣传图片
                                params.put("price", item.itemprice);//商品原价
                                params.put("org_price", "");//商品拼团价格（接口对应字段price_pg）
                                params.put("after_price", item.itemendprice);//券后/活动/折扣后最终价格（接口对应字段price_after）
                                params.put("commission",item.commission );//佣金
                                params.put("ts_time", "");//商品推荐描述
                                params.put("ts_time",item.couponstarttime);//券开始时间
                                params.put("te_time",item.couponendtime);//券开始时间
                                params.put("linkurl",item.couponurl);//推广购买链接
                                params.put("descurl", "");//详情页面链接
                                params.put("discount", item.couponmoney+"");
                                params.put("shopname",item.shopname);
                                HttpUtils.post(Constants.getAddShngPin, mContext,params, new TextHttpResponseHandler() {
                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                                    }

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                        try {
                                            JSONObject jsonObject = new JSONObject( responseString );
                                            //showToast("成功");
                                            Toast.makeText(mContext, "添加成功", Toast.LENGTH_SHORT).show();
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

        ImageView imageView = holder.getView(R.id.image);
        Glide.with(mContext).load(item.itempic+"_310x310.jpg").placeholder(R.drawable.no_banner).override(CommonUtils.getScreenWidth()/2,180).dontAnimate().into(imageView);
        ((TextView)holder.getView(R.id.title_child)).setText(item.itemtitle);
     //券后2
        TextView tx2_2 = holder.getView(R.id.tx2_2);
        //设置删除线
        tx2_2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tx2_2.setText(item.itemprice);
        //内部
        TextView tx3 = holder.getView(R.id.tx3);
        tx3.setText(item.couponmoney);
        //券后
        holder.setText(R.id.tx2, "¥" +item.itemendprice);
        holder.setText(R.id.tx4, "奖:"+df.format(Double.valueOf(item.tkmoney)* Double.parseDouble(df.format((float) SPUtils.getIntData(mContext, "rate", 0) / 100))));
        holder.setText(R.id.tx5,"月售"+item.itemsale);
        if(Double.valueOf(item.videoid)>0){
            holder.getView(R.id.play_img).setVisibility(View.VISIBLE);
        }else{
            holder.getView(R.id.play_img).setVisibility(View.GONE);
        }
    }
}
