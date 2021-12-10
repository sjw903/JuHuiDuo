package com.android.jdhshop.juduohui.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.R;
import com.android.jdhshop.advistion.JuDuoHuiAdvertisement;
import com.android.jdhshop.bean.MessageEvent;
import com.android.jdhshop.utils.RelativeDateFormat;
import com.android.jdhshop.utils.UIUtils;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.panpf.sketch.SketchImageView;
import me.panpf.sketch.shaper.CircleImageShaper;

public class NewsCommentAdapter extends RecyclerView.Adapter<NewsCommentAdapter.CommentHolder> {
    private JSONArray comments;
    private JSONArray adv_config;
    private Activity mContext;
    private String TAG = getClass().getSimpleName();
    private CommentLikeListen listen;

    int adv_interval = 1;
    int adv_error_count = 0;
    String ADV_POSITION = "CommentAdv";

    public NewsCommentAdapter(@NonNull Activity context, @NonNull JSONArray comment_array, @NonNull CommentLikeListen likeListen) {
        mContext = context;
        comments = comment_array;
        listen = likeListen;
    }

    public void setInterval(int interval){ if (interval>0) adv_interval = interval; }
    public void setAdvConfig(JSONArray advConfig){
        adv_config = advConfig;
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_news_comment, parent, false);
        return new CommentHolder(view);
    }

    public interface CommentLikeListen {
        /**
         * 评论点赞
         *
         * @param comment_id   评论的ID
         * @param like_state   预期点赞状态 1喜欢，0不喜欢
         * @param index 当前数组中的索引
         */
        void like(int comment_id, int like_state, int index);
        void load();
    }

    @Override
    public void onViewRecycled(CommentHolder holder) {
        if (holder != null) {

            holder.comment_adv.removeAllViews();
            holder.comment_adv.setTag(R.id.news_list_item_adapter_position,"___");
            holder.comment_adv.getLayoutParams().height = UIUtils.dp2px(100);
        }
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(CommentHolder h, int i) {
        if (comments == null) return;
        final int j = h.getAdapterPosition();
        h.comment_adv.setTag(R.id.news_list_item_adapter_position,ADV_POSITION+j);

        h.user_head_pic.getOptions().setShaper(new CircleImageShaper());
        h.user_head_pic.displayImage(comments.getJSONObject(i).getString("user_profile_image_url"));
        h.user_nick.setText(comments.getJSONObject(i).getString("nickname"));
        String pub_time =comments.getJSONObject(i).getString("create_time");
        DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date date = fmt.parse(pub_time);
            String pub_format_time = RelativeDateFormat.format(date);
            h.comment_pub_date.setText(pub_format_time);
        }
        catch (Exception e){
            h.comment_pub_date.setText(pub_time);
        }

        h.comment_content.setText(comments.getJSONObject(i).getString("content"));
        h.comment_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageEvent m = new MessageEvent("reply_message");
                m.setPosition(j);
                EventBus.getDefault().post(m);
            }
        });

        h.comment_like.setText(comments.getJSONObject(j).getString("digg_count"));
        if (comments.getJSONObject(j).getIntValue("did") == 1){
            h.comment_like.setTextColor(Color.RED);
            h.comment_like_bt.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        }
        else{
            h.comment_like.setTextColor(Color.DKGRAY);
            h.comment_like_bt.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN);
        }

        final int comment_id = comments.getJSONObject(j).getIntValue("id");
        final int comment_state = comments.getJSONObject(j).getIntValue("did") == 1 ? 0 : 1;
        h.comment_like_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listen.like(comment_id , comment_state , j);
            }
        });

        if (comments.getJSONObject(i).containsKey("child") && comments.getJSONObject(i).getJSONArray("child").size() > 0) {
            h.sub_comment_list_box.setVisibility(View.VISIBLE);
            JSONArray sub_comment = comments.getJSONObject(i).getJSONArray("child");
            LinearLayoutManager lm = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            h.sub_comment_list.setAdapter(new NewsCommentSubListAdapter(mContext, sub_comment));
            h.sub_comment_list.setLayoutManager(lm);

        } else {
            h.sub_comment_list_box.setVisibility(View.GONE);
        }

        h.comment_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageEvent m = new MessageEvent("reply_message");
                m.setPosition(h.getAdapterPosition());
                EventBus.getDefault().post(m);
            }
        });

        // 广告加载处理

        if (i!=0 && i % adv_interval == 0 && adv_config!=null && adv_config.size()>0){
            Log.d("JSONObject", adv_interval + "onBindViewHolder: " + i +"__"+ (i % adv_interval) + "加载广告入口");

            final LinearLayout adv_box = h.comment_adv;
            JuDuoHuiAdvertisement advertisement = new JuDuoHuiAdvertisement(mContext);
            advertisement.setInfomationAdListen(new JuDuoHuiAdvertisement.InfomationAdListen() {
                @Override
                public void click(View v) {}

                @Override
                public void dislike() {}

                @Override
                public void display(View v, String position, JSONObject current_adv_config) {
                    Log.d("JSONObject", position + "," + "," +adv_box.getTag(R.id.news_list_item_adapter_position) + "," + (ADV_POSITION+j)+"显示广告+"+current_adv_config);
                    if (mContext!=null && !mContext.isDestroyed()) {
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if ((ADV_POSITION+j).equals(adv_box.getTag(R.id.news_list_item_adapter_position))) {
                                    AlphaAnimation ani = new AlphaAnimation(0, 1);
                                    ani.setDuration(500);
                                    LayoutAnimationController controller = new LayoutAnimationController(ani);
                                    adv_box.setVisibility(View.VISIBLE);
                                    adv_box.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    adv_box.setLayoutAnimation(controller);
                                    adv_box.addView(v);
                                }
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
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                advertisement.getInfomationAdv(adv_config,ADV_POSITION+j);
                            }
                        });
                        adv_error_count = adv_error_count + 1;
                    }
                    else{
                        adv_box.setVisibility(View.VISIBLE);
                    }
                }
            });
            advertisement.getInfomationAdv(adv_config,ADV_POSITION+j);
        }
        else{
            h.comment_adv.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class CommentHolder extends RecyclerView.ViewHolder {
        TextView user_nick;
        SketchImageView user_head_pic;
        TextView comment_pub_date;
        TextView comment_like;
        ImageView comment_like_bt;
        TextView comment_content;
        LinearLayout sub_comment_list_box;
        RecyclerView sub_comment_list;
        LinearLayout comment_adv;
        TextView comment_reply;

        public CommentHolder(View itemView) {
            super(itemView);
            comment_content = itemView.findViewById(R.id.comment_content);
            comment_pub_date = itemView.findViewById(R.id.comment_pus_date);
            sub_comment_list_box = itemView.findViewById(R.id.sub_comment_list_box);
            sub_comment_list = itemView.findViewById(R.id.sub_comment_list);
            user_nick = itemView.findViewById(R.id.user_nick);
            comment_like = itemView.findViewById(R.id.comment_like);
            comment_like_bt = itemView.findViewById(R.id.comment_like_bt);
            user_head_pic = itemView.findViewById(R.id.user_head_pic);
            comment_adv = itemView.findViewById(R.id.comment_adv);
            comment_reply = itemView.findViewById(R.id.comment_reply);
        }
    }
}
