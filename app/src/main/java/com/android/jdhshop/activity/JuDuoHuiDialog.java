package com.android.jdhshop.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.jdhshop.R;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.common.LogUtils;

public class JuDuoHuiDialog extends AlertDialog {
    private final AlertDialog dialog;
    private final TextView tv_message;
    private final TextView tv_title;
    private final TextView tv_submit;
    private final ImageView tv_custom_icon;
//    private final View.OnClickListener dialog_onclick_listener;
    public JuDuoHuiDialog(@NonNull Context context, @NonNull int layout_id, View.OnClickListener listener) {
        super(context);
        View dialog_layout_inflater = getLayoutInflater().inflate(R.layout.dialog_juduohui, null);
        ViewGroup.LayoutParams layoutParams = dialog_layout_inflater.getLayoutParams();
        layoutParams.width = CommonUtils.getScreenWidth() -200;
        dialog_layout_inflater.setLayoutParams(layoutParams);
        tv_title = dialog_layout_inflater.findViewById(R.id.title);
        tv_message = dialog_layout_inflater.findViewById(R.id.message);
        tv_submit = dialog_layout_inflater.findViewById(R.id.tvSubmit);
        tv_custom_icon = dialog_layout_inflater.findViewById(R.id.custom_icon);
        tv_custom_icon.setScaleType(ImageView.ScaleType.FIT_XY);
        tv_custom_icon.setAdjustViewBounds(true);
        dialog = new AlertDialog.Builder(context).setView(dialog_layout_inflater).create();
//        dialog.setCanceledOnTouchOutside(false);
        tv_submit.setOnClickListener(v -> {
            if (listener!=null){
                listener.onClick(v);
            }
            dialog.dismiss();
        });
//        dialog_onclick_listener = listener;
    }

    public void setButton(String msg){
        tv_submit.setText(msg);
    }

    @Override
    public void setIcon(int resId) {
        //super.setIcon(resId);
        tv_custom_icon.setImageResource(resId);
        tv_custom_icon.setScaleType(ImageView.ScaleType.FIT_XY);
        tv_custom_icon.setAdjustViewBounds(true);
    }

    @Override
    public void setIcon(Drawable icon) {
        //super.setIcon(icon);
        tv_custom_icon.setImageDrawable(icon);
        tv_custom_icon.setScaleType(ImageView.ScaleType.FIT_XY);
        tv_custom_icon.setAdjustViewBounds(true);
    }

    @Override
    public void setButton(int whichButton, CharSequence text, DialogInterface.OnClickListener listener) {
        //super.setButton(whichButton, text, listener);
        LogUtils.d("TAG", "setButton: "+whichButton);
        LogUtils.d("TAG", "setButton: "+text);

    }

    @Override
    public void setTitle(CharSequence title) {
//        super.setTitle(title);
        tv_title.setText(title);
    }

    @Override
    public void setMessage(CharSequence message) {
//        super.setMessage(message);
        tv_message.setText(message);
    }

    @Override
    public void show() {
        //super.show();
        dialog.show();
        Window dialogWindow = dialog.getWindow();
        //dialogWindow.getDecorView().setPadding(0, 0, 0, 0);//去除边框
        dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ViewGroup.LayoutParams lm = dialogWindow.getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        dialogWindow.setLayout((int) (dm.widthPixels*0.5), lm.height);
    }
}
