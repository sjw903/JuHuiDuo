package com.android.jdhshop.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.android.jdhshop.MainActivity;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.DaiTuiAiAdapter;
import com.android.jdhshop.adapter.LiShiTuiSongAdapter;
import com.android.jdhshop.adapter.XiTongDaiTuiAiAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.DaiTuiAiBean;
import com.android.jdhshop.bean.SetAiShowBean;
import com.android.jdhshop.bean.XiTongDaiTuiAiBean;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * 个性化设置页面
 */
public class SetAssistantActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_ai_sethuanying)
    TextView tv_ai_sethuanying;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_ai_settuiguang)
    TextView tv_ai_settuiguang;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.tv_title_yijiesuan)
    NiceSpinner tv_title_xiala;
    @BindView(R.id.tv_title_tui_youhui)
    NiceSpinner tv_title_tui_youhui;
    @BindView(R.id.tv_title_tui_yongjin)
    NiceSpinner tv_title_tui_yongjin;
    @BindView(R.id.pdd_switchbutton)
    Switch pdd_switchbutton;
    @BindView(R.id.jd_switchbutton)
    Switch jd_switchbutton;
    @BindView(R.id.tb_switchbutton)
    Switch tb_switchbutton;
    @BindView(R.id.recyclerView_stay)
    RecyclerView recyclerView_stay;
    @BindView(R.id.recyclerView_xitong)
    RecyclerView recyclerView_xitong;
    @BindView(R.id.recyclerView_history)
    RecyclerView recyclerView_history;
    @BindView(R.id.rg_type)
    RadioGroup rgType;


    public List<String> yijiesList = new ArrayList<>();
    public List<String> tui_youhuiq = new ArrayList<>();
    public List<String> tui_yongjin = new ArrayList<>();
    @BindView(R.id.tv_ai_settime)
    TextView tv_ai_settime;
    @BindView(R.id.welcome_status)
    TextView welcome_status;
    public int page = 1;
    private boolean hasdata = true;
    Gson gson = new Gson();
    private int product_type = 0;
    private int commission_type = 0;
    List<DaiTuiAiBean> daituiList = new ArrayList<>();
    List<DaiTuiAiBean> lishidaituilist = new ArrayList<>();
    List<SetAiShowBean> setAiShowBeans = new ArrayList<>();
    List<XiTongDaiTuiAiBean> xitongdaituiList = new ArrayList<>();
    public DaiTuiAiAdapter daiTuiAiAdapter;
    public XiTongDaiTuiAiAdapter xiTongDaiTuiAiAdapter;
    public LiShiTuiSongAdapter liShiTuiSongAdapter;

    public String qunid;
    public String welcome = "";
    public String robot_name = "";
    public String prols;
    public List<String> prolList = new LinkedList<String>();
    private String token;
    public List<String> showid;
    private List<NiceSpinner> spinnerList = new ArrayList<>();
    private final int result_for_welcome = 2;
    private final int result_for_send_time = 3;
    private final int result_from_tui_guang = 4;
    private com.alibaba.fastjson.JSONArray platform = new com.alibaba.fastjson.JSONArray();

    private Context mContext;
    private boolean switch_box_is_click = false;

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_set_assistant);
        ButterKnife.bind(this);
        tv_left.setVisibility(View.VISIBLE);
        tv_title.setText("个性化设置");
        mContext = this;
    }

    @Override
    protected void initData() {
//        setAiShowBeans.clear();
//        ShowQunXin();
        prolList.clear();
//        String zhuaqu = SPUtils.getStringData(this, "token", "");
//        Log.d("wwwwwww", zhuaqu + "");
//        yijiesList = new LinkedList<>(Arrays.asList("所有群"));
        tui_yongjin = new LinkedList<>(Arrays.asList("全部推送", "推送有佣金商品", "推送无佣金商品"));
        tui_youhuiq = new LinkedList<>(Arrays.asList("全部推送", "推送有券商品", "推送无券商品"));
//        tv_title_xiala.attachDataSource(yijiesList);
        tv_title_xiala.setGravity(Gravity.CENTER | Gravity.RIGHT);
        //showToast(tv_title_xiala.getText().toString());
        tv_title_tui_youhui.attachDataSource(tui_youhuiq);
        tv_title_tui_yongjin.attachDataSource(tui_yongjin);



        spinnerList.add(tv_title_xiala);
        spinnerList.add(tv_title_tui_yongjin);
        spinnerList.add(tv_title_tui_youhui);

        for(NiceSpinner nsp : spinnerList){
            nsp.setArrowDrawable(R.drawable.ic_arrow_drop_down_black_24dp);
        }

        /* platform = [1,2] 1拼多多，2京东，3淘宝.. */

        /* 获取线上群列表数据 */
        getAiTalkGroupSetting(tv_title_xiala.getSelectedIndex());
        /* 页面默认不加载待推送及历史推送 */
        smartRefreshLayout.setVisibility(View.GONE);
        rgType.setVisibility(View.GONE);

        /* 选择要设置的群聊选择事件监听 */
        tv_title_xiala.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                com.alibaba.fastjson.JSONObject item_info = talk_group_list.getJSONObject(position);
                setDisplayStatus(item_info);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        /* 拼多多商品推送点击切换状态推送 */
        pdd_switchbutton.setOnCheckedChangeListener(new SwitchBoxCheckerListener());
        /* 京东商品推送点击切换状态推送 */
        jd_switchbutton.setOnCheckedChangeListener(new SwitchBoxCheckerListener());
        /* 进群欢迎语点击进入 */
        tv_ai_sethuanying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log.d(TAG, "入群欢迎语: " + talk_group_list.getString(tv_title_xiala.getSelectedIndex()));
                Intent welcome_intent = new Intent(mContext,SetHuanYingYuActivity.class);
                welcome_intent.putExtra("item_info",talk_group_list.getString(tv_title_xiala.getSelectedIndex()));
                startActivityForResult(welcome_intent,result_for_welcome);
            }
        });
        /* 发品商品时间 */
        tv_ai_settime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent send_time_intent = new Intent(mContext,AiSetTimeActivity.class);
                send_time_intent.putExtra("item_info",talk_group_list.getString(tv_title_xiala.getSelectedIndex()));
                startActivityForResult(send_time_intent,result_for_send_time);
            }
        });
        /* 选择推送优惠券商品 */
        tv_title_tui_youhui.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RequestParams params = new RequestParams();
                params.put("product_type",position);
                setAiTalkGroupSetting(params);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        /* 选择推送佣金商品 */

        tv_title_tui_yongjin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RequestParams params = new RequestParams();
                params.put("commission_type",position);
                setAiTalkGroupSetting(params);
                }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        /* 推广设置 */
        tv_ai_settuiguang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tui_guang_intent = new Intent(mContext,AiSetExtensionActivity.class);
                tui_guang_intent.putExtra("item_info",talk_group_list.getString(tv_title_xiala.getSelectedIndex()));
                startActivityForResult(tui_guang_intent,result_from_tui_guang);
            }
        });
//        tv_title_xiala.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (yijiesList.get(position).equals("所有群")) {
//                    smartRefreshLayout.setVisibility(View.GONE);
//                    rgType.setVisibility(View.GONE);
//                    //拼多多商品推送选择
//                    pdd_switchbutton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(SwitchButton view, boolean isChecked) {
//                            if (view.isChecked()) {
//                                prols = "1";
//                                prolList.add("1");
//                            }else{
//                                prolList.remove("1");
//                            }
//                            if(tv_title_xiala.getText().toString().equals("所有群")){
//                                SetAi("" + "", product_type, commission_type, "",  "", "", "", "", "0", "", "", "");
//                            }else{
//                                SetAi(setAiShowBeans.get(0).id + "", product_type, commission_type, "", "", "", "", "", "1", "", "", "");
//                            }
//                        }
//                    });
//                    //京东商品推送
//                    jd_switchbutton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(SwitchButton view, boolean isChecked) {
//                            if (view.isChecked()) {
//                                prols = "2";
//                                prolList.add("2");
//                            }else{
//                                prolList.remove("2");
//                            }
//                            if(tv_title_xiala.getText().toString().equals("所有群")){
//                                SetAi("" + "", product_type, commission_type, "",  "", "", "", "", "0", "", "", "");
//                            }else{
//                                SetAi(setAiShowBeans.get(0).id + "", product_type, commission_type, "", "", "", "", "", "1", "", "", "");
//                            }
//                        }
//                    });
//                    tv_title_tui_yongjin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            String robot_name = SPUtils.getStringData(SetAssistantActivity.this, "robot_name", "");
//                            String welcome = SPUtils.getStringData(SetAssistantActivity.this, "welcome", "");
//                            String welcome_status = SPUtils.getStringData(SetAssistantActivity.this, "welcome_status", "");
//                            String start_timestamp = SPUtils.getStringData(SetAssistantActivity.this, "start_timestamp", "");
//                            String stop_timestamp = SPUtils.getStringData(SetAssistantActivity.this, "stop_timestamp", "");
//                            String interval_time = SPUtils.getStringData(SetAssistantActivity.this, "interval_time", "");
//                            if (tui_yongjin.get(position).equals("全部推送")) {
//                                // showToast("全部推送");
//                                commission_type = 0;
//                                if(tv_title_xiala.getText().toString().equals("所有群")){
//                                    SetAi("" + "", product_type, commission_type, "",  "", "", "", "", "0", "", "", "");
//                                }else{
//                                    SetAi(setAiShowBeans.get(0).id + "", product_type, commission_type, "", "", "", "", "", "1", "", "", "");
//                                }
//                            } else if (tui_yongjin.get(position).equals("推送有佣金商品")) {
//                                commission_type = 1;
//                                if(tv_title_xiala.getText().toString().equals("所有群")){
//                                    SetAi("" + "", product_type, commission_type, "",  "", "", "", "", "0", "", "", "");
//                                }else{
//                                    SetAi(setAiShowBeans.get(0).id + "", product_type, commission_type, "", "", "", "", "", "1", "", "", "");
//                                }
//                            } else if (tui_yongjin.get(position).equals("推送无佣金商品")) {
//                                commission_type = 2;
//                                if(tv_title_xiala.getText().toString().equals("所有群")){
//                                    SetAi("" + "", product_type, commission_type, "",  "", "", "", "", "0", "", "", "");
//                                }else{
//                                    SetAi(setAiShowBeans.get(0).id + "", product_type, commission_type, "", "", "", "", "", "1", "", "", "");
//                                }
//                            }
//
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {
//
//                        }
//                    });
//                    tv_title_tui_youhui.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            String robot_name = SPUtils.getStringData(SetAssistantActivity.this, "robot_name", "");
//                            String welcome = SPUtils.getStringData(SetAssistantActivity.this, "welcome", "");
//                            String welcome_status = SPUtils.getStringData(SetAssistantActivity.this, "welcome_status", "");
//                            String start_timestamp = SPUtils.getStringData(SetAssistantActivity.this, "start_timestamp", "");
//                            String stop_timestamp = SPUtils.getStringData(SetAssistantActivity.this, "stop_timestamp", "");
//                            String interval_time = SPUtils.getStringData(SetAssistantActivity.this, "interval_time", "");
//                            if (tui_youhuiq.get(position).equals("全部推送")) {
//                                //showToast("全部推送");
//                                product_type = 0;
//                                if(tv_title_xiala.getText().toString().equals("所有群")){
//                                    SetAi("" + "", product_type, commission_type, "",  "", "", "", "", "0", "", "", "");
//                                }else{
//                                    SetAi(setAiShowBeans.get(0).id + "", product_type, commission_type, "", "", "", "", "", "1", "", "", "");
//                                }
//                            } else if (tui_youhuiq.get(position).equals("推送有券商品")) {
//                                product_type = 1;
//                                if(tv_title_xiala.getText().toString().equals("所有群")){
//                                    SetAi("" + "", product_type, commission_type, "",  "", "", "", "", "0", "", "", "");
//                                }else{
//                                    SetAi(setAiShowBeans.get(0).id + "", product_type, commission_type, "", "", "", "", "", "1", "", "", "");
//                                }
//                            } else if (tui_youhuiq.get(position).equals("推送无券商品")) {
//                                product_type = 2;
//                                if(tv_title_xiala.getText().toString().equals("所有群")){
//                                    SetAi("" + "", product_type, commission_type, "",  "", "", "", "", "0", "", "", "");
//                                }else{
//                                    SetAi(setAiShowBeans.get(0).id + "", product_type, commission_type, "", "", "", "", "", "1", "", "", "");
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {
//
//                        }
//                    });
//                    //ShowQunXin();
//                } else {
//                    smartRefreshLayout.setVisibility(View.VISIBLE);
//                    rgType.setVisibility(View.VISIBLE);
//                    String robot_name = SPUtils.getStringData(SetAssistantActivity.this, "robot_name", "");
//                    String welcome = SPUtils.getStringData(SetAssistantActivity.this, "welcome", "");
//                    String welcome_status = SPUtils.getStringData(SetAssistantActivity.this, "welcome_status", "");
//                    String start_timestamp = SPUtils.getStringData(SetAssistantActivity.this, "start_timestamp", "");
//                    String stop_timestamp = SPUtils.getStringData(SetAssistantActivity.this, "stop_timestamp", "");
//                    String interval_time = SPUtils.getStringData(SetAssistantActivity.this, "interval_time", "");
//                    String desc_type = SPUtils.getStringData(SetAssistantActivity.this, "desc_type", "");
//                    String href_type = SPUtils.getStringData(SetAssistantActivity.this, "href_type", "");
//                    prolList.clear();
//                    if (pdd_switchbutton.isChecked()) {
//                        prolList.add("1");
//                    }
//                    if (jd_switchbutton.isChecked()) {
//                        prolList.add("2");
//                    }
//                    if (tb_switchbutton.isChecked()) {
//                        prolList.add("3");
//                    }
//                    Log.d("aaaaaaaaa", prolList + "");
//                    if (tv_title_xiala.getText().toString().equals("所有群")) {
//
//                    } else {
//                        SetAi(setAiShowBeans.get(0).id + "", product_type, commission_type, "", "", "", "", "", "1", "", "", "");
//                    }
//                    ShowQunXin();
//                    String groupid = SPUtils.getStringData(SetAssistantActivity.this, "id", "");
//                    //DaiTuiGuang(1,groupid);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

//        if (tv_title_xiala.getText().toString().equals("所有群")) {

//        }



//        tb_switchbutton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
//                if (view.isChecked()) {
//                    prols = "3";
//                }
//            }
//        });
//
//        tv_title_tui_youhui.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String robot_name = SPUtils.getStringData(SetAssistantActivity.this, "robot_name", "");
//                String welcome = SPUtils.getStringData(SetAssistantActivity.this, "welcome", "");
//                String welcome_status = SPUtils.getStringData(SetAssistantActivity.this, "welcome_status", "");
//                String start_timestamp = SPUtils.getStringData(SetAssistantActivity.this, "start_timestamp", "");
//                String stop_timestamp = SPUtils.getStringData(SetAssistantActivity.this, "stop_timestamp", "");
//                String interval_time = SPUtils.getStringData(SetAssistantActivity.this, "interval_time", "");
//                if (tui_youhuiq.get(position).equals("全部推送")) {
//                    //showToast("全部推送");
//                    product_type = 0;
//                    SetAi(setAiShowBeans.get(0).id + "", product_type, commission_type, "0", "0", start_timestamp + "", stop_timestamp + "", interval_time + "", "1", robot_name, welcome_status, welcome);
//                } else if (tui_youhuiq.get(position).equals("推送有券商品")) {
//                    product_type = 1;
//                    SetAi(setAiShowBeans.get(0).id + "", product_type, commission_type, "0", "0", start_timestamp + "", stop_timestamp + "", interval_time + "", "1", robot_name, welcome_status, welcome);
//                } else if (tui_youhuiq.get(position).equals("推送无券商品")) {
//                    product_type = 2;
//                    SetAi(setAiShowBeans.get(0).id + "", product_type, commission_type, "0", "0", start_timestamp + "", stop_timestamp + "", interval_time + "", "1", robot_name, welcome_status, welcome);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        tv_title_tui_yongjin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String robot_name = SPUtils.getStringData(SetAssistantActivity.this, "robot_name", "");
//                String welcome = SPUtils.getStringData(SetAssistantActivity.this, "welcome", "");
//                String welcome_status = SPUtils.getStringData(SetAssistantActivity.this, "welcome_status", "");
//                String start_timestamp = SPUtils.getStringData(SetAssistantActivity.this, "start_timestamp", "");
//                String stop_timestamp = SPUtils.getStringData(SetAssistantActivity.this, "stop_timestamp", "");
//                String interval_time = SPUtils.getStringData(SetAssistantActivity.this, "interval_time", "");
//                if (tui_yongjin.get(position).equals("全部推送")) {
//                    // showToast("全部推送");
//                    commission_type = 0;
//                    SetAi(setAiShowBeans.get(0).id + "", product_type, commission_type, "0", "0", start_timestamp + "", stop_timestamp + "", interval_time + "", "1", robot_name, welcome_status, welcome);
//                } else if (tui_yongjin.get(position).equals("推送有佣金商品")) {
//                    commission_type = 1;
//                    SetAi(setAiShowBeans.get(0).id + "", product_type, commission_type, "0", "0", start_timestamp + "", stop_timestamp + "", interval_time + "", "1", robot_name, welcome_status, welcome);
//                } else if (tui_yongjin.get(position).equals("推送无佣金商品")) {
//                    commission_type = 2;
//                    SetAi(setAiShowBeans.get(0).id + "", product_type, commission_type, "0", "0", start_timestamp + "", stop_timestamp + "", interval_time + "", "1", robot_name, welcome_status, welcome);
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        //开始刷新
        //smartRefreshLayout.autoRefresh();
        //管理器
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView_stay.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_history.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_xitong.setLayoutManager(layoutManager1);

        daiTuiAiAdapter = new DaiTuiAiAdapter(this, R.layout.daituiai_item, daituiList, new ddd());
        liShiTuiSongAdapter = new LiShiTuiSongAdapter(this, R.layout.lishituisong_item, lishidaituilist);
        xiTongDaiTuiAiAdapter = new XiTongDaiTuiAiAdapter(this, R.layout.xitongdaituiai_item, xitongdaituiList, new ddd());
        daiTuiAiAdapter.notifyDataSetChanged();
        liShiTuiSongAdapter.notifyDataSetChanged();
        recyclerView_stay.setAdapter(daiTuiAiAdapter);
        recyclerView_history.setAdapter(liShiTuiSongAdapter);
        recyclerView_xitong.setAdapter(xiTongDaiTuiAiAdapter);

//        DaiTuiGuang(1);
//        LiShiGuang(1);
//        XiTongDaiTui(1);

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                if (recyclerView_stay.getVisibility() == View.VISIBLE) {
                    daituiList.clear();
                    xitongdaituiList.clear();
                    page = 1;
                    hasdata = true;
                    DaiTuiGuang(page);
                    if (daituiList.size() == 0) {
                        XiTongDaiTui(page);
                    }
                } else if (recyclerView_history.getVisibility() == View.VISIBLE) {
                    lishidaituilist.clear();
                    page = 1;
                    hasdata = true;
                    LiShiGuang(page);
                }
//                else if (recyclerView_xitong.getVisibility() == View.VISIBLE) {
//                    //xitongdaituiList.clear();
//                    page = 1;
//                    hasdata = true;
//                    XiTongDaiTui(page);
//                }
                //refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        recyclerView_stay.setNestedScrollingEnabled(false);
        recyclerView_stay.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //解决RecyclerView和smartRefreshLayout滑动冲突问题
                super.onScrolled(recyclerView, dx, dy);
                int topRowVerticalPosition =
                        (recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                smartRefreshLayout.setNestedScrollingEnabled(topRowVerticalPosition >= 0);

            }
        });
        recyclerView_xitong.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //解决RecyclerView和smartRefreshLayout滑动冲突问题
                super.onScrolled(recyclerView, dx, dy);
                int topRowVerticalPosition =
                        (recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                smartRefreshLayout.setNestedScrollingEnabled(topRowVerticalPosition >= 0);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                if (recyclerView_stay.getVisibility() == View.VISIBLE) {
                    if (hasdata) {
                        page++;
                        DaiTuiGuang(page);
                        XiTongDaiTui(page);
                    } else {
                        showToast("没有更多数据了");
                        //refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                    }
                } else if (recyclerView_history.getVisibility() == View.VISIBLE) {
                    if (hasdata) {
                        page++;
                        LiShiGuang(page);
                    } else {
                        showToast("没有更多数据了");
                        //refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                    }
                }
            }
        });
    }

    class ddd implements ViewGroup.OnClickListener {
        @Override
        public void onClick(View v) {
            smartRefreshLayout.autoRefresh();
            smartRefreshLayout.finishRefresh();
            //DaiTuiGuang(page,daituiList.get(0).id);
        }
    }

    // plat_form [1,2]转换成json数组对象
    private com.alibaba.fastjson.JSONArray getPlatform(String platform_string){
        com.alibaba.fastjson.JSONArray return_json = new com.alibaba.fastjson.JSONArray();
        String[] tmp_string  = platform_string.replace("[","").replace("]","").split(",");
        for (String s:tmp_string){
            return_json.add(Integer.parseInt(s.trim()));
        }
        return return_json;
    }
    private void setDisplayStatus(com.alibaba.fastjson.JSONObject item_info){
        // 逐个设置状态
        pdd_switchbutton.setChecked(item_info.getString("platform").contains("1"));
        jd_switchbutton.setChecked(item_info.getString("platform").contains("2"));

        welcome_status.setText(item_info.getIntValue("welcome_status") == 1 ? "开启" : "关闭");
        tv_title_tui_yongjin.setSelectedIndex(item_info.getIntValue("commission_type"));
        tv_title_tui_youhui.setSelectedIndex(item_info.getIntValue("product_type"));
        if (item_info.getIntValue("group_type") != 0){
            // 加载推广商品列表及推送历史列表
            smartRefreshLayout.setVisibility(View.VISIBLE);
            rgType.setVisibility(View.VISIBLE);
            DaiTuiGuang(page);
            XiTongDaiTui(1);
            LiShiGuang(1);
        }
        else
        {
            smartRefreshLayout.setVisibility(View.GONE);
            rgType.setVisibility(View.GONE);
        }
    }
    /**
     * 获取群线上设定
     */
    private com.alibaba.fastjson.JSONArray talk_group_list;
    private void getAiTalkGroupSetting(int current_select_index){
        RequestParams request_params = new RequestParams();
        HttpUtils.post(Constants.GETai_assistan_set, SetAssistantActivity.this,request_params, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog("正在加载群个性化设置信息...");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showTipDialog("群信息获取失败，请稍后再试或联系客服。");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                com.alibaba.fastjson.JSONObject response_json = com.alibaba.fastjson.JSONObject.parseObject(responseString);
                if (response_json.getIntValue("code") == 0){
                    talk_group_list = response_json.getJSONArray("list");
                    yijiesList.clear();

                    if (talk_group_list.size()>0){
                        for(Object item : talk_group_list){
                            yijiesList.add(((com.alibaba.fastjson.JSONObject) item).getString("name"));
                        }
                        tv_title_xiala.attachDataSource(yijiesList);
                        tv_title_xiala.setSelectedIndex(current_select_index);
                        pdd_switchbutton.setChecked(talk_group_list.getJSONObject(tv_title_xiala.getSelectedIndex()).getString("platform").contains("1"));
                        jd_switchbutton.setChecked(talk_group_list.getJSONObject(tv_title_xiala.getSelectedIndex()).getString("platform").contains("2"));
                        // Log.d(TAG, "onSuccess: 设置： 优惠券商品类型："+ talk_group_list.getJSONObject(tv_title_xiala.getSelectedIndex()).getIntValue("product_type") );
                        // Log.d(TAG, "onSuccess: 设置： 佣金商品类型："+ talk_group_list.getJSONObject(tv_title_xiala.getSelectedIndex()).getIntValue("product_type") );
                        tv_title_tui_youhui.setSelectedIndex(talk_group_list.getJSONObject(tv_title_xiala.getSelectedIndex()).getIntValue("product_type"));
                        tv_title_tui_yongjin.setSelectedIndex(talk_group_list.getJSONObject(tv_title_xiala.getSelectedIndex()).getIntValue("commission_type"));
                        if (talk_group_list.getJSONObject(tv_title_xiala.getSelectedIndex()).getBooleanValue("welcome_status")) {
                            welcome_status.setText("开启");
                        }
                        else
                        {
                            welcome_status.setText("关闭");
                        }
                    }
                    else{
                        // 还没有群呢，进来干什么
                    }
                }
                else{
                    showTipDialog(response_json.getString("msg"));
                }
            }

        });
    }
    private void setAiTalkGroupSetting(RequestParams request_params){
        // Log.d(TAG, "更新线上数据: " + request_params.toString());
        com.alibaba.fastjson.JSONObject update_item = talk_group_list.getJSONObject(tv_title_xiala.getSelectedIndex());
        request_params.put("group_id",update_item.getString("id"));
        request_params.put("group_type",update_item.getString("group_type"));

        HttpUtils.post(Constants.getSETAI, SetAssistantActivity.this,request_params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // Log.d(TAG, "onFailure: " + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                // Log.d(TAG, "onSuccess: " + responseString);
                postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        getAiTalkGroupSetting(tv_title_xiala.getSelectedIndex());
                    }
                });
            }
        });
    }
    //个性化设置显示接口请求
    private void ShowQunXin() {
        RequestParams params = new RequestParams();
        HttpUtils.post(Constants.GETai_assistan_set, SetAssistantActivity.this,params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    if (yijiesList!=null) yijiesList.clear();
                    //setAiShowBeans.clear();
                    String s = tv_title_xiala.getText().toString();
                    JSONObject jsonObject = new JSONObject(responseString);
                    JSONArray list = jsonObject.getJSONArray("list");
                    //yijiesList.add("所有群");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject obj = (JSONObject) list.get(i);
                        SetAiShowBean setAiShowBean = new SetAiShowBean();
                        setAiShowBean.name = obj.getString("name");
//                        setAiShowBean.user_id = obj.getString("user_id");
//                        setAiShowBean.id = obj.getString("id");
//                        setAiShowBean.welcome = obj.getString("welcome");
//                        welcome = obj.getString("welcome") + "";
//                        setAiShowBean.welcome_status = obj.getString("welcome_status");
//                        setAiShowBean.robot_name = obj.getString("robot_name");
//                        robot_name = obj.getString("robot_name") + "";
//                        setAiShowBean.welcome_status = obj.getString("welcome_status");
//                        setAiShowBean.start_timestamp = obj.getString("start_timestamp");
//                        setAiShowBean.stop_timestamp = obj.getString("stop_timestamp");
//                        setAiShowBean.interval_time = obj.getString("interval_time");
//                        setAiShowBean.product_type = obj.getString("product_type");
//                        setAiShowBean.commission_type = obj.getString("commission_type");
//                        setAiShowBean.desc_type = obj.getString("desc_type");
//                        setAiShowBean.href_type = obj.getString("href_type");
//                        setAiShowBean.group_type = obj.getString("group_type");
//                        setAiShowBean.platform =obj.getString("platform");
//                        setAiShowBeans.add(setAiShowBean);
                        yijiesList.add(obj.getString("name"));
                        if (s.equals(obj.getString("name"))) {
                            setAiShowBeans.clear();
                            setAiShowBean.user_id = obj.getString("user_id");
                            setAiShowBean.id = obj.getString("id");
                            SPUtils.saveStringData(SetAssistantActivity.this, "id", obj.getString("id"));
                            setAiShowBean.welcome = obj.getString("welcome");
                            welcome = obj.getString("welcome") + "";
                            setAiShowBean.welcome_status = obj.getString("welcome_status");
                            setAiShowBean.robot_name = obj.getString("robot_name");
                            robot_name = obj.getString("robot_name") + "";
                            setAiShowBean.welcome_status = obj.getString("welcome_status");
                            setAiShowBean.start_timestamp = obj.getString("start_timestamp");
                            setAiShowBean.stop_timestamp = obj.getString("stop_timestamp");
                            setAiShowBean.interval_time = obj.getString("interval_time");
                            setAiShowBean.product_type = obj.getString("product_type");
                            setAiShowBean.commission_type = obj.getString("commission_type");
                            setAiShowBean.desc_type = obj.getString("desc_type");
                            setAiShowBean.href_type = obj.getString("href_type");
                            setAiShowBean.group_type = obj.getString("group_type");
                            setAiShowBean.platform = obj.getString("platform");
                            SPUtils.saveStringData(SetAssistantActivity.this, "robot_name", obj.getString("robot_name"));
                            SPUtils.saveStringData(SetAssistantActivity.this, "welcome", obj.getString("welcome"));
                            SPUtils.saveStringData(SetAssistantActivity.this, "welcome_status", obj.getString("welcome_status"));
                            SPUtils.saveStringData(SetAssistantActivity.this, "start_timestamp", obj.getString("start_timestamp"));
                            SPUtils.saveStringData(SetAssistantActivity.this, "stop_timestamp", obj.getString("stop_timestamp"));
                            SPUtils.saveStringData(SetAssistantActivity.this, "interval_time", obj.getString("interval_time"));
                            SPUtils.saveStringData(SetAssistantActivity.this, "desc_type", obj.getString("desc_type"));
                            SPUtils.saveStringData(SetAssistantActivity.this, "href_type", obj.getString("href_type"));
                            String platform = obj.getString("platform");

                            pdd_switchbutton.setChecked(platform.contains("1"));
                            jd_switchbutton.setChecked(platform.contains("2"));

                            if (obj.getString("welcome_status").equals("0")) {
                                welcome_status.setText("未开启");
                            } else {
                                welcome_status.setText("已开启");
                            }
                            if (obj.getString("product_type").equals("0")) {
                                tui_youhuiq.clear();
                                tui_youhuiq = new LinkedList<>(Arrays.asList("全部推送", "推送有券商品", "推送无券商品"));
                                tv_title_tui_youhui.attachDataSource(tui_youhuiq);
                                product_type = 0;
                            } else if (obj.getString("product_type").equals("1")) {
                                tui_youhuiq.clear();
                                tui_youhuiq = new LinkedList<>(Arrays.asList("推送有券商品", "全部推送", "推送无券商品"));
                                product_type = 1;
                                tv_title_tui_youhui.attachDataSource(tui_youhuiq);
                            } else if (obj.getString("product_type").equals("2")) {
                                tui_youhuiq.clear();
                                tui_youhuiq = new LinkedList<>(Arrays.asList("推送无券商品", "全部推送", "推送有券商品"));
                                product_type = 2;
                                tv_title_tui_youhui.attachDataSource(tui_youhuiq);
                            }
                            if (obj.getString("commission_type").equals("0")) {
                                tui_yongjin.clear();
                                tui_yongjin = new LinkedList<>(Arrays.asList("全部推送", "推送有佣金商品", "推送无佣金商品"));
                                tv_title_tui_yongjin.attachDataSource(tui_yongjin);
                                commission_type = 0;
                            } else if (obj.getString("commission_type").equals("1")) {
                                tui_yongjin.clear();
                                tui_yongjin = new LinkedList<>(Arrays.asList("推送有佣金商品", "全部推送", "推送无佣金商品"));
                                commission_type = 1;
                                tv_title_tui_yongjin.attachDataSource(tui_yongjin);
                            } else if (obj.getString("commission_type").equals("2")) {
                                tui_yongjin.clear();
                                tui_yongjin = new LinkedList<>(Arrays.asList("推送无佣金商品", "全部推送", "推送有佣金商品"));
                                commission_type = 2;
                                tv_title_tui_yongjin.attachDataSource(tui_yongjin);
                            }
                            //DaiTuiGuang(1,obj.getString("id"));
                            smartRefreshLayout.autoRefresh();
                            setAiShowBeans.add(setAiShowBean);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //获取用户待推广商品
    private void DaiTuiGuang(int page) {
        com.alibaba.fastjson.JSONObject selected_item = talk_group_list.getJSONObject(tv_title_xiala.getSelectedIndex());

        RequestParams requestParams = new RequestParams();
        requestParams.put("talk_group_id", selected_item.getString("id"));
        requestParams.put("page", page);
        requestParams.put("pagesize", "15");
        HttpUtils.post(Constants.getYongHuDaiTui, SetAssistantActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(throwable.getMessage());
//                Log.e( "statuscode",search_content+","+ responseString+","+throwable.getMessage());
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("ssdaafsdf", responseString);

                try {
                    JSONObject object = new JSONObject(responseString);

                    if ("0".equals(object.getString("code"))) {
                        JSONObject data = object.getJSONObject("list");
                        JSONArray array = data.getJSONArray("data");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = (JSONObject) array.get(i);
                            DaiTuiAiBean daiTuiAiBean = new DaiTuiAiBean();
                            daiTuiAiBean.id = obj.getString("id");
                            daiTuiAiBean.user_id = obj.getString("user_id");
                            daiTuiAiBean.talk_group_id = obj.getString("talk_group_id");
                            daiTuiAiBean.add_timestamp = obj.getString("add_timestamp");
                            daiTuiAiBean.send_timestamp = obj.getString("send_timestamp");
//                            xuanpinkbean.small_images = obj.getString( "small_images" );
//                            xuanpinkbean.reserve_price = obj.getString( "reserve_price" );
                            try {
                                daiTuiAiBean.product_title = obj.getString("product_title");
                            } catch (JSONException e) {
                                daiTuiAiBean.product_title = "21";
                            }
                            try {
                                daiTuiAiBean.product_desc = obj.getString("product_desc");
                            } catch (JSONException e) {
                                daiTuiAiBean.product_desc = "1";
                            }
                            try {
                                daiTuiAiBean.product_sign_id = obj.getString("product_sign_id");
                            } catch (JSONException e) {
                                daiTuiAiBean.product_sign_id = "2";
                            }
                            try {
                                daiTuiAiBean.product_id = obj.getString("product_id");
                            } catch (JSONException e) {
                                daiTuiAiBean.product_id = "4";
                            }
                            daiTuiAiBean.platform_id = obj.getString("platform_id");
                            daiTuiAiBean.thumb_image_url = obj.getString("thumb_image_url");
                            daiTuiAiBean.org_price = obj.getString("org_price");
                            daiTuiAiBean.price = obj.getString("price");
                            daiTuiAiBean.cur_price = obj.getString("cur_price");
                            daiTuiAiBean.ticket_start_time = obj.getString("ticket_start_time");
                            daiTuiAiBean.ticket_end_time = obj.getString("ticket_end_time");
                            daiTuiAiBean.commission = obj.getString("commission");
                            daiTuiAiBean.linkurl = obj.getString("linkurl");
                            daiTuiAiBean.descurl = obj.getString("descurl");
                            daiTuiAiBean.status = obj.getString("status");
                            daiTuiAiBean.discount = obj.getString("discount");
                            daiTuiAiBean.fee_user_commission = obj.getString("fee_user_commission");
                            daiTuiAiBean.referrer_rate_commission = obj.getString("referrer_rate_commission");
                            daituiList.add(daiTuiAiBean);
                        }
                        daiTuiAiAdapter.notifyDataSetChanged();
                        if (smartRefreshLayout != null) {
                            smartRefreshLayout.finishRefresh();
                            smartRefreshLayout.finishLoadMore();
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

    //获取用户历史推广商品
    private void LiShiGuang(int page) {
        com.alibaba.fastjson.JSONObject selected_item = talk_group_list.getJSONObject(tv_title_xiala.getSelectedIndex());

        RequestParams requestParams = new RequestParams();
        requestParams.put("talk_group_id", selected_item.getString("id"));
        requestParams.put("page", page);
        requestParams.put("pagesize", "15");
        HttpUtils.post(Constants.getLiSHITUIGUANG,SetAssistantActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(throwable.getMessage());
//                Log.e( "statuscode",search_content+","+ responseString+","+throwable.getMessage());
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("ssdaafsdf", responseString);

                try {
                    JSONObject object = new JSONObject(responseString);

                    if ("0".equals(object.getString("code"))) {
                        JSONObject data = object.getJSONObject("list");
                        JSONArray array = data.getJSONArray("data");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = (JSONObject) array.get(i);
                            DaiTuiAiBean daiTuiAiBean = new DaiTuiAiBean();
                            daiTuiAiBean.id = obj.getString("id");
                            daiTuiAiBean.user_id = obj.getString("user_id");
                            daiTuiAiBean.talk_group_id = obj.getString("talk_group_id");
                            daiTuiAiBean.add_timestamp = obj.getString("add_timestamp");
                            daiTuiAiBean.send_timestamp = obj.getString("send_timestamp");
//                            xuanpinkbean.small_images = obj.getString( "small_images" );
//                            xuanpinkbean.reserve_price = obj.getString( "reserve_price" );
                            try {
                                daiTuiAiBean.product_title = obj.getString("product_title");
                            } catch (JSONException e) {
                                daiTuiAiBean.product_title = "21";
                            }
                            try {
                                daiTuiAiBean.product_desc = obj.getString("product_desc");
                            } catch (JSONException e) {
                                daiTuiAiBean.product_desc = "1";
                            }
                            try {
                                daiTuiAiBean.product_sign_id = obj.getString("product_sign_id");
                            } catch (JSONException e) {
                                daiTuiAiBean.product_sign_id = "2";
                            }
                            try {
                                daiTuiAiBean.product_id = obj.getString("product_id");
                            } catch (JSONException e) {
                                daiTuiAiBean.product_id = "4";
                            }
                            daiTuiAiBean.platform_id = obj.getString("platform_id");
                            daiTuiAiBean.thumb_image_url = obj.getString("thumb_image_url");
                            daiTuiAiBean.org_price = obj.getString("org_price");
                            daiTuiAiBean.price = obj.getString("price");
                            daiTuiAiBean.cur_price = obj.getString("cur_price");
                            daiTuiAiBean.ticket_start_time = obj.getString("ticket_start_time");
                            daiTuiAiBean.ticket_end_time = obj.getString("ticket_end_time");
                            daiTuiAiBean.commission = obj.getString("commission");
                            daiTuiAiBean.linkurl = obj.getString("linkurl");
                            daiTuiAiBean.descurl = obj.getString("descurl");
                            daiTuiAiBean.status = obj.getString("status");
                            daiTuiAiBean.discount = obj.getString("discount");
                            lishidaituilist.add(daiTuiAiBean);
                        }
                        liShiTuiSongAdapter.notifyDataSetChanged();
                        if (smartRefreshLayout != null) {
                            smartRefreshLayout.finishRefresh();
                            smartRefreshLayout.finishLoadMore();
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

    //获取系统待推广商品
    private void XiTongDaiTui(int page) {
        com.alibaba.fastjson.JSONObject selected_item = talk_group_list.getJSONObject(tv_title_xiala.getSelectedIndex());
        RequestParams requestParams = new RequestParams();
        requestParams.put("talk_group_id", selected_item.getString("id"));
        requestParams.put("page", page);
        requestParams.put("pagesize", "10");
        HttpUtils.post(Constants.getXITONGDAITUI,SetAssistantActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(throwable.getMessage());
//                Log.e( "statuscode",search_content+","+ responseString+","+throwable.getMessage());
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("ssdaafsdf", responseString);

                try {
                    JSONObject object = new JSONObject(responseString);

                    if ("0".equals(object.getString("code"))) {
                        JSONObject data = object.getJSONObject("data");
                        JSONArray array = data.getJSONArray("data");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = (JSONObject) array.get(i);
                            XiTongDaiTuiAiBean daiTuiAiBean = new XiTongDaiTuiAiBean();
                            daiTuiAiBean.id = obj.getString("id");
                            daiTuiAiBean.user_id = obj.getString("user_id");
                            daiTuiAiBean.talk_group_id = obj.getString("talk_group_id");
                            daiTuiAiBean.add_timestamp = obj.getString("add_timestamp");
                            daiTuiAiBean.send_timestamp = obj.getString("send_timestamp");
//                            xuanpinkbean.small_images = obj.getString( "small_images" );
//                            xuanpinkbean.reserve_price = obj.getString( "reserve_price" );
                            try {
                                daiTuiAiBean.product_title = obj.getString("product_title");
                            } catch (JSONException e) {
                                daiTuiAiBean.product_title = "21";
                            }
                            try {
                                daiTuiAiBean.product_desc = obj.getString("product_desc");
                            } catch (JSONException e) {
                                daiTuiAiBean.product_desc = "1";
                            }
                            try {
                                daiTuiAiBean.product_sign_id = obj.getString("product_sign_id");
                            } catch (JSONException e) {
                                daiTuiAiBean.product_sign_id = "2";
                            }
                            try {
                                daiTuiAiBean.product_id = obj.getString("product_id");
                            } catch (JSONException e) {
                                daiTuiAiBean.product_id = "4";
                            }
                            daiTuiAiBean.platform_id = obj.getString("platform_id");
                            daiTuiAiBean.thumb_image_url = obj.getString("thumb_image_url");
                            daiTuiAiBean.org_price = obj.getString("org_price");
                            daiTuiAiBean.price = obj.getString("price");
                            daiTuiAiBean.cur_price = obj.getString("cur_price");
                            daiTuiAiBean.ticket_start_time = obj.getString("ticket_start_time");
                            daiTuiAiBean.ticket_end_time = obj.getString("ticket_end_time");
                            daiTuiAiBean.commission = obj.getString("commission");
                            daiTuiAiBean.linkurl = obj.getString("linkurl");
                            daiTuiAiBean.descurl = obj.getString("descurl");
                            daiTuiAiBean.status = obj.getString("status");
                            daiTuiAiBean.discount = obj.getString("discount");
                            daiTuiAiBean.fee_user_commission = obj.getString("fee_user_commission");
                            daiTuiAiBean.referrer_rate_commission = obj.getString("referrer_rate_commission");
                            xitongdaituiList.add(daiTuiAiBean);
                        }
                        xiTongDaiTuiAiAdapter.notifyDataSetChanged();
                        if (smartRefreshLayout != null) {
                            smartRefreshLayout.finishRefresh();
                            smartRefreshLayout.finishLoadMore();
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
        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_my:
                        recyclerView_stay.setVisibility(View.VISIBLE);
                        recyclerView_xitong.setVisibility(View.VISIBLE);
                        recyclerView_history.setVisibility(View.GONE);

                        smartRefreshLayout.autoRefresh();
                        if (daituiList.size() <= 0) {
                            smartRefreshLayout.autoRefresh();
                        }
                        if (xitongdaituiList.size() <= 0) {
                            smartRefreshLayout.autoRefresh();
                        }
                        break;
                    case R.id.rb_next:
                        recyclerView_stay.setVisibility(View.GONE);
                        recyclerView_xitong.setVisibility(View.GONE);
                        recyclerView_history.setVisibility(View.VISIBLE);
                        smartRefreshLayout.autoRefresh();
                        if (lishidaituilist.size() <= 0) {
                            smartRefreshLayout.autoRefresh();
                        }
                        break;
                }
            }
        });

    }

    @OnClick({R.id.tv_left, R.id.set_weixinqun_fuzhu_ai})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.set_weixinqun_fuzhu_ai:
                String jiaocheng = SPUtils.getStringData(SetAssistantActivity.this, "jiaocheng", "");
                if (jiaocheng.equals("1")) {
                    Intent intent1 = new Intent(SetAssistantActivity.this, MainActivity.class);
                    SPUtils.saveStringData(SetAssistantActivity.this, "zhuaqu", "1");
                    startActivity(intent1);
                } else {
                    Intent intent1 = new Intent(SetAssistantActivity.this, JiaoCActivity.class);
                    SPUtils.saveStringData(SetAssistantActivity.this, "zhuaqu", "1");
                    startActivity(intent1);
                }

                break;

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (daituiList.size()>0 || xitongdaituiList.size()>0) {
            smartRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int need_update = 0; // 0 不需要更新数据，1需要更新数据
        try {
            need_update = data.getIntExtra("need_update",0);
            // Log.d(TAG, "是否需要更新数据: " + need_update);
        } catch (Exception ignored) {}
        if (need_update == 0) return;

        RequestParams update_params = new RequestParams();
        switch (resultCode) {
            case 2: // 欢迎语
                String back_robot_name = data.getStringExtra("robot_name");
                String back_welcome_status = data.getStringExtra("welcome_status");
                String back_welcome_text = data.getStringExtra("welcome");

                update_params.put("robot_name",back_robot_name);
                update_params.put("welcome_status",back_welcome_status);
                update_params.put("welcome",back_welcome_text);

                break;
            case 3: // 设置推送起止时间及间隔
                String start_timestamp = data.getStringExtra("start_timestamp");
                String stop_timestamp = data.getStringExtra("stop_timestamp");
                String interval_time = data.getStringExtra("interval_time");

                update_params.put("start_timestamp",start_timestamp);
                update_params.put("stop_timestamp",stop_timestamp);
                update_params.put("interval_time",interval_time);

                break;
            case 4: // 设置推广方式【小程序详情推广、商品链接推广】
                String desc_type = data.getStringExtra("desc_type");
                String href_type = data.getStringExtra("href_type");

                update_params.put("desc_type",desc_type);
                update_params.put("href_type",href_type);
                break;
        }

        setAiTalkGroupSetting(update_params);
    }

    class SwitchBoxCheckerListener implements Switch.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton view, boolean isChecked) {
            // Log.d(TAG, "onCheckedChanged: " + view.isPressed());
            com.alibaba.fastjson.JSONObject current_item = talk_group_list.getJSONObject(tv_title_xiala.getSelectedIndex());
            String platform = current_item.getString("platform");
            // 请求网络设置
            String update_plant_form = "";

            if (jd_switchbutton.isChecked() && pdd_switchbutton.isChecked()){
                if (platform.contains("1") && platform.contains("2")) return;
                update_plant_form = "[1,2]";
            }
            else if (jd_switchbutton.isChecked() && !pdd_switchbutton.isChecked()){
                if (!platform.contains("1") && platform.contains("2")) return;
                update_plant_form = "[2]";
            }
            else if (!jd_switchbutton.isChecked() && pdd_switchbutton.isChecked()){
                if (platform.contains("1") && !platform.contains("2")) return;
                update_plant_form = "[1]";
            }
            else
            {
                if (!platform.contains("1") && !platform.contains("2")) return;
                update_plant_form = "[]";
        }

            if (view.isPressed()) {
                RequestParams req = new RequestParams();
                req.put("platform", update_plant_form);
                setAiTalkGroupSetting(req);
        }
            else
            {
                // Log.d(TAG, "程序设置操作，不需要更新！" );
        }
        }
    }
}
