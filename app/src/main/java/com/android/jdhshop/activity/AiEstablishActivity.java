package com.android.jdhshop.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.widget.ImageSelectDialog;
import com.bumptech.glide.Glide;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * 建群升级页面
 */
public class AiEstablishActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.establish_text_qunhao)
    TextView establish_text_qunhao;
    @BindView(R.id.establish_text_fuzhiqunhao)
    TextView establish_text_fuzhiqunhao;
    @BindView(R.id.establish_text_fuzhiwexin)
    TextView establish_text_fuzhiwexin;
    @BindView(R.id.establish_text_chakan)
    TextView establish_text_chakan;
    @BindView(R.id.shangchuan)
    ImageView shangchuan;
    @BindView(R.id.shangchuan2)
    ImageView shangchuan2;
    private String group_title;
    private String wx_service_user;
    private List<String> imageList = new ArrayList<>();
    private File avaterFile = null;
    private String url1 = "";
    private String url2 = "";

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_ai_establish);
        ButterKnife.bind(this);
        tv_left.setVisibility(View.VISIBLE);
        tv_title.setText("建群升级");


        BaseLogDZiYuan.LogDingZiYuan(shangchuan, "shangchaunimage.png");
        BaseLogDZiYuan.LogDingZiYuan(shangchuan2, "shangchaunimage.png");


    }

    @Override
    protected void initData() {
        getLinshiID();
    }


    private void getLinshiID() {
        String act = "";
        Intent intent = getIntent();

        act = intent.getStringExtra("action");

        if (act != null && act.equals("reRegWxGroup")) {
            // Log.d(TAG, "getLinshiID: 走进if");
            String tmpid = "";
            String str1 = "";
            String strweixinhao = "";
            String strchakan = "";

            tmpid = intent.getStringExtra("tmpid");
            group_title = intent.getStringExtra("group_title");
            wx_service_user = intent.getStringExtra("wx_service_user");

            str1 = "\t\t\t1.新拉或已有一个超50人的微信推广群,群名称修改为\t\"" + "<font color = '#DE4F4B'>" + group_title + "</font>" + "\"\t<font color = '#3DA0F4'><u>复制群名称</u></font>";
            strweixinhao = "\t\t\t也可添加导师微信咨询,导师微信号:\t\t<font color = '#DE4F4B'>" + wx_service_user + "</font>\t<font color = '#3DA0F4'><u>复制群名称</u></font>";
            strchakan = "\t\t\t2.上传两张截图(截图一:显示出群主和群人数截图二:显示出群名称)\t<font color = '#3DA0F4'><u>查看示例</u></font>";


            establish_text_qunhao.setText(tmpid);
            CharSequence charSequence;
            CharSequence charSequence_weixinhao;
            CharSequence charSequence_chakan;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                charSequence = Html.fromHtml(str1, Html.FROM_HTML_MODE_LEGACY);
                charSequence_weixinhao = Html.fromHtml(strweixinhao, Html.FROM_HTML_MODE_LEGACY);
                charSequence_chakan = Html.fromHtml(strchakan, Html.FROM_HTML_MODE_LEGACY);
            } else {
                charSequence = Html.fromHtml(str1);
                charSequence_weixinhao = Html.fromHtml(strweixinhao);
                charSequence_chakan = Html.fromHtml(strchakan);
            }
            establish_text_fuzhiqunhao.setText(charSequence);
            establish_text_fuzhiwexin.setText(charSequence_weixinhao);
            establish_text_chakan.setText(charSequence_chakan);
        } else {
            // Log.d(TAG, "getLinshiID: 走进else");
            RequestParams params = new RequestParams();
            HttpUtils.post(Constants.getTmpGroupID, AiEstablishActivity.this, params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseString);
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        if ("0".equals(code)) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            group_title = data.getString("group_title");
                            wx_service_user = data.getString("wx_service_user");
                            String tmp_id = data.getString("tmp_id");
                            Intent intent = getIntent();
                            String tmpid = intent.getStringExtra("tmpid");
                            if (tmpid.equals("3")) {
                                establish_text_qunhao.setText(tmp_id);
                            } else {
                                establish_text_qunhao.setText(tmpid);
                            }

                            String str1 = "\t\t\t1.新拉或已有一个超50人的微信推广群,群名称修改为\t\"" + "<font color = '#DE4F4B'>" + group_title + "</font>" + "\"\t<font color = '#3DA0F4'><u>复制群名称</u></font>";
                            String strweixinhao = "\t\t\t也可添加导师微信咨询,导师微信号:\t\t<font color = '#DE4F4B'>" + wx_service_user + "</font>\t<font color = '#3DA0F4'><u>复制群名称</u></font>";
                            String strchakan = "\t\t\t2.上传两张截图(截图一:显示出群主和群人数截图二:显示出群名称)\t<font color = '#3DA0F4'><u>查看示例</u></font>";

                            CharSequence charSequence;
                            CharSequence charSequence_weixinhao;
                            CharSequence charSequence_chakan;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                charSequence = Html.fromHtml(str1, Html.FROM_HTML_MODE_LEGACY);
                                charSequence_weixinhao = Html.fromHtml(strweixinhao, Html.FROM_HTML_MODE_LEGACY);
                                charSequence_chakan = Html.fromHtml(strchakan, Html.FROM_HTML_MODE_LEGACY);
                            } else {
                                charSequence = Html.fromHtml(str1);
                                charSequence_weixinhao = Html.fromHtml(strweixinhao);
                                charSequence_chakan = Html.fromHtml(strchakan);
                            }
                            establish_text_fuzhiqunhao.setText(charSequence);
                            establish_text_fuzhiwexin.setText(charSequence_weixinhao);
                            establish_text_chakan.setText(charSequence_chakan);
                        } else {
                            showToast(msg);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    //获取加密数据接口
    private void GetJIaMi(String file_name) {
        // Log.d(TAG, "GetJIaMi: " + file_name);
        RequestParams params = new RequestParams();
        params.put("file_name", file_name);
        params.put("oss_path_name", "regAssistant");
        params.put("channel_type", "2");
        HttpUtils.post(Constants.getHUOQUJIAMI, AiEstablishActivity.this, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    Log.d("ssssss", jsonObject + "");
                    String msg = jsonObject.getString("msg");
                    String code = jsonObject.getString("code");
                    JSONObject data = jsonObject.getJSONObject("data");
                    String accessid = data.getString("accessid");
                    String host = data.getString("host");
                    String policy = data.getString("policy");
                    String signature = data.getString("signature");
                    //String expire = data.getString("expire");
                    String callback = data.getString("callback");
                    String dir = data.getString("dir");
                    String pic_name = data.getString("pic_name");

                    // Log.d(TAG, "onSuccess:  " + dir + pic_name + "");

                    if (code.equals("0")) {
                        try {
                            String key_file = dir + "/" + pic_name;

//                            key_file = key_file.replace("/","//");

                            Log.d("rtttttt", avaterFile + "");
                            RequestParams params = new RequestParams();
                            params.put("OSSAccessKeyId", accessid + "");
                            params.put("callback", callback + "");
                            params.put("key", key_file);
                            params.put("policy", policy + "");
                            params.put("signature", signature + "");
                            params.put("success_action_status", "200");

                            File f = new File(file_name);
                            params.put("file", f.getName(), f);

                            HttpUtils.postUpload(host, AiEstablishActivity.this, params, new TextHttpResponseHandler() {
                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    Log.d("eeeeeeeeee", responseString + "");
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

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                    // Log.d(TAG, "onSuccess: " + responseString);
                                    try {
                                        JSONObject jsonObject = new JSONObject(responseString);
                                        int code = jsonObject.optInt("code");
                                        Log.d("code", statusCode + "--------" + responseString + "");
                                        String msg = jsonObject.optString("msg");

                                        String url = jsonObject.optString("url");
                                        Log.d("urlurlurl", url);
                                        //imageList.add(url);
                                        url1 = url;

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        } catch (Exception e) {
//                Toast.makeText(this, "上传失败", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //获取加密数据接口
    private void GetJIaMi1(String file_name) {
        // Log.d(TAG, "GetJIaMi: " + file_name);
        RequestParams params = new RequestParams();
        params.put("file_name", file_name);
        params.put("oss_path_name", "regAssistant");
        params.put("channel_type", "2");
        HttpUtils.post(Constants.getHUOQUJIAMI, AiEstablishActivity.this, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    Log.d("ssssss", jsonObject + "");
                    String msg = jsonObject.getString("msg");
                    String code = jsonObject.getString("code");
                    JSONObject data = jsonObject.getJSONObject("data");
                    String accessid = data.getString("accessid");
                    String host = data.getString("host");
                    String policy = data.getString("policy");
                    String signature = data.getString("signature");
                    //String expire = data.getString("expire");
                    String callback = data.getString("callback");
                    String dir = data.getString("dir");
                    String pic_name = data.getString("pic_name");

                    // Log.d(TAG, "onSuccess:  " + dir + pic_name + "");

                    if (code.equals("0")) {
                        try {
                            String key_file = dir + "/" + pic_name;

//                            key_file = key_file.replace("/","//");

                            Log.d("rtttttt", avaterFile + "");
                            RequestParams params = new RequestParams();
                            params.put("OSSAccessKeyId", accessid + "");
                            params.put("callback", callback + "");
                            params.put("key", key_file);
                            params.put("policy", policy + "");
                            params.put("signature", signature + "");
                            params.put("success_action_status", "200");

                            File f = new File(file_name);
                            params.put("file", f.getName(), f);

                            HttpUtils.postUpload(host, AiEstablishActivity.this, params, new TextHttpResponseHandler() {
                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    Log.d("eeeeeeeeee", responseString + "");
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

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                    // Log.d(TAG, "onSuccess: " + responseString);
                                    try {
                                        JSONObject jsonObject = new JSONObject(responseString);
                                        int code = jsonObject.optInt("code");
                                        Log.d("code", statusCode + "--------" + responseString + "");
                                        String msg = jsonObject.optString("msg");

                                        String url = jsonObject.optString("url");
                                        Log.d("urlurlurl", url);
                                        //imageList.add(url);
                                        url2 = url;
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        } catch (Exception e) {
//                Toast.makeText(this, "上传失败", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //判断文件是否存在
    public boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    //上传群信息图片审核接口
    private void PushImage() {
        String s = establish_text_qunhao.getText().toString();
        //Log.d("listlistlist", "[https://juduohui-s.oss-cn-shenzhen.aliyuncs.com/Upload/XhAssistant/CheckWxGroup/421/regAssistant_897281622020083.png,https://juduohui-s.oss-cn-shenzhen.aliyuncs.com/Upload/XhAssistant/CheckWxGroup/421/regAssistant_6027681622020091.png]");
        RequestParams params = new RequestParams();
        params.put("img", JSON.toJSONString(imageList));
        params.put("gid", s);
        Log.d("sssssssss", imageList + "");
        HttpUtils.post(Constants.getPushImage, AiEstablishActivity.this, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    String msg = jsonObject.getString("msg");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        showToast("上传成功等待审核");
                        finish();
                    } else {
                        showToast("上传的审核图片不正确");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String image1;
    private String image2;

    @OnClick({R.id.tv_left, R.id.shangchuan, R.id.shangchuan2, R.id.establish_text_fuzhiqunhao, R.id.establish_text_fuzhiwexin, R.id.set_huanying_geng, R.id.establish_text_chakan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.shangchuan:
                new ImageSelectDialog(getComeActivity(), R.style.ActionSheetDialogStyle).setOnImageSelectDialogListener(new ImageSelectDialog.onImageSelectDialogListener() {
                    @Override
                    public void onImageSelectResult(String picturePath) {
                        if (picturePath == null || "".equals(picturePath))

                            return;
                        Glide.with(getComeActivity()).load(picturePath).into(shangchuan);

                        GetJIaMi(picturePath + "");
                        shangchuan2.setVisibility(View.VISIBLE);
                    }
                }).show();
                break;
            case R.id.shangchuan2:
                new ImageSelectDialog(getComeActivity(), R.style.ActionSheetDialogStyle).setOnImageSelectDialogListener(new ImageSelectDialog.onImageSelectDialogListener() {
                    @Override
                    public void onImageSelectResult(String picturePath) {
                        if (picturePath == null || "".equals(picturePath))
                            return;
//
//                        Intent intent = new Intent(getComeActivity(), CropActivity.class);
//                        intent.putExtra("url", picturePath);
//                        startActivityForResult(intent, 1000);
                        Glide.with(getComeActivity()).load(picturePath).into(shangchuan2);

                        GetJIaMi1(picturePath + "");
                        Log.d("dddddddd", picturePath);

                    }
                }).show();
                break;
            case R.id.establish_text_fuzhiqunhao:
                ClipboardManager cmb = (ClipboardManager) AiEstablishActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(group_title.trim());
                T.showShort(AiEstablishActivity.this, "复制成功");
                break;
            case R.id.establish_text_fuzhiwexin:
                ClipboardManager cmb1 = (ClipboardManager) AiEstablishActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb1.setText(wx_service_user.trim());
                T.showShort(AiEstablishActivity.this, "复制成功");
                break;
            case R.id.set_huanying_geng://提交审核
                imageList.clear();
                if (!url1.equals("")) {
                    imageList.add(url1);
                }
                if (!url2.equals("")) {
                    imageList.add(url2);
                }
                if (imageList.size() == 1) {
                    showToast("必须上传两张图片");
                } else {
                    PushImage();
                }
                Log.d("rrrrrr", imageList + "");
                Log.d("rrrrrr", imageList.size() + "");
                //PushImage();
                break;
            case R.id.establish_text_chakan:
                openActivity(LookExamplesActivity.class);
                break;
        }

    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK&&requestCode==1000&&data!=null) {
//            avaterFile = new File(data.getStringExtra("url"));
//            Glide.with(getComeActivity()).load(data.getStringExtra("url")).into(shangchuan2);
////            imageList.add("\""+avaterFile+"\"");
////            imageList.add("\""+avaterFile+"\"");
//            GetJIaMi(avaterFile+"");
//        }
//    }

    @Override
    protected void initListener() {

    }
}
