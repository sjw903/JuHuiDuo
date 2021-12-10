package com.android.jdhshop.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.android.jdhshop.R;
import com.android.jdhshop.adapter.SjxxsAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.bean.Sjxxsbean;
import com.android.jdhshop.config.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : szp
 *     time   : 2020/05/08
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class SjxxsActivity extends BaseActivity {
    private List<Sjxxsbean> sjxxsbeanList = new ArrayList<>();
    private ImageView sjxxtop,fanhui;
    @Override
    protected void initUI() {
        setContentView( R.layout.activity_sjxxs );
        ButterKnife.bind( this );
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initSjxxsbean();
        RecyclerView recyclerView = findViewById( R.id.recycler_view);
        sjxxtop=findViewById(R.id.sjxxs_top);
        fanhui=findViewById(R.id.back);
        BaseLogDZiYuan.LogDingZiYuan(sjxxtop, "bg_shop.png");
        BaseLogDZiYuan.LogDingZiYuan(fanhui, "icon_back.png");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        recyclerView.setLayoutManager( linearLayoutManager );
        SjxxsAdapter adapter = new SjxxsAdapter(sjxxsbeanList);
        recyclerView.setAdapter( adapter );
    }
    private void initSjxxsbean() {
        for (int i = 0; i < 2; i++) {
            Sjxxsbean pj = new Sjxxsbean( "2019-09-18", "#不定时掉落的福利# 奶茶来啦~ 醇香奶茶搭配香滑补丁、\tQ弹珍珠及鲜嫩口感的仙草冻~口感丰富，一次满足！9.9元/杯，限量1W份，先到先得哦~", R.mipmap.default_cover, "浏览 10万+", R.mipmap.icon_dynamic_praice, "2.9W", R.mipmap.icon_dynamic_comment, "1333", R.mipmap.icon_dynamic_share );
            sjxxsbeanList.add( pj );
            Sjxxsbean pj1  = new Sjxxsbean( "2019-09-18", "#不定时掉落的福利# 奶茶来啦~ 醇香奶茶搭配香滑补丁、\tQ弹珍珠及鲜嫩口感的仙草冻~口感丰富，一次满足！9.9元/杯，限量1W份，先到先得哦~", R.mipmap.default_cover, "浏览 10万+", R.mipmap.icon_dynamic_praice, "2.9W", R.mipmap.icon_dynamic_comment, "1333", R.mipmap.icon_dynamic_share );
            sjxxsbeanList.add( pj1 );
        }


    }
    @Override
    protected void initData() {
    }
    @Override
    protected void initListener() {

    }
}
