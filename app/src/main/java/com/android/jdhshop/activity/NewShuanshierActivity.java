package com.android.jdhshop.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class NewShuanshierActivity extends BaseActivity {
    private LinearLayout ly_back;
    private TextView tv_title;
    private ImageView imageView, fanhuihui;
    String tkl;
    private TextView tv_wc;

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_new_shuanshier);
    }

    @Override
    protected void initData() {
        ly_back = findViewById(R.id.back);
        tv_title = findViewById(R.id.title);
        imageView = findViewById(R.id.img);
        fanhuihui = findViewById(R.id.fanhuii);
        tv_wc = findViewById(R.id.wocao);
        BaseLogDZiYuan.LogDingZiYuan(fanhuihui, "icon_back.png");
        findViewById(R.id.btn_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(tkl);
                showToast("????????????");
            }
        });
        findViewById(R.id.btn_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> exParams = new HashMap<>();
                AlibcTrade.openByUrl(NewShuanshierActivity.this, "", url, null, new WebViewClient(), new WebChromeClient(), new AlibcShowParams(OpenType.Native)
                        , new AlibcTaokeParams(SPUtils.getStringData(getApplicationContext(), "tbk_pid", ""), "", ""), exParams, new AlibcTradeCallback() {
                            @Override
                            public void onTradeSuccess(AlibcTradeResult alibcTradeResult) {

                            }

                            @Override
                            public void onFailure(int i, String s) {

                            }
                        });
            }
        });
//        imageView.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ClipboardManager cmb = (ClipboardManager) getSystemService( Context.CLIPBOARD_SERVICE);
//                cmb.setText(tkl);
//                showToast( "????????????????????????????????????" );
//            }
//        } );
        ly_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_title.setText(getIntent().getStringExtra("title"));
        Glide.with(this).load(getIntent().getStringExtra("img"))
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        setBitmapToImg(resource);
                        imageView.setImageBitmap(resource);
                    }
                });
//        Glide.with( NewShuanshierActivity.this ).load( getIntent().getStringExtra( "img" ) ).into( imageView );
//        imageView.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ClipboardManager cmb = (ClipboardManager) getSystemService( Context.CLIPBOARD_SERVICE);
//                cmb.setText(tkl);
//                showToast( "????????????????????????????????????" );
//            }
//        } );
        getActivities();
    }

    @Override
    protected void initListener() {

    }


    //    private void setBitmapToImg(Bitmap resource) {
//        try {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            resource.compress(Bitmap.CompressFormat.PNG, 100, baos);
//
//            InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
//
//            //BitmapRegionDecoder newInstance(InputStream is, boolean isShareable)
//            //????????????BitmapRegionDecoder???isBm????????????????????????jpeg???png??????????????????????????????
//            // isShareable?????????true??????BitmapRegionDecoder?????????????????????????????????????????????
//            // ?????????false????????????????????????????????????????????????????????????????????????????????????true???????????????????????????????????????????????????????????????
//            // ??????????????????????????????????????????true??????????????????????????????????????????????????????????????????????????????????????????????????????
//            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(isBm, true);
//
//            final int imgWidth = decoder.getWidth();
//            final int imgHeight = decoder.getHeight();
//
//            BitmapFactory.Options opts = new BitmapFactory.Options();
//
//            //??????????????????????????????????????????
//            // ??????sum=0 ???????????????????????????3000px?????????????????? ????????????
//            // ??????sum>0 ????????????????????????????????????????????????????????????????????????3000?????????????????????
//            int sum = imgHeight/3000;
//
//            int redundant = imgHeight%3000;
//
//            List<Bitmap> bitmapList = new ArrayList<>();
//
//            //????????????????????? < 3000
//            if (sum == 0){
//                //????????????
//                bitmapList.add(resource);
//            }else {
//                //????????????????????????
//                for (int i = 0; i < sum; i++) {
//                    //???????????????mRect.set(left, top, right, bottom)?????????????????????
//                    //??????????????????????????????????????????4096
//                    mRect.set(0, i*3000, imgWidth, (i+1) * 3000);
//                    Bitmap bm = decoder.decodeRegion(mRect, opts);
//                    bitmapList.add(bm);
//                }
//
//                //??????????????????3000???????????????????????????
//                if (redundant > 0){
//                    mRect.set(0, sum*3000, imgWidth, imgHeight);
//                    Bitmap bm = decoder.decodeRegion(mRect, opts);
//                    bitmapList.add(bm);
//                }
//
//            }
//
//            Bitmap bigbitmap = Bitmap.createBitmap(imgWidth, imgHeight, Bitmap.Config.ARGB_8888);
//            Canvas bigcanvas = new Canvas(bigbitmap);
//
//            Paint paint = new Paint();
//            int iHeight = 0;
//
//            //????????????bitmap????????????????????????bitmap
//            for (int i = 0; i < bitmapList.size(); i++) {
//                Bitmap bmp = bitmapList.get(i);
//                bigcanvas.drawBitmap(bmp, 0, iHeight, paint);
//                iHeight += bmp.getHeight();
//
//                bmp.recycle();
//                bmp = null;
//            }
//
//            imageView.setImageBitmap(bigbitmap);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    private String url = "";

    private void getActivities() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("activityId", getIntent().getStringExtra("hdid"));
        requestParams.put("promotion_scene_id", getIntent().getStringExtra("hdid"));
        requestParams.put("relationId", getIntent().getStringExtra("tbuid"));
        requestParams.put("text", getIntent().getStringExtra("text"));
        requestParams.put("is_tkl", "Y");
        HttpUtils.post(Constants.APP_IP + "/api/Tbk/getActivityLink", NewShuanshierActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(throwable.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject object = new JSONObject(responseString);
                    if (object.getString("code").equals("0")) {
                        JSONObject jsonObject = object.getJSONObject("data");
                        url = jsonObject.getString("url");
                        tkl = "0???" + getIntent().getStringExtra("text") + "????????????(" + jsonObject.getString("tkl") + ")????????????/";
                        tv_wc.setText(tkl);
                    } else if (object.getString("msg").equals("????????????")) {
                        showToast("???????????????????????????????????????");
                        finish();
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
