package com.android.jdhshop.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jdhshop.R;

import java.util.List;

public class ScrollBanner extends LinearLayout {
    private TextView mBannerTV1;
    private TextView mBannerTV2;
    private Handler handler;
    private boolean isShow=false;
    private int startY1, endY1, startY2, endY2;
    private Runnable runnable;
    private List<String> list;
    private int position = 0;
    private int offsetY = 100;
    private int currentPosition=0;
    public ScrollBanner(Context context) {
        this(context, null);
    }

    public ScrollBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public ScrollBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.view_scroll_banner, this);
        mBannerTV1 =view.findViewById(R.id.tv_banner1);
        mBannerTV2 = view.findViewById(R.id.tv_banner2);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                isShow = !isShow;
                if (position == list.size()-1) {
                    position = 0;
                }
                currentPosition=position;
                if (isShow) {
                    mBannerTV1.setText(list.get(position++));
                    mBannerTV2.setText(list.get(position));
                } else {
                    mBannerTV2.setText(list.get(position++));
                    mBannerTV1.setText(list.get(position));
                }
                startY1 = isShow ? 0 : offsetY;
                endY1 = isShow ? -offsetY : 0;
                ObjectAnimator.ofFloat(mBannerTV1, "translationY", startY1, endY1).setDuration(300).start();
                startY2 = isShow ? offsetY : 0;
                endY2 = isShow ? 0 : -offsetY;
                ObjectAnimator.ofFloat(mBannerTV2, "translationY", startY2, endY2).setDuration(300).start();
                handler.postDelayed(runnable, 3000);
            }
        };
    }
    public List<String> getList() {
        return list;
    }
    public void setList(List<String> list) {
        this.list = list;
    }
    public void startScroll() {
        handler.post(runnable);
    }
    public void stopScroll() {
        handler.removeCallbacks(runnable);
    }
}