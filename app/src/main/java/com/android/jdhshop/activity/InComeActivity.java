package com.android.jdhshop.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.BannerBean;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.my.MyOrderActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class InComeActivity extends BaseActivity {
    @BindView(R.id.edt_money)
    TextView edtMoney;
    @BindView(R.id.txt_may_money)
    TextView txtMayMoney;
    @BindView(R.id.txt_today_money)
    TextView txtTodayMoney;
    @BindView(R.id.txt_ye)
    TextView txtYe;
    @BindView(R.id.txt_order_num)
    TextView txtOrderNum;
    @BindView(R.id.txt_pay_money)
    TextView txtPayMoney;
    @BindView(R.id.txt_actual_money)
    TextView txtActualMoney;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    JSONObject object;

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_income);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        radioGroup.check(R.id.radio_one);
        radioGroup.setClickable(false);
        getData();
    }

    @Override
    protected void initData() {
//        edtMoney.setText("¥" + SPUtils.getStringData(this, "my_money_one", "0.00"));
//        txtYe.setText("账户余额(元): ¥" + SPUtils.getStringData(this, "my_money_four", "0.00"));
//        txtMayMoney.setText("¥" + SPUtils.getStringData(this, "my_money_two", "0.00"));
//        txtTodayMoney.setText("¥" + SPUtils.getStringData(this, "my_money_three", "0.00"));
    }

    @Override
    protected void initListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(object==null){
                    return;
                }
                try {
                    switch (checkedId) {
                        case R.id.radio_one:
                            txtOrderNum.setText(object.getJSONObject("today").getString("num"));
                            txtPayMoney.setText(object.getJSONObject("today").getString("amount1"));
                            txtActualMoney.setText(object.getJSONObject("today").getString("amount2"));
                            break;
                        case R.id.radio_two:
                            txtOrderNum.setText(object.getJSONObject("yesterday").getString("num"));
                            txtPayMoney.setText(object.getJSONObject("yesterday").getString("amount1"));
                            txtActualMoney.setText(object.getJSONObject("yesterday").getString("amount2"));
                            break;
                        case R.id.radio_three:
                            txtOrderNum.setText(object.getJSONObject("sevenday").getString("num"));
                            txtPayMoney.setText(object.getJSONObject("sevenday").getString("amount1"));
                            txtActualMoney.setText(object.getJSONObject("sevenday").getString("amount2"));
                            break;
                        case R.id.radio_four:
                            txtOrderNum.setText(object.getJSONObject("lastmonth").getString("num"));
                            txtPayMoney.setText(object.getJSONObject("lastmonth").getString("amount1"));
                            txtActualMoney.setText(object.getJSONObject("lastmonth").getString("amount2"));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getData() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.INCOME_STATICS,InComeActivity.this, requestParams, new TextHttpResponseHandler() {
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
                    JSONObject temp = new JSONObject(responseString);
                    if(temp.getInt("code")==0){
                        radioGroup.setClickable(true);
                        object = temp.getJSONObject("data");
                        edtMoney.setText("¥" + object.getString("amount"));
                        txtYe.setText("账户余额(元): ¥" + object.getString("balance"));
                        txtMayMoney.setText("¥" + object.getString("amount_last_finish"));
                        txtTodayMoney.setText("¥" +object.getString("amount_current"));
                        txtOrderNum.setText(object.getJSONObject("today").getString("num"));
                        txtPayMoney.setText(object.getJSONObject("today").getString("amount1"));
                        txtActualMoney.setText(object.getJSONObject("today").getString("amount2"));
                    }else{
                        showToast(temp.getString("msg"));
                        if("用户不存在".equals(temp.getString("msg"))){
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    object = null;
                    showToast("获取收益失败");
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.tv_left, R.id.tv_team, R.id.tv_commission, R.id.tv_put_forward})
    public void onViewClicked(View view) {
        Intent intent = new Intent(this, CommissionActivity.class);
        ;
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.tv_team:
                openActivity(TeamInComeActivity.class);
                break;
            case R.id.tv_commission:
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.tv_put_forward:
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
        }
    }
}
