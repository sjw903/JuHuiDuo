package com.android.jdhshop.widget;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;


/**
 * @属性:图片选择器
 * @开发者:陈飞
 * @时间:2018/7/22 10:43
 */
public class ImageSelectDialog extends Dialog {

    private static final int PLAY_PHOTO = 0x0001;
    private static final int PLAY_LOAD_PHOTO = 0x0002;
    private static final int PHOTO_REQUEST_CUT = 0x0003;

    private BaseActivity activity;
    private Uri data1;

    public ImageSelectDialog(@NonNull Context context) {
        super(context);
        activity = (BaseActivity) context;
    }

    public ImageSelectDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        activity = (BaseActivity) context;
    }

    @Override
    protected void onStart() {
        super.onStart();
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(displayMetrics.widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_select_dialog);
        //取消
        findViewById(R.id.abroad_choose_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        //拍照
        findViewById(R.id.abroad_takephoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.photographRequest(activity, PLAY_PHOTO);
            }
        });

        /**
         * @属性:本地
         * @开发者:陈飞
         * @时间:2018/7/22 11:03
         */
        findViewById(R.id.abroad_choosephoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.selectPhoto(activity, PLAY_LOAD_PHOTO);
            }
        });

        activity.setOnActivityResultLisntener(new BaseActivity.onActivityResultLisntener() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                switch (requestCode) {
                    case PLAY_PHOTO: //拍照选择的照片
                        data1 = BaseActivity.uri;
                        if (data1 != null) {
                            if (listener != null) {
                                if(resultCode==0){
                                    listener.onImageSelectResult("");
                                    dismiss();
                                    return;
                                }
                                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                                Cursor cursor = activity.getContentResolver().query(data1, filePathColumn, null, null, null);
                                if (cursor != null) {
                                    cursor.moveToFirst();
                                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                    final String picturePath = cursor.getString(columnIndex);
                                    listener.onImageSelectResult(picturePath);
                                } else {
                                    listener.onImageSelectResult(data1.getPath());
                                }

                            }
                        }
                        dismiss();
                        break;
                    case PLAY_LOAD_PHOTO: //本地选择的照片
                        if (data != null) {
                            data1 = data.getData();
                            if (data1 != null) {
                                if (listener != null) {
                                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                                    Cursor cursor = activity.getContentResolver().query(data1, filePathColumn, null, null, null);
                                    cursor.moveToFirst();
                                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                    final String picturePath = cursor.getString(columnIndex);
                                    listener.onImageSelectResult(picturePath);
                                }
                            }
                        }

                        dismiss();
                        break;
                }
            }
        });
    }

    public interface onImageSelectDialogListener {
        void onImageSelectResult(String picturePath);
    }

    private onImageSelectDialogListener listener;

    /**
     * @属性:处理监听
     * @开发者:陈飞
     * @时间:2018/7/22 11:24
     */
    public ImageSelectDialog setOnImageSelectDialogListener(onImageSelectDialogListener listener) {
        this.listener = listener;
        return this;
    }


//    private void cropPhoto(Uri uri) {
//        // 裁剪图片意图
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        intent.putExtra("crop", "true");
//        // 裁剪框的比例，1：1
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        // 裁剪后输出图片的尺寸大小
//        intent.putExtra("outputX", 250);
//        intent.putExtra("outputY", 250);
//        intent.putExtra("outputFormat", "JPEG");// 图片格式
//        intent.putExtra("noFaceDetection", true);// 取消人脸识别
//        intent.putExtra("return-data", true);
//        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
//        activity.startActivityForResult(intent, PHOTO_REQUEST_CUT);
//    }
}
