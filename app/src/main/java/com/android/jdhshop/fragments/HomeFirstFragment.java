package com.android.jdhshop.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.BaoYouActivity;
import com.android.jdhshop.activity.CommissionPhbActivity;
import com.android.jdhshop.activity.DailyBonusActivity;
import com.android.jdhshop.activity.DialogActivity;
import com.android.jdhshop.activity.DialogActivity2;
import com.android.jdhshop.activity.DouActivity;
import com.android.jdhshop.activity.ElemeActivity;
import com.android.jdhshop.activity.GaoyongActivity;
import com.android.jdhshop.activity.GuoyedanActivity;
import com.android.jdhshop.activity.JdActivity;
import com.android.jdhshop.activity.JiayoulistActivity;
import com.android.jdhshop.activity.NewClassActivity;
import com.android.jdhshop.activity.NewShuanshierActivity;
import com.android.jdhshop.activity.PHBActivity;
import com.android.jdhshop.activity.PddActivity;
import com.android.jdhshop.activity.PddTheamActivity;
import com.android.jdhshop.activity.PromotionDetailsActivity;
import com.android.jdhshop.activity.QdActivity;
import com.android.jdhshop.activity.SearchActivity;
import com.android.jdhshop.activity.SearchResultActivity;
import com.android.jdhshop.activity.SetActivity;
import com.android.jdhshop.activity.SetAssistantActivity;
import com.android.jdhshop.activity.TianMaoShuangShiyiActivity;
import com.android.jdhshop.activity.WebViewActivity;
import com.android.jdhshop.activity.WebViewActivity2;
import com.android.jdhshop.activity.WebViewActivity3;
import com.android.jdhshop.activity.WebViewActivityNotOpenDefaultWeb;
import com.android.jdhshop.activity.WphActivity;
import com.android.jdhshop.activity.XuanpinkActivity;
import com.android.jdhshop.activity.ZeroBuyActivity;
import com.android.jdhshop.adapter.AddWeiXinQunAdapter;
import com.android.jdhshop.adapter.HomeDouAdapter;
import com.android.jdhshop.adapter.HomeIconAdapter;
import com.android.jdhshop.adapter.TbActivityAdapter;
import com.android.jdhshop.adapter.TodayHighlightsAdapter;
import com.android.jdhshop.adapter.TodayHighlightsChildAdapter3;
import com.android.jdhshop.advistion.JuDuoHuiAdvertisement;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.bean.BannerBean;
import com.android.jdhshop.bean.HaoDanBean;
import com.android.jdhshop.bean.Item;
import com.android.jdhshop.bean.MessageCenterBean;
import com.android.jdhshop.bean.MessageEvent;
import com.android.jdhshop.bean.PDDBean;
import com.android.jdhshop.bean.PddClient;
import com.android.jdhshop.bean.PromotionDetailsBean;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.SetBean;
import com.android.jdhshop.bean.TaobaoGuestBean;
import com.android.jdhshop.bean.TodayHighlightsBean2;
import com.android.jdhshop.bean.WeiXinQunXinXIBean;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.juduohui.message.JuduohuiMessage;
import com.android.jdhshop.juduohui.response.HttpJsonResponse;
import com.android.jdhshop.login.WelActivity;
import com.android.jdhshop.mall.ShopMallActivity;
import com.android.jdhshop.merchantactivity.MerchanthomeActivity;
import com.android.jdhshop.suning.SuningHomeActivity;
import com.android.jdhshop.utils.CornerTransform;
import com.android.jdhshop.utils.MyRecyclerView2;
import com.android.jdhshop.utils.SignUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gcssloop.widget.PagerGridLayoutManager;
import com.gcssloop.widget.PagerGridSnapHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.king.zxing.Intents;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.loopj.android.http.RequestParams;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.uuch.adlibrary.AdConstant;
import com.uuch.adlibrary.AdManager;
import com.uuch.adlibrary.bean.AdInfo;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.youth.banner.transformer.ZoomOutTranformer;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;
import jd.union.open.goods.query.request.GoodsReq;
import jd.union.open.goods.query.request.UnionOpenGoodsQueryRequest;
import jd.union.open.goods.query.response.UnionOpenGoodsQueryResponse;

/**
 * 首页
 * Created by yohn on 2018/7/11.
 */

public class HomeFirstFragment extends BaseLazyFragment implements View.OnClickListener {
    private View view;
    public static HomeFirstFragment fragment;
    private ImageView homeBg;
    //导航标题
    @BindView(R.id.home_recyclerView)
    RecyclerView homeRecyclerView;
    Unbinder unbinder;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.aiicon)
    ImageView aiicon;
    @BindView(R.id.qunliaoicon)
    ImageView qunliaoicon;

    private String min_id = "1";

    private List<TodayHighlightsBean2> todayHighlightsBeans = new ArrayList<>();
    private TodayHighlightsAdapter highlightsAdapter;
    private MyRecyclerView2 homeGrid;
    private int Home_Search = 0; //0为首页,1为搜索页

    private String search_content;//搜索内容

    private String search_sort;//排序字段

    private boolean has_coupon = true;//是否优惠券

    private int indexNum = 1;

    private int refreshNum = 0;

    private int refreshStatus = 0;

    List<HaoDanBean> taobaoGuesChildtBeans = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private HeaderAndFooterWrapper headerAndFooterWrapper;
    private RecyclerView recyclerViewAc;
    private HomeDouAdapter homeDouAdapter;
    //wmm
    private Banner banner;
    private MZBannerView banner2;
    private RadioButton rb1, rb2, rb3, rb4, rb5, rb6;
    private RecyclerView recy_dou;
    private LinearLayout list_adv;
    private List<BannerBean> images3 = new ArrayList<>();
    private List<BannerBean> images = new ArrayList<>();
    private List<MessageCenterBean.MessageCenterChildBean> images2 = new ArrayList<>();
    private HomeIconAdapter homeIconAdapter;
    private List<Item> homeItems = new ArrayList<>();
    private RadioGroup page_rg;
    private TodayHighlightsChildAdapter3 highlightsAdapter2;
    private List<BannerBean> acBanner = new ArrayList<>();
    private TbActivityAdapter tbActivityAdapter;
    private List<HaoDanBean> douList = new ArrayList<>();
    private Activity mActivity;
    View homeTabView;
    Date endTime;
    ACache aCache;
    private GridView gridView;
    private List<WeiXinQunXinXIBean> weiXinQunXinXIBeanList = new ArrayList<>();
    private AddWeiXinQunAdapter weiXinQunAdapter;
    //    private RecyclerView tuijianRecy, baoyouRecy, qgRecy;
    List<TaobaoGuestBean.TaobaoGuesChildtBean> nineList = new ArrayList<>();
    //    private NineAdapter nineAdapter;
//    private PHBAdapter phbAdapter;
    DecimalFormat df = new DecimalFormat("0.00");
    //    private List<HaoDanBean> list = new ArrayList<>();
//    private List<HaoDanBean> qgList = new ArrayList<>();
//    private TextView txtHour, tipTwo, tipThree;
//    private HomeQGAdapter homeQGAdapter;
    DateFormat df_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private List<HaoDanBean> todayHighlightsBeans2 = new ArrayList<>();

    public static HomeFirstFragment getInstance() {
        if (fragment == null) {
            fragment = new HomeFirstFragment();
        }
        return fragment;
    }

    int hour_type = 1;
    private boolean hasdata = true;
    private Gson gson = new Gson();
    RadioGroup rg_index;
    private ImageView bankuai_image1, bankuai_image2, pdd_img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Log.d(TAG, "onCreateView: 这" + savedInstanceState);
        // Log.d(TAG, "onCreateView: 这" + view);
        view = inflater.inflate(R.layout.fragment_first_home, container, false);
        ButterKnife.bind(this, view);
        init();
        addListener();
        isVisible = true;
        lazyload();
        // Log.d(TAG, "onCreateView: 这里执行！！");
        getAdvConfig();
        return view;
    }

    private void init() {
        aCache = ACache.get(getContext());
        String zhuaqu = SPUtils.getStringData(getContext(), "zhuaqu", "");
        if (zhuaqu.equals("2")) {
            aiicon.setVisibility(View.VISIBLE);
            qunliaoicon.setVisibility(View.VISIBLE);
        }
        mActivity = getActivity();
        BaseLogDZiYuan.LogDingZiYuan(qunliaoicon, "qunliaoicon.png");
        BaseLogDZiYuan.LogDingZiYuan(aiicon, "aiicon.png");

        aiicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SetAssistantActivity.class);
                SPUtils.saveStringData(getContext(), "zhuaqu", "0");
                aiicon.setVisibility(View.GONE);
                qunliaoicon.setVisibility(View.GONE);
                homeDouAdapter.notifyDataSetChanged();
                startActivity(intent);
            }
        });
        //添加商品选择群聊
        qunliaoicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1.创建弹出式对话框
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());    // 系统默认Dialog没有输入框
                // 获取自定义的布局
                View alertDialogView = View.inflate(getContext(), R.layout.dialog_ai_xzqunliao, null);
                final AlertDialog tempDialog = alertDialog.create();
                tempDialog.setView(alertDialogView, 0, 0, 0, 0);
                tempDialog.getWindow().setBackgroundDrawableResource(R.drawable.yuanjiao);
                final EditText editText = (EditText) alertDialogView.findViewById(R.id.ed_message);
                tempDialog.setCancelable(true);
                tempDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView quxiao = alertDialogView.findViewById(R.id.positiveTextView);
                quxiao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tempDialog.dismiss();
                    }
                });
                TextView queren = alertDialogView.findViewById(R.id.negativeTextView);
                queren.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String addid = SPUtils.getStringData(context, "addid", "");
                        SPUtils.saveStringData(context, "huoquaddid", addid + "");
                        tempDialog.dismiss();
                    }
                });

                RecyclerView addrecy = alertDialogView.findViewById(R.id.xzqunliao_recy);
                addrecy.setLayoutManager(new LinearLayoutManager(context));
                weiXinQunXinXIBeanList.clear();
                weiXinQunAdapter = new AddWeiXinQunAdapter(context, weiXinQunXinXIBeanList);
                addrecy.setAdapter(weiXinQunAdapter);
                WeiXinq();
                tempDialog.show();
            }
        });
        highlightsAdapter = new TodayHighlightsAdapter(getActivity(), R.layout.today_highlights_item, todayHighlightsBeans);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homeRecyclerView.setLayoutManager(linearLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        homeRecyclerView.setHasFixedSize(true);
        homeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置一个头部
        homeTabView = getLayoutInflater().inflate(R.layout.home_tab_layout, null);
//        if("N".equals(SPUtils.getStringData(context,"is_tm_11","N"))){
//        }else{
        recyclerViewAc = homeTabView.findViewById(R.id.ac_recy);
        recyclerViewAc.setLayoutManager(new GridLayoutManager(context, 2));
        tbActivityAdapter = new TbActivityAdapter(R.layout.item_tb_ac, acBanner);
        recyclerViewAc.setAdapter(tbActivityAdapter);
        tbActivityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("title", acBanner.get(position).taobao_activity_name);
                bundle.putString("material_id", acBanner.get(position).tb_activity_id);
                openActivity(TianMaoShuangShiyiActivity.class, bundle);
            }
        });
        getTbActivity();
//        }
        banner = homeTabView.findViewById(R.id.main_banner);
//        banner.setLayoutParams(new RelativeLayout.LayoutParams(BitmapUtils.getScreenWith(context), (int) (BitmapUtils.getScreenWith(context) / 2.3)));
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                if (context != null && !((Activity) context).isDestroyed())
                    Glide.with(context).load(path).asBitmap().skipMemoryCache(false).dontAnimate().error(R.drawable.no_banner).transform(new CornerTransform(context, 10)).into(imageView);
            }

        });
        banner2 = homeTabView.findViewById(R.id.home_ad);
        homeBg = homeTabView.findViewById(R.id.home_bg);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        list_adv = homeTabView.findViewById(R.id.list_adv);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        homeTabView.setLayoutParams(layoutParams);
        //优惠转
        homeTabView.findViewById(R.id.ll_home_one).setOnClickListener(this);
        homeTabView.findViewById(R.id.ll_home_two).setOnClickListener(this);
        homeTabView.findViewById(R.id.ll_home_three).setOnClickListener(this);
        homeTabView.findViewById(R.id.ll_home_four).setOnClickListener(this);
        homeTabView.findViewById(R.id.ll_home_five).setOnClickListener(this);
        homeTabView.findViewById(R.id.ll_home_six).setOnClickListener(this);
        homeTabView.findViewById(R.id.ll_home_seven).setOnClickListener(this);
        homeTabView.findViewById(R.id.pdd_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id", "1");
                bundle.putString("name", "实时热销榜");
                bundle.putString("type", "1");
                openActivity(PddTheamActivity.class, bundle);
            }
        });
        recy_dou = homeTabView.findViewById(R.id.recy_dou);
        rg_index = homeTabView.findViewById(R.id.rg_index);

        bankuai_image1 = homeTabView.findViewById(R.id.bankuai_image1);
        bankuai_image2 = homeTabView.findViewById(R.id.bankuai_image2);
        pdd_img = homeTabView.findViewById(R.id.pdd_img);

        BaseLogDZiYuan.LogDingZiYuan(bankuai_image1, "icon_dou_title.png");
        BaseLogDZiYuan.LogDingZiYuan(bankuai_image2, "icon_limitedtime_title.png");
        BaseLogDZiYuan.LogDingZiYuan(pdd_img, "icon_pdd_top.png");
        homeDouAdapter = new HomeDouAdapter(R.layout.today_highlights_child_item4, douList);
        LinearLayoutManager douManager = new LinearLayoutManager(context);
        douManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recy_dou.setLayoutManager(douManager);
        recy_dou.setAdapter(homeDouAdapter);
        homeDouAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HaoDanBean bean = douList.get(position);
                if (bean != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("num_iid", bean.itemid);
                    bundle.putSerializable("bean", bean);
                    //bundle.putString("comm", df.format(Double.valueOf(bean.tkmoney)* Double.parseDouble(df.format((float) SPUtils.getIntData(getActivity(), "rate", 0) / 100)))+"");
                    bundle.putString("price", bean.itemendprice);
                    bundle.putString("tye", "1");
                    bundle.putString("url", bean.dy_video_url);
                    Intent intent = new Intent(context, PromotionDetailsActivity.class);
                    if (bundle != null) {
                        intent.putExtras(bundle);
                    }
                    context.startActivity(intent);
                }
            }
        });
        getDous();
        gridView = homeTabView.findViewById(R.id.listView2);
        highlightsAdapter2 = new TodayHighlightsChildAdapter3(context, R.layout.index_item, todayHighlightsBeans2);
        gridView.setAdapter(highlightsAdapter2);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HaoDanBean bean = todayHighlightsBeans2.get(position);
                if (bean != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("num_iid", bean.itemid);
                    bundle.putString("price", bean.itemendprice);
                    bundle.putString("yuan_price", bean.itemprice);

                    bundle.putSerializable("bean", bean);
                    openActivity(PromotionDetailsActivity.class, bundle);
                }
            }
        });
        Calendar ca = Calendar.getInstance();
        int hour = ca.get(Calendar.HOUR_OF_DAY);//小时
        int year = ca.get(Calendar.YEAR);//获取年份
        int month = ca.get(Calendar.MONTH) + 1;//获取月份
        int day = ca.get(Calendar.DATE);//获取日
        rb1 = homeTabView.findViewById(R.id.rb_one);
        rb2 = homeTabView.findViewById(R.id.rb_two);
        rb3 = homeTabView.findViewById(R.id.rb_three);
        rb4 = homeTabView.findViewById(R.id.rb_four);
        rb5 = homeTabView.findViewById(R.id.rb_five);
        rb6 = homeTabView.findViewById(R.id.rb_six);
        if (hour < 10) {
            hour_type = 6;
            rb1.setText(String.format(getResources().getString(R.string.test_text), "00:00", "抢购中"));
            rb1.setChecked(true);
            try {
                endTime = df_date.parse(year + "-" + month + "-" + day + " 10:00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            rb1.setTextColor(getResources().getColor(R.color.white));
            rb2.setText(String.format(getResources().getString(R.string.test_text), "10:00", "即将开始"));
            rb3.setText(String.format(getResources().getString(R.string.test_text), "12:00", "即将开始"));
            rb4.setText(String.format(getResources().getString(R.string.test_text), "15:00", "即将开始"));
            rb5.setText(String.format(getResources().getString(R.string.test_text), "20:00", "即将开始"));
            rb6.setText(String.format(getResources().getString(R.string.test_text), "24:00", "即将开始"));
        } else if (hour < 12) {
            hour_type = 7;

            rb1.setText(String.format(getResources().getString(R.string.test_text), "00:00", "已结束"));
            rb2.setChecked(true);
            try {
                endTime = df_date.parse(year + "-" + month + "-" + day + " 12:00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            rb2.setTextColor(getResources().getColor(R.color.white));
            rb2.setText(String.format(getResources().getString(R.string.test_text), "10:00", "抢购中"));
            rb3.setText(String.format(getResources().getString(R.string.test_text), "12:00", "即将开始"));
            rb4.setText(String.format(getResources().getString(R.string.test_text), "15:00", "即将开始"));
            rb5.setText(String.format(getResources().getString(R.string.test_text), "20:00", "即将开始"));
            rb6.setText(String.format(getResources().getString(R.string.test_text), "24:00", "即将开始"));
        } else if (hour < 15) {
            hour_type = 8;

            rb1.setText(String.format(getResources().getString(R.string.test_text), "00:00", "已结束"));
            rb3.setChecked(true);
//            rb3.setTextColor(getResources().getColor(R.color.white));
            try {
                endTime = df_date.parse(year + "-" + month + "-" + day + " 15:00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            rb2.setText(String.format(getResources().getString(R.string.test_text), "10:00", "已结束"));
            rb3.setText(String.format(getResources().getString(R.string.test_text), "12:00", "抢购中"));
            rb4.setText(String.format(getResources().getString(R.string.test_text), "15:00", "即将开始"));
            rb5.setText(String.format(getResources().getString(R.string.test_text), "20:00", "即将开始"));
            rb6.setText(String.format(getResources().getString(R.string.test_text), "24:00", "即将开始"));
        } else if (hour < 20) {
            hour_type = 9;

            rb1.setText(String.format(getResources().getString(R.string.test_text), "00:00", "已结束"));
            rb4.setChecked(true);
//            rb4.setTextColor(getResources().getColor(R.color.white));
            rb2.setText(String.format(getResources().getString(R.string.test_text), "10:00", "已结束"));
            try {
                endTime = df_date.parse(year + "-" + month + "-" + day + " 20:00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            rb3.setText(String.format(getResources().getString(R.string.test_text), "12:00", "已结束"));
            rb4.setText(String.format(getResources().getString(R.string.test_text), "15:00", "抢购中"));
            rb5.setText(String.format(getResources().getString(R.string.test_text), "20:00", "即将开始"));
            rb6.setText(String.format(getResources().getString(R.string.test_text), "24:00", "即将开始"));
        } else if (hour < 24) {
            hour_type = 10;

            rb1.setText(String.format(getResources().getString(R.string.test_text), "00:00", "已结束"));
            rb5.setChecked(true);
            try {
                endTime = df_date.parse(year + "-" + month + "-" + day + " 24:00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            rb5.setTextColor(getResources().getColor(R.color.white));
            rb2.setText(String.format(getResources().getString(R.string.test_text), "10:00", "已结束"));
            rb3.setText(String.format(getResources().getString(R.string.test_text), "12:00", "已结束"));
            rb4.setText(String.format(getResources().getString(R.string.test_text), "15:00", "已结束"));
            rb5.setText(String.format(getResources().getString(R.string.test_text), "20:00", "抢购中"));
            rb6.setText(String.format(getResources().getString(R.string.test_text), "24:00", "即将开始"));
        }
        rg_index.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_one:
                        hour_type = 6;
                        getTbkListRequst1(search_sort, "必推精选", "服装");
                        break;
                    case R.id.rb_two:
                        hour_type = 7;
                        getTbkListRequst1(search_sort, "必推精选", "服装");
                        break;
                    case R.id.rb_three:
                        hour_type = 8;
                        getTbkListRequst1(search_sort, "必推精选", "服装");
                        break;
                    case R.id.rb_four:
                        hour_type = 9;
                        getTbkListRequst1(search_sort, "必推精选", "服装");
                        break;
                    case R.id.rb_five:
                        hour_type = 10;
                        getTbkListRequst1(search_sort, "必推精选", "服装");
                        break;
                    case R.id.rb_six:
                        hour_type = 11;
                        getTbkListRequst1(search_sort, "必推精选", "服装");
                        break;


                }
            }
        });
        homeTabView.findViewById(R.id.d_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id", "0");
                openActivity(DouActivity.class, bundle);
            }
        });

        homeTabView.findViewById(R.id.ll_home_eight).setOnClickListener(this);
//        homeTabView.findViewById(R.id.txt_cj).setOnClickListener(this);
        homeGrid = homeTabView.findViewById(R.id.home_grid);
        // 1.水平分页布局管理器
        PagerGridLayoutManager layoutManager = new PagerGridLayoutManager(
                2, 5, PagerGridLayoutManager.HORIZONTAL);
        layoutManager.setPageListener(new PagerGridLayoutManager.PageListener() {
            @Override
            public void onPageSizeChanged(int pageSize) {
            }

            @Override
            public void onPageSelect(int pageIndex) {
                page_rg.check(pageIndex);
            }
        });
        getTbkListRequst1(search_sort, "必推精选", "服装");
        page_rg = homeTabView.findViewById(R.id.page_rg);
        layoutManager.setChangeSelectInScrolling(true);
        homeGrid.setLayoutManager(layoutManager);
// 2.设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(homeGrid);

//        homeGrid.setLayoutManager(new GridLayoutManager(context, 5));
        homeIconAdapter = new HomeIconAdapter(context, R.layout.dd, homeItems);
        homeGrid.setAdapter(homeIconAdapter);
        homeIconAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent;
                switch (homeItems.get(position).id) {
                    case "1":
                        intent = new Intent(getActivity(), BaoYouActivity.class);
                        intent.putExtra("type", "20");
                        startActivity(intent);
                        break;
                    case "2":
                        intent = new Intent(getActivity(), BaoYouActivity.class);
                        intent.putExtra("type", "9");
                        startActivity(intent);
                        break;
                    case "3":
                        intent = new Intent(getActivity(), BaoYouActivity.class);
                        intent.putExtra("type", "2");
                        startActivity(intent);
                        break;
                    case "4":
                        intent = new Intent(getActivity(), BaoYouActivity.class);
                        intent.putExtra("type", "5");
                        startActivity(intent);
                        break;
                    case "5":
                        intent = new Intent(getActivity(), BaoYouActivity.class);
                        intent.putExtra("type", "4");
                        startActivity(intent);
                        break;
                    case "6":
                        intent = new Intent(context, WebViewActivity.class); // WebViewActivity3.class);
                        intent.putExtra("title", "天猫超市");
//                        LogUtils.d("TAG", "onItemClick: "+homeItems.get(position).href);
                        intent.putExtra("url", homeItems.get(position).href);
                        startActivity(intent);
                        break;
                    case "7":
                        intent = new Intent(getActivity(), BaoYouActivity.class);
                        intent.putExtra("type", "1");
                        startActivity(intent);
                        break;
                    case "8":
                        openActivity(PHBActivity.class);
                        break;
                    case "9":
                        intent = new Intent(getActivity(), BaoYouActivity.class);
                        intent.putExtra("type", "22");
                        startActivity(intent);
                        break;
                    case "10":
                        openActivity(PddActivity.class);
                        break;
                    case "11":
                        openActivity(JdActivity.class);
                        break;
                    case "12":
                        openActivity(ShopMallActivity.class);
                        break;
                    case "13":
                        openActivity(NewClassActivity.class);
                        break;
                    case "14":
                        intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("title", "拉新活动");
                        intent.putExtra("url", Constants.APP_IP + "/wap/Rookie/index/uid/" + SPUtils.getStringData(context, Constants.UID, ""));
                        startActivity(intent);
                        break;
                    case "15":
                        openActivity(ZeroBuyActivity.class);
                        break;
                    case "16":
                        hongbao();
                        break;
                    case "17":
                        openActivity(DailyBonusActivity.class);
                        break;
                    case "18":
                        break;
                    case "19":
                        intent = new Intent(getActivity(), BaoYouActivity.class);
                        intent.putExtra("type", "23");
                        startActivity(intent);
                        break;
                    case "20":
                        intent = new Intent(context, WebViewActivityNotOpenDefaultWeb.class); // WebViewActivity3.class);
                        intent.putExtra("title", "天猫国际");
                        intent.putExtra("url", "https://pages.tmall.com/wow/jinkou/act/zhiyingchaoshi?from=zebra:offline");
                        startActivity(intent);
                        break;
                    case "21":
                        openActivity(PinPaiFragment.class);
                        break;
                    case "22":
                        intent = new Intent(context, WebViewActivity3.class);
                        intent.putExtra("title", "生活券");
                        long timeStamp = System.currentTimeMillis();
                        String signstr = "agentId=405&machineCode=" + SPUtils.getStringData(context, "uid", "1") + "&timestamp=" + timeStamp;
                        String singstrs = signstr + "&secretKey=Pe7HRBJYwQfpxCb3s5TGW3hXeWxhXKpH";
                        String sign = md5(singstrs);
                        intent.putExtra("url", "http://tq.jfshou.cn/seller/app/classify?machineCode=" + SPUtils.getStringData(context, "uid", "1") + "&agentId=405&timestamp=" + timeStamp + "&sign=" + sign);
                        startActivity(intent);
                        break;
                    case "23":
                        intent = new Intent(context, DailyBonusActivity.class);
                        startActivity(intent);
                        break;
                    case "24":
                        intent = new Intent(context, CommissionPhbActivity.class);
                        startActivity(intent);
                        break;
                    case "25":
                        intent = new Intent(context, WebViewActivityNotOpenDefaultWeb.class);//, WebViewActivity3.class);
                        intent.putExtra("title", "天猫美妆");
                        intent.putExtra("url", "https://meizhuang.tmall.com");
                        startActivity(intent);
                        break;
                    case "26":
                        intent = new Intent(context, WebViewActivity3.class);
                        intent.putExtra("title", "飞猪旅行");
                        intent.putExtra("url", "https://h5.m.taobao.com/trip/wx-random-door/index/index.html");
                        startActivity(intent);
                        break;
                    case "27":
                        Bundle bundle = new Bundle();
                        bundle.putString("id", "0");
                        openActivity(DouActivity.class, bundle);
                        break;
                    case "28":
                        Acp.getInstance(context).request(new AcpOptions.Builder()
                                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                                .build(), new AcpListener() {
                            @Override
                            public void onGranted() {
                                openActivity(JiayoulistActivity.class);
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                showToast("加油站需要您的定位权限");
                            }
                        });
                        break;
                    case "29":
                        openActivity(WphActivity.class);
                        break;
                    case "30":
                        openActivity(SuningHomeActivity.class);
                        break;
                    case "31":
                        Intent intent1 = new Intent(getActivity(), GuoyedanActivity.class);
                        startActivity(intent1);
                        break;
                    case "32":
                        Intent intent4 = new Intent(getActivity(), GaoyongActivity.class);
                        intent4.putExtra("url", "/api/Zhetaoke/getBaodanGoods");
                        intent4.putExtra("title", "捡漏神单");
                        intent4.putExtra("type", "2");
                        startActivity(intent4);
                        break;
                    case "33":
                        openActivity(ElemeActivity.class);
                        break;
                    case "34":
                        Intent intent12 = new Intent(context, WebViewActivity.class);
                        intent12.putExtra("title", "充值");
                        intent12.putExtra("url", "http://app.yangkeduo.com/deposit.html");
                        startActivity(intent12);
                        break;
                    case "35":
                        Acp.getInstance(context).request(new AcpOptions.Builder()
                                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                                .build(), new AcpListener() {
                            @Override
                            public void onGranted() {
                                openActivity(MerchanthomeActivity.class);
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                showToast("加油站需要您的定位权限");
                            }
                        });
                        break;
                    case "36":
                        if ("".equals(SPUtils.getStringData(context, "token", ""))) {
                            openActivity(WelActivity.class);
                            return;
                        }
                        intent = new Intent(context, WebViewActivity3.class);
                        intent.putExtra("title", "美团酒店");
                        intent.putExtra("url", "https://runion.meituan.com/url?a=1&key=ebf5da1e1cd2073df1e538618fca245c348&url=https://i.meituan.com/awp/h5/hotel/search/search.html?cevent=imt%2Fhomepage%2Fcategory1%2F20&sid=hkx_" + SPUtils.getStringData(context, "uid", ""));
                        startActivity(intent);
                        break;
                    case "38":
                        Acp.getInstance(context).request(new AcpOptions.Builder()
                                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                                .build(), new AcpListener() {
                            @Override
                            public void onGranted() {
                                getToken();
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                showToast("需要您的相机权限");
                            }
                        });
                        break;
                    case "39":
                        RequestParams params = new RequestParams();
                        params.put("share_code", "127SIwqxj");
                        long timeStamp2 = System.currentTimeMillis();
                        params.put("user_id", SPUtils.getStringData(context, "uid", "1"));
                        params.put("timestamp", timeStamp2);
//        params.put("secretKey","C78266CF1A8848B48ECF96C0BD5EE465");
                        Map<String, String> map = new HashMap<>();
                        map.put("shareCode", "127SIwqxj");
                        map.put("userId", SPUtils.getStringData(context, "uid", "1"));
                        map.put("timestamp", timeStamp2 + "");
//        map.put("secretKey","C78266CF1A8848B48ECF96C0BD5EE465");
                        params.put("sign", PddClient.getSign4(map, "C78266CF1A8848B48ECF96C0BD5EE465"));
                        intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("title", "肯德基");
                        intent.putExtra("url", "https://ot.jfshou.cn/api/entrance?" + params.toString());
                        startActivity(intent);
                        break;
                    case "40":
                        intent = new Intent(context, WebViewActivity3.class);
                        intent.putExtra("title", "视频充值");
                        long timeSftamp = System.currentTimeMillis();
                        String signstrd = "agentId=405&machineCode=" + SPUtils.getStringData(context, "uid", "1") + "&timestamp=" + timeSftamp;
                        String singstrsd = signstrd + "&secretKey=Pe7HRBJYwQfpxCb3s5TGW3hXeWxhXKpH";
                        String signs = md5(singstrsd);
                        intent.putExtra("url", "http://tq.jfshou.cn/seller/videoApp/classify?machineCode=" + SPUtils.getStringData(context, "uid", "1") + "&agentId=405&timestamp=" + timeSftamp + "&sign=" + signs);
                        startActivity(intent);
                        break;
                    case "41":
                        try {
                            // 以下固定写法
                            final Intent intent1e = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("alipays://platformapi/startapp?appId=20000193"));
                            intent1e.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent1e);
                        } catch (Exception e) {
                            // 防止没有安装的情况
                            e.printStackTrace();
                            ToastUtils.showLongToast(context, "请先安装支付宝");
                        }
                        break;
                    case "42":
                        getInfo("1");
                        break;
                    case "43":
                        getInfo("3");
                        break;
                    case "44":
                        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                        req.userName = "gh_5afd2977e019";
                        req.path = "pages/movie/index?scene=oM9RbtwwIAgjsuEbvCLvFGOSVGMQ";
                        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;
                        CaiNiaoApplication.api.sendReq(req);
                        break;
                    case "45":
                        if ("".equals(SPUtils.getStringData(context, "token", ""))) {
                            openActivity(WelActivity.class);
                            return;
                        }
                        AlibcLogin alibcLogin = AlibcLogin.getInstance();

                        if (CaiNiaoApplication.getUserInfoBean().user_msg.tb_rid == null || "".equals(CaiNiaoApplication.getUserInfoBean().user_msg.tb_rid) || "null".equals(CaiNiaoApplication.getUserInfoBean().user_msg.tb_rid)) {
                            openActivity(SetActivity.class);
                            ToastUtils.showLongToast(context, "请先绑定淘宝渠道");
                            return;
                        }
                        intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("title", homeItems.get(position).name);
                        intent.putExtra("url", Constants.APP_IP + "/wap/Tbk/firstorder/relation_id/" + CaiNiaoApplication.getUserInfoBean().user_msg.tb_rid);
                        startActivity(intent);
                        break;
                    default:
                        if (homeItems.get(position).href != null && !"".equals(homeItems.get(position).href)) {
                            intent = new Intent(context, WebViewActivity.class);
                            intent.putExtra("title", homeItems.get(position).name);
                            intent.putExtra("url", homeItems.get(position).href);
                            startActivity(intent);
                        }
                        break;
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        List<Item> tempList = CaiNiaoApplication.getInstances().getDaoSession() == null ? new ArrayList<>() : CaiNiaoApplication.getInstances().getDaoSession().loadAll(Item.class);
        if (tempList.size() <= 0) {
            getIndexIcon();
        } else {
            homeItems.addAll(tempList);
            int count = (homeItems.size() - 1) / 10 + 1;
            for (int i = 0; i < count; i++) {
                RadioButton rb = (RadioButton) LayoutInflater.from(context).inflate(R.layout.item_page_group, null);
                rb.setId(i);
                if (i == 0) {
                    rb.setChecked(true);
                }
                page_rg.addView(rb);
            }
            homeIconAdapter.notifyDataSetChanged();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getIndexIcon();
                }
            }, 10000);
        }
        //头部适配器
        headerAndFooterWrapper = new HeaderAndFooterWrapper(highlightsAdapter);
        //添加一个头部
        headerAndFooterWrapper.addHeaderView(homeTabView);
        homeRecyclerView.setAdapter(headerAndFooterWrapper);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.autoRefresh();
                getBanner();
//                getAD();
                getHomeADimg();
            }
        }, 200);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                homeRecyclerView.scrollToPosition(0);
            }
        }, 500);
    }

    com.alibaba.fastjson.JSONArray adv_config = new com.alibaba.fastjson.JSONArray();
    JuDuoHuiAdvertisement juDuoHuiAdvertisement = null;

    // 获取广告配置
    private void getAdvConfig() {
        RequestParams req = new RequestParams();
        req.put("identifys", "article_list");
        HttpUtils.post(Constants.GET_ADVERTISEMENT_LIST, HomeFirstFragment.this, req, new HttpJsonResponse() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);

            }

            @Override
            protected void onSuccess(com.alibaba.fastjson.JSONObject response) {
                super.onSuccess(response);
                if (response.getIntValue("code") == 0) {
                    for (String key : response.getJSONObject("data").getJSONObject("place_list").keySet()) {
                        com.alibaba.fastjson.JSONObject tmp = response.getJSONObject("data").getJSONObject("place_list").getJSONObject(key);
                        if ("article_list".equals(tmp.getString("identify"))) {
                            adv_config = tmp.getJSONArray("list");
                        }
                    }
                    juDuoHuiAdvertisement = new JuDuoHuiAdvertisement(getActivity(), null);
                    juDuoHuiAdvertisement.setInfomationAdListen(new JuDuoHuiAdvertisement.InfomationAdListen() {
                        @Override
                        public void click(View v) {

                        }

                        @Override
                        public void dislike() {

                        }

                        @Override
                        public void display(View v, String position, com.alibaba.fastjson.JSONObject config) {
                            mActivity.runOnUiThread(() -> {
                                list_adv.setVisibility(View.VISIBLE);
                                list_adv.removeAllViews();
                                list_adv.addView(v);
                            });
                        }

                        @Override
                        public void displayed() {

                        }

                        @Override
                        public void close() {

                        }

                        @Override
                        public void error(com.alibaba.fastjson.JSONObject error) {
                            getActivity().runOnUiThread(() -> {
                                juDuoHuiAdvertisement.getInfomationAdv(adv_config, error.getString("position"));
                            });
                        }
                    });

                    juDuoHuiAdvertisement.getInfomationAdv(adv_config, "my_ads");

                } else {
                    // Log.d(TAG, "onSuccess: 获取广告配置失败");
                }
            }
        });
    }

    public static String getSign4(Map<String, String> map) {
        map = PddClient.sortMapByKey2(map);
        String temp = "";
        for (String key : map.keySet()) {
            temp += (key + "=" + map.get(key) + "&");
        }
        return getSign(temp.substring(0, temp.length() - 1));
    }

    private void getToken() {
        RequestParams requestParams = new RequestParams();
        long time = new Date().getTime();
        requestParams.put("platformCode", "920062401");
        requestParams.put("timestamp", time + "");
        requestParams.put("phone", SPUtils.getStringData(context, "phone", ""));
        requestParams.put("seq", time + "");
        Map<String, String> map = new HashMap<>();
        map.put("platformCode", "920062401");
        map.put("timestamp", time + "");
        map.put("seq", time + "");
        map.put("phone", SPUtils.getStringData(context, "phone", ""));
        requestParams.put("sig", getSign4(map));
        HttpUtils.post("https://tch.fleetingpower.com/api/v1/queryUserToken/", HomeFirstFragment.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject o = new JSONObject(responseString);
                    if (0 == o.getInt("ret")) {
                        String token = o.getJSONObject("data").getString("token");
                        getPosition(token);
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        });
    }

    // 声明定位回调监听器
    Double longitudestr = 0.0;
    String tokens = "";
    Double latitudestr = 0.0;
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            closeLoadingDialog();
            if (amapLocation == null) {
//                Log.i(TAG, "amapLocation is null!");
                return;
            }
            if (amapLocation.getErrorCode() != 0) {
                ToastUtils.showLongToast(context, "定位失败，请检查设备是否开启定位服务");
                return;
            }
            Double longitude = amapLocation.getLongitude();//获取经度
            Double latitude = amapLocation.getLatitude();//获取纬度
            longitudestr = longitude;
            latitudestr = latitude;
            getUrl();
//            if (Content.wds == 0.0&& Content.jds == 0.0){
//
//            }else {
//
//            }
//            Log.i(TAG, "longitude:" + longitude + ",latitude：" + latitude+"\n"+"记录的："+Content.jds+","+Content.wds);
        }
    };

    private void getUrl() {
        RequestParams requestParams = new RequestParams();
        long time = new Date().getTime();
        requestParams.put("platformCode", "920062401");
        requestParams.put("timestamp", time + "");
        requestParams.put("userLatStr", latitudestr + "");
        requestParams.put("userLngStr", longitudestr + "");
        requestParams.put("seq", time + "");
        requestParams.put("token", tokens);
        Map<String, String> map = new HashMap<>();
        map.put("platformCode", "920062401");
        map.put("timestamp", time + "");
        map.put("seq", time + "");
        map.put("token", tokens);
        map.put("userLatStr", latitudestr + "");
        map.put("userLngStr", longitudestr + "");
        requestParams.put("sig", getSign4(map));
        HttpUtils.post1("https://tch.fleetingpower.com/api/v1/getStationListUrl/", HomeFirstFragment.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject o = new JSONObject(responseString);
                    if (0 == o.getInt("ret")) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("title", "充电站");
                        intent.putExtra("url", o.getJSONObject("data").getString("url"));
                        intent.putExtra("ua", o.getJSONObject("data").getString("seq"));
                        startActivity(intent);
//                        String token=o.getJSONObject("data").getString("token");
//                        getPosition(token);
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        });
    }

    //声明AMapLocationClient类对象
    AMapLocationClient mLocationClient = null;
    // 声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    public void getPosition(String token) {
        tokens = token;
        showLoadingDialog("定位中");
        //初始化定位
        mLocationClient = new AMapLocationClient(context);
        // 设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        // 初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        // 设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(60000 * 5);
        // 获取一次定位结果： //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        mLocationOption.setOnceLocationLatest(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 启动定位
        mLocationClient.startLocation();
    }

    /* 密钥内容 base64 code */
    private static String PUCLIC_KEY =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDdSA1k2MJd6ePph761YUgLrh48\n" +
                    "wIUWfvPmcy14ZE8v0cZhtwk02gNyE2rBQ/3HAnCVg8+CWyXneitIoe8e6ldiuzNx\n" +
                    "Y44hq90/nEOsmkzWdyPsrQybmd1Vb/HF/17F4nec3gXw6ZICWMLH/qUY9nPTGQZi\n" +
                    "v5vXoPtBVCgXrZ3yvwIDAQAB\n";
    private static String PRIVATE_KEY =
            "MIICXQIBAAKBgQDdSA1k2MJd6ePph761YUgLrh48wIUWfvPmcy14ZE8v0cZhtwk0\n" +
                    "2gNyE2rBQ/3HAnCVg8+CWyXneitIoe8e6ldiuzNxY44hq90/nEOsmkzWdyPsrQyb\n" +
                    "md1Vb/HF/17F4nec3gXw6ZICWMLH/qUY9nPTGQZiv5vXoPtBVCgXrZ3yvwIDAQAB\n" +
                    "AoGAL+Xq0EuDNyTrqp8xjr1yBOU5sljR9h6g2N9Rll/QLD+yO3CNU51lZYoCb7cx\n" +
                    "9aP3jsWrY0IroEF3oQ88XWrIYzWQS7kSz47hyeu7bqfIshMs+kajsxf9yJn7P6lp\n" +
                    "sDJLrmHiqrSXx74wLpnKcWqAk4uTHeYh966Di4Q461pBRTECQQD5PDRDrhsQcNdl\n" +
                    "b/Y2HcX2mivJ0FC+LtywtWnwg0ZDyW7FHoleFqXoYN0s2i7bH4x3dJ+8EpE/nJjZ\n" +
                    "N8GF899NAkEA40mdCDmqr8FH4mtvl9fVNJTqb7Sp7glXJlEnrT2RO6V4POecQJ5T\n" +
                    "/npNGSni+sZA2ovspC8MaHUtztg8HIlsOwJBAM+pPu7JSRmIu590CxQJ+KDA0g1D\n" +
                    "+ZKMnyrI7O0No+TlF9s71z7C5hdZZc9yNyox/iqlzFW6rrTuuFf8Yc1HZd0CQQCy\n" +
                    "7p8LfKqdVuJKpB3kQmx8yseNTYOB/CR56+X6gr+1X107RXNDg+HIM2xC5TDmD/G6\n" +
                    "m/Geh9OS4L1BXAZmyOFFAkBUhuvotV77f32CAdXj4b7BCP4+gEHz6Nldegi9tc9T\n" +
                    "h5uWGkqtjKlWU48Cv2/gt2Ownjm86PU3n4N6b8dyRWE3";

    private static String getSign(String source) {
        String sign = "";
        try {
            RSAPrivateKey publicKey = SignUtils.loadPrivateKeyByStr(PRIVATE_KEY);
            sign = SignUtils.sign(source, publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sign;
    }

    private void getInfo(String type) {

        if ("".equals(SPUtils.getStringData(getContext(), "token", ""))) {
            //T.showShortBottom(this.getContext(),"用户不存在");
            openActivity(WelActivity.class);
            return;
        }

        RequestParams requestParams = new RequestParams();
        requestParams.put("type", type);
        HttpUtils.post(Constants.APP_IP + "/api/MeiTuan/getQudaoLinkUrl", HomeFirstFragment.this, requestParams, new TextHttpResponseHandler() {
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
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("title", "1".equals(type) ? "美团外卖" : "美团团购");
                        intent.putExtra("url", object.getString("data"));
                        startActivity(intent);
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private synchronized void getTbkListRequst1(String sort, final String title, String search) {
        RequestParams requestParams = new RequestParams();
        LogUtils.d("TAG", "getTbkListRequst1: url:" + "http://v2.api.haodanku.com/fastbuy/apikey/dmooo/hour_type/" + hour_type + "/min_id/1");
        HttpUtils.get(HomeFirstFragment.this, "http://v2.api.haodanku.com/fastbuy/apikey/dmooo/hour_type/" + hour_type + "/min_id/1", requestParams, new TextHttpResponseHandler() {
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
                    JSONObject object = new JSONObject(responseString);
                    if ("1".equals(object.getString("code"))) {
                        JSONArray array = object.getJSONArray("data");
                        todayHighlightsBeans2.clear();
                        for (int i = 0; i < array.length(); i++) {
                            todayHighlightsBeans2.add(new Gson().fromJson(array.getJSONObject(i).toString(), HaoDanBean.class));
                        }
                        highlightsAdapter2.notifyDataSetChanged();
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void getDous() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("back", 5);
        requestParams.put("min_id", "1");
        requestParams.put("cat_id", 0);
        HttpUtils.post(Constants.GET_DOU_LIST, HomeFirstFragment.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(throwable.getMessage());
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
                    JSONObject object = new JSONObject(responseString);
                    if ("1".equals(object.getString("code"))) {
                        JSONArray array = object.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            douList.add(new Gson().fromJson(array.getJSONObject(i).toString(), HaoDanBean.class));
                        }
                        homeDouAdapter.notifyDataSetChanged();
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result.toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    class BannerViewHolder implements MZViewHolder<String> {
        private ImageView mImageView;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.img_t, null);
            mImageView = view.findViewById(R.id.img);
            return view;
        }

        @Override
        public void onBind(Context context, int position, final String data) {
            Glide.with(context).load(data).into(mImageView);
        }
    }

    private void getTbActivity() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.TBACTIVITY, HomeFirstFragment.this, requestParams, new onOKJsonHttpResponseHandler<BannerBean>(new TypeToken<Response<BannerBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(throwable.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Response<BannerBean> datas) {
                if (datas.isSuccess()) {
                    acBanner.addAll(datas.getData().getList());
                    if (acBanner.size() <= 0) {
                        homeTabView.findViewById(R.id.ll_tm).setVisibility(View.GONE);
                    }
                    tbActivityAdapter.notifyDataSetChanged();
                } else {
                    showToast(datas.getMsg());
                }
            }
        });
    }

    /**
     * @属性:获取屏幕高度的方法
     * @开发者:陈飞
     * @时间:2018/8/7 14:22
     */
    public long getScollYDistance() {
        int position = linearLayoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = linearLayoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }

    private void addListener() {
//        scrollBanner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (images2.size() > 0) {
//                    Intent intent = new Intent(context, NewsActivity.class);
//                    intent.putExtra("title", images2.get(scrollBanner.getCurrentPosition() == images2.size() - 1 ? 0 : scrollBanner.getCurrentPosition() + 1).getTitle());
//                    intent.putExtra("article_id", images2.get(scrollBanner.getCurrentPosition() == images2.size() - 1 ? 0 : scrollBanner.getCurrentPosition() + 1).getArticle_id());
//                    startActivity(intent);
//                }
//            }
//        });
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if ("1".equals(images.get(position).getType())) {
                    if ("".equals(images.get(position).getHref())) {
                        return;
                    }
                    if (images.get(position).getHref() != null) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("title", images.get(position).getTitle());
                        intent.putExtra("url", images.get(position).getHref());
                        startActivity(intent);
                    }
                } else if ("2".equals(images.get(position).getType())) {
                    Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.taobao.taobao");
                    if (intent != null) {
                        //获取剪贴板管理器：
                        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        // 创建普通字符型ClipData
                        ClipData mClipData = ClipData.newPlainText("Label", images.get(position).getType_value());
                        // 将ClipData内容放到系统剪贴板里。
                        cm.setPrimaryClip(mClipData);
                        startActivity(intent);
                        return;
                    } else {
                        T.showShort(context, "未安装淘宝客户端");
                    }
                } else if ("3".equals(images.get(position).getType())) {
                    Uri uri = Uri.parse(images.get(position).getType());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else if ("4".equals(images.get(position).getType())) {
                    Uri uri = Uri.parse(images.get(position).getType());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    return;
                } else if ("6".equals(images.get(position).getType())) {
                    Intent intent = new Intent(context, WebViewActivity2.class);
                    intent.putExtra("title", "年货节");
                    intent.putExtra("url", "");
                    startActivity(intent);
                    return;
                } else if ("7".equals(images.get(position).getType()) || "8".equals(images.get(position).getType())) {
                    hongbao();
                    return;
                } else if ("9".equals(images.get(position).getType())) {
                    Bundle bundle = new Bundle();
                    bundle.putString("num_iid", images.get(position).getType_value());
                    openActivity(PromotionDetailsActivity.class, bundle);
                    return;
                } else if ("10".equals(images.get(position).getType())) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("title", "拉新活动");
                    intent.putExtra("url", Constants.APP_IP + "/wap/Rookie/index/uid/" + SPUtils.getStringData(context, Constants.UID, ""));
                    startActivity(intent);
                    return;
                } else if ("11".equals(images.get(position).getType())) {
                    openActivity(ZeroBuyActivity.class);
                    return;
                } else if ("12".equals(images.get(position).getType())) {
                    Bundle bundle = new Bundle();
                    bundle.putString("type_value", images.get(position).getType_value());
                    openActivity(XuanpinkActivity.class, bundle);
                    return;
                } else if ("13".equals(images.get(position).getType())) {
                    //进洞
                    openActivity(JdActivity.class);
                    return;
                } else if ("16".equals(images.get(position).getType())) {
                    //进洞
                    openActivity(ElemeActivity.class);
                    return;
                } else if ("17".equals(images.get(position).getType())) {
                    try {
                        Intent intent1 = new Intent(context, NewShuanshierActivity.class);
                        intent1.putExtra("img", Constants.APP_IP + images.get(position).detail_img);
                        intent1.putExtra("title", images.get(position).getTitle());
                        intent1.putExtra("text", images.get(position).text);
                        intent1.putExtra("tbuid", CaiNiaoApplication.getUserInfoBean().user_msg.tb_rid);
                        intent1.putExtra("hdid", images.get(position).getType_value());
                        startActivity(intent1);
                    } catch (Exception e) {

                    }
                } else if ("14".equals(images.get(position).getType())) {
//                    openActivity(XuanpinkActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", images.get(position).getTitle());
                    bundle.putString("material_id", images.get(position).getType_value());
                    openActivity(TianMaoShuangShiyiActivity.class, bundle);
                    return;
                } else if ("15".equals(images.get(position).getType())) {
                    Acp.getInstance(context).request(new AcpOptions.Builder()
                            .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                            .build(), new AcpListener() {
                        @Override
                        public void onGranted() {
                            openActivity(JiayoulistActivity.class);
                        }

                        @Override
                        public void onDenied(List<String> permissions) {
                            showToast("加油站需要您的定位权限");
                        }
                    });
                    return;
                } else if ("18".equals(images.get(position).getType())) {
                    // 新跳转
                    try {
                        com.alibaba.fastjson.JSONObject json_m = com.alibaba.fastjson.JSONObject.parseObject(images.get(position).getHref());
                        JuduohuiMessage message1 = new JuduohuiMessage();
                        message1.putMessage(json_m);
                        EventBus.getDefault().post(message1);
                    } catch (Exception t) {
                        t.printStackTrace();
                    }
                }
            }
        });
        banner2.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {
                if ("1".equals(images3.get(position).getType())) {
                    if ("".equals(images3.get(position).getHref())) {
                        return;
                    }
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("title", images3.get(position).getTitle());
                    intent.putExtra("url", images3.get(position).getHref());
                    startActivity(intent);
                } else if ("17".equals(images3.get(position).getType())) {
                    try {
                        Intent intent1 = new Intent(context, NewShuanshierActivity.class);
                        intent1.putExtra("img", Constants.APP_IP + images3.get(position).detail_img);
                        intent1.putExtra("title", images3.get(position).getTitle());
                        intent1.putExtra("text", images3.get(position).text);
                        intent1.putExtra("tbuid", CaiNiaoApplication.getUserInfoBean().user_msg.tb_rid);
                        intent1.putExtra("hdid", images3.get(position).getType_value());
                        startActivity(intent1);
                    } catch (Exception e) {

                    }
                } else if ("2".equals(images3.get(position).getType())) {
                    Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.taobao.taobao");
                    if (intent != null) {
                        //获取剪贴板管理器：
                        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        // 创建普通字符型ClipData
                        ClipData mClipData = ClipData.newPlainText("Label", images3.get(position).getType_value());
                        // 将ClipData内容放到系统剪贴板里。
                        cm.setPrimaryClip(mClipData);
                        startActivity(intent);
                        return;
                    } else {
                        T.showShort(context, "未安装淘宝客户端");
                    }
                } else if ("3".equals(images3.get(position).getType())) {
                    Uri uri = Uri.parse(images3.get(position).getType());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else if ("4".equals(images3.get(position).getType())) {
                    Uri uri = Uri.parse(images3.get(position).getType());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    return;
                } else if ("6".equals(images3.get(position).getType())) {
                    Intent intent = new Intent(context, WebViewActivity2.class);
                    intent.putExtra("title", "年货节");
                    intent.putExtra("url", "");
                    startActivity(intent);
                    return;
                } else if ("7".equals(images3.get(position).getType()) || "8".equals(images3.get(position).getType())) {
                    hongbao();
                    return;
                } else if ("16".equals(images3.get(position).getType())) {
                    //进洞
                    openActivity(ElemeActivity.class);
                    return;
                } else if ("9".equals(images3.get(position).getType())) {
                    Bundle bundle = new Bundle();
                    bundle.putString("num_iid", images3.get(position).getType_value());
                    openActivity(PromotionDetailsActivity.class, bundle);
                    return;
                } else if ("10".equals(images3.get(position).getType())) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("title", "拉新活动");
                    intent.putExtra("url", Constants.APP_IP + "/wap/Rookie/index/uid/" + SPUtils.getStringData(context, Constants.UID, ""));
                    startActivity(intent);
                    return;
                } else if ("11".equals(images3.get(position).getType())) {
                    openActivity(ZeroBuyActivity.class);
                    return;
                } else if ("12".equals(images3.get(position).getType())) {
                    openActivity(XuanpinkActivity.class);
                    return;
                } else if ("13".equals(images3.get(position).getType())) {
                    //进洞
                    openActivity(JdActivity.class);
                    return;
                } else if ("14".equals(images3.get(position).getType())) {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", images3.get(position).getTitle());
                    bundle.putString("material_id", images3.get(position).getType_value());
                    openActivity(TianMaoShuangShiyiActivity.class, bundle);
                    return;
                } else if ("15".equals(images3.get(position).getType())) {
                    Acp.getInstance(context).request(new AcpOptions.Builder()
                            .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                            .build(), new AcpListener() {
                        @Override
                        public void onGranted() {
                            openActivity(JiayoulistActivity.class);
                        }

                        @Override
                        public void onDenied(List<String> permissions) {
                            showToast("加油站需要您的定位权限");
                        }
                    });
                    return;
                }
            }
        });
        banner.setDelayTime(5000);
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (images.get(position).getColor() == null || "".equals(images.get(position).getColor())) {
                        images.get(position).setColor("#FF0000");
                    }
                    homeBg.getDrawable().setTint(Color.parseColor(images.get(position).getColor()));
                    SPUtils.saveStringData(context, "color", images.get(position).getColor());
                    refreshLayout.setPrimaryColors(Color.parseColor(images.get(position).getColor()), Color.WHITE);
                    MessageEvent event = new MessageEvent(images.get(position).getColor());
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                switch (Home_Search) {
                    case 0:
                        if (hasdata) {
                            refreshStatus = 0;
                            indexNum++;
                            getTbkListRequst(search_sort, "必推精选", "服装");
                        } else {
                            showToast("没有更多数据了");
                            refreshLayout.finishLoadMore();
                        }
                        break;
                    case 1:
                        if (hasdata) {
                            refreshStatus = 0;
                            indexNum++;
                            getTbkListRequst(search_sort, "", search_content);
                        } else {
                            showToast("没有更多数据了");
                            refreshLayout.finishLoadMore();
                        }
                        break;
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                // Log.d(TAG,"onRefresh: 刷新页面");
                if (refreshNum != 0) {
                    getAdvConfig();
                }
                refreshNum++;
                indexNum = 1;
                hasdata = true;
                switch (Home_Search) {
                    case 0: //首页
                        refreshStatus = 1;
                        taobaoGuesChildtBeans.clear();
                        getTbkListRequst("tk_total_sales_des", "必推精选", "服装");
                        break;
                    case 1: //搜索页
                        refreshStatus = 1;
                        getTbkListRequst(search_sort, "", search_content);
                        break;
                }
            }
        });
        homeRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                if (getScollYDistance() > bgHead2.getMeasuredHeight()) {
//                    bgHead2.setVisibility(View.VISIBLE);
//                    bgHead2.getBackground().mutate().setAlpha(255);
//                } else {
//                    if (getScollYDistance() == 0) {
//                        bgHead2.setVisibility(View.INVISIBLE);
//                        return;
//                    }
//                    bgHead2.setVisibility(View.VISIBLE);
//                    bgHead2.getBackground().mutate().setAlpha((int) (getScollYDistance() * 1.00 * 255 / bgHead2.getMeasuredHeight()));
//                }
            }
        });
    }

    private void hongbao() {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        RequestParams requestParams = new RequestParams();
        requestParams.put("data_type", PddClient.data_type);
        requestParams.put("version", PddClient.version);
        requestParams.put("timestamp", time);
        requestParams.put("token", SPUtils.getStringData(context, "token", ""));
        requestParams.put("type", "hkx.UserBalanceRecord.receiveBonus");
        Map<String, String> temp = new HashMap<>();
        temp.put("data_type", PddClient.data_type);
        temp.put("version", PddClient.version);
        temp.put("timestamp", time);
        temp.put("token", SPUtils.getStringData(context, "token", ""));
        temp.put("type", "hkx.UserBalanceRecord.receiveBonus");
        String sign = PddClient.getSign1(temp);
        requestParams.put("sign", sign);
        HttpUtils.post(Constants.HONGBAO, HomeFirstFragment.this, requestParams, new TextHttpResponseHandler() {
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
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    Intent intent = new Intent(context, QdActivity.class);
                    intent.putExtra("money", jsonObject.getString("money"));
                    SPUtils.saveIntData(context, "hongbao", 1);
                    if (code == 0) {
                        intent.putExtra("mess", "新年红包已存至余额~");
                    } else {
                        intent.putExtra("mess", msg);
                    }
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void lazyload() {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_title_content://搜索
                openActivity(SearchActivity.class);
                break;
            case R.id.ll_home_one:
                openActivity(ZeroBuyActivity.class);
                break;
            case R.id.ll_home_seven://过夜单
                Intent intent1 = new Intent(getActivity(), GuoyedanActivity.class);

                startActivity(intent1);
//                intent = new Intent(getActivity(), BaoYouActivity.class);
//                intent.putExtra("type", "6");
//                startActivity(intent);
                break;
            case R.id.ll_home_eight://捡漏神单
                Intent intent4 = new Intent(getActivity(), GaoyongActivity.class);
                intent4.putExtra("url", "/api/Zhetaoke/getBaodanGoods");
                intent4.putExtra("title", "捡漏神单");
                intent4.putExtra("type", "2");
                startActivity(intent4);
//                showToast( "敬请期待!" );
                break;
            case R.id.ll_home_two:
                openActivity(PHBActivity.class);
                break;
            case R.id.ll_home_three:
                intent = new Intent(getActivity(), BaoYouActivity.class);
                intent.putExtra("type", "4");
                startActivity(intent);
                break;
            case R.id.ll_home_four:
                intent = new Intent(getActivity(), BaoYouActivity.class);
                intent.putExtra("type", "22");
                startActivity(intent);
                break;
            case R.id.ll_home_five:
                intent = new Intent(getActivity(), BaoYouActivity.class);
                intent.putExtra("type", "2");
                startActivity(intent);
                break;
            case R.id.ll_home_six:
                intent = new Intent(getActivity(), BaoYouActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Log.d(TAG, "onStart: ");
        if (banner != null) banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        // Log.d(TAG, "onStop: ");
        banner.releaseBanner();
    }

    @Override
    protected void ReceiverBroadCastMessage(String status, String result, Serializable serializable, Intent intent) {
        super.ReceiverBroadCastMessage(status, result, serializable, intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Log.d(TAG, "onDestroy: ");
    }

    /**
     * @属性:获取推荐商品列表
     * @开发者:WMM
     * @时间:2018/11/20 17:05
     */
    private synchronized void getTbkListRequst(String sort, final String title, String search) {

        if (aCache.getAsString(title + "_last_min_id") != null) {
            min_id = aCache.getAsString(title + "_last_min_id");
        }

        RequestParams requestParams = new RequestParams();
        requestParams.put("type", "1");
        requestParams.put("cid", "0");
        requestParams.put("min_id", min_id);
        HttpUtils.post(Constants.GET_NINEBY_NEW_HD, HomeFirstFragment.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                showToast(throwable.getMessage());
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject object = new JSONObject(responseString);
                    if ("1".equals(object.getString("code"))) {
                        JSONArray array = object.getJSONArray("data");
                        if (min_id.equals("1")) {
                            taobaoGuesChildtBeans.clear();
                        }
                        todayHighlightsBeans.clear();
                        min_id = object.getString("min_id");
                        aCache.put(title + "_last_min_id", min_id, Constants.CacheSaveTime);
                        if (array.length() <= 0) {
                            hasdata = false;
                            return;
                        }
                        for (int i = 0; i < array.length(); i++) {
                            taobaoGuesChildtBeans.add(new Gson().fromJson(array.getJSONObject(i).toString(), HaoDanBean.class));
                        }
                        TodayHighlightsBean2 todayHighlightsBean = new TodayHighlightsBean2();
                        todayHighlightsBean.setTitle(title);
                        todayHighlightsBean.setList(taobaoGuesChildtBeans);
                        todayHighlightsBeans.add(todayHighlightsBean);
                        headerAndFooterWrapper.notifyDataSetChanged();
                        if (array.length() <= 0) {
                            hasdata = false;
                        }
                    } else {
                        if (!min_id.equals("1")) {
                            min_id = "1";
                            aCache.put(title + "_last_min_id", min_id, Constants.CacheSaveTime);
                            getTbkListRequst(sort, title, search);
                        } else {
                            showToast(object.getString("msg"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void getHomeADimg() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("cat_id", 3);
        HttpUtils.post(Constants.GET_BANNER, HomeFirstFragment.this, requestParams, new onOKJsonHttpResponseHandler<BannerBean>(new TypeToken<Response<BannerBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(throwable.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Response<BannerBean> datas) {
                if (datas.isSuccess()) {
                    images3.clear();
                    images3.addAll(datas.getData().getList());
                    List<String> strings = new ArrayList<>();
                    for (int i = 0; i < images3.size(); i++) {
                        if (images3.get(i).getImg().startsWith("http")) {
                            strings.add(images3.get(i).getImg());
                        } else {
                            strings.add(Constants.APP_IP + images3.get(i).getImg());
                        }
                    }
                    if (images3.size() <= 0) {
                        banner2.setVisibility(View.GONE);
                    } else {
                        banner2.setVisibility(View.VISIBLE);
                        banner2.setPages(strings, new MZHolderCreator<BannerViewHolder>() {
                            @Override
                            public BannerViewHolder createViewHolder() {
                                return new BannerViewHolder();
                            }
                        });
                        banner2.start();
                    }
                } else {
//                    showToast(datas.getMsg());
                }
            }
        });
    }

    private void getAD() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("cat_id", 4);
        HttpUtils.post(Constants.MESSAGE_ARTICLE_GETARTICLELIST_URL, HomeFirstFragment.this, requestParams, new onOKJsonHttpResponseHandler<MessageCenterBean>(new TypeToken<Response<MessageCenterBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(throwable.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Response<MessageCenterBean> datas) {
                if (datas.isSuccess()) {
                    if (datas.getData().getList().size() <= 0) {
                        return;
                    }
                    images2.clear();
                    images2.addAll(datas.getData().getList());
                    List<String> strings = new ArrayList<>();
                    for (int i = 0; i < images2.size(); i++) {
                        strings.add(images2.get(i).getTitle());
                    }
                    if (images2.size() > 0)
                        strings.add(images2.get(0).getTitle());
//                    scrollBanner.setList(strings);
//                    scrollBanner.startScroll();
                } else {
                    showToast(datas.getMsg());
                }
            }
        });
    }

    private void tankuang() {
        final AdInfo adInfo = new AdInfo();
        List<AdInfo> adInfos = new ArrayList<>();
        final AdManager adManager = new AdManager(getActivity(), adInfos);
        adInfo.setActivityImg(Constants.APP_IP + "/static/images/chunjie.png");
        adInfo.setUrl("");
        adInfo.setTitle("");
        adInfos.add(adInfo);
        adManager.setOnImageClickListener(new AdManager.OnImageClickListener() {
            @Override
            public void onImageClick(View view, AdInfo advInfo) {
                hongbao();
                adManager.dismissAdDialog();
            }
        }).setOnCloseClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        adManager.setOverScreen(true)
                .setPageTransformer(new ZoomOutTranformer()).showAdDialog(AdConstant.ANIM_UP_TO_DOWN);
    }

    private void updateData(List<Item> list) {
        CaiNiaoApplication.getInstances().getDaoSession().deleteAll(Item.class);
        for (int i = 0; i < list.size(); i++) {
            CaiNiaoApplication.getInstances().getDaoSession().getItemDao().insert(list.get(i));
        }
//        homeItems.clear();
//        homeItems.addAll(list);
//        int count = (homeItems.size() - 1) / 10 + 1;
//        page_rg.removeAllViews();
//        for (int i = 0; i < count; i++) {
//            RadioButton rb = (RadioButton) LayoutInflater.from(context).inflate(R.layout.item_page_group, null);
//            rb.setId(i);
//            if (i == 0) {
//                rb.setChecked(true);
//            }
//            page_rg.addView(rb);
//        }
//        homeIconAdapter.notifyDataSetChanged();
    }

    private void getIndexIcon() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.SET, HomeFirstFragment.this, requestParams, new onOKJsonHttpResponseHandler<SetBean>(new TypeToken<Response<SetBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(throwable.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Response<SetBean> datas) {
                if (datas.isSuccess()) {
                    if (homeItems.size() == 0) {
                        homeItems.addAll(datas.getData().moduleList);
                        int count = (homeItems.size() - 1) / 10 + 1;
                        for (int i = 0; i < count; i++) {
                            RadioButton rb = (RadioButton) LayoutInflater.from(context).inflate(R.layout.item_page_group, null);
                            rb.setId(i);
                            if (i == 0) {
                                rb.setChecked(true);
                            }
                            page_rg.addView(rb);
                        }
                        homeIconAdapter.notifyDataSetChanged();
                    }
                    updateData(datas.getData().moduleList);
                } else {
                    showToast(datas.getMsg());
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK && requestCode == 5) {
            String temp = data.getStringExtra(Intents.Scan.RESULT);
            if (!"".equals(temp)) {
                LogUtils.d("TAG", "onActivityResult: 是否走这里了？");
                if (temp.contains("mobile.yangkeduo.com")) {
                    Uri uri = Uri.parse(temp);
                    if (uri.getQueryParameter("goods_id") != null && !"".equals(uri.getQueryParameter("goods_id"))) {
                        getPddDetail(uri.getQueryParameter("goods_id"));
                    }
                } else if (temp.contains("item.m.jd.com/product")) {
                    getJdGoodsRequest(StringUtils.substringBetween(temp, "product/", ".html"));
                } else {
                    getGoodsMsgRequest(temp);
                }
            }
        }
    }

    /**
     * @属性:获取拼多多商品详情
     * @开发者:wmm
     * @时间:2018/11/22 9:00
     */
    private void getPddDetail(final String goods_id) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("goods_id", goods_id);
        HttpUtils.post(Constants.GET_PDD_DETAIL, HomeFirstFragment.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
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
                try {
                    if (new JSONObject(responseString).getInt("code") != 0) {
                        T.showShort(context, "该商品没有优惠券或奖");
                        return;
                    }
                    DecimalFormat df = new DecimalFormat("0.00");
                    JSONObject object = new JSONObject(responseString).getJSONObject("data").getJSONObject("goods_details");
                    Intent intent = new Intent(context, DialogActivity.class);
                    Gson gson = new Gson();
                    double tem = (Double.valueOf(object.getString("min_group_price")) - Double.valueOf(object.getString("coupon_discount"))) * Double.valueOf(df.format(Double.valueOf(object.getString("promotion_rate")) / 1000));
                    object.put("commission", df.format(tem * SPUtils.getIntData(context, "rate", 0) / 100));
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("goods", gson.fromJson(object.toString().replace("goods_gallery_urls", "imagss"), PDDBean.class));
                    intent.putExtra("goods", bundle);
                    intent.putExtra("pic", object.getString("goods_thumbnail_url"));
                    intent.putExtra("title", object.getString("goods_name"));
                    intent.putExtra("commission", "");
                    intent.putExtra("type", "pdd");
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * @属性:获取京东推送商品详情
     * @开发者:wmm
     * @时间:2018/12/11 9:50
     */
    private Handler handlers = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            T.showShort(context, "该商品没有优惠券或奖");
            super.handleMessage(msg);
        }
    };

    private void getJdGoodsRequest(String id) {
//        JAnalyticsInterface.onEvent(this,new CountEvent("jd_copy_search_lq"));
        String SERVER_URL = "https://router.jd.com/api";
        String appKey = "094f4eeba5cd4108ba1cd4e7a6d93cc6";
        String appSecret = "852e6f678c3b4e27a89cc9dc6e67ee01";
        String accessToken = "";
        final JdClient client = new DefaultJdClient(SERVER_URL, accessToken, appKey, appSecret);
        final UnionOpenGoodsQueryRequest request = new UnionOpenGoodsQueryRequest();
        final GoodsReq goodsReq = new GoodsReq();
        goodsReq.setSkuIds(new Long[]{Long.valueOf(id)});
        goodsReq.setPageSize(6);
        goodsReq.setPageIndex(1);
        request.setGoodsReqDTO(goodsReq);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    UnionOpenGoodsQueryResponse response = client.execute(request);
                    if (response.getData() == null) {
                        handlers.sendEmptyMessage(0);
                        return;
                    }
                    if (response.getData().length <= 0) {
                        handlers.sendEmptyMessage(0);
                        return;
                    }
                    try {
                        Intent intent = new Intent(context, DialogActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("goods", response.getData()[0]);
                        intent.putExtra("goods", bundle);
                        intent.putExtra("pic", response.getData()[0].getImageInfo()[0].getImageList()[0].getUrl());
                        intent.putExtra("title", response.getData()[0].getSkuName());
                        intent.putExtra("commission", "");
                        intent.putExtra("type", "jd");
                        startActivity(intent);
                    } catch (Exception e) {
                        LogUtils.d("jddddj", "3" + e.toString());

                    }
                } catch (JdException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getGoodsMsgRequest(final String tkl) {
        LogUtils.d("TAG", "getGoodsMsgRequest: HomeFirstFragment");
        String url = "";
        RequestParams requestParams = new RequestParams();
        requestParams.put("tkl", tkl);
        HttpUtils.post(Constants.SEARCHTKL, HomeFirstFragment.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
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
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject object = new JSONObject(responseString);
                    if (object.getInt("code") == 0) {
                        object = object.getJSONObject("data");
                        Intent intent = new Intent(context, DialogActivity.class);
                        intent.putExtra("url", tkl);
                        intent.putExtra("num_iid", object.getString("num_iid"));
                        intent.putExtra("commission", "");
                        intent.putExtra("type", "tb");
//                        startActivity(intent);
                        getDetail(object.getString("num_iid"), intent);
                    } else {
                        if (tkl.startsWith("【") && tkl.contains("http") && tkl.contains("¥")) {
                            //获取剪贴板管理器：
                            Bundle bundle = new Bundle();
                            bundle.putString("content", tkl.substring(tkl.indexOf("【") + 1, tkl.lastIndexOf("】")));
                            bundle.putInt("type", 0);//1高级搜索 0 普通搜索
                            openActivity(SearchResultActivity.class, bundle);
                            showToast(object.getString("msg"));
                        } else {
                            Intent intent = new Intent(context, DialogActivity2.class);
                            intent.putExtra("search", tkl);
                            startActivity(intent);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getDetail(String num_iid, final Intent intent) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("num_iid", num_iid);
        HttpUtils.post(Constants.HOME_TBK_GETGOODSMSG_URL, HomeFirstFragment.this, requestParams, new onOKJsonHttpResponseHandler<PromotionDetailsBean>(new TypeToken<Response<PromotionDetailsBean>>() {
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
                if (datas.isSuccess()) {
                    PromotionDetailsBean data = datas.getData();
                    if (data != null) {
                        intent.putExtra("pic", data.getPict_url());
                        intent.putExtra("title", data.getTitle());
                        intent.putExtra("one", data.getCommission());
                        intent.putExtra("two", data.getZk_final_price());
                        intent.putExtra("three", data.getCoupon_amount());
                        startActivity(intent);
                    }
                } else {
                    showToast(datas.getMsg());
                }
            }
        });
    }

    private void getBanner() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("cat_id", 1);
        HttpUtils.post(Constants.GET_BANNER, HomeFirstFragment.this, requestParams, new onOKJsonHttpResponseHandler<BannerBean>(new TypeToken<Response<BannerBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(throwable.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Response<BannerBean> datas) {
                if (datas.isSuccess()) {
                    images.clear();
                    images.addAll(datas.getData().getList());
                    List<String> strings = new ArrayList<>();
                    for (int i = 0; i < images.size(); i++) {
                        if (images.get(i).getImg().startsWith("http")) {
                            strings.add(images.get(i).getImg());
                        } else {

                            strings.add(Constants.APP_IP + images.get(i).getImg());
                        }
                        if ("15".equals(images.get(i).getId()) && SPUtils.getIntData(context, "hongbao", 0) != 1) {
                            //有红包，并且没领过
//                            tankuang();
                        }
                    }
                    banner.stopAutoPlay();
                    banner.update(strings);
                    banner.startAutoPlay();
                } else {
//                    showToast(datas.getMsg());
                }
            }
        });
    }

    private void WeiXinq() {
        //// Log.d(TAG, "weixinqun: ");
        RequestParams params = new RequestParams();
        params.put("status", "9");
        HttpUtils.post(Constants.getWXQinXinXI, HomeFirstFragment.this, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    JSONArray arry = jsonObject.getJSONArray("data");
                    for (int i = 0; i < arry.length(); i++) {
                        JSONObject object = (JSONObject) arry.get(i);
                        WeiXinQunXinXIBean weiXinQunXinXIBean = new WeiXinQunXinXIBean();
                        if (!"所有群".equals(object.getString("group_title"))) {
                            try {
                                weiXinQunXinXIBean.group_title = object.getString("group_title");
                            } catch (JSONException e) {
                                weiXinQunXinXIBean.group_title = "21";
                            }

                            try {
                                weiXinQunXinXIBean.tmp_id = object.getString("tmp_id");
                            } catch (JSONException e) {
                                weiXinQunXinXIBean.tmp_id = "21";
                            }
                            weiXinQunXinXIBeanList.add(weiXinQunXinXIBean);
                        }

                    }
                    //// Log.d(TAG, "weixinqun: 2");
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            weiXinQunAdapter.notifyDataSetChanged();
//                        }
//                    });
//
                    weiXinQunAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    //// Log.d(TAG, "weixinqun: 1");
                    e.printStackTrace();
                }
            }
        });
    }
}
