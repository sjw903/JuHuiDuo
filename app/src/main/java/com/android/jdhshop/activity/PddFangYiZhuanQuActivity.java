package com.android.jdhshop.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jdhshop.R;
import com.android.jdhshop.adapter.AddWeiXinQunAdapter;
import com.android.jdhshop.adapter.PddFangYiRecyclerAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.bean.PDDBean;
import com.android.jdhshop.bean.WeiXinQunXinXIBean;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.utils.DrawableCenterTextView;
import com.android.jdhshop.utils.UIUtils;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
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

import static com.android.jdhshop.config.Constants.pddSearch;

/**
 * pdd防疫专区
 * Created by yohn on 2018/8/25.
 */

public class PddFangYiZhuanQuActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.aiicon)
    ImageView aiicon;
    @BindView(R.id.qunliaoicon)
    ImageView qunliaoicon;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    DecimalFormat df = new DecimalFormat("0.00");
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.right_icon)
    ImageView rightIcon;
    private PddFangYiRecyclerAdapter shopRecyclerAdapter;
    private String sort_gz = "0";
    List<PDDBean> taobaoGuesChildtBeans = new ArrayList<>();


    private int indexNum = 0;
    private String keyword = "";
    private LinearLayoutManager linearLayoutManager;
    private boolean hasdata = true;
    private List<String> goods_idList;
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

    private List<WeiXinQunXinXIBean> weiXinQunXinXIBeanList = new ArrayList<>();
    private AddWeiXinQunAdapter weiXinQunAdapter;

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_pdd_search);
        unbinder = ButterKnife.bind(this);
        BaseLogDZiYuan.LogDingZiYuan(aiicon, "aiicon.png");
        BaseLogDZiYuan.LogDingZiYuan(qunliaoicon, "qunliaoicon.png");
        BaseLogDZiYuan.LogDingZiYuan(rightIcon, "right_icon.png");
    }

    @Override
    protected void initData() {
        tvLeft.setVisibility(View.VISIBLE);
        keyword = getIntent().getStringExtra("keyword");
        tvTitle.setText(keyword);
        tuiguangSt.setVisibility(View.GONE);
        yongjinSt.setText("奖");
        findViewById(R.id.ll_root).setVisibility(View.GONE);
        textViews = new TextView[]{jiageSt, xiaoliangSt, yongjinSt};
        shopRecyclerAdapter = new PddFangYiRecyclerAdapter(getComeActivity(), R.layout.pdd_item, taobaoGuesChildtBeans);
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
        refreshLayout.autoRefresh();
        String zhuaqu = SPUtils.getStringData(this, "zhuaqu", "");
        if (zhuaqu.equals("1")) {
            aiicon.setVisibility(View.VISIBLE);
            qunliaoicon.setVisibility(View.VISIBLE);
        }
        aiicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PddFangYiZhuanQuActivity.this, SetAssistantActivity.class);
                SPUtils.saveStringData(PddFangYiZhuanQuActivity.this, "zhuaqu", "0");
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
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PddFangYiZhuanQuActivity.this);    // 系统默认Dialog没有输入框
                // 获取自定义的布局
                View alertDialogView = View.inflate(PddFangYiZhuanQuActivity.this, R.layout.dialog_ai_xzqunliao, null);
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
                        String addid = SPUtils.getStringData(PddFangYiZhuanQuActivity.this, "addid", "");
                        SPUtils.saveStringData(PddFangYiZhuanQuActivity.this, "huoquaddid", addid + "");
                        tempDialog.dismiss();
                    }
                });

                RecyclerView addrecy = alertDialogView.findViewById(R.id.xzqunliao_recy);
                addrecy.setLayoutManager(new LinearLayoutManager(PddFangYiZhuanQuActivity.this));
                weiXinQunXinXIBeanList.clear();
                weiXinQunAdapter = new AddWeiXinQunAdapter(PddFangYiZhuanQuActivity.this, weiXinQunXinXIBeanList);
                addrecy.setAdapter(weiXinQunAdapter);
                WeiXinq();
                tempDialog.show();
            }
        });
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
                indexNum = 0;
                hasdata = true;
                getTbkListRequst();
            }
        });
        //点击进入详情发生崩溃
        //点击进入详情
        shopRecyclerAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                PDDBean taobaoGuesChildtBean = taobaoGuesChildtBeans.get(position);
                if (taobaoGuesChildtBean != null) {

                    String goods_id = taobaoGuesChildtBeans.get(position).getGoods_id();
                    //showToast(goods_id+"");
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("apikey", Constants.JD_APP_KEY_NEW);
                    requestParams.put("isunion", "1");
                    requestParams.put("keyword", goods_id + "");
                    Map<String, Object> temp = new HashMap<>();
                    temp.put("apikey", Constants.JD_APP_KEY_NEW);
                    temp.put("isunion", "1");
                    temp.put("keyword", goods_id + "");
                    HttpUtils.post1(pddSearch + "", PddFangYiZhuanQuActivity.this, requestParams, new TextHttpResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Toast.makeText(PddFangYiZhuanQuActivity.this, "失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(responseString);
                                LogUtils.d("--------Goods_id", responseString + "");

                                PDDBean newPddBeans;

                                newPddBeans = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("goods_list").getJSONObject(0).toString(), PDDBean.class);

                                Intent intent = new Intent(PddFangYiZhuanQuActivity.this, PddDetailsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("goods", newPddBeans);
                                intent.putExtra("goods", bundle);

                                startActivity(intent);
                                LogUtils.d("--------Goods_id", goods_idList + "");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String msg = jsonObject.optString("message");
                        }
                    });

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
        String id = getIntent().getExtras().getString("id");
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        RequestParams requestParams = new RequestParams();
        requestParams.put("offset", indexNum);
        requestParams.put("limit", "20");
        requestParams.put("id", id + "");
        // requestParams.put("apikey", Constants.JD_APP_KEY_NEW);
        //requestParams.put("channel_type",getIntent().getExtras().getString("channel_type"));
        Map<String, Object> temp = new HashMap<>();
        temp.put("offset", indexNum);
        temp.put("limit", "20");
        //temp.put("apikey",Constants.JD_APP_KEY_NEW);
        /// temp.put("channel_type",getIntent().getExtras().getString("channel_type"));
        String href = getIntent().getExtras().getString("href");
        //String substring = href.substring(0, 63);
        //T.showShort(PddXiaoKaPianActivity.this, substring+"");
        //LogUtils.d(substring+"22222");
        //https://api-gw.haojingke.com/index.php/v1/api/pdd/getrecommendgoods
        HttpUtils.post1(href + "", PddFangYiZhuanQuActivity.this, requestParams, new TextHttpResponseHandler() {
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
                    if (indexNum == 0) {
                        refreshLayout.finishRefresh();
                    } else {
                        refreshLayout.finishLoadMore();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(PddFangYiZhuanQuActivity.this, "失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("dfasdf", responseString);
                goods_idList = new ArrayList<>();
                if (responseString.contains("error_response")) {
                    return;
                }
                if (indexNum == 0) {
                    taobaoGuesChildtBeans.clear();
                }
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    String msg = jsonObject.optString("message");
                    //shopRecyclerAdapter.notifyDataSetChanged();
                    JSONArray array = jsonObject.getJSONObject("data").getJSONArray("goods_list");

                    JSONObject tempObj;
                    for (int i = 0; i < array.length(); i++) {
                        tempObj = array.getJSONObject(i);
//                       double tem=(Double.valueOf(tempObj.getString("min_group_price"))-Double.valueOf(tempObj.getString("coupon_discount")))*Double.valueOf(df.format(Double.valueOf(tempObj.getString("promotion_rate"))/1000));
//                        tempObj.put("commission",df.format(  tem*SPUtils.getIntData(PddXiaoKaPianActivity.this,"rate",0)/100));
                        taobaoGuesChildtBeans.add(gson.fromJson(tempObj.toString(), PDDBean.class));
                        goods_idList.add(taobaoGuesChildtBeans.get(i).getGoods_id());
                    }
                    LogUtils.d("--------Goods_id", goods_idList + "");
//                    shopRecyclerAdapter = new PddXiaoKaRecyclerAdapter(getComeActivity(), R.layout.pdd_item, taobaoGuesChildtBeans);
//                    linearLayoutManager = new LinearLayoutManager(getComeActivity());
//                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//                    recyclerView.setLayoutManager(linearLayoutManager);
//                    recyclerView.addItemDecoration(new DividerItemDecoration(getComeActivity(), DividerItemDecoration.VERTICAL));
//                    recyclerView.setAdapter(shopRecyclerAdapter);
                    shopRecyclerAdapter.notifyDataSetChanged();
                    // Toast.makeText(PddXiaoKaPianActivity.this, msg+"", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                try {
//                    JSONObject jsonObject=new JSONObject(responseString);
                // JSONArray array=jsonObject.getJSONObject("goods_basic_detail_response").getJSONArray("list");
//                    JSONObject tempObj;
//                    for(int i=0;i<array.length();i++){
//                        tempObj=array.getJSONObject(i);
//                       double tem=(Double.valueOf(tempObj.getString("min_group_price"))-Double.valueOf(tempObj.getString("coupon_discount")))*Double.valueOf(df.format(Double.valueOf(tempObj.getString("promotion_rate"))/1000));
//                        tempObj.put("commission",df.format(  tem*SPUtils.getIntData(PddXiaoKaPianActivity.this,"rate",0)/100));
//                        taobaoGuesChildtBeans.add(gson.fromJson(tempObj.toString(),PddXiaoKaListBean.DataBean.GoodsListBean.class));
//                        shopRecyclerAdapter.notifyDataSetChanged();
//                    }
////
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
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

    private void WeiXinq() {
        //// Log.d(TAG, "weixinqun: ");
        RequestParams params = new RequestParams();
        params.put("status", "9");
        HttpUtils.post(Constants.getWXQinXinXI, PddFangYiZhuanQuActivity.this, params, new TextHttpResponseHandler() {
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
