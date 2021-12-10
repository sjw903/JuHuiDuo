package com.android.jdhshop.merchantadapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.merchantbean.Merchantshoplistbean;

import java.text.DecimalFormat;
import java.util.List;


public class ShoplistischeckAdapter extends RecyclerView.Adapter<ShoplistischeckAdapter.MyHolder> {

    private Context context;
    private List<Merchantshoplistbean> lists;
    private SubClickListener subClickListener;
    private int selectindex = 0;
    DecimalFormat df=new DecimalFormat("0.00");
    public ShoplistischeckAdapter(Context context, List<Merchantshoplistbean> list, int selectindex) {
        this.context = context;
        this.lists = list;
        this.selectindex = selectindex;
    }
    public void setsubClickListener(SubClickListener topicClickListener) {
        this.subClickListener = topicClickListener;
    }
    public void setSelectindex(int seleindex){
        this.selectindex = seleindex;

    }
    public interface SubClickListener {
        void OntopicClickListener(View v, String detail, int posit);
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_shoplistischeck , parent , false);
        MyHolder myHolder = new MyHolder( view );
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        Glide.with( context ).load( Constants.APP_IP +lists.get( position ).img ).into( holder.iv_img );
        holder.tv_name.setText( lists.get( position ).service_name );
        if (selectindex == position){
            holder.ly_bk.setBackgroundResource( R.drawable.shopidcheck);
            holder.tv_name.setTextColor( Color.parseColor( "#0096E7" ) );
        }else{
            holder.ly_bk.setBackgroundColor( Color.parseColor( "#00000000" ));
            holder.tv_name.setTextColor( Color.parseColor( "#000000" ) );
        }
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
        private TextView tv_name;
        private LinearLayout ly_bk;
        public MyHolder(View itemView) {
            super( itemView );
            tv_name = itemView.findViewById( R.id.itemshoplist_name );
            iv_img = itemView.findViewById( R.id.itemshoplist_img );
            ly_bk = itemView.findViewById( R.id.itemshoplistidcheck_ly );
        }
    }
}
