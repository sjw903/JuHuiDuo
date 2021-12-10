package com.android.jdhshop.merfragment;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.android.jdhshop.R;


/**
 * Created by wmm on 2019/2/25.
 * @version 1.0
 */
public class ZDYDialog extends Dialog {
    private String title = "";
    private String content = "";
    private String cancleStr = "取消";
    private String okStr = "确定";
    TextView txtTitle, txtContet, txtCancle, txtOk;
    private View.OnClickListener cancleListener, okListener;

    /**
     * @param title 标题
     * @param content 内容
     * @param cancleStr 取消文字
     * @param cancleClickListener 取消点击事件
     * @param okStr 确定文字
     * @param okClickListener 确定点击事件
      */
    public ZDYDialog(Context context, String title, String content, String cancleStr, View.OnClickListener cancleClickListener, String okStr, View.OnClickListener okClickListener) {
        super(context, R.style.MyDialogStyle);
        this.content = content;
        this.title = title;
        this.cancleStr = cancleStr;
        this.okStr = okStr;
        this.cancleListener = cancleClickListener;
        this.okListener = okClickListener;
        init();
    }

    /**
     *
     * @param context
     * @param cancleClickListener
     * @param okClickListener
     */
    public ZDYDialog(Context context, View.OnClickListener cancleClickListener, View.OnClickListener okClickListener) {
        super(context, R.style.MyDialogStyle);
        this.cancleListener = cancleClickListener;
        this.okListener = okClickListener;
        init();
    }

    /**
     *
     * @param context
     * @param title
     * @param content
     * @param cancleClickListener
     * @param okClickListener
     */
    public ZDYDialog(Context context, String title, String content, View.OnClickListener cancleClickListener, View.OnClickListener okClickListener) {
        super(context, R.style.MyDialogStyle);
        this.content = content;
        this.title = title;
        this.cancleListener = cancleClickListener;
        this.okListener = okClickListener;
        init();
    }

    /**
     *
     */
    private void init() {
        setContentView(R.layout.zdy_dialog);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        txtTitle = findViewById(R.id.txt_title);
        txtTitle.setText(title);
        txtContet = findViewById(R.id.txt_message);
        txtContet.setText(content);
        txtCancle = findViewById(R.id.txt_cancle);
        txtCancle.setText(cancleStr);
        txtOk = findViewById(R.id.txt_ok);
        txtOk.setText(okStr);
        txtCancle.setOnClickListener(cancleListener);
        txtOk.setOnClickListener(okListener);
    }

    /**
     *  设置取消和确定、标题的颜色
     * @param color
     */
    private void setCancleTxtColor(int color) {
        txtCancle.setTextColor(color);
    }
    private void setOkTxtColor(int color) {
        txtOk.setTextColor(color);
    }
    private void seTitlekTxtColor(int color) {
        txtTitle.setTextColor(color);
    }
    /**
     *  设置对话框标题
     * @param title
     */
    public void setTitle(String title) {
        txtTitle.setText(title);
    }

    /**
     *设置对话框内容
     * @param content
     */
    public void setContent(String content) {
       txtContet.setText(content);
    }

    /**
     *  设置对话框取消文字
     * @param cancleStr
     */
    public void setCancleStr(String cancleStr) {
        txtCancle.setText(cancleStr);
    }

    /**
     *设置对话框确定文字
     * @param okStr
     */
    public void setOkStr(String okStr) {
        txtOk.setText(okStr);
    }
    @Override
    public void show() {//在要用到的地方调用这个方法
        super.show();
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }
    public void dismissDialog(ZDYDialog zdyDialog) {
        if (null == zdyDialog) {
            return;
        }
        zdyDialog.dismiss();
    }
}
