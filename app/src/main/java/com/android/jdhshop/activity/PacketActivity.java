package com.android.jdhshop.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.widget.CircleImageView;
import com.android.jdhshop.ziyuan.ResourceManager;
import com.tencent.mm.opensdk.utils.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PacketActivity extends BaseActivity {
    @BindView(R.id.ad_top)
    ImageView ad_top;
    @BindView(R.id.ad_logo)
    CircleImageView ad_logo;
    @BindView(R.id.ad_title)
    ImageView ad_title;
    @BindView(R.id.pack_erweima)
    ImageView pack_erweima;
    @BindView(R.id.bottom_ad)
    ImageView bottom_ad;
    @BindView(R.id.erweima_lift)
    ImageView erweima_lift;
    @BindView(R.id.erweima_red)
    ImageView erweima_red;

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_ad);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void initData() {
        //本地资源文件获取图片
        BaseLogDZiYuan.LogDingZiYuan(ad_top, "ad_top.png");
        BaseLogDZiYuan.LogDingZiYuan(ad_logo, "app_icon.png");
        BaseLogDZiYuan.LogDingZiYuan(ad_title, "ad_title.png");
        BaseLogDZiYuan.LogDingZiYuan(pack_erweima, "erweima.png");
        BaseLogDZiYuan.LogDingZiYuan(bottom_ad, "ad_title_bottom.png");
        BaseLogDZiYuan.LogDingZiYuan(erweima_lift, "left_man.png");
        BaseLogDZiYuan.LogDingZiYuan(erweima_red, "right_man.png");
    }

    @Override
    protected void initListener() {
        findViewById(R.id.open_zfb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.eg.android.AlipayGphone");
                if (intent != null) {
                    //获取剪贴板管理器：
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("Label", "640452457");
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    startActivity(intent);
                } else {
                    T.showShort(PacketActivity.this, "未安装支付宝客户端");
                }
            }
        });
    }

    @OnClick(R.id.tv_left)
    public void onViewClicked() {
        finish();
    }
}
