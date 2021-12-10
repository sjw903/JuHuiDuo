package com.android.jdhshop.juduohui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.JuDuoHuiActivity;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.juduohui.NewsListFragment;
import com.android.jdhshop.juduohui.NewsPubListActivity;
import com.android.jdhshop.juduohui.adapter.NewsPagerAdapter;
import com.android.jdhshop.juduohui.adapter.NewsTypeSetAdapter;
import com.android.jdhshop.juduohui.response.HttpJsonResponse;
import com.android.jdhshop.login.WelActivity;
import com.android.jdhshop.utils.UIUtils;
import com.android.jdhshop.widget.indicator.MagicIndicator;
import com.android.jdhshop.widget.indicator.ViewPagerHelper;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.CommonNavigator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.titles.ClipPagerTitleView;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class NewsReadFragment extends BaseLazyFragment {

    @BindView(R.id.go_back_button)
    ImageView go_back_button;
    @BindView(R.id.pub_button)
    TextView pub_button;
    @BindView(R.id.today_read_time)
    TextView today_read_time;
    @BindView(R.id.all_read_coins)
    TextView all_read_coins;
    @BindView(R.id.img_more)
    ImageView more_menu_button;
    @BindView(R.id.more_menus)
    LinearLayout more_menus;
    @BindView(R.id.tab_menus)
    MagicIndicator tab_menus;

    JSONArray tab_menus_list = new JSONArray();
    List<Object> disable_type = new ArrayList<>();
    CommonNavigatorAdapter tab_menus_adapter;
    @BindView(R.id.news_list_page)
    ViewPager news_list_page;
    NewsPagerAdapter newsPagerAdapter;
    List<Fragment> fragments = new ArrayList<>();

    Context mContext;
    Activity mActivity;
    ACache aCache;

    int current_page_index = 0; // 当前页面所处数组的index
    int current_page_type =0; // 当前页面的分类ID

    String TAG = "NewsReadFragment";

    @Override
    protected void lazyload() {
        // Log.d(TAG, "lazyload: ");
        mContext = getContext();
        mActivity = getActivity();
        aCache = ACache.get(mContext);

        loadDisableType();

        setStatusBar(Color.parseColor("#FD7F56"));

        /* 发布 */
        Drawable i_go_pub_icon = getResources().getDrawable(R.drawable.ic_news_pub);
        i_go_pub_icon.setBounds(0, 0, 52, 52);
        i_go_pub_icon.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        pub_button.setCompoundDrawables(null, i_go_pub_icon, null, null);
        pub_button.setCompoundDrawablePadding(6);

        initListener();
        initData();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_read,container,false);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lazyload();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void initData() {
        getNewsTypes();
        getDateTime();
    }

    protected void initListener() {
        Animation an = new AlphaAnimation(0.2F,1F);
        an.setDuration(1000);
        an.setRepeatCount(0);
        an.setRepeatMode(Animation.INFINITE);
        an.setFillBefore(false);
        an.setFillAfter(false);
        go_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        more_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                more_menu_button.startAnimation(an);
                showTypeSetDialog();
            }
        });

        pub_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(SPUtils.getStringData(getActivity(), "token", ""))) {
                    showToast( "请先登录");
                    openActivity(WelActivity.class);
                    return;
                }

                openActivity(NewsPubListActivity.class);
            }
        });

        all_read_coins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ("".equals(SPUtils.getStringData(getActivity(), "token", ""))) {
                    showToast( "请先登录");
                    openActivity(WelActivity.class);
                    return;
                }

                Bundle b = new Bundle();
                b.putString("url","./news_history.html");
                openActivity(JuDuoHuiActivity.class,b);
            }
        });
    }

    private void getFragment(){
        fragments.clear();
        for(int i=0;i<tab_menus_list.size();i++) {
            NewsListFragment news_page_fragment = new NewsListFragment();
            if (tab_menus_list.getJSONObject(i).getIntValue("state") == 1) {
                Bundle bundle = new Bundle();
                bundle.putString("config", tab_menus_list.getString(i));
                news_page_fragment.setArguments(bundle);
                fragments.add(news_page_fragment);
            }
        }
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    private void setNewsTypeMenu(){
        getFragment();
        news_list_page.setOffscreenPageLimit(fragments.size());
        newsPagerAdapter = new NewsPagerAdapter(getFragmentManager(),fragments);
        news_list_page.setAdapter(newsPagerAdapter);

        CommonNavigator commonNavigator = new CommonNavigator(mActivity);
        commonNavigator.setSkimOver(true);
        tab_menus_adapter = new CommonNavigatorAdapter() {
            final JSONArray real_list = new JSONArray();
            @Override
            public int getCount() {
                real_list.clear();
                for (int i=0;i<tab_menus_list.size();i++){
                    if (tab_menus_list.getJSONObject(i).getIntValue("state") == 1){
                        real_list.add(tab_menus_list.getJSONObject(i));
                    }
                }
                return real_list.size();
            }
            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context){
                    @Override
                    public void onSelected(int index, int totalCount) {
                        super.onSelected(index, totalCount);
                        setTextSize(UIUtils.dp2px(18));
                    }

                    @Override
                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
                        super.onLeave(index, totalCount, leavePercent, leftToRight);
                        setTextSize(UIUtils.dp2px(16));
                    }
                };
                clipPagerTitleView.setText(real_list.getJSONObject(index).getString("type_name"));
                clipPagerTitleView.setTextColor(getResources().getColor(R.color.aliuser_default_text_color));
                clipPagerTitleView.setTextSize(UIUtils.dp2px(16));
                clipPagerTitleView.setClipColor(getResources().getColor(R.color.aliuser_edittext_bg_color_activated));
                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        news_list_page.setCurrentItem(index);
                        current_page_index = index;
                        current_page_type = Integer.parseInt(((NewsListFragment) fragments.get(index)).current_type);
                    }
                });

                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(1);
                indicator.setColors(getResources().getColor(R.color.aliuser_edittext_bg_color_activated));
                return indicator;
            }
        };
        commonNavigator.setAdapter(tab_menus_adapter);
        tab_menus.setNavigator(commonNavigator);

        ViewPagerHelper.bind(tab_menus, news_list_page);
        //设置页数
        news_list_page.setCurrentItem(current_page_type);
    }

    private JSONArray filterNewsType(JSONArray news_type_list){
        JSONArray tmp = new JSONArray();
        for (int i=0;i<news_type_list.size();i++){
            JSONObject tmp_obj = news_type_list.getJSONObject(i);
            if (disable_type!=null && tmp_obj!=null && disable_type.contains(tmp_obj.getIntValue("id"))){
                tmp_obj.put("state",0);
            }
            tmp.add(tmp_obj);
        }
        return tmp;
    }
    // 获取文章分类
    private void getNewsTypes(){
        tab_menus_list.clear();
        JSONArray tmp = aCache.getAsJSONArray("news_type_array");
        // Log.d(TAG, "getNewsTypes: " + tmp);
        if (tmp !=null ){
            // Log.d(TAG, "getNewsTypes: 走缓存 ");
            JSONArray tt = filterNewsType(tmp);
            tab_menus_list.addAll(tt);
            setNewsTypeMenu();
        }
        else {
            // Log.d(TAG, "getNewsTypes: 走线上获取 ");
            HttpUtils.post(Constants.GET_XH_MEDIA_LIB_TYPE, NewsReadFragment.this,new RequestParams(), new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    // Log.d(TAG, "请求文章分类接口失败，返回信息: " + responseString);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    JSONObject response = JSONObject.parseObject(responseString);
                    if (response.getIntValue("code") == 0) {
                        aCache.put("news_type_array", response.getJSONArray("list"), 5 * 60);
                        JSONArray tt = filterNewsType(response.getJSONArray("list"));
                        tab_menus_list.addAll(tt);
                        setNewsTypeMenu();
                    } else {
                        // Log.d(TAG, "接口请求数据成功，但是CODE值不正确，CODE值 = " + response.getString("code") + ", MSG：" + response.getString("msg"));
                    }
                }
            });
        }
    }
    /**
     * 获取当日阅读收益信息
     */
    private void getDateTime(){
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.GET_READ_DAYTIME, NewsReadFragment.this,requestParams, new HttpJsonResponse() {
            @Override
            protected void onSuccess(JSONObject response) {
                super.onSuccess(response);
                // Log.d(TAG, "onSuccess: " + response.toJSONString());
                if (response.getIntValue("code") == 0){
                    long read_sum_time = response.getJSONObject("data").getLongValue("day_readtime");
                    long read_sum_coin = response.getJSONObject("data").getLongValue("sum_gold");
                    int d_sum_time = (int) read_sum_time/60;
                    all_read_coins.setText(String.format("%d金币", read_sum_coin));
                    today_read_time.setText(String.format("今日已阅读%d分钟", d_sum_time));
                }
            }
        });
    }
    private AlertDialog alertDialog = null;
    private void showTypeSetDialog(){

        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        View custom_view = View.inflate(mContext,R.layout.new_type_set_dialog,null);
        RecyclerView news_type_rec = custom_view.findViewById(R.id.news_type_set_rec);

        // Log.d(TAG, "showTypeSetDialog: " + disable_type.size());

        news_type_rec.setAdapter(new NewsTypeSetAdapter(tab_menus_list, new NewsTypeSetAdapter.ItemCheckChangeListen() {
            @Override
            public void change(CompoundButton v, int optionTypeID, boolean isChecked) {
                // Log.d(TAG, "change: " + optionTypeID + ", statue:"+isChecked);

                JSONArray xx = filterNewsType(tab_menus_list);
                // Log.d(TAG, "change: " + disable_type.size() + "," + tab_menus_list.size() +"," +xx.size());
                if (disable_type.size()+1 == tab_menus_list.size() && !isChecked){
                    showToast("最少保留一个新闻分类");
                    v.setChecked(true);
                    return;
                }
                if (!isChecked) {
                    disable_type.add((Object)optionTypeID);
                }
                else
                {
                    disable_type.remove((Object)optionTypeID);
                }
            }
        }));
        GridLayoutManager lm = new GridLayoutManager(mContext,2);
        news_type_rec.setLayoutManager(lm);

        Button save_button = custom_view.findViewById(R.id.type_save);
        Button close_button = custom_view.findViewById(R.id.type_close);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Log.d(TAG, "onClick: " + disable_type.toString());
                aCache.put("disable_type", disable_type.toString());
                getNewsTypes();

                alertDialog.dismiss();
            }
        });

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog!=null) alertDialog.dismiss();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                disable_type.clear();
                loadDisableType();
            }
        });

        dialog.setView(custom_view);
        loadDisableType();
        alertDialog = dialog.create();
        alertDialog.show();
    }


    private void loadDisableType(){
        if (aCache == null) aCache = ACache.get(getActivity());
        String ss = aCache.getAsString("disable_type");
            if (ss!=null) {
            try {
                disable_type = JSONArray.parseArray(ss);
                // Log.d(TAG, "lazyload: disable_type" + disable_type);
            }
            catch (Exception e){
                e.printStackTrace();
            }

            if (disable_type == null) disable_type = new ArrayList<>();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Log.d(TAG, "onResume: ");
        getDateTime();
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        // Log.d(TAG, "onVisible: ");
    }

    @Override
    protected void onInVisible() {
        super.onInVisible();
        // Log.d(TAG, "onInVisible: ");

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // Log.d(TAG, "onHiddenChanged: " + hidden);
        if (!hidden){
            if (current_page_type == 0){
                // Log.d(TAG, "onHiddenChanged: 调用页面的onResume");
                if (0 != fragments.size()) {
                    fragments.get(0).onResume();
                }
            }
        }
    }
}
