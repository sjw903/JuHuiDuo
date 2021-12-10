package com.android.jdhshop.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.common.SPUtils;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.SearchHistoryNewAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.HotSearchBean;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.SearchHistoryBean;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.utils.StringUtils;
import com.android.jdhshop.widget.AutoClearEditText;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * @属性:搜索页面
 * @开发者:陈飞
 * @时间:2018/7/28 13:05
 */
public class SearchActivity extends BaseActivity {

    @BindView(R.id.bg_head)
    LinearLayout bg_head;
    @BindView(R.id.tv_title)
    AutoClearEditText tvTitle;
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.grid_view)
    GridView gridView;
    @BindView(R.id.fy_hot)
    TagFlowLayout fy_hot;
    //高级
    @BindView(R.id.tv_senior)
    TextView tv_senior;
    @BindView(R.id.delete_icon)
    ImageView delete_icon;
    List<SearchHistoryBean> historyBeans = new ArrayList<>();
    @BindView(R.id.search_lv_tips)
    ListView searchLvTips;
    private SearchHistoryNewAdapter searchHistoryAdapter;
    private ACache aCache;
    private List<String> searchTips=new ArrayList<>();
    private ArrayAdapter searchAdapter;

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        BaseLogDZiYuan.LogDingZiYuan(delete_icon,"btn_blue.png");
    }

    @Override
    protected void initData() {
        tvTitle.setBackground(getResources().getDrawable(R.drawable.bg_round_gray));
        tvTitle.setTextColor(getResources().getColor(R.color.col_333));
        tv_left.setVisibility(View.VISIBLE);
//        bg_head.setBackgroundColor(getResources(). getColor( R.color.app_main_color ) );
        aCache = ACache.get(getComeActivity());

        //searchHistoryAdapter = new SearchHistoryAdapter(getComeActivity(), R.layout.search_history_item, historyBeans);
        searchHistoryAdapter = new SearchHistoryNewAdapter(this);
        searchHistoryAdapter.setData(historyBeans);
        gridView.setAdapter(searchHistoryAdapter);
        getHotSearch();
    }

    @Override
    protected void initListener() {
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SearchHistoryBean searchHistoryBean = historyBeans.get(i);
                if (searchHistoryBean != null) {
                    //  BroadcastManager.getInstance(getComeActivity()).sendBroadcast(BroadcastContants.sendSearchMessage, searchHistoryBean.getContent());
                    Bundle bundle = new Bundle();
                    bundle.putString("content", searchHistoryBean.getContent());
                    openActivity(SearchNewCommonActivity.class, bundle);
//                    finish();
                }
            }
        });
        //高级搜索
        tv_senior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(HighSearchActivity.class);
            }
        });
        tvTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!TextUtils.isEmpty(StringUtils.doViewToString(tvTitle))) {
                    ((InputMethodManager) tvTitle.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    getComeActivity()
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    //存储历史
                    SearchHistoryBean searchHistoryBean = new SearchHistoryBean();
                    searchHistoryBean.setContent(StringUtils.doViewToString(tvTitle));
                    if (!historyBeans.contains(searchHistoryBean)) {
                        historyBeans.add(searchHistoryBean);
                        aCache.put(Constants.HISTORICAL_RECORDS, (Serializable) historyBeans);
                        searchHistoryAdapter.setData(historyBeans);
                        gridView.setAdapter(searchHistoryAdapter);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("content", StringUtils.doViewToString(tvTitle));
                    openActivity(SearchNewCommonActivity.class, bundle);
                } else {
                    showToast("你未输入搜索内容");
                }
                return false;
            }
        });
        tvTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equals("")){
                    searchLvTips.setVisibility(View.GONE);
                }else{
                    searchLvTips.setVisibility(View.VISIBLE);
                    getTip();
                }
            }
        });
        searchAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, searchTips);
        searchLvTips.setAdapter(searchAdapter);
        searchLvTips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = searchTips.get(position);
                //存储历史
                SearchHistoryBean searchHistoryBean = new SearchHistoryBean();
                searchHistoryBean.setContent(s);
                if (!historyBeans.contains(searchHistoryBean)) {
                    historyBeans.add(searchHistoryBean);
                    aCache.put(Constants.HISTORICAL_RECORDS, (Serializable) historyBeans);
                    searchHistoryAdapter.setData(historyBeans);
                    gridView.setAdapter(searchHistoryAdapter);
                }
                Bundle bundle = new Bundle();
                bundle.putString("content", searchTips.get(position));
                openActivity(SearchNewCommonActivity.class, bundle);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //读取历史
        List<SearchHistoryBean> searchHistoryBeans = (List<SearchHistoryBean>) aCache.getAsObject(Constants.HISTORICAL_RECORDS);
        if (searchHistoryBeans != null && searchHistoryBeans.size() > 0) {
            historyBeans.clear();
            historyBeans.addAll(searchHistoryBeans);
            searchHistoryAdapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.tv_right, R.id.delete_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_right: //确定
                if (!TextUtils.isEmpty(StringUtils.doViewToString(tvTitle))) {
                    //存储历史
                    SearchHistoryBean searchHistoryBean = new SearchHistoryBean();
                    searchHistoryBean.setContent(StringUtils.doViewToString(tvTitle));
                    if (!historyBeans.contains(searchHistoryBean)) {
                        historyBeans.add(searchHistoryBean);
                        aCache.put(Constants.HISTORICAL_RECORDS, (Serializable) historyBeans);
                        searchHistoryAdapter.setData(historyBeans);
                        gridView.setAdapter(searchHistoryAdapter);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("content", StringUtils.doViewToString(tvTitle));
                    openActivity(SearchNewCommonActivity.class, bundle);
//                    BroadcastManager.getInstance(getComeActivity()).sendBroadcast(BroadcastContants.sendSearchMessage, StringUtils.doViewToString(tvTitle));
                } else {
                    showToast("你未输入搜索内容");
                }
                break;
            case R.id.delete_icon://清空
                aCache.remove(Constants.HISTORICAL_RECORDS);
                //读取历史
                historyBeans.clear();
                List<SearchHistoryBean> searchHistoryBeans = (List<SearchHistoryBean>) aCache.getAsObject(Constants.HISTORICAL_RECORDS);
                if (searchHistoryBeans != null && searchHistoryBeans.size() > 0) {
                    historyBeans.addAll(searchHistoryBeans);
                    searchHistoryAdapter.notifyDataSetChanged();
                }
                searchHistoryAdapter.notifyDataSetChanged();
                break;
        }
    }
    private void getTip(){
        RequestParams requestParams = new RequestParams();
        HttpUtils.post("https://suggest.taobao.com/sug?&code=utf-8&q="+(tvTitle.getText().toString().replaceAll(" ",""))+"&callback=jQuery22408778692875219454_1542943610945&_=1542943610950&qq-pf-to=pcqq.group",SearchActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogUtils.d("dsfasd",responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                String str=responseString.replace("jQuery22408778692875219454_1542943610945(","");
                str=str.substring(0,str.length()-1);
                try {
                    JSONObject object=new JSONObject(str);
                    JSONArray array=object.getJSONArray("result");
                    searchTips.clear();
                    for(int i=0;i<array.length();i++){
                        JSONArray array1=array.getJSONArray(i);
                        searchTips.add(array1.getString(0));
                    }
                    searchAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    searchLvTips.setVisibility(View.GONE);
                }
            }
        });
    }
    /**
     * 热门搜索
     */
    private void getHotSearch() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("num", 10);//条数，默认10条
        HttpUtils.post(Constants.GET_HOT_SEARCH, SearchActivity.this,requestParams, new onOKJsonHttpResponseHandler<HotSearchBean>(new TypeToken<Response<HotSearchBean>>() {
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
            public void onSuccess(int statusCode, Response<HotSearchBean> datas) {
                if (datas.isSuccess()) {
                    final List<HotSearchBean.HotSearchChildBean> list = datas.getData().getList();
                    //热门搜索
                    setHotSearch(list);
                } else {
                    showToast(datas.getMsg());
                }
            }
        });
    }

    //设置热门搜索标签
    private void setHotSearch(final List<HotSearchBean.HotSearchChildBean> list) {
        {
            final TagAdapter purposeAdapter = new TagAdapter<HotSearchBean.HotSearchChildBean>(list) {

                @Override
                public View getView(FlowLayout parent, int position, HotSearchBean.HotSearchChildBean s) {
                    TextView tv = (TextView) LayoutInflater.from(SearchActivity.this).inflate(R.layout.tv1,
                            fy_hot, false);
                    tv.setTextColor(getResources().getColor(R.color.gray));
                    tv.setBackground(getResources().getDrawable(R.drawable.bg_hot_search));
                    tv.setText(s.getSearch());
                    tv.setTag(s.getId());
                    tv.setPadding(40, 0, 40, 0);

//                    //编辑赋值
//                    for (int j = 0; j < list.size(); j++) {
//                        if (s.id.equals(purposeSelect.get(j))) {
//                            tv.setTextColor(getResources().getColor(R.color.app_main_color));
//                            tv.setBackground(getResources().getDrawable(R.drawable.bg_visit_select_purpose));
//                            tv.setPadding(40, 0, 40, 0);
//                        }
//                    }
                    return tv;
                }
            };
            fy_hot.setAdapter(purposeAdapter);
            fy_hot.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
//                    String id = list.get(position).getId();
//                    BroadcastManager.getInstance(getComeActivity()).sendBroadcast(BroadcastContants.sendSearchMessage,list.get(position).getSearch() );
                    //存储历史
                    String search = list.get(position).getSearch();
                    SearchHistoryBean searchHistoryBean = new SearchHistoryBean();
                    searchHistoryBean.setContent(search);
                    if (!historyBeans.contains(searchHistoryBean)) {
                        historyBeans.add(searchHistoryBean);
                        aCache.put(Constants.HISTORICAL_RECORDS, (Serializable) historyBeans);
                        searchHistoryAdapter.setData(historyBeans);
                        gridView.setAdapter(searchHistoryAdapter);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("content", list.get(position).getSearch());
                    openActivity(SearchNewCommonActivity.class, bundle);
//                    finish();
//                    //判断是否选择
//                    if (purposeSelect.contains(id)) {//已选择
//                        for (int s = 0; s < purposeSelect.size(); s++) {
//                            if (purposeSelect.get(s).equals(id)) {
//                                purposeSelect.remove(id);
//                            }
//                        }
//                    } else {//未选择
//                        purposeSelect.add(id);
//                    }
//                    purposeAdapter.notifyDataChanged();
                    return true;
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
