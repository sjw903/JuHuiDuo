package com.android.jdhshop.my;

import android.content.Intent;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.dialog.CustomDialog;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.utils.CheckUtil;
import com.android.jdhshop.widget.AutoClearEditText;
import com.loopj.android.http.RequestParams;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.ViewConvertListener;
import com.shehuan.nicedialog.ViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 手机号修改
 */

public class UserPhoneEditActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;

    @BindView(R.id.et_two)
    AutoClearEditText et_two;

    private String phone = "";


    @Override
    protected void initUI() {
        setContentView(R.layout.ac_edit_username);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        phone = getIntent().getStringExtra("phone");
        et_two.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(11) });
        et_two.setInputType(InputType.TYPE_CLASS_PHONE);
        if (!TextUtils.isEmpty(phone)) {
            et_two.setText(phone);
            et_two.setSelection(et_two.length());
        }
        tv_left.setVisibility(View.VISIBLE);
        tv_left.setOnClickListener(v -> {
            if (quit()) {
                finish();
            }
        });
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(R.string.homepage_phone);

        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(R.string.homepage_submit);

    }


    @Override
    protected void initListener() {
        tv_right.setOnClickListener(v -> submit());
    }

    private void submit() {
        String content = et_two.getText().toString();
        if (!TextUtils.isEmpty(content)) {
            if (!content.equals(phone)) {
                if (CheckUtil.isMobile(content)) {
                    upload(content);
                } else {
                    showToast(getString(R.string.homepage_phone_tips));
                }
            } else {
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (quit()) {
            super.onBackPressed();
        }
    }


    private boolean quit() {
        String content = et_two.getText().toString();
        if (!TextUtils.isEmpty(content)) {
            if (!content.equals(phone)) {
                CustomDialog customDialog = CustomDialog.init();
                customDialog
                        .setLayoutId(R.layout.confirm_layout)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                                TextView title = holder.getView(R.id.title);
                                title.setVisibility(View.GONE);
                                TextView msg = holder.getView(R.id.message);
                                msg.setText(R.string.homepage_user_name_back_direct_msg);
                                TextView cancel = holder.getView(R.id.cancel);
                                cancel.setText(getString(R.string.homepage_user_name_back_direct));
                                TextView ok = holder.getView(R.id.ok);
                                ok.setText(getString(R.string.homepage_submit));
                                cancel.setOnClickListener(v1 -> {
                                    dialog.dismissAllowingStateLoss();
                                    finish();
                                });
                                ok.setOnClickListener(v1 -> {
                                    dialog.dismissAllowingStateLoss();
                                    if (CheckUtil.isMobile(content)) {
                                        upload(content);
                                    } else {
                                        showToast(getString(R.string.homepage_phone_tips));
                                    }
                                });
                            }
                        })
                        .setMargin(20)
                        .setGravity(Gravity.CENTER)
                        .show(getSupportFragmentManager());

                return false;
            }
        }
        return true;
    }

    /**
     * 添加个人主页信息数据
     *
     * @param content 用户名
     */
    private void upload(String content) {
        RequestParams requestParams = new RequestParams("phone", content);
        showLoadingDialog();
        HttpUtils.post(Constants.GET_ADD_USER_HOME, UserPhoneEditActivity.this, requestParams, new HttpUtils.TextHttpResponseHandler() {
            @Override
            public void onFinish() {
                closeLoadingDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("UserPhoneEditActivity upload onSuccess responseString = " + responseString);
                com.alibaba.fastjson.JSONObject response = com.alibaba.fastjson.JSONObject.parseObject(responseString);
                if (response.getIntValue("code") == 0) {
                    String integrity_rate = response.getString("integrity_rate");
                    Intent intent = new Intent();
                    intent.putExtra("integrity_rate", integrity_rate);
                    intent.putExtra("phone", content);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    String msg = response.getString("msg");
                    if (!TextUtils.isEmpty(msg)) {
                        showToast(msg);
                    }
                }
            }
        });
    }

}
