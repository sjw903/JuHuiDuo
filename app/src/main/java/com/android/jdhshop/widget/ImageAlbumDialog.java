package com.android.jdhshop.widget;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.adapter.ImageAlbumAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @属性:相册
 * @开发者:陈飞
 * @时间:2018/7/27 16:05
 */
public class ImageAlbumDialog extends Dialog {


    @BindView(R.id.colose_btn)
    TextView coloseBtn;
    @BindView(R.id.image_gridview)
    GridView imageGridview;

    List<String> images = new ArrayList<>();
    private ImageAlbumAdapter imageAlbumAdapter;

    public ImageAlbumDialog(@NonNull Context context) {
        super(context);
    }

    public ImageAlbumDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    /**
     * @属性:设置图片
     * @开发者:陈飞
     * @时间:2018/7/27 16:20
    */
    public void setImages(List<String> images) {
        this.images.clear();
        this.images.addAll(images);
        if (imageAlbumAdapter!=null) {
            imageAlbumAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_image_album_dialog);
        ButterKnife.bind(this);
        imageAlbumAdapter = new ImageAlbumAdapter(getContext(), R.layout.image_album_item, images);
        imageGridview.setAdapter(imageAlbumAdapter);
        imageAlbumAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.colose_btn)
    public void onViewClicked() {
        dismiss();
    }
}
