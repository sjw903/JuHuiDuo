package com.android.jdhshop.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.common.LogUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.TaskBigImgActivity;
import com.android.jdhshop.bean.SjxxsPjbean;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.merchantactivity.CommentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : szp
 *     time   : 2020/05/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class SjxxsPjAdapter extends RecyclerView.Adapter<SjxxsPjAdapter.ViewHolder> {
    private List<SjxxsPjbean> mSjxxsbeanPjList;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pj, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SjxxsPjbean sjxxsPjbean = mSjxxsbeanPjList.get(position);
        Glide.with(CaiNiaoApplication.getAppContext()).load(sjxxsPjbean.user_vavtar).error(R.mipmap.app_icon).into(holder.Image);
        holder.Name.setText(sjxxsPjbean.user_nickname);
        holder.Time.setText(sjxxsPjbean.comment_time);
        holder.Text.setText(sjxxsPjbean.content);
        try {
            holder.Rating.setRating(Float.valueOf(sjxxsPjbean.score));
        } catch (Exception e) {

        }
        if ("Y".equals(sjxxsPjbean.have_img)) {
            holder.recyclerView.setLayoutManager(new GridLayoutManager(CaiNiaoApplication.getAppContext(), 3));
            List<String> imgs = new ArrayList<>();
            String[] s=sjxxsPjbean.img.substring(1,sjxxsPjbean.img.length()-1).split(",");
            for(int i=0;i<s.length;i++){
                imgs.add(s[i].replaceAll("\"","").replaceAll("\\\\","/"));
            }
            MyAdapter adapter = new MyAdapter(R.layout.img_select, imgs);
            holder.recyclerView.setAdapter(adapter);
            adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    ArrayList<String> recordPaths = new ArrayList<>(); //缺陷记录的图片集合
                    for(int i=0;i<imgs.size();i++){
                        recordPaths.add(Constants.APP_IP+imgs.get(i));
                    }
                    Intent imgIntent = new Intent(CaiNiaoApplication.getAppContext(), TaskBigImgActivity.class);
                    imgIntent.putStringArrayListExtra("paths",recordPaths);
                    imgIntent.putExtra("title","图片");
                    imgIntent.putExtra("position",position);
                    CaiNiaoApplication.getAppContext().startActivity(imgIntent);
                }
            });
        }

//        holder.Img.setImageResource( sjxxsPjbean.getImg() );
    }

    @Override
    public int getItemCount() {
        return mSjxxsbeanPjList.size();
    }

    class MyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public MyAdapter(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            ImageView imageView = helper.getView(R.id.img);
            helper.getView(R.id.img_del).setVisibility(View.GONE);
            LogUtils.d("dwefe",item);
            Glide.with(mContext).load(Constants.APP_IP+item).into(imageView);
            helper.addOnClickListener(R.id.img_del).addOnClickListener(R.id.img);
           ImageView delimage= helper.getView(R.id.img_del);
            BaseLogDZiYuan.LogDingZiYuan(delimage, "del.png");
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView Image;
        ImageView Image1;
        TextView Name;
        RatingBar Rating;
        TextView Time;
        TextView Text;
        RecyclerView recyclerView;

        public ViewHolder(View view) {
            super(view);
            Image = (ImageView) view.findViewById(R.id.iv_image);
            Image1 = (ImageView) view.findViewById(R.id.img_del);
            Name = (TextView) view.findViewById(R.id.item_name);
            Time = (TextView) view.findViewById(R.id.it_time);
            Text = (TextView) view.findViewById(R.id.tv_text);
            recyclerView = view.findViewById(R.id.recyclerView);
            Rating = view.findViewById(R.id.rating);
        }
    }

    public SjxxsPjAdapter(List<SjxxsPjbean> sjxxsPjbeanList) {
        mSjxxsbeanPjList = sjxxsPjbeanList;
    }
}
