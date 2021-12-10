package com.android.jdhshop.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.ImageFolder;
import com.android.jdhshop.widget.recyclerview.CommonRecycleAdapter;
import com.android.jdhshop.widget.recyclerview.CommonViewHolder;

import java.util.List;

/**
 * Description:
 * Data：9/4/2018-3:14 PM
 *
 * @author yanzhiwen
 */
public class ImageFolderAdapter extends CommonRecycleAdapter<ImageFolder> {
    private Context mContext;

    public ImageFolderAdapter(Context context, List<ImageFolder> imageFolders, int layoutId) {
        super(context, imageFolders, layoutId);
        this.mContext = context;
    }

    @Override
    protected void convert(CommonViewHolder holder, ImageFolder imageFolder, int potion) {
        holder.setText(R.id.tv_folder_name, imageFolder.getName())
                .setText(R.id.tv_size, imageFolder.getImages().size() + "张");
        ImageView iv_folder = holder.getView(R.id.iv_folder);
        Glide.with(mContext)
                .load(imageFolder.getAlbumPath())
                .into(iv_folder);

    }
}
