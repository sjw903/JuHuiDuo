package com.android.jdhshop.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.PddAdAdapter;
import com.android.jdhshop.base.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PddAdActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private PddAdAdapter adapter;
    private List<String> list1=new ArrayList<>();
    private List<String> list2=new ArrayList<>();
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_attend_record);
        ButterKnife.bind(this);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableRefresh(false);
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("活动列表");
    }

    @Override
    protected void initData() {
        try {
            JSONArray array=new JSONArray(getIntent().getExtras().getString("urls"));
            for(int i=0;i<array.length();i++){
                list1.add(array.getJSONObject(i).getString("url"));
                list2.add(array.getJSONObject(i).getString("desc"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter=new PddAdAdapter(R.layout.item_pdd_ad,list1,list2);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(PddAdActivity.this, WebViewActivity.class);
                intent.putExtra("title", list2.get(position));
                intent.putExtra("url", list1.get(position));
                startActivity(intent);
            }
        });
    }
    @Override
    protected void initListener() {

    }
    @OnClick(R.id.tv_left)
    public void onViewClicked() {
        finish();
    }
}
