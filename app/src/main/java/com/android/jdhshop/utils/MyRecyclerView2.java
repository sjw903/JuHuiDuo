package com.android.jdhshop.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.android.jdhshop.common.LogUtils;

public class MyRecyclerView2 extends RecyclerView {
    public MyRecyclerView2(Context context) {
        super(context);
    }

    public MyRecyclerView2(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public MyRecyclerView2(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    private int startX, startY;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //解决recyclerView和viewPager的滑动影响
        //当滑动recyclerView时，告知父控件不要拦截事件，交给子view处理
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) ev.getX();
                int endY = (int) ev.getY();
                int disX = Math.abs(endX - startX);
                int disY = Math.abs(endY - startY);
                //LogUtils.debugInfo("DispatchTouchEvent disX="+ disX + "; disY" + disY + "; canScrollHorizontally(startX - endX) = " + canScrollHorizontally(startX - endX) + "; canScrollVertically(startY - endY)" + canScrollVertically(startY - endY));
                if (disX > disY) {
                    //如果是纵向滑动，告知父布局不进行时间拦截，交由子布局消费，　requestDisallowInterceptTouchEvent(true)
                    getParent().requestDisallowInterceptTouchEvent(canScrollHorizontally(startX - endX));
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(canScrollVertically(startX - endX));
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
       //getParent().requestDisallowInterceptTouchEvent(true);

        return super.dispatchTouchEvent(ev);
    }
}
