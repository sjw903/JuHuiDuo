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
import android.widget.TextView;

import com.android.jdhshop.activity.SetAssistantActivity;
import com.android.jdhshop.adapter.AddWeiXinQunAdapter;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.bean.WeiXinQunXinXIBean;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.https.HttpUtils;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.JdDetailsActivity;
import com.android.jdhshop.adapter.JDAdapterList;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.bean.MyGoodsResp;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.utils.DrawableCenterTextView;
import com.android.jdhshop.utils.OkHttpUtils;
import com.android.jdhshop.utils.UIUtils;
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
import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;

/**
 * 搜索商品页
 *
 * @开发者:wmm
 * @时间:2018/12-10 9:45
 */
public class JdSearchRestultFragment extends BaseLazyFragment {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.right_icon)
    ImageView rightIcon;
    @BindView(R.id.aiicon)
    ImageView aiicon;
    @BindView(R.id.qunliaoicon)
    ImageView qunliaoicon;
    private JDAdapterList shopRecyclerAdapter;
    List<MyGoodsResp> taobaoGuesChildtBeans = new ArrayList<>();
    private int indexNum = 1;
    private GridLayoutManager linearLayoutManager;
    private boolean hasdata = true;
    Gson gson = new Gson();

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
        refreshLayout.autoRefresh();
    }

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
    private String sort = "1";
    private String sort_gz = "desc";
    View view;

    private List<WeiXinQunXinXIBean> weiXinQunXinXIBeanList = new ArrayList<>();
    private AddWeiXinQunAdapter weiXinQunAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_search_jd, container, false);
        ButterKnife.bind(this, view);
        view.findViewById(R.id.bg_head).setVisibility(View.GONE);
        initUI();
        initData();
        initListener();
        return view;
    }
    protected void initUI() {
        tuiguangSt.setVisibility(View.GONE);
        yongjinSt.setText("奖");
        textViews = new TextView[]{jiageSt, xiaoliangSt, yongjinSt};
        BaseLogDZiYuan.LogDingZiYuan(aiicon,"aiicon.png");
        BaseLogDZiYuan.LogDingZiYuan(qunliaoicon,"qunliaoicon.png");
        BaseLogDZiYuan.LogDingZiYuan(rightIcon,"home_btn.png");
    }

    protected void initData() {
        shopRecyclerAdapter = new JDAdapterList(context, R.layout.item_jd, taobaoGuesChildtBeans);
        linearLayoutManager = new GridLayoutManager(context, 2);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(shopRecyclerAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int screenMeasuredHeight = UIUtils.getScreenMeasuredHeight(context);
                if (getScollYDistance() >= (screenMeasuredHeight / 2)) {
                    rightIcon.setVisibility(View.VISIBLE);
                } else {
                    rightIcon.setVisibility(View.GONE);
                }
            }
        });
        String zhuaqu = SPUtils.getStringData(getContext(), "zhuaqu", "");
        if(zhuaqu.equals("1")){
            aiicon.setVisibility(View.VISIBLE);
            qunliaoicon.setVisibility(View.VISIBLE);
        }
        aiicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), SetAssistantActivity.class);
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
                        SPUtils.saveStringData(context, "huoquaddid", addid+"");
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
                getTbkListRequst(keys);
            }
        });
        //点击进入详情
        shopRecyclerAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                MyGoodsResp taobaoGuesChildtBean = taobaoGuesChildtBeans.get(position);
                if (taobaoGuesChildtBean != null) {
                    Intent intent = new Intent(context, JdDetailsActivity.class);
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
    private void WeiXinq() {
        //// Log.d(TAG, "weixinqun: ");
        RequestParams params = new RequestParams();
        params.put("status", "9");
        HttpUtils.post(Constants.getWXQinXinXI, JdSearchRestultFragment.this,params, new TextHttpResponseHandler() {
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
    /**
     * @属性:获取屏幕高度的方法
     * @开发者:陈飞
     * @时间:2018/8/7 14:22
     */
    public long getScollYDistance() {
        int position = linearLayoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = linearLayoutManager.findViewByPosition(position);
        if(firstVisiableChildView==null){
            return  0;
        }

        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
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
//        requestParams.put("sortname", sort);
//        requestParams.put("minprice", "30");
        requestParams.put("minprice", "30");
        requestParams.put("isunion", "1");
        requestParams.put("keyword", keyword);
        OkHttpUtils.getInstance().get(Constants.JDNEWGOODS_LIST + "?" + requestParams.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d("dsfasdf",e.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String s=response.body().string();
               getActivity(). runOnUiThread(new Runnable() {
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
                sort_gz = "desc";
                sort = "4";
                refreshLayout.autoRefresh();
                selectView(1);
                break;
            case R.id.jiage_st: //价格
                sort = "1";
                sort_gz = "asc";
                refreshLayout.autoRefresh();
                selectView(0);
                break;
            case R.id.yongjin_st: //奖
                sort_gz = "desc";
                sort = "3";
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

    @Override
    protected void lazyload() {

    }
}
