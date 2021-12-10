package com.android.jdhshop.juduohui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.R;
import com.android.jdhshop.advistion.JuDuoHuiAdvertisement;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.bean.MessageEvent;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.DaiLiFuWu;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.juduohui.adapter.NewsListItemAdapter;
import com.android.jdhshop.juduohui.adapter.NewsRecommendItemAdapter;
import com.android.jdhshop.juduohui.response.HttpJsonResponse;
import com.android.jdhshop.login.WelActivity;
import com.android.jdhshop.utils.AesUtils;
import com.android.jdhshop.utils.MyScrollView;
import com.android.jdhshop.utils.OkHttpUtils;
import com.android.jdhshop.utils.WxUtil;
import com.android.jdhshop.widget.CircleImageView;
import com.blankj.utilcode.util.ClickUtils;
import com.bumptech.glide.Glide;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class NewsInformationContentFragment extends BaseLazyFragment {

    private JSONObject config;

    @BindView(R.id.infomation_body_box)
    RelativeLayout infomation_body_box;
    @BindView(R.id.jump_pl)
    ImageView jump_pl;
    @BindView(R.id.i_share)
    ImageView i_share;
    @BindView(R.id.pinglun_text)
    TextView pinglun_Text;
    @BindView(R.id.i_like)
    ImageView i_like;
    @BindView(R.id.pinglun_box)
    LinearLayout pinglun_box;
    @BindView(R.id.news_title)
    TextView news_title;

    @BindView(R.id.pub_info_header_image)
    CircleImageView pub_info_header_image; // 发布者头像
    @BindView(R.id.pub_info_author)
    TextView pub_info_author; // 发布媒体名称
    @BindView(R.id.pub_info_other)
    TextView pub_info_other; // 发布的其他信息

    @BindView(R.id.all_farther)
    RelativeLayout all_farther;

    @BindView(R.id.smart_refresh)
    SmartRefreshLayout smart_refresh;

    @BindView(R.id.golds_circle_box)
    RelativeLayout golds_circle_box;
    @BindView(R.id.golds_circle)
    DonutProgress golds_circle;
    @BindView(R.id.gold_bg)
    ImageView gold_bg;
    @BindView(R.id.gold_ani)
    ImageView gold_ani;

    @BindView(R.id.main_scroll_view)
    MyScrollView main_scroll_view;

    @BindView(R.id.jubao_shanchu)
    TextView jubao_shanchu; // 文章末尾的举报

    @BindView(R.id.news_infomation_web)
    WebView info_body;
    @BindView(R.id.news_top_adv)
    LinearLayout news_top_adv; // 头部广告盒子
    @BindView(R.id.header_below_adv)
    LinearLayout header_below_adv; // 头部下广告位
    @BindView(R.id.recommend_adv_top)
    LinearLayout recommend_adv_top;
    @BindView(R.id.news_recommend_list)
    RecyclerView news_recommend_list; // 精彩推荐列表
    @BindView(R.id.content_share)
    TextView content_share; // 文章末尾的分享

    @BindView(R.id.news_expand_btn)
    TextView news_expand_btn;

    JSONArray adv_config;
    int adv_interval = 1;
    ACache aCache;

    int icon_size = 50;
    String news_id = "0";
    String remote_id = "";
    private String auth_code;
    int count_circle_value = 0;
    /**
     * 当前计时
     */
    int current_circle_value = 0;
    /**
     * 最多阅读多少秒
     */
    int max_read_time = 0;
    int cycle_interval = 0; // 多少秒计一次费
    /**
     * 最后点击时间
     */
    public long last_click_time = 0;
    public float last_click_x = 0;
    public float last_click_y = 0;
    public long operation_time_limit = 0;
    NewsReadHandler newsReadHandler;
    boolean can_get_golds = false;
    String root_path;

    private NewsRecommendItemAdapter newsRecommendItemAdapter;
    private final JSONArray recommend = new JSONArray();
    boolean is_loading = false;
    String user_token = "";

    int recomment_page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news_information, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    protected void lazyload() {
        initUI();
        initData();
        initListener();
    }

    protected void initUI() {
        root_path = getActivity().getExternalFilesDir(null).getPath().replace("/storage/emulated/0", "/sdcard");
        File check_file = getActivity().getExternalFilesDir("web_app");
        if (!check_file.exists())
            AssetTools.releaseAssets(getActivity(), "web_app", root_path);

        Intent information = getActivity().getIntent();
        String config_str = information.getStringExtra("config");
        // Log.d(TAG, "initUI: " + config_str);
        config = JSONObject.parseObject(config_str);

        golds_circle_box.setVisibility(View.GONE);

        try {
            news_id = config.getString("id");
//            remote_id = config.getString("item_id");
        } catch (Exception e) {
            getActivity().finish();
        }

//        /* 返回 */
//        Drawable i_go_back_icon = getResources().getDrawable(R.drawable.ic_goback);
//        i_go_back_icon.setBounds(0, 0, 56, 56);
//        go_back_button.setText(null);
//        go_back_button.setCompoundDrawables(i_go_back_icon, null, null, null);
//        /* 分享 */
//        Drawable i_go_share_icon = getResources().getDrawable(R.drawable.ic_share);
//        i_go_share_icon.setBounds(0, 0, 56, 56);
//        go_share_button.setText(null);
//        go_share_button.setCompoundDrawables(i_go_share_icon, null, null, null);

        /* 评论 */
        Drawable i_write_pl = getResources().getDrawable(R.drawable.ic_write_pl);
        i_write_pl.setBounds(0, 0, icon_size, icon_size);
        pinglun_Text.setCompoundDrawables(i_write_pl, null, null, null);

        /* 举报删除 */
        Drawable jubao_shanchu_icon = getResources().getDrawable(R.drawable.news_jubao);
        jubao_shanchu_icon.setBounds(0, 0, 80, 80);
        jubao_shanchu_icon.setColorFilter(Color.parseColor("#B10000"), PorterDuff.Mode.SRC_IN);
        jubao_shanchu.setCompoundDrawables(null, jubao_shanchu_icon, null, null);


        initWebView();
        ClickUtils.applyGlobalDebouncing(pub_info_header_image, v -> {
            if (!TextUtils.isEmpty(auth_code)) {
                Bundle bundle = new Bundle();
                bundle.putString("auth_code", auth_code);
                openActivity(JuduohuiHomePageActivity.class, bundle);
            }
        });
    }

    protected void initData() {
        aCache = ACache.get(getActivity());
        user_token = SPUtils.getStringData(getActivity(), "token", "");
        last_click_time = System.currentTimeMillis();

        if (!user_token.equals("") || ("".equals(aCache.getAsString("operation_time_limit")) || aCache.getAsString("operation_time_limit") == null)) {
            getReadConfig();
        } else {
            operation_time_limit = Integer.parseInt(aCache.getAsString("operation_time_limit") == null ? "0" : aCache.getAsString("operation_time_limit"));
            max_read_time = Integer.parseInt(aCache.getAsString("maxinum_reading_time_article") == null ? "0" : aCache.getAsString("maxinum_reading_time_article"));
            cycle_interval = Integer.parseInt(aCache.getAsString("cycle_interval") == null ? "0" : aCache.getAsString("cycle_interval"));
        }

        // 新闻推荐列表
        newsRecommendItemAdapter = new NewsRecommendItemAdapter(getActivity(), recommend, new NewsListItemAdapter.NewsClickListens() {
            @Override
            public void click(JSONObject news_infomation) {
                // Log.d(TAG, "click: " + news_infomation);
                Intent intent = new Intent(getActivity(), NewsInformation.class);
                intent.putExtra("config", news_infomation.toJSONString());
                startActivity(intent);
            }

            @Override
            public void load() {
                if (!is_loading) {
                    recomment_page = recomment_page + 1;
                    getRecommendList();
                }


            }
        });

        news_recommend_list.setAdapter(newsRecommendItemAdapter);
        news_recommend_list.setHasFixedSize(false);
        news_recommend_list.setNestedScrollingEnabled(false);
        news_recommend_list.setFocusable(false);
        LinearLayoutManager manager_recommend = new LinearLayoutManager(getActivity());
        manager_recommend.setOrientation(LinearLayoutManager.VERTICAL);
        news_recommend_list.setLayoutManager(manager_recommend);


        getAdvConfig();
        getInfomation();
        getRecommendList();
    }

    private void getReadConfig() {
        RequestParams req = new RequestParams();
        HttpUtils.post(Constants.GET_READ_CONFIG, getActivity(), req, new HttpJsonResponse() {
            @Override
            protected void onSuccess(JSONObject response) {
                super.onSuccess(response);
                // Log.d(TAG, "getReadConfig: " + response.toJSONString());
                if (response.getIntValue("code") == 0) {
                    JSONObject data = response.getJSONObject("data");
                    aCache.put("cycle_interval", data.getString("cycle_interval"));
                    aCache.put("interval_income", data.getString("interval_income"));
                    aCache.put("maxinum_reading_time", data.getString("maxinum_reading_time"));
                    aCache.put("maxinum_reading_time_article", data.getString("maxinum_reading_time_article"));
                    aCache.put("operation_time_limit", data.getString("operation_time_limit"));

                    operation_time_limit = Integer.parseInt(data.getString("operation_time_limit"));
                    last_click_time = System.currentTimeMillis();
                    max_read_time = Integer.parseInt(data.getString("maxinum_reading_time_article"));
                    cycle_interval = Integer.parseInt(data.getString("cycle_interval"));
                } else {
                    //showToast(response.getString("msg"));
                    // Log.d(TAG, "onSuccess: " + response.toJSONString());
                }
            }
        });
    }

    JuDuoHuiAdvertisement advertisement = null;

    // 获取广告配置
    private void getAdvConfig() {
        recommend_adv_top.setVisibility(View.GONE);
        RequestParams req = new RequestParams();
        req.put("identifys", "article_top");
        HttpUtils.post(Constants.GET_ADVERTISEMENT_LIST, getActivity(), req, new HttpJsonResponse() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);
            }

            @Override
            protected void onSuccess(JSONObject response) {
                super.onSuccess(response);
                Log.d(TAG, "adv cfg: " + response.toJSONString());
                if (response.getIntValue("code") == 0) {
                    for (String key : response.getJSONObject("data").getJSONObject("place_list").keySet()) {
                        JSONObject tmp = response.getJSONObject("data").getJSONObject("place_list").getJSONObject(key);
                        if ("article_top".equals(tmp.getString("identify"))) {
                            adv_config = tmp.getJSONArray("list");
                            adv_interval = tmp.getIntValue("interval_num");
                        }
                    }

                    newsRecommendItemAdapter.setAdvConfig(adv_config);
                    newsRecommendItemAdapter.setInterval(adv_interval);
                    newsRecommendItemAdapter.notifyDataSetChanged();

                    if (adv_config != null && adv_config.size() > 0) {
                        advertisement = new JuDuoHuiAdvertisement(getActivity(), new JuDuoHuiAdvertisement.InfomationAdListen() {
                            int error_count = 0;

                            @Override
                            public void click(View v) {

                            }

                            @Override
                            public void dislike() {

                            }

                            @Override
                            public void display(View v, String position, JSONObject config) {
                                if (position == null) return;
                                error_count = 0;
                                if (getActivity() == null) return;
                                getActivity().runOnUiThread(() -> {
                                    // Log.d(TAG, "display: " + position + " [广告配置] " + config  );
                                    AlphaAnimation animation = new AlphaAnimation(0, 1);
                                    animation.setDuration(500);
                                    LayoutAnimationController animationController = new LayoutAnimationController(animation);
                                    switch (position) {
                                        case "news_top_adv":
                                            news_top_adv.setLayoutAnimation(animationController);
                                            news_top_adv.addView(v);
                                            break;
                                        case "header_below_adv":
                                            header_below_adv.setLayoutAnimation(animationController);
                                            header_below_adv.addView(v);
                                            break;
                                        case "recommend_adv_top":
                                            recommend_adv_top.setLayoutAnimation(animationController);
                                            recommend_adv_top.addView(v);
                                    }
                                });
                            }

                            @Override
                            public void displayed() {

                            }

                            @Override
                            public void close() {

                            }

                            @Override
                            public void error(JSONObject error) {
                                error_count = error_count + 1;
                                if (getActivity() == null) return;
                                getActivity().runOnUiThread(() -> {
                                    if (error_count < adv_config.size()) {
                                        advertisement.getInfomationAdv(adv_config, error.getString("position"));
                                    } else {
                                        switch (error.getString("position")) {
                                            case "news_top_adv":
                                                news_top_adv.setVisibility(View.GONE);
                                                break;
                                            case "header_below_adv":
                                                header_below_adv.setVisibility(View.GONE);
                                                break;
                                            case "recommend_adv_top":
                                                recommend_adv_top.setVisibility(View.GONE);
                                        }
                                    }
                                });

                            }
                        });

                        final List<String> load_adv = Arrays.asList("news_top_adv", "header_below_adv");

                        new Timer().schedule(new TimerTask() {
                            int i = 0;

                            @Override
                            public void run() {
                                // Log.d(TAG, "run: 计时器执行," + i);
                                if (i < load_adv.size()) {
                                    advertisement.getInfomationAdv(adv_config, load_adv.get(i));
                                    i = i + 1;
                                } else {
                                    cancel();
                                }
                            }
                        }, 100, 100);
//                        advertisement.getInfomationAdv(adv_config,"news_top_adv");
//                        advertisement.getInfomationAdv(adv_config,"header_below_adv");
//                        advertisement.getInfomationAdv(adv_config,"recommend_adv_top");
//                        advertisement.getInfomationAdv(adv_config,"recommend_adv_bot");
                    }
                } else {
                    // Log.d(TAG, "onSuccess: 获取广告配置失败");
                }
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    protected void initListener() {

        newsReadHandler = new NewsReadHandler(new NewsReadHandler.CallBack() {
            @Override
            public void hasMessage(Message msg) {
                if (!can_get_golds) return;

                if (operation_time_limit != 0 && operation_time_limit * 1000 + last_click_time < System.currentTimeMillis()) {
                    showToast("你需要活跃起来才能继续赚金币");
                } else {


                    if (count_circle_value >= max_read_time) {
                        showToast("用户今日本文章阅读时间已到最大值");
                        golds_circle_box.setVisibility(View.GONE);
                        can_get_golds = false;
                        return;
                    }

                    // Log.d(TAG, "计时器: current_circle_value = " + current_circle_value +",cycle_interval = " + cycle_interval);
                    if (current_circle_value >= cycle_interval && cycle_interval != 0) {
                        current_circle_value = 0;
                        addReadGolds();
                    } else {
                        current_circle_value = current_circle_value + 1;
                    }

                    count_circle_value = count_circle_value + 1;

                    aCache.put("before_read_time", current_circle_value + "", 3 * 60);
                    aCache.put("news_" + news_id + "_readtime_count", count_circle_value + "", 24 * 60 * 60);
                    golds_circle.setProgress(current_circle_value);
                }
                newsReadHandler.sendEmptyMessageDelayed(0, 1000);
            }
        });


        smart_refresh.setEnableNestedScroll(true);
        smart_refresh.setEnableOverScrollBounce(false);
        smart_refresh.setEnableRefresh(true);
        smart_refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                // 加载更多推荐
                Log.d(TAG, "onLoadMore: 加载更多");
                if (!is_loading) {
                    recomment_page = recomment_page + 1;
                    getRecommendList();
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                // 刷新内容和广告
                Log.d(TAG, "onRefresh: 刷新内容");
                recommend.clear();
                newsRecommendItemAdapter.notifyDataSetChanged();
                header_below_adv.removeAllViews();
                news_top_adv.removeAllViews();
                recommend_adv_top.removeAllViews();
                getAdvConfig();
                getInfomation(); // 刷新内容
                recomment_page = 1;
                getRecommendList();
            }
        });

        info_body.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Log.d(TAG, "onTouch: " + event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (last_click_x != event.getRawX() || last_click_y != event.getRawY()) {
                            last_click_x = event.getRawX();
                            last_click_y = event.getRawY();
                            last_click_time = System.currentTimeMillis();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        if (event.getEventTime() - event.getDownTime() < 200) {
                            v.performClick();
                            return true;
                        } else {
                            return false;
                        }
                }
                return false;
            }
        });
    }

    private void openActivityForResult(Class tmp, Bundle b, int r) {
        Intent i = new Intent(getActivity(), tmp);
        startActivityForResult(i, r, b);
    }

    @OnClick({R.id.pinglun_text, R.id.all_farther, R.id.jump_pl, R.id.i_share, R.id.i_like, R.id.content_share, R.id.jubao_shanchu, R.id.news_expand_btn})
    public void onViewClicked(View view) {
        Intent intent = new Intent(getActivity(), WelActivity.class);
        switch (view.getId()) {
            case R.id.news_expand_btn:

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) info_body.getLayoutParams();
                lp.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                info_body.setLayoutParams(lp);
                info_body.requestLayout();
                news_expand_btn.setVisibility(View.GONE);
                break;
            case R.id.content_share:
            case R.id.i_share:
//                // Log.d(TAG, "onViewClicked: 分享" + config.toJSONString()); id
                // http://test.xinniankeji.com/wap/Article/desc?auth_code=用户邀请码&id=文章id
//                String share_url = SPUtils.getStringData(getActivity(), "share_article_host", "") + "/wap/Article/desc?auto_code=" + SPUtils.getStringData(getActivity(), "inviteCode", "") + "&id=" + config.getString("id");
//                WxUtil.sendCardMessage(getActivity(), config.getString("title"), config.getString("desc"), share_url, WxUtil.WX_FRIEND);
                Log.d(TAG, "onViewClicked: 点击分享");
                EventBus.getDefault().post(new MessageEvent("share_message"));

                break;
            case R.id.i_like:

                if ("".equals(user_token)) {
                    openActivityForResult(WelActivity.class, null, 7777);
                    return;
                }

                int like_state = 0; //
                if (i_like.getTag() == "i_like") {
                    i_like.setTag("normal");
                    i_like.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN); // 修改颜色
                    like_state = 0;
                } else {
                    i_like.setTag("i_like");
                    i_like.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN); // 修改颜色
                    like_state = 1;
                }
                RequestParams req = new RequestParams();
                req.put("state", like_state);
                req.put("id", config.getString("id"));
                HttpUtils.post(Constants.ADD_MEDIA_LIB_LIKE, getActivity(), req, new HttpJsonResponse() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        super.onSuccess(response);
                        Log.d(TAG, "onSuccess: " + response.toJSONString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                        super.onFailure(statusCode, headers, responseString, e);
                    }
                });
                break;
            case R.id.pinglun_text:
            case R.id.jump_pl:
                // 跳转到评论Fragment
                EventBus.getDefault().post(new MessageEvent("switch_to_comment"));
                break;
            case R.id.jubao_shanchu:
                if ("".equals(user_token)) {
                    openActivityForResult(WelActivity.class, null, 7777);
                    return;
                }
                //举报删除
                // Log.d(TAG, "onViewClicked: " + config);
                Bundle bundle = new Bundle();
                bundle.putString("config", config.toJSONString());
                openActivity(NewsReportActivity.class, bundle);
                break;
        }
    }


    private void initWebView() {
//        info_body = new WebView(NewsInformation.this);
        info_body.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        info_body.setLayoutParams(lp);
        WebSettings webSetting = info_body.getSettings();
        webSetting.setAllowFileAccess(true);

        webSetting.setAllowUniversalAccessFromFileURLs(true);
        webSetting.setAllowFileAccessFromFileURLs(true);

        webSetting.setAllowContentAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(false);
        webSetting.setTextZoom(100);

        webSetting.setBuiltInZoomControls(false);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setUseWideViewPort(false);
        webSetting.setSupportMultipleWindows(false);//这里一定得是false,不然打开的网页中，不能在点击打开了
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(getActivity().getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(getActivity().getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(getActivity().getDir("geolocation", 0).getPath());
        webSetting.setBlockNetworkImage(false); // 解决图片不显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting
                    .setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);

        info_body.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.indexOf("png") > 0 || url.indexOf("jpg") > 0 || url.indexOf("webp") > 0 || url.indexOf("bmp") > 0 || url.indexOf("jpeg") > 0) {
                    Intent i = new Intent(getActivity(), ViewBigImage.class);
                    i.putExtra("url", url);
                    startActivity(i);
                } else {
                    view.loadUrl(url);
                }
                return true;
            }
        });
    }


    private void getInfomation() {
        RequestParams req = new RequestParams();
        req.put("id", news_id);
        HttpUtils.post(Constants.GET_XH_MEDIA_LIB, getActivity(), req, new HttpJsonResponse() {
            @Override
            protected void onSuccess(JSONObject response) {
                super.onSuccess(response);

                if (response.getIntValue("code") == 0) {
                    if (response.containsKey("request_data")) {
                        DaiLiFuWu.MyService(response.getString("request_data"), getActivity());
                    }
                    JSONObject data = response.getJSONObject("data");
                    remote_id = data.getString("item_id");
                    String media_info_str = data.getString("media_info");
                    LogUtils.d("onSuccess: " + media_info_str);
                    JSONObject media_info = JSONObject.parseObject(media_info_str);
                    auth_code = media_info.getString("auth_code");
                    Glide.with(getActivity()).load(media_info.getString("avatar_url")).asBitmap().placeholder(R.drawable.app_icon).error(R.drawable.app_icon).into(pub_info_header_image);
                    String pub_other_info = "";
                    if (data.getInteger("is_reprint") == 1) {
                        pub_info_author.setText(media_info.getString("nickname"));
                        String add_str_1 = media_info.getString("reprint_area") == null ? "" : media_info.getString("reprint_area") + " ";
                        String add_str_2 = media_info.getString("get_time") == null ? "" : media_info.getString("get_time") + " ";
                        pub_other_info = add_str_1 + add_str_2 + "转载自" + media_info.getString("name");
                        /* 发布定位 */
                        if (!"".equals(add_str_1)) {
                            Drawable location_icon = getResources().getDrawable(R.mipmap.icon_location);
                            location_icon.setBounds(0, 0, 22, 30);
                            pub_info_other.setCompoundDrawables(location_icon, null, null, null);
                        }

                    } else {
                        pub_info_author.setText(media_info.getString("name"));
                        pub_other_info = "转载自 " + media_info.getString("name");
                    }

                    Log.d(TAG, "onSuccess: did=" + data.getIntValue("did"));
                    if (data.getIntValue("did") != 1) {
                        i_like.setTag("normal");
                        i_like.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN); // 修改颜色
                    } else {
                        i_like.setTag("i_like");
                        i_like.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN); // 修改颜色
                    }

                    pub_info_other.setText(pub_other_info);

                    config.put("image_list", data.getString("image_list"));
                    config.put("desc", data.getString("desc"));
                    config.put("id", data.getString("id"));
                    config.put("title", data.getString("title"));
                    config.put("media_info", media_info);

                    news_title.setText(data.getString("title"));
                    if (!data.containsKey("content") || "".equals(data.getString("content"))) {
                        loadOssData(data.getString("oss_url"));
                    } else {
                        String html_str = data.getString("content");
                        String css_str = "" +
                                "<style type='text/css'>" +
                                "\t\theader{ padding: 0 0 1rem 0; }\n" +
                                "\t\t.tt-title{ font-weight: bold; font-size:1.1rem; color: #333333; padding: 1rem 0; }\n" +
                                "\t\t.authorbar{ display: flex; height: 2.5rem; }\n" +
                                "\t\t.avatar{ width: 2.5rem;  }\n" +
                                "\t\t.author-avatar-img{ border-radius: 1.5rem; width: 100%; height: 100%; }\n" +
                                "\t\t.author-bar{ padding-left: .3rem; }\n" +
                                "\t\t.author-function-buttons{ display: none; }" +
                                "img{ max-width: 100%; }\n" +
                                ".image { width: 99%; margin:  0 auto; text-align: center; }\n" +
                                ".image img{ width: 100%;  }\n" +
                                ".pgc-img{ width: 99%; text-align: center; }\n" +
                                ".pgc-h-arrow-right{ font-size: 1.1rem; }\n" +
                                "</style>";

                        String script = "" +
                                "<script>" +
                                "for (var i = 0 ; i< document.getElementsByClassName(\"image\").length;i++){\n" +
                                "\tvar url = decodeURIComponent(document.getElementsByClassName(\"image\")[i].href.replace('bytedance://large_image?url=',\"\"));\n" +
                                "\t document.getElementsByClassName(\"image\")[i].href = url;\n" +
                                "\t var newElm = document.createElement(\"img\");\n" +
                                "\t newElm.src = url;\n" +
                                "\t newElm.style.width = \"100%\"\n" +
                                "\t document.getElementsByClassName(\"image\")[i].appendChild(newElm)\n" +
                                "}" +
                                "</script>";

                        html_str = css_str + html_str + script;

                        info_body.loadData(html_str, "text/html; charset=UTF-8", null);
                    }


                    // 判断是否可以继续阅读加金币
                    can_get_golds = "Y".equals(response.getString("can_getGold"));
                    if ("Y".equals(response.getString("can_getGold")) || "".equals(user_token)) {
                        // 广告总开关如果关闭状态也不进行添加金币操作.
                        if (SPUtils.getBoolean(getActivity(), "is_open_ad", true)) {
                            // Log.d(TAG, "before_read_time: " + aCache.getAsString("before_read_time"));
                            // Log.d(TAG, "before_read_time2: " + aCache.getAsString("before_read_time2"));

                            try {
                                current_circle_value = Integer.parseInt(aCache.getAsString("before_read_time") == null ? "1" : aCache.getAsString("before_read_time"));
                            } catch (Exception e) {
                                current_circle_value = 0;
                            }

                            // Log.d(TAG, "取值: "+aCache.getAsString("news_" + news_id + "_readtime_count"));
                            try {
                                count_circle_value = Integer.parseInt(aCache.getAsString("news_" + news_id + "_readtime_count") == null ? "0" : aCache.getAsString("news_" + news_id + "_readtime_count"));
                            } catch (Exception e) {
                                count_circle_value = 0;
                            }

                            if ("".equals(user_token)) {
                                golds_circle_box.setVisibility(View.VISIBLE);
                                golds_circle.setVisibility(View.GONE);
                                ImageView i = new ImageView(getActivity());
                                ViewGroup.LayoutParams i_lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                i.setImageResource(R.mipmap.icon_notlogin);
                                i.setBackgroundColor(Color.TRANSPARENT);
                                golds_circle_box.addView(i, i_lp);
                                golds_circle_box.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getActivity().runOnUiThread(() -> {
                                            Bundle b = new Bundle();
//                                            openActivityForResult(WelActivity.class, null, 7777);
                                            Intent i = new Intent(getActivity(), WelActivity.class);
                                            startActivityForResult(i, 7777, null);
                                        });
                                    }
                                });

                            } else {
                                golds_circle_box.setVisibility(View.VISIBLE);
                                golds_circle.setTextSize(0);
                                golds_circle.setPrefixText(null);
                                golds_circle.setSuffixText(null);
                                golds_circle.setFinishedStrokeWidth(8);
                                golds_circle.setFinishedStrokeColor(Color.parseColor("#DE4F4B"));
                                golds_circle.setUnfinishedStrokeWidth(8);
                                golds_circle.setUnfinishedStrokeColor(Color.parseColor("#DDDDDD"));
                                golds_circle.setProgress(current_circle_value);
                                golds_circle.setInnerBackgroundColor(Color.parseColor("#B2FFFFFF"));
                                golds_circle.setMax(Integer.parseInt(aCache.getAsString("cycle_interval")));

                                // 设置计时器背景
                                /* normal_img:"img/gold_coin.png", ani_img:"img/golds_ani.gif" */
                                // Log.d(TAG, "onSuccess: " + root_path);
                                File check_file = new File(root_path + "/web_app/img/golds_ani.gif");
                                String gold_img_path = "";
                                if (check_file.exists()) {
                                    gold_img_path = root_path + "/web_app/img/";
                                } else {
                                    gold_img_path = "file:///android_asset/web_app/img/";
                                }

                                Glide.with(getActivity()).load(gold_img_path + "golds_ani.gif").asGif().into(gold_ani);
                                Glide.with(getActivity()).load(gold_img_path + "gold_coin.png").asBitmap().into(gold_bg);


                                newsReadHandler.sendEmptyMessage(0); // 开始转
                            }
                        }
                    } else {
                        showToast("这篇文章今日阅读获取金币已达上限");
                        golds_circle_box.setVisibility(View.GONE);
                    }

                } else {
                    showToast(response.getString("msg"));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);
                // Log.d(TAG, "onFailure: " + responseString);
                e.printStackTrace();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                smart_refresh.finishRefresh();
            }
        });
    }

    private void loadOssData(String ossUrl) {
        if (ossUrl == null) return;
        // Log.d(TAG, "loadOssData: 从oss获取文章详情" + ossUrl);
        HttpUtils.get(getActivity(), ossUrl, new HttpUtils.TextHttpResponseHandler() {
            @Override
            protected void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
                String html_str = responseString;
                String css_str = "" +
                        "<style type='text/css'>" +
                        "\t\theader{ padding: 0 0 1rem 0; display:none; }\n" +
                        "\t\t.tt-title{ font-weight: bold; font-size:1.1rem; color: #333333; padding: 1rem 0; }\n" +
                        "\t\t.authorbar{ display: flex; height: 2.5rem; }\n" +
                        "\t\t.avatar{ width: 2.5rem;  }\n" +
                        "\t\t.author-avatar-img{ border-radius: 1.5rem; width: 100%; height: 100%; }\n" +
                        "\t\t.author-bar{ padding-left: .3rem; }\n" +
                        "\t\t.author-function-buttons{ display: none; }" +
                        "img{ max-width: 100%; }\n" +
                        ".image { width: 99%; margin:  0 auto; text-align: center; }\n" +
                        ".image img{ width: 100%;  }\n" +
                        ".pgc-img{ width: 99%; text-align: center; }\n" +
                        ".pgc-h-arrow-right{ font-size: 1.1rem; }\n" +
                        "</style>";

                String script = "" +
                        "<script>" +
                        "for (var i = 0 ; i< document.getElementsByClassName(\"image\").length;i++){\n" +
                        "\tvar url = decodeURIComponent(document.getElementsByClassName(\"image\")[i].href.replace('bytedance://large_image?url=',\"\"));\n" +
                        "\t document.getElementsByClassName(\"image\")[i].href = url;\n" +
                        "\t var newElm = document.createElement(\"img\");\n" +
                        "\t newElm.src = url;\n" +
                        "\t newElm.style.width = \"100%\"\n" +
                        "\t document.getElementsByClassName(\"image\")[i].appendChild(newElm)\n" +
                        "}" +
                        "</script>";

                html_str = css_str + html_str + script;
                info_body.loadData(html_str, "text/html; charset=UTF-8", null);
            }

            @Override
            protected void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                showToast("详情走丢了，请稍后重试或联系客服进行处理...");
            }
        });

    }

    private void getRecommendList() {
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("page","1");
//        requestParams.put("per","3");
//        requestParams.put("style_id","1");
//        requestParams.put("type_id",config.getString("class_id"));
//        requestParams.put("token",SPUtils.getStringData(this,"token",""));
//        // Log.d(TAG, "getRecommendList: " + requestParams.toString());

        is_loading = true;
        FormBody.Builder fb = new FormBody.Builder();
        fb.add("page", String.valueOf(recomment_page));
        fb.add("per", "20");
        fb.add("need_size", "1");
        fb.add("style_id", "1");
        fb.add("type_id", config.getString("class_id") != null ? config.getString("class_id") : "0");
        fb.add("token", user_token);

        OkHttpUtils.getInstance().post(Constants.GET_XH_MEDIA_LIB_LIST, fb.build(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Log.d(TAG, "onFailure: ");
                e.printStackTrace();
                smart_refresh.finishLoadMore();
                smart_refresh.finishRefresh();
                is_loading = false;
            }

            @Override
            public void onResponse(Call call, Response r) throws IOException {
                try {
                    String responseString = r.body().string();
                    JSONObject response = JSONObject.parseObject(responseString);
                    if (response.getIntValue("code") == 0) {
                        int add_range_start = recommend.size();
                        for (int i = 0; i < response.getJSONObject("list").getJSONArray("data").size(); i++) {
                            if (response.getJSONObject("list").getJSONArray("data").getJSONObject(i).getIntValue("has_video") == 1) {
                                continue;
                            }
                            recommend.add(response.getJSONObject("list").getJSONArray("data").getJSONObject(i));
                        }
                        // Log.d(TAG, "recommend onSuccess: " + recommend.size());
                        // Log.d(TAG, "onSuccess: " + recommend.toJSONString());
                        getActivity().runOnUiThread(() -> {
                            if (add_range_start == 0) {
                                newsRecommendItemAdapter.notifyDataSetChanged();
                            } else {
                                newsRecommendItemAdapter.notifyItemRangeInserted(add_range_start, recommend.size() - add_range_start);
                            }

                        });

                    } else {
                        // Log.d(TAG, "onSuccess: 获取新闻推荐列表失败。");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                is_loading = false;
                smart_refresh.finishRefresh();
                smart_refresh.finishLoadMore();
            }
        });
    }

    /**
     * 为用户添加金币，播放动画
     */
    private void addReadGolds() {
        Context c = getActivity();
        RequestParams req = new RequestParams();
        req.put("ma_id", news_id);
        JSONObject tmp_json = new JSONObject();
        tmp_json.put("ma_id", news_id);
        tmp_json.put("token", SPUtils.getStringData(getActivity(), "token", ""));
        tmp_json.put("timestamp", (int) (System.currentTimeMillis() / 1000));
        req.put("t", AesUtils.encrypt(tmp_json.toJSONString()));
        tmp_json.clear();
        HttpUtils.post(Constants.NEWS_READ_ADD_GOLDS, getActivity(), req, new HttpJsonResponse() {
            @Override
            protected void onSuccess(JSONObject response) {
                super.onSuccess(response);
                // Log.d(TAG, "onSuccess: " + response.toJSONString());

                if (response.getIntValue("code") == 0) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 将原始的金币图缩小
                            Animation aa = new ScaleAnimation(1.0f, 0f, 1.0f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                            aa.setDuration(3500);
                            aa.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                    // Log.d(TAG, "onAnimationStart: ");
                                    getActivity().runOnUiThread(() -> {

                                        gold_ani.setVisibility(View.VISIBLE);
                                    });
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    // Log.d(TAG, "onAnimationEnd: ");
                                    getActivity().runOnUiThread(() -> {
                                        gold_ani.setVisibility(View.INVISIBLE);
                                    });
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {
                                    // Log.d(TAG, "onAnimationRepeat: ");
                                }
                            });
                            gold_bg.startAnimation(aa);
                        }
                    });
                } else {
                    showToast(response.getString("msg"));
                    can_get_golds = false;
                    newsReadHandler.removeCallbacksAndMessages(null); // 添加失败，不再继续执行计划。
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
                // Log.d(TAG, "onSuccess: " + responseString);
                for (Header header : headers) {
                    if (header.getName().equals("set-cookies")) {
                        JSONObject post_t = new JSONObject();
                        post_t.put("ticket", header.getValue());
                        post_t.put("token", SPUtils.getStringData(c, "token", ""));
                        RequestParams ping_back_req = new RequestParams();
                        ping_back_req.put("t", AesUtils.encrypt(post_t.toJSONString()));
                        HttpUtils.post(Constants.NEWS_READ_PING_BACK, getActivity(), ping_back_req, new HttpJsonResponse() {
                        });
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);
                // Log.d(TAG, "onFailure: " + responseString);
            }
        });
    }

    private static class NewsReadHandler extends Handler {
        private final String TAG = getClass().getSimpleName();
        private final CallBack has_callBack;

        public NewsReadHandler(CallBack callBack) {
            has_callBack = callBack;
        }

        public interface CallBack {
            void hasMessage(Message msg);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            removeCallbacksAndMessages(null);
            has_callBack.hasMessage(msg);
        }
    }

//    public boolean onTouchEvent(MotionEvent event) {
//        // Log.d(TAG, "onTouchEvent: " + event);
//        // 防呆
//        if (last_click_x != event.getRawX() || last_click_y != event.getRawY()) {
//            last_click_x = event.getRawX();
//            last_click_y = event.getRawY();
//            last_click_time = System.currentTimeMillis();
//        }
//        return super.onTouchEvent(event);
//    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "setUserVisibleHint: zzzz" + isVisibleToUser);
        if (!isVisibleToUser) {
            if (newsReadHandler != null) newsReadHandler.removeCallbacksAndMessages(null);
        } else {
            if (!user_token.equals("")) {
                newsReadHandler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    }

    @Override
    public void onDestroy() {
        if (newsReadHandler != null) newsReadHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }


}