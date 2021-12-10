package com.android.jdhshop.ziyuan;
 
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import androidx.annotation.Nullable;


/**
 * 如果要成功加载外部资源包资源，需要继承ResourceBaseActivity
 */
public class ResourceBaseActivity extends AppCompatActivity {
 
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 必须在调用super.onCreate()方法前设置Factory2实例
        // 如果设置了LayoutInflater.Factory2实例，在调用AppCompatActivity.setContentView()时，
        // 系统会调用LayoutInflater.Factory2.onCreateView()来创建View，
        // 分离的资源就是在这个自定义的LayoutInflater.Factory2实例里实现动态加载并设置到View对应的属性上。
        getLayoutInflater().setFactory2(new ResourceFactory());
        super.onCreate(savedInstanceState);
    }
 
}