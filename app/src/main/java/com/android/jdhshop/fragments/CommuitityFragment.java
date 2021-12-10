package com.android.jdhshop.fragments;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.android.jdhshop.https.HttpUtils.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.NewShareActivity;
import com.android.jdhshop.activity.WebViewActivity4;
import com.android.jdhshop.adapter.BkAdapter;
import com.android.jdhshop.adapter.SuCaiAdapter;
import com.android.jdhshop.adapter.XtAdapter;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.bean.BkBean;
import com.android.jdhshop.bean.MessageCenterBean;
import com.android.jdhshop.bean.MessageEvent;
import com.android.jdhshop.bean.PddClient;
import com.android.jdhshop.bean.PromotionDetailsBean;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.SuCaiBean;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.login.WelActivity;
import com.android.jdhshop.utils.BitmapUtils;
import com.android.jdhshop.utils.ImgUtils;
import com.android.jdhshop.utils.StringUtils;
import com.android.jdhshop.utils.WxUtil;
import com.android.jdhshop.utils.ZxingUtils;
import com.android.jdhshop.widget.indicator.MagicIndicator;
import com.android.jdhshop.widget.indicator.ViewPagerHelper;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.CommonNavigator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jiguang.analytics.android.api.CountEvent;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class CommuitityFragment extends BaseLazyFragment {
    @BindView(R.id.magic_indicator)
    MagicIndicator tabBar;
    @BindView(R.id.view_page)
    ViewPager viewpager;
    @BindView(R.id.view_zz)
    View viewZz;
    @BindView(R.id.copy_friends_cicle_btn)
    TextView copyFriendsCicleBtn;
    @BindView(R.id.copy_friends_btn)
    TextView copyFriendsBtn;
    @BindView(R.id.ll_share)
    LinearLayout llShare;
    @BindView(R.id.copy_friends_qq)
    TextView copyFriendsQq;
    @BindView(R.id.copy_friends_cicle_zone)
    TextView copyFriendsCicleZone;
    @BindView(R.id.title_share_tv)
    TextView titleShareTv;
    @BindView(R.id.after_coupon_share_tv)
    TextView afterCouponShareTv;
    @BindView(R.id.price_share_tv)
    TextView priceShareTv;
    @BindView(R.id.tv_for_share)
    TextView tvForShare;
    @BindView(R.id.erweima_tv)
    ImageView erweimaTv;
    @BindView(R.id.share_fl)
    FrameLayout shareFl;
    @BindView(R.id.iv)
    ImageView iv;
    private View view;
    private ArrayList<String> list = new ArrayList<>();
    private int index = 0;
    private List<View> views = new ArrayList<>();
    private RecyclerView left_recyview, right_recyview, xt_recyview;
    private SmartRefreshLayout left_refresh, right_refresh, xt_refresh;
    private int leftPage = 1, rightPage = 1, xuetangPage = 1;
    private List<BkBean.BKItem> leftList = new ArrayList<>();
    private List<SuCaiBean> rightList = new ArrayList<>();
    private SuCaiAdapter rightAdapter;
    private BkAdapter leftAdapter;
    private XtAdapter xtAdapter;
    private List<MessageCenterBean.MessageCenterChildBean> xtlist = new ArrayList<>();
    SuCaiBean item;
    private ArrayList<String> shareList = new ArrayList<>();
    private AlibcLogin alibcLogin;
    BkBean.BKItem bkBean;
    private String spTkl = "";
    PromotionDetailsBean data;
    private String min_id = "1";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shequ, container, false);
        ButterKnife.bind(this, view);
        alibcLogin = AlibcLogin.getInstance();
        init();
        addListener();
        EventBus.getDefault().register(this);
        return view;
    }

    private void init() {
        list.add("每日爆款");
        list.add("宣传素材");
        list.add("多惠学堂");
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_shequ, null);
        left_refresh = linearLayout.findViewById(R.id.refresh_layout);
        left_recyview = linearLayout.findViewById(R.id.commuitity_list);
        LinearLayoutManager tmp = new LinearLayoutManager(context);
        tmp.setOrientation(LinearLayoutManager.VERTICAL);
        left_recyview = linearLayout.findViewById(R.id.commuitity_list);
        left_recyview.setLayoutManager(tmp);
        leftAdapter = new BkAdapter(context, R.layout.item_shequ, leftList);
        left_recyview.setAdapter(leftAdapter);
        views.add(linearLayout);
        //宣传素材
        LinearLayout linearLayout2 = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_shequ, null);
        right_refresh = linearLayout2.findViewById(R.id.refresh_layout);
        LinearLayoutManager tmp2 = new LinearLayoutManager(context);
        tmp2.setOrientation(LinearLayoutManager.VERTICAL);

        right_recyview = linearLayout2.findViewById(R.id.commuitity_list);
        right_recyview.setLayoutManager(tmp2);
        rightAdapter = new SuCaiAdapter(context, R.layout.item_shequ, rightList);
        right_recyview.setAdapter(rightAdapter);
        views.add(linearLayout2);
        //学堂
        LinearLayout linearLayout3 = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_shequ, null);
        xt_refresh = linearLayout3.findViewById(R.id.refresh_layout);
        LinearLayoutManager tmp3 = new LinearLayoutManager(context);
        tmp3.setOrientation(LinearLayoutManager.VERTICAL);
        xt_recyview = linearLayout3.findViewById(R.id.commuitity_list);
        xt_recyview.setLayoutManager(tmp3);
        xtAdapter = new XtAdapter(context, R.layout.item_xt, xtlist);
        xt_recyview.setAdapter(xtAdapter);
        views.add(linearLayout3);
        viewpager.setAdapter(pagerAdapter);
        CommonNavigator commonNavigator = new CommonNavigator( (Activity) context );
        commonNavigator.setSkimOver(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public IPagerTitleView getTitleView(final Context context, final int index) {
                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);
                commonPagerTitleView.setContentView(R.layout.item_text2);
                final TextView titleText = commonPagerTitleView.findViewById(R.id.txt_cat);
                titleText.setText(list.get(index));

                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {

                    @Override
                    public void onDeselected(int i, int i1) {
                        titleText.setTextColor(getResources().getColor(R.color.col_999));
                    }

                    @Override
                    public void onSelected(int i, int i1) {
                        titleText.setTextColor(getResources().getColor(R.color.red1));
                    }

                    @Override
                    public void onEnter(int i, int i1, float v, boolean b) {
                    }

                    @Override
                    public void onLeave(int i, int i1, float v, boolean b) {

                    }
                });

                commonPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewpager.setCurrentItem(index);
                    }
                });
                return commonPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(1);
                indicator.setColors(getResources().getColor(R.color.red1));
                return indicator;
            }
        });
        tabBar.setNavigator(commonNavigator);
        ViewPagerHelper.bind(tabBar, viewpager);
        //设置页数
        viewpager.setCurrentItem(index);
        left_refresh.autoRefresh();
        right_refresh.autoRefresh();
        xt_refresh.autoRefresh();
    }

    private void addListener() {
        left_refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                leftPage++;
                getLeftList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                leftPage = 1;
                getLeftList();
            }
        });
        right_refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                rightPage++;
                getRightList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                rightPage = 1;
                getRightList();
            }
        });
        xt_refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                xuetangPage++;
                getXt();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                xuetangPage = 1;
                getXt();
            }
        });
        viewZz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewZz.setVisibility(View.GONE);
                llShare.setVisibility(View.GONE);
            }
        });
        copyFriendsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item == null) {
                    return;
                }
                RequestParams requestParams = new RequestParams();
                requestParams.put("id", item.getId());
                HttpUtils.post(Constants.UPDATE_SHARE_NUM, CommuitityFragment.this,requestParams, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    }
                });
                String path = shareList.size() > 0 ? shareList.get(0) : "";
                if (!path.startsWith("http")) {
                    path = Constants.APP_IP + path.replace("\"", "").replace("\\", "");
                }
                Glide.with(context).load(path).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        WxUtil.sharePicByBitMap(resource, "pyq", SendMessageToWX.Req.WXSceneSession, context);
                        viewZz.performClick();
                        ClipboardManager myClipboard;
                        myClipboard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
                        ClipData myClip;
                        myClip = ClipData.newPlainText("text", item.getMob_text());
                        myClipboard.setPrimaryClip(myClip);
                    }
                });
            }
        });
        copyFriendsCicleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item == null) {
                    return;
                }
                RequestParams requestParams = new RequestParams();
                requestParams.put("id", item.getId());
                HttpUtils.post(Constants.UPDATE_SHARE_NUM, CommuitityFragment.this,requestParams, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    }
                });
                String path = shareList.size() > 0 ? shareList.get(0) : "";
                if (!path.startsWith("http")) {
                    path = Constants.APP_IP + path.replace("\"", "").replace("\\", "");
                }
                Glide.with(context).load(path).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        WxUtil.sharePicByBitMap(resource, "pyq", SendMessageToWX.Req.WXSceneTimeline, context);
                        viewZz.performClick();
                        ClipboardManager myClipboard;
                        myClipboard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
                        ClipData myClip;
                        myClip = ClipData.newPlainText("text", item.getMob_text());
                        myClipboard.setPrimaryClip(myClip);
                    }
                });
//                WxUtil.sharePicByBitMap2(shareList.size() > 0 ? shareList.get(0) : "", "pyq", SendMessageToWX.Req.WXSceneTimeline, context, item.getMob_text());
//                viewZz.performClick();
            }
        });
        copyFriendsQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item == null) {
                    return;
                }
                RequestParams requestParams = new RequestParams();
                requestParams.put("id", item.getId());
                HttpUtils.post(Constants.UPDATE_SHARE_NUM, CommuitityFragment.this,requestParams, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    }
                });
                String path = shareList.size() > 0 ? shareList.get(0) : "";
                if (!path.startsWith("http")) {
                    path = Constants.APP_IP + path.replace("\"", "").replace("\\", "");
                }
                Glide.with(context).load(path).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        List<Bitmap> s = new ArrayList<>();
                        s.add(resource);
                        share(2, s);
                        viewZz.performClick();
                        ClipboardManager myClipboard;
                        myClipboard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
                        ClipData myClip;
                        myClip = ClipData.newPlainText("text", item.getMob_text());
                        myClipboard.setPrimaryClip(myClip);
                    }
                });
            }
        });
        copyFriendsCicleZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item == null) {
                    return;
                }
                RequestParams requestParams = new RequestParams();
                requestParams.put("id", item.getId());
                HttpUtils.post(Constants.UPDATE_SHARE_NUM, CommuitityFragment.this,requestParams, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    }
                });
                String path = shareList.size() > 0 ? shareList.get(0) : "";
                if (!path.startsWith("http")) {
                    path = Constants.APP_IP + path.replace("\"", "").replace("\\", "");
                }
                Glide.with(context).load(path).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        List<Bitmap> s = new ArrayList<>();
                        s.add(resource);
                        share(3, s);
                        viewZz.performClick();
                        ClipboardManager myClipboard;
                        myClipboard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
                        ClipData myClip;
                        myClip = ClipData.newPlainText("text", item.getMob_text());
                        myClipboard.setPrimaryClip(myClip);
                    }
                });
            }
        });
    }

    String storePath = "";
    List<String> tempList = new ArrayList<>();

    private List<String> saveImgs(List<Bitmap> list) {
        if (tempList.size() > 0) {
            for (int i = 0; i < tempList.size(); i++) {
                if (new File(storePath + tempList.get(i)).exists()) {
//                        new File(storePath+tempList.get(i)).delete();
                    Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    ContentResolver mContentResolver = context.getContentResolver();
                    String where = MediaStore.Images.Media.DATA + "='" + storePath + tempList.get(i) + "'";
                    mContentResolver.delete(uri, where, null);
                }
            }
        }
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String url = System.currentTimeMillis() + i + ".jpg";
            if (ImgUtils.saveImageToGallery2(context, list.get(i), url)) {
                urls.add(url);
            }
        }
        return urls;
    }

    private void share(final int type, final List<Bitmap> list) {
        if (list.size() <= 0) {
            T.showShort(context, "未选择分享图片");
            return;
        }
        final List<String> urls = saveImgs(list);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Intent intent = new Intent();
                ComponentName comp = null;
                if (type == 2) {
                    comp = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
                } else {
                    comp = new ComponentName("com.qzone", "com.qzonex.module.operation.ui.QZonePublishMoodActivity");
                }
                intent.setComponent(comp);
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.setType("image/*");
                final ArrayList<Uri> imageUris = new ArrayList<>();
                for (int i = 0; i < urls.size(); i++) {
                    imageUris.add(Uri.parse(storePath + urls.get(i)));
                }
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
                intent.putExtra("Kdescription", "");
                for (int i = 0; i < list.size(); i++) {
                    tempList.add(urls.get(i));
                }
                SPUtils.saveStringData(getContext(), "share_zd", "Y");
                startActivity(intent);
                JAnalyticsInterface.onEvent(context, new CountEvent("tb_share_goods"));
            }
        }).start();
        viewZz.setVisibility(View.GONE);
        llShare.setVisibility(View.GONE);
    }

    private void getXt() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("cat_id", 5);
        requestParams.put("p", xuetangPage);
        requestParams.put("per", 8);
        HttpUtils.post(Constants.MESSAGE_ARTICLE_GETARTICLELIST_URL,CommuitityFragment.this, requestParams, new onOKJsonHttpResponseHandler<MessageCenterBean>(new TypeToken<Response<MessageCenterBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(throwable.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Response<MessageCenterBean> datas) {
                if (datas.isSuccess()) {
                    if (xuetangPage == 1) {
                        xtlist.clear();
                        xtlist.addAll(datas.getData().getList());
                        xt_refresh.finishRefresh();
                    } else {
                        if (datas.getData().getList().size() <= 0) {
                            showToast("已加载全部");
                            xuetangPage--;
                        }
                        xtlist.addAll(datas.getData().getList());
                        xt_refresh.finishLoadMore();
                    }
                    xtAdapter.notifyDataSetChanged();
                } else {
                    showToast(datas.getMsg());
                }
            }
        });
    }

    private void getLeftList() {
        if (leftPage == 1) {
            min_id = "1";
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("min_id", min_id);
        HttpUtils.post(Constants.HD_DAY_BK_NEW, CommuitityFragment.this,requestParams, new TextHttpResponseHandler() {
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
                if (left_refresh != null) {
                    left_refresh.finishRefresh();
                    left_refresh.finishLoadMore();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject object = new JSONObject(responseString);
                    if ("1".equals(object.getString("code"))) {
                        JSONArray array = object.getJSONArray("data");
                        if (min_id.equals("1")) {
                            leftList.clear();
                        }
                        min_id = object.getString("min_id");
                        for (int i = 0; i < array.length(); i++) {
                            leftList.add(new Gson().fromJson(array.getJSONObject(i).toString(), BkBean.BKItem.class));
                        }
                        leftAdapter.notifyDataSetChanged();
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("p", leftPage);
//        requestParams.put("per", 6);
//        HttpUtils.post(Constants.GETBK_NEW, requestParams, new onOKJsonHttpResponseHandler<BkBean>(new TypeToken<Response<BkBean>>() {
//        }) {
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(throwable.getMessage());
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Response<BkBean> datas) {
//                if (datas.isSuccess()) {
//                    if (leftPage == 1) {
//                        leftList.clear();
//                        leftList.addAll(datas.getData().list);
//                        left_refresh.finishRefresh();
//                    } else {
//                        if (datas.getData().list.size() <= 0) {
//                            showToast("已加载全部");
//                            leftPage--;
//                        }
//                        leftList.addAll(datas.getData().list);
//                        left_refresh.finishLoadMore();
//                    }
//                    leftAdapter.notifyDataSetChanged();
//                } else {
//                    showToast(datas.getMsg());
//                }
//            }
//        });
    }

    private void getRightList() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("p", rightPage);
        requestParams.put("per", 6);
        requestParams.put("board_id", 2);
        HttpUtils.post(Constants.GET_DAYSC, CommuitityFragment.this,requestParams, new onOKJsonHttpResponseHandler<SuCaiBean>(new TypeToken<Response<SuCaiBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(throwable.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Response<SuCaiBean> datas) {
                if (datas.isSuccess()) {
                    if (rightPage == 1) {
                        rightList.clear();
                        rightList.addAll(datas.getData().getList());
                        right_refresh.finishRefresh();
                    } else {
                        if (datas.getData().getList().size() <= 0) {
                            showToast("已加载全部");
                            rightPage--;
                        }
                        rightList.addAll(datas.getData().getList());
                        right_refresh.finishLoadMore();
                    }
                    rightAdapter.notifyDataSetChanged();
                } else {
                    showToast(datas.getMsg());
                }
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            viewZz.performClick();
        }
    }

    @Override
    protected void lazyload() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void Event(MessageEvent messageEvent) {
        if (messageEvent != null && "share".equals(messageEvent.getMessage())) {
            item = rightList.get(messageEvent.getPosition());
            String[] datas = item.getMob_img().replace("[", "").replace("]", "").split(",");
            shareList.clear();
            for (int i = 0; i < datas.length; i++)
                shareList.add(datas[i]);
            viewZz.setVisibility(View.VISIBLE);
            llShare.setVisibility(View.VISIBLE);
        } else if (messageEvent != null && "share2".equals(messageEvent.getMessage())) {
//            viewZz.setVisibility(View.VISIBLE);
//            llShare.setVisibility(View.VISIBLE);
            if ("".equals(SPUtils.getStringData(context, "token", ""))) {
                T.showShort(context, "请先登录");
                openActivity(WelActivity.class);
                return;
            }
            isbindReld(messageEvent.getPosition());
        }
    }

    private void isbindReld(final int pos) {
        RequestParams requestParams = new RequestParams();
        HttpUtils.postUpload(Constants.ISBIND_RELATION_ID, CommuitityFragment.this,requestParams, new TextHttpResponseHandler() {
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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getHandler.sendEmptyMessage(pos);
                            }
                        });
                    } else {
                        unbinTb();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private Handler getHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            showLoadingDialog("制作分享");
            bkBean = leftList.get(msg.what);
            Glide.with(context).load(bkBean.sola_image+"_310x310.jpg").dontAnimate().into(iv);
            titleShareTv.setText(bkBean.title);
            tvForShare.setText(bkBean.couponmoney.replace(".00", "") + " 元");
            try {
                afterCouponShareTv.setText("¥" + (String.format("%.2f", (StringUtils.doStringToDouble(bkBean.itemprice) - StringUtils.doStringToDouble(bkBean.couponmoney)))).replace(".00", ""));
            } catch (NumberFormatException e) {
                afterCouponShareTv.setText("¥" + (String.format("%.2f", StringUtils.doStringToDouble(bkBean.itemprice))).replace(".00", ""));
            }
            priceShareTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            priceShareTv.setText(bkBean.itemprice.replace(".00", ""));
            erweimaTv.setImageBitmap(ZxingUtils.createBitmap(Constants.SHARE_URL + "/wap/Index/share/num_iid/" + bkBean.itemid + "/uid/" + SPUtils.getStringData(context, Constants.UID, "")));
//            shareFl.setVisibility(View.VISIBLE);
            getGoodsMsgRequest(bkBean.itemid);
        }
    };

    /**
     * @属性:获取淘宝客商品详情
     * @开发者:陈飞
     * @时间:2018/7/27 14:26
     */
    private void getGoodsMsgRequest(String num_iid) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("num_iid", num_iid);
        HttpUtils.post(Constants.HOME_TBK_GETGOODSMSG_URL, CommuitityFragment.this,requestParams, new onOKJsonHttpResponseHandler<PromotionDetailsBean>(new TypeToken<Response<PromotionDetailsBean>>() {
        }) {
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
            public void onSuccess(int statusCode, Response<PromotionDetailsBean> datas) {
                if (datas.isSuccess()) {
                    data = datas.getData();
                    if (data != null) {
                        getTkl(data.getTitle(), ("".equals(data.getCoupon_click_url_r()) || data.getCoupon_click_url_r() == null) ? data.getCoupon_click_url() : data.getCoupon_click_url_r(), data.getPict_url());
                    } else {
                        closeLoadingDialog();
                        showToast(datas.getMsg());
                    }
                } else {
                    showToast(datas.getMsg());
                }
            }
        });
    }


    /**
     * @属性:获取淘口令
     * @开发者:陈飞
     * @时间:2018/7/28 22:04
     */
    private void getTkl(String spName, String spUrl, String spLogoUrl) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("text", spName);
        requestParams.put("url", spUrl);
        requestParams.put("logo", spLogoUrl);
        //获取淘口令
        HttpUtils.post(Constants.CREATE_TP_WD,CommuitityFragment.this, requestParams, new TextHttpResponseHandler() {
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
                        spTkl = data;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                handler.sendEmptyMessage(0);
                            }
                        }, 500);
                    } else {
                        closeLoadingDialog();
                        showToast(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = new Bundle();
            ArrayList<String> images = new ArrayList<>();
            images.addAll(bkBean.itempic);
            bundle.putStringArrayList("urls", images);
            Intent intent = new Intent(context, NewShareActivity.class);
            intent.putExtra("shouyi", data.getCommission());
            intent.putExtra("name", titleShareTv.getText().toString());
            intent.putExtra("price", data.getZk_final_price().replace("原价:¥", ""));
            try {
                intent.putExtra("after_price", ((String.format("%.2f", StringUtils.doStringToDouble(data.getZk_final_price()) - StringUtils.doStringToDouble(data.getCoupon_amount())))));
            } catch (NumberFormatException e) {
                intent.putExtra("after_price", ((String.format("%.2f", StringUtils.doStringToDouble(data.getZk_final_price())))));
            }
            intent.putExtra("kouling", spTkl);
            intent.putExtra("bitmap", bundle);
            Bitmap bit = BitmapUtils.createViewBitmap(shareFl, context);
            String url = System.currentTimeMillis() + ".jpg";
            boolean is = ImgUtils.saveImageToGallery2(context, bit, url);
            if (is) {
                intent.putExtra("imgurl", url);
            } else {
                return;
            }
            startActivity(intent);
            closeLoadingDialog();
        }
    };

    private void unbinTb() {
        RequestParams params = new RequestParams();
        // get方式
        HttpUtils.post(Constants.UNBING_TAOBAO,CommuitityFragment.this, params, new AsyncHttpResponseHandler() {
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
                        alibcLogin.showLogin(new AlibcLoginCallback() {
                            @Override
                            public void onSuccess(int i, String userid, String s1) {
                                userid = alibcLogin.getSession().userid;
                                if (userid == null) {
                                    return;
                                }
                                int length = userid.length();
                                if (length > 6) {
                                    String b = userid.substring(length - 6, length);
                                    String[] bArr = b.split("");
                                    String c = bArr[1] + "" + bArr[2] + "" + bArr[5] + "" + bArr[6] + "" + bArr[3] + "" + bArr[4];
                                    bindingTaobao(c);
//                                    int i=0;
                                }
                            }

                            @Override
                            public void onFailure(int code, String msg) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "取消绑定",
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
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

    //绑定淘宝账号
    private void bindingTaobao(String tb_uid) {
        String url = Constants.BINDING_TAOBAO;
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder().add("token", SPUtils.getStringData(context, "token", "")).add("tb_uid", tb_uid).build();
        Request request = new Request.Builder().url(url).post(body).build();
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

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Intent intent = new Intent(context, WebViewActivity.class);
//                                intent.putExtra("title","绑定淘宝分享渠道");
//                                intent.putExtra("url","https://oauth.taobao.com/authorize?response_type=code&client_id=26017222&redirect_uri=http://127.0.0.1:12345/error&state=1212&view=wap");
//                                startActivity(intent);
                                openActivity(WebViewActivity4.class);
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "绑定淘宝账号失败",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!"0".equals(SPUtils.getStringData(context, "coded", "0"))) {
            getToken(SPUtils.getStringData(context, "coded", "0"));
            SPUtils.saveStringData(context, "coded", "0");
            return;
        }
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
        HttpUtils.post1("https://eco.taobao.com/router/rest", CommuitityFragment.this,requestParams, new TextHttpResponseHandler() {
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
        HttpUtils.post1("https://eco.taobao.com/router/rest", CommuitityFragment.this,requestParams, new TextHttpResponseHandler() {
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
                        ToastUtils.showShortToast(context, object.getJSONObject("error_response").getString("sub_msg")+"");
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
        HttpUtils.post(Constants.BIND_RELATION_ID, CommuitityFragment.this,params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
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
                        showToast("开通绑定成功");
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

    PagerAdapter pagerAdapter = new PagerAdapter() {

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return views.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            // TODO Auto-generated method stub
            container.removeView(views.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            container.addView(views.get(position));
            return views.get(position);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
