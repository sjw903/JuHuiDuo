package com.android.jdhshop.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.bean.UserInfoBean;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.my.BalanceActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class QdActivity extends BaseActivity {

    @BindView(R.id.txt_money)
    TextView txtMoney;
    //    @BindView(R.id.txt_mess)
//    TextView txtMess;
    @BindView(R.id.img_top)
    ImageView img_top;
    @BindView(R.id.qid_yi)
    ImageView qid_yi;
    @BindView(R.id.jinbi)
    ImageView jinbi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_qidong);
        ButterKnife.bind(this);
        txtMoney.setText(getIntent().getStringExtra("money"));
//        txtMess.setText(getIntent().getStringExtra("mess"));
        findViewById(R.id.tv_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        BaseLogDZiYuan.LogDingZiYuan(img_top, "img_top.png");
        BaseLogDZiYuan.LogDingZiYuan(qid_yi,"qid_yi.png");
        BaseLogDZiYuan.LogDingZiYuan(jinbi,"jinbi.png");

    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.btn_ok)
    public void onViewClicked() {
        UserInfoBean userBean = CaiNiaoApplication.getUserInfoBean();
        Bundle b = new Bundle();
        if (null != userBean && null != userBean.user_msg) {
            b.putString("balance", userBean.user_msg.balance);
            b.putString("user", userBean.user_msg.balance_user);
            b.putString("service", userBean.user_msg.balance_service);
            b.putString("plantform", userBean.user_msg.balance_plantform);
        }
        Intent intent = new Intent(this, BalanceActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }
}
