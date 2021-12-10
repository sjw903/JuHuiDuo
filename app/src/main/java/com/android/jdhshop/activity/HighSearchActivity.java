package com.android.jdhshop.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.utils.StringUtils;
import com.android.jdhshop.ziyuan.ResourceManager;
import com.tencent.mm.opensdk.utils.Log;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.util.TextUtils;

/**
 * 高级搜索
 * Created by yohn on 2018/8/24.
 */

public class HighSearchActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_left)
    TextView tv_left;
    //来源
    @BindView(R.id.cb_tm)
    CheckBox cb_tm;
    @BindView(R.id.cb_bm)
    CheckBox cb_bm;
    //券后价区间
    @BindView(R.id.et_start)
    EditText et_start;
    @BindView(R.id.et_end)
    EditText et_end;

    @BindView(R.id.et_start1)
    EditText et_start1;
    @BindView(R.id.et_end1)
    EditText et_end1;

    @BindView(R.id.et_keyword)
    EditText et_keyword;

    @BindView(R.id.tv_search)
    TextView tv_search;


    @Override
    protected void initUI() {
        setContentView( R.layout.ac_high_search );
        ButterKnife.bind( this );
    }

    @Override
    protected void initData() {
        tvTitle.setText( "高级搜索" );
        tv_left.setVisibility( View.VISIBLE );
    }

    @Override
    protected void initListener() {
        cb_bm.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cb_tm.setChecked( false );
                }
            }
        } );
        cb_tm.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cb_bm.setChecked( false );
                }
            }
        });
        tv_left.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );

        tv_search.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double shareOneDb=0;
                double shareTwoDb=0;
                String stMoney =StringUtils.doViewToString(et_start);
                String enMoney =StringUtils.doViewToString(et_end);
                String shareOne = StringUtils.doViewToString(et_start1);
                String shareTwo = StringUtils.doViewToString(et_end1);
                if(!shareOne.equals(""))
                 shareOneDb=Double.parseDouble(shareOne)*100;
                if(!shareTwo.equals(""))
                 shareTwoDb=Double.parseDouble(shareTwo)*100;

                String keyword = StringUtils.doViewToString(et_keyword);
                if(TextUtils.isEmpty( keyword )){
                    showToast( "请输入关键词" );
                    return;
                }
                int tmall=-1;//0 天猫 1 淘宝  -1 都不是
                if(cb_tm.isChecked()){
                    tmall=0;
                }else if(cb_bm.isChecked()){
                    tmall=1;
                }else{
                    tmall=-1;
                }
                Bundle bundle=new Bundle(  );
                bundle.putString( "content",keyword );
                bundle.putString( "stMoney",stMoney );//券后区间价开始
                bundle.putString( "enMoney",enMoney );//券后区间价结束
                bundle.putString( "shareOne",shareOne );//分享比例开始值
                    bundle.putString( "shareTwo",shareTwo );//分享比例结束值

                bundle.putInt( "tmall",tmall );//来源
                bundle.putInt( "type",1 );//1高级搜索 0 普通搜索
                openActivity(SearchResultActivity.class,bundle);
            }
        } );
    }
}
