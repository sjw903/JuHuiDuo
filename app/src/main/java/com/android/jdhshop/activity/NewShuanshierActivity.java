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
                showToast("复制成功");
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
//                showToast( "复制成功，快去邀请好友吧" );
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
//                showToast( "复制成功，快去邀请好友吧" );
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
//            //用于创建BitmapRegionDecoder，isBm表示输入流，只有jpeg和png图片才支持这种方式，
//            // isShareable如果为true，那BitmapRegionDecoder会对输入流保持一个表面的引用，
//            // 如果为false，那么它将会创建一个输入流的复制，并且一直使用它。即使为true，程序也有可能会创建一个输入流的深度复制。
//            // 如果图片是逐步解码的，那么为true会降低图片的解码速度。如果路径下的图片不是支持的格式，那就会抛出异常
//            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(isBm, true);
//
//            final int imgWidth = decoder.getWidth();
//            final int imgHeight = decoder.getHeight();
//
//            BitmapFactory.Options opts = new BitmapFactory.Options();
//
//            //计算图片要被切分成几个整块，
//            // 如果sum=0 说明图片的长度不足3000px，不进行切分 直接添加
//            // 如果sum>0 先添加整图，再添加多余的部分，否则多余的部分不足3000时底部会有空白
//            int sum = imgHeight/3000;
//
//            int redundant = imgHeight%3000;
//
//            List<Bitmap> bitmapList = new ArrayList<>();
//
//            //说明图片的长度 < 3000
//            if (sum == 0){
//                //直接加载
//                bitmapList.add(resource);
//            }else {
//                //说明需要切分图片
//                for (int i = 0; i < sum; i++) {
//                    //需要注意：mRect.set(left, top, right, bottom)的第四个参数，
//                    //也就是图片的高不能大于这里的4096
//                    mRect.set(0, i*3000, imgWidth, (i+1) * 3000);
//                    Bitmap bm = decoder.decodeRegion(mRect, opts);
//                    bitmapList.add(bm);
//                }
//
//                //将多余的不足3000的部分作为尾部拼接
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
//            //将之前的bitmap取出来拼接成一个bitmap
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
                        tkl = "0【" + getIntent().getStringExtra("text") + "复制口令(" + jsonObject.getString("tkl") + ")立即查看/";
                        tv_wc.setText(tkl);
                    } else if (object.getString("msg").equals("参数缺失")) {
                        showToast("请绑定淘宝渠道再参加此活动");
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
