package com.android.jdhshop.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import org.angmarch.views.NiceSpinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AiSetTimeActivity extends BaseActivity {
    public List<String> interval_list = Arrays.asList("10分钟","20分钟","30分钟","50分钟");
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.text_set_tv)
    TextView text_set_tv;
    @BindView(R.id.tv_ai_settime_faqi)
    TextView tv_ai_settime_faqi;
    @BindView(R.id.ai_set_timestar)
    TextView start_time_view;
    @BindView(R.id.ai_set_time_end)
    TextView stop_time_view;
    @BindView(R.id.tv_ai_settime_end)
    TextView tv_ai_settime_end;
    @BindView(R.id.tv_title_yijiesuan)
    NiceSpinner interval_spinner;
    TimePickerView stop_time_picker;
    TimePickerView start_time_picker;

    private JSONObject item_info_json ;

    private String start_timestamp = "";
    private String stop_timestamp = "";
    private int interval_time=0;

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_ai_set_time);
        ButterKnife.bind(this);
        tv_left.setVisibility(View.VISIBLE);
        tv_title.setText("发品时间设置");
        interval_spinner.attachDataSource(interval_list);
        text_set_tv.setText("\t\t" + "1.发品时间起-止不要设置错误，如果错误会导致不发单，默认不设置的情况下时间为7点-晚23点。" + "\n\n" + "\t\t" + "2.发品间隔是这次要发品的时间与上次发品的间隔时间，只可选择10分/20分/30分/50分，默认30分钟。");
    }


    @Override
    protected void initData() {
        Intent intent = getIntent();
        // Log.d(TAG, "initData: " + intent.getStringExtra("item_info"));
        String item_info = intent.getStringExtra("item_info");

        if ("".equals(item_info) || item_info == null){
            showTipDialog2("错误提示", Html.fromHtml("页面信息错误，请从正确渠道进入。"), new BaseActivity.onClickListener(){
                @Override
                public void onClickSure() {
                    finish();
                }
            },"我知道了");
            return;
        }

        item_info_json = JSONObject.parseObject(item_info);

        start_timestamp = item_info_json.getString("start_timestamp");
        if (start_timestamp.length()<13) start_timestamp+= "000";


        stop_timestamp = item_info_json.getString("stop_timestamp");
        if (stop_timestamp.length()<13) stop_timestamp+="000";

        interval_time = item_info_json.getIntValue("interval_time");


        String start_time_second = getSecondToString(start_timestamp, "HH:mm");
        String stop_time_second = getSecondToString(stop_timestamp, "HH:mm");

        // Log.d(TAG, "initData: " + start_time_second + "," + stop_time_second);

        start_time_view.setText(start_time_second);
        stop_time_view.setText(stop_time_second);

        interval_list = new LinkedList<>(Arrays.asList("10分钟", "20分钟", "30分钟", "50分钟"));
        interval_spinner.attachDataSource(interval_list);
        interval_spinner.setSelectedIndex(interval_time);


        Calendar calendar = Calendar.getInstance();
        /* 结束推送时间点击器*/
        stop_time_picker = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                int i = timeCompare(getTimes(date), start_time_view.getText().toString());
                if (i == 3) {
                    showToast("结束时间不得小于开始时间");
                } else {
                    stop_time_view.setText(getTimes(date));
                    stop_timestamp = date.getTime() + "";
                }

            }
        })//年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{false, false, false, true, true, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(true)
                .setDividerColor(Color.DKGRAY)
                .setDecorView(null)
                .build();
        calendar.setTime(new Date(Long.parseLong(stop_timestamp)));
        stop_time_picker.setDate(calendar);
        /* 开启推送时间点击器*/
        start_time_picker = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                int i = timeCompare(getTimes(date).toString(), stop_time_view.getText().toString());
                if (i == 1) {
                    showToast("开始时间不得大于结束时间");
                } else {
                    start_time_view.setText(getTimes(date));
                    start_timestamp = date.getTime() + "";
                }

            }
        })//年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{false, false, false, true, true, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(true)
                .setDividerColor(Color.DKGRAY)
                .setDecorView(null)
                .build();
        calendar.setTime(new Date(Long.parseLong(start_timestamp)));
        start_time_picker.setDate(calendar);
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    public static String getSecondToString(String time, String type) {
        if (TextUtils.isEmpty(type)) {
            type = "yyyy-MM-dd";
        }

        if (TextUtils.isEmpty(time)) {
            return "";
        }
        long longTime = 0;
        if (time.length() == 10) {
            longTime = Long.valueOf(time) * 1000;
        } else if (time.length() == 13) {
            longTime = Long.valueOf(time);
        }

        SimpleDateFormat sdf = new SimpleDateFormat(type);
        String date = sdf.format(new Date(longTime));
        return date + "";
    }

    private String getTimes(Date date) {//年月日时分秒格式
//        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @OnClick({R.id.tv_left, R.id.tv_ai_settime_faqi, R.id.tv_ai_settime_end,R.id.set_huanying_geng})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.tv_ai_settime_faqi:
                //点击组件的点击事件,选择时间
                if (start_time_picker != null) {
                    start_time_picker.show();
                    //pvTime.show(ai_set_timestar);
                }
                break;
            case R.id.tv_ai_settime_end:
                //点击组件的点击事件,选择时间
                if (stop_time_picker != null) {
                    stop_time_picker.show();
                    //int i = timeCompare(pvTime.toString(), ai_set_timestar.toString());
                    //pvTime.show(ai_set_time_end);
                }
                break;
            case R.id.set_huanying_geng:

                Intent i = new Intent();
                i.putExtra("need_update",1);
                i.putExtra("start_timestamp",start_timestamp);
                i.putExtra("stop_timestamp",stop_timestamp);
                i.putExtra("interval_time",interval_spinner.getSelectedIndex()+"");

                setResult(3,i);
                finish();
        }
    }

    public static int timeCompare(String startTime, String endTime) {
        int i = 0;
        //注意：传过来的时间格式必须要和这里填入的时间格式相同
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        try {
            Date date1 = dateFormat.parse(startTime);//开始时间
            Date date2 = dateFormat.parse(endTime);//结束时间
            // 1 结束时间小于开始时间 2 开始时间与结束时间相同 3 结束时间大于开始时间
            if (date2.getTime() < date1.getTime()) {
                //结束时间小于开始时间
                i = 1;
            } else if (date2.getTime() == date1.getTime()) {
                //开始时间与结束时间相同
                i = 2;
            } else if (date2.getTime() > date1.getTime()) {
                //结束时间大于开始时间
                i = 3;
            }
        } catch (Exception e) {

        }
        return i;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            tv_left.callOnClick();
        }

        return false;

    }

}
