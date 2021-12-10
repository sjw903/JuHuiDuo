package com.android.jdhshop.fragments;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.AiEstablishActivity;
import com.android.jdhshop.activity.AiWeiXinQunActivity;
import com.android.jdhshop.activity.ChaojiDaoHangActivity;
import com.android.jdhshop.activity.CommissionPhbActivity;
import com.android.jdhshop.activity.FeedBackActivity;
import com.android.jdhshop.activity.InComeActivity;
import com.android.jdhshop.activity.JuDuoHuiActivity;
import com.android.jdhshop.activity.MyMarketActivity;
import com.android.jdhshop.activity.NewsActivity;
import com.android.jdhshop.activity.SetActivity;
import com.android.jdhshop.activity.SetAssistantActivity;
import com.android.jdhshop.activity.SysMessageActivity;
import com.android.jdhshop.activity.VipActivity;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.bean.MessageEvent;
import com.android.jdhshop.bean.UserBean;
import com.android.jdhshop.bean.UserInfoBean;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.mall.MyShopMallOrderActivity;
import com.android.jdhshop.merchantactivity.OrderListActivity;
import com.android.jdhshop.my.CollectionActivity;
import com.android.jdhshop.my.MyInformationActivity;
import com.android.jdhshop.my.MyMessageActivity;
import com.android.jdhshop.my.MyOrderActivity;
import com.android.jdhshop.my.MyShareUrlActivity;
import com.android.jdhshop.userupdate.UpdateGroupActivity;
import com.android.jdhshop.widget.CircleImageView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
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

public class MyFragmentNew extends BaseLazyFragment {
    @BindView(R.id.it_text)
    TextView it_text;
    @BindView(R.id.swipe)
    SmartRefreshLayout swipeRefreshLayout;
    @BindView(R.id.txt_code)
    TextView Code;
    @BindView(R.id.tvUsername)
    TextView tvUsername;
    @BindView(R.id.head_2)
    CircleImageView head;
    @BindView(R.id.shengqing_text)
    TextView shenqing_zhuli;
    @BindView(R.id.it_button)
    Button it_button;
    private AlibcLogin alibcLogin;
    @BindView(R.id.txt_ye)
    TextView txtYe;
    @BindView( R.id.txt_jie)
    TextView txtJie;
    @BindView( R.id.txt_last_may )
    TextView txtLastMay;
    @BindView( R.id.txt_may_money )
    TextView txtMayMoney;
    @BindView( R.id.txt_month_money )
    TextView txtMonthMoney;
    @BindView( R.id.txt_today_money )
    TextView txtTodayMoney;
    @BindView( R.id.qunshu )
    TextView qunshu;
    @BindView( R.id.ai_weixin )
    LinearLayout ai_weixin;
    @BindView( R.id.yishenqing )
    LinearLayout yishenqing;
    @BindView( R.id.wodetop )
    ImageView wodetop;
    @BindView( R.id.xiaoxx)
    ImageView xiaoxx;
    @BindView( R.id.liwuwu)
    ImageView liwuwu;
    @BindView( R.id.image_view_gift)
    ImageView image_view_gift;
    @BindView( R.id.image_view_phb)
    ImageView image_view_phb;
    @BindView( R.id.txt_sy)
    TextView txt_sy;
    @BindView( R.id.txt_dd)
    TextView txt_dd;
    @BindView( R.id.txt_market)
    TextView txt_market;
    @BindView( R.id.txt_tj)
    TextView txt_tj;
    @BindView( R.id.ll_vip)
    TextView ll_vip;
    @BindView( R.id.txt_gg)
    TextView txt_gg;
    @BindView( R.id.txt_mes)
    TextView txt_mes;
    @BindView( R.id.txt_kf)
    TextView txt_kf;
    @BindView( R.id.txt_collect)
    TextView txt_collect;
    @BindView( R.id.txt_fk)
    TextView txt_fk;
    @BindView( R.id.txt_about)
    TextView txt_about;
    @BindView( R.id.txt_hz)
    TextView txt_hz;
    @BindView(R.id.ai_dou_qun)
    LinearLayout ai_douqun;
    @BindView(R.id.txt_account_set)
    TextView txt_account_set;
    @BindView(R.id.hui_txt_ye)
    TextView hui_txt_ye; //当前惠币数量
    @BindView(R.id.hui_txt_today_money)
    TextView hui_txt_today_money; // 今日收益
    @BindView(R.id.hui_txt_may_money)
    TextView hui_txt_may_money; // 昨日收益
    @BindView(R.id.hui_txt_month_money)
    TextView hui_txt_month_money; // 累计收益
    @BindView(R.id.hui_xiaoxx)
    ImageView hui_xiaoxx;
    @BindView(R.id.txt_setting)
    ImageView txt_setting;
    @BindView(R.id.txt_cjdh)
    TextView txt_cjdh;

    private View view;
    DecimalFormat df=new DecimalFormat("0.00");
    @Override
    protected void lazyload() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.activity_grzx, container, false );
        ButterKnife.bind( this, view );
        init();

//        Drawable txt_account_set_icon = getResources().getDrawable(R.drawable.to);
//        txt_account_set_icon.setBounds(10,0,28,34);
//        txt_account_set_icon.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);

        Drawable dr = getResources().getDrawable(R.drawable.to);
        dr.setBounds(0,0,80,0);
        dr.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);
        txt_cjdh.setCompoundDrawables(null,dr,null,null);

        try {
            InputStream is = getActivity().getAssets().open("web_app/img/hbcoins.png");
            Drawable drawable = Drawable.createFromStream(is,"hbcoins.png");
            hui_xiaoxx.setImageDrawable(drawable);
        }
        catch (Exception e){
            e.printStackTrace();
            Drawable drawable = getResources().getDrawable(R.mipmap.jinbi);
            hui_xiaoxx.setImageDrawable(drawable);
        }

//        txt_account_set.setCompoundDrawables(null,null,txt_account_set_icon,null);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getVipData();
                getUserMsg();
//                weixinqun();
            }
        });
        ai_douqun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new  AlertDialog.Builder(getContext());
                alertDialog.setTitle("").setMessage("抖群功能正在加速开发中，敬请期待!").setPositiveButton("知道啦", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });

        shenqing_zhuli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assistant();
            }
        });
        it_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, JuDuoHuiActivity.class);
                intent.putExtra("url", "./profit.html");
                startActivity(intent);

//                if (CaiNiaoApplication.getUserInfoBean().user_msg.alipay_account == null) {
//                    openActivity(BindActivity.class);
//                } else {
//                    Bundle b = new Bundle();
//                    b.putString("balance",txtYe.getText().toString().replace("余额: ", ""));
//                    openActivity(PutForwardActivity.class, b);
//                }
            }
        } );
        head.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity( MyInformationActivity.class );
            }
        } );
        it_text.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cmb = (ClipboardManager) context.getSystemService( Context.CLIPBOARD_SERVICE );
                cmb.setText( Code.getText().toString().trim() );
                T.showShort( context, "复制成功，快去邀请好友吧" );
            }
        } );
        view.findViewById( R.id.image_view ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FeedBackActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
            }
        } );
        view.findViewById( R.id.image_view_phb ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(CommissionPhbActivity.class);
            }
        });

        LogDingZiYuan();//加载资源

        return view;
    }
    private void LogDingZiYuan(){
        //本地资源文件获取图片
//        Bitmap bitmap1 = getLoacalBitmap(Constants.ZIYUAN_PATH+"bg_personal_top.png");
        Bitmap bitmap2 = getLoacalBitmap(Constants.ZIYUAN_PATH+"icon_personal_money.png");
        Bitmap bitmap3 = getLoacalBitmap(Constants.ZIYUAN_PATH+"icon_personal_gift.png");
        Bitmap bitmap4 = getLoacalBitmap(Constants.ZIYUAN_PATH+"img_my_one.png");
        Bitmap bitmap5 = getLoacalBitmap(Constants.ZIYUAN_PATH+"img_my_three.png");
        Bitmap bitmap6 = getLoacalBitmap(Constants.ZIYUAN_PATH+"wodeshouyi.png");
        Bitmap bitmap7 = getLoacalBitmap(Constants.ZIYUAN_PATH+"dingdan.png");
        Bitmap bitmap8 = getLoacalBitmap(Constants.ZIYUAN_PATH+"wodetuandui.png");
        Bitmap bitmap9 = getLoacalBitmap(Constants.ZIYUAN_PATH+"yaoqinghaoyou.png");
        Bitmap bitmap10 = getLoacalBitmap(Constants.ZIYUAN_PATH+"xinrenketang.png");
        Bitmap bitmap11 = getLoacalBitmap(Constants.ZIYUAN_PATH+"changjianwenti.png");
        Bitmap bitmap12 = getLoacalBitmap(Constants.ZIYUAN_PATH+"guanfanggonggao.png");
        Bitmap bitmap13 = getLoacalBitmap(Constants.ZIYUAN_PATH+"kefu.png");
        Bitmap bitmap14 = getLoacalBitmap(Constants.ZIYUAN_PATH+"shoucang.png");
        Bitmap bitmap15 = getLoacalBitmap(Constants.ZIYUAN_PATH+"fankui.png");
        Bitmap bitmap16 = getLoacalBitmap(Constants.ZIYUAN_PATH+"guanyuwomen.png");
        Bitmap bitmap17 = getLoacalBitmap(Constants.ZIYUAN_PATH+"shangwuhezuo.png");
        BitmapDrawable bd=new BitmapDrawable(bitmap6);
        BitmapDrawable bd1=new BitmapDrawable(bitmap7);
        BitmapDrawable bd2=new BitmapDrawable(bitmap8);
        BitmapDrawable bd3=new BitmapDrawable(bitmap9);
        BitmapDrawable bd4=new BitmapDrawable(bitmap10);
        BitmapDrawable bd5=new BitmapDrawable(bitmap11);
        BitmapDrawable bd6=new BitmapDrawable(bitmap12);
        BitmapDrawable bd7=new BitmapDrawable(bitmap13);
        BitmapDrawable bd8=new BitmapDrawable(bitmap14);
        BitmapDrawable bd9=new BitmapDrawable(bitmap15);
        BitmapDrawable bd10=new BitmapDrawable(bitmap16);
        BitmapDrawable bd11=new BitmapDrawable(bitmap17);
        Drawable bd13 = getResources().getDrawable(R.mipmap.icon_news_light);

        image_view_phb.setImageBitmap(bitmap5);
        image_view_gift.setImageBitmap(bitmap4);
        liwuwu.setImageBitmap(bitmap3);
        xiaoxx.setImageBitmap(bitmap2);

        if (android.os.Build.VERSION.RELEASE.equals("10") || android.os.Build.VERSION.RELEASE.equals("11") ) {
            LogUtils.d("版本banb", android.os.Build.VERSION.RELEASE+"");
            bd.setBounds(0, 0, 80, 80);// 一定要设置setBounds();
            bd1.setBounds(0, 0, 80, 80);// 一定要设置setBounds();
            bd2.setBounds(0, 0, 80, 80);// 一定要设置setBounds();
            bd3.setBounds(0, 0, 80, 80);// 一定要设置setBounds();
            bd4.setBounds(0, 0, 80, 80);// 一定要设置setBounds();
            bd5.setBounds(0, 0, 80, 80);// 一定要设置setBounds();
            bd6.setBounds(0, 0, 80, 80);// 一定要设置setBounds();
            bd7.setBounds(0, 0, 80, 80);// 一定要设置setBounds();
            bd8.setBounds(0, 0, 80, 80);// 一定要设置setBounds();
            bd9.setBounds(0, 0, 80,80);// 一定要设置setBounds();
            bd10.setBounds(0, 0, 80, 80);// 一定要设置setBounds();
            bd11.setBounds(0, 0, 80, 80);// 一定要设置setBounds();
            bd13.setBounds(0, 0, 80, 80);// 一定要设置setBounds();
            txt_sy.setCompoundDrawables(null,bd,null,null);
            txt_dd.setCompoundDrawables(null,bd1,null,null);
            txt_market.setCompoundDrawables(null,bd2,null,null);
            txt_tj.setCompoundDrawables(null,bd3,null,null);
            ll_vip.setCompoundDrawables(null,bd4,null,null);
            txt_gg.setCompoundDrawables(null,bd5,null,null);
            txt_mes.setCompoundDrawables(null,bd6,null,null);
            txt_kf.setCompoundDrawables(null,bd7,null,null);
            txt_collect.setCompoundDrawables(null,bd8,null,null);
            txt_fk.setCompoundDrawables(null,bd9,null,null);
            txt_about.setCompoundDrawables(null,bd10,null,null);
            txt_hz.setCompoundDrawables(null,bd11,null,null);
            txt_cjdh.setCompoundDrawables(null,bd13,null,null);
        }else{
            LogUtils.d("版本banb11", android.os.Build.VERSION.RELEASE+"");
            bd.setBounds(0, 0, 80, 80);// 一定要设置setBounds();
            bd1.setBounds(0, 0, 80, 80);// 一定要设置setBounds();
            bd2.setBounds(0, 0, 80, 80);// 一定要设置setBounds();
            bd3.setBounds(0, 0, 80, 80);// 一定要设置setBounds();
            bd4.setBounds(0, 0, 80, 80);// 一定要设置setBounds();
            bd5.setBounds(0, 0, 80, 80);// 一定要设置setBounds();
            bd6.setBounds(0, 0, 80, 80);// 一定要设置setBounds();
            bd7.setBounds(0, 0, 80, 80);// 一定要设置setBounds();
            bd8.setBounds(0, 0, 80, 80);// 一定要设置setBounds();
            bd9.setBounds(0, 0, 80,80);// 一定要设置setBounds();
            bd10.setBounds(0, 0, 80, 80);// 一定要设置setBounds();
            bd11.setBounds(0, 0, 80, 80);// 一定要设置setBounds();
            bd13.setBounds(0, 0, 80, 80);// 一定要设置setBounds();
            txt_sy.setCompoundDrawables(null,bd,null,null);
            txt_dd.setCompoundDrawables(null,bd1,null,null);
            txt_market.setCompoundDrawables(null,bd2,null,null);
            txt_tj.setCompoundDrawables(null,bd3,null,null);
            ll_vip.setCompoundDrawables(null,bd4,null,null);
            txt_gg.setCompoundDrawables(null,bd5,null,null);
            txt_mes.setCompoundDrawables(null,bd6,null,null);
            txt_kf.setCompoundDrawables(null,bd7,null,null);
            txt_collect.setCompoundDrawables(null,bd8,null,null);
            txt_fk.setCompoundDrawables(null,bd9,null,null);
            txt_about.setCompoundDrawables(null,bd10,null,null);
            txt_hz.setCompoundDrawables(null,bd11,null,null);
            txt_cjdh.setCompoundDrawables(null,bd13,null,null);
        }



    }
    /**
     * 加载本地图片
     *
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void assistant(){
        // 1.创建弹出式对话框
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());    // 系统默认Dialog没有输入框
        // 获取自定义的布局
        View alertDialogView = View.inflate(getContext(), R.layout.dialog_ai_assistant, null);
        final AlertDialog tempDialog = alertDialog.create();
        tempDialog.setView(alertDialogView, 0, 0, 0, 0);
        tempDialog.getWindow().setBackgroundDrawableResource(R.drawable.yuanjiao);
        final EditText editText = (EditText)alertDialogView.findViewById(R.id.ed_message);
        tempDialog.setCancelable(true);
        tempDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView quxiao= alertDialogView.findViewById(R.id.positiveTextView);
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempDialog.dismiss();
            }
        });
        TextView queren= alertDialogView.findViewById(R.id.negativeTextView);
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AiEstablishActivity.class);
                intent.putExtra("tmpid", "3");
                context.startActivity(intent);
                //openActivity(AiEstablishActivity.class);
                tempDialog.dismiss();
            }
        });
        tempDialog.show();
    }
    private void init() {
        alibcLogin = AlibcLogin.getInstance();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onResume() {
        super.onResume();
        if (!CommonUtils.isNetworkAvailable()) {
            showToast( getResources().getString( R.string.error_network ) );
            return;
        }
        if ("".equals( SPUtils.getStringData( context, "token", "" ) )) {
            MessageEvent post_message = new MessageEvent("back_to_main");
            EventBus.getDefault().post(post_message);
            return;
        }
        getVipData();
        getUserMsg();


        // 微信群暂时不启用
//        weixinqun();
    }
    private void getUserMsg(){
        LogUtils.d("TAG", "getUserMsg: ");
        RequestParams params = new RequestParams();
        HttpUtils.post( Constants.GET_USER_MSG, MyFragmentNew.this,params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // 输出错误信息
            }
            @Override
            public void onFinish() {
                // 隐藏进度条
                super.onFinish();
                swipeRefreshLayout.finishRefresh();//.setRefreshing(false);
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    LogUtils.logd("获取个人信息: " + responseString);
                    JSONObject jsonObject = new JSONObject( responseString );
                    //返回码
                    UserInfoBean userBean = null;
                    int code = jsonObject.optInt( "code" );
                    //返回码说明
                    String msg = jsonObject.optString( "msg" );
                    String data = jsonObject.optString( "data" );
                    if (0 == code) {
                        if (!TextUtils.isEmpty( data )) {//GSON对数字都当做double解析
                            Gson gson = new Gson();
                            userBean = gson.fromJson( data.trim(), UserInfoBean.class );
                            CaiNiaoApplication.setUserInfoBean( userBean );
                        }
                        if (null != userBean) {
                            CaiNiaoApplication.setUserBean( new UserBean( userBean.user_detail.user_id, userBean.user_msg.group_id, SPUtils.getStringData( context, "token", "" ), userBean.user_detail.avatar, userBean.user_detail.nickname, userBean.user_msg.is_forever ) );
                            SPUtils.saveStringData(context, "phone", userBean.user_msg.phone);
                            SPUtils.saveStringData(context, "inviteCode", userBean.user_msg.auth_code);
                            hui_txt_ye.setText(userBean.user_msg.hpoint);
                            hui_txt_today_money.setText(userBean.user_msg.hpoint_today);
                            hui_txt_may_money.setText(userBean.user_msg.hpoint_yesterday);
                            hui_txt_month_money.setText(userBean.user_msg.hpoint_sum);
                            //是否申请ai助理
                            String hasTalkGroup = userBean.user_msg.hasTalkGroup;
//                                    if(hasTalkGroup.equals("true")){
//                                        shenqing_zhuli.setVisibility(View.GONE);
//                                        yishenqing.setVisibility(View.VISIBLE);
//                                    }else {
//                                        shenqing_zhuli.setVisibility(View.VISIBLE);
//                                        yishenqing.setVisibility(View.GONE);
//                                    }
                            if (CaiNiaoApplication.getUserBean() != null) {
                                if (!TextUtils.isEmpty( userBean.user_detail.nickname )) {
                                    if (isBase64( userBean.user_detail.nickname )) {
                                        tvUsername.setText( base64Decode( userBean.user_detail.nickname, "utf-8" ) );
                                    } else {
                                        tvUsername.setText( userBean.user_detail.nickname );
                                    }
                                } else {
                                    tvUsername.setText( userBean.user_msg.phone );
                                }
                                Code.setText( CaiNiaoApplication.getUserInfoBean().user_msg.auth_code );
                                if (CaiNiaoApplication.getUserBean().getAvatar() == null || CaiNiaoApplication.getUserBean().getAvatar().equals( "" )) {

                                    //Glide.with( context ).load( SPUtils.getStringData( context, "default_avatar", "" ) ).placeholder( R.mipmap.icon_defult_boy ).error( R.mipmap.icon_defult_boy ).dontAnimate().into( head );
                                } else {
                                    Glide.with( context ).load( CaiNiaoApplication.getUserBean().getAvatar() ).placeholder( R.mipmap.icon_defult_boy ).error( R.mipmap.icon_defult_boy ).dontAnimate().into( head );
                                }
                            }
                        }
                    } else {
                        showToast( msg );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onStart() {
                super.onStart();
            }
        } );
    }
    //微信群消息,设置群数量
    private void weixinqun(){
        RequestParams params = new RequestParams();
        HttpUtils.post(Constants.getWXQinXinXI,MyFragmentNew.this, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject( responseString );
                    JSONArray arry = jsonObject.getJSONArray("data");
                    int length = arry.length();
                    //showToast(length+"");
                    if(length>=11){
                        qunshu.setText("10");
                    }else{
                        qunshu.setText(length - 1+"");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @OnClick({R.id.my_zyallorder,R.id.my_zydfkorder,R.id.my_zydfhorder,R.id.my_zydshorder,
            R.id.my_zyywcorder,R.id.my_zydfkorder1,R.id.my_zydfhorder1,R.id.my_zydshorder1,
            R.id.my_zyywcorder1,R.id.txt_sy, R.id.ai_weixin,R.id.txt_dd,R.id.ai_assistant_set,R.id.ai_dou_qun, R.id.txt_market, R.id.txt_tj,R.id.txt_setting,R.id.ll_vip,R.id.txt_gg,R.id.txt_mes,R.id.txt_kf,R.id.txt_collect,R.id.txt_fk,R.id.txt_about,R.id.txt_hz,R.id.image_view_gift,R.id.txt_account_set,R.id.hui_it_button,R.id.txt_cjdh})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.txt_account_set:
                Bundle b = new Bundle();
                b.putString("url","./account_manage.html");
                openActivity(JuDuoHuiActivity.class,b);
                break;
            case R.id.ai_weixin:
                openActivity(AiWeiXinQunActivity.class);
                break;
            case R.id.ai_assistant_set:
                openActivity(SetAssistantActivity.class);
                break;
            case R.id.image_view_gift:
                openActivity(UpdateGroupActivity.class);
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
            case R.id.txt_sy:
                openActivity( InComeActivity.class );
                break;
            case R.id.txt_dd:
                whetherBindingTaobao();
                break;
            case R.id.txt_market:
                openActivity(MyMarketActivity.class);
                break;
            case R.id.txt_tj:
                openActivity(MyShareUrlActivity.class);
                break;
            case R.id.txt_setting:
                openActivity(SetActivity.class);
                break;
            case R.id.ll_vip:
                openActivity(VipActivity.class);
                break;
            case R.id.txt_gg:
                openActivity(MyMessageActivity.class);
                break;
            case R.id.txt_mes:
                openActivity(SysMessageActivity.class);
                break;
            case R.id.txt_kf:
//                openActivity(KfActivity.class);
                Intent kf_intent = new Intent(context, JuDuoHuiActivity.class);
                LogUtils.d(TAG, "onViewClicked: " + SPUtils.getStringData(getActivity(),"kefu_url","https://jq.qq.com/?_wv=1027&k=orJGmf3a"));
                String custom_kefu = SPUtils.getStringData(getActivity(),"kefu_url","https://jq.qq.com/?_wv=1027&k=orJGmf3a");
                kf_intent.putExtra("url", custom_kefu);
                startActivity(kf_intent);
                break;
            case R.id.txt_collect:
                openActivity(CollectionActivity.class);
                break;
            case R.id.txt_fk:
                Intent intent = new Intent(context, FeedBackActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.txt_about:
                NewsActivity.actionStart(context, "27", "关于我们");
                LogUtils.d(TAG, "onViewClicked: 点击关于我们");
                break;
            case R.id.txt_hz:
                intent = new Intent(context, FeedBackActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
            case R.id.hui_it_button:
                intent = new Intent(context, JuDuoHuiActivity.class);
                intent.putExtra("url", "./profit.html?tab_value=1");
                startActivity(intent);
                break;
            case R.id.txt_cjdh:
                openActivity(ChaojiDaoHangActivity.class);
                LogUtils.d(TAG, "onViewClicked: 点击购物导航");
                break;

        }
    }
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
//                                Log.i("FanliActivity", "淘宝授权登录失败信息=" + msg);
//                                MyFragmentNew.this.getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(MyFragmentNew.this.getActivity(), "请您进行淘宝授权后再进行操作",
//                                                Toast.LENGTH_LONG).show();
//                                    }
//                                });
//                            }
//                        });
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
                        MyFragmentNew.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                openActivity(MyOrderActivity.class);
                                Toast.makeText(MyFragmentNew.this.getActivity(), "绑定淘宝账号成功",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(MyFragmentNew.this.getActivity(), "绑定淘宝账号失败",
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
        return Pattern.matches( base64Pattern, str );
    }
    private String base64Decode(String content, String charsetName) {
        if (TextUtils.isEmpty( charsetName )) {
            charsetName = "UTF-8";
        }
        byte[] contentByte = Base64.decode( content, Base64.DEFAULT );
        try {
            return new String( contentByte, charsetName );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
    private void getVipData() {
        LogUtils.d("TAG", "getVipData: ");
        RequestParams params = new RequestParams();
        HttpUtils.post(Constants.GET_VIP_DATA,MyFragmentNew.this, params, new TextHttpResponseHandler() {
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
                        txtYe.setText("" + jsonObject.getJSONObject("data").getString("balance"));
                        txtLastMay.setText(jsonObject.getJSONObject("data").getString("amount_last"));
                        txtMayMoney.setText(jsonObject.getJSONObject("data").getString("amount_current"));
                        txtMonthMoney.setText(jsonObject.getJSONObject("data").getString("amount_last_finish"));
                        txtTodayMoney.setText(jsonObject.getJSONObject("data").getString("amount_today"));
                        txtJie.setText(getString(R.string.app_name) +"已为您节省 " + jsonObject.getJSONObject("data").getString("amount") + " 元");
                        SPUtils.saveStringData(context, "my_money_one", jsonObject.getJSONObject("data").getString("amount"));
                        SPUtils.saveStringData(context, "my_money_two", jsonObject.getJSONObject("data").getString("amount_last"));
                        SPUtils.saveStringData(context, "my_money_three", jsonObject.getJSONObject("data").getString("amount_current"));
                        SPUtils.saveStringData(context, "my_money_four", jsonObject.getJSONObject("data").getString("balance"));

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