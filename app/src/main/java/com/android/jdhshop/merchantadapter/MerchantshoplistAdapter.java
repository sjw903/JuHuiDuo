package com.android.jdhshop.merchantadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.merchantbean.Authbean;
import com.android.jdhshop.merchantbean.Merchantimglist;
import com.android.jdhshop.merchantbean.Merchantshoplistbean;

import java.text.DecimalFormat;
import java.util.List;


public class MerchantshoplistAdapter extends RecyclerView.Adapter<MerchantshoplistAdapter.MyHolder> {

    private Context context;
    private List<Merchantshoplistbean> lists;
    private SubClickListener subClickListener;
    DecimalFormat df=new DecimalFormat("0.00");
    public MerchantshoplistAdapter(Context context, List<Merchantshoplistbean> list ) {
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
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_merchantshoplist , parent , false);
        MyHolder myHolder = new MyHolder( view );
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        Glide.with( context ).load( Constants.APP_IP+lists.get( position ).img ).error( R.mipmap.app_icon ).into( holder.tv_img );
        holder.tv_name.setText(lists.get(position).goods_name);

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
        private TextView tv_name;
        public MyHolder(View itemView) {
            super( itemView );
            tv_name = itemView.findViewById(R.id.shoplist_name);
            tv_img = itemView.findViewById( R.id.shoplist_img );
        }
    }
}
