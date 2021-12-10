package com.android.jdhshop.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.common.LogUtils;
import com.bumptech.glide.Glide;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.UserBean;
import com.android.jdhshop.bean.UserInfoBean;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.my.MyInformationActivity;
import com.android.jdhshop.my.MyShareUrlActivity;
import com.android.jdhshop.widget.CircleImageView;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class DailyBonusActivity extends BaseActivity {
    @BindView(R.id.civ_head)
    CircleImageView civHead;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.txt_num)
    TextView txtNum;
    @BindView(R.id.img_vip)
    ImageView imgVip;
    @BindView(R.id.txt_group_name)
    TextView txtGroupName;
    @BindView(R.id.txt_one)
    TextView txtOne;
    @BindView(R.id.txt_qd)
    TextView txtQd;
    @BindView(R.id.txt_qdone)
    TextView txtQdOne;
    @BindView(R.id.line_one)
    View lineOne;
    @BindView(R.id.txt_two)
    TextView txtTwo;
    @BindView(R.id.line_two)
    View lineTwo;
    @BindView(R.id.txt_three)
    TextView txtThree;
    @BindView(R.id.line_three)
    View lineThree;
    @BindView(R.id.txt_info)
    TextView txtInfo;
    @BindView(R.id.txt_four)
    TextView txtFour;
    @BindView(R.id.line_four)
    View lineFour;
    @BindView(R.id.txt_five)
    TextView txtFive;
    @BindView(R.id.line_five)
    View lineFive;
    @BindView(R.id.txt_six)
    TextView txtSix;
    @BindView(R.id.line_six)
    View lineSix;
    @BindView(R.id.txt_seven)
    TextView txtSeven;
    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.qiand)
    ImageView qiand;
    @BindView(R.id.tuijian)
    ImageView tuijian;
    @BindView(R.id.ziliao)
    ImageView ziliao;
    @Override
    protected void initUI() {
        setStatusBar(getResources().getColor(R.color.red));
        setContentView(R.layout.activity_daily_bonus);
        ButterKnife.bind(this);

        BaseLogDZiYuan.LogDingZiYuan(img_back, "icon_back_while.png");
        BaseLogDZiYuan.LogDingZiYuan(qiand, "mission_qd.png");
        BaseLogDZiYuan.LogDingZiYuan(tuijian, "tuijian.png");
        BaseLogDZiYuan.LogDingZiYuan(ziliao, "ziliao.png");
    }

    @Override
    protected void initData() {
        getData();
        RequestParams params = new RequestParams();
        HttpUtils.post(Constants.GET_USER_MSG,DailyBonusActivity.this, params, new TextHttpResponseHandler() {
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
                LogUtils.d("dfasd",responseString);
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
                            if("Y".equals(userBean.user_msg.is_complete_info)){
                                txtInfo.setText("已完成");
                                txtInfo.setEnabled(false);
                                txtInfo.setBackground(getResources().getDrawable(R.drawable.aa));
                            }
                            CaiNiaoApplication.setUserInfoBean(userBean);
                        }
                        if (null != userBean) {
                            CaiNiaoApplication.setUserBean(new UserBean(userBean.user_detail.user_id, userBean.user_msg.group_id, SPUtils.getStringData(DailyBonusActivity.this, "token", ""), userBean.user_detail.avatar, userBean.user_detail.nickname, userBean.user_msg.is_forever));
                            if (CaiNiaoApplication.getUserBean() != null) {
                                tvUsername.setText(CaiNiaoApplication.getUserBean().getName());
                                Glide.with(DailyBonusActivity.this).load(CaiNiaoApplication.getUserBean().getAvatar()).placeholder(R.drawable.no_banner).error(R.drawable.no_banner).dontAnimate().into(civHead);
                            }
//                            String temp = "";
//                            switch (userBean.user_msg.group_id) {
//                                case "1":
//                                    temp = "蓝领";
//                                    imgVip.setImageResource(R.mipmap.lanling);
//                                    break;
//                                case "2":
//                                    temp = "白领";
//                                    imgVip.setImageResource(R.mipmap.bailing);
//
//                                    break;
//                                case "3":
//                                    temp = "金领";
//                                    imgVip.setImageResource(R.mipmap.jinling);
//
//                                    break;
//                                case "4":
//                                    temp = "总经理";
//                                    imgVip.setImageResource(R.mipmap.zongjingli);
//
//                                    break;
//                                case "5":
//                                    temp = "CEO";
//                                    imgVip.setImageResource(R.mipmap.ceo);
//
//                                    break;
//                                case "6":
//                                    temp = "总裁";
//                                    imgVip.setImageResource(R.mipmap.zongcai);
//                                    break;
//                            }
                            txtGroupName.setText(userBean.user_msg.group_name);
                        }
                    } else {
                        showToast(msg);
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

    @Override
    protected void initListener() {

    }

    private void getData() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.ATTEND_NUMS,DailyBonusActivity.this, requestParams, new TextHttpResponseHandler() {
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
                        txtNum.setText("您已连续签到" + jsonObject.getJSONObject("data").getString("continuous_day") + "天");
                        switch (jsonObject.getJSONObject("data").getString("continuous_day")) {
                            case "0":
                                txtOne.setBackgroundResource(R.mipmap.yinbi);
                                txtTwo.setBackgroundResource(R.mipmap.yinbi);
                                txtThree.setBackgroundResource(R.mipmap.yinbi);
                                txtFour.setBackgroundResource(R.mipmap.yinbi);
                                txtFive.setBackgroundResource(R.mipmap.yinbi);
                                txtSix.setBackgroundResource(R.mipmap.yinbi);
                                txtSeven.setBackgroundResource(R.mipmap.yinbi);
                                txtOne.setTextColor(getResources().getColor(R.color.col_333));
                                txtTwo.setTextColor(getResources().getColor(R.color.col_333));
                                txtThree.setTextColor(getResources().getColor(R.color.col_333));
                                txtFour.setTextColor(getResources().getColor(R.color.col_333));
                                txtFive.setTextColor(getResources().getColor(R.color.col_333));
                                txtSix.setTextColor(getResources().getColor(R.color.col_333));
                                txtSeven.setTextColor(getResources().getColor(R.color.col_333));
                                lineOne.setBackgroundColor(getResources().getColor(R.color.white));
                                lineTwo.setBackgroundColor(getResources().getColor(R.color.white));
                                lineThree.setBackgroundColor(getResources().getColor(R.color.white));
                                lineFour.setBackgroundColor(getResources().getColor(R.color.white));
                                lineFive.setBackgroundColor(getResources().getColor(R.color.white));
                                lineSix.setBackgroundColor(getResources().getColor(R.color.white));
                                break;
                            case "1":
                                txtOne.setBackgroundResource(R.mipmap.jinbi);
                                txtTwo.setBackgroundResource(R.mipmap.yinbi);
                                txtThree.setBackgroundResource(R.mipmap.yinbi);
                                txtFour.setBackgroundResource(R.mipmap.yinbi);
                                txtFive.setBackgroundResource(R.mipmap.yinbi);
                                txtSix.setBackgroundResource(R.mipmap.yinbi);
                                txtSeven.setBackgroundResource(R.mipmap.yinbi);
                                txtOne.setTextColor(getResources().getColor(R.color.white));
                                txtTwo.setTextColor(getResources().getColor(R.color.col_333));
                                txtThree.setTextColor(getResources().getColor(R.color.col_333));
                                txtFour.setTextColor(getResources().getColor(R.color.col_333));
                                txtFive.setTextColor(getResources().getColor(R.color.col_333));
                                txtSix.setTextColor(getResources().getColor(R.color.col_333));
                                txtSeven.setTextColor(getResources().getColor(R.color.col_333));
                                lineOne.setBackgroundColor(getResources().getColor(R.color.white));
                                lineTwo.setBackgroundColor(getResources().getColor(R.color.white));
                                lineThree.setBackgroundColor(getResources().getColor(R.color.white));
                                lineFour.setBackgroundColor(getResources().getColor(R.color.white));
                                lineFive.setBackgroundColor(getResources().getColor(R.color.white));
                                lineSix.setBackgroundColor(getResources().getColor(R.color.white));
                                break;
                            case "2":
                                txtOne.setBackgroundResource(R.mipmap.jinbi);
                                txtTwo.setBackgroundResource(R.mipmap.jinbi);
                                txtThree.setBackgroundResource(R.mipmap.yinbi);
                                txtFour.setBackgroundResource(R.mipmap.yinbi);
                                txtFive.setBackgroundResource(R.mipmap.yinbi);
                                txtSix.setBackgroundResource(R.mipmap.yinbi);
                                txtSeven.setBackgroundResource(R.mipmap.yinbi);
                                txtOne.setTextColor(getResources().getColor(R.color.white));
                                txtTwo.setTextColor(getResources().getColor(R.color.white));
                                txtThree.setTextColor(getResources().getColor(R.color.col_333));
                                txtFour.setTextColor(getResources().getColor(R.color.col_333));
                                txtFive.setTextColor(getResources().getColor(R.color.col_333));
                                txtSix.setTextColor(getResources().getColor(R.color.col_333));
                                txtSeven.setTextColor(getResources().getColor(R.color.col_333));
                                lineOne.setBackgroundColor(getResources().getColor(R.color.orange));
                                lineTwo.setBackgroundColor(getResources().getColor(R.color.white));
                                lineThree.setBackgroundColor(getResources().getColor(R.color.white));
                                lineFour.setBackgroundColor(getResources().getColor(R.color.white));
                                lineFive.setBackgroundColor(getResources().getColor(R.color.white));
                                lineSix.setBackgroundColor(getResources().getColor(R.color.white));
                                break;
                            case "3":
                                txtOne.setBackgroundResource(R.mipmap.jinbi);
                                txtTwo.setBackgroundResource(R.mipmap.jinbi);
                                txtThree.setBackgroundResource(R.mipmap.jinbi);
                                txtFour.setBackgroundResource(R.mipmap.yinbi);
                                txtFive.setBackgroundResource(R.mipmap.yinbi);
                                txtSix.setBackgroundResource(R.mipmap.yinbi);
                                txtSeven.setBackgroundResource(R.mipmap.yinbi);
                                txtOne.setTextColor(getResources().getColor(R.color.white));
                                txtTwo.setTextColor(getResources().getColor(R.color.white));
                                txtThree.setTextColor(getResources().getColor(R.color.white));
                                txtFour.setTextColor(getResources().getColor(R.color.col_333));
                                txtFive.setTextColor(getResources().getColor(R.color.col_333));
                                txtSix.setTextColor(getResources().getColor(R.color.col_333));
                                txtSeven.setTextColor(getResources().getColor(R.color.col_333));
                                lineOne.setBackgroundColor(getResources().getColor(R.color.orange));
                                lineTwo.setBackgroundColor(getResources().getColor(R.color.orange));
                                lineThree.setBackgroundColor(getResources().getColor(R.color.white));
                                lineFour.setBackgroundColor(getResources().getColor(R.color.white));
                                lineFive.setBackgroundColor(getResources().getColor(R.color.white));
                                lineSix.setBackgroundColor(getResources().getColor(R.color.white));
                                break;
                            case "4":
                                txtOne.setBackgroundResource(R.mipmap.jinbi);
                                txtTwo.setBackgroundResource(R.mipmap.jinbi);
                                txtThree.setBackgroundResource(R.mipmap.jinbi);
                                txtFour.setBackgroundResource(R.mipmap.jinbi);
                                txtFive.setBackgroundResource(R.mipmap.yinbi);
                                txtSix.setBackgroundResource(R.mipmap.yinbi);
                                txtSeven.setBackgroundResource(R.mipmap.yinbi);
                                txtOne.setTextColor(getResources().getColor(R.color.white));
                                txtTwo.setTextColor(getResources().getColor(R.color.white));
                                txtThree.setTextColor(getResources().getColor(R.color.white));
                                txtFour.setTextColor(getResources().getColor(R.color.white));
                                txtFive.setTextColor(getResources().getColor(R.color.col_333));
                                txtSix.setTextColor(getResources().getColor(R.color.col_333));
                                txtSeven.setTextColor(getResources().getColor(R.color.col_333));
                                lineOne.setBackgroundColor(getResources().getColor(R.color.orange));
                                lineTwo.setBackgroundColor(getResources().getColor(R.color.orange));
                                lineThree.setBackgroundColor(getResources().getColor(R.color.orange));
                                lineFour.setBackgroundColor(getResources().getColor(R.color.white));
                                lineFive.setBackgroundColor(getResources().getColor(R.color.white));
                                lineSix.setBackgroundColor(getResources().getColor(R.color.white));
                                break;
                            case "5":
                                txtOne.setBackgroundResource(R.mipmap.jinbi);
                                txtTwo.setBackgroundResource(R.mipmap.jinbi);
                                txtThree.setBackgroundResource(R.mipmap.jinbi);
                                txtFour.setBackgroundResource(R.mipmap.jinbi);
                                txtFive.setBackgroundResource(R.mipmap.jinbi);
                                txtSix.setBackgroundResource(R.mipmap.yinbi);
                                txtSeven.setBackgroundResource(R.mipmap.yinbi);
                                txtOne.setTextColor(getResources().getColor(R.color.white));
                                txtTwo.setTextColor(getResources().getColor(R.color.white));
                                txtThree.setTextColor(getResources().getColor(R.color.white));
                                txtFour.setTextColor(getResources().getColor(R.color.white));
                                txtFive.setTextColor(getResources().getColor(R.color.white));
                                txtSix.setTextColor(getResources().getColor(R.color.col_333));
                                txtSeven.setTextColor(getResources().getColor(R.color.col_333));
                                lineOne.setBackgroundColor(getResources().getColor(R.color.orange));
                                lineTwo.setBackgroundColor(getResources().getColor(R.color.orange));
                                lineThree.setBackgroundColor(getResources().getColor(R.color.orange));
                                lineFour.setBackgroundColor(getResources().getColor(R.color.orange));
                                lineFive.setBackgroundColor(getResources().getColor(R.color.white));
                                lineSix.setBackgroundColor(getResources().getColor(R.color.white));
                                break;
                            case "6":
                                txtOne.setBackgroundResource(R.mipmap.jinbi);
                                txtTwo.setBackgroundResource(R.mipmap.jinbi);
                                txtThree.setBackgroundResource(R.mipmap.jinbi);
                                txtFour.setBackgroundResource(R.mipmap.jinbi);
                                txtFive.setBackgroundResource(R.mipmap.jinbi);
                                txtSix.setBackgroundResource(R.mipmap.jinbi);
                                txtSeven.setBackgroundResource(R.mipmap.yinbi);
                                txtOne.setTextColor(getResources().getColor(R.color.white));
                                txtTwo.setTextColor(getResources().getColor(R.color.white));
                                txtThree.setTextColor(getResources().getColor(R.color.white));
                                txtFour.setTextColor(getResources().getColor(R.color.white));
                                txtFive.setTextColor(getResources().getColor(R.color.white));
                                txtSix.setTextColor(getResources().getColor(R.color.white));
                                txtSeven.setTextColor(getResources().getColor(R.color.col_333));
                                lineOne.setBackgroundColor(getResources().getColor(R.color.orange));
                                lineTwo.setBackgroundColor(getResources().getColor(R.color.orange));
                                lineThree.setBackgroundColor(getResources().getColor(R.color.orange));
                                lineFour.setBackgroundColor(getResources().getColor(R.color.orange));
                                lineFive.setBackgroundColor(getResources().getColor(R.color.orange));
                                lineSix.setBackgroundColor(getResources().getColor(R.color.white));
                                break;
                            default:
                                txtOne.setBackgroundResource(R.mipmap.jinbi);
                                txtTwo.setBackgroundResource(R.mipmap.jinbi);
                                txtThree.setBackgroundResource(R.mipmap.jinbi);
                                txtFour.setBackgroundResource(R.mipmap.jinbi);
                                txtFive.setBackgroundResource(R.mipmap.jinbi);
                                txtSix.setBackgroundResource(R.mipmap.jinbi);
                                txtSeven.setBackgroundResource(R.mipmap.jinbi);
                                txtOne.setTextColor(getResources().getColor(R.color.white));
                                txtTwo.setTextColor(getResources().getColor(R.color.white));
                                txtThree.setTextColor(getResources().getColor(R.color.white));
                                txtFour.setTextColor(getResources().getColor(R.color.white));
                                txtFive.setTextColor(getResources().getColor(R.color.white));
                                txtSix.setTextColor(getResources().getColor(R.color.white));
                                txtSeven.setTextColor(getResources().getColor(R.color.white));
                                lineOne.setBackgroundColor(getResources().getColor(R.color.orange));
                                lineTwo.setBackgroundColor(getResources().getColor(R.color.orange));
                                lineThree.setBackgroundColor(getResources().getColor(R.color.orange));
                                lineFour.setBackgroundColor(getResources().getColor(R.color.orange));
                                lineFive.setBackgroundColor(getResources().getColor(R.color.orange));
                                lineSix.setBackgroundColor(getResources().getColor(R.color.orange));
                                break;
                        }
                    } else {
                        T.showShort(DailyBonusActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.img_back, R.id.txt_qdone, R.id.ll_info, R.id.txt_qd, R.id.txt_gw, R.id.txt_tuijian,R.id.txt_record, R.id.txt_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_record:
                openActivity(AttendRecordActivity.class);
                break;
            case R.id.ll_info:
                break;
            case R.id.txt_qdone:
            case R.id.txt_qd:
                attend();
                break;
            case R.id.txt_gw:
                showToast("商城功能暂未开放");
//                openActivity(ShopMallActivity.class);
                break;
            case R.id.txt_tuijian:
                openActivity(MyShareUrlActivity.class);
                break;
            case R.id.txt_info:
                openActivity(MyInformationActivity.class);
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        isAttend();
    }
    private void isAttend() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.isSignToday,DailyBonusActivity.this, requestParams, new TextHttpResponseHandler() {
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
                LogUtils.d("dfasd",responseString);
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    if (code == 0) {
                        if("Y".equals(jsonObject.getJSONObject("data").getString("is_sign"))){
                            txtQd.setText("已签到");
                            txtQdOne.setText("已签到");
                            txtQd.setEnabled(false);
                            txtQdOne.setEnabled(false);
                            txtQd.setBackground(getResources().getDrawable(R.drawable.aa));
                        }else{
                            txtQd.setText("签到");
                            txtQdOne.setText("立即签到");
                            txtQd.setEnabled(true);
                            txtQdOne.setEnabled(true);
                            txtQd.setBackground(getResources().getDrawable(R.drawable.asd));
                        }
                    } else {
                        T.showShort(DailyBonusActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void attend() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.ATTEND,DailyBonusActivity.this, requestParams, new TextHttpResponseHandler() {
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
                        isAttend();
                        T.showShort(DailyBonusActivity.this, "今日已签到，再接再励!");
                    } else {
                        T.showShort(DailyBonusActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
