package com.android.jdhshop.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.widget.AutoClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 申请订单
 * Created by yohn on 2018/7/27.
 */

public class ApplyOrderFragment extends BaseLazyFragment {
    public static final String TAG = "ApplyOrderFragment";
    @BindView(R.id.et_account)
    AutoClearEditText et_account;
    @BindView(R.id.tv_commit)
    TextView tv_commit;

    private View view;
    public static ApplyOrderFragment fragment;

    private ACache mAcache;
    String token;

    public static ApplyOrderFragment getInstance() {
        if (fragment == null) {
            fragment = new ApplyOrderFragment();
        }
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_apply, container, false);
        ButterKnife.bind(this, view);

        init();
        addListener();
        return view;
    }
    //初始化数据
    private void init() {
        mAcache = ACache.get(getActivity());
        token=mAcache.getAsString(Constants.TOKEN);
    }

    private void addListener() {
        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String order_num= et_account.getText().toString().trim();
                if(TextUtils.isEmpty(order_num)){
                    showToast("请输入订单号");
                    return;
                }
                getData(order_num);
            }
        });
    }
    /**
     * 申请淘宝订单奖
     * @param orderNum
     */
    private void getData(String orderNum) {
        if (!CommonUtils.isNetworkAvailable()) {
            showToast(getResources().getString(R.string.error_network));
            return;
        }

        RequestParams params = new RequestParams();
        params.put("token", token);
        params.put("order_num", orderNum);
        HttpUtils.post(Constants.ORDER_APPLY,ApplyOrderFragment.this, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // 输出错误信息
                LogUtils.e(TAG, "onFailure()--" + responseString);
            }

            @Override
            public void onFinish() {
                // 隐藏进度条
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                LogUtils.e(TAG, "onSuccess()--" + responseString);


                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    //返回码
                    int code = jsonObject.optInt("code");
                    //返回码说明
                    String msg = jsonObject.optString("msg");

                        showToast(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onStart() {
                // 显示进度条
                super.onStart();
                showLoadingDialog();
            }
        });

    }

    @Override
    protected void lazyload() {

    }
}
