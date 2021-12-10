package com.android.jdhshop.fragments;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import com.android.jdhshop.R;
import com.android.jdhshop.activity.PromotionDetailsActivity;
import com.android.jdhshop.activity.SetAssistantActivity;
import com.android.jdhshop.adapter.AddWeiXinQunAdapter;
import com.android.jdhshop.adapter.NineAdapterListNew;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.bean.HaoDanBean;
import com.android.jdhshop.bean.WeiXinQunXinXIBean;
import com.android.jdhshop.common.ACache;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;

/**
 * @属性:商品页
 * @开发者:wmm
 * @时间:2018/11/16 15:18
 */
public class BaoYouFragment extends BaseLazyFragment {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private TextView[] textViews;
    private TextView currentSearchView;
    private boolean hasdata = true;
    @BindView(R.id.jiage_st)
    DrawableCenterTextView jiageSt;
    @BindView(R.id.xiaoliang_st)
    DrawableCenterTextView xiaoliangSt;
    @BindView(R.id.yongjin_st)
    DrawableCenterTextView yongjinSt;
    @BindView(R.id.tuiguang_st)
    DrawableCenterTextView tuiguangSt;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.right_icon)
    ImageView rightIcon;
    @BindView(R.id.aiicon)
    ImageView aiicon;
    @BindView(R.id.qunliaoicon)
    ImageView qunliaoicon;
    private String pid;
    private int indexNum = 1;
    private String name;
    private int status = 0;
    private String sort="0";
    private GridLayoutManager linearLayoutManager;
    private String group_id;
    private ACache mAcache;
    List<HaoDanBean> nineList = new ArrayList<>();
    private NineAdapterListNew nineAdapter;
    private String min_id="1";
    private List<WeiXinQunXinXIBean> weiXinQunXinXIBeanList = new ArrayList<>();
    private AddWeiXinQunAdapter weiXinQunAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nine, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        addListener();
        BaseLogDZiYuan.LogDingZiYuan(qunliaoicon, "qunliaoicon.png");
        BaseLogDZiYuan.LogDingZiYuan(aiicon, "aiicon.png");
        BaseLogDZiYuan.LogDingZiYuan(rightIcon, "home_btn.png");

        return view;
    }

    private void init() {
        yongjinSt.setText("佣金比例");
        textViews = new TextView[]{jiageSt, xiaoliangSt, yongjinSt, tuiguangSt};
        mAcache = ACache.get(getContext());
        group_id = mAcache.getAsString("group_id");
        Bundle arguments = getArguments();
        if (arguments != null) {
            pid = arguments.getString("pid");
            name = arguments.getString("type");
        }
        //适配器
        nineAdapter = new NineAdapterListNew(getActivity(), R.layout.item_phb, nineList);
        //管理器
        linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(nineAdapter);

        //显示回滚
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //屏幕高度
                int screenMeasuredHeight = UIUtils.getScreenMeasuredHeight(getActivity());
                //如果滚动距离大于屏幕高度，那么显示回滚，否则。。。
                if (getScollYDistance() >= (screenMeasuredHeight / 2)) {
                    rightIcon.setVisibility(View.VISIBLE);
                } else {
                    rightIcon.setVisibility(View.GONE);
                }
//                LogUtils.d("TAG", "高度为:" + getScollYDistance());

            }
        });
        String zhuaqu = SPUtils.getStringData(getContext(), "zhuaqu", "");
        if(zhuaqu.equals("2")){
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

    private void addListener() {
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                status = 0;
                if (hasdata) {
                    indexNum++;
                    getNine();
                } else {
                    showToast("没有更多数据了");
                    refreshLayout.finishLoadMore(2000);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                indexNum = 1;
                min_id="1";
                hasdata=true;
                nineList.clear();
                nineAdapter.notifyDataSetChanged();
                getNine();
            }
        });
        //点击进入详情
        nineAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                HaoDanBean bean = nineList.get(position);
                if (bean != null) {
                    DecimalFormat df=new DecimalFormat("0.00");
                    Bundle bundle = new Bundle();
                    bundle.putString("num_iid", bean.itemid);
                    bundle.putString("comm", df.format(Double.valueOf(bean.tkmoney)* Double.parseDouble(df.format((float) SPUtils.getIntData(getActivity(), "rate", 0) / 100)))+"");
                    bundle.putString("price", bean.itemendprice);
                    bundle.putSerializable("bean",bean);
                    if(Double.valueOf(bean.videoid)>0){
                        bundle.putString("tye","1");
                        bundle.putString("url",bean.videoid);
                    }
                    openActivity(PromotionDetailsActivity.class, bundle);
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
        HttpUtils.post(Constants.getWXQinXinXI,BaoYouFragment.this, params, new TextHttpResponseHandler() {
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected void lazyload() { //懒加载，界面开始后刷新
        indexNum = 1;
        hasdata=true;
        getNine();
    }

    private void getNine() {

        LogUtils.d("TAG", "nine_"+pid+"_min_id");

        LogUtils.d("TAG", "getNine: "+ mAcache.getAsString("nine_"+pid+"_min_id"));

        if (null != mAcache.getAsString("nine_"+pid+"_min_id")){
            min_id = mAcache.getAsString("nine_"+pid+"_min_id");
        }

        RequestParams requestParams = new RequestParams();
        if ("20".equals(name)) {
            requestParams.put("back", 50);
        } else {
            requestParams.put("back", 20);
        }
        requestParams.put("sort", sort);
        requestParams.put("min_id", min_id);
        if("22".equals(name)){
            requestParams.put("nav", "3");
            requestParams.put("cid", pid);
            requestParams.put("coupon_min","50");
        }else  if("23".equals(name)){
            requestParams.put("nav", "4");
            requestParams.put("cid", pid);
            requestParams.put("coupon_min","10");
        }else {
            requestParams.put("type", name.equals("20")?"1":name);
            requestParams.put("cid", pid);
        }

        HttpUtils.post(("22".equals(name)||"23".equals(name))?Constants.GET_NINEBY_NEW_HD_CJQ:Constants.GET_NINEBY_NEW_HD , BaoYouFragment.this,requestParams, new TextHttpResponseHandler() {
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
                try {
                    JSONObject object = new JSONObject(responseString);
                    if ("1".equals(object.getString("code"))) {
                        JSONArray array = object.getJSONArray("data");
                        if (min_id.equals("1")) {
                            nineList.clear();
                        }
                        min_id=object.getString("min_id");
                        mAcache.put("nine_"+pid+"_min_id",min_id,Constants.CacheSaveTime);
                        if(array.length()<=0){
                            hasdata=false;
                            return;
                        }

                        for (int i = 0; i < array.length(); i++) {
                            if("9".equals(name)&&"C".equals(array.getJSONObject(i).getString("shoptype"))){
//                                天猫店（B）淘宝店（C）
//                                continue;
                            }
                            if("20".equals(name)&&"B".equals(array.getJSONObject(i).getString("shoptype"))){
//                                天猫店（B）淘宝店（C）
//                                continue;
                            }
                            nineList.add(new Gson().fromJson(array.getJSONObject(i).toString(), HaoDanBean.class));
                        }
                        if(nineList.size()<=4){
                            getNine();
                        }
                        nineAdapter.notifyDataSetChanged();
                    }else{

                        if (!min_id.equals("1")){
                            mAcache.put("nine_"+pid+"_min_id","1",Constants.CacheSaveTime);
                            postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    getNine();
                                }
                            });

                        }else {
                            showToast("没有更多数据了");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    @OnClick({R.id.right_icon, R.id.jiage_st, R.id.xiaoliang_st, R.id.yongjin_st, R.id.tuiguang_st})
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
            case R.id.jiage_st:
                if ("1".equals(sort)) {
                    sort = "2";
                    jiageSt.setText("价格(降)");
                } else if ("2".equals(sort)) {
                    sort = "1";
                    jiageSt.setText("价格(升)");
                } else {
                    sort = "1";
                    jiageSt.setText("价格(升)");
                }
                selectView(1);
                min_id = "1";
                refreshLayout.autoRefresh();
                break;
            case R.id.xiaoliang_st:
                if ("4".equals(sort)) {
                    sort = "7";
                    xiaoliangSt.setText("销量(升)");
                } else if ("7".equals(sort)) {
                    sort = "4";
                    xiaoliangSt.setText("销量(降)");
                } else {
                    sort = "4";
                    xiaoliangSt.setText("销量(降)");
                }
                selectView(2);
                min_id = "1";
                refreshLayout.autoRefresh();
                break;
            case R.id.yongjin_st:
                if ("5".equals(sort)) {
                    yongjinSt.setText("佣金比例(升)");
                    sort = "8";
                } else if ("8".equals(sort)) {
                    sort = "5";
                    yongjinSt.setText("佣金比例(降)");
                } else {
                    sort = "5";
                    yongjinSt.setText("佣金比例(降)");
                }
                selectView(3);
                min_id = "1";
                refreshLayout.autoRefresh();
                break;
            case R.id.tuiguang_st:
                if ("6".equals(sort)) {
                    tuiguangSt.setText("推广量(升)");
                    sort = "13";
                } else if ("13".equals(sort)) {
                    sort = "6";
                    tuiguangSt.setText("推广量(降)");
                } else {
                    sort = "6";
                    tuiguangSt.setText("推广量(降)");
                }
                selectView(4);
                min_id = "1";
                refreshLayout.autoRefresh();


//                sort = "13";
//                tuiguangSt.setText("推广量(降)");
//                selectView(4);
//                min_id = "1";
//                refreshLayout.autoRefresh();
                break;
        }
    }

    private void selectView(int position) {
        for (TextView textView : textViews) {
            textView.setTextColor(getResources().getColor(R.color.black));
        }
        textViews[position - 1].setTextColor(getResources().getColor(R.color.red1));
    }

}
