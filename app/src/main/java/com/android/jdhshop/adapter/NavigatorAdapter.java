package com.android.jdhshop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.android.jdhshop.widget.indicator.buildins.commonnavigator.titles.SimplePagerTitleView;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : szp
 *     time   : 2020/05/13
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class NavigatorAdapter extends CommonNavigatorAdapter {
    String[] str; //title
    Context context;
    public NavigatorAdapter(Context context, String[] str) {
        this.str = str;
        this.context = context;
    }
    @Override  //返回title长度
    public int getCount() {
        return str.length;
    }
    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        SimplePagerTitleView titleView = new SimplePagerTitleView(context); //新建简单titleView
        titleView.setText(str[index]); //设置title内容
        titleView.setTextSize(14); //设置title字体大小
        titleView.setSelectedColor( Color.WHITE ); //设置选中时的title颜色
        titleView.setNormalColor(Color.parseColor( "#BCBCBC" )); //设置未选中时的title颜色
        return titleView;
    }
    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context); //新建指示条
        indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT); //设置指示条与内容同宽
        indicator.setRoundRadius(5); //设置指示条圆角度
        indicator.setColors(Color.WHITE); //设置指示条颜色（此方法可设置多个颜色）
        indicator.setStartInterpolator(new AccelerateDecelerateInterpolator( )); //设置指示条插值器
        return indicator;
    }
}


