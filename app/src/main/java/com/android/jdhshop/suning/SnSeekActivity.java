package com.android.jdhshop.suning;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.base.BaseLogDZiYuan;

public class SnSeekActivity extends BaseActivity {


    private EditText et_cont;
    private LinearLayout ly_back;
    private TextView tv_button;
    private ImageView fanhui;
    @Override
    protected void initUI() {
        setStatusBar(Color.parseColor("#F4B945"));
        setContentView( R.layout.activity_sn_seek );
    }

    @Override
    protected void initData() {
        et_cont = findViewById( R.id.snseek_etcont );
        ly_back = findViewById( R.id.snseek_lyback );
        tv_button = findViewById( R.id.snseek_button );
        fanhui = findViewById( R.id.seek_fanhui );
        BaseLogDZiYuan.LogDingZiYuan(fanhui, "icon_back_while.png");
        ly_back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        } );
        tv_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( SnSeekActivity.this,SuningGoodlistActivity.class );
                intent.putExtra( "id", "");
                intent.putExtra( "title", et_cont.getText().toString());
                intent.putExtra( "url","/api/Suning/getKeywordGoods" );
                intent.putExtra( "seek",et_cont.getText().toString() );
                startActivity( intent );
            }
        } );
    }

    @Override
    protected void initListener() {

    }
}
