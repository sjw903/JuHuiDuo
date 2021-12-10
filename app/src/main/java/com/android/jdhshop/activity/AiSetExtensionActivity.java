package com.android.jdhshop.activity;

import android.content.Intent;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AiSetExtensionActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.text_set_tv)
    TextView text_set_tv;
    @BindView(R.id.tv_nice_xiaochengxu)
    NiceSpinner tv_nice_xiaochengxu;
    @BindView(R.id.tv_nice_shangpin)
    NiceSpinner tv_nice_shangpin;
    public List<String> extension_xiao_list;
    public List<String> extension_shang_list;

    private JSONObject item_info_json ;

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_ai_set_extension);
        ButterKnife.bind(this);
        tv_left.setVisibility(View.VISIBLE);
        tv_title.setText("推广方式");
        extension_xiao_list = new LinkedList<>(Arrays.asList("不推送", "我拿返利", "我拿推广"));
        tv_nice_xiaochengxu.attachDataSource(extension_xiao_list);
        extension_shang_list = new LinkedList<>(Arrays.asList("不推送", "我拿返利"));
        tv_nice_shangpin.attachDataSource(extension_shang_list);
        text_set_tv.setText("\t\t\t" + "1.小程序详情推广是分享文字及产品图片，群成员可以扫描图片二维码进行购买商品。" + "\n\n" + "\t\t\t" + "2.商品链接推广，只会分享文字及商品链接，群成员点击链接可以直接购买商品。" + "\n\n" + "\t\t\t" + "3.小程序详情推广有三个选项【不推送】【我拿返利】【我拿推广】商品链接推广有两个选项【不推送】【我拿返利】，小程序详情推广和商品链接推广不能同时选择【不推送】。" + "\n\n" + "\t\t\t" + "4.选择【我拿返利】可以获得购买者购买商品的全部返利，购买者没有返利。选择【我拿推广】可以获得购买者购买商品的部分推广佣金，购买者获得全部返利。");
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();

        // Log.d(TAG, "initData: " + intent.getStringExtra("item_info"));
        String item_info = intent.getStringExtra("item_info");

        if ("".equals(item_info) || item_info == null){
            showTipDialog2("错误提示", Html.fromHtml("页面信息错误，请从正确渠道进入。"), new BaseActivity.onClickListener(){
                @Override
                public void onClickSure() {
                    finish();
                }
            },"我知道了");
            return;
        }

        item_info_json = JSONObject.parseObject(item_info);
        tv_nice_xiaochengxu.setSelectedIndex(item_info_json.getIntValue("desc_type"));
        tv_nice_shangpin.setSelectedIndex(item_info_json.getIntValue("href_type"));

        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_nice_xiaochengxu.getSelectedIndex() == 0 && tv_nice_shangpin.getSelectedIndex() == 0) {
//                    showToast("小程序详情推广和商品链接推广不能同时选择不推送，请重新选择");
                    showTipDialog("错误提醒",Html.fromHtml("小程序详情推广和商品链接推广不能同时选择不推送，请重新选择"));
                } else {

                    if (item_info_json.getIntValue("desc_type") != tv_nice_xiaochengxu.getSelectedIndex() || item_info_json.getIntValue("href_type") != tv_nice_shangpin.getSelectedIndex()){
                        Intent  i = new Intent();
                        i.putExtra("need_update",1);
                        i.putExtra("desc_type",tv_nice_xiaochengxu.getSelectedIndex()+"");
                        i.putExtra("href_type",tv_nice_shangpin.getSelectedIndex()+"");
                        setResult(4,i);
                    }
                    finish();
                }
            }
        });

        tv_nice_shangpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 && tv_nice_xiaochengxu.getSelectedIndex() == 0){
                    showTipDialog("错误提醒",Html.fromHtml("小程序详情推广和商品链接推广不能同时选择不推送，请重新选择"));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tv_nice_xiaochengxu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 && tv_nice_shangpin.getSelectedIndex() == 0){
                    showTipDialog("错误提醒",Html.fromHtml("小程序详情推广和商品链接推广不能同时选择不推送，请重新选择"));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void initListener() {

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            tv_left.callOnClick();
        }

        return false;

    }
}
