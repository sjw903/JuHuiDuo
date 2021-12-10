package com.android.jdhshop.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.bean.Hyzxbean;

import java.util.List;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : szp
 *     time   : 2020/05/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class HyzxAdapter extends RecyclerView.Adapter<HyzxAdapter.ViewHolder>{
    private List<Hyzxbean> mHyzxbeanList;


    @Override
    public HyzxAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_hyzx, parent, false );
        HyzxAdapter.ViewHolder holder = new HyzxAdapter.ViewHolder( view );
        return holder;
    }
    @Override
    public void onBindViewHolder(HyzxAdapter.ViewHolder holder, int position) {
        Hyzxbean hyzxbean = mHyzxbeanList.get( position );
        holder.Image.setImageResource( hyzxbean.getImage() );
        holder.Text.setText( hyzxbean.getText() );
//        holder.Text.setText( hyzxbean.getText2() );
//        holder.Text.setText( hyzxbean.getText3() );
//        holder.Text.setText( hyzxbean.getText4() );
//        holder.Text.setText( hyzxbean.getText5() );
//        holder.Text.setText( hyzxbean.getText6() );
        holder.Text.setText( hyzxbean.getText7() );
    }

    @Override
    public int getItemCount() {
        return mHyzxbeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView Image;
        TextView Text;
//        TextView Text2;
//        TextView Text3;
//        TextView Text4;
//        TextView Text5;
//        TextView Text6;
        TextView Text7;
        public ViewHolder(View itemView) {
            super( itemView );
            Image = (ImageView) itemView.findViewById( R.id.iv_image );
            Text = (TextView) itemView.findViewById( R.id.it_text_1 );
            Text = (TextView) itemView.findViewById( R.id.it_text_2 );
            Text = (TextView) itemView.findViewById( R.id.it_text_3 );
            Text = (TextView) itemView.findViewById( R.id.it_text_4 );
            Text = (TextView) itemView.findViewById( R.id.it_text_5 );
            Text = (TextView) itemView.findViewById( R.id.it_text_6 );
            Text = (TextView) itemView.findViewById( R.id.it_text_7 );
        }
    }
    public HyzxAdapter(List<Hyzxbean> hyzxbeanList) {
        mHyzxbeanList = hyzxbeanList;
    }
}
