package com.android.jdhshop.activity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jdhshop.adapter.AddWeiXinQunAdapter;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.bean.WeiXinQunXinXIBean;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.JDAdapterList;
import com.android.jdhshop.adapter.JinFenAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.JinFenBean;
import com.android.jdhshop.bean.MyGoodsResp;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.TeamListBean;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.utils.DrawableCenterTextView;
import com.android.jdhshop.utils.OkHttpUtils;
import com.android.jdhshop.utils.UIUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
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
public class JinfenActivity extends BaseActivity {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.aiicon)
    ImageView aiicon;
    @BindView(R.id.qunliaoicon)
    ImageView qunliaoicon;
    private JinFenAdapter shopRecyclerAdapter;
    List<JinFenBean.Item> taobaoGuesChildtBeans = new ArrayList<>();
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
    private String sort = "commissionShare";
    private String sort_gz = "desc";
    private int page=1;
    private List<WeiXinQunXinXIBean> weiXinQunXinXIBeanList = new ArrayList<>();
    private  AddWeiXinQunAdapter weiXinQunAdapter;
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_jinfen);
        ButterKnife.bind(this);
        tuiguangSt.setVisibility(View.GONE);
        yongjinSt.setText("奖");
        textViews = new TextView[]{jiageSt, xiaoliangSt, yongjinSt};
        tvLeft.setVisibility(View.VISIBLE);
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        keys = getIntent().getStringExtra("keys");
        tvTitle.setText(keys);
    }

    @Override
    protected void initData() {
        shopRecyclerAdapter = new JinFenAdapter( R.layout.item_jd, taobaoGuesChildtBeans);
        linearLayoutManager = new GridLayoutManager(this, 2);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(shopRecyclerAdapter);
        refreshLayout.autoRefresh();
        String zhuaqu = SPUtils.getStringData(this, "zhuaqu", "");
        if(zhuaqu.equals("1")){
            aiicon.setVisibility(View.VISIBLE);
            qunliaoicon.setVisibility(View.VISIBLE);
        }
        aiicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(JinfenActivity.this, SetAssistantActivity.class);
                SPUtils.saveStringData(JinfenActivity.this, "zhuaqu", "0");
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
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(JinfenActivity.this);    // 系统默认Dialog没有输入框
                // 获取自定义的布局
                View alertDialogView = View.inflate(JinfenActivity.this, R.layout.dialog_ai_xzqunliao, null);
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
                        String addid = SPUtils.getStringData(JinfenActivity.this, "addid", "");
                        SPUtils.saveStringData(JinfenActivity.this, "huoquaddid", addid+"");
                        tempDialog.dismiss();
                    }
                });

                RecyclerView addrecy = alertDialogView.findViewById(R.id.xzqunliao_recy);
                addrecy.setLayoutManager(new LinearLayoutManager(JinfenActivity.this));
                weiXinQunXinXIBeanList.clear();
                weiXinQunAdapter = new AddWeiXinQunAdapter(JinfenActivity.this, weiXinQunXinXIBeanList);
                addrecy.setAdapter(weiXinQunAdapter);
                WeiXinq();
                tempDialog.show();
            }
        });

        BaseLogDZiYuan.LogDingZiYuan(aiicon, "aiicon.png");
        BaseLogDZiYuan.LogDingZiYuan(qunliaoicon, "qunliaoicon.png");

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
                getTbkListRequst(keys);
            }
        });
        shopRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                getJdGoodsRequest(taobaoGuesChildtBeans.get(position).skuId);
            }
        });
//        //点击进入详情
//        shopRecyclerAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
////                GoodsResp taobaoGuesChildtBean = taobaoGuesChildtBeans.get(position);
////                if (taobaoGuesChildtBean != null) {
////                    Intent intent = new Intent(JinfenActivity.this, JdDetailsActivity.class);
////                    Bundle bundle = new Bundle();
////                    bundle.putSerializable("goods", taobaoGuesChildtBean);
////                    intent.putExtra("goods", bundle);
////                    startActivity(intent);
////                }
//            }
//
//            @Override
//            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
//                return false;
//            }
//        });
    }
    private void WeiXinq() {
        //// Log.d(TAG, "weixinqun: ");
        RequestParams params = new RequestParams();
        params.put("status", "9");
        HttpUtils.post(Constants.getWXQinXinXI,JinfenActivity.this, params, new TextHttpResponseHandler() {
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
     * @属性:获取京东推送商品详情
     * @开发者:wmm
     * @时间:2018/12/11 9:50
     */
    protected void getJdGoodsRequest(String id) {
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
                        handlers.sendEmptyMessage(0);
                        return;
                    }
                    for (int i = 0; i < 1; i++) {
                        MyGoodsResp resp = new Gson().fromJson(array.getJSONObject(i).toString(), MyGoodsResp.class);
                        handlers.sendEmptyMessage(1);
                        Intent intent = new Intent(JinfenActivity.this, JdDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("goods", resp);
                        intent.putExtra("goods", bundle);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private Handler handlers= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            closeLoadingDialog();
            if(msg.what==1){
            }else {
                T.showShort(JinfenActivity.this,"暂未查到该商品，或已下架");
            }
            super.handleMessage(msg);
        }
    };
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

    /**
     * @属性:获取京东商品列表
     * @开发者:wmm
     * @时间:2018/12/10 9:50
     */
    private void getTbkListRequst(String keyword) {
        RequestParams params=new RequestParams();
        params.put("eliteId",getIntent().getExtras().getString("eliteId"));
        params.put("sortName",sort);
        params.put("sort",sort_gz);
        params.put("p",indexNum);
        params.put("per","60");
        HttpUtils.post( Constants.JINFEN, JinfenActivity.this,params, new onOKJsonHttpResponseHandler<JinFenBean>(new TypeToken<Response<JinFenBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(throwable.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Response<JinFenBean> datas) {
                if (datas.isSuccess()) {
                    if(indexNum==1)
                        taobaoGuesChildtBeans.clear();
                    for(int i=0;i<datas.getData().list.size();i++){
                        if(datas.getData().list.get(i).couponInfo==null||datas.getData().list.get(i).couponInfo.couponList.size()==0){
                            continue;
                        }
                        taobaoGuesChildtBeans.add(datas.getData().list.get(i));
                    }
                } else {
                    showToast(datas.getMsg());
                }
                shopRecyclerAdapter.notifyDataSetChanged();
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    @OnClick({ R.id.jiage_st, R.id.xiaoliang_st, R.id.yongjin_st})
    public void onViewClicked(View view) { //回到头部
        switch (view.getId()) {
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
}
