package com.android.jdhshop.juduohui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.jdhshop.R;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.utils.UIUtils;
import com.android.jdhshop.utils.WxUtil;
import com.android.jdhshop.wmm.QQShareUtil;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JuduohuiShareDialog extends AlertDialog {
    private String TAG = getClass().getSimpleName();
    private Activity mActivity;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    private boolean mCancelable = false;
    @BindView(R.id.share_main)
    RelativeLayout share_main;
    private View mRootView;
    @BindView(R.id.share_wx_friend)
    LinearLayout share_wx_friend;
    @BindView(R.id.share_wx_timeline)
    LinearLayout share_wx_timeline;
    @BindView(R.id.share_qq_friend)
    LinearLayout share_qq_friend;
    @BindView(R.id.share_qzone)
    LinearLayout share_qzone;
    @BindView(R.id.share_copy_link)
    LinearLayout share_copy_link;
    @BindView(R.id.share_system)
    LinearLayout share_system;

    public static int SHARE_CHANNEL_WX_FRIEND = 1;
    public static int SHARE_CHANNEL_WX_TIMELINE = 2;
    public static int SHARE_CHANNEL_QQ_FRIEND = 3;
    public static int SHARE_CHANNEL_QQ_ZONE = 4;
    public static int SHARE_CHANNEL_CLIPBOARD = 5;
    public static int SHARE_CHANNEL_SYSTEM = 6;

    String share_message_title, share_message_content, share_url, share_image;
    ShareListen listen;
    ArrayList<String> share_message_image;

    public JuduohuiShareDialog(@NonNull Activity activity) {
        super(activity);
        mActivity = activity;
        builder = new AlertDialog.Builder(mActivity);
        mRootView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_juduohui_share, null, false);
        builder.setView(mRootView);
        ButterKnife.bind(this, mRootView);
    }

    public interface ShareListen {
        void onShareSuccess(int share_channel);

        void onShareFailure(int share_channel);

        void onCancel();
    }

    public JuduohuiShareDialog setShareMessageTitle(String messageTitle) {
        share_message_title = messageTitle;
        return this;
    }

    public JuduohuiShareDialog setShareMessageContent(String messageContent) {
        share_message_content = messageContent;
        return this;
    }

    public JuduohuiShareDialog setShareMessageUrl(String messageUrl) {
        share_url = messageUrl;
        return this;
    }

    public JuduohuiShareDialog setShareMessageImage(String messageImage) {
        share_image = messageImage;
        return this;
    }

    public JuduohuiShareDialog setShareMessageImage(ArrayList<String> messageImage) {
        share_message_image = messageImage;
        return this;
    }

    public JuduohuiShareDialog setShareListen(ShareListen shareListen) {
        listen = shareListen;
        return this;
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.share_main, R.id.share_qzone, R.id.share_wx_timeline, R.id.share_wx_friend, R.id.share_qq_friend, R.id.share_copy_link, R.id.share_system})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.share_copy_link:
                ClipboardManager clipboardManager = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboardManager != null) {
                    clipboardManager.setPrimaryClip(ClipData.newPlainText(share_message_title, share_message_title + "\n\n" + share_url));
                    listen.onShareSuccess(SHARE_CHANNEL_CLIPBOARD);
                } else {
                    listen.onShareFailure(SHARE_CHANNEL_CLIPBOARD);
                }
                dialog.dismiss();
                break;
            case R.id.share_main:
                listen.onCancel();
                dialog.dismiss();
                break;
            case R.id.share_wx_friend:
                if (WxUtil.sendCardMessage(mActivity, share_message_title, share_message_content, share_url, WxUtil.WX_FRIEND)) {
                    listen.onShareSuccess(SHARE_CHANNEL_WX_FRIEND);
                } else {
                    listen.onShareFailure(SHARE_CHANNEL_WX_FRIEND);
                }
                dialog.dismiss();
                break;
            case R.id.share_wx_timeline:
                if (WxUtil.sendCardMessage(mActivity, share_message_title, share_message_content, share_url, WxUtil.WX_TIMELINE)) {
                    listen.onShareSuccess(SHARE_CHANNEL_WX_TIMELINE);
                } else {
                    listen.onShareFailure(SHARE_CHANNEL_WX_TIMELINE);
                }
                dialog.dismiss();
                break;
            case R.id.share_qzone:
                if (share_message_image == null && share_image != null) {
                    share_message_image = new ArrayList<>();
                    share_message_image.add(share_image);
                }

                Log.d(TAG, "onViewClick: " + share_message_image);
                Log.d(TAG, "onViewClick: " + share_message_image.size());

                try {
                    QQShareUtil.shareToQZone(share_message_image, share_message_title, share_message_content, share_url, mActivity, new IUiListener() {
                        @Override
                        public void onComplete(Object o) {
                            Log.d(TAG, "onComplete: " + o);
                            listen.onShareSuccess(SHARE_CHANNEL_QQ_ZONE);
                        }

                        @Override
                        public void onError(UiError uiError) {
                            Log.d(TAG, "onError: " + uiError.errorCode + "," + uiError.errorMessage + "," + uiError.errorDetail);
                            listen.onShareFailure(SHARE_CHANNEL_QQ_ZONE);
                        }

                        @Override
                        public void onCancel() {
                            Log.d(TAG, "onCancel: 取消");
                            listen.onCancel();
                        }
                    });
                    listen.onShareSuccess(SHARE_CHANNEL_QQ_ZONE);
                } catch (Exception e) {
                    listen.onShareFailure(SHARE_CHANNEL_QQ_ZONE);
                }
                dialog.dismiss();
                break;
            case R.id.share_qq_friend:
                if (share_message_image == null && share_image != null) {
                    share_message_image = new ArrayList<>();
                    share_message_image.add(share_image);
                }
                try {
                    QQShareUtil.shareToQQ(share_message_title, share_message_content, share_url, share_message_image, mActivity, new IUiListener() {
                        @Override
                        public void onComplete(Object o) {
                            Log.d(TAG, "onComplete: " + o);
                            listen.onShareSuccess(SHARE_CHANNEL_QQ_FRIEND);
                        }

                        @Override
                        public void onError(UiError uiError) {
                            Log.d(TAG, "onError: " + uiError.errorCode + "," + uiError.errorMessage + "," + uiError.errorDetail);
                            listen.onShareFailure(SHARE_CHANNEL_QQ_FRIEND);
                        }

                        @Override
                        public void onCancel() {
                            Log.d(TAG, "onCancel: 取消");
                            listen.onCancel();
                        }
                    });
                    listen.onShareSuccess(SHARE_CHANNEL_QQ_FRIEND);
                } catch (Exception e) {
                    listen.onShareFailure(SHARE_CHANNEL_QQ_FRIEND);
                }
                Log.d(TAG, "onViewClick: share_qq_friend");
                dialog.dismiss();
                break;
            case R.id.share_system:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, share_message_title + "\n\n" + share_url);
                mActivity.startActivity(Intent.createChooser(intent, share_message_title));
                listen.onShareSuccess(SHARE_CHANNEL_SYSTEM);
        }
    }

    public void show() {
        if (null == share_message_image) {
            share_message_image = new ArrayList<>();
        }
        if (share_image != null) {
            share_message_image.add(share_image);
        } else if ((share_image == null || "".equals(share_image))) {
            share_message_image.add(SPUtils.getStringData(mActivity, "app_logo", "https://juduohui-s.oss-cn-shenzhen.aliyuncs.com/Upload/logo/logo.png"));
        }

        if (listen == null) listen = new ShareListen() {
            @Override
            public void onShareSuccess(int channel) {
            }

            @Override
            public void onShareFailure(int channel) {
            }

            @Override
            public void onCancel() {
            }
        };


        dialog = builder.create();
        dialog.show();

        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.main_menu_animstyle);
        window.setBackgroundDrawable(null);
        DisplayMetrics dm = new DisplayMetrics();
        window.setLayout(UIUtils.getScreenMeasuredWidth(mActivity), UIUtils.getScreenMeasuredHeight(mActivity));
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.WHITE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
