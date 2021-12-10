package com.android.jdhshop.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jdhshop.common.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.BrandlistActivity;
import com.android.jdhshop.adapter.HomeIconAdapter;
import com.android.jdhshop.adapter.PinPaiSelectAdapter;
import com.android.jdhshop.adapter.VipbuttonAdapter;
import com.android.jdhshop.adapter.ViptopAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.Baritem;
import com.android.jdhshop.bean.Item;
import com.android.jdhshop.bean.SetBean;
import com.android.jdhshop.bean.Vippptype;
import com.android.jdhshop.bean.Vipptitem;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.utils.RecyclerViewSpacesItemDecoration;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 会员中心    赚钱
 */
public class PinPaiFragment extends BaseActivity {

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.vip_toprecyclerview)
    RecyclerView top_recy;
    @BindView(R.id.left)
    RecyclerView left;
    @BindView(R.id.vip_buttonrecyclerview)
    RecyclerView button_recy;
    @BindView(R.id.al_ll)
    LinearLayout al_ll;
    @BindView(R.id.ll_top)
    LinearLayout ll_top;
    @BindView(R.id.right)
            RecyclerView right;
    List<String> str=new ArrayList<>(Arrays.asList("全部", "母婴童品", "百变女装", "食品酒水", "居家日用", "美妆洗护", "品质男装", "舒适内衣", "箱包配饰", "男女鞋靴","宠物用品","数码家电","车品文体"));
    boolean[] select={true,false,false,false,false,false,false,false,false,false,false,false};
    List<String> str2=new ArrayList<>(Arrays.asList( "母婴童品", "百变女装", "食品酒水", "居家日用", "美妆洗护", "品质男装", "舒适内衣", "箱包配饰", "男女鞋靴","宠物用品","数码家电","车品文体"));
    ViptopAdapter viptopAdapter;
    VipbuttonAdapter vipbuttonAdapter;
    private List<Baritem> list = new ArrayList<>();
    HomeIconAdapter homeIconAdapter;
    List<Item> items=new ArrayList<>();
    @BindView(R.id.tabBar)
    TabLayout magicIndicator;
    private String min_id="1",min_id2="1";
    public void settopbar() {
        for (int i = 0; i < 13; i++) {
            Baritem baritem = new Baritem();
            baritem.id = (i) + "";
            baritem.name = str.get(i);
            list.add(baritem);
        }
        for (int i = 0; i < str.size(); i++) {
            magicIndicator.addTab(magicIndicator.newTab().setText(str.get(i)));
        }
        magicIndicator.setTabMode(TabLayout.MODE_SCROLLABLE);
        reflex(magicIndicator);
        magicIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(tab.getPosition()==0){
//                            ll_top.setVisibility(View.GONE);
                            button_recy.setVisibility(View.GONE);
                            al_ll.setVisibility(View.VISIBLE);
                        }else{
                            pos2=tab.getPosition();
                            min_id2="1";
                            getviptype1();
//                            ll_top.setVisibility(View.VISIBLE);
                            button_recy.setVisibility(View.VISIBLE);
                            al_ll.setVisibility(View.GONE);

                        }
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
    PinPaiSelectAdapter adapter;
    int pos=1,pos2=1;

    public void reflex(final TabLayout tabLayout) {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

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
                        params.leftMargin = 15;
                        params.rightMargin = 15;
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
    protected void initUI() {
        setContentView(R.layout.activity_pinpai);
        ButterKnife.bind(this);
        tvLeft.setVisibility(View.VISIBLE);
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("优质品牌");
        left.setLayoutManager(new LinearLayoutManager(this));
        adapter=new PinPaiSelectAdapter(R.layout.tr,str2,select);
        left.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i=0;i<select.length;i++){
                    select[i]=false;
                }
                select[position]=true;
                pos=position+1;
                min_id="1";
                getviptype2();
                adapter.notifyDataSetChanged();
            }
        });
        right.setLayoutManager(new GridLayoutManager(this,3));
        homeIconAdapter=new HomeIconAdapter(this,R.layout.dd,items);
        right.setAdapter(homeIconAdapter);
        homeIconAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(PinPaiFragment.this, BrandlistActivity.class);
                intent.putExtra("title", items.get(position).name);
                intent.putExtra("id", items.get(position).id);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        settopbar();
//        getviptype();
        getviptype2();
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if(magicIndicator.getSelectedTabPosition()==0){
                    getviptype2();
                }else{
                    getviptype1();
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if(magicIndicator.getSelectedTabPosition()==0){
                    min_id="1";
                    getviptype2();
                }else{
                    min_id2="1";
                    getviptype1();
                }
            }
        });
        vipbuttonAdapter = new VipbuttonAdapter(this, typelist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        button_recy.setLayoutManager(layoutManager);
        button_recy.setAdapter(vipbuttonAdapter);
    }

    @Override
    protected void initData() {

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
        HttpUtils.post(Constants.GETPPLIST,PinPaiFragment.this, params, new TextHttpResponseHandler() {
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


                        viptopAdapter = new ViptopAdapter(PinPaiFragment.this, typelistss);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(PinPaiFragment.this, 2);
                        top_recy.addItemDecoration(new RecyclerViewSpacesItemDecoration(0, 5, 0, 5));
                        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        top_recy.setLayoutManager(gridLayoutManager);
                        top_recy.setAdapter(viptopAdapter);

                        viptopAdapter.setsubClickListener(new ViptopAdapter.SubClickListener() {
                            @Override
                            public void OntopicClickListener(View v, String detail, int posit) {
                                if (detail.equals("dj")) {
                                    Intent intent = new Intent(PinPaiFragment.this, BrandlistActivity.class);
                                    intent.putExtra("title", typelistss.get(posit).fq_brand_name);
                                    intent.putExtra("id", typelistss.get(posit).id);
                                    startActivity(intent);
                                }
                            }
                        });
                    } else {
//                        showToast(msg);
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

    public List<Vippptype> typelist = new ArrayList<>();

    private void getviptype1() {
        RequestParams params = new RequestParams();
        params.put("min_id",min_id2);
        params.put("brandcat", pos2);
        HttpUtils.post(Constants.GETPPLIST,PinPaiFragment.this, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // 输出错误信息
            }

            @Override
            public void onFinish() {
                // 隐藏进度条
                super.onFinish();
//                closeLoadingDialog();
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
                        if("1".equals(min_id2))
                            typelist.clear();
                        min_id2=jsonObject.optString("min_id");
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
                        vipbuttonAdapter.notifyDataSetChanged();

                    } else {
//                        showToast(msg);
                    }
                    refreshLayout.finishLoadMore();
                    refreshLayout.finishRefresh();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
//                showLoadingDialog();
            }
        });
    }
    private void getviptype2() {
        RequestParams params = new RequestParams();
        params.put("min_id", min_id);
        params.put("brandcat", pos+"");
//        params.put("back","21");
        HttpUtils.post(Constants.GETPPLIST,PinPaiFragment.this, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // 输出错误信息
            }

            @Override
            public void onFinish() {
                // 隐藏进度条
                super.onFinish();
//                closeLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("dsfasd",responseString);
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    //返回码
                    int code = jsonObject.optInt("code");
                    //返回码说明
                    String msg = jsonObject.optString("msg");
                    if (1 == code) {
                        if("1".equals(min_id))
                        items.clear();
                        min_id=jsonObject.optString("min_id");
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.getJSONObject(i);
                            Item type = new Item();
                            type.id = obj.getString("id");
                            type.name = obj.getString("tb_brand_name");
                            type.icon = obj.getString("brand_logo");
                            items.add(type);
                        }
                        homeIconAdapter.notifyDataSetChanged();
                    } else {
//                        showToast(msg);
                    }
                    refreshLayout.finishLoadMore();
                    refreshLayout.finishRefresh();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
//                showLoadingDialog();
            }
        });
    }

}
