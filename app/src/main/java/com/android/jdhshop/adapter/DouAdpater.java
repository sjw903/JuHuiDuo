package com.android.jdhshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.bean.IsCollectBean;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.lmx.library.media.VideoPlayAdapter;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.DouKindActivity;
import com.android.jdhshop.activity.PromotionDetailsActivity;
import com.android.jdhshop.bean.HaoDanBean;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.utils.BitmapUtils;
import com.android.jdhshop.utils.VideoPlayer;
import com.android.jdhshop.widget.VideoLoadingProgressbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.android.jdhshop.utils.UIUtils.getResources;

public class DouAdpater extends VideoPlayAdapter<DouAdpater.ViewHolder> {
    private Context mContext;
    private int mCurrentPosition;
    private ViewHolder mCurrentHolder;
    DecimalFormat df = new DecimalFormat("0.00");
    private VideoPlayer videoPlayer;
    private TextureView textureView;
    private List<HaoDanBean> items;
    private boolean isCollect = false;//未收藏

    public DouAdpater(Context mContext, List<HaoDanBean> items) {
        this.mContext = mContext;
        videoPlayer = new VideoPlayer();
        this.items = items;
        textureView = new TextureView(mContext);
        videoPlayer.setTextureView(textureView);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_dou, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onPageSelected(int itemPosition, View itemView) {
        mCurrentPosition = itemPosition;
        mCurrentHolder = new ViewHolder(itemView);
        BaseLogDZiYuan.LogDingZiYuan(mCurrentHolder.pinglun, "morexxhdpi.png");
        BaseLogDZiYuan.LogDingZiYuan(mCurrentHolder.goumai, "buyxxhdpi.png");
        mCurrentHolder.title.setText(items.get(mCurrentPosition).itemtitle);
        mCurrentHolder.after.setText("券后:" + items.get(mCurrentPosition).itemendprice.replace(".00", ""));
        mCurrentHolder.coupon.setText(items.get(mCurrentPosition).couponmoney.replace(".00", ""));
        Glide.with(mContext).load(items.get(mCurrentPosition).itempic + "_310x310.jpg").into(mCurrentHolder.ivCover);
        mCurrentHolder.zhuan.setText("1预估赚:" + df.format(Double.valueOf(items.get(mCurrentPosition).tkmoney) * Double.parseDouble(df.format((float) SPUtils.getIntData(mContext, "rate", 0) / 100))));
        mCurrentHolder.llBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HaoDanBean bean = items.get(mCurrentPosition);
                if (bean != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("num_iid", bean.itemid);
                    bundle.putSerializable("bean", bean);
                    bundle.putString("tye", "1");
                    bundle.putString("url", bean.dy_video_url);
                    Intent intent = new Intent(mContext, PromotionDetailsActivity.class);
                    if (bundle != null) {
                        intent.putExtras(bundle);
                    }
                    mContext.startActivity(intent);
                }
            }
        });
        isCollectRequest(items.get(mCurrentPosition).itemid);
        mCurrentHolder.llDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HaoDanBean bean = items.get(mCurrentPosition);
                if (bean != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("num_iid", bean.itemid);
                    bundle.putSerializable("bean", bean);
                    bundle.putString("tye", "1");
                    bundle.putString("url", bean.dy_video_url);
                    Intent intent = new Intent(mContext, PromotionDetailsActivity.class);
                    if (bundle != null) {
                        intent.putExtras(bundle);
                    }
                    mContext.startActivity(intent);
                }
            }
        });
        mCurrentHolder.llMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, DouKindActivity.class));
            }
        });
        mCurrentHolder.Llcol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HaoDanBean bean = items.get(mCurrentPosition);
                if (isCollect) { //已收藏，那么取消收藏
                    if (bean != null) {
                        cancelCollectRequest(bean.itemid);
                    }
                } else {//未收藏，那么收藏
                    if (bean != null) {
                        collectRequest(bean.itemid);
                    }
                }
            }
        });
        playVideo();
    }

    /**
     * @属性:用户是否收藏商品
     * @开发者:陈飞
     * @时间:2018/7/28 21:52
     */
    private void isCollectRequest(String goods_id) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("goods_id", goods_id);
        //LogUtils.d(TAG, "isCollectRequest请求方法内参数 : " + goods_id);
        HttpUtils.post(Constants.MESSAGE_GOODSCOLLECT_IS_COLLECT_URL, mContext, requestParams, new onOKJsonHttpResponseHandler<IsCollectBean>(new TypeToken<Response<IsCollectBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ToastUtils.showShortToast(mContext, responseString + "_isCollectRequest");
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Response<IsCollectBean> datas) {
                if (datas.isSuccess()) {
                    String is_collect = datas.getData().getIs_collect();
                    if ("Y".equals(is_collect)) {//Y已收藏
                        isCollect = true;
                        //动态设置drawableLeft属性
                        Drawable drawable1 = getResources().getDrawable(R.mipmap.coll_red);
                        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                        mCurrentHolder.collectxxhdpi.setImageDrawable(drawable1);
                        //mCurrentHolder.dou_text_shou.setText("已收藏");
                    } else if ("N".equals(is_collect)) {//N未收藏
                        isCollect = false;
                        Drawable drawable1 = getResources().getDrawable(R.drawable.collectxxhdpi);
                        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                        mCurrentHolder.collectxxhdpi.setImageDrawable(drawable1);
                        // mCurrentHolder.dou_text_shou.setText("收藏");
                    }
                } else {
                    ToastUtils.showShortToast(mContext, datas.getMsg() + "_isCollectRequest_success");
                }
            }
        });
    }

    private void collectRequest(String goods_id) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("goods_id", goods_id);
        HttpUtils.post(Constants.MESSAGE_GOODSCOLLECT_COLLECT_URL, mContext,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    //ToastUtils.showShortToast(mContext,msg);
                    if (msg.equals("成功")) {
                        isCollect = true;
                        Drawable drawable1 = mContext.getResources().getDrawable(R.mipmap.coll_red);
                        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                        mCurrentHolder.collectxxhdpi.setImageDrawable(drawable1);
                        //mCurrentHolder.dou_text_shou.setText("已收藏");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * @属性:收藏
     * @开发者:陈飞
     * @时间:2018/7/28 22:04
     */
    private void cancelCollectRequest(final String goods_id) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("goods_id", goods_id);
        HttpUtils.post(Constants.MESSAGE_GOODSCOLLECT_CANCELCOLLECT_URL,mContext, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ToastUtils.showShortToast(mContext, responseString + "__cancelCollectRequest");
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    if (code == 0) {
                        isCollect = false;
                        Drawable drawable1 = getResources().getDrawable(R.drawable.collectxxhdpi);
                        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                        mCurrentHolder.collectxxhdpi.setImageDrawable(drawable1);
                        // mCurrentHolder.dou_text_shou.setText("收藏");
                    } else {
                        ToastUtils.showShortToast(mContext, msg + "__cancelCollectRequest_success");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.clear(holder.bg);
    }

    private void playVideo() {
        videoPlayer.reset();
        mCurrentHolder.videoLoadingProgressbar.setVisibility(View.VISIBLE);
        Glide.with(mContext)
                .load(items.get(mCurrentPosition).first_frame)
                .bitmapTransform(new BlurTransformation(mContext, 5, 3))
                .into(mCurrentHolder.bg);
        videoPlayer.setOnStateChangeListener(new VideoPlayer.OnStateChangeListener() {
            @Override
            public void onReset() {
                mCurrentHolder.videoLoadingProgressbar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onRenderingStart() {
                if (videoPlayer.getWidth() >= videoPlayer.getHeight()) {
                    textureView.setLayoutParams(new FrameLayout.LayoutParams(BitmapUtils.getScreenWith(mContext), (int) (videoPlayer.getHeight() * (((videoPlayer.getWidth() * 1.00 / videoPlayer.getHeight()))))));
                } else {
                    textureView.setLayoutParams(new FrameLayout.LayoutParams(BitmapUtils.getScreenWith(mContext), (int) (videoPlayer.getHeight() * (((videoPlayer.getHeight() * 1.00 / videoPlayer.getWidth()))))));
                }
                mCurrentHolder.videoLoadingProgressbar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onProgressUpdate(float per) {
            }

            @Override
            public void onPause() {
                mCurrentHolder.videoLoadingProgressbar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onStop() {
                mCurrentHolder.videoLoadingProgressbar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onComplete() {
                videoPlayer.start();
            }
        });
        if (textureView.getParent() != mCurrentHolder.flVideo) {
            if (textureView.getParent() != null) {
                ((FrameLayout) textureView.getParent()).removeView(textureView);
            }
            mCurrentHolder.flVideo.addView(textureView);
        }
        videoPlayer.setDataSource(items.get(mCurrentPosition).dy_video_url);
        videoPlayer.prepare();
    }

    public void release() {
        videoPlayer.release();
    }

    public void pause() {
        videoPlayer.pause();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivCover, bg, pinglun, goumai;
        private FrameLayout flVideo;
        private TextView title, after, coupon, zhuan, dou_text_shou;
        private LinearLayout llDetail, llMore, llBuy, Llcol;
        private VideoLoadingProgressbar videoLoadingProgressbar;
        private ImageView collectxxhdpi;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            bg = itemView.findViewById(R.id.bg);
            pinglun = itemView.findViewById(R.id.dou_imageping);
            goumai = itemView.findViewById(R.id.dou_imagegou);
            flVideo = itemView.findViewById(R.id.flVideo);
            ivCover = itemView.findViewById(R.id.img_cover);
            title = itemView.findViewById(R.id.title);
            after = itemView.findViewById(R.id.txt_after);
            coupon = itemView.findViewById(R.id.txt_coupon);
            zhuan = itemView.findViewById(R.id.txt_zhuan);
            llDetail = itemView.findViewById(R.id.ll_detail);
            llMore = itemView.findViewById(R.id.more);
            llBuy = itemView.findViewById(R.id.buy);
            Llcol = itemView.findViewById(R.id.coll);
            videoLoadingProgressbar = itemView.findViewById(R.id.bar);
            collectxxhdpi = itemView.findViewById(R.id.dou_shoucang);
            dou_text_shou = itemView.findViewById(R.id.dou_text_shou);

        }
    }
}
