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
import android.widget.Toast;

import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.widget.EchartView;
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
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.IsCollectBean;
import com.android.jdhshop.bean.MyGoodsResp;
import com.android.jdhshop.bean.Response;
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
import jd.union.open.goods.query.response.GoodsResp;
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
public class JdDetailsActivity extends BaseActivity implements MyScrollView.OnScrollListener,IUiListener {

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
    @BindView(R.id.xqzq_icon)
    ImageView xqzq_icon;
    @BindView(R.id.store_sold_num)
    TextView storeSoldNum;
    @BindView(R.id.echart_view)
    EchartView echartView;
    @BindView(R.id.jgqs_title)
    TextView jgqs_title;
    private boolean isCollect = false;//未收藏
    private ACache mAcache;
    String token;
    private String comm;
    String group_id;
    private MyGoodsResp jdBean;
    DecimalFormat df = new DecimalFormat("0.00");
    private String pddLink = "";
    private GradientDrawable gradientDrawable;
    @BindView(R.id.web_detail)
    WebView webDetail;
    private KeplerAttachParameter mKeplerAttachParameter = new KeplerAttachParameter();
    OpenAppAction mOpenAppAction;
    private String url;

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
        jdBean = (MyGoodsResp) getIntent().getBundleExtra("goods").get("goods");
        getDetail();
        loadTrendEcharts(jdBean.getSkuId() + "", Constants.trendChannel_JD, echartView, jgqs_title);

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
        if (jdBean.imageInfo != null && jdBean.imageInfo.getImageList().length > 0) {
            int lenght = jdBean.imageInfo.getImageList().length;
            List<String> images = new ArrayList<>();
            for (int i = 0; i < lenght; i++)
                images.add(jdBean.imageInfo.getImageList()[i].getUrl());
            homeBanner.update(images);
        }
        homeBanner.start();
        SpannableString spannableString = new SpannableString("   " + jdBean.getSkuName());
        Drawable drawable = getResources().getDrawable(R.mipmap.label_jd);
        drawable.setBounds(0, 0, 120, 100);

        spannableString.setSpan(new VerticalImageSpan(drawable), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        titleTv.setText(spannableString);
        storeNameTv.setText(jdBean.shopInfo.getShopName());
        price_share_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        priceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        priceTv.setText("原价¥" + jdBean.priceInfo.getPrice());
        price_share_tv.setText("原价¥" + jdBean.priceInfo.getPrice());
        try {
            afterCouponTv.setText("¥" + df.format(jdBean.pingGouInfo.getPingouPrice() - jdBean.couponInfo.getCouponList()[0].getDiscount()));
            after_coupon_share_tv.setText("¥" + df.format(jdBean.pingGouInfo.getPingouPrice() - jdBean.couponInfo.getCouponList()[0].getDiscount()));
        } catch (Exception e) {
            try {
                afterCouponTv.setText("¥" + df.format(jdBean.priceInfo.getPrice() - jdBean.couponInfo.getCouponList()[0].getDiscount()));
                after_coupon_share_tv.setText("¥" + df.format(jdBean.priceInfo.getPrice() - jdBean.couponInfo.getCouponList()[0].getDiscount()));
            } catch (Exception eq) {
                afterCouponTv.setText("¥" + df.format(jdBean.priceInfo.getPrice()));
                after_coupon_share_tv.setText("¥" + df.format(jdBean.priceInfo.getPrice()));
            }
        }
        if (jdBean.commissionInfo == null) {
            tvShare.setText("奖:" + afterCouponTv.getText().toString().replace("¥", ""));
        } else {
            tvShare.setText("奖:" + df.format(Double.valueOf(df.format(jdBean.commissionInfo.getCommissionShare() / 100)) * Double.valueOf(afterCouponTv.getText().toString().replace("¥", "")) * Double.parseDouble(df.format((float) SPUtils.getIntData(this, "rate", 0) / 100))) + "元");
            comm =  df.format(Double.valueOf(df.format(jdBean.commissionInfo.getCommissionShare() / 100)) * Double.valueOf(afterCouponTv.getText().toString().replace("¥", "")) * Double.parseDouble(df.format((float) SPUtils.getIntData(this, "rate", 0) / 100)));
        }
        price_share_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        price_share_tv.setText("原价¥" + jdBean.priceInfo.getPrice());
        try {
            tv_for_share.setText(jdBean.couponInfo.getCouponList()[0].getDiscount() + "");
            txt_left.setText(jdBean.couponInfo.getCouponList()[0].getDiscount() + "元");
        } catch (Exception e) {
            tv_for_share.setText(0 + "");
            txt_left.setText(0 + "元");
        }
//        try {
//            tv_for_share.setText(jdBean.getCouponInfo()[0].getCouponList()[0].getDiscount() + "");
//            txt_left.setText("¥:" + jdBean.getCouponInfo()[0].getCouponList()[0].getDiscount() + "元");
//        } catch (Exception e) {
//            tv_for_share.setText(0 + "");
//            txt_left.setText("¥:" + 0 + "元");
//        }
        storeSoldNum.setText("销量:" + jdBean.getInOrderCount30Days());
        title_share_tv.setText(jdBean.getSkuName());
        Glide.with(JdDetailsActivity.this).load(jdBean.imageInfo.getImageList()[0].getUrl()).dontAnimate().into(iv);
        String zhuaqu = SPUtils.getStringData(this, "zhuaqu", "");
        if (zhuaqu.equals("1")) {
            xqzq_icon.setVisibility(View.VISIBLE);
            addAi();
        }
        //关闭上啦加载
        refreshLayout.setEnableLoadMore(false);
        tv_num.setText("升级会员等级，最高可得奖¥" + df.format(Double.valueOf(df.format(jdBean.commissionInfo.getCommissionShare() / 100)) * Double.valueOf(afterCouponTv.getText().toString().replace("¥", "")) * 0.9) + "。");
        //开始刷新
        refreshLayout.autoRefresh();
        isCollectRequest(jdBean.getSkuId() + "");
        mOpenAppAction = new OpenAppAction() {
            @Override
            public void onStatus(final int status, final String url) {
                mHandle.post(new Runnable() {
                    @Override
                    public void run() {
                        if (status == OpenAppAction.OpenAppAction_start) {//开始状态未必一定执行，
//                            dialogShow();
                        } else {
//                            mKelperTask = null;
//                            dialogDiss();
                        }
                        if (status == OpenAppAction.OpenAppAction_result_NoJDAPP) {
                            //未安装京东
                        } else if (status == OpenAppAction.OpenAppAction_result_BlackUrl) {
                            //不在白名单
                        } else if (status == OpenAppAction.OpenAppAction_result_ErrorScheme) {
                            //协议错误
                        } else if (status == OpenAppAction.OpenAppAction_result_APP) {
                            //呼京东成功
                        } else if (status == OpenAppAction.OpenAppAction_result_NetError) {
                            //网络异常
                        }
                    }
                });
            }
        };

    }

    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void initListener() {
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

    @OnClick({R.id.txt_finish, R.id.tv_left, R.id.xqzq_icon, R.id.ll_right, R.id.copy_friends_qq, R.id.copy_friends_cicle_zone, R.id.txt_left, R.id.copy_taobao_btn, R.id.copy_friends_cicle_btn, R.id.copy_friends_btn, R.id.tv_finish, R.id.view_zz})
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
                    LogUtils.d("JD", "onViewClicked: 没安装JDAPP时的连接地址：" + pddLink);
                    if (getPackageManager().getLaunchIntentForPackage("com.jingdong.app.mall") == null) {
                        Intent intent = new Intent(this, WebViewActivity.class);
                        intent.putExtra("title", jdBean.getSkuName());
                        intent.putExtra("url", pddLink);
                        startActivity(intent);
//                    JAnalyticsInterface.onEvent(JdDetailsActivity.this,new CountEvent("jd_lq"));
                    } else {
                        LogUtils.d(TAG, "onViewClicked: 安装京东APP时的传参" + mKeplerAttachParameter.toString() + "," + mOpenAppAction.toString());
                        KeplerApiManager.getWebViewService().openAppWebViewPage(this,
                                pddLink,
                                mKeplerAttachParameter,
                                mOpenAppAction);
                    }
                } else {
                    T.showShort(JdDetailsActivity.this, "该商品没有优惠券");
                }
                break;
            case R.id.copy_taobao_btn:
                boolean isSuccess = ImgUtils.saveImageToGallery(JdDetailsActivity.this, BitmapUtils.createViewBitmap(share_fl, JdDetailsActivity.this));
                if (isSuccess)
                    T.showShort(JdDetailsActivity.this, "保存成功");
                break;
            case R.id.copy_friends_cicle_btn:
                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mm") == null) {
                    T.showShort(JdDetailsActivity.this, "请安装微信客户端");
                    return;
                }
                Bitmap bitmap = BitmapUtils.createViewBitmap(share_fl, JdDetailsActivity.this);
                WxUtil.sharePicByBitMap(bitmap, "pyq", SendMessageToWX.Req.WXSceneTimeline, JdDetailsActivity.this);
//                JAnalyticsInterface.onEvent(JdDetailsActivity.this,new CountEvent("jd_share_goods"));
                break;
            case R.id.copy_friends_btn:
                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mm") == null) {
                    T.showShort(JdDetailsActivity.this, "请安装微信客户端");
                    return;
                }
                Bitmap bitmap1 = BitmapUtils.createViewBitmap(share_fl, JdDetailsActivity.this);
                WxUtil.sharePicByBitMap(bitmap1, "pyq", SendMessageToWX.Req.WXSceneSession, JdDetailsActivity.this);
//                JAnalyticsInterface.onEvent(JdDetailsActivity.this,new CountEvent("jd_share_goods"));
                break;
            case R.id.copy_friends_qq:
                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq") == null) {
                    T.showShort(JdDetailsActivity.this, "请安装QQ客户端");
                    return;
                }
                Bitmap temp = BitmapUtils.createViewBitmap(share_fl, JdDetailsActivity.this);
                String str=ImgUtils.saveImageToGallery2(this,temp);
                if("".equals(str)){
                    T.showShort(JdDetailsActivity.this, "分享失败");
                    return;
                }
                QQShareUtil.shareImgToQQ(str, this, this);
//                JAnalyticsInterface.onEvent(JdDetailsActivity.this,new CountEvent("jd_share_goods"));
                break;
            case R.id.copy_friends_cicle_zone:
                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq") == null) {
                    T.showShort(JdDetailsActivity.this, "请安装QQ客户端");
                    return;
                }
                Bitmap temp2 = BitmapUtils.createViewBitmap(share_fl, JdDetailsActivity.this);
                String str1=ImgUtils.saveImageToGallery2(this,temp2);
                if("".equals(str1)){
                    T.showShort(JdDetailsActivity.this, "分享失败");
                    return;
                }
                QQShareUtil.shareImgToQQZONE(str1, this,this);
//                JAnalyticsInterface.onEvent(JdDetailsActivity.this,new CountEvent("jd_share_goods"));
                break;
            case R.id.ll_right: //收藏
                if (isCollect) { //已收藏，那么取消收藏
                    if (jdBean != null) {
                        cancelCollectRequest(jdBean.getSkuId() + "");
                    }
                } else {//未收藏，那么收藏
                    if (jdBean != null) {
                        collectRequest(jdBean.getSkuId() + "");
                    }
                }
                break;

        }
    }
    private void addAi(){
        RequestParams params1 = new RequestParams();
        params1.put("product_id_str", jdBean.getSkuId());
        HttpUtils.post(Constants.getJianCHa,  JdDetailsActivity.this,params1, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject( responseString );
                    String string = jsonObject.getString("list");
                    if(string.equals("[]")){
                        xqzq_icon.setVisibility(View.VISIBLE);
                    }else{
                        xqzq_icon.setVisibility(View.GONE);
                    }
                    xqzq_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(string.equals("[]")){
                                String huoquaddid = SPUtils.getStringData(JdDetailsActivity.this, "huoquaddid", "");
                                String replace = huoquaddid.replaceAll("\\[", "");
                                String s = replace.replaceAll("]", "");
                                RequestParams params = new RequestParams();
                                params.put("gid", s);
                                params.put("pid", Constants.PLATFORM_JD);
                                params.put("id", jdBean.getSkuId());
                                params.put("title", jdBean.getSkuName());//商品标题
                                params.put("desc", jdBean.getSkuName());//商品推荐描述
                                params.put("img", jdBean.imageInfo.getImageList()[0].getUrl());//宣传图片
                                params.put("price", jdBean.pingGouInfo!=null?jdBean.priceInfo.getPrice():0);//商品原价
                                params.put("org_price", "");//商品拼团价格（接口对应字段price_pg）
                                params.put("after_price", jdBean.priceInfo.getPrice()- jdBean.couponInfo.getCouponList()[0].getDiscount()+"");//券后/活动/折扣后最终价格（接口对应字段price_after）
                                params.put("commission", df.format(Double.valueOf(jdBean.priceInfo.getPrice() - jdBean.couponInfo.getCouponList()[0].getDiscount()) * Double.valueOf(df.format(jdBean.commissionInfo.getCommissionShare() / 100))));//佣金
                                params.put("ts_time", "");//商品推荐描述
                                params.put("ticket_start_time",jdBean.couponInfo.getCouponList()[0].getGetStartTime()+"");//券开始时间
                                params.put("ticket_end_time",jdBean.couponInfo.getCouponList()[0].getGetEndTime()+"");//券开始时间
                                params.put("linkurl",jdBean.couponInfo.getCouponList()[0].getLink());//推广购买链接
                                params.put("descurl", "");//详情页面链接coupon_amount
                                params.put("discount", jdBean.couponInfo.getCouponList()[0].getDiscount() + "");
                                params.put("shopname",jdBean.shopInfo.getShopName());
                                HttpUtils.post(Constants.getAddShngPin, JdDetailsActivity.this,params, new TextHttpResponseHandler() {
                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                                    }

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                        try {
                                            JSONObject jsonObject = new JSONObject( responseString );
                                            String msg = jsonObject.getString("msg");
                                            showToast(msg);
                                            xqzq_icon.setVisibility(View.GONE);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }else{
                                showToast("已有该商品");
                            }
                        }
                    });

                    //showToast("添加成功");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.getData().getString("url").contains("error")) {
                T.showShort(JdDetailsActivity.this, "获取推广链接失败");
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
    private void getGoodsMsgRequest2(String string) {
        OkHttpUtils.getInstance().get(Constants.JDNEWGOODS_COUPON + "&goods_id=" + jdBean.getSkuId() + "&positionid=" + SPUtils.getStringData(this, "uid", "") + "&couponurl=" + URLEncoder.encode(string), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d("dsfasdf",e.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String s = response.body().string();
                try {
                    final JSONObject object1 = new JSONObject(s);
                    pddLink = object1.getString("data");
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
        requestParams.put("apikey", Constants.JD_APP_KEY_NEW);
        requestParams.put("goods_id", jdBean.getSkuId());
        LogUtils.d("京东产品请求","参数: " + requestParams.toString());
        OkHttpUtils.getInstance().get(Constants.JDNEWGOODS_DETAIL + "?" + requestParams.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d("dsfasdf",e.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String s = response.body().string();
                try {
                    final JSONObject object1 = new JSONObject(s).getJSONObject("data");
                    temp = "";
                    String[] de = object1.getString("picurls").split(",");
                    for (int i = 0; i < de.length; i++) {
                        temp += "<img style='width:100%;height:auto' src='" + de[i] + "'/>";
                    }
                    temp = "<html>" + temp + "</html>";
                    url = object1.getString("couponurl");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webDetail.loadData(temp, "text/html", "utf-8");
                            getGoodsMsgRequest2(url);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getUrl(String detail) {
        HttpUtils.get( JdDetailsActivity.this,"http://" + detail, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject object1=new JSONObject(responseString);
                    LogUtils.d("dsfasdf",responseString);
                    if(object1.getString("content").contains("https://")){
                        if(object1.getString("content").contains("width")){
                            webDetail.loadData(object1.getString("content").replaceAll("data-lazyload","src").replaceAll("width: auto","width:100%").replaceAll("width:auto","width:100%"), "text/html", "utf-8");
                        }else{
                            webDetail.loadData(object1.getString("content").replaceAll("data-lazyload","src").replaceAll("<img","<img style='width:100%;height:auto'").replaceAll("width:auto","width:100%"), "text/html", "utf-8");
                        }
                    } else {
                        if (object1.getString("content").contains("width")) {
                            webDetail.loadData(object1.getString("content").replaceAll("data-lazyload", "src").replaceAll("src=\"//", "src=\"https://").replaceAll("width:auto", "width:100%").replaceAll("width: auto", "width:100%"), "text/html", "utf-8");
                        } else {
                            webDetail.loadData(object1.getString("content").replaceAll("data-lazyload", "src").replaceAll("src=\"//", "src=\"https://").replaceAll("<img", "<img style='width:100%;height:auto'"), "text/html", "utf-8");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * @属性:获取京东领券短连接
     * @开发者:wmm
     * @时间:2018/12/07 14:03
     */
    private void getGoodsMsgRequest() {
        String SERVER_URL = "https://router.jd.com/api";
        String appKey = Constants.JD_CLIENT_ID;
        String appSecret = Constants.JD_SECRET;
        String accessToken = "";
        final JdClient client = new DefaultJdClient(SERVER_URL, accessToken, appKey, appSecret);
        final UnionOpenPromotionBysubunionidGetRequest request = new UnionOpenPromotionBysubunionidGetRequest();
        PromotionCodeReq promotionCodeReq = new PromotionCodeReq();
        promotionCodeReq.setMaterialId("https://wqitem.jd.com/item/view?sku=" + jdBean.getSkuId());
//        promotionCodeReq.setPid("1000484908_4100141768_3003139315");
        promotionCodeReq.setPositionId(Long.valueOf(Constants.JD_POSITIONID));
        promotionCodeReq.setSubUnionId(SPUtils.getStringData(this, Constants.UID, ""));
        request.setPromotionCodeReq(promotionCodeReq);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    try {
                        LogUtils.d(TAG, "run: "+request.getAppJsonParams());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    UnionOpenPromotionBysubunionidGetResponse response = client.execute(request);
                    if ("success".equals(response.getMessage())) {
//                        pddLink = response.getData().getShortURL();
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("url", response.getData().getShortURL());
                        message.setData(bundle);
                        handler.sendMessage(message);
                    } else {
                    }
                } catch (JdException e) {
                    e.printStackTrace();
                } finally {
                    refreshLayout.finishRefresh();
                }
            }
        }).start();
    }

    /**
     * @属性:用户是否收藏商品
     * @开发者:陈飞
     * @时间:2018/7/28 21:52
     */
    private void isCollectRequest(String goods_id) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("goods_id", goods_id);
        HttpUtils.post(Constants.IS_JD_GOOD_COLLECT, JdDetailsActivity.this, requestParams, new onOKJsonHttpResponseHandler<IsCollectBean>(new TypeToken<Response<IsCollectBean>>() {
        }) {
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
            public void onSuccess(int statusCode, Response<IsCollectBean> datas) {
                if (datas.isSuccess()) {
                    String is_collect = datas.getData().getIs_collect();
                    if ("Y".equals(is_collect)) {//Y已收藏
                        isCollect = true;
                        Drawable drawable1 = getResources().getDrawable(R.mipmap.coll_red);
                        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                        llRight.setCompoundDrawables(drawable1, null, null, null);
                        llRight.setText("已收藏");
                    } else if ("N".equals(is_collect)) {//N未收藏
                        isCollect = false;
                        Drawable drawable1 = getResources().getDrawable(R.mipmap.coll_dark);
                        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                        llRight.setCompoundDrawables(drawable1, null, null, null);
                        llRight.setText("收藏");
                    }
                } else {
                    showToast(datas.getMsg());
                }
            }
        });
    }

    /**
     * @属性:收藏
     * @开发者:陈飞
     * @时间:2018/7/28 22:04
     */
    private void collectRequest(final String goods_id) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("goods_id", goods_id);
        requestParams.put("goods_name", jdBean.getSkuName());
        HttpUtils.post(Constants.COLLECT_JD_GOOD, JdDetailsActivity.this, requestParams, new TextHttpResponseHandler() {
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
                        isCollect = true;
                        Drawable drawable1 = getResources().getDrawable(R.mipmap.coll_red);
                        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                        llRight.setCompoundDrawables(drawable1, null, null, null);
                        llRight.setText("已收藏");
                    } else {
                        showToast(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * @属性:收藏
     * @开发者:陈飞
     * @时间:2018/7/28 22:04
     */
    private void cancelCollectRequest(final String goods_id) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("goods_id", goods_id);
        HttpUtils.post(Constants.DE_COLLECT_JD_GOOD,  JdDetailsActivity.this,requestParams, new TextHttpResponseHandler() {
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
                        isCollect = false;
                        Drawable drawable1 = getResources().getDrawable(R.mipmap.coll_dark);
                        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                        llRight.setCompoundDrawables(drawable1, null, null, null);
                        llRight.setText("收藏");
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
    public void onComplete(Object o) {

    }

    @Override
    public void onError(UiError uiError) {

    }

    @Override
    public void onCancel() {

    }
}
