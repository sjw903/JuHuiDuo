package com.android.jdhshop.merchantadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.merchantbean.Merchantimglist;

import java.text.DecimalFormat;
import java.util.List;


public class CaipinimglistssAdapter extends RecyclerView.Adapter<CaipinimglistssAdapter.MyHolder> {

    private Context context;
    private List<Merchantimglist> lists;
    private SubClickListener subClickListener;
    DecimalFormat df=new DecimalFormat("0.00");
    public CaipinimglistssAdapter(Context context, List<Merchantimglist> list) {
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
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_caipinimglistss , parent , false);
        MyHolder myHolder = new MyHolder( view );
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        String img_str = lists.get( position ).img;
        if (!img_str.startsWith("http")) img_str = Constants.APP_IP + img_str;
        Glide.with( context ).load( img_str ).into( holder.iv_img );

        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subClickListener!=null){
                    subClickListener.OntopicClickListener( view,"jy",position );
                }
            }
        } );
//        holder.itemView.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        } );
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
        public MyHolder(View itemView) {
            super( itemView );
            iv_img = itemView.findViewById( R.id.itemcaipinimg_img );
        }
    }
}
