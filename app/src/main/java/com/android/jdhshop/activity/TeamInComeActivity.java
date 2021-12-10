package com.android.jdhshop.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class TeamInComeActivity extends BaseActivity {
    @BindView(R.id.edt_money)
    TextView edtMoney;
    @BindView(R.id.txt_one)
    TextView txtOne;
    @BindView(R.id.txt_two)
    TextView txtTwo;
    @BindView(R.id.txt_three)
    TextView txtThree;
    @BindView(R.id.txt_four)
    TextView txtFour;
    @BindView(R.id.txt_five)
    TextView txtFive;
    @BindView(R.id.txt_six)
    TextView txtSix;
    @BindView(R.id.txt_seven)
    TextView txtSeven;
    @BindView(R.id.txt_eight)
    TextView txtEight;
    @BindView(R.id.txt_nine)
    TextView txtNine;
    @BindView(R.id.txt_ten)
    TextView txtTen;
    @BindView(R.id.txt_eleven)
    TextView txtEleven;
    @BindView(R.id.txt_twelve)
    TextView txtTwelve;
    @BindView(R.id.txt_thirteen)
    TextView txtThirteen;
    @BindView(R.id.txt_fourteen)
    TextView txtFourteen;
    @BindView(R.id.tv_left)
    ImageView fanhui;
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_team_income);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        getData();
        BaseLogDZiYuan.LogDingZiYuan(fanhui, "icon_back_while.png");
    }

    @Override
    protected void initData() {

    }
    private void getData() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.TEAM_INCOME, TeamInComeActivity.this,requestParams, new TextHttpResponseHandler() {
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
                    JSONObject temp2 = new JSONObject(responseString);
                    if(temp2.getInt("code")==0){
                        JSONObject temp = new JSONObject(responseString).getJSONObject("data");
                        edtMoney.setText(temp.getString("team_num"));
                        txtFive.setText(temp.getJSONObject("order_num").getString("allnum"));
                        txtTen.setText(temp.getJSONObject("money").getString("all"));
                        txtOne.setText(String.format(getResources().getString(R.string.test_text), temp.getString("today_num"), "今日"));
                        txtTwo.setText(String.format(getResources().getString(R.string.test_text), temp.getString("yesterday_num"), "昨日"));
                        txtThree.setText(String.format(getResources().getString(R.string.test_text), temp.getString("team_num1"), "一级团队"));
                        txtFour.setText(String.format(getResources().getString(R.string.test_text), temp.getString("team_num2"), "二级团队"));
                        txtSix.setText(String.format(getResources().getString(R.string.test_text), temp.getJSONObject("order_num").getString("today"), "今日"));
                        txtSeven.setText(String.format(getResources().getString(R.string.test_text), temp.getJSONObject("order_num").getString("yesterday"), "昨日"));
                        txtEight.setText(String.format(getResources().getString(R.string.test_text), temp.getJSONObject("order_num").getString("month"), "本月"));
                        txtNine.setText(String.format(getResources().getString(R.string.test_text), temp.getJSONObject("order_num").getString("lastmonth"), "上月"));
                        txtEleven.setText(String.format(getResources().getString(R.string.test_text), temp.getJSONObject("money").getString("today"), "今日"));
                        txtTwelve.setText(String.format(getResources().getString(R.string.test_text), temp.getJSONObject("money").getString("yesterday"), "昨日"));
                        txtThirteen.setText(String.format(getResources().getString(R.string.test_text), temp.getJSONObject("money").getString("month"), "本月"));
                        txtFourteen.setText(String.format(getResources().getString(R.string.test_text), temp.getJSONObject("money").getString("lastmonth"), "上月"));
                    }else{
                        showToast(temp2.getString("msg"));
                        if("用户不存在".equals(temp2.getString("msg"))){
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
    protected void initListener() {

    }
    @OnClick(R.id.tv_left)
    public void onViewClicked() {
        finish();
    }
}
