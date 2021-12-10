package com.android.jdhshop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.airbnb.lottie.L;
import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.BannerBean;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.wmm.StampPadding;
import com.android.jdhshop.wmm.StampType;
import com.android.jdhshop.wmm.StampWatcher;
import com.android.jdhshop.wmm.Stamper;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.ViewHolder> {

    private Context context;
    private List<BannerBean> data;
    private static Bitmap logo;
    private String url;
    private List<Bitmap> bitmaps = new ArrayList<>();
    private List<CheckBox> checkBoxes = new ArrayList<>();
    private JSONObject bitmap_list;
    private int checked = 0;
    private onCheckedStatusListener onCheckedStatusListener;
    ACache aCache;

    public InviteAdapter(Context context, String url, List<BannerBean> data) {
        this.context = context;
        this.data = data;
        this.url = url;
        bitmaps.clear();
        checkBoxes.clear();
        bitmap_list  = new JSONObject();
        logo = BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_icon);
    }

    public Bitmap getBitmap()
    {
        return bitmap_list.getObject("bitmap_"+checked,Bitmap.class);// bitmaps.size() <= 0 ? null : bitmaps.get(checked);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_invite, parent, false);
        return new ViewHolder(view);
    }

    public void setOnCheckedStatusListener(onCheckedStatusListener onCheckedStatusListener) {
        this.onCheckedStatusListener = onCheckedStatusListener;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        String img_url = data.get(position).getImg().startsWith("http") ? data.get(position).getImg() : Constants.APP_IP + data.get(position).getImg();
        if (img_url!=null){
            img_url = img_url.replace("\\/","/");
            img_url = img_url.replace("\"", "").replace("\\", "");
        }
        Glide.with(context).load(img_url).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                Bitmap tempBit = createQRcodeImage(bitmap);
//                bitmaps.add(tempBit);
//                bitmaps.add(position,tempBit);
                // 改用json数组存在bitmap
                bitmap_list.put("bitmap_"+position,tempBit);
                holder.imageView.setImageBitmap(tempBit);
//                Stamper.with(context)
//                        .setLabel("邀请码:" + CaiNiaoApplication.getUserInfoBean().user_msg.auth_code)
//                        .setLabelColor(context.getResources().getColor(R.color.white))//Color.rgb(255, 60, 70)
//                        .setLabelSize(40)
//                        .setMasterBitmap(tempBit)
//                        .setStampType(StampType.TEXT)
//                        .setStampPadding(new StampPadding(tempBit.getWidth() / 3 - 15, tempBit.getHeight() - 70))
//                        .setStampWatcher(new StampWatcher() {
//                            @Override
//                            protected void onError(String error, int requestId) {
//                                super.onError(error, requestId);
//                                LogUtils.d("dsfsdf", error);
//                            }
//                            @Override
//                            protected void onSuccess(Bitmap bitmap, int requestId) {
//                                super.onSuccess(bitmap, requestId);
//                                holder.imageView.setImageBitmap(bitmap);
//                                if (bitmaps.size() < position) {
//                                    bitmaps.add(bitmap);
//                                } else {
//                                    bitmaps.add(position, bitmap);
//                                }
//                            }
//                        })
//                        .setRequestId(position)
//                        .build();
            }
        });
        //holder.imageView.setTag(position);
        //checkBoxes.add(holder.checkBox);
        holder.checkBox.setChecked(data.get(position).getChecked() == 1);
        //holder.checkBox.setTag(position);
        holder.checkBox.setOnClickListener(view -> {
            onCheckedStatusListener.onCheckedStatus(position);
        });
        holder.imageView.setOnClickListener(view -> {
            onCheckedStatusListener.onCheckedStatus(position);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setCheckedStatus(int position) {
        checked = position;
        for (int i = 0; i < data.size(); i++) {
            if (i == position) {
                data.get(i).setChecked(1);
            } else {
                data.get(i).setChecked(0);
            }
        }
        notifyDataSetChanged();
    }

    public interface onCheckedStatusListener {
        void onCheckedStatus(int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private CheckBox checkBox;


        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            checkBox = itemView.findViewById(R.id.ch_box);

        }

    }

    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }

    public Bitmap createQRcodeImage(Bitmap myBit) {
        int w = 20;
        int h = 20;
        try {
            //判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1) {
                return null;
            }
            Hashtable<EncodeHintType, Object> hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, 1);
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, w, h, hints);
            bitMatrix = deleteWhite(bitMatrix);
            w = bitMatrix.getWidth();
            h = bitMatrix.getHeight();
            int[] pixels = new int[w * h];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * w + x] = 0xff000000;
                    } else {
                        pixels[y * w + x] = 0xffffffff;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
//            Bitmap bitmap1= addLogo(bitmap);
            return addQR(myBit, bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Bitmap addQR(Bitmap src, Bitmap logo) {
        //如果原二维码为空，返回空
        if (src == null) {
            return null;
        }
        //如果logo为空，返回原二维码
        if (src == null || logo == null) {
            return src;
        }

        //这里得到原图bitmap的数据
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        //logo的Width和Height
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        //同样如果为空，返回空
        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }
        //同样logo大小为0，返回原二维码
        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }
        Rect mSrcRect, mDestRect;
        float scaleFactor = 1 + srcWidth * 1.0f / 4 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            mSrcRect = new Rect(0, 0, srcWidth, srcHeight);
            mDestRect = new Rect(0, 0, srcWidth, srcHeight);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, mSrcRect, mDestRect, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight) / 2 + 11 + logoHeight, null);
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;
    }

    //给二维码添加图片
    //第一个参数为原二维码，第二个参数为添加的logo
    private static Bitmap addLogo(Bitmap src) {
        //如果原二维码为空，返回空
        if (src == null) {
            return null;
        }
        //如果logo为空，返回原二维码
        if (src == null || logo == null) {
            return src;
        }

        //这里得到原二维码bitmap的数据
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        //logo的Width和Height
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        //同样如果为空，返回空
        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }
        //同样logo大小为0，返回原二维码
        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        //logo大小为二维码整体大小的1/5，也可以自定义多大，越小越好
        //二维码有一定的纠错功能，中间图片越小，越容易纠错
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 3, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;
    }

}