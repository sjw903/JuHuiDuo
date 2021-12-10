package com.android.jdhshop.juduohui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.R;

public class NewsCommentSubListAdapter extends RecyclerView.Adapter<NewsCommentSubListAdapter.SubCommentViewHolder> {
    Activity mActivity;
    final JSONArray comments = new JSONArray();

    JSONArray adv_config;

    int adv_interval = 1;
    int adv_error_count = 0;


    public NewsCommentSubListAdapter(@NonNull Activity activity , @NonNull JSONArray sub_comments) {
        comments.addAll(sub_comments);
        mActivity = activity;
    }

    public void setInterval(int interval){ if (interval>0) adv_interval = interval; }
    public void setAdvConfig(JSONArray advConfig){
        adv_config = advConfig;
    }

    @Override
    public SubCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mActivity).inflate(R.layout.item_news_sub_comment,parent,false);
        return new SubCommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SubCommentViewHolder h, int position) {
        JSONObject current = comments.getJSONObject(position);
        Spanned sp = Html.fromHtml("<font color='#FB6500'>"+current.getString("nickname")+":</font>" + current.getString("content"));
        h.sub_comment_item_t.setText(sp);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class SubCommentViewHolder extends RecyclerView.ViewHolder{
        TextView sub_comment_item_t;
        public SubCommentViewHolder(View i) {
            super(i);
            sub_comment_item_t = i.findViewById(R.id.sub_comment_item_t);
        }
    }
}
