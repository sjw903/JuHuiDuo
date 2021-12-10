package com.android.jdhshop.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class KindActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_top)
    LinearLayout llTop;

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_kind_two);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("全部分类");
        getSupportFragmentManager()    //
                .beginTransaction()
                .add(R.id.ll_top,new KindFragment())
                .commit();
    }

    @Override
    protected void initListener() {

    }

    @OnClick(R.id.tv_left)
    public void onViewClicked() {
        finish();
    }
}
