package com.android.jdhshop.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.jdhshop.adapter.AddWeiXinQunAdapter;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.bean.WeiXinQunXinXIBean;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.utils.MyScrollView;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.XuanpinkAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.Xuanpinkbean;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

/**
         * 选品库
         * */
public class XuanpinkActivity extends BaseActivity {

    public ImageView qunliaoicon;
    private LinearLayout ly_back;
    private GridView recyclerView;
    public int page = 1;
    public String favorites_id = Constants.XUANPIN_ID;
    private boolean hasdata = true;
    private boolean bl_down;
    private boolean bl_up;
    private SmartRefreshLayout smartRefreshLayout;
    private XuanpinkAdapter xuanpinkAdapter;
    private ScrollView scro;

    public ImageView aiicon,fanhui;
    private List<WeiXinQunXinXIBean> weiXinQunXinXIBeanList = new ArrayList<>();
    private AddWeiXinQunAdapter weiXinQunAdapter;

    @Override
    protected void initUI() {
        setContentView( R.layout.activity_xuanpink );

        ly_back = findViewById( R.id.xuanpink_lyback );
        aiicon = findViewById( R.id.aiicon );
        qunliaoicon = findViewById( R.id.qunliaoicon );
        fanhui = findViewById( R.id.xuanpink_fanhui );
        recyclerView = findViewById( R.id.xuanpink_recycler );
        //scro = findViewById( R.id.scro);

        smartRefreshLayout = findViewById( R.id.refresh_layout );



        ly_back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        String zhuaqu = SPUtils.getStringData(this ,"zhuaqu", "");
        if(zhuaqu.equals("1")){
            aiicon.setVisibility(View.VISIBLE);
            qunliaoicon.setVisibility(View.VISIBLE);
        }
        aiicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(XuanpinkActivity.this, SetAssistantActivity.class);
                SPUtils.saveStringData(XuanpinkActivity.this, "zhuaqu", "0");
                aiicon.setVisibility(View.GONE);
                qunliaoicon.setVisibility(View.GONE);
                startActivity(intent);
            }
        });
        BaseLogDZiYuan.LogDingZiYuan(aiicon,"aiicon.png");
        BaseLogDZiYuan.LogDingZiYuan(qunliaoicon,"qunliaoicon.png");
        BaseLogDZiYuan.LogDingZiYuan(fanhui,"icon_back.png");
    }

    @Override
    protected void initData() {
        if (getIntent() != null && getIntent().getStringExtra("type_value")!=null){
            favorites_id = getIntent().getStringExtra("type_value");
        }
        xuanpinkAdapter = new XuanpinkAdapter( XuanpinkActivity.this,R.layout.today_highlights_child_item2,xuanpinkbeans );
        recyclerView.setAdapter( xuanpinkAdapter );
        recyclerView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putString("num_iid", xuanpinkbeans.get( i ).num_iid);
                bundle.putString("price", xuanpinkbeans.get( i ).reserve_price);
                Intent intent = new Intent(XuanpinkActivity.this, PromotionDetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        getTbkListRequst(1);

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                xuanpinkbeans.clear();
                page = 1;
                hasdata = true;
                getTbkListRequst(page);
               //refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                if(true){
                    page++;
                    getTbkListRequst(page);
                }else{
                    showToast("没有更多数据了");
                    //refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                }


            }
        });
        //添加商品选择群聊
        qunliaoicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1.创建弹出式对话框
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(XuanpinkActivity.this);    // 系统默认Dialog没有输入框
                // 获取自定义的布局
                View alertDialogView = View.inflate(XuanpinkActivity.this, R.layout.dialog_ai_xzqunliao, null);
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
                        String addid = SPUtils.getStringData(XuanpinkActivity.this, "addid", "");
                        SPUtils.saveStringData(XuanpinkActivity.this, "huoquaddid", addid+"");
                        tempDialog.dismiss();
                    }
                });

                RecyclerView addrecy = alertDialogView.findViewById(R.id.xzqunliao_recy);
                addrecy.setLayoutManager(new LinearLayoutManager(XuanpinkActivity.this));
                weiXinQunXinXIBeanList.clear();
                weiXinQunAdapter = new AddWeiXinQunAdapter(XuanpinkActivity.this, weiXinQunXinXIBeanList);
                addrecy.setAdapter(weiXinQunAdapter);
                WeiXinq();
                tempDialog.show();
            }
        });
    }

    @Override
    protected void initListener() {

    }

    public List<Xuanpinkbean> xuanpinkbeans = new ArrayList<>(  );
    private  void getTbkListRequst(int p) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("favorites_id", favorites_id);//Constants.XUANPIN_ID);
        requestParams.put("p", p);
        requestParams.put("per", "8");
        HttpUtils.post( Constants.XUANPINKU, XuanpinkActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(throwable.getMessage());
//                Log.e( "statuscode",search_content+","+ responseString+","+throwable.getMessage());
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("ssdaafsdf",responseString);
                try {
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        JSONObject data = object.getJSONObject( "data" );
                        JSONArray array = data.getJSONArray("list");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = (JSONObject) array.get( i );
                            Xuanpinkbean xuanpinkbean = new Xuanpinkbean();
                            xuanpinkbean.num_iid = obj.getString( "num_iid" );
                            xuanpinkbean.title = obj.getString( "title" );
                            xuanpinkbean.pict_url = obj.getString( "pict_url" );
//                            xuanpinkbean.small_images = obj.getString( "small_images" );
//                            xuanpinkbean.reserve_price = obj.getString( "reserve_price" );
                            try {
                                xuanpinkbean.zk_final_price = obj.getString( "zk_final_price" );
                            }catch (JSONException e){
                                xuanpinkbean.zk_final_price = "21";
                            }
                            try {
                                xuanpinkbean.volume = obj.getString( "volume" );
                            }catch (JSONException e){
                                xuanpinkbean.volume = "1";
                            }
                            try {
                                xuanpinkbean.tk_rate = obj.getString( "tk_rate" );
                            }catch (JSONException e){
                                xuanpinkbean.tk_rate="2";
                            }
                            try {
                                xuanpinkbean.coupon_amount = obj.getString( "coupon_amount" );
                            }catch (JSONException e){
                                xuanpinkbean.coupon_amount = "4";
                            }

//                            xuanpinkbean.user_type = obj.getString( "user_type" );
//                            xuanpinkbean.provcity = obj.getString( "provcity" );
//                            xuanpinkbean.item_url = obj.getString( "item_url" );
//                            xuanpinkbean.click_url = obj.getString( "click_url" );
//                            xuanpinkbean.nick = obj.getString( "nick" );
//                            xuanpinkbean.seller_id = obj.getString( "seller_id" );


//                            xuanpinkbean.zk_final_price_wap = obj.getString( "zk_final_price_wap" );
//                            xuanpinkbean.shop_title = obj.getString( "shop_title" );
//                            xuanpinkbean.event_start_time = obj.getString( "event_start_time" );
//                            xuanpinkbean.event_end_time = obj.getString( "event_end_time" );
//                            xuanpinkbean.type = obj.getString( "type" );
//                            xuanpinkbean.status = obj.getString( "status" );
//                            xuanpinkbean.category = obj.getString( "category" );
//                            xuanpinkbean.coupon_click_url = obj.getString( "coupon_click_url" );
//                            xuanpinkbean.coupon_end_time = obj.getString( "coupon_end_time" );
//                            xuanpinkbean.coupon_info = obj.getString( "coupon_info" );

//                            xuanpinkbean.coupon_start_time = obj.getString( "coupon_start_time" );
//                            xuanpinkbean.coupon_total_count = obj.getString( "coupon_total_count" );
//                            xuanpinkbean.coupon_remain_count = obj.getString( "coupon_remain_count" );
                            xuanpinkbeans.add( xuanpinkbean );
//                            xuanpinkbeans.add(new Gson().fromJson(array.getJSONObject(i).toString(), Xuanpinkbean.class));
                        }
                        if (smartRefreshLayout != null) {
                            smartRefreshLayout.finishRefresh();
                            smartRefreshLayout.finishLoadMore();
                        }
                        xuanpinkAdapter.notifyDataSetChanged();
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }
    private void WeiXinq() {
        //// Log.d(TAG, "weixinqun: ");
        RequestParams params = new RequestParams();
        params.put("status", "9");
        HttpUtils.post(Constants.getWXQinXinXI,XuanpinkActivity.this, params, new TextHttpResponseHandler() {
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
