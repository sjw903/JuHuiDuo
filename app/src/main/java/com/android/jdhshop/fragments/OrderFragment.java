package com.android.jdhshop.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseLazyFragment;

/**
 * @属性:订单
 * @开发者:陈飞
 * @时间:2018/7/23 15:43
 */
public class OrderFragment extends BaseLazyFragment {



    butterknife.Unbinder unbinder;


    public OrderFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        unbinder = butterknife.ButterKnife.bind(this, view);


        return view;
    }






    @Override
    protected void lazyload() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



}
