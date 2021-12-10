package com.android.jdhshop.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.widget.CircleImageView;
import com.android.jdhshop.ziyuan.ResourceManager;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class CommissionPhbActivity extends BaseActivity {
    @BindView(R.id.txt_name_one)
    TextView txtNameOne;
    @BindView(R.id.txt_money_one)
    TextView txtMoneyOne;
    @BindView(R.id.txt_name_two)
    TextView txtNameTwo;
    @BindView(R.id.txt_money_two)
    TextView txtMoneyTwo;
    @BindView(R.id.txt_name_three)
    TextView txtNameThree;
    @BindView(R.id.txt_money_three)
    TextView txtMoneyThree;
    @BindView(R.id.txt_money)
    TextView txtMoney;
    @BindView(R.id.img_head)
    CircleImageView imgHead;
    @BindView(R.id.txt_ph)
    TextView txtPh;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.img_head_one)
    CircleImageView imgHeadOne;
    @BindView(R.id.img_head_two)
    CircleImageView imgHeadTwo;
    @BindView(R.id.img_head_three)
    CircleImageView imgHeadThree;
    private List<Item> list = new ArrayList<>();
    private Adapter adapter;
    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.img_one)
    ImageView img_one;
    @BindView(R.id.yinkuang)
    ImageView yinkuang;
    @BindView(R.id.jinkuang)
    ImageView jinkuang;
    @BindView(R.id.huangt)
    ImageView huangt;
    @BindView(R.id.img_two)
    ImageView img_two;
    @BindView(R.id.img_three)
    ImageView img_three;
    @Override
    protected void initUI() {
        if (Build.VERSION.SDK_INT >= 21) {
            setStatusBar(Color.parseColor("#FA412C"));
        }
        setContentView(R.layout.activity_commission_phb);
        ButterKnife.bind(this);
    }


    @Override
    protected void initData() {

        LogDingZiYuan();//加载本地资源
        adapter = new Adapter(R.layout.item_rank, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        RequestParams params = new RequestParams();
        HttpUtils.post(Constants.ranking, CommissionPhbActivity.this,params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // 输出错误信息
            }

            @Override
            public void onFinish() {
                // 隐藏进度条
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("dsfasd", responseString);
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    if (jsonObject.getInt("code") == 0) {
                        jsonObject = jsonObject.getJSONObject("data");
                        Glide.with(CommissionPhbActivity.this).load(jsonObject.getJSONObject("uMsg").getString("avatar")).into(imgHead);
                        txtPh.setText("第" + jsonObject.getJSONObject("uMsg").getString("rank") + "位");
                        txtMoney.setText(jsonObject.getJSONObject("uMsg").getString("balance").equals("null") ? "0.00" : jsonObject.getJSONObject("uMsg").getString("balance"));
                        JSONArray array = jsonObject.getJSONArray("list");
                        Item item;
                        for (int i = 0; i < array.length(); i++) {
                            if (i == 0) {
                                txtMoneyTwo.setText(array.getJSONObject(i).getString("all_money"));
                                txtNameTwo.setText(array.getJSONObject(i).getString("nickname"));
                                Glide.with(CommissionPhbActivity.this).load(array.getJSONObject(i).getString("avatar")).placeholder(R.mipmap.app_icon).dontAnimate().into(imgHeadTwo);
                                continue;
                            }
                            if (i == 1) {
                                txtMoneyOne.setText(array.getJSONObject(i).getString("all_money"));
                                txtNameOne.setText(array.getJSONObject(i).getString("nickname"));
                                Glide.with(CommissionPhbActivity.this).load(array.getJSONObject(i).getString("avatar")).placeholder(R.mipmap.app_icon).dontAnimate().into(imgHeadOne);
                                continue;
                            }
                            if (i == 2) {
                                txtMoneyThree.setText(array.getJSONObject(i).getString("all_money"));
                                txtNameThree.setText(array.getJSONObject(i).getString("nickname"));
                                Glide.with(CommissionPhbActivity.this).load(array.getJSONObject(i).getString("avatar")).placeholder(R.mipmap.app_icon).dontAnimate().into(imgHeadThree);
                                continue;
                            }
                            item = new Item();
                            item.num = array.getJSONObject(i).getString("num");
                            item.all_money = array.getJSONObject(i).getString("all_money");
                            item.user_id = array.getJSONObject(i).getString("user_id");
                            item.avatar = array.getJSONObject(i).getString("avatar");
                            item.nickname = array.getJSONObject(i).getString("nickname");
                            list.add(item);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        showToast(jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
            }
        });
    }

    //加载本地资源
    private void LogDingZiYuan(){
        BaseLogDZiYuan.LogDingZiYuan(img_back,"icon_back_while.png");
        //BaseLogDZiYuan.LogDingZiYuan(img_one,"/sdcard/juduohuiziyuanhot_update_file/drawable/icon_temp.png");
        //BaseLogDZiYuan.LogDingZiYuan(yinkuang,"/sdcard/juduohuiziyuanhot_update_file/drawable/ranking_sec.webp");
        BaseLogDZiYuan.LogDingZiYuan(imgHeadOne,"app_icon.png");
        //BaseLogDZiYuan.LogDingZiYuan(jinkuang,"/sdcard/juduohuiziyuanhot_update_file/drawable/ranking_fir.webp");
        BaseLogDZiYuan.LogDingZiYuan(imgHeadTwo,"app_icon.png");
        //BaseLogDZiYuan.LogDingZiYuan(huangt,"/sdcard/juduohuiziyuanhot_update_file/drawable/ranking_thr.webp");
        BaseLogDZiYuan.LogDingZiYuan(imgHeadThree,"app_icon.png");
        BaseLogDZiYuan.LogDingZiYuan(imgHead,"app_icon.png");
        //BaseLogDZiYuan.LogDingZiYuan(img_two,"/sdcard/juduohuiziyuanhot_update_file/drawable/icon_temp2.png");
        //BaseLogDZiYuan.LogDingZiYuan(img_three,"/sdcard/juduohuiziyuanhot_update_file/drawable/bg_ranking_min.png");
    }

    @Override
    protected void initListener() {

    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }

    class Item {
        public String num;//	排名
        public String user_id;//用户ID
        public String all_money;//	收益
        public String avatar;//	用户头像
        public String nickname;//	用户昵称

    }

    class Adapter extends BaseQuickAdapter<Item, BaseViewHolder> {

        public Adapter(int layoutResId, @Nullable List<Item> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Item item) {
            LinearLayout linearLayout = helper.getView(R.id.ll_root);
            helper.setText(R.id.txt_rank, item.num);
            helper.setText(R.id.txt_name, item.nickname);
            helper.setText(R.id.txt_score, item.all_money);
            Glide.with(mContext).load(item.avatar.replaceAll("\\\\", "")).placeholder(R.mipmap.app_icon).dontAnimate().into((ImageView) helper.getView(R.id.img_head));
            if (("第" + item.num + "位").equals(txtPh.getText().toString())) {
                linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
            } else {
                linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            }
            if(helper.getAdapterPosition()<3){
                Drawable drawable1 = getResources().getDrawable(R.drawable.icon_ranking_star);
                drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                ( (TextView) helper.getView(R.id.txt_score)).setCompoundDrawables(drawable1, null, null, null);
            }else{
                ( (TextView) helper.getView(R.id.txt_score)).setCompoundDrawables(null, null, null, null);
            }
        }
    }
}
