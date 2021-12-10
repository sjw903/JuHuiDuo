package com.android.jdhshop.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.common.LogUtils;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.ShopActicleBean;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.login.WelActivity;
import com.android.jdhshop.utils.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class ElemeActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.txt_kouling)
    TextView txtKouling;
    @BindView(R.id.txt_desc)
    TextView txtDesc;
    String url = "";
    @BindView(R.id.e_bbg)
    ImageView e_bbg;
    @BindView(R.id.img_top)
    ImageView img_top;
    @BindView(R.id.img_btn_a)
    ImageView img_btn_a;
    @BindView(R.id.img_btn_b)
    ImageView img_btn_b;
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_eleme);
        ButterKnife.bind(this);


        BaseLogDZiYuan.LogDingZiYuan(e_bbg, "e_bg.png");
        BaseLogDZiYuan.LogDingZiYuan(img_top, "e_card.png");
        BaseLogDZiYuan.LogDingZiYuan(img_btn_a, "e_btn_a.png");
        BaseLogDZiYuan.LogDingZiYuan(img_btn_b, "e_btn_b.png");
    }

    @Override
    protected void initData() {
        if ("".equals(SPUtils.getStringData(this, "token", ""))) {
            openActivity(WelActivity.class);
            finish();
            return;
        }
        RequestParams requestParams = new RequestParams();
        HttpUtils.postUpload(Constants.ISBIND_RELATION_ID,ElemeActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    String isBinding = jsonData.optString("is_binding");
                    if ("Y".equals(isBinding)) {
                    } else {
                        openActivity(SetActivity.class);
                        ToastUtils.showLongToast(ElemeActivity.this,"请先绑定淘宝渠道");
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        tvTitle.setText("饿了么");
        tvLeft.setVisibility(View.VISIBLE);
        RequestParams shopParams = new RequestParams();
        shopParams.put("article_id", "32");
        HttpUtils.post(Constants.MESSAGE_ARTICLE_GETARTICLEMSG_URL,ElemeActivity.this, shopParams, new onOKJsonHttpResponseHandler<ShopActicleBean>(new TypeToken<Response<ShopActicleBean>>() {
        }) {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Response<ShopActicleBean> datas) {
                if (datas.isSuccess()) {
                    ShopActicleBean.ArticleAsg article_msg = datas.getData().getArticle_msg();
                    if (article_msg != null) {
                        txtDesc.setText(Html.fromHtml(article_msg.getContent()));
                    }
                } else {
                    showToast(datas.getMsg());
                }
            }
        });
        RequestParams shopParams1 = new RequestParams();
        shopParams1.put("token", SPUtils.getStringData(this, "token", ""));
        shopParams1.put("promotion_scene_id", "1571715733668");
        HttpUtils.post(Constants.APP_IP + "/api/Tbk/getActivityLink",ElemeActivity.this, shopParams1, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d(TAG, "onSuccess: "+responseString);
                try {
                    JSONObject object = new JSONObject(responseString);
                    if (object.getInt("code") == 0) {
                        url = object.getJSONObject("data").getString("url");
                        getTkl();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * @属性:获取淘口令
     * @开发者:陈飞
     * @时间:2018/7/28 22:04
     */
    private void getTkl() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("text", "来"+ getString(R.string.app_name) +"领取大额饿了么红包");
        requestParams.put("url", url);
        requestParams.put("logo", Constants.APP_IP + "/static/admin/img/logo.png");
        HttpUtils.post(Constants.CREATE_TP_WD,ElemeActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    if (code == 0) {
                        String data = jsonObject.optString("data");
                        txtKouling.setText("注意◆◆饿了么狂撒①亿拷鋇這段("+data+")打开[手机TAO寶]立享[饿了么隐藏]优惠");
                    } else {
                       showToast(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    protected void initListener() {

    }

    @OnClick({R.id.tv_left, R.id.img_btn_a, R.id.img_btn_b})
    public void onViewClicked(View view) {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", txtKouling.getText().toString());
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.img_btn_a:
                if ("".equals(url)) {
                    return;
                }
                cm.setPrimaryClip(mClipData);
                PackageManager packageManager = getPackageManager();
                Intent intent=new Intent();
                intent =packageManager.getLaunchIntentForPackage("com.taobao.taobao");
                if(intent==null){
                    ToastUtils.showLongToast(this,"请先安装淘宝客户端");
                }else{
                    startActivity(intent);
                }
//                Map<String, String> exParams = new HashMap<>();
//                AlibcTrade.openByUrl(this, "", url, null, new WebViewClient(), new WebChromeClient(), new AlibcShowParams(OpenType.Native)
//                        , new AlibcTaokeParams("mm_21742772_104250451_20294800352", "", ""), exParams, new AlibcTradeCallback() {
//                            @Override
//                            public void onTradeSuccess(AlibcTradeResult alibcTradeResult) {
//
//                            }
//
//                            @Override
//                            public void onFailure(int i, String s) {
//
//                            }
//                        });
//                Intent intent = new Intent(this, WebViewActivity.class);
//                intent.putExtra("title", "饿了么");
//                intent.putExtra("url", url);
//                startActivity(intent);
                break;
            case R.id.img_btn_b:
                cm.setPrimaryClip(mClipData);
                ToastUtils.showLongToast(this, "复制成功");
                break;
        }
    }
}
