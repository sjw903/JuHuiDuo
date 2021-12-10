package com.android.jdhshop.fragments;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.BindActivity;
import com.android.jdhshop.activity.CommissionPhbActivity;
import com.android.jdhshop.activity.FeedBackActivity;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.bean.GroupListBean;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.UserBean;
import com.android.jdhshop.bean.UserInfoBean;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.my.PutForwardActivity;
import com.android.jdhshop.my.RechargeActivity;
import com.android.jdhshop.widget.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class VIPFragment extends BaseLazyFragment {
    @BindView(R.id.civ_head)
    CircleImageView civHead;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.txt_huiyuan)
    TextView txtHuiyuan;
    @BindView(R.id.txt_code)
    TextView txtCode;
    @BindView(R.id.txt_jie)
    TextView txtJie;
    @BindView(R.id.txt_ye)
    TextView txtYe;
    @BindView(R.id.txt_may_money)
    TextView txtMayMoney;
    @BindView(R.id.txt_last_may)
    TextView txtLastMay;
    @BindView(R.id.txt_today_money)
    TextView txtTodayMoney;
    @BindView(R.id.txt_month_money)
    TextView txtMonthMoney;
    @BindView(R.id.txt_grade)
    TextView txtGrade;
    @BindView(R.id.pb_progressbar)
    ProgressBar pbProgressbar;
    @BindView(R.id.ll_12)
    LinearLayout ll12;
    @BindView(R.id.btn_copy)
    TextView btnCopy;
    @BindView(R.id.txt_name_group_one)
    TextView txtNameGroupOne;
    @BindView(R.id.txt_name_group_two)
    TextView txtNameGroupTwo;
    @BindView(R.id.txt_name_group_three)
    TextView txtNameGroupThree;
    @BindView(R.id.txt_name_group_four)
    TextView txtNameGroupFour;
    @BindView(R.id.txt_exp_group_one)
    TextView txtexpGroupOne;
    @BindView(R.id.txt_exp_group_two)
    TextView txtexpGroupTwo;
    @BindView(R.id.txt_exp_group_three)
    TextView txtexpGroupThree;
    @BindView(R.id.txt_exp_group_four)
    TextView txtexpGroupFour;
    @BindView(R.id.txt_tip)
    TextView txtTip;
    private View view;
    @BindView(R.id.lift)
    ImageView lift;
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_vip, container, false);
        ButterKnife.bind(this, view);
        view.findViewById(R.id.img_feed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FeedBackActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
            }
        });
        Glide.with(context).load(R.drawable.gif_list).asGif().into(lift);
        lift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(CommissionPhbActivity.class);
            }
        });
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(txtCode.getText().toString().trim());
                T.showShort(context, "复制成功，快去邀请好友吧");
            }
        });
        return view;
    }

    @Override
    protected void lazyload() {
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!CommonUtils.isNetworkAvailable()) {
            showToast(getResources().getString(R.string.error_network));
            return;
        }
        if("".equals(SPUtils.getStringData(context, "token", ""))){
            return;
        }
        getGroupList();
        RequestParams params = new RequestParams();
        HttpUtils.post(Constants.GET_USER_MSG,VIPFragment.this, params, new TextHttpResponseHandler() {
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
                            CaiNiaoApplication.setUserInfoBean(userBean);
                        }
                        if (null != userBean) {
                            CaiNiaoApplication.setUserBean(new UserBean(userBean.user_detail.user_id, userBean.user_msg.group_id, SPUtils.getStringData(context, "token", ""), userBean.user_detail.avatar, userBean.user_detail.nickname, userBean.user_msg.is_forever));
                            if (CaiNiaoApplication.getUserBean() != null) {
                                if (!TextUtils.isEmpty(userBean.user_detail.nickname)) {
                                    if(isBase64(userBean.user_detail.nickname)){
                                        tvUsername.setText(base64Decode(userBean.user_detail.nickname,"utf-8"));
                                    }else{
                                        tvUsername.setText(userBean.user_detail.nickname);
                                    }
                                } else {
                                    tvUsername.setText(userBean.user_msg.phone);
                                }
                                txtCode.setText(CaiNiaoApplication.getUserInfoBean().user_msg.auth_code);
                                if(CaiNiaoApplication.getUserBean().getAvatar()==null||CaiNiaoApplication.getUserBean().getAvatar().equals("")){
                                    Glide.with(context).load(SPUtils.getStringData(context,"default_avatar","")).placeholder(R.mipmap.icon_defult_boy).error(R.mipmap.icon_defult_boy).dontAnimate().into(civHead);
                                }else{
                                    Glide.with(context).load(CaiNiaoApplication.getUserBean().getAvatar()).placeholder(R.mipmap.icon_defult_boy).error(R.mipmap.icon_defult_boy).dontAnimate().into(civHead);
                                }
                                if ("3".equals(CaiNiaoApplication.getUserBean().getGroup_id())) {
                                    ll12.setVisibility(View.GONE);
                                } else if ("4".equals(CaiNiaoApplication.getUserBean().getGroup_id())) {
                                    ll12.setVisibility(View.GONE);
                                } else if ("2".equals(CaiNiaoApplication.getUserBean().getGroup_id())) {
                                    ll12.setVisibility(View.VISIBLE);
                                } else {
                                    ll12.setVisibility(View.GONE);
                                }
                                txtHuiyuan.setText(userBean.user_msg.group_name);
                            }
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
    private void getGroupList() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.GROUP_LIST,VIPFragment.this, requestParams, new onOKJsonHttpResponseHandler<GroupListBean>(new TypeToken<Response<GroupListBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(throwable.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Response<GroupListBean> datas) {
                if (datas.isSuccess()) {
                    List<GroupListBean.Item> items=datas.getData().list;
                    for (int i=0;i<items.size();i++){
                        switch (i){
                            case 0:
                                txtNameGroupOne.setText(items.get(i).title);
                                txtexpGroupOne.setText("0");
                                break;
                            case 1:
                                txtNameGroupTwo.setText(items.get(i).title);
                                txtexpGroupTwo.setText(items.get(i).exp);
                                break;
                            case 2:
                                txtNameGroupThree.setText(items.get(i).title);
                                pbProgressbar.setMax(Integer.valueOf(items.get(i).exp));
                                txtexpGroupThree.setText(items.get(i).exp);
                                break;
                            case 3:
                                txtNameGroupFour.setText(items.get(i).title);
                                pbProgressbar.setMax(Integer.valueOf(items.get(i).exp));
                                txtexpGroupFour.setText(items.get(i).exp);
                                txtTip.setText("——升级至"+items.get(i).title+"·4大权益——");
                                break;
                        }
                    }
                    getVipData();
                } else {
                    showToast(datas.getMsg());
                }
            }
        });
    }
    private static boolean isBase64(String str) {
        String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        return Pattern.matches(base64Pattern, str);
    }
    /**
     * Base64解密字符串
     * @param content -- 待解密字符串
     * @param charsetName -- 字符串编码方式
     * @return
     */
    private String base64Decode(String content, String charsetName) {
        if (TextUtils.isEmpty(charsetName)) {
            charsetName = "UTF-8";
        }
        byte[] contentByte = Base64.decode(content, Base64.DEFAULT);
        try {
            return new String(contentByte, charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
    private void getVipData() {
        RequestParams params = new RequestParams();
        HttpUtils.post(Constants.GET_VIP_DATA,VIPFragment.this, params, new TextHttpResponseHandler() {
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
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    //返回码
                    int code = jsonObject.optInt("code");
                    //返回码说明
                    String msg = jsonObject.optString("msg");
                    if (0 == code) {
                        txtYe.setText("余额: " + jsonObject.getJSONObject("data").getString("balance"));
                        txtGrade.setText(jsonObject.getJSONObject("data").getString("exp"));
                        pbProgressbar.setProgress(Integer.valueOf(jsonObject.getJSONObject("data").getString("exp")));
                        txtLastMay.setText(jsonObject.getJSONObject("data").getString("amount_last"));
                        txtMayMoney.setText(jsonObject.getJSONObject("data").getString("amount_current"));
                        txtMonthMoney.setText(jsonObject.getJSONObject("data").getString("amount_last_finish"));
                        txtTodayMoney.setText(jsonObject.getJSONObject("data").getString("amount_today"));
                        txtJie.setText(getString(R.string.app_name) +"已为您节省 " + jsonObject.getJSONObject("data").getString("amount") + " 元");
                        SPUtils.saveStringData(context, "my_money_one", jsonObject.getJSONObject("data").getString("amount"));
                        SPUtils.saveStringData(context, "my_money_two", jsonObject.getJSONObject("data").getString("amount_last"));
                        SPUtils.saveStringData(context, "my_money_three", jsonObject.getJSONObject("data").getString("amount_current"));
                        SPUtils.saveStringData(context, "my_money_four", jsonObject.getJSONObject("data").getString("balance"));

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

    @OnClick({R.id.btn_tx, R.id.txt_open})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_tx:
                if (CaiNiaoApplication.getUserInfoBean().user_msg.alipay_account == null) {
                    openActivity(BindActivity.class);
                } else {
                    Bundle b = new Bundle();
                    b.putString("balance", txtYe.getText().toString().replace("余额: ", ""));
                    openActivity(PutForwardActivity.class, b);
                }
                break;
            case R.id.txt_open:
                if (txtHuiyuan.getText().equals(getString(R.string.app_name)) || txtHuiyuan.getText().equals(getString(R.string.app_name))) {
                    T.showShort(context, "您已经是终身VIP啦");
                    return;
                }
                openActivity(RechargeActivity.class);
                break;
        }
    }
}
