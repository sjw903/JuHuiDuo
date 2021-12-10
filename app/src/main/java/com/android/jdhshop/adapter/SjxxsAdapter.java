package com.android.jdhshop.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.bean.Sjxxsbean;

import java.util.List;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : szp
 *     time   : 2020/05/09
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class SjxxsAdapter extends RecyclerView.Adapter<SjxxsAdapter.ViewHolder>{
    private List<Sjxxsbean> mSjxxsbeanList;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.sjxxs_item, parent, false );
        SjxxsAdapter.ViewHolder holder = new SjxxsAdapter.ViewHolder( view );
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Sjxxsbean sjxxsbean = mSjxxsbeanList.get( position );
        holder.Time.setText( sjxxsbean.getTime());
        holder.Summary.setText( sjxxsbean.getSummary() );
        holder.Img.setImageResource( sjxxsbean.getImg() );
        holder.Tx.setText( sjxxsbean.getTx() );
        holder.Praice.setImageResource( sjxxsbean.getPraice() );
        holder.Tx1.setText( sjxxsbean.getTx1() );
        holder.Comment.setImageResource( sjxxsbean.getComment() );
        holder.Tx2.setText( sjxxsbean.getTx2() );
        holder.Share.setImageResource( sjxxsbean.getShare());
    }
    @Override
    public int getItemCount() {
        return mSjxxsbeanList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Time;
        TextView Summary;
        ImageView Img;
        TextView Tx;
        ImageView Praice;
        TextView Tx1;
        ImageView Comment;
        TextView Tx2;
        ImageView Share;
        public ViewHolder(View itemView) {
            super( itemView );
            Time = (TextView) itemView.findViewById( R.id.it_time );
            Summary = (TextView) itemView.findViewById( R.id.it_summary );
            Img = (ImageView) itemView.findViewById( R.id.image_img );
            Tx = (TextView) itemView.findViewById( R.id.it_tx );
            Praice = (ImageView) itemView.findViewById( R.id.image_praice );
            Tx1 = (TextView) itemView.findViewById( R.id.it_tx1 );
            Comment= (ImageView) itemView.findViewById( R.id.image_comment );
            Tx2 = (TextView) itemView.findViewById( R.id.it_tx2 );
            Share = (ImageView) itemView.findViewById( R.id.image_share );
        }
    }
    public SjxxsAdapter(List<Sjxxsbean> sjxxsbeanList){
        mSjxxsbeanList = sjxxsbeanList;
    }
}
