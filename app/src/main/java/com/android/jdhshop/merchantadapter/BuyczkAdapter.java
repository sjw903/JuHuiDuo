package com.android.jdhshop.merchantadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.bean.Buyczqbean;

import java.util.List;


public class BuyczkAdapter extends RecyclerView.Adapter<BuyczkAdapter.MyHolder> {

    private Context context;
    private List<Buyczqbean> lists;
    private SubClickListener subClickListener;
    private int selectindex = 0;

    public BuyczkAdapter(Context context, List<Buyczqbean> list) {
        this.context = context;
        this.lists = list;
    }
    public void setsubClickListener(SubClickListener topicClickListener) {
        this.subClickListener = topicClickListener;
    }
    public void setSelectindex(int selectindex){
        this.selectindex = selectindex;
//        notifyDataSetChanged();
    }
    public interface SubClickListener {
        void OntopicClickListener(String detail, int posit);
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_buyczq , parent , false);
        MyHolder myHolder = new MyHolder( view );
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        if (selectindex == position){
            holder.ly_all.setBackgroundResource(R.drawable.buybgck);
        }else{
            holder.ly_all.setBackgroundResource(R.drawable.buybg);
        }
        if (lists.get(position).is_top.equals("Y")){
            holder.ly_istop.setVisibility(View.VISIBLE);
        }else{
            holder.ly_istop.setVisibility(View.GONE);
        }
        holder.tv_one.setText( lists.get( position ).cat_name);
        holder.tv_two.setText( lists.get( position ).payment_money );
        holder.ly_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subClickListener!=null){
                    subClickListener.OntopicClickListener("",position);
                }
            }
        });

//        notifyDataSetChanged();

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

        private TextView tv_one,tv_two;
        private LinearLayout ly_all,ly_istop;
        public MyHolder(View itemView) {
            super( itemView );
            tv_one = itemView.findViewById( R.id.itembuyczq_one );
            tv_two = itemView.findViewById( R.id.itembuyczq_two );
            ly_all = itemView.findViewById(R.id.itembuyczq_lyall);
            ly_istop = itemView.findViewById(R.id.buy_istop);
        }
    }
}
