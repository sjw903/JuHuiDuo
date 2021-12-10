package com.android.jdhshop.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.JuDuoHuiDialog;
import com.android.jdhshop.common.T;
import com.android.jdhshop.login.WelActivity;
import com.android.jdhshop.utils.BroadcastContants;
import com.android.jdhshop.utils.BroadcastManager;
import com.android.jdhshop.widget.LoadingDialog;

import java.io.Serializable;

import me.drakeet.materialdialog.MaterialDialog;


/**
 * Fragment懒加载，界面可见时才加载数据
 */
public abstract class BaseLazyFragment extends Fragment {

//    private final String mPageName = "BaseLazyFragment";

    public boolean isVisible;


    public Context context;
    public FrameLayout rootContainer;
    public View errorLayout;
    public Button bt_try;
    public static int width, height;


    //    private Toast toast = null;
    private LoadingDialog loadingDialog;

    protected boolean isViewInitiated;
    protected boolean isVisibleToUser;
    protected boolean isDataInitiated;

    public MaterialDialog mMaterialDialog;
    public final String TAG = getClass().getSimpleName();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
        mMaterialDialog = new MaterialDialog(getActivity());//初始化MaterialDialog
    }

    public boolean prepareFetchData() {
        return prepareFetchData(false);
    }

    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            onVisible();
            isDataInitiated = true;
            return true;
        }
        return false;
    }

    /**
     * @属性:优化后的懒加载方法
     * @开发者:陈飞
     * @时间:2018/7/28 21:43
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }

    /**
     * 该方法  在Fragment的onCreateView（）方法前就被调用了，如果在这个方法内部直接调用
     * lazyload()加载数据，就可能会造成空指针异常，因为视图还未创建
     */
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (getUserVisibleHint()) {
//            isVisible = true;
//            onVisible();
//        } else {
//            isVisible = false;
//            onInVisible();
//        }
//    }


    /**
     * Fragment可见时被调用
     */
    protected void onVisible() {
        lazyload();
    }

    /**
     * 加载数据
     */
    protected abstract void lazyload();

    /**
     * 界面不可见时被调用
     */
    protected void onInVisible() {
    }

    ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        createErrorLayout();

        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels; // 屏幕宽度（像素）
        height = metric.heightPixels;

    }

    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(mPageName);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        // 移除进度条和出错界面
//		spBar.showContent();
        rootContainer.removeView(errorLayout);
        BroadcastManager.getInstance(getActivity()).destroy(BroadcastContants.sendLoginMessage);
        super.onDestroy();
    }

    /************************* 创建自定义布局  *************************************/
    // 创建请求服务器数据失败时显示的页面
    private void createErrorLayout() {
        rootContainer = (FrameLayout) getActivity().findViewById(android.R.id.content);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置对其方式为：屏幕居中对其
        lp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        LayoutInflater inflater = (LayoutInflater) CaiNiaoApplication.getAppContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        errorLayout = inflater.inflate(R.layout.customer_error, null);
        bt_try = (Button) errorLayout.findViewById(R.id.btn_retry);
        rootContainer.addView(errorLayout, lp);
        errorLayout.setVisibility(View.GONE);
    }


    /************************  跳转 ( 可以加入转场动画) start *********************************/
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);


    }

    public void startActivityForResultBase(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);

    }

    /**
     * 通过类名启动Activity
     *
     * @param pClass
     */
    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }


    /**
     * 通过类名启动Activity，并且含有Bundle数据
     *
     * @param pClass
     * @param pBundle
     */
    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(context, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    /**
     * 通过类名启动Activity，并且Intent含有int数据
     *
     * @param pClass
     * @param name
     * @param value
     */
    protected void openActivity(Class<?> pClass, String name, int value) {
        Intent intent = new Intent(context, pClass);
        intent.putExtra(name, value);
        startActivity(intent);
    }

    /**
     * 通过Action启动Activity
     *
     * @param pAction
     */
    protected void openActivity(String pAction) {
        openActivity(pAction, null);
    }

    /**
     * 通过Action启动Activity，并且含有Bundle数据
     *
     * @param pAction
     * @param pBundle
     */
    protected void openActivity(String pAction, Bundle pBundle) {
        Intent intent = new Intent(pAction);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    /************************  跳转 ( 可以加入转场动画) end *********************************/

    /**
     * 显示圆形加载进度对话框
     */
    protected void showLoadingDialog() {
        if (loadingDialog == null && context != null) {
            loadingDialog = LoadingDialog.createDialog(context);
            loadingDialog.setMessage("正在加载..");
        }
        if (!getActivity().isFinishing()) {
            loadingDialog.show();
        }
    }

    /**
     * 显示圆形加载进度对话框
     */
    protected void showLoadingDialog(String msg) {
        if (loadingDialog == null && context != null) {
            loadingDialog = LoadingDialog.createDialog(context);
            if (!TextUtils.isEmpty(msg)) {
                loadingDialog.setMessage(msg);
            } else {
                loadingDialog.setMessage("正在加载..");
            }

        }
//            loadingDialog.show();

        //修正后代码
        if (!getActivity().isFinishing()) {
            loadingDialog.show();
        }

    }

    /**
     * 关闭进度对话框
     */
    protected void closeLoadingDialog() {
        try {
            if (!getActivity().isFinishing() && loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
                loadingDialog = null;
            }
        }
        catch (Exception ignored){}
    }


    public void showDuoHuiDialog(String title,String msg,String bt_msg,int logo_resid,View.OnClickListener listener){
        JuDuoHuiDialog duoHuiDialog = new JuDuoHuiDialog(getContext(), R.layout.dialog_juduohui, listener);
        duoHuiDialog.setIcon(logo_resid);
        duoHuiDialog.setMessage(msg);
        duoHuiDialog.setTitle(title);
        duoHuiDialog.setButton(bt_msg);
        duoHuiDialog.show();
    }
    /**
     * 对话框提示
     **/
    public void showTipDialog(String content) {

        if (mMaterialDialog == null) {
            mMaterialDialog = new MaterialDialog(getActivity());//实例化对话框
        }
        mMaterialDialog.setTitle("友情提示")
                .setMessage(content)
                .setPositiveButton("知道啦", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                })
                .setCanceledOnTouchOutside(true)
                .setOnDismissListener(
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                mMaterialDialog = null;
                            }
                        })
                .show();
    }

    public void showTipDialog(String title, Spanned content) {

        if (mMaterialDialog == null) {
            mMaterialDialog = new MaterialDialog(getActivity());//实例化对话框
        }
        mMaterialDialog.setTitle(title)
                .setMessage(content)
                .setPositiveButton("知道啦", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                })
                .setCanceledOnTouchOutside(true)
                .setOnDismissListener(
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                mMaterialDialog = null;
                            }
                        })
                .show();
    }


    public void showTipDialog2(String title, Spanned content, final BaseActivity.onClickListener listener, String text) {
        if (mMaterialDialog == null) {
            mMaterialDialog = new MaterialDialog(getContext());//实例化对话框
        }
        mMaterialDialog.setTitle(title)
                .setMessage(content)
                .setPositiveButton(text, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onClickSure();
                        }
                        mMaterialDialog.dismiss();
                    }
                })
                .setCanceledOnTouchOutside(true)
                .setOnDismissListener(
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                mMaterialDialog = null;
                            }
                        })
                .show();
    }
    /**
     * short吐司
     *
     * @param msg
     * @author NML
     */
    public void showToast(String msg) {
        if("用户不存在".equals(msg)){
            T.showShort(context, "登录信息已过期");
            openActivity(WelActivity.class);
        }else{
            T.showShortBottom(context, msg);
        }
    }

    /**
     * 设置文本图片
     */
    public void setViewDrawable(Context mContext, TextView v, int drawable) {
        v.setVisibility(View.VISIBLE);
        v.setPadding(40, 0, 40, 0);
        Drawable dra = mContext.getResources().getDrawable(drawable);
        dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
        v.setCompoundDrawables(null, null, dra, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        onReceiverBroadCastMessage();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMaterialDialog = null;
    }
    /**
     * @属性:接收广播是否登录消息
     * @开发者:陈飞
     * @时间:2018/7/21 14:33
     */
    private void onReceiverBroadCastMessage() {
        BroadcastManager.getInstance(getActivity()).addAction(new String[]{BroadcastContants.sendLoginMessage, BroadcastContants.sendUserMessage}, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BroadcastContants.sendLoginMessage.equals(action)) {
                    ReceiverIsLoginMessage();
                } else {
                    String result = intent.getStringExtra("result");
                    Serializable resultSear = intent.getSerializableExtra("result");
                    ReceiverBroadCastMessage(action, resultSear, intent);
                    ReceiverBroadCastMessage(action, result, resultSear, intent);
                }
            }
        });
    }
    /**
     * @属性:
     * @开发者:陈飞
     * @时间:2018/7/21 14:57
     */
    protected void ReceiverIsLoginMessage() {

    }


    /**
     * @属性:
     * @开发者:陈飞
     * @时间:2018/7/21 14:57
     */
    protected void ReceiverBroadCastMessage(String status, Serializable serializable, Intent intent) {

    }

    /**
     * @属性:
     * @开发者:陈飞
     * @时间:2018/7/21 14:57
     */
    protected void ReceiverBroadCastMessage(String status, String result, Serializable serializable, Intent intent) {

    }


    protected void setStatusBar(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getActivity().getWindow().getDecorView();
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getActivity().getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        if (color == Color.WHITE) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            if (isFlyme()) {
//                CommonUtils.setMeizuStatusBarDarkIcon(getActivity(), true);
//            } else if (isMIUI()) {
//                CommonUtils.setMiuiStatusBarDarkMode(getActivity(), true);
//            } else if (Build.MANUFACTURER.equalsIgnoreCase("OPPO")) {
//                CommonUtils.setOPPOStatusTextColor(true, getActivity());
//            } else {
//            }
        } else {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }
}
