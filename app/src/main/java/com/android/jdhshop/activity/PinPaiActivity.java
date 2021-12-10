package com.android.jdhshop.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.adapter.VipbuttonAdapter;
import com.android.jdhshop.adapter.ViptopAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.Baritem;
import com.android.jdhshop.bean.Vippptype;
import com.android.jdhshop.bean.Vipptitem;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.utils.RecyclerViewSpacesItemDecoration;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.uuch.adlibrary.utils.DisplayUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 会员中心    赚钱
 */
public class PinPaiActivity extends BaseActivity {

    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.vip_toprecyclerview)
    RecyclerView top_recy;
    @BindView(R.id.vip_buttonrecyclerview)
    RecyclerView button_recy;

    String[] str = {"母婴童品", "百变女装", "食品酒水", "居家日用", "美妆洗护", "品质男装", "舒适内衣", "箱包配饰", "男女鞋靴", "宠物用品", "数码家电", "车品文体"};

    ViptopAdapter viptopAdapter;
    VipbuttonAdapter vipbuttonAdapter;
    private List<Baritem> list = new ArrayList<>();

    @BindView(R.id.tabBar)
    TabLayout magicIndicator;


    public void settopbar() {
        for (int i = 0; i < 12; i++) {
            Baritem baritem = new Baritem();
            baritem.id = (i + 1) + "";
            baritem.name = str[i];
            list.add(baritem);
        }
        for (int i = 0; i < str.length; i++) {
            magicIndicator.addTab(magicIndicator.newTab().setText(str[i]));
        }
        magicIndicator.setTabMode(TabLayout.MODE_SCROLLABLE);
        reflex(magicIndicator);
        magicIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getviptype1((tab.getPosition() + 1) + "");
                    }
                }, 100);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_pinpai);
        ButterKnife.bind(this);
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

    @Override
    protected void initData() {
        tvLeft.setVisibility(View.VISIBLE);
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("优质品牌");
        settopbar();
        getviptype();
        getviptype1(1 + "");
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    String openstr = "2";


    public List<Vippptype> typelistss;

    private void getviptype() {
        RequestParams params = new RequestParams();
        params.put("min_id", "1");
        HttpUtils.post(Constants.GETPPLIST, PinPaiActivity.this, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // 输出错误信息
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
                    //返回码说明
                    String msg = jsonObject.optString("msg");
                    if (1 == code) {
                        typelistss = new ArrayList<>();
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.getJSONObject(i);
                            Vippptype type = new Vippptype();
                            type.id = obj.getString("id");
                            type.tb_brand_name = obj.getString("tb_brand_name");
                            type.fq_brand_name = obj.getString("fq_brand_name");
                            type.brand_logo = obj.getString("brand_logo");
                            type.brandcat = obj.getString("brandcat");
                            type.introduce = obj.getString("introduce");
                            JSONArray array = obj.getJSONArray("item");
                            for (int j = 0; j < array.length(); j++) {
                                type.item.add(new Gson().fromJson(array.getJSONObject(j).toString(), Vipptitem.class));
                            }
                            typelistss.add(type);
                        }
//                        Log.e( "home",""+typelist.size()+","+typelist.get( 0 ).item.size() );


                        viptopAdapter = new ViptopAdapter(PinPaiActivity.this, typelistss);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(PinPaiActivity.this, 2);
                        top_recy.addItemDecoration(new RecyclerViewSpacesItemDecoration(0, 5, 0, 5));
                        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        top_recy.setLayoutManager(gridLayoutManager);
                        top_recy.setAdapter(viptopAdapter);

                        viptopAdapter.setsubClickListener(new ViptopAdapter.SubClickListener() {
                            @Override
                            public void OntopicClickListener(View v, String detail, int posit) {
                                if (detail.equals("dj")) {
                                    Intent intent = new Intent(PinPaiActivity.this, BrandlistActivity.class);
                                    intent.putExtra("title", typelistss.get(posit).fq_brand_name);
                                    intent.putExtra("id", typelistss.get(posit).id);
                                    startActivity(intent);
                                }
                            }
                        });
                    } else {
                        showToast(msg);
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

    public List<Vippptype> typelist;

    private void getviptype1(String index) {
        RequestParams params = new RequestParams();
        params.put("min_id", "1");
        params.put("brandcat", index);
        HttpUtils.post(Constants.GETPPLIST, PinPaiActivity.this, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // 输出错误信息
            }

            @Override
            public void onFinish() {
                // 隐藏进度条
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    //返回码
                    int code = jsonObject.optInt("code");
                    //返回码说明
                    String msg = jsonObject.optString("msg");
                    if (1 == code) {
                        typelist = new ArrayList<>();
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.getJSONObject(i);
                            Vippptype type = new Vippptype();
                            type.id = obj.getString("id");
                            type.tb_brand_name = obj.getString("tb_brand_name");
                            type.fq_brand_name = obj.getString("fq_brand_name");
                            type.brand_logo = obj.getString("brand_logo");
                            type.brandcat = obj.getString("brandcat");
                            type.introduce = obj.getString("introduce");
                            JSONArray array = obj.getJSONArray("item");
                            for (int j = 0; j < array.length(); j++) {
                                type.item.add(new Gson().fromJson(array.getJSONObject(j).toString(), Vipptitem.class));
                            }
                            typelist.add(type);
                        }
                        vipbuttonAdapter = new VipbuttonAdapter(PinPaiActivity.this, typelist);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(PinPaiActivity.this);
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        button_recy.setLayoutManager(layoutManager);
                        button_recy.setAdapter(vipbuttonAdapter);
                        vipbuttonAdapter.notifyDataSetChanged();
                    } else {
                        showToast(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }
        });
    }

}
