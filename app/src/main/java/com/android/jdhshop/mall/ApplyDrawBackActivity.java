package com.android.jdhshop.mall;

import android.Manifest;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.android.jdhshop.common.LogUtils;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.malladapter.GridViewAddImgesAdpter;
import com.android.jdhshop.widget.ImageSelectDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;
import me.drakeet.materialdialog.MaterialDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class ApplyDrawBackActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.txt_order_num)
    TextView txtOrderNum;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.grid_view)
    GridView gridView;
    private Unbinder unbinder;
    private MaterialDialog materialDialog;
    private GridViewAddImgesAdpter addImgesAdpter;
    private List<Map<String, Object>> datas=new ArrayList<>();
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_drawback_money);
        unbinder = ButterKnife.bind(this);
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("申请退款");
        txtOrderNum.setText("订单编号: " + getIntent().getExtras().getString("order_num"));
        addImgesAdpter=new GridViewAddImgesAdpter(datas,this);
        gridView.setAdapter(addImgesAdpter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                permision(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA});
            }
        });
    }
    private void permision(String[] permission){
        //读取sd卡的权限
        if (EasyPermissions.hasPermissions(this, permission)) {
            new ImageSelectDialog(getComeActivity(), R.style.ActionSheetDialogStyle).setOnImageSelectDialogListener(new ImageSelectDialog.onImageSelectDialogListener() {
                @Override
                public void onImageSelectResult(String picturePath) {
                    if (picturePath == null || "".equals(picturePath))
                        return;
                    Map<String,Object> map=new HashMap<>();
                    map.put("path",picturePath);
                    datas.add(map);
                    addImgesAdpter.notifyDataSetChanged();
                }
            }).show();
        } else {
            EasyPermissions.requestPermissions(this, "此操作需要获取手机的权限",1, permission);
        }
    }
    @Override
    protected void initData() {
        materialDialog = new MaterialDialog(this);
        materialDialog.setTitle("申请退款")
                .setMessage("申请退款后，不可撤销申请，确定申请退款吗?")
                .setNegativeButton("申请退款", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestCancle();
                        materialDialog.dismiss();
                    }
                }).setPositiveButton("再想想", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        }).setCanceledOnTouchOutside(true);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_left, R.id.btn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.btn_ok:
                if (TextUtils.isEmpty(etContent.getText().toString().trim())) {
                    showToast("请填写申请退款原因");
                    return;
                }
                materialDialog.show();
                break;
        }
    }

    private void requestCancle() {
        try {
            RequestParams requestParams = new RequestParams();
            requestParams.put("order_id", getIntent().getExtras().getString("order_id"));
            requestParams.put("drawback_reason",etContent.getText().toString().trim());
            if(datas.size()>0){
                for(int i=0;i<datas.size();i++){
                    try {
                        requestParams.put("drawback_img["+i+"]",new File(datas.get(i).get("path").toString()));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            LogUtils.d("sdfasdf",requestParams.toString());
            HttpUtils.postUpload("1".equals(Constants.MALL_ORDER_TYPE)?Constants.APPLY_TUIKUAN_UPDATE:Constants.APPLY_TUIKUAN, ApplyDrawBackActivity.this,requestParams, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

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
                    try {
                        JSONObject jsonObject = new JSONObject(responseString);
                        String msg = jsonObject.optString("msg");
                        if (jsonObject.getInt("code") == 0) {
                            showToast("申请成功,后台审核后款将自动退回");
                            finish();
                        }else{
                            showToast(msg);
                        }
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
}
