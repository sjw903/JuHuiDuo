package com.android.jdhshop.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.jdhshop.common.LogUtils;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.SearchActivity;
import com.android.jdhshop.activity.ShopActivity;
import com.android.jdhshop.adapter.ShopGridAdapter;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.bean.BannerBean;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.ShopTabsBean;
import com.android.jdhshop.bean.ShopTabsChildBean;
import com.android.jdhshop.bean.SubListByParentBean;
import com.android.jdhshop.bean.SubListByParentChildBean;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.utils.BroadcastContants;
import com.android.jdhshop.utils.CornerTransform;
import com.android.jdhshop.utils.VerticalViewPager;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * @属性:收藏列表
 * @开发者:陈飞
 * @时间:2018/7/28 11:10
 */
public class KindFragment extends BaseLazyFragment {

    @BindView(R.id.ver_ll)
    LinearLayout verLl;
    @BindView(R.id.ver_page)
    VerticalViewPager verPage;
    @BindView(R.id.tv_right)
    ImageView tvRight;
    private View view;
    public static KindFragment fragment;
    private LayoutInflater minflater;
    private TextView currentView;
    private List<View> views = new ArrayList<>();
    private boolean isClick = false;
    private List<Banner> bannerList = new ArrayList<>();
    private List<ShopGridAdapter> adapters = new ArrayList<>();
    private List<List<BannerBean>> beanList = new ArrayList<>();
    private List<GridView> gridViews = new ArrayList<>();
    private List<List<SubListByParentChildBean>> kindList = new ArrayList<>();
    private ShopGridAdapter current_adapter;
    private Banner current_banner;
    private List<SubListByParentChildBean> currentBeans;
    private List<BannerBean> currentBanners;
    private GridView current_grid;

    public static KindFragment getInstance() {
        if (fragment == null) {
            fragment = new KindFragment();
        }
        return fragment;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_kind, container, false);
        ButterKnife.bind(this, view);
        minflater = LayoutInflater.from(getContext());
        init();
        addListener();
        return view;
    }

    private void init() {
        tvRight.setVisibility(View.GONE);
        getTopKind();
    }

    private void addListener() {
        verPage.setOnVerticalPageChangeListener(new VerticalViewPager.OnVerticalPageChangeListener() {
            @Override
            public void onVerticalPageSelected(int position) {
                current_banner.stopAutoPlay();
                current_adapter = adapters.get(position);
                current_grid = gridViews.get(position);
                currentBanners = beanList.get(position);
                currentBeans = kindList.get(position);
                current_banner = bannerList.get(position);
                List<String> images = new ArrayList<>();
                for (int i=0;i<currentBanners.size();i++){
                    images.add(currentBanners.get(i).getImg());
                }
                current_banner.update(images);
                current_banner.startAutoPlay();
                currentView.setTextColor(Color.BLACK);
                currentView.setBackgroundColor(getResources().getColor(R.color.col_f7));
                currentView.setBackground(getResources().getDrawable(R.drawable.txt_shape2));
                verLl.getChildAt(position).setBackgroundColor(Color.WHITE);
                ((TextView)verLl.getChildAt(position)).setTextColor(getResources().getColor(R.color.red1));
                currentView = (TextView)  verLl.getChildAt(position);
                currentView.setBackground(getResources().getDrawable(R.drawable.txt_shape));
                if (currentBeans.size() > 0) {
                    //当已经加载过了就不用继续加载
                    return;
                }
                initListener();
                getChildKind(views.get(position).getTag().toString());
            }
        });
    }

    private void initListener() {
        current_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ShopActivity.class);
                intent.putExtra("title", "商品");
                intent.putExtra("index", currentView.getId());
                intent.putExtra("name", currentBeans.get(position).getName());
                startActivity(intent);
            }
        });
    }

    private void getTopKind() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.HOME_TAOBAOCAT_GETTOPCATLIST_URL, KindFragment.this,requestParams, new onOKJsonHttpResponseHandler<ShopTabsBean>(new TypeToken<Response<ShopTabsBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(responseString);
            }

            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Response<ShopTabsBean> datas) {
                if (datas.isSuccess()) {
                    final List<ShopTabsChildBean> list = datas.getData().getList();
                    for (int i = 0; i < list.size(); i++) {
                        TextView textView = (TextView) minflater.inflate(R.layout.item_text, null);
                        textView.setText(list.get(i).getName());
                        textView.setTag(list.get(i).getTaobao_cat_id());
                        textView.setOnClickListener(myCLick);
                        textView.setId(i);
                        verLl.addView(textView);
                        ScrollView rightView = (ScrollView) minflater.inflate(R.layout.item_view_kind, null);
                        rightView.setTag(list.get(i).getTaobao_cat_id());
                        views.add(rightView);
                        Banner banner = rightView.findViewById(R.id.kind_banner);
                        banner.setImageLoader(new ImageLoader() {
                            @Override
                            public void displayImage(Context context, Object path, ImageView imageView) {
                                String img_url = path.toString().startsWith("http") ? path.toString() : Constants.APP_IP+path;
                                Glide.with(context).load(img_url).asBitmap().skipMemoryCache(false).dontAnimate().error(R.drawable.no_banner).transform(new CornerTransform(context,8)).into(imageView);
                            }
                        });
                        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                        banner.setIndicatorGravity(BannerConfig.RIGHT);
                        List<BannerBean> tempList = new ArrayList<>();
                        LogUtils.d("list.get(i).getIcon()", "onSuccess: " + list.get(i).getIcon());
                        tempList.add(new BannerBean(list.get(i).getIcon()));
                        GridView gridView = rightView.findViewById(R.id.kind_recy);
                        List<SubListByParentChildBean> beans = new ArrayList<>();
                        ShopGridAdapter adapter = new ShopGridAdapter(context, R.layout.service_home_grid_item, beans);
                        gridView.setAdapter(adapter);
                        beanList.add(tempList);
                        bannerList.add(banner);
                        gridViews.add(gridView);
                        kindList.add(beans);
                        adapters.add(adapter);
                        if (i == 0) {
                            currentView = textView;
                            currentView.setBackgroundColor(Color.WHITE);
                            currentView.setTextColor(getResources().getColor(R.color.red1));
                            currentView.setBackground(getResources().getDrawable(R.drawable.txt_shape));
                        }
                    }
                    verPage.setViewList(views);
                    if (views.size() > 0) {
                        current_grid = gridViews.get(0);
                        currentBanners = beanList.get(0);
                        currentBeans = kindList.get(0);
                        current_banner = bannerList.get(0);
                        current_adapter = adapters.get(0);
                        List<String> images = new ArrayList<>();
                        for (int i=0;i<currentBanners.size();i++){
                            images.add(currentBanners.get(i).getImg());
                        }
                        current_banner.update(images);
                        current_banner.startAutoPlay();
                        initListener();
                        getChildKind(views.get(0).getTag().toString());
                    }
                } else {
                    showToast(datas.getMsg());
                }
            }
        });
//        List<ShopTabsChildBean> list = CaiNiaoApplication.getInstances().getDaoSession().loadAll(ShopTabsChildBean.class);
//        for (int i = 0; i < list.size(); i++) {
//            TextView textView = (TextView) minflater.inflate(R.layout.item_text, null);
//            textView.setText(list.get(i).getName());
//            textView.setTag(list.get(i).getTaobao_cat_id());
//            textView.setOnClickListener(myCLick);
//            textView.setId(i);
//            verLl.addView(textView);
//            ScrollView rightView = (ScrollView) minflater.inflate(R.layout.item_view_kind, null);
//            rightView.setTag(list.get(i).getTaobao_cat_id());
//            views.add(rightView);
//            Banner banner = rightView.findViewById(R.id.kind_banner);
//            banner.setImageLoader(new ImageLoader() {
//                @Override
//                public void displayImage(Context context, Object path, ImageView imageView) {
//                    Glide.with(context).load(Constants.APP_IP+path).asBitmap().skipMemoryCache(false).dontAnimate().error(R.drawable.no_banner).transform(new CornerTransform(context,8)).into(imageView);
//                }
//            });
//            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
//            banner.setIndicatorGravity(BannerConfig.RIGHT);
//            List<BannerBean> tempList = new ArrayList<>();
//            tempList.add(new BannerBean(list.get(i).getIcon()));
//            GridView gridView = rightView.findViewById(R.id.kind_recy);
//            List<SubListByParentChildBean> beans = new ArrayList<>();
//            ShopGridAdapter adapter = new ShopGridAdapter(context, R.layout.service_home_grid_item, beans);
//            gridView.setAdapter(adapter);
//            beanList.add(tempList);
//            bannerList.add(banner);
//            gridViews.add(gridView);
//            kindList.add(beans);
//            adapters.add(adapter);
//            if (i == 0) {
//                currentView = textView;
//                currentView.setBackgroundColor(Color.WHITE);
//                currentView.setTextColor(getResources().getColor(R.color.red1));
//                currentView.setBackground(getResources().getDrawable(R.drawable.txt_shape));
//            }
//        }
//        verPage.setViewList(views);
//        if (views.size() > 0) {
//            current_grid = gridViews.get(0);
//            currentBanners = beanList.get(0);
//            currentBeans = kindList.get(0);
//            current_banner = bannerList.get(0);
//            current_adapter = adapters.get(0);
//            List<String> images = new ArrayList<>();
//            for (int i=0;i<currentBanners.size();i++){
//                images.add(currentBanners.get(i).getImg());
//            }
//            current_banner.update(images);
//            current_banner.startAutoPlay();
//            initListener();
//            getChildKind(views.get(0).getTag().toString());
//        }
    }

    private void getChildKind(String id) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("pid", id);
        HttpUtils.post(Constants.HOME_TAOBAOCAT_GETSUBLISTBYPARENT_URL,KindFragment.this, requestParams, new onOKJsonHttpResponseHandler<SubListByParentBean>(new TypeToken<Response<SubListByParentBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Response<SubListByParentBean> datas) {
                if (datas.isSuccess()) {
                    List<SubListByParentChildBean> list = datas.getData().getList();
                    currentBeans.clear();
                    currentBeans.addAll(list);
                } else {
                    showToast(datas.getMsg());
                }
                current_adapter.notifyDataSetChanged();
            }
        });
    }

    private View.OnClickListener myCLick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == currentView.getId()) {
                //避免重复点击
                getChildKind( views.get(v.getId()).getTag().toString());
                return;
            }
            currentView.setTextColor(Color.BLACK);
            currentView.setBackgroundColor(getResources().getColor(R.color.col_f7));
            currentView.setBackground(getResources().getDrawable(R.drawable.txt_shape2));
            v.setBackgroundColor(Color.WHITE);
            ((TextView) v).setTextColor(getResources().getColor(R.color.red1));
            currentView = (TextView) v;
            currentView.setBackground(getResources().getDrawable(R.drawable.txt_shape));
            verPage.setToScreen(v.getId());
        }
    };

    @Override
    protected void lazyload() {
//        if (refreshLayout != null) {
//            //开始刷新
//            refreshLayout.autoRefresh();
//        }
    }

    @Override
    protected void ReceiverIsLoginMessage() {
        super.ReceiverIsLoginMessage();
    }

    @Override
    protected void ReceiverBroadCastMessage(String status, String result, Serializable serializable, Intent intent) {
        super.ReceiverBroadCastMessage(status, result, serializable, intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        null.unbind();
    }

    @OnClick(R.id.tv_title)
    public void onViewClicked() {
        openActivity(SearchActivity.class);
    }
}