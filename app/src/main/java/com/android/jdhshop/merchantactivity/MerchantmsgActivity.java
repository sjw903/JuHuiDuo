package com.android.jdhshop.merchantactivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.DjjActivity;
import com.android.jdhshop.activity.SjhdListAxticity;
import com.android.jdhshop.activity.SjxxsDjjActivity;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.merchantadapter.MerchantDjqAdapter;
import com.android.jdhshop.merchantadapter.MerchantNewAdapter;
import com.android.jdhshop.merchantadapter.MerchantimglistAdapter;
import com.android.jdhshop.merchantadapter.MerchantshoplistAdapter;
import com.android.jdhshop.merchantbean.MerchantNewBean;
import com.android.jdhshop.merchantbean.Merchantgglistbean;
import com.android.jdhshop.merchantbean.Merchantimglist;
import com.android.jdhshop.merchantbean.Merchantmsgbean;
import com.android.jdhshop.merchantbean.Merchantshoplistbean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.loopj.android.http.RequestParams;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MerchantmsgActivity extends BaseActivity implements View.OnClickListener {


    private LinearLayout ly_back;
    private ImageView iv_sc, iv_share, iv_callphone, shmsg_location, icon_time, xiaoxiimage;
    private TextView tv_name, tv_rjxf, tv_xiaol, tv_address, tv_isyy, tv_yytime, tv_more;
    private RecyclerView imgrecy, fwrecy, tgcrecy;
    private TextView tv_yhxx;
    private RelativeLayout rl_sjcd, rl_sjpl, rl_sjhd;
    private LinearLayout ly_address, ly_callphone;
    private TextView tv_tgmore;
    private TextView tv_score;
    private MerchantimglistAdapter merchantimglistAdapter;
    private MerchantshoplistAdapter merchantshoplistAdapter;

    public boolean flag = false;
    @BindView(R.id.recy_new)
    RecyclerView recy_new;
    private MerchantNewAdapter newAdapter;
    private List<MerchantNewBean.Item> list = new ArrayList<>();
    @BindView(R.id.recy_djq)
    RecyclerView recy_djq;
    MerchantDjqAdapter djqAdapter;
    private List<MerchantNewBean.Item> list2 = new ArrayList<>();
    private String title;
    private String cont;
    private ImageView merchimage;

    private void LogDingZiYuan() {

        BaseLogDZiYuan.LogDingZiYuan(merchimage, "icon_back_while.png");
        BaseLogDZiYuan.LogDingZiYuan(iv_sc, "icon_collection.png");
        BaseLogDZiYuan.LogDingZiYuan(iv_share, "icon_share.png");
        BaseLogDZiYuan.LogDingZiYuan(shmsg_location, "icon_location.png");
        BaseLogDZiYuan.LogDingZiYuan(iv_callphone, "icon_phone.png");
        BaseLogDZiYuan.LogDingZiYuan(icon_time, "icon_time.png");
        BaseLogDZiYuan.LogDingZiYuan(xiaoxiimage, "xiaoxiss.png");
    }

    @Override
    protected void initUI() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_merchantmsg);
        ButterKnife.bind(this);
        ly_back = findViewById(R.id.shmsg_lyback);
        iv_sc = findViewById(R.id.shmsg_sc);
        iv_share = findViewById(R.id.shmsg_share);
        icon_time = findViewById(R.id.icon_time);
        xiaoxiimage = findViewById(R.id.xiaoxiimage);
        shmsg_location = findViewById(R.id.shmsg_location);
        iv_callphone = findViewById(R.id.shmsg_ivcallphone);
        tv_name = findViewById(R.id.shmsg_name);
        tv_rjxf = findViewById(R.id.shmsg_rjxf);
        tv_xiaol = findViewById(R.id.shmsg_xiaol);
        tv_address = findViewById(R.id.shmsg_address);
        tv_isyy = findViewById(R.id.shmsg_isyy);
        tv_yytime = findViewById(R.id.shmsg_yytime);
        tv_more = findViewById(R.id.shmsg_more);
        imgrecy = findViewById(R.id.shmsg_imgrecy);
        fwrecy = findViewById(R.id.shmsg_fwrecy);
        tv_yhxx = findViewById(R.id.shmsg_tvyhxx);
        tgcrecy = findViewById(R.id.shmsg_tgrecy);
        tv_tgmore = findViewById(R.id.shmsg_tvtgmore);
        ly_address = findViewById(R.id.shmsg_lyaddress);
        ly_callphone = findViewById(R.id.shmsg_lycallphone);
        rl_sjcd = findViewById(R.id.shmsg_rlsjcd);
        rl_sjpl = findViewById(R.id.shmsg_rlsjpl);
        rl_sjhd = findViewById(R.id.shmsg_rlsjhd);
        tv_score = findViewById(R.id.tv_score);
        ly_back.setOnClickListener(this);
        tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (MapUtil.checkMapAppsIsExist(MerchantmsgActivity.this, MapUtil.BAIDU_PKG)) {
                        MapUtil.openGaoDe(MerchantmsgActivity.this, Double.valueOf(Constants.lat), Double.valueOf(Constants.lng));
                    } else {
                        if (MapUtil.checkMapAppsIsExist(MerchantmsgActivity.this, MapUtil.GAODE_PKG))
                            MapUtil.openGaoDe(MerchantmsgActivity.this, Double.valueOf(Constants.lat), Double.valueOf(Constants.lng));
                        else
                            ToastUtils.showLongToast(MerchantmsgActivity.this, "未检测到百度或高德导航软件");
                    }
                } catch (Exception e) {
                    ToastUtils.showLongToast(MerchantmsgActivity.this, "获取商家位置失败");
                }
            }
        });
        isCollect();
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle bundle=new Bundle();
//                bundle.putString("merchant_id", getIntent().getStringExtra("msgid"));
//                openActivity( SjxxsActivity.class,bundle);
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = Constants.APP_IP + "/wap/Merchant/share/merchant_id/" + getIntent().getStringExtra("msgid") + "/referrer_id/" + SPUtils.getStringData(MerchantmsgActivity.this, "uid", "");
                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = webpage;
                msg.title = title;
                msg.description = cont;
                //设置缩略图
                if (bitmap != null) {
                    Bitmap bt = Bitmap.createScaledBitmap(bitmap, 80, 80, true);
                    msg.thumbData = bmpToByteArray(bt, true);
                }
//                Bitmap thumbBmp = BitmapFactory.decodeResource(getResources(), R.drawable.);
//                msg.thumbData = bmpToByteArray(thumbBmp, true);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("img");//  transaction字段用
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneSession;
                CaiNiaoApplication.api.sendReq(req);
            }
        });
        findViewById(R.id.recyclerView_djq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("msgid", getIntent().getStringExtra("msgid"));
                bundle.putString("name", tv_name.getText().toString());
                openActivity(DjjActivity.class, bundle);
            }
        });
        findViewById(R.id.txt_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MerchantmsgActivity.this, ShopaddorderOfflineActivity.class);
                intent.putExtra("pric", "0");
                intent.putExtra("img", datadetail.merchant_avatar);
                intent.putExtra("name", datamsg.merchant_name);
//                    intent.putExtra( "sku",jsonArray.toString() );
                intent.putExtra("goodid", datamsg.merchant_id);
                intent.putExtra("type", "1");
                intent.putExtra("dkq", "0");
                startActivity(intent);
            }
        });
        rl_sjpl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("msgid", getIntent().getStringExtra("msgid"));
                bundle.putString("name", tv_name.getText().toString());
                openActivity(SjxxsDjjActivity.class, bundle);

            }
        });
        rl_sjhd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("merchant_id", getIntent().getStringExtra("msgid"));
                openActivity(SjhdListAxticity.class, bundle);
            }
        });
        findViewById(R.id.shmsg_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MapUtil.checkMapAppsIsExist(MerchantmsgActivity.this, MapUtil.BAIDU_PKG)) {
                    MapUtil.openGaoDe(MerchantmsgActivity.this, Double.valueOf(Constants.lat), Double.valueOf(Constants.lng));
                } else {
                    if (MapUtil.checkMapAppsIsExist(MerchantmsgActivity.this, MapUtil.GAODE_PKG))
                        MapUtil.openGaoDe(MerchantmsgActivity.this, Double.valueOf(Constants.lat), Double.valueOf(Constants.lng));
                    else
                        ToastUtils.showLongToast(MerchantmsgActivity.this, "未检测到百度或高德导航软件");
                }
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recy_new.setLayoutManager(manager);
        newAdapter = new MerchantNewAdapter(R.layout.item_merchant_new, list);
        recy_new.setAdapter(newAdapter);
        getList();
        newAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("news_id", list.get(position).news_id);
                openActivity(MerchantNewDetailActivity.class, bundle);
            }
        });
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recy_djq.setLayoutManager(manager);
        djqAdapter = new MerchantDjqAdapter(R.layout.item_djq, list2);
        recy_djq.setAdapter(djqAdapter);
        getList2();
        djqAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("merchant_id", getIntent().getStringExtra("msgid"));
                bundle.putSerializable("bean", list2.get(position));
                bundle.putString("name", tv_name.getText().toString());
                openActivity(SjxxsDjjActivity.class, bundle);
            }
        });
        djqAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                buy(position);
            }
        });
    }


    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private void buy(int po) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("voucher_id", list2.get(po).id);
        requestParams.put("num", "1");
        HttpUtils.post(Constants.APP_IP + "/api/O2oVoucherOrder/order", MerchantmsgActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
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
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        Intent intent = new Intent(MerchantmsgActivity.this, MerRechargeActivity2.class);
                        intent.putExtra("ordernum", object.getJSONObject("data").getString("order_num"));
                        intent.putExtra("allprice", list2.get(po).price);
                        intent.putExtra("title", "商家代金券购买");
                        intent.putExtra("orderid", object.getJSONObject("data").getString("order_id"));
                        startActivity(intent);
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getList2() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("merchant_id", getIntent().getStringExtra("msgid"));
        requestParams.put("p", "1");
        requestParams.put("per", "2");
        HttpUtils.post(Constants.APP_IP + "/api/O2oVoucher/getList", MerchantmsgActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Log.e("msg", responseString);
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        list2.addAll(new Gson().fromJson(object.getJSONObject("data").toString(), MerchantNewBean.class).list);
                        if (list2.size() > 0) {
                            findViewById(R.id.ll_djq).setVisibility(View.VISIBLE);
                        }
                        djqAdapter.notifyDataSetChanged();
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getList() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("merchant_id", getIntent().getStringExtra("msgid"));
        requestParams.put("p", "1");
        requestParams.put("per", "8");
        HttpUtils.post(Constants.APP_IP + "/api/MerchantNews/getList",MerchantmsgActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Log.e("msg", responseString);
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        list.addAll(new Gson().fromJson(object.getJSONObject("data").toString(), MerchantNewBean.class).list);
                        if (list.size() > 0) {
                            findViewById(R.id.shmsg_new).setVisibility(View.VISIBLE);
                        }
                        newAdapter.notifyDataSetChanged();
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void initData() {

        merchantimglistAdapter = new MerchantimglistAdapter(MerchantmsgActivity.this, imglist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        imgrecy.setLayoutManager(layoutManager);
        imgrecy.setAdapter(merchantimglistAdapter);

        merchantshoplistAdapter = new MerchantshoplistAdapter(MerchantmsgActivity.this, shoplist);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        tgcrecy.setLayoutManager(layoutManager1);
        tgcrecy.setAdapter(merchantshoplistAdapter);
        merchantshoplistAdapter.setsubClickListener(new MerchantshoplistAdapter.SubClickListener() {
            @Override
            public void OntopicClickListener(View v, String detail, int posit) {
                Intent gojf = new Intent(MerchantmsgActivity.this, ShopmsgActivity.class);
                gojf.putExtra("isyx", "is");
                gojf.putExtra("goodid", datamsg.merchant_id);
                gojf.putExtra("shopid", shoplist.get(posit).goods_id);
                gojf.putExtra("dkq", shoplist.get(posit).deduction_point);
                startActivity(gojf);
            }
        });
        getmerchantmsg();
        getmerchantjiesao();
        getmerchantgg();
        getmerchantshoplist();

        //收藏按钮
        iv_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag) { // 已收藏，取消收藏
                    cancelCollect();
                    iv_sc.setImageResource(R.mipmap.icon_collection);
                    flag = false;
                } else {//未收藏
                    doCollect();
                    flag = true;
                    iv_sc.setImageResource(R.mipmap.icon_collection_pre);
                }
            }
        });
        ly_callphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPhone(datamsg.phone);
            }
        });


    }

    Bitmap bitmap;

    //商家相册
    @Override
    protected void initListener() {
        rl_sjcd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MerchantmsgActivity.this, SjimagelistActivity.class);
                intent.putExtra("merid", datamsg.merchant_id);
                startActivity(intent);
            }
        });
        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MerchantmsgActivity.this, ShoplistmsgmoreActivity.class);
                intent.putExtra("title", tv_name.getText().toString());
                intent.putExtra("yytime", tv_yytime.getText().toString());
                intent.putExtra("service", datamsg.auth);
                intent.putExtra("id", datamsg.merchant_id);
                startActivity(intent);
            }
        });
        tv_tgmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shoplist.size() > 0) {
                    Intent gojf = new Intent(MerchantmsgActivity.this, ShopmsgActivity.class);
                    gojf.putExtra("isyx", "no");
                    gojf.putExtra("goodid", datamsg.merchant_id);
                    gojf.putExtra("shopid", shoplist.get(0).goods_id);
                    gojf.putExtra("dkq", shoplist.get(0).deduction_point);
                    startActivity(gojf);
                }

            }
        });


    }

    //获取商户信息
    public Merchantmsgbean.merchant_msg datamsg;
    public Merchantmsgbean.merchant_detail datadetail;

    private void getmerchantmsg() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("merchant_id", getIntent().getStringExtra("msgid"));
        HttpUtils.post(Constants.getMsg, MerchantmsgActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("dsfasd", responseString + Constants.getMsg + "whatfar:" + getIntent().getStringExtra("msgid"));
                try {
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        JSONObject array = object.getJSONObject("data").getJSONObject("merchant_msg");
                        datamsg = new Gson().fromJson(array.toString(), Merchantmsgbean.merchant_msg.class);
                        title = datamsg.merchant_name;
                        JSONObject array1 = object.getJSONObject("data").getJSONObject("merchant_detail");
                        datadetail = new Gson().fromJson(array1.toString(), Merchantmsgbean.merchant_detail.class);
                        cont = datadetail.province + datadetail.city + datadetail.county + datadetail.detail_address;
                        Message message = Message.obtain();
                        Glide.with(MerchantmsgActivity.this).load(Constants.APP_IP + datadetail.merchant_avatar).asBitmap().into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                bitmap = resource;
                            }
                        });
                        message.what = 1;
                        mHandler.sendMessage(message);
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //获取商户店铺图片
    public List<Merchantimglist> imglist = new ArrayList<>();

    private void getmerchantjiesao() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("merchant_id", getIntent().getStringExtra("msgid"));
        requestParams.put("type", "2");
        requestParams.put("p", "1");
        requestParams.put("per", "8");
        HttpUtils.post(Constants.getimgList, MerchantmsgActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Log.e("msg", responseString);
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        JSONArray array = object.getJSONObject("data").getJSONArray("list");
                        for (int i = 0; i < array.length(); i++) {
                            imglist.add(new Gson().fromJson(array.getJSONObject(i).toString(), Merchantimglist.class));
                        }
                        merchantimglistAdapter.notifyDataSetChanged();

                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //获取商户公告列表
    public List<Merchantgglistbean> shgglist = new ArrayList<>();

    private void getmerchantgg() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("merchant_id", getIntent().getStringExtra("msgid"));
        requestParams.put("p", "1");
        requestParams.put("per", "8");
        HttpUtils.post(Constants.getggList,MerchantmsgActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Log.e("msg", responseString);
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        JSONArray array = object.getJSONObject("data").getJSONArray("list");
                        for (int i = 0; i < array.length(); i++) {
                            shgglist.add(new Gson().fromJson(array.getJSONObject(i).toString(), Merchantgglistbean.class));
                        }
                        Message message = Message.obtain();
                        message.what = 2;
                        mHandler.sendMessage(message);
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //获取商品列表
    public List<Merchantshoplistbean> shoplist = new ArrayList<>();

    private void getmerchantshoplist() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", SPUtils.getStringData(MerchantmsgActivity.this, "token", ""));
        requestParams.put("merchant_id", getIntent().getStringExtra("msgid"));
        requestParams.put("p", "1");
        requestParams.put("per", "8");
        HttpUtils.post(Constants.getservicelist,MerchantmsgActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Log.e("msg", responseString);
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        JSONArray array = object.getJSONObject("data").getJSONArray("list");
                        for (int i = 0; i < array.length(); i++) {
                            shoplist.add(new Gson().fromJson(array.getJSONObject(i).toString(), Merchantshoplistbean.class));
                        }
                        merchantshoplistAdapter.notifyDataSetChanged();
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://详情
                    if (datadetail == null || datamsg == null) {
                        return;
                    }
                    tv_name.setText(datamsg.merchant_name);
                    tv_rjxf.setText("¥" + datamsg.consumption + "/人");
                    tv_score.setText(datamsg.comment_score + "分");
                    tv_xiaol.setText("销量:" + datamsg.sales_num);
                    tv_address.setText(datadetail.detail_address);
                    tv_yytime.setText("周" + onetoone(datadetail.business_day_begin) + "到周" + onetoone(datadetail.business_day_end) + "   " + datadetail.business_hours_begin.replace("时", "") + " - " + datadetail.business_hours_end.replace("时", "") + "");


                    break;
                case 2://获取商家公告
                    if (shgglist.size() != 0) {
                        tv_yhxx.setText(shgglist.get(0).content);
                    } else {
                        tv_yhxx.setText("暂无公告");
                    }
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shmsg_lyback:
                finish();
                break;
        }
    }

    public String onetoone(String str) {
        switch (str) {
            case "1":
                str = "一";
                break;
            case "2":
                str = "二";
                break;
            case "3":
                str = "三";
                break;
            case "4":
                str = "四";
                break;
            case "5":
                str = "五";
                break;
            case "6":
                str = "六";
                break;
            case "7":
                str = "日";
                break;
        }
        return str;
    }

    /**
     * 收藏店铺
     */
    private void doCollect() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", SPUtils.getStringData(MerchantmsgActivity.this, "token", ""));
        requestParams.put("merchant_id", getIntent().getStringExtra("msgid"));
        HttpUtils.post(Constants.collect,MerchantmsgActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Log.e("msg", responseString);
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        showToast(object.getString("msg"));
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 取消收藏
     */
    private void cancelCollect() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", SPUtils.getStringData(MerchantmsgActivity.this, "token", ""));
        requestParams.put("merchant_id", getIntent().getStringExtra("msgid"));
        HttpUtils.post(Constants.cancelCollect, MerchantmsgActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Log.e("msg", responseString);
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        showToast(object.getString("msg"));
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //是否收藏
    private void isCollect() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", SPUtils.getStringData(MerchantmsgActivity.this, "token", ""));
        requestParams.put("merchant_id", getIntent().getStringExtra("msgid"));
        HttpUtils.post(Constants.cancelCollect, MerchantmsgActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Log.e("msg", responseString);
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        String is_collect = object.getJSONObject("data").getString("is_collect");
                        if ("Y".equals(is_collect)) {
                            flag = true;
                            iv_sc.setImageResource(R.mipmap.icon_collection_pre);
                        } else if ("N".equals(is_collect)) {
                            iv_sc.setImageResource(R.mipmap.icon_collection);
                            flag = false;
                        }
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }


}
