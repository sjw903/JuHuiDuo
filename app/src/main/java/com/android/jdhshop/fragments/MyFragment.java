package com.android.jdhshop.fragments;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.FeedBackActivity;
import com.android.jdhshop.activity.InComeActivity;
import com.android.jdhshop.activity.KfActivity;
import com.android.jdhshop.activity.MyMarketActivity;
import com.android.jdhshop.activity.NewClassActivity;
import com.android.jdhshop.activity.NewsActivity;
import com.android.jdhshop.activity.SetActivity;
import com.android.jdhshop.activity.SysMessageActivity;
import com.android.jdhshop.activity.TeamIncomeNewActivity;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.bean.GroupListBean;
import com.android.jdhshop.bean.UserBalanceRecordBean;
import com.android.jdhshop.bean.UserBean;
import com.android.jdhshop.bean.UserInfoBean;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.mall.MyShopMallOrderActivity;
import com.android.jdhshop.merchantactivity.OrderListActivity;
import com.android.jdhshop.my.CollectionActivity;
import com.android.jdhshop.my.MyInformationActivity;
import com.android.jdhshop.my.MyMessageActivity;
import com.android.jdhshop.my.MyOrderActivity;
import com.android.jdhshop.my.MyShareUrlActivity;
import com.android.jdhshop.userupdate.UpdateGroupActivity;
import com.android.jdhshop.utils.BroadcastContants;
import com.android.jdhshop.utils.BroadcastManager;
import com.android.jdhshop.utils.FixedHeadScrollView;
import com.android.jdhshop.utils.MyScrollView;
import com.android.jdhshop.widget.CircleImageView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 我的
 * Created by wim
 */

public class MyFragment extends BaseLazyFragment implements FixedHeadScrollView.FixedHeadScrollViewListener {
    public static final String TAG = "MyFragment";
    @BindView(R.id.civ_head)
    CircleImageView civ_head;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.tv_username)
    TextView tv_username;
    @BindView(R.id.txt_market_income)
    TextView txtMarketIncome;
    @BindView(R.id.view_one)
    View viewOne;
    @BindView(R.id.img_one)
    ImageView imgOne;
    @BindView(R.id.view_two)
    View viewTwo;
    @BindView(R.id.img_two)
    ImageView imgTwo;
    @BindView(R.id.view_three)
    View viewThree;
    @BindView(R.id.img_three)
    ImageView imgThree;
    @BindView(R.id.txt_one)
    TextView txtOne;
    @BindView(R.id.txt_two)
    TextView txtTwo;
    @BindView(R.id.txt_three)
    TextView txtThree;
    @BindView(R.id.txt_four)
    TextView txtFour;
    private View view;
    public static MyFragment fragment;
    private Bundle bundle;
    @BindView(R.id.fix_scol)
    MyScrollView myScrollView;
    @BindView(R.id.txt_grade)
    TextView txtGrade;
    private ACache mAcache;
    String token = "";
    private UserInfoBean userBean;
    @BindView(R.id.ll_top)
    RelativeLayout llTop;
    @BindView(R.id.ll_info)
    LinearLayout llINfo;
    @BindView(R.id.txt_set2)
    ImageView set2;
    @BindView(R.id.txt_code)
    TextView txtCode;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private UserBalanceRecordBean recordBean;
    // 标志位，标志已经初始化完成。
    private boolean isPrepared;

    private AlibcLogin alibcLogin;

    public static MyFragment getInstance() {
        if (fragment == null) {
            fragment = new MyFragment();
        }
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my, container, false);
        ButterKnife.bind(this, view);
        llTop.measure(0, 0);
        llTop.getBackground().mutate().setAlpha(0);
        init();
        view.findViewById(R.id.img_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("1".equals(CaiNiaoApplication.getUserInfoBean().user_msg.group_id)||"2".equals(CaiNiaoApplication.getUserInfoBean().user_msg.group_id)){
                    openActivity(UpdateGroupActivity.class);
                }
            }
        });
        addListener();
        getGroupList();
        getUserMsg();
        getVipData();
        return view;
    }

    private void init() {
        alibcLogin = AlibcLogin.getInstance();
    }


    private void addListener() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getUserMsg();
                getVipData();
                getGroupList();
            }
        });
        myScrollView.setScrolListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                if (scrollY < 0)
                    return;
                if (scrollY > llTop.getMeasuredHeight()) {
                    llTop.getBackground().mutate().setAlpha(255);
                } else {
                    llINfo.setScaleX(1 - scrollY * 1.0f / llTop.getMeasuredHeight());
                    llINfo.setScaleY(1 - scrollY * 1.0f / llTop.getMeasuredHeight());
                    txtName.setScaleX(scrollY * 1.0f / llTop.getMeasuredHeight());
                    txtName.setScaleY(scrollY * 1.0f / llTop.getMeasuredHeight());
                    set2.setScaleX(scrollY * 1.0f / llTop.getMeasuredHeight());
                    set2.setScaleY(scrollY * 1.0f / llTop.getMeasuredHeight());
                    llTop.getBackground().mutate().setAlpha((int) (scrollY * 1.00 * 255 / llTop.getMeasuredHeight()));
                }
            }
        });
    }

    @Override
    protected void lazyload() {
    }

    /**
     * @属性:接收登录的成功的消息
     * @开发者:陈飞
     * @时间:2018/7/21 14:55
     */
    @Override
    protected void ReceiverIsLoginMessage() {
        super.ReceiverIsLoginMessage();
        //发送消息选择我的页

    }

    /**
     * @属性:接收广播队列
     * @开发者:陈飞
     * @时间:2018/7/29 16:16
     */
    @Override
    protected void ReceiverBroadCastMessage(String status, String result, Serializable serializable, Intent intent) {
        super.ReceiverBroadCastMessage(status, result, serializable, intent);
        //接收修改用户信息，那么刷新当前用户信息
        if (BroadcastContants.sendUserMessage.equals(status)) {
            getUserMsg();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //销毁广播
        BroadcastManager.getInstance(getActivity()).destroy(BroadcastContants.sendUserMessage);
    }

    @OnClick({R.id.my_zyallorder,R.id.my_zydfkorder,R.id.my_zydfhorder,R.id.my_zydshorder,
            R.id.my_zyywcorder,R.id.my_zydfkorder1,R.id.my_zydfhorder1,R.id.my_zydshorder1,
            R.id.my_zyywcorder1,R.id.txt_mes, R.id.txt_about, R.id.txt_hz, R.id.btn_copy, R.id.civ_head, R.id.txt_collect, R.id.txt_market_income, R.id.ll_vip, R.id.txt_market, R.id.txt_tj, R.id.txt_sy, R.id.txt_dd, R.id.txt_gg, R.id.txt_kf, R.id.txt_fk, R.id.txt_set, R.id.txt_set2})
    public void onViewClicked(View view) {
        Bundle bundle=new Bundle();
        switch (view.getId()) {
            case R.id.txt_mes:
                openActivity(SysMessageActivity.class);
                break;
            case R.id.btn_copy:
                ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(txtCode.getText().toString().trim());
                T.showShort(context, "复制成功，快去邀请好友吧");
                break;
            case R.id.civ_head:
                openActivity(MyInformationActivity.class);
                break;
            case R.id.txt_market_income:
                openActivity(TeamIncomeNewActivity.class);
                break;
            case R.id.txt_collect:
                openActivity(CollectionActivity.class);
                break;
            case R.id.ll_vip:
                openActivity(NewClassActivity.class);
//                EventBus.getDefault().post(new MessageEvent("huiyuan"));
                break;
            case R.id.txt_sy:
                openActivity(InComeActivity.class);
                break;
            case R.id.txt_dd:
                whetherBindingTaobao();
                break;
            case R.id.txt_gg:
                openActivity(MyMessageActivity.class);
                break;
            case R.id.my_zyallorder:
                bundle.putString("type","0");
                Constants.MALL_ORDER_TYPE="0";
                openActivity(MyShopMallOrderActivity.class,bundle);
                break;
            case R.id.my_zydfkorder:
                bundle.putString("type","1");
                Constants.MALL_ORDER_TYPE="0";
                openActivity(MyShopMallOrderActivity.class,bundle);
                break;
            case R.id.my_zydfhorder:
                bundle.putString("type","2");
                Constants.MALL_ORDER_TYPE="0";
                openActivity(MyShopMallOrderActivity.class,bundle);
                break;
            case R.id.my_zydshorder:
                bundle.putString("type","3");
                Constants.MALL_ORDER_TYPE="0";
                openActivity(MyShopMallOrderActivity.class,bundle);
                break;
            case R.id.my_zyywcorder:
                bundle.putString("type","4");
                Constants.MALL_ORDER_TYPE="0";
                openActivity(MyShopMallOrderActivity.class,bundle);
                break;
            case R.id.my_zydfkorder1:
                bundle.putString("type","1");
                openActivity(OrderListActivity.class,bundle);
                break;
            case R.id.my_zydfhorder1:
                bundle.putString("type","2");
                openActivity(OrderListActivity.class,bundle);
                break;
            case R.id.my_zydshorder1:
                bundle.putString("type","3");
                openActivity(OrderListActivity.class,bundle);
                break;
            case R.id.my_zyywcorder1:
                bundle.putString("type","4");
                openActivity(OrderListActivity.class,bundle);
                break;
            case R.id.txt_kf:
                openActivity(KfActivity.class);
//                CommonUtils.getContactCustomerService(getActivity());
                break;
            case R.id.txt_fk:
                Intent intent = new Intent(context, FeedBackActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.txt_set2:
            case R.id.txt_set:
                openActivity(SetActivity.class);
                break;
            case R.id.txt_tj:
                openActivity(MyShareUrlActivity.class);
                break;
            case R.id.txt_market:
                openActivity(MyMarketActivity.class);
                break;
            case R.id.txt_about:
                NewsActivity.actionStart(context, "27", "关于我们");
                break;
            case R.id.txt_hz:
                intent = new Intent(context, FeedBackActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
//            case R.id.txt_ye:
//                Bundle b = new Bundle();
//                if (null != userBean && null != userBean.user_msg) {
//                    b.putString("balance", userBean.user_msg.balance);
//                    b.putString("user", userBean.user_msg.balance_user);
//                    b.putString("service", userBean.user_msg.balance_service);
//                    b.putString("plantform", userBean.user_msg.balance_plantform);
//                    openActivity(BalanceActivity.class, b);
//                }
//                break;
        }
    }

    //      Intent intent = new Intent();
//                    ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
//                    intent.setAction(Intent.ACTION_MAIN);
//                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.setComponent(cmp);
//                    startActivity(intent);
    //是否绑定淘宝账号
    private void whetherBindingTaobao() {
//        String url = Constants.WHETHER_BINDING_TAOBAO;
//        OkHttpClient okHttpClient = new OkHttpClient();
//        RequestBody body = new FormBody.Builder().add("token", SPUtils.getStringData(context, "token", "")).build();
//        Request request = new Request.Builder().url(url).post(body).build();
//        Call call = okHttpClient.newCall(request);
//        showLoadingDialog("检测绑定淘宝");
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//                closeLoadingDialog();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String result = response.body().string();
//                closeLoadingDialog();
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    String code = jsonObject.getString("code");
//                    if ("105".equals(code)) {
//                        showToast("登录信息已过期");
//                        openActivity(WelActivity.class);
//                        return;
//                    }
//                    JSONObject jsonData = jsonObject.getJSONObject("data");
//                    String isBinding = jsonData.optString("is_binding");
//                    if ("N".equals(isBinding)) {
//                        //如果未绑定
//                        alibcLogin.showLogin(new AlibcLoginCallback() {
//                            @Override
//                            public void onSuccess(int i, String s, String s1) {
//                                s=alibcLogin.getSession().userid;
//                                if(s==null){
//                                    return;
//                                }
//                                int length = s.length();
//                                if (length > 6) {
//                                    String b = s.substring(length - 6, length);
//                                    String[] bArr = b.split("");
//                                    String c = bArr[1] + "" + bArr[2] + "" + bArr[5] + "" + bArr[6] + "" + bArr[3] + "" + bArr[4];
//                                    bindingTaobao(c);
////                                    int i=0;
//                                }
//                            }
//                            @Override
//                            public void onFailure(int code, String msg) {
//
//                                Log.i("FanliActivity", "淘宝授权登录失败信息=" + msg);
//                                MyFragment.this.getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(MyFragment.this.getActivity(), "请您进行淘宝授权后再进行操作",
//                                                Toast.LENGTH_LONG).show();
//                                    }
//                                });
//                            }
//                        });
//
//
//                    } else {
                        openActivity(MyOrderActivity.class);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
    }

    //绑定淘宝账号
    private void bindingTaobao(String tb_uid) {
        String url = Constants.BINDING_TAOBAO;
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder().add("token", SPUtils.getStringData(context, "token", "")).add("tb_uid", tb_uid).build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if ("0".equals(code)) {
                        MyFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                openActivity(MyOrderActivity.class);
                                Toast.makeText(MyFragment.this.getActivity(), "绑定淘宝账号成功",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(MyFragment.this.getActivity(), "绑定淘宝账号失败",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    private static boolean isBase64(String str) {
        String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        return Pattern.matches(base64Pattern, str);
    }
    /**
     * Base64解密字符串
     * @param content -- 待解密字符串
     * @param charsetName -- 字符串编码方式
     * @return
     */
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
    /**
     * 获取用户信息
     */
    private void getUserMsg() {
        if (!CommonUtils.isNetworkAvailable()) {
            showToast(getResources().getString(R.string.error_network));
            return;
        }
        if("".equals(SPUtils.getStringData(context, "token", ""))){
            return;
        }
        RequestParams params = new RequestParams();
//        params.put("token", token);
        HttpUtils.post(Constants.GET_USER_MSG,MyFragment.this, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // 输出错误信息
                LogUtils.e(TAG, "onFailure()--" + responseString);
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
                    String data = jsonObject.optString("data");
                    if (0 == code) {
                        if (!TextUtils.isEmpty(data)) {//GSON对数字都当做double解析
                            Gson gson = new Gson();
                            userBean = gson.fromJson(data.trim(), UserInfoBean.class);
                            CaiNiaoApplication.setUserInfoBean(userBean);
                        }
                        if (null != userBean) {
                            CaiNiaoApplication.setUserBean(new UserBean(userBean.user_detail.user_id, userBean.user_msg.group_id, token, userBean.user_detail.avatar, userBean.user_detail.nickname, userBean.user_msg.is_forever));
                            SPUtils.saveStringData(context, "phone", userBean.user_msg.phone);
                            if (null != userBean.user_detail) {
                                if (!TextUtils.isEmpty(userBean.user_detail.nickname)) {
                                    if(isBase64(userBean.user_detail.nickname)){
                                        tv_username.setText(base64Decode(userBean.user_detail.nickname,"utf-8"));
                                        txtName.setText(base64Decode(userBean.user_detail.nickname,"utf-8"));
                                    }else{
                                        tv_username.setText(userBean.user_detail.nickname);
                                        txtName.setText(userBean.user_detail.nickname);
                                    }
                                } else {
                                    tv_username.setText(userBean.user_msg.phone);
                                    txtName.setText(userBean.user_msg.phone);
                                }
                                txtCode.setText(CaiNiaoApplication.getUserInfoBean().user_msg.auth_code);
                                if(CaiNiaoApplication.getUserBean().getAvatar()==null||CaiNiaoApplication.getUserBean().getAvatar().equals("")){
                                    Glide.with(context).load(SPUtils.getStringData(context,"default_avatar","")).placeholder(R.mipmap.icon_defult_boy).error(R.mipmap.icon_defult_boy).dontAnimate().into(civ_head);
                                }else{
                                    Glide.with(context).load(CaiNiaoApplication.getUserBean().getAvatar()).placeholder(R.mipmap.icon_defult_boy).error(R.mipmap.icon_defult_boy).dontAnimate().into(civ_head);
                                }                                mAcache = ACache.get(getActivity());
                                if (null != userBean) {
                                    if (null != userBean.user_detail && null != userBean.user_detail.avatar) {
                                        mAcache.put("avatar", userBean.user_detail.avatar);
                                    }
                                    if (null != userBean.user_msg && null != userBean.user_msg.group_id) {
                                        mAcache.put("group_id", userBean.user_msg.group_id);
                                        if ("3".equals(userBean.user_msg.group_id)) {
//                                            tv_isvip.setText("精英熊");
                                            txtMarketIncome.setVisibility(View.VISIBLE);
                                            viewOne.setBackgroundColor(context.getResources().getColor(R.color.colo_3366));
                                            viewTwo.setBackgroundColor(context.getResources().getColor(R.color.colo_3366));
                                            viewThree.setBackgroundColor(context.getResources().getColor(R.color.col_eb));
                                            imgOne.setImageResource(R.drawable.shape_dot);
                                            imgTwo.setImageResource(R.drawable.ic_dot);
                                            imgThree.setImageResource(R.drawable.shape_dot_gray);
                                            txtOne.setTextColor(context.getResources().getColor(R.color.colo_3366));
                                            txtTwo.setTextColor(context.getResources().getColor(R.color.colo_3366));
                                            txtThree.setTextColor(context.getResources().getColor(R.color.gray));
                                        } else if ("4".equals(userBean.user_msg.group_id)) {
//                                            tv_isvip.setText("汇客熊");
                                            txtMarketIncome.setVisibility(View.VISIBLE);
                                            viewOne.setBackgroundColor(context.getResources().getColor(R.color.colo_3366));
                                            viewTwo.setBackgroundColor(context.getResources().getColor(R.color.colo_3366));
                                            viewThree.setBackgroundColor(context.getResources().getColor(R.color.colo_3366));
                                            imgOne.setImageResource(R.drawable.shape_dot);
                                            imgTwo.setImageResource(R.drawable.shape_dot);
                                            imgThree.setImageResource(R.drawable.ic_dot);
                                            txtOne.setTextColor(context.getResources().getColor(R.color.colo_3366));
                                            txtTwo.setTextColor(context.getResources().getColor(R.color.colo_3366));
                                            txtThree.setTextColor(context.getResources().getColor(R.color.colo_3366));
                                        } else if ("2".equals(userBean.user_msg.group_id)) {
//                                            tv_isvip.setText("奋斗熊");
                                            txtMarketIncome.setVisibility(View.GONE);
                                            viewOne.setBackgroundColor(context.getResources().getColor(R.color.colo_3366));
                                            viewTwo.setBackgroundColor(context.getResources().getColor(R.color.col_eb));
                                            viewThree.setBackgroundColor(context.getResources().getColor(R.color.col_eb));
                                            imgOne.setImageResource(R.drawable.ic_dot);
                                            imgTwo.setImageResource(R.drawable.shape_dot_gray);
                                            imgThree.setImageResource(R.drawable.shape_dot_gray);
                                            txtOne.setTextColor(context.getResources().getColor(R.color.colo_3366));
                                            txtTwo.setTextColor(context.getResources().getColor(R.color.gray));
                                            txtThree.setTextColor(context.getResources().getColor(R.color.gray));
                                        } else {
//                                            tv_isvip.setText("成长熊");
                                            txtMarketIncome.setVisibility(View.GONE);
                                            viewOne.setBackgroundColor(context.getResources().getColor(R.color.col_eb));
                                            viewTwo.setBackgroundColor(context.getResources().getColor(R.color.col_eb));
                                            viewThree.setBackgroundColor(context.getResources().getColor(R.color.col_eb));
                                            imgOne.setImageResource(R.drawable.shape_dot_gray);
                                            imgTwo.setImageResource(R.drawable.shape_dot_gray);
                                            imgThree.setImageResource(R.drawable.shape_dot_gray);
                                            txtOne.setTextColor(context.getResources().getColor(R.color.gray));
                                            txtTwo.setTextColor(context.getResources().getColor(R.color.gray));
                                            txtThree.setTextColor(context.getResources().getColor(R.color.gray));
                                        }
                                    }
                                }
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

    private void getVipData() {
        if("".equals(SPUtils.getStringData(context, "token", ""))){
            return;
        }
        RequestParams params = new RequestParams();
        HttpUtils.post(Constants.GET_VIP_DATA,MyFragment.this, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // 输出错误信息
            }

            @Override
            public void onFinish() {
                // 隐藏进度条
                super.onFinish();
                refreshLayout.finishRefresh();
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
                        txtGrade.setText(jsonObject.getJSONObject("data").getString("exp"));
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

    @Override
    public void onResume() {
        super.onResume();
        token = SPUtils.getStringData(context, "token", "");
        if("1".equals(SPUtils.getStringData(context,"is","1"))){
            refreshLayout.autoRefresh();
            SPUtils.saveStringData(context,"is","0");
        }
    }
    private void getGroupList() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.GROUP_LIST, MyFragment.this,requestParams, new onOKJsonHttpResponseHandler<GroupListBean>(new TypeToken<com.android.jdhshop.bean.Response<GroupListBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(throwable.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, com.android.jdhshop.bean.Response<GroupListBean> datas) {
                if (datas.isSuccess()) {
                    List<GroupListBean.Item> items=datas.getData().list;
                    for (int i=0;i<items.size();i++){
                        switch (i){
                            case 0:
                                txtFour.setText(items.get(i).title);
                                break;
                            case 1:
                                txtOne.setText(items.get(i).title);
                                break;
                            case 2:
                                txtTwo.setText(items.get(i).title);
                                break;
                            case 3:
                                txtThree.setText(items.get(i).title);
                                break;
                        }
                    }
                } else {
                    showToast(datas.getMsg());
                }
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void sendDistanceY(int distance) {

    }
}
