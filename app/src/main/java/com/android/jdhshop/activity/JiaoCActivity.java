package com.android.jdhshop.activity;

import android.view.View;
import android.widget.ImageView;

import com.android.jdhshop.MainActivity;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.suke.widget.SwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JiaoCActivity extends BaseActivity {
    @BindView(R.id.jc1)
    ImageView jc1;
    @BindView(R.id.jc2)
    ImageView jc2;
    @BindView(R.id.jc3)
    ImageView jc3;
    @BindView(R.id.jc4)
    ImageView jc4;
    @Override
    protected void initUI() {
        setContentView(R.layout.jiaocheng);
        ButterKnife.bind(this);

            BaseLogDZiYuan.LogDingZiYuan(jc1, "jc1.jpg");
            BaseLogDZiYuan.LogDingZiYuan(jc2, "jc2.jpg");
            BaseLogDZiYuan.LogDingZiYuan(jc3, "jc3.jpg");
            BaseLogDZiYuan.LogDingZiYuan(jc4, "jc4.jpg");


    }

    @Override
    protected void initData() {

    jc1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            jc1.setVisibility(View.GONE);
        }
    });
        jc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jc2.setVisibility(View.GONE);
            }
        });
        jc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jc3.setVisibility(View.GONE);
            }
        });
        jc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jc4.setVisibility(View.GONE);
                openActivity(MainActivity.class);
                SPUtils.saveStringData(JiaoCActivity.this, "jiaocheng", "1");
                finish();
            }
        });
    }

    @Override
    protected void initListener() {

    }
}
