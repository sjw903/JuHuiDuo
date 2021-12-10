package com.android.jdhshop.merfragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.R;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.merchantadapter.OrderlistAdapter;
import com.android.jdhshop.merchantbean.Orderlistbean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class OrderdpjFragment extends Fragment {
    private ListView listView;
    private View mview;
    private OrderlistAdapter orderlistAdapter;
    private String token;
    private String orderid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate( R.layout.fragment_orderdpj, container, false );
        init();
        return mview;
    }

    public void init(){
        listView = mview.findViewById( R.id.orderdpj_listview );
        token = SPUtils.getStringData(getActivity(), "token", "");
        postgetaddresslist("");
    }

    //获取订单列表
    public List<Orderlistbean> orderlist = new ArrayList<>(  );


    private void postgetaddresslist(String search) {

        RequestParams requestParams = new RequestParams();
        requestParams.put("token",token);
        requestParams.put( "status","4" );
        requestParams.put( "p","1" );
        requestParams.put( "per","200" );
        HttpUtils.post1( Constants.getOrderList,OrderdpjFragment.this, requestParams, new TextHttpResponseHandler() {
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
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject=new JSONObject(responseString);
                    if ("0".equals( jsonObject.getString( "code" ) )) {
                        orderlist.clear();
                        JSONObject object = jsonObject.getJSONObject( "data" );
                        JSONArray array = object.getJSONArray( "list" );
                        for (int i = 0; i < array.length(); i++) {
                            orderlist.add( new Gson().fromJson( array.getJSONObject( i ).toString(), Orderlistbean.class ) );
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

    //取消订单

    private void postqxorder() {

        RequestParams requestParams = new RequestParams();
        requestParams.put( "token",token );
        requestParams.put( "order_id",orderid);
        HttpUtils.post1( Constants.cancel, OrderdpjFragment.this,requestParams, new TextHttpResponseHandler() {
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
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject=new JSONObject(responseString);
                    if ("0".equals( jsonObject.getString( "code" ) )) {
                        postgetaddresslist( "");

                        Toast.makeText( getActivity(),jsonObject.getString( "msg" ),Toast.LENGTH_SHORT ).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(  ){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage( msg );
            switch (msg.what){
                case 1:
                    orderlistAdapter = new OrderlistAdapter( getActivity(),orderlist );
                    listView.setAdapter( orderlistAdapter );
                    orderlistAdapter.setsubClickListener( new OrderlistAdapter.SubClickListener() {
                        @Override
                        public void OntopicClickListener(View v, String detail, int posit) {
                            if (detail.equals( "qxdd" )){//取消订单
                                orderid = orderlist.get( posit ).detail.get( 0 ).order_id;
                                dialog();
                            }else if (detail.equals( "fk" )){//付款
//                                Intent payment = new Intent( getActivity(), PaymentActivity.class );
//                                payment.putExtra( "orderid",orderlist.get( posit ).id );
//                                startActivity( payment );
                            }else if (detail.equals( "txfh" )){//提醒发货

                            }else if (detail.equals( "ckwl" )){//查看物流

                            }else if (detail.equals( "qrsh" )){//确认收货
                                orderid = orderlist.get( posit ).detail.get( 0 ).order_id;
                                postqxorder();
                            }else if (detail.equals( "pj" )){//评价
//                                Intent intent = new Intent( getActivity(), EvaluateWebActivity.class );
//                                intent.putExtra( "orderid",orderlist.get( posit ).id );
//                                startActivity( intent );
                            }else if (detail.equals( "sqtk" )){//申请退款

                            }
                        }
                    } );
                    break;
            }
        }
    };
    ZDYDialog zdyDialog;

    public void dialog(){
        zdyDialog = new ZDYDialog( getActivity(),"温馨提示","是否确认取消订单!", new View.OnClickListener() {
            @Override
            public void onClick(View view) {//取消
                zdyDialog.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {//确定
                postqxorder();
                zdyDialog.dismiss();
            }
        } );
        zdyDialog.show();

    }
    @Override
    public void onDestroy() {

        mHandler.removeCallbacksAndMessages( null);
        super.onDestroy();
    }
}
