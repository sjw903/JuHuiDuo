package com.android.jdhshop.juduohui.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.R;
import com.android.jdhshop.advistion.JuDuoHuiAdvertisement;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.dialog.CustomDialog;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.juduohui.CommonDialog;
import com.android.jdhshop.juduohui.NewsInformation;
import com.android.jdhshop.juduohui.NewsReportActivity;
import com.android.jdhshop.juduohui.dialog.JuduohuiShareDialog;
import com.android.jdhshop.juduohui.response.HttpJsonResponse;
import com.android.jdhshop.utils.WxUtil;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.loopj.android.http.RequestParams;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.ViewConvertListener;
import com.shehuan.nicedialog.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 我的个人主页 文章列表
 */
public class HomePagePubListItemAdapter extends RecyclerView.Adapter<HomePagePubListItemAdapter.NewsItemHolder> {
    JSONArray news_array_list;
    int interval_nums;
    NewsClickListens newsClickListens;
    Activity mActivity;
    JSONArray adv_config;
    JSONArray adv_history = new JSONArray();
    private String current_type = "1";
    private boolean is_me;

    public void setIs_me(boolean is_me) {
        this.is_me = is_me;
    }


    public HomePagePubListItemAdapter(Activity activity, JSONArray news_array, NewsClickListens listens) {
        news_array_list = news_array;
        mActivity = activity;
        newsClickListens = listens;
    }

    public void setAdvConfig(JSONArray advConfig, String interval_num) {
        adv_config = advConfig;
        interval_nums = Integer.valueOf(interval_num);
    }

    public void setCurrentType(String type) {
        current_type = type;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public HomePagePubListItemAdapter.NewsItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Log.d(TAG, "onCreateViewHolder: " + parent.getWidth() + "," + parent.getMeasuredWidth());
        View view = LayoutInflater.from(mActivity).inflate(R.layout.homepage_news_pub_list_item, parent, false);
        return new HomePagePubListItemAdapter.NewsItemHolder(view);
    }


    @Override
    public void onViewRecycled(HomePagePubListItemAdapter.NewsItemHolder holder) {
        // Log.d(TAG, "onViewRecycled: ");
        if (holder != null) {
            holder.share_text.setTag(R.id.tag_pub_list_id, "Recycled");
            Glide.clear(holder.pub_image);
            holder.pub_image.setImageResource(R.drawable.no_banner);
        }
        super.onViewRecycled(holder);
    }

    public JSONObject getConfigByKey(String key) {
        for (int i = 0; i < adv_history.size(); i++) {
            if (key.equals(adv_history.getJSONObject(i).getString("save_key"))) {
                return adv_history.getJSONObject(i).getJSONObject("config");
            }
        }
        return null;
    }

    public void clearHistory() {
        adv_history.clear();
    }

    @Override
    public void onBindViewHolder(HomePagePubListItemAdapter.NewsItemHolder holder, int position) {
//        holder.setIsRecyclable(false); // 此方法需要重写。先暂时设置为不复用。
        long j = getItemId(position);
        final String save_key = "position_" + j;
        JSONObject news_info = news_array_list.getJSONObject(position);
        holder.share_text.setTag(R.id.tag_pub_list_id, news_info.getString("id"));
        String news_title = news_info.getString("title");
        holder.share_text.setVisibility(View.GONE);
        holder.delete_text.setVisibility(View.VISIBLE);
        if (interval_nums != 0 && (position - 1) % interval_nums == 0 && position != 0 || position == 1) {
            JSONObject current_config = getConfigByKey(save_key);
            holder.news_adv_box.setVisibility(View.VISIBLE);
            holder.news_adv_box_bt.setVisibility(View.VISIBLE);
            holder.news_adv_box.setTag(R.id.tag_adv_position, save_key);

            if (current_config != null && current_config.getIntValue("height") != 0 && save_key.equals(current_config.getString("display"))) {
                holder.news_adv_box.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, current_config.getIntValue("height")));
            } else {
                holder.news_adv_box.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            if (adv_config != null) {
                JuDuoHuiAdvertisement juDuoHuiAdvertisement = new JuDuoHuiAdvertisement(mActivity, null);
                juDuoHuiAdvertisement.setInfomationAdListen(new JuDuoHuiAdvertisement.InfomationAdListen() {
                    int error_count = 0;
                    int max_count = 5;

                    @Override
                    public void click(View v) {
                        // Log.d(TAG, "click: 广告被点击了");
                    }

                    @Override
                    public void dislike() {
                        // Log.d(TAG, "dislike: 不喜欢啊");
                    }

                    @Override
                    public void display(View v, String p, JSONObject current_adv_config) {
                        mActivity.runOnUiThread(() -> {
                            error_count = 0;
                            if (holder.news_adv_box.getTag(R.id.tag_adv_position).toString().equals(p)) {
                                holder.news_adv_box.removeAllViews();
                                holder.news_adv_box.addView(v);
                                holder.news_adv_box.setTag(R.id.tag_adv_config, current_adv_config);
                                // 保存已加载的广告配置
                                Animation aa = new AlphaAnimation(0, 1);
                                aa.setDuration(1000);
                                holder.news_adv_box.setLayoutAnimation(new LayoutAnimationController(aa));

                                int intw = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                                int inth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                                v.measure(intw, inth);
                                int intwidth = v.getMeasuredWidth();
                                int intheight = v.getMeasuredHeight();


                                // Log.d(TAG, "计算后的宽高: " + intwidth + "," + intheight);
                                // Log.d(TAG, "间隔间隔间隔");
                                v.postDelayed(() -> {
                                    // Log.d(TAG, "计算后宽高111: - " + v.getMeasuredWidth() + "," + v.getMeasuredHeight());
                                }, 100);

                                v.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                                    @Override
                                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                                        if (v.getHeight() > 10 && v.isAttachedToWindow() && p.equals(save_key)) {
                                            current_adv_config.put("height", v.getHeight() + 16);
                                            current_adv_config.put("display", save_key);
                                            saveConfigByKey(save_key, current_adv_config);
                                            v.removeOnLayoutChangeListener(this);
                                            // Log.d(TAG,"adv_history: "+adv_history.toString());
                                        }
                                    }
                                });
                            } else {
//                            holder.news_adv_box.setVisibility(View.GONE);
                            }
                        });
                    }

                    @Override
                    public void displayed() {
                    }

                    @Override
                    public void close() {
                        holder.news_adv_box.setVisibility(View.GONE);
                    }

                    @Override
                    public void error(JSONObject error) {
                        // Log.d(TAG, "error: " + error.toJSONString() + "error count:" + error_count + "--" + (error_count < adv_config.size()));
                        // Log.d(TAG, "error: 重新加载广告");

                        error_count = error_count + 1;
                        mActivity.runOnUiThread(() -> {
                            if (error_count < adv_config.size()) {
                                juDuoHuiAdvertisement.getInfomationAdv(adv_config, error.getString("position"));
//                                    error_count = error_count +1;
                            } else {
                                holder.news_adv_box.setVisibility(View.GONE);
                            }
                        });
                    }
                });

                if (current_config == null) {
                    juDuoHuiAdvertisement.getInfomationAdv(adv_config, save_key);
                } else {
                    juDuoHuiAdvertisement.getInfomationAdvWithChannel(current_config, save_key);
                }
            }

        } else {
            holder.news_adv_box.setVisibility(View.GONE);
            holder.news_adv_box_bt.setVisibility(View.GONE);
//            holder.news_adv_box.removeAllViews();
        }

        // {"image_list":"","title":"九九重阳节，优惠抢不停19.9元抢179元大礼包，今日开抢！赶快行动吧","id":30099,"readnum":1,"state":2,"create_time":"14小时前","gold":11,"source_add_time":1634177987}
        // 文章状态 1.未审核 2.审核通过 3.审核未通过 4.用户发起重审 5.重审通过 6.重审未通过 7.软删除
        String ds_state = "";
        switch (news_info.getIntValue("state")) {
            case 1:
                ds_state = "待审核";
                break;
            case 2:
            case 5:
                ds_state = "已发布";
                if (news_info.getString("id").equals(holder.share_text.getTag(R.id.tag_pub_list_id))) {
                    holder.share_text.setVisibility(View.VISIBLE);
                    holder.share_text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //分享赚金币
                            // http://test.xinniankeji.com/wap/Article/desc?auth_code=用户邀请码&id=文章id
                            String share_url = SPUtils.getStringData(mActivity, "share_article_host", "") + "/wap/Article/desc?auto_code=" + SPUtils.getStringData(mActivity, "inviteCode", "") + "&id=" + news_info.getString("id");
                            WxUtil.sendCardMessage(mActivity, news_info.getString("title"), news_info.getString("desc"), share_url, WxUtil.WX_FRIEND);
                        }
                    });
                }
                break;
            case 3:
                ds_state = news_info.getString("refuse_str") == null ? "审核未通过" : "审核未通过 :" + news_info.getString("refuse_str");
                if (news_info.getString("id").equals(holder.share_text.getTag(R.id.tag_pub_list_id))) {
                    holder.share_text.setVisibility(View.VISIBLE);
                    holder.share_text.setText("重审");
                    holder.share_text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //发起重审
                            RequestParams req = new RequestParams();
                            req.put("media_id", news_info.getString("id"));

                            HttpUtils.post(Constants.MEDIA_LIB_REPORT_RECHECK, mActivity, req, new HttpJsonResponse() {
                                @Override
                                protected void onSuccess(JSONObject response) {
                                    super.onSuccess(response);
                                    if (response.getIntValue("code") == 0) {
                                        ToastUtils.showShortToast(mActivity, response.getString("msg"));
                                        newsClickListens.refresh();
                                    } else {
                                        ToastUtils.showShortToast(mActivity, "重审失败，" + response.getString("msg"));
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                                    super.onFailure(statusCode, headers, responseString, e);
                                }
                            });
                        }
                    });
                }
                break;
            case 4:
                ds_state = "用户发起重审";
                break;
            case 6:
                ds_state = news_info.getString("refuse_str") == null ? "重审未通过" : "重审未通过: " + news_info.getString("refuse_str");
                break;
            case 7:
                ds_state = "已删除";
                holder.news_title.setTextColor(Color.parseColor("#666666"));
                news_title = "<s>" + news_info.getString("title") + "</s>";
                holder.delete_text.setVisibility(View.GONE);
                break;
            case 8:
                ds_state = "已被举报 :" + news_info.getString("report_str");
        }
        holder.status_text.setText(ds_state);
        // 软删除
        holder.delete_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log.d(TAG, "onClick: 想删除 " + news_info.getString("id"));

                CommonDialog.getInstance(mActivity).setTitle("删除转载文章提醒").setMessage("是否要删除此文章？").setSubmit("确定", true).setCancelButton("取消", false).setListener(new CommonDialog.CommonDialogListener() {
                    @Override
                    public void OnSubmit(AlertDialog dialog) {
                        dialog.dismiss();
                        RequestParams req = new RequestParams();
                        req.put("media_id", news_info.getString("id"));
                        HttpUtils.post(Constants.MEDIA_LIB_REPORT_DEL, mActivity, req, new HttpJsonResponse() {
                            @Override
                            protected void onSuccess(JSONObject response) {
                                super.onSuccess(response);
                                // Log.d(TAG, "onSuccess: " + response.toJSONString());
                                if (response.getIntValue("code") == 0) {
                                    ToastUtils.showShortToast(mActivity, response.getString("msg"));
                                    newsClickListens.refresh();
                                } else {
                                    ToastUtils.showShortToast(mActivity, "删除失败，" + response.getString("msg"));
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                                super.onFailure(statusCode, headers, responseString, e);
                                ToastUtils.showShortToast(mActivity, "删除失败，" + responseString);
                            }
                        });

                    }

                    @Override
                    public void OnCancel(AlertDialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void OnDismiss() {

                    }

                    @Override
                    public void OnClose(AlertDialog dialog) {

                    }
                }).show();
            }
        });
        // 分享赚金币等功能

        int digg_count = news_info.getIntValue("digg_count");
        int comment_count = news_info.getIntValue("comment_count");
        int share_num = news_info.getIntValue("share_num");
        if (0 != digg_count) {
            holder.tv_like.setText(String.valueOf(digg_count));
        } else {
            holder.tv_like.setText("赞");
        }
        if (0 != comment_count) {
            holder.tv_comment.setText(String.valueOf(comment_count));
        } else {
            holder.tv_comment.setText("评论");

        }
        if (0 != share_num) {
            holder.tv_share.setText(String.valueOf(share_num));
        } else {
            holder.tv_share.setText("分享");
        }
        holder.news_title.setText(Html.fromHtml(news_title));
        String state = getState(news_info.getIntValue("state"));
        holder.tv_state.setText(state);
        holder.pub_info.setText(news_info.getString("create_time"));
        holder.user_name.setText(news_info.getString("user_name"));
        Glide.with(mActivity).load(news_info.getString("auth_icon")).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(holder.auth_icon);

        holder.pub_info_1.setText("已获得" + (news_info.getString("gold") != null ? news_info.getString("gold") : "0") + "金币");

        holder.pub_info_2.setText(news_info.getString("readnum") + "浏览");

        if (is_me) {
            holder.iv_top.setVisibility(View.VISIBLE);
        } else {
            holder.iv_top.setVisibility(View.GONE);
            holder.pub_info_2.setVisibility(View.GONE);
        }
        int is_top = news_info.getIntValue("is_top");
        if (0 == is_top) {
            holder.iv_top.setVisibility(View.GONE);
        } else {
            holder.iv_top.setVisibility(View.VISIBLE);
        }
        int is_reprint = news_info.getIntValue("is_reprint");
        if (0 == is_reprint) {
            holder.is_reprint.setVisibility(View.GONE);
        } else {
            holder.is_reprint.setVisibility(View.VISIBLE);
        }
        String tmp_image_list = news_info.getString("image_list");
        if (tmp_image_list != null) {
            if (tmp_image_list.startsWith("{")) {
                // jsonObject
                JSONObject tmp = JSONObject.parseObject(tmp_image_list);
                Glide.with(mActivity).load(tmp.getString("url")).asBitmap().placeholder(R.drawable.no_banner).error(R.drawable.no_banner).into(holder.pub_image);

            } else if (tmp_image_list.startsWith("[")) {
                // jsonArray
                JSONArray image_list = JSONArray.parseArray(tmp_image_list);
                Glide.with(mActivity).load(image_list.getJSONObject(0).getString("url")).asBitmap().placeholder(R.drawable.no_banner).error(R.drawable.no_banner).into(holder.pub_image);
            }

        } else {
            holder.pub_image.setImageResource(R.drawable.no_banner);
        }

        // Log.d(TAG, "onBindViewHolder: tmp_image_list = " + tmp_image_list +"," + (tmp_image_list == null) + "null".equals(tmp_image_list));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsClickListens.click(news_info, position);
            }
        });
        int did = news_info.getIntValue("did");
        setLikeState(holder.iv_like, did);

        holder.iv_more.setOnClickListener(v -> {
            showMore(news_info, is_me, is_top);
        });
        holder.tv_comment.setOnClickListener(v -> {
            comment(news_info);
        });
        holder.ll_like.setOnClickListener(v -> {
            boolean isEnabled = holder.iv_like.isEnabled();
            int status = 1;
            if (isEnabled) {
                status = 0;
            }
            setLikeState(status, holder.iv_like);
            if (!isEnabled) {
                try {
                    int num = Integer.parseInt(holder.tv_like.getText().toString());
                    num++;
                    holder.tv_like.setText(String.valueOf(num));
                } catch (NumberFormatException e) {
                    holder.tv_like.setText(String.valueOf(1));
                }
            } else {
                int num = Integer.parseInt(holder.tv_like.getText().toString());
                num--;
                if (0 == num) {
                    holder.tv_like.setText("赞");
                } else {
                    holder.tv_like.setText(String.valueOf(num));
                }
            }
            int id = news_info.getIntValue("id");
            like(status, id, position);
        });
        holder.ll_share.setOnClickListener(v -> {
            share(news_info);
        });
//        JSONArray image_list = JSONArray.parseArray(tmp_image_list);
//        Glide.with(mActivity).load(image_list.getJSONObject(0).getString("url")).asBitmap().placeholder(R.drawable.no_banner).error(R.drawable.kuang).into(holder.pub_image);
    }

    public void saveConfigByKey(String key, JSONObject config) {
        int find_index = -1;
        for (int i = 0; i < adv_history.size(); i++) {
            if (key.equals(adv_history.getJSONObject(i).getString("save_key"))) {
                find_index = i;
            }
        }

        if (find_index != -1) adv_history.remove(find_index);
        // Log.d(TAG, "saveConfigByKey: " + key + "," + config);
        if (key.equals(config.getString("display"))) {
            JSONObject add_item = new JSONObject();
            add_item.put("save_key", key);
            add_item.put("config", config);

            adv_history.add(add_item);
        }
    }

    @Override
    public int getItemCount() {
        if (news_array_list.size() == 0) {
            // Log.d(TAG, "getItemCount: 暂无数据");
        }
        return news_array_list.size();
    }

    public static class NewsItemHolder extends RecyclerView.ViewHolder {
        public TextView news_title;
        public LinearLayout pub_info_box;
        public LinearLayout ll_like;
        public LinearLayout ll_comment;
        public LinearLayout ll_share;
        public TextView pub_info; // 发布时间
        public TextView pub_info_1; // 已获得金币数量
        public TextView pub_info_2; // 浏览次数
        public TextView is_reprint; // 是否为转载文章 1.转载文章 2.爬取文章
        public TextView status_text; //状态信息
        public TextView delete_text; // 删除按钮
        public TextView share_text; // 分享
        public ImageView auth_icon; // 用户头像
        public ImageView iv_like; // 赞图片
        public ImageView iv_top; // 置顶
        public ImageView iv_more; // 更多
        public TextView tv_state; // 状态
        public TextView user_name; // 用户名称
        public TextView tv_comment; // 评论
        public TextView tv_like; // 赞
        public TextView tv_share; // 分享

        public LinearLayout news_adv_box;
        public LinearLayout news_adv_box_bt;

        public ImageView pub_image;
        public int myPositionID = 0;

        public NewsItemHolder(View itemView) {
            super(itemView);
            pub_info_box = itemView.findViewById(R.id.pub_info_box);
            news_adv_box = itemView.findViewById(R.id.news_adv_box);
            news_adv_box_bt = itemView.findViewById(R.id.news_adv_box_bt);
            news_title = itemView.findViewById(R.id.news_title);
            pub_info = itemView.findViewById(R.id.pub_info);
            pub_info_1 = itemView.findViewById(R.id.pub_info2);
            pub_info_2 = itemView.findViewById(R.id.pub_info3);

            ll_like = itemView.findViewById(R.id.ll_like);
            ll_comment = itemView.findViewById(R.id.ll_comment);
            ll_share = itemView.findViewById(R.id.ll_share);

            auth_icon = itemView.findViewById(R.id.iv_header);
            user_name = itemView.findViewById(R.id.tv_name);
            is_reprint = itemView.findViewById(R.id.tv_is_reprint);

            iv_like = itemView.findViewById(R.id.iv_like);
            tv_comment = itemView.findViewById(R.id.tv_comment);
            tv_like = itemView.findViewById(R.id.tv_like);
            iv_top = itemView.findViewById(R.id.iv_top);
            iv_more = itemView.findViewById(R.id.iv_more);
            tv_state = itemView.findViewById(R.id.tv_state);
            tv_share = itemView.findViewById(R.id.tv_share);

            status_text = itemView.findViewById(R.id.status_text);
            delete_text = itemView.findViewById(R.id.delete_text);
            share_text = itemView.findViewById(R.id.share_text);
            pub_image = itemView.findViewById(R.id.pub_image);

        }
    }

    private String getState(int state) {
        String result = "";
        switch (state) {
            case 1:
                result = "未审核";
                break;
            case 2:
                result = "审核通过";
                break;
            case 3:
                result = "审核未通过";
                break;
            case 4:
                result = "用户发起重审";
                break;
            case 5:
                result = "重审通过";
                break;
            case 6:
                result = "重审未通过";
                break;
            case 7:
                result = "软删除";
                break;
        }
        return result;
    }

    /**
     * 显示更多
     *
     * @param news_info
     * @param is_me
     * @param isTop
     */
    private void showMore(JSONObject news_info, boolean is_me, int isTop) {
        if (mActivity instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) mActivity;

            CustomDialog customDialog = CustomDialog.init();
            List<String> stringList = new ArrayList<>();
            if (is_me) {
                if (isTop == 1) {
                    stringList.add(mActivity.getString(R.string.homepage_top_cancel));
                } else {
                    stringList.add(mActivity.getString(R.string.homepage_top));
                }
                stringList.add(mActivity.getString(R.string.homepage_delete));
//                stringList.add(mActivity.getString(R.string.homepage_retrial));
            } else {
//                stringList.add(mActivity.getString(R.string.homepage_cancel_attention));
//                stringList.add(mActivity.getString(R.string.homepage_defriend));
                stringList.add(mActivity.getString(R.string.homepage_report_content));
            }

            customDialog
                    .setLayoutId(R.layout.dialog_attention_order)
                    .setConvertListener(new ViewConvertListener() {
                        @Override
                        public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                            RecyclerView recyclerView = holder.getView(R.id.recyclerView);
                            BaseQuickAdapter<String, BaseViewHolder> adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_attention_order, stringList) {
                                @Override
                                protected void convert(BaseViewHolder baseViewHolder, String s) {
                                    TextView textView = baseViewHolder.getView(R.id.tv_text);
                                    textView.setText(s);
                                    if (s.equals(mActivity.getString(R.string.homepage_top)) || s.equals(mActivity.getString(R.string.homepage_top_cancel))) {
                                        textView.setOnClickListener(v -> {
                                            dialog.dismiss();
                                            String mid = news_info.getString("id");
                                            setState(mid, isTop);
                                        });
                                    } else if (s.equals(mActivity.getString(R.string.homepage_delete))) {
                                        textView.setOnClickListener(v -> {
                                            dialog.dismiss();
                                            String id = news_info.getString("id");
                                            delete(id);
                                        });
//                                    } else if (s.equals(mActivity.getString(R.string.homepage_retrial))) {
//                                    } else if (s.equals(mActivity.getString(R.string.homepage_cancel_attention))) {
//                                    } else if (s.equals(mActivity.getString(R.string.homepage_defriend))) {
                                    } else if (s.equals(mActivity.getString(R.string.homepage_report_content))) {
                                        textView.setOnClickListener(v -> {
                                            dialog.dismiss();
                                            informOn(news_info);
                                        });
                                    }
                                }
                            };
                            recyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
                            recyclerView.setAdapter(adapter);
                            TextView title = holder.getView(R.id.tv_title);
                            title.setVisibility(View.GONE);
                            TextView cancel = holder.getView(R.id.tv_cancel);
                            cancel.setText(mActivity.getString(android.R.string.cancel));
                            cancel.setOnClickListener(v1 -> {
                                dialog.dismissAllowingStateLoss();
                            });
                        }
                    })
                    .setGravity(Gravity.BOTTOM)
                    .show(appCompatActivity.getSupportFragmentManager());
        }

    }

    /**
     * 举报
     */
    private void informOn(JSONObject news_infomation) {
        Bundle bundle = new Bundle();
        bundle.putString("config", news_infomation.toJSONString());
        Intent intent = new Intent(mActivity, NewsReportActivity.class);
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
    }

    /**
     * 分享
     */
    private void share(JSONObject news_infomation) {
        String share_url = SPUtils.getStringData(mActivity, "share_article_host", "") + "/wap/Article/desc?auto_code=" + SPUtils.getStringData(mActivity, "inviteCode", "") + "&id=" + news_infomation.getString("id");
        String tmp_str = news_infomation.getString("image_list");
        if (!tmp_str.startsWith("[")) tmp_str = "[" + tmp_str + "]";
        JSONArray tmp_image_list;
        try {
            tmp_image_list = JSONArray.parseArray(tmp_str);
        } catch (Exception e) {
            tmp_image_list = null;

        }

        ArrayList<String> image_list = new ArrayList<>();
        JSONArray tmp_arr = tmp_image_list.getJSONObject(0).getJSONArray("url_list");
        if (tmp_arr == null) tmp_arr = new JSONArray();
        for (int i = 0; i < tmp_arr.size(); i++) {
            image_list.add(tmp_arr.getJSONObject(i).getString("url"));
        }
        JuduohuiShareDialog shareDialog = new JuduohuiShareDialog(mActivity);
        shareDialog
                .setShareMessageTitle(news_infomation.getString("title"))
                .setShareMessageContent(news_infomation.getString("desc"))
                .setShareMessageUrl(share_url)
                .setShareMessageImage(image_list)
                .setShareListen(new JuduohuiShareDialog.ShareListen() {
                    @Override
                    public void onShareSuccess(int channel) {
                        if (channel == JuduohuiShareDialog.SHARE_CHANNEL_CLIPBOARD) {
                        }
                    }

                    @Override
                    public void onShareFailure(int channel) {
                        if (channel == JuduohuiShareDialog.SHARE_CHANNEL_CLIPBOARD) {
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                })
                .show();
    }

    /**
     * 启动评论
     */
    private void comment(JSONObject news_infomation) {
        Intent intent = new Intent(mActivity, NewsInformation.class);
        intent.putExtra("config", news_infomation.toJSONString());
        intent.putExtra("comment", true);
        mActivity.startActivity(intent);
    }

    /**
     * @param state 增加状态字段 1.点赞数加一（默认）0.点赞数减一
     * @param id    点赞 本地文章id
     */
    private void like(int state, int id, int pos) {
        RequestParams req = new RequestParams();
        req.put("state", state);
        req.put("id", id);
        HttpUtils.post(Constants.ADD_MEDIA_LIB_LIKE, mActivity, req, new HttpJsonResponse() {
            @Override
            protected void onSuccess(JSONObject response) {
                LogUtils.log("like onSuccess = " + response.toJSONString());
//                newsClickListens.update(pos);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
            }
        });
    }

    public interface NewsClickListens {
        void click(JSONObject information, int pos);

        void refresh();

        void update(int pos);

    }

    private void setLikeState(ImageView imageView, int did) {
        if (1 == did) {
            imageView.setEnabled(true);
            imageView.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN); // 修改颜色
        } else {
            imageView.setEnabled(false);
            imageView.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN); // 修改颜色
        }
    }

    private void setLikeState(int status, ImageView imageView) {
        if (1 == status) {
            imageView.setEnabled(true);
            imageView.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN); // 修改颜色
        } else {
            imageView.setEnabled(false);
            imageView.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN); // 修改颜色
        }
    }

    /**
     * 置顶或者取消
     *
     * @param mid    需置顶的文章id
     * @param is_top 是否置顶 1.置顶 0.不置顶（取消置顶） 默认为0
     */
    private void setState(String mid, int is_top) {
        int top = 0;
        if (is_top == 0) {
            top = 1;
        }

        RequestParams req = new RequestParams();
        req.put("is_top", top);
        req.put("mid", mid);
        HttpUtils.post(Constants.GET_MEDIA_TOP, mActivity, req, new HttpJsonResponse() {
            @Override
            protected void onSuccess(JSONObject response) {
                LogUtils.log("setState onSuccess = " + response.toJSONString());
                if (response.getIntValue("code") == 0) {
                    if (null != newsClickListens) {
                        newsClickListens.refresh();
                    }
                }
//                newsClickListens.update(pos);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
            }
        });

    }

    private void delete(String id) {
        CommonDialog.getInstance(mActivity).setTitle("删除转载文章提醒").setMessage("是否要删除此文章？").setSubmit("确定", true).setCancelButton("取消", false).setListener(new CommonDialog.CommonDialogListener() {
            @Override
            public void OnSubmit(AlertDialog dialog) {
                dialog.dismiss();
                RequestParams req = new RequestParams();
                req.put("media_id", id);
                HttpUtils.post(Constants.MEDIA_LIB_REPORT_DEL, mActivity, req, new HttpJsonResponse() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        super.onSuccess(response);
                        // Log.d(TAG, "onSuccess: " + response.toJSONString());
                        if (response.getIntValue("code") == 0) {
                            ToastUtils.showShortToast(mActivity, response.getString("msg"));
                            newsClickListens.refresh();
                        } else {
                            ToastUtils.showShortToast(mActivity, "删除失败，" + response.getString("msg"));
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                        super.onFailure(statusCode, headers, responseString, e);
                        ToastUtils.showShortToast(mActivity, "删除失败，" + responseString);
                    }
                });

            }

            @Override
            public void OnCancel(AlertDialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void OnDismiss() {

            }

            @Override
            public void OnClose(AlertDialog dialog) {

            }
        }).show();

    }
}