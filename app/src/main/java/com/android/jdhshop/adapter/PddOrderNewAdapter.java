package com.android.jdhshop.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jdhshop.common.LogUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.DialogActivity;
import com.android.jdhshop.activity.PddDetailsActivity;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.PDDBean;
import com.android.jdhshop.bean.PddOrderBean;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.my.MyOrderActivity;
import com.android.jdhshop.widget.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by yohn on 2018/9/19.
 */

public class PddOrderNewAdapter extends BaseAdapter {
    private Context context;
    private List<PddOrderBean> dataList = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("0.00");
    final LoadingDialog loadingDialog;
    public PddOrderNewAdapter(Context context) {
        this.context = context;
        loadingDialog= LoadingDialog.createDialog(context);;
        loadingDialog.setMessage("查询中...");
        loadingDialog.setCanceledOnTouchOutside(false);
    }

    public void setData(List<PddOrderBean> dataList) {
        if (dataList == null) {
            this.dataList = new ArrayList<>();
        } else {
            this.dataList = dataList;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order_new, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_stu = (TextView) convertView.findViewById(R.id.tv_stu);
            holder.title_child = (TextView) convertView.findViewById(R.id.title_child);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_one = (TextView) convertView.findViewById(R.id.tv_one);
            holder.tv_two = (TextView) convertView.findViewById(R.id.tv_two);
            holder.txt_code = (TextView) convertView.findViewById(R.id.order_num);
            holder.tv_three = (TextView) convertView.findViewById(R.id.tv_three);
            holder.txt_copy1 = (TextView) convertView.findViewById(R.id.txt_copy1);
            holder.shop_image = (ImageView) convertView.findViewById(R.id.shop_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
     final    PddOrderBean model = dataList.get(position);
        holder.tv_name.setText("");
        LogUtils.d("tahn",model.getOrder_status());
//        淘客订单状态，3：订单结算，12：订单付款， 13：订单失效，14：订单成功
        if ("5".equalsIgnoreCase(model.getOrder_status())) {
            holder.tv_stu.setText("已结算");
        } else if ("0".equalsIgnoreCase(model.getOrder_status())) {
            holder.tv_stu.setText("已付款");
        } else if ("4".equalsIgnoreCase(model.getOrder_status())) {
            holder.tv_stu.setText("失效");
        } else if ("1".equalsIgnoreCase(model.getOrder_status())) {
            holder.tv_stu.setText("已成团");
        } else if ("2".equalsIgnoreCase(model.getOrder_status())) {
            holder.tv_stu.setText("已收货");
        } else if ("-1".equalsIgnoreCase(model.getOrder_status())) {
            holder.tv_stu.setText("未支付");
        }else if("3".equalsIgnoreCase(model.getOrder_status())){
            holder.tv_stu.setText("审核成功");
        }
        holder.txt_code.setText("订单号:"+model.getOrder_sn());
        holder.title_child.setText(model.getGoods_name());
        holder.tv_date.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date((long) (Double.valueOf(model.getOrder_create_time()) * 1000L))));
        if (model.getPromotion_amount() != null)
            holder.tv_one.setText(df.format(Float.valueOf(model.getPromotion_amount()) / 100));
        if (model.getOrder_amount() != null)
            holder.tv_two.setText(df.format(Float.valueOf(model.getOrder_amount()) / 100));
        if (model.getPromotion_rate() != null)
            if (!TextUtils.isEmpty(model.getPromotion_rate())) {
                StringBuffer sb = new StringBuffer();
                sb.append(df.format(Float.valueOf(model.getPromotion_rate()) / 10)).append("%");
                holder.tv_three.setText(sb.toString());
            } else {
                holder.tv_three.setText("0%");
            }
        else
            holder.tv_three.setText("0%");
        if(MyOrderActivity.CURRENT_ORDER_TYPE>1){
            holder.tv_three.setVisibility(View.INVISIBLE);
        }else{
            holder.tv_three.setVisibility(View.VISIBLE);
        }
        if (model.getGoods_thumbnail_url() != null && model.getGoods_thumbnail_url().startsWith("http")) {
            Glide.with(context).load(model.getGoods_thumbnail_url()).dontAnimate().into(holder.shop_image);
        } else {
            Glide.with(context).load(Constants.APP_IP + model.getGoods_thumbnail_url()).placeholder(R.drawable.no_banner).dontAnimate().into(holder.shop_image);
        }
        holder.txt_copy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(model.getOrder_sn());
                T.showShort(context, "订单号已复制");
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPddDetail( dataList.get(position).getGoods_sign());
            }
        });
        return convertView;
    }

    // 声明缓存类
    static class ViewHolder {
        TextView tv_name, tv_date, title_child, tv_stu, tv_one, tv_two, tv_three,txt_code,txt_copy1;//值
        ImageView shop_image;//
    }
    /**
     * @属性:获取拼多多商品详情
     * @开发者:wmm
     * @时间:2018/11/22 9:00
     */
    private void getPddDetail(final String goods_id) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("goods_id", goods_id);
        HttpUtils.post(Constants.GET_PDD_DETAIL, context,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if(responseString!=null){
                    T.showShort(context,responseString);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                loadingDialog.dismiss();
            }

            @Override
            public void onStart() {
                super.onStart();
                loadingDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    if (new JSONObject(responseString).getInt("code") != 0) {
                        T.showShort(context,"暂未查到该商品，或已下架");
                        return;
                    }
                    DecimalFormat df = new DecimalFormat("0.00");
                    JSONObject object = new JSONObject(responseString).getJSONObject("data").getJSONObject("goods_details");
                    Intent intent = new Intent(context, PddDetailsActivity.class);
                    Gson gson = new Gson();
                    double tem = (Double.valueOf(object.getString("min_group_price")) - Double.valueOf(object.getString("coupon_discount"))) * Double.valueOf(df.format(Double.valueOf(object.getString("promotion_rate")) / 1000));
                    object.put("commission", df.format(tem * SPUtils.getIntData(context, "rate", 0) / 100));
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("goods", gson.fromJson(object.toString().replace("goods_gallery_urls", "imagss"), PDDBean.class));
                    intent.putExtra("goods", bundle);
                    context.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
