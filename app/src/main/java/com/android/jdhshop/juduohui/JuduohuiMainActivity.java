package com.android.jdhshop.juduohui;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.fragments.JuDuoHuiFragment;
import com.android.jdhshop.juduohui.fragment.NewsReadFragment;
import com.android.jdhshop.login.WelActivity;
import com.android.jdhshop.utils.UIUtils;
import com.android.jdhshop.widget.CaiNiaoRadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JuduohuiMainActivity extends BaseActivity {

    @BindView(R.id.bottom_menus)
    CaiNiaoRadioGroup bottom_menus;
    @BindView(R.id.rb_1) RadioButton bottom_menu_1;
    @BindView(R.id.rb_2) RadioButton bottom_menu_2;
    @BindView(R.id.rb_3) RadioButton bottom_menu_3;
    @BindView(R.id.rb_4) RadioButton bottom_menu_4;
    Drawable bottom_menu_icon_1,bottom_menu_icon_2,bottom_menu_icon_3,bottom_menu_icon_4;

    private Fragment f1, f2, f3, f4;
    FragmentTransaction ft;
    private Fragment currentFragment = new Fragment();
    private int normal_color = Color.parseColor("#666666");
    private int active_color = Color.parseColor("#EF411F");
    private CaiNiaoRadioGroup.OnCheckedChangeListener listener;
    int current_tab_id = 0;
    private String TAG = getClass().getSimpleName();

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_juduohui_main);
        ButterKnife.bind(this);

        bottom_menu_icon_1 = getResources().getDrawable(R.drawable.news_read);
        bottom_menu_icon_1.setBounds(0,UIUtils.dp2px(2), UIUtils.dp2px(28) ,UIUtils.dp2px(30));
        bottom_menu_icon_1.setColorFilter(active_color, PorterDuff.Mode.SRC_IN);
        bottom_menu_1.setCompoundDrawables(null,bottom_menu_icon_1,null,null);

        bottom_menu_icon_2 = getResources().getDrawable(R.drawable.news_signin);
        bottom_menu_icon_2.setBounds(0,UIUtils.dp2px(2), UIUtils.dp2px(28) ,UIUtils.dp2px(30));
        bottom_menu_icon_2.setColorFilter(normal_color, PorterDuff.Mode.SRC_IN);
        bottom_menu_2.setCompoundDrawables(null,bottom_menu_icon_2,null,null);

        bottom_menu_icon_3 = getResources().getDrawable(R.drawable.ic_fuli);
        bottom_menu_icon_3.setBounds(0,UIUtils.dp2px(2), UIUtils.dp2px(28) ,UIUtils.dp2px(30));
        bottom_menu_icon_3.setColorFilter(normal_color, PorterDuff.Mode.SRC_IN);
        bottom_menu_3.setCompoundDrawables(null,bottom_menu_icon_3,null,null);

        bottom_menu_icon_4 = getResources().getDrawable(R.drawable.news_myhome);
        bottom_menu_icon_4.setBounds(0,UIUtils.dp2px(2), UIUtils.dp2px(28) ,UIUtils.dp2px(30));
        bottom_menu_icon_4.setColorFilter(normal_color, PorterDuff.Mode.SRC_IN);
        bottom_menu_4.setCompoundDrawables(null,bottom_menu_icon_4,null,null);

        listener = new CaiNiaoRadioGroup.OnCheckedChangeListener() {
            final String TAG = "ChangeListener";
            @Override
            public void onCheckedChanged(CaiNiaoRadioGroup group, int checkedId) {
                // Log.d(TAG, "onCheckedChanged: " + checkedId);
                bottom_menu_icon_1.setColorFilter(normal_color, PorterDuff.Mode.SRC_IN);
                bottom_menu_icon_2.setColorFilter(normal_color, PorterDuff.Mode.SRC_IN);
                bottom_menu_icon_3.setColorFilter(normal_color, PorterDuff.Mode.SRC_IN);
                bottom_menu_icon_4.setColorFilter(normal_color, PorterDuff.Mode.SRC_IN);
                switch (checkedId){
                    case R.id.rb_1:
                        bottom_menu_icon_1.setColorFilter(active_color, PorterDuff.Mode.SRC_IN);
                        if (currentFragment != f1) switchFragment(f1).commit();
                        current_tab_id = R.id.rb_1;
                        break;
                    case R.id.rb_2:

                        if ("".equals(SPUtils.getStringData(getComeActivity(), "token", ""))) {
                            showToast( "请先登录");
                            bottom_menus.check(R.id.rb_1);
                            if (currentFragment != f1) switchFragment(f1).commit();
                            openActivity(WelActivity.class);
                            return;
                        }

                        bottom_menu_icon_2.setColorFilter(active_color, PorterDuff.Mode.SRC_IN);
                        if (currentFragment != f2){switchFragment(f2).commit();}
                        current_tab_id = R.id.rb_2;
                        break;
                    case R.id.rb_3:
                        if ("".equals(SPUtils.getStringData(getComeActivity(), "token", ""))) {
                            showToast( "请先登录");
                            bottom_menus.check(R.id.rb_1);
                            if (currentFragment != f1) switchFragment(f1).commit();
                            openActivity(WelActivity.class);
                            return;
                        }
                        if (currentFragment != f3) switchFragment(f3).commit();
                        bottom_menu_icon_3.setColorFilter(active_color, PorterDuff.Mode.SRC_IN);
                        current_tab_id = R.id.rb_3;

                        break;
                    case R.id.rb_4:
                        if ("".equals(SPUtils.getStringData(getComeActivity(), "token", ""))) {
                            showToast( "请先登录");
                            bottom_menus.check(R.id.rb_1);
                            if (currentFragment != f1) switchFragment(f1).commit();
                            openActivity(WelActivity.class);
                            return;
                        }
                        bottom_menu_icon_4.setColorFilter(active_color, PorterDuff.Mode.SRC_IN);
                        if (currentFragment != f4) switchFragment(f4).commit();
                        current_tab_id = R.id.rb_4;
                        break;
                }

            }
        };

    }

    @Override
    protected void initData() {
        f1 = new NewsReadFragment();
        f2 = new JuDuoHuiFragment();
        f3 = new JuDuoHuiFragment();
        f4 = new JuDuoHuiFragment();

        Bundle f1_bundle = new Bundle();
        f1_bundle.putString("load_file","index.html");
        f1.setArguments(f1_bundle);

        Bundle f3_bundle = new Bundle();
        f3_bundle.putString("load_file","neck_salary.html");
        f3.setArguments(f3_bundle);

        Bundle f4_bundle = new Bundle();
        f4_bundle.putString("load_file","my_data.html");
        f4.setArguments(f4_bundle);

        current_tab_id = bottom_menu_1.getId();
        switchFragment(f1).commit();

        Intent income_data = getIntent();
        if (income_data!=null && income_data.getStringExtra("jump")!=null){
            jumpFragment(income_data.getStringExtra("path"),income_data.getStringExtra("notice"));
        }

    }

    @Override
    protected void initListener() {
        bottom_menus.setOnCheckedChangeListener(listener);
    }
    @OnClick({R.id.rb_1,R.id.rb_2,R.id.rb_3,R.id.rb_4})
    public void onClick(View v){
        // Log.d(TAG, "onClick: ");
        Bundle b = new Bundle();
        b.putInt("click_check",1);
        switch (v.getId()){
            case R.id.rb_1:
                break;
            case R.id.rb_2:
                if (current_tab_id == R.id.rb_2) {
                    b.putInt("reload", 1);
                    currentFragment.setArguments(b);
                }
                break;
            case R.id.rb_3:
                if (current_tab_id == R.id.rb_3) {
                    b.putInt("reload", 1);
                    currentFragment.setArguments(b);
                }
                break;
            case R.id.rb_4:
                if (current_tab_id == R.id.rb_4) {
                    b.putInt("reload", 1);
                    currentFragment.setArguments(b);
                }
                break;
        }
    }

    private FragmentTransaction switchFragment(Fragment targetFragment) {
        getSupportFragmentManager().executePendingTransactions();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            Bundle b = targetFragment.getArguments();
            if (b==null) b = new Bundle();
            String bb = b.getString("load_file","nono");
            // Log.d(TAG, "switchFragment: " + bb);
            transaction.add(R.id.frame, targetFragment, bb);
        } else {
            transaction.hide(currentFragment).show(targetFragment);
        }
        currentFragment = targetFragment;
        return transaction;
    }

    private void jumpFragment(String activity_path,String notice_toast){
        if (!"".equals(notice_toast)) showToast(notice_toast);
        // Log.d(TAG, "juduohuimainactivity jumpFragment: " + activity_path + " , " + notice_toast);
        if (!activity_path.equals("")) {
            //特殊处理 fragments
            if (activity_path.contains(",")) {
                bottom_menus.check(bottom_menus.getCheckedRadioButtonId());
                String[] tmp_arr = activity_path.split(",");
                Fragment f;
                RadioButton rb;
                int checked_id = 0;
                switch (tmp_arr[1]) {
                    case "f2":
                        f = f2;
                        rb = bottom_menu_2;
                        break;
                    case "f3":
                        f = f3;
                        rb = bottom_menu_3;
                        break;
                    case "f4":
                        f = f4;
                        rb = bottom_menu_4;
                        break;
                    case "f1":
                    default:
                        f = f1;
                        rb = bottom_menu_1;
                        break;
                }
                bottom_menus.check(rb.getId());
                listener.onCheckedChanged(bottom_menus,rb.getId());

            } else {
                try {
                    Intent tmp_intent = new Intent();
                    tmp_intent.setComponent(new ComponentName(getPackageName(), activity_path));
                    startActivity(tmp_intent);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Log.d(TAG, "onNewIntent: " + intent);

    }
}