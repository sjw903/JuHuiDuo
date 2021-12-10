package com.android.jdhshop.juduohui;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.UserHomePageBean;
import com.android.jdhshop.bean.UserInfoBean;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.juduohui.fragment.HomePageOtherFragment;
import com.android.jdhshop.juduohui.fragment.HomePagePersonalFragment;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 个人主页
 */
public class JuduohuiHomePageActivity extends BaseActivity {

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_juduohui_homepage);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        loadData();
    }

    @Override
    protected void initListener() {

    }

    private void loadData() {
        String auth_code = getIntent().getStringExtra("auth_code");
        RequestParams params = new RequestParams();
        params.put("auth_code", auth_code);
        HttpUtils.post(Constants.GET_USER_HOME, JuduohuiHomePageActivity.this, params, new HttpUtils.TextHttpResponseHandler() {
            @Override
            protected void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("JuduohuiHomePageActivity onSuccess responseString = " + responseString);
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    int code = jsonObject.optInt("code");
                    //返回码说明
                    String msg = jsonObject.optString("msg");
                    String data = jsonObject.optString("data");
                    if (0 == code) {
                        if (!TextUtils.isEmpty(data)) {
                            Gson gson = new Gson();
                            UserHomePageBean userBean = gson.fromJson(data.trim(), UserHomePageBean.class);
                            if (null != userBean) {
                                goTarget(userBean);
                            }
                        } else {
                            showToast(msg);
                        }
                    } else {
                        showToast(msg);
                    }
                } catch (JSONException e) {
                }
            }

            @Override
            protected void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }
        });
    }

    private void goTarget(UserHomePageBean userHomePageBean) {
        Fragment fragment = userHomePageBean.is_me ? HomePagePersonalFragment.newInstance(userHomePageBean) : HomePageOtherFragment.newInstance(userHomePageBean);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
    }
}