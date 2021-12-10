package com.android.jdhshop.merchantactivity;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.merfragment.OrderallFragment;
import com.android.jdhshop.merfragment.OrderdfhFragment;
import com.android.jdhshop.merfragment.OrderdfkFragment;
import com.android.jdhshop.merfragment.OrderdpjFragment;
import com.android.jdhshop.merfragment.OrderdshFragment;

import java.util.ArrayList;
import java.util.List;

/**
             * 订单列表
             * */
public class OrderListActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rl_dfk,rl_dfh,rl_dsh,rl_dpj,rl_all;
    private TextView tv_dfk,tv_dfh,tv_dsh,tv_dpj,tv_all;
    private View v_dfk,v_dfh,v_dsh,v_dpj,v_all;
    private LinearLayout ly_back;
    private ViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<>(  );
    private OrderallFragment orderallFragment;
    private OrderdpjFragment orderdpjFragment;
    private OrderdshFragment orderdshFragment;
    private OrderdfhFragment orderdfhFragment;
    private OrderdfkFragment orderdfkFragment;
    private FragmentAdapter mFragmentAdapter;
    private ImageView fanhui;

    @Override
    protected void initUI() {
        setContentView( R.layout.activity_order_list );
    }

    @Override
    protected void initData() {
        rl_dfk = findViewById( R.id.orderlist_rldfk );
        rl_dfh = findViewById( R.id.orderlist_rldfh );
        rl_dsh = findViewById( R.id.orderlist_rldsh );
        rl_dpj = findViewById( R.id.orderlist_rldpj );
        rl_all = findViewById( R.id.orderlist_rlall );
        tv_dfk = findViewById( R.id.orderlist_tvdfk );
        tv_dfh = findViewById( R.id.orderlist_tvdfh );
        tv_dsh = findViewById( R.id.orderlist_tvdsh );
        tv_dpj = findViewById( R.id.orderlist_tvdpj );
        tv_all = findViewById( R.id.orderlist_tvall );
        v_dfk = findViewById( R.id.orderlist_vdfk );
        v_dfh = findViewById( R.id.orderlist_vdfh );
        v_dsh = findViewById( R.id.orderlist_vdsh );
        v_dpj = findViewById( R.id.orderlist_vdpj );
        v_all = findViewById( R.id.orderlist_vall );
        fanhui = findViewById( R.id.fanhuifan );
        BaseLogDZiYuan.LogDingZiYuan(fanhui, "icon_back.png");
        ly_back = findViewById( R.id.orderlist_lyback );
        viewPager = findViewById( R.id.orderlist_viewpager );
        ly_back.setOnClickListener( this );
        rl_dfk.setOnClickListener( this );
        rl_dfh.setOnClickListener( this );
        rl_dsh.setOnClickListener( this );
        rl_dpj.setOnClickListener( this );
        rl_all.setOnClickListener( this );

        orderallFragment = new OrderallFragment();
        orderdfkFragment = new OrderdfkFragment();
        orderdfhFragment = new OrderdfhFragment();
        orderdshFragment = new OrderdshFragment();
        orderdpjFragment = new OrderdpjFragment();


        fragmentList.add( orderdfkFragment );
        fragmentList.add( orderdfhFragment );
        fragmentList.add( orderdshFragment );
        fragmentList.add( orderdpjFragment );
        fragmentList.add( orderallFragment );

        mFragmentAdapter = new FragmentAdapter( getSupportFragmentManager(),fragmentList );
        viewPager.setAdapter( mFragmentAdapter );
        viewPager.setOffscreenPageLimit( 1 );
//        viewPager.setCurrentItem( 2 );
        LogUtils.d(TAG, "initData: "+getIntent().getStringExtra( "type" ));;
        if (getIntent().getStringExtra( "type" ).equals( "1" )){
            viewPager.setCurrentItem(0,true);
            changeTextColor(0);
        }else if (getIntent().getStringExtra( "type" ).equals( "2" )){
            viewPager.setCurrentItem(1,true);
            changeTextColor(1);
        }else if (getIntent().getStringExtra( "type" ).equals( "3" )){
            viewPager.setCurrentItem(2,true);
            changeTextColor(2);
        }
        else if (getIntent().getStringExtra( "type" ).equals( "4" )){
            viewPager.setCurrentItem(3,true);
            changeTextColor(3);
        }
//        else if (getIntent().getStringExtra( "type" ).equals( "2" )){
//            viewPager.setCurrentItem(3,true);
//            changeTextColor(3);
//        }else if (getIntent().getStringExtra( "type" ).equals( "3" )){
//            viewPager.setCurrentItem(4,true);
//            changeTextColor(4);
//        }else if (getIntent().getStringExtra( "type" ).equals( "4" )){
//            viewPager.setCurrentItem(0,true);
//            changeTextColor(0);
//        }


        viewPager.setOnPageChangeListener( new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTextColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        } );
    }

    @Override
    protected void initListener() {

    }
    @Override
    public void onClick(View view) {
        LogUtils.d(TAG, "点击的VIEW: "+view.getId());
        LogUtils.d(TAG, "已退款的ID: "+R.id.orderlist_rldpj);
        switch (view.getId()){
            case R.id.orderlist_rldfk://待付款
                viewPager.setCurrentItem(0,true);
                break;
            case R.id.orderlist_rldfh://待使用
                viewPager.setCurrentItem(1,true);
                break;
            case R.id.orderlist_rldsh://已使用
                viewPager.setCurrentItem(2,true);
                break;
            case R.id.orderlist_rldpj://已退款
                viewPager.setCurrentItem(3,true);
                break;
            case R.id.orderlist_lyback:
                finish();
                break;

        }
    }
    private void changeTextColor(int position) {
        if (position == 0){
            v_dfk.setVisibility( View.VISIBLE );
            v_dfh.setVisibility( View.GONE );
            v_dsh.setVisibility( View.GONE );
            v_dpj.setVisibility( View.GONE );
            v_all.setVisibility( View.GONE );
            tv_dfk.setTextColor( Color.parseColor("#F03E00"));
            tv_dfh.setTextColor(Color.parseColor("#000000"));
            tv_dsh.setTextColor(Color.parseColor("#000000"));
            tv_dpj.setTextColor(Color.parseColor("#000000"));
            tv_all.setTextColor( Color.parseColor( "#000000" ) );
        }else if(position == 1){
            v_dfk.setVisibility( View.GONE );
            v_dfh.setVisibility( View.VISIBLE );
            v_dsh.setVisibility( View.GONE );
            v_dpj.setVisibility( View.GONE );
            v_all.setVisibility( View.GONE );
            tv_dfk.setTextColor(Color.parseColor("#000000"));
            tv_dfh.setTextColor(Color.parseColor("#F03E00"));
            tv_dsh.setTextColor(Color.parseColor("#000000"));
            tv_dpj.setTextColor(Color.parseColor("#000000"));
            tv_all.setTextColor( Color.parseColor( "#000000" ) );
        }else if (position == 2){
            v_dfk.setVisibility( View.GONE );
            v_dfh.setVisibility( View.GONE );
            v_dsh.setVisibility( View.VISIBLE );
            v_dpj.setVisibility( View.GONE );
            v_all.setVisibility( View.GONE );
            tv_dfk.setTextColor(Color.parseColor("#000000"));
            tv_dfh.setTextColor(Color.parseColor("#000000"));
            tv_dsh.setTextColor(Color.parseColor("#F03E00"));
            tv_dpj.setTextColor(Color.parseColor("#000000"));
            tv_all.setTextColor( Color.parseColor( "#000000" ) );
        }else if(position == 3){
            v_dfk.setVisibility( View.GONE );
            v_dfh.setVisibility( View.GONE );
            v_dsh.setVisibility( View.GONE );
            v_dpj.setVisibility( View.VISIBLE );
            v_all.setVisibility( View.GONE );
            tv_dfk.setTextColor(Color.parseColor("#000000"));
            tv_dfh.setTextColor(Color.parseColor("#000000"));
            tv_dsh.setTextColor(Color.parseColor("#000000"));
            tv_dpj.setTextColor(Color.parseColor("#F03E00"));
            tv_all.setTextColor( Color.parseColor( "#000000" ) );
        }
//        else if (position == 0){
//            v_dfk.setVisibility( View.GONE );
//            v_dfh.setVisibility( View.GONE );
//            v_dsh.setVisibility( View.GONE );
//            v_dpj.setVisibility( View.GONE );
//            v_all.setVisibility( View.VISIBLE );
//            tv_dfk.setTextColor(Color.parseColor("#000000"));
//            tv_dfh.setTextColor(Color.parseColor("#000000"));
//            tv_dsh.setTextColor(Color.parseColor("#000000"));
//            tv_dpj.setTextColor(Color.parseColor("#000000"));
//            tv_all.setTextColor( Color.parseColor( "#F03E00" ) );
//        }
    }
    public class FragmentAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<Fragment>();

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }



        /*
         *由ViewPager的滑动修改底部导航Text的颜色
         */

    }
}
