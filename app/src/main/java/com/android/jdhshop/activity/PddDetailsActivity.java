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
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.text.Html;
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
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.utils.CheckUtil;
import com.android.jdhshop.utils.DES3DUtils;
import com.android.jdhshop.utils.OkHttpUtils;
import com.android.jdhshop.widget.EchartView;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.hp.hpl.sparta.Text;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.IsCollectBean;
import com.android.jdhshop.bean.PDDBean;
import com.android.jdhshop.bean.PddClient;
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
import com.android.jdhshop.utils.RoundImageView2;
import com.android.jdhshop.utils.WxUtil;
import com.android.jdhshop.utils.ZxingUtils;
import com.android.jdhshop.wmm.QQShareUtil;
import com.youth.banner.Banner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.analytics.android.api.CountEvent;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;

/**
 * @属性:推广详情
 * @开发者:WMM
 * @时间:2018/11/22 8:50
 */
public class PddDetailsActivity extends BaseActivity implements MyScrollView.OnScrollListener, IUiListener {

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
    //    @BindView(R.id.txt_desc)
//    TextView txtDesc;
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
    @BindView(R.id.rating)
    RatingBar rating;
    @BindView(R.id.store_sold_num)
    TextView storeSoldNum;
    @BindView(R.id.txt_score1)
    TextView txtScore1;
    @BindView(R.id.txt_score2)
    TextView txtScore2;
    @BindView(R.id.txt_score3)
    TextView txtScore3;
    @BindView(R.id.txt_left2)
    TextView txt_left2;
    @BindView(R.id.progressBar1)
    ProgressBar progressBar1;
    @BindView(R.id.txt_one)
    TextView txtOne;
    @BindView(R.id.txt_total)
    TextView txt_total;
    @BindView(R.id.progressBar2)
    ProgressBar progressBar2;
    @BindView(R.id.txt_two)
    TextView txtTwo;
    @BindView(R.id.progressBar3)
    ProgressBar progressBar3;
    @BindView(R.id.txt_three)
    TextView txtThree;
    private boolean isCollect = false;//未收藏
    private ACache mAcache;
    String token;
    @BindView(R.id.web_detail)
    WebView webDetail;
    @BindView(R.id.txt_tag)
    TextView txt_tag;
    @BindView(R.id.go_lingquan)
    TextView go_lingquan;
    String group_id;
    @BindView(R.id.txt_wuliu)
    TextView txt_wuliu;
    @BindView(R.id.txt_miaoshu)
    TextView txt_miaoshu;
    @BindView(R.id.txt_fuwu)
    TextView txt_fuwu;
    @BindView(R.id.shope_img)
    RoundImageView2 shope_img;
    @BindView(R.id.jgqs_title)
    TextView jgqs_title;
    @BindView(R.id.xqzq_icon)
    ImageView xqzq_icon;
    @BindView(R.id.echart_view)
    EchartView echartView;
    private PDDBean pddBean;
    DecimalFormat df = new DecimalFormat("0.00");
    private String pddLink = "";
    private GradientDrawable gradientDrawable;
    Map<String, String> map = new HashMap<>();
    private String comm;
    //（4-送货入户并安装,5-送货入户,6-电子发票,9-坏果包赔,11-闪电退款,12-24小时发货,13-48小时发货,17-顺丰包邮,18-只换不修,19-全国联保,20-分期付款,24-极速退款,25-品质保障,26-缺重包退,27-当日发货,28-可定制化,29-预约配送,1000001-正品发票,1000002-送货入户并安装
    @Override
    protected void initUI() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_pdd_details);
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
        map.put("4", "送货入户并安装");
        map.put("5", "送货入户");
        map.put("6", "电子发票");
        map.put("9", "坏果包赔");
        map.put("11", "闪电退款");
        map.put("12", "24小时发货");
        map.put("13", "48小时发货");
        map.put("17", "顺丰包邮");
        map.put("18", "只换不修");
        map.put("19", "全国联保");
        map.put("20", "分期付款");
        map.put("24", "极速退款");
        map.put("25", "品质保障");
        map.put("26", "缺重包退");
        map.put("27", "当日发货");
        map.put("1000001", "正品发票");
        //showToast("5555");
        webDetail.getSettings().setJavaScriptEnabled(true);
        //showToast("66666");
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
                super.onPageFinished(view, url);
            }
        });
        webDetail.setWebChromeClient(new WebChromeClient());
        pddBean = (PDDBean) getIntent().getBundleExtra("goods").get("goods");
        //showToast("11111");
        try{
            LogUtils.d(TAG, "initData: " + pddBean.getGoods_id());
            loadTrendEcharts(pddBean.getGoods_id(),1,echartView,jgqs_title);}
        catch
        (Exception e)
        { e.printStackTrace();}
        mAcache = ACache.get(this);
        token = mAcache.getAsString(Constants.TOKEN);
        group_id = mAcache.getAsString("group_id");
        //动态设置drawableLeft属性
        Drawable drawable1 = getResources().getDrawable(R.mipmap.icon_back_while);
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
        tvLeft.setCompoundDrawables(drawable1, null, null, null);
        tvLeft.setVisibility(View.VISIBLE);
        gradientDrawable = (GradientDrawable) tvLeft.getBackground();
//        gradientDrawable2= (GradientDrawable) llright.getBackground();
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
        homeBanner.setImageLoader(new BannerImageLoader());
        homeBanner.setLayoutParams(new RelativeLayout.LayoutParams(BitmapUtils.getScreenWith(this), BitmapUtils.getScreenWith(this)));
        homeBanner.setDelayTime(3000);
        titleTv.setText(pddBean.getGoods_name());
        storeNameTv.setText(pddBean.getMall_name());
        priceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        priceTv.setText("原价:¥" + df.format(Float.valueOf(pddBean.getMin_normal_price()) / 100));
        afterCouponTv.setText("¥" + df.format((Double.valueOf(df.format(Float.valueOf(pddBean.getMin_group_price()) / 100)) - Double.valueOf(df.format(Float.valueOf(pddBean.getCoupon_discount()) / 100)))));
        txtPtj.setText("¥" + df.format(Float.valueOf(pddBean.getMin_group_price()) / 100));
        tvShare.setText("奖:" + df.format(Float.valueOf(pddBean.getCommission() == null ? "0" : pddBean.getCommission()) / 100) + "元");
        comm=df.format(Float.valueOf(pddBean.getCommission() == null ? "0" : pddBean.getCommission()) / 100);
//        tvShare.setText("奖:" + (pddBean.getCommission() == null ? "0" : pddBean.getCommission()) + "元");
        price_share_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        price_share_tv.setText("原价¥" + df.format(Float.valueOf(pddBean.getMin_normal_price()) / 100));
        tv_for_share.setText(df.format(Float.valueOf(pddBean.getCoupon_discount()) / 100).replace(".00", ""));
        txt_left.setText(Html.fromHtml("¥:" + df.format((Double.valueOf(df.format(Float.valueOf(pddBean.getMin_group_price()) / 100)) - Double.valueOf(df.format(Float.valueOf(pddBean.getCoupon_discount()) / 100))))+ "<br/>发起拼单"));
        txt_left2.setText(Html.fromHtml("¥:" + df.format(Float.valueOf(pddBean.getMin_group_price()) / 100) + "<br/>单独购买"));
//        if (Long.valueOf(pddBean.getSold_quantity()) < 1000) {
        storeSoldNum.setText("销量:" + pddBean.getSales_tip());
//        } else {
//            storeSoldNum.setText("销量:" + df.format(Long.valueOf(pddBean.getSold_quantity()) / 10000) + "万");
//        }
        title_share_tv.setText(pddBean.getGoods_name());
        after_coupon_share_tv.setText("¥" + df.format((Double.valueOf(df.format(Float.valueOf(pddBean.getMin_normal_price()) / 100)) - Double.valueOf(df.format(Float.valueOf(pddBean.getCoupon_discount()) / 100)))));
        Glide.with(PddDetailsActivity.this).load(pddBean.getGoods_thumbnail_url()).dontAnimate().into(iv);
        //关闭上啦加载
        refreshLayout.setEnableLoadMore(false);
        isCollectRequest(pddBean.getGoodsSign() + "");


//        txtScore1.setText("卖家服务:  "+df.format(Double.valueOf(pddBean.getAvg_serv())/100));
//        txtScore2.setText("商品评价:  "+df.format(Double.valueOf(pddBean.getAvg_desc())/100));
//        txtScore3.setText("物流评分:  "+df.format(Double.valueOf(pddBean.getAvg_lgst())/100));
        //开始刷新
        refreshLayout.autoRefresh();
        String zhuaqu = SPUtils.getStringData(this, "zhuaqu", "");
        if(zhuaqu.equals("1")){
            xqzq_icon.setVisibility(View.VISIBLE);
            addpddAi();
        }

    }

    private void getMerchantInfo(String mall_id) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("mall_id", mall_id);
        HttpUtils.post(Constants.APP_IP + "/api/Pdd/getMerchantMsg",PddDetailsActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
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
                if (getComeActivity().isDestroyed())
                    return;
                try {
                    if (new JSONObject(responseString).getInt("code") != 0) {

                        //
                        //  T.showShort(PddDetailsActivity.this, new JSONObject(responseString).getString("msg"));
                        return;
                    }
                    JSONObject object = new JSONObject(responseString).getJSONObject("data");
                    txt_fuwu.setText(object.getJSONObject("merchantMsg").getString("serv_txt"));
                    txt_wuliu.setText(object.getJSONObject("merchantMsg").getString("lgst_txt"));
                    txt_miaoshu.setText(object.getJSONObject("merchantMsg").getString("desc_txt"));
                    Glide.with(PddDetailsActivity.this).load(object.getJSONObject("merchantMsg").getString("img_url")).error(R.mipmap.app_icon).dontAnimate().into(shope_img);
                    String tags = object.getJSONObject("merchantMsg").getJSONArray("goods_detail_vo_list").getJSONObject(0).getString("service_tags");
                    String[] tag = tags.replace("[", "").replace("]", "").split(",");
                    for (int i = 0; i < tag.length; i++) {
                        txt_tag.setText(txt_tag.getText().toString() + map.get(tag[i]) + " ");
                    }
//                    txtTwo.setText( object.getJSONObject("merchantMsg").getJSONArray("goods_detail_vo_list").getJSONObject(0).getString("serv_txt"));
//                    txtThree.setText( object.getJSONObject("merchantMsg").getJSONArray("goods_detail_vo_list").getJSONObject(0).getString("lgst_txt"));
//                    txtOne.setText( object.getJSONObject("merchantMsg").getJSONArray("goods_detail_vo_list").getJSONObject(0).getString("desc_txt"));
                    double to=0;
                    if("高".equals(txt_fuwu.getText().toString())){
                        double count=Math.random()+4;
                        to+=count;
                        txtTwo.setText(String.format("%.1f",count)+"");
                        progressBar2.setProgress(((int)(count*10)));
                    }else   if("中".equals(txt_fuwu.getText().toString())){
                        double count=Math.random()+3;
                        to+=count;
                        txtTwo.setText(String.format("%.1f",count)+"");
                        progressBar2.setProgress(((int)(count*10)));
                    }else   if("低".equals(txt_fuwu.getText().toString())){
                        double count=Math.random()+2;
                        to+=count;
                        txtTwo.setText(String.format("%.1f",count)+"");
                        progressBar2.setProgress(((int)(count*10)));
                    }
                    if("高".equals(txt_miaoshu.getText().toString())){
                        double count=Math.random()+4;
                        to+=count;
                        txtOne.setText(String.format("%.1f",count)+"");
                        progressBar1.setProgress(((int)(count*10)));
                    }else   if("中".equals(txt_miaoshu.getText().toString())){
                        double count=Math.random()+3;
                        to+=count;
                        txtOne.setText(String.format("%.1f",count)+"");
                        progressBar1.setProgress(((int)(count*10)));
                    }else   if("低".equals(txt_miaoshu.getText().toString())){
                        double count=Math.random()+2;
                        to+=count;
                        txtOne.setText(String.format("%.1f",count)+"");
                        progressBar1.setProgress(((int)(count*10)));
                    }
                    if("高".equals(txt_wuliu.getText().toString())){
                        double count=Math.random()+4;
                        to+=count;
                        txtThree.setText(String.format("%.1f",count)+"");
                        progressBar3.setProgress(((int)(count*10)));
                    }else   if("中".equals(txt_wuliu.getText().toString())){
                        double count=Math.random()+3;
                        to+=count;
                        txtThree.setText(String.format("%.1f",count)+"");
                        progressBar3.setProgress(((int)(count*10)));
                    }else   if("低".equals(txt_wuliu.getText().toString())){
                        double count=Math.random()+2;
                        to+=count;
                        txtThree.setText(String.format("%.1f",count)+"");
                        progressBar3.setProgress(((int)(count*10)));
                    }
                    txt_total.setText(String.format("%.1f",to/3)+"分");
                    rating.setRating(Float.valueOf(to/3+""));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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

    @OnClick({R.id.txt_finish, R.id.tv_left, R.id.xqzq_icon,R.id.txt_left, R.id.txt_left2, R.id.copy_taobao_btn, R.id.copy_friends_qq, R.id.copy_friends_cicle_zone, R.id.copy_friends_cicle_btn, R.id.copy_friends_btn, R.id.ll_right, R.id.tv_finish, R.id.view_zz,R.id.go_lingquan})
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
            case R.id.txt_left2:
            case R.id.go_lingquan:
//                Map<String,Object> map=new HashMap<>();
//                map.put("goods_id",pddBean.getGoods_id());
//                map.put("customParameters","3");
//                map.put("duo_pull_new","1");
//                map.put("duoduo_type","2");
//                map.put("pid","2642045_41895967");
//                String sign=MySecurity.getSign4(map);
//                final Intent intent = new Intent(Intent.ACTION_VIEW,
//                        Uri.parse("pinduoduo://com.xunmeng.pinduoduo/duo_coupon_landing.html?goods_id="+pddBean.getGoods_id()+"&pid=2642045_41895967&customParameters=3&duo_pull_new=1&cpsSign=CC_190523_2642045_41895967_"+sign+"&duoduo_type=2"));
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);


                /* 进行拼多多apk是否安装检测 */

                if (getPackageManager().getLaunchIntentForPackage("com.xunmeng.pinduoduo") == null){
                    LogUtils.d(TAG, "未安装拼多多APP");
                    T.showShort(PddDetailsActivity.this, "请安装拼多多APP");
                    /*
                     "we_app_icon_url": "http://xcxcdn.yangkeduo.com/pdd_logo.png",
                    "user_name": "gh_0e7477744313@app",
                    "page_path": "package_a/welfare_coupon/welfare_coupon?goods_id=175626917398&pid=13796493_181628841&goods_sign=c9r2io5g6IJKpCYxwfPY-craQjeW_J9fXL8Uim&customParameters=2&cpsSign=CC_201218_13796493_181628841_168396db2499594e72182d1fc9f8b504&duoduo_type=2&_oc_cps_sign=CC_201218_13796493_181628841_168396db2499594e72182d1fc9f8b504&_oc_pid=13796493_181628841",
                    "source_display_name": "拼多多",
                    "title": "大童少男女学生高领圆领打底衫男童女童纯棉长袖T恤2020春秋新款",
                    "app_id": "wx32540bd863b27570",
                    "desc": "拼多多，多实惠，多乐趣。"
                     */
                    WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                    req.userName = "gh_0e7477744313@app";
//                    req.openId = "wx32540bd863b27570";
                    req.path = "package_a/welfare_coupon/welfare_coupon?goods_id=175626917398&pid=13796493_181628841&goods_sign=c9f2io5g6IJKpCYxwfPY_5hLXN-N_J9fXL8Uim&customParameters=2&cpsSign=CC_201212_13796493_181628841_e8ca7a3d23293fd2321e815f9935d487&duoduo_type=2&_oc_cps_sign=CC_201212_13796493_181628841_e8ca7a3d23293fd2321e815f9935d487&_oc_pid=13796493_181628841";
                    req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;

                    CaiNiaoApplication.api.sendReq(req);

                    return;
                }

                if (pddLink.equals("")){
                    showPddShouQuan();
                    break;
                }


                /*结束 */
                LogUtils.d(TAG, "onViewClicked: "+pddLink);
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("title", "优惠券领取");
                intent.putExtra("url", pddLink);
                startActivity(intent);
                break;
            case R.id.copy_taobao_btn:
                boolean isSuccess = ImgUtils.saveImageToGallery(PddDetailsActivity.this, BitmapUtils.createViewBitmap(share_fl, PddDetailsActivity.this));
                if (isSuccess)
                    T.showShort(PddDetailsActivity.this, "保存成功");
                break;
            case R.id.copy_friends_cicle_btn:
                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mm") == null) {
                    T.showShort(PddDetailsActivity.this, "请安装微信客户端");
                    return;
                }
                Bitmap bitmap = BitmapUtils.createViewBitmap(share_fl, PddDetailsActivity.this);
                WxUtil.sharePicByBitMap(bitmap, "pyq", SendMessageToWX.Req.WXSceneTimeline, PddDetailsActivity.this);
                JAnalyticsInterface.onEvent(PddDetailsActivity.this, new CountEvent("pdd_share_goods"));
                break;
            case R.id.copy_friends_btn:
                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mm") == null) {
                    T.showShort(PddDetailsActivity.this, "请安装微信客户端");
                    return;
                }
                Bitmap bitmap1 = BitmapUtils.createViewBitmap(share_fl, PddDetailsActivity.this);
                WxUtil.sharePicByBitMap(bitmap1, "pyq", SendMessageToWX.Req.WXSceneSession, PddDetailsActivity.this);
                JAnalyticsInterface.onEvent(PddDetailsActivity.this, new CountEvent("pdd_share_goods"));
                break;
            case R.id.copy_friends_qq:
                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq") == null) {
                    T.showShort(PddDetailsActivity.this, "请安装QQ客户端");
                    return;
                }
                Bitmap temp = BitmapUtils.createViewBitmap(share_fl, PddDetailsActivity.this);
                String str = ImgUtils.saveImageToGallery2(this, temp);
                if ("".equals(str)) {
                    T.showShort(PddDetailsActivity.this, "分享失败");
                    return;
                }
                QQShareUtil.shareImgToQQ(str, this, this);
                JAnalyticsInterface.onEvent(PddDetailsActivity.this, new CountEvent("pdd_share_goods"));
                break;
            case R.id.copy_friends_cicle_zone:
                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq") == null) {
                    T.showShort(PddDetailsActivity.this, "请安装QQ客户端");
                    return;
                }
                Bitmap temp2 = BitmapUtils.createViewBitmap(share_fl, PddDetailsActivity.this);
                String str1 = ImgUtils.saveImageToGallery2(this, temp2);
                if ("".equals(str1)) {
                    T.showShort(PddDetailsActivity.this, "分享失败");
                    return;
                }
                QQShareUtil.shareImgToQQZONE(str1, this, this);
                JAnalyticsInterface.onEvent(PddDetailsActivity.this, new CountEvent("pdd_share_goods"));
                break;
            case R.id.ll_right: //收藏
                if (isCollect) { //已收藏，那么取消收藏
                    if (pddBean != null) {
                        cancelCollectRequest(pddBean.getGoodsSign());
                    }
                } else {//未收藏，那么收藏
                    if (pddBean != null) {
                        collectRequest(pddBean.getGoodsSign());
                    }
                }
                break;

        }
    }

    private void addpddAi() {

                RequestParams params1 = new RequestParams();
                params1.put("product_id_str", pddBean.getGoods_id());
                HttpUtils.post(Constants.getJianCHa, PddDetailsActivity.this,params1, new TextHttpResponseHandler() {
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
                                        String huoquaddid = SPUtils.getStringData(PddDetailsActivity.this, "huoquaddid", "");
                                        String replace = huoquaddid.replaceAll("\\[", "");
                                        String s = replace.replaceAll("]", "");
                                        RequestParams params = new RequestParams();
                                        params.put("gid", s);
                                        params.put("pid", Constants.PLATFORM_PDD);
                                        params.put("id", pddBean.getGoods_id());
                                        params.put("title", pddBean.getGoods_name());//商品标题
                                        params.put("desc", pddBean.getGoods_desc());//商品推荐描述
                                        params.put("img", pddBean.getGoods_image_url());//宣传图片
                                        params.put("price", df.format(Float.valueOf(pddBean.getMin_normal_price())/100));//商品原价
                                        params.put("org_price", df.format(Float.valueOf(pddBean.getMin_normal_price())/100));//商品拼团价格（接口对应字段price_pg）
                                        params.put("after_price", afterCouponTv.getText().toString().replace("¥", "")+"");//券后/活动/折扣后最终价格（接口对应字段price_after）
                                        params.put("commission",df.format(Float.valueOf(pddBean.getCommission() == null ? "0" : pddBean.getCommission()) / 100 * 2) );//佣金
                                        params.put("ts_time","");//商品推荐描述
                                        params.put("ticket_start_time",pddBean.getCoupon_start_time());//券开始时间
                                        params.put("ticket_end_time",pddBean.getCoupon_end_time());//券开始时间
                                        params.put("linkurl",pddBean.getMateriaurl());//推广购买链接
                                        params.put("descurl", "");//详情页面链接
                                        params.put("discount",df.format(Float.valueOf(pddBean.getCoupon_discount()) / 100) + "");
                                        params.put("sign_id",pddBean.getGoodsSign());
                                        params.put("shopname",pddBean.getMall_name());
                                        HttpUtils.post(Constants.getAddShngPin,PddDetailsActivity.this, params, new TextHttpResponseHandler() {
                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                                            }

                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject( responseString );
                                                    String msg = jsonObject.getString("msg");
                                                    //showToast("成功");
                                                    showToast(msg);
                                                    Log.d("成功成功成功", "成功成功成功");
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

                            //showToast("成功");

                            //Log.d("成功成功成功", "成功成功成功");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }



    /**
     * @属性:获取拼多多商品详情
     * @开发者:wmm
     * @时间:2018/11/22 9:00
     */
    private void getGoodsMsgRequest() {


        RequestParams requestParams = new RequestParams();
        String goodsSign = pddBean.getGoodsSign();
        String goods_id = pddBean.getGoods_id();
        requestParams.put("goods_id", goodsSign);
        //requestParams.put("goods_sign", pddBean.getGoods_id());
        //Toast.makeText(this, goodsSign+"", Toast.LENGTH_SHORT).show();
        HttpUtils.post(Constants.GET_PDD_DETAIL,PddDetailsActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
              showToast(responseString);
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
                LogUtils.d(TAG, "onSuccess: "+responseString);
                if (getComeActivity().isDestroyed())
                    return;
                try {
                    if (new JSONObject(responseString).getInt("code") != 0) {
                        T.showShort(PddDetailsActivity.this, new JSONObject(responseString).getString("msg"));
                        return;
                    }
                    JSONObject object = new JSONObject(responseString).getJSONObject("data");
                    ///tv_num.setText("升级会员等级，最高可得奖¥" + df.format(object.getDouble("commission_vip") / 100) + "。");
                    tv_num.setText("升级会员等级，最高可得奖¥" + object.getString("commission_vip") + "。");
                    JSONArray images = object.getJSONObject("goods_details").getJSONArray("goods_gallery_urls");
                    getMerchantInfo(object.getJSONObject("goods_details").getString("mall_id"));
                    List<String> listStr = new ArrayList<>();
                    if (images != null && images.length() > 0) {
                        for (int i = 0; i < images.length(); i++) {
                            listStr.add(images.getString(i));
                        }
                    } else {
                        listStr.add(object.getJSONObject("goods_details").getString("goods_thumbnail_url"));
                    }
                    homeBanner.update(listStr);
                    homeBanner.start();
                    String temp = "";
                    for (int i = 0; i < listStr.size(); i++) {
                        temp += "<img style='width:100%;height:auto' src='" + listStr.get(i) + "'/>";
                    }
                    temp = "<html>" + temp + "</html>";
//                   final String detailString="cd.jd.com/description/channel?skuId="+jdBean.getSkuId()+"&mainSkuId=11442428535&cdn=2";
                    webDetail.loadData(temp, "text/html", "utf-8");
                    JSONArray urls = object.getJSONArray("url_list");
                    if (urls.length()>0) {
                        erweima_tv.setImageBitmap(ZxingUtils.createBitmap(urls.getJSONObject(0).getString("we_app_web_view_short_url")));
                        pddLink = urls.getJSONObject(0).getString("mobile_url");
                        pddLink = pddLink.replace("duoduo_type%3D2","duoduo_type%3D3");

                    }
                    else {
                        // 需要绑定授权拼多多
                        showPddShouQuan();
                    }
//                    webDetail.loadData( object.getJSONObject("goods_details").getString("content").replaceAll("<img","<img style='width:100%;height:auto'"), "text/html", "utf-8");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * @属性:用户是否收藏商品
     * @开发者:陈飞
     * @时间:2018/7/28 21:52
     */
    private void isCollectRequest(String goods_id) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("goods_id", goods_id);
        HttpUtils.post(Constants.IS_PDD_GOOD_COLLECT,PddDetailsActivity.this, requestParams, new onOKJsonHttpResponseHandler<IsCollectBean>(new TypeToken<Response<IsCollectBean>>() {
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
                        //动态设置drawableLeft属性
                        Drawable drawable1 = getResources().getDrawable(R.mipmap.coll_red);
                        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                        llRight.setCompoundDrawables(null, drawable1, null, null);
                        llRight.setText("已收藏");
                    } else if ("N".equals(is_collect)) {//N未收藏
                        isCollect = false;
                        Drawable drawable1 = getResources().getDrawable(R.mipmap.coll_dark);
                        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                        llRight.setCompoundDrawables(null, drawable1, null, null);
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
        requestParams.put("goods_name",pddBean.getGoods_name());
        HttpUtils.post(Constants.COLLECT_PDD_GOOD,PddDetailsActivity.this, requestParams, new TextHttpResponseHandler() {
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
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    if (code == 0) {
                        isCollect = true;
                        Drawable drawable1 = getResources().getDrawable(R.mipmap.coll_red);
                        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                        llRight.setCompoundDrawables(null, drawable1, null, null);
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
        HttpUtils.post(Constants.DE_COLLECT_PDD_GOOD, PddDetailsActivity.this,requestParams, new TextHttpResponseHandler() {
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
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    if (code == 0) {
                        isCollect = false;
                        Drawable drawable1 = getResources().getDrawable(R.mipmap.coll_dark);
                        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                        llRight.setCompoundDrawables(null, drawable1, null, null);
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

    private void getDetail() {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        RequestParams requestParams = new RequestParams();
        requestParams.put("data_type", PddClient.data_type);
        requestParams.put("client_id", PddClient.client_id);
        requestParams.put("timestamp", time);
        requestParams.put("goods_id", pddBean.getGoods_id());
        requestParams.put("type", "pdd.goods.detail.get");
        Map<String, Object> temp = new HashMap<>();
        temp.put("data_type", PddClient.data_type);
        temp.put("type", "pdd.goods.commit.detail.get");
        temp.put("client_id", PddClient.client_id);
        temp.put("timestamp", time);
        temp.put("goods_id", pddBean.getGoods_id());
        String sign = PddClient.getSign(temp);
        requestParams.put("sign", sign);
        HttpUtils.post1(PddClient.serverUrl,PddDetailsActivity.this, requestParams, new TextHttpResponseHandler() {
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
                LogUtils.d("fsdfasd", responseString);
                if (responseString.contains("error_response")) {
                    return;
                }
            }
        });
    }

    private void showPddShouQuan(){
        showTipDialog3("温馨提示", "检测到您未绑定拼多多，请先授权", new onClickListener() {
            @Override
            public void onClickSure() {
                getUrl();
            }
        }, new onClickListener() {
            @Override
            public void onClickSure() {

            }
        }, "绑定", "暂不绑定");
    }

    private void getUrl() {
        RequestParams params = new RequestParams();
        HttpUtils.post(Constants.APP_IP+"/api/Pdd/getUrl",PddDetailsActivity.this, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onFinish() {
                // 隐藏进度条
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    //返回码
                    int code = jsonObject.optInt("code");
                    if (0 == code) {
                        Intent intent = new Intent(PddDetailsActivity.this, WebViewActivity.class);
                        intent.putExtra("title", "绑定拼多多");
                        intent.putExtra("url", jsonObject.getJSONObject("data").getJSONArray("url_list").getJSONObject(0).getString("mobile_url"));
                        startActivity(intent);
                    } else {
                        showToast(jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
            }
        });

    }
}
