package com.android.jdhshop.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * 用户反馈
 * 想代理的城市以及推广方式的简要说明
 * 手机号
 *
 * @author wim
 */
public class FeedBackActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_question)
    EditText etQuestion;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.rg_sex)
    RadioGroup rgSex;
    @BindView(R.id.tv_ten)
    Button tvTen;
    private int id = 1;

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("意见反馈");
        id = getIntent().getIntExtra("type", 1);
        if (id == 2) {
            ((RadioButton) findViewById(R.id.cb_sex_woman)).setChecked(true);
            tvTitle.setText("城市合伙人申请");
            tvTen.setText("立即申请");
            etQuestion.setHint("想代理的城市以及推广方式的简要说明");
            etPhone.setHint("手机号");
        }
    }

    @Override
    protected void initListener() {
        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.cb_sex_man) {
                    id = 1;
                } else {
                    id = 2;
                }
            }
        });
    }

    @OnClick({R.id.tv_left, R.id.tv_ten})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.tv_ten:
                submitData();
                break;
        }
    }

    private void submitData() {
        if (TextUtils.isEmpty(etPhone.getText().toString()) || TextUtils.isEmpty(etQuestion.getText().toString())) {
            T.showShort(this, "请完善相关信息");
            return;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("cat_id", id);
        requestParams.put("linkman", etName.getText().toString().trim());
        requestParams.put("phone", etPhone.getText().toString().trim());
        requestParams.put("content", etQuestion.getText().toString().trim());
        HttpUtils.post(Constants.FEEDBACK, FeedBackActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    T.showShort(FeedBackActivity.this, jsonObject.getString("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
