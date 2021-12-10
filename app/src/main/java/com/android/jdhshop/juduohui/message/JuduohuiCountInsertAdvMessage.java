package com.android.jdhshop.juduohui.message;

import android.app.Activity;

public class JuduohuiCountInsertAdvMessage {
    Activity mActivity;
    int mAction = 0;
    public void setActivity(Activity activity){
        mActivity = activity;
    }
    public Activity getActivity(){ return mActivity; }
    public void setAction(int act){ mAction = act; }
    public int getAction(){ return mAction; }
}
