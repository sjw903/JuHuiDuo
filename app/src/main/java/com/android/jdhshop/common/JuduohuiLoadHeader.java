package com.android.jdhshop.common;



import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.internal.InternalAbstract;

public class JuduohuiLoadHeader extends InternalAbstract {

    public static String REFRESH_HEADER_PULLING = "下拉可以刷新";//"下拉可以刷新";
    public static String REFRESH_HEADER_LOADING = "正在加载...";//"正在加载...";
    public static String REFRESH_HEADER_RELEASE = "释放立即刷新";
    public static String REFRESH_HEADER_FINISH = "刷新成功";//"刷新完成";
    public static String REFRESH_HEADER_FAILED = "刷新失败";//"刷新失败";

    private TextView mTitleText;
    private WaveLoadingView waveLoadingView;
    private ImageView imageView;

    public JuduohuiLoadHeader(Context context) {
        this(context, null);
    }

    public JuduohuiLoadHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JuduohuiLoadHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View view = LayoutInflater.from(context).inflate(R.layout.juduohui_loading_refresh_head, this);
        mTitleText = view.findViewById(R.id.txt);
        waveLoadingView = view.findViewById(R.id.waveLoadingView22);
        imageView = view.findViewById(R.id.logo_loading);

    }



    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
//        if (success) {
//            mTitleText.setText(REFRESH_HEADER_FINISH);
//        } else {
//            mTitleText.setText(REFRESH_HEADER_FAILED);
//        }
        super.onFinish(layout, success);
        return 500; //延迟500毫秒之后再弹回
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        refreshLayout.setEnableScrollContentWhenRefreshed(false);
//        refreshLayout.setEnableOverScrollBounce(false);
        refreshLayout.setEnableHeaderTranslationContent(false);
        switch (newState) {
            case PullDownToRefresh: //下拉过程
                //mTitleText.setText(REFRESH_HEADER_PULLING);
                waveLoadingView.setVisibility(INVISIBLE);
                imageView.setVisibility(VISIBLE);
                break;
            case ReleaseToRefresh: //松开刷新
                //mTitleText.setText(REFRESH_HEADER_RELEASE);
                waveLoadingView.setVisibility(INVISIBLE);
                imageView.setVisibility(VISIBLE);
                break;
            case Refreshing: //loading中
                //mTitleText.setText(REFRESH_HEADER_LOADING);
                waveLoadingView.setVisibility(VISIBLE);
                imageView.setVisibility(INVISIBLE);
                break;
        }
    }
}
