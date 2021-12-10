package com.android.jdhshop.my;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.activity.JdDetailsActivity;
import com.android.jdhshop.activity.PddDetailsActivity;
import com.android.jdhshop.activity.PromotionDetailsActivity;
import com.android.jdhshop.adapter.JDAdapterList;
import com.android.jdhshop.adapter.PddRecyclerAdapter;
import com.android.jdhshop.adapter.ShopRecyclerAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.bean.MyGoodsResp;
import com.android.jdhshop.bean.PDDBean;
import com.android.jdhshop.bean.PddClient;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.TaobaoGuestBean;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.login.WelActivity;
import com.android.jdhshop.utils.OkHttpUtils;
import com.android.jdhshop.utils.SlideRecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;

/**
 * @属性:我的收藏
 * @开发者:wmm
 * @时间:2018/12/11 11:10
 */
public class CollectionActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    SlideRecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.rg_type)
    RadioGroup rgType;
    @BindView(R.id.wushuju_image)
    ImageView wushuju_imagee;
    @BindView(R.id.wushuju_text)
    TextView wushuju_textT;
    @BindView(R.id.recyclerView_jd)
    SlideRecyclerView recyclerViewJd;
    @BindView(R.id.recyclerView_pdd)
    SlideRecyclerView recyclerViewPdd;
    private View view;
    public static CollectionActivity fragment;
    private ShopRecyclerAdapter shopRecyclerAdapter;
    List<TaobaoGuestBean.TaobaoGuesChildtBean> taobaoGuesChildtBeans = new ArrayList<>();

    private JDAdapterList jdAdapterList;
    List<MyGoodsResp> jdList = new ArrayList<>();
    private GridLayoutManager gridrLayoutManager;

    private boolean hasdata = true;
    private PddRecyclerAdapter pddAdapter;
    List<PDDBean> pddList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    DecimalFormat df = new DecimalFormat("0.00");
    Gson gson = new Gson();
    private int page1 = 1, page2 = 1, page3 = 1;

    @Override
    protected void initUI() {
        setContentView(R.layout.fragment_consultation);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        //设置标题
        tvTitle.setText("我的收藏");
        tvLeft.setVisibility(View.VISIBLE);
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //适配器
        shopRecyclerAdapter = new ShopRecyclerAdapter(this, R.layout.today_highlights_child_item, taobaoGuesChildtBeans);
        //管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //添加分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(shopRecyclerAdapter);
        //取消加载
        //开始刷新
        refreshLayout.autoRefresh();


        jdAdapterList = new JDAdapterList(this, R.layout.item_jd, jdList);
        gridrLayoutManager = new GridLayoutManager(this, 2);
        gridrLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewJd.setLayoutManager(gridrLayoutManager);
        recyclerViewJd.setAdapter(jdAdapterList);

        pddAdapter = new PddRecyclerAdapter(this, R.layout.pdd_item, pddList);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewPdd.setLayoutManager(linearLayoutManager);
        recyclerViewPdd.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerViewPdd.setAdapter(pddAdapter);
        BaseLogDZiYuan.LogDingZiYuan(wushuju_imagee, "wushuju.png");

    }

    @Override
    protected void initListener() {
        shopRecyclerAdapter.setOnDeleteClickListener(new ShopRecyclerAdapter.OnDeleteClickLister() {
            @Override
            public void onDeleteClick(View view, int position) {
                cancelCollectRequest(1, position);
            }
        });
        jdAdapterList.setOnDeleteClickListener(new ShopRecyclerAdapter.OnDeleteClickLister() {
            @Override
            public void onDeleteClick(View view, int position) {
                cancelCollectRequest(3, position);
            }
        });
        pddAdapter.setOnDeleteClickListener(new ShopRecyclerAdapter.OnDeleteClickLister() {
            @Override
            public void onDeleteClick(View view, int position) {
                cancelCollectRequest(2, position);
            }
        });
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (TextUtils.isEmpty(SPUtils.getStringData(CollectionActivity.this, Constants.TOKEN, ""))) {
                    openActivity(WelActivity.class);
                    refreshLayout.finishRefresh();
                    return;
                }
                if (recyclerView.getVisibility() == View.VISIBLE) {
                    page1++;
                    getTbkListRequst();
                } else if (recyclerViewJd.getVisibility() == View.VISIBLE) {
                    if (hasdata) {
                        page2++;
                        getJdCollect();
                    } else {
                        wushuju_imagee.setVisibility(View.GONE);
                        wushuju_textT.setVisibility(View.GONE);
                        refreshLayout.finishLoadMore();
                    }

                } else if (recyclerViewPdd.getVisibility() == View.VISIBLE) {
                    page3++;
                    getPddCollect();
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (TextUtils.isEmpty(SPUtils.getStringData(CollectionActivity.this, Constants.TOKEN, ""))) {
                    openActivity(WelActivity.class);
                    refreshLayout.finishRefresh();
                    return;
                }
                if (recyclerView.getVisibility() == View.VISIBLE) {
                    page1 = 1;
                    getTbkListRequst();
                } else if (recyclerViewJd.getVisibility() == View.VISIBLE) {
                    page2 = 1;
                    getJdCollect();
                    hasdata = true;
                } else if (recyclerViewPdd.getVisibility() == View.VISIBLE) {
                    page3 = 1;
                    getPddCollect();
                }
            }
        });
        //点击进入详情
        shopRecyclerAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                TaobaoGuestBean.TaobaoGuesChildtBean taobaoGuesChildtBean = taobaoGuesChildtBeans.get(position);
                if (taobaoGuesChildtBean != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("num_iid", taobaoGuesChildtBean.getNum_iid());
                    openActivity(PromotionDetailsActivity.class, bundle);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        //点击进入详情
        jdAdapterList.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                MyGoodsResp taobaoGuesChildtBean = jdList.get(position);
                if (taobaoGuesChildtBean != null) {
                    Intent intent = new Intent(CollectionActivity.this, JdDetailsActivity.class);
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
        //点击进入详情
        pddAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                PDDBean taobaoGuesChildtBean = pddList.get(position);
                LogUtils.d(TAG, "onItemClick: " + taobaoGuesChildtBean.getGoodsSign());
                //  Y9z2libOyt5KLoQRwfDZfjGN6WbyJ8Vz_JZO1srbWc

                if (taobaoGuesChildtBean != null) {
                    Intent intent = new Intent(CollectionActivity.this, PddDetailsActivity.class);
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
        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_my:
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerViewJd.setVisibility(View.GONE);
                        recyclerViewPdd.setVisibility(View.GONE);
                        refreshLayout.autoRefresh();
                        wushuju_imagee.setVisibility(View.GONE);
                        wushuju_textT.setVisibility(View.GONE);
                        if (taobaoGuesChildtBeans.size() <= 0) {
                            refreshLayout.autoRefresh();
                        }
                        break;
                    case R.id.rb_direct:
                        recyclerView.setVisibility(View.GONE);
                        recyclerViewJd.setVisibility(View.VISIBLE);
                        recyclerViewPdd.setVisibility(View.GONE);
                        refreshLayout.autoRefresh();
                        wushuju_imagee.setVisibility(View.GONE);
                        wushuju_textT.setVisibility(View.GONE);
                        if (jdList.size() <= 0) {
                            refreshLayout.autoRefresh();
                        }
                        break;
                    case R.id.rb_next:
                        recyclerView.setVisibility(View.GONE);
                        recyclerViewJd.setVisibility(View.GONE);
                        recyclerViewPdd.setVisibility(View.VISIBLE);
                        refreshLayout.autoRefresh();
                        wushuju_imagee.setVisibility(View.GONE);
                        wushuju_textT.setVisibility(View.GONE);
                        if (pddList.size() <= 0) {
                            refreshLayout.autoRefresh();
                        }
                        break;
                }
            }
        });
    }

    @Override
    protected void ReceiverIsLoginMessage() {
        super.ReceiverIsLoginMessage();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (refreshLayout != null) {
                    wushuju_imagee.setVisibility(View.GONE);
                    wushuju_textT.setVisibility(View.GONE);
                    refreshLayout.autoRefresh();
                }
            }
        }, 1000);
    }

    @Override
    protected void ReceiverBroadCastMessage(String status, String result, Serializable serializable, Intent intent) {
        super.ReceiverBroadCastMessage(status, result, serializable, intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 获取京东收藏ids
     */
    private void getJdCollect() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("p", page2);
        requestParams.put("per", 6);
        HttpUtils.post(Constants.GETJD_COLLECT_LIST, CollectionActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject object = new JSONObject(responseString);
                    LogUtils.d("tttttttttt", responseString);
                    if (object.getInt("code") == 0) {

                        String string = object.getString("data");

                        if (string.equals("[]")) {

                            wushuju_imagee.setVisibility(View.VISIBLE);
                            wushuju_textT.setVisibility(View.VISIBLE);
                            //T.showShort(CollectionActivity.this,"暂无更多收藏");
                            if (page2 == 1)
                                jdList.clear();
                            jdAdapterList.notifyDataSetChanged();
                            refreshLayout.finishRefresh();
                            refreshLayout.finishLoadMore();
                            return;
                        }
                        wushuju_imagee.setVisibility(View.GONE);
                        wushuju_textT.setVisibility(View.GONE);
                        String[] strs = object.getJSONObject("data").getString("goods_allid").split(",");
                        Long[] ids = new Long[strs.length];
                        for (int i = 0; i < strs.length; i++) {
                            ids[i] = Long.valueOf(strs[i]);
                        }
                        if (jdList.size() <= 0) {
                            hasdata = false;
                        }
                        getJdGoodsMsgRequest(ids);
                        jdAdapterList.notifyDataSetChanged();
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getPddCollect() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("p", page3);
        requestParams.put("per", 6);
        HttpUtils.post(Constants.GETPDD_COLLECT_LIST, CollectionActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("dsfasdf", responseString);
                try {
                    JSONObject object = new JSONObject(responseString);
                    if (object.getInt("code") == 0) {
                        String string = object.getJSONObject("data").getString("count");
                        if (string.equals("0")) {
                            refreshLayout.finishRefresh();
                            refreshLayout.finishLoadMore();
                            wushuju_imagee.setVisibility(View.VISIBLE);
                            wushuju_textT.setVisibility(View.VISIBLE);
                            //T.showShort(CollectionActivity.this,"暂无更多收藏");
                            if (page3 == 1)
                                pddList.clear();
                            pddAdapter.notifyDataSetChanged();
                            return;
                        }
                        wushuju_imagee.setVisibility(View.GONE);
                        wushuju_textT.setVisibility(View.GONE);
                        getPddList(object.getJSONObject("data").getString("goods_allid"));
                    } else {
                        wushuju_imagee.setVisibility(View.GONE);
                        wushuju_textT.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * @属性:获取Pdd商品列表
     * @开发者:wmm
     * @时间:2018/12/11 15:30
     */
    private void getPddList(String ids) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);

        /* 原逻辑变改 */
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("data_type", PddClient.data_type);
//        requestParams.put("client_id", PddClient.client_id);
//        requestParams.put("timestamp", time);
//        requestParams.put("with_coupon", "true");
//        requestParams.put("goods_sign_list", "["+ids+"]");
//        requestParams.put("type", "pdd.ddk.goods.basic.info.get");
//        Map<String, Object> temp = new HashMap<>();
//        temp.put("data_type", PddClient.data_type);
//        temp.put("type", "pdd.ddk.goods.basic.info.get");
//        temp.put("client_id", PddClient.client_id);
//        temp.put("timestamp", time);
//        temp.put("goods_sign_list",  "["+ids+"]");
//        temp.put("with_coupon", "true");
//        String sign = PddClient.getSign(temp);
//        requestParams.put("sign", sign);
        /* 启用新逻辑 */

        RequestParams requestParams = new RequestParams();
        requestParams.put("data_type", PddClient.data_type);
        requestParams.put("type", "pdd.ddk.goods.search"); // 改用多多
        requestParams.put("client_id", PddClient.client_id);
        requestParams.put("timestamp", time);
        requestParams.put("goods_sign_list", ids);
        requestParams.put("with_coupon", "false");

        Map<String, Object> temp = new HashMap<>();

        temp.put("data_type", PddClient.data_type);
        temp.put("type", "pdd.ddk.goods.search");
        temp.put("client_id", PddClient.client_id);
        temp.put("timestamp", time);
        temp.put("goods_sign_list", ids);
        temp.put("with_coupon", "false");

        String sign = PddClient.getSign(temp);
        requestParams.put("sign", sign);

        HttpUtils.post1(PddClient.serverUrl, CollectionActivity.this, requestParams, new TextHttpResponseHandler() {
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
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("fdasdfas", responseString);

                if (responseString.contains("error_response")) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    JSONArray array = jsonObject.getJSONObject("goods_search_response").getJSONArray("goods_list");

                    JSONObject tempObj;
                    if (page3 == 1)
                        pddList.clear();
                    for (int i = 0; i < array.length(); i++) {
                        tempObj = array.getJSONObject(i);
//                        double tem = (Double.valueOf(tempObj.getString("min_group_price")) - Double.valueOf(tempObj.getString("coupon_discount"))) * Double.valueOf(df.format(Double.valueOf(tempObj.getString("promotion_rate")) / 1000));
//                        tempObj.put("commission", df.format(tem * SPUtils.getIntData(CollectionActivity.this, "rate", 0) / 100));
                        pddList.add(gson.fromJson(tempObj.toString(), PDDBean.class));
                    }
                    pddAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            jdAdapterList.notifyDataSetChanged();
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
            super.handleMessage(msg);
        }
    };

    /**
     * @属性:获取京东品详情
     * @开发者:wmm
     * @时间:2018/12/11 2018/12/11 15:30
     */
    private void getJdGoodsMsgRequest(Long[] ids) {
        if (ids.length == 0) {
            handler.sendEmptyMessage(0);
            return;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("apikey", Constants.JD_APP_KEY_NEW);
        requestParams.put("pageindex", page2);
        requestParams.put("pagesize", "50");
        requestParams.put("isunion", "1");
        String id1s = "";
        for (int i = 0; i < ids.length; i++) {
            id1s += ids[i] + ",";
        }
        requestParams.put("goods_ids", id1s.substring(0, id1s.length() - 1));
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
                    if (page2 == 1) {
                        jdList.clear();
                    }
                    JSONArray array = object1.getJSONObject("data").getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        MyGoodsResp resp = new Gson().fromJson(array.getJSONObject(i).toString(), MyGoodsResp.class);
                        try {
                            if (Double.valueOf(resp.priceInfo.getPrice()) - Double.valueOf(resp.couponInfo.getCouponList()[0].getDiscount()) < 0) {
                                continue;
                            }
                            jdList.add(resp);
                        } catch (Exception e) {
                            jdList.add(resp);
                        }
                    }
                    handler.sendEmptyMessage(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * @属性:获取淘宝客商品列表
     * @开发者:陈飞
     * @时间:2018/7/26 17:05
     */
    private void getTbkListRequst() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("p", page1);
        requestParams.put("per", 6);
        HttpUtils.post(Constants.MESSAGE_GOODSCOLLECT_GETCOLLECTLIST_URL, CollectionActivity.this, requestParams, new onOKJsonHttpResponseHandler<TaobaoGuestBean>(new TypeToken<Response<TaobaoGuestBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onSuccess(int statusCode, Response<TaobaoGuestBean> datas) {
                if (datas.isSuccess()) {
                    List<TaobaoGuestBean.TaobaoGuesChildtBean> list = datas.getData().getList();

                    if (page1 == 1) {
                        taobaoGuesChildtBeans.clear();
                    }
                    taobaoGuesChildtBeans.addAll(list);
                    if (taobaoGuesChildtBeans.size() == 0 || taobaoGuesChildtBeans == null) {
                        wushuju_imagee.setVisibility(View.VISIBLE);
                        wushuju_textT.setVisibility(View.VISIBLE);
                        //showToast("暂无更多收藏");

                    }
                } else {
                    showToast(datas.getMsg());
                }
                shopRecyclerAdapter.notifyDataSetChanged();
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadMore();
                }
            }
        });
    }

    /**
     * 取消收藏
     *
     * @param position
     */
    private void cancelCollectRequest(final int type, final int position) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("goods_id", type == 1 ? taobaoGuesChildtBeans.get(position).getNum_iid() : type == 2 ? pddList.get(position).getGoods_id() : String.valueOf(jdList.get(position).getSkuId()));
        HttpUtils.post(type == 1 ? Constants.MESSAGE_GOODSCOLLECT_CANCELCOLLECT_URL : type == 2 ? Constants.DE_COLLECT_PDD_GOOD : Constants.DE_COLLECT_JD_GOOD, CollectionActivity.this, requestParams, new TextHttpResponseHandler() {
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
                        if (type == 1) {
                            taobaoGuesChildtBeans.remove(position);
                            shopRecyclerAdapter.notifyDataSetChanged();
                            recyclerView.closeMenu();
                        } else if (type == 2) {
                            pddList.remove(position);
                            pddAdapter.notifyDataSetChanged();
                            recyclerViewPdd.closeMenu();
                        } else {
                            jdList.remove(position);
                            jdAdapterList.notifyDataSetChanged();
                            recyclerViewJd.closeMenu();
                        }
                    } else {
                        showToast(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}