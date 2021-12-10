package com.android.jdhshop.merchantactivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.merchantadapter.MerchantNewImgAdapter;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class MerchantNewDetailActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_time)
    TextView txtTime;
    @BindView(R.id.txt_num)
    TextView txtNum;
    @BindView(R.id.txt_content)
    TextView txtContent;
    @BindView(R.id.my_recy)
    RecyclerView my_recy;

    List<String> list=new ArrayList<>();
    MerchantNewImgAdapter adapter;
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_merchant_new_detail);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("详情内容");
        my_recy.setLayoutManager(new LinearLayoutManager(this));
        adapter=new MerchantNewImgAdapter(R.layout.item_img,list);
        my_recy.setAdapter(adapter);
        RequestParams requestParams = new RequestParams();
        requestParams.put("news_id", getIntent().getExtras().getString("news_id"));
        HttpUtils.post(Constants.APP_IP + "/api/MerchantNews/getMsg",MerchantNewDetailActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Log.e("msg", responseString);
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        txtName.setText(object.getJSONObject("data").getJSONObject("newsMsg").getString("title"));
                        txtTime.setText(object.getJSONObject("data").getJSONObject("newsMsg").getString("pubtime"));
                        txtNum.setText(object.getJSONObject("data").getJSONObject("newsMsg").getString("clicknum")+"阅读量");
                        txtContent.setText(object.getJSONObject("data").getJSONObject("newsMsg").getString("mob_text"));
                        String mob_img=object.getJSONObject("data").getJSONObject("newsMsg").getString("mob_img");
                        if(mob_img!=null&&mob_img.length()>10){
                            String[] ar=mob_img.replace("[","").replace("]","").replaceAll("\\\\","").replaceAll("\"","").split(",");
                            for(int i=0;i<ar.length;i++){
                                list.add(ar[i]);
                            }
                            adapter.notifyDataSetChanged();
                        }
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

    @OnClick(R.id.tv_left)
    public void onViewClicked() {
        finish();
    }
}
