package com.android.jdhshop.juduohui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.jdhshop.R;
import com.android.jdhshop.juduohui.unt.ZoomImageView;
import com.bumptech.glide.Glide;

public class ViewBigImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_big_image);
        ZoomImageView z = findViewById(R.id.big_img_bbb);
        Intent i = getIntent();
        String url = i.getStringExtra("url");
        Glide.with(this).load(url).asBitmap().into(z);
    }
}