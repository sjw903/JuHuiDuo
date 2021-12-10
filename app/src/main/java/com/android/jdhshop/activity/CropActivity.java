package com.android.jdhshop.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.jdhshop.ziyuan.ResourceManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.edmodo.cropper.CropImageView;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.CommonUtils;
import com.tencent.mm.opensdk.utils.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CropActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    Bitmap mybitmap;
    @BindView(R.id.CropImageView)
    CropImageView CropImageView;
    private String url;
    @BindView(R.id.btn_ok)
    Button btn_ok;
    @Override
    protected void initUI() {
        setContentView(R.layout.ac_crop);
        ButterKnife.bind(this);
        initView();
        url = Environment.getExternalStorageDirectory().getPath() + "/crop/" + System.currentTimeMillis() + ".jpg";
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.WHITE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        CommonUtils.setMeizuStatusBarDarkIcon(this, true);
        Glide.with(this).load(getIntent().getStringExtra("url")).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                CropImageView.setImageBitmap(bitmap);
            }
        });
        tvTitle.setText("头像裁剪");
        tvLeft.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.tv_left, R.id.btn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.btn_ok:
                crop();
                break;
        }
    }

    private void crop() {
        mybitmap = CropImageView.getCroppedImage();
        File file=saveImageToGallery(this,mybitmap);
        Intent intent=new Intent();
        if(file!=null){
            intent.putExtra("url",file.getPath());
        }else{
            intent.putExtra("url",getIntent().getStringExtra("url"));
        }
        setResult(RESULT_OK,intent);
        finish();
    }
    public static File saveImageToGallery(Context context, Bitmap bmp) {
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "hkx";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return file;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
