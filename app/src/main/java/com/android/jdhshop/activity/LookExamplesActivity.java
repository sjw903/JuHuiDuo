package com.android.jdhshop.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.config.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LookExamplesActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.lokkex_image1)
    ImageView lokkex_image1;
    @BindView(R.id.lokkex_image2)
    ImageView lokkex_image2;
    @Override
    protected void initUI() {
        setContentView(R.layout.lookexampies_activity);
        ButterKnife.bind(this);
        tv_left.setVisibility(View.VISIBLE);
        tv_title.setText("图片示例");
        BaseLogDZiYuan.LogDingZiYuan(lokkex_image1, "chakanshili2.jpg");
        BaseLogDZiYuan.LogDingZiYuan(lokkex_image2, "chakanshili1.jpg");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @OnClick({R.id.tv_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
        }
    }
}
