package com.android.jdhshop.my;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.MTAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.MTBean;
import com.android.jdhshop.bean.MessageEvent;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.login.WelActivity;
import com.android.jdhshop.widget.AutoClearEditText;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

import org.angmarch.views.NiceSpinner;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 我的订单
 * Created by yohn on 2018/7/27.
 */

public class MyOrderActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_title)
    NiceSpinner tv_title;
    @BindView(R.id.tv_title_yijiesuan)
    NiceSpinner tv_title_yijies;
    @BindView( R.id.rg_1 )
    RadioGroup rg_1;
    @BindView( R.id.rg_type_type1)
    RadioGroup rg_type_type1;
    @BindView( R.id.et_title_1 )
    AutoClearEditText etTitle_1;
    @BindView( R.id.tv_search_1 )
    TextView tv_search_1;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.rb_one)
    RadioButton rb_one;
    @BindView(R.id.rb_two)
    RadioButton rb_two;
    @BindView(R.id.rb_three)
    RadioButton rb_three;
    @BindView(R.id.rb_four)
    RadioButton rb_four;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.rg_type)
    RadioGroup rg_type;
    @BindView(R.id.et_title)
    AutoClearEditText etTitle;
    @BindView(R.id.tv_search)
    TextView tvRight;
    private FragmentManager fm;
    private ArrayList<Fragment> fragments;
    private AllOrderFragment one;//所有
    private ValidOrderFragment two;//已付款
    private InvalidOrderFragment three;//已失效
    private ReturnedOrderFragment four;//已结算
    private ACache mAcache;
    private List<MTBean> mtBeanList = new ArrayList<>();
    String token;
    private AlibcLogin alibcLogin;
    public static int CURRENT_TYPE = 0;//订单来源
    public static int CURRENT_ORDER_TYPE = 1;//1 表示我的订单 2表示直邀订单 3 表示间邀订单
    public static  int settlement=0;//已结算未结算状态
    public static Activity orderActivity;
    public static String order_sn="";
    public static String order_num;
    int type=1;//1 表示默认 2 表示直邀订单 3表示 间邀订单
    public  List<String> yijiesList ;

    String order_status="";
    String type_jiesuan="";
    MTAdapter mtAdapter;

    @Override
    protected void initUI() {
        setContentView(R.layout.ac_order);
        ButterKnife.bind(this);

        orderActivity = this;
        RecyclerView recyclerView = findViewById( R.id.recycler_view );
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        recyclerView.setLayoutManager( linearLayoutManager );
        mtAdapter = new MTAdapter( mtBeanList );
        recyclerView.setAdapter(mtAdapter);
        List<String> dataset = new LinkedList<>(Arrays.asList("淘宝订单", "拼多多订单", "京东订单","唯品会订单","美团订单"));
        tv_title.attachDataSource(dataset);

        if(getIntent().getStringExtra("se")!=null){
            etTitle.setText(getIntent().getStringExtra("se"));
            order_sn=etTitle.getText().toString();
        }
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(TextUtils.isEmpty(etTitle.getText().toString())){
//                    order_sn="";
//                    return;
//                }
                order_sn=etTitle.getText().toString().trim();
                EventBus.getDefault().post(new MessageEvent("search"));
            }
        });
        if (getIntent().getStringExtra( "se" ) != null) {
            etTitle_1.setText( getIntent().getStringExtra( "se" ) );
            order_num = etTitle_1.getText().toString();
        }
        tv_search_1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order_num = etTitle_1.getText().toString().trim();
                EventBus.getDefault().post(new MessageEvent("search"));
            }
        } );
        rg_1.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.rb_one_one) {
                    order_status = "1";
                }else if (checkedId == R.id.rb_two_two){
                    order_status = "2";
                }else {
                    order_status="";
                }
                getOrderList("");
            }
        } );
        rg_type_type1.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(rg_type_type1.getCheckedRadioButtonId()==R.id.rb_my_my1){
                    type = 1;//本身默认
                }else if(rg_type_type1.getCheckedRadioButtonId()==R.id.rb_direct_direct1){
                    type = 2;//直接
                }else{
                    type = 3;//间接
                }
                getOrderList("");
            }
        } );
    }

    /**
     * wmm
     * 2018-12-11
     * 淘宝1代表下一级 淘宝2表示再一级订单
     * 京东。。
     * 拼多多。。
     */
    @Override
    protected void onResume() {
        super.onResume();
        if ("taobao".equals(SPUtils.getStringData(this, "inform_message", "-1"))) {
            CURRENT_TYPE = 0;
            CURRENT_ORDER_TYPE = 1;
        } else if ("taobao1".equals(SPUtils.getStringData(this, "inform_message", "-1"))) {
            CURRENT_TYPE = 0;
            CURRENT_ORDER_TYPE = 2;
        } else if ("taobao2".equals(SPUtils.getStringData(this, "inform_message", "-1"))) {
            CURRENT_TYPE = 0;
            CURRENT_ORDER_TYPE = 3;
        } else if ("pdd".equals(SPUtils.getStringData(this, "inform_message", "-1"))) {
            CURRENT_TYPE = 1;
            CURRENT_ORDER_TYPE = 1;
        } else if ("pdd1".equals(SPUtils.getStringData(this, "inform_message", "-1"))) {
            CURRENT_TYPE = 1;
            CURRENT_ORDER_TYPE = 2;
        } else if ("pdd2".equals(SPUtils.getStringData(this, "inform_message", "-1"))) {
            CURRENT_TYPE = 1;
            CURRENT_ORDER_TYPE = 3;
        } else if ("jingdong".equals(SPUtils.getStringData(this, "inform_message", "-1"))) {
            CURRENT_TYPE = 2;
            CURRENT_ORDER_TYPE = 1;
        } else if ("jingdong1".equals(SPUtils.getStringData(this, "inform_message", "-1"))) {
            CURRENT_TYPE = 2;
            CURRENT_ORDER_TYPE = 2;
        } else if ("jingdong2".equals(SPUtils.getStringData(this, "inform_message", "-1"))) {
            CURRENT_TYPE = 2;
            CURRENT_ORDER_TYPE = 3;
        }
        tv_title.setSelectedIndex(CURRENT_TYPE);
        rg_type.check(CURRENT_ORDER_TYPE == 1 ? R.id.rb_my : CURRENT_ORDER_TYPE == 2 ? R.id.rb_direct : R.id.rb_next);
        SPUtils.saveStringData(this, "inform_message", "-1");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CURRENT_TYPE = 0;
        CURRENT_ORDER_TYPE = 1;
        order_sn="";
    }

    @Override
    protected void initData() {
        mAcache = ACache.get(this);
        token = mAcache.getAsString(Constants.TOKEN);

        if ("".equals(SPUtils.getStringData(this,"token",""))){
            openActivityForResult(WelActivity.class,null,8341);
            this.setOnActivityResultLisntener(new onActivityResultLisntener() {
                @Override
                public void onActivityResult(int requestCode, int resultCode, Intent data) {
                    if (requestCode == 8341 && resultCode == RESULT_OK){
                        runOnUiThread(()->{
                            initData();
                        });
                    }
                    else{
                        finish();
                    }
                }
            });
            return;
        }

        alibcLogin = AlibcLogin.getInstance();
        tv_left.setVisibility(View.VISIBLE);
//        tv_title.setText("我的订单");
        fm = getSupportFragmentManager();
        fragments = new ArrayList<>();
        one = new AllOrderFragment();
        two = new ValidOrderFragment();
        three = new InvalidOrderFragment();
        four = new ReturnedOrderFragment();
        fragments.add(one);
        fragments.add(two);
        fragments.add(four);
        fragments.add(three);
        viewpager.setAdapter(new MainPageAdpter(fm));
        viewpager.setOnPageChangeListener(this);
        viewpager.setCurrentItem(0);
        settlement=0;
        yijiesList= new LinkedList<>(Arrays.asList("未结算", "已结算"));
        tv_title_yijies.attachDataSource(yijiesList);
        tv_title_yijies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    settlement=1;
                    EventBus.getDefault().post(new MessageEvent("refresh2"));

                    //EventBus.getDefault().post(new MessageEvent("refresh"));
                    //Toast.makeText(MyOrderActivity.this, "1", Toast.LENGTH_SHORT).show();
                }else {
                    settlement=0;
                    EventBus.getDefault().post(new MessageEvent("refresh2"));

                    //EventBus.getDefault().post(new MessageEvent("refresh2"));
                    //Toast.makeText(MyOrderActivity.this, "2", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tv_title.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rb_two.setText("已付款");
                rb_three.setText("已失效");
                rb_four.setText("已结算");
                if (position == 0) {
                    CURRENT_TYPE = 0;//淘宝
                    findViewById( R.id.ll_top ).setVisibility( View.VISIBLE  );
                    findViewById( R.id.ll_meituan ).setVisibility( View.GONE );
                    settlement=0;
                    yijiesList= new LinkedList<>(Arrays.asList("未结算", "已结算"));
                    tv_title_yijies.attachDataSource(yijiesList);
                    EventBus.getDefault().post(new MessageEvent("refresh2"));
                } else if (position == 1) {
                    findViewById( R.id.ll_top ).setVisibility( View.VISIBLE  );
                    findViewById( R.id.ll_meituan ).setVisibility( View.GONE );
                    yijiesList= new LinkedList<>(Arrays.asList("未结算", "已结算"));
                    tv_title_yijies.attachDataSource(yijiesList);
                    EventBus.getDefault().post(new MessageEvent("refresh2"));
                    settlement=0;
                    CURRENT_TYPE = 1;//拼多多
                    EventBus.getDefault().post(new MessageEvent("refresh2"));
                } else if (position == 2) {
                    CURRENT_TYPE = 2;//京东
                    findViewById( R.id.ll_top ).setVisibility( View.VISIBLE  );
                    findViewById( R.id.ll_meituan ).setVisibility( View.GONE );
                    yijiesList= new LinkedList<>(Arrays.asList("未结算", "已结算"));
                    tv_title_yijies.attachDataSource(yijiesList);
                    settlement=0;
                    EventBus.getDefault().post(new MessageEvent("refresh2"));
                }else if (position == 3){
                    CURRENT_TYPE=3;//唯品会
                    findViewById( R.id.ll_top ).setVisibility( View.VISIBLE  );
                    yijiesList= new LinkedList<>(Arrays.asList("未结算", "已结算"));
                    tv_title_yijies.attachDataSource(yijiesList);
                    findViewById( R.id.ll_meituan ).setVisibility( View.GONE );
                    settlement=0;
                    EventBus.getDefault().post(new MessageEvent("refresh2"));
                    rb_two.setText("待定");
                    rb_three.setText("已完结");
                    rb_four.setText("不合格");
                }else if (position == 4){
                    CURRENT_TYPE = 4;//美团
                    findViewById( R.id.ll_top ).setVisibility(View.GONE);
                    yijiesList= new LinkedList<>(Arrays.asList("未结算", "已结算"));
                    tv_title_yijies.attachDataSource(yijiesList);
                    findViewById( R.id.ll_meituan ).setVisibility( View.VISIBLE );
                    settlement=0;
                    getOrderList("");
                }
                tv_title_yijies.dismissDropDown();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected void initListener() {
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_one) {
                    viewpager.setCurrentItem(0);
                } else if (checkedId == R.id.rb_two) {
                    viewpager.setCurrentItem(1);
                } else if (checkedId == R.id.rb_three) {
                    viewpager.setCurrentItem(3);
                } else if (checkedId == R.id.rb_four) {
                    viewpager.setCurrentItem(2);
                }
                EventBus.getDefault().post(new MessageEvent("refresh2"));
            }
        });
        rg_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_my) {
                    if (CURRENT_ORDER_TYPE == 1)
                        return;
                    CURRENT_ORDER_TYPE = 1;
                } else if (checkedId == R.id.rb_direct) {
                    if (CURRENT_ORDER_TYPE == 2)
                        return;
                    CURRENT_ORDER_TYPE = 2;
                } else if (checkedId == R.id.rb_next) {
                    if (CURRENT_ORDER_TYPE == 3)
                        return;
                    CURRENT_ORDER_TYPE = 3;
                }
                EventBus.getDefault().post(new MessageEvent("refresh2"));
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        rb_one.setChecked(false);
        rb_two.setChecked(false);
        rb_three.setChecked(false);
        rb_four.setChecked(false);
        switch (position) {
            case 0:
                rb_two.setChecked(false);
                rb_three.setChecked(false);
                rb_one.setChecked(true);
                rb_four.setChecked(false);
                viewpager.setCurrentItem(0);
                break;
            case 1:
                rb_one.setChecked(false);
                rb_three.setChecked(false);
                rb_two.setChecked(true);
                rb_four.setChecked(false);
                viewpager.setCurrentItem(1);
                break;
            case 2:
                rb_one.setChecked(false);
                rb_two.setChecked(false);
                rb_three.setChecked(false);
                rb_four.setChecked(true);
                viewpager.setCurrentItem(2);
                break;
            case 3:
                rb_one.setChecked(false);
                rb_two.setChecked(false);
                rb_four.setChecked(false);
                rb_three.setChecked(true);
                viewpager.setCurrentItem(3);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
    class MainPageAdpter extends FragmentStatePagerAdapter {

        public MainPageAdpter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int arg0) {
            return fragments.get(arg0);
        }

        @Override
        public int getCount() {
            return fragments == null ? 0 : fragments.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            /**
             * 这里千万不能加下面这句话，如果加了  界面来回切换时 会出现空白
             */
            //	container.removeView(fragments.get(position).getView()); // 移出viewpager两边之外的page布局
        }
    }
    private void getOrderList(String settlement) {
        RequestParams requestParams = new RequestParams();
        requestParams.put( "type", type);//订单类型
        requestParams.put( "order_status",order_status);//订单状态
        requestParams.put( "per","6");
        requestParams.put( "p","1" );
        requestParams.put("settlement", settlement);
        //showToast(settlement+"");
        HttpUtils.post(Constants.setMeituans, MyOrderActivity.this,requestParams, new onOKJsonHttpResponseHandler<MTBean>( new TypeToken<Response<MTBean>>() {
        })  {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogUtils.d("dfasdf",responseString);
            }
            @Override
            public void onSuccess(int statusCode, Response<MTBean> datas) {
                if (datas.isSuccess()) {
                    MTBean datasData = datas.getData();
                   LogUtils.d("dfasdf",datasData.list.size()+"");
                    if (datasData != null) {
                        mtBeanList.clear();
                        mtBeanList.addAll(datasData.list);
                        mtAdapter.notifyDataSetChanged();
                    }
                } else {
//                    showToast( datas.getMsg());
                }
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
        });
    }
}
