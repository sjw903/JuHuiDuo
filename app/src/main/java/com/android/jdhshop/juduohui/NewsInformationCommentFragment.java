package com.android.jdhshop.juduohui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.advistion.JuDuoHuiAdvertisement;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.bean.MessageEvent;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.DaiLiFuWu;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.juduohui.adapter.NewsCommentAdapter;
import com.android.jdhshop.juduohui.adapter.NewsListItemAdapter;
import com.android.jdhshop.juduohui.adapter.NewsRecommendItemAdapter;
import com.android.jdhshop.juduohui.response.HttpJsonResponse;
import com.android.jdhshop.login.BindPhoneActivity;
import com.android.jdhshop.login.WelActivity;
import com.android.jdhshop.utils.AesUtils;
import com.android.jdhshop.utils.MyScrollView;
import com.android.jdhshop.utils.OkHttpUtils;
import com.android.jdhshop.utils.RelativeDateFormat;
import com.android.jdhshop.utils.WxUtil;
import com.android.jdhshop.widget.CircleImageView;
import com.bumptech.glide.Glide;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class NewsInformationCommentFragment extends BaseLazyFragment {

    private JSONObject config;

    @BindView(R.id.infomation_body_box)
    RelativeLayout infomation_body_box;
    @BindView(R.id.i_share)
    ImageView i_share;
    @BindView(R.id.pinglun_text)
    TextView pinglun_Text;
    @BindView(R.id.pinglun_text_big)
    EditText pinglun_text_big;
    @BindView(R.id.i_like)
    ImageView i_like;
    @BindView(R.id.pinglun_box)
    LinearLayout pinglun_box;
    @BindView(R.id.pinglun_box_big)
    LinearLayout pinglun_box_big;
    @BindView(R.id.pub)
    TextView pub_comment;

    @BindView(R.id.all_farther)
    RelativeLayout all_farther;
    @BindView(R.id.comment_list)
    RecyclerView comment_list;

    @BindView(R.id.smart_refresh)
    SmartRefreshLayout smart_refresh;

    @BindView(R.id.main_scroll_view)
    MyScrollView main_scroll_view;


    @BindView(R.id.news_top_adv)
    LinearLayout news_top_adv; // 头部广告盒子

    @BindView(R.id.comment_empty_box)
    LinearLayout comment_empty_box;

    @BindView(R.id.open_pl_box)
    Button open_pl_box;


    JSONArray adv_config;
    ACache aCache;

    int icon_size = 50;
    int comment_page_size = 20;
    int current_page = 1;
    String news_id = "0";
    String remote_id = "";
    int adv_interval = 1;

    private NewsCommentAdapter newsCommentAdapter;
    private final JSONArray comments = new JSONArray();

    private JuDuoHuiAdvertisement advertisement = null;
    String user_token = "";
    int fid = 0;
    int current_replay_index;
    JSONObject current_reply_message;
    boolean is_loading = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news_comment,container,false);
        ButterKnife.bind(this,v);

        return v;
    }

    @Override
    protected void lazyload() {
        initUI();
        initData();
        initListener();
        EventBus.getDefault().register(this);
    }

    protected void initUI() {

        Intent information = getActivity().getIntent();
        String config_str = information.getStringExtra("config");
        config = JSONObject.parseObject(config_str);
        Log.d(TAG, "initUI: " + config_str);
        try {
            news_id = config.getString("id");
        } catch (Exception e) {
            getActivity().finish();
        }

        /* 评论 */
        Drawable i_write_pl = getResources().getDrawable(R.drawable.ic_write_pl);
        i_write_pl.setBounds(0, 0, icon_size, icon_size);
        pinglun_Text.setCompoundDrawables(i_write_pl, null, null, null);

        // 新闻评论列表
        newsCommentAdapter = new NewsCommentAdapter(getActivity(), comments, new NewsCommentAdapter.CommentLikeListen() {
            @Override
            public void load() {
                if (!is_loading) {
                    is_loading = true;
                    current_page = current_page + 1;
                    getComment();
                }
            }

            @Override
            public void like(int comment_id, int like_state, int index) {
                RequestParams req = new RequestParams();
                req.put("id",comment_id);
                req.put("state",like_state);
                HttpUtils.post(Constants.ADD_MEDIA_LIB_COMMENT_LIKE, this, req, new HttpJsonResponse() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        super.onSuccess(response);
                        if (response.getIntValue("code") == 0){
                            // 操作成功，设置状态
                            JSONObject comment_obj = comments.getJSONObject(index);
                            Log.d(TAG, "onSuccess: " + comment_obj);
                            comments.getJSONObject(index).put("did",like_state);
                            int digg_count = comments.getJSONObject(index).getIntValue("digg_count");
                            if (like_state == 1){ digg_count = digg_count+1; } else{ digg_count = digg_count-1; }
                            comments.getJSONObject(index).put("digg_count", digg_count);
                            newsCommentAdapter.notifyItemChanged(index);
                        }
                        else{
                            // 操作失败，TOAST提醒
                            showToast(response.getString("msg"));
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                        super.onFailure(statusCode, headers, responseString, e);
                    }
                });

            }
        });//, info_body);
        comment_list.setAdapter(newsCommentAdapter);
        comment_list.setHasFixedSize(false);
        comment_list.setNestedScrollingEnabled(false);
        comment_list.setFocusable(false);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        comment_list.setLayoutManager(manager);
    }

    protected void initData() {
        aCache = ACache.get(getActivity());
        user_token = SPUtils.getStringData(getActivity(), "token", "");
        getAdvConfig();
        getComment();
    }

    // 获取广告配置
    private void getAdvConfig() {
        RequestParams req = new RequestParams();
        req.put("identifys", "article_top");
        HttpUtils.post(Constants.GET_ADVERTISEMENT_LIST, getActivity(), req, new HttpJsonResponse() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);

            }

            @Override
            protected void onSuccess(JSONObject response) {
                super.onSuccess(response);
                if (response.getIntValue("code") == 0) {
                    for (String key : response.getJSONObject("data").getJSONObject("place_list").keySet()) {
                        JSONObject tmp = response.getJSONObject("data").getJSONObject("place_list").getJSONObject(key);
                        if ("article_top".equals(tmp.getString("identify"))) {
                            adv_config = tmp.getJSONArray("list");
                            adv_interval = tmp.getIntValue("interval_num");
                        }
                    }

                    if (adv_config != null && adv_config.size() > 0) {

                        newsCommentAdapter.setAdvConfig(adv_config);
                        newsCommentAdapter.setInterval(adv_interval);
                        newsCommentAdapter.notifyDataSetChanged();

                        advertisement = new JuDuoHuiAdvertisement(getActivity(), new JuDuoHuiAdvertisement.InfomationAdListen() {
                            int error_count = 0;

                            @Override
                            public void click(View v) {

                            }

                            @Override
                            public void dislike() {

                            }

                            @Override
                            public void display(View v, String position, JSONObject config) {
                                if (position == null) return;
                                error_count = 0;
                                getActivity().runOnUiThread(() -> {
                                    // Log.d(TAG, "display: " + position + " [广告配置] " + config  );
                                    AlphaAnimation animation = new AlphaAnimation(0, 1);
                                    animation.setDuration(500);
                                    LayoutAnimationController animationController = new LayoutAnimationController(animation);

                                    news_top_adv.setLayoutAnimation(animationController);
                                    news_top_adv.addView(v);
                                });
                            }

                            @Override
                            public void displayed() {

                            }

                            @Override
                            public void close() {

                            }

                            @Override
                            public void error(JSONObject error) {
                                // Log.d(TAG, "newsInfomation error: " + error.toJSONString());
                                error_count = error_count + 1;

                                getActivity().runOnUiThread(() -> {
                                    if (error_count < adv_config.size()) {
                                        advertisement.getInfomationAdv(adv_config, error.getString("position"));
                                    } else {
                                        news_top_adv.setVisibility(View.GONE);
                                    }
                                });

                            }
                        });
                        advertisement.getInfomationAdv(adv_config, "news_top_adv");
                    }
                } else {
                    // Log.d(TAG, "onSuccess: 获取广告配置失败");
                }
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    protected void initListener() {
        smart_refresh.setEnableNestedScroll(true);
        smart_refresh.setEnableOverScrollBounce(false);
        smart_refresh.setEnableRefresh(true);
        smart_refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (!is_loading) {
                    current_page = current_page + 1;
                    getComment();
                }
                else{
                    smart_refresh.finishLoadMore();
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smart_refresh.setEnableLoadMore(true);
                current_page = 1;
                comments.clear();
                newsCommentAdapter.notifyDataSetChanged();
                if (adv_config != null && adv_config.size() > 0) {
                    news_top_adv.removeAllViews();
                    advertisement.getInfomationAdv(adv_config,"news_top_adv");
                }
                getComment();
            }
        });

        pub_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log.d(TAG, "onClick: 发布按钮点击");
                if (pinglun_text_big.getText().toString().length() < 2) {
                    showToast("请输入评论内容");
                    return;
                }

                Log.d(TAG, "onClick: " + CaiNiaoApplication.getUserInfoBean().user_msg);
                Log.d(TAG, "onClick: " + CaiNiaoApplication.getUserInfoBean().user_msg.phone);
                if (CaiNiaoApplication.getUserInfoBean().user_msg.phone == null || CaiNiaoApplication.getUserInfoBean().user_msg.phone.equals("") || CaiNiaoApplication.getUserInfoBean().user_msg.phone.length() <11){
                    showToast("必须绑定手机号后才能参与评论。");
                    openActivity(BindPhoneActivity.class);
                    return;
                }

                RequestParams req = new RequestParams();
                req.put("id", config.getString("id"));
                req.put("content", pinglun_text_big.getText().toString());
                req.put("fid",fid);

                HttpUtils.post(Constants.ADD_MEDIA_LIB_COMMENT, getActivity(), req, new HttpJsonResponse() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        super.onSuccess(response);
                        // Log.d(TAG, "onSuccess: " + response.toJSONString());
                        if (response.getIntValue("code") == 0) {
                            pinglun_box_big.clearFocus();
                            pinglun_box_big.setVisibility(View.GONE);

                            if (comment_empty_box.getVisibility() == View.VISIBLE){
                                comment_empty_box.setVisibility(View.GONE);
                            }

                            JSONObject insert_obj = response.getJSONObject("data");
                            DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String pub_time =insert_obj.getString("create_time");
                            try {
                                Date date = fmt.parse(pub_time);
                                String pub_format_time = RelativeDateFormat.format(date);
                                insert_obj.put("create_time",pub_format_time);
                            }
                            catch (Exception ignored){}

                            if (fid != 0){
                                comments.remove(current_replay_index);
                                JSONArray sub_replay = current_reply_message.getJSONArray("child");
                                if (sub_replay == null) sub_replay = new JSONArray();
                                sub_replay.add(0,insert_obj);
                                comments.add(current_replay_index,current_reply_message);
                                newsCommentAdapter.notifyItemChanged(current_replay_index);
                            }
                            else {
                                comments.add(0, insert_obj);
                                newsCommentAdapter.notifyItemInserted(0);
                            }
                            fid =0;
                            current_reply_message = null;
                            current_replay_index = 0;
                        }

                        showToast(response.getString("msg"));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                        super.onFailure(statusCode, headers, responseString, e);
                        e.printStackTrace();
                        showToast("评论数据提交时发生未知错误，请稍后尝试！");
                        Log.d(TAG, "onFailure: " + responseString);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        pinglun_text_big.setHint("友善是交流的起点");
                    }
                });
            }
        });

    }

    private void openActivityForResult(Class tmp,Bundle b,int r){
        Intent i = new Intent(getActivity(),tmp);
        startActivityForResult(i,r,b);
    }
    @OnClick({R.id.pinglun_text, R.id.all_farther, R.id.pinglun_box_big, R.id.i_share, R.id.i_like,R.id.open_pl_box})
    public void onViewClicked(View view) {
        Intent intent = new Intent(getActivity(),WelActivity.class);
        switch (view.getId()) {
            case R.id.i_share:
//                // Log.d(TAG, "onViewClicked: 分享" + config.toJSONString()); id
                // http://test.xinniankeji.com/wap/Article/desc?auth_code=用户邀请码&id=文章id
//                String share_url = SPUtils.getStringData(getActivity(), "share_article_host", "") + "/wap/Article/desc?auto_code=" + SPUtils.getStringData(getActivity(), "inviteCode", "") + "&id=" + config.getString("id");
//                WxUtil.sendCardMessage(getActivity(), config.getString("title"), config.getString("desc"), share_url, WxUtil.WX_FRIEND);
                EventBus.getDefault().post(new MessageEvent("share_message"));
                break;
            case R.id.i_like:
                if ("".equals(user_token)) {
                    openActivityForResult(WelActivity.class, null, 7777);
                    return;
                }

                int like_state = 0; //
                if (i_like.getTag() == "i_like") {
                    i_like.setTag("normal");
                    i_like.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN); // 修改颜色
                    like_state = 0;
                } else {
                    i_like.setTag("i_like");
                    i_like.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN); // 修改颜色
                    like_state = 1;
                }
                RequestParams req = new RequestParams();
                req.put("state", like_state);
                req.put("id", config.getString("id"));
                HttpUtils.post(Constants.ADD_MEDIA_LIB_LIKE, getActivity(), req, new HttpJsonResponse() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        super.onSuccess(response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                        super.onFailure(statusCode, headers, responseString, e);
                    }
                });
                break;
            case R.id.pinglun_box_big:
                pinglun_text_big.clearFocus();
                pinglun_box_big.setVisibility(View.GONE);
                break;
            case R.id.open_pl_box:
            case R.id.pinglun_text:
                Log.d(TAG, "onViewClicked: 会触发这里么？");
                if ("".equals(user_token)) {
                    openActivityForResult(WelActivity.class, null, 7777);
                    return;
                }

                // Log.d(TAG, "onViewClicked: 展开大评论");
                fid = 0;
                expandPubComment();
                break;
        }
    }
    private void expandPubComment(){
        pinglun_box_big.setVisibility(View.VISIBLE);
        pinglun_text_big.setText("");
        pinglun_text_big.clearComposingText();
        pinglun_text_big.setVisibility(View.VISIBLE);
        pinglun_text_big.requestFocus();
        InputMethodManager mSoftInputManager = (InputMethodManager) pinglun_text_big.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mSoftInputManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void getComment() {

        is_loading = true;
        RequestParams req = new RequestParams();
        req.put("id",config.getString("id"));
        req.put("page",current_page);
        req.put("per",20);
        req.put("fid",fid);
        Log.d(TAG, "getComment: " + req);

        HttpUtils.post(Constants.GET_XH_MEDIA_LIB_COMMENT_LIST,this, req,new HttpJsonResponse(){
            @Override
            protected void onSuccess(JSONObject response) {
                super.onSuccess(response);
                Log.d(TAG, "onSuccess: " + response);
                if (response.getIntValue("code") == 0){

                    if (response.getJSONObject("list").getJSONArray("data").size() < 1 && current_page == 1){
                        comment_empty_box.setVisibility(View.VISIBLE);
                        smart_refresh.finishLoadMoreWithNoMoreData();
                    }
                    else if (response.getJSONObject("list").getJSONArray("data").size() < 1 && current_page != 1){
                        smart_refresh.finishLoadMoreWithNoMoreData();
                    }
                    else {
                        int add_range_start = comments.size();
                        comments.addAll(response.getJSONObject("list").getJSONArray("data"));
                        comment_empty_box.setVisibility(View.GONE);
                        if (current_page == 1) {
                            newsCommentAdapter.notifyDataSetChanged();
                        }
                        else{
                            newsCommentAdapter.notifyItemRangeInserted(add_range_start,response.getJSONObject("list").getJSONArray("data").size());
                        }
                    }
                }
                else{
                    showToast(response.getString("msg"));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);
                showToast("获取评论信息出错，请稍后再试");
                Log.d(TAG, "onFailure: " + responseString);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                is_loading = false;
                smart_refresh.finishRefresh();
                smart_refresh.finishLoadMore();
            }
        });

    }
    @Subscribe(threadMode = ThreadMode.POSTING, priority = 9)
    public void onMessage(MessageEvent event){
        if (event.getMessage().equals("reply_message")){
            current_replay_index = event.getPosition();
            current_reply_message = comments.getJSONObject(current_replay_index);
            fid = current_reply_message.getIntValue("id");
            expandPubComment();
            pinglun_text_big.setHint("回复"+ current_reply_message.getString("nickname")+":");
        }
    }


}