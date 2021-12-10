package com.android.jdhshop.my;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jiguang.analytics.android.api.CountEvent;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;

/**
 * 分享推荐
 * Created by yohn on 2018/7/14.
 */

public class MyShareUrlActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.rb_invite_1)
    RadioButton rb_invite_1;
    @BindView(R.id.rb_invite_2)
    RadioButton rb_invite_2;
    FragmentManager fragmentManager;
    ArrayList<Fragment> fragments = new ArrayList<>();
    Fragment currentFragment;
    Fragment one,two;


    private void  addTime(){
        JAnalyticsInterface.onEvent(this,new CountEvent("copy_invite"));
    }

    @Override
    protected void initUI() {
        setContentView(R.layout.ac_invite_pv);
        ButterKnife.bind(this);
        tvTitle.setText( "邀请好友" );
        tv_left.setVisibility( View.VISIBLE );

        fragmentManager = getSupportFragmentManager();
        one = new ShareImageFragment();
        two = new ShareLinkFragment();
        fragments.add(one);
        fragments.add(two);
        switchFragment(one).commit();
    }

    @Override
    protected void initData() {

    }

    public void refreshFragment(){
        super.recreate();
    }

    @Override
    protected void initListener() {
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rb_invite_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Log.d(TAG, "onCheckedChanged: " + isChecked);
                if (isChecked) switchFragment(one).commit();
            }
        });

        rb_invite_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Log.d(TAG, "onCheckedChanged2: " + isChecked);
                if (isChecked) switchFragment(two).commit();
            }
        });
    }

    private FragmentTransaction switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.invite_frame, targetFragment, targetFragment.getClass().getName());
        } else {
            transaction
                    .hide(currentFragment)
                    .show(targetFragment);
        }
        currentFragment = targetFragment;
        return transaction;
    }
}
