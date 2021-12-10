package com.android.jdhshop.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
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
import com.android.jdhshop.activity.PddAdActivity;
import com.android.jdhshop.activity.PddDetailsActivity;
import com.android.jdhshop.activity.PddFangYiZhuanQuActivity;
import com.android.jdhshop.activity.PddXiaoKaPianActivity;
import com.android.jdhshop.activity.SetAssistantActivity;
import com.android.jdhshop.activity.WebViewActivity;
import com.android.jdhshop.adapter.AddWeiXinQunAdapter;
import com.android.jdhshop.adapter.PddRecyclerAdapter;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.bean.PDDBean;
import com.android.jdhshop.bean.PddBannerBean;
import com.android.jdhshop.bean.PddClient;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.WeiXinQunXinXIBean;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.utils.CornerTransform;
import com.android.jdhshop.utils.DrawableCenterTextView;
import com.android.jdhshop.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;
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
 * @属性:拼多多商品页
 * @开发者:wmm
 * @时间:2018/11/21 17:36
 */
public class PddFragment extends BaseLazyFragment {


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
    private PddRecyclerAdapter shopRecyclerAdapter;
    private String pid;

    List<PDDBean> taobaoGuesChildtBeans = new ArrayList<>();
    private int indexNum = 1;
    private String name;
    private String sort;
    private LinearLayoutManager linearLayoutManager;
    private boolean hasdata = true;
    private String sort_gz = "0";
    Gson gson = new Gson();
    DecimalFormat df = new DecimalFormat("0.00");
    private TextView[] textViews;
    @BindView(R.id.jiage_st)
    DrawableCenterTextView jiageSt;
    @BindView(R.id.xiaoliang_st)
    DrawableCenterTextView xiaoliangSt;
    @BindView(R.id.yongjin_st)
    DrawableCenterTextView yongjinSt;
    @BindView(R.id.tuiguang_st)
    DrawableCenterTextView tuiguangSt;
    MZBannerView img_banner;
    private List<PddBannerBean.DataBean.ListBean> images3 = new ArrayList<>();
    private List<PddBannerBean.DataBean.ListBean> images4 = new ArrayList<>();
    private List<String> strings;
    private List<String> stringTitle;
    private List<String> stringhref;
    private List<String> stringtype;

    View view;

    ACache mCache;
    private List<WeiXinQunXinXIBean> weiXinQunXinXIBeanList = new ArrayList<>();
    private AddWeiXinQunAdapter weiXinQunAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pdd_new, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        addListener();
        BaseLogDZiYuan.LogDingZiYuan(qunliaoicon, "qunliaoicon.png");
        BaseLogDZiYuan.LogDingZiYuan(aiicon, "aiicon.png");
        BaseLogDZiYuan.LogDingZiYuan(rightIcon, "home_btn.png");

        return view;
    }

    private void init() {
        mCache = ACache.get(getContext());
        Bundle arguments = getArguments();
        if (arguments != null) {
            pid = arguments.getString("pid");
            name = arguments.getString("name");
            sort = arguments.getString("sort");
            if ("0".equals(arguments.getString("index"))) {
                view.findViewById(R.id.ll_root1).setVisibility(View.VISIBLE);
                img_banner = view.findViewById(R.id.img_banner);
                getHomeADimg();
                getXiaoKa();
                img_banner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
                    @Override
                    public void onPageClick(View view, int i) {
                        Bundle bundle = new Bundle();
                        //bundle.putString("id",images3.get(i).getId());
                        bundle.putString("keyword", images3.get(i).getTitle());
                        bundle.putString("href", images3.get(i).getHref());
                        LogUtils.d("-----------baneer", bundle + "");
                        openActivity(PddFangYiZhuanQuActivity.class, bundle);

                    }
                });
                view.findViewById(R.id.go_p_four).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("channel_type", "1");
                        bundle.putString("keyword", stringTitle.get(2));
                        bundle.putString("href", stringhref.get(2));
                        openActivity(PddXiaoKaPianActivity.class, bundle);
                    }
                });
                view.findViewById(R.id.go_p_five).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IWXAPI api = WXAPIFactory.createWXAPI(context, Constants.WX_APP_ID);// 填对应开发平台移动应用AppId
                        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                        req.userName = stringtype.get(3) + ""; // 填小程序原始id（官方实例请填写自己的小程序id）
                        req.path = stringhref.get(3) + ""; //拉起小程序页面的可带参路径，不填默认拉起小程序首页
                        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 正式版
                        api.sendReq(req);
                    }
                });
                view.findViewById(R.id.go_p_six).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("channel_type", "2");
                        bundle.putString("keyword", stringTitle.get(4));
                        bundle.putString("href", stringhref.get(4));
                        openActivity(PddXiaoKaPianActivity.class, bundle);
                    }
                });
                view.findViewById(R.id.ll_chaneel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        get(4);
                    }
                });
                view.findViewById(R.id.pdd_list_left).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Intent intent = new Intent(context, PddXiaoKaPianActivity.class); // WebViewActivity3.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("channel_type", "0");
                        bundle.putString("keyword", stringTitle.get(0));
                        bundle.putString("href", stringhref.get(0));
                        openActivity(PddXiaoKaPianActivity.class, bundle);
                    }
                });
                view.findViewById(R.id.pdd_list_right).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("keyword", stringTitle.get(1));
                        bundle.putString("href", stringhref.get(1));
                        openActivity(PddFangYiZhuanQuActivity.class, bundle);
                    }
                });
                view.findViewById(R.id.pdd_card_a).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        get(2);
                    }
                });
                view.findViewById(R.id.pdd_card_b).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        get(7);
                    }
                });
                view.findViewById(R.id.pdd_card_c).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        get(-1);
                    }
                });
                view.findViewById(R.id.pdd_card_d).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        get(0);
                    }
                });
                view.findViewById(R.id.pdd_card_e).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        get(3);
                    }
                });
                view.findViewById(R.id.pdd_card_f).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        get(5);
                    }
                });
                view.findViewById(R.id.pdd_channel_a).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        get2("4");
                    } //限时秒杀
                });
                view.findViewById(R.id.pdd_channel_d).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        get2("40000");
                    } // 电器城39999 - 已下载更换为 领券中心
                });
                view.findViewById(R.id.pdd_channel_b).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        get2("39996");
                    }  // 百亿补贴
                });
                view.findViewById(R.id.pdd_channel_c).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        get2("39997");
                    } // 充值中心
                });
            }
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
        tuiguangSt.setVisibility(View.GONE);
        yongjinSt.setText("奖");
        textViews = new TextView[]{jiageSt, xiaoliangSt, yongjinSt};
        shopRecyclerAdapter = new PddRecyclerAdapter(getActivity(), R.layout.pdd_item, taobaoGuesChildtBeans);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
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
    }

    private void getXiaoKa() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("cat_id", "13");
        HttpUtils.post(Constants.APP_IP + "/api/Banner/getBannerList",PddFragment.this, requestParams, new onOKJsonHttpResponseHandler<PddBannerBean.DataBean>(new TypeToken<Response<PddBannerBean.DataBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(throwable.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Response<PddBannerBean.DataBean> datas) {
                if (datas.isSuccess()) {
                    //images3.clear();
                    images4.addAll(datas.getData().getList());
                    strings = new ArrayList<>();
                    stringTitle = new ArrayList<>();
                    stringhref = new ArrayList<>();
                    stringtype = new ArrayList<>();
                    for (int i = 0; i < images4.size(); i++) {
                        strings.add(images4.get(i).getImg());
                        stringTitle.add(images4.get(i).getTitle());
                        stringhref.add(images4.get(i).getHref());
                        stringtype.add(images4.get(i).getType_value());
                    }

                    // Toast.makeText(context, s+"", Toast.LENGTH_SHORT).show();
                    ImageView viewById1 = view.findViewById(R.id.pdd_list_left);
                    Glide.with(getActivity()).load(strings.get(0)).asBitmap().placeholder(R.drawable.no_banner).override(CommonUtils.getScreenWidth() / 2, CommonUtils.getScreenWidth() / 4).transform(new CornerTransform(context, 10)).into(viewById1);
//                    Glide.with(getActivity()).load(strings.get(0)).asBitmap().placeholder(R.drawable.no_banner).override(CommonUtils.getScreenWidth() / 2, 180).dontAnimate().into(viewById1);
//                        Glide.with(context).load(strings.get(0)).asBitmap().skipMemoryCache(false).dontAnimate().error(R.drawable.no_banner).transform(new CornerTransform(context, 10)).into(viewById1);
                    ImageView viewById = view.findViewById(R.id.pdd_list_right);
                    Glide.with(getActivity()).load(strings.get(1)).asBitmap().placeholder(R.drawable.no_banner).override(CommonUtils.getScreenWidth() / 2, CommonUtils.getScreenWidth() / 4).transform(new CornerTransform(context, 10)).into(viewById);
//                    Glide.with(getActivity()).load(strings.get(1)).asBitmap().placeholder(R.drawable.no_banner).override(CommonUtils.getScreenWidth() / 2, 180).dontAnimate().into(viewById);
//                        Glide.with(context).load(strings.get(1)).asBitmap().skipMemoryCache(false).dontAnimate().error(R.drawable.no_banner).transform(new CornerTransform(context, 10)).into(viewById);
                    RoundedImageView viewById2 = view.findViewById(R.id.go_p_four);
                    Glide.with(getActivity()).load(strings.get(2)).placeholder(R.drawable.no_banner).override(CommonUtils.getScreenWidth() / 2, 180).dontAnimate().into(viewById2);
//                        Glide.with(context).load(strings.get(2)).asBitmap().skipMemoryCache(false).dontAnimate().error(R.drawable.no_banner).transform(new CornerTransform(context, 10)).into(viewById2);
                    RoundedImageView viewById3 = view.findViewById(R.id.go_p_five);
                    Glide.with(getActivity()).load(strings.get(3)).placeholder(R.drawable.no_banner).override(CommonUtils.getScreenWidth() / 2, 180).dontAnimate().into(viewById3);
//                        Glide.with(context).load(strings.get(3)).asBitmap().skipMemoryCache(false).dontAnimate().error(R.drawable.no_banner).transform(new CornerTransform(context, 10)).into(viewById3);
                    RoundedImageView viewById4 = view.findViewById(R.id.go_p_six);
                    Glide.with(getActivity()).load(strings.get(4)).placeholder(R.drawable.no_banner).override(CommonUtils.getScreenWidth() / 2, 180).dontAnimate().into(viewById4);
//                        Glide.with(context).load(strings.get(4)).asBitmap().skipMemoryCache(false).dontAnimate().error(R.drawable.no_banner).transform(new CornerTransform(context, 10)).into(viewById4);

                } else {
                    showToast(datas.getMsg());
                }
            }
        });
    }

    private void getHomeADimg() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("cat_id", "12");
        HttpUtils.post(Constants.APP_IP + "/api/Banner/getBannerList",PddFragment.this, requestParams, new onOKJsonHttpResponseHandler<PddBannerBean.DataBean>(new TypeToken<Response<PddBannerBean.DataBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(throwable.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Response<PddBannerBean.DataBean> datas) {
                if (datas.isSuccess()) {
                    images3.clear();
                    images3.addAll(datas.getData().getList());
                    List<String> strings = new ArrayList<>();
                    for (int i = 0; i < images3.size(); i++) {
                        strings.add(images3.get(i).getImg());
                    }
                    if (images3.size() <= 0) {
                        img_banner.setVisibility(View.GONE);
                    } else {
                        img_banner.setVisibility(View.VISIBLE);
                        img_banner.setPages(strings, new MZHolderCreator<BannerViewHolder>() {
                            @Override
                            public BannerViewHolder createViewHolder() {
                                return new BannerViewHolder();
                            }
                        });
                        img_banner.start();
                    }
                } else {
                    showToast(datas.getMsg());
                }
            }
        });
    }

    class BannerViewHolder implements MZViewHolder<String> {
        private ImageView mImageView;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.img_t, null);
            mImageView = view.findViewById(R.id.img);

            return view;
        }

        @Override
        public void onBind(Context context, int position, final String data) {
            ///Glide.with(context).load(data).into(mImageView);
            Glide.with(context).load(data).asBitmap().skipMemoryCache(false).dontAnimate().error(R.drawable.no_banner).transform(new CornerTransform(context, 10)).into(mImageView);

        }
    }

    private void get(int type) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("channel_type", type);
        LogUtils.d("拼多多列表", "请求网址：" + Constants.APP_IP + "/api/Pdd/genRpPromUrl");
        LogUtils.d("拼多多列表", "get: " + requestParams.toString());
        HttpUtils.post(Constants.APP_IP + "/api/Pdd/genRpPromUrl", PddFragment.this,requestParams, new TextHttpResponseHandler() {
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
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogUtils.d("dsafsd", responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("dsafsd", responseString);
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    if ("0".equals(jsonObject.getString("code"))) {
                        if (type == -1) {
                            Bundle bundle = new Bundle();
                            bundle.putString("urls", jsonObject.getJSONObject("data").getJSONObject("rp_promotion_url_generate_response").getJSONArray("resource_list").toString());
                            openActivity(PddAdActivity.class, bundle);
                            return;
                        }
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("title", "详情");
                        intent.putExtra("url", jsonObject.getJSONObject("data").getJSONObject("rp_promotion_url_generate_response").getJSONArray("url_list").getJSONObject(0).getString("mobile_url"));
                        startActivity(intent);
                    } else {
                        showToast(jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void get2(String type) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("resource_type", type);
        HttpUtils.post(Constants.APP_IP + "/api/Pdd/genResourceUrl",PddFragment.this, requestParams, new TextHttpResponseHandler() {
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
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogUtils.d("dsafsd", responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("dsafsd", responseString);
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    if ("0".equals(jsonObject.getString("code"))) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("title", "详情");
                        intent.putExtra("url", jsonObject.getJSONObject("data").getJSONObject("resource_url_response").getJSONObject("single_url_list").getString("mobile_url"));
                        startActivity(intent);
                    } else {
                        showToast(jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addListener() {
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (hasdata) {
                    indexNum++;
                    getTbkListRequst(name);
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
                getTbkListRequst(name);
            }
        });
        //点击进入详情
        shopRecyclerAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                PDDBean taobaoGuesChildtBean = taobaoGuesChildtBeans.get(position);
                if (taobaoGuesChildtBean != null) {
                    Intent intent = new Intent(context, PddDetailsActivity.class);
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected void lazyload() { //懒加载，界面开始后刷新
        //开始刷新
        refreshLayout.autoRefresh();
    }

    /**
     * @属性:获取Pdd商品列表
     * @开发者:wmm
     * @时间:2018/11/21 17:05
     */
    private void getTbkListRequst(String search) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        if (mCache.getAsString("pdd_frament_" + pid + "_indexNum") != null && !"".equals(mCache.getAsString("pdd_frament_" + pid + "_indexNum"))) {
            indexNum = Integer.parseInt(mCache.getAsString("pdd_frament_" + pid + "_indexNum"));
        }

        RequestParams requestParams = new RequestParams();
        requestParams.put("page", indexNum + "");
        requestParams.put("sort_type", sort_gz);
        requestParams.put("page_size", "10");
        requestParams.put("data_type", PddClient.data_type);
        requestParams.put("client_id", PddClient.client_id);
        requestParams.put("timestamp", time);
        requestParams.put("with_coupon", "true");
        requestParams.put("type", "pdd.ddk.goods.search");
        if (pid.equals("0")) {
            requestParams.put("opt_id", pid);
        } else {
            requestParams.put("cat_id", pid);
        }
        Map<String, Object> temp = new HashMap<>();
        temp.put("page", indexNum + "");
        if (pid.equals("0")) {
            temp.put("opt_id", pid);
        } else {
            temp.put("cat_id", pid);
        }
        temp.put("sort_type", sort_gz);
        temp.put("page_size", "10");
        temp.put("data_type", PddClient.data_type);
        temp.put("type", "pdd.ddk.goods.search");
        temp.put("client_id", PddClient.client_id);
        temp.put("timestamp", time);
        temp.put("with_coupon", "true");
        String sign = PddClient.getSign(temp);
        requestParams.put("sign", sign);
        Log.d("vvvvvvvvvvvvv", requestParams+"");
        HttpUtils.post1(PddClient.serverUrl,PddFragment.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                try{showLoadingDialog();} catch (Exception e){ }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    closeLoadingDialog();
                }
                catch (Exception e){ }
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadMore();
//                    if (indexNum == 1) {
//                        refreshLayout.finishRefresh();
//                    } else {
//                        refreshLayout.finishLoadMore();
//                    }
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
                        tempObj.put("commission", df.format(tem * SPUtils.getIntData(context, "rate", 0) / 100));
                        taobaoGuesChildtBeans.add(gson.fromJson(tempObj.toString(), PDDBean.class));
                    }
                    shopRecyclerAdapter.notifyDataSetChanged();
                    mCache.put("pdd_frament_" + pid + "_indexNum", (indexNum + 1) + "", Constants.CacheSaveTime);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.right_icon, R.id.jiage_st, R.id.yongjin_st, R.id.xiaoliang_st})
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
        HttpUtils.post(Constants.getWXQinXinXI,PddFragment.this, params, new TextHttpResponseHandler() {
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
