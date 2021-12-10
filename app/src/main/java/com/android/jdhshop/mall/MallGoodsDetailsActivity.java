package com.android.jdhshop.mall;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.jdhshop.common.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.login.WelActivity;
import com.android.jdhshop.malladapter.PopwinlistviewAdapter;
import com.android.jdhshop.mallbean.BuyCarBean;
import com.android.jdhshop.mallbean.GoodsDetailBean;
import com.android.jdhshop.mallbean.Selectbean;
import com.android.jdhshop.utils.BitmapUtils;
import com.android.jdhshop.utils.MyScrollView;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class MallGoodsDetailsActivity extends BaseActivity implements MyScrollView.OnScrollListener, IUiListener {

    @BindView(R.id.bg_head2)
    LinearLayout headView;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.view_zz)
    View viewZZ;
    @BindView(R.id.ll_share)
    LinearLayout llShare;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.txt_collect)
    TextView txtCollect;
    @BindView(R.id.homeBanner)
    MZBannerView homeBanner;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.scroll_view)
    MyScrollView scrollView;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.txt_select)
    TextView txtSelect;
    @BindView(R.id.txt_merchant_name)
    TextView txtMerchantName;
    @BindView(R.id.txt_price)
    TextView txtPrice;
    @BindView(R.id.txt_old_price)
    TextView txtOldPrice;
    @BindView(R.id.txt_share)
    TextView txtShare;
    @BindView(R.id.rb_detail)
    RadioButton rbDetail;
    @BindView(R.id.txt_detail)
    WebView txtDetail;
    @BindView(R.id.recy_comment)
    RecyclerView recyComment;
    @BindView(R.id.rb_comment)
    RadioButton rbComment;
    @BindView(R.id.rg_select)
    RadioGroup rgSelect;
    private GradientDrawable gradientDrawable;
    private boolean isCollect = false;//未收藏
    private String goods_id = "";
    private TextView tv_num;
    private ImageView iv_img;
    private int goodnum = 1;
    private GoodsDetailBean goodsDetailBean;
    Dialog dialog;
    private List<Selectbean> skuList = new ArrayList();
    PopwinlistviewAdapter popwinlistviewAdapter;
    private boolean hasvideo = false;
    List<String> images = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("0.00");
    Bitmap bitmap;

    @Override
    protected void initUI() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_mall_goods_detail);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        Drawable drawable1 = getResources().getDrawable(R.mipmap.icon_back_while);
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
        tvLeft.setCompoundDrawables(drawable1, null, null, null);
        tvLeft.setVisibility(View.VISIBLE);
        gradientDrawable = (GradientDrawable) tvLeft.getBackground();
        if (getIntent().getExtras().getString("isVip") != null) {
            findViewById(R.id.txt_select).setVisibility(View.GONE);
            findViewById(R.id.tv_car).setVisibility(View.GONE);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(80, 0, 80, 0);
            findViewById(R.id.txt_buy).setLayoutParams(lp);
            findViewById(R.id.txt_old_price).setVisibility(View.GONE);
            findViewById(R.id.txt_merchant_name).setVisibility(View.VISIBLE);
        }
        tvTitle.setText("商品详情");
        homeBanner.setLayoutParams(new RelativeLayout.LayoutParams(BitmapUtils.getScreenWith(this), (int) (BitmapUtils.getScreenWith(this))));
        homeBanner.setCanLoop(false);
        txtDetail.getSettings().setJavaScriptEnabled(true);
        txtDetail.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);// 不使用缓存
        txtDetail.getSettings().setUserAgentString(System.getProperty("http.agent"));
        txtDetail.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
        txtDetail.getSettings().setAppCacheEnabled(true);
        txtDetail.getSettings().setDomStorageEnabled(true);
        txtDetail.getSettings().setUseWideViewPort(true);
        txtDetail.getSettings().setLoadWithOverviewMode(true);
        txtDetail.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                imgReset();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        txtDetail.setWebChromeClient(new WebChromeClient());
//        homeBanner.setImageLoader(new BannerImageLoader());
//        homeBanner.setDelayTime(3000);

        goods_id = getIntent().getExtras().getString("goods_id");
        if (getIntent().getExtras().getString("tt") != null) {
            findViewById(R.id.tv_car).setVisibility(View.GONE);
        }
        refreshLayout.setEnableLoadMore(false);
        txtSelect.setTag("0");
//        isCollectRequest(goods_id);
        refreshLayout.autoRefresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initListener() {
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
        tvTitle.setTextColor(Color.argb(0, 255, 255, 255));
        scrollView.setScrolListener(this);
        rgSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_detail) {
                    txtDetail.setVisibility(View.VISIBLE);
                    recyComment.setVisibility(View.GONE);
                } else {
                    txtDetail.setVisibility(View.GONE);
                    recyComment.setVisibility(View.VISIBLE);
                }
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
            headView.getBackground().mutate().setAlpha(255);
        }
    }

    private void imgReset() {
        txtDetail.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                "    img.style.maxWidth = '100%'; img.style.height = 'auto';  " +
                "}" +
                "})()");
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

        ///wap.php/Goods/share/goods_id/{$goods_id}/uid/{$uid}
        @Override
        public void onBind(Context context, int position, final String data) {
            // 数据绑定
            if (hasvideo && position == 0) {
                standard.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.VISIBLE);
                play.setVisibility(View.VISIBLE);
                Glide.with(MallGoodsDetailsActivity.this).load(images.get(1)).into(mImageView);
                standard.setUp(data, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
                Glide.with(MallGoodsDetailsActivity.this).load(images.get(1)).into(standard.thumbImageView);
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
                Glide.with(MallGoodsDetailsActivity.this).load(data).into(mImageView);
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

    private void getGoodsMsgRequest() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("goods_id", goods_id);
        HttpUtils.post(getIntent().getExtras().getString("isVip") != null ? Constants.APP_IP + "/api/UserGoods/getMsg" : Constants.SELF_GOODS_DETAIL, MallGoodsDetailsActivity.this,requestParams, new onOKJsonHttpResponseHandler<GoodsDetailBean>(new TypeToken<Response<GoodsDetailBean>>() {
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
            public void onSuccess(int statusCode, Response<GoodsDetailBean> datas) {
                goodsDetailBean = datas.getData();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefresh();
                        images.clear();
                        if (goodsDetailBean.imglist != null && goodsDetailBean.imglist.size() > 0) {
                            for (int i = 0; i < goodsDetailBean.imglist.size(); i++) {
                                images.add(goodsDetailBean.imglist.get(i).img.contains("http") ? goodsDetailBean.imglist.get(i).img : Constants.APP_IP + goodsDetailBean.imglist.get(i).img);
                            }
                        } else {
                            images.add(goodsDetailBean.goodsMsg.img.contains("http") ? goodsDetailBean.goodsMsg.img : Constants.APP_IP + goodsDetailBean.goodsMsg.img);
                        }
                        if (goodsDetailBean.goodsMsg.video != null && !"".endsWith(goodsDetailBean.goodsMsg.video)) {
                            hasvideo = true;
                            images.add(0, goodsDetailBean.goodsMsg.video);
                        } else {
                            hasvideo = false;
                        }
                        homeBanner.setPages(images, new MZHolderCreator<BannerViewHolder>() {
                            @Override
                            public BannerViewHolder createViewHolder() {
                                return new BannerViewHolder();
                            }
                        });
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String goods_img = goodsDetailBean.goodsMsg.img.startsWith("http") ? goodsDetailBean.goodsMsg.img : Constants.APP_IP + goodsDetailBean.goodsMsg.img;
                                    bitmap = Glide.with(MallGoodsDetailsActivity.this).load(goods_img).asBitmap().into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        titleTv.setText(goodsDetailBean.goodsMsg.goods_name);
                        try {
                            txtMerchantName.setText("预估赚:" + df.format(Double.valueOf(goodsDetailBean.goodsMsg.price) * Double.parseDouble(goodsDetailBean.goodsMsg.commission_rate) * Double.parseDouble(df.format((float) SPUtils.getIntData(MallGoodsDetailsActivity.this, "rate", 0) / 100))));
                        } catch (Exception c) {
                            txtMerchantName.setText("预估赚:" + df.format(Double.valueOf(goodsDetailBean.goodsMsg.price) * Double.parseDouble(df.format((float) SPUtils.getIntData(MallGoodsDetailsActivity.this, "rate", 0) / 100))));
                        }
                        if (getIntent().getExtras().getString("isVip") != null) {
                            txtMerchantName.setText("购买成为" + goodsDetailBean.catMsg.day_num + "天会员");
                        }
                        txtPrice.setText(goodsDetailBean.goodsMsg.price);
                        txtOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        txtOldPrice.setText("  " + goodsDetailBean.goodsMsg.old_price);
                        txtDetail.loadData(goodsDetailBean.goodsMsg.content == null ? "" : goodsDetailBean.goodsMsg.content.replaceAll("<img", "<img style='width:100%;height:auto'"), "text/html", "utf-8");
                    }
                });
                if (goodsDetailBean.goodsMsg.sku_arr != null)
                    initDialog();
            }
        });
    }

    private void initDialog() {
        View view = getLayoutInflater().inflate(R.layout.item_popwindows,
                null);
        ListView pop_listview = view.findViewById(R.id.popwindow_listvew);
        popwinlistviewAdapter = new PopwinlistviewAdapter(this, goodsDetailBean.goodsMsg.sku_arr);
        skuList.clear();
        txtSelect.setText("请选择规格、数量");
        txtSelect.setTag("0");
        for (int i = 0; i < goodsDetailBean.goodsMsg.sku_arr.size(); i++) {
            skuList.add(new Selectbean());
        }
        pop_listview.setAdapter(popwinlistviewAdapter);
        TextView tv_name = view.findViewById(R.id.itempopwindows_name);
        tv_num = view.findViewById(R.id.itempopwindows_num);
        TextView tv_addgwc = view.findViewById(R.id.itempopwindows_addgwc);
        ImageView iv_jia = view.findViewById(R.id.itempopwindows_jia);
        ImageView iv_jian = view.findViewById(R.id.itempopwindows_jian);
        ImageView iv_back = view.findViewById(R.id.itempopwindows_back);
        iv_img = view.findViewById(R.id.itempopwindows_img);
        tv_name.setText(goodsDetailBean.goodsMsg.goods_name);
        tv_num.setText(goodnum + "");
        Glide.with(this).load(Constants.APP_IP + goodsDetailBean.goodsMsg.img).into(iv_img);
        dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.onWindowAttributesChanged(wl);
        dialog.setCanceledOnTouchOutside(true);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                for (int i = 0; i < skuList.size(); i++) {
                    if (skuList.get(i).getString().equals("")) {
                        txtSelect.setText("请选择规格、数量");
                        txtSelect.setTag("0");
                        break;
                    }
                }
            }
        });
        iv_jia.setOnClickListener(new View.OnClickListener() {//加
            @Override
            public void onClick(View view) {
                goodnum++;
                tv_num.setText(goodnum + "");
            }
        });
        iv_jian.setOnClickListener(new View.OnClickListener() {//减
            @Override
            public void onClick(View view) {
                if (goodnum == 1) {
                    showToast("不能再减少了");
                } else {
                    goodnum--;
                    tv_num.setText(goodnum + "");
                }
            }
        });
        tv_addgwc.setOnClickListener(new View.OnClickListener() {//加入购物车
            @Override
            public void onClick(View view) {
                boolean has = false;
                for (int i = 0; i < skuList.size() && i < goodsDetailBean.goodsMsg.sku_arr.size(); i++) {
                    if (skuList.get(i).getString().equals("")) {
                        showToast("请选择属性" + goodsDetailBean.goodsMsg.sku_arr.get(i).attribute_name);
                        has = true;
                        break;
                    }
                }
                if (!has) {
                    //都选择了
                    String temp = "";
                    for (int i = 0; i < skuList.size(); i++) {
                        if (i != skuList.size() - 1) {
                            temp += skuList.get(i).getValue() + ",";
                        } else {
                            temp += skuList.get(i).getValue();
                        }
                    }
                    JSONObject object1 = new JSONObject();
                    JSONArray array = new JSONArray();
                    for (int i = 0; i < skuList.size(); i++) {
                        object1 = new JSONObject();
                        try {
                            object1.put("attribute_id", Integer.valueOf(skuList.get(i).getString()));
                            object1.put("value", skuList.get(i).getValue());
                            array.put(object1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    LogUtils.d("qweqwer", array.toString());
                    for (int i = 0; i < goodsDetailBean.skulist.size(); i++) {
                        LogUtils.d("qweqwer", goodsDetailBean.skulist.get(i).sku + "  " + goodsDetailBean.skulist.get(i).price);
                        if (goodsDetailBean.skulist.get(i).sku.equals(array.toString())) {
                            txtPrice.setText("¥" + goodsDetailBean.skulist.get(i).price);
                            try {
                                txtMerchantName.setText("预估赚:" + df.format(Double.valueOf(goodsDetailBean.skulist.get(i).price) * Double.parseDouble(goodsDetailBean.goodsMsg.commission_rate) * Double.parseDouble(df.format((float) SPUtils.getIntData(MallGoodsDetailsActivity.this, "rate", 0) / 100))));
                            } catch (Exception c) {
                                txtMerchantName.setText("预估赚:" + df.format(Double.valueOf(goodsDetailBean.skulist.get(i).price) * Double.parseDouble(df.format((float) SPUtils.getIntData(MallGoodsDetailsActivity.this, "rate", 0) / 100))));
                            }
                            break;
                        }
                    }
                    txtSelect.setText("已选:" + temp);
                    txtSelect.setTag("1");
                    dialog.dismiss();
                }
            }
        });
        popwinlistviewAdapter.setsubClickListener(new PopwinlistviewAdapter.SubClickListener() {
            @Override
            public void OntopicClickListeners(View v, boolean detail, int posit, String str) {
                if (detail) {
                    skuList.get(posit).setString(goodsDetailBean.goodsMsg.sku_arr.get(posit).attribute_id);
                    skuList.get(posit).setValue(str);
                } else {
                    skuList.get(posit).setString("");
                    skuList.get(posit).setValue("");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    /**
//     * @属性:用户是否收藏商品
//     * @开发者:陈飞
//     * @时间:2018/7/28 21:52
//     */
//    private void isCollectRequest(String goods_id) {
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("goods_id", goods_id);
//        HttpUtils.post(Constants.IS_COLLECT_SELF_MALL_GOODS, requestParams, new onOKJsonHttpResponseHandler<IsCollectBean>(new TypeToken<Response<IsCollectBean>>() {
//        }) {
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(responseString);
//            }
//
//            @Override
//            public void onStart() {
//                super.onStart();
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Response<IsCollectBean> datas) {
//                if (datas.isSuccess()) {
//                    String is_collect = datas.getData().getIs_collect();
//                    if ("Y".equals(is_collect)) {//Y已收藏
//                        isCollect = true;
//                        //动态设置drawableLeft属性
//                        Drawable drawable1 = getResources().getDrawable(R.mipmap.coll_red);
//                        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
//                        txtCollect.setCompoundDrawables(null, drawable1, null, null);
//                        txtCollect.setText("已收藏");
//                    } else if ("N".equals(is_collect)) {//N未收藏
//                        isCollect = false;
//                        Drawable drawable1 = getResources().getDrawable(R.mipmap.coll_dark);
//                        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
//                        txtCollect.setCompoundDrawables(null, drawable1, null, null);
//                        txtCollect.setText("收藏");
//                    }
//                } else {
//                    showToast(datas.getMsg());
//                }
//            }
//        });
//    }
//
//    /**
//     * @属性:收藏
//     * @开发者:陈飞
//     * @时间:2018/7/28 22:04
//     */
//    private void collectRequest(final String goods_id) {
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("goods_id", goods_id);
//        HttpUtils.post(Constants.COLLECT_SELF_MALL_GOODS, requestParams, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(responseString);
//            }
//
//            @Override
//            public void onStart() {
//                super.onStart();
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                try {
//                    JSONObject jsonObject = new JSONObject(responseString);
//                    int code = jsonObject.optInt("code");
//                    String msg = jsonObject.optString("msg");
//                    if (code == 0) {
//                        isCollect = true;
//                        Drawable drawable1 = getResources().getDrawable(R.mipmap.coll_red);
//                        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
//                        txtCollect.setCompoundDrawables(null, drawable1, null, null);
//                        txtCollect.setText("已收藏");
//                    } else {
//                        showToast(msg);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    /**
//     * @属性:收藏
//     * @开发者:陈飞
//     * @时间:2018/7/28 22:04
//     */
//    private void cancelCollectRequest(final String goods_id) {
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("goods_id", goods_id);
//        HttpUtils.post(Constants.DIS_COLLECT_SELF_MALL_GOODS, requestParams, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(responseString);
//            }
//
//            @Override
//            public void onStart() {
//                super.onStart();
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                try {
//                    JSONObject jsonObject = new JSONObject(responseString);
//                    int code = jsonObject.optInt("code");
//                    String msg = jsonObject.optString("msg");
//                    if (code == 0) {
//                        isCollect = false;
//                        Drawable drawable1 = getResources().getDrawable(R.mipmap.coll_dark);
//                        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
//                        txtCollect.setCompoundDrawables(null, drawable1, null, null);
//                        txtCollect.setText("收藏");
//                    } else {
//                        showToast(msg);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    @Override
    public void onComplete(Object o) {

    }

    @Override
    public void onError(UiError uiError) {
    }

    @Override
    public void onCancel() {

    }

    @OnClick({R.id.tv_left, R.id.copy_friends_cicle_zone, R.id.copy_friends_btn, R.id.copy_friends_qq, R.id.copy_friends_cicle_btn, R.id.txt_select, R.id.tv_share, R.id.view_zz, R.id.tv_car, R.id.txt_buy, R.id.txt_collect, R.id.img_shop_car})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_shop_car:
//                openActivity(MallGoodsCartActivity.class);
                break;
            case R.id.tv_share:
                viewZZ.setVisibility(View.VISIBLE);
                llShare.setVisibility(View.VISIBLE);
                break;
            case R.id.view_zz:
                viewZZ.setVisibility(View.GONE);
                llShare.setVisibility(View.GONE);
                break;
//            case R.id.copy_friends_qq:
//                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq") == null) {
//                    T.showShort(this, "请安装QQ客户端");
//                    return;
//                }
//                QQShareUtil.shareToQQ(goodsDetailBean.goodsMsg.goods_name, goodsDetailBean.goodsMsg.description, new ArrayList<String>(Arrays.asList(Constants.APP_IP + "/wap.php/Goods/share/goods_id/" + goods_id + "/uid/" + SPUtils.getStringData(this, "uid", ""))), this, this);
//                viewZZ.setVisibility(View.GONE);
//                llShare.setVisibility(View.GONE);
//                break;
//            case R.id.copy_friends_cicle_zone:
//                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq") == null) {
//                    T.showShort(this, "请安装QQ客户端");
//                    return;
//                }
//                QQShareUtil.shareToQZone2(Constants.APP_IP + "/wap.php/Goods/share/goods_id/" + goods_id + "/uid/" + SPUtils.getStringData(this, "uid", ""), new ArrayList<String>(Arrays.asList(Constants.APP_IP + goodsDetailBean.goodsMsg.img)), goodsDetailBean.goodsMsg.goods_name, goodsDetailBean.goodsMsg.description, this, this);
//                viewZZ.setVisibility(View.GONE);
//                llShare.setVisibility(View.GONE);
//                break;
//            case R.id.copy_friends_cicle_btn:
//                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mm") == null) {
//                    T.showShort(this, "请安装微信客户端");
//                    return;
//                }
//                if(bitmap!=null){
//                    WxUtil.sharePicByBitMap3(bitmap,Constants.APP_IP + "/wap.php/Goods/share/goods_id/" + goods_id + "/uid/" + SPUtils.getStringData(this, "uid", ""), "wxhy", SendMessageToWX.Req.WXSceneTimeline, this, goodsDetailBean.goodsMsg.goods_name);
//                }else{
//                    WxUtil.sharePicByBitMap2(Constants.APP_IP + "/wap.php/Goods/share/goods_id/" + goods_id + "/uid/" + SPUtils.getStringData(this, "uid", ""), "wxhy", SendMessageToWX.Req.WXSceneTimeline, this, goodsDetailBean.goodsMsg.goods_name);
//                }
//                viewZZ.setVisibility(View.GONE);
//                llShare.setVisibility(View.GONE);
//                break;
//            case R.id.copy_friends_btn:
//                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mm") == null) {
//                    T.showShort(this, "请安装微信客户端");
//                    return;
//                }
//                if(bitmap!=null){
//                    WxUtil.sharePicByBitMap3(bitmap,Constants.APP_IP + "/wap.php/Goods/share/goods_id/" + goods_id + "/uid/" + SPUtils.getStringData(this, "uid", ""), "wxhy", SendMessageToWX.Req.WXSceneSession, this, goodsDetailBean.goodsMsg.goods_name);
//                }else{
//                    WxUtil.sharePicByBitMap2(Constants.APP_IP + "/wap.php/Goods/share/goods_id/" + goods_id + "/uid/" + SPUtils.getStringData(this, "uid", ""), "wxhy", SendMessageToWX.Req.WXSceneSession, this, goodsDetailBean.goodsMsg.goods_name);
//                }
//                viewZZ.setVisibility(View.GONE);
//                llShare.setVisibility(View.GONE);
//                break;
            case R.id.tv_left:
                finish();
                break;
            case R.id.txt_select:
                if (goodsDetailBean.goodsMsg.sku_arr == null || goodsDetailBean.goodsMsg.sku_arr.size() == 0) {
                    showToast("暂未获取到商品属性");
                    return;
                }
                popwinlistviewAdapter.setSelectbeans(skuList);
                dialog.show();
                break;
            case R.id.tv_car:
                if ((goodsDetailBean.goodsMsg.sku_arr == null || goodsDetailBean.goodsMsg.sku_arr.size() == 0)) {
                    addToCar();
                    return;
                }
                if ("0".equals(txtSelect.getTag().toString())) {
                    showToast("商品规格数量未选择");
                    return;
                }
                addToCar();
                break;
            case R.id.txt_collect:
//                if (isCollect) {
//                    cancelCollectRequest(goods_id);
//                } else {
//                    collectRequest(goods_id);
//                }
                break;
            case R.id.txt_buy:
                if (getIntent().getExtras().getString("isVip") != null) {
                    if ("Y".equals(goodsDetailBean.goodsMsg.is_send)) {
                        Bundle bundle = new Bundle();
                        BuyCarBean buyCarBean = new BuyCarBean();
                        buyCarBean.setGoods_id(goodsDetailBean.goodsMsg.goods_id);
                        buyCarBean.setGoods_name(goodsDetailBean.goodsMsg.goods_name);
                        buyCarBean.setImg(goodsDetailBean.goodsMsg.img);
                        buyCarBean.setMerchant_id(goodsDetailBean.goodsMsg.merchant_id);
                        buyCarBean.setNum(goodnum + "");
                        buyCarBean.setOld_price(goodsDetailBean.goodsMsg.old_price);
                        buyCarBean.setPrice(goodsDetailBean.goodsMsg.price);
                        buyCarBean.setPostage(goodsDetailBean.goodsMsg.postage);
                        buyCarBean.setSelectbeans(skuList);
                        List<BuyCarBean> buys = new ArrayList<>();
                        buys.add(buyCarBean);
                        bundle.putSerializable("buyCarBean", (Serializable) buys);
                        bundle.putString("isVip", "1");
                        openActivity(BuyGoodsActivity.class, bundle);
                    } else {
                        RequestParams requestParams = new RequestParams();
                        requestParams.put("goods_id", goods_id);
                        requestParams.put("num", "1");
                        HttpUtils.post(Constants.APP_IP + "/api/UserOrder/order",MallGoodsDetailsActivity.this, requestParams, new TextHttpResponseHandler() {
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
                                        String order_id = jsonObject.getJSONObject("data").getString("order_id");
                                        String order_num = jsonObject.getJSONObject("data").getString("order_num");
                                        Intent intent = new Intent(MallGoodsDetailsActivity.this, PayOrderMoneyActivity.class);
                                        intent.putExtra("money", goodsDetailBean.goodsMsg.price);
                                        intent.putExtra("order_num", order_num);
                                        intent.putExtra("order_id", order_id);
                                        if (getIntent().getExtras().getString("isVip") != null) {
                                            intent.putExtra("isVip", "1");
                                        }
                                        intent.putExtra("name", goodsDetailBean.goodsMsg.goods_name);
                                        startActivity(intent);
                                    } else if (code == 208) {
                                        showToast(msg);
                                    } else {
                                        showToast(msg);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    return;
                }
                if ((goodsDetailBean.goodsMsg.sku_arr == null || goodsDetailBean.goodsMsg.sku_arr.size() == 0)) {
                    Bundle bundle = new Bundle();
                    BuyCarBean buyCarBean = new BuyCarBean();
                    buyCarBean.setGoods_id(goodsDetailBean.goodsMsg.goods_id);
                    buyCarBean.setGoods_name(goodsDetailBean.goodsMsg.goods_name);
                    buyCarBean.setImg(goodsDetailBean.goodsMsg.img);
                    buyCarBean.setMerchant_id(goodsDetailBean.goodsMsg.merchant_id);
                    buyCarBean.setNum(goodnum + "");
                    buyCarBean.setOld_price(goodsDetailBean.goodsMsg.old_price);
                    buyCarBean.setPrice(goodsDetailBean.goodsMsg.price);
                    buyCarBean.setPostage(goodsDetailBean.goodsMsg.postage);
                    buyCarBean.setSelectbeans(skuList);
                    List<BuyCarBean> buys = new ArrayList<>();
                    buys.add(buyCarBean);
                    bundle.putSerializable("buyCarBean", (Serializable) buys);
                    openActivity(BuyGoodsActivity.class, bundle);
                    return;
                }
                if ("0".equals(txtSelect.getTag().toString())) {
                    showToast("商品规格数量未选择");
                    return;
                }
                Bundle bundle = new Bundle();
                BuyCarBean buyCarBean = new BuyCarBean();
                buyCarBean.setGoods_id(goodsDetailBean.goodsMsg.goods_id);
                buyCarBean.setGoods_name(goodsDetailBean.goodsMsg.goods_name);
                buyCarBean.setImg(goodsDetailBean.goodsMsg.img);
                buyCarBean.setMerchant_id(goodsDetailBean.goodsMsg.merchant_id);
                buyCarBean.setNum(goodnum + "");
                buyCarBean.setOld_price(goodsDetailBean.goodsMsg.old_price);
                buyCarBean.setPrice(txtPrice.getText().toString().replace("¥", ""));
                buyCarBean.setPostage(goodsDetailBean.goodsMsg.postage);
                buyCarBean.setSelectbeans(skuList);
                List<BuyCarBean> buys = new ArrayList<>();
                buys.add(buyCarBean);
                bundle.putSerializable("buyCarBean", (Serializable) buys);
                openActivity(BuyGoodsActivity.class, bundle);
                break;
        }
    }

    private void addToCar() {
        RequestParams requestParams = new RequestParams();
        JSONArray array = new JSONArray();
        JSONObject object;
        if (!(goodsDetailBean.goodsMsg.sku_arr == null || goodsDetailBean.goodsMsg.sku_arr.size() == 0)) {
            for (int i = 0; i < skuList.size(); i++) {
                object = new JSONObject();
                try {
                    object.put("attribute_id", Integer.valueOf(skuList.get(i).getString()));
                    object.put("value", skuList.get(i).getValue());
                    array.put(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            requestParams.put("goods_sku", array.toString());
        }
        requestParams.put("goods_id", goods_id);
        requestParams.put("goods_num", goodnum);
        LogUtils.d("dsfasd", requestParams.toString());
        HttpUtils.post(Constants.ADD_TO_SHOP_CAR, MallGoodsDetailsActivity.this,requestParams, new TextHttpResponseHandler() {
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
                    String msg = jsonObject.optString("msg");
                    showToast(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public class URLImageParser implements Html.ImageGetter {
        TextView mTextView;

        public URLImageParser(TextView textView) {
            this.mTextView = textView;
        }

        @Override
        public Drawable getDrawable(String source) {
            final URLDrawable urlDrawable = new URLDrawable();
            Glide.with(MallGoodsDetailsActivity.this).load(source).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    urlDrawable.bitmap = resource;
                    urlDrawable.setBounds(0, 0, resource.getWidth(), resource.getHeight());
                    mTextView.invalidate();
                    mTextView.setText(mTextView.getText());
                }
            });
            return urlDrawable;
        }
    }

    public class URLDrawable extends BitmapDrawable {
        protected Bitmap bitmap;

        @Override
        public void draw(Canvas canvas) {
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, 0, 0, getPaint());
            }
        }
    }
}
