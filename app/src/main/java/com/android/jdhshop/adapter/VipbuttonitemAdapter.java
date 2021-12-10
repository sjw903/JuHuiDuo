package com.android.jdhshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.PromotionDetailsActivity;
import com.android.jdhshop.bean.Vipptitem;

import java.util.List;


public class VipbuttonitemAdapter extends RecyclerView.Adapter<VipbuttonitemAdapter.MyHolder> {

    private Context context;
    private List<Vipptitem> lists;
    private SubClickListener subClickListener;

    public VipbuttonitemAdapter(Context context, List<Vipptitem> list) {
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
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.itembutton_item , parent , false);
        MyHolder myHolder = new MyHolder( view );
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {

        holder.tv_name.setText( lists.get( position ).itemshorttitle );
        Glide.with( context ).load( lists.get( position ).itempic+"_310x310.jpg" ).into( holder.iv_img );
        holder.tv_pric.setText("券后价: ¥"+ lists.get( position ).itemendprice );
        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("num_iid", lists.get( position ).itemid);
                Intent intent = new Intent( context,PromotionDetailsActivity.class );
                intent.putExtras( bundle );
                context.startActivity( intent );
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
        private TextView tv_name,tv_pric;

        public MyHolder(View itemView) {
            super( itemView );
            iv_img = itemView.findViewById( R.id.itemitem_img);
            tv_name = itemView.findViewById( R.id.itemitem_name );
            tv_pric = itemView.findViewById( R.id.itemitem_qhj );

        }
    }
}
