package com.android.jdhshop.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.activity.PromotionDetailsActivity;
import com.android.jdhshop.adapter.NineAdapterListNew;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.bean.HaoDanBean;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.utils.UIUtils;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.uuch.adlibrary.utils.DisplayUtil;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;

/**
 * @属性:商品页
 * @开发者:wmm
 * @时间:2018/11/16 15:18
 */
public class PHBFragment extends BaseLazyFragment {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;

    DecimalFormat df = new DecimalFormat("0.00");
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.right_icon)
    ImageView rightIcon;
    @BindView(R.id.tabBar_my)
    TabLayout tabBarMy;
    private String pid;
    private int indexNum = 1;
    private String name;
    private int status = 0;
    private String sort;
    private GridLayoutManager linearLayoutManager;
    private NineAdapterListNew phbAdapter;
    private List<HaoDanBean> list = new ArrayList<>();
    private String group_id;
    private ACache mAcache;
    private boolean hasdata = true;
    private String min_id = "1";
    private List<String> temp = new ArrayList<>();
    private String curr = "0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nine2, container, false);
        unbinder = ButterKnife.bind(this, view);
        BaseLogDZiYuan.LogDingZiYuan(rightIcon, "home_btn.png");
        temp.add("全部");
        temp.add("女装");
        temp.add("男装");
        temp.add("内衣");
        temp.add("美妆");
        temp.add("配饰");
        temp.add("鞋品");
        temp.add("箱包");
        temp.add("儿童");
        temp.add("母婴");
        temp.add("居家");
        temp.add("美食");
        temp.add("数码");
        temp.add("家电");
        temp.add("其他");
        temp.add("车品");
        temp.add("文体");
        temp.add("宠物");
        for (int i = 0; i < temp.size(); i++) {
            tabBarMy.addTab(tabBarMy.newTab().setText(temp.get(i)));
//          mTabLayout.addTab(mTabLayout.newTab().setText(mTitles[i]).setIcon(R.mipmap.ic_launcher));
        }
        tabBarMy.setTabMode(TabLayout.MODE_SCROLLABLE);
        reflex(tabBarMy);
        tabBarMy.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                curr = tab.getPosition() + "";
                min_id = "1";
                getTbkListRequst();
                refreshLayout.autoRefresh();
                list.clear();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        init();
        addListener();
        return view;
    }

    public void reflex(final TabLayout tabLayout) {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

                    int dp10 = DisplayUtil.dip2px(tabLayout.getContext(), 10);

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void init() {
        mAcache = ACache.get(getContext());
        group_id = mAcache.getAsString("group_id");
        Bundle arguments = getArguments();
        if (arguments != null) {
            pid = arguments.getString("pid");
            name = arguments.getString("name");
            sort = arguments.getString("sort");
        }
        //适配器
        phbAdapter = new NineAdapterListNew(getActivity(), R.layout.item_phb, list);
        //管理器
        linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(phbAdapter);

        //显示回滚
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //屏幕高度
                int screenMeasuredHeight = UIUtils.getScreenMeasuredHeight(getActivity());
                //如果滚动距离大于屏幕高度，那么显示回滚，否则。。。
                if (getScollYDistance() >= (screenMeasuredHeight / 2)) {
                    rightIcon.setVisibility(View.VISIBLE);
                } else {
                    rightIcon.setVisibility(View.GONE);
                }
                LogUtils.d("TAG", "高度为:" + getScollYDistance());

            }
        });
    }

    private void addListener() {
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                status = 0;
                if (hasdata) {
                    indexNum++;
                    getTbkListRequst();
                } else {
                    showToast("没有更多数据了");

                    refreshLayout.finishLoadMore(2000);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                min_id = "1";
                hasdata = true;
                getTbkListRequst();
            }
        });
        //点击进入详情
        phbAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                HaoDanBean bean = list.get(position);
                if (bean != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("num_iid", bean.itemid);
                    bundle.putSerializable("bean", bean);
                    if (Double.valueOf(bean.videoid) > 0) {
                        bundle.putString("tye", "1");
                        bundle.putString("url", bean.videoid);
                    }
                    openActivity(PromotionDetailsActivity.class, bundle);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * @属性:获取屏幕高度的方法
     * @开发者:陈飞
     * @时间:2018/8/7 14:22
     */
    public long getScollYDistance() {
        int position = linearLayoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = linearLayoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected void lazyload() { //懒加载，界面开始后刷新
        indexNum = 1;
        hasdata = true;
        getTbkListRequst();
    }

    /**
     * @属性:获取淘宝客商品列表
     * @开发者:陈飞
     * @时间:2018/7/26 17:05
     */
    private void getTbkListRequst() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("sale_type", pid);
        requestParams.put("cid", curr);
        requestParams.put("back", "10");
        requestParams.put("min_id", min_id);
//        if(sort.equals("综合")){
//            url=Constants.GET_TOPGOODS + "&topcate=热销&page=" + indexNum;
//        }else{
//            url=Constants.GET_TOPGOODS + "&topcate=热销&subcate=" + sort+ "&page=" + indexNum;
//        }
        HttpUtils.post(Constants.GET_HAODAN_PHB, PHBFragment.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("dsfasd", responseString);
                try {
                    JSONObject object = new JSONObject(responseString);
                    if ("1".equals(object.getString("code"))) {
                        JSONArray array = object.getJSONArray("data");
                        String data = object.getString("data");
                        if (data.equals("[]")) {
                            showToast("没有更多数据了");
                            refreshLayout.finishRefresh();
                            refreshLayout.finishLoadMore();
                        }
                        if (min_id.equals("1")) {
                            list.clear();
                        }

                        min_id = object.getString("min_id");
                        if (array.length() <= 0) {
                            hasdata = false;
                            return;
                        }

                        for (int i = 0; i < array.length(); i++) {
                            list.add(new Gson().fromJson(array.getJSONObject(i).toString(), HaoDanBean.class));
                        }
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        phbAdapter.notifyDataSetChanged();
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (refreshLayout != null) {
                    //if ("1".equals(min_id)) {
                    refreshLayout.finishRefresh();
                    //} else {
                    refreshLayout.finishLoadMore();
                    phbAdapter.notifyDataSetChanged();
                    //}
                }
            }
        });
    }

    @OnClick({R.id.right_icon})
    public void onViewClicked(View view) { //回到头部
        switch (view.getId()) {
            case R.id.right_icon:
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.smoothScrollToPosition(0);
                    }
                });
                break;

        }
    }
}
