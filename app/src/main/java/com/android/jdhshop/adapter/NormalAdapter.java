package com.android.jdhshop.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.ShareBean;

import java.util.ArrayList;
import java.util.List;

public class NormalAdapter extends RecyclerView.Adapter<NormalAdapter.VH>{
    //② 创建ViewHolder
    public static class VH extends RecyclerView.ViewHolder{
        public CheckBox checkBox;
        public ImageView imageView;
        public VH(View v) {
            super(v);
            checkBox =  v.findViewById(R.id.checks);
            imageView =  v.findViewById(R.id.img);
        }
    }
    private List<Bitmap> bitmaps = new ArrayList<>();
    private List<CheckBox> checkBoxes=new ArrayList<>();
    private List<ShareBean> mDatas;
    private Context context;
    public NormalAdapter(Context context,List<ShareBean> data) {
        this.mDatas = data;
        this.context=context;
    }

    public List<Bitmap> getBitmaps() {
        return bitmaps;
    }

    public void setBitmaps(List<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
    }

    public List<CheckBox> getCheckBoxes() {
        return checkBoxes;
    }

    public void setCheckBoxes(List<CheckBox> checkBoxes) {
        this.checkBoxes = checkBoxes;
    }

    //③ 在Adapter中实现3个方法
    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        holder.checkBox.setChecked("Y".equals(mDatas.get(position).getIscheck()));
        Glide.with(context).load(mDatas.get(position).getImgUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                holder.imageView.setImageBitmap(bitmap);
                if(bitmaps.size()<position){
                    bitmaps.add(bitmap);
                    checkBoxes.add(holder.checkBox);
                } else{
                    bitmaps.add(position,bitmap);
                    checkBoxes.add(position,holder.checkBox);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater.from指定写法
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_share, parent, false);
        return new VH(v);
    }
}
