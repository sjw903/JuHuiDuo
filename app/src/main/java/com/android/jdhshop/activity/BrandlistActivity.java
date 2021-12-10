package com.android.jdhshop.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.BrandlistAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.Vipptitem;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * 品牌列表
 */
public class BrandlistActivity extends BaseActivity{

    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private TextView tv_name, tv_cont;
    private ImageView iv_img;
    private RecyclerView recyclerView;
    private String min_id = "1";
    BrandlistAdapter brandlistAdapter;
  private List<Vipptitem>  typelistss = new ArrayList<>();

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_brandlist);
        ButterKnife.bind(this);
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText(getIntent().getStringExtra("title"));
    }

    @Override
    protected void initData() {
        LinearLayout head = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.brand_top, null);
        tv_name = head.findViewById(R.id.brandlist_tvname);
        tv_cont = head.findViewById(R.id.brandlist_tvcont);
        iv_img = head.findViewById(R.id.brandlist_img);
        recyclerView = findViewById(R.id.brandlist_recy);
        brandlistAdapter = new BrandlistAdapter(R.layout.itembutton_itemto, typelistss);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(BrandlistActivity.this, 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        brandlistAdapter.addHeaderView(head);
        recyclerView.setLayoutManager(gridLayoutManager);
        refreshLayout.setEnableLoadMore(false);
        recyclerView.setAdapter(brandlistAdapter);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getviptype();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                min_id="1";
                getviptype();
            }
        });
        getviptype();
    }

    @Override
    protected void initListener() {

    }
    private void getviptype() {
        RequestParams params = new RequestParams();
        params.put("id", getIntent().getStringExtra("id"));
        params.put("min_id", min_id);
        HttpUtils.post(Constants.getBrandMsg,BrandlistActivity.class, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // 输出错误信息
            }

            @Override
            public void onFinish() {
                // 隐藏进度条
                super.onFinish();
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
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
                        if("1".equals(min_id)){
                            typelistss.clear();
                        }
                        min_id = jsonObject.getString("min_id");
                        String datas = jsonObject.getString("data");
                        JSONObject data = new JSONObject(datas);
                        String fq_brand_name = data.getString("fq_brand_name");
                        String brand_logo = data.getString("brand_logo");
                        String introduce = data.getString("introduce");
                        JSONArray array = data.getJSONArray("items");
                        for (int j = 0; j < array.length(); j++) {
                            typelistss.add(new Gson().fromJson(array.getJSONObject(j).toString(), Vipptitem.class));
                        }
                        Glide.with(BrandlistActivity.this).load(brand_logo).error(R.mipmap.app_icon).into(iv_img);
                        tv_name.setText(fq_brand_name);
                        tv_cont.setText(introduce);
                        brandlistAdapter.notifyDataSetChanged();
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
    @OnClick(R.id.tv_left)
    public void onViewClicked() {
        finish();
    }
}
