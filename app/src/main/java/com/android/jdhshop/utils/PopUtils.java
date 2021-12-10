package com.android.jdhshop.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;


import com.android.jdhshop.R;
import com.android.jdhshop.adapter.KeyValueAdapter;
import com.android.jdhshop.bean.KeyValueBean;
import com.android.jdhshop.widget.popupwindow.CommonPopupWindow;

import java.util.List;

/**
 * 开发者：陈飞
 * 时间:2018/7/24 16:59
 * 说明：
 */
public class PopUtils {

    private static CommonPopupWindow popupWindow;

    private static KeyValueAdapter mOneKeyValueAdapter = null;
    private static KeyValueAdapter mTwoKeyValueAdapter = null;
    private static String mOnekeySelected = "0";


    public static void showDownPop(final Context context, View view, int height, final List<KeyValueBean> list, final onShowDropListener listener) {
        if (popupWindow != null && popupWindow.isShowing()) return;

        mOneKeyValueAdapter = new KeyValueAdapter(context);
        popupWindow = new CommonPopupWindow.Builder(context).setView(R.layout.popwindow_menu).setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, height)
//                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, CommonUtils.getScreenHeight()/2)
                .setAnimationStyle(R.style.AnimDown)
//                .setBackGroundLevel(0.8f) //设置背景颜色，取值范围0.0f-1.0f 值越小越暗 1.0f为透明
                .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                    @Override
                    public void getChildView(View view, int layoutResId) {
                        switch (layoutResId) {
                            case R.layout.popwindow_menu://刷选条件1，2
                                ListView lv_pop = view.findViewById(R.id.lv_pop);
                                lv_pop.setDivider(context.getResources().getDrawable(R.color.col_eb));//先设置Divider，再设置DividerHeight
                                lv_pop.setDividerHeight(1);

                                mOneKeyValueAdapter.setData(list);
                                mOneKeyValueAdapter.setKey(mOnekeySelected);
                                lv_pop.setAdapter(mOneKeyValueAdapter);

                                lv_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            setOpenPopupWindow(1,false);
                                        KeyValueBean k = (KeyValueBean) list.get(position);
//
                                        if (listener != null) {
                                            listener.onShowDropResult(position);
                                        }

//                             tv_menu_one.setText(k.value);
//                             if (position==1){
//                                 tv_menu_one.setText("客户等级");
//                             }
                                        mOnekeySelected = k.key;
//                            mMyCustomerRequest.sort = k.key;
                                        // LogUtils.d(TAG,"排序方式："+mOnekeySelected+"_"+k.value);
                                        // getData(true);//从第1页开始,刷新列表

                                        if (popupWindow != null) {
                                            popupWindow.dismiss();
                                        }
                                    }
                                });
                        }
                    }
                }).setOutsideTouchable(true).create();
        popupWindow.showAsDropDown(view);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                showToast("popupWindow 关闭啦");
//                setOpenPopupWindow(0,false);
            }
        });
    }


    public interface onShowDropListener {
        void onShowDropResult(int position);
    }
}
