package com.android.jdhshop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.config.Constants;
import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.Jiayoulistbean;

import java.text.DecimalFormat;
import java.util.List;


public class JiayoulistAdapter extends RecyclerView.Adapter<JiayoulistAdapter.MyHolder> {

    private Context context;
    private List<Jiayoulistbean> lists;
    private SubClickListener subClickListener;
    DecimalFormat df=new DecimalFormat("0.00");
    public JiayoulistAdapter(Context context, List<Jiayoulistbean> list) {
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
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_jiayoulist , parent , false);
        MyHolder myHolder = new MyHolder( view );
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        holder.tvname.setText( lists.get( position ).gas_name );
        holder.tv_pric1.setText( "¥"+lists.get( position ).priceyfq );
        holder.tv_pric.setText( "/升/"+lists.get( position ).oil_name );
        holder.tv_address.setText( lists.get( position ).gasaddress );
        holder.tv_juli.setText( df.format( Double.parseDouble( lists.get( position ).distance )/1000 )+"Km" );
        Glide.with( context ).load( lists.get( position ).gaslogosmall ).into( holder.iv_img );
        holder.tv_jiangjia.setText( "直降"+df.format(Double.parseDouble( lists.get( position ).priceofficial )-Double.parseDouble( lists.get( position ).priceyfq )) );
//        holder.tv_button.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (subClickListener!=null){
//                    subClickListener.OntopicClickListener( view,"jy",position );
//                }
//            }
//        } );

        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subClickListener!=null){
                    subClickListener.OntopicClickListener( view,"jy",position );
                }
            }
        } );
        holder.tv_daohang.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subClickListener!=null){
                    subClickListener.OntopicClickListener( view,"dh",position );
                }
            }
        } );
//        holder.itemView.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        } );
        BaseLogDZiYuan.LogDingZiYuan(holder.image1, "xiajiang.png");
        BaseLogDZiYuan.LogDingZiYuan(holder.image2, "icon_location.png");
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

        private TextView tvname,tv_button,tv_pric,tv_juli,tv_daohang,tv_address;
        private TextView tv_jiangjia;
        private ImageView iv_img,image1,image2;
        private TextView tv_pric1;
        public MyHolder(View itemView) {
            super( itemView );
            tvname = itemView.findViewById( R.id.itemjiayoulist_name );
            tv_button = itemView.findViewById( R.id.itemjiayoulist_buybutton );
            tv_pric = itemView.findViewById( R.id.itemjiayoulist_pric );
            tv_juli = itemView.findViewById( R.id.itemjiayoulist_juli );
            tv_daohang = itemView.findViewById( R.id.itemjiayoulist_daohang );
            tv_address = itemView.findViewById( R.id.itemjiayoulist_address );
            iv_img = itemView.findViewById( R.id.itemjiayoulist_img );
            image1 = itemView.findViewById( R.id.jiayou_image1 );
            image2 = itemView.findViewById( R.id.jiayou_image2 );
            tv_jiangjia = itemView.findViewById( R.id.itemjiayoulist_jiangjia );
            tv_pric1 = itemView.findViewById( R.id.itemjiayoulist_pric1 );
        }
    }
}
