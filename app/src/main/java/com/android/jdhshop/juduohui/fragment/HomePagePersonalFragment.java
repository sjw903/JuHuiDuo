package com.android.jdhshop.juduohui.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.CropActivity;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.bean.UserHomePageBean;
import com.android.jdhshop.bean.UserInfoBean;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.dialog.CustomDialog;
import com.android.jdhshop.dialog.FullScreenDialog;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.juduohui.AttentionActivity;
import com.android.jdhshop.juduohui.FansActivity;
import com.android.jdhshop.juduohui.HomePageSearchActivity;
import com.android.jdhshop.juduohui.NewsPubActivity;
import com.android.jdhshop.juduohui.adapter.NewsPagerAdapter;
import com.android.jdhshop.juduohui.response.HttpJsonResponse;
import com.android.jdhshop.my.MyInformationActivity;
import com.android.jdhshop.utils.Base64Utils;
import com.android.jdhshop.utils.UIUtils;
import com.android.jdhshop.utils.WxUtil;
import com.android.jdhshop.widget.CircleImageView;
import com.android.jdhshop.widget.ImageSelectDialog;
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
import com.loopj.android.http.RequestParams;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.ViewConvertListener;
import com.shehuan.nicedialog.ViewHolder;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class HomePagePersonalFragment extends BaseLazyFragment {

    /**
     * 审核通过
     */
    private static final String PASS_STATE = "100.00";
    private static final String KEY_DATA = "userInfo";
    public static final int HEADER_REQUEST_CODE = 1000;
    public static final int BG_REQUEST_CODE = 1001;
    public static final int DATA_REQUEST_CODE = 1002;

    @BindView(R.id.app_bar)
    AppBarLayout app_bar;


    @BindView(R.id.toolbar)
    Toolbar toolbar;


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

    @BindView(R.id.tv_edit)
    TextView tvEdit;

    @BindView(R.id.tv_submit)
    TextView tvSubmit;

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

    @BindView(R.id.tab_bar)
    MagicIndicator tab_bar;
    @BindView(R.id.tab_body)
    ViewPager tab_body;

    @BindView(R.id.iv_card_share)
    ImageView iv_card_share;


    private List<Pair<String, Integer>> pairList = new ArrayList<>();

    {
        pairList.add(new Pair<>("名片分享", R.mipmap.icon_card_share));
        pairList.add(new Pair<>("微信", R.mipmap.icon_weixin));
        pairList.add(new Pair<>("朋友圈", R.mipmap.icon_friend));
        pairList.add(new Pair<>("QQ", R.mipmap.icon_qq));
        pairList.add(new Pair<>("QQ空间", R.mipmap.icon_qqzone));
    }

    private UserHomePageBean userHomePageBean;
    private String filePath;
    private ImageView currentView;

    List<String> tabs = Arrays.asList("全部", "文章");
    List<Fragment> tabs_body = new ArrayList<>();
    CommonNavigatorAdapter tab_menus_adapter;
    String FragmentScroll = "no";
    private long tabbar_last_click_time = 0;
    private View tabbar_last_click_v = null;

    private String shareBaseUrl;

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
        bundleArticle.putInt("auth_code", userHomePageBean.auth_code);
        bundleArticle.putString("style_id", "4");
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


    public static HomePagePersonalFragment newInstance(UserHomePageBean userHomePageBean) {
        HomePagePersonalFragment homePagePersonalFragment = new HomePagePersonalFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_DATA, userHomePageBean);
        homePagePersonalFragment.setArguments(bundle);
        return homePagePersonalFragment;
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
        View v = inflater.inflate(R.layout.fragment_homepage_personal, container, false);
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
    }


    protected void initListener() {
        ivBack.setOnClickListener(v -> getActivity().finish());
        ClickUtils.applySingleDebouncing(ivMore, v -> {
            goShare();
        });
        ClickUtils.applySingleDebouncing(ivSearch, v -> {
            goSearch();
        });
        ClickUtils.applySingleDebouncing(iv_card_share, v -> {
            goCardShare();
        });
        imgBg.setOnClickListener(v -> changeBg());
        tvEdit.setOnClickListener(v -> {
            if (!PASS_STATE.equals(userHomePageBean.integrity_rate)) {
                showToast(getString(R.string.homepage_data_not_enough));
            }
            Intent intent = new Intent(getContext(), MyInformationActivity.class);
            intent.putExtra("data", userHomePageBean);
            startActivityForResultBase(intent, DATA_REQUEST_CODE);
        });
        tvSubmit.setOnClickListener(v -> {
            if (1 != userHomePageBean.state) {
                String error_msg = userHomePageBean.error_msg;
                if (!TextUtils.isEmpty(error_msg)) {
                    showToast(error_msg);
                }
            } else {
                openActivity(NewsPubActivity.class);
            }
        });
        ivHead.setOnClickListener(v -> changeHeader());
        tvFans.setOnClickListener(v -> {
            openActivity(FansActivity.class);
        });
        tvAttention.setOnClickListener(v -> {
            openActivity(AttentionActivity.class);
        });
//        app_bar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
//            int offset = Math.abs(verticalOffset);
//            if (offset == 0) {
//            } else if (offset >= appBarLayout.getTotalScrollRange()) {
//                toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//            } else {
//                toolbar.setBackgroundColor(getResources().getColor(R.color.orange));
//            }
//        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            filePath = data.getStringExtra("url");
            if (HEADER_REQUEST_CODE == requestCode) {
                File avaterFile = new File(filePath);
                uploadImage(avaterFile, "userAvator", "auth_icon");
                currentView = ivHead;
            } else if (BG_REQUEST_CODE == requestCode) {
                File bgFile = new File(filePath);
                currentView = imgBg;
                uploadImage(bgFile, "userHomebg", "back_image");
            } else if (DATA_REQUEST_CODE == requestCode) {
                boolean changed = data.getBooleanExtra("changed", false);
                if (changed) {
                    loadData();
                }
            }
        }
    }

    /**
     * 搜索
     */
    private void goSearch() {
        Bundle bundle = new Bundle();
        bundle.putInt("auth_code", userHomePageBean.auth_code);
        openActivity(HomePageSearchActivity.class, bundle);
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


    private void changeHeader() {
        new ImageSelectDialog(getActivity(), R.style.ActionSheetDialogStyle).setOnImageSelectDialogListener(picturePath -> {
            if (picturePath == null || "".equals(picturePath))
                return;
            Intent intent = new Intent(getActivity(), CropActivity.class);
            intent.putExtra("url", picturePath);
            startActivityForResult(intent, HEADER_REQUEST_CODE);
        }).show();
    }

    private void changeBg() {
        new ImageSelectDialog(getActivity(), R.style.ActionSheetDialogStyle).setOnImageSelectDialogListener(picturePath -> {
            if (picturePath == null || "".equals(picturePath))
                return;
            Intent intent = new Intent(getActivity(), CropActivity.class);
            intent.putExtra("url", picturePath);
            startActivityForResult(intent, BG_REQUEST_CODE);
        }).show();
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
//        Glide.with(this).load(userHomePageBean.back_image).placeholder(R.mipmap.bg_personal_top).dontAnimate().into(imgBg);
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
     * @param bgFile      文件
     * @param ossPathName oss名字
     * @param uploadType  头像或者背景
     */
    private void uploadImage(File bgFile, String ossPathName, String uploadType) {
        showLoadingDialog();
        String fileName = bgFile.getName();
        RequestParams sign_req = new RequestParams();
        sign_req.put("file_name", fileName.replace("[", "").replace("]", ""));
        sign_req.put("oss_path_name", ossPathName);
        sign_req.put("channel_type", "2");
        HttpUtils.post(Constants.GET_UPLOAD_SIGN, HomePagePersonalFragment.this, sign_req, new HttpJsonResponse() {
            @Override
            protected void onSuccess(com.alibaba.fastjson.JSONObject response) {
                LogUtils.d("onSuccess response = " + response.toJSONString());
                if (response.getIntValue("code") == 0) {
                    try {
                        String jsonStr = response.getString("data");
                        LogUtils.d("onSuccess jsonStr = " + jsonStr);
                        com.alibaba.fastjson.JSONObject signed_file_name = JSON.parseObject(jsonStr);
                        RequestParams tmp_req = new RequestParams();
                        tmp_req.put("OSSAccessKeyId", signed_file_name.getString("accessid"));
                        tmp_req.put("callback", signed_file_name.getString("callback"));
                        tmp_req.put("key", signed_file_name.getString("dir") + "/" + signed_file_name.getString("pic_name"));
                        tmp_req.put("policy", signed_file_name.getString("policy"));
                        tmp_req.put("signature", signed_file_name.getString("signature"));
                        tmp_req.put("success_action_status", "200");
                        tmp_req.put("file", fileName, bgFile);
                        HttpUtils.postUpload(signed_file_name.getString("host"), HomePagePersonalFragment.this, tmp_req, new HttpUtils.TextHttpResponseHandler() {
                            @Override
                            protected void onSuccess(int statusCode, Header[] headers, String responseString) {
                                LogUtils.d("uploadImage onSuccess responseString = " + responseString);
                                try {
                                    com.alibaba.fastjson.JSONObject response = com.alibaba.fastjson.JSONObject.parseObject(responseString);
                                    if (response.getIntValue("code") == 0) {
                                        String url = response.getString("url");
                                        RequestParams requestParams = new RequestParams(uploadType, url);
                                        uploadUrl(uploadType, url);
                                    } else {
                                        // Log.d(TAG, "onSuccess: 有上传文件失败");
                                    }
                                } catch (Exception e) {
                                    closeLoadingDialog();
                                    showToast("提交数据失败!" + e.getMessage());
                                }
                            }

                            @Override
                            protected void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                LogUtils.d("oss onFailure response = " + responseString);
                                closeLoadingDialog();
                                showToast("提交数据失败!" + responseString);
                            }

                        });
                    } catch (Exception e) {
                        closeLoadingDialog();
                        showToast("提交数据失败!" + e.getMessage());
                    }
                } else {
                    closeLoadingDialog();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                LogUtils.d("sign onFailure response = " + responseString);
                closeLoadingDialog();
            }
        });
    }

    /**
     * @param uploadType 上传类型
     * @param url        地址
     */
    private void uploadUrl(String uploadType, String url) {
        RequestParams requestParams = new RequestParams(uploadType, url);
        HttpUtils.post(Constants.GET_ADD_USER_HOME, HomePagePersonalFragment.this, requestParams, new HttpUtils.TextHttpResponseHandler() {
            @Override
            public void onFinish() {
                closeLoadingDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                com.alibaba.fastjson.JSONObject response = com.alibaba.fastjson.JSONObject.parseObject(responseString);
                LogUtils.d("uploadUrl onSuccess responseString = " + responseString);
                if (response.getIntValue("code") == 0) {
                    if (currentView == ivHead) {
                        userHomePageBean.auth_icon = url;
                    }
                    updateImage();
                }
                showToast("提交数据" + response.getString("msg"));
            }
        });
    }

    private void loadData() {
        UserInfoBean userInfoBean = CaiNiaoApplication.getUserInfoBean();
        if (null == userInfoBean) {
            return;
        }
        UserInfoBean.UserMsgBean userMsgBean = userInfoBean.user_msg;
        if (null == userMsgBean) {
            return;
        }
        String auth_code = userMsgBean.auth_code;
        if (TextUtils.isEmpty(auth_code)) {
            return;
        }
        RequestParams params = new RequestParams();
        params.put("auth_code", auth_code);
        HttpUtils.post(Constants.GET_USER_HOME, HomePagePersonalFragment.this, params, new HttpUtils.TextHttpResponseHandler() {
            @Override
            protected void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("HomePagePersonalFragment onSuccess responseString = " + responseString);
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    int code = jsonObject.optInt("code");
                    //返回码说明
                    String msg = jsonObject.optString("msg");
                    String data = jsonObject.optString("data");
                    if (0 == code) {
                        if (!TextUtils.isEmpty(data)) {
                            Gson gson = new Gson();
                            UserHomePageBean userBean = gson.fromJson(data.trim(), UserHomePageBean.class);
                            if (null != userBean) {
                                userHomePageBean = userBean;
                                update();
                            }
                        } else {
                            showToast(msg);
                        }
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

    private void updateImage() {
        Glide.with(this).load(filePath).into(currentView);
    }

    private String getShareUrl() {
        StringBuilder url = new StringBuilder(shareBaseUrl);
        url.append("?auth=");
        int authCode = userHomePageBean.auth_code;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("auth_code", authCode);
            jsonObject.put("share_auth_code", authCode);
        } catch (JSONException e) {
        }
        String base64Content = Base64Utils.encode(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
        url.append(base64Content);
        return url.toString();
    }
}