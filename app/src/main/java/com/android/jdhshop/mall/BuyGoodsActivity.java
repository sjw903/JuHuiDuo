package com.android.jdhshop.mall;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.ziyuan.ResourceManager;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.malladapter.BuyGoodsAdapter;
import com.android.jdhshop.mallbean.AddressBean;
import com.android.jdhshop.mallbean.BuyCarBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class BuyGoodsActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.order_recy)
    RecyclerView orderRecy;
    @BindView(R.id.txt_open)
    TextView txtOpen;
    @BindView(R.id.txt_price)
    TextView txtPrice;
    @BindView(R.id.txt_delivery)
    TextView txtDelivery;
    @BindView(R.id.txt_remark)
    EditText txtRemark;
    @BindView(R.id.txt_react_money)
    TextView txtReactMoney;
//    @BindView(R.id.rb_pay_zfb)
//    RadioButton rbPayZfb;
//    @BindView(R.id.rb_pay_yue)
    RadioButton rbPayYue;
    private List<BuyCarBean> buyCarBeans,tempbuyCarBeans;
    BuyGoodsAdapter buyGoodsAdapter;
    private AddressBean addressBean;
    private boolean hasPay=false;
    private String order_id="",order_num="";
    private String allPrice="";
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_buy_goods);
        ButterKnife.bind(this);
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("购买商品");
        Drawable rb1 = ResourceManager.getInstance().getDrawable("ic_address_map", "drawable");
        Drawable rb2 = ResourceManager.getInstance().getDrawable("ic_keyboard_arrow_right_black_24dp", "drawable");
        if (rb1 != null) {
            rb1.setBounds(0, 0, rb1.getMinimumWidth(), rb1.getMinimumHeight());// 一定要设置setBounds();
            rb2.setBounds(0, 0, rb1.getMinimumWidth(), rb1.getMinimumHeight());// 一定要设置setBounds();
            txtAddress.setCompoundDrawables(rb1,null,rb2,null);

        } else {
            // Log.d(TAG, "drawable is null");
        }

    }
    @Override
    protected void initData() {
        buyCarBeans=new ArrayList<>();
        tempbuyCarBeans= (List<BuyCarBean>) getIntent().getExtras().get("buyCarBean");
        txtOpen.setTag("0");
        txtOpen.setText("共"+tempbuyCarBeans.size()+"件∨");//∧
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        orderRecy.setLayoutManager(linearLayoutManager);
        buyCarBeans.addAll(tempbuyCarBeans.subList(0, tempbuyCarBeans.size() > 2 ? 2 : tempbuyCarBeans.size()));
        buyGoodsAdapter=new BuyGoodsAdapter(this, R.layout.item_order_detail,buyCarBeans);
        buyGoodsAdapter.setOnchange(new BuyGoodsAdapter.Onchange() {
            @Override
            public void change() {
                Float price=0f,old_price=0f,yunfei=0f;
                for(int i=0;i<tempbuyCarBeans.size();i++){
                    price+=Float.valueOf(tempbuyCarBeans.get(i).getPrice())*Float.valueOf(tempbuyCarBeans.get(i).getNum());
                    old_price+=Float.valueOf(tempbuyCarBeans.get(i).getOld_price())*Float.valueOf(tempbuyCarBeans.get(i).getNum());
                    if(tempbuyCarBeans.get(i).getPostage()!=null&&!"".equals(tempbuyCarBeans.get(i).getPostage()))
                        yunfei+=Float.valueOf("null".equals(tempbuyCarBeans.get(i).getPostage())?"0":tempbuyCarBeans.get(i).getPostage())*Float.valueOf(tempbuyCarBeans.get(i).getNum());
                }
                DecimalFormat format=new DecimalFormat("0.00");
                txtPrice.setText(format.format(price)+"元");
                txtReactMoney.setText(format.format(price+yunfei));
                txtDelivery.setText(yunfei<=0?"包邮":yunfei+"");
            }
        });
        orderRecy.setAdapter(buyGoodsAdapter);
        Float price=0f,old_price=0f,yunfei=0f;
        for(int i=0;i<tempbuyCarBeans.size();i++){
            price+=Float.valueOf(tempbuyCarBeans.get(i).getPrice())*Float.valueOf(tempbuyCarBeans.get(i).getNum());
            old_price+=Float.valueOf(tempbuyCarBeans.get(i).getOld_price())*Float.valueOf(tempbuyCarBeans.get(i).getNum());
            if(tempbuyCarBeans.get(i).getPostage()!=null&&!"".equals(tempbuyCarBeans.get(i).getPostage()))
            yunfei+=Float.valueOf("null".equals(tempbuyCarBeans.get(i).getPostage())?"0":tempbuyCarBeans.get(i).getPostage())*Float.valueOf(tempbuyCarBeans.get(i).getNum());
        }
        DecimalFormat format=new DecimalFormat("0.00");
        txtPrice.setText(format.format(price)+"元");
        txtReactMoney.setText(format.format(price+yunfei)+"元");
        txtDelivery.setText(yunfei<=0?"包邮":yunfei+"");
    }

    @Override
    protected void initListener() {

    }
    @OnClick({R.id.tv_left, R.id.txt_address, R.id.txt_open, R.id.btn_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                exit();
                break;
            case R.id.txt_address:
                Intent intent = new Intent(this, AddressActivity.class);
                intent.putExtra("type", "2");
                startActivityForResult(intent, 1);
                break;
            case R.id.txt_open:
                buyCarBeans.clear();
                if ("0".equals(txtOpen.getTag().toString())) {
                    txtOpen.setTag("1");
                    txtOpen.setText("共" +tempbuyCarBeans.size() + "件∧");//∨
                    buyCarBeans.addAll(tempbuyCarBeans);
                } else {
                    txtOpen.setTag("0");
                    txtOpen.setText("共" + tempbuyCarBeans.size() + "件∨");//
                    buyCarBeans.addAll(tempbuyCarBeans.subList(0,tempbuyCarBeans.size() > 2 ? 2 : tempbuyCarBeans.size()));
                }
                buyGoodsAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_pay:
                if(addressBean==null){
                    showToast("请选择收货地址");
                    return;
                }
                if(tempbuyCarBeans.size()==1){
                    //单个商品购买
                    if(!"".equals(order_id)){
                        getpPayMoneyForm(order_id);
                    }else{
                        getOrderId();
                    }
                }else{
                    //从购物车下单
                    getOrderFromCar();
                }
                break;
        }
    }
    private void getOrderFromCar(){
        RequestParams requestParams=new RequestParams();
        requestParams.put("address_id",addressBean.id);
        requestParams.put("remark",txtRemark.getText().toString().trim());
        JSONArray array=new JSONArray();
        JSONObject object;
        for(int i=0;i<tempbuyCarBeans.size();i++){
            object=new JSONObject();
            try {
                object.put("shopcart_id",tempbuyCarBeans.get(i).getGoods_id());
                object.put("goods_num",tempbuyCarBeans.get(i).getNum());
                array.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        requestParams.put("goodslist",array.toString());
        HttpUtils.post(Constants.SHOPCAR_ORDER_BUY,BuyGoodsActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
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
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    if (code == 0) {
                        order_id=jsonObject.getJSONObject("data").getString("order_id");
                        order_num=jsonObject.getJSONObject("data").getString("order_num");
                        Intent intent = new Intent(BuyGoodsActivity.this, PayOrderMoneyActivity.class);
                        intent.putExtra("money",txtReactMoney.getText().toString().replace("¥",""));
                        intent.putExtra("order_num", order_num);
                        intent.putExtra("order_id",order_id);
                        intent.putExtra("name",tempbuyCarBeans.get(0).getGoods_name());
                        startActivity(intent);
                        finish();
//                        allPrice=jsonObject.getJSONObject("data").getString("allprice");
//                        getpPayMoneyForm(jsonObject.getJSONObject("data").getString("order_num"));
                    } else {
                        showToast(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    private void getOrderId(){
        RequestParams requestParams=new RequestParams();
        requestParams.put("goods_id",tempbuyCarBeans.get(0).getGoods_id());
        requestParams.put("address_id",addressBean.id);
        requestParams.put("num",tempbuyCarBeans.get(0).getNum());
        requestParams.put("remark",txtRemark.getText().toString().trim());
        JSONArray array=new JSONArray();
        JSONObject object;
        for(int i=0;i<tempbuyCarBeans.get(0).getSelectbeans().size();i++){
            object=new JSONObject();
            try {
                object.put("attribute_id",tempbuyCarBeans.get(0).getSelectbeans().get(i).getString());
                object.put("value",tempbuyCarBeans.get(0).getSelectbeans().get(i).getValue());
                array.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        requestParams.put("goods_sku",array.toString());
        LogUtils.d("dfsd",requestParams.toString());
        HttpUtils.post(getIntent().getExtras().getString("isVip")!=null?Constants.APP_IP+"/api/UserOrder/order":Constants.MALL_BUY_GOODS, BuyGoodsActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogUtils.d("dfsd",responseString);
                showToast(responseString);
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
                LogUtils.d("dfsd",responseString);
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    if (code == 0) {
                        order_id=jsonObject.getJSONObject("data").getString("order_id");
                        order_num=jsonObject.getJSONObject("data").getString("order_num");
                        Intent intent = new Intent(BuyGoodsActivity.this, PayOrderMoneyActivity.class);
                        intent.putExtra("money",txtReactMoney.getText().toString().replace("¥",""));
                        intent.putExtra("order_num", order_num);
                        intent.putExtra("order_id",order_id);
                        if(getIntent().getExtras().getString("isVip")!=null){
                            intent.putExtra("isVip","1");
                        }
                        intent.putExtra("name",tempbuyCarBeans.get(0).getGoods_name());
                        startActivity(intent);
                        finish();
//                        allPrice=jsonObject.getJSONObject("data").getString("allprice");
//                        getpPayMoneyForm(order_id);
                    } else if(code==208){
//                        AlertDialog.Builder builder = new AlertDialog.Builder(BuyGoodsActivity.this);
//                        builder.setTitle("温馨提示");
//                        builder.setMessage("您的账户不足，暂时无法购买");
//                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                        builder.setPositiveButton("去充值", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                openActivity(RechargeActivity.class);
//                                dialog.dismiss();
//                            }
//                        });
//                        builder.setCancelable(false).show();
                        showToast(msg);
                    }else{
                        showToast(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void getpPayMoneyForm(final String order_num1){
        RequestParams requestParams=new RequestParams();
        requestParams.put("order_id",order_num1);
        requestParams.put("pay_method","wxpay");
        HttpUtils.post(Constants.GET_ORDER_FORM, BuyGoodsActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
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
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    if (code == 0) {
                        if (responseString.contains("alipay_sdk")) {
                            payZFB(jsonObject.getJSONObject("data").getString("pay_parameters"));
                        } else {
                            showToast("购买成功");
                            order_id="";
                            Bundle bundle=new Bundle();
                            bundle.putString("money",allPrice);
                            bundle.putString("order_num",order_num1);
                            openActivity(PayResultActivity.class,bundle);
                            finish();
                        }
                    } else {
                        showToast(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private Handler zfbHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                GsonBuilder builder = new GsonBuilder();
                builder.serializeNulls();
                JSONObject object = new JSONObject(builder.create().toJson(msg.obj));
                showToast(object.getString("memo"));
                if(object.getInt("resultStatus")==9000){
                    Bundle bundle=new Bundle();
                    bundle.putString("money",allPrice);
                    bundle.putString("order_id",order_id);
                    bundle.putString("order_num",order_num);
                    openActivity(PayResultActivity.class,bundle);
                    order_id="";
                    finish();
                }
            } catch (JSONException e) {
                showToast("支付发生错误");
            }
        }
    };
    private void payZFB(final String payInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(BuyGoodsActivity.this);
                Map<String, String> result = alipay.payV2(payInfo, true);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                zfbHandle.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private void exit(){
        if(!hasPay&&!"".equals(order_id)){
            showTipDialog3("退出提示", "您的订单当前还没有支付,稍后支付吗", new onClickListener() {
                @Override
                public void onClickSure() {
                    finish();
                }
            }, new onClickListener() {
                @Override
                public void onClickSure() {

                }
            },"稍后支付","留下支付");
        }else{
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                addressBean= (AddressBean) data.getExtras().get("address");
                String htmlStr="<font color='#333333'size='14px'><big>"+addressBean.province+addressBean.city+addressBean.county+addressBean.detail_address+"</big></font><br/><font size='14px'color='#A9A9A9'>"+addressBean.consignee+"&nbsp;"+addressBean.contact_number+"</font>";
                txtAddress.setText(Html.fromHtml(htmlStr));
            }
        }
    }
}
