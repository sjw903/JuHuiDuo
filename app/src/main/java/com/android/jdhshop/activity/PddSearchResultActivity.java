package com.android.jdhshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.adapter.PddRecyclerAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.bean.PDDBean;
import com.android.jdhshop.bean.PddClient;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.utils.DrawableCenterTextView;
import com.android.jdhshop.utils.UIUtils;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;

/**
 * 搜索结果
 * Created by yohn on 2018/8/25.
 */

public class PddSearchResultActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    DecimalFormat df = new DecimalFormat("0.00");
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.right_icon)
    ImageView rightIcon;
    private PddRecyclerAdapter shopRecyclerAdapter;
    private String sort_gz = "0";
    List<PDDBean> taobaoGuesChildtBeans = new ArrayList<>();
    private int indexNum = 1;
    private String keyword = "";
    private LinearLayoutManager linearLayoutManager;
    private boolean hasdata = true;
    Gson gson = new Gson();
    private TextView[] textViews;
    @BindView(R.id.jiage_st)
    DrawableCenterTextView jiageSt;
    @BindView(R.id.xiaoliang_st)
    DrawableCenterTextView xiaoliangSt;
    @BindView(R.id.yongjin_st)
    DrawableCenterTextView yongjinSt;
    @BindView(R.id.tuiguang_st)
    DrawableCenterTextView tuiguangSt;

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_pdd_search);
        unbinder = ButterKnife.bind(this);
        ifShouQuan();
        BaseLogDZiYuan.LogDingZiYuan(rightIcon,"right_icon.png");
    }
    private void ifShouQuan() {
        RequestParams params = new RequestParams();
        HttpUtils.post(Constants.APP_IP+"/api/Pdd/judgeUser", PddSearchResultActivity.this,params, new TextHttpResponseHandler() {
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
                LogUtils.d("dfasdf",responseString);
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    //返回码
                    int code = jsonObject.optInt("code");
                    if (0 == code) {
                    } else {
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

    private void getUrl() {
        RequestParams params = new RequestParams();
        HttpUtils.post(Constants.APP_IP+"/api/Pdd/getUrl",PddSearchResultActivity.this, params, new TextHttpResponseHandler() {
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
                        Intent intent = new Intent(PddSearchResultActivity.this, WebViewActivity.class);
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
    @Override
    protected void initData() {
        tvLeft.setVisibility(View.VISIBLE);
        if (getIntent().getExtras() != null) {
            keyword = getIntent().getStringExtra("keyword");
            tvTitle.setText("搜索-" + keyword);
        } else {
            tvTitle.setText("拼多多搜索");
        }
        tuiguangSt.setVisibility(View.GONE);
        yongjinSt.setText("奖");
        textViews = new TextView[]{jiageSt, xiaoliangSt, yongjinSt};
        shopRecyclerAdapter = new PddRecyclerAdapter(getComeActivity(), R.layout.pdd_item, taobaoGuesChildtBeans);
        linearLayoutManager = new LinearLayoutManager(getComeActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getComeActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(shopRecyclerAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int screenMeasuredHeight = UIUtils.getScreenMeasuredHeight(getComeActivity());
                if (getScollYDistance() >= (screenMeasuredHeight / 2)) {
                    rightIcon.setVisibility(View.VISIBLE);
                } else {
                    rightIcon.setVisibility(View.GONE);
                }
            }
        });
        //开始刷新
        if (getIntent().getExtras() != null) {
            refreshLayout.autoRefresh();
        }
    }

    @Override
    protected void initListener() {
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
                getTbkListRequst();
            }
        });
        //点击进入详情
        shopRecyclerAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                PDDBean taobaoGuesChildtBean = taobaoGuesChildtBeans.get(position);
                if (taobaoGuesChildtBean != null) {
                    Intent intent = new Intent(PddSearchResultActivity.this, PddDetailsActivity.class);
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

    @OnClick({R.id.right_icon, R.id.tv_left, R.id.jiage_st, R.id.yongjin_st, R.id.xiaoliang_st})
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
                sort_gz = "6";
                refreshLayout.autoRefresh();
                selectView(1);
                break;
            case R.id.jiage_st: //价格
                sort_gz = "3";
                refreshLayout.autoRefresh();
                selectView(0);
                break;
            case R.id.yongjin_st: //奖
                sort_gz = "14";
                refreshLayout.autoRefresh();
                selectView(2);
                break;
            case R.id.tv_left:
                finish();
                break;
        }
    }

    private void selectView(int position) {
        for (TextView textView : textViews) {
            textView.setTextColor(getResources().getColor(R.color.black));
        }
        textViews[position].setTextColor(getResources().getColor(R.color.red1));
    }


    /**
     * @属性:获取Pdd商品列表
     * @开发者:wmm
     * @时间:2018/11/21 17:05
     */
    private void getTbkListRequst() {
        LogUtils.d(TAG, "getTbkListRequst: 是这里吗？"+PddClient.client_id);
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        RequestParams requestParams = new RequestParams();
        requestParams.put("page", indexNum + "");
        requestParams.put("page_size", "10");
        requestParams.put("keyword", keyword);
        requestParams.put("data_type", PddClient.data_type);
        requestParams.put("client_id", PddClient.client_id);
        requestParams.put("sort_type", sort_gz);
        requestParams.put("timestamp", time);
        JSONObject object=new JSONObject();
        try {
            object.put("uid",SPUtils.getStringData(this,"uid","1"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestParams.put("pid",SPUtils.getStringData(this,"pdd_pid",""));
        requestParams.put("custom_parameters",object.toString());
        requestParams.put("with_coupon", "true");
        requestParams.put("type", "pdd.ddk.goods.search");
        Map<String, Object> temp = new HashMap<>();
        temp.put("page", indexNum + "");
        temp.put("page_size", "10");
        temp.put("pid",SPUtils.getStringData(this,"pdd_pid",""));
        temp.put("custom_parameters",object.toString());
        temp.put("keyword", keyword);
        temp.put("data_type", PddClient.data_type);
        temp.put("type", "pdd.ddk.goods.search");
        temp.put("client_id", PddClient.client_id);
        temp.put("sort_type", sort_gz);
        temp.put("timestamp", time);
        temp.put("with_coupon", "true");
        String sign = PddClient.getSign(temp);
        requestParams.put("sign", sign);
        HttpUtils.post1(PddClient.serverUrl,PddSearchResultActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                closeLoadingDialog();
                if (refreshLayout != null) {
                    if (indexNum == 1) {
                        refreshLayout.finishRefresh();
                    } else {
                        refreshLayout.finishLoadMore();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (responseString.contains("error_response")) {
                    return;
                }
                if (indexNum == 1) {
                    taobaoGuesChildtBeans.clear();
                }
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    JSONArray array = jsonObject.getJSONObject("goods_search_response").getJSONArray("goods_list");
                    JSONObject tempObj;
                    for (int i = 0; i < array.length(); i++) {
                        tempObj = array.getJSONObject(i);
                        double tem = (Double.valueOf(tempObj.getString("min_group_price")) - Double.valueOf(tempObj.getString("coupon_discount"))) * Double.valueOf(df.format(Double.valueOf(tempObj.getString("promotion_rate")) / 1000));
                        tempObj.put("commission", df.format(tem * SPUtils.getIntData(PddSearchResultActivity.this, "rate", 0) / 100));
                        taobaoGuesChildtBeans.add(gson.fromJson(tempObj.toString(), PDDBean.class));
                    }
                    shopRecyclerAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
