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
 * ????????????
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
    CircleImageView iv_avater;//??????

    @BindView(R.id.et_one)
    AutoClearEditText et_one;//??????
    @BindView(R.id.et_two)
    TextView et_two;//?????????
    @BindView(R.id.ll_name)
    LinearLayout ll_name;//?????????

    @BindView(R.id.et_three)
    TextView et_three;//?????????

    @BindView(R.id.ll_phone)
    LinearLayout ll_phone;//?????????

    @BindView(R.id.et_four)
    AutoClearEditText et_four;//??????
    @BindView(R.id.et_five)
    AutoClearEditText et_five;//??????
    @BindView(R.id.et_six)
    TextView et_six;//QQ

    @BindView(R.id.ll_contact)
    LinearLayout ll_contact;//QQ

    @BindView(R.id.et_seven)
    AutoClearEditText et_seven;//????????????
    @BindView(R.id.tv_four)
    TextView tv_four;//??????

    @BindView(R.id.ll_birth)
    LinearLayout ll_birth;//??????

    @BindView(R.id.tv_address)
    TextView tv_address;

    @BindView(R.id.ll_area)
    LinearLayout ll_area;

    @BindView(R.id.et_address)
    AutoClearEditText et_adress;//?????????
    @BindView(R.id.rg_sex)
    RadioGroup rg_sex;//??????
    @BindView(R.id.cb_sex_man)
    RadioButton cb_sex_man;//???
    @BindView(R.id.cb_sex_woman)
    RadioButton cb_sex_woman;//???

    @BindView(R.id.ll_sex)
    LinearLayout ll_sex;//??????

    @BindView(R.id.tv_domain)
    TextView tv_domain;

    @BindView(R.id.ll_domain)
    RelativeLayout ll_domain;

    @BindView(R.id.tv_done_rate)
    TextView tv_done_rate;//???????????????


    @BindView(R.id.tv_sign)
    TextView tv_sign;//??????

    @BindView(R.id.ll_sign)
    LinearLayout ll_sign;//??????

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
    private String contact = "";//????????????????????????qq???
    private String phone = ""; //?????????
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

        //????????????drawableLeft??????
        Drawable drawable1 = getResources().getDrawable(R.mipmap.icon_back);
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
        tv_left.setCompoundDrawables(drawable1, null, null, null);
        tv_left.setVisibility(View.VISIBLE);


        tv_title.setText("????????????");
        tv_title.setTextColor(getResources().getColor(R.color.col_333));
        tv_right.setVisibility(View.GONE);
        tv_right.setText("??????");
        tv_right.setTextColor(getResources().getColor(R.color.red));
        cityDialog = new CityPickerView();
        cityDialog.init(this);
        //???????????????config??????????????????application?????????????????????????????????????????????????????????????????????????????????config
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
                case R.id.cb_sex_man://???
                {
                    cb_sex_man.isChecked();
                    RequestParams requestParams = new RequestParams("sex", 1);
                    upload(requestParams);
                    break;
                }
                case R.id.cb_sex_woman:// ???
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
                //?????????dialog????????????
                //new RTMultiCheckDialog(context????????????dialog??????????????????0.7
                RTMultiCheckDialog customDialog = new RTMultiCheckDialog(getComeActivity(), 0.7, 0.7)
                        .setTitleText("??????????????????3?????????")
                        .setConfirmText("??????")
                        .setCancelText("??????")
                        .setConfirmOnclicListener(rtMultiCheckDialog -> {
                            //??????????????????
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
                            //??????????????????
                            rtMultiCheckDialog.dismiss();
                        })
                        .setItemNames(itemList);
                //??????dialog
                customDialog.show();
                //????????????
                //?????????????????????????????????dialog??????????????????
                customDialog.setCancelInOutside(true);
                //???????????????????????????????????????
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
     * Base64???????????????
     *
     * @param content     -- ??????????????????
     * @param charsetName -- ?????????????????????
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
            case R.id.ll_avater: //????????????
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
            case R.id.tv_left: //??????
                quit();
                finish();
                break;
            case R.id.ll_area: //??????
                cityDialog.showCityPicker();
                break;
            case R.id.ll_birth: //????????????
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
     * ??????UI
     **/
    public void refreshUI() {
        if (null != userBean) {
            if (null != userBean.user_detail) {
                if (isBase64(userBean.user_detail.nickname == null ? "" : userBean.user_detail.nickname)) {
                    et_one.setText(base64Decode(userBean.user_detail.nickname, "utf-8"));
                } else {
                    et_one.setText(TypeConvertUtil.getString(userBean.user_detail.nickname, ""));//??????
                }
                et_two.setText(TypeConvertUtil.getString(userBean.user_detail.truename, ""));//?????????
                et_four.setText(TypeConvertUtil.getString(userBean.user_detail.height, ""));//??????
                et_five.setText(TypeConvertUtil.getString(userBean.user_detail.weight, ""));//??????
                et_six.setText(TypeConvertUtil.getString(userBean.user_detail.weixin, ""));//QQ
                et_seven.setText(TypeConvertUtil.getString(userBean.user_detail.signature, ""));//????????????
                tv_four.setText(TypeConvertUtil.getString(userBean.user_detail.birthday, ""));//??????
                if ("2".equals(userBean.user_detail.sex)) {
                    cb_sex_woman.setChecked(true);
                } else {
                    cb_sex_man.setChecked(true);
                }
                et_adress.setText(TypeConvertUtil.getString(userBean.user_detail.detail_address, ""));//??????
                if (!"".equals(TypeConvertUtil.getString(userBean.user_detail.province, ""))) {
                    if (isChinese(tv_address.getText().toString())) {
                    } else {
                        tv_address.setText(TypeConvertUtil.getString(userBean.user_detail.province, "") + "-" + TypeConvertUtil.getString(userBean.user_detail.city, "") + "-" + TypeConvertUtil.getString(userBean.user_detail.county, ""));//??????
                    }
                }
            }
            if (null != userBean.user_msg) {
                et_three.setText(TypeConvertUtil.getString(userBean.user_msg.phone, ""));//?????????
            }
            LogUtils.d(TAG, "refreshUI: userBean.user_detail.avatar = " + userBean.user_detail.avatar);

        }
    }

    /**
     * ??????????????????
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
                // ??????????????????
                LogUtils.e(TAG, "onFailure()--" + responseString);
            }

            @Override
            public void onFinish() {
                // ???????????????
                super.onFinish();
                closeLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.e(TAG, "onSuccess()--" + responseString);
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    //?????????
                    int code = jsonObject.optInt("code");
                    //???????????????
                    String msg = jsonObject.optString("msg");
                    String data = jsonObject.optString("data");
                    if (0 == code) {
                        if (!TextUtils.isEmpty(data)) {//GSON??????????????????double??????
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
                // ???????????????
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

    // ???????????????????????????????????????
    public boolean isChinese(String str) {
        if (str == null)
            return false;
        for (char a : str.toCharArray()) {
            if (isChinese(String.valueOf(a)))
                return true;// ??????????????????????????????
        }
        return false;
    }

//    private void editUserMsgRequest() {
//        if (TextUtils.isEmpty(getTextEditValue(et_one))) {
//            showToast("?????????????????????");
//            return;
//        }
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("token", token);
//        requestParams.put("nickname", getTextEditValue(et_one)); //????????????
//        requestParams.put("truename", getTextEditValue(et_two)); //????????????
//        requestParams.put("height", "312"); //??????
//        requestParams.put("weight", "123"); //??????
//        requestParams.put("blood", "1"); //?????? 1A??? 2B??? 3AB??? 4O??? 5??????
//        requestParams.put("birthday", getTextEditValue(tv_four)); //????????????
//        requestParams.put("weixin", getTextEditValue(et_six)); //?????????
//
//        requestParams.put("qq", "123"); //?????????
//        requestParams.put("expiration_date", "123");
//        requestParams.put("balance_plantform", "123");
//        requestParams.put("balance_service", "123");
//        requestParams.put("balance_user", "123");
//        requestParams.put("email", "123");
//        requestParams.put("username", "123");
//
//
//        if (!"".equals(tv_address.getText().toString())) {
//            requestParams.put("province", tv_address.getText().toString().split("-")[0]); //??????
//            requestParams.put("city", tv_address.getText().toString().split("-")[1]); //??????
//            requestParams.put("county", tv_address.getText().toString().split("-")[2]); //???/??????
//        }
//        requestParams.put("detail_address", et_adress.getText().toString()); //????????????
//        requestParams.put("signature", "11111"); //????????????
//        //?????? 1??? 2??? 3??????
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
//                        //??????????????????
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
     * ??????????????????
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
     * @param bgFile      ??????
     * @param ossPathName oss??????
     * @param uploadType  ??????????????????
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
                                        // Log.d(TAG, "onSuccess: ?????????????????????");
                                    }
                                } catch (Exception e) {
                                    closeLoadingDialog();
                                    showToast("??????????????????!" + e.getMessage());
                                }
                            }

                            @Override
                            protected void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                LogUtils.d("oss onFailure response = " + responseString);
                                closeLoadingDialog();
                                showToast("??????????????????!" + responseString);
                            }

                        });
                    } catch (Exception e) {
                        closeLoadingDialog();
                        showToast("??????????????????!" + e.getMessage());
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
     * ??????????????????????????????
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
