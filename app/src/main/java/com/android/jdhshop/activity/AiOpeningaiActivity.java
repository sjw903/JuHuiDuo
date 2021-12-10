package com.android.jdhshop.activity;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * 开通群助理页面
 */
public class AiOpeningaiActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.textt)
    TextView textt;
    @BindView(R.id.set_text_wubi)
    TextView set_text_wubi;

    @BindView(R.id.weixinq_textq)
    TextView weixinq_textq;
    @BindView(R.id.weixinq_textw)
    TextView weixinq_textw;
    @BindView(R.id.weixinq_texte)
    TextView weixinq_texte;
    @BindView(R.id.weixinq_textr)
    TextView weixinq_textr;
    @BindView(R.id.set_ai_feipei_qunhao)
    TextView set_ai_feipei_qunhao;

    @BindView(R.id.texty)
    TextView texty;
    @BindView(R.id.xuhao1)
    ImageView xuhao1;
    @BindView(R.id.xuhao2)
    ImageView xuhao2;
    @BindView(R.id.xuhao3)
    ImageView xuhao3;
    @BindView(R.id.set_weixinqun_add)
    Button set_weixinqun_add;
    @BindView(R.id.set_weixinqun_fuzhu_ai)
    Button set_weixinqun_fuzhu_ai;
    @BindView(R.id.xuhai_line1)
    LinearLayout xuhai_line1;
    @BindView(R.id.xuhai_line2)
    LinearLayout xuhai_line2;
    @BindView(R.id.xuhai_line3)
    LinearLayout xuhai_line3;
    Drawable drawable;
    private int zhuangtai;
    private String name;
    private String tmpid;
    private String feipei;
    private String wx_service_user;
    private String status;
    @BindView(R.id.openimg_tishi)
    ImageView openimg_tishi;
    @BindView(R.id.zhiyin)
    ImageView zhiyin;
    @BindView(R.id.zhiyin1)
    ImageView zhiyin1;

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_ai_openingai);
        ButterKnife.bind(this);
        tv_left.setVisibility(View.VISIBLE);
        tv_title.setText("开通群助理");
        set_weixinqun_add.setText("下一步");
        textt.setText("50<=群人数<100");
        texty.setText("群人数>=100");

        BaseLogDZiYuan.LogDingZiYuan(openimg_tishi, "zhiyin.png");
        BaseLogDZiYuan.LogDingZiYuan(zhiyin, "zhiyin.png");
        BaseLogDZiYuan.LogDingZiYuan(zhiyin1, "zhiyin1.png");
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        tmpid = intent.getStringExtra("tmpid");
        name = intent.getStringExtra("name");
        wx_service_user = intent.getStringExtra("wx_service_user");
        status = intent.getStringExtra("status");
        if (status.equals("5")) {
            xuhai_line1.setVisibility(View.GONE);
            xuhai_line2.setVisibility(View.GONE);
            xuhai_line3.setVisibility(View.VISIBLE);
            set_weixinqun_add.setVisibility(View.GONE);
            drawable = getResources().getDrawable(R.mipmap.red2);
            xuhao2.setImageDrawable(drawable);
            xuhao1.setImageDrawable(getResources().getDrawable(R.mipmap.redwan1));
            xuhao2.setImageDrawable(getResources().getDrawable(R.mipmap.redwan2));
            xuhao3.setImageDrawable(getResources().getDrawable(R.mipmap.red3));
            TiaoXuanAi("0");
//            set_ai_feipei_qunhao.setText("为您分配的助理微信号:" + feipei);
        }

        set_text_wubi.setText(name);

        String str1 = "申请助理群人数必须在<font color = '#DE4F4B'>50人</font>至<font color = '#DE4F4B'>100人</font>之间。";
        String str2 = "开通后需<font color = '#DE4F4B'>5天</font>内将群人数拉够100人，逾期人数不够100人助理会自动关闭。";
        String str3 = "微信群人数大于<font color = '#DE4F4B'>100人</font>就可以申请助理。";
        String str4 = "务必群人数满<font color = '#DE4F4B'>100人</font>，否则群助理会自动关闭。";
        CharSequence charSequence;
        CharSequence charSequence2;
        CharSequence charSequence3;
        CharSequence charSequence4;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            charSequence = Html.fromHtml(str1, Html.FROM_HTML_MODE_LEGACY);
            charSequence2 = Html.fromHtml(str2, Html.FROM_HTML_MODE_LEGACY);
            charSequence3 = Html.fromHtml(str3, Html.FROM_HTML_MODE_LEGACY);
            charSequence4 = Html.fromHtml(str4, Html.FROM_HTML_MODE_LEGACY);
        } else {
            charSequence = Html.fromHtml(str1);
            charSequence2 = Html.fromHtml(str2);
            charSequence3 = Html.fromHtml(str3);
            charSequence4 = Html.fromHtml(str4);
        }
        weixinq_textq.setText(charSequence);
        weixinq_textw.setText(charSequence2);
        weixinq_texte.setText(charSequence3);
        weixinq_textr.setText(charSequence4);
        set_weixinqun_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (set_weixinqun_add.getText().toString().equals("下一步")) {
                    assistant("请确认群名称已修改", "请修改群名称为【" + name + "】否则无法成功开通助理哦~", "");
                } else {
                    drawable = getResources().getDrawable(R.mipmap.grey2);
                    xuhao2.setImageDrawable(drawable);
                    xuhao1.setImageDrawable(getResources().getDrawable(R.mipmap.red1));
                    set_weixinqun_add.setText("下一步");
                    xuhai_line1.setVisibility(View.VISIBLE);
                    xuhai_line2.setVisibility(View.GONE);
                }
            }
        });
    }

    private void assistant(String dia_title, String text1, String text2) {
        // 1.创建弹出式对话框
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AiOpeningaiActivity.this);    // 系统默认Dialog没有输入框
        // 获取自定义的布局
        View alertDialogView = View.inflate(AiOpeningaiActivity.this, R.layout.dialog_ai_assistant, null);
        final AlertDialog tempDialog = alertDialog.create();
        tempDialog.setView(alertDialogView, 0, 0, 0, 0);
        tempDialog.getWindow().setBackgroundDrawableResource(R.drawable.yuanjiao);
        final EditText editText = (EditText) alertDialogView.findViewById(R.id.ed_message);
        TextView title = (TextView) alertDialogView.findViewById(R.id.ai_assistant_dia_title);
        TextView textv1 = (TextView) alertDialogView.findViewById(R.id.ai_ass_text1);
        TextView textv2 = (TextView) alertDialogView.findViewById(R.id.ai_ass_text2);
        title.setText(dia_title);
        textv1.setText(text1);
        textv2.setText(text2);
        tempDialog.setCancelable(true);

        tempDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView quxiao = alertDialogView.findViewById(R.id.positiveTextView);
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempDialog.dismiss();
            }
        });
        TextView queren = alertDialogView.findViewById(R.id.negativeTextView);
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuhai_line1.setVisibility(View.GONE);
                xuhai_line2.setVisibility(View.VISIBLE);
                drawable = getResources().getDrawable(R.mipmap.red2);
                xuhao2.setImageDrawable(drawable);
                xuhao1.setImageDrawable(getResources().getDrawable(R.mipmap.redwan1));
                set_weixinqun_add.setText("上一步");
                tempDialog.dismiss();
            }
        });

        tempDialog.show();
    }

    private void assistant2(String dia_title, String text1, String text2, String type) {
        // 1.创建弹出式对话框
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AiOpeningaiActivity.this);    // 系统默认Dialog没有输入框
        // 获取自定义的布局
        View alertDialogView = View.inflate(AiOpeningaiActivity.this, R.layout.dialog_ai_assistant, null);
        final AlertDialog tempDialog = alertDialog.create();
        tempDialog.setView(alertDialogView, 0, 0, 0, 0);
        tempDialog.getWindow().setBackgroundDrawableResource(R.drawable.yuanjiao);
        final EditText editText = (EditText) alertDialogView.findViewById(R.id.ed_message);
        TextView title = (TextView) alertDialogView.findViewById(R.id.ai_assistant_dia_title);
        TextView textv1 = (TextView) alertDialogView.findViewById(R.id.ai_ass_text1);
        TextView textv2 = (TextView) alertDialogView.findViewById(R.id.ai_ass_text2);
        title.setText(dia_title);
        textv1.setText(text1);
        textv2.setText(text2);
        tempDialog.setCancelable(true);

        tempDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView quxiao = alertDialogView.findViewById(R.id.positiveTextView);
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempDialog.dismiss();
            }
        });
        TextView queren = alertDialogView.findViewById(R.id.negativeTextView);
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("0")) {
                    TiaoXuanAi("0");
                    xuhai_line1.setVisibility(View.GONE);
                    xuhai_line2.setVisibility(View.GONE);
                    xuhai_line3.setVisibility(View.VISIBLE);
                    drawable = getResources().getDrawable(R.mipmap.red2);
                    xuhao2.setImageDrawable(drawable);
                    xuhao1.setImageDrawable(getResources().getDrawable(R.mipmap.redwan1));
                    xuhao2.setImageDrawable(getResources().getDrawable(R.mipmap.redwan2));
                    set_weixinqun_add.setVisibility(View.GONE);
                    xuhao3.setImageDrawable(getResources().getDrawable(R.mipmap.red3));
                    tempDialog.dismiss();
                } else if (type.equals("1")) {
                    TiaoXuanAi("1");
                    xuhai_line1.setVisibility(View.GONE);
                    xuhai_line2.setVisibility(View.GONE);
                    xuhai_line3.setVisibility(View.VISIBLE);
                    drawable = getResources().getDrawable(R.mipmap.red2);
                    xuhao2.setImageDrawable(drawable);
                    xuhao1.setImageDrawable(getResources().getDrawable(R.mipmap.redwan1));
                    xuhao2.setImageDrawable(getResources().getDrawable(R.mipmap.redwan2));
                    set_weixinqun_add.setVisibility(View.GONE);
                    xuhao3.setImageDrawable(getResources().getDrawable(R.mipmap.red3));
                    tempDialog.dismiss();
                }

            }
        });

        tempDialog.show();
    }

    private void assistant3() {
        // 1.创建弹出式对话框
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AiOpeningaiActivity.this);    // 系统默认Dialog没有输入框
        // 获取自定义的布局
        View alertDialogView = View.inflate(AiOpeningaiActivity.this, R.layout.dialog_ai_fuzhibut, null);
        final AlertDialog tempDialog = alertDialog.create();
        tempDialog.setView(alertDialogView, 0, 0, 0, 0);
        tempDialog.getWindow().setBackgroundDrawableResource(R.drawable.yuanjiao);
        final EditText editText = (EditText) alertDialogView.findViewById(R.id.ed_message);

        tempDialog.setCancelable(true);

        tempDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button viewById = alertDialogView.findViewById(R.id.set_weixinqun_add);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempDialog.dismiss();
                finish();
            }
        });
        tempDialog.show();
    }

    private void TiaoXuanAi(String group_num_type) {
        RequestParams params = new RequestParams();
        params.put("gid", tmpid);
        params.put("group_num_type", group_num_type);
        HttpUtils.post(Constants.getTiaoXuanAI, AiOpeningaiActivity.this, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    feipei = jsonObject.getJSONObject("data").getString("wx_service_user");
                    set_ai_feipei_qunhao.setText("为您分配的助理微信号:" + feipei);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void initListener() {

    }

    @OnClick({R.id.tv_left, R.id.set_weixinqun_dj1, R.id.set_weixinqun_dj2, R.id.set_weixinqun_fuzhu_ai, R.id.set_but_copy_qun})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.set_weixinqun_dj1:
                assistant2("申请助理", "请确认群人数是否在50-100人之间,若人数错误会导致申请失败", "", "0");
                break;
            case R.id.set_weixinqun_dj2:
                assistant2("申请助理", "请确认群人数是否在100人之上,若人数错误会导致申请失败", "", "1");
                break;
            case R.id.set_weixinqun_fuzhu_ai:
                ClipboardManager cmb1 = (ClipboardManager) AiOpeningaiActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb1.setText(feipei.trim());
                T.showShort(AiOpeningaiActivity.this, "复制成功");
                assistant3();
                break;
            case R.id.set_but_copy_qun:
                ClipboardManager cmb = (ClipboardManager) AiOpeningaiActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(name.trim());
                T.showShort(AiOpeningaiActivity.this, "复制成功");
                break;
        }
    }
}
