package com.android.jdhshop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.ShopActicleBean;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.utils.DateUtils;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * @属性:文章类
 * @开发者:陈飞
 * @时间:2018/7/24 10:45
 */
public class NewsOnlyContentActivity extends BaseActivity {


    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.titleOne)
    TextView titleOne;
    @BindView(R.id.read_data_v)
    TextView readDataV;
    @BindView(R.id.read_small_title)
    TextView readSmallTitle;
    @BindView(R.id.read_iamge)
    ImageView readIamge;
    @BindView(R.id.read_content)
    WebView readContent;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private String article_id;
    private String title;


    /**
     * 启动该页面
     *
     * @param mContext
     * @param article_id
     */
    public static void actionStart(Context mContext, String article_id, String title) {
        Intent intent = new Intent(mContext, NewsOnlyContentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("article_id", article_id);
        bundle.putString("title", title);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_news_only_content);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        tvLeft.setVisibility(View.VISIBLE);

        Intent intent = getIntent();

        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                article_id = extras.getString("article_id");
                title = extras.getString("title");
            }
        }

        //设置标题
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }

        if (!TextUtils.isEmpty(article_id)) {
            requestShopData(article_id);
        }

    }

    @Override
    protected void initListener() {

    }

    /**
     * @属性:文章管理接口
     * @开发者:陈飞
     * @时间:2018/7/20 15:32
     */
    private void requestShopData(String position) {
        //首页广告栏请求数据
        RequestParams shopParams = new RequestParams();
        shopParams.put("article_id", position);
        HttpUtils.post(Constants.MESSAGE_ARTICLE_GETARTICLEMSG_URL, NewsOnlyContentActivity.this,shopParams, new onOKJsonHttpResponseHandler<ShopActicleBean>(new TypeToken<Response<ShopActicleBean>>() {
        }) {

            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Response<ShopActicleBean> datas) {
                if (datas.isSuccess()) {
                    ShopActicleBean.ArticleAsg article_msg = datas.getData().getArticle_msg();
                    if (article_msg != null) {
                        //显示标题
                        titleOne.setText(article_msg.getTitle());
                        tvTitle.setText(article_msg.getTitle());
                        //显示时间和阅读数
                        readDataV.setText(DateUtils.format_yyyy_MMstr(article_msg.getPubtime()) + " | " + article_msg.getClicknum() + "人已读");
                        //显示摘要
                        readSmallTitle.setText(article_msg.getKeywords());
                        //为空，则隐藏
                        if (TextUtils.isEmpty(article_msg.getImg())) {
                            readIamge.setVisibility(View.GONE);
                        }
                        //显示图片
                        Glide.with(getComeActivity()).load(Constants.APP_IP + article_msg.getImg()).into(readIamge);
                        //显示内容
                        readContent.loadDataWithBaseURL(Constants.APP_IP,article_msg.getContent()==null?"":article_msg.getContent().replaceAll("<img","<img style='width:100%'"),"text/html","UTF-8","");
                    }
                } else {
                    showToast(datas.getMsg());
                }
            }
        });
    }


    @OnClick(R.id.tv_left)
    public void onViewClicked() {
        finish();
    }

}
