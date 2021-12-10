package com.android.jdhshop.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.reflect.TypeToken;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.MessageCenterBean;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import fm.jiecao.jcvideoplayer_lib.JCUserAction;
import fm.jiecao.jcvideoplayer_lib.JCUserActionStandard;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * 我的页面  新人课堂功能
 */
public class NewClassActivity extends BaseActivity {
    @BindView(R.id.bg_head)
    LinearLayout bg_head;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private MyAdapter myAdapter;
    private List<MessageCenterBean.MessageCenterChildBean> list=new ArrayList<>();
    private int page=1;
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_newclass);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("新手课堂");
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter=new MyAdapter(this,R.layout.item_class,list);
        recyclerView.setAdapter(myAdapter);
        refreshLayout.autoRefresh();
    }

    @Override
    protected void initListener() {
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                page++;
                getArticle();
                refreshLayout.finishLoadMore();
            }

            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                getArticle();
                refreshLayout.finishRefresh();
            }
        });
    }
    private void getArticle(){
        RequestParams requestParams = new RequestParams();
        requestParams.put("cat_id", 3);
        requestParams.put("p", page);
        requestParams.put("per", 6);
        HttpUtils.post(Constants.MESSAGE_ARTICLE_GETARTICLELIST_URL, NewClassActivity.this,requestParams, new onOKJsonHttpResponseHandler<MessageCenterBean>(new TypeToken<Response<MessageCenterBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onSuccess(int statusCode, Response<MessageCenterBean> datas) {
                if (datas.isSuccess()) {
                    if(page==1){
                        list.clear();
                    }
                    list.addAll(datas.getData().getList());
                    if (refreshLayout != null) {
                        if (page == 1) {
                            refreshLayout.finishRefresh();
                        } else {
                            refreshLayout.finishLoadMore();
                        }
                    }
                    if(datas.getData().getList().size()<=0&&page>1){
                        ToastUtils.showShortToast(NewClassActivity.this,"没有更多数据");
                        page--;
                    }
                } else {
                    showToast(datas.getMsg());
                }
                myAdapter.notifyDataSetChanged();
            }
        });
    }
    @OnClick(R.id.tv_left)
    public void onViewClicked() {
        finish();
    }
    class MyAdapter extends CommonAdapter<MessageCenterBean.MessageCenterChildBean> {
        public MyAdapter(Context context, int layoutId, List<MessageCenterBean.MessageCenterChildBean> datas) {
            super(context, layoutId, datas);
        }
        @Override
        protected void convert(ViewHolder holder, MessageCenterBean.MessageCenterChildBean classInfo, int position) {
            holder.setText(R.id.txt_time,classInfo.getPubtime());
           final JCVideoPlayerStandard videoView=holder.getView(R.id.videoView);
            holder.setText(R.id.txt_content,classInfo.getTitle());
            videoView.setUp(classInfo.getHref(), JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, classInfo.getTitle());
            Glide.with(NewClassActivity.this).load(Constants.APP_IP+(classInfo.getImg()==null?"":classInfo.getImg().replace(" ",""))).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                    videoView.thumbImageView.setImageBitmap(bitmap);

                }
            });
            JCVideoPlayer.setJcUserAction(new MyUserActionStandard());
        }
    }

    @Override
    public void onBackPressed() {
        if(JCVideoPlayerStandard.backPress()){
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
    class MyUserActionStandard implements JCUserAction {

        @Override
        public void onEvent(int type, String url, int screen, Object... objects) {
            switch (type) {
                case JCUserAction.ON_CLICK_START_ICON:
                    break;
                case JCUserAction.ON_CLICK_START_ERROR:
                    break;
                case JCUserAction.ON_CLICK_START_AUTO_COMPLETE:
                    break;
                case JCUserAction.ON_CLICK_PAUSE:
                    break;
                case JCUserAction.ON_CLICK_RESUME:
                    break;
                case JCUserAction.ON_SEEK_POSITION:
                    break;
                case JCUserAction.ON_AUTO_COMPLETE:
                    break;
                case JCUserAction.ON_ENTER_FULLSCREEN:
                    break;
                case JCUserAction.ON_QUIT_FULLSCREEN:
                    break;
                case JCUserAction.ON_ENTER_TINYSCREEN:
                    break;
                case JCUserAction.ON_QUIT_TINYSCREEN:
                    break;
                case JCUserAction.ON_TOUCH_SCREEN_SEEK_VOLUME:
                    break;
                case JCUserAction.ON_TOUCH_SCREEN_SEEK_POSITION:
                    break;
                case JCUserActionStandard.ON_CLICK_START_THUMB:
                    break;
                case JCUserActionStandard.ON_CLICK_BLANK:
                    break;
                default:
                    break;
            }
        }
    }
}
