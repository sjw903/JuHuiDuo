package com.android.jdhshop.advistion.entry;

import android.os.Bundle;
import android.util.Log;

import com.android.jdhshop.R;
import com.kwad.sdk.api.KsAdSDK;
import com.kwad.sdk.api.KsContentPage;
import com.kwad.sdk.api.KsScene;

public class EntryActivity extends ContentBaseActivity implements KsContentPage.PageListener, KsContentPage.VideoListener {
    private KsContentPage ksContentPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KsScene ksScene = new KsScene.Builder(7488000010L).setBackUrl("ksad://returnback").build();
        ksContentPage = KsAdSDK.getLoadManager().loadContentPage(ksScene);
        ksContentPage.setAddSubEnable(true);
        ksContentPage.setPageListener(this);
        ksContentPage.setVideoListener(this);
        ksContentPage.setEcBtnClickListener(new KsContentPage.KsEcBtnClickListener() {
            @Override
            public void onOpenKwaiBtnClick() {
                Log.d("TAG", "onOpenKwaiBtnClick: ");
            }

            @Override
            public void onGoShoppingBtnClick(String s) {
                Log.d("TAG", "onGoShoppingBtnClick: ");
            }

            @Override
            public void onCurrentGoodCardClick(String s) {
                Log.d("TAG", "onCurrentGoodCardClick: ");
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.container,ksContentPage.getFragment()).commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_entry;
    }

    @Override
    public void onBackPressed() {
        if (ksContentPage!=null && ksContentPage.onBackPressed()){
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPageEnter(KsContentPage.ContentItem contentItem) {}

    @Override
    public void onPageResume(KsContentPage.ContentItem contentItem) {

    }

    @Override
    public void onPagePause(KsContentPage.ContentItem contentItem) {

    }

    @Override
    public void onPageLeave(KsContentPage.ContentItem contentItem) {}

    @Override
    public void onVideoPlayStart(KsContentPage.ContentItem contentItem) {

    }

    @Override
    public void onVideoPlayPaused(KsContentPage.ContentItem contentItem) {

    }

    @Override
    public void onVideoPlayResume(KsContentPage.ContentItem contentItem) {

    }

    @Override
    public void onVideoPlayCompleted(KsContentPage.ContentItem contentItem) {

    }

    @Override
    public void onVideoPlayError(KsContentPage.ContentItem contentItem, int i, int i1) {

    }
}