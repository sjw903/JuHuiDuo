package com.android.jdhshop.juduohui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.R;
import com.android.jdhshop.advistion.JuDuoHuiAdvertisement;
import com.android.jdhshop.utils.UIUtils;
import com.bumptech.glide.Glide;

public class NewsRecommendItemAdapter extends RecyclerView.Adapter<NewsRecommendItemAdapter.NewsItemHolder>{
    JSONArray news_array_list;
    Activity self_activity;
    NewsListItemAdapter.NewsClickListens newsClickListens;
    JSONArray adv_config;

    int adv_interval = 1;
    int adv_error_count = 0;
    String ADV_POSITION = "RecommendAdv";
    public NewsRecommendItemAdapter(Activity context, JSONArray news_array, NewsListItemAdapter.NewsClickListens listens){
        news_array_list = news_array;
        self_activity = context;
        newsClickListens = listens;

    }
    public void setInterval(int interval){ if (interval>0) adv_interval = interval; }
    public void setAdvConfig(JSONArray advConfig){
        adv_config = advConfig;
    }

    @Override
    public NewsRecommendItemAdapter.NewsItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(self_activity).inflate(R.layout.item_news_recommend_item,parent,false);
        return new NewsRecommendItemAdapter.NewsItemHolder(view);
    }

    @Override
    public void onViewRecycled(NewsRecommendItemAdapter.NewsItemHolder holder) {
        if (holder != null) {
            Glide.clear(holder.recommend_img);
            holder.recommend_img.setImageResource(R.drawable.no_banner);
            holder.recommend_top_adv.removeAllViews();
            holder.recommend_top_adv.setTag(R.id.news_list_item_adapter_position,"___");
            holder.recommend_top_adv.getLayoutParams().height = UIUtils.dp2px(100);
        }
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(NewsRecommendItemAdapter.NewsItemHolder holder, int position) {
        final int j = position;

        Log.d("JSONObject", "onBindViewHolder: " + (news_array_list.size() - position));

        holder.recommend_top_adv.setTag(R.id.news_list_item_adapter_position,ADV_POSITION+j);
        JSONObject news_info = news_array_list.getJSONObject(position);
        holder.recommend_title.setText(news_info.getString("title"));
        holder.recommend_counts.setText(news_info.getString("media_name"));
        String tmp_image_list = news_info.getString("image_list");
        news_info.put("display_id",position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsClickListens.click(news_info);
            }
        });

        if ("".equals(tmp_image_list)){
            holder.recommend_img.setVisibility(View.GONE);
            return;
        }

        if (tmp_image_list.startsWith("{")){
            tmp_image_list = "["+tmp_image_list+"]";
        }

        JSONArray image_list = JSONArray.parseArray(tmp_image_list);
        if (image_list.size()>0){
            holder.recommend_img.setVisibility(View.VISIBLE);
            Glide.with(self_activity).load(image_list.getJSONObject(0).getString("url")).asBitmap().placeholder(R.drawable.no_banner).error(R.drawable.no_banner).into(holder.recommend_img);
        }

        if (position % adv_interval == 0 && adv_config!=null && adv_config.size()>0){
            Log.d("JSONObject", adv_interval + "onBindViewHolder: " + position +"__"+ (position % adv_interval) + "加载广告入口");
            LinearLayout adv_box = holder.recommend_top_adv;
            JuDuoHuiAdvertisement advertisement = new JuDuoHuiAdvertisement(self_activity);
            advertisement.setInfomationAdListen(new JuDuoHuiAdvertisement.InfomationAdListen() {
                @Override
                public void click(View v) {}

                @Override
                public void dislike() {}

                @Override
                public void display(View v, String position, JSONObject current_adv_config) {
                    Log.d("JSONObject", position + "," + "," +adv_box.getTag(R.id.news_list_item_adapter_position) + "," + (ADV_POSITION+j)+"显示广告+"+current_adv_config);
                    if (self_activity!=null && !self_activity.isDestroyed()) {
                        self_activity.runOnUiThread(()->{
                            if ((ADV_POSITION+j).equals(adv_box.getTag(R.id.news_list_item_adapter_position))) {
                                AlphaAnimation ani = new AlphaAnimation(0, 1);
                                ani.setDuration(500);
                                LayoutAnimationController controller = new LayoutAnimationController(ani);
                                adv_box.setVisibility(View.VISIBLE);
                                adv_box.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                adv_box.setLayoutAnimation(controller);
                                adv_box.addView(v);
                            }
                        });
                        adv_error_count = 0;
                    }
                }

                @Override
                public void displayed() {

                }

                @Override
                public void close() {

                }

                @Override
                public void error(JSONObject error) {
                    Log.d("JSONObject", "error: " + error);
                    if (adv_error_count<adv_config.size()){
                        self_activity.runOnUiThread(()->{
                            advertisement.getInfomationAdv(adv_config,ADV_POSITION+j);
                        });

                        adv_error_count = adv_error_count + 1;
                    }
                }
            });
            advertisement.getInfomationAdv(adv_config,ADV_POSITION+j);
        }
        else{
            holder.recommend_top_adv.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return news_array_list.size();
    }

    public static class NewsItemHolder extends RecyclerView.ViewHolder{
        public TextView recommend_title;
        public TextView recommend_source;
        public TextView recommend_counts;
        public ImageView recommend_img;
        public LinearLayout recommend_top_adv;

        public NewsItemHolder(View itemView) {
            super(itemView);
            recommend_title = itemView.findViewById(R.id.recommend_title);
            recommend_source = itemView.findViewById(R.id.recommend_source);
            recommend_counts = itemView.findViewById(R.id.recommend_comments);
            recommend_img = itemView.findViewById(R.id.recommend_img);
            recommend_top_adv = itemView.findViewById(R.id.recommend_top_adv);
        }
    }
}