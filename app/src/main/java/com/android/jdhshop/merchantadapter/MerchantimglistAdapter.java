package com.android.jdhshop.merchantadapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.merchantbean.Authbean;
import com.android.jdhshop.merchantbean.Merchantimglist;
import com.android.jdhshop.merchantbean.Merchantlistbean;

import java.text.DecimalFormat;
import java.util.List;


public class MerchantimglistAdapter extends RecyclerView.Adapter<MerchantimglistAdapter.MyHolder> {

    private Context context;
    private List<Merchantimglist> lists;
    private SubClickListener subClickListener;
    private List<Authbean> aulist;
    DecimalFormat df=new DecimalFormat("0.00");
    public MerchantimglistAdapter(Context context, List<Merchantimglist> list ) {
        this.context = context;
        this.lists = list;
        this.aulist = aulist;
    }
    public void setsubClickListener(SubClickListener topicClickListener) {
        this.subClickListener = topicClickListener;
    }

    public interface SubClickListener {
        void OntopicClickListener(View v, String detail, int posit);
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_merchantimg , parent , false);
        MyHolder myHolder = new MyHolder( view );
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        Glide.with( context ).load( Constants.APP_IP+lists.get( position ).img ).error( R.mipmap.app_icon ).into( holder.tv_img );


        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subClickListener!=null){
                    subClickListener.OntopicClickListener( view,"detail",position );
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

        private ImageView tv_img;
        public MyHolder(View itemView) {
            super( itemView );
            tv_img = itemView.findViewById( R.id.itemmerchant_img );
        }
    }
}
