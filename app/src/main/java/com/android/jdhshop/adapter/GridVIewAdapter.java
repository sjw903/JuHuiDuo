package com.android.jdhshop.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.android.jdhshop.R;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.utils.BitmapUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * 开发者：陈飞
 * 时间:2018/7/12 18:41
 * 说明：服务gridView适配器
 */
public class GridVIewAdapter extends CommonAdapter<String> {
    private int type=0;
    public GridVIewAdapter(Context context, int layoutId, List<String> datas,int type) {
        super(context, layoutId, datas);
        this.type=type;
    }

    @Override
    protected void convert(final ViewHolder viewHolder, String item, int position) {
        if (item!=null){
            item = item.replace("\\/","/");
            item = item.replace("\"", "").replace("\\", "");
        }

        if(!item.startsWith("http")) {

            if(type==0){
                Glide.with(mContext).load(Constants.APP_IP + item).dontAnimate().into((ImageView) viewHolder.getView(R.id.service_icon));
            }else{
                Glide.with(mContext).load(Constants.APP_IP+item).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        setImageViewMathParent(mContext,(ImageView)viewHolder.getView(R.id.service_icon),bitmap);
                    }
                });
            }
        }else{
            if(type==0) {
                Glide.with(mContext).load(item).dontAnimate().into((ImageView) viewHolder.getView(R.id.service_icon));
            }else {
                Glide.with(mContext).load(item).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        setImageViewMathParent(mContext, (ImageView) viewHolder.getView(R.id.service_icon), bitmap);
                    }
                });
            }
        }
    }
    public static void setImageViewMathParent(Context context,ImageView image, Bitmap bitmap) {
        ViewGroup.LayoutParams vgl = image.getLayoutParams();

        if (bitmap == null) {
            return;
        }
        float bitWidth = bitmap.getWidth();
        float bithight = bitmap.getHeight();
        float bitScalew = bitWidth / bithight;
        float imgWidth =BitmapUtils.getScreenWith(context) * 3 / 5;
        //如果是图片的高大于宽  则采用屏幕的三分之一设置图片的高 宽按照比例计算
        float imgHight = BitmapUtils.getScreenHeight(context) * 1 / 3;
//        //如果图片宽度大于高度
        if (bitWidth >= bithight) {
            vgl.width = (int) imgWidth;
            vgl.height = (int) (imgWidth / bitScalew);
        } else {
            //当图片的高度大于宽度的
            vgl.width = (int) (imgHight * bitScalew);
            vgl.height = (int) imgHight;

        }
//        vgl.height = (int) imgHight;
//        vgl.width = (int) imgWidth;
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        image.setAdjustViewBounds(true);
        image.setLayoutParams(vgl);
        image.setImageBitmap(bitmap);
    }
}
