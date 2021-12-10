package com.android.jdhshop.juduohui.unt;

import android.content.Context;

import com.android.jdhshop.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ImageLoad {

    public static void loadPlaceholder(Context context, String url, Target target) {

        Picasso picasso = new Picasso.Builder(context).loggingEnabled(true).build();
        picasso.load(url)
                .placeholder(R.drawable.no_banner)
                .error(R.drawable.no_banner)
                .transform(new CompressTransformation())
                .into(target);
    }

}