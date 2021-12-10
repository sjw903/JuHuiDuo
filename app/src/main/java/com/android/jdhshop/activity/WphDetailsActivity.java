package com.android.jdhshop.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.kepler.jd.Listener.OpenAppAction;
import com.kepler.jd.login.KeplerApiManager;
import com.kepler.jd.sdk.bean.KeplerAttachParameter;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.IsCollectBean;
import com.android.jdhshop.bean.MyGoodsResp;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.Wphbean;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.login.WelActivity;
import com.android.jdhshop.my.RechargeActivity;
import com.android.jdhshop.utils.BannerImageLoader;
import com.android.jdhshop.utils.BitmapUtils;
import com.android.jdhshop.utils.ImgUtils;
import com.android.jdhshop.utils.MyScrollView;
import com.android.jdhshop.utils.OkHttpUtils;
import com.android.jdhshop.utils.VerticalImageSpan;
import com.android.jdhshop.utils.WxUtil;
import com.android.jdhshop.utils.ZxingUtils;
import com.android.jdhshop.wmm.QQShareUtil;
import com.youth.banner.Banner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import jd.union.open.promotion.bysubunionid.get.request.PromotionCodeReq;
import jd.union.open.promotion.bysubunionid.get.request.UnionOpenPromotionBysubunionidGetRequest;
import jd.union.open.promotion.bysubunionid.get.response.UnionOpenPromotionBysubunionidGetResponse;
import okhttp3.Call;
import okhttp3.Callback;

/**
 * @属性:京东推广详情
 * @开发者:WMM
 * @时间:2018/12/07 13:45
 */
public class WphDetailsActivity extends BaseActivity implements MyScrollView.OnScrollListener, IUiListener {

    @BindView(R.id.bg_head2)
    LinearLayout headView;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.homeBanner)
    Banner homeBanner;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.store_name_tv)
    TextView storeNameTv;
    @BindView(R.id.after_coupon_tv)
    TextView afterCouponTv;
    @BindView(R.id.price_tv)
    TextView priceTv;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.scroll_view)
    MyScrollView scrollView;
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.txt_left)
    TextView txt_left;
    @BindView(R.id.view_zz)
    View zz;
    @BindView(R.id.ll_share)
    LinearLayout llShare;
    @BindView(R.id.ll_vip)
    LinearLayout ll_vip;
    @BindView(R.id.tv_num)
    TextView tv_num;
    @BindView(R.id.share_fl)
    FrameLayout share_fl;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.title_share_tv)
    TextView title_share_tv;
    @BindView(R.id.tv_for_share)
    TextView tv_for_share;
    @BindView(R.id.after_coupon_share_tv)
    TextView after_coupon_share_tv;
    @BindView(R.id.price_share_tv)
    TextView price_share_tv;
    @BindView(R.id.erweima_tv)
    ImageView erweima_tv;
    @BindView(R.id.v_line)
    View vLine;
    @BindView(R.id.detail)
    TextView detail;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.copy_taobao_btn)
    TextView copyTaobaoBtn;
    @BindView(R.id.copy_friends_cicle_btn)
    TextView copyFriendsCicleBtn;
    @BindView(R.id.copy_friends_btn)
    TextView copyFriendsBtn;
    @BindView(R.id.txt_finish)
    TextView txtFinish;
    @BindView(R.id.hh)
    TextView hh;
    @BindView(R.id.ll_right)
    TextView llRight;
    @BindView(R.id.store_sold_num)
    TextView storeSoldNum;
    private boolean isCollect = false;//未收藏
    private ACache mAcache;
    String token;

    String group_id;
    private Wphbean jdBean;
    DecimalFormat df = new DecimalFormat("0.00");
    private String pddLink = "";
    private GradientDrawable gradientDrawable;
    @BindView(R.id.web_detail)
    WebView webDetail;
    String url = "";

    @Override
    protected void initUI() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_jd_details);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        if ("".equals(SPUtils.getStringData(this, "token", ""))) {
            T.showShort(this, "请先登录");
            openActivity(WelActivity.class);
            finish();
            return;
        }
        webDetail.getSettings().setJavaScriptEnabled(true);
        webDetail.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                webDetail.loadUrl("javascript:window.java_obj.getSource('<head>'+" +
                        "document.getElementById('commDesc').innerHTML+'</head>');");
                super.onPageFinished(view, url);
            }
        });
        webDetail.setWebChromeClient(new WebChromeClient());
        jdBean = (Wphbean) getIntent().getBundleExtra("goods").get("goods");
        getDetail();
//      webDetail.loadUrl("https://item.m.jd.com/product/"+jdBean.getSkuId()+".html");
        mAcache = ACache.get(this);
        token = mAcache.getAsString(Constants.TOKEN);
        group_id = mAcache.getAsString("group_id");
        //动态设置drawableLeft属性
        Drawable drawable1 = getResources().getDrawable(R.mipmap.icon_back_while);
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
        tvLeft.setCompoundDrawables(drawable1, null, null, null);
        tvLeft.setVisibility(View.VISIBLE);
        gradientDrawable = (GradientDrawable) tvLeft.getBackground();
        if ((null != group_id && "3".equals(group_id)) || (null != group_id && "4".equals(group_id))) {//会员不展示  其他展示
            ll_vip.setVisibility(View.GONE);
            ll_vip.setEnabled(false);
        } else {
            ll_vip.setVisibility(View.VISIBLE);
            ll_vip.setEnabled(true);
        }
        findViewById(R.id.ll_vip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(RechargeActivity.class);
            }
        });
        tvTitle.setText("推广详情");
        homeBanner.setLayoutParams(new RelativeLayout.LayoutParams(BitmapUtils.getScreenWith(this), BitmapUtils.getScreenWith(this)));
        homeBanner.setImageLoader(new BannerImageLoader());
        homeBanner.setDelayTime(3000);
        List<String> images = new ArrayList<>();
        images.add(jdBean.goodsMainPicture);
        homeBanner.update(images);
        homeBanner.start();
        titleTv.setText(jdBean.goodsName);
        storeNameTv.setText(jdBean.storeInfo.storeName);
        price_share_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        priceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        priceTv.setText("原价¥" + jdBean.marketPrice);
        price_share_tv.setText("原价¥" + jdBean.marketPrice);
//        try {
            afterCouponTv.setText("¥" + df.format(Double.valueOf(jdBean.vipPrice)));
            after_coupon_share_tv.setText("¥" + df.format(Double.valueOf(jdBean.vipPrice)));
//        } catch (Exception e) {
//            afterCouponTv.setText("¥" + df.format(jdBean.vipPrice));
//            after_coupon_share_tv.setText("¥" + df.format(jdBean.vipPrice));
//        }
            tvShare.setText("奖:" + df.format(Double.parseDouble( jdBean.commission )* Double.parseDouble(df.format((float) SPUtils.getIntData(this, "rate", 0) / 100))));
//        try {
            tv_for_share.setVisibility(View.INVISIBLE);
            txt_left.setText("购买");
            txt_left.setCompoundDrawables(null,null,null,null);
//        } catch (Exception e) {
//            tv_for_share.setText(0 + "");
//            txt_left.setText(0 + "元");
//        }
//        try {
//            tv_for_share.setText(jdBean.getCouponInfo()[0].getCouponList()[0].getDiscount() + "");
//            txt_left.setText("¥:" + jdBean.getCouponInfo()[0].getCouponList()[0].getDiscount() + "元");
//        } catch (Exception e) {
//            tv_for_share.setText(0 + "");
//            txt_left.setText("¥:" + 0 + "元");
//        }
//        storeSoldNum.setText("销量:" + jdBean.getInOrderCount30Days());
        storeSoldNum.setVisibility(View.INVISIBLE);
        title_share_tv.setText(jdBean.goodsName);
        Glide.with(WphDetailsActivity.this).load(jdBean.goodsMainPicture).dontAnimate().into(iv);
        //关闭上啦加载
        refreshLayout.setEnableLoadMore(false);
        tv_num.setText("升级会员等级，最高可得奖¥" + df.format(Double.valueOf(jdBean.commission)  * 0.9) + "。");
        //开始刷新
        refreshLayout.autoRefresh();
    }

    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void initListener() {
        findViewById(R.id.ll_right).setVisibility(View.GONE);
//        ll_vip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openActivity(RechargeActivity.class);
//            }
//        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (TextUtils.isEmpty(SPUtils.getStringData(getComeActivity(), Constants.TOKEN, ""))) {
                    openActivity(WelActivity.class);
                    refreshLayout.finishRefresh();
                    return;
                }
                getGoodsMsgRequest2();
                getDetail();
            }
        });
        headView.getBackground().mutate().setAlpha(0);
        tvTitle.setTextColor(Color.argb(0, 255, 255, 255));
        scrollView.setScrolListener(this);
        share_fl.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    @Override
    public void onScroll(int scrollY) {
        if (scrollY < 100) {
            headView.getBackground().mutate().setAlpha(0);
            tvTitle.setTextColor(Color.argb(0, 255, 255, 255));
            gradientDrawable.setColor(Color.parseColor("#88000000"));
        } else if (scrollY >= 100 && scrollY <= 355) {
            if ((scrollY - 100) <= 88) {
                gradientDrawable.setColor(Color.argb((88 - scrollY + 100), 0, 0, 0));
            }
            headView.getBackground().mutate().setAlpha((scrollY - 100));
            tvTitle.setTextColor(Color.argb((scrollY - 100), 255, 255, 255));
        } else {
            tvTitle.setTextColor(Color.argb(255, 255, 255, 255));
            gradientDrawable.setColor(Color.parseColor("#00000000"));
        }
    }

    @Override
    protected void ReceiverIsLoginMessage() {
        super.ReceiverIsLoginMessage();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (refreshLayout != null) {
                    refreshLayout.autoRefresh();
                }
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.txt_finish, R.id.tv_left, R.id.ll_right, R.id.copy_friends_qq, R.id.copy_friends_cicle_zone, R.id.txt_left, R.id.copy_taobao_btn, R.id.copy_friends_cicle_btn, R.id.copy_friends_btn, R.id.tv_finish, R.id.view_zz})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left: //左边的
                finish();
                break;
            case R.id.view_zz:
                zz.setVisibility(View.GONE);
                llShare.setVisibility(View.GONE);
                share_fl.setVisibility(View.GONE);
                break;
            case R.id.txt_finish:
                zz.setVisibility(View.GONE);
                llShare.setVisibility(View.GONE);
                share_fl.setVisibility(View.GONE);
                break;
            case R.id.tv_finish:
                zz.setVisibility(View.VISIBLE);
                llShare.setVisibility(View.VISIBLE);
                share_fl.setVisibility(View.VISIBLE);
                break;
            case R.id.txt_left:
                if (pddLink != null && !"".equals(pddLink)) {
                        Intent intent = new Intent(this, WebViewActivity.class);
                        intent.putExtra("title", jdBean.goodsName);
                        intent.putExtra("url", pddLink);
                        startActivity(intent);
//                    JAnalyticsInterface.onEvent(JdDetailsActivity.this,new CountEvent("jd_lq"));
                } else {
                    T.showShort(WphDetailsActivity.this, "该商品没有优惠券");
                }
                break;
            case R.id.copy_taobao_btn:
                boolean isSuccess = ImgUtils.saveImageToGallery(WphDetailsActivity.this, BitmapUtils.createViewBitmap(share_fl, WphDetailsActivity.this));
                if (isSuccess)
                    T.showShort(WphDetailsActivity.this, "保存成功");
                break;
            case R.id.copy_friends_cicle_btn:
                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mm") == null) {
                    T.showShort(WphDetailsActivity.this, "请安装微信客户端");
                    return;
                }
                Bitmap bitmap = BitmapUtils.createViewBitmap(share_fl, WphDetailsActivity.this);
                WxUtil.sharePicByBitMap(bitmap, "pyq", SendMessageToWX.Req.WXSceneTimeline, WphDetailsActivity.this);
//                JAnalyticsInterface.onEvent(JdDetailsActivity.this,new CountEvent("jd_share_goods"));
                break;
            case R.id.copy_friends_btn:
                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mm") == null) {
                    T.showShort(WphDetailsActivity.this, "请安装微信客户端");
                    return;
                }
                Bitmap bitmap1 = BitmapUtils.createViewBitmap(share_fl, WphDetailsActivity.this);
                WxUtil.sharePicByBitMap(bitmap1, "pyq", SendMessageToWX.Req.WXSceneSession, WphDetailsActivity.this);
//                JAnalyticsInterface.onEvent(JdDetailsActivity.this,new CountEvent("jd_share_goods"));
                break;
            case R.id.copy_friends_qq:
                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq") == null) {
                    T.showShort(WphDetailsActivity.this, "请安装QQ客户端");
                    return;
                }
                Bitmap temp = BitmapUtils.createViewBitmap(share_fl, WphDetailsActivity.this);
                String str = ImgUtils.saveImageToGallery2(this, temp);
                if ("".equals(str)) {
                    T.showShort(WphDetailsActivity.this, "分享失败");
                    return;
                }
                QQShareUtil.shareImgToQQ(str, this, this);
//                JAnalyticsInterface.onEvent(JdDetailsActivity.this,new CountEvent("jd_share_goods"));
                break;
            case R.id.copy_friends_cicle_zone:
                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq") == null) {
                    T.showShort(WphDetailsActivity.this, "请安装QQ客户端");
                    return;
                }
                Bitmap temp2 = BitmapUtils.createViewBitmap(share_fl, WphDetailsActivity.this);
                String str1 = ImgUtils.saveImageToGallery2(this, temp2);
                if ("".equals(str1)) {
                    T.showShort(WphDetailsActivity.this, "分享失败");
                    return;
                }
                QQShareUtil.shareImgToQQZONE(str1, this, this);
//                JAnalyticsInterface.onEvent(JdDetailsActivity.this,new CountEvent("jd_share_goods"));
                break;
            case R.id.ll_right: //收藏
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.getData().getString("url").contains("error")) {
                T.showShort(WphDetailsActivity.this, "获取推广链接失败");
            } else {
                erweima_tv.setImageBitmap(ZxingUtils.createBitmap(msg.getData().getString("url")));
            }
            refreshLayout.finishRefresh();
            super.handleMessage(msg);
        }
    };

    /**
     * @属性:获取京东领券短连接
     * @开发者:wmm
     * @时间:2018/12/07 14:03
     */
    private void getGoodsMsgRequest2() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("goodsId", jdBean.goodsId);
        requestParams.put("chanTag", CaiNiaoApplication.getUserInfoBean().user_msg.uid);
        HttpUtils.post(Constants.APP_IP + "/api/WPH/genGoodsUrl", WphDetailsActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    final JSONObject object1 = new JSONObject(responseString).getJSONObject("data");
                    pddLink = object1.getJSONArray("urlInfoList").getJSONObject(0).getString("url");
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("url", pddLink);
                    message.setData(bundle);
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    String temp = "";

    private void getDetail() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("goodsId", jdBean.goodsId);
        HttpUtils.post(Constants.APP_IP + "/api/WPH/goodsInfo",WphDetailsActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    final JSONObject object1 = new JSONObject(responseString).getJSONArray("data").getJSONObject(0);
                    temp = object1.getString("destUrl");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webDetail.loadUrl(temp);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public void onComplete(Object o) {

    }

    @Override
    public void onError(UiError uiError) {

    }

    @Override
    public void onCancel() {

    }
}
