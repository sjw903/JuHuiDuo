package com.android.jdhshop.fragments;

import android.content.ClipboardManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.NavigatorAdapter;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.bean.GroupListBean;
import com.android.jdhshop.bean.MessageEvent;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.UserBean;
import com.android.jdhshop.bean.UserInfoBean;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.mall.MallGoodsDetailsActivity;
import com.android.jdhshop.malladapter.ShopMallGoodsRecyclerAdapter;
import com.android.jdhshop.mallbean.ShopMallGoodsBean;
import com.android.jdhshop.my.MyShareUrlActivity;
import com.android.jdhshop.widget.CircleImageView;
import com.android.jdhshop.widget.indicator.MagicIndicator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.CommonNavigator;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


/**
 * <pre>
 *     author : Administrator
 *     e-mail : szp
 *     time   : 2020/05/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class VipFragmentNew extends BaseLazyFragment {
    @BindView(R.id.Code)
    TextView Code;
    @BindView(R.id.btn_copy)
    TextView btn_copy;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.head)
    CircleImageView head;
    @BindView(R.id.huiyuan)
    TextView huiyuan;
    @BindView(R.id.txt_yq)
    TextView txtyq;
    @BindView(R.id.txt_tj)
    TextView txttj;
    @BindView(R.id.txt_xd)
    TextView txtxd;
    @BindView(R.id.swipe)
    SmartRefreshLayout swipeRefreshLayout;
    @BindView(R.id.txt_exp_group_one)
    TextView txtexpGroupOne;
    @BindView(R.id.txt_exp_group_two)
    TextView txtexpGrouptwo;
    @BindView(R.id.txt_name_group_one)
    TextView txtNameGroupOne;
    @BindView(R.id.txt_name_group_two)
    TextView txtNameGroupTwo;
    @BindView(R.id.txt_name_group_three)
    TextView txtNameGroupThree;
    @BindView(R.id.txt_name_group_four)
    TextView txtNameGroupFour;
    @BindView(R.id.txt_grade_max)
    TextView txtGradeMax;
    @BindView(R.id.txt_grade)
    TextView txtGrade;
    @BindView(R.id.progesss_value)
    TextView progresssvalue;
    @BindView(R.id.user_upgrade_register)
    TextView user_upgrade_register;
    @BindView(R.id.user_upgrade_buy)
    TextView user_upgrade_buy;
    @BindView(R.id.pb_progressbar)
    ProgressBar progressBar;
    @BindView(R.id.full)
    LinearLayout progressBarBox0;
    @BindView(R.id.txt_box)
    LinearLayout txtBox;
    @BindView(R.id.full1)
    LinearLayout progressBarBox;
    @BindView(R.id.vip_quanyi_duibi)
    GridLayout vipQuanYiDuibi;
    @BindView(R.id.back_button)
    ImageView back_button;
    private MagicIndicator magicIndicator;
    //设置总进度
    private float maxCount;
    //   当前进度
    private float currentCount;
    //    当前位置
    private int currentPosition;
    //    得到屏幕的总宽度
    private int width;
    RecyclerView recyclerView;
    private float scrollDistance;
    private int tvWidth;
    List<ShopMallGoodsBean> taobaoGuesChildtBeans = new ArrayList<>();
    private ShopMallGoodsRecyclerAdapter shopRecyclerAdapter;
    private List<ProgressBar> progressBarList = new ArrayList<>(); // 记录动态添加的progressbar
    private List<String> progressBarName = new ArrayList<>();
    @BindView(R.id.image1)
    ImageView image1;
    @BindView(R.id.image2)
    ImageView image2;
    @BindView(R.id.image3)
    ImageView image3;
    @BindView(R.id.image4)
    ImageView image4;
    @BindView(R.id.image5)
    ImageView image5;
    @BindView(R.id.image6)
    ImageView image6;
    @BindView(R.id.image7)
    ImageView image7;
    @BindView(R.id.image8)
    ImageView image8;
    @BindView(R.id.image9)
    ImageView image9;
//    @BindView(R.id.image10)
//    ImageView image10;

    @Override
    protected void lazyload() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_hyzx, container, false);
        ButterKnife.bind(this, view);
        magicIndicator = view.findViewById(R.id.magicIndicator);
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.pb_progressbar);
        progresssvalue = view.findViewById(R.id.progesss_value);
        getUserMsg();
        getGroupList();
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        shopRecyclerAdapter = new ShopMallGoodsRecyclerAdapter(context, R.layout.shop_mall_goods_item, taobaoGuesChildtBeans);
        recyclerView.setAdapter(shopRecyclerAdapter);
        //点击进入详情
        shopRecyclerAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                ShopMallGoodsBean taobaoGuesChildtBean = taobaoGuesChildtBeans.get(position);
                if (taobaoGuesChildtBean != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("goods_id", taobaoGuesChildtBean.goods_id);
                    bundle.putString("isVip", "1");
                    openActivity(MallGoodsDetailsActivity.class, bundle);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        getList();
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getUserMsg();
                getGroupList();
                getList();
            }
        });
        txtxd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent("commuity"));
            }
        });
        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cmb = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
                cmb.setText(Code.getText().toString().trim());
                T.showShort(context, "复制成功，快去邀请好友吧");
            }
        });
//        head.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openActivity(MyInformationActivity.class);
//            }
//        });
        txtyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(MyShareUrlActivity.class);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        txttj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(MyShareUrlActivity.class);
            }
        });
//         得到progressBar控件的宽度
        ViewTreeObserver vto2 = progressBar.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                progressBar.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                width = progressBar.getWidth();
            }
        });

        user_upgrade_register.setText((" (+" + SPUtils.getStringData(context, "user_upgrade_register", "1") + ")"));
        user_upgrade_buy.setText((" (+" + SPUtils.getStringData(context, "user_upgrade_buy", "1") + ")"));
//        初始化监听
        initListener();
        LogDingZiYuan();//加载本地资源
        return view;
    }

    //加载本地资源
    private void LogDingZiYuan() {

        BaseLogDZiYuan.LogDingZiYuan(image2, "icon_vip_privilege_a.png");
        BaseLogDZiYuan.LogDingZiYuan(image3, "icon_vip_privilege_b.png");
        BaseLogDZiYuan.LogDingZiYuan(image4, "icon_vip_privilege_c.png");
        BaseLogDZiYuan.LogDingZiYuan(image5, "icon_vip_privilege_d.png");
        BaseLogDZiYuan.LogDingZiYuan(image6, "icon_vip_privilege_e.png");
        BaseLogDZiYuan.LogDingZiYuan(image7, "icon_vip_privilege_f.png");
        BaseLogDZiYuan.LogDingZiYuan(image8, "icon_vip_privilege_g.png");
        BaseLogDZiYuan.LogDingZiYuan(image9, "icon_vip_title_b.png");
//        BaseLogDZiYuan.LogDingZiYuan(image10, "icon_vip_title_c.png");
    }

    private VipFragmentNew initListener() {
        if (currentCount <= 22) {
//            currentCount = 0;
            progresssvalue.setTranslationX(currentPosition);
//        实现效果
            initAchieve();
            return null;
        } else if (currentCount > 22) {
            currentPosition = (int) (currentCount);
            return this;
        } else {
            currentPosition = (int) (currentCount - 22);
            return this;
        }
    }

    private void initAchieve() {
        progresssvalue.setTranslationX((int) ((progressBar.getWidth() * 1.00 / 500) * Integer.valueOf(progresssvalue.getText().toString())));
//        new Thread( new Runnable() {
//            @Override
//            public void run() {
//                scrollDistance = (float) ((1.0 / progressBar.getMax()) * width);
//                for (int i = 0; i < maxCount; i++) {
//                    runOnUiThread( new Runnable() {
//                        @Override
//                        public void run() {
//                            progressBar.incrementProgressBy( 1 );
//                            currentPosition++;
////                       得到字体的宽度
//                            tvWidth = progresssvalue.getWidth();
//                            currentPosition += scrollDistance;
////                       做一个平移的效果
//                            if (tvWidth + currentPosition <= width - progressBar.getPaddingRight()) {
//
////                                progressBar.setProgress( (int) currentCount );
//                            }
//                        }
//                    } );
//                    try {
//                        Thread.sleep( 80);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        } ).start();
    }

    private void runOnUiThread(Runnable runnable) {
    }

    private void initmagicIndicator() {
        String[] str = {"爆款", "美妆", "家居", "个护", "女装", "数码", "食品"}; //设置指示器title
        CommonNavigator navigator = new CommonNavigator(getActivity());//新建指示器的导航栏
        navigator.setAdjustMode(true); //设置title宽度自适应
        navigator.setEnablePivotScroll(true); //多指示器模式，可以滑动
        NavigatorAdapter adapter = new NavigatorAdapter(context, str);
        navigator.setAdapter(adapter); //设置导航栏适配器
        magicIndicator.setNavigator(navigator); //把导航栏设置给指示器
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onResume() {
        super.onResume();
        if (!CommonUtils.isNetworkAvailable()) {
            showToast(getResources().getString(R.string.error_network));
            return;
        }
        if ("".equals(SPUtils.getStringData(context, "token", ""))) {
            return;
        }
    }

    private void getUserMsg() {
        RequestParams params = new RequestParams();
//                params.put("token", token);
        HttpUtils.post(Constants.GET_USER_MSG, VipFragmentNew.this,params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // 输出错误信息
            }

            @Override
            public void onFinish() {
                // 隐藏进度条
                super.onFinish();
                swipeRefreshLayout.finishRefresh();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    //返回码
                    UserInfoBean userBean = null;
                    int code = jsonObject.optInt("code");
                    //返回码说明
                    String msg = jsonObject.optString("msg");
                    String data = jsonObject.optString("data");
                    if (0 == code) {
                        if (!TextUtils.isEmpty(data)) {//GSON对数字都当做double解析
                            Gson gson = new Gson();
                            userBean = gson.fromJson(data.trim(), UserInfoBean.class);
                            CaiNiaoApplication.setUserInfoBean(userBean);
                        }
                        if (null != userBean) {
                            CaiNiaoApplication.setUserBean(new UserBean(userBean.user_detail.user_id, userBean.user_msg.group_id, SPUtils.getStringData(context, "token", ""), userBean.user_detail.avatar, userBean.user_detail.nickname, userBean.user_msg.is_forever));
                            SPUtils.saveStringData(context, "phone", userBean.user_msg.phone);
                            if (CaiNiaoApplication.getUserBean() != null) {
                                if (!TextUtils.isEmpty(userBean.user_detail.nickname)) {
                                    if (isBase64(userBean.user_detail.nickname)) {
                                        tvUsername.setText(base64Decode(userBean.user_detail.nickname, "utf-8"));
                                    } else {
                                        tvUsername.setText(userBean.user_detail.nickname);
                                    }
                                } else {
                                    tvUsername.setText(userBean.user_msg.phone);
                                }
                                Code.setText(CaiNiaoApplication.getUserInfoBean().user_msg.auth_code);
                                if (CaiNiaoApplication.getUserBean().getAvatar() == null || CaiNiaoApplication.getUserBean().getAvatar().equals("")) {
                                    //Glide.with( context ).load( SPUtils.getStringData( context, "default_avatar", "" ) ).placeholder( R.mipmap.icon_defult_boy ).error( R.mipmap.icon_defult_boy ).dontAnimate().into( head );
                                } else {
                                    Glide.with(context).load(CaiNiaoApplication.getUserBean().getAvatar()).placeholder(R.mipmap.icon_defult_boy).error(R.mipmap.icon_defult_boy).dontAnimate().into(head);
                                }
                                huiyuan.setText(userBean.user_msg.group_name);
                            }
                        }
                    } else {
                        showToast(msg);
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

    private void getGroupList() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.GROUP_LIST,VipFragmentNew.this, requestParams, new onOKJsonHttpResponseHandler<GroupListBean>(new TypeToken<Response<GroupListBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(throwable.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Response<GroupListBean> datas) {
                if (datas.isSuccess()) {
                    List<GroupListBean.Item> items = datas.getData().list;
                    txtNameGroupOne.setVisibility(View.GONE);
                    txtNameGroupTwo.setVisibility(View.GONE);
                    txtNameGroupThree.setVisibility(View.GONE);
                    txtNameGroupFour.setVisibility(View.GONE);
                    txtexpGroupOne.setText("0");
                    txtexpGrouptwo.setText(datas.getData().list.get(datas.getData().list.size() - 1).exp + "");
                    txtGradeMax.setText("/" + datas.getData().list.get(datas.getData().list.size() - 1).exp);
                    txtBox.removeAllViews();
                    progressBarBox.removeAllViews();
                    progressBarList.clear();


                    vipQuanYiDuibi.removeAllViews();
                    // VIP权益对比表格
                    for (int j = 0; j < 7; j++) {
                        TextView quanyiItem = new TextView(context);
                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1F);
                        }

                        quanyiItem.setLayoutParams(params);
                        params.topMargin = 7;
                        params.bottomMargin = 7;
                        String dis_str = "";
                        switch (j) {
                            case 0:
                                dis_str = "会员级别";
                                break;
                            case 1:
                                dis_str = "所需积分";
                                break;
                            case 2:
                                dis_str = "自购返利";
                                break;
                            case 3:
                                dis_str = "邀请奖励";
                                break;
                            case 4:
                                dis_str = "教学奖励";
                                break;
                            case 5:
                                dis_str = "团队奖励";
                                break;
                            case 6:
                                dis_str = "店铺奖励";
                                break;
                        }
                        quanyiItem.setText(dis_str);
                        quanyiItem.setGravity(Gravity.CENTER);
                        quanyiItem.setTextColor(Color.parseColor("#FDEAA5"));
                        quanyiItem.setTextSize(7F);
                        vipQuanYiDuibi.addView(quanyiItem);
                    }

//                    progressBarBox0.setVisibility(View.GONE);
                    LogUtils.d("TAG", "onSuccess: " + progresssvalue.getText());
                    for (int i = 0; i < items.size(); i++) {
                        ProgressBar pb = new ProgressBar(context, null, R.style.StyleProgressBarMini);
                        pb.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommonUtils.dip2px(context, 5.0F), 1.0F));
                        TextView tv = new TextView(context);
                        tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0F));
                        if (i == 0) {
                            tv.setGravity(Gravity.START);
                        } else if (i == items.size() - 1) {
                            tv.setGravity(Gravity.END);
                        } else {
                            tv.setGravity(Gravity.CENTER);
                        }

                        tv.setTextSize(10);
                        tv.setText(items.get(i).title);
                        tv.setVisibility(View.VISIBLE);
                        txtBox.addView(tv);


                        //第1个
                        if (i == 1) {
                            pb.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_left));
                        }

                        // 最后一个
                        if (i > 1) {
                            pb.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_center));
                        }

                        if (i == items.size() - 1) {
                            pb.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_right));
                        }

                        if (i > 0) {
                            pb.setMax(Integer.parseInt(items.get(i).exp));
                            progressBarBox.addView(pb);
                            progressBarList.add(pb);
                            progressBarName.add(items.get(i).title);
                        }


                        // VIP权益对比表格
                        for (int j = 0; j < 7; j++) {
                            TextView quanyiItem = new TextView(context);
                            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1F);
                            }

                            params.topMargin = 10;
                            params.bottomMargin = 10;

                            quanyiItem.setLayoutParams(params);
                            String dis_str = "";
                            switch (j) {
                                case 0:
                                    dis_str = items.get(i).title;
                                    break;
                                case 1:
                                    dis_str = items.get(i).exp;
                                    break;
                                case 2:
                                    dis_str = items.get(i).fee_user + "%";
                                    break;
                                case 3:
                                    dis_str = items.get(i).referrer_rate + "%";
                                    break;
                                case 4:
                                    dis_str = items.get(i).referrer_rate2 + "%";
                                    break;
                                case 5:
                                    dis_str = items.get(i).referrer_team + "%";
                                    break;
                                case 6:
                                    dis_str = items.get(i).referrer_store + "%";
                                    break;
                            }
                            quanyiItem.setText(dis_str);
                            quanyiItem.setGravity(Gravity.CENTER);
                            quanyiItem.setTextColor(Color.parseColor("#FDEAA5"));
                            quanyiItem.setTextSize(10F);
                            vipQuanYiDuibi.addView(quanyiItem);
                        }
                    }


                    // 原始逻辑
//                    for (int i=0;i<items.size();i++){
//                        switch (i){
//                            case 0:
//                                txtNameGroupOne.setText(items.get(i).title);
//                                txtexpGroupOne.setText("0");
//                                break;
//                            case 1:
//                                txtNameGroupTwo.setText(items.get(i).title);
//                                break;
//                            case 2:
//                                txtNameGroupThree.setText(items.get(i).title);
//                                break;
//                            case 3:
//                                txtNameGroupFour.setText(items.get(i).title);
//                                break;
//                        }
//                    }


                    txtexpGrouptwo.setText(datas.getData().list.get(datas.getData().list.size() - 1).exp + "");
                } else {
                    showToast(datas.getMsg());
                }
            }
        });
        getVipData();
    }

    private void getList() {
        if (CaiNiaoApplication.getUserInfoBean() != null) {
            if ("1".equals(CaiNiaoApplication.getUserInfoBean().user_msg.group_id) || "2".equals(CaiNiaoApplication.getUserInfoBean().user_msg.group_id)) {
                RequestParams requestParams = new RequestParams();
                requestParams.put("p", 1);
                requestParams.put("group_id", "3");
                requestParams.put("per", 20);
                HttpUtils.post(Constants.APP_IP + "/api/UserGoods/getList",VipFragmentNew.this, requestParams, new onOKJsonHttpResponseHandler<ShopMallGoodsBean>(new TypeToken<Response<ShopMallGoodsBean>>() {
                }) {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    }

                    @Override
                    public void onSuccess(int statusCode, Response<ShopMallGoodsBean> datas) {
                        if (datas.isSuccess()) {
                            List<ShopMallGoodsBean> list = datas.getData().list;
                            taobaoGuesChildtBeans.clear();
                            taobaoGuesChildtBeans.addAll(list);
                        } else {
                            showToast(datas.getMsg());
                        }
                        shopRecyclerAdapter.notifyDataSetChanged();
                    }
                });
            }
        } else {
            getUserMsg();
        }
    }

    private static boolean isBase64(String str) {
        String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        return Pattern.matches(base64Pattern, str);
    }

    private String base64Decode(String content, String charsetName) {
        if (TextUtils.isEmpty(charsetName)) {
            charsetName = "UTF-8";
        }
        byte[] contentByte = Base64.decode(content, Base64.DEFAULT);
        try {
            return new String(contentByte, charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void getVipData() {
        RequestParams params = new RequestParams();
        HttpUtils.post(Constants.GET_VIP_DATA, VipFragmentNew.this,params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // 输出错误信息
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
                    //返回码说明
                    String msg = jsonObject.optString("msg");
                    if (0 == code) {
                        progresssvalue.setText(jsonObject.getJSONObject("data").getString("exp"));
                        initAchieve();
                        int user_current_exp = Integer.parseInt(jsonObject.getJSONObject("data").getString("exp"));
                        progressBar.setProgress(user_current_exp);
                        String next_tips = ""; //
                        for (ProgressBar p : progressBarList) {
                            LogUtils.d("TAG", "第二次: " + p.getMax());
                            if (p.getMax() < user_current_exp) {
                                p.setProgress(p.getMax());
                            } else {

                                p.setProgress(user_current_exp);

                                LogUtils.d("TAG", "onSuccess1: " + user_current_exp);
                                LogUtils.d("TAG", "onSuccess2: " + p.getMax());
                                float max_size = p.getMax();
                                next_tips = jsonObject.getJSONObject("data").getString("exp");
                                next_tips += "，距离" + progressBarName.get(progressBarList.indexOf(p)) + "还差" + (int) (max_size - Integer.parseInt(next_tips)) + "积分";
                                float cur_size = user_current_exp;
                                float iii = (cur_size / max_size) * p.getWidth();
                                LogUtils.d("TAG", "onSuccess: " + iii);
                                int progressvalue_postion = (int) (p.getLeft() + iii) - 5;
                                progresssvalue.setX(progressvalue_postion);
                                break;
                            }
                        }

                        txtGrade.setText(next_tips);
                        txtGradeMax.setVisibility(View.GONE);

                    } else {
                        showToast(msg);
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
}


