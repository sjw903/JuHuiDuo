package com.android.jdhshop.merchantactivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
public class ShopaddorderActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title,tv_name,tv_pric,tv_num,tv_allpric,tv_allpric1,tv_phone,tv_button;
    private ImageView iv_img,iv_jia,iv_jian;
    private LinearLayout ly_back;
    private int number = 1;
    private TextView tv_dkq;
    private LinearLayout ly_dkq;

    private boolean isDk = true;


    @Override
    protected void initUI() {
        setContentView(R.layout.activity_shopaddorder);
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
        tv_phone = findViewById( R.id.shopaddorder_phone );
        tv_button = findViewById( R.id.shopaddorder_tvbutton );
        iv_img = findViewById( R.id.shopaddorder_img );
        iv_jia = findViewById( R.id.shopaddorder_imgjia );
        iv_jian = findViewById( R.id.shopaddorder_imgjian );
        ly_back = findViewById( R.id.shopaddorder_lyback );
        tv_dkq = findViewById(R.id.shopaddorder_dkq);
        ly_dkq = findViewById(R.id.shopaddorder_lydkq);
        ly_dkq.setOnClickListener(this);
        ly_back.setOnClickListener( this );
        iv_jia.setOnClickListener( this );
        iv_jian.setOnClickListener( this );
        tv_button.setOnClickListener( this );

        tv_phone.setText( SPUtils.getStringData(ShopaddorderActivity.this, "phone", "").substring( 0,3 )+"****"+SPUtils.getStringData(ShopaddorderActivity.this, "phone", "").substring( 7,11 ) );
        Glide.with( ShopaddorderActivity.this ).load( Constants.APP_IP +getIntent().getStringExtra( "img" ) ).into( iv_img );
        tv_title.setText( getIntent().getStringExtra( "name" ) );
        tv_name.setText( getIntent().getStringExtra( "name" ) );
        tv_pric.setText("¥"+ getIntent().getStringExtra( "pric" ) );
        tv_allpric.setText( "¥"+ (Double.parseDouble(getIntent().getStringExtra( "pric" )) - Double.parseDouble(getIntent().getStringExtra("dkq"))) );
        tv_allpric1.setText("¥"+ (Double.parseDouble(getIntent().getStringExtra( "pric" )) - Double.parseDouble(getIntent().getStringExtra("dkq"))));
        tv_button.setText("¥"+ (Double.parseDouble(getIntent().getStringExtra( "pric" )) - Double.parseDouble(getIntent().getStringExtra("dkq")))+" 提交订单" );
        tv_dkq.setText("¥"+getIntent().getStringExtra("dkq"));
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
                    Toast.makeText( ShopaddorderActivity.this,"不能再减了!",Toast.LENGTH_SHORT ).show();
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
                tv_dkq.setText("¥"+getIntent().getStringExtra("dkq"));
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
                tv_dkq.setText("不使用抵扣券");
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
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add( "token",SPUtils.getStringData(ShopaddorderActivity.this, "token", "") );
        builder.add( "goods_id", getIntent().getStringExtra( "goodid" ));
        builder.add( "num", tv_num.getText().toString());
        if (isDk) {
            builder.add("deduction", getIntent().getStringExtra("dkq"));
        }
//        builder.add( "contact_number", SPUtils.getStringData(ShopaddorderActivity.this, "phone", ""));
//        builder.add( "remark", );

        RequestBody body = builder.build();
        Request request = new Request.Builder().url( Constants.addorder).post( body ).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    if (code.equals("0")){
                        JSONObject jbs = new JSONObject(jsonObject.getString("data"));
                        String order_id = jbs.getString("order_id");
//                        String order_num = jbs.getString("order_num");
                        String allprice = jbs.getString("allprice");
                        String title = jbs.getString("title");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                if (code.equals("0")) {
                                    Intent intent = new Intent(ShopaddorderActivity.this, MerRechargeActivity.class);
                                    intent.putExtra("ordernum", "test");
                                    intent.putExtra("allprice", allprice);
                                    intent.putExtra("title", title);
                                    intent.putExtra("orderid", order_id);
//                                intent.putExtra( "type","0" );
                                    startActivity(intent);
//                                Toast.makeText( ShopaddorderActivity.this,msg,Toast.LENGTH_SHORT ).show();
//                                } else {
//                                    Toast.makeText(ShopaddorderActivity.this, msg, Toast.LENGTH_SHORT).show();
//                                }

                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ShopaddorderActivity.this, msg, Toast.LENGTH_SHORT).show();
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
