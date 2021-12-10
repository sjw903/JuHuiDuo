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
import com.android.jdhshop.bean.HaoDanBean;
import com.android.jdhshop.common.SPUtils;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.android.jdhshop.config.Constants.PLATFORM_TB;

public class HomeDouAdapter extends BaseQuickAdapter<HaoDanBean, BaseViewHolder> {
    DecimalFormat df=new DecimalFormat("0.00");
    public HomeDouAdapter(int layoutResId, @Nullable List<HaoDanBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, HaoDanBean item) {
        //设置图片
        String zhuaqu = SPUtils.getStringData(mContext, "zhuaqu", "");
        //判断是不是点击ai助理添加商品进来的
        if(zhuaqu.equals("2")){
            holder.itemView.findViewById(R.id.zq_icon).setVisibility(View.VISIBLE);
        }
        holder.itemView.findViewById(R.id.zq_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params1 = new RequestParams();
                params1.put("product_id_str", item.itemid);
                HttpUtils.post(Constants.getJianCHa,mContext, params1, new TextHttpResponseHandler() {
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
                                params.put("pid", PLATFORM_TB);
                                params.put("id", item.itemid);
                                params.put("title", item.itemtitle);//商品标题
                                params.put("desc", item.itemdesc);//商品推荐描述
                                params.put("img", item.itempic);//宣传图片
                                params.put("price", item.itemprice);//商品原价
                                params.put("org_price", "");//商品拼团价格（接口对应字段price_pg）
                                params.put("after_price", item.itemendprice);//券后/活动/折扣后最终价格（接口对应字段price_after）
                                params.put("commission",item.commission+"");//佣金
                                params.put("ts_time", "");//商品推荐描述
                                params.put("ticket_start_time","");//券开始时间
                                params.put("ticket_end_time","");//券开始时间
                                params.put("linkurl","");//推广购买链接
                                params.put("descurl", "");//详情页面链接
                                params.put("discount", item.couponmoney);
                                params.put("shopname",item.shopname);
                                HttpUtils.post(Constants.getAddShngPin,mContext, params, new TextHttpResponseHandler() {
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
                                            //Log.d("成功成功成功", "成功成功成功");
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
        ImageView imageView = holder.getView(R.id.image);
        Glide.with(mContext).load(item.itempic+"_310x310.jpg").placeholder(R.drawable.no_banner).dontAnimate().into(imageView);
        ((TextView)holder.getView(R.id.title_child)).setText(item.itemtitle);
        //券后
        holder.setText(R.id.tx2, "¥" + item.itemendprice);
        //券后2
        TextView tx2_2 = holder.getView(R.id.tx2_2);
        //设置删除线
        tx2_2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tx2_2.setText( item.itemprice);
        //内部
        TextView tx3 = holder.getView(R.id.tx3);
        tx3.setText(item.couponmoney + "元");

        /*  首页抖券 */
        String zhuan_title = "";
//        if ("".equals(SPUtils.getStringData(mContext, "token", ""))){
//            zhuan_title = "最高赚:";
//        }
//        else
//        {
//            zhuan_title = "预估赚:";
//        }
        if (Double.valueOf(item.tkmoney)>0){
            TextView txv4 = holder.getView(R.id.tx4);
            txv4.setBackgroundResource(R.drawable.bg_txt_taobao);
            zhuan_title = "现金红包";
        }
        holder.setText(R.id.tx4, zhuan_title);
//        holder.setText(R.id.tx4, zhuan_title +df.format(Double.valueOf(item.tkmoney)* Double.parseDouble(df.format((float) SPUtils.getIntData(mContext, "rate", 0) / 100))));
        holder.setText(R.id.tx5, "已售:"+item.itemsale);
    }
    private void addAi(){

    }
}
