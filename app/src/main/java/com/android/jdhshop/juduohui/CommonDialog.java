package com.android.jdhshop.juduohui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.R;
import com.android.jdhshop.advistion.JuDuoHuiAdvertisement;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.utils.UIUtils;
import com.android.jdhshop.widget.RainbowDrawable;


//共用带广告弹窗
public class CommonDialog {
    public static final String ACTIVE = "active";
    Activity mActivity;
    String msg;
    AlertDialog.Builder builder;
    LinearLayout msg_content;
    LinearLayout msg_content_center;
    LinearLayout adv_content;
    LinearLayout button_content;
    LinearLayout progress_content;
    TextView msg_title;
    TextView msg_text;
    Button submit;
    Button cancel;
    View custom_view;
    AlertDialog alertDialog;
    TextView close_button;
    CommonDialogListener commonDialogListener;
    JuDuoHuiAdvertisement advertisement;
    JSONArray adv_config;
    ProgressBar progress_bar;
    Button install_but;
    boolean cancel_on_out = true;
    boolean can_cancel = true;
    private String TAG = getClass().getSimpleName();
    private static CommonDialog commonDialog;
    private int adsErrorMaxRetry = 6;
    private int adsErrorCount = 1;

    public CommonDialog(Activity a) {
        mActivity = a;
        builder = new AlertDialog.Builder(a, R.style.dialogstyle);
        custom_view = View.inflate(mActivity, R.layout.common_dialog, null);
        msg_content = custom_view.findViewById(R.id.msg_content);
        msg_content_center = custom_view.findViewById(R.id.alert_msg_box);
        button_content = custom_view.findViewById(R.id.button_content);
        progress_content = custom_view.findViewById(R.id.progress_content);
        progress_bar = custom_view.findViewById(R.id.progress_bar);

        RainbowDrawable.Builder drawable = new RainbowDrawable.Builder();
        drawable.setStrokeWidth(UIUtils.dp2px(5))
                .setDuration(1000)
                .setBackgroundColor(Color.parseColor("#FFFFFF"))
                .setGradientColorAndPosition(new int[]{Color.RED, Color.parseColor("#FF9800"), Color.parseColor("#FFEB3B"), Color.parseColor("#FF9800"), Color.RED}, new float[]{0f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f})
                .setRadius(UIUtils.dp2px(15));
        adv_content = custom_view.findViewById(R.id.adv_content);


        msg_title = custom_view.findViewById(R.id.alert_title);
        msg_text = custom_view.findViewById(R.id.alert_msg);

        submit = custom_view.findViewById(R.id.submit_bt);
        submit.setVisibility(View.GONE);
        cancel = custom_view.findViewById(R.id.cancel_bt);
        cancel.setVisibility(View.GONE);
        install_but = custom_view.findViewById(R.id.install_bt);

        close_button = custom_view.findViewById(R.id.close_bt);
//        custom_view.setBackgroundColor(Color.parseColor("#000000"));
        advertisement = new JuDuoHuiAdvertisement(a, null);
        String adv_cfg_str = SPUtils.getStringData(a, "ad_place_app_al", null);
        if (adv_cfg_str != null) {
            try {
                JSONObject adv_cfg = JSON.parseObject(adv_cfg_str);
                adv_config = adv_cfg.getJSONArray("list");

                advertisement.setInfomationAdListen(new JuDuoHuiAdvertisement.InfomationAdListen() {
                    @Override
                    public void click(View v) {

                    }

                    @Override
                    public void dislike() {

                    }

                    @Override
                    public void display(View v, String position, JSONObject config) {
                        mActivity.runOnUiThread(() -> {
                            adv_content.addView(v);
                            adv_content.setBackground(drawable.build());
                            v.post(() -> {
                                LinearLayout.LayoutParams lm = (LinearLayout.LayoutParams) adv_content.getLayoutParams();
                                // Log.d(TAG, "ad display: w:" + v.getWidth() + ",h:" + v.getHeight() + ",mh" + v.getMinimumHeight() + ","+v.getMeasuredHeight());
//                                lm.width = v.getWidth();
                                if (v.getHeight() != 0) {
                                    // 兼容快手广告,快手广告渲染出来是757高
                                    if (v.getHeight() > 400) {
                                        WindowManager wn = mActivity.getWindowManager();
                                        DisplayMetrics dm = new DisplayMetrics();
                                        wn.getDefaultDisplay().getRealMetrics(dm);
                                        // Log.d(TAG, "display: " + dm.toString());
                                        lm.height = UIUtils.dp2px(120);
                                    } else {
                                        lm.height = v.getHeight() + UIUtils.dp2px(20);
                                    }

                                    adv_content.setLayoutParams(lm);
                                }
                            });
                        });
                    }

                    @Override
                    public void displayed() {

                    }

                    @Override
                    public void close() {
                        adv_content.setVisibility(View.GONE);
                    }

                    @Override
                    public void error(JSONObject error) {
                        // Log.d(TAG, "news_top_adv error: " + error.toJSONString());
                        // Log.d(TAG, "error: adsErrorCount = " + adsErrorCount +", adsErrorMaxRetry="+adsErrorMaxRetry);
                        if (adsErrorCount < adsErrorMaxRetry) {
                            adsErrorCount = adsErrorCount + 1;
                            mActivity.runOnUiThread(() -> {
                                advertisement.getInfomationAdv(adv_config, "common_dialog", (int) (UIUtils.px2dp(adv_content.getWidth()) - 20), 140);

                            });
                        }
//                        if (error.getIntValue("code") == 4014 || error.getIntValue("code") == -2 || error.getIntValue("code") == 40003){
//                            mActivity.runOnUiThread(()->{
//                                advertisement.getInfomationAdv(adv_confi`g,"common_dialog",(int) (UIUtils.px2dp(adv_content.getWidth())-20),140);
//
//                            });
//                        }
                    }
                });


                adv_content.postDelayed(() -> {
                    // Log.d(TAG, "CommonDialog: " + adv_content.getMeasuredWidth() + ", w " + adv_content.getWidth());
                    advertisement.getInfomationAdv(adv_config, "common_dialog", (int) (UIUtils.px2dp(adv_content.getWidth()) - 20), 140);
                }, 50);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static CommonDialog getInstance(Activity a) {
        commonDialog = new CommonDialog(a);
        return commonDialog;
    }

    public void show() {
        builder.setView(custom_view);
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commonDialogListener != null) commonDialogListener.OnClose(alertDialog);
                alertDialog.dismiss();
            }
        });
        // Log.d(TAG, "show: " + commonDialogListener);
        if (commonDialogListener != null) {
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commonDialogListener.OnSubmit(alertDialog);
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commonDialogListener.OnCancel(alertDialog);
                }
            });
        } else {
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        }

        View.OnTouchListener o = (v, event) -> {
            Button bt = (Button) v;
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (!ACTIVE.equals(bt.getTag())) {
                        bt.setTextColor(Color.parseColor("#EF411F"));
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    if (!ACTIVE.equals(bt.getTag())) {
                        bt.setTextColor(Color.parseColor("#000000"));
                    }
                    break;
            }
            return false;
        };
//        submit.setOnTouchListener(o);
//        cancel.setOnTouchListener(o);

        alertDialog = builder.create();
        if (commonDialogListener != null) {
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    commonDialogListener.OnDismiss();
                }
            });
        }

        alertDialog.setCanceledOnTouchOutside(cancel_on_out);
        alertDialog.setCancelable(can_cancel);
//        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.AnimDown);  //添加动画
        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        custom_view.setMinimumHeight(dm.heightPixels);
        alertDialog.show();
        //设置dialog的宽度和手机宽度一样
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        // Log.d(TAG, "show: " + dm);
        lp.width = window.getWindowManager().getDefaultDisplay().getWidth() - UIUtils.dp2px(40);
        alertDialog.getWindow().setAttributes(lp);//设置宽度
    }

    public CommonDialog setTitle(String title) {
        msg_title.setText(title);
        return this;
    }

    public CommonDialog setMessage(String msg) {
//        msg_text.setText(Html.fromHtml(msg,null,null));
        String tmp = msg.replace("\\n", "\n");
        msg_text.setText(Html.fromHtml(tmp));
        return this;
    }

    /**
     * 是否点击外部关闭弹窗
     *
     * @param canceledOnTouchOutside boolean true是，false否
     * @return this
     */
    public CommonDialog setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        cancel_on_out = canceledOnTouchOutside;
        return this;
    }

    public CommonDialog setCancelable(boolean is_can_cancel) {
        can_cancel = is_can_cancel;
        return this;
    }

    public CommonDialog setShowCloseButton(boolean is_show_close_button) {
        if (!is_show_close_button) close_button.setVisibility(View.GONE);
        return this;
    }

    /**
     * 获取消息布局，为了自定义。
     *
     * @return
     */
    public LinearLayout getMessageContent() {
        return msg_content;
    }

    public LinearLayout getButtonContent() {
        return button_content;
    }

    public ProgressBar getProgressBar() {
        return progress_bar;
    }

    public LinearLayout getProgressContent() {
        return progress_content;
    }

    /**
     * 获取消息布局中心。
     *
     * @return LinearLayout 消息布局中心，不包括按钮。
     */
    public LinearLayout getMessageCenter() {
        return msg_content_center;
    }

    public Button getInstallButton() {
        return install_but;
    }

    public Button getSubmitButton() {
        return submit;
    }

    public Button getCancelButton() {
        return cancel;
    }

    public View getRootView() {
        return custom_view;
    }

    /**
     * 设置确认按钮
     *
     * @param submit_text 确认按钮文本
     * @param is_selected 是否为激活状态
     */

    public CommonDialog setSubmit(String submit_text, boolean is_selected) {
        submit.setVisibility(View.VISIBLE);
        submit.setText(submit_text);
        if (is_selected) {
            submit.setBackgroundResource(R.drawable.common_dialog_submit_act);
            submit.setTag(ACTIVE);
            submit.setTextColor(Color.parseColor("#FFFFFF"));
        }
        return this;
    }

    /**
     * 设置取消按钮
     *
     * @param cancel_text 取消按钮文本
     * @param is_selected 是否为激活状态
     */
    public CommonDialog setCancelButton(String cancel_text, boolean is_selected) {
        cancel.setVisibility(View.VISIBLE);
        cancel.setText(cancel_text);
        if (is_selected) {
            cancel.setBackgroundResource(R.drawable.common_dialog_submit_act);
            cancel.setTag(ACTIVE);
            cancel.setTextColor(Color.parseColor("#FFFFFF"));
        }
        return this;
    }

    public CommonDialog setListener(CommonDialogListener listener) {
        commonDialogListener = listener;
        return this;
    }

    public void dismiss() {
        if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
    }

    public interface CommonDialogListener {
        void OnSubmit(AlertDialog dialog);

        void OnCancel(AlertDialog dialog);

        void OnDismiss();

        void OnClose(AlertDialog dialog);
    }
}