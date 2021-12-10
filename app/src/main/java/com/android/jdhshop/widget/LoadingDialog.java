package com.android.jdhshop.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.android.jdhshop.R;


/**
 * 与服务器交互加载提示框
 *
 */
public class LoadingDialog extends Dialog {

    private Context context = null;
    private static LoadingDialog customProgressDialog = null;

    public LoadingDialog(Context context) {
        super(context);
        this.context = context;
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    public static LoadingDialog createDialog(Context context) {
        customProgressDialog = new LoadingDialog(context, R.style.CustomProgressDialog);
        customProgressDialog.setContentView(R.layout.customer_loading_dialog);
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

        return customProgressDialog;
    }

    public void onWindowFocusChanged(boolean hasFocus) {

        if (customProgressDialog == null) {
            return;
        }
//        ImageView imageView = (ImageView) customProgressDialog
//                .findViewById(R.id.loadingImageView);
//        AnimationDrawable animationDrawable = (AnimationDrawable) imageView
//                .getBackground();
//        animationDrawable.start();
    }

    /**
     * [Summary] setTitile 标题
     * @param strTitle
     * @return
     */
    public LoadingDialog setTitile(String strTitle) {
        return customProgressDialog;
    }

    /**
     * [Summary] setMessage 提示内容
     * @param strMessage
     * @return
     */
    public LoadingDialog setMessage(String strMessage) {
        TextView tvMsg = (TextView) customProgressDialog.findViewById(R.id.tips_loading_msg);
        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }
        return customProgressDialog;
    }
}

