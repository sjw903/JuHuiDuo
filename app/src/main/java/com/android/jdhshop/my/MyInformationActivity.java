package com.android.jdhshop.my;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.CropActivity;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.DomainBean;
import com.android.jdhshop.bean.UserHomePageBean;
import com.android.jdhshop.bean.UserInfoBean;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.common.CommonUtils;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.juduohui.response.HttpJsonResponse;
import com.android.jdhshop.utils.CheckUtil;
import com.android.jdhshop.utils.PickerSelectUtils;
import com.android.jdhshop.utils.TypeConvertUtil;
import com.android.jdhshop.widget.AutoClearEditText;
import com.android.jdhshop.widget.CircleImageView;
import com.android.jdhshop.widget.ImageSelectDialog;
import com.bumptech.glide.Glide;
import com.codert.rtmulticheckdialog_module.RTMultiCheckDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * 我的资料
 * Created by yohn on 2018/7/14.
 */

public class MyInformationActivity extends BaseActivity {
    @BindView(R.id.bg_head)
    RelativeLayout bg_head;
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.tv_finish)
    TextView tv_finish;
    @BindView(R.id.iv_avater)
    CircleImageView iv_avater;//头像

    @BindView(R.id.et_one)
    AutoClearEditText et_one;//昵称
    @BindView(R.id.et_two)
    TextView et_two;//用户名
    @BindView(R.id.ll_name)
    LinearLayout ll_name;//用户名

    @BindView(R.id.et_three)
    TextView et_three;//手机号

    @BindView(R.id.ll_phone)
    LinearLayout ll_phone;//手机号

    @BindView(R.id.et_four)
    AutoClearEditText et_four;//身高
    @BindView(R.id.et_five)
    AutoClearEditText et_five;//体重
    @BindView(R.id.et_six)
    TextView et_six;//QQ

    @BindView(R.id.ll_contact)
    LinearLayout ll_contact;//QQ

    @BindView(R.id.et_seven)
    AutoClearEditText et_seven;//个性签名
    @BindView(R.id.tv_four)
    TextView tv_four;//生日

    @BindView(R.id.ll_birth)
    LinearLayout ll_birth;//生日

    @BindView(R.id.tv_address)
    TextView tv_address;

    @BindView(R.id.ll_area)
    LinearLayout ll_area;

    @BindView(R.id.et_address)
    AutoClearEditText et_adress;//手机号
    @BindView(R.id.rg_sex)
    RadioGroup rg_sex;//生日
    @BindView(R.id.cb_sex_man)
    RadioButton cb_sex_man;//男
    @BindView(R.id.cb_sex_woman)
    RadioButton cb_sex_woman;//女

    @BindView(R.id.ll_sex)
    LinearLayout ll_sex;//性别

    @BindView(R.id.tv_domain)
    TextView tv_domain;

    @BindView(R.id.ll_domain)
    RelativeLayout ll_domain;

    @BindView(R.id.tv_done_rate)
    TextView tv_done_rate;//资料完成度


    @BindView(R.id.tv_sign)
    TextView tv_sign;//简介

    @BindView(R.id.ll_sign)
    LinearLayout ll_sign;//简介

    private int sexStatus = 1;
    private Gson gson = new Gson();

    private File avaterFile = null;

    private ACache mAcache;
    String token;
    private String integrity_rate;

    private UserInfoBean userBean;
    private CityPickerView cityDialog;
    private String domain;
    private UserHomePageBean userHomePageBean;
    private boolean changed;
    private String userName = "";
    private String sign = "";
    private String contact = "";//联系方式（微信或qq）
    private String phone = ""; //手机号
    private String area = "";
    private String birthday = "";
    private static final int REQUEST_CODE_USER_NAME = 888;
    private static final int REQUEST_CODE_SIGN = 889;
    private static final int REQUEST_CODE_PHONE = 890;
    private static final int REQUEST_CODE_CONTACT = 891;
    private static final int REQUEST_CODE_HEADER = 892;

    List<String> itemList = new ArrayList<>();

    @Override
    protected void initUI() {
        setContentView(R.layout.ac_my_info2);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        userHomePageBean = (UserHomePageBean) getIntent().getSerializableExtra("data");
        mAcache = ACache.get(this);
        token = mAcache.getAsString(Constants.TOKEN);
        bg_head.setBackgroundColor(getResources().getColor(android.R.color.white));

        //动态设置drawableLeft属性
        Drawable drawable1 = getResources().getDrawable(R.mipmap.icon_back);
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
        tv_left.setCompoundDrawables(drawable1, null, null, null);
        tv_left.setVisibility(View.VISIBLE);


        tv_title.setText("我的资料");
        tv_title.setTextColor(getResources().getColor(R.color.col_333));
        tv_right.setVisibility(View.GONE);
        tv_right.setText("编辑");
        tv_right.setTextColor(getResources().getColor(R.color.red));
        cityDialog = new CityPickerView();
        cityDialog.init(this);
        //使用前配置config，必须，可在application里面全局配置，如果需要设置默认选择城市等，需要特别设置config
        CityConfig cityConfig = new CityConfig.Builder().provinceCyclic(false)
                .cityCyclic(false)
                .districtCyclic(false).build();
        cityDialog.setConfig(cityConfig);
        cityDialog.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                String content = province.getName() + "-" + city.getName() + "-" + district.getName();
                if (!content.equals(tv_address.getText().toString())) {
                    tv_address.setText(content);
                    if (!TextUtils.isEmpty(content)) {
                        RequestParams requestParams = new RequestParams("area", content);
                        upload(requestParams);
                    }
                }
            }

            @Override
            public void onCancel() {
            }
        });
//        getUserMsg();


//        BaseLogDZiYuan.LogDingZiYuan(iv_avater, "icon_defult_boy.png");
        getLibType();
        updateRate();
        if (null != userHomePageBean.user_name) {
            userName = userHomePageBean.user_name;
        }
        et_two.setText(userName);
        String url = userHomePageBean.auth_icon;
        if (!TextUtils.isEmpty(url)) {
            Glide.with(this).load(url).into(iv_avater);
        }

        if (null != userHomePageBean.user_sign) {
            sign = userHomePageBean.user_sign;
        }
        tv_sign.setText(sign);


        if (null != userHomePageBean.phone) {
            phone = userHomePageBean.phone;
        }
        et_three.setText(phone);


        if (null != userHomePageBean.area) {
            area = userHomePageBean.area;
        }
        tv_address.setText(area);

        int sex = userHomePageBean.sex;
        if (0 != sex) {
            if (1 == sex) {
                cb_sex_man.setChecked(true);
            } else if (2 == sex) {
                cb_sex_woman.setChecked(true);
            }
        }
        if (null != userHomePageBean.birthday) {
            birthday = userHomePageBean.birthday;
        }
        tv_four.setText(birthday);

        if (null != userHomePageBean.contact) {
            contact = userHomePageBean.contact;
        }
        et_six.setText(contact);

        if (null != userHomePageBean.domain) {
            domain = userHomePageBean.domain;
        }
        tv_domain.setText(domain);
    }


    @Override
    protected void initListener() {
        rg_sex.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.cb_sex_man://男
                {
                    cb_sex_man.isChecked();
                    RequestParams requestParams = new RequestParams("sex", 1);
                    upload(requestParams);
                    break;
                }
                case R.id.cb_sex_woman:// 女
                {
                    cb_sex_woman.isChecked();
                    RequestParams requestParams = new RequestParams("sex", 2);
                    upload(requestParams);
                    break;
                }
            }
        });
        ll_domain.setOnClickListener(v -> {
            if (!itemList.isEmpty()) {
                //初始化dialog相关属性
                //new RTMultiCheckDialog(context）为默认dialog宽高屏幕占比0.7
                RTMultiCheckDialog customDialog = new RTMultiCheckDialog(getComeActivity(), 0.7, 0.7)
                        .setTitleText("请选择不超过3个选项")
                        .setConfirmText("确定")
                        .setCancelText("取消")
                        .setConfirmOnclicListener(rtMultiCheckDialog -> {
                            //点击确认事件
                            List<Boolean> list = rtMultiCheckDialog.getItemChecked();
                            int selectCount = 0;

                            for (Boolean value : list) {
                                if (value) {
                                    selectCount++;
                                }
                            }
                            if (selectCount > 3) {
                                showToast(getString(R.string.homepage_domain_limit));
                            } else if (selectCount > 0) {
                                rtMultiCheckDialog.dismiss();
                                int len = list.size();
                                List<String> strList = new ArrayList<>(selectCount);
                                for (int i = 0; i < len; i++) {
                                    if (list.get(i)) {
                                        strList.add(itemList.get(i));
                                    }
                                }
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < selectCount; i++) {
                                    if (i != selectCount - 1) {
                                        stringBuilder.append(strList.get(i)).append(",");
                                    } else {
                                        stringBuilder.append(strList.get(i));
                                    }
                                }
                                domain = stringBuilder.toString();
                                tv_domain.setText(domain);
                                RequestParams requestParams = new RequestParams("domain", domain);
                                upload(requestParams);
                            } else {
                                rtMultiCheckDialog.dismiss();
                            }
                        })
                        .setCancelOnclicListener(rtMultiCheckDialog -> {
                            //点击取消事件
                            rtMultiCheckDialog.dismiss();
                        })
                        .setItemNames(itemList);
                //显示dialog
                customDialog.show();
                //其他设置
                //设置点击空白处是否关闭dialog，默认不关闭
                customDialog.setCancelInOutside(true);
                //设置图标是否显示，默认显示
                customDialog.setIconShow(false);
            }
        });
        ll_name.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("userName", userName);
            openActivityForResult(UserNameEditActivity.class, bundle, REQUEST_CODE_USER_NAME);
        });
        ll_sign.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("sign", sign);
            openActivityForResult(UserSignEditActivity.class, bundle, REQUEST_CODE_SIGN);
        });
        ll_phone.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("phone", phone);
            openActivityForResult(UserPhoneEditActivity.class, bundle, REQUEST_CODE_PHONE);
        });
        ll_contact.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("contact", contact);
            openActivityForResult(UserContactEditActivity.class, bundle, REQUEST_CODE_CONTACT);
        });
    }

    private boolean isBase64(String str) {
        String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        return Pattern.matches(base64Pattern, str);
    }

    /**
     * Base64解密字符串
     *
     * @param content     -- 待解密字符串
     * @param charsetName -- 字符串编码方式
     * @return
     */
    private String base64Decode(String content, String charsetName) {
        if (TextUtils.isEmpty(charsetName)) {
            charsetName = "UTF-8";
        }
        byte[] contentByte = Base64.decode(content, Base64.DEFAULT);
        try {
            return new String(contentByte, charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onBackPressed() {
        quit();
        super.onBackPressed();
    }

    private boolean quit() {
        Intent intent = new Intent();
        intent.putExtra("changed", changed);
        setResult(RESULT_OK, intent);
        return true;
    }

    @OnClick({R.id.tv_right, R.id.ll_avater, R.id.tv_left, R.id.ll_birth, R.id.tv_finish, R.id.ll_area})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_finish:
            case R.id.tv_right:
//                editUserMsgRequest();
                break;
            case R.id.ll_avater: //编辑头像
                new ImageSelectDialog(getComeActivity(), R.style.ActionSheetDialogStyle).setOnImageSelectDialogListener(new ImageSelectDialog.onImageSelectDialogListener() {
                    @Override
                    public void onImageSelectResult(String picturePath) {
                        if (picturePath == null || "".equals(picturePath))
                            return;
                        Intent intent = new Intent(getComeActivity(), CropActivity.class);
                        intent.putExtra("url", picturePath);
                        startActivityForResult(intent, REQUEST_CODE_HEADER);
                    }
                }).show();
                break;
            case R.id.tv_left: //结束
                quit();
                finish();
                break;
            case R.id.ll_area: //结束
                cityDialog.showCityPicker();
                break;
            case R.id.ll_birth: //填写生日
                PickerSelectUtils.getInstence().from(getComeActivity()).TimePicker(tv_four, content -> {
                    String now = tv_four.getText().toString();
                    if (!TextUtils.isEmpty(now)) {
                        if (!content.equals(birthday)) {
                            RequestParams requestParams = new RequestParams("birthday", content);
                            upload(requestParams);
                        }
                    }
                }).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_USER_NAME) {
                String userName = data.getStringExtra("userName");
                String integrity_rate = data.getStringExtra("integrity_rate");
                userHomePageBean.integrity_rate = integrity_rate;
                userHomePageBean.user_name = userName;
                this.userName = userName;
                et_two.setText(userName);
                changed = true;
                updateRate();
            } else if (requestCode == REQUEST_CODE_SIGN) {
                String sign = data.getStringExtra("sign");
                String integrity_rate = data.getStringExtra("integrity_rate");
                userHomePageBean.integrity_rate = integrity_rate;
                userHomePageBean.user_sign = sign;
                this.sign = sign;
                tv_sign.setText(sign);
                changed = true;
                updateRate();
            } else if (requestCode == REQUEST_CODE_PHONE) {
                String phone = data.getStringExtra("phone");
                String integrity_rate = data.getStringExtra("integrity_rate");
                userHomePageBean.integrity_rate = integrity_rate;
                userHomePageBean.phone = phone;
                this.phone = phone;
                et_three.setText(phone);
                changed = true;
                updateRate();
            } else if (requestCode == REQUEST_CODE_CONTACT) {
                String contact = data.getStringExtra("contact");
                String integrity_rate = data.getStringExtra("integrity_rate");
                userHomePageBean.integrity_rate = integrity_rate;
                userHomePageBean.contact = contact;
                this.contact = contact;
                et_six.setText(contact);
                changed = true;
                updateRate();
            } else if (requestCode == REQUEST_CODE_HEADER) {
                avaterFile = new File(data.getStringExtra("url"));
                uploadImage(avaterFile, "userAvator", "auth_icon");
            }
        }
    }

    /**
     * 刷新UI
     **/
    public void refreshUI() {
        if (null != userBean) {
            if (null != userBean.user_detail) {
                if (isBase64(userBean.user_detail.nickname == null ? "" : userBean.user_detail.nickname)) {
                    et_one.setText(base64Decode(userBean.user_detail.nickname, "utf-8"));
                } else {
                    et_one.setText(TypeConvertUtil.getString(userBean.user_detail.nickname, ""));//昵称
                }
                et_two.setText(TypeConvertUtil.getString(userBean.user_detail.truename, ""));//用户名
                et_four.setText(TypeConvertUtil.getString(userBean.user_detail.height, ""));//身高
                et_five.setText(TypeConvertUtil.getString(userBean.user_detail.weight, ""));//体重
                et_six.setText(TypeConvertUtil.getString(userBean.user_detail.weixin, ""));//QQ
                et_seven.setText(TypeConvertUtil.getString(userBean.user_detail.signature, ""));//个性签名
                tv_four.setText(TypeConvertUtil.getString(userBean.user_detail.birthday, ""));//生日
                if ("2".equals(userBean.user_detail.sex)) {
                    cb_sex_woman.setChecked(true);
                } else {
                    cb_sex_man.setChecked(true);
                }
                et_adress.setText(TypeConvertUtil.getString(userBean.user_detail.detail_address, ""));//昵称
                if (!"".equals(TypeConvertUtil.getString(userBean.user_detail.province, ""))) {
                    if (isChinese(tv_address.getText().toString())) {
                    } else {
                        tv_address.setText(TypeConvertUtil.getString(userBean.user_detail.province, "") + "-" + TypeConvertUtil.getString(userBean.user_detail.city, "") + "-" + TypeConvertUtil.getString(userBean.user_detail.county, ""));//昵称
                    }
                }
            }
            if (null != userBean.user_msg) {
                et_three.setText(TypeConvertUtil.getString(userBean.user_msg.phone, ""));//手机号
            }
            LogUtils.d(TAG, "refreshUI: userBean.user_detail.avatar = " + userBean.user_detail.avatar);

        }
    }

    /**
     * 获取用户信息
     */
    private void getUserMsg() {
        if (!CommonUtils.isNetworkAvailable()) {
            showToast(getResources().getString(R.string.error_network));
            return;
        }
        RequestParams params = new RequestParams();
        params.put("token", token);
        HttpUtils.post(Constants.GET_USER_MSG, MyInformationActivity.this, params, new TextHttpResponseHandler() {
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
                    String data = jsonObject.optString("data");
                    if (0 == code) {
                        if (!TextUtils.isEmpty(data)) {//GSON对数字都当做double解析
                            Gson gson = new Gson();
                            userBean = gson.fromJson(data.trim(), UserInfoBean.class);
                        }
                        refreshUI();
                    } else {
                        showToast(msg);
                    }
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


    public static String byteArrayToStr(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        String str = new String(byteArray);
        return str;
    }

    // 判断一个字符串是否含有中文
    public boolean isChinese(String str) {
        if (str == null)
            return false;
        for (char a : str.toCharArray()) {
            if (isChinese(String.valueOf(a)))
                return true;// 有一个中文字符就返回
        }
        return false;
    }

//    private void editUserMsgRequest() {
//        if (TextUtils.isEmpty(getTextEditValue(et_one))) {
//            showToast("请填写用户昵称");
//            return;
//        }
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("token", token);
//        requestParams.put("nickname", getTextEditValue(et_one)); //用户名称
//        requestParams.put("truename", getTextEditValue(et_two)); //真实姓名
//        requestParams.put("height", "312"); //身高
//        requestParams.put("weight", "123"); //体重
//        requestParams.put("blood", "1"); //血型 1A型 2B型 3AB型 4O型 5其它
//        requestParams.put("birthday", getTextEditValue(tv_four)); //出生日期
//        requestParams.put("weixin", getTextEditValue(et_six)); //微信号
//
//        requestParams.put("qq", "123"); //微信号
//        requestParams.put("expiration_date", "123");
//        requestParams.put("balance_plantform", "123");
//        requestParams.put("balance_service", "123");
//        requestParams.put("balance_user", "123");
//        requestParams.put("email", "123");
//        requestParams.put("username", "123");
//
//
//        if (!"".equals(tv_address.getText().toString())) {
//            requestParams.put("province", tv_address.getText().toString().split("-")[0]); //省份
//            requestParams.put("city", tv_address.getText().toString().split("-")[1]); //城市
//            requestParams.put("county", tv_address.getText().toString().split("-")[2]); //县/区域
//        }
//        requestParams.put("detail_address", et_adress.getText().toString()); //详细地址
//        requestParams.put("signature", "11111"); //个性签名
//        //性别 1男 2女 3保密
//        if (cb_sex_man.isChecked()) {
//            requestParams.put("sex", "1");
//        } else if (cb_sex_woman.isChecked()) {
//            requestParams.put("sex", "2");
//        } else {
//            requestParams.put("sex", "3");
//        }
//
//        HttpUtils.post(Constants.EDIT_USER_MSG, MyInformationActivity.this, requestParams, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(responseString);
//                LogUtils.e(TAG, "--" + responseString);
//            }
//
//            @Override
//            public void onStart() {
//                super.onStart();
//                showLoadingDialog();
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                closeLoadingDialog();
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                try {
//                    JSONObject jsonObject = new JSONObject(responseString);
//                    int code = jsonObject.optInt("code");
//                    String msg = jsonObject.optString("msg");
//                    if (code == 0) {
//                        showToast(msg);
//                        //更新用户信息
//                        BroadcastManager.getInstance(getComeActivity()).sendBroadcast(BroadcastContants.sendUserMessage);
//                        quit();
//                        finish();
//                    } else {
//                        showToast(msg);
//                    }
//                } catch (JSONException e) {
//                }
//            }
//        });
//    }

    /**
     * 获取领域类型
     */
    private void getLibType() {
        showLoadingDialog();
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.GET_MEDIA_LIB_TYPE, MyInformationActivity.this, requestParams, new HttpUtils.TextHttpResponseHandler() {
            @Override
            public void onFinish() {
                closeLoadingDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                com.alibaba.fastjson.JSONObject response = com.alibaba.fastjson.JSONObject.parseObject(responseString);
                LogUtils.d("responseString = " + responseString);
                if (response.getIntValue("code") == 0) {
                    JSONArray jsonArray = response.getJSONArray("list");
                    if (null != jsonArray) {
                        int size = jsonArray.size();
                        for (int i = 0; i < size; i++) {
                            com.alibaba.fastjson.JSONObject jsonObject = jsonArray.getJSONObject(i);
                            DomainBean bean = gson.fromJson(jsonObject.toJSONString(), new TypeToken<DomainBean>() {
                            }.getType());
                            if (bean != null) {
                                LogUtils.d("bean = " + bean);
                                if (0 != bean.id) {
                                    itemList.add(bean.type_name);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * @param bgFile      文件
     * @param ossPathName oss名字
     * @param uploadType  头像或者背景
     */
    private void uploadImage(File bgFile, String ossPathName, String uploadType) {
        showLoadingDialog();
        String fileName = bgFile.getName();
        RequestParams sign_req = new RequestParams();
        sign_req.put("file_name", fileName.replace("[", "").replace("]", ""));
        sign_req.put("oss_path_name", ossPathName);
        sign_req.put("channel_type", "2");
        HttpUtils.post(Constants.GET_UPLOAD_SIGN, MyInformationActivity.this, sign_req, new HttpJsonResponse() {
            @Override
            protected void onSuccess(com.alibaba.fastjson.JSONObject response) {
                LogUtils.d("onSuccess response = " + response.toJSONString());
                if (response.getIntValue("code") == 0) {
                    try {
                        String jsonStr = response.getString("data");
                        LogUtils.d("onSuccess jsonStr = " + jsonStr);
                        com.alibaba.fastjson.JSONObject signed_file_name = JSON.parseObject(jsonStr);
                        RequestParams tmp_req = new RequestParams();
                        tmp_req.put("OSSAccessKeyId", signed_file_name.getString("accessid"));
                        tmp_req.put("callback", signed_file_name.getString("callback"));
                        tmp_req.put("key", signed_file_name.getString("dir") + "/" + signed_file_name.getString("pic_name"));
                        tmp_req.put("policy", signed_file_name.getString("policy"));
                        tmp_req.put("signature", signed_file_name.getString("signature"));
                        tmp_req.put("success_action_status", "200");
                        tmp_req.put("file", fileName, bgFile);
                        HttpUtils.postUpload(signed_file_name.getString("host"), MyInformationActivity.this, tmp_req, new HttpUtils.TextHttpResponseHandler() {
                            @Override
                            protected void onSuccess(int statusCode, Header[] headers, String responseString) {
                                super.onSuccess(statusCode, headers, responseString);
                                try {
                                    com.alibaba.fastjson.JSONObject response = com.alibaba.fastjson.JSONObject.parseObject(responseString);
                                    if (response.getIntValue("code") == 0) {
                                        String url = response.getString("url");
                                        RequestParams requestParams = new RequestParams(uploadType, url);
                                        upload(requestParams);
                                    } else {
                                        // Log.d(TAG, "onSuccess: 有上传文件失败");
                                    }
                                } catch (Exception e) {
                                    closeLoadingDialog();
                                    showToast("提交数据失败!" + e.getMessage());
                                }
                            }

                            @Override
                            protected void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                LogUtils.d("oss onFailure response = " + responseString);
                                closeLoadingDialog();
                                showToast("提交数据失败!" + responseString);
                            }

                        });
                    } catch (Exception e) {
                        closeLoadingDialog();
                        showToast("提交数据失败!" + e.getMessage());
                    }
                } else {
                    closeLoadingDialog();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                LogUtils.d("sign onFailure response = " + responseString);
                closeLoadingDialog();
            }
        });
    }

    /**
     * 添加个人主页信息数据
     *
     * @param requestParams
     */
    private void upload(RequestParams requestParams) {
        showLoadingDialog();
        HttpUtils.post(Constants.GET_ADD_USER_HOME, MyInformationActivity.this, requestParams, new HttpUtils.TextHttpResponseHandler() {
            @Override
            public void onFinish() {
                closeLoadingDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("upload onSuccess responseString = " + responseString);
                com.alibaba.fastjson.JSONObject response = com.alibaba.fastjson.JSONObject.parseObject(responseString);
                if (response.getIntValue("code") == 0) {
                    changed = true;
                    integrity_rate = response.getString("integrity_rate");
                    userHomePageBean.integrity_rate = integrity_rate;
                    updateRate();
                    if (requestParams.has("auth_icon")) {
                        updateImage(avaterFile);
                    }
                } else {
                    String msg = response.getString("msg");
                    if (!TextUtils.isEmpty(msg)) {
                        showToast(msg);
                    }
                }
            }
        });
    }

    private void updateImage(File file) {
        Glide.with(this).load(file).into(iv_avater);
    }

    private void updateRate() {
        StringBuilder stringBuilder = new StringBuilder(getString(R.string.homepage_data_complete));
        integrity_rate = userHomePageBean.integrity_rate;
        stringBuilder.append(integrity_rate);
        stringBuilder.append("%");
        tv_done_rate.setText(stringBuilder.toString());
    }

}
