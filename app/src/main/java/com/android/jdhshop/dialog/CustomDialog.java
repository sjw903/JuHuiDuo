package com.android.jdhshop.dialog;

import com.shehuan.nicedialog.NiceDialog;

/**
 * 基于NiceDialog,添加关于窗口关闭的回调
 */
public class CustomDialog extends NiceDialog {
    private DismissListener dismissListener;

    public void setDismissListener(DismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    public static CustomDialog init() {
        return new CustomDialog();
    }

    public interface DismissListener {
        void onDismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != dismissListener) {
            dismissListener.onDismiss();
        }
    }
}
