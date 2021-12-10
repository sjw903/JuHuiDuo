package com.android.jdhshop.my;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.widget.CircleImageView;
import com.android.jdhshop.ziyuan.ResourceManager;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.BindActivity;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.FeeBean;
import com.android.jdhshop.bean.KeyValueBean;
import com.android.jdhshop.bean.PddClient;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.ShopActicleBean;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * 提现
 * Created by yohn on 2018/7/14.
 */

public class PutForwardActivity extends BaseActivity {
    @BindView(R.id.bg_head)
    LinearLayout bg_head;
    @BindView(R.id.txt_rule)
    TextView txtRule;
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.gac)
    CircleImageView gac;
    @BindView(R.id.jiantouac)
    ImageView jiantouac;
    @BindView(R.id.zfbac)
    CircleImageView zfbac;
    @BindView(R.id.edt_money)
    TextView edt_money;
    @BindView(R.id.tv_commit)
    TextView tv_commit;//确认
    @BindView(R.id.et_oldpsd)
    TextInputEditText etOldpsd;
    @BindView(R.id.et_newpsd_sure)
    TextInputEditText etNewpsdSure;
    @BindView(R.id.et_newpsd_sure1)
    TextInputEditText etNewpsdSure1;
    String token, balance;
    private ACache mAcache;
    @Override
    protected void onResume() {
        super.onResume();
        //重新获取数据的逻辑，此处根据自己的要求回去
        //显示信息的界面
        shuaxin();
    }

    @Override
    protected void initUI() {
        setContentView(R.layout.ac_put_forward);
        ButterKnife.bind(this);
        getKeTiXian();
        getRule();
        BaseLogDZiYuan.LogDingZiYuan(gac, "app_icon.png");
        BaseLogDZiYuan.LogDingZiYuan(jiantouac, "temp.png");
        BaseLogDZiYuan.LogDingZiYuan(zfbac, "zfb.png");

    }
    private void getRule(){
        RequestParams shopParams = new RequestParams();
        shopParams.put("article_id", 28);
        HttpUtils.post(Constants.MESSAGE_ARTICLE_GETARTICLEMSG_URL, PutForwardActivity.this,shopParams, new onOKJsonHttpResponseHandler<ShopActicleBean>(new TypeToken<Response<ShopActicleBean>>() {
        }) {

            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Response<ShopActicleBean> datas) {
                if (datas.isSuccess()) {
                    ShopActicleBean.ArticleAsg article_msg = datas.getData().getArticle_msg();
                    if (article_msg != null) {
                        txtRule.setText(Html.fromHtml(article_msg.getContent()));
                    }
                } else {
                    showToast(datas.getMsg());
                }
            }
        });
    }
    private void getKeTiXian(){
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.GET_KETIXIAN, PutForwardActivity.this,requestParams, new TextHttpResponseHandler(){
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
                    JSONObject object=new JSONObject(responseString);
                    if(object.getInt("code")==0){
                        edt_money.setText("可提现金额:" +object.getJSONObject("data").getString("amount"));
                    }else {
                        edt_money.setText("可提现金额:" +"0.00");
                        if ("用户不存在".equals(object.getString("msg"))){
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    edt_money.setText("可提现金额:" +"0.00");
                }
            }
        });
    }

    @Override
    protected void initData() {
        mAcache = ACache.get(this);
        token = mAcache.getAsString(Constants.TOKEN);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey("balance")) {
                balance = b.getString("balance");
            }
        }
//        if (!TextUtils.isEmpty(balance)) {
//            edt_money.setText("可提现金额:" + df.format((Double.valueOf(balance)-Double.valueOf(SPUtils.getStringData(this,"my_money_three","0.00"))<0?0:(Double.valueOf(balance)-Double.valueOf(SPUtils.getStringData(this,"my_money_three","0.00"))))));
//        } else {
//            edt_money.setText("可提现金额:" +"0.00");
//        }
        tv_left.setVisibility(View.VISIBLE);
        tv_title.setText("余额提现");
        tv_left.setVisibility(View.VISIBLE);
        tv_left.setVisibility(View.VISIBLE);
        String zfb_name = SPUtils.getStringData(this, "zfb_name", "");
        String zfb_id = SPUtils.getStringData(this, "zfb_id", "");
//        if(zfb_name.equals("")){
//
//        }else{
//            etOldpsd.setText(zfb_id);
//            etNewpsdSure.setText(zfb_name);
//        }


    }
    private void shuaxin(){
        etOldpsd.setText(CaiNiaoApplication.getUserInfoBean().user_msg.alipay_account);
        etNewpsdSure.setText(CaiNiaoApplication.getUserInfoBean().user_detail.truename);
    }

    @Override
    protected void initListener() {
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawRequest();
            }
        });
    }

    private void drawRequest() {
        if (TextUtils.isEmpty(StringUtils.doViewToString(etNewpsdSure1))) {
            showToast("请输入提现金额");
            return;
        }
        if(Double.valueOf(edt_money.getText().toString().split(":")[1])<=Double.valueOf(etNewpsdSure1.getText().toString().trim())){
            showToast("不能大于可提现金额");
            return;
        }
        String time=String.valueOf(System.currentTimeMillis() / 1000);
        RequestParams requestParams = new RequestParams();
        requestParams.put("data_type",PddClient.data_type);
        requestParams.put("version",PddClient.version);
        requestParams.put("timestamp",time);
        requestParams.put("token",SPUtils.getStringData(PutForwardActivity.this,"token",""));
        requestParams.put("type","1");
        requestParams.put("account_type","1");
        requestParams.put("account", etOldpsd.getText().toString().trim());
        requestParams.put("truename", etNewpsdSure.getText().toString().trim());
        requestParams.put("money",etNewpsdSure1.getText().toString().trim());
        Map<String,String> temp=new HashMap<>();
        temp.put("data_type",PddClient.data_type);
        temp.put("version",PddClient.version);
        temp.put("timestamp",time);
        temp.put("token",SPUtils.getStringData(PutForwardActivity.this,"token",""));
        temp.put("type","1");
        temp.put("account_type","1");
        temp.put("account", etOldpsd.getText().toString().trim());
        temp.put("truename", etNewpsdSure.getText().toString().trim());
        temp.put("money",etNewpsdSure1.getText().toString().trim());
        String sign=PddClient.getSign1(temp);
        requestParams.put("sign",sign);
        HttpUtils.post(Constants.DRAW,PutForwardActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogUtils.d("fsdfds",responseString);
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
                LogUtils.d("fsdfds",responseString);
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    if (code == 0) {
                        showToast(msg);
                        finish();
                    } else {
                        showToast(msg);
                        if ("用户不存在".equals(msg)){
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @OnClick(R.id.get_old_sms)
    public void onViewClicked() {
        Intent  intent=new Intent(PutForwardActivity.this,BindActivity.class);
        startActivityForResult(intent,RESULT_OK);
        //openActivity(BindActivity.class);
    }
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                if(resultCode==RESULT_OK){
                    String returnData = data.getStringExtra("data_return");
                    etNewpsdSure.setText(returnData);
                    Log.d("FirstActivity",returnData);
                }
                break;

        }
    }

}
