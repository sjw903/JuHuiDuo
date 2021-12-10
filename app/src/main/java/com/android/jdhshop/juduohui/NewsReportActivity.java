package com.android.jdhshop.juduohui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.juduohui.response.HttpJsonResponse;
import com.android.jdhshop.utils.UIUtils;
import com.android.jdhshop.widget.ImageSelectDialog;
import com.bumptech.glide.Glide;
import com.loopj.android.http.RequestParams;
import com.zhy.view.flowlayout.FlowLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import me.panpf.sketch.SketchImageView;

public class NewsReportActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.jb_pub_user)
    TextView jb_pub_user;

    @BindView(R.id.jb_news_title)
    TextView jb_news_title;
    @BindView(R.id.jb_news_desc)
    TextView jb_news_desc;
    @BindView(R.id.pub_header)
    ImageView pub_header;

    @BindView(R.id.no_use_4)
    TextView no_use_4;
    @BindView(R.id.notice_layer)
    RelativeLayout notice_layer;

    @BindView(R.id.jb_yuanyin_list)
    RadioGroup jb_yuanyin_list;

    @BindView(R.id.upload_image_box)
    FlowLayout upload_image_box;
    @BindView(R.id.jb_miaoshu_edit)
    EditText jb_miaoshu_edit;
    @BindView(R.id.up_img_count)
    TextView up_img_count;
    @BindView(R.id.jb_miaoshu_count)
    TextView jb_miaoshu_count;
    @BindView(R.id.upload_image_button)
    ImageView upload_image_button;
    private Drawable i_dui_gou = null;
    private static final int REQUEST_IMAGE_UPLOAD = 102;
    private static final int MAX_UPLOAD_PIC_NUMBER = 9;
    private static final int MAX_MIAOSHU_NUMBER = 200;
    private Context mContext;


    private final List<String> upload_img_list = new ArrayList<String>() {
        @Override
        public boolean add(String s) {
            boolean result = super.add(s);
            uploadImageResize();
            return result;
        }

        @Override
        public String remove(int index) {
            // Log.d(TAG, "remove: " + index);
            uploadImageResize();
            return super.remove(index);
        }

        @Override
        public boolean remove(Object o) {
            // Log.d(TAG, "remove: " + o);
            boolean result = super.remove(o);
            uploadImageResize();

            return result;
        }
    };
    private final JSONArray uploaded_image = new JSONArray(); // 已上传的文件URL列表
    private final String TAG = getClass().getSimpleName();
    private JSONObject config;

    private void uploadImageResize() {
        // Log.d(TAG, "uploadImageResize: " + upload_img_list.size() + " , " + MAX_UPLOAD_PIC_NUMBER);
        if (upload_img_list.size() >= MAX_UPLOAD_PIC_NUMBER) {
            upload_image_button.setVisibility(View.GONE);
        } else {
            if (upload_image_button.getVisibility() == View.GONE) {
                upload_image_button.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_news_report);
        ButterKnife.bind(this);

        mContext = this;
        i_dui_gou = getResources().getDrawable(R.drawable.news_duigou);
        i_dui_gou.setBounds(0, 0, 48, 48);
        i_dui_gou.setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.SRC_IN);
        tv_left.setVisibility(View.VISIBLE);
        tv_title.setText("举报");


        final ImageView imageView = findViewById(R.id.no_use_1);
        LayerDrawable drawable = (LayerDrawable) imageView.getDrawable();
        Drawable layerDrawable1 = drawable.findDrawableByLayerId(R.id.duihao);
        layerDrawable1.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
    }

    @Override
    protected void initData() {
        Intent i = getIntent();
        if (i != null) {
            // Log.d(TAG, "initData: " + i.getStringExtra("config"));
            try {
                config = JSONObject.parseObject(i.getStringExtra("config"));
                jb_news_title.setText(config.getString("title"));
                jb_news_desc.setText(config.getString("desc"));
                String t = config.getString("media_info");
                JSONObject media_info = JSONObject.parseObject(t);
//                JSONObject author_info = media_info.getJSONObject("media_info");

                String display_img_src = "";
                String img_list = config.getString("image_list");
                if (img_list.startsWith("{")) {
                    img_list = "[" + img_list + "]";
                }

                try {
                    JSONArray j = JSONArray.parseArray(img_list);
                    display_img_src = j.getJSONObject(0).getString("url");
                } catch (Exception e) {
                    display_img_src = media_info.getString("avatar_url");
                }

                Glide.with(this).load(display_img_src).asBitmap().into(pub_header);
                jb_pub_user.setText(media_info.containsKey("nickname") ? media_info.getString("nickname") : "秋风落叶"); //media_info.getString("name"));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        HttpUtils.post(Constants.MEDIA_LIB_REPORT_OPTION, NewsReportActivity.this,new RequestParams(), new HttpJsonResponse() {
            @Override
            protected void onSuccess(JSONObject response) {
                super.onSuccess(response);
                if (response.getIntValue("code") == 0) {
                    JSONArray option_list = response.getJSONArray("list");
                    for (Object option : option_list) {
                        JSONObject item = (JSONObject) option;
                        RadioButton radioButton = new RadioButton(mContext);
                        RadioGroup.LayoutParams lm = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        radioButton.setText(item.getString("report_str"));
                        radioButton.setTag(item.getString("id"));
                        radioButton.setBackgroundResource(R.drawable.bottom_border_10);
                        radioButton.setButtonDrawable(null);
                        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                                if (isChecked) {
                                    i_dui_gou.setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.SRC_IN);
                                    v.setCompoundDrawables(null, null, i_dui_gou, null);
                                    v.setTextColor(Color.parseColor("#FF0000"));
                                } else {
                                    v.setCompoundDrawables(null, null, null, null);
                                    v.setTextColor(Color.parseColor("#000000"));
                                }
                            }
                        });
                        radioButton.setLayoutParams(lm);
                        jb_yuanyin_list.addView(radioButton);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);
                showToast("获取配置信息失败，请返回后重试！");
                finish();
            }
        });
    }

    View.OnClickListener selected_pic_listen = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Log.d(TAG, "selected_pic_listen onClick: " + v.getTag());
            upload_image_box.removeView(v);
            upload_img_list.remove(v.getTag());
            up_img_count.setText(String.format("%d/%d", upload_img_list.size(), MAX_UPLOAD_PIC_NUMBER));
        }
    };

    @OnClick({R.id.upload_image_button, R.id.bottom_submit_bt, R.id.jb_miaoshu_edit, R.id.no_use_4, R.id.tv_left})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.no_use_4:
            case R.id.tv_left:
                finish();
                break;
            case R.id.jb_miaoshu_edit:
                // Log.d(TAG, "OnClick: focusable");
                jb_miaoshu_edit.setFocusable(true);
                jb_miaoshu_edit.requestFocus();
                jb_miaoshu_edit.requestFocus();
                break;
            case R.id.upload_image_button:
                new ImageSelectDialog(getComeActivity(), R.style.ActionSheetDialogStyle).setOnImageSelectDialogListener(new ImageSelectDialog.onImageSelectDialogListener() {
                    @Override
                    public void onImageSelectResult(String picturePath) {

                        if (picturePath == null || "".equals(picturePath))
                            return;
                        // Log.d(TAG, "onImageSelectResult: " + picturePath);
                        if (upload_img_list.contains(picturePath)) {
                            showToast("请不要添加重复的照片");
                            return;
                        }

                        upload_img_list.add(picturePath);
                        up_img_count.setText(String.format("%d/%d", upload_img_list.size(), MAX_UPLOAD_PIC_NUMBER));
                        SketchImageView imageView = new SketchImageView(mContext);
                        imageView.getOptions().setThumbnailMode(true).setResize(UIUtils.dp2px(80), UIUtils.dp2px(80));
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                        BitmapDrawable bitmapDrawable = (BitmapDrawable) BitmapDrawable.createFromPath(picturePath);
//                        imageView.setImageDrawable(bitmapDrawable);
                        imageView.displayContentImage(picturePath);
                        imageView.setTag(picturePath);
                        imageView.setOnClickListener(selected_pic_listen);

                        LinearLayout.LayoutParams lm = new LinearLayout.LayoutParams(UIUtils.dp2px(80), UIUtils.dp2px(80));
                        lm.setMargins(0, 0, UIUtils.dp2px(5), UIUtils.dp2px(5));
                        imageView.setLayoutParams(lm);
                        upload_image_box.addView(imageView);

                    }
                }).show();
                break;
            case R.id.bottom_submit_bt:
                submitForm();
                break;
        }
    }

    @Override
    protected void initListener() {
        jb_miaoshu_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > MAX_MIAOSHU_NUMBER) {
                    s.delete(MAX_MIAOSHU_NUMBER, s.length());
                }
                jb_miaoshu_count.setText(s.length() + "/" + MAX_MIAOSHU_NUMBER + "字");
            }
        });
    }

    private void submitFormData() {
        String miaoshu = jb_miaoshu_edit.getText().toString();
        RadioButton rb = findViewById(jb_yuanyin_list.getCheckedRadioButtonId());

        RequestParams r = new RequestParams();
        r.put("media_id", config.getString("id"));
        r.put("report_id", rb.getTag());
        r.put("problem_str", miaoshu);
        if (uploaded_image.size() > 0) r.put("voucher_url", uploaded_image.toJSONString());

        // 上传暂时不做
        HttpUtils.post(Constants.MEDIA_LIB_SET_REPORT,NewsReportActivity.this, r, new HttpJsonResponse() {
            @Override
            protected void onSuccess(JSONObject response) {
                super.onSuccess(response);
                closeLoadingDialog();
                if (response.getIntValue("code") == 0) {
                    notice_layer.setVisibility(View.VISIBLE);
                } else {
                    showToast(response.getString("msg"));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);
                closeLoadingDialog();
                showToast("提交数据失败!" + e.getMessage());
            }
        });
    }

    private void submitForm() {

        if (jb_yuanyin_list.getCheckedRadioButtonId() == -1) {
            showToast("请勾选举报原因！");
            return;
        }

        showLoadingDialog("数据提交中...");

        // 上传图片逻辑
        List<String> tmp_name = new ArrayList<>();
        String uid = SPUtils.getStringData(this, "uid", "");
        long current_timer = System.currentTimeMillis();

        for (int i = 0; i < upload_img_list.size(); i++) {
            tmp_name.add(uid + "_" + current_timer + (current_timer + i) + ".png");
        }

        if (upload_img_list.size()<=0){
            submitFormData();
            return;
        }


        RequestParams sign_req = new RequestParams();
        sign_req.put("file_name", tmp_name.toString().replace("[", "").replace("]", ""));
        sign_req.put("oss_path_name", "userHomebg");
        sign_req.put("channel_type", "2");

        HttpUtils.post(Constants.GET_UPLOAD_SIGN, NewsReportActivity.this,sign_req, new HttpJsonResponse() {
            @Override
            protected void onSuccess(JSONObject response) {
                super.onSuccess(response);
                if (response.getIntValue("code") == 0) {
                    String tmp_sign_info_str = response.getString("data");
                    if (upload_img_list.size() == 1) {
                        // 返回的是数组
                        tmp_sign_info_str = "[" + tmp_sign_info_str + "]";
                    }
                    JSONArray signed_file_name = JSONArray.parseArray(tmp_sign_info_str);
                    for (int i = 0; i < signed_file_name.size(); i++) {
                        // Log.d(TAG, "onSuccess: 上传第" + i + "个文件。文件名" + signed_file_name.getJSONObject(i).getString("pic_name"));
                        try {
                            RequestParams tmp_req = new RequestParams();
                            tmp_req.put("OSSAccessKeyId", signed_file_name.getJSONObject(i).getString("accessid"));
                            tmp_req.put("callback", signed_file_name.getJSONObject(i).getString("callback"));
                            tmp_req.put("key", signed_file_name.getJSONObject(i).getString("dir") + "/" + signed_file_name.getJSONObject(i).getString("pic_name"));
                            tmp_req.put("policy", signed_file_name.getJSONObject(i).getString("policy"));
                            tmp_req.put("signature", signed_file_name.getJSONObject(i).getString("signature"));
                            tmp_req.put("success_action_status", "200");
                            tmp_req.put("file", tmp_name.get(i), new File(upload_img_list.get(i)));
                            HttpUtils.postUpload(signed_file_name.getJSONObject(i).getString("host"),NewsReportActivity.this, tmp_req, new HttpUtils.TextHttpResponseHandler() {
                                @Override
                                protected void onSuccess(int statusCode, Header[] headers, String responseString) {
                                    super.onSuccess(statusCode, headers, responseString);
                                    // Log.d(TAG, "onSuccess: " + responseString);
                                    try {
                                        JSONObject response = JSONObject.parseObject(responseString);
                                        if (response.getIntValue("code") == 0) {
                                            uploaded_image.add(response.getString("url"));
                                            if (uploaded_image.size() == upload_img_list.size()) {
                                                // Log.d(TAG, "onSuccess: 全部文件上传成功");
                                                // Log.d(TAG, "onSuccess: " + uploaded_image.toJSONString());
                                                submitFormData();
                                            }
                                        } else {
                                            // Log.d(TAG, "onSuccess: 有上传文件失败");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        closeLoadingDialog();
                                        showToast("提交数据失败!" + e.getMessage());
                                    }
                                }

                                @Override
                                protected void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    super.onFailure(statusCode, headers, responseString, throwable);
                                    // Log.d(TAG, "onFailure: " + responseString);
                                    closeLoadingDialog();
                                    showToast("提交数据失败!" + responseString);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            closeLoadingDialog();
                            showToast("提交数据失败!" + e.getMessage());
                        }
                    }
                }
                else{
                    closeLoadingDialog();
                    showToast(response.getString("msg"));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);
                closeLoadingDialog();
                showToast("提交数据失败!" + responseString);
            }
        });
    }
}