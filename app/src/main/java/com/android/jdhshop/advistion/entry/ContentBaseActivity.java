package com.android.jdhshop.advistion.entry;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.android.jdhshop.utils.AppImmersiveUtils;

public abstract class ContentBaseActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppImmersiveUtils.startImmersiveMode(this, Color.TRANSPARENT, isDarkImmersiveMode());
        setContentView(getLayoutId());
    }

    protected boolean isDarkImmersiveMode() {
        return false;
    }

    @LayoutRes
    protected abstract int getLayoutId();
}
