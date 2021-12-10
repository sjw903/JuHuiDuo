package com.android.jdhshop.merchantactivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.PayResult;
import com.android.jdhshop.bean.WxPayBean;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 充值
 * Created by yohn on 2018/10/25.
 */

public class MerRechargeActivity extends BaseActivity {
    @BindView(R.id.bg_head)
    LinearLayout bg_head;
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView( R.id.ll_two )
    LinearLayout ly_zfb;
    @BindView( R.id.ll_one )
    LinearLayout ly_wx;
    @BindView( R.id.recharge_pric )
    TextView tv_pric;
    @BindView( R.id.rb_one )
    ImageView iv_one;
    @BindView( R.id.rb_two )
    ImageView iv_two;
    @BindView( R.id.recharge_name )
    TextView tv_name;

    //确认充值
    @BindView(R.id.tv_confirm)
    TextView tv_confirm;

    private String paytype = "wxpay";
    //数据源

    //数据源

    private ACache mAcache;
    String token;

    private static final int SDK_PAY_FLAG = 1;





    @Override
    protected void initUI() {
        setContentView( R.layout.activity_merrecharge);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        mAcache = ACache.get(this);
        token = mAcache.getAsString(Constants.TOKEN);
        bg_head.setBackgroundColor( getResources().getColor( R.color.white ) );
        tv_left.setVisibility( View.VISIBLE );
        Drawable drawable1 = getResources().getDrawable(R.mipmap.icon_back);
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
        tv_left.setCompoundDrawables(drawable1, null, null, null);
        tv_left.setVisibility(View.VISIBLE);

        tv_title.setText( "订单支付" );
        tv_title.setTextColor( getResources().getColor( R.color.col_333 ) );

//        if (getIntent().getStringExtra( "pid" ).equals( "2" )){
        tv_pric.setText( "¥"+getIntent().getStringExtra( "allprice" ) );
        tv_name.setText( getIntent().getStringExtra( "title" ) );

    }

    @Override
    protected void initListener() {
        tv_left.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );

        tv_confirm.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(getIntent().getStringExtra( "pid" ).equals("")){
                if (getIntent().getStringExtra("type")!=null&&getIntent().getStringExtra("type").equals("1")){
                    getPayInfo1();
                }else {
                    getPayInfo();
                }
//                }else {
//                    Toast.makeText(MerRechargeActivity.this,"请选择一个VIP会员",Toast.LENGTH_SHORT).show();
//                }
            }
        } );
        ly_wx.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_one.setImageResource( R.mipmap.pay_prexhdpi );
                iv_two.setImageResource( R.mipmap.pay_norxhdpi );
                paytype = "wxpay";
            }
        } );
        ly_zfb.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_one.setImageResource( R.mipmap.pay_norxhdpi );
                iv_two.setImageResource( R.mipmap.pay_prexhdpi );
                paytype = "alipay";
            }
        } );
    }

    /**
     * 获取费用配置
     */
//    private void getFee() {
//        RequestParams requestParams = new RequestParams();
//        HttpUtils.post( Constants.GET_FEE, requestParams, new onOKJsonHttpResponseHandler<FeeBean>( new TypeToken<Response<FeeBean>>() {
//        }) {
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Response<FeeBean> datas) {
//                if (datas.isSuccess()) {
//                    List<FeeBean.FeesBean> list = datas.getData().list;
//                        dataList.clear();
//                    dataList.addAll(list);
//                } else {
//                }
//                adapter.notifyDataSetChanged();
//            }
//        });
//    }


    private void getPayInfo() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", token);
        requestParams.put("order_id", getIntent().getStringExtra( "orderid" ));

        requestParams.put("pay_method", paytype);
        HttpUtils.post("offline".equals(getIntent().getStringExtra("type"))?Constants.APP_IP+"/api/O2oOfflineOrder/getPayForm":Constants.getPayForm, MerRechargeActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // 输出错误信息
                LogUtils.e("RE", "onFailure()--" + responseString);
            }

            @Override
            public void onFinish() {
                // 隐藏进度条
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.e("RE", "onSuccess()--" + responseString);
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    if (code.equals("0")) {
                        JSONObject jsonObject1 = jsonObject.optJSONObject("data");
                        if (jsonObject1 != null) {
                            final String appParameters = jsonObject1.optString("pay_parameters");


                            if (paytype.equals("wxpay")) {
                                XtoJson(appParameters);
                                getWxPay();
                            } else {

                                Runnable payRunnable = new Runnable() {
                                    @Override
                                    public void run() {

                                        PayTask alipay = new PayTask(MerRechargeActivity.this);
                                        Map<String, String> result = alipay.payV2(appParameters, true);

                                        Message msg = new Message();
                                        msg.what = SDK_PAY_FLAG;
                                        msg.obj = result;
                                        mHandler.sendMessage(msg);


                                    }
                                };
                                // 必须异步调用
                                Thread payThread = new Thread(payRunnable);
                                payThread.start();
                            }
                        } else {
                            Toast.makeText(MerRechargeActivity.this, "", Toast.LENGTH_SHORT).show();
                        }
                    }else if (code.equals("14")){
                        Intent intent = new Intent(MerRechargeActivity.this,OrderListActivity.class);
                        intent.putExtra("type","4");
                        startActivity(intent);
                        Toast.makeText(MerRechargeActivity.this, msg, Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(MerRechargeActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch(JSONException e){
                    e.printStackTrace();
                }


            }

            @Override
            public void onStart() {
                // 显示进度条
                super.onStart();
            }
        });
    }

    private void getPayInfo1() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", token);
        requestParams.put("id", getIntent().getStringExtra( "orderid" ));

        requestParams.put("pay_method", paytype);
        HttpUtils.post(Constants.recharge, MerRechargeActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // 输出错误信息
                LogUtils.e("RE", "onFailure()--" + responseString);
            }

            @Override
            public void onFinish() {
                // 隐藏进度条
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.e("RE", "onSuccess()--" + responseString);
                try {
                    JSONObject jsonObject = new JSONObject( responseString );
                    JSONObject jsonObject1 = jsonObject.optJSONObject( "data" );
                    if (jsonObject1!=null ) {
                        final String appParameters = jsonObject1.optString( "AppParameters" );


                        if (paytype.equals( "wxpay" )) {
                            XtoJson( appParameters );
                            getWxPay();
                        } else {

                            Runnable payRunnable = new Runnable() {
                                @Override
                                public void run() {

                                    PayTask alipay = new PayTask( MerRechargeActivity.this );
                                    Map<String, String> result = alipay.payV2( appParameters, true );

                                    Message msg = new Message();
                                    msg.what = SDK_PAY_FLAG;
                                    msg.obj = result;
                                    mHandler.sendMessage( msg );


                                }
                            };
                            // 必须异步调用
                            Thread payThread = new Thread( payRunnable );
                            payThread.start();
                        }
                    }else{
                        Toast.makeText( MerRechargeActivity.this,"",Toast.LENGTH_SHORT ).show();
                    }
                } catch(JSONException e){
                    e.printStackTrace();
                }


            }

            @Override
            public void onStart() {
                // 显示进度条
                super.onStart();
            }
        });
    }


    //微信
    WxPayBean bean;
    /**
     * 解析
     * @param wXpayInfo
     */
    private void XtoJson(String wXpayInfo) {
        try {
            JSONObject jobj=new JSONObject(wXpayInfo);
            bean=new WxPayBean();
            bean.setAppId(jobj.getString("appid"));
//            Constants.WX_APP_ID=bean.getAppId();
            bean.setNonceStr(jobj.getString("noncestr"));
            bean.setPackage1(jobj.getString("package"));
            bean.setPartnerid(jobj.getString("partnerid"));
            bean.setPrepayid(jobj.getString("prepayid"));
            bean.setTimeStamp(jobj.getString("timestamp"));
            bean.setSign(jobj.getString("sign"));

            Log.i("weixin调接口","appid:"+jobj.getString("appid")+"\n"+"noncestr:"+jobj.getString("noncestr")+"\n"
                    +"package:"+jobj.getString("package")+"\n"+"partnerid:"+jobj.getString("partnerid")+"\n"
                    +"prepayid:"+jobj.getString("prepayid")+"\n"+"timestamp:"+jobj.getString("timestamp")+"\n"
                    +"sign:"+jobj.getString("sign"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void getWxPay() {
        IWXAPI api= WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, false);
        api.registerApp(Constants.WX_APP_ID);
        PayReq payreq=new PayReq();
        payreq.appId=Constants.WX_APP_ID;
        payreq.partnerId=bean.getPartnerid();
        payreq.prepayId=bean.getPrepayid();
        payreq.packageValue=bean.getPackage1();
        payreq.nonceStr=bean.getNonceStr();
        payreq.timeStamp=bean.getTimeStamp();
        payreq.sign=bean.getSign();
        api.sendReq(payreq);


        Log.i("weixin请求","appid:"+payreq.appId+"\n"+"noncestr:"+payreq.nonceStr+"\n"
                +"package:"+payreq.packageValue+"\n"+"partnerid:"+payreq.partnerId+"\n"
                +"prepayid:"+payreq.prepayId+"\n"+"timestamp:"+payreq.timeStamp+"\n"
                +"sign:"+payreq.sign);
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: //支付宝支付消息
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /*
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    // 同步返回需要验证的信息
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, Constants.VALUE_ALIPAY_SUCCESS)) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        showToast("支付成功");
                        EventBus.getDefault().post( "order" );
//                        mAcache = ACache.get(getComeActivity());
//                        mAcache.put("group_id", "2");
//                        BroadcastManager.getInstance(getComeActivity()).sendBroadcast(BroadcastContants.sendPaymentSuccessMessage);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        showToast(payResult.getMemo());
                    }
                    break;
            }
        };
    };

}
