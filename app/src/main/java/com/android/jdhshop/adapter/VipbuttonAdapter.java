package com.android.jdhshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.BrandlistActivity;
import com.android.jdhshop.bean.Vippptype;

import java.util.List;


public class VipbuttonAdapter extends RecyclerView.Adapter<VipbuttonAdapter.MyHolder> {

    private Context context;
    private List<Vippptype> lists;
    private SubClickListener subClickListener;

    VipbuttonitemAdapter vipbuttonitemAdapter;

    public VipbuttonAdapter(Context context, List<Vippptype> list) {
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
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_vipbutton , parent , false);
        MyHolder myHolder = new MyHolder( view );
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {

        holder.tv_name.setText( lists.get( position ).tb_brand_name );
        Glide.with( context ).load( lists.get( position ).brand_logo ).into( holder.iv_img );
        vipbuttonitemAdapter = new VipbuttonitemAdapter( context,lists.get( position ).item );
        GridLayoutManager gridLayoutManager = new GridLayoutManager( context,3 );
        gridLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        holder.recyclerView.setLayoutManager( gridLayoutManager );
        holder.recyclerView.setAdapter( vipbuttonitemAdapter );

        holder.tv_more.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent( context, BrandlistActivity.class );
                    intent.putExtra( "title",lists.get( position ).fq_brand_name );
                    intent.putExtra( "id",lists.get( position ).id );
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
        private TextView tv_name,tv_more;
        private RecyclerView recyclerView;

        public MyHolder(View itemView) {
            super( itemView );
            iv_img = itemView.findViewById( R.id.itemvipbutton_img);
            tv_name = itemView.findViewById( R.id.itemvipbutton_name );
            tv_more = itemView.findViewById( R.id.itemvipbutton_more );
            recyclerView = itemView.findViewById( R.id.itemvipbutton_recy );

        }
    }
}
