package com.android.jdhshop.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangeShareMuBanActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.txt_edit)
    TextView txtEdit;
    @BindView(R.id.et_wenan1)
    EditText etWenan1;
    @BindView(R.id.et_wenan2)
    EditText etWenan2;
    @BindView(R.id.et_wenan3)
    EditText etWenan3;
    @BindView(R.id.et_wenan4)
    EditText etWenan4;
    @BindView(R.id.et_wenan5)
    EditText etWenan5;

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_change_share);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("编辑分享模板");
    }

    @Override
    protected void onResume() {
        super.onResume();
        String one = SPUtils.getStringData(this, "mbone", "");
        String two = SPUtils.getStringData(this, "mbtwo", "");
        String three = SPUtils.getStringData(this, "mbthree", "");
        String four = SPUtils.getStringData(this, "mbfour", "");
        String five = SPUtils.getStringData(this, "mbfive", "");

        etWenan1.setText(Html.fromHtml(one));
        etWenan2.setText(Html.fromHtml(two));
        etWenan3.setText(Html.fromHtml(three));
        etWenan4.setText(Html.fromHtml(four));
        etWenan5.setText(Html.fromHtml(five));
    }

    @Override
    protected void initListener() {

    }

    @OnClick({R.id.tv_left, R.id.txt_edit, R.id.btn_copy, R.id.btn_invite})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.txt_edit:
                SPUtils.saveStringData(this, "mbone", "【{标题}】");
                SPUtils.saveStringData(this, "mbtwo", "【在售价】{商品原价}");
                SPUtils.saveStringData(this, "mbthree", "【券后价】{券后价}");
                SPUtils.saveStringData(this, "mbfour", "【下载"+ getString(R.string.app_name) +"最高省】{佣金}元");
                SPUtils.saveStringData(this, "mbfive", "複~製这条信息{淘口令}打开手机Tao寶即可查看");
                onResume();
                break;
            case R.id.btn_copy:
                break;
            case R.id.btn_invite:
                SPUtils.saveStringData(this, "mbone", etWenan1.getText().toString().trim());
                SPUtils.saveStringData(this, "mbtwo", etWenan2.getText().toString().trim());
                SPUtils.saveStringData(this, "mbthree", etWenan3.getText().toString().trim());
                SPUtils.saveStringData(this, "mbfour", etWenan4.getText().toString().trim());
                SPUtils.saveStringData(this, "mbfive", etWenan5.getText().toString().trim());
                finish();
                break;
        }
    }
}
