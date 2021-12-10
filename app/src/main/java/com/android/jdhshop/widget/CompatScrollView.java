package com.android.jdhshop.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ScrollView;

import androidx.annotation.RequiresApi;

public class CompatScrollView extends ScrollView {
    private ScrollViewListener scrollViewListener = null;

    public CompatScrollView(Context context) {
        super(context);
    }

    public CompatScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CompatScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CompatScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        Log.d("JuDuoHuiFragment", "onScrollChanged: " +x+","+y+","+oldx+","+oldy+".....");
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }
}
