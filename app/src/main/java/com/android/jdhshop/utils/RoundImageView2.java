package com.android.jdhshop.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundImageView2 extends ImageView {


    //圆角的半径，依次为左上角xy半径，右上角xy半径，右下角xy半径，左下角xy半径

    private float[] rids = {22.0f,22.0f,22.0f,22.0f,22.0f,22.0f,22.0f,22.0f,};


    public RoundImageView2(Context context) {
        super(context);
    }


    public RoundImageView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public RoundImageView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }




    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        int w = this.getWidth();
        int h = this.getHeight();
        /*向路径中添加圆角矩形。radii数组定义圆角矩形的四个圆角的x,y半径。radii长度必须为8*/
        path.addRoundRect(new RectF(0,0,w,h),rids, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
