package com.android.jdhshop.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.TimesQgAdapter;
import com.android.jdhshop.adapter.TqgAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.TaobaoGuestBean;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.utils.DateUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class PointRobbingActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recy_times)
    RecyclerView recyTimes;
    private int page = 1;
    List<TaobaoGuestBean.TaobaoGuesChildtBean> taobaoGuesChildtBeans = new ArrayList<>();
    private TqgAdapter shopRecyclerAdapter;
    List<String> list = new ArrayList<>(Arrays.asList("00:00", "08:00", "10:00", "12:00", "14:00", "16:00", "18:00", "20:00", "22:00"));
    private TimesQgAdapter qgAdapter;
    private int position = 0;
    private TextView txtOne, txtTwo;
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_point_robbing);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("淘抢购");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        shopRecyclerAdapter = new TqgAdapter(this, R.layout.tqg_item, taobaoGuesChildtBeans);
        recyclerView.setAdapter(shopRecyclerAdapter);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyTimes.setLayoutManager(linearLayoutManager);
        qgAdapter = new TimesQgAdapter(this, R.layout.items_time, list);
        recyTimes.setAdapter(qgAdapter);
        qgAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position1) {
                if (txtTwo != null && txtOne != null&&position!=qgAdapter.getPosition()) {
                    txtOne.setTextColor(getResources().getColor(R.color.light1_gray));
                    txtTwo.setTextColor(getResources().getColor(R.color.light1_gray));
                }
                txtOne = holder.itemView.findViewById(R.id.txt_time);
                txtOne.setTextColor(getResources().getColor(R.color.white));
                txtTwo = holder.itemView.findViewById(R.id.txt_status);
                txtTwo.setTextColor(getResources().getColor(R.color.white));
                position = position1;
                refreshLayout.autoRefresh();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        for (int i = 0; i < list.size(); i++) {
            int HH = calendar.get(Calendar.HOUR_OF_DAY);
            String current = list.get(i).split(":")[0];
            if (current.startsWith("0") && !current.startsWith("00"))
                current = current.replace("0", "");
            int temp = Integer.valueOf(current);
            if ((HH - 2) >= temp) {
            } else if (temp >HH) {
            } else {
                position = i;
                break;
            }
        }
        MoveToPosition(linearLayoutManager, recyTimes, position);
        refreshLayout.autoRefresh();
    }

    public static void MoveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {
        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }
    }

    @Override
    protected void initListener() {
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getList();
            }
        });
    }
    private void getList() {
//
//        String time=System.currentTimeMillis()+"";
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("method","taobao.tbk.ju.tqg.get");
//        requestParams.put("app_key","25899445");
//        requestParams.put("sign_method","md5");
//        requestParams.put("timestamp",time);
//        requestParams.put("v","2.0");
//        requestParams.put("adzone_id","mm_351650074_400650268_107322750398");
//        requestParams.put("fields","click_url,pic_url,reserve_price,zk_final_price,total_amount,sold_num,title,category_name,start_time,end_time");
//        String time1,time2;
//        if (position == list.size() - 1) {
//            time1=DateUtils.format_yyyy_MM_dd(new Date()) + " " + list.get(position) + ":00";
//            time2= DateUtils.format_yyyy_MM_dd(new Date()) + " " + "24:00:00";
//        } else {
//            time1=DateUtils.format_yyyy_MM_dd(new Date()) + " " + list.get(position) + ":00";
//            time2=DateUtils.format_yyyy_MM_dd(new Date()) + " " + list.get(position + 1) + ":00";
//        }
//        requestParams.put("start_time",time1);
//        requestParams.put("end_time",time2);
//        requestParams.put("page_no",page);
//        requestParams.put("page_size","10");
//        Map<String,Object> temp=new HashMap<>();
//        temp.put("method","taobao.tbk.ju.tqg.get");
//        temp.put("app_key","25899445");
//        temp.put("sign_method","md5");
//        temp.put("timestamp",time);
//        temp.put("v","2.0");
//        temp.put("adzone_id","mm_351650074_400650268_107322750398");
//        temp.put("fields","click_url,pic_url,reserve_price,zk_final_price,total_amount,sold_num,title,category_name,start_time,end_time");
//        temp.put("start_time",time1);
//        temp.put("end_time",time2);
//        temp.put("page_no",page);
//        temp.put("page_size","10");
//        String sign=MySecurity.getSign3(temp);
//        requestParams.put("sign",sign);
        RequestParams requestParams = new RequestParams();
        requestParams.put("page_no", page);
        requestParams.put("page_size",4);
        if (position == list.size() - 1) {
            requestParams.put("start_time", DateUtils.format_yyyy_MM_dd(new Date()) + " " + list.get(position) + ":00");
            requestParams.put("end_time", DateUtils.format_yyyy_MM_dd(new Date()) + " " + "24:00:00");
        } else {
            requestParams.put("start_time", DateUtils.format_yyyy_MM_dd(new Date()) + " " + list.get(position) + ":00");
            requestParams.put("end_time", DateUtils.format_yyyy_MM_dd(new Date()) + " " + list.get(position + 1) + ":00");
        }
//        HttpUtils.get("http://gw.api.taobao.com/router/rest", requestParams, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                LogUtils.d("sdfsd",responseString);
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                LogUtils.d("sdfsd",responseString);
//            }
//        });
        HttpUtils.post(Constants.GET_TQG, PointRobbingActivity.this,requestParams, new onOKJsonHttpResponseHandler<TaobaoGuestBean>(new TypeToken<Response<TaobaoGuestBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onSuccess(int statusCode, Response<TaobaoGuestBean> datas) {
                if (datas.isSuccess()) {
                    if (datas.getData().getList() == null) {
                        return;
                    }
                    if (page == 1) {
                        taobaoGuesChildtBeans.clear();
                    }
                    taobaoGuesChildtBeans.addAll(datas.getData().getList());
                    if (refreshLayout != null) {
                        if (page == 1) {
                            refreshLayout.finishRefresh();
                        } else {
                            refreshLayout.finishLoadMore();
                        }
                    }
                    if (datas.getData().getList().size() <= 0 && page > 1) {
                        ToastUtils.showShortToast(PointRobbingActivity.this, "没有更多数据");
                        page--;
                    }
                } else {
                    showToast(datas.getMsg());
                }

                shopRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.tv_left)
    public void onViewClicked() {
        finish();
    }
}
