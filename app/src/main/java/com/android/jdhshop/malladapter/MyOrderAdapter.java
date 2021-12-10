package com.android.jdhshop.malladapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.jdhshop.common.LogUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.R;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.mall.ApplyDrawBackActivity;
import com.android.jdhshop.mall.ExpressActivity;
import com.android.jdhshop.mall.FillNumActivity;
import com.android.jdhshop.mall.MyShopMallOrderActivity;
import com.android.jdhshop.mall.PayOrderMoneyActivity;
import com.android.jdhshop.mall.ShopMallOrderDetailActivity;
import com.android.jdhshop.mallbean.OrderDetailBean;
import com.android.jdhshop.widget.LoadingDialog;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import me.drakeet.materialdialog.MaterialDialog;

public class MyOrderAdapter extends CommonAdapter<OrderDetailBean.OrderMsg> {
    private MaterialDialog materialDialog;
    private LoadingDialog loadingDialog;

    public MyOrderAdapter(Context context, int layoutId, List<OrderDetailBean.OrderMsg> datas) {
        super(context, layoutId, datas);
        loadingDialog = new LoadingDialog(context);
    }

    @Override
    protected void convert(ViewHolder holder, final OrderDetailBean.OrderMsg orderDetailBean, final int position) {
        LogUtils.d("dfasdf",orderDetailBean.toString());
        holder.setText(R.id.txt_order_num, "订单编号:" + orderDetailBean.order_num);
        holder.getView(R.id.txt_pay).setVisibility(View.GONE);
        holder.getView(R.id.txt_reason).setVisibility(View.GONE);
        holder.getView(R.id.txt_cancle).setVisibility(View.GONE);
        holder.getView(R.id.txt_apply_cancle).setVisibility(View.GONE);
        holder.getView(R.id.txt_ok_shouhuo).setVisibility(View.GONE);
        holder.getView(R.id.txt_comment).setVisibility(View.GONE);
        holder.getView(R.id.txt_danhao).setVisibility(View.GONE);
        holder.getView(R.id.txt_wuliu).setVisibility(View.GONE);
        switch (orderDetailBean.status) {
            case "1":
                holder.setText(R.id.txt_status, "待支付");
                holder.getView(R.id.txt_pay).setVisibility(View.VISIBLE);
                holder.getView(R.id.txt_cancle).setVisibility(View.VISIBLE);
                break;
            case "2":
                holder.setText(R.id.txt_status, "待发货");
                holder.getView(R.id.txt_apply_cancle).setVisibility(View.VISIBLE);
                break;
            case "3":
                holder.setText(R.id.txt_status, "待收货");
                holder.getView(R.id.txt_ok_shouhuo).setVisibility(View.VISIBLE);
                holder.getView(R.id.txt_wuliu).setVisibility(View.VISIBLE);
//                holder.getView(R.id.txt_apply_cancle).setVisibility(View.VISIBLE);
//                holder.setText(R.id.txt_reason,"快递单号:"+orderDetailBean.express_number);
                break;
            case "4":
                holder.setText(R.id.txt_status, "已完成");
//                holder.getView(R.id.txt_comment).setVisibility(View.VISIBLE);
                break;
            case "5":
                holder.setText(R.id.txt_status, "已完成");
                break;
            case "6":
                holder.setText(R.id.txt_status, "申请退款中...");
                holder.getView(R.id.txt_reason).setVisibility(View.VISIBLE);
                holder.setText(R.id.txt_reason,"退款原因: "+orderDetailBean.drawback_reason);
                break;
            case "7":
                if(orderDetailBean.refund_express_number!=null&&!"".equals(orderDetailBean.refund_express_number)){
                    holder.setText(R.id.txt_status, "退货中，待退款...");
                    holder.getView(R.id.txt_wuliu).setVisibility(View.VISIBLE);
                }else{
                    holder.setText(R.id.txt_status, "待退货...");
                    holder.getView(R.id.txt_danhao).setVisibility(View.VISIBLE);
                }
                break;
            case "8":
                holder.setText(R.id.txt_status, "拒绝退款");
                holder.getView(R.id.txt_reason).setVisibility(View.VISIBLE);
                holder.setText(R.id.txt_reason,"拒绝原因: "+orderDetailBean.drawback_refuse_reason);
                holder.getView(R.id.txt_apply_cancle).setVisibility(View.VISIBLE);
                break;
            case "9":
                holder.setText(R.id.txt_status, "订单失效");
                break;
        }
        RecyclerView recyclerView=holder.getView(R.id.read_data);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        MyDetail myDetail=new MyDetail(R.layout.item_order_detail,orderDetailBean.detail);
        recyclerView.setAdapter(myDetail);
        myDetail.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("order",orderDetailBean);
                Intent intent=new Intent(mContext, ShopMallOrderDetailActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
         holder.getView(R.id.txt_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancleOrder(position);
            }
        });
        holder.getView(R.id.txt_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PayOrderMoneyActivity.class);
                intent.putExtra("money", orderDetailBean.allprice);
                intent.putExtra("order_num", orderDetailBean.order_num);
                intent.putExtra("order_id", orderDetailBean.detail.get(0).order_id);
                intent.putExtra("name",orderDetailBean.detail.get(0).goods_name);
                mContext.startActivity(intent);
            }
        });
        holder.getView(R.id.txt_apply_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("order_num", orderDetailBean.order_num);
                bundle.putString("order_id", orderDetailBean.id);
                Intent intent = new Intent(mContext, ApplyDrawBackActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        holder.getView(R.id.txt_ok_shouhuo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog = new MaterialDialog(mContext);
                materialDialog.setTitle("确认收货")
                        .setMessage("确认已收到商品吗?")
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                materialDialog.dismiss();
                            }
                        }).setPositiveButton("确认收货", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        okShouHuo(position);
                        materialDialog.dismiss();
                    }
                }).setCanceledOnTouchOutside(true)
                        .show();
            }
        });
        holder.getView(R.id.txt_wuliu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, ExpressActivity.class);
                if(orderDetailBean.status.equals("7")){
                    intent.putExtra("number",orderDetailBean.refund_express_number);
                }else{
                    intent.putExtra("number",orderDetailBean.express_number);
                }
                mContext.startActivity(intent);
            }
        });
        holder.getView(R.id.txt_danhao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final EditText editText=new EditText(mContext);
//                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
//                materialDialog=  new MaterialDialog(mContext).setContentView(
//                        editText).setTitle("请输入退款商品的快递单号").setNegativeButton("取消", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        materialDialog.dismiss();
//                    }
//                }).setPositiveButton("确定", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        submit(editText.getText().toString().trim(),position);
//                        materialDialog.dismiss();
//                    }
//                });
//                materialDialog.show();
                Bundle bundle=new Bundle();
                bundle.putString("id",orderDetailBean.id);
                Intent intent = new Intent(mContext, FillNumActivity.class);
                if (bundle != null) {
                    intent.putExtras(bundle);
                }
                mContext.startActivity(intent);
            }
        });
    }
    private void submit(String str,final int position){
        if(TextUtils.isEmpty(str)){
            T.showShort(mContext,"快递单号未填写");
            return;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("express_number", str);
        requestParams.put("order_id", mDatas.get(position).detail.get(0).order_id);
        HttpUtils.post(Constants.EXPRESS_DANHAO, mContext,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onStart() {
                super.onStart();
                loadingDialog.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                loadingDialog.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    String msg = jsonObject.optString("msg");
                    if (jsonObject.getInt("code") == 0) {
                        ShouHuoHandle.sendEmptyMessage(position);
                    }
                    T.showShort(mContext, msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void okShouHuo(final int position){
        RequestParams requestParams = new RequestParams();
        requestParams.put("order_id", mDatas.get(position).detail.get(0).order_id);
        HttpUtils.post("1".equals(Constants.MALL_ORDER_TYPE)?Constants.CONFIRM_ORDER_UPDATE:Constants.CONFIRM_ORDER, mContext,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onStart() {
                super.onStart();
                loadingDialog.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                loadingDialog.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    String msg = jsonObject.optString("msg");
                    if (jsonObject.getInt("code") == 0) {
                        ShouHuoHandle.sendEmptyMessage(position);
                    }
                    T.showShort(mContext, msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void cancleOrder(final int position) {
        materialDialog = new MaterialDialog(mContext);
        materialDialog.setTitle("取消订单")
                .setMessage("确定取消该条订单吗")
                .setNegativeButton("立即取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestCancle(position);
                        materialDialog.dismiss();
                    }
                }).setPositiveButton("再想想", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        }).setCanceledOnTouchOutside(true)
                .show();
    }

    private void requestCancle(final int position) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("order_id", mDatas.get(position).detail.get(0).order_id);
        HttpUtils.post("1".equals(Constants.MALL_ORDER_TYPE)?Constants.CANCLE_MALL_ORDER_UPDATE:Constants.CANCLE_MALL_ORDER, mContext,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onStart() {
                super.onStart();
                loadingDialog.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                loadingDialog.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    String msg = jsonObject.optString("msg");
                    if (jsonObject.getInt("code") == 0) {
                        delHandle.sendEmptyMessage(position);
                    }
                    T.showShort(mContext, msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private Handler ShouHuoHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mDatas.remove(msg.what);
            notifyDataSetChanged();
        }
    };
    private Handler delHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mDatas.remove(msg.what);
            notifyDataSetChanged();
        }
    };
    public class MyDetail extends BaseQuickAdapter<OrderDetailBean.OrderMsg.Detail, BaseViewHolder> {

        public MyDetail(int layoutResId, @Nullable List<OrderDetailBean.OrderMsg.Detail> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, OrderDetailBean.OrderMsg.Detail item) {

            ImageView img = holder.getView(R.id.img_shop);
            Glide.with(mContext).load(item.img.contains("http")?item.img: Constants.APP_IP + item.img).error(R.drawable.no_banner).dontAnimate().into(img);
            holder.setText(R.id.txt_name, item.goods_name);
            holder.setText(R.id.txt_num, "商品数量: " + item.num);
            holder.setText(R.id.txt_price,  item.price);
            holder.setText(R.id.txt_attribute,(item.sku_str==null||"".equals(item.sku_str))?"":Html.fromHtml(item.sku_str).toString());
            if((item.sku_str==null||"".equals(item.sku_str))){
                holder.getView(R.id.txt_attribute).setVisibility(View.GONE);
            }else {
                holder.getView(R.id.txt_attribute).setVisibility(View.VISIBLE);
            }
            if(mContext instanceof ShopMallOrderDetailActivity || mContext instanceof MyShopMallOrderActivity) {
                holder.getView(R.id.img_jia).setVisibility(View.GONE);
                holder.getView(R.id.img_jian).setVisibility(View.GONE);
            }
        }
    }
}
