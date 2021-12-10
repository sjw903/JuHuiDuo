package com.android.jdhshop.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.ziyuan.ResourceManager;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.tencent.mm.opensdk.utils.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class AttendActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.txt_num)
    TextView txtNum;
    @BindView(R.id.icon_img1)
    ImageView iconImg1;
    @BindView(R.id.icon_img2)
    ImageView iconImg2;
    @BindView(R.id.icon_img3)
    ImageView iconImg3;
    @BindView(R.id.icon_img4)
    ImageView iconImg4;
    @BindView(R.id.icon_img5)
    ImageView iconImg5;
    @BindView(R.id.tv_right)
    TextView tvRight;

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_attend);
        ButterKnife.bind(this);
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("每日签到");
        tvRight.setText("记录");
        tvRight.setVisibility(View.VISIBLE);

        BaseLogDZiYuan.LogDingZiYuan(iconImg1, "icon1.png");
        BaseLogDZiYuan.LogDingZiYuan(iconImg1, "icon2.png");
        BaseLogDZiYuan.LogDingZiYuan(iconImg1, "icon3.png");
        BaseLogDZiYuan.LogDingZiYuan(iconImg1, "icon4.png");
        BaseLogDZiYuan.LogDingZiYuan(iconImg1, "icon5.png");
    }

    @Override
    protected void initData() {
        getData();
    }

    private void getData() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.ATTEND_NUMS, AttendActivity.this,requestParams, new TextHttpResponseHandler() {
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
                    JSONObject jsonObject = new JSONObject(responseString);
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    if (code == 0) {
                        txtNum.setText(jsonObject.getJSONObject("data").getString("continuous_day"));
                        switch (jsonObject.getJSONObject("data").getString("point")) {
                            case "1":
                                iconImg1.setVisibility(View.VISIBLE);
                                iconImg3.setVisibility(View.INVISIBLE);
                                iconImg4.setVisibility(View.INVISIBLE);
                                iconImg5.setVisibility(View.INVISIBLE);
                                iconImg2.setVisibility(View.INVISIBLE);
                                break;
                            case "2":
                                iconImg2.setVisibility(View.VISIBLE);
                                iconImg3.setVisibility(View.INVISIBLE);
                                iconImg4.setVisibility(View.INVISIBLE);
                                iconImg5.setVisibility(View.INVISIBLE);
                                iconImg1.setVisibility(View.INVISIBLE);
                                break;
                            case "3":
                                iconImg3.setVisibility(View.VISIBLE);
                                iconImg1.setVisibility(View.INVISIBLE);
                                iconImg4.setVisibility(View.INVISIBLE);
                                iconImg5.setVisibility(View.INVISIBLE);
                                iconImg2.setVisibility(View.INVISIBLE);

                                break;
                            case "4":
                                iconImg4.setVisibility(View.VISIBLE);
                                iconImg3.setVisibility(View.INVISIBLE);
                                iconImg1.setVisibility(View.INVISIBLE);
                                iconImg5.setVisibility(View.INVISIBLE);
                                iconImg2.setVisibility(View.INVISIBLE);
                                break;
                           default:
                                iconImg5.setVisibility(View.VISIBLE);
                                iconImg3.setVisibility(View.INVISIBLE);
                                iconImg4.setVisibility(View.INVISIBLE);
                                iconImg1.setVisibility(View.INVISIBLE);
                                iconImg2.setVisibility(View.INVISIBLE);
                                break;
                        }
                    } else {
                        T.showShort(AttendActivity.this, msg);
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

    @OnClick({R.id.tv_left, R.id.btn_attend,R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.btn_attend:
                attend();
                break;
            case R.id.tv_right:
                openActivity(AttendRecordActivity.class);
                break;
        }
    }

    private void attend() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.ATTEND, AttendActivity.this,requestParams, new TextHttpResponseHandler() {
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
                    JSONObject jsonObject = new JSONObject(responseString);
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    if (code == 0) {
                        getData();
                        T.showShort(AttendActivity.this, "今日已签到，再接再励!");
                    } else {
                        T.showShort(AttendActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
