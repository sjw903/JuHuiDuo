package com.android.jdhshop.fragments;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.BaoYouActivity;
import com.android.jdhshop.activity.DialogActivity;
import com.android.jdhshop.activity.DialogActivity2;
import com.android.jdhshop.activity.MyScanActivity;
import com.android.jdhshop.activity.PHBActivity;
import com.android.jdhshop.activity.QdActivity;
import com.android.jdhshop.activity.SearchActivity;
import com.android.jdhshop.activity.SearchResultActivity;
import com.android.jdhshop.activity.SysMessageActivity;
import com.android.jdhshop.activity.ZeroBuyActivity;
import com.android.jdhshop.adapter.MyOderViewPagerAdapter;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.bean.MessageEvent;
import com.android.jdhshop.bean.MyGoodsResp;
import com.android.jdhshop.bean.PDDBean;
import com.android.jdhshop.bean.PddClient;
import com.android.jdhshop.bean.PromotionDetailsBean;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.ShopTabsChildBean;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.utils.OkHttpUtils;
import com.android.jdhshop.utils.UIUtils;
import com.android.jdhshop.widget.indicator.MagicIndicator;
import com.android.jdhshop.widget.indicator.ViewPagerHelper;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.CommonNavigator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.titles.ClipPagerTitleView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.king.zxing.Intents;
import com.loopj.android.http.RequestParams;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;

/**
 * 首页
 * Created by yohn on 2018/7/11.
 */

public class HomeFragment extends BaseLazyFragment implements View.OnClickListener {
    static MagicIndicator tabBar;
    @BindView(R.id.ll_top)
    LinearLayout ll_top;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    public static HomeFragment fragment;
    static LinearLayout bgHead2;
    private List<ShopTabsChildBean> tabTitles = new ArrayList<>();

    private com.alibaba.fastjson.JSONArray tab_array = new com.alibaba.fastjson.JSONArray();

    private View view;
    private List<Fragment> fragments = new ArrayList<>();
    private ImageView tv_right2;
    private ACache aCache;
    private CommonNavigatorAdapter navigatorAdapter;

    public static HomeFragment getInstance() {
        if (fragment == null) {
            fragment = new HomeFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        bgHead2 = view.findViewById(R.id.bg_head2);
        tabBar = view.findViewById(R.id.magic_indicator);
        tv_right2 = view.findViewById(R.id.tv_right2);
        BaseLogDZiYuan.LogDingZiYuan(tv_right2, "xiaoxi.png");
        bgHead2.measure(0, 0);
        EventBus.getDefault().register(this);

        aCache = ACache.get(getActivity());

        init();
        view.findViewById(R.id.img_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(KindActivity.class);
            }
        });
        addListener();
        // Log.d(TAG, "onCreateView: " + isDetached());
        isVisible = true;
        lazyload();
        return view;
    }

    private void init() {
        if (aCache.getAsJSONArray("top_cat_array") != null) {
            tab_array.addAll(aCache.getAsJSONArray("top_cat_array"));
        } else {
            getTopCatListRequst();
            return;
        }
        // Log.d(TAG, "init: " + tab_array.toJSONString());

        for (int i = 0; i < tab_array.size(); i++) {
            if (i == 0) {
                HomeFirstFragment firstFragment = new HomeFirstFragment();
                fragments.add(firstFragment);
            } else {
                com.alibaba.fastjson.JSONObject shopTabsChildBean = tab_array.getJSONObject(i);
                ShopFragment homeTabsFragment = new ShopFragment();
                Bundle bundle = new Bundle();
                // Log.d(TAG, "tabbar init: " + shopTabsChildBean.getString("taobao_cat_id") + "," + shopTabsChildBean.getString("name"));
                bundle.putString("pid", shopTabsChildBean.getString("taobao_cat_id"));
                bundle.putString("name", shopTabsChildBean.getString("name"));
                homeTabsFragment.setArguments(bundle);
                fragments.add(homeTabsFragment);
            }
        }

        viewPager.setOffscreenPageLimit(tab_array.size());

        MyOderViewPagerAdapter myOderViewPagerAdapter = new MyOderViewPagerAdapter(getFragmentManager(), fragments);
        viewPager.setAdapter(myOderViewPagerAdapter);
        //头部导航栏
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setSkimOver(true);

        // Log.d(TAG, "init www: " + tab_array.size());

        navigatorAdapter = new CommonNavigatorAdapter() {
            final com.alibaba.fastjson.JSONArray real_list = new com.alibaba.fastjson.JSONArray();
            @Override
            public int getCount() {
                return tab_array.size();
            }
            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context){
                    @Override
                    public void onSelected(int index, int totalCount) {
                        super.onSelected(index, totalCount);
                        setTextSize(UIUtils.dp2px(18));
                    }

                    @Override
                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
                        super.onLeave(index, totalCount, leavePercent, leftToRight);
                        setTextSize(UIUtils.dp2px(16));
                    }
                };
                clipPagerTitleView.setText(tab_array.getJSONObject(index).getString("name"));
                clipPagerTitleView.setTextColor(Color.parseColor("#FFEEEEEE"));
                clipPagerTitleView.setTextSize(UIUtils.dp2px(16));
                clipPagerTitleView.setClipColor(Color.WHITE);
                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });

                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(1);
                indicator.setColors(Color.WHITE);
                return indicator;
            }
        };

        commonNavigator.setAdapter(navigatorAdapter);
        tabBar.setNavigator(commonNavigator);
        ViewPagerHelper.bind(tabBar, viewPager);
        //设置页数
        viewPager.setCurrentItem(0);

    }

    private boolean hidden = false;

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void setColor(MessageEvent event) {
        if (hidden)  return;
        try {
            if (!event.getMessage().startsWith("#")) return;
            bgHead2.setBackgroundColor(Color.parseColor(event.getMessage()));
            ll_top.setBackgroundColor(Color.parseColor(event.getMessage()));
            // Log.d(TAG, "setColor: 当前显示状态：" + hidden );

            setStatusBar(Color.parseColor(event.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            hidden = false;
        } else {
            hidden = true;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
    }

    private void addListener() {
    }

    protected void setStatusBar(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getActivity().getWindow().getDecorView();
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getActivity().getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        if (color == Color.WHITE) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            if (isFlyme()) {
                CommonUtils.setMeizuStatusBarDarkIcon(getActivity(), true);
            } else if (isMIUI()) {
                CommonUtils.setMiuiStatusBarDarkMode(getActivity(), true);
            } else if (Build.MANUFACTURER.equalsIgnoreCase("OPPO")) {
                CommonUtils.setOPPOStatusTextColor(true, getActivity());
            } else {
            }
        } else {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    /**
     * 判断手机是否是魅族
     *
     * @return
     */
    private static boolean isFlyme() {
        try {
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * 判断手机是否是小米
     *
     * @return
     */
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    private static boolean isMIUI() {
        Properties prop = new Properties();
        boolean isMIUI;
        try {
            prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        isMIUI = prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
//        SPUtils.getInstance().putCacheData("isMIUI",isMIUI);//保存是否MIUI
        return isMIUI;
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
        HttpUtils.post(Constants.HONGBAO,HomeFragment.this, requestParams, new TextHttpResponseHandler() {
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
        EventBus.getDefault().unregister(this);
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
            case R.id.tv_right:
                openActivity(SysMessageActivity.class);
                break;

        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void getTopCatListRequst() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.HOME_TAOBAOCAT_GETTOPCATLIST_URL, HomeFragment.this,requestParams, new TextHttpResponseHandler() {
            @Override
            protected void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
                com.alibaba.fastjson.JSONObject response = JSON.parseObject(responseString);
                if (response.getIntValue("code") == 0) {
                    com.alibaba.fastjson.JSONArray all_array = response.getJSONObject("data").getJSONArray("list");
                    com.alibaba.fastjson.JSONObject first = new com.alibaba.fastjson.JSONObject();
                    first.put("pid", 0);
                    first.put("name", "精选");
                    first.put("icon", "");
                    first.put("taobao_cat_id", "0");
                    all_array.add(0, first);
                    aCache.put("top_cat_array", all_array, 24 * 60 * 60);
                    getActivity().runOnUiThread(() -> {
                        init();
                    });
                } else {
                    showToast(response.getString("msg"));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);
                showToast(responseString);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == 5) {
            String temp = data.getStringExtra(Intents.Scan.RESULT);
            if (!"".equals(temp)) {
                if (temp.contains("c.tb.cn") || temp.contains("m.tb.cn") || temp.contains("item.taobao.com")) {
                    Snackbar snackbar = Snackbar.make(getView(), "已扫码完毕，正在后台分析扫码数据", Snackbar.LENGTH_LONG);
                    snackbar.setAction("好的", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });

                    snackbar.setActionTextColor(Color.parseColor("#fff760"));

                    snackbar.show();
                    getActivity().onWindowFocusChanged(true);
                    ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    // 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("Label", temp);
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                } else if (temp.contains("mobile.yangkeduo.com")) {
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
     * @开发者:wmm 首页扫描二维码
     * @时间:2018/11/22 9:00
     */
    private void getPddDetail(final String goods_id) {
        RequestParams requestParams = new RequestParams();

        requestParams.put("apikey", Constants.JD_APP_KEY_NEW);
        requestParams.put("isunion", "1");
        requestParams.put("keyword", goods_id + "");
        Map<String, Object> temp = new HashMap<>();
        temp.put("apikey", Constants.JD_APP_KEY_NEW);
        temp.put("isunion", "1");
        temp.put("keyword", goods_id + "");
        HttpUtils.post(Constants.pddSearch, HomeFragment.this,requestParams, new TextHttpResponseHandler() {
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
                    if (new JSONObject(responseString).getInt("status_code") != 200) {
                        T.showShort(context, "该商品没有优惠券或奖");
                        return;
                    }
                    JSONObject jsonObject = null;
                    jsonObject = new JSONObject(responseString);
                    Gson gson = new Gson();
                    PDDBean newPddBeans;
                    DecimalFormat df = new DecimalFormat("0.00");
                    newPddBeans = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("goods_list").getJSONObject(0).toString(), PDDBean.class);

//                    JSONObject object = new JSONObject(responseString).getJSONObject("data").getJSONObject("goods_details");
                    Intent intent = new Intent(context, DialogActivity.class);
//                    Gson gson = new Gson();
//                    double tem = (Double.valueOf(object.getString("min_group_price")) - Double.valueOf(object.getString("coupon_discount"))) * Double.valueOf(df.format(Double.valueOf(object.getString("promotion_rate")) / 1000));
//                    object.put("commission", df.format(tem * SPUtils.getIntData(context, "rate", 0) / 100));
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("goods", newPddBeans);
                    intent.putExtra("goods", bundle);
                    intent.putExtra("pic", newPddBeans.getGoods_image_url());
                    intent.putExtra("title", newPddBeans.getGoods_name());
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
        RequestParams requestParams = new RequestParams();
        requestParams.put("apikey", Constants.JD_APP_KEY_NEW);
        requestParams.put("goods_ids", id);
        requestParams.put("isunion", "1");
        OkHttpUtils.getInstance().get(Constants.JDNEWGOODS_LIST + "?" + requestParams.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d("dsfasdf", e.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String s = response.body().string();
                LogUtils.d("dsfasdf", s);
                try {
                    JSONObject object1 = new JSONObject(s);
                    JSONArray array = object1.getJSONObject("data").getJSONArray("data");
                    if (array == null || array.length() == 0) {
                        return;
                    }
                    for (int i = 0; i < 1; i++) {
                        MyGoodsResp resp = new Gson().fromJson(array.getJSONObject(i).toString(), MyGoodsResp.class);
                        Intent intent = new Intent(context, DialogActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("goods", resp);
                        intent.putExtra("goods", bundle);
                        intent.putExtra("pic", resp.imageInfo.getImageList()[0].getUrl());
                        intent.putExtra("title", resp.getSkuName());
                        intent.putExtra("commission", "");
                        intent.putExtra("type", "jd");
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getGoodsMsgRequest(final String tkl) {
        String url = "";

        LogUtils.d("TAG", "getGoodsMsgRequest: HomeFragment");
        RequestParams requestParams = new RequestParams();
        requestParams.put("tkl", tkl);
        HttpUtils.post(Constants.SEARCHTKL, HomeFragment.this,requestParams, new TextHttpResponseHandler() {
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
        HttpUtils.post(Constants.HOME_TBK_GETGOODSMSG_URL, HomeFragment.this,requestParams, new onOKJsonHttpResponseHandler<PromotionDetailsBean>(new TypeToken<Response<PromotionDetailsBean>>() {
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

    @OnClick({R.id.tv_title_content2, R.id.tv_right2, R.id.txt_saoma})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_saoma:
                Acp.getInstance(context).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.CAMERA)
                        .build(), new AcpListener() {

                    @Override
                    public void onGranted() {
                        startActivityForResult(new Intent(context, MyScanActivity.class), 5);
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        showToast("拒绝权限不能使用扫一扫");
                    }
                });
                break;
            case R.id.tv_title_content2:
                openActivity(SearchActivity.class);
                break;
            case R.id.tv_right2:
                openActivity(SysMessageActivity.class);
                break;
        }
    }
}
