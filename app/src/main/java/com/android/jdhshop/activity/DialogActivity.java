package com.android.jdhshop.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jdhshop.ziyuan.ResourceManager;
import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.MyGoodsResp;
import com.android.jdhshop.bean.PDDBean;
import com.android.jdhshop.bean.Wphbean;
import com.tencent.mm.opensdk.utils.Log;

import jd.union.open.goods.query.response.GoodsResp;

/**
 * @author wim
 */
public class DialogActivity extends Activity {
    private ImageView pic;
    private TextView textView;
    private Button detail;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_dialog);
        pic=findViewById(R.id.img_pic);
        textView=findViewById(R.id.txt_title);
        detail = findViewById(R.id.detail);
        Glide.with(this).load(getIntent().getStringExtra("pic")).dontAnimate().into(pic);
        textView.setText(getIntent().getStringExtra("title"));
        findViewById(R.id.detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("tb".equals(getIntent().getStringExtra("type"))){
                    Intent intent = new Intent(DialogActivity.this, PromotionDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("tkl", getIntent().getStringExtra("url"));
                    bundle.putString("num_iid",getIntent().getStringExtra("num_iid"));
                    bundle.putString("commission",getIntent().getStringExtra("commission"));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else if("pdd".equals(getIntent().getStringExtra("type"))){
                    Intent intent=new Intent(DialogActivity.this,PddDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("goods", (PDDBean)getIntent().getBundleExtra("goods").get("goods"));
                    intent.putExtra("goods",bundle);
                    startActivity(intent);
                }else if("jd".equals(getIntent().getStringExtra("type"))){
                    Intent intent=new Intent(DialogActivity.this,JdDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("goods", (MyGoodsResp)getIntent().getBundleExtra("goods").get("goods"));
                    intent.putExtra("goods",bundle);
                    startActivity(intent);
                }else if("vip".equals(getIntent().getStringExtra("type"))){
                    Intent intent=new Intent(DialogActivity.this,WphDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("goods", (Wphbean)getIntent().getBundleExtra("goods").get("goods"));
                    intent.putExtra("goods",bundle);
                    startActivity(intent);
                }else if("suning".equals(getIntent().getStringExtra("type"))){
                    Intent intent = new Intent(DialogActivity.this,WebViewActivity3.class);
                    intent.putExtra("url",getIntent().getStringExtra("url"));
                    intent.putExtra("title","苏宁易购");
                    startActivity(intent);
                }else if("jdweb".equals(getIntent().getStringExtra("type"))){
                    Intent intent=new Intent(DialogActivity.this,WebViewActivityNotOpenDefaultWeb.class);
                    Bundle bundle = new Bundle();
                    intent.putExtra("title",getIntent().getStringExtra("title"));
                    intent.putExtra("url",getIntent().getStringExtra("url"));
                    startActivity(intent);
                }else if("tbweb".equals(getIntent().getStringExtra("type"))){
                    Intent intent=new Intent(DialogActivity.this,WebViewActivityNotOpenDefaultWeb.class);
                    Bundle bundle = new Bundle();
                    intent.putExtra("title",getIntent().getStringExtra("title"));
                    intent.putExtra("url",getIntent().getStringExtra("url"));
                    startActivity(intent);
                }
                else if("opentbweb".equals(getIntent().getStringExtra("type"))){
                    Intent intent=new Intent(DialogActivity.this,WebViewActivity3.class);
                    Bundle bundle = new Bundle();
                    intent.putExtra("title",getIntent().getStringExtra("title"));
                    intent.putExtra("url",getIntent().getStringExtra("url"));
                    startActivity(intent);
                }

               finish();
            }
        });
        findViewById(R.id.txt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
