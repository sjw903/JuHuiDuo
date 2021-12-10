package com.android.jdhshop.merchantadapter;

import android.content.Context;
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
import com.android.jdhshop.merchantbean.Fwbean;

import java.text.DecimalFormat;
import java.util.List;


public class FwAdapter extends RecyclerView.Adapter<FwAdapter.MyHolder> {

    private Context context;
    private List<Fwbean> lists;
    private SubClickListener subClickListener;
    DecimalFormat df=new DecimalFormat("0.00");
    public FwAdapter(Context context, List<Fwbean> list) {
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
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_fw , parent , false);
        MyHolder myHolder = new MyHolder( view );
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        Glide.with( context ).load(Constants.APP_IP+ lists.get( position ).img ).into( holder.iv_img );
        holder.tv_name.setText( lists.get( position ).name );
//        for (int is = 0;is <strs.length;is++) {
//            if (lists.get( position ).index == Integer.parseInt( strs[is] )) {
//                holder.ly_all.setVisibility( View.VISIBLE );
//            } else {
//                holder.ly_all.setVisibility( View.GONE );
//            }
//        }
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
        private LinearLayout ly_all;
        public MyHolder(View itemView) {
            super( itemView );
            iv_img = itemView.findViewById( R.id.itemfw_img );
            tv_name = itemView.findViewById( R.id.itemfw_name );
            ly_all = itemView.findViewById( R.id.itemfw_all );
        }
    }
}
