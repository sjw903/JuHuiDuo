package com.android.jdhshop.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.common.LogUtils;
import com.google.gson.Gson;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.JDAdapterList;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.MyGoodsResp;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.utils.DrawableCenterTextView;
import com.android.jdhshop.utils.OkHttpUtils;
import com.android.jdhshop.utils.UIUtils;
import com.android.jdhshop.widget.AutoClearEditText;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jd.union.open.goods.query.request.GoodsReq;
import jd.union.open.goods.query.request.UnionOpenGoodsQueryRequest;
import jd.union.open.goods.query.response.GoodsResp;
import jd.union.open.goods.query.response.UnionOpenGoodsQueryResponse;
import okhttp3.Call;
import okhttp3.Callback;

/**
 * 搜索商品页
 *
 * @开发者:wmm
 * @时间:2018/12-10 9:45
 */
public class JdSearchRestultActivity extends BaseActivity {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.right_icon)
    ImageView rightIcon;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    AutoClearEditText tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.bg_head)
    LinearLayout bgHead;
    private JDAdapterList shopRecyclerAdapter;
    List<MyGoodsResp> taobaoGuesChildtBeans = new ArrayList<>();
    private int indexNum = 1;
    private GridLayoutManager linearLayoutManager;
    private boolean hasdata = true;
    Gson gson = new Gson();
    private String keys = "";
    private TextView[] textViews;
    @BindView(R.id.jiage_st)
    DrawableCenterTextView jiageSt;
    @BindView(R.id.xiaoliang_st)
    DrawableCenterTextView xiaoliangSt;
    @BindView(R.id.yongjin_st)
    DrawableCenterTextView yongjinSt;
    @BindView(R.id.tuiguang_st)
    DrawableCenterTextView tuiguangSt;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            shopRecyclerAdapter.notifyDataSetChanged();
            super.handleMessage(msg);
        }
    };
    private String sort = "";
    private String sort_gz = "desc";
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_search_jd);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.lite_blue));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        tuiguangSt.setVisibility(View.GONE);
        yongjinSt.setText("奖");
        textViews = new TextView[]{jiageSt, xiaoliangSt, yongjinSt};
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        bgHead.setBackgroundColor(getResources().getColor(R.color.lite_blue));
        //动态设置drawableLeft属性
        Drawable drawable1 = getResources().getDrawable(R.mipmap.icon_back_while);
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
        tvLeft.setCompoundDrawables(drawable1, null, null, null);
        tvLeft.setVisibility(View.VISIBLE);
        //tvRight.setVisibility(View.GONE);
        tvTitle.setBackground(getResources().getDrawable(R.drawable.bg_border_white_16dp));
        tvTitle.setTextColor(Color.BLACK);
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        keys = getIntent().getStringExtra("key");
        tvTitle.setText(keys);
        tvTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!TextUtils.isEmpty(tvTitle.getText().toString().trim())) {
                    ((InputMethodManager) tvTitle.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    getComeActivity()
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    keys = tvTitle.getText().toString().trim();
                    indexNum = 1;
                    refreshLayout.autoRefresh();
                } else {
                    showToast("请输入搜索内容");
                }
                return false;
            }
        });
        BaseLogDZiYuan.LogDingZiYuan(rightIcon,"home_btn.png");
    }

    @Override
    protected void initData() {
        shopRecyclerAdapter = new JDAdapterList(this, R.layout.item_jd, taobaoGuesChildtBeans);
        linearLayoutManager = new GridLayoutManager(this, 2);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(shopRecyclerAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int screenMeasuredHeight = UIUtils.getScreenMeasuredHeight(JdSearchRestultActivity.this);
                if (getScollYDistance() >= (screenMeasuredHeight / 2)) {
                    rightIcon.setVisibility(View.VISIBLE);
                } else {
                    rightIcon.setVisibility(View.GONE);
                }
            }
        });
        refreshLayout.autoRefresh();
    }

    @Override
    protected void initListener() {
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (hasdata) {
                    indexNum++;
                    getTbkListRequst(keys);
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
                getTbkListRequst(keys);
            }
        });
        //点击进入详情
        shopRecyclerAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                MyGoodsResp taobaoGuesChildtBean = taobaoGuesChildtBeans.get(position);
                if (taobaoGuesChildtBean != null) {
                    Intent intent = new Intent(JdSearchRestultActivity.this, JdDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("goods", taobaoGuesChildtBean);
                    intent.putExtra("goods", bundle);
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
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

    protected void lazyload() { //懒加载，界面开始后刷新
        //开始刷新
        refreshLayout.autoRefresh();
    }
    /**
     * @属性:获取京东商品列表
     * @开发者:wmm
     * @时间:2018/12/10 9:50
     */
    private void getTbkListRequst(String keyword) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("apikey", Constants.JD_APP_KEY_NEW);
        requestParams.put("pageindex", indexNum);
        requestParams.put("pagesize", "50");
        requestParams.put("iscoupon", "1");
        requestParams.put("sort", sort_gz);
        requestParams.put("sortname", sort);
        requestParams.put("minprice", "30");
        requestParams.put("isunion", "1");
        requestParams.put("keyword", keyword);
        OkHttpUtils.getInstance().get(Constants.JDNEWGOODS_LIST + "?" + requestParams, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d("dsfasdf",e.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String s=response.body().string();
                LogUtils.d("筛选数据数据数据数据", s+"");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                    }
                });
                try {
                    JSONObject object1=new JSONObject(s);
                    if(indexNum==1){
                        taobaoGuesChildtBeans.clear();
                    }
                    JSONArray array=object1.getJSONObject("data").getJSONArray("data");
                    for(int i=0;i<array.length();i++){
                        MyGoodsResp resp=new Gson().fromJson(array.getJSONObject(i).toString(),MyGoodsResp.class);
                        try {
                            if (Double.valueOf(resp.priceInfo.getPrice()) - Double.valueOf(resp.couponInfo.getCouponList()[0].getDiscount()) < 0) {
                                continue;
                            }
                            taobaoGuesChildtBeans.add(resp);
                        } catch (Exception e) {
                            taobaoGuesChildtBeans.add(resp);
                        }
                    }
                    handler.sendEmptyMessage(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
//        String SERVER_URL = "https://router.jd.com/api";
//        String appKey = "094f4eeba5cd4108ba1cd4e7a6d93cc6";
//        String appSecret = "852e6f678c3b4e27a89cc9dc6e67ee01";
//        String accessToken = "";
//        final JdClient client = new DefaultJdClient(SERVER_URL, accessToken, appKey, appSecret);
//        final UnionOpenGoodsQueryRequest request = new UnionOpenGoodsQueryRequest();
//        final GoodsReq goodsReq = new GoodsReq();
////        goodsReq.setSkuIds(new Long[]{6877440L,844529L});
//        goodsReq.setPageSize(6);
//        goodsReq.setPageIndex(indexNum);
//        goodsReq.setIsCoupon(1);
//        goodsReq.setKeyword(keyword);
//        goodsReq.setSortName(sort);
//        goodsReq.setSort(sort_gz);
//        request.setGoodsReqDTO(goodsReq);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    UnionOpenGoodsQueryResponse response = client.execute(request);
//                    if (indexNum == 1) {
//                        taobaoGuesChildtBeans.clear();
//                        refreshLayout.finishRefresh();
//                    } else {
//                        refreshLayout.finishLoadMore();
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
//                        taobaoGuesChildtBeans.add(response.getData()[i]);
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
                if(sort_gz.equals("desc")){
                    sort_gz = "asc";
                    sort = "inOrderCount30Days";
                    refreshLayout.autoRefresh();
                    selectView(1);
                }else{
                    sort_gz = "desc";
                    sort = "inOrderCount30Days";
                    refreshLayout.autoRefresh();
                    selectView(1);
                }
                break;
            case R.id.jiage_st: //价格
                if(sort_gz.equals("desc")){
                    sort = "price";
                    sort_gz = "asc";
                    refreshLayout.autoRefresh();
                    selectView(0);
                }else{
                    sort = "price";
                    sort_gz = "desc";
                    refreshLayout.autoRefresh();
                    selectView(0);
                }
                break;
            case R.id.yongjin_st: //奖
                if(sort_gz.equals("desc")){
                    sort_gz = "asc";
                    sort = "commission";
                    refreshLayout.autoRefresh();
                    selectView(2);
                }else{
                    sort_gz = "desc";
                    sort = "commission";
                    refreshLayout.autoRefresh();
                    selectView(2);
                }

                break;
        }
    }
    private void selectView(int position) {
        for (TextView textView : textViews) {
            textView.setTextColor(getResources().getColor(R.color.black));
        }
        textViews[position].setTextColor(getResources().getColor(R.color.red1));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
