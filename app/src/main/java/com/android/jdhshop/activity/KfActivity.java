package com.android.jdhshop.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class KfActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.img_top)
    ImageView img_top;
    @BindView(R.id.wenan)
    ImageView wenan;
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_kf);
        ButterKnife.bind(this);

    }

    @Override
    protected void initData() {
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("我的客服");
        getWeiXin();

        BaseLogDZiYuan.LogDingZiYuan(img_top, "zhuangshi.png");
        BaseLogDZiYuan.LogDingZiYuan(wenan, "wenan.png");
    }

    private void getWeiXin(){
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.GET_KEFU,KfActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (getComeActivity().isDestroyed())
                    return;
                try {
                    if (new JSONObject(responseString).getInt("code") != 0) {
                        T.showShort(KfActivity.this, new JSONObject(responseString).getString("msg"));
                        return;
                    }
                    JSONObject object = new JSONObject(responseString).getJSONObject("data");
                    phone.setText(object.getString("weixin"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    protected void initListener() {

    }
    @OnClick({R.id.tv_left, R.id.btn_add_wx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.btn_add_wx:
                if(phone.getText().toString().equals("")){
                    T.showShort(this,"暂无客服微信");
                    return;
                }
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label",phone.getText().toString() );
                cm.setPrimaryClip(mClipData);
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                if(intent==null){
                    T.showShort(this,"您还未安装微信客户端");
                    return;
                }
                startActivity(intent);
                break;
        }
    }
}
