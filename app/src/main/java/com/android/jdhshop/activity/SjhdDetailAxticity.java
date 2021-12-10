package com.android.jdhshop.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.SjhdBean;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.bumptech.glide.Glide;
import com.loopj.android.http.RequestParams;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
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

public class SjhdDetailAxticity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.it_text)
    TextView titltTxt;
    @BindView(R.id.it_text_1)
    TextView timeTxt;
    @BindView(R.id.hd_content)
    WebView hd_content;
    @BindView(R.id.hd_reward)
    TextView hd_reward;
    @BindView(R.id.hd_rules)
    TextView hd_rules;
    @BindView(R.id.iv_image)
    ImageView iv_image;
    @BindView(R.id.back)
    ImageView tvLefts;
    @BindView(R.id.iv_shoucang)
    ImageView iv_shoucang;
    @BindView(R.id.iv_fenxiang)
    ImageView iv_fenxiang;
    @BindView(R.id.btn1)
    RadioButton btn1;
    @BindView(R.id.btn2)
    RadioButton btn2;
    @BindView(R.id.btn3)
    RadioButton btn3;
    @BindView(R.id.tab1)
    LinearLayout tab1;
    @BindView(R.id.tab2)
    LinearLayout tab2;
    @BindView(R.id.tab3)
    LinearLayout tab3;
    @BindView(R.id.bt_button)
    Button bt_button;

    SjhdBean.SjhdListBean sjhdListBean;
    public boolean flag = false;
    Bitmap bitmap;


    @Override
    protected void initUI() {
        setContentView(R.layout.sjhd_yhb_hdxq);
        ButterKnife.bind(this);
    }


    @Override
    protected void initData() {
        tvTitle.setText("商家活动");
        tvLeft.setVisibility(View.VISIBLE);
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sjhdListBean = (SjhdBean.SjhdListBean) getIntent().getSerializableExtra("sjhdListBean");
        titltTxt.setText(sjhdListBean.getTitle());
        timeTxt.setText(sjhdListBean.getStart_time());
        Glide.with(SjhdDetailAxticity.this).load(Constants.APP_IP + sjhdListBean.getImg()).placeholder(R.mipmap.default_cover).dontAnimate().into(iv_image);

        hd_content.loadDataWithBaseURL(Constants.APP_IP, sjhdListBean.getContent() == null ? "" : sjhdListBean.getContent().replaceAll("<img", "<img style='width:100%'"), "text/html", "UTF-8", "");
//        hd_content.setText(Html.fromHtml(sjhdListBean.getContent()));//这是显示段落文本的,通过解析html
//        hd_content.setMovementMethod(ScrollingMovementMethod.getInstance());//段落文本的话要加这个
        hd_reward.setText(sjhdListBean.getReward());
        hd_rules.setText(sjhdListBean.getRules());

        isCollect();

    }

    @Override
    protected void initListener() {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab1.setVisibility(View.VISIBLE);
                tab2.setVisibility(View.GONE);
                tab3.setVisibility(View.GONE);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab1.setVisibility(View.GONE);
                tab2.setVisibility(View.VISIBLE);
                tab3.setVisibility(View.GONE);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab1.setVisibility(View.GONE);
                tab2.setVisibility(View.GONE);
                tab3.setVisibility(View.VISIBLE);
            }
        });
        bt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("activityId", sjhdListBean.getActivity_id());
                openActivity(AddSjhdAxticity.class, bundle);
            }
        });

        iv_shoucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag) { // 已收藏，取消收藏
                    cancelCollect();
                    iv_shoucang.setImageResource(R.mipmap.icon_collect_2);
                    flag = false;
                } else {//未收藏
                    doCollect();
                    flag = true;
                    iv_shoucang.setImageResource(R.mipmap.ico_collect_0);
                }
            }
        });


        iv_fenxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = Constants.APP_IP + "/wap/Merchant/share/merchant_id/" + sjhdListBean.getMerchant_id() + "/referrer_id/" + SPUtils.getStringData(SjhdDetailAxticity.this, "uid", "");
                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = webpage;
                msg.title = sjhdListBean.getTitle();
                msg.description = sjhdListBean.getContent();
                //设置缩略图
                if (bitmap != null) {
                    Bitmap bt = Bitmap.createScaledBitmap(bitmap, 80, 80, true);
                    msg.thumbData = bmpToByteArray(bt, true);
                }
//                Bitmap thumbBmp = BitmapFactory.decodeResource(getResources(), R.drawable.);
//                msg.thumbData = bmpToByteArray(thumbBmp, true);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("img");//  transaction字段用
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneSession;
                CaiNiaoApplication.api.sendReq(req);
            }
        });
    }


    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    //是否收藏
    private void isCollect() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", SPUtils.getStringData(SjhdDetailAxticity.this, "token", ""));
        requestParams.put("merchant_id", sjhdListBean.getMerchant_id());
        HttpUtils.post(Constants.cancelCollect, SjhdDetailAxticity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Log.e("msg", responseString);
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        String is_collect = object.getJSONObject("data").getString("is_collect");
                        if ("Y".equals(is_collect)) {
                            flag = true;
                            iv_shoucang.setImageResource(R.mipmap.icon_collect_2);
                        } else if ("N".equals(is_collect)) {
                            iv_shoucang.setImageResource(R.mipmap.ico_collect_0);
                            flag = false;
                        }
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 收藏店铺
     */
    private void doCollect() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", SPUtils.getStringData(SjhdDetailAxticity.this, "token", ""));
        requestParams.put("merchant_id", sjhdListBean.getMerchant_id());
        HttpUtils.post(Constants.collect, SjhdDetailAxticity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Log.e("msg", responseString);
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        showToast(object.getString("msg"));
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 取消收藏
     */
    private void cancelCollect() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", SPUtils.getStringData(SjhdDetailAxticity.this, "token", ""));
        requestParams.put("merchant_id", sjhdListBean.getMerchant_id());
        HttpUtils.post(Constants.cancelCollect, SjhdDetailAxticity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Log.e("msg", responseString);
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        showToast(object.getString("msg"));
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void ReceiverBroadCastMessage(String status, String result, Serializable serializable, Intent intent) {
        super.ReceiverBroadCastMessage(status, result, serializable, intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
