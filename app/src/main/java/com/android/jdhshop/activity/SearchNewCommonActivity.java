package com.android.jdhshop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jdhshop.adapter.SearchHistoryNewAdapter;
import com.android.jdhshop.bean.SearchHistoryBean;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.utils.StringUtils;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.MyOderViewPagerAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.fragments.JdSearchRestultFragment;
import com.android.jdhshop.fragments.PddSearchResultFragment;
import com.android.jdhshop.fragments.SearchResultTbFragment;
import com.android.jdhshop.fragments.SuningGoodlistFragment;
import com.android.jdhshop.fragments.WphSearchResultFragment;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.utils.BitmapUtils;
import com.android.jdhshop.widget.AutoClearEditText;
import com.android.jdhshop.widget.indicator.MagicIndicator;
import com.android.jdhshop.widget.indicator.ViewPagerHelper;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.CommonNavigator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class SearchNewCommonActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    AutoClearEditText tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.view_page)
    ViewPager viewPage;
    List<SearchHistoryBean> historyBeans = new ArrayList<>();
    private SearchHistoryNewAdapter searchHistoryAdapter;
    private ACache aCache;
    private List<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> list = new ArrayList<>();
    SearchResultTbFragment tbFragment=new SearchResultTbFragment();
    PddSearchResultFragment pddSearchResultFragment=new PddSearchResultFragment();
    JdSearchRestultFragment jdSearchRestultFragment=new JdSearchRestultFragment();
    WphSearchResultFragment wphSearchResultFragment=new WphSearchResultFragment();
    SuningGoodlistFragment suningGoodlistFragment=new SuningGoodlistFragment();
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_common_search);
        ButterKnife.bind(this);
        aCache = ACache.get(getComeActivity());
        if (!"".equals(SPUtils.getStringData(getComeActivity(),"token",""))) {
            ifShouQuan();
        }
    }
    private void ifShouQuan() {
        RequestParams params = new RequestParams();
        HttpUtils.post(Constants.APP_IP+"/api/Pdd/judgeUser",SearchNewCommonActivity.this, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onFinish() {
                // 隐藏进度条
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("dfasdf",responseString);
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    //返回码
                    int code = jsonObject.optInt("code");
                    if (0 == code) {
                    } else {
                        showTipDialog3("温馨提示", "检测到您未绑定拼多多，请先授权", new onClickListener() {
                            @Override
                            public void onClickSure() {
                                getUrl();
                            }
                        }, new onClickListener() {
                            @Override
                            public void onClickSure() {

                            }
                        }, "绑定", "暂不绑定");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
            }
        });

    }

    private void getUrl() {
        RequestParams params = new RequestParams();
        HttpUtils.post(Constants.APP_IP+"/api/Pdd/getUrl",SearchNewCommonActivity.this, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onFinish() {
                // 隐藏进度条
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    //返回码
                    int code = jsonObject.optInt("code");
                    if (0 == code) {
                        Intent intent = new Intent(SearchNewCommonActivity.this, WebViewActivity.class);
                        intent.putExtra("title", "绑定拼多多");
                        intent.putExtra("url", jsonObject.getJSONObject("data").getJSONArray("url_list").getJSONObject(0).getString("mobile_url"));
                        startActivity(intent);
                    } else {
                        showToast(jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
            }
        });

    }
    @Override
    protected void initData() {

        tvTitle.setText(getIntent().getExtras().getString("content"));
        tvTitle.setSelection(tvTitle.getText().toString().length());
        tvLeft.setVisibility(View.VISIBLE);
        list.add("淘宝");
        list.add("拼多多");
        list.add("京东");
        list.add("唯品会");
        list.add("苏宁");
        fragments.add(tbFragment);
        fragments.add(pddSearchResultFragment);
        fragments.add(jdSearchRestultFragment);
        fragments.add(wphSearchResultFragment);
        fragments.add(suningGoodlistFragment);
        viewPage.setOffscreenPageLimit(fragments.size());
        MyOderViewPagerAdapter myOderViewPagerAdapter = new MyOderViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPage.setAdapter(myOderViewPagerAdapter);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setSkimOver(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public IPagerTitleView getTitleView(final Context context, final int index) {
                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);
                commonPagerTitleView.setContentView(R.layout.item_text2);
                final TextView titleText = commonPagerTitleView.findViewById(R.id.txt_cat);
                titleText.setLayoutParams(new LinearLayout.LayoutParams(BitmapUtils.getScreenWith(context)/5,LinearLayout.LayoutParams.MATCH_PARENT));
                titleText.setText(list.get(index));

                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {

                    @Override
                    public void onDeselected(int i, int i1) {
                        titleText.setTextColor(getResources().getColor(R.color.col_999));
                    }

                    @Override
                    public void onSelected(int i, int i1) {
                        titleText.setTextColor(getResources().getColor(R.color.red1));
                    }

                    @Override
                    public void onEnter(int i, int i1, float v, boolean b) {
                    }

                    @Override
                    public void onLeave(int i, int i1, float v, boolean b) {

                    }
                });

                commonPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPage.setCurrentItem(index);
                    }
                });
                return commonPagerTitleView;
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
        ViewPagerHelper.bind(magicIndicator, viewPage);
        //设置页数
        viewPage.setCurrentItem(0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tbFragment.setContent(tvTitle.getText().toString().trim());
                pddSearchResultFragment.setKeyword(tvTitle.getText().toString().trim());
                jdSearchRestultFragment.setKeys(tvTitle.getText().toString().trim());
                wphSearchResultFragment.setContent(tvTitle.getText().toString().trim());
                suningGoodlistFragment.setContent(tvTitle.getText().toString().trim());

            }
        },200);
    }

    @Override
    protected void initListener() {

    }

    @OnClick({R.id.tv_left, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.tv_right:
                if("".equals(tvTitle.getText().toString().trim())){
                    ToastUtils.showLongToast(this,"请输入搜索内容");
                    return;
                }
                List<SearchHistoryBean> searchHistoryBeans = (List<SearchHistoryBean>) aCache.getAsObject(Constants.HISTORICAL_RECORDS);
                if (searchHistoryBeans != null && searchHistoryBeans.size() > 0) {
                    historyBeans.clear();
                    historyBeans.addAll(searchHistoryBeans);

                }
                SearchHistoryBean searchHistoryBean = new SearchHistoryBean();
                searchHistoryBean.setContent(StringUtils.doViewToString(tvTitle));
                if (!historyBeans.contains(searchHistoryBean)) {
                    historyBeans.add(searchHistoryBean);
                    aCache.put(Constants.HISTORICAL_RECORDS, (Serializable) historyBeans);
                }
                tbFragment.setContent(tvTitle.getText().toString().trim());
                pddSearchResultFragment.setKeyword(tvTitle.getText().toString().trim());
                jdSearchRestultFragment.setKeys(tvTitle.getText().toString().trim());
                wphSearchResultFragment.setContent(tvTitle.getText().toString().trim());
                suningGoodlistFragment.setContent(tvTitle.getText().toString().trim());

                break;
        }
    }
}
