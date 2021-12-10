package com.android.jdhshop.juduohui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.bean.LikeBean;
import com.android.jdhshop.bean.UserHomePageBean;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.dialog.CustomDialog;
import com.android.jdhshop.dialog.FullScreenDialog;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.juduohui.HomePageSearchActivity;
import com.android.jdhshop.juduohui.adapter.NewsPagerAdapter;
import com.android.jdhshop.juduohui.response.HttpJsonResponse;
import com.android.jdhshop.utils.Base64Utils;
import com.android.jdhshop.utils.MyScrollView;
import com.android.jdhshop.utils.UIUtils;
import com.android.jdhshop.utils.WxUtil;
import com.android.jdhshop.widget.CircleImageView;
import com.android.jdhshop.widget.indicator.MagicIndicator;
import com.android.jdhshop.widget.indicator.ViewPagerHelper;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.CommonNavigator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.titles.ClipPagerTitleView;
import com.android.jdhshop.wmm.QQShareUtil;
import com.blankj.utilcode.util.ClickUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.ViewConvertListener;
import com.shehuan.nicedialog.ViewHolder;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class HomePageOtherFragment extends BaseLazyFragment {
    private static final String KEY_DATA = "userInfo";

    @BindView(R.id.img_bg)
    ImageView imgBg;

    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.iv_head)
    CircleImageView ivHead;

    @BindView(R.id.iv_search)
    ImageView ivSearch;

    @BindView(R.id.iv_more)
    ImageView ivMore;


    @BindView(R.id.tv_production)
    TextView tvProduction;

    @BindView(R.id.tv_like)
    TextView tvLike;

    @BindView(R.id.tv_fans)
    TextView tvFans;

    @BindView(R.id.tv_attention)
    TextView tvAttention;

    @BindView(R.id.tv_attention_name)
    TextView tvAttentionName;

    @BindView(R.id.tv_sex)
    TextView tvSex;

    @BindView(R.id.tv_name)
    TextView tvName;

    @BindView(R.id.tv_follow)
    TextView tv_follow;


    @BindView(R.id.img_arrow)
    ImageView img_arrow;

    @BindView(R.id.tab_bar)
    MagicIndicator tab_bar;

    @BindView(R.id.tab_body)
    ViewPager tab_body;

    @BindView(R.id.ll_recommend)
    LinearLayout ll_recommend;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private UserHomePageBean userHomePageBean;
    private Gson gson = new Gson();

    List<String> tabs = Arrays.asList("全部", "文章");
    List<Fragment> tabs_body = new ArrayList<>();
    CommonNavigatorAdapter tab_menus_adapter;
    String FragmentScroll = "no";
    private long tabbar_last_click_time = 0;
    private View tabbar_last_click_v;
    private boolean arrowDown = true;
    private boolean isFollow = false;
    private BaseQuickAdapter<LikeBean, BaseViewHolder> baseViewHolderBaseQuickAdapter;
    private List<Pair<String, Integer>> pairList = new ArrayList<>();
    private String shareBaseUrl;

    {
        pairList.add(new Pair<>("名片分享", R.mipmap.icon_card_share));
        pairList.add(new Pair<>("微信", R.mipmap.icon_weixin));
        pairList.add(new Pair<>("朋友圈", R.mipmap.icon_friend));
        pairList.add(new Pair<>("QQ", R.mipmap.icon_qq));
        pairList.add(new Pair<>("QQ空间", R.mipmap.icon_qqzone));
    }

    private void initTabBar() {
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setSkimOver(true);
        commonNavigator.setAdjustMode(true);
        tab_menus_adapter = new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return tabs.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
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
                clipPagerTitleView.setText(tabs.get(index));
                clipPagerTitleView.setTextColor(Color.parseColor("#333333")); // 普通的文字
                clipPagerTitleView.setTextSize(UIUtils.dp2px(16));
                // 左 上 右 下
                clipPagerTitleView.setPadding(UIUtils.dp2px(10), UIUtils.dp2px(8), UIUtils.dp2px(10), UIUtils.dp2px(16));
                clipPagerTitleView.setClipColor(Color.parseColor("#FF5722"));

                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tabbar_last_click_time == 0 || tabbar_last_click_v == null) {
                            tabbar_last_click_v = v;
                            tabbar_last_click_time = System.currentTimeMillis();
                        } else {
                            // 如果上次点击的是这个v,并且两次点击时间间隔小号500，则认为是双击
                            if (tabbar_last_click_v == v && System.currentTimeMillis() - tabbar_last_click_time < 500) {
                                Bundle bundle = new Bundle();
                                bundle.putString("refresh", "yes");
                                tabs_body.get(index).setArguments(bundle);
                                tabbar_last_click_v = v;
                                tabbar_last_click_time = System.currentTimeMillis();
                                return;
                            } else {
                                tabbar_last_click_v = v;
                                tabbar_last_click_time = System.currentTimeMillis();
                            }

                        }

                        // Log.d(TAG, "onClick: " + index);
                        tab_body.setCurrentItem(index);
                        Bundle b = new Bundle();
                        b.putString("scroll", FragmentScroll);
                        tab_body.postDelayed(() -> {
                            tabs_body.get(index).setArguments(b);
                        }, 500);

                    }
                });

                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setLineWidth(UIUtils.dp2px(33));
                indicator.setLineHeight(UIUtils.dp2px(3));
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setColors(getResources().getColor(R.color.aliuser_edittext_bg_color_activated));
                return indicator;
            }
        };
        commonNavigator.setAdapter(tab_menus_adapter);
        tab_bar.setNavigator(commonNavigator);
        Fragment fragmentAll = new HomePageFragmentNews();
        Bundle bundle = new Bundle();
        bundle.putString("style_id", "4");
        bundle.putInt("auth_code", userHomePageBean.auth_code);
        bundle.putInt("type", 0);
        fragmentAll.setArguments(bundle);
        tabs_body.add(fragmentAll);

        Fragment fragmentArticle = new HomePageFragmentNews();
        Bundle bundleArticle = new Bundle();
        bundleArticle.putString("style_id", "4");
        bundleArticle.putInt("auth_code", userHomePageBean.auth_code);
        bundleArticle.putInt("type", 1);
        fragmentArticle.setArguments(bundleArticle);
        tabs_body.add(fragmentArticle);

        tab_body.setOffscreenPageLimit(tabs_body.size());

        NewsPagerAdapter pagerAdapter = new NewsPagerAdapter(getChildFragmentManager(), tabs_body);
        tab_body.setAdapter(pagerAdapter);
        ViewPagerHelper.bind(tab_bar, tab_body);
        tab_body.setCurrentItem(0);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        userHomePageBean = (UserHomePageBean) getArguments().getSerializable(KEY_DATA);
    }


    public static HomePageOtherFragment newInstance(UserHomePageBean userHomePageBean) {
        HomePageOtherFragment homePageOtherFragment = new HomePageOtherFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_DATA, userHomePageBean);
        homePageOtherFragment.setArguments(bundle);
        return homePageOtherFragment;
    }

    @Override
    protected void lazyload() {
        setStatusBar(Color.RED);
        initData();
        initListener();
        update();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_homepage_other, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lazyload();
    }


    protected void initData() {
        shareBaseUrl = SPUtils.getStringData(getActivity(), "personage_homepage_host", "");
        initTabBar();
        isFollow = userHomePageBean.is_follow;
        setFollowState();
    }


    protected void initListener() {
        ivBack.setOnClickListener(v -> getActivity().finish());
        ClickUtils.applySingleDebouncing(ivMore, v -> {
            goShare();
        });
        ClickUtils.applySingleDebouncing(ivSearch, v -> {
            goSearch();
        });
        img_arrow.setOnClickListener(v -> {
            arrowDown = !arrowDown;
            if (arrowDown) {
                img_arrow.setImageResource(R.mipmap.icon_up);
                getLikeList();
            } else {
                img_arrow.setImageResource(R.mipmap.icon_down);
                ll_recommend.setVisibility(View.GONE);
            }
        });
        tv_follow.setOnClickListener(v -> {
            if (isFollow) {
                updateFollow(0);
            } else {
                updateFollow(1);
            }
        });
        baseViewHolderBaseQuickAdapter = new BaseQuickAdapter<LikeBean, BaseViewHolder>(R.layout.item_recommed_attention) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, LikeBean likeBean) {
                TextView name = baseViewHolder.getView(R.id.tv_name);
                name.setText(likeBean.user_name);
                ImageView closeView = baseViewHolder.getView(R.id.iv_close);
                closeView.setOnClickListener(v -> {
                    int pos = baseViewHolder.getLayoutPosition();
                    baseViewHolderBaseQuickAdapter.remove(pos);
                });
                TextView tv_sign = baseViewHolder.getView(R.id.tv_sign);
                tv_sign.setText(likeBean.user_sign);
                TextView tv_flag = baseViewHolder.getView(R.id.tv_flag);
                tv_flag.setOnClickListener(v -> {
                    follow(likeBean.auth_code, baseViewHolder.getLayoutPosition());
                });
                ImageView imageView = baseViewHolder.getView(R.id.iv_head);
                Glide.with(getActivity()).load(likeBean.auth_icon).into(imageView);

            }
        };
        LinearLayoutManager ms = new LinearLayoutManager(getContext());
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);// 设置 recyclerview 布局方式为横向布局
        recyclerView.setLayoutManager(ms);
        recyclerView.setAdapter(baseViewHolderBaseQuickAdapter);
    }

    /**
     * 搜索
     */
    private void goSearch() {
        openActivity(HomePageSearchActivity.class);
    }

    /**
     * 分享
     */
    private void goShare() {
        CustomDialog customDialog = CustomDialog.init();
        customDialog
                .setLayoutId(R.layout.dialog_attention_order)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        RecyclerView recyclerView = holder.getView(R.id.recyclerView);
                        BaseQuickAdapter<Pair<String, Integer>, BaseViewHolder> adapter = new BaseQuickAdapter<Pair<String, Integer>, BaseViewHolder>(R.layout.item_share_card, pairList) {
                            @Override
                            protected void convert(BaseViewHolder baseViewHolder, Pair<String, Integer> s) {
                                RelativeLayout relativeLayout = baseViewHolder.getView(R.id.rl_share);
                                CircleImageView img = baseViewHolder.getView(R.id.img);
                                img.setImageResource(s.second);
                                TextView tv_name = baseViewHolder.getView(R.id.tv_name);
                                tv_name.setText(s.first);
                                relativeLayout.setOnClickListener(v -> {
                                    dialog.dismiss();
                                    share(baseViewHolder.getLayoutPosition());
                                });
                            }
                        };
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(adapter);
                        TextView title = holder.getView(R.id.tv_title);
                        title.setVisibility(View.GONE);
                        TextView cancel = holder.getView(R.id.tv_cancel);
                        cancel.setText(getString(android.R.string.cancel));
                        cancel.setOnClickListener(v1 -> {
                            dialog.dismissAllowingStateLoss();
                        });
                    }
                })
                .setGravity(Gravity.BOTTOM)
                .show(getChildFragmentManager());

    }


    private void update() {
        tvProduction.setText(getString(R.string.homepage_production, userHomePageBean.media_num));
        tvLike.setText(getString(R.string.homepage_like, userHomePageBean.goods_num));
        tvFans.setText(getString(R.string.homepage_fans, userHomePageBean.fans_num));
        tvAttention.setText(getString(R.string.homepage_attention, userHomePageBean.follow_num));
        tvAttentionName.setText(userHomePageBean.user_sign);
        setSex(userHomePageBean.sex);
        tvName.setText(userHomePageBean.user_name);
        Glide.with(this).load(userHomePageBean.auth_icon).placeholder(R.mipmap.icon_defult_boy).dontAnimate().into(ivHead);
        Glide.with(this).load(userHomePageBean.back_image).placeholder(R.mipmap.bg_personal_top).dontAnimate().into(imgBg);
        if (!TextUtils.isEmpty(userHomePageBean.user_sign)) {
            tvAttentionName.setText(userHomePageBean.user_sign);
        }
    }

    private void setSex(int sex) {
        switch (sex) {
            case 0:
                tvSex.setVisibility(View.GONE);
                break;
            case 1:
                tvSex.setText(getString(R.string.homepage_sex_men));
                break;
            case 2:
                tvSex.setText(getString(R.string.homepage_sex_women));
                break;
        }
    }

    /**
     * 关注与否
     *
     * @param state 处理状态 0.取消 1.添加 (默认为0)
     */
    private void updateFollow(int state) {
        RequestParams params = new RequestParams();
        params.put("auth_code", userHomePageBean.auth_code);
        params.put("state", state);
        HttpUtils.post(Constants.UPDATE_FOLLOW_LIST, HomePageOtherFragment.this, params, new HttpUtils.TextHttpResponseHandler() {
            @Override
            protected void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("HomePagePersonalFragment updateFollow onSuccess responseString = " + responseString);
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    int code = jsonObject.optInt("code");
                    //返回码说明
                    String msg = jsonObject.optString("msg");
                    if (0 == code) {
                        isFollow = !isFollow;
                        setFollowState();
                    } else {
                        showToast(msg);
                    }
                } catch (JSONException e) {
                }
            }

            @Override
            protected void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }
        });
    }

    /**
     * 关注
     *
     * @param pos       列表位置,关注后用来删除使用
     * @param auth_code 被关注人的auth_code
     */
    private void follow(String auth_code, int pos) {
        RequestParams params = new RequestParams();
        params.put("auth_code", auth_code);
        params.put("state", 1);
        HttpUtils.post(Constants.UPDATE_FOLLOW_LIST, HomePageOtherFragment.this, params, new HttpUtils.TextHttpResponseHandler() {
            @Override
            protected void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("HomePagePersonalFragment updateFollow follow responseString = " + responseString);
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    int code = jsonObject.optInt("code");
                    //返回码说明
                    String msg = jsonObject.optString("msg");
                    if (0 == code) {
                        baseViewHolderBaseQuickAdapter.remove(pos);
                    } else {
                        showToast(msg);
                    }
                } catch (JSONException e) {
                }
            }

            @Override
            protected void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }
        });
    }


    /**
     * 获取用相关推荐个人主页列表
     */
    private void getLikeList() {
        RequestParams params = new RequestParams();
        params.put("auth_code", userHomePageBean.auth_code);
        HttpUtils.post(Constants.GET_LIKE_LIST, HomePageOtherFragment.this, params, new HttpJsonResponse() {
            @Override
            protected void onSuccess(com.alibaba.fastjson.JSONObject responseString) {
                LogUtils.d("HomePagePersonalFragment getLikeList onSuccess responseString = " + responseString);
                //返回码说明
                if (responseString.getIntValue("code") == 0) {
                    if (responseString.getJSONObject("list").getJSONArray("data").size() > 0) {
                        List<LikeBean> list = gson.fromJson(responseString.getJSONObject("list").getJSONArray("data").toJSONString(), new TypeToken<List<LikeBean>>() {
                        }.getType());
                        if (null != list) {
                            ll_recommend.setVisibility(View.VISIBLE);
                            List<LikeBean> data = baseViewHolderBaseQuickAdapter.getData();
                            if (data.isEmpty()) {
                                showLikeList(list);
                            } else {
                                if (!list.equals(data)) {
                                    data.clear();
                                    showLikeList(list);
                                }
                            }
                        }
                    } else {
                        String msg = responseString.getString("msg");
                        showToast(msg);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                showToast(responseString);
            }
        });
    }

    private void setFollowState() {
        if (isFollow) {
            tv_follow.setText(getString(R.string.homepage_other_attentioned));
            tv_follow.setTextColor(getResources().getColor(R.color.white));
        } else {
            tv_follow.setText(getString(R.string.homepage_other_attention));
            tv_follow.setTextColor(getResources().getColor(R.color.black));
        }
    }

    private void showLikeList(List<LikeBean> likeBeanList) {
        baseViewHolderBaseQuickAdapter.addData(likeBeanList);
    }

    private void share(int pos) {
        String url = getShareUrl();
        switch (pos) {
            case 0:
                goCardShare();
                break;
            case 1:
                WxUtil.sendCardMessage(SendMessageToWX.Req.WXSceneSession, getActivity(), getString(R.string.homepage_welcome), url);
                break;
            case 2: {
                WxUtil.sendCardMessage(SendMessageToWX.Req.WXSceneTimeline, getActivity(), getString(R.string.homepage_welcome), url);
                break;
            }
            case 3: {
                QQShareUtil.shareToQQ(getString(R.string.homepage_welcome), getString(R.string.homepage_welcome), url, getActivity(), null);
                break;
            }
            case 4: {
                QQShareUtil.shareToQQZone(getString(R.string.homepage_welcome), getString(R.string.homepage_welcome), url, getActivity(), null);
                break;
            }
        }
    }

    /**
     * 名片分享
     */
    private void goCardShare() {
        FullScreenDialog fullScreenDialog = new FullScreenDialog();
        fullScreenDialog.setContentView(userHomePageBean.auth_icon, userHomePageBean.user_name, getShareUrl(), userHomePageBean.user_sign);
        fullScreenDialog.show(getChildFragmentManager(), "simple_dialog_test");
    }

    /**
     * 删除不喜欢的推荐
     * 如果都删除,就不显示list
     *
     * @param pos
     */
    private void removeLikeList(int pos) {

    }

    private String getShareUrl() {
        StringBuilder url = new StringBuilder(shareBaseUrl);
        url.append("?auth=");
        int authCode = userHomePageBean.auth_code;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("auth_code", userHomePageBean.auth_code);
            jsonObject.put("share_auth_code", CaiNiaoApplication.getUserInfoBean().user_msg.auth_code);
        } catch (JSONException e) {
        }
        String base64Content = Base64Utils.encode(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
        url.append(base64Content);
        return url.toString();
    }

}
