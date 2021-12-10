package com.android.jdhshop.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.chrisbanes.photoview.PhotoView;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.utils.ImgUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskBigImgActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView headerTitle;
    @BindView(R.id.tv_left)
    TextView headerLeftImg;
    @BindView(R.id.big_img_vp)
    ViewPager bigImgVp;
    @BindView(R.id.btn_save)
    Button btn_save;
    //    @BindView(R.id.header_right_tv)
//    TextView headerRightTv;
    private int position;
    private ArrayList<String> paths;
    private List<Bitmap> listBit=new ArrayList<>();
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_task_big_img);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        paths = intent.getStringArrayListExtra("paths");
        String title = intent.getStringExtra("title");
        headerTitle.setText(title);
        headerLeftImg.setVisibility(View.VISIBLE);
        headerTitle.setVisibility(View.VISIBLE);
        headerTitle.setText(position + 1 + "/" + paths.size());
//        tvRight.setText("保存");
//        tvRight.setVisibility(View.VISIBLE);
        initView();
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSuccess= ImgUtils.saveImageToGallery(TaskBigImgActivity.this, listBit.get(position));
                if(isSuccess)
                T.showShort(TaskBigImgActivity.this,"保存成功");
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    private void initView() {
        bigImgVp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return paths == null ? 0 : paths.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                View adView = LayoutInflater.from(TaskBigImgActivity.this).inflate(R.layout.item_big_img, null);
                final PhotoView icon = adView.findViewById(R.id.flaw_img);
                icon.setBackgroundColor(getResources().getColor(R.color.black));
                String item = paths.get(position);
                if (!item.startsWith("http")) {
                    item = item.replace("\"", "").replace("\\", "");
                    Glide.with(TaskBigImgActivity.this).load(Constants.APP_IP + item).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            if(listBit.size()<position){
                                listBit.add(bitmap);
                            } else{
                                listBit.add(position,bitmap);
                            }
                            icon.setImageBitmap(bitmap);
                        }
                    });
//                    Glide.with(TaskBigImgActivity.this).load(Constants.APP_IP + item).dontAnimate().into(icon);
                } else {
                    Glide.with(TaskBigImgActivity.this).load(item).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            if(listBit.size()<position){
                                listBit.add(bitmap);
                            } else{
                                listBit.add(position,bitmap);
                            }
                            icon.setImageBitmap(bitmap);
                        }
                    });
//                    Glide.with(TaskBigImgActivity.this).load(item).dontAnimate().into(icon);
                }
//                Glide.with(TaskBigImgActivity.this)
//                        .load(paths.get(position))
//                        .into(icon);
                container.addView(adView);
                return adView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

        bigImgVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position1) {
                position=position1;
                headerTitle.setText(position1 + 1 + "/" + paths.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

        bigImgVp.setCurrentItem(position, true);
    }

    @OnClick(R.id.tv_left)
    public void onClick() {
        finish();
    }
}