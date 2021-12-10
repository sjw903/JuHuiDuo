package com.android.jdhshop.juduohui.unt;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
//图片手势缩放，点击放大
public class ZoomImageView extends android.support.v7.widget.AppCompatImageView implements OnGlobalLayoutListener, OnScaleGestureListener, OnTouchListener {

    private boolean mOnce = false;

    private float mInitScale;
    //初始化的缩放量
    private float mMidScale;
    //最大缩放量
    private float mMaxScale;
    //中间缩放量
    private Matrix mScaleMatrix = null;

    private ScaleGestureDetector mScaleGestureDetector = null;
    //缩放手势监听

    private int mLastPointerCount;
    //触摸点发生移动时的触摸点个数
    private float mLastX;
    private float mLastY;
    //记录移动之前按下去的那个坐标点

    private int mTouchSlop;
    //开始移动的滑动距离
    private boolean isCanDrag;
    //是否可以移动
    private boolean isCheckLeftAndRight;//是否需要考虑left和right出现白边
    private boolean isCheckTopAndBottom;//是否需要考虑top和boottom出现白边

    private GestureDetector mGestureDetector = null;
    //手势监听，如果正在缩放中就不向下执行,防止多次双击
    private boolean isAutoScale;
    /**
     * Matrix的对图像的处理
     * Translate 平移变换
     * Rotate 旋转变换
     * Scale 缩放变换
     * Skew 错切变换
     */


    public ZoomImageView(Context context) {
        this(context , null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mScaleMatrix = new Matrix();
        setScaleType(ScaleType.MATRIX);//设置缩放类型
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(this);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mGestureDetector = new GestureDetector(
                context,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {

                        if(isAutoScale == true) {
                            return true;
                        }
                        //缩放的中心点
                        float x = e.getX();
                        float y = e.getY();

                        //如果当前缩放值小于这个临界值 则进行放大
                        if(getCurrentScale() < mMidScale) {
                            postDelayed(new AutoScaleRunnable(mMidScale, x, y) , 16);
                            isAutoScale = true;
                        }
                        else { //如果当前缩放值大于这个临界值 则进行缩小操作 缩小到mInitScale
                            postDelayed(new AutoScaleRunnable(mInitScale, x, y) , 16);
                            isAutoScale = true;
                        }

                        return true;
                    }
                });
    }


    /**
     * ImageView
     *图片初始化其大小，把图片定位到屏幕中央，并进行初始化缩放适应屏幕
     */
    @Override
    public void onGlobalLayout() {

        if(mOnce == false) {
            //获取imageview宽高
            int width = getWidth();
            int height = getHeight();

            Drawable drawable = getDrawable();
            if(drawable == null) {
                return;
            }
            int drawableWidth = drawable.getIntrinsicWidth();
            int drawableHeight = drawable.getIntrinsicHeight();


            float scale = 1.0f;

            // 如果图片的宽或者高大于屏幕，则缩放至屏幕的宽或者高
            if(drawableWidth > width && drawableHeight < height) {
                scale = width * 1.0f / drawableWidth;
            }
            if(drawableWidth < width && drawableHeight > height) {
                scale = height * 1.0f / drawableHeight;
            }

            // 如果宽和高都大于屏幕，则让其按按比例适应屏幕大小
            if(drawableWidth > width && drawableHeight > height) {
                scale = Math.min(width * 1.0f / drawableWidth , height * 1.0f / drawableHeight);
            }
            if(drawableWidth < width && drawableHeight < height) {
                scale = Math.min(width * 1.0f / drawableWidth , height * 1.0f / drawableHeight);
            }

            // 图片移动至屏幕中心
            mInitScale = scale;
            mMidScale = mInitScale * 2;
            mMaxScale = mInitScale * 4;
            int dx = width / 2 - drawableWidth / 2;
            int dy = height / 2 - drawableHeight / 2;
            mScaleMatrix.postTranslate(dx, dy);
            mScaleMatrix.postScale(mInitScale , mInitScale , width / 2 , height / 2);
            setImageMatrix(mScaleMatrix);
            mOnce = true;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }


    @SuppressWarnings("deprecation")
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    /**
     * 获取当前缩放比例
     */
    public float getCurrentScale() {
        //Matrix为一个3*3的矩阵，一共9个值,复制到这个数组当中
        float[] values = new float[9];
        mScaleMatrix.getValues(values);
        return values[Matrix.MSCALE_X];//取出图片宽度的缩放比例
    }



    /**
     *处理图片缩放
     */
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scale = getCurrentScale();
        //当前相对于初始尺寸的缩放（之前matrix中获得）
        float scaleFactor = detector.getScaleFactor();
        //这个时刻缩放的/当前缩放尺度 （现在手势获取）
        if(getDrawable() == null) {
            return true;
        }

        //按比例放大缩小
        if( (scale < mMaxScale && scaleFactor > 1.0f) || (scale > mInitScale && scaleFactor < 1.0f) ) {
            if(scale * scaleFactor < mInitScale) {
                scaleFactor = mInitScale / scale;
            }
            if(scale * scaleFactor > mMaxScale) {
                scaleFactor = mMaxScale / scale;
            }
        }

        //缩放中心是屏幕中心点
        mScaleMatrix.postScale(scaleFactor , scaleFactor , detector.getFocusX() , detector.getFocusY());
        checkBorderAndCenterWhenScale();
        setImageMatrix(mScaleMatrix);
        return true;
    }


    /**
     * 获得图片放大缩小以后的宽和高，以及l,r,t,b
     */
    private RectF getMatrixRectF() {
        Matrix matrix = mScaleMatrix;
        RectF rectF = new RectF();
        Drawable drawable = getDrawable();
        if(drawable != null) {
            rectF.set(0 , 0 , drawable.getIntrinsicWidth() , drawable.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }
        return rectF;
    }



    /**
     * 在缩放时，解决上下左右留白的情况
     */
    private void checkBorderAndCenterWhenScale() {

        RectF rect = getMatrixRectF();
        float deltaX = 0.0f;
        float deltaY = 0.0f;

        int width = getWidth();
        int height = getHeight();

        if(rect.width() >= width) {
            if(rect.left > 0) {
                deltaX = -rect.left;
            }
            if(rect.right < width) {
                deltaX = width - rect.right;
            }
        }
        if(rect.height() >= height) {
            if(rect.top > 0) {
                deltaY = -rect.top;
            }
            if(rect.bottom < height) {
                deltaY = height - rect.bottom;
            }
        }

        if(rect.width() < width) {
            deltaX = width / 2f - rect.right + rect.width() / 2f;
        }
        if(rect.height() < height) {
            deltaY = height / 2f - rect.bottom + rect.height() / 2f;
        }

        mScaleMatrix.postTranslate(deltaX, deltaY);
    }

    //缩放开始一定要返回true该detector是否处理后继的缩放事件。返回false时，不会执行onScale()
    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    //缩放结束时
    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        // TODO Auto-generated method stub
    }

    /**
     *处理现图片放大后移动查看
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //双击事件进行关联
        if(mGestureDetector.onTouchEvent(event)) {
            return true;
        }
        mScaleGestureDetector.onTouchEvent(event);
        //缩放的中心点
        float x = 0;
        float y = 0;

        int pointerCount = event.getPointerCount();
        //可能出现多手指触摸的情况 ACTION_DOWN事件只能执行一次所以多点触控不能在down事件里面处理
        for(int i = 0; i < pointerCount ; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }
        //取平均值，得到的就是多点触控后产生的那个点的坐标
        x /= pointerCount;
        y /= pointerCount;

        if(mLastPointerCount != pointerCount) {
            isCanDrag = false;
            mLastX = x;
            mLastY = y;
        }
        mLastPointerCount = pointerCount;
        RectF rectF = getMatrixRectF();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(rectF.width() > getWidth() + 0.01 || rectF.height() > getHeight() + 0.01) {
                    if(getParent() instanceof ViewPager) {

                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(rectF.width() > getWidth() + 0.01 || rectF.height() > getHeight() + 0.01) {
                    if(getParent() instanceof ViewPager) {

                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                float deltaX = x - mLastX;
                float deltaY = y - mLastY;
                if(isCanDrag == false) {
                    isCanDrag = isMoveAction(deltaX , deltaY);
                }
                if(isCanDrag == true) {
                    if(getDrawable() != null) {
                        isCheckLeftAndRight = true;
                        isCheckTopAndBottom = true;
                        if(rectF.width() < getWidth()) {
                            isCheckLeftAndRight = false;
                            deltaX = 0;
                        }
                        if(rectF.height() < getHeight()) {
                            isCheckTopAndBottom = false;
                            deltaY = 0;
                        }
                        mScaleMatrix.postTranslate(deltaX, deltaY);
                        checkBorderWhenTranslate();
                        setImageMatrix(mScaleMatrix);
                    }
                }
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                mLastPointerCount = 0;
                break;
            case MotionEvent.ACTION_CANCEL:
                mLastPointerCount = 0;
                break;
        }
        return true;
    }

    /**
     * 放大移动的过程中解决上下左右留白的情况
     */
    private void checkBorderWhenTranslate() {
        RectF rectF = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;
        //可以上下拖动且距离屏幕上方留白 根据Android系统坐标系往上移动的值要取负值
        int width = getWidth();
        int height = getHeight();

        if(rectF.top > 0 && isCheckTopAndBottom) {
            deltaY = -rectF.top;
        }
        if(rectF.bottom < height && isCheckTopAndBottom) {
            deltaY = height - rectF.bottom;
        }
        if(rectF.left > 0 && isCheckLeftAndRight) {
            deltaX = -rectF.left;
        }
        if(rectF.right < width && isCheckLeftAndRight) {
            deltaX = width - rectF.right;
        }
        mScaleMatrix.postTranslate(deltaX, deltaY);
    }
    /**
     *判断是否可以拖动
     */
    private boolean isMoveAction(float deltaX, float deltaY) {
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY) > mTouchSlop;
    }


    /**
     * View.postDelay()方法延时执行双击放大缩小 在主线程中运行 没隔16ms给用户产生过渡的效果的
     */
    private class AutoScaleRunnable implements Runnable {
        //缩放目标值
        private float mTargetScale;
        //缩放中心点
        private float x;
        private float y;

        private final float BIGGER = 1.07f;
        private final float SMALL = 0.93f;

        //可能是BIGGER可能是SMALLER
        private float tmpScale;

        //构造传入缩放目标值,缩放的中心点
        public AutoScaleRunnable(float mTargetScale, float x, float y) {
            this.mTargetScale = mTargetScale;
            this.x = x;
            this.y = y;

            if(getCurrentScale() < mTargetScale) {
                tmpScale = BIGGER; //双击放大
            }
            else if(getCurrentScale() > mTargetScale) {
                tmpScale = SMALL; ///双击缩小
            }
        }

        @Override
        public void run() {
            //执行缩放
            mScaleMatrix.postScale(tmpScale , tmpScale , x , y);
            checkBorderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);

            float currentScale = getCurrentScale();
            //如果当前正在放大操作并且当前的放大尺度小于缩放的目标值,或者正在缩小并且缩小的尺度大于目标值
            //则再次延时16ms递归调用直到缩放到目标值
            if( (tmpScale > 1.0f && currentScale < mTargetScale) || (tmpScale < 1.0f && currentScale > mTargetScale) ) {
                postDelayed(this , 16); //??this????????
            }
            else {
                //这里我们mTrgetScale / currentScale 用目标缩放尺寸除以当前的缩放尺寸
                //得到缩放比，重新执行缩放到
                //mMidScale或者mInitScale
                float scale = mTargetScale / currentScale;
                mScaleMatrix.postScale(scale, scale, x, y);
                checkBorderAndCenterWhenScale();
                setImageMatrix(mScaleMatrix);
                isAutoScale = false;
            }
        }
    }
}