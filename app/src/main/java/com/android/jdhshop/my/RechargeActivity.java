package com.android.jdhshop.my;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.jdhshop.bean.WxPayBean;
import com.android.jdhshop.merchantactivity.MerRechargeActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.MainActivity;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.FeeAdapter;
import com.android.jdhshop.adapter.PayAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.FeeBean;
import com.android.jdhshop.bean.PayBean;
import com.android.jdhshop.bean.PayResult;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.UserBean;
import com.android.jdhshop.bean.UserInfoBean;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.utils.BroadcastContants;
import com.android.jdhshop.utils.BroadcastManager;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 充值
 * Created by yohn on 2018/10/25.
 */

public class RechargeActivity extends BaseActivity {
    @BindView(R.id.bg_head)
    LinearLayout bg_head;
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.gv_fee)
    GridView gv_fee;

    @BindView(R.id.lv_type)
    ListView lv_type;

    //确认充值
    @BindView(R.id.tv_confirm)
    TextView tv_confirm;

    //数据源
    private List<FeeBean.FeesBean> dataList = new ArrayList<>();
    FeeAdapter adapter=null;

    //数据源
    private List<PayBean> payList = new ArrayList<>();
    PayAdapter payAdapter=null;

    private ACache mAcache;
    String token;

    private static final int SDK_PAY_FLAG = 1;





    @Override
    protected void initUI() {
        setContentView( R.layout.ac_recharge_new);
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

        tv_title.setText( "充值" );
        tv_title.setTextColor( getResources().getColor( R.color.col_333 ) );

        adapter = new FeeAdapter(this, R.layout.item_recharge, dataList);
        gv_fee.setAdapter(adapter);
//        for(int i=0;i<5;i++){
//            FeeBean.FeesBean a=new FeeBean.FeesBean();
//            a.fee="59"+i;
//            a.title="59元/月";
//            dataList.add( a );
//        }
//        adapter.notifyDataSetChanged();


        //支付方式
        payAdapter = new PayAdapter(this, R.layout.item_paytype, payList);
        lv_type.setAdapter(payAdapter);
        PayBean a=new PayBean();
        a.title="支付宝支付";
        a.type=1;
        a.setChecked(true);
        payList.add( a );
        PayBean wx=new PayBean();
        wx.title="微信支付";
        wx.type=2;
        payList.add( wx );
        payAdapter.notifyDataSetChanged();
        getFee();
    }
    private String paytype = "wxpay"; //默认微信支付;
    String pid="";
    @Override
    protected void initListener() {
        tv_left.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );
        lv_type.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(PayBean feesBean : payList){ //遍历list集合中的数据
                    feesBean.setChecked(false);//全部设为未选中
                }
                payList.get(position).setChecked(true);//点击的设为选中
                paytype = payList.get(position).type == 1 ? "alipay" : "wxpay";
                payAdapter.notifyDataSetChanged();//刷新adapter
            }
        } );
        gv_fee.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(FeeBean.FeesBean feesBean : dataList){ //遍历list集合中的数据
                    feesBean.setChecked(false);//全部设为未选中
                }
                pid=dataList.get(position).id;
                dataList.get(position).setChecked(true);//点击的设为选中
                adapter.notifyDataSetChanged();//刷新adapter
            }
        } );
        tv_confirm.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pid.equals("")){
                    getPayInfo();
                }else {
                    Toast.makeText(RechargeActivity.this,"请选择一个VIP会员",Toast.LENGTH_SHORT).show();
                }
            }
        } );
    }

    /**
     * 获取费用配置
     */
    private void getFee() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post( Constants.GET_FEE,RechargeActivity.this, requestParams, new onOKJsonHttpResponseHandler<FeeBean>( new TypeToken<Response<FeeBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Response<FeeBean> datas) {
                if (datas.isSuccess()) {
                    List<FeeBean.FeesBean> list = datas.getData().list;
                        dataList.clear();
                    dataList.addAll(list);
                } else {
                }
                adapter.notifyDataSetChanged();
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

    private void getPayInfo() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", token);
        requestParams.put("id", pid);
        requestParams.put("pay_method", paytype);

        LogUtils.d(TAG, "url: " + Constants.UPGRADE);
        LogUtils.d(TAG, "req: " + requestParams.toString());
        HttpUtils.post(Constants.UPGRADE, RechargeActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // 输出错误信息
                LogUtils.e("RE", "onFailure()--" + responseString);
            }

            @Override
            public void onFinish() {
                // 隐藏进度条
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.e("RE", "onSuccess()--" + responseString);
                try {
                    JSONObject jsonObject=new JSONObject(responseString);
                    if(!"0".equals(jsonObject.getString("code"))){
                        ToastUtils.showLongToast(RechargeActivity.this,jsonObject.getString("msg"));
                        return ;
                    }
                    JSONObject jsonObject1=jsonObject.optJSONObject("data");
                    final String appParameters=jsonObject1.optString("AppParameters");


                    LogUtils.d(TAG, "onSuccess: " + paytype);

                    if (paytype.equals("wxpay")) {
                        LogUtils.d(TAG, "onSuccess: ");
                        XtoJson(appParameters);
                        getWxPay();
                    } else {

                        Runnable payRunnable = new Runnable() {
                            @Override
                            public void run() {

                                PayTask alipay = new PayTask(RechargeActivity.this);
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


//                    Runnable payRunnable = new Runnable() {
//
//                        @Override
//                        public void run() {
//                            PayTask alipay = new PayTask(RechargeActivity.this);
//                            Map<String,String> result = alipay.payV2(appParameters,true);
//
//                            Message msg = new Message();
//                            msg.what = SDK_PAY_FLAG;
//                            msg.obj = result;
//                            mHandler.sendMessage(msg);
//
//
//
//
//
//                        }
//                    };
//                    // 必须异步调用
//                    Thread payThread = new Thread(payRunnable);
//                    payThread.start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onStart() {
                // 显示进度条
                super.onStart();
                showLoadingDialog();
            }
        });
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
                        getInfo();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        showToast(payResult.getMemo());
                    }
                    break;
            }
        };
    };
    private void getInfo(){
        RequestParams params = new RequestParams();
        HttpUtils.post(Constants.GET_USER_MSG, RechargeActivity.this,params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // 输出错误信息
            }

            @Override
            public void onFinish() {
                // 隐藏进度条
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    //返回码
                    UserInfoBean userBean = null;
                    int code = jsonObject.optInt("code");
                    //返回码说明
                    String msg = jsonObject.optString("msg");
                    String data = jsonObject.optString("data");
                    if (0 == code) {
                        if (!TextUtils.isEmpty(data)) {//GSON对数字都当做double解析
                            Gson gson = new Gson();
                            userBean = gson.fromJson(data.trim(), UserInfoBean.class);
                            CaiNiaoApplication.setUserInfoBean(userBean);
                        }
                        if (null != userBean) {
                            CaiNiaoApplication.setUserBean(new UserBean(userBean.user_detail.user_id, userBean.user_msg.group_id, SPUtils.getStringData(RechargeActivity.this, "token", ""), userBean.user_detail.avatar, userBean.user_detail.nickname, userBean.user_msg.is_forever));
                        }
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
            }
        });
    }
}
