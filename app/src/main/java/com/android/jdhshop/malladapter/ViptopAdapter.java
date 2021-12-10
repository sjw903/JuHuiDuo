package com.android.jdhshop.malladapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.Vippptype;

import java.util.List;

/**
 * 购物车  适配器
 * */
public class ViptopAdapter extends RecyclerView.Adapter<ViptopAdapter.MyHolder> {

    private Context context;
    private List<Vippptype> lists;
    private SubClickListener subClickListener;

    public ViptopAdapter(Context context, List<Vippptype> list) {
        this.context = context;
        this.lists = list;
    }
    public void setsubClickListener(SubClickListener topicClickListener) {
        this.subClickListener = topicClickListener;
    }

    public interface SubClickListener {
        void OntopicClickListener(View v, String detail, int posit);
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_viotop , parent , false);
        MyHolder myHolder = new MyHolder( view );
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        holder.tv_name.setText( lists.get( position ).fq_brand_name );
            Glide.with( context ).load( lists.get( position ).brand_logo ).into( holder.iv_img );

        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subClickListener!=null){
                    subClickListener.OntopicClickListener( view,"dj",position );
                }
            }
        } );

    }

    @Override
    public long getItemId(int position) {
        return position;
    }





    @Override
    public int getItemCount() {
        return lists.size();
    }

    //初始化控件
    class MyHolder extends RecyclerView.ViewHolder{

        private ImageView iv_img;
        private TextView tv_name;

        public MyHolder(View itemView) {
            super( itemView );
            iv_img = itemView.findViewById( R.id.itemviotop_img);
            tv_name = itemView.findViewById( R.id.itemviotop_name );

        }
    }
}
