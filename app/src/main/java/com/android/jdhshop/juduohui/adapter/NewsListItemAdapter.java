package com.android.jdhshop.juduohui.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.R;
import com.android.jdhshop.advistion.JuDuoHuiAdvertisement;
import com.android.jdhshop.advistion.entry.EntryActivity;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.juduohui.CommonDialog;
import com.android.jdhshop.juduohui.JuduohuiRewardActivity;
import com.android.jdhshop.juduohui.response.HttpJsonResponse;
import com.android.jdhshop.utils.StartActivityUtils;
import com.android.jdhshop.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.kwad.sdk.api.KsEntryElement;
import com.loopj.android.http.RequestParams;

import org.greenrobot.greendao.annotation.NotNull;

import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_OK;

public class NewsListItemAdapter extends RecyclerView.Adapter<NewsListItemAdapter.NewsItemHolder> {
    JSONArray news_array_list;
    // 信息流广告显示间隔
    int interval_number;
    String TAG = getClass().getSimpleName();
    NewsClickListens newsClickListens;
    Activity mActivity;
    JSONObject infomation_adv_object;
    JSONArray infomation_adv_config;
    JSONArray adv_history = new JSONArray();
    JSONObject content_adv_object;
    JSONArray content_adv_config;
    int news_type = -1;


    public NewsListItemAdapter(Activity activity, JSONArray news_array, NewsClickListens listens) {
        news_array_list = news_array;
        mActivity = activity;
        newsClickListens = listens;

    }

    public interface NewsClickListens {
        void click(JSONObject news_infomation);

        void load();
    }

    public static class InnerJuDuoHuiAdvertisement extends JuDuoHuiAdvertisement {
        NewsListItemAdapter.NewsItemHolder innerHolder;

        public void setHolder(NewsListItemAdapter.NewsItemHolder holder) {
            innerHolder = holder;
        }

        public NewsListItemAdapter.NewsItemHolder getHolder() {
            return innerHolder;
        }

        public InnerJuDuoHuiAdvertisement(Activity activity) {
            super(activity);
        }
    }

    /**
     * 设置当前显示的分类TYPE
     *
     * @param newsType int
     */
    public void setNewsType(int newsType) {
        news_type = newsType;
    }

    /**
     * 设置信息流广告配置 JSON OBJECT ，需包含配置列表【key = list】JSON ARRAY
     *
     * @param advConfig JSONObject
     */
    public void setInfomationAdvConfig(@NotNull JSONObject advConfig) {
        infomation_adv_object = advConfig;
        infomation_adv_config = infomation_adv_object.getJSONArray("list");
        interval_number = infomation_adv_object.getIntValue("interval_num");
    }

    /**
     * 设置视频入口位广告配置 JSON OBJECT，需包含配置列表【list】JSON ARRAY
     *
     * @param contentAdvConfig JSONObject
     */
    public void setContentAdvConfig(@NotNull JSONObject contentAdvConfig) {
        content_adv_object = contentAdvConfig;
        content_adv_config = content_adv_object.getJSONArray("list");
    }

    public void clearHistory() {
        adv_history.clear();
    }

    @Override
    public NewsListItemAdapter.NewsItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.news_list_item, parent, false);
        return new NewsListItemAdapter.NewsItemHolder(view);
    }

    @Override
    public void onViewRecycled(NewsListItemAdapter.NewsItemHolder holder) {
        if (holder != null) {

            // Log.d(TAG, "onViewRecycled: " + holder.news_adv_box.getTag(R.id.tag_adv_config));
            // Log.d(TAG, "onViewRecycled: position_id = " + holder.news_adv_box.getTag(R.id.news_list_item_adapter_position) +",w:" + holder.news_adv_box.getTag(R.id.tag_adv_width) + ",h:" + holder.news_adv_box.getTag(R.id.tag_adv_height));

            // 对 position_0 的进行特殊处理
            if (!"position_0".equals(holder.news_adv_box.getTag(R.id.news_list_item_adapter_position))) {
                holder.news_reward_adv.setVisibility(View.GONE);
            }

            if (holder.news_adv_box.getTag(R.id.tag_adv_height) !=null){
                // Log.d(TAG, "onViewRecycled: 即将设置高度：" + holder.news_adv_box.getTag(R.id.tag_adv_height) + " ..... " + UIUtils.px2dp((int)holder.news_adv_box.getTag(R.id.tag_adv_height)));
                holder.news_adv_box.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) UIUtils.px2dp((int)holder.news_adv_box.getTag(R.id.tag_adv_height))));
                holder.news_adv_box.removeAllViews();
            }


            // 回收时清除掉绑定时的TAG
            holder.news_adv_box.setTag(R.id.news_list_item_adapter_position, "");

            Glide.clear(holder.news_image);
            Glide.clear(holder.news_image_0);
            Glide.clear(holder.news_image_1);
            Glide.clear(holder.news_image_2);

            holder.news_image.setImageResource(R.drawable.no_banner);
            holder.news_image_0.setImageResource(R.drawable.no_banner);
            holder.news_image_0.setImageResource(R.drawable.no_banner);
            holder.news_image_0.setImageResource(R.drawable.no_banner);
            if (holder.news_adv_box.findViewById(R.id.news_list_item_top_adv) != null) {
//                holder.news_adv_box.removeAllViews();
                holder.news_adv_box.removeView(holder.news_adv_box.findViewById(R.id.news_list_item_top_adv));
            }
//            // Log.d(TAG, "onViewRecycled: " + holder.news_adv_box.getMe asuredWidth() + "," + holder.news_adv_box.getMeasuredHeight() + ",," + holder.news_adv_box.getMeasuredHeightAndState() + ",,,," + holder.news_adv_box.getTag());
//            holder.news_adv_box.setVisibility(View.GONE);

        }
        super.onViewRecycled(holder);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void getRewardAdv(final NewsListItemAdapter.NewsItemHolder holder, final String option_key) {
        RequestParams req = new RequestParams();
        req.put("identifys", "videos_money");
        HttpUtils.post(Constants.GET_ADVERTISEMENT_LIST,mActivity, req, new HttpJsonResponse() {
            @Override
            protected void onSuccess(JSONObject response) {
                super.onSuccess(response);
                if (response.getIntValue("code") == 0 && response.getJSONObject("data").getJSONObject("place_list") != null) {
                    for (String o : response.getJSONObject("data").getJSONObject("place_list").keySet()) {
                        if ("videos_money".equals(response.getJSONObject("data").getJSONObject("place_list").getJSONObject(o).getString("identify"))) {
                            int add_golds_coins = response.getJSONObject("data").getJSONObject("place_list").getJSONObject(o).getIntValue("hpoint");
                            add_golds_coins = add_golds_coins * 100;
                            JSONArray show_reward_video_cfg = response.getJSONObject("data").getJSONObject("place_list").getJSONObject(o).getJSONArray("list");
                            // 使用 news_adv_box 做为第0个广告填充。
                            if ("position_0".equals(holder.news_adv_box.getTag(R.id.news_list_item_adapter_position))) {


                                AlphaAnimation animation = new AlphaAnimation(0,1);
                                animation.setDuration(1000);
                                holder.news_adv_box.setLayoutAnimation(new LayoutAnimationController(animation));
                                holder.news_adv_box.removeAllViews();
                                holder.news_adv_box.setVisibility(View.VISIBLE);
                                View v = LayoutInflater.from(mActivity).inflate(R.layout.news_list_adv_btn, holder.news_adv_box, false);
                                v.setId(R.id.news_list_item_top_adv);
                                ImageButton show_reward_adv = v.findViewById(R.id.imageButton);
                                TextView view_add_coins = v.findViewById(R.id.view_add_coins);
                                view_add_coins.setText("+" + add_golds_coins + "金币");
                                show_reward_adv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent ii = new Intent(mActivity, JuduohuiRewardActivity.class);
                                        ii.putExtra("adv_config", response.getJSONObject("data").getJSONObject("place_list").getJSONObject(o).toJSONString());
                                        ii.putExtra("adv_unit", "golds");
//                                            mActivity.startActivityForResult(ii,8341);
                                        mActivity.startActivityFromChild(mActivity, ii, 8341);
                                        ((BaseActivity) mActivity).setOnActivityResultLisntener(new BaseActivity.onActivityResultLisntener() {
                                            @Override
                                            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                                                if (requestCode == 8341 && resultCode == RESULT_OK) {
                                                    // Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode + ", data=" + data);
                                                    CommonDialog commonDialog = new CommonDialog(mActivity);
                                                    commonDialog.getMessageContent().setVisibility(View.GONE);
                                                    commonDialog.setListener(new CommonDialog.CommonDialogListener() {
                                                        @Override
                                                        public void OnSubmit(AlertDialog dialog) {

                                                        }

                                                        @Override
                                                        public void OnCancel(AlertDialog dialog) {

                                                        }

                                                        @Override
                                                        public void OnDismiss() {
                                                            //notifyItemChanged(0);
                                                            holder.news_adv_box.setVisibility(View.GONE);
                                                        }

                                                        @Override
                                                        public void OnClose(AlertDialog dialog) {

                                                        }
                                                    });
                                                    View root_view = commonDialog.getRootView();
                                                    RelativeLayout golds_main = root_view.findViewById(R.id.add_golds_box);
                                                    golds_main.setVisibility(View.VISIBLE);
                                                    TextView desp_message = root_view.findViewById(R.id.desp_message);
                                                    desp_message.setText("恭喜您已获得观看视频奖励");
                                                    TextView desp_message_no = root_view.findViewById(R.id.desp_message_no);
                                                    desp_message_no.setText(String.valueOf(Integer.parseInt(data.getStringExtra("hpoint")) * 100));
                                                    TextView desp_message_add = root_view.findViewById(R.id.desp_message_add);
                                                    desp_message_add.setText("+");
                                                    TextView desp_message_unit = root_view.findViewById(R.id.desp_message_uni);
                                                    desp_message_unit.setText("金币");

                                                    root_view.findViewById(R.id.no_golds_button_box).setVisibility(View.GONE);
                                                    root_view.findViewById(R.id.golds_button_box).setVisibility(View.GONE);
                                                    commonDialog.show();
                                                }
                                            }
                                        });
                                    }
                                });
                                holder.news_adv_box.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                holder.news_adv_box.addView(v);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);
            }
        });
    }

    private void getInfomationAdv(final InnerJuDuoHuiAdvertisement juDuoHuiAdvertisement, final NewsItemHolder holder, final String save_key) {
        // Log.d(TAG, "getInfomationAdv: " + holder.news_adv_box.getTag(R.id.news_list_item_adapter_position));
        if (infomation_adv_config == null) return;

        if (holder.news_adv_box.getTag(R.id.tag_adv_config) != null && !"".equals(holder.news_adv_box.getTag(R.id.tag_adv_config)) && holder.news_adv_box.getTag(R.id.tag_adv_width) != null) {
            // Log.d(TAG, "onBindViewHolder: 当前广告位" + holder.news_adv_box.getTag(R.id.tag_adv_config));

            int current_adv_width = (int) holder.news_adv_box.getTag(R.id.tag_adv_width);
            int current_adv_height = (int) holder.news_adv_box.getTag(R.id.tag_adv_height);
            holder.news_adv_box.getLayoutParams().height = 350;

            if (holder.news_adv_box.getTag(R.id.tag_adv_width) != null && holder.news_adv_box.getTag(R.id.tag_adv_height) != null) {
                juDuoHuiAdvertisement.setHolder(holder);
                // Log.d(TAG, "getInfomationAdv: getInfomationAdvWithChannel" + holder.news_adv_box.getTag(R.id.tag_adv_config).toString());
                juDuoHuiAdvertisement.getInfomationAdvWithChannel(JSONObject.parseObject(holder.news_adv_box.getTag(R.id.tag_adv_config).toString()), save_key);
            }
        } else {
            juDuoHuiAdvertisement.setHolder(holder);
            // Log.d(TAG, "getInfomationAdv: getInfomationAdv");
            juDuoHuiAdvertisement.getInfomationAdv(infomation_adv_config, save_key);
        }
    }

    @Override
    public void onBindViewHolder(NewsListItemAdapter.NewsItemHolder holder, int position) {

        // Log.d(TAG, "onBindViewHolders: " + holder.getAdapterPosition() + "-" + holder.getItemId() + "-" + position);

        boolean need_display_ad = false;
        final InnerJuDuoHuiAdvertisement juDuoHuiAdvertisement = new InnerJuDuoHuiAdvertisement(mActivity);

        long j = getItemId(position);
        final String save_key = "position_" + j;
        final NewsItemHolder current_holder = holder;
        // 设置news_adv_box的TAG为 save_key,回收时去除掉
        // bind中所有逻辑均以此作为根据处理
        if (current_holder.news_adv_box.getTag(R.id.news_list_item_adapter_position) == null || "".equals(current_holder.news_adv_box.getTag(R.id.news_list_item_adapter_position))) {
            current_holder.news_adv_box.setTag(R.id.news_list_item_adapter_position, save_key);
        }
        // 配置广告的信息流回调监听器
        JuDuoHuiAdvertisement.InfomationAdListen listen = new JuDuoHuiAdvertisement.InfomationAdListen() {
            int error_count = 0;

            @Override
            public void click(View v) {
            }

            @Override
            public void dislike() {
            }

            @Override
            public void display(View v, String position, JSONObject current_adv_config) {
                // 当前holder 为
                // Log.d(TAG, "display: " + position + "..." + juDuoHuiAdvertisement.getHolder().getItemId() + " - " + juDuoHuiAdvertisement.getHolder().news_adv_box.getTag(R.id.news_list_item_adapter_position) );
                if (position.equals(juDuoHuiAdvertisement.getHolder().news_adv_box.getTag(R.id.news_list_item_adapter_position))) {
                    juDuoHuiAdvertisement.getHolder().news_adv_box.setTag(R.id.tag_adv_config, current_adv_config.toJSONString());
//                    v.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            juDuoHuiAdvertisement.getHolder().news_adv_box.setTag(R.id.tag_adv_width, v.getMeasuredWidth());
//                            juDuoHuiAdvertisement.getHolder().news_adv_box.setTag(R.id.tag_adv_height, v.getMeasuredHeight());
//                        }
//                    }, 200);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlphaAnimation animation = new AlphaAnimation(0,1);
                            animation.setDuration(1000);
                            animation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    // Log.d(TAG, "onAnimationEnd: " + v.getMeasuredWidth() + ", " + v.getWidth() + ","+v.getMeasuredHeight()+","+v.getHeight());
                                    juDuoHuiAdvertisement.getHolder().news_adv_box.setTag(R.id.tag_adv_width, v.getMeasuredWidth());
                                    juDuoHuiAdvertisement.getHolder().news_adv_box.setTag(R.id.tag_adv_height, v.getMeasuredHeight());
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            juDuoHuiAdvertisement.getHolder().news_adv_box.setLayoutAnimation(new LayoutAnimationController(animation));
                            juDuoHuiAdvertisement.getHolder().news_adv_box.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            juDuoHuiAdvertisement.getHolder().news_adv_box.removeAllViews();
                            juDuoHuiAdvertisement.getHolder().news_adv_box.addView(v);

                        }
                    });
                }
                else
                {
                    juDuoHuiAdvertisement.getHolder().news_adv_box.setVisibility(View.GONE);
                }
            }

            @Override
            public void displayed() {
                // Log.d(TAG, "displayed: " + juDuoHuiAdvertisement.getHolder().news_adv_box.getTag(R.id.news_list_item_adapter_position) + ",,," + juDuoHuiAdvertisement.getHolder().news_adv_box.getWidth() + ",h"+juDuoHuiAdvertisement.getHolder().news_adv_box.getHeight());
                if (juDuoHuiAdvertisement.getHolder().news_adv_box.getHeight()>16) {
                    juDuoHuiAdvertisement.getHolder().news_adv_box.setTag(R.id.tag_adv_width, juDuoHuiAdvertisement.getHolder().news_adv_box.getWidth());
                    juDuoHuiAdvertisement.getHolder().news_adv_box.setTag(R.id.tag_adv_height, juDuoHuiAdvertisement.getHolder().news_adv_box.getHeight());
                }
            }

            @Override
            public void close() {
            }

            @Override
            public void error(JSONObject error) {
                error_count = error_count + 1;
                // Log.d(TAG, "error: " + error + "," + save_key);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (error_count < infomation_adv_config.size()) {
                            juDuoHuiAdvertisement.getInfomationAdv(infomation_adv_config, error.getString("position"));
//                                    error_count = error_count +1;
                        } else {
                            current_holder.news_adv_box.setVisibility(View.GONE);
                        }
                    }
                });
            }
        };
        juDuoHuiAdvertisement.setInfomationAdListen(listen);
        // type类型为0 且 是第0个 ，看视频视图
        if (position == 0 && news_type == 0 && SPUtils.getBoolean(mActivity,"is_open_ad",true)) {
            need_display_ad = true;
            // 获取填充
            String token = SPUtils.getStringData(mActivity, "token", "");
            if (!"".equals(token)) {
                RequestParams req = new RequestParams();
                req.put("identify", "videos_money");
                HttpUtils.post(Constants.AD_IS_CAN_SHOW_REWARD, mActivity,req, new HttpJsonResponse() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        super.onSuccess(response);
                        // Log.d(TAG, "AD_IS_CAN_SHOW_REWARD onSuccess: " + response.toJSONString());
                        if (response.getIntValue("code") == 0) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getRewardAdv(current_holder, save_key);
                                }
                            });
                        }
                        else {
                            // Log.d(TAG, "onSuccess: 失败当前不能看视频");
                            postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    if ("position_0".equals(current_holder.news_adv_box.getTag(R.id.news_list_item_adapter_position))) {
                                        current_holder.news_adv_box.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                        super.onFailure(statusCode, headers, responseString, e);
                        // Log.d(TAG, "onFailure: " + responseString);
                    }
                });
            }
        }
        // 离底部还有5个的时候提前预加载
        if (position + 5 >= news_array_list.size()) newsClickListens.load();

        Log.d(TAG, "onBindViewHolder: " + content_adv_object.getIntValue("interval_num"));
        // 这里是视频入口
        if (news_type == 0 && content_adv_object != null && content_adv_config != null && holder.getAdapterPosition() == content_adv_object.getIntValue("interval_num")) {
            need_display_ad = true;
            juDuoHuiAdvertisement.setContentAdListen(new JuDuoHuiAdvertisement.ContentAdListen() {
                @Override
                public void display(View v, KsEntryElement ke) {
                    if (("position_" + content_adv_object.getIntValue("interval_num")).equals(holder.news_adv_box.getTag(R.id.news_list_item_adapter_position))) {
                        holder.news_content_adv.setVisibility(View.VISIBLE);
                        holder.news_content_adv.removeAllViews();
                        holder.news_content_adv.addView(ke.getEntryView(mActivity, new KsEntryElement.OnFeedClickListener() {
                            @Override
                            public void handleFeedClick(int i, int i1, View view) {
                                // Log.d(TAG, "handleFeedClick: ");
                            }
                        }), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    }
                }

                @Override
                public void error(JSONObject error) {
                    // Log.d(TAG, "getMediaContentAdv error: " + error);
                }

                @Override
                public void click(View v, JSONObject info) {
                    // Log.d(TAG, "getMediaContentAdv click: " + info);
                    StartActivityUtils.ActivityStartParam param = new StartActivityUtils.ActivityStartParam();
                    param.mActivity = mActivity;
                    param.mTargetActivityCls = EntryActivity.class;
                    param.mSourceView = v;
                    StartActivityUtils.startActivityForResult(0, param);

                }
            }).getMediaContentEntryAdv(content_adv_config, "news_content_adv");
        }
        else{
            holder.news_content_adv.setVisibility(View.GONE);
        }
        // 第1篇文章（起始为0）后显示第1个广告。 从此处之后按后台配置的间隔进行广告添加处理.
        if (position == 1) {
            need_display_ad = true;
            // Log.d(TAG, "onBindViewHolder: position = 1");
            holder.news_adv_box.setVisibility(View.VISIBLE);
            getInfomationAdv(juDuoHuiAdvertisement, holder, save_key);
        }

        if (interval_number == 0) interval_number = 1;
        if (position > 1 + interval_number) {
            // 广告按间隔进行显示
            if (position % interval_number == 0) {
                need_display_ad = true;
                holder.news_adv_box.setVisibility(View.VISIBLE);
                getInfomationAdv(juDuoHuiAdvertisement, holder, save_key);
            }
        }

        need_display_ad = SPUtils.getBoolean(mActivity,"is_open_ad",true);

        if (!need_display_ad) {
            holder.news_adv_box.removeAllViews();
            holder.news_adv_box.setVisibility(View.GONE);
        }

        JSONObject news_info = news_array_list.getJSONObject(position);
        holder.news_title.setText(news_info.getString("title"));
        if (news_info.getIntValue("is_reprint") == 1) {
            // 转载的
            holder.pub_info.setText(news_info.getString("nickname"));
        } else {
            // 爬的
            holder.pub_info.setText(news_info.getString("media_name"));
        }
        String tmp_image_list = news_info.getString("image_list");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsClickListens.click(news_info);
            }
        });

//        if (infomation_adv_config !=null && (position-1) % interval_number == 0 && position != 0 || position == 1) {
//            // Log.d(TAG, "加广告: "+position);
//            JSONObject current_config = getConfigByKey(save_key);
//            holder.news_adv_box.setVisibility(View.VISIBLE);
//            holder.news_adv_box.setTag(R.id.tag_adv_position, save_key);
//
//            if (current_config != null && current_config.getIntValue("height") != 0 && save_key.equals(current_config.getString("display"))){
//                holder.news_adv_box.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,current_config.getIntValue("height")));
//            }
//            else{
//                holder.news_adv_box.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            }
//
//                JuDuoHuiAdvertisement juDuoHuiAdvertisement = new JuDuoHuiAdvertisement(mActivity, null);
//                juDuoHuiAdvertisement.setInfomationAdListen(new JuDuoHuiAdvertisement.InfomationAdListen() {
//                    int error_count = 0;
//
//                    @Override
//                    public void click(View v) {
//                        // Log.d(TAG, "click: 广告被点击了");
//                    }
//
//                    @Override
//                    public void dislike() {
//                        // Log.d(TAG, "dislike: 不喜欢啊");
//                    }
//
//                    @Override
//                    public void display(View v, String p, JSONObject current_adv_config) {
//                        mActivity.runOnUiThread(() -> {
//                            error_count = 0;
//                            if (holder.news_adv_box.getTag(R.id.tag_adv_position).toString().equals(p)) {
//                                holder.news_adv_box.removeAllViews();
//                                holder.news_adv_box.addView(v);
//                                holder.news_adv_box.setTag(R.id.tag_adv_config, current_adv_config);
//                                // 保存已加载的广告配置
//                                Animation aa = new AlphaAnimation(0, 1);
//                                aa.setDuration(1000);
//                                holder.news_adv_box.setLayoutAnimation(new LayoutAnimationController(aa));
//
//                                int intw = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//                                int inth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//                                v.measure(intw, inth);
//                                int intwidth = v.getMeasuredWidth();
//                                int intheight = v.getMeasuredHeight();
//
//
//                                // Log.d(TAG, "计算后的宽高: " + intwidth + "," + intheight);
//                                // Log.d(TAG, "间隔间隔间隔");
//                                v.postDelayed(() -> {
//                                    // Log.d(TAG, "计算后宽高: - " + v.getMeasuredWidth() + "," + v.getMeasuredHeight());
//                                }, 100);
//
//                                v.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//                                    @Override
//                                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                                        if (v.getHeight() > 10 && v.isAttachedToWindow() && p.equals(save_key)) {
//                                            current_adv_config.put("height", v.getHeight() + 16);
//                                            current_adv_config.put("display", save_key);
//                                            saveConfigByKey(save_key, current_adv_config);
//                                            v.removeOnLayoutChangeListener(this);
//                                        }
//                                    }
//                                });
//                            } else {
////                            holder.news_adv_box.setVisibility(View.GONE);
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void displayed() {
//                    }
//
//                    @Override
//                    public void close() {
//                        holder.news_adv_box.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void error(JSONObject error) {
//                        // Log.d(TAG, "error: 重新加载广告");
//
//                        error_count = error_count + 1;
//                        mActivity.runOnUiThread(() -> {
//                            if (error_count < infomation_adv_config.size()) {
//                                juDuoHuiAdvertisement.getInfomationAdv(infomation_adv_config, error.getString("position"));
////                                    error_count = error_count +1;
//                            } else {
//                                holder.news_adv_box.setVisibility(View.GONE);
//                            }
//                        });
//                    }
//                });
//
//                if (current_config == null) {
//                    if (infomation_adv_config !=null) juDuoHuiAdvertisement.getInfomationAdv(infomation_adv_config, save_key);
//                } else {
//                    if (infomation_adv_config !=null)  juDuoHuiAdvertisement.getInfomationAdvWithChannel(current_config, save_key);
//                }
//
//        } else {
//            if (position!=0 && infomation_adv_config == null) holder.news_adv_box.setVisibility(View.GONE);
////            holder.news_adv_box.removeAllViews();
//        }


        if ("".equals(tmp_image_list)) {
            holder.news_image.setVisibility(View.GONE);
            holder.image_box_3.setVisibility(View.GONE);
            return;
        }

        if (tmp_image_list.startsWith("{")) {
            tmp_image_list = "[" + tmp_image_list + "]";
        }

        JSONArray image_list = JSONArray.parseArray(tmp_image_list);
        if (image_list.size() > 0) {
            if (image_list.size() < 3) {
                holder.itemView.findViewById(R.id.image_box_1).setVisibility(View.VISIBLE);
                holder.itemView.findViewById(R.id.image_box_big).setVisibility(View.GONE);
                TextView news_title = holder.itemView.findViewById(R.id.news_title_b1);
                news_title.setText(news_info.getString("title"));
                TextView news_pub_info = holder.itemView.findViewById(R.id.pub_info_b1);
                news_pub_info.setText(news_info.getString("media_name"));
                ImageView pic_img = holder.itemView.findViewById(R.id.news_image_b1);
                Glide.with(mActivity).load(image_list.getJSONObject(0).getString("url")).asBitmap().placeholder(R.drawable.no_banner).error(R.drawable.no_banner).into(pic_img);
            } else {
                holder.itemView.findViewById(R.id.image_box_1).setVisibility(View.GONE);
                holder.itemView.findViewById(R.id.image_box_big).setVisibility(View.VISIBLE);
                holder.image_box_3.setVisibility(View.VISIBLE);
                holder.news_image.setVisibility(View.GONE);
                Glide.with(mActivity).load(image_list.getJSONObject(0).getString("url")).asBitmap().placeholder(R.drawable.no_banner).error(R.drawable.no_banner).into(holder.news_image_0);
                Glide.with(mActivity).load(image_list.getJSONObject(1).getString("url")).asBitmap().placeholder(R.drawable.no_banner).error(R.drawable.no_banner).into(holder.news_image_1);
                Glide.with(mActivity).load(image_list.getJSONObject(2).getString("url")).asBitmap().placeholder(R.drawable.no_banner).error(R.drawable.no_banner).into(holder.news_image_2);
            }
        }
    }

    @Override
    public int getItemCount() {
        return news_array_list.size();
    }

    public JSONObject getConfigByKey(String key) {
        for (int i = 0; i < adv_history.size(); i++) {
            if (key.equals(adv_history.getJSONObject(i).getString("save_key"))) {
                return adv_history.getJSONObject(i).getJSONObject("config");
            }
        }
        return null;
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

    public static class NewsItemHolder extends RecyclerView.ViewHolder {
        public TextView news_title;
        public TextView pub_info;
        public ImageView news_image;
        public ImageView news_image_0;
        public ImageView news_image_1;
        public ImageView news_image_2;
        public LinearLayout image_box_3;
        public LinearLayout news_adv_box;
        public LinearLayout news_content_adv;
        public LinearLayout news_reward_adv;

        public NewsItemHolder(View itemView) {
            super(itemView);
            news_title = itemView.findViewById(R.id.news_title);
            pub_info = itemView.findViewById(R.id.pub_info);
            news_image = itemView.findViewById(R.id.news_images);
            news_image_0 = itemView.findViewById(R.id.news_images_0);
            news_image_1 = itemView.findViewById(R.id.news_images_1);
            news_image_2 = itemView.findViewById(R.id.news_images_2);
            image_box_3 = itemView.findViewById(R.id.image_box_3);
            news_adv_box = itemView.findViewById(R.id.news_adv_box);
            news_content_adv = itemView.findViewById(R.id.news_content_adv);
            news_reward_adv = itemView.findViewById(R.id.news_reward_adv);
        }
    }
}