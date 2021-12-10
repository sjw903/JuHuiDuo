package com.android.jdhshop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.jdhshop.R;

import java.util.List;

public class InviteLinkUrlAdapter extends RecyclerView.Adapter<InviteLinkUrlAdapter.InviteHolder>{

    private Context mContext;
    private final List<String> invite_link_list;
    private final OnCopyClickListen onCopyClickListen;
    private String TAG = getClass().getSimpleName();
    public InviteLinkUrlAdapter(Context context, List<String> list,OnCopyClickListen listen){
        mContext = context;
        onCopyClickListen = listen;
        invite_link_list = list;
    }

    public interface OnCopyClickListen{
        void onCopyClick(int position);
    }

    @Override
    public InviteLinkUrlAdapter.InviteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.invite_link_item,parent,false);
        return new InviteHolder(v);
    }

    @Override
    public void onBindViewHolder(InviteLinkUrlAdapter.InviteHolder holder, int position) {
        String tmp = invite_link_list.get(position);
        tmp = tmp.replaceAll("\r\n","\n");

        holder.invite_text.setText(tmp);
        holder.invite_bt.setClickable(true);
        final int copy_position = position;
        holder.invite_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCopyClickListen.onCopyClick(copy_position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return invite_link_list.size();
    }

    static class InviteHolder extends RecyclerView.ViewHolder{
        TextView invite_text;
        TextView invite_bt;
        public InviteHolder(View itemView) {
            super(itemView);
            invite_text = itemView.findViewById(R.id.invite_text);
            invite_bt = itemView.findViewById(R.id.invite_bt);
        }
    }
}