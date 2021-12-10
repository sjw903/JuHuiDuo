package com.android.jdhshop.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.my.MyOrderActivity;
import com.android.jdhshop.suning.SnSeekActivity;
import com.android.jdhshop.suning.SuningGoodlistActivity;
import com.android.jdhshop.ziyuan.ResourceManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author wim
 */
public class DialogActivity2 extends Activity {
    @BindView(R.id.ss)
    TextView ss;
    @BindView(R.id.txt_tip)
    TextView ss2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_dialog2);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ButterKnife.bind(this);
        setFinishOnTouchOutside(true);
        ss.setText(getIntent().getStringExtra("search"));
        LogUtils.d("可能复制了", "onCreate: "+getIntent().getStringExtra("search"));

        if(TextUtils.isDigitsOnly(ss.getText().toString())){
            ss2.setText("您可能复制了订单号，是否前去搜索");
            findViewById(R.id.ll_root).setVisibility(View.GONE);
        }else{
            ss2.setText("您可能复制了商品标题，是否前去搜索");
            findViewById(R.id.ll_root).setVisibility(View.VISIBLE);
            findViewById(R.id.txt_ok).setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.txt_cancle, R.id.txt_ok,R.id.img_t,R.id.img_d,R.id.img_p,R.id.img_z,R.id.img_x})
    public void onViewClicked(View view) {
        Intent intent=new Intent(this,SearchResultActivity.class);
        switch (view.getId()) {
            case R.id.txt_cancle:
                finish();
                break;
            case R.id.img_t:
                Bundle bundle = new Bundle();
                bundle.putString("content",ss.getText().toString());
                bundle.putInt("type", 0);//1高级搜索 0 普通搜索
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
            case R.id.img_d:
                intent=new Intent(this, JdSearchRestultActivity.class);
                intent.putExtra("key", ss.getText().toString().trim());
                startActivity(intent);
                finish();
                break;
            case R.id.img_p:
                intent=new Intent(this,PddSearchResultActivity.class);
                intent.putExtra("keyword",ss.getText().toString().trim());
                startActivity(intent);
                finish();
                break;
            case R.id.img_z:
                intent=new Intent(this,WphSearchActivity.class);
                intent.putExtra("keyword",ss.getText().toString().trim());
                startActivity(intent);
                finish();
                break;
            case R.id.img_x:
                 intent = new Intent( this, SuningGoodlistActivity.class );
                intent.putExtra( "id", "");
                intent.putExtra( "title",ss.getText().toString().trim());
                intent.putExtra( "url","/api/Suning/getKeywordGoods" );
                intent.putExtra( "seek",ss.getText().toString().trim() );
                startActivity( intent );
                finish();
                break;
            case R.id.txt_ok:
                if(TextUtils.isDigitsOnly(ss.getText().toString())){
                    startActivity(new Intent(this,MyOrderActivity.class).putExtra("se",ss.getText().toString()));
                    finish();
                }else{
//                    Bundle bundle = new Bundle();
//                    bundle.putString("content",ss.getText().toString());
//                    bundle.putInt("type", 0);//1高级搜索 0 普通搜索
//                    Intent intent=new Intent(this,SearchResultActivity.class);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                    finish();
                }
                break;
        }
    }
}
