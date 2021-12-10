package com.android.jdhshop.advistion.entry;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSONArray;
import com.android.jdhshop.R;
import com.android.jdhshop.advistion.JuDuoHuiAdvertisement;
import com.kwad.sdk.api.KsAdSDK;
import com.kwad.sdk.api.KsContentPage;
import com.kwad.sdk.api.KsFeedPage;
import com.kwad.sdk.api.KsScene;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoContentFragment extends Fragment implements KsContentPage.PageListener, KsContentPage.VideoListener{
    private final String TAG = getClass().getSimpleName();
    @BindView(R.id.video_container)
    FrameLayout video_container;
    @BindView(R.id.video_box)
    SmartRefreshLayout video_box;
    KsFeedPage kp;
    Activity mActivity;
    Fragment f;

    JuDuoHuiAdvertisement juDuoHuiAdvertisement;
    JSONArray adv_config;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_video_content,container,false);
        ButterKnife.bind(this,v);
        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
        putFragment();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        // Log.d(TAG, "onStop: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        // Log.d(TAG, "onStart: ");
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        if (args.getInt("reload",0) ==1){
            putFragment();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Log.d(TAG, "onViewCreated: ");
        mActivity = getActivity();
        video_box.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                // Log.d(TAG, "onRefresh: ");
                getActivity().runOnUiThread(()->{
                    putFragment();
                    video_box.finishRefresh(true);
                });
            }
        });
        video_box.setEnablePureScrollMode(false).setEnableRefresh(true).setEnableOverScrollBounce(true).setEnableNestedScroll(true);

        KsScene ksScene = new KsScene.Builder(7488000010L).build();
        kp = KsAdSDK.getLoadManager().loadFeedPage(ksScene);
        kp.setVideoListener(this);
        kp.setPageListener(this);
        putFragment();
    }
    private void putFragment(){
        f = kp.getFragment();
        getFragmentManager().beginTransaction().replace(R.id.video_container, f).commit();
    }

    @Override
    public void onPageEnter(KsContentPage.ContentItem contentItem) {
        // Log.d(TAG, "onPageEnter: ");

    }

    @Override
    public void onPageResume(KsContentPage.ContentItem contentItem) {
        // Log.d(TAG, "onPageResume: ");
    }

    @Override
    public void onPagePause(KsContentPage.ContentItem contentItem) {
        // Log.d(TAG, "onPagePause: ");
    }

    @Override
    public void onPageLeave(KsContentPage.ContentItem contentItem) {
        // Log.d(TAG, "onPageLeave: ");
    }

    @Override
    public void onVideoPlayStart(KsContentPage.ContentItem contentItem) {
        // Log.d(TAG, "onVideoPlayStart: ");
    }

    @Override
    public void onVideoPlayPaused(KsContentPage.ContentItem contentItem) {
        // Log.d(TAG, "onVideoPlayPaused: ");
    }

    @Override
    public void onVideoPlayResume(KsContentPage.ContentItem contentItem) {
        // Log.d(TAG, "onVideoPlayResume: ");
    }

    @Override
    public void onVideoPlayCompleted(KsContentPage.ContentItem contentItem) {
        // Log.d(TAG, "onVideoPlayCompleted: ");

    }

    @Override
    public void onVideoPlayError(KsContentPage.ContentItem contentItem, int i, int i1) {
        // Log.d(TAG, "onVideoPlayError: ");
    }
}
