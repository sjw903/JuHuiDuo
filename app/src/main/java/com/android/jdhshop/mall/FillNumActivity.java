package com.android.jdhshop.mall;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import com.android.jdhshop.common.LogUtils;
import com.google.gson.reflect.TypeToken;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.ShopActicleBean;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;

import org.angmarch.views.NiceSpinner;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class FillNumActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.info)
    WebView readContent;
    @BindView(R.id.nice_sp)
    NiceSpinner niceSp;
    @BindView(R.id.et_num)
    EditText etNum;
    private List<Kudi> kudis=new ArrayList<>();
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_fill_num);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("填写退货信息");
        getLogs();
        getInfo();
    }
    private void getLogs(){
        RequestParams shopParams = new RequestParams();
        HttpUtils.post(Constants.getLogistics, FillNumActivity.this,shopParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("sdfwef",responseString);
                try {
                    JSONObject object1=new JSONObject(responseString).getJSONObject("data").getJSONObject("list");
                    Iterator<String> iterator=object1.keys();
                    List<String> strings=new ArrayList<>();
                    while (iterator.hasNext()){
                        String key=iterator.next();
                        kudis.add(new Kudi(object1.getString(key),key));
                        strings.add(object1.getString(key));
                    }
                    niceSp.attachDataSource(strings);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void getInfo(){
        RequestParams shopParams = new RequestParams();
        shopParams.put("article_id", "49");
        HttpUtils.post(Constants.MESSAGE_ARTICLE_GETARTICLEMSG_URL,FillNumActivity.this, shopParams, new onOKJsonHttpResponseHandler<ShopActicleBean>(new TypeToken<Response<ShopActicleBean>>() {
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
                        readContent.loadData(article_msg.getContent()==null?"":article_msg.getContent().replaceAll("<img","<img style='max-width:100%;height:auto'"),"text/html","utf-8");
                    }
                } else {
                    showToast(datas.getMsg());
                }
            }
        });
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
                if(TextUtils.isEmpty(etNum.getText().toString())){
                    ToastUtils.showShortToast(this,"请填写快递单号");
                    return;
                }
                submit();
                break;
        }
    }
    private void submit(){
        RequestParams requestParams = new RequestParams();
        requestParams.put("logistics", kudis.get(niceSp.getSelectedIndex()).name);
        requestParams.put("express_number", etNum.getText().toString());
        requestParams.put("order_id",getIntent().getExtras().getString("id") );
        HttpUtils.post("1".equals(Constants.MALL_ORDER_TYPE)?Constants.EXPRESS_DANHAO_UPDATE:Constants.EXPRESS_DANHAO, FillNumActivity.this,requestParams, new TextHttpResponseHandler() {
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
                    JSONObject jsonObject = new JSONObject(responseString);
                    String msg = jsonObject.optString("msg");
                    if (jsonObject.getInt("code") == 0) {
                        finish();
                    }
                    T.showShort(FillNumActivity.this, msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    class Kudi{
        public String name;
        public String id;

        public Kudi(String name, String id) {
            this.name = name;
            this.id = id;
        }
    }
}
