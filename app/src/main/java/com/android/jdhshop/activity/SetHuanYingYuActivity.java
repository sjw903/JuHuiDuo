package com.android.jdhshop.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.SPUtils;
import com.suke.widget.SwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetHuanYingYuActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_ai_qunname)
    EditText tv_ai_qunname;
    @BindView(R.id.tv_robot_status)
    SwitchButton robot_status_view;
    @BindView(R.id.et_word)
    EditText mEtWord;

    private JSONObject item_info_json ;
    String welcome_default_text = "欢迎您加入【京东–拼多多内购优惠群】 请您花1分钟了解一下【群规】： 1）群主每天为家人们发送京东/拼多多底价好物商品； 2）亲友们如果有指定商品类型可以@ 群主，群主会想办法为大家找优惠商品； 3）如果您觉得信息打扰正常工作，可以点击群右上角设置为【消息免打扰】，但是强烈建议设置为【置顶聊天】，让优惠信息更醒目； 4）同时也恳请各位亲友不要乱发广告和无用信息，维护好咱们自己的群，做相亲相爱的一家人。";
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_set_huan_ying_yu);
        ButterKnife.bind(this);
        tv_left.setVisibility(View.VISIBLE);
        tv_title.setText("进群欢迎语");
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        // Log.d(TAG, "initData: " + intent.getStringExtra("item_info"));
        String item_info = intent.getStringExtra("item_info");
        // Log.d(TAG, "initData: " + item_info);

        if ("".equals(item_info) || item_info == null){
            showTipDialog2("错误提示", Html.fromHtml("页面信息错误，请从正确渠道进入。"), new BaseActivity.onClickListener(){
                @Override
                public void onClickSure() {
                    finish();
                }
            },"我知道了");
            return;
        }
        item_info_json = JSONObject.parseObject(item_info);

        String talk_group_nick_name = item_info_json.getString("robot_name");
        tv_ai_qunname.setText(null == talk_group_nick_name || "".equals(talk_group_nick_name) ? "鑫年科技" : talk_group_nick_name );

        String welcome_text = item_info_json.getString("welcome");
        mEtWord.setText( welcome_text == null || "".equals(welcome_text) ? welcome_default_text : welcome_text);
        robot_status_view.setChecked(item_info_json.getBooleanValue("welcome_status"));

    }

    @Override
    protected void initListener() {
        TextView mTvWordCount = (TextView) findViewById(R.id.tv_word_count);

        //实时记录字数
        mEtWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTvWordCount.setText(String.valueOf(s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.tv_left, R.id.set_huanying_geng})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.set_huanying_geng:

                int need_update = 0;

                if (mEtWord.getText().toString().equals(item_info_json.getString("welcome")) && mEtWord.getText().toString().equals(welcome_default_text)){
                    need_update = 1;
                }

                if (robot_status_view.isChecked() != item_info_json.getBooleanValue("welcome_status")){
                    need_update = 1;
                }

                if (!tv_ai_qunname.getText().toString().equals("鑫年科技") && !tv_ai_qunname.getText().toString().equals("") && !tv_ai_qunname.getText().toString().equals(item_info_json.getString("robot_name"))){
                    need_update = 1;
                }

                if (need_update == 1){
                    Intent intent = new Intent();
                    intent.putExtra("need_update", need_update);
                    intent.putExtra("robot_name", tv_ai_qunname.getText().toString());
                    intent.putExtra("welcome", mEtWord.getText().toString());
                    intent.putExtra("welcome_status", robot_status_view.isChecked()?"1":"0");
                    setResult(2, intent);
                }

                finish();
                //showToast("更新成功");
                break;

        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            tv_left.callOnClick();
        }
        return false;

    }
}
