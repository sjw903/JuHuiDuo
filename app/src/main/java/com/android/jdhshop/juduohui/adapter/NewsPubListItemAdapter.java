package com.android.jdhshop.juduohui.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.juduohui.CommonDialog;
import com.android.jdhshop.juduohui.NewsPubListFragment;
import com.android.jdhshop.juduohui.response.HttpJsonResponse;
import com.android.jdhshop.utils.RelativeDateFormat;
import com.android.jdhshop.utils.WxUtil;
import com.bumptech.glide.Glide;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.loopj.android.http.RequestParams;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class NewsPubListItemAdapter extends RecyclerView.Adapter<NewsPubListItemAdapter.NewsItemHolder> {
    JSONArray news_array_list;
    int interval_nums;
    String TAG = getClass().getSimpleName();
    NewsPubListFragment.NewsClickListens newsClickListens;
    Activity mActivity;
    JSONArray adv_config;
    JSONArray adv_history = new JSONArray();
    public String current_type = "1";

    public NewsPubListItemAdapter(Activity activity, JSONArray news_array, NewsPubListFragment.NewsClickListens listens) {
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
    public NewsPubListItemAdapter.NewsItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Log.d(TAG, "onCreateViewHolder: " + parent.getWidth() + "," + parent.getMeasuredWidth());
        View view = LayoutInflater.from(mActivity).inflate(R.layout.news_pub_list_item, parent, false);
        return new NewsPubListItemAdapter.NewsItemHolder(view);
    }


    @Override
    public void onViewRecycled(NewsPubListItemAdapter.NewsItemHolder holder) {
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
    public void onBindViewHolder(NewsPubListItemAdapter.NewsItemHolder holder, int position) {
//        holder.setIsRecyclable(false); // ??????????????????????????????????????????????????????
        long j = getItemId(position);
        final String save_key = "position_" + j;
        JSONObject news_info = news_array_list.getJSONObject(position);
        holder.share_text.setTag(R.id.tag_pub_list_id, news_info.getString("id"));
        String news_title = news_info.getString("title");
        holder.share_text.setVisibility(View.GONE);
        holder.delete_text.setVisibility(View.VISIBLE);
        if (interval_nums!=0 && (position - 1) % interval_nums == 0 && position != 0 || position == 1) {
            JSONObject current_config = getConfigByKey(save_key);

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

                    @Override
                    public void click(View v) {
                        // Log.d(TAG, "click: ??????????????????");
                    }

                    @Override
                    public void dislike() {
                        // Log.d(TAG, "dislike: ????????????");
                    }

                    @Override
                    public void display(View v, String p, JSONObject current_adv_config) {
                        mActivity.runOnUiThread(() -> {
                            holder.news_adv_box.setVisibility(View.VISIBLE);
                            holder.news_adv_box_bt.setVisibility(View.VISIBLE);
                            error_count = 0;
                            if (holder.news_adv_box.getTag(R.id.tag_adv_position).toString().equals(p)) {
                                holder.news_adv_box.removeAllViews();
                                holder.news_adv_box.addView(v);
                                holder.news_adv_box.setTag(R.id.tag_adv_config, current_adv_config);
                                // ??????????????????????????????
                                Animation aa = new AlphaAnimation(0, 1);
                                aa.setDuration(1000);
                                holder.news_adv_box.setLayoutAnimation(new LayoutAnimationController(aa));

                                int intw = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                                int inth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                                v.measure(intw, inth);
                                int intwidth = v.getMeasuredWidth();
                                int intheight = v.getMeasuredHeight();


                                // Log.d(TAG, "??????????????????: " + intwidth + "," + intheight);
                                // Log.d(TAG, "??????????????????");
                                v.postDelayed(() -> {
                                    // Log.d(TAG, "???????????????111: - " + v.getMeasuredWidth() + "," + v.getMeasuredHeight());
                                }, 100);

                                v.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                                    @Override
                                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                                        if (v.getHeight() > 10 && v.isAttachedToWindow() && p.equals(save_key)) {
                                            current_adv_config.put("height", v.getHeight() + 16);
                                            current_adv_config.put("display", save_key);
                                            saveConfigByKey(save_key, current_adv_config);
                                            v.removeOnLayoutChangeListener(this);
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
                        // Log.d(TAG, "error: ??????????????????");

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

        if (!news_info.containsKey("state")) {
            holder.pub_info_box.setVisibility(View.GONE);
        } else {
            // {"image_list":"","title":"?????????????????????????????????19.9??????179?????????????????????????????????????????????","id":30099,"readnum":1,"state":2,"create_time":"14?????????","gold":11,"source_add_time":1634177987}
            // ???????????? 1.????????? 2.???????????? 3.??????????????? 4.?????????????????? 5.???????????? 6.??????????????? 7.?????????
            String ds_state = "";
            switch (news_info.getIntValue("state")) {
                case 1:
                    ds_state = "?????????";
                    break;
                case 2:
                case 5:
                    ds_state = "?????????";
                    if (news_info.getString("id").equals(holder.share_text.getTag(R.id.tag_pub_list_id))) {
                        holder.share_text.setVisibility(View.VISIBLE);
                        holder.share_text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //???????????????
                                // http://test.xinniankeji.com/wap/Article/desc?auth_code=???????????????&id=??????id
                                String share_url = SPUtils.getStringData(mActivity, "share_article_host", "") + "/wap/Article/desc?auto_code=" + SPUtils.getStringData(mActivity, "inviteCode", "") + "&id=" + news_info.getString("id");
                                WxUtil.sendCardMessage(mActivity, news_info.getString("title"), news_info.getString("desc"), share_url, WxUtil.WX_FRIEND);
                            }
                        });
                    }
                    break;
                case 3:
                    ds_state = news_info.getString("refuse_str") == null ? "???????????????" : "??????????????? :" + news_info.getString("refuse_str");
                    if (news_info.getString("id").equals(holder.share_text.getTag(R.id.tag_pub_list_id))) {
                        holder.share_text.setVisibility(View.VISIBLE);
                        holder.share_text.setText("??????");
                        holder.share_text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //????????????
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
                                            ToastUtils.showShortToast(mActivity, "???????????????" + response.getString("msg"));
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
                    ds_state = "??????????????????";
                    break;
                case 6:
                    ds_state = news_info.getString("refuse_str") == null ? "???????????????" : "???????????????: " + news_info.getString("refuse_str");
                    break;
                case 7:
                    ds_state = "?????????";
                    holder.news_title.setTextColor(Color.parseColor("#666666"));
                    news_title = "<s>" + news_info.getString("title") + "</s>";
                    holder.delete_text.setVisibility(View.GONE);
                    break;
                case 8:
                    ds_state = "???????????? :" + news_info.getString("report_str");
            }
            holder.status_text.setText(ds_state);
            // ?????????
            holder.delete_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Log.d(TAG, "onClick: ????????? " + news_info.getString("id"));

                    CommonDialog.getInstance(mActivity).setTitle("????????????????????????").setMessage("???????????????????????????").setSubmit("??????", true).setCancelButton("??????", false).setListener(new CommonDialog.CommonDialogListener() {
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
                                        ToastUtils.showShortToast(mActivity, "???????????????" + response.getString("msg"));
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                                    super.onFailure(statusCode, headers, responseString, e);
                                    ToastUtils.showShortToast(mActivity, "???????????????" + responseString);
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
            // ????????????????????????

        }
        holder.news_title.setText(Html.fromHtml(news_title));

        DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String pub_time =news_info.getString("create_time");
        Date date;
        try {
            date = fmt.parse(pub_time);

        }
        catch (Exception ignored){
            date = new Date();
        }
        String pub_format_time = RelativeDateFormat.format(date);

        holder.pub_info.setText(pub_format_time);//news_info.getString("create_time"));
        holder.pub_info_1.setText("?????????" + (news_info.getString("gold") != null ? news_info.getString("gold") : "0") + "??????");
        if (current_type.equals("1")) {
            holder.pub_info_2.setText(news_info.getString("readnum") + "??????");
        } else {
            holder.pub_info_2.setVisibility(View.GONE);
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
                newsClickListens.click(news_info);
            }
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
            // Log.d(TAG, "getItemCount: ????????????");
        }
        return news_array_list.size();
    }

    public static class NewsItemHolder extends RecyclerView.ViewHolder {
        public TextView news_title;
        public LinearLayout pub_info_box;
        public TextView pub_info; // ????????????
        public TextView pub_info_1; // ?????????????????????
        public TextView pub_info_2; // ????????????

        public TextView status_text; //????????????
        public TextView delete_text; // ????????????
        public TextView share_text; // ??????
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

            status_text = itemView.findViewById(R.id.status_text);
            delete_text = itemView.findViewById(R.id.delete_text);
            share_text = itemView.findViewById(R.id.share_text);
            pub_image = itemView.findViewById(R.id.pub_image);

        }
    }
}