package com.android.jdhshop.merchantactivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.MyOderViewPagerAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.merchantbean.Sjimglistbean;
import com.android.jdhshop.widget.indicator.MagicIndicator;
import com.android.jdhshop.widget.indicator.ViewPagerHelper;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.CommonNavigator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SjimagelistActivity extends BaseActivity implements View.OnClickListener{

    private ViewPager viewPager;
    private LinearLayout ly_back;
    private MagicIndicator magicIndicator;
    private ImageView fanhui;
    List<Fragment> fragmentList = new ArrayList<>(  );
    private FragmentAdapter mFragmentAdapter;

    private int index=0;

    @Override
    protected void initUI() {
        setContentView( R.layout.activity_sjimagelist );
        BaseLogDZiYuan.LogDingZiYuan(fanhui, "icon_back.png");
    }

    @Override
    protected void initData() {
        initView();
    }

    @Override
    protected void initListener() {

    }

    public void initView(){
        viewPager = findViewById( R.id.sjimg_viewpager );
        ly_back = findViewById( R.id.sjimg_lyback );
        fanhui = findViewById( R.id.sj_fanhui );
        ly_back.setOnClickListener( this );
        magicIndicator = findViewById(R.id.sjimglist_magic);
        getimgtopitem();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sjimg_lyback:
                finish();
                break;

        }
    }


    public class FragmentAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<Fragment>();

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }



        /*
         *由ViewPager的滑动修改底部导航Text的颜色
         */

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    List<Sjimglistbean> sjimglist = new ArrayList<>();
    private void getimgtopitem() {

        RequestParams requestParams = new RequestParams();
        requestParams.put("merchant_id", getIntent().getStringExtra("merid"));
        HttpUtils.post(Constants.getTypelist,SjimagelistActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Log.e("msg",responseString);
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        JSONArray array = object.getJSONObject("data").getJSONArray("list");
                        for (int i = 0;i<array.length();i++){
                            sjimglist.add(new Gson().fromJson(array.getJSONObject(i).toString(), Sjimglistbean.class));
                        }
                        settopcont();
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void settopcont(){
        for (int i = 0;i<sjimglist.size();i++){
            ImgoneFragment homeTabsFragment = new ImgoneFragment();
            Bundle bundle = new Bundle();
            bundle.putString("pid",sjimglist.get( i ).id);
            bundle.putString("name", sjimglist.get(i).name);
            bundle.putString("merid",getIntent().getStringExtra("merid"));
            homeTabsFragment.setArguments(bundle);
            fragmentList.add(homeTabsFragment);
        }
        viewPager.setOffscreenPageLimit(fragmentList.size());
        //适配器
        MyOderViewPagerAdapter myOderViewPagerAdapter = new MyOderViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(myOderViewPagerAdapter);
        CommonNavigator commonNavigator = new CommonNavigator(getComeActivity());
        commonNavigator.setSkimOver(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return sjimglist.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText( sjimglist.get( index ).name );
                clipPagerTitleView.setTextColor(getResources().getColor(R.color.col_666));
                clipPagerTitleView.setClipColor(getResources().getColor(R.color.red1));
                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(1);
                indicator.setColors(getResources().getColor(R.color.red1));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
        //设置页数
        viewPager.setCurrentItem(index);
    }
}
