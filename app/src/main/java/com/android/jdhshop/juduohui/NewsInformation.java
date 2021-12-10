package com.android.jdhshop.juduohui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.MainActivity;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.SplashActivity;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.MessageEvent;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.juduohui.dialog.JuduohuiShareDialog;
import com.android.jdhshop.utils.PMUtil;
import com.android.jdhshop.utils.UIUtils;
import com.android.jdhshop.widget.indicator.MagicIndicator;
import com.android.jdhshop.widget.indicator.ViewPagerHelper;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.CommonNavigator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文章详情
 */
public class NewsInformation extends BaseActivity {
    @BindView(R.id.go_back_button)
    ImageView go_back_button;
    @BindView(R.id.go_share_button)
    ImageView go_share_button;
    @BindView(R.id.top_indicator)
    MagicIndicator top_indicator;
    @BindView(R.id.news_pager)
    ViewPager news_pager;
    Activity mActivity;
    String[] mTitle = new String[]{"文章", "聊一聊"};
    JSONObject newsMessage;
    List<Fragment> fragments = new ArrayList<>();
    /**
     * 启动为了评论
     */
    private boolean isLaunchForComment;

    protected void initUI() {
        setContentView(R.layout.activity_news_information);
        ButterKnife.bind(this);
        mActivity = this;
    }

    @Override
    protected void initData() {
        Intent trans_intent = getIntent();
        isLaunchForComment = trans_intent.getBooleanExtra("comment", false);
        if (trans_intent == null) {
            showToast("乱看啥");
        } else {
            newsMessage = JSON.parseObject(trans_intent.getStringExtra("config"));
            Log.d(TAG, "initData: " + newsMessage);
        }

        Fragment news_content_fragment = new NewsInformationContentFragment();
        Fragment news_comment_fragment = new NewsInformationCommentFragment();
        fragments.add(news_content_fragment);
        fragments.add(news_comment_fragment);
        news_pager.setOffscreenPageLimit(mTitle.length);

        news_pager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });


        CommonNavigatorAdapter commonNavigatorAdapter = new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTitle.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int index) {
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context) {
                    @Override
                    public void onSelected(int index, int totalCount) {
                        super.onSelected(index, totalCount);
                        setTextSize(UIUtils.dp2px(16));
                    }

                    @Override
                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
                        super.onLeave(index, totalCount, leavePercent, leftToRight);
                        setTextSize(UIUtils.dp2px(16));
                    }
                };
                clipPagerTitleView.setText(mTitle[index]);
                clipPagerTitleView.setTextColor(Color.parseColor("#333333")); // 普通的文字
                clipPagerTitleView.setTextSize(UIUtils.dp2px(16));
                // 左 上 右 下
                clipPagerTitleView.setPadding(UIUtils.dp2px(10), UIUtils.dp2px(8), UIUtils.dp2px(10), UIUtils.dp2px(16));
                clipPagerTitleView.setClipColor(Color.parseColor("#FF5722"));
                clipPagerTitleView.setOnClickListener(view -> {
                    news_pager.setCurrentItem(index);
                });


                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setLineWidth(UIUtils.dp2px(12));
                indicator.setLineHeight(UIUtils.dp2px(3));
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setColors(getResources().getColor(R.color.aliuser_edittext_bg_color_activated));
                return indicator;
            }
        };
        CommonNavigator commonNavigator = new CommonNavigator(mActivity);
        commonNavigator.setAdapter(commonNavigatorAdapter);
        commonNavigator.setSkimOver(true);
        top_indicator.setNavigator(commonNavigator);


        ViewPagerHelper.bind(top_indicator, news_pager);
        if (isLaunchForComment) {
            news_pager.setCurrentItem(1);
        } else {
            news_pager.setCurrentItem(0);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if ("switch_to_comment".equals(event.getMessage())) {
            news_pager.setCurrentItem(1);
        } else if ("switch_to_information".equals(event.getMessage())) {
            news_pager.setCurrentItem(0);
        } else if ("share_message".equals(event.getMessage())) {
            Log.d(TAG, "onMessageEvent: 收到分享的消息");
            displayShare();
        }
    }

    @Override
    protected void initListener() {

    }

    @OnClick({R.id.go_back_button, R.id.go_share_button})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go_back_button:
                finish();
                break;
            case R.id.go_share_button:
                displayShare();
        }
    }

    private void displayShare() {
        String img_url = "";
        String share_url = SPUtils.getStringData(mActivity, "share_article_host", "") + "/wap/Article/desc?auto_code=" + SPUtils.getStringData(mActivity, "inviteCode", "") + "&id=" + newsMessage.getString("id");
        Log.d(TAG, "onClick: 不知道为啥，就是不能直接获取JSONARRAY");
        String tmp_str = newsMessage.getString("image_list");
        if (!tmp_str.startsWith("[")) tmp_str = "[" + tmp_str + "]";
        JSONArray tmp_image_list;
        try {
            tmp_image_list = JSONArray.parseArray(tmp_str);
        } catch (Exception e) {
            tmp_image_list = null;

        }

        ArrayList<String> image_list = new ArrayList<>();
        Log.d(TAG, "displayShare: " + tmp_image_list);
        JSONArray tmp_arr = tmp_image_list.getJSONObject(0).getJSONArray("url_list");
        if (tmp_arr == null) tmp_arr = new JSONArray();
        for (int i = 0; i < tmp_arr.size(); i++) {
            image_list.add(tmp_arr.getJSONObject(i).getString("url"));
        }
        JuduohuiShareDialog shareDialog = new JuduohuiShareDialog(mActivity);
        shareDialog
                .setShareMessageTitle(newsMessage.getString("title"))
                .setShareMessageContent(newsMessage.getString("desc"))
                .setShareMessageUrl(share_url)
                .setShareMessageImage(img_url)
                .setShareMessageImage(image_list)
                .setShareListen(new JuduohuiShareDialog.ShareListen() {
                    @Override
                    public void onShareSuccess(int channel) {
                        if (channel == JuduohuiShareDialog.SHARE_CHANNEL_CLIPBOARD) {
                            showToast("复制成功!");
                        }
                    }

                    @Override
                    public void onShareFailure(int channel) {
                        if (channel == JuduohuiShareDialog.SHARE_CHANNEL_CLIPBOARD) {
                            showToast("复制失败!");
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                })
                .show();
    }

    /**
     * 退出检测是否启动过应用
     * 没有启动过应用就走欢迎界面,有的话就正常栈的操作不用处理
     */
    @Override
    public void onBackPressed() {
        boolean launched = PMUtil.getInstance().containAc(MainActivity.class);
        if (!launched) {
            Intent open_intent = new Intent();
            open_intent.setClass(this, SplashActivity.class);
            startActivity(open_intent);
            //等待欢迎界面启动,然后关闭
            new Handler(getMainLooper()).postDelayed(super::onBackPressed, 2000L);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7777 && resultCode == RESULT_OK) {
            super.recreate();
        }
    }

}