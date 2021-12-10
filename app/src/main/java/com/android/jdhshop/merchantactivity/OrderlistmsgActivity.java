package com.android.jdhshop.merchantactivity;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.common.LogUtils;
import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.OrderMsgbean;
import com.android.jdhshop.bean.OrderMsgdetailbean;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
     * 订单详情
     * */
public class OrderlistmsgActivity extends BaseActivity implements View.OnClickListener {

        private LinearLayout ly_back;
        private TextView tv_title,tv_shr,tv_phone,tv_address,tv_shop,tv_name,tv_pric,tv_oldpric,tv_sku,tv_num,
                tv_kd,tv_allpric,tv_jifen,tv_ddbh,tv_fksj,tv_fhsj;
        private ImageView iv_img,fanhui;
    @Override
    protected void initUI() {
        setContentView( R.layout.activity_orderlistmsg );
    }

    @Override
    protected void initData() {
        ly_back = findViewById( R.id.orderlistmsg_lyback );
        tv_title = findViewById( R.id.orderlistmsg_title );
        tv_shr = findViewById( R.id.orderlistmsg_tvshr );
        tv_phone = findViewById( R.id.orderlistmsg_tvphone );
        tv_address = findViewById( R.id.orderlistmsg_tvaddress );
        tv_shop = findViewById( R.id.orderlistmsg_tvshop );
        tv_name = findViewById( R.id.orderlistmsg_tvname );
        tv_pric = findViewById( R.id.orderlistmsg_tvpric );
        tv_oldpric = findViewById( R.id.orderlistmsg_tvoldpric );
        tv_sku = findViewById( R.id.orderlistmsg_tvsku );
        tv_num = findViewById( R.id.orderlistmsg_tvnum );
        tv_kd = findViewById( R.id.orderlistmsg_tvkd );
        tv_allpric = findViewById( R.id.orderlistmsg_tvallpric );
        tv_jifen = findViewById( R.id.orderlistmsg_tvjifen );
        tv_ddbh = findViewById( R.id.orderlistmsg_tvddbh );
        tv_fksj = findViewById( R.id.orderlistmsg_tvfksj );
        tv_fhsj = findViewById( R.id.orderlistmsg_fhsj);
        iv_img = findViewById( R.id.orderlistmsg_ivimg );
        fanhui = findViewById( R.id.order_fanhui );
        BaseLogDZiYuan.LogDingZiYuan(fanhui, "icon_back.png");
        ly_back.setOnClickListener( this );
        tv_title.setText( getIntent().getStringExtra( "title" ) );
        postgetaddressmsg( "");
    }

    @Override
    protected void initListener() {

    }
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.orderlistmsg_lyback:
                    finish();
                    break;
            }
        }
    //获取订单详情
    public List<OrderMsgbean> msglist = new ArrayList<>(  );
    List<OrderMsgdetailbean> detail = new ArrayList<>(  );
    private void postgetaddressmsg(String search) {

        RequestParams requestParams = new RequestParams();
        requestParams.put( "order_id",getIntent().getStringExtra( "orderid" ) );
        requestParams.put( "order_detail_id",getIntent().getStringExtra( "orderdetailid" ) );
        HttpUtils.post1( Constants.getOrderMsg,OrderlistmsgActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();

            }

            @Override
            public void onFinish() {
                super.onFinish();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogUtils.d("dfasd",responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("dfasd",responseString);
                try {
                    JSONObject jsonObject=new JSONObject(responseString);
                    if ("0".equals( jsonObject.getString( "code" ) )) {
                        msglist.clear();
                        JSONObject object = jsonObject.getJSONObject( "data" );
                        JSONObject jb = object.getJSONObject( "orderMsg" );
                          OrderMsgbean om = new OrderMsgbean();
                        om.user_id = jb.getString( "user_id" );
                          om.order_num = jb.getString( "order_num" );
                          om.title = jb.getString( "title" );
                          om.allprice = jb.getString( "allprice" );
//                          om.address = jb.getString( "address" );
//                          om.company = jb.getString( "company" );
//                          om.consignee = jb.getString( "consignee" );
//                          om.contact_number = jb.getString( "contact_number" );
//                          om.postcode = jb.getString( "postcode" );
                          om.remark = jb.getString( "remark" );
                          msglist.add( om );
                        JSONArray array = jb.getJSONArray( "detail" );
//                        OrderMsgdetailbean omd = new OrderMsgdetailbean();
                        detail.clear();
                        for (int i = 0; i < array.length(); i++) {
                            detail.add( new Gson().fromJson( array.getJSONObject( i ).toString(), OrderMsgdetailbean.class ) );
                        }
                        Message message = Message.obtain();
                        message.what = 1;
                        mHandler.sendMessage(message);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

//    private void postgetaddressmsg(String url) {
//        OkGo.<String>post(url)
//                .tag(this)
//                .cacheMode( CacheMode.DEFAULT)
//                .params( "order_id",getIntent().getStringExtra( "orderid" ) )
//
//                .execute(new StringCallback() {
//                    @Override
//                    public void onFinish() {
//                        super.onFinish();
////                        if (smartRefreshLayout!=null){
////                            smartRefreshLayout.finishRefresh();
////                            smartRefreshLayout.finishLoadMore();
////                        }
//                        hideProgress();
//                    }
//
//                    @Override
//                    public void onStart(Request<String, ? extends Request> request) {
//                        super.onStart( request );
////                        if (smartRefreshLayout != null) {
////
////                        }
//                        showProgress();
//                    }
//
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        BaseResponseBean bean = BaseResponseBean.parseObj(response.body(), BaseResponseBean.class);
//                        if (bean.getCode().equals("0")) {
//                            OrderMsginfo orderMsginfo = bean.parseObject( OrderMsginfo.class );
//                            msglist = orderMsginfo.OrderMsg;
//                            Message message = Message.obtain();
//                            message.what = 1;
//                            mHandler.sendMessage(message);
//                        }else{
//                            Toast.makeText( OrderlistmsgActivity.this,bean.getMessage(),Toast.LENGTH_SHORT ).show();
//                        }
//                    }
//                });
//    }
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(  ){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage( msg );
            switch (msg.what){
                case 1:
                    if (is == 1){
                        Glide.with( OrderlistmsgActivity.this ).load( Constants.APP_IP + detail.get( 0 ).img ).into( iv_img );
                    }
                    tv_shr.setText( "收货人:"+msglist.get( 0 ).consignee );
                    tv_phone.setText( msglist.get( 0 ).contact_number );
                    tv_address.setText( "收货地址:"+msglist.get( 0 ).address );
                    tv_shop.setText( "" );
                    tv_name.setText( detail.get( 0 ).goods_name);
                    tv_pric.setText("¥"+ detail.get( 0 ).price );
                    tv_oldpric.setText("¥"+ detail.get( 0 ).price );
                    tv_oldpric.getPaint().setFlags( Paint.STRIKE_THRU_TEXT_FLAG );
                    tv_num.setText( "x"+detail.get( 0 ).num );
                    if (detail.get( 0 ).sku!=null&&!detail.get( 0 ).sku.equals( "" )&&!detail.get( 0 ).sku.equals( "null" )){
                        String str = detail.get( 0 ).sku.replace( "&nbsp","" );
                        str = str.replace( ";;"," " );
                        tv_sku.setText( str );
                    }else{
                        tv_sku.setText( "" );
                    }
                    tv_allpric.setText( "¥"+detail.get( 0 ).allprice );
//                    tv_jifen.setText( "返积分"+msglist.get( 0 ).detail.get( 0 ).all_give_point+"点" );
                    tv_jifen.setVisibility( View.GONE );
                    tv_ddbh.setText( "订单编号:"+detail.get( 0 ).order_num );
                    if (msglist.get( 0 ).pay_time!=null) {
                        tv_fksj.setText( "付款时间:" + msglist.get( 0 ).pay_time );
                    }else{
                        tv_fksj.setText( "付款时间:" );
                    }
//                    if (msglist.get( 0 ).pay_time!=null) {
//                        tv_fhsj.setText( "发货时间" + msglist.get( 0 ).deliver_time );
//                    }else{
//                        tv_fhsj.setText( "发货时间"  );
//                    }
                    tv_fhsj.setVisibility(View.GONE);
                    break;
            }
        }
    };
    private int is = 1;
    @Override
    protected void onDestroy() {
        is++;
        mHandler.removeCallbacksAndMessages( null );
        super.onDestroy();

    }
}
