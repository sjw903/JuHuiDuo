package com.android.jdhshop.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.RecmomendAdapterList;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.GoodsSmoke;
import com.android.jdhshop.bean.HaoDanBean;
import com.android.jdhshop.bean.IsCollectBean;
import com.android.jdhshop.bean.PddClient;
import com.android.jdhshop.bean.PromotionDetailsBean;
import com.android.jdhshop.bean.Recommendbean;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.AsyncHttpResponseHandler;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.login.WelActivity;
import com.android.jdhshop.my.RechargeActivity;
import com.android.jdhshop.utils.BitmapUtils;
import com.android.jdhshop.utils.DateUtils;
import com.android.jdhshop.utils.ImgUtils;
import com.android.jdhshop.utils.MyScrollView;
import com.android.jdhshop.utils.OkHttpUtils;
import com.android.jdhshop.utils.RoundImageView2;
import com.android.jdhshop.utils.StringUtils;
import com.android.jdhshop.utils.VerticalImageSpan;
import com.android.jdhshop.utils.WxUtil;
import com.android.jdhshop.utils.ZxingUtils;
import com.android.jdhshop.widget.CircleImageView;
import com.android.jdhshop.widget.EchartView;
import com.android.jdhshop.wmm.QQShareUtil;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.view.flowlayout.FlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.alibaba.baichuan.trade.biz.applink.adapter.AlibcFailModeType.AlibcNativeFailModeJumpDOWNLOAD;

/**
 * @属性:推广详情
 * @开发者:WMM
 * @时间:2018/11/8 13:37
 */
public class PromotionDetailsActivity extends BaseActivity implements MyScrollView.OnScrollListener, IUiListener {

    @BindView(R.id.bg_head2)
    LinearLayout headView;
    @BindView(R.id.ss)
    LinearLayout ss;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.txt_left4)
    TextView tvLeft4;
    //    @BindView(R.id.tv_title)
//    TextView tvTitle;
    @BindView(R.id.ll_right)
    TextView llRight;
    @BindView(R.id.homeBanner)
    MZBannerView homeBanner;
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
    @BindView(R.id.txt_coupon)
    TextView txt_coupon;
    @BindView(R.id.view_zz)
    View zz;
    @BindView(R.id.ll_share)
    LinearLayout llShare;
    @BindView(R.id.ll_vip)
    LinearLayout ll_vip;
    @BindView(R.id.tv_num)
    TextView tv_num;
    @BindView(R.id.rg_top)
    RadioGroup tpGroup;
    @BindView(R.id.rb_one)
    RadioButton rbOne;
    @BindView(R.id.rb_two)
    RadioButton rbTwo;
    @BindView(R.id.rb_four)
    RadioButton rbFour;
    @BindView(R.id.rb_three)
    RadioButton rbThree;
    @BindView(R.id.share_fl)
    FrameLayout share_fl;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.root_rl)
    RelativeLayout rootRl;
    @BindView(R.id.title_share_tv)
    TextView title_share_tv;
    @BindView(R.id.tv_for_share)
    TextView tv_for_share;
    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.about_recommend)
    TextView aboutComment;
    //    @BindView(R.id.txt_score)
//    TextView txtScore;
    @BindView(R.id.after_coupon_share_tv)
    TextView after_coupon_share_tv;
    @BindView(R.id.price_share_tv)
    TextView price_share_tv;
    @BindView(R.id.erweima_tv)
    ImageView erweima_tv;
    @BindView(R.id.web_detail)
    WebView webDetail;
    @BindView(R.id.store_sold_num)
    TextView storeSoldNum;
    @BindView(R.id.txt_goods_comment_num)
    TextView txtGoodsCommentNum;
    @BindView(R.id.img_rr)
    ImageView imgSmoke;
    @BindView(R.id.ll_smoke)
    LinearLayout llSMoke;
    @BindView(R.id.txt_sm_txt)
    TextView smTxt;
    @BindView(R.id.ll_flow)
    FlowLayout llFlow;
    @BindView(R.id.comment_img)
    CircleImageView commentImg;
    @BindView(R.id.comment_name)
    TextView commentName;
    @BindView(R.id.comment_content)
    TextView commentContent;
    @BindView(R.id.img_shop)
    RoundImageView2 imgShop;
    @BindView(R.id.txt_shop_title)
    TextView txtShopTitle;
    @BindView(R.id.txt_shop_comment)
    TextView txtShopComment;
    @BindView(R.id.txt_a)
    TextView txtA;
    @BindView(R.id.txt_b)
    TextView txtB;
    @BindView(R.id.txt_c)
    TextView txtC;
    @BindView(R.id.xqzq_icon)
    ImageView xqzq_icon;
    @BindView(R.id.echart_view)
    EchartView echartView;
    private Activity activity;
    //    @BindView(R.id.maidian_tv)
//    TextView maidian_tv;
    //******************************************分享出去页面end*************************************************
    private String num_iid;
    private PromotionDetailsBean data;
    private String tkl;
    private String pricetext;
    private int smoke = 0;
    //商品名称
    private String spName = "";
    //商品URL
    private String spUrl = "";
    //商品URL
    private String spLogoUrl = "";
    //淘口令
    private String spTkl = "";

    private boolean isCollect = false;//未收藏
    private ACache mAcache;
    String token = "";

    String group_id = "";
    SpannableString spannableString;
    Drawable drawable;
    private GradientDrawable gradientDrawable;
    private AlibcLogin alibcLogin;
    @BindView(R.id.about_recommend_list)
    RecyclerView aboutListView;
    private List<Recommendbean> jhsListbeans = new ArrayList<>();
    private RecmomendAdapterList adapter;
    private Gson gson = new Gson();
    @BindView(R.id.txt_time)
    TextView txtTime;
    @BindView(R.id.commis)
    LinearLayout commis;
    @BindView(R.id.commis2)
    LinearLayout commis2;
    @BindView(R.id.jgqs_title)
    TextView jgqs_title;
    private boolean isfirst = true;
    List<String> images = new ArrayList<>();
    private boolean hasvideo = false;
    private String thumbUrl = "";
    private HaoDanBean haoDanBean;
    DecimalFormat df = new DecimalFormat("0.00");
    private List<GoodsSmoke.Item> smokeItems = new ArrayList<>();
    Animation translateAnimation;

    @Override
    protected void initUI() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(getResources().getColor(R.color.black_jj));
        }
//        setStatusBar(Color.WHITE);
        setContentView(R.layout.activity_promotion_details);
        ButterKnife.bind(this);
        if ("".equals(SPUtils.getStringData(PromotionDetailsActivity.this, "token", ""))) {
            T.showShort(PromotionDetailsActivity.this, "请先登录");
            Bundle bundle = new Bundle();
            bundle.putString("num_iid", getIntent().getStringExtra("num_iid"));
            openActivity(WelActivity.class, bundle);
            finish();
            return;
        }
        translateAnimation = AnimationUtils.loadAnimation(this, R.anim.rr);
        if (getIntent().getExtras().get("bean") != null) {
            haoDanBean = (HaoDanBean) getIntent().getExtras().get("bean");
            images.add(haoDanBean.itempic);
            homeBanner.setPages(images, new MZHolderCreator<BannerViewHolder>() {
                @Override
                public BannerViewHolder createViewHolder() {
                    return new BannerViewHolder();
                }
            });
            homeBanner.start();
            //标题
            titleTv.setText("商品名称......");
            titleTv.setText(haoDanBean.itemtitle);
            //店名
            storeNameTv.setText(haoDanBean.shopname);
//            tvShare.setText("奖:" +df.format(Double.valueOf(haoDanBean.tkmoney)* Double.parseDouble(df.format((float) SPUtils.getIntData(this, "rate", 0) / 100)))+ "元");
            //暂时取消显示
            //tv_num.setText("升级会员等级，最高可得奖¥" + haoDanBean.tkmoney + "。");
            tv_num.setText("升级会员等级，最高可得奖 ...");
            tvShare.setText("奖:0.00元");

            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            pricetext = extras.getString("price");
            if (pricetext == null) {
                afterCouponTv.setText("0.00元");
            } else {
                afterCouponTv.setText(pricetext + "元");
            }

            String yuan_price = extras.getString("yuan_price");
            storeSoldNum.setText("销量:" + haoDanBean.itemsale);
            //原价
            priceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            priceTv.setText("原价:¥0.00");
            txt_coupon.setText(haoDanBean.couponmoney);
            tvLeft4.setText(haoDanBean.couponmoney + "元");
            if ("null".equals(haoDanBean.couponmoney) || "".equals(haoDanBean.couponmoney) || Double.valueOf(haoDanBean.couponmoney) <= 0) {
                commis2.setVisibility(View.VISIBLE);
                commis.setVisibility(View.GONE);
            } else {
                commis2.setVisibility(View.GONE);
                commis.setVisibility(View.VISIBLE);
            }
            try {
                txtTime.setText(DateUtils.format_yyyy_MM_dd(new Date(Long.valueOf(haoDanBean.couponstarttime) * 1000)) + " 至 " + DateUtils.format_yyyy_MM_dd(new Date(Long.valueOf(haoDanBean.couponendtime) * 1000)));
            } catch (Exception e) {

            }
            Glide.with(PromotionDetailsActivity.this).load(haoDanBean.itempic).dontAnimate().into(iv);
            tv_for_share.setText(haoDanBean.couponmoney);
            after_coupon_share_tv.setText("¥" + haoDanBean.itemendprice + "元");

            price_share_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            price_share_tv.setText("原价¥" + haoDanBean.itemprice);

        }
        homeBanner.setLayoutParams(new RelativeLayout.LayoutParams(BitmapUtils.getScreenWith(this), BitmapUtils.getScreenWith(this)));
        webDetail.setWebChromeClient(new WebChromeClient());
        webDetail.setWebViewClient(new WebViewClient());
        // Log.d(TAG, "initUI: 获取商品ID"+getIntent().getStringExtra("num_iid"));
        loadTrendEcharts(getIntent().getStringExtra("num_iid"), 0, echartView, jgqs_title);
        gradientDrawable = (GradientDrawable) tvLeft.getBackground();
        WebSettings webSettings = webDetail.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //支持屏幕缩放
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDomStorageEnabled(true);
        //不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);
        webDetail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        alibcLogin = AlibcLogin.getInstance();
        mAcache = ACache.get(this);
        activity = this;
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 2);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        aboutListView.setLayoutManager(linearLayoutManager);
        adapter = new RecmomendAdapterList(this, R.layout.main_today_highlights_child_item2, jhsListbeans);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("num_iid", jhsListbeans.get(position).getNum_iid());
                openActivity(PromotionDetailsActivity.class, bundle);
                finish();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        aboutListView.setAdapter(adapter);
        getSmoke();
    }

    private void getGoos() {
        RequestParams requestParams = new RequestParams();
        int page = (int) (Math.random() * 10 + 1);
        HttpUtils.post(Constants.ABOUT_GOODS + data.getCat_name() + "&page_no=" + page + "&pagesize=8&coupon=1", PromotionDetailsActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString + "____getGoos");
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                // Log.d(TAG, "获取看了又看: " + responseString);
                try {
                    JSONObject object = new JSONObject(responseString);
                    if (object.getInt("error") == 0) {
                        JSONArray array = object.getJSONArray("result_list");
                        Recommendbean bean;
                        jhsListbeans.clear();
                        for (int i = 0; i < array.length(); i++) {
                            bean = gson.fromJson(array.getJSONObject(i).toString(), Recommendbean.class);
                            jhsListbeans.add(bean);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
//                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void initData() {
        mAcache = ACache.get(this);
        group_id = mAcache.getAsString("group_id");
        String zhuaqu = SPUtils.getStringData(this, "zhuaqu", "");
        if (zhuaqu.equals("2")) {
            xqzq_icon.setVisibility(View.VISIBLE);
        }
        //动态设置drawableLeft属性
        Drawable drawable1 = getResources().getDrawable(R.mipmap.icon_back_while);
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
        tvLeft.setCompoundDrawables(drawable1, null, null, null);
        tvLeft.setVisibility(View.VISIBLE);
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
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                num_iid = extras.getString("num_iid");
                tkl = extras.getString("tkl");
            }
        }
        refreshLayout.setEnableLoadMore(false);

    }

    class BannerViewHolder implements MZViewHolder<String> {
        private ImageView mImageView;
        private ImageView play;
        private JCVideoPlayerStandard standard;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = view.findViewById(R.id.banner_image);
            standard = view.findViewById(R.id.videoView);
            play = view.findViewById(R.id.play);
            return view;
        }

        @Override
        public void onBind(Context context, int position, final String data) {
            // 数据绑定
            if (hasvideo && position == 0) {
                standard.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.VISIBLE);
                play.setVisibility(View.VISIBLE);
                Glide.with(PromotionDetailsActivity.this).load(images.get(1)).into(mImageView);
                standard.setUp(data, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
                Glide.with(PromotionDetailsActivity.this).load("http:" + thumbUrl).into(standard.thumbImageView);
                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        play.setVisibility(View.GONE);
                        mImageView.setVisibility(View.GONE);
                        standard.startButton.performClick();
                    }
                });
            } else {
                mImageView.setVisibility(View.VISIBLE);
                standard.setVisibility(View.GONE);
                Glide.with(PromotionDetailsActivity.this).load(data).into(mImageView);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayerStandard.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    private void getShopDetail() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.SHOP_DETAIL + "&para=" + num_iid + "&shoptype=" + ("0".equals(data.getUser_type()) ? "C" : "B"), PromotionDetailsActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString + "_____getShopDetail");
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
                    if ("0".equals(jsonObject.getString("error"))) {
                        txtGoodsCommentNum.setText("宝贝评价(" + jsonObject.getJSONObject("data").getJSONObject("comment").getString("commenttotal") + ")");
                        JSONArray obj = jsonObject.getJSONObject("data").getJSONObject("comment").getJSONArray("newcomment");
                        try {
                            if (obj != null && obj.length() > 0) {
                                Glide.with(PromotionDetailsActivity.this).load("http:" + obj.getJSONObject(0).getString("headPic")).into(commentImg);
                                commentName.setText(obj.getJSONObject(0).getString("userName"));
                                commentContent.setText(obj.getJSONObject(0).getString("content"));
                            }
                        } catch (Exception e) {
                            e.toString();
                        }
                        obj = jsonObject.getJSONObject("data").getJSONObject("comment").getJSONArray("keywords");
                        if (obj != null && obj.length() > 0) {
                            for (int i = 0; i < obj.length(); i++) {
                                LinearLayout ll = (LinearLayout) LayoutInflater.from(PromotionDetailsActivity.this).inflate(R.layout.item_text4, null);
                                TextView textView = ll.findViewById(R.id.ssd);
                                textView.setText(obj.getJSONObject(i).getString("word") + "(" + obj.getJSONObject(i).getString("count") + ")");
                                llFlow.addView(ll);
                            }
                        } else {
                            llFlow.setVisibility(View.GONE);
                        }
                        jsonObject = jsonObject.getJSONObject("data").getJSONObject("seller");
                        txtShopTitle.setText(jsonObject.getString("shopName"));
                        Glide.with(PromotionDetailsActivity.this).load("http:" + jsonObject.getString("shopIcon")).into(imgShop);
                        txtShopComment.setText("好评率:  " + jsonObject.getString("goodRatePercentage"));
                        obj = jsonObject.getJSONArray("evaluates");
                        if (obj != null && obj.length() > 0) {
                            for (int i = 0; i < obj.length(); i++) {
                                switch (i) {
                                    case 0:
                                        txtA.setText(obj.getJSONObject(i).getString("title") + "  " + obj.getJSONObject(i).getString("score") + "(" + obj.getJSONObject(i).getString("levelText") + ")");
                                        break;
                                    case 1:
                                        txtB.setText(obj.getJSONObject(i).getString("title") + "  " + obj.getJSONObject(i).getString("score") + "(" + obj.getJSONObject(i).getString("levelText") + ")");
                                        break;
                                    case 2:
                                        txtC.setText(obj.getJSONObject(i).getString("title") + "  " + obj.getJSONObject(i).getString("score") + "(" + obj.getJSONObject(i).getString("levelText") + ")");
                                        break;
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    ss.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        token = SPUtils.getStringData(this, "token", "");
        if (!"0".equals(SPUtils.getStringData(this, "coded", "0"))) {
            getToken(SPUtils.getStringData(this, "coded", "0"));
            SPUtils.saveStringData(this, "coded", "0");
            return;
        }
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null && uri.getScheme().equals("hkxuri")
                && uri.getHost().equals("hkxtb")) {
            num_iid = uri.getQueryParameter("num_iid");
            tkl = uri.getQueryParameter("tkl");
        }

        Bundle extras = intent.getExtras();
        if (extras != null) {
            num_iid = extras.getString("num_iid");
            tkl = extras.getString("tkl");
        }

        if (CaiNiaoApplication.getUserInfoBean() == null) {
            commonGetUserMsg();
        }
        // Log.d(TAG, "onResume: 获取NumIID" + num_iid);
        getGoodsMsgRequest();
    }

    @Override
    protected void initListener() {
        rbOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.scrollTo(0, 0);
                headView.getBackground().mutate().setAlpha(0);
                tpGroup.getBackground().mutate().setAlpha(0);
                rbOne.getBackground().mutate().setAlpha(0);
                rbTwo.getBackground().mutate().setAlpha(0);
                rbThree.getBackground().mutate().setAlpha(0);
                rbFour.getBackground().mutate().setAlpha(0);
                Drawable drawable1 = getResources().getDrawable(R.mipmap.icon_back_while);
                drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                tvLeft.setCompoundDrawables(drawable1, null, null, null);
                rbOne.setTextColor(Color.argb(0, 0, 0, 0));
                rbTwo.setTextColor(Color.argb(0, 0, 0, 0));
                rbThree.setTextColor(Color.argb(0, 0, 0, 0));
                rbFour.setTextColor(Color.argb(0, 0, 0, 0));
                gradientDrawable.setColor(Color.parseColor("#88000000"));
            }
        });
        rbTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        //滚动到指定位置（滚动要跳过的控件的高度的距离）
                        scrollView.scrollTo(0, webDetail.getTop() - headView.getMeasuredHeight() - 180);
                        //如果要平滑滚动，可以这样写
                        //scrollView.smoothScrollTo(0, llNeedToSkip.getMeasuredHeight());
                    }
                });
            }
        });
        rbThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        //滚动到指定位置（滚动要跳过的控件的高度的距离）
                        scrollView.scrollTo(0, ss.getTop() - headView.getMeasuredHeight() - 180);
                        //如果要平滑滚动，可以这样写
                        //scrollView.smoothScrollTo(0, llNeedToSkip.getMeasuredHeight());
                    }
                });
            }
        });
        rbFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        //滚动到指定位置（滚动要跳过的控件的高度的距离）
                        scrollView.scrollTo(0, aboutComment.getTop() - headView.getMeasuredHeight() - 180);
                        //如果要平滑滚动，可以这样写
                        //scrollView.smoothScrollTo(0, llNeedToSkip.getMeasuredHeight());
                    }
                });
            }
        });
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
            }
        });
        headView.getBackground().mutate().setAlpha(0);
        tpGroup.getBackground().mutate().setAlpha(0);
        rbOne.getBackground().mutate().setAlpha(0);
        rbTwo.getBackground().mutate().setAlpha(0);
        rbFour.getBackground().mutate().setAlpha(0);
        rbOne.setTextColor(Color.argb(0, 0, 0, 0));
        rbTwo.setTextColor(Color.argb(0, 0, 0, 0));
        rbThree.setTextColor(Color.argb(0, 0, 0, 0));
        rbFour.setTextColor(Color.argb(0, 0, 0, 0));
//        tvTitle.setTextColor(Color.argb(0, 255, 255, 255));
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
        rootRl.measure(0, 0);
        if (scrollY >= (ss.getTop() - 400) && scrollY < webDetail.getTop() - 400) {
            rbThree.setChecked(true);
        } else if (scrollY >= (webDetail.getTop() - 400) && scrollY < aboutComment.getTop() - 300) {
            rbTwo.setChecked(true);
        } else if (scrollY >= aboutComment.getTop() - 300) {
            rbFour.setChecked(true);
        } else {
            rbOne.setChecked(true);
        }
        if (scrollY < 100) {
            headView.getBackground().mutate().setAlpha(0);
            tpGroup.getBackground().mutate().setAlpha(0);
            rbOne.getBackground().mutate().setAlpha(0);
            rbTwo.getBackground().mutate().setAlpha(0);
            rbThree.getBackground().mutate().setAlpha(0);
            rbFour.getBackground().mutate().setAlpha(0);
            Drawable drawable1 = getResources().getDrawable(R.mipmap.icon_back_while);
            drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
            tvLeft.setCompoundDrawables(drawable1, null, null, null);
            rbOne.setTextColor(Color.argb(0, 0, 0, 0));
            rbTwo.setTextColor(Color.argb(0, 0, 0, 0));
            rbThree.setTextColor(Color.argb(0, 0, 0, 0));
            rbFour.setTextColor(Color.argb(0, 0, 0, 0));
            gradientDrawable.setColor(Color.parseColor("#88000000"));
        } else if (scrollY >= 100 && scrollY <= 355) {
            Drawable drawable1 = getResources().getDrawable(R.mipmap.icon_back_while);
            drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
            tvLeft.setCompoundDrawables(drawable1, null, null, null);
            if ((scrollY - 100) <= 88) {
                gradientDrawable.setColor(Color.argb((88 - scrollY + 100), 0, 0, 0));
            }
            headView.getBackground().mutate().setAlpha((scrollY - 100));
            tpGroup.getBackground().mutate().setAlpha((scrollY - 100));
            rbOne.getBackground().mutate().setAlpha((scrollY - 100));
            rbFour.getBackground().mutate().setAlpha((scrollY - 100));
            rbTwo.getBackground().mutate().setAlpha((scrollY - 100));
            rbThree.getBackground().mutate().setAlpha((scrollY - 100));
            rbOne.setTextColor(Color.argb((scrollY - 100), 0, 0, 0));
            rbTwo.setTextColor(Color.argb((scrollY - 100), 0, 0, 0));
            rbThree.setTextColor(Color.argb((scrollY - 100), 0, 0, 0));
            rbFour.setTextColor(Color.argb((scrollY - 100), 0, 0, 0));
        } else {
            headView.getBackground().mutate().setAlpha(255);
            rbOne.setTextColor(Color.argb(255, 0, 0, 0));
            rbTwo.setTextColor(Color.argb(255, 0, 0, 0));
            rbFour.setTextColor(Color.argb(255, 0, 0, 0));
            rbThree.setTextColor(Color.argb(255, 0, 0, 0));
            //动态设置drawableLeft属性
            Drawable drawable1 = getResources().getDrawable(R.mipmap.icon_back);
            drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
            tvLeft.setCompoundDrawables(drawable1, null, null, null);
            gradientDrawable.setColor(Color.parseColor("#00ffffff"));
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
        webDetail.destroy();
        webDetail = null;
        echartView.destroy();
        echartView = null;
        flag = false;
        //发送刷新收藏的消息
//        BroadcastManager.getInstance(getComeActivity()).sendBroadcast(BroadcastContants.sendRefreshCollectionMessage);
    }

    @OnClick({R.id.txt_left2, R.id.txt_left4, R.id.xqzq_icon, R.id.txt_finish, R.id.tv_left, R.id.txt_left, R.id.copy_taobao_btn, R.id.copy_friends_cicle_btn, R.id.copy_friends_qq, R.id.copy_friends_cicle_zone, R.id.copy_friends_btn, R.id.ll_right, R.id.tv_finish, R.id.view_zz})
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
                isbindReld();
                break;
            case R.id.txt_left4:
            case R.id.txt_left2:
            case R.id.txt_left:
                whetherBindingTaobao(1);
                break;
            case R.id.copy_taobao_btn://
                boolean isSuccess = ImgUtils.saveImageToGallery(PromotionDetailsActivity.this, BitmapUtils.createViewBitmap(share_fl, PromotionDetailsActivity.this));
                if (isSuccess)
                    T.showShort(PromotionDetailsActivity.this, "保存成功");
                break;
            case R.id.copy_friends_cicle_btn:
                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mm") == null) {
                    T.showShort(PromotionDetailsActivity.this, "请安装微信客户端");
                    return;
                }
                Bitmap bitmap = BitmapUtils.createViewBitmap(share_fl, PromotionDetailsActivity.this);
                WxUtil.sharePicByBitMap(bitmap, "pyq", SendMessageToWX.Req.WXSceneTimeline, PromotionDetailsActivity.this);
                break;
            case R.id.copy_friends_btn:
                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mm") == null) {
                    T.showShort(PromotionDetailsActivity.this, "请安装微信客户端");
                    return;
                }
                Bitmap bitmap1 = BitmapUtils.createViewBitmap(share_fl, PromotionDetailsActivity.this);
                WxUtil.sharePicByBitMap(bitmap1, "pyq", SendMessageToWX.Req.WXSceneSession, PromotionDetailsActivity.this);
                break;
            case R.id.copy_friends_qq:
                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq") == null) {
                    T.showShort(PromotionDetailsActivity.this, "请安装QQ客户端");
                    return;
                }
                Bitmap temp = BitmapUtils.createViewBitmap(share_fl, PromotionDetailsActivity.this);
                String str = ImgUtils.saveImageToGallery2(this, temp);
                if ("".equals(str)) {
                    T.showShort(PromotionDetailsActivity.this, "分享失败");
                    return;
                }
                QQShareUtil.shareImgToQQ(str, this, this);
                break;
            case R.id.copy_friends_cicle_zone:
                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq") == null) {
                    T.showShort(PromotionDetailsActivity.this, "请安装QQ客户端");
                    return;
                }
                Bitmap temp2 = BitmapUtils.createViewBitmap(share_fl, PromotionDetailsActivity.this);
                String str1 = ImgUtils.saveImageToGallery2(this, temp2);
                if ("".equals(str1)) {
                    T.showShort(PromotionDetailsActivity.this, "分享失败");
                    return;
                }
                QQShareUtil.shareImgToQQZONE(str1, this, this);
                break;
            case R.id.ll_right: //收藏
                if (isCollect) { //已收藏，那么取消收藏
                    if (data != null) {
                        cancelCollectRequest(data.getNum_iid());
                    }
                } else {//未收藏，那么收藏
                    if (data != null) {
                        collectRequest(data.getNum_iid());
                    }
                }
                break;
            case R.id.xqzq_icon:
                addAi();

                break;
        }
    }

    private void addAi() {
        RequestParams params1 = new RequestParams();
        params1.put("product_id_str", data.getNum_iid());
        HttpUtils.post(Constants.getJianCHa, PromotionDetailsActivity.this, params1, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    String string = jsonObject.getString("list");
                    if (string.equals("[]")) {
                        String huoquaddid = SPUtils.getStringData(PromotionDetailsActivity.this, "huoquaddid", "");
                        String replace = huoquaddid.replaceAll("\\[", "");
                        String s = replace.replaceAll("]", "");
                        RequestParams params = new RequestParams();
                        params.put("gid", s);
                        params.put("pid", Constants.PLATFORM_TB);
                        params.put("id", data.getNum_iid());
                        params.put("title", titleTv.getText().toString());//商品标题
                        params.put("desc", "");//商品推荐描述
                        params.put("img", spLogoUrl);//宣传图片
                        params.put("price", data.getZk_final_price());//商品原价
                        params.put("org_price", "");//商品拼团价格（接口对应字段price_pg）
                        params.put("after_price", pricetext);//券后/活动/折扣后最终价格（接口对应字段price_after）
                        params.put("commission", data.getCommission());//佣金
                        params.put("ts_time", "");//商品推荐描述
                        params.put("ticket_start_time", data.getCoupon_start_time());//券开始时间
                        params.put("ticket_end_time", data.getCoupon_end_time());//券开始时间
                        params.put("linkurl", "");//推广购买链接
                        params.put("descurl", "");//详情页面链接coupon_amount
                        params.put("discount", data.getCoupon_amount() + "");
                        params.put("shopname", data.getNick());
                        HttpUtils.post(Constants.getAddShngPin, PromotionDetailsActivity.this, params, new TextHttpResponseHandler() {
                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                try {
                                    JSONObject jsonObject = new JSONObject(responseString);
                                    String msg = jsonObject.getString("msg");
                                    showToast(msg);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        showToast("已有该商品");
                    }
                    //showToast("添加成功");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void isbindReld() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.postUpload(Constants.ISBIND_RELATION_ID, PromotionDetailsActivity.this, requestParams, new TextHttpResponseHandler() {
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lqhandle.sendEmptyMessage(2);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                unbinTb();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void unbinTb() {
        RequestParams params = new RequestParams();
        params.put("token", token);
        // get方式
        HttpUtils.post(Constants.UNBING_TAOBAO, PromotionDetailsActivity.this, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                // 显示进度条
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                String responseResult = new String(arg2);
                try {
                    JSONObject jsonObject = new JSONObject(responseResult);
                    String code = jsonObject.optString("code");
                    String msg = jsonObject.optString("msg");
                    // String result = jsonObject.optString("result");
                    if ("0".equalsIgnoreCase(code)) { // 操作成功
                        alibcLogin.logout(new AlibcLoginCallback() {
                            @Override
                            public void onSuccess(int i, String s, String s1) {
                                alibcLoginLogin();
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        alibcLogin.showLogin(new AlibcLoginCallback() {
//                                            @Override
//                                            public void onSuccess(int i, String s, String s1) {
//                                                s = alibcLogin.getSession().userid;
//                                                if (s == null) {
//                                                    return;
//                                                }
//                                                int length = s.length();
//                                                if (length > 6) {
//                                                    String b = s.substring(length - 6, length);
//                                                    String[] bArr = b.split("");
//                                                    String c = bArr[1] + "" + bArr[2] + "" + bArr[5] + "" + bArr[6] + "" + bArr[3] + "" + bArr[4];
//                                                    bindingTaobao(c);
////                                    int i=0;
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onFailure(int code, String msg) {
//                                                runOnUiThread(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        Toast.makeText(PromotionDetailsActivity.this, msg,
//                                                                Toast.LENGTH_LONG).show();
//                                                    }
//                                                });
//                                            }
//                                        });
//                                    }
//                                });
                            }

                            @Override
                            public void onFailure(int i, String s) {

                            }
                        });
                    } else {
                        showToast(msg);
                    }
                } catch (JSONException e) {
                }
            }

            @Override
            public void onFinish() {
                // 隐藏进度条
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
            }
        });
    }

    //是否绑定淘宝账号
    private void whetherBindingTaobao(final int type) {
        showLoadingDialog();
        String url = Constants.WHETHER_BINDING_TAOBAO;
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder().add("token", SPUtils.getStringData(this, "token", "")).build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                closeLoadingDialog();
                String result = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    LogUtils.d(TAG, "onResponse: " + code);
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    String isBinding = jsonData.optString("is_binding");
                    if ("N".equals(isBinding)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showTipDialog2("领券提示", Html.fromHtml("领券需要您绑定淘宝号，一个" + getString(R.string.app_name) + "账号只可绑定一个淘宝账号，通过绑定的淘宝账号购物可得到返利，其他淘宝账号无法获得返利"), new onClickListener() {
                                    @Override
                                    public void onClickSure() {

                                        //如果未绑定
                                        alibcLoginLogin();
//                                        alibcLogin.showLogin(new AlibcLoginCallback() {
//                                            @Override
//                                            public void onSuccess(int i, String s, String s1) {
//                                                s = alibcLogin.getSession().userid;
//                                                if (s == null) {
//                                                    return;
//                                                }
//                                                int length = s.length();
//                                                if (length > 6) {
//                                                    String b = s.substring(length - 6, length);
//                                                    String[] bArr = b.split("");
//                                                    String c = bArr[1] + "" + bArr[2] + "" + bArr[5] + "" + bArr[6] + "" + bArr[3] + "" + bArr[4];
//                                                    bindingTaobao(c);
////                                    int i=0;
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onFailure(int code, String msg) {
//                                                runOnUiThread(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        Toast.makeText(PromotionDetailsActivity.this, msg,
//                                                                Toast.LENGTH_LONG).show();
//                                                    }
//                                                });
//                                            }
//                                        });
                                    }
                                }, "去绑定");
                            }
                        });
                    } else {
                        lqhandle.sendEmptyMessage(type);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void alibcLoginLogin() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (alibcLogin.isLogin()) {
                    // Log.d(TAG, "run: 已经登陆了");
                    alibcLogin.logout(new AlibcLoginCallback() {
                        @Override
                        public void onSuccess(int i, String s, String s1) {
                            runOnUiThread(() -> {
                                alibcLogin.showLogin(new AlibcLoginCallback() {
                                    @Override
                                    public void onSuccess(int i, String s, String s1) {
                                        s = alibcLogin.getSession().userid;
                                        if (s == null) {
                                            return;
                                        }
                                        int length = s.length();
                                        if (length > 6) {
                                            String b = s.substring(length - 6, length);
                                            String[] bArr = b.split("");
                                            String c = bArr[1] + "" + bArr[2] + "" + bArr[5] + "" + bArr[6] + "" + bArr[3] + "" + bArr[4];
                                            bindingTaobao(c);
//                                    int i=0;
                                        }
                                    }

                                    @Override
                                    public void onFailure(int code, String msg) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(PromotionDetailsActivity.this, msg,
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });
                            });
                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });
                } else {
                    alibcLogin.showLogin(new AlibcLoginCallback() {
                        @Override
                        public void onSuccess(int i, String s, String s1) {
                            //Toast.makeText(PromotionDetailsActivity.this, "1"+"", Toast.LENGTH_SHORT).show();
                            s = alibcLogin.getSession().userid;

                            if (s == null) {
                                return;
                            }
                            int length = s.length();
                            if (length > 6) {
                                String b = s.substring(length - 6, length);
                                String[] bArr = b.split("");
                                String c = bArr[1] + "" + bArr[2] + "" + bArr[5] + "" + bArr[6] + "" + bArr[3] + "" + bArr[4];
                                bindingTaobao(c);
//                                    int i=0;
                            }
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(PromotionDetailsActivity.this, "2"+"", Toast.LENGTH_SHORT).show();
                                    //吐司   对象为空导致失败
                                    Toast.makeText(PromotionDetailsActivity.this, msg,
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }

            }
        });
    }

    private Handler lqhandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 3) {
                showTipDialog3("淘宝账号重复绑定", (String) msg.obj, null, null, "知道了", null);
//                Toast.makeText(PromotionDetailsActivity.this, (String) msg.obj,
//                        Toast.LENGTH_LONG).show();
                return;
            }
            if (msg.what == 2) {
                share();
                return;
            }

            if (data == null) {
                ToastUtils.showShortToast(PromotionDetailsActivity.this, "获取领券链接失败");
                return;
            }

            AlibcBasePage page = new AlibcDetailPage(data.getNum_iid());
            AlibcShowParams showParams = new AlibcShowParams();
            showParams.setOpenType(OpenType.Auto);
            showParams.setClientType("taobao");
            showParams.setBackUrl("alisdk://");
            showParams.setNativeOpenFailedMode(AlibcNativeFailModeJumpDOWNLOAD);

            AlibcTaokeParams taokeParams = new AlibcTaokeParams(SPUtils.getStringData(getApplicationContext(), "tbk_pid", ""), "", "");
            taokeParams.setPid(SPUtils.getStringData(PromotionDetailsActivity.this, "tbk_pid", ""));
            taokeParams.setAdzoneid(SPUtils.getStringData(PromotionDetailsActivity.this, "tbk_aid", ""));
//adzoneid是需要taokeAppkey参数才可以转链成功&店铺页面需要卖家id（sellerId），具体设置方式如下：
//            taokeParams.extraParams.put("taokeAppkey","31907828");
//            taokeParams.extraParams.put("sellerId", "31907828");
//自定义参数
            Map<String, String> trackParams = new HashMap<>();
//            AlibcTrade.openByBizCode(PromotionDetailsActivity.this, page, null, new WebViewClient(),
//                    new WebChromeClient(), "detail", showParams, taokeParams,
//                    trackParams, new AlibcTradeCallback() {
//                        @Override
//                        public void onTradeSuccess(AlibcTradeResult tradeResult) {
//                            // 交易成功回调（其他情形不回调）
//                            AlibcLogger.i(TAG, "open detail page success");
//                            return;
//                        }
//
//                        @Override
//                        public void onFailure(int code, String msg) {
//                            // 失败回调信息
//                            AlibcLogger.e(TAG, "code=" + code + ", msg=" + msg);
//                            if (code == -1) {
//                                Toast.makeText(PromotionDetailsActivity.this, "唤端失败，失败模式为none",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });

            // Log.d(TAG, "handleMessage: 这里跳淘宝领券么？");
            Map<String, String> exParams = new HashMap<>();
            AlibcTrade.openByUrl(PromotionDetailsActivity.this, "", data.getCoupon_click_url(), null, new WebViewClient(), new WebChromeClient(),
                    //new AlibcShowParams(OpenType.Native)
                    showParams
                    , new AlibcTaokeParams(SPUtils.getStringData(getApplicationContext(), "tbk_pid", ""), "", ""), exParams, new AlibcTradeCallback() {
                        @Override
                        public void onTradeSuccess(AlibcTradeResult alibcTradeResult) {

                            // Log.d(TAG, "onTradeSuccess: " + alibcTradeResult.resultType.name() + "_______" + alibcTradeResult.payResult);
                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });
        }
    };

    private void share() {
        if (data == null) {
            return;
        }
        Bundle bundle = new Bundle();
        if (data != null) {
            PromotionDetailsBean.SmallImagesBean small_images = data.getSmall_images();
            ArrayList<String> images = new ArrayList<>();
            if (small_images != null) {
                Object small_images1 = small_images.getSmall_images();
                if (small_images1 != null) {
                    if (small_images1 instanceof List) {
                        images = (ArrayList<String>) small_images1;
                    } else if (small_images1 instanceof String) {
                        images.add((String) small_images1);
                    }
                }
            } else {
                images.add(data.getPict_url());
            }
            bundle.putStringArrayList("urls", images);
        }
        Intent intent = new Intent(this, NewShareActivity.class);
        intent.putExtra("shouyi", data.getCommission());
        intent.putExtra("name", titleTv.getText().toString());
        intent.putExtra("price", priceTv.getText().toString().replace("原价:¥", ""));
        intent.putExtra("after_price", afterCouponTv.getText().toString().replace("元", ""));
        intent.putExtra("kouling", spTkl);
        intent.putExtra("link", "");
        intent.putExtra("bitmap", bundle);
        Bitmap bit = BitmapUtils.createViewBitmap(share_fl, PromotionDetailsActivity.this);
        String url = System.currentTimeMillis() + ".jpg";
        boolean is = ImgUtils.saveImageToGallery2(this, bit, url);
        if (is) {
            intent.putExtra("imgurl", url);
        } else {
            return;
        }
        startActivity(intent);
    }

    //绑定淘宝账号
    private void bindingTaobao(String tb_uid) {
        String url = Constants.BINDING_TAOBAO;
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder().add("token", SPUtils.getStringData(this, "token", "")).add("tb_uid", tb_uid).build();
        Request request = new Request.Builder().url(url).post(body).build();
        // Log.d(TAG, "bindingTaobao: " + SPUtils.getStringData(this, "tbk_pid", "") + "pid" +  SPUtils.getStringData(this, "tbk_rid", "") + "rid" + SPUtils.getStringData(this, "tbk_aid", "") + "aid");

        // Log.d(TAG, "bindingTaobao: token=" + SPUtils.getStringData(this, "token", "") + "&tb_uid=" + tb_uid);
        // Log.d(TAG, "bindingTaobao: " + url);
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String result = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if ("0".equals(code)) {
                        // Log.d(TAG, "onResponse: 绑定淘宝ID成功");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Log.d(TAG, "run: 跳tipBind");
                                //tipBind();
                                Toast.makeText(PromotionDetailsActivity.this, "绑定淘宝账号成功",
                                        Toast.LENGTH_LONG).show();
                                lqhandle.sendEmptyMessage(1);
                            }
                        });
                    } else {
                        Message send_message = new Message();
                        send_message.what = 3;
                        send_message.obj = jsonObject.getString("msg");
                        lqhandle.sendMessage(send_message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void tipBind() {
        // Log.d(TAG, "tipBind: 开WebViewActivity4");
        openActivity(WebViewActivity4.class);
//        Intent intent = new Intent(this, WebViewActivity.class);
//        intent.putExtra("title", "绑定淘宝分享渠道");
//        intent.putExtra("url", "https://oauth.taobao.com/authorize?response_type=code&client_id="+Constants.qd_app_key+"&redirect_uri=http://127.0.0.1:12345/error&state=1212&view=wap");
//        startActivity(intent);
    }

    private void getSmoke() {
        HttpUtils.post(Constants.GOODS_SMOKE, PromotionDetailsActivity.this, new RequestParams(), new onOKJsonHttpResponseHandler<GoodsSmoke>(new TypeToken<Response<GoodsSmoke>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString + "__getSmoke");
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
            public void onSuccess(int statusCode, Response<GoodsSmoke> datas) {
                if (datas.isSuccess()) {
                    smokeItems.addAll(datas.getData().list);
                    if (smokeItems.size() > 0)
                        smokeHandle.sendEmptyMessageDelayed(0, 2000);
                } else {
                    showToast(datas.getMsg() + "__getSmoke success");
                }
            }
        });
    }

    private boolean flag = true;
    private Handler smokeHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            llSMoke.setVisibility(View.VISIBLE);
            if (smoke == (smokeItems.size() - 1)) {
                smoke = 0;
            }
            if (flag) {
                Glide.with(PromotionDetailsActivity.this).load(smokeItems.get(smoke).avatar).error(R.mipmap.app_icon).into(imgSmoke);
                smTxt.setText("用户" + smokeItems.get(smoke).phone + smokeItems.get(smoke).action);
                smoke++;
                smTxt.startAnimation(translateAnimation);
                imgSmoke.startAnimation(translateAnimation);
                smokeHandle.sendEmptyMessageDelayed(0, 3000);
            }
        }
    };

    /**
     * @属性:获取淘宝客商品详情
     * @开发者:陈飞
     * @时间:2018/7/27 14:26
     */
    private void getGoodsMsgRequest() {
        String url = "";
        RequestParams requestParams = new RequestParams();
//        if (!TextUtils.isEmpty(num_iid)) {
        url = Constants.HOME_TBK_GETGOODSMSG_URL;
        requestParams.put("num_iid", num_iid);
//        } else {
//            //如果是用券进来的
//            if (TextUtils.isEmpty(tkl)) { //商品详情
//                url = Constants.HOME_TBK_GETGOODSMSG_URL;
//                if (token != null && (!"".equals(token))) {
//                    requestParams.put("token", token);
//                }
//            } else {//根据商品链接/淘口令查询淘宝商品信息
//                url = Constants.HOME_TBK_SEARCHTKL_URL;
//                requestParams.put("tkl", tkl);
//            }
//        }
        HttpUtils.post(url, PromotionDetailsActivity.this, requestParams, new onOKJsonHttpResponseHandler<PromotionDetailsBean>(new TypeToken<Response<PromotionDetailsBean>>() {
        }) {
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
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Response<PromotionDetailsBean> datas) {
                if (getComeActivity().isDestroyed())
                    return;
                if (datas.isSuccess()) {
                    data = datas.getData();
                    if (data != null) {
                        //轮播图s
                        images.clear();
                        PromotionDetailsBean.SmallImagesBean small_images = data.getSmall_images();
                        if (small_images != null) {
                            Object small_images1 = small_images.getSmall_images();
                            if (small_images1 != null) {
                                if (small_images1 instanceof List) {
                                    images.addAll((List<String>) small_images1);
                                } else if (small_images1 instanceof String) {
                                    images.add((String) small_images1);
                                }
                            }
                        } else {
                            images.add(data.getPict_url());
                        }
                        if ("1".equals(getIntent().getExtras().getString("tye"))) {
                            hasvideo = true;
                            thumbUrl = "";
                            images.add(0, "http://cloud.video.taobao.com/play/u/1/p/1/e/6/t/1/" + getIntent().getExtras().getString("url") + ".mp4");
                        }
                        homeBanner.setPages(images, new MZHolderCreator<BannerViewHolder>() {
                            @Override
                            public BannerViewHolder createViewHolder() {
                                return new BannerViewHolder();
                            }
                        });
                        homeBanner.start();
                        //标题
                        titleTv.setText(data.getTitle());
                        if (isfirst) {
                            getGoos();
                            getShopDetail();
                            isfirst = false;
                        }
                        //店名
                        storeNameTv.setText(data.getNick());
//                        //分享可赚
//                        double money = Double.parseDouble(data.getCommission()) * 0.5;
//                        if (null != group_id && "2".equals(group_id)) {//会员*0.8 非会员0.5
//                            money = Double.parseDouble(data.getCommission()) * 0.8;
//                        }
                        Intent intent = getIntent();
                        Bundle extras = intent.getExtras();

                        String comm = extras.getString("comm");

                        tvShare.setText("奖:" + comm + "元");
                        String tvsgare = tvShare.getText().toString();
                        if (tvsgare.equals("奖:null元")) {
                            tvShare.setText("奖:" + data.getCommission() + "元");
                        }
                        if (!TextUtils.isEmpty(data.getCommission())) {
//                            double moneyVip = Double.parseDouble(data.getCommission()) * 0.8;
                            //tv_num.setText("升级会员等级，最高可得奖¥" + data.getCommission_vip() + "。");
                            tv_num.setText("升级会员等级，最高可得奖¥" + data.getCommission_vip() + "。");
                        }

                        //优惠后
                        // 如果新取到的数据中的优惠券信息为空则不更新以下信息
                        if ("null".equals(data.getCoupon_amount()) || "".equals(data.getCoupon_amount()) || Double.valueOf(data.getCoupon_amount()) <= 0) {
                            // 券后价
                            // 优惠券金额
                            // 优惠券有效起止期
                            // 底部领券 xx 元 按钮

                            //原价
                            priceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                            priceTv.setText("原价:¥" + data.getZk_final_price());

                            // 从其它地方进来的，需要按原逻辑进行界面处理
                            if (getIntent().getExtras().get("bean") == null) {
                                if ("null".equals(data.getCoupon_amount()) || "".equals(data.getCoupon_amount()) || Double.valueOf(data.getCoupon_amount()) <= 0) {
                                    commis2.setVisibility(View.VISIBLE);
                                    commis.setVisibility(View.GONE);
                                } else {
                                    commis2.setVisibility(View.GONE);
                                    commis.setVisibility(View.VISIBLE);
                                }
                                // 原价
                                priceTv.setText(
                                        ("".equals(data.getReserve_price()) ? data.getZk_final_price() : data.getReserve_price()) + "元"
                                );
                                //券后价
                                afterCouponTv.setText("0.00元");
                                afterCouponTv.setText(
                                        ("0".equals(data.getCoupon_amount()) ? data.getZk_final_price() : (StringUtils.doStringToDouble(data.getZk_final_price()) - StringUtils.doStringToDouble(data.getCoupon_amount()))) + "元"
                                );
                                tvLeft4.setCompoundDrawables(null, null, null, null);
                                tvLeft4.setText("立即购买");
                            }


                        } else {
                            // 分享的券后价
                            try {
                                after_coupon_share_tv.setText("¥" + (String.format("%.2f", StringUtils.doStringToDouble(data.getZk_final_price()) - StringUtils.doStringToDouble(data.getCoupon_amount()))) + "元");
                            } catch (NumberFormatException e) {
                                after_coupon_share_tv.setText("¥" + (String.format("%.2f", StringUtils.doStringToDouble(data.getZk_final_price()))) + "元");
                            }
                            // 券后价
                            try {
                                afterCouponTv.setText((String.format("%.2f", StringUtils.doStringToDouble(data.getZk_final_price()) - StringUtils.doStringToDouble(data.getCoupon_amount()))) + "元");
                            } catch (NumberFormatException e) {
                                afterCouponTv.setText((String.format("%.2f", StringUtils.doStringToDouble(data.getZk_final_price()))) + "元");
                            }
                            // 优惠券值
                            txt_coupon.setText(data.getCoupon_amount());
                            // 底部领券x元按钮
                            tvLeft4.setText(data.getCoupon_amount() + "元");
                            //优惠券有效起止期
                            txtTime.setText(data.getCoupon_start_time() + " 至 " + data.getCoupon_end_time());
                            //原价
                            priceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                            priceTv.setText("原价:¥" + data.getZk_final_price());
                        }
//                        storeSoldNum.setText("销量:" + data.getVolume());
                        txtAddress.setText(data.getProvcity());
//                        txtScore.setText(data.getIs_prepay());

                        // 第二次获取商品信息无优惠券信息时
//                        if ("null".equals(data.getCoupon_amount()) || "".equals(data.getCoupon_amount()) || Double.valueOf(data.getCoupon_amount()) <= 0) {
//                            commis2.setVisibility(View.VISIBLE);
//                            commis.setVisibility(View.GONE);
//                        } else {
//                            commis2.setVisibility(View.GONE);
//                            commis.setVisibility(View.VISIBLE);
//                        }
                        spName = data.getTitle();
                        if (data.getCoupon_click_url_r() == null || "".equals(data.getCoupon_click_url_r())) {
                            spUrl = data.getCoupon_click_url();
                        } else {
                            spUrl = data.getCoupon_click_url_r();
                        }
                        spLogoUrl = data.getPict_url();
                        Glide.with(PromotionDetailsActivity.this).load(data.getPict_url()).dontAnimate().into(iv);
                        spannableString = new SpannableString("   " + data.getTitle());
                        if (data.getUser_type().equals("1")) { //是天猫
                            drawable = getResources().getDrawable(R.mipmap.label_tm);
                        } else {//不是天猫
                            drawable = getResources().getDrawable(R.mipmap.label_tb);
                        }
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        spannableString.setSpan(new VerticalImageSpan(drawable), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        title_share_tv.setText(spannableString);
                        tv_for_share.setText(data.getCoupon_amount());
//                        after_coupon_share_tv.setText("¥" + String.format("%.2f", StringUtils.doStringToDouble(data.getZk_final_price()) - StringUtils.doStringToDouble(data.getCoupon_amount())));

                        price_share_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        price_share_tv.setText("原价¥" + data.getZk_final_price());
                        if ("Y".equals(CaiNiaoApplication.getUserInfoBean().user_msg.is_share_vip)) {
                            erweima_tv.setImageBitmap(ZxingUtils.createBitmap(SPUtils.getStringData(getComeActivity(), "share_url_vip", "") + "/wap/Index/share/num_iid/" + data.getNum_iid() + "/uid/" + SPUtils.getStringData(PromotionDetailsActivity.this, Constants.UID, "")));
                        } else {
                            erweima_tv.setImageBitmap(ZxingUtils.createBitmap(Constants.SHARE_URL + "/wap/Index/share/num_iid/" + data.getNum_iid() + "/uid/" + SPUtils.getStringData(PromotionDetailsActivity.this, Constants.UID, "")));
                        }
                        //判断是否收藏
                        // Log.d(TAG, "请求 data.getNum_iid(): " + data.getNum_iid());
                        // Log.d(TAG, "请求是否收藏开始; 参数 ：");
                        isCollectRequest(data.getNum_iid());
                        // Log.d(TAG, "请求是否收藏结束; ");
                        //获取淘口令
                        getTkl();

                        getTbDescHandler.sendEmptyMessage(1000);
                    }
                } else {
                    showToast(datas.getMsg());
                }
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private final Handler getTbDescHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case 1000:
                    // 取第一个详情
                    getTbDesc(data.getContent_url());
                    break;
                case 2000:
                    // 取第二个详情
                    // LogUtils.d(TAG, "handleMessage: " + msg.obj.toString());
                    if (!"null".equals(msg.obj)) {
                        getTbRealDesc((String) msg.obj);
                    }
                    break;
            }
        }
    };

    /**
     * @属性：获取淘宝详情
     */
    private void getTbDesc(String desc_url) {
        Runnable desc_run = new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.getInstance().get(data.getContent_url(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        showToastLong(call.request().url() + "_" + e.getMessage());

                    }

                    @Override
                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                        // Log.d(TAG, "onResponse: " + call.request().url());
                        String responseString = response.body().string();
                        // Log.d(TAG, "onResponse: " + responseString);
                        String real_desc_url = org.apache.commons.lang3.StringUtils.substringBetween(responseString, "\"descUrl\":\"", "\"");
                        if (!"null".equals(real_desc_url)) {
                            LogUtils.d(TAG, "onResponse: " + real_desc_url);
                            Message message = new Message();
                            message.what = 2000;
                            message.obj = real_desc_url;
                            getTbDescHandler.sendMessage(message);
                        }
                    }
                });
            }
        };
        Thread get_desc_thread = new Thread(desc_run);
        get_desc_thread.start();

//                HttpUtils.get(data.getContent_url(), new AsyncHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                        String responseString = new String(responseBody);
//                        String real_desc_url = org.apache.commons.lang3.StringUtils.substringBetween(responseString, "\"descUrl\":\"", "\"");
//                        // Log.d(TAG, "onSuccess: " + real_desc_url);
//                        HttpUtils.get("https:" + real_desc_url, new TextHttpResponseHandler() {
//                            @Override
//                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//
//                            }
//
//                            @Override
//                            public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                                String real_html = org.apache.commons.lang3.StringUtils.substringBetween(responseString, "var desc='", "';");
//                                //webDetail.loadUrl("https://"+real_desc_url);//data.getContent_url());
//                                real_html = real_html + "<style>p,p img,div{ width:100% !important; max-width:100%  !important;}</style>";
//                                webDetail.loadData(real_html, "text/html", "");
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//
//                    }
//                });
    }

    private void getTbRealDesc(String desc_url) {
        Runnable desc_run = new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.getInstance().get("https:" + desc_url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                        String second_response = response.body().string();
                        String real_html = org.apache.commons.lang3.StringUtils.substringBetween(second_response, "var desc='", "';");
                        //webDetail.loadUrl("https://"+real_desc_url);//data.getContent_url());
                        real_html = real_html + "<style>p,img,p img,div{ width:100% !important; max-width:100%  !important;}.desc_anchor{ height: 0px;  }</style>";
                        String finalReal_html = real_html;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (webDetail != null) {
                                    webDetail.loadData(finalReal_html, "text/html", "");
                                }
                            }
                        });

                    }
                });
            }
        };
        Thread desc_thread = new Thread(desc_run);
        desc_thread.start();
    }

    /**
     * @属性:获取淘口令
     * @开发者:陈飞
     * @时间:2018/7/28 22:04
     */
    private void getTkl() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("text", spName);
        requestParams.put("url", spUrl);
        requestParams.put("logo", spLogoUrl);
        HttpUtils.post(Constants.CREATE_TP_WD, PromotionDetailsActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString + "+++++++++++++++");
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
                // Log.d(TAG, "/api/Tbk/createTpwd: " + responseString);
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    if (code == 0) {
                        String data = jsonObject.optString("data");
                        spTkl = data;
                    } else {

//                        showToast(msg);
                    }
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
        // Log.d(TAG, "isCollectRequest请求方法内参数 : " + goods_id);
        HttpUtils.post(Constants.MESSAGE_GOODSCOLLECT_IS_COLLECT_URL, PromotionDetailsActivity.this, requestParams, new onOKJsonHttpResponseHandler<IsCollectBean>(new TypeToken<Response<IsCollectBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString + "_isCollectRequest");
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
                    showToast(datas.getMsg() + "_isCollectRequest_success");
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
        HttpUtils.post(Constants.MESSAGE_GOODSCOLLECT_COLLECT_URL, PromotionDetailsActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString + "___collectRequest");
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
                        showToast(msg + "___collectRequest_success");
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
        HttpUtils.post(Constants.MESSAGE_GOODSCOLLECT_CANCELCOLLECT_URL, PromotionDetailsActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString + "__cancelCollectRequest");
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
                        showToast(msg + "__cancelCollectRequest_success");
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
        Log.d("dfsdf", uiError.errorMessage);
    }

    @Override
    public void onCancel() {

    }

    private void getToken(String code) {
        String time = new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(new Date());
        RequestParams requestParams = new RequestParams();
        requestParams.put("method", "taobao.top.auth.token.create");
        requestParams.put("app_key", Constants.qd_app_key);
        requestParams.put("format", "json");
        requestParams.put("code", code);
        requestParams.put("timestamp", time);
        requestParams.put("v", "2.0");
        requestParams.put("sign_method", "md5");
        Map<String, String> temp = new HashMap<>();
        temp.put("method", "taobao.top.auth.token.create");
        temp.put("app_key", Constants.qd_app_key);
        temp.put("format", "json");
        temp.put("code", code);
        temp.put("timestamp", time);
        temp.put("v", "2.0");
        temp.put("sign_method", "md5");
        String sign = null;
        sign = PddClient.getSign3(temp, Constants.qd_app_secret);
        requestParams.put("sign", sign);
        HttpUtils.post1("https://eco.taobao.com/router/rest", PromotionDetailsActivity.this, requestParams, new TextHttpResponseHandler() {
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
                String temp = responseString.replaceAll("\\\\", "");
                temp = temp.replace("\"{", "{").replace("}\"", "}");
                try {
                    JSONObject object = new JSONObject(temp);
                    object = object.getJSONObject("top_auth_token_create_response");
                    temp = object.getString("token_result");
                    object = new JSONObject(temp);
                    getTemp(object.getString("access_token"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getTemp(String key) {
        String time = new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(new Date());
        RequestParams requestParams = new RequestParams();
        requestParams.put("method", "taobao.tbk.sc.publisher.info.save");
        requestParams.put("app_key", Constants.qd_app_key);
        requestParams.put("format", "json");
        requestParams.put("session", key);
        requestParams.put("inviter_code", Constants.qd_app_code);
        requestParams.put("timestamp", time);
        requestParams.put("info_type", "1");
        requestParams.put("v", "2.0");
        requestParams.put("sign_method", "md5");
        Map<String, String> temp = new HashMap<>();
        temp.put("method", "taobao.tbk.sc.publisher.info.save");
        temp.put("app_key", Constants.qd_app_key);
        temp.put("format", "json");
        temp.put("inviter_code", Constants.qd_app_code);
        temp.put("session", key);
        temp.put("info_type", "1");
        temp.put("timestamp", time);
        temp.put("v", "2.0");
        temp.put("sign_method", "md5");
        String sign = null;
        sign = PddClient.getSign3(temp, Constants.qd_app_secret);
        requestParams.put("sign", sign);
        HttpUtils.post1("https://eco.taobao.com/router/rest", PromotionDetailsActivity.this, requestParams, new TextHttpResponseHandler() {
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
                if (!responseString.contains("error_response")) {
                    try {
                        JSONObject object = new JSONObject(responseString);
                        bindLd(object.getJSONObject("tbk_sc_publisher_info_save_response").getJSONObject("data").getString("relation_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject object = new JSONObject(responseString);
                        ToastUtils.showShortToast(PromotionDetailsActivity.this, object.getJSONObject("error_response").getString("sub_msg") + "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void bindLd(String relation_id) {
        RequestParams params = new RequestParams();
        params.put("tb_rid", relation_id);
        HttpUtils.post(Constants.BIND_RELATION_ID, PromotionDetailsActivity.this, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                String responseResult = new String(arg2);
//                LogUtils.e(TAG, "onSuccess()--" + responseResult);
                try {
                    JSONObject jsonObject = new JSONObject(responseResult);
                    String code = jsonObject.optString("code");
                    String msg = jsonObject.optString("msg");
                    // String result = jsonObject.optString("result");
                    if ("0".equalsIgnoreCase(code)) { // 操作成功
                        showToast("开通绑定成功");
                        onResume();
                    } else {
                        showToast(msg);
                    }
                } catch (JSONException e) {
//                    LogUtils.i(TAG, e.toString());
                }
            }

            @Override
            public void onFinish() {
                // 隐藏进度条
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
            }
        });
    }
}
