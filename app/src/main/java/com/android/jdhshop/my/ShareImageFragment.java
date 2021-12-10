package com.android.jdhshop.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.InviteAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.base.BaseFragment;
import com.android.jdhshop.bean.BannerBean;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.login.WelActivity;
import com.android.jdhshop.utils.ImgUtils;
import com.android.jdhshop.utils.WxUtil;
import com.android.jdhshop.widget.LoadingDialog;
import com.android.jdhshop.wmm.QQShareUtil;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.analytics.android.api.CountEvent;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cz.msebera.android.httpclient.Header;

public class ShareImageFragment extends BaseFragment implements IUiListener {

    @BindView(R.id.copy_friends_cicle_btn)
    TextView copy_friends_cicle_btn;
    @BindView(R.id.copy_friends_btn)
    TextView copy_friends_btn;
    @BindView(R.id.copy_friends_qq)
    TextView copy_friends_qq;
    @BindView(R.id.copy_friends_cicle_zone)
    TextView copy_friends_cicle_zone;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private InviteAdapter adapter;
    private List<BannerBean> list=new ArrayList<>();
    private static final float MIN_SCALE = .95f;
    private static final float MAX_SCALE = 1.00f;
    private LinearLayoutManager mLinearLayoutManager;
    private int mMinWidth;
    private int mScreenWidth;
    private Bitmap bitmap;
    private String url;
    @BindView(R.id.view_zz)
    View zz;
    @BindView(R.id.ll_share)
    LinearLayout llShare;

    String TAG = getClass().getSimpleName();
    LoadingDialog loadingDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share_image, container, false);
        ButterKnife.bind(this,view);

        loadingDialog = new LoadingDialog(getActivity());

        String user_token = SPUtils.getStringData(getActivity(),"token","");
        if (user_token.equals("")) {
            BaseActivity a = (BaseActivity) getActivity();
            a.openActivityForResult(WelActivity.class,null,6666);
            a.setOnActivityResultLisntener(new BaseActivity.onActivityResultLisntener() {
                @Override
                public void onActivityResult(int requestCode, int resultCode, Intent data) {
                    // Log.d(TAG, "onActivityResult: " + resultCode + " , " + resultCode + "," + data);
                    if (requestCode == 6666 && resultCode == -1){
                        // Log.d(TAG, "onActivityResult: 成功登陆");
                        getActivity().recreate();
                    }
                    else{
                        // Log.d(TAG, "onActivityResult: 关闭");
                        getActivity().finish();
                    }
                }
            });
        }
        else {
            if (CaiNiaoApplication.getUserInfoBean() == null) {
                loadingDialog.show();
                getUserMessage();
            }
            else {
                init();
            }
        }
        return view;
    }

    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (CaiNiaoApplication.getUserInfoBean() != null){
                removeCallbacksAndMessages(null);
                init();
            }
            else{
                sendEmptyMessageDelayed(-1,100);
            }
        }
    };


    private void getUserMessage(){
        CaiNiaoApplication.commonGetUserMsg();
        handler.sendEmptyMessageDelayed(-1,100);
    }

    private void init(){
        loadingDialog.dismiss();
        getBanner();

//        if(CaiNiaoApplication.getUserInfoBean()!=null && "Y".equals(CaiNiaoApplication.getUserInfoBean().user_msg.is_share_vip)){
//            url= SPUtils.getStringData( getActivity(),"share_url_vip","")+"/wap/UserAccount/register/referrer_id/"+ SPUtils.getStringData(getContext(),Constants.UID,"");
//        }else{
////            if("2".equals(SPUtils.getStringData(this,"down_type","1"))){
////                url=Constants.SHARE_URL_REGISTER;
////            }else{
//            //修改邀请链接加邀请码
//            url=Constants.APP_IP+"/wap/Index/down?inviteCode="+ SPUtils.getStringData(getContext(),"inviteCode","");
////            }
//        }

        url = CaiNiaoApplication.getUserInfoBean().user_msg.share_url;

        // 这里报错 java.lang.NullPointerException: Attempt to read from field 'com.android.jdhshop.bean.UserInfoBean$UserMsgBean com.android.jdhshop.bean.UserInfoBean.user_msg' on a null object reference


        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mMinWidth = (int) (mScreenWidth * 0.28f);
        LinearLayoutManager ms= new LinearLayoutManager(getContext());
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(ms);
        adapter=new InviteAdapter(getContext(), url,list);
        recyclerView.setAdapter(adapter);
        if(Build.VERSION.SDK_INT>=23)
            recyclerView.setOnScrollListener(mOnScrollListener);
        adapter.setOnCheckedStatusListener(new InviteAdapter.onCheckedStatusListener() {
            @Override
            public void onCheckedStatus(int position) {
                adapter.setCheckedStatus(position);
            }
        });

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(()->{
                    adapter.setCheckedStatus(0);
                });
            }
        },800);
    }
    @OnClick({R.id.txt_finish,R.id.btn_copy,R.id.btn_invite,R.id.copy_friends_qq,R.id.copy_friends_cicle_zone, R.id.view_zz, R.id.copy_friends_cicle_btn, R.id.copy_friends_btn})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.view_zz:
                zz.setVisibility(View.GONE);
                llShare.setVisibility(View.GONE);
                break;
            case R.id.txt_finish:
                zz.setVisibility(View.GONE);
                llShare.setVisibility(View.GONE);
                break;
            case R.id.btn_copy:
                // 新下载逻辑
                String is_saved= ImgUtils.saveImageToGallery2(getContext(),adapter.getBitmap());
                // Log.d(TAG, "onClick: " + is_saved);
                if("".equals(is_saved)){
                    // Log.d(TAG, "onClick: 失败");
                    showToast("保存失败");
                }
                else
                {
                    // Log.d(TAG, "onClick: 成功");
                    showToast("保存成功");
                }
                // 原复制逻辑
//                ClipboardManager myClipboard;
//                myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
//                ClipData myClip;
//                myClip = ClipData.newPlainText("text", url);
//                myClipboard.setPrimaryClip(myClip);
//                ToastUtils.showShortToast(MyShareUrlActivity.this,"复制成功");
                break;
            case R.id.copy_friends_qq:
                if (getContext().getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq") == null) {
                    T.showShort(getContext(), "请安装QQ客户端");
                    return;
                }
                String str=ImgUtils.saveImageToGallery2(getContext(),bitmap);
                if("".equals(str)){
                    T.showShort(getContext(), "分享失败");
                    return;
                }
                QQShareUtil.shareImgToQQ(str, getActivity(), this);
                addTime();
                zz.setVisibility(View.GONE);
                llShare.setVisibility(View.GONE);
                break;
            case R.id.copy_friends_cicle_zone:
                if (getContext().getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq") == null) {
                    T.showShort(getContext(), "请安装QQ客户端");
                    return;
                }
                String str1=ImgUtils.saveImageToGallery2(getContext(),bitmap);
                if("".equals(str1)){
                    T.showShort(getContext(), "分享失败");
                    return;
                }
                QQShareUtil.shareImgToQQZONE(str1, getActivity(),this);
                addTime();
                zz.setVisibility(View.GONE);
                llShare.setVisibility(View.GONE);
                break;
            case R.id.copy_friends_cicle_btn:
                if (getContext().getPackageManager().getLaunchIntentForPackage("com.tencent.mm") == null) {
                    T.showShort(getContext(), "请安装微信客户端");
                    return;
                }
                WxUtil.sharePicByBitMap(bitmap, "pyq", SendMessageToWX.Req.WXSceneTimeline, getContext());
                addTime();
                zz.setVisibility(View.GONE);
                llShare.setVisibility(View.GONE);
                break;
            case R.id.copy_friends_btn:
                if (getContext().getPackageManager().getLaunchIntentForPackage("com.tencent.mm") == null) {
                    T.showShort(getContext(), "请安装微信客户端");
                    return;
                }
                WxUtil.sharePicByBitMap(bitmap, "pyq", SendMessageToWX.Req.WXSceneSession, getContext());
                addTime();
                zz.setVisibility(View.GONE);
                llShare.setVisibility(View.GONE);
                break;
            case R.id.btn_invite:
                bitmap=adapter.getBitmap();
                if(bitmap==null){
                    showToast("请选择分享的海报");
                    return;
                }
                zz.setVisibility(View.VISIBLE);
                llShare.setVisibility(View.VISIBLE);
                break;
        }
    }
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            final int childCount = recyclerView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                LinearLayout child = (LinearLayout) recyclerView.getChildAt(i);
                RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
                int left = child.getLeft();
                int right = mScreenWidth - child.getRight();
                final float percent = left < 0 || right < 0 ? 0 : Math.min(left, right) * 1f / Math.max(left, right);
                float scaleFactor = MIN_SCALE + Math.abs(percent) * (MAX_SCALE - MIN_SCALE);
                child.setLayoutParams(lp);
                child.setScaleY(scaleFactor);
            }
        }
    };



    private void getBanner(){
        RequestParams requestParams = new RequestParams();
        requestParams.put("cat_id", 4);
        HttpUtils.post(Constants.GET_BANNER, ShareImageFragment.this,requestParams, new onOKJsonHttpResponseHandler<BannerBean>(new TypeToken<Response<BannerBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(throwable.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Response<BannerBean> datas) {
                if (datas.isSuccess()) {
                    list.clear();
                    list.addAll(datas.getData().getList());
                    adapter.notifyDataSetChanged();
                } else {
//                    showToast(datas.getMsg());
                }
            }
        });
    }
    private void  addTime(){
        JAnalyticsInterface.onEvent(getContext(),new CountEvent("copy_invite"));
    }

    @Override
    public void onComplete(Object o) {

    }

    @Override
    public void onError(UiError uiError) {

    }

    @Override
    public void onCancel() {

    }
}