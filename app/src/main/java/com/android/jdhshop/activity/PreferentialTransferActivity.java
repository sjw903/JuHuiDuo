package com.android.jdhshop.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lljjcoder.style.citylist.widget.CleanableEditView;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.PromotionDetailsBean;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.login.LoginActivity;
import com.android.jdhshop.login.WelActivity;
import com.android.jdhshop.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * @属性:优惠转
 * @开发者:陈飞
 * @时间:2018/7/28 09:33
 */
public class PreferentialTransferActivity extends BaseActivity {


    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.edti_et)
    CleanableEditView edtiEt;
    @BindView(R.id.copy_btn)
    TextView copyBtn;
    @BindView(R.id.view_now_btn)
    Button viewNowBtn;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_right_icon)
    FrameLayout tvRightIcon;

    @Override
    protected void initUI() {
        //dfdsfd
        setContentView(R.layout.activity_preferential_transfer);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("立即查券");
        //设置右边按钮
        tvRight.setVisibility(View.GONE);
        tvRightIcon.setVisibility(View.GONE);
    }

    @Override
    protected void initListener() {

    }


    @OnClick({R.id.tv_left, R.id.copy_btn, R.id.view_now_btn, R.id.tv_right_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.copy_btn: //一键粘贴
                //读取剪贴板内容
                String clipboard = getClipboard();
                edtiEt.setText(clipboard);
                break;
            case R.id.view_now_btn://立即查看商品详情
                String token = SPUtils.getStringData(getComeActivity(), Constants.TOKEN, "");
                if (TextUtils.isEmpty(token)) {
                    openActivity(WelActivity.class);
                } else {
                    getGoodsMsgRequest();
                }
                break;
            case R.id.tv_right_icon: //企鹅按钮

                break;
        }
    }


    @OnClick()
    public void onViewClicked() {
    }
    /**
     * @属性:获取淘宝客商品详情
     * @开发者:陈飞
     * @时间:2018/7/27 14:26
     */
    private void getGoodsMsgRequest() {
        if (TextUtils.isEmpty(StringUtils.doViewToString(edtiEt))) {
            showToast("请输入淘宝商品链接或者淘口令，淘宝商品名称无法精确查找");
            return;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("tkl", StringUtils.doViewToString(edtiEt));
        HttpUtils.post(Constants.HOME_TBK_SEARCHTKL_URL, PreferentialTransferActivity.this,requestParams, new onOKJsonHttpResponseHandler<PromotionDetailsBean>(new TypeToken<Response<PromotionDetailsBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

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
            public void onSuccess(int statusCode, Response<PromotionDetailsBean> datas) {
                if (datas.isSuccess()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("tkl", StringUtils.doViewToString(edtiEt));
                    bundle.putString("commission",datas.getData().getCommission());
                    openActivity(PromotionDetailsActivity.class, bundle);
                } else {
                    showToast(datas.getMsg());
                }

            }
        });
    }
}
