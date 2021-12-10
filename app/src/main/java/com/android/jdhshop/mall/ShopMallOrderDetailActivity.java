package com.android.jdhshop.mall;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.jdhshop.common.LogUtils;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.malladapter.MyOrderDetailAdapter;
import com.android.jdhshop.mallbean.OrderDetailBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import me.drakeet.materialdialog.MaterialDialog;

public class ShopMallOrderDetailActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.txt_status)
    TextView txtStatus;
    @BindView(R.id.txt_cancle)
    TextView txtCancle;
    @BindView(R.id.txt_wuliu)
    TextView txtWuLiu;
    @BindView(R.id.txt_apply_cancle)
    TextView txtApplyCancle;
    @BindView(R.id.txt_danhao)
    TextView txtDanhao;
    @BindView(R.id.txt_pay)
    TextView txtPay;
    @BindView(R.id.txt_ok_shouhuo)
    TextView txtOkShouhuo;
    @BindView(R.id.txt_comment)
    TextView txtComment;
    @BindView(R.id.order_recy)
    RecyclerView orderRecy;
    @BindView(R.id.txt_price)
    TextView txtPrice;
    @BindView(R.id.txt_total_price)
    TextView txtTotalPrice;
    @BindView(R.id.txt_delivery_type)
    TextView txtDeliveryType;
    @BindView(R.id.txt_remark)
    TextView txtRemark;
    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.txt_order_num)
    TextView txtOrderNum;
    @BindView(R.id.txt_time)
    TextView txtTime;
    @BindView(R.id.txt_reason)
    TextView txtReason;
    @BindView(R.id.txt_pay_time)
    TextView txtPayTime;
    @BindView(R.id.txt_score)
    TextView txtScore;
    private OrderDetailBean.OrderMsg orderDetailBean;
    private MyOrderDetailAdapter adapter;
    private List<OrderDetailBean.OrderMsg.Detail> list = new ArrayList<>();
    private MaterialDialog materialDialog;
    @Override
    protected void initUI() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_orderdetail);
        ButterKnife.bind(this);
        orderDetailBean = (OrderDetailBean.OrderMsg) getIntent().getExtras().get("order");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        orderRecy.setLayoutManager(layoutManager);
        adapter = new MyOrderDetailAdapter(this, R.layout.item_order_detail, list);
        orderRecy.setAdapter(adapter);
        materialDialog = new MaterialDialog(this);
        getDetail();
    }

    @Override
    protected void initData() {
        switch (orderDetailBean.status) {
            case "1":
                txtStatus.setText("待支付");
                txtPay.setVisibility(View.VISIBLE);
                txtCancle.setVisibility(View.VISIBLE);
                break;
            case "2":
                txtStatus.setText("待发货");
                txtApplyCancle.setVisibility(View.VISIBLE);
                break;
            case "3":
                txtStatus.setText("待收货");
                txtOkShouhuo.setVisibility(View.VISIBLE);
                txtWuLiu.setVisibility(View.VISIBLE);
//                txtApplyCancle.setVisibility(View.VISIBLE);
                break;
            case "4":
                txtStatus.setText("订单已签收");
//                txtComment.setVisibility(View.VISIBLE);
                break;
            case "5":
                txtStatus.setText("已完成");
                break;
            case "6":
                txtStatus.setText("申请退款中...");
                txtReason.setText("退款原因: " + orderDetailBean.drawback_reason);
                txtReason.setVisibility(View.VISIBLE);
                break;
            case "7":
                if(orderDetailBean.refund_express_number!=null&&!"".equals(orderDetailBean.refund_express_number)){
                    txtStatus.setText("退货中，待退款...");
                    txtWuLiu.setVisibility(View.VISIBLE);

                }else{
                    txtStatus.setText("待退货...");
                    txtDanhao.setVisibility(View.VISIBLE);
                }
                break;
            case "8":
                txtStatus.setText("拒绝退款");
                txtApplyCancle.setVisibility(View.VISIBLE);
                txtReason.setText("拒绝原因: " + orderDetailBean.drawback_refuse_reason);
                txtReason.setVisibility(View.VISIBLE);
                Drawable drawable1 = getResources().getDrawable(R.mipmap.order_cancle);
                drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                txtStatus.setCompoundDrawables(null, drawable1, null, null);
                break;
            case "9":
                txtStatus.setText("订单已失效");
                txtReason.setText("失效原因: " + orderDetailBean.reject_reason);
                txtReason.setVisibility(View.VISIBLE);
                break;
        }
        txtCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.setTitle("取消订单")
                        .setMessage("确定取消该条订单吗")
                        .setNegativeButton("立即取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestCancle();
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
        });
        txtPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopMallOrderDetailActivity.this, PayOrderMoneyActivity.class);
                intent.putExtra("money", orderDetailBean.detail.get(0).allprice);
                intent.putExtra("order_num", orderDetailBean.order_num);
                intent.putExtra("order_id", orderDetailBean.detail.get(0).order_id);
                intent.putExtra("name",orderDetailBean.detail.get(0).goods_name);
                startActivity(intent);
            }
        });
        txtWuLiu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ShopMallOrderDetailActivity.this, ExpressActivity.class);
                if(orderDetailBean.status.equals("7")){
                    intent.putExtra("number",orderDetailBean.refund_express_number);
                }else{
                    intent.putExtra("number",orderDetailBean.express_number);
                }
                startActivity(intent);
            }
        });
        txtApplyCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("order_num", orderDetailBean.order_num);
                bundle.putString("order_id", orderDetailBean.id);
                Intent intent = new Intent(ShopMallOrderDetailActivity.this, ApplyDrawBackActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        txtOkShouhuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        okShouHuo();
                        materialDialog.dismiss();
                    }
                }).setCanceledOnTouchOutside(true)
                        .show();
            }
        });
        txtDanhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("id",orderDetailBean.id);
                openActivity(FillNumActivity.class,bundle);
//                final EditText editText=new EditText(ShopMallOrderDetailActivity.this);
//                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
//                materialDialog=  new MaterialDialog(ShopMallOrderDetailActivity.this).setContentView(
//                        editText).setTitle("请输入退款商品的快递单号").setNegativeButton("取消", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        materialDialog.dismiss();
//                    }
//                }).setPositiveButton("确定", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        submit(editText.getText().toString().trim());
//                        materialDialog.dismiss();
//                    }
//                });
//                materialDialog.show();
            }
        });
        txtTime.setText(orderDetailBean.create_time);
        txtOrderNum.setText(orderDetailBean.order_num);
        txtPayTime.setText(orderDetailBean.pay_time);
    }
    private void submit(String str){
        if(TextUtils.isEmpty(str)){
            T.showShort(this,"快递单号未填写");
            return;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("express_number", str);
        requestParams.put("order_id", orderDetailBean.id);
        HttpUtils.post(Constants.EXPRESS_DANHAO, ShopMallOrderDetailActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    String msg = jsonObject.optString("msg");
                    if (jsonObject.getInt("code") == 0) {
                        txtStatus.setText("待退款");
                        txtDanhao.setVisibility(View.GONE);
                    }
                    showToast(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void okShouHuo() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("order_id", orderDetailBean.detail.get(0).order_id);
        HttpUtils.post(Constants.CONFIRM_ORDER, ShopMallOrderDetailActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    String msg = jsonObject.optString("msg");
                    if (jsonObject.getInt("code") == 0) {
                        showToast("收货成功");
                        txtStatus.setText("已收货");
                        txtApplyCancle.setVisibility(View.VISIBLE);
                        txtComment.setVisibility(View.VISIBLE);
                        txtOkShouhuo.setVisibility(View.GONE);
                    } else {
                        showToast(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void requestCancle() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("order_id", orderDetailBean.detail.get(0).order_id);
        HttpUtils.post(Constants.CANCLE_MALL_ORDER,ShopMallOrderDetailActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    String msg = jsonObject.optString("msg");
                    if (jsonObject.getInt("code") == 0) {
                        showToast("取消成功");
                        finish();
                    } else {
                        showToast(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getDetail() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("order_id", orderDetailBean.detail.get(0).order_id);
        LogUtils.d("dsfasd",requestParams.toString());
        HttpUtils.post("1".equals(Constants.MALL_ORDER_TYPE)?Constants.GET_ORDER_DETAIL_UPDATE:Constants.GET_ORDER_DETAIL, ShopMallOrderDetailActivity.this,requestParams, new onOKJsonHttpResponseHandler<OrderDetailBean>(new TypeToken<Response<OrderDetailBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogUtils.d("fewefsd",responseString);
            }

            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, final Response<OrderDetailBean> datas) {
                if (datas.isSuccess()) {
                    LogUtils.d("fewefsd",datas.getData().orderMsg.toString());
                    list.addAll(datas.getData().orderMsg.detail);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtRemark.setText(datas.getData().orderMsg.remark);
                            txtTotalPrice.setText( datas.getData().orderMsg.allprice+" 积分");
                            txtPrice.setText(datas.getData().orderMsg.detail.get(0).allprice);
//                            txtDeliveryType.setText(("".equals(datas.getData().list.get(0).postage) || "0".equals(datas.getData().orderMsg.postage)) ? "包邮" : datas.getData().orderMsg.postage + "");
                            txtAddress.setText(Html.fromHtml("<font><big>"+datas.getData().orderMsg.consignee + "&nbsp;" + datas.getData().orderMsg.contact_number + "</big></font><br/><font size='12px'color='#999999'>" + datas.getData().orderMsg.address + "</font>"));
                            adapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    showToast(datas.getMsg());
                }
            }
        });
    }

    @Override
    protected void initListener() {

    }

    @OnClick({R.id.tv_left, R.id.txt_cancle, R.id.txt_apply_cancle, R.id.txt_pay, R.id.txt_ok_shouhuo, R.id.txt_comment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.txt_cancle:
                break;
            case R.id.txt_apply_cancle:
                break;
            case R.id.txt_pay:
                break;
            case R.id.txt_ok_shouhuo:
                break;
            case R.id.txt_comment:
                break;
        }
    }
}
