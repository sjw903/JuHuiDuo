package com.android.jdhshop.snadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.snbean.Suhomebean;

import java.text.DecimalFormat;
import java.util.List;


public class SnhometopAdapter extends RecyclerView.Adapter<SnhometopAdapter.MyHolder> {

    private Context context;
    private List<Suhomebean> lists;
    private SubClickListener subClickListener;
    DecimalFormat df=new DecimalFormat("0.00");
    public SnhometopAdapter(Context context, List<Suhomebean> list) {
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
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_snhometop , parent , false);
        MyHolder myHolder = new MyHolder( view );
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        if (holder.iv_img!=null)
        Glide.with( context ).load( Constants.sn_appip+ lists.get( position ).icon.replace( "\\","" ) ).into( holder.iv_img );
        Log.e( "imgurl", lists.get( position ).icon.replace( "\\","" ));
//        holder.iv_img.setVisibility( View.GONE );
        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subClickListener!=null){
                    subClickListener.OntopicClickListener( view,"jy",position );
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
        public MyHolder(View itemView) {
            super( itemView );
           iv_img = itemView.findViewById( R.id.snhometop_img );
        }
    }

}
