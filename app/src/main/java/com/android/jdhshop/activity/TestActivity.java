package com.android.jdhshop.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.jdhshop.R;

public class TestActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_welone);
        Toast.makeText(this,"sdfsd",Toast.LENGTH_SHORT).show();
    }
}
