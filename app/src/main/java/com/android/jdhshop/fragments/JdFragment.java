package com.android.jdhshop.fragments;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jdhshop.activity.SetAssistantActivity;
import com.android.jdhshop.adapter.AddWeiXinQunAdapter;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.bean.WeiXinQunXinXIBean;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.https.HttpUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.JdDetailsActivity;
import com.android.jdhshop.activity.JinfenActivity;
import com.android.jdhshop.adapter.JDAdapterList2;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.bean.MyGoodsResp;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.utils.DrawableCenterTextView;
import com.android.jdhshop.utils.OkHttpUtils;
import com.android.jdhshop.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;

/**
 * @属性:京东多商品页
 * @开发者:wmm
 * @时间:2018/12-07 11:36
 */
public class JdFragment extends BaseLazyFragment {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;


    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.right_icon)
    ImageView rightIcon;
    @BindView(R.id.aiicon)
    ImageView aiicon;
    @BindView(R.id.qunliaoicon)
    ImageView qunliaoicon;
    @BindView(R.id.jiage_st)
    DrawableCenterTextView jiageSt;
    @BindView(R.id.xiaoliang_st)
    DrawableCenterTextView xiaoliangSt;
    @BindView(R.id.yongjin_st)
    DrawableCenterTextView yongjinSt;
    @BindView(R.id.tuiguang_st)
    DrawableCenterTextView tuiguangSt;
    private JDAdapterList2 shopRecyclerAdapter;
    private String pid, usId;

    List<MyGoodsResp> taobaoGuesChildtBeans = new ArrayList<>();
    private int indexNum = 1;
    private GridLayoutManager linearLayoutManager;
    private boolean hasdata = true;
    Gson gson = new Gson();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            shopRecyclerAdapter.notifyDataSetChanged();
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
            super.handleMessage(msg);
        }
    };
    private TextView[] textViews;
    private String sort = "";
    private String sort_gz = "asc";
    View view;
    ACache mCache;
    private List<WeiXinQunXinXIBean> weiXinQunXinXIBeanList = new ArrayList<>();
    private AddWeiXinQunAdapter weiXinQunAdapter;

    ImageView jdtopp_image1;
    ImageView jdtop_image2;
    ImageView go_two;
    ImageView go_three;
    ImageView go_four;
    ImageView go_five;
    ImageView go_six;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pdd, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        addListener();
        BaseLogDZiYuan.LogDingZiYuan(qunliaoicon, "qunliaoicon.png");
        BaseLogDZiYuan.LogDingZiYuan(aiicon, "aiicon.png");
        BaseLogDZiYuan.LogDingZiYuan(rightIcon, "home_btn.png");

        BaseLogDZiYuan.LogDingZiYuan(jdtopp_image1, "rexiao.png");
        BaseLogDZiYuan.LogDingZiYuan(go_two, "chaozhi.png");
        BaseLogDZiYuan.LogDingZiYuan(go_three, "damaichang.png");
        BaseLogDZiYuan.LogDingZiYuan(go_four, "haoquan.png");
        BaseLogDZiYuan.LogDingZiYuan(go_five, "chaoshi.png");
        BaseLogDZiYuan.LogDingZiYuan(go_six, "jingcang.png");
        BaseLogDZiYuan.LogDingZiYuan(jdtop_image2, "go.png");
        return view;
    }

    private void init() {
        mCache = ACache.get(getContext());
        Bundle arguments = getArguments();
        LinearLayout ll = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_jd_top, null);

        jdtopp_image1 = ll.findViewById(R.id.jdtopp_image1);
        jdtop_image2 = ll.findViewById(R.id.jdtop_image2);
        go_two = ll.findViewById(R.id.go_two);
        go_three = ll.findViewById(R.id.go_three);
        go_four = ll.findViewById(R.id.go_four);
        go_five = ll.findViewById(R.id.go_five);
        go_six = ll.findViewById(R.id.go_six);


        if (arguments != null) {
            pid = arguments.getString("pid");
            usId = arguments.getString("us_id");
            if ("0".equals(getArguments().getString("index"))) {
//                view.findViewById(R.id.ll_root5).setVisibility(View.VISIBLE);
                ll.findViewById(R.id.go_one).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("eliteId", "22");
                        bundle.putString("keys", "热销爆品");
                        openActivity(JinfenActivity.class, bundle);
                    }
                });
                ll.findViewById(R.id.go_two).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("eliteId", "10");
                        bundle.putString("keys", "9.9元专区");
                        openActivity(JinfenActivity.class, bundle);
                    }
                });
                ll.findViewById(R.id.go_three).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("eliteId", "2");
                        bundle.putString("keys", "超级大卖场");
                        openActivity(JinfenActivity.class, bundle);
                    }
                });
                ll.findViewById(R.id.go_four).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("eliteId", "1");
                        bundle.putString("keys", "好券商品");
                        openActivity(JinfenActivity.class, bundle);
                    }
                });
                ll.findViewById(R.id.go_five).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("eliteId", "25");
                        bundle.putString("keys", "超市");
                        openActivity(JinfenActivity.class, bundle);
                    }
                });
                ll.findViewById(R.id.go_six).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("eliteId", "15");
                        bundle.putString("keys", "京仓配送");
                        openActivity(JinfenActivity.class, bundle);
                    }
                });
                ll.findViewById(R.id.go_seven).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("eliteId", "5");
                        bundle.putString("keys", "精选家电");
                        openActivity(JinfenActivity.class, bundle);
                    }
                });
                ll.findViewById(R.id.go_eight).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("eliteId", "13");
                        bundle.putString("keys", "数码先锋");
                        openActivity(JinfenActivity.class, bundle);
                    }
                });
                ll.findViewById(R.id.go_nine).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("eliteId", "12");
                        bundle.putString("keys", "精致生活");
                        openActivity(JinfenActivity.class, bundle);
                    }
                });
                ll.findViewById(R.id.go_ten).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("eliteId", "7");
                        bundle.putString("keys", "居家生活");
                        openActivity(JinfenActivity.class, bundle);
                    }
                });
                ll.findViewById(R.id.go_eleven).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("eliteId", "14");
                        bundle.putString("keys", "品质家电");
                        openActivity(JinfenActivity.class, bundle);
                    }
                });
                ll.findViewById(R.id.go_twelve).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("eliteId", "4");
                        bundle.putString("keys", "服装运动");
                        openActivity(JinfenActivity.class, bundle);
                    }
                });
                ll.findViewById(R.id.go_thirteen).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("eliteId", "11");
                        bundle.putString("keys", "潮流范儿");
                        openActivity(JinfenActivity.class, bundle);
                    }
                });
            }
        }
        yongjinSt.setText("返");
        yongjinSt.setVisibility(View.GONE);
        tuiguangSt.setVisibility(View.GONE);
        textViews = new TextView[]{jiageSt, xiaoliangSt, yongjinSt};
        shopRecyclerAdapter = new JDAdapterList2(R.layout.item_jd, taobaoGuesChildtBeans);
        linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (arguments != null) {
            if ("0".equals(getArguments().getString("index"))) {
                shopRecyclerAdapter.addHeaderView(ll);
            }
        }
        recyclerView.setAdapter(shopRecyclerAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int screenMeasuredHeight = UIUtils.getScreenMeasuredHeight(getActivity());
                if (getScollYDistance() >= (screenMeasuredHeight / 2)) {
                    rightIcon.setVisibility(View.VISIBLE);
                } else {
                    rightIcon.setVisibility(View.GONE);
                }
            }
        });
        String zhuaqu = SPUtils.getStringData(getContext(), "zhuaqu", "");
        if (zhuaqu.equals("1")) {
            aiicon.setVisibility(View.VISIBLE);
            qunliaoicon.setVisibility(View.VISIBLE);
        }
        aiicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SetAssistantActivity.class);
                SPUtils.saveStringData(getContext(), "zhuaqu", "0");
                aiicon.setVisibility(View.GONE);
                qunliaoicon.setVisibility(View.GONE);
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
    }

    private void addListener() {
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (hasdata) {
                    indexNum++;
                    getTbkListRequst();
                } else {
                    showToast("没有更多数据了");
                    refreshLayout.finishLoadMore(1000);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                indexNum = 1;
                hasdata = true;
                taobaoGuesChildtBeans.clear();
                getTbkListRequst();
            }
        });
        shopRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                if ("0".equals(getArguments().getString("index"))) {
//                    GoodsResp taobaoGuesChildtBean = taobaoGuesChildtBeans.get(position-1);
//                    if (taobaoGuesChildtBean != null) {
//                        Intent intent = new Intent(context, JdDetailsActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("goods", taobaoGuesChildtBean);
//                        intent.putExtra("goods", bundle);
//                        startActivity(intent);
//                    }
//                }else{
                MyGoodsResp taobaoGuesChildtBean = taobaoGuesChildtBeans.get(position);
                if (taobaoGuesChildtBean != null) {
                    Intent intent = new Intent(context, JdDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("goods", taobaoGuesChildtBean);
                    intent.putExtra("goods", bundle);
                    startActivity(intent);
                }
//                }
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        handler.removeCallbacks(null);
        handler = null;
    }

    @Override
    protected void lazyload() { //懒加载，界面开始后刷新
        //开始刷新
        refreshLayout.autoRefresh();
    }

    /**
     * @属性:获取京东商品列表
     * @开发者:wmm
     * @时间:2018/12/2607 11:50
     */
    private void getTbkListRequst() {
        String TAG = "TTAAGG";
        RequestParams requestParams = new RequestParams();

        if (mCache.getAsString("jdfragment_" + pid + "_indexNum") != null) {
            try {
                indexNum = Integer.parseInt(mCache.getAsString("jdfragment_" + pid + "_indexNum"));
            } catch (Exception e) {
                indexNum = 1;
                LogUtils.d(TAG, "getTbkListRequst: " + e.getMessage());
                LogUtils.d(TAG, "getTbkListRequst: " + mCache.getAsString("jdfragment_" + pid + "_indexNum"));
            }
        }
        LogUtils.d(TAG, "getTbkListRequst: " + indexNum);
        requestParams.put("apikey", Constants.JD_APP_KEY_NEW);
        requestParams.put("pageindex", indexNum);
        requestParams.put("pagesize", "50");
        if (usId.equals("4") || usId.equals("5") || usId.equals("6")) {
            requestParams.put("cid2", Long.valueOf(pid));
        } else if (!usId.equals("0")) {
            requestParams.put("cid1", Long.valueOf(pid));
        } else {
            requestParams.put("cid1", Long.valueOf(pid));
        }
        requestParams.put("iscoupon", "1");
        requestParams.put("sort", sort_gz);
        requestParams.put("sortname", sort);
        requestParams.put("minprice", "30");
        requestParams.put("isunion", "1");
//sortname	string	否	1 单价 2佣金比例 3佣金 4销量
//sort	string	否	asc 升序 desc 降序 默认降序
//ispg	string	否	是否拼购 1拼购
//iscoupon	int	否	是否有券 1只查有券 其他查全部
//minpgpirce	number	否	最小拼购金额
//maxpgpirce	number	否	最大拼购金额
//ishot	number	否	1爆品0非爆品
//owner	string	否	g 自营 ，p POP
//brandcode	number	否	品牌code
//shopid	number	否	店铺Id
//isunion	string	否	isunion=1 表示返回京东原接口数据不做处理 返回值参考京东联盟 官方文档
        LogUtils.d("TAG", "京东商品-列表请求网址: " + Constants.JDNEWGOODS_LIST);
        LogUtils.d("TAG", "京东商品-请求参数: " + requestParams.toString());
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

                    if (indexNum == 1) {
                        taobaoGuesChildtBeans.clear();
                    }

                    if ("-200".equals(object1.getString("status_code"))) {
                        if (indexNum != 1) {
                            mCache.remove("jdfragment_" + pid + "_indexNum");
                            getTbkListRequst();
                        } else {
                            LogUtils.d(TAG, "onResponse: 没有商品数据!");
                        }
                    }

                    JSONArray array = object1.getJSONObject("data").getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        MyGoodsResp resp = new Gson().fromJson(array.getJSONObject(i).toString(), MyGoodsResp.class);
                        try {
                            if (Double.valueOf(resp.priceInfo.getPrice()) - Double.valueOf(resp.couponInfo.getCouponList()[0].getDiscount()) < 0) {
                                continue;
                            }
                            taobaoGuesChildtBeans.add(resp);
                        } catch (Exception e) {
                            taobaoGuesChildtBeans.add(resp);
                        }
                    }
                    mCache.put("jdfragment_" + pid + "_indexNum", "" + (indexNum + 1));
                    if (null != handler)
                        handler.sendEmptyMessage(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
//        String SERVER_URL = "https://router.jd.com/api";
//        String appKey = Constants.JD_CLIENT_ID;
//        String appSecret = Constants.JD_SECRET;
//        String accessToken = "";
//        final JdClient client = new DefaultJdClient(SERVER_URL, accessToken, appKey, appSecret);
//        final UnionOpenGoodsQueryRequest request = new UnionOpenGoodsQueryRequest();
//        final GoodsReq goodsReq = new GoodsReq();
//        goodsReq.setPageSize(40);
//        goodsReq.setPageIndex(indexNum);
//        goodsReq.setIsCoupon(1);
//        goodsReq.setIsHot(0);
//        goodsReq.setOwner("p");
//        goodsReq.setSortName(sort);
//        goodsReq.setSort(sort_gz);
//        goodsReq.setPricefrom(30.0);
//        if (usId.equals("4") || usId.equals("5") || usId.equals("6")) {
//            goodsReq.setCid2(Long.valueOf(pid));
//        } else if (!usId.equals("0")) {
//            goodsReq.setCid1(Long.valueOf(pid));
//        } else {
////            goodsReq.setIsHot(1);
//            goodsReq.setCid1(Long.valueOf(pid));
//        }
//        request.setGoodsReqDTO(goodsReq);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    UnionOpenGoodsQueryResponse response = client.execute(request);
//                    if (indexNum == 1) {
//                        taobaoGuesChildtBeans.clear();
//                        if (refreshLayout != null)
//                            refreshLayout.finishRefresh();
//                    } else {
//                        if (refreshLayout != null)
//                            refreshLayout.finishLoadMore();
//                    }
//                    if (response.getData() == null) {
//                        hasdata = false;
//                        return;
//                    }
//                    if (response.getData().length <= 0) {
//                        hasdata = false;
//                    }
//                    int length = response.getData().length;
//                    for (int i = 0; i < length; i++) {
//                        try {
//                            if(Double.valueOf(response.getData()[i].getPriceInfo()[0].getPrice())-Double.valueOf(response.getData()[i].getCouponInfo()[0].getCouponList()[0].getDiscount())<0){
//                                continue;
//                            }
//                            taobaoGuesChildtBeans.add(response.getData()[i]);
//                        } catch (Exception e) {
//                            taobaoGuesChildtBeans.add(response.getData()[i]);
//                        }
//                    }
//                    handler.sendEmptyMessage(0);
//                } catch (JdException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

    @OnClick({R.id.right_icon, R.id.jiage_st, R.id.xiaoliang_st, R.id.yongjin_st})
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
            case R.id.xiaoliang_st: //销量
                sort_gz = "desc";
                sort = "inOrderCount30Days";
                refreshLayout.autoRefresh();
                selectView(1);
                break;
            case R.id.jiage_st: //价格
                sort = "price";
                sort_gz = "asc";
                refreshLayout.autoRefresh();
                selectView(0);
                break;
            case R.id.yongjin_st: //奖
                sort_gz = "desc";
                sort = "commission";
                refreshLayout.autoRefresh();
                selectView(2);
                break;
        }
    }

    private void selectView(int position) {
        for (TextView textView : textViews) {
            textView.setTextColor(getResources().getColor(R.color.black));
        }
        textViews[position].setTextColor(getResources().getColor(R.color.red1));
    }

    private void WeiXinq() {
        //// Log.d(TAG, "weixinqun: ");
        RequestParams params = new RequestParams();
        params.put("status", "9");
        HttpUtils.post(Constants.getWXQinXinXI,JdFragment.this, params, new TextHttpResponseHandler() {
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
