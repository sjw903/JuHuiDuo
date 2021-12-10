package com.android.jdhshop.activity;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.adapter.MyMarketAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.TeamListBean;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * @Params:我的市场
 * @开发者:陈飞
 * @日期:2018/10/25 15:38
 **/
public class MyMarketActivity extends BaseActivity {


    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.num_tv)
    TextView numTv;
    @BindView(R.id.yaoqingren_tv)
    TextView yaoqingrenTv;
    @BindView(R.id.one_person_tv)
    TextView onePersonTv;
    @BindView(R.id.two_person_tv)
    TextView twoPersonTv;
    @BindView(R.id.mListView)
    ListView mListView;

    private List<TeamListBean.Teamlist> teamlists = new ArrayList<>();
    private MyMarketAdapter myMarketAdapter;

    private int select = 1;
    private int page=1;
    private final String TAG = getClass().getSimpleName();
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_my_market);
        ButterKnife.bind(this);

    }

    @Override
    protected void initData() {

        tvLeft.setVisibility( View.VISIBLE);
        tvTitle.setText("我的市场");

        select =  getIntent().getIntExtra("level",1);
        selectView(select-1);

        getTeamList();
        myMarketAdapter = new MyMarketAdapter(getComeActivity(), R.layout.my_marke_item, teamlists);
        mListView.setAdapter(myMarketAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditText editText=new EditText(MyMarketActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(MyMarketActivity.this);
                builder.setTitle("输入好友昵称备注").setView(editText)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        submit(position,teamlists.get(position).getUid(), editText.getText().toString());
                    }
                });
                builder.show();
                }
        });

        refresh_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getTeamList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page=1;
                getTeamList();
            }
        });

    }
    private void submit(int pos,String uid,String remark){
        RequestParams requestParams = new RequestParams();
        requestParams.put("remark",remark);
        requestParams.put("fuid",uid);
        HttpUtils.post(Constants.setFriendRemark, MyMarketActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject object1=new JSONObject(responseString);
                    if("0".equals(object1.getString("code"))){
                        teamlists.get(pos).setName(remark);
                        myMarketAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void initListener() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post( Constants.APP_IP+"/api/User/getTeamStatistics2",MyMarketActivity.this, requestParams, new onOKJsonHttpResponseHandler<TeamListBean>( new TypeToken<Response<TeamListBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(throwable.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Response<TeamListBean> datas) {
                if (datas.isSuccess()) {
                    TeamListBean datasData = datas.getData();
                    if (datasData != null) {
                        numTv.setText(datasData.getReferrer_num());
//                        if(datasData.getEferrer_name() != null){
//                            yaoqingrenTv.setText("我的邀请人：" + datasData.getEferrer_name());
//                        }else{
//                            yaoqingrenTv.setText("");
//                        }
                        onePersonTv.setText(String.format("第一人市场\n%s人", datasData.getTeam_first()));
                        twoPersonTv.setText(String.format("第二人市场\n%s人", datasData.getTeam_second()));
                    }
                } else {

                }
            }

            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                closeLoadingDialog();
            }
        });
    }


    /**
     * @Params:获取用户团队信息
     * @开发者:陈飞
     * @日期:2018/10/25 15:59
     **/
    private void getTeamList() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("type",select);
        requestParams.put("p",page);
        requestParams.put("per","10");

        HttpUtils.post( Constants.APP_IP+"/api/User/getTeamList",MyMarketActivity.this, requestParams, new onOKJsonHttpResponseHandler<TeamListBean>( new TypeToken<Response<TeamListBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(throwable.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Response<TeamListBean> datas) {
                LogUtils.d(TAG, "onSuccess: 执行这里的");
                LogUtils.d(TAG, requestParams+"onSuccess: 执行这里的");

                if (datas.isSuccess()) {
                    TeamListBean datasData = datas.getData();
                    if (datasData != null) {
                        numTv.setText(datasData.getReferrer_num());

                        //判断为空时不显示该字段
                        if(datasData.getEferrer_name() != null){
                            yaoqingrenTv.setText("我的邀请人：" + datasData.getEferrer_name());
                        }else{
                            yaoqingrenTv.setVisibility(View.GONE);
                        }
                        onePersonTv.setText(String.format("第一人市场\n%s人", datasData.getTeam_first()));
                        twoPersonTv.setText(String.format("第二人市场\n%s人", datasData.getTeam_second()));
                        if(page==1)
                            teamlists.clear();
                            teamlists.addAll(datasData.teamlist);

//                        if (select == 1) {
//
//                        } else {
//                            teamlists.addAll(datasData.getTeamlist2());
//                        }

                    }

                } else {
                    showToast(datas.getMsg());
                }
                myMarketAdapter = new MyMarketAdapter(getComeActivity(), R.layout.my_marke_item, teamlists);
                //showToast(teamlists+"");

                mListView.setAdapter(myMarketAdapter);
                myMarketAdapter.notifyDataSetChanged();
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                refresh_layout.finishRefresh();
                refresh_layout.finishLoadMore();
            }
        });
    }


    @OnClick({R.id.tv_left, R.id.one_person_tv, R.id.two_person_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.one_person_tv:
                selectView(0);
                break;
            case R.id.two_person_tv:
                selectView(1);
                break;
        }
    }

    private void selectView(int position) {
        TextView[] textViews = {onePersonTv, twoPersonTv};
        for (TextView textView : textViews) {
            textView.setTextColor(getResources().getColor(R.color.black));
        }
        textViews[position].setTextColor(getResources().getColor(R.color.app_main_color));
        select = position + 1;
        page=1;
        //请求
        getTeamList();

    }
}
