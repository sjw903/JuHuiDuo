package com.android.jdhshop.suning;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.jdhshop.activity.WebViewActivityWithNotIntent;
import com.android.jdhshop.common.LogUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import com.android.jdhshop.activity.WebViewActivity;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.HaoDanBeankuaiqiang;
import com.android.jdhshop.bean.IsCollectBean;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.login.WelActivity;
import com.android.jdhshop.my.RechargeActivity;
import com.android.jdhshop.snadapter.HomeHotAdapter;
import com.android.jdhshop.snbean.HomeGoodlistbean;
import com.android.jdhshop.utils.BannerImageLoader;
import com.android.jdhshop.utils.BitmapUtils;
import com.android.jdhshop.utils.ImgUtils;
import com.android.jdhshop.utils.MyScrollView;
import com.android.jdhshop.utils.WxUtil;
import com.android.jdhshop.utils.ZxingUtils;
import com.android.jdhshop.wmm.QQShareUtil;
import com.youth.banner.Banner;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.analytics.android.api.CountEvent;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cz.msebera.android.httpclient.Header;

/**
 * @属性:推广详情
 * @开发者:WMM
 * @时间:2018/11/22 8:50
 */
public class SnGoodmsgActivity extends BaseActivity implements MyScrollView.OnScrollListener, IUiListener {

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
    @BindView(R.id.txt_desc)
    WebView txtDesc;
    @BindView(R.id.txt_ptj)
    TextView txtPtj;
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
    @BindView(R.id.txt_score1)
    TextView txtScore1;
    @BindView(R.id.txt_score2)
    TextView txtScore2;
    @BindView(R.id.txt_score3)
    TextView txtScore3;
    private boolean isCollect = false;//未收藏
    private ACache mAcache;
    String token;
    @BindView( R.id.details_goopenvip )
    TextView tv_goopenvip;
    @BindView( R.id.sngoodmsg_recy )
    RecyclerView tjrecy;
    String group_id;
    //    private PDDBean pddBean;
    DecimalFormat df = new DecimalFormat("0.00");
    private String pddLink = "";
    private GradientDrawable gradientDrawable;

    private HomeGoodlistbean msgbean;

    private HomeHotAdapter homeHotAdapter;

    @Override
    protected void initUI() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_sn_goodmsg);
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
        msgbean = (HomeGoodlistbean) getIntent().getExtras().get( "msg" );
        WebSettings webSettings = txtDesc.getSettings();
        webSettings.setJavaScriptEnabled(false);//允许使用js
//不支持屏幕缩放
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
//不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);

        mAcache = ACache.get(this);
        token = mAcache.getAsString(Constants.TOKEN);
        group_id = mAcache.getAsString("group_id");
        List<String> listStr = new ArrayList<>();
        homeBanner.setImageLoader(new BannerImageLoader());
        homeBanner.setLayoutParams(new RelativeLayout.LayoutParams(BitmapUtils.getScreenWith(this),BitmapUtils.getScreenWith(this)));
        homeBanner.setDelayTime(3000);
        if (msgbean.commodityInfo.pictureUrl != null && msgbean.commodityInfo.pictureUrl.size() > 0) {
            for (int i = 0; i < msgbean.commodityInfo.pictureUrl.size(); i++) {
                listStr.add(msgbean.commodityInfo.pictureUrl.get( i ).picUrl);
            }
        } else {
//                        listStr.add(object.getJSONObject("goods_details").getString("goods_thumbnail_url"));
        }
        homeBanner.update(listStr);
        homeBanner.start();
//        if ((null != group_id && "3".equals(group_id)) || (null != group_id && "4".equals(group_id))) {//会员不展示  其他展示
            ll_vip.setVisibility(View.GONE);
//            ll_vip.setEnabled(false);
//        } else {
//            ll_vip.setVisibility(View.VISIBLE);
//            ll_vip.setEnabled(true);
//        }
        findViewById(R.id.ll_vip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(RechargeActivity.class);
            }
        });
//        pddBean = (PDDBean) getIntent().getBundleExtra("goods").get("goods");
//        mAcache = ACache.get(this);
//        token = mAcache.getAsString(Constants.TOKEN);
//        group_id = mAcache.getAsString("group_id");
        //动态设置drawableLeft属性
        Drawable drawable1 = getResources().getDrawable(R.mipmap.icon_back_while);
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
        tvLeft.setCompoundDrawables(drawable1, null, null, null);
        tvLeft.setVisibility(View.VISIBLE);
        gradientDrawable = (GradientDrawable) tvLeft.getBackground();
//        gradientDrawable2= (GradientDrawable) llright.getBackground();
        tvTitle.setText("推广详情");

        titleTv.setText(msgbean.commodityInfo.commodityName);
//        storeNameTv.setText(pddBean.getMall_name());
        priceTv.getPaint().setFlags( Paint.STRIKE_THRU_TEXT_FLAG);
        priceTv.setText("原价:¥" + msgbean.commodityInfo.commodityPrice);
        if (msgbean.couponInfo.couponValue==null||msgbean.couponInfo.couponValue.equals( "" )){
            after_coupon_share_tv.setText("¥" +  df.format( Double.parseDouble( msgbean.commodityInfo.commodityPrice ) ));
            tv_for_share.setText("0元");
            afterCouponTv.setText(  "¥" + df.format( Double.parseDouble( msgbean.commodityInfo.commodityPrice ) ) );
//            if (Constants.group_id.equals( "1" )){
//                tvShare.setText( "奖:" + df.format( (Double.parseDouble( msgbean.commodityInfo.commodityPrice ) ) * Double.valueOf( msgbean.commodityInfo.rate ) / 100 * Double.parseDouble( df.format( (float) Double.parseDouble( Constants.yongjinbi ) / 100 ) ) ));
//            }else {
                tvShare.setText( "奖:" + df.format( (Double.parseDouble( msgbean.commodityInfo.commodityPrice ) ) * Double.valueOf( msgbean.commodityInfo.rate ) / 100 * Double.parseDouble( df.format( (float) SPUtils.getIntData( SnGoodmsgActivity.this, "rate", 0 ) / 100.00 ) ) ));
//            tvShare.setText("奖:" + df.format((Double.parseDouble( pddBean.getMin_normal_price() )  - Double.parseDouble( pddBean.getCoupon_discount() ))*Double.parseDouble( pddBean.getPromotion_rate() ) /1000*(SPUtils.getIntData(SnGoodmsgActivity.this, "rate", 0)/100)/100*0.6) + "元" );
//            }
        }else {
            after_coupon_share_tv.setText("¥" +  df.format( Double.parseDouble( msgbean.commodityInfo.commodityPrice ) - Double.parseDouble( msgbean.couponInfo.couponValue ) ));
            tv_for_share.setText(msgbean.couponInfo.couponValue  + "元");
//            if (Constants.group_id.equals( "1" )){
//                tvShare.setText( "奖:" + df.format( (Double.parseDouble( msgbean.commodityInfo.commodityPrice ) - Double.parseDouble( msgbean.couponInfo.couponValue )) * Double.valueOf( msgbean.commodityInfo.rate ) / 100 * Double.parseDouble( df.format( (float) Double.parseDouble( Constants.yongjinbi ) / 100 ) ) )  );
//            }else {
                tvShare.setText( "奖:" + df.format( (Double.parseDouble( msgbean.commodityInfo.commodityPrice ) - Double.parseDouble( msgbean.couponInfo.couponValue )) * Double.valueOf( msgbean.commodityInfo.rate ) / 100 * Double.parseDouble( df.format( (float) SPUtils.getIntData( SnGoodmsgActivity.this, "rate", 0 ) / 100.00 ) ) ));
//            tvShare.setText("奖:" + df.format((Double.parseDouble( pddBean.getMin_normal_price() )  - Double.parseDouble( pddBean.getCoupon_discount() ))*Double.parseDouble( pddBean.getPromotion_rate() ) /1000*(SPUtils.getIntData(SnGoodmsgActivity.this, "rate", 0)/100)/100*0.6) + "元" );
//            }
            afterCouponTv.setText( "¥" + df.format( Double.parseDouble( msgbean.commodityInfo.commodityPrice ) - Double.parseDouble( msgbean.couponInfo.couponValue ) ) );
        }
        txtPtj.setText("¥" + msgbean.commodityInfo.commodityPrice);
        erweima_tv.setImageBitmap( ZxingUtils.createBitmap(msgbean.couponInfo.couponUrl));
//
////        tvShare.setText("奖:" + df.format(Float.valueOf(pddBean.getCommission() == null ? "0" : pddBean.getCommission()) / 100) + "元");
//

//
//
        Glide.with(SnGoodmsgActivity.this).load(msgbean.commodityInfo.pictureUrl.get( 0 ).picUrl).dontAnimate().into(iv);
        price_share_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        price_share_tv.setText("原价¥" + msgbean.commodityInfo.commodityPrice);

        if (msgbean.couponInfo.couponValue.equals("")) {
            txt_left.setText("¥:0元");
        } else {
            txt_left.setText("¥:" + msgbean.couponInfo.couponValue);
        }
//        if (Long.valueOf(pddBean.getSold_quantity()) < 1000) {
        storeSoldNum.setText("销量:" + msgbean.commodityInfo.monthSales);
//        } else {
//            storeSoldNum.setText("销量:" + df.format(Long.valueOf(pddBean.getSold_quantity()) / 10000) + "万");
//        }
//        title_share_tv.setText(pddBean.getGoods_name());

        homeHotAdapter = new HomeHotAdapter( SnGoodmsgActivity.this,R.layout.item_goodlistsn,haobanlist );
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager( SnGoodmsgActivity.this );
        gridLayoutManager.setOrientation( LinearLayoutManager.HORIZONTAL );
        tjrecy.setLayoutManager( gridLayoutManager );
        tjrecy.setAdapter( homeHotAdapter );
        homeHotAdapter.setOnItemClickListener( new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                getlists(haobanlist.get( position ).commodityName);

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        } );
        getlists();
//        //关闭上啦加载
//        refreshLayout.setEnableLoadMore(false);
        isCollectRequest(getIntent().getStringExtra( "goodid" ));
//        if (pddBean.getAvg_serv()!=null&&!pddBean.getAvg_serv().equals( "null" )) {
//            txtScore1.setText( "卖家服务:  " + df.format( Double.valueOf( pddBean.getAvg_serv() ) / 100 ) );
//        }else{
//            txtScore1.setText("");
//        }
//        if (pddBean.getAvg_desc() !=null&&!pddBean.getAvg_desc() .equals( "null" )) {
//            txtScore2.setText( "商品评价:  " + df.format( Double.valueOf( pddBean.getAvg_desc() ) / 100 ) );
//        }else{
//            txtScore2.setText("");
//        }
//        if (pddBean.getAvg_lgst() !=null&&!pddBean.getAvg_lgst() .equals( "null" )) {
//            txtScore3.setText( "物流评分:  " + df.format( Double.valueOf( pddBean.getAvg_lgst() ) / 100 ) );
//        }else{
//            txtScore3.setText("");
//        }




        //开始刷新
        refreshLayout.autoRefresh();

    }

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
                getGoodsMsgRequest();
//                getDetail();
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
        } else if (scrollY >= 100 && scrollY < 355) {
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

    @OnClick({R.id.txt_finish, R.id.tv_left, R.id.txt_left, R.id.copy_taobao_btn,R.id.copy_friends_qq,R.id.copy_friends_cicle_zone, R.id.copy_friends_cicle_btn, R.id.copy_friends_btn, R.id.ll_right, R.id.tv_finish, R.id.view_zz
            ,R.id.details_goopenvip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left: //左边的
                finish();
                break;
            case R.id.details_goopenvip://跳转开通VIP页面
                openActivity( RechargeActivity.class );
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
//                if (group_id.equals( "1" )){
//
//                    showTipDialog2( "提示", Html.fromHtml("请升级至掌柜开启分享赚钱之旅"), new onClickListener() {
//                        @Override
//                        public void onClickSure() {
//
//                        }
//                    },"" );
////                                showToast( msg );
//                    new Thread( new Runnable() {
//                        @Override
//                        public void run() {
//                            SystemClock.sleep( 2000 );
//                            openActivity( OpenVipActivity.class );
//                        }
//                    } ).start();
//                }else {
                zz.setVisibility(View.VISIBLE);
                llShare.setVisibility(View.VISIBLE);
                share_fl.setVisibility(View.VISIBLE);
//                }
//                zz.setVisibility(View.VISIBLE);
//                llShare.setVisibility(View.VISIBLE);
//                share_fl.setVisibility(View.VISIBLE);
                break;
            case R.id.txt_left:

                Intent intent = new Intent(this, WebViewActivityWithNotIntent.class);
                intent.putExtra("title", "优惠券领取");
                LogUtils.d(TAG, "苏宁领券链接: " + pddLink);
                intent.putExtra("url", pddLink);
                startActivity(intent);
                break;
            case R.id.copy_taobao_btn:
                boolean isSuccess= ImgUtils.saveImageToGallery(SnGoodmsgActivity.this,BitmapUtils.createViewBitmap(share_fl, SnGoodmsgActivity.this));
                if(isSuccess)
                    T.showShort(SnGoodmsgActivity.this, "保存成功");
                break;
            case R.id.copy_friends_cicle_btn:
                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mm") == null) {
                    T.showShort(SnGoodmsgActivity.this, "请安装微信客户端");
                    return;
                }
                Bitmap bitmap = BitmapUtils.createViewBitmap(share_fl, SnGoodmsgActivity.this);
                WxUtil.sharePicByBitMap(bitmap, "pyq", SendMessageToWX.Req.WXSceneTimeline, SnGoodmsgActivity.this);
                JAnalyticsInterface.onEvent(SnGoodmsgActivity.this,new CountEvent("pdd_share_goods"));
                break;
            case R.id.copy_friends_btn:
                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mm") == null) {
                    T.showShort(SnGoodmsgActivity.this, "请安装微信客户端");
                    return;
                }
                Bitmap bitmap1 = BitmapUtils.createViewBitmap(share_fl, SnGoodmsgActivity.this);
                WxUtil.sharePicByBitMap(bitmap1, "pyq", SendMessageToWX.Req.WXSceneSession, SnGoodmsgActivity.this);
                JAnalyticsInterface.onEvent(SnGoodmsgActivity.this,new CountEvent("pdd_share_goods"));
                break;
            case R.id.copy_friends_qq:
                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq") == null) {
                    T.showShort(SnGoodmsgActivity.this, "请安装QQ客户端");
                    return;
                }
                Bitmap temp = BitmapUtils.createViewBitmap(share_fl, SnGoodmsgActivity.this);
                String str=ImgUtils.saveImageToGallery2(this,temp);
                if("".equals(str)){
                    T.showShort(SnGoodmsgActivity.this, "分享失败");
                    return;
                }
                QQShareUtil.shareImgToQQ(str, this, this);
                JAnalyticsInterface.onEvent(SnGoodmsgActivity.this,new CountEvent("pdd_share_goods"));
                break;
            case R.id.copy_friends_cicle_zone:
                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq") == null) {
                    T.showShort(SnGoodmsgActivity.this, "请安装QQ客户端");
                    return;
                }
                Bitmap temp2 = BitmapUtils.createViewBitmap(share_fl, SnGoodmsgActivity.this);
                String str1=ImgUtils.saveImageToGallery2(this,temp2);
                if("".equals(str1)){
                    T.showShort(SnGoodmsgActivity.this, "分享失败");
                    return;
                }
                QQShareUtil.shareImgToQQZONE(str1, this,this);
                JAnalyticsInterface.onEvent(SnGoodmsgActivity.this,new CountEvent("pdd_share_goods"));
                break;
            case R.id.ll_right: //收藏
                if (isCollect) { //已收藏，那么取消收藏
                    cancelCollectRequest(getIntent().getStringExtra( "goodid" ));
                } else {//未收藏，那么收藏
                    collectRequest(getIntent().getStringExtra( "goodid" ));
                }
                break;
        }
    }

    /**
     * @属性:获取苏宁商品详情
     * @开发者:wmm
     * @时间:2018/11/22 9:00
     */
    private void getGoodsMsgRequest() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("goodsCode", getIntent().getStringExtra( "goodid" ));
        HttpUtils.post(Constants.getGoodsInfo, SnGoodmsgActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(responseString);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject=new JSONObject(responseString);
                    if (!"0".equals( jsonObject.getString( "code" ) )){
                        return;
                    }
                    JSONObject objects = jsonObject.getJSONObject( "data" );
                    JSONArray object = objects.getJSONObject( "sn_responseContent" ).getJSONObject( "sn_body" ).getJSONArray( "getUnionInfomation" );

//                    if(!Constants.group_id.equals( "1" )) {
//                        tv_num.setText( "升级至淘掌柜等级，最高可得奖¥" + df.format(  Double.parseDouble( ((JSONObject)object.get( 0 )).getString( "prePayCommission" )) * (SPUtils.getIntData( SnGoodmsgActivity.this, "rate", 0 ))/100  )  );
//                    }else{
//                        tv_num.setText( "升级至淘掌柜等级，最高可得奖¥"+df.format(Double.parseDouble( ((JSONObject)object.get( 0 )).getString( "prePayCommission" ) ) *(Double.parseDouble( Constants.yongjinbi ))/100) );
//                    }
                    Log.e( "buyvip", df.format(  Double.parseDouble( ((JSONObject)object.get( 0 )).getString( "prePayCommission" )) * (SPUtils.getIntData( SnGoodmsgActivity.this, "rate", 0 ))  ) );
////                    tv_num.setText("升级会员等级，最高可得奖¥" + df.format(object.getDouble("commission_vip") / 100) + "。");

////                    txtDesc.setText(Html.fromHtml(object.getJSONObject("goods_details").getString("content")));
//                    Document doc = Jsoup.connect( ((JSONObject)object.get( 0 )).getString( "productUrl" )  ).get();
//                    String content = doc.title();
//                    txtDesc.loadDataWithBaseURL( null, HtmlFormat.getNewContent(content ), "text/html", "utf-8", null );
                    txtDesc.setWebViewClient(webViewClient);
                    txtDesc.setWebChromeClient(new MyWebChromeClient());
                    WebSettings webSettings = txtDesc.getSettings();
                    webSettings.setJavaScriptEnabled(false);//允许使用js
                    //支持屏幕缩放
                    webSettings.setDomStorageEnabled(true);
                    webSettings.setUseWideViewPort(true);
                    webSettings.setLoadWithOverviewMode(true);
                    webSettings.setSupportZoom(false);
                    webSettings.setBuiltInZoomControls(false);
                    webSettings.setAllowFileAccess(true);
                    webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
                    webSettings.setSupportZoom(true);
                    webSettings.setBuiltInZoomControls(true);
                    webSettings.setUseWideViewPort(true);
                    webSettings.setSupportMultipleWindows(true);//这里一定得是false,不然打开的网页中，不能在点击打开了
                    // webSetting.setLoadWithOverviewMode(true);
                    webSettings.setAppCacheEnabled(true);
                    // webSetting.setDatabaseEnabled(true);
                    webSettings.setDomStorageEnabled(true);
                    webSettings.setJavaScriptEnabled(true);
                    webSettings.setGeolocationEnabled(true);
                    webSettings.setAppCacheMaxSize(Long.MAX_VALUE);
                    // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
                    webSettings.setPluginState(WebSettings.PluginState.ON_DEMAND);
                    txtDesc.loadUrl(((JSONObject)object.get( 0 )).getString( "productUrlWap" ));
//                    txtDesc.setText( Html.fromHtml( content) );
                    getGoodsMsgRequest1(((JSONObject)object.get( 0 )).getString( "productUrlWap" ),msgbean.couponInfo.couponUrl);
//                    JSONArray urls = object.getJSONArray("url_list");
//                    Log.e( "content", object.getJSONObject("goods_details").getString("content"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void getGoodsMsgRequest1(String url,String couponUrl) {
//        if(msgbean.couponInfo.couponUrl==null||"".equals(msgbean.couponInfo.couponUrl)||"null".equals(msgbean.couponInfo.couponUrl)){
//            erweima_tv.setImageBitmap( ZxingUtils.createBitmap(((JSONObject)object.get( 0 )).getString( "productUrlWap" )));
//            pddLink = ((JSONObject)object.get( 0 )).getString( "productUrlWap" );
//        }else{
//            pddLink = msgbean.couponInfo.couponUrl;
//        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("detailUrl", url);
        requestParams.put("quanUrl", couponUrl);
        requestParams.put("subUser", CaiNiaoApplication.getUserInfoBean().user_msg.uid);
        HttpUtils.post(Constants.APP_IP+"/api/Suning/getgoodsandcouponurl", SnGoodmsgActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(responseString);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("dfasdf",responseString.toString());
                try {
                    JSONObject jsonObject=new JSONObject(responseString);
                    if (!"0".equals( jsonObject.getString( "code" ) )){
                        return;
                    }
                    JSONObject objects = jsonObject.getJSONObject( "data" ).getJSONObject("sn_responseContent").getJSONObject("sn_body").getJSONObject("getExtensionlink");
                        erweima_tv.setImageBitmap( ZxingUtils.createBitmap(objects.getString( "shortLink" )));
                        pddLink = objects.getString( "shortLink" );

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private class MyWebChromeClient extends WebChromeClient {
        private View mCustomView;
        private CustomViewCallback mCustomViewCallback;
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mCustomView = view;
            setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        public void onHideCustomView() {
            txtDesc.setVisibility(View.VISIBLE);
            if (mCustomView == null) {
                return;
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            super.onHideCustomView();
        }
    }

    boolean index = true;
    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String newurl) {
            if (index){
                index = false;
                return super.shouldOverrideUrlLoading(view, newurl);

            }else {
                return true;
            }
////            if(newurl.contains("/error?code=")){
////                SPUtils.saveStringData(WebViewActivity.this,"coded",getValueByName(newurl,"code"));
////                finish();
////            }
//            return super.shouldOverrideUrlLoading(view, newurl);
        }
    };







    /**
     * @属性:用户是否收藏商品
     * @开发者:陈飞
     * @时间:2018/7/28 21:52
     */
    private void isCollectRequest(String goods_id) {
        RequestParams requestParams = new RequestParams();
        requestParams.put( "token",SPUtils.getStringData(SnGoodmsgActivity.this, "token", "") );
        requestParams.put("goods_id", goods_id);
        HttpUtils.post(Constants.APP_IP+"/api/SuningGoodsCollect/is_collect", SnGoodmsgActivity.this,requestParams, new onOKJsonHttpResponseHandler<IsCollectBean>(new TypeToken<Response<IsCollectBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(responseString);
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
                        //动态设置drawableLeft属性
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
        requestParams.put( "token",SPUtils.getStringData(SnGoodmsgActivity.this, "token", "") );
        requestParams.put("goods_id", goods_id);

        HttpUtils.post(Constants.APP_IP+"/api/SuningGoodsCollect/collect", SnGoodmsgActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(responseString);
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
        requestParams.put( "token",SPUtils.getStringData(SnGoodmsgActivity.this, "token", "") );
        requestParams.put("goods_id", goods_id);
        HttpUtils.post(Constants.APP_IP+"/api/SuningGoodsCollect/cancelCollect",SnGoodmsgActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(responseString);
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


    public List<HaoDanBeankuaiqiang> haobanlist = new ArrayList(  );
    private void getlists() {

        RequestParams requestParams = new RequestParams();
        requestParams.put( "supplierCode",getIntent().getStringExtra( "shopid" ) );
        requestParams.put( "commodityCode",getIntent().getStringExtra( "goodid" ) );
        HttpUtils.post1( Constants.sn_appip+"/api/Suning/getRelationGoods",SnGoodmsgActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
//                if (smartRefreshLayout!=null){
//                    smartRefreshLayout.finishRefresh();
//                    smartRefreshLayout.finishLoadMore();
//                }else{
//                    showLoadingDialog();
//                }
            }
            @Override
            public void onFinish() {
                super.onFinish();
//                if (smartRefreshLayout != null) {
//
//                }else{
//                    closeLoadingDialog();
//                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("dfsafd",responseString);
                try {
                    JSONObject jsonObject=new JSONObject(responseString);
                    if ("0".equals( jsonObject.getString( "code" ) )) {
                        JSONObject object = jsonObject.getJSONObject( "data" );
                        JSONArray array;
                        array = object.getJSONObject( "sn_responseContent" ).getJSONObject( "sn_body" ).getJSONObject( "getMorerecommend" ).getJSONArray( "commodityList" );
                        for (int i = 0; i < array.length(); i++) {
                            HaoDanBeankuaiqiang beankuaiqiang=new Gson().fromJson( array.getJSONObject( i ).toString(), HaoDanBeankuaiqiang.class );
                                if(array.getJSONObject(i).get("picList") instanceof String){
                                    beankuaiqiang.img=array.getJSONObject(i).getString("picList");
                                }else{
                                    beankuaiqiang.img=array.getJSONObject(i).getJSONArray("picList").getJSONObject(0).getString("cmmdtyUrl");
                                }
                            haobanlist.add(  beankuaiqiang);
                        }
                        homeHotAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    List<HomeGoodlistbean> homegoodlist = new ArrayList<>(  );
    private void getlists(String seek) {

        RequestParams requestParams = new RequestParams();
        requestParams.put( "keywords", seek );
        requestParams.put( "page","1" );
        requestParams.put( "pagesize","8" );
        HttpUtils.post1( Constants.sn_appip+"/api/Suning/getKeywordGoods", SnGoodmsgActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
//                if (smartRefreshLayout!=null){
//                    smartRefreshLayout.finishRefresh();
//                    smartRefreshLayout.finishLoadMore();
//                }else{
//                    showLoadingDialog();
//                }
            }
            @Override
            public void onFinish() {
                super.onFinish();
//                if (smartRefreshLayout != null) {
//
//                }else{
//                    closeLoadingDialog();
//                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("dfasd",responseString);
                try {
                    JSONObject jsonObject=new JSONObject(responseString);
                    if ("0".equals( jsonObject.getString( "code" ) )) {
                        JSONObject object = jsonObject.getJSONObject( "data" );
                        JSONArray array;
                            array = object.getJSONObject( "sn_responseContent" ).getJSONObject( "sn_body" ).getJSONArray( "querySearchcommodity" );
                        for (int i = 0; i < array.length(); i++) {
                            homegoodlist.add( new Gson().fromJson( array.getJSONObject( i ).toString(), HomeGoodlistbean.class ) );
                        }
                        Intent intent = new Intent( SnGoodmsgActivity.this,SnGoodmsgActivity.class );
                        intent.putExtra( "goodid", homegoodlist.get( 0 ).commodityInfo.commodityCode);
                        intent.putExtra( "shopid",homegoodlist.get( 0 ).commodityInfo.supplierCode );
                        intent.putExtra( "msg", homegoodlist.get( 0 ));
                        startActivity( intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
