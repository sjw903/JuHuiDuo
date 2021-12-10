package com.android.jdhshop.merchantactivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
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

//显示资质许可证
public class ShowxkzActivity extends BaseActivity {

    private ImageView iv_img1,fanhui;
    private LinearLayout ly_back;

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_showxkz);
    }

    @Override
    protected void initData() {
        initView();
    }

    @Override
    protected void initListener() {

    }

    public void initView(){
        iv_img1 = findViewById( R.id.xkz_img1 );
        ly_back = findViewById( R.id.ly_back );
        fanhui = findViewById( R.id.showxkz_fanhui );
        BaseLogDZiYuan.LogDingZiYuan(fanhui, "icon_back.png");
        ly_back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        } );
        getimgs();
    }

    public void getimgs(){

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add( "merchant_id",getIntent().getStringExtra( "id" ) )
                .build();
        Request request = new Request.Builder().url(Constants.getQC).post( body ).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    Log.e("data",result);
                    JSONObject jsonObject = new JSONObject(result);
                    String msg = jsonObject.getString( "msg" );
                    String code = jsonObject.getString( "code" );
                    JSONObject object = jsonObject.getJSONObject( "data" ).getJSONObject("QCMsg");

                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                        if (code.equals( "0" )){
                            try {
                                String img1 = object.getString( "business_license" );
                                Glide.with( ShowxkzActivity.this ).load( Constants.APP_IP+img1.replace("\\","") ).into( iv_img1 );
                                Log.e("data",img1.replace("\\",""));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{

                        }
                        }
                    } );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
