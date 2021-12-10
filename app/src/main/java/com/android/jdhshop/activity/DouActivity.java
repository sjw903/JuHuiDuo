package com.android.jdhshop.activity;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import com.android.jdhshop.R;
import com.android.jdhshop.adapter.DouAdpater;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.bean.HaoDanBean;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.google.gson.Gson;
import com.lmx.library.media.VideoPlayRecyclerView;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class DouActivity extends BaseActivity {
    @BindView(R.id.vedio_recy)
    VideoPlayRecyclerView vedioRecy;
    @BindView(R.id.back_im)
    ImageView back_im;
    private DouAdpater douAdpater;
    private String min_id = "1";
    private List<HaoDanBean> douList = new ArrayList<>();
    public static DouActivity activity;

    @Override
    protected void initUI() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_dou);
        activity = this;
        ButterKnife.bind(this);
        //本地资源文件获取图片
        BaseLogDZiYuan.LogDingZiYuan(back_im, "icon_back_while.png");
    }

    @Override
    protected void initData() {
        getData();
        findViewById(R.id.back_im).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (douAdpater != null)
            douAdpater.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (douAdpater != null)
            douAdpater.pause();
    }

    private void getData() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("back", 100);
        requestParams.put("min_id", min_id);
        requestParams.put("cat_id", getIntent().getExtras().getString("id"));
        HttpUtils.post(Constants.GET_DOU_LIST, DouActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(throwable.getMessage());
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject object = new JSONObject(responseString);
                    if ("1".equals(object.getString("code"))) {
                        JSONArray array = object.getJSONArray("data");
                        if (min_id.equals("1")) {
                            douList.clear();
                        }
                        min_id = object.getString("min_id");
                        for (int i = 0; i < array.length(); i++) {
                            douList.add(new Gson().fromJson(array.getJSONObject(i).toString(), HaoDanBean.class));
                        }
                        douAdpater = new DouAdpater(DouActivity.this, douList);
                        vedioRecy.setAdapter(douAdpater);
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    @Override
    protected void initListener() {
    }
}
