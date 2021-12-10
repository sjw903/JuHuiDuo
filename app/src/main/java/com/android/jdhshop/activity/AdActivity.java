package com.android.jdhshop.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.jdhshop.MainActivity;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.utils.DownloadUtil;
import com.bumptech.glide.Glide;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.alibaba.baichuan.trade.common.AlibcMiniTradeCommon.context;

/**
 * 进入应用5秒跳转到首页
 * 启动页
 */
public class AdActivity extends BaseActivity {
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.txt_time)
    TextView txtTime;
    @BindView(R.id.banner_image)
    MZBannerView banner;
    //    @BindView(R.id.last)
//    ImageView last;
//    @BindView(R.id.next)
//    ImageView next;
    int time = 5;
    int cur = 0;
    JSONArray array;
    private List<String> stringList = new ArrayList<>();
    private int p = 0;//当前进度
    private ProgressBar progressBar;
    private TextView txt_cancle_tishi, txt_ok_tishi,jindu_text;
    private MyHandler myHandler = new MyHandler();//新写的Handler类
    private RelativeLayout llllll;
    private LinearLayout llllll2;
    private LinearLayout llllll3;
    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int code = msg.what;//接受处理码
            switch (code) {
                case 1:
                    progressBar.setProgress(p);
                    jindu_text.setText(p+"%");
                    break;
            }
        }
    }
    @Override
    protected void initUI() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_app_ad);
        ButterKnife.bind(this);

        progressBar = findViewById(R.id.pb_progressbar1);

        llllll = findViewById(R.id.lllll);
        llllll2 = findViewById(R.id.lllll2);
        llllll3 = findViewById(R.id.lllll3);
        jindu_text = findViewById(R.id.jindu_text);
        txt_cancle_tishi = findViewById(R.id.txt_cancle_tishi);
        txt_cancle_tishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txt_ok_tishi = findViewById(R.id.txt_ok_tishi);

        txt_ok_tishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                downloadZipp();
                LogUtils.d(TAG, "onClick: 开始触发版本检测");


                llllll.setVisibility(View.GONE);
                llllll2.setVisibility(View.VISIBLE);
                llllll3.setVisibility(View.GONE);
            }
        });

//        last.setVisibility(View.GONE);
//        next.setVisibility(View.GONE);

        banner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int i) {
                cur = i;
                try {
                    String type = array.getJSONObject(cur).getString("type");
                    String url = array.getJSONObject(cur).getString("href");
                    String value = array.getJSONObject(cur).getString("type_value");
                    if (!"2".equals(type) && !"".equals(url)) {
                        flag = false;
                        smokeHandle.removeCallbacksAndMessages(null);
                        LogUtils.d(TAG, "onPageClick: flag = false,clear");
                    }

                    if ("2".equals(type)) {
                        Intent intent = getPackageManager().getLaunchIntentForPackage("com.taobao.taobao");
                        if (intent != null) {
                            //获取剪贴板管理器：
                            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            // 创建普通字符型ClipData
                            ClipData mClipData = ClipData.newPlainText("Label", value);
                            // 将ClipData内容放到系统剪贴板里。
                            cm.setPrimaryClip(mClipData);
                            startActivity(intent);
                            return;
                        } else {
                            T.showShort(AdActivity.this, "未安装淘宝客户端");
                        }
                    } else if ("6".equals(type)) {
                        Intent intent = new Intent(AdActivity.this, WebViewActivity2.class);
                        intent.putExtra("title", "年货节");
                        intent.putExtra("url", "");
                        startActivityForResult(intent, 9);
                        return;
                    } else if ("7".equals(type) || "8".equals(type)) {
                    } else if ("18".equals(type)) {
                        IWXAPI api = WXAPIFactory.createWXAPI(context, Constants.WX_APP_ID);// 填对应开发平台移动应用AppId
                        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                        req.userName ="gh_2bdc6decf01c"; // 填小程序原始id（官方实例请填写自己的小程序id）
                        req.path = url+""; //拉起小程序页面的可带参路径，不填默认拉起小程序首页
                        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 正式版
                        api.sendReq(req);
                        return;
                    }else if("4".equals(type)){
                        Bundle bundle=new Bundle();
                        bundle.putString("channel_type","1");
                        bundle.putString("keyword","详情");
                        bundle.putString("href",url);
//                        openActivity(PddXiaoKaPianActivity.class,bundle);
                        openActivityForResult(PddXiaoKaPianActivity.class,bundle,991);
                        return;
                    }else if("3".equals(type)){
                        Bundle bundle=new Bundle();
                       // bundle.putString("id",images3.get(i).getId());
                        bundle.putString("keyword","详情");
                        bundle.putString("href", url);
                        openActivity(PddFangYiZhuanQuActivity.class,bundle);

                        return;
                    }
                    if ("".equals(url)) {
                        return;
                    }
                    LogUtils.d(TAG, "onPageClick: " + type);
                    if ("1".equals(type) && url != null) {
                        Intent intent = new Intent(AdActivity.this, WebViewActivityWithNotIntent.class);
                        intent.putExtra("title", "详情");
                        intent.putExtra("url", url);
                        //startActivity(intent);
                        startActivityForResult(intent, 9);
                        return;
                    }
                    if (url != null) {
                        Intent intent = new Intent(AdActivity.this, WebViewActivityNotOpenDefaultWeb.class);
                        intent.putExtra("title", "详情");
                        intent.putExtra("url", url);
                        //startActivity(intent);
                        startActivityForResult(intent, 9);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        banner.addPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                cur = position;
                if (position == stringList.size() - 1) {
//                    findViewById(R.id.txt_detail).setVisibility(View.VISIBLE);
                } else {
//                    findViewById(R.id.txt_detail).setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        try {
            JSONObject object = new JSONObject(SPUtils.getStringData(this, "ade", ""));
            if (object.getInt("code") == 0) {
                array = object.getJSONObject("data").getJSONArray("list");

                for (int i = 0; i < array.length(); i++) {
                    // 如果 类型TYPE = 4，href = '' 则不显示
                    if (array.getJSONObject(i).getString("type").equals("4") && array.getJSONObject(i).getString("href").equals("")) continue;

                    String image_url = "";

                    if (array.getJSONObject(i).getString("img").startsWith("http")) {
                        image_url = array.getJSONObject(i).getString("img");
                    }
                    else
                    {
                        image_url = Constants.APP_IP + array.getJSONObject(i).getString("img");
                    }

                    stringList.add(image_url);
                    // 只保存第一张开屏图作为默认开屏图
                    if (i==0) {
                        DownloadUtil.get().download(image_url, getExternalFilesDir("tmp_banner").toString(), "splash.png", new DownloadUtil.OnDownloadListener() {
                            @Override
                            public void onDownloadSuccess(File file) {
                                // Log.d(TAG, "onDownloadSuccess: 开屏文件下载成功");
                                SPUtils.saveStringData(getComeActivity(), "splash_tmp_file", file.getAbsolutePath());
                            }

                            @Override
                            public void onDownloading(int progress) {

                            }

                            @Override
                            public void onDownloadFailed(Exception e) {

                            }
                        });
                    }
                }

                banner.setPages(stringList, new MZHolderCreator<BannerViewHolder>() {
                    @Override
                    public BannerViewHolder createViewHolder() {
                        return new BannerViewHolder();
                    }
                });
                banner.start();
//                if (array.length() > 1) {
//                    next.setVisibility(View.VISIBLE);
//                }
//                Glide.with(this).load(Constants.APP_IP + array.getJSONObject(0).getString("img")).into(img);
            }
        } catch (JSONException e) {
            Intent intent = new Intent(AdActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void initData() {
        txtTime.setText(time + "s");
        smokeHandle.sendEmptyMessageDelayed(0, 1000);
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
            Glide.with(context).load(data).into(mImageView);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flag = false;
    }

    boolean isClickAd = false;
    private boolean flag = true;
    private Handler smokeHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            time--;

            if (time == 0 && flag && !isClickAd) {
                txtTime.setText(time + "s");
                Intent intent = new Intent(AdActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
//                boolean b = fileIsExists(Constants.ZIYUAN_PATH+"icon_life_g.png");
//                if(b){
//
//                }else{
//                    IsWIFI();
//                    //showToast("资源缺失,需要重新下载.");
//                }

            }
            if (flag) {
                txtTime.setText(time + "s");
                smokeHandle.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };


    @Override
    protected void initListener() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        LogUtils.d(TAG, "onActivityResult: " + requestCode + "____" + resultCode + "____" + data);
        if (requestCode == 9 || requestCode == 991) {
            openActivity(MainActivity.class);
            LogUtils.d(TAG, "onActivityResult: 从webview返回");
            flag = true;
            //smokeHandle.sendEmptyMessageDelayed(0, 1000);

            finish();
        }
    }

    boolean is_click = false;
    @OnClick({R.id.txt_detail, R.id.ll_skip})
    public void onViewClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        switch (view.getId()) {
            case R.id.txt_detail:
                isClickAd = true;
                startActivity(intent);
                finish();
                break;
            case R.id.ll_skip:
                isClickAd = true;
                if (!is_click) {
                    is_click = true;
                    Intent intent1 = new Intent(this, MainActivity.class);
                    startActivity(intent1);
                    finish();
                }
                break;
        }
    }
}
