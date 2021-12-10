package com.android.jdhshop.merchantadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.bean.Conerlistbean;

import java.util.List;


public class CommdityAdapter extends RecyclerView.Adapter<CommdityAdapter.MyHolder> {

    private Context context;
    private List<Conerlistbean> lists;
    private SubClickListener subClickListener;

    public CommdityAdapter(Context context, List<Conerlistbean> list) {
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
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_txjl , parent , false);
        MyHolder myHolder = new MyHolder( view );
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
            holder.tvtitle.setText( lists.get( position ).coupons_fee +"元");
            holder.tv_time.setText( lists.get( position ).create_time );
            holder.tv_ye.setText( "余额:"+lists.get( position ).all_coupons );
            holder.tv_title.setText( lists.get(position).goods_name );
            BaseLogDZiYuan.LogDingZiYuan(holder.image, "youhuiquan.png");
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

        private TextView tvtitle;
        private TextView tv_title,tv_ye,tv_time;
        private ImageView image;
        public MyHolder(View itemView) {
            super( itemView );
            tvtitle = itemView.findViewById( R.id.itemtxjl_qe );
            tv_title = itemView.findViewById( R.id.itemtxjl_title );
            tv_ye = itemView.findViewById( R.id.itemtxjl_ye );
            tv_time = itemView.findViewById( R.id.itemtxjl_time );
            image = itemView.findViewById( R.id.txjl_image );
        }
    }
}
