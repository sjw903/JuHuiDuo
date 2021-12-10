package com.android.jdhshop.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
//http://zhidao.baidu.com/link?url=EF1oaSxgxfsnpML7WN6FZ0JOQaRdTt6vQodWQ2iHy_I_ysY-9sRo8zd_GJEbA0ixjTaboPIUoRrLLrVR3osy1MAFXeVdrr9ybdGlN6oyPt3

/**
 * ListView和GridView就都是ScrollView型的控件，两ScrollView型的控件是不能相互嵌套
 * 因为嵌套后，两个ScrollView型控件的滑动效果就丧失了，同时被嵌套控件的高度也被限定为一行的高度。
 * Created by niumenglin on 16/9/12.
 */
public class NoScrollGridView extends GridView {

    public NoScrollGridView(Context context) {
        super(context);
    }

    public NoScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 重写其onMeasure()方法
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}