package com.android.jdhshop.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.common.LogUtils;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.ShopRecyclerAdapter2;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.HaoDanBean;
import com.android.jdhshop.bean.SearchHistoryBean;
import com.android.jdhshop.bean.TaobaoGuestBean;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.utils.StringUtils;
import com.android.jdhshop.utils.UIUtils;
import com.android.jdhshop.widget.AutoClearEditText;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;

/**
 * 搜索结果
 * Created by yohn on 2018/8/25.
 */

public class SearchResultActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.bg_head)
    LinearLayout bg_head;
    @BindView(R.id.tv_title)
    AutoClearEditText tvTitle;
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_right)
    TextView tvRight;
    //价格
    @BindView(R.id.tv_zero)
    TextView tv_zero;
    //销量
    @BindView(R.id.tv_one)
    TextView tv_one;
    //奖比例
    @BindView(R.id.tv_two)
    TextView tv_two;
    //推广量
    @BindView(R.id.tv_three)
    TextView tv_three;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private String min_id = "", tb_p = "";

    @BindView(R.id.right_icon)
    ImageView rightIcon;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private int indexNum = 1;
    private int status = 0;

    //界面传值 关键字 券后区间价开始 结束 分享比例开始值 结束值
    String content, stMoney, enMoney, shareOne, shareTwo;//
    int tmall = -1, type;//type 1 高级搜素 0 普通搜索

    private String sort = "0";


    List<TaobaoGuestBean.TaobaoGuesChildtBean> taobaoGuesChildtBeans = new ArrayList<>();
    List<SearchHistoryBean> historyBeans = new ArrayList<>();
    private ShopRecyclerAdapter2 shopRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
//    private ShopHighRecyclerAdapter shopHighRecyclerAdapter;

    Boolean isPricaeDes = true;
    private ACache aCache;

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_search_result);
        unbinder = ButterKnife.bind(this);
        aCache = ACache.get(getComeActivity());
        BaseLogDZiYuan.LogDingZiYuan(rightIcon, "home_btn.png");
    }

    @Override
    protected void initData() {
        tv_left.setVisibility(View.VISIBLE);
//        bg_head.setBackgroundColor(getResources().getColor(R.color.app_main_color));
        tvRight.setVisibility(View.GONE);
        tvTitle.setTextColor(getResources().getColor(R.color.col_333));
        tv_zero.setText("价格(升)");
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            if (bundle.containsKey("content")) {
                content = bundle.getString("content");
            }
            //券后区间价开始
            if (bundle.containsKey("stMoney")) {
                stMoney = bundle.getString("stMoney");
            }
            //券后区间价结束
            if (bundle.containsKey("enMoney")) {
                enMoney = bundle.getString("enMoney");
            }
            //分享比例开始值
            if (bundle.containsKey("shareOne")) {
                shareOne = bundle.getString("shareOne");
            }
            //分享比例结束值
            if (bundle.containsKey("shareTwo")) {
                shareTwo = bundle.getString("shareTwo");
            }
            //来源
            if (bundle.containsKey("tmall")) {
                tmall = bundle.getInt("tmall");
            }
            //来源
            if (bundle.containsKey("type")) {
                type = bundle.getInt("type");
            }
        }
        tvTitle.setText(content);
        tvTitle.setBackground(getResources().getDrawable(R.drawable.bg_round_gray));
        //管理器
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //适配器
//        if (type == 0) {
        shopRecyclerAdapter = new ShopRecyclerAdapter2(this, R.layout.today_highlights_child_item, taobaoGuesChildtBeans);
        recyclerView.setAdapter(shopRecyclerAdapter);
//        } else if (type == 1) {
//            shopHighRecyclerAdapter = new ShopHighRecyclerAdapter(this, R.layout.today_highlights_child_item, taobaoGuesChildtBeans);
//            recyclerView.setAdapter(shopHighRecyclerAdapter);
//        }


        //显示回滚
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //屏幕高度
                int screenMeasuredHeight = UIUtils.getScreenMeasuredHeight(SearchResultActivity.this);
                //如果滚动距离大于屏幕高度，那么显示回滚，否则。。。
                if (getScollYDistance() >= (screenMeasuredHeight / 2)) {
                    rightIcon.setVisibility(View.VISIBLE);
                } else {
                    rightIcon.setVisibility(View.GONE);
                }
                LogUtils.d("TAG", "高度为:" + getScollYDistance());

            }
        });

        tv_zero.setOnClickListener(this);
        tv_one.setOnClickListener(this);
        tv_two.setOnClickListener(this);
        tv_three.setOnClickListener(this);

        //开始刷新
        refreshLayout.autoRefresh();
    }

    @Override
    protected void initListener() {
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                status = 0;
                if (taobaoGuesChildtBeans.size() >= 10) {
                    indexNum++;
                    getTabkListNew();
                } else {
                    showToast("没有更多数据了");
                    refreshLayout.finishLoadMore(2000);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                status = 1;
                indexNum = 1;
                getTabkListNew();
            }
        });
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        List<SearchHistoryBean> searchHistoryBeans = (List<SearchHistoryBean>) aCache.getAsObject(Constants.HISTORICAL_RECORDS);
        if (searchHistoryBeans != null && searchHistoryBeans.size() > 0) {
            historyBeans.addAll(searchHistoryBeans);
        }
        //点击进入详情
        shopRecyclerAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                TaobaoGuestBean.TaobaoGuesChildtBean taobaoGuesChildtBean = taobaoGuesChildtBeans.get(position);
                if (taobaoGuesChildtBean != null) {
                    HaoDanBean haoDanBean = new HaoDanBean();
                    haoDanBean.itempic = taobaoGuesChildtBean.getPict_url();
                    haoDanBean.itemtitle = taobaoGuesChildtBean.getTitle();
                    haoDanBean.shopname = taobaoGuesChildtBean.getNick();
                    haoDanBean.itemsale = taobaoGuesChildtBean.getVolume();
                    haoDanBean.couponmoney = taobaoGuesChildtBean.getCoupon_amount();
                    haoDanBean.itemprice = taobaoGuesChildtBean.getZk_final_price();
                    haoDanBean.couponstarttime = taobaoGuesChildtBean.getStart_time();
                    haoDanBean.couponendtime = taobaoGuesChildtBean.getEnd_time();
                    haoDanBean.itemendprice = String.format("%.2f", (StringUtils.doStringToDouble(taobaoGuesChildtBean.getZk_final_price()) - StringUtils.doStringToDouble(taobaoGuesChildtBean.getCoupon_amount())));
                    haoDanBean.tkmoney = String.format("%.2f", Double.valueOf(taobaoGuesChildtBean.getCommission_rate()) / 10000 * Double.valueOf(haoDanBean.itemendprice));
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bean", haoDanBean);
                    bundle.putString("num_iid", taobaoGuesChildtBean.getNum_iid());
                    openActivity(PromotionDetailsActivity.class, bundle);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        tvTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!TextUtils.isEmpty(StringUtils.doViewToString(tvTitle))) {
                    ((InputMethodManager) tvTitle.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    getComeActivity()
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    //存储历史
                    SearchHistoryBean searchHistoryBean = new SearchHistoryBean();
                    searchHistoryBean.setContent(StringUtils.doViewToString(tvTitle));
                    if (!historyBeans.contains(searchHistoryBean)) {
                        historyBeans.add(searchHistoryBean);
                        aCache.put(Constants.HISTORICAL_RECORDS, (Serializable) historyBeans);
                    }
                    content = tvTitle.getText().toString().trim();
                    refreshLayout.autoRefresh();
                } else {
                    showToast("你未输入搜索内容");
                }
                return false;
            }
        });
    }

    @OnClick({R.id.right_icon})
    public void onViewClicked(View view) { //回到头部
        switch (view.getId()) {
            case R.id.right_icon:
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.smoothScrollToPosition(0);
                    }
                });
                break;

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_zero: //价格排序
                sort = "4";
                refreshLayout.autoRefresh();
                tv_zero.setTextColor(getResources().getColor(R.color.app_main_color));
                tv_one.setTextColor(getResources().getColor(R.color.col_333));
                tv_two.setTextColor(getResources().getColor(R.color.col_333));
                tv_three.setTextColor(getResources().getColor(R.color.col_333));

                break;
            case R.id.tv_one: //销量
                sort = "2";
                refreshLayout.autoRefresh();
                tv_zero.setTextColor(getResources().getColor(R.color.col_333));
                tv_one.setTextColor(getResources().getColor(R.color.app_main_color));
                tv_two.setTextColor(getResources().getColor(R.color.col_333));
                tv_three.setTextColor(getResources().getColor(R.color.col_333));

                break;
            case R.id.tv_two: //奖比例
                sort = "6";
                refreshLayout.autoRefresh();
                tv_zero.setTextColor(getResources().getColor(R.color.col_333));
                tv_one.setTextColor(getResources().getColor(R.color.col_333));
                tv_two.setTextColor(getResources().getColor(R.color.app_main_color));
                tv_three.setTextColor(getResources().getColor(R.color.col_333));
                break;
            case R.id.tv_three: //推广量
                sort = "9";
                refreshLayout.autoRefresh();
                tv_zero.setTextColor(getResources().getColor(R.color.col_333));
                tv_one.setTextColor(getResources().getColor(R.color.col_333));
                tv_two.setTextColor(getResources().getColor(R.color.col_333));
                tv_three.setTextColor(getResources().getColor(R.color.app_main_color));
                break;
        }
    }

    /**
     * 搜索结果
     */
    private void getTabkListNew2() {
        RequestParams requestParams = new RequestParams();
        String url = "http://v2.api.haodanku.com/supersearch/apikey/dmooo/keyword/" + URLEncoder.encode(URLEncoder.encode(content) + "/sort/" + sort);
        HttpUtils.get(SearchResultActivity.this, url, requestParams, new TextHttpResponseHandler() {
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
                LogUtils.d("sdfwfr23rew", "dsfasdf");
                indexNum++;
                getTabkListNew();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject object = new JSONObject(responseString);
                    if ("1".equals(object.getString("code"))) {
                        min_id = object.getString("min_id");
                        tb_p = object.getString("tb_p");
                        JSONArray array = object.getJSONArray("data");
                        if (indexNum == 1) {
                            taobaoGuesChildtBeans.clear();
                        }
                        TaobaoGuestBean.TaobaoGuesChildtBean bean;
                        JSONObject object1;
                        for (int i = 0; i < array.length(); i++) {
                            bean = new TaobaoGuestBean.TaobaoGuesChildtBean();
                            try {
                                object1 = array.getJSONObject(i);
                                bean.setCommission_rate(((int) (Double.valueOf(object1.getString("tkrates").replace(".00", "")) * 100) + ""));
                                bean.setNum_iid(object1.getString("itemid"));
                                bean.setNick(object1.getString("shopname"));
                                bean.setPict_url(object1.getString("itempic") + "_310x310.jpg");
                                bean.setPic_url(object1.getString("itempic") + "_310x310.jpg");
                                bean.setTitle(object1.getString("itemtitle"));
                                bean.setVolume(object1.getString("itemsale"));
                                bean.setZk_final_price(object1.getString("itemprice"));
                                bean.setCoupon_amount(object1.getString("couponmoney"));
                                taobaoGuesChildtBeans.add(bean);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        shopRecyclerAdapter.notifyDataSetChanged();
                    } else {
//                        showToast("数据已获取完毕或获取数据失败！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    /**
     * 搜索结果
     */
    private void getTabkListNew() {
        if (indexNum == 1) {
            getTabkListNew2();
            return;
        }
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("search", content);//
////        if(type==1){
//        requestParams.put("has_coupon", "true");
//        //0 天猫 1 淘宝  -1 都不是
//        if (tmall == 0) {
//            requestParams.put("is_tmall", "true");
//        } else if (tmall == 1) {
//            requestParams.put("is_tmall", "false");
//        }
//
//        // stMoney,enMoney,shareOne,shareTwo
//        if (TextUtils.isEmpty(stMoney)) {
//            requestParams.put("start_price", "");
//        } else {
//            requestParams.put("start_price", stMoney);
//        }
//        if (TextUtils.isEmpty(enMoney)) {
//            requestParams.put("end_price", "");
//        } else {
//            requestParams.put("end_price", enMoney);
//        }
//        requestParams.put("start_tk_rate", StringUtils.doStringToInt(shareOne) * 100);
//        requestParams.put("end_tk_rate", StringUtils.doStringToInt(shareTwo) * 100);
//        requestParams.put("platform", "2");
////        }
//        requestParams.put("page_no", indexNum);
//        requestParams.put("page_size", 10);
//        if (!TextUtils.isEmpty(sort)) {
//            requestParams.put("sort", sort);
//
//        }
        RequestParams requestParams = new RequestParams();
//        String url = "http://v2.api.haodanku.com/supersearch/apikey/dmooo/keyword/" + URLEncoder.encode(URLEncoder.encode(content));
        String url = "http://v2.api.haodanku.com/supersearch/apikey/dmooo/keyword/" + URLEncoder.encode(URLEncoder.encode(content)) + "/back/50/min_id/" + (indexNum > 1 ? min_id : indexNum) + "/tb_p/" + (indexNum > 1 ? tb_p : indexNum) + "/sort/" + sort + "/is_coupon/1";
        HttpUtils.get(SearchResultActivity.this, url, requestParams, new TextHttpResponseHandler() {
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
                LogUtils.d("dsfsad", responseString);
                try {
                    JSONObject object = new JSONObject(responseString);
                    if ("1".equals(object.getString("code"))) {
                        min_id = object.getString("min_id");
                        tb_p = object.getString("tb_p");
                        JSONArray array = object.getJSONArray("data");
                        if (indexNum == 1) {
                            taobaoGuesChildtBeans.clear();
                        }
                        TaobaoGuestBean.TaobaoGuesChildtBean bean;
                        JSONObject object1;
                        for (int i = 0; i < array.length(); i++) {
                            bean = new TaobaoGuestBean.TaobaoGuesChildtBean();
                            try {
                                object1 = array.getJSONObject(i);
                                bean.setCommission_rate(((int) (Double.valueOf(object1.getString("tkrates").replace(".00", "")) * 100) + ""));
                                bean.setNum_iid(object1.getString("itemid"));
                                bean.setNick(object1.getString("shopname"));
                                bean.setPict_url(object1.getString("itempic") + "_310x310.jpg");
                                bean.setPic_url(object1.getString("itempic") + "_310x310.jpg");
                                bean.setTitle(object1.getString("itemtitle"));
                                bean.setVolume(object1.getString("itemsale"));
                                bean.setZk_final_price(object1.getString("itemprice"));
                                bean.setCoupon_amount(object1.getString("couponmoney"));
                                taobaoGuesChildtBeans.add(bean);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        shopRecyclerAdapter.notifyDataSetChanged();
                    } else {
//                        showToast("获取数据失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    /**
     * 按照劵后价进行排序  true的话为升序   false为降序
     *
     * @param list
     * @param sortType
     */
    private void sortPrice(List<TaobaoGuestBean.TaobaoGuesChildtBean> list, boolean sortType) {
        int length = list.size();
        if (sortType) {
            for (int i = 0; i < length - 1; i++) {
                for (int j = 0; j < length - 1; j++) {          //升序排列
                    TaobaoGuestBean.TaobaoGuesChildtBean jT = list.get(j);
                    TaobaoGuestBean.TaobaoGuesChildtBean j1T = list.get(j + 1);
                    Double jd = StringUtils.doStringToDouble(jT.getZk_final_price()) - StringUtils.doStringToDouble(jT.getCoupon_amount());
                    Double j1d = StringUtils.doStringToDouble(j1T.getZk_final_price()) - StringUtils.doStringToDouble(j1T.getCoupon_amount());

                    if (jd < j1d) {
                        TaobaoGuestBean.TaobaoGuesChildtBean t = jT;
                        list.set(j, j1T);
                        list.set(j + 1, t);
                    }
                }
            }
        } else {
            for (int i = 0; i < length - 1; i++) {                       // 倒序排列
                for (int j = 0; j < length - 1; j++) {
                    TaobaoGuestBean.TaobaoGuesChildtBean jT = list.get(j);
                    TaobaoGuestBean.TaobaoGuesChildtBean j1T = list.get(j + 1);
                    Double jd = StringUtils.doStringToDouble(jT.getZk_final_price()) - StringUtils.doStringToDouble(jT.getCoupon_amount());
                    Double j1d = StringUtils.doStringToDouble(j1T.getZk_final_price()) - StringUtils.doStringToDouble(j1T.getCoupon_amount());
                    if (jd > j1d) {
                        TaobaoGuestBean.TaobaoGuesChildtBean t = jT;
                        list.set(j, j1T);
                        list.set(j + 1, t);
                    }
                }
            }
        }
    }


    /**
     * @属性:获取屏幕高度的方法
     * @开发者:陈飞
     * @时间:2018/8/7 14:22
     */
    public int getScollYDistance() {
        int position = linearLayoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = linearLayoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
