package com.android.jdhshop.merchantactivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jdhshop.common.LogUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.SjxxsPjbean;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * 门店商品下单
 * */
public class ShopaddorderOfflineActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title,tv_name,tv_pric,tv_num,tv_allpric,tv_allpric1,tv_phone,tv_button,shopaddorder_allpric_pay;
    private ImageView iv_img,iv_jia,iv_jian;
    private LinearLayout ly_back;
    private int number = 1;
    private EditText et_input;
    private NiceSpinner tv_dkq;
    private LinearLayout ly_dkq;

    private boolean isDk = true;
    private String coupon_id="",amount="0.00",amount2="0.00";
    private List<String> list=new ArrayList<>();
    private List<String> names=new ArrayList<>();
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_shopaddorder_offline);
        initView();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    public void initView(){

        tv_title = findViewById( R.id.shopaddorder_title );
        tv_name= findViewById( R.id.shopaddorder_name );
        tv_pric = findViewById( R.id.shopaddorder_pric );
        tv_num = findViewById( R.id.shopaddorder_num );
        tv_allpric = findViewById( R.id.shopaddorder_allpric );
        tv_allpric1 = findViewById( R.id.shopaddorder_allpric1 );
        shopaddorder_allpric_pay= findViewById( R.id.shopaddorder_allpric_pay );
        tv_phone = findViewById( R.id.shopaddorder_phone );
        tv_button = findViewById( R.id.shopaddorder_tvbutton );
        iv_img = findViewById( R.id.shopaddorder_img );
        et_input=findViewById(R.id.et_input);
        iv_jia = findViewById( R.id.shopaddorder_imgjia );
        iv_jian = findViewById( R.id.shopaddorder_imgjian );
        ly_back = findViewById( R.id.shopaddorder_lyback );
        tv_dkq = findViewById(R.id.shopaddorder_dkq1);
        ly_dkq = findViewById(R.id.shopaddorder_lydkq);
        ly_dkq.setOnClickListener(this);
        ly_back.setOnClickListener( this );
        iv_jia.setOnClickListener( this );
        iv_jian.setOnClickListener( this );
        tv_button.setOnClickListener( this );
        getList();
        et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if("".equals(et_input.getText().toString())){
                    tv_allpric1.setText("0");
                }else{
                    getNum();
                }
            }
        });
        tv_phone.setText( SPUtils.getStringData(ShopaddorderOfflineActivity.this, "phone", "").substring( 0,3 )+"****"+SPUtils.getStringData(ShopaddorderOfflineActivity.this, "phone", "").substring( 7,11 ) );
        Glide.with( ShopaddorderOfflineActivity.this ).load( Constants.APP_IP +getIntent().getStringExtra( "img" ) ).into( iv_img );
        tv_title.setText( getIntent().getStringExtra( "name" ) );
        tv_name.setText( getIntent().getStringExtra( "name" ) );
        tv_pric.setText(getIntent().getStringExtra( "pric" )+"折" );
//        tv_allpric.setText( "¥"+ (Double.parseDouble(getIntent().getStringExtra( "pric" )) - Double.parseDouble(getIntent().getStringExtra("dkq"))) );
//        tv_allpric1.setText("¥"+ (Double.parseDouble(getIntent().getStringExtra( "pric" )) - Double.parseDouble(getIntent().getStringExtra("dkq"))));
//        tv_button.setText("¥"+ (Double.parseDouble(getIntent().getStringExtra( "pric" )) - Double.parseDouble(getIntent().getStringExtra("dkq")))+" 提交订单" );
//        tv_dkq.setText(getIntent().getStringExtra("pric")+"折");
        list.add("");
        tv_dkq.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                coupon_id=list.get(position);
                amount=names.get(position);
                if(position==0){
                    amount="0.00";
                }
                shopaddorder_allpric_pay.setText(String.format("%.2f",(Double.valueOf(tv_allpric1.getText().toString())-Double.valueOf(amount))));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private  void getList(){
        RequestParams requestParams = new RequestParams();
        requestParams.put("merchant_id",getIntent().getStringExtra( "goodid" ));
        HttpUtils.post(Constants.APP_IP+"/api/O2oVoucherUser/getlist", ShopaddorderOfflineActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogUtils.d("dsfads",responseString);
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject temp = new JSONObject(responseString);
                    if(temp.getInt("code")==0){
                        JSONArray array=temp.getJSONObject("data").getJSONArray("list");
                        if(array.length()<=0){
                            names.add("暂无可用");
                        }else{
                            names.add("暂不使用");
                        }
                        for(int i=0;i<array.length();i++){
                            JSONObject object=array.getJSONObject(i);
                            list.add(object.getString("id"));
                            names.add(object.getString("amount"));
                        }
                        tv_dkq.attachDataSource(names);
                    }else{
                        showToast(temp.getString("msg"));
                        if("用户不存在".equals(temp.getString("msg"))){
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void getNum(){
        RequestParams requestParams = new RequestParams();
        requestParams.put("merchant_id",getIntent().getStringExtra( "goodid" ));
        requestParams.put("goods_price",et_input.getText().toString());
        HttpUtils.post(Constants.APP_IP+"/api/O2oOfflineOrder/getPrice", ShopaddorderOfflineActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject temp = new JSONObject(responseString);
                    if(temp.getInt("code")==0){
                        amount2=temp.getJSONObject("data").getString("amount");
                        tv_allpric1.setText(String.format("%.2f",(Double.valueOf(et_input.getText().toString())-Double.valueOf(temp.getJSONObject("data").getString("amount")))));
                        shopaddorder_allpric_pay.setText(String.format("%.2f",(Double.valueOf(tv_allpric1.getText().toString())-Double.valueOf(amount))));
                    }else{
                        showToast(temp.getString("msg"));
                        if("用户不存在".equals(temp.getString("msg"))){
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.shopaddorder_lyback:
                finish();
                break;
            case R.id.shopaddorder_imgjia://加数量
                number++;
                tv_num.setText( number+"" );
                if (isDk) {
                    tv_allpric.setText("¥" + (Double.parseDouble(getIntent().getStringExtra("pric")) * number - Double.parseDouble(getIntent().getStringExtra("dkq"))*number));
                    tv_allpric1.setText("¥" + (Double.parseDouble(getIntent().getStringExtra("pric")) * number - Double.parseDouble(getIntent().getStringExtra("dkq"))*number));
                    tv_button.setText("¥" + (Double.parseDouble(getIntent().getStringExtra("pric")) * number - Double.parseDouble(getIntent().getStringExtra("dkq"))*number) + " 提交订单");
                }else{
                    tv_allpric.setText("¥" + Double.parseDouble(getIntent().getStringExtra("pric")) * number);
                    tv_allpric1.setText("¥" + Double.parseDouble(getIntent().getStringExtra("pric")) * number);
                    tv_button.setText("¥" + Double.parseDouble(getIntent().getStringExtra("pric")) * number + " 提交订单");

                }
                break;
            case R.id.shopaddorder_imgjian://减数量
                if (number == 1){
                    Toast.makeText( ShopaddorderOfflineActivity.this,"不能再减了!",Toast.LENGTH_SHORT ).show();
                    return;
                }
                number--;
                tv_num.setText( number+"" );
                if (isDk) {
                    tv_allpric.setText("¥" + (Double.parseDouble(getIntent().getStringExtra("pric")) * number - Double.parseDouble(getIntent().getStringExtra("dkq"))*number));
                    tv_allpric1.setText("¥" + (Double.parseDouble(getIntent().getStringExtra("pric")) * number - Double.parseDouble(getIntent().getStringExtra("dkq"))*number));
                    tv_button.setText("¥" + (Double.parseDouble(getIntent().getStringExtra("pric")) * number - Double.parseDouble(getIntent().getStringExtra("dkq"))*number)+  " 提交订单");
                }else{
                    tv_allpric.setText("¥" + Double.parseDouble(getIntent().getStringExtra("pric")) * number);
                    tv_allpric1.setText("¥" + Double.parseDouble(getIntent().getStringExtra("pric")) * number);
                    tv_button.setText("¥" + Double.parseDouble(getIntent().getStringExtra("pric")) * number + " 提交订单");
                }
                break;
            case R.id.shopaddorder_tvbutton://下单0
                tgddxd();
                break;
            case R.id.shopaddorder_lydkq:
                showdkqdialog();
                break;
        }




    }
    //选中抵扣券
    public void showdkqdialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = LayoutInflater.from(this).inflate(R.layout.item_showdialog, null);
        dialog .setView(view);
        dialog.show();
        final TextView etUsername = view.findViewById(R.id.itemdialog_tvone);
        final TextView etPassword = view.findViewById(R.id.itemdialog_tvtwo);
        etUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDk = true;
//                tv_dkq.setText("¥"+getIntent().getStringExtra("dkq"));
                dialog.dismiss();
                if (isDk) {
                    tv_allpric.setText("¥" + (Double.parseDouble(getIntent().getStringExtra("pric")) * number - Double.parseDouble(getIntent().getStringExtra("dkq"))*number));
                    tv_allpric1.setText("¥" + (Double.parseDouble(getIntent().getStringExtra("pric")) * number - Double.parseDouble(getIntent().getStringExtra("dkq"))*number));
                    tv_button.setText("¥" + (Double.parseDouble(getIntent().getStringExtra("pric")) * number - Double.parseDouble(getIntent().getStringExtra("dkq"))*number)+  " 提交订单");
                }else{
                    tv_allpric.setText("¥" + Double.parseDouble(getIntent().getStringExtra("pric")) * number);
                    tv_allpric1.setText("¥" + Double.parseDouble(getIntent().getStringExtra("pric")) * number);
                    tv_button.setText("¥" + Double.parseDouble(getIntent().getStringExtra("pric")) * number + " 提交订单");
                }

            }
        });
        etPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                tv_dkq.setText("不使用抵扣券");
                isDk = false;
                dialog.dismiss();
                if (isDk) {
                    tv_allpric.setText("¥" + (Double.parseDouble(getIntent().getStringExtra("pric")) * number - Double.parseDouble(getIntent().getStringExtra("dkq"))*number));
                    tv_allpric1.setText("¥" + (Double.parseDouble(getIntent().getStringExtra("pric")) * number - Double.parseDouble(getIntent().getStringExtra("dkq"))*number));
                    tv_button.setText("¥" + (Double.parseDouble(getIntent().getStringExtra("pric")) * number - Double.parseDouble(getIntent().getStringExtra("dkq"))*number)+  " 提交订单");
                }else{
                    tv_allpric.setText("¥" + Double.parseDouble(getIntent().getStringExtra("pric")) * number);
                    tv_allpric1.setText("¥" + Double.parseDouble(getIntent().getStringExtra("pric")) * number);
                    tv_button.setText("¥" + Double.parseDouble(getIntent().getStringExtra("pric")) * number + " 提交订单");
                }
            }
        });



    }

    //  团购订单下单
    public void tgddxd(){
        if(TextUtils.isEmpty(et_input.getText().toString().trim())){
            ToastUtils.showLongToast(this,"请输入消费金额");
            return;
        }
        if(Double.valueOf(amount)>Double.valueOf(tv_allpric1.getText().toString().trim())){
            ToastUtils.showLongToast(this,"券金额不能大于消费金额");
            return;
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add( "token",SPUtils.getStringData(ShopaddorderOfflineActivity.this, "token", "") );
        builder.add( "merchant_id", getIntent().getStringExtra( "goodid" ));
        builder.add( "goods_price", et_input.getText().toString());
        builder.add("price","0");
        builder.add("amount",amount2);
        builder.add("allprice",shopaddorder_allpric_pay.getText().toString());
        builder.add("coupon_id",coupon_id);
        RequestBody body = builder.build();
        Request request = new Request.Builder().url( Constants.APP_IP+"/api/O2oOfflineOrder/order").post( body ).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtils.d("dsfasd",result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    if (code.equals("0")){
                        JSONObject jbs = jsonObject.getJSONObject("data");
                        String order_id = jbs.getString("order_id");
//                        String order_num = jbs.getString("order_num");
                        String allprice = jbs.getString("allprice");
                        String title = "订单支付";
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                if (code.equals("0")) {
                                    Intent intent = new Intent(ShopaddorderOfflineActivity.this, MerRechargeActivity.class);
                                    intent.putExtra("ordernum", "test");
                                    intent.putExtra("allprice", allprice);
                                    intent.putExtra("title", title);
                                    intent.putExtra("orderid", order_id);
                                    intent.putExtra("type","offline");
//                                intent.putExtra( "type","0" );
                                    startActivity(intent);
//                                Toast.makeText( ShopaddorderOfflineActivity.this,msg,Toast.LENGTH_SHORT ).show();
//                                } else {
//                                    Toast.makeText(ShopaddorderOfflineActivity.this, msg, Toast.LENGTH_SHORT).show();
//                                }

                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ShopaddorderOfflineActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
