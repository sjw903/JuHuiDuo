package com.android.jdhshop.merchantactivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.merchantadapter.ShoplistischeckAdapter;
import com.android.jdhshop.merchantbean.Merchantshoplistbean;
import com.android.jdhshop.merchantbean.Shopmsgbean;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ShopmsgActivity extends BaseActivity {
    private ShoplistischeckAdapter shoplistAdapter;
    private RecyclerView recyclerView;
    private ImageView bigimg, shop_share,shop_fanhui;
    private TextView tv_name, tv_xiaol, tv_pric, tv_button;
    private WebView tv_content;
    private LinearLayout ly_back;
    private View view;
    private String goods_name;
    Bitmap bitmap;
//    //属性sku
//    private List<Skubean> skubeans;
//    //是否选中列表
//    private List<Ilemskubean> itemsku = new ArrayList<>(  );

    //是否开启sku
//    String is_sku;
    //价格
    private String str_pric;
    //传入下个页面的参数
    public String nextpric, nextimg, nextname, goodids;
    public String strDkq = "";

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_shopmsg);
    }

    @Override
    protected void initData() {
        initview();
    }

    @Override
    protected void initListener() {

    }

    public void initview() {
        recyclerView = findViewById(R.id.shopmsg_recy);
        tv_name = findViewById(R.id.shopmsg_name);
        bigimg = findViewById(R.id.shopmsg_bigimg);
        tv_xiaol = findViewById(R.id.shopmsg_xiaoliang);
        tv_pric = findViewById(R.id.shopmsg_pric);
        tv_content = findViewById(R.id.shopmsg_msg);
        ly_back = findViewById(R.id.shopmsg_lyback);
        shop_share = findViewById(R.id.shop_share);
        shop_fanhui = findViewById(R.id.shop_fanhui);
        BaseLogDZiYuan.LogDingZiYuan(shop_share, "icon_dynamic_share.png");
        BaseLogDZiYuan.LogDingZiYuan(shop_fanhui, "icon_back.png");
        tv_button = findViewById(R.id.shopmsg_button);
        view = findViewById(R.id.shopmsg_view);
        WebSettings webSettings = tv_content.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js
//不支持屏幕缩放
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
//不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);

        //推荐商品
        shoplistAdapter = new ShoplistischeckAdapter(ShopmsgActivity.this, shoplist, 0);
        LinearLayoutManager gridLayoutManager2 = new LinearLayoutManager(ShopmsgActivity.this);
        gridLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(gridLayoutManager2);
        recyclerView.setAdapter(shoplistAdapter);
        shoplistAdapter.setsubClickListener(new ShoplistischeckAdapter.SubClickListener() {
            @Override
            public void OntopicClickListener(View v, String detail, int posit) {
                shoplistAdapter.setSelectindex(posit);
                shoplistAdapter.notifyDataSetChanged();
                getshopmsg(shoplist.get(posit).goods_id);
            }
        });
        findViewById(R.id.shopmsg_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = Constants.APP_IP + "/wap/Merchant/goods/merchant_id/" + shopmsgbean.merchant_id + "/goods_id/" + goodids + "/referrer_id/" + SPUtils.getStringData(ShopmsgActivity.this, "uid", "");
                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = webpage;
                msg.title = shopmsgbean.goods_name;
                msg.description = shopmsgbean.merchant_name;
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
        getshoplist();
        if (getIntent().getStringExtra("isyx").equals("is")) {
            recyclerView.setVisibility(View.GONE);

        }
        getshopmsg(getIntent().getStringExtra("shopid"));

        ly_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_button.setOnClickListener(new View.OnClickListener() { // 下单   选择sku属性
            @Override
            public void onClick(View view) {
//                if (is_sku.equals( "1" )) {
////                    showsfzrz();
//                }else{
                Intent intent = new Intent(ShopmsgActivity.this, ShopaddorderActivity.class);
                intent.putExtra("pric", nextpric);
                intent.putExtra("img", nextimg);
                intent.putExtra("name", nextname);
//                    intent.putExtra( "sku",jsonArray.toString() );
                intent.putExtra("goodid", goodids);
                intent.putExtra("type", "1");//1团购商品,2积分商品
                intent.putExtra("dkq", getIntent().getStringExtra("dkq"));
                startActivity(intent);
//                }
            }
        });
//        if (getIntent().getStringExtra( "isyx" ).equals( "is" )){
//
//        }
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


    //获取推荐菜品列表
    List<Merchantshoplistbean> shoplist = new ArrayList<>();

    public void getshoplist() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", SPUtils.getStringData(ShopmsgActivity.this, "token", ""));
        requestParams.put("merchant_id", getIntent().getStringExtra("goodid"));
        requestParams.put("p", "1");
        requestParams.put("per", "50");
        HttpUtils.post(Constants.getservicelist, ShopmsgActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Log.e("msg", responseString);
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        JSONArray array = object.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            shoplist.add(new Gson().fromJson(array.getJSONObject(i).toString(), Merchantshoplistbean.class));
                        }
                        shoplistAdapter.notifyDataSetChanged();
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }


    //对比属性sku
//    List<Shopmsgskulist> skulist = new ArrayList<>(  );
    Shopmsgbean shopmsgbean;

    public void getshopmsg(String goodid) {

        RequestParams requestParams = new RequestParams();
        requestParams.put("token", SPUtils.getStringData(ShopmsgActivity.this, "token", ""));
        requestParams.put("goods_id", goodid);
        HttpUtils.post(Constants.getServiceMsg,ShopmsgActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Log.e("msg", responseString);
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        JSONObject array = object.getJSONObject("data").getJSONObject("goodsMsg");
//                        for (int i = 0;i<array.length();i++){
                        shopmsgbean = new Gson().fromJson(array.toString(), Shopmsgbean.class);
//                            shoplist.add();
//                        }
//                        shoplistAdapter.notifyDataSetChanged();

                        str_pric = shopmsgbean.old_price;
                        nextpric = shopmsgbean.price;
                        nextimg = shopmsgbean.img;
                        nextname = shopmsgbean.goods_name;
                        goodids = shopmsgbean.goods_id;
                        Glide.with(ShopmsgActivity.this).load(Constants.APP_IP + shopmsgbean.img).into(bigimg);
                        Glide.with(ShopmsgActivity.this).load(Constants.APP_IP + shopmsgbean.img).asBitmap().into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                bitmap = resource;
                                bigimg.setImageBitmap(resource);
                            }
                        });
                        tv_name.setText(shopmsgbean.goods_name);
                        goods_name = shopmsgbean.goods_name;
                        tv_xiaol.setText("销量" + shopmsgbean.sales_volume);
                        tv_pric.setText("¥" + shopmsgbean.price);


                        tv_content.loadDataWithBaseURL(null, shopmsgbean.content == null ? "" : shopmsgbean.content.replaceAll("<img", "<img style='width:100%;height:auto'"), "text/html", "utf-8", null);
//
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }


//    SkulistAdapter skulistAdapter;
//    JSONArray jsonArray;
//    public void showsfzrz(){
//        final View popwindview = getLayoutInflater().inflate( R.layout.item_sfzrz,null );
//        final SmartPopupWindow popupWindow = SmartPopupWindow.Builder.build( ShopmsgActivity.this,popwindview )
//                .setAlpha( 0.4f )
//                .setOutsideTouchDismiss( false )
//                .setSize( view.getWidth()-50,view.getWidth()+400 )
//                .createPopupWindow();
//        popupWindow.showAtAnchorView( view, VerticalPosition.CENTER, HorizontalPosition.CENTER );
//        ImageView iv_close = popwindview.findViewById( R.id.itemsfzrz_ivcolse );
//        TextView tv_name = popwindview.findViewById( R.id.itemsfzrz_name );
//        TextView tv_pric = popwindview.findViewById( R.id.itemsfzrz_pric );
//        TextView tv_xiadan = popwindview.findViewById( R.id.itemsfzrz_xiadan );
//        RecyclerView skurecy = popwindview.findViewById( R.id.itemsfzrz_recy );
//
//        tv_name.setText( goods_name );
//        if (getIntent().getStringExtra( "type" ).equals( "1" )){
//            tv_pric.setText( "¥"+str_pric );
//        }else if (getIntent().getStringExtra( "type" ).equals( "2" )){
//            tv_pric.setText( "积分"+str_pric );
//        }
//
//        skulistAdapter = new SkulistAdapter( ShopmsgActivity.this,skubeans );
//        GridLayoutManager gridLayoutManager = new GridLayoutManager( ShopmsgActivity.this,1 );
//        gridLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
//        skurecy.setLayoutManager( gridLayoutManager );
//        skurecy.setAdapter( skulistAdapter );
//        itemsku.clear();
//        for (int i = 0; i < skubeans.size(); i++) {
//            itemsku.add( new Ilemskubean() );
//        }
//
//
//        skulistAdapter.setsubClickListener( new SkulistAdapter.SubClickListener() {
//            @Override
//            public void OntopicClickListener(View v, boolean bs, int detail, String posit) {
//                if (bs){
//                    itemsku.get( detail ).setName( skubeans.get( detail ).attribute_id );
//                    itemsku.get( detail ).setValues( posit );
//                    itemsku.get( detail ).ischeck = true;
//                }else{
//                    itemsku.get( detail ).setName( "" );
//                    itemsku.get( detail ).setValues( "" );
//                    itemsku.get( detail ).ischeck = false;
//                }
//                jsonArray = new JSONArray(  );
//                JSONObject obj;
//                for (int i = 0;i<itemsku.size();i++){
//                    if (itemsku.get( i ).ischeck){
//                        if (is_sku.equals( "1" )){//是否开启sku属性配对
//                            obj = new JSONObject(  );
//                            try {
//                                obj.put( "attribute_id",Integer.parseInt( itemsku.get( i ).getName() ) );
//                                obj.put( "value",itemsku.get( i ).getValues() );
//                                jsonArray.put( obj );
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
//
//                for (int i = 0;i<skulist.size();i++){
//                    if (jsonArray.toString().equals( skulist.get( i ).sku )){
//                        nextpric = skulist.get( i ).price;
//                        if (getIntent().getStringExtra( "type" ).equals( "1" )) {
//                            tv_pric.setText( "¥" + skulist.get( i ).price );
//                        }else if (getIntent().getStringExtra( "type" ).equals( "2" )){
//                            tv_pric.setText( "积分" + skulist.get( i ).price );
//                        }
//                    }
//                }
//
//
//
//            }
//        } );
//        tv_xiadan.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                boolean has = false;
//                for (int i =0 ; i< skubeans.size() && i < itemsku.size();i++){
//                    if (itemsku.get( i ).getName().equals( "" )){
//                        Toast.makeText( ShopmsgActivity.this,"请选择"+skubeans.get( i ).attribute_name,Toast.LENGTH_SHORT ).show();
//                        has = true;
//                        return;
//                    }
//                }
//                if (!has){  ///  属性选择没问题  下单
//                    popupWindow.dismiss();
////                    nextpric = tv_pric.getText().toString();
//                    Intent intent = new Intent( ShopmsgActivity.this,ShopaddorderActivity.class );
//                    intent.putExtra( "pric",nextpric );
//                    intent.putExtra( "img",nextimg );
//                    intent.putExtra( "name",nextname );
//                    intent.putExtra( "sku",jsonArray.toString() );
//                    intent.putExtra( "goodid",goodids );
//                    intent.putExtra( "type",getIntent().getStringExtra( "type" ) );//1团购商品,2积分商品
//                    startActivity( intent );
//                }
//            }
//        } );
//        iv_close.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                popupWindow.dismiss();
//            }
//        } );
//    }
}
