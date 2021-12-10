package com.android.jdhshop.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.jdhshop.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class DialogActivityZhuXiao extends Activity {
    private LinearLayout agree_box;

    @BindView(R.id.gouxuanrd)
    CheckBox gouxuanrd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_dialog_zhuxiao);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ButterKnife.bind(this);
        setFinishOnTouchOutside(false);
        gouxuanrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: ");
            }
        });
        agree_box = findViewById(R.id.www);
        findViewById(R.id.txt_agreement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsActivity.actionStart(DialogActivityZhuXiao.this, "73", getString(R.string.zhuxiao_agree_text));
            }
        });
    }

    @OnClick({R.id.txt_cancle, R.id.txt_ok,})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.txt_cancle:
                setResult(RESULT_CANCELED, intent);
                finish();
                break;
            case R.id.txt_ok:
                if (gouxuanrd.isChecked()) {
                    Log.d("TAG", "onViewClicked: 确认注销");
                    setResult(1,intent);
                    finish();
                } else {
                    agree_box.startAnimation(AnimationUtils.loadAnimation(this, R.anim.transla_checkbox));
                    Toast.makeText(DialogActivityZhuXiao.this, "请勾选"+getString(R.string.zhuxiao_agree_text), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
