package com.android.jdhshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.Image;

import java.util.ArrayList;

public class ImageListAdapter  extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;

//    private String[] imageUrls;
    ArrayList<Image> imageUrls;

    public ImageListAdapter(Context context, ArrayList<Image> imageUrls) {
        super(context, R.layout.selected_image_item, imageUrls);
        this.context = context;
        this.imageUrls = imageUrls;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = null;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.selected_image_item, parent, false);
            imageView=convertView.findViewById(R.id.iv_selected_image);
        }
        Glide.with(context)
                .load(imageUrls.get(position).getPath())
                .centerCrop()
                .into(imageView);
        return convertView;
    }
}
