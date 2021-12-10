package com.android.jdhshop.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jdhshop.common.LogUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.JdDetailsActivity;
import com.android.jdhshop.bean.JdOrderBean;
import com.android.jdhshop.bean.MyGoodsResp;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.utils.OkHttpUtils;
import com.android.jdhshop.widget.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jd.union.open.goods.query.request.GoodsReq;
import jd.union.open.goods.query.request.UnionOpenGoodsQueryRequest;
import jd.union.open.goods.query.response.UnionOpenGoodsQueryResponse;
import okhttp3.Call;
import okhttp3.Callback;

/**
 * Created by yohn on 2018/9/19.
 */

public class JdOrderNewAdapter extends BaseAdapter {
    private Context context;
    private List<JdOrderBean> dataList = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("0.00");
    private LoadingDialog loadingDialog;
    public JdOrderNewAdapter(Context context) {
        this.context = context;
        loadingDialog= LoadingDialog.createDialog(context);
        loadingDialog.setMessage("查询中...");
        loadingDialog.setCanceledOnTouchOutside(false);
    }

    public void setData(List<JdOrderBean> dataList) {
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
            holder.tv_name =  convertView.findViewById(R.id.tv_name);
            holder.tv_stu = convertView.findViewById(R.id.tv_stu);
            holder.title_child =  convertView.findViewById(R.id.title_child);
            holder.tv_date =convertView.findViewById(R.id.tv_date);
            holder.tv_one =  convertView.findViewById(R.id.tv_one);
            holder.tv_two = convertView.findViewById(R.id.tv_two);
            holder.txt_copy1 = (TextView) convertView.findViewById(R.id.txt_copy1);
            holder.txt_code = convertView.findViewById(R.id.order_num);
            holder.tv_three =  convertView.findViewById(R.id.tv_three);
            holder.shop_image = convertView.findViewById(R.id.shop_image);
            holder.tv_one_d = convertView.findViewById(R.id.tv_one_d);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
       final JdOrderBean model = dataList.get(position);
        holder.tv_name.setText("");
        if ("18".equalsIgnoreCase(model.getOrder_status())||"17".equalsIgnoreCase(model.getOrder_status())) {
            holder.tv_stu.setText("已结算");
        } else if ("16".equalsIgnoreCase(model.getOrder_status())) {
            holder.tv_stu.setText("已付款");
        } else if ("15".equalsIgnoreCase(model.getOrder_status())) {
            holder.tv_stu.setText("待付款");
        } else if(!"17".equalsIgnoreCase(model.getOrder_status())){
            holder.tv_stu.setText("失效");
        }
        holder.txt_code.setText("订单号:"+model.getOrder_sn());
        holder.title_child.setText(model.getGoods_name());
        holder.tv_date.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.valueOf(model.getOrder_time()))));
        if ("15".equalsIgnoreCase(model.getOrder_status())){
            holder.tv_one_d.setVisibility(View.INVISIBLE);
            holder.tv_one.setText(".....");
        }else {
            holder.tv_one.setText(df.format(Float.valueOf(model.getCommission()) / 100) + "");
        }
        if (model.getPay_price() != null)
            holder.tv_two.setText(model.getPay_price());
        if (model.getGoods_img() != null && model.getGoods_img().startsWith("http")) {
            Glide.with(context).load(model.getGoods_img()).dontAnimate().into(holder.shop_image);
        } else {
            Glide.with(context).load(Constants.APP_IP + model.getGoods_img()).placeholder(R.drawable.no_banner).dontAnimate().into(holder.shop_image);
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
                getJdGoodsRequest(dataList.get(position).getSkuid());
            }
        });
        return convertView;
    }
    /**
     * @属性:获取京东推送商品详情
     * @开发者:wmm
     * @时间:2018/12/11 9:50
     */
    private void getJdGoodsRequest(String id) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("apikey", Constants.JD_APP_KEY_NEW);
        requestParams.put("goods_ids", id);
        requestParams.put("isunion", "1");
        OkHttpUtils.getInstance().get(Constants.JDNEWGOODS_LIST + "?" + requestParams.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d("dsfasdf", e.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String s = response.body().string();
                LogUtils.d("dsfasdf", s);
                try {
                    JSONObject object1 = new JSONObject(s);
                    JSONArray array = object1.getJSONObject("data").getJSONArray("data");
                    if (array == null || array.length() == 0) {
                        handlers.sendEmptyMessage(0);
                        return;
                    }
                    for (int i = 0; i < 1; i++) {
                        MyGoodsResp resp = new Gson().fromJson(array.getJSONObject(i).toString(), MyGoodsResp.class);
                        handlers.sendEmptyMessage(1);
                        Intent intent = new Intent(context, JdDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("goods", resp);
                        intent.putExtra("goods", bundle);
                        context.startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private Handler handlers= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            loadingDialog.dismiss();
            if(msg.what==1){
            }else {
                T.showShort(context,"暂未查到该商品，或已下架");
            }
            super.handleMessage(msg);
        }
    };
    // 声明缓存类
    static class ViewHolder {
        TextView tv_name, tv_date, title_child, tv_stu, tv_one, tv_two, tv_three,txt_code,txt_copy1,tv_one_d;//值
        ImageView shop_image;//
    }
}
