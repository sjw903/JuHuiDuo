package com.android.jdhshop.merchantactivity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.merchantadapter.FwAdapter;
import com.android.jdhshop.merchantbean.Authbean;
import com.android.jdhshop.merchantbean.Fwbean;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ShoplistmsgmoreActivity extends BaseActivity {

    private LinearLayout ly_back;
    private TextView tv_title;
    private TextView tv_yytiem;
    private RecyclerView recyclerView;
    private RelativeLayout rl_zzxkz;
    public List<Fwbean> fwlist = new ArrayList<>(  );
    private FwAdapter fwAdapter;
//    private int[] imgs = {R.mipmap.icon_service_a, R.mipmap.icon_service_b,R.mipmap.icon_service_d,
//            R.mipmap.icon_service_c,R.mipmap.icon_service_e,R.mipmap.icon_service_f,
//            R.mipmap.icon_service_g,R.mipmap.icon_service_h,R.mipmap.icon_service_i};
//    private String[] strs = {"WIFI","停车位","包厢","免费茶水","优待服务","刷卡支付","沙发休闲","宝宝椅","禁止吸烟"};


    @Override
    protected void initUI() {
        setContentView(R.layout.activity_shoplistmsgmore);
    }

    @Override
    protected void initData() {
        initView();
    }

    @Override
    protected void initListener() {

    }

    public void initView(){
        ly_back = findViewById( R.id.shopmore_lyback );
        tv_title = findViewById( R.id.shopmore_title );
        tv_yytiem = findViewById( R.id.shopmore_yytime );
        recyclerView = findViewById( R.id.shopmore_recy );
        rl_zzxkz = findViewById( R.id.shopmore_zzxkz );

        tv_title.setText( getIntent().getStringExtra( "title" ) );
        tv_yytiem.setText( getIntent().getStringExtra( "yytime" ) );

        fwAdapter = new FwAdapter( ShoplistmsgmoreActivity.this, fwlist );
        GridLayoutManager gridLayoutManager = new GridLayoutManager( ShoplistmsgmoreActivity.this, 5 );
        gridLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        recyclerView.setLayoutManager( gridLayoutManager );
        recyclerView.setAdapter( fwAdapter );


        getauthlist();

        ly_back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        } );
        rl_zzxkz.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ShoplistmsgmoreActivity.this,ShowxkzActivity.class );
                intent.putExtra( "id", getIntent().getStringExtra( "id" ));
                startActivity( intent);
            }
        } );
    }

    public List<Authbean> authbeanList = new ArrayList<>();
    private void getauthlist() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.getMerchantAuth, ShoplistmsgmoreActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("dsfasd", responseString);
                try {
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        JSONArray array = object.getJSONObject("data").getJSONArray("list");
                        for (int i = 0; i < array.length(); i++) {
                            authbeanList.add(new Gson().fromJson(array.getJSONObject(i).toString(), Authbean.class));
                        }
                        if (getIntent().getStringExtra( "service" )!=null&&!getIntent().getStringExtra( "service" ).equals( "" )) {
                            String services[] = getIntent().getStringExtra( "service" ).split( "," );
                            //服务列表
                            if (services != null && services.length != 0 && !services.equals( "" )) {

                                for (int i = 0;i<services.length;i++){
                                    for (int j = 0;j<authbeanList.size();j++){
                                        if (services[i].equals(authbeanList.get(j).id)) {
                                            Fwbean fwbean = new Fwbean();
                                            fwbean.img = authbeanList.get(j).icon;
                                            fwbean.name = authbeanList.get(j).name;
                                            fwbean.index = "";
                                            fwlist.add(fwbean);
                                        }
                                    }

                                }
                                fwAdapter.notifyDataSetChanged();
                            }

                        }
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
