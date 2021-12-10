package com.android.jdhshop.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.merchantactivity.MerRechargeActivity2;
import com.android.jdhshop.merchantbean.MerchantNewBean;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : szp
 *     time   : 2020/05/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class SjxxsDjjActivity extends BaseActivity {
    @BindView(R.id.img_merchant)
    ImageView imgMerchant;
    @BindView(R.id.txt_priceice)
    TextView txtPriceice;
    @BindView(R.id.txt_limit)
    TextView txtLimit;
    @BindView(R.id.txt_num)
    TextView txtNum;
    @BindView(R.id.txt_day)
    TextView txtDay;
    @BindView(R.id.iv_top_more)
    ImageView ivTopMore;
    @BindView(R.id.txt_merchant_name)
    TextView txtMerchantName;
    @BindView(R.id.txt_desc)
    TextView txtDesc;
    MerchantNewBean.Item item;
    @BindView(R.id.btn_tx)
    Button btnTx;
    int count=1;
    @Override
    protected void initUI() {
        setContentView(R.layout.sjxxs_gmdjj);
        ButterKnife.bind(this);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        item = (MerchantNewBean.Item) getIntent().getExtras().get("bean");
        txtMerchantName.setText(getIntent().getExtras().getString("name"));
        txtPriceice.setText(item.amount + "元代金券");
        btnTx.setText(item.price+"元立即购买");
        txtLimit.setText("每人限购"+item.quota+"张");
        txtDay.setText("自购买日期起"+item.validity_days+"天内有效");
        txtDesc.setText(item.validity_zh);
    }

    @Override
    protected void initListener() {

    }

    @OnClick({R.id.icon_reduce, R.id.icon_add, R.id.btn_tx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.icon_reduce:
                if(count==1){
                    ToastUtils.showLongToast(this,"最低购买一张");
                    return;
                }
                count--;
                txtNum.setText(count+"");
                btnTx.setText(Double.valueOf(item.price)*Integer.valueOf(txtNum.getText().toString())+"元立即购买");
                break;
            case R.id.icon_add:
                if(Integer.valueOf(item.quota)<(count+1)){
                    ToastUtils.showLongToast(this,"购买数量超限");
                    return;
                }
                count++;
                txtNum.setText(count+"");
                btnTx.setText(Double.valueOf(item.price)*Integer.valueOf(txtNum.getText().toString())+"元立即购买");
                break;
            case R.id.btn_tx:
                buy();
                break;
        }
    }
    private void buy() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("voucher_id",item.id);
        requestParams.put("num", txtNum.getText().toString());
        HttpUtils.post(Constants.APP_IP + "/api/O2oVoucherOrder/order",SjxxsDjjActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
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
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        Intent intent = new Intent(SjxxsDjjActivity.this, MerRechargeActivity2.class);
                        intent.putExtra("ordernum", object.getJSONObject("data").getString("order_num"));
                        intent.putExtra("allprice", String.format("%.2f",Double.valueOf(item.price)*Integer.valueOf(txtNum.getText().toString())));
                        intent.putExtra("title", "商家代金券购买");
                        intent.putExtra("orderid", object.getJSONObject("data").getString("order_id"));
                        startActivity(intent);
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
