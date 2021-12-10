package com.android.jdhshop.mall;

import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.mallbean.OrderDetailBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class PayResultActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.txt_money)
    TextView txtMoney;
    @BindView(R.id.txt_order_num)
    TextView txtOrderNum;

    @Override
    protected void initUI() {
        setContentView(R.layout.actiivty_pay_result);
        ButterKnife.bind(this);
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("支付成功");
        txtMoney.setText("¥"+getIntent().getExtras().getString("money"));
        txtOrderNum.setText("订单号: "+getIntent().getExtras().getString("order_num"));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }
    @OnClick({R.id.tv_left, R.id.btn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.btn_ok:
//                getDetail();
                finish();
                break;
        }
    }
    private void getDetail() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("order_id", getIntent().getStringExtra("order_id"));
        HttpUtils.post(Constants.GET_ORDER_DETAIL, PayResultActivity.this,requestParams, new onOKJsonHttpResponseHandler<OrderDetailBean>(new TypeToken<Response<OrderDetailBean>>() {
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
            public void onSuccess(int statusCode, final Response<OrderDetailBean> datas) {
                if (datas.isSuccess()) {
//                    Bundle bundle=new Bundle();
//                    bundle.putSerializable("order",datas.getData().orderMsg);
//                    openActivity(ShopMallOrderDetailActivity.class,bundle);
                    finish();
                } else {
                    showToast(datas.getMsg());
                }
            }
        });
    }
}
