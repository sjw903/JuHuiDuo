package com.android.jdhshop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.Jiayouorderbean;

import java.text.DecimalFormat;
import java.util.List;


public class JiayouorderAdapter extends RecyclerView.Adapter<JiayouorderAdapter.MyHolder> {

    private Context context;
    private List<Jiayouorderbean> lists;
    private SubClickListener subClickListener;
    DecimalFormat df=new DecimalFormat("0.00");
    public JiayouorderAdapter(Context context, List<Jiayouorderbean> list) {
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
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_jiayouorder , parent , false);
        MyHolder myHolder = new MyHolder( view );
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        holder.tvname.setText( lists.get( position ).gasName );
        holder.tv_state.setText( lists.get( position ).orderStatusName );
        holder.tv_jyxx.setText( lists.get( position ).oilNo+"("+lists.get( position ).litre+"升)" );

        holder.tv_sfje.setText(  lists.get( position ).amountPay );
        Glide.with( context ).load( "" ).error(R.mipmap.app_icon ).into( holder.iv_img );
        holder.tv_zftime.setText( lists.get( position ).payTime );

        holder.tv_help.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subClickListener!=null){
                    subClickListener.OntopicClickListener( view,"help",position );
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

        private TextView tvname,tv_state,tv_jyxx,tv_sfje,tv_zftime;
        private ImageView iv_img;
        private TextView tv_help;
        public MyHolder(View itemView) {
            super( itemView );
            tvname = itemView.findViewById( R.id.itemjiayouorder_name );
            tv_state = itemView.findViewById( R.id.itemjiayouorder_state );
            tv_jyxx = itemView.findViewById( R.id.itemjiayouorder_jyxx );
            tv_sfje = itemView.findViewById( R.id.itemjiayouorder_sfje );
            tv_zftime = itemView.findViewById( R.id.itemjiayouorder_zftime );
            iv_img = itemView.findViewById( R.id.itemjiayouorder_img);
            tv_help = itemView.findViewById( R.id.itemjiayouorder_help );

        }
    }
}
